package com.matrix.focus.mdi;

import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.plastic.theme.ExperienceBlue;
import com.matrix.focus.util.MPanel;
import com.matrix.focus.master.gui.GUIAssetCategory;
import com.matrix.focus.master.gui.GUIAssetModel;
import com.matrix.focus.master.gui.GUIChecklistItem;
import com.matrix.focus.master.gui.GUIConditionMonitoring;
import com.matrix.focus.master.gui.GUIDivision;
import com.matrix.focus.master.gui.GUIEmployee;
import com.matrix.focus.master.gui.GUIPreventiveMaintenance;
import com.matrix.focus.mdi.tabbedPane.CloseTabbedPane;
import com.matrix.focus.register.GUIRegisterMaintenance;
import com.matrix.focus.user.GUIUserManagement;
import com.matrix.focus.util.MImage;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JSplitPane;
import com.matrix.focus.master.GUIAsset;
import com.matrix.focus.master.gui.GUIPart;
import com.matrix.focus.mdi.tabbedPane.CloseListener;
import com.matrix.focus.log.GUIPreventiveMaintenanceLog;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.mdi.tabbedPane.DoubleClickListener;
import com.matrix.focus.report.dss.GUIEmployeeAvailability;
import com.matrix.focus.report.dss.GUIEmployeeDateAvailability;
import com.matrix.focus.report.dss.GUILabourForceAvailability;
import com.matrix.focus.setting.GUIEmployeeDPAvailability;
import com.matrix.focus.setting.GUIEmployeeRates;
import com.matrix.focus.workorder.GUIJobConfirmation;
import com.matrix.focus.workorder.GUIPreventiveMaintenanceWorkOrder;
import com.matrix.focus.register.GUIRegisterCheckList;
import com.matrix.focus.register.GUIRegisterConditionMonitoring;
import com.matrix.focus.user.About;
import com.matrix.focus.user.GUIUserProfile;
import com.matrix.focus.planner.GUIPlanGenerator;
import com.matrix.focus.planner.GUIPlanUpdater;
import com.matrix.focus.util.Authorizer;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.ImageLibrary;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalScrollBarUI;
import javax.swing.plaf.metal.MetalScrollButton;

public class MDI extends JFrame implements ActionListener{

    /** Menus and GUIs*/
    /**************************************************************************/
    public JMenuBar menuBar = new JMenuBar();
    
    private MMenu menuGeneral;
        public MMenuItem menuItemGUICustomer;
            private GUIDivision guiDivision;
        private MMenuItem menuItemAsset;
            private GUIAsset guiAsset;
        public MMenuItem menuItemGUIAssetCategory;
            private GUIAssetCategory guiAssetCategory; 
        public MMenuItem menuItemAssetModel;
            private GUIAssetModel guiAssetModel;
        private MMenuItem menuItemPreventiveMaintenance;
            private GUIPreventiveMaintenance guiPreventiveMaintenance;
        public MMenuItem menuItemConditionMonitoring;
            private GUIConditionMonitoring guiConditionMonitoring;
        public MMenuItem menuItemChecklistItem;
            private GUIChecklistItem guiChecklistItem;
        private MMenuItem menuItemPart;
            private GUIPart guiPart;
        private MMenuItem menuItemGuiEmployee;
            private GUIEmployee guiEmployee;
        private MMenuItem menuItemExit;
    
    private MMenu menuRegisters;
        private MMenuItem menuItemGUIRegisterPreventiveMaintenance;
            private GUIRegisterMaintenance guiRegisterMaintenance;
        private MMenuItem menuItemGUIRegisterConditionMonitoring;
            private GUIRegisterConditionMonitoring guiRegisterConditionMonitoring;
        public MMenuItem menuItemGUIRegisterCheckList;
            private GUIRegisterCheckList guiRegisterCheckList;
    
    private MMenu menuWorkorders;
        public MMenuItem menuItemGUIPreventiveMaintenanceWorkOrder;
            public GUIPreventiveMaintenanceWorkOrder guiPreventiveMaintenanceWorkOrder;
        public MMenuItem menuItemGUIPendingWorkOrders;
            public GUIPendingWorkOrders guiPendingWorkOrders;
    
    private MMenu menuLogs;
        public MMenuItem menuItemGUIPreventiveMaintenanceLog;
            public GUIPreventiveMaintenanceLog guiPreventiveMaintenanceLog;

    private MMenu menuPlanning;
        public MMenuItem menuItemGUIPlanGenerator;
            public GUIPlanGenerator guiPlanGenerator;
        public MMenuItem menuItemGUIPlanUpdater;
            public GUIPlanUpdater guiPlanUpdater;

