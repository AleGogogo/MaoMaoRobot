package com.example.lyw.maomaorobot.Util;


import android.util.Log;


import com.example.lyw.maomaorobot.Bean.SendMsg;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by LYW on 2016/5/25.
 */
public class HttpUtil {
    private static final String API_ROBOT =
            "http://www.tuling123.com/openapi/api";
    private static final String API_KRY = "e623da9baaedd093f67d0902d270bea8";
 private static final String TAG ="tulingRobot is saying";
       public static void doPost(final String massage,final HttpCallbackListner listner) {

        final StringBuffer sbf = new StringBuffer();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url =  new URL(API_ROBOT);
                    HttpURLConnection connection =
                            (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(5000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");

                    connection.setRequestProperty("content-type","applcation/json: charset = utf-8");

                    SendMsg sm = new SendMsg();
                    sm.setKey(API_KRY);
                    //   String Info = URLEncoder.encode(massage);
                    sm.setInfo(massage);
                    String msg =ParaseJson.convertJson(sm);

                    OutputStream out = connection.getOutputStream();
                    //这里不用序列化
                    //                 DataOutputStream wr = new DataOutputStream(out);
                    out.write(msg.getBytes());
                    out.flush();
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            connection.getInputStream()));

                    String str = null;
                    while ((str = br.readLine())!=null){
                        sbf.append(str);
                    }

                    str = sbf.toString();

                    Log.d("TAG","request message is"+str);
                    // result =parseJson(str);
                    br.close();
                    out.close();

                    if (listner!=null){
                        String result = sbf.toString();

                        listner.onSuccess(result);
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    listner.onFailed(e);
                    e.printStackTrace();
                }
            }
        }).start();


    }
    public  interface HttpCallbackListner{
        void onSuccess(String response);
        void onFailed(Exception e);
    }


}
