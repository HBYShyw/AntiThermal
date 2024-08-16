package com.android.server.power;

import android.R;
import android.app.ActivityManagerInternal;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.IActivityManager;
import android.app.ProgressDialog;
import android.app.admin.SecurityLog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IIntentReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManagerInternal;
import android.media.AudioAttributes;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.os.PowerManager;
import android.os.RecoverySystem;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.SystemVibrator;
import android.os.UserManager;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Slog;
import android.util.TimingsTraceLog;
import com.android.server.LocalServices;
import com.android.server.RescueParty;
import com.android.server.display.util.OplusDisplayPanelFeatureHelper;
import com.android.server.job.controllers.JobStatus;
import com.android.server.power.IShutdownThreadExt;
import com.android.server.sensorprivacy.SensorPrivacyService;
import com.android.server.statusbar.StatusBarManagerInternal;
import com.android.server.tare.AlarmManagerEconomicPolicy;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.BiFunction;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ShutdownThread extends Thread {
    private static final int ACTION_DONE_POLL_WAIT_MS = 500;
    private static final int ACTIVITY_MANAGER_STOP_PERCENT = 4;
    private static final int BROADCAST_STOP_PERCENT = 2;
    private static final String CHECK_POINTS_FILE_BASENAME = "/data/system/shutdown-checkpoints/checkpoints";
    private static final boolean DEBUG = false;
    private static final int MAX_BROADCAST_TIME = 5000;
    private static final int MAX_CHECK_POINTS_DUMP_WAIT_TIME = 10000;
    private static final int MAX_RADIO_WAIT_TIME = 3000;
    private static final int MAX_UNCRYPT_WAIT_TIME = 900000;
    private static final String METRICS_FILE_BASENAME = "/data/system/shutdown-metrics";
    private static final int MOUNT_SERVICE_STOP_PERCENT = 20;
    private static final int PACKAGE_MANAGER_STOP_PERCENT = 6;
    private static final int RADIOS_STATE_POLL_SLEEP_MS = 100;
    private static final int RADIO_STOP_PERCENT = 18;
    public static final String REBOOT_SAFEMODE_PROPERTY = "persist.sys.safemode";
    public static final String RO_SAFEMODE_PROPERTY = "ro.sys.safemode";
    public static final String SHUTDOWN_ACTION_PROPERTY = "sys.shutdown.requested";
    private static final int SHUTDOWN_VIBRATE_MS = 500;
    private static final String TAG = "ShutdownThread";
    protected static String mReason = null;
    protected static boolean mReboot = false;
    protected static boolean mRebootHasProgressBar = false;
    protected static boolean mRebootSafeMode = false;
    private static AlertDialog sConfirmDialog = null;
    private static boolean sIsStarted = false;
    private boolean mActionDone;
    protected Context mContext;
    private PowerManager.WakeLock mCpuWakeLock;
    protected Handler mHandler;
    protected PowerManager mPowerManager;
    private ProgressDialog mProgressDialog;
    protected PowerManager.WakeLock mScreenWakeLock;
    private static final Object sIsStartedGuard = new Object();
    protected static final ShutdownThread sInstance = new ShutdownThread();
    private static final AudioAttributes VIBRATION_ATTRIBUTES = new AudioAttributes.Builder().setContentType(4).setUsage(13).build();
    private static final ArrayMap<String, Long> TRON_METRICS = new ArrayMap<>();
    private static String METRIC_SYSTEM_SERVER = "shutdown_system_server";
    private static String METRIC_SEND_BROADCAST = "shutdown_send_shutdown_broadcast";
    private static String METRIC_AM = "shutdown_activity_manager";
    private static String METRIC_PM = "shutdown_package_manager";
    private static String METRIC_RADIOS = "shutdown_radios";
    private static String METRIC_RADIO = "shutdown_radio";
    private static String METRIC_SHUTDOWN_TIME_START = "begin_shutdown";
    private static IShutdownThreadExt.IStaticExt mShutdownThreadStaticExt = (IShutdownThreadExt.IStaticExt) ExtLoader.type(IShutdownThreadExt.IStaticExt.class).create();
    private final Object mActionDoneSync = new Object();
    private IShutdownThreadExt mShutdownThreadExt = (IShutdownThreadExt) ExtLoader.type(IShutdownThreadExt.class).base(this).create();

    /* renamed from: -$$Nest$smnewTimingsLog, reason: not valid java name */
    static /* bridge */ /* synthetic */ TimingsTraceLog m2767$$Nest$smnewTimingsLog() {
        return newTimingsLog();
    }

    protected void mLowLevelShutdownSeq(Context context) {
    }

    protected void mShutdownSeqFinish(Context context) {
    }

    protected boolean mStartShutdownSeq(Context context, boolean z) {
        return true;
    }

    public static void shutdown(Context context, String str, boolean z) {
        if (mShutdownThreadStaticExt.interceptShutdown(context, str)) {
            return;
        }
        mReboot = false;
        mRebootSafeMode = false;
        mReason = str;
        shutdownInner(context, z);
    }

    private static void shutdownInner(final Context context, boolean z) {
        context.assertRuntimeOverlayThemable();
        synchronized (sIsStartedGuard) {
            if (sIsStarted) {
                return;
            }
            ShutdownCheckPoints.recordCheckPoint(null);
            int i = mRebootSafeMode ? R.string.whichOpenHostLinksWith : context.getResources().getInteger(R.integer.config_ntpTimeout) == 2 ? 17041629 : 17041628;
            if (z) {
                CloseDialogReceiver closeDialogReceiver = new CloseDialogReceiver(context);
                AlertDialog alertDialog = sConfirmDialog;
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
                AlertDialog create = new AlertDialog.Builder(context).setTitle(mRebootSafeMode ? R.string.whichOpenHostLinksWithApp : R.string.wfcSpnFormat_wifi_calling_bar_spn).setMessage(i).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() { // from class: com.android.server.power.ShutdownThread.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        ShutdownThread.beginShutdownSequence(context);
                    }
                }).setNegativeButton(R.string.no, (DialogInterface.OnClickListener) null).create();
                sConfirmDialog = create;
                closeDialogReceiver.dialog = create;
                create.setOnDismissListener(closeDialogReceiver);
                sConfirmDialog.getWindow().setType(2009);
                sConfirmDialog.show();
                return;
            }
            beginShutdownSequence(context);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class CloseDialogReceiver extends BroadcastReceiver implements DialogInterface.OnDismissListener {
        public Dialog dialog;
        private Context mContext;

        CloseDialogReceiver(Context context) {
            this.mContext = context;
            context.registerReceiver(this, new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS"), 2);
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            this.dialog.cancel();
        }

        @Override // android.content.DialogInterface.OnDismissListener
        public void onDismiss(DialogInterface dialogInterface) {
            this.mContext.unregisterReceiver(this);
        }
    }

    public static void reboot(Context context, String str, boolean z) {
        if (mShutdownThreadStaticExt.interceptReboot(context, str)) {
            return;
        }
        mReboot = true;
        mRebootSafeMode = false;
        mRebootHasProgressBar = false;
        mReason = str;
        shutdownInner(context, z);
    }

    public static void rebootSafeMode(Context context, boolean z) {
        if (mShutdownThreadStaticExt.interceptReboot(context, "") || ((UserManager) context.getSystemService("user")).hasUserRestriction("no_safe_boot")) {
            return;
        }
        mReboot = true;
        mRebootSafeMode = true;
        mRebootHasProgressBar = false;
        mReason = null;
        shutdownInner(context, z);
    }

    private static ProgressDialog showShutdownDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        String str = mReason;
        if (str != null && str.startsWith("recovery-update")) {
            mRebootHasProgressBar = RecoverySystem.UNCRYPT_PACKAGE_FILE.exists() && !RecoverySystem.BLOCK_MAP_FILE.exists();
            progressDialog.setTitle(context.getText(R.string.whichSendToApplication));
            if (mRebootHasProgressBar) {
                progressDialog.setMax(100);
                progressDialog.setProgress(0);
                progressDialog.setIndeterminate(false);
                progressDialog.setProgressNumberFormat(null);
                progressDialog.setProgressStyle(1);
                progressDialog.setMessage(context.getText(R.string.whichSendApplicationLabel));
            } else {
                if (showSysuiReboot()) {
                    return null;
                }
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage(context.getText(R.string.whichSendApplicationNamed));
            }
        } else {
            String str2 = mReason;
            if (str2 != null && str2.equals("recovery")) {
                if (RescueParty.isAttemptingFactoryReset()) {
                    progressDialog.setTitle(context.getText(R.string.wfcSpnFormat_wifi_calling_bar_spn));
                    progressDialog.setMessage(context.getText(17041630));
                    progressDialog.setIndeterminate(true);
                } else {
                    if (showSysuiReboot()) {
                        return null;
                    }
                    progressDialog.setTitle(context.getText(R.string.whichOpenLinksWithApp));
                    progressDialog.setMessage(context.getText(R.string.whichOpenLinksWith));
                    progressDialog.setIndeterminate(true);
                }
            } else {
                if (showSysuiReboot()) {
                    return null;
                }
                progressDialog.setTitle(context.getText(R.string.wfcSpnFormat_wifi_calling_bar_spn));
                progressDialog.setMessage(context.getText(17041630));
                progressDialog.setIndeterminate(true);
            }
        }
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setType(2009);
        return progressDialog;
    }

    private static boolean showSysuiReboot() {
        if (mShutdownThreadStaticExt.hasFeatureOriginalShutdownAnimation()) {
            String str = mReason;
            if (str != null && str.equals("silence")) {
                Log.d(TAG, "silence reboot case,SysUI is unavailable");
                return false;
            }
            try {
                if (((StatusBarManagerInternal) LocalServices.getService(StatusBarManagerInternal.class)).showShutdownUi(mReboot, mReason)) {
                    return true;
                }
            } catch (Exception unused) {
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void beginShutdownSequence(Context context) {
        synchronized (sIsStartedGuard) {
            if (sIsStarted) {
                return;
            }
            sIsStarted = true;
            ShutdownThread shutdownThread = sInstance;
            shutdownThread.mProgressDialog = showShutdownDialog(context);
            shutdownThread.mContext = context;
            PowerManager powerManager = (PowerManager) context.getSystemService("power");
            shutdownThread.mPowerManager = powerManager;
            shutdownThread.mCpuWakeLock = null;
            try {
                PowerManager.WakeLock newWakeLock = powerManager.newWakeLock(1, "ShutdownThread-cpu");
                shutdownThread.mCpuWakeLock = newWakeLock;
                newWakeLock.setReferenceCounted(false);
                shutdownThread.mCpuWakeLock.acquire();
            } catch (SecurityException e) {
                Log.w(TAG, "No permission to acquire wake lock", e);
                sInstance.mCpuWakeLock = null;
            }
            ShutdownThread shutdownThread2 = sInstance;
            shutdownThread2.mScreenWakeLock = null;
            if (shutdownThread2.mPowerManager.isScreenOn()) {
                try {
                    PowerManager.WakeLock newWakeLock2 = shutdownThread2.mPowerManager.newWakeLock(26, "ShutdownThread-screen");
                    shutdownThread2.mScreenWakeLock = newWakeLock2;
                    newWakeLock2.setReferenceCounted(false);
                    shutdownThread2.mScreenWakeLock.acquire();
                } catch (SecurityException e2) {
                    Log.w(TAG, "No permission to acquire wake lock", e2);
                    sInstance.mScreenWakeLock = null;
                }
            }
            mShutdownThreadStaticExt.beginShutdownSequence(context);
            if (SecurityLog.isLoggingEnabled()) {
                SecurityLog.writeEvent(210010, new Object[0]);
            }
            ShutdownThread shutdownThread3 = sInstance;
            shutdownThread3.mHandler = new Handler() { // from class: com.android.server.power.ShutdownThread.2
            };
            shutdownThread3.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void actionDone() {
        synchronized (this.mActionDoneSync) {
            this.mActionDone = true;
            this.mActionDoneSync.notifyAll();
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        TimingsTraceLog newTimingsLog = newTimingsLog();
        newTimingsLog.traceBegin("SystemServerShutdown");
        metricShutdownStart();
        metricStarted(METRIC_SYSTEM_SERVER);
        mShutdownThreadStaticExt.doShutdownDetect("40");
        Thread newDumpThread = ShutdownCheckPoints.newDumpThread(new File(CHECK_POINTS_FILE_BASENAME));
        newDumpThread.start();
        StringBuilder sb = new StringBuilder();
        sb.append(mReboot ? "1" : "0");
        String str = mReason;
        if (str == null) {
            str = "";
        }
        sb.append(str);
        SystemProperties.set(SHUTDOWN_ACTION_PROPERTY, sb.toString());
        if (mRebootSafeMode) {
            SystemProperties.set(REBOOT_SAFEMODE_PROPERTY, "1");
        }
        newTimingsLog.traceBegin("DumpPreRebootInfo");
        try {
            Slog.i(TAG, "Logging pre-reboot information...");
            PreRebootLogger.log(this.mContext);
        } catch (Exception e) {
            Slog.e(TAG, "Failed to log pre-reboot information", e);
        }
        newTimingsLog.traceEnd();
        metricStarted(METRIC_SEND_BROADCAST);
        newTimingsLog.traceBegin("SendShutdownBroadcast");
        Log.i(TAG, "Sending shutdown broadcast...");
        this.mShutdownThreadExt.checkShutdownTimeout(this.mContext, mReboot, mReason, SensorPrivacyService.REMINDER_DIALOG_DELAY_MILLIS, VIBRATION_ATTRIBUTES);
        this.mActionDone = false;
        Intent intent = new Intent("android.intent.action.ACTION_SHUTDOWN");
        intent.addFlags(AlarmManagerEconomicPolicy.ACTION_ALARM_WAKEUP_EXACT_ALLOW_WHILE_IDLE);
        ((ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class)).broadcastIntentWithCallback(intent, new IIntentReceiver.Stub() { // from class: com.android.server.power.ShutdownThread.3
            public void performReceive(Intent intent2, int i, String str2, Bundle bundle, boolean z, boolean z2, int i2) {
                final ShutdownThread shutdownThread = ShutdownThread.this;
                shutdownThread.mHandler.post(new Runnable() { // from class: com.android.server.power.ShutdownThread$3$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ShutdownThread.this.actionDone();
                    }
                });
            }
        }, (String[]) null, -1, (int[]) null, (BiFunction) null, (Bundle) null);
        long elapsedRealtime = SystemClock.elapsedRealtime() + 5000;
        synchronized (this.mActionDoneSync) {
            while (true) {
                if (this.mActionDone) {
                    break;
                }
                long elapsedRealtime2 = elapsedRealtime - SystemClock.elapsedRealtime();
                if (elapsedRealtime2 <= 0) {
                    Log.w(TAG, "Shutdown broadcast timed out");
                    mShutdownThreadStaticExt.doShutdownDetect("47");
                    break;
                } else {
                    if (mRebootHasProgressBar) {
                        sInstance.setRebootProgress((int) ((((5000 - elapsedRealtime2) * 1.0d) * 2.0d) / 5000.0d), null);
                    }
                    try {
                        this.mActionDoneSync.wait(Math.min(elapsedRealtime2, 500L));
                    } catch (InterruptedException unused) {
                    }
                }
            }
        }
        if (mRebootHasProgressBar) {
            sInstance.setRebootProgress(2, null);
        }
        newTimingsLog.traceEnd();
        metricEnded(METRIC_SEND_BROADCAST);
        Log.i(TAG, "Shutting down activity manager...");
        newTimingsLog.traceBegin("ShutdownActivityManager");
        metricStarted(METRIC_AM);
        long elapsedRealtime3 = SystemClock.elapsedRealtime();
        IActivityManager asInterface = IActivityManager.Stub.asInterface(ServiceManager.checkService("activity"));
        if (asInterface != null) {
            try {
                asInterface.shutdown(5000);
            } catch (RemoteException unused2) {
            }
        }
        if (mRebootHasProgressBar) {
            sInstance.setRebootProgress(4, null);
        }
        if (SystemClock.elapsedRealtime() - elapsedRealtime3 > 5000) {
            mShutdownThreadStaticExt.doShutdownDetect("46");
        }
        newTimingsLog.traceEnd();
        metricEnded(METRIC_AM);
        Log.i(TAG, "Shutting down package manager...");
        newTimingsLog.traceBegin("ShutdownPackageManager");
        metricStarted(METRIC_PM);
        long elapsedRealtime4 = SystemClock.elapsedRealtime();
        PackageManagerInternal packageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        if (packageManagerInternal != null) {
            packageManagerInternal.shutdown();
        }
        if (mRebootHasProgressBar) {
            sInstance.setRebootProgress(6, null);
        }
        if (SystemClock.elapsedRealtime() - elapsedRealtime4 > 5000) {
            mShutdownThreadStaticExt.doShutdownDetect("45");
        }
        newTimingsLog.traceEnd();
        metricEnded(METRIC_PM);
        newTimingsLog.traceBegin("ShutdownRadios");
        metricStarted(METRIC_RADIOS);
        shutdownRadios(MAX_RADIO_WAIT_TIME);
        if (mRebootHasProgressBar) {
            sInstance.setRebootProgress(18, null);
        }
        newTimingsLog.traceEnd();
        metricEnded(METRIC_RADIOS);
        this.mShutdownThreadExt.delayForPlayAnimation(this.mContext);
        if (mRebootHasProgressBar) {
            sInstance.setRebootProgress(20, null);
            uncrypt();
        }
        newTimingsLog.traceBegin("ShutdownCheckPointsDumpWait");
        try {
            newDumpThread.join(JobStatus.DEFAULT_TRIGGER_UPDATE_DELAY);
        } catch (InterruptedException unused3) {
        }
        newTimingsLog.traceEnd();
        newTimingsLog.traceEnd();
        metricEnded(METRIC_SYSTEM_SERVER);
        saveMetrics(mReboot, mReason);
        this.mShutdownThreadExt.shutdownStorageManagerService(this.mContext);
        mShutdownThreadStaticExt.doShutdownDetect("4");
        rebootOrShutdown(this.mContext, mReboot, mReason);
    }

    private static TimingsTraceLog newTimingsLog() {
        return new TimingsTraceLog("ShutdownTiming", 524288L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void metricStarted(String str) {
        ArrayMap<String, Long> arrayMap = TRON_METRICS;
        synchronized (arrayMap) {
            arrayMap.put(str, Long.valueOf(SystemClock.elapsedRealtime() * (-1)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void metricEnded(String str) {
        ArrayMap<String, Long> arrayMap = TRON_METRICS;
        synchronized (arrayMap) {
            arrayMap.put(str, Long.valueOf(SystemClock.elapsedRealtime() + arrayMap.get(str).longValue()));
        }
    }

    private static void metricShutdownStart() {
        ArrayMap<String, Long> arrayMap = TRON_METRICS;
        synchronized (arrayMap) {
            arrayMap.put(METRIC_SHUTDOWN_TIME_START, Long.valueOf(System.currentTimeMillis()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setRebootProgress(final int i, final CharSequence charSequence) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.power.ShutdownThread.4
            @Override // java.lang.Runnable
            public void run() {
                if (ShutdownThread.this.mProgressDialog != null) {
                    ShutdownThread.this.mProgressDialog.setProgress(i);
                    if (charSequence != null) {
                        ShutdownThread.this.mProgressDialog.setMessage(charSequence);
                    }
                }
            }
        });
    }

    private void shutdownRadios(final int i) {
        long j = i;
        final long elapsedRealtime = SystemClock.elapsedRealtime() + j;
        final boolean[] zArr = new boolean[1];
        Thread thread = new Thread() { // from class: com.android.server.power.ShutdownThread.5
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                TimingsTraceLog m2767$$Nest$smnewTimingsLog = ShutdownThread.m2767$$Nest$smnewTimingsLog();
                TelephonyManager telephonyManager = (TelephonyManager) ShutdownThread.this.mContext.getSystemService(TelephonyManager.class);
                boolean z = telephonyManager == null || !telephonyManager.isAnyRadioPoweredOn();
                if (!z) {
                    Log.w(ShutdownThread.TAG, "Turning off cellular radios...");
                    ShutdownThread.metricStarted(ShutdownThread.METRIC_RADIO);
                    telephonyManager.shutdownAllRadios();
                }
                Log.i(ShutdownThread.TAG, "Waiting for Radio...");
                long j2 = elapsedRealtime;
                long elapsedRealtime2 = SystemClock.elapsedRealtime();
                while (true) {
                    if (j2 - elapsedRealtime2 <= 0) {
                        return;
                    }
                    if (ShutdownThread.mRebootHasProgressBar) {
                        ShutdownThread.sInstance.setRebootProgress(((int) ((((r8 - r6) * 1.0d) * 12.0d) / i)) + 6, null);
                    }
                    if (!z) {
                        try {
                            z = !telephonyManager.isAnyRadioPoweredOn();
                        } catch (Exception unused) {
                            Log.i(ShutdownThread.TAG, "phone is dead.......");
                        }
                        if (z) {
                            Log.i(ShutdownThread.TAG, "Radio turned off.");
                            ShutdownThread.metricEnded(ShutdownThread.METRIC_RADIO);
                            m2767$$Nest$smnewTimingsLog.logDuration("ShutdownRadio", ((Long) ShutdownThread.TRON_METRICS.get(ShutdownThread.METRIC_RADIO)).longValue());
                        }
                    }
                    if (z) {
                        Log.i(ShutdownThread.TAG, "Radio shutdown complete.");
                        zArr[0] = true;
                        return;
                    } else {
                        SystemClock.sleep(100L);
                        j2 = elapsedRealtime;
                        elapsedRealtime2 = SystemClock.elapsedRealtime();
                    }
                }
            }
        };
        thread.start();
        try {
            thread.join(j);
        } catch (InterruptedException unused) {
        }
        if (zArr[0]) {
            return;
        }
        Log.w(TAG, "Timed out waiting for Radio shutdown.");
    }

    public static void rebootOrShutdown(Context context, boolean z, String str) {
        OplusDisplayPanelFeatureHelper.setDisplayPanelFeatureValue(210, 1);
        if (!mShutdownThreadStaticExt.rebootOrShutdownSubsystem()) {
            mShutdownThreadStaticExt.doShutdownDetect("43");
        }
        if (z) {
            Log.i(TAG, "Rebooting, reason: " + str);
            if (mShutdownThreadStaticExt.shouldDoLowLevelShutdown(context)) {
                PowerManagerService.lowLevelReboot(str);
            }
            Log.e(TAG, "Reboot failed, will attempt shutdown instead");
            return;
        }
        if (context != null) {
            if (mShutdownThreadStaticExt.shouldDoLowLevelShutdown(context)) {
                SystemVibrator systemVibrator = new SystemVibrator(context);
                try {
                    if (systemVibrator.hasVibrator()) {
                        systemVibrator.vibrate(500L, VIBRATION_ATTRIBUTES);
                        try {
                            Thread.sleep(500L);
                        } catch (InterruptedException unused) {
                        }
                    }
                } catch (Exception e) {
                    Log.w(TAG, "Failed to vibrate during shutdown.", e);
                }
                Log.i(TAG, "Performing low-level shutdown normal...");
                PowerManagerService.lowLevelShutdown(str);
                return;
            }
            Log.i(TAG, "Shutdown process timeout noneed do lowLevelShutdown and vibrate");
        }
    }

    private static void saveMetrics(boolean z, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("reboot:");
        sb.append(z ? "y" : "n");
        sb.append(",");
        sb.append("reason:");
        sb.append(str);
        int size = TRON_METRICS.size();
        boolean z2 = false;
        for (int i = 0; i < size; i++) {
            ArrayMap<String, Long> arrayMap = TRON_METRICS;
            String keyAt = arrayMap.keyAt(i);
            long longValue = arrayMap.valueAt(i).longValue();
            if (longValue < 0) {
                Log.e(TAG, "metricEnded wasn't called for " + keyAt);
            } else {
                sb.append(',');
                sb.append(keyAt);
                sb.append(':');
                sb.append(longValue);
            }
        }
        File file = new File("/data/system/shutdown-metrics.tmp");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            try {
                fileOutputStream.write(sb.toString().getBytes(StandardCharsets.UTF_8));
                z2 = true;
                fileOutputStream.close();
            } finally {
            }
        } catch (IOException e) {
            Log.e(TAG, "Cannot save shutdown metrics", e);
        }
        if (z2) {
            file.renameTo(new File("/data/system/shutdown-metrics.txt"));
        }
    }

    private void uncrypt() {
        Log.i(TAG, "Calling uncrypt and monitoring the progress...");
        final RecoverySystem.ProgressListener progressListener = new RecoverySystem.ProgressListener() { // from class: com.android.server.power.ShutdownThread.6
            @Override // android.os.RecoverySystem.ProgressListener
            public void onProgress(int i) {
                if (i >= 0 && i < 100) {
                    ShutdownThread.sInstance.setRebootProgress(((int) ((i * 80.0d) / 100.0d)) + 20, ShutdownThread.this.mContext.getText(R.string.whichSendApplication));
                } else if (i == 100) {
                    ShutdownThread.sInstance.setRebootProgress(i, ShutdownThread.this.mContext.getText(R.string.whichSendApplicationNamed));
                }
            }
        };
        final boolean[] zArr = {false};
        Thread thread = new Thread() { // from class: com.android.server.power.ShutdownThread.7
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    RecoverySystem.processPackage(ShutdownThread.this.mContext, new File(FileUtils.readTextFile(RecoverySystem.UNCRYPT_PACKAGE_FILE, 0, null)), progressListener);
                } catch (IOException e) {
                    Log.e(ShutdownThread.TAG, "Error uncrypting file", e);
                }
                zArr[0] = true;
            }
        };
        thread.start();
        try {
            thread.join(900000L);
        } catch (InterruptedException unused) {
        }
        if (zArr[0]) {
            return;
        }
        Log.w(TAG, "Timed out waiting for uncrypt.");
        try {
            FileUtils.stringToFile(RecoverySystem.UNCRYPT_STATUS_FILE, String.format("uncrypt_time: %d\nuncrypt_error: %d\n", 900, 100));
        } catch (IOException e) {
            Log.e(TAG, "Failed to write timeout message to uncrypt status", e);
        }
    }
}
