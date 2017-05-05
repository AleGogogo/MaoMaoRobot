package com.example.lyw.maomaorobot.Util;

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

    public static final String TAKE_PHOTO = "拍照";

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    public static MessageFilter getInstance() {
        return INSTANCE;
    }


    private MessageFilter() {
        //no instance
        mFilterItems = new ArrayList<>();

    }


    public void addFilter(FilterItem item) {
        mFilterItems.add(item);

    }

    public boolean doFilter(String messageBody) {
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

        private String message;

        private Runnable runnable;

        private String targetMessage;


        public FilterItem(String targetMessage, String message, Runnable runnable) {
            this.message = message;
            this.runnable = runnable;
            this.targetMessage = targetMessage;
        }

        public boolean match(String message) {
            return message.contains(targetMessage);
        }

        public Runnable getRunnable() {
            return runnable;
        }

        public void setRunnable(Runnable runnable) {
            this.runnable = runnable;
        }
    }

}
