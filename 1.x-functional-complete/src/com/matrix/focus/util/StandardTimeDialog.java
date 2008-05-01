package com.matrix.focus.util;

import com.matrix.components.MTextbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

public class StandardTimeDialog extends JDialog{
    private JPanel jPanel1 = new JPanel();
    private JButton btnCalc = new JButton(new ImageIcon(ImageLibrary.BUTTON_OK));
    private String time = "";
    private boolean ERROR;
    private MTextbox txtDays = new MTextbox();
    private MTextbox txtHours = new MTextbox();
    private MTextbox txtMins = new MTextbox();
     
    public StandardTimeDialog(JFrame frame){
        this(frame, "Time", true);
    }

    public StandardTimeDialog(JFrame parent, String title, boolean modal){
        super(parent, title, modal);
        try{
            jbInit();
            this.getRootPane().setDefaultButton(btnCalc);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
            
    public void setVisible(boolean value,String time){
        //1-1-12
        time = (time.equals("")?"0-0-0":time);
        String[] times = time.split("-");
        txtDays.setInputText(times[0]);
        txtHours.setInputText(times[1]);
        txtMins.setInputText(times[2]);
        setVisible(value);;
    }
            
    private void jbInit() throws Exception{
        this.getContentPane().setLayout( null );
        this.setSize(new Dimension(368, 129));
        this.setResizable(false);
        jPanel1.setBounds(new Rectangle(5, 10, 350, 50));
        jPanel1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        jPanel1.setLayout(null);
        btnCalc.setBounds(new Rectangle(270, 65, 85, 25));
        btnCalc.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
        btnCalc_actionPerformed(e);
        }
        });
        txtDays.setBounds(new Rectangle(15, 15, 125, 20));
        txtDays.setCaption("Days");
        txtDays.setLblWidth(50);
        txtDays.setTxtWidth(50);
        txtHours.setBounds(new Rectangle(125, 15, 100, 20));
        txtHours.setLblWidth(50);
        txtHours.setTxtWidth(50);
        txtHours.setCaption("Hours");
        txtMins.setBounds(new Rectangle(235, 15, 100, 20));
        txtMins.setCaption("Minutes");
        txtMins.setTxtWidth(50);
        txtMins.setLblWidth(50);
        btnCalc.setText("Ok");
        jPanel1.add(txtDays);
        jPanel1.add(txtHours);
        jPanel1.add(txtMins);
        this.getContentPane().add(jPanel1);
        this.getContentPane().add(btnCalc);
    }

    private void btnCalc_actionPerformed(ActionEvent e){
        ERROR = false;
        int d = getDays();
        int h = getHours();
        int m = getMins();
        
        if(!ERROR){
            time = d + "-" + h + "-" + m;
            setVisible(false);
            txtDays.setInputText("");
            txtHours.setInputText("");
            txtMins.setInputText("");
        }
    }
             
    private int getDays(){
        try{
            int d = Integer.parseInt(txtDays.getInputText());
            if(d<0){
                txtDays.setFocus(1);
                ERROR = true;
                return 0;
            }
            else{
                return d;
            }
        }
        catch(Exception ne){
            return 0;
        }
    }

    private int getHours(){
        try{
            int h = Integer.parseInt(txtHours.getInputText());
            if(h<0){
                txtHours.setFocus(1);
                ERROR = true;
                return 0;
            }
            else if(h>23){
                txtHours.setFocus(1);
                ERROR = true;
                return 23;
            }
            else{
                return h;
            }
        }
        catch(Exception ne){
            return 0;
        }
    }
            
    private int getMins(){
        try{
            int m = Integer.parseInt(txtMins.getInputText());
            if(m<0){
                txtMins.setFocus(1);
                ERROR = true;
                return 0;
            }
            else if(m>59){
                txtMins.setFocus(1);
                ERROR = true;
                return 59;
            }
            else{
                return m;
            }
        }
        catch(Exception ne){
            return 0;
        }
    }
 
    public String getValue(){
        return time;
    }
     
}
