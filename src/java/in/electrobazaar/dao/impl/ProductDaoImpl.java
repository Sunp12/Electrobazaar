/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.electrobazaar.dao.impl;


import in.electrobazaar.dao.ProductDao;
import in.electrobazaar.pojo.DemandPojo;
import in.electrobazaar.pojo.ProductPojo;
import in.electrobazaar.utility.DBUtil;
import in.electrobazaar.utility.IDUtil;
import in.electrobazaar.utility.MailMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.mail.MessagingException;

/**
 *
 * @author HP
 */
public class ProductDaoImpl implements ProductDao{

    @Override
    public String addProduct(ProductPojo product) {
      String status ="Product Registration Failed";
      if(product.getProdId()==null){
          product.setProdId(IDUtil.generateProdId());
      }
      Connection conn=DBUtil.provideConnection();
        PreparedStatement ps=null;
        try{
            ps=conn.prepareStatement("Insert into products values(?,?,?,?,?,?,?,?)");
            ps.setString(1, product.getProdId());
            ps.setString(2, product.getProdName());
            ps.setString(3, product.getProdType());
            ps.setString(4, product.getProdInfo());
            ps.setDouble(5, product.getProdPrice());
            ps.setInt(6,product.getProdQuantity() );
            ps.setBinaryStream(7, product.getProdImage(),(int)product.getSize());
            ps.setString(8, "y");
            int count=ps.executeUpdate();
            if(count==1){
                status="Product Added successfully with id :"+product.getProdId();
               
            }
        }catch(SQLException ex){
            System.out.println("Error in addProduct method"+ex);
            ex.printStackTrace();
        }
        
            DBUtil.closeStatement(ps);
            return status;
    }

    @Override
    public String updateProduct(ProductPojo prevProduct, ProductPojo updatedProduct) {
        String status="Product upadation failed";
        if(!prevProduct.getProdId().equals(updatedProduct.getProdId())){
            status="Product ID's Do Not Match. Upadation Failed";
            return status;
        }
        Connection conn=DBUtil.provideConnection();
        PreparedStatement ps=null;
        try{
            ps=conn.prepareStatement("Update products Set pname=?,ptype=?,pinfo=?,pprice=?,pquantity=?,image=? WHERE pid=?");
           
            ps.setString(1, updatedProduct.getProdName());
            ps.setString(2, updatedProduct.getProdType());
            ps.setString(3, updatedProduct.getProdInfo());
            ps.setDouble(4, updatedProduct.getProdPrice());
            ps.setInt(5,updatedProduct.getProdQuantity() );
            ps.setBinaryStream(6,updatedProduct.getProdImage(),(int)updatedProduct.getSize());
             ps.setString(7, updatedProduct.getProdId());
            int count=ps.executeUpdate();
            if(count==1){
                status="Product successfully updated";
               
            }
        }catch(SQLException ex){
            System.out.println("Error in updateProduct method"+ex);
            ex.printStackTrace();
        }
        
            DBUtil.closeStatement(ps);
            return status;
    }

    @Override
    public String updateProductPrice(String prodId, double updatedPrice) {
        String status="Price Updation Failed";
    
        Connection conn=DBUtil.provideConnection();
        PreparedStatement ps=null;
        try{
            ps=conn.prepareStatement("Update products Set pprice=? WHERE pid=?");
            ps.setDouble(1, updatedPrice);
            ps.setString(2, prodId);
            int count=ps.executeUpdate();
            if(count==1){
                status="Product successfully updated";
            }
            }catch(SQLException ex){
                status="Error:"+ex.getMessage();
                System.out.println("Error in updateProductPrice method"+ex);
                ex.printStackTrace();
        }
        
            DBUtil.closeStatement(ps);
            return status;
    }

    @Override
    public List<ProductPojo> getAllProducts() {
        Connection conn=DBUtil.provideConnection();
        Statement st=null;
        ResultSet rs=null;
        List<ProductPojo> productList=new ArrayList<>();
        try{
            st=conn.createStatement();
            rs=st.executeQuery("Select * from products where available='Y'");
            while(rs.next()){
                ProductPojo prod=new ProductPojo();
                prod.setProdId(rs.getString("pid"));
                prod.setProdName(rs.getString("pname"));
                prod.setProdInfo(rs.getString("pinfo"));
                prod.setProdType(rs.getString("ptype"));
                prod.setProdPrice(rs.getDouble("pprice"));
                prod.setProdQuantity(rs.getInt("pquantity"));
                prod.setProdImage(rs.getAsciiStream("image"));
                productList.add(prod);
                
            }
        }catch(SQLException ex){
            System.out.println("Error in getAllProducts method"+ex);
            ex.printStackTrace();
        }
        
            DBUtil.closeStatement(st);
            DBUtil.closeResultSet(rs);
            return productList;
        
    }

