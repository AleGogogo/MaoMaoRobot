package com.example.lyw.maomaorobot.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lyw.maomaorobot.adapter.TipsListViewAdapter;
import com.example.lyw.maomaorobot.Bean.TipsBean;
import com.example.lyw.maomaorobot.DB.SaveTipMessageFile;
import com.example.lyw.maomaorobot.R;

import java.util.ArrayList;

/**
 * Created by rain on 2016/7/22.
 */

public class TipsActivity extends Activity{
    private ArrayList<TipsBean> mTipsData;
    private EditText mEditTipsInfo;
    private TextView mTipsNumber;
    private TextView mTipsModify;
    private ImageButton mIamgeButton;
    private ListView mListView;
    private TipsListViewAdapter mAdapter;
    private String TipsMessage ;
    private SaveTipMessageFile mSaveTips;
    private static final String TAG = "TipsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tips_layout);
        initView();
        initData();
   //     initListener();

    }


    private void initListener() {


    }



    private  void initData() {
        mSaveTips = SaveTipMessageFile.getInstance();
        mTipsData = mSaveTips.loadTips();
        Log.d(TAG, "initData:  "+mTipsData.size());
        if (mTipsData.size() == 0){
            mTipsData.add(new TipsBean(R.drawable.icon_plus, ""));
        }
        mAdapter = new TipsListViewAdapter(TipsActivity.this,mTipsData);
        mListView.setAdapter(mAdapter);
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.id_list_tips) ;
        mTipsNumber = (TextView) findViewById(R.id.id_tips_number);
        mTipsModify = (TextView) findViewById(R.id.id_tips_modify);
        mIamgeButton = (ImageButton) findViewById(R.id.id_img_createtip);


        mEditTipsInfo = (EditText) findViewById(R.id.id_edit_createtip);

    }
}
