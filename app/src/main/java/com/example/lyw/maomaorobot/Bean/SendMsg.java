package com.example.lyw.maomaorobot.Bean;

import java.util.Date;

/**
 * Created by LYW on 2016/5/26.
 */
public class SendMsg extends CheatMessage{
    private String key;
    private String info;
    private String userId;

    public SendMsg(){

    }

    public SendMsg(String msg, Type type, String date) {
        super(msg, type, date);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
