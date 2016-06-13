package com.example.lyw.maomaorobot;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lyw.maomaorobot.Bean.BaseResponse;
import com.example.lyw.maomaorobot.Bean.CaiPuResponse;
import com.example.lyw.maomaorobot.Bean.CheatMessage;
import com.example.lyw.maomaorobot.Bean.LinkResponse;
import com.example.lyw.maomaorobot.Bean.NewsResponse;
import com.example.lyw.maomaorobot.Bean.TextMsg;
import com.example.lyw.maomaorobot.Bean.TextResponse;
import com.example.lyw.maomaorobot.DB.DatabaseManager;
import com.example.lyw.maomaorobot.Util.HttpUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends Activity {
    private ArrayList<Object> mData;
    private ListView mListView;
    private CheatMessageAdapter mAapter;
    private EditText mEditMsg;
    private Button mSendButton;
    private static final int MESSAGE_RESPONSE = 11;
    private static final int REQUEST_MAOMAO = 0;
    private static final int REQUEST_FAILED = 1;
    private MyHandler mHandler;
    private DatabaseManager mManager;
    private static final String TAG = "MainActivity";


    private Gson mGson;


    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
//                case REQUEST_MAOMAO:
//                    //update listView
//                    Object o = (Object) msg.obj;
//                    mData.add(o);
//                    mAapter.notifyDataSetChanged();
//                    mListView.setSelection(mData.size() - 1);
//                    break;
//                case REQUEST_FAILED:
//                    Toast.makeText(MainActivity.this, "Sorry Network is ill",
//                            Toast.LENGTH_SHORT).show();
//                    break;

                case MESSAGE_RESPONSE:
                    mAapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new MyHandler();
        mManager = DatabaseManager.getIntance(MainActivity.this);
        initData();
        initView();
        iniListener();
        loadData();

    }

    private void initData() {
        mData = new ArrayList<>();
        // TODO: 2016/6/8 添加的假数据
//        TextResponse textResponse = new TextResponse();
//        textResponse.code = 100000;
//        textResponse.text = "你好，毛毛为您服务";
//        mData.add(textResponse);
        mGson = new Gson();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.id_listview);
        mEditMsg = (EditText) findViewById(R.id.id_edt_massage);
        mSendButton = (Button) findViewById(R.id.id_but_send);
        mAapter = new CheatMessageAdapter(this, mData);
        mListView.setAdapter(mAapter);
    }


    private void loadData() {

        if (mManager.loadCheatMessage().size() == 0) {
            TextResponse textResponse = new TextResponse();
            textResponse.code = 100000;
            textResponse.text = "你好，毛毛为您服务";
            mData.add(textResponse);
            Log.d("TAG", ((TextResponse) mData.get(0)).getText());
        } else {
            //// TODO: 2016/6/13 做笔记
//                mData.addAll(mManager.loadCheatMessage());
            mData = mManager.loadCheatMessage();
            mAapter.setmDates(mData);

            Log.d(TAG, "loadData: [" + mData + "]");
        }
        //// TODO: 2016/6/13 看源码 
        mAapter.notifyDataSetChanged();
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
                    CheatMessage toMessage = new CheatMessage(toMsg,
                            CheatMessage.Type.OUTCOMING, (new Date())
                            .toString());
                    mData.add(toMessage);
                    mEditMsg.setText("");
                    mListView.setSelection(mData.size() - 1);
                }
                HttpUtil.doPost(toMsg, new HttpUtil.HttpCallbackListener() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            int code = jsonobject.getInt("code");
                            switch (code) {
                                case BaseResponse.RESPONSE_TYPE_TEXT:

                                    TextResponse textResponse = mGson
                                            .fromJson(response, TextResponse
                                                    .class);
                                    mData.add(textResponse);
                                    break;
                                case BaseResponse.RESPONSE_TYPE_LINK:
                                    LinkResponse linkResponse = mGson
                                            .fromJson(response, LinkResponse
                                                    .class);
                                    mData.add(linkResponse);

                                    break;
                                case BaseResponse.RESPONSE_TYPE_NEWS:
                                    NewsResponse newsResponse = mGson
                                            .fromJson(response, NewsResponse
                                                    .class);
                                    Log.d(TAG, "onSuccess: newsResponse[" +
                                            newsResponse.getList() + "]");
                                    mData.add(newsResponse);

                                    break;
                                case BaseResponse.RESPONSE_TYPE_CAIPU:
                                    CaiPuResponse caiPuResponse = mGson
                                            .fromJson(response, CaiPuResponse
                                                    .class);
                                    mData.add(caiPuResponse);
                                    break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Message message = mHandler.obtainMessage();
                        message.what = MESSAGE_RESPONSE;
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

                mAapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //简直了，我是有多蠢！！！！
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.controllerbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_actionbar_add:

                break;
            case R.id.id_actionbar_search:
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        for (int i = 0; i < mData.size(); i++) {
            mManager.saveCheatMsgData(mData.get(i));
        }
        super.onDestroy();
    }
}
