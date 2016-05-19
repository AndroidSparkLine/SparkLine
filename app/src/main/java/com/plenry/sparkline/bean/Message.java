package com.plenry.sparkline.bean;

/**
 * Created by Xiaoyu on 5/17/16.
 */
public class Message {
    private String content;
    private String time;
    private User user;
    public Message(String content, String time, User user){
        this.content = content;
        this.time = time;
        this.user = user;
    }
    public String getContent(){
        return this.content;
    }
    public void setContent(String content){
        this.content = content;
    }
    public String getTime(){
        return this.time;
    }
    public void setTime(String time){
        this.time = time;
    }
    public User getUser(){
        return this.user;
    }
    public void setUser(User user){
        this.user = user;
    }
}
