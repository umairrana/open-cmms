package com.matrix.focus.mdi;

import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

public class MMenuItem extends JMenuItem {
    public MMenuItem(String caption, URL icon) {
        super(caption);
        super.setIcon(new ImageIcon(icon));
    }
}
