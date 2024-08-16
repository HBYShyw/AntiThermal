package com.android.server.pm;

import android.content.Context;
import android.content.pm.PackageStats;
import android.os.Build;
import android.os.CreateAppDataArgs;
import android.os.CreateAppDataResult;
import android.os.IBinder;
import android.os.IInstalld;
import android.os.PersistableBundle;
import android.os.ReconcileSdkDataArgs;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.storage.CrateMetadata;
import android.util.Slog;
import com.android.internal.os.BackgroundThread;
import com.android.server.SystemService;
import dalvik.system.BlockGuard;
import dalvik.system.VMRuntime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class Installer extends SystemService {
    private static final long CONNECT_RETRY_DELAY_MS = 1000;
    private static final long CONNECT_WAIT_MS = 10000;
    public static final int DEXOPT_BOOTCOMPLETE = 8;
    public static final int DEXOPT_DEBUGGABLE = 4;
    public static final int DEXOPT_ENABLE_HIDDEN_API_CHECKS = 1024;
    public static final int DEXOPT_FORCE = 64;
    public static final int DEXOPT_FOR_RESTORE = 8192;
    public static final int DEXOPT_GENERATE_APP_IMAGE = 4096;
    public static final int DEXOPT_GENERATE_COMPACT_DEX = 2048;
    public static final int DEXOPT_IDLE_BACKGROUND_JOB = 512;
    public static final int DEXOPT_PROFILE_GUIDED = 16;
    public static final int DEXOPT_PUBLIC = 2;
    public static final int DEXOPT_SECONDARY_DEX = 32;
    public static final int DEXOPT_STORAGE_CE = 128;
    public static final int DEXOPT_STORAGE_DE = 256;
    public static final int FLAG_CLEAR_APP_DATA_KEEP_ART_PROFILES = 131072;
    public static final int FLAG_CLEAR_CACHE_ONLY = 16;
    public static final int FLAG_CLEAR_CODE_CACHE_ONLY = 32;
    public static final int FLAG_FORCE = 8192;
    public static final int FLAG_FREE_CACHE_DEFY_TARGET_FREE_BYTES = 2048;
    public static final int FLAG_FREE_CACHE_NOOP = 1024;
    public static final int FLAG_FREE_CACHE_V2 = 256;
    public static final int FLAG_FREE_CACHE_V2_DEFY_QUOTA = 512;
    public static final int FLAG_STORAGE_CE = 2;
    public static final int FLAG_STORAGE_DE = 1;
    public static final int FLAG_STORAGE_EXTERNAL = 4;
    public static final int FLAG_STORAGE_SDK = 8;
    public static final int FLAG_USE_QUOTA = 4096;
    public static final int ODEX_IS_PRIVATE = 2;
    public static final int ODEX_IS_PUBLIC = 1;
    public static final int ODEX_NOT_FOUND = 0;
    public static final int PROFILE_ANALYSIS_DONT_OPTIMIZE_EMPTY_PROFILES = 3;
    public static final int PROFILE_ANALYSIS_DONT_OPTIMIZE_SMALL_DELTA = 2;
    public static final int PROFILE_ANALYSIS_OPTIMIZE = 1;
    private static final String TAG = "Installer";
    private volatile boolean mDeferSetFirstBoot;
    private volatile IInstalld mInstalld;
    private volatile CountDownLatch mInstalldLatch;
    public IInstallerExt mInstallerExt;
    private final boolean mIsolated;
    private volatile Object mWarnIfHeld;

    public Installer(Context context) {
        this(context, false);
    }

    public Installer(Context context, boolean z) {
        super(context);
        this.mInstalld = null;
        this.mInstalldLatch = new CountDownLatch(1);
        this.mInstallerExt = (IInstallerExt) ExtLoader.type(IInstallerExt.class).base(this).create();
        this.mIsolated = z;
    }

    public void setWarnIfHeld(Object obj) {
        this.mWarnIfHeld = obj;
    }

    public boolean isIsolated() {
        return this.mIsolated;
    }

    public void onStart() {
        if (this.mIsolated) {
            this.mInstalld = null;
            this.mInstalldLatch.countDown();
        } else {
            connect();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connect() {
        IBinder service = ServiceManager.getService("installd");
        if (service != null) {
            try {
                service.linkToDeath(new IBinder.DeathRecipient() { // from class: com.android.server.pm.Installer$$ExternalSyntheticLambda0
                    @Override // android.os.IBinder.DeathRecipient
                    public final void binderDied() {
                        Installer.this.lambda$connect$0();
                    }
                }, 0);
            } catch (RemoteException unused) {
                service = null;
            }
        }
        if (service != null) {
            this.mInstalld = IInstalld.Stub.asInterface(service);
            this.mInstalldLatch.countDown();
            try {
                invalidateMounts();
                executeDeferredActions();
                this.mInstallerExt.afterInstalldConnected(this, super.getContext());
                return;
            } catch (InstallerException unused2) {
                return;
            }
        }
        Slog.w(TAG, "installd not found; trying again");
        BackgroundThread.getHandler().postDelayed(new Runnable() { // from class: com.android.server.pm.Installer$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                Installer.this.connect();
            }
        }, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$connect$0() {
        Slog.w(TAG, "installd died; reconnecting");
        this.mInstalldLatch = new CountDownLatch(1);
        connect();
    }

    private void executeDeferredActions() throws InstallerException {
        if (this.mDeferSetFirstBoot) {
            setFirstBoot();
        }
    }

    private boolean checkBeforeRemote() throws InstallerException {
        if (this.mWarnIfHeld != null && Thread.holdsLock(this.mWarnIfHeld)) {
            Slog.wtf(TAG, "Calling thread " + Thread.currentThread().getName() + " is holding 0x" + Integer.toHexString(System.identityHashCode(this.mWarnIfHeld)), new Throwable());
        }
        if (this.mIsolated) {
            Slog.i(TAG, "Ignoring request because this installer is isolated");
            return false;
        }
        try {
            if (this.mInstalldLatch.await(10000L, TimeUnit.MILLISECONDS)) {
                return true;
            }
            throw new InstallerException("time out waiting for the installer to be ready");
        } catch (InterruptedException unused) {
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CreateAppDataArgs buildCreateAppDataArgs(String str, String str2, int i, int i2, int i3, String str3, int i4, boolean z) {
        CreateAppDataArgs createAppDataArgs = new CreateAppDataArgs();
        createAppDataArgs.uuid = str;
        createAppDataArgs.packageName = str2;
        createAppDataArgs.userId = i;
        createAppDataArgs.flags = i2;
        if (z) {
            createAppDataArgs.flags = i2 | 8;
        }
        createAppDataArgs.appId = i3;
        createAppDataArgs.seInfo = str3;
        createAppDataArgs.targetSdkVersion = i4;
        return createAppDataArgs;
    }

    private static CreateAppDataResult buildPlaceholderCreateAppDataResult() {
        CreateAppDataResult createAppDataResult = new CreateAppDataResult();
        createAppDataResult.ceDataInode = -1L;
        createAppDataResult.exceptionCode = 0;
        createAppDataResult.exceptionMessage = null;
        return createAppDataResult;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ReconcileSdkDataArgs buildReconcileSdkDataArgs(String str, String str2, List<String> list, int i, int i2, String str3, int i3) {
        ReconcileSdkDataArgs reconcileSdkDataArgs = new ReconcileSdkDataArgs();
        reconcileSdkDataArgs.uuid = str;
        reconcileSdkDataArgs.packageName = str2;
        reconcileSdkDataArgs.subDirNames = list;
        reconcileSdkDataArgs.userId = i;
        reconcileSdkDataArgs.appId = i2;
        reconcileSdkDataArgs.previousAppId = 0;
        reconcileSdkDataArgs.seInfo = str3;
        reconcileSdkDataArgs.flags = i3;
        return reconcileSdkDataArgs;
    }

    public CreateAppDataResult createAppData(CreateAppDataArgs createAppDataArgs) throws InstallerException {
        if (!checkBeforeRemote()) {
            return buildPlaceholderCreateAppDataResult();
        }
        createAppDataArgs.previousAppId = 0;
        try {
            return this.mInstalld.createAppData(createAppDataArgs);
        } catch (Exception e) {
            throw InstallerException.from(e);
        }
    }

    public CreateAppDataResult[] createAppDataBatched(CreateAppDataArgs[] createAppDataArgsArr) throws InstallerException {
        if (!checkBeforeRemote()) {
            CreateAppDataResult[] createAppDataResultArr = new CreateAppDataResult[createAppDataArgsArr.length];
            Arrays.fill(createAppDataResultArr, buildPlaceholderCreateAppDataResult());
            return createAppDataResultArr;
        }
        for (CreateAppDataArgs createAppDataArgs : createAppDataArgsArr) {
            createAppDataArgs.previousAppId = 0;
        }
        try {
            return this.mInstalld.createAppDataBatched(createAppDataArgsArr);
        } catch (Exception e) {
            throw InstallerException.from(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reconcileSdkData(ReconcileSdkDataArgs reconcileSdkDataArgs) throws InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.reconcileSdkData(reconcileSdkDataArgs);
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    public void setFirstBoot() throws InstallerException {
        if (checkBeforeRemote()) {
            try {
                if (this.mInstalld != null) {
                    this.mInstalld.setFirstBoot();
                } else {
                    this.mDeferSetFirstBoot = true;
                }
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Batch {
        private static final int CREATE_APP_DATA_BATCH_SIZE = 256;
        private boolean mExecuted;
        private final List<CreateAppDataArgs> mArgs = new ArrayList();
        private final List<CompletableFuture<Long>> mFutures = new ArrayList();
        public IBatchExt mBatchExt = (IBatchExt) ExtLoader.type(IBatchExt.class).base(this).create();

        public synchronized CompletableFuture<Long> createAppData(CreateAppDataArgs createAppDataArgs) {
            CompletableFuture<Long> completableFuture;
            if (this.mExecuted) {
                throw new IllegalStateException();
            }
            completableFuture = new CompletableFuture<>();
            this.mArgs.add(createAppDataArgs);
            this.mFutures.add(completableFuture);
            return completableFuture;
        }

        public synchronized void execute(Installer installer) throws InstallerException {
            if (this.mExecuted) {
                throw new IllegalStateException();
            }
            this.mExecuted = true;
            int size = this.mArgs.size();
            for (int i = 0; i < size; i += 256) {
                if (!this.mBatchExt.isAsyncJob() || this.mBatchExt.isUserRunningAndNotStopping()) {
                    int min = Math.min(size - i, 256);
                    CreateAppDataArgs[] createAppDataArgsArr = new CreateAppDataArgs[min];
                    for (int i2 = 0; i2 < min; i2++) {
                        createAppDataArgsArr[i2] = this.mArgs.get(i + i2);
                    }
                    CreateAppDataResult[] createAppDataBatched = installer.createAppDataBatched(createAppDataArgsArr);
                    for (int i3 = 0; i3 < createAppDataBatched.length; i3++) {
                        CreateAppDataResult createAppDataResult = createAppDataBatched[i3];
                        CompletableFuture<Long> completableFuture = this.mFutures.get(i + i3);
                        if (createAppDataResult.exceptionCode == 0) {
                            completableFuture.complete(Long.valueOf(createAppDataResult.ceDataInode));
                        } else {
                            completableFuture.completeExceptionally(new InstallerException(createAppDataResult.exceptionMessage));
                        }
                    }
                }
            }
        }
    }

    public void restoreconAppData(String str, String str2, int i, int i2, int i3, String str3) throws InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.restoreconAppData(str, str2, i, i2, i3, str3);
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    public void migrateAppData(String str, String str2, int i, int i2) throws InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.migrateAppData(str, str2, i, i2);
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    public void clearAppData(String str, String str2, int i, int i2, long j) throws InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.clearAppData(str, str2, i, i2, j);
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    public void destroyAppData(String str, String str2, int i, int i2, long j) throws InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.destroyAppData(str, str2, i, i2, j);
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    public void fixupAppData(String str, int i) throws InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.fixupAppData(str, i);
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    public void cleanupInvalidPackageDirs(String str, int i, int i2) throws InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.cleanupInvalidPackageDirs(str, i, i2);
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    public void moveCompleteApp(String str, String str2, String str3, int i, String str4, int i2, String str5) throws InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.moveCompleteApp(str, str2, str3, i, str4, i2, str5);
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    public void getAppSize(String str, String[] strArr, int i, int i2, int i3, long[] jArr, String[] strArr2, PackageStats packageStats) throws InstallerException {
        if (checkBeforeRemote()) {
            if (strArr2 != null) {
                for (String str2 : strArr2) {
                    BlockGuard.getVmPolicy().onPathAccess(str2);
                }
            }
            try {
                long[] appSize = this.mInstalld.getAppSize(str, strArr, i, i2, i3, jArr, strArr2);
                packageStats.codeSize += appSize[0];
                packageStats.dataSize += appSize[1];
                packageStats.cacheSize += appSize[2];
                packageStats.externalCodeSize += appSize[3];
                packageStats.externalDataSize += appSize[4];
                packageStats.externalCacheSize += appSize[5];
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    public void getUserSize(String str, int i, int i2, int[] iArr, PackageStats packageStats) throws InstallerException {
        if (checkBeforeRemote()) {
            try {
                long[] userSize = this.mInstalld.getUserSize(str, i, i2, iArr);
                packageStats.codeSize += userSize[0];
                packageStats.dataSize += userSize[1];
                packageStats.cacheSize += userSize[2];
                packageStats.externalCodeSize += userSize[3];
                packageStats.externalDataSize += userSize[4];
                packageStats.externalCacheSize += userSize[5];
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    public long[] getExternalSize(String str, int i, int i2, int[] iArr) throws InstallerException {
        if (!checkBeforeRemote()) {
            return new long[6];
        }
        try {
            return this.mInstalld.getExternalSize(str, i, i2, iArr);
        } catch (Exception e) {
            throw InstallerException.from(e);
        }
    }

    public CrateMetadata[] getAppCrates(String str, String[] strArr, int i) throws InstallerException {
        if (!checkBeforeRemote()) {
            return null;
        }
        try {
            return this.mInstalld.getAppCrates(str, strArr, i);
        } catch (Exception e) {
            throw InstallerException.from(e);
        }
    }

    public CrateMetadata[] getUserCrates(String str, int i) throws InstallerException {
        if (!checkBeforeRemote()) {
            return null;
        }
        try {
            return this.mInstalld.getUserCrates(str, i);
        } catch (Exception e) {
            throw InstallerException.from(e);
        }
    }

    public void setAppQuota(String str, int i, int i2, long j) throws InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.setAppQuota(str, i, i2, j);
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    public boolean dexopt(String str, int i, String str2, String str3, int i2, String str4, int i3, String str5, String str6, String str7, String str8, boolean z, int i4, String str9, String str10, String str11) throws InstallerException, LegacyDexoptDisabledException {
        checkLegacyDexoptDisabled();
        assertValidInstructionSet(str3);
        BlockGuard.getVmPolicy().onPathAccess(str);
        BlockGuard.getVmPolicy().onPathAccess(str4);
        BlockGuard.getVmPolicy().onPathAccess(str10);
        if (!checkBeforeRemote()) {
            return false;
        }
        try {
            return this.mInstalld.dexopt(str, i, str2, str3, i2, str4, i3, str5, str6, str7, str8, z, i4, str9, str10, str11);
        } catch (Exception e) {
            throw InstallerException.from(e);
        }
    }

    public void controlDexOptBlocking(boolean z) throws LegacyDexoptDisabledException {
        checkLegacyDexoptDisabled();
        try {
            this.mInstalld.controlDexOptBlocking(z);
        } catch (Exception e) {
            Slog.w(TAG, "blockDexOpt failed", e);
        }
    }

    public int mergeProfiles(int i, String str, String str2) throws InstallerException, LegacyDexoptDisabledException {
        checkLegacyDexoptDisabled();
        if (!checkBeforeRemote()) {
            return 2;
        }
        try {
            return this.mInstalld.mergeProfiles(i, str, str2);
        } catch (Exception e) {
            throw InstallerException.from(e);
        }
    }

    public boolean dumpProfiles(int i, String str, String str2, String str3, boolean z) throws InstallerException, LegacyDexoptDisabledException {
        checkLegacyDexoptDisabled();
        if (!checkBeforeRemote()) {
            return false;
        }
        BlockGuard.getVmPolicy().onPathAccess(str3);
        try {
            return this.mInstalld.dumpProfiles(i, str, str2, str3, z);
        } catch (Exception e) {
            throw InstallerException.from(e);
        }
    }

    public boolean copySystemProfile(String str, int i, String str2, String str3) throws InstallerException, LegacyDexoptDisabledException {
        checkLegacyDexoptDisabled();
        if (!checkBeforeRemote()) {
            return false;
        }
        try {
            return this.mInstalld.copySystemProfile(str, i, str2, str3);
        } catch (Exception e) {
            throw InstallerException.from(e);
        }
    }

    public void rmdex(String str, String str2) throws InstallerException, LegacyDexoptDisabledException {
        checkLegacyDexoptDisabled();
        assertValidInstructionSet(str2);
        if (checkBeforeRemote()) {
            BlockGuard.getVmPolicy().onPathAccess(str);
            try {
                this.mInstalld.rmdex(str, str2);
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    public void rmPackageDir(String str, String str2) throws InstallerException {
        if (checkBeforeRemote()) {
            BlockGuard.getVmPolicy().onPathAccess(str2);
            try {
                this.mInstallerExt.beforermPackageDir("rmPackageDir");
                this.mInstalld.rmPackageDir(str, str2);
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    public void clearAppProfiles(String str, String str2) throws InstallerException, LegacyDexoptDisabledException {
        checkLegacyDexoptDisabled();
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.clearAppProfiles(str, str2);
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    public void destroyAppProfiles(String str) throws InstallerException, LegacyDexoptDisabledException {
        checkLegacyDexoptDisabled();
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.destroyAppProfiles(str);
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    public void deleteReferenceProfile(String str, String str2) throws InstallerException, LegacyDexoptDisabledException {
        checkLegacyDexoptDisabled();
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.deleteReferenceProfile(str, str2);
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    public void createUserData(String str, int i, int i2, int i3) throws InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.createUserData(str, i, i2, i3);
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    public void destroyUserData(String str, int i, int i2) throws InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.destroyUserData(str, i, i2);
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    public void freeCache(String str, long j, int i) throws InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.freeCache(str, j, i);
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    public void linkNativeLibraryDirectory(String str, String str2, String str3, int i) throws InstallerException {
        if (checkBeforeRemote()) {
            BlockGuard.getVmPolicy().onPathAccess(str3);
            try {
                this.mInstalld.linkNativeLibraryDirectory(str, str2, str3, i);
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    public void createOatDir(String str, String str2, String str3) throws InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.createOatDir(str, str2, str3);
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    public void linkFile(String str, String str2, String str3, String str4) throws InstallerException {
        if (checkBeforeRemote()) {
            BlockGuard.getVmPolicy().onPathAccess(str3);
            BlockGuard.getVmPolicy().onPathAccess(str4);
            try {
                this.mInstalld.linkFile(str, str2, str3, str4);
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    public void moveAb(String str, String str2, String str3, String str4) throws InstallerException {
        if (checkBeforeRemote()) {
            BlockGuard.getVmPolicy().onPathAccess(str2);
            BlockGuard.getVmPolicy().onPathAccess(str4);
            try {
                this.mInstalld.moveAb(str, str2, str3, str4);
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    public long deleteOdex(String str, String str2, String str3, String str4) throws InstallerException, LegacyDexoptDisabledException {
        checkLegacyDexoptDisabled();
        if (!checkBeforeRemote()) {
            return -1L;
        }
        BlockGuard.getVmPolicy().onPathAccess(str2);
        BlockGuard.getVmPolicy().onPathAccess(str4);
        try {
            return this.mInstalld.deleteOdex(str, str2, str3, str4);
        } catch (Exception e) {
            throw InstallerException.from(e);
        }
    }

    public boolean reconcileSecondaryDexFile(String str, String str2, int i, String[] strArr, String str3, int i2) throws InstallerException, LegacyDexoptDisabledException {
        checkLegacyDexoptDisabled();
        for (String str4 : strArr) {
            assertValidInstructionSet(str4);
        }
        if (!checkBeforeRemote()) {
            return false;
        }
        BlockGuard.getVmPolicy().onPathAccess(str);
        try {
            return this.mInstalld.reconcileSecondaryDexFile(str, str2, i, strArr, str3, i2);
        } catch (Exception e) {
            throw InstallerException.from(e);
        }
    }

    public byte[] hashSecondaryDexFile(String str, String str2, int i, String str3, int i2) throws InstallerException {
        if (!checkBeforeRemote()) {
            return new byte[0];
        }
        BlockGuard.getVmPolicy().onPathAccess(str);
        try {
            return this.mInstalld.hashSecondaryDexFile(str, str2, i, str3, i2);
        } catch (Exception e) {
            throw InstallerException.from(e);
        }
    }

    public boolean createProfileSnapshot(int i, String str, String str2, String str3) throws InstallerException, LegacyDexoptDisabledException {
        checkLegacyDexoptDisabled();
        if (!checkBeforeRemote()) {
            return false;
        }
        try {
            return this.mInstalld.createProfileSnapshot(i, str, str2, str3);
        } catch (Exception e) {
            throw InstallerException.from(e);
        }
    }

    public void destroyProfileSnapshot(String str, String str2) throws InstallerException, LegacyDexoptDisabledException {
        checkLegacyDexoptDisabled();
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.destroyProfileSnapshot(str, str2);
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    public void invalidateMounts() throws InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.invalidateMounts();
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    public boolean isQuotaSupported(String str) throws InstallerException {
        if (!checkBeforeRemote()) {
            return false;
        }
        try {
            return this.mInstalld.isQuotaSupported(str);
        } catch (Exception e) {
            throw InstallerException.from(e);
        }
    }

    public void tryMountDataMirror(String str) throws InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.tryMountDataMirror(str);
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    public void onPrivateVolumeRemoved(String str) throws InstallerException {
        if (checkBeforeRemote()) {
            try {
                this.mInstalld.onPrivateVolumeRemoved(str);
            } catch (Exception e) {
                throw InstallerException.from(e);
            }
        }
    }

    public boolean prepareAppProfile(String str, int i, int i2, String str2, String str3, String str4) throws InstallerException, LegacyDexoptDisabledException {
        checkLegacyDexoptDisabled();
        if (!checkBeforeRemote()) {
            return false;
        }
        BlockGuard.getVmPolicy().onPathAccess(str3);
        BlockGuard.getVmPolicy().onPathAccess(str4);
        try {
            return this.mInstalld.prepareAppProfile(str, i, i2, str2, str3, str4);
        } catch (Exception e) {
            throw InstallerException.from(e);
        }
    }

    public boolean snapshotAppData(String str, int i, int i2, int i3) throws InstallerException {
        if (!checkBeforeRemote()) {
            return false;
        }
        try {
            this.mInstalld.snapshotAppData((String) null, str, i, i2, i3);
            return true;
        } catch (Exception e) {
            throw InstallerException.from(e);
        }
    }

    public boolean restoreAppDataSnapshot(String str, int i, String str2, int i2, int i3, int i4) throws InstallerException {
        if (!checkBeforeRemote()) {
            return false;
        }
        try {
            this.mInstalld.restoreAppDataSnapshot((String) null, str, i, str2, i2, i3, i4);
            return true;
        } catch (Exception e) {
            throw InstallerException.from(e);
        }
    }

    public boolean destroyAppDataSnapshot(String str, int i, int i2, int i3) throws InstallerException {
        if (!checkBeforeRemote()) {
            return false;
        }
        try {
            this.mInstalld.destroyAppDataSnapshot((String) null, str, i, 0L, i2, i3);
            return true;
        } catch (Exception e) {
            throw InstallerException.from(e);
        }
    }

    public boolean destroyCeSnapshotsNotSpecified(int i, int[] iArr) throws InstallerException {
        if (!checkBeforeRemote()) {
            return false;
        }
        try {
            this.mInstalld.destroyCeSnapshotsNotSpecified((String) null, i, iArr);
            return true;
        } catch (Exception e) {
            throw InstallerException.from(e);
        }
    }

    public boolean migrateLegacyObbData() throws InstallerException {
        if (!checkBeforeRemote()) {
            return false;
        }
        try {
            this.mInstalld.migrateLegacyObbData();
            return true;
        } catch (Exception e) {
            throw InstallerException.from(e);
        }
    }

    private static void assertValidInstructionSet(String str) throws InstallerException {
        int i = 0;
        if (Build.MTK_HBT_ON_64BIT_ONLY_CHIP) {
            String[] strArr = Build.MTK_HBT_SUPPORTED_ABIS;
            int length = strArr.length;
            while (i < length) {
                if (VMRuntime.getInstructionSet(strArr[i]).equals(str)) {
                    return;
                } else {
                    i++;
                }
            }
        } else if (Build.QCOM_TANGO_ON_64BIT_ONLY_CHIP) {
            String[] strArr2 = Build.QCOM_TANGO_SUPPORTED_ABIS;
            int length2 = strArr2.length;
            while (i < length2) {
                if (VMRuntime.getInstructionSet(strArr2[i]).equals(str)) {
                    return;
                } else {
                    i++;
                }
            }
        } else {
            String[] strArr3 = Build.SUPPORTED_ABIS;
            int length3 = strArr3.length;
            while (i < length3) {
                if (VMRuntime.getInstructionSet(strArr3[i]).equals(str)) {
                    return;
                } else {
                    i++;
                }
            }
        }
        throw new InstallerException("Invalid instruction set: " + str);
    }

    public boolean compileLayouts(String str, String str2, String str3, int i) {
        try {
            return this.mInstalld.compileLayouts(str, str2, str3, i);
        } catch (RemoteException unused) {
            return false;
        }
    }

    public int getOdexVisibility(String str, String str2, String str3, String str4) throws InstallerException, LegacyDexoptDisabledException {
        checkLegacyDexoptDisabled();
        if (!checkBeforeRemote()) {
            return -1;
        }
        BlockGuard.getVmPolicy().onPathAccess(str2);
        BlockGuard.getVmPolicy().onPathAccess(str4);
        try {
            return this.mInstalld.getOdexVisibility(str, str2, str3, str4);
        } catch (Exception e) {
            throw InstallerException.from(e);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class InstallerException extends Exception {
        public InstallerException(String str) {
            super(str);
        }

        public static InstallerException from(Exception exc) throws InstallerException {
            throw new InstallerException(exc.toString());
        }
    }

    public PersistableBundle oplusCommonInterface(String str, PersistableBundle persistableBundle) throws InstallerException {
        if (!checkBeforeRemote()) {
            Slog.e(TAG, "oplusCommonInterface: no remote.");
            return null;
        }
        try {
            return this.mInstalld.oplusCommonInterface(str, persistableBundle);
        } catch (Exception e) {
            throw InstallerException.from(e);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class LegacyDexoptDisabledException extends Exception {
        public LegacyDexoptDisabledException() {
            super("Invalid call to legacy dexopt method while ART Service is in use.");
        }
    }

    public static void checkLegacyDexoptDisabled() throws LegacyDexoptDisabledException {
        if (DexOptHelper.useArtService()) {
            throw new LegacyDexoptDisabledException();
        }
    }
}
