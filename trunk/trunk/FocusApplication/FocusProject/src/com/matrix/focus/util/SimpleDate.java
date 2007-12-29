package com.matrix.focus.util;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

public class SimpleDate extends java.util.GregorianCalendar{
    
    public SimpleDate(String stringDate){
        /*
        int year = Integer.valueOf(stringDate.substring(0,4));
        int month = Integer.valueOf(stringDate.substring(5,7));
        int date = Integer.valueOf(stringDate.substring(8));
        super(year,month,date);
        */
        // super(Integer.valueOf(stringDate.substring(0,4)),Integer.valueOf(stringDate.substring(5,7)),Integer.valueOf(stringDate.substring(8)));
        
        super(0,0,0,0,0,0);

        if(stringDate.equals(""))
            stringDate = "0000-00-00";

        StringTokenizer sT = new StringTokenizer(stringDate.trim(),"-");
        int year = Integer.valueOf(sT.nextToken());
        int month = Integer.valueOf(sT.nextToken());
        int date = Integer.valueOf(sT.nextToken());
        
        this.clear();
        this.set(year,month-1,date,0,0,0);    
    }
    
    public SimpleDate(int year,int month,int date){
        super(0,0,0,0,0,0);
        
        this.clear();
        this.set(year,month-1,date,0,0,0);    
    }    
    
    public boolean isBefore(String stringDate){
        if(stringDate.equals(""))
            stringDate = "0000-00-00";
        StringTokenizer sT = new StringTokenizer(stringDate.trim(),"-");
        int year = Integer.valueOf(sT.nextToken());
        int month = Integer.valueOf(sT.nextToken());
        int date = Integer.valueOf(sT.nextToken());
        
        SimpleDate otherDate = new SimpleDate(year,month,date); 
        
        if(this.before(otherDate))
           return true;
        else   
           return false;
    }
    
    public boolean isAfter(String stringDate){
        if(stringDate.equals(""))
            stringDate = "0000-00-00";    
        StringTokenizer sT = new StringTokenizer(stringDate.trim(),"-");
        int year = Integer.valueOf(sT.nextToken());
        int month = Integer.valueOf(sT.nextToken());
        int date = Integer.valueOf(sT.nextToken());
        
        SimpleDate otherDate = new SimpleDate(year,month,date); 
        
        if(this.after(otherDate))
           return true;
        else   
           return false;        
    }
    
    public boolean isEqual(String stringDate){
        if(stringDate.equals(""))
            stringDate = "0000-00-00";
        StringTokenizer sT = new StringTokenizer(stringDate.trim(),"-");
        int year = Integer.valueOf(sT.nextToken());
        int month = Integer.valueOf(sT.nextToken());
        int date = Integer.valueOf(sT.nextToken());
 
        SimpleDate otherDate = new SimpleDate(year,month,date);
        
        if(this.compareTo(otherDate)==0)
           return true;
        else   
           return false;        
    }
    
    public boolean isEqual(SimpleDate otherDate){        
        if(this.compareTo(otherDate)==0)
           return true;
        else   
           return false;        
    }
    
    public String getStringDate(){
        String stringDate = this.get(GregorianCalendar.YEAR)+"-"+( this.get(GregorianCalendar.MONTH)+1)+"-"+this.get(GregorianCalendar.DAY_OF_MONTH);
        //if(stringDate.equals("0000-00-00"))
          //  stringDate = "";
        return stringDate;
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
    
    public int getYear(){
       return this.get(GregorianCalendar.YEAR);
    }
    
    public int getMonth(){
       return this.get(GregorianCalendar.MONTH)+1;
    }
    
    public int getDate(){
       return this.get(GregorianCalendar.DATE);
    }
    
}

