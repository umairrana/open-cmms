package com.matrix.focus.util.tree;


import com.matrix.focus.util.ImageLibrary;

//import com.matrix.focus.util.Log;

import java.awt.event.MouseAdapter;

import java.awt.event.MouseEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.ImageIcon;

import javax.swing.JTree;

import javax.swing.event.TreeExpansionEvent;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;

import javax.swing.tree.TreeSelectionModel;

public class SingleTree extends JTree //implements TreeWillExpandListener
{
  
  private Connection conn;//=new DatabaseConnection().getConnection();
 // private String [] sqlQuery;
 // private ImageIcon [] icon;
  private String treeType;
  
  public final static String TREE_PLANTS="Plants";
  public final static String TREE_LOCATIONS="Locations";
  public final static String TREE_CATEGORIES="Categories";
  public final static String TREE_ASSET_CATEGORIES="Asset_Categories";
  public final static String TREE_CATEGORIES_3="Categories_3";
  
 
  
    private String locationQuery=" (SELECT  company.Comp_ID as Comp_ID, department.Dept_ID as Dept_ID,division.Division_ID as Division_ID,asset.Asset_ID as Asset_ID,location.Location_ID as Location_ID,location.Plant_Location as Plant_Location,location.Building as Building, location.Floor as Floor,location.Block as Block" +
                                " FROM company " +
                                "INNER JOIN department ON (company.Comp_ID = department.Comp_ID) "+
                                "INNER JOIN division ON (department.Comp_ID = division.Comp_ID) "+
                                "AND (department.Dept_ID = division.Dept_ID) "+
                                "INNER JOIN asset ON (division.Comp_ID = asset.Comp_ID) "+
                                "AND (division.Dept_ID = asset.Dept_ID) "+
                                "AND (division.Division_ID = asset.Division_ID) "+
                                "INNER JOIN asset_model ON (asset.Model_ID = asset_model.Model_ID) "+
                                "INNER JOIN asset_category ON (asset_model.Category_ID = asset_category.Category_ID) "+
                                "INNER JOIN location ON (location.Location_ID = asset.Location_ID)) a";
    
    private String appendQry="'";
    private String appendWhereQry="";
    
      public String getType(){
          return treeType;
      }
  public SingleTree(String rootName, Connection con) throws Exception//,String query[],ImageIcon icn[])
  {
      super(new MyTreeNode(rootName,null));
      if(!(rootName==TREE_PLANTS || rootName==TREE_LOCATIONS || rootName==TREE_CATEGORIES || rootName==TREE_ASSET_CATEGORIES || rootName==TREE_CATEGORIES_3))
      {
        throw new Exception("Invalid Tree Name");
      }
      conn=con;
      treeType=rootName;
      
      init();
       
      addMouseListener(new MouseAdapter(){
        public void mouseClicked(MouseEvent e){
            if(getPathForLocation(e.getX(),e.getY())==null){
                //SingleTree.this.clearSelection();
                 SingleTree.this.setSelectionRow(0);
            }
        }
        
      });
      
  }

  
    private void init()
  {     
            
        BasicTreeUI treeUI=((BasicTreeUI)this.getUI()); 
        treeUI.setCollapsedIcon(new ImageIcon(ImageLibrary.TREE_PLUS));
        treeUI.setExpandedIcon(new ImageIcon(ImageLibrary.TREE_MINUS));
        this.setUI(treeUI);
        this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        //this.setSelectionRow(0);
        
       
       addNodes((MyTreeNode)(getModel()).getRoot(),0);
       expandRow(0);
       
       
       MyTreeCellRenderer r=new MyTreeCellRenderer(this);
        setCellRenderer(r);
      
        addTreeSelectionListener(new TreeSelectionListener(){
            public void valueChanged(TreeSelectionEvent e)
            {
            try{
             
              if (!isExpanded(getSelectionPath())){
             // TreeWillExpandListener treeExpandListeners[]=getTreeWillExpandListeners();
              //treeExpandListeners[0].treeWillExpand(new TreeExpansionEvent(e.getPath().getLastPathComponent(),e.getPath()));
              }
           
            }
            catch(Exception ee){
            ee.printStackTrace();
             //Log.log(ee);
            }
            }
       });
     
        
        addTreeWillExpandListener(new TreeWillExpandListener(){
        
        
            public void treeWillExpand(TreeExpansionEvent e){
            
             
                addNodes((MyTreeNode)e.getPath().getLastPathComponent(),0);           
                ((DefaultTreeModel)getModel()).reload((MyTreeNode)e.getPath().getLastPathComponent());
              
                
            }
            
            public void treeWillCollapse(TreeExpansionEvent e) throws ExpandVetoException{
              if(getRowForPath(e.getPath())==0){
                    throw new ExpandVetoException(e, "node can't be collapsed");
                   // treeWillExpand(e);
                }
            }
       });
              
      
      
 }
          

    

