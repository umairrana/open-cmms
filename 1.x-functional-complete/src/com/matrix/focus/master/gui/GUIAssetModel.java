package com.matrix.focus.master.gui;

import com.matrix.components.TitleBar;
import com.matrix.focus.mdi.messageBar.MessageBar;
import java.sql.Connection;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import com.matrix.components.MTextbox;
import com.matrix.focus.util.MPanel;
import com.matrix.focus.master.data.AssetModelData;
import com.matrix.focus.master.entity.AssetModel;
import com.matrix.focus.mdi.MDI;
import com.matrix.focus.util.Authorizer;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.DataAssistantDialog;
import com.matrix.focus.util.ImageLibrary;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GUIAssetModel extends MPanel{
   
    private JButton btnAdd = new JButton(new ImageIcon(ImageLibrary.BUTTON_NEW));
    private JButton btnEdit = new JButton(new ImageIcon(ImageLibrary.BUTTON_EDIT));
    private MTextbox mtxtModelID = new MTextbox();
    private JButton btnCancel = new JButton(new ImageIcon(ImageLibrary.BUTTON_CANCEL));
    private JButton btnSave = new JButton(new ImageIcon(ImageLibrary.BUTTON_SAVE));
    private JButton btnDelete = new JButton(new ImageIcon(ImageLibrary.BUTTON_DELETE));
    private JButton btnSearch = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    private JPanel jPanel2 = new JPanel();
    private TitleBar titlebar = new TitleBar();
    private JButton btnNewCat = new JButton(new ImageIcon(ImageLibrary.BUTTON_NEW));
    private MTextbox mtxtCategory = new MTextbox();
    private JButton btnPickCat = new JButton(new ImageIcon(ImageLibrary.BUTTON_SELECT));
    
    private Connection connection;
    private MDI frame;
    private MessageBar messageBar;
    private AssetModel model;

    public GUIAssetModel(DBConnectionPool pool, MDI frame, MessageBar msgBar)  {
        connection = pool.getConnection();
        this.frame = frame;
        this.messageBar = msgBar;
          
        try{
            titlebar.setTitle("Machine Model Information");
            titlebar.setDescription("The facility to manage Machine model information.");
            titlebar.setImage(ImageLibrary.TITLE_ASSET_MODEL);
            
            jbInit(); 
            setMode("LOAD");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception{
        this.setLayout(null);
        this.setSize(new Dimension(746, 271));
        btnAdd.setText("New");
        btnAdd.setBounds(new Rectangle(85, 175, 100, 25));
        btnAdd.setSize(new Dimension(100, 25));
        btnAdd.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
              btnAdd_actionPerformed(e);
            }
        });
        btnEdit.setText("Edit");
        btnEdit.setBounds(new Rectangle(295, 175, 100, 25));
        btnEdit.setSize(new Dimension(100, 25));
        btnEdit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnEdit_actionPerformed(e);
            }
        });
        mtxtModelID.setBounds(new Rectangle(20, 20, 260, 20));
        mtxtModelID.setTxtWidth(350);
        mtxtModelID.setCaption("Model ID");
        mtxtModelID.setEditable(false);
        mtxtModelID.setLblWidth(110);
        mtxtModelID.setFont(new Font("Tahoma", 0, 11));
        mtxtModelID.setTxtFont(new Font("Tahoma", 0, 11));
        mtxtModelID.setLblFont(new Font("Tahoma", 0, 11));
        btnCancel.setText("Cancel");
        btnCancel.setBounds(new Rectangle(505, 175, 100, 25));
        btnCancel.setSize(new Dimension(100, 25));
        btnCancel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnCancel_actionPerformed(e);
                }
        });
        btnSave.setText("Save");
        btnSave.setBounds(new Rectangle(400, 175, 100, 25));
        btnSave.setSize(new Dimension(100, 25));
        btnSave.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnSave_actionPerformed(e);
            }
        });
        btnDelete.setText("Delete");
        btnDelete.setBounds(new Rectangle(190, 175, 100, 25));
        btnDelete.setSize(new Dimension(100, 25));
        btnDelete.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnDelete_actionPerformed(e);
                    }
                });
        btnSearch.setBounds(new Rectangle(485, 20, 30, 20));
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                btnSearch_actionPerformed(e);
            }
        });
        jPanel2.setBounds(new Rectangle(10, 85, 595, 85));
        jPanel2.setBorder(BorderFactory.createTitledBorder("Machine Model Information"));
        jPanel2.setLayout(null);
        titlebar.setBounds(new Rectangle(10, 10, 595, 70));
        btnNewCat.setBounds(new Rectangle(520, 45, 30, 20));
        btnNewCat.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnNewCat_actionPerformed(e);
                    }
                });
        mtxtCategory.setBounds(new Rectangle(20, 45, 310, 20));
        mtxtCategory.setCaption("Machine Category");
        mtxtCategory.setEditable(false);
        mtxtCategory.setLblWidth(110);
        mtxtCategory.setTxtWidth(350);
        btnPickCat.setBounds(new Rectangle(485, 45, 30, 20));
        btnPickCat.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btnPickCat_actionPerformed(e);
                    }
                });
        jPanel2.add(btnPickCat, null);
        jPanel2.add(mtxtCategory, null);
        jPanel2.add(btnNewCat, null);
        jPanel2.add(btnSearch, null);
        jPanel2.add(mtxtModelID, null);
        this.add(titlebar, null);
        this.add(jPanel2, null);
        this.add(btnDelete, null);
        this.add(btnSave, null);
        this.add(btnCancel, null);
        this.add(btnAdd, null);
        this.add(btnEdit, null);
    }

    private void btnSave_actionPerformed(ActionEvent e){
            saveUpdateAssetModel();
    }
  
    private void saveUpdateAssetModel(){        
        try{
            model.setID(mtxtModelID.getInputText());
            model.setCategory(mtxtCategory.getInputText());
            
            if(model.isSaved()){
                AssetModelData.updateAssetModel(model,connection);
                messageBar.setMessage("Machine model details updated","OK");
            }
            else{
                AssetModelData.saveAssetModel(model,connection);
                messageBar.setMessage("New Machine model saved","OK");
            }
            setMode("SEARCH");
        } 
        catch (Exception ex){
            ex.printStackTrace();
            messageBar.setMessage(ex.getMessage(),"ERROR");
        } 
    }

    private void btnEdit_actionPerformed(ActionEvent e){
        setMode("EDIT");
    }
    
    private void btnAdd_actionPerformed(ActionEvent e){
        setMode("ADD");
        model = new AssetModel();
    }
  
    private void btnCancel_actionPerformed(ActionEvent e){
        setMode("LOAD");
    }

    private void btnSearch_actionPerformed(ActionEvent e) {   
        DataAssistantDialog d = new DataAssistantDialog(frame,"Select Model - Data Assistant","SELECT Model_ID AS 'Model' FROM asset_model WHERE Deleted ='false'",connection);
        d.setFirstColumnWidth(300);
        d.setLocationRelativeTo(frame);
        d.setVisible(true); 
        String rtnVal = d.getValue();
        if(!rtnVal.isEmpty()){
            setAssetModel(rtnVal);
        }
    }
    
    private void setAssetModel(String id){
        try{
            model = AssetModelData.getAssetModel(id,connection);
            mtxtModelID.setInputText(model.getID());
            mtxtCategory.setInputText(model.getCategory());
            setMode("SEARCH");
        }
        catch(Exception e){
            messageBar.setMessage(e.getMessage(),"ERROR");
        }
    }

    private void btnDelete_actionPerformed(ActionEvent e){
        try{
            if(MessageBar.showConfirmDialog(frame,"Are you sure you want to delete \nthe selected Machine model from the system?","Machine Model")==MessageBar.YES_OPTION){
                AssetModelData.deleteAssetModel(model.getID(),connection);
                setMode("LOAD");
                messageBar.setMessage("Machine model deleted","OK");
            }
        } 
        catch (Exception er){
            messageBar.setMessage(er.getMessage(),"ERROR");
        }
    }

    private void btnNewCat_actionPerformed(ActionEvent e) {
        frame.menuItemGUIAssetCategory.doClick();
    }

    private void btnPickCat_actionPerformed(ActionEvent e) {
        DataAssistantDialog d = new DataAssistantDialog( frame,"Select Category  - Data Assistant","SELECT Category_ID AS 'Category ID' FROM asset_category WHERE Deleted !='true'",connection);
        d.setFirstColumnWidth(300);
        d.setLocationRelativeTo(btnPickCat);
        d.setVisible(true); 
        String rtnVal = d.getValue();
        if(!(rtnVal.equals(""))){
            mtxtCategory.setInputText(rtnVal);
        }
    }
    
    private void setMode(String mode){
        if(mode.equals("EDIT")){
            setEditable(true);
            mtxtModelID.setEditable(false);
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
        mtxtModelID.setEditable(value);
        btnNewCat.setEnabled(value);
        btnPickCat.setEnabled(value);
    }

    private void clearAll(){
        mtxtModelID.setInputText("");
        mtxtCategory.setInputText("");
        model = null;
        messageBar.clearMessage();
    }
}
