package com.oplus.resolver;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.oplusdevicepolicy.OplusDevicepolicyManager;
import android.util.Log;

/* loaded from: classes.dex */
public class OplusOShareUtil {
    private static final String FROM_PACKAGE = "from_package";
    private static final String TAG = "OplusOShareUtil";

    public static boolean isOsharePackage(Context context, String packageName) {
        if (context == null) {
            return false;
        }
        return context.getString(201588967).equals(packageName);
    }

    public static boolean isNoOshareApplication(Context context, Intent intent) {
        if (!isOshareExist(context)) {
            Log.e(TAG, "isOshareDisabled component is not found");
            return true;
        }
        if (isOshareDisabledByCustomize()) {
            Log.e(TAG, "isOshareDisabled is disabled by customize.");
            return true;
        }
        if (getOshareComponent(context, intent) == null) {
            Log.e(TAG, "isOshareDisabled has not oshareComponent");
            return true;
        }
        return false;
    }

    public static ResolveInfo getOshareComponent(Context context, Intent intent) {
        Intent resolveIntent = getResolveIntent(context, intent);
        ResolveInfo ri = context.getPackageManager().resolveActivity(resolveIntent, 512);
        if (ri != null && ri.activityInfo != null) {
            return ri;
        }
        return null;
    }

    public static Intent getResolveIntent(Context context, Intent intent) {
        Intent resolveIntent = new Intent(intent);
        ComponentName cn2 = new ComponentName(context.getString(201588967), context.getString(201589211));
        resolveIntent.setComponent(cn2);
        return resolveIntent;
    }

    public static void startOshareActivity(Context context, Intent originIntent) {
        Intent grantIntent = new Intent(originIntent);
        grantIntent.setComponent(new ComponentName(context.getString(201588967), context.getString(201588968)));
        grantIntent.addFlags(65536);
        try {
            ((Activity) context).startActivityAsCaller(grantIntent, null, false, -10000);
            Intent intent = getResolveIntent(context, originIntent);
            intent.putExtra(FROM_PACKAGE, context.getPackageName());
            ((Activity) context).startActivityAsCaller(intent, null, false, -10000);
        } catch (Exception e) {
            Log.e(TAG, "oShareClick:" + e.getMessage());
        }
    }

    private static boolean isOshareExist(Context context) {
        try {
            context.getPackageManager().getPackageInfo(context.getString(201588967), 512);
            return true;
        } catch (PackageManager.NameNotFoundException ignore) {
            Log.e(TAG, "isOshareExist has not exist:" + ignore.getMessage());
            return false;
        }
    }

    private static boolean isOshareDisabledByCustomize() {
        return OplusDevicepolicyManager.getInstance().getBoolean("customize_set_oshare_disabled", 1, false);
    }
}
