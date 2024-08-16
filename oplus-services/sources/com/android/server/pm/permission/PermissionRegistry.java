package com.android.server.pm.permission;

import android.util.ArrayMap;
import android.util.ArraySet;
import com.android.server.pm.pkg.component.ParsedPermissionGroup;
import java.util.Collection;
import java.util.Iterator;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PermissionRegistry {
    private final ArrayMap<String, Permission> mPermissions = new ArrayMap<>();
    private final ArrayMap<String, Permission> mPermissionTrees = new ArrayMap<>();
    private final ArrayMap<String, ParsedPermissionGroup> mPermissionGroups = new ArrayMap<>();
    private final ArrayMap<String, ArraySet<String>> mAppOpPermissionPackages = new ArrayMap<>();

    public Collection<Permission> getPermissions() {
        return this.mPermissions.values();
    }

    public Permission getPermission(String str) {
        return this.mPermissions.get(str);
    }

    public void addPermission(Permission permission) {
        this.mPermissions.put(permission.getName(), permission);
    }

    public void removePermission(String str) {
        this.mPermissions.remove(str);
    }

    public Collection<Permission> getPermissionTrees() {
        return this.mPermissionTrees.values();
    }

    public Permission getPermissionTree(String str) {
        return this.mPermissionTrees.get(str);
    }

    public void addPermissionTree(Permission permission) {
        this.mPermissionTrees.put(permission.getName(), permission);
    }

    public void transferPermissions(String str, String str2) {
        int i = 0;
        while (i < 2) {
            Iterator<Permission> it = (i == 0 ? this.mPermissionTrees : this.mPermissions).values().iterator();
            while (it.hasNext()) {
                it.next().transfer(str, str2);
            }
            i++;
        }
    }

    public Collection<ParsedPermissionGroup> getPermissionGroups() {
        return this.mPermissionGroups.values();
    }

    public ParsedPermissionGroup getPermissionGroup(String str) {
        return this.mPermissionGroups.get(str);
    }

    public void addPermissionGroup(ParsedPermissionGroup parsedPermissionGroup) {
        this.mPermissionGroups.put(parsedPermissionGroup.getName(), parsedPermissionGroup);
    }

    public void removePermissionGroup(String str) {
        this.mPermissionGroups.remove(str);
    }

    public ArrayMap<String, ArraySet<String>> getAllAppOpPermissionPackages() {
        return this.mAppOpPermissionPackages;
    }

    public ArraySet<String> getAppOpPermissionPackages(String str) {
        return this.mAppOpPermissionPackages.get(str);
    }

    public void addAppOpPermissionPackage(String str, String str2) {
        ArraySet<String> arraySet = this.mAppOpPermissionPackages.get(str);
        if (arraySet == null) {
            arraySet = new ArraySet<>();
            this.mAppOpPermissionPackages.put(str, arraySet);
        }
        arraySet.add(str2);
    }

    public void removeAppOpPermissionPackage(String str, String str2) {
        ArraySet<String> arraySet = this.mAppOpPermissionPackages.get(str);
        if (arraySet != null && arraySet.remove(str2) && arraySet.isEmpty()) {
            this.mAppOpPermissionPackages.remove(str);
        }
    }

    public Permission enforcePermissionTree(String str, int i) {
        return Permission.enforcePermissionTree(this.mPermissionTrees.values(), str, i);
    }
}
