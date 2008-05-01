package com.matrix.focus.log;

import com.matrix.focus.util.Utilities;

import java.awt.Dimension;

import java.sql.Connection;

import java.sql.DriverManager;

import java.util.Properties;

import javax.swing.JFrame;

public class GUIJobCompletionRemarks extends JFrame {
    private String url,username,password;
    public GUIJobCompletionRemarks() {
        
        try {
            Properties pro = Utilities.getProperties("conf/dbconnection.inf");
            String driver = pro.getProperty("DRIVER");
            url = pro.getProperty("URL");
            username = pro.getProperty("USERNAME");
            password = pro.getProperty("PASSWORD");
            
            /**Without the pool*/
            Class.forName (driver);
            
            Connection con = DriverManager.getConnection("192.168.0.18",username,password);
            
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout( null );
        this.setSize(new Dimension(548, 300));
        this.setTitle( "Job Completion Remarks" );
    }
    
    public static void main(String[] args) {
        new GUIJobCompletionRemarks().setVisible(true);
    }
    
    
}
