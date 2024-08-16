package com.oplus.deepsleep.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import b6.LocalLog;
import java.util.ArrayList;
import java.util.HashSet;

/* loaded from: classes.dex */
public class DeepSleepSharepref {
    public static final String DEEP_SLEEP_SHAREPREF_FILE = "DeepSleepSharepref";
    private static final String TAG = "DeepSleepSharepref";
    private static SharedPreferences sSp;

    public static boolean getBooleanValue(Context context, String str, boolean z10) {
        isInit(context);
        try {
            return sSp.getBoolean(str, z10);
        } catch (Exception e10) {
            LocalLog.b("DeepSleepSharepref", "getBooleanValue() Exception: " + e10);
            return z10;
        }
    }

    public static int getIntValue(Context context, String str, int i10) {
        isInit(context);
        try {
            return sSp.getInt(str, i10);
        } catch (Exception e10) {
            LocalLog.b("DeepSleepSharepref", "getIntValue() Exception: " + e10);
            return i10;
        }
    }

    public static long getLongValue(Context context, String str, long j10) {
        isInit(context);
        try {
            return sSp.getLong(str, j10);
        } catch (Exception e10) {
            LocalLog.b("DeepSleepSharepref", "getLongValue() Exception: " + e10);
            return j10;
        }
    }

    public static String getStringValue(Context context, String str, String str2) {
        isInit(context);
        try {
            return sSp.getString(str, str2);
        } catch (Exception e10) {
            LocalLog.b("DeepSleepSharepref", "getStringValue() Exception: " + e10);
            return str2;
        }
    }

    @SuppressLint({"CommitPrefEdits"})
    public static void insertXML(Context context, String str, ArrayList<String> arrayList) {
        isInit(context);
        HashSet hashSet = new HashSet();
        hashSet.addAll(arrayList);
        sSp.edit().putStringSet(str, hashSet).apply();
    }

    private static void isInit(Context context) {
        if (sSp == null) {
            sSp = context.getSharedPreferences("DeepSleepSharepref", 0);
        }
    }

    @SuppressLint({"CommitPrefEdits"})
    public static void insertXML(Context context, String str, String str2) {
        isInit(context);
        SharedPreferences.Editor edit = sSp.edit();
        edit.putString(str, str2);
        edit.apply();
    }

    @SuppressLint({"CommitPrefEdits"})
    public static void insertXML(Context context, String str, boolean z10) {
        isInit(context);
        SharedPreferences.Editor edit = sSp.edit();
        edit.putBoolean(str, z10);
        edit.apply();
    }

    @SuppressLint({"CommitPrefEdits"})
    public static void insertXML(Context context, String str, int i10) {
        isInit(context);
        SharedPreferences.Editor edit = sSp.edit();
        edit.putInt(str, i10);
        edit.apply();
    }

    @SuppressLint({"CommitPrefEdits"})
    public static void insertXML(Context context, String str, long j10) {
        isInit(context);
        SharedPreferences.Editor edit = sSp.edit();
        edit.putLong(str, j10);
        edit.apply();
    }
}
