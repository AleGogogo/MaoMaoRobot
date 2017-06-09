package com.example.lyw.maomaorobot;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.litepal.LitePal;

/**
 * Created by LYW on 2016/6/13.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(configuration);
        LitePal.initialize(this);
    }
}
