package com.android.server.permission.access;

import android.content.pm.PermissionGroupInfo;
import android.util.ArrayMap;
import android.util.SparseArray;
import com.android.server.SystemConfig;
import com.android.server.permission.access.collection.IndexedListSet;
import com.android.server.permission.access.collection.IntSet;
import com.android.server.permission.access.permission.Permission;
import com.android.server.permission.jarjar.kotlin.collections.MapsKt;
import com.android.server.pm.permission.PermissionAllowlist;
import com.android.server.pm.pkg.PackageState;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/* compiled from: AccessState.kt */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class SystemState extends WritableState {

    @NotNull
    private final SparseArray<IndexedListSet<String>> appIds;

    @NotNull
    private Map<String, SystemConfig.PermissionEntry> configPermissions;

    @NotNull
    private SparseArray<String> deviceAndProfileOwners;

    @NotNull
    private Map<String, ? extends PackageState> disabledSystemPackageStates;

    @NotNull
    private ArrayMap<String, IndexedListSet<String>> implicitToSourcePermissions;
    private boolean isLeanback;
    private boolean isSystemReady;

    @NotNull
    private SparseArray<String[]> knownPackages;

    @NotNull
    private Map<String, ? extends PackageState> packageStates;

    @NotNull
    private PermissionAllowlist permissionAllowlist;

    @NotNull
    private final ArrayMap<String, PermissionGroupInfo> permissionGroups;

    @NotNull
    private final ArrayMap<String, Permission> permissionTrees;

    @NotNull
    private final ArrayMap<String, Permission> permissions;

    @NotNull
    private IndexedListSet<String> privilegedPermissionAllowlistPackages;

    @NotNull
    private final IntSet userIds;

    @NotNull
    public final IntSet getUserIds() {
        return this.userIds;
    }

    @NotNull
    public final Map<String, PackageState> getPackageStates() {
        return this.packageStates;
    }

    public final void setPackageStates(@NotNull Map<String, ? extends PackageState> map) {
        this.packageStates = map;
    }

    @NotNull
    public final Map<String, PackageState> getDisabledSystemPackageStates() {
        return this.disabledSystemPackageStates;
    }

    public final void setDisabledSystemPackageStates(@NotNull Map<String, ? extends PackageState> map) {
        this.disabledSystemPackageStates = map;
    }

    @NotNull
    public final SparseArray<IndexedListSet<String>> getAppIds() {
        return this.appIds;
    }

    @NotNull
    public final SparseArray<String[]> getKnownPackages() {
        return this.knownPackages;
    }

    public final void setKnownPackages(@NotNull SparseArray<String[]> sparseArray) {
        this.knownPackages = sparseArray;
    }

    public final boolean isLeanback() {
        return this.isLeanback;
    }

    public final void setLeanback(boolean z) {
        this.isLeanback = z;
    }

    @NotNull
    public final Map<String, SystemConfig.PermissionEntry> getConfigPermissions() {
        return this.configPermissions;
    }

    public final void setConfigPermissions(@NotNull Map<String, SystemConfig.PermissionEntry> map) {
        this.configPermissions = map;
    }

    @NotNull
    public final IndexedListSet<String> getPrivilegedPermissionAllowlistPackages() {
        return this.privilegedPermissionAllowlistPackages;
    }

    public final void setPrivilegedPermissionAllowlistPackages(@NotNull IndexedListSet<String> indexedListSet) {
        this.privilegedPermissionAllowlistPackages = indexedListSet;
    }

    @NotNull
    public final PermissionAllowlist getPermissionAllowlist() {
        return this.permissionAllowlist;
    }

    public final void setPermissionAllowlist(@NotNull PermissionAllowlist permissionAllowlist) {
        this.permissionAllowlist = permissionAllowlist;
    }

    @NotNull
    public final ArrayMap<String, IndexedListSet<String>> getImplicitToSourcePermissions() {
        return this.implicitToSourcePermissions;
    }

    public final void setImplicitToSourcePermissions(@NotNull ArrayMap<String, IndexedListSet<String>> arrayMap) {
        this.implicitToSourcePermissions = arrayMap;
    }

    public final boolean isSystemReady() {
        return this.isSystemReady;
    }

    public final void setSystemReady(boolean z) {
        this.isSystemReady = z;
    }

    @NotNull
    public final SparseArray<String> getDeviceAndProfileOwners() {
        return this.deviceAndProfileOwners;
    }

    @NotNull
    public final ArrayMap<String, PermissionGroupInfo> getPermissionGroups() {
        return this.permissionGroups;
    }

    @NotNull
    public final ArrayMap<String, Permission> getPermissionTrees() {
        return this.permissionTrees;
    }

    @NotNull
    public final ArrayMap<String, Permission> getPermissions() {
        return this.permissions;
    }

    private SystemState(IntSet intSet, Map<String, ? extends PackageState> map, Map<String, ? extends PackageState> map2, SparseArray<IndexedListSet<String>> sparseArray, SparseArray<String[]> sparseArray2, boolean z, Map<String, SystemConfig.PermissionEntry> map3, IndexedListSet<String> indexedListSet, PermissionAllowlist permissionAllowlist, ArrayMap<String, IndexedListSet<String>> arrayMap, boolean z2, SparseArray<String> sparseArray3, ArrayMap<String, PermissionGroupInfo> arrayMap2, ArrayMap<String, Permission> arrayMap3, ArrayMap<String, Permission> arrayMap4) {
        this.userIds = intSet;
        this.packageStates = map;
        this.disabledSystemPackageStates = map2;
        this.appIds = sparseArray;
        this.knownPackages = sparseArray2;
        this.isLeanback = z;
        this.configPermissions = map3;
        this.privilegedPermissionAllowlistPackages = indexedListSet;
        this.permissionAllowlist = permissionAllowlist;
        this.implicitToSourcePermissions = arrayMap;
        this.isSystemReady = z2;
        this.deviceAndProfileOwners = sparseArray3;
        this.permissionGroups = arrayMap2;
        this.permissionTrees = arrayMap3;
        this.permissions = arrayMap4;
    }

    public SystemState() {
        this(new IntSet(), MapsKt.emptyMap(), MapsKt.emptyMap(), new SparseArray(), new SparseArray(), false, MapsKt.emptyMap(), new IndexedListSet(), new PermissionAllowlist(), new ArrayMap(), false, new SparseArray(), new ArrayMap(), new ArrayMap(), new ArrayMap());
    }

    @NotNull
    public final SystemState copy() {
        IntSet copy = this.userIds.copy();
        Map<String, ? extends PackageState> map = this.packageStates;
        Map<String, ? extends PackageState> map2 = this.disabledSystemPackageStates;
        SparseArray<IndexedListSet<String>> clone = this.appIds.clone();
        int size = clone.size();
        for (int i = 0; i < size; i++) {
            clone.setValueAt(i, clone.valueAt(i).copy());
        }
        SparseArray<String[]> sparseArray = this.knownPackages;
        boolean z = this.isLeanback;
        Map<String, SystemConfig.PermissionEntry> map3 = this.configPermissions;
        IndexedListSet<String> indexedListSet = this.privilegedPermissionAllowlistPackages;
        PermissionAllowlist permissionAllowlist = this.permissionAllowlist;
        ArrayMap<String, IndexedListSet<String>> arrayMap = this.implicitToSourcePermissions;
        boolean z2 = this.isSystemReady;
        SparseArray<String> sparseArray2 = this.deviceAndProfileOwners;
        ArrayMap arrayMap2 = new ArrayMap(this.permissionGroups);
        int i2 = 0;
        for (int size2 = arrayMap2.size(); i2 < size2; size2 = size2) {
            arrayMap2.setValueAt(i2, (PermissionGroupInfo) arrayMap2.valueAt(i2));
            i2++;
        }
        ArrayMap arrayMap3 = new ArrayMap(this.permissionTrees);
        int i3 = 0;
        for (int size3 = arrayMap3.size(); i3 < size3; size3 = size3) {
            arrayMap3.setValueAt(i3, (Permission) arrayMap3.valueAt(i3));
            i3++;
        }
        ArrayMap arrayMap4 = new ArrayMap(this.permissions);
        int i4 = 0;
        for (int size4 = arrayMap4.size(); i4 < size4; size4 = size4) {
            arrayMap4.setValueAt(i4, (Permission) arrayMap4.valueAt(i4));
            i4++;
        }
        return new SystemState(copy, map, map2, clone, sparseArray, z, map3, indexedListSet, permissionAllowlist, arrayMap, z2, sparseArray2, arrayMap2, arrayMap3, arrayMap4);
    }
}
