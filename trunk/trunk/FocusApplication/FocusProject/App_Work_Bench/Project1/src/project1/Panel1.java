package project1;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Panel1 extends JPanel {

    private JTextArea jTextArea1 = new JTextArea();
    private JTextArea jTextArea2 = new JTextArea();
    private JTextArea jTextArea3 = new JTextArea();
    private JTextArea jTextArea4 = new JTextArea();
    private JTextArea jTextArea5 = new JTextArea();

    public Panel1() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout( null );
        this.setSize(new Dimension(576, 428));
        this.setBackground(Color.white);
        jTextArea1.setBounds(new Rectangle(55, 115, 60, 190));
        jTextArea2.setBounds(new Rectangle(115, 115, 60, 190));
        jTextArea3.setBounds(new Rectangle(175, 115, 60, 190));
        jTextArea4.setBounds(new Rectangle(235, 115, 60, 190));
        jTextArea5.setBounds(new Rectangle(295, 115, 60, 190));
        this.add(jTextArea5, null);
        this.add(jTextArea4, null);
        this.add(jTextArea3, null);
        this.add(jTextArea2, null);
        this.add(jTextArea1, null);
    }
}
