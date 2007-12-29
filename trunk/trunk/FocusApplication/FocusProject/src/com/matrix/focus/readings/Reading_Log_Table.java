package com.matrix.focus.readings;

import com.matrix.focus.inspection.Reading_Log_TableCellRenderer;
//import Util.DBConnection;
//import com.matrix.focus.util.DatabaseConnection;

import com.matrix.focus.util.DBConnectionPool;

import java.sql.*;

import javax.swing.JOptionPane;
import javax.swing.JTable;

public class Reading_Log_Table
{     
    private Connection con;
    private ResultSet rsFromInsMaster;
    private ResultSet rsCategCount;
    private ResultSet rsLogVals;
    private Reading_Log_Table_Model tm;
    private JTable table;
    private DBConnectionPool pool;
    
    public Reading_Log_Table(DBConnectionPool pool, String strSONo)
    {
        con = null;
        rsFromInsMaster = null;
        rsCategCount = null;
        rsLogVals = null;
        tm = null;
        table = null;
        Statement st2;        
        this.con = pool.getConnection() ;
        this.pool = pool;
        
        try
        {
                                            
            //st2 = dbcon.getConnection().createStatement();
            st2 = con.createStatement();
            rsFromInsMaster = st2.executeQuery("SELECT r.Category, r.Description, r.Reading_ID " + 
                                                "FROM readingsmaster r, preventive_maintenance_work_order w, machine_reading mr " +
                                                "WHERE w.PM_Work_Order_ID = '" + strSONo + "' AND " +
                                                "w.Asset_ID = mr.Machine_No AND " +
                                                "mr.Reading_ID = r.Reading_ID " +
                                               "ORDER BY Category");

            //Statement st3 =  dbcon.getConnection().createStatement();
            Statement st3 = con.createStatement();
            rsCategCount = st3.executeQuery("SELECT r.Category,COUNT(r.Reading_ID) " +
                                            "FROM readingsmaster r, preventive_maintenance_work_order w, machine_reading mr " +
                                            "WHERE w.PM_Work_Order_ID = '" + strSONo + "' AND " +
                                            "w.Asset_ID = mr.Machine_No AND " +
                                            "mr.Reading_ID = r.Reading_ID " +
                                            "GROUP BY Category");
            
            if(strSONo != "new")
            {
                //Statement st4 =  dbcon.getConnection().createStatement();
                Statement st4 =  con.createStatement();
                rsLogVals = st4.executeQuery((new StringBuilder()).append("SELECT Reading_ID,Value,Normal,Remarks FROM readings_log WHERE SO_No= '").append(strSONo).append("' ORDER BY Reading_ID").toString());
            }
            
            tm = new Reading_Log_Table_Model(rsFromInsMaster, rsCategCount, strSONo, rsLogVals);
            
            table = new JTable(tm);
            table.getColumnModel().getColumn(1).setPreferredWidth(220);
            table.setRowHeight(20);
            table.putClientProperty("terminateEditOnFocusLost",Boolean.TRUE);
            Reading_Log_TableCellRenderer myRend = new Reading_Log_TableCellRenderer();
            table.getColumnModel().getColumn(0).setCellRenderer(myRend);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,"Database Error\n" + e.toString(),"DB Error",JOptionPane.OK_OPTION);
            e.printStackTrace();
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