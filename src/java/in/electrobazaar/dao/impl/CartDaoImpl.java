/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.electrobazaar.dao.impl;


import in.electrobazaar.dao.CartDao;
import in.electrobazaar.pojo.CartPojo;
import in.electrobazaar.pojo.DemandPojo;
import in.electrobazaar.utility.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HP
 */
public class CartDaoImpl implements CartDao {

    @Override
    public String addProductToCart(CartPojo cart){
        String status="Filed to add into cart!";
        Connection conn=DBUtil.provideConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;
    
        try{
            ps=conn.prepareStatement("Select * from USERCART where useremail=? and prodid=? ");
            ps.setString(1, cart.getUseremail());
            ps.setString(2, cart.getProdId());
            rs=ps.executeQuery();
            if(rs.next()){
                ProductDaoImpl prodDao=new ProductDaoImpl();
                int stockQty=prodDao.getProductQuantity(cart.getProdId());
                int newQty=cart.getQuantity()+rs.getInt("quantity");
                if(stockQty<newQty){
                     cart.setQuantity(stockQty);
                     updateProductInCart(cart);
                     status="Only "+stockQty+" no of items are avaiailble in our stock so we are adding "+stockQty+" in your cart";
                     DemandPojo demandPojo=new DemandPojo();
                     demandPojo.setProdId(cart.getProdId());
                     demandPojo.setUseremail(cart.getUseremail());
                     demandPojo.setDemandQuantity(newQty-stockQty);
                     DemandDaoImpl demandImpl=new DemandDaoImpl();
                     boolean result=demandImpl.addProduct(demandPojo);
                     if(result==true){
                         status="We eill mail you when "+(newQty-stockQty)+" no of items will be avaialble";
                     }
                }else{
                    cart.setQuantity(newQty);
                    status=updateProductInCart(cart);
                }
            }
        }
        catch(SQLException ex){
            status="Addition failed due to exception";
            System.out.println("Error in addProductToCart method"+ex);
            ex.printStackTrace();
        }
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(ps);
            return status;
    
    }

    @Override
    public String updateProductInCart(CartPojo cart) {
        String status="Filed to add to cart!";
        Connection conn=DBUtil.provideConnection();
        PreparedStatement ps1=null;
        PreparedStatement ps2=null;
        ResultSet rs=null;
        int ans=0;
        try{
            ps1=conn.prepareStatement("Select * from USERCART where prodid=? and useremail=?");
            ps1.setString(1, cart.getProdId());
            ps1.setString(2, cart.getUseremail());
            rs=ps1.executeQuery();
            if(rs.next()){
                int qty=cart.getQuantity();
                if(qty>0){
                    ps2=conn.prepareStatement("Update USERCART set quantity=? where useremail=? and prodid=?");
                    ps2.setInt(1, cart.getQuantity());
                    ps2.setString(2, cart.getUseremail());
                    ps2.setString(3,cart.getProdId());
                    ans=ps2.executeUpdate();
                    if(ans>0){
                        status="Product Successfully updated in the cart";
                    }else{
                        status="Could not update the Product ";
                   
                    }
                    
                }
                else if(qty==0){
                    ps2=conn.prepareStatement("Delete from USERCART where useremail=? and prodid=?");
                    ps2.setString(1, cart.getUseremail());
                    ps2.setString(2,cart.getProdId());
                    ans=ps2.executeUpdate();
                    if(ans>0){
                        status="Product Successfully updated in the cart";
                    }
                    else{
                        status="Could not delete the Product ";
                   
                    }
                }
            }
            
            else{
                ps2=conn.prepareStatement("Insert into USERCART values(?,?,?)");
                
                ps2.setString(1, cart.getUseremail());
                ps2.setString(2,cart.getProdId());
                ps2.setInt(3, cart.getQuantity());
                ans=ps2.executeUpdate();
                if(ans>0){
                    status="Product successfully added into the cart";
                }
                else{
                        status="Could not add the Product ";
                   
                    }
            }
        } catch(SQLException ex){
            status="Updation failed due to exception";
            System.out.println("Error in updateProductInCart method"+ex);
            ex.printStackTrace();
        }
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(ps1);
            DBUtil.closeStatement(ps2);
            return status;
    }

