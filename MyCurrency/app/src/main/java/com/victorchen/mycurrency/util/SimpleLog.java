package com.victorchen.mycurrency.util;

import android.util.Log;

/**
 * Log utility whose log level can be configured
 */
public final class SimpleLog {
    private static LogLevel sLogLevel = LogLevel.ERROR;
    private static String TAG = "SimpleLog";

    /**
     * Sets the minimum log level
     *
     * @param level {@link LogLevel} minimum level to start logging
     */
    public static void setLogLevel(LogLevel level) {
        sLogLevel = level;
    }

    /**
     * Sets the global tag to use if no tag is specified in log method
     *
     * @param tag global tag to use
     */
    public static void setGlobalTag(String tag) {
        TAG = tag;
    }

    public static void logV(String tag, String message, Throwable t) {
        if (sLogLevel.ordinal() <= LogLevel.VERBOSE.ordinal()) {
            Log.v(tag != null ? tag : TAG, message + (t != null ? "\n" + Log.getStackTraceString(t) : ""));
        }
    }

    public static void logV(String tag, String message) {
        logV(tag, message, null);
    }

    public static void logV(String message, Throwable t) {
        logV(null, message, t);
    }

    public static void logV(String message) {
        logV(null, message, null);
    }

    public static void logD(String tag, String message, Throwable t) {
        if (sLogLevel.ordinal() <= LogLevel.DEBUG.ordinal()) {
            Log.d(tag != null ? tag : TAG, message + (t != null ? "\n" + Log.getStackTraceString(t) : ""));
        }
    }

    public static void logD(String tag, String message) {
        logD(tag, message, null);
    }

    public static void logD(String message, Throwable t) {
        logD(null, message, t);
    }

    public static void logD(String message) {
        logD(null, message, null);
    }

    public static void logI(String tag, String message, Throwable t) {
        if (sLogLevel.ordinal() <= LogLevel.INFO.ordinal()) {
            Log.i(tag != null ? tag : TAG, message + (t != null ? "\n" + Log.getStackTraceString(t) : ""));
        }
    }

    public static void logI(String tag, String message) {
        logI(tag, message, null);
    }

    public static void logI(String message, Throwable t) {
        logI(null, message, t);
    }

    public static void logI(String message) {
        logI(null, message, null);
    }

    public static void logW(String tag, String message, Throwable t) {
        if (sLogLevel.ordinal() <= LogLevel.WARNING.ordinal()) {
            Log.w(tag != null ? tag : TAG, message + (t != null ? "\n" + Log.getStackTraceString(t) : ""));
        }
    }

    public static void logW(String tag, String message) {
        logW(tag, message, null);
    }

    public static void logW(String message, Throwable t) {
        logW(null, message, t);
    }

    public static void logW(String message) {
        logW(null, message, null);
    }

    public static void logE(String tag, String message, Throwable t) {
        if (sLogLevel.ordinal() <= LogLevel.ERROR.ordinal()) {
            Log.e(tag != null ? tag : TAG, message + (t != null ? "\n" + Log.getStackTraceString(t) : ""));
        }
    }

    public static void logE(String tag, String message) {
        logE(tag, message, null);
    }

    public static void logE(String message, Throwable t) {
        logE(null, message, t);
    }

    public static void logE(String message) {
        logE(null, message, null);
    }

    public enum LogLevel {
        VERBOSE,
        DEBUG,
        INFO,
        WARNING,
        ERROR
    }

}
