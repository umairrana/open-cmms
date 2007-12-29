package com.matrix.focus.mdi.messageBar;

import com.matrix.focus.util.MConfirmDialog;
import com.matrix.focus.util.MErrorDialog;
import com.matrix.focus.util.MInputDialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;

public class MessageBar extends JPanel{
        private Color ERROR_COLOR = Color.RED;
        private Color OK_COLOR = new Color(0,134,0,255);
        private Color WARN_COLOR = Color.decode("#FE8080");
        private Color CLOCK_COLOR = Color.WHITE;
        private Color BOTTOM_LINE_COLOR = Color.BLACK;
        private Color MESSAGE_BACKGROUND = Color.WHITE;
        
	private Message message;
	public MessageBar(){
		setLayout(null);
                this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		message = new Message();
		message.setBounds(0,5,1200,30);
		add(message);
	}
        
        
        public static final int YES_OPTION = 1;
        public static final int NO_OPTION = 0;
        
        public static int showConfirmDialog(JFrame parent,String message,String title){
            MConfirmDialog dlg = new MConfirmDialog(parent,title,true);
            dlg.setMessage(message);
            dlg.setLocationRelativeTo(parent);
            dlg.setVisible(true);
            return dlg.getSelectedOption();
        }
        
        public static int showConfirmDialog(JDialog parent,String message,String title){
            MConfirmDialog dlg = new MConfirmDialog(parent,title,true);
            dlg.setMessage(message);
            dlg.setLocationRelativeTo(parent);
            dlg.setVisible(true);
            return dlg.getSelectedOption();
        }
        
        public static String showInputDialog(JDialog parent,String input,String title){
            MInputDialog dlg = new MInputDialog(parent,title,true);
            dlg.setInput(input);
            dlg.setLocationRelativeTo(parent);
            dlg.setVisible(true);
            if(dlg.getSelectedOption()==YES_OPTION){
                return dlg.getInput();
            }
            else{
                return null;
            }
        }
        
        public static String showInputDialog(JFrame parent,String input,String title){
            MInputDialog dlg = new MInputDialog(parent,title,true);
            dlg.setInput(input);
            dlg.setLocationRelativeTo(parent);
            dlg.setVisible(true);
            if(dlg.getSelectedOption()==YES_OPTION){
                return dlg.getInput();
            }
            else{
                return null;
            }
        }
        
        public static void showErrorDialog(JDialog parent,String message,String title){
            MErrorDialog dlg = new MErrorDialog(parent,title,true);
            dlg.setMessage(message);
            dlg.setLocationRelativeTo(parent);
            dlg.setVisible(true);
        }
        
        public static void showErrorDialog(JFrame parent,String message,String title){
            MErrorDialog dlg = new MErrorDialog(parent,title,true);
            dlg.setMessage(message);
            dlg.setLocationRelativeTo(parent);
            dlg.setVisible(true);
        }
        
	public void setMessage(String msg, String type){
   	message.setMessage(msg, type);
  }
  public void clearMessage(){
   	message.clearMessage();	
    }
  public void paintComponent(Graphics g){
   	super.paintComponent(g);
   	g.setColor(BOTTOM_LINE_COLOR);
   	//g.fillRect(0,37,2000,40);
  }
  
  /**Message Class*/
  class Message extends JPanel{
    private JLabel iconlbl;
    private JLabel messagelbl;
    private Animator iconAnimator;
    private Color bar;
    
    public Message(){
      setBackground(MESSAGE_BACKGROUND);
      setLayout(null);
      iconlbl = new JLabel();
      iconlbl.setSize(30,30);
      iconlbl.setLocation(0,0);
      add(iconlbl);
      messagelbl = new JLabel();
      messagelbl.setFont(new Font("Tahoma",0,11));
      messagelbl.setSize(1000,30);
      messagelbl.setLocation(45,0);
      add(messagelbl);
      
      iconAnimator = new Animator(iconlbl,this);
    }
      public void setMessage(String message, String type){
        messagelbl.setText(message);
        iconAnimator.setMessageType(type);
        bar = getBarColor(type);
      }
      public void clearMessage(){
        messagelbl.setText("");
        bar = getBarColor("");
        iconlbl.setIcon(null);
        iconAnimator.stop();
      }
      public void paintComponent(Graphics g){
        super.paintComponent(g); 
        g.setColor(bar);
        g.drawRect(35,3,2000,25);
      }
      private Color getBarColor(String type){
          if(type.equalsIgnoreCase("OK")){
            return OK_COLOR;	
          }
          else if(type.equalsIgnoreCase("ERROR")){
            return ERROR_COLOR;
          }
          else if(type.equalsIgnoreCase("WARN")){
            return WARN_COLOR;
          }
          else if(type.equalsIgnoreCase("CLOCK")){
            return CLOCK_COLOR;
          }
          else{
            return null;
          }
       }
    }
  /**End Message Class*/
  
  /**Animator Class*/
  class Animator implements ActionListener{
    private JLabel icon;
    private JPanel messageBackground;
    private Timer animationTimer;
    private Color messageColor;
    private ImageCollection images;
    private ImageIcon img[];
    private ImageIcon imgClock[];
    private Color bg;
    private	int counter = 0;
    
