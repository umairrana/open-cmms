package com.matrix.focus.setting;

import com.matrix.components.MDatebox;
import com.matrix.components.MTextbox;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.DataAssistantDialog;
import com.matrix.focus.util.ImageLibrary;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class NewEmployeeDailyAvailabilityDialog extends JDialog {
    private JPanel jPanel1 = new JPanel();
    private MTextbox txtEmpID = new MTextbox();
    private MTextbox txtName = new MTextbox();
    private MDatebox dtDate = new MDatebox();
    private JButton btnEmp = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton btnOK = new JButton(new ImageIcon(ImageLibrary.BUTTON_OK));
    private Connection connection;
    private JFrame frame;
    private MessageBar messageBar;
    private String[] info = new String[3];
    private boolean created = false;
    private boolean periodical_availability = false;

    public NewEmployeeDailyAvailabilityDialog(JFrame frame, MessageBar msgBar, boolean periodical_availability, Connection con) {
        super(frame, "New Daily Availability Entry", true);
        this.connection = con;
        this.frame = frame;
        this.messageBar = msgBar;
        this.periodical_availability = periodical_availability;
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.setSize(new Dimension(400, 185));
        this.getContentPane().setLayout( null );
        jPanel1.setBounds(new Rectangle(5, 5, 385, 110));
        jPanel1.setLayout(null);
        jPanel1.setBorder(BorderFactory.createTitledBorder("Entry Details"));
        txtEmpID.setBounds(new Rectangle(15, 25, 245, 20));
        txtEmpID.setCaption("Employee ID");
        txtEmpID.setTxtWidth(100);
        txtEmpID.setEditable(false);
        txtName.setBounds(new Rectangle(15, 50, 360, 20));
        txtName.setTxtWidth(250);
        txtName.setCaption("Name");
        txtName.setEditable(false);
        dtDate.setBounds(new Rectangle(15, 75, 225, 20));
        dtDate.setCaption("Date");
        btnEmp.setBounds(new Rectangle(215, 25, 30, 20));
        btnEmp.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnEmp_actionPerformed(e);
                    }
                });
        btnOK.setText("OK");
        btnOK.setBounds(new Rectangle(290, 120, 95, 25));
        btnOK.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnOK_actionPerformed(e);
                    }
                });
        jPanel1.add(btnEmp, null);
        jPanel1.add(dtDate, null);
        jPanel1.add(txtName, null);
        jPanel1.add(txtEmpID, null);
        this.getContentPane().add(btnOK, null);
        this.getContentPane().add(jPanel1, null);
    }

    private void btnEmp_actionPerformed(ActionEvent e) {
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select Employee - Data Assistant","SELECT Employee_ID as 'Employeey ID', Name  FROM employee WHERE deleted ='false'",connection);
        d.setLocationRelativeTo(frame);
        d.setVisible(true); 
        String rtnVal = d.getValue();
        if(!rtnVal.equals("")){
            txtEmpID.setInputText(rtnVal);
            txtName.setInputText(d.getDescription());
        }
    }
    
    public void setPeriodical(boolean flag){
        this.periodical_availability = flag;
    }
    
    private void btnOK_actionPerformed(ActionEvent e) {
        if(isValidInput()){
            info[0] = txtEmpID.getInputText();
            info[1] = txtName.getInputText();
            info[2] = dtDate.getInputText();
            try{
                if(periodical_availability){
                    new EmployeePeriodAvailabilityEntry(info[0],info[2],connection);
                }
                else{
                    new EmployeeDailyAvailabilityEntry(info[0],info[2],connection);
                }
                created = true;
                this.setVisible(false);
            } 
            catch (Exception ex){
                ex.printStackTrace();
                messageBar.setMessage(ex.getMessage(),"ERROR");
            } 
        }
    }
    
    public boolean oneCreated(){
        return created;
    }
    
    public String[] getCreatedEntry(){
        return info;
    }
    
    private boolean isValidInput(){
        if(txtEmpID.getInputText().equals("")){
            messageBar.setMessage("Employee ID field is empty.","ERROR");
            return false;
        }
        else if(dtDate.getInputText().equals("")){
            messageBar.setMessage("Date field is empty.","ERROR");
            return false;
        }
        else{
            return true;
        }
    }
}
