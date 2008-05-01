/*******************************************************************************
 * Class   : DataAssistantDialog.java
 * Details : The Data Assistant Dialog for easy pick up of master data.
 * Author  : sara (Civilized by Kantha)
 * Date Created  : 2006.06.10
 * Date Modified : 2006.06.11
 * ****************************************************************************/

/** The Example 
    DataAssistantDialog d = new DataAssistantDialog(this,"Data Assistant","SELECT Model_ID,Description  FROM models",con);
    d.setLocationRelativeTo(btnSend);
    d.setVisible(true); 
    String rtnVal = d.getValue();
    txtField.setText( (rtnVal.equals("") ? txtField.getText() : rtnVal));
*/

package com.matrix.focus.util;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;

import java.awt.event.KeyEvent;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;

/** The Example
    DataAssistantDialog d = new DataAssistantDialog(this,"Data Assistant","SELECT Model_ID,Description  FROM models",con);
    d.setLocationRelativeTo(btnSend);
    d.setVisible(true);
    String rtnVal = d.getValue();
    txtField.setText( (rtnVal.equals("") ? txtField.getText() : rtnVal));
*/
public class DataAssistantDialog extends JDialog{
  
  private JPanel pnlBottom = new JPanel();
  private String query;
  private Connection con;
  private JTable tblSelectingTable = new JTable();
  private JScrollPane jsp = new JScrollPane();
  private String ID = "";
  private String Description = "";
  private String Third;
  private String Fourth;
  private String Fifth;
  private JButton btnSearch = new JButton(new ImageIcon(ImageLibrary.BUTTON_SEARCH));
  private JTextField txtSearch = new JTextField();
  private JLabel jLabel1 = new JLabel();

