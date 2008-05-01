package com.matrix.focus.mdi;

import com.matrix.focus.util.ImageLibrary;
import com.matrix.focus.util.MImage;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

public class PMWorkOrderDialog extends JDialog {
    private MImage img = new MImage(ImageLibrary.TITLE_PREVENTIVE_MAINTENANCE_WORKORDER);
    private JLabel jLabel2 = new JLabel();
    private JButton btnLog = new JButton(new ImageIcon(ImageLibrary.MENU_PREVENTIVE_LOG));
    private JButton btnEdit = new JButton(new ImageIcon(ImageLibrary.MENU_PREVENTIVE_WORKORDER));
    private JButton btnCancel = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
    public final int EDIT = 0;
    public final int MODIFY = 1;
    public final int LOG = 2;
    private int option = -1;

    public PMWorkOrderDialog(JFrame parent, String wo) {
        super(parent);
        try {
            jbInit();
            setLocationRelativeTo(parent);
            jLabel2.setText("Select what you want to do to the Job : " + wo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public int getOption(){
        return option;
    }
    
    private void jbInit() throws Exception {
        this.setSize(new Dimension(458, 106));
        this.setResizable(false);
        this.getContentPane().setLayout( null );
        this.setTitle("Job");
        this.setModal(true);
        img.setBounds(new Rectangle(15, 10, 50, 50));
        jLabel2.setBounds(new Rectangle(80, 10, 315, 25));
        btnLog.setText("Log");
        btnLog.setBounds(new Rectangle(195, 35, 105, 25));
        btnLog.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnLog_actionPerformed(e);
                    }
                });
        btnEdit.setText("Job");
        btnEdit.setBounds(new Rectangle(85, 35, 105, 25));
        btnEdit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnEdit_actionPerformed(e);
                    }
                });
        btnCancel.setText("Cancel");
        btnCancel.setBounds(new Rectangle(305, 35, 105, 25));
        btnCancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnCancel_actionPerformed(e);
                    }
                });
        this.getContentPane().add(btnCancel, null);
        this.getContentPane().add(btnEdit, null);
        this.getContentPane().add(btnLog, null);
        this.getContentPane().add(jLabel2, null);
        this.getContentPane().add(img, null);
    }

    private void btnCancel_actionPerformed(ActionEvent e) {
        option = -1;
        setVisible(false);
    }

    private void btnLog_actionPerformed(ActionEvent e) {
        option = 2;
        setVisible(false);
    }

    private void btnModify_actionPerformed(ActionEvent e){
        option = 1;
        setVisible(false);
    }

    private void btnEdit_actionPerformed(ActionEvent e){
        option = 0;
        setVisible(false);
    }
}
