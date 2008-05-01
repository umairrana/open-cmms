package com.matrix.focus.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Properties;

public class Utilities {

    public static String getApplicationPath(){
        try {
            Properties pro = Utilities.getProperties("conf/common.inf");
            return pro.getProperty("APPLICATION_PATH");
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static int getMinutes(String dd_hh_mm){
        String parts[] = dd_hh_mm.split("-");
        return (Integer.parseInt(parts[0])*60*24) + (Integer.parseInt(parts[1])*60) + Integer.parseInt(parts[2]);
    }
    
    public static int getDays(String yy_mm_dd){
        String value = yy_mm_dd.trim();
        if(value.isEmpty()){
            value = "0-0-0";
        }
        String parts[] = value.split("-");
        return (Integer.parseInt(parts[0])*365) + (Integer.parseInt(parts[1])*30) + Integer.parseInt(parts[2]);
    }
    
    public static String getDDHHMM(int minutes){
        int d = (minutes/(60*24));
        int rest = minutes%(60*24);
        int h = rest/60;
        rest = rest%60;
        return d + "-" + h + "-" + rest;
    }
    
    public static String getDoubleFigures(int value){
        String val = "";
        if(value<10){
            val = "0" + new Integer(value).toString();
        }
        else{
            val = new Integer(value).toString();
        }
        return val;     
    }
    
    public static String get4DigitFigure(int value){
        String val = "";
        if(value<10){
            val = "000" + new Integer(value).toString();
        }
        else if(value<100){
            val = "00" + new Integer(value).toString();
        }
        else if(value<1000){
            val = "0" + new Integer(value).toString();
        }
        else{
            val = new Integer(value).toString();
        }
        return val;     
    }
    
    public static String encrypt(String text){
        String encrypted = "";
        int ch = 0;
        for(int i=0;i<text.length();i++){
            ch = (int)text.charAt(i)+3;
            encrypted += (char)ch;
        }
        return encrypted;
    }
    
    public static String decrypt(String text){
        String decrypted = "";
        int ch = 0;
        for(int i=0;i<text.length();i++){
            ch = (int)text.charAt(i)-3;
            decrypted += (char)ch;
        }
        return decrypted;
    }
    
    public static void setProperties(String filePath, String properties[][], String comments) throws Exception{
        if(filePath.trim().equals("")){
            throw new Exception("Properties file name is empty.");
        }
        
        try{
            Properties pro = new Properties();
            FileOutputStream proFile = new FileOutputStream(filePath);
            for(int i=0;i<properties.length;i++){
                pro.setProperty(properties[i][0], properties[i][1]);
            }
            pro.store(proFile, "---"+comments+"---");
            proFile.close();             
        }
        catch(Exception e){
                throw new Exception("Could not set properties.");
        }
    }
    
    public static Properties getProperties(String filePath) throws Exception{
        try{
            Properties pro = new Properties();
            FileInputStream proFile = new FileInputStream(filePath);
            pro.load(proFile);
            proFile.close();
            return pro;
        }
        catch(Exception e){
                throw new Exception("Could not get properties.");
        }       
    }
    
    
    public static String getNICHardwareAddress(){
        try{      
            Enumeration cards = NetworkInterface.getNetworkInterfaces();
            NetworkInterface card = null;
            String address = "";
            while(cards.hasMoreElements()){
                card=(NetworkInterface)cards.nextElement();
                if(!card.isLoopback()){
                    byte bts[] = card.getHardwareAddress();
                    for(int i=0;i<bts.length;i++){
                        address = address.concat((int)bts[i]+"");
                    }
                    return address; 
                }
            }
            return "ERROR";
  
        }
        catch(SocketException e){
            e.printStackTrace();
            return "ERROR";
        } 
    }
    
    public static String getToday(){
        Calendar today = Calendar.getInstance();
        int month = today.get(GregorianCalendar.MONTH)+1;
        int day = today.get(GregorianCalendar.DATE);
        String strMonth = "";
        if(month<10)
            strMonth = "0"+month;
        else
            strMonth = String.valueOf(month);            
        
        return today.get(GregorianCalendar.YEAR)+"-"+strMonth+"-"+ (day<10?("0"+day):""+day);
    }
    
    public static boolean isValidEmail(String email){                           
        String regex = "^[a-zA-Z0-9-_]+(\\.[a-zA-Z0-9-_]+)*@([a-zA-Z_0-9-]+\\.)+[a-zA-Z]{2,7}$";                                 
        return email.matches(regex);                   
    }
    
    public static boolean isNumber(String txt){
        try{
            Double.parseDouble(txt);
            return true;
        }
        catch(NumberFormatException nfe){
            return false;
        }
    }
    
    public static boolean isNonNegative(String txt){
        try{
              if(isNumber(txt) && (Double.parseDouble(txt)>=0)){
                  return true;
              }
              else{
                  return false;
              }
        }
        catch(NumberFormatException nfe){
            return false;
        }
    }
    
    public static boolean isPositive(String txt){
          try{
                if(isNumber(txt) && (Double.parseDouble(txt)>0)){
                    return true;
                }
                else{
                    return false;
                }
          }
          catch(NumberFormatException nfe){
              return false;
          }
      }
      
        public static int getDateDifference(String first_date, String second_date, Connection connection)throws Exception{
            ResultSet rec = connection.createStatement().executeQuery("SELECT DATEDIFF('" + first_date + "','" + second_date + "')");
            rec.first();
            return rec.getInt(1); 
        }
}
