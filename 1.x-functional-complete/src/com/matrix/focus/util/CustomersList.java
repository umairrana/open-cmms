package com.matrix.focus.util;

import java.awt.Dimension;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

public class CustomersList extends JTable {
	private CustomersListTableModel myModel = null ;

	public CustomersList() {
		myModel = new CustomersListTableModel();
		this.setModel(myModel);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.getTableHeader().setReorderingAllowed(false);
                
                this.getTableHeader().setVisible(false);
                this.getTableHeader().setPreferredSize(new Dimension(500,3));
		
		this.getColumnModel().getColumn(0).setMinWidth(0);
                this.getColumnModel().getColumn(0).setPreferredWidth(0);

		this.getColumnModel().getColumn(1).setPreferredWidth(250);
		
		/**Single row selection*/
		this.setSelectionMode(0);
	}

	public void populate(Connection connection){
		try{
			Statement stmt = connection.createStatement();
			ResultSet rec = stmt.executeQuery("SELECT division_id, name FROM division WHERE comp_id = 'Matrix' AND dept_id = 'Customers' ORDER BY name");
			this.deleteAll();
			while(rec.next()){
				addRow();
				int row = rec.getRow() - 1;
				
				String id = rec.getString("division_id");
				String name = rec.getString("name");
				
				setValueAt(id,row,0);
				setValueAt(name,row,1);
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

	private class CustomersListTableModel extends AbstractTableModel{
		private int rowCount = 0;
		private String [] colNames = {"ID","Name"};
		private Object [][] valueArray = null;
		private Object [][] tempArray = null;

		public CustomersListTableModel(){
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