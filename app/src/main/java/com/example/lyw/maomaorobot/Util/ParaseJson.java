package com.example.lyw.maomaorobot.Util;

import android.util.Log;

import com.example.lyw.maomaorobot.Bean.CheatMessage;
import com.example.lyw.maomaorobot.Bean.ReturnMsg;
import com.example.lyw.maomaorobot.Bean.SendMsg;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by LYW on 2016/5/28.
 */
public class ParaseJson {
    public static String convertJson(SendMsg sm) {
        Gson gson =new Gson();
        String jsonObject = gson.toJson(sm);
        return jsonObject;
    }
    public static ReturnMsg parseJson(String result)  {
//        Gson gson =new Gson();
       ReturnMsg r =new ReturnMsg();
//
//        try {
//             r= gson.fromJson(jsonObject, ReturnMsg.class);
//            Log.d("TAG","r is"+r.getMeg());
//        }catch (JsonSyntaxException e){
//            Log.d("TAG","json data parse failed"+e.toString());
//        }
//        return r;
        try {
            JSONObject jsonobject =new JSONObject(result);
            double code = jsonobject.getDouble("code");
            String msg = jsonobject.getString("text");

            r.setCode(code);
            r.setMeg(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return r;
    }
}
