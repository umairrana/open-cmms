package com.matrix.focus.util;

import java.util.Date;
import java.sql.Connection;
import java.sql.ResultSet;

public class Validator{

  /**Return true if the length of the given string is bigger than the checked length*/
	public static boolean isBiggerThan(String txt, int len){		
		if(txt.length()>len){
			return true;
		}
		else{
			return false;
		}
	}
  
  /**Return true if the length of the given string is smaller than the checked length*/
  public static boolean isSmallerThan(String txt, int len){		 
		if(txt.length()<len){
			return true;
		}
		else{
			return false;
		}
	}
  
  /**Return true if the given date is an actual date*/
	public static boolean isDate(String sDate){
		try{
			String givenDate  = "";
			String actualDate = "";
			
			String sYear  = sDate.substring(0,4);
			String sMonth = sDate.substring(5,7);
			String sDay  = sDate.substring(8,10);
			givenDate = sYear+"."+sMonth+"."+sDay;
			
			int iYear = Integer.parseInt(sYear)		- 1900;
			int iMonth = Integer.parseInt(sMonth)	- 1;
			int iDay = Integer.parseInt(sDay);
			Date tDate = new Date(iYear, iMonth, iDay);
			iYear  = tDate.getYear()  + 1900;
			iMonth = tDate.getMonth() + 1;
			iDay   = tDate.getDate();
			sYear  = new Integer(iYear).toString();
			sMonth = Utilities.getDoubleFigures(iMonth);
			sDay   = Utilities.getDoubleFigures(iDay);
			actualDate =sYear+"."+sMonth+"."+sDay;
			
			if(actualDate.equals(givenDate))return true;
			else return false;
		}
		catch(Exception e){
			return false;
		}
	}
    
        public static boolean isPeriodValid(String end,String start,Connection con){
            try  {
                
                String query="select timediff('"+end+"','"+start+"')";
                String query2="select datediff('"+end+"','"+start+"')";
                ResultSet rec=con.createStatement().executeQuery(query);
                ResultSet rec2=con.createStatement().executeQuery(query2);
                rec.next();
                rec2.next();
               
                int ddif=Integer.parseInt(rec2.getString(1));
                if(ddif>0){
                    return true;
                }
                else if (ddif==0){
                    char val=rec.getString(1).charAt(0);
                    //int val=Integer.parseInt(sT.nextToken());
                    return (val=='-' ? false:true);
                }
                else{
                    return false;
                }
            } catch (Exception ex)  {
                //ex.printStackTrace();
                return false;
            } 
        }
	/**Return true if the give date is after today*/
	public static boolean isAfterToday(String date){
		int y = Integer.parseInt(date.substring(0,4));
		int m = Integer.parseInt(date.substring(5,7));
		int d = Integer.parseInt(date.substring(8,10));
		
		Date given = new Date(y-1900,m-1,d);
		Date today = new Date();
		
		return given.after(today);
	}
  
  /**Return true if the given text is empty*/
	public static boolean isEmpty(String txt){		 
		txt = txt.trim();
		if(txt.equals("")){
			return true;
		}
		else{
			return false;
		}
	}
  
  /**Return true if the given text is only containing the checked string*/
	public static boolean isEmpty(String txt, String mask){	 
		txt = txt.trim();
		if(txt.equals(mask)){
			return true;
		}
		else{
			return false;
		}
	}
	
  /**Return true only if all the characters are letters*/
	public static boolean isLetters(String txt){
		boolean output = false;
		int l = txt.length();
		for(int i=0;i<l;i++){
			if(Character.isDigit(txt.charAt(i))){
				output = false;
				break;
			}
			else{
				output = true;
			}
		}
		return output;
	}
	
  /**Return true if the given text starts with a letter*/
	public static boolean isStartingWithALetter(String txt){
		String text = txt;
		return Character.isLetter(text.charAt(0));
	}
  
  /**Return true for valide number*/
	public static boolean isNumber(String txt){
		try{
			Double.parseDouble(txt);
			return true;
		}
		catch(NumberFormatException nfe){
			return false;
		}
	}
	
  /**Return true for valid positive numbers*/
  public static boolean isNonNegative(String txt){
		try{
			if(isNumber(txt) && (Double.parseDouble(txt)>=0))
				return true;
			else
				return false;
		}
		catch(NumberFormatException nfe){
			return false;
		}
	}
        
    public static boolean isPositive(String txt){
                  try{
                          if(isNumber(txt) && (Double.parseDouble(txt)>0))
                                  return true;
                          else
                                  return false;
                  }
                  catch(NumberFormatException nfe){
                          return false;
                  }
          }
}
