package com.example.lyw.maomaorobot.Bean;

/**
 * Project: MaoMaoRobot.
 * Data: 2016/7/1.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class TulingMessage {

    /*消息状态*/
    public static final int TYPE_MESSAGE_SEND = 1001; //发送的消息
    public static final int TYPE_MESSAGE_RECEIVE = 1002; //接受的消息

    private int mMessageType; //标识消息类型

    private long messageData = System.currentTimeMillis();//用于记录消息产生时间

    protected boolean isStoreDB; //是否存入数据库


    public boolean isStoreDB() {
        return isStoreDB;
    }

    public void setStoreDB(boolean storeDB) {
        isStoreDB = storeDB;
    }

    public int getMessageType() {
        return mMessageType;
    }

    public void setMessageType(int messageType) {
        mMessageType = messageType;
    }


    public long getMessageData() {
        return messageData;
    }

    public void setMessageData(long messageData) {
        this.messageData = messageData;
    }
}
