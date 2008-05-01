package com.matrix.focus.master.gui;

import com.matrix.components.TitleBar;
import com.matrix.focus.master.entity.Division;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.DataAssistantDialog;
import java.sql.Connection;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JScrollPane;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JButton;
import com.matrix.components.MTextbox;
import com.matrix.focus.connect.SyncCustomer;
import com.matrix.focus.connect.SyncCustomerContact;
import com.matrix.focus.util.MPanel;
import com.matrix.focus.master.data.DivisionData;
import com.matrix.focus.mdi.MDI;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.ImageLibrary;

import java.awt.Component;
import java.awt.Cursor;

import javax.swing.JTable;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

public class GUIDivision extends MPanel{

    private JPanel jPanel1 = new JPanel();
    private MTextbox mtxtDivisionID = new MTextbox();
    private MTextbox mtxtEmail = new MTextbox();
    private MTextbox mtxtEmail1 = new MTextbox();
    private MTextbox mtxtContactPerson = new MTextbox();
    private MTextbox mtxtFaxNo = new MTextbox();
    private MTextbox mtxtTelephoneNo = new MTextbox();
    private MTextbox mtxtName = new MTextbox();
    private JTextArea taRemarks = new JTextArea();
    private JLabel jLabel2 = new JLabel();
    private JButton btnCancel = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
    private JButton btnSave = new JButton(new ImageIcon(ImageLibrary.BUTTON_SAVE));
    private JButton btnDelete = new JButton(new ImageIcon(ImageLibrary.BUTTON_DELETE));
    private JButton btnAdd = new JButton(new ImageIcon(ImageLibrary.BUTTON_NEW));
    private JButton btnEdit = new JButton(new ImageIcon(ImageLibrary.BUTTON_EDIT));
    private JButton btnSearchDivi = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton btnMasterRefresh = new JButton(new ImageIcon(ImageLibrary.BUTTON_REFRESH));
    private JButton btnContactMasterRefresh = new JButton(new ImageIcon(ImageLibrary.BUTTON_REFRESH));
    private JScrollPane jScrollPane1 = new JScrollPane();
    private TitleBar titlebar = new TitleBar();

    private Division division;
    private Connection connection;
    private MDI frame;
    private MessageBar messageBar;
    private JTextArea textAreaAddress = new JTextArea();
    private JScrollPane scrollPaneAddress = new JScrollPane();
    private JLabel jLabel1 = new JLabel();
    private MTextbox mtxtDesignation = new MTextbox();
    private MTextbox mtxtTravelTime = new MTextbox();
    private JPanel jPanel2 = new JPanel();
    private JScrollPane jScrollPane2 = new JScrollPane();
    private ContactPersonsTable tblPerons = new ContactPersonsTable();
    
    private final Cursor hgCursor = new Cursor(Cursor.WAIT_CURSOR); // cursor hour glass
    private final Cursor defCursor = new Cursor(Cursor.DEFAULT_CURSOR); // cursor default



