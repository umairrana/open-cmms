package com.matrix.focus.setting;

import com.matrix.components.TitleBar;
import com.matrix.focus.util.MPanel;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.ImageLibrary;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

public class GUIEmployeeRates extends MPanel{

    private Connection connection;
    private TitleBar titlebar = new TitleBar();
    private JPanel jPanel1 = new JPanel();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private LabourRatesTable tbl = new LabourRatesTable();
    private JButton jButton1 = new JButton(new ImageIcon(ImageLibrary.BUTTON_SAVE));
    private MessageBar messageBar = null;

    public GUIEmployeeRates(DBConnectionPool pool, JFrame frame, MessageBar msgBar){
        this.connection = pool.getConnection();
        messageBar = msgBar;
        try {
            titlebar.setTitle("Employee Rates");
            titlebar.setDescription("Employee Rates of maintenance crew are shown here.");
            titlebar.setImage(ImageLibrary.TITLE_EMPLOYEE_RATES);
            jbInit();
            tbl.populate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(null);
        this.setSize(new Dimension(829, 556));
        titlebar.setBounds(new Rectangle(10, 10, 815, 70));
        jPanel1.setBounds(new Rectangle(10, 85, 815, 435));
        jPanel1.setBorder(BorderFactory.createTitledBorder("Labour Rates"));
        jPanel1.setLayout(null);
        jScrollPane1.setBounds(new Rectangle(10, 20, 795, 405));
        jButton1.setText("Save");
        jButton1.setBounds(new Rectangle(730, 525, 95, 25));
        jButton1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        jButton1_actionPerformed(e);
                    }
                });
        jScrollPane1.getViewport().add(tbl, null);
        jPanel1.add(jScrollPane1, null);
        this.add(jButton1, null);
        this.add(jPanel1, null);
        this.add(titlebar, null);
    }

    private void jButton1_actionPerformed(ActionEvent e) {
        this.updateDB(tbl.getArray());       
    }

    private void updateDB(Object [][] tableArray){
        String sql = null;
        
        try{
            for(int y=0; y < tableArray.length; y++ ){
//                sql =   "UPDATE labour_rates "+
//                        "SET "+
//                        "Regular_Rate = "+tableArray[y][2].toString()+
//                        ", OT_Rate = "+tableArray[y][3].toString()+
//                        ", Double_OT_Rate = "+tableArray[y][4].toString()+
//                        ", Triple_OT_Rate = "+tableArray[y][5].toString()+
//                        " WHERE Employee_ID = "+tableArray[y][0].toString();
                //System.out.println(sql);
                sql =   "INSERT INTO labour_rates ( Employee_ID, Regular_Rate, OT_Rate, Double_OT_Rate, Triple_OT_Rate ) "+
                        "VALUES ('"+tableArray[y][0].toString()+"',"
                        +tableArray[y][2].toString()+","
                        +tableArray[y][3].toString()+","
                        +tableArray[y][4].toString()+","
                        +tableArray[y][5].toString()+") "
                        +" ON DUPLICATE KEY UPDATE "
                        +" Regular_Rate = "+tableArray[y][2].toString()
                        +", OT_Rate = "+tableArray[y][3].toString()
                        +", Double_OT_Rate = "+tableArray[y][4].toString()
                        +", Triple_OT_Rate = "+tableArray[y][5].toString();                
//                System.out.println(sql);        
                int success = connection.createStatement().executeUpdate(sql);                
                if(success!=0)
                      messageBar.setMessage("Rrecords updated","OK");  
//                    messageBar.setMessage(success+" records updated","OK");
                else{                    
                    messageBar.setMessage("Zero records updated","WARN");
                }
            }
        }
        catch(Exception ex){
            messageBar.setMessage(ex.getMessage(),"ERROR");
            ex.printStackTrace();
        }
    }

    public class LabourRatesTable extends JTable {
        private LabourRatesTableModel myModel = null ;

        public LabourRatesTable() {
            myModel = new LabourRatesTableModel();
            this.setModel(myModel);
            
            this.getColumnModel().getColumn(0).setPreferredWidth(100);
            this.getColumnModel().getColumn(1).setPreferredWidth(300);
            this.getColumnModel().getColumn(2).setPreferredWidth(100);
            this.getColumnModel().getColumn(3).setPreferredWidth(100);
            this.getColumnModel().getColumn(4).setPreferredWidth(100);
            this.getColumnModel().getColumn(5).setPreferredWidth(100);
                            
            this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.getTableHeader().setReorderingAllowed(false);
            this.setSelectionMode(0);
        }
        
        public void populate(){
            try {
//                String sql =    "SELECT " +
//                                    "r.Employee_ID, " +
//                                    "e.Name, " +
//                                    "r.Regular_Rate, " +
//                                    "r.OT_Rate, " +
//                                    "r.Double_OT_Rate, " +
//                                    "r.Triple_OT_Rate " +
//                                "FROM " +
//                                    "labour_rates r," +
//                                    "employee e " +
//                                "WHERE " +
//                                    "e.Employee_ID = r.Employee_ID";

                 String sql =    "SELECT " +
                                     "e.Employee_ID, " +
                                     "e.Name, " +
                                     "r.Regular_Rate, " +
                                     "r.OT_Rate, " +
                                     "r.Double_OT_Rate, " +
                                     "r.Triple_OT_Rate " +
                                 "FROM " +
                                     "employee e LEFT OUTER JOIN labour_rates r ON e.Employee_ID = r.Employee_ID "+
                                 "ORDER BY e.Employee_ID ";
                          
                ResultSet rec = connection.createStatement().executeQuery(sql);
                deleteAll();
                int row = -1;
                while(rec.next()){
                    addRow();
                    row = getRowCount()-1;
                    setValueAt(rec.getString("Employee_ID"),row,0);
                    setValueAt(rec.getString("Name"),row,1);
                    setValueAt(rec.getDouble("Regular_Rate"),row,2);
                    setValueAt(rec.getDouble("OT_Rate"),row,3);
                    setValueAt(rec.getDouble("Double_OT_Rate"),row,4);
                    setValueAt(rec.getDouble("Triple_OT_Rate"),row,5);
                }
            } 
            catch (Exception ex)  {
                ex.printStackTrace();
            }
        }
            
        public void addRow(){
            myModel.addRow();
            this.tableChanged(new TableModelEvent(myModel)); 
        }
        
        public void deleteAll(){
            myModel.deleteAll();
            this.tableChanged(new TableModelEvent(myModel));        
        }
        
        public Object [][] getArray(){
            return myModel.valueArray;
        }

        private class LabourRatesTableModel extends AbstractTableModel{
            private String [] colNames = {"Employee ID",//0
                                          "Name",       //1  
                                          "Regular (Rs)",    //2
                                          "Over Time (Rs)",  //3  
                                          "Double O.T. (Rs)",//4
                                          "Triple O.T. (Rs)" //5
                                          };
            private Object [][] valueArray = null;
            private Object [][] tempArray = null;

            public LabourRatesTableModel() {
                valueArray = new Object[0][colNames.length];
            }
            
            public Class getColumnClass(int c){
                if(c > 1)
                    return Double.class;
                else
                    return String.class;
                //return this.getValueAt(0,c).getClass();     
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
                if(c > 1)
                    return true;
                else
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
