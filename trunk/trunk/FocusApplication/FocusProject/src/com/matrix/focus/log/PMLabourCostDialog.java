package com.matrix.focus.log;

import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.ImageLibrary;
import com.matrix.focus.util.LockedLabelCellRenderer;
import com.matrix.focus.connect.ODBCConnection;
import com.matrix.focus.util.Validator;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

public class PMLabourCostDialog extends JDialog {
    private JPanel jPanel1 = new JPanel();
    private JButton btnOk = new JButton(new ImageIcon(ImageLibrary.BUTTON_OK));
    private Connection connection;
    private JScrollPane jScrollPane1 = new JScrollPane();
    private LabourCostTable tblCost = new LabourCostTable();
    private JTextField txtTotal = new JTextField();
    private JLabel jLabel1 = new JLabel();
    private String work_order;
    private String employee_id;
    private String skill;
    private String slot;
    private MessageBar messageBar;

    public PMLabourCostDialog(JDialog parent, Connection conn, MessageBar msgBar, boolean capable) {
        this(parent, "", true);
        connection = conn;
        messageBar = msgBar;
        btnOk.setEnabled(capable);
    }

    public PMLabourCostDialog(JDialog parent, String title, boolean modal) {
        super(parent, title, modal);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setEmployee(String emp_id, String wo, String skill, String slot){
        setTitle("Labour Cost - "+emp_id);
        this.work_order = wo;
        this.employee_id = emp_id;
        this.skill = skill;
        this.slot = slot;
        
        if(isLogged(emp_id,wo,skill,slot)){
            /**Pick from the database utilisation table*/
            try{
                String sql = "SELECT " +
                                "Regular_Time," +
                                "Regular_Rate," +
                                "Over_Time," +
                                "Over_Time_Rate," +
                                "Double_Over_Time," +
                                "Double_Over_Time_Rate," +
                                "Triple_Over_Time," +
                                "Triple_Over_Time_Rate " +
                             "FROM " +
                                "labour_utilisation " +
                             "WHERE " +
                                "PM_Work_Order_ID =? AND " +
                                "Employee_ID =? AND " + 
                                "Skill_Category =? AND " + 
                                "Time_Slot_ID =?";
                
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1,wo);
                stmt.setString(2,emp_id);
                stmt.setString(3,skill);
                stmt.setString(4,slot);

                tblCost.deleteAll();
                ResultSet rec = stmt.executeQuery();
                rec.next();
                
                String time[] = rec.getString("Regular_Time").split(":");
                String rate = rec.getString("Regular_Rate");
                tblCost.addCategory("Regular Time",time[0],time[1],rate);
                
                time = rec.getString("Over_Time").split(":");
                rate = rec.getString("Over_Time_Rate");
                tblCost.addCategory("Over Time",time[0],time[1],rate);
                
                time = rec.getString("Double_Over_Time").split(":");
                rate = rec.getString("Double_Over_Time_Rate");
                tblCost.addCategory("Double Over Time",time[0],time[1],rate);
                
                time = rec.getString("Triple_Over_Time").split(":");
                rate = rec.getString("Triple_Over_Time_Rate");
                tblCost.addCategory("Triple Over Time",time[0],time[1],rate);
            } 
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            /**Get rates from the excel*/
            try{
                String sql = "SELECT * FROM labour_rates WHERE Employee_ID =?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1,emp_id);
                ResultSet rec = stmt.executeQuery();
                if(rec.first()){
                    tblCost.deleteAll();
                    tblCost.addCategory("Regular Time","0","0",rec.getString("Regular_Rate"));
                    tblCost.addCategory("Over Time","0","0",rec.getString("OT_Rate"));
                    tblCost.addCategory("Double Over Time","0","0",rec.getString("Double_OT_Rate"));
                    tblCost.addCategory("Triple Over Time","0","0",rec.getString("Triple_OT_Rate"));
                }
                else{
                    messageBar.setMessage("Labour rates not found for employee: "+emp_id,"WARN");
                }
            } 
            catch(Exception er)  {
                er.printStackTrace();
            }
        }
    }
    
