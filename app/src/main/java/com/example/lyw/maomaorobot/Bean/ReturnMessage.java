package com.example.lyw.maomaorobot.Bean;

import java.util.Date;

/**
 * Created by LYW on 2016/6/1.
 */
public  class ReturnMessage {
    public int mCode;
    public String mMsg;
    public  Type mType;
    public String mDate;

    public ReturnMessage() {
    }

    public ReturnMessage(int mCode, String mMsg, String mDate) {
        this.mCode = mCode;
        this.mMsg = mMsg;
        this.mType = Type.INCOMING;
        this.mDate = mDate;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public double getmCode() {
        return mCode;
    }

    public void setmCode(int mcode) {
        this.mCode = mcode;
    }

    public String getmMsg() {
        return mMsg;
    }

    public void setmMsg(String msg) {
        this.mMsg = msg;
    }

    public Type getType() {
        return mType;
    }

    public void setType(Type type) {
        this.mType = type;
    }

    public enum Type{
      INCOMING,OUTCOMING;
    }


}
