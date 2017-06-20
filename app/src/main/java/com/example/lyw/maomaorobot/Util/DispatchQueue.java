
package com.example.lyw.maomaorobot.Util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by wuyexiong on 5/11/15.
 */
public class DispatchQueue extends Thread {

    public volatile Handler handler = null;
    private final Object handlerSyncObject = new Object();
    public Handler mUIThreadHandler;

    public DispatchQueue(final String threadName) {
        setName(threadName);
        start();
        mUIThreadHandler = new Handler(Looper.getMainLooper());
    }

    private void sendMessage(Message msg, int delay) {
        if (handler == null) {
            try {
                synchronized (handlerSyncObject) {
                    handlerSyncObject.wait();
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        if (handler != null) {
            if (delay <= 0) {
                handler.sendMessage(msg);
            } else {
                handler.sendMessageDelayed(msg, delay);
            }
        }
    }

    public void cancelRunnable(Runnable runnable) {
        if (handler == null) {
            synchronized (handlerSyncObject) {
                if (handler == null) {
                    try {
                        handlerSyncObject.wait();
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        }

        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    public void postRunnable(Runnable runnable , Callback callback) {
        postRunnable(runnable, 0 , callback);
    }

    public void postRunnable(Runnable runnable, long delay , Callback callback) {
        if (handler == null) {
            synchronized (handlerSyncObject) {
                if (handler == null) {
                    try {
                        handlerSyncObject.wait();
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        }

        if (handler != null) {
            if (delay <= 0) {
                handler.post(runnable);
            } else {
                handler.postDelayed(runnable, delay);
            }
        }
    }

    public void cleanupQueue() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void run() {
        Looper.prepare();
        synchronized (handlerSyncObject) {
            handler = new Handler();
            handlerSyncObject.notify();
        }
        Looper.loop();
    }

    public interface Callback {
        void onFinish();

        void onError();
    }

}
