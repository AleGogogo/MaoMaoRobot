package com.example.lyw.maomaorobot.Util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.ListView;

import java.io.Closeable;
import java.io.IOException;
import java.text.SimpleDateFormat;

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

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

    public static DispatchQueue queue = new DispatchQueue("Asyn");
    /**
     * 将Listview移动到最后一个位置
     */
    public static void moveListToLastPosition(ListView listView) {
        if (null == listView.getAdapter())
            return;
        listView.smoothScrollToPosition(listView.getAdapter().getCount());
    }

    public static boolean canOpenUserApp(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        return intent.resolveActivity(packageManager) != null;

    }

    /**
     * 关闭资源
     *
     * @param closeable
     */
    public static void closeQuilty(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
