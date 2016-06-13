package com.example.lyw.maomaorobot.Bean;

import java.util.Date;
import java.util.List;

/**
 * Created by LYW on 2016/6/7.
 */
public class BaseResponse {

    public static final int RESPONSE_TYPE_TEXT = 100000;
    public static final int RESPONSE_TYPE_LINK = 200000;
    public static final int RESPONSE_TYPE_NEWS = 302000;
    public static final int RESPONSE_TYPE_CAIPU = 308000;

    public int code;

    public String mDate;

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Type mType = Type.INCOMING;

    public enum Type{
        INCOMING,OUTCOMING;
    }

    public Type getmType() {
        return mType;
    }

    public void setmType(Type mType) {
        this.mType = mType;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", mDate='" + mDate + '\'' +
                ", mType=" + mType +
                '}';
    }

}
