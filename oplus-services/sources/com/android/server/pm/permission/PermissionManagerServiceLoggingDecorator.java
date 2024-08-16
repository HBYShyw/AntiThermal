package com.android.server.pm.permission;

import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.permission.SplitPermissionInfoParcelable;
import android.permission.IOnPermissionsChangeListener;
import android.util.Log;
import com.android.server.pm.permission.PermissionManagerServiceInternal;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageState;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PermissionManagerServiceLoggingDecorator implements PermissionManagerServiceInterface {
    private static final String LOG_TAG = PermissionManagerServiceLoggingDecorator.class.getSimpleName();
    private final PermissionManagerServiceInterface mService;

    public PermissionManagerServiceLoggingDecorator(PermissionManagerServiceInterface permissionManagerServiceInterface) {
        this.mService = permissionManagerServiceInterface;
    }

    public byte[] backupRuntimePermissions(int i) {
        Log.i(LOG_TAG, "backupRuntimePermissions(userId = " + i + ")");
        return this.mService.backupRuntimePermissions(i);
    }

    public void restoreRuntimePermissions(byte[] bArr, int i) {
        Log.i(LOG_TAG, "restoreRuntimePermissions(backup = " + bArr + ", userId = " + i + ")");
        this.mService.restoreRuntimePermissions(bArr, i);
    }

    public void restoreDelayedRuntimePermissions(String str, int i) {
        Log.i(LOG_TAG, "restoreDelayedRuntimePermissions(packageName = " + str + ", userId = " + i + ")");
        this.mService.restoreDelayedRuntimePermissions(str, i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        Log.i(LOG_TAG, "dump(fd = " + fileDescriptor + ", pw = " + printWriter + ", args = " + Arrays.toString(strArr) + ")");
        this.mService.dump(fileDescriptor, printWriter, strArr);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public List<PermissionGroupInfo> getAllPermissionGroups(int i) {
        Log.i(LOG_TAG, "getAllPermissionGroups(flags = " + i + ")");
        return this.mService.getAllPermissionGroups(i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public PermissionGroupInfo getPermissionGroupInfo(String str, int i) {
        Log.i(LOG_TAG, "getPermissionGroupInfo(groupName = " + str + ", flags = " + i + ")");
        return this.mService.getPermissionGroupInfo(str, i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public PermissionInfo getPermissionInfo(String str, int i, String str2) {
        Log.i(LOG_TAG, "getPermissionInfo(permName = " + str + ", flags = " + i + ", opPackageName = " + str2 + ")");
        return this.mService.getPermissionInfo(str, i, str2);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public List<PermissionInfo> queryPermissionsByGroup(String str, int i) {
        Log.i(LOG_TAG, "queryPermissionsByGroup(groupName = " + str + ", flags = " + i + ")");
        return this.mService.queryPermissionsByGroup(str, i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean addPermission(PermissionInfo permissionInfo, boolean z) {
        Log.i(LOG_TAG, "addPermission(info = " + permissionInfo + ", async = " + z + ")");
        return this.mService.addPermission(permissionInfo, z);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void removePermission(String str) {
        Log.i(LOG_TAG, "removePermission(permName = " + str + ")");
        this.mService.removePermission(str);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int getPermissionFlags(String str, String str2, int i) {
        Log.i(LOG_TAG, "getPermissionFlags(packageName = " + str + ", permName = " + str2 + ", userId = " + i + ")");
        return this.mService.getPermissionFlags(str, str2, i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void updatePermissionFlags(String str, String str2, int i, int i2, boolean z, int i3) {
        Log.i(LOG_TAG, "updatePermissionFlags(packageName = " + str + ", permName = " + str2 + ", flagMask = " + i + ", flagValues = " + i2 + ", checkAdjustPolicyFlagPermission = " + z + ", userId = " + i3 + ")");
        this.mService.updatePermissionFlags(str, str2, i, i2, z, i3);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void updatePermissionFlagsForAllApps(int i, int i2, int i3) {
        Log.i(LOG_TAG, "updatePermissionFlagsForAllApps(flagMask = " + i + ", flagValues = " + i2 + ", userId = " + i3 + ")");
        this.mService.updatePermissionFlagsForAllApps(i, i2, i3);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void addOnPermissionsChangeListener(IOnPermissionsChangeListener iOnPermissionsChangeListener) {
        Log.i(LOG_TAG, "addOnPermissionsChangeListener(listener = " + iOnPermissionsChangeListener + ")");
        this.mService.addOnPermissionsChangeListener(iOnPermissionsChangeListener);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void removeOnPermissionsChangeListener(IOnPermissionsChangeListener iOnPermissionsChangeListener) {
        Log.i(LOG_TAG, "removeOnPermissionsChangeListener(listener = " + iOnPermissionsChangeListener + ")");
        this.mService.removeOnPermissionsChangeListener(iOnPermissionsChangeListener);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean addAllowlistedRestrictedPermission(String str, String str2, int i, int i2) {
        Log.i(LOG_TAG, "addAllowlistedRestrictedPermission(packageName = " + str + ", permName = " + str2 + ", flags = " + i + ", userId = " + i2 + ")");
        return this.mService.addAllowlistedRestrictedPermission(str, str2, i, i2);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public List<String> getAllowlistedRestrictedPermissions(String str, int i, int i2) {
        Log.i(LOG_TAG, "getAllowlistedRestrictedPermissions(packageName = " + str + ", flags = " + i + ", userId = " + i2 + ")");
        return this.mService.getAllowlistedRestrictedPermissions(str, i, i2);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean removeAllowlistedRestrictedPermission(String str, String str2, int i, int i2) {
        Log.i(LOG_TAG, "removeAllowlistedRestrictedPermission(packageName = " + str + ", permName = " + str2 + ", flags = " + i + ", userId = " + i2 + ")");
        return this.mService.removeAllowlistedRestrictedPermission(str, str2, i, i2);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void grantRuntimePermission(String str, String str2, int i) {
        Log.i(LOG_TAG, "grantRuntimePermission(packageName = " + str + ", permName = " + str2 + ", userId = " + i + ")");
        this.mService.grantRuntimePermission(str, str2, i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void revokeRuntimePermission(String str, String str2, int i, String str3) {
        Log.i(LOG_TAG, "revokeRuntimePermission(packageName = " + str + ", permName = " + str2 + ", userId = " + i + ", reason = " + str3 + ")");
        this.mService.revokeRuntimePermission(str, str2, i, str3);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void revokePostNotificationPermissionWithoutKillForTest(String str, int i) {
        Log.i(LOG_TAG, "revokePostNotificationPermissionWithoutKillForTest(packageName = " + str + ", userId = " + i + ")");
        this.mService.revokePostNotificationPermissionWithoutKillForTest(str, i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean shouldShowRequestPermissionRationale(String str, String str2, int i) {
        Log.i(LOG_TAG, "shouldShowRequestPermissionRationale(packageName = " + str + ", permName = " + str2 + ", userId = " + i + ")");
        return this.mService.shouldShowRequestPermissionRationale(str, str2, i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean isPermissionRevokedByPolicy(String str, String str2, int i) {
        Log.i(LOG_TAG, "isPermissionRevokedByPolicy(packageName = " + str + ", permName = " + str2 + ", userId = " + i + ")");
        return this.mService.isPermissionRevokedByPolicy(str, str2, i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public List<SplitPermissionInfoParcelable> getSplitPermissions() {
        Log.i(LOG_TAG, "getSplitPermissions()");
        return this.mService.getSplitPermissions();
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int checkPermission(String str, String str2, int i) {
        Log.i(LOG_TAG, "checkPermission(pkgName = " + str + ", permName = " + str2 + ", userId = " + i + ")");
        return this.mService.checkPermission(str, str2, i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int checkUidPermission(int i, String str) {
        Log.i(LOG_TAG, "checkUidPermission(uid = " + i + ", permName = " + str + ")");
        return this.mService.checkUidPermission(i, str);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void addOnRuntimePermissionStateChangedListener(PermissionManagerServiceInternal.OnRuntimePermissionStateChangedListener onRuntimePermissionStateChangedListener) {
        Log.i(LOG_TAG, "addOnRuntimePermissionStateChangedListener(listener = " + onRuntimePermissionStateChangedListener + ")");
        this.mService.addOnRuntimePermissionStateChangedListener(onRuntimePermissionStateChangedListener);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void removeOnRuntimePermissionStateChangedListener(PermissionManagerServiceInternal.OnRuntimePermissionStateChangedListener onRuntimePermissionStateChangedListener) {
        Log.i(LOG_TAG, "removeOnRuntimePermissionStateChangedListener(listener = " + onRuntimePermissionStateChangedListener + ")");
        this.mService.removeOnRuntimePermissionStateChangedListener(onRuntimePermissionStateChangedListener);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public Map<String, Set<String>> getAllAppOpPermissionPackages() {
        Log.i(LOG_TAG, "getAllAppOpPermissionPackages()");
        return this.mService.getAllAppOpPermissionPackages();
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean isPermissionsReviewRequired(String str, int i) {
        Log.i(LOG_TAG, "isPermissionsReviewRequired(packageName = " + str + ", userId = " + i + ")");
        return this.mService.isPermissionsReviewRequired(str, i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void resetRuntimePermissions(AndroidPackage androidPackage, int i) {
        Log.i(LOG_TAG, "resetRuntimePermissions(pkg = " + androidPackage + ", userId = " + i + ")");
        this.mService.resetRuntimePermissions(androidPackage, i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void resetRuntimePermissionsForUser(int i) {
        Log.i(LOG_TAG, "resetRuntimePermissionsForUser(userId = " + i + ")");
        this.mService.resetRuntimePermissionsForUser(i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void readLegacyPermissionStateTEMP() {
        Log.i(LOG_TAG, "readLegacyPermissionStateTEMP()");
        this.mService.readLegacyPermissionStateTEMP();
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void writeLegacyPermissionStateTEMP() {
        Log.i(LOG_TAG, "writeLegacyPermissionStateTEMP()");
        this.mService.writeLegacyPermissionStateTEMP();
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public Set<String> getInstalledPermissions(String str) {
        Log.i(LOG_TAG, "getInstalledPermissions(packageName = " + str + ")");
        return this.mService.getInstalledPermissions(str);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public Set<String> getGrantedPermissions(String str, int i) {
        Log.i(LOG_TAG, "getGrantedPermissions(packageName = " + str + ", userId = " + i + ")");
        return this.mService.getGrantedPermissions(str, i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int[] getPermissionGids(String str, int i) {
        Log.i(LOG_TAG, "getPermissionGids(permissionName = " + str + ", userId = " + i + ")");
        return this.mService.getPermissionGids(str, i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public String[] getAppOpPermissionPackages(String str) {
        Log.i(LOG_TAG, "getAppOpPermissionPackages(permissionName = " + str + ")");
        return this.mService.getAppOpPermissionPackages(str);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public Permission getPermissionTEMP(String str) {
        Log.i(LOG_TAG, "getPermissionTEMP(permName = " + str + ")");
        return this.mService.getPermissionTEMP(str);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public List<PermissionInfo> getAllPermissionsWithProtection(int i) {
        Log.i(LOG_TAG, "getAllPermissionsWithProtection(protection = " + i + ")");
        return this.mService.getAllPermissionsWithProtection(i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public List<PermissionInfo> getAllPermissionsWithProtectionFlags(int i) {
        Log.i(LOG_TAG, "getAllPermissionsWithProtectionFlags(protectionFlags = " + i + ")");
        return this.mService.getAllPermissionsWithProtectionFlags(i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public List<LegacyPermission> getLegacyPermissions() {
        Log.i(LOG_TAG, "getLegacyPermissions()");
        return this.mService.getLegacyPermissions();
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public LegacyPermissionState getLegacyPermissionState(int i) {
        Log.i(LOG_TAG, "getLegacyPermissionState(appId = " + i + ")");
        return this.mService.getLegacyPermissionState(i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void readLegacyPermissionsTEMP(LegacyPermissionSettings legacyPermissionSettings) {
        Log.i(LOG_TAG, "readLegacyPermissionsTEMP(legacyPermissionSettings = " + legacyPermissionSettings + ")");
        this.mService.readLegacyPermissionsTEMP(legacyPermissionSettings);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void writeLegacyPermissionsTEMP(LegacyPermissionSettings legacyPermissionSettings) {
        Log.i(LOG_TAG, "writeLegacyPermissionsTEMP(legacyPermissionSettings = " + legacyPermissionSettings + ")");
        this.mService.writeLegacyPermissionsTEMP(legacyPermissionSettings);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onSystemReady() {
        Log.i(LOG_TAG, "onSystemReady()");
        this.mService.onSystemReady();
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onStorageVolumeMounted(String str, boolean z) {
        Log.i(LOG_TAG, "onStorageVolumeMounted(volumeUuid = " + str + ", fingerprintChanged = " + z + ")");
        this.mService.onStorageVolumeMounted(str, z);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int[] getGidsForUid(int i) {
        Log.i(LOG_TAG, "getGidsForUid(uid = " + i + ")");
        return this.mService.getGidsForUid(i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onUserCreated(int i) {
        Log.i(LOG_TAG, "onUserCreated(userId = " + i + ")");
        this.mService.onUserCreated(i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onUserRemoved(int i) {
        Log.i(LOG_TAG, "onUserRemoved(userId = " + i + ")");
        this.mService.onUserRemoved(i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageAdded(PackageState packageState, boolean z, AndroidPackage androidPackage) {
        Log.i(LOG_TAG, "onPackageAdded(packageState = " + packageState + ", isInstantApp = " + z + ", oldPkg = " + androidPackage + ")");
        this.mService.onPackageAdded(packageState, z, androidPackage);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageInstalled(AndroidPackage androidPackage, int i, PermissionManagerServiceInternal.PackageInstalledParams packageInstalledParams, int i2) {
        Log.i(LOG_TAG, "onPackageInstalled(pkg = " + androidPackage + ", previousAppId = " + i + ", params = " + packageInstalledParams + ", userId = " + i2 + ")");
        this.mService.onPackageInstalled(androidPackage, i, packageInstalledParams, i2);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageRemoved(AndroidPackage androidPackage) {
        Log.i(LOG_TAG, "onPackageRemoved(pkg = " + androidPackage + ")");
        this.mService.onPackageRemoved(androidPackage);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageUninstalled(String str, int i, PackageState packageState, AndroidPackage androidPackage, List<AndroidPackage> list, int i2) {
        Log.i(LOG_TAG, "onPackageUninstalled(packageName = " + str + ", appId = " + i + ", packageState = " + packageState + ", pkg = " + androidPackage + ", sharedUserPkgs = " + list + ", userId = " + i2 + ")");
        this.mService.onPackageUninstalled(str, i, packageState, androidPackage, list, i2);
    }
}
