/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 package in.electrobazaar.utility;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 *
 * @author HP
 */
public class OTPMessage {
// 'https://www.fast2sms.com/dev/bulkV2 ?authorization=YOUR_API_KEY&variables_values=5599&route=otp&numbers=9999999999,8888888888,7777777777'
    

   private static final String API_KEY = "duHBWpUh0v9jotnFYkZQLybz71il8wN6qMg5aEsIO24xRmDKPC3KWj9BiRutoMhZamp0Nqv6Hzxl5rCU"; // Replace with your actual Fast2SMS API key

    public static void sendOtpSms(String mobileNumber, String otp) {
        try {
            String apiUrl = "https://www.fast2sms.com/dev/bulkV2";
            String payload = "authorization=" + API_KEY + 
                             "&route=otp" +
                             "&variables_values=" + otp + 
                             "&numbers=" + mobileNumber;

            URL url = new URL(apiUrl);
            System.out.println(url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("authorization", API_KEY);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setDoOutput(true);

            // Write the request body
//            try (OutputStream os = con.getOutputStream()) {
//                os.write(payload.getBytes());
//                os.flush();
//            }
            System.out.println(con);
            // Read response
            int responseCode = con.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            try (Scanner scanner = new Scanner(con.getInputStream())) {
                while (scanner.hasNext()) {
                    System.out.println(scanner.nextLine());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        sendOtpSms("8085207945", "894567");
    }
}


