package com.example.lyw.maomaorobot;


import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
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

import com.example.lyw.maomaorobot.Bean.CheatMessage;
import com.example.lyw.maomaorobot.Bean.ReturnMessage;
import com.example.lyw.maomaorobot.Bean.TextMsg;
import com.example.lyw.maomaorobot.DB.DatabaseManager;
import com.example.lyw.maomaorobot.Util.HttpUtil;
import com.example.lyw.maomaorobot.Util.ParaseJson;


import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends Activity {
    private ArrayList<Object> mData;
    private ListView mListView;
    private CheatMessageAdapter mAapter;
    private EditText mEditMsg;
    private Button mSendButton;
    private static final int REQUEST_MAOMAO = 0;
    private static final int REQUEST_FAILED = 1;
    private MyHandler mHandler;
    private DatabaseManager mManager;
    private static final String TAG = "MainActivity";


    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_MAOMAO:
                    //update listView
                    Object o = (Object) msg.obj;
                    mData.add(o);
                    mAapter.notifyDataSetChanged();
                    mListView.setSelection(mData.size() - 1);
                    break;
                case REQUEST_FAILED:
                    Toast.makeText(MainActivity.this, "Sorry Network is ill",
                            Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new MyHandler();
        mManager = DatabaseManager.getIntance(MainActivity.this);
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

                            .Type.OUTCOMING, (new Date()).toString());
                    mData.add(toMessage);
                    mEditMsg.setText("");
                    //        mListView.setSelection(mData.size()-1);
                }
                HttpUtil.doPost(toMsg, new HttpUtil.HttpCallbackListner() {
                    @Override
                    public void onSuccess(String response) {
                        Object o = ParaseJson.parseJson(response);

                        Message message = mHandler.obtainMessage();
                        message.obj = o;
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
        try {
            if (mManager.laodReturnMsg().size()!=0||
                    mManager.loadCheatMessage().size() != 0)
            {
                if (mManager.loadCheatMessage() != null&&mManager.laodReturnMsg().size()!=0)
                {
                    for (int i = 0; i < mManager.loadCheatMessage().size();
                         i++) {
                        mData.add(mManager.loadCheatMessage().get(i));
                        mAapter = new CheatMessageAdapter(this, mData);
                        mListView.setAdapter(mAapter);
                    }
                } else if (mManager.laodReturnMsg() != null&&mManager.laodReturnMsg().size()!=0)
                {
                    for (int i = 0; i < mManager.laodReturnMsg().size(); i++) {
                        mData.add(mManager.laodReturnMsg().get(i));

                    }
                    mAapter = new CheatMessageAdapter(this, mData);
                    mListView.setAdapter(mAapter);
                }
            } else {
                mData = new ArrayList<Object>();
                mData.add(new TextMsg(100000, "你好，毛毛为您服务", (new Date()).toString()));
                Log.d("TAG", ((TextMsg) mData.get(0)).getmMsg());
                mAapter = new CheatMessageAdapter(this, mData);
                mListView.setAdapter(mAapter);
            }
        }catch (NullPointerException e){
            Log.d("TAG",e.getMessage());
        }

        }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //简直了，我是有多蠢！！！！
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.controllerbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
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
        for (int i = 0;i<mData.size();i++){
            if (mData.get(i)instanceof CheatMessage){
                mManager.saveCheatMsgData((CheatMessage)mData.get(i));
            }else {
                mManager.saveReturnMsgData((ReturnMessage)mData.get(i));
            }
        }
    }
}
