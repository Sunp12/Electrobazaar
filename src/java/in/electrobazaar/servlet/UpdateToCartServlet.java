/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.electrobazaar.servlet;

import in.electrobazaar.dao.CartDao;
import in.electrobazaar.dao.impl.CartDaoImpl;
import in.electrobazaar.dao.impl.DemandDaoImpl;
import in.electrobazaar.dao.impl.ProductDaoImpl;
import in.electrobazaar.pojo.CartPojo;
import in.electrobazaar.pojo.DemandPojo;
import in.electrobazaar.pojo.ProductPojo;
import java.io.IOException;
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
public class UpdateToCartServlet extends HttpServlet { 
 
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException { 
        HttpSession session = request.getSession(); 
        String userName = (String) session.getAttribute("userName"); 
        String password = (String) session.getAttribute("password"); 
        String userType = (String) session.getAttribute("userType"); 
        if (userType == null|| password==null||userName == null || !userType.equalsIgnoreCase("customer")) { 
            response.sendRedirect("login.jsp?message=Access denied ! Please login first"); 
            return; 
        } 
        String prodId = request.getParameter("pid"); 
        int pQty = Integer.parseInt(request.getParameter("pqty")); 
        ProductDaoImpl prodDao = new ProductDaoImpl(); 
        CartDao cartDao=new CartDaoImpl(); 
        ProductPojo product = prodDao.getProductDetails(prodId); 
        int availableQty = product.getProdQuantity(); 
        if (availableQty < pQty) { 
            String status = "Only " + availableQty + " no of " + product.getProdName() + " are available in the shop!.So we can add only " + availableQty + " products in your cart"; 
            DemandDaoImpl demand = new DemandDaoImpl(); 
            DemandPojo demandPojo = new DemandPojo(userName, prodId, pQty - availableQty); 
            boolean flag = demand.addProduct(demandPojo); 
            if (flag) { 
                status += " Later we will mail you when " + product.getProdName() + " will be available"; 
            } 
            RequestDispatcher rd = request.getRequestDispatcher("./CartDetailsServlet"); 
            request.setAttribute("message",status); 
            rd.forward(request, response); 
        }else{ 
            String status = cartDao.updateProductInCart(new CartPojo(userName, prodId, pQty)); 
            RequestDispatcher rd = request.getRequestDispatcher("./CartDetailsServlet"); 
            request.setAttribute("message",status); 
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
