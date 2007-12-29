package com.matrix.focus.mdi;

import com.matrix.components.TitleBar;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.Authorizer;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.ImageLibrary;
import com.matrix.focus.util.MPanel;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

public class GUIOncomingVisits extends MPanel{
    private TitleBar titleBar = new TitleBar();
    private JPanel jPanel1 = new JPanel();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private OncomingEventsTable tblEvents = new OncomingEventsTable();
    private Connection connection;
    private MessageBar messagerBar;
    private MDI frame;
    private PMWorkOrderDialog dlg;

    public GUIOncomingVisits(DBConnectionPool pool, final MDI frame, MessageBar msgBar) {
        connection = pool.getConnection();
        messagerBar = msgBar;
        this.frame = frame;
        try {
            titleBar.setTitle("Oncoming Scheduled Maintenances");
            titleBar.setDescription("The facility to view oncoming maintenace visits.");
            titleBar.setImage(ImageLibrary.TITLE_PREVENTIVE_MAINTENANCE_WORKORDER);
            
            jbInit();
            
            tblEvents.addMouseListener(
                new MouseAdapter(){
                    public void mouseClicked(MouseEvent me){
                        int row = tblEvents.getSelectedRow();
                        if(row!=-1){
                            if(me.getClickCount()==2){
                                String machine = tblEvents.getValueAt(row,2).toString();
                                 if(Authorizer.isCapable(MDI.USERNAME,Authorizer.JOB_CONFIRMATION,connection)){
                                     if(MessageBar.YES_OPTION == MessageBar.showConfirmDialog(frame,"This will load the Job Confirmation window.\nDo you want to proceed?","On Coming Schedule")){
                                         frame.menuItemJobConfirmation.doClick();
                                         frame.guiJobConfirmation.setAsset(machine);
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
    
    public void setWeeks(int weeks){
        tblEvents.populate(weeks);
    }

    private void jbInit() throws Exception {
        this.setLayout(null);
        this.setSize(new Dimension(951, 522));
        titleBar.setBounds(new Rectangle(10, 10, 940, 70));
        jPanel1.setBounds(new Rectangle(10, 85, 940, 430));
        jPanel1.setLayout(null);
        jPanel1.setBorder(BorderFactory.createTitledBorder("Pending Jobs"));
        jScrollPane1.setBounds(new Rectangle(10, 20, 920, 400));
        jScrollPane1.getViewport().add(tblEvents, null);
        jPanel1.add(jScrollPane1, null);
        this.add(jPanel1, null);
        this.add(titleBar, null);
    }

    class Event{
        public String date;              //0
        public String customer;          //1
        public String machine;           //2
        public String model;             //3
    }
    
    class OncomingEventsTable extends JTable{
        private OncomingEventsTableModel model;
        
        public OncomingEventsTable(){
                model = new OncomingEventsTableModel();
                this.setModel(model);
                this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                this.getTableHeader().setReorderingAllowed(false);
                this.setSelectionMode(0);
                
                this.getColumnModel().getColumn(0).setPreferredWidth(100);
                this.getColumnModel().getColumn(1).setPreferredWidth(300);
                this.getColumnModel().getColumn(2).setPreferredWidth(200);
                this.getColumnModel().getColumn(3).setPreferredWidth(200);
        }
        
        public void populate(int weeks){
            /**
             * From last visit to today     :- weeks = 0
             * From last visit to nextweek  :- weeks = 1
             * From last visit to next2weeks:- weeks = 2
             * From last visit to next3weeks:- weeks = 3
             * */
             
            String sql =      "SELECT DISTINCT " +
                                    "get_next_customer_visit(asset.Asset_ID) AS Scheduled_Date," +
                                    "division.Name AS Customer," +
                                    "asset.Asset_ID AS Machine," +
                                    "asset.Model_ID AS Model " +
                              "FROM " +
                                    "scheduled_preventive_maintenance " +
                                    "Left Outer Join preventive_maintenance_log ON scheduled_preventive_maintenance.Scheduled_ID = preventive_maintenance_log.Scheduled_ID " +
                                    "Inner Join preventive_maintenance ON scheduled_preventive_maintenance.Preventive_Maintenance_ID = preventive_maintenance.ID " +
                                    "Inner Join asset ON asset.Asset_ID = scheduled_preventive_maintenance.Asset_ID " +
                                    "Inner Join division ON asset.Comp_ID = division.Comp_ID AND asset.Dept_ID = division.Dept_ID AND asset.Division_ID = division.Division_ID " +
                              "WHERE " +
                                    "(get_next_customer_visit(asset.Asset_ID) BETWEEN  get_last_customer_visit(asset.Asset_ID) AND DATE_ADD(DATE(NOW()),INTERVAL 7 * ? DAY)) " +
                             "ORDER BY " +
                                    "Scheduled_Date ASC";
                            
            try{
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1,weeks);
                ResultSet rec = stmt.executeQuery();
                this.removeAll();
                while(rec.next()){
                    Event event = new Event();
                    event.date = rec.getString("Scheduled_Date");
                    event.customer = rec.getString("Customer");
                    event.machine = rec.getString("Machine");
                    event.model = rec.getString("Model");
                    this.addEvent(event);
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            
        }
                
        public void addEvent(Event event){
                model.addEvent(event);
                this.tableChanged(new TableModelEvent(model));
        }
                
        public void removeAll(){
                model.removeAll();
                this.tableChanged(new TableModelEvent(model));
        }
                
        private class OncomingEventsTableModel extends AbstractTableModel{
            private String [] colNames;
            
            private Vector<Event> events = new Vector();
        
            public OncomingEventsTableModel(){
                this.colNames = new String[]{
                                    "Scheduled Date",    //0
                                    "Customer",          //1
                                    "Machine",           //2
                                    "Model"              //3
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
                    return events.size();  
            }
        
            public Object getValueAt(int r, int c){
                    Event event = events.get(r);
                    switch(c){
                        case 0:return event.date;
                        case 1:return event.customer;
                        case 2:return event.machine;
                        case 3:return event.model;
                        default: return "";
                    }
            }
        
            public void setValueAt(Object value,int row,int col){
                    //nothing
            }
        
            public boolean isCellEditable(int r,int c){
                return false;
            }
                
            public void addEvent(Event event){
                    events.add(event);
            }
            
            public void removeAll(){
                    events.removeAllElements(); 
            }
        }            
    } 
}

