/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.electrobazaar.utility;

import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author HP
 */
public class OtpGeneratore {
    private static final int OTP_LENGTH = 6;
    private static final HashMap<String, String> otpStorage = new HashMap<>();

    public static String generateOtp(String email) {
        Random rand = new Random();
        String otp = String.format("%06d", rand.nextInt(1000000));
        
        otpStorage.put(email, otp);
        return otp;
    }

    public static boolean verifyOtp(String email, String userOtp) {
        return otpStorage.containsKey(email) && otpStorage.get(email).equals(userOtp);
    }
}
