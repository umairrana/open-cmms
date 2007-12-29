package com.matrix.focus.mdi;

import com.l2fprod.common.swing.JTaskPane;
import com.l2fprod.common.swing.JTaskPaneGroup;
import com.matrix.focus.mdi.messageBar.MessageBar;
import com.matrix.focus.mdi.tabbedPane.CloseTabbedPane;
import com.matrix.focus.util.DBConnectionPool;
import com.matrix.focus.util.ImageLibrary;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.SwingUtilities;

public class CommonTaskPane extends JPanel{
    private MDI mdi;
    private Connection connection;
    private JTaskPane taskPane;
    private JTaskPaneGroup groupTasks;
    private JTaskPaneGroup groupMsg;
    private ImageIcon iconWorkorder;
    private ImageIcon iconScheduled;
    private ImageIcon iconMsg;
    private MessageBar messageBar;
    private CloseTabbedPane tabbedPane;
    private DBConnectionPool pool;
    
    public CommonTaskPane(MDI p_mdi, DBConnectionPool pool,CloseTabbedPane tabbedPane,MessageBar msgBar){
        mdi = p_mdi;
        this.tabbedPane = tabbedPane;
        messageBar = msgBar;
        this.pool = pool;
        connection = pool.getConnection();
        setLayout(new BorderLayout());
        
        iconWorkorder = new ImageIcon(ImageLibrary.COMMONTASK_WORKORDER);
        iconScheduled = new ImageIcon(ImageLibrary.COMMONTASK_SCHEDULED);
        iconMsg = new ImageIcon(ImageLibrary.COMMONTASK_MESSAGE);
        
        taskPane = new JTaskPane();
        add("Center", new JScrollPane(taskPane));
        taskPane.setBackground(Color.decode("#002EB8"));
         
        groupTasks = new JTaskPaneGroup();
        groupTasks.setTitle("Maintenance Tasks");
        taskPane.add(groupTasks);
        groupTasks.setExpanded(true);
        
        groupTasks.add(new PendingWorkorderItem(getNoOfPendingJobs()));
        groupTasks.add(new ScheduledThisWeekItem());
        groupTasks.add(new ScheduledNextWeekItem());
        groupTasks.add(new ScheduledNextTwoWeeksItem());
        groupTasks.add(new ScheduledNextThreeWeeksItem());
        groupTasks.add(new ScheduledNextMonthItem());
        
        groupMsg = new JTaskPaneGroup();
        groupMsg.setTitle("Messages");
        taskPane.add(groupMsg);
        groupMsg.setExpanded(false);
        groupMsg.addMouseListener(
            new MouseAdapter(){
                public void mouseClicked(MouseEvent e){
                    if(e.getClickCount()==2){
                        MDIMessageDialog dlg = new MDIMessageDialog(mdi,messageBar,connection);
                        dlg.setVisible(true);
                        refreshMessages();
                    }
                }
            }
        );

        refreshMessages();
    }

    public CommonTaskPane() {
        
    }
    
    private int getNoOfPendingJobs(){
        String sqlWorkorder = "SELECT COUNT(pm_work_order_id) FROM preventive_maintenance_work_order WHERE Done_Date ='0000-00-00 00:00:00'";
        try{
           ResultSet rec = connection.createStatement().executeQuery(sqlWorkorder); 
           rec.first();
           return rec.getInt(1);
        } catch(Exception ex){
            ex.printStackTrace();
            return 0;
        }
    }
    
    private void refreshMessages(){
        boolean have = false;
        String sqlMessages = "SELECT * FROM mdi_message WHERE deleted ='false'";
        try{
            ResultSet rec = connection.createStatement().executeQuery(sqlMessages);
            removeAllFromMessage();
            while(rec.next()){
                groupMsg.add(
                    new MessageItem(
                        new MDIMessage(
                            rec.getString("id"),
                            rec.getString("message"),
                            rec.getString("username"),
                            rec.getString("created_time")
                        )
                    )
                );
                have = true;
            }
            if(have){
                groupMsg.setExpanded(true);
                SwingUtilities.updateComponentTreeUI(this);
            }
        }
        catch(Exception er){
            er.printStackTrace();
        }
    }
    
    private void addToMessage(MDIMessage message){
        groupMsg.add(new MessageItem(message));
    }
    
    private void removeAllFromMessage(){
        groupMsg.removeAll();
    }
    
    class MessageItem extends AbstractAction{
        private MDIMessage message;
        public MessageItem(MDIMessage message){
            super(message.message,iconMsg);
            this.message = message;
        }
        
        public void actionPerformed(ActionEvent e) {
            MDIMessageDialog dlg = new MDIMessageDialog(mdi,messageBar,message,connection);
            dlg.setVisible(true);
            refreshMessages();
        }
    } 
    
    class PendingWorkorderItem extends AbstractAction{

        public PendingWorkorderItem(int count){
            super("Pending Jobs ("+count+")",iconWorkorder);
        }
        
        public void actionPerformed(ActionEvent e){
            mdi.menuItemGUIPendingWorkOrders.doClick();
        }
    }
    
    class ScheduledThisWeekItem extends AbstractAction{
        public ScheduledThisWeekItem(){
            super("This week",iconScheduled);
        }
        
        public void actionPerformed(ActionEvent e) {
            mdi.menuItemOncomingVisits0.doClick();
            mdi.guiOncomingVisits0.setWeeks(1);
        }
    }
    
    class ScheduledNextWeekItem extends AbstractAction{
        public ScheduledNextWeekItem(){
            super("Next week",iconScheduled);
        }
        
        public void actionPerformed(ActionEvent e) {
            mdi.menuItemOncomingVisits1.doClick();
            mdi.guiOncomingVisits1.setWeeks(2);
        }
    }
    
    class ScheduledNextTwoWeeksItem extends AbstractAction{
        public ScheduledNextTwoWeeksItem(){
            super("Next 2 weeks",iconScheduled);
        }
        
        public void actionPerformed(ActionEvent e) {
            mdi.menuItemOncomingVisits2.doClick();
            mdi.guiOncomingVisits2.setWeeks(3);
        }
    }
    
    class ScheduledNextThreeWeeksItem extends AbstractAction{
        public ScheduledNextThreeWeeksItem(){
            super("Next 3 weeks",iconScheduled);
        }
        
        public void actionPerformed(ActionEvent e) {
            mdi.menuItemOncomingVisits3.doClick();
            mdi.guiOncomingVisits3.setWeeks(4);
        }
    }
    
    class ScheduledNextMonthItem extends AbstractAction{
        public ScheduledNextMonthItem(){
            super("Next month",iconScheduled);
        }
        
        public void actionPerformed(ActionEvent e) {
            mdi.menuItemOncomingVisits4.doClick();
            mdi.guiOncomingVisits4.setWeeks(5);
        }
    }
}
