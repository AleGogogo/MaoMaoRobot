package com.example.lyw.maomaorobot;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.speech.VoiceRecognitionService;
import com.example.lyw.maomaorobot.info.Constant;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by LYW on 2016/6/25.
 */
public class VoiceRecognizeTest extends Activity implements RecognitionListener {
    private TextView mLog;
    private Button mSetting;
    private Button mStart;
    private SpeechRecognizer mSpeechRecognizer;
    public static final int STATUS_None = 0;
    private int REQUEST_UI = 1;
    private long speechEndTime = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voicetest);
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this,
                new ComponentName(this, VoiceRecognitionService.class));
        mSpeechRecognizer.setRecognitionListener(this);
        iniView();
        iniListener();


    }

    private void iniView() {
        mLog = (TextView) findViewById(R.id.log);
        mSetting = (Button) findViewById(R.id.setting);
        mStart = (Button) findViewById(R.id.begin);
    }

    private void iniListener() {
        mSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.example.lyw.maomaorobot.setting");
                startActivity(intent);
            }
        });
        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                bindParams(intent);
                mSpeechRecognizer.startListening(intent);
//                intent.setAction("com.baidu.action.RECOGNIZE_SPEECH");
//                //跳转到哪个Activity？
//                Log.d("TAG", "onClick------->");
//                startActivityForResult(intent, REQUEST_UI);
    }
    });
    }
    @Override
    protected void onDestroy() {
        mSpeechRecognizer.destroy();
        super.onDestroy();
    }


    //准备就绪
    @Override
    public void onReadyForSpeech(Bundle bundle) {
        Log.d("TAG","onReadyForSpeech----->");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d("TAG", "onBeginningOfSpeech-----> ");

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
        Log.d("TAG", "onError------->");
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
        mLog.setText(sb);
        mStart.setText("开始");
    }

    @Override
    public void onResults(Bundle bundle) {
        ArrayList<String> data = bundle.getStringArrayList("results_recognition");
        mLog.setText(Arrays.toString(data.toArray(new String[data.size()])));
        String json_res =bundle.getString("origin_result");
        mLog.setText(json_res);
    }

    @Override
    public void onPartialResults(Bundle bundle) {
        Log.d("TAG", "onPartialResults------>");
    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }


    private void bindParams(Intent intent) {
        Log.d("TAG", "bindParams-----> ");
        intent.putExtra(Constant.SOUND_START , R.raw.bdspeech_recognition_start);
        intent.putExtra(Constant.SOUND_END, R.raw.bdspeech_speech_end);
        intent.putExtra(Constant.SOUND_SUCCESS, R.raw.bdspeech_recognition_success);
        intent.putExtra(Constant.SOUND_ERROR, R.raw.bdspeech_recognition_error);
        intent.putExtra(Constant.SOUND_CANCEL, R.raw.bdspeech_recognition_cancel);

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent
//            data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RESULT_OK){
//            onResults(data.getExtras());
//            Log.d("TAG", "onActivityResult-----> ");
//        }
//    }
}