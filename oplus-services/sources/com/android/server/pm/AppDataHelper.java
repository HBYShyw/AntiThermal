package com.android.server.pm;

import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.os.CreateAppDataArgs;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Trace;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.os.storage.StorageManagerInternal;
import android.os.storage.VolumeInfo;
import android.security.AndroidKeyStoreMaintenance;
import android.text.TextUtils;
import android.util.Slog;
import android.util.TimingsTraceLog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import com.android.server.SystemServerInitThreadPool;
import com.android.server.pm.Installer;
import com.android.server.pm.PackageManagerLocal;
import com.android.server.pm.dex.ArtManagerService;
import com.android.server.pm.parsing.pkg.AndroidPackageUtils;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageState;
import com.android.server.pm.pkg.PackageStateInternal;
import com.android.server.pm.pkg.SELinuxUtil;
import dalvik.system.VMRuntime;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class AppDataHelper {
    public static boolean DEBUG_APP_DATA = false;
    private final IAppDataHelperExt mAppDataHelperExt;
    private final ArtManagerService mArtManagerService;
    private final PackageManagerServiceInjector mInjector;
    private final Installer mInstaller;
    private final PackageManagerService mPm;
    private final IAppDataHelperWrapper mWrapper = new AppDataHelperWrapper();

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppDataHelper(PackageManagerService packageManagerService) {
        this.mPm = packageManagerService;
        PackageManagerServiceInjector packageManagerServiceInjector = packageManagerService.mInjector;
        this.mInjector = packageManagerServiceInjector;
        this.mInstaller = packageManagerServiceInjector.getInstaller();
        this.mArtManagerService = packageManagerServiceInjector.getArtManagerService();
        IAppDataHelperExt iAppDataHelperExt = (IAppDataHelperExt) ExtLoader.type(IAppDataHelperExt.class).base(this).create();
        this.mAppDataHelperExt = iAppDataHelperExt;
        iAppDataHelperExt.init(packageManagerService, this);
    }

    @GuardedBy({"mPm.mInstallLock"})
    public void prepareAppDataAfterInstallLIF(AndroidPackage androidPackage) {
        prepareAppDataPostCommitLIF(androidPackage, 0);
    }

    @GuardedBy({"mPm.mInstallLock"})
    public void prepareAppDataPostCommitLIF(final AndroidPackage androidPackage, int i) {
        PackageSetting packageLPr;
        int i2;
        synchronized (this.mPm.mLock) {
            packageLPr = this.mPm.mSettings.getPackageLPr(androidPackage.getPackageName());
            this.mPm.mSettings.writeKernelMappingLPr(packageLPr);
        }
        if (!shouldHaveAppStorage(androidPackage)) {
            Slog.w("PackageManager", "Skipping preparing app data for " + androidPackage.getPackageName());
            return;
        }
        Installer.Batch batch = new Installer.Batch();
        final UserManagerInternal userManagerInternal = this.mInjector.getUserManagerInternal();
        final StorageManagerInternal storageManagerInternal = (StorageManagerInternal) this.mInjector.getLocalService(StorageManagerInternal.class);
        for (final UserInfo userInfo : userManagerInternal.getUsers(false)) {
            if (StorageManager.isUserKeyUnlocked(userInfo.id) && storageManagerInternal.isCeStoragePrepared(userInfo.id)) {
                i2 = 3;
            } else if (userManagerInternal.isUserRunning(userInfo.id)) {
                i2 = 1;
            }
            int i3 = i2;
            if (packageLPr.getInstalled(userInfo.id)) {
                prepareAppData(batch, androidPackage, i, userInfo.id, i3).thenRun(new Runnable() { // from class: com.android.server.pm.AppDataHelper$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        AppDataHelper.lambda$prepareAppDataPostCommitLIF$0(UserManagerInternal.this, userInfo, androidPackage, storageManagerInternal);
                    }
                });
            }
        }
        executeBatchLI(batch);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$prepareAppDataPostCommitLIF$0(UserManagerInternal userManagerInternal, UserInfo userInfo, AndroidPackage androidPackage, StorageManagerInternal storageManagerInternal) {
        if (userManagerInternal.isUserUnlockingOrUnlocked(userInfo.id)) {
            storageManagerInternal.prepareAppDataAfterInstall(androidPackage.getPackageName(), UserHandle.getUid(userInfo.id, UserHandle.getAppId(androidPackage.getUid())));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void executeBatchLI(Installer.Batch batch) {
        try {
            batch.execute(this.mInstaller);
        } catch (Installer.InstallerException e) {
            Slog.w("PackageManager", "Failed to execute pending operations", e);
        }
    }

    private CompletableFuture<?> prepareAppData(Installer.Batch batch, AndroidPackage androidPackage, int i, int i2, int i3) {
        if (androidPackage == null) {
            Slog.wtf("PackageManager", "Package was null!", new Throwable());
            return CompletableFuture.completedFuture(null);
        }
        if (!shouldHaveAppStorage(androidPackage)) {
            Slog.w("PackageManager", "Skipping preparing app data for " + androidPackage.getPackageName());
            return CompletableFuture.completedFuture(null);
        }
        return prepareAppDataLeaf(batch, androidPackage, i, i2, i3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prepareAppDataAndMigrate(Installer.Batch batch, final PackageState packageState, final AndroidPackage androidPackage, final int i, final int i2, final boolean z) {
        prepareAppData(batch, androidPackage, -1, i, i2).thenRun(new Runnable() { // from class: com.android.server.pm.AppDataHelper$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                AppDataHelper.this.lambda$prepareAppDataAndMigrate$1(z, packageState, androidPackage, i, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareAppDataAndMigrate$1(boolean z, PackageState packageState, AndroidPackage androidPackage, int i, int i2) {
        if (z && maybeMigrateAppDataLIF(packageState, androidPackage, i)) {
            Installer.Batch batch = new Installer.Batch();
            prepareAppData(batch, androidPackage, -1, i, i2);
            executeBatchLI(batch);
        }
        this.mWrapper.getExtImpl().afterDataPreparedInPrepareAppDataAndMigrate(androidPackage, i, i2);
    }

    private CompletableFuture<?> prepareAppDataLeaf(final Installer.Batch batch, final AndroidPackage androidPackage, int i, final int i2, final int i3) {
        final PackageSetting packageLPr;
        String seinfoUser;
        if (DEBUG_APP_DATA) {
            Slog.v("PackageManager", "prepareAppData for " + androidPackage.getPackageName() + " u" + i2 + " 0x" + Integer.toHexString(i3));
        }
        synchronized (this.mPm.mLock) {
            packageLPr = this.mPm.mSettings.getPackageLPr(androidPackage.getPackageName());
            seinfoUser = SELinuxUtil.getSeinfoUser(packageLPr.readUserState(i2));
        }
        final String volumeUuid = androidPackage.getVolumeUuid();
        final String packageName = androidPackage.getPackageName();
        final int appId = UserHandle.getAppId(androidPackage.getUid());
        String seInfo = packageLPr.getSeInfo();
        Preconditions.checkNotNull(seInfo);
        final String str = seInfo + seinfoUser;
        final CreateAppDataArgs buildCreateAppDataArgs = Installer.buildCreateAppDataArgs(volumeUuid, packageName, i2, i3, appId, str, androidPackage.getTargetSdkVersion(), !androidPackage.getUsesSdkLibraries().isEmpty());
        buildCreateAppDataArgs.previousAppId = i;
        return batch.createAppData(buildCreateAppDataArgs).whenComplete(new BiConsumer() { // from class: com.android.server.pm.AppDataHelper$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                AppDataHelper.this.lambda$prepareAppDataLeaf$2(batch, packageLPr, androidPackage, i2, i3, appId, volumeUuid, str, packageName, buildCreateAppDataArgs, (Long) obj, (Throwable) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareAppDataLeaf$2(Installer.Batch batch, PackageSetting packageSetting, AndroidPackage androidPackage, int i, int i2, int i3, String str, String str2, String str3, CreateAppDataArgs createAppDataArgs, Long l, Throwable th) {
        Long l2;
        if (this.mWrapper.getExtImpl().skipWorkAfterCreateAppData(batch.mBatchExt, packageSetting)) {
            return;
        }
        if (th != null) {
            l2 = Long.valueOf(this.mWrapper.getExtImpl().fixDataForExceptionInPrepareAppDataLeaf(-1L, androidPackage, i, i2, i3, str, str2, str3));
            if (l2.longValue() == -1) {
                PackageManagerServiceUtils.logCriticalInfo(5, "Failed to create app data for " + str3 + ", but trying to recover: " + th);
                destroyAppDataLeafLIF(androidPackage, i, i2);
                try {
                    l2 = Long.valueOf(this.mInstaller.createAppData(createAppDataArgs).ceDataInode);
                    PackageManagerServiceUtils.logCriticalInfo(3, "Recovery succeeded!");
                } catch (Installer.InstallerException unused) {
                    PackageManagerServiceUtils.logCriticalInfo(3, "Recovery failed!");
                }
            }
        } else {
            l2 = l;
        }
        if (!DexOptHelper.useArtService() && (this.mPm.isDeviceUpgrading() || this.mPm.isFirstBoot() || i != 0)) {
            try {
                this.mArtManagerService.prepareAppProfiles(androidPackage, i, false);
            } catch (Installer.LegacyDexoptDisabledException e) {
                throw new RuntimeException(e);
            }
        }
        if ((i2 & 2) != 0 && l2.longValue() != -1) {
            synchronized (this.mPm.mLock) {
                packageSetting.setCeDataInode(l2.longValue(), i);
            }
        }
        prepareAppDataContentsLeafLIF(androidPackage, packageSetting, i, i2);
        this.mWrapper.getExtImpl().afterCreateAppDataCompleted(l2, th, androidPackage, i, i2);
    }

    public void prepareAppDataContentsLIF(AndroidPackage androidPackage, PackageStateInternal packageStateInternal, int i, int i2) {
        if (androidPackage == null) {
            Slog.wtf("PackageManager", "Package was null!", new Throwable());
        } else {
            prepareAppDataContentsLeafLIF(androidPackage, packageStateInternal, i, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prepareAppDataContentsLeafLIF(AndroidPackage androidPackage, PackageStateInternal packageStateInternal, int i, int i2) {
        String volumeUuid = androidPackage.getVolumeUuid();
        String packageName = androidPackage.getPackageName();
        if ((i2 & 2) != 0) {
            String rawPrimaryCpuAbi = packageStateInternal == null ? AndroidPackageUtils.getRawPrimaryCpuAbi(androidPackage) : packageStateInternal.getPrimaryCpuAbi();
            if (rawPrimaryCpuAbi == null || VMRuntime.is64BitAbi(rawPrimaryCpuAbi)) {
                return;
            }
            String nativeLibraryDir = androidPackage.getNativeLibraryDir();
            if (new File(nativeLibraryDir).exists()) {
                try {
                    this.mInstaller.linkNativeLibraryDirectory(volumeUuid, packageName, nativeLibraryDir, i);
                } catch (Installer.InstallerException e) {
                    Slog.e("PackageManager", "Failed to link native for " + packageName + ": " + e);
                }
            }
        }
    }

    private boolean maybeMigrateAppDataLIF(PackageState packageState, AndroidPackage androidPackage, int i) {
        if (!packageState.isSystem() || StorageManager.isFileEncrypted()) {
            return false;
        }
        try {
            this.mInstaller.migrateAppData(androidPackage.getVolumeUuid(), androidPackage.getPackageName(), i, androidPackage.isDefaultToDeviceProtectedStorage() ? 1 : 2);
        } catch (Installer.InstallerException e) {
            PackageManagerServiceUtils.logCriticalInfo(5, "Failed to migrate " + androidPackage.getPackageName() + ": " + e.getMessage());
        }
        return true;
    }

    public void reconcileAppsData(int i, int i2, boolean z) {
        StorageManager storageManager = (StorageManager) this.mInjector.getSystemService(StorageManager.class);
        IAppDataHelperExt iAppDataHelperExt = this.mAppDataHelperExt;
        if (iAppDataHelperExt != null) {
            iAppDataHelperExt.beforeReconcileAppsData("reconcileAppsData");
        }
        Iterator it = storageManager.getWritablePrivateVolumes().iterator();
        while (it.hasNext()) {
            String fsUuid = ((VolumeInfo) it.next()).getFsUuid();
            synchronized (this.mPm.mInstallLock) {
                reconcileAppsDataLI(fsUuid, i, i2, z);
            }
        }
        IAppDataHelperExt iAppDataHelperExt2 = this.mAppDataHelperExt;
        if (iAppDataHelperExt2 != null) {
            iAppDataHelperExt2.afterReconcileAppsData("reconcileAppsData");
        }
        this.mWrapper.getExtImpl().onEndInReconcileAppsData(this.mPm.isDeviceUpgrading(), PackageManagerService.DEBUG_DEXOPT);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mPm.mInstallLock"})
    public void reconcileAppsDataLI(String str, int i, int i2, boolean z) {
        reconcileAppsDataLI(str, i, i2, z, false);
    }

    @GuardedBy({"mPm.mInstallLock"})
    private List<String> reconcileAppsDataLI(String str, int i, int i2, boolean z, boolean z2) {
        String str2;
        String str3;
        String str4;
        int i3;
        Computer computer;
        IPkgReconcileSkipExt iPkgReconcileSkipExt;
        IPkgReconcileDelayedExt iPkgReconcileDelayedExt;
        File[] fileArr;
        String str5;
        Computer computer2;
        int i4;
        String str6;
        int i5;
        String str7;
        String str8;
        int i6;
        String str9;
        int i7;
        int i8;
        Computer computer3;
        Slog.v("PackageManager", "reconcileAppsData for " + str + " u" + i + " 0x" + Integer.toHexString(i2) + " migrateAppData=" + z);
        ((IPackageManagerServiceUtilsExt) ExtLoader.type(IPackageManagerServiceUtilsExt.class).create()).addBootEvent("PMS:reconcileAppsDataLI");
        ArrayList arrayList = z2 ? new ArrayList() : null;
        int i9 = 5;
        try {
            this.mInstaller.cleanupInvalidPackageDirs(str, i, i2);
        } catch (Installer.InstallerException e) {
            PackageManagerServiceUtils.logCriticalInfo(5, "Failed to cleanup deleted dirs: " + e);
        }
        File dataUserCeDirectory = Environment.getDataUserCeDirectory(str, i);
        File dataUserDeDirectory = Environment.getDataUserDeDirectory(str, i);
        Computer snapshotComputer = this.mPm.snapshotComputer();
        String str10 = " due to: ";
        String str11 = "Destroying ";
        if ((i2 & 2) == 0) {
            str2 = "Destroying ";
            str3 = " due to: ";
            str4 = "Failed to destroy: ";
            i3 = 5;
            computer = snapshotComputer;
        } else if (!this.mWrapper.getExtImpl().useCustomLogicForCeInReconcileAppsDataLI(z2, i2, i)) {
            Slog.i("PackageManager", "reconcileAppsData for StorageManager.FLAG_STORAGE_CE start");
            if (StorageManager.isFileEncrypted() && !StorageManager.isUserKeyUnlocked(i)) {
                throw new RuntimeException("Yikes, someone asked us to reconcile CE storage while " + i + " was still locked; this would have caused massive data loss!");
            }
            File[] listFilesOrEmpty = FileUtils.listFilesOrEmpty(dataUserCeDirectory);
            int length = listFilesOrEmpty.length;
            String str12 = "Failed to destroy: ";
            int i10 = 0;
            while (i10 < length) {
                File file = listFilesOrEmpty[i10];
                File[] fileArr2 = listFilesOrEmpty;
                String name = file.getName();
                try {
                    assertPackageStorageValid(snapshotComputer, str, name, i);
                    i7 = length;
                    str7 = str11;
                    str8 = str10;
                    computer3 = snapshotComputer;
                    str9 = str12;
                    i6 = 5;
                    i8 = i10;
                } catch (PackageManagerException e2) {
                    int i11 = length;
                    PackageManagerServiceUtils.logCriticalInfo(5, str11 + file + str10 + e2);
                    try {
                        if (this.mWrapper.getExtImpl().skipDestroyCeDataInReconcileAppsDataLI(str, name, i)) {
                            str7 = str11;
                            str8 = str10;
                            i6 = 5;
                            str9 = str12;
                            i7 = i11;
                            i8 = i10;
                            computer3 = snapshotComputer;
                        } else {
                            i7 = i11;
                            str7 = str11;
                            str8 = str10;
                            str9 = str12;
                            i8 = i10;
                            i6 = 5;
                            computer3 = snapshotComputer;
                            try {
                                this.mInstaller.destroyAppData(str, name, i, 2, 0L);
                            } catch (Installer.InstallerException e3) {
                                e = e3;
                                PackageManagerServiceUtils.logCriticalInfo(i6, str9 + e);
                                i10 = i8 + 1;
                                i9 = i6;
                                str12 = str9;
                                length = i7;
                                listFilesOrEmpty = fileArr2;
                                snapshotComputer = computer3;
                                str11 = str7;
                                str10 = str8;
                            }
                        }
                    } catch (Installer.InstallerException e4) {
                        e = e4;
                        str7 = str11;
                        str8 = str10;
                        i6 = 5;
                        str9 = str12;
                        i7 = i11;
                        i8 = i10;
                        computer3 = snapshotComputer;
                    }
                }
                i10 = i8 + 1;
                i9 = i6;
                str12 = str9;
                length = i7;
                listFilesOrEmpty = fileArr2;
                snapshotComputer = computer3;
                str11 = str7;
                str10 = str8;
            }
            str2 = str11;
            str3 = str10;
            i3 = i9;
            computer = snapshotComputer;
            str4 = str12;
            Slog.i("PackageManager", "reconcileAppsData for StorageManager.FLAG_STORAGE_CE end");
        } else {
            return this.mWrapper.getExtImpl().customLogicForCeInReconcileAppsDataLI(arrayList, dataUserCeDirectory, str, i, i2, z2, z, snapshotComputer);
        }
        if ((i2 & 1) != 0) {
            Slog.i("PackageManager", "reconcileAppsData for StorageManager.FLAG_STORAGE_DE start");
            File[] listFilesOrEmpty2 = FileUtils.listFilesOrEmpty(dataUserDeDirectory);
            int length2 = listFilesOrEmpty2.length;
            int i12 = 0;
            while (i12 < length2) {
                File file2 = listFilesOrEmpty2[i12];
                String name2 = file2.getName();
                Computer computer4 = computer;
                try {
                    assertPackageStorageValid(computer4, str, name2, i);
                    computer2 = computer4;
                    i4 = i12;
                    fileArr = listFilesOrEmpty2;
                    i5 = length2;
                    str6 = str2;
                    str5 = str3;
                } catch (PackageManagerException e5) {
                    StringBuilder sb = new StringBuilder();
                    fileArr = listFilesOrEmpty2;
                    String str13 = str2;
                    sb.append(str13);
                    sb.append(file2);
                    String str14 = str3;
                    sb.append(str14);
                    sb.append(e5);
                    PackageManagerServiceUtils.logCriticalInfo(i3, sb.toString());
                    try {
                        if (this.mWrapper.getExtImpl().skipDestroyDeDataInReconcileAppsDataLI(str, name2, i)) {
                            str5 = str14;
                            computer2 = computer4;
                            i4 = i12;
                            str6 = str13;
                            i5 = length2;
                        } else {
                            str5 = str14;
                            computer2 = computer4;
                            i4 = i12;
                            str6 = str13;
                            i5 = length2;
                            try {
                                this.mInstaller.destroyAppData(str, name2, i, 1, 0L);
                            } catch (Installer.InstallerException e6) {
                                e = e6;
                                PackageManagerServiceUtils.logCriticalInfo(i3, str4 + e);
                                i12 = i4 + 1;
                                listFilesOrEmpty2 = fileArr;
                                length2 = i5;
                                str3 = str5;
                                str2 = str6;
                                computer = computer2;
                            }
                        }
                    } catch (Installer.InstallerException e7) {
                        e = e7;
                        str5 = str14;
                        computer2 = computer4;
                        i4 = i12;
                        str6 = str13;
                        i5 = length2;
                    }
                }
                i12 = i4 + 1;
                listFilesOrEmpty2 = fileArr;
                length2 = i5;
                str3 = str5;
                str2 = str6;
                computer = computer2;
            }
        }
        Slog.i("PackageManager", "reconcileAppsData, will try to execute prepareAppDataAndMigrateLIF");
        Trace.traceBegin(262144L, "prepareAppDataAndMigrate");
        Installer.Batch batch = new Installer.Batch();
        List<? extends PackageStateInternal> volumePackages = computer.getVolumePackages(str);
        IPkgReconcileDelayedExt beforePrepareAppDataInRADL = this.mWrapper.getExtImpl().beforePrepareAppDataInRADL(i2, i, this.mPm.isDeviceUpgrading(), str, z);
        IPkgReconcileSkipExt beforePrepareAppDataInRADL2 = this.mWrapper.getExtImpl().beforePrepareAppDataInRADL2(i2, i, str);
        int i13 = 0;
        for (PackageStateInternal packageStateInternal : volumePackages) {
            String packageName = packageStateInternal.getPackageName();
            if (packageStateInternal.getPkg() == null) {
                Slog.w("PackageManager", "Odd, missing scanned package " + packageName);
            } else if (!this.mWrapper.getExtImpl().skipPrepareAppDataForPkgInRADL(beforePrepareAppDataInRADL2, packageStateInternal)) {
                if (z2 && !packageStateInternal.getPkg().isCoreApp()) {
                    arrayList.add(packageName);
                } else {
                    if (!packageStateInternal.getUserStateOrDefault(i).isInstalled() || this.mWrapper.getExtImpl().delayPrepareAppDataInRADL(beforePrepareAppDataInRADL, packageStateInternal)) {
                        iPkgReconcileSkipExt = beforePrepareAppDataInRADL2;
                        iPkgReconcileDelayedExt = beforePrepareAppDataInRADL;
                        i13 = i13;
                    } else {
                        iPkgReconcileSkipExt = beforePrepareAppDataInRADL2;
                        iPkgReconcileDelayedExt = beforePrepareAppDataInRADL;
                        prepareAppDataAndMigrate(batch, packageStateInternal, packageStateInternal.getPkg(), i, i2, z);
                        i13++;
                    }
                    beforePrepareAppDataInRADL = iPkgReconcileDelayedExt;
                    beforePrepareAppDataInRADL2 = iPkgReconcileSkipExt;
                }
            }
        }
        executeBatchLI(batch);
        this.mWrapper.getExtImpl().afterExecuteBatchInReconcileAppsDataLI0(beforePrepareAppDataInRADL);
        this.mWrapper.getExtImpl().afterExecuteBatchInReconcileAppsDataLI(str, i, i2);
        Trace.traceEnd(262144L);
        Slog.v("PackageManager", "reconcileAppsData finished " + i13 + " packages");
        Slog.i("PackageManager", "reconcileAppsData finished ");
        this.mWrapper.getExtImpl().onEndInReconcileAppsDataLI(i2);
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void assertPackageStorageValid(Computer computer, String str, String str2, int i) throws PackageManagerException {
        PackageStateInternal packageStateInternal = computer.getPackageStateInternal(str2);
        if (packageStateInternal == null) {
            throw PackageManagerException.ofInternalError("Package " + str2 + " is unknown", -7);
        }
        if (!TextUtils.equals(str, packageStateInternal.getVolumeUuid())) {
            throw PackageManagerException.ofInternalError("Package " + str2 + " found on unknown volume " + str + "; expected volume " + packageStateInternal.getVolumeUuid(), -8);
        }
        if (!packageStateInternal.getUserStateOrDefault(i).isInstalled()) {
            throw PackageManagerException.ofInternalError("Package " + str2 + " not installed for user " + i, -9);
        }
        if (packageStateInternal.getPkg() == null || shouldHaveAppStorage(packageStateInternal.getPkg())) {
            return;
        }
        throw PackageManagerException.ofInternalError("Package " + str2 + " shouldn't have storage", -10);
    }

    public Future<?> fixAppsDataOnBoot() {
        final int i = StorageManager.isFileEncrypted() ? 1 : 3;
        this.mWrapper.getExtImpl().beforeReconcileAppsDataInConstructor();
        final List<String> reconcileAppsDataLI = reconcileAppsDataLI(StorageManager.UUID_PRIVATE_INTERNAL, 0, i, true, true);
        return SystemServerInitThreadPool.submit(new Runnable() { // from class: com.android.server.pm.AppDataHelper$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                AppDataHelper.this.lambda$fixAppsDataOnBoot$3(reconcileAppsDataLI, i);
            }
        }, "prepareAppData");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fixAppsDataOnBoot$3(List list, int i) {
        TimingsTraceLog timingsTraceLog = new TimingsTraceLog("SystemServerTimingAsync", 262144L);
        timingsTraceLog.traceBegin("AppDataFixup");
        try {
            this.mInstaller.fixupAppData(StorageManager.UUID_PRIVATE_INTERNAL, 3);
        } catch (Installer.InstallerException e) {
            Slog.w("PackageManager", "Trouble fixing GIDs", e);
        }
        timingsTraceLog.traceEnd();
        timingsTraceLog.traceBegin("AppDataPrepare");
        if (list == null || list.isEmpty()) {
            this.mWrapper.getExtImpl().onPrepareAppDataFutureEndByNoDefer();
            return;
        }
        Installer.Batch batch = new Installer.Batch();
        Iterator it = list.iterator();
        int i2 = 0;
        while (it.hasNext()) {
            PackageStateInternal packageStateInternal = this.mPm.snapshotComputer().getPackageStateInternal((String) it.next());
            if (packageStateInternal != null && packageStateInternal.getUserStateOrDefault(0).isInstalled()) {
                prepareAppDataAndMigrate(batch, packageStateInternal, packageStateInternal.getPkg(), 0, i, true);
                i2++;
            }
        }
        synchronized (this.mPm.mInstallLock) {
            executeBatchLI(batch);
        }
        this.mWrapper.getExtImpl().onPrepareAppDataFutureEndByDeferDone(i);
        timingsTraceLog.traceEnd();
        Slog.i("PackageManager", "Deferred reconcileAppsData finished " + i2 + " packages");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearAppDataLIF(AndroidPackage androidPackage, int i, int i2) {
        if (androidPackage == null) {
            return;
        }
        clearAppDataLeafLIF(androidPackage, i, i2);
        if ((131072 & i2) == 0) {
            clearAppProfilesLIF(androidPackage);
        }
    }

    private void clearAppDataLeafLIF(AndroidPackage androidPackage, int i, int i2) {
        PackageStateInternal packageStateInternal = this.mPm.snapshotComputer().getPackageStateInternal(androidPackage.getPackageName());
        for (int i3 : this.mPm.resolveUserIds(i)) {
            try {
                this.mInstaller.clearAppData(androidPackage.getVolumeUuid(), androidPackage.getPackageName(), i3, i2, packageStateInternal != null ? packageStateInternal.getUserStateOrDefault(i3).getCeDataInode() : 0L);
            } catch (Installer.InstallerException e) {
                Slog.w("PackageManager", String.valueOf(e));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearAppProfilesLIF(AndroidPackage androidPackage) {
        if (androidPackage == null) {
            Slog.wtf("PackageManager", "Package was null!", new Throwable());
        } else {
            if (DexOptHelper.useArtService()) {
                destroyAppProfilesWithArtService(androidPackage);
                return;
            }
            try {
                this.mArtManagerService.clearAppProfiles(androidPackage);
            } catch (Installer.LegacyDexoptDisabledException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void destroyAppDataLIF(AndroidPackage androidPackage, int i, int i2) {
        if (androidPackage == null) {
            Slog.wtf("PackageManager", "Package was null!", new Throwable());
        } else {
            destroyAppDataLeafLIF(androidPackage, i, i2);
        }
    }

    public void destroyAppDataLeafLIF(AndroidPackage androidPackage, int i, int i2) {
        PackageStateInternal packageStateInternal = this.mPm.snapshotComputer().getPackageStateInternal(androidPackage.getPackageName());
        for (int i3 : this.mPm.resolveUserIds(i)) {
            if (!this.mWrapper.getExtImpl().skipDestroyAppDataInDestroyAppDataLeafLIF(androidPackage, i3, i2)) {
                long ceDataInode = packageStateInternal != null ? packageStateInternal.getUserStateOrDefault(i3).getCeDataInode() : 0L;
                if (!this.mPm.mPackageManagerServiceExt.skipDestroyAppDataInDestroyAppDataLeafLIF2(androidPackage, i3, i2, ceDataInode)) {
                    try {
                        this.mInstaller.destroyAppData(androidPackage.getVolumeUuid(), androidPackage.getPackageName(), i3, i2, ceDataInode);
                    } catch (Installer.InstallerException e) {
                        Slog.w("PackageManager", String.valueOf(e));
                    }
                    this.mPm.getDexManager().notifyPackageDataDestroyed(androidPackage.getPackageName(), i);
                    this.mPm.getDynamicCodeLogger().notifyPackageDataDestroyed(androidPackage.getPackageName(), i);
                }
            }
        }
    }

    public void destroyAppProfilesLIF(AndroidPackage androidPackage) {
        if (androidPackage == null) {
            Slog.wtf("PackageManager", "Package was null!", new Throwable());
        } else {
            destroyAppProfilesLeafLIF(androidPackage);
        }
    }

    private void destroyAppProfilesLeafLIF(AndroidPackage androidPackage) {
        if (DexOptHelper.useArtService()) {
            destroyAppProfilesWithArtService(androidPackage);
            return;
        }
        try {
            this.mInstaller.destroyAppProfiles(androidPackage.getPackageName());
        } catch (Installer.InstallerException e) {
            Slog.w("PackageManager", String.valueOf(e));
        } catch (Installer.LegacyDexoptDisabledException e2) {
            throw new RuntimeException(e2);
        }
    }

    private void destroyAppProfilesWithArtService(AndroidPackage androidPackage) {
        if (DexOptHelper.artManagerLocalIsInitialized()) {
            PackageManagerLocal.FilteredSnapshot withFilteredSnapshot = PackageManagerServiceUtils.getPackageManagerLocal().withFilteredSnapshot();
            try {
                try {
                    DexOptHelper.getArtManagerLocal().clearAppProfiles(withFilteredSnapshot, androidPackage.getPackageName());
                } catch (IllegalArgumentException e) {
                    Slog.w("PackageManager", e);
                }
                if (withFilteredSnapshot != null) {
                    withFilteredSnapshot.close();
                }
            } catch (Throwable th) {
                if (withFilteredSnapshot != null) {
                    try {
                        withFilteredSnapshot.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldHaveAppStorage(AndroidPackage androidPackage) {
        PackageManager.Property property = androidPackage.getProperties().get("android.internal.PROPERTY_NO_APP_DATA_STORAGE");
        return (property == null || !property.getBoolean()) && androidPackage.getUid() >= 0;
    }

    public void clearKeystoreData(int i, int i2) {
        if (i2 < 0) {
            return;
        }
        int length = this.mPm.resolveUserIds(i).length;
        for (int i3 = 0; i3 < length; i3++) {
            AndroidKeyStoreMaintenance.clearNamespace(0, UserHandle.getUid(r4[i3], i2));
        }
    }

    public IAppDataHelperWrapper getWrapper() {
        return this.mWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class AppDataHelperWrapper implements IAppDataHelperWrapper {
        private AppDataHelperWrapper() {
        }

        @Override // com.android.server.pm.IAppDataHelperWrapper
        public IAppDataHelperExt getExtImpl() {
            return AppDataHelper.this.mAppDataHelperExt;
        }

        @Override // com.android.server.pm.IAppDataHelperWrapper
        public void prepareAppDataAndMigrate(Installer.Batch batch, PackageState packageState, AndroidPackage androidPackage, int i, int i2, boolean z) {
            AppDataHelper.this.prepareAppDataAndMigrate(batch, packageState, androidPackage, i, i2, z);
        }

        @Override // com.android.server.pm.IAppDataHelperWrapper
        public void assertPackageStorageValid(Computer computer, String str, String str2, int i) throws PackageManagerException {
            AppDataHelper.this.assertPackageStorageValid(computer, str, str2, i);
        }

        @Override // com.android.server.pm.IAppDataHelperWrapper
        public void executeBatchLI(Installer.Batch batch) {
            AppDataHelper.this.executeBatchLI(batch);
        }

        @Override // com.android.server.pm.IAppDataHelperWrapper
        public boolean shouldHaveAppStorage(AndroidPackage androidPackage) {
            return AppDataHelper.this.shouldHaveAppStorage(androidPackage);
        }

        @Override // com.android.server.pm.IAppDataHelperWrapper
        public void prepareAppDataContentsLeafLIF(AndroidPackage androidPackage, PackageStateInternal packageStateInternal, int i, int i2) {
            AppDataHelper.this.prepareAppDataContentsLeafLIF(androidPackage, packageStateInternal, i, i2);
        }
    }
}
