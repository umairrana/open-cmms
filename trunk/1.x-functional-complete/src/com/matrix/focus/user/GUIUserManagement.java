package com.matrix.focus.user;

import com.matrix.components.MTextbox;
import com.matrix.components.TitleBar;
import com.matrix.focus.util.MPanel;
import com.matrix.focus.mdi.MDI;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.DataAssistantDialog;
import com.matrix.focus.util.ImageLibrary;
import com.matrix.focus.util.Utilities;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

public class GUIUserManagement extends MPanel{
    private Connection connection;
    private JPanel jPanel1 = new JPanel();
    private JButton btnEdit = new JButton(new ImageIcon(ImageLibrary.BUTTON_EDIT));
    private JButton btnCancel = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
    private JButton btnSave = new JButton(new ImageIcon(ImageLibrary.BUTTON_SAVE));
    private JButton btnDelete = new JButton(new ImageIcon(ImageLibrary.BUTTON_DELETE));
    private MTextbox mtxtEmpID = new MTextbox();
    private MTextbox mtxtName = new MTextbox();
    private MTextbox mtxtTitle = new MTextbox();
    private MTextbox mtxtUserName = new MTextbox();
    private JLabel jLabel1 = new JLabel();
    private JLabel jLabel2 = new JLabel();
    private JLabel jLabel3 = new JLabel();
    private JRadioButton rbYes = new JRadioButton();
    private JRadioButton rbNo = new JRadioButton();
    private JPasswordField pwdConfirm = new JPasswordField();
    private JPasswordField pwd = new JPasswordField();
    private JButton btnEmpSearch = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton btnAddSkill = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));
    private JButton btnRemoveSkill = new JButton(new ImageIcon(ImageLibrary.BUTTON_REMOVE));
    private JPanel jPanel2 = new JPanel();
    private JScrollPane jsp = new JScrollPane();
    private ButtonGroup bg = new ButtonGroup();
    private MessageBar messageBar;
    private JFrame frame;
    private JButton btnAdd = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));
    private TitleBar titlebar = new TitleBar();
    private JPanel jPanel3 = new JPanel();
    private JButton btnUsername = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private MTextbox mtxtDate = new MTextbox();
    private User user;
    private MTextbox mtxtCreater = new MTextbox();
    private boolean IS_NEW_USER;
    private UserRolesTable tblUserRoles = new UserRolesTable();

    public GUIUserManagement(DBConnectionPool pool, JFrame frame, MessageBar msgBar){
        connection = pool.getConnection();    
        this.messageBar = msgBar;
        this.frame = frame;
        try {
            titlebar.setTitle("User Management");
            titlebar.setDescription("The facility to manage system users.");
            titlebar.setImage(ImageLibrary.TITLE_USER_MANAGEMENT);
            
            jbInit();
            setMode("LOAD");
        } catch (Exception e) {
            e.printStackTrace();
        }
  }
  

    private void jbInit() throws Exception {
        this.setLayout(null);
        this.setSize(new Dimension(990, 564));
        jPanel1.setBounds(new Rectangle(10, 85, 380, 115));
        jPanel1.setLayout(null);
        jPanel1.setBorder(BorderFactory.createTitledBorder("Employee Information"));
        btnEdit.setText("Edit");
        btnEdit.setBounds(new Rectangle(625, 390, 100, 25));
        btnEdit.setSize(new Dimension(100, 25));
        btnEdit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnEdit_actionPerformed(e);
                    }
                });
        btnCancel.setText("Cancel");
        btnCancel.setBounds(new Rectangle(835, 390, 100, 25));
        btnCancel.setSize(new Dimension(100, 25));
        btnCancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnCancel_actionPerformed(e);
                    }
                });
        btnSave.setText("Save");
        btnSave.setBounds(new Rectangle(730, 390, 100, 25));
        btnSave.setSize(new Dimension(100, 25));
        btnSave.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnSave_actionPerformed(e);
                    }
                });
        btnDelete.setText("Delete");
        btnDelete.setBounds(new Rectangle(520, 390, 100, 25));
        btnDelete.setSize(new Dimension(100, 25));
        btnDelete.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnDelete_actionPerformed(e);
                    }
                });
        mtxtEmpID.setBounds(new Rectangle(30, 25, 300, 20));
        mtxtEmpID.setCaption("Employee ID");
        mtxtName.setBounds(new Rectangle(30, 50, 300, 20));
        mtxtName.setCaption("Name");
        mtxtName.setEditable(false);
        mtxtTitle.setBounds(new Rectangle(30, 75, 305, 20));
        mtxtTitle.setCaption("Title");
        mtxtUserName.setBounds(new Rectangle(30, 25, 300, 20));
        mtxtUserName.setCaption("User Name");
        mtxtUserName.setTxtWidth(150);
        jLabel1.setFont(new Font("Tahoma", 0, 11));
        jLabel2.setFont(new Font("Tahoma", 0, 11));
        jLabel3.setFont(new Font("Tahoma", 0, 11));
        btnAddSkill.setBounds(new Rectangle(950, 100, 30, 30));
        btnAddSkill.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnAddSkill_actionPerformed(e);
                    }
                });
        btnRemoveSkill.setBounds(new Rectangle(950, 135, 30, 30));
        btnRemoveSkill.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnRemoveSkill_actionPerformed(e);
                    }
                });
        mtxtEmpID.setTxtWidth(100);
        mtxtEmpID.setEditable(false);
        mtxtTitle.setTxtWidth(150);
        mtxtTitle.setEditable(false);
        jLabel1.setText("Password");
        jLabel1.setBounds(new Rectangle(30, 50, 85, 20));
        jLabel2.setText("Confirm Password");
        jLabel2.setBounds(new Rectangle(30, 75, 95, 25));
        jLabel3.setText("Active");
        jLabel3.setBounds(new Rectangle(30, 100, 85, 20));
        rbYes.setText("Yes");
        rbYes.setBounds(new Rectangle(125, 100, 45, 20));
        rbYes.setSelected(true);
        rbNo.setText("No");
        rbNo.setBounds(new Rectangle(175, 100, 40, 20));
        bg.add(rbYes);
        bg.add(rbNo);
        pwdConfirm.setBounds(new Rectangle(125, 75, 100, 20));
        pwd.setBounds(new Rectangle(125, 50, 100, 20));
        btnEmpSearch.setBounds(new Rectangle(230, 25, 30, 20));
        btnEmpSearch.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnSearch_actionPerformed(e);
                    }
                });
        jPanel2.setBounds(new Rectangle(395, 85, 550, 300));
        jPanel2.setLayout(null);
        jPanel2.setBorder(BorderFactory.createTitledBorder("User Roles"));
        jsp.setBounds(new Rectangle(10, 15, 530, 275));
        jsp.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        btnAdd.setText("Add ");
        btnAdd.setBounds(new Rectangle(415, 390, 100, 25));
        btnAdd.setSize(new Dimension(100, 25));
        btnAdd.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnAdd_actionPerformed(e);
                    }
                });
        titlebar.setBounds(new Rectangle(10, 10, 935, 70));
        jPanel3.setBounds(new Rectangle(10, 200, 380, 185));
        jPanel3.setLayout(null);
        jPanel3.setBorder(BorderFactory.createTitledBorder("User Information"));
        btnUsername.setBounds(new Rectangle(280, 25, 30, 20));
        btnUsername.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnUsername_actionPerformed(e);
                    }
                });
        mtxtDate.setBounds(new Rectangle(30, 125, 215, 20));
        mtxtDate.setCaption("Created Date");
        mtxtDate.setTxtWidth(120);
        mtxtDate.setEditable(false);
        mtxtCreater.setBounds(new Rectangle(30, 150, 305, 20));
        mtxtCreater.setCaption("Creater");
        mtxtCreater.setEditable(false);
        jsp.getViewport().add(tblUserRoles, null);
        jPanel2.add(jsp, null);
        jPanel3.add(mtxtCreater, null);
        jPanel3.add(mtxtDate, null);
        jPanel3.add(btnUsername, null);
        jPanel3.add(mtxtUserName, null);
        jPanel3.add(pwd, null);
        jPanel3.add(pwdConfirm, null);
        jPanel3.add(jLabel2, null);
        jPanel3.add(jLabel1, null);
        jPanel3.add(jLabel3, null);
        jPanel3.add(rbYes, null);
        jPanel3.add(rbNo, null);
        this.add(jPanel3, null);
        this.add(titlebar, null);
        this.add(jPanel2, null);
        this.add(btnDelete, null);
        this.add(btnSave, null);
        this.add(btnCancel, null);
        this.add(jPanel1, null);
        this.add(btnEdit, null);
        this.add(btnAdd, null);
        this.add(btnAddSkill, null);
        this.add(btnRemoveSkill, null);
        jPanel1.add(btnEmpSearch, null);
        jPanel1.add(mtxtTitle, null);
        jPanel1.add(mtxtName, null);
        jPanel1.add(mtxtEmpID, null);
    }
    private void setMode(String mode){
        if(mode.equals("ADD")){
            IS_NEW_USER = true;
            btnAdd.setEnabled(false);
            btnEmpSearch.setEnabled(true);
            btnDelete.setEnabled(false);
            btnEdit.setEnabled(false);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
            setEditable(true);
            clearAll();
        }
        else if(mode.equals("SEARCH")){
            IS_NEW_USER = false;
            btnAdd.setEnabled(false);
            btnEmpSearch.setEnabled(false);
            btnDelete.setEnabled(true);
            btnEdit.setEnabled(true);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            setEditable(false);
        }
        else if(mode.equals("EDIT")){
            IS_NEW_USER = false;
            btnAdd.setEnabled(false);
            btnEmpSearch.setEnabled(false);
            btnDelete.setEnabled(false);
            btnEdit.setEnabled(false);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
            setEditable(true);
        }
        else if(mode.equals("LOAD")){
            IS_NEW_USER = false;
            btnAdd.setEnabled(true);
            btnEmpSearch.setEnabled(false);
            btnDelete.setEnabled(false);
            btnEdit.setEnabled(false);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(false);
            clearAll();
            setEditable(false);
        }
    }
    
    private void setEditable(boolean flag){
        mtxtUserName.setEditable(flag);
        mtxtEmpID.setEditable(flag);
        mtxtName.setEditable(flag);
        mtxtTitle.setEditable(flag);
        pwd.setEditable(flag);
        pwdConfirm.setEditable(flag);
        tblUserRoles.setEnabled(flag);
        btnAddSkill.setEnabled(flag);
        btnRemoveSkill.setEnabled(flag);
    }
    
    private void clearAll(){
        mtxtEmpID.setInputText("");
        mtxtName.setInputText("");
        mtxtTitle.setInputText("");
        mtxtUserName.setInputText("");
        pwd.setText("");
        pwdConfirm.setText("");
        mtxtDate.setInputText("");
        mtxtCreater.setInputText("");
        tblUserRoles.deleteAll();
    }
    
    private void btnSearch_actionPerformed(ActionEvent e) {
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select Employee - Data Assistant","SELECT Employee_ID as 'Employee ID', Name, Title FROM employee WHERE deleted ='false' ORDER BY Employee_ID",connection);
        d.setLocationRelativeTo(frame);
        d.setSecondColumnWidth(150);
        d.setVisible(true); 
        String rtnVal = d.getValue();
        if(!rtnVal.equals("")){
            mtxtEmpID.setInputText(rtnVal);
            mtxtName.setInputText(d.getDescription());
            mtxtTitle.setInputText(d.getThirdValue());
            String username = getUsername(rtnVal);
            if(!username.equals("")){
                setUser(username);
                setMode("SEARCH");
            }
            else{
                mtxtUserName.setInputText("");
                pwd.setText("");
                pwdConfirm.setText("");
                mtxtDate.setInputText("");
                mtxtCreater.setInputText("");
            }
        }
    }
    
    private String getUsername(String employee_id){
        try{
            String sql = "SELECT username FROM user WHERE Employee_ID ='"+ employee_id +"'";
            ResultSet rec = connection.createStatement().executeQuery(sql);
            rec.next();
            return rec.getString("username");
        } 
        catch(Exception ex){
            //ex.printStackTrace();
            return "";
        } 
        
    }
    
    private void btnAdd_actionPerformed(ActionEvent e) {
        setMode("ADD");
    }

    private void btnSave_actionPerformed(ActionEvent e) {
        if(IS_NEW_USER){
            user = new User(connection);
        }
        user.employee_id = mtxtEmpID.getInputText();
        user.name = mtxtName.getInputText();
        user.title = mtxtTitle.getInputText();
        user.username = mtxtUserName.getInputText();
        user.password = Utilities.encrypt(pwd.getText());
        user.confirm_password = Utilities.encrypt(pwdConfirm.getText());
        user.active = (rbYes.isSelected()?"true":"false");
        user.creater = MDI.USERNAME;
        
        try{
            if(IS_NEW_USER){
                if(user.save() && saveUserRoles()){
                    setUser(user.username);
                    setMode("SEARCH");
                    messageBar.setMessage("New user saved successfully","OK");
                }
                else{
                    messageBar.setMessage("Could not save new user","ERROR");
                }
            }
            else{
                if(user.update()&& saveUserRoles()){
                    setUser(user.username);
                    setMode("SEARCH");
                    messageBar.setMessage("User details updated successfully","OK");
                }
                else{
                    messageBar.setMessage("Could not update user details","ERROR");
                }
            }
        }
        catch(Exception er){
            messageBar.setMessage(er.getMessage(),"ERROR");
        }
    }

    private void btnUsername_actionPerformed(ActionEvent e) {
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select User - Data Assistant","SELECT u.Employee_ID as 'Employee ID', u.Username, u.Employee_Name as 'Name' FROM user u WHERE u.deleted ='false' ORDER BY u.Employee_ID",connection);
        d.setLocationRelativeTo(frame);
        d.setSecondColumnWidth(80);
        d.setThirdColumnWidth(150);
        d.setVisible(true); 
        String rtnVal = d.getValue();
        if(!rtnVal.equals("")){
            mtxtUserName.setInputText(d.getDescription());
            setUser(d.getDescription());
            setMode("SEARCH");
        }
    }
    
    private void setUser(String username){
        try{
            user = new User(connection,username);
            mtxtEmpID.setInputText(user.employee_id);
            mtxtName.setInputText(user.name);
            mtxtTitle.setInputText(user.title);
            mtxtUserName.setInputText(user.username);
            pwd.setText(Utilities.decrypt(user.password));
            pwdConfirm.setText(Utilities.decrypt(user.password));
            mtxtDate.setInputText(user.date_created);
            mtxtCreater.setInputText(user.creater);
            if(Boolean.parseBoolean(user.active)){
                rbYes.setSelected(true);
            }
            else{
                rbNo.setSelected(true);
            }
            tblUserRoles.populate(username);
        }
        catch(Exception er){
            er.printStackTrace();
        }
    }

    private void btnEdit_actionPerformed(ActionEvent e) {
        setMode("EDIT");
    }

    private void btnCancel_actionPerformed(ActionEvent e) {
        setMode("LOAD");
    }

    private void btnDelete_actionPerformed(ActionEvent e) {
        if(MessageBar.showConfirmDialog(frame,"Are you sure you want to delete \nthe selected user from the system?","Delete User")==MessageBar.YES_OPTION){
          if(user.delete()){
            setMode("LOAD");
            messageBar.setMessage("User deleted successfully","OK");
          }
          else{
            messageBar.setMessage("Error while deleting the user","ERROR");
          }
        }
    }

    private void btnAddSkill_actionPerformed(ActionEvent e) {
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select User Role - Data Assistant","SELECT role_name AS 'Role Name', Description FROM role",connection);
        d.setLocationRelativeTo(frame);
        d.setFirstColumnWidth(150);
        d.setSecondColumnWidth(330);
        d.grow(500,365,480);
        d.setVisible(true); 
        String rtnVal = d.getValue();
        if(!rtnVal.equals("")){
            if(!isRoleExist(rtnVal)){
                addRole(rtnVal,d.getDescription());
            }
            else{
                messageBar.setMessage("Selected role already exists","ERROR");
            }
        }
    }
    
    private boolean isRoleExist(String role){
        for(int i=0;i<tblUserRoles.getRowCount();i++){
            if(role.equals(tblUserRoles.getValueAt(i,0).toString())){
                return true;
            }
        }
        return false;
    }
    
    private void addRole(String role, String desc){
        tblUserRoles.addRow();
        tblUserRoles.setValueAt(role,tblUserRoles.getRowCount()-1,0);
        tblUserRoles.setValueAt(desc,tblUserRoles.getRowCount()-1,1);
        tblUserRoles.setValueAt(true,tblUserRoles.getRowCount()-1,2);
        tblUserRoles.setValueAt("Today",tblUserRoles.getRowCount()-1,3);
        tblUserRoles.setValueAt(MDI.USERNAME,tblUserRoles.getRowCount()-1,4);
    }
    
    private boolean saveUserRoles(){
        /**Using the same object does not make error since the key attribute are changing in the loop*/
        UserRole userrole = new UserRole(connection);
        for(int i=0;i<tblUserRoles.getRowCount();i++){
            userrole.username = mtxtUserName.getInputText();
            userrole.role_name = tblUserRoles.getValueAt(i,0).toString();
            userrole.active = tblUserRoles.getValueAt(i,2).toString();
            userrole.creater = tblUserRoles.getValueAt(i,4).toString();
            if(userrole.save()){
                continue;
            }
            else if(userrole.update()){
                continue;
            }
            else{
                return false;
            }
        }
        return true;
    }

    private void btnRemoveSkill_actionPerformed(ActionEvent e) {
        int row = tblUserRoles.getSelectedRow();
        if(row!=-1){
            if(MessageBar.showConfirmDialog(frame,"Are you sure you want to delete \nthe selected role from the user?","Delete Role")==MessageBar.YES_OPTION){
              UserRole ur = new UserRole(connection);
              ur.username = mtxtUserName.getInputText();
              ur.role_name = tblUserRoles.getValueAt(row,0).toString();
              if(ur.delete()){
                tblUserRoles.deleteRow(row);
                messageBar.setMessage("User role deleted successfully","OK");
              }
              else{
                messageBar.setMessage("Error while deleting the role","ERROR");
              }
            }
        }
        else{
            messageBar.setMessage("Please select a role first","ERROR");
        }
    }

    public class UserRolesTable extends JTable {
        private URTableModel myModel = null ;
    
        public UserRolesTable(){
            myModel = new URTableModel();
            this.setModel(myModel);
            this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.getTableHeader().setReorderingAllowed(false);
            this.setSelectionMode(0);
            
            this.getColumnModel().getColumn(0).setPreferredWidth(150);
            this.getColumnModel().getColumn(1).setPreferredWidth(250);
            this.getColumnModel().getColumn(2).setPreferredWidth(40);
            this.getColumnModel().getColumn(3).setPreferredWidth(100);
            this.getColumnModel().getColumn(4).setPreferredWidth(100);
        }
        
        public void populate(String username){
            try{
                deleteAll();
                String sql = "SELECT u.Role_Name, r.Description, u.Active, u.Date_Assigned, u.Creater FROM user_role u, role r WHERE u.Role_Name = r.Role_Name AND u.Username ='"+ username + "'";
                ResultSet rec = connection.createStatement().executeQuery(sql);
                while(rec.next()){
                    addRow();
                    setValueAt(rec.getString("Role_Name"),getRowCount()-1,0);
                    setValueAt(rec.getString("Description"),getRowCount()-1,1);
                    setValueAt(rec.getBoolean("Active"),getRowCount()-1,2);
                    setValueAt(rec.getString("Date_Assigned").substring(0,19),getRowCount()-1,3);
                    setValueAt(rec.getString("Creater"),getRowCount()-1,4);
                }
            } 
            catch(Exception ex){
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
    
        private class URTableModel extends AbstractTableModel{
            private int rowCount = 0;
            private String [] colNames = {"Role Name","Description","Active","Assigned Date","Assiged By"};
            private Object [][] valueArray = null;
            private Object [][] tempArray = null;
        
            public URTableModel() {
                valueArray = new Object[rowCount][colNames.length];
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
                return (c==2);
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
                    //Do not press Delete while it has no elements
                }
            } 
    
            public void deleteAll(){
                valueArray = new Object[0][colNames.length];
            }
        }
    }
    
}
