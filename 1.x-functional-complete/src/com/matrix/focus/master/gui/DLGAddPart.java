package com.matrix.focus.master.gui;

import com.matrix.components.MTextbox;

import com.matrix.focus.master.entity.PartsMaster;

import com.matrix.focus.mdi.MDI;

import com.matrix.focus.mdi.messageBar.MessageBar;

import com.matrix.focus.util.ImageLibrary;

import java.awt.Dimension;
import java.awt.Frame;

import java.awt.Rectangle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class DLGAddPart extends JDialog {
    private MTextbox mtxtPartId = new MTextbox();
    private MTextbox mtxtDesc = new MTextbox();
    private JLabel jLabel1 = new JLabel();
    private JTextArea jtaRemarks = new JTextArea();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JButton btnAdd = new JButton(new ImageIcon(ImageLibrary.BUTTON_ADD));
    private JButton btnCancel = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
    
    PartsMaster part = null;
    private JPanel jpAddPart = new JPanel();
    private JPanel jPanel1 = new JPanel();

    public DLGAddPart() {
        this(null, "", false);
    }

    public DLGAddPart(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setSize(new Dimension(376, 261));
        this.getContentPane().setLayout( null );
        this.setTitle("Add Part");
        mtxtPartId.setBounds(new Rectangle(20, 20, 305, 25));
        mtxtPartId.setCaption("Part ID");
        mtxtDesc.setBounds(new Rectangle(20, 45, 305, 25));
        mtxtDesc.setCaption("Description");
        jLabel1.setText("Remarks");
        jLabel1.setBounds(new Rectangle(20, 70, 60, 20));
        jScrollPane1.setBounds(new Rectangle(115, 70, 200, 90));
        btnAdd.setText("Add");
        btnAdd.setBounds(new Rectangle(185, 190, 80, 25));
        btnAdd.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnAdd_actionPerformed(e);
                    }
                });
        btnCancel.setText("Cancel");
        btnCancel.setBounds(new Rectangle(275, 190, 80, 25));
        btnCancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnCancel_actionPerformed(e);
                    }
                });
        jpAddPart.setBounds(new Rectangle(15, 5, 330, 155));
        jpAddPart.setBorder(BorderFactory.createTitledBorder("Add Part"));
        jPanel1.setBounds(new Rectangle(15, 15, 340, 170));
        jPanel1.setBorder(BorderFactory.createTitledBorder("Add Part"));
        jPanel1.setLayout(null);
        jPanel1.add(mtxtPartId, null);
        jPanel1.add(mtxtDesc, null);
        jPanel1.add(jLabel1, null);
        jScrollPane1.getViewport().add(jtaRemarks, null);
        jPanel1.add(jScrollPane1, null);
        this.getContentPane().add(jPanel1, null);
        this.getContentPane().add(btnCancel, null);
        this.getContentPane().add(btnAdd, null);
    }
    
    private void setValue(){
         part = new PartsMaster();
         part.setPartId(mtxtPartId.getInputText());  
         part.setDescription(mtxtDesc.getInputText());
         part.setRemarks(jtaRemarks.getText());
         part.setCreator(MDI.USERNAME);
    }
    
    public PartsMaster getValue(){  
        return part;
    }

    private void btnAdd_actionPerformed(ActionEvent e) {
        if(mtxtPartId.getInputText().equals("") || mtxtDesc.getInputText().equals("") ){
            MessageBar.showErrorDialog(this, "Part Id and Description should be entered before adding a part!", "Error");
        }else{
            setValue();
            setVisible(false);
        }
    }

    private void btnCancel_actionPerformed(ActionEvent e) {
        part = null;
        setVisible(false);
    }
}
