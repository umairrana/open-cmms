package com.matrix.focus.register;

import com.matrix.focus.mdi.MDI;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.Authorizer;
import com.matrix.focus.util.DataAssistantDialog;
import com.matrix.focus.util.ImageLibrary;
import com.matrix.focus.util.StandardTimeCell;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Savepoint;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

public class LabourRequirementDialog extends JDialog{
    private JScrollPane jScrollPane1 = new JScrollPane();
    private LabourRequirementTable tblLabour = new LabourRequirementTable();
    private JButton btnDone = new JButton(new ImageIcon(ImageLibrary.BUTTON_SAVE));
    private JButton btnRemoveL = new JButton(new ImageIcon(ImageLibrary.BUTTON_REMOVE));
    private JButton btnAddL = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));
    private LabourRequirementEntry lre;
    private String SELECTED_PM;
    private String SELECTED_ASSET;
    private MDI frame;
    private Connection connection;
    private MessageBar messageBar;
    private boolean capable;
      
    public LabourRequirementDialog(MDI frame,MessageBar msgBar, String selected_asset, String selected_pm,boolean capable,Connection connection){
        super(frame, "Labour Requirement", true);
        this.frame = frame;
        this.messageBar = msgBar;
        this.connection = connection;
        this.SELECTED_PM = selected_pm;
        this.SELECTED_ASSET = selected_asset;
        this.capable = capable;
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
        this.setSize(new Dimension(335, 340));
        this.setResizable(false);
        this.getContentPane().setLayout(null);
        jScrollPane1.setBounds(new Rectangle(10, 10, 310, 250));
        btnRemoveL.setBounds(new Rectangle(115, 270, 100, 25));
        btnRemoveL.setText("Remove");
        btnRemoveL.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnRemoveL_actionPerformed(e);
                    }
                });
        btnRemoveL.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnRemoveL_actionPerformed(e);
                    }
                });
        btnDone.setBounds(new Rectangle(220, 270, 100, 25));
        btnDone.setText("Save");
        btnDone.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnDone_actionPerformed(e);
                    }
                });
        btnDone.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnDone_actionPerformed(e);
                    }
                });
        btnAddL.setBounds(new Rectangle(10, 270, 100, 25));
        btnAddL.setText("Add");
        btnAddL.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnAddL_actionPerformed(e);
                    }
                });
        jScrollPane1.getViewport().add(tblLabour, null);
        this.getContentPane().add(btnRemoveL, null);
        this.getContentPane().add(btnAddL, null);
        this.getContentPane().add(btnDone, null);
        this.getContentPane().add(jScrollPane1, null);
    }
    
    private boolean categoryExists(String cat){
        for(int i=0;i<tblLabour.getRowCount();i++){
           if(cat.equals(tblLabour.getValueAt(i,0).toString())){
              return true;
           } 
        }
        return false;
    }
    
    private void populate(){
        String sql = "SELECT Maintenance_Category,Amount FROM labour_requirement WHERE Preventive_Maintenance_ID ='" + SELECTED_PM + "' AND asset_id = '" + SELECTED_ASSET + "';";
        tblLabour.deleteAll();
        try{
            ResultSet rec = connection.createStatement().executeQuery(sql);
            int cnt = 0;
            while(rec.next()){
                cnt = rec.getRow()-1;
                tblLabour.addRow();
                tblLabour.setValueAt(rec.getString("Maintenance_Category"),cnt,0);
                tblLabour.setValueAt(rec.getString("Amount"),cnt,1);
            }
        }
        catch(Exception er){
        }
    }
    
    private boolean allocateLabour(){
        int rows = tblLabour.getRowCount();
        boolean done = false;

        for(int j=0;j<rows;j++){
            lre = new LabourRequirementEntry(connection);
            lre.assetID = SELECTED_ASSET;
            lre.preventiveMaintenanceID = SELECTED_PM;
            lre.maintenanceCategory = tblLabour.getValueAt(j,0).toString();
            lre.amount = tblLabour.getValueAt(j,1).toString();
            if(lre.save()){
                done = true;
            }
            else if(lre.update()){
                done = true;
            }
            else{
                done = false;
                break;
            }
        }
        return done;
    }
    
    private boolean deleteLabour(){
        int rows = tblLabour.getRowCount();
        boolean done = false;
    
        for(int j=0;j<rows;j++){
            lre = new LabourRequirementEntry(connection);
            lre.assetID = SELECTED_ASSET;
            lre.preventiveMaintenanceID = SELECTED_PM;
            lre.maintenanceCategory = tblLabour.getValueAt(j,0).toString();
            if(lre.delete()){
                done = true;
            }
            else{
                done = false;
                break;
            }
        }
        return done;
    }

    private void btnAddL_actionPerformed(ActionEvent e) {
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select Skill Category - Data Assistant","SELECT Maintenance_Category as 'Skill Category'  FROM preventive_maintenance_category",connection);
        d.setFirstColumnWidth(320);
        d.setLocationRelativeTo(this);
        d.setVisible(true); 
        String rtnVal = d.getValue();
        if(!rtnVal.equals("")){
            if(!categoryExists(rtnVal)){
                tblLabour.addRow();
                tblLabour.setValueAt(rtnVal,tblLabour.getRowCount()-1,0);
                tblLabour.setValueAt("0-0-0",tblLabour.getRowCount()-1,1);
                tblLabour.repaint();
            }
            else{
                messageBar.setMessage("The category already exists.","ERROR");
            }
        } 
    }

    private void btnRemoveL_actionPerformed(ActionEvent e) {
        int row = tblLabour.getSelectedRow();
        if(row!=-1){
            if(MessageBar.showConfirmDialog(this,"Are you sure you want to remove \nthe selected category from the registration?","Romove Category")==MessageBar.YES_OPTION){
                try{
                    Savepoint sPointCategory = connection.setSavepoint("Category");
                    if(deleteLabour()){
                        tblLabour.deleteRow(tblLabour.getSelectedRow());
                        messageBar.setMessage("Selected category was removed.","OK");
                    }
                    else{
                        connection.rollback(sPointCategory);
                        messageBar.setMessage("Could not remove the selected category.","ERROR");
                    }
                }
                catch(Exception er){
                    er.printStackTrace();
                }
            }
        }
        else{
            messageBar.setMessage("Select a category first.","ERROR");
        }
    }

    private void btnDone_actionPerformed(ActionEvent e) {
        try{
            Savepoint sPointLabour = connection.setSavepoint("Labour");
            if(allocateLabour()){ 
                messageBar.setMessage("Labour Requirements for the selected maintenance was saved.","OK");
                this.setVisible(false);
            }
            else{
                connection.rollback(sPointLabour);
                messageBar.setMessage("Could not save Labour Requirements for the selected maintenance.","ERROR");
            }
        }
        catch(Exception er){
        }
    }

    public class LabourRequirementTable extends JTable {
        private LRTableModel myModel = null ;

        public LabourRequirementTable() {
            myModel = new LRTableModel();
            this.setModel(myModel);
            this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.getTableHeader().setReorderingAllowed(false);
            this.setSelectionMode(0);
            
            this.getColumnModel().getColumn(0).setPreferredWidth(240);
            this.getColumnModel().getColumn(1).setPreferredWidth(70);
            
            this.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new StandardTimeCell(frame))); 
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

        private class LRTableModel extends AbstractTableModel{
            private int rowCount = 0;
            private String [] colNames = {"Skill Category","Man Hours"};
            private Object [][] valueArray = null;
            private Object [][] tempArray = null;

            public LRTableModel() {
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
                return (c==1?true:false);
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
                    //no elements
                }
            }
            
            public void deleteAll(){
                valueArray = new Object[0][colNames.length];
            }
        }
    }
}
