package com.example.lyw.maomaorobot.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bluerain on 17-6-20.
 */

public class CommonFilter {


    static class InstanceHolder{
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
     *
     * @param input
     * @return
     */
    public String getStringDate(String input) {
        String result = "";
        //今天 明天    周末  13号 13日 星期3
        final String[] regexs = {".{1}天" , "周.{1}" , "\\d{1,2}(号|日)" , "星期(一|二|三|四|五|六|天|日)"};
        for (String regex : regexs) {
            final Pattern compile = Pattern.compile(regex);
            final Matcher matcher = compile.matcher(input);
            while (matcher.find()){
                 result = matcher.group(0);
                return result;
            }
        }
        return null;
    }

    /**
     * 获取事件详情  例如"提醒我明天拿雨伞  ----> 拿雨伞"
     * @param dateSplit    日期状语分割 --> 明天  周末  星期三
     * @param input
     * @return
     */
    public String getDetail(String dateSplit, String input) {

        final String[] split1 = input.split(dateSplit);
        if (split1.length > 1) return split1[1];

        return null;
    }

}
