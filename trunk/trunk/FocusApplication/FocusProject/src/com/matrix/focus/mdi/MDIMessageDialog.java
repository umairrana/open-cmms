package com.matrix.focus.mdi;

import com.matrix.components.MTextbox;

import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.ImageLibrary;



import java.awt.Dimension;
import java.awt.Frame;

import java.awt.Rectangle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MDIMessageDialog extends JDialog {

    private MTextbox txtUser = new MTextbox();
    private MTextbox txtTime = new MTextbox();
    private JLabel jLabel1 = new JLabel();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JTextArea txtMessage = new JTextArea();
    private JButton btnClose = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
    private JButton btnSave = new JButton(new ImageIcon(ImageLibrary.BUTTON_SAVE));
    private JButton btnDelete = new JButton(new ImageIcon(ImageLibrary.BUTTON_DELETE));
    private MDIMessage message;
    private Connection connection;
    private MessageBar messageBar;

    public MDIMessageDialog(JFrame frame,MessageBar msgBar,MDIMessage message,Connection connection){
        this(frame, "Message", true);
        this.messageBar = msgBar;
        this.setLocationRelativeTo(frame);
        this.message = message;
        this.connection = connection;
        txtUser.setInputText(this.message.username);
        txtTime.setInputText(this.message.timestamp);
        txtMessage.setText(this.message.message);
    }
    
    public MDIMessageDialog(JFrame frame,MessageBar msgBar,Connection connection){
        this(frame, "Message", true);
        this.messageBar = msgBar;
        this.setLocationRelativeTo(frame);
        this.connection = connection;
        this.message = new MDIMessage("new","Type your message here.",MDI.USERNAME,"NOW");
        txtUser.setInputText(this.message.username);
        txtTime.setInputText(this.message.timestamp);
        txtMessage.setText(this.message.message);
    }

    public MDIMessageDialog(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setSize(new Dimension(400, 327));
        this.getContentPane().setLayout( null );
        txtUser.setBounds(new Rectangle(15, 10, 245, 20));
        txtUser.setCaption("User");
        txtUser.setTxtWidth(150);
        txtUser.setEditable(false);
        txtTime.setBounds(new Rectangle(15, 35, 215, 20));
        txtTime.setCaption("Time");
        txtTime.setTxtWidth(120);
        txtTime.setEditable(false);
        jLabel1.setText("Message");
        jLabel1.setBounds(new Rectangle(15, 55, 55, 20));
        jScrollPane1.setBounds(new Rectangle(10, 75, 375, 180));
        btnClose.setText("Close");
        btnClose.setBounds(new Rectangle(310, 260, 75, 25));
        btnClose.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnClose_actionPerformed(e);
                    }
                });
        btnSave.setText("Save");
        btnSave.setBounds(new Rectangle(150, 260, 75, 25));
        btnSave.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnSave_actionPerformed(e);
                    }
                });
        btnDelete.setText("Delete");
        btnDelete.setBounds(new Rectangle(230, 260, 75, 25));
        btnDelete.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnDelete_actionPerformed(e);
                    }
                });
        jScrollPane1.getViewport().add(txtMessage, null);
        this.getContentPane().add(btnDelete, null);
        this.getContentPane().add(btnSave, null);
        this.getContentPane().add(btnClose, null);
        this.getContentPane().add(jScrollPane1, null);
        this.getContentPane().add(jLabel1, null);
        this.getContentPane().add(txtTime, null);
        this.getContentPane().add(txtUser, null);
    }

    private void btnClose_actionPerformed(ActionEvent e) {
        dispose();
    }

    private void btnSave_actionPerformed(ActionEvent e){
        if(message.username.equals(MDI.USERNAME)){
            saveOrUpdate();
        }
        else{
            messageBar.setMessage("You can not edit this message.","ERROR");
        }
        
    }

    private void btnDelete_actionPerformed(ActionEvent e) {
        if(!message.msg_id.equals("new")){
             String sql = "UPDATE mdi_message SET deleted ='true' WHERE id=?";
             try {
                 PreparedStatement stmt = connection.prepareStatement(sql);
                 stmt.setString(1,message.msg_id);
                     if(stmt.executeUpdate()==1){
                         messageBar.setMessage("Message deleted.","OK");
                     }
                     else{
                         messageBar.setMessage("Message was not deleted.","ERROR");
                     }
             }
             catch (SQLException er) {
                 er.printStackTrace();
             }
        }
        dispose();
    }

    private void saveOrUpdate() {
        if(message.msg_id.equals("new")){
            String sql = "INSERT INTO mdi_message (message,username) VALUES(?,?)";
            try {
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1,txtMessage.getText());
                stmt.setString(2,MDI.USERNAME);
                if(stmt.executeUpdate()==1){
                    messageBar.setMessage("Message saved.","OK");
                }
                else{
                    messageBar.setMessage("Message was not saved.","ERROR");
                }
            }
            catch (SQLException er) {
                er.printStackTrace();
            }
        }
        else{
             String sql = "UPDATE mdi_message SET message = ?,created_time=NOW() WHERE id=?";
             try {
                 PreparedStatement stmt = connection.prepareStatement(sql);
                 stmt.setString(1,txtMessage.getText());
                 stmt.setString(2,message.msg_id);
                     if(stmt.executeUpdate()==1){
                         messageBar.setMessage("Message saved.","OK");
                     }
                     else{
                         messageBar.setMessage("Message was not saved.","ERROR");
                     }
             }
             catch (SQLException er) {
                 er.printStackTrace();
             }
        }
    }
}
