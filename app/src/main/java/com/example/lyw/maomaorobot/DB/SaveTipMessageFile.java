package com.example.lyw.maomaorobot.DB;

import android.content.Context;
import android.util.Log;

import com.example.lyw.maomaorobot.Bean.TipsBean;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by rain on 2016/8/1.
 */
public class SaveTipMessageFile {
    private static final String TIPS_MESSAGE = "tips";
    private static final String TAG = "tips";
    private static final SaveTipMessageFile INSTANCE = new SaveTipMessageFile();

    public static SaveTipMessageFile getInstance(){
        return INSTANCE;
    }

    public static void saveTips(Context context, TipsBean tipsBean){
        ObjectOutputStream oos = null;
        try {
            FileOutputStream out = context.openFileOutput(TIPS_MESSAGE, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(out);
            oos.writeObject(tipsBean);
            oos.flush();
            Log.d(TAG, "存储成功！");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (oos !=null){
                try {
                   oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    public static ArrayList<TipsBean> loadTips(){
        ObjectInputStream ois = null;
        ArrayList<TipsBean> beans = new ArrayList<>();
        try {
            FileInputStream in = new FileInputStream(TIPS_MESSAGE);
            ois = new ObjectInputStream(in);
            while(ois!=null) {
                TipsBean bean;
                bean = (TipsBean) ois.readObject();
                beans.add(bean);

            }
            Log.d(TAG, "读取成功  "+beans.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois!=null){
                try {
                   ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return beans;
    }

}
