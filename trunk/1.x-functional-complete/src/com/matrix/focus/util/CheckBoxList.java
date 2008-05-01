package com.matrix.focus.util;

import java.awt.Color;
import java.sql.ResultSet;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

public class CheckBoxList extends JTable{

    private CheckBoxListModel mListModel;
    private boolean multiSelectionPosible = false;
    
    public CheckBoxList(){
        mListModel = new CheckBoxListModel();
        this.setModel(mListModel);
        this.getColumnModel().getColumn(0).setMaxWidth(10);
        this.getTableHeader().setReorderingAllowed(false);
        this.setGridColor(Color.white);
        setMultiSelectPosible(true);
    }
    
    public void setColumnHead(String text){
        this.getColumnModel().getColumn(1).setHeaderValue(text);
        this.getTableHeader().updateUI();
    }
    
    public void setMultiSelectPosible(boolean flag){
        this.multiSelectionPosible = flag;
        setAllTo(false);
        this.setSelectionMode((flag?1:0));
    }
    
    public void addItem(String value){
        mListModel.addItem(value);
        this.tableChanged(new TableModelEvent(mListModel)); 
    }
    
    public void addItem(boolean selected,String value){
        mListModel.addItem(selected,value);
        this.tableChanged(new TableModelEvent(mListModel)); 
    }
    
    public String[] getSelectedValues(){
        int rows = mListModel.getRowCount();
        int selected_items = 0;
        for(int i=0;i<rows;i++){
            if(mListModel.getValueAt(i,0)==Boolean.TRUE){
                selected_items++;
            }
        }
        String[] selectedItems = new String[selected_items];
        for(int i=0,j=0;i<rows;i++){
            if(mListModel.getValueAt(i,0)==Boolean.TRUE){
                selectedItems[j] = mListModel.getValueAt(i,1).toString();
                j++;
            }
        }
        return selectedItems;
    }
    
    public boolean isMultiSelected() throws Exception{
        String ar[] = getSelectedValues();
        if(ar.length==-1){
            throw new Exception("Nothing selected.");
        }
        return (getSelectedValues().length==1?false:true);
    }
    
    public void setSelected(int[] items){
        for(int i=0;i<items.length;i++){
            this.setValueAt(true,items[i],0);
        }
    }
    
    public void setAllTo(boolean flag){
        mListModel.setAllTo(flag);
    }
    
    public boolean areAllSelected(){
        int rows = getRowCount();
        if(rows>0){
            for(int i=0;i<rows;i++){
                if(getValueAt(i,0).equals("true")){
                    continue;
                }
                else{
                    break;
                }
            }
            return true;
        }
        else{
            return false;
        }
    } 
    
    public void populate(ResultSet rec){
        try{
            deleteAll();
            while(rec.next()){
                addItem(rec.getString(1));
            }
            rec.close();
            rec = null;
        }
        catch(Exception er){
            er.printStackTrace();
        }
    }
    
    public void deleteAll(){
        mListModel.deleteAll();
        this.tableChanged(new TableModelEvent(mListModel));        
    }
    
    private class CheckBoxListModel extends AbstractTableModel{
        private String [] colNames = new String[2];
        private Object [][] valueArray = null;
        private Object [][] tempArray = null;

        public CheckBoxListModel(){
            valueArray = new Object[0][colNames.length];
            colNames[0] = "*";
            colNames[1]= "Items";
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
            return valueArray[r][c];
        }
        
        public void setValueAt(Object value,int row,int col){
            if(!multiSelectionPosible){
                setAllTo(false);
            }
            valueArray[row][col] = value;
            repaint();
        }
        
        public void setAllTo(boolean flag){
            int rows = getRowCount();
            for(int i=0;i<rows;i++){
                valueArray[i][0] = flag;
            }
            repaint();
        }
        
        public boolean isCellEditable(int r,int c){
            return (c==0);
        }
        
        public Class getColumnClass(int c){
            return this.getValueAt(0,c).getClass();  
        }
        
        public void addItem(boolean selected,String value){
            tempArray = new Object[this.getRowCount()+1][this.getColumnCount()];
        
            for(int y=0 ; y<valueArray.length; y++){
                for(int x=0; x<valueArray[0].length; x++){
                    tempArray[y][x] =  valueArray[y][x];
                }
            }
            tempArray[tempArray.length-1][0] = selected;
            tempArray[tempArray.length-1][1] = value;
            valueArray = tempArray;
        }
        
        public void addItem(String value){
            addItem(Boolean.FALSE,value);
        }
        
        public void deleteAll(){
            valueArray = new Object[0][2];
        }
    }
}