package com.matrix.focus.log;

import com.matrix.components.MTextbox;
import com.matrix.components.TitleBar;
import com.matrix.focus.workorder.PreventiveMaintenanceWorkOrder;
import com.matrix.focus.workorder.RequestedTask;
import com.matrix.focus.util.MPanel;
import com.matrix.focus.inspection.InspectionDialog;
import com.matrix.focus.mdi.MDI;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.readings.ReadingsDialog;
import com.matrix.focus.planner.AssetPM;
import com.matrix.focus.planner.PlanningAsset;
import com.matrix.focus.util.ButtonCellRenderer;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.DataAssistantDialog;
import com.matrix.focus.util.DateTimeCell;
import com.matrix.focus.util.DateTimePicker;
import com.matrix.focus.util.ImageLibrary;
import com.matrix.focus.util.LockedLabelCellRenderer;
import com.matrix.focus.util.StandardTimeCell;
import com.matrix.focus.util.StandardTimeDialog;
import com.matrix.focus.util.TaskRemarksDialog;
import com.matrix.focus.util.Utilities;
import com.matrix.focus.util.Validator;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

public class GUIPreventiveMaintenanceLog extends MPanel {

     private Connection connection;
     private MDI frame;
     private MessageBar messageBar;
     private PreventiveMaintenanceWorkOrder WORK_ORDER;
     private Savepoint spMain;
     private JPanel jPanel3 = new JPanel();
     private MTextbox txtWorkorder = new MTextbox();
     private JScrollPane jScrollPane4 = new JScrollPane();
     private JButton btnSave = new JButton(new ImageIcon(ImageLibrary.BUTTON_SAVE));
     private WorkOrderTable tblWorkOrder;
     private PMLabourUtilisationDialog dlgLabour;
     private JButton btnWO = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
     private JButton btnEmployees = new JButton(new ImageIcon(ImageLibrary.BUTTON_CREW));
     private JButton btnCancel = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
      private MTextbox txtStartedDate = new MTextbox();
     private JButton btnStartedDate = new JButton(new ImageIcon(ImageLibrary.BUTTON_DT_PICKER));
     private DateTimePicker dlgDTP;
     private JButton btnEdit = new JButton(new ImageIcon(ImageLibrary.BUTTON_EDIT));
     private PlannedPartUtilisationDialog dlgPartUtil;
     private StandardTimeDialog dlgStdTime;
    private TaskRemarksDialog dlgPMRemarks;
     private TitleBar titlebar = new TitleBar();
     private JPanel jPanel1 = new JPanel();
     private MTextbox txtCustomer = new MTextbox();
     private MTextbox txtAsset = new MTextbox();
     private MTextbox txtWODate = new MTextbox();
     private MTextbox txtPromisedDate = new MTextbox();
    private MTextbox txtDoneDate = new MTextbox();
     private JButton btnDoneDate = new JButton(new ImageIcon(ImageLibrary.BUTTON_DT_PICKER));
    private JScrollPane jScrollPane2 = new JScrollPane();
    private JTextArea txtWODescription = new JTextArea();
    private JPanel jPanel6 = new JPanel();
    private JLabel jLabel1 = new JLabel();
    private JLabel jLabel2 = new JLabel();
    private JTabbedPane jTabbedPane1 = new JTabbedPane();
    private JPanel jPanel2 = new JPanel();
    private JPanel jPanel4 = new JPanel();
    private JScrollPane jScrollPane3 = new JScrollPane();
    private RequestedTasksTable tblRequested = new RequestedTasksTable();
    private RequestedPartUtilisationDialog dlgReqPartUtil;
    private MTextbox txtType = new MTextbox();
    private JButton btnInspectionLog = new JButton(new ImageIcon(ImageLibrary.MENU_CHECKLIST_REGISTER));
    private JButton btnReadingLog = new JButton(new ImageIcon(ImageLibrary.MENU_CONDITION_MONITORING_REGISTER));
    private DBConnectionPool pool;
    private JButton btnUndone = new JButton(new ImageIcon(ImageLibrary.BUTTON_UNDO));
    private MTextbox txtCategory = new MTextbox();
    private JTextField txtModel = new JTextField();
    private JButton btnViewCompletion = new JButton(new ImageIcon(ImageLibrary.BUTTON_VIEW));
    private MTextbox txtMeter = new MTextbox();
    private JButton btnWO_DTL = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton btnP_WO = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton btnD_WO = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    //private JTextArea txtRemarks = new JTextArea();
    private JTextArea taRemarks = new JTextArea();
    private JScrollPane jScrollPane1 = new JScrollPane();

    public GUIPreventiveMaintenanceLog(DBConnectionPool pool, final MDI frame, MessageBar msgBar) {
         /**New Database connection was taken to manage the transactions*/
         this.connection = pool.getConnection();
         this.pool = pool;
         try{
             this.connection.setAutoCommit(false);
         }
         catch(Exception er){}
         
         this.frame = frame;
         this.messageBar = msgBar;
         try {
             titlebar.setTitle("Job Completion");
             titlebar.setDescription("The facility to keep logs of Jobs.");
             titlebar.setImage(ImageLibrary.TITLE_PREVENTIVE_MAINTENANCE_LOG);
         
             tblWorkOrder = new WorkOrderTable();
             dlgLabour = new PMLabourUtilisationDialog(frame,this.connection,msgBar,true);
             dlgPMRemarks = new TaskRemarksDialog(frame);
             dlgPMRemarks.setLocationRelativeTo(frame);
             
             dlgDTP = new DateTimePicker(frame);
             dlgStdTime = new StandardTimeDialog(frame);
             
             jbInit();
            
             tblWorkOrder.addMouseListener(new MouseAdapter(){
                                      public void mouseClicked(MouseEvent me){
                                          if(me.getButton()==1 && tblWorkOrder.isEnabled()){
                                          int row = tblWorkOrder.getSelectedRow();
                                          if(row!=-1){
                                              if(tblWorkOrder.getSelectedColumn()==10){  
                                                  dlgPartUtil = new PlannedPartUtilisationDialog(frame,messageBar,true,connection);
                                                  dlgPartUtil.setLocationRelativeTo(frame);
                                                  dlgPartUtil.populate(tblWorkOrder.getValueAt(row,2).toString(),txtAsset.getInputText(),tblWorkOrder.getValueAt(row,0).toString());
                                                  dlgPartUtil.setVisible(true); 
                                              }
                                              else if(tblWorkOrder.getSelectedColumn()==12){                                              
                                                  dlgPMRemarks.setText(tblWorkOrder.getValueAt(row,12).toString());
                                                  dlgPMRemarks.setVisible(true);
                                                  String rtnValue = dlgPMRemarks.getText();
                                                  if(!rtnValue.equals("null")){
                                                    tblWorkOrder.setValueAt(dlgPMRemarks.getText(),row,12);
                                                    tblWorkOrder.repaint();
                                                  }
                                              }
                                          }
                                          else{
                                              messageBar.setMessage("Select a row in the tasks table.","ERROR");
                                          }
                                      }
                                    }
                                });
                                
             tblRequested.addMouseListener(
                new MouseAdapter(){
                    public void mouseClicked(MouseEvent me){
                        if(me.getButton()==1 && tblRequested.isEnabled()){
                            int row = tblRequested.getSelectedRow();
                            if(row!=-1){
                                if(tblRequested.getSelectedColumn()==4){
                                    dlgReqPartUtil = new RequestedPartUtilisationDialog(
                                                                                        frame,
                                                                                        messageBar,
                                                                                        connection,
                                                                                        true);
                                    dlgReqPartUtil.populate(txtWorkorder.getInputText(),
                                                            txtAsset.getInputText(),
                                                            tblRequested.getValueAt(row,0).toString());
                                    dlgReqPartUtil.setLocationRelativeTo(frame);
                                    dlgReqPartUtil.setVisible(true); 
                                }
                                else if(tblRequested.getSelectedColumn()==6){                                              
                                    dlgPMRemarks.setText(tblRequested.getValueAt(row,6).toString());
                                    dlgPMRemarks.setVisible(true);
                                    String rtnValue = dlgPMRemarks.getText();
                                    if(!rtnValue.equals("null")){
                                        tblRequested.setValueAt(dlgPMRemarks.getText(),row,6);
                                    } 
                                }    
                            }
                            else{
                                messageBar.setMessage("Select a row in the tasks table.","ERROR");
                            }
                        }
                    }
                }
            );
                                
            
             setMode("LOAD");
         }catch (Exception e){
             e.printStackTrace();
         }
     }
     
