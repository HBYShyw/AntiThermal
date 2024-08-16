package com.oplus.util;

import android.content.Context;
import android.content.Intent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class OplusKillMethodProvider {
    private static final String ACTION_CLEAR_SPEC_APP = "oplus.intent.action.REQUEST_CLEAR_SPEC_APP";
    private static final String ACTION_ONEKEY_CLEAR = "oplus.intent.action.REQUEST_APP_CLEAN_RUNNING";
    private static final String CALLED_PACKAGE = "com.oplus.athena";
    private static final String CALLER_PACKAGE = "caller_package";
    private static final String CALLER_PACKAGE_THEIA_UITIMEOUT_KILL = "android.theia_UITimeout_kill";
    private static final String FILTER_APP_LIST = "filterapplist";
    private static final String LIST = "list";
    private static final String PID = "pid";
    private static final String P_NAME = "p_name";
    private static final String REASON = "reason";
    private static final int REQUEST_TYPE_KILL = 12;
    private static final int REQUEST_TYPE_KILL_OR_STOP = 11;
    private static final int REQUEST_TYPE_REMOVE_TASK = 14;
    private static final int REQUEST_TYPE_STOP = 13;
    private static final String TAG = "OplusKillMethodProvider";
    private static final String TYPE = "type";

    public static void killProcess(Context context, int i, String str, String str2) {
        try {
            Intent intent = new Intent(ACTION_CLEAR_SPEC_APP);
            intent.setPackage(CALLED_PACKAGE);
            intent.putExtra(PID, i);
            intent.putExtra(P_NAME, str);
            intent.putExtra(CALLER_PACKAGE, CALLER_PACKAGE_THEIA_UITIMEOUT_KILL);
            intent.putExtra(REASON, str2);
            intent.putExtra(TYPE, 12);
            context.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void killPackage(Context context, String str, String str2) {
        try {
            Intent intent = new Intent(ACTION_CLEAR_SPEC_APP);
            intent.setPackage(CALLED_PACKAGE);
            intent.putExtra(P_NAME, str);
            intent.putExtra(CALLER_PACKAGE, CALLER_PACKAGE_THEIA_UITIMEOUT_KILL);
            intent.putExtra(REASON, str2);
            intent.putExtra(TYPE, 12);
            context.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void killProcessList(Context context, List<KillInfo> list, String str) {
        if (list == null || list.isEmpty()) {
            return;
        }
        try {
            ArrayList<String> arrayList = new ArrayList<>();
            Iterator<KillInfo> it = list.iterator();
            while (it.hasNext()) {
                arrayList.add(it.next().getInfoString());
            }
            Intent intent = new Intent(ACTION_CLEAR_SPEC_APP);
            intent.setPackage(CALLED_PACKAGE);
            intent.putStringArrayListExtra(LIST, arrayList);
            intent.putExtra(CALLER_PACKAGE, CALLER_PACKAGE_THEIA_UITIMEOUT_KILL);
            intent.putExtra(REASON, str);
            intent.putExtra(TYPE, 12);
            context.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopPackageConfig(Context context, List<String> list, String str) {
        try {
            ArrayList arrayList = new ArrayList(list);
            Intent intent = new Intent(ACTION_ONEKEY_CLEAR);
            intent.setPackage(CALLED_PACKAGE);
            intent.putExtra(CALLER_PACKAGE, context.getPackageName());
            intent.putExtra(FILTER_APP_LIST, arrayList);
            intent.putExtra(REASON, str);
            context.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class KillInfo {
        public int pid;
        public String procName;

        public String toString() {
            return getInfoString();
        }

        String getInfoString() {
            return this.pid + "|" + this.procName;
        }
    }
}
