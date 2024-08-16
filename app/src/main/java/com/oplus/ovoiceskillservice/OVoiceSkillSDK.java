package com.oplus.ovoiceskillservice;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.List;

/* loaded from: classes.dex */
public class OVoiceSkillSDK {
    private static final String TAG = "OVSS.OVoiceSkillSDK";

    public static void deinitialize() {
        Log.d(TAG, "deinitialize");
        OVoiceSkillProxy.getInstance().deinitialize();
    }

    public static long getVersionCode(Context context) {
        Log.d(TAG, "getVersionCode");
        return OVoiceSkillProxy.getVersionCode(context);
    }

    public static String getVersionName(Context context) {
        Log.d(TAG, "getVersionName");
        return OVoiceSkillProxy.getVersionName(context);
    }

    public static boolean initialize(Context context) {
        return initialize(context, null);
    }

    public static boolean newSkillSession(Intent intent, SkillActionListener skillActionListener) {
        return OVoiceSkillProxy.getInstance().newSkillSession(intent, skillActionListener);
    }

    public static boolean registerActionExecutionCallback(String str, SkillActionListener skillActionListener) {
        Log.d(TAG, String.format("registerActionExecutionCallback actionID[%s]", str));
        return OVoiceSkillProxy.getInstance().registerActionExecutionCallback(str, skillActionListener);
    }

    public static boolean unregisterActionExecutionCallback(String str) {
        Log.d(TAG, String.format("unregisterActionExecutionCallback, actionID[%s]", str));
        return OVoiceSkillProxy.getInstance().unregisterActionExecutionCallback(str);
    }

    public static boolean initialize(Context context, OVoiceConnectionCallback oVoiceConnectionCallback) {
        Log.d(TAG, "initialize");
        return OVoiceSkillProxy.getInstance().initialize(context, oVoiceConnectionCallback);
    }

    public static boolean registerActionExecutionCallback(List<String> list, SkillActionListener skillActionListener) {
        Log.d(TAG, String.format("registerActionExecutionCallback[%d]", Integer.valueOf(list.size())));
        return OVoiceSkillProxy.getInstance().registerActionExecutionCallback(list, skillActionListener);
    }

    public static boolean unregisterActionExecutionCallback(List<String> list) {
        Log.d(TAG, String.format("unregisterActionExecutionCallback[%d]", Integer.valueOf(list.size())));
        return OVoiceSkillProxy.getInstance().unregisterActionExecutionCallback(list);
    }

    public static boolean unregisterActionExecutionCallback() {
        Log.d(TAG, "unregisterActionExecutionCallback");
        return OVoiceSkillProxy.getInstance().unregisterActionExecutionCallback();
    }
}
