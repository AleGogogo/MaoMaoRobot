package com.example.lyw.maomaorobot.Util;

import android.util.Log;

import com.example.lyw.maomaorobot.Bean.CheatMessage;
import com.example.lyw.maomaorobot.Bean.LinkMsg;
import com.example.lyw.maomaorobot.Bean.NewsMsg;
import com.example.lyw.maomaorobot.Bean.ReturnMessage;
import com.example.lyw.maomaorobot.Bean.SendMsg;
import com.example.lyw.maomaorobot.Bean.TextMsg;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;

/**
 * Created by LYW on 2016/5/28.
 */
public class ParaseJson {
    public static Gson gson = new Gson();

    public static String convertJson(SendMsg sm) {

        String jsonObject = gson.toJson(sm);
        return jsonObject;
    }

    public static Object parseJson(String result) {
        List<String> list = null;

        try {
            JSONObject jsonobject = new JSONObject(result);
            int code = jsonobject.getInt("code");
            String msg = jsonobject.getString("text");
            if (jsonobject.has("url")) {
                String url = jsonobject.getString("url");
                if (jsonobject.has("list")) {
                    JSONArray jsonArray = jsonobject.getJSONArray("list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jFather = jsonArray.getJSONObject(i);
                        String address = jFather.getString("detailurl");
                        list.add(address);
                    }
                    NewsMsg n = new NewsMsg();
                    n.setContentType(ReturnMessage.CONTENT_TYPE_NEWS);
                    n.setmCode(code);
//                    n.setmMsg(msg);
                    StringBuilder sb = new StringBuilder();
                    sb.append(msg);
                    //   assert list != null;
                    for (int k = 0; k < list.size(); k++) {
                        sb.append(list.get(k));
                    }
                    msg = sb.toString();
                    n.setmMsg(msg);
                    return n;
                } else {
                    LinkMsg l = new LinkMsg();
                    l.setUrl(url);
                    l.setmCode(code);
                    l.setmMsg(msg + url);
                    return l;
                }

            } else {
                TextMsg t = new TextMsg();
                t.setmMsg(msg);
                t.setmCode(code);
                return t;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static LinkMsg parseFromJson(String result) {
        LinkMsg link = new LinkMsg();
        JSONObject jsonobject = null;
        try {
            jsonobject = new JSONObject(result);
            int code = jsonobject.getInt("code");
            String msg = jsonobject.getString("text");
            String url = jsonobject.getString("url");
            link.setmCode(code);
            link.setmMsg(msg);
            link.setUrl(url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return link;
    }

//    public static Object parseFromGson(String result ,){
//
//        gson.fromJson(result);
//
//    }
}
