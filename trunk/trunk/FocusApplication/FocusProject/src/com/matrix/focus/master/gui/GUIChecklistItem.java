package com.matrix.focus.master.gui;

import com.matrix.components.MDataCombo;
import com.matrix.components.MTextbox;
import com.matrix.components.TitleBar;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.matrix.focus.util.MPanel;
import com.matrix.focus.master.entity.Inspection;
import com.matrix.focus.mdi.MDI;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.DataAssistantDialog;
import com.matrix.focus.util.ImageLibrary;
import java.awt.Font;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
//import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GUIChecklistItem extends MPanel {
    
    private TitleBar titlebar = new TitleBar();
    private MDI frame;
    private MessageBar messagebar;
    
    private Connection CONN;
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
    private MDataCombo mcmbType = new MDataCombo();
    private JLabel jLabel1 = new JLabel();
    private JScrollPane jScrollPane2 = new JScrollPane();
    private boolean hasLogData = false;
    private int categoryComboSelectedIndex = 0;
    private Inspection inspection;

    public GUIChecklistItem(DBConnectionPool connection, MDI frame, MessageBar messagebar) {
            this.CONN = connection.getConnection();
            this.frame = frame;
            this.messagebar = messagebar;
            try{
                titlebar.setTitle("Inspection Master Data");
                titlebar.setDescription("The facility to manage reading master data.");
                titlebar.setImage(ImageLibrary.TITLE_CONDITION_MONITORING);
                setMode("LOAD");
                jbInit();
            }
            catch(Exception e) {
              e.printStackTrace();
            }
        }
        
    private void jbInit() throws Exception{
        this.setLayout(null);
        this.setSize(new Dimension(988, 305));
        titlebar.setBounds(new Rectangle(0, 0, 715, 75));
        jPanel1.setBounds(new Rectangle(10, 85, 590, 170));
        jPanel1.setLayout(null);
        jPanel1.setBorder(BorderFactory.createTitledBorder("Inspections"));
        jLabel2.setText("Description");
        jLabel2.setBounds(new Rectangle(20, 40, 65, 25));
        jLabel2.setFont(new Font("Tahoma", 0, 11));
        mtxtPreventiveMaintenanceID.setBounds(new Rectangle(20, 20, 250, 20));
        mtxtPreventiveMaintenanceID.setTxtWidth(100);
        mtxtPreventiveMaintenanceID.setCaption("Preventive Maintenance ID");
        mtxtPreventiveMaintenanceID.setEditable(false);
        mtxtPreventiveMaintenanceID.setLblWidth(150);
        mtxtPreventiveMaintenanceID.setLayout(null);
        mtxtPreventiveMaintenanceID.setLblFont(new Font("Tahoma", 0, 11));
        mtxtPreventiveMaintenanceID.setFont(new Font("Tahoma", 0, 11));
        btnCancel.setText("Cancel");
        btnCancel.setBounds(new Rectangle(615, 210, 100, 25));
        btnCancel.setSize(new Dimension(100, 25));
        btnCancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    btnCancel_actionPerformed(e);
                }
            });
        btnSave.setText("Save");
        btnSave.setBounds(new Rectangle(615, 180, 100, 25));
        btnSave.setSize(new Dimension(100, 25));
        btnSave.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    btnSave_actionPerformed(e);
                }
            });
        btnDelete.setText("Delete");
        btnDelete.setBounds(new Rectangle(615, 150, 100, 25));
        btnDelete.setSize(new Dimension(100, 25));
        btnDelete.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    btnDelete_actionPerformed(e);
                }
            });
        btnAdd.setText("Add ");
        btnAdd.setBounds(new Rectangle(615, 90, 100, 25));
        btnAdd.setSize(new Dimension(100, 25));
        btnAdd.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    btnAdd_actionPerformed(e);
                }
            });
        btnEdit.setText("Edit");
        btnEdit.setBounds(new Rectangle(615, 120, 100, 25));
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
            
        btnAddCategory.setBounds(new Rectangle(375, 110, 30, 20));
        btnAddCategory.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    btnAddCategory_actionPerformed(e);
                }
            });
        mcmbType.setBounds(new Rectangle(20, 135, 350, 20));
        mcmbType.setCaption("Maintenance Type");
        mcmbType.setLblWidth(150);
        mcmbType.setCmbWidth(200);
        //jLabel1.setText("Procedure");
        jLabel1.setText("");
        jLabel1.setBounds(new Rectangle(20, 155, 55, 20));
        jScrollPane2.setBounds(new Rectangle(170, 45, 405, 60));
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
        mdcCategory.populate(CONN,"SELECT DISTINCT Category FROM inspectionmaster");
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
      mcmbType.setCmbEnable(value);
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
        inspection= new Inspection(CONN);
        inspection.id = mtxtPreventiveMaintenanceID.getInputText();
        inspection.description = taDescription.getText();
        inspection.category = mdcCategory.getSelectedItem().toString();
        //preventiveMaintenance.procedure = txtProcedure.getText();
        //preventiveMaintenance.pm_type = mcmbType.getSelectedItem().toString();
        
        boolean result = inspection.save();
        if(result){
            messagebar.setMessage("New Inspection saved","OK");
          setMode("SEARCH");
        }
        else{
            messagebar.setMessage("Inspection already exists","WARN");
        }
    }
    
    public void updatePM() {
        inspection.id = mtxtPreventiveMaintenanceID.getInputText();
        inspection.description = taDescription.getText();
        inspection.category = mdcCategory.getSelectedItem().toString();
        //preventiveMaintenance.procedure = txtProcedure.getText();
        //preventiveMaintenance.pm_type = mcmbType.getSelectedItem().toString();
        
        boolean result = inspection.update();
        if(result){
          messagebar.setMessage("Preventive Maintenance updated","OK");
          setMode("SEARCH");
        }
        else{
          messagebar.setMessage("Error while updating.","ERROR");
        }
    }

    private void btnSave_actionPerformed(ActionEvent e) {
        e.toString();
        if(validate_PM()){
            if(ADD_NEW_MODE){
              savePM();
            }
            else{
              updatePM();
            }
        }
    }

    private void btnSearch_actionPerformed(ActionEvent e) {
        e.toString();
        hasLogData = false;
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select Preventive Maintenance - Data Assistant","SELECT Inspection_ID, Category, Description FROM inspectionmaster",CONN);
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
    }
    
    private void searchPreventiveMaintenance(String id){
      inspection = new Inspection(CONN,id);
      mtxtPreventiveMaintenanceID.setInputText(inspection.id);
      taDescription.setText(inspection.description);
      mdcCategory.setSelectedItem(inspection.category);
      hasLogData  = isExistingLogData();
      categoryComboSelectedIndex = mdcCategory.getComboBox().getSelectedIndex();
      //mcmbType.setSelectedItem(preventiveMaintenance.pm_type);
      //txtProcedure.setText(preventiveMaintenance.procedure);
    }

    private void btnEdit_actionPerformed(ActionEvent e) {
      e.toString();
      setMode("EDIT");
    }

    private void btnAdd_actionPerformed(ActionEvent e) {
      e.toString();
      setMode("ADD");
    }

    private void btnCancel_actionPerformed(ActionEvent e) {
      e.toString();
      setMode("LOAD");    
    }

    private void btnDelete_actionPerformed(ActionEvent e) {
        e.toString();
        if ((messagebar.showConfirmDialog(frame,"Are you sure you want to delete \nthe selected Preventive Maintenance from the system?","Remove Item")== MessageBar.YES_OPTION)){
            boolean result = false;
            if (!(isExistingLogData()))
            {
                result = inspection.delete();
            }
            else {
                messagebar.setMessage("There are existing log entries for this corresponding Inspection item\n" +
                "Remove the log entries before delete this item","OK");
            }
                
          if(result){
            messagebar.setMessage("Preventive Maintenance deleted","OK");
            setMode("LOAD");
          }
          else{
            messagebar.setMessage("Error while deleting","ERROR");
          }
        }   
    }
    
    private boolean isExistingLogData() {
         String sql="SELECT * FROM inspection_log WHERE Inspection_ID = '" + inspection.id + "'";  
         try{
         Statement st=CONN.createStatement();
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
        String newCategory = messagebar.showInputDialog(frame,"Enter the new category","New Category");
        if(!(newCategory.equals("")))
        {
            mdcCategory.addItem(newCategory);
            mdcCategory.setSelectedItem(newCategory);
        }
    }

 
    private boolean validate_PM(){
    
        if(mtxtPreventiveMaintenanceID.getInputText().trim().equals("")){
            messagebar.setMessage("Preventive Maintenance ID field is empty","ERROR");
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
        
    public synchronized String getNextPreventiveMaintenanceID(){
        /*[ PM-XXXX ]*/
        String sql = "SELECT MAX(Inspection_ID)+1 FROM inspectionmaster";
        try{
            ResultSet rec = CONN.createStatement().executeQuery(sql);
            rec.next();
            return rec.getString(1);
            
        }
        catch(Exception er){
            return "1";
        }
        
    }
}
