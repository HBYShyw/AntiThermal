package com.android.internal.telephony.util;

import android.content.Context;
import android.content.res.OplusThemeResources;
import android.os.SystemClock;
import android.telephony.Rlog;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

/* loaded from: classes.dex */
public class OplusManagerHelper {
    private static final String TAG = "OplusManagerHelper";

    public static int writeLogToPartition(Context context, String resName, String logstring, String issue) {
        String string = getOemRes(context, resName, "");
        if (string.equals("")) {
            Rlog.e(TAG, "Can not get resource of identifier for : " + resName);
            return -1;
        }
        String[] log_array = string.split(",");
        int log_type = Integer.valueOf(log_array[0]).intValue();
        String log_desc = log_array[1];
        return writeLogToPartition(log_type, logstring, issue, log_desc);
    }

    public static String getOemRes(Context context, String resName, String defValue) {
        try {
            return context.getString(context.getResources().getIdentifier(resName, "string", OplusThemeResources.OPLUS_PACKAGE));
        } catch (Exception e) {
            return defValue;
        }
    }

    public static int writeLogToPartition(int type, String logstring, String issue, String desc) {
        try {
            Class<?> OplusManager = Class.forName("android.os.OplusManager");
            Field field = OplusManager.getDeclaredField("NETWORK_TAG");
            field.setAccessible(true);
            Object obj = field.get(OplusManager);
            if (obj == null) {
                return -1;
            }
            String NETWORK_TAG = obj.toString();
            int result = writeLogToPartition(type, logstring, NETWORK_TAG, issue, desc);
            return result;
        } catch (Exception e) {
            Log.i(TAG, "OplusManager writeLogToPartition" + e.getMessage());
            return -1;
        }
    }

    public static int writeLogToPartition(int type, String logstring, String tagString, String issue, String desc) {
        try {
            Class<?> OplusManager = Class.forName("android.os.OplusManager");
            Method writeLogToPartition = OplusManager.getMethod("writeLogToPartition", Integer.TYPE, String.class, String.class, String.class, String.class);
            Object obj = writeLogToPartition.invoke(null, Integer.valueOf(type), logstring, tagString, issue, desc);
            if (obj == null) {
                return -1;
            }
            int result = ((Integer) obj).intValue();
            return result;
        } catch (Exception e) {
            Log.i(TAG, "OplusManager writeLogToPartition" + e.getMessage());
            return -1;
        }
    }

    public static void onStamp(String eventId, Map<String, String> logMap) {
        try {
            Class<?> OplusManager = Class.forName("android.os.OplusManager");
            Method readCriticalData = OplusManager.getMethod("onStamp", String.class, Map.class);
            readCriticalData.invoke(null, eventId, logMap);
        } catch (Exception e) {
            Log.i(TAG, "OplusManager ClassNotFoundException" + e.getMessage());
        }
    }

    public static boolean limitLog(ArrayList<Long> list, long limitPeriod, int limitCount, boolean debug, String tag) {
        try {
            synchronized (list) {
                long begin = SystemClock.elapsedRealtime() - limitPeriod;
                while (!list.isEmpty() && list.get(0).longValue() < begin) {
                    list.remove(0);
                }
                int size = list.size();
                if (debug) {
                    Rlog.d(tag, "limitLog, cursize=" + size);
                }
                if (size >= limitCount) {
                    if (debug) {
                        Rlog.d(tag, "limitLog,limitCount=" + limitCount);
                    }
                    return true;
                }
                list.add(Long.valueOf(SystemClock.elapsedRealtime()));
                return false;
            }
        } catch (Exception e) {
            Rlog.e(tag, "limitLog " + e.getMessage());
            return true;
        }
    }
}
