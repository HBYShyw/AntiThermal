package k7;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.util.Log;

/* compiled from: AppUtils.java */
/* loaded from: classes.dex */
public class a {
    public static String a(Context context) {
        return b(context, Binder.getCallingPid());
    }

    private static String b(Context context, int i10) {
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            return null;
        }
        String str = "";
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
            try {
                if (runningAppProcessInfo.pid == i10) {
                    Log.d("OVSS.AppUtils", "Process Id: " + runningAppProcessInfo.pid + " ProcessName: " + runningAppProcessInfo.processName + "  Label: " + packageManager.getApplicationLabel(packageManager.getApplicationInfo(runningAppProcessInfo.processName, 128)).toString());
                    str = runningAppProcessInfo.processName;
                }
            } catch (Exception unused) {
            }
        }
        return str;
    }

    private static String c(Context context, int i10) {
        String[] packagesForUid;
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null || (packagesForUid = packageManager.getPackagesForUid(i10)) == null || packagesForUid.length == 0) {
            return null;
        }
        Log.d("OVSS.AppUtils", "Package Name: " + packagesForUid[0]);
        return packagesForUid[0];
    }

    public static PackageInfo d(Context context) {
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            return null;
        }
        try {
            return packageManager.getPackageInfo("com.oplus.ovoicemanager", 0);
        } catch (PackageManager.NameNotFoundException e10) {
            e10.printStackTrace();
            return null;
        }
    }

    public static String e(Context context) {
        return c(context, Binder.getCallingUid());
    }
}
