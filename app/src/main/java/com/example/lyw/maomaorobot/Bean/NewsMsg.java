package com.example.lyw.maomaorobot.Bean;

import java.net.URL;
import java.util.Date;

/**
 * Created by LYW on 2016/6/1.
 */
public class NewsMsg extends ReturnMessage {
    private String  url;

    public NewsMsg() {
    }

    public NewsMsg(int mCode, String mMsg, String mDate, String url) {
        super(mCode, mMsg, mDate);
        this.url = url;

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
