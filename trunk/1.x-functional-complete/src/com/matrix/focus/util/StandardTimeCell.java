package com.matrix.focus.util;

import com.matrix.components.MTextbox;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

public class StandardTimeCell extends JTextField implements FocusListener, ActionListener{
        private StandardTimeDialog stdlg;
        private JButton btn;
 
        public StandardTimeCell(JFrame frame){
            super();
            setEditable(false);
            setLayout(null);
            btn = new JButton(new ImageIcon(ImageLibrary.UTIL_ALERT));
            btn.setSize(17,20);
            btn.setVisible(false);
            btn.addActionListener(this);
            add(btn);
            stdlg = new StandardTimeDialog(frame);
            addFocusListener(this);
        }
     
        public void actionPerformed(ActionEvent e){
            if(btn==e.getSource()){
                /**Show Dialog*/
                
                stdlg.setLocationRelativeTo(btn);
                stdlg.setVisible(true,getText());
                /**Get value from the dialog*/
                String rtnVal = stdlg.getValue();
                String curVal = getText();
                /**If the dialog returns empty, keep the previous value in the text field*/
                setText( (rtnVal.equals("") ? curVal : rtnVal));
            }
        }
     
        public void focusLost(FocusEvent fe){
            btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
  
        public void focusGained(FocusEvent fe){
            int w = getWidth();
            btn.setLocation(w-17,0);
            btn.setVisible(true);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }
