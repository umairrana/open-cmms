package com.matrix.focus.report.dss;


import com.matrix.components.MDataCombo;
import com.matrix.focus.util.MPanel;
import com.matrix.focus.mdi.MDI;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.mdi.tabbedPane.CloseTabbedPane;
import com.matrix.focus.workorder.TimeRulerDialog;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.ImageLibrary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.ButtonUI;

public class GUIEmployeeAvailability extends MPanel{

        private Connection connection;        
        private MDI frame;
        private MessageBar messageBar;
        private AvailabilityPlan availabilityPlan;
        private JPanel mainPanel;       
        private JSplitPane splitPane;
        private JTextField txtYear;
        private JComboBox cboMonth;
        private JLabel labelYear=new JLabel("  Year");
        private JLabel labelMonth=new JLabel("  Month");
        private JButton bView=new JButton("View");
        private MDataCombo cmbEmpCat=new MDataCombo();        
        private MDataCombo cmbSkillCat=new MDataCombo();
        private MDataCombo cmbSkillLevel=new MDataCombo();       
        private JButton bNext=new JButton(">");
        private JButton bPrevious=new JButton("<");
        private Vector list=new Vector();        
        private Connection dCon;
        private CloseTabbedPane tabbedPane;
        private Color color=new Color(39,140,222);
       
        
        public GUIEmployeeAvailability(DBConnectionPool pool, MDI frame, MessageBar msgBar,CloseTabbedPane tabbedPane){
        
            connection = pool.getConnection();
            dCon = pool.getConnection();            
            this.frame = frame;
            this.messageBar = msgBar;
            this.tabbedPane=tabbedPane;
            
            try{ 
            
                jbInit();                    
                setBackground(new Color(235,235,235));               
            }  
            catch(Exception e){
                e.printStackTrace();                
            }
            
        }
           
        public void close(){
            try{
                connection.close();
            } 
            catch(SQLException e){
                e.printStackTrace();
            }
    }
    
