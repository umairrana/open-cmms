package com.matrix.focus.util;

import java.awt.Font;

import java.awt.GridLayout;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JTextField;

public class TimeCell extends JTextField{
    private JComboBox cmbHour;
    private JComboBox cmbMin;
    private String HOUR = "00";
    private String MIN = "00";
    public TimeCell(){
        setEditable(false);
        cmbHour = new JComboBox();
        cmbHour.setFont(new Font("Tahoma",0,11));
        fillFromNumbers(cmbHour,24);
        cmbMin = new JComboBox();
        cmbMin.setFont(new Font("Tahoma",0,11));
        fillFromNumbers(cmbMin,60);
        setLayout(new GridLayout(1,2));
        add(cmbHour);
        add(cmbMin);
        cmbHour.addItemListener(new ItemListener(){
                    public void itemStateChanged(ItemEvent e) {
                        HOUR = cmbHour.getSelectedItem().toString();
                        setText(HOUR + ":" + MIN);
                    }
                });
        cmbMin.addItemListener(new ItemListener(){
                    public void itemStateChanged(ItemEvent e) {
                        MIN = cmbMin.getSelectedItem().toString();
                        setText(HOUR + ":" + MIN);
                    }
                });
    }
    private void fillFromNumbers(JComboBox cmb, int limit){
        for(int i=0;i<limit;i++){
            cmb.addItem(Utilities.getDoubleFigures(i));
        }
    }
}
