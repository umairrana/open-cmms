package com.matrix.focus.master.gui;

import com.matrix.focus.util.ImageLibrary;
import com.matrix.focus.util.MPanel;

import com.matrix.components.MDataCombo;
import com.matrix.components.MTextbox;

import com.matrix.components.TitleBar;
import com.matrix.focus.master.entity.Reading;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.DBConnectionPool;

import com.matrix.focus.util.DataAssistantDialog;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
//import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class GUIConditionMonitoring extends MPanel {
    private TitleBar titlebar = new TitleBar();
    private JFrame frame;
    private MessageBar messagebar;
    
    private Reading reading; 
    private boolean ADD_NEW_MODE;
    private JPanel jPanel1 = new JPanel();
    private JLabel jLabel2 = new JLabel();
    private JTextArea taDescription = new JTextArea();
    private MTextbox mtxtPreventiveMaintenanceID = new MTextbox();
    private JButton btnCancel = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
    private JButton btnSave = new JButton(new ImageIcon(ImageLibrary.BUTTON_SAVE));
    private JButton btnDelete = new JButton(new ImageIcon(ImageLibrary.BUTTON_DELETE));
    private JButton btnAdd = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));
    private JButton btnEdit = new JButton(new ImageIcon(ImageLibrary.BUTTON_EDIT));
    private JButton btnSearch = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private MDataCombo mdcCategory = new MDataCombo();
    private JButton btnAddCategory = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));
    private JLabel jLabel1 = new JLabel();
    private JScrollPane jScrollPane2 = new JScrollPane();
    private boolean hasLogData = false;
    private int categoryComboSelectedIndex = 0;
    private Connection dbcon;
    
    public GUIConditionMonitoring(DBConnectionPool pool, JFrame frame, MessageBar msgBar){
        dbcon = pool.getConnection();
        this.frame = frame;
        this.messagebar = msgBar;
        try{
            titlebar.setTitle("Reading Master Data");
            titlebar.setDescription("The facility to manage reading master data.");
            titlebar.setImage(ImageLibrary.TITLE_CONDITION_MONITORING);
            setMode("LOAD");
            jbInit();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception{
        this.setLayout(null);
        this.setSize(new Dimension(865, 547));
        titlebar.setBounds(new Rectangle(10, 10, 675, 70));
        jPanel1.setBounds(new Rectangle(10, 85, 565, 170));
        jPanel1.setLayout(null);
        jPanel1.setBorder(BorderFactory.createTitledBorder("Readings"));
        jLabel2.setText("Description");
        jLabel2.setBounds(new Rectangle(20, 40, 65, 25));
        jLabel2.setFont(new Font("Tahoma", 0, 11));
        mtxtPreventiveMaintenanceID.setBounds(new Rectangle(20, 20, 250, 20));
        mtxtPreventiveMaintenanceID.setTxtWidth(100);
        mtxtPreventiveMaintenanceID.setCaption("Reading ID");
        mtxtPreventiveMaintenanceID.setEditable(false);
        mtxtPreventiveMaintenanceID.setLblWidth(150);
        mtxtPreventiveMaintenanceID.setLayout(null);
        mtxtPreventiveMaintenanceID.setLblFont(new Font("Tahoma", 0, 11));
        mtxtPreventiveMaintenanceID.setFont(new Font("Tahoma", 0, 11));
        btnCancel.setText("Cancel");
        btnCancel.setBounds(new Rectangle(585, 215, 100, 25));
        btnCancel.setSize(new Dimension(100, 25));
        btnCancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnCancel_actionPerformed(e);
                    }
                });
        btnSave.setText("Save");
        btnSave.setBounds(new Rectangle(585, 185, 100, 25));
        btnSave.setSize(new Dimension(100, 25));
        btnSave.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnSave_actionPerformed(e);
                    }
                });
        btnDelete.setText("Delete");
        btnDelete.setBounds(new Rectangle(585, 155, 100, 25));
        btnDelete.setSize(new Dimension(100, 25));
        btnDelete.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnDelete_actionPerformed(e);
                    }
                });
        btnAdd.setText("Add ");
        btnAdd.setBounds(new Rectangle(585, 95, 100, 25));
        btnAdd.setSize(new Dimension(100, 25));
        btnAdd.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnAdd_actionPerformed(e);
                    }
                });
        btnEdit.setText("Edit");
        btnEdit.setBounds(new Rectangle(585, 125, 100, 25));
        btnEdit.setSize(new Dimension(100, 25));
        btnEdit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnEdit_actionPerformed(e);
                    }
                });
        btnSearch.setBounds(new Rectangle(275, 20, 30, 20));
        
        btnSearch.setFont(new Font("Tahoma", 0, 11));
        btnSearch.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
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
        mdcCategory.getComboBox().addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e) {
                            if (hasLogData){
                                messagebar.setMessage("There are existing log data for the corresponding inspection\n" +
                                "Cannot change the category","ERROR");
                                mdcCategory.getComboBox().setSelectedIndex(categoryComboSelectedIndex);
                            }
                        }
                    });
        mdcCategory.populate(dbcon,"SELECT DISTINCT Category FROM readingsmaster");

        btnAddCategory.setBounds(new Rectangle(375, 110, 30, 20));
        btnAddCategory.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnAddCategory_actionPerformed(e);
                    }
                });
        //jLabel1.setText("Procedure");
        jLabel1.setText("");
        jLabel1.setBounds(new Rectangle(20, 155, 55, 20));
        jScrollPane2.setBounds(new Rectangle(170, 45, 380, 60));
        this.add(jPanel1, null);
        this.add(btnCancel, null);
        this.add(btnSave, null);
        jScrollPane2.getViewport().add(taDescription, null);
        jPanel1.add(jScrollPane2, null);
        jPanel1.add(jLabel1, null);
        //jPanel1.add(mcmbType, null);
        jPanel1.add(btnAddCategory, null);
        jPanel1.add(mdcCategory, null);
        jPanel1.add(btnSearch, null);
        jPanel1.add(mtxtPreventiveMaintenanceID, null);
        jPanel1.add(jLabel2, null);


        this.add(titlebar, null);
        this.add(btnAdd, null);
        this.add(btnDelete, null);
        this.add(btnEdit, null);
    }
    
    private void setMode(String mode){
      if(mode.equals("EDIT")){
        ADD_NEW_MODE = false;
        setEditable(true);
       
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
      }
      else if(mode.equals("ADD")){
        ADD_NEW_MODE = true;
        clearAll();
        mtxtPreventiveMaintenanceID.setInputText(getNextPreventiveMaintenanceID());
        mtxtPreventiveMaintenanceID.setEditable(false);
        setEditable(true);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
      }
      else if(mode.equals("LOAD")){
          hasLogData = false;
        ADD_NEW_MODE = false;
        clearAll();
        btnAdd.setEnabled(true);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(false);
        setEditable(false);
      }
      else if(mode.equals("SEARCH")){
        ADD_NEW_MODE = false;
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnAdd.setEnabled(false);
        setEditable(false);
      }
    }

    private void setEditable(boolean value){
      mtxtPreventiveMaintenanceID.setEditable(false);
      taDescription.setEditable(value);
      mdcCategory.setCmbEnable(value);
      btnAddCategory.setEnabled(value);
    }
    
    private void clearAll(){
      try {
          mtxtPreventiveMaintenanceID.setInputText("");
          taDescription.setText("");
          mdcCategory.getComboBox().setSelectedIndex(0);
      }
      catch (Exception e) {
          e.toString();
      }
    }
    
    public void savePM() {
        reading= new Reading(dbcon);
        reading.id = mtxtPreventiveMaintenanceID.getInputText();
        reading.description = taDescription.getText();
        reading.category = mdcCategory.getSelectedItem().toString();
        //preventiveMaintenance.procedure = txtProcedure.getText();
        //preventiveMaintenance.pm_type = mcmbType.getSelectedItem().toString();
        
        boolean result = reading.save();
        if(result){
          messagebar.setMessage("New Inspection saved", "OK");  
          setMode("SEARCH");
        }
        else{
            messagebar.setMessage("Inspection already exists", "OK"); 
          //messageBar.setMessage("Duplicate entry for Preventive Maintainance ID.","ERROR");
        }
    }
    
    public void updatePM() {
        reading.id = mtxtPreventiveMaintenanceID.getInputText();
        reading.description = taDescription.getText();
        reading.category = mdcCategory.getSelectedItem().toString();
        //preventiveMaintenance.procedure = txtProcedure.getText();
        //preventiveMaintenance.pm_type = mcmbType.getSelectedItem().toString();
        
        boolean result = reading.update();
        if(result){
          messagebar.setMessage("Reading updated","OK");
          setMode("SEARCH");
        }
        else{
          messagebar.setMessage("Error while updating.","ERROR");
        }
    }

    private void btnSave_actionPerformed(ActionEvent e) {
        if(validate_PM()){
            if(ADD_NEW_MODE){
              savePM();
            }
            else{
              updatePM();
              e.toString();
            }
        }
    }

    private void btnSearch_actionPerformed(ActionEvent e) {
        hasLogData = false;
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select Preventive Maintenance - Data Assistant","SELECT Reading_ID, Category, Description FROM readingsmaster",dbcon);
        d.grow(500,600,450);
        d.setFirstColumnWidth(100);
        d.setSecondColumnWidth(80);
        d.setThirdColumnWidth(250);
        d.setLocationRelativeTo(frame);
        d.setVisible(true); 
        String rtnVal = d.getValue();
        mtxtPreventiveMaintenanceID.setInputText( (rtnVal.equals("") ? mtxtPreventiveMaintenanceID.getInputText() : rtnVal));
        if(!rtnVal.equals("")){
          searchPreventiveMaintenance(rtnVal);
          setMode("SEARCH");
        }
        e.toString();
    }
    
    private void searchPreventiveMaintenance(String id){
      reading = new Reading(dbcon,id);
      mtxtPreventiveMaintenanceID.setInputText(reading.id);
      taDescription.setText(reading.description);
      mdcCategory.setSelectedItem(reading.category);
        hasLogData  = isExistingLogData();
        categoryComboSelectedIndex = mdcCategory.getComboBox().getSelectedIndex();
      //mcmbType.setSelecte45851dItem(preventiveMaintenance.pm_type);
      //txtProcedure.setText(preventiveMaintenance.procedure);
    }

    private void btnEdit_actionPerformed(ActionEvent e) {
      setMode("EDIT");
      e.toString();
    }

    private void btnAdd_actionPerformed(ActionEvent e) {
      setMode("ADD");
      e.toString();
    }

    private void btnCancel_actionPerformed(ActionEvent e) {
      setMode("LOAD");  
      e.toString();
    }

    private void btnDelete_actionPerformed(ActionEvent e) {
    e.toString();
        if(messagebar.showConfirmDialog(frame,"Are you sure you want to delete \nthe selected Reading from the system?","Remove Item")== MessageBar.YES_OPTION){
            boolean result = false;
            if (!(isExistingLogData()))
            {
                result = reading.delete();
            }
            else {
                messagebar.setMessage("There are existing log entries for this corresponding Reading item\n" +
                "Remove the log entries before delete this item","ERROR");
                    
            }
                
          if(result){
            messagebar.setMessage("Reading deleted","OK");
            setMode("LOAD");
          }
          else{
            messagebar.setMessage("Reading NOT deleted","ERROR");
          }
        }   
    }
    
    private boolean isExistingLogData() {
         String sql="SELECT * FROM Readings_log WHERE Reading_ID = '" + reading.id + "'";  
         try{
         Statement st=dbcon.createStatement();
         ResultSet rsmax=st.executeQuery(sql);
         
         while(rsmax.next())
         {
            // JOptionPane.showMessageDialog(this,"rows there");
             return true; 
         }
         }
         catch(Exception ee)
         {
            ee.printStackTrace();
         }
         return false;
     }

    private void btnAddCategory_actionPerformed(ActionEvent e) {
        e.toString();
        String newCategory = messagebar.showInputDialog(frame,"Enter the new category","New category");
        
        if(!(newCategory.equals("")))
        {
            mdcCategory.addItem(newCategory);
            mdcCategory.setSelectedItem(newCategory);
        }
    }

    private void btnType_actionPerformed(ActionEvent e) {
    e.toString();
    /*
        try{
            String s = MessageBar.showInputDialog(frame,"Preventive Maintenance Type","Add New Type");
            if (!s.equals("")){
              mcmbType.addItem(s);
              mcmbType.setSelectedItem(s);  
            }
        }catch(Exception er){}
        */
    }
    
    private boolean validate_PM(){
    
        if(mtxtPreventiveMaintenanceID.getInputText().trim().equals("")){
            messagebar.setMessage("Reading ID field is empty","ERROR");
            mtxtPreventiveMaintenanceID.setFocus(1);
            return false;
        }
        else if(taDescription.getText().trim().equals("")){
            messagebar.setMessage("Description field is empty","ERROR");
            return false;
        }
        else if(mdcCategory.getComboBox().getSelectedIndex()==0){
            messagebar.setMessage("Please select the category","ERROR");
            return false;
        }
            return true;
        }
        
    public String getNextPreventiveMaintenanceID(){
        /*[ PM-XXXX ]*/
        String sql = "SELECT MAX(Reading_ID)+1 FROM readingsmaster";
        try{
            ResultSet rec = dbcon.createStatement().executeQuery(sql);
            rec.next();
            return rec.getString(1);
            
        }
        catch(Exception er){
            return "1";
        }
        
    }
    
}
