package com.matrix.focus.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class LockedCheckBoxCellRenderer extends JCheckBox implements TableCellRenderer{
    public LockedCheckBoxCellRenderer(){
        setFont(new Font("System",0,12));
        setOpaque(true);
        setHorizontalAlignment(JCheckBox.CENTER);
    }

    public Component getTableCellRendererComponent(JTable table, 
                                                          Object value, 
                                                          boolean isSelected, 
                                                          boolean hasFocus, 
                                                          int row, 
                                                          int column) {
        this.setSelected(Boolean.parseBoolean(value.toString()));
        if(isSelected){
            this.setBackground(table.getSelectionBackground());
            this.setForeground(table.getSelectionForeground());
        }
        else{
            this.setBackground(Color.LIGHT_GRAY);
            this.setForeground(table.getForeground());
        }         
        return this;
    }
}