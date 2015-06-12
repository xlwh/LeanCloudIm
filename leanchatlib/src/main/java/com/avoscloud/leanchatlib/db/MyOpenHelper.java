package com.avoscloud.leanchatlib.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import static com.avoscloud.leanchatlib.db.MessageDao.*;

/**
 * Created by zhangyong on 2015/4/8.
 */
public class MyOpenHelper extends SQLiteOpenHelper {
    private static MyOpenHelper instance;

    private static final String CHAT_MSG_TABLE_CREATE = "create table if not exists " + TABLE_NAME + "(" +
            C_ID + " integer primary key autoincrement," +
            C_MESSAGE + " varchar(100) default ''," +
            C_MSG_TYPE + " varchar(50) default '' ," +
            C_MSG_ID + " varchar(100) default '' ," +
            C_MSG_TIME + " varchar(20) default ''," +
            C_DIRECT + " varchar(50) default ''," +
            C_FUSER + " varchar(50) default '' ," +
            C_TUSER + " varchar(50) default ''," +
            C_SUCCESS + " varchar(50) default 'TRUE'," +
            C_MSG_TIME2 + " varchar(20) default ''," +
            C_OFFON + " varchar(50) default 'ON'," +
            C_DATA_TYPE + " varchar(50) default ''," +
            C_CHANNEL + " varchar(50) default 'ANDROID'," +
            C_UUID + " varchar(50) default ''," +
            C_PRODUCT + " varchar(50) default 'HX'," +
            C_BRAND + " varchar(50)," +
            C_REGAIN_TIME + " integer default 0," +
            C_SEND_TIME + " integer," +
            C_SEND_TIME2 + " datetime)";


    public MyOpenHelper(Context context) {
        super(context, Environment.getExternalStorageDirectory().getAbsolutePath()+"/LeanCloud/imdata.db", null, 1);
    }


    public static MyOpenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new MyOpenHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CHAT_MSG_TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
