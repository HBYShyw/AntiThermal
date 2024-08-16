package com.android.server;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.os.Environment;
import android.os.SystemClock;
import android.os.Trace;
import android.util.ArraySet;
import android.util.Dumpable;
import android.util.EventLog;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.SystemServerClassLoaderFactory;
import com.android.internal.util.Preconditions;
import com.android.server.SystemService;
import com.android.server.pm.ApexManager;
import com.android.server.pm.UserManagerInternal;
import com.android.server.utils.TimingsTraceAndSlog;
import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class SystemServiceManager implements Dumpable {
    private static final boolean DEBUG = false;
    private static final int DEFAULT_MAX_USER_POOL_THREADS = 3;
    private static final int SERVICE_CALL_WARN_TIME_MS = 50;
    private static final String TAG = SystemServiceManager.class.getSimpleName();
    private static final String USER_COMPLETED_EVENT = "CompletedEvent";
    private static final long USER_POOL_SHUTDOWN_TIMEOUT_SECONDS = 30;
    private static final String USER_STARTING = "Start";
    private static final String USER_STOPPED = "Cleanup";
    private static final String USER_STOPPING = "Stop";
    private static final String USER_SWITCHING = "Switch";
    private static final String USER_UNLOCKED = "Unlocked";
    private static final String USER_UNLOCKING = "Unlocking";
    private static volatile int sOtherServicesStartIndex;
    private static File sSystemDir;
    private final Context mContext;

    @GuardedBy({"mTargetUsers"})
    private SystemService.TargetUser mCurrentUser;
    private boolean mRuntimeRestarted;
    private long mRuntimeStartElapsedTime;
    private long mRuntimeStartUptime;
    private boolean mSafeMode;
    private UserManagerInternal mUserManagerInternal;
    private int mCurrentPhase = -1;

    @GuardedBy({"mTargetUsers"})
    private final SparseArray<SystemService.TargetUser> mTargetUsers = new SparseArray<>();
    private ISystemServiceManagerExt mSystemServiceManagerExt = (ISystemServiceManagerExt) ExtLoader.type(ISystemServiceManagerExt.class).create();
    private List<SystemService> mServices = new ArrayList();
    private Set<String> mServiceClassnames = new ArraySet();
    private final int mNumUserPoolThreads = Math.min(Runtime.getRuntime().availableProcessors(), 3);

    /* JADX INFO: Access modifiers changed from: package-private */
    public SystemServiceManager(Context context) {
        this.mContext = context;
    }

    public SystemService startService(String str) {
        return startService(loadClassFromLoader(str, SystemServiceManager.class.getClassLoader()));
    }

    public SystemService startServiceFromJar(String str, String str2) {
        return startService(loadClassFromLoader(str, SystemServerClassLoaderFactory.getOrCreateClassLoader(str2, SystemServiceManager.class.getClassLoader(), isJarInTestApex(str2))));
    }

    private boolean isJarInTestApex(String str) {
        Path path = Paths.get(str, new String[0]);
        if (path.getNameCount() < 2 || !path.getName(0).toString().equals("apex")) {
            return false;
        }
        try {
            return (this.mContext.getPackageManager().getPackageInfo(ApexManager.getInstance().getActivePackageNameForApexModuleName(path.getName(1).toString()), PackageManager.PackageInfoFlags.of(1073741824L)).applicationInfo.flags & 256) != 0;
        } catch (Exception unused) {
            return false;
        }
    }

    private static Class<SystemService> loadClassFromLoader(String str, ClassLoader classLoader) {
        try {
            return Class.forName(str, true, classLoader);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to create service " + str + " from class loader " + classLoader.toString() + ": service class not found, usually indicates that the caller should have called PackageManager.hasSystemFeature() to check whether the feature is available on this device before trying to start the services that implement it. Also ensure that the correct path for the classloader is supplied, if applicable.", e);
        }
    }

    public <T extends SystemService> T startService(Class<T> cls) {
        try {
            String name = cls.getName();
            Slog.i(TAG, "Starting " + name);
            Trace.traceBegin(524288L, "StartService " + name);
            if (!SystemService.class.isAssignableFrom(cls)) {
                throw new RuntimeException("Failed to create " + name + ": service must extend " + SystemService.class.getName());
            }
            try {
                try {
                    try {
                        T newInstance = cls.getConstructor(Context.class).newInstance(this.mContext);
                        startService(newInstance);
                        return newInstance;
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Failed to create service " + name + ": service must have a public constructor with a Context argument", e);
                    } catch (InvocationTargetException e2) {
                        throw new RuntimeException("Failed to create service " + name + ": service constructor threw an exception", e2);
                    }
                } catch (NoSuchMethodException e3) {
                    throw new RuntimeException("Failed to create service " + name + ": service must have a public constructor with a Context argument", e3);
                }
            } catch (InstantiationException e4) {
                throw new RuntimeException("Failed to create service " + name + ": service could not be instantiated", e4);
            }
        } finally {
            Trace.traceEnd(524288L);
        }
    }

    public void startService(SystemService systemService) {
        String name = systemService.getClass().getName();
        if (this.mServiceClassnames.contains(name)) {
            Slog.i(TAG, "Not starting an already started service " + name);
            return;
        }
        this.mServiceClassnames.add(name);
        this.mServices.add(systemService);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        try {
            systemService.onStart();
            warnIfTooLong(SystemClock.elapsedRealtime() - elapsedRealtime, systemService, "onStart");
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to start service " + systemService.getClass().getName() + ": onStart threw an exception", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sealStartedServices() {
        this.mServiceClassnames = Collections.emptySet();
        this.mServices = Collections.unmodifiableList(this.mServices);
    }

    public void startBootPhase(TimingsTraceAndSlog timingsTraceAndSlog, int i) {
        if (i <= this.mCurrentPhase) {
            throw new IllegalArgumentException("Next phase must be larger than previous");
        }
        this.mCurrentPhase = i;
        Slog.i(TAG, "Starting phase " + this.mCurrentPhase);
        try {
            timingsTraceAndSlog.traceBegin("OnBootPhase_" + i);
            this.mSystemServiceManagerExt.colorSystemServiceOnBootPhase(i);
            int size = this.mServices.size();
            for (int i2 = 0; i2 < size; i2++) {
                SystemService systemService = this.mServices.get(i2);
                long elapsedRealtime = SystemClock.elapsedRealtime();
                timingsTraceAndSlog.traceBegin("OnBootPhase_" + i + "_" + systemService.getClass().getName());
                try {
                    systemService.onBootPhase(this.mCurrentPhase);
                    warnIfTooLong(SystemClock.elapsedRealtime() - elapsedRealtime, systemService, "onBootPhase");
                    timingsTraceAndSlog.traceEnd();
                } catch (Exception e) {
                    throw new RuntimeException("Failed to boot service " + systemService.getClass().getName() + ": onBootPhase threw an exception during phase " + this.mCurrentPhase, e);
                }
            }
            timingsTraceAndSlog.traceEnd();
            if (i == 1000) {
                timingsTraceAndSlog.logDuration("TotalBootTime", SystemClock.uptimeMillis() - this.mRuntimeStartUptime);
                SystemServerInitThreadPool.shutdown();
            }
        } catch (Throwable th) {
            timingsTraceAndSlog.traceEnd();
            throw th;
        }
    }

    public boolean isBootCompleted() {
        return this.mCurrentPhase >= 1000;
    }

    public void updateOtherServicesStartIndex() {
        if (isBootCompleted()) {
            return;
        }
        sOtherServicesStartIndex = this.mServices.size();
    }

    public void preSystemReady() {
        this.mUserManagerInternal = (UserManagerInternal) LocalServices.getService(UserManagerInternal.class);
    }

    private SystemService.TargetUser getTargetUser(int i) {
        SystemService.TargetUser targetUser;
        synchronized (this.mTargetUsers) {
            targetUser = this.mTargetUsers.get(i);
        }
        return targetUser;
    }

    private SystemService.TargetUser newTargetUser(int i) {
        UserInfo userInfo = this.mUserManagerInternal.getUserInfo(i);
        Preconditions.checkState(userInfo != null, "No UserInfo for " + i);
        return new SystemService.TargetUser(userInfo);
    }

    public void onUserStarting(TimingsTraceAndSlog timingsTraceAndSlog, int i) {
        SystemService.TargetUser newTargetUser = newTargetUser(i);
        synchronized (this.mTargetUsers) {
            if (i == 0) {
                if (this.mTargetUsers.contains(i)) {
                    Slog.e(TAG, "Skipping starting system user twice");
                    return;
                }
            }
            this.mTargetUsers.put(i, newTargetUser);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("put userid ");
            sb.append(i);
            sb.append(" to mTargetUsers, result = ");
            sb.append(this.mTargetUsers.get(i) != null ? "true" : "false");
            Slog.d(str, sb.toString());
            EventLog.writeEvent(com.android.server.am.EventLogTags.SSM_USER_STARTING, i);
            onUser(timingsTraceAndSlog, USER_STARTING, null, newTargetUser);
        }
    }

    public void onUserUnlocking(int i) {
        EventLog.writeEvent(com.android.server.am.EventLogTags.SSM_USER_UNLOCKING, i);
        onUser(USER_UNLOCKING, i);
    }

    public void onUserUnlocked(int i) {
        EventLog.writeEvent(com.android.server.am.EventLogTags.SSM_USER_UNLOCKED, i);
        onUser(USER_UNLOCKED, i);
    }

    public void onUserSwitching(int i, int i2) {
        SystemService.TargetUser targetUser;
        SystemService.TargetUser targetUser2;
        EventLog.writeEvent(com.android.server.am.EventLogTags.SSM_USER_SWITCHING, Integer.valueOf(i), Integer.valueOf(i2));
        synchronized (this.mTargetUsers) {
            SystemService.TargetUser targetUser3 = this.mCurrentUser;
            if (targetUser3 == null) {
                targetUser = newTargetUser(i);
            } else {
                if (i != targetUser3.getUserIdentifier()) {
                    Slog.wtf(TAG, "switchUser(" + i + "," + i2 + "): mCurrentUser is " + this.mCurrentUser + ", it should be " + i);
                }
                targetUser = this.mCurrentUser;
            }
            targetUser2 = getTargetUser(i2);
            this.mCurrentUser = targetUser2;
            Preconditions.checkState(targetUser2 != null, "No TargetUser for " + i2);
        }
        onUser(TimingsTraceAndSlog.newAsyncLog(), USER_SWITCHING, targetUser, targetUser2);
    }

    public void onUserStopping(int i) {
        EventLog.writeEvent(com.android.server.am.EventLogTags.SSM_USER_STOPPING, i);
        onUser(USER_STOPPING, i);
    }

    public void onUserStopped(int i) {
        EventLog.writeEvent(com.android.server.am.EventLogTags.SSM_USER_STOPPED, i);
        onUser(USER_STOPPED, i);
        synchronized (this.mTargetUsers) {
            this.mTargetUsers.remove(i);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("removed userid ");
            sb.append(i);
            sb.append(" from mTargetUsers, result = ");
            sb.append(this.mTargetUsers.get(i) == null ? "true" : "false");
            Slog.d(str, sb.toString());
        }
    }

    public void onUserCompletedEvent(int i, int i2) {
        SystemService.TargetUser targetUser;
        EventLog.writeEvent(com.android.server.am.EventLogTags.SSM_USER_COMPLETED_EVENT, Integer.valueOf(i), Integer.valueOf(i2));
        if (i2 == 0 || (targetUser = getTargetUser(i)) == null) {
            return;
        }
        onUser(TimingsTraceAndSlog.newAsyncLog(), USER_COMPLETED_EVENT, null, targetUser, new SystemService.UserCompletedEventType(i2));
    }

    private void onUser(String str, int i) {
        SystemService.TargetUser targetUser = getTargetUser(i);
        Preconditions.checkState(targetUser != null, "No TargetUser for " + i);
        onUser(TimingsTraceAndSlog.newAsyncLog(), str, null, targetUser);
    }

    private void onUser(TimingsTraceAndSlog timingsTraceAndSlog, String str, SystemService.TargetUser targetUser, SystemService.TargetUser targetUser2) {
        onUser(timingsTraceAndSlog, str, targetUser, targetUser2, null);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:28:0x016e. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:31:0x01ba. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01bd  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x028e  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x02c5  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x02f0  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x02b4  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x01cc  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x01f5  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0205 A[Catch: Exception -> 0x0266, TryCatch #3 {Exception -> 0x0266, blocks: (B:33:0x01c8, B:34:0x026a, B:35:0x027e, B:61:0x0200, B:62:0x0205, B:63:0x0214, B:64:0x0223, B:67:0x023f, B:68:0x024a, B:69:0x0247, B:70:0x0250), top: B:60:0x0200 }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0214 A[Catch: Exception -> 0x0266, TryCatch #3 {Exception -> 0x0266, blocks: (B:33:0x01c8, B:34:0x026a, B:35:0x027e, B:61:0x0200, B:62:0x0205, B:63:0x0214, B:64:0x0223, B:67:0x023f, B:68:0x024a, B:69:0x0247, B:70:0x0250), top: B:60:0x0200 }] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0223 A[Catch: Exception -> 0x0266, TryCatch #3 {Exception -> 0x0266, blocks: (B:33:0x01c8, B:34:0x026a, B:35:0x027e, B:61:0x0200, B:62:0x0205, B:63:0x0214, B:64:0x0223, B:67:0x023f, B:68:0x024a, B:69:0x0247, B:70:0x0250), top: B:60:0x0200 }] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0232  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0250 A[Catch: Exception -> 0x0266, TryCatch #3 {Exception -> 0x0266, blocks: (B:33:0x01c8, B:34:0x026a, B:35:0x027e, B:61:0x0200, B:62:0x0205, B:63:0x0214, B:64:0x0223, B:67:0x023f, B:68:0x024a, B:69:0x0247, B:70:0x0250), top: B:60:0x0200 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void onUser(TimingsTraceAndSlog timingsTraceAndSlog, String str, SystemService.TargetUser targetUser, SystemService.TargetUser targetUser2, SystemService.UserCompletedEventType userCompletedEventType) {
        String str2;
        boolean z;
        SystemService systemService;
        int i;
        String str3;
        int i2;
        String str4;
        SystemService systemService2;
        ExecutorService executorService;
        int i3;
        int i4;
        char c;
        String str5;
        int userIdentifier = targetUser2.getUserIdentifier();
        timingsTraceAndSlog.traceBegin("ssm." + str + "User-" + userIdentifier);
        String str6 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Calling on");
        sb.append(str);
        sb.append("User ");
        sb.append(userIdentifier);
        if (targetUser != null) {
            str2 = " (from " + targetUser + ")";
        } else {
            str2 = "";
        }
        sb.append(str2);
        Slog.i(str6, sb.toString());
        boolean useThreadPool = useThreadPool(userIdentifier, str);
        ExecutorService newFixedThreadPool = useThreadPool ? Executors.newFixedThreadPool(this.mNumUserPoolThreads) : null;
        int size = this.mServices.size();
        Slog.i(str6, "Starting phase " + this.mCurrentPhase + " for " + size + " services.");
        this.mSystemServiceManagerExt.initTimeCosted();
        boolean z2 = false;
        int i5 = 0;
        while (i5 < size) {
            SystemService systemService3 = this.mServices.get(i5);
            String name = systemService3.getClass().getName();
            boolean isUserSupported = systemService3.isUserSupported(targetUser2);
            if (!isUserSupported && targetUser != null) {
                isUserSupported = systemService3.isUserSupported(targetUser);
            }
            if (!isUserSupported) {
                Slog.i(TAG, "Skipping " + str + "User-" + userIdentifier + " on " + name);
                i4 = i5;
                i = size;
                i3 = userIdentifier;
                z = useThreadPool;
                executorService = newFixedThreadPool;
            } else {
                boolean z3 = useThreadPool && useThreadPoolForService(str, i5);
                if (!z3) {
                    timingsTraceAndSlog.traceBegin("ssm.on" + str + "User-" + userIdentifier + "_" + name);
                }
                z = useThreadPool;
                long elapsedRealtime = SystemClock.elapsedRealtime();
                int i6 = userIdentifier;
                if (this.mSystemServiceManagerExt.isDebuggable()) {
                    String str7 = TAG;
                    systemService = systemService3;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onBootPhase ");
                    i = size;
                    sb2.append(this.mCurrentPhase);
                    sb2.append(" begin for server[");
                    sb2.append(i5);
                    sb2.append("]:");
                    sb2.append(name);
                    Slog.i(str7, sb2.toString());
                } else {
                    systemService = systemService3;
                    i = size;
                }
                try {
                } catch (Exception e) {
                    e = e;
                    str3 = "]:";
                    i2 = i5;
                    str4 = "onBootPhase ";
                    systemService2 = systemService;
                    executorService = newFixedThreadPool;
                }
                switch (str.hashCode()) {
                    case -1805606060:
                        if (str.equals(USER_SWITCHING)) {
                            c = 0;
                            switch (c) {
                                case 0:
                                    str3 = "]:";
                                    i2 = i5;
                                    str4 = "onBootPhase ";
                                    systemService2 = systemService;
                                    str5 = name;
                                    executorService = newFixedThreadPool;
                                    systemService2.onUserSwitching(targetUser, targetUser2);
                                    this.mSystemServiceManagerExt.setCustomOnWhatToSwitch();
                                    name = str5;
                                    if (z3) {
                                        i3 = i6;
                                    } else {
                                        long elapsedRealtime2 = SystemClock.elapsedRealtime() - elapsedRealtime;
                                        StringBuilder sb3 = new StringBuilder();
                                        sb3.append("on");
                                        sb3.append(str);
                                        sb3.append("User-");
                                        i3 = i6;
                                        sb3.append(i3);
                                        warnIfTooLong(elapsedRealtime2, systemService2, sb3.toString());
                                        timingsTraceAndSlog.traceEnd();
                                    }
                                    this.mSystemServiceManagerExt.recordTimeOut(elapsedRealtime, 50, name);
                                    if (!this.mSystemServiceManagerExt.isDebuggable()) {
                                        i4 = i2;
                                        break;
                                    } else {
                                        String str8 = TAG;
                                        StringBuilder sb4 = new StringBuilder();
                                        sb4.append(str4);
                                        sb4.append(this.mCurrentPhase);
                                        sb4.append("   end for server[");
                                        i4 = i2;
                                        sb4.append(i4);
                                        sb4.append(str3);
                                        sb4.append(name);
                                        Slog.i(str8, sb4.toString());
                                        break;
                                    }
                                case 1:
                                    str3 = "]:";
                                    i2 = i5;
                                    str4 = "onBootPhase ";
                                    systemService2 = systemService;
                                    str5 = name;
                                    executorService = newFixedThreadPool;
                                    if (z3) {
                                        executorService.submit(getOnUserStartingRunnable(timingsTraceAndSlog, systemService2, targetUser2));
                                    } else {
                                        systemService2.onUserStarting(targetUser2);
                                    }
                                    this.mSystemServiceManagerExt.setCustomOnWhatToStart();
                                    name = str5;
                                    if (z3) {
                                    }
                                    this.mSystemServiceManagerExt.recordTimeOut(elapsedRealtime, 50, name);
                                    if (!this.mSystemServiceManagerExt.isDebuggable()) {
                                    }
                                    break;
                                case 2:
                                    str3 = "]:";
                                    i2 = i5;
                                    str4 = "onBootPhase ";
                                    systemService2 = systemService;
                                    str5 = name;
                                    executorService = newFixedThreadPool;
                                    systemService2.onUserUnlocking(targetUser2);
                                    name = str5;
                                    if (z3) {
                                    }
                                    this.mSystemServiceManagerExt.recordTimeOut(elapsedRealtime, 50, name);
                                    if (!this.mSystemServiceManagerExt.isDebuggable()) {
                                    }
                                    break;
                                case 3:
                                    str3 = "]:";
                                    i2 = i5;
                                    str4 = "onBootPhase ";
                                    systemService2 = systemService;
                                    str5 = name;
                                    executorService = newFixedThreadPool;
                                    systemService2.onUserUnlocked(targetUser2);
                                    name = str5;
                                    if (z3) {
                                    }
                                    this.mSystemServiceManagerExt.recordTimeOut(elapsedRealtime, 50, name);
                                    if (!this.mSystemServiceManagerExt.isDebuggable()) {
                                    }
                                    break;
                                case 4:
                                    str3 = "]:";
                                    i2 = i5;
                                    str4 = "onBootPhase ";
                                    systemService2 = systemService;
                                    str5 = name;
                                    executorService = newFixedThreadPool;
                                    systemService2.onUserStopping(targetUser2);
                                    name = str5;
                                    if (z3) {
                                    }
                                    this.mSystemServiceManagerExt.recordTimeOut(elapsedRealtime, 50, name);
                                    if (!this.mSystemServiceManagerExt.isDebuggable()) {
                                    }
                                    break;
                                case 5:
                                    str3 = "]:";
                                    i2 = i5;
                                    str4 = "onBootPhase ";
                                    systemService2 = systemService;
                                    str5 = name;
                                    executorService = newFixedThreadPool;
                                    try {
                                        systemService2.onUserStopped(targetUser2);
                                        name = str5;
                                    } catch (Exception e2) {
                                        e = e2;
                                        name = str5;
                                        logFailure(str, targetUser2, name, e);
                                        if (z3) {
                                        }
                                        this.mSystemServiceManagerExt.recordTimeOut(elapsedRealtime, 50, name);
                                        if (!this.mSystemServiceManagerExt.isDebuggable()) {
                                        }
                                        i5 = i4 + 1;
                                        newFixedThreadPool = executorService;
                                        useThreadPool = z;
                                        userIdentifier = i3;
                                        size = i;
                                    }
                                    if (z3) {
                                    }
                                    this.mSystemServiceManagerExt.recordTimeOut(elapsedRealtime, 50, name);
                                    if (!this.mSystemServiceManagerExt.isDebuggable()) {
                                    }
                                    break;
                                case 6:
                                    SystemService systemService4 = systemService;
                                    str5 = name;
                                    i2 = i5;
                                    str3 = "]:";
                                    str4 = "onBootPhase ";
                                    executorService = newFixedThreadPool;
                                    try {
                                        executorService.submit(getOnUserCompletedEventRunnable(timingsTraceAndSlog, systemService4, str5, targetUser2, userCompletedEventType));
                                        systemService2 = systemService4;
                                        name = str5;
                                    } catch (Exception e3) {
                                        e = e3;
                                        systemService2 = systemService4;
                                        name = str5;
                                        logFailure(str, targetUser2, name, e);
                                        if (z3) {
                                        }
                                        this.mSystemServiceManagerExt.recordTimeOut(elapsedRealtime, 50, name);
                                        if (!this.mSystemServiceManagerExt.isDebuggable()) {
                                        }
                                        i5 = i4 + 1;
                                        newFixedThreadPool = executorService;
                                        useThreadPool = z;
                                        userIdentifier = i3;
                                        size = i;
                                    }
                                    if (z3) {
                                    }
                                    this.mSystemServiceManagerExt.recordTimeOut(elapsedRealtime, 50, name);
                                    if (!this.mSystemServiceManagerExt.isDebuggable()) {
                                    }
                                    break;
                                default:
                                    str3 = "]:";
                                    i2 = i5;
                                    str4 = "onBootPhase ";
                                    systemService2 = systemService;
                                    str5 = name;
                                    executorService = newFixedThreadPool;
                                    throw new IllegalArgumentException(str + " what?");
                                    break;
                            }
                        }
                        c = 65535;
                        switch (c) {
                        }
                    case -1773539708:
                        if (str.equals(USER_STOPPED)) {
                            c = 5;
                            switch (c) {
                            }
                        }
                        c = 65535;
                        switch (c) {
                        }
                    case -240492034:
                        if (str.equals(USER_UNLOCKING)) {
                            c = 2;
                            switch (c) {
                            }
                        }
                        c = 65535;
                        switch (c) {
                        }
                    case -146305277:
                        if (str.equals(USER_UNLOCKED)) {
                            c = 3;
                            switch (c) {
                            }
                        }
                        c = 65535;
                        switch (c) {
                        }
                    case 2587682:
                        if (str.equals(USER_STOPPING)) {
                            c = 4;
                            switch (c) {
                            }
                        }
                        c = 65535;
                        switch (c) {
                        }
                    case 80204866:
                        if (str.equals(USER_STARTING)) {
                            c = 1;
                            switch (c) {
                            }
                        }
                        c = 65535;
                        switch (c) {
                        }
                    case 537825071:
                        if (str.equals(USER_COMPLETED_EVENT)) {
                            c = 6;
                            switch (c) {
                            }
                        }
                        c = 65535;
                        switch (c) {
                        }
                    default:
                        c = 65535;
                        switch (c) {
                        }
                }
            }
            i5 = i4 + 1;
            newFixedThreadPool = executorService;
            useThreadPool = z;
            userIdentifier = i3;
            size = i;
        }
        int i7 = userIdentifier;
        ExecutorService executorService2 = newFixedThreadPool;
        if (useThreadPool) {
            executorService2.shutdown();
            try {
                z2 = executorService2.awaitTermination(30L, TimeUnit.SECONDS);
            } catch (InterruptedException e4) {
                logFailure(str, targetUser2, "(user lifecycle threadpool was interrupted)", e4);
            }
            if (!z2) {
                logFailure(str, targetUser2, "(user lifecycle threadpool was not terminated)", null);
            }
        }
        timingsTraceAndSlog.traceEnd();
        this.mSystemServiceManagerExt.onUserExit(i7);
    }

    private boolean useThreadPool(int i, String str) {
        str.hashCode();
        return !str.equals(USER_STARTING) ? str.equals(USER_COMPLETED_EVENT) : (ActivityManager.isLowRamDeviceStatic() || i == 0) ? false : true;
    }

    private boolean useThreadPoolForService(String str, int i) {
        str.hashCode();
        return !str.equals(USER_STARTING) ? str.equals(USER_COMPLETED_EVENT) : i >= sOtherServicesStartIndex;
    }

    private Runnable getOnUserStartingRunnable(final TimingsTraceAndSlog timingsTraceAndSlog, final SystemService systemService, final SystemService.TargetUser targetUser) {
        return new Runnable() { // from class: com.android.server.SystemServiceManager$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SystemServiceManager.this.lambda$getOnUserStartingRunnable$0(timingsTraceAndSlog, systemService, targetUser);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getOnUserStartingRunnable$0(TimingsTraceAndSlog timingsTraceAndSlog, SystemService systemService, SystemService.TargetUser targetUser) {
        TimingsTraceAndSlog timingsTraceAndSlog2 = new TimingsTraceAndSlog(timingsTraceAndSlog);
        String name = systemService.getClass().getName();
        int userIdentifier = targetUser.getUserIdentifier();
        timingsTraceAndSlog2.traceBegin("ssm.onStartUser-" + userIdentifier + "_" + name);
        try {
            try {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                systemService.onUserStarting(targetUser);
                warnIfTooLong(SystemClock.elapsedRealtime() - elapsedRealtime, systemService, "onStartUser-" + userIdentifier);
            } catch (Exception e) {
                logFailure(USER_STARTING, targetUser, name, e);
            }
        } finally {
            timingsTraceAndSlog2.traceEnd();
        }
    }

    private Runnable getOnUserCompletedEventRunnable(final TimingsTraceAndSlog timingsTraceAndSlog, final SystemService systemService, final String str, final SystemService.TargetUser targetUser, final SystemService.UserCompletedEventType userCompletedEventType) {
        return new Runnable() { // from class: com.android.server.SystemServiceManager$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                SystemServiceManager.this.lambda$getOnUserCompletedEventRunnable$1(timingsTraceAndSlog, targetUser, userCompletedEventType, str, systemService);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getOnUserCompletedEventRunnable$1(TimingsTraceAndSlog timingsTraceAndSlog, SystemService.TargetUser targetUser, SystemService.UserCompletedEventType userCompletedEventType, String str, SystemService systemService) {
        TimingsTraceAndSlog timingsTraceAndSlog2 = new TimingsTraceAndSlog(timingsTraceAndSlog);
        int userIdentifier = targetUser.getUserIdentifier();
        timingsTraceAndSlog2.traceBegin("ssm.onCompletedEventUser-" + userIdentifier + "_" + userCompletedEventType + "_" + str);
        try {
            try {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                systemService.onUserCompletedEvent(targetUser, userCompletedEventType);
                warnIfTooLong(SystemClock.elapsedRealtime() - elapsedRealtime, systemService, "onCompletedEventUser-" + userIdentifier);
            } catch (Exception e) {
                logFailure(USER_COMPLETED_EVENT, targetUser, str, e);
                throw e;
            }
        } finally {
            timingsTraceAndSlog2.traceEnd();
        }
    }

    private void logFailure(String str, SystemService.TargetUser targetUser, String str2, Exception exc) {
        Slog.wtf(TAG, "SystemService failure: Failure reporting " + str + " of user " + targetUser + " to service " + str2, exc);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSafeMode(boolean z) {
        this.mSafeMode = z;
    }

    public boolean isSafeMode() {
        return this.mSafeMode;
    }

    public boolean isRuntimeRestarted() {
        return this.mRuntimeRestarted;
    }

    public long getRuntimeStartElapsedTime() {
        return this.mRuntimeStartElapsedTime;
    }

    public long getRuntimeStartUptime() {
        return this.mRuntimeStartUptime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setStartInfo(boolean z, long j, long j2) {
        this.mRuntimeRestarted = z;
        this.mRuntimeStartElapsedTime = j;
        this.mRuntimeStartUptime = j2;
    }

    private void warnIfTooLong(long j, SystemService systemService, String str) {
        if (j > 50) {
            Slog.w(TAG, "Service " + systemService.getClass().getName() + " took " + j + " ms in " + str);
        }
    }

    @Deprecated
    public static File ensureSystemDir() {
        if (sSystemDir == null) {
            File file = new File(Environment.getDataDirectory(), "system");
            sSystemDir = file;
            file.mkdirs();
        }
        return sSystemDir;
    }

    @Override // android.util.Dumpable
    public String getDumpableName() {
        return SystemServiceManager.class.getSimpleName();
    }

    @Override // android.util.Dumpable
    public void dump(PrintWriter printWriter, String[] strArr) {
        int i;
        printWriter.printf("Current phase: %d\n", Integer.valueOf(this.mCurrentPhase));
        synchronized (this.mTargetUsers) {
            if (this.mCurrentUser != null) {
                printWriter.print("Current user: ");
                this.mCurrentUser.dump(printWriter);
                printWriter.println();
            } else {
                printWriter.println("Current user not set!");
            }
            int size = this.mTargetUsers.size();
            if (size > 0) {
                printWriter.printf("%d target users: ", Integer.valueOf(size));
                for (int i2 = 0; i2 < size; i2++) {
                    this.mTargetUsers.valueAt(i2).dump(printWriter);
                    if (i2 != size - 1) {
                        printWriter.print(", ");
                    }
                }
                printWriter.println();
            } else {
                printWriter.println("No target users");
            }
        }
        int size2 = this.mServices.size();
        if (size2 > 0) {
            printWriter.printf("%d started services:\n", Integer.valueOf(size2));
            for (i = 0; i < size2; i++) {
                SystemService systemService = this.mServices.get(i);
                printWriter.print("  ");
                printWriter.println(systemService.getClass().getCanonicalName());
            }
            return;
        }
        printWriter.println("No started services");
    }
}
