package com.matrix.focus.user;

import com.matrix.components.MTextbox;
import com.matrix.components.TitleBar;
import com.matrix.focus.util.MPanel;
import com.matrix.focus.mdi.MDI;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.DBConnectionPool;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

public class GUIUserProfile extends MPanel{
    private Connection connection;
    private JPanel jPanel1 = new JPanel();
    private JButton btnEdit = new JButton(new ImageIcon(ImageLibrary.BUTTON_EDIT));
    private JButton btnCancel = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
    private JButton btnSave = new JButton(new ImageIcon(ImageLibrary.BUTTON_SAVE));
    private MTextbox mtxtEmpID = new MTextbox();
    private MTextbox mtxtName = new MTextbox();
    private MTextbox mtxtTitle = new MTextbox();
    private MTextbox mtxtUserName = new MTextbox();
    private JLabel jLabel1 = new JLabel();
    private JLabel jLabel2 = new JLabel();
    private JPasswordField pwdConfirm = new JPasswordField();
    private JPasswordField pwd = new JPasswordField();
    private JPanel jPanel2 = new JPanel();
    private JScrollPane jsp = new JScrollPane();
    private MessageBar messageBar;
    private JFrame frame;
    private TitleBar titlebar = new TitleBar();
    private JPanel jPanel3 = new JPanel();
    private User user;
    private UserRolesTable tblUserRoles = new UserRolesTable();
    private JPasswordField curPswd = new JPasswordField();
    private JLabel jLabel3 = new JLabel();

    public GUIUserProfile(DBConnectionPool pool, JFrame frame, MessageBar msgBar){
        connection = pool.getConnection();    
        this.messageBar = msgBar;
        this.frame = frame;
        try {
            titlebar.setTitle("User Profile");
            titlebar.setDescription("The facility to view/edit user details.");
            titlebar.setImage(ImageLibrary.TITLE_USER_PROFILE);
            
            jbInit();
            setUser(MDI.USERNAME);
            setMode("SEARCH");
    
        } 
        catch(Exception e){
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
        btnEdit.setBounds(new Rectangle(75, 340, 100, 25));
        btnEdit.setSize(new Dimension(100, 25));
        btnEdit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnEdit_actionPerformed(e);
                    }
                });
        btnCancel.setText("Cancel");
        btnCancel.setBounds(new Rectangle(285, 340, 100, 25));
        btnCancel.setSize(new Dimension(100, 25));
        btnCancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnCancel_actionPerformed(e);
                    }
                });
        btnSave.setText("Save");
        btnSave.setBounds(new Rectangle(180, 340, 100, 25));
        btnSave.setSize(new Dimension(100, 25));
        btnSave.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnSave_actionPerformed(e);
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
        mtxtUserName.setEditable(false);
        jLabel1.setFont(new Font("Tahoma", 0, 11));
        jLabel2.setFont(new Font("Tahoma", 0, 11));
        mtxtEmpID.setTxtWidth(100);
        mtxtEmpID.setEditable(false);
        mtxtTitle.setTxtWidth(150);
        mtxtTitle.setEditable(false);
        jLabel1.setText("Password");
        jLabel1.setBounds(new Rectangle(30, 75, 85, 20));
        jLabel2.setText("Confirm Password");
        jLabel2.setBounds(new Rectangle(30, 100, 95, 25));
        pwdConfirm.setBounds(new Rectangle(125, 100, 100, 20));
        pwd.setBounds(new Rectangle(125, 75, 100, 20));
        jPanel2.setBounds(new Rectangle(395, 85, 550, 250));
        jPanel2.setLayout(null);
        jPanel2.setBorder(BorderFactory.createTitledBorder("User Roles"));
        jsp.setBounds(new Rectangle(10, 15, 530, 225));
        jsp.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        titlebar.setBounds(new Rectangle(10, 10, 935, 70));
        jPanel3.setBounds(new Rectangle(10, 200, 380, 135));
        jPanel3.setLayout(null);
        jPanel3.setBorder(BorderFactory.createTitledBorder("User Information"));
        curPswd.setBounds(new Rectangle(125, 50, 100, 20));
        jLabel3.setFont(new Font("Tahoma", 0, 11));
        jLabel3.setText("Current Password");
        jLabel3.setBounds(new Rectangle(30, 50, 95, 20));
        tblUserRoles.setEnabled(false);
        jsp.getViewport().add(tblUserRoles, null);
        jPanel2.add(jsp, null);
        jPanel3.add(jLabel3, null);
        jPanel3.add(curPswd, null);
        jPanel3.add(mtxtUserName, null);
        jPanel3.add(pwd, null);
        jPanel3.add(pwdConfirm, null);
        jPanel3.add(jLabel2, null);
        jPanel3.add(jLabel1, null);
        this.add(jPanel3, null);
        this.add(titlebar, null);
        this.add(jPanel2, null);
        this.add(btnSave, null);
        this.add(btnCancel, null);
        this.add(jPanel1, null);
        this.add(btnEdit, null);
        jPanel1.add(mtxtTitle, null);
        jPanel1.add(mtxtName, null);
        jPanel1.add(mtxtEmpID, null);
    }
    private void setMode(String mode){
        if(mode.equals("SEARCH")){
            btnEdit.setEnabled(true);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            setEditable(false);
        }
        else if(mode.equals("EDIT")){
            btnEdit.setEnabled(false);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
            setEditable(true);
        }
    }
    
    private void setEditable(boolean flag){
        curPswd.setEditable(flag);
        pwd.setEditable(flag);
        pwdConfirm.setEditable(flag);
    }

    private void btnSave_actionPerformed(ActionEvent e) {
        try{
            if(curPswd.getText().equals(Utilities.decrypt(user.password))){
                user.password = Utilities.encrypt(pwd.getText());
                user.confirm_password = Utilities.encrypt(pwdConfirm.getText());
                if(user.changePassword()){
                    setUser(user.username);
                    setMode("SEARCH");
                    messageBar.setMessage("Password changed successfully","OK");
                }
                else{
                    setUser(user.username);
                    messageBar.setMessage("Could not change the password","ERROR");
                }
            }
            else{
                setUser(user.username);
                messageBar.setMessage("Incorrect current pasword","ERROR");
            }
        }
        catch(Exception er){
            setUser(user.username);
            messageBar.setMessage(er.getMessage(),"ERROR");
        }
    }
    
    private void setUser(String username){
        try{
            user = new User(connection,username);
            mtxtEmpID.setInputText(user.employee_id);
            mtxtName.setInputText(user.name);
            mtxtTitle.setInputText(user.title);
            mtxtUserName.setInputText(user.username);
            tblUserRoles.populate(username);
        }
        catch(Exception er){
            setUser(MDI.USERNAME);
            er.printStackTrace();
        }
    }

    private void btnEdit_actionPerformed(ActionEvent e) {
        setMode("EDIT");
    }

    private void btnCancel_actionPerformed(ActionEvent e) {
        setMode("SEARCH");
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
