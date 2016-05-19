package com.plenry.sparkline.bean;

/**
 * Created by Xiaoyu on 5/17/16.
 */
public class Room {
    private String topic;
    private String time;
    private String passcode;
    private int size;

    public Room(String topic, String time, String passcode) {
        this.size = 0;
        this.topic = topic;
        this.passcode = passcode;
        this.time = time;
    }
    public String getTopic(){
        return this.topic;
    }

    public void setTopic(String topic){
        this.topic = topic;
    }

    public String getTime(){
        return this.time;
    }

    public String getPasscode(){
        return this.passcode;
    }
    public void setPasscode( String passcode){
        this.passcode = passcode;
    }
    public int getSize(){
        return this.size;
    }
    public void setSize(int size){
        this.size = size;
    }

}
