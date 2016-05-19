package com.plenry.sparkline.data;

import android.provider.Settings;

/**
 * Created by Xiaoyu on 5/17/16.
 */
public class Controller {
    private String getDeviceToken() {
        return Settings.Secure.ANDROID_ID;
    }

//    private User newUser() {
//        //tbd
//        return new User();
//    }

//    private User changePhoto() {
//        tbd
//    }

    private void changeName(String name) {

    }

    private void createRoom(String topic, String passcode) {

    }

    private void sendMessage(String content, String room) {
        //need to use user name,photo,color from sharedpreference.
    }

//    private List<Room> getRooms(int start, int end) {
//
//        return List<Room>;
//    }

//    private List<Message> getMessage(String room) {
//
//    }
}
