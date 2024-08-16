package com.android.server.profcollect;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UpdateEngine;
import android.os.UpdateEngineCallback;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.util.Log;
import com.android.internal.os.BackgroundThread;
import com.android.server.IoThread;
import com.android.server.LocalServices;
import com.android.server.SystemService;
import com.android.server.pm.PackageManagerService;
import com.android.server.profcollect.IProfCollectd;
import com.android.server.profcollect.IProviderStatusCallback;
import com.android.server.profcollect.ProfcollectForwardingService;
import com.android.server.timezonedetector.ServiceConfigAccessor;
import com.android.server.vibrator.VibratorManagerService;
import com.android.server.wm.ActivityMetricsLaunchObserver;
import com.android.server.wm.ActivityTaskManagerInternal;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class ProfcollectForwardingService extends SystemService {
    private static final String INTENT_UPLOAD_PROFILES = "com.android.server.profcollect.UPLOAD_PROFILES";
    private static ProfcollectForwardingService sSelfService;
    private final AppLaunchObserver mAppLaunchObserver;
    private final BroadcastReceiver mBroadcastReceiver;
    private final Handler mHandler;
    private IProfCollectd mIProfcollect;
    private IProviderStatusCallback mProviderStatusCallback;
    public static final String LOG_TAG = "ProfcollectForwardingService";
    private static final boolean DEBUG = Log.isLoggable(LOG_TAG, 3);
    private static final long BG_PROCESS_PERIOD = TimeUnit.HOURS.toMillis(4);

    public ProfcollectForwardingService(Context context) {
        super(context);
        this.mHandler = new ProfcollectdHandler(IoThread.getHandler().getLooper());
        this.mProviderStatusCallback = new IProviderStatusCallback.Stub() { // from class: com.android.server.profcollect.ProfcollectForwardingService.1
            @Override // com.android.server.profcollect.IProviderStatusCallback
            public void onProviderReady() {
                ProfcollectForwardingService.this.mHandler.sendEmptyMessage(1);
            }
        };
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.profcollect.ProfcollectForwardingService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (ProfcollectForwardingService.INTENT_UPLOAD_PROFILES.equals(intent.getAction())) {
                    Log.d(ProfcollectForwardingService.LOG_TAG, "Received broadcast to pack and upload reports");
                    ProfcollectForwardingService.this.packAndUploadReport();
                }
            }
        };
        this.mBroadcastReceiver = broadcastReceiver;
        this.mAppLaunchObserver = new AppLaunchObserver();
        if (sSelfService != null) {
            throw new AssertionError("only one service instance allowed");
        }
        sSelfService = this;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(INTENT_UPLOAD_PROFILES);
        context.registerReceiver(broadcastReceiver, intentFilter, 4);
    }

    public static boolean enabled() {
        return DeviceConfig.getBoolean("profcollect_native_boot", ServiceConfigAccessor.PROVIDER_MODE_ENABLED, false) || SystemProperties.getBoolean("persist.profcollectd.enabled_override", false);
    }

    public void onStart() {
        if (DEBUG) {
            Log.d(LOG_TAG, "Profcollect forwarding service start");
        }
        connectNativeService();
    }

    public void onBootPhase(int i) {
        if (i != 1000 || this.mIProfcollect == null) {
            return;
        }
        BackgroundThread.get().getThreadHandler().post(new Runnable() { // from class: com.android.server.profcollect.ProfcollectForwardingService$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                ProfcollectForwardingService.this.lambda$onBootPhase$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBootPhase$0() {
        if (serviceHasSupportedTraceProvider()) {
            registerProviderStatusCallback();
        }
    }

    private void registerProviderStatusCallback() {
        IProfCollectd iProfCollectd = this.mIProfcollect;
        if (iProfCollectd == null) {
            return;
        }
        try {
            iProfCollectd.registerProviderStatusCallback(this.mProviderStatusCallback);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Failed to register provider status callback: " + e.getMessage());
        }
    }

    private boolean serviceHasSupportedTraceProvider() {
        if (this.mIProfcollect == null) {
            return false;
        }
        try {
            return !r3.get_supported_provider().isEmpty();
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Failed to get supported provider: " + e.getMessage());
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean tryConnectNativeService() {
        if (connectNativeService()) {
            return true;
        }
        this.mHandler.sendEmptyMessageDelayed(0, 5000L);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean connectNativeService() {
        try {
            IProfCollectd asInterface = IProfCollectd.Stub.asInterface(ServiceManager.getServiceOrThrow("profcollectd"));
            asInterface.asBinder().linkToDeath(new ProfcollectdDeathRecipient(), 0);
            this.mIProfcollect = asInterface;
            return true;
        } catch (ServiceManager.ServiceNotFoundException | RemoteException unused) {
            Log.w(LOG_TAG, "Failed to connect profcollectd binder service.");
            return false;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class ProfcollectdHandler extends Handler {
        public static final int MESSAGE_BINDER_CONNECT = 0;
        public static final int MESSAGE_REGISTER_SCHEDULERS = 1;

        public ProfcollectdHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                ProfcollectForwardingService.this.connectNativeService();
                return;
            }
            if (i == 1) {
                ProfcollectForwardingService.this.registerObservers();
                ProfcollectBGJobService.schedule(ProfcollectForwardingService.this.getContext());
            } else {
                throw new AssertionError("Unknown message: " + message);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class ProfcollectdDeathRecipient implements IBinder.DeathRecipient {
        private ProfcollectdDeathRecipient() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Log.w(ProfcollectForwardingService.LOG_TAG, "profcollectd has died");
            ProfcollectForwardingService.this.mIProfcollect = null;
            ProfcollectForwardingService.this.tryConnectNativeService();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ProfcollectBGJobService extends JobService {
        private static final int JOB_IDLE_PROCESS = 260817;
        private static final ComponentName JOB_SERVICE_NAME = new ComponentName(PackageManagerService.PLATFORM_PACKAGE_NAME, ProfcollectBGJobService.class.getName());

        @Override // android.app.job.JobService
        public boolean onStopJob(JobParameters jobParameters) {
            return false;
        }

        public static void schedule(Context context) {
            ((JobScheduler) context.getSystemService(JobScheduler.class)).schedule(new JobInfo.Builder(JOB_IDLE_PROCESS, JOB_SERVICE_NAME).setRequiresDeviceIdle(true).setRequiresCharging(true).setPeriodic(ProfcollectForwardingService.BG_PROCESS_PERIOD).setPriority(100).build());
        }

        @Override // android.app.job.JobService
        public boolean onStartJob(JobParameters jobParameters) {
            if (ProfcollectForwardingService.DEBUG) {
                Log.d(ProfcollectForwardingService.LOG_TAG, "Starting background process job");
            }
            BackgroundThread.get().getThreadHandler().post(new Runnable() { // from class: com.android.server.profcollect.ProfcollectForwardingService$ProfcollectBGJobService$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ProfcollectForwardingService.ProfcollectBGJobService.lambda$onStartJob$0();
                }
            });
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onStartJob$0() {
            try {
                if (ProfcollectForwardingService.sSelfService.mIProfcollect == null) {
                    return;
                }
                ProfcollectForwardingService.sSelfService.mIProfcollect.process();
            } catch (RemoteException e) {
                Log.e(ProfcollectForwardingService.LOG_TAG, "Failed to process profiles in background: " + e.getMessage());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerObservers() {
        BackgroundThread.get().getThreadHandler().post(new Runnable() { // from class: com.android.server.profcollect.ProfcollectForwardingService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ProfcollectForwardingService.this.lambda$registerObservers$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerObservers$1() {
        registerAppLaunchObserver();
        registerOTAObserver();
    }

    private void registerAppLaunchObserver() {
        ((ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class)).getLaunchObserverRegistry().registerLaunchObserver(this.mAppLaunchObserver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void traceOnAppStart(String str) {
        if (this.mIProfcollect == null) {
            return;
        }
        if (ThreadLocalRandom.current().nextInt(100) < DeviceConfig.getInt("profcollect_native_boot", "applaunch_trace_freq", 2)) {
            if (DEBUG) {
                Log.d(LOG_TAG, "Tracing on app launch event: " + str);
            }
            BackgroundThread.get().getThreadHandler().post(new Runnable() { // from class: com.android.server.profcollect.ProfcollectForwardingService$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ProfcollectForwardingService.this.lambda$traceOnAppStart$2();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$traceOnAppStart$2() {
        try {
            this.mIProfcollect.trace_once("applaunch");
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Failed to initiate trace: " + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AppLaunchObserver extends ActivityMetricsLaunchObserver {
        private AppLaunchObserver() {
        }

        public void onIntentStarted(Intent intent, long j) {
            ProfcollectForwardingService.this.traceOnAppStart(intent.getPackage());
        }
    }

    private void registerOTAObserver() {
        new UpdateEngine().bind(new UpdateEngineCallback() { // from class: com.android.server.profcollect.ProfcollectForwardingService.3
            public void onPayloadApplicationComplete(int i) {
            }

            public void onStatusUpdate(int i, float f) {
                if (ProfcollectForwardingService.DEBUG) {
                    Log.d(ProfcollectForwardingService.LOG_TAG, "Received OTA status update, status: " + i + ", percent: " + f);
                }
                if (i == 6) {
                    ProfcollectForwardingService.this.packAndUploadReport();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void packAndUploadReport() {
        if (this.mIProfcollect == null) {
            return;
        }
        final Context context = getContext();
        BackgroundThread.get().getThreadHandler().post(new Runnable() { // from class: com.android.server.profcollect.ProfcollectForwardingService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                ProfcollectForwardingService.this.lambda$packAndUploadReport$3(context);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$packAndUploadReport$3(Context context) {
        int i;
        try {
            try {
                i = Settings.Global.getInt(context.getContentResolver(), "multi_cb");
            } catch (Settings.SettingNotFoundException e) {
                Log.i(LOG_TAG, "Usage setting not found: " + e.getMessage());
                i = -1;
            }
            String str = this.mIProfcollect.report(i) + ".zip";
            if (!context.getResources().getBoolean(17891789)) {
                Log.i(LOG_TAG, "Upload is not enabled.");
            } else {
                context.sendBroadcast(new Intent().setPackage(VibratorManagerService.VibratorManagerShellCommand.SHELL_PACKAGE_NAME).setAction("com.android.shell.action.PROFCOLLECT_UPLOAD").putExtra("filename", str));
            }
        } catch (RemoteException e2) {
            Log.e(LOG_TAG, "Failed to upload report: " + e2.getMessage());
        }
    }
}
