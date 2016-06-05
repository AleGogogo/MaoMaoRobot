package com.example.lyw.maomaorobot.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.lyw.maomaorobot.Bean.CheatMessage;
import com.example.lyw.maomaorobot.Bean.LinkMsg;
import com.example.lyw.maomaorobot.Bean.NewsMsg;
import com.example.lyw.maomaorobot.Bean.ReturnMessage;
import com.example.lyw.maomaorobot.info.DBinfo;

import java.util.ArrayList;



/**
 * Created by LYW on 2016/6/2.
 */
public class DatabaseManager {

    /**
     * 数据库名
     */
    public static final String DB_NAME = DBinfo.DB_NAME;

    /**
     * 数据库版本号
     */
    public static final int VERSION = 1;

    private static DatabaseManager databaseManager;
    private CheatOpenHelper cheatOpenHelper;

    /**
     * 将构造方法私有化
     */
    private DatabaseManager(Context context) {
        cheatOpenHelper = new CheatOpenHelper(context, DB_NAME, null, VERSION);
    }

    public static DatabaseManager getIntance(Context context) {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager(context);
        }
        return databaseManager;
    }

    /**
     * 将cheatmsg实例存进数据库
     */
    public  void saveCheatMsgData(CheatMessage c) {
        if (c != null) {
            ContentValues values =new ContentValues();
            values.put(DBinfo.COLUMN_CHEAT_TYPE,(c.getType()).toString());
            values.put(DBinfo.COLUMN_CHEAT_MSG,c.getMsg());
            values.put(DBinfo.COLUMN_CHEAT_DATE,c.getDate());
            cheatOpenHelper.getWritableDatabase().insert(DBinfo.CHEAT_TABLE_NAME,
                            null,values);
                }
            }


    /**
     * 从数据库中取出CheatMessage
     */
     public ArrayList<CheatMessage> loadCheatMessage()throws NullPointerException{
         ArrayList<CheatMessage> list =new ArrayList<CheatMessage>();

         //SQLiteDatabase 提供的query()会返回一个Cursor对象，
         //查询到的所有数据都将从这个对象中取出
         Cursor cursor = cheatOpenHelper.getReadableDatabase().query(
                 DBinfo.CHEAT_TABLE_NAME,
                 null,
                 DBinfo.COLUMN_CHEAT_TYPE +"=?",
                 new String[]{CheatMessage.Type.OUTCOMING+""},
                 null,
                 null,
                 null);
         if (cursor == null){
             return null;
         }
         else if (cursor.moveToFirst()){
             do {
                 CheatMessage c =  new CheatMessage();
                 c.setMsg(cursor.getString(cursor.getColumnIndex(DBinfo.COLUMN_CHEAT_MSG)));
                 c.setDate(cursor.getString(cursor.getColumnIndex(DBinfo.COLUMN_CHEAT_DATE)));
                 list.add(c);
             }while (cursor.moveToNext());

         }
          return list;
     }

    /**
     * 保存Returnmsg 数据到数据库
     */
    public void saveReturnMsgData(ReturnMessage r) {
        if ( r!=null ){
            ContentValues values = new ContentValues();
            values.put(DBinfo.COLUMN_RETRUN_CODE,r.getmCode());
            values.put(DBinfo.COLUMN_RETRUN_MSG,r.getmMsg());
            values.put(DBinfo.COLUMN_RETURN_DATE,r.getType()+"");
            if (r instanceof LinkMsg){
              values.put(DBinfo.COLUMN_RETRUN_URL,((LinkMsg) r).getUrl());
            }else if (r instanceof NewsMsg){
                values.put(DBinfo.COLUMN_RETRUN_DETAILURL,((NewsMsg)r).getmDate());
            }
            cheatOpenHelper.getWritableDatabase().insert(DBinfo.RETRUN_TABLE_NAME,
                    null,values);
        }
    }

    /**
     * 从数据库读出retrunMsg数据
     */
    public ArrayList<ReturnMessage> laodReturnMsg()throws NullPointerException{
         ArrayList<ReturnMessage> list =new ArrayList<ReturnMessage>();
       Cursor cursor = cheatOpenHelper.getReadableDatabase().query(DBinfo.RETRUN_TABLE_NAME,
               null,
               DBinfo.COLUMN_CHEAT_TYPE+"=?",
               new String[]{ReturnMessage.Type.INCOMING+""},
               null,
               null,
               null);
        if (cursor == null){
            return null;
        } else if (cursor.moveToFirst()){
            do {
                  ReturnMessage r = new ReturnMessage();
                r.setmMsg(cursor.getString(cursor.getColumnIndex(DBinfo.COLUMN_RETRUN_MSG)));
                r.setmCode(cursor.getInt(cursor.getColumnIndex(DBinfo.COLUMN_RETRUN_CODE)));
                r.setmDate(cursor.getString(cursor.getColumnIndex(DBinfo.COLUMN_RETURN_DATE)));
                if (cursor.getString(cursor.getColumnIndex(DBinfo.COLUMN_RETRUN_URL))!=null){
                    ((LinkMsg)r).setUrl(cursor.getString(cursor.getColumnIndex(DBinfo.COLUMN_RETRUN_URL)));
                }else if (cursor.getString(cursor.getColumnIndex(DBinfo.COLUMN_RETRUN_DETAILURL))
                        !=null){
                    ((NewsMsg)r).setUrl(cursor.getString(cursor.getColumnIndex(DBinfo.COLUMN_RETRUN_DETAILURL)));
                }
                    list.add(r);

            }while (cursor.moveToNext());

        }
        return list;
    }
}