     public void close(){
         try{
             connection.close();
         } 
         catch(SQLException e) {}
     }
     
     private boolean hasWorkorders(String asset_id){
         try{
             ResultSet rec = connection.createStatement().executeQuery(getSQLString(asset_id));
             rec.last();
             return ((rec.getRow()>0) ? true : false);
         }
         catch(Exception er){
             return false;
         }
     }
     
     private String getSQLString(String asset_id){    
         String mainSQL = "SELECT DISTINCT pmwo.PM_Work_Order_ID," +
                                 "SUBSTRING(pmwo.Created_Date,1,10)," +
                                 "pmwo.EmployeesAllocated, " +
                                 "IF(pmwo.Starting_Time='0000-00-00 00:00:00','Pending','Done')" +
                          "FROM preventive_maintenance_work_order pmwo," +
                               "preventive_maintenance_log pml," +
                               "scheduled_preventive_maintenance spm " +
                          "WHERE pmwo.PM_Work_Order_ID = pml.PM_Work_Order_ID AND " +
                                "pml.Scheduled_ID = spm.Scheduled_ID AND " +
                                "spm.Asset_ID = '" + asset_id + "' AND ";
         //String option1 = "DATEDIFF(pmwo.Created_Date,'" + mdtDate.getInputText() + "') = 0 ";
         //String option2 = "pmwo.Created_Date >=DATE('" + mdtFrom.getInputText() + "') AND pmwo.Created_Date <=DATE('"+ mdtTo.getInputText() + "')";
         //mainSQL += (rbtnDate.isSelected()?option1:option2);

         return mainSQL;
     }
     
