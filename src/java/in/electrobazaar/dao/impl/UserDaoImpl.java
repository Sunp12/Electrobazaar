/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.electrobazaar.dao.impl;


import in.electrobazaar.dao.UserDao;
import in.electrobazaar.pojo.UserPojo;
import in.electrobazaar.utility.DBUtil;
import in.electrobazaar.utility.MailMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.mail.MessagingException;

/**
 *
 * @author HP
 */
public class UserDaoImpl implements UserDao{

    @Override
    public String registerUser(UserPojo user) {
        String result="Registration failed";
        boolean isUserRegistered=isRegistered(user.getUseremail());
        if(isUserRegistered){
            result="User already registerd, Try again";
            return result;
        }
        Connection conn=DBUtil.provideConnection();
        PreparedStatement ps=null;
        try{
            ps=conn.prepareStatement("Insert into Users values(?,?,?,?,?,?,?)");
            ps.setString(1, user.getUseremail());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getMobile());
            ps.setString(4, user.getAddress());
            ps.setInt(5, user.getPincode());
            ps.setString(6, user.getPassword());
            ps.setString(7, user.getUsertype());
            int count=ps.executeUpdate();
            System.out.println("Insert data"+count);
            if(count==1){
                result="Registration Successful";
                MailMessage.registrationSuccess(user.getUseremail(), user.getUsername());
                System.out.println("Mail sent successfully");
                //Code to send email
            }
        }catch(SQLException | MessagingException ex){
            System.out.println("Error in registereUser method"+ex);
            ex.printStackTrace();
        }
        
            DBUtil.closeStatement(ps);
            return result;
        
    }

    @Override
    public boolean isRegistered(String emailId) {
        Connection conn=DBUtil.provideConnection();
        boolean flag=false;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
          
            ps=conn.prepareStatement("SELECT 1 FROM Users WHERE useremail=?");
            ps.setString(1, emailId);
            rs=ps.executeQuery();
            if(rs.next()){
                flag=true;
            }
          
        }catch(SQLException ex){
            System.out.println("Error in isRegistered method"+ex);
            ex.printStackTrace();
        }
        
            DBUtil.closeStatement(ps);
            DBUtil.closeResultSet(rs);
            return flag;
        
    }

    @Override
    public String isValidCredentials(String emailId, String password,String userType) {
        Connection conn=DBUtil.provideConnection();
        String status="Login Denied! Incorrect Username or Password";
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
          
            ps=conn.prepareStatement("SELECT 1 FROM Users WHERE useremail=? and password=? and user_type=?");
            ps.setString(1, emailId);
            ps.setString(2, password);
            ps.setString(3, userType);
            rs=ps.executeQuery();
            if(rs.next()){
               status="Login Successful";
            }
          
        }catch(SQLException ex){
//            status="Error :"+ex.getMessage();
            System.out.println("Error in isValidCredentials method"+ex);
            ex.printStackTrace();
        }
        
            DBUtil.closeStatement(ps);
            DBUtil.closeResultSet(rs);
            return status;
        
    
    }

    @Override
    public UserPojo getUserDetails(String emailId) {
        Connection conn=DBUtil.provideConnection();
        UserPojo user=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
          
            ps=conn.prepareStatement("SELECT * FROM Users WHERE useremail=?");
            ps.setString(1, emailId);
            rs=ps.executeQuery();
            if(rs.next()){
                user=new UserPojo();
                user.setUseremail(rs.getString("useremail"));
                user.setUsername(rs.getString("username"));
                user.setMobile(rs.getString("mobile"));
                user.setAddress(rs.getString("address"));
                user.setPincode(rs.getInt("pincode"));
                user.setPassword(rs.getString("password"));
                
            }
        }
        catch(SQLException ex){
            System.out.println("Error in getUserDetails method"+ex);
            ex.printStackTrace();
        }
        
            DBUtil.closeStatement(ps);
            DBUtil.closeResultSet(rs);
            return user;
    }

    @Override
    public String getUserFirstName(String emailId) {
        Connection conn=DBUtil.provideConnection();
        String fname=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
          
            ps=conn.prepareStatement("SELECT username FROM Users WHERE useremail=?");
            ps.setString(1, emailId);
            rs=ps.executeQuery();
            if(rs.next()){
                String fullName=rs.getString("username");
                fname=fullName.split(" ")[0];
            }
          
        }catch(SQLException ex){
            System.out.println("Error in getUserFirstName method"+ex);
            ex.printStackTrace();
        }
        
            DBUtil.closeStatement(ps);
            DBUtil.closeResultSet(rs);
            return fname;
    }

    @Override
    public String getUserAddr(String emailId) {
        Connection conn=DBUtil.provideConnection();
        String addr=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
          
            ps=conn.prepareStatement("SELECT address FROM Users WHERE useremail=?");
            ps.setString(1, emailId);
            rs=ps.executeQuery();
            if(rs.next()){
                addr=rs.getString("address");
            }
          
        }catch(SQLException ex){
            System.out.println("Error in getUserAddr method"+ex);
            ex.printStackTrace();
        }
        
            DBUtil.closeStatement(ps);
            DBUtil.closeResultSet(rs);
            return addr;
        
      
    }
    
}
