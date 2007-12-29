package com.matrix.focus.workorder;

import com.matrix.components.MDataCombo;
import com.matrix.components.MDatebox;
import com.matrix.components.MTextbox;
import com.matrix.components.TitleBar;
import com.matrix.focus.log.PreventiveMaintenanceLogEntry;
import com.matrix.focus.util.MPanel;
import com.matrix.focus.mdi.MDI;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.ButtonCellRenderer;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.DataAssistantDialog;
import com.matrix.focus.util.ImageLibrary;
import com.matrix.focus.util.JasReport;
import com.matrix.focus.util.LockedLabelCellRenderer;
import com.matrix.focus.connect.ODBCConnection;
import com.matrix.focus.util.Utilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Savepoint;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

public class GUIPreventiveMaintenanceWorkOrder extends MPanel {
      
    private JPanel jPanel2 = new JPanel();
    private MDatebox txtFromDate = new MDatebox();
    private MDatebox txtToDate = new MDatebox();
    private JPanel jPanel3 = new JPanel();
    private MTextbox mtxtWorkorder = new MTextbox();
    private JScrollPane jScrollPane4 = new JScrollPane();
    private JButton btnSave = new JButton(new ImageIcon(ImageLibrary.BUTTON_SAVE));
    private JButton btnNew = new JButton(new ImageIcon(ImageLibrary.BUTTON_NEW));
    private JButton btnRemove = new JButton(new ImageIcon(ImageLibrary.BUTTON_REMOVE));
    private JButton btnAddReqTask = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));
    private JButton btnRemoveReqTask = new JButton(new ImageIcon(ImageLibrary.BUTTON_REMOVE));
    private JButton btnEmployees = new JButton(new ImageIcon(ImageLibrary.BUTTON_CREW));
    private JButton btnEdit = new JButton(new ImageIcon(ImageLibrary.BUTTON_EDIT));
    private JButton btnCancel = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
    private JButton btnWorkOrder = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private MDataCombo mDataCombo1 = new MDataCombo();
    private TitleBar titlebar = new TitleBar();
    private JButton btnDeleteWorkorder = new JButton(new ImageIcon(ImageLibrary.BUTTON_DELETE));
    private JPanel jPanel5 = new JPanel();
    private MTextbox txtCustomer = new MTextbox();
    private MTextbox txtAsset = new MTextbox();
    private JButton btnCustomer = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton btnAsset = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton btnRefreshSchedule = new JButton(new ImageIcon(ImageLibrary.BUTTON_REFRESH));
    private MDatebox txtPromisedDate = new MDatebox();
    private JPanel jPanel1 = new JPanel();
    private JScrollPane spScheduleEntries = new JScrollPane();
    private WorkOrderTable tblWorkOrder;
    private LabourAllocationDialog dlgLabour;
    private PlannedJobsPartAllocationDialog dlgPA;
    private JPopupMenu popupMenu;
    private JMenuItem menuItemWorkOrder;
    private ScheduledMaintenanceTable tblScheduledMaintenance = new ScheduledMaintenanceTable();
    private Connection connection;
    private MDI frame;
    private MessageBar messageBar;
    private PreventiveMaintenanceWorkOrder WORK_ORDER;
    private Savepoint spMain;
    private boolean ADDING_TO_WORKORDER;
    private String SELECTED_CUSTOMER = "";
    private JTabbedPane tabTables = new JTabbedPane();
    private JPanel jPanel4 = new JPanel();
    private JPanel jPanel6 = new JPanel();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private RequestedTable tblRequested = new RequestedTable();
    private UPMTasksPickerDialog dlgUPMTasks;
    private RequsetedJobsPartAllocationDialog dlgRPA;
    private MTextbox txtWODate = new MTextbox();
    private ODBCConnection odbc = new ODBCConnection("TAG","mit","matrixit");
    private Connection tagConnection;
    private JScrollPane jScrollPane2 = new JScrollPane();
    private JTextArea txtDescription = new JTextArea();
    private JLabel jLabel1 = new JLabel();
    private MDataCombo cmbCategory = new MDataCombo();
    private JButton btnPrint = new JButton(new ImageIcon(ImageLibrary.BUTTON_PRINT));
    private MDataCombo cmbType = new MDataCombo();
    private JTextField txtModel = new JTextField();
    private JButton btnWO_DTL = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));

    public GUIPreventiveMaintenanceWorkOrder(DBConnectionPool pool,final MDI frame, MessageBar msgBar) {
        /**New Database connection was taken to manage the transactions*/
        
        this.connection = pool.getConnection();
        //try{
        //    this.connection.setAutoCommit(false);
        //}
        //catch(Exception er){}
        
        this.frame = frame;
        this.messageBar = msgBar;
        try {
            titlebar.setTitle("Job");
            titlebar.setDescription("The facility to create and manage maintenance jobs.");
            titlebar.setImage(ImageLibrary.TITLE_PREVENTIVE_MAINTENANCE_WORKORDER);
            
            
            cmbType.addItem("CHARGEABLE");
            cmbType.addItem("NONE CHARGEABLE");
            setCategories("CHARGEABLE");
            cmbType.getComboBox().addItemListener(
                new ItemListener(){
                    public void itemStateChanged(ItemEvent ie){
                        if(cmbType.getSelectedItem()!=null){
                            setCategories(cmbType.getSelectedItem().toString());
                        }
                    }
                }
            );
            
            tblWorkOrder = new WorkOrderTable(connection);
            
            jbInit();
                                    
            tblWorkOrder.addMouseListener(
                new MouseAdapter(){
                     public void mouseClicked(MouseEvent me){
                         if(me.getButton()==1 && tblWorkOrder.getSelectedColumn()==6 && tblWorkOrder.isEnabled()){
                             int row = tblWorkOrder.getSelectedRow();
                             if(row!=-1){
                                dlgPA = new PlannedJobsPartAllocationDialog(frame,connection,messageBar,true);
                                dlgPA.populate(
                                    txtAsset.getInputText(),
                                    tblWorkOrder.getValueAt(row,0).toString(),
                                    tblWorkOrder.getValueAt(row,1).toString(),
                                    tblWorkOrder.getValueAt(row,2).toString()
                                );
                                dlgPA.setLocationRelativeTo(frame);
                                dlgPA.setVisible(true); 
                             }
                             else{
                                 messageBar.setMessage("Select a row in the tasks table.","ERROR");
                             } 
                         }
                     }
                }
            );
            
            tblRequested.addMouseListener(new MouseAdapter(){
                     public void mouseClicked(MouseEvent me){
                         int row = tblRequested.getSelectedRow();
                         if(me.getButton()==1 && tblRequested.isEnabled() && row!=-1){
                            if(tblRequested.getSelectedColumn()==3){
                                dlgRPA = new RequsetedJobsPartAllocationDialog(frame,connection,messageBar,true);
                                dlgRPA.setLocationRelativeTo(frame);
                                dlgRPA.populate(mtxtWorkorder.getInputText(),txtAsset.getInputText(),tblRequested.getValueAt(row,0).toString());
                                dlgRPA.setVisible(true);
                            }
                        }
                     }
            });
            
            popupMenu = new JPopupMenu();
            menuItemWorkOrder = new JMenuItem(new ImageIcon(ImageLibrary.MENU_PREVENTIVE_WORKORDER));
            menuItemWorkOrder.setFont(new Font("System",0,12));
            popupMenu.add(menuItemWorkOrder);
            menuItemWorkOrder.addActionListener(
                new ActionListener(){
                   public void actionPerformed(ActionEvent e) {
                       int row = tblScheduledMaintenance.getSelectedRow();
                       String wo = tblScheduledMaintenance.getValueAt(row,5).toString().trim();
                       setWorkOrder(wo);
                   }
                }
            );
            
            tblScheduledMaintenance.addMouseListener(new MouseAdapter(){
               public void mouseClicked(MouseEvent me){
                   if(me.getButton()==3){
                       int row = tblScheduledMaintenance.getSelectedRow();
                       if(row!=-1){
                           String wo = tblScheduledMaintenance.getValueAt(row,5).toString().trim();
                           if(!wo.equals("-NO-")){
                               menuItemWorkOrder.setText("Go to Job "+ wo);
                               popupMenu.show(tblScheduledMaintenance,me.getPoint().x, me.getPoint().y);
                           }
                       }
                   }
               }
            });
            
            tblScheduledMaintenance.addMouseListener(
                new MouseAdapter(){
                    public void mouseClicked(MouseEvent e){
                        int r = tblScheduledMaintenance.getSelectedRow();
                        if(e.getClickCount()==2 && r!=-1){
                            /**Only WORK_ORDER_CREATOR can add new taska to jobs*/
                            if(true){
                                if(ADDING_TO_WORKORDER){
                                    String scheduled_id = tblScheduledMaintenance.getValueAt(r,0).toString();
                                    String pm_id = tblScheduledMaintenance.getValueAt(r,1).toString();
                                    String description = tblScheduledMaintenance.getValueAt(r,2).toString();
                                    String modified_date = tblScheduledMaintenance.getValueAt(r,3).toString();
                                    String modified_meter = tblScheduledMaintenance.getValueAt(r,4).toString();
                                    String work_order = tblScheduledMaintenance.getValueAt(r,5).toString();
                                                                       
                                    if(work_order.equals("-NO-")){
                                        if(!isExisting(scheduled_id)){
                                            addToWorkOrder(scheduled_id,pm_id,description,modified_date,modified_meter);
                                        }
                                        else{
                                            messageBar.setMessage("Selected Maintenance has been already added to the current Job.","ERROR");
                                        }
                                    }
                                    else{
                                        messageBar.setMessage("Selected Maintenance has been already added to another Job.","ERROR");
                                    }
                                }
                                else{
                                    messageBar.setMessage("You can not add Maintenance in this mode.","ERROR");
                                }
                            }
                        }
                    }
                }
            );
                        
            /**Mode*/
            setMode("LOAD");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void setCategories(String type){
        cmbCategory.removeAllItems();
        if(type.equals("CHARGEABLE")){
            cmbCategory.addItem("SERVICE");
            cmbCategory.addItem("BREAKDOWN");
            cmbCategory.addItem("REPAIR");
            cmbCategory.addItem("FABRICATION");
        }
        else if(type.equals("NONE CHARGEABLE")){
            cmbCategory.addItem("STARTUP");
            cmbCategory.addItem("WARRANTY");
            cmbCategory.addItem("OTHER");
        }
    }
    
    public void close(){
        try {
            //connection.close();
            //tagConnection.close();
        } catch (Exception e) {
        }
    }
    
    private void addToWorkOrder(String scheduled_id,String pm_id,String description,String modified_date,String modified_meter){
        PreventiveMaintenanceLogEntry pmle = new PreventiveMaintenanceLogEntry(connection);
        pmle.scheduled_id = scheduled_id;
        pmle.pm_work_order_id = mtxtWorkorder.getInputText();
        try{
            pmle.save();
            WorkOrderTask task = new WorkOrderTask();
            task.log_id = pmle.preventive_maintenance_log_id;
            task.scheduled_id = scheduled_id;
            task.pm_id = pm_id;
            task.description = description;
            task.planed_date = modified_date;
            task.planed_meter = modified_meter;
            tblWorkOrder.addRow(task);
            messageBar.setMessage("Selected maintenance was added.","OK");
        } 
        catch (Exception ex){
            messageBar.setMessage(ex.getMessage(),"ERROR");
            ex.printStackTrace();
        } 
    }
    
    private boolean isExisting(String scheduled_id){
        int rows = tblWorkOrder.getRowCount();
        for(int i=0;i<rows;i++){
            String s_id = tblWorkOrder.getValueAt(i,1).toString();
            if(s_id.equals(scheduled_id)){
                return true;
            }
        }
        return false;
    }

    private void setMode(String mode){
        if(mode.equals("NEW_WORKORDER")){
            clearAll();
            ADDING_TO_WORKORDER = true;
            btnRemove.setEnabled(true);
            btnAddReqTask.setEnabled(true);
            btnRemoveReqTask.setEnabled(true);
            btnNew.setEnabled(false);
            btnEmployees.setEnabled(true);
            btnEdit.setEnabled(false);
            btnSave.setEnabled(true);
            btnDeleteWorkorder.setEnabled(true);
            btnCancel.setEnabled(true);
            tblWorkOrder.setEnabled(true);
            tblRequested.setEnabled(true);
            btnCustomer.setEnabled(true);
            btnAsset.setEnabled(true);
            txtPromisedDate.getButton().setEnabled(true);
            txtDescription.setEditable(true);
            cmbType.getComboBox().setEnabled(true);
            cmbCategory.getComboBox().setEnabled(true);
            btnPrint.setEnabled(false);
        }
        else if(mode.equals("LOAD")){
            ADDING_TO_WORKORDER = false;
            btnNew.setEnabled(true);
            btnEmployees.setEnabled(false);
            btnDeleteWorkorder.setEnabled(false);
            btnSave.setEnabled(false);
            btnRemove.setEnabled(false);
            btnAddReqTask.setEnabled(false);
            btnRemoveReqTask.setEnabled(false);
            btnEdit.setEnabled(false);
            btnCancel.setEnabled(false);
            tblWorkOrder.setEnabled(false);
            tblRequested.setEnabled(false);
            btnCustomer.setEnabled(false);
            btnAsset.setEnabled(false);
            txtPromisedDate.getButton().setEnabled(false);
            txtDescription.setEditable(false);
            cmbType.getComboBox().setEnabled(false);
            cmbCategory.getComboBox().setEnabled(false);
            btnPrint.setEnabled(false);
            clearAll();
        }
        else if(mode.equals("EDIT")){
            ADDING_TO_WORKORDER = true;
            btnDeleteWorkorder.setEnabled(true);
            btnSave.setEnabled(true);
            btnRemove.setEnabled(true);
            btnAddReqTask.setEnabled(true);
            btnRemoveReqTask.setEnabled(true);
            btnNew.setEnabled(false);
            btnEmployees.setEnabled(true);
            btnEdit.setEnabled(false);
            btnCancel.setEnabled(true);
            tblWorkOrder.setEnabled(true);
            tblRequested.setEnabled(true);
            btnCustomer.setEnabled(true);
            btnAsset.setEnabled(true);
            txtPromisedDate.getButton().setEnabled(true);
            txtDescription.setEditable(true);
            cmbType.getComboBox().setEnabled(true);
            cmbCategory.getComboBox().setEnabled(true);
            btnPrint.setEnabled(false);
        }
        else if(mode.equals("SEARCH")){
            ADDING_TO_WORKORDER = false;
            btnEmployees.setEnabled(false);
            btnRemove.setEnabled(false);
            btnAddReqTask.setEnabled(false);
            btnRemoveReqTask.setEnabled(false);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnNew.setEnabled(false);
            btnDeleteWorkorder.setEnabled(true);
            btnEdit.setEnabled(true);
            tblWorkOrder.setEnabled(false);
            tblRequested.setEnabled(false);
            btnCustomer.setEnabled(false);
            btnAsset.setEnabled(false);
            txtPromisedDate.getButton().setEnabled(false);
            txtDescription.setEditable(false);
            cmbType.getComboBox().setEnabled(false);
            cmbCategory.getComboBox().setEnabled(false);
            btnPrint.setEnabled(true);
        }
    }
    
    private void clearAll(){
        mtxtWorkorder.setInputText("");
        txtCustomer.setInputText("");
        txtAsset.setInputText("");
        txtModel.setText("");
        txtWODate.setInputText("");
        txtPromisedDate.setInputText("");
        txtDescription.setText("");
        txtFromDate.setInputText("");
        txtToDate.setInputText("");
        tblScheduledMaintenance.deleteAll();
        tblWorkOrder.deleteAll();
        tblRequested.deleteAll();
    }
    
    private void jbInit() throws Exception {
        this.setLayout( null );
        this.setSize(new Dimension(988, 624));
        jPanel2.setBounds(new Rectangle(470, 85, 480, 65));
        jPanel2.setBorder(BorderFactory.createTitledBorder("Time Period Selection"));
        jPanel2.setLayout(null);
        txtFromDate.setBounds(new Rectangle(30, 25, 150, 20));
        txtFromDate.setCaption("From Date");
        txtFromDate.setLblWidth(65);
        txtFromDate.setLblFont(new Font("Tahoma", 0, 11));
        txtFromDate.setTxtFont(new Font("Tahoma", 0, 11));
        txtToDate.setBounds(new Rectangle(215, 25, 150, 20));
        txtToDate.setCaption("To Date");
        txtToDate.setLblWidth(65);
        txtToDate.setLblFont(new Font("Tahoma", 0, 11));
        txtToDate.setTxtFont(new Font("Tahoma", 0, 11));
        jPanel3.setBounds(new Rectangle(10, 85, 460, 230));
        jPanel3.setBorder(BorderFactory.createTitledBorder("Job Information"));
        jPanel3.setLayout(null);
        mtxtWorkorder.setBounds(new Rectangle(25, 25, 195, 20));
        mtxtWorkorder.setCaption("Job No :");
        mtxtWorkorder.setTxtWidth(100);
        mtxtWorkorder.setEditable(false);
        mtxtWorkorder.setTxtFont(new Font("Tahoma", 0, 11));
        mtxtWorkorder.setLblFont(new Font("Tahoma", 0, 11));
        mtxtWorkorder.setTxtForeColor(Color.RED);
        jScrollPane4.setBounds(new Rectangle(5, 5, 865, 150));
        btnSave.setText("Save");
        btnSave.setBounds(new Rectangle(675, 565, 130, 30));
        btnSave.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnSave_actionPerformed(e);
                    }
                });
        btnSave.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnSave_actionPerformed(e);
                    }
                });
        btnNew.setText("New Job");
        btnNew.setBounds(new Rectangle(270, 565, 130, 30));
        btnNew.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnNew_actionPerformed(e);
                    }
                });
        btnRemove.setBounds(new Rectangle(875, 5, 35, 30));
        btnRemove.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnRemove_actionPerformed(e);
                    }
                });
        btnEmployees.setText("Maintenance Crew");
        btnEmployees.setBounds(new Rectangle(10, 205, 130, 25));
        btnEmployees.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnEmployees_actionPerformed(e);
                    }
                });
        btnEdit.setText("Edit");
        btnEdit.setBounds(new Rectangle(540, 565, 130, 30));
        btnEdit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnEdit_actionPerformed(e);
                    }
                });
        btnCancel.setText("Cancel");
        btnCancel.setBounds(new Rectangle(810, 565, 130, 30));
        btnCancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnCancel_actionPerformed(e);
                    }
                });
        btnWorkOrder.setBounds(new Rectangle(225, 25, 30, 20));
        btnWorkOrder.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnWorkOrder_actionPerformed(e);
                    }
                });
        mDataCombo1.setBounds(new Rectangle(445, 165, 1, 1));
        titlebar.setBounds(new Rectangle(10, 10, 940, 70));
        btnDeleteWorkorder.setText("Delete Job");
        btnDeleteWorkorder.setBounds(new Rectangle(405, 565, 130, 30));
        btnDeleteWorkorder.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnDeleteWorkorder_actionPerformed(e);
                    }
                });
        jPanel5.setBounds(new Rectangle(470, 150, 480, 165));
        jPanel5.setLayout(null);
        jPanel5.setBorder(BorderFactory.createTitledBorder("Scheduled Maintenance"));
        txtCustomer.setBounds(new Rectangle(25, 50, 345, 20));
        txtCustomer.setCaption("Customer");
        txtCustomer.setTxtWidth(250);
        txtCustomer.setEditable(false);
        txtAsset.setBounds(new Rectangle(25, 75, 245, 20));
        txtAsset.setTxtWidth(150);
        txtAsset.setCaption("Machine");
        txtAsset.setEditable(false);
        btnCustomer.setBounds(new Rectangle(375, 50, 30, 20));
        btnCustomer.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnCustomer_actionPerformed(e);
                    }
                });
        btnAsset.setBounds(new Rectangle(275, 75, 30, 20));
        btnAsset.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnAsset_actionPerformed(e);
                    }
                });
        txtPromisedDate.setBounds(new Rectangle(25, 125, 220, 20));
        txtPromisedDate.setCaption("Promised Date");
        jPanel1.setBounds(new Rectangle(10, 315, 940, 240));
        jPanel1.setLayout(null);
        jPanel1.setBorder(BorderFactory.createTitledBorder("Job Tasks"));
        spScheduleEntries.setBounds(new Rectangle(10, 15, 460, 140));
        tabTables.setBounds(new Rectangle(10, 15, 920, 185));
        jPanel4.setLayout(null);
        jPanel6.setLayout(null);
        jScrollPane1.setBounds(new Rectangle(5, 5, 865, 150));
        txtWODate.setBounds(new Rectangle(255, 125, 175, 20));
        txtWODate.setCaption("Job Info:");
        txtWODate.setTxtWidth(120);
        txtWODate.setEditable(false);
        txtWODate.setLblWidth(60);
        jScrollPane2.setBounds(new Rectangle(15, 165, 430, 50));
        jLabel1.setText("Job Description");
        jLabel1.setBounds(new Rectangle(25, 150, 150, 10));
        cmbCategory.setBounds(new Rectangle(255, 100, 180, 20));
        cmbCategory.setLblWidth(60);
        cmbCategory.setCaption("Category");
        cmbCategory.setCmbWidth(120);
        btnPrint.setText("Print View");
        btnPrint.setBounds(new Rectangle(20, 565, 130, 30));
        btnPrint.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnPrint_actionPerformed(e);
                    }
                });
        cmbType.setBounds(new Rectangle(25, 100, 215, 20));
        cmbType.setCaption("Type");
        cmbType.setLblWidth(95);
        cmbType.setCmbWidth(120);
        txtModel.setBounds(new Rectangle(315, 75, 120, 20));
        txtModel.setEditable(false);
        btnWO_DTL.setText("Detail");
        btnWO_DTL.setBounds(new Rectangle(255, 25, 70, 20));
        btnWO_DTL.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnWO_DTL_actionPerformed(e);
                    }
                });
        btnAddReqTask.setBounds(new Rectangle(875, 5, 35, 30));
        btnAddReqTask.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnAddReqTask_actionPerformed(e);
                    }
                });
        btnRemoveReqTask.setBounds(new Rectangle(875, 40, 35, 30));
        btnRemoveReqTask.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnRemoveReqTask_actionPerformed(e);
                    }
                });
        btnRefreshSchedule.setBounds(new Rectangle(405, 25, 30, 20));
        btnRefreshSchedule.setToolTipText("Refresh Schedule");
        btnRefreshSchedule.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnRefreshSchedule_actionPerformed(e);
                    }
                });
        jPanel2.add(txtToDate, null);
        jPanel2.add(txtFromDate, null);
        jPanel2.add(btnRefreshSchedule, null);
        jScrollPane2.getViewport().add(txtDescription, null);
        jPanel3.add(btnWO_DTL, null);
        jPanel3.add(txtModel, null);
        jPanel3.add(cmbType, null);
        jPanel3.add(cmbCategory, null);
        jPanel3.add(jLabel1, null);
        jPanel3.add(jScrollPane2, null);
        jPanel3.add(txtWODate, null);
        jPanel3.add(txtPromisedDate, null);
        jPanel3.add(btnAsset, null);
        jPanel3.add(btnCustomer, null);
        jPanel3.add(txtAsset, null);
        jPanel3.add(txtCustomer, null);
        jPanel3.add(btnWorkOrder, null);
        jPanel3.add(mtxtWorkorder, null);
        jScrollPane4.getViewport().add(tblWorkOrder, null);
        jPanel4.add(jScrollPane4, null);
        jPanel4.add(btnRemove, null);
        tabTables.addTab("Scheduled Tasks", jPanel4);
        jScrollPane1.getViewport().add(tblRequested, null);
        jPanel6.add(btnRemoveReqTask, null);
        jPanel6.add(jScrollPane1, null);
        jPanel6.add(btnAddReqTask, null);
        tabTables.addTab("Other Tasks", jPanel6);
        jPanel1.add(tabTables, null);
        jPanel1.add(btnEmployees, null);
        this.add(jPanel1, null);
        jPanel5.add(spScheduleEntries, null);
        spScheduleEntries.setViewportView(tblScheduledMaintenance);
        this.add(jPanel5, null);
        this.add(btnDeleteWorkorder, null);
        this.add(titlebar, null);
        this.add(mDataCombo1, null);
        this.add(btnCancel, null);
        this.add(jPanel3, null);
        this.add(btnSave, null);
        this.add(btnEdit, null);
        this.add(btnNew, null);
        this.add(jPanel2, null);
        this.add(btnPrint, null);
    }

    private void btnDeleteWorkorder_actionPerformed(ActionEvent e){
        if(MessageBar.showConfirmDialog(frame,"Are you sure you want to delete \nthe selected Job from the system?","Delete Job")==MessageBar.YES_OPTION){
            if(!WORK_ORDER.done_date.equals("0000-00-00 00:00:00")){
                messageBar.setMessage("The Job has been logged. You can not delete it now.","ERROR");
            }
            else{
                if(MessageBar.showConfirmDialog(frame,"Please check weather there are partially done log information for the Job.\nDo you want to proceede Job deleting operation?","Delete Job")==MessageBar.YES_OPTION){
                    try{
                        WORK_ORDER.delete();
                        deleteWorkOrderTo_TAG(WORK_ORDER.workorder_id);
                        //connection.commit();
                        //tagConnection.commit();
                        setMode("LOAD");
                        messageBar.setMessage("Job deleted.","OK");
                    } 
                    catch (Exception ex){
                        messageBar.setMessage(ex.getMessage(),"ERROR");
                        ex.printStackTrace();
                    } 
                }
            }
        }
    }

    private void btnEmployees_actionPerformed(ActionEvent e) {
        /**Show the Labour Requirement Dialog*/
        dlgLabour = new LabourAllocationDialog(
                            frame,
                            messageBar,
                            tblWorkOrder,
                            tblRequested,
                            mtxtWorkorder.getInputText(),
                            txtAsset.getInputText(),
                            true,
                            txtPromisedDate.getInputText(),
                            connection);
        dlgLabour.populate(mtxtWorkorder.getInputText());
        dlgLabour.setLocationRelativeTo(frame);
        dlgLabour.setVisible(true);
    }

    private void btnNew_actionPerformed(ActionEvent e) {
        setMode("NEW_WORKORDER");
        WORK_ORDER = new PreventiveMaintenanceWorkOrder(connection);
        try{
            WORK_ORDER.type = cmbType.getSelectedItem().toString();
            WORK_ORDER.category = cmbCategory.getSelectedItem().toString();
            WORK_ORDER.creater = MDI.USERNAME;
            spMain = connection.setSavepoint("Main");
            WORK_ORDER.save();
            
            saveWorkOrderTo_TAG(WORK_ORDER.workorder_id);
            mtxtWorkorder.setInputText(WORK_ORDER.workorder_id);
            txtWODate.setInputText(WORK_ORDER.creater + " on " + WORK_ORDER.created_date);
            messageBar.setMessage("New Job created.","OK");
        } 
        catch(Exception er) {
            //try {
            //    connection.rollback(spMain);
            //}
            //catch (SQLException ser) {
            //    ser.printStackTrace();
            //}
            messageBar.setMessage(er.getMessage(),"ERROR");
            er.printStackTrace();
        }
    }
    
    private void saveWorkOrderTo_TAG(String workorder_id) throws Exception{
        try{
            this.tagConnection = odbc.getConnection();
        //    this.tagConnection.setAutoCommit(false);
        }
        catch(Exception er){
            er.printStackTrace();
        }
        PreparedStatement stmt = tagConnection.prepareStatement("INSERT INTO job_master (JOB_NO,JOB_DATE,CUST_CODE,JOB_TYPE,JOB_CATEGORY) VALUES(?,DATE(),'0',?,?)");
        stmt.setString(1,workorder_id);
        stmt.setString(2,WORK_ORDER.type);
        stmt.setString(3,WORK_ORDER.category);
        
        if(stmt.executeUpdate()!=1){
            throw new Exception("Job was not saved in TAG");
        }
    }
    
    private void updateWorkOrderTo_TAG(String workorder_id, String customer_id, String description, String type, String category) throws Exception{
        PreparedStatement stmt = tagConnection.prepareStatement("UPDATE job_master SET CUST_CODE = ?, Job_Description = ?, JOB_TYPE = ?, JOB_CATEGORY = ? WHERE  JOB_NO = ?");
        stmt.setString(1,customer_id);
        stmt.setString(2,description);
        stmt.setString(3,type);
        stmt.setString(4,category);
        stmt.setString(5,workorder_id);
        
        if(stmt.executeUpdate()!=1){
            throw new Exception("Job was not saved in TAG");
        }
    }
    
    private void deleteWorkOrderTo_TAG(String workorder_id) throws Exception{
        PreparedStatement stmt = tagConnection.prepareStatement("DELETE FROM job_master WHERE  JOB_NO = ?");
        stmt.setString(1,workorder_id);
        
        if(stmt.executeUpdate()!=1){
            throw new Exception("Job was not deleted from TAG");
        }
    }

    private void btnRemove_actionPerformed(ActionEvent e) {
        int row = tblWorkOrder.getSelectedRow();
        if(row!=-1){
            if(MessageBar.showConfirmDialog(frame,"Are sure you want to remove selected maintenance?","Job")==MessageBar.YES_OPTION){
                try{
                    deletePlannedJob(row);
                    messageBar.setMessage("Maintenance was deleted.","OK");
                }
                catch(Exception err){
                    messageBar.setMessage(err.getMessage(),"ERROR");
                }
            }
        }
        else{
            messageBar.setMessage("Select a Maintenance first.","ERROR");
        }
    }
    
    private void btnRemoveReqTask_actionPerformed(ActionEvent e) {
        int row = tblRequested.getSelectedRow();
        if(row!=-1){
            if(MessageBar.showConfirmDialog(frame,"Are sure you want to remove selected maintenance?","Job")==MessageBar.YES_OPTION){
                try{
                    deleteRequestedJob(row);
                    messageBar.setMessage("Maintenance was deleted.","OK");
                }
                catch(Exception err){
                    messageBar.setMessage(err.getMessage(),"ERROR");
                }
            }
        }
        else{
            messageBar.setMessage("Select a Maintenance first.","ERROR");
        }
    }
    
    private void deletePlannedJob(int row) throws Exception{
        PreventiveMaintenanceLogEntry pmle = new PreventiveMaintenanceLogEntry(connection);
        pmle.preventive_maintenance_log_id = tblWorkOrder.getValueAt(row,0).toString();
        pmle.delete();
        tblWorkOrder.deleteRow(row);
    }
    
    private void deleteRequestedJob(int row) throws Exception {
        RequestedTask requestedTask = new RequestedTask(connection);
        requestedTask.pm_work_order_id = mtxtWorkorder.getInputText();
        requestedTask.asset_id = txtAsset.getInputText();
        requestedTask.task_id = tblRequested.getValueAt(row,0).toString();
        requestedTask.delete();
        tblRequested.deleteRow(row);
    }
    
    private void btnEdit_actionPerformed(ActionEvent e) {
        //try {
        //    spMain = connection.setSavepoint("Main");
        //} 
        //catch(SQLException f) {
        //    f.printStackTrace();
        //}
        setMode("EDIT");
    }
    
    private void btnCancel_actionPerformed(ActionEvent e) {
        //try{
        //    connection.rollback(spMain);
        //    tagConnection.rollback();
        //} 
        //catch(Exception er) {}
        setMode("LOAD");
    }
    
    private void btnSave_actionPerformed(ActionEvent e){
        if(!txtAsset.getInputText().isEmpty()){
            messageBar.setMessage("You have not selected a Machine","WARN");
        }
            try{
                WORK_ORDER.type = cmbType.getSelectedItem().toString();
                WORK_ORDER.category = cmbCategory.getSelectedItem().toString();
                WORK_ORDER.promised_date = txtPromisedDate.getInputText();
                WORK_ORDER.asset_id = txtAsset.getInputText();
                WORK_ORDER.customer_id = SELECTED_CUSTOMER;
                WORK_ORDER.description = txtDescription.getText();
                WORK_ORDER.update();
                updateWorkOrderTo_TAG(WORK_ORDER.workorder_id,WORK_ORDER.customer_id,WORK_ORDER.description, WORK_ORDER.type, WORK_ORDER.category);
                //connection.commit();
                //tagConnection.commit();
                setMode("SEARCH");
                messageBar.setMessage("Job saved.","OK");
            }
            catch(Exception er){
                //try{
                //    connection.rollback(spMain);
                //}
                //catch (SQLException ser){}
                er.printStackTrace();
                messageBar.setMessage("Could not save Job. Check for empty feild.","ERROR");
            }
        //}
        //else{
        //    messageBar.setMessage("You must select the Machine before saving the Job.","ERROR");
        //}
    }

    private void btnWorkOrder_actionPerformed(ActionEvent e) {
        String sql = "SELECT PM_Work_Order_ID as 'Job', " +
                            "Created_Date AS 'Job Date', " +
                            "IF(Promised_Date='0000-00-00','0000-00-00',Promised_Date) AS 'Promsied Date', " +
                            "IF(Done_Date='0000-00-00 00:00:00','0000-00-00',DATE(Done_Date)) AS 'Done Date' FROM preventive_maintenance_work_order WHERE Done_Date ='0000-00-00 00:00:00' ORDER BY PM_Work_Order_ID DESC";
        
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select Job - Data Assistant",sql,connection);
        d.setFirstColumnWidth(80);
        d.setSecondColumnWidth(80);
        d.setThirdColumnWidth(80);
        d.setFourthColumnWidth(80);
        d.setLocationRelativeTo(jPanel3);
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
                "         (preventive_maintenance_work_order w JOIN division d ON w.Division_ID = d.Division_ID AND w.Done_Date ='0000-00-00 00:00:00') " + 
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
    
    public void setWorkOrder(String workorder_id){
        try{
            this.tagConnection = odbc.getConnection();
        //    this.tagConnection.setAutoCommit(false);
        }
        catch(Exception er){
            er.printStackTrace();
        }
        
        try{
            spMain = connection.setSavepoint("Main");
            WORK_ORDER = new PreventiveMaintenanceWorkOrder(workorder_id, connection);
            mtxtWorkorder.setInputText(WORK_ORDER.workorder_id);
            if(!WORK_ORDER.type.isEmpty()){
                cmbType.setSelectedItem(WORK_ORDER.type);
            }
            if(!WORK_ORDER.category.isEmpty()){
                cmbCategory.setSelectedItem(WORK_ORDER.category);
            }
            txtCustomer.setInputText(WORK_ORDER.customer);
            txtAsset.setInputText(WORK_ORDER.asset_id);
            txtModel.setText(WORK_ORDER.model);
            txtWODate.setInputText(WORK_ORDER.creater + " on " + WORK_ORDER.created_date);
            txtPromisedDate.setInputText(WORK_ORDER.promised_date);
            txtDescription.setText(WORK_ORDER.description);
            this.SELECTED_CUSTOMER = WORK_ORDER.customer_id;
            getPlannedJobs(workorder_id);
            getRequestedJobs(workorder_id);
            setMode("SEARCH");
        } 
        catch(Exception er) {
            messageBar.setMessage(er.getMessage(),"ERROR");
            er.printStackTrace();
        }
    }

    private void getPlannedJobs(String workorder_id) {
        try{
            String sql = "SELECT " +
                            "log.Preventive_Maintenance_Log_ID," +
                            "sch.Scheduled_ID," +
                            "sch.Preventive_Maintenance_ID," +
                            "pm.Description," +
                            "sch.Modified_Date," +
                            "sch.Modified_Meter " +
                         "FROM " +
                            "preventive_maintenance_work_order wo, " +
                            "preventive_maintenance_log log, " +
                            "scheduled_preventive_maintenance sch, " +
                            "preventive_maintenance pm " +
                         "WHERE " +
                            "wo.PM_Work_Order_ID= ? AND " +
                            "wo.PM_Work_Order_ID = log.PM_Work_Order_ID AND " +
                            "log.Scheduled_ID = sch.Scheduled_ID AND " +
                            "pm.ID = sch.Preventive_Maintenance_ID";
            
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,workorder_id);
            ResultSet rec = stmt.executeQuery();
            tblWorkOrder.deleteAll();
            WorkOrderTask task = null;
            while(rec.next()){
                task = new WorkOrderTask();
                task.log_id = rec.getString("Preventive_Maintenance_Log_ID");
                task.scheduled_id = rec.getString("Scheduled_ID");
                task.pm_id = rec.getString("Preventive_Maintenance_ID");
                task.description = rec.getString("Description");
                task.planed_date = rec.getString("Modified_Date");
                task.planed_meter = rec.getString("Modified_Meter");
                
                tblWorkOrder.addRow(task);
            }
        }
        catch(Exception er){
            er.printStackTrace();
        }
    }
    
    private void getRequestedJobs(String workorder_id){
        try{
            String sql = "SELECT " +
                                "log.Task_ID," +
                                "task.Description," +
                                "task.UPM_Category " +
                         "FROM " +
                                "requested_maintenance_log log, " +
                                "upmaintenance_master task " +
                         "WHERE " +
                                "PM_Work_Order_ID = ? AND " +
                                "log.Task_ID = task.UPM_ID ";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,workorder_id);
            ResultSet rec = stmt.executeQuery();
            tblRequested.deleteAll();
            UPMTask task = null;
            while(rec.next()){
                task = new UPMTask(null);
                task.upm_id = rec.getString("Task_ID");
                task.description = rec.getString("Description");
                task.category = rec.getString("UPM_Category");
                tblRequested.addTask(task);
            }
        } 
        catch(Exception ex){
            ex.printStackTrace();
        } 
    }

    private void btnCustomer_actionPerformed(ActionEvent e) {
        if(tblWorkOrder.getRowCount()==0){
            String sql = "SELECT division_id AS ID, Name, Address " +
                         "FROM division " +
                         "WHERE division_id IN (SELECT division_id FROM asset) ORDER BY Name";
                         
            DataAssistantDialog d = new DataAssistantDialog(frame,"Select Customer - Data Assistant",sql,connection);
            d.grow(600,0,580);
            d.setFirstColumnWidth(50);
            d.setSecondColumnWidth(200);
            d.setThirdColumnWidth(330);
            d.setLocationRelativeTo(btnCustomer);
            d.setVisible(true); 
            String rtnVal = d.getValue();
            if(!rtnVal.equals("")){
                this.SELECTED_CUSTOMER = rtnVal;
                txtCustomer.setInputText(d.getDescription());
                txtAsset.setInputText("");
                txtModel.setText("");
                tblScheduledMaintenance.deleteAll();
            }
        }
        else{
            messageBar.setMessage("You can not change the customer when you have added maintenance to the Job.","ERROR");
        }
    }

    private void btnAsset_actionPerformed(ActionEvent e) {
        if(tblWorkOrder.getRowCount()==0){
            String sql = "SELECT asset_id AS 'Machine', model_id AS 'Model' FROM asset WHERE division_id ='"+ SELECTED_CUSTOMER +"'";
            
            DataAssistantDialog d = new DataAssistantDialog(frame,"Select Customer - Data Assistant",sql,connection);
            d.setFirstColumnWidth(150);
            d.setSecondColumnWidth(150);
            d.setLocationRelativeTo(btnCustomer);
            d.setVisible(true); 
            String rtnVal = d.getValue();
            if(!rtnVal.equals("")){
                txtAsset.setInputText(rtnVal);
                txtModel.setText(d.getDescription());
                refreshPlanView();
            }
        }
        else{
            messageBar.setMessage("You can not change the asset when you have added maintenance to the Job.","ERROR");
        }
    }
    
    private void refreshPlanView(){
        try {
            String sql = "SELECT get_last_customer_visit(?),get_second_next_customer_visit(?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,txtAsset.getInputText());
            stmt.setString(2,txtAsset.getInputText());
            ResultSet rec = stmt.executeQuery();
            rec.first();
            txtFromDate.setInputText(rec.getString(1));
            txtToDate.setInputText(rec.getString(2));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        tblScheduledMaintenance.populate(
                                        txtAsset.getInputText(),
                                        txtFromDate.getInputText(),
                                        txtToDate.getInputText());
    }

    private void btnRefreshSchedule_actionPerformed(ActionEvent e) {
        tblScheduledMaintenance.populate(txtAsset.getInputText(),txtFromDate.getInputText(),txtToDate.getInputText());   
    }

    private void btnAddReqTask_actionPerformed(ActionEvent e) {
        dlgUPMTasks = new UPMTasksPickerDialog(frame,messageBar,connection);
        dlgUPMTasks.setLocationRelativeTo(frame);
        dlgUPMTasks.populate();
        dlgUPMTasks.setVisible(true); 
        UPMTask upmTask = dlgUPMTasks.getSelectedTask();
        if(upmTask!=null){
            if(ADDING_TO_WORKORDER && true){
                try{ 
                    if(!isAlreadyExisting(upmTask.upm_id)){
                        RequestedTask requestedTask = new RequestedTask(connection);
                        requestedTask.pm_work_order_id = mtxtWorkorder.getInputText();
                        requestedTask.asset_id = txtAsset.getInputText();
                        requestedTask.task_id = upmTask.upm_id;
                        
                        requestedTask.save();
                        tblRequested.addTask(upmTask);
                        messageBar.setMessage("The task was added into the Job.","OK");
                    }
                    else{
                        messageBar.setMessage("The task you seleted is already in the list.","ERROR");
                    }
                }
                catch(Exception er){
                    messageBar.setMessage(er.getMessage(),"ERROR");
                    er.printStackTrace();   
                }
            }
            else{
                messageBar.setMessage("You can not add things to the Job on this mode.","ERROR");
            }
        }
    }
    
    private boolean isAlreadyExisting(String upm_id){
        int rows = tblRequested.getRowCount();
        for(int i=0;i<rows;i++){
            if(tblRequested.getValueAt(i,0).toString().equals(upm_id)){
                return true;
            }    
        }
        return false;
    }

    private void btnPrint_actionPerformed(ActionEvent e) {
        String work_order = mtxtWorkorder.getInputText();
        HashMap hm = new HashMap();
        hm.put("PM_WORK_ORDER",work_order);
        String app_path = Utilities.getApplicationPath();
        hm.put("PATH",app_path);
        String reportPath = app_path + "/reports/documents/pm_work_order/pm_work_order.jrxml";
        try{
            JasReport jasReport = new JasReport(reportPath,hm,connection);
            frame.tabbedPane.addTab("Job - "+work_order,jasReport.getPrintView());
        }
        catch (Exception exp){
            exp.printStackTrace();
            messageBar.setMessage(exp.getMessage(),"ERROR");
        }
    }

    class ScheduledMaintenance{ 
        public String Scheduled_ID;
        public String Preventive_Maintenance_ID;
        public String Description;
        public String Modified_Date;
        public String Modified_Meter;
        public String Work_Order;
        public boolean Done;
    }

    public class ScheduledMaintenanceTable extends JTable{
        private ScheduledMaintenanceTableModel mListModel;
        
        public ScheduledMaintenanceTable(){
            mListModel = new ScheduledMaintenanceTableModel();
            this.setModel(mListModel);
            
            this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.setSelectionMode(0);
            this.getTableHeader().setReorderingAllowed(false);
            
            /**Hidden*/
            this.getColumnModel().getColumn(0).setMinWidth(0);
            this.getColumnModel().getColumn(0).setPreferredWidth(0);
            
            this.getColumnModel().getColumn(1).setPreferredWidth(30);
            this.getColumnModel().getColumn(2).setPreferredWidth(160);
            this.getColumnModel().getColumn(3).setPreferredWidth(80);
            this.getColumnModel().getColumn(4).setPreferredWidth(85);
            this.getColumnModel().getColumn(5).setPreferredWidth(60);
            this.getColumnModel().getColumn(6).setPreferredWidth(30);
            
            this.getColumnModel().getColumn(1).setCellRenderer(new LabelCellRenderer());
            this.getColumnModel().getColumn(2).setCellRenderer(new LabelCellRenderer());
            this.getColumnModel().getColumn(3).setCellRenderer(new LabelCellRenderer());
            this.getColumnModel().getColumn(4).setCellRenderer(new LabelCellRenderer());
            this.getColumnModel().getColumn(5).setCellRenderer(new LabelCellRenderer());
            this.getColumnModel().getColumn(6).setCellRenderer(new CheckBoxCellRenderer());
        }
        
        public boolean populate(String asset_id, String from_date, String to_date){
            String sql = "SELECT " + 
                            "scheduled_preventive_maintenance.Scheduled_ID, " + 
                            "scheduled_preventive_maintenance.Preventive_Maintenance_ID, " + 
                            "preventive_maintenance.Description, " + 
                            "scheduled_preventive_maintenance.Modified_Date, " + 
                            "scheduled_preventive_maintenance.Modified_Meter, " + 
                            "IF(preventive_maintenance_log.PM_Work_Order_ID IS NULL ,'-NO-',preventive_maintenance_log.PM_Work_Order_ID ) as Done , " + 
                            "IF(preventive_maintenance_log.Done_Date IS NULL ,'false','true' ) as Done " + 
                        "FROM " + 
                            "preventive_maintenance " + 
                            "Inner Join scheduled_preventive_maintenance ON preventive_maintenance.ID = scheduled_preventive_maintenance.Preventive_Maintenance_ID " + 
                            "Left Outer Join preventive_maintenance_log ON scheduled_preventive_maintenance.Scheduled_ID = preventive_maintenance_log.Scheduled_ID " + 
                        "WHERE " + 
                            "scheduled_preventive_maintenance.Asset_ID =  ? AND " + 
                            "scheduled_preventive_maintenance.Modified_Date >= ? AND scheduled_preventive_maintenance.Modified_Date < ? " +
                        "ORDER BY scheduled_preventive_maintenance.Modified_Date";

            try{
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1,asset_id);
                stmt.setString(2,from_date);
                stmt.setString(3,to_date);
                ResultSet rec = stmt.executeQuery();
                this.deleteAll();
                while(rec.next()){
                    ScheduledMaintenance obj = new ScheduledMaintenance();
                    obj.Scheduled_ID = rec.getString(1);
                    obj.Preventive_Maintenance_ID = rec.getString(2);
                    obj.Description = rec.getString(3);
                    obj.Modified_Date = rec.getString(4);
                    obj.Modified_Meter = rec.getString(5);
                    obj.Work_Order = rec.getString(6);
                    obj.Done = rec.getBoolean(7);
                    this.addRow(obj);
                }
                if(this.getRowCount()==0){
                    return false;
                }
                else{
                    return true;
                }
            } 
            catch (Exception ex){
                ex.printStackTrace();
                return false;
            } 
        }
        
        public void addRow(ScheduledMaintenance scheduledMaintenance){
            mListModel.addRow(scheduledMaintenance);
            this.tableChanged(new TableModelEvent(mListModel)); 
        }
        
        public void deleteAll(){
            mListModel.deleteAll();
            this.tableChanged(new TableModelEvent(mListModel));        
        }
        
        private class ScheduledMaintenanceTableModel extends AbstractTableModel{
            private String [] colNames = new String[7];
            
            private Vector<ScheduledMaintenance> maintenaces = new Vector();
            
            public ScheduledMaintenanceTableModel(){
                colNames[0]= "Scheduled_ID";
                colNames[1]= "PM ID";
                colNames[2]= "Description";
                colNames[3]= "Scheduled Date";
                colNames[4]= "Scheduled Meter";
                colNames[5]= "Job";
                colNames[6]= "Done";
            }
            
            public Class getColumnClass(int c){
                return this.getValueAt(0,c).getClass();
            }
            
            public String getColumnName(int c){ 
                return colNames[c];  
            }
            
            public int getColumnCount(){  
                return colNames.length;
            }
            
            public int getRowCount(){  
                return maintenaces.size();
            }
            
            public Object getValueAt(int r, int c){
                ScheduledMaintenance obj = maintenaces.get(r);
                switch(c){
                    case 0: return obj.Scheduled_ID;
                    case 1: return obj.Preventive_Maintenance_ID;
                    case 2: return obj.Description;
                    case 3: return obj.Modified_Date;
                    case 4: return obj.Modified_Meter;
                    case 5: return obj.Work_Order;
                    case 6: return obj.Done;
                    default: return null;
                }
            }
            
            public void setValueAt(Object value,int row,int col){
                
            }
            
            public boolean isCellEditable(int r,int c){
               return false;
            }
            
            public void addRow(ScheduledMaintenance scheduledMaintenance){
                maintenaces.add(scheduledMaintenance);    
            }
        
            public void deleteAll(){
                maintenaces.removeAllElements();
            }
        }
        
            private class LabelCellRenderer extends JLabel implements TableCellRenderer{
               public LabelCellRenderer(){
                   setOpaque(true);
               }

               public Component getTableCellRendererComponent(JTable table, 
                                                              Object value, 
                                                              boolean isSelected, 
                                                              boolean hasFocus, 
                                                              int row, 
                                                              int column) {
                   this.setText(value.toString());
                   
                    boolean done = Boolean.parseBoolean(table.getValueAt(row,6).toString());
                    boolean workorder = !table.getValueAt(row,5).toString().equals("-NO-");
                                      
                    if(isSelected){
                        this.setBackground(table.getSelectionBackground());
                        this.setForeground(table.getSelectionForeground());
                    }
                    else if(done){
                        this.setBackground(Color.GRAY);
                        this.setForeground(table.getForeground());
                    }
                    else if(workorder){
                        this.setBackground(Color.LIGHT_GRAY);
                        this.setForeground(table.getForeground());
                    }
                    else{
                        this.setBackground(Color.WHITE);
                        this.setForeground(table.getForeground());
                    }
                   return this;
               }
               
           }
       
           private class CheckBoxCellRenderer extends JCheckBox implements TableCellRenderer{
              public CheckBoxCellRenderer(){
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
                  
                  boolean done = Boolean.parseBoolean(table.getValueAt(row,6).toString());
                  boolean workorder = !table.getValueAt(row,5).toString().equals("-NO-");
                                    
                  if(isSelected){
                      this.setBackground(table.getSelectionBackground());
                      this.setForeground(table.getSelectionForeground());
                  }
                  else if(done){
                      this.setBackground(Color.GRAY);
                      this.setForeground(table.getForeground());
                  }
                  else if(workorder){
                      this.setBackground(Color.LIGHT_GRAY);
                      this.setForeground(table.getForeground());
                  }
                  else{
                      this.setBackground(Color.WHITE);
                      this.setForeground(table.getForeground());
                  }
                  return this;
              }
          }
       }
    
    private class WorkOrderTask{
        public String log_id;
        public String scheduled_id;
        public String pm_id;
        public String description;
        public String planed_date;
        public String planed_meter;     
    }

    private class WorkOrderTable extends JTable {
        private WorkOrderTableModel myModel = null;
        private Connection WorkOrderTableConnection;
             
        public WorkOrderTable(Connection con){
            WorkOrderTableConnection = con;
            myModel = new WorkOrderTableModel();
            this.setModel(myModel);
            
            this.setSelectionMode(0);
            this.getTableHeader().setReorderingAllowed(false);
            this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            
            this.getColumnModel().getColumn(2).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(3).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(4).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(5).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(6).setCellRenderer(new ButtonCellRenderer());
            
            this.getColumnModel().getColumn(0).setMinWidth(0);
            this.getColumnModel().getColumn(0).setPreferredWidth(0);
            this.getColumnModel().getColumn(1).setMinWidth(0);
            this.getColumnModel().getColumn(1).setPreferredWidth(0);
            
            this.getColumnModel().getColumn(2).setPreferredWidth(60);
            this.getColumnModel().getColumn(3).setPreferredWidth(560);
            this.getColumnModel().getColumn(4).setPreferredWidth(80);
            this.getColumnModel().getColumn(5).setPreferredWidth(80);
            this.getColumnModel().getColumn(6).setPreferredWidth(80);
        }
         
        public void addRow(WorkOrderTask task){
            myModel.addRow(task);
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
            Vector<WorkOrderTask> tasks = new Vector();
            private String [] colNames = {
                             "Log_ID",                      //0
                             "Scheduled_ID",                //1
                             "PM ID",                       //2
                             "Description",                 //3
                             "Planed Date",                 //4
                             "Planed Meter",                //5     
                             "Part Allocation"              //6
                             };
        
            public WorkOrderTableModel() {
               
            }
            
            public String getColumnName(int c){ 
                return colNames[c];  
            }
            
            public int getColumnCount(){  
                return colNames.length;
            }  
            
            public int getRowCount(){  
                return tasks.size();
            }
             
            public Object getValueAt(int r, int c){
                WorkOrderTask task = tasks.get(r);
                switch(c){
                    case 0:return task.log_id;
                    case 1:return task.scheduled_id;
                    case 2:return task.pm_id;
                    case 3:return task.description;
                    case 4:return task.planed_date;
                    case 5:return task.planed_meter;
                    default :return "";
                }
            }
                
            public void setValueAt(Object value,int row,int col){
                
            }
                 
            public boolean isCellEditable(int r,int c){
                return false;
            }
       
            public void addRow(WorkOrderTask task){
                tasks.add(task);
            }          
        
            public void deleteRow(int i){
                tasks.remove(i);
            }
             
            public void deleteAll(){
                tasks.removeAllElements();
            }
        }
    }
    
    private class RequestedTable extends JTable {
            private RequestedTableModel myModel = null;
                 
            public RequestedTable(){
                    myModel = new RequestedTableModel();
                    this.setModel(myModel);
                    
                    this.setSelectionMode(0);
                    this.getTableHeader().setReorderingAllowed(false);
                    this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                    
                    this.getColumnModel().getColumn(0).setCellRenderer(new LockedLabelCellRenderer());
                    this.getColumnModel().getColumn(1).setCellRenderer(new LockedLabelCellRenderer());
                    this.getColumnModel().getColumn(2).setCellRenderer(new LockedLabelCellRenderer());
                    this.getColumnModel().getColumn(3).setCellRenderer(new ButtonCellRenderer());
                    
                    this.getColumnModel().getColumn(0).setPreferredWidth(70);
                    this.getColumnModel().getColumn(1).setPreferredWidth(600);
                    this.getColumnModel().getColumn(2).setPreferredWidth(100);
                    this.getColumnModel().getColumn(3).setPreferredWidth(100);
            }
        
        
            public void addTask(UPMTask task){
                    myModel.addTask(task);
                    this.tableChanged(new TableModelEvent(myModel)); 
            }

            public void deleteRow(int i){
                myModel.deleteRow(i);
                this.tableChanged(new TableModelEvent(myModel));        
            }

            public void deleteAll(){
                    myModel.deleteAll();
                    this.tableChanged(new TableModelEvent(myModel));        
            }
     
            private class RequestedTableModel extends AbstractTableModel{
                private String [] colNames = {
                                                    "Task ID",             //0
                                                    "Description",         //1
                                                    "Category",            //2  
                                                    "Part Allocation"      //3
                                         };
                                         
                private Vector<UPMTask> tasks = new Vector();
                
                public RequestedTableModel() {
                    
                }
                        
                public String getColumnName(int c){ 
                    return colNames[c];  
                }
                        
                public Class getColumnClass(int c){
                    return this.getValueAt(0,c).getClass(); 
                }
                
                public int getColumnCount(){  
                    return colNames.length;
                }  
                
                public int getRowCount(){  
                    return tasks.size();
                }
                     
                public Object getValueAt(int r, int c){
                    UPMTask task = tasks.get(r);
                    switch(c){
                        case 0:return task.upm_id;
                        case 1:return task.description;
                        case 2:return task.category;
                        default : return "";
                    }
                }
                    
                public void setValueAt(Object value,int row,int col){
                    
                }
                     
                public boolean isCellEditable(int r,int c){
                    return false;
                }
                     
                public void addTask(UPMTask task){
                    tasks.add(task);
                }
                   
                public void deleteRow(int i){
                    tasks.remove(i);
                }
                 
                public void deleteAll(){
                    tasks.removeAllElements();
                }
            }
    }
}
