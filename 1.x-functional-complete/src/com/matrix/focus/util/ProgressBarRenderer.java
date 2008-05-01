package com.matrix.focus.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ProgressBarRenderer extends JLabel implements TableCellRenderer{
    private JTable table;
    private int completed;
    private int column;
    private float elapsed;
    private float due;
    private int gap;
    
    public ProgressBarRenderer(JTable table, int gap){
        setOpaque(true);
        setHorizontalAlignment(JLabel.CENTER);
        this.table = table;
        this.gap = gap;
    }

    public Component getTableCellRendererComponent(JTable table, 
                                                  Object value, 
                                                  boolean isSelected, 
                                                  boolean hasFocus, 
                                                  int row, 
                                                  int column) {
       setText(value.toString()+"%");
       setBackground(isSelected?table.getSelectionBackground():table.getBackground());
       setForeground(isSelected?table.getSelectionForeground():table.getForeground());
       this.column = column;
       
       String arg[] = value.toString().split("_");
       this.completed = Integer.parseInt(arg[0]);
       
       arg = arg[1].split("-");
       this.elapsed = ( Integer.parseInt(arg[0]) + (Float.parseFloat(arg[1])/24) + (Float.parseFloat(arg[2])/(24*60)) );
       this.due = (elapsed * 100) / gap;
       return this;
    }
    
    public void paint(Graphics g){
        /**Duration*/
        g.setColor(Color.GRAY);
        g.fillRect(0,0,table.getColumnModel().getColumn(column).getWidth(),table.getRowHeight()/3);
        
        /**Due*/
        g.setColor(Color.GREEN);
        g.fillRect(0,table.getRowHeight()/3,(int)getValue(due),table.getRowHeight()/3);
        
        /**Completed*/
        g.setColor(Color.BLUE);
        g.fillRect(0,table.getRowHeight()*2/3,(int)getValue(completed),table.getRowHeight()/3);
        
        g.setFont(new Font("",1,12));
        g.setColor(Color.BLACK);
        g.drawString("Completed "+completed+"%",table.getColumnModel().getColumn(column).getWidth()/2,12);
        
    }
            
    private float getValue(float value){
        return (table.getColumnModel().getColumn(column).getWidth() /100) * value;
    }
}
