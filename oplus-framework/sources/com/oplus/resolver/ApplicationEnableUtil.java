package com.oplus.resolver;

import android.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.util.Log;

/* loaded from: classes.dex */
public class ApplicationEnableUtil {
    private static final String TAG = "ApplicationEnableUtil";

    private static void setApplicationEnable(PackageManager packageManager, String packageName) {
        try {
            packageManager.setApplicationEnabledSetting(packageName, 1, 1);
        } catch (Exception e) {
            Log.d(TAG, "setApplicationEnable: error:" + e.getMessage());
        }
    }

    public static boolean applicationEnable(PackageManager packageManager, String packageName) {
        try {
            int enableSetting = packageManager.getApplicationEnabledSetting(packageName);
            return enableSetting == 1 || enableSetting == 0;
        } catch (Exception e) {
            Log.d(TAG, "applicationEnable: error:" + e.getMessage());
            return true;
        }
    }

    public static boolean showApplicationEnableDialog(Context context, final String packageName, final DialogInterface.OnClickListener callback) {
        final PackageManager packageManager = context.getPackageManager();
        try {
            CharSequence appName = packageManager.getApplicationInfo(packageName, 0).loadLabel(packageManager);
            Dialog dialog = new AlertDialog.Builder(context).setTitle(context.getString(201588974, appName)).setMessage(context.getString(201588975, appName, context.getString(R.string.autofill_area))).setPositiveButton(201588976, new DialogInterface.OnClickListener() { // from class: com.oplus.resolver.ApplicationEnableUtil$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    ApplicationEnableUtil.lambda$showApplicationEnableDialog$0(packageManager, packageName, callback, dialogInterface, i);
                }
            }).setNegativeButton(201588797, new DialogInterface.OnClickListener() { // from class: com.oplus.resolver.ApplicationEnableUtil$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).create();
            dialog.setCancelable(false);
            dialog.show();
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$showApplicationEnableDialog$0(PackageManager packageManager, String packageName, DialogInterface.OnClickListener callback, DialogInterface dialog1, int which) {
        dialog1.dismiss();
        setApplicationEnable(packageManager, packageName);
        callback.onClick(dialog1, which);
    }
}
