package com.avoscloud.leanchatlib.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by zhangyong on 2015/4/8.
 */
public class MessageDao {

    private static MessageDao messageDao;
    private SQLiteDatabase db;

    public static final String TABLE_NAME = "imdata";
    public static final String C_ID = "id";
    public static final String C_MESSAGE = "mes";
    public static final String C_MSG_TYPE = "mesType";
    public static final String C_MSG_ID = "messageId";
    public static final String C_MSG_TIME = "recTime";
    public static final String C_DIRECT = "actionType";
    public static final String C_FUSER = "fromUser";
    public static final String C_TUSER = "toUser";
    public static final String C_SUCCESS = "success";
    public static final String C_MSG_TIME2 = "recTime2";
    public static final String C_OFFON = "offOn";
    public static final String C_DATA_TYPE = "dataType";
    public static final String C_CHANNEL = "channel";
    public static final String C_UUID = "uuid";
    public static final String C_PRODUCT = "product";
    public static final String C_BRAND = "phoneCaption";
    public static final String C_REGAIN_TIME = "reSendTimes";
    public static final String C_SEND_TIME = "sendTime";
    public static final String C_SEND_TIME2 = "sendTime2";


    public static final String MIPAD_MODEL = "MI PAD";
    public static final int TOTAL_RESEND_TIMES = 10;


    private MessageDao(Context context) {
        db = MyOpenHelper.getInstance(context).getWritableDatabase();
    }

    public static MessageDao getInstance(Context context) {
        if (messageDao == null) {
            messageDao = new MessageDao(context);
        }
        return messageDao;
    }

    public void insert(Map<String, Object> map) {
        ContentValues values = new ContentValues();
        values.put(C_MESSAGE, String.valueOf(map.get(C_MESSAGE)));
        values.put(C_MSG_TYPE, String.valueOf(map.get(C_MSG_TYPE)));
        values.put(C_MSG_ID, String.valueOf(map.get(C_MSG_ID)));
        values.put(C_MSG_TIME, String.valueOf(map.get(C_MSG_TIME)));
        values.put(C_DIRECT, String.valueOf(map.get(C_DIRECT)));
        values.put(C_FUSER, String.valueOf(map.get(C_FUSER)));
        values.put(C_TUSER, String.valueOf(map.get(C_TUSER)));

        values.put(C_MSG_TIME2, String.valueOf(map.get(C_MSG_TIME2)));
        if (!empty(map.get(C_OFFON))) {
            values.put(C_OFFON, String.valueOf(map.get(C_OFFON)));
        }
        if (!empty(map.get(C_SUCCESS))) {
            values.put(C_SUCCESS, String.valueOf(map.get(C_SUCCESS)));
        }
        values.put(C_DATA_TYPE, String.valueOf(map.get(C_DATA_TYPE)));

        // 平板取不到imei值
        if (MIPAD_MODEL.equals(Build.MODEL)) {
            values.put(C_UUID, "100");
        }else{
            values.put(C_UUID, String.valueOf(map.get(C_UUID)));
        }
        values.put(C_BRAND, Build.BRAND + Build.MODEL);

        db.insert(TABLE_NAME, null, values);
    }


    public void updateSuccess(String msgid, String success) {
        ContentValues values = new ContentValues();
        values.put(C_SUCCESS, success);
        db.update(TABLE_NAME, values, C_MSG_ID.concat(" = ?"), new String[]{msgid});
    }

    public int getRetryTimes(String msgid) {
        int time = 0;
        Cursor cursor = db.rawQuery("select " + C_REGAIN_TIME + " from " + TABLE_NAME + " where " + C_MSG_ID + " = ?", new String[]{msgid});
        if (cursor != null && cursor.moveToFirst()) {
            try {
                time = cursor.getInt(cursor.getColumnIndex(C_REGAIN_TIME));
                return time;
            } catch (Exception e) { }
        }
        return time;
    }

    public void updateRetryTimes(String msgid) {
        db.execSQL("update " + TABLE_NAME + " set " + C_REGAIN_TIME + " = " + C_REGAIN_TIME + " + 1," + C_SUCCESS + "= 'FALSE' where " + C_MSG_ID + " = ?", new Object[]{msgid});
    }

    public void updateMsgIdTimeSuccess(String oldMsgid, String newMsgid, String success) {
        db.execSQL("update " + TABLE_NAME + " set " + C_MSG_ID + " = ? " + ", " + C_REGAIN_TIME + " = " + C_REGAIN_TIME + " + 1, " + C_SUCCESS + " = ? where " + C_MSG_ID + " = ?",
                new Object[]{newMsgid, success, oldMsgid});
    }

    public void updateSuccessTime(String msgid, String time, String time2) {
        ContentValues values = new ContentValues();
        values.put(C_SEND_TIME, time);
        values.put(C_SEND_TIME2, time2);

        db.update(TABLE_NAME, values, C_MSG_ID + " = ?", new String[]{msgid});
    }

    public boolean empty(Object object) {
        if (object == null || "".equals(object.toString())) {
            return true;
        }
        return false;
    }

    public JSONArray getAll() {
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        JSONArray array = new JSONArray();
        if (cursor == null || cursor.getCount() == 0) {
            return array;
        }
        JSONObject obj;
        while (cursor.moveToNext()) {
            try {
                obj = new JSONObject();
                obj.put(C_ID, cursor.getString(cursor.getColumnIndex(C_ID)));
                obj.put(C_MESSAGE, cursor.getString(cursor.getColumnIndex(C_MESSAGE)));
                obj.put(C_MSG_TYPE, cursor.getString(cursor.getColumnIndex(C_MSG_TYPE)));
                obj.put(C_MSG_ID, cursor.getString(cursor.getColumnIndex(C_MSG_ID)));
                obj.put(C_MSG_TIME, cursor.getString(cursor.getColumnIndex(C_MSG_TIME)));
                obj.put(C_DIRECT, cursor.getString(cursor.getColumnIndex(C_DIRECT)));
                obj.put(C_FUSER, cursor.getString(cursor.getColumnIndex(C_FUSER)));
                obj.put(C_TUSER, cursor.getString(cursor.getColumnIndex(C_TUSER)));
                obj.put(C_SUCCESS, cursor.getString(cursor.getColumnIndex(C_SUCCESS)));
                obj.put(C_MSG_TIME2, cursor.getString(cursor.getColumnIndex(C_MSG_TIME2)));
                obj.put(C_OFFON, cursor.getString(cursor.getColumnIndex(C_OFFON)));
                obj.put(C_DATA_TYPE, cursor.getString(cursor.getColumnIndex(C_DATA_TYPE)));
                obj.put(C_CHANNEL, cursor.getString(cursor.getColumnIndex(C_CHANNEL)));
                obj.put(C_UUID, cursor.getString(cursor.getColumnIndex(C_UUID)));
                obj.put(C_PRODUCT, cursor.getString(cursor.getColumnIndex(C_PRODUCT)));
                obj.put(C_BRAND, cursor.getString(cursor.getColumnIndex(C_BRAND)));
                obj.put(C_REGAIN_TIME, cursor.getString(cursor.getColumnIndex(C_REGAIN_TIME)));
                obj.put(C_SEND_TIME, cursor.getString(cursor.getColumnIndex(C_SEND_TIME)));
                obj.put(C_SEND_TIME2, cursor.getString(cursor.getColumnIndex(C_SEND_TIME2)));

                array.put(obj);

            } catch (Exception e) { }
        }
        cursor.close();

        return array;
    }

    public void deleteAll() {
        db.delete(TABLE_NAME, null, null);
    }
}