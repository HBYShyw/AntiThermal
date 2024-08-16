package com.android.server.pm;

import android.app.ApplicationPackageManager;
import android.content.Intent;
import android.content.pm.IPackageDeleteObserver2;
import android.content.pm.SharedLibraryInfo;
import android.content.pm.UserInfo;
import android.content.pm.UserProperties;
import android.content.pm.VersionedPackage;
import android.net.Uri;
import android.os.Binder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.Preconditions;
import com.android.server.job.controllers.JobStatus;
import com.android.server.pm.parsing.pkg.AndroidPackageInternal;
import com.android.server.pm.permission.PermissionManagerServiceInternal;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageStateInternal;
import com.android.server.pm.pkg.PackageUserStateInternal;
import com.android.server.wm.ActivityTaskManagerInternal;
import dalvik.system.VMRuntime;
import java.util.Collections;
import java.util.List;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class DeletePackageHelper {
    private static final boolean DEBUG_CLEAN_APKS = false;
    private static final boolean DEBUG_SD_INSTALL = false;
    private final AppDataHelper mAppDataHelper;
    private final PermissionManagerServiceInternal mPermissionManager;
    private final PackageManagerService mPm;
    private final RemovePackageHelper mRemovePackageHelper;
    private final UserManagerInternal mUserManagerInternal;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DeletePackageHelper(PackageManagerService packageManagerService, RemovePackageHelper removePackageHelper, AppDataHelper appDataHelper) {
        this.mPm = packageManagerService;
        this.mUserManagerInternal = packageManagerService.mInjector.getUserManagerInternal();
        this.mPermissionManager = packageManagerService.mInjector.getPermissionManagerServiceInternal();
        this.mRemovePackageHelper = removePackageHelper;
        this.mAppDataHelper = appDataHelper;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DeletePackageHelper(PackageManagerService packageManagerService) {
        this.mPm = packageManagerService;
        AppDataHelper appDataHelper = new AppDataHelper(packageManagerService);
        this.mAppDataHelper = appDataHelper;
        this.mUserManagerInternal = packageManagerService.mInjector.getUserManagerInternal();
        this.mPermissionManager = packageManagerService.mInjector.getPermissionManagerServiceInternal();
        this.mRemovePackageHelper = new RemovePackageHelper(packageManagerService, appDataHelper);
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x00d8, code lost:
    
        if (r1.getUserInfo(r1.getProfileParentId(r29)).isAdmin() == false) goto L36;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int deletePackageX(String str, long j, int i, int i2, boolean z) {
        int[] iArr;
        AndroidPackage androidPackage;
        AndroidPackageInternal androidPackageInternal;
        SparseArray sparseArray;
        int i3;
        PackageSetting packageSetting;
        PackageSetting packageSetting2;
        int i4;
        PackageSetting packageLPr;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        SharedLibraryInfo sharedLibraryInfo;
        PackageRemovedInfo packageRemovedInfo = new PackageRemovedInfo(this.mPm);
        int i5 = (i2 & 2) != 0 ? -1 : i;
        if (this.mPm.isPackageDeviceAdmin(str, i5) && !this.mPm.mPackageManagerServiceExt.allowUninstallDeviceAdminInDeletePackageX(str, i)) {
            Slog.w("PackageManager", "Not removing package " + str + ": has active device admin");
            return -2;
        }
        synchronized (this.mPm.mLock) {
            Computer snapshotComputer = this.mPm.snapshotComputer();
            PackageSetting packageLPr2 = this.mPm.mSettings.getPackageLPr(str);
            if (packageLPr2 == null) {
                Slog.w("PackageManager", "Not removing non-existent package " + str);
                return -1;
            }
            if (j != -1 && packageLPr2.getVersionCode() != j) {
                Slog.w("PackageManager", "Not removing package " + str + " with versionCode " + packageLPr2.getVersionCode() + " != " + j);
                return -1;
            }
            boolean z6 = false;
            boolean z7 = true;
            if (PackageManagerServiceUtils.isUpdatedSystemApp(packageLPr2) && (i2 & 4) == 0) {
                UserInfo userInfo = this.mUserManagerInternal.getUserInfo(i);
                if (userInfo != null) {
                    if (!userInfo.isAdmin()) {
                        UserManagerInternal userManagerInternal = this.mUserManagerInternal;
                    }
                }
                Slog.w("PackageManager", "Not removing package " + str + " as only admin user (or their profile) may downgrade system apps");
                EventLog.writeEvent(1397638484, "170646036", -1, str);
                return -3;
            }
            PackageSetting disabledSystemPkgLPr = this.mPm.mSettings.getDisabledSystemPkgLPr(str);
            AndroidPackage androidPackage2 = this.mPm.mPackages.get(str);
            int[] userIds = this.mUserManagerInternal.getUserIds();
            if (androidPackage2 != null) {
                if (androidPackage2.getStaticSharedLibraryName() != null) {
                    packageSetting = packageLPr2;
                    sharedLibraryInfo = snapshotComputer.getSharedLibraryInfo(androidPackage2.getStaticSharedLibraryName(), androidPackage2.getStaticSharedLibraryVersion());
                } else {
                    packageSetting = packageLPr2;
                    sharedLibraryInfo = androidPackage2.getSdkLibraryName() != null ? snapshotComputer.getSharedLibraryInfo(androidPackage2.getSdkLibraryName(), androidPackage2.getSdkLibVersionMajor()) : null;
                }
                if (sharedLibraryInfo != null) {
                    for (int i6 : userIds) {
                        if (i5 == -1 || i5 == i6) {
                            List<VersionedPackage> packagesUsingSharedLibrary = snapshotComputer.getPackagesUsingSharedLibrary(sharedLibraryInfo, 4202496L, 1000, i6);
                            if (!ArrayUtils.isEmpty(packagesUsingSharedLibrary)) {
                                Slog.w("PackageManager", "Not removing package " + androidPackage2.getManifestPackageName() + " hosting lib " + sharedLibraryInfo.getName() + " version " + sharedLibraryInfo.getLongVersion() + " used by " + packagesUsingSharedLibrary + " for user " + i6);
                                return -6;
                            }
                        }
                    }
                }
                packageLPr2 = packageSetting;
                z7 = true;
            }
            packageRemovedInfo.mOrigUsers = packageLPr2.queryInstalledUsers(userIds, z7);
            if (PackageManagerServiceUtils.isUpdatedSystemApp(packageLPr2) && (i2 & 4) == 0) {
                SparseArray sparseArray2 = new SparseArray();
                int i7 = 0;
                while (i7 < userIds.length) {
                    PackageUserStateInternal readUserState = packageLPr2.readUserState(userIds[i7]);
                    sparseArray2.put(userIds[i7], new TempUserState(readUserState.getEnabledState(), readUserState.getLastDisableAppCaller(), readUserState.isInstalled()));
                    i7++;
                    userIds = userIds;
                    androidPackage2 = androidPackage2;
                }
                iArr = userIds;
                androidPackage = androidPackage2;
                androidPackageInternal = null;
                sparseArray = sparseArray2;
                i3 = -1;
            } else {
                iArr = userIds;
                androidPackage = androidPackage2;
                androidPackageInternal = null;
                sparseArray = null;
                i3 = i5;
            }
            boolean isInstallerPackage = this.mPm.mSettings.isInstallerPackage(str);
            this.mPm.mPackageManagerServiceExt.beforeDeletePackageX(str);
            Object obj = this.mPm.mInstallLock;
            synchronized (obj) {
                try {
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
                try {
                    if (PackageManagerService.DEBUG_REMOVE) {
                        Slog.d("PackageManager", "deletePackageX: pkg=" + str + " user=" + i);
                    }
                    AndroidPackageInternal androidPackageInternal2 = androidPackageInternal;
                    AndroidPackage androidPackage3 = androidPackage;
                    PackageFreezer freezePackageForDelete = this.mPm.freezePackageForDelete(str, i3, i2, "deletePackageX", 13);
                    try {
                        packageSetting = obj;
                        packageSetting2 = packageLPr2;
                    } catch (Throwable th2) {
                        th = th2;
                        packageSetting = obj;
                    }
                    try {
                        boolean deletePackageLIF = deletePackageLIF(str, UserHandle.of(i5), true, iArr, i2 | Integer.MIN_VALUE, packageRemovedInfo, true);
                        if (freezePackageForDelete != null) {
                            freezePackageForDelete.close();
                        }
                        if (deletePackageLIF && androidPackage3 != null) {
                            synchronized (this.mPm.mLock) {
                                z5 = this.mPm.mPackages.get(androidPackage3.getPackageName()) != null;
                            }
                            this.mPm.mInstantAppRegistry.onPackageUninstalled(androidPackage3, packageSetting2, packageRemovedInfo.mRemovedUsers, z5);
                        }
                        synchronized (this.mPm.mLock) {
                            if (deletePackageLIF) {
                                this.mPm.updateSequenceNumberLP(packageSetting2, packageRemovedInfo.mRemovedUsers);
                                this.mPm.updateInstantAppInstallerLocked(str);
                            }
                        }
                        ApplicationPackageManager.invalidateGetPackagesForUidCache();
                        if (deletePackageLIF) {
                            if ((i2 & 8) == 0) {
                                z3 = z;
                                z4 = true;
                            } else {
                                z3 = z;
                                z4 = false;
                            }
                            packageRemovedInfo.sendPackageRemovedBroadcasts(z4, z3);
                            packageRemovedInfo.sendSystemPackageUpdatedBroadcasts();
                            PackageMetrics.onUninstallSucceeded(packageRemovedInfo, i2, i5);
                            if (packageRemovedInfo.mRemovedForAllUsers) {
                                this.mPm.mPackageManagerServiceExt.notifyPackageDeleteForAbiInfo(packageRemovedInfo.mRemovedPackage);
                            }
                        }
                        VMRuntime.getRuntime().requestConcurrentGC();
                        synchronized (this.mPm.mInstallLock) {
                            InstallArgs installArgs = packageRemovedInfo.mArgs;
                            if (installArgs != null) {
                                this.mRemovePackageHelper.cleanUpResources(installArgs.mCodeFile, installArgs.mInstructionSets);
                            }
                            if (sparseArray != null) {
                                synchronized (this.mPm.mLock) {
                                    PackageSetting packageSettingForMutation = this.mPm.getPackageSettingForMutation(str);
                                    if (packageSettingForMutation != null) {
                                        AndroidPackageInternal pkg = packageSettingForMutation.getPkg();
                                        boolean z8 = pkg != null && pkg.isEnabled();
                                        int[] iArr2 = iArr;
                                        int i8 = 0;
                                        while (i8 < iArr2.length) {
                                            TempUserState tempUserState = (TempUserState) sparseArray.get(iArr2[i8]);
                                            int i9 = tempUserState.enabledState;
                                            int[] iArr3 = iArr2;
                                            packageSettingForMutation.setEnabled(i9, iArr2[i8], tempUserState.lastDisableAppCaller);
                                            if (!z6 && tempUserState.installed) {
                                                if (i9 == 0 && z8) {
                                                    z2 = true;
                                                    z6 = z2;
                                                }
                                                z2 = true;
                                                if (i9 != 1) {
                                                }
                                                z6 = z2;
                                            }
                                            i8++;
                                            iArr2 = iArr3;
                                        }
                                        i4 = 1;
                                    } else {
                                        i4 = 1;
                                        Slog.w("PackageManager", "Missing PackageSetting after uninstalling the update for system app: " + str + ". This should not happen.");
                                    }
                                    this.mPm.mSettings.writeAllUsersPackageRestrictionsLPr();
                                }
                            } else {
                                i4 = 1;
                            }
                            AndroidPackageInternal pkg2 = disabledSystemPkgLPr == null ? androidPackageInternal2 : disabledSystemPkgLPr.getPkg();
                            if (pkg2 != null && pkg2.isStub()) {
                                synchronized (this.mPm.mLock) {
                                    packageLPr = this.mPm.mSettings.getPackageLPr(pkg2.getPackageName());
                                }
                                if (packageLPr != null) {
                                    if (z6) {
                                        if (PackageManagerService.DEBUG_COMPRESSION) {
                                            Slog.i("PackageManager", "Enabling system stub after removal; pkg: " + pkg2.getPackageName());
                                        }
                                        new InstallPackageHelper(this.mPm).enableCompressedPackage(pkg2, packageLPr);
                                    } else if (PackageManagerService.DEBUG_COMPRESSION) {
                                        Slog.i("PackageManager", "System stub disabled for all users, leaving uncompressed after removal; pkg: " + pkg2.getPackageName());
                                    }
                                }
                            }
                        }
                        if (deletePackageLIF && isInstallerPackage) {
                            this.mPm.mInjector.getPackageInstallerService().onInstallerPackageDeleted(packageSetting2.getAppId(), i5);
                        }
                        this.mPm.mPackageManagerServiceSocExt.acquireUxPerfLockPkgUninstall(str, i, deletePackageLIF);
                        this.mPm.mPackageManagerServiceExt.afterDeleteSucceededInDeletePackageX(str, i, i5);
                        if (deletePackageLIF) {
                            return i4;
                        }
                        return -1;
                    } catch (Throwable th3) {
                        th = th3;
                        Throwable th4 = th;
                        if (freezePackageForDelete == null) {
                            throw th4;
                        }
                        try {
                            freezePackageForDelete.close();
                            throw th4;
                        } catch (Throwable th5) {
                            th4.addSuppressed(th5);
                            throw th4;
                        }
                    }
                } catch (Throwable th6) {
                    th = th6;
                    packageSetting = obj;
                    throw th;
                }
            }
        }
    }

    @GuardedBy({"mPm.mInstallLock"})
    public boolean deletePackageLIF(String str, UserHandle userHandle, boolean z, int[] iArr, int i, PackageRemovedInfo packageRemovedInfo, boolean z2) {
        DeletePackageAction mayDeletePackageLocked;
        synchronized (this.mPm.mLock) {
            PackageManagerService packageManagerService = this.mPm;
            PackageSetting adjustPackageSettingInDeletePackageLIF = packageManagerService.mPackageManagerServiceExt.adjustPackageSettingInDeletePackageLIF(packageManagerService.mSettings.getPackageLPr(str), this.mPm.mContext);
            PackageSetting disabledSystemPkgLPr = this.mPm.mSettings.getDisabledSystemPkgLPr(adjustPackageSettingInDeletePackageLIF);
            if (adjustPackageSettingInDeletePackageLIF != null) {
                ((IHbtUtilSocExt) ExtLoader.type(IHbtUtilSocExt.class).create()).hbtCheckUninstall(str, InstructionSets.getAppDexInstructionSets(adjustPackageSettingInDeletePackageLIF.getPrimaryCpuAbiLegacy(), adjustPackageSettingInDeletePackageLIF.getSecondaryCpuAbiLegacy()));
            }
            mayDeletePackageLocked = mayDeletePackageLocked(packageRemovedInfo, adjustPackageSettingInDeletePackageLIF, disabledSystemPkgLPr, i, userHandle);
        }
        if (PackageManagerService.DEBUG_REMOVE) {
            Slog.d("PackageManager", "deletePackageLI: " + str + " user " + userHandle);
        }
        if (mayDeletePackageLocked == null) {
            if (PackageManagerService.DEBUG_REMOVE) {
                Slog.d("PackageManager", "deletePackageLI: action was null");
            }
            return false;
        }
        try {
            executeDeletePackageLIF(mayDeletePackageLocked, str, z, iArr, z2);
            return true;
        } catch (SystemDeleteException e) {
            if (PackageManagerService.DEBUG_REMOVE) {
                Slog.d("PackageManager", "deletePackageLI: system deletion failure", e);
            }
            return false;
        }
    }

    public static DeletePackageAction mayDeletePackageLocked(PackageRemovedInfo packageRemovedInfo, PackageSetting packageSetting, PackageSetting packageSetting2, int i, UserHandle userHandle) {
        if (packageSetting == null) {
            return null;
        }
        if (PackageManagerServiceUtils.isSystemApp(packageSetting)) {
            boolean z = true;
            boolean z2 = (i & 4) != 0;
            if (userHandle != null && userHandle.getIdentifier() != -1) {
                z = false;
            }
            if ((!z2 || z) && packageSetting2 == null) {
                Slog.w("PackageManager", "Attempt to delete unknown system package " + packageSetting.getPkg().getPackageName());
                return null;
            }
        }
        return new DeletePackageAction(packageSetting, packageSetting2, packageRemovedInfo, i, userHandle);
    }

    public void executeDeletePackage(DeletePackageAction deletePackageAction, String str, boolean z, int[] iArr, boolean z2) throws SystemDeleteException {
        synchronized (this.mPm.mInstallLock) {
            executeDeletePackageLIF(deletePackageAction, str, z, iArr, z2);
        }
    }

    @GuardedBy({"mPm.mInstallLock"})
    private void executeDeletePackageLIF(DeletePackageAction deletePackageAction, String str, boolean z, int[] iArr, boolean z2) throws SystemDeleteException {
        boolean z3;
        boolean z4;
        PackageSetting packageSetting = deletePackageAction.mDeletingPs;
        PackageRemovedInfo packageRemovedInfo = deletePackageAction.mRemovedInfo;
        UserHandle userHandle = deletePackageAction.mUser;
        int i = deletePackageAction.mFlags;
        boolean isSystemApp = PackageManagerServiceUtils.isSystemApp(packageSetting);
        SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
        for (int i2 : iArr) {
            sparseBooleanArray.put(i2, this.mPm.checkPermission("android.permission.SUSPEND_APPS", str, i2) == 0);
        }
        int identifier = userHandle == null ? -1 : userHandle.getIdentifier();
        if ((isSystemApp && (i & 4) == 0) || identifier == -1) {
            z3 = true;
        } else {
            synchronized (this.mPm.mLock) {
                markPackageUninstalledForUserLPw(packageSetting, userHandle);
                if (!isSystemApp) {
                    boolean shouldKeepUninstalledPackageLPr = this.mPm.shouldKeepUninstalledPackageLPr(str);
                    if (!packageSetting.isAnyInstalled(this.mUserManagerInternal.getUserIds()) && !shouldKeepUninstalledPackageLPr) {
                        if (PackageManagerService.DEBUG_REMOVE) {
                            Slog.d("PackageManager", "Not installed by other users, full delete");
                        }
                        packageSetting.setPkg(null);
                        z3 = true;
                        packageSetting.setInstalled(true, identifier);
                        this.mPm.mSettings.writeKernelMappingLPr(packageSetting);
                        z4 = false;
                    }
                    z3 = true;
                    if (PackageManagerService.DEBUG_REMOVE) {
                        Slog.d("PackageManager", "Still installed by other users");
                    }
                } else {
                    z3 = true;
                    if (PackageManagerService.DEBUG_REMOVE) {
                        Slog.d("PackageManager", "Deleting system app");
                    }
                }
                z4 = z3;
            }
            if (z4) {
                clearPackageStateForUserLIF(packageSetting, identifier, packageRemovedInfo, i);
                this.mPm.scheduleWritePackageRestrictions(userHandle);
                return;
            }
        }
        if (isSystemApp) {
            if (PackageManagerService.DEBUG_REMOVE) {
                Slog.d("PackageManager", "Removing system package: " + packageSetting.getPackageName());
            }
            deleteInstalledSystemPackage(deletePackageAction, iArr, z2);
            new InstallPackageHelper(this.mPm).restoreDisabledSystemPackageLIF(deletePackageAction, iArr, z2);
        } else {
            if (PackageManagerService.DEBUG_REMOVE) {
                Slog.d("PackageManager", "Removing non-system package: " + packageSetting.getPackageName());
            }
            deleteInstalledPackageLIF(packageSetting, z, i, iArr, packageRemovedInfo, z2);
        }
        int[] iArr2 = packageRemovedInfo != null ? packageRemovedInfo.mRemovedUsers : null;
        if (iArr2 == null) {
            iArr2 = this.mPm.resolveUserIds(identifier);
        }
        Computer snapshotComputer = this.mPm.snapshotComputer();
        for (int i3 : iArr2) {
            if (sparseBooleanArray.get(i3)) {
                this.mPm.unsuspendForSuspendingPackage(snapshotComputer, str, i3);
                this.mPm.removeAllDistractingPackageRestrictions(snapshotComputer, i3);
            }
        }
        if (packageRemovedInfo != null) {
            synchronized (this.mPm.mLock) {
                packageRemovedInfo.mRemovedForAllUsers = this.mPm.mPackages.get(packageSetting.getPackageName()) == null ? z3 : false;
            }
        }
    }

    private void clearPackageStateForUserLIF(PackageSetting packageSetting, int i, PackageRemovedInfo packageRemovedInfo, int i2) {
        AndroidPackage androidPackage;
        SharedUserSetting sharedUserSettingLPr;
        int[] iArr;
        synchronized (this.mPm.mLock) {
            androidPackage = this.mPm.mPackages.get(packageSetting.getPackageName());
            sharedUserSettingLPr = this.mPm.mSettings.getSharedUserSettingLPr(packageSetting);
        }
        this.mAppDataHelper.destroyAppProfilesLIF(androidPackage);
        List<AndroidPackage> packages = sharedUserSettingLPr != null ? sharedUserSettingLPr.getPackages() : Collections.emptyList();
        PreferredActivityHelper preferredActivityHelper = new PreferredActivityHelper(this.mPm);
        if (i == -1) {
            iArr = this.mUserManagerInternal.getUserIds();
        } else {
            iArr = new int[]{i};
        }
        int[] iArr2 = iArr;
        boolean z = false;
        for (int i3 : iArr2) {
            if (PackageManagerService.DEBUG_REMOVE) {
                Slog.d("PackageManager", "Updating package:" + packageSetting.getPackageName() + " install state for user:" + i3);
            }
            if ((i2 & 1) == 0) {
                this.mAppDataHelper.destroyAppDataLIF(androidPackage, i3, 7);
            }
            this.mAppDataHelper.clearKeystoreData(i3, packageSetting.getAppId());
            preferredActivityHelper.clearPackagePreferredActivities(packageSetting.getPackageName(), i3);
            this.mPm.mDomainVerificationManager.clearPackageForUser(packageSetting.getPackageName(), i3);
        }
        this.mPermissionManager.onPackageUninstalled(packageSetting.getPackageName(), packageSetting.getAppId(), packageSetting, androidPackage, packages, i);
        if (packageRemovedInfo != null) {
            if ((i2 & 1) == 0) {
                packageRemovedInfo.mDataRemoved = true;
            }
            packageRemovedInfo.mRemovedPackage = packageSetting.getPackageName();
            packageRemovedInfo.mInstallerPackageName = packageSetting.getInstallSource().mInstallerPackageName;
            if (androidPackage != null && androidPackage.getStaticSharedLibraryName() != null) {
                z = true;
            }
            packageRemovedInfo.mIsStaticSharedLib = z;
            packageRemovedInfo.mRemovedAppId = packageSetting.getAppId();
            packageRemovedInfo.mRemovedUsers = iArr2;
            packageRemovedInfo.mBroadcastUsers = iArr2;
            packageRemovedInfo.mIsExternal = packageSetting.isExternalStorage();
            packageRemovedInfo.mRemovedPackageVersionCode = packageSetting.getVersionCode();
        }
    }

    @GuardedBy({"mPm.mInstallLock"})
    private void deleteInstalledPackageLIF(PackageSetting packageSetting, boolean z, int i, int[] iArr, PackageRemovedInfo packageRemovedInfo, boolean z2) {
        synchronized (this.mPm.mLock) {
            if (packageRemovedInfo != null) {
                packageRemovedInfo.mUid = packageSetting.getAppId();
                PackageManagerService packageManagerService = this.mPm;
                packageRemovedInfo.mBroadcastAllowList = packageManagerService.mAppsFilter.getVisibilityAllowList(packageManagerService.snapshotComputer(), packageSetting, iArr, this.mPm.mSettings.getPackagesLocked());
            }
        }
        this.mRemovePackageHelper.removePackageDataLIF(packageSetting, iArr, packageRemovedInfo, i, z2);
        if (!z || packageRemovedInfo == null) {
            return;
        }
        packageRemovedInfo.mArgs = new InstallArgs(packageSetting.getPathString(), InstructionSets.getAppDexInstructionSets(packageSetting.getPrimaryCpuAbiLegacy(), packageSetting.getSecondaryCpuAbiLegacy()));
    }

    @GuardedBy({"mPm.mLock"})
    private void markPackageUninstalledForUserLPw(PackageSetting packageSetting, UserHandle userHandle) {
        int[] userIds;
        PackageSetting packageSetting2 = packageSetting;
        if (userHandle == null || userHandle.getIdentifier() == -1) {
            userIds = this.mUserManagerInternal.getUserIds();
        } else {
            userIds = new int[]{userHandle.getIdentifier()};
        }
        int length = userIds.length;
        int i = 0;
        while (i < length) {
            int i2 = userIds[i];
            if (PackageManagerService.DEBUG_REMOVE) {
                Slog.d("PackageManager", "Marking package:" + packageSetting.getPackageName() + " uninstalled for user:" + i2);
            }
            packageSetting.setUserState(i2, 0L, 0, false, true, true, false, 0, null, false, false, null, null, null, 0, 0, null, null, 0L);
            packageSetting2 = packageSetting;
            this.mPm.mPackageManagerServiceExt.onMarkPackageUninstalledForUser(packageSetting2, i2);
            i++;
            length = length;
            userIds = userIds;
        }
        this.mPm.mSettings.writeKernelMappingLPr(packageSetting2);
    }

    private void deleteInstalledSystemPackage(DeletePackageAction deletePackageAction, int[] iArr, boolean z) {
        int i = deletePackageAction.mFlags;
        PackageSetting packageSetting = deletePackageAction.mDeletingPs;
        PackageRemovedInfo packageRemovedInfo = deletePackageAction.mRemovedInfo;
        boolean z2 = (packageRemovedInfo == null || packageRemovedInfo.mOrigUsers == null) ? false : true;
        AndroidPackageInternal pkg = packageSetting.getPkg();
        PackageSetting packageSetting2 = deletePackageAction.mDisabledPs;
        if (PackageManagerService.DEBUG_REMOVE) {
            Slog.d("PackageManager", "deleteSystemPackageLI: newPs=" + pkg.getPackageName() + " disabledPs=" + packageSetting2);
        }
        Slog.d("PackageManager", "Deleting system pkg from data partition");
        if (PackageManagerService.DEBUG_REMOVE && z2) {
            Slog.d("PackageManager", "Remembering install states:");
            for (int i2 : iArr) {
                Slog.d("PackageManager", "   u=" + i2 + " inst=" + ArrayUtils.contains(packageRemovedInfo.mOrigUsers, i2));
            }
        }
        if (packageRemovedInfo != null) {
            packageRemovedInfo.mIsRemovedPackageSystemUpdate = true;
        }
        int i3 = (packageSetting2.getVersionCode() < packageSetting.getVersionCode() || packageSetting2.getAppId() != packageSetting.getAppId()) ? i & (-2) : i | 1;
        synchronized (this.mPm.mInstallLock) {
            deleteInstalledPackageLIF(packageSetting, true, i3, iArr, packageRemovedInfo, z);
        }
    }

    public void deletePackageVersionedInternal(VersionedPackage versionedPackage, final IPackageDeleteObserver2 iPackageDeleteObserver2, final int i, final int i2, boolean z) {
        final int callingUid = Binder.getCallingUid();
        this.mPm.mContext.enforceCallingOrSelfPermission("android.permission.DELETE_PACKAGES", null);
        Computer snapshotComputer = this.mPm.snapshotComputer();
        final boolean canViewInstantApps = snapshotComputer.canViewInstantApps(callingUid, i);
        Preconditions.checkNotNull(versionedPackage);
        Preconditions.checkNotNull(iPackageDeleteObserver2);
        Preconditions.checkArgumentInRange(versionedPackage.getLongVersionCode(), -1L, JobStatus.NO_LATEST_RUNTIME, "versionCode must be >= -1");
        final String packageName = versionedPackage.getPackageName();
        final long longVersionCode = versionedPackage.getLongVersionCode();
        this.mPm.mPackageManagerServiceExt.onStartInDeletePackageVersionedInternal(packageName);
        if (this.mPm.mProtectedPackages.isPackageDataProtected(i, packageName)) {
            this.mPm.mHandler.post(new Runnable() { // from class: com.android.server.pm.DeletePackageHelper$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    DeletePackageHelper.lambda$deletePackageVersionedInternal$0(packageName, iPackageDeleteObserver2);
                }
            });
            return;
        }
        try {
            if (((ActivityTaskManagerInternal) this.mPm.mInjector.getLocalService(ActivityTaskManagerInternal.class)).isBaseOfLockedTask(packageName)) {
                iPackageDeleteObserver2.onPackageDeleted(packageName, -7, (String) null);
                EventLog.writeEvent(1397638484, "127605586", -1, "");
                return;
            }
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
        final String resolveInternalPackageName = snapshotComputer.resolveInternalPackageName(packageName, longVersionCode);
        int callingUid2 = Binder.getCallingUid();
        if (!isOrphaned(snapshotComputer, resolveInternalPackageName) && !z && !isCallerAllowedToSilentlyUninstall(snapshotComputer, callingUid2, resolveInternalPackageName, i)) {
            this.mPm.mHandler.post(new Runnable() { // from class: com.android.server.pm.DeletePackageHelper$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    DeletePackageHelper.lambda$deletePackageVersionedInternal$1(packageName, iPackageDeleteObserver2);
                }
            });
            return;
        }
        boolean z2 = (i2 & 2) != 0;
        final int[] userIds = z2 ? this.mUserManagerInternal.getUserIds() : new int[]{i};
        if (UserHandle.getUserId(callingUid2) != i || (z2 && userIds.length > 1)) {
            this.mPm.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "deletePackage for user " + i);
        }
        if (this.mPm.isUserRestricted(i, "no_uninstall_apps")) {
            this.mPm.mHandler.post(new Runnable() { // from class: com.android.server.pm.DeletePackageHelper$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    DeletePackageHelper.lambda$deletePackageVersionedInternal$2(iPackageDeleteObserver2, packageName);
                }
            });
            this.mPm.mPackageManagerServiceExt.writeMdmLog("006", "0", packageName);
            return;
        }
        if (!z2 && snapshotComputer.getBlockUninstallForUser(resolveInternalPackageName, i)) {
            this.mPm.mHandler.post(new Runnable() { // from class: com.android.server.pm.DeletePackageHelper$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    DeletePackageHelper.lambda$deletePackageVersionedInternal$3(iPackageDeleteObserver2, packageName);
                }
            });
            this.mPm.mPackageManagerServiceExt.writeMdmLog("006", "0", packageName);
            return;
        }
        PackageManagerService packageManagerService = this.mPm;
        final boolean z3 = z2;
        if (packageManagerService.mPackageManagerServiceExt.interceptDeleteInDeletePackageVersionedInternal(packageManagerService.mContext, packageName, i, callingUid2, packageManagerService.mHandler, iPackageDeleteObserver2, versionedPackage)) {
            return;
        }
        this.mPm.mPackageManagerServiceExt.beforePostDeleteInDeletePackageVersionedInternal(versionedPackage, i, packageName);
        if (PackageManagerService.DEBUG_REMOVE) {
            StringBuilder sb = new StringBuilder();
            sb.append("deletePackageAsUser: pkg=");
            sb.append(resolveInternalPackageName);
            sb.append(" user=");
            sb.append(i);
            sb.append(" deleteAllUsers: ");
            sb.append(z3);
            sb.append(" version=");
            sb.append(longVersionCode == -1 ? "VERSION_CODE_HIGHEST" : Long.valueOf(longVersionCode));
            Slog.d("PackageManager", sb.toString());
        }
        this.mPm.mHandler.post(new Runnable() { // from class: com.android.server.pm.DeletePackageHelper$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                DeletePackageHelper.this.lambda$deletePackageVersionedInternal$4(resolveInternalPackageName, callingUid, canViewInstantApps, z3, longVersionCode, i, i2, userIds, packageName, iPackageDeleteObserver2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$deletePackageVersionedInternal$0(String str, IPackageDeleteObserver2 iPackageDeleteObserver2) {
        try {
            Slog.w("PackageManager", "Attempted to delete protected package: " + str);
            iPackageDeleteObserver2.onPackageDeleted(str, -1, (String) null);
        } catch (RemoteException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$deletePackageVersionedInternal$1(String str, IPackageDeleteObserver2 iPackageDeleteObserver2) {
        try {
            Intent intent = new Intent("android.intent.action.UNINSTALL_PACKAGE");
            intent.setData(Uri.fromParts("package", str, null));
            intent.putExtra("android.content.pm.extra.CALLBACK", iPackageDeleteObserver2.asBinder());
            iPackageDeleteObserver2.onUserActionRequired(intent);
        } catch (RemoteException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$deletePackageVersionedInternal$2(IPackageDeleteObserver2 iPackageDeleteObserver2, String str) {
        try {
            iPackageDeleteObserver2.onPackageDeleted(str, -3, (String) null);
        } catch (RemoteException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$deletePackageVersionedInternal$3(IPackageDeleteObserver2 iPackageDeleteObserver2, String str) {
        try {
            iPackageDeleteObserver2.onPackageDeleted(str, -4, (String) null);
        } catch (RemoteException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deletePackageVersionedInternal$4(String str, int i, boolean z, boolean z2, long j, int i2, int i3, int[] iArr, String str2, IPackageDeleteObserver2 iPackageDeleteObserver2) {
        int i4;
        int i5;
        int i6;
        int[] iArr2;
        String str3;
        UserProperties userProperties;
        int i7;
        int i8;
        String str4;
        Computer snapshotComputer = this.mPm.snapshotComputer();
        PackageStateInternal packageStateInternal = snapshotComputer.getPackageStateInternal(str);
        if (packageStateInternal == null || !packageStateInternal.getUserStateOrDefault(UserHandle.getUserId(i)).isInstantApp() || z) {
            String str5 = ", returnCode ";
            if (!z2) {
                String str6 = ", returnCode ";
                i4 = deletePackageX(str, j, i2, i3, false);
                if (i4 == 1 && packageStateInternal != null) {
                    int[] profileIds = this.mUserManagerInternal.getProfileIds(i2, true);
                    int length = profileIds.length;
                    int i9 = i4;
                    int i10 = 0;
                    while (i10 < length) {
                        int i11 = profileIds[i10];
                        if (i11 != i2 && packageStateInternal.getUserStateOrDefault(i11).isInstalled() && (userProperties = this.mUserManagerInternal.getUserProperties(i11)) != null && userProperties.getDeleteAppWithParent()) {
                            i7 = i10;
                            i8 = length;
                            int deletePackageX = deletePackageX(str, j, i11, i3, false);
                            if (deletePackageX != 1) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("Package delete failed for user ");
                                sb.append(i11);
                                str4 = str6;
                                sb.append(str4);
                                sb.append(deletePackageX);
                                Slog.w("PackageManager", sb.toString());
                                i9 = -8;
                                i10 = i7 + 1;
                                str6 = str4;
                                length = i8;
                            }
                        } else {
                            i7 = i10;
                            i8 = length;
                        }
                        str4 = str6;
                        i10 = i7 + 1;
                        str6 = str4;
                        length = i8;
                    }
                    i4 = i9;
                }
            } else {
                int[] blockUninstallForUsers = getBlockUninstallForUsers(snapshotComputer, str, iArr);
                if (ArrayUtils.isEmpty(blockUninstallForUsers)) {
                    i4 = deletePackageX(str, j, i2, i3, false);
                } else {
                    int i12 = i3 & (-3);
                    int length2 = iArr.length;
                    int i13 = 0;
                    while (i13 < length2) {
                        int i14 = iArr[i13];
                        if (ArrayUtils.contains(blockUninstallForUsers, i14)) {
                            i5 = i13;
                            i6 = length2;
                            iArr2 = blockUninstallForUsers;
                            str3 = str5;
                        } else {
                            i5 = i13;
                            i6 = length2;
                            iArr2 = blockUninstallForUsers;
                            String str7 = str5;
                            int deletePackageX2 = deletePackageX(str, j, i14, i12, false);
                            if (deletePackageX2 != 1) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("Package delete failed for user ");
                                sb2.append(i14);
                                str3 = str7;
                                sb2.append(str3);
                                sb2.append(deletePackageX2);
                                Slog.w("PackageManager", sb2.toString());
                            } else {
                                str3 = str7;
                            }
                        }
                        str5 = str3;
                        length2 = i6;
                        blockUninstallForUsers = iArr2;
                        i13 = i5 + 1;
                    }
                    i4 = -4;
                }
            }
        } else {
            i4 = -1;
        }
        this.mPm.mPackageManagerServiceExt.writeMdmLog("006", i4 == 1 ? "1" : "0", str2);
        try {
            iPackageDeleteObserver2.onPackageDeleted(str2, i4, (String) null);
        } catch (RemoteException unused) {
            Log.i("PackageManager", "Observer no longer exists.");
        }
        this.mPm.schedulePruneUnusedStaticSharedLibraries(true);
        PackageManagerService packageManagerService = this.mPm;
        packageManagerService.mPackageManagerServiceExt.afterDeleteInDeletePackageVersionedInternal(packageStateInternal, str2, packageManagerService.mHandler);
    }

    private boolean isOrphaned(Computer computer, String str) {
        PackageStateInternal packageStateInternal = computer.getPackageStateInternal(str);
        return packageStateInternal != null && packageStateInternal.getInstallSource().mIsOrphaned;
    }

    private boolean isCallerAllowedToSilentlyUninstall(Computer computer, int i, String str, int i2) {
        if (PackageManagerServiceUtils.isRootOrShell(i) || UserHandle.getAppId(i) == 1000 || this.mPm.mPackageManagerServiceExt.customAllowInIsCallerAllowedToSilentlyUninstall(computer, i)) {
            return true;
        }
        int userId = UserHandle.getUserId(i);
        if (i == computer.getPackageUid(computer.getInstallerPackageName(str, i2), 0L, userId)) {
            return true;
        }
        for (String str2 : this.mPm.mRequiredVerifierPackages) {
            if (i == computer.getPackageUid(str2, 0L, userId)) {
                return true;
            }
        }
        String str3 = this.mPm.mRequiredUninstallerPackage;
        if (str3 != null && i == computer.getPackageUid(str3, 0L, userId)) {
            return true;
        }
        String str4 = this.mPm.mStorageManagerPackage;
        return (str4 != null && i == computer.getPackageUid(str4, 0L, userId)) || computer.checkUidPermission("android.permission.MANAGE_PROFILE_AND_DEVICE_OWNERS", i) == 0;
    }

    private int[] getBlockUninstallForUsers(Computer computer, String str, int[] iArr) {
        int[] iArr2 = PackageManagerService.EMPTY_INT_ARRAY;
        for (int i : iArr) {
            if (computer.getBlockUninstallForUser(str, i)) {
                iArr2 = ArrayUtils.appendInt(iArr2, i);
            }
        }
        return iArr2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class TempUserState {
        public final int enabledState;
        public final boolean installed;
        public final String lastDisableAppCaller;

        private TempUserState(int i, String str, boolean z) {
            this.enabledState = i;
            this.lastDisableAppCaller = str;
            this.installed = z;
        }
    }

    @GuardedBy({"mPm.mLock"})
    public void removeUnusedPackagesLPw(UserManagerService userManagerService, final int i) {
        int[] userIds = userManagerService.getUserIds();
        int size = this.mPm.mSettings.getPackagesLocked().size();
        for (int i2 = 0; i2 < size; i2++) {
            PackageSetting valueAt = this.mPm.mSettings.getPackagesLocked().valueAt(i2);
            if (valueAt.getPkg() != null) {
                final String packageName = valueAt.getPkg().getPackageName();
                boolean z = true;
                if ((valueAt.getFlags() & 1) == 0 && TextUtils.isEmpty(valueAt.getPkg().getStaticSharedLibraryName()) && TextUtils.isEmpty(valueAt.getPkg().getSdkLibraryName())) {
                    boolean shouldKeepUninstalledPackageLPr = this.mPm.shouldKeepUninstalledPackageLPr(packageName);
                    if (!shouldKeepUninstalledPackageLPr) {
                        for (int i3 : userIds) {
                            if (i3 != i && valueAt.getInstalled(i3)) {
                                break;
                            }
                        }
                    }
                    z = shouldKeepUninstalledPackageLPr;
                    if (!z) {
                        this.mPm.mHandler.post(new Runnable() { // from class: com.android.server.pm.DeletePackageHelper$$ExternalSyntheticLambda5
                            @Override // java.lang.Runnable
                            public final void run() {
                                DeletePackageHelper.this.lambda$removeUnusedPackagesLPw$5(packageName, i);
                            }
                        });
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeUnusedPackagesLPw$5(String str, int i) {
        deletePackageX(str, -1L, i, 0, true);
    }

    public void deleteExistingPackageAsUser(VersionedPackage versionedPackage, IPackageDeleteObserver2 iPackageDeleteObserver2, int i) {
        int length;
        this.mPm.mContext.enforceCallingOrSelfPermission("android.permission.DELETE_PACKAGES", null);
        Preconditions.checkNotNull(versionedPackage);
        Preconditions.checkNotNull(iPackageDeleteObserver2);
        String packageName = versionedPackage.getPackageName();
        long longVersionCode = versionedPackage.getLongVersionCode();
        synchronized (this.mPm.mLock) {
            PackageSetting packageLPr = this.mPm.mSettings.getPackageLPr(this.mPm.snapshotComputer().resolveInternalPackageName(packageName, longVersionCode));
            length = packageLPr != null ? packageLPr.queryInstalledUsers(this.mUserManagerInternal.getUserIds(), true).length : 0;
        }
        if (length > 1) {
            deletePackageVersionedInternal(versionedPackage, iPackageDeleteObserver2, i, this.mPm.mPackageManagerServiceExt.adjustDeleteFlagInDeleteExistingPackageAsUser(0, versionedPackage), true);
        } else {
            try {
                iPackageDeleteObserver2.onPackageDeleted(packageName, -1, (String) null);
            } catch (RemoteException unused) {
            }
        }
    }
}
