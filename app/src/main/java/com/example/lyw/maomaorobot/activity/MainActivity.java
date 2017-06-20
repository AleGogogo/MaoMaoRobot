package com.example.lyw.maomaorobot.activity;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextUtils;
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
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.speech.VoiceRecognitionService;
import com.baidu.yuyin.Constant;
import com.example.lyw.maomaorobot.Bean.NoteItemBean;
import com.example.lyw.maomaorobot.Bean.SendMessage;
import com.example.lyw.maomaorobot.Bean.TextResponseMessage;
import com.example.lyw.maomaorobot.Bean.TulingMessage;
import com.example.lyw.maomaorobot.DB.SaveTipMessageFile;
import com.example.lyw.maomaorobot.Profile;
import com.example.lyw.maomaorobot.R;
import com.example.lyw.maomaorobot.Util.CommonFilter;
import com.example.lyw.maomaorobot.Util.CommonUtils;
import com.example.lyw.maomaorobot.Util.MessageFilter;
import com.example.lyw.maomaorobot.adapter.CheatMessageAdapter;
import com.example.lyw.maomaorobot.net.HttpEngine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.lyw.maomaorobot.Util.MessageFilter.MEMOREY;
import static com.example.lyw.maomaorobot.Util.MessageFilter.OPEN_NOTE;
import static com.example.lyw.maomaorobot.Util.MessageFilter.SEARCH_KEY_WORDS;
import static com.example.lyw.maomaorobot.Util.MessageFilter.TAKE_PHOTO;

/**
 *
 */
public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    /*handler消息类型 ,用于鉴别服务器返回消息状态*/
    public static final int HANDLER_MESSAGE_RESPONSE_SUCCESS = 1001;
    public static final int HANDLER_MESSAGE_RESPONSE_FAILED = 1002;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    private ArrayList<TulingMessage> mData;
    private ListView mListView;
    private CheatMessageAdapter mAapter;
    private responseHandler mHandler;

    // private DatabaseManager mManager;
    private SaveTipMessageFile mTipsFile;

    private TextView mRobotTextView;

    /*底部区域*/
    private Button mButtonVoice;
    private ImageView mImageViewVoice;
    private ImageView mImageViewKeyboard;
    private EditText mEditTextInput;
    private Button mButtonSend; //发送按钮

    /*状态区*/
    boolean isVoiceInput = true;

    private SpeechRecognizer mSpeechRecognizer;

    /*筛选关键字*/
    private MessageFilter mMessageFilter;
    public static final String TIXING = "提醒";
    public static final String BEIWANGLU = "备忘录";

    /*是否匹配标志位*/
    private boolean isMatch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new responseHandler();
        // mManager = DatabaseManager.getIntance(MainActivity.this);
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this, new
                ComponentName(
                this, VoiceRecognitionService.class));
        mSpeechRecognizer.setRecognitionListener(new
                CustomRecognitionListener());
        initData();
        initView();
        iniListener();
