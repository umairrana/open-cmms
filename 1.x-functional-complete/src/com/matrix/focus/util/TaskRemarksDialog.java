package com.matrix.focus.util;


import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TaskRemarksDialog extends JDialog {
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JTextArea txtRemarks = new JTextArea();
    private JButton btnOK = new JButton(new ImageIcon(ImageLibrary.BUTTON_OK));

    public TaskRemarksDialog(JFrame frame) {
        this(frame, "", true);
        this.setTitle("Task Remarks");
    }

    public TaskRemarksDialog(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        try {
            
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                setText("null");
            }
        });
    }
    
    public TaskRemarksDialog(Frame parent, String title, boolean modal, boolean editable) {
        super(parent, title, modal);
        try {
            
            jbInit();
            txtRemarks.setEditable(editable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                setText("null");
            }
        });
    }
    
    public void setText(String remarks){
        txtRemarks.setText(remarks);
    }
    public String getText(){
        return txtRemarks.getText();
    }
    
    private void jbInit() throws Exception {
        this.setSize(new Dimension(400, 187));
        this.setResizable(false);
        this.getContentPane().setLayout( null );
        jScrollPane1.setBounds(new Rectangle(5, 5, 385, 115));
        btnOK.setText("OK");
        btnOK.setBounds(new Rectangle(295, 125, 95, 25));
        getRootPane().setDefaultButton(btnOK);
        btnOK.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnOK_actionPerformed(e);
                    }
                });
        jScrollPane1.getViewport().add(txtRemarks, null);
        this.getContentPane().add(btnOK, null);
        this.getContentPane().add(jScrollPane1, null);
    }

    private void btnOK_actionPerformed(ActionEvent e) {
        setVisible(false);
    }
}
