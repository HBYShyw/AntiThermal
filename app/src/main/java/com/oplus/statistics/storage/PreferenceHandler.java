package com.oplus.statistics.storage;

import android.content.Context;

/* loaded from: classes2.dex */
public class PreferenceHandler {
    public static final String ACTIVITY_END_TIME = "activity.end.time";
    public static final String ACTIVITY_START_TIME = "activity.start.time";
    public static final String CURRENT_ACTIVITY = "current.activity";
    public static final String EVENT_START = "event.start";
    public static final String KVEVENT_START = "kv.start";
    public static final String PAGEVISIT_DURATION = "pagevisit.duration";
    public static final String PAGEVISIT_ROUTES = "pagevisit.routes";
    public static final String SESSION_TIMEOUT = "session.timeout";
    public static final int SESSION_TIMEOUT_DEFAULT = 30;
    public static final String SSOID = "ssoid";
    private static final MemoryPreference sMemoryPref = new MemoryPreference();

    public static long getActivityEndTime(Context context) {
        return sMemoryPref.getLong(ACTIVITY_END_TIME, -1L);
    }

    public static long getActivityStartTime(Context context) {
        return sMemoryPref.getLong(ACTIVITY_START_TIME, -1L);
    }

    public static String getCurrentActivity(Context context) {
        return sMemoryPref.getString(CURRENT_ACTIVITY, "");
    }

    public static long getEventStart(Context context, String str, String str2) {
        return sMemoryPref.getLong(EVENT_START + str + "_" + str2, 0L);
    }

    public static String getKVEventStart(Context context, String str, String str2) {
        return sMemoryPref.getString(KVEVENT_START + str + "_" + str2, "");
    }

    public static long getLong(Context context, String str, long j10) {
        return sMemoryPref.getLong(str, j10);
    }

    public static int getPageVisitDuration(Context context) {
        return sMemoryPref.getInt(PAGEVISIT_DURATION, 0);
    }

    public static String getPageVisitRoutes(Context context) {
        return sMemoryPref.getString(PAGEVISIT_ROUTES, "");
    }

    public static int getSessionTimeout(Context context) {
        return sMemoryPref.getInt(SESSION_TIMEOUT, 30);
    }

    public static String getSsoID(Context context) {
        return sMemoryPref.getString(SSOID, "0");
    }

    public static String getString(Context context, String str, String str2) {
        return sMemoryPref.getString(str, str2);
    }

    public static void setActivityEndTime(Context context, long j10) {
        sMemoryPref.setLong(ACTIVITY_END_TIME, j10);
    }

    public static void setActivityStartTime(Context context, long j10) {
        sMemoryPref.setLong(ACTIVITY_START_TIME, j10);
    }

    public static void setCurrentActivity(Context context, String str) {
        sMemoryPref.setString(CURRENT_ACTIVITY, str);
    }

    public static void setEventStart(Context context, String str, String str2, long j10) {
        sMemoryPref.setLong(EVENT_START + str + "_" + str2, j10);
    }

    public static void setKVEventStart(String str, String str2, String str3) {
        sMemoryPref.setString(KVEVENT_START + str + "_" + str3, str2);
    }

    public static void setLong(Context context, String str, long j10) {
        sMemoryPref.setLong(str, j10);
    }

    public static void setPageVisitDuration(Context context, int i10) {
        sMemoryPref.setInt(PAGEVISIT_DURATION, i10);
    }

    public static void setPageVisitRoutes(Context context, String str) {
        sMemoryPref.setString(PAGEVISIT_ROUTES, "");
    }

    public static void setSessionTimeout(Context context, int i10) {
        sMemoryPref.setInt(SESSION_TIMEOUT, i10);
    }

    public static void setSsoID(Context context, String str) {
        sMemoryPref.setString(SSOID, str);
    }

    public static void setString(Context context, String str, String str2) {
        sMemoryPref.setString(str, str2);
    }

    public static void setSsoID(Context context) {
        sMemoryPref.setString(SSOID, "0");
    }
}
