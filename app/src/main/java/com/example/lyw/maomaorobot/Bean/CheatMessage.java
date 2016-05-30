package com.example.lyw.maomaorobot.Bean;

import java.util.Date;

/**
 * Created by LYW on 2016/5/26.
 */
public class CheatMessage {
    private String name ;
    private String msg;
    private Date date;
    private Type type;
   // private int code;

    public CheatMessage(String msg, Type type, Date date) {

        this.msg = msg;
        this.type = Type.OUTCOMING;
        this.date = date;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type{
        INCOMING,OUTCOMING
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