    private boolean isLogged(String emp_id, String wo, String skill, String slot){
        String sql = "SELECT " +
                        "Regular_Rate " +
                     "FROM " +
                        "labour_utilisation " +
                     "WHERE " +
                        "PM_Work_Order_ID =? AND " +
                        "Employee_ID =? AND " + 
                        "Skill_Category =? AND " + 
                        "Time_Slot_ID =?";
        try{            
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,wo);
            stmt.setString(2,emp_id);
            stmt.setString(3,skill);
            stmt.setString(4,slot);
            
            ResultSet rec = stmt.executeQuery();
            rec.next();
            return ((rec.getDouble("Regular_Rate")>0)?true:false);
        }
        catch(Exception er){
            er.printStackTrace();
            return false;
        }
    }
    
    private void jButton1_actionPerformed(ActionEvent e){
        try{
            Savepoint sp = connection.setSavepoint();
            if(saveLabourRates()){
                saveEmplyeeCostTo_TAG();
                messageBar.setMessage("Labour rates saved.","OK");
                setVisible(false);
            }
            else{
                connection.rollback(sp);
                messageBar.setMessage("Error while saving Labour rates.","ERROR");
            }
        }
        catch(Exception er){
            er.printStackTrace();
            messageBar.setMessage(er.getMessage(),"ERROR");
        }
    }
    
    private boolean saveLabourRates(){
        LabourCostEntry lce = new LabourCostEntry(connection);
        lce.work_order = this.work_order;
        lce.emp_id = this.employee_id;
        lce.skill = this.skill;
        lce.slot = this.slot;
        lce.regulat_time = tblCost.getValueAt(0,1).toString() + ":" + tblCost.getValueAt(0,2).toString() + ":00";
        lce.regular_rate = tblCost.getValueAt(0,3).toString();
        lce.over_time = tblCost.getValueAt(1,1).toString() + ":" + tblCost.getValueAt(1,2).toString() + ":00";
        lce.over_time_rate = tblCost.getValueAt(1,3).toString();
        lce.double_ot_time = tblCost.getValueAt(2,1).toString() + ":" + tblCost.getValueAt(2,2).toString() + ":00";
        lce.double_ot_rate = tblCost.getValueAt(2,3).toString();
        lce.triple_ot_time = tblCost.getValueAt(3,1).toString() + ":" + tblCost.getValueAt(3,2).toString() + ":00";
        lce.triple_ot_rate = tblCost.getValueAt(3,3).toString();
        return lce.savelog();
    }
    
    private void saveEmplyeeCostTo_TAG() throws Exception{
        ODBCConnection odbc = new ODBCConnection("Matrix","","matrixit");
        Connection tagConnection = odbc.getConnection();
        tagConnection.setAutoCommit(false);
        /*****************************************************************/
           //Delete existing labour cost details
           PreparedStatement stmt_del = tagConnection.prepareStatement("DELETE FROM Labour_Utilize_MF WHERE JOB_NO=?");
           stmt_del.setString(1,work_order);
           stmt_del.executeUpdate();
           //Get new labour costing from the system
           PreparedStatement stmt = connection.prepareStatement("CALL getLabourCostForWorkOrder(?)");
           stmt.setString(1,work_order);
           ResultSet rec = stmt.executeQuery();
           //Save to TAG
           String sql = "INSERT INTO Labour_Utilize_MF (JOB_NO, EMP_ID, REG_Cost, OT_Cost, DBL_OT_Cost, TPL_OT_Cost) VALUES(?,?,?,?,?,?)";
           while(rec.next()){
               PreparedStatement stmt_tag = tagConnection.prepareStatement(sql);
                   stmt_tag.setString(1,work_order);
                   stmt_tag.setString(2,rec.getString("Employee_ID"));
                   stmt_tag.setString(3,rec.getString("REG_COST"));
                   stmt_tag.setString(4,rec.getString("OT_COST"));
                   stmt_tag.setString(5,rec.getString("DBL_COST"));
                   stmt_tag.setString(6,rec.getString("TRP_COST"));
               if(stmt_tag.executeUpdate()!=1){
                   throw new Exception("Labour cost was not saved.");
               }
           }
        /*****************************************************************/   
        tagConnection.commit();
        tagConnection.close();
    }
        
    class LabourCostEntry{
        public String work_order;
        public String emp_id;
        public String skill;
        public String slot;
        public String regulat_time;
        public String regular_rate;
        public String over_time;
        public String over_time_rate;
        public String double_ot_time;
        public String double_ot_rate;
        public String triple_ot_time;
        public String triple_ot_rate;
        private Connection connection;
        public LabourCostEntry(Connection conn){
            connection = conn;
        }
        
        public boolean savelog(){
            
            String sql = "UPDATE labour_utilisation SET " +
                                "Regular_Time = ? , " +
                                "Regular_Rate = ? , " +
                                "Over_Time = ? , " +
                                "Over_Time_Rate = ? , " +
                                "Double_Over_Time = ? , " +
                                "Double_Over_Time_Rate = ?, " +
                                "Triple_Over_Time = ? , " +
                                "Triple_Over_Time_Rate = ? " + 
                         "WHERE " +
                                "PM_Work_Order_ID =? AND " +
                                "Employee_ID =? AND " + 
                                "Skill_Category =? AND " + 
                                "Time_Slot_ID =?";

            try{
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1,regulat_time);
                stmt.setString(2,regular_rate);
                stmt.setString(3,over_time);
                stmt.setString(4,over_time_rate);
                stmt.setString(5,double_ot_time);
                stmt.setString(6,double_ot_rate);
                stmt.setString(7,triple_ot_time);
                stmt.setString(8,triple_ot_rate);
                stmt.setString(9,work_order);
                stmt.setString(10,emp_id);
                stmt.setString(11,skill);
                stmt.setString(12,slot);
                
                return (stmt.executeUpdate()>0?true:false);
            } 
            catch(SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
    
    public String getTotalCost(){
        return txtTotal.getText();
    }

    private void jbInit() throws Exception {
        this.setSize(new Dimension(407, 237));
        this.setResizable(false);
        this.getContentPane().setLayout( null );
        this.setTitle("Labour Cost");
        jPanel1.setBounds(new Rectangle(5, 5, 390, 165));
        jPanel1.setBorder(BorderFactory.createTitledBorder("Composition"));
        jPanel1.setLayout(null);
        btnOk.setText("OK");
        btnOk.setBounds(new Rectangle(295, 175, 90, 25));
        btnOk.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        jButton1_actionPerformed(e);
                    }
                });
        jScrollPane1.setBounds(new Rectangle(10, 15, 370, 140));
        txtTotal.setBounds(new Rectangle(200, 175, 85, 20));
        txtTotal.setHorizontalAlignment(JTextField.RIGHT);
        txtTotal.setEditable(false);
        jLabel1.setText("Total Cost Rs.");
        jLabel1.setBounds(new Rectangle(125, 175, 85, 20));
        jScrollPane1.getViewport().add(tblCost, null);
        jPanel1.add(jScrollPane1, null);
        this.getContentPane().add(jLabel1, null);
        this.getContentPane().add(txtTotal, null);
        this.getContentPane().add(btnOk, null);
        this.getContentPane().add(jPanel1, null);
    }
    
     private class LabourCostTable extends JTable {
              private LabourCostTableModel myModel = null;
                            
              public LabourCostTable(){
                  myModel = new LabourCostTableModel();
                  this.setModel(myModel);

                  this.setSelectionMode(0);
                  this.getTableHeader().setReorderingAllowed(false);
                  this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                  
                  this.getColumnModel().getColumn(0).setCellRenderer(new LockedLabelCellRenderer());
                  this.getColumnModel().getColumn(3).setCellRenderer(new LockedLabelCellRenderer());
                  this.getColumnModel().getColumn(4).setCellRenderer(new LockedLabelCellRenderer());
                  
                  this.getColumnModel().getColumn(3).setMinWidth(0);
                  this.getColumnModel().getColumn(3).setPreferredWidth(0);
                  
                  this.getColumnModel().getColumn(0).setPreferredWidth(100);
                  this.getColumnModel().getColumn(1).setPreferredWidth(80);
                  this.getColumnModel().getColumn(2).setPreferredWidth(80);
                  this.getColumnModel().getColumn(4).setPreferredWidth(100);  
              }
            
              public void addCategory(String name,String hours,String mins,String rate){
                addRow();
                int row = this.getRowCount()-1;
                this.setValueAt(name,row,0);
                this.setValueAt(hours,row,1);
                this.setValueAt(mins,row,2);
                this.setValueAt(rate,row,3);
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
                   
              private class LabourCostTableModel extends AbstractTableModel{
                  private int rowCount = 0;
                  private String [] colNames = {
                                   "Category",//0
                                   "Hours",//1
                                   "Minutes",//2
                                   "Rate",//3
                                   "Sub Total"//4
                                   };
                  private Object [][] valueArray = null;
                  private Object [][] tempArray = null;
              
                  public LabourCostTableModel() {
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
                      if(col==1 || col==2){
                          if(Validator.isNonNegative(value.toString())){
                              valueArray[row][col] = (int)Double.parseDouble(value.toString());
                          }
                          else{
                              valueArray[row][col] = "0";
                          }
                          
                      }
                      else{
                          valueArray[row][col] = value;
                      }
                      calculate(row);
                      setTotal();
                  }
                  
                  private void calculate(int row){
                      try{
                          int hours = Integer.parseInt(valueArray[row][1].toString());
                          double minutes = Integer.parseInt(valueArray[row][2].toString());
                          double rate = Double.parseDouble(valueArray[row][3].toString());
                          valueArray[row][4] = (hours+(minutes/60))*rate;
                          tblCost.repaint();
                      }
                      catch(Exception er){
                          er.printStackTrace();
                      }
                  }
                  
                  private void setTotal(){
                    double total = 0.00;
                    int rows = this.getRowCount();
                    for (int i=0;i<rows;i++){
                        total += Double.parseDouble(valueArray[i][4].toString());
                    }
                    txtTotal.setText(total+"");
                  }
                       
                  public boolean isCellEditable(int r,int c){
                      return ((c==1 || c==2)?true:false);
                  }
                   
             
                  public void addRow(){
                      tempArray = new Object[this.getRowCount()+1][this.getColumnCount()];
                  
                      for(int y=0 ; y<valueArray.length; y++){
                          for(int x=0; x<valueArray[0].length; x++){
                              tempArray[y][x] =  valueArray[y][x];
                          }
                      }
                      valueArray = tempArray;
                      valueArray[valueArray.length-1][1] = "0";
                      valueArray[valueArray.length-1][2] = "0";
                      valueArray[valueArray.length-1][3] = "0";
                      valueArray[valueArray.length-1][4] = "0";
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
