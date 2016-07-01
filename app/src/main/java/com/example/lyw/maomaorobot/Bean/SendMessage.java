package com.example.lyw.maomaorobot.Bean;

import com.example.lyw.maomaorobot.Profile;
import com.google.gson.JsonObject;

import java.util.Random;

/**
 * Created by LYW on 2016/5/26.
 */
public class SendMessage extends TulingMessage {

    private String key = Profile.API_KRY;
    private String info;
    private String loc; //位置信息
    private String userId;

    private long requestID = new Random().nextInt(1<<20);

    private boolean isHadResponse; //记录消息是否得到应答

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

    public long getRequestID() {
        return requestID;
    }

    public boolean isHadResponse() {
        return isHadResponse;
    }

    public void setHadResponse(boolean hadResponse) {
        isHadResponse = hadResponse;
    }

    public String getJsonString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key",key);
        jsonObject.addProperty("info",info);
        jsonObject.addProperty("loc",loc);
        jsonObject.addProperty("userId",userId);
        return jsonObject.toString();
    }


}
