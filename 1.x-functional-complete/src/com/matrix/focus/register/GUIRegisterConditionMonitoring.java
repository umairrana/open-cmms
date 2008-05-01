package com.matrix.focus.register;

import com.matrix.components.TitleBar;
import com.matrix.focus.mdi.MDI;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.Authorizer;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.ImageLibrary;
import com.matrix.focus.util.LockedLabelCellRenderer;
import com.matrix.focus.util.MList;
import com.matrix.focus.util.MPanel;
import com.matrix.focus.util.Validator;
import com.matrix.focus.util.tree.SingleTree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.lang.Boolean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


public class GUIRegisterConditionMonitoring extends MPanel implements TreeSelectionListener{

    /**Get these from the MDI*/
    private Connection connection;
    private JFrame frame;
    private MessageBar messageBar;

    private JPanel jPanel2 = new JPanel();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JScrollPane jScrollPane2 = new JScrollPane();
    private JButton btnAddFrom = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));
    private JButton btnRemoveFrom = new JButton(new ImageIcon(ImageLibrary.BUTTON_REMOVE));
    private JButton btnSave = new JButton(new ImageIcon(ImageLibrary.BUTTON_SAVE));
    private JScrollPane jScrollPane3 = new JScrollPane();
    private RegisterTable tblRegister;
    private ConditionMonitoringMasteListTable tblMaster = new ConditionMonitoringMasteListTable();
    private MList lstAssets = new MList("Assets");
    private SingleTree categoriesTree;
    private JScrollPane jScrollPane4 = new JScrollPane();
    private String registeredLevel;
    private JButton btnCancel = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
    private TitleBar titlebar = new TitleBar();
    private JPanel jPanel1 = new JPanel();
    private JPanel jPanel3 = new JPanel();
    private JPanel jPanel4 = new JPanel();
    private JButton btnEdit = new JButton(new ImageIcon(ImageLibrary.BUTTON_EDIT));
    private Savepoint savePoint;
    private boolean capable;

    public GUIRegisterConditionMonitoring(DBConnectionPool pool, JFrame frame, MessageBar msgBar){
        /**From the MDI*/
        /**New Database connection was taken to manage the transactions*/
        this.connection = pool.getConnection();
        try{
            this.connection.setAutoCommit(false);
        }
        catch(Exception er){
            er.toString();
        }
        
        this.frame = frame;
        this.messageBar = msgBar;
    
        titlebar.setTitle("Register Condition Monitorings");
        titlebar.setDescription("The facility to register condition monitorings to assets.");
        titlebar.setImage(ImageLibrary.TITLE_CONDITION_MONITORING_REGISTER);
        
        try{
          lstAssets.getColumnModel().getColumn(0).setCellRenderer(new NewAssetCellRenderer());
          tblRegister = new RegisterTable(connection);
          categoriesTree = new SingleTree(SingleTree.TREE_ASSET_CATEGORIES,connection);
          categoriesTree.expandRow(0);
          jbInit();
          tblMaster.populate();
        }
        catch(Exception e){
          e.printStackTrace();
            
        }
        setMode("LOAD");
    }
    
    public void close(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void setVisible(boolean flag){
        capable = Authorizer.isCapable(MDI.USERNAME,Authorizer.REGISTER_ALL,connection);
        super.setVisible(flag);
    }

    private void btnEdit_actionPerformed(ActionEvent e){
        try{
            savePoint = connection.setSavepoint();
        } 
        catch (Exception ex){
            ex.printStackTrace();
        }
        setMode("EDIT");
    }

    private void setMode(String mode){
        if(mode.equals("LOAD")){
            btnAddFrom.setEnabled(false);
            btnRemoveFrom.setEnabled(false);
            btnSave.setEnabled(false);
            btnEdit.setEnabled(false);
            tblRegister.setEnabled(false);
        }
        else if(mode.equals("EDIT")){
            if(capable){
                btnAddFrom.setEnabled(true);
                btnRemoveFrom.setEnabled(true);
                btnSave.setEnabled(true);
            }
            btnEdit.setEnabled(false);
            tblRegister.setEnabled(true);
        }
        else if(mode.equals("SEARCH")){
            btnAddFrom.setEnabled(false);
            btnRemoveFrom.setEnabled(false);
            btnSave.setEnabled(false);
            btnEdit.setEnabled(true);
            tblRegister.setEnabled(false);
        }
    }

    public class NewAssetCellRenderer extends JLabel implements TableCellRenderer{
        public NewAssetCellRenderer(){
            setOpaque(true);
            setIcon(new ImageIcon(ImageLibrary.TREE_ASSET));
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
            this.setText(value.toString());
            if(isSelected){
                this.setBackground(table.getSelectionBackground());
                this.setForeground(table.getSelectionForeground());
            }
            else if(isNewAsset(value.toString())){
                this.setBackground(table.getBackground());
                this.setForeground(Color.RED);
            }
            else{
                this.setBackground(table.getBackground());
                this.setForeground(table.getForeground());
            }
            return this;
        }
        
        private boolean isNewAsset(String asset){
            String sql = "SELECT reading_id FROM machine_reading WHERE machine_no ='"+asset+"'";
            try{
                ResultSet rec = connection.createStatement().executeQuery(sql);
                rec.next();
                return (!rec.getString(1).equals("")?false:true);
            }
            catch (Exception ex)  {
                //ex.printStackTrace();
                 
                return true;
            } 
        }
    }
    
    private void jbInit() throws Exception{
        this.setLayout(null);
        this.setSize(new Dimension(998, 606));
        jPanel2.setBounds(new Rectangle(10, 320, 940, 245));
        jPanel2.setBorder(BorderFactory.createTitledBorder("Registered Condition Monitorings"));
        jPanel2.setLayout(null);
        jScrollPane1.setBounds(new Rectangle(10, 20, 480, 200));
        jScrollPane2.setBounds(new Rectangle(10, 20, 920, 215));
        btnAddFrom.setBounds(new Rectangle(955, 340, 30, 30));
        btnAddFrom.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnAddFrom_actionPerformed(e);
            }
        });
        btnRemoveFrom.setBounds(new Rectangle(955, 375, 30, 30));
        btnRemoveFrom.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnRemoveFrom_actionPerformed(e);
            }
        });
        btnSave.setText("Save");
        btnSave.setBounds(new Rectangle(735, 570, 100, 25));
        btnSave.setSize(new Dimension(100, 25));
        btnSave.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnSave_actionPerformed(e);
                    }
                });
        jScrollPane3.setBounds(new Rectangle(10, 20, 200, 200));
        jScrollPane3.setBackground(Color.white);
        jScrollPane4.setBounds(new Rectangle(10, 20, 200, 200));
        btnCancel.setText("Cancel");
        btnCancel.setBounds(new Rectangle(840, 570, 100, 25));
        btnCancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnCancel_actionPerformed(e);
                    }
                });
        titlebar.setBounds(new Rectangle(10, 10, 940, 70));
        jPanel1.setBounds(new Rectangle(10, 90, 220, 230));
        jPanel1.setBorder(BorderFactory.createTitledBorder("Asset Selection"));
        jPanel1.setLayout(null);
        jPanel3.setBounds(new Rectangle(230, 90, 220, 230));
        jPanel3.setBorder(BorderFactory.createTitledBorder("Selected Assets"));
        jPanel3.setLayout(null);
        jPanel4.setBounds(new Rectangle(450, 90, 500, 230));
        jPanel4.setBorder(BorderFactory.createTitledBorder("Condition Monitorings"));
        jPanel4.setLayout(null);
        btnEdit.setText("Edit");
        btnEdit.setBounds(new Rectangle(630, 570, 100, 25));
        btnEdit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnEdit_actionPerformed(e);
                    }
                });
        jScrollPane3.getViewport().setBackground(Color.WHITE);
        tblRegister.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        categoriesTree.addTreeSelectionListener(this);
        jScrollPane4.getViewport().add(categoriesTree, null);
        jPanel1.add(jScrollPane4, null);
        jScrollPane1.getViewport().add(tblMaster, null);
        jPanel4.add(jScrollPane1, null);
        this.add(btnEdit, null);
        this.add(jPanel4, null);
        jScrollPane3.getViewport().add(lstAssets, null);
        jPanel3.add(jScrollPane3, null);
        this.add(jPanel3, null);
        this.add(jPanel1, null);
        this.add(titlebar, null);
        this.add(btnCancel, null);
        jScrollPane2.getViewport().add(tblRegister, null);
        jPanel2.add(jScrollPane2, null);
        this.add(jPanel2, null);
        this.add(btnSave, null);
        this.add(btnAddFrom, null);
        this.add(btnRemoveFrom, null);
    }
    
    private void btnRemoveFrom_actionPerformed(ActionEvent e){
        /**Selected row in the Ragister table*/
        int row = tblRegister.getSelectedRow();
            if(row != -1){
                String registeredLevel_of_the_selected_row = tblRegister.getValueAt(row,7).toString();

                if(registeredLevel_of_the_selected_row.equals(registeredLevel)){
        
                    if(MessageBar.showConfirmDialog(frame,"Are you sure you want to delete \nthe selected Condition Monitoring from the system?","Delete Condition Monitoring")==MessageBar.YES_OPTION){
                        /**Get the CM ID*/
                        String cond_moni_id = tblRegister.getValueAt(row,0).toString();
                        /**Get Ticked Assets*/
                        Object[] assets = lstAssets.getSelectedValues();
                        int no_of_selected_assets = assets.length;
                        /**Tmp object*/
                        ConditionMonitoringRegisterEntry cmre;
                        /**Status*/
                        int done = -1;
                    
                        try{
                            Savepoint sPointDelete = connection.setSavepoint("Delete");
                            /**For each selcted asset*/
                            for(int i=0;i<no_of_selected_assets;i++){
                                cmre = new ConditionMonitoringRegisterEntry(connection);
                                /**Only two key fields*/
                                cmre.condition_monitoring_id = cond_moni_id;
                                cmre.asset_id = assets[i].toString();
                                /**Delete*/
                                done = cmre.delete();
                                if(done==-1){
                                    connection.rollback(sPointDelete);
                                    break;
                                }
                            }
                        }
                        catch(Exception er){
                            er.printStackTrace();
                        }
                    
                        if(done>=0 || no_of_selected_assets==0){
                            tblRegister.deleteRow(row);
                            messageBar.setMessage("Selected Condition Monitoring was deleted.","OK");
                        }
                    }
                }
            else{
                messageBar.setMessage("You can not change selected maintenance at this level. Please go to a "+ tblRegister.getValueAt(row,7).toString().toLowerCase() +" level.","WARN");
            }
        }
        else{
            messageBar.setMessage("Select a Condition Monitoring first.","ERROR");
        }
    }
    
    private void btnAddFrom_actionPerformed(ActionEvent e){
        int sRow = tblMaster.getSelectedRow();
        if(lstAssets.getRowCount()>0){
            if(sRow!=-1){
                String sCMID = tblMaster.getValueAt(sRow,1)+"";
                String desc = tblMaster.getValueAt(sRow,2)+"";
                if(!isExisting(sCMID)){
                    addToRegisterTable(sCMID,desc);
                    messageBar.setMessage("Selected Condition Monitoring was added.","OK");
                }
                else{
                    messageBar.setMessage("Selected Condition Monitoring has been already registered.","ERROR");
                }
            }
            else{
                messageBar.setMessage("Select a Condition Monitoring first.","ERROR");
            }
        }
        else{
            messageBar.setMessage("Select assets before adding Condition Monitoring.","ERROR");
        }
    }
    
    private boolean isExisting(String cmid){
        int rows = tblRegister.getRowCount();
        for(int i=0;i<rows;i++){
            String id = tblRegister.getValueAt(i,0) + "";
            if(id.equals(cmid)){
                return true;
            }
        }
        return false;
    }
    
    private void addToRegisterTable(String cmid,String desc){
        tblRegister.addRow();
        //System.out.println("Done");
        int row = tblRegister.getRowCount()-1;
        tblRegister.setValueAt(cmid,row,0);
        tblRegister.setValueAt(desc,row,1);
    }

    private boolean register(){
        /**Total number of assets*/
        Object[] assets = lstAssets.getSelectedValues();
        int no_of_selected_assets = assets.length;
        /**Total number of maintenance*/
        int no_of_condition_monitorings = tblRegister.getRowCount();
        /**Entry object*/
        ConditionMonitoringRegisterEntry cmre;
        /**Return value*/
        boolean done = false;
        
        /**For each assets*/
        for(int i=0;i<no_of_selected_assets;i++){
            for(int j=0;j<no_of_condition_monitorings;j++){
                /**Registering only selected level maintenance*/
                if(registeredLevel.equals(tblRegister.getValueAt(j,7).toString())){
                    cmre = new ConditionMonitoringRegisterEntry(connection);
                        cmre.asset_id = assets[i].toString();
                        cmre.condition_monitoring_id = tblRegister.getValueAt(j,0).toString();
                        cmre.lower_limit = tblRegister.getValueAt(j,2).toString();
                        cmre.upper_limit = tblRegister.getValueAt(j,3).toString();
                        cmre.considering_limit = tblRegister.getValueAt(j,4).toString();
                        cmre.unit_of_messure = tblRegister.getValueAt(j,5).toString();
                        cmre.interrupted = tblRegister.getValueAt(j,6).toString();
                        cmre.order = tblRegister.getValueAt(j,7).toString();
                    if(cmre.save()){
                        done = true;
                    }
                    else if(cmre.update()){
                        done = true;
                    }
                    else{
                        done = false;
                        break;
                    }
                }
                else{
                    done = true;
                }
            }
        }
        return done;
    }
    
    private void btnSave_actionPerformed(ActionEvent e) {
        if(validateRegistering()){
            try {
              savePoint = connection.setSavepoint();
                if(register()){
                    connection.commit();
                    messageBar.setMessage("Condition Monitoring information saved.","OK");
                    setMode("SEARCH");
                }
                else{
                    connection.rollback(savePoint);
                    messageBar.setMessage("Could not save Condition Monitoring information","ERROR");
                }
            } catch(SQLException f){
                f.printStackTrace();
            }
        }
    }
    
    private void btnCancel_actionPerformed(ActionEvent e) {
        try {
            tblRegister.deleteAll();
            getRegisteredConditionMonitorings(lstAssets.getValueAt(0,0).toString());
            connection.rollback(savePoint);
        } catch (Exception f){
            f.printStackTrace();
        }
        setMode("SEARCH");
    }
    
    private boolean validateRegistering(){
        String lower, upper, consider;        
        int rows = tblRegister.getRowCount();
        
        for(int row=0;row<rows;row++){
            lower = tblRegister.getValueAt(row,2)+"";
            upper = tblRegister.getValueAt(row,3)+"";
            consider = tblRegister.getValueAt(row,4)+"";
            
            if(consider.equals("Lower Limit") && !validateForNumber(lower)){
                messageBar.setMessage("Invalid Lower limit in row " + (row + 1),"ERROR");
                return false;
            }
            if(consider.equals("Upper Limit") && !validateForNumber(upper)){
                messageBar.setMessage("Invalid Upper limit in row " + (row + 1),"ERROR");
                return false;
            }
            else if(consider.equals("Both Limits") && !validateForNumber(upper)){
                if(!validateForNumber(lower)){
                    messageBar.setMessage("Invalid Lower limit in row " + (row + 1),"ERROR");
                    return false;
                }
                else if(!validateForNumber(upper)){
                    messageBar.setMessage("Invalid Upper limit in row " + (row + 1),"ERROR");
                    return false;
                }
            }
            else{
                continue;
            }
        }
        return true;
    }
    
    private boolean validateForNumber(String value){
        if(Validator.isEmpty(value)){
            return false;
        }
        else if(!Validator.isNumber(value)){
            return false;
        }
        else if(!Validator.isNonNegative(value)){
            return false;
        }
        else{
            return true;
        }
    }

    public void valueChanged(TreeSelectionEvent e) {
        try{
            Object[] path = categoriesTree.getSelectionPath().getPath();
            String lbl = "Selection :- ";
            for(int i=1;i<path.length;i++){
                if(i==1) lbl += path[i].toString();
                else lbl += " --) " + path[i];
            }            
            
            lstAssets.deleteAll();
            tblRegister.deleteAll();
            if(path.length>1){
                lstAssets.populate(connection,getAssetSQLString(path));
                getRegisteredConditionMonitorings(lstAssets.getValueAt(0,0).toString());
                setMode("SEARCH");
            }
        }
        catch(Exception er){
            er.printStackTrace();
            
        }
    }
    
    private String getAssetSQLString(Object[] path){
        /**Excluding the first node CATEGORIES*/
        int length = path.length-1;
        /**Tmp sql*/
        String sql = "SELECT asset_id FROM asset WHERE ";
        
        /**Category Level*/
        if(length==1){
            registeredLevel = "CATEGORY";
            /**SELECT asset_id FROM asset WHERE model_id IN (SELECT model_id FROM asset_model WHERE category_id='XXXX')*/
            sql += "model_id IN (SELECT model_id FROM asset_model WHERE category_id='" + path[1] +"')";
        }
        /**Model Level*/
        else if(length==2){
            registeredLevel = "MODEL";
            /**SELECT asset_id FROM asset WHERE model_id IN (SELECT model_id FROM asset_model WHERE category_id='XXX' AND Sub_category = 'XXX' AND model_id='XXX');*/
            sql += "model_id IN (SELECT model_id FROM asset_model WHERE category_id='" + path[1] +"' AND model_id='"+ path[2] +"')";
        }
        /**Asset Level*/
        else if(length==3){
            registeredLevel = "ASSET";
            /**SELECT asset_id FROM asset WHERE model_id = (SELECT model_id FROM asset_model WHERE category_id='XXX' AND Sub_category = 'XXX' AND model_id='XXX');*/
             sql += "model_id = (SELECT model_id FROM asset_model WHERE category_id='" + path[1] +"' AND model_id='"+ path[2] +"') AND asset_id ='" + path[3] + "'";
        }
        return sql;
    }
    
    private void getRegisteredConditionMonitorings(String a_asset_id){
        try{
            Statement stmt = connection.createStatement();
            ResultSet rec = stmt.executeQuery(getConditionMonitoringSQLString(categoriesTree.getSelectionPath().getPath(),a_asset_id));
            int cnt = 0;
            while(rec.next()){
                tblRegister.addRow();
                cnt = rec.getRow()-1;
                tblRegister.setValueAt(rec.getString("reading_id"),cnt,0);
                tblRegister.setValueAt(rec.getString("Description"),cnt,1);
                tblRegister.setValueAt(rec.getString("Lower_Limit"),cnt,2);
                tblRegister.setValueAt(rec.getString("Upper_Limit"),cnt,3);
                tblRegister.setValueAt(rec.getString("Considering_Limit"),cnt,4);
                tblRegister.setValueAt(rec.getString("Unit_of_Measure"),cnt,5);
                tblRegister.setValueAt(rec.getBoolean("Interrupted"),cnt,6);
                tblRegister.setValueAt(rec.getString("order"),cnt,7);
            }
            tblRegister.repaint();
        }
        catch(Exception er){
            er.printStackTrace();
            
        }
    }
    private String getConditionMonitoringSQLString(Object[] path, String a_asset_id){
        /**Excluding the first node CATEGORIES*/
        int length = path.length-1;
        /**Tmp sql*/
        String sql = "SELECT " +
                        "r.reading_id, " +
                        "c.Description," +
                        "r.Interrupted, " +
                        "r.Lower_Limit, " +
                        "r.Upper_Limit, " +
                        "r.Considering_Limit, " +
                        "r.Unit_of_Measure, " +
                        "r.order " +
                    "FROM " +
                        "machine_reading r," +
                        "readingsmaster c " +
                    "WHERE " +
                        "c.Reading_ID = r.reading_id AND ";
        
        /**Category*/
        if(length==1){
            sql += "r.machine_no='" + a_asset_id + "'";
        }
        /**Model*/
        else if(length==2){
            sql += "r.machine_no='" + a_asset_id + "'";
        }
        /**Asset*/
        else if(length==3){
            sql += "r.machine_no='" + a_asset_id + "'";
        }
        return sql;
    }

    private class ConditionMonitoringMasteListTable extends JTable {
        private ConditionMonitoringMasteListTableModel myModel = null ;
        
        public ConditionMonitoringMasteListTable() {
            myModel = new ConditionMonitoringMasteListTableModel();
            this.setModel(myModel);
            this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.getTableHeader().setReorderingAllowed(false);
            this.getColumnModel().getColumn(0).setPreferredWidth(70);
            this.getColumnModel().getColumn(1).setPreferredWidth(70);
            this.getColumnModel().getColumn(2).setPreferredWidth(300);
            /**Single row selection*/
            this.setSelectionMode(0);
        }
        
        public void populate(){
            try{
                Statement stmt = connection.createStatement();
                ResultSet rec = stmt.executeQuery("SELECT category, reading_id, description FROM readingsmaster ORDER BY category, reading_id");
                while(rec.next()){
                    addRow();
                    int row = rec.getRow() - 1;
                    
                    String cate = rec.getString("category");
                    String id = rec.getString("reading_ID");
                    String desc = rec.getString("Description");
                    
                    setValueAt(cate,row,0);
                    setValueAt(id,row,1);
                    setValueAt(desc,row,2);
                }
                rec.close();
                stmt.close();
                rec = null;
                stmt = null;
            }
            catch(Exception er){
                er.printStackTrace();
              
            }
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
            
        private class ConditionMonitoringMasteListTableModel extends AbstractTableModel{
            private int rowCount = 0;
            private String [] colNames = {"Category","ID","Description"};
            private Object [][] valueArray = null;
            private Object [][] tempArray = null;

       
            public ConditionMonitoringMasteListTableModel(){
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
                valueArray = new Object[0][3];
            }
        }
    }

    
    public class RegisterTable extends JTable {
        private RegisterTableModel myModel = null ;
        private Connection registerTableConnection;
        private UnitsCombo cmbUnit = new UnitsCombo(connection);
         
         
        public RegisterTable(Connection con){
            registerTableConnection = con;
            myModel = new RegisterTableModel();
            this.setModel(myModel);
            this.setSelectionMode(0);
            this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.getTableHeader().setReorderingAllowed(false);
            
            TableColumn tc = this.getColumnModel().getColumn(4);
            tc.setCellEditor(new DefaultCellEditor(new LimitCombo()));
            tc = this.getColumnModel().getColumn(5);
            tc.setCellEditor(new DefaultCellEditor(cmbUnit));
            
            this.getColumnModel().getColumn(0).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(1).setCellRenderer(new LockedLabelCellRenderer());
            this.getColumnModel().getColumn(2).setCellRenderer(new LowerLimitCellRenderer());
            this.getColumnModel().getColumn(3).setCellRenderer(new UpperLimitCellRenderer());
            
            this.getColumnModel().getColumn(0).setPreferredWidth(70);
            this.getColumnModel().getColumn(1).setPreferredWidth(490);
            this.getColumnModel().getColumn(2).setPreferredWidth(80);
            this.getColumnModel().getColumn(3).setPreferredWidth(80);
            this.getColumnModel().getColumn(4).setPreferredWidth(90);
            this.getColumnModel().getColumn(5).setPreferredWidth(50);
            this.getColumnModel().getColumn(6).setPreferredWidth(70);         
            
            /**Hidden columns */
            //this.getColumnModel().getColumn(7).setMinWidth(0);
            //this.getColumnModel().getColumn(7).setPreferredWidth(0);
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
        
        public class LowerLimitCellRenderer extends JLabel implements TableCellRenderer{
            public LowerLimitCellRenderer(){
                setOpaque(true);
            }

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
                this.setText(value.toString());
                if(isSelected){
                    this.setBackground(table.getSelectionBackground());
                    this.setForeground(table.getSelectionForeground());
                }
                else if(!table.getValueAt(row,4).toString().equals("Lower Limit") && !table.getValueAt(row,4).toString().equals("Both Limits")){
                    this.setBackground(Color.LIGHT_GRAY);
                    this.setForeground(table.getForeground());
                }
                else{
                    this.setBackground(table.getBackground());
                    this.setForeground(table.getForeground());
                }
                return this;
            }
        }
        
        public class UpperLimitCellRenderer extends JLabel implements TableCellRenderer{
            public UpperLimitCellRenderer(){
                setOpaque(true);
            }

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
                this.setText(value.toString());
                if(isSelected){
                    this.setBackground(table.getSelectionBackground());
                    this.setForeground(table.getSelectionForeground());
                }
                else if(!table.getValueAt(row,4).toString().equals("Upper Limit") && !table.getValueAt(row,4).toString().equals("Both Limits")){
                    this.setBackground(Color.LIGHT_GRAY);
                    this.setForeground(table.getForeground());
                }
                else{
                    this.setBackground(table.getBackground());
                    this.setForeground(table.getForeground());
                }
                return this;
            }
        }
           
        private class RegisterTableModel extends AbstractTableModel{
             private int rowCount = 0;
             private String [] colNames = {
                                "ID",                       //0
                                "Description",              //1
                                "Lower Limit",              //2
                                "Upper Limit",              //3
                                "Considering Limit",        //4 
                                "Unit",                     //5
                                "Interrupted",              //6
                                "Order No"          //7
                                };
            private Object [][] valueArray = null;
            private Object [][] tempArray = null;
     
            public RegisterTableModel() {
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
                if(valueArray[r][c]==null)
                    return "";
                else
                    return valueArray[r][c];
            }
             
            public void setValueAt(Object value,int row,int col){
                if(col==5 && !value.equals("")){
                    boolean have = false;
                    for(int i=0;i<cmbUnit.getItemCount();i++){
                        if(value.equals(cmbUnit.getItemAt(i))){
                            have = true;
                            break; 
                        }
                    }
                    if(!have)cmbUnit.addItem(value);
                }
                valueArray[row][col] = value;
            }
                
            public boolean isCellEditable(int r,int c){
                    String consider = valueArray[r][4]+"";
                    
                    /**Upper level registrations
                    if(!registeredLevel.equals(valueArray[r][7])){
                        messageBar.setMessage("You can not change selected Condition Monitoring at this level. Please go to a "+ valueArray[r][7].toString().toLowerCase() +" level.","WARN");
                        return false;
                    }
                    */
                    //else{
                        valueArray[r][7] = registeredLevel;
                        if(c==0 || c==1){
                            return false;
                        }
                        else if(consider.equals("Lower Limit") && (c==3)){
                            return false;
                        }
                        else if(consider.equals("Upper Limit") && c==2){
                            return false;
                        }
                        else{
                            return true;
                        }
                    //}
            }
            
            public Class getColumnClass(int c){
                if(c==6)
                    return this.getValueAt(0,c).getClass();
                else
                    return "".getClass();   
            }
            
            public void addRow(){
                tempArray = new Object[this.getRowCount()+1][this.getColumnCount()];
            
                for(int y=0 ; y<valueArray.length; y++){
                    for(int x=0; x<valueArray[0].length; x++){
                        tempArray[y][x] =  valueArray[y][x];
                    }
                }
                //Initial Value
                tempArray[tempArray.length-1][4] = "Both Limits";
                tempArray[tempArray.length-1][6] = Boolean.FALSE;
                tempArray[tempArray.length-1][7] = registeredLevel;
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
    
        class LimitCombo extends JComboBox implements ItemListener{
            
            public LimitCombo(){
                addItem("Upper Limit");
                addItem("Lower Limit");
                addItem("Both Limits");
                setFont(new Font("Tahoma", 0, 11));
                addItemListener(this);
            }

            public void itemStateChanged(ItemEvent e) {
                int row = tblRegister.getSelectedRow();
                if(row!=-1){
                    String value = getSelectedItem()+"";
                    if(value.equals("Upper Limit")){
                        tblRegister.setValueAt("",row,2);
                    }
                    else if(value.equals("Lower Limit")){
                        tblRegister.setValueAt("",row,3);
                    }
                }
                tblRegister.repaint();
            }
        }
    
        class UnitsCombo extends JComboBox{
            private Connection meterReadingUnitsComboConnection;

            public UnitsCombo(Connection con){
                meterReadingUnitsComboConnection = con;
                setFont(new Font("Tahoma", 0, 11));
                populate("SELECT DISTINCT Unit_of_Measure FROM machine_reading WHERE TRIM(Unit_of_Measure)!=''");
                setEditable(true);
            }
      
            private void populate(String sql){
                try{
                    ResultSet rec = meterReadingUnitsComboConnection.createStatement().executeQuery(sql);
                    while(rec.next()){
                        this.addItem(rec.getString(1));
                    }
                }
                catch(Exception er){
                    er.printStackTrace();
                    
                }
            }
        }
    }
}
 