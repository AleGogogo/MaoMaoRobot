package com.example.lyw.maomaorobot;


import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lyw.maomaorobot.Bean.CheatMessage;
import com.example.lyw.maomaorobot.Bean.ReturnMsg;
import com.example.lyw.maomaorobot.Util.HttpUtil;
import com.example.lyw.maomaorobot.Util.ParaseJson;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends Activity {
   private ArrayList mData;
   private ListView mListView;
   private CheatMessageAdapter mAapter;
   private EditText mEditMsg;
   private Button mSendButton;
   private ReturnMsg mReMsg;
   private static final int REQUEST_MAOMAO = 0;
   private static final int REQUEST_FAILED = 1;
    private MyHandler mHandler;
    private static final String TAG = "MainActivity";

    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REQUEST_MAOMAO:
                    //update listView
                    ReturnMsg c = (ReturnMsg) msg.obj;
                    mData.add(c);
                    mAapter.notifyDataSetChanged();
                    mListView.setSelection(mData.size()-1);
                break;
                case REQUEST_FAILED:
                    Toast.makeText(MainActivity.this,"Sorry Network is ill",
                            Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.lyw.maomaorobot.R.layout.activity_main);
        mHandler = new MyHandler();
        initView();
        initData();
        iniListener();
    }

    private void iniListener() {
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String toMsg = mEditMsg.getText().toString();
                if (TextUtils.isEmpty(toMsg)) {
                    Toast.makeText(MainActivity.this, "不可以发空信息"
                            , Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    CheatMessage toMessage = new CheatMessage(toMsg, CheatMessage

                            .Type.OUTCOMING, new Date());
                    mData.add(toMessage);
                    mEditMsg.setText("");
            //        mListView.setSelection(mData.size()-1);
                }
                HttpUtil.doPost(toMsg, new HttpUtil.HttpCallbackListner() {
                        @Override
                        public void onSuccess(String response) {
                            ReturnMsg r = ParaseJson.parseJson(response);
                            Log.d("TAG","maomao de request is "+ r.getMeg());
                            Message message = mHandler.obtainMessage();
                            message.obj = r;
                            message.what = REQUEST_MAOMAO;
                            mHandler.sendMessage(message);
                        }

                        @Override
                        public void onFailed(Exception e) {
                            Message message = mHandler.obtainMessage();
                            message.what = REQUEST_FAILED;
                            mHandler.sendMessage(message);
                        }
                    });

//                    Message message = mHandler.obtainMessage();
//                    message.what = REQUST_MAOMAO;

                CheatMessage ReMessage = new CheatMessage(toMsg, CheatMessage

                        .Type.OUTCOMING, new Date());
                mData.add(ReMessage);
                mAapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.id_listview);
        mEditMsg = (EditText) findViewById(R.id.id_edt_massage);
        mSendButton = (Button) findViewById(R.id.id_but_send);
    }

    private void initData() {
        mData = new ArrayList<>();
        mData.add(new ReturnMsg(ReturnMsg.Type.INCOMING,
                new Date(),"你好，毛毛为您服务"));
        mData.add(new CheatMessage("你好，我是小萌", CheatMessage.Type.OUTCOMING,
                new Date()));
        mAapter = new CheatMessageAdapter(this, mData);
        mListView.setAdapter(mAapter);
    }
}
