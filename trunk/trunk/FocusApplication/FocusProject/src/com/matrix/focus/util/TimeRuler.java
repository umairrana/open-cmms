package com.matrix.focus.util;

import com.matrix.focus.mdi.MDI;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

public class TimeRuler extends JPanel{

    private Slot slots[];
    private String SPLIT_STRING = ":";
    private int LEFT = 20;
    private int TOP = 40;
    private int FACTOR = 32;
    private final int MINITES_PER_HOURS = 60;
    private final int HOURS_PER_DAY = 24;
    private double START_WORK = -1;
    private double END_WORK = -1;
    private Color LETTER_COLOR = Color.BLACK;
    private Color RULER_COLOR = Color.BLACK;
    private MDI mdi;
    private String employee_id = "";
    private String employee_name = "";
        
    public TimeRuler(MDI mdi){
        this.mdi = mdi;
        try {           
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setAssignedInfo(String emp_id, String name, String date,Connection connection){
        employee_id = emp_id;
        employee_name = name;
        String info[] = new LabourAvailability(connection).getAvailabilityOf(emp_id,date);
        setAssignedInfo(getAvailabilityInfo(employee_id,date,connection),info[0],info[1],null);
    }
    
    public void setAssignedInfo(String emp_id, String name, String date,Connection connection,JDialog dlg){
        employee_id = emp_id;
        employee_name = name;
        String info[] = new LabourAvailability(connection).getAvailabilityOf(emp_id,date);
        setAssignedInfo(getAvailabilityInfo(employee_id,date,connection),info[0],info[1],dlg);
    }
     
    private void setAssignedInfo(Slot p_slots[], String start, String end,JDialog dlg){
        this.slots = null;
        this.slots = p_slots;
        double x1=0,x2=0;
        int y1=0,y2=0;
        String tmp[];
        y1 = TOP-4;
        y2 = 14;
        int count = (slots==null?0:slots.length);
        try{
            for(int i=0;i<count;i++){
                tmp = slots[i].start_time.split(SPLIT_STRING);
                x1 = ((Double.parseDouble(tmp[0]) + Double.parseDouble(tmp[1])/MINITES_PER_HOURS) * FACTOR) + LEFT;
                tmp = slots[i].end_time.split(SPLIT_STRING);
                x2 = ((Double.parseDouble(tmp[0]) + Double.parseDouble(tmp[1])/MINITES_PER_HOURS) * FACTOR) + LEFT;
                slots[i].bounds = new Rectangle((int)x1,y1,(int)(x2-x1),y2);
                slots[i].setMDI(mdi);
                if(dlg!=null){
                    slots[i].setDialog(dlg);
                }
            }
            
            /**Work start and end times for the day*/
            String times[] = start.split(SPLIT_STRING);
            START_WORK = ((Double.parseDouble(times[0]) + Double.parseDouble(times[1])/MINITES_PER_HOURS) * FACTOR) + LEFT;
            times = end.split(SPLIT_STRING);
            END_WORK = ((Double.parseDouble(times[0]) + Double.parseDouble(times[1])/MINITES_PER_HOURS) * FACTOR) + LEFT;
            
            /**Remove all existing Slots*/
            removeAll();
            /**Refresh drawing*/
            repaint();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**Drawings*/
    public void paintComponent(Graphics g){
        super.paintComponent(g);
		
	//Base line
	g.setColor(RULER_COLOR);
	g.drawLine(LEFT,TOP+10,FACTOR*(HOURS_PER_DAY)+LEFT,TOP+10);
		
	//Points
	for(int i=0;i<=HOURS_PER_DAY;i++){
		g.drawLine(
			((i*FACTOR)+LEFT),
			TOP+10,
			((i*FACTOR)+LEFT),
			TOP+20);
		if(i>0){
			g.drawLine(
				((i*FACTOR)-FACTOR/2)+LEFT,
				TOP+10,
				((i*FACTOR)-FACTOR/2)+LEFT,
				TOP+17
			);
			g.drawLine(
				((i*FACTOR)-FACTOR*3/4)+LEFT,
				TOP+10,
				((i*FACTOR)-FACTOR*3/4)+LEFT,
				TOP+13
			);
        		g.drawLine(
				((i*FACTOR)-FACTOR/4)+LEFT,
				TOP+10,
				((i*FACTOR)-FACTOR/4)+LEFT,
				TOP+13
			);
		}
        }
	
	//Digits
	g.setColor(LETTER_COLOR);
	for(int i=0;i<=HOURS_PER_DAY;i++){
        	g.drawString(i+"",(i*FACTOR)+LEFT-4 ,TOP+35);
        }

        //Definition
        g.drawImage(new ImageIcon(ImageLibrary.UTIL_SLOT).getImage(),LEFT+600,TOP-30,10,10,this);
        g.setColor(LETTER_COLOR);
        g.drawRect(LEFT+600,TOP-30,10,10);
        g.drawString("Assigned",LEFT+620,TOP-21);

        g.drawImage(new ImageIcon(ImageLibrary.UTIL_AVAILABLE).getImage(),LEFT+700,TOP-30,10,10,this);
        g.setColor(LETTER_COLOR);
        g.drawRect(LEFT+700,TOP-30,10,10);
        g.drawString("Available",LEFT+720,TOP-21);

	//Available
        g.drawImage(new ImageIcon(ImageLibrary.UTIL_AVAILABLE).getImage(),(int)START_WORK,TOP-10,(int)(END_WORK-START_WORK),20,this);
        
        /**Employee information*/
        if(!employee_id.equals("")){
            g.drawImage(new ImageIcon(ImageLibrary.UTIL_WORKER).getImage(),LEFT-15,TOP-37,40,40,this);    
            g.setFont(new Font("System",0,11));
            g.drawString(employee_name + " - " + employee_id,LEFT+15,TOP-20);
        }
	/**Draw slots*/        
        if(slots!=null){
            for(int i=0;i<slots.length;i++){
                slots[i].setBounds(slots[i].bounds);
                add(slots[i]);
            }
        }
    }
    
    private Slot[] getAvailabilityInfo(String emp_id, String date,Connection connection){
        try{
            String sql = "SELECT " + 
                            "DISTINCT Employee_ID," + 
                            "get_labour_availabilty('" + emp_id +  "','" + date + "') AS periods " + 
                         "FROM " + 
                            "labour_utilisation";
            ResultSet rec = connection.createStatement().executeQuery(sql);
            rec.next();
            String data = rec.getString("periods");
            return getSlots(data);
        }
        catch(Exception er){
            //er.printStackTrace();
            return null;
        }
    }
    
    private Slot[] getSlots(String data){
        /**|09:00:00_11:00:00_PM-060817-1_Multi Diseplinary - Electrical_1|*/
        String str_slots[] = data.split("#");        
        Slot[] tmp_slots = new Slot[str_slots.length];
        String tmp[];
        for(int i=0;i<str_slots.length;i++){
            tmp = str_slots[i].split("_");
            tmp_slots[i] = new Slot();
            tmp_slots[i].start_time = tmp[0];
            tmp_slots[i].end_time = tmp[1];
            tmp_slots[i].work_order = tmp[2];
            tmp_slots[i].skill_category = tmp[3];
            tmp_slots[i].slot_id = tmp[4];
        }
        return tmp_slots;
    }

    private void jbInit() throws Exception {
        this.setSize(810,90);
        this.setLayout(null);
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    }
}
