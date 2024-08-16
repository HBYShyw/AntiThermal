package com.android.server.am;

import android.R;
import android.app.BroadcastOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.os.Binder;
import android.os.BugreportManager;
import android.os.BugreportParams;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Slog;
import com.android.server.SystemConfig;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class BugReportHandlerUtil {
    private static final String INTENT_BUGREPORT_REQUESTED = "com.android.internal.intent.action.BUGREPORT_REQUESTED";
    private static final String INTENT_GET_BUGREPORT_HANDLER_RESPONSE = "com.android.internal.intent.action.GET_BUGREPORT_HANDLER_RESPONSE";
    private static final String SHELL_APP_PACKAGE = "com.android.shell";
    private static final String TAG = "ActivityManager";

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isBugReportHandlerEnabled(Context context) {
        return context.getResources().getBoolean(R.bool.config_defaultWindowFeatureOptionsPanel);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean launchBugReportHandlerApp(Context context) {
        if (!isBugReportHandlerEnabled(context)) {
            return false;
        }
        String customBugReportHandlerApp = getCustomBugReportHandlerApp(context);
        if (isShellApp(customBugReportHandlerApp)) {
            return false;
        }
        int customBugReportHandlerUser = getCustomBugReportHandlerUser(context);
        if (!isValidBugReportHandlerApp(customBugReportHandlerApp)) {
            customBugReportHandlerApp = getDefaultBugReportHandlerApp(context);
            customBugReportHandlerUser = context.getUserId();
        } else if (getBugReportHandlerAppReceivers(context, customBugReportHandlerApp, customBugReportHandlerUser).isEmpty()) {
            customBugReportHandlerApp = getDefaultBugReportHandlerApp(context);
            customBugReportHandlerUser = context.getUserId();
            resetCustomBugreportHandlerAppAndUser(context);
        }
        if (isShellApp(customBugReportHandlerApp) || !isValidBugReportHandlerApp(customBugReportHandlerApp) || getBugReportHandlerAppReceivers(context, customBugReportHandlerApp, customBugReportHandlerUser).isEmpty()) {
            return false;
        }
        if (getBugReportHandlerAppResponseReceivers(context, customBugReportHandlerApp, customBugReportHandlerUser).isEmpty()) {
            launchBugReportHandlerApp(context, customBugReportHandlerApp, customBugReportHandlerUser);
            return true;
        }
        Slog.i("ActivityManager", "Getting response from bug report handler app: " + customBugReportHandlerApp);
        Intent intent = new Intent(INTENT_GET_BUGREPORT_HANDLER_RESPONSE);
        intent.setPackage(customBugReportHandlerApp);
        intent.addFlags(AudioFormat.EVRC);
        intent.addFlags(16777216);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            context.sendOrderedBroadcastAsUser(intent, UserHandle.of(customBugReportHandlerUser), "android.permission.DUMP", -1, null, new BugreportHandlerResponseBroadcastReceiver(customBugReportHandlerApp, customBugReportHandlerUser), null, 0, null, null);
            return true;
        } catch (RuntimeException e) {
            Slog.e("ActivityManager", "Error while trying to get response from bug report handler app.", e);
            return false;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void launchBugReportHandlerApp(Context context, String str, int i) {
        Slog.i("ActivityManager", "Launching bug report handler app: " + str);
        Intent intent = new Intent(INTENT_BUGREPORT_REQUESTED);
        intent.setPackage(str);
        intent.addFlags(AudioFormat.EVRC);
        intent.addFlags(16777216);
        BroadcastOptions makeBasic = BroadcastOptions.makeBasic();
        makeBasic.setBackgroundActivityStartsAllowed(true);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                context.sendBroadcastAsUser(intent, UserHandle.of(i), "android.permission.DUMP", makeBasic.toBundle());
            } catch (RuntimeException e) {
                Slog.e("ActivityManager", "Error while trying to launch bugreport handler app.", e);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private static String getCustomBugReportHandlerApp(Context context) {
        return Settings.Secure.getStringForUser(context.getContentResolver(), "custom_bugreport_handler_app", context.getUserId());
    }

    private static int getCustomBugReportHandlerUser(Context context) {
        return Settings.Secure.getIntForUser(context.getContentResolver(), "custom_bugreport_handler_user", -10000, context.getUserId());
    }

    private static boolean isShellApp(String str) {
        return SHELL_APP_PACKAGE.equals(str);
    }

    private static boolean isValidBugReportHandlerApp(String str) {
        return !TextUtils.isEmpty(str) && isBugreportWhitelistedApp(str);
    }

    private static boolean isBugreportWhitelistedApp(String str) {
        return SystemConfig.getInstance().getBugreportWhitelistedPackages().contains(str);
    }

    private static List<ResolveInfo> getBugReportHandlerAppReceivers(Context context, String str, int i) {
        Intent intent = new Intent(INTENT_BUGREPORT_REQUESTED);
        intent.setPackage(str);
        return context.getPackageManager().queryBroadcastReceiversAsUser(intent, AudioDevice.OUT_FM, i);
    }

    private static List<ResolveInfo> getBugReportHandlerAppResponseReceivers(Context context, String str, int i) {
        Intent intent = new Intent(INTENT_GET_BUGREPORT_HANDLER_RESPONSE);
        intent.setPackage(str);
        return context.getPackageManager().queryBroadcastReceiversAsUser(intent, AudioDevice.OUT_FM, i);
    }

    private static String getDefaultBugReportHandlerApp(Context context) {
        return context.getResources().getString(R.string.config_misprovisionedDeviceModel);
    }

    private static void resetCustomBugreportHandlerAppAndUser(Context context) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Settings.Secure.putString(context.getContentResolver(), "custom_bugreport_handler_app", getDefaultBugReportHandlerApp(context));
            Settings.Secure.putInt(context.getContentResolver(), "custom_bugreport_handler_user", context.getUserId());
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private static class BugreportHandlerResponseBroadcastReceiver extends BroadcastReceiver {
        private final String handlerApp;
        private final int handlerUser;

        BugreportHandlerResponseBroadcastReceiver(String str, int i) {
            this.handlerApp = str;
            this.handlerUser = i;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (getResultCode() == -1) {
                BugReportHandlerUtil.launchBugReportHandlerApp(context, this.handlerApp, this.handlerUser);
            } else {
                Slog.w("ActivityManager", "Request bug report because no response from handler app.");
                ((BugreportManager) context.getSystemService(BugreportManager.class)).requestBugreport(new BugreportParams(1), null, null);
            }
        }
    }
}
