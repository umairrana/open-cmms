package com.matrix.focus.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class AssetListRenderer extends JLabel implements ListCellRenderer{
    public AssetListRenderer(){
        setFont(new Font("System",0,12));
        setOpaque(true);
        setBackground(Color.WHITE);
        setIcon(new ImageIcon(ImageLibrary.TREE_ASSET));
    }
    public Component getListCellRendererComponent(JList list, Object value, 
                                                  int index, 
                                                  boolean isSelected, 
                                                  boolean cellHasFocus) {
        this.setText(value.toString());
        if(isSelected){
          this.setBackground(list.getSelectionBackground());
          this.setForeground(list.getSelectionForeground());
        }
        else{
          this.setBackground(list.getBackground());
          this.setForeground(list.getForeground());
        }
        return this;
    }
}
