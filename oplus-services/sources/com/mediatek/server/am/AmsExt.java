package com.mediatek.server.am;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import com.android.server.am.ProcessRecord;
import com.android.server.wm.ActivityRecord;
import java.io.PrintWriter;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class AmsExt {
    public static final int COLLECT_PSS_FG_MSG = 2;

    public boolean IsBuildInApp() {
        return true;
    }

    public void addDuraSpeedService() {
    }

    public void addToSuppressRestartList(String str) {
    }

    public void enableAmsLog(PrintWriter printWriter, String[] strArr, int i, ArrayList<ProcessRecord> arrayList) {
    }

    public void enableAmsLog(ArrayList<ProcessRecord> arrayList) {
    }

    public void enableMtkAmsLog() {
    }

    public void enableProcessMainThreadLooperLog(PrintWriter printWriter, String[] strArr, int i, ArrayList<ProcessRecord> arrayList) {
    }

    public boolean notRemoveAlarm(String str) {
        return false;
    }

    public void onActivityStateChanged(ActivityRecord activityRecord, boolean z) {
    }

    public void onAddErrorToDropBox(String str, String str2, int i) {
    }

    public void onAfterActivityResumed(ActivityRecord activityRecord) {
    }

    public void onAppProcessDied(Context context, ProcessRecord processRecord, ApplicationInfo applicationInfo, int i, ArrayList<ProcessRecord> arrayList, String str) {
    }

    public void onBeforeActivitySwitch(ActivityRecord activityRecord, ActivityRecord activityRecord2, boolean z, int i, boolean z2) {
    }

    public boolean onBeforeStartProcessForStaticReceiver(String str) {
        return false;
    }

    public void onEndOfActivityIdle(Context context, ActivityRecord activityRecord) {
    }

    public void onNotifyAppCrash(int i, int i2, String str) {
    }

    public String onReadyToStartComponent(String str, int i, String str2, String str3) {
        return null;
    }

    public void onStartProcess(String str, String str2) {
    }

    public void onSystemReady(Context context) {
    }

    public void onWakefulnessChanged(int i) {
    }

    public boolean preLaunchApplication(String str, Intent intent, String str2, int i) {
        return false;
    }

    public void startDuraSpeedService(Context context) {
    }
}
