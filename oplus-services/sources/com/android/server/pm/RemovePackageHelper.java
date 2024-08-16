package com.android.server.pm;

import android.content.pm.parsing.ApkLiteParseUtils;
import android.content.pm.parsing.PackageLite;
import android.content.pm.parsing.result.ParseResult;
import android.content.pm.parsing.result.ParseTypeImpl;
import android.os.Environment;
import android.os.Trace;
import android.os.incremental.IncrementalManager;
import android.util.Log;
import android.util.Slog;
import android.util.SparseBooleanArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.ArrayUtils;
import com.android.server.pm.Installer;
import com.android.server.pm.parsing.PackageCacher;
import com.android.server.pm.parsing.pkg.AndroidPackageInternal;
import com.android.server.pm.parsing.pkg.AndroidPackageUtils;
import com.android.server.pm.parsing.pkg.PackageImpl;
import com.android.server.pm.permission.PermissionManagerServiceInternal;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageStateInternal;
import com.android.server.pm.pkg.component.ParsedInstrumentation;
import java.io.File;
import java.util.Collections;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class RemovePackageHelper {
    private final AppDataHelper mAppDataHelper;
    private final IncrementalManager mIncrementalManager;
    private final Installer mInstaller;
    private final PermissionManagerServiceInternal mPermissionManager;
    private final PackageManagerService mPm;
    private final SharedLibrariesImpl mSharedLibraries;
    private final UserManagerInternal mUserManagerInternal;
    private final RemovePackageHelplerWrapper mWrapper;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RemovePackageHelper(PackageManagerService packageManagerService, AppDataHelper appDataHelper) {
        this.mWrapper = new RemovePackageHelplerWrapper();
        this.mPm = packageManagerService;
        this.mIncrementalManager = packageManagerService.mInjector.getIncrementalManager();
        this.mInstaller = packageManagerService.mInjector.getInstaller();
        this.mUserManagerInternal = packageManagerService.mInjector.getUserManagerInternal();
        this.mPermissionManager = packageManagerService.mInjector.getPermissionManagerServiceInternal();
        this.mSharedLibraries = packageManagerService.mInjector.getSharedLibrariesImpl();
        this.mAppDataHelper = appDataHelper;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RemovePackageHelper(PackageManagerService packageManagerService) {
        this(packageManagerService, new AppDataHelper(packageManagerService));
    }

    public void removeCodePath(File file) {
        synchronized (this.mPm.mInstallLock) {
            removeCodePathLI(file);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mPm.mInstallLock"})
    public void removeCodePathLI(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File parentFile = file.getParentFile();
            boolean startsWith = parentFile.getName().startsWith("~~");
            try {
                if (this.mIncrementalManager != null && IncrementalManager.isIncrementalPath(file.getAbsolutePath())) {
                    if (startsWith) {
                        this.mIncrementalManager.rmPackageDir(parentFile);
                    } else {
                        this.mIncrementalManager.rmPackageDir(file);
                    }
                }
                String name = file.getName();
                this.mInstaller.rmPackageDir(name, file.getAbsolutePath());
                if (startsWith) {
                    this.mInstaller.rmPackageDir(name, parentFile.getAbsolutePath());
                    removeCachedResult(parentFile);
                    return;
                }
                return;
            } catch (Installer.InstallerException e) {
                Slog.w("PackageManager", "Failed to remove code path", e);
                return;
            }
        }
        file.delete();
    }

    private void removeCachedResult(File file) {
        if (this.mPm.getCacheDir() == null) {
            return;
        }
        new PackageCacher(this.mPm.getCacheDir()).cleanCachedResult(file);
    }

    public void removePackage(AndroidPackage androidPackage, boolean z) {
        synchronized (this.mPm.mInstallLock) {
            removePackageLI(androidPackage, z);
        }
    }

    @GuardedBy({"mPm.mInstallLock"})
    private void removePackageLI(AndroidPackage androidPackage, boolean z) {
        PackageStateInternal packageStateInternal = this.mPm.snapshotComputer().getPackageStateInternal(androidPackage.getPackageName());
        if (packageStateInternal != null) {
            removePackageLI(packageStateInternal.getPackageName(), z);
            return;
        }
        if (PackageManagerService.DEBUG_REMOVE && z) {
            Log.d("PackageManager", "Not removing package " + androidPackage.getPackageName() + "; mExtras == null");
        }
    }

    @GuardedBy({"mPm.mInstallLock"})
    private void removePackageLI(String str, boolean z) {
        if (PackageManagerService.DEBUG_INSTALL && z) {
            Log.d("PackageManager", "Removing package " + str);
        }
        synchronized (this.mPm.mLock) {
            AndroidPackage remove = this.mPm.mPackages.remove(str);
            if (remove != null) {
                cleanPackageDataStructuresLILPw(remove, AndroidPackageUtils.isSystem(remove), z);
            }
        }
    }

    @GuardedBy({"mPm.mLock"})
    private void cleanPackageDataStructuresLILPw(AndroidPackage androidPackage, boolean z, boolean z2) {
        this.mPm.mComponentResolver.removeAllComponents(androidPackage, z2);
        this.mPermissionManager.onPackageRemoved(androidPackage);
        this.mPm.getPackageProperty().removeAllProperties(androidPackage);
        int size = ArrayUtils.size(androidPackage.getInstrumentations());
        StringBuilder sb = null;
        StringBuilder sb2 = null;
        for (int i = 0; i < size; i++) {
            ParsedInstrumentation parsedInstrumentation = androidPackage.getInstrumentations().get(i);
            this.mPm.getInstrumentation().remove(parsedInstrumentation.getComponentName());
            if (PackageManagerService.DEBUG_REMOVE && z2) {
                if (sb2 == null) {
                    sb2 = new StringBuilder(256);
                } else {
                    sb2.append(' ');
                }
                sb2.append(parsedInstrumentation.getName());
            }
        }
        if (sb2 != null && PackageManagerService.DEBUG_REMOVE) {
            Log.d("PackageManager", "  Instrumentation: " + ((Object) sb2));
        }
        if (z) {
            int size2 = androidPackage.getLibraryNames().size();
            StringBuilder sb3 = null;
            for (int i2 = 0; i2 < size2; i2++) {
                String str = androidPackage.getLibraryNames().get(i2);
                if (this.mSharedLibraries.removeSharedLibrary(str, 0L) && PackageManagerService.DEBUG_REMOVE && z2) {
                    if (sb3 == null) {
                        sb3 = new StringBuilder(256);
                    } else {
                        sb3.append(' ');
                    }
                    sb3.append(str);
                }
            }
        }
        if (androidPackage.getSdkLibraryName() != null && this.mSharedLibraries.removeSharedLibrary(androidPackage.getSdkLibraryName(), androidPackage.getSdkLibVersionMajor()) && PackageManagerService.DEBUG_REMOVE && z2) {
            sb = new StringBuilder(256);
            sb.append(androidPackage.getSdkLibraryName());
        }
        if (androidPackage.getStaticSharedLibraryName() != null && this.mSharedLibraries.removeSharedLibrary(androidPackage.getStaticSharedLibraryName(), androidPackage.getStaticSharedLibraryVersion()) && PackageManagerService.DEBUG_REMOVE && z2) {
            if (sb == null) {
                sb = new StringBuilder(256);
            } else {
                sb.append(' ');
            }
            sb.append(androidPackage.getStaticSharedLibraryName());
        }
        if (sb == null || !PackageManagerService.DEBUG_REMOVE) {
            return;
        }
        Log.d("PackageManager", "  Libraries: " + ((Object) sb));
    }

    public void removePackageData(PackageSetting packageSetting, int[] iArr, PackageRemovedInfo packageRemovedInfo, int i, boolean z) {
        synchronized (this.mPm.mInstallLock) {
            removePackageDataLIF(packageSetting, iArr, packageRemovedInfo, i, z);
        }
    }

    @GuardedBy({"mPm.mInstallLock"})
    public void removePackageDataLIF(PackageSetting packageSetting, int[] iArr, PackageRemovedInfo packageRemovedInfo, int i, boolean z) {
        final int i2;
        int i3;
        PackageManagerTracedLock packageManagerTracedLock;
        final String packageName = packageSetting.getPackageName();
        if (PackageManagerService.DEBUG_REMOVE) {
            Slog.d("PackageManager", "removePackageDataLI: " + packageSetting);
        }
        AndroidPackageInternal pkg = packageSetting.getPkg();
        boolean z2 = false;
        if (packageRemovedInfo != null) {
            packageRemovedInfo.mRemovedPackage = packageName;
            packageRemovedInfo.mInstallerPackageName = packageSetting.getInstallSource().mInstallerPackageName;
            packageRemovedInfo.mIsStaticSharedLib = (pkg == null || pkg.getStaticSharedLibraryName() == null) ? false : true;
            packageRemovedInfo.populateUsers(packageSetting.queryInstalledUsers(this.mUserManagerInternal.getUserIds(), true), packageSetting);
            packageRemovedInfo.mIsExternal = packageSetting.isExternalStorage();
            packageRemovedInfo.mRemovedPackageVersionCode = packageSetting.getVersionCode();
        }
        removePackageLI(packageSetting.getPackageName(), (i & Integer.MIN_VALUE) != 0);
        int i4 = i & 1;
        if (i4 == 0) {
            AndroidPackage buildFakeForDeletion = pkg != null ? pkg : PackageImpl.buildFakeForDeletion(packageSetting.getPackageName(), packageSetting.getVolumeUuid());
            this.mAppDataHelper.destroyAppDataLIF(buildFakeForDeletion, -1, 7);
            this.mAppDataHelper.destroyAppProfilesLIF(buildFakeForDeletion);
            if (packageRemovedInfo != null) {
                packageRemovedInfo.mDataRemoved = true;
            }
        }
        if (i4 == 0) {
            SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
            PackageManagerTracedLock packageManagerTracedLock2 = this.mPm.mLock;
            synchronized (packageManagerTracedLock2) {
                try {
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    this.mPm.mDomainVerificationManager.clearPackage(packageSetting.getPackageName());
                    this.mPm.mSettings.getKeySetManagerService().removeAppKeySetDataLPw(packageName);
                    this.mPm.mInjector.getUpdateOwnershipHelper().removeUpdateOwnerDenyList(packageName);
                    Computer snapshotComputer = this.mPm.snapshotComputer();
                    this.mPm.mAppsFilter.removePackage(snapshotComputer, snapshotComputer.getPackageStateInternal(packageName));
                    int removePackageLPw = this.mPm.mSettings.removePackageLPw(packageName);
                    if (packageRemovedInfo != null) {
                        packageRemovedInfo.mRemovedAppId = removePackageLPw;
                    }
                    if (this.mPm.mSettings.isDisabledSystemPackageLPr(packageName)) {
                        i3 = removePackageLPw;
                        packageManagerTracedLock = packageManagerTracedLock2;
                    } else {
                        SharedUserSetting sharedUserSettingLPr = this.mPm.mSettings.getSharedUserSettingLPr(packageSetting);
                        i3 = removePackageLPw;
                        packageManagerTracedLock = packageManagerTracedLock2;
                        this.mPermissionManager.onPackageUninstalled(packageName, packageSetting.getAppId(), packageSetting, pkg, sharedUserSettingLPr != null ? sharedUserSettingLPr.getPackages() : Collections.emptyList(), -1);
                        if (sharedUserSettingLPr != null) {
                            this.mPm.mSettings.checkAndConvertSharedUserSettingsLPw(sharedUserSettingLPr);
                        }
                    }
                    this.mPm.clearPackagePreferredActivitiesLPw(packageSetting.getPackageName(), sparseBooleanArray, -1);
                    this.mPm.mSettings.removeRenamedPackageLPw(packageSetting.getRealName());
                    if (sparseBooleanArray.size() > 0) {
                        new PreferredActivityHelper(this.mPm).updateDefaultHomeNotLocked(this.mPm.snapshotComputer(), sparseBooleanArray);
                        this.mPm.postPreferredActivityChangedBroadcast(-1);
                    }
                    i2 = i3;
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            }
            throw th;
        }
        i2 = -1;
        if (packageRemovedInfo != null && packageRemovedInfo.mOrigUsers != null) {
            if (PackageManagerService.DEBUG_REMOVE) {
                Slog.d("PackageManager", "Propagating install state across downgrade");
            }
            boolean z3 = false;
            for (int i5 : iArr) {
                boolean contains = ArrayUtils.contains(packageRemovedInfo.mOrigUsers, i5);
                if (PackageManagerService.DEBUG_REMOVE) {
                    Slog.d("PackageManager", "    user " + i5 + " => " + contains);
                }
                if (contains != packageSetting.getInstalled(i5)) {
                    z3 = true;
                }
                packageSetting.setInstalled(contains, i5);
                if (contains) {
                    packageSetting.setUninstallReason(0, i5);
                }
            }
            z2 = z3;
        }
        synchronized (this.mPm.mLock) {
            if (z) {
                try {
                    this.mPm.writeSettingsLPrTEMP();
                } finally {
                }
            }
            if (z2) {
                this.mPm.mSettings.writeKernelMappingLPr(packageSetting);
            }
        }
        if (i2 != -1) {
            this.mPm.mInjector.getBackgroundHandler().post(new Runnable() { // from class: com.android.server.pm.RemovePackageHelper$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    RemovePackageHelper.this.lambda$removePackageDataLIF$0(i2, packageName);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removePackageDataLIF$0(int i, String str) {
        try {
            Trace.traceBegin(262144L, "clearKeystoreData:" + i);
            if (!this.mPm.mPackageManagerServiceExt.skipRemoveKeyStoreInRPDLIF(str, i)) {
                this.mAppDataHelper.clearKeystoreData(-1, i);
            }
        } finally {
            Trace.traceEnd(262144L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cleanUpResources(File file, String[] strArr) {
        synchronized (this.mPm.mInstallLock) {
            cleanUpResourcesLI(file, strArr);
        }
    }

    @GuardedBy({"mPm.mInstallLock"})
    private void cleanUpResourcesLI(File file, String[] strArr) {
        List<String> list = Collections.EMPTY_LIST;
        if (file != null && file.exists()) {
            ParseResult parsePackageLite = ApkLiteParseUtils.parsePackageLite(ParseTypeImpl.forDefaultParsing().reset(), file, 0);
            if (parsePackageLite.isSuccess()) {
                list = ((PackageLite) parsePackageLite.getResult()).getAllApkPaths();
            }
        }
        removeCodePathLI(file);
        removeDexFilesLI(list, strArr);
    }

    @GuardedBy({"mPm.mInstallLock"})
    private void removeDexFilesLI(List<String> list, String[] strArr) {
        if (list.isEmpty()) {
            return;
        }
        if (strArr == null) {
            throw new IllegalStateException("instructionSet == null");
        }
        if (DexOptHelper.useArtService()) {
            return;
        }
        String[] dexCodeInstructionSets = InstructionSets.getDexCodeInstructionSets(strArr);
        for (String str : list) {
            for (String str2 : dexCodeInstructionSets) {
                try {
                    this.mPm.mInstaller.rmdex(str, str2);
                } catch (Installer.InstallerException unused) {
                } catch (Installer.LegacyDexoptDisabledException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cleanUpForMoveInstall(String str, String str2, String str3) {
        File file = new File(Environment.getDataAppDirectory(str), new File(str3).getName());
        Slog.d("PackageManager", "Cleaning up " + str2 + " on " + str);
        int[] userIds = this.mPm.mUserManager.getUserIds();
        synchronized (this.mPm.mInstallLock) {
            for (int i : userIds) {
                try {
                    this.mPm.mInstaller.destroyAppData(str, str2, i, 131075, 0L);
                } catch (Installer.InstallerException e) {
                    Slog.w("PackageManager", String.valueOf(e));
                }
            }
            removeCodePathLI(file);
        }
    }

    public IRemovePackageHelperWrapper getWrapper() {
        return this.mWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class RemovePackageHelplerWrapper implements IRemovePackageHelperWrapper {
        private RemovePackageHelplerWrapper() {
        }

        @Override // com.android.server.pm.IRemovePackageHelperWrapper
        public void removeCodePathLI(File file) {
            RemovePackageHelper.this.removeCodePathLI(file);
        }
    }
}
