package com.matrix.focus.planner;

import com.matrix.focus.mdi.messageBar.MessageBar;

import java.awt.Dimension;

import java.awt.Rectangle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class GUIMeterAverage extends JDialog {

    private JRadioButton radioButtonCurrentAverage = new JRadioButton();
    private JRadioButton radioButtonCalculatedAverage = new JRadioButton();
    private JRadioButton radioButtonNewAverage = new JRadioButton();
    private JTextField textFieldCurrentAverage = new JTextField();
    private JTextField textFieldCalculatedAverage = new JTextField();
    private JTextField textFieldNewAverage = new JTextField();
    private ButtonGroup bGroup = new ButtonGroup();
    private JButton buttonSave = new JButton();
    private JPanel jPanel1 = new JPanel();
    private PlanningAsset pAsset;
    private int newMeter=0;
    private String newDate="";
    private Connection connection;
    private MessageBar messageBar;
    public int OPTION = 0;
    
    public GUIMeterAverage(JFrame frame, PlanningAsset pAsset,int newMeter,String newDate,Connection connection,MessageBar messageBar) {
       super(frame,"Meter Average",true);
       this.pAsset = pAsset;
       this.newMeter = newMeter;
       this.newDate = newDate;
       this.connection = connection;
       this.messageBar = messageBar;
       
        try {
        
            jbInit();
            
            setFieldsValues();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                OPTION = -1;
            }
            
        });
    }
    
    private void setFieldsValues() throws SQLException {
    
        textFieldCurrentAverage.setText(""+pAsset.AVERAGE_METER_PER_DAY);
        textFieldCalculatedAverage.setText(""+pAsset.getCalculatedAverage(newMeter,newDate));
        
    }
    
    private void jbInit() throws Exception {
    
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        
        this.setSize(new Dimension(348, 223));
        this.getContentPane().setLayout(null);
        radioButtonCurrentAverage.setText("Use Current Average");
        radioButtonCurrentAverage.setBounds(new Rectangle(25, 35, 120, 20));
        radioButtonCurrentAverage.setSelected(true);
        radioButtonCalculatedAverage.setText("Use Calculated Average");
        radioButtonCalculatedAverage.setBounds(new Rectangle(25, 60, 150, 20));
        radioButtonCalculatedAverage.setSelected(true);
        radioButtonNewAverage.setText("Use  New Average");
        radioButtonNewAverage.setBounds(new Rectangle(25, 85, 145, 20));
        textFieldCurrentAverage.setBounds(new Rectangle(180, 35, 120, 20));
        textFieldCurrentAverage.setEditable(false);
        textFieldCalculatedAverage.setBounds(new Rectangle(180, 60, 120, 20));
        textFieldCalculatedAverage.setEditable(false);
        textFieldNewAverage.setBounds(new Rectangle(180, 85, 120, 20));
        buttonSave.setText("Save Average Meter and Reschedule");
        buttonSave.setBounds(new Rectangle(10, 150, 320, 30));
        buttonSave.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        buttonSave_actionPerformed(e);
                    }
                });
        jPanel1.setBounds(new Rectangle(10, 10, 320, 130));
        jPanel1.setLayout(null);
        jPanel1.setBorder(BorderFactory.createTitledBorder("Average Meter"));
        bGroup.add(radioButtonCurrentAverage);
        bGroup.add(radioButtonCalculatedAverage);
        bGroup.add(radioButtonNewAverage);
        jPanel1.add(radioButtonCurrentAverage, null);
        jPanel1.add(textFieldNewAverage, null);
        jPanel1.add(textFieldCalculatedAverage, null);
        jPanel1.add(textFieldCurrentAverage, null);
        jPanel1.add(radioButtonNewAverage, null);
        jPanel1.add(radioButtonCalculatedAverage, null);
        this.getContentPane().add(jPanel1, null);
        this.getContentPane().add(buttonSave, null);
        
    }

    private void buttonSave_actionPerformed(ActionEvent e) {
        
        int average = 0;
        
        if(radioButtonCurrentAverage.isSelected()){
            average = Integer.parseInt(textFieldCurrentAverage.getText());
        }
        else if(radioButtonCalculatedAverage.isSelected()){
            average = Integer.parseInt(textFieldCalculatedAverage.getText());
        }
        else{
            
            try {
                average = Integer.parseInt(textFieldNewAverage.getText());
                if(average<0)
                    throw new Exception();
            }
            catch (Exception ex) {
                messageBar.setMessage("Invalid average","ERROR");
                return;
            }
        }

        try {
            pAsset.setAverageMeterPerDay(average);
            setVisible(false);
        } catch (Exception f) {
             messageBar.setMessage(f.getMessage(),"ERROR");
                f.printStackTrace();
        }
    }
}
