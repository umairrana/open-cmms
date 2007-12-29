package com.matrix.focus.util;

import java.awt.Graphics;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class MImage extends JLabel{
    private ImageIcon image;
   
    public MImage(String imagePath){
        this.image = new ImageIcon(imagePath) ;
    }
    
    public MImage(URL imagePath){
        this.image = new ImageIcon(imagePath) ;
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(image!=null){
            g.drawImage(image.getImage(),0,0,this.getWidth(),this.getHeight(),this);
        }           
    } 
}