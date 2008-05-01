package com.matrix.focus.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class ViewOnlyRSTable 
{ 
  ResultSet resultSet;
  static JTable table;
  Vector v;
  int r,c;
  

  public ViewOnlyRSTable(ResultSet rs){
    this.resultSet=rs;
    
    try{
      ResultSetMetaData meta = resultSet.getMetaData();
      c = meta.getColumnCount();
      r = resultSet.getRow();
     
      ViewOnlyTableModel tm = new ViewOnlyTableModel(rs);
      table = new JTable(tm);
      table.setAutoCreateRowSorter(true);    
    }catch(Exception ex){
      ex.printStackTrace();
    }
    
  }
  
  public static JTable getRSTable()
  {
    table.repaint();
    return table;
  }
  
 

}
abstract class VOTestModel extends AbstractTableModel
{  
   /**
      Constructs the table model.
      @param aResultSet the result set to display.
   */
   //DatabaseConnection dbcon = new DatabaseConnection();
   int row=0;
   public VOTestModel(ResultSet aResultSet)
   {  
      rs = aResultSet;
      try
      {  
         rsmd = rs.getMetaData();
      }
      catch(SQLException e)
      {  
         e.printStackTrace();
      }
   }

   public String getColumnName(int c)
   {  
      try
      {  
         return rsmd.getColumnName(c + 1);
      }
      catch(SQLException e)
      {  
         e.printStackTrace();
         return "";
      }
   }

   public int getColumnCount()
   {  
      try
      {  
         
         return rsmd.getColumnCount();
      }
      catch(SQLException e)
      {  
         e.printStackTrace();
         return 0;
      }
   }

   /**
      Gets the result set that this model exposes.
      @return the result set
   */
   protected ResultSet getResultSet()
   {  
      return rs;
   }

   public ResultSet rs;
   public ResultSetMetaData rsmd;
}

/**
   This class uses a scrolling cursor, a JDBC 2 feature,
   to locate result set elements.
*/
class ViewOnlyTableModel extends VOTestModel{  
   /**
      Constructs the table model.
      @param aResultSet the result set to display.
   */
   public ViewOnlyTableModel(ResultSet rs)
   {  
      super(rs);
   }

   public Object getValueAt(int r, int c)
   {  
      try
      {  
         ResultSet rs = getResultSet();
         rs.absolute(r + 1);
         return rs.getObject(c + 1);
      }
      catch(SQLException e)
      {  
         e.printStackTrace();
         return null;
      }
   }

   public int getRowCount()
   {  
      try
      {  
         ResultSet rs = getResultSet();
         rs.last();
         return rs.getRow();
      }
      catch(SQLException e)
      {  
         e.printStackTrace();
         return 0;
      }
   }
   
   public void setValueAt(Object value,int row,int col)
   {
     try{                
        ResultSet rs = getResultSet();
        rs.absolute(row + 1);
        rs.updateString(col + 1, value.toString());
        rs.updateRow();
          
      }catch(Exception ex){
       ex.printStackTrace();
     }
   }
   
   public boolean isCellEditable(int r,int c)
   {  
      return false;
   }
   
    /*public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }*/

}




