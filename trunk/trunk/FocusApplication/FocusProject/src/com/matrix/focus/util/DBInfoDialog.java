package com.matrix.focus.util;

import com.matrix.components.MTextbox;
import com.matrix.focus.mdi.messageBar.MessageBar;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class DBInfoDialog extends JDialog {
    private JPanel jPanel1 = new JPanel();
    private MTextbox txtDriver = new MTextbox();
    private MTextbox txtURL = new MTextbox();
    private MTextbox txtUsername = new MTextbox();
    private JLabel jLabel1 = new JLabel();
    private JPasswordField txtPassword = new JPasswordField();
    private JButton jButton1 = new JButton();
    private JButton jButton2 = new JButton();

    public DBInfoDialog() {
        this(null, "", true);
    }

    public DBInfoDialog(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLocation(350,300);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(new Dimension(362, 213));
        this.getContentPane().setLayout( null );
        this.setTitle("Database Connection Information");
        jPanel1.setBounds(new Rectangle(5, 5, 345, 140));
        jPanel1.setBorder(BorderFactory.createTitledBorder("Database Connection Information"));
        jPanel1.setLayout(null);
        txtDriver.setBounds(new Rectangle(25, 25, 300, 20));
        txtDriver.setCaption("Database Driver");
        txtDriver.setInputText("com.mysql.jdbc.Driver");
        txtURL.setBounds(new Rectangle(25, 50, 295, 20));
        txtURL.setCaption("Database URL");
        txtURL.setInputText("jdbc:mysql://192.168.0.17/focus");
        txtUsername.setBounds(new Rectangle(25, 75, 305, 20));
        txtUsername.setCaption("Username");
        txtUsername.setInputText("focus");
        jLabel1.setText("Password");
        jLabel1.setBounds(new Rectangle(25, 95, 50, 25));
        txtPassword.setBounds(new Rectangle(120, 100, 200, 20));
        txtPassword.setText("matrixit");
        jButton1.setText("Save");
        this.getRootPane().setDefaultButton(jButton1);
        jButton1.setBounds(new Rectangle(175, 150, 85, 25));
        jButton1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        jButton1_actionPerformed(e);
                    }
                });
        jButton2.setText("Cancel");
        jButton2.setBounds(new Rectangle(265, 150, 85, 25));
        jButton2.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        jButton2_actionPerformed(e);
                    }
                });
        jPanel1.add(txtPassword, null);
        jPanel1.add(jLabel1, null);
        jPanel1.add(txtUsername, null);
        jPanel1.add(txtURL, null);
        jPanel1.add(txtDriver, null);
        this.getContentPane().add(jButton2, null);
        this.getContentPane().add(jButton1, null);
        this.getContentPane().add(jPanel1, null);
        
        Properties pro = Utilities.getProperties("conf/dbconnection.inf");
        txtDriver.setInputText(pro.getProperty("DRIVER"));
        txtURL.setInputText(pro.getProperty("URL"));
        txtUsername.setInputText(pro.getProperty("USERNAME"));
        txtPassword.setText(pro.getProperty("PASSWORD"));
        setVisible(true);
    }

    private void jButton1_actionPerformed(ActionEvent e) {
        try {
            Utilities.setProperties("conf/dbconnection.inf",
                                new String[][]{
                                    {"DRIVER",txtDriver.getInputText()},
                                    {"URL",txtURL.getInputText()},
                                    {"USERNAME",txtUsername.getInputText()},
                                    {"PASSWORD",txtPassword.getText()}
                                }
                                ,"Database Connection Information");
            System.exit(0);
        } catch (Exception f) {
            f.printStackTrace();
        }
        setVisible(false);
    }

    private void jButton2_actionPerformed(ActionEvent e){
        setVisible(false);
        System.exit(0);
    }
}
