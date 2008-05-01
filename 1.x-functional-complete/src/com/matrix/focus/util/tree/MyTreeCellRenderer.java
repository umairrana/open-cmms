package com.matrix.focus.util.tree;

import com.matrix.focus.util.ImageLibrary;
import java.awt.Component;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class MyTreeCellRenderer extends DefaultTreeCellRenderer {
  
    SingleTree tree;
    
    public MyTreeCellRenderer(SingleTree tree){
        this.tree=tree;
        setFont(new Font("Tahoma",Font.PLAIN,11));   
    }

    public Component getTreeCellRendererComponent(JTree tree,Object value,boolean sel,boolean expanded,boolean leaf,int row,boolean hasFocus) {
        setText(value.toString());
        this.hasFocus = hasFocus;
        
        if(sel)
            setForeground(getTextSelectionColor());
        else
            setForeground(getTextNonSelectionColor());
            
        if (!tree.isEnabled()){
                setEnabled(false);
        }
        else{
            setEnabled(true);
        }
        
        selected = sel;
        return setIcons(value,sel);
    }

    protected Component setIcons(Object value,final boolean sel) {
        MyTreeNode node =(MyTreeNode)value;
        if(tree.getType()==SingleTree.TREE_PLANTS){
            if (node.level==0){
                setIcon(new ImageIcon(ImageLibrary.TREE_ADMIN));   
            }
            else if(node.level==1){
                setIcon(new ImageIcon(ImageLibrary.TREE_COMPANY));
            }
            else if(node.level==2){
                setIcon(new ImageIcon(ImageLibrary.TREE_DEPARTMENT));
            }
            else if(node.level==3){
                setIcon(new ImageIcon(ImageLibrary.TREE_DIVISION));
            }     
        }
        else if(tree.getType()==SingleTree.TREE_CATEGORIES){
            if (node.level==0){
                setIcon(new ImageIcon(ImageLibrary.TREE_CATEGORIES));
            }
            else if(node.level==1){
                setIcon(new ImageIcon(ImageLibrary.TREE_CATEGORY));
            }
            else if(node.level==2){
                setIcon(new ImageIcon(ImageLibrary.TREE_MODEL));
            }
            else if(node.level==3){
                setIcon(new ImageIcon(ImageLibrary.TREE_ASSET));
            }
        }
        else if(tree.getType()==SingleTree.TREE_LOCATIONS){
            if (node.level==0){
                setIcon(new ImageIcon(ImageLibrary.TREE_LOCATION));
            }
            else if(node.level==1){
                setIcon(new ImageIcon(ImageLibrary.TREE_PLANT));
            }
            else if(node.level==2){
                setIcon(new ImageIcon(ImageLibrary.TREE_BUILDING));
            }
            else if(node.level==3){
                setIcon(new ImageIcon(ImageLibrary.TREE_FLOOR));
            }
            else if(node.level==4) {
                setIcon(new ImageIcon(ImageLibrary.TREE_BLOCK));
            }
        }
        else{
            if (node.level==0){
                setIcon(new ImageIcon(ImageLibrary.TREE_CATEGORIES));
            }
            else if(node.level==1){
                setIcon(new ImageIcon(ImageLibrary.TREE_CATEGORY));
            }
            else if(node.level==2){
                setIcon(new ImageIcon(ImageLibrary.TREE_MODEL));
            }
            else if(node.level==3){
                setIcon(new ImageIcon(ImageLibrary.TREE_ASSET));
            }
        }
        setDisabledIcon(getIcon());
        return this;
    } 
}
