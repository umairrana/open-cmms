package com.matrix.focus.register;

import com.matrix.components.MTextbox;
import com.matrix.components.TitleBar;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.DataAssistantDialog;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.sql.Connection;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Rectangle;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import com.matrix.focus.util.MPanel;
import com.matrix.focus.mdi.MDI;
import com.matrix.focus.planner.AssetPM;
import com.matrix.focus.planner.PlanningAsset;
import com.matrix.focus.util.ButtonCellRenderer;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.DateCell;
import com.matrix.focus.util.ImageLibrary;
import com.matrix.focus.util.LockedLabelCellRenderer;
import com.matrix.focus.util.MList;
import com.matrix.focus.util.StandardTimeCell;
import com.matrix.focus.util.TimePeriodDialog;
import com.matrix.focus.util.Utilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

import java.util.Vector;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

public class GUIRegisterMaintenance extends MPanel{

    private JPanel jPanel2 = new JPanel();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JScrollPane jScrollPane2 = new JScrollPane();
    private JButton btnAddFrom = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));
    private JButton btnRemoveFrom = new JButton(new ImageIcon(ImageLibrary.BUTTON_REMOVE));
    private JButton btnSave = new JButton(new ImageIcon(ImageLibrary.BUTTON_SAVE));
    private JScrollPane jScrollPane3 = new JScrollPane();
    private JButton btnCancel = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
    private TitleBar titlebar = new TitleBar();
    private JPanel jPanel1 = new JPanel();
    private JPanel jPanel4 = new JPanel();
    private JButton btnEdit = new JButton(new ImageIcon(ImageLibrary.BUTTON_EDIT));
    private MTextbox txtCustomer = new MTextbox();
    private MTextbox txtBrand = new MTextbox();
    private MTextbox txtCategory = new MTextbox();
    private MTextbox txtModel = new MTextbox();
    private JButton btnBrand = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton btnCategory = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton btnModel = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton btnCustomer = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private MTextbox txtAsset = new MTextbox();
    private MTextbox txtCommisionedDate = new MTextbox();
    private JButton btnAsset = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton btnMasterRefresh = new JButton(new ImageIcon(ImageLibrary.BUTTON_REFRESH));
    
    private Connection connection;
    private Connection pmMasterConnection;
    private MDI frame;
    private MessageBar messageBar;
    private MList lstAssets = new MList("Machines");
    private PreventiveMaintenanceMasteListTable tblMaster = new PreventiveMaintenanceMasteListTable();
    private RegisterTable tblRegister;
    private LabourRequirementDialog dlgLabour;
    private PartRequirementDialog dlgPart;
    private String SELECTED_CATEGORY_ID = "";
    private String SELECTED_MODEL_ID = "";
    private String SELECTED_CUSTOMER_ID = "";
    private Savepoint savePoint;
    private Vector<PreventiveMaintenanceRegisterEntry> REGISTED_PMS_ORIGINALS;

    public GUIRegisterMaintenance(DBConnectionPool pool, final MDI frame, MessageBar msgBar){
        /**From the MDI*/
        /**New Database connection was taken to manage the transactions*/
        this.connection = pool.getConnection();
        this.pmMasterConnection = pool.getConnection();
        try{
            this.connection.setAutoCommit(false);
        }
        catch(Exception er){
        }
        
        this.frame = frame;
        this.messageBar = msgBar;
    
        try{
            titlebar.setTitle("Register Preventive Maintenances");
            titlebar.setDescription("The facility to register preventive maintenances to Machines.");
            titlebar.setImage(ImageLibrary.TITLE_PREVENTIVE_MAINTENANCE_REGISTER);
        
            lstAssets.getColumnModel().getColumn(0).setCellRenderer(new NewAssetCellRenderer());
            
            tblRegister = new RegisterTable(connection);
        
            jbInit();
          
            lstAssets.addMouseListener(
                  new MouseAdapter(){
                      public void mouseClicked(MouseEvent me){
                        int row = lstAssets.getSelectedRow();
                        if(row!=-1){
                            selectAsset(lstAssets.getValueAt(row,0).toString());
                        }
                      }    
                  }
            );
            
            lstAssets.addKeyListener(
                new KeyAdapter(){
                    public void keyReleased(KeyEvent ke){
                        int row = lstAssets.getSelectedRow();
                        if(row!=-1){
                            selectAsset(lstAssets.getValueAt(row,0).toString());
                        }
                    }
                }
            );
          
          tblRegister.addMouseListener(
                new MouseAdapter(){
                    public void mouseClicked(MouseEvent me){
                        int row = tblRegister.getSelectedRow();
                        int col = tblRegister.getSelectedColumn();
                        if(row!=-1){
                            if(me.getButton()==1){
                                String selected_asset = txtAsset.getInputText();
                                String selected_pm = tblRegister.getValueAt(row,0).toString();
                                if(col==8){
                                    dlgLabour = new LabourRequirementDialog(frame,messageBar,selected_asset,selected_pm,true,connection);
                                    dlgLabour.setLocationRelativeTo(frame);
                                    dlgLabour.setVisible(true);
                                }
                                else if(col==9){
                                    dlgPart = new PartRequirementDialog(frame,messageBar,selected_asset,selected_pm,true,connection);
                                    dlgPart.setLocationRelativeTo(frame);
                                    dlgPart.setVisible(true); 
                                }
                            }
                        }
                        else{
                            messageBar.setMessage("Press the Edit button and then select a row.","ERROR");
                        }
                    }
                }
          );
          
          tblMaster.populate();
        }
        catch(Exception e){
          e.printStackTrace();
        }
        setMode("LOAD");
    }

    private void selectAsset(String asset_id){
          try {
              setAssetInfo(asset_id);
              getRegisteredMaintenance(asset_id);
              setMode("SEARCH");
          }
          catch(Exception e) {
              messageBar.setMessage(e.getMessage(),"ERROR");
          }
    }
    
    private void setAssetInfo(String asset_id){
    
        txtAsset.setInputText("");
        txtBrand.setInputText("");
        SELECTED_CATEGORY_ID = "";
        txtCategory.setInputText("");
        SELECTED_MODEL_ID = "";
        txtModel.setInputText("");
        txtCommisionedDate.setInputText("");
        SELECTED_CUSTOMER_ID = "";
        txtCustomer.setInputText("");
        
        String sql = "SELECT a.brand, " +
                            "c.category_id," +
                            "m.model_id," +
                            "IF(a.Date_of_Commission='0000-00-00','0000-00-00',a.Date_of_Commission) com_date," +
                            "a.division_id, " +
                            "d.name " +
                     "FROM asset a, asset_category c, asset_model m, division d " +
                     "WHERE m.category_id = c.category_id AND m.model_id = a.model_id AND a.asset_id='"+asset_id+"' AND a.division_id = d.division_id";
        try{
            ResultSet rec = connection.createStatement().executeQuery(sql);
            rec.first();
            txtAsset.setInputText(asset_id);
            txtBrand.setInputText(rec.getString("brand"));
            SELECTED_CATEGORY_ID = rec.getString("category_id");
            txtCategory.setInputText(SELECTED_CATEGORY_ID);
            SELECTED_MODEL_ID = rec.getString("model_id");
            txtModel.setInputText(SELECTED_MODEL_ID);
            SELECTED_CUSTOMER_ID = rec.getString("division_id");
            txtCustomer.setInputText(rec.getString("d.name"));
            txtCommisionedDate.setInputText(rec.getString("com_date"));
        } 
        catch(Exception ex){
            ex.printStackTrace();
        } 
        
    }
    
    public void close(){
        try {
            connection.close();
        } catch (SQLException e) {
        }
    }

    private void btnEdit_actionPerformed(ActionEvent e) {
        try{
            savePoint = connection.setSavepoint();
        } 
        catch (Exception ex){
            ex.printStackTrace();
        }
        setMode("EDIT");
    }

    private void btnBrand_actionPerformed(ActionEvent e) {
        String sql = "SELECT DISTINCT Brand FROM asset";
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select Brand - Data Assistant",sql,connection);
        d.setFirstColumnWidth(300);
        d.setLocationRelativeTo(btnBrand);
        d.setVisible(true); 
        String rtnVal = d.getValue();
        if(!rtnVal.equals("")){
            txtBrand.setInputText(rtnVal);
            txtCategory.setInputText("");
            txtModel.setInputText("");
            txtCustomer.setInputText("");
            txtAsset.setInputText("");
            txtCommisionedDate.setInputText("");
            lstAssets.populate(connection,"SELECT asset_id FROM asset WHERE brand='"+txtBrand.getInputText()+"'");
        }
    }

    private void btnCategory_actionPerformed(ActionEvent e) {
        if(txtBrand.getInputText().isEmpty()){
            messageBar.setMessage("Please select a Brand first.","ERROR");
        }
        else{
            String sql = "SELECT category_id AS ID FROM asset_category WHERE category_id IN " +
            "(SELECT category_id FROM asset_model WHERE model_id IN (SELECT model_id FROM asset WHERE brand ='" + txtBrand.getInputText() + "'))";
            
            DataAssistantDialog d = new DataAssistantDialog(frame,"Select Category - Data Assistant",sql,connection);
            d.setFirstColumnWidth(300);
            d.setLocationRelativeTo(btnBrand);
            d.setVisible(true); 
            String rtnVal = d.getValue();
            if(!rtnVal.equals("")){
                SELECTED_CATEGORY_ID = rtnVal;
                txtCategory.setInputText(d.getDescription());
                txtModel.setInputText("");
                txtCustomer.setInputText("");
                txtAsset.setInputText("");
                txtCommisionedDate.setInputText("");
                sql = "SELECT asset_id " +
                      "FROM asset " +
                      "WHERE brand='"+txtBrand.getInputText()+"' AND " +
                            "model_id IN (SELECT model_id FROM asset_model WHERE category_id='"+SELECTED_CATEGORY_ID+"')";
                lstAssets.populate(connection,sql);
            }
        }
    }

    private void btnModel_actionPerformed(ActionEvent e) {
        if(txtCategory.getInputText().isEmpty()){
            messageBar.setMessage("Please select a Category first.","ERROR");
        }
        else{
            String sql = "SELECT model_id AS ID,Description FROM asset_model WHERE category_id='"+SELECTED_CATEGORY_ID+"'";
            DataAssistantDialog d = new DataAssistantDialog(frame,"Select Model - Data Assistant",sql,connection);
            d.setLocationRelativeTo(btnBrand);
            d.setVisible(true); 
            String rtnVal = d.getValue();
            if(!rtnVal.equals("")){
                SELECTED_MODEL_ID = rtnVal;
                txtModel.setInputText(d.getDescription());
                txtCustomer.setInputText("");
                txtAsset.setInputText("");
                txtCommisionedDate.setInputText("");
                sql = "SELECT asset_id " +
                      "FROM asset " +
                      "WHERE brand='"+txtBrand.getInputText()+"' AND " +
                            "model_id ='"+SELECTED_MODEL_ID+"'";
                lstAssets.populate(connection,sql);
            }
        }
    }

    private void btnCustomer_actionPerformed(ActionEvent e) {
        if(isCustomerBasedSelection()){
            String sql = "SELECT division_id AS ID, Name, Address " +
                         "FROM division " +
                         "WHERE division_id IN (SELECT division_id FROM asset) ORDER BY Name";
                         
            DataAssistantDialog d = new DataAssistantDialog(frame,"Select Customer - Data Assistant",sql,connection);
            d.grow(600,0,580);
            d.setFirstColumnWidth(50);
            d.setSecondColumnWidth(200);
            d.setThirdColumnWidth(330);
            d.setLocationRelativeTo(btnBrand);
            d.setVisible(true); 
            String rtnVal = d.getValue();
            if(!rtnVal.equals("")){
                SELECTED_CUSTOMER_ID = rtnVal;
                txtCustomer.setInputText(d.getDescription());
                txtAsset.setInputText("");
                txtCommisionedDate.setInputText("");
                sql = "SELECT asset_id " +
                      "FROM asset " +
                      "WHERE division_id ='"+SELECTED_CUSTOMER_ID+"'";
                lstAssets.populate(connection,sql);
            }
        }
        else{
            if(txtModel.getInputText().isEmpty()){
                messageBar.setMessage("Please select a Model first.","ERROR");
            }
            else{
                String sql = "SELECT division_id AS ID, Name " +
                             "FROM division " +
                             "WHERE division_id IN (" +
                                    "SELECT division_id " +
                                    "FROM asset " +
                                    "WHERE brand = '"+txtBrand.getInputText()+"' AND " +
                                    "model_id = '"+SELECTED_MODEL_ID+"')";
                                                  
                DataAssistantDialog d = new DataAssistantDialog(frame,"Select Customer - Data Assistant",sql,connection);
                d.setLocationRelativeTo(btnBrand);
                d.setVisible(true); 
                String rtnVal = d.getValue();
                if(!rtnVal.equals("")){
                    SELECTED_CUSTOMER_ID = rtnVal;
                    txtCustomer.setInputText(d.getDescription());
                    sql = "SELECT asset_id " +
                          "FROM asset " +
                          "WHERE brand='"+txtBrand.getInputText()+"' AND " +
                                "model_id ='"+SELECTED_MODEL_ID+"' AND " +
                                "division_id ='"+SELECTED_CUSTOMER_ID+"'";
                    lstAssets.populate(connection,sql);
                }
            }
        }
    }
    
    private boolean isCustomerBasedSelection(){
        return (txtBrand.getInputText().isEmpty() && txtCategory.getInputText().isEmpty() && txtModel.getInputText().isEmpty());
    }
    
    private void btnAsset_actionPerformed(ActionEvent e) {
        String sql = "SELECT asset_id AS 'ID' FROM asset";
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select Asset - Data Assistant",sql,connection);
        d.setFirstColumnWidth(300);
        d.setLocationRelativeTo(btnBrand);
        d.setVisible(true); 
        String rtnVal = d.getValue();
        if(!rtnVal.equals("")){
            txtAsset.setInputText(rtnVal);
            selectAsset(rtnVal);
            lstAssets.deleteAll();
            lstAssets.addItem(rtnVal);
        }
    }
    
    private void btnMasterRefresh_actionPerformed(ActionEvent e) {
        tblMaster.populate();
    }

    private void setMode(String mode){
        if(mode.equals("LOAD")){
            btnAddFrom.setEnabled(false);
            btnRemoveFrom.setEnabled(false);
            btnSave.setEnabled(false);
            btnEdit.setEnabled(false);
            tblRegister.setEnabled(false);
        }
        else if(mode.equals("EDIT")){
            if(true){
                btnAddFrom.setEnabled(true);
                btnRemoveFrom.setEnabled(true);
                btnSave.setEnabled(true);
            }
            btnEdit.setEnabled(false);
            tblRegister.setEnabled(true);
        }
        else if(mode.equals("SEARCH")){
            btnAddFrom.setEnabled(false);
            btnRemoveFrom.setEnabled(false);
            btnSave.setEnabled(false);
            btnEdit.setEnabled(true);
            tblRegister.setEnabled(false);
        }
    }

    private void jbInit() throws Exception{
        this.setLayout(null);
        this.setSize(new Dimension(998, 613));
        jPanel2.setBounds(new Rectangle(10, 320, 940, 245));
        jPanel2.setBorder(BorderFactory.createTitledBorder("Registered Preventive Maintenances"));
        jPanel2.setLayout(null);
        jScrollPane1.setBounds(new Rectangle(10, 20, 480, 200));
        jScrollPane2.setBounds(new Rectangle(10, 20, 920, 215));
        btnAddFrom.setBounds(new Rectangle(955, 340, 30, 30));
        btnAddFrom.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnAddFrom_actionPerformed(e);
            }
        });
        btnRemoveFrom.setBounds(new Rectangle(955, 375, 30, 30));
        btnRemoveFrom.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnRemoveFrom_actionPerformed(e);
            }
        });
        btnSave.setText("Save");
        btnSave.setBounds(new Rectangle(735, 570, 100, 25));
        btnSave.setSize(new Dimension(100, 25));
        btnSave.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnSave_actionPerformed(e);
                    }
                });
        jScrollPane3.setBounds(new Rectangle(270, 50, 160, 170));
        jScrollPane3.setBackground(Color.white);
        btnCancel.setText("Cancel");
        btnCancel.setBounds(new Rectangle(840, 570, 100, 25));
        btnCancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnCancel_actionPerformed(e);
                    }
                });
        titlebar.setBounds(new Rectangle(10, 10, 940, 70));
        jPanel1.setBounds(new Rectangle(10, 90, 440, 230));
        jPanel1.setLayout(null);
        jPanel1.setBorder(BorderFactory.createTitledBorder("Assets Selection"));
        jPanel4.setBounds(new Rectangle(450, 90, 500, 230));
        jPanel4.setLayout(null);
        jPanel4.setBorder(BorderFactory.createTitledBorder("Preventive Maintenances"));
        btnEdit.setText("Edit");
        btnEdit.setBounds(new Rectangle(630, 570, 100, 25));
        btnEdit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnEdit_actionPerformed(e);
                    }
                });
        txtCustomer.setBounds(new Rectangle(20, 25, 310, 20));
        txtCustomer.setCaption("Customer");
        txtCustomer.setLblWidth(60);
        txtCustomer.setTxtWidth(315);
        txtCustomer.setEditable(false);
        txtBrand.setBounds(new Rectangle(20, 50, 210, 20));
        txtBrand.setCaption("Brand");
        txtBrand.setLblWidth(60);
        txtBrand.setTxtWidth(150);
        txtBrand.setEditable(false);
        txtCategory.setBounds(new Rectangle(20, 75, 210, 20));
        txtCategory.setCaption("Category");
        txtCategory.setLblWidth(60);
        txtCategory.setTxtWidth(150);
        txtCategory.setEditable(false);
        txtModel.setBounds(new Rectangle(20, 100, 210, 20));
        txtModel.setCaption("Model");
        txtModel.setLblWidth(60);
        txtModel.setTxtWidth(150);
        txtModel.setEditable(false);
        btnBrand.setBounds(new Rectangle(235, 50, 30, 20));
        btnBrand.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnBrand_actionPerformed(e);
                    }
                });
        btnCategory.setBounds(new Rectangle(235, 75, 30, 20));
        btnCategory.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnCategory_actionPerformed(e);
                    }
                });
        btnModel.setBounds(new Rectangle(235, 100, 30, 20));
        btnModel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnModel_actionPerformed(e);
                    }
                });
        btnCustomer.setBounds(new Rectangle(400, 25, 30, 20));
        btnCustomer.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnCustomer_actionPerformed(e);
                    }
                });
        txtAsset.setBounds(new Rectangle(20, 125, 210, 20));
        txtAsset.setCaption("Machine");
        txtAsset.setLblWidth(60);
        txtAsset.setTxtWidth(150);
        txtAsset.setEditable(false);
        txtCommisionedDate.setBounds(new Rectangle(20, 150, 210, 20));
        txtCommisionedDate.setTxtWidth(80);
        txtCommisionedDate.setCaption("Commsioned Date");
        txtCommisionedDate.setEditable(false);
        btnAsset.setBounds(new Rectangle(235, 125, 30, 20));
        btnAsset.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnAsset_actionPerformed(e);
                    }
                });
        btnMasterRefresh.setBounds(new Rectangle(955, 110, 30, 30));
        btnMasterRefresh.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnMasterRefresh_actionPerformed(e);
                    }
                });
        jScrollPane3.getViewport().setBackground(Color.WHITE);
        jScrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.getViewport().add(tblMaster, null);
        jPanel4.add(jScrollPane1, null);
        this.add(btnMasterRefresh, null);
        this.add(btnEdit, null);
        this.add(jPanel4, null);
        jPanel1.add(btnAsset, null);
        jPanel1.add(txtCommisionedDate, null);
        jPanel1.add(txtAsset, null);
        jPanel1.add(btnCustomer, null);
        jPanel1.add(btnModel, null);
        jPanel1.add(btnCategory, null);
        jPanel1.add(btnBrand, null);
        jPanel1.add(txtModel, null);
        jPanel1.add(txtCategory, null);
        jPanel1.add(txtBrand, null);
        jPanel1.add(txtCustomer, null);
        jScrollPane3.getViewport().add(lstAssets, null);
        jPanel1.add(jScrollPane3, null);
        this.add(jPanel1, null);
        this.add(titlebar, null);
        this.add(btnCancel, null);
        jScrollPane2.getViewport().add(tblRegister, null);
        jPanel2.add(jScrollPane2, null);
        this.add(jPanel2, null);
        this.add(btnSave, null);
        this.add(btnAddFrom, null);
        this.add(btnRemoveFrom, null);
    }
    
    private void btnRemoveFrom_actionPerformed(ActionEvent e){
            int row = tblRegister.getSelectedRow();
            if(row!=-1){
                if(MessageBar.showConfirmDialog(frame,"Are you sure you want to delete \nthe selected Maintenance from the system?","Delete Maintenance")==MessageBar.YES_OPTION){               
                    String maintenance_id = tblRegister.getValueAt(row,0).toString();
                    
                    try{
                        AssetPM mpm;
                        try{
                            mpm = new AssetPM(txtAsset.getInputText(),maintenance_id,connection);
                        }
                        catch(Exception ace){
                            messageBar.setMessage("Selected maintenance was removed.","OK");
                            tblRegister.deleteRow(row);
                            return /*VOID*/;
                        }
                        
                        Savepoint sPointDelete = connection.setSavepoint("Delete");
                        try{
                            if(mpm.isPlanned()){
                                if(mpm.hasPendingWorkOrders()){
                                    messageBar.setMessage("There are pending Jobs for the selected PM.","ERROR");    
                                }
                                else if(mpm.HAS_DONE_WORK_ORDERS){
                                    mpm.deletePlanFrom(mpm.LAST_DONE_DATE);
                                    mpm.stopPlanning();
                                    connection.commit();
                                    messageBar.setMessage("Selected maintenance was stopped.","OK");
                                }
                                else{
                                    mpm.deleteCompletePlan();
                                    mpm.removeFromRegister();
                                    connection.commit();
                                    messageBar.setMessage("Selected maintenance was completly deleted.","OK");
                                    tblRegister.deleteRow(row);
                                }
                            }
                            else{
                                mpm.removeFromRegister();
                                connection.commit();
                                messageBar.setMessage("Selected maintenance was removed.","OK");
                                tblRegister.deleteRow(row);
                            } 
                        }
                        catch(Exception er){
                            connection.rollback(sPointDelete);
                            messageBar.setMessage("Error while deleting.","ERROR");
                        }
                    }
                    catch(Exception er){
                        er.printStackTrace();
                        messageBar.setMessage(er.getMessage(),"ERROR");
                    }
                }
            }
            else{
                messageBar.setMessage("Please selected a maintenace from the registration table.","ERROR");
            }
    }
    
    private boolean isPlanned(String maintenance_id){        
        String sql = "SELECT " +
                        "Preventive_Maintenance_ID " +
                     "FROM " +
                        "scheduled_preventive_maintenance " +
                     "WHERE " +
                        "Preventive_Maintenance_ID ='" + maintenance_id + "' AND " +
                        "Asset_ID = '" + txtAsset.getInputText() + "'";
        try{
            ResultSet rec = connection.createStatement().executeQuery(sql);
            int rows = 0;
            while(rec.next()){
                rows++;
            }
            return rows>0?true:false;
        }
        catch (Exception ex)  {
            ex.printStackTrace();
            return false;
        }
    }
    
    private boolean deleteRegistered(String maintenance_id){
        PreventiveMaintenanceRegisterEntry prme = new PreventiveMaintenanceRegisterEntry(connection);
        prme.preventive_maintenance_id = maintenance_id;
        prme.asset_id = txtAsset.getInputText();
        if(prme.delete()){
            return true;       
        }
        else{
            return false;
        }
    }
    
    private void btnAddFrom_actionPerformed(ActionEvent e){        
            int pmRow = tblMaster.getSelectedRow();
            
            if(!txtAsset.getInputText().isEmpty()){
                if(pmRow!=-1){
                    String pm_id = tblMaster.getValueAt(pmRow,1).toString();
                    String desc = tblMaster.getValueAt(pmRow,2).toString();
                    if(!isExisting(pm_id)){
                        if(addToRegisterTable(pm_id,desc)){
                            messageBar.setMessage("Selected maintenance was added.","OK");
                        }
                        else{
                            messageBar.setMessage("Error on adding new maintenance.","ERROR");
                        }
                    }
                    else{
                        messageBar.setMessage("Selected Maintenance has been already registered.","ERROR");
                    }
                }
                else{
                    messageBar.setMessage("Select a maintenance first.","ERROR");
                }
            }
            else{
                messageBar.setMessage("Select a Machine before adding maintenance.","ERROR");
            }
    }
    
    private boolean isExisting(String mid){
        int rows = tblRegister.getRowCount();
        for(int i=0;i<rows;i++){
            String id = tblRegister.getValueAt(i,0) + "";
            if(id.equals(mid)){
                return true;
            }
        }
        return false;
    }

    private boolean addToRegisterTable(String pm_id, String desc){
        /**Only plan affecting properties are assigned*/
        PreventiveMaintenanceRegisterEntry pmre = new PreventiveMaintenanceRegisterEntry(null);
        pmre.asset_id = txtAsset.getInputText();
        pmre.preventive_maintenance_id = pm_id;
        
        pmre.basis          = "Time Period";
        pmre.cycle_time     = "0-0-0";
        pmre.cycle_meter    = 0;
        pmre.starting_meter = 0;
        pmre.starting_date  = txtCommisionedDate.getInputText();
        pmre.plan           = Boolean.TRUE;
        
        /**Original registering values are kept in this Vector*/
        REGISTED_PMS_ORIGINALS.add(pmre);
        tblRegister.addRow(pm_id,desc,txtCommisionedDate.getInputText());     
        return true;
    }

    private boolean register(){
        int no_of_maintenance = tblRegister.getRowCount();
        PreventiveMaintenanceRegisterEntry edited;
        boolean done = false;
        
        try{
            PlanningAsset machine = new PlanningAsset(txtAsset.getInputText(),connection);
                
            for(int j=0;j<no_of_maintenance;j++){
                edited = new PreventiveMaintenanceRegisterEntry(connection);
                    edited.asset_id                   = txtAsset.getInputText();
                    edited.preventive_maintenance_id  = tblRegister.getValueAt(j,0).toString();
                    edited.criticalness               = tblRegister.getValueAt(j,2).toString();
                    edited.basis                      = tblRegister.getValueAt(j,3).toString();
                    edited.cycle_time                 = tblRegister.getValueAt(j,4).toString();
                    edited.cycle_meter                = Integer.parseInt(tblRegister.getValueAt(j,5).toString());
                    edited.standard_time              = tblRegister.getValueAt(j,6).toString();
                    edited.tolerance                  = Integer.parseInt(tblRegister.getValueAt(j,7).toString());
                    edited.starting_meter             = Integer.parseInt(tblRegister.getValueAt(j,10).toString());
                    edited.starting_date              = tblRegister.getValueAt(j,11).toString();
                    edited.plan                       = Boolean.parseBoolean(tblRegister.getValueAt(j,12).toString());
                
                    PreventiveMaintenanceRegisterEntry original = getPMRE(edited.preventive_maintenance_id);
                    
                    if(edited.save()){
                        AssetPM mpm = new AssetPM(original.asset_id,original.preventive_maintenance_id,connection);
                        done = true;
                        if(machine.hasPlannedPMs()){
                            mpm.planFrom(mpm.PLAN_START_DATE,mpm.PLAN_END_DATE,machine.AVERAGE_METER_PER_DAY);
                        }
                    }
                    else if(edited.update()){
                        AssetPM mpm = new AssetPM(original.asset_id,original.preventive_maintenance_id,connection);
                        modifyPlan(machine,mpm,original,edited);
                        done = true;
                    }
                    else{
                        return false;
                    }
            }
        }
        catch(Exception er){
            messageBar.setMessage(er.getMessage(),"ERROR");
            er.printStackTrace();
            return false;
        }
        return done;
    }
    
    private void modifyPlan(PlanningAsset machine, 
                            AssetPM mpm,
                            PreventiveMaintenanceRegisterEntry original,
                            PreventiveMaintenanceRegisterEntry edited) throws Exception{
                            
        if( (!original.basis.equals(edited.basis)) ||
            (!original.cycle_time.equals(edited.cycle_time)) ||
            (original.cycle_meter != edited.cycle_meter) ||
            (original.starting_meter != edited.starting_meter) ||
            (!original.starting_date.equals(edited.starting_date)) ){
            
            if(mpm.isPlanned()){
                if(mpm.hasPendingWorkOrders()){
                    throw new Exception("PM " + mpm.PM_ID + " is in one or more pending Jobs.");
                }
                else if(mpm.HAS_DONE_WORK_ORDERS){
                    mpm.deletePlanFrom(mpm.LAST_DONE_DATE);
                    mpm.planFrom(mpm.PLAN_START_DATE,mpm.PLAN_END_DATE,machine.AVERAGE_METER_PER_DAY);
                }
                else{
                    mpm.deleteCompletePlan();
                    mpm.planFrom(mpm.PLAN_START_DATE,mpm.PLAN_END_DATE,machine.AVERAGE_METER_PER_DAY);
                }
            }
            else{
                //Do Nothing
            }
        }   
        else if(original.plan != edited.plan){
            if(mpm.hasPendingWorkOrders()){
                throw new Exception("PM " + mpm.PM_ID + " is in one or more pending Jobs.");
            }
            else if(mpm.HAS_DONE_WORK_ORDERS){
                mpm.deletePlanFrom(mpm.LAST_DONE_DATE);
            }
            else{
                mpm.deleteCompletePlan();
            }
        }
    }
    
    
    private PreventiveMaintenanceRegisterEntry getPMRE(String pm_id){
        int size = REGISTED_PMS_ORIGINALS.size();
        PreventiveMaintenanceRegisterEntry e = null;
        for(int i=0;i<size;i++){
            e = REGISTED_PMS_ORIGINALS.get(i);
            if(e.preventive_maintenance_id.equals(pm_id)){
                return e;
            }
        }
        return null;
    }
 
    private void btnSave_actionPerformed(ActionEvent e) {    
        if(isValidateRegisters()){
            try {
                savePoint = connection.setSavepoint();
                if(register()){
                    connection.commit();
                    messageBar.setMessage("Maintenance registering information saved.","OK");
                    setMode("SEARCH");
                }
                else{
                    connection.rollback(savePoint);
                    messageBar.setMessage("Could not save or no maintenance registering information","ERROR");
                }
            } catch(SQLException f){
                f.printStackTrace();
            }
        }
    }
    
    private void btnCancel_actionPerformed(ActionEvent e) {
        try {
            connection.rollback(savePoint);
        } catch (Exception f) {
            //f.printStackTrace();
        }
        clearAll();
        setMode("LOAD");
    }
    
    private void clearAll(){
        txtBrand.setInputText("");
        txtCategory.setInputText("");
        txtModel.setInputText("");
        txtCustomer.setInputText("");
        txtAsset.setInputText("");
        txtCommisionedDate.setInputText("");
        lstAssets.deleteAll();
        tblRegister.deleteAll();
    }

    private boolean isValidateRegisters(){
        int rows = tblRegister.getRowCount();
        String basis = "";
        String cycleTime = "";
        String cycleMeter = "";
        String startingMeter = "";
        String startingDate = "";
        
        for(int i=0;i<rows;i++){
            basis = tblRegister.getValueAt(i,3).toString();
            cycleTime = tblRegister.getValueAt(i,4).toString();
            cycleMeter = tblRegister.getValueAt(i,5).toString();
            startingMeter = tblRegister.getValueAt(i,10).toString();
            startingDate = tblRegister.getValueAt(i,11).toString();
            
            if(basis.equals("Time Period")){
                if(cycleTime.isEmpty()){
                    messageBar.setMessage("Cycle Time can not be empty at row "+(i+1),"ERROR");
                    return false;
                }
                else if(cycleTime.equals("0-0-0")){
                    messageBar.setMessage("Cycle Time can not be 0-0-0 at row "+(i+1),"ERROR");
                    return false;
                }
            }
            else{
                if(cycleMeter.isEmpty()){
                    messageBar.setMessage("Cycle Meter can not be empty at row "+(i+1),"ERROR");
                    return false;
                }
                else if(!Utilities.isPositive(cycleMeter)){
                    messageBar.setMessage("Invalid Cycle Meter at row "+(i+1),"ERROR");
                    return false;
                }
                else if(startingMeter.isEmpty()){
                    messageBar.setMessage("Starting Meter can not be empty at row "+(i+1),"ERROR");
                    return false;
                }
                else if(!Utilities.isNonNegative(startingMeter)){
                    messageBar.setMessage("Invalid Starting Meter at row "+(i+1),"ERROR");
                    return false;
                }
            }
            
            if(startingDate.equals("0000-00-00")){
                messageBar.setMessage("Invalid Starting Date at row "+(i+1),"ERROR");
                return false;
            }
        }
        //Nothing has went wrong so far, so we return true here;
        return true;
    }
    
    
    private void getRegisteredMaintenance(String asset_id) throws Exception{
        String sql = "SELECT " +
                        "r.Preventive_Maintenance_ID, " +
                        "p.Description, " +
                        "r.Criticalness, " +
                        "r.Basis, " +
                        "r.CycleTime, " +
                        "r.CycleMeter, " +
                        "r.Standard_Time, " +
                        "r.Tolerance, " +
                        "r.StartingMeter, " +
                        "IF(r.StartingDate='0000-00-00','0000-00-00',StartingDate) AS StartingDate, " +
                        "r.Plan " +
                    "FROM " +
                        "preventive_maintenance_register r, " +
                        "preventive_maintenance p " +
                    "WHERE " +
                        "r.Preventive_Maintenance_ID = p.id AND r.asset_id=?";
     
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,asset_id);
            ResultSet rec = stmt.executeQuery();
            int cnt = 0;
            tblRegister.deleteAll();
            REGISTED_PMS_ORIGINALS = new Vector();
            PreventiveMaintenanceRegisterEntry pmre = null;
            while(rec.next()){
                /**Only plan affecting properties are assigned*/
                pmre = new PreventiveMaintenanceRegisterEntry(null);
                pmre.asset_id = txtAsset.getInputText();
                pmre.preventive_maintenance_id = rec.getString("Preventive_Maintenance_ID");
                pmre.basis = rec.getString("Basis");
                pmre.cycle_time = rec.getString("CycleTime");
                pmre.cycle_meter = rec.getInt("CycleMeter");
                pmre.starting_meter = rec.getInt("StartingMeter");
                pmre.starting_date = rec.getString("StartingDate");
                pmre.plan = rec.getBoolean("Plan");
                
                /**Original registering values are kept in this Vector*/
                REGISTED_PMS_ORIGINALS.add(pmre);
                
                /**Showable and editable table*/
                tblRegister.addRow();
                cnt = rec.getRow()-1;
             
                tblRegister.setValueAt(pmre.preventive_maintenance_id,cnt,0);
                tblRegister.setValueAt(rec.getString("Description"),cnt,1);
                tblRegister.setValueAt(rec.getString("Criticalness"),cnt,2);
                tblRegister.setValueAt(pmre.basis,cnt,3);
                tblRegister.setValueAt(pmre.cycle_time,cnt,4);
                tblRegister.setValueAt(pmre.cycle_meter,cnt,5);
                tblRegister.setValueAt(rec.getString("Standard_Time"),cnt,6);
                tblRegister.setValueAt(rec.getInt("Tolerance"),cnt,7);
                tblRegister.setValueAt(pmre.starting_meter,cnt,10);
                tblRegister.setValueAt(pmre.starting_date,cnt,11);
                tblRegister.setValueAt(pmre.plan,cnt,12);
            }
            tblRegister.repaint();
        
        if(tblRegister.getRowCount()==0){
            messageBar.setMessage("There are no pms registered for this machine","WARN");
        }
    }

    private class PreventiveMaintenanceMasteListTable extends JTable {
        private PreventiveMaintenanceMasterListTableModel myModel = null ;
        
        public PreventiveMaintenanceMasteListTable() {
            myModel = new PreventiveMaintenanceMasterListTableModel();
            this.setModel(myModel);
            this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.getTableHeader().setReorderingAllowed(false);
            
            this.getColumnModel().getColumn(0).setPreferredWidth(80);
            this.getColumnModel().getColumn(1).setPreferredWidth(60);
            this.getColumnModel().getColumn(2).setPreferredWidth(250);
            this.getColumnModel().getColumn(3).setPreferredWidth(80);
            this.setSelectionMode(0);
            this.setAutoCreateRowSorter(true);
        }
        
        public void populate(){
            try{
                Statement stmt = pmMasterConnection.createStatement();
                ResultSet rec = stmt.executeQuery("SELECT maintenance_category, id, description, PM_Type FROM preventive_maintenance ORDER BY PM_Type, maintenance_category, id");
                this.deleteAll();
              
                while(rec.next()){
                    this.addRow();
                    int row = rec.getRow() - 1;
                    
                    String cate = rec.getString("Maintenance_Category");
                    String id = rec.getString("ID");
                    String desc = rec.getString("Description");
                    String type = rec.getString("PM_Type");
                    
                    this.setValueAt(cate,row,0);
                    this.setValueAt(id,row,1);
                    this.setValueAt(desc,row,2);
                    this.setValueAt(type,row,3);
                }
        
            }
            catch(Exception er){
                er.printStackTrace();
            }
        }
              
        public void addRow(){
            myModel.addRow();
            this.tableChanged(new TableModelEvent(myModel)); 
        }        
        
        public void deleteRow(int i){
            if(i>-1){
                myModel.deleteRow(i);
            }
            this.tableChanged(new TableModelEvent(myModel));        
        } 
               
        public void deleteAll(){
            myModel.deleteAll();
            this.tableChanged(new TableModelEvent(myModel));        
        }
            
        private class PreventiveMaintenanceMasterListTableModel extends AbstractTableModel{
            private int rowCount = 0;
            private String [] colNames = {"Category","ID","Description","Type"};
            private Object [][] valueArray = null;
            private Object [][] tempArray = null;
       
            public PreventiveMaintenanceMasterListTableModel(){
                valueArray = new Object[rowCount][colNames.length];
            }
                      
            public String getColumnName(int c){ 
                return colNames[c];  
            }
           
            public int getColumnCount(){  
                return colNames.length;
            }  
            
            public int getRowCount(){  
                return valueArray.length;
            }
            
            public Object getValueAt(int r, int c){  
                return valueArray[r][c];
            }
            
            public void setValueAt(Object value,int row,int col){
                valueArray[row][col] = value;
            }
            
            public boolean isCellEditable(int r,int c){
                return false;
            }
            
            public void addRow(){
                tempArray = new Object[this.getRowCount()+1][this.getColumnCount()];
            
                for(int y=0 ; y<valueArray.length; y++){
                    for(int x=0; x<valueArray[0].length; x++){
                        tempArray[y][x] =  valueArray[y][x];
                    }
                }
                valueArray = tempArray;
            }

            public void deleteRow(int i){
                int arrayIndex = i; 
                tempArray = new Object[this.getRowCount()-1][this.getColumnCount()];
                for(int y=0 ; y<arrayIndex; y++){
                    for(int x=0; x<tempArray[0].length; x++){
                        tempArray[y][x] =  valueArray[y][x];
                    }
                }
                for(int y=arrayIndex+1 ; y<valueArray.length; y++){
                    for(int x=0; x<tempArray[0].length; x++){
                        tempArray[y-1][x] =  valueArray[y][x];
                    }
                }            
                valueArray = tempArray;
            }
            
            public void deleteAll(){
                valueArray = new Object[0][colNames.length];
            }
        }
    }

    public class RegisterTable extends JTable {
        private RegisterTableModel myModel = null ;
        private Connection registerTableConnection;
          
        public RegisterTable(Connection con){
            registerTableConnection = con;
            myModel = new RegisterTableModel();
            this.setModel(myModel);
            this.setSelectionMode(0);
            this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.getTableHeader().setReorderingAllowed(false);
            
            
            this.getColumnModel().getColumn(0).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(1).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(4).setCellRenderer(new CycleTimeCellRenderer());
            this.getColumnModel().getColumn(5).setCellRenderer(new MeterReadingCellRenderer());
            this.getColumnModel().getColumn(10).setCellRenderer(new MeterReadingCellRenderer());
            
            this.getColumnModel().getColumn(8).setCellRenderer(new ButtonCellRenderer());
            this.getColumnModel().getColumn(9).setCellRenderer(new ButtonCellRenderer());
            this.getColumnModel().getColumn(12).setCellRenderer(new PlanCheckBoxCellRenderer());
           
            
            this.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new CriticalnessCombo()));
            this.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new MaintenanceTypeCombo()));
            this.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new TimePeriodCell()));
            this.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(new StandardTimeCell(frame))); 
            this.getColumnModel().getColumn(11).setCellEditor(new DefaultCellEditor(new DateCell(frame))); 
            
            this.getColumnModel().getColumn(0).setPreferredWidth(50);
            this.getColumnModel().getColumn(1).setPreferredWidth(250);
            this.getColumnModel().getColumn(2).setPreferredWidth(65);
            this.getColumnModel().getColumn(3).setPreferredWidth(50);
            this.getColumnModel().getColumn(4).setPreferredWidth(60);
            this.getColumnModel().getColumn(5).setPreferredWidth(60);
            this.getColumnModel().getColumn(6).setPreferredWidth(65);
            this.getColumnModel().getColumn(7).setPreferredWidth(50);
            this.getColumnModel().getColumn(8).setPreferredWidth(40);
            this.getColumnModel().getColumn(9).setPreferredWidth(40);
            this.getColumnModel().getColumn(10).setPreferredWidth(80);
            this.getColumnModel().getColumn(11).setPreferredWidth(80);
            this.getColumnModel().getColumn(12).setPreferredWidth(30);
        }
       
        public void addRow(String pm_id, String desc, String starting_date){
            myModel.addRow(pm_id,desc,starting_date);
            this.tableChanged(new TableModelEvent(myModel)); 
        }
        
        public void addRow(){
            myModel.addRow();
            this.tableChanged(new TableModelEvent(myModel)); 
        }
        
        public void deleteRow(int i){
            if(i>-1){
                myModel.deleteRow(i);
            }
            this.tableChanged(new TableModelEvent(myModel));        
        }
    
        public void deleteAll(){
            myModel.deleteAll();
            this.tableChanged(new TableModelEvent(myModel));        
        }
        
        private class RegisterTableModel extends AbstractTableModel{
             private int rowCount = 0;
             private String [] colNames = {
                                "ID",                       //0 
                                "Descritpion",              //1
                                "Criticalness",             //2
                                "Basis",                    //3
                                "Cycle Time",               //4
                                "Cycle Meter",              //5
                                "Std: Time",                //6
                                "Tolerance",                //7
                                "Labour",                   //8
                                "Part",                     //9
                                "Starting Meter",           //10
                                "Starting Date",            //11     
                                "Plan"                      //12
                                };
             private Object [][] valueArray = null;
             private Object [][] tempArray = null;
 
            public RegisterTableModel() {
                valueArray = new Object[rowCount][colNames.length];
            }
           
            public String getColumnName(int c){ 
                return colNames[c];  
            }
            
            public int getColumnCount(){  
                return colNames.length;
            }  
        
            public int getRowCount(){  
                return valueArray.length;
            }
             
            public Object getValueAt(int r, int c){
                if(valueArray[r][c]==null)
                    return "";
                else
                    return valueArray[r][c];
            }
             
            public void setValueAt(Object value,int row,int col){
                valueArray[row][col] = value;
            }
                 
            public boolean isCellEditable(int r,int c){
                String basis = valueArray[r][3]+"";
                boolean planning = Boolean.parseBoolean(valueArray[r][12].toString());
                
                if(c==12){
                    return true;
                }
                else{
                    if(planning){
                        if( c==2 || c==3 || c==6 || c==7 || c==11){
                            return true;
                        }
                        else if( c==4 && basis.equals(MaintenanceTypeCombo.TIME_PERIOD) ){
                            return true;
                        }
                        else if( c==5 && basis.equals(MaintenanceTypeCombo.METER_READING) ){
                            return true;
                        }
                        else if( c==10 && basis.equals(MaintenanceTypeCombo.METER_READING) ){
                            return true;
                        }
                        else{
                            return false;
                        }
                    }
                    else{
                        return false;
                    }
                }
            }
             
            public Class getColumnClass(int c){
                return this.getValueAt(0,c).getClass();
            }
            
            public void addRow(){
                tempArray = new Object[this.getRowCount()+1][this.getColumnCount()];
            
                for(int y=0 ; y<valueArray.length; y++){
                    for(int x=0; x<valueArray[0].length; x++){
                        tempArray[y][x] =  valueArray[y][x];
                    }
                }
                valueArray = tempArray;
            } 
             
            public void addRow(String pm_id, String desc, String strating_date){
                tempArray = new Object[this.getRowCount()+1][this.getColumnCount()];
            
                for(int y=0 ; y<valueArray.length; y++){
                    for(int x=0; x<valueArray[0].length; x++){
                        tempArray[y][x] =  valueArray[y][x];
                    }
                }
                //Initial Value
                tempArray[tempArray.length-1][0] = pm_id;
                tempArray[tempArray.length-1][1] = desc;
                tempArray[tempArray.length-1][2] = "Moderate" ;
                tempArray[tempArray.length-1][3] = MaintenanceTypeCombo.TIME_PERIOD ;
                tempArray[tempArray.length-1][4] = "0-0-0" ;
                tempArray[tempArray.length-1][5] = 0 ;
                tempArray[tempArray.length-1][6] = "0-0-0" ;
                tempArray[tempArray.length-1][7] = 0 ;
                tempArray[tempArray.length-1][10] = 0 ;
                tempArray[tempArray.length-1][11] = strating_date;
                tempArray[tempArray.length-1][12] = Boolean.TRUE;
                
                valueArray = tempArray;
            }
          
            public void deleteRow(int i){
                int arrayIndex = i; 
                tempArray = new Object[this.getRowCount()-1][this.getColumnCount()];
                for(int y=0 ; y<arrayIndex; y++){
                    for(int x=0; x<tempArray[0].length; x++){
                        tempArray[y][x] =  valueArray[y][x];
                    }
                }
                for(int y=arrayIndex+1 ; y<valueArray.length; y++){
                    for(int x=0; x<tempArray[0].length; x++){
                        tempArray[y-1][x] =  valueArray[y][x];
                    }
                }            
                valueArray = tempArray;
            }
        
            public void deleteAll(){
                valueArray = new Object[0][colNames.length];
            }
        } 
   
        class CriticalnessCombo extends JComboBox{
            public CriticalnessCombo(){
                addItem("High");
                addItem("Moderate");
                addItem("Low");
            }
        }
        
        class MaintenanceTypeCombo extends JComboBox implements ItemListener{
        
            public static final String TIME_PERIOD = "Time Period";
            public static final String METER_READING = "Meter Reading";
            
            public MaintenanceTypeCombo(){
                addItem(TIME_PERIOD);
                addItem(METER_READING);
                addItemListener(this);
            }
                        
            public void itemStateChanged(ItemEvent e) {
                int row = tblRegister.getSelectedRow();
                if(row!=-1){
                    String value = getSelectedItem().toString();
                    if(value.equals(TIME_PERIOD)){
                        setValueAt("0",row,5);
                        setValueAt("0",row,10);
                    }
                    else{
                        setValueAt("",row,4);
                    }
                }
                repaint();
            }
        }
    
        class TimePeriodCell extends JTextField implements FocusListener, ActionListener{
            private TimePeriodDialog cdlg;
            private JButton btn;
   
            public TimePeriodCell(){
                super();
                setEditable(false);
                setLayout(null);
                btn = new JButton(new ImageIcon(ImageLibrary.UTIL_ALERT));
                btn.setSize(17,17);
                btn.setVisible(false);
                btn.addActionListener(this);
                add(btn);
                cdlg = new TimePeriodDialog(frame);
                addFocusListener(this);
            }
        
            public void actionPerformed(ActionEvent e){
                if(btn==e.getSource()){
                    cdlg.setLocationRelativeTo(btn);
                    cdlg.setVisible(true,getText()); 
                    String rtnVal = cdlg.getStringValue();
                    String curVal = getText();
                    setText( (rtnVal.equals("") ? curVal : rtnVal));
                }
            }
        
            public void focusLost(FocusEvent fe){
                btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
     
            public void focusGained(FocusEvent fe){
                int w = getWidth();
                btn.setLocation(w-17,0);
                btn.setVisible(true);
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        }
        
        public class CycleTimeCellRenderer extends JLabel implements TableCellRenderer{
            public CycleTimeCellRenderer(){
                setOpaque(true);
            }

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
                this.setText(value.toString());
                if(isSelected){
                    this.setBackground(table.getSelectionBackground());
                    this.setForeground(table.getSelectionForeground());
                }
                else if(!table.getValueAt(row,3).toString().equals(MaintenanceTypeCombo.TIME_PERIOD)){
                    this.setBackground(Color.LIGHT_GRAY);
                    this.setForeground(table.getForeground());
                }
                else{
                    this.setBackground(table.getBackground());
                    this.setForeground(table.getForeground());
                }
                 
                return this;
            }
        }
        
        private class PlanCheckBoxCellRenderer extends JCheckBox implements TableCellRenderer{
            public PlanCheckBoxCellRenderer(){
                setOpaque(true);
                setHorizontalAlignment(JCheckBox.CENTER);
            }

            public Component getTableCellRendererComponent(JTable table, 
                                                                  Object value, 
                                                                  boolean isSelected, 
                                                                  boolean hasFocus, 
                                                                  int row, 
                                                                  int column) {
                this.setSelected(Boolean.parseBoolean(value.toString()));
                if(isSelected){
                    this.setBackground(table.getSelectionBackground());
                    this.setForeground(table.getSelectionForeground());
                }
                else{
                    this.setBackground((Boolean.parseBoolean(value.toString())?Color.GREEN:Color.RED));
                    this.setForeground(table.getForeground());
                }         
                return this;
            }
        }
        
        public class MeterReadingCellRenderer extends JLabel implements TableCellRenderer{
            public MeterReadingCellRenderer(){
                setOpaque(true);
            }

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
                this.setText(value.toString());
                if(isSelected){
                    this.setBackground(table.getSelectionBackground());
                    this.setForeground(table.getSelectionForeground());
                }
                else if(!table.getValueAt(row,3).toString().equals(MaintenanceTypeCombo.METER_READING)){
                    this.setBackground(Color.LIGHT_GRAY);
                    this.setForeground(table.getForeground());
                }
                else{
                    this.setBackground(table.getBackground());
                    this.setForeground(table.getForeground());
                }
                return this;
            }
        }
        
    }
    
    public class NewAssetCellRenderer extends JLabel implements TableCellRenderer{
        public NewAssetCellRenderer(){
            setOpaque(true);
            setIcon(new ImageIcon(ImageLibrary.TREE_ASSET));
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
            this.setText(value.toString());
            if(isSelected){
                this.setBackground(table.getSelectionBackground());
                this.setForeground(table.getSelectionForeground());
            }
            else if(isNewAsset(value.toString())){
                this.setBackground(table.getBackground());
                this.setForeground(Color.RED);
            }
            else{
                this.setBackground(table.getBackground());
                this.setForeground(table.getForeground());
            }
            return this;
        }
        
        private boolean isNewAsset(String asset){
            String sql = "SELECT Preventive_Maintenance_ID FROM preventive_maintenance_register WHERE Asset_ID ='"+asset+"'";
            try{
                ResultSet rec = connection.createStatement().executeQuery(sql);
                rec.next();
                return (!rec.getString(1).equals("")?false:true);
            }
            catch (Exception ex)  {
                //ex.printStackTrace();
                return true;
            } 
        }
    }
}
