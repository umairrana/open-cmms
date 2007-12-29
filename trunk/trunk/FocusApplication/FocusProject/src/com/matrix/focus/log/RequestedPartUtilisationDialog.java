package com.matrix.focus.log;

import com.matrix.focus.mdi.MDI;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.ImageLibrary;
import com.matrix.focus.util.LockedLabelCellRenderer;
import com.matrix.focus.util.Validator;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Savepoint;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

public class RequestedPartUtilisationDialog extends JDialog implements ActionListener{
     private JScrollPane jScrollPane1 = new JScrollPane();
     private PartUtilizationTable tblReqParts = new PartUtilizationTable();
     private JButton btnDone = new JButton(new ImageIcon(ImageLibrary.BUTTON_OK));
     private RequestedJobsPartAllocationEntry pue;
     
     private MDI frame;
     private MessageBar messageBar;
     private Connection connection;
     private boolean capable;
     private String work_order;
     private String asset_id;
     private String ump_id;
     
     public RequestedPartUtilisationDialog(MDI frame, MessageBar msgBar, Connection con, boolean capable){
             super(frame, "Part Utilisation", true);
             this.frame = frame;
             this.messageBar = msgBar;
             this.connection = con;
             this.capable = capable;

             try{
                jbInit();
             }
             catch(Exception e){
                e.printStackTrace();
             }
     }

     private void jbInit() throws Exception{
         this.setSize(new Dimension(905, 240));
         this.setResizable(false);
         this.getContentPane().setLayout(null);
         jScrollPane1.setBounds(new Rectangle(10, 10, 880, 155));
         btnDone.setBounds(new Rectangle(790, 170, 100, 25));
         btnDone.setText("OK");
         btnDone.addActionListener(this);
         jScrollPane1.getViewport().add(tblReqParts, null);
         
         this.getContentPane().add(btnDone, null);
         this.getContentPane().add(jScrollPane1, null);
             
         btnDone.setEnabled(capable);
     }

     public void actionPerformed(ActionEvent e){
             Object click = e.getSource();
             if(click==btnDone){
                     if(tblReqParts.getRowCount()>0){
                             if(validatePartRequirementEntry()){
                                     try{
                                            Savepoint sPointPart= connection.setSavepoint("Part");
                                            try{
                                                savePartUtilisation();
                                                messageBar.setMessage("Part utilisation for the selected maintenance was saved.","OK");
                                                this.setVisible(false);
                                            }
                                            catch (Exception er) {
                                                connection.rollback(sPointPart);
                                                messageBar.setMessage(er.getMessage(),"ERROR");
                                            }
                                     }
                                     catch(Exception er){
                                             er.printStackTrace();
                                     }
                             }
                     }
                     else{
                             messageBar.setMessage("No Parts to be saved.","ERROR");
                     }
             }
     }

 public void populate(String work_order, String asset_id, String ump_id){ 
              this.work_order = work_order;
              this.asset_id = asset_id;
              this.ump_id = ump_id;
              
              String sql = "SELECT " + 
                              "pu.Part_ID, " + 
                              "p.Description, " +
                              "pu.Brand, " +
                              "pu.Supplier, " +
                              "pu.Batch, " + 
                              "pu.Required_Amount, " + 
                              "p.Unit_Price," + 
                              "pu.Used_Amount," +
                              "p.Unit " + 
                           "FROM " + 
                              "part_utilization_for_requested_maintenance pu, " + 
                              "part p " +  
                           "WHERE " +
                              "pu.PM_Work_Order_ID =? AND " +
                              "pu.Asset_ID =? AND " +
                              "pu.Task_ID =? AND " +
                              "pu.Part_ID = p.Part_ID AND " +
                              "pu.Authorized = 'true'";
              try{
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setString(1,work_order);
                    stmt.setString(2,asset_id);
                    stmt.setString(3,ump_id);
                    
                  ResultSet rec = stmt.executeQuery();
                  int cnt = 0;
                  tblReqParts.deleteAll();
                  while(rec.next()){
                      cnt = rec.getRow()-1;
                      tblReqParts.addRow();
                      tblReqParts.setValueAt(rec.getString("Part_ID"),cnt,0);
                      tblReqParts.setValueAt(rec.getString("Description"),cnt,1);
                      tblReqParts.setValueAt(rec.getString("Brand"),cnt,2);
                      tblReqParts.setValueAt(rec.getString("Supplier"),cnt,3);
                      tblReqParts.setValueAt(rec.getString("Batch"),cnt,4);
                      tblReqParts.setValueAt(rec.getString("Required_Amount"),cnt,5);
                      tblReqParts.setValueAt(rec.getString("Used_Amount"),cnt,6);
                      tblReqParts.setValueAt(rec.getString("Unit"),cnt,7);
                      tblReqParts.setValueAt(rec.getString("Unit_Price"),cnt,8);
                      
                  }
              }
              catch(Exception er){
                  er.printStackTrace();
              }
          }

