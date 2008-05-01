package com.matrix.focus.util.table;

import java.util.Vector;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

public class Table<T extends TableItem> extends JTable{
    
            private TableModel model;
            private String columnNames[]; 
             
            
            public Table(String columnNames[]){
                    
                    this.columnNames = columnNames;
                    model = new TableModel();
                    this.setModel(model);
                    this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                    this.getTableHeader().setReorderingAllowed(false);
                    this.setSelectionMode(0);
                    
                    /*this.getColumnModel().getColumn(0).setPreferredWidth(100);
                    this.getColumnModel().getColumn(1).setPreferredWidth(150);
                    this.getColumnModel().getColumn(2).setPreferredWidth(300);
                    this.getColumnModel().getColumn(3).setPreferredWidth(80);
                    this.getColumnModel().getColumn(4).setPreferredWidth(130);
                    this.getColumnModel().getColumn(5).setPreferredWidth(55);
                    this.getColumnModel().getColumn(6).setPreferredWidth(100);*/
                  
            }
                    
            public void addRow(T item){
                    model.addRow(item);
                    this.tableChanged(new TableModelEvent(model));
            }
                    
            public void removeAll(){
                    model.removeAll();
                    this.tableChanged(new TableModelEvent(model));
            }
                    
            private class TableModel extends AbstractTableModel{
            
                
                
                private Vector<T> items = new Vector<T>();
            
                public TableModel(){
                   
                }
                    
                public Class getColumnClass(int c){
                    return "".getClass();   
                }
                
                public String getColumnName(int c){ 
                    return columnNames[c];
                }
            
                public int getColumnCount(){  
                    return columnNames.length;
                }    
                
                public int getRowCount(){
                        return items.size();  
                }
            
                public Object getValueAt(int r, int c){
                        T item = items.get(r);
                        return item.getValueAt(c);
                }
            
                public void setValueAt(Object value,int row,int col){
                        //nothing
                }
            
                public boolean isCellEditable(int r,int c){
                    return false;
                }
                    
                public void addRow(T item){
                        items.add(item);
                }
                
                public void removeAll(){
                        items.removeAllElements(); 
                }
            }
            
               
        } 