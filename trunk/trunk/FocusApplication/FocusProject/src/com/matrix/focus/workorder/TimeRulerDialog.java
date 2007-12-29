package com.matrix.focus.workorder;

import com.matrix.components.DatePicker;
import com.matrix.focus.mdi.MDI;
import com.matrix.focus.util.ImageLibrary;
import com.matrix.focus.util.TimeRuler;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class TimeRulerDialog extends JDialog {
    private TimeRuler timeRuler;
    private Connection connection;
    private String emp_id;
    private String name;
    private JLabel Date = new JLabel();
    private JTextField txtDate = new JTextField();
    private JButton btnPick = new JButton(new ImageIcon(ImageLibrary.BUTTON_DT_PICKER));
    private static DatePicker dtp;

    public TimeRulerDialog(MDI mdi, JDialog frame, Connection conn) {
        this(mdi, frame, "Labour Availability", true);
        this.connection = conn;
    }
    public TimeRulerDialog(MDI mdi, JDialog parent, String title, boolean modal) {
        super(parent, title, modal);
        try {
            dtp = new DatePicker(null);
            dtp.FORMAT="YYYY-MM-DD";
            timeRuler = new TimeRuler(mdi);
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setAssignedInfo(String emp, String name, String date){
        emp_id = emp;
        this.name = name;
        setTitle("Labour Availability");
        txtDate.setText(date.substring(0,10));
        timeRuler.setAssignedInfo(emp,name,date,connection,this);
    }
    
    private void jbInit() throws Exception {
        this.setSize(new Dimension(837, 171));
        this.setResizable(false);
        this.getContentPane().setLayout( null );
        timeRuler.setBounds(new Rectangle(10, 45, 810, 90));
        Date.setText("Date");
        Date.setBounds(new Rectangle(10, 10, 45, 20));
        txtDate.setBounds(new Rectangle(45, 10, 70, 20));
        txtDate.setEditable(false);
        btnPick.setBounds(new Rectangle(120, 10, 30, 20));
        btnPick.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnPick_actionPerformed(e);
                    }
                });
        timeRuler.setLocation(10,40);
        this.getContentPane().add(btnPick, null);
        this.getContentPane().add(txtDate, null);
        this.getContentPane().add(Date, null);
        this.getContentPane().add(timeRuler);
    }
    
    private void btnPick_actionPerformed(ActionEvent e) {
        dtp.setLocationRelativeTo(btnPick);
        dtp.setDate(txtDate.getText());
        dtp.setVisible(true);
        txtDate.setText((dtp.DATE.equals("")?txtDate.getText():dtp.DATE));
        setAssignedInfo(emp_id,name,txtDate.getText());
    }
}
