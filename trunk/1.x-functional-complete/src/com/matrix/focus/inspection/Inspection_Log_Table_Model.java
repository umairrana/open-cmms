package com.matrix.focus.inspection;

import java.sql.ResultSet;
import java.sql.SQLException;

class Inspection_Log_Table_Model extends Inspection_Log_Table_Abs_Model
{
    private String colNames[];
    private ResultSet rsFromInsMaster;
    private ResultSet rsCategCount;
    private String strSONo;
    private ResultSet rsLogVals;
    private Object resultArray[][];
    private Object categArray[];
    
    public Inspection_Log_Table_Model(ResultSet rs1, ResultSet rs2, String soNo, ResultSet rs3)
    {
        colNames = (new String[] {
            "Category", "Inspections", "Good", "Poor", "Cleaned", "Repaired", "Replaced", "Replace at Next Service"
        });
        rsFromInsMaster = null;
        rsCategCount = null;
        strSONo = null;
        rsLogVals = null;
        resultArray = null;
        categArray = null;
        rsFromInsMaster = rs1;
        rsCategCount = rs2;
        strSONo = soNo;
        rsLogVals = rs3;
        try
        {
            rsCategCount.last();
            categArray = new Object[rsCategCount.getRow()];
            for(int x = 0; x < categArray.length; x++)
            {
                rsCategCount.absolute(x + 1);
                categArray[x] = rsCategCount.getString("Category");
            }
            
            /******************************************************************/   
            rsFromInsMaster.last();
            resultArray = new Object[rsFromInsMaster.getRow()][colNames.length + 1];
            int intCategCount = 0;
            for(int x = 0; x < resultArray.length; x++)
            {
                rsFromInsMaster.absolute(x + 1);
                if(x == 0)
                    resultArray[x][0] = rsFromInsMaster.getString("Category");
                else
                if(categArray[intCategCount].equals(rsFromInsMaster.getString("Category")))
                {
                    resultArray[x][0] = "";
                } else
                {
                    resultArray[x][0] = rsFromInsMaster.getString("Category");
                    intCategCount++;
                }
                resultArray[x][1] = rsFromInsMaster.getString("Description");
                for(int i = 2; i < colNames.length; i++)
                    resultArray[x][i] = "false";

                resultArray[x][colNames.length] = rsFromInsMaster.getString("Inspection_ID");
            }

            if(strSONo != "new")
                while(rsLogVals.next()) 
                {
                    
                    for(int x = 0; x < resultArray.length; x++)
                        if(resultArray[x][colNames.length].toString().equals(rsLogVals.getString("Inspection_ID")))
                        {
                            resultArray[x][2] = rsLogVals.getString("Good");
                            resultArray[x][3] = rsLogVals.getString("Poor");
                            resultArray[x][4] = rsLogVals.getString("Cleaned");
                            resultArray[x][5] = rsLogVals.getString("Repaired");
                            resultArray[x][6] = rsLogVals.getString("Replaced");
                            resultArray[x][7] = rsLogVals.getString("ReplaceatNext");
                        }

                }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getColumnName(int c)
    {
        return colNames[c];
    }

    public int getColumnCount()
    {
        return colNames.length;
    }

    public int getRowCount()
    {
        try
        {
            ResultSet rs = rsFromInsMaster;
            rs.last();
            int i = rs.getRow();
            return i;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        int j = 0;
        return j;
    }

    public Object getValueAt(int r, int c)
    {
        
        Object retVal = "empty";
        try
        {
            if(c == 0 || c == 1)
                retVal = resultArray[r][c].toString();
            else
                if(resultArray[r][c].toString().trim().equalsIgnoreCase("true"))
                    retVal = Boolean.TRUE;
                else
                    retVal = Boolean.FALSE;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            retVal = "empty";
        }
        
        return retVal;
    }

    public void setValueAt(Object value, int r, int c)
    {
        resultArray[r][c] = value.toString();
        fireTableDataChanged();
    }

    public Object[][] getResultArray()
    {
        return resultArray;
    }

    public boolean isCellEditable(int r, int c)
    {
        return c != 0 && c != 1;
    }

    public Class getColumnClass(int c)
    {
        return getValueAt(0, c).getClass();
    }

 
}