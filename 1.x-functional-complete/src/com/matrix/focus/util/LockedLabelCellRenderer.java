package com.matrix.focus.util;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class LockedLabelCellRenderer extends JLabel implements TableCellRenderer{
    private Color color;
    private String prefix;
    
    public LockedLabelCellRenderer( ){
        setOpaque(true);
        color = Color.LIGHT_GRAY;
        prefix = "";
        setBackground(color);
    }
    
    public LockedLabelCellRenderer(Color color){
        setOpaque(true);
        this.color = color;
        prefix = "";
        setBackground(color);
    }
    
    public LockedLabelCellRenderer(Color color, String prefix){
        setOpaque(true);
        this.color = color;
        this.prefix = prefix;
        setBackground(color);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        this.setText(prefix+value.toString());
        if(isSelected){
            this.setBackground(table.getSelectionBackground());
            this.setForeground(table.getSelectionForeground());
        }
        else{
            this.setBackground(color);
            this.setForeground(table.getForeground());
        }
        return this;
    }
}
