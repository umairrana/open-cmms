package com.matrix.focus.workorder;

import com.matrix.components.MDataCombo;
import com.matrix.components.MTextbox;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.ImageLibrary;
import com.matrix.focus.util.Validator;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.Savepoint;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class UPMTaskDialog extends JDialog{
    private JPanel jPanel1 = new JPanel();
    private MTextbox mtxtID = new MTextbox();
    private JButton btnOK = new JButton(new ImageIcon(ImageLibrary.BUTTON_OK));
    private MTextbox mtxtDesc = new MTextbox();
    private MDataCombo mcmbCategory = new MDataCombo();
    private JButton btnNewCat = new JButton(new ImageIcon(ImageLibrary.BUTTON_NEW));
    private Connection connection;
    private MessageBar messageBar;
    private UPMTask upmTask;
    private boolean isEditing;
  
    public UPMTaskDialog(JFrame frame, MessageBar msgBar,Connection con, boolean isEditing){
        this(frame, "", true);
        connection = con;
        mtxtID.setInputText((isEditing?"":"New"));
        messageBar = msgBar;
        mcmbCategory.populate(this.connection,"(SELECT Maintenance_Category FROM preventive_maintenance_category)UNION(SELECT DISTINCT UPM_Category FROM upmaintenance_master)");
        this.isEditing = isEditing;
    }
    
    public void setTask(UPMTask task){
        this.upmTask = task;
        mtxtID.setInputText(this.upmTask.upm_id);
        mtxtDesc.setInputText(this.upmTask.description);
        mcmbCategory.setSelectedItem(this.upmTask.category);
    }


  public UPMTaskDialog(Frame parent, String title, boolean modal){
    super(parent, title, modal);
    try{
      jbInit();
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
              upmTask = null;
            }
          });
    }
    catch (Exception e){
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception{
    this.setSize(new Dimension(534, 191));
    this.setResizable(false);
    this.getContentPane().setLayout( null );
    this.setTitle("Unplanned Maintenance Task ");
    jPanel1.setBounds(new Rectangle(5, 10, 515, 110));
    jPanel1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    jPanel1.setLayout(null);
    mtxtID.setBounds(new Rectangle(15, 15, 195, 20));
    mtxtID.setCaption("Task ID");
    mtxtID.setTxtWidth(100);
        mtxtID.setEditable(false);
        btnOK.setText("OK");
    btnOK.setBounds(new Rectangle(405, 125, 115, 25));
    btnOK.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            jButton1_actionPerformed(e);
          }
        });
    mtxtDesc.setBounds(new Rectangle(15, 45, 445, 20));
    mtxtDesc.setCaption("Description");
    mtxtDesc.setTxtWidth(350);
    mcmbCategory.setBounds(new Rectangle(15, 75, 315, 20));
    mcmbCategory.setCaption("Category");
    mcmbCategory.setLblWidth(95);
        btnNewCat.setBounds(new Rectangle(310, 75, 30, 20));
    btnNewCat.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            jButton2_actionPerformed(e);
          }
        });
        jPanel1.add(btnNewCat, null);
        jPanel1.add(mcmbCategory, null);
    jPanel1.add(mtxtDesc, null);
    jPanel1.add(mtxtID, null);
        this.getContentPane().add(btnOK, null);
        this.getContentPane().add(jPanel1, null);
  }
  
  public UPMTask getNewTask(){
    return upmTask;
  }

    private void jButton1_actionPerformed(ActionEvent e) {
        upmTask = new UPMTask(connection);
        upmTask.upm_id = mtxtID.getInputText();
        upmTask.description = mtxtDesc.getInputText();
        upmTask.category = mcmbCategory.getSelectedItem().toString();
        if(validateTask()){
            try{
                Savepoint spTask = connection.setSavepoint();
                try{
                    if(isEditing){
                        upmTask.update();;
                        messageBar.setMessage("Task updated.","OK");
                        this.setVisible(false);
                    }
                    else{
                        upmTask.save();
                        messageBar.setMessage("New task saved.","OK");
                        this.setVisible(false);
                    }
                    
                }
                catch(Exception er){
                    er.printStackTrace();
                    try{
                        connection.rollback(spTask);
                    }
                    catch(SQLException err) {}
                    messageBar.setMessage(er.getMessage(),"ERROR");
                }
            }
            catch(Exception er){
                er.printStackTrace();
            }
        }
    }

  private void jButton2_actionPerformed(ActionEvent e){
    try{
        String s = MessageBar.showInputDialog(this,"Category","Add New Category");
        if (!s.equals("")){
          mcmbCategory.addItem(s);
          mcmbCategory.setSelectedItem(s);  
        }
    }catch(Exception er){
    }
  }

  private boolean validateTask(){
    if(Validator.isEmpty(mtxtID.getInputText())){ 
        messageBar.setMessage("Task ID field is empty","ERROR");
        mtxtID.setFocus(1);
        return false;
    }
    else if(Validator.isEmpty(mtxtDesc.getInputText())){
        messageBar.setMessage("Description field is empty","ERROR");
        mtxtDesc.setFocus(1);
        return false;
    }
    else if(mcmbCategory.getComboBox().getSelectedIndex()==0){
        messageBar.setMessage("Select a category.","ERROR");
        return false;
    }
    else{
      return true;
    }
  }
}
