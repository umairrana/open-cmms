package com.matrix.focus.register;

import com.matrix.components.TitleBar;
import com.matrix.focus.util.MPanel;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.ImageLibrary;
import com.matrix.components.MDataCombo;
import com.matrix.focus.inspection.MasterTaskDataCHK;
import com.matrix.focus.mdi.MDI;
import com.matrix.focus.readings.MasterTaskDataCM;
import com.matrix.focus.util.DataAssistantDialog;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.lang.Boolean;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Rectangle;
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
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

public class GUIRegisterConditionMonitoring extends MPanel{

    /**Get these from the MDI*/
    private MDI frame;
    private MessageBar messageBar;
    private TitleBar titlebar = new TitleBar();
    
    private JButton jButton1 = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));
    private JButton btnMachine = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton btnModel = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton btnCategory = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton btnBrand = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton btnDelete = new JButton(new ImageIcon(ImageLibrary.BUTTON_DELETE));
    private JButton btnNew = new JButton(new ImageIcon(ImageLibrary.BUTTON_NEW));


    private JPanel jPanel1 = new JPanel();
    private JScrollPane ps = new JScrollPane();
    public  static JTable table = new JTable();
    public  static JTable tableBCM = new JTable();
    public  MasterTaskDataCM data;
    public  ReadingListModel dataB;
    private JScrollPane jsSelected = new JScrollPane();
    private boolean started;
    private boolean mState;
    
    private JPanel jPanel2 = new JPanel();
    private JTextField txtBrand = new JTextField();
    private JTextField txtCategory = new JTextField();
    private JTextField txtModel = new JTextField();
    private JTextField txtMachine = new JTextField();
    private JLabel jLabel5 = new JLabel();
    private JLabel jLabel4 = new JLabel();
    private JLabel jLabel2 = new JLabel();
    private JLabel jLabel6 = new JLabel();
    private MDataCombo cmbCategory = new MDataCombo();
    
    private DataAssistantDialog dadBrand;
    private DataAssistantDialog dadCategory;
    private DataAssistantDialog dadModel;
    private DataAssistantDialog dadMachine;
    
    private String brandID ;
    private String categoryID;
    private String modelID;
    
    private Connection dbcon;
    private String machineNo = "";
    private int reading_id;
    private ReadingListModel readingListModel = new ReadingListModel();
    private String strSqlForRegisteredReadings = "SELECT r.category,r.description, mr.dont_use " +
             "FROM readingsmaster r,machine_reading mr " +
             "WHERE mr.Reading_ID = r.Reading_ID AND " +
             "mr.machine_no = ? " +
             "ORDER BY r.category";
    private JPanel jPanel3 = new JPanel();
    private JPanel jPanel4 = new JPanel();
    private JPanel jPanel5 = new JPanel();

    public GUIRegisterConditionMonitoring(DBConnectionPool pool, MDI frame, MessageBar msgBar){
        /**From the MDI*/
        /**New Database connection was taken to manage the transactions*/
        this.dbcon = pool.getConnection();
        this.frame = frame;
        this.messageBar = msgBar;
    
        titlebar.setTitle("Register Readings");
        titlebar.setDescription("The facility to register readings to assets.");
        titlebar.setImage(ImageLibrary.TITLE_CONDITION_MONITORING_REGISTER);
        
        try{
          jbInit();
          cmbCategory.getComboBox().setSelectedIndex(1);
        }
        catch(Exception e){
          e.printStackTrace();
        }
    }
    
    private void jbInit() throws Exception{
        this.setLayout(null);
        this.setSize(new Dimension(998, 606));
       
        titlebar.setBounds(new Rectangle(10, 10, 940, 70));
        
        started=false;
        mState=false;
        this.setLayout(null);
        this.setSize(new Dimension(991, 626));
        jButton1.setBounds(new Rectangle(795, 15, 30, 25));
        jPanel1.setBounds(new Rectangle(10, 320, 830, 280));
        jPanel1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        jPanel1.setLayout(null);
        ps.setBounds(new Rectangle(10, 20, 390, 175));
        btnDelete.setBounds(new Rectangle(795, 15, 30, 25));
        jPanel3.add(txtBrand, null);
        jPanel3.add(btnBrand, null);
        jPanel3.add(btnCategory, null);
        jPanel3.add(btnModel, null);
        jPanel3.add(btnMachine, null);
        jPanel3.add(jLabel5, null);
        jPanel3.add(jLabel4, null);
        jPanel3.add(jLabel2, null);
        jPanel3.add(jLabel6, null);
        jPanel3.add(txtCategory, null);
        jPanel3.add(txtModel, null);
        jPanel3.add(txtMachine, null);
        jPanel3.add(cmbCategory, null);
        btnCategory.setEnabled(false);
        btnModel.setEnabled(false);
        ps.getViewport().add(table, null);
        jPanel4.add(ps, null);
        jPanel2.add(jPanel4, null);
        jPanel2.add(jPanel3, null);
        jPanel2.add(btnNew, null);
        jPanel2.add(jButton1, null);
        this.add(jPanel2, null);
        jsSelected.getViewport().add(tableBCM, null);
        jPanel5.add(jsSelected, null);
        jPanel1.add(jPanel5, null);
        jPanel1.add(btnDelete, null);
        this.add(jPanel1, null);

        dataB = new ReadingListModel();
        tableBCM = new JTable();
        tableBCM.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        setReadingID();
                    }
                });
        data = new MasterTaskDataCM(dbcon, this);
        table = new JTable();
        jButton1.setFont(new Font("Tahoma", 1, 11));

        jsSelected.setBounds(new Rectangle(15, 20, 755, 230));

        jPanel2.setBounds(new Rectangle(10, 85, 830, 225));
        jPanel2.setLayout(null);
        jPanel2.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        txtBrand.setBounds(new Rectangle(115, 25, 180, 20));
        txtBrand.setEditable(false);
        txtCategory.setBounds(new Rectangle(115, 55, 180, 20));
        txtCategory.setEditable(false);
        txtModel.setBounds(new Rectangle(115, 85, 180, 20));
        txtModel.setEditable(false);
        txtMachine.setBounds(new Rectangle(115, 115, 180, 20));
        txtMachine.setEditable(false);
        btnMachine.setBounds(new Rectangle(305, 115, 30, 20));
        btnMachine.setSize(new Dimension(30, 20));
        btnMachine.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnMachine_actionPerformed(e);
                    }
                });
        btnModel.setBounds(new Rectangle(305, 85, 30, 20));
        btnModel.setSize(new Dimension(30, 20));
        btnModel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnModel_actionPerformed(e);
                    }
                });
        btnCategory.setBounds(new Rectangle(305, 55, 30, 20));
        btnCategory.setSize(new Dimension(30, 20));
        btnCategory.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnCategory_actionPerformed(e);
                    }
                });
        btnBrand.setBounds(new Rectangle(305, 25, 30, 20));
        btnBrand.setSize(new Dimension(30, 20));
        btnBrand.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnBrand_actionPerformed(e);
                    }
                });
        jLabel5.setText("Machine");
        jLabel5.setBounds(new Rectangle(10, 115, 85, 20));
        jLabel4.setText("Model");
        jLabel4.setBounds(new Rectangle(10, 85, 85, 20));
        jLabel2.setText("Category");
        jLabel2.setBounds(new Rectangle(10, 55, 65, 20));
        jLabel6.setText("Brand");
        jLabel6.setBounds(new Rectangle(10, 25, 60, 25));
        cmbCategory.setBounds(new Rectangle(10, 150, 285, 20));
        cmbCategory.setCaption("Reading Category");
        cmbCategory.setLblWidth(105);
        cmbCategory.setCmbWidth(180);
        jPanel3.setBounds(new Rectangle(5, 5, 355, 210));
        jPanel3.setLayout(null);
        jPanel3.setBorder(BorderFactory.createTitledBorder("Assets Selection"));
        jPanel4.setBounds(new Rectangle(375, 5, 415, 210));
        jPanel4.setLayout(null);
        jPanel4.setBorder(BorderFactory.createTitledBorder("Reading for the category"));
        jPanel5.setBounds(new Rectangle(5, 5, 785, 265));
        jPanel5.setLayout(null);
        jPanel5.setBorder(BorderFactory.createTitledBorder("Registered Readings for the machine"));
        btnNew.setBounds(new Rectangle(795, 45, 30, 25));
        btnNew.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnNew_actionPerformed(e);
                    }
                });
        cmbCategory.populate(dbcon, 
                             "SELECT DISTINCT Category FROM readingsmaster");
        jButton1.setEnabled(false);
        cmbCategory.getComboBox().addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        cmbCategory_actionPerformed(e);
                    }
                });
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


        // 1----------------------------------------   

        ps.getViewport().add(table, null);
        ps.repaint();
        table.setAutoCreateColumnsFromModel(false);
        table.setModel(data);
        table.addMouseListener(new MouseAdapter(){
              public void mouseClicked(MouseEvent e){
                  jButton1.setEnabled(true);
              }
          });
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

        MasterTaskDataCHK.ViewSavedCategory("Air", "readingsmaster");
        showTable();
        jsSelected.repaint();


        //-------------------------------------Started
        started = true;
        this.add(titlebar, null);

    }
    
    public void close(){
        try {
            dbcon.close();
        } catch (SQLException e) {
            e.toString();
        }
    }
    
    private void cmbCategory_actionPerformed(ActionEvent e){
    
        try
        {
        if(started)
        {
        //JOptionPane.showMessageDialog(null,"Category trg");
            MasterTaskDataCM.ViewSavedCategory(cmbCategory.getSelectedItem().toString().trim() ,"readingsmaster");
            showTable();
        }            
        }
        
        catch(Exception eCat)
        {
            JOptionPane.showMessageDialog(null,"ECAT"+eCat);
        }
        e.toString();
    
    }
    
    private void jButton1_actionPerformed(ActionEvent e){  //btnadd
        int row = table.getSelectedRow();
        if(row<0)
        {
            messageBar.setMessage("Select a Value ", "WARN");
        }
        else
        {    
            saveRegisterReadingsToMachine();   
            loadRegisteredReadings();
        
         }
         e.toString();
    }
    
    private void saveRegisterReadingsToMachine() {
        try
        {
          String sql="insert into machine_reading values(?,?,?)";
          Statement maxst=dbcon.createStatement();
          
          //--------------------------    get the reading ID
          ResultSet rsIID=maxst.executeQuery("select Reading_ID from readingsmaster " +
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
          messageBar.setMessage("Reading successfully registered for the machine no : " + txtMachine.getText(),"OK");
        }
        
        catch(Exception bcex){
          messageBar.setMessage("Selected Reading already registered !","WARN");
        }
    }
    
    private void loadRegisteredReadings(){
     
        try {
            String query = getPreparedQuery(txtMachine.getText().trim());
            readingListModel.emptyDataSet();
            readingListModel.setDataset(dbcon.createStatement().executeQuery(query));
            populateReadingTable();
        } 
        catch (SQLException e) {
            messageBar.setMessage("error in load details","ERROR");
        }
    }
    
    public void showTable(){
    ps.getViewport().add(table, null);
    ps.repaint();
    table.setAutoCreateColumnsFromModel(false);
    table.setModel(data); 
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
    table.repaint();
    }
    
    public void showTableBCM() {
        jsSelected.getViewport().add(tableBCM, null);
        jsSelected.repaint();
        tableBCM.setAutoCreateColumnsFromModel(false);
        tableBCM.setModel(dataB); 
        tableBCM.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        tableBCM.repaint();
    }
    
    private void btnDelete_actionPerformed(ActionEvent e){
    if (!(isExistingLogData())) {
    if (messageBar.showConfirmDialog(frame,"Are you sure you want to delete the selected inspection from the system?","Remove Item") == MessageBar.YES_OPTION){
          //delete
          
          String sql = "DELETE FROM machine_reading " +
          "WHERE Reading_ID = '" + reading_id + "'";
          Statement stmt;
          try{
            stmt = dbcon.createStatement();
            if(stmt.executeUpdate(sql) >0)
                messageBar.setMessage("Reading Unregistered","OK");
            stmt.close();
            loadRegisteredReadings();
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
        String sql="SELECT rl.Reading_ID " +
                    "FROM readings_log rl, preventive_maintenance_work_order w " +
                    "WHERE rl.Reading_ID = '" + reading_id + "' AND " +
                    "w.PM_Work_Order_ID = rl.SO_No AND " +
                    "w.Asset_ID = '" + txtMachine.getText().toString().trim() + "'";    
        
        Statement st=dbcon.createStatement();
        ResultSet rsmax=st.executeQuery(sql);
        
        while(rsmax.next())
        {
            if(JOptionPane.showConfirmDialog(null,"There are existing log entries for this corresponding Inspection item\n" +
            "Do you want to mark this inspection as don't use?\n\n" +
            "Inspections marked as \'don't use\' will be not included in log data","Remove Item",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE) == 0){
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
    
    private void setDontUse(Boolean value){
         String sql = "UPDATE machine_reading " +
         "SET dont_use = '" + value.TRUE + "'" +
         "WHERE Reading_ID = '" + reading_id + "' AND " +
         "machine_no = '" + txtMachine.getText().trim() + "'";
         Statement stmt;
         try{
           stmt = dbcon.createStatement();
           stmt.executeUpdate(sql) ;
           //    JOptionPane.showMessageDialog(null, "Inspection Removed","Delete",JOptionPane.INFORMATION_MESSAGE);
           stmt.close();
           loadRegisteredReadings();
         }
         
         catch(Exception er){
           stmt = null;
           er.printStackTrace();
         }
    }
    
    private void setReadingID(){
        try
        {
        //--------------------------    get the inspection ID
        Statement maxst=dbcon.createStatement();
        ResultSet rsIID=maxst.executeQuery("select Reading_ID from readingsmaster " +
        "WHERE Description='"+tableBCM.getValueAt(tableBCM.getSelectedRow(),2).toString().trim()+ "' AND " +
        "Category = '" + tableBCM.getValueAt(tableBCM.getSelectedRow(),1).toString().trim() + "'");
        //"Category  = '" + cmbCategory.getSelectedItem().toString() + "'");
        
        btnDelete.setEnabled(true);
        reading_id=0;
        while(rsIID.next())
        {
            reading_id =rsIID.getInt(1);
        }
        }
        
        catch(SQLException sqlex) {
            sqlex.toString();
        }
    }     

    private void populateReadingTable() {
        tableBCM.setModel(readingListModel);
        tableBCM.getColumnModel().getColumn(0).setPreferredWidth(15);
        tableBCM.setAutoCreateColumnsFromModel(false);
        tableBCM.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableBCM.setAutoCreateRowSorter(true);
        jsSelected.getViewport().add(tableBCM);
        tableBCM.repaint();
    }
    
    private String getPreparedQuery(String machineNo){
        //String s= strSqlForRegisteredInspections;
        
        StringTokenizer sT = new StringTokenizer(strSqlForRegisteredReadings,"?",false);
        String temp="";
        //while(sT.hasMoreTokens()){
            temp = sT.nextToken() + "'" + machineNo + "'";
            temp = temp + sT.nextToken();
            //temp = temp + "'" + category + "'";
            //temp = temp + sT.nextToken();
        //}
        return temp;
    } //prepare the query for view the 
    
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
                    loadRegisteredReadings();
                
                    //loadReadingsMaster();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
                
            }
        }
        

        dadMachine.setFirstColumnWidth(300);
        dadMachine.setSecondColumnWidth(500);
    }

    private void loadReadingsMaster() {
        for (int k = 0; k < 2 ; k++) {
            TableColumn column = new TableColumn(k, 10, null, null);
            table.addColumn(column);
            table.repaint();
            repaint();
        
        }
        
    }

  

    private void btnNew_actionPerformed(ActionEvent e) {
    e.toString();
    frame.menuItemConditionMonitoring.doClick();
    //mdi.menu
    
    }

    /*******************************************************************************/
    
    static class ReadingListData{
        public int pmID;     
        public String category;
        public String pmDescription;
        public boolean visible;
        
        public ReadingListData(int id, String cat, String des, boolean du) {
           pmID = id;
           category = cat;
           pmDescription = des;
           visible = du;
        }
    } 
    
    static class ReadingListModel extends AbstractTableModel{  
      private Vector v_pm = new Vector();
      public static String col_names[] = {"ID","Category", "Description","Don't use anymore"};
      private int r,c;
      private ResultSetMetaData meta;
      
      public ReadingListModel(){
      
      }
      
        public void setDataset(ResultSet rs){
            try{  
              meta = rs.getMetaData();
              r = rs.getRow();
              c = meta.getColumnCount();
                int x=1;
              while(rs.next()){
              //JOptionPane.showMessageDialog(null,x + " : " + rs.getString(1));
                  v_pm.addElement(new ReadingListData(x++, rs.getString(1), rs.getString(2), rs.getBoolean(3) ));
              }
            }
            catch(Exception e){  
               e.printStackTrace();
            }
        }
        
        public void setOtherDataset(ResultSet rs){
          try{  
            meta = rs.getMetaData();
            r = rs.getRow();
            c = meta.getColumnCount();
            int x = 0;
            while(rs.next()){
              v_pm.addElement(new ReadingListData(x++, rs.getString(1), rs.getString(2), rs.getBoolean(3) ));
            }
          }
          catch(Exception e){  
             e.printStackTrace();
          }
        }
        
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
              ReadingListData p = (ReadingListData) v_pm.elementAt(r);
              if(p.pmID == pmID){
                   in = true;
                   break;
              }
          }
          if(!in)
              v_pm.addElement(new ReadingListData(pmID,cat, des, true));
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
           ReadingListData m = (ReadingListData)v_pm.elementAt(r);
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
           ReadingListData m_set = (ReadingListData)v_pm.elementAt(row);
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
          if(c == 3)
            return true;
          else
            return false;
        }
        
        public Class getColumnClass(int c) {
           return getValueAt(0, c).getClass();
        }
    }
}
 