     private void savePartUtilisation() throws Exception{
             for(int i=0;i<tblReqParts.getRowCount();i++){ 
                 pue = new RequestedJobsPartAllocationEntry(connection);
                 pue.pm_work_order_id = work_order;
                 pue.asset_id = asset_id;
                 pue.task_id = ump_id;
                 pue.part_id = tblReqParts.getValueAt(i,0).toString();
                 pue.brand = tblReqParts.getValueAt(i,2).toString();
                 pue.supplier = tblReqParts.getValueAt(i,3).toString();
                 pue.batch = tblReqParts.getValueAt(i,4).toString();
                 pue.used_amount = tblReqParts.getValueAt(i,6).toString();
                     
                 pue.log();
             }
     }

     private boolean validatePartRequirementEntry(){
             /**Get All labour Requirements*/
             int rows = tblReqParts.getRowCount();
             /**Return value*/
             boolean value = false;
             String qty = "";
             for(int j=0;j<rows;j++){
                     qty = tblReqParts.getValueAt(j,6).toString().trim();       
                     if(!validateForNumber(qty)){
                             messageBar.setMessage("Invalid value for Used Quantity in row "+(j+1),"ERROR");
                             return false;
                     }
                     else{
                             value = true;
                     }
             }
             return value;
     }

     private boolean validateForEmpty(String value){
             return ((Validator.isEmpty(value))?false:true);
     }

     private boolean validateForNumber(String value){
             if(Validator.isEmpty(value)){
                     return false;
             }
             else if(!Validator.isNumber(value)){
                     return false;
             }
             else if(!Validator.isNonNegative(value)){
                     return false;
             }
             else{
                     return true;
             }
     }

     public class PartUtilizationTable extends JTable {
             private PRTableModel myModel = null ;

             public PartUtilizationTable() {
                     myModel = new PRTableModel();
                     this.setModel(myModel);
                     
                     this.setSelectionMode(0);
                     this.getTableHeader().setReorderingAllowed(false);
                     this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                     
                     this.getColumnModel().getColumn(0).setCellRenderer(new LockedLabelCellRenderer());
                     this.getColumnModel().getColumn(1).setCellRenderer(new LockedLabelCellRenderer());
                     this.getColumnModel().getColumn(2).setCellRenderer(new LockedLabelCellRenderer());
                     this.getColumnModel().getColumn(3).setCellRenderer(new LockedLabelCellRenderer());
                     this.getColumnModel().getColumn(4).setCellRenderer(new LockedLabelCellRenderer());
                     this.getColumnModel().getColumn(5).setCellRenderer(new LockedLabelCellRenderer());
                     this.getColumnModel().getColumn(7).setCellRenderer(new LockedLabelCellRenderer());
                     this.getColumnModel().getColumn(8).setCellRenderer(new LockedLabelCellRenderer());
                     this.getColumnModel().getColumn(10).setCellRenderer(new LockedLabelCellRenderer());
                     
                     this.getColumnModel().getColumn(0).setPreferredWidth(100);
                     this.getColumnModel().getColumn(1).setPreferredWidth(180);
                     this.getColumnModel().getColumn(2).setPreferredWidth(100);
                     this.getColumnModel().getColumn(3).setPreferredWidth(100);
                     this.getColumnModel().getColumn(4).setPreferredWidth(80);
                     this.getColumnModel().getColumn(5).setPreferredWidth(60);
                     this.getColumnModel().getColumn(6).setPreferredWidth(60);
                     this.getColumnModel().getColumn(7).setPreferredWidth(60);
                     this.getColumnModel().getColumn(8).setPreferredWidth(70);
                     
                     /**Hidden*/
                     this.getColumnModel().getColumn(9).setMinWidth(0);
                     this.getColumnModel().getColumn(9).setPreferredWidth(0);
                     
                     this.getColumnModel().getColumn(10).setPreferredWidth(70);
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


             private class PRTableModel extends AbstractTableModel{
                     private int rowCount = 0;
                     private String [] colNames = {"Part ID",//0
                                                 "Description",//1
                                                 "Brand",//2
                                                 "Supplier",//3
                                                 "Batch",//4
                                                 "Req: Qty",//5
                                                 "Used Qty",//6
                                                 "Unit",//7
                                                 "Unit Cost",//8
                                                 "PM_LOG_ID",//9 Hidden*/
                                                 "Total Cost"};//10
                     private Object [][] valueArray = null;
                     private Object [][] tempArray = null;

                     public PRTableModel() {
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
                             //Total cost calculation
                             setTotalCost(row);
                             tblReqParts.repaint();
                     }

                     public boolean isCellEditable(int r,int c){
                             return (c==6?true:false);
                     }
                     
                     private void setTotalCost(int r){
                             try{
                                     double qty = Double.parseDouble(valueArray[r][6].toString());
                                     qty = (qty<0)?0:qty;
                                     double unit_price = Double.parseDouble(valueArray[r][8].toString());
                                     valueArray[r][10] = (qty * unit_price) + "";
                             }
                             catch(Exception er){
                                     valueArray[r][10] = "0.00";
                             }
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
                             //Do not press Delete while it has no elements
                             }
                     } 

                     public void deleteAll(){
                             valueArray = new Object[0][colNames.length];
                     }
             }
     }
} 
