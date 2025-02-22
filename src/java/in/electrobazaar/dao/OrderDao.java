/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.electrobazaar.dao;

import in.electrobazaar.pojo.OrderDetailsPojo;
import in.electrobazaar.pojo.OrderPojo;
import in.electrobazaar.pojo.TransactionPojo;
import java.util.List;

/**
 *
 * @author HP
 */
public interface OrderDao {
    
    public boolean addOrder(OrderPojo order);
    public boolean addTransaction(TransactionPojo transaction);
    public List<OrderPojo> getAllOrders();
    public List<OrderDetailsPojo> getAllOrderDetails(String userEmailId);
    public String shipNow(String orderId, String prodId);
    public String paymentSuccess(String username, double paidAmount);
    public int getSoldQuantity(String prodId);
    
}
