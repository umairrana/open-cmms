package com.matrix.focus.workorder;

import com.matrix.components.MTextbox;
import com.matrix.components.TitleBar;
import com.matrix.focus.log.GUIPreventiveMaintenanceLog;
import com.matrix.focus.util.MPanel;
import com.matrix.focus.util.TaskRemarksDialog;
import com.matrix.focus.mdi.MDI;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.ImageLibrary;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

public class GUIJobConfirmation extends MPanel{

    private TitleBar titleBar = new TitleBar();
    private JPanel jPanel1 = new JPanel();
    private MTextbox txtCustomerName = new MTextbox();
    private MTextbox txtContactPerson = new MTextbox();
    private MTextbox txtDesignation = new MTextbox();
    private JLabel jLabel1 = new JLabel();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JTextArea txtAddress = new JTextArea();
    private MTextbox txtTelephone = new MTextbox();
    private MTextbox txtFax = new MTextbox();
    private MTextbox txtLastVisit = new MTextbox();
    private JPanel jPanel2 = new JPanel();
    private JScrollPane jScrollPane2 = new JScrollPane();
    private MaintenaceTable tblMaintenace = new MaintenaceTable();
    private Connection connection;
    private MDI frame;
    private MessageBar messagerBar;
    private JTabbedPane jTabbedPane1 = new JTabbedPane();
    private JPanel jPanel3 = new JPanel();
    private JPanel jPanel4 = new JPanel();
    private JScrollPane jScrollPane3 = new JScrollPane();
    private JButton btnAdd = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));
    private JButton btnRemove = new JButton(new ImageIcon(ImageLibrary.BUTTON_REMOVE));
    private DivisionContactRecordsTable tblContactRecords = new DivisionContactRecordsTable();
    private TaskRemarksDialog dlgRemarks;
    private String ASSET_ID;
    private JPanel jPanel5 = new JPanel();
    private MTextbox txtAsset = new MTextbox();
    private MTextbox txtModel = new MTextbox();
    private JScrollPane jScrollPane4 = new JScrollPane();
    private LastVisitInspectionsTable tblInspections = new LastVisitInspectionsTable();
    private JButton btnR2M = new JButton(new ImageIcon(ImageLibrary.BUTTON_EXECUTE));
    private JPanel jPanel6 = new JPanel();
    private JScrollPane jScrollPane5 = new JScrollPane();
    private ContactPersonsTable tblPerons = new ContactPersonsTable();
    private JPanel jPanel7 = new JPanel();
    private JScrollPane jScrollPane6 = new JScrollPane();
    //private JTable tblJobCompletion = new JTable();
    private JobCompletionRemarksTable tblJobCompletion = new JobCompletionRemarksTable();

    public GUIJobConfirmation(DBConnectionPool pool, final MDI frame, MessageBar msgBar) {
        this.connection = pool.getConnection();
        this.frame = frame;
        this.messagerBar = msgBar;
        try{
            titleBar.setTitle("Job Confirmation");
            titleBar.setDescription("The facility to contact customers for on coming maintenances.");
            titleBar.setImage(ImageLibrary.TITLE_CUSTOMER_CONTACT);
            
            jbInit();
            
            tblContactRecords.addMouseListener(
                new MouseAdapter(){
                    public void mouseClicked(MouseEvent me){
                        int row = tblContactRecords.getSelectedRow();
                        if(row!=-1){
                            if(tblContactRecords.getSelectedColumn()==2){    
                                dlgRemarks = new TaskRemarksDialog(
                                                                frame,
                                                                "Comment",
                                                                true,
                                                                false);
                                dlgRemarks.setText(tblContactRecords.getValueAt(row,2).toString());
                                dlgRemarks.setLocationRelativeTo(frame);
                                dlgRemarks.setVisible(true);
                                String rtnValue = dlgRemarks.getText();
                                if(!rtnValue.equals("null")){
                                    tblContactRecords.setValueAt(rtnValue,row,2);
                                    tblContactRecords.repaint();
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
    
    public void setAsset(String asset_id){
        this.ASSET_ID = asset_id;
        String customer_id = "";
        try{
            PreparedStatement stmt = connection.prepareStatement(getDataQuery());
            stmt.setString(1,ASSET_ID);
            
            ResultSet rec = stmt.executeQuery();
            tblMaintenace.removeAll();
            while(rec.next()){
                if(rec.getRow()==1){
                    customer_id = rec.getString("division.Division_ID");
                    txtCustomerName.setInputText(rec.getString("division.Name"));
                    txtContactPerson.setInputText(rec.getString("division.Contact_Person"));
                    txtDesignation.setInputText(rec.getString("division.Designation"));
                    txtAddress.setText(rec.getString("division.Address"));
                    txtTelephone.setInputText(rec.getString("division.Telephone_No"));
                    txtFax.setInputText(rec.getString("division.Fax_No"));
                    txtAsset.setInputText(rec.getString("asset.Asset_ID"));
                    txtModel.setInputText(rec.getString("asset.Model_ID"));
                    txtLastVisit.setInputText(rec.getString("last_customer_visit"));
                }
            
                Maintenace maintenace = new Maintenace();
                    maintenace.pm_id = rec.getString("Preventive_Maintenance_ID");
                    maintenace.description = rec.getString("Preventive_Maintenance");
                    maintenace.scheduled_date = rec.getString("Planned_Date");
                    maintenace.scheduled_meter = rec.getString("Planned_Meter");
                    maintenace.jobbed = rec.getBoolean("Jobbed");
                    maintenace.workorder = rec.getString("JobNo");
                tblMaintenace.addMaintenace(maintenace);
            }
            
            tblContactRecords.populate(ASSET_ID,txtLastVisit.getInputText());
            tblInspections.populate();
            tblJobCompletion.populate(ASSET_ID);
            tblPerons.populate(customer_id);
            btnAdd.setEnabled(true);
            btnRemove.setEnabled(true);
            btnR2M.setEnabled(true);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    private String getDataQuery(){
        return "SELECT " +
                    "division.Division_ID," +
                    "division.Name," +
                    "division.Contact_Person," +
                    "division.Designation," +
                    "division.Address," +
                    "division.Telephone_No," +
                    "division.Fax_No," +
                    "IF(get_last_customer_visit(asset.Asset_ID)='0000-00-00','0000-00-00',get_last_customer_visit(asset.Asset_ID)) as last_customer_visit," +
                    "asset.Asset_ID," +
                    "asset.Model_ID," +
                    
                    "scheduled_preventive_maintenance.Preventive_Maintenance_ID, " +
                    "preventive_maintenance.Description AS Preventive_Maintenance," +
                    "scheduled_preventive_maintenance.Modified_Date AS Planned_Date," +
                    "scheduled_preventive_maintenance.Modified_Meter AS Planned_Meter," +
                    "if(preventive_maintenance_log.PM_Work_Order_ID,'true','false') as Jobbed," +
                    "get_workorder_number_for_pending_customers(scheduled_preventive_maintenance.Scheduled_ID) as JobNo " + 
            "FROM " +
                    "scheduled_preventive_maintenance " +
                    "Left Outer Join preventive_maintenance_log ON scheduled_preventive_maintenance.Scheduled_ID = preventive_maintenance_log.Scheduled_ID " +
                    "Inner Join preventive_maintenance ON scheduled_preventive_maintenance.Preventive_Maintenance_ID = preventive_maintenance.ID " +
                    "Inner Join asset ON asset.Asset_ID = scheduled_preventive_maintenance.Asset_ID " +
                    "Inner Join division ON asset.Comp_ID = division.Comp_ID AND asset.Dept_ID = division.Dept_ID AND asset.Division_ID = division.Division_ID " +

            "WHERE " +
                    "(scheduled_preventive_maintenance.Modified_Date >=  get_last_customer_visit(asset.Asset_ID) AND scheduled_preventive_maintenance.Modified_Date < get_second_next_customer_visit(asset.Asset_ID)) " +
                    "AND " +
                    "asset.Asset_ID =? " +
                    "ORDER BY Planned_Date ASC";
    }
    
    private void btnR2M_actionPerformed(ActionEvent e) {
        frame.menuItemGUIPlanUpdater.doClick();
        //frame.guiPlanUpdater.setAsset(txtAsset.getInputText());
    }
    
     private void btnAdd_actionPerformed(ActionEvent e) {
        try{
            String comment = messagerBar.showInputDialog(frame,"Comment","Contact Remarks");  
            if(!comment.isEmpty()){
                try{
                    Record record = new Record(ASSET_ID,comment,MDI.USERNAME,connection);
                    tblContactRecords.addRecord(record);
                } 
                catch (Exception ex){
                    messagerBar.setMessage(ex.getMessage(),"ERROR");    
                } 
            }
        } 
        catch (Exception ex){
            //ex.printStackTrace();     
        } 
     }

     private void btnRemove_actionPerformed(ActionEvent e) {
        int row = tblContactRecords.getSelectedRow();
        if(row!=-1){
            try{
                if(MessageBar.YES_OPTION==messagerBar.showConfirmDialog(frame,"Are you sure you want to delete this comment?","Contact Remarks")){
                    if(tblContactRecords.getValueAt(row,3).toString().equals(MDI.USERNAME)){
                        if(!tblContactRecords.getValueAt(row,0).toString().equals("new")){
                            Record.deleteRecord(tblContactRecords.getValueAt(row,0).toString(),connection);
                        }
                        tblContactRecords.removeRecord(tblContactRecords.getValueAt(row,0).toString());
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

    class Maintenace{
        public String pm_id;                //0
        public String description;          //1
        public String scheduled_date;       //2
        public String scheduled_meter;      //3
        public boolean jobbed;              //4
        public String workorder;            //5
         
    }
    
    class MaintenaceTable extends JTable{
        private MaintenaceTableModel model;
        
        public MaintenaceTable(){
                model = new MaintenaceTableModel();
                this.setModel(model);
                this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                this.getTableHeader().setReorderingAllowed(false);
                this.setSelectionMode(0);
                
                this.getColumnModel().getColumn(0).setPreferredWidth(100);
                this.getColumnModel().getColumn(1).setPreferredWidth(500);
                this.getColumnModel().getColumn(2).setPreferredWidth(100);
                this.getColumnModel().getColumn(3).setPreferredWidth(100);
                this.getColumnModel().getColumn(4).setPreferredWidth(55);
                this.getColumnModel().getColumn(5).setPreferredWidth(100);
                
                this.getColumnModel().getColumn(0).setCellRenderer(new RegularInspectionCellRenderer());
                this.getColumnModel().getColumn(1).setCellRenderer(new RegularInspectionCellRenderer());
                this.getColumnModel().getColumn(2).setCellRenderer(new RegularInspectionCellRenderer());
                this.getColumnModel().getColumn(3).setCellRenderer(new RegularInspectionCellRenderer());
                this.getColumnModel().getColumn(4).setCellRenderer(new RegularInspectionCheckboxCellRenderer());
                this.getColumnModel().getColumn(5).setCellRenderer(new RegularInspectionCellRenderer());
        }
                
        public void addMaintenace(Maintenace maintenace){
                model.addMaintenace(maintenace);
                this.tableChanged(new TableModelEvent(model));
        }
                
        public void removeAll(){
                model.removeAll();
                this.tableChanged(new TableModelEvent(model));
        }
                
        private class MaintenaceTableModel extends AbstractTableModel{
            private String [] colNames;
            
            private Vector<Maintenace> maintenaces = new Vector();
        
            public MaintenaceTableModel(){
                this.colNames = new String[]{
                                    "PM",                           //0
                                    "Description",                  //1
                                    "Scheduled Date",               //2
                                    "Scheduled Meter",              //3
                                    "Jobbed",                       //4
                                    "Job"                    //5
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
                    return maintenaces.size();  
            }
        
            public Object getValueAt(int r, int c){
                    Maintenace maintenace = maintenaces.get(r);
                    switch(c){
                        case 0:return maintenace.pm_id;
                        case 1:return maintenace.description;
                        case 2:return maintenace.scheduled_date;
                        case 3:return maintenace.scheduled_meter;
                        case 4:return maintenace.jobbed;
                        case 5:return maintenace.workorder;
                        default: return "";
                    }
            }
        
            public void setValueAt(Object value,int row,int col){
                    //nothing
            }
        
            public boolean isCellEditable(int r,int c){
                return false;
            }
                
            public void addMaintenace(Maintenace maintenace){
                    maintenaces.add(maintenace);
            }
            
            public void removeAll(){
                    maintenaces.removeAllElements(); 
            }
        }
        
        private class RegularInspectionCellRenderer extends JLabel implements TableCellRenderer{
            private final String REGULAR_INSPECTION_ID = "PM-0";
            private final Color REGULAR_INSPECTION_COLOR = Color.LIGHT_GRAY;
            
            public RegularInspectionCellRenderer(){
                setOpaque(true);
            }
            
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
                this.setText(value.toString());
                if(isSelected){
                    this.setBackground(table.getSelectionBackground());
                    this.setForeground(table.getSelectionForeground());
                }
                else{
                    this.setBackground((REGULAR_INSPECTION_ID.equals(table.getValueAt(row,0)))?REGULAR_INSPECTION_COLOR:table.getBackground());
                    this.setForeground(table.getForeground());
                }
                return this;
            }
        }
        
        private class RegularInspectionCheckboxCellRenderer extends JCheckBox implements TableCellRenderer{
                private final String REGULAR_INSPECTION_ID = "PM-0";
                private final Color REGULAR_INSPECTION_COLOR = Color.LIGHT_GRAY;
                
                public RegularInspectionCheckboxCellRenderer(){
                    setOpaque(true);
                    setHorizontalAlignment(JCheckBox.CENTER);
                }
                
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
                    this.setSelected(Boolean.parseBoolean(value.toString()));
                    if(isSelected){
                        this.setBackground(table.getSelectionBackground());
                        this.setForeground(table.getSelectionForeground());
                    }
                    else{
                        this.setBackground((REGULAR_INSPECTION_ID.equals(table.getValueAt(row,0)))?REGULAR_INSPECTION_COLOR:table.getBackground());
                        this.setForeground(table.getForeground());
                    }
                    return this;
                }
            }
            
    } 

    static class Record{
        public String id;
        public String asset_id;
        public String timestamp;
        public String comment;
        public String username;
        
        public Record(){}
        
        public Record(String asset_id, String comment, String username,Connection con) throws Exception{
            String sql = "INSERT INTO asset_contact_records " +
                            "(Asset_ID, Created_Time, Comment, username) " +
                         "VALUES(?,NOW(),?,?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1,asset_id);
            stmt.setString(2,comment);
            stmt.setString(3,username);
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("Comment not saved.");
            }
            
            this.id = "new";
            this.timestamp = "now";
            this.asset_id = asset_id;
            this.comment = comment;
            this.username = username;
            
        }
        
        public static void deleteRecord(String id, Connection con) throws Exception{
            String sql = "DELETE FROM asset_contact_records WHERE id = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1,id);
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("Comment not deleted.");
            }
        }
    }
        
    class DivisionContactRecordsTable extends JTable{
        private DivisionContactRecordsTableModel model;
        
        public DivisionContactRecordsTable(){
                model = new DivisionContactRecordsTableModel();
                this.setModel(model);
                this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                this.getTableHeader().setReorderingAllowed(false);
                this.setSelectionMode(0);
                
                this.getColumnModel().getColumn(0).setMinWidth(0);
                this.getColumnModel().getColumn(0).setPreferredWidth(0);
                
                this.getColumnModel().getColumn(1).setPreferredWidth(100);
                this.getColumnModel().getColumn(2).setPreferredWidth(350);
                this.getColumnModel().getColumn(3).setPreferredWidth(50);
                
        }
        
        public void populate(String asset_id, String last_visit_date){
            try{
                String sql = "SELECT id, Created_Time, Comment, username FROM asset_contact_records WHERE Asset_ID = ? AND Created_Time >= ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1,asset_id);
                stmt.setString(2,last_visit_date);
                
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
        
        private class DivisionContactRecordsTableModel extends AbstractTableModel{
            private String [] colNames;
            
            private Vector<Record> records = new Vector();
        
            public DivisionContactRecordsTableModel(){
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
                int size = records.size();
                for(int i=0;i<size;i++){
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
    
    class Inspection{
        public String description;
        public boolean poor;
        public boolean replace_at_next;
    }
    
    class LastVisitInspectionsTable extends JTable{
        private LastVisitInspectionsTableModel model;
        
        public LastVisitInspectionsTable(){
                model = new LastVisitInspectionsTableModel();
                this.setModel(model);
                this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                this.getTableHeader().setReorderingAllowed(false);
                this.setSelectionMode(0);
                
                this.getColumnModel().getColumn(0).setPreferredWidth(250);
                this.getColumnModel().getColumn(1).setPreferredWidth(100);
                this.getColumnModel().getColumn(2).setPreferredWidth(120);
                
        }
        
        public void populate(){
            try{

                String sql = " SELECT " + 
                "	inspectionmaster.Description, " + 
                "	inspection_log.Poor, " + 
                "	inspection_log.ReplaceatNext " + 
                " FROM " + 
                "	inspection_log " + 
                "	Inner Join inspectionmaster ON inspectionmaster.Inspection_ID = inspection_log.Inspection_ID " + 
                " WHERE " + 
                "  	(inspection_log.Poor = 'true' OR inspection_log.ReplaceatNext = 'true') " + 
                "  	AND " + 
                "  	(inspection_log.SO_No = (  SELECT " + 
                "                                   preventive_maintenance_log.PM_Work_Order_ID " + 
                "                                 FROM " + 
                "                                   preventive_maintenance_log " + 
                "                                   Inner Join scheduled_preventive_maintenance " + 
                "                                   ON scheduled_preventive_maintenance.Scheduled_ID = preventive_maintenance_log.Scheduled_ID " + 
                "                                 WHERE " + 
                "                                   scheduled_preventive_maintenance.Asset_ID = ? " + 
                "                                 ORDER BY preventive_maintenance_log.Done_Date DESC " + 
                "                                 LIMIT 1) " + 
                "  	)";
                
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1,ASSET_ID);
                                
                ResultSet rec = stmt.executeQuery();
                Inspection inspection = null;
                removeAll();
                while(rec.next()){
                    inspection = new Inspection();
                    inspection.description = rec.getString("description");
                    inspection.poor = rec.getBoolean("poor");
                    inspection.replace_at_next = rec.getBoolean("ReplaceatNext");
                    addInspection(inspection);
                }
            } 
            catch(Exception ex){
                ex.printStackTrace();
            } 
            
        }
                
        public void addInspection(Inspection inspection){
                model.addInspection(inspection);
                this.tableChanged(new TableModelEvent(model));
        }
        
                
        public void removeAll(){
                model.removeAll();
                this.tableChanged(new TableModelEvent(model));
        }
        
        private class LastVisitInspectionsTableModel extends AbstractTableModel{
            private String [] colNames;
            
            private Vector<Inspection> inspections = new Vector();
        
            public LastVisitInspectionsTableModel(){
                this.colNames = new String[]{
                                    "Description",          //0
                                    "Poor",                 //1
                                    "Replace At Next Visit",//2
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
                    return inspections.size();  
            }
        
            public Object getValueAt(int r, int c){
                    Inspection inspection = inspections.get(r);
                    switch(c){
                        case 0:return inspection.description;
                        case 1:return inspection.poor;
                        case 2:return inspection.replace_at_next;
                        default: return "";
                    }
            }
        
            public void setValueAt(Object value,int row,int col){
                    //nothing
            }
        
            public boolean isCellEditable(int r,int c){
                return false;
            }
                
            public void addInspection(Inspection inspection){
                    inspections.add(inspection);
            }
            
            public void removeAll(){
                    inspections.removeAllElements(); 
            }
        }
    }
    
    class Person{
        public String name;
        public String designation;
        public String phone;
        public String email;
    }
    
    class ContactPersonsTable extends JTable{
        private ContactPersonsTableModel model;
        
        public ContactPersonsTable(){
                model = new ContactPersonsTableModel();
                this.setModel(model);
                this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                this.getTableHeader().setReorderingAllowed(false);
                this.setSelectionMode(0);
                
                this.getColumnModel().getColumn(0).setPreferredWidth(200);
                this.getColumnModel().getColumn(1).setPreferredWidth(100);
                this.getColumnModel().getColumn(2).setPreferredWidth(100);
                this.getColumnModel().getColumn(3).setPreferredWidth(100);
                
        }
        
        public void populate(String customer){
            try{

                String sql = "SELECT " +
                                "IFNULL(Contact_Person,' ') AS Contact_Person, " +
                                "IFNULL(Designation,' ') AS Designation, " +
                                "IFNULL(Email,' ') AS Email, " +
                                "IFNULL(Direct_Phone_No,' ') AS Direct_Phone_No " +
                             "FROM division_contacts " +
                             "WHERE Division_ID=?";
                
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1,customer);
                                
                ResultSet rec = stmt.executeQuery();
                Person person = null;
                removeAll();
                while(rec.next()){
                    person = new Person();
                    person.name = rec.getString("Contact_Person");
                    person.designation = rec.getString("Designation");
                    person.phone = rec.getString("Direct_Phone_No");
                    person.email = rec.getString("Email");
                    addPerson(person);
                }
            } 
            catch(Exception ex){
                ex.printStackTrace();
            } 
            
        }
                
        public void addPerson(Person person){
                model.addPerson(person);
                this.tableChanged(new TableModelEvent(model));
        }
                       
        public void removeAll(){
                model.removeAll();
                this.tableChanged(new TableModelEvent(model));
        }
        
        private class ContactPersonsTableModel extends AbstractTableModel{
            private String [] colNames;
            
            private Vector<Person> persons = new Vector();
        
            public ContactPersonsTableModel(){
                this.colNames = new String[]{
                                    "Name",                 //0
                                    "Designation",          //1
                                    "Phone",                //2
                                    "Email"                 //3
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
                    return persons.size();  
            }
        
            public Object getValueAt(int r, int c){
                    Person person = persons.get(r);
                    switch(c){
                        case 0:return person.name;
                        case 1:return person.designation;
                        case 2:return person.phone;
                        case 3:return person.email;
                        default: return "";
                    }
            }
        
            public void setValueAt(Object value,int row,int col){
                    //nothing
            }
        
            public boolean isCellEditable(int r,int c){
                return false;
            }
                
            public void addPerson(Person person){
                    persons.add(person);
            }
            
            public void removeAll(){
                    persons.removeAllElements(); 
            }
        }
        
    }

    private void jbInit() throws Exception {
        this.setSize(new Dimension(960, 636));
        this.setLayout(null);
        titleBar.setBounds(new Rectangle(10, 10, 940, 70));
        jPanel1.setBounds(new Rectangle(10, 85, 450, 210));
        jPanel1.setLayout(null);
        jPanel1.setBorder(BorderFactory.createTitledBorder("Customer Details"));
        txtCustomerName.setBounds(new Rectangle(25, 25, 395, 20));
        txtCustomerName.setCaption("Customer Name");
        txtCustomerName.setTxtWidth(270);
        txtCustomerName.setEditable(false);
        txtContactPerson.setBounds(new Rectangle(25, 50, 355, 20));
        txtContactPerson.setCaption("Contact Person");
        txtContactPerson.setTxtWidth(210);
        txtContactPerson.setEditable(false);
        txtDesignation.setBounds(new Rectangle(25, 75, 305, 20));
        txtDesignation.setCaption("Designation");
        txtDesignation.setTxtWidth(150);
        txtDesignation.setEditable(false);
        jLabel1.setText("Address");
        jLabel1.setBounds(new Rectangle(25, 100, 70, 20));
        jScrollPane1.setBounds(new Rectangle(120, 100, 300, 65));
        txtAddress.setEditable(false);
        txtTelephone.setBounds(new Rectangle(25, 170, 195, 20));
        txtTelephone.setCaption("Telephone");
        txtTelephone.setTxtWidth(100);
        txtTelephone.setEditable(false);
        txtFax.setBounds(new Rectangle(260, 170, 160, 20));
        txtFax.setCaption("Fax");
        txtFax.setTxtWidth(100);
        txtFax.setEditable(false);
        txtFax.setLblWidth(60);
        txtLastVisit.setBounds(new Rectangle(245, 25, 175, 20));
        txtLastVisit.setCaption("Last Customer Visit");
        txtLastVisit.setTxtWidth(80);
        txtLastVisit.setEditable(false);
        jPanel2.setBounds(new Rectangle(10, 390, 945, 175));
        jPanel2.setLayout(null);
        jPanel2.setBorder(BorderFactory.createTitledBorder("Overdue/Oncoming Maintenances"));
        jScrollPane2.setBounds(new Rectangle(10, 20, 925, 145));
        jTabbedPane1.setBounds(new Rectangle(465, 90, 490, 295));
        jTabbedPane1.setBorder(BorderFactory.createTitledBorder(""));
        jPanel3.setLayout(null);
        jPanel4.setLayout(null);
        jScrollPane3.setBounds(new Rectangle(5, 5, 465, 215));
        btnAdd.setBounds(new Rectangle(255, 225, 105, 25));
        btnAdd.setText("Add");
        btnAdd.setEnabled(false);
        btnAdd.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnAdd_actionPerformed(e);
                    }
                });
        btnRemove.setBounds(new Rectangle(365, 225, 105, 25));
        btnRemove.setText("Remove");
        btnRemove.setEnabled(false);
        btnRemove.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnRemove_actionPerformed(e);
                    }
                });
        jPanel5.setBounds(new Rectangle(10, 295, 450, 90));
        jPanel5.setBorder(BorderFactory.createTitledBorder("Machine Details"));
        jPanel5.setLayout(null);
        txtAsset.setBounds(new Rectangle(25, 25, 210, 20));
        txtAsset.setCaption("Machine");
        txtAsset.setTxtWidth(150);
        txtAsset.setLblWidth(60);
        txtAsset.setEditable(false);
        txtModel.setBounds(new Rectangle(25, 50, 210, 20));
        txtModel.setCaption("Model");
        txtModel.setLblWidth(60);
        txtModel.setEditable(false);
        jScrollPane4.setBounds(new Rectangle(5, 5, 465, 250));
        btnR2M.setText("Reschedule To Current Meter");
        btnR2M.setBounds(new Rectangle(735, 570, 210, 30));
        btnR2M.setEnabled(false);
        btnR2M.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnR2M_actionPerformed(e);
                    }
                });
        jPanel6.setLayout(null);
        jScrollPane5.setBounds(new Rectangle(5, 5, 465, 250));
        jPanel7.setLayout(null);
        jScrollPane6.setBounds(new Rectangle(5, 5, 465, 245));
        jScrollPane1.getViewport().add(txtAddress, null);
        jPanel1.add(txtFax, null);
        jPanel1.add(txtTelephone, null);
        jPanel1.add(jScrollPane1, null);
        jPanel1.add(jLabel1, null);
        jPanel1.add(txtDesignation, null);
        jPanel1.add(txtContactPerson, null);
        jPanel1.add(txtCustomerName, null);
        jPanel3.add(btnRemove, null);
        jPanel3.add(btnAdd, null);
        jScrollPane3.getViewport().add(tblContactRecords, null);
        jPanel3.add(jScrollPane3, null);
        jTabbedPane1.addTab("Remarks", jPanel3);
        jScrollPane4.getViewport().add(tblInspections, null);
        jPanel4.add(jScrollPane4, null);
        jTabbedPane1.addTab("Last Visit Inspections", jPanel4);
        jScrollPane5.getViewport().add(tblPerons, null);
        jPanel6.add(jScrollPane5, null);
        jTabbedPane1.addTab("Contact Persons", jPanel6);
        jScrollPane6.getViewport().add(tblJobCompletion, null);
        jPanel7.add(jScrollPane6, null);
        jTabbedPane1.addTab("Last Visit Remarks", jPanel7);
        jPanel5.add(txtModel, null);
        jPanel5.add(txtAsset, null);
        jPanel5.add(txtLastVisit, null);
        this.add(btnR2M, null);
        this.add(jPanel5, null);
        this.add(jTabbedPane1, null);
        jScrollPane2.getViewport().add(tblMaintenace, null);
        jPanel2.add(jScrollPane2, null);
        this.add(jPanel2, null);
        this.add(jPanel1, null);
        this.add(titleBar, null);
    }
    
    private class JobCompletionRemarksTable extends JTable {
         private JobCompletionRemarksTableModel myModel = null;
              
         public JobCompletionRemarksTable(){
             myModel = new JobCompletionRemarksTableModel();
             this.setModel(myModel);
             
             this.setSelectionMode(0);
             this.getTableHeader().setReorderingAllowed(false);
             this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                                       
             this.getColumnModel().getColumn(0).setPreferredWidth(125);
             this.getColumnModel().getColumn(1).setPreferredWidth(350);
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
