package com.matrix.focus.setting;

import com.matrix.components.MDatebox;
import com.matrix.components.TitleBar;
import com.matrix.focus.util.MPanel;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.DateCell;
import com.matrix.focus.util.DateTimeCell;
import com.matrix.focus.util.ImageLibrary;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

public class GUIEmployeeDPAvailability extends MPanel{

    private Connection connection;
    private TitleBar titlebar = new TitleBar();
    private JPanel jPanel1 = new JPanel();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private LabourAvailabilityDailyTable tblDaily = new LabourAvailabilityDailyTable();
    private LabourAvailabilityPeriodicalTable tblPeriod = new LabourAvailabilityPeriodicalTable();
    private JFrame frame;
    private MessageBar messageBar;
    private JButton btnDelete = new JButton(new ImageIcon(ImageLibrary.BUTTON_REMOVE));
    private JButton btnAdd = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));
    private JPanel jPanel2 = new JPanel();
    private MDatebox dtFrom = new MDatebox();
    private MDatebox dtTo = new MDatebox();
    private JButton btnView = new JButton(new ImageIcon(ImageLibrary.BUTTON_VIEW));
    private JTabbedPane tabPane = new JTabbedPane();
    private JScrollPane jScrollPane2 = new JScrollPane();

    public GUIEmployeeDPAvailability(DBConnectionPool pool, JFrame frame, MessageBar msgBar){
        this.connection = pool.getConnection();
        this.frame = frame;
        this.messageBar = msgBar;
        try {
            titlebar.setTitle("Employee Availability");
            titlebar.setDescription("Daily/Periodically availability of employees are shown here.");
            titlebar.setImage(ImageLibrary.TITLE_EMPLOYEE_AVAILABOLITY);
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(null);
        this.setSize(new Dimension(805, 533));
        titlebar.setBounds(new Rectangle(10, 10, 715, 70));
        jPanel1.setBounds(new Rectangle(10, 155, 720, 365));
        jPanel1.setBorder(BorderFactory.createTitledBorder("Availability"));
        jPanel1.setLayout(null);
        btnDelete.setBounds(new Rectangle(735, 235, 30, 30));
        btnDelete.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnDelete_actionPerformed(e);
                    }
                });
        btnAdd.setBounds(new Rectangle(735, 200, 30, 30));
        btnAdd.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnAdd_actionPerformed(e);
                    }
                });
        jPanel2.setBounds(new Rectangle(10, 85, 715, 70));
        jPanel2.setBorder(BorderFactory.createTitledBorder("Period Selection"));
        jPanel2.setLayout(null);
        dtFrom.setBounds(new Rectangle(15, 25, 175, 20));
        dtFrom.setCaption("From");
        dtFrom.setLblWidth(50);
        dtTo.setBounds(new Rectangle(215, 25, 135, 20));
        dtTo.setCaption("To");
        dtTo.setLblWidth(50);
        btnView.setText("View");
        btnView.setBounds(new Rectangle(410, 25, 90, 20));
        btnView.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnView_actionPerformed(e);
                    }
                });
        tabPane.setBounds(new Rectangle(10, 20, 700, 335));
        jScrollPane1.getViewport().add(tblDaily, null);
        jScrollPane2.getViewport().add(tblPeriod, null);
        tabPane.addTab("Daily", jScrollPane1);
        tabPane.addTab("Periodically", jScrollPane2);
        jPanel1.add(tabPane, null);
        jPanel2.add(btnView, null);
        jPanel2.add(dtTo, null);
        jPanel2.add(dtFrom, null);
        this.add(jPanel2, null);
        this.add(btnAdd, null);
        this.add(btnDelete, null);
        this.add(jPanel1, null);
        this.add(titlebar, null);
    }

    private void btnDelete_actionPerformed(ActionEvent e){
        try{
            if(tabPane.getSelectedIndex()==0){
                int row = tblDaily.getSelectedRow();
                if(row!=-1){
                    if(MessageBar.showConfirmDialog(frame,"Are sure you want to remove selected information?","Employee Daily Availability")==MessageBar.YES_OPTION){EmployeeDailyAvailabilityEntry.delete(tblDaily.getValueAt(row,0).toString(),tblDaily.getValueAt(row,2).toString(),connection);  
                        tblDaily.deleteRow(row);
                    }
                }
                else{
                    messageBar.setMessage("Select an availability entry first.","ERROR");
                }
            }
            else if(tabPane.getSelectedIndex()==1){
                int row = tblPeriod.getSelectedRow();
                if(row!=-1){
                    if(MessageBar.showConfirmDialog(frame,"Are sure you want to remove selected information?","Employee Periodical Availability")==MessageBar.YES_OPTION){EmployeePeriodAvailabilityEntry.delete(tblPeriod.getValueAt(row,0).toString(),tblPeriod.getValueAt(row,2).toString(),connection);  
                        tblPeriod.deleteRow(row);
                    }
                }
                else{
                    messageBar.setMessage("Select an availability entry first.","ERROR");
                }
            }
        } 
        catch (Exception ex){
            ex.printStackTrace();
        } 
        
    }

    private void btnAdd_actionPerformed(ActionEvent e){
        if(tabPane.getSelectedIndex()==0){
            NewEmployeeDailyAvailabilityDialog nedad = new NewEmployeeDailyAvailabilityDialog(frame,messageBar,false,connection);
            nedad.setLocationRelativeTo(frame);
            nedad.setVisible(true);
            if(nedad.oneCreated()){
                tblDaily.addRow();
                int row = tblDaily.getRowCount()-1;
                String info[] = nedad.getCreatedEntry();
                tblDaily.setValueAt(info[0],row,0);//ID
                tblDaily.setValueAt(info[1],row,1);//Name
                tblDaily.setValueAt(info[2],row,2);//Date
                tblDaily.setValueAt(info[2]+" 00:00:00",row,3);//Check In
                tblDaily.setValueAt(info[2]+" 00:00:00",row,4);//Check Out
            }
        }
        else if(tabPane.getSelectedIndex()==1){
            NewEmployeeDailyAvailabilityDialog nedad = new NewEmployeeDailyAvailabilityDialog(frame,messageBar,true,connection);
            nedad.setLocationRelativeTo(frame);
            nedad.setVisible(true);
            if(nedad.oneCreated()){
                tblPeriod.addRow();
                int row = tblPeriod.getRowCount()-1;
                String info[] = nedad.getCreatedEntry();
                tblPeriod.setValueAt(info[0],row,0);//ID
                tblPeriod.setValueAt(info[1],row,1);//Name
                tblPeriod.setValueAt(info[2],row,2);//Check In Date
                tblPeriod.setValueAt("0000-00-00",row,3);//Check Out Date
                tblPeriod.setValueAt(info[2]+" 00:00:00",row,4);//Check In
                tblPeriod.setValueAt(info[2]+" 00:00:00",row,5);//Check Out
            }
        }
    }

    private void btnView_actionPerformed(ActionEvent e){
        String from = dtFrom.getInputText();
        String to = dtTo.getInputText();
        if(!from.equals("") && !to.equals("")){
            tblDaily.populate(from,to);
            tblPeriod.populate(from,to);
        }
        else{
            messageBar.setMessage("Please select a valid period.","ERROR");
        }
    }

    public class LabourAvailabilityDailyTable extends JTable {
        private LabourAvailabilityTableModel myModel = null ;

        public LabourAvailabilityDailyTable() {
            myModel = new LabourAvailabilityTableModel();
            this.setModel(myModel);
            
            this.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new DateTimeCell(frame,true)));
            this.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new DateTimeCell(frame,true)));
            
            this.getColumnModel().getColumn(3).setCellRenderer(new TimeCellRenderer());
            this.getColumnModel().getColumn(4).setCellRenderer(new TimeCellRenderer());
            
            this.getColumnModel().getColumn(0).setPreferredWidth(100);
            this.getColumnModel().getColumn(1).setPreferredWidth(300);
            this.getColumnModel().getColumn(2).setPreferredWidth(100);
            this.getColumnModel().getColumn(3).setPreferredWidth(100);
            this.getColumnModel().getColumn(4).setPreferredWidth(100);
                            
            this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.getTableHeader().setReorderingAllowed(false);
            this.setSelectionMode(0);
        }
        
        public void populate(String from, String to){
            try {
                String sql =    "SELECT " +
                                    "r.Employee_ID, " +
                                    "e.Name, " +
                                    "r.Date, " +
                                    "CONCAT(`Date`,' ',r.Check_In_Time) AS Check_In_Time, " +
                                    "CONCAT(`Date`,' ',r.Check_Out_Time) AS Check_Out_Time " +
                                "FROM " +
                                    "employee_availability_daily r," +
                                    "employee e " +
                                "WHERE " +
                                    "e.Employee_ID = r.Employee_ID AND " +
                                    "r.Date BETWEEN '"+from+"' AND '"+to+"'";
                          
                ResultSet rec = connection.createStatement().executeQuery(sql);
                deleteAll();
                int row = -1;
                while(rec.next()){
                    addRow();
                    row = getRowCount()-1;
                    setValueAt(rec.getString("Employee_ID"),row,0);
                    setValueAt(rec.getString("Name"),row,1);
                    setValueAt(rec.getString("Date"),row,2);
                    setValueAt(rec.getString("Check_In_Time"),row,3);
                    setValueAt(rec.getString("Check_Out_Time"),row,4);
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

        private class LabourAvailabilityTableModel extends AbstractTableModel{
            private String [] colNames = {"Employee ID",    //0
                                          "Name",           //1  
                                          "Date",           //2
                                          "Check In Time",  //3  
                                          "Check Out Time"  //4
                                          };
            private Object [][] valueArray = null;
            private Object [][] tempArray = null;


            public LabourAvailabilityTableModel() {
                valueArray = new Object[0][colNames.length];
            }
            
            public Class getColumnClass(int c){
                return this.getValueAt(0,c).getClass();     
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
                try{
                    if(col==3){
                        EmployeeDailyAvailabilityEntry.setCheckInTime(valueArray[row][0].toString(),valueArray[row][2].toString(),valueArray[row][3].toString().substring(11),connection);
                    }
                    else if(col==4){
                        EmployeeDailyAvailabilityEntry.setCheckOutTime(valueArray[row][0].toString(),valueArray[row][2].toString(),valueArray[row][4].toString().substring(11),connection);
                    }
                } 
                catch (Exception ex){
                    ex.printStackTrace();
                } 
                
                
            }
     
            public boolean isCellEditable(int r,int c){
                return c>2;
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
    
    public class LabourAvailabilityPeriodicalTable extends JTable {
        private LabourAvailabilityTableModel myModel = null ;

        public LabourAvailabilityPeriodicalTable() {
            myModel = new LabourAvailabilityTableModel();
            this.setModel(myModel);
            
            this.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new DateCell(frame)));
            this.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new DateTimeCell(frame,true)));
            this.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(new DateTimeCell(frame,true)));
            
            this.getColumnModel().getColumn(4).setCellRenderer(new TimeCellRenderer());
            this.getColumnModel().getColumn(5).setCellRenderer(new TimeCellRenderer());
            
            this.getColumnModel().getColumn(0).setPreferredWidth(100);
            this.getColumnModel().getColumn(1).setPreferredWidth(200);
            this.getColumnModel().getColumn(2).setPreferredWidth(100);
            this.getColumnModel().getColumn(3).setPreferredWidth(100);
            this.getColumnModel().getColumn(4).setPreferredWidth(100);
            this.getColumnModel().getColumn(5).setPreferredWidth(100);
            
            this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.getTableHeader().setReorderingAllowed(false);
            this.setSelectionMode(0);
        }
        
        public void populate(String from, String to){
            try {
                String sql =    "SELECT " +
                                    "r.Employee_ID, " +
                                    "e.Name, " +
                                    "r.Check_In_Date, " +
                                    "r.Check_Out_Date, " +
                                    "CONCAT(Check_In_Date,' ',r.Check_In_Time) AS Check_In_Time, " +
                                    "CONCAT(Check_Out_Date,' ',r.Check_Out_Time) AS Check_Out_Time " +
                                "FROM " +
                                    "employee_availability_period r," +
                                    "employee e " +
                                "WHERE " +
                                    "e.Employee_ID = r.Employee_ID AND " +
                                    "r.Check_In_Date BETWEEN '"+from+"' AND '"+to+"'";
                          
                ResultSet rec = connection.createStatement().executeQuery(sql);
                deleteAll();
                int row = -1;
                while(rec.next()){
                    addRow();
                    row = getRowCount()-1;
                    setValueAt(rec.getString("Employee_ID"),row,0);
                    setValueAt(rec.getString("Name"),row,1);
                    setValueAt(rec.getString("Check_In_Date"),row,2);
                    setValueAt(rec.getString("Check_Out_Date"),row,3);
                    setValueAt(rec.getString("Check_In_Time"),row,4);
                    setValueAt(rec.getString("Check_Out_Time"),row,5);
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

        private class LabourAvailabilityTableModel extends AbstractTableModel{
            private String [] colNames = {"Employee ID",    //0
                                          "Name",           //1  
                                          "Check In Date",  //2
                                          "Check Out Date", //3
                                          "Check In Time",  //4  
                                          "Check Out Time"  //5
                                          };
            private Object [][] valueArray = null;
            private Object [][] tempArray = null;


            public LabourAvailabilityTableModel() {
                valueArray = new Object[0][colNames.length];
            }
            
            public Class getColumnClass(int c){
                return this.getValueAt(0,c).getClass();     
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
                try{
                    if(col==3){
                        EmployeePeriodAvailabilityEntry.setCheckOutDate(valueArray[row][0].toString(),valueArray[row][2].toString(),valueArray[row][3].toString(),connection);
                    }
                    else if(col==4){
                        EmployeePeriodAvailabilityEntry.setCheckInTime(valueArray[row][0].toString(),valueArray[row][2].toString(),valueArray[row][4].toString().substring(11),connection);
                    }
                    else if(col==5){
                        EmployeePeriodAvailabilityEntry.setCheckOutTime(valueArray[row][0].toString(),valueArray[row][2].toString(),valueArray[row][5].toString().substring(11),connection);
                    }
                } 
                catch (Exception ex){
                    ex.printStackTrace();
                } 
                
                
            }
     
            public boolean isCellEditable(int r,int c){
                return c>2;
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
    
    public class TimeCellRenderer extends JLabel implements TableCellRenderer{
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
            this.setText(value.toString().substring(11));
            this.setOpaque(true);
            if(isSelected){
                this.setBackground(table.getSelectionBackground());
                this.setForeground(table.getSelectionForeground());
            }
            else{
                this.setBackground(table.getBackground());
                this.setForeground(table.getForeground());
            }
            
            return this;
        }
    }
}
