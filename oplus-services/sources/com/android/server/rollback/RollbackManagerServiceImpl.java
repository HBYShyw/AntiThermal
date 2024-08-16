package com.android.server.rollback;

import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.content.pm.ParceledListSlice;
import android.content.pm.VersionedPackage;
import android.content.pm.parsing.ApkLite;
import android.content.pm.parsing.ApkLiteParseUtils;
import android.content.pm.parsing.result.ParseResult;
import android.content.pm.parsing.result.ParseTypeImpl;
import android.content.rollback.IRollbackManager;
import android.content.rollback.RollbackInfo;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.ext.SdkExtensions;
import android.provider.DeviceConfig;
import android.util.Log;
import android.util.LongArrayQueue;
import android.util.Slog;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.IndentingPrintWriter;
import com.android.internal.util.Preconditions;
import com.android.server.LocalServices;
import com.android.server.PackageWatchdog;
import com.android.server.SystemConfig;
import com.android.server.Watchdog;
import com.android.server.pm.ApexManager;
import com.android.server.pm.DumpState;
import com.android.server.pm.Installer;
import com.android.server.rollback.RollbackManagerServiceImpl;
import java.io.File;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class RollbackManagerServiceImpl extends IRollbackManager.Stub implements RollbackManagerInternal {
    private final AppDataRollbackHelper mAppDataRollbackHelper;
    private final Context mContext;
    private final Executor mExecutor;
    private final Handler mHandler;
    private final Installer mInstaller;
    private final RollbackPackageHealthObserver mPackageHealthObserver;
    private final RollbackStore mRollbackStore;
    private static final String TAG = "RollbackManager";
    private static final boolean LOCAL_LOGV = Log.isLoggable(TAG, 2);
    private static final long DEFAULT_ROLLBACK_LIFETIME_DURATION_MILLIS = TimeUnit.DAYS.toMillis(14);
    private static final long HANDLER_THREAD_TIMEOUT_DURATION_MILLIS = TimeUnit.MINUTES.toMillis(10);
    private long mRollbackLifetimeDurationInMillis = DEFAULT_ROLLBACK_LIFETIME_DURATION_MILLIS;
    private final Random mRandom = new SecureRandom();
    private final SparseBooleanArray mAllocatedRollbackIds = new SparseBooleanArray();
    private final List<Rollback> mRollbacks = new ArrayList();
    private final Runnable mRunExpiration = new Runnable() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda11
        @Override // java.lang.Runnable
        public final void run() {
            RollbackManagerServiceImpl.this.runExpiration();
        }
    };
    private final LongArrayQueue mSleepDuration = new LongArrayQueue();
    private long mRelativeBootTime = calculateRelativeBootTime();

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private @interface ExtThread {
    }

    /* renamed from: -$$Nest$smcalculateRelativeBootTime, reason: not valid java name */
    static /* bridge */ /* synthetic */ long m3080$$Nest$smcalculateRelativeBootTime() {
        return calculateRelativeBootTime();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RollbackManagerServiceImpl(final Context context) {
        this.mContext = context;
        Installer installer = new Installer(context);
        this.mInstaller = installer;
        installer.onStart();
        this.mRollbackStore = new RollbackStore(new File(Environment.getDataDirectory(), "rollback"), new File(Environment.getDataDirectory(), "rollback-history"));
        this.mPackageHealthObserver = new RollbackPackageHealthObserver(context);
        this.mAppDataRollbackHelper = new AppDataRollbackHelper(installer);
        HandlerThread handlerThread = new HandlerThread("RollbackManagerServiceHandler");
        handlerThread.start();
        this.mHandler = new Handler(handlerThread.getLooper());
        Watchdog.getInstance().addThread(getHandler(), HANDLER_THREAD_TIMEOUT_DURATION_MILLIS);
        this.mExecutor = new HandlerExecutor(getHandler());
        getHandler().post(new Runnable() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                RollbackManagerServiceImpl.this.lambda$new$0(context);
            }
        });
        Iterator it = ((UserManager) context.getSystemService(UserManager.class)).getUserHandles(true).iterator();
        while (it.hasNext()) {
            registerUserCallbacks((UserHandle) it.next());
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ENABLE_ROLLBACK");
        try {
            intentFilter.addDataType("application/vnd.android.package-archive");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            Slog.e(TAG, "addDataType", e);
        }
        this.mContext.registerReceiver(new AnonymousClass1(), intentFilter, null, getHandler());
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.intent.action.CANCEL_ENABLE_ROLLBACK");
        this.mContext.registerReceiver(new BroadcastReceiver() { // from class: com.android.server.rollback.RollbackManagerServiceImpl.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                RollbackManagerServiceImpl.this.assertInWorkerThread();
                if ("android.intent.action.CANCEL_ENABLE_ROLLBACK".equals(intent.getAction())) {
                    int intExtra = intent.getIntExtra("android.content.pm.extra.ENABLE_ROLLBACK_SESSION_ID", -1);
                    if (RollbackManagerServiceImpl.LOCAL_LOGV) {
                        Slog.v(RollbackManagerServiceImpl.TAG, "broadcast=ACTION_CANCEL_ENABLE_ROLLBACK id=" + intExtra);
                    }
                    Rollback rollbackForSession = RollbackManagerServiceImpl.this.getRollbackForSession(intExtra);
                    if (rollbackForSession == null || !rollbackForSession.isEnabling()) {
                        return;
                    }
                    RollbackManagerServiceImpl.this.mRollbacks.remove(rollbackForSession);
                    RollbackManagerServiceImpl.this.deleteRollback(rollbackForSession, "Rollback canceled");
                }
            }
        }, intentFilter2, null, getHandler());
        this.mContext.registerReceiver(new BroadcastReceiver() { // from class: com.android.server.rollback.RollbackManagerServiceImpl.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                int intExtra;
                RollbackManagerServiceImpl.this.assertInWorkerThread();
                if (!"android.intent.action.USER_ADDED".equals(intent.getAction()) || (intExtra = intent.getIntExtra("android.intent.extra.user_handle", -1)) == -1) {
                    return;
                }
                RollbackManagerServiceImpl.this.registerUserCallbacks(UserHandle.of(intExtra));
            }
        }, new IntentFilter("android.intent.action.USER_ADDED"), null, getHandler());
        registerTimeChangeReceiver();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Context context) {
        this.mRollbacks.addAll(this.mRollbackStore.loadRollbacks());
        if (!context.getPackageManager().isDeviceUpgrading()) {
            Iterator<Rollback> it = this.mRollbacks.iterator();
            while (it.hasNext()) {
                this.mAllocatedRollbackIds.put(it.next().info.getRollbackId(), true);
            }
            return;
        }
        Iterator<Rollback> it2 = this.mRollbacks.iterator();
        while (it2.hasNext()) {
            deleteRollback(it2.next(), "Fingerprint changed");
        }
        this.mRollbacks.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.rollback.RollbackManagerServiceImpl$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass1 extends BroadcastReceiver {
        AnonymousClass1() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            RollbackManagerServiceImpl.this.assertInWorkerThread();
            if ("android.intent.action.PACKAGE_ENABLE_ROLLBACK".equals(intent.getAction())) {
                final int intExtra = intent.getIntExtra("android.content.pm.extra.ENABLE_ROLLBACK_TOKEN", -1);
                final int intExtra2 = intent.getIntExtra("android.content.pm.extra.ENABLE_ROLLBACK_SESSION_ID", -1);
                RollbackManagerServiceImpl.this.queueSleepIfNeeded();
                RollbackManagerServiceImpl.this.getHandler().post(new Runnable() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        RollbackManagerServiceImpl.AnonymousClass1.this.lambda$onReceive$0(intExtra2, intExtra);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0(int i, int i2) {
            RollbackManagerServiceImpl.this.assertInWorkerThread();
            ((PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class)).setEnableRollbackCode(i2, !RollbackManagerServiceImpl.this.enableRollback(i) ? -1 : 1);
        }
    }

    private <U> U awaitResult(Supplier<U> supplier) {
        assertNotInWorkerThread();
        try {
            return (U) CompletableFuture.supplyAsync(supplier, this.mExecutor).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void awaitResult(Runnable runnable) {
        assertNotInWorkerThread();
        try {
            CompletableFuture.runAsync(runnable, this.mExecutor).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void assertInWorkerThread() {
        Preconditions.checkState(getHandler().getLooper().isCurrentThread());
    }

    private void assertNotInWorkerThread() {
        Preconditions.checkState(!getHandler().getLooper().isCurrentThread());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerUserCallbacks(UserHandle userHandle) {
        Context contextAsUser = getContextAsUser(userHandle);
        if (contextAsUser == null) {
            Slog.e(TAG, "Unable to register user callbacks for user " + userHandle);
            return;
        }
        contextAsUser.getPackageManager().getPackageInstaller().registerSessionCallback(new SessionCallback(), getHandler());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        intentFilter.addAction("android.intent.action.PACKAGE_FULLY_REMOVED");
        intentFilter.addDataScheme("package");
        contextAsUser.registerReceiver(new BroadcastReceiver() { // from class: com.android.server.rollback.RollbackManagerServiceImpl.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                RollbackManagerServiceImpl.this.assertInWorkerThread();
                String action = intent.getAction();
                if ("android.intent.action.PACKAGE_REPLACED".equals(action)) {
                    String schemeSpecificPart = intent.getData().getSchemeSpecificPart();
                    if (RollbackManagerServiceImpl.LOCAL_LOGV) {
                        Slog.v(RollbackManagerServiceImpl.TAG, "broadcast=ACTION_PACKAGE_REPLACED pkg=" + schemeSpecificPart);
                    }
                    RollbackManagerServiceImpl.this.onPackageReplaced(schemeSpecificPart);
                }
                if ("android.intent.action.PACKAGE_FULLY_REMOVED".equals(action)) {
                    String schemeSpecificPart2 = intent.getData().getSchemeSpecificPart();
                    Slog.i(RollbackManagerServiceImpl.TAG, "broadcast=ACTION_PACKAGE_FULLY_REMOVED pkg=" + schemeSpecificPart2);
                    RollbackManagerServiceImpl.this.onPackageFullyRemoved(schemeSpecificPart2);
                }
            }
        }, intentFilter, null, getHandler());
    }

    public ParceledListSlice getAvailableRollbacks() {
        assertNotInWorkerThread();
        enforceManageRollbacks("getAvailableRollbacks");
        return (ParceledListSlice) awaitResult(new Supplier() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda13
            @Override // java.util.function.Supplier
            public final Object get() {
                ParceledListSlice lambda$getAvailableRollbacks$1;
                lambda$getAvailableRollbacks$1 = RollbackManagerServiceImpl.this.lambda$getAvailableRollbacks$1();
                return lambda$getAvailableRollbacks$1;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ ParceledListSlice lambda$getAvailableRollbacks$1() {
        assertInWorkerThread();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.mRollbacks.size(); i++) {
            Rollback rollback = this.mRollbacks.get(i);
            if (rollback.isAvailable()) {
                arrayList.add(rollback.info);
            }
        }
        return new ParceledListSlice(arrayList);
    }

    public ParceledListSlice<RollbackInfo> getRecentlyCommittedRollbacks() {
        assertNotInWorkerThread();
        enforceManageRollbacks("getRecentlyCommittedRollbacks");
        return (ParceledListSlice) awaitResult(new Supplier() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda9
            @Override // java.util.function.Supplier
            public final Object get() {
                ParceledListSlice lambda$getRecentlyCommittedRollbacks$2;
                lambda$getRecentlyCommittedRollbacks$2 = RollbackManagerServiceImpl.this.lambda$getRecentlyCommittedRollbacks$2();
                return lambda$getRecentlyCommittedRollbacks$2;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ ParceledListSlice lambda$getRecentlyCommittedRollbacks$2() {
        assertInWorkerThread();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.mRollbacks.size(); i++) {
            Rollback rollback = this.mRollbacks.get(i);
            if (rollback.isCommitted()) {
                arrayList.add(rollback.info);
            }
        }
        return new ParceledListSlice(arrayList);
    }

    public void commitRollback(final int i, final ParceledListSlice parceledListSlice, final String str, final IntentSender intentSender) {
        assertNotInWorkerThread();
        enforceManageRollbacks("commitRollback");
        ((AppOpsManager) this.mContext.getSystemService(AppOpsManager.class)).checkPackage(Binder.getCallingUid(), str);
        getHandler().post(new Runnable() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                RollbackManagerServiceImpl.this.lambda$commitRollback$3(i, parceledListSlice, str, intentSender);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$commitRollback$3(int i, ParceledListSlice parceledListSlice, String str, IntentSender intentSender) {
        commitRollbackInternal(i, parceledListSlice.getList(), str, intentSender);
    }

    private void registerTimeChangeReceiver() {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.rollback.RollbackManagerServiceImpl.5
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                RollbackManagerServiceImpl.this.assertInWorkerThread();
                long j = RollbackManagerServiceImpl.this.mRelativeBootTime;
                RollbackManagerServiceImpl.this.mRelativeBootTime = RollbackManagerServiceImpl.m3080$$Nest$smcalculateRelativeBootTime();
                long j2 = RollbackManagerServiceImpl.this.mRelativeBootTime - j;
                for (Rollback rollback : RollbackManagerServiceImpl.this.mRollbacks) {
                    rollback.setTimestamp(rollback.getTimestamp().plusMillis(j2));
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.TIME_SET");
        this.mContext.registerReceiver(broadcastReceiver, intentFilter, null, getHandler());
    }

    private static long calculateRelativeBootTime() {
        return System.currentTimeMillis() - SystemClock.elapsedRealtime();
    }

    private void commitRollbackInternal(int i, List<VersionedPackage> list, String str, IntentSender intentSender) {
        assertInWorkerThread();
        Slog.i(TAG, "commitRollback id=" + i + " caller=" + str);
        Rollback rollbackForId = getRollbackForId(i);
        if (rollbackForId == null) {
            sendFailure(this.mContext, intentSender, 2, "Rollback unavailable");
        } else {
            rollbackForId.commit(this.mContext, list, str, intentSender);
        }
    }

    public void reloadPersistedData() {
        assertNotInWorkerThread();
        this.mContext.enforceCallingOrSelfPermission("android.permission.TEST_MANAGE_ROLLBACKS", "reloadPersistedData");
        awaitResult(new Runnable() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                RollbackManagerServiceImpl.this.lambda$reloadPersistedData$4();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reloadPersistedData$4() {
        assertInWorkerThread();
        this.mRollbacks.clear();
        this.mRollbacks.addAll(this.mRollbackStore.loadRollbacks());
    }

    private void expireRollbackForPackageInternal(String str, String str2) {
        assertInWorkerThread();
        Iterator<Rollback> it = this.mRollbacks.iterator();
        while (it.hasNext()) {
            Rollback next = it.next();
            if (next.includesPackage(str)) {
                it.remove();
                deleteRollback(next, str2);
            }
        }
    }

    public void expireRollbackForPackage(final String str) {
        assertNotInWorkerThread();
        this.mContext.enforceCallingOrSelfPermission("android.permission.TEST_MANAGE_ROLLBACKS", "expireRollbackForPackage");
        awaitResult(new Runnable() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                RollbackManagerServiceImpl.this.lambda$expireRollbackForPackage$5(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$expireRollbackForPackage$5(String str) {
        expireRollbackForPackageInternal(str, "Expired by API");
    }

    public void blockRollbackManager(final long j) {
        assertNotInWorkerThread();
        this.mContext.enforceCallingOrSelfPermission("android.permission.TEST_MANAGE_ROLLBACKS", "blockRollbackManager");
        getHandler().post(new Runnable() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                RollbackManagerServiceImpl.this.lambda$blockRollbackManager$6(j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$blockRollbackManager$6(long j) {
        assertInWorkerThread();
        this.mSleepDuration.addLast(j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void queueSleepIfNeeded() {
        assertInWorkerThread();
        if (this.mSleepDuration.size() == 0) {
            return;
        }
        final long removeFirst = this.mSleepDuration.removeFirst();
        if (removeFirst <= 0) {
            return;
        }
        getHandler().post(new Runnable() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                RollbackManagerServiceImpl.this.lambda$queueSleepIfNeeded$7(removeFirst);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$queueSleepIfNeeded$7(long j) {
        assertInWorkerThread();
        try {
            Thread.sleep(j);
        } catch (InterruptedException unused) {
            throw new IllegalStateException("RollbackManagerHandlerThread interrupted");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onUnlockUser(final int i) {
        assertNotInWorkerThread();
        if (LOCAL_LOGV) {
            Slog.v(TAG, "onUnlockUser id=" + i);
        }
        awaitResult(new Runnable() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                RollbackManagerServiceImpl.this.lambda$onUnlockUser$8(i);
            }
        });
        getHandler().post(new Runnable() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                RollbackManagerServiceImpl.this.lambda$onUnlockUser$9(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUnlockUser$8(int i) {
        assertInWorkerThread();
        ArrayList arrayList = new ArrayList(this.mRollbacks);
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            ((Rollback) arrayList.get(i2)).commitPendingBackupAndRestoreForUser(i, this.mAppDataRollbackHelper);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: destroyCeSnapshotsForExpiredRollbacks, reason: merged with bridge method [inline-methods] */
    public void lambda$onUnlockUser$9(int i) {
        int size = this.mRollbacks.size();
        int[] iArr = new int[size];
        for (int i2 = 0; i2 < size; i2++) {
            iArr[i2] = this.mRollbacks.get(i2).info.getRollbackId();
        }
        ApexManager.getInstance().destroyCeSnapshotsNotSpecified(i, iArr);
        try {
            this.mInstaller.destroyCeSnapshotsNotSpecified(i, iArr);
        } catch (Installer.InstallerException e) {
            Slog.e(TAG, "Failed to delete snapshots for user: " + i, e);
        }
    }

    private void updateRollbackLifetimeDurationInMillis() {
        assertInWorkerThread();
        long j = DEFAULT_ROLLBACK_LIFETIME_DURATION_MILLIS;
        long j2 = DeviceConfig.getLong("rollback_boot", "rollback_lifetime_in_millis", j);
        this.mRollbackLifetimeDurationInMillis = j2;
        if (j2 < 0) {
            this.mRollbackLifetimeDurationInMillis = j;
        }
        Slog.d(TAG, "mRollbackLifetimeDurationInMillis=" + this.mRollbackLifetimeDurationInMillis);
        runExpiration();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onBootCompleted() {
        DeviceConfig.addOnPropertiesChangedListener("rollback_boot", this.mExecutor, new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda2
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                RollbackManagerServiceImpl.this.lambda$onBootCompleted$10(properties);
            }
        });
        getHandler().post(new Runnable() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                RollbackManagerServiceImpl.this.lambda$onBootCompleted$11();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBootCompleted$10(DeviceConfig.Properties properties) {
        updateRollbackLifetimeDurationInMillis();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBootCompleted$11() {
        assertInWorkerThread();
        updateRollbackLifetimeDurationInMillis();
        runExpiration();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        HashSet hashSet = new HashSet();
        Iterator<Rollback> it = this.mRollbacks.iterator();
        while (it.hasNext()) {
            Rollback next = it.next();
            if (next.isStaged()) {
                PackageInstaller.SessionInfo sessionInfo = this.mContext.getPackageManager().getPackageInstaller().getSessionInfo(next.getOriginalSessionId());
                if (sessionInfo == null || sessionInfo.isStagedSessionFailed()) {
                    if (next.isEnabling()) {
                        it.remove();
                        deleteRollback(next, "Session " + next.getOriginalSessionId() + " not existed or failed");
                    }
                } else {
                    if (sessionInfo.isStagedSessionApplied()) {
                        if (next.isEnabling()) {
                            arrayList.add(next);
                        } else if (next.isRestoreUserDataInProgress()) {
                            arrayList2.add(next);
                        }
                    }
                    hashSet.addAll(next.getApexPackageNames());
                }
            }
        }
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            makeRollbackAvailable((Rollback) it2.next());
        }
        Iterator it3 = arrayList2.iterator();
        while (it3.hasNext()) {
            ((Rollback) it3.next()).setRestoreUserDataInProgress(false);
        }
        Iterator it4 = hashSet.iterator();
        while (it4.hasNext()) {
            onPackageReplaced((String) it4.next());
        }
        this.mPackageHealthObserver.onBootCompletedAsync();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPackageReplaced(String str) {
        assertInWorkerThread();
        long installedPackageVersion = getInstalledPackageVersion(str);
        Iterator<Rollback> it = this.mRollbacks.iterator();
        while (it.hasNext()) {
            Rollback next = it.next();
            if (next.isAvailable() && next.includesPackageWithDifferentVersion(str, installedPackageVersion)) {
                it.remove();
                deleteRollback(next, "Package " + str + " replaced");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPackageFullyRemoved(String str) {
        assertInWorkerThread();
        expireRollbackForPackageInternal(str, "Package " + str + " removed");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void sendFailure(Context context, IntentSender intentSender, int i, String str) {
        Slog.e(TAG, str);
        try {
            Intent intent = new Intent();
            intent.putExtra("android.content.rollback.extra.STATUS", i);
            intent.putExtra("android.content.rollback.extra.STATUS_MESSAGE", str);
            intentSender.sendIntent(context, 0, intent, null, null);
        } catch (IntentSender.SendIntentException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runExpiration() {
        getHandler().removeCallbacks(this.mRunExpiration);
        assertInWorkerThread();
        Instant now = Instant.now();
        Iterator<Rollback> it = this.mRollbacks.iterator();
        Instant instant = null;
        while (it.hasNext()) {
            Rollback next = it.next();
            if (next.isAvailable() || next.isCommitted()) {
                Instant timestamp = next.getTimestamp();
                if (!now.isBefore(timestamp.plusMillis(this.mRollbackLifetimeDurationInMillis))) {
                    Slog.i(TAG, "runExpiration id=" + next.info.getRollbackId());
                    it.remove();
                    deleteRollback(next, "Expired by timeout");
                } else if (instant == null || instant.isAfter(timestamp)) {
                    instant = timestamp;
                }
            }
        }
        if (instant != null) {
            getHandler().postDelayed(this.mRunExpiration, now.until(instant.plusMillis(this.mRollbackLifetimeDurationInMillis), ChronoUnit.MILLIS));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Handler getHandler() {
        return this.mHandler;
    }

    private Context getContextAsUser(UserHandle userHandle) {
        try {
            Context context = this.mContext;
            return context.createPackageContextAsUser(context.getPackageName(), 0, userHandle);
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean enableRollback(int i) {
        assertInWorkerThread();
        if (LOCAL_LOGV) {
            Slog.v(TAG, "enableRollback sessionId=" + i);
        }
        PackageInstaller packageInstaller = this.mContext.getPackageManager().getPackageInstaller();
        PackageInstaller.SessionInfo sessionInfo = packageInstaller.getSessionInfo(i);
        if (sessionInfo == null) {
            Slog.e(TAG, "Unable to find session for enabled rollback.");
            return false;
        }
        PackageInstaller.SessionInfo sessionInfo2 = sessionInfo.hasParentSessionId() ? packageInstaller.getSessionInfo(sessionInfo.getParentSessionId()) : sessionInfo;
        if (sessionInfo2 == null) {
            Slog.e(TAG, "Unable to find parent session for enabled rollback.");
            return false;
        }
        Rollback rollbackForSession = getRollbackForSession(sessionInfo.getSessionId());
        if (rollbackForSession == null) {
            rollbackForSession = createNewRollback(sessionInfo2);
        }
        if (!enableRollbackForPackageSession(rollbackForSession, sessionInfo)) {
            return false;
        }
        if (rollbackForSession.allPackagesEnabled()) {
            return completeEnableRollback(rollbackForSession);
        }
        return true;
    }

    private int computeRollbackDataPolicy(int i, int i2) {
        assertInWorkerThread();
        return i2 != 0 ? i2 : i;
    }

    private boolean enableRollbackForPackageSession(Rollback rollback, PackageInstaller.SessionInfo sessionInfo) {
        assertInWorkerThread();
        int i = sessionInfo.installFlags;
        if ((262144 & i) == 0) {
            Slog.e(TAG, "Rollback is not enabled.");
            return false;
        }
        if ((i & 2048) != 0) {
            Slog.e(TAG, "Rollbacks not supported for instant app install");
            return false;
        }
        if (sessionInfo.resolvedBaseCodePath == null) {
            Slog.e(TAG, "Session code path has not been resolved.");
            return false;
        }
        ParseResult parseApkLite = ApkLiteParseUtils.parseApkLite(ParseTypeImpl.forDefaultParsing().reset(), new File(sessionInfo.resolvedBaseCodePath), 0);
        if (parseApkLite.isError()) {
            Slog.e(TAG, "Unable to parse new package: " + parseApkLite.getErrorMessage(), parseApkLite.getException());
            return false;
        }
        ApkLite apkLite = (ApkLite) parseApkLite.getResult();
        String packageName = apkLite.getPackageName();
        int computeRollbackDataPolicy = computeRollbackDataPolicy(sessionInfo.rollbackDataPolicy, apkLite.getRollbackDataPolicy());
        if (!sessionInfo.isStaged() && (i & 131072) != 0 && computeRollbackDataPolicy != 2) {
            Slog.e(TAG, "Only RETAIN is supported for rebootless APEX: " + packageName);
            return false;
        }
        Slog.i(TAG, "Enabling rollback for install of " + packageName + ", session:" + sessionInfo.sessionId + ", rollbackDataPolicy=" + computeRollbackDataPolicy);
        String installerPackageName = sessionInfo.getInstallerPackageName();
        if (!enableRollbackAllowed(installerPackageName, packageName)) {
            Slog.e(TAG, "Installer " + installerPackageName + " is not allowed to enable rollback on " + packageName);
            return false;
        }
        boolean z = (i & 131072) != 0;
        try {
            PackageInfo packageInfo = getPackageInfo(packageName);
            if (z) {
                for (String str : ((PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class)).getApksInApex(packageName)) {
                    try {
                        if (!rollback.enableForPackageInApex(str, getPackageInfo(str).getLongVersionCode(), computeRollbackDataPolicy)) {
                            return false;
                        }
                    } catch (PackageManager.NameNotFoundException unused) {
                        Slog.e(TAG, str + " is not installed");
                        return false;
                    }
                }
            }
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            return rollback.enableForPackage(packageName, apkLite.getVersionCode(), packageInfo.getLongVersionCode(), z, applicationInfo.sourceDir, applicationInfo.splitSourceDirs, computeRollbackDataPolicy);
        } catch (PackageManager.NameNotFoundException unused2) {
            Slog.e(TAG, packageName + " is not installed");
            return false;
        }
    }

    @Override // com.android.server.rollback.RollbackManagerInternal
    public void snapshotAndRestoreUserData(String str, List<UserHandle> list, int i, long j, String str2, int i2) {
        assertNotInWorkerThread();
        snapshotAndRestoreUserData(str, UserHandle.fromUserHandles(list), i, j, str2, i2);
    }

    public void snapshotAndRestoreUserData(final String str, final int[] iArr, final int i, long j, final String str2, final int i2) {
        assertNotInWorkerThread();
        if (Binder.getCallingUid() != 1000) {
            throw new SecurityException("snapshotAndRestoreUserData may only be called by the system.");
        }
        getHandler().post(new Runnable() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                RollbackManagerServiceImpl.this.lambda$snapshotAndRestoreUserData$12(str, iArr, i, str2, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$snapshotAndRestoreUserData$12(String str, int[] iArr, int i, String str2, int i2) {
        assertInWorkerThread();
        snapshotUserDataInternal(str, iArr);
        restoreUserDataInternal(str, iArr, i, str2);
        if (i2 > 0) {
            ((PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class)).finishPackageInstall(i2, false);
        }
    }

    private void snapshotUserDataInternal(String str, int[] iArr) {
        assertInWorkerThread();
        if (LOCAL_LOGV) {
            Slog.v(TAG, "snapshotUserData pkg=" + str + " users=" + Arrays.toString(iArr));
        }
        for (int i = 0; i < this.mRollbacks.size(); i++) {
            this.mRollbacks.get(i).snapshotUserData(str, iArr, this.mAppDataRollbackHelper);
        }
    }

    private void restoreUserDataInternal(String str, int[] iArr, int i, String str2) {
        assertInWorkerThread();
        if (LOCAL_LOGV) {
            Slog.v(TAG, "restoreUserData pkg=" + str + " users=" + Arrays.toString(iArr));
        }
        for (int i2 = 0; i2 < this.mRollbacks.size() && !this.mRollbacks.get(i2).restoreUserDataForPackageIfInProgress(str, iArr, i, str2, this.mAppDataRollbackHelper); i2++) {
        }
    }

    @Override // com.android.server.rollback.RollbackManagerInternal
    public int notifyStagedSession(final int i) {
        assertNotInWorkerThread();
        if (Binder.getCallingUid() != 1000) {
            throw new SecurityException("notifyStagedSession may only be called by the system.");
        }
        return ((Integer) awaitResult(new Supplier() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda10
            @Override // java.util.function.Supplier
            public final Object get() {
                Integer lambda$notifyStagedSession$13;
                lambda$notifyStagedSession$13 = RollbackManagerServiceImpl.this.lambda$notifyStagedSession$13(i);
                return lambda$notifyStagedSession$13;
            }
        })).intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Integer lambda$notifyStagedSession$13(int i) {
        assertInWorkerThread();
        Rollback rollbackForSession = getRollbackForSession(i);
        return Integer.valueOf(rollbackForSession != null ? rollbackForSession.info.getRollbackId() : -1);
    }

    private boolean enableRollbackAllowed(String str, String str2) {
        if (str == null) {
            return false;
        }
        PackageManager packageManager = this.mContext.getPackageManager();
        return (isRollbackAllowed(str2) && (packageManager.checkPermission("android.permission.MANAGE_ROLLBACKS", str) == 0)) || (packageManager.checkPermission("android.permission.TEST_MANAGE_ROLLBACKS", str) == 0);
    }

    private boolean isRollbackAllowed(String str) {
        return SystemConfig.getInstance().getRollbackWhitelistedPackages().contains(str) || isModule(str);
    }

    private boolean isModule(String str) {
        try {
            return this.mContext.getPackageManager().getModuleInfo(str, 0) != null;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    private long getInstalledPackageVersion(String str) {
        try {
            return getPackageInfo(str).getLongVersionCode();
        } catch (PackageManager.NameNotFoundException unused) {
            return -1L;
        }
    }

    private PackageInfo getPackageInfo(String str) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = this.mContext.getPackageManager();
        try {
            return packageManager.getPackageInfo(str, DumpState.DUMP_CHANGES);
        } catch (PackageManager.NameNotFoundException unused) {
            return packageManager.getPackageInfo(str, 1073741824);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class SessionCallback extends PackageInstaller.SessionCallback {
        @Override // android.content.pm.PackageInstaller.SessionCallback
        public void onActiveChanged(int i, boolean z) {
        }

        @Override // android.content.pm.PackageInstaller.SessionCallback
        public void onBadgingChanged(int i) {
        }

        @Override // android.content.pm.PackageInstaller.SessionCallback
        public void onCreated(int i) {
        }

        @Override // android.content.pm.PackageInstaller.SessionCallback
        public void onProgressChanged(int i, float f) {
        }

        private SessionCallback() {
        }

        @Override // android.content.pm.PackageInstaller.SessionCallback
        public void onFinished(int i, boolean z) {
            RollbackManagerServiceImpl.this.assertInWorkerThread();
            if (RollbackManagerServiceImpl.LOCAL_LOGV) {
                Slog.v(RollbackManagerServiceImpl.TAG, "SessionCallback.onFinished id=" + i + " success=" + z);
            }
            Rollback rollbackForSession = RollbackManagerServiceImpl.this.getRollbackForSession(i);
            if (rollbackForSession != null && rollbackForSession.isEnabling() && i == rollbackForSession.getOriginalSessionId()) {
                if (z) {
                    if (rollbackForSession.isStaged() || !RollbackManagerServiceImpl.this.completeEnableRollback(rollbackForSession)) {
                        return;
                    }
                    RollbackManagerServiceImpl.this.makeRollbackAvailable(rollbackForSession);
                    return;
                }
                Slog.w(RollbackManagerServiceImpl.TAG, "Delete rollback id=" + rollbackForSession.info.getRollbackId() + " for failed session id=" + i);
                RollbackManagerServiceImpl.this.mRollbacks.remove(rollbackForSession);
                RollbackManagerServiceImpl.this.deleteRollback(rollbackForSession, "Session " + i + " failed");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean completeEnableRollback(Rollback rollback) {
        assertInWorkerThread();
        if (LOCAL_LOGV) {
            Slog.v(TAG, "completeEnableRollback id=" + rollback.info.getRollbackId());
        }
        if (!rollback.allPackagesEnabled()) {
            Slog.e(TAG, "Failed to enable rollback for all packages in session.");
            this.mRollbacks.remove(rollback);
            deleteRollback(rollback, "Failed to enable rollback for all packages in session");
            return false;
        }
        rollback.saveRollback();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"rollback.getLock"})
    public void makeRollbackAvailable(Rollback rollback) {
        assertInWorkerThread();
        Slog.i(TAG, "makeRollbackAvailable id=" + rollback.info.getRollbackId());
        rollback.makeAvailable();
        this.mPackageHealthObserver.notifyRollbackAvailable(rollback.info);
        this.mPackageHealthObserver.startObservingHealth(rollback.getPackageNames(), this.mRollbackLifetimeDurationInMillis);
        runExpiration();
    }

    private Rollback getRollbackForId(int i) {
        assertInWorkerThread();
        for (int i2 = 0; i2 < this.mRollbacks.size(); i2++) {
            Rollback rollback = this.mRollbacks.get(i2);
            if (rollback.info.getRollbackId() == i) {
                return rollback;
            }
        }
        return null;
    }

    private int allocateRollbackId() {
        assertInWorkerThread();
        int i = 0;
        while (true) {
            int nextInt = this.mRandom.nextInt(2147483646) + 1;
            if (!this.mAllocatedRollbackIds.get(nextInt, false)) {
                this.mAllocatedRollbackIds.put(nextInt, true);
                return nextInt;
            }
            int i2 = i + 1;
            if (i >= 32) {
                throw new IllegalStateException("Failed to allocate rollback ID");
            }
            i = i2;
        }
    }

    protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        assertNotInWorkerThread();
        if (DumpUtils.checkDumpPermission(this.mContext, TAG, printWriter)) {
            final IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
            awaitResult(new Runnable() { // from class: com.android.server.rollback.RollbackManagerServiceImpl$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    RollbackManagerServiceImpl.this.lambda$dump$14(indentingPrintWriter);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dump$14(IndentingPrintWriter indentingPrintWriter) {
        assertInWorkerThread();
        Iterator<Rollback> it = this.mRollbacks.iterator();
        while (it.hasNext()) {
            it.next().dump(indentingPrintWriter);
        }
        indentingPrintWriter.println();
        List<Rollback> loadHistorialRollbacks = this.mRollbackStore.loadHistorialRollbacks();
        if (!loadHistorialRollbacks.isEmpty()) {
            indentingPrintWriter.println("Historical rollbacks:");
            indentingPrintWriter.increaseIndent();
            Iterator<Rollback> it2 = loadHistorialRollbacks.iterator();
            while (it2.hasNext()) {
                it2.next().dump(indentingPrintWriter);
            }
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println();
        }
        PackageWatchdog.getInstance(this.mContext).dump(indentingPrintWriter);
    }

    private void enforceManageRollbacks(String str) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_ROLLBACKS") == 0 || this.mContext.checkCallingOrSelfPermission("android.permission.TEST_MANAGE_ROLLBACKS") == 0) {
            return;
        }
        throw new SecurityException(str + " requires android.permission.MANAGE_ROLLBACKS or android.permission.TEST_MANAGE_ROLLBACKS");
    }

    private Rollback createNewRollback(PackageInstaller.SessionInfo sessionInfo) {
        int identifier;
        int[] iArr;
        Rollback createNonStagedRollback;
        assertInWorkerThread();
        int allocateRollbackId = allocateRollbackId();
        if (sessionInfo.getUser().equals(UserHandle.ALL)) {
            identifier = UserHandle.SYSTEM.getIdentifier();
        } else {
            identifier = sessionInfo.getUser().getIdentifier();
        }
        int i = identifier;
        String installerPackageName = sessionInfo.getInstallerPackageName();
        int sessionId = sessionInfo.getSessionId();
        if (LOCAL_LOGV) {
            Slog.v(TAG, "createNewRollback id=" + allocateRollbackId + " user=" + i + " installer=" + installerPackageName);
        }
        if (sessionInfo.isMultiPackage()) {
            iArr = sessionInfo.getChildSessionIds();
        } else {
            iArr = new int[]{sessionId};
        }
        int[] iArr2 = iArr;
        if (sessionInfo.isStaged()) {
            createNonStagedRollback = this.mRollbackStore.createStagedRollback(allocateRollbackId, sessionId, i, installerPackageName, iArr2, getExtensionVersions());
        } else {
            createNonStagedRollback = this.mRollbackStore.createNonStagedRollback(allocateRollbackId, sessionId, i, installerPackageName, iArr2, getExtensionVersions());
        }
        this.mRollbacks.add(createNonStagedRollback);
        return createNonStagedRollback;
    }

    private SparseIntArray getExtensionVersions() {
        Map<Integer, Integer> allExtensionVersions = SdkExtensions.getAllExtensionVersions();
        SparseIntArray sparseIntArray = new SparseIntArray(allExtensionVersions.size());
        Iterator<Integer> it = allExtensionVersions.keySet().iterator();
        while (it.hasNext()) {
            int intValue = it.next().intValue();
            sparseIntArray.put(intValue, allExtensionVersions.get(Integer.valueOf(intValue)).intValue());
        }
        return sparseIntArray;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Rollback getRollbackForSession(int i) {
        assertInWorkerThread();
        for (int i2 = 0; i2 < this.mRollbacks.size(); i2++) {
            Rollback rollback = this.mRollbacks.get(i2);
            if (rollback.getOriginalSessionId() == i || rollback.containsSessionId(i)) {
                return rollback;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteRollback(Rollback rollback, String str) {
        assertInWorkerThread();
        rollback.delete(this.mAppDataRollbackHelper, str);
        this.mRollbackStore.saveRollbackToHistory(rollback);
    }
}
