package com.matrix.focus.log;

import com.matrix.components.MTextbox;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.ButtonCellRenderer;
import com.matrix.focus.util.DateTimeCell;
import com.matrix.focus.util.ImageLibrary;
import com.matrix.focus.util.LockedLabelCellRenderer;
import com.matrix.focus.util.StandardTimeCell;
import com.matrix.focus.util.Validator;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Savepoint;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

public class PMLabourUtilisationDialog extends JDialog{
         private JScrollPane jScrollPane1 = new JScrollPane();
         private LabourLogTable tblLabour = new LabourLogTable();
         private JButton btnDone = new JButton(new ImageIcon(ImageLibrary.BUTTON_OK));
         private LabourUtilisationEntry lue;
         private MTextbox mtxtTeam = new MTextbox();
         private MessageBar messageBar;
         private Connection connection;
         private JFrame frame;
         private String WORK_ORDER = "";
         private PMLabourCostDialog dlgCost;

         public PMLabourUtilisationDialog(JFrame frame,Connection conn,MessageBar msgBar,boolean capable){
             super(frame, "Labour Utilisation", true);
             connection = conn;
             messageBar = msgBar;
             this.frame = frame;
                 
             btnDone.setEnabled(capable);
             
             dlgCost = new PMLabourCostDialog(this,connection,msgBar,capable);
             dlgCost.setLocationRelativeTo(this);
                 try{
                     jbInit();
                     tblLabour.addMouseListener(new MouseAdapter(){
                          public void mouseClicked(MouseEvent me){
                              if(me.getButton()==1 && tblLabour.getSelectedColumn()==8){
                                  int row = tblLabour.getSelectedRow();
                                  if(row!=-1){
                                      dlgCost.setEmployee(
                                        tblLabour.getValueAt(row,1).toString(),
                                        WORK_ORDER,
                                        tblLabour.getValueAt(row,0).toString(),
                                        tblLabour.getValueAt(row,6).toString()
                                      );
                                      dlgCost.setVisible(true);
                                      tblLabour.setValueAt(dlgCost.getTotalCost(),tblLabour.getSelectedRow(),7);
                                      tblLabour.repaint();
                                  }
                                  else{
                                      messageBar.setMessage("Select a row in the tasks table.","ERROR");
                                  } 
                              }
                          }
                     });
                 }
             catch(Exception e){
                 e.printStackTrace();
             }
         }

         private void jbInit() throws Exception{
             this.setSize(new Dimension(926, 288));
             this.setResizable(false);
             this.getContentPane().setLayout(null);
             mtxtTeam.setLocation(10,10);
             mtxtTeam.setCaption("Maintenance Team");
             mtxtTeam.setLblFont(new Font("Tahoma",0,11));
             mtxtTeam.setTxtFont(new Font("Tahoma",0,11));
             mtxtTeam.setEditable(false);
             mtxtTeam.setLblWidth(120);
             mtxtTeam.setTxtWidth(150);
             this.getContentPane().add(mtxtTeam);

            this.getContentPane().add(btnDone, null);
            jScrollPane1.setBounds(new Rectangle(10, 40, 900, 170));
            btnDone.setBounds(new Rectangle(810, 220, 100, 25));
            btnDone.setText("OK");
            btnDone.addActionListener(new ActionListener() {
                                     public void actionPerformed(ActionEvent e) {
                                         btnDone_actionPerformed(e);
                                     }
                                 });
            jScrollPane1.getViewport().add(tblLabour, null);
             this.getContentPane().add(jScrollPane1, null);
         }

         public void setVisible(boolean bool){
             super.setVisible(bool);
             tblLabour.deleteAll();
         }
         
