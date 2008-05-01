package com.matrix.focus.util;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class DateTimeCell extends JTextField implements FocusListener, ActionListener{
        private JButton btn;
        private DateTimePicker dlgDateTimePicker;
        
        public DateTimeCell(JFrame frame){
            super();
            setEditable(false);
            setLayout(null);
            btn = new JButton(new ImageIcon(ImageLibrary.UTIL_ALERT));
            btn.setSize(17,17);
            btn.setVisible(false);
            btn.addActionListener(this);
            add(btn);
            addFocusListener(this);
            dlgDateTimePicker = new DateTimePicker(frame);
        }
        
        public DateTimeCell(JFrame frame, boolean timeOnly){
            super();
            setEditable(false);
            setLayout(null);
            btn = new JButton(new ImageIcon(ImageLibrary.UTIL_ALERT));
            btn.setSize(17,17);
            btn.setVisible(false);
            btn.addActionListener(this);
            add(btn);
            addFocusListener(this);
            dlgDateTimePicker = new DateTimePicker(frame);
            dlgDateTimePicker.setDateSelectable(!timeOnly);
        }
     
        public void actionPerformed(ActionEvent e){
            if(btn==e.getSource()){
                dlgDateTimePicker.setLocationRelativeTo(btn);
                String value = getText();
                dlgDateTimePicker.setDateTime(value.substring(0,10),value.substring(11,16));
                dlgDateTimePicker.setVisible(true); 
                String rtnValue = dlgDateTimePicker.getDateTime();
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