    public GUIDivision(DBConnectionPool pool, MDI frame, MessageBar msgBar){
        connection = pool.getConnection();
        this.frame = frame;
        this.messageBar = msgBar;
        try{
            titlebar.setTitle("Customer Information");
            titlebar.setDescription("The facility to manage customer information.");
            titlebar.setImage(ImageLibrary.TITLE_DIVISION);
            jbInit();
            setMode("LOAD");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception{
        this.setLayout(null);
        this.setSize(new Dimension(951, 576));
        jPanel1.setBounds(new Rectangle(10, 85, 940, 450));
        jPanel1.setBorder(BorderFactory.createTitledBorder("Customer Information"));
        jPanel1.setLayout(null);
        mtxtDivisionID.setBounds(new Rectangle(20, 30, 195, 20));
        mtxtDivisionID.setTxtWidth(100);
        mtxtDivisionID.setCaption("Customer ID");
        mtxtDivisionID.setLblFont(new Font("Tahoma", 0, 11));
        mtxtEmail.setBounds(new Rectangle(545, 220, 315, 20));
        mtxtEmail1.setBounds(new Rectangle(0, 0, 1, 1));
        mtxtEmail1.setBackground(new Color(230, 230, 230));
        mtxtContactPerson.setBounds(new Rectangle(545, 120, 300, 20));
        mtxtFaxNo.setBounds(new Rectangle(545, 195, 225, 20));
        mtxtTelephoneNo.setBounds(new Rectangle(545, 170, 225, 20));
        mtxtName.setBounds(new Rectangle(20, 55, 380, 20));
        mtxtEmail.setCaption("Email Address");
        mtxtEmail.setLblFont(new Font("Tahoma", 0, 11));
        mtxtEmail1.setCaption("Email Address");
        mtxtContactPerson.setCaption("Contact Person");
        mtxtContactPerson.setLblFont(new Font("Tahoma", 0, 11));
        mtxtFaxNo.setCaption("Fax No.");
        mtxtFaxNo.setTxtWidth(130);
        mtxtFaxNo.setLblFont(new Font("Tahoma", 0, 11));
        mtxtTelephoneNo.setCaption("Telephone No.");
        mtxtTelephoneNo.setTxtWidth(130);
        mtxtTelephoneNo.setLblFont(new Font("Tahoma", 0, 11));
        mtxtName.setCaption("Customer Name");
        mtxtName.setLblFont(new Font("Tahoma", 0, 11));
        mtxtName.setTxtWidth(300);
        jLabel2.setText("Remarks");
        jLabel2.setBounds(new Rectangle(20, 190, 75, 20));
        jLabel2.setFont(new Font("Tahoma", 0, 11));
        btnCancel.setText("Cancel");
        btnCancel.setBounds(new Rectangle(430, 540, 100, 25));
        btnCancel.setSize(new Dimension(100, 25));
        btnCancel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnCancel_actionPerformed(e);
            }
        });
        btnSave.setText("Save");
        btnSave.setBounds(new Rectangle(325, 540, 100, 25));
        btnSave.setSize(new Dimension(100, 25));
        btnSave.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnSave_actionPerformed(e);
            }
        });
        btnDelete.setText("Delete");
        btnDelete.setBounds(new Rectangle(220, 540, 100, 25));
        btnDelete.setSize(new Dimension(100, 25));
        btnDelete.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnDelete_actionPerformed(e);
            }
        });
        btnAdd.setText("New");
        btnAdd.setBounds(new Rectangle(10, 540, 100, 25));
        btnAdd.setSize(new Dimension(100, 25));
        btnAdd.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnAdd_actionPerformed(e);
            }
        });
        btnEdit.setText("Edit");
        btnEdit.setBounds(new Rectangle(115, 540, 100, 25));
        btnEdit.setSize(new Dimension(100, 25));
        btnEdit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnEdit_actionPerformed(e);
            }
        });
        btnSearchDivi.setBounds(new Rectangle(220, 30, 30, 20));
        btnSearchDivi.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnSearchDivi_actionPerformed(e);
            }
        });
        jScrollPane1.setBounds(new Rectangle(115, 190, 380, 50));
        titlebar.setBounds(new Rectangle(10, 10, 940, 70));
        textAreaAddress.setEditable(false);
        scrollPaneAddress.setBounds(new Rectangle(115, 80, 265, 80));
        jLabel1.setText("Address");
        jLabel1.setBounds(new Rectangle(20, 80, 90, 20));
        mtxtDesignation.setBounds(new Rectangle(545, 145, 245, 20));
        mtxtDesignation.setCaption("Designation");
        mtxtDesignation.setEditable(false);
        mtxtDesignation.setTxtWidth(150);
        mtxtTravelTime.setBounds(new Rectangle(20, 165, 295, 20));
        mtxtTravelTime.setCaption("Travel Time");
        mtxtTravelTime.setEditable(false);
        jPanel2.setBounds(new Rectangle(10, 250, 920, 190));
        jPanel2.setBorder(BorderFactory.createTitledBorder("Contact Persons"));
        jPanel2.setLayout(null);
        jScrollPane2.setBounds(new Rectangle(10, 20, 875, 160));
        btnContactMasterRefresh.setBounds(new Rectangle(886, 20, 30, 20));
        btnContactMasterRefresh.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnContactMasterRefresh_actionPerformed(e);
                    }
                });
        btnMasterRefresh.setBounds(new Rectangle(255, 30, 30, 20));
        btnMasterRefresh.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnMasterRefresh_actionPerformed(e);
                    }
                });
        jPanel1.add(btnMasterRefresh, null);
        jPanel1.add(mtxtTravelTime, null);
        jPanel1.add(mtxtDesignation, null);
        jPanel1.add(jLabel1, null);
        scrollPaneAddress.getViewport().add(textAreaAddress, null);
        jPanel1.add(scrollPaneAddress, null);
        jPanel1.add(btnSearchDivi, null);
        jPanel1.add(jLabel2, null);
        jPanel1.add(mtxtName, null);
        jPanel1.add(mtxtTelephoneNo, null);
        jPanel1.add(mtxtFaxNo, null);
        jPanel1.add(mtxtContactPerson, null);
        mtxtEmail.add(mtxtEmail1, null);
        jPanel1.add(mtxtEmail, null);
        jPanel1.add(mtxtDivisionID, null);
        jScrollPane1.getViewport().add(taRemarks, null);
        jPanel1.add(jScrollPane1, null);
        jScrollPane2.getViewport().add(tblPerons, null);
        jPanel2.add(jScrollPane2, null);
        jPanel2.add(btnContactMasterRefresh, null);
        jPanel1.add(jPanel2, null);
        jPanel1.add(mtxtEmail1, null);
        this.add(titlebar, null);
        this.add(btnDelete, null);
        this.add(btnSave, null);
        this.add(btnCancel, null);
        this.add(jPanel1, null);
        this.add(btnAdd, null);
        this.add(btnEdit, null);
    }

    private void btnSearchDivi_actionPerformed(ActionEvent e){
    
        e.toString();
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select Customer - Data Assistant","SELECT division_id as 'Customer ID', Name  FROM division WHERE comp_id ='Matrix' AND dept_id ='Customers' AND deleted !='true'",connection);
        d.setLocationRelativeTo(jPanel1);
        d.setVisible(true); 
        String rtnVal = d.getValue();
        if(!rtnVal.isEmpty()){
           // String comp = "Matrix";
           // String dept = "Customers";
          
            clearAll();
            //mtxtCompanyID.setInputText(comp);
            //mtxtDepartmentID.setInputText(dept);
            mtxtDivisionID.setInputText(rtnVal);
            
            setDivision(rtnVal);
        }
    }
  
    private void btnSave_actionPerformed(ActionEvent e){
        e.toString();
        saveUpdateDivision();
    }
  
    private void btnDelete_actionPerformed(ActionEvent e){
        e.toString();
        try{
            if(MessageBar.showConfirmDialog(frame,"Are you sure you want to delete \nthe selected Division from the system?","Division")==MessageBar.YES_OPTION){
                DivisionData.deleteDivision(division.getCompany(),division.getDepartment(),division.getID(),connection);
                setMode("LOAD");
                messageBar.setMessage("Division deleted.","OK");
            }
        } 
        catch (Exception er){
            messageBar.setMessage(er.getMessage(),"ERROR");
        }
    }
  
    private void btnAdd_actionPerformed(ActionEvent e){
        e.toString();
        setMode("ADD");
        division = new Division();
        
    }
    
    private void btnEdit_actionPerformed(ActionEvent e){
        e.toString();
        setMode("EDIT");
    }

    private void btnCancel_actionPerformed(ActionEvent e){
        e.toString();
        setMode("LOAD");
    }
    
    private void saveUpdateDivision(){
        try{
           
            division.setID(mtxtDivisionID.getInputText());
            division.setCompany("Matrix");
            division.setDepartment("Customers");
            division.setName(mtxtName.getInputText());
            division.setTelephone(mtxtTelephoneNo.getInputText());
            division.setFax(mtxtFaxNo.getInputText());
            division.setContactPerson(mtxtContactPerson.getInputText());
            division.setEmail(mtxtEmail.getInputText());
            division.setRemarks(taRemarks.getText()); 
            division.setAddress(textAreaAddress.getText());
            division.setDesignation(mtxtDesignation.getInputText());
            division.setTravelTime(mtxtTravelTime.getInputText());
            
            if(division.isSaved()){
                DivisionData.updateDivision(division,connection);
                messageBar.setMessage("Division details updated","OK");
            }
            else{
                DivisionData.saveDivision(division,connection);
                messageBar.setMessage("New division saved","OK");
            }
            setMode("SEARCH");
        } 
        catch (Exception ex){
            ex.printStackTrace();
            messageBar.setMessage(ex.getMessage(),"ERROR");
        } 
    }
  
    private void setDivision(String division_id){
        try {
            division = DivisionData.getDivision("Matrix","Customers",division_id,connection);
            
            mtxtDivisionID.setInputText(division.getID());
            //mtxtCompanyID.setInputText(division.getCompany());
            //mtxtDepartmentID.setInputText(division.getDepartment());
            mtxtName.setInputText(division.getName());
            mtxtTelephoneNo.setInputText(division.getTelephone());
            mtxtFaxNo.setInputText(division.getFax());
            mtxtContactPerson.setInputText(division.getContactPerson());
            mtxtEmail.setInputText(division.getEmail());
            taRemarks.setText(division.getRemarks());
            mtxtDesignation.setInputText(division.getDesignation());
            mtxtTravelTime.setInputText(division.getTravelTime());
            textAreaAddress.setText(division.getAddress());
            
            tblPerons.populate(division.getID());
            
            setMode("SEARCH");
        } 
        catch(Exception e){
             messageBar.setMessage(e.getMessage(),"ERROR");
        }
    }
  
    private void setMode(String mode){
        if(mode.equals("EDIT")){
            setEditable(true);
            //mtxtCompanyID.setEditable(false);
            //mtxtDepartmentID.setEditable(false);
            mtxtDivisionID.setEditable(false);
            btnDelete.setEnabled(true);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
            btnAdd.setEnabled(false);
            btnEdit.setEnabled(false);
        }
        else if(mode.equals("ADD")){
            clearAll();
            setEditable(true);
            //mtxtCompanyID.setEditable(false);
            //mtxtDepartmentID.setEditable(false);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
            btnAdd.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
        }
        else if(mode.equals("LOAD")){
            clearAll();
            btnAdd.setEnabled(true);
            btnSave.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            btnCancel.setEnabled(false);
            setEditable(false);
        }
        else if(mode.equals("SEARCH")){
            btnEdit.setEnabled(true);
            btnDelete.setEnabled(true);
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
            btnAdd.setEnabled(false);
            setEditable(false);
        }
    }
  
    private void setEditable(boolean value){
        //mtxtCompanyID.setEditable(value);
        //mtxtDepartmentID.setEditable(value);
        mtxtDivisionID.setEditable(value);
        mtxtName.setEditable(value);
        mtxtTelephoneNo.setEditable(value);
        mtxtFaxNo.setEditable(value);
        mtxtContactPerson.setEditable(value);
        mtxtEmail.setEditable(value);
        taRemarks.setEditable(value);    
        //btnNewCompany.setEnabled(value);
        //btnNewDept.setEnabled(value);
        textAreaAddress.setEditable(value);
        mtxtDesignation.setEditable(value);
        mtxtTravelTime.setEditable(value);
    }
  
    private void clearAll(){
    
        //mtxtCompanyID.setInputText("");
        //mtxtDepartmentID.setInputText("");
        mtxtDivisionID.setInputText("");
        mtxtName.setInputText("");
        mtxtTelephoneNo.setInputText("");
        mtxtFaxNo.setInputText("");
        mtxtContactPerson.setInputText("");
        mtxtEmail.setInputText("");
        taRemarks.setText("");
        messageBar.clearMessage();
        textAreaAddress.setText("");
        mtxtDesignation.setInputText("");
        mtxtTravelTime.setInputText("");
        division = null;
    }

    private void btnMasterRefresh_actionPerformed(ActionEvent e) {

        try {
            SyncCustomer syncCust = new SyncCustomer(connection,messageBar);            
            frame.setCursor(hgCursor);
            messageBar.setMessage("Busy","CLOCK");
                syncCust.syncCustomer();
            //messageBar.clearMessage();    
            frame.setCursor(defCursor);
        }
        catch(Exception ex){
             messageBar.setMessage(ex.getMessage(),"ERROR");
        }
    }

    private void btnContactMasterRefresh_actionPerformed(ActionEvent e) {
        try {
            if(mtxtDivisionID.getInputText().trim().equals("")){
                messageBar.setMessage("Missing requred value - Select customer ID -","ERROR");
            }
            else{
                SyncCustomerContact syncCustContact =new SyncCustomerContact(connection,messageBar);            
                frame.setCursor(hgCursor);
                    syncCustContact.syncCustomerContact(mtxtDivisionID.getInputText().trim());
                    setDivision(mtxtDivisionID.getInputText().trim());
                frame.setCursor(defCursor);
            }
        }
        catch(Exception ex){
             messageBar.setMessage(ex.getMessage(),"ERROR");
        }
    }

    private void jButton1_actionPerformed(ActionEvent e) {
        frame.setCursor(hgCursor);
    }

    private void jButton2_actionPerformed(ActionEvent e) {
        frame.setCursor(defCursor);
    
    }

    class Person{
        public String name;
        public String designation;
        public String phone;
        public String email;
    }
    
    class ContactPersonsTable extends JTable{
        private ContactPersonsTableModel model;
        
        public ContactPersonsTable(){
                model = new ContactPersonsTableModel();
                this.setModel(model);
                this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                this.getTableHeader().setReorderingAllowed(false);
                this.setSelectionMode(0);
                
                this.getColumnModel().getColumn(0).setPreferredWidth(300);
                this.getColumnModel().getColumn(1).setPreferredWidth(200);
                this.getColumnModel().getColumn(2).setPreferredWidth(120);
                this.getColumnModel().getColumn(3).setPreferredWidth(250);
                
        }
        
        public void populate(String customer){
            try{

                String sql = "SELECT " +
                                "IFNULL(Contact_Person,'Not Found') AS Contact_Person, " +
                                "IFNULL(Designation,'Not Found') AS Designation, " +
                                "IFNULL(Email,'Not Found') AS Email, " +
                                "IFNULL(Direct_Phone_No,'Not Found') AS Direct_Phone_No " +
                             "FROM division_contacts " +
                             "WHERE Division_ID=?";
                
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1,customer);
                                
                ResultSet rec = stmt.executeQuery();
                Person person = null;
                removeAll();
                while(rec.next()){
                    person = new Person();
                    person.name = rec.getString("Contact_Person");
                    person.designation = rec.getString("Designation");
                    person.phone = rec.getString("Direct_Phone_No");
                    person.email = rec.getString("Email");
                    addPerson(person);
                }
            } 
            catch(Exception ex){
                ex.printStackTrace();
            } 
            
        }
                
        public void addPerson(Person person){
                model.addPerson(person);
                this.tableChanged(new TableModelEvent(model));
        }
                       
        public void removeAll(){
                model.removeAll();
                this.tableChanged(new TableModelEvent(model));
        }
        
        private class ContactPersonsTableModel extends AbstractTableModel{
            private String [] colNames;
            
            private Vector<Person> persons = new Vector();
        
            public ContactPersonsTableModel(){
                this.colNames = new String[]{
                                    "Name",                 //0
                                    "Designation",          //1
                                    "Phone",                //2
                                    "Email"                 //3
                                    };
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
                    return persons.size();  
            }
        
            public Object getValueAt(int r, int c){
                    Person person = persons.get(r);
                    switch(c){
                        case 0:return person.name;
                        case 1:return person.designation;
                        case 2:return person.phone;
                        case 3:return person.email;
                        default: return "";
                    }
            }
        
            public void setValueAt(Object value,int row,int col){
                    //nothing
            }
        
            public boolean isCellEditable(int r,int c){
                return false;
            }
                
            public void addPerson(Person person){
                    persons.add(person);
            }
            
            public void removeAll(){
                    persons.removeAllElements(); 
            }
        }
        
    }
    
}
