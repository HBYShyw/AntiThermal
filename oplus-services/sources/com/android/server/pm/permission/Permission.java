package com.android.server.pm.permission;

import android.content.pm.PackageManagerInternal;
import android.content.pm.PermissionInfo;
import android.os.UserHandle;
import android.util.Log;
import android.util.Slog;
import com.android.server.pm.DumpState;
import com.android.server.pm.PackageManagerService;
import com.android.server.pm.pkg.PackageState;
import com.android.server.pm.pkg.PackageStateInternal;
import com.android.server.pm.pkg.component.ParsedPermission;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import libcore.util.EmptyArray;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class Permission {
    private static final String TAG = "Permission";
    public static final int TYPE_CONFIG = 1;
    public static final int TYPE_DYNAMIC = 2;
    public static final int TYPE_MANIFEST = 0;
    private boolean mDefinitionChanged;
    private int[] mGids;
    private boolean mGidsPerUser;
    private PermissionInfo mPermissionInfo;
    private boolean mReconciled;
    private final int mType;
    private int mUid;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface PermissionType {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface ProtectionLevel {
    }

    public Permission(String str, String str2, int i) {
        this.mGids = EmptyArray.INT;
        PermissionInfo permissionInfo = new PermissionInfo();
        this.mPermissionInfo = permissionInfo;
        permissionInfo.name = str;
        permissionInfo.packageName = str2;
        permissionInfo.protectionLevel = 2;
        this.mType = i;
    }

    public Permission(PermissionInfo permissionInfo, int i) {
        this.mGids = EmptyArray.INT;
        this.mPermissionInfo = permissionInfo;
        this.mType = i;
    }

    public Permission(PermissionInfo permissionInfo, int i, boolean z, int i2, int[] iArr, boolean z2) {
        this(permissionInfo, i);
        this.mReconciled = z;
        this.mUid = i2;
        this.mGids = iArr;
        this.mGidsPerUser = z2;
    }

    public PermissionInfo getPermissionInfo() {
        return this.mPermissionInfo;
    }

    public void setPermissionInfo(PermissionInfo permissionInfo) {
        if (permissionInfo != null) {
            this.mPermissionInfo = permissionInfo;
        } else {
            PermissionInfo permissionInfo2 = new PermissionInfo();
            PermissionInfo permissionInfo3 = this.mPermissionInfo;
            permissionInfo2.name = permissionInfo3.name;
            permissionInfo2.packageName = permissionInfo3.packageName;
            permissionInfo2.protectionLevel = permissionInfo3.protectionLevel;
            this.mPermissionInfo = permissionInfo2;
        }
        this.mReconciled = permissionInfo != null;
    }

    public String getName() {
        return this.mPermissionInfo.name;
    }

    public int getProtectionLevel() {
        return this.mPermissionInfo.protectionLevel;
    }

    public String getPackageName() {
        return this.mPermissionInfo.packageName;
    }

    public int getType() {
        return this.mType;
    }

    public int getUid() {
        return this.mUid;
    }

    public boolean hasGids() {
        return this.mGids.length != 0;
    }

    public int[] getRawGids() {
        return this.mGids;
    }

    public boolean areGidsPerUser() {
        return this.mGidsPerUser;
    }

    public void setGids(int[] iArr, boolean z) {
        this.mGids = iArr;
        this.mGidsPerUser = z;
    }

    public int[] computeGids(int i) {
        if (this.mGidsPerUser) {
            int[] iArr = new int[this.mGids.length];
            int i2 = 0;
            while (true) {
                int[] iArr2 = this.mGids;
                if (i2 >= iArr2.length) {
                    return iArr;
                }
                iArr[i2] = UserHandle.getUid(i, iArr2[i2]);
                i2++;
            }
        } else {
            int[] iArr3 = this.mGids;
            return iArr3.length != 0 ? (int[]) iArr3.clone() : iArr3;
        }
    }

    public boolean isDefinitionChanged() {
        return this.mDefinitionChanged;
    }

    public void setDefinitionChanged(boolean z) {
        this.mDefinitionChanged = z;
    }

    public int calculateFootprint(Permission permission) {
        if (this.mUid == permission.mUid) {
            return permission.mPermissionInfo.name.length() + permission.mPermissionInfo.calculateFootprint();
        }
        return 0;
    }

    public boolean isPermission(ParsedPermission parsedPermission) {
        PermissionInfo permissionInfo = this.mPermissionInfo;
        return permissionInfo != null && Objects.equals(permissionInfo.packageName, parsedPermission.getPackageName()) && Objects.equals(this.mPermissionInfo.name, parsedPermission.getName());
    }

    public boolean isDynamic() {
        return this.mType == 2;
    }

    public boolean isNormal() {
        return (this.mPermissionInfo.protectionLevel & 15) == 0;
    }

    public boolean isRuntime() {
        return (this.mPermissionInfo.protectionLevel & 15) == 1;
    }

    public boolean isRemoved() {
        return (this.mPermissionInfo.flags & 2) != 0;
    }

    public boolean isSoftRestricted() {
        return (this.mPermissionInfo.flags & 8) != 0;
    }

    public boolean isHardRestricted() {
        return (this.mPermissionInfo.flags & 4) != 0;
    }

    public boolean isHardOrSoftRestricted() {
        return (this.mPermissionInfo.flags & 12) != 0;
    }

    public boolean isImmutablyRestricted() {
        return (this.mPermissionInfo.flags & 16) != 0;
    }

    public boolean isSignature() {
        return (this.mPermissionInfo.protectionLevel & 15) == 2;
    }

    public boolean isInternal() {
        return (this.mPermissionInfo.protectionLevel & 15) == 4;
    }

    public boolean isAppOp() {
        return (this.mPermissionInfo.protectionLevel & 64) != 0;
    }

    public boolean isDevelopment() {
        return isSignature() && (this.mPermissionInfo.protectionLevel & 32) != 0;
    }

    public boolean isInstaller() {
        return (this.mPermissionInfo.protectionLevel & 256) != 0;
    }

    public boolean isInstant() {
        return (this.mPermissionInfo.protectionLevel & 4096) != 0;
    }

    public boolean isOem() {
        return (this.mPermissionInfo.protectionLevel & 16384) != 0;
    }

    public boolean isPre23() {
        return (this.mPermissionInfo.protectionLevel & 128) != 0;
    }

    public boolean isPreInstalled() {
        return (this.mPermissionInfo.protectionLevel & 1024) != 0;
    }

    public boolean isPrivileged() {
        return (this.mPermissionInfo.protectionLevel & 16) != 0;
    }

    public boolean isRuntimeOnly() {
        return (this.mPermissionInfo.protectionLevel & 8192) != 0;
    }

    public boolean isSetup() {
        return (this.mPermissionInfo.protectionLevel & 2048) != 0;
    }

    public boolean isVerifier() {
        return (this.mPermissionInfo.protectionLevel & 512) != 0;
    }

    public boolean isVendorPrivileged() {
        return (this.mPermissionInfo.protectionLevel & 32768) != 0;
    }

    public boolean isSystemTextClassifier() {
        return (this.mPermissionInfo.protectionLevel & 65536) != 0;
    }

    public boolean isConfigurator() {
        return (this.mPermissionInfo.protectionLevel & 524288) != 0;
    }

    public boolean isIncidentReportApprover() {
        return (this.mPermissionInfo.protectionLevel & 1048576) != 0;
    }

    public boolean isAppPredictor() {
        return (this.mPermissionInfo.protectionLevel & DumpState.DUMP_COMPILER_STATS) != 0;
    }

    public boolean isCompanion() {
        return (this.mPermissionInfo.protectionLevel & 8388608) != 0;
    }

    public boolean isModule() {
        return (this.mPermissionInfo.protectionLevel & DumpState.DUMP_CHANGES) != 0;
    }

    public boolean isRetailDemo() {
        return (this.mPermissionInfo.protectionLevel & DumpState.DUMP_SERVICE_PERMISSIONS) != 0;
    }

    public boolean isRecents() {
        return (this.mPermissionInfo.protectionLevel & DumpState.DUMP_APEX) != 0;
    }

    public boolean isRole() {
        return (this.mPermissionInfo.protectionLevel & 67108864) != 0;
    }

    public boolean isKnownSigner() {
        return (this.mPermissionInfo.protectionLevel & DumpState.DUMP_KNOWN_PACKAGES) != 0;
    }

    public Set<String> getKnownCerts() {
        return this.mPermissionInfo.knownCerts;
    }

    public void transfer(String str, String str2) {
        if (str.equals(this.mPermissionInfo.packageName)) {
            PermissionInfo permissionInfo = new PermissionInfo();
            PermissionInfo permissionInfo2 = this.mPermissionInfo;
            permissionInfo.name = permissionInfo2.name;
            permissionInfo.packageName = str2;
            permissionInfo.protectionLevel = permissionInfo2.protectionLevel;
            this.mPermissionInfo = permissionInfo;
            this.mReconciled = false;
            this.mUid = 0;
            this.mGids = EmptyArray.INT;
            this.mGidsPerUser = false;
        }
    }

    public boolean addToTree(int i, PermissionInfo permissionInfo, Permission permission) {
        PermissionInfo permissionInfo2 = this.mPermissionInfo;
        boolean z = (permissionInfo2.protectionLevel == i && this.mReconciled && this.mUid == permission.mUid && Objects.equals(permissionInfo2.packageName, permission.mPermissionInfo.packageName) && comparePermissionInfos(this.mPermissionInfo, permissionInfo)) ? false : true;
        PermissionInfo permissionInfo3 = new PermissionInfo(permissionInfo);
        this.mPermissionInfo = permissionInfo3;
        permissionInfo3.packageName = permission.mPermissionInfo.packageName;
        permissionInfo3.protectionLevel = i;
        this.mReconciled = true;
        this.mUid = permission.mUid;
        return z;
    }

    public void updateDynamicPermission(Collection<Permission> collection) {
        Permission findPermissionTree;
        if (PackageManagerService.DEBUG_SETTINGS) {
            Log.v(TAG, "Dynamic permission: name=" + getName() + " pkg=" + getPackageName() + " info=" + this.mPermissionInfo);
        }
        if (this.mType != 2 || (findPermissionTree = findPermissionTree(collection, this.mPermissionInfo.name)) == null) {
            return;
        }
        this.mPermissionInfo.packageName = findPermissionTree.mPermissionInfo.packageName;
        this.mReconciled = true;
        this.mUid = findPermissionTree.mUid;
    }

    public static boolean isOverridingSystemPermission(Permission permission, PermissionInfo permissionInfo, PackageManagerInternal packageManagerInternal) {
        PackageStateInternal packageStateInternal;
        if (permission == null || Objects.equals(permission.mPermissionInfo.packageName, permissionInfo.packageName) || !permission.mReconciled || (packageStateInternal = packageManagerInternal.getPackageStateInternal(permission.mPermissionInfo.packageName)) == null) {
            return false;
        }
        return packageStateInternal.isSystem();
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x007c  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x008b  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0159 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x012c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Permission createOrUpdate(Permission permission, PermissionInfo permissionInfo, PackageState packageState, Collection<Permission> collection, boolean z) {
        boolean z2;
        StringBuilder sb = null;
        if (permission != null && !Objects.equals(permission.mPermissionInfo.packageName, permissionInfo.packageName) && packageState.isSystem()) {
            if (permission.mType == 1 && !permission.mReconciled) {
                permission.mPermissionInfo = permissionInfo;
                permission.mReconciled = true;
                permission.mUid = packageState.getAppId();
            } else if (!z) {
                Slog.w(TAG, "New decl " + packageState + " of permission  " + permissionInfo.name + " is system; overriding " + permission.mPermissionInfo.packageName);
                permission = null;
                z2 = true;
                boolean z3 = (permission != null || permission.mType == 1 || permission.isInternal()) ? false : true;
                boolean z4 = (permission != null || permission.mType == 1 || permission.isRuntime()) ? false : true;
                if (permission == null) {
                    permission = new Permission(permissionInfo.name, permissionInfo.packageName, 0);
                }
                if (permission.mReconciled) {
                    String str = permission.mPermissionInfo.packageName;
                    if (str == null || str.equals(permissionInfo.packageName)) {
                        Permission findPermissionTree = findPermissionTree(collection, permissionInfo.name);
                        if (findPermissionTree == null || findPermissionTree.mPermissionInfo.packageName.equals(permissionInfo.packageName)) {
                            permission.mPermissionInfo = permissionInfo;
                            permission.mReconciled = true;
                            permission.mUid = packageState.getAppId();
                            if (PackageManagerService.DEBUG_PACKAGE_SCANNING) {
                                StringBuilder sb2 = new StringBuilder(256);
                                sb2.append(permissionInfo.name);
                                sb = sb2;
                            }
                        } else {
                            Slog.w(TAG, "Permission " + permissionInfo.name + " from package " + permissionInfo.packageName + " ignored: base tree " + findPermissionTree.mPermissionInfo.name + " is from package " + findPermissionTree.mPermissionInfo.packageName);
                        }
                    } else {
                        Slog.w(TAG, "Permission " + permissionInfo.name + " from package " + permissionInfo.packageName + " ignored: original from " + permission.mPermissionInfo.packageName);
                    }
                } else if (PackageManagerService.DEBUG_PACKAGE_SCANNING) {
                    sb = new StringBuilder(256);
                    sb.append("DUP:");
                    sb.append(permissionInfo.name);
                }
                if ((permission.isInternal() && (z2 || z3)) || (permission.isRuntime() && (z2 || z4))) {
                    permission.mDefinitionChanged = true;
                }
                if (PackageManagerService.DEBUG_PACKAGE_SCANNING && sb != null) {
                    Log.d(TAG, "  Permissions: " + ((Object) sb));
                }
                return permission;
            }
        }
        z2 = false;
        if (permission != null) {
        }
        if (permission != null) {
        }
        if (permission == null) {
        }
        if (permission.mReconciled) {
        }
        if (permission.isInternal()) {
            permission.mDefinitionChanged = true;
            if (PackageManagerService.DEBUG_PACKAGE_SCANNING) {
                Log.d(TAG, "  Permissions: " + ((Object) sb));
            }
            return permission;
        }
        permission.mDefinitionChanged = true;
        if (PackageManagerService.DEBUG_PACKAGE_SCANNING) {
        }
        return permission;
    }

    public static Permission enforcePermissionTree(Collection<Permission> collection, String str, int i) {
        Permission findPermissionTree;
        if (str != null && (findPermissionTree = findPermissionTree(collection, str)) != null && findPermissionTree.getUid() == UserHandle.getAppId(i)) {
            return findPermissionTree;
        }
        throw new SecurityException("Calling uid " + i + " is not allowed to add to or remove from the permission tree");
    }

    private static Permission findPermissionTree(Collection<Permission> collection, String str) {
        for (Permission permission : collection) {
            String name = permission.getName();
            if (str.startsWith(name) && str.length() > name.length() && str.charAt(name.length()) == '.') {
                return permission;
            }
        }
        return null;
    }

    public String getBackgroundPermission() {
        return this.mPermissionInfo.backgroundPermission;
    }

    public String getGroup() {
        return this.mPermissionInfo.group;
    }

    public int getProtection() {
        return this.mPermissionInfo.protectionLevel & 15;
    }

    public int getProtectionFlags() {
        return this.mPermissionInfo.protectionLevel & 65520;
    }

    public PermissionInfo generatePermissionInfo(int i) {
        return generatePermissionInfo(i, 10000);
    }

    public PermissionInfo generatePermissionInfo(int i, int i2) {
        PermissionInfo permissionInfo;
        if (this.mPermissionInfo != null) {
            permissionInfo = new PermissionInfo(this.mPermissionInfo);
            if ((i & 128) != 128) {
                permissionInfo.metaData = null;
            }
        } else {
            permissionInfo = new PermissionInfo();
            PermissionInfo permissionInfo2 = this.mPermissionInfo;
            permissionInfo.name = permissionInfo2.name;
            permissionInfo.packageName = permissionInfo2.packageName;
            permissionInfo.nonLocalizedLabel = permissionInfo2.name;
        }
        permissionInfo.flags |= 1073741824;
        if (i2 >= 26) {
            permissionInfo.protectionLevel = this.mPermissionInfo.protectionLevel;
        } else {
            int i3 = this.mPermissionInfo.protectionLevel;
            int i4 = i3 & 15;
            if (i4 == 2) {
                permissionInfo.protectionLevel = i3;
            } else {
                permissionInfo.protectionLevel = i4;
            }
        }
        return permissionInfo;
    }

    private static boolean comparePermissionInfos(PermissionInfo permissionInfo, PermissionInfo permissionInfo2) {
        return permissionInfo.icon == permissionInfo2.icon && permissionInfo.logo == permissionInfo2.logo && permissionInfo.protectionLevel == permissionInfo2.protectionLevel && Objects.equals(permissionInfo.name, permissionInfo2.name) && Objects.equals(permissionInfo.nonLocalizedLabel, permissionInfo2.nonLocalizedLabel) && Objects.equals(permissionInfo.packageName, permissionInfo2.packageName);
    }
}
