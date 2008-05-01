package com.matrix.focus.util;

import com.matrix.components.MTextbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class TimePeriodDialog extends JDialog{
    private JPanel jPanel1 = new JPanel();
    private JButton btnCalc = new JButton(new ImageIcon(ImageLibrary.BUTTON_OK));
    private final int NO_OF_DATES_IN_THE_YEAR = 365;  
    private final int NO_OF_DATES_IN_THE_MONTH = 31;
    private String dates = "";
    private int int_dates;
    private boolean ERROR;
    private MTextbox txtY = new MTextbox();
    private MTextbox txtM = new MTextbox();
    private MTextbox txtD = new MTextbox();

    public TimePeriodDialog(JFrame frame){
        this(frame, "Period", true);
    }
    
    public void setVisible(boolean value,String time){
        //1-1-12
        time = (time.equals("")?"0-0-0":time);
        String[] times = time.split("-");
        txtY.setInputText(times[0]);
        txtM.setInputText(times[1]);
        txtD.setInputText(times[2]);
        setVisible(value);;
    }
    
    public TimePeriodDialog(Frame parent, String title, boolean modal){
        super(parent, title, modal);
        try{
            jbInit();
            this.getRootPane().setDefaultButton(btnCalc);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception{
        this.getContentPane().setLayout( null );
        this.setResizable(false);
        this.setSize(new Dimension(353, 128));
        jPanel1.setBounds(new Rectangle(5, 10, 335, 50));
        jPanel1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        jPanel1.setLayout(null);
        btnCalc.setBounds(new Rectangle(255, 65, 85, 25));
        btnCalc.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
              btnCalc_actionPerformed(e);
            }
        });
        txtY.setBounds(new Rectangle(15, 15, 125, 20));
        txtY.setCaption("Years");
        txtY.setLblWidth(40);
        txtY.setTxtWidth(50);
        txtM.setBounds(new Rectangle(115, 15, 100, 20));
        txtM.setLblWidth(50);
        txtM.setTxtWidth(50);
        txtM.setCaption("Months");
        txtD.setBounds(new Rectangle(225, 15, 90, 20));
        txtD.setCaption("Days");
        txtD.setTxtWidth(50);
        txtD.setLblWidth(40);
        jPanel1.add(txtM, null);
        jPanel1.add(txtD, null);
        jPanel1.add(txtY, null);
        this.getContentPane().add(jPanel1, null);
        this.getContentPane().add(btnCalc, null);
        btnCalc.setText("Ok");
    }

    private void btnCalc_actionPerformed(ActionEvent e){
        ERROR = false;
        int dY = getYears();
        int dM = getMonths();
        int dD = getDays();
        
        if(!ERROR){
            dates = dY + "-" + dM + "-" + dD;
            int_dates = (dY * NO_OF_DATES_IN_THE_YEAR) + (dM * NO_OF_DATES_IN_THE_MONTH) + dD;
            setVisible(false);
        }
    }
                    
    private int getYears(){
        try{
            int y = Integer.parseInt(txtY.getInputText());
            if(y<0){
                txtY.setFocus(1);
                ERROR = true;
                return 0;
            }
            else{
                return y;
            }
        }
        catch(Exception ne){
            return 0;
        }
    }
    
    private int getMonths(){
        try{
            int m = Integer.parseInt(txtM.getInputText());
            if(m<0){
                txtM.setFocus(1);
                ERROR = true;
                return 0;
            }
            else if(m>11){
                txtM.setFocus(1);
                ERROR = true;
                return 11;
            }
            else{
                return m;
            }
        }
        catch(Exception ne){
            return 0;
        }
    }

    private int getDays(){
        try{
            int d = Integer.parseInt(txtD.getInputText());
            if(d<0){
                txtD.setFocus(1);
                ERROR = true;
                return 0;
            }
            else if(d>30){
                txtD.setFocus(1);
                ERROR = true;
                return 30;
            }
            else{
                return d;
            }
        }
        catch(Exception ne){
            return 0;
        }
    }
    
    public String getStringValue(){
        return dates;
    }

    public int getIntValue(){
        return int_dates;
    }
}