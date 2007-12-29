package com.matrix.focus.mdi;

import com.matrix.components.TitleBar;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.Authorizer;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.ImageLibrary;
import com.matrix.focus.util.MPanel;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

public class GUIPendingWorkOrders extends MPanel{
    private TitleBar titleBar = new TitleBar();
    private JPanel jPanel1 = new JPanel();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private PendingWorkOrdersTable tblJobs = new PendingWorkOrdersTable();
    private Connection connection;
    private JPanel jPanel2 = new JPanel();
    private JScrollPane jScrollPane2 = new JScrollPane();
    private PendingWorkorderRecordsTable tblRemarks = new PendingWorkorderRecordsTable();
    private JButton btnAdd = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));
    private JButton btnRemove = new JButton(new ImageIcon(ImageLibrary.BUTTON_REMOVE));
    private MessageBar messagerBar;
    private MDI frame;
    private PMWorkOrderDialog dlg;

    public GUIPendingWorkOrders(DBConnectionPool pool, final MDI frame, MessageBar msgBar) {
        connection = pool.getConnection();
        messagerBar = msgBar;
        this.frame = frame;
        try {
            titleBar.setTitle("Pending Jobs");
            titleBar.setDescription("The facility to view and manage pending jobs.");
            titleBar.setImage(ImageLibrary.TITLE_PREVENTIVE_MAINTENANCE_WORKORDER);
            
            jbInit();
            
            tblJobs.populate();
            
            tblJobs.addMouseListener(
                new MouseAdapter(){
                    public void mouseClicked(MouseEvent me){
                        int row = tblJobs.getSelectedRow();
                        if(row!=-1){
                            String workorder_id = tblJobs.getValueAt(row,0).toString();
                            if(me.getClickCount()==1){
                                tblRemarks.populate(workorder_id);
                            }
                            else if(me.getClickCount()==2){
                                dlg = new PMWorkOrderDialog(frame,workorder_id);
                                dlg.setVisible(true);
                                int option = dlg.getOption();
                                if(option==dlg.EDIT){
                                    /**Load PM Job Creation*/
                                    if(Authorizer.isCapable(MDI.USERNAME,Authorizer.JOB_CREATION,connection)){
                                        frame.menuItemGUIPreventiveMaintenanceWorkOrder.doClick();
                                        frame.guiPreventiveMaintenanceWorkOrder.setWorkOrder(workorder_id);
                                        frame.guiPreventiveMaintenanceWorkOrder.repaint();
                                    }
                                }
                                else if(option==dlg.LOG){
                                    /**Load PM Log*/
                                     if(Authorizer.isCapable(MDI.USERNAME,Authorizer.JOB_COMPLETION,connection)){
                                         frame.menuItemGUIPreventiveMaintenanceLog.doClick();
                                         frame.guiPreventiveMaintenanceLog.search(workorder_id);
                                         frame.guiPreventiveMaintenanceLog.repaint();
                                     }
                                }
                            }
                        }
                    }
                }
            );
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(null);
        this.setSize(new Dimension(951, 522));
        titleBar.setBounds(new Rectangle(10, 10, 940, 70));
        jPanel1.setBounds(new Rectangle(10, 85, 940, 230));
        jPanel1.setLayout(null);
        jPanel1.setBorder(BorderFactory.createTitledBorder("Pending Jobs"));
        jScrollPane1.setBounds(new Rectangle(10, 20, 920, 200));
        jPanel2.setBounds(new Rectangle(10, 320, 940, 165));
        jPanel2.setBorder(BorderFactory.createTitledBorder("Pending Job Remarks"));
        jPanel2.setLayout(null);
        jScrollPane2.setBounds(new Rectangle(10, 15, 810, 140));
        btnAdd.setBounds(new Rectangle(830, 15, 100, 25));
        btnAdd.setText("Add");
        btnAdd.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnAdd_actionPerformed(e);
                    }
                });
        btnRemove.setBounds(new Rectangle(830, 45, 100, 25));
        btnRemove.setText("Remove");
        btnRemove.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnRemove_actionPerformed(e);
                    }
                });
        jScrollPane1.getViewport().add(tblJobs, null);
        jPanel1.add(jScrollPane1, null);
        jScrollPane2.getViewport().add(tblRemarks, null);
        jPanel2.add(jScrollPane2, null);
        jPanel2.add(btnAdd, null);
        jPanel2.add(btnRemove, null);
        this.add(jPanel2, null);
        this.add(jPanel1, null);
        this.add(titleBar, null);
    }

    private void btnAdd_actionPerformed(ActionEvent e) {
        int row = tblJobs.getSelectedRow();
        if(row!=-1){
            addCommentForWorkOrder(tblJobs.getValueAt(row,0).toString());
        }
        else{
            messagerBar.setMessage("Select a job first.","ERROR");
        }
    }
    
    private void addCommentForWorkOrder(String workorder){
        String comment = messagerBar.showInputDialog(frame,"Comment","Contact Remarks");  
        if(!comment.isEmpty()){
            try{
                Record record = new Record(workorder,comment,MDI.USERNAME,connection);
                tblRemarks.addRecord(record);
            } 
            catch (Exception ex){
                messagerBar.setMessage(ex.getMessage(),"ERROR");    
            } 
        }    
    }

    private void btnRemove_actionPerformed(ActionEvent e) {
        int row = tblRemarks.getSelectedRow();
        if(row!=-1){
            try{
                if(MessageBar.YES_OPTION==messagerBar.showConfirmDialog(frame,"Are you sure you want to delete this comment?","Pending Jobs")){
                    if(tblRemarks.getValueAt(row,3).toString().equals(MDI.USERNAME)){
                        if(!tblRemarks.getValueAt(row,0).toString().equals("new")){
                            Record.deleteRecord(tblRemarks.getValueAt(row,0).toString(),connection);
                        }
                        tblRemarks.removeRecord(tblRemarks.getValueAt(row,0).toString());
                    }
                    else{
                        throw new Exception("You can not delete this comment.");
                    }
                }    
            } 
            catch(Exception ex){
                messagerBar.setMessage(ex.getMessage(),"ERROR"); 
                ex.printStackTrace();
            } 
            
        }
        else{
            messagerBar.setMessage("Please select a comment first.","ERROR");
        }
    }

    class WorkOrder{
        public String workorder;                //0
        public String type;                     //1
        public String category;                 //2
        public String customer;                 //3
        public String machine;                  //4
        public String model;                    //5
        public String promised_date;           //6 
    }
    
    class PendingWorkOrdersTable extends JTable{
        private PendingWorkOrdersTableModel model;
        
        public PendingWorkOrdersTable(){
                model = new PendingWorkOrdersTableModel();
                this.setModel(model);
                this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                this.getTableHeader().setReorderingAllowed(false);
                this.setSelectionMode(0);
                this.setAutoCreateRowSorter(true);
                
                this.getColumnModel().getColumn(0).setPreferredWidth(100);
                this.getColumnModel().getColumn(1).setPreferredWidth(100);
                this.getColumnModel().getColumn(2).setPreferredWidth(100);
                this.getColumnModel().getColumn(3).setPreferredWidth(300);
                this.getColumnModel().getColumn(4).setPreferredWidth(100);
                this.getColumnModel().getColumn(5).setPreferredWidth(100);
                this.getColumnModel().getColumn(6).setPreferredWidth(100);
        }
        
        public void populate(){
            String sql = "SELECT " + 
                    "  PM_Work_Order_ID, " + 
                    "  WO.type, " +
                    "  WO.category, " + 
                    "  Customer, " +
                    "  IFNULL(WO.Asset_ID,' ') AS Machine, " +
                    "  IFNULL(A.model_id, ' ') AS Model, " +
                    "  Promised_Date " + 
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
                    "  A.asset_id = WO.asset_id ORDER BY PM_Work_Order_ID";
                    
            try{
                ResultSet rec = connection.createStatement().executeQuery(sql);
                while(rec.next()){
                    WorkOrder wo = new WorkOrder();
                    wo.workorder = rec.getString("pm_work_order_id");
                    wo.type = rec.getString("type");
                    wo.category = rec.getString("category");
                    wo.customer = rec.getString("Customer");
                    wo.machine = rec.getString("Machine");
                    wo.model = rec.getString("Model");
                    wo.promised_date = rec.getString("Promised_Date");
                    tblJobs.addWorkOrder(wo);
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            
        }
                
        public void addWorkOrder(WorkOrder wo){
                model.addWorkOrder(wo);
                this.tableChanged(new TableModelEvent(model));
        }
                
        public void removeAll(){
                model.removeAll();
                this.tableChanged(new TableModelEvent(model));
        }
                
        private class PendingWorkOrdersTableModel extends AbstractTableModel{
            private String [] colNames;
            
            private Vector<WorkOrder> workorders = new Vector();
        
            public PendingWorkOrdersTableModel(){
                this.colNames = new String[]{
                                    "Job No",                //0
                                    "Type",                  //1
                                    "Category",              //2
                                    "Customer",              //3
                                    "Machine",               //4
                                    "Model",                 //5
                                    "Promised Date"          //6
                                    };
            }
                
            //public Class getColumnClass(int c){
            //    return this.getValueAt(0,c).getClass();   
            //}
            
            public String getColumnName(int c){ 
                return colNames[c];  
            }
        
            public int getColumnCount(){  
                return colNames.length;
            }    
            
            public int getRowCount(){
                    return workorders.size();  
            }
        
            public Object getValueAt(int r, int c){
                    WorkOrder workorder = workorders.get(r);
                    switch(c){
                        case 0:return workorder.workorder;
                        case 1:return workorder.type;
                        case 2:return workorder.category;
                        case 3:return workorder.customer;
                        case 4:return workorder.machine;
                        case 5:return workorder.model;
                        case 6:return workorder.promised_date;
                        default: return "";
                    }
            }
        
            public void setValueAt(Object value,int row,int col){
                    //nothing
            }
        
            public boolean isCellEditable(int r,int c){
                return false;
            }
                
            public void addWorkOrder(WorkOrder wo){
                    workorders.add(wo);
            }
            
            public void removeAll(){
                    workorders.removeAllElements(); 
            }
        }            
    } 
    
    static class Record{
        public String id;
        public String workorder;
        public String timestamp;
        public String comment;
        public String username;
        
        public Record(){}
        
        public Record(String workorder, String comment, String username,Connection con) throws Exception{
            String sql = "INSERT INTO pending_workorder_record " +
                            "(WO_No, Created_Time, Comment, username) " +
                         "VALUES(?,NOW(),?,?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1,workorder);
            stmt.setString(2,comment);
            stmt.setString(3,username);
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("Comment not saved.");
            }
            
            this.id = "new";
            this.timestamp = "now";
            this.workorder = workorder;
            this.comment = comment;
            this.username = username;
            
        }
        
        public static void deleteRecord(String id, Connection con) throws Exception{
            String sql = "DELETE FROM pending_workorder_record WHERE id = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1,id);
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("Comment not deleted.");
            }
        }
    }
        
    class PendingWorkorderRecordsTable extends JTable{
        private PendingWorkorderRecordsTableModel model;
        
        public PendingWorkorderRecordsTable(){
                model = new PendingWorkorderRecordsTableModel();
                this.setModel(model);
                this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                this.getTableHeader().setReorderingAllowed(false);
                this.setSelectionMode(0);
                
                this.getColumnModel().getColumn(0).setMinWidth(0);
                this.getColumnModel().getColumn(0).setPreferredWidth(0);
                
                this.getColumnModel().getColumn(1).setPreferredWidth(100);
                this.getColumnModel().getColumn(2).setPreferredWidth(600);
                this.getColumnModel().getColumn(3).setPreferredWidth(100);
                
        }
        
        public void populate(String wo_no){
            try{
                String sql = "SELECT id, Created_Time, Comment, username FROM pending_workorder_record WHERE WO_No = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1,wo_no);
                
                ResultSet rec = stmt.executeQuery();
                Record record = null;
                removeAll();
                while(rec.next()){
                    record = new Record();
                    record.id = rec.getString("id");
                    record.timestamp = rec.getString("Created_Time");
                    record.comment = rec.getString("Comment");
                    record.username = rec.getString("username");
                    addRecord(record);
                }
            } 
            catch(Exception ex){
                ex.printStackTrace();
            } 
            
        }
                
        public void addRecord(Record record){
                model.addRecord(record);
                this.tableChanged(new TableModelEvent(model));
        }
        
        public void removeRecord(String id){
                model.removeRecord(id);
                this.tableChanged(new TableModelEvent(model));
        }
                
        public void removeAll(){
                model.removeAll();
                this.tableChanged(new TableModelEvent(model));
        }
        
        private class PendingWorkorderRecordsTableModel extends AbstractTableModel{
            private String [] colNames;
            
            private Vector<Record> records = new Vector();
        
            public PendingWorkorderRecordsTableModel(){
                this.colNames = new String[]{
                                    "ID",                   //0
                                    "Timestamp",            //1
                                    "Comment",              //2
                                    "User"                  //3
                                    };
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
                    return records.size();  
            }
        
            public Object getValueAt(int r, int c){
                    Record record = records.get(r);
                    switch(c){
                        case 0:return record.id;
                        case 1:return record.timestamp;
                        case 2:return record.comment;
                        case 3:return record.username;
                        default: return "";
                    }
            }
        
            public void setValueAt(Object value,int row,int col){
                    //nothing
            }
        
            public boolean isCellEditable(int r,int c){
                return false;
            }
                
            public void addRecord(Record record){
                    records.add(record);
            }
            
            public void removeRecord(String id){
                for(int i=0;i<records.size();i++){
                    Record r = records.get(i);
                    if(r.id.equals(id)){
                        records.remove(r);
                    }
                }    
            }
            
            public void removeAll(){
                    records.removeAllElements(); 
            }
        }
    }
}
