package com.oplus.deepsleep;

import android.app.ActivityManager;
import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Process;
import android.provider.Settings;
import android.text.TextUtils;
import b6.LocalLog;
import com.oplus.battery.R;
import f6.CommonUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import y5.b;

/* loaded from: classes.dex */
public class SuperSleepStatsHelper {
    private static final String TAG = "SuperSleepStatsHelper";
    private static SuperSleepStatsHelper mInstance;
    private Context mContext;
    private String mPip = null;
    private DeepSleepUtils mUtils;

    public SuperSleepStatsHelper(Context context) {
        this.mContext = context;
        this.mUtils = DeepSleepUtils.getInstance(context);
    }

    private boolean containKeyWord(String str) {
        if (b.D()) {
            String[] stringArray = this.mContext.getResources().getStringArray(R.array.super_sleep_key_word);
            if (stringArray.length < 1) {
                return true;
            }
            for (String str2 : stringArray) {
                if (str.toLowerCase().contains(str2)) {
                    return true;
                }
            }
            ArrayList<String> N = CommonUtil.N();
            if (N != null && !N.isEmpty()) {
                Iterator<String> it = N.iterator();
                while (it.hasNext()) {
                    String next = it.next();
                    LocalLog.a(TAG, "containKeyWord app =" + next);
                    if (str.equalsIgnoreCase(next)) {
                        return true;
                    }
                }
            } else if (b.D()) {
                String[] stringArray2 = this.mContext.getResources().getStringArray(R.array.super_sleep_white_list);
                if (stringArray2.length < 1) {
                    return true;
                }
                for (String str3 : stringArray2) {
                    if (str.toLowerCase().contains(str3)) {
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }

    private List<String> getDefaultSkipApps(Context context) {
        int indexOf;
        WallpaperInfo wallpaperInfo;
        ArrayList arrayList = new ArrayList();
        for (ResolveInfo resolveInfo : context.getPackageManager().queryIntentServices(new Intent("oplus.intent.action.keyguard"), 128)) {
            LocalLog.a(TAG, "keyguard app =" + resolveInfo.serviceInfo.packageName);
            arrayList.add(resolveInfo.serviceInfo.packageName);
        }
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        for (ResolveInfo resolveInfo2 : context.getPackageManager().queryIntentServices(intent, 0)) {
            LocalLog.a(TAG, "launchers app =" + resolveInfo2.serviceInfo.packageName);
            arrayList.add(resolveInfo2.serviceInfo.packageName);
        }
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        if (wallpaperManager != null && (wallpaperInfo = wallpaperManager.getWallpaperInfo()) != null) {
            LocalLog.a(TAG, "WallpaperInfo app =" + wallpaperInfo.getPackageName());
            arrayList.add(wallpaperInfo.getPackageName());
        }
        String stringForUser = Settings.Secure.getStringForUser(context.getContentResolver(), "default_input_method", Process.myUserHandle().hashCode());
        if (!TextUtils.isEmpty(stringForUser) && (indexOf = stringForUser.indexOf("/")) != -1) {
            arrayList.add(stringForUser.substring(0, indexOf));
        }
        String str = this.mPip;
        if (str != null) {
            arrayList.add(str);
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            LocalLog.a(TAG, "getDefaultSkipApps app =" + ((String) it.next()));
        }
        return arrayList;
    }

    public static synchronized SuperSleepStatsHelper getInstance(Context context) {
        SuperSleepStatsHelper superSleepStatsHelper;
        synchronized (SuperSleepStatsHelper.class) {
            if (mInstance == null) {
                mInstance = new SuperSleepStatsHelper(context);
            }
            superSleepStatsHelper = mInstance;
        }
        return superSleepStatsHelper;
    }

    public void batteryCurveOptimize(int i10) {
        LocalLog.b(TAG, "batteryCurveOptimize status =" + i10);
        if (b.D()) {
            try {
                Class<?> cls = Class.forName("android.os.OplusBatteryManager");
                LocalLog.a(TAG, "nightstandby: " + ((Integer) cls.getMethod("nightstandby", Integer.TYPE).invoke(cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]), Integer.valueOf(i10))).intValue());
            } catch (Exception e10) {
                e10.printStackTrace();
                LocalLog.b(TAG, "batteryCurveOptimize Exception");
            }
        }
    }

    public void forcestopApps() {
        ActivityManager activityManager;
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses;
        int i10;
        if (CommonUtil.M() || (runningAppProcesses = (activityManager = (ActivityManager) this.mContext.getSystemService("activity")).getRunningAppProcesses()) == null || runningAppProcesses.isEmpty()) {
            return;
        }
        ArrayList arrayList = (ArrayList) getDefaultSkipApps(this.mContext);
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            String str = runningAppProcessInfo.processName;
            try {
                i10 = this.mContext.getPackageManager().getApplicationInfo(str, 0).flags;
            } catch (Exception unused) {
                LocalLog.b(TAG, "forcestopApps cannot get packageinfo :" + str);
            }
            if ((i10 & 1) == 0 && (i10 & 128) == 0) {
                int i11 = runningAppProcessInfo.importance;
                if (i11 != 100 && i11 != 300) {
                    if (runningAppProcessInfo.uid < 10000) {
                        LocalLog.a(TAG, "forcestopApps skip system2: uid =" + runningAppProcessInfo.uid + "pgkName =" + str);
                    } else if (containKeyWord(str)) {
                        LocalLog.a(TAG, "forcestopApps static skip: " + str);
                    } else if (arrayList != null && arrayList.contains(str)) {
                        LocalLog.a(TAG, "forcestopApps dynamic skip: " + str);
                    } else {
                        LocalLog.b(TAG, "forcestopApps package :" + str);
                        activityManager.forceStopPackage(str);
                    }
                }
                LocalLog.a(TAG, "forcestopApps skip processinfo.importance: " + str + "processinfo.importance =" + runningAppProcessInfo.importance);
            }
            LocalLog.a(TAG, "forcestopApps skip system1: " + str);
        }
    }

    public boolean isSupportBatteryOptimize() {
        if (!b.D()) {
            return false;
        }
        try {
            Class<?> cls = Class.forName("android.os.OplusBatteryManager");
            cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            cls.getMethod("nightstandby", Integer.TYPE);
            LocalLog.a(TAG, "nightstandby method exists");
            return true;
        } catch (Exception e10) {
            e10.printStackTrace();
            LocalLog.b(TAG, "isSupportBatteryOptimize Exception");
            return false;
        }
    }
}
