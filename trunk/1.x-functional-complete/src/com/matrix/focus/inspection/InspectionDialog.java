package com.matrix.focus.inspection;
/*inspectionDialog.java
 * Original Author - Yasantha for MatrixMeter
 * Redeveloped by Dimuthu for MatrixFocus
 * 2007 - 07 - 10
 */

import com.matrix.focus.mdi.MDI;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.DBConnectionPool;

import com.matrix.focus.util.ImageLibrary;

import java.awt.*;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InspectionDialog extends JDialog
{
    private JButton btnSave;
    private Connection con;
    private Inspection_Log_Table insTable;
    private JScrollPane jsp2;
    private String strSONo = "new";
    private JLabel jLabel1 = new JLabel();
    private JPanel jPanel1 = new JPanel();
    private JButton btnCan = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
    private DBConnectionPool pool;
    private MDI frame;
    private MessageBar messagebar;

    public InspectionDialog(DBConnectionPool pool, String strSONo,MDI frame, MessageBar messagebar){
        btnSave = new JButton(new ImageIcon(ImageLibrary.BUTTON_SAVE));
        con = null;
        insTable = null;
        jsp2 = new JScrollPane();
        this.frame = frame;
        this.messagebar = messagebar;
        
        try
        {
            con = pool.getConnection();
            this.pool = pool;
            this.strSONo = strSONo;
            jbInit();
            setLayout(null);
            setResizable(false);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setTitle("Inspection Log details - Job No: '" + strSONo + "'");
        setSize(new Dimension(875, 614));
        getContentPane().setLayout(null);
        btnSave.setText("Save");
        btnSave.setBounds(new Rectangle(640, 550, 110, 25)); //buttonlocation        
        btnSave.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                btnSave_actionPerformed(e);
            }
        });
        
        btnCan.setText("Cancel");
        btnCan.setBounds(new Rectangle(760, 550, 90, 25));
        btnCan.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnCan_actionPerformed(e);
                    }
                });
                
        this.getContentPane().add(btnCan, null);
        this.getContentPane().add(jPanel1, null);
        this.getContentPane().add(jLabel1, null);
        getContentPane().add(btnSave, null);
        
        insTable = new Inspection_Log_Table(pool, strSONo);
        
        insTable.getRSTable().setAutoResizeMode(0);
        jsp2.setVerticalScrollBarPolicy(22);
        jsp2.setHorizontalScrollBarPolicy(32);
        jsp2.setSize(new Dimension(700, 650));
        jsp2.setBounds(new Rectangle(0, 0, 850, 500)); //scroll pane size

        jLabel1.setText("Enter the Corresponding Log details for the job no '" +
                        strSONo + "'");
        jLabel1.setBounds(new Rectangle(10, 15, 495, 25));
        jPanel1.setBounds(new Rectangle(5, 45, 855, 500));
        jPanel1.setLayout(null);
        jPanel1.add(jsp2);
        jsp2.getViewport().add(insTable.getRSTable());
        getContentPane().add(jPanel1, null);
               
        this.setSize(875,620); //windows size
        this.setAlwaysOnTop(true);
        this.setLocationRelativeTo(null);
    }

    private boolean isExist(String query){
        boolean result = false;
        ResultSet rs = null;
        try
        {
            Statement st = con.createStatement();
            rs = st.executeQuery(query);
            rs.last();
            if(rs.getRow() == 0)
                result = false;
            else
                result = true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    private int addRecord(String query) {
        Statement st = null;
        int success = 1;
        try
        {
            st = con.createStatement();
            success = st.executeUpdate(query);
        }
        catch(SQLException err)
        {
            err.printStackTrace();
            success = 0;
        }
        return success;
    }

    private void saveInspections(String strSONo) {
        Object resultArray[][] = insTable.getResultArray();
        int success = 1;
        for(int x = 0; x < resultArray.length; x++)
        {
            String isExQuery = "SELECT * " +
            "FROM inspection_log " +
            "WHERE SO_No='" + strSONo + "' AND " +
            "Inspection_ID=" + resultArray[x][8].toString();
            
            if(!isExist(isExQuery))
            {
            
                String query = "INSERT INTO inspection_log VALUES('" + 
                strSONo + "','" + 
                resultArray[x][8].toString().trim() + "','" + 
                resultArray[x][2].toString().trim() + "','" + 
                resultArray[x][3].toString().trim() + "','" + 
                resultArray[x][4].toString().trim() + "','" + 
                resultArray[x][5].toString().trim() + "','" + 
                resultArray[x][6].toString().trim() + "','" + 
                resultArray[x][7].toString().trim() +"')";
                success *= addRecord(query);
            } else
            {
                String query = "UPDATE inspection_log " +
                                "SET Good = '" + resultArray[x][2].toString().trim() + "', " +
                                "Poor = '" + resultArray[x][3].toString().trim() + "', " +
                                "Cleaned = '" + resultArray[x][4].toString().trim() + "', " +
                                "Repaired = '" + resultArray[x][5].toString().trim() + "', " +
                                "Replaced = '" + resultArray[x][6].toString().trim() + "', " +
                                "ReplaceatNext = '" + resultArray[x][7].toString().trim() + "' " +
                                "WHERE SO_No = '" + strSONo + "' " +
                                "AND Inspection_ID = " + resultArray[x][8];
                success *= addRecord(query);
            }
        }

            if(success != 0)
                JOptionPane.showMessageDialog(this, "Data saved successfully");
            else
                JOptionPane.showMessageDialog(this, "Error occured on saving data. Please Contact system administrator !");
    }

    private void btnSave_actionPerformed(ActionEvent e) {
        saveInspections(strSONo);
        e.toString();
    }
    
    private void btnCan_actionPerformed(ActionEvent e) {
        this.setVisible(false);
        e.toString();
    }
}
