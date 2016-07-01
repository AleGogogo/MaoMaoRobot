package com.example.lyw.maomaorobot.Bean;

import com.example.lyw.maomaorobot.Profile;

/**
 * Created by LYW on 2016/5/26.
 */
public class SendMessage extends TulingMessage {

    private String key = Profile.API_KRY;
    private String info;
    private String loc; //位置信息
    private String userId;

    public SendMessage(String message) {
        this.info = message;
        this.setMessageType(TYPE_MESSAGE_SEND);
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

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
