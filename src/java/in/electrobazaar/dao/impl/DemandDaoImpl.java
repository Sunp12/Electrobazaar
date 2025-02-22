/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.electrobazaar.dao.impl;

import in.electrobazaar.dao.DemandDao;
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
public class DemandDaoImpl implements DemandDao {

    @Override
    public boolean addProduct(DemandPojo demandPojo) {
        boolean status=false;
        Connection conn=DBUtil.provideConnection();
        PreparedStatement ps=null;
        try{
            ps=conn.prepareStatement("Update userdemand set quantity=quantity+? where useremail=? and prodid=?");
            ps.setInt(1, demandPojo.getDemandQuantity());
            ps.setString(2, demandPojo.getUseremail());
            ps.setString(3, demandPojo.getProdId());
            int k= ps.executeUpdate();
            if(k==0){
                ps=conn.prepareStatement("Insert into userdemand values  (?,?,?)");
                ps.setString(1, demandPojo.getUseremail());
                ps.setString(2, demandPojo.getProdId());
                ps.setInt(3, demandPojo.getDemandQuantity());
                ps.executeUpdate();
            }
            status=true;
            
        }
        catch(SQLException ex){
            System.out.println("Error in addProduct method"+ex);
            ex.printStackTrace();
        }
        
            DBUtil.closeStatement(ps);
            return status;
        
    }
    public boolean removeProduct(String userId, String prodId) {
        boolean status=false;
        Connection conn=DBUtil.provideConnection();
        PreparedStatement ps=null;
        try{
            ps=conn.prepareStatement("Delete from userdemand where useremail=? and prodid=?");
            ps.setString(1, userId);
            ps.setString(2, prodId);
            status=ps.executeUpdate()>0;
        }
        catch(SQLException ex){
            System.out.println("Error in removeProduct method"+ex);
            ex.printStackTrace();
        }
        
            DBUtil.closeStatement(ps);
            return status;
    }

    @Override
    public List<DemandPojo> haveDemanded(String prodId) {
        List<DemandPojo> demandList=new ArrayList<>();
        Connection conn=DBUtil.provideConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
            ps=conn.prepareStatement("Select * from userdemand where prodid=?");
            ps.setString(1, prodId);
            rs=ps.executeQuery();
            while(rs.next()){
                DemandPojo demandPojo=new DemandPojo();
                demandPojo.setUseremail(rs.getString("useremail"));
                demandPojo.setProdId(rs.getString("prodid"));
                demandPojo.setDemandQuantity(rs.getInt("quantity"));
                demandList.add(demandPojo);
            }
        }
        catch(SQLException ex){
            System.out.println("Error in haveDemand method"+ex);
            ex.printStackTrace();
        }
            DBUtil.closeResultSet(rs);
            DBUtil.closeStatement(ps);
            return demandList;
    }
    
}
