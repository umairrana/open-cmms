package com.matrix.focus.register;

import com.matrix.focus.mdi.MDI;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.Authorizer;
import com.matrix.focus.util.DataAssistantDialog;
import com.matrix.focus.util.ImageLibrary;
import com.matrix.focus.util.Validator;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Savepoint;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

public class PartRequirementDialog extends JDialog implements ActionListener{
    private JScrollPane jScrollPane1 = new JScrollPane();
    private PartRequirementTable tblPart = new PartRequirementTable();
    private JButton btnDone = new JButton(new ImageIcon(ImageLibrary.BUTTON_SAVE));
    private JButton btnRemoveL = new JButton(new ImageIcon(ImageLibrary.BUTTON_REMOVE));
    private JButton btnAddL = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));
    private PartRequirementEntry pre;
    private String SELECTED_PM;
    private String SELECTED_ASSET;
    private MDI frame;
    private Connection connection;
    private MessageBar messageBar;

    public PartRequirementDialog(MDI frame,MessageBar msgBar, String selected_asset, String selected_pm,boolean capable,Connection connection){
        super(frame, "Part Requirement", true);
        this.frame = frame;
        this.messageBar = msgBar;
        this.connection = connection;
        this.SELECTED_PM = selected_pm;
        this.SELECTED_ASSET = selected_asset;
        try{
            jbInit();
            btnDone.setEnabled(capable);
            btnAddL.setEnabled(capable);
            btnRemoveL.setEnabled(capable);
            populate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception{
        this.setSize(new Dimension(385, 340));
        this.setResizable(false);
        this.getContentPane().setLayout(null);
        jScrollPane1.setBounds(new Rectangle(10, 10, 360, 250));
        btnRemoveL.setBounds(new Rectangle(165, 270, 100, 25));
        btnRemoveL.setText("Remove");
        btnRemoveL.addActionListener(this);
        btnDone.setBounds(new Rectangle(270, 270, 100, 25));
        btnDone.setText("Save");
        btnDone.addActionListener(this);
        btnAddL.setBounds(new Rectangle(60, 270, 100, 25));
        btnAddL.setText("Add");
        btnAddL.addActionListener(this);
        jScrollPane1.getViewport().add(tblPart, null);
        this.getContentPane().add(btnRemoveL, null);
        this.getContentPane().add(btnAddL, null);
        this.getContentPane().add(btnDone, null);
        this.getContentPane().add(jScrollPane1, null);
    }

    public void actionPerformed(ActionEvent e){
        Object click = e.getSource();
        if(click==btnAddL){
            addPart();
        }
        else if(click==btnRemoveL){
            LabourRequirementEntry lre = new LabourRequirementEntry(connection);
            int row = tblPart.getSelectedRow();
            if(row!=-1){
                if(MessageBar.showConfirmDialog(this,"Are you sure you want to remove \nthe selected part from the registration?","Romove Part")==MessageBar.YES_OPTION){
                    try{
                        Savepoint sPointPart = connection.setSavepoint("Part");
                        if(deletePart()){
                            tblPart.deleteRow(tblPart.getSelectedRow());
                            messageBar.setMessage("Selected part was removed.","OK");
                        }
                        else{
                            connection.rollback(sPointPart);
                            messageBar.setMessage("Could not remove the selected part.","ERROR");
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
        else if(click==btnDone){             
            if(validatePartRequirementEntry()){
                try{
                    Savepoint sPointPart= connection.setSavepoint("Part");
                    if(allocatePart()){ 
                        messageBar.setMessage("Part Requirements for the selected maintenance was saved.","OK");
                        this.setVisible(false);
                    }
                    else{
                        connection.rollback(sPointPart);
                        messageBar.setMessage("Could not save Part Requirements for the selected maintenance.","ERROR");
                    }
                }
                catch(Exception er){
                    er.printStackTrace();
                }
            }
        }
    }
    
    private void addPart(){
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select Part - Data Assistant","SELECT part_id as 'Part ID', description as Descripion, Unit  FROM part",connection);
        d.setFirstColumnWidth(80);
        d.setSecondColumnWidth(200);
        d.setThirdColumnWidth(30);
        d.setLocationRelativeTo(this);
        d.setVisible(true); 
        String rtnVal_ID = d.getValue();
        if(!rtnVal_ID.equals("")){
            if(!partExists(rtnVal_ID)){
                tblPart.addRow();
                int r = tblPart.getRowCount()-1;
                tblPart.setValueAt(rtnVal_ID,r,0);
                tblPart.setValueAt(d.getDescription(),r,1);
                tblPart.setValueAt(d.getThirdValue(),r,3);
            }
            else{
                messageBar.setMessage("The part already exists.","ERROR");
            }
        }            
    }
    
    private boolean partExists(String part){
        for(int i=0;i<tblPart.getRowCount();i++){
           if(part.equals(tblPart.getValueAt(i,0).toString())){
              return true;
           } 
        }
        return false;
    }
    
    private void populate(){
        String sql = "SELECT pr.Part_ID,p.Description, pr.Amount, p.Unit FROM part_requirement pr,part p WHERE Preventive_Maintenance_ID ='" + SELECTED_PM + "' AND asset_id = '" + SELECTED_ASSET + "' AND pr.part_id = p.part_id;";
        try{
            ResultSet rec = connection.createStatement().executeQuery(sql);
            tblPart.deleteAll();
            int cnt = 0;
            while(rec.next()){
                cnt = rec.getRow()-1;
                tblPart.addRow();
                tblPart.setValueAt(rec.getString("Part_ID"),cnt,0);
                tblPart.setValueAt(rec.getString("Description"),cnt,1);
                tblPart.setValueAt(rec.getString("Amount"),cnt,2);
                tblPart.setValueAt(rec.getString("Unit"),cnt,3);
            }
        }
        catch(Exception er){
            er.printStackTrace();
        }
    }
    private boolean allocatePart(){
        int rows = tblPart.getRowCount();
        boolean done = false;
    
        for(int j=0;j<rows;j++){
            pre = new PartRequirementEntry(connection);
            pre.assetID = SELECTED_ASSET;
            pre.preventiveMaintenanceID = SELECTED_PM;
            pre.partID = tblPart.getValueAt(j,0).toString();
            pre.amount = tblPart.getValueAt(j,2).toString();
            if(pre.save()){
                done = true;
            }
            else if(pre.update()){
                done = true;
            }
            else{
                done = false;
                break;
            }
        }
        return done;
    }
    private boolean deletePart(){
        int rows = tblPart.getRowCount();
        boolean done = false;
    
        for(int j=0;j<rows;j++){
            pre = new PartRequirementEntry(connection);
            pre.assetID = SELECTED_ASSET;
            pre.preventiveMaintenanceID = SELECTED_PM;
            pre.partID = tblPart.getValueAt(j,0).toString();
            
            if(pre.delete()){
                done = true;
            }
            else{
                done = false;
                break;
            }
        }
        return done;
    }
    
    private boolean validatePartRequirementEntry(){
        /**Get All labour Requirements*/
        int rows = tblPart.getRowCount();
        /**Return value*/
        boolean value = false;
        String part = "";
        String qty = "";
        for(int j=0;j<rows;j++){
            part = tblPart.getValueAt(j,0).toString().trim();
            qty = tblPart.getValueAt(j,2).toString().trim();
                                    
            if(!validateForEmpty(part)){
                messageBar.setMessage("Part ID field is empty in row "+(j+1),"ERROR");
                return false;
            }
            else if(!validateForNumber(qty)){
                messageBar.setMessage("Invalid value for Quantity in row "+(j+1),"ERROR");
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

    public class PartRequirementTable extends JTable {
        private PRTableModel myModel = null ;

        public PartRequirementTable() {
            myModel = new PRTableModel();
            this.setModel(myModel);
            this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.getTableHeader().setReorderingAllowed(false);
            this.setSelectionMode(0);
    
            this.getColumnModel().getColumn(0).setPreferredWidth(70);
            this.getColumnModel().getColumn(1).setPreferredWidth(190);
            this.getColumnModel().getColumn(2).setPreferredWidth(50);
            this.getColumnModel().getColumn(3).setPreferredWidth(50);
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
            private String [] colNames = {"Part ID","Description","Quantity","Unit"};
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
                return (c==2);
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
