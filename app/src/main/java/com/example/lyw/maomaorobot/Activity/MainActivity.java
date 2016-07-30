package com.example.lyw.maomaorobot.Activity;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.speech.VoiceRecognitionService;
import com.baidu.yuyin.Constant;
import com.example.lyw.maomaorobot.Bean.SendMessage;
import com.example.lyw.maomaorobot.Bean.TextResponseMessage;
import com.example.lyw.maomaorobot.Bean.TulingMessage;
import com.example.lyw.maomaorobot.CheatMessageAdapter;
import com.example.lyw.maomaorobot.DB.DatabaseManager;
import com.example.lyw.maomaorobot.Profile;
import com.example.lyw.maomaorobot.R;
import com.example.lyw.maomaorobot.Util.CommonUtils;
import com.example.lyw.maomaorobot.Util.MessageFilter;
import com.example.lyw.maomaorobot.net.HttpEngine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 */
public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    /*handler消息类型 ,用于鉴别服务器返回消息状态*/
    public static final int HANDLER_MESSAGE_RESPONSE_SUCCESS = 1001;
    public static final int HANDLER_MESSAGE_RESPONSE_FAILED = 1002;

    private ArrayList<TulingMessage> mData;
    private ListView mListView;
    private CheatMessageAdapter mAapter;
    private responseHandler mHandler;

    private DatabaseManager mManager;

    /*底部区域*/
    private Button mButtonVoice;
    private ImageView mImageViewVoice;
    private ImageView mImageViewKeyboard;
    private EditText mEditTextInput;
    private Button mButtonSend; //发送按钮

    /*状态区*/
    boolean isVoiceInput = true;

    private SpeechRecognizer mSpeechRecognizer;

    private MessageFilter mMessageFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new responseHandler();
        mManager = DatabaseManager.getIntance(MainActivity.this);
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this, new ComponentName(
                this, VoiceRecognitionService.class));
        mSpeechRecognizer.setRecognitionListener(new CustomRecognitionListener());
        initData();
        initView();
        iniListener();
