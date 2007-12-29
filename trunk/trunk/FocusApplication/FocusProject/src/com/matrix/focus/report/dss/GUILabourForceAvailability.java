package com.matrix.focus.report.dss;

import com.matrix.components.MDataCombo;
import com.matrix.components.MDatebox;
import com.matrix.focus.util.MPanel;
import com.matrix.focus.mdi.MDI;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.ImageLibrary;
import com.matrix.focus.util.TimeRuler;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.MaskFormatter;

public class GUILabourForceAvailability extends MPanel{

    private Connection connection;
    private MDI frame;
    private MessageBar messageBar;
    private JPanel jPanel1 = new JPanel();
    private MDatebox mdtDate = new MDatebox();
    private MDataCombo cmbEmpCat = new MDataCombo();
    private MDataCombo cmbSkillCat = new MDataCombo();
    private MDataCombo cmbSkillLevel = new MDataCombo();
    private JPanel jPanel2 = new JPanel();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private ViewPort viewPort = new ViewPort();
    private JFormattedTextField txtStart;
    private JFormattedTextField txtEnd;
    private JLabel jLabel1 = new JLabel();
    private JLabel jLabel2 = new JLabel();
    private JButton btnView = new JButton(new ImageIcon(ImageLibrary.BUTTON_VIEW));

