package project1;
//copyright at Matrix Pvt Ltd.

import Panel2;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.Rectangle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class Frame1 extends JFrame {
    private JTextArea jTextArea1 = new JTextArea();
    private JTextArea jTextArea2 = new JTextArea();
    private JTextArea jTextArea3 = new JTextArea();
    private JTextArea jTextArea4 = new JTextArea();
    private JTextArea jTextArea5 = new JTextArea();
    private JTextArea jTextArea6 = new JTextArea();
    private JTextArea jTextArea7 = new JTextArea();
    private JTextArea jTextArea8 = new JTextArea();
    private JTextArea jTextArea9 = new JTextArea();
    private double category[] = new double[8];
   
    private JLabel jLabel1 = new JLabel();
    private JButton jButton1 = new JButton();
    private JLabel jLabel2 = new JLabel();
    private JTextArea jTextArea10 = new JTextArea();
    private JTextArea jTextArea11 = new JTextArea();
    private JLabel jLabel3 = new JLabel();
    private JLabel jLabel4 = new JLabel();
    private JLabel jLabel5 = new JLabel();
    private JTextArea jTextArea12 = new JTextArea();
    private JTextArea jTextArea13 = new JTextArea();
    private JTextArea jTextArea14 = new JTextArea();
    private JLabel jLabel6 = new JLabel();
    private JLabel jLabel7 = new JLabel();
    private JLabel jLabel8 = new JLabel();
    private JLabel jLabel9 = new JLabel();
    private JLabel jLabel10 = new JLabel();
    private JLabel jLabel11 = new JLabel();
    private JLabel jLabel12 = new JLabel();
    private JLabel jLabel13 = new JLabel();

    public Frame1() {
        try {
            
            
            jbInit();
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(800,600);
            setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout( null );
        this.setSize(new Dimension(578, 461));
        this.setBackground(Color.white);
        jTextArea1.setBounds(new Rectangle(55, 105, 60, 200));
        jTextArea1.setEditable(false);
        jTextArea2.setBounds(new Rectangle(115, 105, 60, 200));
        jTextArea2.setEditable(false);
        jTextArea3.setBounds(new Rectangle(175, 105, 60, 200));
        jTextArea3.setEditable(false);
        jTextArea4.setBounds(new Rectangle(235, 105, 60, 200));
        jTextArea4.setEditable(false);
        jTextArea5.setBounds(new Rectangle(295, 105, 60, 200));
        jTextArea5.setEditable(false);
        jTextArea6.setBounds(new Rectangle(20, 350, 60, 65));
        jTextArea6.setEditable(false);
        jTextArea6.setBackground(new Color(231, 231, 0));
        jTextArea7.setBounds(new Rectangle(355, 105, 60, 200));
        jTextArea7.setEditable(false);
        jTextArea8.setBounds(new Rectangle(415, 105, 60, 200));
        jTextArea8.setEditable(false);
        jTextArea9.setBounds(new Rectangle(475, 105, 60, 200));
        jTextArea9.setEditable(false);
        jLabel1.setText("jLabel1");
        jLabel1.setBounds(new Rectangle(130, 15, 165, 25));
        jButton1.setText("Press Me");
        jButton1.setBounds(new Rectangle(340, 35, 115, 35));
        jButton1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        jButton1_actionPerformed(e);
                    }
                });
        jLabel2.setText("Total Expense");
        jLabel2.setBounds(new Rectangle(20, 15, 100, 20));
        jTextArea10.setBounds(new Rectangle(80, 350, 60, 65));
        jTextArea10.setBackground(new Color(33, 33, 255));
        jTextArea10.setEditable(false);
        jTextArea11.setBounds(new Rectangle(140, 350, 60, 65));
        jTextArea11.setBackground(new Color(198, 0, 0));
        jTextArea11.setEditable(false);
        jLabel3.setText("blue background color 33,33,255");
        jLabel3.setBounds(new Rectangle(270, 360, 210, 20));
        jLabel4.setText("red background color 198,0,0");
        jLabel4.setBounds(new Rectangle(270, 380, 210, 20));
        jLabel5.setText("yellow background color 231,231,0");
        jLabel5.setBounds(new Rectangle(270, 400, 210, 20));
        jTextArea12.setBounds(new Rectangle(115, 170, 60, 135));
        jTextArea12.setEditable(false);
        jTextArea13.setBounds(new Rectangle(55, 210, 60, 95));
        jTextArea13.setEditable(false);
        jTextArea14.setBounds(new Rectangle(175, 140, 60, 165));
        jTextArea14.setEditable(false);
        jLabel6.setText("jLabel6");
        jLabel6.setBounds(new Rectangle(55, 305, 55, 20));
        jLabel7.setText("jLabel6");
        jLabel7.setBounds(new Rectangle(175, 305, 55, 20));
        jLabel8.setText("jLabel6");
        jLabel8.setBounds(new Rectangle(115, 305, 55, 20));
        jLabel9.setText("jLabel6");
        jLabel9.setBounds(new Rectangle(235, 305, 55, 20));
        jLabel10.setText("jLabel6");
        jLabel10.setBounds(new Rectangle(295, 305, 55, 20));
        jLabel11.setText("jLabel6");
        jLabel11.setBounds(new Rectangle(355, 305, 55, 20));
        jLabel12.setText("jLabel6");
        jLabel12.setBounds(new Rectangle(415, 305, 55, 20));
        jLabel13.setText("jLabel6");
        jLabel13.setBounds(new Rectangle(475, 305, 55, 20));
        this.getContentPane().add(jLabel13, null);
        this.getContentPane().add(jLabel12, null);
        this.getContentPane().add(jLabel11, null);
        this.getContentPane().add(jLabel10, null);
        this.getContentPane().add(jLabel9, null);
        this.getContentPane().add(jLabel8, null);
        this.getContentPane().add(jLabel7, null);
        this.getContentPane().add(jLabel6, null);
        this.getContentPane().add(jTextArea14, null);
        this.getContentPane().add(jTextArea13, null);
        this.getContentPane().add(jTextArea12, null);
        this.getContentPane().add(jLabel5, null);
        this.getContentPane().add(jLabel4, null);
        this.getContentPane().add(jLabel3, null);
        this.getContentPane().add(jTextArea11, null);
        this.getContentPane().add(jTextArea10, null);
        this.getContentPane().add(jLabel2, null);
        this.getContentPane().add(jButton1, null);
        this.getContentPane().add(jLabel1, null);
        this.add(jTextArea9, null);
        this.add(jTextArea8, null);
        this.add(jTextArea7, null);
        this.add(jTextArea6, null);
        this.add(jTextArea5, null);
        this.add(jTextArea4, null);
        this.add(jTextArea3, null);
        this.add(jTextArea2, null);
        this.add(jTextArea1, null);
    }
    
    public static void main(String arg[])
    {
            new Frame1();
    }

    private void jButton1_actionPerformed(ActionEvent e) //all the function is here. calculating avarages, total, and printing
    {
        long totalexpense = 0;
        category[0] = 150000;
        category[1] = 60000;
        category[2] = 230000;
        category[3] = 150000;
        category[4] = 50000;
        category[5] = 700000;
        category[6] = 140000;
        category[7] = 200000;
        
        
        for (int i=0; i<8; i++) 
        {
                totalexpense += category[i];
        }
        jLabel1.setText(totalexpense+"");
        
        /**calculate average values here
         * average for category1 is = category[0]/totalexpense*100
         */
        
         double average[] = new double[8];
         average[0] = category[0]/totalexpense*100;
         
        /**if its less than 25% of totalexpense set the back color as follow*/
        
        jTextArea13.setBackground(new Color(231,231,0));;
        jTextArea12.setBackground(new Color(198,0,0));;
        jTextArea14.setBackground(new Color(33,33,255));;
        
        /**calculate the average and choose the color appropriately**/
        /**call me if there is a prob*/
        
        
        
    }
}