         public void populate(String work_order_id){
             this.WORK_ORDER = work_order_id;
             String sql = "SELECT lu.Team_ID, " +
                                 "lu.Skill_Category, " +
                                 "e.name, " +
                                 "lu.Employee_ID, " +
                                 "IF(lu.Starting_Time='0000-00-00 00:00:00','0000-00-00 00:00:00',lu.Starting_Time) AS Starting_Time," +
                                 "IF(lu.Ending_Time='0000-00-00 00:00:00','0000-00-00 00:00:00',lu.Ending_Time) AS Ending_Time," +
                                 "lu.Worked_Hours, " +
                                 "lu.Time_Slot_ID, " +
                                 "lu.Asset_ID, " +
                                 "lu.PM_Job " +
                          "FROM labour_utilisation lu, " +
                               "employee e " +
                          "WHERE PM_Work_Order_ID='"+ work_order_id +"' AND " +
                                "lu.Employee_ID = e.Employee_ID " +
                          "ORDER BY " +
                                "lu.Employee_ID," +
                                "lu.Skill_Category," +
                                "lu.Time_Slot_ID";
             try{
                 ResultSet rec = connection.createStatement().executeQuery(sql);
                 int cnt = 0;
                 while(rec.next()){
                     cnt = rec.getRow()-1;
                     tblLabour.addRow();
                     mtxtTeam.setInputText(rec.getString("Team_ID"));
                     tblLabour.setValueAt(rec.getString("Skill_Category"),cnt,0);
                     tblLabour.setValueAt(rec.getString("Employee_ID"),cnt,1);
                     tblLabour.setValueAt(rec.getString("Name"),cnt,2);
                     
                     tblLabour.setValueAt(rec.getString("Starting_Time"),cnt,3);
                     tblLabour.setValueAt(rec.getString("Ending_Time"),cnt,4);
                     tblLabour.setValueAt(rec.getString("Worked_Hours"),cnt,5);
                     tblLabour.setValueAt(rec.getString("Time_Slot_ID"),cnt,6);
                     tblLabour.setValueAt(rec.getString("Asset_ID"),cnt,9);
                     tblLabour.setValueAt(rec.getString("PM_Job"),cnt,10);
                 }
             }
             catch(Exception er){
                er.printStackTrace();
             }
         }
         
         private boolean saveEmployeeLog(){
             int rows = tblLabour.getRowCount();
            
             for(int j=0;j<rows;j++){
                 lue = new LabourUtilisationEntry(connection);
                 lue.team_id = mtxtTeam.getInputText();
                 lue.pm_work_order_id = WORK_ORDER;
                 lue.employee_id = tblLabour.getValueAt(j,1).toString();
                 lue.starting_time = tblLabour.getValueAt(j,3).toString();
                 lue.ending_time = tblLabour.getValueAt(j,4).toString();
                 lue.worked_hours = tblLabour.getValueAt(j,5).toString();
                 lue.skill_category = tblLabour.getValueAt(j,0).toString();
                 lue.time_slot_id = tblLabour.getValueAt(j,6).toString();
                    
                 try{
                     lue.log();
                 }
                 catch (Exception e) {
                    e.printStackTrace();
                    return false;
                 }
             }
             return true;
         }

         private boolean validateEmployeeInfo(){
             int rows = tblLabour.getRowCount();
             boolean value = false;
             String empID = "";

             for(int j=0;j<rows;j++){
                 empID = tblLabour.getValueAt(j,1).toString().trim();
                 
                 if(Validator.isEmpty(empID)){
                     messageBar.setMessage("Employee ID field is empty in row "+(j+1),"ERROR");
                     return false;
                 }
                 else{
                     value = true;
                 }
             }
             return value;
         }

    void btnDone_actionPerformed(ActionEvent e){
        if(validateEmployeeInfo()){
            Savepoint sPointEmployee;
            try{
                sPointEmployee = connection.setSavepoint("Employee");
                if(saveEmployeeLog()){ 
                    messageBar.setMessage("Labour utilisation saved.","OK");
                    this.setVisible(false);
                }
                else{
                    connection.rollback(sPointEmployee);
                    messageBar.setMessage("Could not save Labour utilisation.","ERROR");
                }
            }
            catch(Exception er){
                er.printStackTrace();
            }
        }
    }

