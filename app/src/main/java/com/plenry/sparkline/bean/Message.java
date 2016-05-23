package com.plenry.sparkline.bean;

/**
 * Created by Xiaoyu on 5/17/16.
 */
public class Message {
    private String content;
    private String time;
    private String username;
    private String photo;
    private String color;
    private String room;
    private boolean isComMeg = true;  //to check source of message

    public Message(String content, String time, String username, String photo, String color, String room, boolean isComMsg){
        this.content = content;
        this.time = time;
        this.username = username;
        this.photo = photo;
        this.color = color;
        this.room = room;
        this.isComMeg = isComMsg;
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
    public String getUsername(){
        return this.username;
    }
    public void setUsername(String  username){
        this.username = username;
    }
    public String getPhoto(){
        return this.photo;
    }
    public void setPhoto(User user){
        this.photo = photo;
    }
    public String getColor(){
        return this.color;
    }
    public void setColor(User user){this.color = color;}
    public String getRoom(){
        return this.room;
    }
    public void setRoom(User user){
        this.room = room;
    }
    public boolean getMsgType() {
        return isComMeg;
    }
    public void setMsgType(boolean isComMsg) {
        isComMeg = isComMsg;
    }
}
