/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.electrobazaar.dao;

import in.electrobazaar.pojo.UserPojo;

/**
 *
 * @author HP
 */
public interface UserDao {
    String registerUser(UserPojo user);
    boolean isRegistered(String emailId);
    String isValidCredentials(String emailId,String password ,String userType );
    UserPojo getUserDetails(String emailId);
    String getUserFirstName(String emailId);
    String getUserAddr(String emailId);
    
}
