package com.oplus.icon;

import android.app.UxIconPackageManagerExt;
import android.content.ComponentName;
import android.content.pm.IPackageManagerExt;
import android.content.pm.PackageManager;
import android.content.res.IUxIconPackageManagerExt;
import android.graphics.drawable.Drawable;
import android.util.Log;

/* loaded from: classes.dex */
public class OplusUxIconManager {
    private static final String TAG = "OPlusUxIconPackageManager";

    public static Drawable getUxIconDrawable(PackageManager pm, String packageName, Drawable src, boolean isForegroundDrawable) {
        if (pm == null) {
            Log.e(TAG, "getUxIconDrawable: PackageManager is null");
            return src;
        }
        if (packageName == null) {
            Log.e(TAG, "getUxIconDrawable: packageName is null");
            return src;
        }
        IPackageManagerExt packageManagerExt = pm.mPackageManagerExt;
        if (packageManagerExt == null) {
            Log.e(TAG, "getUxIconDrawable: packageMangerExt is null");
            return src;
        }
        Object iconPackageManager = packageManagerExt.getUxIconPackageManagerExt();
        if (iconPackageManager instanceof IUxIconPackageManagerExt) {
            return ((UxIconPackageManagerExt) iconPackageManager).getUxIconDrawable(packageName, src, isForegroundDrawable);
        }
        return src;
    }

    public static void clearCachedIconForActivity(PackageManager pm, ComponentName activityName) {
        if (pm == null) {
            Log.e(TAG, "clearCachedIconForActivity: PackageManager is null");
            return;
        }
        IPackageManagerExt packageManagerExt = pm.mPackageManagerExt;
        if (packageManagerExt == null) {
            Log.e(TAG, "clearCachedIconForActivity: packageMangerExt is null");
            return;
        }
        Object iconPackageManager = packageManagerExt.getUxIconPackageManagerExt();
        if (iconPackageManager instanceof IUxIconPackageManagerExt) {
            try {
                ((UxIconPackageManagerExt) iconPackageManager).clearCachedIconForActivity(activityName);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "clearCachedIconForActivity: PackageManager.NameNotFoundException:" + activityName);
            }
        }
    }
}
