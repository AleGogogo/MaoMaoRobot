package com.example.lyw.maomaorobot.Util;


import android.util.Log;

import com.example.lyw.maomaorobot.Bean.BaseResponseMessage;
import com.example.lyw.maomaorobot.Bean.CaiPuResponseMessage;
import com.example.lyw.maomaorobot.Bean.LinkResponseMessage;
import com.example.lyw.maomaorobot.Bean.NewsResponseMessage;
import com.example.lyw.maomaorobot.Bean.TextResponseMessage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by LYW on 2016/5/25.
 */
public class HttpEngine {

    private static final String TAG = "HttpEngine";


    public static void doPost(final String url, final String message, final
    HttpCallbackListener listener) {

        new Thread(new Runnable() {

            StringBuffer sbf = new StringBuffer();

            private void notifyListener(int messageType, String message) {
                if (null == listener)
                    return;
                switch (messageType) {
                    case HttpCallbackListener.MESSAGE_SUCCESS:
                        listener.onSuccess(message);
                        break;
                    case HttpCallbackListener.MESSAGE_FAILED:
                        listener.onFailed(message);
                        break;
                }
            }

            @Override
            public void run() {
                BufferedReader BR = null;
                BufferedOutputStream BOS = null;
                try {
                    URL u = new URL(url);
                    HttpURLConnection connection =
                            (HttpURLConnection) u.openConnection();
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(5000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("content-type",
                            "applcation/json: charset = utf-8");

                    /*写入POST数据*/
                    BOS = new BufferedOutputStream(connection.getOutputStream());
                    BOS.write(message.getBytes());
                    BOS.flush();

                    /*接口错误*/
                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        notifyListener(HttpCallbackListener.MESSAGE_FAILED, "服务器出错，请稍候再试！");
                        return;
                    }
                    /*获取返回数据*/
                    BR = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    for (String str = ""; str != null; str = BR.readLine()) {
                        sbf.append(str);
                    }
                    notifyListener(HttpCallbackListener.MESSAGE_SUCCESS, sbf.toString());
                    Log.d(TAG, "response message = [" + sbf.toString() + "]");

                } catch (Exception e) {
                    notifyListener(HttpCallbackListener.MESSAGE_FAILED, "其他错误，请稍候再试！");
                } finally {
                    CommonUtils.colseQuilty(BR);
                    CommonUtils.colseQuilty(BOS);
                }
            }
        }).start();

    }

    /**
     * 将服务器返回JSON串序列化为对象
     *
     * @param response
     * @return
     */
    public static BaseResponseMessage serializeResponse(String response) {
        Gson mGson = new Gson();
        BaseResponseMessage responseObject = null;
        try {
            JSONObject jsonobject = new JSONObject(response);
            int code = jsonobject.getInt("code");
            switch (code) {
                case BaseResponseMessage.RESPONSE_TYPE_TEXT:
                    responseObject = mGson.fromJson(response, TextResponseMessage.class);
                    break;
                case BaseResponseMessage.RESPONSE_TYPE_LINK:
                    responseObject = mGson.fromJson(response, LinkResponseMessage.class);
                    break;
                case BaseResponseMessage.RESPONSE_TYPE_NEWS:
                    responseObject = mGson.fromJson(response, NewsResponseMessage.class);
                    break;
                case BaseResponseMessage.RESPONSE_TYPE_CAIPU:
                    responseObject = mGson.fromJson(response, CaiPuResponseMessage.class);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseObject;
    }


    public interface HttpCallbackListener {

        int MESSAGE_SUCCESS = 1001;
        int MESSAGE_FAILED = 1002;

        void onSuccess(String response);

        void onFailed(String errorMessage);
    }


}
