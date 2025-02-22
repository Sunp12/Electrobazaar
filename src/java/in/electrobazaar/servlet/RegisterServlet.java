/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.electrobazaar.servlet;

import in.electrobazaar.dao.impl.UserDaoImpl;
import in.electrobazaar.pojo.UserPojo;
import in.electrobazaar.utility.MailMessage;
import in.electrobazaar.utility.OtpGeneratore;
import java.io.IOException;
import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author HP
 */
public class RegisterServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String email = request.getParameter("user_email");
        if ("generateOtp".equals(action)) {
            String otp;
            if (email != null) {
                otp = OtpGeneratore.generateOtp(email);
                System.out.println(otp);
                try {
                    MailMessage.sendOtp(email, otp);
                } catch (MessagingException ex) {
                    System.out.println("Error in requestServlet mail send :" + ex);
                    ex.printStackTrace();
                }

            }
             //else {
//                otp = OtpGeneratore.generateOtp(mob);
//                try {
//                    OTPMessage.sendOtpSms(mob, otp);
//                } catch (Exception ex) {
//                    Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//            System.out.println("Generated OTP for  " + otp); // Debugging
            response.getWriter().write("OTP sent successfully");
        } else if ("verifyOtp".equals(action)) {
            String userOtp = request.getParameter("otp");
            System.out.println(userOtp+" In verification");
            if (OtpGeneratore.verifyOtp(email, userOtp)) {
                
                response.getWriter().write("success");
                
            } else {
                
                response.getWriter().write("failure");
            }
//            RequestDispatcher rd = request.getRequestDispatcher("register.jsp");
//            rd.forward(request, response);
        } else {

            String name = request.getParameter("user_name");
            
            String address = request.getParameter("address");
            
            email = request.getParameter("user_email");
           
            String mob = request.getParameter("mobile");
            
            int pincode = Integer.parseInt(request.getParameter("pincode"));
            
            String password = request.getParameter("password");
            
            String usertype=request.getParameter("userType");
            System.out.println(email);
            System.out.println(name);
             System.out.println(address);
              System.out.println(mob);
               System.out.println(pincode); 
                System.out.println(password);
                 System.out.println(usertype);
               
            UserPojo user = new UserPojo();
            user.setUsername(name);
            user.setAddress(address);
            user.setMobile(mob);
            user.setUseremail(email);
            user.setPassword(password);
            user.setPincode(pincode);
            user.setUsertype(usertype);
             System.out.println("After set data");
              System.out.println(user.toString());
            
            UserDaoImpl userdao = new UserDaoImpl();
            String status = userdao.registerUser(user);
            System.out.println(status);
            RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
            rd.forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
