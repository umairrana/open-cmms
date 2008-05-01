package com.matrix.focus.workorder;

import com.matrix.components.MDataCombo;
import com.matrix.focus.log.LabourUtilisationEntry;
import com.matrix.focus.mdi.MDI;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.ButtonCellRenderer;
import com.matrix.focus.util.DateTimeCell;
import com.matrix.focus.util.ImageLibrary;
import com.matrix.focus.util.LockedLabelCellRenderer;
import com.matrix.focus.util.StandardTimeCell;
import com.matrix.focus.util.Utilities;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Savepoint;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

public class LabourAllocationDialog extends JDialog{
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JScrollPane jScrollPane2 = new JScrollPane();
    private LabourAllocationTable tblLabour = new LabourAllocationTable();
    private JButton btnDone = new JButton(new ImageIcon(ImageLibrary.BUTTON_OK));
    private JButton btnRemoveL = new JButton(new ImageIcon(ImageLibrary.BUTTON_REMOVE));
    private JButton btnAddL = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));
    private LabourUtilisationEntry lue;
    private MDataCombo mcmbLeader = new MDataCombo();
    private LabourCategoryTable tblCategory = new LabourCategoryTable();
    private EmployeeAvailabilityDialog dlgEA;
    private String WORK_ORDER;
    private JPanel jPanel1 = new JPanel();
    private JPanel jPanel2 = new JPanel();
    private JobDialog dlgJob;
    private MDI frame;
    private MessageBar messageBar;
    private JTable tblWorkOrder;
    private JTable tblRequested;
    private Connection connection;
    private boolean capable;
    private String promised_date;
    private String work_order;
    private String asset_id;

    public LabourAllocationDialog(MDI frame, MessageBar msgBar, JTable tblWorkOrder, JTable tblRequested, String work_order, String asset_id, boolean capable, String promised_date,Connection connection){
        super(frame, "Labour Allocation", true);
        this.frame = frame;
        this.messageBar = msgBar;
        this.tblWorkOrder = tblWorkOrder;
        this.tblRequested = tblRequested;
        this.connection = connection;
        this.work_order = work_order;
        this.asset_id = asset_id;
        this.capable = capable;
        this.promised_date = promised_date;
        try{
            jbInit();
            dlgEA = new EmployeeAvailabilityDialog(frame,this,connection);
            dlgJob = new JobDialog(this);
            dlgJob.setLocationRelativeTo(this);
            
            tblLabour.addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent me){
                    if(me.getButton()==1){
                        if(tblLabour.getSelectedColumn()==9){
                            dlgJob.populate(getJobs());
                            dlgJob.setVisible(true);
                            String data[] = dlgJob.getJob();
                            if(!data[0].equals("")){
                                int row = tblLabour.getSelectedRow();
                                tblLabour.setValueAt(data[0],row,10);
                                tblLabour.setValueAt(data[1],row,11);
                                tblLabour.repaint();
                            }
                        }
                    }
                }
            });
        }
        catch(Exception e){
            e.printStackTrace();
        }
        /**Authorization*/
        btnAddL.setEnabled(capable);
        btnRemoveL.setEnabled(capable);
    }
    
    private String[][] getJobs(){
        int rows_1 = tblWorkOrder.getRowCount();
        int rows_2 = tblRequested.getRowCount();
        String data[][] = new String[(rows_1+rows_2)+1][2];
        int i=0;
        for(;i<rows_1;i++){
            data[i][0] = asset_id;
            data[i][1] = tblWorkOrder.getValueAt(i,2).toString();
        }
        for(;i<(rows_1+rows_2);i++){
            data[i][0] = asset_id;
            data[i][1] = tblRequested.getValueAt(i,0).toString();
        }
        data[(rows_1+rows_2)][0] = asset_id;
        data[(rows_1+rows_2)][1] = "ALL";
        return data;
    }

    private void jbInit() throws Exception{
        this.setSize(new Dimension(975, 385));
        this.setResizable(false);
        this.getContentPane().setLayout(null);
        mcmbLeader.setLocation(10,10);
        mcmbLeader.setCaption("Team Leader");
        mcmbLeader.setLblFont(new Font("System",0,12));
        mcmbLeader.setCmbFont(new Font("System",0,12));
        mcmbLeader.setLblWidth(80);
        mcmbLeader.setCmbWidth(120);
        mcmbLeader.setBounds(new Rectangle(300, 105, 200, 20));

        btnRemoveL.setBounds(new Rectangle(750, 315, 100, 25));
        btnRemoveL.setText("Remove");
        btnRemoveL.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnRemoveL_actionPerformed(e);
                    }
                });
        btnDone.setBounds(new Rectangle(855, 315, 100, 25));
        btnDone.setText("OK");
        btnDone.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnDone_actionPerformed(e);
                    }
                });
        btnAddL.setBounds(new Rectangle(645, 315, 100, 25));
        btnAddL.setText("Add");

        btnAddL.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnAddL_actionPerformed(e);
                    }
                });
        this.getContentPane().add(mcmbLeader);
        this.getContentPane().add(btnRemoveL, null);
        this.getContentPane().add(btnAddL, null);
        this.getContentPane().add(btnDone, null);
        jScrollPane2.getViewport().add(tblCategory);
        jPanel1.add(jScrollPane2, null);
        this.getContentPane().add(jPanel1, null);
        jScrollPane1.getViewport().add(tblLabour);
        jPanel2.add(jScrollPane1, null);
        this.getContentPane().add(jPanel2, null);
        jPanel1.setBorder(BorderFactory.createTitledBorder("Labour Requirement for Planned Jobs"));
        jPanel1.setBounds(new Rectangle(5, 5, 290, 125));
        jPanel1.setLayout(null);
        jScrollPane2.setBounds(new Rectangle(10, 15, 270, 100));

        jPanel2.setBorder(BorderFactory.createTitledBorder("Labour Allocation"));
        jPanel2.setBounds(new Rectangle(5, 135, 960, 175));
        jPanel2.setLayout(null);
        jScrollPane1.setBounds(new Rectangle(10, 15, 940, 150));
    }
    
    public void populate(String work_order_id){
        WORK_ORDER = work_order_id;
        mcmbLeader.removeAllItems();
        mcmbLeader.addItem("");
        String sql = "SELECT " +
                            "lu.Skill_Category," +
                            "lu.Employee_ID," +
                            "e.Name," +
                            "e.Title," +
                            "IF(lu.planned_start_time='0000-00-00 00:00:00','0000-00-00 00:00:00',lu.planned_start_time) AS start_time," +
                            "lu.Setup_time," +
                            "lu.Duration," +
                            "IF(lu.planned_end_time='0000-00-00 00:00:00','0000-00-00 00:00:00',lu.planned_end_time) AS end_time," +
                            "lu.Team_ID, " +
                            "lu.Time_Slot_ID," +
                            "lu.Asset_ID," +
                            "lu.PM_Job " +
                     "FROM " +
                            "labour_utilisation lu, " +/**UPM Table labour_utilization_for_up_mnt*/
                            "employee e " +
                     "WHERE " +
                            "PM_Work_Order_ID='"+ WORK_ORDER +"' AND " +
                            "lu.Employee_ID = e.Employee_ID " +
                     "ORDER BY " +
                            "lu.Employee_ID," +
                            "lu.Skill_Category," +
                            "lu.Time_Slot_ID";
        try{
            ResultSet rec = connection.createStatement().executeQuery(sql);
            int cnt = 0;
            tblLabour.deleteAll();
            String emp_id = "";
            while(rec.next()){
                cnt = rec.getRow()-1;
                tblLabour.addRow();
                tblLabour.setValueAt(rec.getString("Skill_Category"),cnt,0);
                emp_id = rec.getString("Employee_ID");
                mcmbLeader.addItem(emp_id);
                tblLabour.setValueAt(emp_id,cnt,1);
                tblLabour.setValueAt(rec.getString("Name"),cnt,2);
                tblLabour.setValueAt(rec.getString("Title"),cnt,3);
                tblLabour.setValueAt(rec.getString("start_time"),cnt,4);
                tblLabour.setValueAt(rec.getString("Setup_time"),cnt,5);
                tblLabour.setValueAt(rec.getString("Duration"),cnt,6);
                tblLabour.setValueAt(rec.getString("end_time"),cnt,7);
                tblLabour.setValueAt(rec.getString("Time_Slot_ID"),cnt,8);
                tblLabour.setValueAt(rec.getString("Asset_ID"),cnt,10);
                tblLabour.setValueAt(rec.getString("PM_Job"),cnt,11);
                emp_id = rec.getString("Team_ID");
            }
            mcmbLeader.setSelectedItem(emp_id);
            setTotalRequirement();
            setAllocatedAndRequiredHours();
        }
        catch(Exception er){
            er.printStackTrace();
        }
    }
    private void setTotalRequirement(){
        int rows = tblWorkOrder.getRowCount();
        if(rows>0){
            /**Get Schedule ID set*/
            String smids = "'" + tblWorkOrder.getValueAt(0,1).toString();
            for(int i=1;i<rows;i++){
                smids += "','" + tblWorkOrder.getValueAt(i,1).toString();
            }
            smids += "'";
            
            /**Get Categories*/
            String sqlCat = "SELECT " +
                                "DISTINCT lr.Maintenance_Category " +
                            "FROM " +
                                "scheduled_preventive_maintenance spm, " +
                                "labour_requirement lr " +
                            "WHERE " +
                                "spm.Scheduled_ID IN("+ smids +") AND " +
                                "spm.Asset_ID = lr.Asset_ID AND " +
                                "spm.Preventive_Maintenance_ID = lr.Preventive_Maintenance_ID";
            try{
               ResultSet rec = connection.createStatement().executeQuery(sqlCat);
               tblCategory.deleteAll();
               while(rec.next()){
                  tblCategory.addRow();
                  tblCategory.setValueAt(rec.getString("Maintenance_Category"),rec.getRow()-1,0);
               }
            } 
            catch(Exception ex){
                ex.printStackTrace();
            }
            
            /**Get total amount of each category*/
            for(int i=0;i<tblCategory.getRowCount();i++){
                sqlCat = "SELECT " +
                                "lr.Amount " +
                             "FROM " +
                                "scheduled_preventive_maintenance spm, " +
                                "labour_requirement lr " +
                            "WHERE " +
                                "spm.Scheduled_ID IN("+ smids +") AND " +
                                "spm.Asset_ID = lr.Asset_ID AND " +
                                "spm.Preventive_Maintenance_ID = lr.Preventive_Maintenance_ID AND " +
                                "lr.Maintenance_Category ='"+tblCategory.getValueAt(i,0).toString()+"'";
                try{
                   ResultSet rec = connection.createStatement().executeQuery(sqlCat);
                   int minutes = 0;
                   while(rec.next()){
                       minutes += Utilities.getMinutes(rec.getString("Amount"));
                   }
                   tblCategory.setValueAt(minutes,i,1);
                } 
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    }
    
    private void setAllocatedAndRequiredHours(){
        int c_rows = tblCategory.getRowCount();
        int e_rows = tblLabour.getRowCount();
        String c_category = "";
        String e_category = "";
        int tmpValue = 0;
        //From all categories
        for (int c=0;c<c_rows;c++){
            //Select a category
            c_category = tblCategory.getValueAt(c,0).toString();
            //Reset category's old values
            tblCategory.setValueAt("0",c,2);
            tblCategory.setValueAt("0",c,3);
            for(int e=0;e<e_rows;e++){
                //For each added employee
                 e_category = tblLabour.getValueAt(e,0).toString();
                 //Check for same category
                 if(c_category.equals(e_category)){
                     //Get the current value
                     tmpValue = Integer.parseInt(tblCategory.getValueAt(c,2).toString());
                     //Add this employee's amount
                     tmpValue += Utilities.getMinutes(tblLabour.getValueAt(e,6).toString());
                     //Allocated Hours
                     tblCategory.setValueAt(tmpValue+"",c,2);  
                 }
            }
            //Required Hours
            tmpValue = Integer.parseInt(tblCategory.getValueAt(c,1).toString()) - Integer.parseInt(tblCategory.getValueAt(c,2).toString());
            tblCategory.setValueAt((tmpValue<0?0:tmpValue),c,3);
            tmpValue = 0;    
        }
    }
    
    private String getPlannedEndTime(String start, String setup, String duration){
        String setupTimes[] = setup.split("-");
        String durationTimes[] = duration.split("-");
                
        int days = Integer.parseInt(setupTimes[0]) + Integer.parseInt(durationTimes[0]);
        int hours = Integer.parseInt(setupTimes[1]) + Integer.parseInt(durationTimes[1]);
        int mins = Integer.parseInt(setupTimes[2]) + Integer.parseInt(durationTimes[2]);
        
        if(mins>=60){
            hours++;
            mins -= 60;
        }
        if(hours>=24){
            days++;
            hours -= 24;
        }
    
        String addition = days + " " + hours + ":" + mins + ":00";
        
        try{
            String sql = "SELECT ADDTIME('" + start + "','" + addition + "')";
            ResultSet rec = connection.createStatement().executeQuery(sql);
            rec.next();
            return rec.getString(1);
        }
        catch(Exception er){
            return "ERROR";
        }
    } 
    
    private boolean allocateEmployees(){
        int rows = tblLabour.getRowCount();
        for(int j=0;j<rows;j++){
            lue = new LabourUtilisationEntry(connection);
            lue.pm_work_order_id = work_order;
            lue.team_id = mcmbLeader.getSelectedItem().toString();
            lue.skill_category = tblLabour.getValueAt(j,0).toString();
            lue.employee_id = tblLabour.getValueAt(j,1).toString();
            lue.planned_start_time = tblLabour.getValueAt(j,4).toString();
            lue.planned_end_time = tblLabour.getValueAt(j,7).toString();
            lue.setup_time = tblLabour.getValueAt(j,5).toString();
            lue.duration = tblLabour.getValueAt(j,6).toString();
            lue.ending_time = tblLabour.getValueAt(j,7).toString();
            lue.time_slot_id = tblLabour.getValueAt(j,8).toString();
            lue.asset_id = tblLabour.getValueAt(j,10).toString();
            lue.pm_job = tblLabour.getValueAt(j,11).toString();
            
            /**new slots*/
            lue.time_slot_id = (lue.time_slot_id.trim().equals("")?getTimeSlotID(lue.pm_work_order_id,lue.employee_id,lue.skill_category):lue.time_slot_id);
            
            try{
                lue.save(); 
            } 
            catch (Exception ex){
                try{
                    lue.update();
                } 
                catch (Exception exep){
                    exep.printStackTrace();
                    return false;
                }
                //ex.printStackTrace();
            } 
        }
        
        return true;
    }
    
    private String getTimeSlotID(String pm_work_order_id, String employee_id, String skill_category){
        String sql = "SELECT " + 
                        "(MAX(Time_Slot_ID) + 1 ) AS ID " + 
                     "FROM " + 
                        "labour_utilisation " + 
                     "WHERE " + 
                        "PM_Work_Order_ID = '" + pm_work_order_id + "' AND " + 
                        "Employee_ID = '" + employee_id + "' AND " + 
                        "Skill_Category = '" + skill_category +"'";        
        try  {
            ResultSet rec = connection.createStatement().executeQuery(sql);
            rec.next();
            String s_id = rec.getString("ID");
            return (s_id==null?"1":s_id); 
        } catch (Exception ex)  {
            ex.printStackTrace();
            return "1";
        } 
    }
    
    private boolean validateEmployeeInfo(){
        if(tblLabour.getRowCount()==1){
            mcmbLeader.getComboBox().setSelectedIndex(1);
        }
        
        if(mcmbLeader.getComboBox().getSelectedIndex()==0){
            messageBar.setMessage("Please specify the team leader.","ERROR");
            return false;
        }
        else{
            return true;
        }
    }

    private void btnDone_actionPerformed(ActionEvent e) {
         if(capable){
             if(validateEmployeeInfo()){
                 try{
                     Savepoint sPointEmployee = connection.setSavepoint("Employee");
                     if(allocateEmployees()){ 
                         messageBar.setMessage("Employees for the selected maintenance was saved.","OK");
                         this.setVisible(false);
                     }
                     else{
                         connection.rollback(sPointEmployee);
                         messageBar.setMessage("Could not save Employees for the selected maintenance.","ERROR");
                     }
                 }
                 catch(Exception er){
                    er.printStackTrace();
                 }
             }
         }
         else{
             this.setVisible(false);
         }
    }

    private void btnAddL_actionPerformed(ActionEvent e) {
        dlgEA.setLocationRelativeTo(frame);
        dlgEA.populate(promised_date); 
        dlgEA.setVisible(true);
        /**Picking*/
        String info[] = dlgEA.getSelectedEmployeeInfo();
        if(info!=null){
          tblLabour.addRow();
          int row = tblLabour.getRowCount()-1;                  
          tblLabour.setValueAt(info[0],row,0);
          tblLabour.setValueAt(info[1],row,1);
          tblLabour.setValueAt(info[2],row,2);
          tblLabour.setValueAt(info[3],row,3);
          tblLabour.repaint();
          mcmbLeader.addItem(info[1]);
        }
    }

    private void btnRemoveL_actionPerformed(ActionEvent e) {
        int row = tblLabour.getSelectedRow();
        if(row!=-1){
            if(MessageBar.showConfirmDialog(frame,"Are you sure you want remove selected entry?","Labour Allocation")==MessageBar.YES_OPTION){
                try {
                    if(!tblLabour.getValueAt(row,8).toString().isEmpty()){
                        LabourUtilisationEntry lue = new LabourUtilisationEntry(connection);
                        lue.pm_work_order_id = WORK_ORDER;
                        lue.employee_id = tblLabour.getValueAt(row,1).toString();
                        lue.skill_category = tblLabour.getValueAt(row,0).toString();
                        lue.time_slot_id =tblLabour.getValueAt(row,8).toString();
                        lue.delete();
                    }
                    tblLabour.deleteRow(row);
                    setAllocatedAndRequiredHours();
                }
                catch (Exception er) {
                    messageBar.setMessage(er.getMessage(),"ERROR");
                }
            }
        }
        else{
            messageBar.setMessage("Select an entry first.","ERROR");
        }
    }

    public class LabourCategoryTable extends JTable {
         private LabourCategoryTableModel myModel = null ;

         public LabourCategoryTable() {
             myModel = new LabourCategoryTableModel();
             this.setModel(myModel); 
             
             this.setSelectionMode(0);
             this.getTableHeader().setReorderingAllowed(false);
             this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
             
             this.getColumnModel().getColumn(0).setPreferredWidth(100);
             this.getColumnModel().getColumn(1).setPreferredWidth(70);
             this.getColumnModel().getColumn(2).setPreferredWidth(50);
             this.getColumnModel().getColumn(3).setPreferredWidth(50);
             
             this.getColumnModel().getColumn(1).setCellRenderer(new TimeCellRenderer());
             this.getColumnModel().getColumn(2).setCellRenderer(new TimeCellRenderer());
             this.getColumnModel().getColumn(3).setCellRenderer(new TimeCellRenderer());
         }
         
         public void populate(String sql){
             myModel.deleteAll();
             try{
                 ResultSet rec = connection.createStatement().executeQuery(sql);
                 while(rec.next()){
                     myModel.addRow();
                     this.setValueAt(rec.getString("Maintenance_Category"),rec.getRow()-1,0);
                     this.setValueAt(rec.getString("Total"),rec.getRow()-1,1);
                 }
             }
             catch(Exception er){
                 er.printStackTrace();
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

         private class LabourCategoryTableModel extends AbstractTableModel{
             private int rowCount = 0;
             private String [] colNames = {"Category",//0
                                           "Planned",//1
                                           "Allocated",//2
                                           "Required"//3
                                           };
             private Object [][] valueArray = null;
             private Object [][] tempArray = null;


             public LabourCategoryTableModel() {
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
                 valueArray[valueArray.length-1][2] = "0";
             }

             public void deleteAll(){
                 valueArray = new Object[0][colNames.length];
             }
         }
         
         /**For formating time values in decimals*/
         class TimeCellRenderer extends JLabel implements TableCellRenderer{
            
            public TimeCellRenderer(){
                setOpaque(true);
            }
            
            public Component getTableCellRendererComponent(JTable table, 
                                                   Object value, 
                                                   boolean isSelected, 
                                                   boolean hasFocus, 
                                                   int row, int column){                                                       
              setText(Utilities.getDDHHMM(Integer.parseInt(value.toString())));
              
              this.setBackground(isSelected?table.getSelectionBackground():table.getBackground());
              this.setForeground(isSelected?table.getSelectionForeground():table.getForeground());
    
              return this;
            }
        }

} 
    
    public class LabourAllocationTable extends JTable {
        private LabourAllocationTableModel myModel = null ;

        public LabourAllocationTable() {
            myModel = new LabourAllocationTableModel();
            this.setModel(myModel);
            
            this.getTableHeader().setReorderingAllowed(false);
            this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.setSelectionMode(0);
            
            this.getColumnModel().getColumn(0).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(1).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(2).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(3).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(7).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(8).setCellRenderer(new LockedLabelCellRenderer());
            
            this.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new DateTimeCell(frame)));
            this.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(new StandardTimeCell(frame))); 
            this.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(new StandardTimeCell(frame))); 
            
            this.getColumnModel().getColumn(9).setCellRenderer(new ButtonCellRenderer());
            
            this.getColumnModel().getColumn(0).setPreferredWidth(60);
            this.getColumnModel().getColumn(1).setPreferredWidth(60);
            this.getColumnModel().getColumn(2).setPreferredWidth(200);
            this.getColumnModel().getColumn(3).setPreferredWidth(60);
            this.getColumnModel().getColumn(4).setPreferredWidth(110);
            this.getColumnModel().getColumn(5).setPreferredWidth(70);
            this.getColumnModel().getColumn(6).setPreferredWidth(65);
            this.getColumnModel().getColumn(7).setPreferredWidth(110);
            this.getColumnModel().getColumn(8).setPreferredWidth(60);
            this.getColumnModel().getColumn(9).setPreferredWidth(25);
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

        private class LabourAllocationTableModel extends AbstractTableModel{
            private int rowCount = 0;
            private String [] colNames = {"Category",     //0
                                          "Employee ID",  //1
                                          "Name",         //2
                                          "Title",        //3
                                          "Start Time",   //4
                                          "Setup Time",   //5 
                                          "Duration",     //6
                                          "End Time",     //7
                                          "Time Slot",    //8
                                          "Job",          //9
                                          "Machine",        //10
                                          "Task"};        //11
            private Object [][] valueArray = null;
            private Object [][] tempArray = null;


            public LabourAllocationTableModel() {
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
                if(col==4 || col==5 || col==6){
                    setEndTime(row);
                    tblLabour.repaint();
                }
            }
     
            public boolean isCellEditable(int r,int c){
                return ((c==4) || (c==5) || (c==6))?true:false;
            }
     
            public void addRow(){
                tempArray = new Object[this.getRowCount()+1][this.getColumnCount()];
                for(int y=0 ; y<valueArray.length; y++){
                    for(int x=0; x<valueArray[0].length; x++){
                        tempArray[y][x] =  valueArray[y][x];
                    }
                }
                valueArray = tempArray;
                valueArray[valueArray.length-1][4] = promised_date + " 00:00:00";
                valueArray[valueArray.length-1][5] = "0-0-0";
                valueArray[valueArray.length-1][6] = "0-0-0";
                valueArray[valueArray.length-1][7] = "0000-00-00 00:00:00";
                valueArray[valueArray.length-1][10] = asset_id;
                valueArray[valueArray.length-1][11] = "All";
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
            
            private void setEndTime(int row){
                String start = getValueAt(row,4).toString();
                String setup = getValueAt(row,5).toString();
                String duration = getValueAt(row,6).toString();
                setValueAt(getPlannedEndTime(start,setup,duration),row,7);
            }
            
            private String getPlannedEndTime(String start, String setup, String duration){
                String setupTimes[] = setup.split("-");
                String durationTimes[] = duration.split("-");
                        
                int days = Integer.parseInt(setupTimes[0]) + Integer.parseInt(durationTimes[0]);
                int hours = Integer.parseInt(setupTimes[1]) + Integer.parseInt(durationTimes[1]);
                int mins = Integer.parseInt(setupTimes[2]) + Integer.parseInt(durationTimes[2]);
                
                if(mins>=60){
                    hours++;
                    mins -= 60;
                }
                if(hours>=24){
                    days++;
                    hours -= 24;
                }
            
                String addition = days + " " + hours + ":" + mins + ":00";
                
                try{
                    String sql = "SELECT ADDTIME('" + start + "','" + addition + "')";
                    ResultSet rec = connection.createStatement().executeQuery(sql);
                    rec.next();
                    return rec.getString(1);
                }
                catch(Exception er){
                    //er.printStackTrace();
                    return "ERROR";
                }
            }
        }
    }
}