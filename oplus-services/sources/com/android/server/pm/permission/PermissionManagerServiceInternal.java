package com.android.server.pm.permission;

import android.content.pm.PermissionInfo;
import android.permission.PermissionManagerInternal;
import android.util.ArrayMap;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface PermissionManagerServiceInternal extends PermissionManagerInternal, LegacyPermissionDataProvider {

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface HotwordDetectionServiceProvider {
        int getUid();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface OnRuntimePermissionStateChangedListener {
        void onRuntimePermissionStateChanged(String str, int i);
    }

    void addOnRuntimePermissionStateChangedListener(OnRuntimePermissionStateChangedListener onRuntimePermissionStateChangedListener);

    int checkPermission(String str, String str2, int i);

    int checkUidPermission(int i, String str);

    List<PermissionInfo> getAllPermissionsWithProtection(int i);

    List<PermissionInfo> getAllPermissionsWithProtectionFlags(int i);

    String[] getAppOpPermissionPackages(String str);

    List<String> getDelegatedShellPermissions();

    Set<String> getGrantedPermissions(String str, int i);

    HotwordDetectionServiceProvider getHotwordDetectionServiceProvider();

    Set<String> getInstalledPermissions(String str);

    int[] getPermissionGids(String str, int i);

    Permission getPermissionTEMP(String str);

    boolean isPermissionsReviewRequired(String str, int i);

    void onPackageAdded(PackageState packageState, boolean z, AndroidPackage androidPackage);

    void onPackageInstalled(AndroidPackage androidPackage, int i, PackageInstalledParams packageInstalledParams, int i2);

    void onPackageRemoved(AndroidPackage androidPackage);

    void onPackageUninstalled(String str, int i, PackageState packageState, AndroidPackage androidPackage, List<AndroidPackage> list, int i2);

    void onStorageVolumeMounted(String str, boolean z);

    void onSystemReady();

    void onUserCreated(int i);

    void onUserRemoved(int i);

    void readLegacyPermissionStateTEMP();

    void readLegacyPermissionsTEMP(LegacyPermissionSettings legacyPermissionSettings);

    void removeOnRuntimePermissionStateChangedListener(OnRuntimePermissionStateChangedListener onRuntimePermissionStateChangedListener);

    void resetRuntimePermissions(AndroidPackage androidPackage, int i);

    void resetRuntimePermissionsForUser(int i);

    void setHotwordDetectionServiceProvider(HotwordDetectionServiceProvider hotwordDetectionServiceProvider);

    void startShellPermissionIdentityDelegation(int i, String str, List<String> list);

    void stopShellPermissionIdentityDelegation();

    void writeLegacyPermissionStateTEMP();

    void writeLegacyPermissionsTEMP(LegacyPermissionSettings legacyPermissionSettings);

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class PackageInstalledParams {
        public static final PackageInstalledParams DEFAULT = new Builder().build();
        private final List<String> mAllowlistedRestrictedPermissions;
        private final int mAutoRevokePermissionsMode;
        private final ArrayMap<String, Integer> mPermissionStates;

        private PackageInstalledParams(ArrayMap<String, Integer> arrayMap, List<String> list, int i) {
            this.mPermissionStates = arrayMap;
            this.mAllowlistedRestrictedPermissions = list;
            this.mAutoRevokePermissionsMode = i;
        }

        public ArrayMap<String, Integer> getPermissionStates() {
            return this.mPermissionStates;
        }

        public List<String> getAllowlistedRestrictedPermissions() {
            return this.mAllowlistedRestrictedPermissions;
        }

        public int getAutoRevokePermissionsMode() {
            return this.mAutoRevokePermissionsMode;
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public static final class Builder {
            private ArrayMap<String, Integer> mPermissionStates = null;
            private List<String> mAllowlistedRestrictedPermissions = Collections.emptyList();
            private int mAutoRevokePermissionsMode = 3;

            public Builder setPermissionStates(ArrayMap<String, Integer> arrayMap) {
                Objects.requireNonNull(arrayMap);
                this.mPermissionStates = arrayMap;
                return this;
            }

            public void setAllowlistedRestrictedPermissions(List<String> list) {
                Objects.requireNonNull(list);
                this.mAllowlistedRestrictedPermissions = new ArrayList(list);
            }

            public void setAutoRevokePermissionsMode(int i) {
                this.mAutoRevokePermissionsMode = i;
            }

            public PackageInstalledParams build() {
                ArrayMap<String, Integer> arrayMap = this.mPermissionStates;
                if (arrayMap == null) {
                    arrayMap = new ArrayMap<>();
                }
                return new PackageInstalledParams(arrayMap, this.mAllowlistedRestrictedPermissions, this.mAutoRevokePermissionsMode);
            }
        }
    }
}
