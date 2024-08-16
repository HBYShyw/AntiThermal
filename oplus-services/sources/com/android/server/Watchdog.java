package com.android.server;

import android.app.IActivityController;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.hardware.biometrics.face.V1_0.IBiometricsFace;
import android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint;
import android.hardware.health.V2_0.IHealth;
import android.hidl.manager.V1_0.IServiceManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Debug;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceDebugInfo;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.sysprop.WatchdogProperties;
import android.util.Dumpable;
import android.util.EventLog;
import android.util.Log;
import android.util.Slog;
import android.util.SparseBooleanArray;
import com.android.internal.os.BackgroundThread;
import com.android.internal.os.ProcessCpuTracker;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.am.ActivityManagerService;
import com.android.server.am.StackTracesDumpHelper;
import com.android.server.am.TraceErrorLogger;
import com.android.server.am.trace.SmartTraceUtils;
import com.android.server.criticalevents.CriticalEventLog;
import com.android.server.wm.SurfaceAnimationThread;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class Watchdog implements Dumpable {
    private static final int COMPLETED = 0;
    private static final boolean DB = false;
    public static final boolean DEBUG = false;
    private static final long DEFAULT_TIMEOUT = 60000;
    private static final int OVERDUE = 3;
    private static final String PROP_FATAL_LOOP_COUNT = "framework_watchdog.fatal_count";
    private static final String PROP_FATAL_LOOP_WINDOWS_SECS = "framework_watchdog.fatal_window.second";
    static final String TAG = "Watchdog";
    private static final String TIMEOUT_HISTORY_FILE = "/data/system/watchdog-timeout-history.txt";
    private static final int WAITED_HALF = 2;
    private static final int WAITING = 1;
    public static IWatchdogExt mWdtExt;
    private static Watchdog sWatchdog;
    private ActivityManagerService mActivity;
    private boolean mAllowRestart;
    private IActivityController mController;
    private final ArrayList<HandlerCheckerAndTimeout> mHandlerCheckers;
    private final List<Integer> mInterestingJavaPids;
    private final HandlerChecker mMonitorChecker;
    private boolean mSfHang;
    private final Thread mThread;
    SimpleDateFormat mTraceDateFormat;
    private final TraceErrorLogger mTraceErrorLogger;
    private volatile long mWatchdogTimeoutMillis;
    public IWatchdogSocExt mWdtSocExt;
    public static final String[] NATIVE_STACKS_OF_INTEREST = {"/system/bin/audioserver", "/system/bin/cameraserver", "/system/bin/drmserver", "/system/bin/keystore2", "/system/bin/mediadrmserver", "/system/bin/mediaserver", "/system/bin/mediaserver64", "/system/bin/netd", "/system/bin/sdcard", "/system/bin/surfaceflinger", "/system/bin/vold", "media.extractor", "media.metrics", "media.codec", "media.swcodec", "media.transcoding", "com.android.bluetooth", "/apex/com.android.os.statsd/bin/statsd"};
    public static final List<String> HAL_INTERFACES_OF_INTEREST = Arrays.asList("android.hardware.audio@4.0::IDevicesFactory", "android.hardware.audio@5.0::IDevicesFactory", "android.hardware.audio@6.0::IDevicesFactory", "android.hardware.audio@7.0::IDevicesFactory", IBiometricsFace.kInterfaceName, IBiometricsFingerprint.kInterfaceName, "android.hardware.bluetooth@1.0::IBluetoothHci", "android.hardware.camera.provider@2.4::ICameraProvider", "android.hardware.gnss@1.0::IGnss", "android.hardware.graphics.allocator@2.0::IAllocator", "android.hardware.graphics.composer@2.1::IComposer", IHealth.kInterfaceName, "android.hardware.light@2.0::ILight", "android.hardware.media.c2@1.0::IComponentStore", "android.hardware.media.omx@1.0::IOmx", "android.hardware.media.omx@1.0::IOmxStore", "android.hardware.neuralnetworks@1.0::IDevice", "android.hardware.power@1.0::IPower", "android.hardware.power.stats@1.0::IPowerStats", "android.hardware.sensors@1.0::ISensors", "android.hardware.sensors@2.0::ISensors", "android.hardware.sensors@2.1::ISensors", "android.hardware.vibrator@1.0::IVibrator", "android.hardware.vr@1.0::IVr", "android.system.suspend@1.0::ISystemSuspend");
    public static final String[] AIDL_INTERFACE_PREFIXES_OF_INTEREST = {"android.hardware.audio.core.IModule/", "android.hardware.audio.core.IConfig/", "android.hardware.biometrics.face.IFace/", "android.hardware.biometrics.fingerprint.IFingerprint/", "android.hardware.bluetooth.IBluetoothHci/", "android.hardware.camera.provider.ICameraProvider/", "android.hardware.gnss.IGnss/", "android.hardware.graphics.allocator.IAllocator/", "android.hardware.graphics.composer3.IComposer/", "android.hardware.health.IHealth/", "android.hardware.input.processor.IInputProcessor/", "android.hardware.light.ILights/", "android.hardware.neuralnetworks.IDevice/", "android.hardware.power.IPower/", "android.hardware.power.stats.IPowerStats/", "android.hardware.sensors.ISensors/", "android.hardware.vibrator.IVibrator/", "android.hardware.vibrator.IVibratorManager/", "android.system.suspend.ISystemSuspend/"};
    private final int TIME_SF_WAIT = 20000;
    private final Object mLock = new Object();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Monitor {
        void monitor();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class HandlerCheckerAndTimeout {
        private final Optional<Long> mCustomTimeoutMillis;
        private final HandlerChecker mHandler;

        private HandlerCheckerAndTimeout(HandlerChecker handlerChecker, Optional<Long> optional) {
            this.mHandler = handlerChecker;
            this.mCustomTimeoutMillis = optional;
        }

        HandlerChecker checker() {
            return this.mHandler;
        }

        Optional<Long> customTimeoutMillis() {
            return this.mCustomTimeoutMillis;
        }

        static HandlerCheckerAndTimeout withDefaultTimeout(HandlerChecker handlerChecker) {
            return new HandlerCheckerAndTimeout(handlerChecker, Optional.empty());
        }

        static HandlerCheckerAndTimeout withCustomTimeout(HandlerChecker handlerChecker, long j) {
            return new HandlerCheckerAndTimeout(handlerChecker, Optional.of(Long.valueOf(j)));
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class HandlerChecker implements Runnable {
        private Monitor mCurrentMonitor;
        private final Handler mHandler;
        private final String mName;
        private int mPauseCount;
        private long mStartTimeMillis;
        private long mWaitMaxMillis;
        private final ArrayList<Monitor> mMonitors = new ArrayList<>();
        private final ArrayList<Monitor> mMonitorQueue = new ArrayList<>();
        private boolean mCompleted = true;

        HandlerChecker(Handler handler, String str) {
            this.mHandler = handler;
            this.mName = str;
        }

        void addMonitorLocked(Monitor monitor) {
            this.mMonitorQueue.add(monitor);
        }

        public void scheduleCheckLocked(long j) {
            this.mWaitMaxMillis = j;
            if (this.mCompleted) {
                this.mMonitors.addAll(this.mMonitorQueue);
                this.mMonitorQueue.clear();
            }
            if ((this.mMonitors.size() == 0 && this.mHandler.getLooper().getQueue().isPolling()) || this.mPauseCount > 0) {
                this.mCompleted = true;
            } else if (this.mCompleted) {
                this.mCompleted = false;
                this.mCurrentMonitor = null;
                this.mStartTimeMillis = SystemClock.uptimeMillis();
                this.mHandler.postAtFrontOfQueue(this);
            }
        }

        public int getCompletionStateLocked() {
            if (this.mCompleted) {
                return 0;
            }
            long uptimeMillis = SystemClock.uptimeMillis() - this.mStartTimeMillis;
            long j = this.mWaitMaxMillis;
            if (uptimeMillis < j / 2) {
                return 1;
            }
            return uptimeMillis < j ? 2 : 3;
        }

        public Thread getThread() {
            return this.mHandler.getLooper().getThread();
        }

        public String getName() {
            return this.mName;
        }

        String describeBlockedStateLocked() {
            String str;
            if (this.mCurrentMonitor == null) {
                str = "Blocked in handler on ";
            } else {
                str = "Blocked in monitor " + this.mCurrentMonitor.getClass().getName();
            }
            return str + " on " + this.mName + " (" + getThread().getName() + ") for " + ((SystemClock.uptimeMillis() - this.mStartTimeMillis) / 1000) + "s";
        }

        @Override // java.lang.Runnable
        public void run() {
            Monitor monitor;
            int size = this.mMonitors.size();
            for (int i = 0; i < size; i++) {
                synchronized (Watchdog.this.mLock) {
                    monitor = this.mMonitors.get(i);
                    this.mCurrentMonitor = monitor;
                }
                monitor.monitor();
            }
            synchronized (Watchdog.this.mLock) {
                this.mCompleted = true;
                this.mCurrentMonitor = null;
            }
        }

        public void pauseLocked(String str) {
            this.mPauseCount++;
            this.mCompleted = true;
            Slog.i(Watchdog.TAG, "Pausing HandlerChecker: " + this.mName + " for reason: " + str + ". Pause count: " + this.mPauseCount);
        }

        public void resumeLocked(String str) {
            int i = this.mPauseCount;
            if (i > 0) {
                this.mPauseCount = i - 1;
                Slog.i(Watchdog.TAG, "Resuming HandlerChecker: " + this.mName + " for reason: " + str + ". Pause count: " + this.mPauseCount);
                return;
            }
            Slog.wtf(Watchdog.TAG, "Already resumed HandlerChecker: " + this.mName);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    final class RebootRequestReceiver extends BroadcastReceiver {
        RebootRequestReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getIntExtra("nowait", 0) != 0) {
                Watchdog.this.rebootSystem("Received ACTION_REBOOT broadcast");
                return;
            }
            Slog.w(Watchdog.TAG, "Unsupported ACTION_REBOOT broadcast: " + intent);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private static final class BinderThreadMonitor implements Monitor {
        private BinderThreadMonitor() {
        }

        @Override // com.android.server.Watchdog.Monitor
        public void monitor() {
            Watchdog.mWdtExt.getBinderBlockTimeMS();
            Binder.blockUntilThreadAvailable();
            Watchdog.mWdtExt.onProcessBinderCnt();
        }
    }

    public static Watchdog getInstance() {
        if (sWatchdog == null) {
            sWatchdog = new Watchdog();
        }
        return sWatchdog;
    }

    private Watchdog() {
        ArrayList<HandlerCheckerAndTimeout> arrayList = new ArrayList<>();
        this.mHandlerCheckers = arrayList;
        this.mAllowRestart = true;
        this.mSfHang = false;
        this.mWatchdogTimeoutMillis = 60000L;
        this.mTraceDateFormat = new SimpleDateFormat("dd_MM_HH_mm_ss.SSS");
        ArrayList arrayList2 = new ArrayList();
        this.mInterestingJavaPids = arrayList2;
        this.mWdtSocExt = (IWatchdogSocExt) ExtLoader.type(IWatchdogSocExt.class).base(this).create();
        this.mThread = new Thread(new Runnable() { // from class: com.android.server.Watchdog$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                Watchdog.this.run();
            }
        }, "watchdog");
        ServiceThread serviceThread = new ServiceThread("watchdog.monitor", 0, true);
        serviceThread.start();
        HandlerChecker handlerChecker = new HandlerChecker(new Handler(serviceThread.getLooper()), "monitor thread");
        this.mMonitorChecker = handlerChecker;
        arrayList.add(HandlerCheckerAndTimeout.withDefaultTimeout(handlerChecker));
        arrayList.add(HandlerCheckerAndTimeout.withDefaultTimeout(new HandlerChecker(FgThread.getHandler(), "foreground thread")));
        arrayList.add(HandlerCheckerAndTimeout.withDefaultTimeout(new HandlerChecker(new Handler(Looper.getMainLooper()), "main thread")));
        arrayList.add(HandlerCheckerAndTimeout.withDefaultTimeout(new HandlerChecker(UiThread.getHandler(), "ui thread")));
        arrayList.add(HandlerCheckerAndTimeout.withDefaultTimeout(new HandlerChecker(IoThread.getHandler(), "i/o thread")));
        arrayList.add(HandlerCheckerAndTimeout.withDefaultTimeout(new HandlerChecker(DisplayThread.getHandler(), "display thread")));
        arrayList.add(HandlerCheckerAndTimeout.withDefaultTimeout(new HandlerChecker(AnimationThread.getHandler(), "animation thread")));
        arrayList.add(HandlerCheckerAndTimeout.withDefaultTimeout(new HandlerChecker(SurfaceAnimationThread.getHandler(), "surface animation thread")));
        addMonitor(new BinderThreadMonitor());
        arrayList2.add(Integer.valueOf(Process.myPid()));
        mWdtExt = (IWatchdogExt) ExtLoader.type(IWatchdogExt.class).base(this).create();
        this.mWdtSocExt.getExceptionLog();
        this.mTraceErrorLogger = new TraceErrorLogger();
    }

    public void start() {
        if (mWdtExt.checkIfNeedCloseWdt()) {
            return;
        }
        this.mThread.start();
    }

    public void init(Context context, ActivityManagerService activityManagerService) {
        this.mActivity = activityManagerService;
        context.registerReceiver(new RebootRequestReceiver(), new IntentFilter("android.intent.action.REBOOT"), "android.permission.REBOOT", null);
        this.mWdtSocExt.WDTMatterJava(0L);
        mWdtExt.init(context, activityManagerService);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private static class SettingsObserver extends ContentObserver {
        private final Context mContext;
        private final Uri mUri;
        private final Watchdog mWatchdog;

        SettingsObserver(Context context, Watchdog watchdog) {
            super(BackgroundThread.getHandler());
            this.mUri = Settings.Global.getUriFor("system_server_watchdog_timeout_ms");
            this.mContext = context;
            this.mWatchdog = watchdog;
            onChange();
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri, int i) {
            if (this.mUri.equals(uri)) {
                onChange();
            }
        }

        public void onChange() {
            try {
                this.mWatchdog.updateWatchdogTimeout(Settings.Global.getLong(this.mContext.getContentResolver(), "system_server_watchdog_timeout_ms", 60000L));
            } catch (RuntimeException e) {
                Slog.e(Watchdog.TAG, "Exception while reading settings " + e.getMessage(), e);
            }
        }
    }

    public void registerSettingsObserver(Context context) {
        context.getContentResolver().registerContentObserver(Settings.Global.getUriFor("system_server_watchdog_timeout_ms"), false, new SettingsObserver(context, this), 0);
    }

    void updateWatchdogTimeout(long j) {
        if (j <= 20000) {
            j = 20001;
        }
        if ("1".equals(SystemProperties.get("persist.sys.agingtest", ""))) {
            j = 240000;
        }
        this.mWatchdogTimeoutMillis = j;
        Slog.i(TAG, "Watchdog timeout updated to " + this.mWatchdogTimeoutMillis + " millis");
    }

    private static boolean isInterestingJavaProcess(String str) {
        return str.equals(StorageManagerService.sMediaStoreAuthorityProcessName) || str.equals("com.android.phone");
    }

    public void processStarted(String str, int i) {
        mWdtExt.processStarted(str, i);
        if (isInterestingJavaProcess(str)) {
            Slog.i(TAG, "Interesting Java process " + str + " started. Pid " + i);
            synchronized (this.mLock) {
                this.mInterestingJavaPids.add(Integer.valueOf(i));
            }
        }
    }

    public void processDied(String str, int i) {
        if (isInterestingJavaProcess(str)) {
            Slog.i(TAG, "Interesting Java process " + str + " died. Pid " + i);
            synchronized (this.mLock) {
                this.mInterestingJavaPids.remove(Integer.valueOf(i));
            }
        }
    }

    public void setActivityController(IActivityController iActivityController) {
        synchronized (this.mLock) {
            this.mController = iActivityController;
        }
    }

    public void setAllowRestart(boolean z) {
        synchronized (this.mLock) {
            this.mAllowRestart = z;
        }
    }

    public void addMonitor(Monitor monitor) {
        synchronized (this.mLock) {
            this.mMonitorChecker.addMonitorLocked(monitor);
        }
    }

    public void addThread(Handler handler) {
        synchronized (this.mLock) {
            this.mHandlerCheckers.add(HandlerCheckerAndTimeout.withDefaultTimeout(new HandlerChecker(handler, handler.getLooper().getThread().getName())));
        }
    }

    public void addThread(Handler handler, long j) {
        synchronized (this.mLock) {
            this.mHandlerCheckers.add(HandlerCheckerAndTimeout.withCustomTimeout(new HandlerChecker(handler, handler.getLooper().getThread().getName()), j));
        }
    }

    public void pauseWatchingCurrentThread(String str) {
        synchronized (this.mLock) {
            Iterator<HandlerCheckerAndTimeout> it = this.mHandlerCheckers.iterator();
            while (it.hasNext()) {
                HandlerChecker checker = it.next().checker();
                if (Thread.currentThread().equals(checker.getThread())) {
                    checker.pauseLocked(str);
                }
            }
        }
    }

    public void resumeWatchingCurrentThread(String str) {
        synchronized (this.mLock) {
            Iterator<HandlerCheckerAndTimeout> it = this.mHandlerCheckers.iterator();
            while (it.hasNext()) {
                HandlerChecker checker = it.next().checker();
                if (Thread.currentThread().equals(checker.getThread())) {
                    checker.resumeLocked(str);
                }
            }
        }
    }

    void rebootSystem(String str) {
        Slog.i(TAG, "Rebooting system because: " + str);
        try {
            ServiceManager.getService("power").reboot(false, str, false);
        } catch (RemoteException unused) {
        }
    }

    private int evaluateCheckerCompletionLocked() {
        int i = 0;
        for (int i2 = 0; i2 < this.mHandlerCheckers.size(); i2++) {
            i = Math.max(i, this.mHandlerCheckers.get(i2).checker().getCompletionStateLocked());
        }
        return i;
    }

    private ArrayList<HandlerChecker> getCheckersWithStateLocked(int i) {
        ArrayList<HandlerChecker> arrayList = new ArrayList<>();
        for (int i2 = 0; i2 < this.mHandlerCheckers.size(); i2++) {
            HandlerChecker checker = this.mHandlerCheckers.get(i2).checker();
            if (checker.getCompletionStateLocked() == i) {
                arrayList.add(checker);
            }
        }
        return arrayList;
    }

    private String describeCheckersLocked(List<HandlerChecker> list) {
        StringBuilder sb = new StringBuilder(128);
        for (int i = 0; i < list.size(); i++) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(list.get(i).describeBlockedStateLocked());
        }
        return sb.toString();
    }

    private static void addInterestingHidlPids(HashSet<Integer> hashSet) {
        try {
            Iterator<IServiceManager.InstanceDebugInfo> it = IServiceManager.getService().debugDump().iterator();
            while (it.hasNext()) {
                IServiceManager.InstanceDebugInfo next = it.next();
                if (next.pid != -1 && HAL_INTERFACES_OF_INTEREST.contains(next.interfaceName)) {
                    hashSet.add(Integer.valueOf(next.pid));
                }
            }
        } catch (RemoteException e) {
            Log.w(TAG, e);
        }
    }

    private static void addInterestingAidlPids(HashSet<Integer> hashSet) {
        ServiceDebugInfo[] serviceDebugInfo = ServiceManager.getServiceDebugInfo();
        if (serviceDebugInfo == null) {
            return;
        }
        for (ServiceDebugInfo serviceDebugInfo2 : serviceDebugInfo) {
            for (String str : AIDL_INTERFACE_PREFIXES_OF_INTEREST) {
                if (serviceDebugInfo2.name.startsWith(str)) {
                    hashSet.add(Integer.valueOf(serviceDebugInfo2.debugPid));
                }
            }
        }
    }

    public static ArrayList<Integer> getInterestingNativePids() {
        HashSet<Integer> hashSet = new HashSet<>();
        addInterestingAidlPids(hashSet);
        addInterestingHidlPids(hashSet);
        mWdtExt.addWatchdogExtNativePids(hashSet);
        int[] pidsForCommands = Process.getPidsForCommands(NATIVE_STACKS_OF_INTEREST);
        if (pidsForCommands != null) {
            for (int i : pidsForCommands) {
                hashSet.add(Integer.valueOf(i));
            }
        }
        return new ArrayList<>(hashSet);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x0159, code lost:
    
        android.util.Slog.i(com.android.server.Watchdog.TAG, "Activity controller requested to coninue to wait");
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0123, code lost:
    
        if (com.android.server.am.trace.SmartTraceUtils.isPerfettoDumpEnabled() == false) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0125, code lost:
    
        com.android.server.am.trace.SmartTraceUtils.traceStart();
        r11 = android.os.SystemClock.uptimeMillis() + 30000;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x012f, code lost:
    
        logWatchog(r9, r6, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0132, code lost:
    
        if (r9 == false) goto L126;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0134, code lost:
    
        com.android.server.Watchdog.mWdtExt.sendTheiaMsg();
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x013b, code lost:
    
        r3 = r17.mLock;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x013d, code lost:
    
        monitor-enter(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x013e, code lost:
    
        r5 = r17.mController;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0140, code lost:
    
        monitor-exit(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0143, code lost:
    
        if (r17.mSfHang != false) goto L115;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0145, code lost:
    
        if (r5 == null) goto L116;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0147, code lost:
    
        android.util.Slog.i(com.android.server.Watchdog.TAG, "Reporting stuck state to activity controller");
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x014e, code lost:
    
        android.os.Binder.setDumpDisabled("Service dumps disabled due to hung system process.");
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0157, code lost:
    
        if (r5.systemNotResponding(r6) < 0) goto L118;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void run() {
        char c;
        long j;
        ArrayList<HandlerChecker> checkersWithStateLocked;
        ArrayList<Integer> arrayList;
        String str;
        boolean z;
        int i;
        while (true) {
            boolean z2 = false;
            while (true) {
                mWdtExt.triggerDetect();
                Collections.emptyList();
                long j2 = this.mWatchdogTimeoutMillis;
                long j3 = j2 / 2;
                this.mSfHang = false;
                this.mWdtSocExt.WDTMatterJava(300L);
                synchronized (this.mLock) {
                    for (int i2 = 0; i2 < this.mHandlerCheckers.size(); i2++) {
                        HandlerCheckerAndTimeout handlerCheckerAndTimeout = this.mHandlerCheckers.get(i2);
                        handlerCheckerAndTimeout.checker().scheduleCheckLocked(handlerCheckerAndTimeout.customTimeoutMillis().orElse(Long.valueOf(Build.HW_TIMEOUT_MULTIPLIER * j2)).longValue());
                    }
                    long uptimeMillis = SystemClock.uptimeMillis();
                    c = 0;
                    long j4 = j3;
                    while (true) {
                        j = 0;
                        if (j4 <= 0) {
                            break;
                        }
                        char c2 = Debug.isDebuggerConnected() ? (char) 2 : c;
                        try {
                            this.mLock.wait(j4);
                        } catch (InterruptedException e) {
                            Log.wtf(TAG, e);
                        }
                        c = Debug.isDebuggerConnected() ? (char) 2 : c2;
                        j4 = j3 - (SystemClock.uptimeMillis() - uptimeMillis);
                        mWdtExt.eventDailyPush();
                    }
                    mWdtExt.checkSystemHeapMem();
                    long sfHangTime = this.mWdtSocExt.getSfHangTime();
                    boolean z3 = true;
                    if (sfHangTime > 40000) {
                        Slog.v(TAG, "**SF hang Time **" + sfHangTime);
                        this.mSfHang = true;
                        checkersWithStateLocked = getCheckersWithStateLocked(2);
                        arrayList = new ArrayList<>(this.mInterestingJavaPids);
                        str = "";
                    } else {
                        int evaluateCheckerCompletionLocked = evaluateCheckerCompletionLocked();
                        if (evaluateCheckerCompletionLocked == 0) {
                            mWdtExt.setWatchdogHappenValue(false);
                            if (z2) {
                                this.mWdtSocExt.switchFtrace(4);
                            }
                        } else if (evaluateCheckerCompletionLocked != 1) {
                            if (evaluateCheckerCompletionLocked != 2) {
                                mWdtExt.removeTheiaMsg();
                                checkersWithStateLocked = getCheckersWithStateLocked(3);
                                str = describeCheckersLocked(checkersWithStateLocked);
                                z3 = this.mAllowRestart;
                                arrayList = new ArrayList<>(this.mInterestingJavaPids);
                            } else if (!z2) {
                                mWdtExt.setWatchdogHappenValue(true);
                                Slog.i(TAG, "WAITED_HALF");
                                mWdtExt.killMultimediaProcess();
                                checkersWithStateLocked = getCheckersWithStateLocked(2);
                                str = describeCheckersLocked(checkersWithStateLocked);
                                arrayList = new ArrayList<>(this.mInterestingJavaPids);
                                z2 = true;
                                z = true;
                            }
                        }
                    }
                    z = z3;
                    z3 = false;
                }
            }
        }
        if (Debug.isDebuggerConnected()) {
            c = 2;
        }
        if (c >= 2) {
            Slog.w(TAG, "Debugger connected: Watchdog is *not* killing the system process");
        } else if (c > 0) {
            Slog.w(TAG, "Debugger was connected: Watchdog is *not* killing the system process");
        } else if (!z) {
            Slog.w(TAG, "Restart not allowed: Watchdog is *not* killing the system process");
        } else {
            Slog.w(TAG, "*** WATCHDOG KILLING SYSTEM PROCESS: " + str);
            mWdtExt.unfreezeForWatchdog();
            WatchdogDiagnostics.diagnoseCheckers(checkersWithStateLocked);
            mWdtExt.writeEvent(str);
            Slog.w(TAG, "*** GOODBYE!");
            if (SmartTraceUtils.isPerfettoDumpEnabled() && j > SystemClock.uptimeMillis()) {
                long uptimeMillis2 = j - SystemClock.uptimeMillis();
                Slog.i(TAG, "Sleep " + uptimeMillis2 + " ms to make sure perfetto log to be dumped completely");
                SystemClock.sleep(uptimeMillis2);
            }
            if (!Build.IS_USER && isCrashLoopFound() && !WatchdogProperties.should_ignore_fatal_count().orElse(Boolean.FALSE).booleanValue()) {
                breakCrashLoop();
            }
            this.mWdtSocExt.WDTMatterJava(330L);
            if (this.mSfHang) {
                Slog.w(TAG, "SF hang!");
                if (this.mWdtSocExt.getSfRebootTime() > 3) {
                    Slog.w(TAG, "SF hang reboot time larger than 3 time, reboot device!");
                    rebootSystem("Maybe SF driver hang, reboot device.");
                } else {
                    this.mWdtSocExt.setSfRebootTime();
                }
                Slog.v(TAG, "killing surfaceflinger for surfaceflinger hang");
                int[] pidsForCommands = Process.getPidsForCommands(new String[]{"/system/bin/surfaceflinger"});
                if (pidsForCommands != null && (i = pidsForCommands[0]) > 0) {
                    Process.killProcess(i);
                }
                Slog.v(TAG, "kill surfaceflinger end");
            } else {
                Process.killProcess(Process.myPid());
            }
            System.exit(10);
        }
    }

    private void logWatchog(boolean z, String str, ArrayList<Integer> arrayList) {
        String str2;
        ArrayList<Integer> interestingNativePids = getInterestingNativePids();
        String logLinesForSystemServerTraceFile = CriticalEventLog.getInstance().logLinesForSystemServerTraceFile();
        final UUID generateErrorId = this.mTraceErrorLogger.generateErrorId();
        if (this.mTraceErrorLogger.isAddErrorIdEnabled()) {
            this.mTraceErrorLogger.addProcessInfoAndErrorIdToTrace("system_server", Process.myPid(), generateErrorId);
            this.mTraceErrorLogger.addSubjectToTrace(str, generateErrorId);
        }
        if (z) {
            this.mWdtSocExt.WDTMatterJava(360L);
            this.mWdtSocExt.switchFtrace(3);
            CriticalEventLog.getInstance().logHalfWatchdog(str);
            FrameworkStatsLog.write(FrameworkStatsLog.SYSTEM_SERVER_PRE_WATCHDOG_OCCURRED);
            str2 = "pre_watchdog";
        } else {
            CriticalEventLog.getInstance().logWatchdog(str, generateErrorId);
            Slog.e(TAG, "**SWT happen **" + str);
            this.mWdtSocExt.switchFtrace(2);
            String str3 = (this.mSfHang && str.isEmpty()) ? "surfaceflinger hang." : "";
            if (str3.isEmpty()) {
                str3 = str;
            }
            EventLog.writeEvent(EventLogTags.WATCHDOG, str3);
            this.mWdtSocExt.WDTMatterJava(420L);
            FrameworkStatsLog.write(185, str);
            str2 = "watchdog";
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        final StringBuilder sb = new StringBuilder();
        sb.append(ResourcePressureUtil.currentPsiState());
        ProcessCpuTracker processCpuTracker = new ProcessCpuTracker(false);
        StringWriter stringWriter = new StringWriter();
        mWdtExt.addBinderPid(arrayList, interestingNativePids, Process.myPid());
        final File dumpStackTraces = StackTracesDumpHelper.dumpStackTraces(arrayList, processCpuTracker, new SparseBooleanArray(), CompletableFuture.completedFuture(getInterestingNativePids()), stringWriter, str, logLinesForSystemServerTraceFile, new SystemServerInitThreadPool$$ExternalSyntheticLambda1(), null);
        if (dumpStackTraces != null) {
            SmartTraceUtils.dumpStackTraces(Process.myPid(), arrayList, interestingNativePids, dumpStackTraces);
        }
        SystemClock.sleep(5000L);
        processCpuTracker.update();
        sb.append(processCpuTracker.printCurrentState(uptimeMillis));
        sb.append(stringWriter.getBuffer());
        if (!z) {
            doSysRq('w');
            doSysRq('l');
        }
        mWdtExt.addStabilityDebugInAll(z, dumpStackTraces, str);
        final String str4 = str2;
        Thread thread = new Thread("watchdogWriteToDropbox") { // from class: com.android.server.Watchdog.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                if (Watchdog.this.mActivity != null) {
                    Watchdog.this.mActivity.addErrorToDropBox(str4, null, "system_server", null, null, null, null, sb.toString(), dumpStackTraces, null, null, null, generateErrorId);
                }
            }
        };
        thread.start();
        try {
            thread.join(2000L);
            if (z || !Build.isMtkPlatform()) {
                return;
            }
            Thread.sleep(8000L);
        } catch (InterruptedException unused) {
        }
    }

    private void doSysRq(char c) {
        try {
            FileWriter fileWriter = new FileWriter("/proc/sysrq-trigger");
            fileWriter.write(c);
            fileWriter.close();
        } catch (IOException e) {
            Slog.w(TAG, "Failed to write to /proc/sysrq-trigger", e);
        }
    }

    private void resetTimeoutHistory() {
        writeTimeoutHistory(new ArrayList());
    }

    private void writeTimeoutHistory(Iterable<String> iterable) {
        String join = String.join(",", iterable);
        try {
            FileWriter fileWriter = new FileWriter(TIMEOUT_HISTORY_FILE);
            try {
                fileWriter.write(SystemProperties.get("ro.boottime.zygote"));
                fileWriter.write(":");
                fileWriter.write(join);
                fileWriter.close();
            } finally {
            }
        } catch (IOException e) {
            Slog.e(TAG, "Failed to write file /data/system/watchdog-timeout-history.txt", e);
        }
    }

    private String[] readTimeoutHistory() {
        String[] strArr = new String[0];
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(TIMEOUT_HISTORY_FILE));
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    String[] split = readLine.trim().split(":");
                    String str = split.length >= 1 ? split[0] : "";
                    String str2 = split.length >= 2 ? split[1] : "";
                    if (!SystemProperties.get("ro.boottime.zygote").equals(str) || str2.isEmpty()) {
                        bufferedReader.close();
                        return strArr;
                    }
                    String[] split2 = str2.split(",");
                    bufferedReader.close();
                    return split2;
                }
                bufferedReader.close();
                return strArr;
            } catch (Throwable th) {
                try {
                    bufferedReader.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (FileNotFoundException unused) {
            return strArr;
        } catch (IOException e) {
            Slog.e(TAG, "Failed to read file /data/system/watchdog-timeout-history.txt", e);
            return strArr;
        }
    }

    private boolean hasActiveUsbConnection() {
        try {
            return "CONFIGURED".equals(FileUtils.readTextFile(new File("/sys/class/android_usb/android0/state"), 128, null).trim());
        } catch (IOException e) {
            Slog.w(TAG, "Failed to determine if device was on USB", e);
            return false;
        }
    }

    private boolean isCrashLoopFound() {
        int intValue = WatchdogProperties.fatal_count().orElse(0).intValue();
        long millis = TimeUnit.SECONDS.toMillis(WatchdogProperties.fatal_window_seconds().orElse(0).intValue());
        if (intValue == 0 || millis == 0) {
            if (intValue != millis) {
                Slog.w(TAG, String.format("sysprops '%s' and '%s' should be set or unset together", PROP_FATAL_LOOP_COUNT, PROP_FATAL_LOOP_WINDOWS_SECS));
            }
            return false;
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        String[] readTimeoutHistory = readTimeoutHistory();
        ArrayList arrayList = new ArrayList(Arrays.asList((String[]) Arrays.copyOfRange(readTimeoutHistory, Math.max(0, (readTimeoutHistory.length - intValue) - 1), readTimeoutHistory.length)));
        arrayList.add(String.valueOf(elapsedRealtime));
        writeTimeoutHistory(arrayList);
        if (hasActiveUsbConnection()) {
            return false;
        }
        try {
            return arrayList.size() >= intValue && elapsedRealtime - Long.parseLong((String) arrayList.get(0)) < millis;
        } catch (NumberFormatException e) {
            Slog.w(TAG, "Failed to parseLong " + ((String) arrayList.get(0)), e);
            resetTimeoutHistory();
            return false;
        }
    }

    private void breakCrashLoop() {
        try {
            FileWriter fileWriter = new FileWriter("/dev/kmsg_debug", true);
            try {
                fileWriter.append((CharSequence) "Fatal reset to escape the system_server crashing loop\n");
                fileWriter.close();
            } finally {
            }
        } catch (IOException e) {
            Slog.w(TAG, "Failed to append to kmsg", e);
        }
        doSysRq('c');
    }

    @Override // android.util.Dumpable
    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.print("WatchdogTimeoutMillis=");
        printWriter.println(this.mWatchdogTimeoutMillis);
    }

    private void appendFile(File file, File file2) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file2));
            FileWriter fileWriter = new FileWriter(file, true);
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    fileWriter.write(readLine);
                    fileWriter.write(10);
                } else {
                    bufferedReader.close();
                    fileWriter.close();
                    return;
                }
            }
        } catch (IOException e) {
            Slog.e(TAG, "Exception while writing watchdog traces to new file!");
            e.printStackTrace();
        }
    }
}
