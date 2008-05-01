package com.matrix.focus.util;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class MConfirmDialog extends JDialog {
    private MImage icon = new MImage(ImageLibrary.MESSAGEBAR_CONF);
    private JButton yes = new JButton(new ImageIcon(ImageLibrary.BUTTON_OK));
    private JButton no = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
    private JTextArea msg = new JTextArea();
    private int option = 0;
    
    public MConfirmDialog(JFrame parent, String title, boolean modal) {
        super(parent, title, modal);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public MConfirmDialog(JDialog parent, String title, boolean modal) {
        super(parent, title, modal);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setMessage(String message){
        msg.setText(message);
    }
    
    public int getSelectedOption(){
        return option;
    }

    private void jbInit() throws Exception {
        this.setResizable(false);
        this.setSize(new Dimension(405, 132));
        this.getContentPane().setLayout( null );
        icon.setBounds(new Rectangle(15, 10, 60, 60));
        yes.setText("Yes");
        yes.setBounds(new Rectangle(180, 65, 100, 25));
        yes.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        yes_actionPerformed(e);
                    }
                });
        no.setText("No");
        no.setBounds(new Rectangle(285, 65, 100, 25));
        no.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        no_actionPerformed(e);
                    }
                });
        msg.setBounds(new Rectangle(85, 15, 300, 45));
        msg.setAutoscrolls(true);
        msg.setBackground(icon.getBackground());
        msg.setRows(5);
        msg.setLineWrap(true);
        msg.setWrapStyleWord(true);
        msg.setEditable(false);
        this.getContentPane().add(msg, null);
        this.getContentPane().add(icon, null);
        this.getContentPane().add(no, null);
        this.getContentPane().add(yes, null);
        this.getRootPane().setDefaultButton(yes);
    }

    private void yes_actionPerformed(ActionEvent e) {
        option = 1;
        this.setVisible(false);
    }

    private void no_actionPerformed(ActionEvent e) {
        option = 0;
        this.setVisible(false);
    }
}
