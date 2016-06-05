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
          + DBinfo.COLUMN_CHEAT_TYPE+" text, "
          + DBinfo.COLUMN_CHEAT_DATE+" text, "
          + DBinfo.COLUMN_CHEAT_MSG+" text)";

   public static final String CREATE_RETURNMSG= "create table "+ DBinfo
           .RETRUN_TABLE_NAME+ "("
           + DBinfo.COLUMN_RETRUN_MSG +" text, "
           + DBinfo.COLUMN_RETRUN_TYPE +" text, "
           + DBinfo.COLUMN_RETURN_DATE + " text, "
           + DBinfo.COLUMN_RETRUN_DETAILURL +" text, "
           + DBinfo.COLUMN_RETRUN_URL +" text, "
           + DBinfo.COLUMN_RETRUN_CODE +" integer )";
    private Context mContext;

    public CheatOpenHelper(Context context, String name, SQLiteDatabase
            .CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
         sqLiteDatabase.execSQL(CREATE_CHEATMSG);
         sqLiteDatabase.execSQL(CREATE_RETURNMSG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
