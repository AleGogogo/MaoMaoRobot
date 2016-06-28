package com.example.lyw.maomaorobot.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lyw.maomaorobot.R;

/**
 * Created by LYW on 2016/6/14.
 */
public class SecondActivity extends Activity {
    private WebView mWebView;
    private  ProgressBar mProgressBar;
    private TextView mTextView;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //启动带进度条的progressBar
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.layout_url_jump);
        initView();
        initData();
        jumpToWeb();
    }

    private void jumpToWeb() {
        mWebView.setWebChromeClient(new WebChromeClient(){

            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100){
                    mProgressBar.setVisibility(View.INVISIBLE);
                }else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
            }


            public void onReceivedTitle(WebView view, String title) {
               mTextView.setText(title);
            }

        });
        mWebView.loadUrl(mUrl);
    }

    private void initData() {
        mProgressBar.setMax(100);
        mUrl = getIntent().getData().toString();
    }
    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        mWebView = (WebView) findViewById(R.id.id_webview);
        mTextView = (TextView) findViewById(R.id.id_titleTextView);
        mProgressBar = (ProgressBar) findViewById(R.id.id_progress);

        mWebView.getSettings().setJavaScriptEnabled(true);
    }
}
