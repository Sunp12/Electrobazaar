/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.electrobazaar.utility;

import in.electrobazaar.dao.impl.ProductDaoImpl;
import in.electrobazaar.dao.impl.UserDaoImpl;
import in.electrobazaar.pojo.ProductPojo;
import in.electrobazaar.pojo.TransactionPojo;
import javax.mail.MessagingException;

/**
 *
 * @author HP
 */
public class MailMessage {

    public static void registrationSuccess(String recipientMailId, String name) throws MessagingException {
        String subject = "Registration Successfull";
        String htmlTextMessage = "" + "<html>" + "<body>"
                + "<h2 style='color:green;'>Welcome to " + AppInfo.appName + "</h2>" + "" + "Hi " + name + ","
                + "<br><br>Thanks for singing up with " + AppInfo.appName + ".<br>"
                + "We are glad that you choose us. We invite you to check out our latest collection of new electonics appliances."
                + "<br>We are providing upto 60% OFF on most of the electronic gadgets. So please visit our site and explore the collections."
                + "<br><br>Our Online electronics is growing in a larger amount these days and we are in high demand so we thanks all of you for "
                + "making us up to that level. We Deliver Product to your house with no extra delivery charges and we also have collection of most of the"
                + "branded items.<br><br>As a Welcome gift for our New Customers we are providing additional 10% OFF Upto 500 Rs for the first product purchase. "
                + "<br>To avail this offer you only have "
                + "to enter the promo code given below.<br><br><br> PROMO CODE: " + "" + AppInfo.appName.toUpperCase() + "500<br><br><br>"
                + "Have a good day!<br>" + "" + "</body>" + "</html>";
        JavaMailUtil.sendMail(recipientMailId, subject, htmlTextMessage);
    }

    public static void transactionSuccess(TransactionPojo transaction, String userEmail) throws MessagingException {
        UserDaoImpl user = new UserDaoImpl();
        String name = user.getUserFirstName(userEmail);
        String subject = "Transaction Successfull";
        String htmlTextMessage = ""
                + "<html>"
                + "<body>"
                + "<h2 style='color:green;'>Welcome to " + AppInfo.appName + "</h2>"
                + "Hi " + name + ","
                + "<br><br>We are delighted to inform you that your recent transaction with us was successful!<br>"
                + "Thank you for choosing <span style='color:green;'>" + AppInfo.appName + "</span> for your purchase."
                + "<br><br>Here are the transaction details for your reference:"
                + "<ul>"
                + "<li><strong>Transaction ID:</strong> " + transaction.getTransactionId() + "</li>"
                + "<li><strong>Amount Paid:</strong> RS" + transaction.getAmount() + "</li>"
                + "<li><strong>Date:</strong> " + transaction.getTransTime() + "</li>"
                + "</ul>"
                + "We hope you enjoy your purchase. If you have any questions or need further assistance, please don't hesitate to reach out to our support team."
                + "<br><br>Have a great day!<br>"
                + "</body>"
                + "</html>";

        JavaMailUtil.sendMail(userEmail, subject, htmlTextMessage);
    }

    public static void productShippedSuccess(String userId, String prodId) throws MessagingException {
        ProductDaoImpl product = new ProductDaoImpl();
        ProductPojo productPojo = new ProductPojo();
        productPojo = product.getProductDetails(prodId);
        UserDaoImpl user = new UserDaoImpl();
        String name = user.getUserFirstName(userId);
        String subject = "ProductShipped Successfull";
        String htmlTextMessage = ""
                + "<html>"
                + "<body>"
                + "<h2 style='color:green;'>Welcome to " + AppInfo.appName + "</h2>"
                + "Hi " + name + ","
                + "<br><br>Great news! Your product has been shipped and is on its way to you.<br>"
                + "Here are the shipping details:"
                + "<ul>"
                + "<li><strong>Order ID:</strong> " + userId + "</li>"
                + "<li><strong>Product ID:</strong> " + prodId + "</li>"
                + "<li><strong>Product Name:</strong> " + productPojo.getProdName() + "</li>"
                + "<li><strong>Product Price:</strong> " + productPojo.getProdPrice() + "</li>"
                //                + "<li><strong>Shipping Carrier:</strong> " + carrierName + "</li>"
                //                + "<li><strong>Tracking Number:</strong> " + trackingNumber + "</li>"
                + "</ul>"
                + "You can track your shipment by clicking the link below:<br>"
                + "<a href='  trackingUrl  ' style='color:blue;'>Track Your Shipment</a>"
                + "<br><br>We hope your product meets your expectations. Thank you for shopping with " + AppInfo.appName + "!"
                + "<br><br>Have a wonderful day!<br>"
                + "</body>"
                + "</html>";

        JavaMailUtil.sendMail(userId, subject, htmlTextMessage);
    }

    public static void productupdateSuccess(String prodId, String userId) throws MessagingException {
        String subject = "Product demand quantity available";
        ProductPojo productPojo = new ProductPojo();
        ProductDaoImpl product = new ProductDaoImpl();
        productPojo = product.getProductDetails(prodId);
        UserDaoImpl user = new UserDaoImpl();
        String name = user.getUserFirstName(userId);
        String productUpdateMessage = ""
                + "<html>"
                + "<body>"
                + "<h2 style='color:green;'>Good News from " + AppInfo.appName + "!</h2>"
                + "Hi " + name + ","
                + "<br><br>We are excited to inform you that the product you were interested in is now back in stock!"
                + "<br><br>Here are the updated details:"
                + "<ul>"
                + "<li><strong>Product Name:</strong> " + productPojo.getProdName() + "</li>"
                + "<li><strong>Available Quantity:</strong> " + productPojo.getProdQuantity() + "</li>"
                + "</ul>"
                + "You can now proceed with your purchase before it goes out of stock again."
                + "<br><br>Thank you for choosing " + AppInfo.appName + "! If you need any assistance, feel free to contact our support team."
                + "<br><br>Happy Shopping!<br>"
                + "</body>"
                + "</html>";
        JavaMailUtil.sendMail(userId, subject, productUpdateMessage);
    }

    public static void sendOtp(String email, String otp) throws MessagingException {
         String subject = "Email id verification code";
        UserDaoImpl user = new UserDaoImpl();
        String name = user.getUserFirstName(email);
        String otpEmailMessage = ""
                + "<html>"
                + "<body>"
                + "<h2 style='color:green;'>Security Verification - " + AppInfo.appName + "</h2>"
                + "Hi " + name + ","
                + "<br><br>We received a request to verify your account. Please use the OTP below to proceed with your verification."
                + "<br><br><strong>Your OTP Code:</strong> <span style='font-size:20px; font-weight:bold; color:blue;'>" + otp + "</span>"
                + "<br><br>This OTP is valid for <strong>10 minutes</strong>. Please do not share it with anyone for security reasons."
                + "<br><br>If you did not request this verification, please ignore this email or contact our support team immediately."
                + "<br><br>Thank you for choosing " + AppInfo.appName + "!"
                + "<br><br>Best Regards,<br>"
                + "The " + AppInfo.appName + " Team"
                + "</body>"
                + "</html>";
        JavaMailUtil.sendMail(email, subject, otpEmailMessage);

    }
}
