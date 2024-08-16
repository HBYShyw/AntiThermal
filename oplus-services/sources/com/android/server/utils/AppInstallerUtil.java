package com.android.server.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.util.Log;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class AppInstallerUtil {
    private static final String LOG_TAG = "AppInstallerUtil";

    private static Intent resolveIntent(Context context, Intent intent) {
        ResolveInfo resolveActivity = context.getPackageManager().resolveActivity(intent, 0);
        if (resolveActivity == null) {
            return null;
        }
        Intent intent2 = new Intent(intent.getAction());
        ActivityInfo activityInfo = resolveActivity.activityInfo;
        return intent2.setClassName(activityInfo.packageName, activityInfo.name);
    }

    public static String getInstallerPackageName(Context context, String str) {
        String str2;
        try {
            str2 = context.getPackageManager().getInstallerPackageName(str);
        } catch (IllegalArgumentException e) {
            Log.e(LOG_TAG, "Exception while retrieving the package installer of " + str, e);
            str2 = null;
        }
        if (str2 == null) {
            return null;
        }
        return str2;
    }

    public static Intent createIntent(Context context, String str, String str2) {
        Intent resolveIntent = resolveIntent(context, new Intent("android.intent.action.SHOW_APP_INFO").setPackage(str));
        if (resolveIntent == null) {
            return null;
        }
        resolveIntent.putExtra("android.intent.extra.PACKAGE_NAME", str2);
        resolveIntent.addFlags(268435456);
        return resolveIntent;
    }

    public static Intent createIntent(Context context, String str) {
        return createIntent(context, getInstallerPackageName(context, str), str);
    }
}
