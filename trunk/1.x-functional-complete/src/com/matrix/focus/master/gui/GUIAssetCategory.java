/********************************************************
 *  Class   = GUICompany.java
 *  Details = The GUI for managing Asset Categories
 *  Author  = Dimuthu
 *  Date    = 2006.05.16
 * ******************************************************/
 
package com.matrix.focus.master.gui;

import com.matrix.components.TitleBar;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.util.DataAssistantDialog;
import java.sql.Connection;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.Rectangle;
import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JTextPane;
import com.matrix.components.MDataCombo;
import javax.swing.JButton;
import com.matrix.components.MTextbox;
import com.matrix.focus.util.MPanel;
import com.matrix.focus.master.data.AssetCategoryData;
import com.matrix.focus.master.entity.AssetCategory;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.ImageLibrary;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GUIAssetCategory extends MPanel{
    
    private JTextPane txtRemarks = new JTextPane();
    private JPanel jPanel1 = new JPanel();
    private MDataCombo mDataCombo1 = new MDataCombo();
    private JButton btnCancel = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
    private JButton btnSave = new JButton(new ImageIcon(ImageLibrary.BUTTON_SAVE));
    private JButton btnDelete = new JButton(new ImageIcon(ImageLibrary.BUTTON_DELETE));
    private JButton btnAdd = new JButton(new ImageIcon(ImageLibrary.BUTTON_NEW));
    private JButton btnEdit = new JButton(new ImageIcon(ImageLibrary.BUTTON_EDIT));
    private MTextbox txtEmployee = new MTextbox();
    private MTextbox txtCategory = new MTextbox();
    private JButton btnCat = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JButton btnEmp = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JScrollPane jScrollPane1 = new JScrollPane();
    private TitleBar titlebar = new TitleBar();
    private JLabel jLabel1 = new JLabel();
    
    private AssetCategory assetCategory;
    private Connection connection;
    private JFrame frame;
    private MessageBar messageBar;

    public GUIAssetCategory(DBConnectionPool pool, JFrame frame, MessageBar msgBar){
        connection = pool.getConnection();
        this.frame = frame;
        this.messageBar = msgBar;
    
        try{
            titlebar.setTitle("Machine Category Information");
            titlebar.setDescription("The facility to manage Machine category information.");
            titlebar.setImage(ImageLibrary.TITLE_ASSET_CATEGORY);
            
            jbInit();
            setMode("LOAD");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception{
        this.setLayout(null);
        this.setSize(new Dimension(713, 341));
        txtRemarks.setFont(new Font("System", 0, 11));
        btnEmp.setBounds(new Rectangle(345, 50, 30, 20));
        btnEmp.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnSearch_actionPerformed(e);
            }
        });
        jScrollPane1.setBounds(new Rectangle(140, 75, 430, 100));
        titlebar.setBounds(new Rectangle(10, 10, 695, 70));
        jLabel1.setText("Remarks");
        jLabel1.setBounds(new Rectangle(30, 70, 55, 20));
        jPanel1.setBounds(new Rectangle(10, 85, 590, 200));
        jPanel1.setLayout(null);
        jPanel1.setBorder(BorderFactory.createTitledBorder("Machine Category Information"));
        mDataCombo1.setBounds(new Rectangle(0, 0, 1, 1));
        btnCancel.setText("Cancel");
        btnCancel.setBounds(new Rectangle(605, 215, 100, 25));
        btnCancel.setSize(new Dimension(100, 25));
        btnCancel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnCancel_actionPerformed(e);
            }
        });
        btnSave.setText("Save");
        btnSave.setBounds(new Rectangle(605, 185, 100, 25));
        btnSave.setSize(new Dimension(100, 25));
        btnSave.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnSave_actionPerformed(e);
            }
        });
        btnDelete.setText("Delete");
        btnDelete.setBounds(new Rectangle(605, 125, 100, 25));
        btnDelete.setSize(new Dimension(100, 25));
        btnDelete.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnDelete_actionPerformed(e);
                    }
                });
        btnAdd.setText("New");
        btnAdd.setBounds(new Rectangle(605, 95, 100, 25));
        btnAdd.setSize(new Dimension(100, 25));
        btnAdd.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnAdd_actionPerformed(e);
            }
        });
        btnEdit.setText("Edit");
        btnEdit.setBounds(new Rectangle(605, 155, 100, 25));
        btnEdit.setSize(new Dimension(100, 25));
        btnEdit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnEdit_actionPerformed(e);
            }
        });
        txtEmployee.setBounds(new Rectangle(30, 50, 310, 20));
        txtEmployee.setCaption("Officer In Charge");
        txtEmployee.setEditable(false);
        txtEmployee.setLblFont(new Font("Tahoma", 0, 11));
        txtEmployee.setLblWidth(110);
        txtCategory.setBounds(new Rectangle(30, 25, 275, 25));
        txtCategory.setCaption("Category ID");
        txtCategory.setLblFont(new Font("Tahoma", 0, 11));
        txtCategory.setLblWidth(110);
        txtCategory.setTxtWidth(300);
        btnCat.setBounds(new Rectangle(445, 25, 30, 20));
        btnCat.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnCat_actionPerformed(e);
            }
        });
        jPanel1.add(jLabel1, null);
        jPanel1.add(btnCat, null);
        jPanel1.add(txtCategory, null);
        jPanel1.add(mDataCombo1, null);
        jPanel1.add(txtEmployee, null);
        jPanel1.add(btnEmp, null);
        jScrollPane1.getViewport().add(txtRemarks, null);
        jPanel1.add(jScrollPane1, null);
        mDataCombo1.setCaption("Machine Type");
        this.add(mDataCombo1, null);
        this.add(titlebar, null);
        this.add(btnDelete, null);
        this.add(btnSave, null);
        this.add(btnCancel, null);
        this.add(jPanel1, null);
        this.add(btnAdd, null);
        this.add(btnEdit, null);
    }

    private void btnSave_actionPerformed(ActionEvent e){
        saveUpdateAssetCategory();
    }

    private void saveUpdateAssetCategory(){
        try{
            assetCategory.setID(txtCategory.getInputText());
            assetCategory.setOfficer(txtEmployee.getInputText());
            assetCategory.setRemarks(txtRemarks.getText());
            
            if(assetCategory.isSaved()){
                AssetCategoryData.updateAssetCategory(assetCategory,connection);
                messageBar.setMessage("Machine category details updated","OK");
            }
            else{
                AssetCategoryData.saveAssetCategory(assetCategory,connection);
                messageBar.setMessage("New Machine category saved","OK");
            }
            setMode("SEARCH");
        } 
        catch (Exception ex){
            ex.printStackTrace();
            messageBar.setMessage(ex.getMessage(),"ERROR");
        } 
    }
    
    private void btnAdd_actionPerformed(ActionEvent e){
        setMode("ADD");
        assetCategory = new AssetCategory();
    }

    private void btnEdit_actionPerformed(ActionEvent e){
        setMode("EDIT");
    }

    private void btnCancel_actionPerformed(ActionEvent e){
        setMode("LOAD");
    }

    private void btnSearch_actionPerformed(ActionEvent e){
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select Officer - Data Assistant","SELECT Employee_ID AS 'Employee ID', Title, Name FROM employee",connection);
        d.setSecondColumnWidth(75);
        d.setThirdColumnWidth(150);
        d.setLocationRelativeTo(frame);
        d.setVisible(true); 
        String rtnVal = d.getValue();
        if (!(rtnVal.equals(""))){
            txtEmployee.setInputText(rtnVal);
        }
    }

    private void btnCat_actionPerformed(ActionEvent e){    
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select Category - Data Assistant","SELECT Category_ID AS 'Category' FROM asset_category WHERE deleted='false'",connection);
        d.setFirstColumnWidth(300);
        d.setLocationRelativeTo(frame);
        d.setVisible(true); 
        String rtnVal = d.getValue();
        if(!rtnVal.isEmpty()){
            setAssetCategory(rtnVal);
        }
    }

    private void setAssetCategory(String id){
        try{
            assetCategory = AssetCategoryData.getAssetCategory(id,connection);
            txtCategory.setInputText(assetCategory.getID());
            txtEmployee.setInputText(assetCategory.getOfficer());
            txtRemarks.setText(assetCategory.getRemarks());
            setMode("SEARCH");
        }
        catch(Exception e){
            messageBar.setMessage(e.getMessage(),"ERROR");
        }
    }

    private void btnDelete_actionPerformed(ActionEvent e){
            try{
                if(MessageBar.showConfirmDialog(frame,"Are you sure you want to delete \nthe selected Machine category from the system?","Machine Category")==MessageBar.YES_OPTION){
                    AssetCategoryData.deleteAssetCategory(assetCategory.getID(),connection);
                    setMode("LOAD");
                    messageBar.setMessage("Machine category deleted","OK");
                }
            } 
            catch (Exception er){
                messageBar.setMessage(er.getMessage(),"ERROR");
            }
    }
    
    private void setMode(String mode){
        if(mode.equals("EDIT")){
            setEditable(true);
            txtCategory.setEditable(false);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
            btnAdd.setEnabled(false);
            btnEdit.setEnabled(false);
        }
        else if(mode.equals("ADD")){
            clearAll();
            setEditable(true);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
            btnAdd.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
        }
        else if(mode.equals("LOAD")){
            clearAll();
            btnAdd.setEnabled(true);
            btnSave.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            btnCancel.setEnabled(false);
            setEditable(false);
        }
        else if(mode.equals("SEARCH")){
            btnEdit.setEnabled(true);
            btnDelete.setEnabled(true);
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
            btnAdd.setEnabled(false);
            setEditable(false);
        }
    }
    
    private void setEditable(boolean value){
        btnEmp.setEnabled(value);
        txtCategory.setEditable(value);
        txtRemarks.setEditable(value);
    }

    private void clearAll(){
        txtCategory.setInputText("");
        txtEmployee.setInputText("");
        txtRemarks.setText("");
        assetCategory = null;
        messageBar.clearMessage();
    }
}
