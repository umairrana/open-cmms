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

public class MErrorDialog extends JDialog {
    private MImage icon = new MImage(ImageLibrary.MESSAGEBAR_ERROR);
    private JButton ok = new JButton(new ImageIcon(ImageLibrary.BUTTON_OK));
    private JTextArea msg = new JTextArea();
    
    public MErrorDialog(JFrame parent, String title, boolean modal) {
        super(parent, title, modal);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public MErrorDialog(JDialog parent, String title, boolean modal) {
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

    private void jbInit() throws Exception {
        this.setResizable(false);
        this.setSize(new Dimension(405, 129));
        this.getContentPane().setLayout( null );
        icon.setBounds(new Rectangle(15, 10, 60, 60));
        ok.setText("OK");
        ok.setBounds(new Rectangle(285, 60, 100, 25));
        ok.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        setVisible(false);
                    }
                });
        msg.setBounds(new Rectangle(85, 15, 300, 40));
        msg.setAutoscrolls(true);
        msg.setBackground(icon.getBackground());
        msg.setRows(5);
        msg.setLineWrap(true);
        msg.setWrapStyleWord(true);
        msg.setEditable(false);
        this.getContentPane().add(msg, null);
        this.getContentPane().add(icon, null);
        this.getContentPane().add(ok, null);
        this.getRootPane().setDefaultButton(ok);
    }
}
