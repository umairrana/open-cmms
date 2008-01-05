package com.matrix.focus.master.gui;

import com.matrix.components.MTextbox;
import com.matrix.focus.master.data.MaintenanceCategoryData;
import com.matrix.focus.master.entity.MaintenanceCategory;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.DataAssistantDialog;
import com.matrix.focus.util.ImageLibrary;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class DLGMaintenanceCategory extends JDialog{
    
    private MaintenanceCategory maintenanceCategory; 
    private JPanel jPanel1 = new JPanel();
    private MTextbox mtxtMaintenanceCategory = new MTextbox();
    private JLabel jLabel2 = new JLabel();
    private JTextArea taRemarks = new JTextArea();
    private JButton btnCancel = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
    private JButton btnSave = new JButton(new ImageIcon(ImageLibrary.BUTTON_SAVE));
    private JButton btnAdd = new JButton(new ImageIcon(ImageLibrary.BUTTON_NEW));
    private JButton btnDelete = new JButton(new ImageIcon(ImageLibrary.BUTTON_DELETE));
    private JButton btnEdit = new JButton(new ImageIcon(ImageLibrary.BUTTON_EDIT));
    private JButton btnSearch = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private MTextbox mtxtEmployee = new MTextbox();
    private JButton btnAddEmployee = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JScrollPane jScrollPane1 = new JScrollPane();
    
    private JFrame frame;
    private Connection connection;
    private MessageBar messageBar;

    public DLGMaintenanceCategory(JFrame parent, Connection con, MessageBar msgBar){
        super(parent,"Maintenance Category",true);
        frame = parent;
        connection = con;
        messageBar = msgBar;
        
        try{
            jbInit();
            setMode("LOAD");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void jbInit() throws Exception{
        this.setLayout(null);
        this.setSize(new Dimension(654, 242));
        this.setResizable(false);
        jPanel1.setBounds(new Rectangle(5, 5, 530, 200));
        jPanel1.setLayout(null);
        jPanel1.setBorder(BorderFactory.createTitledBorder("Category Information"));
        mtxtMaintenanceCategory.setBounds(new Rectangle(20, 15, 350, 20));
        jLabel2.setText("Remarks");
        jLabel2.setBounds(new Rectangle(20, 60, 70, 25));
        jLabel2.setFont(new Font("Tahoma", 0, 11));
        mtxtMaintenanceCategory.setCaption("Maintenance Category");
        mtxtMaintenanceCategory.setLblWidth(150);
        mtxtMaintenanceCategory.setFont(new Font("Tahoma", 0, 11));
        mtxtMaintenanceCategory.setLblFont(new Font("Tahoma", 0, 11));
        btnCancel.setText("Cancel");
        btnCancel.setBounds(new Rectangle(540, 130, 100, 25));
        btnCancel.setSize(new Dimension(100, 25));
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnCancel_actionPerformed(e);
            }
        });
        btnSave.setText("Save");
        btnSave.setBounds(new Rectangle(540, 100, 100, 25));
        btnSave.setSize(new Dimension(100, 25));
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnSave_actionPerformed(e);
            }
        });
        btnAdd.setText("New");
        btnAdd.setBounds(new Rectangle(540, 10, 100, 25));
        btnAdd.setSize(new Dimension(100, 25));
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnAdd_actionPerformed(e);
            }
        });
        btnEdit.setText("Edit");
        btnEdit.setBounds(new Rectangle(540, 70, 100, 25));
        btnEdit.setSize(new Dimension(100, 25));
        btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnEdit_actionPerformed(e);
            }
        });
        btnSearch.setBounds(new Rectangle(375, 15, 30, 20));
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnSearch_actionPerformed(e);
            }
        });
        mtxtEmployee.setBounds(new Rectangle(20, 40, 350, 20));
        mtxtEmployee.setLblWidth(150);
        mtxtEmployee.setCaption("Responsible Person");
        mtxtEmployee.setEditable(false);
        mtxtEmployee.setFont(new Font("Tahoma", 0, 11));
        mtxtEmployee.setLblFont(new Font("Tahoma", 0, 11));
        btnAddEmployee.setBounds(new Rectangle(375, 40, 30, 20));
        btnAddEmployee.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnAddEmployee_actionPerformed(e);
            }
        });
        jScrollPane1.setBounds(new Rectangle(170, 65, 340, 120));
        btnDelete.setText("Delete");
        btnDelete.setBounds(new Rectangle(540, 40, 100, 25));
        btnDelete.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnDelete_actionPerformed(e);
                    }
                });
        this.getContentPane().add(btnDelete, null);
        this.add(btnSave, null);
        this.add(btnCancel, null);
        this.add(jPanel1, null);
        this.getContentPane().add(btnAdd, null);
        this.getContentPane().add(btnEdit, null);
        jScrollPane1.getViewport().add(taRemarks, null);
        jPanel1.add(jScrollPane1, null);
        jPanel1.add(btnAddEmployee, null);
        jPanel1.add(mtxtEmployee, null);
        jPanel1.add(btnSearch, null);
        jPanel1.add(mtxtMaintenanceCategory, null);
        jPanel1.add(jLabel2, null);
    }
    
    private void btnSave_actionPerformed(ActionEvent e) {
        saveUpdateCategory();      
    }
    
    public void saveUpdateCategory(){
        try{
            maintenanceCategory.setCategory(mtxtMaintenanceCategory.getInputText());
            maintenanceCategory.setResponsiblePerson(mtxtEmployee.getInputText());
            maintenanceCategory.setRemarks(taRemarks.getText());
            
            if(maintenanceCategory.isSaved()){
                MaintenanceCategoryData.updateMaintenanceCategory(maintenanceCategory,connection);
                messageBar.setMessage("Maintenance category details updated","OK");
            }
            else{
                MaintenanceCategoryData.saveMaintenanceCategory(maintenanceCategory,connection);
                messageBar.setMessage("New maintenance category saved","OK");
            }
            setMode("SEARCH");
        } 
        catch (Exception ex){
            ex.printStackTrace();
            messageBar.setMessage(ex.getMessage(),"ERROR");
        } 
    }

    private void btnSearch_actionPerformed(ActionEvent e) {
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select Condition - Data Assistant","SELECT Maintenance_Category AS 'Maintenance Category' FROM preventive_maintenance_category",connection);
        d.setFirstColumnWidth(300);
        d.setLocationRelativeTo(frame);
        d.setVisible(true); 
        String rtnVal = d.getValue();
        mtxtMaintenanceCategory.setInputText( (rtnVal.equals("") ? mtxtMaintenanceCategory.getInputText() : rtnVal));
        if(!rtnVal.equals("")){
            setCategory(rtnVal);          
        }
    }
      
    private void setCategory(String category){
        try{
            maintenanceCategory = MaintenanceCategoryData.getMaintenanceCategory(category,connection);  
            
            mtxtMaintenanceCategory.setInputText(maintenanceCategory.getCategory());
            mtxtEmployee.setInputText(maintenanceCategory.getResponsiblePerson());
            taRemarks.setText(maintenanceCategory.getRemarks());
            
            setMode("SEARCH");
        }
        catch(Exception e){
            messageBar.setMessage(e.getMessage(),"ERROR");
        }
    }

    private void btnEdit_actionPerformed(ActionEvent e) {
        setMode("EDIT");
    }

    private void btnAdd_actionPerformed(ActionEvent e) {
        setMode("ADD");
        maintenanceCategory = new MaintenanceCategory();
    }

    private void btnCancel_actionPerformed(ActionEvent e) {
        setMode("LOAD");
    }
    
    private void btnDelete_actionPerformed(ActionEvent e) {
        try{
            if(MessageBar.showConfirmDialog(frame,"Are you sure you want to delete \nthe selected maintenance category from the system?","Mintenance Category")==MessageBar.YES_OPTION){
                MaintenanceCategoryData.deleteMaintenanceCategory(maintenanceCategory.getCategory(),connection);
                setMode("LOAD");
                messageBar.setMessage("Maintenance category deleted","OK");
            }
        } 
        catch (Exception er){
            messageBar.setMessage(er.getMessage(),"ERROR");
        }
    }

    private void btnAddEmployee_actionPerformed(ActionEvent e) {
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select Responsible Person - Data Assistant","select Employee_ID AS 'Employee ID', Name from employee where Deleted='false'",connection);
        d.setLocationRelativeTo(mtxtEmployee);
        d.setVisible(true); 
        String rtnVal = d.getValue();
        if(!rtnVal.isEmpty()){
            mtxtEmployee.setInputText(rtnVal);
        }
    }
    
    private void setMode(String mode){
        if(mode.equals("EDIT")){
            setEditable(true);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
            btnAdd.setEnabled(false);
            btnEdit.setEnabled(false);
            mtxtMaintenanceCategory.setEditable(false);
        }
        else if(mode.equals("ADD")){
            clearAll();
            setEditable(true);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
            btnAdd.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            mtxtMaintenanceCategory.setEditable(true);
        }
        else if(mode.equals("LOAD")){
            clearAll();
            btnAdd.setEnabled(true);
            btnSave.setEnabled(false);
            btnEdit.setEnabled(false);
            btnCancel.setEnabled(false);
            btnDelete.setEnabled(false);
            setEditable(false);
            mtxtMaintenanceCategory.setEditable(false);
        }
        else if(mode.equals("SEARCH")){
            btnEdit.setEnabled(true);
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
            btnAdd.setEnabled(false);
            btnDelete.setEnabled(true);
            setEditable(false);
            mtxtMaintenanceCategory.setEditable(false);
        }
    }
    
    private void setEditable(boolean value){
        btnAddEmployee.setEnabled(value);
        taRemarks.setEditable(value);
    }
    
    private void clearAll(){
        mtxtMaintenanceCategory.setInputText("");
        mtxtEmployee.setInputText("");
        taRemarks.setText("");
        maintenanceCategory = null;
        messageBar.clearMessage();
    }
}