  /** Constructor taking the query string and the connection*/
  public DataAssistantDialog(JFrame parent, String title, String queryString, Connection con){
    super(parent, title, true);
    query = queryString;
    this.con = con;
    try{
      jbInit();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }
  
    public DataAssistantDialog(JDialog parent, String title, String queryString, Connection con){
      super(parent, title, true);
      query = queryString;
      this.con = con;
      try{
        jbInit();
      }
      catch(Exception e){
        e.printStackTrace();
      }
    }
  
    public void setFirstColumnWidth(int w){
        resizeColumn(0,w);
    }
    
    public void setSecondColumnWidth(int w){
         resizeColumn(1,w);
    }
    
    public void setThirdColumnWidth(int w){
        resizeColumn(2,w);
    }
    
    public void setFourthColumnWidth(int w){
        resizeColumn(3,w);
    }
    
    public void setFifthColumnWidth(int w){
        resizeColumn(4,w);
    }
    
    private int getRow(String text){
        int rows = tblSelectingTable.getRowCount();
        int cols = tblSelectingTable.getColumnCount();
        Object cell = null;
        for(int y=0;y<rows;y++){
            for(int x=0;x<cols;x++){
                cell = tblSelectingTable.getValueAt(y,x);
                String value = (cell==null?"":cell.toString().toUpperCase());
                if(value.indexOf(text.toUpperCase())!=-1){
                    return y;
                }
            }
        }
        return -1;
    }
    
  private void resizeColumn(int col, int size){
      tblSelectingTable.getColumnModel().getColumn(col).setPreferredWidth(size);
      if(size==0){
        tblSelectingTable.getColumnModel().getColumn(col).setMaxWidth(0);
        tblSelectingTable.getColumnModel().getColumn(col).setMinWidth(0);
        tblSelectingTable.getColumnModel().getColumn(col).setPreferredWidth(0);
      }
  }
    
  public void grow(int width, int x, int sp_width){
    this.setSize(width,460);//330,475
    jsp.setSize(sp_width,390);//310,390
    pnlBottom.setSize(sp_width,430); //310, 430
  }
    
    
  private void jbInit() throws Exception{
    this.setSize(new Dimension(330, 458));
    this.setResizable(false);
    this.getContentPane().setLayout(null);
    this.addWindowListener(new WindowAdapter()
      {
        public void windowClosing(WindowEvent e){
          ID = "";
        }
      });
    
    pnlBottom.setBounds(new Rectangle(5, 30, 310, 395));
    pnlBottom.setLayout(null);

        btnSearch.setBounds(new Rectangle(275, 5, 30, 20));
        btnSearch.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnSearch_actionPerformed(e);
                    }
                });
        txtSearch.setBounds(new Rectangle(65, 5, 205, 20));
        txtSearch.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnSearch_actionPerformed(e);
                    }
                });
        jLabel1.setText("Search");
        jLabel1.setBounds(new Rectangle(15, 5, 55, 20));
        prepareTable();
        this.getContentPane().add(jLabel1, null);
        this.getContentPane().add(txtSearch, null);
        this.getContentPane().add(btnSearch, null);
        this.getContentPane().add(pnlBottom, null);
  }
  
  /** Preparing the table acc. to the sent query string */
  public void prepareTable(){
    try{
      ResultSet rs = con.createStatement().executeQuery(query);                                
      DataTable pt = new DataTable(rs);   
       
      tblSelectingTable = pt.getDataTable();
      tblSelectingTable.getColumnModel().getColumn(0).setPreferredWidth(80);
      try{
        tblSelectingTable.getColumnModel().getColumn(1).setPreferredWidth(230);
      }
      catch(Exception te){
      }
      tblSelectingTable.getTableHeader().setReorderingAllowed(false);
      tblSelectingTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      tblSelectingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      tblSelectingTable.setAutoCreateRowSorter(true);
      
      /** putting the selected value into the variable 
       * which is later taken by the using programme */
      tblSelectingTable.addMouseListener(new MouseAdapter()
      {
        public void mouseClicked(MouseEvent e)
        {
          if(e.getClickCount()==2){
              selectAndExit();
          }
        }
      });
      
        tblSelectingTable.addKeyListener(
            new KeyAdapter(){
                    public void keyPressed(KeyEvent e){
                            if(e.getKeyCode()==KeyEvent.VK_ENTER && tblSelectingTable.getSelectedRow()!=-1){
                                    selectAndExit();
                            }
                    }
            }
        );
      
      jsp.getViewport().add(tblSelectingTable);
      jsp.setBounds(new Rectangle(0, 0, 310, 390));
      pnlBottom.add(jsp); 
    }catch(Exception e){
      e.printStackTrace();
    }
  }
  
  private void selectAndExit(){
      ID = tblSelectingTable.getValueAt(tblSelectingTable.getSelectedRow(),0).toString();
      try{
        Description = tblSelectingTable.getValueAt(tblSelectingTable.getSelectedRow(),1).toString();
        Third = tblSelectingTable.getValueAt(tblSelectingTable.getSelectedRow(),2).toString();
        Fourth = tblSelectingTable.getValueAt(tblSelectingTable.getSelectedRow(),3).toString();
        Fifth = tblSelectingTable.getValueAt(tblSelectingTable.getSelectedRow(),4).toString();
      }
      catch(Exception er){
      }
      setVisible(false);
  }
  
  /** to get the selected value */
  public String getValue(){
    return ID;
  }
  
  /** to get the selected value */
  public String getDescription(){
    return Description;
  }
  
    /** to get the selected value */
    public String getThirdValue(){
      return Third;
    }
    
    /** to get the selected value */
    public String getFourthValue(){
      return Fourth;
    }
    
    /** to get the selected value */
    public String getFifthValue(){
      return Fifth;
    }

    private void btnSearch_actionPerformed(ActionEvent e) {
        int row = getRow(txtSearch.getText());
        if(row!=-1){
            ListSelectionModel selectionModel = tblSelectingTable.getSelectionModel();
            selectionModel.setSelectionInterval(row, row);
            
            JViewport vp = jsp.getViewport();
            int rowY = row*tblSelectingTable.getRowHeight();
            int curY = (int)vp.getViewPosition().getY();
            int vpH = vp.getHeight()-20;
            
            int gap = rowY - curY;
           
            if(gap>vpH){
                vp.setViewPosition(new Point(0,gap+curY));
            }
            else if(gap<0){
                vp.setViewPosition(new Point(0,rowY));
            }
            else{
                //row is visible
            }
     
            tblSelectingTable.grabFocus();
            vp.repaint();
        }
        else{
            ListSelectionModel selectionModel = tblSelectingTable.getSelectionModel();
            selectionModel.clearSelection();
        }
    }

    class DataTable{ 
    private ResultSet resultSet;
    private JTable table;
    private int r,c;
   
    public DataTable(ResultSet rs){
    this.resultSet=rs;
      
    try{
        ResultSetMetaData meta = resultSet.getMetaData();
        r = resultSet.getRow();
        c = meta.getColumnCount();
        
        DataAssistantTableModel tm = new DataAssistantTableModel(resultSet);
        table = new JTable(tm);
            
      }catch(Exception ex){
        ex.printStackTrace();
      }
    }
   
   public JTable getDataTable(){
      table.repaint();
      return table;
    }
  }
  
    class DataAssistantTableModel extends AbstractTableModel{  
     /**
        Constructs the table model.
        @param aResultSet the result set to display.
     */
     private ResultSet rs;
     private ResultSetMetaData rsmd;
     
     public DataAssistantTableModel(ResultSet aResultSet){  
        rs = aResultSet;
        try{  
           rsmd = rs.getMetaData();
        }
        catch(Exception e){  
           e.printStackTrace();
        }
     }
  
     public String getColumnName(int c){  
        try{  
           return rsmd.getColumnName(c + 1);
        }
        catch(SQLException e){  
           e.printStackTrace();
           return "";
        }
     }
  
     public int getColumnCount(){  
        try{  
           return rsmd.getColumnCount();
        }
        catch(SQLException e){  
           e.printStackTrace();
           return 0;
        }
     }

     public Object getValueAt(int r, int c){  
        try{  
           rs.absolute(r + 1);
           return rs.getObject(c + 1);
        }
        catch(SQLException e){  
           e.printStackTrace();
           return null;
        }
     }
  
     public int getRowCount(){  
        try{  
           rs.last();
           return rs.getRow();
        }
        catch(SQLException e){  
           e.printStackTrace();
           return 0;
        }
     }
     
     public void setValueAt(Object value,int row,int col){
       try{                
          rs.absolute(row + 1);
          rs.updateString(col + 1, value.toString());
          rs.updateRow();
            
        }catch(Exception ex){
         ex.printStackTrace();
       }
     }
     
     public boolean isCellEditable(int r,int c){  
        return false;
     }
  }
}