    public class LabourLogTable extends JTable {
             private LabourAllocationTableModel myModel = null ;

             public LabourLogTable() {
                 myModel = new LabourAllocationTableModel();
                 this.setModel(myModel);
                 
                 TableColumn tc = this.getColumnModel().getColumn(5);
                 tc.setCellEditor(new DefaultCellEditor(new StandardTimeCell(frame)));
                 tc = this.getColumnModel().getColumn(3);
                 tc.setCellEditor(new DefaultCellEditor(new DateTimeCell(frame)));
                 tc = this.getColumnModel().getColumn(4);
                 tc.setCellEditor(new DefaultCellEditor(new DateTimeCell(frame)));
                 
                 this.getColumnModel().getColumn(0).setCellRenderer(new LockedLabelCellRenderer());
                 this.getColumnModel().getColumn(1).setCellRenderer(new LockedLabelCellRenderer());
                 this.getColumnModel().getColumn(2).setCellRenderer(new LockedLabelCellRenderer());
                 this.getColumnModel().getColumn(6).setCellRenderer(new LockedLabelCellRenderer());
                 this.getColumnModel().getColumn(9).setCellRenderer(new LockedLabelCellRenderer());
                 this.getColumnModel().getColumn(10).setCellRenderer(new LockedLabelCellRenderer());
                 
                 this.getColumnModel().getColumn(8).setCellRenderer(new ButtonCellRenderer());
                 
                 this.getColumnModel().getColumn(0).setPreferredWidth(70);
                 this.getColumnModel().getColumn(1).setPreferredWidth(80);
                 this.getColumnModel().getColumn(2).setPreferredWidth(150);
                 this.getColumnModel().getColumn(3).setPreferredWidth(110);
                 this.getColumnModel().getColumn(4).setPreferredWidth(110);
                 this.getColumnModel().getColumn(5).setPreferredWidth(80);
                 this.getColumnModel().getColumn(6).setPreferredWidth(60);
                 this.getColumnModel().getColumn(7).setPreferredWidth(80);
                 this.getColumnModel().getColumn(8).setPreferredWidth(20);
                 
                 this.setSelectionMode(0);
                 this.getTableHeader().setReorderingAllowed(false);
                 this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
             }
  
             public void addRow(){
                 myModel.addRow();
                 this.tableChanged(new TableModelEvent(myModel)); 
             }

             public void deleteRow(){
                 myModel.deleteRow();
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

             private class LabourAllocationTableModel extends AbstractTableModel{
                 private int rowCount = 0;
                 private String [] colNames = {"Category",//0
                                                "Employee ID",//1
                                                "Name",//2
                                                "Start Time",//3
                                                "End Time",//4
                                                "Productive Time",//5
                                                "Time Slot",//6
                                                "Cost",//7
                                                "",//8
                                                "Machine",//9
                                                "Job"};//10
                 private Object [][] valueArray = null;
                 private Object [][] tempArray = null;


                 public LabourAllocationTableModel() {
                     valueArray = new Object[rowCount][colNames.length];
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
                     if(valueArray[r][c]==null) return "";
                     else return valueArray[r][c];
                 }
          
                 public void setValueAt(Object value,int row,int col){
                     valueArray[row][col] = value;
                 }
          
                 public boolean isCellEditable(int r,int c){
                     if(c==3 || c==4 || c==5) return true;
                     else return false;
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

                 public void deleteRow(){
                     tempArray = new Object[this.getRowCount()-1][this.getColumnCount()];
                     for(int y=0 ; y<tempArray.length; y++){
                         for(int x=0; x<tempArray[0].length; x++){
                             tempArray[y][x] =  valueArray[y][x];
                         }
                     } 
                     valueArray = tempArray;
                 }

                 public void deleteRow(int i){
                     try{
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
                     catch(Exception er){
                     }
                 }

                 public void deleteAll(){
                     valueArray = new Object[0][colNames.length];
                 }
             }           
         }
     }
