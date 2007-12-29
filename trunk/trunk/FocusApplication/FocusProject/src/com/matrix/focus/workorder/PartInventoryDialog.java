package com.matrix.focus.workorder;

import com.matrix.focus.master.entity.Part;
import com.matrix.focus.util.ImageLibrary;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

public class PartInventoryDialog extends JDialog {
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JButton btnCancel = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
    private Connection connection;
    private PartsInventoryTable tblParts = new PartsInventoryTable();
    private Part part;
    private String part_id;
    private boolean part_selected;
    
    public PartInventoryDialog(JDialog parent,Connection con) {
        this(parent, "", false);
        connection = con;
    }

    public PartInventoryDialog(JDialog parent, String title, boolean modal) {
        super(parent, title, modal);
        try {
            jbInit();
            tblParts.addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent me){
                    int row = tblParts.getSelectedRow();
                    if(me.getButton() == 1 && me.getClickCount() ==2){
                        part = new Part();
                        part.id = part_id;
                        part.description = tblParts.getValueAt(row,0).toString();
                        part.brand = tblParts.getValueAt(row,1).toString();
                        part.supplier = tblParts.getValueAt(row,2).toString();
                        part.batch = tblParts.getValueAt(row,3).toString();
                        part.unit_price = tblParts.getValueAt(row,4).toString();
                        part.unit = tblParts.getValueAt(row,5).toString();
                        part.needApproval = Boolean.parseBoolean(tblParts.getValueAt(row,6).toString());
                        part.qty_at_hand = tblParts.getValueAt(row,7).toString();  
                        part_selected = true;
                        PartInventoryDialog.this.setVisible(false);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setPart(String part_id){  
        this.part_id = part_id;
        this.setTitle("Parts Inventory for " + this.part_id);
        part_selected = false;
        /**
        Fields list in the excel file
        -----------------------------
        Part_ID
        Description     
        Brand   
        Supplier        
        Batch   
        Unit_Price      
        Unit    
        Authorization      
        Qty_At_Hand
        -----------------------------
        */
        try  {
            String sql = "SELECT * FROM Part WHERE Part_ID = '" + part_id + "'";
            ResultSet rec = connection.createStatement().executeQuery(sql);
            tblParts.deleteAll();
            int row = -1;
            while(rec.next()){
                tblParts.addRow();
                row = tblParts.getRowCount()-1;
                tblParts.setValueAt(rec.getString("Description"),row,0);
                tblParts.setValueAt(rec.getString("Brand"),row,1);
                tblParts.setValueAt(rec.getString("Supplier"),row,2);
                tblParts.setValueAt(rec.getString("Batch"),row,3);
                tblParts.setValueAt(rec.getString("Unit_Price"),row,4);
                tblParts.setValueAt(rec.getString("Unit"),row,5);
                tblParts.setValueAt((rec.getString("Authorization").equals("YES")?true:false),row,6);
                tblParts.setValueAt(rec.getString("Qty_At_Hand"),row,7);
                tblParts.setValueAt(getAllocatedAmount(part_id,
                                                      tblParts.getValueAt(row,1).toString(),
                                                      tblParts.getValueAt(row,2).toString(),
                                                      tblParts.getValueAt(row,3).toString()
                                                      ),row,8);
                tblParts.setValueAt(getAvailableQty(row),row,9);
            }
        } 
        catch (Exception ex)  {
            ex.printStackTrace();
        }
        
    }
    
    private String getAvailableQty(int row){
        double at_h = Double.parseDouble(tblParts.getValueAt(row,7).toString());
        double used = Double.parseDouble(tblParts.getValueAt(row,8).toString());
        if(at_h>=used){
            return (at_h - used) + "";
        }
        else{
            return "0";
        }
    }
    
    private String getAllocatedAmount(String pid, String brand, String supplier, String batch){
        String sql = "SELECT tmp.Used_Amount " +
                     "FROM " +
                        "(" +
                            "(" +
                                "SELECT " +
                                        "pmpart.Required_Amount AS Used_Amount " +
                                  "FROM " +
                                        "part_utilisation pmpart, " +
                                        "preventive_maintenance_log pmlog, " +
                                        "preventive_maintenance_work_order pmwo " +
                                  "WHERE " +
                                        "pmpart.Part_ID = '" + pid + "' AND " +
                                        "pmpart.Brand = '" + brand + "' AND " +
                                        "pmpart.Supplier = '" + supplier + "' AND " +
                                        "pmpart.Batch = '" + batch + "' AND " +
                                        "pmlog.Preventive_Maintenance_Log_ID = pmpart.Preventive_Maintenance_Log_ID AND " +
                                        "pmlog.PM_Work_Order_ID = pmwo.PM_Work_Order_ID AND " +
                                        "pmwo.Process_Completed = 'false'" +
                            ") " +
                        "UNION " +
                            "(" +
                                  "SELECT " +
                                        "rmpart.Required_Amount AS Used_Amount " +
                                  "FROM " +
                                        "part_utilization_for_requested_maintenance rmpart, " +
                                        "preventive_maintenance_work_order rmlog " +
                                  "WHERE " +
                                        "rmpart.Part_ID = '" + pid + "' AND " +
                                        "rmpart.Brand = '" + brand + "' AND " +
                                        "rmpart.Supplier = '" + supplier + "' AND " +
                                        "rmpart.Batch = '" + batch + "' AND " +
                                        "rmpart.PM_Work_Order_ID = rmlog.PM_Work_Order_ID AND " +
                                        "rmlog.Process_Completed = 'false'" +
                            ") " +
                        "UNION " +
                            "(" +
                                  "SELECT " +
                                        "uppart.Allocated_Amount AS Used_Amount " +
                                  "FROM " +
                                        "part_utilization_for_up_mnt uppart, " +
                                        "unplanned_maintenance_log uplog " +
                                  "WHERE " +
                                        "uppart.Part_ID = '" + pid + "' AND " + 
                                        "uppart.Brand = '" + brand + "' AND " +
                                        "uppart.Supplier = '" + supplier + "' AND " +
                                        "uppart.Batch = '" + batch + "' AND " +
                                        "uppart.UPM_Log_ID = uplog.Unplanned_Maintenance_Log_ID AND " +
                                        "uplog.Process_Completed = 'false'" +
                            ")" +
                        ") tmp ";
        try {
            ResultSet rec = connection.createStatement().executeQuery(sql);
            rec.next();
            return rec.getString("Used_Amount");
        } catch (Exception e) {
            //e.printStackTrace();
            return "0";
        }
    }
    
    
    public Part getPart(){
        if(part_selected){
            return part;
        }
        else{
            return null;
        }
    }
    
    private void jbInit() throws Exception {
        this.setSize(new Dimension(816, 218));
        this.setResizable(false);
        this.getContentPane().setLayout( null );
        this.setTitle("Parts Inventory");
        this.setModal(true);
        jScrollPane1.setBounds(new Rectangle(5, 5, 795, 140));
        btnCancel.setText("Close");
        btnCancel.setBounds(new Rectangle(715, 150, 85, 25));
        btnCancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        jButton1_actionPerformed(e);
                    }
                });
        jScrollPane1.getViewport().add(tblParts, null);
        this.getContentPane().add(btnCancel, null);
        this.getContentPane().add(jScrollPane1, null);
    }

