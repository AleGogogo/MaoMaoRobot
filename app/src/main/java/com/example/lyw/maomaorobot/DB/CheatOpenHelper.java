package com.example.lyw.maomaorobot.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.lyw.maomaorobot.info.DBinfo;

/**
 * Created by LYW on 2016/6/2.
 */
public class CheatOpenHelper extends SQLiteOpenHelper{
  public static final String CREATE_CHEATMSG ="create table "+ DBinfo
          .CHEAT_TABLE_NAME + "( "
          + DBinfo.COLUMN_CHEAT_ID+" integer primary key autoincrement, "
          + DBinfo.COLUMN_CHEAT_TYPE+" text, "
          + DBinfo.COLUMN_CHEAT_DATE+" text, "
          + DBinfo.COLUMN_CHEAT_URL +" text, "
          + DBinfo.COLUMN_CHEAT_CODE+" integer, "
          + DBinfo.COLUMN_CHEAT_MSG+" text)";

    public static final String CREATE_BEANS ="create table "+ DBinfo
            .CHEAT_TABLE_BEANS + "( "
            + DBinfo.COLUMN_CHEAT_BEAN_KEY+" integer, "
            + DBinfo.COLUMN_CHEAT_ARTICLE+" text, "
            + DBinfo.COLUMN_CHEAT_DETAILURL+" text, "
            + DBinfo.COLUMN_CHEAT_ICON+" text, "
            + DBinfo.COLUMN_CHEAT_NAME+" text, "
            + DBinfo.COLUMN_CHEAT_INFO+" text, "
            + DBinfo.COLUMN_CHEAT_SOURCE+" text)";

    private Context mContext;

    public CheatOpenHelper(Context context, String name, SQLiteDatabase
            .CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
         sqLiteDatabase.execSQL(CREATE_CHEATMSG);
         sqLiteDatabase.execSQL(CREATE_BEANS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
