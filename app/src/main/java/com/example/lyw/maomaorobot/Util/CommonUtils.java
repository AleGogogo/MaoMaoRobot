package com.example.lyw.maomaorobot.Util;

import android.widget.ListView;

import java.io.Closeable;
import java.io.IOException;

/**
 * 这个类里面写一些通用的工具方法
 * <p/>
 * <p/>
 * Project: MaoMaoRobot.
 * Data: 2016/7/1.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class CommonUtils {


    /**
     * 将Listview移动到最后一个位置
     */
    public static void moveListToLastPosition(ListView listView) {
        if (null == listView.getAdapter())
            return;
        listView.smoothScrollToPosition(listView.getAdapter().getCount());
    }


    /**
     * 关闭资源
     *
     * @param closeable
     */
    public static void colseQuilty(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
