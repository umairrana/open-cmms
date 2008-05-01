package com.matrix.focus.inspection;


import com.matrix.focus.inspection.Reading_Log_TableCellRenderer;

import com.matrix.focus.util.DBConnectionPool;

import java.sql.*;
import javax.swing.JTable;

public class Inspection_Log_Table
{
    private Connection con;
    private ResultSet rsFromInsMaster;
    private ResultSet rsCategCount;
    private ResultSet rsLogVals;
    private Inspection_Log_Table_Model tm;
    private JTable table;
    private DBConnectionPool pool;
    
    public Inspection_Log_Table(DBConnectionPool pool, String strSONo)
    {  
        con = null;
        rsFromInsMaster = null;
        rsCategCount = null;
        rsLogVals = null;
        tm = null;
        table = null;
        this.pool = pool;
        con = pool.getConnection();
        
        try
        {
             /******************************************************************/

            Statement st2 = con.createStatement();
            
            rsFromInsMaster = st2.executeQuery("SELECT i.Category, i.Description, i.Inspection_ID " +
                                               "FROM inspectionmaster i, preventive_maintenance_work_order w, machine_Inspection mi " +
                                               "WHERE w.PM_Work_Order_ID = '" + strSONo + "' AND " +
                                               "w.Asset_ID = mi.Machine_No AND " +
                                               "mi.Inspection_ID = i.Inspection_ID " +
                                               "ORDER BY Category, Description");
            
            
            Statement st3 = con.createStatement();
            rsCategCount = st3.executeQuery("SELECT i.Category, COUNT(i.Inspection_ID) " +
                                            "FROM inspectionmaster i, preventive_maintenance_work_order w, machine_Inspection mi " +
                                            "WHERE w.PM_Work_Order_ID = '" + strSONo + "' AND " +
                                            "w.Asset_ID = mi.Machine_No AND " +
                                            "mi.Inspection_ID = i.Inspection_ID " +
                                            "GROUP BY Category");
                   
            /******************************************************************/     
            
            
            if(strSONo != "new")
            {
                Statement st4 = con.createStatement();
                rsLogVals = st4.executeQuery("SELECT Inspection_ID,Good,Poor,Cleaned,Repaired,Replaced,ReplaceatNext " +
                                             "FROM inspection_log " +
                                             "WHERE SO_No = '" + strSONo + "' ORDER BY Inspection_ID");
            }
            
            tm = new Inspection_Log_Table_Model(rsFromInsMaster, rsCategCount, strSONo, rsLogVals);
            
            table = new JTable(tm);
            Reading_Log_TableCellRenderer myRend = new Reading_Log_TableCellRenderer();
            table.getColumnModel().getColumn(0).setCellRenderer(myRend);
            table.getColumnModel().getColumn(1).setPreferredWidth(220);
            table.getColumnModel().getColumn(7).setPreferredWidth(150);
            table.setRowHeight(20);
            table.putClientProperty("terminateEditOnFocusLost",Boolean.TRUE);
        }
        catch(Exception e)
        {
            //JOptionPane.showMessageDialog(null,"Database Error\n" + e.toString(),"DB Error",JOptionPane.OK_OPTION);
            //e.printStackTrace();
            e.toString();
        }
    }

    public JTable getRSTable()
    {
        table.repaint();
        return table;
    }

    public Object[][] getResultArray()
    {
        return tm.getResultArray();
    }

    
}
