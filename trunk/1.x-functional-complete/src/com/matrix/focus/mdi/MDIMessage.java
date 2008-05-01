package com.matrix.focus.mdi;

public class MDIMessage{
    public String msg_id;
    public String message;
    public String username;
    public String timestamp;
    
    public MDIMessage(String id, String message, String username, String created_time){
        this.msg_id = id;
        this.message = message;
        this.username = username;
        this.timestamp = created_time;
    }
}
