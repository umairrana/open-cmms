package com.matrix.focus.workorder;

import com.matrix.focus.log.RequestedJobsPartAllocationEntry;
import com.matrix.focus.master.entity.Part;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.ImageLibrary;
import com.matrix.focus.util.LockedCheckBoxCellRenderer;
import com.matrix.focus.util.LockedLabelCellRenderer;
import com.matrix.focus.util.Validator;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Savepoint;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

public class RequsetedJobsPartAllocationDialog extends JDialog{
    private JScrollPane jScrollPane1 = new JScrollPane();
    private PartRequirementTable tblPart = new PartRequirementTable();
    private JButton btnDone = new JButton(new ImageIcon(ImageLibrary.BUTTON_OK));
    private JButton btnRemoveL = new JButton(new ImageIcon(ImageLibrary.BUTTON_REMOVE));
    private JButton btnAddL = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));
    private RequestedJobsPartAllocationEntry pre;
    private InventoryDialog dlgInventory;
    private JFrame frame;
    private Connection connection;
    private MessageBar messageBar;
    private String WORK_ORDER;
    private String ASSET;
    private String TASK_ID;
    private JPanel jPanel1 = new JPanel();
    private boolean capable;

    public RequsetedJobsPartAllocationDialog(JFrame frame, Connection con, MessageBar msgBar, boolean isCapable){
        super(frame, "Part Allocation", true);
        this.frame = frame;
        connection = con;
        messageBar = msgBar;
        capable = isCapable;
        try{
            jbInit();
            dlgInventory = new InventoryDialog(this,connection);
            dlgInventory.setLocationRelativeTo(frame);
            
            btnAddL.setEnabled(capable);
            btnRemoveL.setEnabled(capable);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void jbInit() throws Exception{
        this.setSize(new Dimension(844, 240));
        this.setResizable(false);
        this.getContentPane().setLayout(null);
        jScrollPane1.setBounds(new Rectangle(10, 20, 810, 130));
        btnRemoveL.setBounds(new Rectangle(620, 170, 100, 25));
        btnRemoveL.setText("Remove");
        btnRemoveL.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnRemoveL_actionPerformed(e);
                    }
                });
        btnDone.setBounds(new Rectangle(725, 170, 100, 25));
        btnDone.setText("OK");
        btnDone.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnDone_actionPerformed(e);
                    }
                });
        btnAddL.setBounds(new Rectangle(515, 170, 100, 25));
        btnAddL.setText("Add");
        btnAddL.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnAddL_actionPerformed(e);
                    }
                });
        jPanel1.setBounds(new Rectangle(5, 5, 830, 160));
        jPanel1.setBorder(BorderFactory.createTitledBorder("Parts Allocation"));
        jPanel1.setLayout(null);
        jScrollPane1.getViewport().add(tblPart, null);
        jPanel1.add(jScrollPane1, null);
        this.getContentPane().add(jPanel1, null);
        this.getContentPane().add(btnRemoveL, null);
        this.getContentPane().add(btnAddL, null);
        this.getContentPane().add(btnDone, null);
    }
   
    private void btnDone_actionPerformed(ActionEvent e) {
        if(capable){
            if(validatePartRequirementEntry()){
                try{
                    Savepoint sPointPart = connection.setSavepoint("Part");
                    if(allocatePart()){ 
                        messageBar.setMessage("Part Allocation for the selected maintenance was saved.","OK");
                        this.setVisible(false);
                    }
                    else{
                        connection.rollback(sPointPart);
                        messageBar.setMessage("Could not save Part Allocation for the selected maintenance.","ERROR");
                    }
                }
                catch(Exception er){
                    er.printStackTrace();
                }
            }
        }
        else{
            this.setVisible(false);
        }    
    }
    
    private void btnRemoveL_actionPerformed(ActionEvent e) {
        int row = tblPart.getSelectedRow();
        if(row!=-1){
            if(MessageBar.showConfirmDialog(this,"Are you sure you want to remove the selected part?","Part Allocation")==MessageBar.YES_OPTION){
                try{
                    Savepoint sPointPart = connection.setSavepoint("Part");
                    try{
                        deletePart();
                        tblPart.deleteRow(tblPart.getSelectedRow());
                        messageBar.setMessage("Selected part was removed.","OK");
                    } 
                    catch (Exception ex){
                        ex.printStackTrace();
                        connection.rollback(sPointPart);
                        messageBar.setMessage(ex.getMessage(),"ERROR");
                    } 
                }
                catch(Exception er){
                    er.printStackTrace();
                }
            }
        }
        else{
            messageBar.setMessage("Select a part first.","ERROR");
        }
    }
    
    private void btnAddL_actionPerformed(ActionEvent e){
         dlgInventory.populate();
         dlgInventory.setVisible(true);
         Part part = dlgInventory.getPart();
         try{
             if(part!=null){
                if(!isExistingPart(part.id)){
                     tblPart.addRow();
                     int row = tblPart.getRowCount()-1;
                     tblPart.setValueAt(part.id,row,0);
                     tblPart.setValueAt(part.description,row,1);
                     tblPart.setValueAt("0",row,2);
                     tblPart.setValueAt(part.unit,row,3);
                     tblPart.setValueAt(part.brand,row,4);
                     tblPart.setValueAt(part.supplier,row,5);
                     tblPart.setValueAt(part.batch,row,6);
                     tblPart.setValueAt(part.unit_price,row,7);
                     tblPart.setValueAt(part.needApproval,row,8);   
                     tblPart.repaint();
                }
                else{
                    messageBar.setMessage("The part you selected is already in the list.","ERROR");
                }
             }
         }
         catch(Exception er){
            er.printStackTrace();
         }
    }
    
    private boolean isExistingPart(String part_id){
        int rows = tblPart.getRowCount();
        for(int i=0;i<rows;i++){
            if(tblPart.getValueAt(i,0).toString().equals(part_id)){
                return true;
            }   
        }
        return false;
    }
    
    public void populate(String workOrder, String asset, String task){
        WORK_ORDER = workOrder;
        ASSET = asset;
        TASK_ID = task;
        String sql = "SELECT " +
                          "u.Part_ID," +
                          "p.Description, " +
                          "u.Required_Amount, " +
                          "p.unit, " +
                          "u.Brand, " +
                          "u.Supplier, " +
                          "u.Batch, " +
                          "u.Unit_Price, " +
                          "u.Authorization_Required " +
                     "FROM " +
                          "part_utilization_for_requested_maintenance u," +
                          "part p " +
                     "WHERE " +
                          "u.PM_Work_Order_ID ='" + WORK_ORDER + "' AND " +
                          "u.Asset_ID ='" + ASSET + "' AND " +
                          "u.Task_ID = '" + TASK_ID + "' AND " +
                          "u.part_id = p.part_id";
        try{
            ResultSet rec = connection.createStatement().executeQuery(sql);
            tblPart.deleteAll();
            int cnt = 0;
            while(rec.next()){
                cnt = rec.getRow()-1;
                tblPart.addRow();
                tblPart.setValueAt(rec.getString("Part_ID"),cnt,0);
                tblPart.setValueAt(rec.getString("Description"),cnt,1);
                tblPart.setValueAt(rec.getString("Required_Amount"),cnt,2);
                tblPart.setValueAt(rec.getString("Unit"),cnt,3);
                tblPart.setValueAt(rec.getString("Brand"),cnt,4);
                tblPart.setValueAt(rec.getString("Supplier"),cnt,5);
                tblPart.setValueAt(rec.getString("Batch"),cnt,6);
                tblPart.setValueAt(rec.getString("Unit_Price"),cnt,7);
                tblPart.setValueAt(rec.getBoolean("Authorization_Required"),cnt,8);
            }
        }
        catch(Exception er){
            er.printStackTrace();
        }
    }
    
    private boolean allocatePart(){
        for(int j=0;j<tblPart.getRowCount();j++){
            pre = new RequestedJobsPartAllocationEntry(connection);
            pre.pm_work_order_id = WORK_ORDER;
            pre.asset_id = ASSET;
            pre.task_id = TASK_ID;
            pre.part_id = tblPart.getValueAt(j,0).toString();
            pre.required_amount = tblPart.getValueAt(j,2).toString();
            pre.brand = tblPart.getValueAt(j,4).toString();
            pre.supplier = tblPart.getValueAt(j,5).toString();
            pre.batch = tblPart.getValueAt(j,6).toString();
            pre.unit_price = tblPart.getValueAt(j,7).toString();
            pre.authorization_required = tblPart.getValueAt(j,8).toString();
            pre.authorized = (pre.authorization_required.equals("true")?"false":"true");
            
            try{
                pre.save();
            }
            catch(Exception e){
                try{
                    pre.update();
                }
                catch(Exception er){
                    er.printStackTrace();
                    return false; 
                }
                //e.printStackTrace();
            }
        }
        return true;
    }
    private void deletePart() throws Exception{
        for(int j=0;j<tblPart.getRowCount();j++){
            pre = new RequestedJobsPartAllocationEntry(connection);
            pre.pm_work_order_id = WORK_ORDER;
            pre.asset_id = ASSET;
            pre.task_id = TASK_ID;
            pre.part_id = tblPart.getValueAt(j,0).toString();
            pre.brand = tblPart.getValueAt(j,4).toString();
            pre.supplier = tblPart.getValueAt(j,5).toString();
            pre.batch =  tblPart.getValueAt(j,6).toString();
            pre.delete();
        }
    }
        
    private boolean validatePartRequirementEntry(){
        /**Get All labour Requirements*/
        int rows = tblPart.getRowCount();
        /**Return value*/
        boolean value = false;
        String qty = "";
        for(int j=0;j<rows;j++){
            qty = tblPart.getValueAt(j,2).toString().trim();
                                    
            if(!Validator.isNonNegative(qty)){
                messageBar.setMessage("Invalid value for Required Quantity in row "+(j+1),"ERROR");
                return false;
            }
            else{
                value = true;
            }
        }
        return value;
    }

    public class PartRequirementTable extends JTable {
        private PRTableModel myModel = null ;

        public PartRequirementTable() {
            myModel = new PRTableModel();
            this.setModel(myModel);
            
            this.getTableHeader().setReorderingAllowed(false);
            this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.setSelectionMode(0);
            
            this.getColumnModel().getColumn(0).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(1).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(3).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(4).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(5).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(6).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(7).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(8).setCellRenderer(new LockedCheckBoxCellRenderer());
                        
            this.getColumnModel().getColumn(0).setPreferredWidth(100);
            this.getColumnModel().getColumn(1).setPreferredWidth(200);
            this.getColumnModel().getColumn(2).setPreferredWidth(80);
            this.getColumnModel().getColumn(3).setPreferredWidth(50);
            this.getColumnModel().getColumn(4).setPreferredWidth(80);
            this.getColumnModel().getColumn(5).setPreferredWidth(150);
            this.getColumnModel().getColumn(6).setPreferredWidth(60);
            this.getColumnModel().getColumn(7).setPreferredWidth(60);
            this.getColumnModel().getColumn(8).setPreferredWidth(80);
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
            private String [] colNames = {
                                         "Part ID",     //0
                                         "Description", //1
                                         "Req: Qty",    //2
                                         "Unit",        //3
                                         "Brand",       //4
                                         "Supplier",    //5
                                         "Batch",       //6
                                         "Unit Price",  //7
                                         "Auth: Req:"   //8
                                         };
            private Object [][] valueArray = null;
            private Object [][] tempArray = null;
      
            public PRTableModel() {
                valueArray = new Object[rowCount][colNames.length];
            }
            
            public Class getColumnClass(int c){
                if(c==8)
                    return this.getValueAt(0,c).getClass();
                else
                    return "".getClass();   
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
                return (c==2)?true:false;
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

