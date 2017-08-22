package com.example.lyw.maomaorobot.Util;

import android.content.Context;
import android.os.Bundle;
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
    public static final String COMMAND_OPEN_LIGHT_URL = "http://1v77873a21.iok.la/iot/temp.php";

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

    public void openLight() {
        StringRequest request = new StringRequest(COMMAND_OPEN_LIGHT_URL, new ResponseListener(), new ErrorListener());
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
