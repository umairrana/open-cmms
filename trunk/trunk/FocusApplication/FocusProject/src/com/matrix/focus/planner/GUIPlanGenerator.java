package com.matrix.focus.planner;

import com.matrix.components.MDatebox;
import com.matrix.focus.util.MPanel;
import com.matrix.focus.mdi.MDI;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.DataAssistantDialog;
import com.matrix.focus.util.ImageLibrary;
import com.matrix.focus.util.ViewOnlyRSTable;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class GUIPlanGenerator extends MPanel{

    private JButton btnMachine = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JTextField txtMachine = new JTextField();
    private JLabel labelMachine = new JLabel();
    private JButton btnModel = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JTextField txtModel = new JTextField();
    private JLabel labelModel = new JLabel();
    private JButton btnCategory = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JLabel labelCategory = new JLabel();
    private JTextField txtCategory = new JTextField();
    private JTextField txtBrand = new JTextField();
    private JButton btnBrand = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JLabel labelBrand = new JLabel();
    private JTable registeredPMs;
    private CustomTable plan = new CustomTable(new String[]{"PM Description","Scheduled Date","Scheduled Meter"});
    private JButton buttonEdit = new JButton(new ImageIcon(ImageLibrary.BUTTON_EDIT));
    private JButton buttonCancel = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
    private DataAssistantDialog dadBrand;
    private DataAssistantDialog dadCategory;
    private DataAssistantDialog dadModel;
    private DataAssistantDialog dadMachine;
    private Connection connection;
    private String brandID ;
    private String categoryID;
    private String modelID;
    private JPanel panelMachineDetails = new JPanel();
    private JScrollPane scrollPaneRegisteredPMs = new JScrollPane();
    private JPanel jPanel1 = new JPanel();
    private JCheckBox checkBoxServicing = new JCheckBox();
    private JLabel labelAverageRunPerDay = new JLabel();
    private JTextField textFieldAverageRunPerDay = new JTextField();
    private JRadioButton radioButtonDate = new JRadioButton();
    private JRadioButton radioButtonMeter = new JRadioButton();
    private JLabel labelEnding = new JLabel();
    private JTextField textFieldEndingMeter = new JTextField();
    private JScrollPane scrollPanePlan = new JScrollPane();
    private JButton buttonSave = new JButton(new ImageIcon(ImageLibrary.BUTTON_SAVE));
    private ButtonGroup bGroup = new ButtonGroup();
    private int AVERAGE_METER_PER_DAY=-1;
    private String END_DATE="";
    private String END_METER="";
    private boolean SERVICING = false;
    private MDI frame;
    private MessageBar messageBar;
    private MDatebox txtEndingDate = new MDatebox();
    private JPanel jPanel2 = new JPanel();
    private JPanel jPanel3 = new JPanel();
    private DBConnectionPool pool;

    public GUIPlanGenerator(DBConnectionPool pool, MDI frame, MessageBar msgBar){
        connection = pool.getConnection();
        this.frame = frame;
        this.messageBar = msgBar;
        this.pool = pool;
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setVisible(true);
    }

    private void jbInit() throws Exception {
    
        this.setLayout( null );
        plan.setAutoCreateRowSorter(true);
        plan.getColumnModel().getColumn(0).setPreferredWidth(300);
        plan.getColumnModel().getColumn(1).setPreferredWidth(60);
        plan.getColumnModel().getColumn(2).setPreferredWidth(60);
        this.setSize(new Dimension(1000, 616)); // 947 552
        btnMachine.setBounds(new Rectangle(280, 100, 30, 20));
        btnMachine.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnMachine_actionPerformed(e);
                    }
                });
        txtMachine.setBounds(new Rectangle(85, 100, 190, 20));
        txtMachine.setEditable(false);
        labelMachine.setText("Machine");
        labelMachine.setBounds(new Rectangle(30, 100, 85, 20));
        btnModel.setEnabled(false);
        btnModel.setBounds(new Rectangle(280, 75, 30, 20));
        btnModel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnModel_actionPerformed(e);
                    }
                });
        txtModel.setBounds(new Rectangle(85, 75, 190, 20));
        txtModel.setEditable(false);
        txtEndingDate.getButton().setEnabled(false);
        labelModel.setText("Model");
        labelModel.setBounds(new Rectangle(30, 75, 85, 20));
        btnCategory.setEnabled(false);
        btnCategory.setBounds(new Rectangle(280, 50, 30, 20));
        btnCategory.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnCategory_actionPerformed(e);
                    }
                });
        btnBrand.setBounds(new Rectangle(280, 25, 30, 20));
        btnBrand.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnBrand_actionPerformed(e);
                    }
                });
        txtCategory.setBounds(new Rectangle(85, 50, 190, 20));
        txtCategory.setEditable(false);
        txtBrand.setBounds(new Rectangle(85, 25, 190, 20));
        txtBrand.setEditable(false);
        labelCategory.setText("Category");
        labelCategory.setBounds(new Rectangle(30, 50, 65, 20));
        labelBrand.setText("Brand");
        labelBrand.setBounds(new Rectangle(30, 25, 60, 25));

        buttonEdit.setText("Edit");
        buttonEdit.setBounds(new Rectangle(690, 35, 110, 25));
        buttonEdit.setEnabled(false);
        buttonEdit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        buttonEdit_actionPerformed(e);
                    }
                });
        buttonCancel.setText("Cancel");
        buttonCancel.setBounds(new Rectangle(690, 95, 110, 25));
        buttonCancel.setEnabled(false);
        buttonCancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        buttonCancel_actionPerformed(e);
                    }
                });
        panelMachineDetails.setBounds(new Rectangle(25, 30, 345, 140));
        panelMachineDetails.setLayout(null);
        panelMachineDetails.setBorder(BorderFactory.createTitledBorder("Machine Details"));
        scrollPaneRegisteredPMs.setBounds(new Rectangle(10, 20, 755, 105));
        scrollPaneRegisteredPMs.setToolTipText("null");
        jPanel1.setBounds(new Rectangle(375, 30, 310, 140));
        jPanel1.setLayout(null);
        jPanel1.setBorder(BorderFactory.createTitledBorder("Service Details"));
        checkBoxServicing.setText("Servicing");
        checkBoxServicing.setBounds(new Rectangle(30, 25, 75, 20));
        checkBoxServicing.setEnabled(false);
        labelAverageRunPerDay.setText("Average Run Per Day");
        labelAverageRunPerDay.setBounds(new Rectangle(30, 50, 115, 20));
        textFieldAverageRunPerDay.setBounds(new Rectangle(160, 50, 80, 20));
        textFieldAverageRunPerDay.setEditable(false);
        radioButtonDate.setText("Date");
        radioButtonDate.setBounds(new Rectangle(75, 75, 80, 20));
        radioButtonDate.setEnabled(false);
        radioButtonMeter.setText(" Meter");
        radioButtonMeter.setBounds(new Rectangle(75, 100, 75, 20));
        radioButtonMeter.setEnabled(false);
        bGroup.add(radioButtonDate);
        bGroup.add(radioButtonMeter);
        radioButtonDate.setSelected(true);
        labelEnding.setText("Ending");
        labelEnding.setBounds(new Rectangle(30, 75, 40, 20));
        textFieldEndingMeter.setBounds(new Rectangle(160, 100, 80, 20));
        textFieldEndingMeter.setEditable(false);
        scrollPanePlan.setBounds(new Rectangle(10, 20, 755, 225));
        buttonSave.setText("Save");
        buttonSave.setBounds(new Rectangle(690, 65, 110, 25));
        buttonSave.setEnabled(false);
        buttonSave.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        buttonSave_actionPerformed(e);
                    }
                });
        txtEndingDate.setBounds(new Rectangle(160, 75, 115, 20));
        txtEndingDate.setCaption("Date");
        txtEndingDate.setLblWidth(0);
        txtEndingDate.getButton().addActionListener(
            new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        radioButtonDate.setSelected(true);    
                    }
                });
        jPanel2.setBounds(new Rectangle(25, 175, 775, 135));
        jPanel2.setLayout(null);
        jPanel2.setBorder(BorderFactory.createTitledBorder("Registered Maintenances"));
        jPanel3.setBounds(new Rectangle(25, 310, 775, 255));
        jPanel3.setLayout(null);
        jPanel3.setBorder(BorderFactory.createTitledBorder("Maintenance Schedule"));
        panelMachineDetails.add(txtBrand, null);
        panelMachineDetails.add(labelBrand, null);
        panelMachineDetails.add(labelCategory, null);
        panelMachineDetails.add(txtCategory, null);
        panelMachineDetails.add(btnBrand, null);
        panelMachineDetails.add(btnCategory, null);
        panelMachineDetails.add(labelModel, null);
        panelMachineDetails.add(txtModel, null);
        panelMachineDetails.add(btnModel, null);
        panelMachineDetails.add(labelMachine, null);
        panelMachineDetails.add(txtMachine, null);
        panelMachineDetails.add(btnMachine, null);
        jPanel1.add(txtEndingDate, null);
        jPanel1.add(textFieldEndingMeter, null);
        jPanel1.add(labelEnding, null);
        jPanel1.add(radioButtonMeter, null);
        jPanel1.add(radioButtonDate, null);
        jPanel1.add(textFieldAverageRunPerDay, null);
        jPanel1.add(labelAverageRunPerDay, null);
        jPanel1.add(checkBoxServicing, null);
        scrollPaneRegisteredPMs.getViewport().add(registeredPMs);
        jPanel2.add(scrollPaneRegisteredPMs, null);
        jPanel3.add(scrollPanePlan, null);
        this.add(jPanel3, null);
        this.add(jPanel2, null);
        this.add(buttonSave, null);
        this.add(jPanel1, null);
        this.add(panelMachineDetails, null);
        this.add(buttonEdit, null);
        this.add(buttonCancel, null);
    }
    
    private void btnMachine_actionPerformed(ActionEvent e) {
            e.toString();
            if(btnModel.isEnabled()){
                    dadMachine = new DataAssistantDialog(frame,
                                                      "Select Machine ",
                                                      "SELECT Asset_ID AS Machine, Model_ID AS Model " +
                                                      "FROM asset " + 
                                                      "WHERE Model_ID = '"+modelID+"' ",
                                                      connection);
                                                      
                    dadMachine.setLocationRelativeTo(btnMachine);  
                    dadMachine.setVisible(true);
                    
                    if(!dadMachine.getValue().equals("")){
                        txtMachine.setText(dadMachine.getValue());
                        scrollPaneRegisteredPMs.getViewport().removeAll();
                        try {
                            registeredPMs = setRegisteredPMTableColumnWidth(new ViewOnlyRSTable(connection.createStatement().executeQuery("SELECT Preventive_Maintenance_ID as ID,Description,Basis,CycleMeter,CycleTime,StartingMeter,IF(StartingDate='0000-00-00','0000-00-00',StartingDate) AS StartingDate FROM preventive_maintenance_register,preventive_maintenance WHERE Asset_ID='"+txtMachine.getText()+"' AND ID=Preventive_Maintenance_ID ")).getRSTable());
                            //plan = setPlanTableColumnWidth(new ViewOnlyRSTable(connection.createStatement().executeQuery("SELECT Description AS 'PM Description',Modified_Date as 'Modified Date',Modified_Meter as 'Modified Meter' FROM scheduled_preventive_maintenance PMS,preventive_maintenance PMM WHERE PMS.Asset_ID='"+txtMachine.getText()+"' AND  PMS.Preventive_Maintenance_ID=PMM.ID  ORDER BY Modified_Date ASC")).getRSTable());
                            setPlan();
                            scrollPaneRegisteredPMs.getViewport().add(registeredPMs);
                        }
                        catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                        //scrollPanePlan.getViewport().add(plan);
                        
                        fillFields();
                        
                    }
                    
                
            }else{
                dadMachine = new DataAssistantDialog(frame,
                                                  "Select Machine",
                                                  "SELECT Asset_ID AS Machine, Model_ID AS Model " +
                                                  "FROM asset m ",
                                                  connection);
                                                  
                dadMachine.setLocationRelativeTo(btnMachine);  
                dadMachine.setVisible(true);
                
                if(!dadMachine.getValue().equals("")){
                
                    txtMachine.setText(dadMachine.getValue());
                    scrollPaneRegisteredPMs.getViewport().removeAll();
                    try {
                        registeredPMs = setRegisteredPMTableColumnWidth(new ViewOnlyRSTable(connection.createStatement().executeQuery("SELECT Preventive_Maintenance_ID as ID,Description,Basis,CycleMeter,CycleTime,StartingMeter,IF(StartingDate='0000-00-00','0000-00-00',StartingDate) AS StartingDate FROM preventive_maintenance_register,preventive_maintenance WHERE Asset_ID='"+txtMachine.getText()+"' AND ID=Preventive_Maintenance_ID")).getRSTable());
                       // plan = setPlanTableColumnWidth(new ViewOnlyRSTable(connection.createStatement().executeQuery("SELECT Description AS 'PM Description',Modified_Date as 'Modified Date',Modified_Meter as 'Modified Meter' FROM scheduled_preventive_maintenance PMS,preventive_maintenance PMM WHERE PMS.Asset_ID='"+txtMachine.getText()+"' AND  PMS.Preventive_Maintenance_ID=PMM.ID  ORDER BY Modified_Date ASC")).getRSTable());
                       setPlan();
                        scrollPaneRegisteredPMs.getViewport().add(registeredPMs);
                    }
                    catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    //scrollPanePlan.getViewport().add(plan);
                    btnCategory.setEnabled(false);
                    btnModel.setEnabled(false);
                    try{
                        PreparedStatement pS = connection.prepareStatement("SELECT " +
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
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }
                   
                    fillFields();
                }
            }
       
        dadMachine.setFirstColumnWidth(300);
        dadMachine.setSecondColumnWidth(500);
    }

    private void setPlan() throws SQLException {
    
        try {
            scrollPanePlan.getViewport().remove(plan);
        }
        catch (Exception e) {
            e.toString();
        }
        
        String sql = "SELECT Description AS 'PM Description',Modified_Date as 'Scheduled Date',Modified_Meter as 'Scheduled Meter' FROM scheduled_preventive_maintenance PMS,preventive_maintenance PMM WHERE PMS.Asset_ID='"+txtMachine.getText()+"' AND  PMS.Preventive_Maintenance_ID=PMM.ID  ORDER BY Modified_Date ASC";
        ResultSet r = connection.createStatement().executeQuery(sql);
        plan.deleteAll();
        
            for(int i = 0; r.next() ; i++){
                plan.addRow();
                plan.setValueAt(r.getString(1),i,0);
                plan.setValueAt(r.getString(2),i,1);
                plan.setValueAt(Integer.valueOf(r.getString(3)),i,2);
            }
        scrollPanePlan.getViewport().add(plan);
    }
    
    private void fillFields() {
    
        try{
            PreparedStatement pS = connection.prepareStatement("SELECT Planned,AverageMeterPerDay,IF(PlanEndDate='0000-00-00','0000-00-00',PlanEndDate) AS PlanEndDate, PlanEndMeter FROM Asset WHERE Asset_ID=?");
            pS.setString(1,txtMachine.getText());
            ResultSet r = pS.executeQuery();
            r.first();
            if(r.getString("Planned").equals("true")){
                checkBoxServicing.setSelected(true);
                SERVICING = true;
            }
            else{
                checkBoxServicing.setSelected(false);
                SERVICING = false;
                
            }
                                        
            textFieldAverageRunPerDay.setText(r.getInt("AverageMeterPerDay")+"");
            AVERAGE_METER_PER_DAY = r.getInt("AverageMeterPerDay");
            
            if(r.getInt("PlanEndMeter")!=0){
                radioButtonMeter.setSelected(true);
                textFieldEndingMeter.setText(r.getInt(4)+"");
                END_METER = r.getInt("PlanEndMeter")+"";
            }
            else{
                radioButtonDate.setSelected(true);
                txtEndingDate.getButton().setEnabled(false);
                txtEndingDate.setInputText(r.getString("PlanEndDate"));
                END_DATE = r.getString(3);
            }
            buttonEdit.setEnabled(true);
            btnMachine.setEnabled(false);
            btnBrand.setEnabled(false);
            btnCategory.setEnabled(false);
            btnModel.setEnabled(false);
            buttonCancel.setEnabled(true);
            
        }
        catch(Exception ex){
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
                                                  connection);
                                                  
               
                dadModel.setLocationRelativeTo(btnCategory);         
                dadModel.setFirstColumnWidth(300);
                dadModel.setVisible(true);
                modelID = dadModel.getValue();
                txtModel.setText(modelID);
                txtMachine.setText("");
                try{
                    scrollPaneRegisteredPMs.getViewport().remove(registeredPMs);
                    scrollPanePlan.getViewport().remove(plan);
                }
                catch(Exception ex){
                    ex.toString();
                }
                buttonSave.setEnabled(false);
                buttonEdit.setEnabled(false);
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
                                                      connection);
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
                    scrollPaneRegisteredPMs.getViewport().remove(registeredPMs);
                    scrollPanePlan.getViewport().remove(plan);
                    }
                    catch(Exception ex){
                        ex.toString();
                    }
                    buttonSave.setEnabled(false);
                    buttonEdit.setEnabled(false);
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
                                           connection);
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
                scrollPaneRegisteredPMs.getViewport().remove(registeredPMs);
                scrollPanePlan.getViewport().remove(plan);
            }
            catch(Exception ex){
                ex.toString();
            }
            buttonSave.setEnabled(false);
            buttonEdit.setEnabled(false);
            btnCategory.setEnabled(true);
            btnModel.setEnabled(false);
            buttonCancel.setEnabled(true);
        }
    }

    private void buttonEdit_actionPerformed(ActionEvent e) {
        e.toString();
        checkBoxServicing.setEnabled(true);
        textFieldAverageRunPerDay.setEditable(true);
        txtEndingDate.getButton().setEnabled(true);
        textFieldEndingMeter.setEditable(true);
        radioButtonDate.setEnabled(true);
        radioButtonMeter.setEnabled(true);
        buttonSave.setEnabled(true);
        buttonEdit.setEnabled(false);
        if(radioButtonDate.isSelected()){
            txtEndingDate.getButton().setEnabled(true);
        }
        else{
            textFieldEndingMeter.setEditable(true);
        }
    }
    
    private void buttonCancel_actionPerformed(ActionEvent e) {
    
        e.toString();
        btnBrand.setEnabled(true);
        txtBrand.setText("");
        txtCategory.setText("");
        txtModel.setText("");
        txtMachine.setText("");
        btnMachine.setEnabled(true);
        try {
            scrollPaneRegisteredPMs.getViewport().remove(registeredPMs);
            scrollPanePlan.getViewport().remove(plan);
        }
        catch (Exception ex) {
            ex.toString();
        }
        checkBoxServicing.setSelected(false);
        checkBoxServicing.setEnabled(false);
        textFieldAverageRunPerDay.setEditable(false);
        textFieldAverageRunPerDay.setText("");
        txtEndingDate.setInputText("");
        txtEndingDate.getButton().setEnabled(false);
        textFieldEndingMeter.setText("");
        textFieldEndingMeter.setEditable(false);
        radioButtonDate.setEnabled(false);
        radioButtonMeter.setEnabled(false);
        radioButtonDate.setSelected(false);
        radioButtonMeter.setSelected(false);
        buttonEdit.setEnabled(false);
        buttonSave.setEnabled(false);
        buttonCancel.setEnabled(false);
        btnBrand.setEnabled(true);
        btnMachine.setEnabled(true);
        btnCategory.setEnabled(false);
        btnModel.setEnabled(false);
        AVERAGE_METER_PER_DAY = -1;
        END_DATE = "";
        END_METER = "";
        SERVICING = false;
        
        connection = pool.getConnection();
        
    }
    
    private JTable setRegisteredPMTableColumnWidth(JTable table){
        
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        for(int i=2; i<table.getColumnCount(); i++){
            table.getColumnModel().getColumn(i).setPreferredWidth(85);;
        }
        table.getColumnModel().getColumn(4).setPreferredWidth(90);
        return table;
    }
    
    private JTable setPlanTableColumnWidth(JTable table){
        table.getColumnModel().getColumn(0).setPreferredWidth(300);;
        for(int i=1; i<table.getColumnCount(); i++){
            table.getColumnModel().getColumn(i).setPreferredWidth(105);;
        }
        
        return table;
    }

    private void buttonSave_actionPerformed(ActionEvent e) {
        e.toString();
        Connection conn = pool.getConnection();
        
        try{
            conn.setAutoCommit(false);
            PlanningAsset planningAsset = new PlanningAsset(txtMachine.getText(),conn);
            
            if(checkBoxServicing.isSelected() ^ SERVICING){
                changeServicingCheckBox(planningAsset);
                SERVICING = true;
            }
            
            if(planningAsset.hasRegistedMeterBasedPlanningPMs()){
                int newAverageRunPerDay = getNewAverageRunPerDay();
                if((AVERAGE_METER_PER_DAY - newAverageRunPerDay) != 0){
                    changeAverageRunPerDayTextField(planningAsset,newAverageRunPerDay);
                }
            }
            
           if(radioButtonMeter.isSelected()){
                validateEndingMeter();
           }
           else{
                validateEndingDate();
           }
            
           if(
           (radioButtonDate.isSelected() && !txtEndingDate.getInputText().equals(END_DATE))
           ||
           (radioButtonMeter.isSelected() && !textFieldEndingMeter.getText().equals(END_METER))
           ){
               changeEndingDateOrMeter(planningAsset);
           }
           /**End of plan modifing*/
           conn.commit();
           
            try {
                registeredPMs = setRegisteredPMTableColumnWidth(new ViewOnlyRSTable(connection.createStatement().executeQuery("SELECT Preventive_Maintenance_ID as ID,Description,Basis,CycleMeter,CycleTime,StartingMeter,IF(StartingDate='0000-00-00','0000-00-00',StartingDate) AS StartingDate FROM preventive_maintenance_register,preventive_maintenance WHERE Asset_ID='"+txtMachine.getText()+"' AND ID=Preventive_Maintenance_ID ")).getRSTable());
                //plan = setPlanTableColumnWidth(new ViewOnlyRSTable(connection.createStatement().executeQuery("SELECT Description AS 'PM Description',Modified_Date as 'Modified Date',Modified_Meter as 'Modified Meter' FROM scheduled_preventive_maintenance PMS,preventive_maintenance PMM WHERE PMS.Asset_ID='"+txtMachine.getText()+"' AND  PMS.Preventive_Maintenance_ID=PMM.ID  ORDER BY Modified_Date ASC")).getRSTable());
                setPlan();
                scrollPaneRegisteredPMs.getViewport().add(registeredPMs);
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
            //scrollPanePlan.getViewport().add(plan);
            
           messageBar.setMessage("Scheduling modification saved successfully.","OK");
        } 
        catch(Exception ex){
            messageBar.setMessage(ex.getMessage(),"ERROR");
            ex.printStackTrace();
            try{
                conn.rollback();
            }
            catch(Exception exp){
                exp.printStackTrace();
            }
        } 
    }
    
    private void changeServicingCheckBox(PlanningAsset planningAsset) throws Exception{

        //Servicing = TRUE
        if(checkBoxServicing.isSelected()){
            if(planningAsset.hasRegistedPMs()){
                if(planningAsset.hasRegistedPlanningPMs()){
                    planningAsset.planOrExtend();
                }
                else{
                    throw new Exception("Go to PM Register and enable PM's");
                }
            }
            else{
                throw new Exception("Go to PM Register and register some PM's for this Machine");
            }
        }
        //Servicing = FALSE
        else{
            if(planningAsset.hasPlannedPMs()){
                if(planningAsset.hasPendingWorkOrders()){
                    throw new Exception("This Machine has pending jobs.\nCannot stop servicing.");
                }
                else{
                    //Delete all plan entries of PMs from their last_done_date
                    planningAsset.deletePlan();
                    planningAsset.stopPlanningAllAssetPMs();
                }
                
            }    
        }
        planningAsset.setServicing(checkBoxServicing.isSelected());
    }
    
    private void changeAverageRunPerDayTextField(PlanningAsset planningAsset,int new_avg)throws Exception{
    
        if(AVERAGE_METER_PER_DAY == 0 && new_avg == 0){            
            if(planningAsset.hasRegistedMeterBasedPlanningPMs()){
                throw new Exception("There are active meter reading based pms.\nMeter average can not be zero.");
            }
            
        }
        else if((AVERAGE_METER_PER_DAY - new_avg)!=0){
            if(planningAsset.hasRegistedMeterBasedPlanningPMs()){
                planningAsset.setAverageMeterPerDay(new_avg);
                planningAsset.reschedule();
                AVERAGE_METER_PER_DAY = new_avg;
            }
            else{
                throw new Exception("There are no meter based PM's registered for this machine");
            }
        }
    }
    
    private void changeEndingDateOrMeter(PlanningAsset planningAsset) throws Exception{
        
        String new_end_date = null;
        int new_end_meter   = 0;
        
       if(radioButtonDate.isSelected()){
            new_end_date = txtEndingDate.getInputText();
            new_end_meter = 0;
       }
       else{
            new_end_meter = Integer.parseInt(textFieldEndingMeter.getText());
            new_end_date = "0000-00-00";
       }
       
        if(!END_DATE.isEmpty()){
            if(radioButtonDate.isSelected()){ //date to date
                planningAsset.setPlanEndDate(new_end_date);
                planningAsset.setPlanEndMeter(0);
                changeEndingDate(planningAsset,new_end_date);
            }
            else{ // date - meter
                new_end_date = planningAsset.getEndingDate(new_end_meter);
                planningAsset.setPlanEndMeter(new_end_meter);
                planningAsset.setPlanEndDate("0000-00-00");
                changeEndingDate(planningAsset,new_end_date);
            }
        }
          else{
                if(radioButtonMeter.isSelected()){ // meter - meter
                    new_end_date = planningAsset.getEndingDate(new_end_meter);
                    planningAsset.setPlanEndMeter(new_end_meter);
                    planningAsset.setPlanEndDate("0000-00-00");
                    changeEndingDate(planningAsset,new_end_date);
                }
                else{ // meter - date
                    planningAsset.setPlanEndMeter(0);
                    planningAsset.setPlanEndDate(new_end_date);
                    changeEndingDate(planningAsset,new_end_date);  
                }
          }
    }

    private void changeEndingDate(PlanningAsset planningAsset,String newEndingDate) throws Exception {
        ResultSet rec = connection.createStatement().executeQuery("SELECT DATEDIFF('"+newEndingDate+"','"+END_DATE+"')");
        rec.first();
        int diff = rec.getInt(1);
        if(diff>0){//EXTEND
            planningAsset.extend();        
        }
        else{//DELETE
            planningAsset.curtail();
        }
    }
    
    private int getNewAverageRunPerDay() throws Exception{
        int new_average = -1;
        try{
            new_average = Integer.parseInt(textFieldAverageRunPerDay.getText());
            if(new_average<0){
                throw new NumberFormatException("Average run per day cannot be negative");
            }
            return new_average;
        }
        catch(NumberFormatException nfe){
            nfe.toString();
            throw new NumberFormatException("invalid average run per day");
        }
        
        
    }
    
    private void validateEndingMeter() throws Exception{
        try{
            int meter = Integer.parseInt(textFieldEndingMeter.getText());
            if(meter<0){
                throw new Exception("Invalid ending meter");
            }
        } 
        catch(Exception ex)  {
            throw new Exception("Invalid ending meter");
        }
    }

    private void validateEndingDate() throws Exception {
        if(txtEndingDate.getInputText().equals("")){
            throw new Exception("Invalid ending date");
        }
    }

    public class CustomTable extends JTable{

        private ItemTableModel model = null ;
        
        public CustomTable(String [] columnNames) {
            model = new ItemTableModel(columnNames);
            this.setModel(model);
            for(int c=0;c<this.getColumnCount();c++){
              this.getColumnModel().getColumn(c).setHeaderRenderer(new DefaultTableCellRenderer(){
                          public Component getTableCellRendererComponent(JTable table, 
                                                                         Object value, 
                                                                         boolean isSelected, 
                                                                         boolean hasFocus, 
                                                                         int row, 
                                                                         int column) {
                              //this.setBackground(scrollPaneItemTable.getBackground());
                              this.setText(value.toString());
                              this.setBorder(BorderFactory.createEtchedBorder());
                              return this;
                          }
                      });
            }
           
            this.getTableHeader().setReorderingAllowed(false);
            //this.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new ItemCombo()));
            
           // setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            this.setGridColor(Color.lightGray);
            try{
            //this.getColumnModel().getColumn(1).setPreferredWidth(300);
            }
            catch(Exception e){
                e.toString();
            }
            
            addRow();
            
        }
        
        public void setFirstColumnWidth(int w){
            resizeColumn(0,w);
        }
        
        public void setSecondColumnWidth(int w){
             resizeColumn(1,w);
        }
        
        public void setThirdColumnWidth(int w){
            resizeColumn(2,w);
        }
        
        public void setFourthColumnWidth(int w){
            resizeColumn(3,w);
        }
        
        public void setFifthColumnWidth(int w){
            resizeColumn(4,w);
        }
        
        private void resizeColumn(int col, int size){
        
              this.getColumnModel().getColumn(col).setPreferredWidth(size);
              if(size==0){
                this.getColumnModel().getColumn(col).setMaxWidth(0);
                this.getColumnModel().getColumn(col).setMinWidth(0);
                this.getColumnModel().getColumn(col).setPreferredWidth(0);
         }
        }
        public void addRow(){
            model.addRow();
            this.tableChanged(new TableModelEvent(model)); 
        }
        
        public void deleteRow(int i){
            model.deleteRow(i);
            this.tableChanged(new TableModelEvent(model));        
        } 
        
        public void deleteAll(){
            model.deleteAll();
            this.tableChanged(new TableModelEvent(model));        
        }
        
        private class ItemTableModel extends AbstractTableModel{
        
          private String [] colNames; //= ;
          private Object [][] valueArray = null;
          private Object [][] tempArray = null;
          
          public ItemTableModel(String [] columnNames) {
              colNames=columnNames;            
              valueArray = new Object[0][colNames.length];
          }
          
          public Class getColumnClass(int col){
                try {
                    return getValueAt(0,col).getClass();
                }
                catch (Exception e) {
                    return "".getClass();
                }
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
              /*if(col==3){
                  try{
                      value = Integer.parseInt(value.toString());
                  }
                  catch(Exception e){
                      JOptionPane.showMessageDialog(null,"Invalid Qty");
                      value = "";
                  }
              }*/
              valueArray[row][col] = value;
          }
              
          /*public boolean isCellEditable(int r,int c){
              return (c==3 || c==0);
          }*/
          
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
    
    
}
