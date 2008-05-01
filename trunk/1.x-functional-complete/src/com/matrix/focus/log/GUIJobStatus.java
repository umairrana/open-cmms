package com.matrix.focus.log;

import com.matrix.focus.util.ProgressBarRenderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

public class GUIJobStatus extends JDialog{
    private JScrollPane jScrollPane1 = new JScrollPane();
    private TasksTable tblTasks;
    private Connection connection;
    private JPanel jPanel1 = new JPanel();
    private JLabel jLabel1 = new JLabel();
    private JLabel jLabel2 = new JLabel();
    private JLabel jLabel3 = new JLabel();
    private JLabel jLabel4 = new JLabel();
    private JLabel jLabel5 = new JLabel();
    private JLabel jLabel6 = new JLabel();
    private int days_gap;

    public GUIJobStatus(JFrame frame, String jobno, String customer,int days_gap, Connection connection) {
        super(frame);
        try{
            this.connection = connection;
            tblTasks = new TasksTable(days_gap);
            jbInit();
            setTitle("Job : "+jobno + " Customer : "+customer);
            populate(jobno);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void populate(String jobno) throws SQLException {
        String sql = "SELECT " +
                        "log.Task_ID, " +
                        "task.Description, " +
                        "IF(log.Done_Date='0000-00-00 00:00:00','0000-00-00 00:00:00',log.Done_Date) AS Done_Date, " +
                        "log.Time_Taken, " +
                        "log.Success " +
                      "FROM " +
                         "requested_maintenance_log log, " +
                         "upmaintenance_master task " +
                      "WHERE " +
                         "log.PM_Work_Order_ID=? AND " +
                         "log.Task_ID = task.UPM_ID";
                         
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,jobno);
                         
        ResultSet rec = stmt.executeQuery();
        tblTasks.removeAll();
        while(rec.next()){
            Task t = new Task();
            t.upm_id = rec.getString("Task_ID");
            t.description = rec.getString("Description");
            t.starting_date = rec.getString("Done_Date");
            t.time_take = rec.getString("Time_Taken");
            t.completed_amount = rec.getInt("Success");
            tblTasks.addTask(t);
        }
    }

    private void jbInit() throws Exception {
        this.setLayout( null );
        this.setSize(new Dimension(843, 396));
        jScrollPane1.setBounds(new Rectangle(5, 60, 825, 300));
        jPanel1.setBounds(new Rectangle(5, 5, 825, 50));
        jPanel1.setBorder(BorderFactory.createTitledBorder("Job Completion Status "));
        jPanel1.setLayout(null);
        jLabel1.setText("Job Duration");
        jLabel1.setBounds(new Rectangle(95, 20, 75, 15));
        jLabel2.setBounds(new Rectangle(180, 20, 100, 15));
        jLabel2.setBackground(Color.gray);
        jLabel2.setOpaque(true);
        jLabel3.setBackground(Color.green);
        jLabel3.setOpaque(true);
        jLabel3.setBounds(new Rectangle(370, 20, 100, 15));
        jLabel4.setText("Due ");
        jLabel4.setBounds(new Rectangle(325, 20, 40, 15));
        jLabel5.setBackground(Color.blue);
        jLabel5.setOpaque(true);
        jLabel5.setBounds(new Rectangle(600, 20, 100, 15));
        jLabel6.setText("Completed");
        jLabel6.setBounds(new Rectangle(520, 20, 75, 15));
        jPanel1.add(jLabel6, null);
        jPanel1.add(jLabel5, null);
        jPanel1.add(jLabel4, null);
        jPanel1.add(jLabel3, null);
        jPanel1.add(jLabel2, null);
        jPanel1.add(jLabel1, null);
        this.getContentPane().add(jPanel1, null);
        jScrollPane1.getViewport().add(tblTasks, null);
        this.add(jScrollPane1, null);
    }
    
    class Task{
        public String upm_id;
        public String description;
        public String starting_date;
        public String time_take;
        public int completed_amount;
    }
    
    class TasksTable extends JTable{
        private TasksTableModel model;
        private int days_gap;
        
        public TasksTable(int days_gap){
            this.days_gap = days_gap;
                model = new TasksTableModel();
                this.setModel(model);
                this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                this.getTableHeader().setReorderingAllowed(false);
                this.setSelectionMode(0);
                
                this.getColumnModel().getColumn(0).setPreferredWidth(50);
                this.getColumnModel().getColumn(1).setPreferredWidth(200);
                this.getColumnModel().getColumn(2).setPreferredWidth(80);
                this.getColumnModel().getColumn(3).setPreferredWidth(80);
                this.getColumnModel().getColumn(4).setPreferredWidth(400);
                                
                this.getColumnModel().getColumn(4).setCellRenderer(new ProgressBarRenderer(this,GUIJobStatus.this.days_gap));
                this.setRowHeight(30);
                this.setGridColor(Color.BLACK);
        }
                
        public void addTask(Task task){
                model.addTask(task);
                this.tableChanged(new TableModelEvent(model));
        }
        
                
        public void removeAll(){
                model.removeAll();
                this.tableChanged(new TableModelEvent(model));
        }
        
        private class TasksTableModel extends AbstractTableModel{
            private String [] colNames;
            
            private Vector<Task> tasks = new Vector();
        
            public TasksTableModel(){
                this.colNames = new String[]{
                                    "UPM",                  //0
                                    "Description",          //1
                                    "Started Date",        //2
                                    "Time Taken",           //3
                                    "Completed Amount"      //4
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
                    return tasks.size();  
            }
        
            public Object getValueAt(int r, int c){
                    Task task = tasks.get(r);
                    switch(c){
                        case 0:return task.upm_id;
                        case 1:return task.description;
                        case 2:return task.starting_date;
                        case 3:return task.time_take;
                        case 4:return task.completed_amount + "_" + task.time_take;
                        default: return "";
                    }
            }
        
            public void setValueAt(Object value,int row,int col){
                    //nothing
            }
        
            public boolean isCellEditable(int r,int c){
                return false;
            }
                
            public void addTask(Task task){
                    tasks.add(task);
            }
            
            public void removeAll(){
                    tasks.removeAllElements(); 
            }
        }
    }
}