    private void jButton1_actionPerformed(ActionEvent e) {
        setVisible(false);
    }
    
    public class PartsInventoryTable extends JTable {
        private PartsInventoryTableModel myModel = null ;

        public PartsInventoryTable() {
            myModel = new PartsInventoryTableModel();
            this.setModel(myModel);                
            this.getColumnModel().getColumn(0).setPreferredWidth(150);
            this.getColumnModel().getColumn(1).setPreferredWidth(100);
            this.getColumnModel().getColumn(2).setPreferredWidth(100);
            this.getColumnModel().getColumn(3).setPreferredWidth(80);
            this.getColumnModel().getColumn(4).setPreferredWidth(100);
            this.getColumnModel().getColumn(5).setPreferredWidth(50);
            this.getColumnModel().getColumn(6).setPreferredWidth(80);           
            this.getColumnModel().getColumn(7).setPreferredWidth(70);
            this.getColumnModel().getColumn(8).setPreferredWidth(50);
            this.getColumnModel().getColumn(9).setPreferredWidth(50);
            
            this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.getTableHeader().setReorderingAllowed(false);
            this.setSelectionMode(0);
        }
            
        public void addRow(){
            myModel.addRow();
            this.tableChanged(new TableModelEvent(myModel)); 
        }

        public void deleteRow(){
            myModel.deleteRow();
            this.tableChanged(new TableModelEvent(myModel));        
        }
        
        public void deleteAll(){
            myModel.deleteAll();
            this.tableChanged(new TableModelEvent(myModel));        
        }

        private class PartsInventoryTableModel extends AbstractTableModel{
            private int rowCount = 0;
            private String [] colNames = {"Description",     
                                          "Brand",   
                                          "Supplier",        
                                          "Batch",   
                                          "Unit Price",      
                                          "Unit",    
                                          "Authorization",      
                                          "Qty At Hand",
                                          "Allocated",
                                          "Available"
                                          };
            private Object [][] valueArray = null;
            private Object [][] tempArray = null;


            public PartsInventoryTableModel() {
                valueArray = new Object[rowCount][colNames.length];
            }
            
            public Class getColumnClass(int c){
                if(c==6)
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

            public void deleteRow(){
                tempArray = new Object[this.getRowCount()-1][this.getColumnCount()];
                for(int y=0 ; y<tempArray.length; y++){
                    for(int x=0; x<tempArray[0].length; x++){
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
