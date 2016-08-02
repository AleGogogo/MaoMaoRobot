package com.example.lyw.maomaorobot.Util;

import android.util.Log;

/**
 * Created by rain on 2016/7/30.
 * 很错误的典型示范面向过程
 */
public class FilterUtil {

  //  private static final String TAG = "FilterUtil";
    private static String TIPS = "提醒" ;
    private static String REMEMBER = "备忘录" ;

    public static boolean doFilter(String message){
        boolean isMatch = false;
       if (message.contains(TIPS)){


                   Log.d("TAG","成功匹配到: "+TIPS);
                   isMatch = true;
           }

        if (message.contains(REMEMBER)){
                    isMatch = true;
                    Log.d("TAG","成功匹配到: "+REMEMBER);
                }
        return isMatch;
            }



}
