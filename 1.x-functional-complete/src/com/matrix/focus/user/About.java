package com.matrix.focus.user;

import com.matrix.focus.util.ImageLibrary;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class About extends JDialog {
    private JLabel jLabel1 = new JLabel();
    private JLabel lblf = new JLabel();
    private JLabel jLabel2 = new JLabel();
    private JTextArea jTextArea1 = new JTextArea();
    private JLabel jLabel4 = new JLabel();
    private JLabel jLabel3 = new JLabel();
    private JButton cmdOK = new JButton(new ImageIcon(ImageLibrary.BUTTON_OK));

    public About(JFrame frame, String title) {
        this(frame, "About "+title, true);
    }

    public About(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        try {
            jbInit();
            jTextArea1.append("\n");
            jTextArea1.append("\nOperating System : "+ System.getProperty(( "os.name" ), "os_name" ));
            jTextArea1.append("\nProcessor : "+ System.getProperty(( "os.arch" ), "os_arch" ));
            jTextArea1.append("\nOS Version : "+ System.getProperty(( "os.version" ), "os_version" ));
            jTextArea1.append("\n\nJava Vendor : "+ System.getProperty(( "java.vendor" ), "java_vendor" ));
            jTextArea1.append("\nJava Class Version : "+ System.getProperty(( "java.class.version" ), "java_class_version" ));
            jTextArea1.append("\nJava Version : "+ System.getProperty(( "java.version" ), "java_version" ));
            jTextArea1.append("\n\nUser Name : "+ System.getProperty(( "user.name" ), "user_name" ));
           // jTextArea1.append("\nOperating System : "+ System.getProperty(( "java.vendor" ), "java_vendor" ));
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        lblf.setIcon(new ImageIcon(ImageLibrary.FOCUS_LOGO));
        this.setSize(new Dimension(461, 404));
        this.getContentPane().setLayout( null );
        jLabel1.setBounds(new Rectangle(0, 0, 455, 65));
        jLabel1.setOpaque(true);
        jLabel1.setBackground(new Color(198, 198, 198));
        lblf.setBounds(new Rectangle(0, 0, 130, 65));
        lblf.setHorizontalAlignment(SwingConstants.CENTER);
        lblf.setHorizontalTextPosition(SwingConstants.CENTER);
        jLabel2.setText("Maintenance Management System");
        jLabel2.setBounds(new Rectangle(60, 10, 360, 45));
        jLabel2.setFont(new Font("Tahoma", 1, 14));
        jLabel2.setHorizontalTextPosition(SwingConstants.CENTER);
        jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        jTextArea1.setBounds(new Rectangle(5, 75, 445, 225));
        jTextArea1.setLineWrap(true);
        jTextArea1.setText("WARNING: THIS COMPUTER PROGRAM IS PROTECTED BY COPYRIGHT LAW AND \n" +
        "INTERNATIONAL TREATIES. \n\n" + 
        "UNAUTHORIZED REPRODUCTION, DISTRIBUTION OR USE OF THIS PROGRAM, " + 
        "\nOR ANY PORTION OF IT, MAY RESULT IN SEVERE CIVIL AND CRIMINAL PENALTIES, " + 
        "\nAND WILL BE PROSECUTED TO THE MAXIMUM EXTENT POSSIBLE UNDER THE LAW.");
        jTextArea1.setFont(new Font("Tahoma", 0, 10));
        jTextArea1.setEditable(false);
        jLabel4.setOpaque(true);
        jLabel4.setBackground(new Color(198, 198, 198));
        jLabel4.setBounds(new Rectangle(0, 345, 455, 35));
        jLabel3.setText("Copyright 2005-2007 Matrix IT Solutions. All Rights Reserved.");
        jLabel3.setBounds(new Rectangle(20, 345, 410, 30));
        jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel3.setHorizontalTextPosition(SwingConstants.CENTER);
        cmdOK.setText("OK");
        cmdOK.setBounds(new Rectangle(355, 310, 95, 25));
        cmdOK.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        cmdOK_actionPerformed(e);
                    }
                });
        this.getContentPane().add(cmdOK, null);
        this.getContentPane().add(jLabel3, null);
        this.getContentPane().add(jLabel4, null);
        this.getContentPane().add(jTextArea1, null);
        this.getContentPane().add(jLabel2, null);
        this.getContentPane().add(lblf, null);
        this.getContentPane().add(jLabel1, null);
        setResizable(false);
    }

    private void cmdOK_actionPerformed(ActionEvent e) {
        this.setVisible(false);
    }
}
