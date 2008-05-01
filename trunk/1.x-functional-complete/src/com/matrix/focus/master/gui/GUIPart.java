package com.matrix.focus.master.gui;

import com.matrix.components.TitleBar;
import com.matrix.focus.connect.SyncCustomer;
import com.matrix.focus.connect.SyncPart;
import com.matrix.focus.util.MPanel;
import com.matrix.focus.util.TaskRemarksDialog;
import com.matrix.focus.master.data.PartData;
import com.matrix.focus.master.entity.Part;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.ButtonCellRenderer;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.ImageLibrary;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

public class GUIPart extends MPanel{
    
    private TitleBar titlebar = new TitleBar();
    private JPanel jPanel1 = new JPanel();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private PartsInventoryTable tblInventory = new PartsInventoryTable();
    private TaskRemarksDialog dlgRemarks;
    
    private final Cursor hgCursor = new Cursor(Cursor.WAIT_CURSOR); // cursor hour glass
    private final Cursor defCursor = new Cursor(Cursor.DEFAULT_CURSOR); // cursor default


    private Connection connection;
    private JFrame frame;
    private MessageBar messageBar;
    private Vector<Part> parts;
    private JButton btnMasterRefresh = new JButton(new 
ImageIcon(ImageLibrary.BUTTON_REFRESH));

    public GUIPart(DBConnectionPool pool, JFrame frame, MessageBar msgBar){
    
        this.connection = pool.getConnection();    
        this.frame = frame;
        this.messageBar = msgBar;
        try{
            titlebar.setTitle("Parts Inventory");
            titlebar.setDescription("The facility to manage parts inventory.");
            titlebar.setImage(ImageLibrary.TITLE_PARTS);
        
            dlgRemarks = new TaskRemarksDialog(frame,"Remarks",true);
            dlgRemarks.setLocationRelativeTo(frame);
    
            tblInventory.addMouseListener(
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
        jPanel1.setBounds(new Rectangle(10, 85, 980, 465));
        jPanel1.setBorder(BorderFactory.createTitledBorder("Parts Inventory"));
        jPanel1.setLayout(null);
        jScrollPane1.setBounds(new Rectangle(10, 20, 930, 435));
        btnMasterRefresh.setBounds(new Rectangle(955, 105, 30, 25));
        btnMasterRefresh.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnMasterRefresh_actionPerformed(e);
                    }
                });
        jScrollPane1.getViewport().add(tblInventory, null);
        jPanel1.add(jScrollPane1, null);
        this.add(btnMasterRefresh, null);
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
            parts =  PartData.getParts(connection);
            tblInventory.deleteAll();
            int size = parts.size();
            Part part = null;
            for(int i=0;i<size;i++){
                tblInventory.addRow();
                part = parts.get(i);
                tblInventory.setValueAt(part.id,i,0);
                tblInventory.setValueAt(part.description,i,1);
                tblInventory.setValueAt(part.brand,i,2);
                tblInventory.setValueAt(part.supplier,i,3);
                tblInventory.setValueAt(part.batch,i,4);
                tblInventory.setValueAt(part.unit_price,i,5);
                tblInventory.setValueAt(part.unit,i,6);
                tblInventory.setValueAt(part.needApproval,i,7);
                tblInventory.setValueAt(part.qty_at_hand,i,8);
                tblInventory.setValueAt(part.allocated_qty,i,9);
                tblInventory.setValueAt(part.available_qty,i,10);
                tblInventory.setValueAt(part.remarks,i,11);
            }
        } 
        catch (Exception e) {
            messageBar.setMessage(e.getMessage(),"ERROR");
        }
    }
    
    private void tblInventory_mouseClicked(MouseEvent me){
        if(me.getButton()==1 && tblInventory.getSelectedColumn()==11){
            int row = tblInventory.getSelectedRow();
            dlgRemarks.setText(tblInventory.getValueAt(row,11).toString());
            dlgRemarks.setVisible(true);
            String rtnValue = dlgRemarks.getText();
            if(!rtnValue.equals("null")){
                try{
                    tblInventory.setValueAt(dlgRemarks.getText(),row,11);
                    tblInventory.repaint();
                    Part part = parts.get(row);
                    part.remarks = tblInventory.getValueAt(row,11).toString();
                    PartData.updatePartRemarks(part,connection);
                }
                catch(Exception e){
                    messageBar.setMessage(e.getMessage(),"ERROR");
                }
            }
        }
    }

    private void btnMasterRefresh_actionPerformed(ActionEvent e) {
        try {
            SyncPart syncPart =new SyncPart(connection,messageBar);            
            frame.setCursor(hgCursor);
                syncPart.sync();
                populatePartsTable();
            frame.setCursor(defCursor);
        }
        catch(Exception ex){
             messageBar.setMessage(ex.getMessage(),"ERROR");
        }
    }

    public class PartsInventoryTable extends JTable {
        private PartsInventoryTableModel myModel = null ;

        public PartsInventoryTable() {
            myModel = new PartsInventoryTableModel();
            this.setModel(myModel);
            
            this.getColumnModel().getColumn(0).setPreferredWidth(80);
            this.getColumnModel().getColumn(1).setPreferredWidth(190);
            this.getColumnModel().getColumn(2).setPreferredWidth(100);
            this.getColumnModel().getColumn(3).setPreferredWidth(100);
            this.getColumnModel().getColumn(4).setPreferredWidth(80);
            this.getColumnModel().getColumn(5).setPreferredWidth(60);
            this.getColumnModel().getColumn(6).setPreferredWidth(30);
            this.getColumnModel().getColumn(7).setPreferredWidth(70);
            this.getColumnModel().getColumn(8).setPreferredWidth(70);
            this.getColumnModel().getColumn(9).setPreferredWidth(50);
            this.getColumnModel().getColumn(10).setPreferredWidth(50);
            this.getColumnModel().getColumn(11).setPreferredWidth(50);
            
            this.getColumnModel().getColumn(11).setCellRenderer(new ButtonCellRenderer());
            
            this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.getTableHeader().setReorderingAllowed(false);
            this.setSelectionMode(0);
        }
            
        public void addRow(){
            myModel.addRow();
            this.tableChanged(new TableModelEvent(myModel)); 
        }
        
        public void deleteAll(){
            myModel.deleteAll();
            this.tableChanged(new TableModelEvent(myModel));        
        }

        private class PartsInventoryTableModel extends AbstractTableModel{
            private String [] colNames = {"Part ID",        //0
                                          "Description",    //1  
                                          "Brand",          //2
                                          "Supplier",       //3  
                                          "Batch",          //4
                                          "Unit Price",     //5  
                                          "Unit",           //6
                                          "Authorization",  //7     
                                          "Qty At Hand",    //8
                                          "Allocated",      //9
                                          "Available",      //10
                                          "Remarks"         //11
                                          };
            private Object [][] valueArray = null;
            private Object [][] tempArray = null;


            public PartsInventoryTableModel() {
                valueArray = new Object[0][colNames.length];
            }
            
            public Class getColumnClass(int c){
                if(c==7)
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

            public void deleteAll(){
                valueArray = new Object[0][colNames.length];
            }
        }
    }
    
}
