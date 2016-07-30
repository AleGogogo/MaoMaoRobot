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

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    public static MessageFilter getInstance() {
        return INSTANCE;
    }


    private MessageFilter() {
        //no instance
        mFilterItems = new ArrayList<>();
        mFilterItems.add(new FilterItem(TIXING, null, new Runnable() {
            @Override
            public void run() {
                // TODO: 2016/7/30
                Log.d(TAG, "run: 成功匹配到：" + TIXING);
            }
        }));
        mFilterItems.add(new FilterItem(BEIWANGLU, null, new Runnable() {
            @Override
            public void run() {
                // TODO: 2016/7/30
                Log.d(TAG, "run: 成功匹配到：" + BEIWANGLU);
            }
        }));


    }


    public void doFilter(String messageBody) {
        for (FilterItem item : mFilterItems) {
            if (item.match(messageBody)) {
                Runnable runnable = item.getRunnable();
                if (null == runnable)
                    continue;
                EXECUTOR_SERVICE.execute(runnable);
            }
        }
    }


    class FilterItem {

        private String filterRegx;

        private Runnable runnable;

        private String targetMessage;


        public FilterItem(String targetMessage, String filterRegx, Runnable runnable) {
            this.filterRegx = filterRegx;
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
