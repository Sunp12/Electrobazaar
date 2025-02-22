/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.electrobazaar.dao.impl;


import in.electrobazaar.dao.OrderDao;
import in.electrobazaar.pojo.CartPojo;
import in.electrobazaar.pojo.OrderDetailsPojo;
import in.electrobazaar.pojo.OrderPojo;
import in.electrobazaar.pojo.TransactionPojo;
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
public class OrderDaoImpl implements OrderDao {

    @Override
    public boolean addOrder(OrderPojo order) {
        boolean status = false;
        Connection conn = DBUtil.provideConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("Insert into orders values(?,?,?,?,?)");
            ps.setString(1, order.getOrderId());
            ps.setString(2, order.getProdId());
            ps.setInt(3, order.getQuantity());
            ps.setDouble(4, order.getAmount());
            ps.setInt(5, 0);
            int count = ps.executeUpdate();
            status = count > 0;
        } catch (SQLException ex) {
            System.out.println("Error in addOrder method" + ex);
            ex.printStackTrace();
        }

        DBUtil.closeStatement(ps);
        return status;
    }

    @Override
    public boolean addTransaction(TransactionPojo transaction) {
        boolean status = false;
        Connection conn = DBUtil.provideConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("Insert into transactions values(?,?,?,?)");
            ps.setString(1, transaction.getTransactionId());
            ps.setString(2, transaction.getUseremail());
            java.util.Date d1 = transaction.getTransTime();
            java.sql.Date d2 = new java.sql.Date(d1.getTime());
            ps.setDate(3, d2);
            ps.setDouble(4, transaction.getAmount());
            int count = ps.executeUpdate();
            status = count > 0;
        } catch (SQLException ex) {
            System.out.println("Error in addTransaction method" + ex);
            ex.printStackTrace();
        }

        DBUtil.closeStatement(ps);
        return status;

    }

    @Override
    public List<OrderPojo> getAllOrders() {
        List<OrderPojo> orderList = new ArrayList<>();
        Connection conn = DBUtil.provideConnection();
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            rs = st.executeQuery("Select * from Orders");
            while (rs.next()) {
                OrderPojo order = new OrderPojo();
                order.setOrderId(rs.getString("orderid"));
                order.setProdId(rs.getString("prodid"));
                order.setQuantity(rs.getInt("quantity"));
                order.setAmount(rs.getDouble("amount"));
                order.setShipped(rs.getInt("shipped"));
                orderList.add(order);
            }
        } catch (SQLException ex) {
            System.out.println("Error in getAllOrders method" + ex);
            ex.printStackTrace();
        }

        DBUtil.closeStatement(st);
        DBUtil.closeResultSet(rs);
        return orderList;

    }

    @Override
    public List<OrderDetailsPojo> getAllOrderDetails(String userEmailId) {
        List<OrderDetailsPojo> orderList = new ArrayList<>();
        Connection conn = DBUtil.provideConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("Select p.pid as prodid, o.orderid as orderid, o.shipped as shipped, p.image as image, p.pname as pname,o.quantity as qty,o.amount as amount,t.transtime as time FROM orders o, products p, transactions t where o.orderid=t.transid and o.prodid=p.pid and t.useremail=?");
            ps.setString(1, userEmailId);
            rs = ps.executeQuery();
            while (rs.next()) {
                OrderDetailsPojo orderDetails = new OrderDetailsPojo();
                orderDetails.setOrderId(rs.getString("orderid"));
                orderDetails.setProdImage(rs.getAsciiStream("image"));
                orderDetails.setProdId(rs.getString("prodid"));
                orderDetails.setProdName(rs.getString("pname"));
                orderDetails.setQuantity(rs.getInt("qty"));
                orderDetails.setAmount(rs.getDouble("amount"));
                orderDetails.setTime(rs.getTimestamp("time"));
                orderDetails.setShipped(rs.getInt("shipped"));
                orderList.add(orderDetails);
            }
        } catch (SQLException ex) {
            System.out.println("Error in getAllOrderDetails method" + ex);
            ex.printStackTrace();
        }

        DBUtil.closeResultSet(rs);
        DBUtil.closeStatement(ps);
        return orderList;
    }

    @Override
    public String shipNow(String orderId, String prodId) {
        String status = "Failure";
        Connection conn = DBUtil.provideConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("Update orders set shipped=1 where orderid=? and prodid=?");
            ps.setString(1, orderId);
            ps.setString(2, prodId);
            int count = ps.executeUpdate();
            if (count > 0) {
                status = "Order has been shipped successfully";
                TransactionDaoImpl trans=new TransactionDaoImpl();
                try{
                    MailMessage.productShippedSuccess(trans.getUserId(orderId), prodId);
                }
                catch(MessagingException ex){
                    System.out.println("Error in shipNow method sending mail" + ex);
                    ex.printStackTrace();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in shipNow method" + ex);
            ex.printStackTrace();
        }

        DBUtil.closeStatement(ps);

        return status;
    }

    @Override
    public String paymentSuccess(String username, double paidAmount) {
        String status = "Order Placement Failed!";
        CartDaoImpl cartDao = new CartDaoImpl();
        List<CartPojo> cartList = cartDao.getAllCartItems(username);
        if (cartList.isEmpty()) {
            return status;
        }
        String transactionId = IDUtil.generateTransId();
        TransactionPojo trPojo = new TransactionPojo();
        trPojo.setTransactionId(transactionId);
        trPojo.setUseremail(username);
        trPojo.setAmount(paidAmount);
        trPojo.setTransTime(new java.util.Date());
        boolean result = addTransaction(trPojo);
        if (result == false) {
            return status;

        }
        boolean ordered = true;
        ProductDaoImpl productDao = new ProductDaoImpl();
        for (CartPojo cartPojo : cartList) {
            double amount = productDao.getProductPrice(cartPojo.getProdId()) * cartPojo.getQuantity();
            OrderPojo order = new OrderPojo();
            order.setOrderId(transactionId);
            order.setProdId(cartPojo.getProdId());
            order.setQuantity(cartPojo.getQuantity());
            order.setAmount(amount);
            order.setShipped(0);
            ordered = addOrder(order);
            if (!ordered) {
                break;
            }
            ordered = cartDao.removeAProduct(cartPojo.getUseremail(), cartPojo.getProdId());
            if (!ordered) {
                break;
            }
            ordered = productDao.sellNProduct(cartPojo.getProdId(), cartPojo.getQuantity());
            if (!ordered) {
                break;
            }
        }
        if (ordered) {
            status = "Order Placed Successfully";
            try {
                MailMessage.transactionSuccess(trPojo, username);
                System.out.println("Mail successfully send");
            } catch (MessagingException ex) {
                System.out.println("Error in paymentSuccess method mail send!" + ex);
                ex.printStackTrace();
            }
            System.out.println("Transaction successful : " + transactionId);
        } else {
            System.out.println("Transaction failed: " + transactionId);
        }
        return status;
    }

    @Override
    public int getSoldQuantity(String prodId) {

        Connection conn = DBUtil.provideConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        int quantity = 0;
        try {
            ps = conn.prepareStatement("Select sum(quantity) as quant from orders where prodid=?");
            ps.setString(1, prodId);
            rs = ps.executeQuery();
            if (rs.next()) {
                quantity = rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error in getSoldQuantity method" + ex);
            ex.printStackTrace();
        }

        DBUtil.closeResultSet(rs);
        DBUtil.closeStatement(ps);
        return quantity;
    }

}
