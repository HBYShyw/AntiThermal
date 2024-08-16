package com.android.server.pm;

import android.R;
import android.content.Intent;
import android.content.pm.IPackageInstallObserver2;
import android.content.pm.IPackageMoveObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.content.pm.parsing.ApkLiteParseUtils;
import android.content.pm.parsing.PackageLite;
import android.content.pm.parsing.result.ParseResult;
import android.content.pm.parsing.result.ParseTypeImpl;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.os.storage.VolumeInfo;
import android.util.MathUtils;
import android.util.Slog;
import android.util.SparseIntArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.SomeArgs;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.pm.Installer;
import com.android.server.pm.parsing.pkg.AndroidPackageInternal;
import com.android.server.pm.parsing.pkg.AndroidPackageUtils;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageStateInternal;
import com.android.server.pm.pkg.PackageStateUtils;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class MovePackageHelper {
    final PackageManagerService mPm;

    public MovePackageHelper(PackageManagerService packageManagerService) {
        this.mPm = packageManagerService;
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x0171  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x01b5 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void movePackageInternal(final String str, String str2, final int i, int i2, UserHandle userHandle) throws PackageManagerException {
        String absolutePath;
        final PackageFreezer freezePackage;
        File dataAppDirectory;
        File path;
        boolean z;
        File file;
        InstallSource installSource;
        long j;
        int i3;
        PackageLite packageLite;
        MoveInfo moveInfo;
        StorageManager storageManager = (StorageManager) this.mPm.mInjector.getSystemService(StorageManager.class);
        PackageManager packageManager = this.mPm.mContext.getPackageManager();
        Computer snapshotComputer = this.mPm.snapshotComputer();
        PackageStateInternal packageStateForInstalledAndFiltered = snapshotComputer.getPackageStateForInstalledAndFiltered(str, i2, userHandle.getIdentifier());
        if (packageStateForInstalledAndFiltered == null || packageStateForInstalledAndFiltered.getPkg() == null) {
            throw new PackageManagerException(-2, "Missing package");
        }
        AndroidPackageInternal pkg = packageStateForInstalledAndFiltered.getPkg();
        if (packageStateForInstalledAndFiltered.isSystem()) {
            throw new PackageManagerException(-3, "Cannot move system application");
        }
        boolean equals = "private".equals(str2);
        boolean z2 = this.mPm.mContext.getResources().getBoolean(R.bool.config_allowTheaterModeWakeFromCameraLens);
        if (equals && !z2) {
            throw new PackageManagerException(-9, "3rd party apps are not allowed on internal storage");
        }
        String volumeUuid = packageStateForInstalledAndFiltered.getVolumeUuid();
        File file2 = new File(pkg.getPath());
        File file3 = new File(file2, "oat");
        if (!file2.isDirectory() || !file3.isDirectory()) {
            throw new PackageManagerException(-6, "Move only supported for modern cluster style installs");
        }
        if (Objects.equals(volumeUuid, str2)) {
            throw new PackageManagerException(-6, "Package already moved to " + str2);
        }
        if (!pkg.isExternalStorage() && this.mPm.isPackageDeviceAdminOnAnyUser(snapshotComputer, str)) {
            throw new PackageManagerException(-8, "Device admin cannot be moved");
        }
        if (snapshotComputer.getFrozenPackages().containsKey(str)) {
            throw new PackageManagerException(-7, "Failed to move already frozen package");
        }
        final boolean isExternalStorage = pkg.isExternalStorage();
        File file4 = new File(pkg.getPath());
        InstallSource installSource2 = packageStateForInstalledAndFiltered.getInstallSource();
        String cpuAbiOverride = packageStateForInstalledAndFiltered.getCpuAbiOverride();
        int appId = UserHandle.getAppId(pkg.getUid());
        String seInfo = packageStateForInstalledAndFiltered.getSeInfo();
        String valueOf = String.valueOf(packageManager.getApplicationLabel(AndroidPackageUtils.generateAppInfoWithoutState(pkg)));
        int targetSdkVersion = pkg.getTargetSdkVersion();
        int[] queryInstalledUsers = PackageStateUtils.queryInstalledUsers(packageStateForInstalledAndFiltered, this.mPm.mUserManager.getUserIds(), true);
        if (file4.getParentFile().getName().startsWith("~~")) {
            absolutePath = file4.getParentFile().getAbsolutePath();
        } else {
            absolutePath = file4.getAbsolutePath();
        }
        String str3 = absolutePath;
        synchronized (this.mPm.mLock) {
            freezePackage = this.mPm.freezePackage(str, -1, "movePackageInternal", 10);
        }
        Bundle bundle = new Bundle();
        bundle.putString("android.intent.extra.PACKAGE_NAME", str);
        bundle.putString("android.intent.extra.TITLE", valueOf);
        this.mPm.mMoveCallbacks.notifyCreated(i, bundle);
        if (Objects.equals(StorageManager.UUID_PRIVATE_INTERNAL, str2)) {
            dataAppDirectory = Environment.getDataAppDirectory(str2);
        } else {
            if ("primary_physical".equals(str2)) {
                path = storageManager.getPrimaryPhysicalVolume().getPath();
                z = false;
                if (z) {
                    for (int i4 : queryInstalledUsers) {
                        if (StorageManager.isFileEncrypted() && !StorageManager.isUserKeyUnlocked(i4)) {
                            freezePackage.close();
                            throw new PackageManagerException(-10, "User " + i4 + " must be unlocked");
                        }
                    }
                }
                PackageStats packageStats = new PackageStats(null, -1);
                synchronized (this.mPm.mInstallLock) {
                    int length = queryInstalledUsers.length;
                    int i5 = 0;
                    while (i5 < length) {
                        int i6 = length;
                        if (!getPackageSizeInfoLI(str, queryInstalledUsers[i5], packageStats)) {
                            freezePackage.close();
                            throw new PackageManagerException(-6, "Failed to measure package size");
                        }
                        i5++;
                        length = i6;
                    }
                }
                if (PackageManagerService.DEBUG_INSTALL) {
                    Slog.d("PackageManager", "Measured code size " + packageStats.codeSize + ", data size " + packageStats.dataSize);
                }
                final long usableSpace = path.getUsableSpace();
                if (z) {
                    file = file4;
                    installSource = installSource2;
                    j = packageStats.codeSize + packageStats.dataSize;
                } else {
                    file = file4;
                    installSource = installSource2;
                    j = packageStats.codeSize;
                }
                if (j > storageManager.getStorageBytesUntilLow(path)) {
                    freezePackage.close();
                    throw new PackageManagerException(-6, "Not enough free space to move");
                }
                this.mPm.mMoveCallbacks.notifyStatusChanged(i, 10);
                final CountDownLatch countDownLatch = new CountDownLatch(1);
                IPackageInstallObserver2.Stub stub = new IPackageInstallObserver2.Stub() { // from class: com.android.server.pm.MovePackageHelper.1
                    public void onUserActionRequired(Intent intent) throws RemoteException {
                        freezePackage.close();
                        throw new IllegalStateException();
                    }

                    public void onPackageInstalled(String str4, int i7, String str5, Bundle bundle2) throws RemoteException {
                        if (PackageManagerService.DEBUG_INSTALL) {
                            Slog.d("PackageManager", "Install result for move: " + PackageManager.installStatusToString(i7, str5));
                        }
                        countDownLatch.countDown();
                        freezePackage.close();
                        int installStatusToPublicStatus = PackageManager.installStatusToPublicStatus(i7);
                        if (installStatusToPublicStatus == 0) {
                            MovePackageHelper.this.mPm.mMoveCallbacks.notifyStatusChanged(i, -100);
                            MovePackageHelper.this.logAppMovedStorage(str, isExternalStorage);
                        } else if (installStatusToPublicStatus == 6) {
                            MovePackageHelper.this.mPm.mMoveCallbacks.notifyStatusChanged(i, -1);
                        } else {
                            MovePackageHelper.this.mPm.mMoveCallbacks.notifyStatusChanged(i, -6);
                        }
                    }
                };
                if (z) {
                    final File file5 = path;
                    packageLite = null;
                    final long j2 = j;
                    i3 = 0;
                    new Thread(new Runnable() { // from class: com.android.server.pm.MovePackageHelper$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            MovePackageHelper.this.lambda$movePackageInternal$0(countDownLatch, usableSpace, file5, j2, i);
                        }
                    }).start();
                    moveInfo = new MoveInfo(i, volumeUuid, str2, str, appId, seInfo, targetSdkVersion, str3);
                } else {
                    i3 = 0;
                    packageLite = null;
                    moveInfo = null;
                }
                OriginInfo fromExistingFile = OriginInfo.fromExistingFile(file);
                ParseResult parsePackageLite = ApkLiteParseUtils.parsePackageLite(ParseTypeImpl.forDefaultParsing(), new File(fromExistingFile.mResolvedPath), i3);
                new InstallingSession(fromExistingFile, moveInfo, stub, 18, installSource, str2, userHandle, cpuAbiOverride, 0, parsePackageLite.isSuccess() ? (PackageLite) parsePackageLite.getResult() : packageLite, this.mPm).movePackage();
                return;
            }
            VolumeInfo findVolumeByUuid = storageManager.findVolumeByUuid(str2);
            if (findVolumeByUuid == null || findVolumeByUuid.getType() != 1 || !findVolumeByUuid.isMountedWritable()) {
                freezePackage.close();
                throw new PackageManagerException(-6, "Move location not mounted private volume");
            }
            dataAppDirectory = Environment.getDataAppDirectory(str2);
        }
        path = dataAppDirectory;
        z = true;
        if (z) {
        }
        PackageStats packageStats2 = new PackageStats(null, -1);
        synchronized (this.mPm.mInstallLock) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$movePackageInternal$0(CountDownLatch countDownLatch, long j, File file, long j2, int i) {
        while (!countDownLatch.await(1L, TimeUnit.SECONDS)) {
            this.mPm.mMoveCallbacks.notifyStatusChanged(i, ((int) MathUtils.constrain(((j - file.getUsableSpace()) * 80) / j2, 0L, 80L)) + 10);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logAppMovedStorage(String str, boolean z) {
        AndroidPackage androidPackage = this.mPm.snapshotComputer().getPackage(str);
        if (androidPackage == null) {
            return;
        }
        int packageExternalStorageType = PackageManagerServiceUtils.getPackageExternalStorageType(((StorageManager) this.mPm.mInjector.getSystemService(StorageManager.class)).findVolumeByUuid(StorageManager.convert(androidPackage.getVolumeUuid()).toString()), androidPackage.isExternalStorage());
        if (!z && androidPackage.isExternalStorage()) {
            FrameworkStatsLog.write(183, packageExternalStorageType, 1, str);
        } else {
            if (!z || androidPackage.isExternalStorage()) {
                return;
            }
            FrameworkStatsLog.write(183, packageExternalStorageType, 2, str);
        }
    }

    @GuardedBy({"mPm.mInstallLock"})
    private boolean getPackageSizeInfoLI(String str, int i, PackageStats packageStats) {
        PackageStateInternal packageStateInternal = this.mPm.snapshotComputer().getPackageStateInternal(str);
        if (packageStateInternal == null) {
            Slog.w("PackageManager", "Failed to find settings for " + str);
            return false;
        }
        try {
            this.mPm.mInstaller.getAppSize(packageStateInternal.getVolumeUuid(), new String[]{str}, i, 0, packageStateInternal.getAppId(), new long[]{packageStateInternal.getUserStateOrDefault(i).getCeDataInode()}, new String[]{packageStateInternal.getPathString()}, packageStats);
            if (PackageManagerServiceUtils.isSystemApp(packageStateInternal) && !PackageManagerServiceUtils.isUpdatedSystemApp(packageStateInternal)) {
                packageStats.codeSize = 0L;
            }
            packageStats.dataSize -= packageStats.cacheSize;
            return true;
        } catch (Installer.InstallerException e) {
            Slog.w("PackageManager", String.valueOf(e));
            return false;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class MoveCallbacks extends Handler {
        private static final int MSG_CREATED = 1;
        private static final int MSG_STATUS_CHANGED = 2;
        private final RemoteCallbackList<IPackageMoveObserver> mCallbacks;
        public final SparseIntArray mLastStatus;

        public MoveCallbacks(Looper looper) {
            super(looper);
            this.mCallbacks = new RemoteCallbackList<>();
            this.mLastStatus = new SparseIntArray();
        }

        public void register(IPackageMoveObserver iPackageMoveObserver) {
            this.mCallbacks.register(iPackageMoveObserver);
        }

        public void unregister(IPackageMoveObserver iPackageMoveObserver) {
            this.mCallbacks.unregister(iPackageMoveObserver);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            SomeArgs someArgs = (SomeArgs) message.obj;
            int beginBroadcast = this.mCallbacks.beginBroadcast();
            for (int i = 0; i < beginBroadcast; i++) {
                try {
                    invokeCallback(this.mCallbacks.getBroadcastItem(i), message.what, someArgs);
                } catch (RemoteException unused) {
                }
            }
            this.mCallbacks.finishBroadcast();
            someArgs.recycle();
        }

        private void invokeCallback(IPackageMoveObserver iPackageMoveObserver, int i, SomeArgs someArgs) throws RemoteException {
            if (i == 1) {
                iPackageMoveObserver.onCreated(someArgs.argi1, (Bundle) someArgs.arg2);
            } else {
                if (i != 2) {
                    return;
                }
                iPackageMoveObserver.onStatusChanged(someArgs.argi1, someArgs.argi2, ((Long) someArgs.arg3).longValue());
            }
        }

        public void notifyCreated(int i, Bundle bundle) {
            Slog.v("PackageManager", "Move " + i + " created " + bundle.toString());
            SomeArgs obtain = SomeArgs.obtain();
            obtain.argi1 = i;
            obtain.arg2 = bundle;
            obtainMessage(1, obtain).sendToTarget();
        }

        public void notifyStatusChanged(int i, int i2) {
            notifyStatusChanged(i, i2, -1L);
        }

        public void notifyStatusChanged(int i, int i2, long j) {
            Slog.v("PackageManager", "Move " + i + " status " + i2);
            SomeArgs obtain = SomeArgs.obtain();
            obtain.argi1 = i;
            obtain.argi2 = i2;
            obtain.arg3 = Long.valueOf(j);
            obtainMessage(2, obtain).sendToTarget();
            synchronized (this.mLastStatus) {
                this.mLastStatus.put(i, i2);
            }
        }
    }
}
