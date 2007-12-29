package com.matrix.focus.util;

import java.awt.Color;
import java.awt.Dimension;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

public class MList extends JTable{
    private MListModel mListModel;
    
    public MList(String name){
        mListModel = new MListModel(name);
        this.setModel(mListModel);
        this.getTableHeader().setReorderingAllowed(false);
        this.setGridColor(Color.white);
        this.getTableHeader().setVisible(false);
        this.getTableHeader().setPreferredSize(new Dimension(0,0));
        /**Single row selection*/
        this.setSelectionMode(0);
    }
    
    public void addItem(String text){
        mListModel.addRow(text);
        this.tableChanged(new TableModelEvent(mListModel)); 
    }
    
    public Object[] getSelectedValues(){
        int rows = mListModel.getRowCount();
        Object[] selectedItems = new Object[rows];
        for(int i=0;i<rows;i++){
            selectedItems[i] = mListModel.getValueAt(i,0);
        }
        return selectedItems;
    }
    
    public void populate(Connection con, String sql){
        try{
            Statement stmt = con.createStatement();
            ResultSet rec = stmt.executeQuery(sql);
            
            /**Populate*/
            deleteAll();                
            while(rec.next()){
                addItem(rec.getString(1));
            }
            rec.close();
            stmt.close();
            rec = null;
            stmt = null;
        }
        catch(Exception er){
            er.printStackTrace();
        }
    }
    
    public void setColumnName(int c, String name){
        mListModel.setColumnName(c,name);
        this.tableChanged(new TableModelEvent(mListModel));   
    }
    
    public void deleteRow(){
        mListModel.deleteRow();
        this.tableChanged(new TableModelEvent(mListModel));        
    }
    
    public void deleteRow(int i){
        if(i>-1){
            mListModel.deleteRow(i);
        }
        this.tableChanged(new TableModelEvent(mListModel));        
    }
    
    public void deleteAll(){
        mListModel.deleteAll();
        this.tableChanged(new TableModelEvent(mListModel));        
    }
    
    private class MListModel extends AbstractTableModel{
        private int rowCount = 0;
        private String [] colNames = new String[1];
        private Object [][] valueArray = null;
        private Object [][] tempArray = null;

        public MListModel(String name){
            valueArray = new Object[rowCount][colNames.length];
            colNames[0] = name;
        }
        
        public String getColumnName(int c){ 
            return colNames[c];  
        }
        
        public void setColumnName(int c, String name){ 
            colNames[c] = name;  
        }
        public int getColumnCount(){  
            return colNames.length;
        }
        
        public int getRowCount(){  
            return valueArray.length;
        }
        
        public Object getValueAt(int r, int c){  
            return valueArray[r][c];
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
        
        public void addRow(String text){
            addRow();
            tempArray[tempArray.length-1][0] = text;
        }
        
        public void deleteRow(){
            tempArray = new Object[this.getRowCount()-1][this.getColumnCount()];
            for(int y=0 ; y<tempArray.length; y++){
                for(int x=0; x<tempArray[0].length; x++){
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
            valueArray = new Object[0][1];
        }
    }
}