    @Override
    public List<ProductPojo> getAllProductsByType(String type) {
        Connection conn=DBUtil.provideConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;
        List<ProductPojo> productList=new ArrayList<>();
        try{
           ps=conn.prepareStatement("Select * from products where ptype like ?");
           ps.setString(1, "%"+type+"%");
           rs=ps.executeQuery();
           while(rs.next()){
                ProductPojo prod=new ProductPojo();
                prod.setProdId(rs.getString("pid"));
                prod.setProdName(rs.getString("pname"));
                prod.setProdInfo(rs.getString("pinfo"));
                prod.setProdType(rs.getString("ptype"));
                prod.setProdPrice(rs.getDouble("pprice"));
                prod.setProdQuantity(rs.getInt("pquantity"));
                prod.setProdImage(rs.getAsciiStream("image"));
                productList.add(prod);
                
           }
        }catch(SQLException ex){
            System.out.println("Error in getAllProductsByType method"+ex);
            ex.printStackTrace();
        }
        
            DBUtil.closeStatement(ps);
            DBUtil.closeResultSet(rs);
            return productList;
    }

    @Override
    public List<ProductPojo> searchAllProducts(String search) {
        Connection conn=DBUtil.provideConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;
        List<ProductPojo> productList=new ArrayList<>();
        try{
           ps=conn.prepareStatement("Select * from products where ptype like ? or pname like ? or pinfo like ?");
           ps.setString(1, "%"+search+"%");
           ps.setString(2, "%"+search+"%");
           ps.setString(3, "%"+search+"%");
           rs=ps.executeQuery();
           while(rs.next()){
                ProductPojo prod=new ProductPojo();
                prod.setProdId(rs.getString("pid"));
                prod.setProdName(rs.getString("pname"));
                prod.setProdInfo(rs.getString("pinfo"));
                prod.setProdType(rs.getString("ptype"));
                prod.setProdPrice(rs.getDouble("pprice"));
                prod.setProdQuantity(rs.getInt("pquantity"));
                prod.setProdImage(rs.getAsciiStream("image"));
                productList.add(prod);
                
           }
        }catch(SQLException ex){
            System.out.println("Error in searchAllProducts method"+ex);
            ex.printStackTrace();
        }
        
            DBUtil.closeStatement(ps);
            DBUtil.closeResultSet(rs);
            return productList;
    }

    @Override
    public ProductPojo getProductDetails(String prodId) {
        ProductPojo product=null;
        Connection conn=DBUtil.provideConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
            ps=conn.prepareStatement("Select * from products where pid=?");
            ps.setString(1, prodId);
            rs=ps.executeQuery();
          
            if(rs.next()){
                product=new ProductPojo();
                product.setProdId(rs.getString("pid"));
                product.setProdName(rs.getString("pname"));
                product.setProdPrice(rs.getDouble("pprice"));
                product.setProdType(rs.getString("ptype"));
                product.setProdInfo(rs.getString("pinfo"));
                product.setProdQuantity(rs.getInt("pquantity"));
                product.setProdImage(rs.getAsciiStream("image"));
                             
            }
           
        }
        catch(SQLException ex){
            
            System.out.println("Error in getProductDetails:"+ex);
            ex.printStackTrace();
        } 
        DBUtil.closeResultSet(rs);
        DBUtil.closeStatement(ps);
      
