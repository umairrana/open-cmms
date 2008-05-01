package com.matrix.focus.master.gui;

import com.matrix.components.MDataCombo;
import com.matrix.components.MTextbox;
import com.matrix.components.TitleBar;
import com.matrix.focus.util.MPanel;
import com.matrix.focus.master.data.EmployeeData;
import com.matrix.focus.master.entity.Employee;
import com.matrix.focus.master.entity.EmployeeSkill;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.DataAssistantDialog;
import com.matrix.focus.util.ImageLibrary;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class GUIEmployee extends MPanel {
    
    private JPanel jPanel1 = new JPanel();
    private MTextbox mtxtEmpID = new MTextbox();
    private JButton btnSearch = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private MTextbox mtxtEmpName = new MTextbox();
    private MDataCombo mcmbJobTitle = new MDataCombo();
    private MTextbox mtxtEmail = new MTextbox();
    private MTextbox mtxtMobile = new MTextbox();
    private JPanel jPanel2 = new JPanel();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JButton btnCancel = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
    private JButton btnSave = new JButton(new ImageIcon(ImageLibrary.BUTTON_SAVE));
    private JButton btnDelete = new JButton(new ImageIcon(ImageLibrary.BUTTON_DELETE));
    private JButton btnAdd = new JButton(new ImageIcon(ImageLibrary.BUTTON_NEW));
    private JButton btnEdit = new JButton(new ImageIcon(ImageLibrary.BUTTON_EDIT));
    private SkillTable tblSkills = new SkillTable();
    private JButton btnAddSkill = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));
    private JButton btnRemoveSkill = new JButton(new ImageIcon(ImageLibrary.BUTTON_REMOVE));
    private JButton btnTitle = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));
    private MDataCombo mcmbType = new MDataCombo();
    private MDataCombo mcmbSkill = new MDataCombo();
    private JButton btnType = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));
    private JButton btnSkilLevel = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));
    private TitleBar titlebar = new TitleBar();
    
    private Connection connection;
    private JFrame frame;
    private MessageBar messageBar;
    private Employee employee;
      
    public GUIEmployee(DBConnectionPool pool, JFrame frame, MessageBar msgBar) {
        try {
            connection = pool.getConnection();
            this.frame = frame;
            this.messageBar = msgBar;
            
            titlebar.setTitle("Employee Information");
            titlebar.setDescription("The facility to manage employee information.");
            titlebar.setImage(ImageLibrary.TITLE_EMPLOYEES);
            
            jbInit();
            mcmbJobTitle.populate(connection,"SELECT DISTINCT title FROM employee");
            mcmbType.populate(connection,"SELECT DISTINCT employee_type FROM employee");
            mcmbSkill.populate(connection,"SELECT DISTINCT skill_level FROM employee");
                    
            setMode("LOAD");

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void close(){
        try {
            connection.close();
        } catch (SQLException e) {}
    }
    
    private void jbInit() throws Exception {
        this.setLayout( null );
        this.setSize(new Dimension(793, 482));
        jPanel1.setBounds(new Rectangle(10, 85, 485, 210));
        jPanel1.setBorder(BorderFactory.createTitledBorder("Employee Information"));
        jPanel1.setLayout(null);
        
        mtxtEmpID.setCaption("Employee ID");
        mtxtEmpID.setBounds(new Rectangle(25, 20, 215, 25));
        mtxtEmpID.setTxtWidth(120);
        mtxtEmpID.setLblFont(new Font("Tahoma", 0, 11));
        mtxtEmpID.setTxtFont(new Font("Tahoma", 0, 11));
        btnSearch.setBounds(new Rectangle(245, 20, 30, 20));
        btnSearch.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnSearch_actionPerformed(e);
            }
        });
        mtxtEmpName.setBounds(new Rectangle(25, 45, 345, 25));
        mtxtEmpName.setTxtWidth(250);
        mtxtEmpName.setCaption("Name");
        mtxtEmpName.setLblFont(new Font("Tahoma", 0, 11));
        mtxtEmpName.setTxtFont(new Font("Tahoma", 0, 11));
        mcmbJobTitle.setBounds(new Rectangle(25, 70, 290, 20));
        mcmbJobTitle.setCaption("Job Title");
        mcmbJobTitle.setLblWidth(95);
        mcmbJobTitle.setLblFont(new Font("Tahoma", 0, 11));
        mcmbJobTitle.setCmbFont(new Font("Tahoma", 0, 11));
        mtxtEmail.setBounds(new Rectangle(25, 145, 345, 20));
        mtxtEmail.setCaption("E-Mail ");
        mtxtEmail.setTxtWidth(250);
        mtxtEmail.setLblFont(new Font("Tahoma", 0, 11));
        mtxtEmail.setTxtFont(new Font("Tahoma", 0, 11));
        mtxtMobile.setBounds(new Rectangle(25, 170, 175, 20));
        mtxtMobile.setCaption("Mobile No");
        mtxtMobile.setTxtWidth(80);
        mtxtMobile.setLblFont(new Font("Tahoma", 0, 11));
        mtxtMobile.setTxtFont(new Font("Tahoma", 0, 11));
        jPanel2.setBounds(new Rectangle(10, 300, 485, 155));
        jPanel2.setBorder(BorderFactory.createTitledBorder("Employee Skills"));
        jPanel2.setLayout(null);
        jScrollPane1.setBounds(new Rectangle(10, 20, 465, 125));
        btnCancel.setText("Cancel");
        btnCancel.setBounds(new Rectangle(500, 210, 100, 25));
        btnCancel.setSize(new Dimension(100, 25));
        btnCancel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnCancel_actionPerformed(e);
            }
        });
        btnSave.setText("Save");
        btnSave.setBounds(new Rectangle(500, 180, 100, 25));
        btnSave.setSize(new Dimension(100, 25));
        btnSave.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnSave_actionPerformed(e);
            }
        });
        btnDelete.setText("Delete");
        btnDelete.setBounds(new Rectangle(500, 150, 100, 25));
        btnDelete.setSize(new Dimension(100, 25));
        btnDelete.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnDelete_actionPerformed(e);
            }
        });
        btnAdd.setText("New");
        btnAdd.setBounds(new Rectangle(500, 90, 100, 25));
        btnAdd.setSize(new Dimension(100, 25));
        btnAdd.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnAdd_actionPerformed(e);
            }
        });
        btnEdit.setText("Edit");
        btnEdit.setBounds(new Rectangle(500, 120, 100, 25));
        btnEdit.setSize(new Dimension(100, 25));
        btnEdit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnEdit_actionPerformed(e);
            }
        });
        btnType.setBounds(new Rectangle(320, 95, 30, 20));
        btnType.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        try{
                            String s = MessageBar.showInputDialog(frame,"Employee Type","Add New Employee Type");
                            if (!s.equals("")){
                              mcmbType.addItem(s);
                              mcmbType.setSelectedItem(s);  
                            }
                        }catch(Exception er){}
                    }
                });
        btnSkilLevel.setBounds(new Rectangle(320, 120, 30, 20));
        btnSkilLevel.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        try{
                            String s = MessageBar.showInputDialog(frame,"Skill Level","Add New Skill Level");
                            if (!s.equals("")){
                              mcmbSkill.addItem(s);
                              mcmbSkill.setSelectedItem(s);  
                            }
                        }catch(Exception er){}
                    }
                });
        titlebar.setBounds(new Rectangle(10, 10, 590, 70));
        mcmbSkill.setBounds(new Rectangle(25, 120, 290, 20));
        mcmbSkill.setCaption("Skill Level");
        mcmbSkill.setLblWidth(95);
        mcmbType.setBounds(new Rectangle(25, 95, 290, 20));
        mcmbType.setCaption("Type");
        mcmbType.setLblWidth(95);
        btnTitle.setBounds(new Rectangle(320, 70, 30, 20));
        btnTitle.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        try{
                            String s = MessageBar.showInputDialog(frame,"Job Title","Add New Job Title");
                            if (!s.equals("")){
                              mcmbJobTitle.addItem(s);
                              mcmbJobTitle.setSelectedItem(s);  
                            }
                        }catch(Exception er){}
                    }
                });
        btnAddSkill.setBounds(new Rectangle(500, 320, 30, 30));
        btnAddSkill.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    btnAddSkill_actionPerformed(e);
                }
        });
        btnRemoveSkill.setBounds(new Rectangle(500, 355, 30, 30));
        btnRemoveSkill.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    btnRemoveSkill_actionPerformed(e);
                }
        });
        jPanel1.add(btnTitle, null);
        jPanel1.add(mcmbSkill, null);
        jPanel1.add(mcmbType, null);
        jPanel1.add(btnSkilLevel, null);
        jPanel1.add(btnType, null);
        jPanel1.add(mtxtMobile, null);

        jPanel1.add(mtxtEmail, null);
        jPanel1.add(mcmbJobTitle, null);
        jPanel1.add(mtxtEmpName, null);
        jPanel1.add(btnSearch, null);
        jPanel1.add(mtxtEmpID, null);
        this.add(titlebar, null);
        this.add(btnDelete, null);
        this.add(btnSave, null);
        this.add(btnCancel, null);
        jScrollPane1.getViewport().add(tblSkills, null);
        jPanel2.add(jScrollPane1, null);
        this.add(jPanel2, null);
        this.add(jPanel1, null);
        this.add(btnAddSkill, null);
        this.add(btnRemoveSkill, null);
        this.add(btnAdd, null);
        this.add(btnEdit, null);
        jScrollPane1.getViewport().add(tblSkills, null);
    }

    private void btnSearch_actionPerformed(ActionEvent e){
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select Employee - Data Assistant","SELECT Employee_ID as 'Employee ID', name as Name  FROM employee WHERE deleted ='false'",connection);
        d.setLocationRelativeTo(frame);
        d.setVisible(true); 
        String rtnVal = d.getValue();
        mtxtEmpID.setInputText( (rtnVal.equals("") ? mtxtEmpID.getInputText() : rtnVal));
        if(!rtnVal.equals("")){
            setEmployee(rtnVal);
        }
    }
    
    private void btnAdd_actionPerformed(ActionEvent e){
        setMode("ADD");
        employee = new Employee();
    }
    
    private void btnEdit_actionPerformed(ActionEvent e){
        setMode("EDIT");
    }
    
    private void btnCancel_actionPerformed(ActionEvent e){
        setMode("LOAD");
    }
    
    private void btnSave_actionPerformed(ActionEvent e){
        saveUpdateEmployee();
    }
    
    private void btnAddSkill_actionPerformed(ActionEvent e){
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select Skill Category - Data Assistant","SELECT Maintenance_Category as 'Skill Category' FROM preventive_maintenance_category",connection);
        d.setFirstColumnWidth(320);
        d.setLocationRelativeTo(frame);
        d.setVisible(true); 
        String rtnVal = d.getValue();
        if(!rtnVal.equals("")){
            addSkill(rtnVal);
        }
    }
    
    private void addSkill(String pSkill){
        try{
            tblSkills.addRow();
            EmployeeSkill skill = new EmployeeSkill(mtxtEmpID.getInputText(),pSkill,tblSkills.getRowCount());
            employee.addEmployeeSkill(skill);
            tblSkills.setValueAt(pSkill,tblSkills.getRowCount()-1,0);
        }
        catch(Exception er){
            messageBar.setMessage(er.getMessage(),"ERROR");
        }
    }
    
    private void btnRemoveSkill_actionPerformed(ActionEvent e){
        int row = tblSkills.getSelectedRow();
        if(row!=-1){
            if(MessageBar.showConfirmDialog(frame,"Are sure you want to remove selected skill?","Employee Skills")==MessageBar.YES_OPTION){
                employee.removeEmployeeSkill(employee.getEmployeeSkill().get(row));
                tblSkills.deleteRow(tblSkills.getSelectedRow());
            }
        }
        else{
            messageBar.setMessage("Please select a skill first.","ERROR");
        }
    }
        
    private void btnDelete_actionPerformed(ActionEvent e){
        try{
            if(MessageBar.showConfirmDialog(frame,"Are you sure you want to delete \nthe selected employee from the system?","Employee")==MessageBar.YES_OPTION){
                EmployeeData.deleteEmployee(employee.getEmployeeID(),connection);
                setMode("LOAD");
                messageBar.setMessage("Employee deleted","OK");
            }
        } 
        catch (Exception er){
            messageBar.setMessage(er.getMessage(),"ERROR");
        }
    }
      
    private void saveUpdateEmployee(){
        try{
            employee.setEmployeeID(mtxtEmpID.getInputText());
            employee.setName(mtxtEmpName.getInputText());
            employee.setJobTitle(mcmbJobTitle.getSelectedItem().toString());
            employee.setType(mcmbType.getSelectedItem().toString());
            employee.setSkillLevel(mcmbSkill.getSelectedItem().toString());
            employee.setEmail(mtxtEmail.getInputText());
            employee.setMobilephone(mtxtMobile.getInputText());
            
            if(employee.isSaved()){
                EmployeeData.updateEmployee(employee,connection);
                messageBar.setMessage("Employee details updated","OK");
            }
            else{
                EmployeeData.saveEmployee(employee,connection);
                messageBar.setMessage("New employee saved","OK");
            }
            setMode("SEARCH");
        }
        catch (Exception ex){
            ex.printStackTrace();
            messageBar.setMessage(ex.getMessage(),"ERROR");
        }
    }
    
    private void setEmployee(String emp_id){
        try{
            employee = EmployeeData.getEmployee(emp_id,connection); 
            
            mtxtEmpName.setInputText(employee.getName()); 
            mcmbJobTitle.setSelectedItem(employee.getJobTitle());
            mcmbType.setSelectedItem(employee.getType());
            mcmbSkill.setSelectedItem(employee.getSkillLevel());
            mtxtEmail.setInputText(employee.getEmail());
            mtxtMobile.setInputText(employee.getMobilephone());
            Vector<EmployeeSkill> skills = employee.getEmployeeSkill();
            int rows = skills.size();
            EmployeeSkill skill = null;
            tblSkills.deleteAll();
            for(int i=0;i<rows;i++){
                skill = skills.get(i);
                tblSkills.addRow();
                tblSkills.setValueAt(skill.getSkill(),i,0);
                tblSkills.setValueAt(skill.getProficiency(),i,1);
            }
            setMode("SEARCH");
        }
        catch(Exception e){
            messageBar.setMessage(e.getMessage(),"ERROR");
        }
    }
    
    private void setMode(String mode){
        if(mode.equals("EDIT")){
            setEditable(true);
            mtxtEmpID.setEditable(false);
            btnAddSkill.setEnabled(true);
            btnRemoveSkill.setEnabled(true);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
            btnAdd.setEnabled(false);
            btnEdit.setEnabled(false);
            tblSkills.setEnabled(true);
        }
        else if(mode.equals("ADD")){
            clearAll();
            tblSkills.deleteAll();
            setEditable(true);
            btnAddSkill.setEnabled(true);
            btnRemoveSkill.setEnabled(true);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
            btnAdd.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            tblSkills.setEnabled(true);
        }
        else if(mode.equals("LOAD")){
            clearAll();
            tblSkills.deleteAll();
            btnAddSkill.setEnabled(false);
            btnRemoveSkill.setEnabled(false);
            btnAdd.setEnabled(true);
            btnSave.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            btnCancel.setEnabled(false);
            setEditable(false);
            tblSkills.setEnabled(false);
        }
        else if(mode.equals("SEARCH")){
            btnAddSkill.setEnabled(false);
            btnRemoveSkill.setEnabled(false);
            btnEdit.setEnabled(true);
            btnDelete.setEnabled(true);
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
            btnAdd.setEnabled(false);
            setEditable(false);
            tblSkills.setEnabled(false);
        }
    }
    
    private void setEditable(boolean value){
        mtxtEmpID.setEditable(value);
        mtxtEmpName.setEditable(value);
        mtxtMobile.setEditable(value);
        mtxtEmail.setEditable(value);
        mcmbJobTitle.getComboBox().setEnabled(value);
        mcmbSkill.getComboBox().setEnabled(value);
        mcmbType.getComboBox().setEnabled(value);
        btnTitle.setEnabled(value);
        btnType.setEnabled(value);
        btnSkilLevel.setEnabled(value);
    }
    
    private void clearAll(){
        mtxtEmpID.setInputText("");
        mtxtEmpName.setInputText("");   
        mtxtMobile.setInputText("");  
        mtxtEmail.setInputText("");
        mcmbJobTitle.getComboBox().setSelectedIndex(0);
        mcmbType.getComboBox().setSelectedIndex(0);
        mcmbSkill.getComboBox().setSelectedIndex(0);
        messageBar.clearMessage();
        employee = null;
    }

    /**SkillTable */
    public class SkillTable extends JTable{
      
      private SkillTableModel myModel = null ;
      
      public SkillTable() {
          myModel = new SkillTableModel();
          this.setModel(myModel);
          this.getColumnModel().getColumn(0).setPreferredWidth(400);
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
          myModel.deleteRow(i);
          this.tableChanged(new TableModelEvent(myModel));        
      } 
      public void deleteAll(){
          myModel.deleteAll();
          this.tableChanged(new TableModelEvent(myModel));        
      }
      
      private class SkillTableModel extends AbstractTableModel{
        private String [] colNames = {"Skill Category","Skill Priority"};
        private Object [][] valueArray = null;
        private Object [][] tempArray = null;
        
        public SkillTableModel() {
            valueArray = new Object[0][colNames.length];
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
            return valueArray[r][c];
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
          valueArray = new Object[0][2];
        }
        
      }     
    }/**END*/
}