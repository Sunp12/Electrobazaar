/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.electrobazaar.servlet;

import in.electrobazaar.dao.impl.OrderDaoImpl;
import in.electrobazaar.dao.impl.ProductDaoImpl;
import in.electrobazaar.dao.impl.TransactionDaoImpl;
import in.electrobazaar.dao.impl.UserDaoImpl;
import in.electrobazaar.pojo.OrderPojo;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author HP
 */
public class ShippedItemServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session=request.getSession();
                String userName=(String)session.getAttribute("userName");
                String password=(String)session.getAttribute("password");
                String orderId=request.getParameter("orderid");
                String prodId=request.getParameter("prodid");
                if(userName==null||password==null){
                    response.sendRedirect("login.jsp?message=Session expired ! Please login again");
                }
                OrderDaoImpl order=new OrderDaoImpl();
                order.shipNow(orderId, prodId);
                
                List<OrderPojo> orderList=order.getAllOrders();
                Map<String, String> user_Id = new HashMap<String, String>();
                Map<String, String> user_address = new HashMap<String, String>();
                
                UserDaoImpl user=new UserDaoImpl();
                TransactionDaoImpl transaction=new TransactionDaoImpl();
                
                for(OrderPojo ord: orderList){
                    String userId=transaction.getUserId(ord.getOrderId());
                    user_Id.put(ord.getOrderId(), userId);
                    user_address.put(userId, user.getUserAddr(userId));
                }
                
                RequestDispatcher rd=request.getRequestDispatcher("shippedItems.jsp"); 
                request.setAttribute("orders",orderList);
                request.setAttribute("user_Id",user_Id);
                request.setAttribute("user_address",user_address);
                rd.forward(request, response); 
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
