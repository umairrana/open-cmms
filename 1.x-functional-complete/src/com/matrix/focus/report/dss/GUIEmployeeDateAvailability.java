package com.matrix.focus.report.dss;

import com.matrix.components.DatePicker;
import com.matrix.components.MDataCombo;
import com.matrix.components.MTextbox;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GUIEmployeeDateAvailability extends MPanel{
    private Connection connection;
    private MDI frame;
    private MessageBar messageBar;
    private JPanel jPanel1 = new JPanel();
    private MDataCombo cmbEmpCat = new MDataCombo();
    private MDataCombo cmbSkillCat = new MDataCombo();
    private MDataCombo cmbSkillLevel = new MDataCombo();
    private JPanel jPanel2 = new JPanel();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private ViewPort viewPort = new ViewPort();
    private JButton btnPrint = new JButton(new ImageIcon(ImageLibrary.BUTTON_PRINT));
    private JButton btnPickDate = new JButton(new ImageIcon(ImageLibrary.BUTTON_DT_PICKER));
    private static DatePicker dtp;
    private MTextbox txtDate = new MTextbox();

    public GUIEmployeeDateAvailability(DBConnectionPool pool, MDI frame, MessageBar msgBar){
        this.frame = frame;
        this.messageBar = msgBar;
        this.connection = pool.getConnection();
        try {
            cmbEmpCat.populate(connection,"SELECT DISTINCT title FROM employee");
            cmbSkillCat.populate(connection,"SELECT DISTINCT maintenance_category FROM employee_skills");
            cmbSkillLevel.populate(connection,"SELECT DISTINCT skill_level FROM employee");
                        
            dtp = new DatePicker(null);
            dtp.FORMAT="YYYY-MM-DD";
            
            cmbEmpCat.getComboBox().addItemListener(
                new ItemListener(){
                        public void itemStateChanged(ItemEvent e){
                            getEmployeeInformation(txtDate.getInputText());
                        }
                }
            );
            cmbSkillCat.getComboBox().addItemListener(
                new ItemListener(){
                        public void itemStateChanged(ItemEvent e){
                            getEmployeeInformation(txtDate.getInputText());
                        }
                }
            );
            cmbSkillLevel.getComboBox().addItemListener(
                new ItemListener(){
                        public void itemStateChanged(ItemEvent e){
                            getEmployeeInformation(txtDate.getInputText());
                        }
                }
            );
            
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setDate(String date){
        txtDate.setInputText(date);
        getEmployeeInformation(date);
    }
    
    private void getEmployeeInformation(String date){
        if(!txtDate.getInputText().equals("")){
            try{
                ResultSet rec = connection.createStatement().executeQuery(getEmployeesSQL());
                String emp_id = "";
                String name = "";
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
                             sql="SELECT employee_id, name FROM employee";
                        }
                        else
                        {
                             sql=sql_SkillCat;
                        }
                    }
                    else
                    {
                        if(sql_SkillCat.equals("All"))
                        {
                             sql=sql_SkillLevel; 
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
                              sql=sql_EmpCat;
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
                
        //System.out.println(sql);
        return sql;
    }
    
    private void jbInit() throws Exception {
        this.setLayout(null);
        this.setSize(new Dimension(941, 608));
        jPanel1.setBounds(new Rectangle(10, 5, 930, 95));
        jPanel1.setLayout(null);
        jPanel1.setBorder(BorderFactory.createTitledBorder("Employee Selection"));
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
        btnPrint.setText("Print");
        btnPrint.setBounds(new Rectangle(810, 20, 100, 20));
        btnPrint.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnPrint_actionPerformed(e);
                    }
                });
        btnPickDate.setBounds(new Rectangle(220, 25, 30, 20));
        btnPickDate.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnPickDate_actionPerformed(e);
                    }
                });
        txtDate.setBounds(new Rectangle(25, 25, 205, 20));
        txtDate.setCaption("Date");
        txtDate.setTxtWidth(70);
        txtDate.setLblWidth(120);
        jPanel1.add(txtDate, null);
        jPanel1.add(btnPickDate, null);
        jPanel1.add(cmbSkillLevel, null);
        jPanel1.add(cmbSkillCat, null);
        jPanel1.add(cmbEmpCat, null);
        jPanel1.add(btnPrint, null);
        jScrollPane1.getViewport().add(viewPort, null);
        jPanel2.add(jScrollPane1, null);
        this.add(jPanel2, null);
        this.add(jPanel1, null);
    }

    private void btnPrint_actionPerformed(ActionEvent e) {
        try{
            /*
            Rectangle r = viewPort.getBounds();
            Image image = viewPort.createImage(r.width, r.height);
            Graphics g = image.getGraphics();
            viewPort.paint(g);
            ImageIO.write((RenderedImage)image, "png", new File("C:/employees.png"));  
            messageBar.setMessage("Image was saved to C:/employees.png","OK");
            */
        }
        catch(Exception er){}
    }

    private void btnPickDate_actionPerformed(ActionEvent e) {
        dtp.setLocationRelativeTo(btnPickDate);
        dtp.setDate(txtDate.getInputText());
        dtp.setVisible(true);
        txtDate.setInputText((dtp.DATE.equals("")?txtDate.getInputText():dtp.DATE));
        getEmployeeInformation(txtDate.getInputText());
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

