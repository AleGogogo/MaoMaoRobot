package com.example.lyw.maomaorobot.Util;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by rain on 2016/7/30.
 */
public class MessageFilter {
    private static final String TAG = "MessageFilter";

    private static final MessageFilter INSTANCE = new MessageFilter();

    private List<FilterItem> mFilterItems;

    public static final String TIXING = "提醒";

    public static final String BEIWANGLU = "备忘录";

    public static final String[] TAKE_PHOTO = {"拍.{0,}照" , "开.{0,}相机"};

    public static final String[] OPEN_NOTE = {".+备忘录"};

    public static final String[] MEMOREY = {"提醒.+"};

    public static final String[] SEARCH_KEY_WORDS = {"搜索"};

    //智能家居命令
    public static final String[] SMATE_HOME_OPEN_LIGHT = {"开灯"};

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    private String mCurrentMessage;

    public static MessageFilter getInstance() {
        return INSTANCE;
    }


    public String getCurrentMessage() {
        return mCurrentMessage;
    }

    private MessageFilter() {
        //no instance
        mFilterItems = new ArrayList<>();

    }


    public void addFilter(FilterItem item) {
        mFilterItems.add(item);
    }

    public boolean doFilter(String messageBody) {
        mCurrentMessage = messageBody;
        boolean isMatch = false;
        for (FilterItem item : mFilterItems) {
            if (item.match(messageBody)) {
                Runnable runnable = item.getRunnable();
                //为啥在这里进行一个判断?
                if (null == runnable)
                    continue;
                EXECUTOR_SERVICE.execute(runnable);
                isMatch = true;
                Log.d(TAG, "doFilter: ");
            }
        }
        return isMatch;
    }


    public static class FilterItem {


        private Runnable runnable;

        private String[] targetRegs;


        /**
         * @param regString 正则表达式 用于匹配message
         */
        public FilterItem(String[] regString, Runnable runnable) {
            this.runnable = runnable;
            this.targetRegs = regString;
        }

        public boolean match(String message) {
            boolean match = false;
            for (String reg : targetRegs) {
                match = !TextUtils.isEmpty(message) && message.split(reg).length != 1;
                if (match) break;
            }
            return match;
        }

        public Runnable getRunnable() {
            return runnable;
        }

        public void setRunnable(Runnable runnable) {
            this.runnable = runnable;
        }
    }

}
