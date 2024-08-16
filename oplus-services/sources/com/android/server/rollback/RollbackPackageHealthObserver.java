package com.android.server.rollback;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.VersionedPackage;
import android.content.rollback.PackageRollbackInfo;
import android.content.rollback.RollbackInfo;
import android.content.rollback.RollbackManager;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.util.ArraySet;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import com.android.server.PackageWatchdog;
import com.android.server.SystemConfig;
import com.android.server.pm.ApexManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class RollbackPackageHealthObserver implements PackageWatchdog.PackageHealthObserver {
    private static final String NAME = "rollback-observer";
    private static final int PERSISTENT_MASK = 9;
    private static final String PROP_ATTEMPTING_REBOOT = "sys.attempting_reboot";
    private static final String TAG = "RollbackPackageHealthObserver";
    private final ApexManager mApexManager;
    private final Context mContext;
    private final Handler mHandler;
    private final File mLastStagedRollbackIdsFile;
    private final Set<Integer> mPendingStagedRollbackIds = new ArraySet();
    private boolean mTwoPhaseRollbackEnabled;
    private final File mTwoPhaseRollbackEnabledFile;

    public String getName() {
        return NAME;
    }

    public boolean isPersistent() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RollbackPackageHealthObserver(Context context) {
        this.mContext = context;
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        this.mHandler = new Handler(handlerThread.getLooper());
        File file = new File(Environment.getDataDirectory(), NAME);
        file.mkdirs();
        this.mLastStagedRollbackIdsFile = new File(file, "last-staged-rollback-ids");
        File file2 = new File(file, "two-phase-rollback-enabled");
        this.mTwoPhaseRollbackEnabledFile = file2;
        PackageWatchdog.getInstance(context).registerHealthObserver(this);
        this.mApexManager = ApexManager.getInstance();
        if (SystemProperties.getBoolean("sys.boot_completed", false)) {
            this.mTwoPhaseRollbackEnabled = readBoolean(file2);
        } else {
            this.mTwoPhaseRollbackEnabled = false;
            writeBoolean(file2, false);
        }
    }

    public int onHealthCheckFailed(VersionedPackage versionedPackage, int i, int i2) {
        boolean z = !((RollbackManager) this.mContext.getSystemService(RollbackManager.class)).getAvailableRollbacks().isEmpty();
        if (!(i == 1 && z) && getAvailableRollback(versionedPackage) == null) {
            return z ? 70 : 0;
        }
        return 30;
    }

    public boolean execute(final VersionedPackage versionedPackage, final int i, int i2) {
        if (i == 1) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.rollback.RollbackPackageHealthObserver$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    RollbackPackageHealthObserver.this.lambda$execute$0(i);
                }
            });
            return true;
        }
        final RollbackInfo availableRollback = getAvailableRollback(versionedPackage);
        if (availableRollback != null) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.rollback.RollbackPackageHealthObserver$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    RollbackPackageHealthObserver.this.lambda$execute$1(availableRollback, versionedPackage, i);
                }
            });
        } else {
            this.mHandler.post(new Runnable() { // from class: com.android.server.rollback.RollbackPackageHealthObserver$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    RollbackPackageHealthObserver.this.lambda$execute$2(i);
                }
            });
        }
        return true;
    }

    public boolean mayObservePackage(String str) {
        if (((RollbackManager) this.mContext.getSystemService(RollbackManager.class)).getAvailableRollbacks().isEmpty()) {
            return false;
        }
        return isPersistentSystemApp(str);
    }

    private boolean isPersistentSystemApp(String str) {
        try {
            return (this.mContext.getPackageManager().getApplicationInfo(str, 0).flags & 9) == 9;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    private void assertInWorkerThread() {
        Preconditions.checkState(this.mHandler.getLooper().isCurrentThread());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startObservingHealth(List<String> list, long j) {
        PackageWatchdog.getInstance(this.mContext).startObservingHealth(this, list, j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyRollbackAvailable(final RollbackInfo rollbackInfo) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.rollback.RollbackPackageHealthObserver$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                RollbackPackageHealthObserver.this.lambda$notifyRollbackAvailable$3(rollbackInfo);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyRollbackAvailable$3(RollbackInfo rollbackInfo) {
        if (isRebootlessApex(rollbackInfo)) {
            this.mTwoPhaseRollbackEnabled = true;
            writeBoolean(this.mTwoPhaseRollbackEnabledFile, true);
        }
    }

    private static boolean isRebootlessApex(RollbackInfo rollbackInfo) {
        if (rollbackInfo.isStaged()) {
            return false;
        }
        Iterator it = rollbackInfo.getPackages().iterator();
        while (it.hasNext()) {
            if (((PackageRollbackInfo) it.next()).isApex()) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onBootCompletedAsync() {
        this.mHandler.post(new Runnable() { // from class: com.android.server.rollback.RollbackPackageHealthObserver$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                RollbackPackageHealthObserver.this.lambda$onBootCompletedAsync$4();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onBootCompleted, reason: merged with bridge method [inline-methods] */
    public void lambda$onBootCompletedAsync$4() {
        assertInWorkerThread();
        RollbackManager rollbackManager = (RollbackManager) this.mContext.getSystemService(RollbackManager.class);
        if (!rollbackManager.getAvailableRollbacks().isEmpty()) {
            PackageWatchdog.getInstance(this.mContext).scheduleCheckAndMitigateNativeCrashes();
        }
        SparseArray<String> popLastStagedRollbackIds = popLastStagedRollbackIds();
        for (int i = 0; i < popLastStagedRollbackIds.size(); i++) {
            WatchdogRollbackLogger.logRollbackStatusOnBoot(this.mContext, popLastStagedRollbackIds.keyAt(i), popLastStagedRollbackIds.valueAt(i), rollbackManager.getRecentlyCommittedRollbacks());
        }
    }

    private RollbackInfo getAvailableRollback(VersionedPackage versionedPackage) {
        for (RollbackInfo rollbackInfo : ((RollbackManager) this.mContext.getSystemService(RollbackManager.class)).getAvailableRollbacks()) {
            for (PackageRollbackInfo packageRollbackInfo : rollbackInfo.getPackages()) {
                if (packageRollbackInfo.getVersionRolledBackFrom().equals(versionedPackage)) {
                    return rollbackInfo;
                }
                if (packageRollbackInfo.isApkInApex() && packageRollbackInfo.getVersionRolledBackFrom().getPackageName().equals(versionedPackage.getPackageName())) {
                    return rollbackInfo;
                }
            }
        }
        return null;
    }

    private boolean markStagedSessionHandled(int i) {
        assertInWorkerThread();
        return this.mPendingStagedRollbackIds.remove(Integer.valueOf(i));
    }

    private boolean isPendingStagedSessionsEmpty() {
        assertInWorkerThread();
        return this.mPendingStagedRollbackIds.isEmpty();
    }

    private static boolean readBoolean(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                boolean z = fileInputStream.read() == 1;
                fileInputStream.close();
                return z;
            } finally {
            }
        } catch (IOException unused) {
            return false;
        }
    }

    private static void writeBoolean(File file, boolean z) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            try {
                fileOutputStream.write(z ? 1 : 0);
                fileOutputStream.flush();
                FileUtils.sync(fileOutputStream);
                fileOutputStream.close();
            } finally {
            }
        } catch (IOException unused) {
        }
    }

    private void saveStagedRollbackId(int i, VersionedPackage versionedPackage) {
        assertInWorkerThread();
        writeStagedRollbackId(this.mLastStagedRollbackIdsFile, i, versionedPackage);
    }

    static void writeStagedRollbackId(File file, int i, VersionedPackage versionedPackage) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            PrintWriter printWriter = new PrintWriter(fileOutputStream);
            printWriter.append((CharSequence) String.valueOf(i)).append((CharSequence) ",").append((CharSequence) (versionedPackage != null ? versionedPackage.getPackageName() : ""));
            printWriter.println();
            printWriter.flush();
            FileUtils.sync(fileOutputStream);
            printWriter.close();
        } catch (IOException e) {
            Slog.e(TAG, "Failed to save last staged rollback id", e);
            file.delete();
        }
    }

    private SparseArray<String> popLastStagedRollbackIds() {
        assertInWorkerThread();
        try {
            return readStagedRollbackIds(this.mLastStagedRollbackIdsFile);
        } finally {
            this.mLastStagedRollbackIdsFile.delete();
        }
    }

    static SparseArray<String> readStagedRollbackIds(File file) {
        SparseArray<String> sparseArray = new SparseArray<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    return sparseArray;
                }
                String[] split = readLine.trim().split(",");
                String str = split[0];
                String str2 = "";
                if (split.length > 1) {
                    str2 = split[1];
                }
                sparseArray.put(Integer.parseInt(str), str2);
            }
        } catch (Exception unused) {
            return new SparseArray<>();
        }
    }

    private boolean isModule(String str) {
        String activeApexPackageNameContainingPackage = this.mApexManager.getActiveApexPackageNameContainingPackage(str);
        if (activeApexPackageNameContainingPackage != null) {
            str = activeApexPackageNameContainingPackage;
        }
        try {
            return this.mContext.getPackageManager().getModuleInfo(str, 0) != null;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: rollbackPackage, reason: merged with bridge method [inline-methods] */
    public void lambda$execute$1(final RollbackInfo rollbackInfo, VersionedPackage versionedPackage, int i) {
        String packageName;
        assertInWorkerThread();
        if (isAutomaticRollbackDenied(SystemConfig.getInstance(), versionedPackage)) {
            Slog.d(TAG, "Automatic rollback not allowed for package " + versionedPackage.getPackageName());
            return;
        }
        RollbackManager rollbackManager = (RollbackManager) this.mContext.getSystemService(RollbackManager.class);
        final int mapFailureReasonToMetric = WatchdogRollbackLogger.mapFailureReasonToMetric(i);
        if (i == 1) {
            packageName = SystemProperties.get("sys.init.updatable_crashing_process_name", "");
        } else {
            packageName = versionedPackage.getPackageName();
        }
        final String str = packageName;
        final VersionedPackage logPackage = isModule(versionedPackage.getPackageName()) ? WatchdogRollbackLogger.getLogPackage(this.mContext, versionedPackage) : null;
        WatchdogRollbackLogger.logEvent(logPackage, 1, mapFailureReasonToMetric, str);
        final Consumer consumer = new Consumer() { // from class: com.android.server.rollback.RollbackPackageHealthObserver$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                RollbackPackageHealthObserver.this.lambda$rollbackPackage$5(rollbackInfo, logPackage, mapFailureReasonToMetric, str, (Intent) obj);
            }
        };
        rollbackManager.commitRollback(rollbackInfo.getRollbackId(), Collections.singletonList(versionedPackage), new LocalIntentReceiver(new Consumer() { // from class: com.android.server.rollback.RollbackPackageHealthObserver$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                RollbackPackageHealthObserver.this.lambda$rollbackPackage$7(consumer, (Intent) obj);
            }
        }).getIntentSender());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$rollbackPackage$5(RollbackInfo rollbackInfo, VersionedPackage versionedPackage, int i, String str, Intent intent) {
        assertInWorkerThread();
        if (intent.getIntExtra("android.content.rollback.extra.STATUS", 1) == 0) {
            if (rollbackInfo.isStaged()) {
                saveStagedRollbackId(rollbackInfo.getRollbackId(), versionedPackage);
                WatchdogRollbackLogger.logEvent(versionedPackage, 4, i, str);
            } else {
                WatchdogRollbackLogger.logEvent(versionedPackage, 2, i, str);
            }
        } else {
            WatchdogRollbackLogger.logEvent(versionedPackage, 3, i, str);
        }
        if (rollbackInfo.isStaged()) {
            markStagedSessionHandled(rollbackInfo.getRollbackId());
            if (isPendingStagedSessionsEmpty()) {
                SystemProperties.set(PROP_ATTEMPTING_REBOOT, "true");
                ((PowerManager) this.mContext.getSystemService(PowerManager.class)).reboot("Rollback staged install");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$rollbackPackage$7(final Consumer consumer, final Intent intent) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.rollback.RollbackPackageHealthObserver$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                consumer.accept(intent);
            }
        });
    }

    @VisibleForTesting
    public static boolean isAutomaticRollbackDenied(SystemConfig systemConfig, VersionedPackage versionedPackage) {
        return systemConfig.getAutomaticRollbackDenylistedPackages().contains(versionedPackage.getPackageName());
    }

    private boolean useTwoPhaseRollback(List<RollbackInfo> list) {
        assertInWorkerThread();
        if (!this.mTwoPhaseRollbackEnabled) {
            return false;
        }
        Slog.i(TAG, "Rolling back all rebootless APEX rollbacks");
        boolean z = false;
        for (RollbackInfo rollbackInfo : list) {
            if (isRebootlessApex(rollbackInfo)) {
                lambda$execute$1(rollbackInfo, ((PackageRollbackInfo) rollbackInfo.getPackages().get(0)).getVersionRolledBackFrom(), 1);
                z = true;
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: rollbackAll, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void lambda$execute$2(int i) {
        assertInWorkerThread();
        List<RollbackInfo> availableRollbacks = ((RollbackManager) this.mContext.getSystemService(RollbackManager.class)).getAvailableRollbacks();
        if (useTwoPhaseRollback(availableRollbacks)) {
            return;
        }
        Slog.i(TAG, "Rolling back all available rollbacks");
        for (RollbackInfo rollbackInfo : availableRollbacks) {
            if (rollbackInfo.isStaged()) {
                this.mPendingStagedRollbackIds.add(Integer.valueOf(rollbackInfo.getRollbackId()));
            }
        }
        for (RollbackInfo rollbackInfo2 : availableRollbacks) {
            lambda$execute$1(rollbackInfo2, ((PackageRollbackInfo) rollbackInfo2.getPackages().get(0)).getVersionRolledBackFrom(), i);
        }
    }
}
