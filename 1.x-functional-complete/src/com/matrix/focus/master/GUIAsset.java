package com.matrix.focus.master;

import com.matrix.focus.util.table.Table;
import com.matrix.components.MDataCombo;
import com.matrix.components.MDatebox;
import com.matrix.components.MTextbox;
import com.matrix.components.TitleBar;
import com.matrix.focus.util.MPanel;
import com.matrix.focus.mdi.MDI;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.planner.PlanningAsset;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.DataAssistantDialog;
import com.matrix.focus.util.ImageLibrary;
import com.matrix.focus.util.LockedLabelCellRenderer;
import com.matrix.focus.util.table.TableItem;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

public class GUIAsset extends MPanel{

    private JPanel panelAssetSelection = new JPanel();
    private JScrollPane scrollPaneAssetList = new JScrollPane();
    private MTextbox textFieldCustomer = new MTextbox();
    private MTextbox textFieldBrand = new MTextbox();
    private MTextbox textFieldCategory = new MTextbox();
    private MTextbox textFieldModel = new MTextbox();
    private JButton buttonBrand = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton buttonCategory = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton buttonModel = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton buttonCustomer = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private MTextbox textFieldAsset = new MTextbox();
    private JButton buttonAsset = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private AssetTableItem assetTableItem;  
    private Table<AssetTableItem> tableAssets ; 
   
