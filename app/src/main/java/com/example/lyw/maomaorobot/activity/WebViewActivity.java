package com.example.lyw.maomaorobot.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.lyw.maomaorobot.R;

/**
 * Created by bluerain on 17-5-7.
 */

public class WebViewActivity extends Activity {

    private WebView mWebView;

    public static final String EXTRA_KEY_WORD = "extra_key_word";

    public static final String BASE_URL = "https://m.baidu.com/s?word=";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initView();
        handleData();
    }

    private void initView() {
        mWebView = (WebView) findViewById(R.id.id_activity_web_view);
    }

    private void handleData() {
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            request(extras.getString(EXTRA_KEY_WORD));

        }
    }

    private void request(String keyWord) {
        if (!TextUtils.isEmpty(keyWord)) {
            mWebView.loadUrl(BASE_URL + keyWord);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();//返回上一页面
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
