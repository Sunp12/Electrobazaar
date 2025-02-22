/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.electrobazaar.dao.impl;


import in.electrobazaar.dao.TransactionDao;
import in.electrobazaar.utility.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author HP
 */
public class TransactionDaoImpl implements TransactionDao{

    @Override
    public String getUserId(String transId) {
        String userId="";
        Connection conn=DBUtil.provideConnection();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
            ps=conn.prepareStatement("Select useremail from transactions where transid=?");
            ps.setString(1, transId);
            rs=ps.executeQuery();
            if(rs.next()){
                userId=rs.getString(1);
            }
            
        }catch(SQLException ex){
            System.out.println("Error in getUserId method"+ex);
            ex.printStackTrace();
        }
        
            DBUtil.closeStatement(ps);
            DBUtil.closeResultSet(rs);
            return userId;
    }
    
}