    private MMenu menuSettings;
        private MMenuItem menuItemGUILabourRates;
            private GUIEmployeeRates guiEmployeeRates;
        private MMenuItem menuItemGUIEmployeeAvailability;
            private GUIEmployeeDPAvailability guiEmployeeDPAvailability;
        
    private MMenu menuInquiries;
        public MMenuItem menuItemJobConfirmation;
            public GUIJobConfirmation guiJobConfirmation;
        public MMenuItem menuItemOncomingVisits0;
        public MMenuItem menuItemOncomingVisits1;
        public MMenuItem menuItemOncomingVisits2;
        public MMenuItem menuItemOncomingVisits3;
        public MMenuItem menuItemOncomingVisits4;
            public GUIOncomingVisits guiOncomingVisits0;
            public GUIOncomingVisits guiOncomingVisits1;
            public GUIOncomingVisits guiOncomingVisits2;
            public GUIOncomingVisits guiOncomingVisits3;
            public GUIOncomingVisits guiOncomingVisits4;
            public MMenuItem menuItemEmployeeTimeAllocation;
            public GUIEmployeeDateAvailability guiEmployeeDateAvailability;
        private MMenuItem menuItemLabourForceAvailability;
            private GUILabourForceAvailability guiLabourForceAvailability;
        private MMenuItem menuItemEmployeeAvailability;
            private GUIEmployeeAvailability guiEmployeeAvilability;

    private MMenu menuReports = new MMenu("Reports");
        
    private MMenu menuLogin;
        private MMenuItem menuItemGUIUserMgt;
            private GUIUserManagement guiUserManagement;
        private MMenuItem menuItemGUIUserProfile;
            private GUIUserProfile guiUserProfile;
    
    private MMenu menuHelp;
        private MMenuItem menuItemAbout;
            private About about;
    /**************************************************************************/
    
    private DBConnectionPool connectionPool;  
    private MessageBar messageBar;
    private JSplitPane splitPane;
    private CommonTaskPane taskPane;
    public CloseTabbedPane tabbedPane;

    private final String TITLE = "Focus - Maintenance Management System";
    /**Loged in Username*/
    public static String USERNAME;
    
    public MDI(DBConnectionPool pool,String username){
        connectionPool = pool;
        USERNAME = username;
        try{
            setTitle(TITLE + "  ..:: logged in as " + username + " ::..");
            setIconImage(Toolkit.getDefaultToolkit().getImage(ImageLibrary.TITLE_BAR));
            PlasticXPLookAndFeel.setCurrentTheme(new mmmm());
            setBackground(new Color(235,235,235));
            jbInit();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        this.addWindowListener(
            new WindowAdapter(){
                public void windowClosing(WindowEvent e){
                    menuItemExit.doClick();
                }
            }
        );
      
  }
  
    private void setMenubar(){  
        //menuBar.setBackground(Color.decode("#191970"));
        setGeneralMenu();
        setSettingsMenu();
        setRegisterMenu();
        setPlanningMenu();
        setWorkordersMenu();
        setLogsMenu();
        setInquiriesMenu();
        //setReportsMenu();
        setLoingMenu();
        setHelpMenu();
    }
  
    private void setGeneralMenu(){
        menuGeneral = new MMenu("General");
        
            if(Authorizer.isCapable(MDI.USERNAME,Authorizer.CUSTOMERS,connectionPool.getConnection())){
                menuItemGUICustomer = new MMenuItem("Customers",ImageLibrary.MENU_DIVISION);
                menuItemGUICustomer.addActionListener(this);
                menuGeneral.add(menuItemGUICustomer);
            }
        
            setGeneralAssetInfoSubMenu();
            setGeneralMaintenanceInfoSubMenu();
            
            if(Authorizer.isCapable(MDI.USERNAME,Authorizer.PARTS_INVENTORY,connectionPool.getConnection())){
                menuItemPart = new MMenuItem("Parts Inventory",ImageLibrary.MENU_PARTS);
                menuItemPart.addActionListener(this);
                menuGeneral.add(menuItemPart);
            }
            
            if(Authorizer.isCapable(MDI.USERNAME,Authorizer.EMPLOYEE,connectionPool.getConnection())){
                menuItemGuiEmployee = new MMenuItem("Employees",ImageLibrary.MENU_EMPLOYEES);  
                menuItemGuiEmployee.addActionListener(this);
                menuGeneral.add(menuItemGuiEmployee);
            }
            
            menuItemExit = new MMenuItem("Exit",ImageLibrary.BUTTON_CANCEL);
            menuItemExit.addActionListener(this);
            menuItemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));  
            menuGeneral.add(menuItemExit);
            
