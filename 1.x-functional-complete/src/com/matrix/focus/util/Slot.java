package com.matrix.focus.util;

import com.matrix.focus.mdi.MDI;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class Slot extends JLabel{
    public String start_time;
    public String end_time;
    public String work_order;
    public String skill_category;
    public String slot_id;
    public Rectangle bounds;
    private static JPopupMenu popupMenu = new JPopupMenu();
    public JMenuItem menuItemWorkOrder = new JMenuItem();
    private MDI mdi;
    private JDialog dlg;
    private Cursor hnd,dflt;
    
    public Slot(){
        this.setOpaque(true);
        this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        hnd = new Cursor(Cursor.HAND_CURSOR);
        dflt = new Cursor(Cursor.DEFAULT_CURSOR);
        
        this.addMouseListener(
            new MouseAdapter(){
                public void mouseClicked(MouseEvent me){
                    if(me.getButton()==1){
                        popupMenu.removeAll();
                        String info = "<html><table border='0' width='380'>" + 
                                        "<tr>" + 
                                            "<td width='18%' bgcolor='#B1C3D9'><b>Job</b></td>" + 
                                            "<td  bgcolor='#FFFFFF'>"+work_order+"</td>" + 
                                        "</tr>" + 
                                        "<tr>" + 
                                            "<td width='18%' bgcolor='#B1C3D9'><b>Start Time</b></td>" + 
                                            "<td bgcolor='#FFFFFF'>"+start_time+"</td>" + 
                                        "</tr>" + 
                                        "<tr>" + 
                                            "<td width='18%' bgcolor='#B1C3D9'><b>End Time</b></td>" + 
                                            "<td bgcolor='#FFFFFF'>"+end_time+"</td>" + 
                                        "</tr>" + 
                                        "<tr>" + 
                                            "<td width='18%' bgcolor='#B1C3D9'><b>Skill Used</b></td>" + 
                                            "<td bgcolor='#FFFFFF'>"+skill_category+"</td>" + 
                                        "</tr>" + 
                                        "<tr>" + 
                                            "<td width='18%' bgcolor='#B1C3D9'><b>Time Slot</b></td>" + 
                                            "<td bgcolor='#FFFFFF'>"+slot_id+"</td>" + 
                                        "</tr>" + 
                                    "</table></html>";
                        popupMenu.add(info);
                        menuItemWorkOrder.setIcon(new ImageIcon((work_order.substring(0,2).equals("BD")?ImageLibrary.MENU_UNPLANNED_WORKORDER:ImageLibrary.MENU_PREVENTIVE_WORKORDER))); 
                        menuItemWorkOrder.setText("Go to Job");
                        popupMenu.add(menuItemWorkOrder);
                        popupMenu.show(Slot.this,me.getPoint().x, me.getPoint().y);
                    }
                }
                public void mouseEntered(MouseEvent e) {
                    mdi.setCursor(hnd);
                    if(dlg!=null)dlg.setCursor(hnd);
                }
                public void mouseExited(MouseEvent e) {
                    mdi.setCursor(dflt);
                    if(dlg!=null)dlg.setCursor(dflt);
                }
            }
        );
        
        menuItemWorkOrder.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    //if(work_order.substring(0,2).equals("PM")){
                        mdi.menuItemGUIPreventiveMaintenanceWorkOrder.doClick();
                        mdi.guiPreventiveMaintenanceWorkOrder.setWorkOrder(work_order);
                    //}
                    //else{
                    //    mdi.menuItemGUIUnplannedMaintenanceWorkOrder.doClick();
                    //    mdi.guiUnplannedMaintenanceWorkOrder.setWorkOrder(work_order);
                    //}
                }
            }
        );
    }
    
    public void setDialog(JDialog owner){
        this.dlg = owner;
    }
    
    public void setMDI(MDI owner){
        this.mdi = owner;
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(new ImageIcon(ImageLibrary.UTIL_SLOT).getImage(),0,0,this.getWidth(),this.getHeight(),this);    
    }
}