    @Override
    public List<CartPojo> getAllCartItems(String userId) {
        List<CartPojo> itemList= new ArrayList<>();
        Connection conn=DBUtil.provideConnection();
        PreparedStatement ps=null;
       
        ResultSet rs=null;
        try{
            ps=conn.prepareStatement("Select * from USERCART where useremail=?");
            ps.setString(1,userId);
            rs=ps.executeQuery();
            while(rs.next()){
                CartPojo cart= new CartPojo();
                cart.setProdId(rs.getString("prodid"));
                cart.setUseremail(rs.getString("useremail"));
                cart.setQuantity(rs.getInt("quantity"));
                itemList.add(cart);
            }
        }catch(SQLException ex){
            System.out.println("Error in getAllCartItems method"+ex);
            ex.printStackTrace();
        }
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(ps);
            
            return itemList;
     }

    @Override
    public int getCartItemCount(String userId, String itemId) {
        if(userId==null || itemId==null){
            return 0;
        }
        Connection conn=DBUtil.provideConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;
        int count=0;
        try{
            ps=conn.prepareStatement("Select quantity from USERCART  where useremail=? and prodid=?");
            ps.setString(1, userId);
            ps.setString(2,itemId);
            rs=ps.executeQuery();
            if(rs.next()){
                count=rs.getInt(1);
            }
        }catch(SQLException ex){
           
            System.out.println("Error in getCartItemCount method"+ex);
            ex.printStackTrace();
        }
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(ps);
           
            return count;
            
      }

    @Override
    public String removeProductFromCart(String userId, String prodId) {
        String status="Product Removal Failed!";
        Connection conn=DBUtil.provideConnection();
        PreparedStatement ps1=null;
        PreparedStatement ps2=null;
        ResultSet rs=null;
        try{
            ps1=conn.prepareStatement("Select * from USERCART where useremail=? and prodid=?");
            ps1.setString(1,userId);
            ps1.setString(2, prodId);
            rs=ps1.executeQuery();
            if(rs.next()){
                int prodQty=rs.getInt("quantity");
                prodQty--;
                if(prodQty>0){
                    ps2=conn.prepareStatement("Update USERCART set quantity=? where useremail=? and prodid=?");
                    ps2.setInt(1, prodQty);
                    ps2.setString(2, userId);
                    ps2.setString(3, prodId);
                    int k=ps2.executeUpdate();
                    if(k>0){
                        status="Product successfully removed from the cart";
                    }
                }else{
                    ps2=conn.prepareStatement("Delete from USERCART where useremail=? and prodid=?");
                   
                    ps2.setString(1, userId);
                    ps2.setString(2, prodId);
                    int k=ps2.executeUpdate();
                    if(k>0){
                        status="Product successfully removed from the cart";
                        
                    }  
                }  
            }
        }
        catch(SQLException ex){
            status="Removal failed due to exception";
            System.out.println("Error in removeProductFromCart method"+ex);
            ex.printStackTrace();
        }
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(ps1);
            DBUtil.closeStatement(ps2);
            return status;
    }

    @Override
    public Boolean removeAProduct(String userId, String prodId) {
        boolean result=false;
        Connection conn=DBUtil.provideConnection();
        PreparedStatement ps=null;
        
        try{
            ps=conn.prepareStatement("Delete from USERCART where useremail=? and prodid=?");
            ps.setString(1,userId);
            ps.setString(2,prodId);
            int k=ps.executeUpdate();
            if(k>0){
                result=true;
            }
        }catch(SQLException ex){
            System.out.println("Error in removedAProduct method"+ex);
            ex.printStackTrace();
        }
            
            DBUtil.closeStatement(ps);
            
            return result;
        
       
    }
    
}