        return product;
    }

    @Override
    public int getProductQuantity(String prodId) {
        int quantity=0;
       Connection conn=DBUtil.provideConnection();
       PreparedStatement ps=null;
       ResultSet rs=null;
        try{
            ps=conn.prepareStatement("Select pquantity from products where pid=?");
            ps.setString(1,prodId);
            rs=ps.executeQuery();
            if(rs.next()){
                quantity=rs.getInt(1);               
            }
        }catch(SQLException ex){
            System.out.println("Error in getProductQuantity:"+ex);
            ex.printStackTrace();
        } 
        DBUtil.closeResultSet(rs);
        DBUtil.closeStatement(ps);
        return quantity;
    }

    @Override
    public String updateProductWithoutImage(String prevProductId, ProductPojo updatedProduct) {
        String status="Updation Failed!";
        int prevQuantity=0;
        if(!prevProductId.equals(updatedProduct.getProdId())){
            status="Product ID's Do Not Match. Updation Failed";
            return status;
        }
        Connection conn=DBUtil.provideConnection();
        PreparedStatement ps1=null;
        PreparedStatement ps2=null;
        ResultSet rs=null;
        try{
            ps1=conn.prepareStatement("Select pquantity from products where pid=?");
            ps1.setString(1,prevProductId);
            rs=ps1.executeQuery();
            if(rs.next()){
                prevQuantity=rs.getInt(1);               
            }
            ps2=conn.prepareStatement("Update products set pname=?,ptype=?,pinfo=?,pprice=?,pquantity=? where pid=?");
            ps2.setString(1,updatedProduct.getProdName());
            ps2.setString(2,updatedProduct.getProdType());
            ps2.setString(3,updatedProduct.getProdInfo());
            ps2.setDouble(4,updatedProduct.getProdPrice());
            ps2.setInt(5,updatedProduct.getProdQuantity());
            ps2.setString(6,updatedProduct.getProdId());
            int count=ps2.executeUpdate();
            if(count==1 && prevQuantity<updatedProduct.getProdQuantity()){
                status="Product Updated Successfully And Mail Sent";
                DemandDaoImpl demand=new DemandDaoImpl();
                List<DemandPojo> demandList=demand.haveDemanded(prevProductId);
                for(DemandPojo demandPojo:demandList){
                    String userId=demandPojo.getUseremail();
                    try{
                        MailMessage.productupdateSuccess(prevProductId,userId);
                    }
                    catch(MessagingException ex){
                        System.out.println("Error in updateProduct main send :"+ex);
                        ex.printStackTrace();
                    }
                }
                
            }else if(count==1){
                status="Product Updated Successfully";
            }
        }catch(SQLException ex){
            System.out.println("Error in updateProduct:"+ex);
            ex.printStackTrace();
        } 
        DBUtil.closeStatement(ps2);
        DBUtil.closeStatement(ps1);
        DBUtil.closeResultSet(rs);
        return status;
    }

    @Override
    public double getProductPrice(String prodId) {
       double price=0.0;
       Connection conn=DBUtil.provideConnection();
       PreparedStatement ps=null;
       ResultSet rs=null;
        try{
            ps=conn.prepareStatement("Select pprice from products where pid=?");
            ps.setString(1,prodId);
            rs=ps.executeQuery();
            if(rs.next()){
                price=rs.getDouble(1);               
            }
        }catch(SQLException ex){
            System.out.println("Error in getProductPrice:"+ex);
            ex.printStackTrace();
        } 
        DBUtil.closeResultSet(rs);
        DBUtil.closeStatement(ps);
        return price;  
    }

    @Override
    public Boolean sellNProduct(String prodId, int n) {
        boolean result=false;
        Connection conn=DBUtil.provideConnection();
        PreparedStatement ps=null;
        try{
            ps=conn.prepareStatement("Update products set pquantity=(pquantity-?) where pid=?");
            ps.setInt(1,n);
            ps.setString(2,prodId);
            int count=ps.executeUpdate();
            if(count==1){
                result=true;
               
            }
        }catch(SQLException ex){
            System.out.println("Error in sellNProducts:"+ex);
            ex.printStackTrace();
        } 
        DBUtil.closeStatement(ps);
        return result;   
    }

    @Override
    public List<String> getAllProductsType() {
      List <String> productTypeList=new ArrayList<>();
        Connection conn=DBUtil.provideConnection();
        Statement st=null;
        ResultSet rs=null;
         try{
            st=conn.createStatement();
            rs=st.executeQuery("Select distinct ptype from products");
            while(rs.next()){
                 productTypeList.add(rs.getString(1));                
            }
        }
        catch(SQLException ex){
            System.out.println("Error in getAllProductsType:"+ex);
            ex.printStackTrace();
        } 
        DBUtil.closeResultSet(rs);
        DBUtil.closeStatement(st);
        return productTypeList;
    }

    @Override
    public byte[] getImage(String prodId) {
       byte[]arr=null;
       Connection conn=DBUtil.provideConnection();
       PreparedStatement ps=null;
       ResultSet rs=null;
        try{
            ps=conn.prepareStatement("Select image from products where pid=?");
            ps.setString(1,prodId);
            rs=ps.executeQuery();
            if(rs.next()){
               arr=rs.getBytes(1);
            }
        }catch(SQLException ex){
            System.out.println("Error in getImage:"+ex);
            ex.printStackTrace();
        } 
        DBUtil.closeResultSet(rs);
        DBUtil.closeStatement(ps);
        return arr;   
    }

    @Override
    public String removeProduct(String prodId) {
        String status="Product Not Found!";
        Connection conn=DBUtil.provideConnection();
        PreparedStatement ps1=null;
        PreparedStatement ps2=null;
        try{
            ps1=conn.prepareStatement("Update products set available='N' where pid=? and available='Y' ");
            ps1.setString(1, prodId);
            int k=ps1.executeUpdate();
            if(k>0){
                status="Product Removed Successfully!";
                ps2=conn.prepareStatement("Delete from usercart where prodid=?");
                ps2.setString(1, prodId);
                k=ps1.executeUpdate();

            }
        }catch(SQLException ex){
            status="Error:"+ex.getMessage();
            System.out.println("Error in removeProduct method"+ex);
            ex.printStackTrace();
        }
        
            DBUtil.closeStatement(ps1);
            DBUtil.closeStatement(ps2);
            return status;
    }
    
}
