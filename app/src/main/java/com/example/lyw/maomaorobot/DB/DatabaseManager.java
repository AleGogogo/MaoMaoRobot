package com.example.lyw.maomaorobot.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorJoiner;
import android.util.Log;

import com.example.lyw.maomaorobot.Bean.BaseResponse;
import com.example.lyw.maomaorobot.Bean.CaiPuResponse;
import com.example.lyw.maomaorobot.Bean.CheatMessage;
import com.example.lyw.maomaorobot.Bean.LinkResponse;
import com.example.lyw.maomaorobot.Bean.NewsResponse;
import com.example.lyw.maomaorobot.Bean.TextResponse;
import com.example.lyw.maomaorobot.R;
import com.example.lyw.maomaorobot.info.DBinfo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by LYW on 2016/6/2.
 */
public class DatabaseManager {
    private static final String TAG = "DatabaseManager";

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
     * 将CheatMessage实例存进数据库
     */
    public void saveCheatMsgData(Object o) {
        if (o != null) {
            ContentValues values1 = new ContentValues();
            if (o instanceof CheatMessage) {
                CheatMessage c = (CheatMessage) o;
                values1.put(DBinfo.COLUMN_CHEAT_TYPE, (c.getType()).toString());
                values1.put(DBinfo.COLUMN_CHEAT_MSG, c.getMsg());
                values1.put(DBinfo.COLUMN_CHEAT_DATE, c.getDate());
                cheatOpenHelper.getWritableDatabase().insert(DBinfo
                                .CHEAT_TABLE_NAME,
                        null, values1);
            } else {
                BaseResponse b = (BaseResponse) o;
                //得到现 所在行
                Cursor cursor = cheatOpenHelper.getWritableDatabase()
                        .rawQuery("SELECT COUNT(*) AS NumberOfOrders FROM "
                                + DBinfo.CHEAT_TABLE_NAME, null);
                int lineNums = -1;
                if (cursor.moveToFirst()) {
                    lineNums = cursor.getInt(cursor.getColumnIndex
                            ("NumberOfOrders"));
                    Log.d(TAG, "lineNums[" + lineNums + "]");
                }
                cursor.close();
                switch (b.getCode()) {
                    case BaseResponse.RESPONSE_TYPE_TEXT:
                        TextResponse t = (TextResponse) b;
                        values1.put(DBinfo.COLUMN_CHEAT_CODE, t.getCode());
                        values1.put(DBinfo.COLUMN_CHEAT_DATE, t.getDate());
                        values1.put(DBinfo.COLUMN_CHEAT_TYPE, (t.getmType())
                                .toString());
                        values1.put(DBinfo.COLUMN_CHEAT_MSG, t.getText());
                        cheatOpenHelper.getWritableDatabase().insert(DBinfo
                                        .CHEAT_TABLE_NAME,
                                null, values1);
                        break;
                    case BaseResponse.RESPONSE_TYPE_LINK:
                        LinkResponse l = (LinkResponse) b;
                        values1.put(DBinfo.COLUMN_CHEAT_CODE, l.getCode());
                        values1.put(DBinfo.COLUMN_CHEAT_DATE, l.getDate());
                        values1.put(DBinfo.COLUMN_CHEAT_TYPE, (l.getmType())
                                .toString());
                        values1.put(DBinfo.COLUMN_CHEAT_MSG, l.getText());
                        values1.put(DBinfo.COLUMN_CHEAT_URL, l.getUrl());
                        cheatOpenHelper.getWritableDatabase().insert(DBinfo
                                        .CHEAT_TABLE_NAME,
                                null, values1);
                        break;
                    case BaseResponse.RESPONSE_TYPE_NEWS:
                        NewsResponse n = (NewsResponse) b;
                        List<NewsResponse.ListBean> listBean = n.getList();
                        values1.put(DBinfo.COLUMN_CHEAT_CODE, n.getCode());
                        values1.put(DBinfo.COLUMN_CHEAT_DATE, n.getDate());
                        values1.put(DBinfo.COLUMN_CHEAT_TYPE, (n.getmType())
                                .toString());
                        values1.put(DBinfo.COLUMN_CHEAT_MSG, n.getText());
                        cheatOpenHelper.getWritableDatabase().insert(DBinfo
                                        .CHEAT_TABLE_NAME,
                                null, values1);

                        for (NewsResponse.ListBean bean : listBean) {
                            ContentValues values2 = new ContentValues();
                            values2.put(DBinfo.COLUMN_CHEAT_BEAN_KEY,
                                    lineNums + 1);
                            values2.put(DBinfo.COLUMN_CHEAT_ARTICLE, bean
                                    .getArticle());
                            values2.put(DBinfo.COLUMN_CHEAT_DETAILURL, bean
                                    .getDetailurl());
                            values2.put(DBinfo.COLUMN_CHEAT_ICON, bean
                                    .getIcon());
                            values2.put(DBinfo.COLUMN_CHEAT_SOURCE, bean
                                    .getSource());
                            Log.d("TAG", "value is " + values2.toString());
                            cheatOpenHelper.getWritableDatabase().insert
                                    (DBinfo.CHEAT_TABLE_BEANS,
                                            null, values2);
                        }
                        break;
                    case BaseResponse.RESPONSE_TYPE_CAIPU:
                        CaiPuResponse c = (CaiPuResponse) b;
                        List<CaiPuResponse.ListBean> Beans = c.getList();
                        values1.put(DBinfo.COLUMN_CHEAT_CODE, c.getCode());
                        values1.put(DBinfo.COLUMN_CHEAT_DATE, c.getDate());
                        values1.put(DBinfo.COLUMN_CHEAT_TYPE, (c.getmType())
                                .toString());
                        values1.put(DBinfo.COLUMN_CHEAT_MSG, c.getText());
                        cheatOpenHelper.getWritableDatabase().insert(DBinfo
                                        .CHEAT_TABLE_NAME,
                                null, values1);
                        for (CaiPuResponse.ListBean bean : Beans) {
                            ContentValues values3 = new ContentValues();
                            values3.put(DBinfo.COLUMN_CHEAT_BEAN_KEY,
                                    lineNums + 1);
                            values3.put(DBinfo.COLUMN_CHEAT_ICON, bean
                                    .getIcon());
                            values3.put(DBinfo.COLUMN_CHEAT_DETAILURL, bean
                                    .getDetailurl());
                            values3.put(DBinfo.COLUMN_CHEAT_NAME, bean
                                    .getName());
                            values3.put(DBinfo.COLUMN_CHEAT_INFO, bean
                                    .getInfo());
                            cheatOpenHelper.getWritableDatabase().insert
                                    (DBinfo.CHEAT_TABLE_BEANS,
                                            null, values3);
                            Log.d(TAG, " lineNums["+ lineNums+"]");
                        }
                        break;

                }
            }

        }
    }


