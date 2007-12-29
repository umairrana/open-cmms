package com.matrix.focus.util;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ButtonCellRenderer extends JLabel implements TableCellRenderer{
    public ButtonCellRenderer(){
       setOpaque(true);
       setBackground(Color.WHITE);
       setHorizontalAlignment(JButton.CENTER);
       setIcon(new ImageIcon(ImageLibrary.BUTTON_POPUP));
    }

    public Component getTableCellRendererComponent(JTable table, 
                                                  Object value, 
                                                  boolean isSelected, 
                                                  boolean hasFocus, 
                                                  int row, 
                                                  int column) {
       setBackground(isSelected?table.getSelectionBackground():table.getBackground());
       setForeground(isSelected?table.getSelectionForeground():table.getForeground());
       return this;
    }
}