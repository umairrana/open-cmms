package com.matrix.focus.workorder;

import com.matrix.focus.util.ImageLibrary;


import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;


public class JobDialog extends JDialog {
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JButton btnClose = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
    private JobsTable tblJobs = new JobsTable();
    private String jobInfo[] = new String[2];

    public JobDialog(JDialog parent) {
        this(parent, "", false);
    }

    public JobDialog(JDialog parent, String title, boolean modal) {
        super(parent, title, modal);
        try {
            jbInit();
            tblJobs.addMouseListener(
                new MouseAdapter(){
                    public void mouseClicked(MouseEvent me){
                        int row = tblJobs.getSelectedRow();
                        if(me.getButton()==1 && me.getClickCount()==2 && row!=-1){
                            jobInfo[0] = tblJobs.getValueAt(row,0).toString();
                            jobInfo[1] = tblJobs.getValueAt(row,1).toString();
                            setVisible(false);
                        }
                    }
                }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void populate(String[][] data){
        jobInfo[0] = "";
        jobInfo[1] = "";
        tblJobs.populate(data);
    }
    
    public String[] getJob(){
        return jobInfo;
    }
    
    private void jbInit() throws Exception {
        this.setSize(new Dimension(228, 295));
        this.setResizable(false);
        this.getContentPane().setLayout( null );
        this.setTitle("Maintenance Job");
        this.setModal(true);
        jScrollPane1.setBounds(new Rectangle(5, 5, 210, 220));
        btnClose.setText("Close");
        btnClose.setBounds(new Rectangle(135, 230, 80, 25));
        btnClose.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnClose_actionPerformed(e);
                    }
                });
        this.getContentPane().add(btnClose, null);
        jScrollPane1.getViewport().add(tblJobs, null);
        this.getContentPane().add(jScrollPane1, null);
    }

    private void btnClose_actionPerformed(ActionEvent e) {
        setVisible(false);
    }

    public class JobsTable extends JTable {
        private JobsTableModel myModel = null ;

        public JobsTable() {
            myModel = new JobsTableModel();
            this.setModel(myModel); 
        
            this.setSelectionMode(0);
            this.getTableHeader().setReorderingAllowed(false);
            this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            
            this.getColumnModel().getColumn(0).setPreferredWidth(110);
            this.getColumnModel().getColumn(1).setPreferredWidth(100);
        }
        
        public void populate(String[][] data){
            myModel.deleteAll();
            for(int i=0;i<data.length;i++){
                addRow();
                setValueAt(data[i][0],i,0);
                setValueAt(data[i][1],i,1);
            }
        }
        
        public void addRow(){
            myModel.addRow();
            this.tableChanged(new TableModelEvent(myModel)); 
        }

        public void deleteAll(){
            myModel.deleteAll();
            this.tableChanged(new TableModelEvent(myModel));        
        }

        private class JobsTableModel extends AbstractTableModel{
            private int rowCount = 0;
            private String [] colNames = {"Machine ID",//0
                                          "Job"
                                          };
            private Object [][] valueArray = null;
            private Object [][] tempArray = null;


            public JobsTableModel() {
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

            public void deleteAll(){
                valueArray = new Object[0][colNames.length];
            }
        }
    }
    
}