//        loadData();

    }

    private void initData() {
        mMessageFilter = MessageFilter.getInstance();
        mData = new ArrayList<>();
        // TODO: 2016/6/8 添加的假数据
        TextResponseMessage textResponseMessage = new TextResponseMessage();
        textResponseMessage.setCode(100000);
        textResponseMessage.setText("你好，毛毛为你服务！");
        mData.add(textResponseMessage);
    }

    private void initView() {
        mImageViewVoice = (ImageView) findViewById(R.id.igv_voice);
        mImageViewKeyboard = (ImageView) findViewById(R.id.igv_keyboard);
        mButtonVoice = (Button) findViewById(R.id.btn_voice);
        mEditTextInput = (EditText) findViewById(R.id.edv_input_main);
        mButtonSend = (Button) findViewById(R.id.btn_message_send);

        mListView = (ListView) findViewById(R.id.id_listview);
        mAapter = new CheatMessageAdapter(this, mData);
        mListView.setAdapter(mAapter);
    }


    private void loadData() {
//
//        if (mManager.loadCheatMessage().size() == 0) {
//            TextResponseMessage textResponse = new TextResponseMessage();
//            textResponse.code = 100000;
//            textResponse.text = "你好，毛毛为您服务";
//            mData.add(textResponse);
//            Log.d("TAG", ((TextResponseMessage) mData.get(0)).getText());
//        } else {
//            // TODO: 2016/6/13 做笔记
//            mAapter.setDates(mData);
//            Log.d(TAG, "loadData: [" + mData + "]");
//        }
    }

    private void iniListener() {

        initSwitchImageViewListener();

        mButtonVoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        Intent intent = new Intent();
                        bindParams(intent);
                        mSpeechRecognizer.startListening(intent);
                        break;
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        break;
                }
                return false;
            }
        });

        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty())
                    return;
                String input = mEditTextInput.getText().toString();
                handleInput(input);
            }
        });
    }


    /**
     * 将发送的消息插入到Listview中,并获取服务器返回
     */
    private void handleInput(String input) {
        SendMessage message = new SendMessage(input);
        mData.add(message);
        mAapter.notifyDataSetChanged();
        CommonUtils.moveListToLastPosition(mListView);
        clearInput();
        mMessageFilter.doFilter(input);
        /*获取服务器返回*/
        postMessage(message);
    }

    private void initSwitchImageViewListener() {
        /*设定语音按钮状态*/
        mImageViewKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVoiceInput) {
                    isVoiceInput = false;
                    mImageViewVoice.setVisibility(View.VISIBLE);
                    mImageViewKeyboard.setVisibility(View.GONE);

                    mEditTextInput.setVisibility(View.VISIBLE);
                    mEditTextInput.requestFocus();

                    mButtonVoice.setVisibility(View.GONE);
                    if (!isEmpty()) {
                        mButtonSend.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        mImageViewVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isVoiceInput) {
                    isVoiceInput = true;
                    mImageViewKeyboard.setVisibility(View.VISIBLE);
                    mImageViewVoice.setVisibility(View.GONE);

                    mButtonVoice.setVisibility(View.VISIBLE);


                    mEditTextInput.setVisibility(View.GONE);
                    mButtonSend.setVisibility(View.GONE);
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

    /**
     * @param intent
     */
    private void bindParams(Intent intent) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        if (defaultSharedPreferences.getBoolean("tips_sound", true)) {
            intent.putExtra(Constant.SOUND_START, R.raw.bdspeech_recognition_start);
            intent.putExtra(Constant.SOUND_END, R.raw.bdspeech_speech_end);
            intent.putExtra(Constant.SOUND_SUCCESS, R.raw.bdspeech_recognition_success);
            intent.putExtra(Constant.SOUND_ERROR, R.raw.bdspeech_recognition_error);
            intent.putExtra(Constant.SOUND_CANCEL, R.raw.bdspeech_recognition_cancel);
        }

        //获取Setting页面设置参数
        {
            /*输入语音文件，可以代替说话了*/
            if (defaultSharedPreferences.contains(Constant.EXTRA_INFILE)) {
                String inputFilePath = defaultSharedPreferences.getString(Constant.EXTRA_INFILE, "").replaceAll(",.*", "").trim();
                Log.d(TAG, "bindParams: inputFilePath = [" + inputFilePath + "]");
                intent.putExtra(Constant.EXTRA_INFILE, inputFilePath);
            }

            /*保存录音*/
            if (defaultSharedPreferences.getBoolean(Constant.EXTRA_OUTFILE, false)) {
                intent.putExtra(Constant.EXTRA_OUTFILE, "sdcard/outfile.pcm");
            }

            /*采样率*/
            if (defaultSharedPreferences.contains(Constant.EXTRA_SAMPLE)) {
                String tmp = defaultSharedPreferences.getString(Constant.EXTRA_SAMPLE, "").replaceAll(",.*", "").trim();
                if (null != tmp && !"".equals(tmp)) {
                    intent.putExtra(Constant.EXTRA_SAMPLE, Integer.parseInt(tmp));
                }
            }

            /*语种*/
            if (defaultSharedPreferences.contains(Constant.EXTRA_LANGUAGE)) {
                String tmp = defaultSharedPreferences.getString(Constant.EXTRA_LANGUAGE, "").replaceAll(",.*", "").trim();
                if (null != tmp && !"".equals(tmp)) {
                    intent.putExtra(Constant.EXTRA_LANGUAGE, tmp);
                }
            }

            /*语义解析*/
            if (defaultSharedPreferences.contains(Constant.EXTRA_NLU)) {
                String tmp = defaultSharedPreferences.getString(Constant.EXTRA_NLU, "").replaceAll(",.*", "").trim();
                if (null != tmp && !"".equals(tmp)) {
                    intent.putExtra(Constant.EXTRA_NLU, tmp);
                }
            }

            /*VAD*/
            if (defaultSharedPreferences.contains(Constant.EXTRA_VAD)) {
                String tmp = defaultSharedPreferences.getString(Constant.EXTRA_VAD, "").replaceAll(",.*", "").trim();
                if (null != tmp && !"".equals(tmp)) {
                    intent.putExtra(Constant.EXTRA_VAD, tmp);
                }
            }

            /*垂直领域*/
            String prop = null;
            if (defaultSharedPreferences.contains(Constant.EXTRA_PROP)) {
                String tmp = defaultSharedPreferences.getString(Constant.EXTRA_PROP, "").replaceAll(",.*", "").trim();
                if (null != tmp && !"".equals(tmp)) {
                    intent.putExtra(Constant.EXTRA_PROP, Integer.parseInt(tmp));
                    prop = tmp;
                }
            }

            // offline asr
            {
                intent.putExtra(Constant.EXTRA_OFFLINE_ASR_BASE_FILE_PATH, "/sdcard/easr/s_1");
                if (null != prop) {
                    int propInt = Integer.parseInt(prop);
                    if (propInt == 10060) {
                        intent.putExtra(Constant.EXTRA_OFFLINE_LM_RES_FILE_PATH, "/sdcard/easr/s_2_Navi");
                    } else if (propInt == 20000) {
                        intent.putExtra(Constant.EXTRA_OFFLINE_LM_RES_FILE_PATH, "/sdcard/easr/s_2_InputMethod");
                    }
                }
                intent.putExtra(Constant.EXTRA_OFFLINE_SLOT_DATA, buildTestSlotData()); // TODO: 2016/6/30 未查到此参数含义！
            }
        }

    }

    private String buildTestSlotData() {
        JSONObject slotData = new JSONObject();
        JSONArray name = new JSONArray().put("李涌泉").put("郭下纶");
        JSONArray song = new JSONArray().put("七里香").put("发如雪");
        JSONArray artist = new JSONArray().put("周杰伦").put("李世龙");
        JSONArray app = new JSONArray().put("手机百度").put("百度地图");
        JSONArray usercommand = new JSONArray().put("关灯").put("开门");
        try {
            slotData.put(Constant.EXTRA_OFFLINE_SLOT_NAME, name);
            slotData.put(Constant.EXTRA_OFFLINE_SLOT_SONG, song);
            slotData.put(Constant.EXTRA_OFFLINE_SLOT_ARTIST, artist);
            slotData.put(Constant.EXTRA_OFFLINE_SLOT_APP, app);
            slotData.put(Constant.EXTRA_OFFLINE_SLOT_USERCOMMAND, usercommand);
        } catch (JSONException e) {

        }
        return slotData.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_actionbar_voice_settings: //语音设定按钮
                OnMenuVoiceSettingClick();
                break;

            case R.id.id_actionbar_search:

                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void OnMenuVoiceSettingClick() {
        startActivity(new Intent("com.example.lyw.maomaorobot.setting"));
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
//            mManager.saveCheatMsgData(mData.get(i));
        }
        mSpeechRecognizer.destroy();
        super.onDestroy();
    }


    /**
     * 获取返回消息
     *
     * @param sendMessage
     */
    public void postMessage(final SendMessage sendMessage) {

        String sendMessageJson = sendMessage.getJsonString();//序列化发送消息对象为josn串

        Log.d(TAG, "postMessage: sendMessageJson = [" + sendMessageJson + "]");

        HttpEngine.doPost(Profile.API_ROBOT_URL, sendMessageJson, new HttpEngine.HttpCallbackListener() {
            @Override
            public void onSuccess(String response) {
                sendMessage.setHadResponse(true);
                mData.add(HttpEngine.serializeResponse(response));
                Message message = mHandler.obtainMessage();
                message.what = HANDLER_MESSAGE_RESPONSE_SUCCESS;
                mHandler.sendMessage(message);
            }

            @Override
            public void onFailed(String errorMessage) {
                Message message = mHandler.obtainMessage();
                message.what = HANDLER_MESSAGE_RESPONSE_FAILED;
                message.obj = errorMessage;
                mHandler.sendMessage(message);
            }

        });
    }

    /**
     * 在UI线程中处理服务器返回消息
     */
    class responseHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_MESSAGE_RESPONSE_SUCCESS:
                    mAapter.notifyDataSetChanged();
                    CommonUtils.moveListToLastPosition(mListView);
                    break;
                case HANDLER_MESSAGE_RESPONSE_FAILED:
                    String errorMessage = (String) msg.obj;
                    Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    break;
            }
        }
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
//            mSpeechRecognizer.stopListening();
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
//            mSpeechRecognizer.stopListening();
            Log.d("TAG", "onEndOfSpeech----->");
        }

        @Override
        public void onError(int error) {
            Log.d("TAG", "onError------>");
            StringBuilder sb = new StringBuilder();
            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    sb.append("音频问题");
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    sb.append("囧..我听不见在说什么..大声点吧！");
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
                    sb.append("囧..我听不懂你在说什么..");
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    sb.append("引擎忙");
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    sb.append("服务器错误");
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    sb.append("连接超时");
                    break;
            }
            Toast.makeText(MainActivity.this,
                    "出错了 :" + sb, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle bundle) {
            Log.d("TAG", "onResults-----> ");
            ArrayList<String> data = bundle.getStringArrayList("results_recognition");
            //mLog.setText(Arrays.toString(data.toArray(new String[data.size()])));
            String result = Arrays.toString(data.toArray(new String[data.size()]));
            handleInput(result);
//            String[] orgResult = result.split("[^\\[]*");
//            if (orgResult.length != 0) {
//                Log.d("TAG", "data is " + orgResult);
//                handleInput(orgResult[0]);
//            }
//            Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
//            toMsg = data.get(0);
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