   public void addNodes(MyTreeNode node,int depth)
  {
  
       //System.out.println(((TreePath)getSelectionPath()).getLastPathComponent().toString());
       node.removeAllChildren();
       Object ob[]=node.getPath();//getSelectionPath().getPath();
       String query =getQuery(ob); 
       //System.out.println(query);/**QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ*/
       ResultSet resultSet;  
       Vector v=new Vector();
       try{
       resultSet = conn.createStatement().executeQuery(query);
       resultSet.beforeFirst();
         while(resultSet.next()){
                v.add(resultSet.getString(1));  
                
         }
       }
      catch(Exception e){
            //results are exmpty
            e.toString();
            //e.printStackTrace();
      }
      
      for(int i=0;i<v.size();i++){
    
       MyTreeNode n= new MyTreeNode((String)v.get(i),node);
       if(depth==1)break;
       if (depth==0){
           addNodes(n,depth+1);
       }
      }
     
  }   
  
  public void setlocationQuery(String appendQry)
    {
        this.appendQry="";
        this.appendWhereQry="";
        
        if (appendQry.trim()!="")
        {
          this.appendQry="' and "+appendQry;
          this.appendWhereQry=" where "+appendQry;
        }
        else
          this.appendQry="'";
          
          
    }
    
    public void populateTree()
    {  
        this.removeAll();
        ((DefaultTreeModel)this.getModel()).reload();
    
        this.addNodes((MyTreeNode)(getModel()).getRoot(),0);
        this.expandRow(0);
    }
    
    public void hideTree()
    {  
        this.removeAll();
        ((DefaultTreeModel)this.getModel()).reload();
    }
    
  private String getQuery(Object ob[])
  {
    
            if (treeType==TREE_PLANTS){
                  if(ob.length==1)
                    return "SELECT Comp_ID FROM Company"; 
                  else if(ob.length==2)
                    return "SELECT department.Dept_ID FROM Company INNER JOIN department ON (Company.Comp_ID = department.Comp_ID) WHERE (Company.Comp_ID ='"+ob[1].toString()+"')";          
                  else if(ob.length==3)
                    return "SELECT division.Division_ID FROM Company INNER JOIN department ON (Company.Comp_ID = department.Comp_ID) INNER JOIN division ON (department.Comp_ID = division.Comp_ID) AND (department.Dept_ID = division.Dept_ID) WHERE (department.Dept_ID='" +ob[2].toString()+"')";
                  else
                    return "";
                  //break;
            }
            else if(treeType==TREE_LOCATIONS){
                  if(ob.length==1)
                    return "SELECT distinct a.Plant_Location FROM "+locationQuery+appendWhereQry; 
                  else if(ob.length==2)
                    return "SELECT distinct a.Building FROM "+locationQuery+" WHERE Plant_Location='"+ob[1].toString()+appendQry;         
                  else if(ob.length==3)
                    return "SELECT distinct a.Floor FROM "+locationQuery+" WHERE Plant_Location='"+ob[1].toString()+"' && Building='" +ob[2].toString()+appendQry;          
                  else if (ob.length==4)
                    return "SELECT distinct a.Block FROM "+locationQuery+" WHERE Plant_Location='"+ob[1].toString()+"' && Building='" +ob[2].toString()+"' && Floor='" +ob[3].toString()+appendQry;          
                  else
                    return "";
                  //break;
            }
            else if(treeType==TREE_CATEGORIES_3){
                  if(ob.length==1)
                    return "SELECT Category_Id FROM asset_category"; 
                  else if(ob.length==2)
                    return "SELECT   distinct Sub_Category FROM  `asset_model` where Category_ID='" +ob[1].toString()+"'";
                  else if(ob.length==3)
                    return "SELECT  Model_ID FROM  `asset_model` where Sub_Category='" +ob[2].toString()+"' && Category_ID='" +ob[1].toString()+"'";
                  else
                    return "";
            }
            else if(treeType==TREE_CATEGORIES){
                if(ob.length==1)
                  return "SELECT Category_Id FROM asset_category"; 
                //else if(ob.length==2)
                //  return "SELECT   distinct Sub_Category FROM  `asset_model` where Category_ID='" +ob[1].toString()+"'";
                else if(ob.length==2)
                  return "SELECT  Model_ID FROM  `asset_model` where  Category_ID='" +ob[1].toString()+"'";
                else
                  return "";
            }
            else if(treeType==TREE_ASSET_CATEGORIES)
            {
                  if(ob.length==1)
                    return "SELECT Category_Id FROM asset_category"; 
                //  else if(ob.length==2)
                //    return "SELECT   distinct Sub_Category FROM  `asset_model` where Category_ID='" +ob[1].toString()+"'";
                  else if(ob.length==2)
                    return "SELECT  Model_ID FROM  `asset_model` where  Category_ID='" +ob[1].toString()+"'";
                  else if(ob.length==3) 
                    return "SELECT Asset_ID FROM Asset where Model_ID='" +ob[2].toString()+"'";
                  else 
                    return "";
            }
            else
            return "";            
      
  }

    
}


