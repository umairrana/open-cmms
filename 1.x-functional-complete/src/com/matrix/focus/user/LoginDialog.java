package com.matrix.focus.user;

import com.matrix.focus.mdi.MDI;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.ImageLibrary;
import com.matrix.focus.util.MImage;
import com.matrix.focus.util.Utilities;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.net.URL;

import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;


public class LoginDialog extends JDialog{
    private MImage imgBackground;
    private JLabel lblUsnm= new JLabel("Username");
    private JLabel lblPswd = new JLabel("Password");
    private JTextField txtUsnm = new JTextField();
    private JPasswordField txtPswd = new JPasswordField();
    private JButton btnCancel = new JButton("Cancel");
    private JButton btnOk = new JButton("OK");
    private JProgressBar progressBar;
    private Timer timer;
    private MImage img[] = new MImage[5];
    private MDI mdi;
    private static DBConnectionPool pool;
    
    public LoginDialog() {
        try {
            setTitle("Focus - Maintenance Management System");
            setModal(true);
            jbInit();           
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
        this.setSize(new Dimension(1024, 735));
        this.getContentPane().setLayout(null);
        this.getContentPane().setBackground(Color.GRAY);
        this.setAlwaysOnTop(true);
        //this.txtUsnm.setText("admin");
        //this.txtPswd.setText("123123");
        //this.setModal(true);
        //this.
        imgBackground = new MImage(ImageLibrary.LOGIN_SCREEN);
        imgBackground.setSize(523,324);
        imgBackground.setLayout(null);
        btnCancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnCancel_actionPerformed(e);
                    }
                });
        btnOk.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnOk_actionPerformed(e);
                    }
                });
        this.getContentPane().add(imgBackground);
        imgBackground.setLocation((1024-523)/2,(768-324)/2);
                
        lblUsnm.setBounds(200,130,100,25);
        imgBackground.add(lblUsnm);
        txtUsnm.setBounds(260,132,190,20);
        imgBackground.add(txtUsnm);
        
        lblPswd.setBounds(200,160,100,25);
        imgBackground.add(lblPswd);
        txtPswd.setBounds(260,162,190,20);
        imgBackground.add(txtPswd);
        
        btnOk.setBounds(260,192,90,25);
        imgBackground.add(btnOk);
        this.getRootPane().setDefaultButton(btnOk);
        
        btnCancel.setBounds(360,192,90,25);
        imgBackground.add(btnCancel);
        
        progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setBorderPainted(true);
        progressBar.setBounds(200,265,250,20);
        progressBar.setVisible(false);
        imgBackground.add(progressBar);
        
        int sPos = 210;
        URL path=null;
        for(int i=0;i<5;i++){
            path = ClassLoader.getSystemResource("images/login/"+i+".png");
            img[i] = new MImage(path); 
            img[i].setBounds(new Rectangle(sPos, 225, 35, 35));
            imgBackground.add(img[i], null);
            img[i].setVisible(false);
            sPos += 50;
        }
        //default value 300 - for a slower strart
        timer = new Timer(100,
                          new ActionListener(){
                                int no_of_activities = 6;
                                int count = 0;
                                String msgs[] = new String[]{
                                    "Loading Planner...",
                                    "Loading Log Data...",
                                    "Configuring Network and E-Mail...",
                                    "Configuring Mobile and PDA...",
                                    "Loading Analytical Tools...",
                                    "Loading GUI...",
                                    "Loading Completed."
                                };
                                public void actionPerformed(ActionEvent e){
                                    if(count<7){
                                        progressBar.setString(msgs[count]);
                                    }
                                    progressBar.setValue(progressBar.getValue()+(100/no_of_activities));
                                    if(count<5){
                                        img[count].setVisible(true);
                                    }
                                    repaint();
                                    if(count==7){
                                        progressBar.setValue(100);
                                        timer.stop();
                                        mdi.setVisible(true);
                                        setVisible(false);
                                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                                    }
                                    count++;
                                }
                          }
        );
    }
    
    private void btnOk_actionPerformed(ActionEvent e) {
        e.toString();
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        try{
            User user = new User(pool.getConnection(),txtUsnm.getText());
            if(Utilities.decrypt(user.password).equals(txtPswd.getText())){
                if(user.active.equals("true")){
                    progressBar.setVisible(true);
                    timer.start();
                    mdi = new MDI(pool,user.username);
                }
                else{
                    MessageBar.showErrorDialog(this,"Your Focus login has been blocked by the administrator.","Loing");
                    clearAll();
                }
            }
            else{
                MessageBar.showErrorDialog(this,"Username or Password incorrect","Login");
                clearAll();
            }
        }
        catch(Exception er){
            MessageBar.showErrorDialog(this,"Username or Password incorrect","Login");
            er.printStackTrace();
            clearAll();
        }
    }
    
    private void clearAll(){
        txtUsnm.setText("");
        txtPswd.setText("");
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        txtUsnm.requestFocusInWindow();
    }
    
    public static void main(String[] args) {
        
        try{
            Color c=new Color(231,231,231);//Plastic3DLookAndFeel
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
            UIManager.put("MenuBar.background", c);
            UIManager.put("Menu.foreground", Color.black);
            //UIManager.put("MenuBar.foreground", Color.black);
            UIManager.put("Menu.selectionBackground",c);  
            UIManager.put("Menu.selectionForeground",Color.black);            
            UIManager.put("MenuItem.selectionBackground",new Color(49,106,196));
            UIManager.put("MenuItem.background",Color.white);            
            UIManager.put("ScrollBar.background",c); 
            UIManager.put("ScrollBar.thumb",new Color(197,213,233) ); 
            UIManager.put("ScrollBar.thumbShadow",new Color(118,156,203));            
            UIManager.put("ScrollBar.thumbHighlight",Color.white);          
            UIManager.put("ScrollBar.shadow",c);                       
            UIManager.put("Tree.selectionBackground",new Color(49,106,196));
            UIManager.put("TabbedPane.background",c);
            UIManager.put("TabbedPane.tabAreaBackground",c);
            UIManager.put("TabbedPane.tabBackground",c);
            UIManager.put("TabbedPane.selected",c);          
            UIManager.put("Button.background", c);           
            UIManager.put("ComboBox.background",c);            
            UIManager.put("List.selectionBackground",new Color(49,106,196)); 
            UIManager.put("Table.selectionBackground",new Color(49,106,196)); 
            UIManager.put("CheckBox.background",c);
            UIManager.put("EditorPane.background",c);
            UIManager.put("Label.background",c);            
            UIManager.put("OptionPane.background",c);            
            UIManager.put("Panel.background",c);
            UIManager.put("RadioButton.background",c);
            UIManager.put("Separator.background",c);
            UIManager.put("Spinner.background",c); 
            UIManager.put("ScrollPane.background",c); 
            UIManager.put("SplitPane.background",c);
            UIManager.put("SplitPane.border",BorderFactory.createLineBorder(Color.GRAY));
            UIManager.put("ToolBar.background",c);                                
            UIManager.put("TableHeader.background",c);
            //UIManager.put("TableHeader.cellBorder",BorderFactory.createRaisedBevelBorder());
            UIManager.put("TextArea.background",Color.white);            
            UIManager.put("Viewport.background",c); 
            
            Enumeration keys = UIManager.getDefaults().keys();
            Font f=new Font("Tahoma",Font.PLAIN,11);
            while (keys.hasMoreElements()) {
                Object key = keys.nextElement();
                Object value = UIManager.get (key);
                if (value instanceof FontUIResource)
                    UIManager.put (key, f);
                
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        /**System starts*/
        LoginDialog ld = new LoginDialog();
        pool = new DBConnectionPool(ld);
        ld.setVisible(true);
    }

    private void btnCancel_actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
