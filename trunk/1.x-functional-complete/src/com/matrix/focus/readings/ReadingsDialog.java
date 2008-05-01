package com.matrix.focus.readings;


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

public class ReadingsDialog extends JDialog
{   
    private JButton btnSave;
    private Connection con;
    private Reading_Log_Table readingTable;
    private JScrollPane jsp2;
    private String strSONo = "new";
    private JButton btnCancel = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
    private JPanel jPanel1 = new JPanel();
    private JLabel jLabel1 = new JLabel();
    private DBConnectionPool pool;    
    private MDI frame;
    private MessageBar messagebar;

    public ReadingsDialog(DBConnectionPool pool, String strSONo,MDI frame, MessageBar messagebar)
    {
        btnSave = new JButton(new ImageIcon(ImageLibrary.BUTTON_SAVE));
        con = null;
        readingTable = null;
        jsp2 = new JScrollPane();
        this.frame = frame;
        this.messagebar = messagebar;
        //strSONo = "new";
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
        this.setTitle("Reading Log details - Job No: '" + strSONo + "'");
        setSize(new Dimension(875, 637));
        getContentPane().setLayout(null);
        btnSave.setText("Save");
        btnSave.setBounds(new Rectangle(640, 540, 110, 25)); //buttonlocation
        btnSave.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                btnSave_actionPerformed(e);
            }

        });
        
        btnCancel.setText("Cancel");
        btnCancel.setBounds(new Rectangle(750, 540, 100, 25));
        btnCancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnCancel_actionPerformed(e);
                    }
                });

        this.getContentPane().add(jLabel1, null);
        jPanel1.add(jsp2);
        this.getContentPane().add(jPanel1, null);
        this.getContentPane().add(btnCancel, null);
        getContentPane().add(btnSave, null);

        readingTable = new Reading_Log_Table(pool, strSONo);
        readingTable.getRSTable().setAutoResizeMode(1);

        jsp2.setVerticalScrollBarPolicy(22);
        jsp2.setHorizontalScrollBarPolicy(32);
        jsp2.setSize(new Dimension(700, 650));
        jsp2.setBounds(new Rectangle(0, 0, 850, 500)); //scroll pane size

   
        jPanel1.setBounds(new Rectangle(10, 35, 860, 520));
        jPanel1.setLayout(null);
        jLabel1.setText("Enter the Corresponding Log details for the job no '" + 
                        strSONo + "'");
        jLabel1.setBounds(new Rectangle(10, 15, 495, 25));
        jsp2.getViewport().add(readingTable.getRSTable());
        getContentPane().add(jPanel1, null);
        
        this.setSize(875,600); //windows size
        this.setAlwaysOnTop(true);
        this.setLocationRelativeTo(null);
    }

    private boolean isExist(String query)
    {
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

    private int addRecord(String query)
    {
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

    private void saveReadings(String strSONo)
    {
        Object resultArray[][] = readingTable.getResultArray();
        int success = 1;
        for(int x = 0; x < resultArray.length; x++)
        {
            String isExQuery = (new StringBuilder()).append("SELECT * FROM readings_log WHERE SO_No='").append(strSONo).append("' AND Reading_ID=").append(resultArray[x][5]).toString();
            if(!isExist(isExQuery))
            {
                String query = (new StringBuilder()).append("INSERT INTO readings_log VALUES('").append(strSONo).append("','").append(resultArray[x][5]).append("','").append(resultArray[x][2]).append("','").append(resultArray[x][3]).append("','").append(resultArray[x][4]).append("')").toString();
                success *= addRecord(query);
            } else
            {
                String query = (new StringBuilder()).append(" UPDATE `readings_log` r SET r.Value = '").append(resultArray[x][2]).append("', r.Normal=+'").append(resultArray[x][3]).append("', r.Remarks = '").append(resultArray[x][4]).append("'").append(" WHERE SO_No = '").append(strSONo).append("' AND Reading_ID = '").append(resultArray[x][5]).append("'").toString();
                success *= addRecord(query);
            }
        }

        if(success != 0)
            JOptionPane.showMessageDialog(this, "Data saved successfully");
        else
            JOptionPane.showMessageDialog(this, "Error occured on saving data. Please Contact system administrator !");
    }

    private void btnSave_actionPerformed(ActionEvent e)
    {
        saveReadings(strSONo);
        e.toString();
    }

    private void btnCancel_actionPerformed(ActionEvent e) {
        this.setVisible(false);
        e.toString();
    }
}
