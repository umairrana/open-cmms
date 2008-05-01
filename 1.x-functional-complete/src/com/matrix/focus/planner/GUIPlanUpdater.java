package com.matrix.focus.planner;

import com.matrix.components.MDatebox;
import com.matrix.components.TitleBar;
import com.matrix.focus.util.MPanel;
import com.matrix.focus.mdi.MDI;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.DataAssistantDialog;
import com.matrix.focus.util.ImageLibrary;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class GUIPlanUpdater extends MPanel{

    private JTextField textFieldCustomer = new JTextField();
    private JTextField textFieldMachine = new JTextField();
    private JTextField textFieldCurrentLimit = new JTextField();
    private JButton buttonCustomer = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton buttonMachine = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JLabel labelCustomer = new JLabel();
    private JLabel LabelMachine = new JLabel();
    private JLabel labelCurentLimit = new JLabel();
    private JButton buttonReSchedulePlan = new JButton(new ImageIcon(ImageLibrary.BUTTON_CRASH));
    private JScrollPane scrollPanePlan = new JScrollPane();
    private CustomTable plan = new CustomTable(new String[]{"Description","Scheduled Date","Scheduled Meter"});
    private Connection connection ;
    private int customerID = 0; 
    private String machineID = "";
    private JButton buttonCancel = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
    private JButton buttonEdit = new JButton(new ImageIcon(ImageLibrary.BUTTON_EDIT));
    private MDI frame;
    private MessageBar messageBar;
    private JPanel jPanel1 = new JPanel();
    private DBConnectionPool pool;
    private TitleBar titlebar = new TitleBar();
    private MDatebox txtCurrentDate = new MDatebox();
    private JPanel jPanel2 = new JPanel();

    public GUIPlanUpdater(DBConnectionPool pool, MDI frame, MessageBar msgBar) {
        connection = pool.getConnection();
        this.frame = frame;
        this.messageBar = msgBar;
        this.pool = pool;
        try {
            titlebar.setTitle("Plan Updater");
            titlebar.setDescription("The facility to update plan.");
            titlebar.setImage(ImageLibrary.TITLE_PREVENTIVE_MAINTENANCE);
            
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setVisible(true);
    }

    private void jbInit() throws Exception {
        
        setLayout(null);
        plan.setAutoCreateRowSorter(true);
        plan.getColumnModel().getColumn(0).setPreferredWidth(300);
        plan.getColumnModel().getColumn(1).setPreferredWidth(60);
        plan.getColumnModel().getColumn(2).setPreferredWidth(60);
        this.setSize(new Dimension(948, 527));
        textFieldCustomer.setBounds(new Rectangle(110, 30, 390, 20));
        textFieldCustomer.setEditable(false);
        textFieldMachine.setBounds(new Rectangle(110, 55, 185, 20));
        textFieldMachine.setEditable(false);
        textFieldCurrentLimit.setBounds(new Rectangle(110, 80, 80, 20));
        textFieldCurrentLimit.setEditable(false);
        txtCurrentDate.setInputText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        buttonCustomer.setBounds(new Rectangle(505, 30, 30, 20));
        buttonCustomer.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        buttonCustomer_actionPerformed(e);
                    }
                });
        buttonMachine.setBounds(new Rectangle(300, 55, 30, 20));
        buttonMachine.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        buttonMachine_actionPerformed(e);
                    }
                });
        labelCustomer.setText("Customer");
        labelCustomer.setBounds(new Rectangle(30, 30, 120, 20));
        LabelMachine.setText("Machine");
        LabelMachine.setBounds(new Rectangle(30, 55, 125, 20));
        labelCurentLimit.setText("Current Limit");
        labelCurentLimit.setBounds(new Rectangle(30, 80, 120, 20));
        buttonReSchedulePlan.setText("Reschedule Plan");
        buttonReSchedulePlan.setBounds(new Rectangle(620, 120, 130, 25));
        buttonReSchedulePlan.setEnabled(false);
        buttonReSchedulePlan.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        buttonReSchedulePlan_actionPerformed(e);
                    }
                });
        scrollPanePlan.setBounds(new Rectangle(10, 20, 585, 260));
        buttonCancel.setText("Cancel");
        buttonCancel.setBounds(new Rectangle(620, 150, 130, 25));
        buttonCancel.setEnabled(false);
        titlebar.setBounds(new Rectangle(10, 10, 745, 70));
        txtCurrentDate.setBounds(new Rectangle(30, 105, 165, 20));
        txtCurrentDate.setLblWidth(80);
        txtCurrentDate.setCaption("Current Date");
        jPanel2.setBounds(new Rectangle(10, 230, 605, 290));
        jPanel2.setBorder(BorderFactory.createTitledBorder("Maintenance Schedule"));
        jPanel2.setLayout(null);
        jPanel2.add(scrollPanePlan, null);
        this.add(jPanel2, null);
        this.add(titlebar, null);
        this.add(jPanel1, null);
        add(buttonEdit, null);
        add(buttonCancel, null);
        add(buttonReSchedulePlan, null);
        buttonCancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        buttonCancel_actionPerformed(e);
                    }
                });
        buttonEdit.setText("Edit");
        buttonEdit.setBounds(new Rectangle(620, 90, 130, 25));
        buttonEdit.setEnabled(false);
        buttonEdit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        buttonEdit_actionPerformed(e);
                    }
                });
        jPanel1.setBounds(new Rectangle(10, 85, 605, 145));
        jPanel1.setBorder(BorderFactory.createTitledBorder("Asset Details"));
        jPanel1.setLayout(null);
        jPanel1.add(txtCurrentDate, null);
        jPanel1.add(labelCustomer, null);
        jPanel1.add(labelCurentLimit, null);
        jPanel1.add(LabelMachine, null);
        jPanel1.add(buttonMachine, null);
        jPanel1.add(buttonCustomer, null);
        jPanel1.add(textFieldCurrentLimit, null);
        jPanel1.add(textFieldMachine, null);
        jPanel1.add(textFieldCustomer, null);
        setVisible(true);
    }
    
    private void buttonCustomer_actionPerformed(ActionEvent e) {
        e.toString();
        DataAssistantDialog dADCustomer = new DataAssistantDialog(
                                                frame,
                                                "Select Customer",
                                                
                                                "SELECT " +
                                                    "C.Division_ID AS ID, " +
                                                    "C.Name AS Name " +
                                                "FROM " +
                                                    "Division C " +
                                                "WHERE " +
                                                "EXISTS " +
                                                "( " +
                                                    "SELECT " +
                                                        "MM.Asset_ID " +
                                                    "FROM " +
                                                        "asset MM " +
                                                    "WHERE " +
                                                        "C.Division_ID = MM.Division_ID " +
                                                        "AND " +
                                                        "EXISTS " +
                                                        "(" +
                                                            "SELECT " +
                                                                "* " +
                                                            "FROM " +
                                                                "Scheduled_Preventive_Maintenance PMS " +
                                                            "WHERE " +
                                                                "PMS.Asset_ID=MM.Asset_ID " +
                                                        ") " +
                                                        "AND " +
                                                        "EXISTS " +
                                                        "(" +
                                                            "SELECT " +
                                                                "* " +
                                                            "FROM " +
                                                                "Preventive_Maintenance_Register MPM " +
                                                            "WHERE " +
                                                                "MPM.Asset_ID=MM.Asset_ID " +
                                                                "AND " +
                                                                "MPM.Basis='Meter Reading' " +
                                                                "AND " +
                                                                "MPM.Plan='true'" +
                                                        ")" +
                                                ")"
                                                ,connection);
        dADCustomer.setLocationRelativeTo(buttonCustomer);  
        dADCustomer.setVisible(true);
        
        if(!dADCustomer.getValue().equals("")){
            customerID = Integer.parseInt(dADCustomer.getValue());
            textFieldCustomer.setText(dADCustomer.getDescription());
            machineID = "";
            textFieldMachine.setText("");
            buttonCancel.setEnabled(true);
            }
    }

    private void buttonMachine_actionPerformed(ActionEvent e) {
        e.toString();
        DataAssistantDialog dADMachine = null;
        
        if(customerID == 0){
            dADMachine = new DataAssistantDialog(frame,
                                                "Select Machine",
                                                
                                                "SELECT " +
                                                    "MM.asset_id AS ID " +
                                                "FROM " +
                                                    "asset MM " +
                                                "WHERE " +
                                                "EXISTS " +
                                                "(" +
                                                    "SELECT " +
                                                        "* " +
                                                    "FROM " +
                                                        "Scheduled_Preventive_Maintenance PMS " +
                                                    "WHERE " +
                                                        "PMS.asset_id=MM.asset_id" +
                                                ") " +
                                                "AND " +
                                                "EXISTS " +
                                                "(" +
                                                    "SELECT " +
                                                        "* " +
                                                    "FROM " +
                                                        "Preventive_Maintenance_Register MPM " +
                                                    "WHERE " +
                                                        "MPM.asset_id=MM.asset_id " +
                                                        "AND " +
                                                        "MPM.basis='Meter Reading' " +
                                                        "AND " +
                                                        "MPM.Plan='true'" +
                                                ")"
                                                
                                                ,connection);
            dADMachine.setFirstColumnWidth(300);                                    
            dADMachine.setLocationRelativeTo(buttonMachine);  
            dADMachine.setVisible(true);
        }
        else{
            dADMachine = new DataAssistantDialog(frame,
                                                "Select Machine",
                                                
                                                "SELECT " +
                                                    "MM.asset_id " +
                                                "FROM " +
                                                    "asset MM " +
                                                "WHERE " +
                                                    "MM.division_ID="+ customerID+" " +
                                                    "AND " +
                                                    "EXISTS " +
                                                    "(" +
                                                        "SELECT " +
                                                            "* " +
                                                        "FROM " +
                                                            "Scheduled_Preventive_Maintenance PMS " +
                                                        "WHERE " +
                                                            "PMS.asset_id=MM.asset_id" +
                                                    ") " +
                                                    "AND " +
                                                    "EXISTS " +
                                                    "(" +
                                                        "SELECT " +
                                                            "* " +
                                                        "FROM " +
                                                            "Preventive_Maintenance_Register MPM " +
                                                        "WHERE " +
                                                            "MPM.asset_id=MM.asset_id " +
                                                            "AND " +
                                                            "MPM.basis='Meter Reading' " +
                                                            "AND " +
                                                            "MPM.Plan='true'" +
                                                    ")"
                                                
                                                ,connection);
            dADMachine.setFirstColumnWidth(300);
            dADMachine.setLocationRelativeTo(buttonMachine);  
            dADMachine.setVisible(true);
        }
        
    
        if(!dADMachine.getValue().equals("")){
            machineID = dADMachine.getValue();
            textFieldMachine.setText(dADMachine.getValue());
            buttonCustomer.setEnabled(false);
            buttonMachine.setEnabled(false);
            
            if(customerID == 0){
                try{
                    ResultSet r = connection.prepareStatement("SELECT " +
                                                            "C.Name," +
                                                            "C.Division_ID " +
                                                        "FROM " +
                                                            "asset M," +
                                                            "division C " +
                                                        "WHERE " +
                                                            "M.asset_id='"+machineID+"' " +
                                                            "AND " +
                                                            "M.Division_ID=C.division_ID"
                                                        ).executeQuery();
                    r.first();
                    textFieldCustomer.setText(r.getString(1));
                    customerID = r.getInt(2);
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }
            
            buttonEdit.setEnabled(true);
            buttonCancel.setEnabled(true);
            
            try {
                setTable();
            
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        
    }
    
    private void setTable() throws SQLException {
    
        try {
            scrollPanePlan.getViewport().remove(plan);
        }
        catch (Exception e) {
            
        }
        String sql = "SELECT " +
                    "Description ," +
                    "Modified_Date AS 'Scheduled Date'," +
                    "Modified_Meter AS 'Scheduled Meter' " +
                    "FROM " +
                    "Scheduled_Preventive_Maintenance PMS," +
                    "preventive_maintenance PMM " +
                    "WHERE " +
                    "PMS.asset_id='"+textFieldMachine.getText()+"' " +
                    "AND " +
                    "PMS.Preventive_Maintenance_ID=PMM.id " +
                    "ORDER BY " +
                    "Modified_Date";
                    
            ResultSet r = connection.createStatement().executeQuery(sql);        
            plan.deleteAll();
            
            
            try {
                for(int i = 0; r.next() ; i++){
                    plan.addRow();
                    plan.setValueAt(r.getString(1),i,0);
                    plan.setValueAt(r.getString(2),i,1);
                    plan.setValueAt(Integer.valueOf(r.getString(3)),i,2);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            
            
           /* plan = setPlanTableColumnWidth(new ViewOnlyRSTable(connection.createStatement().executeQuery("SELECT " +
                                                                                                    "Description ," +
                                                                                                    "Modified_Date AS 'Scheduled Date'," +
                                                                                              //      "IF(Done_Date='0000-00-00','0000-00-00',Done_Date) AS 'Done Date'," +
                                                                                                    "Modified_Meter AS 'Scheduled Meter' " +
                                                                                                    "FROM " +
                                                                                                    "Scheduled_Preventive_Maintenance PMS," +
                                                                                                    "preventive_maintenance PMM " +
                                                                                                    "WHERE " +
                                                                                                    "PMS.asset_id='"+textFieldMachine.getText()+"' " +
                                                                                                    "AND " +
                                                                                                    "PMS.Preventive_Maintenance_ID=PMM.id " +
                                                                                                    "ORDER BY " +
                                                                                                    "Modified_Date"
                                                                                                    )).getRSTable());*/
            scrollPanePlan.getViewport().add(plan);
    }
    
    private void buttonCancel_actionPerformed(ActionEvent e) {
        e.toString();
        buttonCustomer.setEnabled(true);
        buttonMachine.setEnabled(true);
        textFieldCustomer.setText("");
        textFieldMachine.setText("");
        customerID = 0;
        machineID = "";
        buttonCancel.setEnabled(false);
        buttonEdit.setEnabled(false);
        buttonReSchedulePlan.setEnabled(false);
        textFieldCurrentLimit.setEditable(false);
        txtCurrentDate.getButton().setEnabled(false);
        buttonReSchedulePlan.setEnabled(false);
        textFieldCurrentLimit.setText("");
        try  {
            scrollPanePlan.getViewport().remove(plan);
        } catch (Exception ex)  {
            ex.printStackTrace();
        } 
        
        
    }

    private JTable setPlanTableColumnWidth(JTable table){
        for(int i=0; i<table.getColumnCount(); i++){
            table.getColumnModel().getColumn(i).setPreferredWidth(105);;
        }
        
        return table;
    }
    
    private void buttonEdit_actionPerformed(ActionEvent e) {
        e.toString();
        textFieldCurrentLimit.setEditable(true);
        txtCurrentDate.getButton().setEnabled(true);
        buttonReSchedulePlan.setEnabled(true);
        buttonEdit.setEnabled(false);
    }

    private void buttonReSchedulePlan_actionPerformed(ActionEvent e) {
        e.toString();
        try  {
            validateCurrentLimitTextField();
            validateCurrentDate();
            
            int newMeter = Integer.parseInt(textFieldCurrentLimit.getText());
            String newMeterDate = txtCurrentDate.getInputText();
            
            connection.setAutoCommit(false);
            
            PlanningAsset planningAsset = new PlanningAsset(textFieldMachine.getText(),connection);
            if(newMeter <=  planningAsset.getCurrentMeter(null)){
               messageBar.setMessage("You cannot enter past meter values","ERROR");
            }
            else{
                GUIMeterAverage guiMeterAverage = new GUIMeterAverage(frame,planningAsset,newMeter,newMeterDate,connection,messageBar);
                guiMeterAverage.setLocationRelativeTo(frame);
                guiMeterAverage.setVisible(true);
                
                if(guiMeterAverage.OPTION == -1){
                    /** rescheduling aborted */
                    return;
                } 
                
                Vector<AssetPM> assetPMs = planningAsset.getAllPlannedAssetPMs();
                
                AssetPM assetPM = null;
                int nextHighestMeter=0;
                int diff_Between_NHMV_AND_NewMeter = 0;
                int daysToNextHighestMeter = 0;
                String scheduledNHMV_Date = "";
                String calculatedNHM_Date = "";
                int scheduledDifferenceDays = 0;
                String newPlanEndDate = "";
                int endDifference = 0;
                
                for(int i = 0; i < assetPMs.size(); i++){
                    assetPM = assetPMs.get(i);// only the meter based PMs are effected
                     if(!assetPM.isTimeBased()){                     
                        //System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                        
                        nextHighestMeter = assetPM.getNextHighestMeterTo(newMeter); 
                        //System.out.println("nextHighestMeter:"+nextHighestMeter);
                        
                        diff_Between_NHMV_AND_NewMeter = nextHighestMeter - newMeter ;
                        //System.out.println("diff_Between_NHMV_AND_NewMeter:"+diff_Between_NHMV_AND_NewMeter);
                        
                        daysToNextHighestMeter = diff_Between_NHMV_AND_NewMeter / planningAsset.AVERAGE_METER_PER_DAY;
                        //System.out.println("AVERAGE_METER_PER_DAY:"+planningAsset.AVERAGE_METER_PER_DAY);
                        //System.out.println("daysToNextHighestMeter:"+daysToNextHighestMeter);
                        
                        calculatedNHM_Date = addDays(newMeterDate,daysToNextHighestMeter);
                        //System.out.println("calculatedNHM_Date:"+calculatedNHM_Date);
                        
                        scheduledNHMV_Date = assetPM.getDateForMeter(nextHighestMeter);
                        //System.out.println("scheduledNHMV_Date:"+scheduledNHMV_Date);
                        
                        scheduledDifferenceDays = dateDifference(calculatedNHM_Date,scheduledNHMV_Date);
                        //System.out.println("scheduledDifferenceDays:"+scheduledDifferenceDays);
                        
                        //assetPM.rescheduleFrom(scheduledNHMV_Date,scheduledDifferenceDays,0);
                        assetPM.rescheduleFrom(assetPM.LAST_DONE_DATE,scheduledDifferenceDays,0);
                        
                        newPlanEndDate = assetPM.getLastScheduledDate();
                        //System.out.println("newPlanEndDate:"+newPlanEndDate);
                        //System.out.println("assetPM.PLAN_END_DATE:"+assetPM.PLAN_END_DATE);
                        
                        endDifference = dateDifference(newPlanEndDate,assetPM.PLAN_END_DATE);
                        //System.out.println("endDifference:"+endDifference);
                        
                        if(endDifference > 0){
                            //Delete from old plan end date
                             assetPM.deletePlanFrom(assetPM.PLAN_END_DATE);
                        }
                        else if(endDifference < 0) {
                            assetPM.planFrom(newPlanEndDate,assetPM.PLAN_END_DATE,planningAsset.AVERAGE_METER_PER_DAY);
                            //Extend
                        }
                        
                        //System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    } 
                }
                connection.commit();
                
                try {
                   setTable();
                    /*scrollPanePlan.getViewport().remove(plan);
                    
                    plan = setPlanTableColumnWidth(new ViewOnlyRSTable(connection.createStatement().executeQuery("SELECT " +
                                                                                                            "Description ," +
                                                                                                            "Modified_Date AS 'Scheduled Date'," +
                                                                                                      //      "IF(Done_Date='0000-00-00','0000-00-00',Done_Date) AS 'Done Date'," +
                                                                                                            "Modified_Meter AS 'Scheduled Meter' " +
                                                                                                            "FROM " +
                                                                                                            "Scheduled_Preventive_Maintenance PMS," +
                                                                                                            "preventive_maintenance PMM " +
                                                                                                            "WHERE " +
                                                                                                            "PMS.asset_id='"+textFieldMachine.getText()+"' " +
                                                                                                            "AND " +
                                                                                                            "PMS.Preventive_Maintenance_ID=PMM.id " +
                                                                                                            "ORDER BY " +
                                                                                                            "Modified_Date"
                                                                                                            )).getRSTable());
                    scrollPanePlan.getViewport().add(plan);*/
                }
                catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } 
        } 
        catch (Exception ex)  {
            messageBar.setMessage(ex.getMessage(),"ERROR");
            ex.printStackTrace();
            try {
                connection.rollback();
                connection.setAutoCommit(false);
            }
            catch (SQLException excep) {
                
            }
        }
    }
    
    private String addDays(String date,int days) throws Exception{
        String sql = "SELECT ADDDATE(?,?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,date);
        stmt.setInt(2,days);
        
        ResultSet rS = stmt.executeQuery();
        rS.first();
        return rS.getString(1);
    }
    
    private int dateDifference(String date1,String date2) throws Exception{
        ResultSet rSet = connection.prepareStatement("SELECT DATEDIFF('"+date1+"','"+date2+"')").executeQuery();
        rSet.first();
        return rSet.getInt(1);
    }
    
    private void validateCurrentLimitTextField() throws Exception{
        try {
            Integer.parseInt(textFieldCurrentLimit.getText());
        }
        catch (Exception e) {
            e.toString();
            throw new Exception("Inavalid current meter");
        }
    }

    private void validateCurrentDate() {
        
        try {
            
            PreparedStatement pS = connection.prepareStatement("SELECT IF(MAX(Done_Date)='0000-00-00 00:00:00','0000-00-00',MAX(Done_Date))as DoneDate FROM Preventive_Maintenance_Work_Order WHERE Asset_ID = ?");
            pS.setString(1,textFieldMachine.getText());
            ResultSet rS = pS.executeQuery();
            rS.first();
            String maxDoneDate  = rS.getString(1);
            
            ResultSet rec = connection.createStatement().executeQuery("SELECT DATEDIFF('"+txtCurrentDate.getInputText()+"','"+maxDoneDate+"')");
            rec.first();
            
            if(rec.getInt(1)<0){
                throw new Exception("Invalid Date! You can't enter a date lower than the last done date");
            }
            
        }
        catch (Exception e) {
           e.printStackTrace(); 
        }
        
    }
    
    public void setAsset(String assetID){
        //machineID = dADMachine.getValue();
        textFieldMachine.setText(assetID);
        buttonCustomer.setEnabled(false);
        buttonMachine.setEnabled(false);
        
        if(customerID == 0){
            try{
                ResultSet r = connection.prepareStatement("SELECT " +
                                                        "C.Name," +
                                                        "C.Division_ID " +
                                                    "FROM " +
                                                        "asset M," +
                                                        "division C " +
                                                    "WHERE " +
                                                        "M.asset_id='"+assetID+"' " +
                                                        "AND " +
                                                        "M.Division_ID=C.division_ID"
                                                    ).executeQuery();
                r.first();
                textFieldCustomer.setText(r.getString(1));
                customerID = r.getInt(2);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
        buttonEdit.setEnabled(true);
        buttonCancel.setEnabled(true);
        
        try {
        setTable();
        /*
            plan = setPlanTableColumnWidth(new ViewOnlyRSTable(connection.createStatement().executeQuery("SELECT " +
                                                                                                    "Description ," +
                                                                                                    "Modified_Date AS 'Scheduled Date'," +
                                                                                              //      "IF(Done_Date='0000-00-00','0000-00-00',Done_Date) AS 'Done Date'," +
                                                                                                    "Modified_Meter AS 'Scheduled Meter' " +
                                                                                                    "FROM " +
                                                                                                    "Scheduled_Preventive_Maintenance PMS," +
                                                                                                    "preventive_maintenance PMM " +
                                                                                                    "WHERE " +
                                                                                                    "PMS.asset_id='"+assetID+"' " +
                                                                                                    "AND " +
                                                                                                    "PMS.Preventive_Maintenance_ID=PMM.id " +
                                                                                                    "ORDER BY " +
                                                                                                    "Modified_Date"
                                                                                                    )).getRSTable());
            scrollPanePlan.getViewport().add(plan);*/
        }
        catch (SQLException ex) {
            ex.printStackTrace();
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
