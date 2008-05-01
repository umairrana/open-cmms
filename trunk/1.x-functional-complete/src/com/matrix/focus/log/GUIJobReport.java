package com.matrix.focus.log;

import com.matrix.components.MDatebox;
import com.matrix.components.MTextbox;
import com.matrix.components.TitleBar;
import com.matrix.focus.util.MPanel;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.ButtonCellRenderer;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.DataAssistantDialog;
import com.matrix.focus.util.DateCell;
import com.matrix.focus.util.DateTimeCell;
import com.matrix.focus.util.ImageLibrary;
import com.matrix.focus.util.LockedLabelCellRenderer;
import com.matrix.focus.util.StandardTimeCell;
import com.matrix.focus.util.Validator;

import com.matrix.focus.workorder.PreventiveMaintenanceWorkOrder;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class GUIJobReport extends MPanel{

    private Connection connection;
    private TitleBar titlebar = new TitleBar();
    private JPanel jPanel1 = new JPanel();
    private JFrame frame;
    private MessageBar messageBar;
    private JButton btnDelete = new JButton(new ImageIcon(ImageLibrary.BUTTON_REMOVE));
    private JButton btnAdd = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));
    private JPanel jPanel2 = new JPanel();
    private JButton btnView = new JButton(new ImageIcon(ImageLibrary.BUTTON_VIEW));
    private JTabbedPane tabPane = new JTabbedPane();
    private MTextbox txtCustomer = new MTextbox();
    private String SELECTED_CUSTOMER = "";
    private JButton btnCustomer =  new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private MTextbox txtAsset = new MTextbox();
    private JButton btnAsset = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private PreventiveMaintenanceWorkOrder WORK_ORDER;

    public GUIJobReport(DBConnectionPool pool, JFrame frame, MessageBar msgBar){
        this.connection = pool.getConnection();
        this.frame = frame;
        this.messageBar = msgBar;
        try {
            titlebar.setTitle("Job Report");
            titlebar.setDescription("Information on jobs are shown here.");
            titlebar.setImage(ImageLibrary.TITLE_PREVENTIVE_MAINTENANCE_LOG);
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(null);
        this.setSize(new Dimension(805, 533));
        titlebar.setBounds(new Rectangle(10, 10, 715, 70));
        jPanel1.setBounds(new Rectangle(10, 190, 720, 330));
        jPanel1.setBorder(BorderFactory.createTitledBorder("Job Information"));
        jPanel1.setLayout(null);
        jPanel2.setBounds(new Rectangle(10, 85, 715, 95));
        jPanel2.setBorder(BorderFactory.createTitledBorder("Selection"));
        jPanel2.setLayout(null);
        txtCustomer.setCaption("Customer:");
        btnCustomer.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnCustomer_actionPerformed(e);
                    }
                });
        btnView.setText("View");
        btnView.setBounds(new Rectangle(375, 55, 90, 20));
        btnView.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnView_actionPerformed(e);
                    }
                });
        tabPane.setBounds(new Rectangle(10, 20, 700, 300));
        txtCustomer.setBounds(new Rectangle(20, 25, 300, 25));
        btnCustomer.setBounds(new Rectangle(325, 25, 25, 20));
        btnCustomer.setSize(new Dimension(30, 20));
        txtAsset.setBounds(new Rectangle(20, 55, 300, 20));
        txtAsset.setCaption("Machine");
        btnAsset.setBounds(new Rectangle(325, 55, 30, 20));
        btnAsset.setSize(new Dimension(30, 20));
        btnAsset.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnAsset_actionPerformed(e);
                    }
                });
        jPanel1.add(tabPane, null);
        jPanel2.add(btnAsset, null);
        jPanel2.add(txtAsset, null);
        jPanel2.add(btnCustomer, null);
        jPanel2.add(txtCustomer, null);
        jPanel2.add(btnView, null);
        jPanel2.add(btnCustomer, null);
        this.add(jPanel2, null);
        this.add(jPanel1, null);
        this.add(titlebar, null);
    }

    public void  btnView_actionPerformed(ActionEvent e){
        
    }

    private void btnCustomer_actionPerformed(ActionEvent e) {
            //to select all the customers from database             
            String sql = "SELECT division_id AS ID, Name, Address " +
                         "FROM division " +
                         "ORDER BY Name";
                         
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
            }
    }
    
    private void btnAsset_actionPerformed(ActionEvent e) {
            String sql = "SELECT asset_id AS 'Machine', model_id AS 'Model' FROM asset WHERE division_id ='"+ SELECTED_CUSTOMER +"'";
            
            DataAssistantDialog d = new DataAssistantDialog(frame,"Select Machine - Data Assistant",sql,connection);
            d.setFirstColumnWidth(150);
            d.setSecondColumnWidth(150);
            d.setLocationRelativeTo(btnCustomer);
            d.setVisible(true); 
            String rtnVal = d.getValue();
            if(!rtnVal.equals("")){
                txtAsset.setInputText(rtnVal);
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

}