     private void jbInit() throws Exception {
         this.setLayout( null );
         this.setSize(new Dimension(953, 613));
        
        jPanel3.setBounds(new Rectangle(10, 85, 475, 270));
         jPanel3.setBorder(BorderFactory.createTitledBorder("Job Details"));
         jPanel3.setLayout(null);
         txtWorkorder.setBounds(new Rectangle(35, 20, 195, 20));
         txtWorkorder.setCaption("Job No ");
         txtWorkorder.setTxtWidth(80);
         txtWorkorder.setEditable(false);
         txtWorkorder.setTxtFont(new Font("Tahoma", 0, 11));
         txtWorkorder.setLblFont(new Font("Tahoma", 0, 11));
         txtWorkorder.setTxtForeColor(Color.RED);
        jScrollPane4.setBounds(new Rectangle(5, 5, 905, 145));
         btnSave.setText("Save");
         btnSave.setBounds(new Rectangle(750, 565, 95, 30));
         btnSave.addActionListener(new ActionListener() {
                     public void actionPerformed(ActionEvent e) {
                         btnSave_actionPerformed(e);
                     }
                 });
         btnWO.setBounds(new Rectangle(215, 20, 30, 20));
        btnWO.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnWO_actionPerformed(e);
                    }
                });
        btnEmployees.setText("Labour Utilization");
        btnEmployees.setBounds(new Rectangle(10, 565, 145, 30));
        btnEmployees.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnEmployees_actionPerformed(e);
                    }
                });
        btnCancel.setText("Cancel");
        btnCancel.setBounds(new Rectangle(850, 565, 95, 30));
        btnCancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnCancel_actionPerformed(e);
                    }
                });
        txtStartedDate.setBounds(new Rectangle(35, 30, 215, 20));
        txtStartedDate.setTxtWidth(120);
        txtStartedDate.setCaption("Started Date");
        txtStartedDate.setEditable(false);
        btnStartedDate.setBounds(new Rectangle(255, 30, 30, 20));
        btnStartedDate.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnStartedDate_actionPerformed(e);
                    }
                });
        btnEdit.setText("Edit");
        btnEdit.setBounds(new Rectangle(650, 565, 95, 30));
        btnEdit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnEdit_actionPerformed(e);
                    }
                });
        titlebar.setBounds(new Rectangle(10, 10, 940, 70));
        jPanel1.setBounds(new Rectangle(10, 355, 940, 205));
        jPanel1.setBorder(BorderFactory.createTitledBorder("JobTasks"));
        jPanel1.setLayout(null);
        txtCustomer.setBounds(new Rectangle(35, 45, 395, 20));
        txtCustomer.setCaption("Customer");
        txtCustomer.setTxtWidth(300);
        txtCustomer.setEditable(false);
        txtAsset.setBounds(new Rectangle(35, 70, 300, 20));
        txtAsset.setCaption("Machine");
        txtAsset.setEditable(false);
        txtAsset.setTxtWidth(150);
        txtWODate.setBounds(new Rectangle(255, 120, 175, 20));
        txtWODate.setCaption("Job Info:");
        txtWODate.setTxtWidth(120);
        txtWODate.setEditable(false);
        txtWODate.setLblWidth(75);
        txtPromisedDate.setBounds(new Rectangle(35, 120, 175, 20));
        txtPromisedDate.setTxtWidth(80);
        txtPromisedDate.setCaption("Promised Date");
        txtPromisedDate.setEditable(false);
        txtDoneDate.setBounds(new Rectangle(35, 55, 215, 20));
        txtDoneDate.setCaption("Done Date");
        txtDoneDate.setTxtWidth(120);
        txtDoneDate.setEditable(false);
        btnDoneDate.setBounds(new Rectangle(255, 55, 30, 20));
        btnDoneDate.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnDoneDate_actionPerformed(e);
                    }
                });
        jScrollPane2.setBounds(new Rectangle(10, 160, 455, 100));
        txtWODescription.setEditable(false);
        jPanel6.setBounds(new Rectangle(485, 85, 465, 270));
        jPanel6.setLayout(null);
        jPanel6.setBorder(BorderFactory.createTitledBorder("Job Completion Details"));
        jLabel1.setText("Job Description");
        jLabel1.setBounds(new Rectangle(10, 140, 120, 20));
        jLabel2.setText("Job Completion Remarks");
        jLabel2.setBounds(new Rectangle(10, 120, 165, 20));
        jTabbedPane1.setBounds(new Rectangle(10, 15, 920, 180));
        jPanel2.setLayout(null);
        jPanel4.setLayout(null);
        jScrollPane3.setBounds(new Rectangle(5, 5, 840, 145));
        txtType.setBounds(new Rectangle(35, 95, 140, 20));
        txtType.setTxtWidth(100);
        txtType.setCaption("Type");
        txtType.setEditable(false);
        btnInspectionLog.setText("Inspection Log");
        btnInspectionLog.setBounds(new Rectangle(285, 565, 125, 30));
        btnInspectionLog.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnInspectionLog_actionPerformed(e);
                    }
                });
        btnReadingLog.setText("Reading Log");
        btnReadingLog.setBounds(new Rectangle(160, 565, 120, 30));
        btnReadingLog.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnReadingLog_actionPerformed(e);
                    }
                });
        btnUndone.setText("Make Undone");
        btnUndone.setBounds(new Rectangle(525, 565, 120, 30));
        btnUndone.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnUndone_actionPerformed(e);
                    }
                });
        txtCategory.setBounds(new Rectangle(255, 95, 175, 20));
        txtCategory.setCaption("Category");
        txtCategory.setLblWidth(75);
        txtCategory.setTxtWidth(100);
        txtCategory.setEditable(false);
        txtModel.setBounds(new Rectangle(285, 70, 145, 20));
        txtModel.setEditable(false);
        btnViewCompletion.setText("View");
        btnViewCompletion.setBounds(new Rectangle(850, 5, 60, 25));
        btnViewCompletion.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnViewCompletion_actionPerformed(e);
                    }
                });
        txtMeter.setBounds(new Rectangle(35, 80, 215, 20));
        txtMeter.setCaption("Done Meter");
        txtMeter.setTxtWidth(100);
        btnWO_DTL.setText("Detail");
        btnWO_DTL.setBounds(new Rectangle(245, 20, 65, 20));
        btnWO_DTL.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnWO_DTL_actionPerformed(e);
                    }
                });
        btnP_WO.setText("Pending");
        btnP_WO.setBounds(new Rectangle(310, 20, 80, 20));
        btnP_WO.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnP_WO_actionPerformed(e);
                    }
                });
        btnD_WO.setText("Done");
        btnD_WO.setBounds(new Rectangle(390, 20, 75, 20));
        btnD_WO.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnD_WO_actionPerformed(e);
                    }
                });
        jScrollPane1.setBounds(new Rectangle(10, 140, 445, 120));
        jPanel3.add(btnD_WO, null);
        jPanel3.add(btnP_WO, null);
        jPanel3.add(btnWO_DTL, null);
        jPanel3.add(txtModel, null);
        jPanel3.add(txtCategory, null);
        jPanel3.add(txtType, null);
        jPanel3.add(jLabel1, null);
        jPanel3.add(txtPromisedDate, null);
        jPanel3.add(txtWODate, null);
        jPanel3.add(txtAsset, null);
        jPanel3.add(txtCustomer, null);
        jPanel3.add(btnWO, null);
        jPanel3.add(txtWorkorder, null);
        jScrollPane2.getViewport().add(txtWODescription, null);
        jPanel3.add(jScrollPane2, null);
        jScrollPane1.getViewport().add(taRemarks, null);
        jPanel6.add(jScrollPane1, null);
        jPanel6.add(txtMeter, null);
        jPanel6.add(txtStartedDate, null);
        jPanel6.add(btnStartedDate, null);
        jPanel6.add(txtDoneDate, null);
        jPanel6.add(btnDoneDate, null);
        jPanel6.add(jLabel2, null);
        jScrollPane4.getViewport().add(tblWorkOrder, null);
        jPanel2.add(jScrollPane4, null);
        jTabbedPane1.addTab("Scheduled Maintenances", jPanel2);
        jScrollPane3.getViewport().add(tblRequested, null);
        jPanel4.add(btnViewCompletion, null);
        jPanel4.add(jScrollPane3, null);
        jTabbedPane1.addTab("Other Tasks", jPanel4);
        jPanel1.add(jTabbedPane1, null);
        this.add(jPanel6, null);
        this.add(jPanel1, null);
        this.add(titlebar, null);
        this.add(btnEdit, null);
        this.add(jPanel3, null);
        this.add(btnCancel, null);
        this.add(btnSave, null);
        this.add(btnEmployees, null);
        this.add(btnUndone, null);
        this.add(btnReadingLog, null);
        this.add(btnInspectionLog, null);
    }
     

    private void btnStartedDate_actionPerformed(ActionEvent e) {
        String txt = txtStartedDate.getInputText().trim();
        if(!txt.isEmpty()){
            dlgDTP.setDateTime(txt.substring(0,10),txt.substring(11));
        }
        dlgDTP.setLocationRelativeTo(btnStartedDate);
        dlgDTP.setVisible(true);
        String value = dlgDTP.getDateTime();
        
        if(!value.isEmpty()){
            txtStartedDate.setInputText(value);
        }
    }

    private void btnDoneDate_actionPerformed(ActionEvent e) {
        String txt = txtDoneDate.getInputText().trim();
        if(!txt.isEmpty()){
            dlgDTP.setDateTime(txt.substring(0,10),txt.substring(11));
        }
        dlgDTP.setLocationRelativeTo(btnStartedDate);
        dlgDTP.setVisible(true);
        String value = dlgDTP.getDateTime();
        
        if(!value.isEmpty()){
            txtDoneDate.setInputText(value);
            tblWorkOrder.setAllDoneDatesTo(value);
        }
    }

    private void btnEdit_actionPerformed(ActionEvent e){
        try{
            spMain = connection.setSavepoint("Main");
        } catch (SQLException f) {}
        setMode("EDIT");
    }
     
     private void btnEmployees_actionPerformed(ActionEvent e) {
         /**Show the Labour Requirement Dialog*/
         dlgLabour.populate(txtWorkorder.getInputText());
         dlgLabour.setLocationRelativeTo(frame);
         dlgLabour.setVisible(true);
     }
          
    private void btnSave_actionPerformed(ActionEvent e) {
        try{
            saveWorkOrder();
            connection.commit();
            messageBar.setMessage("Job log saved.","OK");
            setMode("SEARCH");
        }
        catch(Exception er){
            try {
                connection.rollback(spMain);
            }
            catch (SQLException se) {}
            messageBar.setMessage(er.getMessage(),"ERROR");
            //er.printStackTrace();
        }
    }
          
     private void setMode(String mode){
        if(mode.equals("LOAD")){
            clearAll();
            btnEmployees.setEnabled(false);
            btnEdit.setEnabled(false);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(false);
            btnInspectionLog.setEnabled(false);
            btnReadingLog.setEnabled(false);
            btnUndone.setEnabled(false);
            btnViewCompletion.setEnabled(false);
            setEditable(false);
        }
        else if(mode.equals("EDIT")){
            btnEmployees.setEnabled(true);
            btnEdit.setEnabled(false);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
            btnUndone.setEnabled(true);
            btnInspectionLog.setEnabled(true);
            btnReadingLog.setEnabled(true);
            btnViewCompletion.setEnabled(false);
            setEditable(true);
        }
        else if(mode.equals("SEARCH")){
            btnEmployees.setEnabled(false);
            btnEdit.setEnabled(true);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnInspectionLog.setEnabled(false);
            btnReadingLog.setEnabled(false);
            btnUndone.setEnabled(false);
            btnViewCompletion.setEnabled(true);
            setEditable(false);
        }
     }
     
     private void clearAll(){
         tblWorkOrder.deleteAll();
         txtWorkorder.setInputText("");
         txtStartedDate.setInputText("");
         txtDoneDate.setInputText("");
         taRemarks.setText("");
         txtWODescription.setText("");
         txtCustomer.setInputText("");
         txtAsset.setInputText("");
         txtWODate.setInputText("");
         txtPromisedDate.setInputText("");
         txtType.setInputText("");
         txtCategory.setInputText("");
         txtModel.setText("");
         txtMeter.setInputText("");
     }
     
     private void setEditable(boolean flag){
         btnStartedDate.setEnabled(flag);
         btnDoneDate.setEnabled(flag);
         txtMeter.setEditable(flag);
         tblWorkOrder.setEnabled(flag);
         taRemarks.setEnabled(flag);
         tblRequested.setEnabled(flag);    
        }
     
    private void saveWorkOrder() throws Exception{
        WORK_ORDER.started_date = txtStartedDate.getInputText();
        WORK_ORDER.done_date = txtDoneDate.getInputText();
        WORK_ORDER.remarks = taRemarks.getText();
        WORK_ORDER.update();
        
        int rows = tblWorkOrder.getRowCount();
        String done_date = txtDoneDate.getInputText().substring(0,10);
        String meter_value = txtMeter.getInputText();
        PlanningAsset planningAsset = null;
    
        if(done_date.equals("0000-00-00")){
            throw new Exception("Done date field is empty");
        }
     
        if(!txtAsset.getInputText().isEmpty()){
            System.out.println("1");
            planningAsset = new PlanningAsset(txtAsset.getInputText(),connection);
            System.out.println("2");
            if(planningAsset.hasRegistedMeterBasedPlanningPMs()){
                System.out.println("3");
                if(meter_value.isEmpty()){
                    System.out.println("4");
                    throw new Exception("Done meter field is empty. Done meter is required for this machine.");
                }
                else if(!Utilities.isPositive(meter_value)){
                    System.out.println("5");
                    txtMeter.setFocus(1);
                    throw new Exception("Invalid Done meter");
                }
                else{
                    System.out.println("6");
                    /**Set all PMs to done mater*/
                    for(int i=0;i<rows;i++){
                        tblWorkOrder.setValueAt(meter_value,i,7);
                    }
                }
            }
        }
        
        PreventiveMaintenanceLogEntry pmle = null;
        for(int i=0;i<rows;i++){
            pmle = new PreventiveMaintenanceLogEntry(connection);
            pmle.preventive_maintenance_log_id = tblWorkOrder.getValueAt(i,0).toString();
            pmle.scheduled_id = tblWorkOrder.getValueAt(i,1).toString();
            pmle.done_date = tblWorkOrder.getValueAt(i,6).toString();
            pmle.done_meter = tblWorkOrder.getValueAt(i,7).toString();
            pmle.time_taken = tblWorkOrder.getValueAt(i,9).toString();
            pmle.success = tblWorkOrder.getValueAt(i,11).toString();
            pmle.remarks = tblWorkOrder.getValueAt(i,12).toString();
            
            /**Save log entry*/
            pmle.log();
            
            /**Modify plan of the current PM*/
            modifyPlan(
                    pmle,
                    tblWorkOrder.getValueAt(i,2).toString(),
                    tblWorkOrder.getValueAt(i,4).toString(),
                    tblWorkOrder.getValueAt(i,5).toString()
            );
        }
        
        /**Reschedule Non-WorkOrder PMs*/
        if(planningAsset!=null){
            if(planningAsset.hasRegistedMeterBasedPlanningPMs()){
                rescheduleNonWorkOrderPMsTo(Integer.parseInt(meter_value),txtDoneDate.getInputText().substring(0,10));
            }
        }
    
        rows = tblRequested.getRowCount();
        RequestedTask reqJob = null;
        for(int i=0;i<rows;i++){
            reqJob = new RequestedTask(connection);
            reqJob.pm_work_order_id = txtWorkorder.getInputText();
            reqJob.asset_id = txtAsset.getInputText();
            reqJob.task_id = tblRequested.getValueAt(i,0).toString();
            reqJob.done_date = tblRequested.getValueAt(i,2).toString();
            reqJob.time_taken = tblRequested.getValueAt(i,3).toString();
            reqJob.success = tblRequested.getValueAt(i,5).toString();
            reqJob.remarks = tblRequested.getValueAt(i,6).toString();
            reqJob.log();
        }
        
    }
     
    private void modifyPlan(PreventiveMaintenanceLogEntry pmle, String pm_id, String modified_date, String modified_meter) throws Exception{
        AssetPM assetPM = new AssetPM(txtAsset.getInputText(),pm_id,connection);
        System.out.println("7");
        
        int days_diff = getDateDifference(pmle.done_date,assetPM.getLastDoneDate());
        int meter_diff = Integer.parseInt(pmle.done_meter) - assetPM.getLastDoneMeter();
        System.out.println("8");
        
        if(days_diff > 0 || meter_diff > 0){
            System.out.println("9");
            int schedule_date_diff = getDateDifference(pmle.done_date,modified_date);
            if(schedule_date_diff!=0){
                System.out.println("10");
                assetPM.rescheduleFrom(modified_date,schedule_date_diff,0);    
                System.out.println("11");
            }
            
            int schedule_meter_diff = (Integer.parseInt(pmle.done_meter) - Integer.parseInt(modified_meter));
            if(schedule_meter_diff !=0){
                System.out.println("12");
                assetPM.rescheduleFrom(modified_date,0,schedule_meter_diff);
                System.out.println("13");
            }
            
        }
        else{
            System.out.println("1");
            //Entering a past Job log
        }
    }
    
     private void rescheduleNonWorkOrderPMsTo(int newMeter, String newMeterDate) throws Exception{
         PlanningAsset planningAsset = new PlanningAsset(txtAsset.getInputText(),connection);
         planningAsset.hasRegistedMeterBasedPlanningPMs(); 
                     
         if(newMeter <=  planningAsset.getCurrentMeter(null)){
            //Do nothing
         }
         else{
             Vector<AssetPM> assetPMs = planningAsset.getAllPlannedAssetPMs();
             AssetPM assetPM = null;
             int nextHighestMeter=0;
             int diff_Between_NHMV_AND_NewMeter = 0;
             int daysToNextHighestMeter = 0;
             String scheduledNHMV_Date = "";
             String calculatedNHM_Date = "";
             int scheduledDifferenceDays = 0;
             
             for(int i = 0; i < assetPMs.size(); i++){
                 assetPM = assetPMs.get(i);
                 if(!isInWorkOrder(assetPM.PM_ID)){
                     if(!assetPM.isTimeBased()){
                         nextHighestMeter = assetPM.getNextHighestMeterTo(newMeter);
                         diff_Between_NHMV_AND_NewMeter = nextHighestMeter - newMeter ;
                         daysToNextHighestMeter = diff_Between_NHMV_AND_NewMeter / planningAsset.AVERAGE_METER_PER_DAY;
                         calculatedNHM_Date = addDays(newMeterDate,daysToNextHighestMeter);
                         scheduledNHMV_Date = assetPM.getDateForMeter(nextHighestMeter);
                         scheduledDifferenceDays = getDateDifference(calculatedNHM_Date,scheduledNHMV_Date);
                         assetPM.rescheduleFrom(scheduledNHMV_Date,scheduledDifferenceDays,0);
                     }
                 }
             }
         }    
     }
                 
     private String addDays(String date,int days) throws Exception{
         String sql = "SELECT ADDDATE(?,?)";
         PreparedStatement stmt = connection.prepareStatement(sql);
         stmt.setString(1,date);
         stmt.setInt(2,days);
         ResultSet rec = stmt.executeQuery();
         rec.first();
         return rec.getString(1);
     }           
    
    private int getDateDifference(String second_date, String first_date) throws Exception{
        String sql = "SELECT DATEDIFF(?,?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,second_date);
        stmt.setString(2,first_date);
        ResultSet rec = stmt.executeQuery();
        rec.first();
        return rec.getInt(1);
    }
    
    private boolean isInWorkOrder(String pm_id){
        int rows = tblWorkOrder.getRowCount();
        for(int i=0;i<rows;i++){
            if(tblWorkOrder.getValueAt(i,2).toString().equals(pm_id)){
                return true;
            }
        }
        return false;
    }
    
     private void btnCancel_actionPerformed(ActionEvent e) {
         try{
            connection.rollback(spMain);   
         } 
         catch(Exception er) {}
         setMode("LOAD");
     }
     
    private void setWorkOrder(String workorder){
        try{
            spMain = connection.setSavepoint("Main");
            WORK_ORDER = new PreventiveMaintenanceWorkOrder(workorder,connection);
                txtWorkorder.setInputText(WORK_ORDER.workorder_id);
                txtType.setInputText(WORK_ORDER.type);
                txtCategory.setInputText(WORK_ORDER.category);
                txtCustomer.setInputText(WORK_ORDER.customer);
                txtAsset.setInputText(WORK_ORDER.asset_id);
                txtModel.setText(WORK_ORDER.model);
                txtWODate.setInputText(WORK_ORDER.creater + " on "+ WORK_ORDER.created_date);
                txtPromisedDate.setInputText(WORK_ORDER.promised_date);
                txtStartedDate.setInputText(WORK_ORDER.started_date);
                txtDoneDate.setInputText(WORK_ORDER.done_date);
                taRemarks.setText(WORK_ORDER.remarks);
                txtWODescription.setText(WORK_ORDER.description);
            tblWorkOrder.populate(WORK_ORDER.workorder_id);
//            tblJobCompletion.populate(WORK_ORDER.asset_id);
            tblRequested.populate(WORK_ORDER.workorder_id);
            setMode("SEARCH");
        } 
        catch (Exception ex){
            ex.printStackTrace();
            messageBar.setMessage(ex.getMessage(),"ERROR");
            setMode("LOAD");
        } 
    }
     
     /**This method is the interface for the common task bar*/
     public void search(String workorder){
         setWorkOrder(workorder);
     }
     
     private String getDuration(String end, String start){        
        String sql = "SELECT DATEDIFF('"+ end.substring(0,10) +"','"+ start.substring(0,10) +"') AS DatePart, " +
                            "TIMEDIFF('"+ end.substring(11,19) +"','"+ start.substring(11,19) +"') AS TimePart ";
         try{
             ResultSet rec = connection.createStatement().executeQuery(sql);
             rec.next();
             String date = rec.getString("DatePart");
             String time[] = rec.getString("TimePart").split(":");
             return (date==null?"0":date) + "-" + time[0] + "-" + time[1];
         }
         catch(Exception er){
             //er.printStackTrace();
             return "0-0-0";
         }
     }

    private void btnWO_actionPerformed(ActionEvent e){
        String sql = "SELECT PM_Work_Order_ID as 'Job', " +
                            "Created_Date AS 'Job Date', " +
                            "IF(Promised_Date='0000-00-00','0000-00-00',Promised_Date) AS 'Promsied Date', " +
                            "IF(Done_Date='0000-00-00 00:00:00','0000-00-00',DATE(Done_Date)) AS 'Done Date' FROM preventive_maintenance_work_order ";
        
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select Job - Data Assistant",sql,connection);
        d.setFirstColumnWidth(100);
        d.setSecondColumnWidth(80);
        d.setThirdColumnWidth(80);
        d.setFourthColumnWidth(80);
        d.setLocationRelativeTo(btnWO);
        d.setVisible(true); 
        String rtnVal = d.getValue();
        if(!rtnVal.equals("")){
            setWorkOrder(rtnVal);
        }
    }
    
     private void btnWO_DTL_actionPerformed(ActionEvent e) {
         String sql = "SELECT " + 
                 "  PM_Work_Order_ID AS Job, " + 
                 "  WO.Type, " +
                 "  WO.Category, " + 
                 "  Customer, " +
                 "  IFNULL(WO.Asset_ID,' ') AS Machine, " +
                 "  IFNULL(A.model_id, ' ') AS Model, " +
                 "  Promised_Date AS 'Promised Date' " + 
                 "FROM " + 
                 "  ( " + 
                 "  SELECT " + 
                 "    w.PM_Work_Order_ID, " + 
                 "    d.Name AS Customer, " + 
                 "    w.Asset_ID, " + 
                 "    IF(w.Promised_Date='0000-00-00','0000-00-00',w.Promised_Date) AS Promised_Date, " + 
                 "    w.type, " + 
                 "    w.category " + 
                 "  FROM " + 
                 "         (preventive_maintenance_work_order w JOIN division d ON w.Division_ID = d.Division_ID ) " + 
                 "  ) WO " + 
                 "  LEFT JOIN " + 
                 "    asset A " + 
                 "  ON " + 
                 "  A.asset_id = WO.asset_id ORDER BY Job DESC";
                 
         DataAssistantDialog d = new DataAssistantDialog(frame,"Select Job - Data Assistant",sql,connection);
         d.setFirstColumnWidth(80);
         d.setSecondColumnWidth(80);
         d.setThirdColumnWidth(80);
         d.setFourthColumnWidth(160);
         d.grow(650,0,630);
         d.setLocationRelativeTo(btnWO_DTL);
         d.setVisible(true); 
         String rtnVal = d.getValue();
         if(!rtnVal.equals("")){
             setWorkOrder(rtnVal);
         }
     }
     
     private void btnP_WO_actionPerformed(ActionEvent e) {
         String sql = "SELECT " + 
                 "  PM_Work_Order_ID AS Job, " + 
                 "  WO.Type, " +
                 "  WO.Category, " + 
                 "  Customer, " +
                 "  IFNULL(WO.Asset_ID,' ') AS Machine, " +
                 "  IFNULL(A.model_id, ' ') AS Model, " +
                 "  Promised_Date AS 'Promised Date' " + 
                 "FROM " + 
                 "  ( " + 
                 "  SELECT " + 
                 "    w.PM_Work_Order_ID, " + 
                 "    d.Name AS Customer, " + 
                 "    w.Asset_ID, " + 
                 "    IF(w.Promised_Date='0000-00-00','0000-00-00',w.Promised_Date) AS Promised_Date, " + 
                 "    w.type, " + 
                 "    w.category " + 
                 "  FROM " + 
                 "         (preventive_maintenance_work_order w JOIN division d ON w.Division_ID = d.Division_ID) WHERE w.Done_Date='0000-00-00 00:00:00' " + 
                 "  ) WO " + 
                 "  LEFT JOIN " + 
                 "    asset A " + 
                 "  ON " + 
                 "  A.asset_id = WO.asset_id ORDER BY Job DESC";
                 
         DataAssistantDialog d = new DataAssistantDialog(frame,"Select Job - Data Assistant",sql,connection);
         d.setFirstColumnWidth(80);
         d.setSecondColumnWidth(80);
         d.setThirdColumnWidth(80);
         d.setFourthColumnWidth(160);
         d.grow(650,0,630);
         d.setLocationRelativeTo(btnP_WO);
         d.setVisible(true); 
         String rtnVal = d.getValue();
         if(!rtnVal.equals("")){
             setWorkOrder(rtnVal);
         }
     }
     
     private void btnD_WO_actionPerformed(ActionEvent e) {
         String sql = "SELECT " + 
                 "  PM_Work_Order_ID AS Job, " + 
                 "  WO.Type, " +
                 "  WO.Category, " + 
                 "  Customer, " +
                 "  IFNULL(WO.Asset_ID,' ') AS Machine, " +
                 "  IFNULL(A.model_id, ' ') AS Model, " +
                 "  Promised_Date AS 'Promised Date' " + 
                 "FROM " + 
                 "  ( " + 
                 "  SELECT " + 
                 "    w.PM_Work_Order_ID, " + 
                 "    d.Name AS Customer, " + 
                 "    w.Asset_ID, " + 
                 "    IF(w.Promised_Date='0000-00-00','0000-00-00',w.Promised_Date) AS Promised_Date, " + 
                 "    w.type, " + 
                 "    w.category " + 
                 "  FROM " + 
                 "         (preventive_maintenance_work_order w JOIN division d ON w.Division_ID = d.Division_ID) WHERE w.Done_Date !='0000-00-00 00:00:00' " + 
                 "  ) WO " + 
                 "  LEFT JOIN " + 
                 "    asset A " + 
                 "  ON " + 
                 "  A.asset_id = WO.asset_id ORDER BY Job DESC";
                 
         DataAssistantDialog d = new DataAssistantDialog(frame,"Select Job - Data Assistant",sql,connection);
         d.setFirstColumnWidth(80);
         d.setSecondColumnWidth(80);
         d.setThirdColumnWidth(80);
         d.setFourthColumnWidth(160);
         d.grow(650,0,630);
         d.setLocationRelativeTo(btnD_WO);
         d.setVisible(true); 
         String rtnVal = d.getValue();
         if(!rtnVal.equals("")){
             setWorkOrder(rtnVal);
         }
     }

    private void btnInspectionLog_actionPerformed(ActionEvent e) {
        new InspectionDialog(pool,WORK_ORDER.workorder_id,frame, messageBar).setVisible(true);
    }

    private void btnReadingLog_actionPerformed(ActionEvent e) {
        new ReadingsDialog(pool,WORK_ORDER.workorder_id,frame, messageBar).setVisible(true);
    }

    private void btnUndone_actionPerformed(ActionEvent e) {
        if(MessageBar.showConfirmDialog(frame,"Are you sure you want to undone \nthe selected Job?","Undone Job")==MessageBar.YES_OPTION){    
            try {
                undo_job();
                if(!WORK_ORDER.asset_id.isEmpty()){
                    if(isLastJob(WORK_ORDER.workorder_id, WORK_ORDER.asset_id)){
                        undo_plan_changes();
                    }
                    else{
                        /**Do nothing*/
                    }
                }
                connection.commit();
            }
            catch(Exception er) {
                er.printStackTrace();
                messageBar.setMessage(er.getMessage(),"ERROR");
            }
        }
    }
    
    private boolean isLastJob(String job_no, String asset){
        return true;
    }
    
    private void undo_job() throws Exception {
        WORK_ORDER.started_date = txtStartedDate.getInputText();
        txtDoneDate.setInputText("0000-00-00 00:00:00");
        WORK_ORDER.done_date = txtDoneDate.getInputText();
        txtMeter.setInputText("");
        WORK_ORDER.remarks = taRemarks.getText();
        WORK_ORDER.update();
        
        /**Reset PMs*/
        int rows = tblWorkOrder.getRowCount();
        PreventiveMaintenanceLogEntry pmle = null;
        for(int i=0;i<rows;i++){
            pmle = new PreventiveMaintenanceLogEntry(connection);
            pmle.preventive_maintenance_log_id = tblWorkOrder.getValueAt(i,0).toString();
            pmle.scheduled_id = tblWorkOrder.getValueAt(i,1).toString();
            pmle.done_date = tblWorkOrder.getValueAt(i,6).toString();
            tblWorkOrder.setValueAt("0",i,7);
            pmle.done_meter = tblWorkOrder.getValueAt(i,7).toString();;
            pmle.time_taken = tblWorkOrder.getValueAt(i,9).toString();
            pmle.success = tblWorkOrder.getValueAt(i,11).toString();
            pmle.remarks = tblWorkOrder.getValueAt(i,12).toString();
            pmle.log();
        }
    }
    
    private void undo_plan_changes() throws Exception {
        PlanningAsset planningAsset = new PlanningAsset(WORK_ORDER.asset_id,connection);
        planningAsset.deletePlan();
        planningAsset.planOrExtend();
    }

    private void btnViewCompletion_actionPerformed(ActionEvent e) {
        try {
            int gap = Utilities.getDateDifference(txtPromisedDate.getInputText(), txtStartedDate.getInputText() ,connection);
            GUIJobStatus dlg = new GUIJobStatus(frame,WORK_ORDER.workorder_id, WORK_ORDER.customer, gap, connection);
            dlg.setLocationRelativeTo(frame);
            dlg.setVisible(true);
        }
        catch (Exception er) {
            er.printStackTrace();
        }
        
    }

    private class WorkOrderTable extends JTable {
         private WorkOrderTableModel myModel = null;
              
         public WorkOrderTable(){
             myModel = new WorkOrderTableModel();
             this.setModel(myModel);
             
             this.setSelectionMode(0);
             this.getTableHeader().setReorderingAllowed(false);
             this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
             
             TableColumn tc = this.getColumnModel().getColumn(6);  
             tc.setCellEditor(new DefaultCellEditor(new DateTimeCell(frame)));
             tc = this.getColumnModel().getColumn(9);  
             tc.setCellEditor(new DefaultCellEditor(new StandardTimeCell(frame)));
             
             this.getColumnModel().getColumn(2).setCellRenderer(new LockedLabelCellRenderer());
             this.getColumnModel().getColumn(3).setCellRenderer(new LockedLabelCellRenderer());
             this.getColumnModel().getColumn(4).setCellRenderer(new LockedLabelCellRenderer());
             this.getColumnModel().getColumn(5).setCellRenderer(new LockedLabelCellRenderer());
             this.getColumnModel().getColumn(8).setCellRenderer(new LockedLabelCellRenderer());
             this.getColumnModel().getColumn(10).setCellRenderer(new ButtonCellRenderer());
             this.getColumnModel().getColumn(12).setCellRenderer(new ButtonCellRenderer());
             
             this.getColumnModel().getColumn(0).setMinWidth(0);
             this.getColumnModel().getColumn(0).setPreferredWidth(0);
             this.getColumnModel().getColumn(1).setMinWidth(0);
             this.getColumnModel().getColumn(1).setPreferredWidth(0);
             
             this.getColumnModel().getColumn(2).setPreferredWidth(50);
             this.getColumnModel().getColumn(3).setPreferredWidth(200);
             this.getColumnModel().getColumn(4).setPreferredWidth(80);
             this.getColumnModel().getColumn(5).setPreferredWidth(85);
             this.getColumnModel().getColumn(6).setPreferredWidth(115);
             this.getColumnModel().getColumn(7).setPreferredWidth(80);
             this.getColumnModel().getColumn(8).setPreferredWidth(80);
             this.getColumnModel().getColumn(9).setPreferredWidth(60);
             this.getColumnModel().getColumn(10).setPreferredWidth(40);
             this.getColumnModel().getColumn(11).setPreferredWidth(50);
             this.getColumnModel().getColumn(12).setPreferredWidth(80);
         }
         
         public void populate(String workorder_id) throws Exception{
            String sql = "SELECT " +
                            "log.Preventive_Maintenance_Log_ID, " +
                            "log.Scheduled_ID, " +
                            "sch.Preventive_Maintenance_ID, " +
                            "pm.Description, " +
                            "sch.Modified_Date," +
                            "sch.Modified_Meter," +
                            "IF(log.Done_Date='0000-00-00 00:00:00','0000-00-00 00:00:00',log.Done_Date) AS Done_Date, " +
                            "log.Done_Meter, " +
                            "pmr.Standard_Time, " +
                            "log.Time_Taken, " +
                            "log.Success, " +
                            "log.Remarks " +
                          "FROM " +
                             "preventive_maintenance_work_order wo, " +
                             "preventive_maintenance_log log, " +
                             "scheduled_preventive_maintenance sch," +
                             "preventive_maintenance_register pmr, " +
                             "preventive_maintenance pm " +
                          "WHERE " +
                             "wo.PM_Work_Order_ID=? AND " +
                             "wo.PM_Work_Order_ID = log.PM_Work_Order_ID AND " +
                             "log.Scheduled_ID = sch.Scheduled_ID AND " +
                             "sch.Preventive_Maintenance_ID = pmr.Preventive_Maintenance_ID AND " +
                             "sch.Asset_ID = pmr.Asset_ID AND " +
                             "pm.ID = sch.Preventive_Maintenance_ID";
                             
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,workorder_id);
                             
             ResultSet rec = stmt.executeQuery();
             this.deleteAll();
             int cnt = 0;
             while(rec.next()){
                 this.addRow();
                 cnt = rec.getRow()-1;
                 this.setValueAt(rec.getString("Preventive_Maintenance_Log_ID"),cnt,0);
                 this.setValueAt(rec.getString("Scheduled_ID"),cnt,1);
                 this.setValueAt(rec.getString("Preventive_Maintenance_ID"),cnt,2);
                 this.setValueAt(rec.getString("Description"),cnt,3);
                 this.setValueAt(rec.getString("Modified_Date"),cnt,4);
                 this.setValueAt(rec.getString("Modified_Meter"),cnt,5);
                 this.setValueAt(rec.getString("Done_Date"),cnt,6);
                 this.setValueAt(rec.getString("Done_Meter"),cnt,7);
                 txtMeter.setInputText(this.getValueAt(cnt,7).toString());
                 this.setValueAt(rec.getString("Standard_Time"),cnt,8);
                 this.setValueAt(rec.getString("Time_Taken"),cnt,9);
                 this.setValueAt(rec.getInt("Success"),cnt,11);
                 this.setValueAt(rec.getString("Remarks"),cnt,12);
                 
                 
             }
         }
         
         public void setAllDoneDatesTo(String done_date){
            int rows = this.getRowCount();
            for(int i=0;i<rows;i++){
                this.setValueAt(done_date,i,6);   
            }
            this.repaint();
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
              
         private class WorkOrderTableModel extends AbstractTableModel{
             private String [] colNames = {
                      "Log_ID",             //0
                      "Scheduled_ID",       //1
                      "PM ID",              //2
                      "Description",        //3
                      "Scheduled Date",     //4
                      "Scheduled Meter",    //5
                      "Done Date",          //6
                      "Done Meter",         //7
                      "Standard Time",      //8
                      "Time Taken",         //9
                      "Parts Utilisation",  //10
                      "Success(%)",         //11
                      "Remarks"             //12
                      };
             private Object [][] valueArray = null;
             private Object [][] tempArray = null;
         
             public WorkOrderTableModel() {
                 valueArray = new Object[0][colNames.length];
             }
             
             public String getColumnName(int c){ 
                 return colNames[c];  
             }
             
             public Class getColumnClass(int c){
                 return getValueAt(0,c).getClass();
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
                 if(col==11){
                     if(validSuccess(value)){
                         valueArray[row][col] = (int)Double.parseDouble(value.toString());
                     }
                     else{
                         valueArray[row][col] = "0";
                     }
                 }
                 else{
                    valueArray[row][col] = value;
                 }
             }
                  
             private boolean validSuccess(Object value){
                if(!Validator.isEmpty(value.toString()) && Validator.isNonNegative(value.toString())){
                    if(Double.parseDouble(value.toString())<=100){
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
                  
             public boolean isCellEditable(int r,int c){
                return (c==6 || c==9 || c==11);
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
     
    private class RequestedTasksTable extends JTable {
          private RequestedTasksTableModel myModel = null;
                   
          public RequestedTasksTable(){
              myModel = new RequestedTasksTableModel();
              this.setModel(myModel);
              
              this.setSelectionMode(0);
              this.getTableHeader().setReorderingAllowed(false);
              this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
              
              TableColumn tc = this.getColumnModel().getColumn(2);  
              tc.setCellEditor(new DefaultCellEditor(new DateTimeCell(frame)));
              tc = this.getColumnModel().getColumn(3);  
              tc.setCellEditor(new DefaultCellEditor(new StandardTimeCell(frame)));
              
              this.getColumnModel().getColumn(0).setCellRenderer(new LockedLabelCellRenderer());
              this.getColumnModel().getColumn(1).setCellRenderer(new LockedLabelCellRenderer());
              this.getColumnModel().getColumn(4).setCellRenderer(new ButtonCellRenderer());
              this.getColumnModel().getColumn(6).setCellRenderer(new ButtonCellRenderer());
              
              this.getColumnModel().getColumn(0).setPreferredWidth(100);
              this.getColumnModel().getColumn(1).setPreferredWidth(300);
              this.getColumnModel().getColumn(2).setPreferredWidth(115);
              this.getColumnModel().getColumn(3).setPreferredWidth(80);
              this.getColumnModel().getColumn(4).setPreferredWidth(100);
              this.getColumnModel().getColumn(5).setPreferredWidth(80);
              this.getColumnModel().getColumn(6).setPreferredWidth(80);
          }
          
          public void populate(String workorder_id) throws Exception{
             String sql = "SELECT " +
                             "log.Task_ID, " +
                             "task.Description, " +
                             "IF(log.Done_Date='0000-00-00 00:00:00','0000-00-00 00:00:00',log.Done_Date) AS Done_Date, " +
                             "log.Time_Taken, " +
                             "log.Success, " +
                             "log.Remarks " +
                           "FROM " +
                              "requested_maintenance_log log, " +
                              "upmaintenance_master task " +
                           "WHERE " +
                              "log.PM_Work_Order_ID=? AND " +
                              "log.Task_ID = task.UPM_ID";
                              
             PreparedStatement stmt = connection.prepareStatement(sql);
             stmt.setString(1,workorder_id);
                              
              ResultSet rec = stmt.executeQuery();
              this.deleteAll();
              int cnt = 0;
              while(rec.next()){
                  this.addRow();
                  cnt = rec.getRow()-1;
                  this.setValueAt(rec.getString("Task_ID"),cnt,0);
                  this.setValueAt(rec.getString("Description"),cnt,1);
                  this.setValueAt(rec.getString("Done_Date"),cnt,2);
                  this.setValueAt(rec.getString("Time_Taken"),cnt,3);
                  this.setValueAt(rec.getString("Success"),cnt,5);
                  this.setValueAt(rec.getString("Remarks"),cnt,6);
              }
          }
          
          public void setAllDoneDatesTo(String done_date){
             int rows = this.getRowCount();
             for(int i=0;i<rows;i++){
                 this.setValueAt(done_date,i,6);   
             }
             this.repaint();
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
               
          private class RequestedTasksTableModel extends AbstractTableModel{
              private String [] colNames = {
                       "UPM ID",             //0
                       "Description",        //1
                       "Started Date",          //2
                       "Time Taken",         //3
                       "Parts Utilisation",  //4
                       "Success(%)",         //5
                       "Remarks"             //6
                       };
              private Object [][] valueArray = null;
              private Object [][] tempArray = null;
          
              public RequestedTasksTableModel() {
                  valueArray = new Object[0][colNames.length];
              }
              
              public String getColumnName(int c){ 
                  return colNames[c];  
              }
              
              public Class getColumnClass(int c){
                  return getValueAt(0,c).getClass();
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
                  if(col==5){
                      if(validSuccess(value)){
                          valueArray[row][col] = (int)Double.parseDouble(value.toString());
                      }
                      else{
                          valueArray[row][col] = "0";
                      }
                  }
                  else{
                     valueArray[row][col] = value;
                  }
              }
                   
              private boolean validSuccess(Object value){
                 if(!Validator.isEmpty(value.toString()) && Validator.isNonNegative(value.toString())){
                     if(Double.parseDouble(value.toString())<=100){
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
                   
              public boolean isCellEditable(int r,int c){
                  return (c==2 || c==3 || c==5);
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
      
     private class JobCompletionRemarksTable extends JTable {
          private JobCompletionRemarksTableModel myModel = null;
               
          public JobCompletionRemarksTable(){
              myModel = new JobCompletionRemarksTableModel();
              this.setModel(myModel);
              
              this.setSelectionMode(0);
              this.getTableHeader().setReorderingAllowed(false);
              this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                                        
              this.getColumnModel().getColumn(0).setPreferredWidth(150);
              this.getColumnModel().getColumn(1).setPreferredWidth(300);
          }
          
          public void populate(String asset_id) throws Exception{
             String sql = "SELECT CAST(p.Done_Date as CHAR) as Done_Date, p.Remarks " + 
                          "FROM preventive_maintenance_work_order p " + 
                          "WHERE p.Asset_ID = ? " +
                          "AND p.Remarks <> '' " +
                          "Order by Done_Date DESC;";
                          
             PreparedStatement stmt = connection.prepareStatement(sql);
             stmt.setString(1,asset_id);
                              
              ResultSet rec = stmt.executeQuery();
              this.deleteAll();
              int cnt = 0;
              while(rec.next()){
                  this.addRow();
                  cnt = rec.getRow()-1;
                  this.setValueAt(rec.getString("Done_Date"),cnt,0);                  
                  this.setValueAt(rec.getString("Remarks"),cnt,1);
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
               
          private class JobCompletionRemarksTableModel extends AbstractTableModel{
              private String [] colNames = {
                       "Done Date",          //1
                       "Remarks"             //2
                       };
              private Object [][] valueArray = null;
              private Object [][] tempArray = null;
          
              public JobCompletionRemarksTableModel() {
                  valueArray = new Object[0][colNames.length];
              }
              
              public String getColumnName(int c){ 
                  return colNames[c];  
              }
              
              public Class getColumnClass(int c){
                  return getValueAt(0,c).getClass();
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
 }
