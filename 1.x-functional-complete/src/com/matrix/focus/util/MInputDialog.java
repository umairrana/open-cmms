package com.matrix.focus.util;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class MInputDialog extends JDialog {
    private MImage icon = new MImage(ImageLibrary.MESSAGEBAR_INFO);
    private JButton ok = new JButton(new ImageIcon(ImageLibrary.BUTTON_OK));
    private JButton cancel = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
    private JTextField text = new JTextField();
    private int option = 0;
    private JLabel input = new JLabel();

    public MInputDialog(JFrame parent, String title, boolean modal) {
        super(parent, title, modal);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public MInputDialog(JDialog parent, String title, boolean modal) {
        super(parent, title, modal);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setInput(String input){
        this.input.setText(input);
    }
    
    public String getInput(){
        return text.getText();
    }
    
    public int getSelectedOption(){
        return option;
    }

    private void jbInit() throws Exception {
        this.setResizable(false);
        this.setSize(new Dimension(405, 127));
        this.getContentPane().setLayout( null );
        icon.setBounds(new Rectangle(15, 10, 60, 60));
        ok.setText("OK");
        ok.setBounds(new Rectangle(180, 60, 100, 25));
        ok.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ok_actionPerformed(e);
                    }
                });
        cancel.setText("Cancel");
        cancel.setBounds(new Rectangle(285, 60, 100, 25));
        cancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        cancel_actionPerformed(e);
                    }
                });
        text.setBounds(new Rectangle(85, 30, 300, 20));
        input.setText("Input");
        input.setBounds(new Rectangle(85, 10, 300, 20));
        this.getContentPane().add(input, null);
        this.getContentPane().add(text, null);
        this.getContentPane().add(icon, null);
        this.getContentPane().add(ok, null);
        this.getContentPane().add(cancel, null);
        this.getRootPane().setDefaultButton(ok);
    }

    private void ok_actionPerformed(ActionEvent e){
        option = 1;
        this.setVisible(false);
    }

    private void cancel_actionPerformed(ActionEvent e){
        option = 0;
        this.setVisible(false);
    }
}
