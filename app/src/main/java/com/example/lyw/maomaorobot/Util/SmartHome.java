package com.example.lyw.maomaorobot.Util;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by bluerain on 17-8-22.
 */

public class SmartHome {
    public static final String COMMAND_CPU_TMP_URL = "http://1v77873a21.iok.la/iot/temp.php";
    public static final String COMMAND_OPEN_LIGHT_URL = "http://1v77873a21.iok.la/iot/setE.php/?key=eon";
    public static final String COMMAND_CLOSE_LIGHT_URL = "http://1v77873a21.iok.la/iot/setE.php/?key=eoff";
    //
    public static final String COMMAND_OPEN_FAN_URL = "http://1v77873a21.iok.la/iot/setE.php?key=fon";
    public static final String COMMAND_CLOSE_FAN_URL = "http://1v77873a21.iok.la/iot/setE.php?key=foff";
    //
    public static final String COMMAND_OPEN_JIASHIQI_URL = "http://1v77873a21.iok.la/iot/setE.php?key=lon";
    public static final String COMMAND_CLOSE_JIASHIQI_URL = "http://1v77873a21.iok.la/iot/setE.php?key=loff";
    //
    public static final String COMMAND_OPEN_MUSIC_URL = "http://1v77873a21.iok.la/iot/setE.php?key=mon";
    public static final String COMMAND_CLOSE_MUSIC_URL = "http://1v77873a21.iok.la/iot/setE.php?key=moff";

    private static SmartHome INSTANCE;
    private final RequestQueue requestQueue;
    private Context mContext;

    private SmartHome(Context context) {
        //no instance
        mContext = context;
        requestQueue = Volley.newRequestQueue(mContext);

    }

    public static SmartHome getInstance(Context context) {
        if (null == INSTANCE)
            INSTANCE = new SmartHome(context);
        return INSTANCE;
    }

    public void getCPUTmp() {
        StringRequest request = new StringRequest(COMMAND_CPU_TMP_URL, new ResponseListener(), new ErrorListener());
        requestQueue.add(request);
    }
    public void openLight() {
        StringRequest request = new StringRequest(COMMAND_OPEN_LIGHT_URL, new ResponseListener(), new ErrorListener());
        requestQueue.add(request);
    }
    public void closeLight() {
        StringRequest request = new StringRequest(COMMAND_CLOSE_LIGHT_URL, new ResponseListener(), new ErrorListener());
        requestQueue.add(request);
    }

    public void openFan() {
        StringRequest request = new StringRequest(COMMAND_OPEN_FAN_URL, new ResponseListener(), new ErrorListener());
        requestQueue.add(request);
    }
    public void closeFan() {
        StringRequest request = new StringRequest(COMMAND_CLOSE_FAN_URL, new ResponseListener(), new ErrorListener());
        requestQueue.add(request);
    }


    public void openJiashiqi() {
        StringRequest request = new StringRequest(COMMAND_OPEN_JIASHIQI_URL, new ResponseListener(), new ErrorListener());
        requestQueue.add(request);
    }
    public void closeJiashiqi() {
        StringRequest request = new StringRequest(COMMAND_CLOSE_JIASHIQI_URL, new ResponseListener(), new ErrorListener());
        requestQueue.add(request);
    }

    public void openMusic() {
        StringRequest request = new StringRequest(COMMAND_OPEN_MUSIC_URL, new ResponseListener(), new ErrorListener());
        requestQueue.add(request);
    }
    public void closeMusic() {
        StringRequest request = new StringRequest(COMMAND_CLOSE_MUSIC_URL, new ResponseListener(), new ErrorListener());
        requestQueue.add(request);
    }



    private class ResponseListener implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
        }
    }

    private class ErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(mContext, error.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
