package com.example.lyw.maomaorobot.Bean;

import java.util.Date;

/**
 * Created by LYW on 2016/5/28.
 */
public class ReturnMsg {
    private double code;
    private String meg;
    private Type type;
    private Date date;

    public ReturnMsg() {
    }

    public ReturnMsg(Type type, Date date, String meg) {

        this.type = Type.INCOMING;
        this.date = date;
        this.meg = meg;
    }

    public enum Type{
        INCOMING,OUTCOMING;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Type getType() {

        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public double getCode() {return code;
    }

    public void setCode(double code) {
        this.code = code;
    }

    public String getMeg() {
        return meg;
    }

    public void setMeg(String meg) {
        this.meg = meg;
    }
}
