package com.example.lyw.maomaorobot.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lyw.maomaorobot.greendao.DaoMaster;
import com.lyw.maomaorobot.greendao.DaoSession;
import com.lyw.maomaorobot.greendao.NoteDao;


/**
 * Created by LYW on 2016/8/4.
 */
public class NoteManager {
  public static SQLiteDatabase db;
  public static DaoMaster daoMaster;
  public static DaoSession daoSession;
  public static NoteDao noteDao;
  private static NoteManager noteManager;

   public static String DB_NAME = "note_db";

    private NoteManager() {
    }

    public static NoteManager getIntance() {
        if (noteManager == null) {
            noteManager = new NoteManager();
        }
        return noteManager;
    }

    public  NoteDao initNote(Context context){
        DaoMaster.DevOpenHelper help = new DaoMaster.DevOpenHelper
                (context, "note_db", null);
          db = help.getWritableDatabase();
          daoMaster = new DaoMaster(db);
          daoSession = daoMaster.newSession();
          noteDao = daoSession.getNoteDao();
        return noteDao;
    }

}
