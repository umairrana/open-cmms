package com.matrix.focus.register;

import com.matrix.components.TitleBar;
import com.matrix.focus.util.MPanel;

import com.matrix.focus.mdi.MDI;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.ImageLibrary;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.sql.Connection;
import java.sql.SQLException;

import com.matrix.components.MDataCombo;
import com.matrix.focus.inspection.MasterTaskDataCHK;
import com.matrix.focus.util.DataAssistantDialog;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.Font;
import java.sql.ResultSetMetaData;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.table.AbstractTableModel;

public class GUIRegisterCheckList extends MPanel{

    /**Get these from the MDI*/
    private Connection dbcon;
    private MDI frame;
    private MessageBar messageBar;

    private TitleBar titlebar = new TitleBar();
    private JButton jButton1 = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));
    private JButton btnDelete = new JButton(new ImageIcon(ImageLibrary.BUTTON_REMOVE));
    private JButton btnMachine = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton btnModel = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton btnCategory = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton btnBrand = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton btnNewInspection = new JButton(new ImageIcon(ImageLibrary.BUTTON_NEW));

    private JPanel jPanel1 = new JPanel();
    private JScrollPane ps = new JScrollPane();
    public static JTable table = new JTable();
    public static JTable tableBCM = new JTable();  //Brandchkmodel - right hand table
    public MasterTaskDataCHK data;
    private JPanel pnlTop = new JPanel();
    private JLabel lblTitle = new JLabel();
    private MDataCombo cmbCategory = new MDataCombo();
    //private JComboBox cmbCategory = new JComboBox(pmcategory);
    private JScrollPane jsSelected = new JScrollPane();
    private boolean started;
    private boolean mState;  
    private int inspection_id;
    private JPanel jPanel2 = new JPanel();
    private JTextField txtMachine = new JTextField();
    private JLabel jLabel5 = new JLabel();
    private JTextField txtModel = new JTextField();
    private JLabel jLabel4 = new JLabel();
    private JTextField txtCategory = new JTextField();
    private JLabel jLabel2 = new JLabel();
    private JTextField txtBrand = new JTextField();
    private JLabel jLabel6 = new JLabel();
    
    //*************************************
    private String brandID = "0";
    private String brandName;    
    private String categoryID = "0";
    private String categoryName;
    private String modelID = "";
    private String modelName;
    private String machineNo = "";
    
    private InspectionListModel inspectionListModel = new InspectionListModel();
    
    private DataAssistantDialog dadBrand;
    private DataAssistantDialog dadCategory;
    private DataAssistantDialog dadModel;
    private DataAssistantDialog dadMachine;

    private JPanel jPanel3 = new JPanel();
    private JPanel jPanel4 = new JPanel();
    
    private String strSqlForRegisteredInspections = "SELECT i.category,i.description, mi.dont_use " +
             "FROM inspectionmaster i,machine_inspection mi " +
             "WHERE mi.inspection_id = i.inspection_id AND " +
             "mi.machine_no = ? " +
             "ORDER BY i.category";
    private JPanel jPanel5 = 
        new JPanel();
    private JButton btnSave = 
        new JButton(); /// to get the correct query pass the values to getPreparedQuery()
             
    

    public GUIRegisterCheckList(DBConnectionPool pool, MDI frame, MessageBar msgBar){
        /**From the MDI*/
        /**New Database connection was taken to manage the transactions*/

        this.dbcon = pool.getConnection();
        try{
            dadBrand = new DataAssistantDialog(new JFrame(),
                                           "Select the Brand",
                                           "SELECT * " +
                                           "FROM matrixmeter.brands b " +
                                           "ORDER BY b.Description",
                                           dbcon);  
        this.frame = frame;
        this.messageBar = msgBar;
        
        titlebar.setTitle("Register Inspections");
        titlebar.setDescription("The facility to register inspections to assets.");
        titlebar.setImage(ImageLibrary.TITLE_CHECKLIST_REGISTER);
        
        jbInit();
        cmbCategory.getComboBox().setSelectedIndex(1);
        }
        catch(Exception e){
          e.printStackTrace();
        }
    }
   
    public void close(){
        try {
            dbcon.close();
        } catch (SQLException e) {
        e.printStackTrace();
        }
    }
    
    
    private void jbInit() throws Exception{
        this.setLayout(null);
        this.setSize(new Dimension(998, 597));
        titlebar.setBounds(new Rectangle(10, 10, 940, 70));
        this.add(titlebar, null);
        this.add(jPanel2, null);
        started = false;
        mState = false;
        this.setLayout(null);
        this.setSize(new Dimension(970, 634));
        jButton1.setBounds(new Rectangle(390, 20, 35, 25));
        jPanel1.setBounds(new Rectangle(10, 345, 815, 260));
        btnNewInspection.setBounds(new Rectangle(390, 50, 35, 25));
        //btnNewInspection.setSize(new Dimension(145, 25));
        jPanel1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        jPanel1.setLayout(null);
        ps.setBounds(new Rectangle(10, 20, 375, 190));
        btnDelete.setBounds(new Rectangle(755, 20, 35, 25));
        jPanel5.add(cmbCategory, null);
        jPanel5.add(btnBrand, null);
        jPanel5.add(btnCategory, null);
        jPanel5.add(btnModel, null);
        jPanel5.add(btnMachine, null);
        jPanel5.add(jLabel5, null);
        jPanel5.add(jLabel4, null);
        jPanel5.add(jLabel2, null);
        jPanel5.add(jLabel6, null);
        jPanel5.add(txtBrand, null);
        jPanel5.add(txtCategory, null);
        jPanel5.add(txtModel, null);
        jPanel5.add(txtMachine, null);
        jsSelected.getViewport().add(tableBCM, null);
        jPanel3.add(jsSelected, null);
        jPanel3.add(btnDelete, null);
        jPanel1.add(jPanel3, null);
        ps.getViewport().add(table, null);
        jPanel4.add(btnSave, null);
        jPanel4.add(ps, null);
        jPanel4.add(jButton1, null);
        jPanel4.add(btnNewInspection, null);
        jPanel2.add(jPanel4, null);
        jPanel2.add(jPanel5, null);
        btnCategory.setEnabled(false);
        btnModel.setEnabled(false);
        cmbCategory.setBounds(new Rectangle(10, 160, 345, 20));
        cmbCategory.setCaption("Inspection Category");
        cmbCategory.setLblWidth(105);
        cmbCategory.setCmbWidth(180);
        jButton1.setEnabled(false);
        pnlTop.add(lblTitle, null);
        this.add(pnlTop, null);

        this.add(jPanel1, null);
        data = new MasterTaskDataCHK(dbcon, this);
        table = new JTable();
        table.addMouseListener(new MouseAdapter(){
              public void mouseClicked(MouseEvent e){
                  jButton1.setEnabled(true);
              }
          });
        
        tableBCM = new JTable();
        //tableBCM.getColumnModel().getColumn(0).setPreferredWidth(50);
        //tableBCM.getColumnModel().getColumn(1).setPreferredWidth(100);
        //tableBCM.getColumnModel().getColumn(2).setPreferredWidth(150);
        //tableBCM.getColumnModel().getColumn(3).setPreferredWidth(50);
        
        tableBCM.addMouseListener(new MouseAdapter(){
              public void mouseClicked(MouseEvent e){
                  setInspectionID();
              }
          });
        jPanel2.setBounds(new Rectangle(10, 90, 815, 250));
        jPanel2.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        jPanel2.setLayout(null);
        btnMachine.setBounds(new Rectangle(305, 125, 30, 20));
        btnMachine.setSize(new Dimension(30, 20));
        btnMachine.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnMachine_actionPerformed(e);
                    }
                });
        txtMachine.setBounds(new Rectangle(115, 125, 180, 20));
        txtMachine.setEditable(false);
        jLabel5.setText("Machine");
        jLabel5.setBounds(new Rectangle(10, 125, 85, 20));
        btnModel.setBounds(new Rectangle(305, 95, 30, 20));
        btnModel.setSize(new Dimension(30, 20));
        btnModel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnModel_actionPerformed(e);
                    }
                });
        txtModel.setBounds(new Rectangle(115, 95, 180, 20));
        txtModel.setEditable(false);
        jLabel4.setText("Model");
        jLabel4.setBounds(new Rectangle(10, 95, 85, 20));
        btnCategory.setBounds(new Rectangle(305, 65, 30, 20));
        btnCategory.setSize(new Dimension(30, 20));
        btnCategory.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnCategory_actionPerformed(e);
                    }
                });
        txtCategory.setBounds(new Rectangle(115, 65, 180, 20));
        txtCategory.setEditable(false);
        jLabel2.setText("Category");
        jLabel2.setBounds(new Rectangle(10, 65, 65, 20));
        btnBrand.setBounds(new Rectangle(305, 35, 30, 20));
        btnBrand.setSize(new Dimension(30, 20));
        btnBrand.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnBrand_actionPerformed(e);
                    }
                });
        txtBrand.setBounds(new Rectangle(115, 35, 180, 20));
        txtBrand.setEditable(false);
        jLabel6.setText("Brand");
        jLabel6.setBounds(new Rectangle(10, 35, 60, 25));
            jPanel3.setBounds(new Rectangle(10, 10, 795, 245));
        jPanel3.setLayout(null);
        jPanel3.setBorder(BorderFactory.createTitledBorder("All Registered Inspections for the machine"));
        jPanel4.setBounds(new Rectangle(375, 10, 430, 220));
        jPanel4.setBorder(BorderFactory.createTitledBorder("Inspection in the Category"));
        jPanel4.setLayout(null);


    

        btnNewInspection.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    btnNewInspection_actionPerformed(e);
                }
            });
        jPanel5.setBounds(new Rectangle(10, 10, 360, 220));
        jPanel5.setLayout(null);
        jPanel5.setBorder(BorderFactory.createTitledBorder("Assets Selection"));
        btnSave.setBounds(new Rectangle(390, 80, 35, 25));
        btnSave.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnSave_actionPerformed(e);
                    }
                });
        jsSelected.setBounds(new Rectangle(10, 20, 740, 215));
        
        
        cmbCategory.getComboBox().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cmbCategory_actionPerformed(e);
            }
        });

        lblTitle.setFont(new Font("Comic Sans MS", 2, 20));
        lblTitle.setBounds(new Rectangle(25, 15, 305, 30));
        lblTitle.setText("Register Inspection List ........");
        pnlTop.setBackground(new Color(168, 196, 238));
        pnlTop.setLayout(null);
        pnlTop.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        pnlTop.setBounds(new Rectangle(15, 10, 755, 55));
        btnDelete.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnDelete_actionPerformed(e);
                    }
                });
        btnDelete.setEnabled(false);
        jButton1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        jButton1_actionPerformed(e);
                    }
                });
        
        
        //----------------------------------------------Fill Combo   
        cmbCategory.populate(dbcon,"select Distinct(Category) from inspectionmaster");
        
        // 1----------------------------------------    
        
        ps.getViewport().add(table, null);
        ps.repaint();
        table.setAutoCreateColumnsFromModel(false);
        table.setModel(data);
        //table.setBackground(tableBack);
        
        for (int k = 0; k < MasterTaskDataCHK.columns.length; k++) {
            TableColumn column = new TableColumn(k, 10, null, null);
            table.addColumn(column);
            table.repaint();
            repaint();
        
        }
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setPreferredWidth(25);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.setAutoResizeMode(table.AUTO_RESIZE_OFF);
        
       // MasterTaskDataCHK.ViewSavedCategory("Air Circuit", "inspectionmaster");
        showTableMaster();
        jsSelected.repaint();
        //-------------------------------------Started
        started=true;
    }
    
  
    private void cmbCategory_actionPerformed(ActionEvent e)  //change inspection category combo
    {
      try
      {
        if(started)
        {
            //loads inspection master details according to inspection category
         
            MasterTaskDataCHK.ViewSavedCategory((String)cmbCategory.getSelectedItem(),"inspectionmaster");
            showTableMaster();
            
            //loads registered inspections for the machine
            loadRegisteredInspections();
        }
      }
       catch(Exception eCat)
      {
        JOptionPane.showMessageDialog(null,"ECAT"+eCat + " - \n" + e.toString());
      }
    }

    private void jButton1_actionPerformed(ActionEvent e) //add buttonclick
    {
      int row = table.getSelectedRow();
      if(row<0)
      {
        messageBar.setMessage("Select a Value ", "WARN");
      }
      else
      {       
        saveRegisterInspectionToMachine();  
        loadRegisteredInspections();
        e.toString();
      }
    }
    
    private boolean loadRegisteredInspections() {
        try {      
            String query = getPreparedQuery(txtMachine.getText().trim());
            inspectionListModel.emptyDataSet();
            inspectionListModel.setDataset(dbcon.createStatement().executeQuery(query));
            populateInspectionTable();
            return true;
        } catch (SQLException e) {
            messageBar.setMessage("Error in loading details", "OK");
            return false;
        }
        
    }
    
    private boolean saveRegisterInspectionToMachine() {
      
          try {
              String sql="insert into machine_inspection values(?,?,?)";
              Statement maxst=dbcon.createStatement();
              
              //--------------------------    get the inspection ID
              ResultSet rsIID=maxst.executeQuery("select Inspection_ID from inspectionmaster " +
              "where Description='"+table.getValueAt(table.getSelectedRow(),1).toString().trim()+ "'");
              
              int IID=0;
              while(rsIID.next())
              {
                  IID =rsIID.getInt(1);
              }
              //-------------------------------------
              
              PreparedStatement st=dbcon.prepareStatement(sql);
              st.setString(1,txtMachine.getText().toString().trim());
              st.setInt(2,IID);
              st.setString(3,"false");
              
              st.executeUpdate();
              messageBar.setMessage("Inspection successfully registered for the machine no : " + txtMachine.getText(),"OK");
              return true;
          }
          catch (SQLException e) {
              messageBar.setMessage("Selected Inspection already registered !","WARN");
              return false;
          }
        
       
    }
    
    public void showTableMaster()  //repaint the table inspection master
    {
      ps.getViewport().add(table, null);
      ps.repaint();
      table.setAutoCreateColumnsFromModel(false);
      table.setModel(data); 
      table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      table.repaint();
    }
    
    private void btnDelete_actionPerformed(ActionEvent e)
    {
        if (!(isExistingLogData())) {
            if(messageBar.showConfirmDialog(frame,"Are you sure you want to delete \nthe selected inspection from the system?","Delete Inspection") == MessageBar.YES_OPTION){

            String sql = "DELETE FROM machine_inspection " +
            "WHERE inspection_id = '" + inspection_id + "'";

            Statement stmt;

                try{
                    stmt = dbcon.createStatement();
                    if(stmt.executeUpdate(sql) >0)
                        messageBar.setMessage("Inspection Removed", "OK");
                    stmt.close();
                    loadRegisteredInspections();
                }
                
                catch(Exception er){
                    stmt = null;
                    er.printStackTrace();
                    e.toString();
                }
            }
        }
    
    }
    private boolean isExistingLogData() {
        try
        {
            String sql="SELECT il.Inspection_ID " +
                       "FROM inspection_log il, preventive_maintenance_work_order w " +
                       "WHERE il.Inspection_ID = '" + inspection_id + "' AND " +
                       "w.PM_Work_Order_ID = il.SO_No AND " +
                       "w.Asset_ID = '" + txtMachine.getText().toString().trim() + "'";  
            
            Statement st=dbcon.createStatement();
            ResultSet rsmax=st.executeQuery(sql);
        
            while(rsmax.next())
            {
                if (messageBar.showConfirmDialog(frame,"There are existing log entries for this corresponding Inspection item\n" +
                "Do you want to mark this inspection as don't use?\n\n" +
                "Inspections marked as \'don't use\' will be not included in log data","Remove Item") == MessageBar.YES_OPTION){
                setDontUse(true); 
            }
            return true; 
            }
        }
        catch(Exception ee)
        {
            ee.printStackTrace();
        }
        return false;
    }
    
    private void setDontUse(Boolean value) 
    {
    //tableBCM.setValueAt(Boolean.TRUE,tableBCM.getSelectedRow(),3); 
    
    String sql = "UPDATE machine_inspection " +
    "SET dont_use = '" + value.TRUE + "'" +
    "WHERE inspection_id = '" + inspection_id + "' AND " +
    "machine_no = '" + txtMachine.getText().trim() + "'";
    Statement stmt;
    try{
    stmt = dbcon.createStatement();
    stmt.executeUpdate(sql) ;
    //    JOptionPane.showMessageDialog(null, "Inspection Removed","Delete",JOptionPane.INFORMATION_MESSAGE);
    stmt.close();
    loadRegisteredInspections();
    }
    
    catch(Exception er){
    stmt = null;
    er.printStackTrace();
    }
    }
    
    private void setInspectionID(){
        try
        {
            //--------------------------    get the inspection ID
            Statement maxst=dbcon.createStatement();
            ResultSet rsIID=maxst.executeQuery("select Inspection_ID from inspectionmaster " +
            "WHERE Description='"+tableBCM.getValueAt(tableBCM.getSelectedRow(),2).toString().trim()+ "' AND " +
            "Category = '" + tableBCM.getValueAt(tableBCM.getSelectedRow(),1).toString().trim() + "'");
            //"Category  = '" + cmbCategory.getSelectedItem().toString() + "'");
            
            btnDelete.setEnabled(true);
            inspection_id=0;
            while(rsIID.next())
            {
                inspection_id =rsIID.getInt(1);
            }
        }
            
        catch(SQLException sqlex) {
            sqlex.toString();
        }
    }
    
    private void btnMachine_actionPerformed(ActionEvent e) {
        e.toString();
        machineNo = txtMachine.getText().toString().trim();
        if(btnModel.isEnabled()){
                dadMachine = new DataAssistantDialog(frame,
                                                  "Select Asset ",
                                                  "SELECT Asset_ID AS Asset, Model_ID AS Model " +
                                                  "FROM " +
                                                  "" +
                                                  "asset " + 
                                                  "WHERE Model_ID = '"+modelID+"' ",
                                                  dbcon);
                                                  
                dadMachine.setLocationRelativeTo(btnMachine);  
                dadMachine.setVisible(true);
                
                if(!dadMachine.getValue().equals("")){
                    txtMachine.setText(dadMachine.getValue());
                    //scrollPaneRegisteredPMs.getViewport().removeAll();
                    
                   // scrollPanePlan.getViewport().add(plan);
                    
                    //fillFields();
                    
                }
                
            
        }else{
            dadMachine = new DataAssistantDialog(frame,
                                              "Select Machine",
                                              "SELECT Asset_ID AS Asset, Model_ID AS Model " +
                                              "FROM asset m ",
                                              dbcon);
                                              
            dadMachine.setLocationRelativeTo(btnMachine);  
            dadMachine.setVisible(true);
            
            if(!dadMachine.getValue().equals("")){
            
                txtMachine.setText(dadMachine.getValue());
                //scrollPaneRegisteredPMs.getViewport().removeAll();
               
                //scrollPanePlan.getViewport().add(plan);
                btnCategory.setEnabled(false);
                btnModel.setEnabled(false);
                try{
                    PreparedStatement pS = dbcon.prepareStatement("SELECT " +
                                                                            "a.Brand," +
                                                                            "c.Category_ID," +
                                                                            "m.Model_ID " +
                                                                       "FROM " +
                                                                            "asset a, " +
                                                                            "asset_Category c, " +
                                                                            "asset_Model m " +
                                                                       "WHERE " +
                                                                            "a.asset_ID=? " +
                                                                            "AND " +
                                                                            "m.Category_ID=c.Category_ID " +
                                                                            "AND " +
                                                                            "a.Model_ID=m.Model_ID ");
                    pS.setString(1,txtMachine.getText());
                    ResultSet r = pS.executeQuery();
                    r.first();
                    txtBrand.setText(r.getString(1));
                    txtCategory.setText(r.getString(2));
                    txtModel.setText(r.getString(3));
                    loadRegisteredInspections();
              
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }
        
        dadMachine.setFirstColumnWidth(300);
        dadMachine.setSecondColumnWidth(500);
    }
    
    private void btnModel_actionPerformed(ActionEvent e) {
        e.toString();
        try{   
            String sql = "SELECT model_id AS Model FROM asset_model WHERE category_id='"+categoryID+"'";

            dadModel = new DataAssistantDialog(frame,
                                              "Select the Model",
                                             sql,
                                              dbcon);
                                              
           
            dadModel.setLocationRelativeTo(btnCategory);         
            dadModel.setFirstColumnWidth(300);
            dadModel.setVisible(true);
            modelID = dadModel.getValue();
            txtModel.setText(modelID);
            txtMachine.setText("");
            try{
               // scrollPaneRegisteredPMs.getViewport().remove(registeredPMs);
                //scrollPanePlan.getViewport().remove(plan);
            }
            catch(Exception ex){
                ex.toString();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        } 
    }
    
    private void btnCategory_actionPerformed(ActionEvent e) {
        e.toString();
        
        try{   
            String sql = "SELECT " +
                            "category_id AS Category " +
                         "FROM " +
                            "asset_category " +
                         "WHERE category_id IN " +
            "(SELECT category_id FROM asset_model WHERE model_id IN (SELECT model_id FROM asset WHERE brand ='" + brandID + "'))";
            
            dadCategory = new DataAssistantDialog(frame,
                                                  "Select Category",
                                                  sql,
                                                  dbcon);
            dadCategory.setLocationRelativeTo(btnCategory);         
            dadCategory.setFirstColumnWidth(300);
            dadCategory.setVisible(true);
           
            if(!dadCategory.getValue().equals("")){
                categoryID = dadCategory.getValue();
                txtCategory.setText(categoryID);
                btnModel.setEnabled(true);
                txtModel.setText("");
                txtMachine.setText("");
                try{
                //scrollPaneRegisteredPMs.getViewport().remove(registeredPMs);
                //scrollPanePlan.getViewport().remove(plan);
                }
                catch(Exception ex){
                    ex.toString();
                }
            }
            
        }catch (NullPointerException nex){
            nex.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        } 
    }
    
    private void btnBrand_actionPerformed(ActionEvent e) {
        e.toString();
        dadBrand = new DataAssistantDialog(frame,
                                           "Select Brand",
                                           "SELECT DISTINCT brand " +
                                           "FROM asset " ,
                                           dbcon);
        dadBrand.setLocationRelativeTo(btnBrand);        
        dadBrand.setFirstColumnWidth(300);
        dadBrand.setVisible(true); 
        
        
        if(!dadBrand.getValue().equals("")){
            brandID = dadBrand.getValue();
            txtBrand.setText(dadBrand.getValue());
            txtCategory.setText("");
            txtModel.setText("");
            txtMachine.setText("");
            
            try{
                //scrollPaneRegisteredPMs.getViewport().remove(registeredPMs);
                //scrollPanePlan.getViewport().remove(plan);
            }
            catch(Exception ex){
                ex.toString();
            }
            btnCategory.setEnabled(true);
            btnModel.setEnabled(false);
        }
      
        
         
    }
    
    private void fillToUp() {
    try{
    ResultSet rsUp = dbcon.createStatement().executeQuery("SELECT m.Brand_ID, b.Description, m.Category_ID, c.Description, m.Model_ID, mo.Description " + 
                                  "FROM machinesmaster m, brands b, categories c, models mo " + 
                                  "WHERE m.Brand_ID = b.Brand_ID " + 
                                  "AND m.Category_ID = c.Category_ID " + 
                                  "AND m.Model_ID = mo.Model_ID " + 
                                  "AND m.Machine_No = '"+machineNo+"'");
    rsUp.next();
    
    brandID = rsUp.getString(1);
    brandName = rsUp.getString(2);
    txtBrand.setText(brandName);
    
    categoryID = rsUp.getString(3);
    categoryName = rsUp.getString(4);
    txtCategory.setText(categoryName);
    
    
    
    modelID = rsUp.getString(5);
    modelName = rsUp.getString(6);
    txtModel.setText(modelName);
    
    }catch (Exception ex){
    ex.printStackTrace();
    }
    }
    
    private String getPreparedQuery(String machineNo){
    //String s= strSqlForRegisteredInspections;
    
    StringTokenizer sT = new StringTokenizer(strSqlForRegisteredInspections,"?",false);
    String temp="";
    //while(sT.hasMoreTokens()){
    temp = sT.nextToken() + "'" + machineNo + "'";
    temp = temp + sT.nextToken();
    //temp = temp + "'" + category + "'";
    //temp = temp + sT.nextToken();
    //}
    return temp;
    } //prepare the query for view the 
    
    private void populateInspectionTable() {
    tableBCM.setModel(inspectionListModel);
    tableBCM.getColumnModel().getColumn(0).setPreferredWidth(15);
    tableBCM.setAutoCreateColumnsFromModel(false);
    tableBCM.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tableBCM.setAutoCreateRowSorter(true);
    jsSelected.getViewport().add(tableBCM);
    tableBCM.repaint();
    }
    
    private void btnNewInspection_actionPerformed(ActionEvent e) {
        e.toString();
        frame.menuItemChecklistItem.doClick();

    }

    private void btnSave_actionPerformed(ActionEvent e) {
        
    }


    /***************************************************************************/
   class InspectionListData{
      public int pmID;     
      public String category;
      public String pmDescription;
      public boolean visible;

      public InspectionListData(int id, String cat, String des, boolean du) {
         pmID = id;
         category = cat;
         pmDescription = des;
         visible = du;
      }
    } 
   
   class InspectionListModel extends AbstractTableModel{  
        private Vector v_pm = new Vector();
        public String col_names[] = {"ID","Category", "Description","Don't use anymore"};
        private int r,c;
        private ResultSetMetaData meta;
        
        public InspectionListModel(){
        
        }
        
        public void setDataset(ResultSet rs){
            try{  
              meta = rs.getMetaData();
              r = rs.getRow();
              c = meta.getColumnCount();
                int x=1;
              while(rs.next()){
              //JOptionPane.showMessageDialog(null,x + " : " + rs.getString(1));
                  v_pm.addElement(new InspectionListData(x++, rs.getString(1), rs.getString(2), rs.getBoolean(3) ));
              }
            }
            catch(Exception e){  
               e.printStackTrace();
            }
        }
        
//        public void setOtherDataset(ResultSet rs){
//          try{  
//            meta = rs.getMetaData();
//            r = rs.getRow();
//            c = meta.getColumnCount();
//            int x = 0;
//            while(rs.next()){
//              v_pm.addElement(new InspectionListData(x++, rs.getString(1), rs.getString(2), rs.getBoolean(3) ));
//            }
//          }
//          catch(Exception e){  
//             e.printStackTrace();
//          }
//      }
        
        public void emptyDataSet(){
           if(v_pm.size() != 0){
               v_pm.removeAllElements();
           }
        }
        
        public boolean isEmpty(){
           if(v_pm.size() == 0){
               return true;
           }else
               return false;
        }
        
        public void addRow(int pmID, String cat, String des){
          boolean in = false;
          for(int r = 0; r < v_pm.size(); r++) {
              InspectionListData p = (InspectionListData) v_pm.elementAt(r);
              if(p.pmID == pmID){
                   in = true;
                   break;
              }
          }
          if(!in)
              v_pm.addElement(new InspectionListData(pmID,cat, des, true));
        }
     
        public void removeRow(int nodeIndex){
            v_pm.removeElementAt(nodeIndex);            
        }
     
        public String getColumnName(int c){  
           return col_names[c];
        }
     
        public int getColumnCount(){ 
           return col_names.length;
        }

        public Object getValueAt(int r, int c){  
           InspectionListData m = (InspectionListData)v_pm.elementAt(r);
           switch (c) {
             case 0: return m.pmID;
             case 1: return m.category;
             case 2: return m.pmDescription;
             case 3: return m.visible;
           }
           return null;
        }
     
        public int getRowCount(){  
           return v_pm.size();
        }
        
        public void setValueAt(Object value,int row,int col){
           InspectionListData m_set = (InspectionListData)v_pm.elementAt(row);
           switch (col) {
             case 0:
                   m_set.pmID = Integer.parseInt(value.toString()); 
                   break;
             case 1:
                   m_set.category = value.toString(); 
                   break;
             case 2:
                   m_set.pmDescription = value.toString();  
                   break;
             case 3:
                   m_set.visible = Boolean.valueOf(value.toString()); 
                  
           }
        }
                  
        public boolean isCellEditable(int r,int c){  
          if(c == 3 || c == 4)
            return true;
          else
            return false;
        }
       
        public Class getColumnClass(int c) {
           return getValueAt(0, c).getClass();
        }
      }
      
//   class FirstPMData{
//     public String pmID;      
//     public String pmDescription;
//     public int pmLimit;
//     
//     public FirstPMData(String id, String des, int limit) {
//        pmID = id;      
//        pmDescription = des;
//        pmLimit = limit;
//     }
//   }
   
  /**********************************************************************************************/
//   class FirstPMTableModel extends AbstractTableModel{  
//       private Vector v_pm = new Vector();
//       public String col_names[] = {"PM_ID","PM Description","First Limit"}; 
//       private int r,c;
//       private ResultSetMetaData meta;
//       
//       public FirstPMTableModel(){}
//       
//       public void setDataset(ResultSet aResultSet){
//           try{  
//             meta = aResultSet.getMetaData();
//             r = aResultSet.getRow();
//             c = meta.getColumnCount();
//             while(aResultSet.next()){
//               v_pm.addElement(new FirstPMData(aResultSet.getString(1),aResultSet.getString(2),aResultSet.getInt(3)));
//             }
//           }
//           catch(Exception e){  
//              e.printStackTrace();
//           }
//       }
//       
//       public void emptyDataSet(){
//          if(v_pm.size() != 0){
//              v_pm.removeAllElements();
//          }
//       }
//       
//       public boolean isEmpty(){
//          if(v_pm.size() == 0){
//              return true;
//          }else
//              return false;
//       }
//       
//       public void addRow(String pmID, String des){
//           boolean in = false;
//           for(int r = 0; r < v_pm.size(); r++) {
//               FirstPMData p = (FirstPMData) v_pm.elementAt(r);
//               if(p.pmID.equals(pmID)){
//                    in = true;
//                    break;
//               }
//           }
//           
//           if(!in)
//              v_pm.addElement(new FirstPMData(pmID, des, 0));            
//       }
//    
//       public void removeRow(int nodeIndex){
//           v_pm.removeElementAt(nodeIndex);            
//       }
//    
//       public String getColumnName(int c){  
//          return col_names[c];
//       }
//    
//       public int getColumnCount(){ 
//          return col_names.length;
//       }
//
//       public Object getValueAt(int r, int c){  
//          FirstPMData m = (FirstPMData)v_pm.elementAt(r);
//          switch (c) {
//            case 0: return m.pmID;
//            case 1: return m.pmDescription;
//            case 2: return m.pmLimit;
//          }
//          return null;
//       }
//    
//       public int getRowCount(){  
//          return v_pm.size();
//       }
//       
//       public void setValueAt(Object value,int row,int col){
//          FirstPMData m_set = (FirstPMData)v_pm.elementAt(row);
//          switch (col) {
//            case 0:
//                  m_set.pmID = value.toString(); 
//                  break;
//            case 1:
//                  m_set.pmDescription = value.toString(); 
//                  break;
//            case 2:
//                  m_set.pmLimit = Integer.parseInt(value.toString());
//                  break;
//          }
//       }
//       
//       public boolean isCellEditable(int r,int c){  
//          if(c == 1)
//              return false; 
//          else
//              return true; 
//       }
//      
//       public Class getColumnClass(int c) {
//          return getValueAt(0, c).getClass();
//       }
//     }       
    
   
}
