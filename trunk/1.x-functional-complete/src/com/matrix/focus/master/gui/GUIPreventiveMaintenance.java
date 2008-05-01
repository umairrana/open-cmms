package com.matrix.focus.master.gui;

import com.matrix.components.MDataCombo;
import com.matrix.components.TitleBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import com.matrix.components.MTextbox;
import com.matrix.focus.util.MPanel;
import com.matrix.focus.master.data.PreventiveMaintenanceData;
import com.matrix.focus.master.entity.PreventiveMaintenance;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.DataAssistantDialog;
import com.matrix.focus.util.ImageLibrary;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.Font;
import java.sql.Connection;
import javax.swing.JButton;
import javax.swing.JFrame;

public class GUIPreventiveMaintenance extends MPanel{
    
    private JPanel jPanel1 = new JPanel();
    private JLabel jLabel2 = new JLabel();
    private JTextArea taDescription = new JTextArea();
    private MTextbox mtxtPreventiveMaintenanceID = new MTextbox();
    private JButton btnCancel = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
    private JButton btnSave = new JButton(new ImageIcon(ImageLibrary.BUTTON_SAVE));
    private JButton btnDelete = new JButton(new ImageIcon(ImageLibrary.BUTTON_DELETE));
    private JButton btnAdd = new JButton(new ImageIcon(ImageLibrary.BUTTON_NEW));
    private JButton btnEdit = new JButton(new ImageIcon(ImageLibrary.BUTTON_EDIT));
    private JButton btnSearch = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private MDataCombo mdcCategory = new MDataCombo();
    private JButton btnAddCategory = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));
    private TitleBar titlebar = new TitleBar();
    private MDataCombo mcmbType = new MDataCombo();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JTextArea txtProcedure = new JTextArea();
    private JLabel jLabel1 = new JLabel();
    private JButton btnType = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));
    private JScrollPane jScrollPane2 = new JScrollPane();
    
    private Connection connection;
    private JFrame frame;
    private MessageBar messageBar;
    private PreventiveMaintenance preventiveMaintenance; 

    public GUIPreventiveMaintenance(DBConnectionPool pool, JFrame frame, MessageBar msgBar){
        connection = pool.getConnection();
        this.frame = frame;
        this.messageBar = msgBar;
        try {
        
            titlebar.setTitle("Preventive Maintenance");
            titlebar.setDescription("The facility to manage preventive maintenances master data.");
            titlebar.setImage(ImageLibrary.TITLE_PREVENTIVE_MAINTENANCE);
            
            jbInit();
            
            mdcCategory.populate(connection,"SELECT DISTINCT Maintenance_Category from preventive_maintenance_category");
            mcmbType.populate(connection,"SELECT DISTINCT pm_type from preventive_maintenance");
            
            setMode("LOAD");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception{
        this.setLayout(null);
        this.setSize(new Dimension(953, 367));
        jPanel1.setBounds(new Rectangle(10, 85, 820, 265));
        jPanel1.setLayout(null);
        jPanel1.setBorder(BorderFactory.createTitledBorder("Preventive Maintenance"));
        jLabel2.setText("Description");
        jLabel2.setBounds(new Rectangle(20, 40, 65, 25));
        jLabel2.setFont(new Font("Tahoma", 0, 11));
        mtxtPreventiveMaintenanceID.setBounds(new Rectangle(20, 20, 250, 20));
        mtxtPreventiveMaintenanceID.setTxtWidth(100);
        mtxtPreventiveMaintenanceID.setCaption("Preventive Maintenance ID");
        mtxtPreventiveMaintenanceID.setLblWidth(150);
        mtxtPreventiveMaintenanceID.setEditable(false);
        mtxtPreventiveMaintenanceID.setLayout(null);
        mtxtPreventiveMaintenanceID.setLblFont(new Font("Tahoma", 0, 11));
        mtxtPreventiveMaintenanceID.setFont(new Font("Tahoma", 0, 11));
        btnCancel.setText("Cancel");
        btnCancel.setBounds(new Rectangle(835, 215, 100, 25));
        btnCancel.setSize(new Dimension(100, 25));
        btnCancel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnCancel_actionPerformed(e);
            }
        });
        btnSave.setText("Save");
        btnSave.setBounds(new Rectangle(835, 185, 100, 25));
        btnSave.setSize(new Dimension(100, 25));
        btnSave.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnSave_actionPerformed(e);
            }
        });
        btnDelete.setText("Delete");
        btnDelete.setBounds(new Rectangle(835, 155, 100, 25));
        btnDelete.setSize(new Dimension(100, 25));
        btnDelete.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnDelete_actionPerformed(e);
            }
        });
        btnAdd.setText("New");
        btnAdd.setBounds(new Rectangle(835, 95, 100, 25));
        btnAdd.setSize(new Dimension(100, 25));
        btnAdd.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnAdd_actionPerformed(e);
            }
        });
        btnEdit.setText("Edit");
        btnEdit.setBounds(new Rectangle(835, 125, 100, 25));
        btnEdit.setSize(new Dimension(100, 25));
        btnEdit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnEdit_actionPerformed(e);
            }
        });
        btnSearch.setBounds(new Rectangle(275, 20, 30, 20));
    
        btnSearch.setFont(new Font("Tahoma", 0, 11));
        btnSearch.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnSearch_actionPerformed(e);
            }
        });
        mdcCategory.setBounds(new Rectangle(20, 110, 350, 20));
        mdcCategory.setLblWidth(150);
        mdcCategory.setCaption("Maintenance Category");
        mdcCategory.setCmbWidth(200);
        mdcCategory.setLayout(null);
        mdcCategory.setLblFont(new Font("Tahoma", 0, 11));
        mdcCategory.setFont(new Font("Tahoma", 0, 11));
        mdcCategory.setCmbFont(new Font("Tahoma", 0, 11));
        btnAddCategory.setBounds(new Rectangle(375, 110, 30, 20));
        btnAddCategory.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnAddCategory_actionPerformed(e);
            }
        });
        titlebar.setBounds(new Rectangle(10, 10, 925, 70));
        mcmbType.setBounds(new Rectangle(20, 135, 350, 20));
        mcmbType.setCaption("Maintenance Type");
        mcmbType.setLblWidth(150);
        mcmbType.setCmbWidth(200);
        jScrollPane1.setBounds(new Rectangle(170, 160, 620, 90));
        jLabel1.setText("Procedure");
        jLabel1.setBounds(new Rectangle(20, 155, 55, 20));
        btnType.setBounds(new Rectangle(375, 135, 30, 20));
        btnType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnType_actionPerformed(e);
            }
        });
        jScrollPane2.setBounds(new Rectangle(170, 45, 620, 60));
        this.add(titlebar, null);
        this.add(btnDelete, null);
        this.add(btnSave, null);
        this.add(btnCancel, null);
        this.add(jPanel1, null);
        this.add(btnEdit, null);
        this.add(btnAdd, null);
        jScrollPane2.getViewport().add(taDescription, null);
        jPanel1.add(jScrollPane2, null);
        jPanel1.add(btnType, null);
        jPanel1.add(jLabel1, null);
        jScrollPane1.getViewport().add(txtProcedure, null);
        jPanel1.add(jScrollPane1, null);
        jPanel1.add(mcmbType, null);
        jPanel1.add(btnAddCategory, null);
        jPanel1.add(mdcCategory, null);
        jPanel1.add(btnSearch, null);
        jPanel1.add(mtxtPreventiveMaintenanceID, null);
        jPanel1.add(jLabel2, null);
    }
        
    public void saveUpdatePreventiveMaintenance() {
        try{
            preventiveMaintenance.setID(mtxtPreventiveMaintenanceID.getInputText());
            preventiveMaintenance.setDescription(taDescription.getText());
            preventiveMaintenance.setCategory(mdcCategory.getSelectedItem().toString());
            preventiveMaintenance.setType(mcmbType.getSelectedItem().toString());
            preventiveMaintenance.setProcedure(txtProcedure.getText());
            
            if(preventiveMaintenance.isSaved()){
                PreventiveMaintenanceData.updatePreventiveMaintenance(preventiveMaintenance,connection);
                messageBar.setMessage("Preventive maintenance details updated","OK");
            }
            else{
                PreventiveMaintenanceData.savePreventiveMaintenance(preventiveMaintenance,connection);
                messageBar.setMessage("New company saved","OK");
            }
            setMode("SEARCH");
        } 
        catch (Exception ex){
            ex.printStackTrace();
            messageBar.setMessage(ex.getMessage(),"ERROR");
        } 
    }

    private void btnSave_actionPerformed(ActionEvent e) {
        saveUpdatePreventiveMaintenance();
    }

    private void btnSearch_actionPerformed(ActionEvent e) {
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select Preventive Maintenance - Data Assistant","SELECT ID, Description FROM preventive_maintenance where Deleted='false'",connection);
        d.grow(600,0,580);
        d.setSecondColumnWidth(500);
        d.setLocationRelativeTo(frame);
        d.setVisible(true); 
        String rtnVal = d.getValue();
        mtxtPreventiveMaintenanceID.setInputText( (rtnVal.equals("") ? mtxtPreventiveMaintenanceID.getInputText() : rtnVal));
        if(!rtnVal.equals("")){
            setPreventiveMaintenance(rtnVal);
        }
    }
    
    private void setPreventiveMaintenance(String id){
        try {
            preventiveMaintenance = PreventiveMaintenanceData.getPreventiveMaintenance(id,connection);
            
            mtxtPreventiveMaintenanceID.setInputText(preventiveMaintenance.getID());
            taDescription.setText(preventiveMaintenance.getDescription());
            mdcCategory.setSelectedItem(preventiveMaintenance.getCategory());
            mcmbType.setSelectedItem(preventiveMaintenance.getType());
            txtProcedure.setText(preventiveMaintenance.getProcedure());
            setMode("SEARCH");
        }
        catch(Exception e){
            messageBar.setMessage(e.getMessage(),"ERROR");
        }
    }

    private void btnEdit_actionPerformed(ActionEvent e) {
        setMode("EDIT");
    }

    private void btnAdd_actionPerformed(ActionEvent e){
        setMode("ADD");
        preventiveMaintenance = new PreventiveMaintenance();
        mtxtPreventiveMaintenanceID.setInputText(PreventiveMaintenanceData.getNextPreventiveMaintenanceID(connection));
    }

    private void btnCancel_actionPerformed(ActionEvent e) {
        setMode("LOAD");    
    }

    private void btnDelete_actionPerformed(ActionEvent e) {
        try{
            if(MessageBar.showConfirmDialog(frame,"Are you sure you want to delete \nthe selected Preventive Maintenance from the system?","Preventive Maintenance")==MessageBar.YES_OPTION){
                PreventiveMaintenanceData.deletePreventiveMaintenance(preventiveMaintenance.getID(),connection);
                setMode("LOAD");
                messageBar.setMessage("Preventive Maintenance deleted","OK");
            }
        } 
        catch (Exception er){
            messageBar.setMessage(er.getMessage(),"ERROR");
        }
    }

    private void btnAddCategory_actionPerformed(ActionEvent e) {
        DLGMaintenanceCategory d = new DLGMaintenanceCategory(frame,connection,messageBar);
        d.setLocationRelativeTo(frame);
        d.setVisible(true);
        Object old = mdcCategory.getSelectedItem();
        mdcCategory.removeAllItems();
        mdcCategory.populate(connection,"select Maintenance_Category from preventive_maintenance_category");
        mdcCategory.setSelectedItem(old);
    }

    private void btnType_actionPerformed(ActionEvent e) {
        try{
            String s = MessageBar.showInputDialog(frame,"Preventive Maintenance Type","Add New Type");
            if (!s.equals("")){
                mcmbType.addItem(s);
                mcmbType.setSelectedItem(s);  
            }
        }
        catch(Exception er){
        }
    }
    
    private void setMode(String mode){
        if(mode.equals("EDIT")){
            setEditable(true);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
            btnAdd.setEnabled(false);
            btnEdit.setEnabled(false);
        }
        else if(mode.equals("ADD")){
            clearAll();
            setEditable(true);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
            btnAdd.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
        }
        else if(mode.equals("LOAD")){
            clearAll();
            btnAdd.setEnabled(true);
            btnSave.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            btnCancel.setEnabled(false);
            setEditable(false);
        }
        else if(mode.equals("SEARCH")){
            btnEdit.setEnabled(true);
            btnDelete.setEnabled(true);
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
            btnAdd.setEnabled(false);
            setEditable(false);
        }
    }

    private void setEditable(boolean value){
        taDescription.setEditable(value);
        txtProcedure.setEditable(value);
        mdcCategory.setCmbEnable(value);
        mcmbType.setCmbEnable(value);
        btnAddCategory.setEnabled(value);
        btnType.setEnabled(value);
    }
    
    private void clearAll(){
        mtxtPreventiveMaintenanceID.setInputText("");
        taDescription.setText("");
        txtProcedure.setText("");
        mdcCategory.getComboBox().setSelectedIndex(0);
        mcmbType.getComboBox().setSelectedIndex(0);
        messageBar.clearMessage();
        preventiveMaintenance = null;
    }
}
