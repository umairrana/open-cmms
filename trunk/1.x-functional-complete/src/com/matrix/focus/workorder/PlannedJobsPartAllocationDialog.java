package com.matrix.focus.workorder;

import com.matrix.focus.log.PartUtilisationEntry;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

public class PlannedJobsPartAllocationDialog extends JDialog{
    private JScrollPane jScrollPane1 = new JScrollPane();
    private PartAllocationTable tblPart = new PartAllocationTable();
    private JButton btnDone = new JButton(new ImageIcon(ImageLibrary.BUTTON_OK));
    private PartInventoryDialog dlgInventory;
    private String PREVENTIVE_MAINTENANCE_LOG_ID;
    private JFrame frame;
    private Connection connection;
    private MessageBar messageBar;
    private JScrollPane jScrollPane2 = new JScrollPane();
    private PartRequirementTable tblPR = new PartRequirementTable();
    private JPanel jPanel1 = new JPanel();
    private JPanel jPanel2 = new JPanel();
    private JButton btnRemove = new JButton(new ImageIcon(ImageLibrary.BUTTON_REMOVE));
    private boolean capable;

    public PlannedJobsPartAllocationDialog(JFrame frame, Connection con, MessageBar msgBar, boolean isCapable){
        super(frame, "Part Allocation", true);
        this.frame = frame;
        this.connection = con;
        this.messageBar = msgBar;
        this.capable = isCapable;
        try{
            dlgInventory = new PartInventoryDialog(this,connection);
            dlgInventory.setLocationRelativeTo(frame);
            jbInit();
            
            tblPR.addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent me){
                    if(me.getButton()==1 && me.getClickCount()==2 && capable){
                        int pr_row = tblPR.getSelectedRow();
                        dlgInventory.setPart(tblPR.getValueAt(pr_row,0).toString());
                        dlgInventory.setVisible(true);
                        Part part = dlgInventory.getPart();
                        
                        try{
                            if( part!=null ){
                                if(!isExistingPart(part.id)){
                                    tblPart.addRow();
                                    int pa_row = tblPart.getRowCount()-1;
                                    tblPart.setValueAt(part.id,pa_row,0);
                                    tblPart.setValueAt(part.description,pa_row,1);
                                    tblPart.setValueAt(tblPR.getValueAt(pr_row,2).toString(),pa_row,2);
                                    tblPart.setValueAt(tblPR.getValueAt(pr_row,2).toString(),pa_row,3);
                                    tblPart.setValueAt(part.unit,pa_row,4);
                                    tblPart.setValueAt(part.brand,pa_row,5);
                                    tblPart.setValueAt(part.supplier,pa_row,6);
                                    tblPart.setValueAt(part.batch,pa_row,7);
                                    tblPart.setValueAt(part.unit_price,pa_row,8);
                                    tblPart.setValueAt(part.needApproval,pa_row,9);   
                                    tblPart.repaint();
                                }
                                else{
                                    messageBar.setMessage("The part you selected is already in the list","ERROR");
                                }
                            }
                        }
                        catch(Exception er){
                           er.printStackTrace();
                        }
                    }
                }
            });
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        /**Authorizations*/
        btnRemove.setEnabled(capable);
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
        
    private void jbInit() throws Exception{
        this.setSize(new Dimension(810, 390));
        this.setResizable(false);
        this.getContentPane().setLayout(null);
        jScrollPane1.setBounds(new Rectangle(10, 15, 770, 150));
        btnDone.setBounds(new Rectangle(695, 325, 100, 25));
        btnDone.setText("Ok");
        btnDone.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnDone_actionPerformed(e);
                    }
                });
        jScrollPane2.setBounds(new Rectangle(10, 15, 385, 110));
        jPanel1.setBounds(new Rectangle(5, 5, 405, 135));
        jPanel1.setLayout(null);
        jPanel1.setBorder(BorderFactory.createTitledBorder("Parts Requirement"));
        jPanel2.setBounds(new Rectangle(5, 145, 790, 175));
        jPanel2.setLayout(null);
        jPanel2.setBorder(BorderFactory.createTitledBorder("Parts Allocation"));
        btnRemove.setText("Remove");
        btnRemove.setBounds(new Rectangle(590, 325, 100, 25));
        btnRemove.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnRemove_actionPerformed(e);
                    }
                });
        jScrollPane2.getViewport().add(tblPR, null);
        jPanel1.add(jScrollPane2, null);
        jScrollPane1.getViewport().add(tblPart, null);
        jPanel2.add(jScrollPane1, null);
        this.getContentPane().add(btnRemove, null);
        this.getContentPane().add(jPanel2, null);
        this.getContentPane().add(jPanel1, null);
        this.getContentPane().add(btnDone, null);
    }
    
    private void btnRemove_actionPerformed(ActionEvent e){
        int row = tblPart.getSelectedRow();
        if(row!=-1){
            if(MessageBar.showConfirmDialog(frame,"Are sure you want to remove selected part?","Part Allocation")==MessageBar.YES_OPTION){
                PartUtilisationEntry pue = new PartUtilisationEntry(connection);
                pue.preventive_maintenance_log_id = PREVENTIVE_MAINTENANCE_LOG_ID;
                pue.part_id = tblPart.getValueAt(row,0).toString();
                pue.brand = tblPart.getValueAt(row,5).toString();
                pue.supplier = tblPart.getValueAt(row,6).toString(); 
                pue.batch = tblPart.getValueAt(row,7).toString();
                try{
                    pue.delete();
                    tblPart.deleteRow(row);
                    messageBar.setMessage("Part was deleted.","OK");
                }
                catch(Exception er){
                    er.printStackTrace();
                    messageBar.setMessage(er.getMessage(),"ERROR");
                }
            }
        }
        else{
            messageBar.setMessage("Select a row first.","ERROR");
        }
    }
        
    private void btnDone_actionPerformed(ActionEvent e){
        if(capable){
            if(validatePartUtilisation()){
                try{
                    Savepoint sp = connection.setSavepoint();
                    if(savePartAllocation()){
                        messageBar.setMessage("Patrs allocation saved.","OK");
                        setVisible(false);
                    }
                    else{
                        connection.rollback(sp);
                        messageBar.setMessage("Could not allocate patrs.","ERROR");
                    }
                }
                catch(Exception er){
                    er.printStackTrace();
                }
            }
        }
        else{
            setVisible(false);
        }
    }
            
    private boolean savePartAllocation(){
        PartUtilisationEntry pue;
        boolean done = false;
        int rows = tblPart.getRowCount();
            for(int i=0;i<rows;i++){
                pue = new PartUtilisationEntry(connection);
                pue.preventive_maintenance_log_id = PREVENTIVE_MAINTENANCE_LOG_ID;
                pue.part_id = tblPart.getValueAt(i,0).toString();
                pue.required_amount = tblPart.getValueAt(i,3).toString();
                pue.brand = tblPart.getValueAt(i,5).toString();
                pue.supplier = tblPart.getValueAt(i,6).toString(); 
                pue.batch = tblPart.getValueAt(i,7).toString();
                pue.unit_price = tblPart.getValueAt(i,8).toString();
                pue.authorization_required = tblPart.getValueAt(i,9).toString();
                pue.authorized = (pue.authorization_required.equals("true")?"false":"true");
                
                try{
                    pue.save();
                }
                catch (Exception e) {
                    try{
                        pue.update();
                    }
                    catch (Exception er) {
                        er.printStackTrace();
                        messageBar.setMessage(er.getMessage(),"ERROR");
                        return false; 
                        
                    }
                }
            }
        return true;
    }
            
    private boolean validatePartUtilisation(){
        int rows = tblPart.getRowCount();
        for(int i=0;i<rows;i++){
            if(Validator.isEmpty(tblPart.getValueAt(i,3).toString())){
               messageBar.setMessage("Required Quantity field is empty at row "+ (i+1) +".","ERROR");
               return false;
            } 
            else if(!Validator.isNonNegative(tblPart.getValueAt(i,3).toString())){
              messageBar.setMessage("Invalid quantity for Required Quantity at row "+ (i+1) +".","ERROR");
              return false;
            }
        }
        return true;
    }
    
    public void populate(String asset_id, String log_id, String scheduled_id, String pm_id){
        setRequirement(asset_id,pm_id);
        setParts(scheduled_id,log_id);
    }
            
    private void setParts(String scheduled_id, String log_id){
        PREVENTIVE_MAINTENANCE_LOG_ID = log_id;
        
        String sql = "SELECT pr.Part_ID, " +
                            "p.Description, " +
                            "pr.Amount," +
                            "IF(pu.Required_Amount=0,pr.Amount,pu.Required_Amount) AS Required_Amount, " +
                            "p.unit, " +
                            "pu.Brand, " +
                            "pu.Supplier," +
                            "pu.Batch," +
                            "pu.Unit_Price, " +
                            "pu.Authorization_Required " +
                     "FROM part_requirement pr," +
                          "part p, " +
                          "scheduled_preventive_maintenance spm, " +
                          "part_utilisation pu " +
                     "WHERE spm.Scheduled_ID = '" + scheduled_id + "' AND " +
                           "pr.Preventive_Maintenance_ID = spm.Preventive_Maintenance_ID AND " +
                           "pr.asset_id = spm.Asset_ID AND " +
                           "pr.part_id = p.part_id AND " +
                           "pu.Preventive_Maintenance_Log_ID ='" + PREVENTIVE_MAINTENANCE_LOG_ID + "' AND " +
                           "pr.part_id = pu.Part_ID";
        try{
            tblPart.deleteAll();
            ResultSet rec = connection.createStatement().executeQuery(sql);
            int cnt = 0;
            while(rec.next()){
                cnt = rec.getRow()-1;
                tblPart.addRow();
                tblPart.setValueAt(rec.getString("Part_ID"),cnt,0);
                tblPart.setValueAt(rec.getString("Description"),cnt,1);
                tblPart.setValueAt(rec.getString("Amount"),cnt,2);
                tblPart.setValueAt(rec.getString("Required_Amount"),cnt,3);
                tblPart.setValueAt(rec.getString("unit"),cnt,4);
                tblPart.setValueAt(rec.getString("Brand"),cnt,5);
                tblPart.setValueAt(rec.getString("Supplier"),cnt,6);
                tblPart.setValueAt(rec.getString("Batch"),cnt,7);
                tblPart.setValueAt(rec.getString("Unit_Price"),cnt,8);
                tblPart.setValueAt(rec.getBoolean("Authorization_Required"),cnt,9);
            }
        }
        catch(Exception er){
            er.printStackTrace();
        }
    }
            
    private void setRequirement(String asset_id, String pm_id){           
        String sql = "SELECT pr.Part_ID, " +
                            "p.Description, " +
                            "pr.Amount, " +
                            "p.unit " +
                     "FROM part_requirement pr," +
                          "part p  " +
                     "WHERE pr.Asset_ID ='" + asset_id + "' AND " +
                           "pr.Preventive_Maintenance_ID ='" + pm_id + "' AND " +
                           "pr.Part_ID = p.Part_ID";
        try{
            tblPR.deleteAll();
            ResultSet rec = connection.createStatement().executeQuery(sql);
            int cnt = 0;
            while(rec.next()){
                cnt = rec.getRow()-1;
                tblPR.addRow();
                tblPR.setValueAt(rec.getString("Part_ID"),cnt,0);
                tblPR.setValueAt(rec.getString("Description"),cnt,1);
                tblPR.setValueAt(rec.getString("Amount"),cnt,2);
                tblPR.setValueAt(rec.getString("Unit"),cnt,3);
            }
        }
        catch(Exception er){
            er.printStackTrace();
        }
    }

    public class PartAllocationTable extends JTable {
        private PATableModel myModel = null ;

        public PartAllocationTable() {
            myModel = new PATableModel();
            this.setModel(myModel);
            
            this.getTableHeader().setReorderingAllowed(false);
            this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.setSelectionMode(0);
            
            this.getColumnModel().getColumn(0).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(1).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(2).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(4).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(5).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(6).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(7).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(8).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(9).setCellRenderer(new LockedCheckBoxCellRenderer());
                            
            this.getColumnModel().getColumn(0).setPreferredWidth(80);
            this.getColumnModel().getColumn(1).setPreferredWidth(150);
            this.getColumnModel().getColumn(2).setPreferredWidth(60);
            this.getColumnModel().getColumn(3).setPreferredWidth(60);
            this.getColumnModel().getColumn(4).setPreferredWidth(50);
            this.getColumnModel().getColumn(5).setPreferredWidth(80);
            this.getColumnModel().getColumn(6).setPreferredWidth(150);
            this.getColumnModel().getColumn(7).setPreferredWidth(60);
            this.getColumnModel().getColumn(8).setPreferredWidth(60);
            this.getColumnModel().getColumn(9).setPreferredWidth(80);
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
                
        private class PATableModel extends AbstractTableModel{
            private int rowCount = 0;
            private String [] colNames = {"Part ID",        //0
                                          "Description",    //1
                                          "Plan: Qty",      //2
                                          "Req: Qty",       //3
                                          "Unit",           //4
                                          "Brand",          //5
                                          "Supplier",       //6
                                          "Batch",          //7
                                          "Unit Price",     //8
                                          "Auth: Req:"      //9
                                          };
            private Object [][] valueArray = null;
            private Object [][] tempArray = null;
      
            public PATableModel() {
                valueArray = new Object[rowCount][colNames.length];
            }
                    
            public Class getColumnClass(int c){
                if(c==9)
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
                return (c==3?true:false);
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
            
    public class PartRequirementTable extends JTable {
        private PRTableModel myModel = null ;
    
        public PartRequirementTable() {
            myModel = new PRTableModel();
            this.setModel(myModel);
            
            this.getTableHeader().setReorderingAllowed(false);
            this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.setSelectionMode(0);
                                        
            this.getColumnModel().getColumn(0).setPreferredWidth(80);
            this.getColumnModel().getColumn(1).setPreferredWidth(150);
            this.getColumnModel().getColumn(2).setPreferredWidth(80);
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
            private String [] colNames = {"Part ID",        //0
                                          "Description",    //1
                                          "Planned Qty",    //2
                                          "Unit"            //3
                                          };
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
