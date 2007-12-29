package com.matrix.focus.inspection;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class Reading_Log_TableCellRenderer extends DefaultTableCellRenderer
{
  public Reading_Log_TableCellRenderer()
  {
    super();
    this.setOpaque(true);
  }
  public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus,int row,int col)
  {
    Color backColor = new Color(136,206,245);
    if(value.equals(""))
      this.setBackground(Color.WHITE);
    else
      this.setBackground(backColor);
    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
  }
}