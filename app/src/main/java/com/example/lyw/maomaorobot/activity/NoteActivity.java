package com.example.lyw.maomaorobot.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.lyw.maomaorobot.R;

/**
 * Created by bluerain on 17-6-9.
 */

public class Note extends Activity {
    private ListView mLvTips;
    private ImageView mIgvPlus;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        customTitle();
        initViews();
    }

    private void customTitle() {
        getActionBar().setTitle("备忘录");
    }

    private void initViews() {
        mLvTips = (ListView) findViewById(R.id.lv_tips);
        mIgvPlus = (ImageView) findViewById(R.id.igv_plus);
    }


}
