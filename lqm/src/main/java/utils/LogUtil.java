package utils;

import android.util.Log;

/**
 * Created by Hack on 2016/1/13.
 */
public class LogUtil {

    private static String Tag = "--lqm--";

//    private LogUtil() {
//    }
//
//    private static LogUtil logUtil = null;
//
//    public static LogUtil log() {
//        if (null == logUtil)
//            logUtil = new LogUtil();
//        return logUtil;
//    }

    public static void wtf(String tag, String msg) {
        Log.wtf(tag, msg);
    }

    public static void wtf(String msg) {
        Log.wtf(Tag, msg);
    }

    public static void wtf(Object msg) {
        Log.w(Tag, "" + msg);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void e(String msg) {
        Log.e(Tag, msg);
    }

    public static void w(String tag, String msg) {
        Log.w(tag, msg);
    }

    public static void w(Object msg) {
        Log.w(Tag, "" + msg);
    }

    public static void w(String msg) {
        Log.w(Tag, msg);
    }

}
