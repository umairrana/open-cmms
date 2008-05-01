package com.matrix.focus.util.tree;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;


public class MyTreeNode extends DefaultMutableTreeNode{
    public int level;
    public MyTreeNode(String nodeName,MyTreeNode parent){
        super((nodeName==SingleTree.TREE_CATEGORIES || nodeName==SingleTree.TREE_ASSET_CATEGORIES || nodeName==SingleTree.TREE_CATEGORIES_3) ? SingleTree.TREE_CATEGORIES:nodeName);
        try{
            this.level=parent.level+1;
            parent.add(this);
        }catch(Exception e){
            e.printStackTrace();
            this.level=0;
        }
    }
    
    public int getChildIndex(String node){
        Enumeration e=children();
        int c=0;
        while(e.hasMoreElements()){
            Object o=e.nextElement();
            if(o.toString().trim().equalsIgnoreCase(node)){
                return c;
            }
            c++;
        }
        return -1;
    }
}