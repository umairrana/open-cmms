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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

public class InventoryDialog extends JDialog {
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JButton btnCancel = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
    private Connection connection;
    private PartsInventoryTable tblParts = new PartsInventoryTable();
    private Part part;
    private boolean part_selected;

    public InventoryDialog(JDialog parent,Connection con) {
        this(parent, "", true);
        this.connection = con;
    }

    public InventoryDialog(JDialog parent, String title, boolean modal) {
        super(parent, title, modal);
        this.setTitle("Parts Inventory");
        try {
            jbInit();
            tblParts.addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent me){
                    int row = tblParts.getSelectedRow();
                    if(me.getButton() == 1 && me.getClickCount() ==2){
                        part = new Part();
                        part.id = tblParts.getValueAt(row,0).toString();
                        part.description = tblParts.getValueAt(row,1).toString();
                        part.brand = tblParts.getValueAt(row,2).toString();
                        part.supplier = tblParts.getValueAt(row,3).toString();
                        part.batch = tblParts.getValueAt(row,4).toString();
                        part.unit_price = tblParts.getValueAt(row,5).toString();
                        part.unit = tblParts.getValueAt(row,6).toString();
                        part.needApproval = Boolean.parseBoolean(tblParts.getValueAt(row,7).toString());
                        part.qty_at_hand = tblParts.getValueAt(row,8).toString();
                        part_selected = true;
                        setVisible(false);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void populate(){  
        part_selected = false;
        try {
            String sql =    "SELECT " +
                                "p.Part_ID, " +
                                "p.Description, " +
                                "p.Brand, " +
                                "p.Supplier, " +
                                "p.Batch, " +
                                "p.Unit_Price, " +
                                "p.Unit, " +
                                "p.Authorization, " +
                                "p.Qty_At_Hand " +
                            "FROM " +
                                "Part p";
                      
            ResultSet rec = connection.createStatement().executeQuery(sql);
            tblParts.deleteAll();
            int row = -1;
            while(rec.next()){
                tblParts.addRow();
                row = tblParts.getRowCount()-1;
                tblParts.setValueAt(rec.getString("Part_ID"),row,0);
                tblParts.setValueAt(rec.getString("Description"),row,1);
                tblParts.setValueAt(rec.getString("Brand"),row,2);
                tblParts.setValueAt(rec.getString("Supplier"),row,3);
                tblParts.setValueAt(rec.getString("Batch"),row,4);
                tblParts.setValueAt(rec.getString("Unit_Price"),row,5);
                tblParts.setValueAt(rec.getString("Unit"),row,6);
                tblParts.setValueAt((rec.getString("Authorization").equals("YES")?true:false),row,7);
                tblParts.setValueAt(rec.getString("Qty_At_Hand"),row,8);
                tblParts.setValueAt(getAllocatedAmount(tblParts.getValueAt(row,0).toString(),
                                                      tblParts.getValueAt(row,2).toString(),
                                                      tblParts.getValueAt(row,3).toString(),
                                                      tblParts.getValueAt(row,4).toString()
                                                      ),row,9);
                tblParts.setValueAt(getAvailableQty(row),row,10);
            }
        } 
        catch (Exception ex)  {
            ex.printStackTrace();
        }
        
    }
    
    private String getAvailableQty(int row){
        double at_h = Double.parseDouble(tblParts.getValueAt(row,8).toString());
        double used = Double.parseDouble(tblParts.getValueAt(row,9).toString());
        if(at_h>=used){
            return (at_h - used) + "";
        }
        else{
            return "0";
        }
    }
    
    private String getAllocatedAmount(String pid, String brand, String supplier, String batch){
        /*
        String sql = "CALL get_part_allocated_amount(?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,pid);
            stmt.setString(2,brand);
            stmt.setString(3,supplier);
            stmt.setString(4,batch);
            
            ResultSet rec = connection.createStatement().executeQuery(sql);
            rec.next();
            return rec.getString(1);
        } catch (Exception e) {
            e.printStackTrace();
            Log.log(e);
            return "0";
        }
        */
        return "000";
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
        this.setSize(new Dimension(929, 214));
        this.setResizable(false);
        this.getContentPane().setLayout( null );
        this.setTitle("Parts Inventory");
        jScrollPane1.setBounds(new Rectangle(5, 5, 910, 140));
        btnCancel.setText("Close");
        btnCancel.setBounds(new Rectangle(830, 150, 85, 25));
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
            this.getColumnModel().getColumn(0).setPreferredWidth(80);
            this.getColumnModel().getColumn(1).setPreferredWidth(150);
            this.getColumnModel().getColumn(2).setPreferredWidth(100);
            this.getColumnModel().getColumn(3).setPreferredWidth(100);
            this.getColumnModel().getColumn(4).setPreferredWidth(80);
            this.getColumnModel().getColumn(5).setPreferredWidth(100);
            this.getColumnModel().getColumn(6).setPreferredWidth(50);
            this.getColumnModel().getColumn(7).setPreferredWidth(80);
            this.getColumnModel().getColumn(8).setPreferredWidth(70);
            this.getColumnModel().getColumn(9).setPreferredWidth(50);
            this.getColumnModel().getColumn(10).setPreferredWidth(50);
            
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
            private int rowCount = 0;
            private String [] colNames = {"Part ID",
                                          "Description",     
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

