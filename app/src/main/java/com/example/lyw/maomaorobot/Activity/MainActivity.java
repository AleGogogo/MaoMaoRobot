package com.example.lyw.maomaorobot.Activity;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.speech.VoiceRecognitionService;
import com.example.lyw.maomaorobot.Bean.BaseResponse;
import com.example.lyw.maomaorobot.Bean.CaiPuResponse;
import com.example.lyw.maomaorobot.Bean.LinkResponse;
import com.example.lyw.maomaorobot.Bean.NewsResponse;
import com.example.lyw.maomaorobot.Bean.TextResponse;
import com.example.lyw.maomaorobot.CheatMessageAdapter;
import com.example.lyw.maomaorobot.DB.DatabaseManager;
import com.example.lyw.maomaorobot.R;
import com.example.lyw.maomaorobot.Util.HttpUtil;
import com.baidu.yuyin.Constant;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 */
public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    private ArrayList<Object> mData;
    private ListView mListView;
    private CheatMessageAdapter mAapter;
    private EditText mEditMsg;
    private Button mSendButton;
    private static final int MESSAGE_RESPONSE = 11;
    private static final int REQUEST_ME = 12;
    private static final int REQUEST_FAILED = 1;
    private MyHandler mHandler;
    private DatabaseManager mManager;


    /*底部区域*/
    private Button mButtonVoice;
    private ImageView mImageViewVoice;
    private ImageView mImageViewKeyboard;
    private EditText mEditTextInput;
    private Button mButtonSend; //发送按钮


    /*状态位*/
    boolean isVoiceInput = false;

    private SpeechRecognizer mSpeechRecognizer;
    private String toMsg;

    private Gson mGson;


    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
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
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this, new ComponentName(
                this, VoiceRecognitionService.class));
        mSpeechRecognizer.setRecognitionListener(new CustomRecognitionListener());
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
        mImageViewVoice = (ImageView) findViewById(R.id.igv_voice);
        mImageViewKeyboard = (ImageView) findViewById(R.id.igv_keyboard);
        mButtonVoice = (Button) findViewById(R.id.btn_voice);
        mEditTextInput = (EditText) findViewById(R.id.edv_input_main);
        mButtonSend = (Button) findViewById(R.id.btn_message_send);

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
            // TODO: 2016/6/13 做笔记
