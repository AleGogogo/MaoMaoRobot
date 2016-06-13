package com.example.lyw.maomaorobot.Bean;

/**
 * Created by LYW on 2016/6/1.
 */
public  class ReturnMessage {
    public static final int CONTENT_TYPE_NEWS = 605;
    public static final int CONTENT_TYPE_TEXT = 775;
    public static final int CONTENT_TYPE_LINK = 764;
    public static final int CONTENT_TYPE_OTHER = 774;
    
    
    public int mContentType;
    public int mCode;
    public String mMsg;
    public  Type mType = Type.INCOMING;
    public String mDate;

    public ReturnMessage() {
    }

    public ReturnMessage(int mCode, String mMsg, String mDate) {
        this.mCode = mCode;
        this.mMsg = mMsg;
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

    public int getContentType() {
        return mContentType;
    }

    public void setContentType(int mContentType) {
        this.mContentType = mContentType;
    }

    public enum Type{
      INCOMING,OUTCOMING;
    }


}
