package com.matrix.focus.workorder;

import com.matrix.focus.mdi.MDI;
import com.matrix.focus.util.ButtonCellRenderer;
import com.matrix.focus.util.ImageLibrary;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

public class EmployeeAvailabilityDialog extends JDialog {
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JButton btnCancel = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
    private EmployeeAvailabilityTable tblEmp;
    private JDialog frame;
    private Connection connection;
    private String DATE;
    private TimeRulerDialog dlgTimeRuler;
    private String[] info = new String[4];
    private boolean emp_selected;
    
    public EmployeeAvailabilityDialog(MDI mdi, JDialog frame,Connection conn) {
        super(frame, "", true);
        this.frame = frame;
        this.connection = conn;
        try {
            dlgTimeRuler =  new TimeRulerDialog(mdi,this,connection);
            tblEmp = new EmployeeAvailabilityTable(connection);
            dlgTimeRuler.setLocationRelativeTo(frame);
            jbInit();
            tblEmp.addMouseListener(new MouseAdapter(){
              public void mouseClicked(MouseEvent me){
                if(me.getClickCount()==2){
                    int r = tblEmp.getSelectedRow();
                    if(r!=-1){
                      info[0] = tblEmp.getValueAt(r,1).toString();
                      info[1] = tblEmp.getValueAt(r,4).toString();
                      info[2] = tblEmp.getValueAt(r,5).toString();
                      info[3] = tblEmp.getValueAt(r,6).toString();
                    }
                    emp_selected = true;
                    setVisible(false);
                }
                if(tblEmp.getSelectedColumn()==7){
                  int row = tblEmp.getSelectedRow();
                  if(row!=-1){
                      String emp_id = tblEmp.getValueAt(row,4).toString();
                      String name = tblEmp.getValueAt(row,5).toString();
                      dlgTimeRuler.setAssignedInfo(emp_id,name,DATE.substring(0,10));
                      dlgTimeRuler.setVisible(true);
                  }
                }
              }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        
    public String[] getSelectedEmployeeInfo(){
        if(emp_selected){
            return info;
        }
        else{
            return null;
        }
    }
    
    public void populate(String date){
        emp_selected = false;
        DATE = date;
        String sql = "SELECT " +
                        "e.Employee_Type, " +
                        "es.Maintenance_Category AS Skill," +
                        "es.Proficiency, " +
                        "e.Skill_Level," +
                        "e.Employee_ID," +
                        "e.Name," +
                        "e.Title " +
                     "FROM " +
                        "employee e, " +
                        "employee_skills es " +
                     "WHERE " +
                        "e.Employee_ID = es.Employee_ID " +
                     "ORDER BY " +
                        "e.Employee_Type," +
                        "e.Employee_ID," +
                        "es.Proficiency";
        try{
            ResultSet rec = connection.createStatement().executeQuery(sql);
            tblEmp.deleteAll();
            int row = -1;
            while(rec.next()){
                tblEmp.addRow();
                row = rec.getRow()-1;
                tblEmp.setValueAt(rec.getString("Employee_Type"),row,0);
                tblEmp.setValueAt(rec.getString("Skill"),row,1);
                tblEmp.setValueAt(rec.getString("Proficiency"),row,2);
                tblEmp.setValueAt(rec.getString("Skill_Level"),row,3);
                tblEmp.setValueAt(rec.getString("Employee_ID"),row,4);
                tblEmp.setValueAt(rec.getString("Name"),row,5);
                tblEmp.setValueAt(rec.getString("Title"),row,6);
            }
        }
        catch(Exception er){
            er.printStackTrace();
        }
    }
    
    private void jbInit() throws Exception {
        this.setSize(new Dimension(763, 298));
        this.setResizable(false);
        this.getContentPane().setLayout( null );
        this.setTitle("Employee Availability");
        jScrollPane1.setBounds(new Rectangle(10, 10, 735, 215));
        btnCancel.setText("Cancel");
        btnCancel.setBounds(new Rectangle(645, 230, 100, 25));
        btnCancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        jButton1_actionPerformed(e);
                    }
                });
        jScrollPane1.getViewport().add(tblEmp, null);
        this.getContentPane().add(btnCancel, null);
        this.getContentPane().add(jScrollPane1, null);
        this.addWindowListener(new WindowAdapter(){
            public void windowClosed(WindowEvent e){
                info[0] = null;
            }
        });
    }

    private void jButton1_actionPerformed(ActionEvent e) {
        info[0] = null;
        this.setVisible(false);
    }
    
    public class EmployeeAvailabilityTable extends JTable {
        private EmployeeAvailabilityTableModel myModel = null ;

        public EmployeeAvailabilityTable(Connection conn) {
            myModel = new EmployeeAvailabilityTableModel();
            this.setModel(myModel);
            this.getTableHeader().setReorderingAllowed(false);
            this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.setSelectionMode(0); 
            
            this.getColumnModel().getColumn(7).setCellRenderer(new ButtonCellRenderer());
            
            this.getColumnModel().getColumn(0).setPreferredWidth(100);
            this.getColumnModel().getColumn(1).setPreferredWidth(100);
            this.getColumnModel().getColumn(2).setPreferredWidth(50);
            this.getColumnModel().getColumn(3).setPreferredWidth(120);
            this.getColumnModel().getColumn(4).setPreferredWidth(80);
            this.getColumnModel().getColumn(5).setPreferredWidth(150);
            this.getColumnModel().getColumn(6).setPreferredWidth(80);
            this.getColumnModel().getColumn(7).setPreferredWidth(60);
        }
    
        public void addRow(){
            myModel.addRow();
            this.tableChanged(new TableModelEvent(myModel)); 
        }

        public void deleteRow(){
            myModel.deleteRow();
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

        private class EmployeeAvailabilityTableModel extends AbstractTableModel{
            private int rowCount = 0;
            private String [] colNames = {"Type",         //0
                                          "Skill",        //1
                                          "Priority",     //2
                                          "Skill Level",  //3
                                          "Employee ID",  //4
                                          "Name",         //5
                                          "Title",        //6
                                          "Availability"};//7
            private Object [][] valueArray = null;
            private Object [][] tempArray = null;


            public EmployeeAvailabilityTableModel() {
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
                valueArray[valueArray.length-1][7] = "Select Here";
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
                }
            }

            public void deleteAll(){
                valueArray = new Object[0][colNames.length];
            }
        }
    }
}
