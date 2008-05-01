package com.matrix.focus.master.gui;
import com.matrix.components.TitleBar;
import com.matrix.focus.connect.SyncCustomer;
import com.matrix.focus.connect.SyncPart;
import com.matrix.focus.util.MPanel;
import com.matrix.focus.util.TaskRemarksDialog;
import com.matrix.focus.master.data.PartData;
import com.matrix.focus.master.data.PartsMasterData;
import com.matrix.focus.master.entity.Part;
import com.matrix.focus.master.entity.PartsMaster;
import com.matrix.focus.mdi.MDI;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.ButtonCellRenderer;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.DataAssistantDialog;
import com.matrix.focus.util.ImageLibrary;

import java.awt.Button;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.Date;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.DateFormatter;

public class GUIPartsMaster extends MPanel{
    
    private TitleBar titlebar = new TitleBar();
    private JPanel jPanel1 = new JPanel();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private PartsTable tblParts = new PartsTable();
    private TaskRemarksDialog dlgRemarks;
    
    private final Cursor hgCursor = new Cursor(Cursor.WAIT_CURSOR); // cursor hour glass
    private final Cursor defCursor = new Cursor(Cursor.DEFAULT_CURSOR); // cursor default


    private Connection connection;
    private JFrame frame;
    private MessageBar messageBar;
    private Vector<PartsMaster> partsMaster;
    private JButton btnAddPart = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));
    private JButton btnRemovePart = new JButton(new ImageIcon(ImageLibrary.BUTTON_REMOVE));
    private JButton btnUpdatePart = new JButton(new ImageIcon(ImageLibrary.BUTTON_SAVE));

    public GUIPartsMaster(DBConnectionPool pool, JFrame frame, MessageBar msgBar){
    
        this.connection = pool.getConnection();    
        this.frame = frame;
        this.messageBar = msgBar;
        try{
            titlebar.setTitle("Parts Master");
            titlebar.setDescription("The facility to manage parts master data.");
            titlebar.setImage(ImageLibrary.TITLE_PARTS);
        
            dlgRemarks = new TaskRemarksDialog(frame,"Remarks",true);
            dlgRemarks.setLocationRelativeTo(frame);
    
            tblParts.addMouseListener(
                new MouseAdapter(){
                    public void mouseClicked(MouseEvent me){
                        tblInventory_mouseClicked(me);
                    }
                }
            );
            jbInit();
            populatePartsTable();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void jbInit() throws Exception{
        this.setLayout(null);
        this.setSize(new Dimension(996, 596));
        titlebar.setBounds(new Rectangle(10, 10, 980, 70));
        jPanel1.setBounds(new Rectangle(10, 85, 980, 500));
        jPanel1.setBorder(BorderFactory.createTitledBorder("Parts Master"));
        jPanel1.setLayout(null);
        jScrollPane1.setBounds(new Rectangle(10, 20, 930, 435));
        btnAddPart.setBounds(new Rectangle(945, 20, 30, 30));
        btnAddPart.setHorizontalTextPosition(SwingConstants.CENTER);
        btnAddPart.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnAddPart_actionPerformed(e);
                    }
                });
        btnRemovePart.setBounds(new Rectangle(945, 55, 30, 30));
        btnRemovePart.setHorizontalTextPosition(SwingConstants.CENTER);
        btnRemovePart.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnRemovePart_actionPerformed(e);
                    }
                });
        btnUpdatePart.setBounds(new Rectangle(810, 460, 130, 30));
        btnUpdatePart.setSize(new Dimension(130, 30));
        btnUpdatePart.setText("Save");
        btnUpdatePart.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnUpdatePart_actionPerformed(e);
                    }
                });
        jPanel1.add(btnUpdatePart, null);
        jPanel1.add(btnRemovePart, null);
        jPanel1.add(btnAddPart, null);
        jScrollPane1.getViewport().add(tblParts, null);
        jPanel1.add(jScrollPane1, null);
        this.add(jPanel1, null);
        this.add(titlebar, null);
    }
    
    public void close(){
        try{
            connection.close();
        }
        catch(SQLException e) {
        }
    }
    
    private void populatePartsTable(){
        try{
            partsMaster =  PartsMasterData.getParts(connection);
            tblParts.deleteAll();
            int size = partsMaster.size();
            PartsMaster part = null;
            for(int i=0;i<size;i++){
                tblParts.addRow();
                part = partsMaster.get(i);
                tblParts.setValueAt(part.partId,i,0);
                tblParts.setValueAt(part.description,i,1);
                tblParts.setValueAt((part.authorization.equals("true")?true:false),i,2);
                tblParts.setValueAt(part.remarks,i,3);
                tblParts.setValueAt(part.creator,i,4);
                tblParts.setValueAt(part.createdDate,i,5);
                tblParts.setValueAt(part.lastUpdater,i,6);
                tblParts.setValueAt(part.updatedDate,i,7);
            }
        } 
        catch (Exception e) {
            messageBar.setMessage(e.getMessage(),"ERROR");
        }
    }
    
    private void tblInventory_mouseClicked(MouseEvent me){
        if(me.getButton()==1 && tblParts.getSelectedColumn()==3){
            int row = tblParts.getSelectedRow();
            dlgRemarks.setText(tblParts.getValueAt(row,3).toString());
            dlgRemarks.setVisible(true);
            String rtnValue = dlgRemarks.getText();
            if(!rtnValue.equals("null")){
                try{
                    tblParts.setValueAt(dlgRemarks.getText(),row,3);
                    tblParts.repaint();
                    PartsMaster part = partsMaster.get(row);
                    part.remarks = tblParts.getValueAt(row,3).toString();
                    PartsMasterData.updatePartRemarks(part,connection);
                }
                catch(Exception e){
                    messageBar.setMessage(e.getMessage(),"ERROR");
                }
            }
        }
    }

    private void btnAddPart_actionPerformed(ActionEvent e) {
        DLGAddPart d = new DLGAddPart(frame, "Add Part", true);
        d.setLocationRelativeTo(frame);
        d.setVisible(true); 
        PartsMaster part = d.getValue();
        if(part!=null){
            try {
                PartsMasterData.insertPart(part, connection);
                populatePartsTable();
            } catch (Exception f) {
                messageBar.setMessage(f.getMessage(),"ERROR");
            }
        }
    }

    private void btnRemovePart_actionPerformed(ActionEvent e) {
        int row = tblParts.getSelectedRow();
        if(row!=-1){
            if(MessageBar.showConfirmDialog(frame,"Are you sure you want to remove the selected part?","Parts Master")==MessageBar.YES_OPTION){
                try {
                    PartsMasterData.deletePart((String)tblParts.getValueAt(tblParts.getSelectedRow(),0), connection);
                    tblParts.deleteRow(tblParts.getSelectedRow());
                    populatePartsTable();
                } catch (Exception f) {
                     messageBar.setMessage(f.getMessage(),"ERROR");
                }                
            }
        }
        else{
            messageBar.setMessage("Please select a part first.","ERROR");
        }
    }

    private void btnUpdatePart_actionPerformed(ActionEvent e) {
        if(MessageBar.showConfirmDialog(frame,"Are you sure you want to update parts?","Parts Master")==MessageBar.YES_OPTION){
            try {
                PartsMaster part = new PartsMaster();
                for (int i = 0; i < tblParts.getRowCount(); i++)  {
                    part.setPartId((String)tblParts.getValueAt(i,0));
                    part.setAuthorization(tblParts.getValueAt(i,2).toString()); 
                    part.setRemarks((String)tblParts.getValueAt(i,3));
                    part.setLastUpdater(MDI.USERNAME);
                    PartsMasterData.updatePartRemarksAndAuthorization(part, connection);                    
                }                       
                populatePartsTable();
                messageBar.setMessage("Parts successfully updated!","OK");
            } catch (Exception f) {
                 messageBar.setMessage(f.getMessage(),"ERROR");
            }                
        }  
    }
    
    public class PartsTable extends JTable {
        private PartsTableModel myModel = null ;

        public PartsTable() {
            myModel = new PartsTableModel();
            this.setModel(myModel);
            
            this.getColumnModel().getColumn(0).setPreferredWidth(80);
            this.getColumnModel().getColumn(1).setPreferredWidth(190);
            this.getColumnModel().getColumn(2).setPreferredWidth(80);
            this.getColumnModel().getColumn(3).setPreferredWidth(100);
            this.getColumnModel().getColumn(4).setPreferredWidth(80);
            this.getColumnModel().getColumn(5).setPreferredWidth(110);
            this.getColumnModel().getColumn(6).setPreferredWidth(80);
            this.getColumnModel().getColumn(7).setPreferredWidth(110);
             
            this.getColumnModel().getColumn(3).setCellRenderer(new ButtonCellRenderer());
            
            this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.getTableHeader().setReorderingAllowed(false);
            this.setSelectionMode(0);
        }
            
        public void addRow(){
            myModel.addRow();
            this.tableChanged(new TableModelEvent(myModel)); 
        }
        
        public void deleteRow(int i){
            myModel.deleteRow(i);
            this.tableChanged(new TableModelEvent(myModel));        
        } 
        
        public void deleteAll(){
            myModel.deleteAll();
            this.tableChanged(new TableModelEvent(myModel));        
        }

        private class PartsTableModel extends AbstractTableModel{
            private String [] colNames = {"Part ID",        //0
                                          "Description",    //1  
                                          "Authorization",  //2
                                          "Remarks",        //3  
                                          "Creator",        //4
                                          "Created Date",   //5  
                                          "Last Updater",   //6
                                          "Updated Date"    //7 
                                          };
            private Object [][] valueArray = null;
            private Object [][] tempArray = null;


            public PartsTableModel() {
                valueArray = new Object[0][colNames.length];
            }
            
            public Class getColumnClass(int c){
                if(c==2)
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
                if(c==2 || c==3){
                    return true;
                }      
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