    public Animator(JLabel iconLabel, JPanel backGround){
      bg = backGround.getBackground();
      icon = iconLabel;
      messageBackground = backGround;
      images = new ImageCollection();
      img = new ImageIcon[4];
      imgClock = new ImageIcon[32];
      animationTimer = new Timer (50, this);
    }
    public void setMessageType(String type){
      if(type.equalsIgnoreCase("CLOCK")){
        imgClock = images.getClockImages();
      }
      else{	
        img = images.getImages(type);
      }
      
      setMessageColor(type);
      counter = 0;
      animationTimer.start();
    }
    
    public void stop(){
        animationTimer.stop();
    }
    
    private void setMessageColor(String type){
      if(type.equalsIgnoreCase("OK")){
        messageColor = OK_COLOR;	
      }
      else if(type.equalsIgnoreCase("ERROR")){
        messageColor = ERROR_COLOR;
      }
      else if(type.equalsIgnoreCase("WARN")){
        messageColor = WARN_COLOR;
      }
      else if(type.equalsIgnoreCase("CLOCK")){
        messageColor = CLOCK_COLOR;
      }
    }
    public void actionPerformed (ActionEvent e){
      if(messageColor == CLOCK_COLOR){
        messageBackground.setBackground(messageColor);
        icon.setIcon(imgClock[counter]);
          counter++;
          if(counter == 32){
            counter = 0;
          }
      }
      else{
        if (counter == 0){
          messageBackground.setBackground(messageColor);
          icon.setIcon(img[0]);
            counter = 1;
          }
          else if(counter == 1){
            icon.setIcon(img[1]);
            counter = 2;
          }
          else if(counter == 2){
            icon.setIcon(img[2]);
            counter = 3;
          }
          else if(counter == 3){
            icon.setIcon(img[1]);
            counter = 4;
          }
          else if(counter == 4){
            icon.setIcon(img[0]);
            counter = 5;
          }
          else if(counter == 5){
            icon.setIcon(img[1]);
            counter = 6;
          }
          else if(counter == 6){
            icon.setIcon(img[2]);
            counter = 7;
          }
          else if(counter == 7){
            icon.setIcon(img[1]);
            counter = 8;
          }
          else{
            icon.setIcon(img[3]);
            counter = 0;
            messageBackground.setBackground(bg);
            animationTimer.stop();
          }
        }
    }
  }
  /**End Animator Class*/
  
  /**ImageCollection Class*/
  class ImageCollection{
    public ImageIcon images[];
    public ImageCollection(){
      images = new ImageIcon[44];
      //OK images
      images[0] = new ImageIcon(ClassLoader.getSystemResource("images/messagebar/ok/ok1.png"));
      images[1] = new ImageIcon(ClassLoader.getSystemResource("images/messagebar/ok/ok2.png"));
      images[2] = new ImageIcon(ClassLoader.getSystemResource("images/messagebar/ok/ok3.png"));
      images[3] = new ImageIcon(ClassLoader.getSystemResource("images/messagebar/ok/ok.png"));
      //ERROR images
      images[4] = new ImageIcon(ClassLoader.getSystemResource("images/messagebar/error/error1.png"));
      images[5] = new ImageIcon(ClassLoader.getSystemResource("images/messagebar/error/error2.png"));
      images[6] = new ImageIcon(ClassLoader.getSystemResource("images/messagebar/error/error3.png"));
      images[7] = new ImageIcon(ClassLoader.getSystemResource("images/messagebar/error/error.png"));
      //WARN images
      images[8] = new ImageIcon(ClassLoader.getSystemResource("images/messagebar/warn/warn1.png"));
      images[9] = new ImageIcon(ClassLoader.getSystemResource("images/messagebar/warn/warn2.png"));
      images[10] = new ImageIcon(ClassLoader.getSystemResource("images/messagebar/warn/warn3.png"));
      images[11] = new ImageIcon(ClassLoader.getSystemResource("images/messagebar/warn/warn.png"));
      
      //CLOCK images
      String fileName = "";
      for(int i=1; i<=32;i++){
        fileName = "images/messagebar/clock/" + i + ".png";
        images[11+i] = new ImageIcon(ClassLoader.getSystemResource(fileName));
      }
      
    }
    public ImageIcon[] getImages(String type){
      ImageIcon img[] = new ImageIcon[4];
      int firstImage = -1;
      
      if(type.equalsIgnoreCase("OK")){
        firstImage = 0;	
      }
      else if(type.equalsIgnoreCase("ERROR")){
        firstImage = 4;
      }
      else if(type.equalsIgnoreCase("WARN")){
        firstImage = 8;
      }
      img[0] = images[firstImage]; 
      img[1] = images[firstImage + 1]; 
      img[2] = images[firstImage + 2];
      img[3] = images[firstImage + 3];
      
      return img;
    }
    public ImageIcon[] getClockImages(){
      ImageIcon img[] = new ImageIcon[32];
      for(int i=0;i<32;i++){
        img[i] = images[11+i];
      }	
      return img;
    }
  }
  /**End ImageCollection*/
}