//        loadData();

    }

    private void initData() {
        mMessageFilter = MessageFilter.getInstance();
        mTipsFile = SaveTipMessageFile.getInstance();
        mData = new ArrayList<>();
        // TODO: 2016/6/8 添加的假数据
        TextResponseMessage textResponseMessage = new TextResponseMessage();
        textResponseMessage.setCode(100000);
        textResponseMessage.setText("你好，毛毛为你服务！");
        mData.add(textResponseMessage);
        addFilterItem();
    }

    private void initView() {
        mImageViewVoice = (ImageView) findViewById(R.id.igv_voice);
        mImageViewKeyboard = (ImageView) findViewById(R.id.igv_keyboard);
        mButtonVoice = (Button) findViewById(R.id.btn_voice);
        mEditTextInput = (EditText) findViewById(R.id.edv_input_main);
        mButtonSend = (Button) findViewById(R.id.btn_message_send);
        mRobotTextView = (TextView) findViewById(R.id.id_tvt_cheat_robot);

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

//        mRobotTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,TipsActivity
// .class);
//                startActivity(intent);
//                Log.d(TAG, "run: 成功进如"+BEIWANGLU);
//            }
//        });
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
        messageDoFilter(input);
        if (!isMatch) {
            postMessage(message);
        } else {
            switch (input) {
                case TIXING:
                    TextResponseMessage textResponseMessage = new
                            TextResponseMessage();
                    textResponseMessage.setCode(100000);
                    textResponseMessage.setText("已帮您记下来了");
                    mData.add(textResponseMessage);
                    mAapter.notifyDataSetChanged();
                    CommonUtils.moveListToLastPosition(mListView);
                    break;
                case BEIWANGLU:
                    TextResponseMessage rememberResponseMessage = new
                            TextResponseMessage();
                    rememberResponseMessage.setCode(100000);
                    rememberResponseMessage.setText("点击打开备忘录");
                    mData.add(rememberResponseMessage);
                    mAapter.notifyDataSetChanged();
                    CommonUtils.moveListToLastPosition(mListView);
//                    Intent intent = new Intent(MainActivity.this,
//                            TipsActivity.class);
//                    startActivity(intent);
                    break;

            }
            message.setHadResponse(true);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent
            data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Log.d(TAG, "data is NOT null, file on default position.");
                }
            }
        }
    }

    private void addFilterItem() {
//        mMessageFilter.addFilter(new MessageFilter.FilterItem(TIXING, input,
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        TipsBean tipsBean = new TipsBean();
//                        tipsBean.setmImageId(R.drawable.icon_plus);
//                        tipsBean.setmTipsInfo(input);
//                        mTipsFile.saveTips(MainActivity.this, tipsBean);
//                        Log.d(TAG, "run: 成功进入" + TIXING);
//                    }
//                }));
//        mMessageFilter.addFilter(new MessageFilter.FilterItem(BEIWANGLU, input,
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        TipsBean tipsBean = new TipsBean();
//                        tipsBean.setmImageId(R.drawable.icon_plus);
//                        tipsBean.setmTipsInfo(input);
//                        mTipsFile.saveTips(MainActivity.this, tipsBean);
//                        Log.d(TAG, "run: 成功进入" + BEIWANGLU);
//                    }
//                }));
        mMessageFilter.addFilter(new MessageFilter.FilterItem(TAKE_PHOTO, new Runnable() {
            @Override
            public void run() {
                try {
                    openCamera();
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "error，请检查相机应用是否存在！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }));

        mMessageFilter.addFilter(new MessageFilter.FilterItem(MEMOREY, new Runnable() {
            @Override
            public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rememberSomething(mMessageFilter.getCurrentMessage());
                        }
                    });
            }
        }));

        mMessageFilter.addFilter(new MessageFilter.FilterItem(OPEN_NOTE, new Runnable() {
            @Override
            public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            openRemember();
                        }
                    });
            }
        }));
        mMessageFilter.addFilter(new MessageFilter.FilterItem(SEARCH_KEY_WORDS, new Runnable() {
            @Override
            public void run() {
                String currentMessage = mMessageFilter.getCurrentMessage();
                if (!TextUtils.isEmpty(currentMessage)) {
                    for (String key : SEARCH_KEY_WORDS) {
                        String[] split = currentMessage.split(key);
                        if (split.length > 1) {
                            searchOnWebView(split[1].split("]")[0]);
                            break;
                        }
                    }
                }
            }
        }));
    }

    /**
     * 添加提醒
     * @param currentMessage
     */
    private void rememberSomething(String currentMessage) {
        final String date = CommonFilter.getInstance().getStringDate(currentMessage);
        final String detail = CommonFilter.getInstance().getDetail(date, currentMessage);
        if (!TextUtils.isEmpty(date) && !TextUtils.isEmpty(detail)) {
            final NoteItemBean itemBean = new NoteItemBean("提醒","主人,要记得 " + date + detail+ "哦~~", System.currentTimeMillis() );
            itemBean.save();
            Toast.makeText(this, "备忘录添加成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "我似乎没法分析这句\" "+currentMessage+" \"" + " 囧 .....", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 打开备忘录
     */
    private void openNote() {
        Toast.makeText(this, "备忘录", Toast.LENGTH_SHORT).show();
    }

    private void searchOnWebView(String keyWord) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(WebViewActivity.EXTRA_KEY_WORD, keyWord);
        startActivity(intent);
    }

    private void openCamera() {
        Intent intent = new Intent();
        // 指定开启系统相机的Action
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        if (CommonUtils.canOpenUserApp(this, intent)) {
            startActivity(intent);
        } else {
            throw new RuntimeException("不能打开相机");
        }
    }


    private void messageDoFilter(String message) {

        isMatch = mMessageFilter.doFilter(message);
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
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
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
        SharedPreferences defaultSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());

        if (defaultSharedPreferences.getBoolean("tips_sound", true)) {
            intent.putExtra(Constant.SOUND_START, R.raw
                    .bdspeech_recognition_start);
            intent.putExtra(Constant.SOUND_END, R.raw.bdspeech_speech_end);
            intent.putExtra(Constant.SOUND_SUCCESS, R.raw
                    .bdspeech_recognition_success);
            intent.putExtra(Constant.SOUND_ERROR, R.raw
                    .bdspeech_recognition_error);
            intent.putExtra(Constant.SOUND_CANCEL, R.raw
                    .bdspeech_recognition_cancel);
        }

        //获取Setting页面设置参数
        {
            /*输入语音文件，可以代替说话了*/
            if (defaultSharedPreferences.contains(Constant.EXTRA_INFILE)) {
                String inputFilePath = defaultSharedPreferences.getString
                        (Constant.EXTRA_INFILE, "").replaceAll(",.*", "")
                        .trim();
                Log.d(TAG, "bindParams: inputFilePath = [" + inputFilePath +
                        "]");
                intent.putExtra(Constant.EXTRA_INFILE, inputFilePath);
            }

            /*保存录音*/
            if (defaultSharedPreferences.getBoolean(Constant.EXTRA_OUTFILE,
                    false)) {
                intent.putExtra(Constant.EXTRA_OUTFILE, "sdcard/outfile.pcm");
            }

            /*采样率*/
            if (defaultSharedPreferences.contains(Constant.EXTRA_SAMPLE)) {
                String tmp = defaultSharedPreferences.getString(Constant
                        .EXTRA_SAMPLE, "").replaceAll(",.*", "").trim();
                if (null != tmp && !"".equals(tmp)) {
                    intent.putExtra(Constant.EXTRA_SAMPLE, Integer.parseInt
                            (tmp));
                }
            }

            /*语种*/
            if (defaultSharedPreferences.contains(Constant.EXTRA_LANGUAGE)) {
                String tmp = defaultSharedPreferences.getString(Constant
                        .EXTRA_LANGUAGE, "").replaceAll(",.*", "").trim();
                if (null != tmp && !"".equals(tmp)) {
                    intent.putExtra(Constant.EXTRA_LANGUAGE, tmp);
                }
            }

            /*语义解析*/
            if (defaultSharedPreferences.contains(Constant.EXTRA_NLU)) {
                String tmp = defaultSharedPreferences.getString(Constant
                        .EXTRA_NLU, "").replaceAll(",.*", "").trim();
                if (null != tmp && !"".equals(tmp)) {
                    intent.putExtra(Constant.EXTRA_NLU, tmp);
                }
            }

            /*VAD*/
            if (defaultSharedPreferences.contains(Constant.EXTRA_VAD)) {
                String tmp = defaultSharedPreferences.getString(Constant
                        .EXTRA_VAD, "").replaceAll(",.*", "").trim();
                if (null != tmp && !"".equals(tmp)) {
                    intent.putExtra(Constant.EXTRA_VAD, tmp);
                }
            }

            /*垂直领域*/
            String prop = null;
            if (defaultSharedPreferences.contains(Constant.EXTRA_PROP)) {
                String tmp = defaultSharedPreferences.getString(Constant
                        .EXTRA_PROP, "").replaceAll(",.*", "").trim();
                if (null != tmp && !"".equals(tmp)) {
                    intent.putExtra(Constant.EXTRA_PROP, Integer.parseInt(tmp));
                    prop = tmp;
                }
            }

            // offline asr
            {
                intent.putExtra(Constant.EXTRA_OFFLINE_ASR_BASE_FILE_PATH,
                        "/sdcard/easr/s_1");
                if (null != prop) {
                    int propInt = Integer.parseInt(prop);
                    if (propInt == 10060) {
                        intent.putExtra(Constant
                                        .EXTRA_OFFLINE_LM_RES_FILE_PATH,
                                "/sdcard/easr/s_2_Navi");
                    } else if (propInt == 20000) {
                        intent.putExtra(Constant
                                        .EXTRA_OFFLINE_LM_RES_FILE_PATH,
                                "/sdcard/easr/s_2_InputMethod");
                    }
                }
                intent.putExtra(Constant.EXTRA_OFFLINE_SLOT_DATA,
                        buildTestSlotData()); // TODO: 2016/6/30 未查到此参数含义！
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
            case R.id.id_actionbar_remember: //打开备忘录
                openRemember();

        }
        return super.onOptionsItemSelected(item);

    }

    /**
     * 打开备忘录
     */
    private void openRemember() {
        Intent intent = new Intent(MainActivity.this, com.example.lyw.maomaorobot.activity.NoteActivity.class);
        startActivity(intent);
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

        String sendMessageJson = sendMessage.getJsonString();//序列化发送消息对象为json串

        Log.d(TAG, "postMessage: sendMessageJson = [" + sendMessageJson + "]");

        HttpEngine.doPost(Profile.API_ROBOT_URL, sendMessageJson, new
                HttpEngine.HttpCallbackListener() {
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
                    Toast.makeText(MainActivity.this, errorMessage, Toast
                            .LENGTH_LONG).show();
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
            ArrayList<String> data = bundle.getStringArrayList
                    ("results_recognition");
            //mLog.setText(Arrays.toString(data.toArray(new String[data.size
            // ()])));
            StringBuilder builder = new StringBuilder();
            for (String s : data) {
                builder.append(s);
            }
            handleInput(builder.toString());
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