    private String SELECTED_CATEGORY_ID = "";
    private String SELECTED_MODEL_ID = "";
    private String SELECTED_CUSTOMER_ID = "";
    /////////////////////////////////////////////////////
    private boolean reSchedule=false;
    private Connection connection;
    private MDI frame;
    private MessageBar messageBar;
    private TitleBar titlebar = new TitleBar();
    private JPanel jPanel1 = new JPanel();
    private MTextbox txtAssetID = new MTextbox();
    private MTextbox txtCategory = new MTextbox();
    private MTextbox txtModel = new MTextbox();
    private MTextbox txtDivis = new MTextbox();
    private MTextbox txtBrand = new MTextbox();
    private MTextbox txtEmp = new MTextbox();
    private MTextbox mTextbox11 = new MTextbox();
    private MDatebox dtManuf = new MDatebox();
    private MDatebox dtPurch = new MDatebox();
    private MDatebox dtWarra = new MDatebox();
    private JTabbedPane jTabbedPane1 = new JTabbedPane();
    private JPanel jPanel2 = new JPanel();
    private JPanel jPanel3 = new JPanel();
    private JPanel jPanel5 = new JPanel();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JTextArea txtDesc = new JTextArea();
    private JPanel jPanel6 = new JPanel();
    private JButton btnCancel = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
    private JButton btnSave = new JButton(new ImageIcon(ImageLibrary.BUTTON_SAVE));
    private JButton btnEdit = new JButton(new ImageIcon(ImageLibrary.BUTTON_EDIT));
    private JButton btnDelete = new JButton(new ImageIcon(ImageLibrary.BUTTON_DELETE));
    private JButton btnAdd = new JButton(new ImageIcon(ImageLibrary.BUTTON_NEW));
    private MDatebox dtComm = new MDatebox();
    private Asset asset ;
    private JButton btnCategory = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton btnModel = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton btnBrand = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton btnDivi = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton btnEmp = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private MDataCombo cmbWPeriod = new MDataCombo();
    private boolean ADD_NEW = false;
    private AssetImage lblImage = new AssetImage();
    private JButton btnClearImage = new JButton(new ImageIcon(ImageLibrary.BUTTON_DELETE));
    private JButton btnSaveImage = new JButton(new ImageIcon(ImageLibrary.BUTTON_SAVE));
    private JButton btnLoadImage = new JButton(new ImageIcon(ImageLibrary.BUTTON_OPEN));
    private JPanel jPanel7 = new JPanel();
    private JScrollPane jScrollPane2 = new JScrollPane();
    private PropertiesTable tblProperties = new PropertiesTable();
    private JButton btnAddPro = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));
    private JButton btnRemovePro = new JButton(new ImageIcon(ImageLibrary.BUTTON_DELETE));
    private MTextbox txtInsurance_Reference_No = new MTextbox();
    private MDatebox dtInsurance_Expiry_Date = new MDatebox();
    private JPopupMenu popupMenu = new JPopupMenu();;
    private JMenuItem menuItemSelectAsset = new JMenuItem();
    private JMenuItem menuItemRelateAsset = new JMenuItem();
    private boolean AN_ASSET_HAS_BEEN_SELECTED = false;
    private String SELECTED_ASSET = "";
    private JButton btnNewCat = new JButton(new ImageIcon(ImageLibrary.BUTTON_NEW));
    private JButton btnNewModel = new JButton(new ImageIcon(ImageLibrary.BUTTON_NEW));
    private JButton btnNewDivi = new JButton(new ImageIcon(ImageLibrary.BUTTON_NEW));
    private MTextbox txtCustomerName = new MTextbox();
    private MTextbox textFieldTelephoneNo = new MTextbox();
    private JTextArea textAreaAddress = new JTextArea();
    private JLabel jLabel1 = new JLabel();
    private MTextbox textFieldFaxNo = new MTextbox();
    private MTextbox textFieldEmail = new MTextbox();
    private JScrollPane scrollPaneAddress = new JScrollPane();
    private JPanel jPanel4 = new JPanel();


    public GUIAsset(DBConnectionPool pool, MDI frame, MessageBar msgBar){
        connection = pool.getConnection();
        this.frame = frame;
        this.messageBar = msgBar;
        tableAssets = new Table<AssetTableItem>(new String[]{"Machine","Model","Customer ID","Customer Name","Address"});
        
        try {
        
            titlebar.setTitle("Sales");
            titlebar.setDescription("The facility to manage sales/machine information.");
            titlebar.setImage(ImageLibrary.TITLE_ASSET);            
            
            popupMenu.add(menuItemSelectAsset);
            popupMenu.add(menuItemRelateAsset);
            jbInit();
            fill_cmbWPeriod();
            setMode("LOAD");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
       
    private void btnNewCat_actionPerformed(ActionEvent e) {
        e.toString();
        frame.menuItemGUIAssetCategory.doClick();
    }

    private void btnNewModel_actionPerformed(ActionEvent e) {
        e.toString();
        frame.menuItemAssetModel.doClick();
    }

    private void btnNewDivi_actionPerformed(ActionEvent e) { 
        e.toString();
        frame.menuItemGUICustomer.doClick();
    }
    
    private void setAsset(String asset_id){
        asset = new Asset(connection,asset_id);
        /**General*/
        txtAssetID.setInputText(asset.asset);
        txtCategory.setInputText(asset.category);
        txtModel.setInputText(asset.model);
        txtBrand.setInputText(asset.brand);
        dtManuf.setInputText(asset.date_of_manufacture.trim());
        dtPurch.setInputText(asset.Date_of_Sale.trim());
        dtWarra.setInputText(asset.warranty_start_date.trim());
        cmbWPeriod.setSelectedItem(asset.warranty_period);
        txtInsurance_Reference_No.setInputText(asset.insurance_reference_no);
        dtInsurance_Expiry_Date.setInputText(asset.insurance_expiry_date);
        txtDesc.setText(asset.description);
        /**Administrative*/
    
        txtDivis.setInputText(asset.division);
        txtCustomerName.setInputText(asset.getDivisionName());
        textAreaAddress.setText(asset.address);
        textFieldTelephoneNo.setInputText(asset.telephoneNo);
        textFieldFaxNo.setInputText(asset.faxNo);
        textFieldEmail.setInputText(asset.email);
        txtEmp.setInputText(asset.Contact_Person);
        /**Maintenance*/
        dtComm.setInputText(asset.commission_date.trim());
        
        /**Properties*/
        tblProperties.populate(asset.asset);
        
        
        /**Image*/
         lblImage.setImageToView("./Asset_Images/" + asset.asset + ".jpg");
         
         
         //System.out.println("guiasset set asset "+asset.company);
    }
    
    private void setMode(String mode){
    
        if(mode.equals("EDIT")){
            ADD_NEW = false;
            btnDelete.setEnabled(true);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
            btnAdd.setEnabled(false);
            btnEdit.setEnabled(false);
            setEditable(true);
            txtAssetID.setEditable(false);
        }
        else if(mode.equals("ADD")){
            ADD_NEW = true;
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
            btnAdd.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            clearAll();
            setEditable(true);
        }
        else if(mode.equals("LOAD")){
            ADD_NEW = false;
            btnAdd.setEnabled(true);
            btnDelete.setEnabled(false);
            btnEdit.setEnabled(false);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(false);
            setEditable(false);
            clearAll();
        }
        else if(mode.equals("SEARCH")){
            ADD_NEW = false;
            btnEdit.setEnabled(true);
            btnDelete.setEnabled(true);
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
            btnAdd.setEnabled(false);
            setEditable(false);
        }
    }
    
    private void setEditable(boolean flag){
        /**General*/
        txtAssetID.setEditable(flag);
       // txtCategory.setEditable(flag);
      //  txtModel.setEditable(flag);
        txtBrand.setEditable(flag);
        txtInsurance_Reference_No.setEditable(flag);
        dtManuf.getButton().setEnabled(flag);
        dtPurch.getButton().setEnabled(flag);
        dtWarra.getButton().setEnabled(flag);
        dtInsurance_Expiry_Date.getButton().setEnabled(flag);
        txtDesc.setEditable(flag);
        cmbWPeriod.getComboBox().setEnabled(flag);
        /**Administrative*/
       
     //   txtDivis.setEditable(flag);
      //  txtEmp.setEditable(flag);
        /**Maintenance*/
        dtComm.getButton().setEnabled(flag);
     
        /**Properties*/
        tblProperties.setEnabled(flag);
      
        /**Buttons*/
        
        btnBrand.setEnabled(flag);
        btnCategory.setEnabled(flag);
        btnDivi.setEnabled(flag);
        btnEmp.setEnabled(flag);
        btnModel.setEnabled(flag);
        btnAddPro.setEnabled(flag);
        btnRemovePro.setEnabled(flag);
        btnLoadImage.setEnabled(flag);
        btnSaveImage.setEnabled(flag);
        btnClearImage.setEnabled(flag);
        btnNewCat.setEnabled(flag);
        btnNewModel.setEnabled(flag);
        btnNewDivi.setEnabled(flag);
        menuItemRelateAsset.setEnabled(flag);
    }
    
    private void clearAll(){
        /**General*/
        txtAssetID.setInputText("");
        txtCategory.setInputText("");
        txtModel.setInputText("");
        txtBrand.setInputText("");
        dtManuf.setInputText("");
        dtPurch.setInputText("");
        dtWarra.setInputText("");
        dtInsurance_Expiry_Date.setInputText("");
        txtInsurance_Reference_No.setInputText("");
        cmbWPeriod.getComboBox().setSelectedIndex(0);
        txtDesc.setText("");
        /**Administrative*/
       
        txtDivis.setInputText("");
        txtCustomerName.setInputText("");
        textAreaAddress.setText("");
        textFieldTelephoneNo.setInputText("");
        textFieldFaxNo.setInputText("");
        textFieldEmail.setInputText("");
        txtEmp.setInputText("");
        /**Maintenance*/
        dtComm.setInputText("");
        
        /**Images*/
         lblImage.setImageToView("");
         tblProperties.deleteAll();
         
         
         /**/
         textFieldCustomer.setInputText("");
         textFieldBrand.setInputText("");
         textFieldCategory.setInputText("");
         textFieldModel.setInputText("");
         textFieldAsset.setInputText("");
         
         tableAssets.removeAll();
        
    }
    
    private void btnCancel_actionPerformed(ActionEvent e) {
        e.toString();
        txtAssetID.setTxtBackColor(Color.WHITE);;
        SELECTED_ASSET = "";
        AN_ASSET_HAS_BEEN_SELECTED = false;
        messageBar.clearMessage();
        asset = null;
        setMode("LOAD");
        
    }
    
    private boolean validateFields(){
    
        if(txtAssetID.getInputText().trim().equals("")){
            messageBar.setMessage("Asset ID field is empty","ERROR");
            return false;
        }
        else if(txtBrand.getInputText().trim().equals("")){
            messageBar.setMessage("Brand field is empty","ERROR");
            return false;
        }
        else if(txtCategory.getInputText().trim().equals("")){
            messageBar.setMessage("Category field is empty","ERROR");
            return false;
        }
        else if(txtModel.getInputText().trim().equals("")){
            messageBar.setMessage("Model field is empty","ERROR");
            return false;
        }
        else if(dtPurch.getInputText().trim().equals("")){
            messageBar.setMessage("Date of Purchase field is empty","ERROR");
            return false;
        }
        else if(txtDivis.getInputText().trim().equals("")){
            messageBar.setMessage("Customer ID field is empty","ERROR");
            return false;
        }
        
        
        try {
       
            if(!dtComm.getInputText().equals(asset.commission_date)){
                PlanningAsset pAsset = new PlanningAsset(txtAssetID.getInputText().trim(),connection);
                if(pAsset.hasPlannedPMs()){
                    if(pAsset.hasDoneWorkOrders()){
                        throw new Exception("Cannot change commissioned date. This machine has completed PMs");
                    }
                    else{
                        reSchedule = true;
                    }
                }
            }
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return true;
    }
    
    private void btnSave_actionPerformed(ActionEvent e){
        e.toString();
        if(validateFields()){
            if(saveAsset()){
                try {
                    PlanningAsset pA = new PlanningAsset(asset.asset,connection);
                    pA.reschedule();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                if(updateProperties()){
                    messageBar.setMessage("Asset information saved","OK");
                    setMode("SEARCH");
                    
                }
                else{
                    messageBar.setMessage("Error while saving asset properties","ERROR");
                }
            }
            else{
                messageBar.setMessage("Error while saving asset information","ERROR");
            }
        }
        
    }
    
    private boolean saveAsset(){
    
        
        
        asset = new Asset(connection);
        asset.asset = txtAssetID.getInputText();
        asset.model = txtModel.getInputText();
        asset.brand = txtBrand.getInputText();
        asset.date_of_manufacture = dtManuf.getInputText().trim().equals("") ? "0000-00-00":dtManuf.getInputText().trim();
        asset.Date_of_Sale = dtPurch.getInputText();
        asset.warranty_start_date = dtWarra.getInputText().trim().equals("") ? "0000-00-00":dtWarra.getInputText().trim();
        asset.warranty_period = cmbWPeriod.getSelectedItem().toString().trim().equals("") ? "0":cmbWPeriod.getSelectedItem().toString();
        asset.company="Matrix";
        asset.department = "Customers";
        asset.description = txtDesc.getText();
        asset.division = txtDivis.getInputText();
        asset.Contact_Person = txtEmp.getInputText();
        asset.commission_date = dtComm.getInputText().trim().equals("") ? "0000-00-00":dtComm.getInputText().trim();
        asset.insurance_reference_no = txtInsurance_Reference_No.getInputText().trim().equals("") ? "0":txtInsurance_Reference_No.getInputText().trim();
        asset.insurance_expiry_date = dtInsurance_Expiry_Date.getInputText().trim().equals("") ? "0000-00-00":dtInsurance_Expiry_Date.getInputText().trim();
        asset.planned = "false";
        
        
        
        if(ADD_NEW){
            if(asset.save()){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            if(asset.update()){
                return true;
            }
            else{
                return false;
            }
        }
        
        
    }
    
    private void jbInit() throws Exception {
        this.setLayout(null);
        this.setSize(new Dimension(1028, 614));
        titlebar.setBounds(new Rectangle(10, 10, 965, 70));
        jPanel1.setBounds(new Rectangle(455, 85, 540, 470));
        jPanel1.setBorder(BorderFactory.createTitledBorder("Machine Information "));
        jPanel1.setLayout(null);
        txtAssetID.setBounds(new Rectangle(25, 25, 270, 20));
        txtAssetID.setCaption("Machine ID");
        txtAssetID.setLblWidth(120);
        txtAssetID.setTxtWidth(150);
        txtAssetID.setEditable(false);
        txtCategory.setBounds(new Rectangle(15, 35, 270, 20));
        txtCategory.setCaption("Category");
        txtCategory.setLblWidth(120);
        txtCategory.setTxtWidth(150);
        txtCategory.setEditable(false);
        txtModel.setBounds(new Rectangle(15, 60, 320, 20));
        txtModel.setCaption("Model");
        txtModel.setLblWidth(120);
        txtModel.setEditable(false);
        txtDivis.setBounds(new Rectangle(15, 10, 270, 20));
        txtDivis.setCaption("Customer ID");
        txtDivis.setLblWidth(120);
        txtDivis.setTxtWidth(150);
        txtDivis.setEditable(false);
        txtBrand.setBounds(new Rectangle(15, 10, 320, 20));
        txtBrand.setCaption("Brand");
        txtBrand.setLblWidth(120);
        txtBrand.setEditable(false);
        txtEmp.setBounds(new Rectangle(15, 215, 270, 20));
        txtEmp.setCaption("Responsible Person");
        txtEmp.setLblWidth(120);
        txtEmp.setTxtWidth(150);
        txtEmp.setEditable(false);
        mTextbox11.setBounds(new Rectangle(795, 85, 1, 1));
        dtManuf.setBounds(new Rectangle(15, 135, 205, 20));
        dtManuf.setCaption("Date of Manufacture");
        dtManuf.setLblWidth(120);
        dtPurch.setBounds(new Rectangle(15, 85, 205, 20));
        dtPurch.setCaption("Date of Purchase");
        dtPurch.setLblWidth(120);
        dtWarra.setBounds(new Rectangle(15, 160, 205, 20));
        dtWarra.setCaption("Warranty Start Date");
        dtWarra.setLblWidth(120);
        jTabbedPane1.setBounds(new Rectangle(10, 50, 520, 410));
        jPanel2.setLayout(null);
        jPanel3.setLayout(null);
        jPanel5.setBounds(new Rectangle(10, 260, 500, 115));
        jPanel5.setLayout(null);
        jPanel5.setBorder(BorderFactory.createTitledBorder("Description"));
        jScrollPane1.setBounds(new Rectangle(10, 20, 480, 85));
        txtDesc.setEditable(false);
        jPanel6.setLayout(null);
        btnCancel.setText("Cancel");
        btnCancel.setBounds(new Rectangle(440, 560, 100, 25));
        btnCancel.setSize(new Dimension(100, 25));
        btnCancel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnCancel_actionPerformed(e);
            }
        });
        btnSave.setText("Save");
        btnSave.setBounds(new Rectangle(335, 560, 100, 25));
        btnSave.setSize(new Dimension(100, 25));
        btnSave.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnSave_actionPerformed(e);
            }
        });
        btnEdit.setText("Edit");
        btnEdit.setBounds(new Rectangle(230, 560, 100, 25));
        btnEdit.setSize(new Dimension(100, 25));

        btnEdit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnEdit_actionPerformed(e);
                    }
                });
        btnDelete.setText("Delete");
        btnDelete.setBounds(new Rectangle(125, 560, 100, 25));
        btnDelete.setSize(new Dimension(100, 25));

        btnDelete.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnDelete_actionPerformed(e);
                    }
                });
        btnAdd.setText("New");
        btnAdd.setBounds(new Rectangle(20, 560, 100, 25));
        btnAdd.setSize(new Dimension(100, 25));

        btnAdd.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnAdd_actionPerformed(e);
                    }
                });
        dtComm.setBounds(new Rectangle(15, 110, 205, 20));
        dtComm.setCaption("Date of Commission");
        dtComm.setLblWidth(120);
        btnCategory.setBounds(new Rectangle(290, 35, 30, 20));
        btnCategory.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnCategory_actionPerformed(e);
                    }
                });
        btnModel.setBounds(new Rectangle(340, 60, 30, 20));
        btnModel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnModel_actionPerformed(e);
                    }
                });
        btnBrand.setBounds(new Rectangle(340, 10, 30, 20));
        btnBrand.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnBrand_actionPerformed(e);
                    }
                });
        btnDivi.setBounds(new Rectangle(290, 10, 30, 20));
        btnDivi.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnDivi_actionPerformed(e);
                    }
                });
        btnEmp.setBounds(new Rectangle(290, 215, 30, 20));
        btnEmp.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnEmp_actionPerformed(e);
                    }
                });
        cmbWPeriod.setBounds(new Rectangle(15, 185, 170, 20));
        cmbWPeriod.setCaption("Warranty Period");
        cmbWPeriod.setLblWidth(120);
        cmbWPeriod.setCmbWidth(50);
        btnClearImage.setText("Clear");
        btnClearImage.setBounds(new Rectangle(430, 10, 80, 25));
        btnClearImage.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnClearImage_actionPerformed(e);
                    }
                });
        btnSaveImage.setText("Export");
        btnSaveImage.setBounds(new Rectangle(345, 10, 80, 25));
        btnSaveImage.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnSaveImage_actionPerformed(e);
                    }
                });
        btnLoadImage.setText("Select");
        btnLoadImage.setBounds(new Rectangle(260, 10, 80, 25));
        btnLoadImage.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnLoadImage_actionPerformed(e);
                    }
                });
        jPanel7.setLayout(null);
        jScrollPane2.setBounds(new Rectangle(5, 45, 505, 335));
        btnAddPro.setText("Add");
        btnAddPro.setBounds(new Rectangle(345, 10, 80, 25));
        btnAddPro.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnAddPro_actionPerformed(e);
                    }
                });
        btnRemovePro.setText("Remove");
        btnRemovePro.setBounds(new Rectangle(430, 10, 80, 25));
        btnRemovePro.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnRemovePro_actionPerformed(e);
                    }
                });
        txtInsurance_Reference_No.setBounds(new Rectangle(15, 210, 270, 20));
        txtInsurance_Reference_No.setCaption("Insurance Reference No");
        txtInsurance_Reference_No.setLblWidth(120);
        txtInsurance_Reference_No.setTxtWidth(150);
        txtInsurance_Reference_No.setEditable(false);
        dtInsurance_Expiry_Date.setBounds(new Rectangle(15, 235, 205, 20));
        dtInsurance_Expiry_Date.setCaption("Insurance Expiry Date");
        dtInsurance_Expiry_Date.setLblWidth(120);
        btnNewCat.setBounds(new Rectangle(325, 35, 30, 20));
        btnNewCat.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnNewCat_actionPerformed(e);
                    }
                });
        btnNewModel.setBounds(new Rectangle(375, 60, 30, 20));
        btnNewModel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnNewModel_actionPerformed(e);
                    }
                });
        btnNewDivi.setBounds(new Rectangle(325, 10, 30, 20));
        btnNewDivi.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnNewDivi_actionPerformed(e);
                    }
                });
        txtCustomerName.setBounds(new Rectangle(15, 35, 380, 25));
        txtCustomerName.setLblWidth(120);
        txtCustomerName.setTxtWidth(260);
        txtCustomerName.setCaption("Customer Name");
        txtCustomerName.setEditable(false);
        textFieldTelephoneNo.setBounds(new Rectangle(15, 135, 270, 25));
        textFieldTelephoneNo.setTxtWidth(150);
        textFieldTelephoneNo.setLblWidth(120);
        textFieldTelephoneNo.setCaption("Telephone No");
        textFieldTelephoneNo.setEditable(false);
        textAreaAddress.setEditable(false);
        jLabel1.setText("Address");
        jLabel1.setBounds(new Rectangle(15, 60, 110, 20));
        textFieldFaxNo.setBounds(new Rectangle(15, 160, 270, 25));
        textFieldFaxNo.setLblWidth(120);
        textFieldFaxNo.setTxtWidth(150);
        textFieldFaxNo.setCaption("Fax No");
        textFieldFaxNo.setEditable(false);
        textFieldEmail.setBounds(new Rectangle(15, 185, 270, 25));
        textFieldEmail.setTxtWidth(150);
        textFieldEmail.setLblWidth(120);
        textFieldEmail.setCaption("Email");
        textFieldEmail.setEditable(false);
        scrollPaneAddress.setBounds(new Rectangle(135, 60, 260, 70));
        jPanel4.setBounds(new Rectangle(10, 155, 420, 295));
        jPanel4.setLayout(null);
        jPanel4.setBorder(BorderFactory.createTitledBorder("Selected Machines"));
        lblImage.setBounds(new Rectangle(5, 45, 505, 335));
        lblImage.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        jPanel2.add(btnNewModel, null);
        jPanel2.add(btnNewCat, null);
        jPanel2.add(dtInsurance_Expiry_Date, null);
        jPanel2.add(txtInsurance_Reference_No, null);
        jPanel2.add(cmbWPeriod, null);
        jPanel2.add(btnBrand, null);
        jPanel2.add(btnModel, null);
        jPanel2.add(btnCategory, null);
        jScrollPane1.getViewport().add(txtDesc, null);
        jPanel5.add(jScrollPane1, null);
        jPanel2.add(jPanel5, null);
        jPanel2.add(dtWarra, null);
        jPanel2.add(dtPurch, null);
        jPanel2.add(dtManuf, null);
        jPanel2.add(txtBrand, null);
        jPanel2.add(txtModel, null);
        jPanel2.add(txtCategory, null);
        jPanel2.add(dtComm, null);
        scrollPaneAddress.getViewport().add(textAreaAddress, null);
        jPanel3.add(scrollPaneAddress, null);
        jPanel3.add(textFieldEmail, null);
        jPanel3.add(textFieldFaxNo, null);
        jPanel3.add(jLabel1, null);
        jPanel3.add(textFieldTelephoneNo, null);
        jPanel3.add(txtCustomerName, null);
        jPanel3.add(btnNewDivi, null);
        jPanel3.add(btnEmp, null);
        jPanel3.add(btnDivi, null);
        jPanel3.add(txtEmp, null);
        jPanel3.add(txtDivis, null);
        jTabbedPane1.addTab("General", jPanel2);
        jTabbedPane1.addTab("Customer Details", jPanel3);
        jTabbedPane1.addTab("Properties", jPanel7);
        jTabbedPane1.addTab("Image", jPanel6);
        jPanel6.add(lblImage, null);
        jPanel6.add(btnLoadImage, null);
        jPanel6.add(btnSaveImage, null);
        jPanel6.add(btnClearImage, null);
        jPanel7.add(btnRemovePro, null);
        jPanel7.add(btnAddPro, null);
        jScrollPane2.getViewport().add(tblProperties, null);
        jPanel7.add(jScrollPane2, null);
        this.add(mTextbox11, null);
        this.add(jPanel1, null);
        jPanel1.add(jTabbedPane1, null);
        jPanel1.add(txtAssetID, null);
        this.add(titlebar, null);

        this.add(btnCancel, null);
        this.add(btnSave, null);
        this.add(btnEdit, null);
        this.add(btnDelete, null);
        this.add(btnAdd, null);
        this.add(panelAssetSelection, null);
        scrollPaneAssetList.setBounds(new Rectangle(10, 20, 400, 265));
        scrollPaneAssetList.setBackground(Color.white);
        panelAssetSelection.setBounds(new Rectangle(10, 85, 440, 460));
        panelAssetSelection.setLayout(null);
        panelAssetSelection.setBorder(BorderFactory.createTitledBorder("Machines Selection"));
        textFieldCustomer.setBounds(new Rectangle(20, 25, 360, 20));
        textFieldCustomer.setCaption("Customer");
        textFieldCustomer.setLblWidth(60);
        textFieldCustomer.setTxtWidth(305);
        textFieldCustomer.setEditable(false);
        textFieldBrand.setBounds(new Rectangle(20, 50, 210, 20));
        textFieldBrand.setCaption("Brand");
        textFieldBrand.setLblWidth(60);
        textFieldBrand.setEditable(false);
        textFieldBrand.setTxtWidth(240);
        textFieldCategory.setBounds(new Rectangle(20, 75, 210, 20));
        textFieldCategory.setCaption("Category");
        textFieldCategory.setLblWidth(60);
        textFieldCategory.setEditable(false);
        textFieldCategory.setTxtWidth(240);
        textFieldModel.setBounds(new Rectangle(20, 100, 210, 20));
        textFieldModel.setCaption("Model");
        textFieldModel.setLblWidth(60);
        textFieldModel.setEditable(false);

        textFieldModel.setTxtWidth(240);
        tableAssets.getColumnModel().getColumn(0).setCellRenderer(new NewAssetCellRenderer());

        tableAssets.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent me) {
                        int row = tableAssets.getSelectedRow();
                        if (row != -1) {
                            selectAsset(tableAssets.getValueAt(row, 
                                                             0).toString());
                        }

                        if (me.getClickCount() == 2) {
                            setAsset(tableAssets.getValueAt(row, 0).toString());
                            setMode("SEARCH");
                        }


                    }
                });

        tableAssets.addKeyListener(new KeyAdapter() {
                    public void keyReleased(KeyEvent ke) {
                        int row = tableAssets.getSelectedRow();
                        if (row != -1) {
                            selectAsset(tableAssets.getValueAt(row, 
                                                             0).toString());
                        }
                    }
                });


        buttonBrand.setBounds(new Rectangle(325, 50, 30, 20));
        buttonBrand.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        buttonBrand_actionPerformed(e);
                    }
                });
        buttonCategory.setBounds(new Rectangle(325, 75, 30, 20));
        buttonCategory.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        buttonCategory_actionPerformed(e);
                    }
                });
        buttonModel.setBounds(new Rectangle(325, 100, 30, 20));
        buttonModel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        buttonModel_actionPerformed(e);
                    }
                });
        buttonCustomer.setBounds(new Rectangle(390, 25, 30, 20));
        buttonCustomer.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        buttonCustomer_actionPerformed(e);
                    }
                });
        textFieldAsset.setBounds(new Rectangle(20, 125, 210, 20));
        textFieldAsset.setCaption("Machine");
        textFieldAsset.setLblWidth(60);
        textFieldAsset.setEditable(false);
        textFieldAsset.setTxtWidth(240);
        buttonAsset.setBounds(new Rectangle(325, 125, 30, 20));
        buttonAsset.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        buttonAsset_actionPerformed(e);
                    }
                });
        scrollPaneAssetList.getViewport().setBackground(Color.WHITE);
        scrollPaneAssetList.getViewport().add(tableAssets, null);
        jPanel4.add(scrollPaneAssetList, null);
        panelAssetSelection.add(jPanel4, null);
        panelAssetSelection.add(buttonAsset, null);
        panelAssetSelection.add(textFieldAsset, null);
        panelAssetSelection.add(buttonCustomer, null);
        panelAssetSelection.add(textFieldCustomer, null);
        panelAssetSelection.add(buttonModel, null);
        panelAssetSelection.add(buttonBrand, null);
        panelAssetSelection.add(textFieldModel, null);
        panelAssetSelection.add(textFieldCategory, null);
        panelAssetSelection.add(textFieldBrand, null);
        panelAssetSelection.add(buttonCategory, null);
    }

    private void btnDivi_actionPerformed(ActionEvent e){
        e.toString();
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select Division - Data Assistant","SELECT Division_ID as Division, name as Name  FROM division WHERE comp_id ='Matrix' AND Dept_ID ='Customers' AND deleted ='false'",connection);
        d.setLocationRelativeTo(frame);
        d.setVisible(true); 
        String rtnVal = d.getValue();
        if(!rtnVal.isEmpty()){
            txtDivis.setInputText(rtnVal);
            String sql = "SELECT Division_ID as ID, name as Name,Address,Telephone_No, Fax_No, Email, Contact_Person  FROM division WHERE comp_id ='Matrix' AND Dept_ID ='Customers' AND Division_ID=? AND deleted ='false'";
            
            try {
                PreparedStatement pS = connection.prepareStatement(sql);
                pS.setString(1,rtnVal);
                ResultSet r = pS.executeQuery();
                r.first();
                txtCustomerName.setInputText(r.getString("Name"));
                textAreaAddress.setText(r.getString("Address"));
                textFieldTelephoneNo.setInputText(r.getString("Telephone_No"));
                textFieldFaxNo.setInputText(r.getString("Fax_No"));
                textFieldEmail.setInputText(r.getString("Email"));
                txtEmp.setInputText(r.getString("Contact_Person"));
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void btnEmp_actionPerformed(ActionEvent e){
        e.toString();
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select Contact Person - Data Assistant","SELECT Contact_Person FROM division_contacts WHERE division_id ='"+txtDivis.getInputText()+"'",connection);
        d.setSecondColumnWidth(80);
        d.setThirdColumnWidth(150);
        d.setLocationRelativeTo(frame);
        d.setVisible(true); 
        String rtnVal = d.getValue();
        txtEmp.setInputText( (rtnVal.equals("") ? txtEmp.getInputText() : rtnVal));
    }

    private void btnCategory_actionPerformed(ActionEvent e){
        e.toString();
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select Category - Data Assistant","SELECT Category_ID AS Name FROM asset_category WHERE deleted ='false'",connection);
        d.setFirstColumnWidth(300);
        d.setLocationRelativeTo(frame);
        d.setVisible(true); 
        String rtnVal = d.getValue();
        txtCategory.setInputText( (rtnVal.equals("") ? txtCategory.getInputText() : rtnVal));
    }

    private void btnModel_actionPerformed(ActionEvent e){
        e.toString();
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select Model - Data Assistant","SELECT Model_ID AS 'Name' FROM asset_model WHERE Category_ID ='" + txtCategory.getInputText() + "' AND deleted ='false'",connection);
        d.setFirstColumnWidth(300);
        d.setLocationRelativeTo(frame);
        d.setVisible(true); 
        String rtnVal = d.getValue();
        txtModel.setInputText( (rtnVal.equals("") ? txtModel.getInputText() : rtnVal));
    }

    private void btnBrand_actionPerformed(ActionEvent e){
        e.toString();
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select Brand - Data Assistant","SELECT DISTINCT Brand FROM asset WHERE deleted ='false'",connection);
        d.setFirstColumnWidth(300);
        d.setLocationRelativeTo(frame);
        d.setVisible(true); 
        String rtnVal = d.getValue();
        txtBrand.setInputText( (rtnVal.equals("") ? txtBrand.getInputText() : rtnVal));
    }
    
    private void buttonBrand_actionPerformed(ActionEvent e) {
        e.toString();
        String sql = "SELECT DISTINCT Brand FROM asset";
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select Brand - Data Assistant",sql,connection);
        d.setFirstColumnWidth(300);
        d.setLocationRelativeTo(buttonBrand);
        d.setVisible(true); 
        String rtnVal = d.getValue();
        if(!rtnVal.equals("")){
            textFieldBrand.setInputText(rtnVal);
            textFieldCategory.setInputText("");
            textFieldModel.setInputText("");
            textFieldCustomer.setInputText("");
            textFieldAsset.setInputText("");
            //tableAssets.populate(connection,"SELECT asset_id FROM asset WHERE brand='"+textFieldBrand.getInputText()+"'");
            
            try {
                PreparedStatement pS = connection.prepareStatement("SELECT A.Asset_ID AS Machine,A.Model_ID AS Model ,D.Name AS 'Customer Name', D.Division_ID AS 'Customer ID',D.Address FROM asset A,division D WHERE brand= ? AND A.Division_ID = D.Division_ID");
                pS.setString(1,textFieldBrand.getInputText());
                ResultSet r = pS.executeQuery();
                
                try {
                    tableAssets.removeAll();
                }
                catch (Exception ex) {
                    ex.toString();
                }
                
                //if(! r.wasNull()){
                   
                    while(r.next()){
                        assetTableItem = new AssetTableItem();
                        assetTableItem.setAsset(r.getString("Machine"));
                        assetTableItem.setModel(r.getString("Model"));
                        assetTableItem.setCustomerID(r.getString("Customer ID"));
                        assetTableItem.setCustomerName(r.getString("Customer Name"));
                        assetTableItem.setAddress(r.getString("Address"));
                        tableAssets.addRow(assetTableItem);
                    }
                //}
                btnCancel.setEnabled(true);
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void buttonCategory_actionPerformed(ActionEvent e) {
        e.toString();
        if(textFieldBrand.getInputText().isEmpty()){
            messageBar.setMessage("Please select a Brand first.","ERROR");
        }
        else{
            String sql = "SELECT category_id AS ID FROM asset_category WHERE category_id IN " +
            "(SELECT category_id FROM asset_model WHERE model_id IN (SELECT model_id FROM asset WHERE brand ='" + textFieldBrand.getInputText() + "'))";
            
            DataAssistantDialog d = new DataAssistantDialog(frame,"Select Category - Data Assistant",sql,connection);
            d.setLocationRelativeTo(buttonBrand);
            d.setVisible(true); 
            String rtnVal = d.getValue();
            if(!rtnVal.equals("")){
                SELECTED_CATEGORY_ID = rtnVal;
                textFieldCategory.setInputText(SELECTED_CATEGORY_ID);
                textFieldModel.setInputText("");
                textFieldCustomer.setInputText("");
                textFieldAsset.setInputText("");
                sql = "SELECT asset_id As Machine, A.Model_ID AS Model ,D.Name AS 'Customer Name', D.Division_ID AS 'Customer ID',D.Address " +
                      " FROM asset A , Division D " +
                      " WHERE brand= ? AND " +
                            "model_id IN " +
                            "(SELECT model_id FROM asset_model WHERE category_id=?) " +
                            "AND D.Division_ID = A.Division_ID";
                
                
               
                
                try {
                
                    PreparedStatement pS = connection.prepareStatement(sql);
                    pS.setString(1,textFieldBrand.getInputText());
                    pS.setString(2,SELECTED_CATEGORY_ID);
                    
                    ResultSet r = pS.executeQuery();
                    
                    try {
                        tableAssets.removeAll();
                    }
                    catch (Exception ex) {
                        ex.toString();
                    }
                    
                    //if(! r.wasNull()){
                        
                        while(r.next()){
                            assetTableItem = new AssetTableItem();
                            assetTableItem.setAsset(r.getString("Machine"));
                            assetTableItem.setModel(r.getString("Model"));
                            assetTableItem.setCustomerID(r.getString("Customer ID"));
                            assetTableItem.setCustomerName(r.getString("Customer Name"));
                            assetTableItem.setAddress(r.getString("Address"));
                            tableAssets.addRow(assetTableItem);
                        }
                    //}
                    btnCancel.setEnabled(true);
                }
                catch (SQLException ex) {
                    ex.printStackTrace();
                }
                
            }
        }
    }

    private void buttonModel_actionPerformed(ActionEvent e) {
        e.toString();
        if(textFieldCategory.getInputText().isEmpty()){
            messageBar.setMessage("Please select a Category first.","ERROR");
        }
        else{
            String sql = "SELECT model_id AS ID FROM asset_model WHERE category_id='"+SELECTED_CATEGORY_ID+"'";
            DataAssistantDialog d = new DataAssistantDialog(frame,"Select Model - Data Assistant",sql,connection);
            d.setLocationRelativeTo(buttonBrand);
            d.setVisible(true); 
            String rtnVal = d.getValue();
            if(!rtnVal.equals("")){
                SELECTED_MODEL_ID = rtnVal;
                textFieldModel.setInputText(SELECTED_MODEL_ID);
                textFieldCustomer.setInputText("");
                textFieldAsset.setInputText("");
                sql = "SELECT asset_id As Machine, A.Model_ID AS Model ,D.Name AS 'Customer Name', D.Division_ID AS 'Customer ID',D.Address " +
                      " FROM asset A, Division D " +
                      " WHERE brand=? AND " +
                            "model_id = ? AND A.Division_ID = A.Division_ID";
                            
                
                
                try {
                
                    PreparedStatement pS = connection.prepareStatement(sql);
                    pS.setString(1,textFieldBrand.getInputText());
                    pS.setString(2,SELECTED_MODEL_ID);
                    
                    ResultSet r = pS.executeQuery();
                    
                    try {
                        tableAssets.removeAll();
                    }
                    catch (Exception ex) {
                        ex.toString();
                    }
                    
                    //if(! r.wasNull()){
                        
                        while(r.next()){
                            assetTableItem = new AssetTableItem();
                            assetTableItem.setAsset(r.getString("Machine"));
                            assetTableItem.setModel(r.getString("Model"));
                            assetTableItem.setCustomerID(r.getString("Customer ID"));
                            assetTableItem.setCustomerName(r.getString("Customer Name"));
                            assetTableItem.setAddress(r.getString("Address"));
                            tableAssets.addRow(assetTableItem);
                        }
                    //}
                    btnCancel.setEnabled(true);
                }
                catch (SQLException ex) {
                    ex.printStackTrace();
                }
                
                
            }
        }
    }

    private void buttonCustomer_actionPerformed(ActionEvent e) {
        e.toString();
        if(isCustomerBasedSelection()){
        
            String sql = "SELECT division_id AS ID, Name, Address " +
                         "FROM division " +
                         "WHERE division_id IN (SELECT division_id FROM asset) ORDER BY Name";
                         
            DataAssistantDialog d = new DataAssistantDialog(frame,"Select Customer - Data Assistant",sql,connection);
             
            d.grow(600,0,580);
            d.setFirstColumnWidth(50);
            d.setSecondColumnWidth(200);
            d.setThirdColumnWidth(330);
            d.setLocationRelativeTo(buttonCustomer);
            d.setVisible(true); 
            String rtnVal = d.getValue();
            
            if(!rtnVal.equals("")){
                SELECTED_CUSTOMER_ID = rtnVal;
                textFieldCustomer.setInputText(d.getDescription());
                textFieldAsset.setInputText("");
               
                sql = "SELECT asset_id As Machine, A.Model_ID AS Model ,D.Name AS 'Customer Name', D.Division_ID AS 'Customer ID',D.Address " +
                      "FROM asset A, Division D " +
                      " WHERE A.division_id = ? AND A.Division_ID = D.Division_ID";
                      
                try {
                
                    PreparedStatement pS = connection.prepareStatement(sql);
                    pS.setString(1,SELECTED_CUSTOMER_ID);
                   
                    try {
                        tableAssets.removeAll();
                    }
                    catch (Exception ex) {
                        ex.toString();
                    }
                    ResultSet r = pS.executeQuery();
                    //if(! r.wasNull()){
                        
                        while(r.next()){
                            assetTableItem = new AssetTableItem();
                            assetTableItem.setAsset(r.getString("Machine"));
                            assetTableItem.setModel(r.getString("Model"));
                            assetTableItem.setCustomerID(r.getString("Customer ID"));
                            assetTableItem.setCustomerName(r.getString("Customer Name"));
                            assetTableItem.setAddress(r.getString("Address"));
                            tableAssets.addRow(assetTableItem);
                        }
                   // }
                    btnCancel.setEnabled(true);
                }
                catch (SQLException ex) {
                    ex.printStackTrace();
                }
              
            }
        }
        else{
            if(textFieldModel.getInputText().isEmpty()){
                messageBar.setMessage("Please select a Model first.","ERROR");
            }
            else{
                String sql = "SELECT division_id AS ID, Name " +
                             "FROM division " +
                             "WHERE division_id IN (" +
                                    "SELECT division_id " +
                                    "FROM asset " +
                                    "WHERE brand = '"+textFieldBrand.getInputText()+"' AND " +
                                    "model_id = '"+SELECTED_MODEL_ID+"')";
                                                  
                DataAssistantDialog d = new DataAssistantDialog(frame,"Select Customer - Data Assistant",sql,connection);
                d.setLocationRelativeTo(buttonBrand);
                d.setVisible(true); 
                String rtnVal = d.getValue();
                if(!rtnVal.equals("")){
                    SELECTED_CUSTOMER_ID = rtnVal;
                    textFieldCustomer.setInputText(d.getDescription());
                    sql = "SELECT asset_id As Machine, A.Model_ID AS Model ,D.Name AS 'Customer Name', D.Division_ID AS 'Customer ID',D.Address " +
                          "FROM asset A , Division D  " +
                          "WHERE brand=? AND " +
                                "model_id =? AND " +
                                "A.division_id = ? AND D.Division_ID = A.Division_ID";
                                
                    try {
                    
                        PreparedStatement pS = connection.prepareStatement(sql);
                        pS.setString(1,textFieldBrand.getInputText());
                        pS.setString(2,SELECTED_MODEL_ID);
                        pS.setString(3,SELECTED_CUSTOMER_ID);
                        try {
                            tableAssets.removeAll();
                        }
                        catch (Exception ex) {
                            ex.toString();
                        }
                        ResultSet r = pS.executeQuery();
                        //if(! r.wasNull()){
                            
                            while(r.next()){
                                assetTableItem = new AssetTableItem();
                                assetTableItem.setAsset(r.getString("Machine"));
                                assetTableItem.setModel(r.getString("Model"));
                                assetTableItem.setCustomerID(r.getString("Customer ID"));
                                assetTableItem.setCustomerName(r.getString("Customer Name"));
                                assetTableItem.setAddress(r.getString("Address"));
                                tableAssets.addRow(assetTableItem);
                            }
                       // }
                        btnCancel.setEnabled(true);
                    }
                    catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                   
                }
            }
        }
    }
    
    private void buttonAsset_actionPerformed(ActionEvent e) {
        e.toString();
        String sql = "SELECT asset_id AS 'ID' FROM asset where deleted='false'";
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select Asset - Data Assistant",sql,connection);
        d.setFirstColumnWidth(300);
        d.setLocationRelativeTo(buttonBrand);
        d.setVisible(true); 
        String rtnVal = d.getValue();
        if(!rtnVal.equals("")){
            textFieldAsset.setInputText(rtnVal);
            selectAsset(rtnVal);
            tableAssets.removeAll();
            
            try {
                PreparedStatement pS  = connection.prepareStatement("SELECT asset_id As Machine, A.Model_ID AS Model ,D.Name AS 'Customer Name', D.Division_ID AS 'Customer ID',D.Address " +
                                                                    " FROM Asset A, Division D " +
                                                                    " WHERE A.Asset_ID = ? AND A.Division_ID = D.Division_ID");
                pS.setString(1,rtnVal);
                ResultSet r = pS.executeQuery();
                
                try {
                    tableAssets.removeAll();
                }
                catch (Exception ex) {
                    ex.toString();
                }
                
                //if(! r.wasNull()){
                    
                    while(r.next()){
                        assetTableItem = new AssetTableItem();
                        assetTableItem.setAsset(r.getString("Machine"));
                        assetTableItem.setModel(r.getString("Model"));
                        assetTableItem.setCustomerID(r.getString("Customer ID"));
                        assetTableItem.setCustomerName(r.getString("Customer Name"));
                        assetTableItem.setAddress(r.getString("Address"));
                        tableAssets.addRow(assetTableItem);
                    }
                //}
                btnCancel.setEnabled(true);
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
            
            
        }
    }
    
    private boolean isCustomerBasedSelection(){
        return (textFieldBrand.getInputText().isEmpty() && textFieldCategory.getInputText().isEmpty() && textFieldModel.getInputText().isEmpty());
    }
    
    private void selectAsset(String asset_id){
          try {
              setAssetInfo(asset_id);
          }
          catch(Exception e) {
              messageBar.setMessage(e.getMessage(),"ERROR");
          }
    }
    
    private void setAssetInfo(String asset_id){
    
        textFieldAsset.setInputText("");
        textFieldBrand.setInputText("");
        SELECTED_CATEGORY_ID = "";
        textFieldCategory.setInputText("");
        SELECTED_MODEL_ID = "";
        textFieldModel.setInputText("");
       
        SELECTED_CUSTOMER_ID = "";
        textFieldCustomer.setInputText("");
        
        String sql = "SELECT a.brand, " +
                            "c.category_id, " +
                            
                            "m.model_id," +
                          
                            "IF(a.Date_of_Commission='0000-00-00','0000-00-00',a.Date_of_Commission) com_date," +
                            "a.division_id, " +
                            "d.name " +
                     "FROM asset a, asset_category c, asset_model m, division d " +
                     "WHERE m.category_id = c.category_id AND m.model_id = a.model_id AND a.asset_id=? AND a.division_id = d.division_id";
        try{
            PreparedStatement pS = connection.prepareStatement(sql);
            pS.setString(1,asset_id);
            ResultSet rec = pS.executeQuery();
            rec.first();
            textFieldAsset.setInputText(asset_id);
            //System.out.println(sql);
            textFieldBrand.setInputText(rec.getString("brand"));
            SELECTED_CATEGORY_ID = rec.getString("category_id");
            textFieldCategory.setInputText(rec.getString("category_id"));
            SELECTED_MODEL_ID = rec.getString("model_id");
            textFieldModel.setInputText(rec.getString("model_id"));
            SELECTED_CUSTOMER_ID = rec.getString("division_id");
            textFieldCustomer.setInputText(rec.getString("d.name"));
           
        } 
        catch(Exception ex){
            ex.printStackTrace();
        } 
        
    }
    
    private void fill_cmbWPeriod(){
        for(int i=0;i<=120;i++){
            cmbWPeriod.addItem(i+"");   
        }
    }
    
    private void btnLoadImage_actionPerformed(ActionEvent e) {
        e.toString();
        JFileChooser chooser = new JFileChooser();
        ImageFilter filter = new ImageFilter();
        chooser.addChoosableFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setAccessory(new ImagePreview(chooser));
        chooser.showOpenDialog(this.frame);
        try {
            if (chooser.getSelectedFile().getPath()!="")  {
                lblImage.setImageToView(chooser.getSelectedFile().getPath());  
            }
        } 
        catch (Exception ex){
            ex.toString();
        }         
    }
    
    private void btnSaveImage_actionPerformed(ActionEvent e) {
        e.toString();
        JFileChooser chooser = new JFileChooser();
        chooser.showSaveDialog(this.frame);
        try{
            if (chooser.getSelectedFile().getPath()!="")  {
                lblImage.saveImage(chooser.getSelectedFile().getPath());         
            }        
        } 
        catch (Exception ex){
         ex.toString();
         }    
    }
    
    private void btnClearImage_actionPerformed(ActionEvent e) {
        e.toString();
        if(MessageBar.showConfirmDialog(frame,"Are you sure you want to delete \nthe Machine Image from the system?","Delete Image")==MessageBar.YES_OPTION){
            lblImage.setImageToView("");
            lblImage.deleteImage(txtAssetID.getInputText().trim());            
        }
    }
    
    private void btnAddPro_actionPerformed(ActionEvent e){
        e.toString();
        try{
            String newProperty = MessageBar.showInputDialog(frame,"Property Name","Add Property");
            if(!newProperty.trim().equals("")){
                if(saveNewProperty(newProperty,"Not Specified")){
                    tblProperties.addRow();
                    tblProperties.setValueAt(newProperty,tblProperties.getRowCount()-1,0);
                    tblProperties.setValueAt("Not Specified",tblProperties.getRowCount()-1,1);
                }
                else{
                    messageBar.setMessage("Error while saving the new property.","ERROR");
                }
            }
            else{
                messageBar.setMessage("The property name was empty.","ERROR");
            }
        } 
        catch(Exception ex){
        ex.toString();
        } 
    }

    private void btnRemovePro_actionPerformed(ActionEvent e){
        e.toString();
            int row = tblProperties.getSelectedRow();
            if(row!=-1){
                if(MessageBar.showConfirmDialog(frame,"Are you sure you want to delete \nthe selected property from the Machine?","Delete Propertry")==MessageBar.YES_OPTION){
                    if(deleteAssetProperty(row)){
                         tblProperties.deleteRow(row);
                         messageBar.setMessage("Property deleted.","OK");
                    }
                    else{
                        messageBar.setMessage("Error while deleting.","ERROR");
                    }
                }
            }
            else{
                messageBar.setMessage("Please select a property first.","ERROR");
            }
    }
    
    private boolean saveNewProperty(String name,String value){
        try{
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO user_defined_asset_property (Asset_ID,Property,Value) VALUES (?,?,?)");
           stmt.setString(1,txtAssetID.getInputText());
            stmt.setString(2,name);
            stmt.setString(3,value);
            stmt.executeUpdate();
            if(stmt.getUpdateCount()>0){
                return true;
            }
            else{
                return false;
            }
        }
        catch(Exception er){
            er.printStackTrace();
            return false;
        }  
    }
    
    private boolean deleteAssetProperty(int row){
        try{
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM user_defined_asset_property WHERE Asset_ID = ? AND Property = ?");
            stmt.setString(1,txtAssetID.getInputText());
            stmt.setString(2,tblProperties.getValueAt(row,0).toString());
            stmt.executeUpdate();
            if(stmt.getUpdateCount()>0){
                return true;
            }
            else{
                return false;
            }
        }
        catch(Exception er){
            er.printStackTrace();
            return false;
        }  
    }
    
    private boolean updateProperties(){
    
        int rows = tblProperties.getRowCount();
        try{
            
            
            for(int i=0;i<rows;i++){
                PreparedStatement stmt = connection.prepareStatement("UPDATE user_defined_asset_property SET Value =? WHERE Asset_ID =? AND Property = ?");
                stmt.setString(1,tblProperties.getValueAt(i,1).toString());
                stmt.setString(2,txtAssetID.getInputText());
                stmt.setString(3,tblProperties.getValueAt(i,0).toString());
                stmt.executeUpdate();
            }
            return true;
        }
        catch(Exception er){
            er.printStackTrace();
            return false;
        }
    }

    private void btnEdit_actionPerformed(ActionEvent e) {
        e.toString();
        setMode("EDIT");
    }

    private void btnAdd_actionPerformed(ActionEvent e) {
        e.toString();
        setMode("ADD");
    }

    private void btnDelete_actionPerformed(ActionEvent e) {
        e.toString();
        if(MessageBar.showConfirmDialog(frame,"Are you sure you want to delete \nthe selected Machine from the system?","Delete Machine")==MessageBar.YES_OPTION){
                 if(asset.delete()){
                   messageBar.setMessage("Machine deleted","OK");
                   setMode("LOAD");
                 }
                 else{
                   messageBar.setMessage("Error while deleting.","ERROR");
                 }
               }
    }

    /**********************************************************************************************/
    public class PropertiesTable extends JTable {
        private PropertiesTableModel myModel = null ;

        public PropertiesTable() {
            myModel = new PropertiesTableModel();
            this.setModel(myModel); 
            
            this.setSelectionMode(0);
            this.getTableHeader().setReorderingAllowed(false);
            this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.setRowHeight(this.getRowHeight()+5);
            this.getTableHeader().setVisible(false);
            this.getTableHeader().setPreferredSize(new Dimension(500,3));
            this.setGridColor(jPanel1.getBackground());
            this.getColumnModel().getColumn(0).setCellRenderer(new LockedLabelCellRenderer(jPanel1.getBackground(),"   "));
            
            this.getColumnModel().getColumn(0).setPreferredWidth(300);
            this.getColumnModel().getColumn(1).setPreferredWidth(200);
        }
     
        public void populate(String asset){
            tblProperties.deleteAll();
            try{
                PreparedStatement stmt = connection.prepareStatement("SELECT Property, Value FROM user_defined_asset_property WHERE Asset_ID = ? ORDER BY Property");
                stmt.setString(1,asset);
                ResultSet rec = stmt.executeQuery();
                while(rec.next()){
                    tblProperties.addRow();
                    int row = rec.getRow() - 1;
                    tblProperties.setValueAt(rec.getString("Property"),row,0);
                    tblProperties.setValueAt(rec.getString("Value"),row,1);
                }
                rec.close();
                stmt.close();
                rec = null;
                stmt = null;
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

        private class PropertiesTableModel extends AbstractTableModel{
            private int rowCount = 0;
            private String [] colNames = {"Property",//0
                                          "Value"//1
                                         };
            private Object [][] valueArray = null;
            private Object [][] tempArray = null;

            public PropertiesTableModel() {
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
                if(valueArray[r][c]==null) return "";
                else return valueArray[r][c];
            }
            
            public void setValueAt(Object value,int row,int col){
                valueArray[row][col] = value;
            }
    
            public boolean isCellEditable(int r,int c){
                return (c==1?true:false);
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
    /**Asset Image/Done by Himesha*/
    class AssetImage extends JLabel{
        private ImageIcon image;
        private String path;
        
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            if(image!=null){
                g.drawImage(image.getImage(),0,0,this.getWidth(),this.getHeight(),this);
            }           
        }
        
         public void setImageToView(String path){
             this.path = path;
             image = new ImageIcon(path);
             if(image.getIconHeight()<0){
                 image = new ImageIcon("./Asset_Images/default.jpg");
             }
             this.repaint();
             this.updateUI();
         }  
         
         public void saveImage(String savePath){
             try  {                
                 ImageIO.write(ImageIO.read(new File(path)),"jpg", new File(savePath + ".jpg"));
                 setImageToView(savePath + ".jpg");
             } catch (Exception ex){
                ex.toString();
             }
         }
         
         public void deleteImage(String assetID){
             new File("./Asset_Images/" + assetID + ".jpg").delete();
         }
    }   
    
    public class ImageFilter extends FileFilter {
        //Accept all directories and all gif, jpg, tiff, or png files.
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = new Utils().getExtension(f);
            if (extension != null) {
                if (extension.equals(Utils.tiff) ||
                    extension.equals(Utils.tif) ||
                    extension.equals(Utils.gif) ||
                    extension.equals(Utils.jpeg) ||
                    extension.equals(Utils.jpg) ||
                    extension.equals(Utils.png)) {
                    return true;
                } 
                else{
                    return false;
                }
            }
            return false;
        }
        
        //The description of this filter
        public String getDescription() {
            return "Just Images";
        }
    }
    
    /* Utils.java is a 1.4 example used by FileChooserDemo2.java. */
    public class Utils {
        public final static String jpeg = "jpeg";
        public final static String jpg = "jpg";
        public final static String gif = "gif";
        public final static String tiff = "tiff";
        public final static String tif = "tif";
        public final static String png = "png";

        /*
        * Get the extension of a file.
        */
        public String getExtension(File f) {
            String ext = null;
            String s = f.getName();
            int i = s.lastIndexOf('.');
        
            if (i > 0 &&  i < s.length() - 1) {
                ext = s.substring(i+1).toLowerCase();
            }
            return ext;
        }

         /** Returns an ImageIcon, or null if the path was invalid. */
         protected ImageIcon createImageIcon(String path) {
             URL imgURL = Utils.class.getResource(path);
             if (imgURL != null) {
                 return new ImageIcon(imgURL);
             } else {
                 System.err.println("Couldn't find file: " + path);
                 return null;
             }
         }
    }

    public class ImagePreview extends JComponent implements PropertyChangeListener {
        ImageIcon thumbnail = null;
        File file = null;
    
        public ImagePreview(JFileChooser fc) {
            setPreferredSize(new Dimension(100, 50));
            fc.addPropertyChangeListener(this);
        }

        public void loadImage() {
            if (file == null) {
                thumbnail = null;
                return;
            }
            //Don't use createImageIcon (which is a wrapper for getResource)
            //because the image we're trying to load is probably not one
            //of this program's own resources.
            ImageIcon tmpIcon = new ImageIcon(file.getPath());
            if (tmpIcon != null) {
                if (tmpIcon.getIconWidth() > 90) {
                    thumbnail = new ImageIcon(tmpIcon.getImage().getScaledInstance(90, -1,Image.SCALE_DEFAULT));
                } 
                else { 
                    //no need to miniaturize
                    thumbnail = tmpIcon;
                }
            }
        }

        public void propertyChange(PropertyChangeEvent e) {
            boolean update = false;
            String prop = e.getPropertyName();
        
            //If the directory changed, don't show an image.
            if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop)) {
                file = null;
                update = true;
                //If a file became selected, find out which one.
            } 
            else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
                file = (File) e.getNewValue();
                update = true;
            }
        
            //Update the preview accordingly.
            if (update) {
                thumbnail = null;
                    if (isShowing()) {
                        loadImage();
                        repaint();
                    }
            }
        }

        protected void paintComponent(Graphics g) {
            if (thumbnail == null) {
                loadImage();
            }
            if (thumbnail != null) {
                int x = getWidth()/2 - thumbnail.getIconWidth()/2;
                int y = getHeight()/2 - thumbnail.getIconHeight()/2;
        
                if (y < 0) {
                    y = 0;
                }
        
                if (x < 5) {
                    x = 5;
                }
                thumbnail.paintIcon(this, g, x, y);
            }
        }
    }
    
    class AssetTableItem extends TableItem{
    
        private String asset;
        private String model;
        private String customerID;
        private String customerName;
        private String address;
        
        public String getValueAt(int c){
            switch (c){
                case 0: return asset;
                case 1: return model;
                case 2: return customerID;
                case 3: return customerName;
                case 4: return address;
                default :return "";
            }
        }

        public void setAsset(String asset){
            this.asset = asset;
        }
        
        public void setModel(String model){
            this.model = model;
        }
        
        public void setCustomerID(String customerID){
            this.customerID = customerID;
        }
        
        public void setCustomerName(String customerName){
            this.customerName = customerName;
        }
        
        public void setAddress(String address){
            this.address = address; 
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