        menuBar.add(menuGeneral);
    }
    
    private void setGeneralAssetInfoSubMenu(){
        MMenu sub = new MMenu("Sales Information",ImageLibrary.MENU_ASSET_INFORMATION);
        int items = 0;
            if(Authorizer.isCapable(MDI.USERNAME,Authorizer.SALES,connectionPool.getConnection())){
                menuItemAsset = new MMenuItem("Machines",ImageLibrary.MENU_ASSET);
                menuItemAsset.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, ActionEvent.CTRL_MASK));
                menuItemAsset.addActionListener(this);
                sub.add(menuItemAsset);
                items++;
            }
            
            if(Authorizer.isCapable(MDI.USERNAME,Authorizer.MACHINE_CATEGORY,connectionPool.getConnection())){
                menuItemGUIAssetCategory = new MMenuItem("Machine Category",ImageLibrary.MENU_ASSET_CATEGORY);
                menuItemGUIAssetCategory.addActionListener(this);
                sub.add(menuItemGUIAssetCategory);
                items++;
            }
            
            if(Authorizer.isCapable(MDI.USERNAME,Authorizer.MACHINE_MODEL,connectionPool.getConnection())){
                menuItemAssetModel = new MMenuItem("Machine Model",ImageLibrary.MENU_ASSET_MODEL);
                menuItemAssetModel.addActionListener(this);
                sub.add(menuItemAssetModel);
                items++;
            }
        if(items!=0){    
            menuGeneral.add(sub);
        }
    }
    
    private void setGeneralMaintenanceInfoSubMenu(){
        MMenu sub = new MMenu("Maintenance Information",ImageLibrary.MENU_MAINTENANCE_INFORMATION);
        int items = 0;
            if(Authorizer.isCapable(MDI.USERNAME,Authorizer.PREVENTIVE_MAINTENANCES_MASTER,connectionPool.getConnection())){
                menuItemPreventiveMaintenance = new MMenuItem("Preventive Maintenance",ImageLibrary.MENU_PREVENTIVE_MAINTENANCE);
                menuItemPreventiveMaintenance.addActionListener(this);
                sub.add(menuItemPreventiveMaintenance );
                items++;
            }
            
            if(Authorizer.isCapable(MDI.USERNAME,Authorizer.READINGS_MASTER,connectionPool.getConnection())){
                menuItemConditionMonitoring = new MMenuItem("Readings",ImageLibrary.MENU_CONDITION_MONITORING);
                menuItemConditionMonitoring.addActionListener(this);
                sub.add(menuItemConditionMonitoring);
                items++;
            }
            
            if(Authorizer.isCapable(MDI.USERNAME,Authorizer.INSPECTIONS_MASTER,connectionPool.getConnection())){
                menuItemChecklistItem = new MMenuItem("Inspections",ImageLibrary.MENU_CHECKLIST);
                menuItemChecklistItem.addActionListener(this);
                sub.add(menuItemChecklistItem );
                items++;
            }
        if(items!=0){    
            menuGeneral.add(sub);
        }   
    }
  
    private void setSettingsMenu(){
        menuSettings = new MMenu("Settings");
        int items = 0;    
            if(Authorizer.isCapable(MDI.USERNAME,Authorizer.EMPLOYEE_RATES,connectionPool.getConnection())){
                menuItemGUILabourRates = new MMenuItem("Employee Rates",ImageLibrary.MENU_LABOUR_RATE);
                menuSettings.add(menuItemGUILabourRates);
                menuItemGUILabourRates.addActionListener(this);
                items++;
            }
            
            if(Authorizer.isCapable(MDI.USERNAME,Authorizer.EMPLOYEE_AVAILABLITY,connectionPool.getConnection())){
                menuItemGUIEmployeeAvailability = new MMenuItem("Employee Availability",ImageLibrary.MENU_EMPLOYEE_AVAILABLITY);
                menuSettings.add(menuItemGUIEmployeeAvailability);
                menuItemGUIEmployeeAvailability.addActionListener(this);
                items++;
            }
        if(items!=0){    
            menuBar.add(menuSettings);
        }     
    }
    
    private void setRegisterMenu(){
        menuRegisters = new MMenu("Registers");
        int items = 0;    
            if(Authorizer.isCapable(MDI.USERNAME,Authorizer.PREVENTIVE_MAINTENANCE_REGISTER,connectionPool.getConnection())){
                menuItemGUIRegisterPreventiveMaintenance = new MMenuItem("Register Preventive Maintenance",ImageLibrary.MENU_PREVENTIVE_MAINTENANCE_REGISTER);
                menuItemGUIRegisterPreventiveMaintenance.addActionListener(this);
                menuItemGUIRegisterPreventiveMaintenance.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, ActionEvent.CTRL_MASK));
                menuRegisters.add(menuItemGUIRegisterPreventiveMaintenance);
                items++;
            }
            
            if(Authorizer.isCapable(MDI.USERNAME,Authorizer.READINGS_REGISTER,connectionPool.getConnection())){
                menuItemGUIRegisterConditionMonitoring = new MMenuItem("Register Readings",ImageLibrary.MENU_CONDITION_MONITORING_REGISTER);
                menuItemGUIRegisterConditionMonitoring.addActionListener(this);
                menuItemGUIRegisterConditionMonitoring.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, ActionEvent.CTRL_MASK));
                menuRegisters.add(menuItemGUIRegisterConditionMonitoring);
                items++;
            }
            
            if(Authorizer.isCapable(MDI.USERNAME,Authorizer.INSPECTIONS_REGISTER,connectionPool.getConnection())){
                menuItemGUIRegisterCheckList = new MMenuItem("Register Inspections",ImageLibrary.MENU_CHECKLIST_REGISTER);
                menuItemGUIRegisterCheckList.addActionListener(this);
                menuItemGUIRegisterCheckList.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F8, ActionEvent.CTRL_MASK));
                menuRegisters.add(menuItemGUIRegisterCheckList);
                items++;
            }
        if(items!=0){    
            menuBar.add(menuRegisters); 
        } 
    }
    
    private void setPlanningMenu(){
        menuPlanning = new MMenu("Planning");
        int items = 0;
            if(Authorizer.isCapable(MDI.USERNAME,Authorizer.PLANNING,connectionPool.getConnection())){
                menuItemGUIPlanGenerator = new MMenuItem("Plan Generator",ImageLibrary.MENU_YEAR_PLAN);
                menuItemGUIPlanGenerator.addActionListener(this);
                menuItemGUIPlanGenerator.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, ActionEvent.CTRL_MASK));  
                menuPlanning.add(menuItemGUIPlanGenerator);
                items++;
            }
            
            if(Authorizer.isCapable(MDI.USERNAME,Authorizer.PLANNING,connectionPool.getConnection())){
                menuItemGUIPlanUpdater = new MMenuItem("Plan Updater",ImageLibrary.MENU_MONTHLY_PLAN);
                menuItemGUIPlanUpdater.addActionListener(this);
                menuItemGUIPlanUpdater.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F10, ActionEvent.CTRL_MASK));  
                menuPlanning.add(menuItemGUIPlanUpdater);
                items++;
            }
        if(items!=0){    
            menuBar.add(menuPlanning); 
        } 
        
    }
    
    private void setWorkordersMenu(){
        menuWorkorders = new MMenu("Jobs");
        int items = 0;
            if(Authorizer.isCapable(MDI.USERNAME,Authorizer.JOB_CREATION,connectionPool.getConnection())){
                menuItemGUIPreventiveMaintenanceWorkOrder = new MMenuItem("Jobs",ImageLibrary.MENU_PREVENTIVE_WORKORDER);
                menuItemGUIPreventiveMaintenanceWorkOrder.addActionListener(this);
                menuItemGUIPreventiveMaintenanceWorkOrder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, ActionEvent.CTRL_MASK));  
                menuWorkorders.add(menuItemGUIPreventiveMaintenanceWorkOrder);
                items++;
            }
        if(Authorizer.isCapable(MDI.USERNAME,Authorizer.JOB_CREATION,connectionPool.getConnection())){
            menuItemGUIPendingWorkOrders = new MMenuItem("Pending Jobs",ImageLibrary.MENU_PREVENTIVE_WORKORDER);
            menuItemGUIPendingWorkOrders.addActionListener(this);
            menuItemGUIPendingWorkOrders.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, ActionEvent.CTRL_MASK));  
            menuWorkorders.add(menuItemGUIPendingWorkOrders);
            items++;
        }
        if(items!=0){    
            menuBar.add(menuWorkorders); 
        } 
    }
    
    private void setLogsMenu(){
        menuLogs = new MMenu("Job Completions");
        int items = 0;
            if(Authorizer.isCapable(MDI.USERNAME,Authorizer.JOB_COMPLETION,connectionPool.getConnection())){
                menuItemGUIPreventiveMaintenanceLog = new MMenuItem("Job Completion",ImageLibrary.MENU_PREVENTIVE_LOG);
                menuItemGUIPreventiveMaintenanceLog.addActionListener(this);
                menuItemGUIPreventiveMaintenanceLog.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F12, ActionEvent.CTRL_MASK));
                menuLogs.add(menuItemGUIPreventiveMaintenanceLog);
                items++;
            }
        if(items!=0){    
            menuBar.add(menuLogs);
        } 
    }
    
    private void setInquiriesMenu(){ 
        menuInquiries = new MMenu("Inquiries");
        int items = 0;    
            if(Authorizer.isCapable(MDI.USERNAME,Authorizer.JOB_CONFIRMATION,connectionPool.getConnection())){
                menuItemJobConfirmation = new MMenuItem("Job Confirmation",ImageLibrary.MENU_CUSTOMER_CONTACT);
                menuItemJobConfirmation.addActionListener(this);
                //menuInquiries.add(menuItemJobConfirmation);
                items++;
            }
            
            if(Authorizer.isCapable(MDI.USERNAME,Authorizer.JOB_CONFIRMATION,connectionPool.getConnection())){
                menuItemOncomingVisits0 = new MMenuItem("Oncoming Visits",ImageLibrary.MENU_CUSTOMER_CONTACT);
                menuItemOncomingVisits0.addActionListener(this);
                menuItemOncomingVisits1 = new MMenuItem("Oncoming Visits",ImageLibrary.MENU_CUSTOMER_CONTACT);
                menuItemOncomingVisits1.addActionListener(this);
                menuItemOncomingVisits2 = new MMenuItem("Oncoming Visits",ImageLibrary.MENU_CUSTOMER_CONTACT);
                menuItemOncomingVisits2.addActionListener(this);
                menuItemOncomingVisits3 = new MMenuItem("Oncoming Visits",ImageLibrary.MENU_CUSTOMER_CONTACT);
                menuItemOncomingVisits3.addActionListener(this);
                menuItemOncomingVisits4 = new MMenuItem("Oncoming Visits",ImageLibrary.MENU_CUSTOMER_CONTACT);
                menuItemOncomingVisits4.addActionListener(this);
                //menuInquiries.add(menuItemJobConfirmation0);
                items++;
            }
            
            if(Authorizer.isCapable(MDI.USERNAME,Authorizer.EMPLOYEE_SCHEDULE,connectionPool.getConnection())){
                menuItemEmployeeTimeAllocation = new MMenuItem("Employee Time Allocation",ImageLibrary.MENU_EMPLOYEE_TIME_ALLOCATION);
                menuItemEmployeeTimeAllocation.addActionListener(this);
                menuInquiries.add(menuItemEmployeeTimeAllocation);
                items++;
            }
            
            if(Authorizer.isCapable(MDI.USERNAME,Authorizer.EMPLOYEE_SCHEDULE,connectionPool.getConnection())){
                menuItemLabourForceAvailability = new MMenuItem("Labour Force Availability",ImageLibrary.MENU_LABOUR_FORCE_AVAILABILITY);
                menuItemLabourForceAvailability.addActionListener(this);
                menuInquiries.add(menuItemLabourForceAvailability);
                items++;
            }
            
            if(Authorizer.isCapable(MDI.USERNAME,Authorizer.EMPLOYEE_SCHEDULE,connectionPool.getConnection())){
                menuItemEmployeeAvailability = new MMenuItem("Labour Schedule",ImageLibrary.MENU_LABOUR_SCHEDULE);
                menuItemEmployeeAvailability.addActionListener(this);
                menuInquiries.add(menuItemEmployeeAvailability);
                items++;
            }
        if(items!=0){    
            menuBar.add(menuInquiries);
        } 
        
    } 
    
    private void setLoingMenu(){
        menuLogin = new MMenu("Login");
            
            if(Authorizer.isCapable(MDI.USERNAME,Authorizer.USERS_MANAGEMENT,connectionPool.getConnection())){
                menuItemGUIUserMgt = new MMenuItem("User Management",ImageLibrary.MENU_USERS);   
                menuItemGUIUserMgt.addActionListener(this);
                menuLogin.add(menuItemGUIUserMgt);
            }
            
            menuItemGUIUserProfile = new MMenuItem("User Profile",ImageLibrary.MENU_USER);
            menuItemGUIUserProfile.addActionListener(this);
            menuLogin.add(menuItemGUIUserProfile);
        
        menuBar.add(menuLogin);
    }
    
    private void setHelpMenu(){
        menuHelp = new MMenu("Help");
        
            menuItemAbout = new MMenuItem("About",ImageLibrary.MENU_ABOUT); 
            menuItemAbout.addActionListener(this);
            menuHelp.add(menuItemAbout);
            
        menuBar.add(menuHelp);
    }
    
    private void setReportsMenu(){
        menuBar.add(menuReports);
    }

    
    private void jbInit() throws Exception{
        /**MDI Properties*/
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.getContentPane().setLayout(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        Rectangle screenRec = this.getGraphicsConfiguration().getBounds();
        this.setBounds(screenRec.x,screenRec.y,screenRec.width,screenRec.height-25);

        setMenubar();
        this.getRootPane().setJMenuBar(menuBar);
    
        final JPanel glass = (JPanel) this.getGlassPane();
        glass.setLayout(null);
        JLabel logo = new JLabel(new ImageIcon(ImageLibrary.FOCUS_LOGO));
        logo.setBounds(screenRec.width-160,0,200,75);
        glass.add(logo);
        glass.setVisible(true);
        
        messageBar = new MessageBar();
        messageBar.setBounds(0,0,1200,40);
	this.getContentPane().add(messageBar);
    
        tabbedPane = new CloseTabbedPane();
        
        tabbedPane.addDoubleClickListener(new DoubleClickListener(){
                    public void doubleClickOperation(MouseEvent e) {
                        splitPane.setDividerLocation((splitPane.getDividerLocation()==1?190:1));
                    }
                });
        tabbedPane.addTab("Welcome",new MImage(ImageLibrary.WALLPAPER));
        
        taskPane = new CommonTaskPane(this,connectionPool,tabbedPane,messageBar);
        
        tabbedPane.addCloseListener(new CloseListener(){
            public void closeOperation(MouseEvent e, int overTabIndex) {
                try{
                    MPanel panel = (MPanel)tabbedPane.getComponentAt(overTabIndex);
                    panel.close();
                }
                catch(Exception er){
                er.toString();
                }
                finally{
                    if(overTabIndex!=0){
                        tabbedPane.removeTabAt(overTabIndex);
                    }
                    messageBar.clearMessage();
                }
            }
        });
        
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, taskPane, tabbedPane);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(190);
        splitPane.setBounds(0,40,screenRec.width,screenRec.height-130);
        this.getContentPane().add(splitPane);
    }
 
    
    class mmmm extends ExperienceBlue{
        public ColorUIResource getFocusColor() {
            return new ColorUIResource(new Color(118,206,81));
        }
    }
 
    public static  class SBUI extends MetalScrollBarUI{
         
                 
        public SBUI(){}
        public static ComponentUI createUI(JComponent c ){
            return  new SBUI();  
        }
           
        protected JButton createDecreaseButton(int orientation){
           decreaseButton = new MetalScrollButton( orientation, scrollBarWidth, isFreeStanding );
           decreaseButton.setBackground(new Color(231,231,231));
           decreaseButton.setBorder(BorderFactory.createRaisedBevelBorder());
           return decreaseButton;
        }

        /** Returns the view that represents the increase view. */
        protected JButton createIncreaseButton( int orientation ){
           increaseButton =  new MetalScrollButton( orientation, scrollBarWidth, isFreeStanding );
           increaseButton.setBackground(new Color(231,231,231));
           increaseButton.setBorder(BorderFactory.createRaisedBevelBorder());
           return increaseButton;
       }
    }
  
    private void loadingGUI(){
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));   
        messageBar.setMessage("","CLOCK");
    }
    
    private void loadedGUI(){
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));    
        messageBar.setMessage("Successfully Loaded","OK");
    }
    
    public void actionPerformed(ActionEvent e){
        Object click = e.getSource();
        
        if(click==menuItemGUICustomer){
            loadingGUI();
            //if(guiDivision==null){
                guiDivision = new GUIDivision(connectionPool,this, messageBar);
            //}
            tabbedPane.addTab("Customers",guiDivision);
            loadedGUI();
        }
        else if(click==menuItemAsset){
            loadingGUI();
            //if(guiAsset==null){
                guiAsset = new GUIAsset(connectionPool,this,messageBar);
            //}
            tabbedPane.addTab("Sales/Machine Master",guiAsset);
            loadedGUI();
        }
        else if(click==menuItemAssetModel){
            loadingGUI();
            //if(guiAssetModel==null){
                guiAssetModel =  new GUIAssetModel(connectionPool,this, messageBar);
            //}
            tabbedPane.addTab("Machine Model",guiAssetModel);
            loadedGUI();
        }
        else if(click==menuItemGUIAssetCategory){
            loadingGUI();
            //if(guiAssetCategory==null){
                guiAssetCategory = new GUIAssetCategory(connectionPool,this, messageBar);
            //}
            tabbedPane.addTab("Machine Category",guiAssetCategory);
            loadedGUI();
        }
        else if(click==menuItemPart){
            loadingGUI();
            //if(guiPart==null){
                guiPart =  new GUIPart(connectionPool,this, messageBar);
            //}
            tabbedPane.addTab("Parts Inventory",guiPart);
            loadedGUI();
        }
        else if(click==menuItemGuiEmployee){
            loadingGUI();
            //if(guiEmployee==null){
                guiEmployee = new GUIEmployee(connectionPool,this, messageBar);
            //}
            tabbedPane.addTab("Employees",guiEmployee);
            loadedGUI();
        }
        else if(click==menuItemConditionMonitoring){
            loadingGUI();
            //if(guiConditionMonitoring==null){
                guiConditionMonitoring= new GUIConditionMonitoring(connectionPool,this, messageBar);
            //}
            tabbedPane.addTab("Readings Master", guiConditionMonitoring);
            loadedGUI();
        }
        else if(click==menuItemChecklistItem){
            loadingGUI();
            //if(guiChecklistItem==null){
                guiChecklistItem = new GUIChecklistItem(connectionPool,this, messageBar);
            //}
            tabbedPane.addTab("Inspection Master",guiChecklistItem);
            loadedGUI();
        }
        else if(click==menuItemPreventiveMaintenance){
            loadingGUI();
            //if(guiPreventiveMaintenance==null){
                guiPreventiveMaintenance = new GUIPreventiveMaintenance(connectionPool,this, messageBar);  
            //}
            tabbedPane.addTab("Preventive Maintenance",guiPreventiveMaintenance);
            loadedGUI();
        }
        else if(click==menuItemGUIRegisterPreventiveMaintenance){
            loadingGUI();
            //if(guiRegisterMaintenance==null){
                guiRegisterMaintenance = new GUIRegisterMaintenance(connectionPool,this,messageBar);
            //}
            tabbedPane.addTab("Register Maintenance",guiRegisterMaintenance);
            loadedGUI();
        }
        else if(click==menuItemGUIPreventiveMaintenanceWorkOrder){
            loadingGUI();
            //if(guiPreventiveMaintenanceWorkOrder==null){
                guiPreventiveMaintenanceWorkOrder = new GUIPreventiveMaintenanceWorkOrder(connectionPool,this,messageBar);
            //}
            tabbedPane.addTab("Job",guiPreventiveMaintenanceWorkOrder);
            loadedGUI();
        }
        else if(click==menuItemGUIPendingWorkOrders){
            loadingGUI();
            //if(guiPendingWorkOrders==null){
                guiPendingWorkOrders = new GUIPendingWorkOrders(connectionPool,this,messageBar);
            //}
            tabbedPane.addTab("Pending Jobs",guiPendingWorkOrders);
            loadedGUI();
        }
        else if(click==menuItemGUIRegisterConditionMonitoring){
            loadingGUI();
            //if(guiRegisterConditionMonitoring==null){
                guiRegisterConditionMonitoring = new GUIRegisterConditionMonitoring(connectionPool,this,messageBar);
            //}
            tabbedPane.addTab("Register Readings",guiRegisterConditionMonitoring);
            loadedGUI();
        }
        else if(click==menuItemGUIRegisterCheckList){
            loadingGUI();
            //if(guiRegisterCheckList==null){
                guiRegisterCheckList = new GUIRegisterCheckList(connectionPool,this,messageBar);
            //}
            tabbedPane.addTab("Register Inspections",guiRegisterCheckList);
            loadedGUI();
        }
        else if(click==menuItemGUIPlanGenerator){
            loadingGUI();
            //if(guiPlanGenerator==null){
                guiPlanGenerator = new GUIPlanGenerator(connectionPool,this,messageBar);
            //}
            tabbedPane.addTab("Plan Generator",guiPlanGenerator);
            loadedGUI();
        }
        else if(click==menuItemGUIPlanUpdater){
            loadingGUI();
            //if(guiPlanUpdater==null){
                guiPlanUpdater = new GUIPlanUpdater(connectionPool,this,messageBar);
            //}
            tabbedPane.addTab("Plan Updater",guiPlanUpdater);
            loadedGUI();
        }
        else if(click==menuItemGUIPreventiveMaintenanceLog){
            loadingGUI();
            //if(guiPreventiveMaintenanceLog==null){
                guiPreventiveMaintenanceLog  = new GUIPreventiveMaintenanceLog(connectionPool,this,messageBar);
            //}
            tabbedPane.addTab("Job Completion",guiPreventiveMaintenanceLog);
            loadedGUI();
        }
        else if(click == menuItemGUIUserMgt){
            loadingGUI();
            //if(guiUserManagement==null){
                guiUserManagement = new GUIUserManagement(connectionPool,this,messageBar);
            //}
            tabbedPane.addTab("User Management",guiUserManagement);
            loadedGUI();
        }
        else if(click == menuItemGUIUserProfile){
            loadingGUI();
            //if(guiUserProfile==null){
                guiUserProfile = new GUIUserProfile(connectionPool,this,messageBar);
            //}
            tabbedPane.addTab("User Profile",guiUserProfile);
            loadedGUI();
        }
        else if(click == menuItemJobConfirmation){
            loadingGUI();
            //if(guiJobConfirmation==null){
                guiJobConfirmation = new GUIJobConfirmation(connectionPool,this,messageBar);
            //}
            tabbedPane.addTab("Job Confirmation",guiJobConfirmation);
            loadedGUI();
        }
        else if(click == menuItemOncomingVisits0){
            loadingGUI();
            //if(guiOncomingVisits0==null){
                guiOncomingVisits0 = new GUIOncomingVisits(connectionPool,this,messageBar);
            //}
            tabbedPane.addTab("Oncoming Visits - This week",guiOncomingVisits0);
            loadedGUI();
        }
        else if(click == menuItemOncomingVisits1){
            loadingGUI();
            //if(guiOncomingVisits1==null){
                guiOncomingVisits1 = new GUIOncomingVisits(connectionPool,this,messageBar);
            //}
            tabbedPane.addTab("Oncoming Visits - Next week",guiOncomingVisits1);
            loadedGUI();
        }
        else if(click == menuItemOncomingVisits2){
            loadingGUI();
            //if(guiOncomingVisits2==null){
                guiOncomingVisits2 = new GUIOncomingVisits(connectionPool,this,messageBar);
            //}
            tabbedPane.addTab("Oncoming Visits - Next 2 weeks",guiOncomingVisits2);
            loadedGUI();
        }
        else if(click == menuItemOncomingVisits3){
            loadingGUI();
            //if(guiOncomingVisits3==null){
                guiOncomingVisits3 = new GUIOncomingVisits(connectionPool,this,messageBar);
            //}
            tabbedPane.addTab("Oncoming Visits - Next 3 weeks",guiOncomingVisits3);
            loadedGUI();
        }
        else if(click == menuItemOncomingVisits4){
            loadingGUI();
            //if(guiOncomingVisits4==null){
                guiOncomingVisits4 = new GUIOncomingVisits(connectionPool,this,messageBar);
            //}
            tabbedPane.addTab("Oncoming Visits - Next month",guiOncomingVisits4);
            loadedGUI();
        }
        else if(click == menuItemEmployeeTimeAllocation){
            loadingGUI();
            //if(guiEmployeeDateAvailability==null){
                guiEmployeeDateAvailability = new GUIEmployeeDateAvailability(connectionPool,this,messageBar);
            //}
            tabbedPane.addTab("Employee Time Allocation",guiEmployeeDateAvailability);
            loadedGUI();
        }
        else if(click==menuItemEmployeeAvailability){
            loadingGUI();
            //if(guiEmployeeAvilability==null){
                guiEmployeeAvilability = new GUIEmployeeAvailability(connectionPool,this,messageBar,tabbedPane);
            //}
            tabbedPane.addTab("Labour Schedule",guiEmployeeAvilability);
            loadedGUI();
        } 
        else if(click == menuItemLabourForceAvailability){
            loadingGUI();
            //if(guiLabourForceAvailability==null){
                guiLabourForceAvailability = new GUILabourForceAvailability(connectionPool,this,messageBar);
            //}
            tabbedPane.addTab("Labour Force Availability",guiLabourForceAvailability);
            loadedGUI();
        }
        else if(click == menuItemAbout){
            about = new About(this,TITLE);
            about.setLocationRelativeTo(this);
            about.setVisible(true);
        }
        else if(click == menuItemGUILabourRates){
            loadingGUI();
            //if(guiEmployeeRates==null){
                guiEmployeeRates = new GUIEmployeeRates(connectionPool,this,messageBar);
            //}
            tabbedPane.addTab("Employee Rates",guiEmployeeRates);
            loadedGUI();
        }
        else if(click == menuItemGUIEmployeeAvailability){
            loadingGUI();
            //if(guiEmployeeDPAvailability==null){
                guiEmployeeDPAvailability = new GUIEmployeeDPAvailability(connectionPool,this,messageBar);
            //}
            tabbedPane.addTab("Employee Availability",guiEmployeeDPAvailability);
            loadedGUI();
        }
        else if(click == menuItemExit){
            if(MessageBar.showConfirmDialog(this,"Are you sure you want to exit from the system?","Focus")==MessageBar.YES_OPTION){
                System.exit(0);
            }
        }
    }
}

 