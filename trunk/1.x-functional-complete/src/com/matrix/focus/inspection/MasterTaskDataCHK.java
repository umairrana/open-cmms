package com.matrix.focus.inspection;

import com.matrix.focus.master.gui.GUIChecklistItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.AbstractTableModel;
import java.util.Vector;

public class MasterTaskDataCHK extends AbstractTableModel
{
  public static Vector vector=new Vector();
  public static final int COL_ID = 0;
  public static final int COL_DESC = 1;
  public static  int x=1;
  public static final String columns[]={"ID","Description"};
  private JPanel parent;
  private static Connection con = null;
  
  public MasterTaskDataCHK(Connection c, JPanel p)
  {
    con = c;
    parent=p;
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
  return columns.length;
  }
        
  public String getColumnName(int column) 
  { 
  return columns[column]; 
  }
  
  public static void insert(int row) 
  {
    if (row < 0)
      row = 0;
    if (row > vector.size())
      row = vector.size();
      vector.insertElementAt(new MasterTaskCHK(1,""), row);
     
  }
  
  public static void insert(int row,String desc) {
    if (row < 0)
      row = 0;
    if (row > vector.size())
      row = vector.size();
      vector.insertElementAt(new MasterTaskCHK(row,desc), row);
     
  }

  public static boolean delete(int row,String cat,String desc,String tableused) {
    if (row < 0 || row >= vector.size())
      return false;
    vector.remove(row);
    try
    {
    String sql="delete from "+tableused+" where Category=? and Description=?";
      PreparedStatement pst= con.prepareStatement(sql);
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
    MasterTaskCHK row = (MasterTaskCHK)vector.elementAt(nRow);
    switch (nCol) {
       case COL_ID:return new Integer(row.id);
       case COL_DESC: return row.desc;
    }
    return "";
  }
  
  public void setValueAt(Object value, int nRow, int nCol) {
    if (nRow < 0 || nRow>=getRowCount())
     return;
    MasterTaskCHK row = (MasterTaskCHK)vector.elementAt(nRow);
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
  
   public static  void ViewSavedCategory( String s, String tableused)
   {
   try
   {
       Statement st=con.createStatement();
       String sql="select Description from "+ tableused +" where Category='"+s.trim()+ "'";
       ResultSet rs=st.executeQuery(sql);
      vector=new Vector();
      
       int x=1;
       while(rs.next())
       {
         MasterTaskCHK mt=new MasterTaskCHK(x,rs.getString(1));
         vector.addElement(mt);
         x++;
       }
       //GUIChecklistItem.table.repaint();
   }
   catch(Exception e){
    //e.printStackTrace();
    //JOptionPane.showMessageDialog(null,e.getMessage());
    }
 }
  
 
  
}

class MasterTaskCHK
{
public int id=0;
public String desc="";
public MasterTaskCHK(int a,String b)
  {
    id=a;
    desc=b;
  }
    
}