//                mData.addAll(mManager.loadCheatMessage());
            mData = mManager.loadCheatMessage();
            mAapter.setmDates(mData);

            Log.d(TAG, "loadData: [" + mData + "]");
        }
        //// TODO: 2016/6/13 看源码 
        mAapter.notifyDataSetChanged();
    }

    private void iniListener() {

        initSwitchImageViewListener();
        mButtonVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                bindParams(intent);
                mSpeechRecognizer.startListening(intent);
//                while (toMsg.equals("")){
//                    mSpeakingButton.setVisibility(View.GONE);
//                    mProgressBar.setVisibility(View.VISIBLE);
//                }
//                CheatMessage toMessage = new CheatMessage(toMsg,
//                            CheatMessage.Type.OUTCOMING, (new Date())
//                            .toString());
//                    mData.add(toMessage);
//
//                mListView.setSelection(mData.size() - 1);
//            }
//        });
//        mSendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final String toMsg = mEditMsg.getText().toString();
//                if (TextUtils.isEmpty(toMsg)) {
//                    Toast.makeText(MainActivity.this, "不可以发空信息"
//                            , Toast.LENGTH_SHORT).show();
//                    return;
//                } else {
//
//                }
            }
        });

        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty())
                    return;
                sendPostMessage(mEditTextInput.getText().toString());
                clearInput();
            }
        });
    }

    private void initSwitchImageViewListener() {
        /*设定语音按钮状态*/
        mImageViewVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVoiceInput) {
                    isVoiceInput = false;
                    mButtonVoice.setVisibility(View.GONE);
                    mEditTextInput.setVisibility(View.VISIBLE);
                    mEditTextInput.requestFocus();
                    mImageViewKeyboard.setVisibility(View.VISIBLE);
                    mImageViewVoice.setVisibility(View.INVISIBLE);
                    if (!isEmpty()) {
                        mButtonSend.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        mImageViewKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isVoiceInput) {
                    isVoiceInput = true;
                    mButtonVoice.setVisibility(View.VISIBLE);
                    mEditTextInput.setVisibility(View.GONE);
                    mButtonSend.setVisibility(View.GONE);
                    mImageViewKeyboard.setVisibility(View.INVISIBLE);
                    mImageViewVoice.setVisibility(View.VISIBLE);
                }
            }
        });

        /*设定输入框状态*/
        mEditTextInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isEmpty()) {
                    mButtonSend.setVisibility(View.GONE);
                } else {
                    mButtonSend.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /**
     * 判断用户是否有输入字符
     *
     * @return
     */
    private boolean isEmpty() {
        return mEditTextInput.getText().toString().length() == 0;
    }

    /**
     * 清空用户输入
     */
    private void clearInput() {
        mEditTextInput.setText("");
    }

    private void bindParams(Intent intent) {

        intent.putExtra(Constant.SOUND_START, R.raw.bdspeech_recognition_start);
        intent.putExtra(Constant.SOUND_END, R.raw.bdspeech_speech_end);
        intent.putExtra(Constant.SOUND_SUCCESS, R.raw.bdspeech_recognition_success);
        intent.putExtra(Constant.SOUND_ERROR, R.raw.bdspeech_recognition_error);
        intent.putExtra(Constant.SOUND_CANCEL, R.raw.bdspeech_recognition_cancel);

        //输入语音文件，可以代替说话了

//        String path = Environment.getExternalStorageDirectory().getPath() + "/maomao/s_1";
//        Log.d("TAG", "path is " + path);
//        intent.putExtra(Constant.EXTRA_OFFLINE_ASR_BASE_FILE_PATH, path);
        //    intent.putExtra(Constant.EXTRA_LICENSE_FILE_PATH, "/sdcard/easr/license-tmp-20150530.txt");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        //简直了，我是有多蠢！！！！
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.controllerbar, menu);
        return super.onCreateOptionsMenu(menu);
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
        mSpeechRecognizer.destroy();
        super.onDestroy();
    }


    public void sendPostMessage(String message) {
        HttpUtil.doPost(message, new HttpUtil.HttpCallbackListener() {
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
    }

    class CustomRecognitionListener implements RecognitionListener {
        //准备就绪
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            Log.d("TAG", "onReadyForSpeech----->");
        }

        @Override
        public void onBeginningOfSpeech() {
            Log.d("TAG", "onBeginningOfSpeech-----> ");
            mSpeechRecognizer.stopListening();
        }

        @Override
        public void onRmsChanged(float v) {
            Log.d("TAG", "onRmsChanged------>");
        }

        @Override
        public void onBufferReceived(byte[] bytes) {
            Log.d("TAG", "onBufferReceived------> ");
        }

        @Override
        public void onEndOfSpeech() {
            mSpeechRecognizer.stopListening();
            Log.d("TAG", "onEndOfSpeech----->");
        }


        @Override
        public void onError(int i) {
            Log.d("TAG", "onError------>");
            StringBuilder sb = new StringBuilder();
            switch (i) {
                case SpeechRecognizer.ERROR_AUDIO:
                    sb.append("音频问题");
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    sb.append("没有语音输入");
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    sb.append("其它客户端错误");
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    sb.append("权限不足");
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    sb.append("网络问题");
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    sb.append("没有匹配的识别结果");
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    sb.append("引擎忙");
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    sb.append("服务端错误");
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    sb.append("连接超时");
                    break;
            }
            sb.append(":" + i);
            Toast.makeText(MainActivity.this,
                    "error is :" + sb, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle bundle) {
            Log.d("TAG", "onResults-----> ");
            ArrayList<String> data = bundle.getStringArrayList("results_recognition");
            //mLog.setText(Arrays.toString(data.toArray(new String[data.size()])));
            Log.d("TAG", "data is " + Arrays.toString(data.toArray(new String[data.size()])));
            toMsg = data.get(0);

        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int eventType, Bundle bundle) {
            Log.d("TAG", "onEvent----->");
            switch (eventType) {
                // eventType == 11 表是返回详细错误信息
                case 11:
                    String reason = bundle.get("reason") + "";
                    Log.d("TAG", "EVENT_ERROR is" + reason);
                    break;
            }
        }
    }
}
