package com.matrix.focus.readings;

import com.matrix.focus.master.gui.GUIConditionMonitoring;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.AbstractTableModel;
import java.util.Vector;


public class MasterTaskDataCM extends AbstractTableModel
{
  public static Vector vector=new Vector();
  public static final int COL_ID = 0;
  public static final int COL_DESC = 1;
  public static  int x=1;
  private static final String [] cols = {"ID","Description"}; 
  private static Connection conn;
  JPanel parent;
  
  
  public MasterTaskDataCM(Connection c, JPanel p)
  {
    parent=p;
    this.conn = c;
  }
  public int getRowCount() 
  {
    return vector==null ? 0 : vector.size(); 
  }
  public boolean isCellEditable(int nRow, int nCol) 
  {
    return false;
  }   
     
  public int getColumnCount() 
  {
    //return columns.length;
    return cols.length;
  }
        
  public String getColumnName(int column) 
  { 
    //return columns[column].m_title; 
    return cols[column];
  }
  
  public static void insert(int row) 
  {
    if (row < 0)
      row = 0;
    if (row > vector.size())
      row = vector.size();
      vector.insertElementAt(new MasterTaskCM(1,""), row);
     
  }
  
  public static void insert(int row,String desc) {
    if (row < 0)
      row = 0;
    if (row > vector.size())
      row = vector.size();
      vector.insertElementAt(new MasterTaskCM(row,desc), row);
     
  }

  public static boolean delete(int row,String cat,String desc,String tableused) {
    if (row < 0 || row >= vector.size())
      return false;
    vector.remove(row);
    try
    {
    String sql="delete from "+tableused+" where Category=? and Description=?";
      PreparedStatement pst= conn.prepareStatement(sql);
      pst.setString(1,cat);
      pst.setString(2,desc);
      pst.executeUpdate();
      JOptionPane.showMessageDialog(null,"Deleted ");
      
    }
    catch(Exception e){e.printStackTrace();}
      return true;
  }



  public Object getValueAt(int nRow, int nCol) {
    if (nRow < 0 || nRow>=getRowCount())
      return "";
    MasterTaskCM row = (MasterTaskCM)vector.elementAt(nRow);
    switch (nCol) {
       case COL_ID:return new Integer(row.id);
       case COL_DESC: return row.desc;
      
    }
    return "";
  }
  
  public void setValueAt(Object value, int nRow, int nCol) {
    if (nRow < 0 || nRow>=getRowCount())
     return;
    MasterTaskCM row = (MasterTaskCM)vector.elementAt(nRow);
    String svalue = value.toString();
    
    switch (nCol) {
     
      case COL_ID:
            row.id = Integer.parseInt(svalue);
            break;
     
      case COL_DESC:
            row.desc= svalue; 
            break;
     
    }
  }
  
   public static void ViewSavedCategory(String s, String tableused)
   {
   
   try
   {
       Statement st=conn.createStatement();
       String sql="select Description from "+ tableused +" where Category='"+s.trim()+ "'";
       ResultSet rs=st.executeQuery(sql);
       vector=new Vector();
      
       int x=1;
       while(rs.next())
       {
         MasterTaskCM mt=new MasterTaskCM(x,rs.getString(1));
         vector.addElement(mt);
         //System.out.println(mt.desc);
         x++;        
       }
       
       //GUIConditionMonitoring.table.repaint();
   }
   catch(Exception e){
    //JOptionPane.showMessageDialog(null,e);
    }
 }  
}

class MasterTaskCM
{
  public int id=0;
  public String desc="";
  public MasterTaskCM(int a,String b)
  {
    id=a;
    desc=b;
  }
    
}