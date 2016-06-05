package com.example.lyw.maomaorobot.Bean;

import java.net.URL;
import java.util.Date;

/**
 * Created by LYW on 2016/6/1.
 */
public class LinkMsg extends ReturnMessage {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LinkMsg() {
    }

    public LinkMsg(int mCode, String mMsg, String mDate, String url) {
        super(mCode, mMsg, mDate);
        this.url = url;

    }
}
