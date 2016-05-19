package com.plenry.sparkline.bean;

/**
 * Created by Xiaoyu on 5/17/16.
 */
public class User {
    private String name;
    private String photo;
    private String color;

    public User (String name, String photo, String color){
        this.name = name;
        this.photo = photo;
        this.color = color;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setPhoto(String photoUrl){
        this.photo = photoUrl;
    }
    public String getPhoto(){
        return this.photo;
    }
    public void setColor(String color){
        this.color = color;
    }
    public String getColor(){
        return this.color;
    }
}