    /**
     * 从数据库中取出CheatMessage
     */
    public ArrayList<Object> loadCheatMessage() {
        ArrayList<Object> list = new ArrayList<Object>();
        //SQLiteDatabase 提供的query()会返回一个Cursor对象，
        //查询到的所有数据都将从这个对象中取出
        Cursor cursor = cheatOpenHelper.getReadableDatabase().query(
                DBinfo.CHEAT_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                String messageType = cursor.getString(cursor.getColumnIndex
                        (DBinfo.COLUMN_CHEAT_TYPE));
                if (messageType.equals(CheatMessage.Type.OUTCOMING + "")) {
                    CheatMessage c = new CheatMessage();
                    c.setMsg(cursor.getString(cursor.getColumnIndex(DBinfo
                            .COLUMN_CHEAT_MSG)));
                    c.setDate(cursor.getString(cursor.getColumnIndex(DBinfo
                            .COLUMN_CHEAT_DATE)));
                    list.add(c);
                } else {
                    int code = cursor.getInt(cursor.getColumnIndex(DBinfo
                            .COLUMN_CHEAT_CODE));
                    int messageId = cursor.getInt(cursor
                            .getColumnIndex("id"));
                    Cursor cursor_bean = cheatOpenHelper
                            .getReadableDatabase()
                            .query(DBinfo.CHEAT_TABLE_BEANS,
                                    null,
                                    "bean_key=?",
                                    new String[]{messageId + ""},
                                    null,
                                    null,
                                    null);
                    switch (code) {
                        case BaseResponse.RESPONSE_TYPE_TEXT:
                            TextResponse t = new TextResponse();
                            t.setCode(code);
                            t.setText(cursor.getString(cursor.getColumnIndex(DBinfo.COLUMN_CHEAT_MSG)));
                            t.setmType(BaseResponse.Type.INCOMING);
                            list.add(t);
                            break;
                        case BaseResponse.RESPONSE_TYPE_LINK:
                            LinkResponse l = new LinkResponse();
                            l.setCode(code);
                            l.setText(cursor.getString(cursor.getColumnIndex
                                    (DBinfo.COLUMN_CHEAT_MSG)));
                            l.setmType(BaseResponse.Type.INCOMING);
                            l.setUrl(cursor.getString(cursor.getColumnIndex
                                    (DBinfo.COLUMN_CHEAT_URL)));
                            list.add(l);

                            break;
                        case BaseResponse.RESPONSE_TYPE_NEWS:
                            NewsResponse n = new NewsResponse();
                            List<NewsResponse.ListBean> news = new
                                    ArrayList<>();
                            n.setList(news);
                            n.setCode(code);
                            n.setText(cursor.getString(cursor.getColumnIndex
                                    (DBinfo.COLUMN_CHEAT_MSG)));
                            n.setmType(BaseResponse.Type.INCOMING);
                            //// TODO: 2016/6/13  做笔记


                            if (cursor_bean.moveToFirst()) {
                                while (cursor_bean.moveToNext()) {
                                    String article = cursor_bean.getString
                                            (cursor_bean.getColumnIndex
                                                    (DBinfo.COLUMN_CHEAT_ARTICLE));
                                    String detailUrl = cursor_bean.getString
                                            (cursor_bean.getColumnIndex
                                                    (DBinfo.COLUMN_CHEAT_DETAILURL));
                                    String iconUrl = cursor_bean.getString
                                            (cursor_bean.getColumnIndex
                                                    (DBinfo.COLUMN_CHEAT_ICON));
                                    String source = cursor_bean.getString
                                            (cursor_bean.getColumnIndex
                                                    (DBinfo.COLUMN_CHEAT_SOURCE));
                                    news.add(new NewsResponse.ListBean
                                            (article, source, iconUrl,
                                                    detailUrl));

                                }

                            }
                            list.add(n);

                            break;
                        case BaseResponse.RESPONSE_TYPE_CAIPU:
                            CaiPuResponse c = new CaiPuResponse();
                            List<CaiPuResponse.ListBean> bills = new ArrayList<>();
                            c.setList(bills);
                            c.setCode(code);
                            c.setText(cursor.getString(cursor.getColumnIndex
                                    (DBinfo.COLUMN_CHEAT_MSG)));
                            c.setmType(BaseResponse.Type.INCOMING);

//                            String Command = "SELECT * FROM " + DBinfo
//                                    .CHEAT_TABLE_NAME +
//                                    " LEFT JOIN " + DBinfo.CHEAT_TABLE_BEANS +
//                                    " ON " + DBinfo.CHEAT_TABLE_NAME + "." +
//                                    DBinfo.COLUMN_CHEAT_ID +
//                                    "=" + DBinfo.CHEAT_TABLE_BEANS + "." +
//                                    DBinfo.COLUMN_CHEAT_BEAN_KEY;
                            if (cursor_bean.moveToFirst()) {
                                while (cursor_bean.moveToNext()){
                                        String name = cursor_bean.getString
                                                (cursor_bean.getColumnIndex
                                                        (DBinfo.COLUMN_CHEAT_NAME));
                                        String info = cursor_bean.getString
                                                (cursor_bean.getColumnIndex
                                                        (DBinfo.COLUMN_CHEAT_INFO));
                                        String detailUrl = cursor_bean.getString
                                                (cursor_bean.getColumnIndex
                                                        (DBinfo.COLUMN_CHEAT_DETAILURL));
                                        String icon = cursor_bean.getString
                                                (cursor_bean.getColumnIndex
                                                        (DBinfo.COLUMN_CHEAT_ICON));
                                        bills.add(new CaiPuResponse.ListBean(name,icon,
                                                info,detailUrl));
                                    }

                                }
                            list.add(c);
                            break;
                    }
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }
//
//
//    /**
//     * 从数据库读出retrunMsg数据
//     */
//    public ArrayList<ReturnMessage> laodReturnMsg()throws
// NullPointerException{
//         ArrayList<ReturnMessage> list =new ArrayList<ReturnMessage>();
//       Cursor cursor = cheatOpenHelper.getReadableDatabase().query(DBinfo
// .RETRUN_TABLE_NAME,
//               null,
//               DBinfo.COLUMN_CHEAT_TYPE+"=?",
//               new String[]{ReturnMessage.Type.INCOMING+""},
//               null,
//               null,
//               null);
//        if (cursor == null){
//            return null;
//        } else if (cursor.moveToFirst()){
//            do {
//                  ReturnMessage r = new ReturnMessage();
//                r.setmMsg(cursor.getString(cursor.getColumnIndex(DBinfo
// .COLUMN_RETRUN_MSG)));
//                r.setmCode(cursor.getInt(cursor.getColumnIndex(DBinfo
// .COLUMN_RETRUN_CODE)));
//                r.setmDate(cursor.getString(cursor.getColumnIndex(DBinfo
// .COLUMN_RETURN_DATE)));
//                if (cursor.getString(cursor.getColumnIndex(DBinfo
// .COLUMN_RETRUN_URL))!=null){
//                    ((LinkMsg)r).setUrl(cursor.getString(cursor
// .getColumnIndex(DBinfo.COLUMN_RETRUN_URL)));
//                }else if (cursor.getString(cursor.getColumnIndex(DBinfo
// .COLUMN_RETRUN_DETAILURL))
//                        !=null){
//                    ((NewsMsg)r).setUrl(cursor.getString(cursor
// .getColumnIndex(DBinfo.COLUMN_RETRUN_DETAILURL)));
//                }
//                    list.add(r);
//
//            }while (cursor.moveToNext());
//
//        }
//        return list;
//    }
}
