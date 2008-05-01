package com.matrix.focus.mdi;

import java.awt.Color;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JMenu;

public class MMenu extends JMenu {
    public MMenu(String caption, URL icon){
        super(caption);
        super.setIcon(new ImageIcon(icon));
    }
    public MMenu(String caption){
        super(caption);
        setOpaque(true);
        //super.setForeground(Color.WHITE);
    }
}
