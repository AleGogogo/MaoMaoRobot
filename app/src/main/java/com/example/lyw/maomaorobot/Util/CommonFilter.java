package com.example.lyw.maomaorobot.Util;

import android.text.TextUtils;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bluerain on 17-6-20.
 */

public class CommonFilter {
    private static final String TAG = "CommonFilter";
    public static HashMap<String, Integer> maps;

    {
        maps = new HashMap<>();
        maps.put("一", 1);
        maps.put("二", 2);
        maps.put("两", 2);
        maps.put("三", 3);
        maps.put("四", 4);
        maps.put("五", 5);
        maps.put("六", 6);
        maps.put("七", 7);
        maps.put("八", 8);
        maps.put("九", 9);
    }

    static class InstanceHolder {
        public static CommonFilter INSTANCE = new CommonFilter();
    }

    private CommonFilter() {
        //no instance
    }

    public static CommonFilter getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * 提取日期信息
     *
     * @param input
     * @return
     */
    public String getStringDate(String input) {
        String result = "";
        //今天 明天    周末  13号 13日 星期3
        final String[] regexs = {".{1}天", ".+(分钟｜钟秒|分|秒)", "周.{1}", "\\d{1,2}(号|日)", "星期(一|二|三|四|五|六|天|日)"};
        for (String regex : regexs) {
            final Pattern compile = Pattern.compile(regex);
            final Matcher matcher = compile.matcher(input);
            while (matcher.find()) {
                result = matcher.group(0);
                return result;
            }
        }
        return null;
    }

    public Calendar getCalendar(String s) {
        Calendar calendar = null;
        long start = 1000 * (s.contains("分钟") ? 60 : 1);
        try {
            String rgx = "(?<=提醒我).+(?=分钟|秒钟|秒|分)";
            Pattern pattern = Pattern.compile(rgx);
            Matcher matcher = pattern.matcher(s);
            while (matcher.find()) {
                calendar = Calendar.getInstance();
                String result = matcher.group();
                String rgxNum = "\\d+";
                if (Pattern.compile(rgxNum).matcher(result).find()) {
                    calendar.setTime(new Date(System.currentTimeMillis() + Integer.parseInt(result) * start));
                    break;
                } else {
                    calendar.setTime(new Date(System.currentTimeMillis() + convertDate2Long(result) * start));
                }
            }
            Log.d(TAG, "getCalendar: time[" + calendar + "]");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    public long convertDate2Long(String date) {
        long result = 0;
        String rgx;
        rgx = "(一|二|两|三|四|五|六|七|八|九)(万|千|百|十|)";
        Pattern pattern = Pattern.compile(rgx);
        Matcher matcher = pattern.matcher(date);
        while (matcher.find()) {
            String tmp = matcher.group();
            if (tmp.length() == 1)
                result += maps.get(tmp.substring(0, 1));
            if (tmp.length() == 2) {
                int num = maps.get(tmp.substring(0, 1));
                if (tmp.substring(1, 2).equals("十")) {
                    result += num * 10;
                }
                if (tmp.substring(1, 2).equals("百")) {
                    result += num * 100;
                }
                if (tmp.substring(1, 2).equals("千")) {
                    result += num * 1000;
                }
                if (tmp.substring(1, 2).equals("万")) {
                    result += num * 10000;
                }
            }
        }
        return result;
    }


    /**
     * 获取事件详情  例如"提醒我明天拿雨伞  ----> 拿雨伞"
     *
     * @param dateSplit 日期状语分割 --> 明天  周末  星期三
     * @param input
     * @returnu
     */
    public String getDetail(String dateSplit, String input) {
        if (TextUtils.isEmpty(dateSplit)) return null;
        final String[] split1 = input.split(dateSplit);
        if (split1.length > 1) return split1[1];
        return null;
    }

    public String getDetail(String content) {
        if (TextUtils.isEmpty(content)) return null;
        String rgx = "(?<=后).+";
        Pattern pattern = Pattern.compile(rgx);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

}
