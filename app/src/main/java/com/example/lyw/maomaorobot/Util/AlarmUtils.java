package com.example.lyw.maomaorobot.Util;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.lyw.maomaorobot.R;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by bluerain on 17-8-30.
 */

public class AlarmUtils {

    private static final String TAG = "AlarmUtils";
    public static final String KEY_CONTENT = "key_content";

    public static final HashMap<String, Integer> mContentMap = new HashMap<>();
    public static int requestCode = 100;
    private static MediaPlayer mp;
    private static android.os.Handler mUIHandler = new UIHandler();
    private static Context mContext;


    public static class InstanceHolder {

        public static AlarmUtils INSTANCE = new AlarmUtils();
    }

    public static class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (null != msg.obj && msg.obj instanceof String) {
                createDialog(mContext, (String) msg.obj);
                startMedia(mContext);
            }

        }
    }

    private AlarmUtils() {
    }

    /**
     * 开启提醒
     */
    public static void startRemind(Context context, Calendar calendar, String content) {
        mContext = context;

        //得到日历实例，主要是为了下面的获取时间
//        mCalendar = Calendar.getInstance();
//        mCalendar.setTimeInMillis(System.currentTimeMillis());


        //是设置日历的时间，主要是让日历的年月日和当前同步
//        mCalendar.setTimeInMillis(System.currentTimeMillis());
        // 这里时区需要设置一下，不然可能个别手机会有8个小时的时间差
//        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //设置在几点提醒  设置的为13点
//        mCalendar.set(Calendar.HOUR_OF_DAY, 13);
        //设置在几分提醒  设置的为25分
//        mCalendar.set(Calendar.MINUTE, 25);
        //下面这两个看字面意思也知道
//        mCalendar.set(Calendar.SECOND, 0);
//        mCalendar.set(Calendar.MILLISECOND, 0);

        //上面设置的就是13点25分的时间点


        //获取当前毫秒值
        long systemTime = System.currentTimeMillis();

        //获取上面设置的13点25分的毫秒值
        long selectTime = calendar.getTimeInMillis();

        // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if (systemTime > selectTime) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        mContentMap.put(content, requestCode++);

        //AlarmReceiver.class为广播接受者
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(KEY_CONTENT, content);
        PendingIntent pi = PendingIntent.getBroadcast(context, mContentMap.get(content), intent, 0);
        //得到AlarmManager实例
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //**********注意！！下面的两个根据实际需求任选其一即可*********

        /**
         * 单次提醒
         * mCalendar.getTimeInMillis() 上面设置的13点25分的时间点毫秒值
         */
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);

        /**
         * 重复提醒
         * 第一个参数是警报类型；下面有介绍
         * 第二个参数网上说法不一，很多都是说的是延迟多少毫秒执行这个闹钟，但是我用的刷了MIUI的三星手机的实际效果是与单次提醒的参数一样，即设置的13点25分的时间点毫秒值
         * 第三个参数是重复周期，也就是下次提醒的间隔 毫秒值 我这里是一天后提醒
         */
//        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), (1000 * 60 * 60 * 24), pi);

    }


    /**
     * 关闭提醒
     */
    public static void stopRemind(Context context, String content) {

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, mContentMap.get(content),
                intent, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //取消警报
        am.cancel(pi);
        Toast.makeText(context, "关闭了提醒", Toast.LENGTH_SHORT).show();

    }

    public static class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String content = null;
            if (null != intent.getExtras() && intent.getExtras().containsKey(KEY_CONTENT)) {
                content = intent.getExtras().getString(KEY_CONTENT);
                Message obtain = Message.obtain();
                obtain.obj = content;
                mUIHandler.sendMessage(obtain);

            }
            //当系统到我们设定的时间点的时候会发送广播，执行这里
//            Toast.makeText(context, "onReceive: ...." + content, Toast.LENGTH_LONG).show();
        }
    }


    private static void createDialog(Context context, final String content) {
        new AlertDialog.Builder(context)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle("闹钟提醒时间到了!!")
                .setMessage("该[  " + content + "  ]啦")
                .setPositiveButton("推迟5分钟", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date(System.currentTimeMillis() + 5 * 60 * 1000));
                        startRemind(mContext, calendar, content);
                    }
                })
                .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mp.stop();
                    }
                }).show();
    }

    /**
     * 开始播放铃声
     */
    private static void startMedia(Context context) {
        try {
            mp = new MediaPlayer();
            mp.setDataSource(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)); //铃声类型为默认闹钟铃声
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
