package com.zhangchong.libutils;

import android.os.Environment;
import android.os.Process;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;

public class LogHelper {
    public static final String TAG = "tools";
    private static final boolean DEBUG_TRACE = true;
    private static final String LOG_FILE_PATH = Environment.getExternalStorageDirectory()
            + "/Android/data/com.zhangchong.toolsapplication/log.txt";
    private static final long MAX_LOG_FILE_SIZE = 4L * 1024 * 1024;
    private final static boolean WRITE_LOG_FILE = true;
    private static final boolean mWriteSdcard = true;

    private static final HashMap<Integer, Long> mLastLongTimes = new HashMap<Integer, Long>(20);

    private static boolean isLogEnable() {
        return true;
    }

    public static void logD(String tag, String msg) {
        if (isLogEnable()) {
            Log.d(tag, msg);
        }
    }

    public static void logD( String msg) {
        if (isLogEnable()) {
            Log.d(TAG, msg);
        }
    }

    public static void logD(String tag, String msg, Throwable tr) {
        if (isLogEnable()) {
            Log.d(tag, msg, tr);
        }
    }

    public static void logT(String tag, String msg) {
        if (DEBUG_TRACE && isLogEnable()) {
            Log.d(tag, msg);
        }
    }

    public static void logI(String tag, String msg) {
        if (isLogEnable()) {
            Log.i(tag, msg);
        }
    }

    public static void logE(String tag, String msg) {
        Log.e(tag, msg);
        writeLogFile(tag, msg);
    }

    public static void logE(String tag, String msg, Exception e) {
        Log.e(tag, msg, e);
        writeLogFile(tag, msg + e.getMessage());
    }

    public static void logW(String tag, String msg) {
        Log.w(tag, msg);
        writeLogFile(tag, msg);
    }

    public static void logW(String tag, String msg, Exception e) {
        Log.w(tag, msg, e);
        writeLogFile(tag, msg + e.getMessage());
    }

    private static String currentTimeToString() {
        return DateFormat.format("yyyy-MM-dd kk:mm", System.currentTimeMillis()).toString();
    }

    private static String currentRawTimeToString() {
        int tid = Process.myTid();
        long lastTime = 0;
        if (mLastLongTimes.containsKey(tid)) {
            lastTime = mLastLongTimes.get(tid);
        }
        long cur = System.currentTimeMillis();
        mLastLongTimes.put(tid, cur);

        return String.valueOf(cur) + " , tid: " + tid + " , delta: " + (cur - lastTime);
    }

    private static void writeLogFile(String tag, String log) {
        if (WRITE_LOG_FILE && mWriteSdcard) {
            writeSingleLog(tag, log, LOG_FILE_PATH, MAX_LOG_FILE_SIZE);
        }
    }

    public static void writeLogFile(Throwable ex) {
        if (WRITE_LOG_FILE && mWriteSdcard) {
            writeException(ex, LOG_FILE_PATH, MAX_LOG_FILE_SIZE);
        }
    }

    private static void writeSingleLog(String tag, String log, String filePath, long maxSize) {
        String logString = "" + currentRawTimeToString() + "  " + tag + " : " + log + "\n";
        writeLogToFile(logString, filePath, maxSize);
    }

    private static void writeException(Throwable ex, String filePath, long maxSize) {
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);

        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        String logString = info.toString();
        printWriter.close();

        StringBuilder builder = new StringBuilder();
        builder.append("----------------------------------------\n");
        builder.append("[" + currentTimeToString() + "]\n");
        builder.append(logString);
        builder.append("\n\n");
        writeLogToFile(builder.toString(), filePath, maxSize);
    }

    private static void writeLogToFile(String logString, String filePath, long maxSize) {
        try {
            File file = new File(filePath);
            File dir = file.getParentFile();
            if (dir != null && !dir.exists()) {
                dir.mkdirs();
            }
            if (file.exists() && file.length() >= maxSize) {
                file.delete();
            }
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    return;
                }
            }
            FileOutputStream out = new FileOutputStream(file, true);
            OutputStreamWriter writer = new OutputStreamWriter(out);
            writer.write(logString);
            writer.flush();
            out.flush();
            writer.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
