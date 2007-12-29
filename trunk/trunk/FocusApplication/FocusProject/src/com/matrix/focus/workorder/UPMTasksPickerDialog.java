package com.matrix.focus.workorder;

import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.ImageLibrary;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Savepoint;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

public class UPMTasksPickerDialog extends JDialog{

  private JScrollPane jScrollPane1 = new JScrollPane();
  private JButton btnCancel = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
  private JButton btnRemove = new JButton(new ImageIcon(ImageLibrary.BUTTON_REMOVE));
  private JButton btnAdd = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));
  private TasksTable tblTasks = new TasksTable();
  private Connection connection;
  private MessageBar messageBar;
  private JFrame frame;
  private UPMTask upmTask;
  
    private JButton btnEdit = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));

  public UPMTasksPickerDialog(JFrame frame,MessageBar msgBar,Connection con){
    this(frame, "", true);
    this.connection = con;
    this.messageBar = msgBar;
    this.frame = frame;
  }

  public UPMTasksPickerDialog(Frame parent, String title, boolean modal){
    super(parent, title, modal);
    try{
      jbInit();
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
              upmTask = null;
            }
          });
      tblTasks.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent me){
                if(me.getButton()==1 && me.getClickCount()==2){
                    int row = tblTasks.getSelectedRow();
                    upmTask = new UPMTask(null);
                    upmTask.upm_id = tblTasks.getValueAt(row,0).toString();
                    upmTask.description = tblTasks.getValueAt(row,1).toString();
                    upmTask.category = tblTasks.getValueAt(row,2).toString();
                    setVisible(false);
                }
            }
      });
    }
    catch (Exception e){
      e.printStackTrace();
    }
  }
  
    public UPMTask getSelectedTask(){
      return upmTask;
    }
    
  public boolean populate(){
      String sql = "SELECT * FROM upmaintenance_master";
      try{
          /**Remove All OLD*/
          tblTasks.deleteAll();
          Statement stmt = connection.createStatement();
          ResultSet rec = stmt.executeQuery(sql);
          
          /**The array*/
          rec.last();
          int rows = rec.getRow();
          if(rows==0) return false;
          rec.beforeFirst();
          
          /**Populate*/
          while(rec.next()){
             String id = rec.getString("UPM_ID");
             String desc = rec.getString("Description");
             String category = rec.getString("UPM_Category");
             tblTasks.addItem(id,desc,category);
          }
          return true;
      }
      catch(Exception er){
         er.printStackTrace();
         return false;
      }
  }
  private void jbInit()throws Exception{
    this.setSize(new Dimension(543, 410));
    this.setResizable(false);
    this.getContentPane().setLayout( null );
    this.setTitle("Unplanned Maintenance Tasks");
    jScrollPane1.setBounds(new Rectangle(5, 10, 525, 325));
    btnCancel.setText("Cancel");
    btnCancel.setBounds(new Rectangle(445, 345, 85, 25));
    btnCancel.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            btnCancel_actionPerformed(e);
          }
        });
    btnRemove.setText("Remove");
    btnRemove.setBounds(new Rectangle(355, 345, 85, 25));
    btnRemove.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            btnRemove_actionPerformed(e);
          }
        });
    btnAdd.setText("Add");
    btnAdd.setBounds(new Rectangle(175, 345, 85, 25));
    btnAdd.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            btnAdd_actionPerformed(e);
          }
        });
        btnEdit.setText("Edit");
        btnEdit.setBounds(new Rectangle(265, 345, 85, 25));
        btnEdit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnEdit_actionPerformed(e);
                    }
                });
        this.getContentPane().add(btnEdit, null);
        this.getContentPane().add(btnAdd, null);
        this.getContentPane().add(btnRemove, null);
        this.getContentPane().add(btnCancel, null);
        jScrollPane1.getViewport().add(tblTasks, null);
    this.getContentPane().add(jScrollPane1, null);
  }

  private void btnAdd_actionPerformed(ActionEvent e){
    UPMTaskDialog dlgUPMTask = new UPMTaskDialog(frame,messageBar,connection,false);
    dlgUPMTask.setLocationRelativeTo(frame);
    dlgUPMTask.setVisible(true);
    try{
      UPMTask upmTask = dlgUPMTask.getNewTask();
      if(upmTask!=null){
        tblTasks.addItem(upmTask.upm_id,upmTask.description,upmTask.category);
      }
    }
    catch(Exception er){
      er.printStackTrace();
    }
  }
  
    private void btnEdit_actionPerformed(ActionEvent e){
        int row = tblTasks.getSelectedRow();
        
        if(row!=-1){
            UPMTaskDialog dlgUPMTask = new UPMTaskDialog(frame,messageBar,connection,true);
            dlgUPMTask.setLocationRelativeTo(frame);
            
            UPMTask upmTask = new UPMTask(connection);
            upmTask.upm_id = tblTasks.getValueAt(row,0).toString();
            upmTask.description = tblTasks.getValueAt(row,1).toString();
            upmTask.category = tblTasks.getValueAt(row,2).toString();
            
            dlgUPMTask.setTask(upmTask);
            
            dlgUPMTask.setVisible(true);
            
            if(upmTask!=null){
              tblTasks.deleteRow(row);
              upmTask = dlgUPMTask.getNewTask();
              tblTasks.addItem(upmTask.upm_id,upmTask.description,upmTask.category);
            }
        }
        else{
            messageBar.setMessage("Please select a task to edit.","ERROR");
        }
    }

  private void btnRemove_actionPerformed(ActionEvent e){
    int row = tblTasks.getSelectedRow();
    if(row!=-1){
        if(MessageBar.showConfirmDialog(this,"Are you sure you want to delete \nthe selected task from the system?","Delete Task")==MessageBar.YES_OPTION){
            if(!hasBeenUsed(tblTasks.getValueAt(row,0).toString())){
                UPMTask upmTask = new UPMTask(connection);
                upmTask.upm_id = tblTasks.getValueAt(row,0).toString();
                try{
                    Savepoint sp = connection.setSavepoint();
                    try{
                        upmTask.delete();
                        tblTasks.deleteRow(row);
                    }
                    catch(Exception er){
                        connection.rollback(sp);
                        messageBar.setMessage(er.getMessage(),"ERROR");
                    }
                }
                catch(Exception er){
                    er.printStackTrace();
                }
            }
            else{
                messageBar.setMessage("You can not delete this task,\nbecause this task has been used before.","ERROR");
            }
        }
    }
    else{
      messageBar.setMessage("Select a task first","ERROR");
    }
  
  }

  private void btnCancel_actionPerformed(ActionEvent e){
    this.setVisible(false);
  }

    private boolean hasBeenUsed(String ump_id){
        String sql = "SELECT UPM_ID FROM upmaintenance_log_tasks WHERE UPM_ID= '"+ ump_id +"'";
        try{
            ResultSet rec = connection.createStatement().executeQuery(sql);
            rec.last();
            return (rec.getRow()>0?true:false);
        }
        catch (Exception ex)  {
            ex.printStackTrace();
            return false;
        }
    }

    /****************************************************************************/
   private class TasksTable extends JTable {
       private TasksTableModel myModel = null;
            
       public TasksTable(){
           myModel = new TasksTableModel();
           this.setModel(myModel);
           this.setSelectionMode(0);          
           
           this.getColumnModel().getColumn(0).setPreferredWidth(60);
           this.getColumnModel().getColumn(1).setPreferredWidth(400);
           this.getColumnModel().getColumn(2).setPreferredWidth(80);
           
           this.setSelectionMode(0);
           this.getTableHeader().setReorderingAllowed(false);
           this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
           this.setAutoCreateRowSorter(true);
       }
        
       public void addItem(String id, String desc, String category){
         addRow();
         int row = this.getRowCount()-1;
         this.setValueAt(id,row,0);
         this.setValueAt(desc,row,1);
         this.setValueAt(category,row,2);
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
            
       private class TasksTableModel extends AbstractTableModel{
           private int rowCount = 0;
           private String [] colNames = {
                            "Task ID",
                            "Description",
                            "Category"     
                            };
           private Object [][] valueArray = null;
           private Object [][] tempArray = null;
       
           public TasksTableModel() {
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
               if(valueArray[r][c]==null) return "";
               else return valueArray[r][c];
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
   }
  /****************************************************************************/
  
  
}
