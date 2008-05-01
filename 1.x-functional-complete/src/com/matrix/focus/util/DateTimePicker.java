package com.matrix.focus.util;

import com.matrix.components.MDatebox;
import com.matrix.components.MTimeControl;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DateTimePicker extends JDialog {
    private JPanel jPanel1 = new JPanel();
    private JButton btnOK = new JButton(new ImageIcon(ImageLibrary.BUTTON_OK));
    private MDatebox mdtDate;
    private MTimeControl mtmTime = new MTimeControl();
    private String dateTime;

    public DateTimePicker(JFrame frame) {
        this(frame, "Date Time Picker", true);
    }
    
    public DateTimePicker(JDialog frame) {
        this(frame, "Date Time Picker", true);
    }

    public DateTimePicker(JFrame parent, String title, boolean modal) {
        super(parent, title, modal);
        try {
            mdtDate = new MDatebox();
            this.addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent we){
                    dateTime = "";
                }
            });
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public DateTimePicker(JDialog parent, String title, boolean modal) {
        super(parent, title, modal);
        try {
            mdtDate = new MDatebox();
            this.addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent we){
                    dateTime = "";
                }
            });
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public void setDateTime(String date, String time){
        if(!date.equals("0000-00-00")){
            mdtDate.setDate(date);
            mdtDate.setInputText(date);
            String times[] = time.split(":"); 
            mtmTime.setTime(times[0],times[1]);
        }
    }
    
    public void setDateSelectable(boolean flag){
        mdtDate.getButton().setEnabled(flag);
    }
    
    public String getDateTime(){
        return dateTime;
    }
    
    private void jbInit() throws Exception {
        this.setSize(new Dimension(400, 137));
        this.setResizable(false);
        this.getContentPane().setLayout( null );
        jPanel1.setBounds(new Rectangle(10, 10, 375, 50));
        jPanel1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        jPanel1.setLayout(null);
        btnOK.setText("OK");
        btnOK.setBounds(new Rectangle(290, 70, 95, 25));
        this.getRootPane().setDefaultButton(btnOK);
        btnOK.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnOK_actionPerformed(e);
                    }
                });
        mdtDate.setBounds(new Rectangle(20, 15, 230, 20));
        mdtDate.setCaption("Date");
        mdtDate.setLblWidth(40);
        mtmTime.setBounds(new Rectangle(205, 15, 155, 20));
        mtmTime.setCaption("Time");
        mtmTime.setLblWidth(30);
        jPanel1.add(mtmTime, null);
        jPanel1.add(mdtDate, null);
        this.getContentPane().add(btnOK, null);
        this.getContentPane().add(jPanel1, null);
    }

    private void btnOK_actionPerformed(ActionEvent e) {
        String tm = mtmTime.getTime();
        tm = (!tm.equals("")?tm:"00:00:00");
        String s=mdtDate.getInputText().equals("")? "0000-00-00":mdtDate.getInputText();
        dateTime =  s+ " " + tm;
        setVisible(false);
    }
    
    public MDatebox getDateBox(){
        return mdtDate;
    }
    
}
