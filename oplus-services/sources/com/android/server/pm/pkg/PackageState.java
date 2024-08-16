package com.android.server.pm.pkg;

import android.annotation.SystemApi;
import android.content.pm.SigningInfo;
import android.os.UserHandle;
import android.processor.immutability.Immutable;
import android.util.SparseArray;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Immutable
@SystemApi(client = SystemApi.Client.SYSTEM_SERVER)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface PackageState {
    AndroidPackage getAndroidPackage();

    String getApexModuleName();

    int getAppId();

    int getCategoryOverride();

    String getCpuAbiOverride();

    int getHiddenApiEnforcementPolicy();

    long getLastModifiedTime();

    @Immutable.Ignore
    long[] getLastPackageUsageTime();

    long getLastUpdateTime();

    Map<String, Set<String>> getMimeGroups();

    String getPackageName();

    File getPath();

    String getPrimaryCpuAbi();

    String getSeInfo();

    String getSecondaryCpuAbi();

    List<SharedLibrary> getSharedLibraryDependencies();

    int getSharedUserAppId();

    @Immutable.Ignore
    SigningInfo getSigningInfo();

    PackageUserState getStateForUser(UserHandle userHandle);

    @Immutable.Ignore
    SparseArray<? extends PackageUserState> getUserStates();

    List<String> getUsesLibraryFiles();

    @Immutable.Ignore
    String[] getUsesSdkLibraries();

    @Immutable.Ignore
    long[] getUsesSdkLibrariesVersionsMajor();

    @Immutable.Ignore
    String[] getUsesStaticLibraries();

    @Immutable.Ignore
    long[] getUsesStaticLibrariesVersions();

    long getVersionCode();

    String getVolumeUuid();

    boolean hasSharedUser();

    boolean isApex();

    boolean isApkInUpdatedApex();

    boolean isExternalStorage();

    boolean isForceQueryableOverride();

    boolean isHiddenUntilInstalled();

    boolean isInstallPermissionsFixed();

    boolean isOdm();

    boolean isOem();

    boolean isPrivileged();

    boolean isProduct();

    boolean isRequiredForSystemUser();

    boolean isSystem();

    boolean isSystemExt();

    boolean isUpdateAvailable();

    boolean isUpdatedSystemApp();

    boolean isVendor();

    default PackageUserState getUserStateOrDefault(int i) {
        PackageUserState packageUserState = getUserStates().get(i);
        return packageUserState == null ? PackageUserState.DEFAULT : packageUserState;
    }
}
