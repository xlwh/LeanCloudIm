package com.avoscloud.leanchatlib.db;

import android.os.Environment;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by zhangyong on 2015-05-02.
 */
public class MockUtil {
    public static String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final Random r = new Random();


    public static String getText() {
        return "Text " + Math.random() + " Message";
    }

    public static String image() {
        return SD_PATH.concat("/imsend/image/image3.png");
    }

    public static String video() {
        return SD_PATH.concat("/imsend/video/video_20.mp4");
    }

    public static String voice() {
        return SD_PATH.concat("/imsend/voice/voice_91.amr");
    }



    public static int getIndex() {
        return r.nextInt(4);
    }

    public static int getSleepTime() {
        return r.nextInt(70001) + 20000;
    }

    public static boolean isTimeUp() {
        Calendar up = Calendar.getInstance();
        up.set(Calendar.HOUR_OF_DAY, 16);
        up.set(Calendar.MINUTE, 30);

        if (System.currentTimeMillis() > up.getTimeInMillis()) {
            return true;
        }
        return false;
    }

    public static String getString(JSONObject json, String key) {
        String string = null;
        try {
            string = json.getString(key);
        } catch (Exception e) {
            string = "";
        }
        return string;
    }
}
