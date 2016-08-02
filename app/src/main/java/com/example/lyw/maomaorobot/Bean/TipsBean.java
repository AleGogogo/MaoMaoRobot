package com.example.lyw.maomaorobot.Bean;

import java.io.Serializable;

/**
 * Created by rain on 2016/7/22.
 */
public class TipsBean implements Serializable {
    public int mId;
    public int mImageId;
    public String mTipsInfo;


    public TipsBean(int mImageId, String mTipsInfo) {
        this.mImageId = mImageId;
        this.mTipsInfo = mTipsInfo;
    }

    public TipsBean(){

    }
    public int getmImageId() {
        return mImageId;
    }

    public void setmImageId(int mImageId) {
        this.mImageId = mImageId;
    }

    public String getmTipsInfo() {
        return mTipsInfo;
    }

    public void setmTipsInfo(String mTipsInfo) {
        this.mTipsInfo = mTipsInfo;
    }
}