    public GUILabourForceAvailability(DBConnectionPool pool, MDI frame, MessageBar msgBar){
        this.frame = frame;
        this.messageBar = msgBar;
        this.connection = pool.getConnection();
        try {
            cmbEmpCat.populate(connection,"SELECT DISTINCT title FROM employee");
            cmbSkillCat.populate(connection,"SELECT DISTINCT maintenance_category FROM employee_skills");
            cmbSkillLevel.populate(connection,"SELECT DISTINCT skill_level FROM employee");
        
            txtStart = new JFormattedTextField(new MaskFormatter("##':##"));
            txtEnd = new JFormattedTextField(new MaskFormatter("##':##"));
            
            cmbEmpCat.getComboBox().addItemListener(
                new ItemListener(){
                        public void itemStateChanged(ItemEvent e){
                            getEmployeeInformation();
                        }
                }
            );
            cmbSkillCat.getComboBox().addItemListener(
                new ItemListener(){
                        public void itemStateChanged(ItemEvent e){
                            getEmployeeInformation();
                        }
                }
            );
            cmbSkillLevel.getComboBox().addItemListener(
                new ItemListener(){
                        public void itemStateChanged(ItemEvent e){
                            getEmployeeInformation();
                        }
                }
            );
            
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void btnView_actionPerformed(ActionEvent e) {
        getEmployeeInformation();
    }
    
    private void getEmployeeInformation(){
        if(!mdtDate.getInputText().equals("")){
            if(getValidatedTime()){
                try{
                    ResultSet rec = connection.createStatement().executeQuery(getEmployeesSQL());
                    String emp_id = "";
                    String name = "";
                    String date = mdtDate.getInputText();
                    TimeRuler timeRuler;
                    viewPort.removeAll();
                    while(rec.next()){
                        emp_id = rec.getString(1);
                        name = rec.getString(2);
                        timeRuler = new TimeRuler(frame);
                        timeRuler.setAssignedInfo(emp_id,name,date,connection);
                        viewPort.add(timeRuler);
                    }
                } 
                catch(Exception ex)  {
                    ex.printStackTrace();
                }
            }
            else{
                messageBar.setMessage("Please select a valid time period.","ERROR");
            }
            
        }
        else{
            messageBar.setMessage("Please select a date.","ERROR");
        }
    }
    
    private String getEmployeesSQL(){
        String emp_cat = cmbEmpCat.getSelectedItem().toString();
        String skl_cat = cmbSkillCat.getSelectedItem().toString();
        String skl_lvl = cmbSkillLevel.getSelectedItem().toString();
        
        String sql_EmpCat = "";
        String sql_SkillLevel = "";
        String sql_SkillCat = "";
        
        if(cmbEmpCat.getComboBox().getSelectedIndex()==0){
            /**All*/
             sql_EmpCat = "All";
        }
        else{
            sql_EmpCat = "(SELECT employee_id, name FROM employee WHERE title ='" + emp_cat + "')";
        }
        
        if(cmbSkillCat.getComboBox().getSelectedIndex()==0){
            /**All*/
             sql_SkillCat = "All";
        }
        else{
            sql_SkillCat ="(SELECT e.employee_id, e.name FROM employee e, employee_skills s WHERE e.employee_id = s.employee_id AND s.Maintenance_Category ='" + skl_cat + "')"; 
        }
        
        if(cmbSkillLevel.getComboBox().getSelectedIndex()==0){
            /**All*/
             sql_SkillLevel = "All";
        }
        else{
            sql_SkillLevel = "(SELECT employee_id, name FROM employee WHERE skill_level ='" + skl_lvl + "')";
        }
        
        String sql = "";
        
        
        if(sql_EmpCat.equals("All"))
                {
                    if(sql_SkillLevel.equals("All"))
                    {
                        if(sql_SkillCat.equals("All"))
                        {
                             sql="SELECT employee_id, name FROM employee ";
                        }
                        else
                        {
                            // sql=sql_SkillCat;
                            sql="SELECT employee_id, name FROM  ("+sql_SkillCat+" as a )";
                        }
                    }
                    else
                    {
                        if(sql_SkillCat.equals("All"))
                        {
                            // sql=sql_SkillLevel;
                            sql="SELECT employee_id, name FROM  ("+sql_SkillLevel+" as a )";
                             
                        }
                        else
                        {
                             sql="SELECT employee_id, name FROM  ("+sql_SkillCat+" as a ) INNER JOIN ("+sql_SkillLevel+" as b) USING (employee_id, name) "; 
                        }
                    }
                }
                else
                {
                    if(sql_SkillLevel.equals("All"))
                    {
                        if(sql_SkillCat.equals("All"))
                        {
                            //  sql=sql_EmpCat;
                            sql="SELECT employee_id, name FROM  ("+sql_EmpCat+" as a )";
                        }
                        else
                        {
                              sql="SELECT employee_id, name FROM  ("+sql_EmpCat+" as a ) INNER JOIN ("+sql_SkillCat+" as b) USING (employee_id, name) "; 
                        }
                             
                    }
                    else
                    {
                        if(sql_SkillCat.equals("All"))
                        {
                              sql="SELECT employee_id, name FROM  ("+sql_EmpCat+" as a ) INNER JOIN ("+sql_SkillLevel+" as b) USING (employee_id, name) "; 
                        }
                        else
                        {
                             String sql_EmpCat_SkillLevel="(SELECT employee_id, name FROM  ("+sql_EmpCat+" as a ) INNER JOIN ("+sql_SkillLevel+" as b) USING (employee_id, name)) "; 
                             sql="SELECT employee_id, name FROM  ("+sql_EmpCat_SkillLevel+" as c ) INNER JOIN ("+sql_SkillCat+" as d) USING (employee_id, name) "; 
                        }
                    }
                }
                

         sql=sql+" where employee_id not in (SELECT pp.Employee_ID FROM " +
                     "(( SELECT ppin.Employee_ID FROM" + 
                         " ((SELECT lu.Employee_ID  as Employee_ID  FROM labour_utilisation lu  " +
                             "WHERE   (( '"+  mdtDate.getInputText()+" "+txtStart.getText()+":00"  +"'  BETWEEN lu.planned_start_time AND lu.planned_end_time ) " +
                             "OR " +
                             "( '"+   mdtDate.getInputText()+" "+txtEnd.getText()+":00"  +"'   BETWEEN lu.planned_start_time AND lu.planned_end_time ))  )" + 
                         " UNION" + 
                         " (SELECT luupm.Employee_ID as Employee_ID FROM labour_utilization_for_up_mnt  luupm " +
                             "WHERE (('"+  mdtDate.getInputText()+" "+txtStart.getText()+":00"  +"'  BETWEEN luupm.planned_start_time AND luupm.planned_end_time ) " +
                             "OR " +
                             "( '"+ mdtDate.getInputText()+" "+txtEnd.getText()+":00"  +"'   BETWEEN luupm.planned_start_time AND luupm.planned_end_time )) )) AS ppin" + 
                         " ORDER BY pp.Employee_ID ASC )" +
                 "UNION " +
                     "( SELECT ppout.Employee_ID FROM" + 
                         " ((SELECT lu.Employee_ID  as Employee_ID  FROM labour_utilisation lu  WHERE   ((lu.planned_start_time  BETWEEN '"+ mdtDate.getInputText()+" "+txtStart.getText()+":00"  +"' AND '"+  mdtDate.getInputText()+" "+txtEnd.getText()+":00"  +"' ) OR (lu.planned_end_time  BETWEEN '"+  mdtDate.getInputText()+" "+txtStart.getText()+":00"  +"' AND '"+   mdtDate.getInputText()+" "+txtEnd.getText()+":00"  +"' ))  )" + 
                         " UNION" + 
                         " (SELECT luupm.Employee_ID as Employee_ID FROM labour_utilization_for_up_mnt  luupm WHERE ((luupm.planned_start_time  BETWEEN '"+  mdtDate.getInputText()+" "+txtStart.getText()+":00"  +"' AND '"+   mdtDate.getInputText()+" "+txtEnd.getText()+":00"  +"' ) OR (luupm.planned_end_time BETWEEN '"+  mdtDate.getInputText()+" "+txtStart.getText()+":00"  +"' AND '"+   mdtDate.getInputText()+" "+txtEnd.getText()+":00"  +"' )) )) AS ppout" + 
                         " ORDER BY pp.Employee_ID ASC )) " +
                 " AS pp " +
                 " ORDER BY pp.Employee_ID ASC )";
        
        //System.out.println("-----"+sql);
        return sql;
    }
    
    private void jbInit() throws Exception {
        this.setLayout(null);
        this.setSize(new Dimension(941, 608));
        jPanel1.setBounds(new Rectangle(10, 5, 930, 95));
        jPanel1.setLayout(null);
        jPanel1.setBorder(BorderFactory.createTitledBorder("Employee Selection"));
        mdtDate.setBounds(new Rectangle(25, 25, 235, 20));
        mdtDate.setCaption("Date");
        mdtDate.setLblWidth(120);
        cmbEmpCat.setBounds(new Rectangle(25, 50, 315, 20));
        cmbEmpCat.setCaption("Employee Category");
        cmbEmpCat.setLblWidth(120);
        cmbSkillCat.setBounds(new Rectangle(350, 50, 275, 20));
        cmbSkillCat.setCaption("Skill Category");
        cmbSkillCat.setLblWidth(80);
        cmbSkillLevel.setBounds(new Rectangle(635, 50, 275, 20));
        cmbSkillLevel.setCaption("Skill Level");
        cmbSkillLevel.setLblWidth(80);
        jPanel2.setBounds(new Rectangle(10, 100, 930, 505));
        jPanel2.setBorder(BorderFactory.createTitledBorder("Employee Availability"));
        jPanel2.setLayout(null);
        jScrollPane1.setBounds(new Rectangle(10, 20, 910, 475));
        txtStart.setBounds(new Rectangle(325, 25, 40, 20));
        txtStart.setText("00:00");
        txtEnd.setBounds(new Rectangle(430, 25, 40, 20));
        txtEnd.setText("00:00");
        jLabel1.setText("Start Time");
        jLabel1.setBounds(new Rectangle(270, 30, 60, 15));
        jLabel2.setText("End Time");
        jLabel2.setBounds(new Rectangle(375, 30, 60, 15));
        btnView.setText("View");
        btnView.setBounds(new Rectangle(815, 20, 95, 20));
        btnView.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnView_actionPerformed(e);
                    }
                });
        jPanel1.add(btnView, null);
        jPanel1.add(jLabel2, null);
        jPanel1.add(jLabel1, null);
        jPanel1.add(txtStart, null);
        jPanel1.add(txtEnd, null);
        jPanel1.add(cmbSkillLevel, null);
        jPanel1.add(cmbSkillCat, null);
        jPanel1.add(cmbEmpCat, null);
        jPanel1.add(mdtDate, null);
        jScrollPane1.getViewport().add(viewPort, null);
        jPanel2.add(jScrollPane1, null);
        this.add(jPanel2, null);
        this.add(jPanel1, null);
        
        
        
    }
    
    private boolean getValidatedTime(){
        boolean is_valid= false;
        String sql="Select IF(TIME('"+ mdtDate.getInputText()+" "+txtStart.getText()+":00" +"') <= TIME('"+  mdtDate.getInputText()+" "+txtEnd.getText()+":00" +"'),'true','false') as Is_Valid";
        try{
            ResultSet rec = connection.createStatement().executeQuery(sql);
            //System.out.println(sql);
            while(rec.next()){
                is_valid = rec.getBoolean(1);
            }
        } 
        catch(Exception ex)  {
            ex.printStackTrace();
        } 
        return is_valid;
    }

    class ViewPort extends JTextArea{
        private int POS_Y;
                
        public ViewPort(){
            setEditable(false);
            setBackground(jPanel1.getBackground());
            setLayout(null);
            POS_Y = 10;
        }
        
        public Component add(Component comp){
            super.add(comp);
            comp.setLocation(50,POS_Y);
            setText(getText()+"\n\n\n\n\n\n\n");
            setCaretPosition(0);
            POS_Y += 100;
            return comp;
        }
        
        public void removeAll(){
            super.removeAll();
            POS_Y = 10;
            setText("\n\n\n\n\n");
            this.repaint();
        }
    }
    
}


