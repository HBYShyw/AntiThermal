package com.android.server.pm;

import android.apex.ApexInfo;
import android.app.AppOpsManager;
import android.app.ApplicationPackageManager;
import android.app.BroadcastOptions;
import android.app.admin.DevicePolicyManagerInternal;
import android.app.backup.IBackupManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInfoLite;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.SharedLibraryInfo;
import android.content.pm.SigningDetails;
import android.content.pm.VerifierInfo;
import android.content.pm.dex.DexMetadataHelper;
import android.content.pm.parsing.ApkLiteParseUtils;
import android.content.pm.parsing.result.ParseInput;
import android.content.pm.parsing.result.ParseResult;
import android.content.pm.parsing.result.ParseTypeImpl;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.RemoteException;
import android.os.SELinux;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.incremental.IncrementalManager;
import android.os.incremental.IncrementalStorage;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.system.ErrnoException;
import android.system.Os;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.EventLog;
import android.util.Log;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseIntArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.content.F2fsUtils;
import com.android.internal.security.VerityUtils;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.CollectionUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.LocalManagerRegistry;
import com.android.server.SystemConfig;
import com.android.server.job.controllers.JobStatus;
import com.android.server.pm.ApexManager;
import com.android.server.pm.Installer;
import com.android.server.pm.PackageAbiHelper;
import com.android.server.pm.PackageManagerLocal;
import com.android.server.pm.ParallelPackageParser;
import com.android.server.pm.dex.ArtManagerService;
import com.android.server.pm.dex.DexManager;
import com.android.server.pm.dex.DexoptOptions;
import com.android.server.pm.dex.ViewCompiler;
import com.android.server.pm.parsing.PackageCacher;
import com.android.server.pm.parsing.PackageParser2;
import com.android.server.pm.parsing.pkg.AndroidPackageInternal;
import com.android.server.pm.parsing.pkg.AndroidPackageUtils;
import com.android.server.pm.parsing.pkg.ParsedPackage;
import com.android.server.pm.permission.Permission;
import com.android.server.pm.permission.PermissionManagerServiceInternal;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageStateInternal;
import com.android.server.pm.pkg.SharedLibraryWrapper;
import com.android.server.pm.pkg.component.ComponentMutateUtils;
import com.android.server.pm.pkg.component.ParsedActivity;
import com.android.server.pm.pkg.component.ParsedInstrumentation;
import com.android.server.pm.pkg.component.ParsedIntentInfo;
import com.android.server.pm.pkg.component.ParsedPermission;
import com.android.server.pm.pkg.component.ParsedPermissionGroup;
import com.android.server.pm.pkg.parsing.ParsingPackageUtils;
import com.android.server.rollback.RollbackManagerInternal;
import com.android.server.security.FileIntegrityService;
import com.android.server.usb.descriptors.UsbTerminalTypes;
import com.android.server.utils.WatchedArrayMap;
import com.android.server.utils.WatchedLongSparseArray;
import dalvik.system.VMRuntime;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class InstallPackageHelper {
    private final ApexManager mApexManager;
    private final AppDataHelper mAppDataHelper;
    private final ArtManagerService mArtManagerService;
    private final BroadcastHelper mBroadcastHelper;
    private final Context mContext;
    private final DexManager mDexManager;
    private final IncrementalManager mIncrementalManager;
    private final PackageManagerServiceInjector mInjector;
    private final PackageAbiHelper mPackageAbiHelper;
    private final PackageDexOptimizer mPackageDexOptimizer;
    private final PackageManagerService mPm;
    private final RemovePackageHelper mRemovePackageHelper;
    private final SharedLibrariesImpl mSharedLibraries;
    private final UpdateOwnershipHelper mUpdateOwnershipHelper;
    private final ViewCompiler mViewCompiler;
    private final IInstallPackageHelperWrapper mWrapper;

    /* JADX INFO: Access modifiers changed from: package-private */
    public InstallPackageHelper(PackageManagerService packageManagerService, AppDataHelper appDataHelper) {
        this.mWrapper = new InstallPackageHelperWrapper();
        this.mPm = packageManagerService;
        this.mInjector = packageManagerService.mInjector;
        this.mAppDataHelper = appDataHelper;
        this.mBroadcastHelper = new BroadcastHelper(packageManagerService.mInjector);
        this.mRemovePackageHelper = new RemovePackageHelper(packageManagerService);
        this.mIncrementalManager = packageManagerService.mInjector.getIncrementalManager();
        this.mApexManager = packageManagerService.mInjector.getApexManager();
        this.mDexManager = packageManagerService.mInjector.getDexManager();
        this.mArtManagerService = packageManagerService.mInjector.getArtManagerService();
        this.mContext = packageManagerService.mInjector.getContext();
        this.mPackageDexOptimizer = packageManagerService.mInjector.getPackageDexOptimizer();
        this.mPackageAbiHelper = packageManagerService.mInjector.getAbiHelper();
        this.mViewCompiler = packageManagerService.mInjector.getViewCompiler();
        this.mSharedLibraries = packageManagerService.mInjector.getSharedLibrariesImpl();
        this.mUpdateOwnershipHelper = packageManagerService.mInjector.getUpdateOwnershipHelper();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InstallPackageHelper(PackageManagerService packageManagerService) {
        this(packageManagerService, new AppDataHelper(packageManagerService));
    }

    /* JADX WARN: Type inference failed for: r11v14, types: [boolean] */
    @GuardedBy({"mPm.mLock"})
    public AndroidPackage commitReconciledScanResultLocked(ReconciledPackage reconciledPackage, int[] iArr) {
        final PackageSetting packageSetting;
        SharedUserSetting sharedUserSetting;
        int i;
        int userId;
        InstallRequest installRequest = reconciledPackage.mInstallRequest;
        ParsedPackage parsedPackage = installRequest.getParsedPackage();
        if (parsedPackage != null && PackageManagerService.PLATFORM_PACKAGE_NAME.equals(parsedPackage.getPackageName())) {
            parsedPackage.setVersionCode(this.mPm.getSdkVersion()).setVersionCodeMajor(0);
        }
        int scanFlags = installRequest.getScanFlags();
        PackageSetting scanRequestOldPackageSetting = installRequest.getScanRequestOldPackageSetting();
        PackageSetting scanRequestOriginalPackageSetting = installRequest.getScanRequestOriginalPackageSetting();
        String realPackageName = installRequest.getRealPackageName();
        List<String> changedAbiCodePath = DexOptHelper.useArtService() ? null : installRequest.getChangedAbiCodePath();
        if (installRequest.getScanRequestPackageSetting() != null) {
            SharedUserSetting sharedUserSettingLPr = this.mPm.mSettings.getSharedUserSettingLPr(installRequest.getScanRequestPackageSetting());
            SharedUserSetting sharedUserSettingLPr2 = this.mPm.mSettings.getSharedUserSettingLPr(installRequest.getScannedPackageSetting());
            if (sharedUserSettingLPr != null && sharedUserSettingLPr != sharedUserSettingLPr2) {
                sharedUserSettingLPr.removePackage(installRequest.getScanRequestPackageSetting());
                if (this.mPm.mSettings.checkAndPruneSharedUserLPw(sharedUserSettingLPr, false)) {
                    installRequest.setRemovedAppId(sharedUserSettingLPr.mAppId);
                }
            }
        }
        if (installRequest.isExistingSettingCopied()) {
            packageSetting = installRequest.getScanRequestPackageSetting();
            packageSetting.updateFrom(installRequest.getScannedPackageSetting());
        } else {
            PackageSetting scannedPackageSetting = installRequest.getScannedPackageSetting();
            if (scanRequestOriginalPackageSetting != null) {
                this.mPm.mSettings.addRenamedPackageLPw(AndroidPackageUtils.getRealPackageOrNull(parsedPackage, scannedPackageSetting.isSystem()), scanRequestOriginalPackageSetting.getPackageName());
                this.mPm.mTransferredPackages.add(scanRequestOriginalPackageSetting.getPackageName());
            } else {
                this.mPm.mSettings.removeRenamedPackageLPw(parsedPackage.getPackageName());
            }
            packageSetting = scannedPackageSetting;
        }
        SharedUserSetting sharedUserSettingLPr3 = this.mPm.mSettings.getSharedUserSettingLPr(packageSetting);
        if (sharedUserSettingLPr3 != null) {
            sharedUserSettingLPr3.addPackage(packageSetting);
            if (parsedPackage.isLeavingSharedUser() && SharedUidMigration.applyStrategy(2) && sharedUserSettingLPr3.isSingleUser()) {
                this.mPm.mSettings.convertSharedUserSettingsLPw(sharedUserSettingLPr3);
            }
        }
        if (installRequest.isForceQueryableOverride()) {
            packageSetting.setForceQueryableOverride(true);
        }
        InstallSource installSource = installRequest.getInstallSource();
        boolean z = (67108864 & scanFlags) != 0;
        boolean z2 = scanRequestOldPackageSetting != null;
        String str = z2 ? scanRequestOldPackageSetting.getInstallSource().mUpdateOwnerPackageName : null;
        String systemAppUpdateOwnerPackageName = (z || !packageSetting.isSystem()) ? null : this.mPm.mInjector.getSystemConfig().getSystemAppUpdateOwnerPackageName(parsedPackage.getPackageName());
        List<String> list = changedAbiCodePath;
        boolean isUpdateOwnershipDenylisted = this.mUpdateOwnershipHelper.isUpdateOwnershipDenylisted(parsedPackage.getPackageName());
        boolean z3 = str != null;
        if (installSource != null) {
            if (PackageManagerServiceUtils.isInstalledByAdb(installSource.mInitiatingPackageName)) {
                sharedUserSetting = sharedUserSettingLPr3;
            } else {
                sharedUserSetting = sharedUserSettingLPr3;
                PackageSetting packageLPr = this.mPm.mSettings.getPackageLPr(installSource.mInitiatingPackageName);
                if (packageLPr != null) {
                    installSource = installSource.setInitiatingPackageSignatures(packageLPr.getSignatures());
                }
            }
            if (!z) {
                int i2 = installSource.mInstallerPackageUid;
                if (i2 != -1) {
                    userId = UserHandle.getUserId(i2);
                } else {
                    userId = installRequest.getUserId();
                }
                boolean z4 = z2 && (userId < 0 ? scanRequestOldPackageSetting.getNotInstalledUserIds().length <= UserManager.isHeadlessSystemUserMode() : scanRequestOldPackageSetting.getInstalled(userId));
                boolean z5 = (installRequest.getInstallFlags() & DumpState.DUMP_APEX) != 0;
                boolean equals = TextUtils.equals(str, installSource.mInstallerPackageName);
                boolean isUpdateOwnershipDenyListProvider = this.mUpdateOwnershipHelper.isUpdateOwnershipDenyListProvider(installSource.mUpdateOwnerPackageName);
                if (z4) {
                    if (!equals || !z3) {
                        installSource = installSource.setUpdateOwnerPackageName(null);
                    }
                } else if (!z5 || isUpdateOwnershipDenylisted || isUpdateOwnershipDenyListProvider) {
                    installSource = installSource.setUpdateOwnerPackageName(null);
                } else if ((!z3 && z2) || (z3 && !equals)) {
                    installSource = installSource.setUpdateOwnerPackageName(null);
                }
            }
            packageSetting.setInstallSource(installSource);
            ((IHbtUtilSocExt) ExtLoader.type(IHbtUtilSocExt.class).create()).hbtCheckInstall(packageSetting, scanRequestOldPackageSetting, z2);
        } else {
            sharedUserSetting = sharedUserSettingLPr3;
            if (packageSetting.isSystem()) {
                boolean z6 = z3 && TextUtils.equals(str, systemAppUpdateOwnerPackageName);
                if (!z2 || z6) {
                    packageSetting.setUpdateOwnerPackage(systemAppUpdateOwnerPackageName);
                } else {
                    packageSetting.setUpdateOwnerPackage(null);
                }
            }
        }
        if ((8388608 & scanFlags) != 0) {
            boolean z7 = (scanFlags & DumpState.DUMP_APEX) != 0;
            i = 1;
            packageSetting.getPkgState().setApkInUpdatedApex(!z7);
        } else {
            i = 1;
        }
        packageSetting.getPkgState().setApexModuleName(installRequest.getApexModuleName());
        parsedPackage.setUid(packageSetting.getAppId());
        AndroidPackageInternal hideAsFinal = parsedPackage.hideAsFinal();
        this.mPm.mSettings.writeUserRestrictionsLPw(packageSetting, scanRequestOldPackageSetting);
        if (realPackageName != null) {
            this.mPm.mTransferredPackages.add(hideAsFinal.getPackageName());
        }
        if (reconciledPackage.mCollectedSharedLibraryInfos != null || (scanRequestOldPackageSetting != null && !scanRequestOldPackageSetting.getSharedLibraryDependencies().isEmpty())) {
            this.mSharedLibraries.executeSharedLibrariesUpdate(hideAsFinal, packageSetting, null, null, reconciledPackage.mCollectedSharedLibraryInfos, iArr);
        }
        KeySetManagerService keySetManagerService = this.mPm.mSettings.getKeySetManagerService();
        if (reconciledPackage.mRemoveAppKeySetData) {
            keySetManagerService.removeAppKeySetDataLPw(hideAsFinal.getPackageName());
        }
        if (reconciledPackage.mSharedUserSignaturesChanged) {
            SharedUserSetting sharedUserSetting2 = sharedUserSetting;
            sharedUserSetting2.signaturesChanged = Boolean.TRUE;
            sharedUserSetting2.signatures.mSigningDetails = reconciledPackage.mSigningDetails;
        }
        packageSetting.setSigningDetails(reconciledPackage.mSigningDetails);
        if (list != null && list.size() > 0) {
            int size = list.size() - i;
            while (size >= 0) {
                List<String> list2 = list;
                String str2 = list2.get(size);
                try {
                    synchronized (this.mPm.mInstallLock) {
                        this.mPm.mInstaller.rmdex(str2, InstructionSets.getDexCodeInstructionSet(InstructionSets.getPreferredInstructionSet()));
                    }
                } catch (Installer.InstallerException unused) {
                    continue;
                } catch (Installer.LegacyDexoptDisabledException e) {
                    throw new RuntimeException(e);
                }
                size--;
                list = list2;
            }
        }
        int userId2 = installRequest.getUserId();
        commitPackageSettings(hideAsFinal, packageSetting, scanRequestOldPackageSetting, reconciledPackage);
        if (packageSetting.getInstantApp(userId2)) {
            this.mPm.mInstantAppRegistry.addInstantApp(userId2, packageSetting.getAppId());
        }
        if (!IncrementalManager.isIncrementalPath(packageSetting.getPathString())) {
            packageSetting.setLoadingProgress(1.0f);
        }
        if (UpdateOwnershipHelper.hasValidOwnershipDenyList(packageSetting)) {
            this.mPm.mHandler.post(new Runnable() { // from class: com.android.server.pm.InstallPackageHelper$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    InstallPackageHelper.this.lambda$commitReconciledScanResultLocked$0(packageSetting);
                }
            });
        }
        return hideAsFinal;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: handleUpdateOwnerDenyList, reason: merged with bridge method [inline-methods] */
    public void lambda$commitReconciledScanResultLocked$0(PackageSetting packageSetting) {
        ArraySet<String> readUpdateOwnerDenyList = this.mUpdateOwnershipHelper.readUpdateOwnerDenyList(packageSetting);
        if (readUpdateOwnerDenyList == null || readUpdateOwnerDenyList.isEmpty()) {
            return;
        }
        this.mUpdateOwnershipHelper.addToUpdateOwnerDenyList(packageSetting.getPackageName(), readUpdateOwnerDenyList);
        SystemConfig systemConfig = SystemConfig.getInstance();
        synchronized (this.mPm.mLock) {
            Iterator<String> it = readUpdateOwnerDenyList.iterator();
            while (it.hasNext()) {
                String next = it.next();
                PackageSetting packageLPr = this.mPm.mSettings.getPackageLPr(next);
                if (packageLPr != null && systemConfig.getSystemAppUpdateOwnerPackageName(next) == null) {
                    packageLPr.setUpdateOwnerPackage(null);
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x012d A[Catch: all -> 0x01ac, TryCatch #1 {, blocks: (B:32:0x00bb, B:34:0x00d2, B:35:0x00d7, B:37:0x00dc, B:38:0x00e7, B:41:0x0101, B:43:0x010b, B:46:0x0112, B:47:0x0121, B:49:0x012d, B:52:0x014b, B:53:0x0158, B:56:0x0153, B:55:0x015f, B:60:0x0164, B:62:0x0168, B:63:0x017e, B:65:0x0188, B:66:0x018c, B:74:0x0198, B:76:0x0199, B:79:0x01a4, B:80:0x01a7, B:84:0x011a, B:68:0x018d, B:69:0x0194), top: B:31:0x00bb, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0188 A[Catch: all -> 0x01ac, TryCatch #1 {, blocks: (B:32:0x00bb, B:34:0x00d2, B:35:0x00d7, B:37:0x00dc, B:38:0x00e7, B:41:0x0101, B:43:0x010b, B:46:0x0112, B:47:0x0121, B:49:0x012d, B:52:0x014b, B:53:0x0158, B:56:0x0153, B:55:0x015f, B:60:0x0164, B:62:0x0168, B:63:0x017e, B:65:0x0188, B:66:0x018c, B:74:0x0198, B:76:0x0199, B:79:0x01a4, B:80:0x01a7, B:84:0x011a, B:68:0x018d, B:69:0x0194), top: B:31:0x00bb, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x01a1  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x01a3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void commitPackageSettings(AndroidPackage androidPackage, PackageSetting packageSetting, PackageSetting packageSetting2, ReconciledPackage reconciledPackage) {
        int size;
        StringBuilder sb;
        int i;
        List<String> protectedBroadcasts;
        String packageName = androidPackage.getPackageName();
        InstallRequest installRequest = reconciledPackage.mInstallRequest;
        AndroidPackage scanRequestOldPackage = installRequest.getScanRequestOldPackage();
        int scanFlags = installRequest.getScanFlags();
        boolean z = (installRequest.getParseFlags() & Integer.MIN_VALUE) != 0;
        ComponentName componentName = this.mPm.mCustomResolverComponentName;
        if (componentName != null && componentName.getPackageName().equals(androidPackage.getPackageName())) {
            this.mPm.setUpCustomResolverActivity(androidPackage, packageSetting);
        }
        if (androidPackage.getPackageName().equals(PackageManagerService.PLATFORM_PACKAGE_NAME)) {
            this.mPm.setPlatformPackage(androidPackage, packageSetting);
        }
        boolean z2 = z;
        ArrayList<AndroidPackage> commitSharedLibraryChanges = this.mSharedLibraries.commitSharedLibraryChanges(androidPackage, packageSetting, reconciledPackage.mAllowedSharedLibraryInfos, reconciledPackage.getCombinedAvailablePackages(), scanFlags);
        installRequest.setLibraryConsumers(commitSharedLibraryChanges);
        if ((scanFlags & 16) == 0 && (scanFlags & 1024) == 0 && (scanFlags & 2048) == 0) {
            this.mPm.snapshotComputer().checkPackageFrozen(packageName);
        }
        boolean isInstallReplace = installRequest.isInstallReplace();
        if (commitSharedLibraryChanges != null && (androidPackage.getStaticSharedLibraryName() == null || isInstallReplace)) {
            for (int i2 = 0; i2 < commitSharedLibraryChanges.size(); i2++) {
                AndroidPackage androidPackage2 = commitSharedLibraryChanges.get(i2);
                this.mPm.killApplication(androidPackage2.getPackageName(), androidPackage2.getUid(), "update lib", 12);
            }
        }
        Trace.traceBegin(262144L, "updateSettings");
        synchronized (this.mPm.mLock) {
            this.mPm.mSettings.insertPackageSettingLPw(packageSetting, androidPackage);
            this.mPm.mPackages.put(androidPackage.getPackageName(), androidPackage);
            if ((8388608 & scanFlags) != 0) {
                this.mApexManager.registerApkInApex(androidPackage);
            }
            if ((67108864 & scanFlags) == 0) {
                this.mPm.mSettings.getKeySetManagerService().addScannedPackageLPw(androidPackage);
            }
            Computer snapshotComputer = this.mPm.snapshotComputer();
            PackageManagerService packageManagerService = this.mPm;
            packageManagerService.mComponentResolver.addAllComponents(androidPackage, z2, packageManagerService.mSetupWizardPackage, snapshotComputer);
            this.mPm.mAppsFilter.addPackage(snapshotComputer, packageSetting, isInstallReplace, (scanFlags & 1024) != 0);
            this.mPm.addAllPackageProperties(androidPackage);
            if (packageSetting2 != null && packageSetting2.getPkg() != null) {
                this.mPm.mDomainVerificationManager.migrateState(packageSetting2, packageSetting);
                size = ArrayUtils.size(androidPackage.getInstrumentations());
                sb = null;
                for (i = 0; i < size; i++) {
                    ParsedInstrumentation parsedInstrumentation = androidPackage.getInstrumentations().get(i);
                    ComponentMutateUtils.setPackageName(parsedInstrumentation, androidPackage.getPackageName());
                    this.mPm.addInstrumentation(parsedInstrumentation.getComponentName(), parsedInstrumentation);
                    if (z2) {
                        if (sb == null) {
                            sb = new StringBuilder(256);
                        } else {
                            sb.append(' ');
                        }
                        sb.append(parsedInstrumentation.getName());
                    }
                }
                if (sb != null && PackageManagerService.DEBUG_PACKAGE_SCANNING) {
                    Log.d("PackageManager", "  Instrumentation: " + ((Object) sb));
                }
                protectedBroadcasts = androidPackage.getProtectedBroadcasts();
                if (!protectedBroadcasts.isEmpty()) {
                    synchronized (this.mPm.mProtectedBroadcasts) {
                        this.mPm.mProtectedBroadcasts.addAll(protectedBroadcasts);
                    }
                }
                this.mPm.mPermissionManager.onPackageAdded(packageSetting, (scanFlags & 8192) == 0, scanRequestOldPackage);
            }
            this.mPm.mDomainVerificationManager.addPackage(packageSetting);
            size = ArrayUtils.size(androidPackage.getInstrumentations());
            sb = null;
            while (i < size) {
            }
            if (sb != null) {
                Log.d("PackageManager", "  Instrumentation: " + ((Object) sb));
            }
            protectedBroadcasts = androidPackage.getProtectedBroadcasts();
            if (!protectedBroadcasts.isEmpty()) {
            }
            this.mPm.mPermissionManager.onPackageAdded(packageSetting, (scanFlags & 8192) == 0, scanRequestOldPackage);
        }
        Trace.traceEnd(262144L);
    }

    /* JADX WARN: Removed duplicated region for block: B:104:0x0246  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0196 A[Catch: all -> 0x0264, TRY_ENTER, TryCatch #3 {all -> 0x0264, blocks: (B:22:0x00cf, B:23:0x00d3, B:66:0x0196, B:68:0x01a8, B:71:0x01af, B:73:0x01b6, B:76:0x01be, B:77:0x01c1, B:79:0x01c7, B:81:0x01d2, B:82:0x01dd, B:83:0x01f8, B:91:0x0206, B:92:0x0207, B:93:0x021e, B:97:0x0229, B:103:0x0245, B:113:0x0263, B:25:0x00d4, B:27:0x00e6, B:31:0x00f0, B:33:0x00f6, B:35:0x00fc, B:36:0x0104, B:39:0x0109, B:41:0x0113, B:43:0x0121, B:45:0x0130, B:49:0x0137, B:50:0x013f, B:54:0x0144, B:56:0x014a, B:60:0x0157, B:63:0x018c, B:64:0x0193, B:106:0x0167, B:107:0x0254, B:108:0x025c, B:95:0x021f, B:96:0x0228, B:85:0x01f9, B:86:0x0202), top: B:21:0x00cf }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Pair<Integer, IntentSender> installExistingPackageAsUser(final String str, final int i, int i2, int i3, List<String> list, final IntentSender intentSender) {
        int i4;
        int i5;
        boolean z;
        int i6;
        IntentSender intentSender2;
        if (PackageManagerService.DEBUG_INSTALL) {
            Log.v("PackageManager", "installExistingPackageAsUser package=" + str + " userId=" + i + " installFlags=" + i2 + " installReason=" + i3 + " allowlistedRestrictedPermissions=" + list);
        }
        int callingUid = Binder.getCallingUid();
        if (this.mContext.checkCallingOrSelfPermission("android.permission.INSTALL_PACKAGES") != 0 && this.mContext.checkCallingOrSelfPermission("com.android.permission.INSTALL_EXISTING_PACKAGES") != 0) {
            throw new SecurityException("Neither user " + callingUid + " nor current process has android.permission.INSTALL_PACKAGES.");
        }
        Computer snapshotComputer = this.mPm.snapshotComputer();
        snapshotComputer.enforceCrossUserPermission(callingUid, i, true, true, "installExistingPackage for user " + i);
        if (this.mPm.isUserRestricted(i, "no_install_apps")) {
            return Pair.create(-111, intentSender);
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        boolean z2 = (i2 & 2048) != 0;
        boolean z3 = (i2 & 16384) != 0;
        try {
            synchronized (this.mPm.mLock) {
                Computer snapshotComputer2 = this.mPm.snapshotComputer();
                PackageSetting packageLPr = this.mPm.mSettings.getPackageLPr(str);
                if (packageLPr != null && packageLPr.getPkg() != null) {
                    if (z2 && (packageLPr.isSystem() || packageLPr.isUpdatedSystemApp())) {
                        return Pair.create(-3, intentSender);
                    }
                    if (!snapshotComputer2.canViewInstantApps(callingUid, UserHandle.getUserId(callingUid))) {
                        int[] userIds = this.mPm.mUserManager.getUserIds();
                        int length = userIds.length;
                        int i7 = 0;
                        boolean z4 = false;
                        while (i7 < length) {
                            int i8 = length;
                            z4 = !packageLPr.getInstantApp(userIds[i7]);
                            if (z4) {
                                break;
                            }
                            i7++;
                            length = i8;
                        }
                        if (!z4) {
                            return Pair.create(-3, intentSender);
                        }
                    }
                    if (packageLPr.getInstalled(i) && !this.mPm.mPackageManagerServiceExt.shouldSetInstallSettingInInstallExistingPackageAsUser(packageLPr, str, i)) {
                        if (z3 && packageLPr.getInstantApp(i)) {
                            i5 = 0;
                            i4 = 1;
                            z = true;
                        } else {
                            i5 = 0;
                            i4 = 1;
                            z = false;
                        }
                        ScanPackageUtils.setInstantAppForUser(this.mPm.mInjector, packageLPr, i, z2, z3);
                        if (z) {
                            i6 = i4;
                            intentSender2 = intentSender;
                        } else {
                            String str2 = packageLPr.getInstallSource().mUpdateOwnerPackageName;
                            DevicePolicyManagerInternal devicePolicyManagerInternal = (DevicePolicyManagerInternal) this.mInjector.getLocalService(DevicePolicyManagerInternal.class);
                            if (devicePolicyManagerInternal != null && devicePolicyManagerInternal.isUserOrganizationManaged(i)) {
                                i5 = i4;
                            }
                            intentSender2 = null;
                            if (!snapshotComputer.isCallerSameApp(str2, callingUid) && (!packageLPr.isSystem() || i5 == 0)) {
                                packageLPr.setUpdateOwnerPackage(null);
                            }
                            if (packageLPr.getPkg() != null) {
                                PermissionManagerServiceInternal.PackageInstalledParams.Builder builder = new PermissionManagerServiceInternal.PackageInstalledParams.Builder();
                                if ((i2 & DumpState.DUMP_CHANGES) != 0) {
                                    builder.setAllowlistedRestrictedPermissions(packageLPr.getPkg().getRequestedPermissions());
                                }
                                this.mPm.mPermissionManager.onPackageInstalled(packageLPr.getPkg(), -1, builder.build(), i);
                                this.mPm.mPackageManagerServiceExt.beforePrepareAppDataInInstallExistingPackageAsUser(str, i);
                                synchronized (this.mPm.mInstallLock) {
                                    this.mAppDataHelper.prepareAppDataAfterInstallLIF(packageLPr.getPkg());
                                }
                            }
                            PackageManagerService packageManagerService = this.mPm;
                            i6 = i4;
                            packageManagerService.sendPackageAddedForUser(packageManagerService.snapshotComputer(), str, packageLPr, i, 0);
                            synchronized (this.mPm.mLock) {
                                this.mPm.updateSequenceNumberLP(packageLPr, new int[]{i});
                            }
                            restoreAndPostInstall(new InstallRequest(i, 1, packageLPr.getPkg(), new int[]{i}, new Runnable() { // from class: com.android.server.pm.InstallPackageHelper$$ExternalSyntheticLambda2
                                @Override // java.lang.Runnable
                                public final void run() {
                                    InstallPackageHelper.this.lambda$installExistingPackageAsUser$1(str, i, intentSender);
                                }
                            }));
                        }
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        return Pair.create(Integer.valueOf(i6), intentSender2);
                    }
                    i4 = 1;
                    packageLPr.setInstalled(true, i);
                    i5 = 0;
                    packageLPr.setHidden(false, i);
                    packageLPr.setInstallReason(i3, i);
                    packageLPr.setUninstallReason(0, i);
                    packageLPr.setFirstInstallTime(System.currentTimeMillis(), i);
                    this.mPm.mSettings.writePackageRestrictionsLPr(i);
                    this.mPm.mSettings.writeKernelMappingLPr(packageLPr);
                    z = true;
                    ScanPackageUtils.setInstantAppForUser(this.mPm.mInjector, packageLPr, i, z2, z3);
                    if (z) {
                    }
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    return Pair.create(Integer.valueOf(i6), intentSender2);
                }
                return Pair.create(-3, intentSender);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$installExistingPackageAsUser$1(String str, int i, IntentSender intentSender) {
        this.mPm.restorePermissionsAndUpdateRolesForNewUserInstall(str, i);
        if (intentSender != null) {
            onInstallComplete(1, this.mContext, intentSender);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void onInstallComplete(int i, Context context, IntentSender intentSender) {
        Intent intent = new Intent();
        intent.putExtra("android.content.pm.extra.STATUS", PackageManager.installStatusToPublicStatus(i));
        try {
            BroadcastOptions makeBasic = BroadcastOptions.makeBasic();
            makeBasic.setPendingIntentBackgroundActivityLaunchAllowed(false);
            intentSender.sendIntent(context, 0, intent, null, null, null, makeBasic.toBundle());
        } catch (IntentSender.SendIntentException unused) {
        }
    }

    public void restoreAndPostInstall(InstallRequest installRequest) {
        int userId = installRequest.getUserId();
        if (PackageManagerService.DEBUG_INSTALL) {
            Log.v("PackageManager", "restoreAndPostInstall userId=" + userId + " package=" + installRequest.getPkg());
        }
        this.mPm.mPackageManagerServiceExt.onStartInRestoreAndPostInstall(installRequest);
        boolean isUpdate = installRequest.isUpdate();
        boolean z = (isUpdate || installRequest.getPkg() == null) ? false : true;
        PackageManagerService packageManagerService = this.mPm;
        if (packageManagerService.mNextInstallToken < 0) {
            packageManagerService.mNextInstallToken = 1;
        }
        int i = packageManagerService.mNextInstallToken;
        packageManagerService.mNextInstallToken = i + 1;
        packageManagerService.mRunningInstalls.put(i, installRequest);
        if (PackageManagerService.DEBUG_INSTALL) {
            Log.v("PackageManager", "+ starting restore round-trip " + i);
        }
        if (installRequest.getReturnCode() == 1 && z) {
            installRequest.closeFreezer();
            z = performBackupManagerRestore(userId, i, installRequest);
        }
        if (installRequest.getReturnCode() == 1 && !z && isUpdate) {
            z = performRollbackManagerRestore(userId, i, installRequest);
        }
        if (z) {
            return;
        }
        if (PackageManagerService.DEBUG_INSTALL) {
            Log.v("PackageManager", "No restore - queue post-install for " + i);
        }
        Trace.asyncTraceBegin(262144L, "postInstall", i);
        this.mPm.mHandler.sendMessage(this.mPm.mHandler.obtainMessage(9, i, 0));
    }

    private boolean performBackupManagerRestore(int i, int i2, InstallRequest installRequest) {
        if (installRequest.getPkg() == null) {
            return false;
        }
        IBackupManager iBackupManager = this.mInjector.getIBackupManager();
        if (iBackupManager != null) {
            if (i == -1) {
                i = 0;
            }
            if (PackageManagerService.DEBUG_INSTALL) {
                Log.v("PackageManager", "token " + i2 + " to BM for possible restore for user " + i);
            }
            Trace.asyncTraceBegin(262144L, "restore", i2);
            try {
                if (iBackupManager.isUserReadyForBackup(i)) {
                    iBackupManager.restoreAtInstallForUser(i, installRequest.getPkg().getPackageName(), i2);
                    return true;
                }
                Slog.w("PackageManager", "User " + i + " is not ready. Restore at install didn't take place.");
                return false;
            } catch (RemoteException unused) {
                return true;
            } catch (Exception e) {
                Slog.e("PackageManager", "Exception trying to enqueue restore", e);
                return false;
            }
        }
        Slog.e("PackageManager", "Backup Manager not found!");
        return false;
    }

    private boolean performRollbackManagerRestore(int i, int i2, InstallRequest installRequest) {
        PackageSetting packageLPr;
        int[] iArr;
        long j;
        int i3;
        if (installRequest.getPkg() == null) {
            return false;
        }
        String packageName = installRequest.getPkg().getPackageName();
        int[] userIds = this.mPm.mUserManager.getUserIds();
        synchronized (this.mPm.mLock) {
            packageLPr = this.mPm.mSettings.getPackageLPr(packageName);
            if (packageLPr != null) {
                i3 = packageLPr.getAppId();
                j = packageLPr.getCeDataInode(i);
                iArr = packageLPr.queryInstalledUsers(userIds, true);
            } else {
                iArr = new int[0];
                j = -1;
                i3 = -1;
            }
        }
        int installFlags = installRequest.getInstallFlags();
        boolean z = ((262144 & installFlags) == 0 && (installFlags & 128) == 0) ? false : true;
        if (packageLPr == null || !z) {
            return false;
        }
        ((RollbackManagerInternal) this.mInjector.getLocalService(RollbackManagerInternal.class)).snapshotAndRestoreUserData(packageName, UserHandle.toUserHandles(iArr), i3, j, packageLPr.getSeInfo(), i2);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void installPackagesTraced(List<InstallRequest> list) {
        synchronized (this.mPm.mInstallLock) {
            try {
                Trace.traceBegin(262144L, "installPackages");
                installPackagesLI(list);
            } finally {
                Trace.traceEnd(262144L);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:197:0x04bf  */
    /* JADX WARN: Removed duplicated region for block: B:214:0x0511  */
    /* JADX WARN: Type inference failed for: r12v5 */
    /* JADX WARN: Type inference failed for: r12v7, types: [long] */
    /* JADX WARN: Type inference failed for: r12v8 */
    /* JADX WARN: Type inference failed for: r12v9, types: [java.lang.Object, java.lang.String] */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    @GuardedBy({"mPm.mInstallLock"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void installPackagesLI(List<InstallRequest> list) {
        long j;
        boolean z;
        long j2;
        InstallRequest installRequest;
        long j3;
        InstallRequest installRequest2;
        long j4;
        ArraySet arraySet = new ArraySet(list.size());
        ArrayMap arrayMap = new ArrayMap(list.size());
        ArrayMap arrayMap2 = new ArrayMap(list.size());
        long j5 = 262144;
        try {
            Trace.traceBegin(262144L, "installPackagesLI");
            for (InstallRequest installRequest3 : list) {
                try {
                    Trace.traceBegin(j5, "preparePackage");
                    installRequest3.onPrepareStarted();
                    preparePackageLI(installRequest3);
                    installRequest3.onPrepareFinished();
                    Trace.traceEnd(j5);
                    ParsedPackage parsedPackage = installRequest3.getParsedPackage();
                    if (parsedPackage == null) {
                        installRequest3.setError(-116, "Failed to obtain package to scan");
                        for (InstallRequest installRequest4 : list) {
                            if (installRequest4.getParsedPackage() != null && ((Boolean) arrayMap2.getOrDefault(installRequest4.getParsedPackage().getPackageName(), Boolean.FALSE)).booleanValue()) {
                                cleanUpAppIdCreation(installRequest4);
                            }
                        }
                        for (InstallRequest installRequest5 : list) {
                            installRequest5.closeFreezer();
                            if (installRequest5.getReturnCode() == 1) {
                                installRequest5.setReturnCode(0);
                                this.mPm.mPackageManagerServiceExt.writeMdmLog("005", "0", installRequest5.getName());
                            }
                        }
                        Trace.traceEnd(j5);
                        return;
                    }
                    installRequest3.setReturnCode(1);
                    String packageName = parsedPackage.getPackageName();
                    try {
                        installRequest3.onScanStarted();
                        j = packageName;
                        installRequest2 = installRequest3;
                        j4 = j5;
                    } catch (PackageManagerException e) {
                        e = e;
                        installRequest2 = installRequest3;
                        j4 = j5;
                    }
                    try {
                        try {
                            installRequest2.setScanResult(scanPackageTracedLI(installRequest3.getParsedPackage(), installRequest3.getParseFlags(), installRequest3.getScanFlags(), System.currentTimeMillis(), installRequest3.getUser(), installRequest3.getAbiOverride()));
                            installRequest2.onScanFinished();
                            if (!arraySet.add(j)) {
                                installRequest2.setError(-5, "Duplicate package " + ((String) j) + " in multi-package install request.");
                                this.mPm.mPackageManagerServiceExt.writeMdmLog("005", "0", installRequest2.getName());
                                for (InstallRequest installRequest6 : list) {
                                    if (installRequest6.getParsedPackage() != null && ((Boolean) arrayMap2.getOrDefault(installRequest6.getParsedPackage().getPackageName(), Boolean.FALSE)).booleanValue()) {
                                        cleanUpAppIdCreation(installRequest6);
                                    }
                                }
                                for (InstallRequest installRequest7 : list) {
                                    installRequest7.closeFreezer();
                                    if (installRequest7.getReturnCode() == 1) {
                                        installRequest7.setReturnCode(0);
                                        this.mPm.mPackageManagerServiceExt.writeMdmLog("005", "0", installRequest7.getName());
                                    }
                                }
                                return;
                            }
                            if (!checkNoAppStorageIsConsistent(installRequest2.getScanRequestOldPackage(), parsedPackage)) {
                                installRequest2.setError(-7, "Update attempted to change value of android.internal.PROPERTY_NO_APP_DATA_STORAGE");
                                for (InstallRequest installRequest8 : list) {
                                    if (installRequest8.getParsedPackage() != null && ((Boolean) arrayMap2.getOrDefault(installRequest8.getParsedPackage().getPackageName(), Boolean.FALSE)).booleanValue()) {
                                        cleanUpAppIdCreation(installRequest8);
                                    }
                                }
                                for (InstallRequest installRequest9 : list) {
                                    installRequest9.closeFreezer();
                                    if (installRequest9.getReturnCode() == 1) {
                                        installRequest9.setReturnCode(0);
                                        this.mPm.mPackageManagerServiceExt.writeMdmLog("005", "0", installRequest9.getName());
                                    }
                                }
                                return;
                            }
                            if ((installRequest2.getScanFlags() & 67108864) != 0) {
                                installRequest2.getScannedPackageSetting().setAppId(-1);
                            } else {
                                arrayMap2.put(j, Boolean.valueOf(optimisticallyRegisterAppId(installRequest2)));
                            }
                            arrayMap.put(j, this.mPm.getSettingsVersionForPackage(parsedPackage));
                            ((IHbtUtilSocExt) ExtLoader.type(IHbtUtilSocExt.class).create()).hbtCheckStatus(j, InstructionSets.getAppDexInstructionSets(installRequest2.getRealPackageSetting().getPrimaryCpuAbi(), installRequest2.getRealPackageSetting().getSecondaryCpuAbi()));
                            j5 = j4;
                        } catch (Throwable th) {
                            th = th;
                            z = false;
                            j = j4;
                            if (z) {
                            }
                            throw th;
                        }
                    } catch (PackageManagerException e2) {
                        e = e2;
                        installRequest2.setError("Scanning Failed.", e);
                        this.mPm.mPackageManagerServiceExt.writeMdmLog("005", "0", installRequest2.getName());
                        for (InstallRequest installRequest10 : list) {
                            if (installRequest10.getParsedPackage() != null && ((Boolean) arrayMap2.getOrDefault(installRequest10.getParsedPackage().getPackageName(), Boolean.FALSE)).booleanValue()) {
                                cleanUpAppIdCreation(installRequest10);
                            }
                        }
                        for (InstallRequest installRequest11 : list) {
                            installRequest11.closeFreezer();
                            if (installRequest11.getReturnCode() == 1) {
                                installRequest11.setReturnCode(0);
                                this.mPm.mPackageManagerServiceExt.writeMdmLog("005", "0", installRequest11.getName());
                            }
                        }
                        Trace.traceEnd(j4);
                        return;
                    }
                } catch (PrepareFailure e3) {
                    installRequest = installRequest3;
                    j3 = j5;
                    try {
                        installRequest.setError(e3.error, e3.getMessage());
                        installRequest.setOriginPackage(e3.mConflictingPackage);
                        installRequest.setOriginPermission(e3.mConflictingPermission);
                        this.mPm.mPackageManagerServiceExt.writeMdmLog("005", "0", installRequest.getName());
                        installRequest.onPrepareFinished();
                        Trace.traceEnd(j3);
                        for (InstallRequest installRequest12 : list) {
                            if (installRequest12.getParsedPackage() != null && ((Boolean) arrayMap2.getOrDefault(installRequest12.getParsedPackage().getPackageName(), Boolean.FALSE)).booleanValue()) {
                                cleanUpAppIdCreation(installRequest12);
                            }
                        }
                        for (InstallRequest installRequest13 : list) {
                            installRequest13.closeFreezer();
                            if (installRequest13.getReturnCode() == 1) {
                                installRequest13.setReturnCode(0);
                                this.mPm.mPackageManagerServiceExt.writeMdmLog("005", "0", installRequest13.getName());
                            }
                        }
                        return;
                    } catch (Throwable th2) {
                        th = th2;
                        installRequest.onPrepareFinished();
                        Trace.traceEnd(j3);
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    installRequest = installRequest3;
                    j3 = j5;
                    installRequest.onPrepareFinished();
                    Trace.traceEnd(j3);
                    throw th;
                }
            }
            j2 = j5;
        } catch (Throwable th4) {
            th = th4;
            j = j5;
        }
        try {
            try {
                synchronized (this.mPm.mLock) {
                    j = j2;
                    try {
                        try {
                            Trace.traceBegin(j, "reconcilePackages");
                            List<ReconciledPackage> reconcilePackages = ReconcilePackageUtils.reconcilePackages(list, Collections.unmodifiableMap(this.mPm.mPackages), arrayMap, this.mSharedLibraries, this.mPm.mSettings.getKeySetManagerService(), this.mPm.mSettings);
                            try {
                                Trace.traceBegin(j, "commitPackages");
                                commitPackagesLocked(reconcilePackages, this.mPm.mUserManager.getUserIds());
                                try {
                                    try {
                                        executePostCommitStepsLIF(reconcilePackages);
                                        for (InstallRequest installRequest14 : list) {
                                            if (installRequest14.getDataLoaderType() == 2 && installRequest14.getSignatureSchemeVersion() == 4) {
                                                String baseApkPath = installRequest14.getPkg().getBaseApkPath();
                                                String[] splitCodePaths = installRequest14.getPkg().getSplitCodePaths();
                                                Uri originUri = installRequest14.getOriginUri();
                                                PackageManagerService packageManagerService = this.mPm;
                                                int i = packageManagerService.mPendingVerificationToken;
                                                packageManagerService.mPendingVerificationToken = i + 1;
                                                VerificationUtils.broadcastPackageVerified(i, originUri, 1, PackageManagerServiceUtils.buildVerificationRootHashString(baseApkPath, splitCodePaths), installRequest14.getDataLoaderType(), installRequest14.getUser(), this.mContext);
                                            }
                                        }
                                        Trace.traceEnd(j);
                                        PackageManagerService packageManagerService2 = this.mPm;
                                        packageManagerService2.mPackageManagerServiceExt.afterInstallPackagesLIForIconPack(packageManagerService2.mContext);
                                    } catch (Throwable th5) {
                                        th = th5;
                                        z = true;
                                        if (z) {
                                            for (InstallRequest installRequest15 : list) {
                                                if (installRequest15.getParsedPackage() != null && ((Boolean) arrayMap2.getOrDefault(installRequest15.getParsedPackage().getPackageName(), Boolean.FALSE)).booleanValue()) {
                                                    cleanUpAppIdCreation(installRequest15);
                                                }
                                            }
                                            for (InstallRequest installRequest16 : list) {
                                                installRequest16.closeFreezer();
                                                if (installRequest16.getReturnCode() == 1) {
                                                    installRequest16.setReturnCode(0);
                                                    this.mPm.mPackageManagerServiceExt.writeMdmLog("005", "0", installRequest16.getName());
                                                }
                                            }
                                        } else {
                                            for (InstallRequest installRequest17 : list) {
                                                if (installRequest17.getDataLoaderType() == 2 && installRequest17.getSignatureSchemeVersion() == 4) {
                                                    String baseApkPath2 = installRequest17.getPkg().getBaseApkPath();
                                                    String[] splitCodePaths2 = installRequest17.getPkg().getSplitCodePaths();
                                                    Uri originUri2 = installRequest17.getOriginUri();
                                                    PackageManagerService packageManagerService3 = this.mPm;
                                                    int i2 = packageManagerService3.mPendingVerificationToken;
                                                    packageManagerService3.mPendingVerificationToken = i2 + 1;
                                                    VerificationUtils.broadcastPackageVerified(i2, originUri2, 1, PackageManagerServiceUtils.buildVerificationRootHashString(baseApkPath2, splitCodePaths2), installRequest17.getDataLoaderType(), installRequest17.getUser(), this.mContext);
                                                }
                                            }
                                        }
                                        throw th;
                                    }
                                } catch (Throwable th6) {
                                    th = th6;
                                    z = true;
                                    j = j;
                                    while (true) {
                                        try {
                                            try {
                                                break;
                                            } catch (Throwable th7) {
                                                th = th7;
                                                if (z) {
                                                }
                                                throw th;
                                            }
                                        } catch (Throwable th8) {
                                            th = th8;
                                        }
                                    }
                                    throw th;
                                }
                            } finally {
                                Trace.traceEnd(j);
                            }
                        } catch (Throwable th9) {
                            throw th9;
                        }
                    } catch (ReconcileFailure e4) {
                        for (InstallRequest installRequest18 : list) {
                            installRequest18.setError("Reconciliation failed...", e4);
                            this.mPm.mPackageManagerServiceExt.writeMdmLog("005", "0", installRequest18.getName());
                        }
                        Trace.traceEnd(j);
                        for (InstallRequest installRequest19 : list) {
                            if (installRequest19.getParsedPackage() != null && ((Boolean) arrayMap2.getOrDefault(installRequest19.getParsedPackage().getPackageName(), Boolean.FALSE)).booleanValue()) {
                                cleanUpAppIdCreation(installRequest19);
                            }
                        }
                        for (InstallRequest installRequest20 : list) {
                            installRequest20.closeFreezer();
                            if (installRequest20.getReturnCode() == 1) {
                                installRequest20.setReturnCode(0);
                                this.mPm.mPackageManagerServiceExt.writeMdmLog("005", "0", installRequest20.getName());
                            }
                        }
                    }
                }
            } catch (Throwable th10) {
                th = th10;
                z = false;
                j = j;
            }
        } catch (Throwable th11) {
            th = th11;
            j = j2;
            z = false;
            if (z) {
            }
            throw th;
        }
    }

    @GuardedBy({"mPm.mInstallLock"})
    private boolean checkNoAppStorageIsConsistent(AndroidPackage androidPackage, AndroidPackage androidPackage2) {
        if (androidPackage == null) {
            return true;
        }
        PackageManager.Property property = androidPackage.getProperties().get("android.internal.PROPERTY_NO_APP_DATA_STORAGE");
        PackageManager.Property property2 = androidPackage2.getProperties().get("android.internal.PROPERTY_NO_APP_DATA_STORAGE");
        return (property == null || !property.getBoolean()) ? property2 == null || !property2.getBoolean() : property2 != null && property2.getBoolean();
    }

    /* JADX WARN: Code restructure failed: missing block: B:266:0x0cc7, code lost:
    
        r5 = null;
        r17 = null;
        r3 = r12;
        r8 = false;
        r12 = null;
        r19 = r19;
     */
    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:137:0x0493 A[Catch: all -> 0x0d69, TryCatch #8 {, blocks: (B:593:0x0288, B:595:0x029a, B:597:0x02a4, B:599:0x02af, B:602:0x0305, B:606:0x0320, B:607:0x0351, B:608:0x0352, B:610:0x0358, B:612:0x035c, B:615:0x0367, B:616:0x0387, B:111:0x0390, B:113:0x039a, B:115:0x03a0, B:117:0x03ac, B:119:0x03b2, B:120:0x03c7, B:122:0x03cd, B:124:0x03d5, B:127:0x03e4, B:129:0x03e8, B:130:0x0407, B:132:0x041d, B:137:0x0493, B:139:0x0497, B:140:0x04ad, B:141:0x04c6, B:143:0x04d1, B:145:0x04ea, B:147:0x04f0, B:149:0x04f4, B:153:0x04fe, B:158:0x0505, B:159:0x0552, B:152:0x0553, B:163:0x055b, B:165:0x0569, B:168:0x0589, B:170:0x05bf, B:172:0x05c9, B:174:0x05d1, B:178:0x05e4, B:179:0x061c, B:180:0x061d, B:181:0x062a, B:183:0x0636, B:185:0x063f, B:187:0x0645, B:188:0x0677, B:190:0x067d, B:194:0x0686, B:200:0x06a6, B:202:0x06b5, B:204:0x06bf, B:210:0x06c7, B:211:0x0723, B:214:0x0724, B:215:0x0773, B:196:0x06a0, B:208:0x0774, B:222:0x077f, B:564:0x0424, B:565:0x0444, B:567:0x0445, B:569:0x046b, B:570:0x046f, B:579:0x047c, B:583:0x047f, B:584:0x048a, B:620:0x02d4, B:622:0x02e0, B:624:0x02e4, B:625:0x02fa), top: B:592:0x0288, inners: #12 }] */
    /* JADX WARN: Removed duplicated region for block: B:143:0x04d1 A[Catch: all -> 0x0d69, TryCatch #8 {, blocks: (B:593:0x0288, B:595:0x029a, B:597:0x02a4, B:599:0x02af, B:602:0x0305, B:606:0x0320, B:607:0x0351, B:608:0x0352, B:610:0x0358, B:612:0x035c, B:615:0x0367, B:616:0x0387, B:111:0x0390, B:113:0x039a, B:115:0x03a0, B:117:0x03ac, B:119:0x03b2, B:120:0x03c7, B:122:0x03cd, B:124:0x03d5, B:127:0x03e4, B:129:0x03e8, B:130:0x0407, B:132:0x041d, B:137:0x0493, B:139:0x0497, B:140:0x04ad, B:141:0x04c6, B:143:0x04d1, B:145:0x04ea, B:147:0x04f0, B:149:0x04f4, B:153:0x04fe, B:158:0x0505, B:159:0x0552, B:152:0x0553, B:163:0x055b, B:165:0x0569, B:168:0x0589, B:170:0x05bf, B:172:0x05c9, B:174:0x05d1, B:178:0x05e4, B:179:0x061c, B:180:0x061d, B:181:0x062a, B:183:0x0636, B:185:0x063f, B:187:0x0645, B:188:0x0677, B:190:0x067d, B:194:0x0686, B:200:0x06a6, B:202:0x06b5, B:204:0x06bf, B:210:0x06c7, B:211:0x0723, B:214:0x0724, B:215:0x0773, B:196:0x06a0, B:208:0x0774, B:222:0x077f, B:564:0x0424, B:565:0x0444, B:567:0x0445, B:569:0x046b, B:570:0x046f, B:579:0x047c, B:583:0x047f, B:584:0x048a, B:620:0x02d4, B:622:0x02e0, B:624:0x02e4, B:625:0x02fa), top: B:592:0x0288, inners: #12 }] */
    /* JADX WARN: Removed duplicated region for block: B:165:0x0569 A[Catch: all -> 0x0d69, TryCatch #8 {, blocks: (B:593:0x0288, B:595:0x029a, B:597:0x02a4, B:599:0x02af, B:602:0x0305, B:606:0x0320, B:607:0x0351, B:608:0x0352, B:610:0x0358, B:612:0x035c, B:615:0x0367, B:616:0x0387, B:111:0x0390, B:113:0x039a, B:115:0x03a0, B:117:0x03ac, B:119:0x03b2, B:120:0x03c7, B:122:0x03cd, B:124:0x03d5, B:127:0x03e4, B:129:0x03e8, B:130:0x0407, B:132:0x041d, B:137:0x0493, B:139:0x0497, B:140:0x04ad, B:141:0x04c6, B:143:0x04d1, B:145:0x04ea, B:147:0x04f0, B:149:0x04f4, B:153:0x04fe, B:158:0x0505, B:159:0x0552, B:152:0x0553, B:163:0x055b, B:165:0x0569, B:168:0x0589, B:170:0x05bf, B:172:0x05c9, B:174:0x05d1, B:178:0x05e4, B:179:0x061c, B:180:0x061d, B:181:0x062a, B:183:0x0636, B:185:0x063f, B:187:0x0645, B:188:0x0677, B:190:0x067d, B:194:0x0686, B:200:0x06a6, B:202:0x06b5, B:204:0x06bf, B:210:0x06c7, B:211:0x0723, B:214:0x0724, B:215:0x0773, B:196:0x06a0, B:208:0x0774, B:222:0x077f, B:564:0x0424, B:565:0x0444, B:567:0x0445, B:569:0x046b, B:570:0x046f, B:579:0x047c, B:583:0x047f, B:584:0x048a, B:620:0x02d4, B:622:0x02e0, B:624:0x02e4, B:625:0x02fa), top: B:592:0x0288, inners: #12 }] */
    /* JADX WARN: Removed duplicated region for block: B:277:0x0d3d  */
    /* JADX WARN: Removed duplicated region for block: B:279:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:563:0x04c4  */
    /* JADX WARN: Removed duplicated region for block: B:602:0x0305 A[Catch: all -> 0x0d69, TryCatch #8 {, blocks: (B:593:0x0288, B:595:0x029a, B:597:0x02a4, B:599:0x02af, B:602:0x0305, B:606:0x0320, B:607:0x0351, B:608:0x0352, B:610:0x0358, B:612:0x035c, B:615:0x0367, B:616:0x0387, B:111:0x0390, B:113:0x039a, B:115:0x03a0, B:117:0x03ac, B:119:0x03b2, B:120:0x03c7, B:122:0x03cd, B:124:0x03d5, B:127:0x03e4, B:129:0x03e8, B:130:0x0407, B:132:0x041d, B:137:0x0493, B:139:0x0497, B:140:0x04ad, B:141:0x04c6, B:143:0x04d1, B:145:0x04ea, B:147:0x04f0, B:149:0x04f4, B:153:0x04fe, B:158:0x0505, B:159:0x0552, B:152:0x0553, B:163:0x055b, B:165:0x0569, B:168:0x0589, B:170:0x05bf, B:172:0x05c9, B:174:0x05d1, B:178:0x05e4, B:179:0x061c, B:180:0x061d, B:181:0x062a, B:183:0x0636, B:185:0x063f, B:187:0x0645, B:188:0x0677, B:190:0x067d, B:194:0x0686, B:200:0x06a6, B:202:0x06b5, B:204:0x06bf, B:210:0x06c7, B:211:0x0723, B:214:0x0724, B:215:0x0773, B:196:0x06a0, B:208:0x0774, B:222:0x077f, B:564:0x0424, B:565:0x0444, B:567:0x0445, B:569:0x046b, B:570:0x046f, B:579:0x047c, B:583:0x047f, B:584:0x048a, B:620:0x02d4, B:622:0x02e0, B:624:0x02e4, B:625:0x02fa), top: B:592:0x0288, inners: #12 }] */
    /* JADX WARN: Removed duplicated region for block: B:618:0x0388  */
    /* JADX WARN: Type inference failed for: r19v1 */
    /* JADX WARN: Type inference failed for: r19v10, types: [boolean] */
    /* JADX WARN: Type inference failed for: r19v13 */
    /* JADX WARN: Type inference failed for: r19v14 */
    /* JADX WARN: Type inference failed for: r19v15 */
    /* JADX WARN: Type inference failed for: r19v6 */
    /* JADX WARN: Type inference failed for: r19v7 */
    /* JADX WARN: Type inference failed for: r5v11, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r5v54, types: [java.lang.String] */
    @GuardedBy({"mPm.mInstallLock"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void preparePackageLI(InstallRequest installRequest) throws PrepareFailure {
        int i;
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        int i2;
        boolean z5;
        int size;
        int i3;
        boolean z6;
        int size2;
        InstallRequest installRequest2;
        PackageSetting packageLPr;
        boolean z7;
        int i4;
        int i5;
        ?? r19;
        Throwable th;
        int i6;
        PackageFreezer packageFreezer;
        PackageSetting packageLPr2;
        Throwable th2;
        PackageSetting packageLPr3;
        PackageSetting packageSetting;
        int i7;
        boolean z8;
        AndroidPackage androidPackage;
        int i8;
        PackageSetting packageSetting2;
        PackageFreezer packageFreezer2;
        PackageFreezer packageFreezer3;
        PackageFreezer packageFreezer4;
        InstallRequest installRequest3;
        boolean z9;
        SharedLibraryInfo latestStaticSharedLibraVersion;
        WatchedLongSparseArray<SharedLibraryInfo> sharedLibraryInfos;
        int installFlags = installRequest.getInstallFlags();
        boolean z10 = installRequest.getVolumeUuid() != null;
        boolean z11 = (installFlags & 2048) != 0;
        boolean z12 = (installFlags & 16384) != 0;
        boolean z13 = (installFlags & 65536) != 0;
        boolean z14 = (installFlags & 131072) != 0;
        boolean z15 = installRequest.getInstallReason() == 5;
        int i9 = installRequest.isInstallMove() ? UsbTerminalTypes.TERMINAL_IN_PROC_MIC_ARRAY : 6;
        if ((installFlags & 4096) != 0) {
            i9 |= 1024;
        }
        if (z11) {
            i9 |= 8192;
        }
        if (z12) {
            i9 |= 16384;
        }
        if (z13) {
            i9 |= 32768;
        }
        if (z14) {
            i9 |= 67108864;
        }
        File file = new File(z14 ? installRequest.getApexInfo().modulePath : installRequest.getCodePath());
        if (PackageManagerService.DEBUG_INSTALL) {
            Slog.d("PackageManager", "installPackageLI: path=" + file);
        }
        if (z11 && z10) {
            Slog.i("PackageManager", "Incompatible ephemeral install; external=" + z10);
            throw new PrepareFailure(-116);
        }
        int defParseFlags = this.mPm.getDefParseFlags() | Integer.MIN_VALUE | 64 | (z10 ? 8 : 0);
        Trace.traceBegin(262144L, "parsePackage");
        try {
            try {
                PackageParser2 preparingPackageParser = this.mPm.mInjector.getPreparingPackageParser();
                try {
                    ParsedPackage parsePackage = preparingPackageParser.parsePackage(file, defParseFlags, false);
                    PackageManagerService packageManagerService = this.mPm;
                    packageManagerService.mPackageManagerServiceExt.afterPackageParsedInPreparePackageLI(packageManagerService.mContext, parsePackage);
                    AndroidPackageUtils.validatePackageDexMetadata(parsePackage);
                    preparingPackageParser.close();
                    Trace.traceEnd(262144L);
                    if (parsePackage != null) {
                        installRequest.setName(parsePackage.getPackageName());
                    }
                    boolean z16 = (16777216 & installFlags) != 0;
                    if (!z16 && parsePackage.isTestOnly()) {
                        z16 = true;
                    }
                    if (!this.mPm.mPackageManagerServiceExt.hookBeforeTargetSdkBlock(installRequest, z16, parsePackage.getTargetSdkVersion()) && parsePackage.getTargetSdkVersion() < 23) {
                        Slog.w("PackageManager", "App " + parsePackage.getPackageName() + " targets deprecated sdk version");
                        throw new PrepareFailure(-29, "App package must target at least SDK version 23, but found " + parsePackage.getTargetSdkVersion());
                    }
                    if (z11) {
                        if (parsePackage.getTargetSdkVersion() < 26) {
                            Slog.w("PackageManager", "Instant app package " + parsePackage.getPackageName() + " does not target at least O");
                            throw new PrepareFailure(-116, "Instant app package must target at least O");
                        }
                        if (parsePackage.getSharedUserId() != null) {
                            Slog.w("PackageManager", "Instant app package " + parsePackage.getPackageName() + " may not declare sharedUserId.");
                            throw new PrepareFailure(-116, "Instant app package may not declare a sharedUserId");
                        }
                    }
                    if (parsePackage.isStaticSharedLibrary()) {
                        PackageManagerService.renameStaticSharedLibraryPackage(parsePackage);
                        if (z10) {
                            Slog.i("PackageManager", "Static shared libs can only be installed on internal storage.");
                            throw new PrepareFailure(-19, "Static shared libs can only be installed on internal storage.");
                        }
                    }
                    if (this.mPm.mPackageManagerServiceExt.interceptOsdkVersionInPreparePackageLI(parsePackage)) {
                        throw new PrepareFailure(-116, "Packages declaring osdk version do not match");
                    }
                    String packageName = parsePackage.getPackageName();
                    installRequest.setName(packageName);
                    PackageManagerService packageManagerService2 = this.mPm;
                    PrepareFailure prepareFailure = (PrepareFailure) packageManagerService2.mPackageManagerServiceExt.interceptWithoutSigInPreparePackageLI(packageManagerService2.mContext, installRequest, packageName);
                    if (prepareFailure != null) {
                        throw prepareFailure;
                    }
                    if (parsePackage.isTestOnly() && (installFlags & 4) == 0) {
                        throw new PrepareFailure(-15, "Failed to install test-only apk. Did you forget to add -t?");
                    }
                    if (installRequest.getSigningDetails() != SigningDetails.UNKNOWN) {
                        parsePackage.setSigningDetails(installRequest.getSigningDetails());
                    } else {
                        ParseResult<SigningDetails> signingDetails = ParsingPackageUtils.getSigningDetails((ParseInput) ParseTypeImpl.forDefaultParsing(), parsePackage, false);
                        if (signingDetails.isError()) {
                            throw new PrepareFailure("Failed collect during installPackageLI", signingDetails.getException());
                        }
                        parsePackage.setSigningDetails((SigningDetails) signingDetails.getResult());
                    }
                    PrepareFailure prepareFailure2 = (PrepareFailure) this.mPm.mPackageManagerServiceExt.interceptWithSigInPreparePackageLI(parsePackage, packageName, installRequest.getWrapper().getInstallArgs(), installFlags);
                    if (prepareFailure2 != null) {
                        throw prepareFailure2;
                    }
                    if (z11 && parsePackage.getSigningDetails().getSignatureSchemeVersion() < 2) {
                        Slog.w("PackageManager", "Instant app package " + parsePackage.getPackageName() + " is not signed with at least APK Signature Scheme v2");
                        throw new PrepareFailure(-116, "Instant app package must be signed with APK Signature Scheme v2 or greater");
                    }
                    synchronized (this.mPm.mLock) {
                        if ((installFlags & 2) != 0) {
                            String renamedPackageLPr = this.mPm.mSettings.getRenamedPackageLPr(packageName);
                            if (parsePackage.getOriginalPackages().contains(renamedPackageLPr) && this.mPm.mPackages.containsKey(renamedPackageLPr)) {
                                parsePackage.setPackageName(renamedPackageLPr);
                                packageName = parsePackage.getPackageName();
                                if (PackageManagerService.DEBUG_INSTALL) {
                                    StringBuilder sb = new StringBuilder();
                                    i = defParseFlags;
                                    sb.append("Replacing existing renamed package: oldName=");
                                    sb.append(renamedPackageLPr);
                                    sb.append(" pkgName=");
                                    sb.append(packageName);
                                    Slog.d("PackageManager", sb.toString());
                                } else {
                                    i = defParseFlags;
                                }
                            } else {
                                i = defParseFlags;
                                if (this.mPm.mPackages.containsKey(packageName)) {
                                    if (PackageManagerService.DEBUG_INSTALL) {
                                        Slog.d("PackageManager", "Replace existing package: " + packageName);
                                    }
                                    this.mPm.mPackageManagerServiceSocExt.acquireUxPerfLockPkgUpdate(packageName);
                                } else {
                                    z = false;
                                    if (z) {
                                        z2 = z;
                                    } else {
                                        AndroidPackage androidPackage2 = this.mPm.mPackages.get(packageName);
                                        int targetSdkVersion = androidPackage2.getTargetSdkVersion();
                                        int targetSdkVersion2 = parsePackage.getTargetSdkVersion();
                                        z2 = z;
                                        if (targetSdkVersion > 22 && targetSdkVersion2 <= 22) {
                                            throw new PrepareFailure(-26, "Package " + parsePackage.getPackageName() + " new target SDK " + targetSdkVersion2 + " doesn't support runtime permissions but the old target SDK " + targetSdkVersion + " does.");
                                        }
                                        if (androidPackage2.isPersistent() && (installFlags & DumpState.DUMP_COMPILER_STATS) == 0 && !this.mPm.mPackageManagerServiceExt.allowPersistentUpdateInPreparePackageLI()) {
                                            throw new PrepareFailure(-2, "Package " + androidPackage2.getPackageName() + " is a persistent app. Persistent apps are not updateable.");
                                        }
                                    }
                                    z3 = z2;
                                }
                            }
                            z = true;
                            if (z) {
                            }
                            z3 = z2;
                        } else {
                            i = defParseFlags;
                            z3 = false;
                        }
                        PackageSetting packageLPr4 = this.mPm.mSettings.getPackageLPr(packageName);
                        PackageSetting packageLPr5 = (packageLPr4 != null || !parsePackage.isSdkLibrary() || (sharedLibraryInfos = this.mSharedLibraries.getSharedLibraryInfos(parsePackage.getSdkLibraryName())) == null || sharedLibraryInfos.size() <= 0) ? packageLPr4 : this.mPm.mSettings.getPackageLPr(sharedLibraryInfos.valueAt(0).getPackageName());
                        if (parsePackage.isStaticSharedLibrary() && (latestStaticSharedLibraVersion = this.mSharedLibraries.getLatestStaticSharedLibraVersion(parsePackage)) != null) {
                            packageLPr5 = this.mPm.mSettings.getPackageLPr(latestStaticSharedLibraVersion.getPackageName());
                        }
                        PackageSetting packageSetting3 = packageLPr5;
                        if (packageSetting3 != null) {
                            if (PackageManagerService.DEBUG_INSTALL) {
                                z4 = z14;
                                StringBuilder sb2 = new StringBuilder();
                                i2 = installFlags;
                                sb2.append("Existing package for signature checking: ");
                                sb2.append(packageSetting3);
                                Slog.d("PackageManager", sb2.toString());
                            } else {
                                z4 = z14;
                                i2 = installFlags;
                            }
                            KeySetManagerService keySetManagerService = this.mPm.mSettings.getKeySetManagerService();
                            SharedUserSetting sharedUserSettingLPr = this.mPm.mSettings.getSharedUserSettingLPr(packageSetting3);
                            if (keySetManagerService.shouldCheckUpgradeKeySetLocked(packageSetting3, sharedUserSettingLPr, i9)) {
                                if (!keySetManagerService.checkUpgradeKeySetLocked(packageSetting3, parsePackage)) {
                                    throw new PrepareFailure(-7, "Package " + parsePackage.getPackageName() + " upgrade keys do not match the previously installed version");
                                }
                            } else {
                                try {
                                    r19 = ReconcilePackageUtils.isCompatSignatureUpdateNeeded(this.mPm.getSettingsVersionForPackage(parsePackage));
                                    if (PackageManagerServiceUtils.verifySignatures(packageSetting3, sharedUserSettingLPr, null, parsePackage.getSigningDetails(), r19, ReconcilePackageUtils.isRecoverSignatureUpdateNeeded(this.mPm.getSettingsVersionForPackage(parsePackage)), z15)) {
                                        synchronized (this.mPm.mLock) {
                                            keySetManagerService.removeAppKeySetDataLPw(parsePackage.getPackageName());
                                        }
                                    }
                                    if (packageLPr4 == null) {
                                        if (PackageManagerService.DEBUG_INSTALL) {
                                            Slog.d("PackageManager", "Existing package: " + packageLPr4);
                                        }
                                        boolean isSystem = packageLPr4.isSystem();
                                        installRequest.setOriginUsers(packageLPr4.queryInstalledUsers(this.mPm.mUserManager.getUserIds(), true));
                                        z5 = isSystem;
                                    } else {
                                        z5 = false;
                                    }
                                    size = ArrayUtils.size(parsePackage.getPermissionGroups());
                                    i3 = 0;
                                    while (i3 < size) {
                                        ParsedPermissionGroup parsedPermissionGroup = parsePackage.getPermissionGroups().get(i3);
                                        boolean z17 = z15;
                                        PermissionGroupInfo permissionGroupInfo = this.mPm.getPermissionGroupInfo(parsedPermissionGroup.getName(), 0);
                                        if (permissionGroupInfo != null && cannotInstallWithBadPermissionGroups(parsePackage)) {
                                            String str = permissionGroupInfo.packageName;
                                            if ((z3 || !parsePackage.getPackageName().equals(str)) && !doesSignatureMatchForPermissions(str, parsePackage, i9)) {
                                                EventLog.writeEvent(1397638484, "146211400", -1, parsePackage.getPackageName());
                                                throw new PrepareFailure(-126, "Package " + parsePackage.getPackageName() + " attempting to redeclare permission group " + parsedPermissionGroup.getName() + " already owned by " + str);
                                            }
                                        }
                                        i3++;
                                        z15 = z17;
                                    }
                                    z6 = z15;
                                    for (size2 = ArrayUtils.size(parsePackage.getPermissions()) - 1; size2 >= 0; size2--) {
                                        ParsedPermission parsedPermission = parsePackage.getPermissions().get(size2);
                                        Permission permissionTEMP = this.mPm.mPermissionManager.getPermissionTEMP(parsedPermission.getName());
                                        if ((parsedPermission.getProtectionLevel() & 4096) != 0 && !z5) {
                                            Slog.w("PackageManager", "Non-System package " + parsePackage.getPackageName() + " attempting to delcare ephemeral permission " + parsedPermission.getName() + "; Removing ephemeral.");
                                            ComponentMutateUtils.setProtectionLevel(parsedPermission, parsedPermission.getProtectionLevel() & (-4097));
                                        }
                                        if (permissionTEMP != null) {
                                            String packageName2 = permissionTEMP.getPackageName();
                                            if (!doesSignatureMatchForPermissions(packageName2, parsePackage, i9)) {
                                                if (!packageName2.equals(PackageManagerService.PLATFORM_PACKAGE_NAME) && !this.mPm.mPackageManagerServiceExt.allowDuplicatedPermInPreparePackageLI(installRequest.getWrapper().getInstallArgs(), packageName2)) {
                                                    throw new PrepareFailure(-112, "Package " + parsePackage.getPackageName() + " attempting to redeclare permission " + parsedPermission.getName() + " already owned by " + packageName2).conflictsWithExistingPermission(parsedPermission.getName(), packageName2);
                                                }
                                                PackageManagerService packageManagerService3 = this.mPm;
                                                packageManagerService3.mPackageManagerServiceExt.customLogDuplicatedPermDeclared(packageManagerService3.mContext, packageName2, parsePackage, parsedPermission);
                                                parsePackage.removePermission(size2);
                                            } else if (!PackageManagerService.PLATFORM_PACKAGE_NAME.equals(parsePackage.getPackageName()) && (parsedPermission.getProtectionLevel() & 15) == 1 && !permissionTEMP.isRuntime()) {
                                                Slog.w("PackageManager", "Package " + parsePackage.getPackageName() + " trying to change a non-runtime permission " + parsedPermission.getName() + " to runtime; keeping old protection level");
                                                ComponentMutateUtils.setProtectionLevel(parsedPermission, permissionTEMP.getProtectionLevel());
                                            }
                                        }
                                        if (parsedPermission.getGroup() != null && cannotInstallWithBadPermissionGroups(parsePackage)) {
                                            int i10 = 0;
                                            while (true) {
                                                if (i10 >= size) {
                                                    z9 = false;
                                                    break;
                                                } else {
                                                    if (parsePackage.getPermissionGroups().get(i10).getName().equals(parsedPermission.getGroup())) {
                                                        z9 = true;
                                                        break;
                                                    }
                                                    i10++;
                                                }
                                            }
                                            if (z9) {
                                                continue;
                                            } else {
                                                PermissionGroupInfo permissionGroupInfo2 = this.mPm.getPermissionGroupInfo(parsedPermission.getGroup(), 0);
                                                if (permissionGroupInfo2 == null) {
                                                    EventLog.writeEvent(1397638484, "146211400", -1, parsePackage.getPackageName());
                                                    throw new PrepareFailure(-127, "Package " + parsePackage.getPackageName() + " attempting to declare permission " + parsedPermission.getName() + " in non-existing group " + parsedPermission.getGroup());
                                                }
                                                String str2 = permissionGroupInfo2.packageName;
                                                if (!PackageManagerService.PLATFORM_PACKAGE_NAME.equals(str2) && !doesSignatureMatchForPermissions(str2, parsePackage, i9)) {
                                                    EventLog.writeEvent(1397638484, "146211400", -1, parsePackage.getPackageName());
                                                    throw new PrepareFailure(-127, "Package " + parsePackage.getPackageName() + " attempting to declare permission " + parsedPermission.getName() + " in group " + parsedPermission.getGroup() + " owned by package " + str2 + " with incompatible certificate");
                                                }
                                            }
                                        }
                                    }
                                } catch (PackageManagerException e) {
                                    throw new PrepareFailure(e.error, e.getMessage());
                                }
                            }
                        } else {
                            z4 = z14;
                            i2 = installFlags;
                        }
                        if (packageLPr4 == null) {
                        }
                        size = ArrayUtils.size(parsePackage.getPermissionGroups());
                        i3 = 0;
                        while (i3 < size) {
                        }
                        z6 = z15;
                        while (size2 >= 0) {
                        }
                    }
                    if (z5) {
                        if (z10) {
                            throw new PrepareFailure(-19, "Cannot install updates to system apps on sdcard");
                        }
                        if (z11) {
                            throw new PrepareFailure(-116, "Cannot update a system app with an instant app");
                        }
                        PrepareFailure prepareFailure3 = (PrepareFailure) this.mPm.mPackageManagerServiceExt.interceptSystemAppInPreparePackageLI(z3, parsePackage);
                        if (prepareFailure3 != null) {
                            throw prepareFailure3;
                        }
                    }
                    if (installRequest.isInstallMove()) {
                        int i11 = i9 | 1 | 256;
                        synchronized (this.mPm.mLock) {
                            PackageSetting packageLPr6 = this.mPm.mSettings.getPackageLPr(packageName);
                            if (packageLPr6 == null) {
                                InstallRequest installRequest4 = installRequest;
                                installRequest4.setError(PackageManagerException.ofInternalError("Missing settings for moved package " + packageName, -3));
                                installRequest3 = installRequest4;
                            } else {
                                installRequest3 = installRequest;
                            }
                            parsePackage.setPrimaryCpuAbi(packageLPr6.getPrimaryCpuAbiLegacy()).setSecondaryCpuAbi(packageLPr6.getSecondaryCpuAbiLegacy());
                        }
                        i5 = i11;
                        i4 = i2;
                        installRequest2 = installRequest3;
                    } else {
                        installRequest2 = installRequest;
                        int i12 = i9 | 1;
                        try {
                            synchronized (this.mPm.mLock) {
                                packageLPr = this.mPm.mSettings.getPackageLPr(packageName);
                            }
                            boolean z18 = packageLPr != null && packageLPr.isUpdatedSystemApp();
                            String deriveAbiOverride = PackageManagerServiceUtils.deriveAbiOverride(installRequest.getAbiOverride());
                            boolean z19 = packageLPr != null && packageLPr.isSystem();
                            PackageAbiHelper packageAbiHelper = this.mPackageAbiHelper;
                            if (!z18 && !z19) {
                                z7 = false;
                                Pair<PackageAbiHelper.Abis, PackageAbiHelper.NativeLibraryPaths> derivePackageAbi = packageAbiHelper.derivePackageAbi(parsePackage, z5, z7, deriveAbiOverride, ScanPackageUtils.getAppLib32InstallDir());
                                ((PackageAbiHelper.Abis) derivePackageAbi.first).applyTo(parsePackage);
                                ((PackageAbiHelper.NativeLibraryPaths) derivePackageAbi.second).applyTo(parsePackage);
                                i4 = i2;
                                this.mPm.mPackageManagerServiceExt.onHandleForNotMoveInPreparePackageLI(installRequest.getWrapper().getInstallArgs(), i4);
                                i5 = i12;
                            }
                            z7 = true;
                            Pair<PackageAbiHelper.Abis, PackageAbiHelper.NativeLibraryPaths> derivePackageAbi2 = packageAbiHelper.derivePackageAbi(parsePackage, z5, z7, deriveAbiOverride, ScanPackageUtils.getAppLib32InstallDir());
                            ((PackageAbiHelper.Abis) derivePackageAbi2.first).applyTo(parsePackage);
                            ((PackageAbiHelper.NativeLibraryPaths) derivePackageAbi2.second).applyTo(parsePackage);
                            i4 = i2;
                            this.mPm.mPackageManagerServiceExt.onHandleForNotMoveInPreparePackageLI(installRequest.getWrapper().getInstallArgs(), i4);
                            i5 = i12;
                        } catch (PackageManagerException e2) {
                            Slog.e("PackageManager", "Error deriving application ABI", e2);
                            throw PrepareFailure.ofInternalError("Error deriving application ABI: " + e2.getMessage(), -4);
                        }
                    }
                    if (!z4) {
                        doRenameLI(installRequest2, parsePackage);
                        try {
                            setUpFsVerity(parsePackage);
                        } catch (Installer.InstallerException | IOException | DigestException | NoSuchAlgorithmException e3) {
                            throw PrepareFailure.ofInternalError("Failed to set up verity: " + e3, -5);
                        }
                    } else {
                        parsePackage.setPath(installRequest.getApexInfo().modulePath);
                        parsePackage.setBaseApkPath(installRequest.getApexInfo().modulePath);
                    }
                    int i13 = i;
                    int i14 = i4;
                    PackageFreezer freezePackageForInstall = freezePackageForInstall(packageName, -1, i14, "installPackageLI", 16);
                    try {
                    } catch (Throwable th3) {
                        th = th3;
                    }
                    if (z3) {
                        try {
                            String packageName3 = parsePackage.getPackageName();
                            synchronized (this.mPm.mLock) {
                                try {
                                    packageLPr2 = this.mPm.mSettings.getPackageLPr(packageName3);
                                } catch (Throwable th4) {
                                    th = th4;
                                    while (true) {
                                        try {
                                            break;
                                        } catch (Throwable th5) {
                                            th = th5;
                                        }
                                    }
                                    throw th;
                                }
                            }
                            AndroidPackage androidPackage3 = packageLPr2.getAndroidPackage();
                            if (parsePackage.isStaticSharedLibrary() && androidPackage3 != null && (i4 & 32) == 0) {
                                try {
                                    throw new PrepareFailure(-5, "Packages declaring static-shared libs cannot be updated");
                                } catch (Throwable th6) {
                                    th = th6;
                                    packageFreezer2 = freezePackageForInstall;
                                }
                            } else {
                                boolean z20 = (i5 & 8192) != 0;
                                PackageManagerService packageManagerService4 = this.mPm;
                                packageManagerService4.mPackageManagerServiceExt.beforePrepareForReplaceInPreparePackageLI(packageManagerService4.mPackages.get(packageName3));
                                try {
                                    synchronized (this.mPm.mLock) {
                                        try {
                                            if (PackageManagerService.DEBUG_INSTALL) {
                                                try {
                                                    Slog.d("PackageManager", "replacePackageLI: new=" + parsePackage + ", old=" + androidPackage3);
                                                } catch (Throwable th7) {
                                                    th2 = th7;
                                                    throw th2;
                                                }
                                            }
                                            packageLPr3 = this.mPm.mSettings.getPackageLPr(packageName3);
                                            PackageSetting disabledSystemPkgLPr = this.mPm.mSettings.getDisabledSystemPkgLPr(packageLPr3);
                                            SharedUserSetting sharedUserSettingLPr2 = this.mPm.mSettings.getSharedUserSettingLPr(packageLPr3);
                                            packageSetting = disabledSystemPkgLPr;
                                            KeySetManagerService keySetManagerService2 = this.mPm.mSettings.getKeySetManagerService();
                                            if (keySetManagerService2.shouldCheckUpgradeKeySetLocked(packageLPr3, sharedUserSettingLPr2, i5)) {
                                                if (!keySetManagerService2.checkUpgradeKeySetLocked(packageLPr3, parsePackage)) {
                                                    throw new PrepareFailure(-7, "New package not signed by keys specified by upgrade-keysets: " + packageName3);
                                                }
                                                r19 = freezePackageForInstall;
                                            } else {
                                                SigningDetails signingDetails2 = parsePackage.getSigningDetails();
                                                SigningDetails signingDetails3 = androidPackage3.getSigningDetails();
                                                PackageFreezer packageFreezer5 = freezePackageForInstall;
                                                try {
                                                    r19 = packageFreezer5;
                                                    if (!signingDetails2.checkCapability(signingDetails3, 1)) {
                                                        try {
                                                            r19 = packageFreezer5;
                                                            if (!signingDetails3.checkCapability(signingDetails2, 8)) {
                                                                if (!z6 || !signingDetails3.hasAncestorOrSelf(signingDetails2)) {
                                                                    throw new PrepareFailure(-7, "New package has a different signature: " + packageName3);
                                                                }
                                                                r19 = packageFreezer5;
                                                            }
                                                        } catch (Throwable th8) {
                                                            th2 = th8;
                                                            throw th2;
                                                        }
                                                    }
                                                } catch (Throwable th9) {
                                                    th = th9;
                                                    th2 = th;
                                                    throw th2;
                                                }
                                            }
                                        } catch (Throwable th10) {
                                            th = th10;
                                        }
                                        try {
                                            if (androidPackage3.getRestrictUpdateHash() == null || !packageLPr2.isSystem()) {
                                                i7 = i13;
                                            } else {
                                                try {
                                                    MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
                                                    updateDigest(messageDigest, new File(parsePackage.getBaseApkPath()));
                                                    if (ArrayUtils.isEmpty(parsePackage.getSplitCodePaths())) {
                                                        i7 = i13;
                                                    } else {
                                                        String[] splitCodePaths = parsePackage.getSplitCodePaths();
                                                        int length = splitCodePaths.length;
                                                        i7 = i13;
                                                        int i15 = 0;
                                                        while (i15 < length) {
                                                            updateDigest(messageDigest, new File(splitCodePaths[i15]));
                                                            i15++;
                                                            length = length;
                                                            splitCodePaths = splitCodePaths;
                                                        }
                                                    }
                                                    if (!Arrays.equals(androidPackage3.getRestrictUpdateHash(), messageDigest.digest())) {
                                                        throw new PrepareFailure(-2, "New package fails restrict-update check: " + packageName3);
                                                    }
                                                    parsePackage.setRestrictUpdateHash(androidPackage3.getRestrictUpdateHash());
                                                } catch (IOException | NoSuchAlgorithmException unused) {
                                                    throw new PrepareFailure(-2, "Could not compute hash: " + packageName3);
                                                }
                                            }
                                            String sharedUserId = androidPackage3.getSharedUserId() != null ? androidPackage3.getSharedUserId() : "<nothing>";
                                            String sharedUserId2 = parsePackage.getSharedUserId() != null ? parsePackage.getSharedUserId() : "<nothing>";
                                            if (!sharedUserId.equals(sharedUserId2)) {
                                                throw new PrepareFailure(-24, "Package " + parsePackage.getPackageName() + " shared user changed from " + sharedUserId + " to " + sharedUserId2);
                                            }
                                            if (androidPackage3.isLeavingSharedUser() && !parsePackage.isLeavingSharedUser()) {
                                                throw new PrepareFailure(-24, "Package " + parsePackage.getPackageName() + " attempting to rejoin " + sharedUserId2);
                                            }
                                            int[] userIds = this.mPm.mUserManager.getUserIds();
                                            try {
                                                int[] queryInstalledUsers = packageLPr3.queryInstalledUsers(userIds, true);
                                                int[] queryInstalledUsers2 = packageLPr3.queryInstalledUsers(userIds, false);
                                                if (z20) {
                                                    if (installRequest.getUserId() == -1) {
                                                        int length2 = userIds.length;
                                                        int i16 = 0;
                                                        while (i16 < length2) {
                                                            int i17 = length2;
                                                            int i18 = userIds[i16];
                                                            if (!packageLPr3.getInstantApp(i18)) {
                                                                Slog.w("PackageManager", "Can't replace full app with instant app: " + packageName3 + " for user: " + i18);
                                                                throw new PrepareFailure(-116);
                                                            }
                                                            i16++;
                                                            length2 = i17;
                                                        }
                                                    } else if (!packageLPr3.getInstantApp(installRequest.getUserId())) {
                                                        Slog.w("PackageManager", "Can't replace full app with instant app: " + packageName3 + " for user: " + installRequest.getUserId());
                                                        throw new PrepareFailure(-116);
                                                    }
                                                }
                                                try {
                                                    PackageRemovedInfo packageRemovedInfo = new PackageRemovedInfo(this.mPm);
                                                    packageRemovedInfo.mUid = androidPackage3.getUid();
                                                    packageRemovedInfo.mRemovedPackage = androidPackage3.getPackageName();
                                                    packageRemovedInfo.mInstallerPackageName = packageLPr3.getInstallSource().mInstallerPackageName;
                                                    packageRemovedInfo.mIsStaticSharedLib = parsePackage.getStaticSharedLibraryName() != null;
                                                    packageRemovedInfo.mIsUpdate = true;
                                                    packageRemovedInfo.mOrigUsers = queryInstalledUsers;
                                                    packageRemovedInfo.mInstallReasons = new SparseIntArray(queryInstalledUsers.length);
                                                    for (int i19 : queryInstalledUsers) {
                                                        try {
                                                            packageRemovedInfo.mInstallReasons.put(i19, packageLPr3.getInstallReason(i19));
                                                        } catch (Throwable th11) {
                                                            th = th11;
                                                            packageFreezer2 = r19;
                                                        }
                                                    }
                                                    packageRemovedInfo.mUninstallReasons = new SparseIntArray(queryInstalledUsers2.length);
                                                    for (int i20 : queryInstalledUsers2) {
                                                        packageRemovedInfo.mUninstallReasons.put(i20, packageLPr3.getUninstallReason(i20));
                                                    }
                                                    packageRemovedInfo.mIsExternal = androidPackage3.isExternalStorage();
                                                    packageRemovedInfo.mRemovedPackageVersionCode = androidPackage3.getLongVersionCode();
                                                    installRequest2.setRemovedInfo(packageRemovedInfo);
                                                    boolean isSystem2 = packageLPr2.isSystem();
                                                    if (isSystem2) {
                                                        i5 = i5 | 65536 | (packageLPr2.isPrivileged() ? 131072 : 0) | (packageLPr2.isOem() ? DumpState.DUMP_DOMAIN_PREFERRED : 0) | (packageLPr2.isVendor() ? 524288 : 0) | (packageLPr2.isProduct() ? 1048576 : 0) | (packageLPr2.isOdm() ? DumpState.DUMP_CHANGES : 0) | (packageLPr2.isSystemExt() ? 2097152 : 0);
                                                        if (PackageManagerService.DEBUG_INSTALL) {
                                                            Slog.d("PackageManager", "replaceSystemPackageLI: new=" + parsePackage + ", old=" + androidPackage3);
                                                        }
                                                        installRequest2.setReturnCode(1);
                                                        installRequest2.setApexModuleName(packageLPr2.getApexModuleName());
                                                    } else if (PackageManagerService.DEBUG_INSTALL) {
                                                        Slog.d("PackageManager", "replaceNonSystemPackageLI: new=" + parsePackage + ", old=" + androidPackage3);
                                                    }
                                                    z8 = isSystem2;
                                                    androidPackage = androidPackage3;
                                                    i8 = i5;
                                                    packageSetting2 = packageLPr3;
                                                    packageFreezer3 = r19;
                                                } catch (Throwable th12) {
                                                    th = th12;
                                                    i14 = 1;
                                                    th = th;
                                                    i6 = i14;
                                                    packageFreezer = r19;
                                                    installRequest2.setFreezer(packageFreezer);
                                                    if (i6 != 0) {
                                                    }
                                                }
                                            } catch (Throwable th13) {
                                                th = th13;
                                                th2 = th;
                                                throw th2;
                                            }
                                        } catch (Throwable th14) {
                                            th = th14;
                                            th2 = th;
                                            throw th2;
                                        }
                                    }
                                } catch (Throwable th15) {
                                    th = th15;
                                }
                            }
                            i6 = 1;
                            packageFreezer = packageFreezer2;
                        } catch (Throwable th16) {
                            th = th16;
                            i6 = 1;
                            packageFreezer = freezePackageForInstall;
                        }
                        installRequest2.setFreezer(packageFreezer);
                        if (i6 != 0) {
                            packageFreezer.close();
                            throw th;
                        }
                        throw th;
                    }
                    i7 = i13;
                    PackageFreezer packageFreezer6 = freezePackageForInstall;
                    try {
                        String packageName4 = parsePackage.getPackageName();
                        packageFreezer4 = "installPackageLI";
                        if (PackageManagerService.DEBUG_INSTALL) {
                            Slog.d("PackageManager", "installNewPackageLI: " + parsePackage);
                            packageFreezer4 = "installNewPackageLI: ";
                        }
                        try {
                            synchronized (this.mPm.mLock) {
                                try {
                                    String renamedPackageLPr2 = this.mPm.mSettings.getRenamedPackageLPr(packageName4);
                                    if (renamedPackageLPr2 != null) {
                                        throw new PrepareFailure(-1, "Attempt to re-install " + packageName4 + " without first uninstalling package running as " + renamedPackageLPr2);
                                    }
                                    if (this.mPm.mPackages.containsKey(packageName4)) {
                                        throw new PrepareFailure(-1, "Attempt to re-install " + packageName4 + " without first uninstalling.");
                                    }
                                } catch (Throwable th17) {
                                    th = th17;
                                    packageFreezer4 = packageFreezer6;
                                    throw th;
                                }
                            }
                        } catch (Throwable th18) {
                            th = th18;
                        }
                    } catch (Throwable th19) {
                        th = th19;
                        packageFreezer4 = packageFreezer6;
                    }
                    try {
                        throw th;
                    } catch (Throwable th20) {
                        th = th20;
                        th = th;
                        i6 = 1;
                        packageFreezer = packageFreezer4;
                        installRequest2.setFreezer(packageFreezer);
                        if (i6 != 0) {
                        }
                    }
                    try {
                        installRequest.setPrepareResult(z3, i8, i7, androidPackage, parsePackage, z3, z8, packageSetting2, packageSetting);
                        installRequest2.setFreezer(packageFreezer3);
                    } catch (Throwable th21) {
                        packageFreezer = packageFreezer3;
                        th = th21;
                        i6 = 0;
                    }
                } finally {
                }
            } catch (PackageManagerException e4) {
                throw new PrepareFailure("Failed parse during installPackageLI", e4);
            }
        } catch (Throwable th22) {
            Trace.traceEnd(262144L);
            throw th22;
        }
    }

    @GuardedBy({"mPm.mInstallLock"})
    private void doRenameLI(InstallRequest installRequest, ParsedPackage parsedPackage) throws PrepareFailure {
        int returnCode = installRequest.getReturnCode();
        String returnMsg = installRequest.getReturnMsg();
        if (installRequest.isInstallMove()) {
            if (returnCode == 1) {
                return;
            }
            this.mRemovePackageHelper.cleanUpForMoveInstall(installRequest.getMoveToUuid(), installRequest.getMovePackageName(), installRequest.getMoveFromCodePath());
            throw new PrepareFailure(returnCode, returnMsg);
        }
        if (returnCode != 1) {
            this.mRemovePackageHelper.removeCodePath(installRequest.getCodeFile());
            throw new PrepareFailure(returnCode, returnMsg);
        }
        File resolveTargetDir = resolveTargetDir(installRequest.getInstallFlags(), installRequest.getCodeFile());
        File codeFile = installRequest.getCodeFile();
        File nextCodePath = PackageManagerServiceUtils.getNextCodePath(resolveTargetDir, parsedPackage.getPackageName());
        if (PackageManagerService.DEBUG_INSTALL) {
            Slog.d("PackageManager", "Renaming " + codeFile + " to " + nextCodePath);
        }
        boolean z = this.mPm.mIncrementalManager != null && IncrementalManager.isIncrementalPath(codeFile.getAbsolutePath());
        try {
            PackageManagerServiceUtils.makeDirRecursive(nextCodePath.getParentFile(), 505);
            if (z) {
                this.mPm.mIncrementalManager.linkCodePath(codeFile, nextCodePath);
            } else {
                Os.rename(codeFile.getAbsolutePath(), nextCodePath.getAbsolutePath());
            }
            if (!z && !SELinux.restoreconRecursive(nextCodePath)) {
                Slog.w("PackageManager", "Failed to restorecon");
                throw new PrepareFailure(-20, "Failed to restorecon");
            }
            installRequest.setCodeFile(nextCodePath);
            try {
                parsedPackage.setPath(nextCodePath.getCanonicalPath());
                parsedPackage.setBaseApkPath(FileUtils.rewriteAfterRename(codeFile, nextCodePath, parsedPackage.getBaseApkPath()));
                parsedPackage.setSplitCodePaths(FileUtils.rewriteAfterRename(codeFile, nextCodePath, parsedPackage.getSplitCodePaths()));
            } catch (IOException e) {
                Slog.e("PackageManager", "Failed to get path: " + nextCodePath, e);
                throw new PrepareFailure(-20, "Failed to get path: " + nextCodePath);
            }
        } catch (ErrnoException | IOException e2) {
            Slog.w("PackageManager", "Failed to rename", e2);
            throw new PrepareFailure(-4, "Failed to rename");
        }
    }

    private File resolveTargetDir(int i, File file) {
        if ((2097152 & i) != 0) {
            return Environment.getDataAppDirectory(null);
        }
        return file.getParentFile();
    }

    private static boolean cannotInstallWithBadPermissionGroups(ParsedPackage parsedPackage) {
        return parsedPackage.getTargetSdkVersion() >= 31;
    }

    private boolean doesSignatureMatchForPermissions(String str, ParsedPackage parsedPackage, int i) {
        PackageSetting packageLPr;
        KeySetManagerService keySetManagerService;
        SharedUserSetting sharedUserSettingLPr;
        synchronized (this.mPm.mLock) {
            packageLPr = this.mPm.mSettings.getPackageLPr(str);
            keySetManagerService = this.mPm.mSettings.getKeySetManagerService();
            sharedUserSettingLPr = this.mPm.mSettings.getSharedUserSettingLPr(packageLPr);
        }
        SigningDetails signingDetails = packageLPr == null ? SigningDetails.UNKNOWN : packageLPr.getSigningDetails();
        if (str.equals(parsedPackage.getPackageName()) && keySetManagerService.shouldCheckUpgradeKeySetLocked(packageLPr, sharedUserSettingLPr, i)) {
            return keySetManagerService.checkUpgradeKeySetLocked(packageLPr, parsedPackage);
        }
        if (signingDetails.checkCapability(parsedPackage.getSigningDetails(), 4)) {
            return true;
        }
        if (!parsedPackage.getSigningDetails().checkCapability(signingDetails, 4)) {
            return false;
        }
        synchronized (this.mPm.mLock) {
            packageLPr.setSigningDetails(parsedPackage.getSigningDetails());
        }
        return true;
    }

    private void setUpFsVerity(AndroidPackage androidPackage) throws Installer.InstallerException, PrepareFailure, IOException, DigestException, NoSuchAlgorithmException {
        if (PackageManagerServiceUtils.isApkVerityEnabled()) {
            if (!IncrementalManager.isIncrementalPath(androidPackage.getPath()) || IncrementalManager.getVersion() >= 2) {
                ArrayMap arrayMap = new ArrayMap();
                arrayMap.put(androidPackage.getBaseApkPath(), VerityUtils.getFsveritySignatureFilePath(androidPackage.getBaseApkPath()));
                String buildDexMetadataPathForApk = DexMetadataHelper.buildDexMetadataPathForApk(androidPackage.getBaseApkPath());
                if (new File(buildDexMetadataPathForApk).exists()) {
                    arrayMap.put(buildDexMetadataPathForApk, VerityUtils.getFsveritySignatureFilePath(buildDexMetadataPathForApk));
                }
                for (String str : androidPackage.getSplitCodePaths()) {
                    arrayMap.put(str, VerityUtils.getFsveritySignatureFilePath(str));
                    String buildDexMetadataPathForApk2 = DexMetadataHelper.buildDexMetadataPathForApk(str);
                    if (new File(buildDexMetadataPathForApk2).exists()) {
                        arrayMap.put(buildDexMetadataPathForApk2, VerityUtils.getFsveritySignatureFilePath(buildDexMetadataPathForApk2));
                    }
                }
                FileIntegrityService service = FileIntegrityService.getService();
                for (Map.Entry entry : arrayMap.entrySet()) {
                    try {
                        String str2 = (String) entry.getKey();
                        if (!VerityUtils.hasFsverity(str2)) {
                            String str3 = (String) entry.getValue();
                            if (new File(str3).exists()) {
                                VerityUtils.setUpFsverity(str2);
                                if (!service.verifyPkcs7DetachedSignature(str3, str2)) {
                                    throw new PrepareFailure(-118, "fs-verity signature does not verify against a known key");
                                }
                            } else {
                                continue;
                            }
                        }
                    } catch (IOException e) {
                        throw new PrepareFailure(-118, "Failed to enable fs-verity: " + e);
                    }
                }
            }
        }
    }

    private PackageFreezer freezePackageForInstall(String str, int i, int i2, String str2, int i3) {
        if ((i2 & 4096) != 0) {
            return new PackageFreezer(this.mPm);
        }
        return this.mPm.freezePackage(str, i, str2, i3);
    }

    private static void updateDigest(MessageDigest messageDigest, File file) throws IOException {
        DigestInputStream digestInputStream = new DigestInputStream(new FileInputStream(file), messageDigest);
        do {
            try {
            } catch (Throwable th) {
                try {
                    digestInputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } while (digestInputStream.read() != -1);
        digestInputStream.close();
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0145  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0182  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0190 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00da  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0125  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0127  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00fb  */
    @GuardedBy({"mPm.mLock"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void commitPackagesLocked(List<ReconciledPackage> list, int[] iArr) {
        boolean z;
        PackageSetting packageLPr;
        AndroidPackage androidPackage;
        PackageSetting packageLPr2;
        for (ReconciledPackage reconciledPackage : list) {
            InstallRequest installRequest = reconciledPackage.mInstallRequest;
            ParsedPackage parsedPackage = installRequest.getParsedPackage();
            String packageName = parsedPackage.getPackageName();
            RemovePackageHelper removePackageHelper = new RemovePackageHelper(this.mPm);
            DeletePackageHelper deletePackageHelper = new DeletePackageHelper(this.mPm);
            installRequest.onCommitStarted();
            if (installRequest.isInstallReplace()) {
                AndroidPackage androidPackage2 = this.mPm.mPackages.get(packageName);
                PackageStateInternal packageStateInternal = this.mPm.snapshotComputer().getPackageStateInternal(androidPackage2.getPackageName());
                installRequest.setScannedPackageSettingFirstInstallTimeFromReplaced(packageStateInternal, iArr);
                installRequest.setScannedPackageSettingLastUpdateTime(System.currentTimeMillis());
                PackageRemovedInfo removedInfo = installRequest.getRemovedInfo();
                PackageManagerService packageManagerService = this.mPm;
                removedInfo.mBroadcastAllowList = packageManagerService.mAppsFilter.getVisibilityAllowList(packageManagerService.snapshotComputer(), installRequest.getScannedPackageSetting(), iArr, this.mPm.mSettings.getPackagesLocked());
                if (installRequest.isInstallSystem()) {
                    removePackageHelper.removePackage(androidPackage2, true);
                    if (!disableSystemPackageLPw(androidPackage2)) {
                        installRequest.getRemovedInfo().mArgs = new InstallArgs(androidPackage2.getPath(), InstructionSets.getAppDexInstructionSets(packageStateInternal.getPrimaryCpuAbi(), packageStateInternal.getSecondaryCpuAbi()));
                    } else {
                        installRequest.getRemovedInfo().mArgs = null;
                    }
                    z = true;
                } else {
                    try {
                        androidPackage = androidPackage2;
                        z = true;
                    } catch (SystemDeleteException e) {
                        e = e;
                        androidPackage = androidPackage2;
                        z = true;
                    }
                    try {
                        deletePackageHelper.executeDeletePackage(reconciledPackage.mDeletePackageAction, packageName, true, iArr, false);
                    } catch (SystemDeleteException e2) {
                        e = e2;
                        if (this.mPm.mIsEngBuild) {
                            throw new RuntimeException("Unexpected failure", e);
                        }
                        PackageSetting packageLPr3 = this.mPm.mSettings.getPackageLPr(installRequest.getExistingPackageName());
                        if ((installRequest.getInstallFlags() & (z ? 1 : 0)) != 0) {
                        }
                        if (installRequest.getReturnCode() == z) {
                            installRequest.getRemovedInfo().mRemovedForAllUsers = this.mPm.mPackages.get(packageLPr2.getPackageName()) != null ? z ? 1 : 0 : false;
                        }
                        updateSettingsLI(commitReconciledScanResultLocked(reconciledPackage, iArr), iArr, installRequest);
                        packageLPr = this.mPm.mSettings.getPackageLPr(packageName);
                        if (packageLPr != null) {
                        }
                        if (installRequest.getReturnCode() != z) {
                        }
                        installRequest.onCommitFinished();
                    }
                    PackageSetting packageLPr32 = this.mPm.mSettings.getPackageLPr(installRequest.getExistingPackageName());
                    if ((installRequest.getInstallFlags() & (z ? 1 : 0)) != 0) {
                        Set<String> oldCodePaths = packageLPr32.getOldCodePaths();
                        if (oldCodePaths == null) {
                            oldCodePaths = new ArraySet<>();
                        }
                        Collections.addAll(oldCodePaths, androidPackage.getBaseApkPath());
                        Collections.addAll(oldCodePaths, androidPackage.getSplitCodePaths());
                        packageLPr32.setOldCodePaths(oldCodePaths);
                    } else {
                        packageLPr32.setOldCodePaths(null);
                    }
                    if (installRequest.getReturnCode() == z && (packageLPr2 = this.mPm.mSettings.getPackageLPr(parsedPackage.getPackageName())) != null) {
                        installRequest.getRemovedInfo().mRemovedForAllUsers = this.mPm.mPackages.get(packageLPr2.getPackageName()) != null ? z ? 1 : 0 : false;
                    }
                }
            } else {
                z = true;
            }
            updateSettingsLI(commitReconciledScanResultLocked(reconciledPackage, iArr), iArr, installRequest);
            packageLPr = this.mPm.mSettings.getPackageLPr(packageName);
            if (packageLPr != null) {
                installRequest.setNewUsers(packageLPr.queryInstalledUsers(this.mPm.mUserManager.getUserIds(), z));
                packageLPr.setUpdateAvailable(false);
                File file = new File(packageLPr.getPath(), "app.metadata");
                if (file.exists()) {
                    packageLPr.setAppMetadataFilePath(file.getAbsolutePath());
                } else {
                    packageLPr.setAppMetadataFilePath(null);
                }
                this.mPm.mPackageManagerServiceExt.notifyPackageAddOrUpdateForAbiInfo(packageName, packageLPr);
            }
            if (installRequest.getReturnCode() != z) {
                this.mPm.updateSequenceNumberLP(packageLPr, installRequest.getNewUsers());
                this.mPm.updateInstantAppInstallerLocked(packageName);
            }
            installRequest.onCommitFinished();
        }
        ApplicationPackageManager.invalidateGetPackagesForUidCache();
    }

    @GuardedBy({"mPm.mLock"})
    private boolean disableSystemPackageLPw(AndroidPackage androidPackage) {
        return this.mPm.mSettings.disableSystemPackageLPw(androidPackage.getPackageName(), true);
    }

    private void updateSettingsLI(AndroidPackage androidPackage, int[] iArr, InstallRequest installRequest) {
        updateSettingsInternalLI(androidPackage, iArr, installRequest);
    }

    /* JADX WARN: Removed duplicated region for block: B:166:0x019a A[Catch: all -> 0x033f, TryCatch #0 {, blocks: (B:7:0x004b, B:9:0x0059, B:11:0x0061, B:13:0x0065, B:15:0x007d, B:17:0x0083, B:19:0x0087, B:23:0x0090, B:24:0x008d, B:29:0x0097, B:31:0x009b, B:33:0x00a5, B:35:0x00c8, B:40:0x00d6, B:42:0x00da, B:44:0x00e2, B:46:0x00f0, B:47:0x00fc, B:49:0x0102, B:52:0x0114, B:55:0x013c, B:57:0x0122, B:60:0x0131, B:65:0x0146, B:67:0x0150, B:68:0x01a1, B:70:0x01b7, B:72:0x01bf, B:74:0x01cc, B:76:0x01ed, B:78:0x01f3, B:81:0x01fc, B:83:0x0208, B:85:0x0222, B:87:0x022d, B:89:0x0231, B:91:0x023d, B:93:0x0243, B:95:0x0246, B:99:0x0256, B:101:0x0260, B:103:0x0264, B:104:0x0272, B:106:0x0276, B:108:0x027e, B:110:0x0284, B:114:0x0287, B:118:0x02a1, B:119:0x02ab, B:121:0x02b1, B:123:0x02c2, B:124:0x02cf, B:128:0x02dd, B:130:0x02e8, B:131:0x02eb, B:133:0x0308, B:135:0x030f, B:136:0x02e2, B:138:0x02c6, B:140:0x02cc, B:142:0x0249, B:144:0x0253, B:146:0x0157, B:148:0x015b, B:152:0x019e, B:153:0x0168, B:155:0x0177, B:162:0x018b, B:164:0x0190, B:166:0x019a, B:171:0x0316, B:172:0x033a), top: B:6:0x004b }] */
    /* JADX WARN: Removed duplicated region for block: B:168:0x019e A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateSettingsInternalLI(AndroidPackage androidPackage, int[] iArr, InstallRequest installRequest) {
        boolean z;
        List<String> allowlistedRestrictedPermissions;
        IncrementalManager incrementalManager;
        PackageSetting packageLPr;
        int i;
        Trace.traceBegin(262144L, "updateSettings");
        String packageName = androidPackage.getPackageName();
        int[] originUsers = installRequest.getOriginUsers();
        int installReason = installRequest.getInstallReason();
        String installerPackageName = installRequest.getInstallerPackageName();
        if (PackageManagerService.DEBUG_INSTALL) {
            Slog.d("PackageManager", "New package installed in " + androidPackage.getPath());
        }
        this.mPm.mPackageManagerServiceSocExt.acquireUxPerfLockPkgInstall(packageName);
        synchronized (this.mPm.mLock) {
            PackageSetting packageLPr2 = this.mPm.mSettings.getPackageLPr(packageName);
            int userId = installRequest.getUserId();
            if (packageLPr2 != null) {
                if (packageLPr2.isSystem()) {
                    if (PackageManagerService.DEBUG_INSTALL) {
                        Slog.d("PackageManager", "Implicitly enabling system package on upgrade: " + packageName);
                    }
                    if (originUsers != null && !installRequest.isApplicationEnabledSettingPersistent()) {
                        for (int i2 : originUsers) {
                            if (userId == -1 || userId == i2) {
                                packageLPr2.setEnabled(0, i2, installerPackageName);
                            }
                        }
                    }
                    if (iArr != null && originUsers != null) {
                        int length = iArr.length;
                        int i3 = 0;
                        while (i3 < length) {
                            int i4 = iArr[i3];
                            boolean contains = ArrayUtils.contains(originUsers, i4);
                            if (PackageManagerService.DEBUG_INSTALL) {
                                i = length;
                                Slog.d("PackageManager", "    user " + i4 + " => " + contains);
                            } else {
                                i = length;
                            }
                            packageLPr2.setInstalled(contains, i4);
                            i3++;
                            length = i;
                        }
                    }
                    if (iArr != null) {
                        for (int i5 : iArr) {
                            packageLPr2.resetOverrideComponentLabelIcon(i5);
                        }
                    }
                }
                if (!packageLPr2.getPkgState().getUsesLibraryInfos().isEmpty()) {
                    Iterator<SharedLibraryWrapper> it = packageLPr2.getPkgState().getUsesLibraryInfos().iterator();
                    while (it.hasNext()) {
                        SharedLibraryWrapper next = it.next();
                        int[] userIds = UserManagerService.getInstance().getUserIds();
                        int length2 = userIds.length;
                        int i6 = 0;
                        while (i6 < length2) {
                            int i7 = userIds[i6];
                            Iterator<SharedLibraryWrapper> it2 = it;
                            int[] iArr2 = userIds;
                            if (next.getType() == 1 && (packageLPr = this.mPm.mSettings.getPackageLPr(next.getPackageName())) != null) {
                                packageLPr2.setOverlayPathsForLibrary(next.getName(), packageLPr.getOverlayPaths(i7), i7);
                            }
                            i6++;
                            it = it2;
                            userIds = iArr2;
                        }
                    }
                }
                if (userId != -1) {
                    packageLPr2.setInstalled(true, userId);
                    if (!installRequest.isApplicationEnabledSettingPersistent()) {
                        packageLPr2.setEnabled(0, userId, installerPackageName);
                    }
                } else if (iArr != null) {
                    for (int i8 : iArr) {
                        if (!this.mPm.mPackageManagerServiceExt.skipInstallInMultiUser(i8, packageName)) {
                            boolean contains2 = ArrayUtils.contains(originUsers, i8);
                            if (!this.mPm.isUserRestricted(i8, "no_install_apps") && !this.mPm.isUserRestricted(i8, "no_debugging_features")) {
                                z = false;
                                if (!contains2 && z) {
                                    packageLPr2.setInstalled(false, i8);
                                }
                                packageLPr2.setInstalled(true, i8);
                                if (installRequest.isApplicationEnabledSettingPersistent()) {
                                    packageLPr2.setEnabled(0, i8, installerPackageName);
                                }
                            }
                            z = true;
                            if (!contains2) {
                                packageLPr2.setInstalled(false, i8);
                            }
                            packageLPr2.setInstalled(true, i8);
                            if (installRequest.isApplicationEnabledSettingPersistent()) {
                            }
                        }
                    }
                }
                this.mPm.mSettings.addInstallerPackageNames(packageLPr2.getInstallSource());
                ArraySet arraySet = new ArraySet();
                if (installRequest.getRemovedInfo() != null && installRequest.getRemovedInfo().mInstallReasons != null) {
                    int size = installRequest.getRemovedInfo().mInstallReasons.size();
                    for (int i9 = 0; i9 < size; i9++) {
                        int keyAt = installRequest.getRemovedInfo().mInstallReasons.keyAt(i9);
                        packageLPr2.setInstallReason(installRequest.getRemovedInfo().mInstallReasons.valueAt(i9), keyAt);
                        arraySet.add(Integer.valueOf(keyAt));
                    }
                }
                if (installRequest.getRemovedInfo() != null && installRequest.getRemovedInfo().mUninstallReasons != null) {
                    for (int i10 = 0; i10 < installRequest.getRemovedInfo().mUninstallReasons.size(); i10++) {
                        packageLPr2.setUninstallReason(installRequest.getRemovedInfo().mUninstallReasons.valueAt(i10), installRequest.getRemovedInfo().mUninstallReasons.keyAt(i10));
                    }
                }
                int[] userIds2 = this.mPm.mUserManager.getUserIds();
                if (userId == -1) {
                    for (int i11 : userIds2) {
                        if (!arraySet.contains(Integer.valueOf(i11)) && packageLPr2.getInstalled(i11)) {
                            packageLPr2.setInstallReason(installReason, i11);
                        }
                    }
                } else if (!arraySet.contains(Integer.valueOf(userId))) {
                    packageLPr2.setInstallReason(installReason, userId);
                }
                String pathString = packageLPr2.getPathString();
                if (IncrementalManager.isIncrementalPath(pathString) && (incrementalManager = this.mIncrementalManager) != null) {
                    incrementalManager.registerLoadingProgressCallback(pathString, new IncrementalProgressListener(packageLPr2.getPackageName(), this.mPm));
                }
                for (int i12 : userIds2) {
                    if (packageLPr2.getInstalled(i12)) {
                        packageLPr2.setUninstallReason(0, i12);
                    }
                }
                this.mPm.mSettings.writeKernelMappingLPr(packageLPr2);
                PermissionManagerServiceInternal.PackageInstalledParams.Builder builder = new PermissionManagerServiceInternal.PackageInstalledParams.Builder();
                if ((installRequest.getInstallFlags() & 256) != 0) {
                    ArrayMap<String, Integer> arrayMap = new ArrayMap<>();
                    List<String> requestedPermissions = androidPackage.getRequestedPermissions();
                    for (int i13 = 0; i13 < requestedPermissions.size(); i13++) {
                        arrayMap.put(requestedPermissions.get(i13), 1);
                    }
                    builder.setPermissionStates(arrayMap);
                } else {
                    ArrayMap<String, Integer> permissionStates = installRequest.getPermissionStates();
                    if (permissionStates != null) {
                        builder.setPermissionStates(permissionStates);
                    }
                }
                if ((installRequest.getInstallFlags() & DumpState.DUMP_CHANGES) != 0) {
                    allowlistedRestrictedPermissions = androidPackage.getRequestedPermissions();
                } else {
                    allowlistedRestrictedPermissions = installRequest.getAllowlistedRestrictedPermissions();
                }
                if (allowlistedRestrictedPermissions != null) {
                    builder.setAllowlistedRestrictedPermissions(allowlistedRestrictedPermissions);
                }
                builder.setAutoRevokePermissionsMode(installRequest.getAutoRevokePermissionsMode());
                this.mPm.mPermissionManager.onPackageInstalled(androidPackage, installRequest.getPreviousAppId(), builder.build(), userId);
                if (installRequest.getPackageSource() == 3 || installRequest.getPackageSource() == 4) {
                    enableRestrictedSettings(packageName, androidPackage.getUid());
                }
            }
            installRequest.setName(packageName);
            installRequest.setAppId(androidPackage.getUid());
            installRequest.setPkg(androidPackage);
            installRequest.setReturnCode(1);
            Trace.traceBegin(262144L, "writeSettings");
            this.mPm.writeSettingsLPrTEMP();
            Trace.traceEnd(262144L);
        }
        Trace.traceEnd(262144L);
    }

    private void enableRestrictedSettings(String str, int i) {
        AppOpsManager appOpsManager = (AppOpsManager) this.mPm.mContext.getSystemService(AppOpsManager.class);
        for (int i2 : this.mPm.mUserManager.getUserIds()) {
            appOpsManager.setMode(119, UserHandle.getUid(i2, i), str, 2);
        }
    }

    @GuardedBy({"mPm.mInstallLock"})
    private void executePostCommitStepsLIF(List<ReconciledPackage> list) {
        boolean z;
        String str;
        long j;
        ArraySet arraySet = new ArraySet();
        for (ReconciledPackage reconciledPackage : list) {
            InstallRequest installRequest = reconciledPackage.mInstallRequest;
            boolean z2 = (installRequest.getScanFlags() & 8192) != 0;
            boolean z3 = (installRequest.getScanFlags() & 67108864) != 0;
            AndroidPackageInternal pkg = installRequest.getScannedPackageSetting().getPkg();
            String packageName = pkg.getPackageName();
            String path = pkg.getPath();
            boolean z4 = this.mIncrementalManager != null && IncrementalManager.isIncrementalPath(path);
            if (z4) {
                IncrementalStorage openStorage = this.mIncrementalManager.openStorage(path);
                if (openStorage == null) {
                    throw new IllegalArgumentException("Install: null storage for incremental package " + packageName);
                }
                arraySet.add(openStorage);
            }
            this.mAppDataHelper.prepareAppDataPostCommitLIF(pkg, 0);
            if (installRequest.isClearCodeCache()) {
                this.mAppDataHelper.clearAppDataLIF(pkg, -1, 39);
            }
            if (installRequest.isInstallReplace()) {
                this.mDexManager.notifyPackageUpdated(pkg.getPackageName(), pkg.getBaseApkPath(), pkg.getSplitCodePaths());
                this.mPm.mPackageManagerServiceExt.afterNotifyUpdateForDexInExecutePostCommitSteps(pkg);
            }
            if (!DexOptHelper.useArtService()) {
                try {
                    this.mArtManagerService.prepareAppProfiles((AndroidPackage) pkg, this.mPm.resolveUserIds(installRequest.getUserId()), true);
                } catch (Installer.LegacyDexoptDisabledException e) {
                    throw new RuntimeException(e);
                }
            }
            boolean doPreWorkBeforeDexOptInExecutePostCommitSteps = this.mPm.mPackageManagerServiceExt.doPreWorkBeforeDexOptInExecutePostCommitSteps(pkg);
            DexoptOptions modifyDexoptOptionsBeforDo = this.mPm.mPackageManagerServiceExt.modifyDexoptOptionsBeforDo(reconciledPackage.mInstallRequest.getWrapper().getInstallArgs().mInstallArgsExt, new DexoptOptions(packageName, this.mDexManager.getCompilationReasonForInstallScenario(installRequest.getInstallScenario()), (installRequest.getInstallReason() == 2 || installRequest.getInstallReason() == 3 ? 2048 : 0) | UsbTerminalTypes.TERMINAL_BIDIR_SKRPHONE_CANCEL));
            if (((z2 && Settings.Global.getInt(this.mContext.getContentResolver(), "instant_app_dexopt_enabled", 0) == 0) || pkg.isDebuggable() || z4 || !modifyDexoptOptionsBeforDo.isCompilationEnabled() || z3) ? false : true) {
                if (SystemProperties.getBoolean("pm.precompile_layouts", false)) {
                    Trace.traceBegin(262144L, "compileLayouts");
                    this.mViewCompiler.compileLayouts(pkg);
                    Trace.traceEnd(262144L);
                }
                Trace.traceBegin(262144L, "dexopt");
                PackageSetting realPackageSetting = installRequest.getRealPackageSetting();
                realPackageSetting.getPkgState().setUpdatedSystemApp(installRequest.getScannedPackageSetting().isUpdatedSystemApp());
                if (DexOptHelper.useArtService()) {
                    try {
                        PackageManagerLocal.FilteredSnapshot withFilteredSnapshot = ((PackageManagerLocal) LocalManagerRegistry.getManager(PackageManagerLocal.class)).withFilteredSnapshot();
                        try {
                            installRequest.onDexoptFinished(this.mPm.mPackageManagerServiceExt.dexoptInExecutePostCommitStepsLIF(withFilteredSnapshot, packageName, modifyDexoptOptionsBeforDo.convertToDexoptParams(0)));
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
                            break;
                        }
                    } catch (IllegalStateException e2) {
                        if (Build.MTK_HBT_ON_64BIT_ONLY_CHIP && e2.getMessage().contains("Unsupported isa 'arm'")) {
                            Slog.w("PackageManager", "Dexopt with art service is conflict with hbt_translator");
                        } else if (Build.QCOM_TANGO_ON_64BIT_ONLY_CHIP && e2.getMessage().contains("Unsupported isa 'arm'")) {
                            Slog.w("PackageManager", "Dexopt with art service is conflict with tango_translator");
                        } else {
                            throw e2;
                        }
                    }
                    j = 262144;
                    z = doPreWorkBeforeDexOptInExecutePostCommitSteps;
                    str = packageName;
                } else {
                    try {
                        j = 262144;
                        z = doPreWorkBeforeDexOptInExecutePostCommitSteps;
                        str = packageName;
                        this.mPackageDexOptimizer.performDexOpt(pkg, realPackageSetting, null, this.mPm.getOrCreateCompilerPackageStats(pkg), this.mDexManager.getPackageUseInfoOrDefault(packageName), modifyDexoptOptionsBeforDo);
                    } catch (Installer.LegacyDexoptDisabledException e3) {
                        throw new RuntimeException(e3);
                    }
                }
                Trace.traceEnd(j);
            } else {
                z = doPreWorkBeforeDexOptInExecutePostCommitSteps;
                str = packageName;
            }
            this.mPm.mPackageManagerServiceExt.afterDexOptInExecutePostCommitSteps(pkg, str, z);
            if (!DexOptHelper.useArtService()) {
                try {
                    BackgroundDexOptService.getService().notifyPackageChanged(str);
                } catch (Installer.LegacyDexoptDisabledException e4) {
                    throw new RuntimeException(e4);
                }
            }
        }
        PackageManagerServiceUtils.waitForNativeBinariesExtractionForIncremental(arraySet);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Pair<Integer, String> verifyReplacingVersionCode(PackageInfoLite packageInfoLite, long j, int i) {
        if ((131072 & i) != 0) {
            return verifyReplacingVersionCodeForApex(packageInfoLite, j, i);
        }
        String str = packageInfoLite.packageName;
        synchronized (this.mPm.mLock) {
            AndroidPackage androidPackage = this.mPm.mPackages.get(str);
            PackageSetting packageLPr = this.mPm.mSettings.getPackageLPr(str);
            if (androidPackage == null && packageLPr != null) {
                androidPackage = packageLPr.getPkg();
            }
            if (j != -1) {
                if (androidPackage == null) {
                    String str2 = "Required installed version code was " + j + " but package is not installed";
                    Slog.w("PackageManager", str2);
                    return Pair.create(-121, str2);
                }
                if (androidPackage.getLongVersionCode() != j) {
                    String str3 = "Required installed version code was " + j + " but actual installed version is " + androidPackage.getLongVersionCode();
                    Slog.w("PackageManager", str3);
                    return Pair.create(-121, str3);
                }
            }
            if (androidPackage != null && !androidPackage.isSdkLibrary()) {
                if (!PackageManagerServiceUtils.isDowngradePermitted(i, androidPackage.isDebuggable())) {
                    try {
                        PackageManagerServiceUtils.checkDowngrade(androidPackage, packageInfoLite);
                    } catch (PackageManagerException e) {
                        String str4 = "Downgrade detected: " + e.getMessage();
                        Slog.w("PackageManager", str4);
                        return Pair.create(-25, str4);
                    }
                } else if (packageLPr.isSystem()) {
                    PackageSetting disabledSystemPkgLPr = this.mPm.mSettings.getDisabledSystemPkgLPr(packageLPr);
                    if (disabledSystemPkgLPr != null) {
                        androidPackage = disabledSystemPkgLPr.getPkg();
                    }
                    if (!Build.IS_DEBUGGABLE && !androidPackage.isDebuggable()) {
                        try {
                            PackageManagerServiceUtils.checkDowngrade(androidPackage, packageInfoLite);
                        } catch (PackageManagerException e2) {
                            String str5 = "System app: " + str + " cannot be downgraded to older than its preloaded version on the system image. " + e2.getMessage();
                            Slog.w("PackageManager", str5);
                            return Pair.create(-25, str5);
                        }
                    }
                }
            }
            return Pair.create(1, null);
        }
    }

    private Pair<Integer, String> verifyReplacingVersionCodeForApex(PackageInfoLite packageInfoLite, long j, int i) {
        String str = packageInfoLite.packageName;
        PackageInfo packageInfo = this.mPm.snapshotComputer().getPackageInfo(str, 1073741824L, 0);
        if (packageInfo == null) {
            String str2 = "Attempting to install new APEX package " + str;
            Slog.w("PackageManager", str2);
            return Pair.create(-23, str2);
        }
        long longVersionCode = packageInfo.getLongVersionCode();
        if (j != -1 && longVersionCode != j) {
            String str3 = "Installed version of APEX package " + str + " does not match required. Active version: " + longVersionCode + " required: " + j;
            Slog.w("PackageManager", str3);
            return Pair.create(-121, str3);
        }
        boolean z = (packageInfo.applicationInfo.flags & 2) != 0;
        long longVersionCode2 = packageInfoLite.getLongVersionCode();
        if (!PackageManagerServiceUtils.isDowngradePermitted(i, z) && longVersionCode2 < longVersionCode) {
            String str4 = "Downgrade of APEX package " + str + " is not allowed. Active version: " + longVersionCode + " attempted: " + longVersionCode2;
            Slog.w("PackageManager", str4);
            return Pair.create(-25, str4);
        }
        return Pair.create(1, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getUidForVerifier(VerifierInfo verifierInfo) {
        synchronized (this.mPm.mLock) {
            AndroidPackage androidPackage = this.mPm.mPackages.get(verifierInfo.packageName);
            if (androidPackage == null) {
                return -1;
            }
            if (androidPackage.getSigningDetails().getSignatures().length != 1) {
                Slog.i("PackageManager", "Verifier package " + verifierInfo.packageName + " has more than one signature; ignoring");
                return -1;
            }
            try {
                if (!Arrays.equals(verifierInfo.publicKey.getEncoded(), androidPackage.getSigningDetails().getSignatures()[0].getPublicKey().getEncoded())) {
                    Slog.i("PackageManager", "Verifier package " + verifierInfo.packageName + " does not have the expected public key; ignoring");
                    return -1;
                }
                return androidPackage.getUid();
            } catch (CertificateException unused) {
                return -1;
            }
        }
    }

    public void sendPendingBroadcasts() {
        synchronized (this.mPm.mLock) {
            SparseArray<ArrayMap<String, ArrayList<String>>> copiedMap = this.mPm.mPendingBroadcasts.copiedMap();
            int size = copiedMap.size();
            int i = 0;
            for (int i2 = 0; i2 < size; i2++) {
                i += copiedMap.valueAt(i2).size();
            }
            if (i == 0) {
                return;
            }
            String[] strArr = new String[i];
            ArrayList<String>[] arrayListArr = new ArrayList[i];
            int[] iArr = new int[i];
            int i3 = 0;
            for (int i4 = 0; i4 < size; i4++) {
                int keyAt = copiedMap.keyAt(i4);
                ArrayMap<String, ArrayList<String>> valueAt = copiedMap.valueAt(i4);
                int size2 = CollectionUtils.size(valueAt);
                for (int i5 = 0; i5 < size2; i5++) {
                    strArr[i3] = valueAt.keyAt(i5);
                    arrayListArr[i3] = valueAt.valueAt(i5);
                    PackageSetting packageLPr = this.mPm.mSettings.getPackageLPr(strArr[i3]);
                    iArr[i3] = packageLPr != null ? UserHandle.getUid(keyAt, packageLPr.getAppId()) : -1;
                    i3++;
                }
            }
            this.mPm.mPendingBroadcasts.clear();
            Computer snapshotComputer = this.mPm.snapshotComputer();
            for (int i6 = 0; i6 < i3; i6++) {
                this.mPm.sendPackageChangedBroadcast(snapshotComputer, strArr[i6], true, arrayListArr[i6], iArr[i6], null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:137:0x0420  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x014c  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0168  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void handlePackagePostInstall(InstallRequest installRequest, boolean z) {
        boolean z2;
        int i;
        boolean z3;
        String str;
        String str2;
        String str3;
        String str4;
        InstallArgs installArgs;
        SparseArray<int[]> visibilityAllowList;
        int packageExternalStorageType;
        PackageStateInternal packageStateInternal;
        int[] iArr;
        boolean z4;
        boolean z5;
        boolean z6 = (installRequest.getInstallFlags() & 4096) == 0;
        boolean z7 = (installRequest.getInstallFlags() & 65536) != 0;
        String installerPackageName = installRequest.getInstallerPackageName();
        int dataLoaderType = installRequest.getDataLoaderType();
        boolean z8 = installRequest.getReturnCode() == 1;
        boolean isUpdate = installRequest.isUpdate();
        String name = installRequest.getName();
        PackageStateInternal packageStateInternal2 = z8 ? this.mPm.snapshotComputer().getPackageStateInternal(name) : null;
        boolean z9 = packageStateInternal2 == null || (packageStateInternal2.isSystem() && !packageStateInternal2.getPath().getPath().equals(installRequest.getPkg().getPath()));
        if (z8 && z9) {
            Slog.e("PackageManager", name + " was removed before handlePackagePostInstall could be executed");
            installRequest.setReturnCode(-23);
            installRequest.setReturnMessage("Package was removed before install could complete.");
            this.mRemovePackageHelper.cleanUpResources(installRequest.getOldCodeFile(), installRequest.getOldInstructionSet());
            this.mPm.notifyInstallObserver(installRequest);
            return;
        }
        if (z8) {
            this.mPm.mPerUidReadTimeoutsCache = null;
            if (installRequest.getRemovedInfo() != null) {
                if (installRequest.getRemovedInfo().mIsExternal) {
                    if (PackageManagerService.DEBUG_INSTALL) {
                        Slog.i("PackageManager", "upgrading pkg " + installRequest.getRemovedInfo().mRemovedPackage + " is ASEC-hosted -> UNAVAILABLE");
                    }
                    String[] strArr = {installRequest.getRemovedInfo().mRemovedPackage};
                    int[] iArr2 = {installRequest.getRemovedInfo().mUid};
                    BroadcastHelper broadcastHelper = this.mBroadcastHelper;
                    PackageManagerService packageManagerService = this.mPm;
                    Objects.requireNonNull(packageManagerService);
                    broadcastHelper.sendResourcesChangedBroadcast(new InstallPackageHelper$$ExternalSyntheticLambda1(packageManagerService), false, true, strArr, iArr2);
                }
                installRequest.getRemovedInfo().sendPackageRemovedBroadcasts(z6, false);
            }
            if (installRequest.getInstallerPackageName() != null) {
                str2 = installRequest.getInstallerPackageName();
            } else if (installRequest.getRemovedInfo() != null) {
                str2 = installRequest.getRemovedInfo().mInstallerPackageName;
            } else {
                str = null;
                this.mPm.notifyInstantAppPackageInstalled(installRequest.getPkg().getPackageName(), installRequest.getNewUsers());
                int[] iArr3 = PackageManagerService.EMPTY_INT_ARRAY;
                boolean z10 = installRequest.getOriginUsers() != null || installRequest.getOriginUsers().length == 0;
                if (packageStateInternal2 != null) {
                    Slog.d("PackageManager", "handlePackagePostInstall packageName:" + name + " ps is null!");
                    return;
                }
                int[] newUsers = installRequest.getNewUsers();
                int length = newUsers.length;
                int i2 = 0;
                int[] iArr4 = iArr3;
                int[] iArr5 = iArr4;
                int[] iArr6 = iArr5;
                while (i2 < length) {
                    int i3 = length;
                    int i4 = newUsers[i2];
                    boolean isInstantApp = packageStateInternal2.getUserStateOrDefault(i4).isInstantApp();
                    if (z10) {
                        if (isInstantApp) {
                            iArr5 = ArrayUtils.appendInt(iArr5, i4);
                        } else {
                            iArr3 = ArrayUtils.appendInt(iArr3, i4);
                        }
                        packageStateInternal = packageStateInternal2;
                        iArr = newUsers;
                        z4 = z8;
                    } else {
                        packageStateInternal = packageStateInternal2;
                        int[] originUsers = installRequest.getOriginUsers();
                        iArr = newUsers;
                        int length2 = originUsers.length;
                        z4 = z8;
                        int i5 = 0;
                        while (true) {
                            if (i5 >= length2) {
                                z5 = true;
                                break;
                            }
                            int i6 = length2;
                            if (originUsers[i5] == i4) {
                                z5 = false;
                                break;
                            } else {
                                i5++;
                                length2 = i6;
                            }
                        }
                        if (z5) {
                            if (isInstantApp) {
                                iArr5 = ArrayUtils.appendInt(iArr5, i4);
                            } else {
                                iArr3 = ArrayUtils.appendInt(iArr3, i4);
                            }
                        } else if (isInstantApp) {
                            iArr4 = ArrayUtils.appendInt(iArr4, i4);
                        } else {
                            iArr6 = ArrayUtils.appendInt(iArr6, i4);
                        }
                    }
                    i2++;
                    length = i3;
                    packageStateInternal2 = packageStateInternal;
                    newUsers = iArr;
                    z8 = z4;
                }
                z3 = z8;
                Bundle bundle = new Bundle();
                bundle.putInt("android.intent.extra.UID", installRequest.getAppId());
                if (isUpdate) {
                    bundle.putBoolean("android.intent.extra.REPLACING", true);
                }
                bundle.putInt("android.content.pm.extra.DATA_LOADER_TYPE", dataLoaderType);
                if (str != null && installRequest.getPkg().getStaticSharedLibraryName() != null) {
                    this.mPm.sendPackageBroadcast("android.intent.action.PACKAGE_ADDED", name, bundle, 0, str, null, installRequest.getNewUsers(), null, null, null);
                }
                if (installRequest.getPkg().getStaticSharedLibraryName() == null) {
                    this.mPm.mProcessLoggingHandler.invalidateBaseApkHash(installRequest.getPkg().getBaseApkPath());
                    int appId = UserHandle.getAppId(installRequest.getAppId());
                    boolean isInstallSystem = installRequest.isInstallSystem();
                    PackageManagerService packageManagerService2 = this.mPm;
                    Computer snapshotComputer = packageManagerService2.snapshotComputer();
                    int[] iArr7 = iArr3;
                    z2 = z6;
                    str3 = str;
                    installArgs = null;
                    str4 = name;
                    packageManagerService2.sendPackageAddedForNewUsers(snapshotComputer, name, isInstallSystem || z7, z7, appId, iArr7, iArr5, dataLoaderType);
                    synchronized (this.mPm.mLock) {
                        Computer snapshotComputer2 = this.mPm.snapshotComputer();
                        visibilityAllowList = this.mPm.mAppsFilter.getVisibilityAllowList(snapshotComputer2, snapshotComputer2.getPackageStateInternal(str4, 1000), iArr6, this.mPm.mSettings.getPackagesLocked());
                    }
                    this.mPm.sendPackageBroadcast("android.intent.action.PACKAGE_ADDED", str4, bundle, 0, null, null, iArr6, iArr4, visibilityAllowList, null);
                    i = 0;
                    this.mPm.mPackageManagerServiceSocExt.setInstallationBoost(false);
                    if (str3 != null) {
                        this.mPm.sendPackageBroadcast("android.intent.action.PACKAGE_ADDED", str4, bundle, 0, str3, null, iArr6, iArr4, null, null);
                    }
                    this.mPm.mPackageManagerServiceExt.afterSendPackageAddedForAllInHPPI(installRequest.getPkg(), installerPackageName, bundle);
                    if (BroadcastHelper.isPrivacySafetyLabelChangeNotificationsEnabled(this.mContext)) {
                        PackageManagerService packageManagerService3 = this.mPm;
                        packageManagerService3.sendPackageBroadcast("android.intent.action.PACKAGE_ADDED", str4, bundle, 0, packageManagerService3.mRequiredPermissionControllerPackage, null, iArr6, iArr4, null, null);
                    }
                    for (String str5 : this.mPm.mRequiredVerifierPackages) {
                        if (str5 != null && !str5.equals(str3)) {
                            this.mPm.sendPackageBroadcast("android.intent.action.PACKAGE_ADDED", str4, bundle, 0, str5, null, iArr6, iArr4, null, null);
                        }
                    }
                    PackageManagerService packageManagerService4 = this.mPm;
                    String str6 = packageManagerService4.mRequiredInstallerPackage;
                    if (str6 != null) {
                        packageManagerService4.sendPackageBroadcast("android.intent.action.PACKAGE_ADDED", str4, bundle, DumpState.DUMP_SERVICE_PERMISSIONS, str6, null, iArr7, iArr4, null, null);
                    }
                    if (isUpdate) {
                        this.mPm.sendPackageBroadcast("android.intent.action.PACKAGE_REPLACED", str4, bundle, 0, null, null, iArr6, iArr4, installRequest.getRemovedInfo().mBroadcastAllowList, null);
                        if (str3 != null) {
                            this.mPm.sendPackageBroadcast("android.intent.action.PACKAGE_REPLACED", str4, bundle, 0, str3, null, iArr6, iArr4, null, null);
                        }
                        for (String str7 : this.mPm.mRequiredVerifierPackages) {
                            if (str7 != null && !str7.equals(str3)) {
                                this.mPm.sendPackageBroadcast("android.intent.action.PACKAGE_REPLACED", str4, bundle, 0, str7, null, iArr6, iArr4, null, null);
                            }
                        }
                        this.mPm.sendPackageBroadcast("android.intent.action.MY_PACKAGE_REPLACED", null, null, 0, str4, null, iArr6, iArr4, null, this.mBroadcastHelper.getTemporaryAppAllowlistBroadcastOptions(311).toBundle());
                    } else if (z && !installRequest.isInstallSystem()) {
                        if (PackageManagerService.DEBUG_BACKUP) {
                            Slog.i("PackageManager", "Post-restore of " + str4 + " sending FIRST_LAUNCH in " + Arrays.toString(iArr7));
                        }
                        iArr3 = iArr7;
                        this.mBroadcastHelper.sendFirstLaunchBroadcast(str4, installerPackageName, iArr3, iArr5);
                        if (installRequest.getPkg().isExternalStorage()) {
                            if (!isUpdate && (packageExternalStorageType = PackageManagerServiceUtils.getPackageExternalStorageType(((StorageManager) this.mInjector.getSystemService(StorageManager.class)).findVolumeByUuid(StorageManager.convert(installRequest.getPkg().getVolumeUuid()).toString()), installRequest.getPkg().isExternalStorage())) != 0) {
                                FrameworkStatsLog.write(181, packageExternalStorageType, str4);
                            }
                            if (PackageManagerService.DEBUG_INSTALL) {
                                Slog.i("PackageManager", "upgrading pkg " + installRequest.getPkg() + " is external");
                            }
                            int[] iArr8 = {installRequest.getPkg().getUid()};
                            BroadcastHelper broadcastHelper2 = this.mBroadcastHelper;
                            PackageManagerService packageManagerService5 = this.mPm;
                            Objects.requireNonNull(packageManagerService5);
                            broadcastHelper2.sendResourcesChangedBroadcast(new InstallPackageHelper$$ExternalSyntheticLambda1(packageManagerService5), true, true, new String[]{str4}, iArr8);
                        }
                    }
                    iArr3 = iArr7;
                    if (installRequest.getPkg().isExternalStorage()) {
                    }
                } else {
                    z2 = z6;
                    str3 = str;
                    str4 = name;
                    i = 0;
                    installArgs = null;
                    if (!ArrayUtils.isEmpty(installRequest.getLibraryConsumers())) {
                        Computer snapshotComputer3 = this.mPm.snapshotComputer();
                        boolean z11 = (isUpdate || installRequest.getPkg().getStaticSharedLibraryName() == null) ? false : true;
                        for (int i7 = 0; i7 < installRequest.getLibraryConsumers().size(); i7++) {
                            AndroidPackage androidPackage = installRequest.getLibraryConsumers().get(i7);
                            this.mPm.sendPackageChangedBroadcast(snapshotComputer3, androidPackage.getPackageName(), z11, new ArrayList<>(Collections.singletonList(androidPackage.getPackageName())), androidPackage.getUid(), null);
                        }
                    }
                }
                if (iArr3.length > 0) {
                    int length3 = iArr3.length;
                    for (int i8 = i; i8 < length3; i8++) {
                        this.mPm.restorePermissionsAndUpdateRolesForNewUserInstall(str4, iArr3[i8]);
                    }
                }
                if (z10 && !isUpdate) {
                    this.mPm.notifyPackageAdded(str4, installRequest.getAppId());
                    this.mPm.mPackageManagerServiceExt.showAppInstallationRecommendPage(str4, installRequest.getInstallSource());
                } else {
                    this.mPm.notifyPackageChanged(str4, installRequest.getAppId());
                }
                EventLog.writeEvent(3110, getUnknownSourcesSettings());
                InstallArgs installArgs2 = installRequest.getRemovedInfo() != null ? installRequest.getRemovedInfo().mArgs : installArgs;
                if (installArgs2 == null) {
                    VMRuntime.getRuntime().requestConcurrentGC();
                } else if (!z2) {
                    this.mPm.scheduleDeferredNoKillPostDelete(installArgs2);
                } else {
                    this.mRemovePackageHelper.cleanUpResources(installArgs2.mCodeFile, installArgs2.mInstructionSets);
                    this.mPm.mPackageManagerServiceExt.beforeDoPostDeleteLIInHPPI(str4);
                }
                Computer snapshotComputer4 = this.mPm.snapshotComputer();
                int length4 = iArr3.length;
                for (int i9 = i; i9 < length4; i9++) {
                    int i10 = iArr3[i9];
                    PackageInfo packageInfo = snapshotComputer4.getPackageInfo(str4, 0L, i10);
                    if (packageInfo != null) {
                        this.mDexManager.notifyPackageInstalled(packageInfo, i10);
                    }
                }
                PackageManagerService packageManagerService6 = this.mPm;
                packageManagerService6.mPackageManagerServiceExt.handleSuccessAtEndInHPPI(packageManagerService6.mContext, installRequest.getPkg(), str4, str3, isUpdate, iArr6);
            }
            str = str2;
            this.mPm.notifyInstantAppPackageInstalled(installRequest.getPkg().getPackageName(), installRequest.getNewUsers());
            int[] iArr32 = PackageManagerService.EMPTY_INT_ARRAY;
            if (installRequest.getOriginUsers() != null) {
            }
            if (packageStateInternal2 != null) {
            }
        } else {
            z2 = z6;
            i = 0;
            z3 = z8;
        }
        if (((z3 && isUpdate) ? 1 : i) == 0) {
            this.mPm.notifyInstallObserver(installRequest);
        } else if (z2) {
            this.mPm.scheduleDeferredPendingKillInstallObserver(installRequest);
        } else {
            this.mPm.scheduleDeferredNoKillInstallObserver(installRequest);
        }
        this.mPm.schedulePruneUnusedStaticSharedLibraries(true);
        if (installRequest.getTraceMethod() != null) {
            Trace.asyncTraceEnd(262144L, installRequest.getTraceMethod(), installRequest.getTraceCookie());
        }
    }

    private int getUnknownSourcesSettings() {
        return Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "install_non_market_apps", -1, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mPm.mLock", "mPm.mInstallLock"})
    public void installSystemStubPackages(List<String> list, int i) {
        int size = list.size();
        while (true) {
            size--;
            if (size < 0) {
                break;
            }
            String str = list.get(size);
            if (this.mPm.mSettings.isDisabledSystemPackageLPr(str)) {
                list.remove(size);
            } else {
                AndroidPackage androidPackage = this.mPm.mPackages.get(str);
                if (androidPackage == null) {
                    list.remove(size);
                } else {
                    PackageSetting packageLPr = this.mPm.mSettings.getPackageLPr(str);
                    if (packageLPr != null && packageLPr.getEnabled(0) == 3) {
                        list.remove(size);
                    } else {
                        try {
                            installStubPackageLI(androidPackage, 0, i);
                            packageLPr.setEnabled(0, 0, PackageManagerService.PLATFORM_PACKAGE_NAME);
                            list.remove(size);
                        } catch (PackageManagerException e) {
                            Slog.e("PackageManager", "Failed to parse uncompressed system package: " + e.getMessage());
                        }
                    }
                }
            }
        }
        for (int size2 = list.size() - 1; size2 >= 0; size2 += -1) {
            String str2 = list.get(size2);
            this.mPm.mSettings.getPackageLPr(str2).setEnabled(2, 0, PackageManagerService.PLATFORM_PACKAGE_NAME);
            PackageManagerServiceUtils.logCriticalInfo(6, "Stub disabled; pkg: " + str2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:20:0x006c A[Catch: all -> 0x009a, PackageManagerException -> 0x009f, TRY_ENTER, TRY_LEAVE, TryCatch #12 {PackageManagerException -> 0x009f, blocks: (B:20:0x006c, B:47:0x0099, B:46:0x0096), top: B:7:0x0026 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean enableCompressedPackage(AndroidPackage androidPackage, PackageSetting packageSetting) {
        PackageManagerTracedLock packageManagerTracedLock;
        int defParseFlags = this.mPm.getDefParseFlags() | Integer.MIN_VALUE | 64;
        synchronized (this.mPm.mInstallLock) {
            try {
                PackageFreezer freezePackage = this.mPm.freezePackage(androidPackage.getPackageName(), -1, "setEnabledSetting", 16);
                try {
                    try {
                        AndroidPackage installStubPackageLI = installStubPackageLI(androidPackage, defParseFlags, 0);
                        this.mAppDataHelper.prepareAppDataAfterInstallLIF(installStubPackageLI);
                        PackageManagerTracedLock packageManagerTracedLock2 = this.mPm.mLock;
                        try {
                            synchronized (packageManagerTracedLock2) {
                                try {
                                    packageManagerTracedLock = packageManagerTracedLock2;
                                } catch (PackageManagerException e) {
                                    e = e;
                                    packageManagerTracedLock = packageManagerTracedLock2;
                                } catch (Throwable th) {
                                    th = th;
                                    throw th;
                                }
                                try {
                                    this.mSharedLibraries.updateSharedLibraries(installStubPackageLI, packageSetting, null, null, Collections.unmodifiableMap(this.mPm.mPackages));
                                } catch (PackageManagerException e2) {
                                    e = e2;
                                    Slog.w("PackageManager", "updateAllSharedLibrariesLPw failed: ", e);
                                    this.mPm.mPermissionManager.onPackageInstalled(installStubPackageLI, -1, PermissionManagerServiceInternal.PackageInstalledParams.DEFAULT, -1);
                                    this.mPm.writeSettingsLPrTEMP();
                                    if (freezePackage != null) {
                                    }
                                    this.mAppDataHelper.clearAppDataLIF(installStubPackageLI, -1, 39);
                                    this.mDexManager.notifyPackageUpdated(installStubPackageLI.getPackageName(), installStubPackageLI.getBaseApkPath(), installStubPackageLI.getSplitCodePaths());
                                    return true;
                                }
                                this.mPm.mPermissionManager.onPackageInstalled(installStubPackageLI, -1, PermissionManagerServiceInternal.PackageInstalledParams.DEFAULT, -1);
                                this.mPm.writeSettingsLPrTEMP();
                                if (freezePackage != null) {
                                    freezePackage.close();
                                }
                                this.mAppDataHelper.clearAppDataLIF(installStubPackageLI, -1, 39);
                                this.mDexManager.notifyPackageUpdated(installStubPackageLI.getPackageName(), installStubPackageLI.getBaseApkPath(), installStubPackageLI.getSplitCodePaths());
                                return true;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                        }
                        throw th;
                    } finally {
                    }
                } catch (PackageManagerException unused) {
                    try {
                        try {
                            freezePackage = this.mPm.freezePackage(androidPackage.getPackageName(), -1, "setEnabledSetting", 16);
                            try {
                                synchronized (this.mPm.mLock) {
                                    this.mPm.mSettings.enableSystemPackageLPw(androidPackage.getPackageName());
                                }
                                installPackageFromSystemLIF(androidPackage.getPath(), this.mPm.mUserManager.getUserIds(), null, true);
                                if (freezePackage != null) {
                                    freezePackage.close();
                                }
                                synchronized (this.mPm.mLock) {
                                    PackageSetting packageLPr = this.mPm.mSettings.getPackageLPr(androidPackage.getPackageName());
                                    if (packageLPr != null) {
                                        packageLPr.setEnabled(2, 0, PackageManagerService.PLATFORM_PACKAGE_NAME);
                                    }
                                    this.mPm.writeSettingsLPrTEMP();
                                }
                            } finally {
                            }
                        } catch (PackageManagerException e3) {
                            Slog.wtf("PackageManager", "Failed to restore system package:" + androidPackage.getPackageName(), e3);
                            synchronized (this.mPm.mLock) {
                                PackageSetting packageLPr2 = this.mPm.mSettings.getPackageLPr(androidPackage.getPackageName());
                                if (packageLPr2 != null) {
                                    packageLPr2.setEnabled(2, 0, PackageManagerService.PLATFORM_PACKAGE_NAME);
                                }
                                this.mPm.writeSettingsLPrTEMP();
                                return false;
                            }
                        }
                        return false;
                    } catch (Throwable th3) {
                        synchronized (this.mPm.mLock) {
                            PackageSetting packageLPr3 = this.mPm.mSettings.getPackageLPr(androidPackage.getPackageName());
                            if (packageLPr3 != null) {
                                packageLPr3.setEnabled(2, 0, PackageManagerService.PLATFORM_PACKAGE_NAME);
                            }
                            this.mPm.writeSettingsLPrTEMP();
                            throw th3;
                        }
                    }
                }
            } catch (PackageManagerException unused2) {
            }
        }
    }

    @GuardedBy({"mPm.mInstallLock"})
    private AndroidPackage installStubPackageLI(AndroidPackage androidPackage, int i, int i2) throws PackageManagerException {
        if (PackageManagerService.DEBUG_COMPRESSION) {
            Slog.i("PackageManager", "Uncompressing system stub; pkg: " + androidPackage.getPackageName());
        }
        File decompressPackage = decompressPackage(androidPackage.getPackageName(), androidPackage.getPath());
        if (decompressPackage == null) {
            throw PackageManagerException.ofInternalError("Unable to decompress stub at " + androidPackage.getPath(), -11);
        }
        synchronized (this.mPm.mLock) {
            this.mPm.mSettings.disableSystemPackageLPw(androidPackage.getPackageName(), true);
        }
        RemovePackageHelper removePackageHelper = new RemovePackageHelper(this.mPm);
        removePackageHelper.removePackage(androidPackage, true);
        try {
            return initPackageTracedLI(decompressPackage, i, i2);
        } catch (PackageManagerException e) {
            Slog.w("PackageManager", "Failed to install compressed system package:" + androidPackage.getPackageName(), e);
            removePackageHelper.removeCodePath(decompressPackage);
            throw e;
        }
    }

    @GuardedBy({"mPm.mInstallLock"})
    private File decompressPackage(String str, String str2) {
        if (!PackageManagerServiceUtils.compressedFileExists(str2)) {
            if (PackageManagerService.DEBUG_COMPRESSION) {
                Slog.i("PackageManager", "No files to decompress at: " + str2);
            }
            return null;
        }
        File nextCodePath = PackageManagerServiceUtils.getNextCodePath(Environment.getDataAppDirectory(null), str);
        int decompressFiles = PackageManagerServiceUtils.decompressFiles(str2, nextCodePath, str);
        if (decompressFiles == 1) {
            decompressFiles = PackageManagerServiceUtils.extractNativeBinaries(nextCodePath, str);
        }
        if (decompressFiles == 1) {
            if (!this.mPm.isSystemReady()) {
                PackageManagerService packageManagerService = this.mPm;
                if (packageManagerService.mReleaseOnSystemReady == null) {
                    packageManagerService.mReleaseOnSystemReady = new ArrayList();
                }
                this.mPm.mReleaseOnSystemReady.add(nextCodePath);
            } else {
                F2fsUtils.releaseCompressedBlocks(this.mContext.getContentResolver(), nextCodePath);
            }
            return nextCodePath;
        }
        if (!nextCodePath.exists()) {
            return null;
        }
        new RemovePackageHelper(this.mPm).removeCodePath(nextCodePath);
        return null;
    }

    public void restoreDisabledSystemPackageLIF(DeletePackageAction deletePackageAction, int[] iArr, boolean z) throws SystemDeleteException {
        PackageSetting packageSetting = deletePackageAction.mDeletingPs;
        PackageRemovedInfo packageRemovedInfo = deletePackageAction.mRemovedInfo;
        PackageSetting packageSetting2 = deletePackageAction.mDisabledPs;
        synchronized (this.mPm.mLock) {
            this.mPm.mSettings.enableSystemPackageLPw(packageSetting2.getPkg().getPackageName());
            PackageManagerServiceUtils.removeNativeBinariesLI(packageSetting);
        }
        if (PackageManagerService.DEBUG_REMOVE) {
            Slog.d("PackageManager", "Re-installing system package: " + packageSetting2);
        }
        try {
            try {
                synchronized (this.mPm.mInstallLock) {
                    installPackageFromSystemLIF(packageSetting2.getPathString(), iArr, packageRemovedInfo == null ? null : packageRemovedInfo.mOrigUsers, z);
                }
                if (packageSetting2.getPkg().isStub()) {
                    synchronized (this.mPm.mLock) {
                        disableStubPackage(deletePackageAction, packageSetting, iArr);
                    }
                }
                this.mPm.mPackageManagerServiceExt.notifyPackageAddOrUpdateForAbiInfo(packageSetting2.getPkg().getPackageName(), packageSetting2);
            } catch (PackageManagerException e) {
                Slog.w("PackageManager", "Failed to restore system package:" + packageSetting.getPackageName() + ": " + e.getMessage());
                throw new SystemDeleteException(e);
            }
        } catch (Throwable th) {
            if (packageSetting2.getPkg().isStub()) {
                synchronized (this.mPm.mLock) {
                    disableStubPackage(deletePackageAction, packageSetting, iArr);
                }
            }
            this.mPm.mPackageManagerServiceExt.notifyPackageAddOrUpdateForAbiInfo(packageSetting2.getPkg().getPackageName(), packageSetting2);
            throw th;
        }
    }

    @GuardedBy({"mPm.mLock"})
    private void disableStubPackage(DeletePackageAction deletePackageAction, PackageSetting packageSetting, int[] iArr) {
        PackageSetting packageLPr = this.mPm.mSettings.getPackageLPr(packageSetting.getPackageName());
        if (packageLPr != null) {
            UserHandle userHandle = deletePackageAction.mUser;
            int identifier = userHandle == null ? -1 : userHandle.getIdentifier();
            if (identifier != -1) {
                if (identifier >= 0) {
                    packageLPr.setEnabled(2, identifier, PackageManagerService.PLATFORM_PACKAGE_NAME);
                }
            } else {
                for (int i : iArr) {
                    packageLPr.setEnabled(2, i, PackageManagerService.PLATFORM_PACKAGE_NAME);
                }
            }
        }
    }

    @GuardedBy({"mPm.mInstallLock"})
    private void installPackageFromSystemLIF(String str, int[] iArr, int[] iArr2, boolean z) throws PackageManagerException {
        File file = new File(str);
        AndroidPackage initPackageTracedLI = initPackageTracedLI(file, this.mPm.getDefParseFlags() | 1 | 16, this.mPm.getSystemPackageScanFlags(file));
        synchronized (this.mPm.mLock) {
            try {
                this.mSharedLibraries.updateSharedLibraries(initPackageTracedLI, this.mPm.mSettings.getPackageLPr(initPackageTracedLI.getPackageName()), null, null, Collections.unmodifiableMap(this.mPm.mPackages));
            } catch (PackageManagerException e) {
                Slog.e("PackageManager", "updateAllSharedLibrariesLPw failed: " + e.getMessage());
            }
        }
        this.mAppDataHelper.prepareAppDataAfterInstallLIF(initPackageTracedLI);
        setPackageInstalledForSystemPackage(initPackageTracedLI, iArr, iArr2, z);
    }

    private void setPackageInstalledForSystemPackage(AndroidPackage androidPackage, int[] iArr, int[] iArr2, boolean z) {
        synchronized (this.mPm.mLock) {
            PackageSetting packageLPr = this.mPm.mSettings.getPackageLPr(androidPackage.getPackageName());
            boolean z2 = iArr2 != null;
            if (z2) {
                if (PackageManagerService.DEBUG_REMOVE) {
                    Slog.d("PackageManager", "Propagating install state across reinstall");
                }
                boolean z3 = false;
                for (int i : iArr) {
                    boolean contains = ArrayUtils.contains(iArr2, i);
                    if (PackageManagerService.DEBUG_REMOVE) {
                        Slog.d("PackageManager", "    user " + i + " => " + contains);
                    }
                    if (contains != packageLPr.getInstalled(i)) {
                        z3 = true;
                    }
                    packageLPr.setInstalled(contains, i);
                    if (contains) {
                        packageLPr.setUninstallReason(0, i);
                    }
                }
                this.mPm.mSettings.writeAllUsersPackageRestrictionsLPr();
                if (z3) {
                    this.mPm.mSettings.writeKernelMappingLPr(packageLPr);
                }
            }
            this.mPm.mPermissionManager.onPackageInstalled(androidPackage, -1, PermissionManagerServiceInternal.PackageInstalledParams.DEFAULT, -1);
            for (int i2 : iArr) {
                if (z2) {
                    this.mPm.mSettings.writePermissionStateForUserLPr(i2, false);
                }
            }
            if (z) {
                this.mPm.writeSettingsLPrTEMP();
            }
        }
    }

    @GuardedBy({"mPm.mLock"})
    public void prepareSystemPackageCleanUp(WatchedArrayMap<String, PackageSetting> watchedArrayMap, List<String> list, ArrayMap<String, File> arrayMap, int[] iArr) {
        for (int size = watchedArrayMap.size() - 1; size >= 0; size--) {
            PackageSetting valueAt = watchedArrayMap.valueAt(size);
            String packageName = valueAt.getPackageName();
            if (valueAt.isSystem()) {
                AndroidPackage androidPackage = this.mPm.mPackages.get(packageName);
                PackageSetting disabledSystemPkgLPr = this.mPm.mSettings.getDisabledSystemPkgLPr(packageName);
                if (androidPackage != null) {
                    if (!androidPackage.isApex() && disabledSystemPkgLPr != null) {
                        PackageManagerServiceUtils.logCriticalInfo(5, "Expecting better updated system app for " + packageName + "; removing system app.  Last known codePath=" + valueAt.getPathString() + ", versionCode=" + valueAt.getVersionCode() + "; scanned versionCode=" + androidPackage.getLongVersionCode());
                        this.mRemovePackageHelper.removePackage(androidPackage, true);
                        arrayMap.put(valueAt.getPackageName(), valueAt.getPath());
                    }
                } else if (disabledSystemPkgLPr == null) {
                    PackageManagerServiceUtils.logCriticalInfo(5, "System package " + packageName + " no longer exists; its data will be wiped");
                    this.mPm.mPackageManagerServiceExt.onSystemAppNotExistCheckedInConstructor(valueAt);
                    this.mRemovePackageHelper.removePackageData(valueAt, iArr, null, 0, false);
                } else if (disabledSystemPkgLPr.getPath() == null || !disabledSystemPkgLPr.getPath().exists() || disabledSystemPkgLPr.getPkg() == null) {
                    list.add(packageName);
                } else {
                    arrayMap.put(disabledSystemPkgLPr.getPackageName(), disabledSystemPkgLPr.getPath());
                }
            }
        }
    }

    @GuardedBy({"mPm.mLock"})
    public void cleanupDisabledPackageSettings(List<String> list, int[] iArr, int i) {
        String str;
        for (int size = list.size() - 1; size >= 0; size--) {
            String str2 = list.get(size);
            AndroidPackage androidPackage = this.mPm.mPackages.get(str2);
            this.mPm.mSettings.removeDisabledSystemPackageLPw(str2);
            if (androidPackage == null) {
                str = "Updated system package " + str2 + " no longer exists; removing its data";
            } else {
                String str3 = "Updated system package " + str2 + " no longer exists; rescanning package on data";
                this.mRemovePackageHelper.removePackage(androidPackage, true);
                PackageSetting packageLPr = this.mPm.mSettings.getPackageLPr(str2);
                if (packageLPr != null) {
                    packageLPr.getPkgState().setUpdatedSystemApp(false);
                }
                if (!this.mPm.mPackageManagerServiceExt.shouldRemoveUpdatedMainlineApk(str2)) {
                    try {
                        File file = new File(androidPackage.getPath());
                        synchronized (this.mPm.mInstallLock) {
                            initPackageTracedLI(file, 0, i);
                        }
                    } catch (PackageManagerException e) {
                        Slog.e("PackageManager", "Failed to parse updated, ex-system package: " + e.getMessage());
                    }
                }
                str = str3;
            }
            PackageSetting packageLPr2 = this.mPm.mSettings.getPackageLPr(str2);
            if (packageLPr2 != null && this.mPm.mPackages.get(str2) == null) {
                this.mRemovePackageHelper.removePackageData(packageLPr2, iArr, null, 0, false);
            }
            PackageManagerServiceUtils.logCriticalInfo(5, str);
        }
    }

    @GuardedBy({"mPm.mInstallLock", "mPm.mLock"})
    public List<ApexManager.ScanResult> scanApexPackages(ApexInfo[] apexInfoArr, int i, int i2, PackageParser2 packageParser2, ExecutorService executorService) {
        int i3;
        int i4;
        if (apexInfoArr == null) {
            return Collections.EMPTY_LIST;
        }
        ParallelPackageParser parallelPackageParser = new ParallelPackageParser(packageParser2, executorService);
        final ArrayMap arrayMap = new ArrayMap();
        for (ApexInfo apexInfo : apexInfoArr) {
            File file = new File(apexInfo.modulePath);
            parallelPackageParser.submit(file, i);
            arrayMap.put(file, apexInfo);
        }
        ArrayList arrayList = new ArrayList(arrayMap.size());
        for (int i5 = 0; i5 < arrayMap.size(); i5++) {
            arrayList.add(parallelPackageParser.take());
        }
        Collections.sort(arrayList, new Comparator() { // from class: com.android.server.pm.InstallPackageHelper$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$scanApexPackages$2;
                lambda$scanApexPackages$2 = InstallPackageHelper.lambda$scanApexPackages$2(arrayMap, (ParallelPackageParser.ParseResult) obj, (ParallelPackageParser.ParseResult) obj2);
                return lambda$scanApexPackages$2;
            }
        });
        ArrayList arrayList2 = new ArrayList(arrayMap.size());
        for (int i6 = 0; i6 < arrayList.size(); i6++) {
            ParallelPackageParser.ParseResult parseResult = (ParallelPackageParser.ParseResult) arrayList.get(i6);
            Throwable th = parseResult.throwable;
            ApexInfo apexInfo2 = (ApexInfo) arrayMap.get(parseResult.scanFile);
            int systemPackageScanFlags = i2 | 67108864 | this.mPm.getSystemPackageScanFlags(parseResult.scanFile);
            if (apexInfo2.isFactory) {
                i3 = systemPackageScanFlags;
                i4 = i;
            } else {
                i4 = i & (-17);
                i3 = systemPackageScanFlags | 4;
            }
            if (th == null) {
                try {
                    addForInitLI(parseResult.parsedPackage, i4, i3, null, new ApexManager.ActiveApexInfo(apexInfo2));
                    AndroidPackageInternal hideAsFinal = parseResult.parsedPackage.hideAsFinal();
                    if (apexInfo2.isFactory && !apexInfo2.isActive) {
                        disableSystemPackageLPw(hideAsFinal);
                    }
                    arrayList2.add(new ApexManager.ScanResult(apexInfo2, hideAsFinal, hideAsFinal.getPackageName()));
                } catch (PackageManagerException e) {
                    String str = apexInfo2.modulePath;
                    if (str != null && str.contains("/data/apex/active/")) {
                        SystemProperties.set("sys.revert.data.apex", "1");
                    }
                    throw new IllegalStateException("Failed to scan: " + apexInfo2.modulePath, e);
                }
            } else {
                if (th instanceof PackageManagerException) {
                    throw new IllegalStateException("Unable to parse: " + apexInfo2.modulePath, th);
                }
                throw new IllegalStateException("Unexpected exception occurred while parsing " + apexInfo2.modulePath, th);
            }
        }
        return arrayList2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$scanApexPackages$2(ArrayMap arrayMap, ParallelPackageParser.ParseResult parseResult, ParallelPackageParser.ParseResult parseResult2) {
        return Boolean.compare(((ApexInfo) arrayMap.get(parseResult2.scanFile)).isFactory, ((ApexInfo) arrayMap.get(parseResult.scanFile)).isFactory);
    }

    /* JADX WARN: Removed duplicated region for block: B:47:0x019c A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x01ad A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x01d0  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x01d7 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x01d7 A[ADDED_TO_REGION, SYNTHETIC] */
    @GuardedBy({"mPm.mInstallLock", "mPm.mLock"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void installPackagesFromDir(File file, int i, int i2, PackageParser2 packageParser2, ExecutorService executorService, ApexManager.ActiveApexInfo activeApexInfo) {
        ParallelPackageParser.ParseResult parseResult;
        int i3;
        ParallelPackageParser parallelPackageParser;
        String str;
        int i4;
        int i5;
        String str2;
        int i6;
        PackageManagerService packageManagerService;
        String str3;
        int i7 = i;
        int i8 = i2;
        File[] listFiles = file.listFiles();
        String str4 = "PackageManager";
        if (ArrayUtils.isEmpty(listFiles)) {
            Log.d("PackageManager", "No files in app dir " + file);
            return;
        }
        this.mPm.mPackageManagerServiceExt.beforeScanInScanDirLI();
        ((IPackageManagerServiceUtilsExt) ExtLoader.type(IPackageManagerServiceUtilsExt.class).create()).addBootEvent("Android:PMS_scan_data:" + file.getPath().toString());
        if (PackageManagerService.DEBUG_PACKAGE_SCANNING) {
            Log.d("PackageManager", "Scanning app dir " + file + " scanFlags=" + i8 + " flags=0x" + Integer.toHexString(i));
        }
        ParallelPackageParser parallelPackageParser2 = new ParallelPackageParser(packageParser2, executorService);
        int i9 = 0;
        int i10 = 0;
        for (File file2 : listFiles) {
            if ((ApkLiteParseUtils.isApkFile(file2) || file2.isDirectory()) && !PackageInstallerService.isStageName(file2.getName())) {
                if ((16777216 & i8) != 0) {
                    PackageCacher packageCacher = new PackageCacher(this.mPm.getCacheDir());
                    Log.w("PackageManager", "Dropping cache of " + file2.getAbsolutePath());
                    packageCacher.cleanCachedResult(file2);
                }
                parallelPackageParser2.submit(file2, i7);
                i10++;
            }
        }
        int i11 = i10;
        while (i11 > 0) {
            ParallelPackageParser.ParseResult take = parallelPackageParser2.take();
            Throwable th = take.throwable;
            if (th == null) {
                if (this.mPm.mPackageManagerServiceExt.interceptUseParseResultWithoutThrowInScanDirLI(take, i8) || this.mPm.mPackageManagerServiceExt.interceptUseParseResultWithoutThrowInScanDirLI2(take, i8, i7, file)) {
                    i3 = i9;
                    parallelPackageParser = parallelPackageParser2;
                    str = str4;
                    i11--;
                    i7 = i;
                    i8 = i2;
                    str4 = str;
                    i9 = i3;
                    parallelPackageParser2 = parallelPackageParser;
                } else {
                    try {
                        str3 = ": ";
                    } catch (PackageManagerException e) {
                        e = e;
                        str3 = ": ";
                    }
                    try {
                        addForInitLI(take.parsedPackage, i, i2, new UserHandle(i9), activeApexInfo);
                        str2 = null;
                        parseResult = take;
                        i3 = i9;
                        parallelPackageParser = parallelPackageParser2;
                        str = str4;
                        i4 = 1;
                        i6 = 1;
                    } catch (PackageManagerException e2) {
                        e = e2;
                        i5 = e.error;
                        str2 = "Failed to scan " + take.scanFile + str3 + e.getMessage();
                        Slog.w(str4, str2);
                        PackageManagerService packageManagerService2 = this.mPm;
                        parseResult = take;
                        i4 = 1;
                        i3 = i9;
                        parallelPackageParser = parallelPackageParser2;
                        str = str4;
                        packageManagerService2.mPackageManagerServiceExt.handleExpOfAddForInitInScanDirLI(packageManagerService2, parseResult, take.scanFile, i2, i, activeApexInfo);
                        i6 = i5;
                        if ((i2 & 8388608) != 0) {
                        }
                        if ((i2 & 65536) != 0) {
                        }
                        i11--;
                        i7 = i;
                        i8 = i2;
                        str4 = str;
                        i9 = i3;
                        parallelPackageParser2 = parallelPackageParser;
                    }
                    if ((i2 & 8388608) != 0 && i6 != i4) {
                        this.mApexManager.reportErrorWithApkInApex(file.getAbsolutePath(), str2);
                    }
                    if ((i2 & 65536) != 0 && i6 != i4) {
                        PackageManagerServiceUtils.logCriticalInfo(5, "Deleting invalid package at " + parseResult.scanFile);
                        packageManagerService = this.mPm;
                        if (packageManagerService.mPackageManagerServiceExt.skipDeleteDataAppWhenFailedInScanDirLI(packageManagerService)) {
                            this.mRemovePackageHelper.removeCodePath(parseResult.scanFile);
                        }
                    }
                    i11--;
                    i7 = i;
                    i8 = i2;
                    str4 = str;
                    i9 = i3;
                    parallelPackageParser2 = parallelPackageParser;
                }
            } else {
                parseResult = take;
                i3 = i9;
                parallelPackageParser = parallelPackageParser2;
                str = str4;
                i4 = 1;
                if (th instanceof PackageManagerException) {
                    PackageManagerException packageManagerException = (PackageManagerException) th;
                    i5 = packageManagerException.error;
                    str2 = "Failed to parse " + parseResult.scanFile + ": " + packageManagerException.getMessage();
                    Slog.w(str, str2);
                } else {
                    throw new IllegalStateException("Unexpected exception occurred while parsing " + parseResult.scanFile, th);
                }
            }
            i6 = i5;
            if ((i2 & 8388608) != 0) {
                this.mApexManager.reportErrorWithApkInApex(file.getAbsolutePath(), str2);
            }
            if ((i2 & 65536) != 0) {
                PackageManagerServiceUtils.logCriticalInfo(5, "Deleting invalid package at " + parseResult.scanFile);
                packageManagerService = this.mPm;
                if (packageManagerService.mPackageManagerServiceExt.skipDeleteDataAppWhenFailedInScanDirLI(packageManagerService)) {
                }
            }
            i11--;
            i7 = i;
            i8 = i2;
            str4 = str;
            i9 = i3;
            parallelPackageParser2 = parallelPackageParser;
        }
    }

    @GuardedBy({"mPm.mLock"})
    public void checkExistingBetterPackages(ArrayMap<String, File> arrayMap, List<String> list, int i, int i2) {
        for (int i3 = 0; i3 < arrayMap.size(); i3++) {
            String keyAt = arrayMap.keyAt(i3);
            if (!this.mPm.mPackages.containsKey(keyAt)) {
                File valueAt = arrayMap.valueAt(i3);
                PackageManagerServiceUtils.logCriticalInfo(5, "Expected better " + keyAt + " but never showed up; reverting to system");
                Pair<Integer, Integer> systemPackageRescanFlagsAndReparseFlags = this.mPm.getSystemPackageRescanFlagsAndReparseFlags(valueAt, i, i2);
                int intValue = ((Integer) systemPackageRescanFlagsAndReparseFlags.first).intValue();
                int intValue2 = ((Integer) systemPackageRescanFlagsAndReparseFlags.second).intValue();
                if (intValue == 0) {
                    Slog.e("PackageManager", "Ignoring unexpected fallback path " + valueAt);
                } else {
                    this.mPm.mSettings.enableSystemPackageLPw(keyAt);
                    try {
                        synchronized (this.mPm.mInstallLock) {
                            if (initPackageTracedLI(valueAt, intValue2, intValue).isStub()) {
                                list.add(keyAt);
                            }
                        }
                    } catch (PackageManagerException e) {
                        Slog.e("PackageManager", "Failed to parse original system package: " + e.getMessage());
                    }
                }
            }
        }
    }

    @GuardedBy({"mPm.mInstallLock"})
    public AndroidPackage initPackageTracedLI(File file, int i, int i2) throws PackageManagerException {
        Trace.traceBegin(262144L, "scanPackage [" + file.toString() + "]");
        try {
            return initPackageLI(file, i, i2);
        } finally {
            Trace.traceEnd(262144L);
        }
    }

    @GuardedBy({"mPm.mInstallLock"})
    private AndroidPackage initPackageLI(File file, int i, int i2) throws PackageManagerException {
        if (PackageManagerService.DEBUG_INSTALL) {
            Slog.d("PackageManager", "Parsing: " + file);
        }
        Trace.traceBegin(262144L, "parsePackage");
        try {
            PackageParser2 scanningPackageParser = this.mPm.mInjector.getScanningPackageParser();
            try {
                ParsedPackage parsePackage = scanningPackageParser.parsePackage(file, i, false);
                scanningPackageParser.close();
                Trace.traceEnd(262144L);
                return addForInitLI(parsePackage, i, i2, new UserHandle(0), null);
            } finally {
            }
        } catch (Throwable th) {
            Trace.traceEnd(262144L);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mPm.mInstallLock"})
    public AndroidPackage addForInitLI(ParsedPackage parsedPackage, int i, int i2, UserHandle userHandle, ApexManager.ActiveApexInfo activeApexInfo) throws PackageManagerException {
        PackageSetting disabledSystemPkgLPr;
        String apexModuleName;
        List<ReconciledPackage> reconcilePackages;
        boolean z;
        PackageSetting packageSetting;
        synchronized (this.mPm.mLock) {
            if (activeApexInfo == null) {
                if (parsedPackage.isStaticSharedLibrary()) {
                    PackageManagerService.renameStaticSharedLibraryPackage(parsedPackage);
                }
            }
            disabledSystemPkgLPr = this.mPm.mSettings.getDisabledSystemPkgLPr(parsedPackage.getPackageName());
            if (activeApexInfo != null && disabledSystemPkgLPr != null) {
                disabledSystemPkgLPr.setApexModuleName(activeApexInfo.apexModuleName);
            }
        }
        Pair<ScanResult, Boolean> scanSystemPackageLI = scanSystemPackageLI(parsedPackage, i, i2, userHandle);
        if (scanSystemPackageLI == null) {
            return null;
        }
        ScanResult scanResult = (ScanResult) scanSystemPackageLI.first;
        boolean booleanValue = ((Boolean) scanSystemPackageLI.second).booleanValue();
        InstallRequest installRequest = new InstallRequest(parsedPackage, i, i2, userHandle, scanResult);
        synchronized (this.mPm.mLock) {
            PackageSetting packageLPr = this.mPm.mSettings.getPackageLPr(parsedPackage.getPackageName());
            apexModuleName = packageLPr != null ? packageLPr.getApexModuleName() : null;
        }
        if (activeApexInfo != null) {
            installRequest.setApexModuleName(activeApexInfo.apexModuleName);
        } else if (disabledSystemPkgLPr != null) {
            installRequest.setApexModuleName(disabledSystemPkgLPr.getApexModuleName());
        } else if (apexModuleName != null) {
            installRequest.setApexModuleName(apexModuleName);
        }
        synchronized (this.mPm.mLock) {
            boolean z2 = false;
            try {
                String packageName = scanResult.mPkgSetting.getPackageName();
                List singletonList = Collections.singletonList(installRequest);
                PackageManagerService packageManagerService = this.mPm;
                reconcilePackages = ReconcilePackageUtils.reconcilePackages(singletonList, packageManagerService.mPackages, Collections.singletonMap(packageName, packageManagerService.getSettingsVersionForPackage(parsedPackage)), this.mSharedLibraries, this.mPm.mSettings.getKeySetManagerService(), this.mPm.mSettings);
                if ((i2 & 67108864) == 0) {
                    this.mPm.mPackageManagerServiceExt.adjustScanResultBeforeRegisterAppIdInAFILI(scanResult);
                    z = optimisticallyRegisterAppId(installRequest);
                } else {
                    installRequest.setScannedPackageSettingAppId(-1);
                    z = false;
                }
            } catch (PackageManagerException e) {
                e = e;
            }
            try {
                commitReconciledScanResultLocked(reconcilePackages.get(0), this.mPm.mUserManager.getUserIds());
            } catch (PackageManagerException e2) {
                e = e2;
                z2 = z;
                if (z2) {
                    cleanUpAppIdCreation(installRequest);
                }
                throw e;
            }
        }
        if (booleanValue) {
            synchronized (this.mPm.mLock) {
                this.mPm.mSettings.disableSystemPackageLPw(parsedPackage.getPackageName(), true);
            }
        }
        if (this.mIncrementalManager != null && IncrementalManager.isIncrementalPath(parsedPackage.getPath()) && (packageSetting = scanResult.mPkgSetting) != null && packageSetting.isLoading()) {
            this.mIncrementalManager.registerLoadingProgressCallback(parsedPackage.getPath(), new IncrementalProgressListener(parsedPackage.getPackageName(), this.mPm));
        }
        this.mPm.mPackageManagerServiceExt.beforeReturnInAddForInitLI(scanResult);
        return scanResult.mPkgSetting.getPkg();
    }

    private boolean optimisticallyRegisterAppId(InstallRequest installRequest) throws PackageManagerException {
        boolean registerAppIdLPw;
        if (installRequest.isExistingSettingCopied() && !installRequest.needsNewAppId()) {
            return false;
        }
        synchronized (this.mPm.mLock) {
            registerAppIdLPw = this.mPm.mSettings.registerAppIdLPw(installRequest.getScannedPackageSetting(), installRequest.needsNewAppId());
        }
        return registerAppIdLPw;
    }

    private void cleanUpAppIdCreation(InstallRequest installRequest) {
        if (installRequest.getScannedPackageSetting() == null || installRequest.getScannedPackageSetting().getAppId() <= 0) {
            return;
        }
        synchronized (this.mPm.mLock) {
            this.mPm.mSettings.removeAppIdLPw(installRequest.getScannedPackageSetting().getAppId());
        }
    }

    @GuardedBy({"mPm.mInstallLock"})
    private ScanResult scanPackageTracedLI(ParsedPackage parsedPackage, int i, int i2, long j, UserHandle userHandle, String str) throws PackageManagerException {
        Trace.traceBegin(262144L, "scanPackage");
        try {
            return scanPackageNewLI(parsedPackage, i, i2, j, userHandle, str);
        } finally {
            Trace.traceEnd(262144L);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x00d6 A[Catch: all -> 0x0112, TryCatch #0 {, blocks: (B:4:0x0008, B:6:0x0024, B:7:0x0027, B:9:0x0045, B:10:0x0064, B:12:0x0073, B:18:0x0084, B:20:0x008a, B:21:0x0098, B:23:0x009c, B:26:0x00a4, B:28:0x00d6, B:29:0x00e1, B:45:0x007c), top: B:3:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00e0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private ScanRequest prepareInitialScanRequest(ParsedPackage parsedPackage, int i, int i2, UserHandle userHandle, String str) throws PackageManagerException {
        AndroidPackage platformPackage;
        String realPackageName;
        PackageSetting originalPackageLocked;
        PackageSetting packageLPr;
        PackageSetting disabledSystemPkgLPr;
        boolean isLeavingSharedUser;
        SharedUserSetting sharedUserLPw;
        SharedUserSetting sharedUserSettingLPr;
        synchronized (this.mPm.mLock) {
            platformPackage = this.mPm.getPlatformPackage();
            boolean isSystem = AndroidPackageUtils.isSystem(parsedPackage);
            String renamedPackageLPr = this.mPm.mSettings.getRenamedPackageLPr(AndroidPackageUtils.getRealPackageOrNull(parsedPackage, isSystem));
            realPackageName = ScanPackageUtils.getRealPackageName(parsedPackage, renamedPackageLPr, isSystem);
            if (realPackageName != null) {
                ScanPackageUtils.ensurePackageRenamed(parsedPackage, renamedPackageLPr);
            }
            originalPackageLocked = getOriginalPackageLocked(parsedPackage, renamedPackageLPr);
            packageLPr = this.mPm.mSettings.getPackageLPr(parsedPackage.getPackageName());
            if (this.mPm.mTransferredPackages.contains(parsedPackage.getPackageName())) {
                Slog.w("PackageManager", "Package " + parsedPackage.getPackageName() + " was transferred to another, but its .apk remains");
            }
            disabledSystemPkgLPr = this.mPm.mSettings.getDisabledSystemPkgLPr(parsedPackage.getPackageName());
            if (packageLPr != null && packageLPr.hasSharedUser()) {
                isLeavingSharedUser = false;
                sharedUserLPw = (!isLeavingSharedUser || parsedPackage.getSharedUserId() == null) ? null : this.mPm.mSettings.getSharedUserLPw(parsedPackage.getSharedUserId(), 0, 0, true);
                if (PackageManagerService.DEBUG_PACKAGE_SCANNING && (i & Integer.MIN_VALUE) != 0 && sharedUserLPw != null) {
                    Log.d("PackageManager", "Shared UserID " + parsedPackage.getSharedUserId() + " (uid=" + sharedUserLPw.mAppId + "): packages=" + sharedUserLPw.getPackageStates());
                }
                sharedUserSettingLPr = packageLPr == null ? this.mPm.mSettings.getSharedUserSettingLPr(packageLPr) : null;
            }
            isLeavingSharedUser = parsedPackage.isLeavingSharedUser();
            if (isLeavingSharedUser) {
            }
            if (PackageManagerService.DEBUG_PACKAGE_SCANNING) {
                Log.d("PackageManager", "Shared UserID " + parsedPackage.getSharedUserId() + " (uid=" + sharedUserLPw.mAppId + "): packages=" + sharedUserLPw.getPackageStates());
            }
            if (packageLPr == null) {
            }
        }
        return new ScanRequest(parsedPackage, sharedUserSettingLPr, packageLPr == null ? null : packageLPr.getPkg(), packageLPr, sharedUserLPw, disabledSystemPkgLPr, originalPackageLocked, realPackageName, i, i2, platformPackage != null && platformPackage.getPackageName().equals(parsedPackage.getPackageName()), userHandle, str);
    }

    @GuardedBy({"mPm.mInstallLock"})
    private ScanResult scanPackageNewLI(ParsedPackage parsedPackage, int i, int i2, long j, UserHandle userHandle, String str) throws PackageManagerException {
        boolean z;
        ScanResult scanPackageOnlyLI;
        ScanRequest prepareInitialScanRequest = prepareInitialScanRequest(parsedPackage, i, i2, userHandle, str);
        PackageSetting packageSetting = prepareInitialScanRequest.mPkgSetting;
        PackageSetting packageSetting2 = prepareInitialScanRequest.mDisabledPkgSetting;
        if (packageSetting != null) {
            z = packageSetting.isUpdatedSystemApp();
        } else {
            z = packageSetting2 != null;
        }
        boolean z2 = z;
        int adjustScanFlags = adjustScanFlags(i2, packageSetting, packageSetting2, userHandle, parsedPackage);
        ScanPackageUtils.applyPolicy(parsedPackage, adjustScanFlags, this.mPm.getPlatformPackage(), z2);
        synchronized (this.mPm.mLock) {
            assertPackageIsValid(parsedPackage, i, adjustScanFlags);
            ScanRequest scanRequest = new ScanRequest(parsedPackage, prepareInitialScanRequest.mOldSharedUserSetting, prepareInitialScanRequest.mOldPkg, packageSetting, prepareInitialScanRequest.mSharedUserSetting, packageSetting2, prepareInitialScanRequest.mOriginalPkgSetting, prepareInitialScanRequest.mRealPkgName, i, i2, prepareInitialScanRequest.mIsPlatformPackage, userHandle, str);
            PackageManagerService packageManagerService = this.mPm;
            scanPackageOnlyLI = ScanPackageUtils.scanPackageOnlyLI(scanRequest, packageManagerService.mInjector, packageManagerService.mFactoryTest, j);
        }
        return scanPackageOnlyLI;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:110:0x02b9  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x03d1  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x03d9 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:153:0x0233  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0150  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0230  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private Pair<ScanResult, Boolean> scanSystemPackageLI(ParsedPackage parsedPackage, int i, int i2, UserHandle userHandle) throws PackageManagerException {
        PackageManagerTracedLock packageManagerTracedLock;
        PackageSetting packageSetting;
        ScanRequest scanRequest;
        int i3;
        int i4;
        int i5;
        int i6;
        PackageSetting packageSetting2;
        boolean z;
        int i7;
        String packageName;
        PackageManagerService packageManagerService;
        PackageSetting packageSetting3;
        boolean z2 = (i & 16) != 0;
        ScanRequest prepareInitialScanRequest = prepareInitialScanRequest(parsedPackage, i, i2, userHandle, null);
        PackageSetting packageSetting4 = prepareInitialScanRequest.mPkgSetting;
        PackageSetting packageSetting5 = prepareInitialScanRequest.mOriginalPkgSetting;
        PackageSetting packageSetting6 = packageSetting5 == null ? packageSetting4 : packageSetting5;
        boolean z3 = packageSetting6 != null;
        String packageName2 = z3 ? packageSetting6.getPackageName() : parsedPackage.getPackageName();
        PackageManagerTracedLock packageManagerTracedLock2 = this.mPm.mLock;
        synchronized (packageManagerTracedLock2) {
            try {
            } catch (Throwable th) {
                th = th;
            }
            try {
                boolean isDeviceUpgrading = this.mPm.isDeviceUpgrading();
                if (z2 && !z3 && this.mPm.mSettings.getDisabledSystemPkgLPr(packageName2) != null) {
                    Slog.w("PackageManager", "Inconsistent package setting of updated system app for " + packageName2 + ". To recover it, enable the system app and install it as non-updated system app.");
                    this.mPm.mSettings.removeDisabledSystemPackageLPw(packageName2);
                }
                PackageSetting disabledSystemPkgLPr = this.mPm.mSettings.getDisabledSystemPkgLPr(packageName2);
                boolean z4 = disabledSystemPkgLPr != null;
                if (PackageManagerService.DEBUG_INSTALL && z4) {
                    Slog.d("PackageManager", "updatedPkg = " + disabledSystemPkgLPr);
                }
                if (z2 && z4) {
                    packageManagerTracedLock = packageManagerTracedLock2;
                    packageSetting = packageSetting6;
                    scanRequest = prepareInitialScanRequest;
                    ScanRequest scanRequest2 = new ScanRequest(parsedPackage, this.mPm.mSettings.getSharedUserSettingLPr(disabledSystemPkgLPr), null, disabledSystemPkgLPr, prepareInitialScanRequest.mSharedUserSetting, null, null, null, i, i2, prepareInitialScanRequest.mIsPlatformPackage, userHandle, null);
                    i4 = i2;
                    i3 = 1;
                    ScanPackageUtils.applyPolicy(parsedPackage, i4, this.mPm.getPlatformPackage(), true);
                    PackageManagerService packageManagerService2 = this.mPm;
                    ScanResult scanPackageOnlyLI = ScanPackageUtils.scanPackageOnlyLI(scanRequest2, packageManagerService2.mInjector, packageManagerService2.mFactoryTest, -1L);
                    if (scanPackageOnlyLI.mExistingSettingCopied && (packageSetting3 = scanPackageOnlyLI.mRequest.mPkgSetting) != null) {
                        packageSetting3.updateFrom(scanPackageOnlyLI.mPkgSetting);
                    }
                } else {
                    packageManagerTracedLock = packageManagerTracedLock2;
                    packageSetting = packageSetting6;
                    scanRequest = prepareInitialScanRequest;
                    i3 = 1;
                    i4 = i2;
                }
                int i8 = (!z3 || packageSetting.getPathString().equals(parsedPackage.getPath())) ? 0 : i3;
                boolean z5 = (!z3 || parsedPackage.getLongVersionCode() <= packageSetting.getVersionCode()) ? 0 : i3;
                if (z3) {
                    ScanRequest scanRequest3 = scanRequest;
                    if (scanRequest3.mOldSharedUserSetting != scanRequest3.mSharedUserSetting) {
                        i5 = i3;
                        i6 = (z2 || !z4 || i8 == 0 || (z5 == 0 && i5 == 0)) ? 0 : i3;
                        if (i6 != 0) {
                            synchronized (this.mPm.mLock) {
                                this.mPm.mPackages.remove(packageSetting.getPackageName());
                            }
                            PackageManagerServiceUtils.logCriticalInfo(5, "System package updated; name: " + packageSetting.getPackageName() + "; " + packageSetting.getVersionCode() + " --> " + parsedPackage.getLongVersionCode() + "; " + packageSetting.getPathString() + " --> " + parsedPackage.getPath());
                            this.mRemovePackageHelper.cleanUpResources(new File(packageSetting.getPathString()), InstructionSets.getAppDexInstructionSets(packageSetting.getPrimaryCpuAbiLegacy(), packageSetting.getSecondaryCpuAbiLegacy()));
                            synchronized (this.mPm.mLock) {
                                this.mPm.mSettings.enableSystemPackageLPw(packageSetting.getPackageName());
                            }
                        }
                        if (!z2 && z4 && i6 == 0) {
                            parsedPackage.hideAsFinal();
                            StringBuilder sb = new StringBuilder();
                            sb.append("Package ");
                            sb.append(parsedPackage.getPackageName());
                            sb.append(" at ");
                            sb.append(parsedPackage.getPath());
                            sb.append(" ignored: updated version ");
                            sb.append(z3 ? String.valueOf(packageSetting.getVersionCode()) : "unknown");
                            sb.append(" better than this ");
                            sb.append(parsedPackage.getLongVersionCode());
                            throw PackageManagerException.ofInternalError(sb.toString(), -12);
                        }
                        boolean isApkVerificationForced = !z2 ? isDeviceUpgrading : PackageManagerServiceUtils.isApkVerificationForced(packageSetting);
                        if (PackageManagerService.DEBUG_VERIFY && isApkVerificationForced) {
                            Slog.d("PackageManager", "Force collect certificate of " + parsedPackage.getPackageName());
                        }
                        packageSetting2 = packageSetting;
                        ScanPackageUtils.collectCertificatesLI(packageSetting2, parsedPackage, this.mPm.getSettingsVersionForPackage(parsedPackage), isApkVerificationForced, (!z2 || (isApkVerificationForced && canSkipForcedPackageVerification(parsedPackage))) ? i3 : 0, this.mPm.isPreNMR1Upgrade());
                        maybeClearProfilesForUpgradesLI(packageSetting2, parsedPackage);
                        if (z2 && !z4 && z3 && !packageSetting2.isSystem()) {
                            if (!parsedPackage.getSigningDetails().checkCapability(packageSetting2.getSigningDetails(), i3) && !packageSetting2.getSigningDetails().checkCapability(parsedPackage.getSigningDetails(), 8)) {
                                packageManagerService = this.mPm;
                                if (!packageManagerService.mPackageManagerServiceExt.skipSigCheckWhenDataToSystemInAddForInitLI(parsedPackage, z5, packageManagerService.mSettings.getInternalVersion().sdkVersion)) {
                                    PackageManagerServiceUtils.logCriticalInfo(5, "System package signature mismatch; name: " + packageSetting2.getPackageName());
                                    PackageFreezer freezePackage = this.mPm.freezePackage(parsedPackage.getPackageName(), -1, "scanPackageInternalLI", 13);
                                    try {
                                        new DeletePackageHelper(this.mPm).deletePackageLIF(parsedPackage.getPackageName(), null, true, this.mPm.mUserManager.getUserIds(), 0, null, false);
                                        if (freezePackage != null) {
                                            freezePackage.close();
                                        }
                                    } finally {
                                    }
                                }
                            }
                            if (z5 == 0 || i5 != 0) {
                                PackageManagerServiceUtils.logCriticalInfo(5, "System package enabled; name: " + packageSetting2.getPackageName() + "; " + packageSetting2.getVersionCode() + " --> " + parsedPackage.getLongVersionCode() + "; " + packageSetting2.getPathString() + " --> " + parsedPackage.getPath());
                                this.mRemovePackageHelper.cleanUpResources(new File(packageSetting2.getPathString()), InstructionSets.getAppDexInstructionSets(packageSetting2.getPrimaryCpuAbiLegacy(), packageSetting2.getSecondaryCpuAbiLegacy()));
                            } else {
                                PackageManagerServiceUtils.logCriticalInfo(4, "System package disabled; name: " + packageSetting2.getPackageName() + "; old: " + packageSetting2.getPathString() + " @ " + packageSetting2.getVersionCode() + "; new: " + parsedPackage.getPath() + " @ " + parsedPackage.getPath());
                                z = i3;
                                int i9 = (67108864 & i4) != 0 ? i3 : 0;
                                if (this.mPm.mShouldStopSystemPackagesByDefault && z2 && !z3 && i9 == 0 && !parsedPackage.isOverlayIsStatic()) {
                                    packageName = parsedPackage.getPackageName();
                                    if (!PackageManagerService.PLATFORM_PACKAGE_NAME.contentEquals(packageName) && !this.mPm.mInitialNonStoppedSystemPackages.contains(packageName) && hasLauncherEntry(parsedPackage)) {
                                        i7 = 134217728 | i4;
                                        return new Pair<>(scanPackageNewLI(parsedPackage, i, i7 | 2, 0L, userHandle, null), Boolean.valueOf(z));
                                    }
                                }
                                i7 = i4;
                                return new Pair<>(scanPackageNewLI(parsedPackage, i, i7 | 2, 0L, userHandle, null), Boolean.valueOf(z));
                            }
                        }
                        z = 0;
                        if ((67108864 & i4) != 0) {
                        }
                        if (this.mPm.mShouldStopSystemPackagesByDefault) {
                            packageName = parsedPackage.getPackageName();
                            if (!PackageManagerService.PLATFORM_PACKAGE_NAME.contentEquals(packageName)) {
                                i7 = 134217728 | i4;
                                return new Pair<>(scanPackageNewLI(parsedPackage, i, i7 | 2, 0L, userHandle, null), Boolean.valueOf(z));
                            }
                        }
                        i7 = i4;
                        return new Pair<>(scanPackageNewLI(parsedPackage, i, i7 | 2, 0L, userHandle, null), Boolean.valueOf(z));
                    }
                }
                i5 = 0;
                if (z2) {
                }
                if (i6 != 0) {
                }
                if (!z2) {
                }
                if (!z2) {
                }
                if (PackageManagerService.DEBUG_VERIFY) {
                    Slog.d("PackageManager", "Force collect certificate of " + parsedPackage.getPackageName());
                }
                packageSetting2 = packageSetting;
                ScanPackageUtils.collectCertificatesLI(packageSetting2, parsedPackage, this.mPm.getSettingsVersionForPackage(parsedPackage), isApkVerificationForced, (!z2 || (isApkVerificationForced && canSkipForcedPackageVerification(parsedPackage))) ? i3 : 0, this.mPm.isPreNMR1Upgrade());
                maybeClearProfilesForUpgradesLI(packageSetting2, parsedPackage);
                if (z2) {
                    if (!parsedPackage.getSigningDetails().checkCapability(packageSetting2.getSigningDetails(), i3)) {
                        packageManagerService = this.mPm;
                        if (!packageManagerService.mPackageManagerServiceExt.skipSigCheckWhenDataToSystemInAddForInitLI(parsedPackage, z5, packageManagerService.mSettings.getInternalVersion().sdkVersion)) {
                        }
                    }
                    if (z5 == 0) {
                    }
                    PackageManagerServiceUtils.logCriticalInfo(5, "System package enabled; name: " + packageSetting2.getPackageName() + "; " + packageSetting2.getVersionCode() + " --> " + parsedPackage.getLongVersionCode() + "; " + packageSetting2.getPathString() + " --> " + parsedPackage.getPath());
                    this.mRemovePackageHelper.cleanUpResources(new File(packageSetting2.getPathString()), InstructionSets.getAppDexInstructionSets(packageSetting2.getPrimaryCpuAbiLegacy(), packageSetting2.getSecondaryCpuAbiLegacy()));
                }
                z = 0;
                if ((67108864 & i4) != 0) {
                }
                if (this.mPm.mShouldStopSystemPackagesByDefault) {
                }
                i7 = i4;
                return new Pair<>(scanPackageNewLI(parsedPackage, i, i7 | 2, 0L, userHandle, null), Boolean.valueOf(z));
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        }
        throw th;
    }

    private static boolean hasLauncherEntry(ParsedPackage parsedPackage) {
        HashSet hashSet = new HashSet();
        hashSet.add("android.intent.category.LAUNCHER");
        List<ParsedActivity> activities = parsedPackage.getActivities();
        for (int i = 0; i < activities.size(); i++) {
            ParsedActivity parsedActivity = activities.get(i);
            if (parsedActivity.isEnabled() && parsedActivity.isExported()) {
                List<ParsedIntentInfo> intents = parsedActivity.getIntents();
                for (int i2 = 0; i2 < intents.size(); i2++) {
                    IntentFilter intentFilter = intents.get(i2).getIntentFilter();
                    if (intentFilter != null && intentFilter.matchCategories(hashSet) == null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean canSkipForcedPackageVerification(AndroidPackage androidPackage) {
        if (!VerityUtils.hasFsverity(androidPackage.getBaseApkPath())) {
            return false;
        }
        String[] splitCodePaths = androidPackage.getSplitCodePaths();
        if (ArrayUtils.isEmpty(splitCodePaths)) {
            return true;
        }
        for (String str : splitCodePaths) {
            if (!VerityUtils.hasFsverity(str)) {
                return false;
            }
        }
        return true;
    }

    private void maybeClearProfilesForUpgradesLI(PackageSetting packageSetting, AndroidPackage androidPackage) {
        if (packageSetting == null || !this.mPm.isDeviceUpgrading() || packageSetting.getVersionCode() == androidPackage.getLongVersionCode()) {
            return;
        }
        this.mAppDataHelper.clearAppProfilesLIF(androidPackage);
        if (PackageManagerService.DEBUG_INSTALL) {
            Slog.d("PackageManager", packageSetting.getPackageName() + " clear profile due to version change " + packageSetting.getVersionCode() + " != " + androidPackage.getLongVersionCode());
        }
    }

    @GuardedBy({"mPm.mLock"})
    private PackageSetting getOriginalPackageLocked(AndroidPackage androidPackage, String str) {
        if (ScanPackageUtils.isPackageRenamed(androidPackage, str)) {
            return null;
        }
        for (int size = ArrayUtils.size(androidPackage.getOriginalPackages()) - 1; size >= 0; size--) {
            PackageSetting packageLPr = this.mPm.mSettings.getPackageLPr(androidPackage.getOriginalPackages().get(size));
            if (packageLPr != null && verifyPackageUpdateLPr(packageLPr, androidPackage)) {
                if (this.mPm.mSettings.getSharedUserSettingLPr(packageLPr) != null) {
                    String str2 = this.mPm.mSettings.getSharedUserSettingLPr(packageLPr).name;
                    if (!str2.equals(androidPackage.getSharedUserId())) {
                        Slog.w("PackageManager", "Unable to migrate data from " + packageLPr.getPackageName() + " to " + androidPackage.getPackageName() + ": old shared user settings name " + str2 + " differs from " + androidPackage.getSharedUserId());
                    }
                } else if (PackageManagerService.DEBUG_UPGRADE) {
                    Log.v("PackageManager", "Renaming new package " + androidPackage.getPackageName() + " to old name " + packageLPr.getPackageName());
                }
                return packageLPr;
            }
        }
        return null;
    }

    @GuardedBy({"mPm.mLock"})
    private boolean verifyPackageUpdateLPr(PackageSetting packageSetting, AndroidPackage androidPackage) {
        if ((packageSetting.getFlags() & 1) == 0) {
            Slog.w("PackageManager", "Unable to update from " + packageSetting.getPackageName() + " to " + androidPackage.getPackageName() + ": old package not in system partition");
            return false;
        }
        if (this.mPm.mPackages.get(packageSetting.getPackageName()) == null) {
            return true;
        }
        Slog.w("PackageManager", "Unable to update from " + packageSetting.getPackageName() + " to " + androidPackage.getPackageName() + ": old package still exists");
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:68:0x01f2, code lost:
    
        r6.mPm.mComponentResolver.assertProvidersNotDefined(r7);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void assertPackageIsValid(AndroidPackage androidPackage, int i, int i2) throws PackageManagerException {
        if ((i & 64) != 0) {
            ScanPackageUtils.assertCodePolicy(androidPackage);
        }
        if (androidPackage.getPath() == null) {
            throw new PackageManagerException(-2, "Code and resource paths haven't been set correctly");
        }
        boolean z = (i2 & 16) == 0;
        boolean z2 = (i2 & 4096) != 0;
        boolean z3 = (67108864 & i2) != 0;
        if ((z || z2) && this.mPm.snapshotComputer().isApexPackage(androidPackage.getPackageName()) && !z3) {
            throw new PackageManagerException(-5, androidPackage.getPackageName() + " is an APEX package and can't be installed as an APK.");
        }
        this.mPm.mSettings.getKeySetManagerService().assertScannedPackageValid(androidPackage);
        synchronized (this.mPm.mLock) {
            if (androidPackage.getPackageName().equals(PackageManagerService.PLATFORM_PACKAGE_NAME) && this.mPm.getCoreAndroidApplication() != null) {
                Slog.w("PackageManager", "*************************************************");
                Slog.w("PackageManager", "Core android package being redefined.  Skipping.");
                Slog.w("PackageManager", " codePath=" + androidPackage.getPath());
                Slog.w("PackageManager", "*************************************************");
                throw new PackageManagerException(-5, "Core android package being redefined.  Skipping.");
            }
            int i3 = i2 & 4;
            if (i3 == 0 && this.mPm.mPackages.containsKey(androidPackage.getPackageName())) {
                throw new PackageManagerException(-5, "Application package " + androidPackage.getPackageName() + " already installed.  Skipping duplicate.");
            }
            if (androidPackage.isStaticSharedLibrary()) {
                if (i3 == 0 && this.mPm.mPackages.containsKey(androidPackage.getManifestPackageName())) {
                    throw PackageManagerException.ofInternalError("Duplicate static shared lib provider package", -13);
                }
                ScanPackageUtils.assertStaticSharedLibraryIsValid(androidPackage, i2);
                assertStaticSharedLibraryVersionCodeIsValid(androidPackage);
            }
            if ((i2 & 128) != 0) {
                if (this.mPm.isExpectingBetter(androidPackage.getPackageName())) {
                    Slog.w("PackageManager", "Relax SCAN_REQUIRE_KNOWN requirement for package " + androidPackage.getPackageName());
                } else {
                    PackageSetting packageLPr = this.mPm.mSettings.getPackageLPr(androidPackage.getPackageName());
                    if (packageLPr != null) {
                        if (PackageManagerService.DEBUG_PACKAGE_SCANNING) {
                            Log.d("PackageManager", "Examining " + androidPackage.getPath() + " and requiring known path " + packageLPr.getPathString());
                        }
                        if (!androidPackage.getPath().equals(packageLPr.getPathString()) && !this.mPm.mPackageManagerServiceExt.hookOplusOtaPs(packageLPr)) {
                            throw new PackageManagerException(-23, "Application package " + androidPackage.getPackageName() + " found at " + androidPackage.getPath() + " but expected at " + packageLPr.getPathString() + "; ignoring.");
                        }
                    } else if (!this.mPm.mPackageManagerServiceExt.allowUnknownWhenScanRequireKnownInAssertPackageIsValid(androidPackage)) {
                        throw new PackageManagerException(-19, "Application package " + androidPackage.getPackageName() + " not found; ignoring.");
                    }
                }
            }
            ScanPackageUtils.assertProcessesAreValid(androidPackage);
            assertPackageWithSharedUserIdIsPrivileged(androidPackage);
            if (androidPackage.getOverlayTarget() != null) {
                assertOverlayIsValid(androidPackage, i, i2);
            }
            ScanPackageUtils.assertMinSignatureSchemeIsValid(androidPackage, i);
        }
    }

    private void assertStaticSharedLibraryVersionCodeIsValid(AndroidPackage androidPackage) throws PackageManagerException {
        WatchedLongSparseArray<SharedLibraryInfo> sharedLibraryInfos = this.mSharedLibraries.getSharedLibraryInfos(androidPackage.getStaticSharedLibraryName());
        long j = Long.MIN_VALUE;
        long j2 = JobStatus.NO_LATEST_RUNTIME;
        if (sharedLibraryInfos != null) {
            int size = sharedLibraryInfos.size();
            int i = 0;
            while (true) {
                if (i >= size) {
                    break;
                }
                SharedLibraryInfo valueAt = sharedLibraryInfos.valueAt(i);
                long longVersionCode = valueAt.getDeclaringPackage().getLongVersionCode();
                if (valueAt.getLongVersion() >= androidPackage.getStaticSharedLibraryVersion()) {
                    if (valueAt.getLongVersion() <= androidPackage.getStaticSharedLibraryVersion()) {
                        j = longVersionCode;
                        j2 = j;
                        break;
                    }
                    j2 = Math.min(j2, longVersionCode - 1);
                } else {
                    j = Math.max(j, longVersionCode + 1);
                }
                i++;
            }
        }
        if (androidPackage.getLongVersionCode() < j || androidPackage.getLongVersionCode() > j2) {
            throw PackageManagerException.ofInternalError("Static shared lib version codes must be ordered as lib versions", -14);
        }
    }

    private void assertOverlayIsValid(AndroidPackage androidPackage, int i, int i2) throws PackageManagerException {
        PackageSetting packageLPr;
        PackageSetting packageLPr2;
        PackageSetting packageLPr3;
        if ((65536 & i2) != 0) {
            if ((i & 16) == 0) {
                if (this.mPm.isOverlayMutable(androidPackage.getPackageName())) {
                    return;
                }
                throw PackageManagerException.ofInternalError("Overlay " + androidPackage.getPackageName() + " is static and cannot be upgraded.", -15);
            }
            if ((524288 & i2) != 0) {
                if (androidPackage.getTargetSdkVersion() < ScanPackageUtils.getVendorPartitionVersion()) {
                    Slog.w("PackageManager", "System overlay " + androidPackage.getPackageName() + " targets an SDK below the required SDK level of vendor overlays (" + ScanPackageUtils.getVendorPartitionVersion() + "). This will become an install error in a future release");
                    return;
                }
                return;
            }
            int targetSdkVersion = androidPackage.getTargetSdkVersion();
            int i3 = Build.VERSION.SDK_INT;
            if (targetSdkVersion < i3) {
                Slog.w("PackageManager", "System overlay " + androidPackage.getPackageName() + " targets an SDK below the required SDK level of system overlays (" + i3 + "). This will become an install error in a future release");
                return;
            }
            return;
        }
        if (androidPackage.getTargetSdkVersion() < 29) {
            synchronized (this.mPm.mLock) {
                packageLPr3 = this.mPm.mSettings.getPackageLPr(PackageManagerService.PLATFORM_PACKAGE_NAME);
            }
            if (!PackageManagerServiceUtils.comparePackageSignatures(packageLPr3, androidPackage.getSigningDetails().getSignatures())) {
                throw PackageManagerException.ofInternalError("Overlay " + androidPackage.getPackageName() + " must target Q or later, or be signed with the platform certificate", -16);
            }
        }
        if (androidPackage.getOverlayTargetOverlayableName() == null) {
            synchronized (this.mPm.mLock) {
                packageLPr = this.mPm.mSettings.getPackageLPr(androidPackage.getOverlayTarget());
            }
            if (packageLPr == null || PackageManagerServiceUtils.comparePackageSignatures(packageLPr, androidPackage.getSigningDetails().getSignatures())) {
                return;
            }
            PackageManagerService packageManagerService = this.mPm;
            if (packageManagerService.mOverlayConfigSignaturePackage == null) {
                throw PackageManagerException.ofInternalError("Overlay " + androidPackage.getPackageName() + " and target " + androidPackage.getOverlayTarget() + " signed with different certificates, and the overlay lacks <overlay android:targetName>", -17);
            }
            synchronized (packageManagerService.mLock) {
                PackageManagerService packageManagerService2 = this.mPm;
                packageLPr2 = packageManagerService2.mSettings.getPackageLPr(packageManagerService2.mOverlayConfigSignaturePackage);
            }
            if (PackageManagerServiceUtils.comparePackageSignatures(packageLPr2, androidPackage.getSigningDetails().getSignatures())) {
                return;
            }
            throw PackageManagerException.ofInternalError("Overlay " + androidPackage.getPackageName() + " signed with a different certificate than both the reference package and target " + androidPackage.getOverlayTarget() + ", and the overlay lacks <overlay android:targetName>", -18);
        }
    }

    private void assertPackageWithSharedUserIdIsPrivileged(AndroidPackage androidPackage) throws PackageManagerException {
        PackageSetting packageLPr;
        if (AndroidPackageUtils.isPrivileged(androidPackage) || androidPackage.getSharedUserId() == null || androidPackage.isLeavingSharedUser()) {
            return;
        }
        SharedUserSetting sharedUserSetting = null;
        try {
            synchronized (this.mPm.mLock) {
                sharedUserSetting = this.mPm.mSettings.getSharedUserLPw(androidPackage.getSharedUserId(), 0, 0, false);
            }
        } catch (PackageManagerException unused) {
        }
        if (sharedUserSetting == null || !sharedUserSetting.isPrivileged()) {
            return;
        }
        synchronized (this.mPm.mLock) {
            packageLPr = this.mPm.mSettings.getPackageLPr(PackageManagerService.PLATFORM_PACKAGE_NAME);
        }
        if (PackageManagerServiceUtils.comparePackageSignatures(packageLPr, androidPackage.getSigningDetails().getSignatures())) {
            return;
        }
        throw PackageManagerException.ofInternalError("Apps that share a user with a privileged app must themselves be marked as privileged. " + androidPackage.getPackageName() + " shares privileged user " + androidPackage.getSharedUserId() + ".", -19);
    }

    private int adjustScanFlags(int i, PackageSetting packageSetting, PackageSetting packageSetting2, UserHandle userHandle, AndroidPackage androidPackage) {
        SharedUserSetting sharedUserSetting;
        int adjustScanFlagsWithPackageSetting = ScanPackageUtils.adjustScanFlagsWithPackageSetting(i, packageSetting, packageSetting2, userHandle);
        boolean z = (524288 & adjustScanFlagsWithPackageSetting) != 0 && ScanPackageUtils.getVendorPartitionVersion() < 28;
        if ((adjustScanFlagsWithPackageSetting & 131072) == 0 && !AndroidPackageUtils.isPrivileged(androidPackage) && androidPackage.getSharedUserId() != null && !z && !androidPackage.isLeavingSharedUser()) {
            synchronized (this.mPm.mLock) {
                try {
                    sharedUserSetting = this.mPm.mSettings.getSharedUserLPw(androidPackage.getSharedUserId(), 0, 0, false);
                } catch (PackageManagerException unused) {
                    sharedUserSetting = null;
                }
                if (sharedUserSetting != null && sharedUserSetting.isPrivileged() && PackageManagerServiceUtils.compareSignatures(this.mPm.mSettings.getPackageLPr(PackageManagerService.PLATFORM_PACKAGE_NAME).getSigningDetails().getSignatures(), androidPackage.getSigningDetails().getSignatures()) != 0) {
                    adjustScanFlagsWithPackageSetting |= 131072;
                }
            }
        }
        return adjustScanFlagsWithPackageSetting;
    }

    public IInstallPackageHelperWrapper getWrapper() {
        return this.mWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class InstallPackageHelperWrapper implements IInstallPackageHelperWrapper {
        private InstallPackageHelperWrapper() {
        }

        @Override // com.android.server.pm.IInstallPackageHelperWrapper
        public AndroidPackage addForInitLI(ParsedPackage parsedPackage, int i, int i2, UserHandle userHandle, ApexManager.ActiveApexInfo activeApexInfo) throws PackageManagerException {
            return InstallPackageHelper.this.addForInitLI(parsedPackage, i, i2, userHandle, activeApexInfo);
        }
    }
}