        private void jbInit() throws Exception{
        
            setBounds(0,0,500,500);
            JPanel jP=new JPanel(){{
            setBounds(320,20,180,120);
            setLayout(null);
                setBorder(new TitledBorder("Period Selection"));
            }};
            this.setLayout(new BorderLayout());
            this.setSize(new Dimension(982, 500));
            bView.setIcon(new ImageIcon(ImageLibrary.BUTTON_VIEW));
            //this.setSize(new Dimension(813, 400));
            mainPanel = new JPanel();
            txtYear=new JTextField();
            cboMonth=new JComboBox(new Object[]{"January","February","March","April","May","June","July","August","September","October","November","December"});                                                                                                                                                                                                                  
            
            txtYear.setText(String.valueOf(Calendar.getInstance(TimeZone.getDefault()).get(Calendar.YEAR)));   
            cboMonth.setSelectedIndex(new Integer(Calendar.getInstance(TimeZone.getDefault()).get(Calendar.MONTH)));                       
            
            txtYear.addKeyListener(new KeyAdapter(){
                public void keyTyped(KeyEvent e){
                   
                try{
                    
                        Integer.parseInt(String.valueOf(e.getKeyChar()));
                        if(txtYear.getText().length()>3){
                        throw new Exception();
                        }
                    }
                    catch(Exception ex){
                          e.setKeyChar((char)11);
                    }
                
                             
                }
            });
            
            bNext.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    txtYear.setText(String.valueOf(Integer.parseInt(txtYear.getText())+1));
                }
            });
            
            bPrevious.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e){
          if(Integer.parseInt(txtYear.getText())>0){
              txtYear.setText(String.valueOf(Integer.parseInt(txtYear.getText())-1));
          }
          }
      });  
      
            labelYear.setHorizontalAlignment(SwingConstants.LEFT);
            labelMonth.setHorizontalAlignment(SwingConstants.LEFT);
            
            labelYear.setBounds(15,30,80,20);  
            txtYear.setBounds(120,30,50,20);
            bPrevious.setBounds(175,30,30,20);
            bNext.setBounds(210,30,30,20);
            bView.setBounds(760,30,70,20);            
            labelMonth.setBounds(335,30,80,20); 
            cboMonth.setBounds(440,30,95,20);
      
              
              
            
            
            bView.setFont(new Font("Tahoma",Font.PLAIN,11));
            
            bView.addActionListener(new ActionListener(){
      
                      public void actionPerformed(ActionEvent e) {
                      // check if categoriesTree is enabled. It will be enabled if the user has selected the line. 
                        list.removeAllElements();
                            
                            
                             String sql = GUIEmployeeAvailability.this.getEmployeesSQL(txtYear.getText(),cboMonth.getSelectedIndex()+1+"");
                             ResultSet rec;
                             try{
                               rec=connection.createStatement().executeQuery(sql);
                               
                               while(rec.next()){
                               
                                Vector temp=new Vector();
                                temp.add(rec.getString(1));
                                temp.add(rec.getString(2).substring(8,10));
                                temp.add(rec.getString(3).substring(8,10));
                                
                                list.add(temp);
                                
                               }
                             }
                             catch(Exception ex){
                             
                             }
                               
                               
                             if(list.size()>0){
                              availabilityPlan = new AvailabilityPlan(Integer.parseInt(txtYear.getText()),cboMonth.getSelectedIndex()+1,list);
                              int dLocation=splitPane.getDividerLocation();
                              splitPane.setRightComponent(availabilityPlan);
                              splitPane.setDividerLocation(dLocation);                              
                              //bView.setEnabled(false);                              
                                                      
                         }
                         else
                              messageBar.setMessage("No Employee found.","ERROR");
                      }
                  }); 
                                                       
            mainPanel.setLayout(null);   
                
            cmbEmpCat.populate(connection,"SELECT DISTINCT title FROM employee");
            cmbEmpCat.setCaption("Employee Category");
            cmbEmpCat.setLblWidth(100);
            cmbEmpCat.setCmbWidth(200);
            cmbSkillCat.populate(connection,"SELECT DISTINCT maintenance_category FROM employee_skills");
            cmbSkillCat.setCaption("Skill Category");
            cmbSkillCat.setLblWidth(100);
            cmbSkillCat.setCmbWidth(200);
            cmbSkillLevel.populate(connection,"SELECT DISTINCT skill_level FROM employee");
            cmbSkillLevel.setCaption("Skill Level");
            cmbSkillLevel.setLblWidth(100);
            cmbSkillLevel.setCmbWidth(200);
            cmbEmpCat.setBounds(20,60,300,20);
            cmbSkillCat.setBounds(340,60,300,20);
            cmbSkillLevel.setBounds(660,60,300,20);
            
            JPanel p=new JPanel();
            p.setBounds(10,20,980,100);
            p.setLayout(null);
            p.setBorder(new TitledBorder("Employee Selection"));
            //mainPanel.add(jP);       
            p.add(cmbEmpCat);
            p.add(cmbSkillCat);
            p.add(cmbSkillLevel); 
            
            p.add(labelYear);            
            p.add(txtYear);            
            p.add(bPrevious);            
            p.add(bNext);            
            p.add(bView);   
            p.add(labelMonth);
            p.add(cboMonth);  
      
            mainPanel.add(p);
            splitPane=new JSplitPane(JSplitPane.VERTICAL_SPLIT,mainPanel,new JPanel());
            
           /*splitPane.addPropertyChangeListener(new PropertyChangeListener(){
                            public void propertyChange(PropertyChangeEvent evt) {
                                treePanel.setBounds(300,10,500,splitPane.getDividerLocation()-60);
                            }
                        });*/
            splitPane.setDividerLocation(140);
            //splitPane.setBounds(new Rectangle(5, 5,1000,600));
            add(splitPane,BorderLayout.CENTER);
    
  }
  
        private String getEmployeesSQL(String targetyear,String targetmonth){
    
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
                sql_EmpCat = "(SELECT employee_id FROM employee WHERE title ='" + emp_cat + "')";
            }
            
            if(cmbSkillCat.getComboBox().getSelectedIndex()==0){
                /**All*/
                 sql_SkillCat = "All";
            }
            else{
                sql_SkillCat ="(SELECT e.employee_id FROM employee e, employee_skills s WHERE e.employee_id = s.employee_id AND s.Maintenance_Category ='" + skl_cat + "')"; 
            }
            
            if(cmbSkillLevel.getComboBox().getSelectedIndex()==0){
                /**All*/
                 sql_SkillLevel = "All";
            }
            else{
                sql_SkillLevel = "(SELECT employee_id FROM employee WHERE skill_level ='" + skl_lvl + "')";
            }
            
            String sql = "";
            
            
            if(sql_EmpCat.equals("All"))
                    {
                        if(sql_SkillLevel.equals("All"))
                        {
                            if(sql_SkillCat.equals("All"))
                            {
                                 sql="SELECT employee_id FROM employee ";
                            }
                            else
                            {
                                // sql=sql_SkillCat;
                                sql="SELECT employee_id FROM  ("+sql_SkillCat+" as a )";
                            }
                        }
                        else
                        {
                            if(sql_SkillCat.equals("All"))
                            {
                                // sql=sql_SkillLevel;
                                sql="SELECT employee_id FROM  ("+sql_SkillLevel+" as a )";
                                 
                            }
                            else
                            {
                                 sql="SELECT employee_id FROM  ("+sql_SkillCat+" as a ) INNER JOIN ("+sql_SkillLevel+" as b) USING (employee_id) "; 
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
                                sql="SELECT employee_id FROM  ("+sql_EmpCat+" as a )";
                            }
                            else
                            {
                                  sql="SELECT employee_id FROM  ("+sql_EmpCat+" as a ) INNER JOIN ("+sql_SkillCat+" as b) USING (employee_id) "; 
                            }
                                 
                        }
                        else
                        {
                            if(sql_SkillCat.equals("All"))
                            {
                                  sql="SELECT employee_id FROM  ("+sql_EmpCat+" as a ) INNER JOIN ("+sql_SkillLevel+" as b) USING (employee_id) "; 
                            }
                            else
                            {
                                 String sql_EmpCat_SkillLevel="(SELECT employee_id FROM  ("+sql_EmpCat+" as a ) INNER JOIN ("+sql_SkillLevel+" as b) USING (employee_id)) "; 
                                 sql="SELECT employee_id FROM  ("+sql_EmpCat_SkillLevel+" as c ) INNER JOIN ("+sql_SkillCat+" as d) USING (employee_id) "; 
                            }
                        }
                    }
                    
            sql="SELECT DISTINCT pp.Employee_ID,DATE(planned_start_time) as From_Date, DATE(planned_end_time) as To_Date FROM" + 
                    " ((SELECT lu.Employee_ID,planned_start_time, planned_end_time FROM labour_utilisation lu,employee e  WHERE " + 
                    " lu.Employee_ID =e.Employee_ID and lu.planned_start_time!='0000-00-00 00:00:00'  )" + 
                    " UNION " + 
                    " (SELECT luupm.Employee_ID,planned_start_time, planned_end_time FROM labour_utilization_for_up_mnt  luupm,employee e " + 
                    "  WHERE  luupm.Employee_ID =e.Employee_ID and luupm.planned_start_time!='0000-00-00 00:00:00')) AS pp" + 
                    "  WHERE ((YEAR(planned_start_time)='"+targetyear+"' and MONTH(planned_start_time)='"+ targetmonth +"') or " +
                    "(YEAR(planned_end_time)='"+ targetyear +"' and MONTH(planned_end_time)='"+ targetmonth +"')) AND employee_id in ("+ sql +") ORDER BY Employee_ID ASC" ;   
            
            return sql;
        }
                         
        class AvailabilityPlan extends JPanel{
   
            private int year;
            private int month;            
            private Vector list;
            private JButton hLine;
            private EmployeeGrid eGrid;
            private JComponent plan;
            private JPanel p=new JPanel(){{
                        setLayout(new GridLayout(1,1));
                    }
            };
            
                        
            private String getMonthName(){        
               return new String[]{"January", "February","March", "April", "May", "June", "July", "August", "September", "October", "November","December"}[month-1];
            }
        
            private int getDayCount(){                
                return new GregorianCalendar(Integer.parseInt(txtYear.getText()),cboMonth.getSelectedIndex(), 1).getActualMaximum(Calendar.DAY_OF_MONTH);
            }
        
            public AvailabilityPlan(int year,int month, Vector list){
            
                    this.month=month;
                    this.year=year;                                        
                    this.list=list;                                     
                    setLayout(new BorderLayout());
                    setBackground(Color.LIGHT_GRAY);                                                                            
                    hLine=new JButton(new ImageIcon(ImageLibrary.UTIL_25X25)){
                        {
                           
                            setBackground(new Color(197,213,233));
                            setForeground(Color.BLACK);
                            setBounds(0,0,1000,50);
                           
                        }
                    
                        public void paintComponent(Graphics g){
                                super.paintComponent(g);
                                //g.setColor(Color.BLUE);
                                int x= 10;
                                int y = 17;
                                g.drawString(AvailabilityPlan.this.year +"   "+getMonthName(), x, y);
                        
                                x = 160;
                                for(int i=0;i<getDayCount()+2;i++){
                                        if (i==0){
                                                g.drawString("<<", x+3, y);
                                        }
                                        else if(i==getDayCount()+1){
                                            g.drawString(">>", x+3, y);
                                        }
                                        else
                                            g.drawString(i+"", x+3, y);
                                            
                                        x += 25;
                                }
                        }
                    
                    };        
                    eGrid = new EmployeeGrid();
                    setPlan(eGrid);                                                                                      
            }
            
            private void setPlan(JComponent component){
                p.removeAll();       
                p.add(hLine);                
                plan=component;
                removeAll();
                add(p,BorderLayout.NORTH);
                add(new JScrollPane(component),BorderLayout.CENTER);                
                updateUI();
                int dLocation=splitPane.getDividerLocation();
                splitPane.setRightComponent(this);
                splitPane.setDividerLocation(dLocation);
            }
            
            private JComponent getPlan(){
                return plan;
            }
                  
            class EmployeeGrid extends JTextArea{
    
                    private EmployeePanel employeePanel[];
                            
                    public EmployeeGrid(){
                    
                            setBackground(new Color(231,231,231));
                            setLayout(null);
                            setColumns(80);
                            setEditable(false);
                            employeePanel=new EmployeePanel[list.size()];
                            addEmployeePanel();                           
                    }
               
                    private void addEmployeePanel(){
                        int y = 0;    
                        int count=0;
                        setRows((40+list.size()*26)/this.getRowHeight());
                        
                        for(int i=0;i<list.size();i++){
                        
                            for(int c=0;c<employeePanel.length;c++){
                                if(employeePanel[c] != null &&((Vector)list.elementAt(i)).elementAt(0).toString().equals(employeePanel[c].employeeID) ){
                                    employeePanel[c].allocate(Integer.parseInt(((Vector)list.elementAt(i)).elementAt(1).toString()),Integer.parseInt(((Vector)list.elementAt(i)).elementAt(2).toString()));
                                    //System.out.println(((Vector)list.elementAt(i)).elementAt(0).toString()+"-"+Integer.parseInt(((Vector)list.elementAt(i)).elementAt(1).toString()) + "  "+Integer.parseInt(((Vector)list.elementAt(i)).elementAt(2).toString()));
                                    break;
                                }
                                
                                if(c==employeePanel.length-1){
                                    employeePanel[count] = new EmployeePanel(((Vector)list.elementAt(i)).elementAt(0).toString(), 0, y);
                                    employeePanel[count].allocate(Integer.parseInt(((Vector)list.elementAt(i)).elementAt(1).toString()),Integer.parseInt(((Vector)list.elementAt(i)).elementAt(2).toString()));
                                    add(employeePanel[count]); 
                                    count++;
                                    y+=26;
                                }
                            }
                                 
                        }
                    }
                                                               
                    class EmployeePanel extends JPanel{
                    
                            private DateCell[] dateCell;                       
                            private Employee employee;    
                            private int left;
                            private int top;
                            private String employeeID;                            
                            //int startDate;
                           // int endDate;                            
                            
                            public EmployeePanel(String employeeID, int left, int top){
                                                                     
                                      this.left = left ;
                                      this.top = top;
                                     // this.startDate=startDate;
                                     // this.endDate=endDate;
                                      this.employeeID=employeeID;
                                      setLayout(null);
                                      setBounds(left,top,1000,25); 
                                      dateCell = new DateCell[getDayCount()+2];
                                      int x = left+155;                                                                      
                                      
                                      for(int i=0;i<dateCell.length;i++){                                                                             
                                          dateCell[i]=new DateCell(i,x,0);
                                          add(dateCell[i]);                                                                            
                                          x += 25;
                                      }
                                      
                                     
                                      
                                      employee=new Employee();                                     
                                      add(employee);                                                                                                                    
                            }                                                                                                  
                        
                            public void allocate(int startDate,int endDate){
                                for(int c=0;c<dateCell.length;c++){
                                
                                    if(dateCell[c].day>=startDate && dateCell[c].day<=endDate ){
                                    
                                        
                                        dateCell[c].setBackground(color);
                                        dateCell[c].setForeground(color);
                                       //new MyButtonUI(color)
                                        
                                        
                                    }
                                }
                            } 
                            
                            class Employee extends JButton{
                                                                      
                                    
                                    public Employee(){
                                    
                                            setHorizontalAlignment(SwingConstants.LEFT);                                            
                                            setBackground(new Color(221,230,242));
                                            setForeground(Color.BLACK);
                                            setLayout(null);
                                            setBounds(0,0,155,25);
                                            
                                            try {
                                                PreparedStatement pS = connection.prepareStatement("SELECT Name FROM Employee WHERE Employee_ID= ?");
                                                pS.setString(1,""+employeeID);
                                                ResultSet r = pS.executeQuery();
                                                r.first();
                                                this.setText(employeeID + " - "+r.getString(1));
                                            }
                                            catch (SQLException e) {
                                                
                                            }
                                            
                                           
                                            
                                           
                                                                                                
                                            addActionListener(new ActionListener(){
                                                public void actionPerformed(ActionEvent e) {
                                                  //System.out.println(mGrid);
                                                   
                                                }
                                            });
                                            
                                    }
                                                                                                                                                                                                                                                                                                              
                            }
                  
                            class DateCell extends JButton { 
                                                                                                                                                                                                
                                private int left;
                                private int top;
                                                                 
                                private int year ,month,date,day;
                               
                    
                                      
                                public DateCell(final int day,int left, int top){ 
                                
                                    this.day=day;
                                    addActionListener(new ActionListener(){
                                    public void actionPerformed(ActionEvent e) {
                                        if(getBackground().equals(color )){
                                            TimeRulerDialog t = new TimeRulerDialog(frame,new JDialog(),connection);
                                            String sql="select name from employee where Employee_ID='"+employeeID+"'";
                                            try{
                                            ResultSet r=connection.createStatement().executeQuery(sql);
                                            r.next();
                                            t.setAssignedInfo(employeeID,r.getString(1),year+"-"+ (month<10 ? 0+""+month:month+"")+"-"+(day<10 ? 0+""+day:day+""));
                                            t.setLocationRelativeTo(GUIEmployeeAvailability.this);
                                            t.setVisible(true);
                                            }
                                            catch(Exception ex){}
                                        }
                                    }
                                });
                                
                                    if(day==0){                                    
                                        GregorianCalendar c=new GregorianCalendar(AvailabilityPlan.this.year,AvailabilityPlan.this.month-1, 1);
                                        c.add(Calendar.DAY_OF_MONTH,-1);
                                        year=c.get(Calendar.YEAR);
                                        month=(c.get(Calendar.MONTH)+1);
                                        date=c.get(Calendar.DATE)  ;
                                        setToolTipText(year+"-"+month+"-"+date);
                                        setBackground(new Color(211,210,227));
                                        setForeground(new Color(211,210,227));
                                    }
                                    else if(day>getDayCount()){
                                        GregorianCalendar c=new GregorianCalendar(AvailabilityPlan.this.year,AvailabilityPlan.this.month-1, getDayCount());
                                        c.add(Calendar.DAY_OF_MONTH,1);
                                        year=c.get(Calendar.YEAR);
                                        month=(c.get(Calendar.MONTH)+1);
                                        date=c.get(Calendar.DATE);
                                        setToolTipText(year+"-"+month+"-"+date);
                                        setBackground(new Color(211,210,227));
                                        setForeground(new Color(211,210,227));
                                    }
                                    else{
                                        year=AvailabilityPlan.this.year;
                                        month=AvailabilityPlan.this.month;
                                        date=day;
                                        setBackground(Color.WHITE);
                                        setForeground(Color.white);
                                        
                                       
                                    }
                                    
                                  
                                                                         
                                    this.left = left;
                                    this.top = top;
                                    setBounds(left, top, 25, 25);                                              
                                                                                                                               
                                }
                                      
                                 public void setUI(ButtonUI ui){
                                    super.setUI(ui);
                                 }                                
                                                    
                            }
                               
                    }
            }
    
    }  
}


