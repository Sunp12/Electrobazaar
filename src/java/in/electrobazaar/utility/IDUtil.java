/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.electrobazaar.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author HP
 */
public class IDUtil {
    public static String generateProdId(){
        Date today= new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmss");
        String prodId="P"+sdf.format(today);
        return prodId;
    }
    
     public static String generateTransId(){
        Date today= new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmss");
        String transId="T"+sdf.format(today);
        return transId;
    }
}
