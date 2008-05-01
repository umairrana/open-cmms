package com.matrix.focus.util;

import com.matrix.components.DatePicker;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class DateCell extends JTextField implements FocusListener, ActionListener{
        private JButton btn;
        private DatePicker dtPicker;
        
        public DateCell(JFrame frame){
            super();
            setEditable(false);
            setLayout(null);
            btn = new JButton(new ImageIcon(ImageLibrary.UTIL_ALERT));
            btn.setSize(17,17);
            btn.setVisible(false);
            btn.addActionListener(this);
            add(btn);
            addFocusListener(this);
            dtPicker = new DatePicker(frame);
        }
     
        public void actionPerformed(ActionEvent e){
            if(btn==e.getSource()){
                dtPicker.setLocationRelativeTo(btn);
                dtPicker.setDate(getText());
                dtPicker.setVisible(true); 
                String rtnValue = dtPicker.DATE;
                setText((rtnValue.equals(""))?getText():rtnValue);
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