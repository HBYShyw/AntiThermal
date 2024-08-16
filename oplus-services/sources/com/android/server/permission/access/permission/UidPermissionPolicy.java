package com.android.server.permission.access.permission;

import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.SigningDetails;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.os.RoSystemProperties;
import com.android.modules.utils.BinaryXmlPullParser;
import com.android.modules.utils.BinaryXmlSerializer;
import com.android.server.SystemConfig;
import com.android.server.hdmi.HdmiCecKeycode;
import com.android.server.permission.access.AccessState;
import com.android.server.permission.access.AccessUri;
import com.android.server.permission.access.GetStateScope;
import com.android.server.permission.access.MutateStateScope;
import com.android.server.permission.access.PermissionUri;
import com.android.server.permission.access.SchemePolicy;
import com.android.server.permission.access.SystemState;
import com.android.server.permission.access.UidUri;
import com.android.server.permission.access.UserState;
import com.android.server.permission.access.WritableState;
import com.android.server.permission.access.collection.IndexedListSet;
import com.android.server.permission.access.collection.IntSet;
import com.android.server.permission.access.util.IntExtensions;
import com.android.server.permission.jarjar.kotlin.Unit;
import com.android.server.permission.jarjar.kotlin.collections.MutableCollections;
import com.android.server.permission.jarjar.kotlin.collections._Arrays;
import com.android.server.permission.jarjar.kotlin.collections._ArraysJvm;
import com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker;
import com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics;
import com.android.server.permission.jarjar.kotlin.ranges._Ranges;
import com.android.server.permission.jarjar.kotlin.text.StringsJVM;
import com.android.server.pm.DumpState;
import com.android.server.pm.PackageManagerService;
import com.android.server.pm.parsing.PackageInfoUtils;
import com.android.server.pm.permission.CompatibilityPermissionInfo;
import com.android.server.pm.permission.PermissionAllowlist;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageState;
import com.android.server.pm.pkg.PackageUserState;
import com.android.server.pm.pkg.component.ParsedPermission;
import com.android.server.pm.pkg.component.ParsedPermissionGroup;
import com.android.server.pm.pkg.parsing.ParsingPackageUtils;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: UidPermissionPolicy.kt */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class UidPermissionPolicy extends SchemePolicy {

    @NotNull
    public static final Companion Companion = new Companion(null);
    private static final String LOG_TAG = UidPermissionPolicy.class.getSimpleName();

    @NotNull
    private static final ArraySet<String> NEARBY_DEVICES_PERMISSIONS;

    @NotNull
    private static final ArraySet<String> NOTIFICATIONS_PERMISSIONS;

    @NotNull
    private static final ArraySet<String> RETAIN_IMPLICIT_FLAGS_PERMISSIONS;

    @NotNull
    private static final ArraySet<String> STORAGE_AND_MEDIA_PERMISSIONS;

    @NotNull
    private final UidPermissionPersistence persistence = new UidPermissionPersistence();

    @NotNull
    private volatile IndexedListSet<OnPermissionFlagsChangedListener> onPermissionFlagsChangedListeners = new IndexedListSet<>();

    @NotNull
    private final Object onPermissionFlagsChangedListenersLock = new Object();

    @NotNull
    private final ArraySet<String> privilegedPermissionAllowlistViolations = new ArraySet<>();

    /* compiled from: UidPermissionPolicy.kt */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static abstract class OnPermissionFlagsChangedListener {
        public abstract void onPermissionFlagsChanged(int i, int i2, @NotNull String str, int i3, int i4);

        public abstract void onStateMutated();
    }

    @Override // com.android.server.permission.access.SchemePolicy
    @NotNull
    public String getObjectScheme() {
        return ParsingPackageUtils.TAG_PERMISSION;
    }

    @Override // com.android.server.permission.access.SchemePolicy
    @NotNull
    public String getSubjectScheme() {
        return "uid";
    }

    private final boolean shouldGrantPrivilegedOrOemPermission(MutateStateScope mutateStateScope, PackageState packageState, Permission permission) {
        String str = permission.getPermissionInfo().name;
        String packageName = packageState.getPackageName();
        if (!IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), 16)) {
            if (IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), 16384) && packageState.isOem()) {
                Boolean oemAppAllowlistState = mutateStateScope.getNewState().getSystemState().getPermissionAllowlist().getOemAppAllowlistState(packageName, str);
                if (oemAppAllowlistState == null) {
                    throw new IllegalStateException(("OEM permission " + str + " requested by package " + packageName + " must be explicitly declared granted or not").toString());
                }
                return oemAppAllowlistState.booleanValue();
            }
        } else if (packageState.isPrivileged()) {
            if (!packageState.isVendor() || IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), 32768)) {
                return true;
            }
            Log.w(LOG_TAG, "Permission " + str + " cannot be granted to privileged vendor app " + packageName + " because it isn't a vendorPrivileged permission");
            return false;
        }
        return false;
    }

    private final Permission updatePermissionIfDynamic(MutateStateScope mutateStateScope, Permission permission) {
        Permission findPermissionTree;
        if (!(permission.getType() == 2) || (findPermissionTree = findPermissionTree(mutateStateScope, permission.getPermissionInfo().name)) == null) {
            return permission;
        }
        PermissionInfo permissionInfo = new PermissionInfo(permission.getPermissionInfo());
        permissionInfo.packageName = findPermissionTree.getPermissionInfo().packageName;
        return Permission.copy$default(permission, permissionInfo, true, 0, findPermissionTree.getAppId(), null, false, 52, null);
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public int getDecision(@NotNull GetStateScope getStateScope, @NotNull AccessUri accessUri, @NotNull AccessUri accessUri2) {
        Intrinsics.checkNotNull(accessUri, "null cannot be cast to non-null type com.android.server.permission.access.UidUri");
        UidUri uidUri = (UidUri) accessUri;
        Intrinsics.checkNotNull(accessUri2, "null cannot be cast to non-null type com.android.server.permission.access.PermissionUri");
        return getPermissionFlags(getStateScope, uidUri.getAppId(), uidUri.getUserId(), ((PermissionUri) accessUri2).getPermissionName());
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void setDecision(@NotNull MutateStateScope mutateStateScope, @NotNull AccessUri accessUri, @NotNull AccessUri accessUri2, int i) {
        Intrinsics.checkNotNull(accessUri, "null cannot be cast to non-null type com.android.server.permission.access.UidUri");
        UidUri uidUri = (UidUri) accessUri;
        Intrinsics.checkNotNull(accessUri2, "null cannot be cast to non-null type com.android.server.permission.access.PermissionUri");
        setPermissionFlags(mutateStateScope, uidUri.getAppId(), uidUri.getUserId(), ((PermissionUri) accessUri2).getPermissionName(), i);
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void onStateMutated(@NotNull GetStateScope getStateScope) {
        IndexedListSet<OnPermissionFlagsChangedListener> indexedListSet = this.onPermissionFlagsChangedListeners;
        int size = indexedListSet.size();
        for (int i = 0; i < size; i++) {
            indexedListSet.elementAt(i).onStateMutated();
        }
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void onInitialized(@NotNull MutateStateScope mutateStateScope) {
        Permission permission;
        for (Map.Entry<String, SystemConfig.PermissionEntry> entry : mutateStateScope.getNewState().getSystemState().getConfigPermissions().entrySet()) {
            String key = entry.getKey();
            SystemConfig.PermissionEntry value = entry.getValue();
            ArrayMap<String, Permission> permissions = mutateStateScope.getNewState().getSystemState().getPermissions();
            Permission permission2 = permissions.get(key);
            if (permission2 != null) {
                int[] iArr = value.gids;
                if (iArr != null) {
                    permission = Permission.copy$default(permission2, null, false, 0, 0, iArr, value.perUser, 15, null);
                }
            } else {
                PermissionInfo permissionInfo = new PermissionInfo();
                permissionInfo.name = key;
                permissionInfo.packageName = PackageManagerService.PLATFORM_PACKAGE_NAME;
                permissionInfo.protectionLevel = 2;
                if (value.gids != null) {
                    permission = new Permission(permissionInfo, false, 1, 0, value.gids, value.perUser);
                } else {
                    permission = new Permission(permissionInfo, false, 1, 0, null, false, 48, null);
                }
            }
            permissions.put(key, permission);
        }
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void onUserAdded(@NotNull MutateStateScope mutateStateScope, int i) {
        Iterator<Map.Entry<String, PackageState>> it = mutateStateScope.getNewState().getSystemState().getPackageStates().entrySet().iterator();
        while (it.hasNext()) {
            evaluateAllPermissionStatesForPackageAndUser(mutateStateScope, it.next().getValue(), i, null);
        }
        SparseArray<IndexedListSet<String>> appIds = mutateStateScope.getNewState().getSystemState().getAppIds();
        int size = appIds.size();
        for (int i2 = 0; i2 < size; i2++) {
            inheritImplicitPermissionStates(mutateStateScope, appIds.keyAt(i2), i);
        }
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void onAppIdRemoved(@NotNull MutateStateScope mutateStateScope, int i) {
        SparseArray<UserState> userStates = mutateStateScope.getNewState().getUserStates();
        int size = userStates.size();
        for (int i2 = 0; i2 < size; i2++) {
            UserState valueAt = userStates.valueAt(i2);
            valueAt.getUidPermissionFlags().remove(i);
            WritableState.requestWrite$default(valueAt, false, 1, null);
        }
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void onStorageVolumeMounted(@NotNull MutateStateScope mutateStateScope, @Nullable String str, boolean z) {
        ArraySet<String> arraySet = new ArraySet<>();
        Iterator<Map.Entry<String, PackageState>> it = mutateStateScope.getNewState().getSystemState().getPackageStates().entrySet().iterator();
        while (it.hasNext()) {
            PackageState value = it.next().getValue();
            AndroidPackage androidPackage = value.getAndroidPackage();
            if (androidPackage != null && Intrinsics.areEqual(androidPackage.getVolumeUuid(), str)) {
                adoptPermissions(mutateStateScope, value, arraySet);
                addPermissionGroups(mutateStateScope, value);
                addPermissions(mutateStateScope, value, arraySet);
                trimPermissions(mutateStateScope, value.getPackageName(), arraySet);
                trimPermissionStates(mutateStateScope, value.getAppId());
                revokePermissionsOnPackageUpdate(mutateStateScope, value.getAppId());
            }
        }
        int size = arraySet.size();
        for (int i = 0; i < size; i++) {
            evaluatePermissionStateForAllPackages(mutateStateScope, arraySet.valueAt(i), null);
        }
        Iterator<Map.Entry<String, PackageState>> it2 = mutateStateScope.getNewState().getSystemState().getPackageStates().entrySet().iterator();
        while (it2.hasNext()) {
            PackageState value2 = it2.next().getValue();
            AndroidPackage androidPackage2 = value2.getAndroidPackage();
            if (androidPackage2 != null && Intrinsics.areEqual(androidPackage2.getVolumeUuid(), str)) {
                evaluateAllPermissionStatesForPackage(mutateStateScope, value2, z ? value2 : null);
            }
        }
        Iterator<Map.Entry<String, PackageState>> it3 = mutateStateScope.getNewState().getSystemState().getPackageStates().entrySet().iterator();
        while (it3.hasNext()) {
            PackageState value3 = it3.next().getValue();
            AndroidPackage androidPackage3 = value3.getAndroidPackage();
            if (androidPackage3 != null && Intrinsics.areEqual(androidPackage3.getVolumeUuid(), str)) {
                IntSet userIds = mutateStateScope.getNewState().getSystemState().getUserIds();
                int size2 = userIds.getSize();
                for (int i2 = 0; i2 < size2; i2++) {
                    inheritImplicitPermissionStates(mutateStateScope, value3.getAppId(), userIds.elementAt(i2));
                }
            }
        }
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void onPackageAdded(@NotNull MutateStateScope mutateStateScope, @NotNull PackageState packageState) {
        ArraySet<String> arraySet = new ArraySet<>();
        adoptPermissions(mutateStateScope, packageState, arraySet);
        addPermissionGroups(mutateStateScope, packageState);
        addPermissions(mutateStateScope, packageState, arraySet);
        trimPermissions(mutateStateScope, packageState.getPackageName(), arraySet);
        trimPermissionStates(mutateStateScope, packageState.getAppId());
        revokePermissionsOnPackageUpdate(mutateStateScope, packageState.getAppId());
        int size = arraySet.size();
        for (int i = 0; i < size; i++) {
            evaluatePermissionStateForAllPackages(mutateStateScope, arraySet.valueAt(i), null);
        }
        evaluateAllPermissionStatesForPackage(mutateStateScope, packageState, packageState);
        IntSet userIds = mutateStateScope.getNewState().getSystemState().getUserIds();
        int size2 = userIds.getSize();
        for (int i2 = 0; i2 < size2; i2++) {
            inheritImplicitPermissionStates(mutateStateScope, packageState.getAppId(), userIds.elementAt(i2));
        }
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void onPackageRemoved(@NotNull MutateStateScope mutateStateScope, @NotNull String str, int i) {
        if (!(!mutateStateScope.getNewState().getSystemState().getDisabledSystemPackageStates().containsKey(str))) {
            throw new IllegalStateException(("Package " + str + " reported as removed before disabled system package is enabled").toString());
        }
        ArraySet<String> arraySet = new ArraySet<>();
        trimPermissions(mutateStateScope, str, arraySet);
        if (mutateStateScope.getNewState().getSystemState().getAppIds().contains(i)) {
            trimPermissionStates(mutateStateScope, i);
        }
        int size = arraySet.size();
        for (int i2 = 0; i2 < size; i2++) {
            evaluatePermissionStateForAllPackages(mutateStateScope, arraySet.valueAt(i2), null);
        }
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void onPackageUninstalled(@NotNull MutateStateScope mutateStateScope, @NotNull String str, int i, int i2) {
        resetRuntimePermissions(mutateStateScope, str, i, i2);
    }

    public final void resetRuntimePermissions(@NotNull MutateStateScope mutateStateScope, @NotNull String str, int i, int i2) {
        AndroidPackage androidPackage;
        boolean z;
        PackageState packageState = mutateStateScope.getNewState().getSystemState().getPackageStates().get(str);
        if (packageState == null || (androidPackage = packageState.getAndroidPackage()) == null) {
            return;
        }
        List<String> requestedPermissions = androidPackage.getRequestedPermissions();
        int size = requestedPermissions.size();
        for (int i3 = 0; i3 < size; i3++) {
            String str2 = requestedPermissions.get(i3);
            Permission permission = mutateStateScope.getNewState().getSystemState().getPermissions().get(str2);
            if (permission != null && !IntExtensions.hasBits(permission.getPermissionInfo().flags, 2)) {
                AccessState newState = mutateStateScope.getNewState();
                IndexedListSet<String> indexedListSet = newState.getSystemState().getAppIds().get(i);
                int size2 = indexedListSet.size();
                int i4 = 0;
                while (true) {
                    if (i4 >= size2) {
                        z = false;
                        break;
                    }
                    PackageState packageState2 = newState.getSystemState().getPackageStates().get(indexedListSet.elementAt(i4));
                    Intrinsics.checkNotNull(packageState2);
                    PackageState packageState3 = packageState2;
                    AndroidPackage androidPackage2 = packageState3.getAndroidPackage();
                    z = true;
                    if (androidPackage2 != null && androidPackage2.getRequestedPermissions().contains(str2) && (Intrinsics.areEqual(packageState3.getPackageName(), str) ^ true)) {
                        break;
                    } else {
                        i4++;
                    }
                }
                if (!z) {
                    int permissionFlags = getPermissionFlags(mutateStateScope, i, i2, str2);
                    if (!IntExtensions.hasAnyBit(permissionFlags, 384)) {
                        int andInv = IntExtensions.andInv((IntExtensions.hasBits(permissionFlags, 8) || IntExtensions.hasBits(permissionFlags, 512)) ? permissionFlags | 16 : IntExtensions.andInv(permissionFlags, 16), 15728736);
                        if (IntExtensions.hasBits(andInv, 1024)) {
                            andInv |= 4096;
                        }
                        setPermissionFlags(mutateStateScope, i, i2, str2, andInv);
                    }
                }
            }
        }
    }

    private final void adoptPermissions(MutateStateScope mutateStateScope, PackageState packageState, ArraySet<String> arraySet) {
        AndroidPackage androidPackage = packageState.getAndroidPackage();
        Intrinsics.checkNotNull(androidPackage);
        List<String> adoptPermissions = androidPackage.getAdoptPermissions();
        int size = adoptPermissions.size();
        for (int i = 0; i < size; i++) {
            String str = adoptPermissions.get(i);
            String packageName = androidPackage.getPackageName();
            if (canAdoptPermissions(mutateStateScope, packageName, str)) {
                SystemState systemState = mutateStateScope.getNewState().getSystemState();
                ArrayMap<String, Permission> permissions = systemState.getPermissions();
                int size2 = permissions.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    String keyAt = permissions.keyAt(i2);
                    Permission valueAt = permissions.valueAt(i2);
                    String str2 = keyAt;
                    if (Intrinsics.areEqual(valueAt.getPermissionInfo().packageName, str)) {
                        PermissionInfo permissionInfo = new PermissionInfo();
                        permissionInfo.name = valueAt.getPermissionInfo().name;
                        permissionInfo.packageName = packageName;
                        permissionInfo.protectionLevel = valueAt.getPermissionInfo().protectionLevel;
                        permissions.setValueAt(i2, Permission.copy$default(valueAt, permissionInfo, false, 0, 0, null, false, 52, null));
                        WritableState.requestWrite$default(systemState, false, 1, null);
                        arraySet.add(str2);
                    }
                }
            }
        }
    }

    private final boolean canAdoptPermissions(MutateStateScope mutateStateScope, String str, String str2) {
        PackageState packageState = mutateStateScope.getNewState().getSystemState().getPackageStates().get(str2);
        if (packageState == null) {
            return false;
        }
        if (!packageState.isSystem()) {
            Log.w(LOG_TAG, "Unable to adopt permissions from " + str2 + " to " + str + ": original package not in system partition");
            return false;
        }
        if (packageState.getAndroidPackage() == null) {
            return true;
        }
        Log.w(LOG_TAG, "Unable to adopt permissions from " + str2 + " to " + str + ": original package still exists");
        return false;
    }

    private final void addPermissionGroups(MutateStateScope mutateStateScope, PackageState packageState) {
        boolean z;
        SparseArray<? extends PackageUserState> userStates = packageState.getUserStates();
        int size = userStates.size();
        int i = 0;
        while (true) {
            if (i >= size) {
                z = true;
                break;
            }
            userStates.keyAt(i);
            if (!userStates.valueAt(i).isInstantApp()) {
                z = false;
                break;
            }
            i++;
        }
        if (z) {
            Log.w(LOG_TAG, "Ignoring permission groups declared in package " + packageState.getPackageName() + ": instant apps cannot declare permission groups");
            return;
        }
        AndroidPackage androidPackage = packageState.getAndroidPackage();
        Intrinsics.checkNotNull(androidPackage);
        List<ParsedPermissionGroup> permissionGroups = androidPackage.getPermissionGroups();
        int size2 = permissionGroups.size();
        for (int i2 = 0; i2 < size2; i2++) {
            PermissionGroupInfo generatePermissionGroupInfo = PackageInfoUtils.generatePermissionGroupInfo(permissionGroups.get(i2), 128L);
            Intrinsics.checkNotNull(generatePermissionGroupInfo);
            String str = generatePermissionGroupInfo.name;
            PermissionGroupInfo permissionGroupInfo = mutateStateScope.getNewState().getSystemState().getPermissionGroups().get(str);
            if (permissionGroupInfo != null && !Intrinsics.areEqual(generatePermissionGroupInfo.packageName, permissionGroupInfo.packageName)) {
                String str2 = generatePermissionGroupInfo.packageName;
                String str3 = permissionGroupInfo.packageName;
                if (!packageState.isSystem()) {
                    Log.w(LOG_TAG, "Ignoring permission group " + str + " declared in package " + str2 + ": already declared in another package " + str3);
                } else {
                    PackageState packageState2 = mutateStateScope.getNewState().getSystemState().getPackageStates().get(str3);
                    if (packageState2 != null && packageState2.isSystem()) {
                        Log.w(LOG_TAG, "Ignoring permission group " + str + " declared in system package " + str2 + ": already declared in another system package " + str3);
                    } else {
                        Log.w(LOG_TAG, "Overriding permission group " + str + " with new declaration in system package " + str2 + ": originally declared in another package " + str3);
                    }
                }
            }
            mutateStateScope.getNewState().getSystemState().getPermissionGroups().put(str, generatePermissionGroupInfo);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:66:0x0202, code lost:
    
        if ((r7.getPermissionInfo().getProtection() == 1) != false) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x021f, code lost:
    
        r16 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x021d, code lost:
    
        if ((r7.getPermissionInfo().getProtection() == 4) == false) goto L71;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void addPermissions(MutateStateScope mutateStateScope, PackageState packageState, ArraySet<String> arraySet) {
        List<ParsedPermission> list;
        int i;
        Permission copy$default;
        int i2;
        int i3;
        IntSet intSet;
        boolean z;
        String str;
        AndroidPackage androidPackage = packageState.getAndroidPackage();
        Intrinsics.checkNotNull(androidPackage);
        List<ParsedPermission> permissions = androidPackage.getPermissions();
        int size = permissions.size();
        int i4 = 0;
        while (i4 < size) {
            ParsedPermission parsedPermission = permissions.get(i4);
            PermissionInfo generatePermissionInfo = PackageInfoUtils.generatePermissionInfo(parsedPermission, 128L);
            Intrinsics.checkNotNull(generatePermissionInfo);
            SystemState systemState = mutateStateScope.getNewState().getSystemState();
            String str2 = generatePermissionInfo.name;
            Permission permission = parsedPermission.isTree() ? systemState.getPermissionTrees().get(str2) : systemState.getPermissions().get(str2);
            Permission findPermissionTree = findPermissionTree(mutateStateScope, str2);
            String str3 = generatePermissionInfo.packageName;
            if (findPermissionTree == null || Intrinsics.areEqual(str3, findPermissionTree.getPermissionInfo().packageName)) {
                if (permission == null || Intrinsics.areEqual(str3, permission.getPermissionInfo().packageName)) {
                    if (permission != null) {
                        boolean z2 = (!generatePermissionInfo.isRuntime() || (str = generatePermissionInfo.group) == null || Intrinsics.areEqual(str, permission.getPermissionInfo().group)) ? false : true;
                        if (permission.getType() != 1) {
                            if (generatePermissionInfo.isRuntime()) {
                            }
                            if (generatePermissionInfo.getProtection() == 4) {
                            }
                        }
                        boolean z3 = false;
                        if (z2 || z3) {
                            IntSet userIds = systemState.getUserIds();
                            int size2 = userIds.getSize();
                            int i5 = 0;
                            while (i5 < size2) {
                                int elementAt = userIds.elementAt(i5);
                                SparseArray<IndexedListSet<String>> appIds = systemState.getAppIds();
                                int size3 = appIds.size();
                                int i6 = 0;
                                while (i6 < size3) {
                                    List<ParsedPermission> list2 = permissions;
                                    int keyAt = appIds.keyAt(i6);
                                    int i7 = size;
                                    int i8 = size3;
                                    SparseArray<IndexedListSet<String>> sparseArray = appIds;
                                    if (z2) {
                                        String str4 = LOG_TAG;
                                        i2 = i5;
                                        String str5 = permission.getPermissionInfo().group;
                                        i3 = size2;
                                        String str6 = generatePermissionInfo.group;
                                        intSet = userIds;
                                        StringBuilder sb = new StringBuilder();
                                        z = z2;
                                        sb.append("Revoking runtime permission ");
                                        sb.append(str2);
                                        sb.append(" for appId ");
                                        sb.append(keyAt);
                                        sb.append(" and userId ");
                                        sb.append(elementAt);
                                        sb.append(" as the permission group changed from ");
                                        sb.append(str5);
                                        sb.append(" to ");
                                        sb.append(str6);
                                        Log.w(str4, sb.toString());
                                    } else {
                                        i2 = i5;
                                        i3 = size2;
                                        intSet = userIds;
                                        z = z2;
                                    }
                                    if (z3) {
                                        Log.w(LOG_TAG, "Revoking permission " + str2 + " for appId " + keyAt + " and userId " + elementAt + " as the permission type changed.");
                                    }
                                    int i9 = elementAt;
                                    setPermissionFlags(mutateStateScope, keyAt, i9, str2, 0);
                                    i6++;
                                    i5 = i2;
                                    size3 = i8;
                                    appIds = sparseArray;
                                    permissions = list2;
                                    size = i7;
                                    elementAt = i9;
                                    size2 = i3;
                                    userIds = intSet;
                                    z2 = z;
                                }
                                i5++;
                                permissions = permissions;
                            }
                        }
                    }
                    list = permissions;
                    i = size;
                    copy$default = permission != null ? Permission.copy$default(permission, generatePermissionInfo, true, 0, packageState.getAppId(), null, false, 52, null) : new Permission(generatePermissionInfo, true, 0, packageState.getAppId(), null, false, 48, null);
                } else {
                    String str7 = permission.getPermissionInfo().packageName;
                    if (packageState.isSystem()) {
                        if (permission.getType() != 1 || permission.isReconciled()) {
                            PackageState packageState2 = systemState.getPackageStates().get(str7);
                            if (packageState2 != null && packageState2.isSystem()) {
                                Log.w(LOG_TAG, "Ignoring permission " + str2 + " declared in system package " + str3 + ": already declared in another system package " + str7);
                            } else {
                                Log.w(LOG_TAG, "Overriding permission " + str2 + " with new declaration in system package " + str3 + ": originally declared in another package " + str7);
                                IntSet userIds2 = systemState.getUserIds();
                                int size4 = userIds2.getSize();
                                int i10 = 0;
                                while (i10 < size4) {
                                    int elementAt2 = userIds2.elementAt(i10);
                                    SparseArray<IndexedListSet<String>> appIds2 = systemState.getAppIds();
                                    int size5 = appIds2.size();
                                    int i11 = 0;
                                    while (i11 < size5) {
                                        setPermissionFlags(mutateStateScope, appIds2.keyAt(i11), elementAt2, str2, 0);
                                        i11++;
                                        appIds2 = appIds2;
                                        size5 = size5;
                                        i10 = i10;
                                        size4 = size4;
                                    }
                                    i10++;
                                }
                                copy$default = new Permission(generatePermissionInfo, true, 0, packageState.getAppId(), permission.getGids(), permission.getAreGidsPerUser());
                            }
                        } else {
                            copy$default = Permission.copy$default(permission, generatePermissionInfo, true, 0, packageState.getAppId(), null, false, 52, null);
                        }
                        list = permissions;
                        i = size;
                    } else {
                        Log.w(LOG_TAG, "Ignoring permission " + str2 + " declared in package " + str3 + ": already declared in another package " + str7);
                    }
                }
                if (parsedPermission.isTree()) {
                    systemState.getPermissionTrees().put(str2, copy$default);
                } else {
                    systemState.getPermissions().put(str2, copy$default);
                }
                WritableState.requestWrite$default(systemState, false, 1, null);
                arraySet.add(str2);
                i4++;
                permissions = list;
                size = i;
            } else {
                Log.w(LOG_TAG, "Ignoring permission " + str2 + " declared in package " + str3 + ": base permission tree " + findPermissionTree.getPermissionInfo().name + " is declared in another package " + findPermissionTree.getPermissionInfo().packageName);
            }
            list = permissions;
            i = size;
            i4++;
            permissions = list;
            size = i;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x008c, code lost:
    
        if (r12 != false) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0143, code lost:
    
        if (r3 != 0) goto L76;
     */
    /* JADX WARN: Removed duplicated region for block: B:107:0x017c  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00c5  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00ca  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00ce A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:83:0x01d0  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x01d3 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void trimPermissions(MutateStateScope mutateStateScope, String str, ArraySet<String> arraySet) {
        int i;
        int i2;
        int i3;
        int i4;
        List<ParsedPermission> permissions;
        int i5;
        int i6;
        boolean z;
        boolean z2;
        List<ParsedPermission> permissions2;
        boolean z3;
        boolean z4;
        SystemState systemState = mutateStateScope.getNewState().getSystemState();
        PackageState packageState = systemState.getPackageStates().get(str);
        AndroidPackage androidPackage = packageState != null ? packageState.getAndroidPackage() : null;
        if (packageState == null || androidPackage != null) {
            PackageState packageState2 = systemState.getDisabledSystemPackageStates().get(str);
            AndroidPackage androidPackage2 = packageState2 != null ? packageState2.getAndroidPackage() : null;
            ArrayMap<String, Permission> permissionTrees = systemState.getPermissionTrees();
            int i7 = 1;
            int size = permissionTrees.size() - 1;
            int i8 = 0;
            boolean z5 = false;
            while (true) {
                if (-1 >= size) {
                    break;
                }
                String keyAt = permissionTrees.keyAt(size);
                if (Intrinsics.areEqual(permissionTrees.valueAt(size).getPermissionInfo().packageName, str)) {
                    if (packageState != null) {
                        Intrinsics.checkNotNull(androidPackage);
                        List<ParsedPermission> permissions3 = androidPackage.getPermissions();
                        int size2 = permissions3.size();
                        int i9 = 0;
                        while (true) {
                            if (i9 >= size2) {
                                z4 = true;
                                break;
                            }
                            ParsedPermission parsedPermission = permissions3.get(i9);
                            if (parsedPermission.isTree() && Intrinsics.areEqual(parsedPermission.getName(), keyAt)) {
                                z4 = false;
                                break;
                            }
                            i9++;
                        }
                    }
                    if (androidPackage2 != null && (permissions2 = androidPackage2.getPermissions()) != null) {
                        int size3 = permissions2.size();
                        int i10 = 0;
                        while (true) {
                            if (i10 >= size3) {
                                z3 = false;
                                break;
                            }
                            ParsedPermission parsedPermission2 = permissions2.get(i10);
                            if (parsedPermission2.isTree() && Intrinsics.areEqual(parsedPermission2.getName(), keyAt)) {
                                z3 = true;
                                break;
                            }
                            i10++;
                        }
                        if (z3) {
                            z2 = true;
                            if (!z2) {
                                z = true;
                                if (!z) {
                                    permissionTrees.removeAt(size);
                                    z5 = true;
                                }
                                size--;
                            }
                        }
                    }
                    z2 = false;
                    if (!z2) {
                    }
                }
                z = false;
                if (!z) {
                }
                size--;
            }
            if (z5) {
                WritableState.requestWrite$default(systemState, false, 1, null);
            }
            ArrayMap<String, Permission> permissions4 = systemState.getPermissions();
            int size4 = permissions4.size() - 1;
            for (i = -1; i < size4; i = -1) {
                String keyAt2 = permissions4.keyAt(size4);
                Permission updatePermissionIfDynamic = updatePermissionIfDynamic(mutateStateScope, permissions4.valueAt(size4));
                mutateStateScope.getNewState().getSystemState().getPermissions().setValueAt(size4, updatePermissionIfDynamic);
                if (Intrinsics.areEqual(updatePermissionIfDynamic.getPermissionInfo().packageName, str)) {
                    if (packageState != null) {
                        Intrinsics.checkNotNull(androidPackage);
                        List<ParsedPermission> permissions5 = androidPackage.getPermissions();
                        int size5 = permissions5.size();
                        int i11 = i8;
                        while (true) {
                            if (i11 >= size5) {
                                i6 = i7;
                                break;
                            }
                            ParsedPermission parsedPermission3 = permissions5.get(i11);
                            if (((parsedPermission3.isTree() || !Intrinsics.areEqual(parsedPermission3.getName(), keyAt2)) ? i8 : i7) != 0) {
                                i6 = i8;
                                break;
                            }
                            i11++;
                        }
                    }
                    if (androidPackage2 != null && (permissions = androidPackage2.getPermissions()) != null) {
                        int size6 = permissions.size();
                        int i12 = i8;
                        while (true) {
                            if (i12 >= size6) {
                                i5 = i8;
                                break;
                            }
                            ParsedPermission parsedPermission4 = permissions.get(i12);
                            if (((parsedPermission4.isTree() || !Intrinsics.areEqual(parsedPermission4.getName(), keyAt2)) ? i8 : i7) != 0) {
                                i5 = i7;
                                break;
                            }
                            i12++;
                        }
                        if (i5 == i7) {
                            i4 = i7;
                            if (i4 == 0) {
                                IntSet userIds = systemState.getUserIds();
                                int size7 = userIds.getSize();
                                int i13 = i8;
                                while (i13 < size7) {
                                    int elementAt = userIds.elementAt(i13);
                                    SparseArray<IndexedListSet<String>> appIds = systemState.getAppIds();
                                    int size8 = appIds.size();
                                    while (i8 < size8) {
                                        setPermissionFlags(mutateStateScope, appIds.keyAt(i8), elementAt, keyAt2, 0);
                                        i8++;
                                        appIds = appIds;
                                    }
                                    i13++;
                                    i8 = 0;
                                }
                                arraySet.add(keyAt2);
                                i8 = 0;
                                i2 = 1;
                                WritableState.requestWrite$default(systemState, false, 1, null);
                                i3 = 1;
                                if (i3 == 0) {
                                    permissions4.removeAt(size4);
                                }
                                size4--;
                                i7 = i2;
                            }
                            i2 = i7;
                        }
                    }
                    i4 = i8;
                    if (i4 == 0) {
                    }
                    i2 = i7;
                } else {
                    i2 = i7;
                }
                i3 = i8;
                if (i3 == 0) {
                }
                size4--;
                i7 = i2;
            }
        }
    }

    private final void trimPermissionStates(MutateStateScope mutateStateScope, int i) {
        ArraySet arraySet = new ArraySet();
        AccessState newState = mutateStateScope.getNewState();
        IndexedListSet<String> indexedListSet = newState.getSystemState().getAppIds().get(i);
        Intrinsics.checkNotNull(indexedListSet);
        IndexedListSet<String> indexedListSet2 = indexedListSet;
        int size = indexedListSet2.size();
        for (int i2 = 0; i2 < size; i2++) {
            PackageState packageState = newState.getSystemState().getPackageStates().get(indexedListSet2.elementAt(i2));
            Intrinsics.checkNotNull(packageState);
            PackageState packageState2 = packageState;
            if (packageState2.getAndroidPackage() != null) {
                AndroidPackage androidPackage = packageState2.getAndroidPackage();
                Intrinsics.checkNotNull(androidPackage);
                MutableCollections.addAll(arraySet, androidPackage.getRequestedPermissions());
            }
        }
        SparseArray<UserState> userStates = mutateStateScope.getNewState().getUserStates();
        int size2 = userStates.size();
        for (int i3 = 0; i3 < size2; i3++) {
            int keyAt = userStates.keyAt(i3);
            ArrayMap<String, Integer> arrayMap = userStates.valueAt(i3).getUidPermissionFlags().get(i);
            if (arrayMap != null) {
                for (int size3 = arrayMap.size() - 1; -1 < size3; size3--) {
                    String keyAt2 = arrayMap.keyAt(size3);
                    arrayMap.valueAt(size3).intValue();
                    String str = keyAt2;
                    if (!arraySet.contains(str)) {
                        setPermissionFlags(mutateStateScope, i, keyAt, str, 0);
                    }
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x00cf A[LOOP:2: B:19:0x008c->B:28:0x00cf, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00cd A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0131 A[LOOP:3: B:32:0x00f0->B:41:0x0131, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x012f A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void revokePermissionsOnPackageUpdate(MutateStateScope mutateStateScope, int i) {
        int i2;
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        UidPermissionPolicy uidPermissionPolicy = this;
        SparseArray<UserState> userStates = mutateStateScope.getNewState().getUserStates();
        int size = userStates.size();
        int i3 = 0;
        while (i3 < size) {
            int keyAt = userStates.keyAt(i3);
            ArrayMap<String, Integer> arrayMap = userStates.valueAt(i3).getUidPermissionFlags().get(i);
            if (arrayMap != null) {
                boolean z5 = true;
                int size2 = arrayMap.size() - 1;
                while (-1 < size2) {
                    String keyAt2 = arrayMap.keyAt(size2);
                    int intValue = arrayMap.valueAt(size2).intValue();
                    String str = keyAt2;
                    if (!STORAGE_AND_MEDIA_PERMISSIONS.contains(str) || intValue == 0) {
                        i2 = size2;
                    } else {
                        int appIdTargetSdkVersion = uidPermissionPolicy.getAppIdTargetSdkVersion(mutateStateScope, i, str, mutateStateScope.getOldState());
                        int appIdTargetSdkVersion2 = uidPermissionPolicy.getAppIdTargetSdkVersion(mutateStateScope, i, str, mutateStateScope.getNewState());
                        boolean z6 = (appIdTargetSdkVersion < 29 || appIdTargetSdkVersion2 >= 29) ? false : z5;
                        boolean z7 = (appIdTargetSdkVersion >= 29 || appIdTargetSdkVersion2 < 29) ? false : z5;
                        AccessState oldState = mutateStateScope.getOldState();
                        IndexedListSet<String> indexedListSet = oldState.getSystemState().getAppIds().get(i);
                        int size3 = indexedListSet.size();
                        int i4 = 0;
                        while (true) {
                            if (i4 >= size3) {
                                z = false;
                                break;
                            }
                            IndexedListSet<String> indexedListSet2 = indexedListSet;
                            AccessState accessState = oldState;
                            PackageState packageState = oldState.getSystemState().getPackageStates().get(indexedListSet.elementAt(i4));
                            Intrinsics.checkNotNull(packageState);
                            PackageState packageState2 = packageState;
                            AndroidPackage androidPackage = packageState2.getAndroidPackage();
                            if (androidPackage != null && androidPackage.getRequestedPermissions().contains(str)) {
                                AndroidPackage androidPackage2 = packageState2.getAndroidPackage();
                                Intrinsics.checkNotNull(androidPackage2);
                                if (androidPackage2.isRequestLegacyExternalStorage()) {
                                    z4 = true;
                                    if (!z4) {
                                        z = true;
                                        break;
                                    } else {
                                        i4++;
                                        indexedListSet = indexedListSet2;
                                        oldState = accessState;
                                    }
                                }
                            }
                            z4 = false;
                            if (!z4) {
                            }
                        }
                        AccessState newState = mutateStateScope.getNewState();
                        IndexedListSet<String> indexedListSet3 = newState.getSystemState().getAppIds().get(i);
                        int size4 = indexedListSet3.size();
                        int i5 = size2;
                        int i6 = 0;
                        while (true) {
                            if (i6 >= size4) {
                                z2 = false;
                                break;
                            }
                            AccessState accessState2 = newState;
                            PackageState packageState3 = newState.getSystemState().getPackageStates().get(indexedListSet3.elementAt(i6));
                            Intrinsics.checkNotNull(packageState3);
                            PackageState packageState4 = packageState3;
                            AndroidPackage androidPackage3 = packageState4.getAndroidPackage();
                            if (androidPackage3 != null && androidPackage3.getRequestedPermissions().contains(str)) {
                                AndroidPackage androidPackage4 = packageState4.getAndroidPackage();
                                Intrinsics.checkNotNull(androidPackage4);
                                if (androidPackage4.isRequestLegacyExternalStorage()) {
                                    z3 = true;
                                    if (!z3) {
                                        z2 = true;
                                        break;
                                    } else {
                                        i6++;
                                        newState = accessState2;
                                    }
                                }
                            }
                            z3 = false;
                            if (!z3) {
                            }
                        }
                        if ((((z7 || z || !z2) ? false : true) || z6) && IntExtensions.hasBits(intValue, 16)) {
                            i2 = i5;
                            setPermissionFlags(mutateStateScope, i, keyAt, str, IntExtensions.andInv(intValue, 15728752));
                        } else {
                            i2 = i5;
                        }
                    }
                    size2 = i2 - 1;
                    z5 = true;
                    uidPermissionPolicy = this;
                }
            }
            i3++;
            uidPermissionPolicy = this;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0071 A[LOOP:2: B:6:0x003d->B:13:0x0071, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0077 A[EDGE_INSN: B:14:0x0077->B:15:0x0077 BREAK  A[LOOP:2: B:6:0x003d->B:13:0x0071], SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void evaluatePermissionStateForAllPackages(MutateStateScope mutateStateScope, String str, PackageState packageState) {
        boolean z;
        boolean z2;
        SystemState systemState = mutateStateScope.getNewState().getSystemState();
        IntSet userIds = systemState.getUserIds();
        int size = userIds.getSize();
        for (int i = 0; i < size; i++) {
            int elementAt = userIds.elementAt(i);
            SparseArray<IndexedListSet<String>> appIds = systemState.getAppIds();
            int size2 = appIds.size();
            for (int i2 = 0; i2 < size2; i2++) {
                int keyAt = appIds.keyAt(i2);
                AccessState newState = mutateStateScope.getNewState();
                IndexedListSet<String> indexedListSet = newState.getSystemState().getAppIds().get(keyAt);
                int size3 = indexedListSet.size();
                int i3 = 0;
                while (true) {
                    if (i3 >= size3) {
                        z = false;
                        break;
                    }
                    PackageState packageState2 = newState.getSystemState().getPackageStates().get(indexedListSet.elementAt(i3));
                    Intrinsics.checkNotNull(packageState2);
                    AndroidPackage androidPackage = packageState2.getAndroidPackage();
                    z = true;
                    if (androidPackage != null && androidPackage.getRequestedPermissions().contains(str)) {
                        z2 = true;
                        if (!z2) {
                            break;
                        } else {
                            i3++;
                        }
                    }
                    z2 = false;
                    if (!z2) {
                    }
                }
                if (z) {
                    evaluatePermissionState(mutateStateScope, keyAt, elementAt, str, packageState);
                }
            }
        }
    }

    private final void evaluateAllPermissionStatesForPackage(MutateStateScope mutateStateScope, PackageState packageState, PackageState packageState2) {
        IntSet userIds = mutateStateScope.getNewState().getSystemState().getUserIds();
        int size = userIds.getSize();
        for (int i = 0; i < size; i++) {
            evaluateAllPermissionStatesForPackageAndUser(mutateStateScope, packageState, userIds.elementAt(i), packageState2);
        }
    }

    private final void evaluateAllPermissionStatesForPackageAndUser(MutateStateScope mutateStateScope, PackageState packageState, int i, PackageState packageState2) {
        List<String> requestedPermissions;
        AndroidPackage androidPackage = packageState.getAndroidPackage();
        if (androidPackage == null || (requestedPermissions = androidPackage.getRequestedPermissions()) == null) {
            return;
        }
        int size = requestedPermissions.size();
        for (int i2 = 0; i2 < size; i2++) {
            evaluatePermissionState(mutateStateScope, packageState.getAppId(), i, requestedPermissions.get(i2), packageState2);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:118:0x045a  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x045f  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x04ce  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x0509 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:154:0x0506 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:160:0x045c  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x026d A[LOOP:6: B:206:0x022a->B:215:0x026d, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:216:0x026b A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:228:0x02f6  */
    /* JADX WARN: Removed duplicated region for block: B:237:0x0324  */
    /* JADX WARN: Removed duplicated region for block: B:244:0x0343 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:247:0x034f  */
    /* JADX WARN: Removed duplicated region for block: B:252:0x02fa  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00cc  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x011d  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0158 A[LOOP:2: B:48:0x011b->B:57:0x0158, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0156 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x015b A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0103 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void evaluatePermissionState(MutateStateScope mutateStateScope, int i, int i2, String str, PackageState packageState) {
        boolean z;
        boolean z2;
        int i3;
        boolean z3;
        boolean z4;
        int size;
        int i4;
        boolean z5;
        int i5;
        boolean z6;
        boolean z7;
        boolean z8;
        boolean z9;
        boolean z10;
        int andInv;
        int i6;
        boolean z11;
        boolean z12;
        boolean z13;
        int size2;
        int i7;
        boolean z14;
        int size3;
        int i8;
        boolean z15;
        boolean z16;
        IndexedListSet<String> indexedListSet = mutateStateScope.getNewState().getSystemState().getAppIds().get(i);
        int size4 = indexedListSet.size();
        int i9 = 0;
        while (true) {
            if (i9 >= size4) {
                z = false;
                break;
            }
            PackageState packageState2 = mutateStateScope.getNewState().getSystemState().getPackageStates().get(indexedListSet.elementAt(i9));
            Intrinsics.checkNotNull(packageState2);
            if (packageState2.getAndroidPackage() == null) {
                z = true;
                break;
            }
            i9++;
        }
        if (indexedListSet.size() == 1 && z) {
            return;
        }
        Permission permission = mutateStateScope.getNewState().getSystemState().getPermissions().get(str);
        int permissionFlags = getPermissionFlags(mutateStateScope, i, i2, str);
        if (permission == null) {
            if (permissionFlags == 0) {
                setPermissionFlags(mutateStateScope, i, i2, str, 2);
                return;
            }
            return;
        }
        if (permission.getPermissionInfo().getProtection() == 0) {
            if (IntExtensions.hasBits(permissionFlags, 1)) {
                return;
            }
            boolean hasBits = IntExtensions.hasBits(permissionFlags, 2);
            if (packageState != null) {
                AndroidPackage androidPackage = packageState.getAndroidPackage();
                Intrinsics.checkNotNull(androidPackage);
                if (androidPackage.getRequestedPermissions().contains(str)) {
                    z13 = true;
                    AccessState newState = mutateStateScope.getNewState();
                    IndexedListSet<String> indexedListSet2 = newState.getSystemState().getAppIds().get(i);
                    size2 = indexedListSet2.size();
                    i7 = 0;
                    while (true) {
                        if (i7 < size2) {
                            z14 = false;
                            break;
                        }
                        PackageState packageState3 = newState.getSystemState().getPackageStates().get(indexedListSet2.elementAt(i7));
                        Intrinsics.checkNotNull(packageState3);
                        PackageState packageState4 = packageState3;
                        AndroidPackage androidPackage2 = packageState4.getAndroidPackage();
                        if (androidPackage2 != null && androidPackage2.getRequestedPermissions().contains(str) && packageState4.isSystem()) {
                            z14 = true;
                            break;
                        }
                        i7++;
                    }
                    AccessState newState2 = mutateStateScope.getNewState();
                    IndexedListSet<String> indexedListSet3 = newState2.getSystemState().getAppIds().get(i);
                    size3 = indexedListSet3.size();
                    i8 = 0;
                    while (true) {
                        if (i8 < size3) {
                            z15 = false;
                            break;
                        }
                        PackageState packageState5 = newState2.getSystemState().getPackageStates().get(indexedListSet3.elementAt(i8));
                        Intrinsics.checkNotNull(packageState5);
                        PackageState packageState6 = packageState5;
                        AndroidPackage androidPackage3 = packageState6.getAndroidPackage();
                        if (androidPackage3 != null && androidPackage3.getRequestedPermissions().contains(str)) {
                            AndroidPackage androidPackage4 = packageState6.getAndroidPackage();
                            Intrinsics.checkNotNull(androidPackage4);
                            if (isCompatibilityPermissionForPackage(androidPackage4, str)) {
                                z16 = true;
                                if (!z16) {
                                    z15 = true;
                                    break;
                                }
                                i8++;
                            }
                        }
                        z16 = false;
                        if (!z16) {
                        }
                    }
                    setPermissionFlags(mutateStateScope, i, i2, str, (hasBits || z13 || z14 || z15) ? 1 : 2);
                    return;
                }
            }
            z13 = false;
            AccessState newState3 = mutateStateScope.getNewState();
            IndexedListSet<String> indexedListSet22 = newState3.getSystemState().getAppIds().get(i);
            size2 = indexedListSet22.size();
            i7 = 0;
            while (true) {
                if (i7 < size2) {
                }
                i7++;
            }
            AccessState newState22 = mutateStateScope.getNewState();
            IndexedListSet<String> indexedListSet32 = newState22.getSystemState().getAppIds().get(i);
            size3 = indexedListSet32.size();
            i8 = 0;
            while (true) {
                if (i8 < size3) {
                }
                i8++;
            }
            setPermissionFlags(mutateStateScope, i, i2, str, (hasBits || z13 || z14 || z15) ? 1 : 2);
            return;
        }
        if (permission.getPermissionInfo().getProtection() == 2) {
            z2 = true;
            i3 = 4;
        } else {
            if (!(permission.getPermissionInfo().getProtection() == 4)) {
                if (!(permission.getPermissionInfo().getProtection() == 1)) {
                    Log.e(LOG_TAG, "Unknown protection level " + permission.getPermissionInfo().protectionLevel + "for permission " + permission.getPermissionInfo().name + " while evaluating permission statefor appId " + i + " and userId " + i2);
                    return;
                }
                int i10 = permissionFlags & 16777208;
                if (getAppIdTargetSdkVersion$default(this, mutateStateScope, i, str, null, 4, null) < 23) {
                    if (IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), 8192)) {
                        andInv = i10 & 229376;
                    } else {
                        andInv = i10 | 1024;
                        if (getOldStatePermissionFlags(mutateStateScope, i, i2, str) == 0) {
                            andInv |= 4096;
                        }
                    }
                    i6 = 229376;
                    z9 = true;
                } else {
                    boolean hasBits2 = IntExtensions.hasBits(i10, 1024);
                    int andInv2 = IntExtensions.andInv(i10, 1024);
                    boolean hasBits3 = IntExtensions.hasBits(andInv2, 2048);
                    boolean z17 = mutateStateScope.getNewState().getSystemState().isLeanback() && NOTIFICATIONS_PERMISSIONS.contains(str);
                    AccessState newState4 = mutateStateScope.getNewState();
                    IndexedListSet<String> indexedListSet4 = newState4.getSystemState().getAppIds().get(i);
                    int size5 = indexedListSet4.size();
                    int i11 = 0;
                    while (true) {
                        if (i11 >= size5) {
                            z8 = false;
                            break;
                        }
                        PackageState packageState7 = newState4.getSystemState().getPackageStates().get(indexedListSet4.elementAt(i11));
                        Intrinsics.checkNotNull(packageState7);
                        PackageState packageState8 = packageState7;
                        AndroidPackage androidPackage5 = packageState8.getAndroidPackage();
                        if (androidPackage5 != null && androidPackage5.getRequestedPermissions().contains(str)) {
                            AndroidPackage androidPackage6 = packageState8.getAndroidPackage();
                            Intrinsics.checkNotNull(androidPackage6);
                            if (androidPackage6.getImplicitPermissions().contains(str)) {
                                z12 = true;
                                if (!z12) {
                                    z8 = true;
                                    break;
                                }
                                i11++;
                            }
                        }
                        z12 = false;
                        if (!z12) {
                        }
                    }
                    IndexedListSet<String> indexedListSet5 = mutateStateScope.getNewState().getSystemState().getImplicitToSourcePermissions().get(str);
                    if (indexedListSet5 != null) {
                        if (!indexedListSet5.isEmpty()) {
                            for (String str2 : indexedListSet5) {
                                Permission permission2 = mutateStateScope.getNewState().getSystemState().getPermissions().get(str2);
                                if (permission2 == null) {
                                    throw new IllegalStateException(("Unknown source permission " + str2 + " in split permissions").toString());
                                }
                                int protection = permission2.getPermissionInfo().getProtection();
                                z9 = true;
                                if (!(protection == 1)) {
                                    z10 = true;
                                    break;
                                }
                            }
                        } else {
                            z10 = false;
                            z9 = true;
                        }
                        boolean z18 = (!z17 || (z8 && z10)) ? z9 : false;
                        int andInv3 = !z18 ? andInv2 | 2048 : IntExtensions.andInv(andInv2, 2048);
                        andInv = ((!hasBits2 || hasBits3) && !z18) ? IntExtensions.andInv(andInv3, 1048576) : andInv3;
                        boolean hasBits4 = IntExtensions.hasBits(andInv, 4096);
                        if (!z8 && hasBits4) {
                            andInv = IntExtensions.andInv(andInv, 4096);
                            if (NEARBY_DEVICES_PERMISSIONS.contains(str)) {
                                int permissionFlags2 = getPermissionFlags(mutateStateScope, i, i2, "android.permission.ACCESS_BACKGROUND_LOCATION");
                                if (PermissionFlags.INSTANCE.isAppOpGranted(permissionFlags2) && !IntExtensions.hasBits(permissionFlags2, 4096)) {
                                    z11 = z9;
                                    boolean hasAnyBit = IntExtensions.hasAnyBit(andInv, 384);
                                    if (z11 && !hasAnyBit) {
                                        andInv = IntExtensions.andInv(andInv, HdmiCecKeycode.UI_BROADCAST_DIGITAL_CABLE);
                                    } else if (hasBits3) {
                                        andInv |= 16;
                                    }
                                }
                            }
                            z11 = false;
                            boolean hasAnyBit2 = IntExtensions.hasAnyBit(andInv, 384);
                            if (z11) {
                            }
                            if (hasBits3) {
                            }
                        }
                        i6 = 229376;
                    }
                    z9 = true;
                    z10 = false;
                    if (z17) {
                    }
                    if (!z18) {
                    }
                    if (hasBits2) {
                    }
                    boolean hasBits42 = IntExtensions.hasBits(andInv, 4096);
                    if (!z8) {
                        andInv = IntExtensions.andInv(andInv, 4096);
                        if (NEARBY_DEVICES_PERMISSIONS.contains(str)) {
                        }
                        z11 = false;
                        boolean hasAnyBit22 = IntExtensions.hasAnyBit(andInv, 384);
                        if (z11) {
                        }
                        if (hasBits3) {
                        }
                    }
                    i6 = 229376;
                }
                boolean hasAnyBit3 = IntExtensions.hasAnyBit(andInv, i6);
                int andInv4 = (!IntExtensions.hasBits(permission.getPermissionInfo().flags, 4) || hasAnyBit3) ? false : z9 ? andInv | DumpState.DUMP_DOMAIN_PREFERRED : IntExtensions.andInv(andInv, DumpState.DUMP_DOMAIN_PREFERRED);
                if (!IntExtensions.hasBits(permission.getPermissionInfo().flags, 8) || hasAnyBit3) {
                    z9 = false;
                }
                setPermissionFlags(mutateStateScope, i, i2, str, z9 ? andInv4 | 524288 : IntExtensions.andInv(andInv4, 524288));
                return;
            }
            i3 = 4;
            z2 = true;
        }
        boolean hasBits5 = IntExtensions.hasBits(permissionFlags, i3);
        if (z && hasBits5) {
            i5 = i3;
        } else {
            if (IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), 16)) {
                AccessState newState5 = mutateStateScope.getNewState();
                IndexedListSet<String> indexedListSet6 = newState5.getSystemState().getAppIds().get(i);
                int size6 = indexedListSet6.size();
                int i12 = 0;
                while (true) {
                    if (i12 >= size6) {
                        z7 = false;
                        break;
                    }
                    PackageState packageState9 = newState5.getSystemState().getPackageStates().get(indexedListSet6.elementAt(i12));
                    Intrinsics.checkNotNull(packageState9);
                    PackageState packageState10 = packageState9;
                    AndroidPackage androidPackage7 = packageState10.getAndroidPackage();
                    if ((androidPackage7 != null && androidPackage7.getRequestedPermissions().contains(str) && checkPrivilegedPermissionAllowlist(mutateStateScope, packageState10, permission)) ? z2 : false) {
                        z7 = z2;
                        break;
                    }
                    i12++;
                }
                if (!z7) {
                    z3 = false;
                    if (permission.getPermissionInfo().getProtection() != 2 ? z2 : false) {
                        AccessState newState6 = mutateStateScope.getNewState();
                        IndexedListSet<String> indexedListSet7 = newState6.getSystemState().getAppIds().get(i);
                        int size7 = indexedListSet7.size();
                        int i13 = 0;
                        while (true) {
                            if (i13 >= size7) {
                                z6 = false;
                                break;
                            }
                            PackageState packageState11 = newState6.getSystemState().getPackageStates().get(indexedListSet7.elementAt(i13));
                            Intrinsics.checkNotNull(packageState11);
                            PackageState packageState12 = packageState11;
                            AndroidPackage androidPackage8 = packageState12.getAndroidPackage();
                            if ((androidPackage8 != null && androidPackage8.getRequestedPermissions().contains(str) && shouldGrantPermissionBySignature(mutateStateScope, packageState12, permission)) ? z2 : false) {
                                z6 = z2;
                                break;
                            }
                            i13++;
                        }
                        if (z6) {
                            z4 = z2;
                            AccessState newState7 = mutateStateScope.getNewState();
                            IndexedListSet<String> indexedListSet8 = newState7.getSystemState().getAppIds().get(i);
                            size = indexedListSet8.size();
                            i4 = 0;
                            while (true) {
                                if (i4 >= size) {
                                    z5 = false;
                                    break;
                                }
                                PackageState packageState13 = newState7.getSystemState().getPackageStates().get(indexedListSet8.elementAt(i4));
                                Intrinsics.checkNotNull(packageState13);
                                PackageState packageState14 = packageState13;
                                AndroidPackage androidPackage9 = packageState14.getAndroidPackage();
                                if ((androidPackage9 != null && androidPackage9.getRequestedPermissions().contains(str) && shouldGrantPermissionByProtectionFlags(mutateStateScope, packageState14, permission)) ? z2 : false) {
                                    z5 = z2;
                                    break;
                                }
                                i4++;
                            }
                            i5 = (z3 || !(z4 || z5)) ? 0 : 4;
                        }
                    }
                    z4 = false;
                    AccessState newState72 = mutateStateScope.getNewState();
                    IndexedListSet<String> indexedListSet82 = newState72.getSystemState().getAppIds().get(i);
                    size = indexedListSet82.size();
                    i4 = 0;
                    while (true) {
                        if (i4 >= size) {
                        }
                        i4++;
                    }
                    if (z3) {
                    }
                }
            }
            z3 = z2;
            if (permission.getPermissionInfo().getProtection() != 2 ? z2 : false) {
            }
            z4 = false;
            AccessState newState722 = mutateStateScope.getNewState();
            IndexedListSet<String> indexedListSet822 = newState722.getSystemState().getAppIds().get(i);
            size = indexedListSet822.size();
            i4 = 0;
            while (true) {
                if (i4 >= size) {
                }
                i4++;
            }
            if (z3) {
            }
        }
        if (IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), 32)) {
            i5 |= permissionFlags & 16;
        }
        setPermissionFlags(mutateStateScope, i, i2, str, IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), 67108864) ? (permissionFlags & 24) | i5 : i5);
    }

    private final void inheritImplicitPermissionStates(MutateStateScope mutateStateScope, int i, int i2) {
        IndexedListSet<String> indexedListSet;
        ArraySet arraySet = new ArraySet();
        AccessState newState = mutateStateScope.getNewState();
        IndexedListSet<String> indexedListSet2 = newState.getSystemState().getAppIds().get(i);
        Intrinsics.checkNotNull(indexedListSet2);
        IndexedListSet<String> indexedListSet3 = indexedListSet2;
        int size = indexedListSet3.size();
        for (int i3 = 0; i3 < size; i3++) {
            PackageState packageState = newState.getSystemState().getPackageStates().get(indexedListSet3.elementAt(i3));
            Intrinsics.checkNotNull(packageState);
            PackageState packageState2 = packageState;
            if (packageState2.getAndroidPackage() != null) {
                AndroidPackage androidPackage = packageState2.getAndroidPackage();
                Intrinsics.checkNotNull(androidPackage);
                MutableCollections.addAll(arraySet, androidPackage.getImplicitPermissions());
            }
        }
        int size2 = arraySet.size();
        for (int i4 = 0; i4 < size2; i4++) {
            String str = (String) arraySet.valueAt(i4);
            Permission permission = mutateStateScope.getNewState().getSystemState().getPermissions().get(str);
            if (permission != null) {
                if (permission.getPermissionInfo().getProtection() == 1) {
                    if ((getOldStatePermissionFlags(mutateStateScope, i, i2, str) == 0) && (indexedListSet = mutateStateScope.getNewState().getSystemState().getImplicitToSourcePermissions().get(str)) != null) {
                        int permissionFlags = getPermissionFlags(mutateStateScope, i, i2, str);
                        int size3 = indexedListSet.size();
                        for (int i5 = 0; i5 < size3; i5++) {
                            String elementAt = indexedListSet.elementAt(i5);
                            if (mutateStateScope.getNewState().getSystemState().getPermissions().get(elementAt) == null) {
                                throw new IllegalStateException(("Unknown source permission " + elementAt + " in split permissions").toString());
                            }
                            int permissionFlags2 = getPermissionFlags(mutateStateScope, i, i2, elementAt);
                            PermissionFlags permissionFlags3 = PermissionFlags.INSTANCE;
                            boolean isPermissionGranted = permissionFlags3.isPermissionGranted(permissionFlags2);
                            boolean isPermissionGranted2 = permissionFlags3.isPermissionGranted(permissionFlags);
                            boolean z = isPermissionGranted && !isPermissionGranted2;
                            if (isPermissionGranted == isPermissionGranted2 || z) {
                                if (z) {
                                    permissionFlags = 0;
                                }
                                permissionFlags = (permissionFlags2 & 16777208) | permissionFlags;
                            }
                        }
                        setPermissionFlags(mutateStateScope, i, i2, str, RETAIN_IMPLICIT_FLAGS_PERMISSIONS.contains(str) ? IntExtensions.andInv(permissionFlags, 4096) : permissionFlags | 4096);
                    }
                }
            } else {
                throw new IllegalStateException(("Unknown implicit permission " + str + " in split permissions").toString());
            }
        }
    }

    private final boolean isCompatibilityPermissionForPackage(AndroidPackage androidPackage, String str) {
        for (CompatibilityPermissionInfo compatibilityPermissionInfo : CompatibilityPermissionInfo.COMPAT_PERMS) {
            if (Intrinsics.areEqual(compatibilityPermissionInfo.getName(), str) && androidPackage.getTargetSdkVersion() < compatibilityPermissionInfo.getSdkVersion()) {
                Log.i(LOG_TAG, "Auto-granting " + str + " to old package " + androidPackage.getPackageName());
                return true;
            }
        }
        return false;
    }

    private final boolean shouldGrantPermissionBySignature(MutateStateScope mutateStateScope, PackageState packageState, Permission permission) {
        AndroidPackage androidPackage;
        AndroidPackage androidPackage2 = packageState.getAndroidPackage();
        Intrinsics.checkNotNull(androidPackage2);
        SigningDetails signingDetails = androidPackage2.getSigningDetails();
        PackageState packageState2 = mutateStateScope.getNewState().getSystemState().getPackageStates().get(permission.getPermissionInfo().packageName);
        SigningDetails signingDetails2 = (packageState2 == null || (androidPackage = packageState2.getAndroidPackage()) == null) ? null : androidPackage.getSigningDetails();
        PackageState packageState3 = mutateStateScope.getNewState().getSystemState().getPackageStates().get(PackageManagerService.PLATFORM_PACKAGE_NAME);
        Intrinsics.checkNotNull(packageState3);
        AndroidPackage androidPackage3 = packageState3.getAndroidPackage();
        Intrinsics.checkNotNull(androidPackage3);
        SigningDetails signingDetails3 = androidPackage3.getSigningDetails();
        return (signingDetails2 != null && signingDetails2.hasCommonSignerWithCapability(signingDetails, 4)) || signingDetails.hasAncestorOrSelf(signingDetails3) || signingDetails3.checkCapability(signingDetails, 4);
    }

    private final boolean checkPrivilegedPermissionAllowlist(MutateStateScope mutateStateScope, PackageState packageState, Permission permission) {
        if (RoSystemProperties.CONTROL_PRIVAPP_PERMISSIONS_DISABLE || Intrinsics.areEqual(packageState.getPackageName(), PackageManagerService.PLATFORM_PACKAGE_NAME) || !packageState.isSystem() || !packageState.isPrivileged() || !mutateStateScope.getNewState().getSystemState().getPrivilegedPermissionAllowlistPackages().contains(permission.getPermissionInfo().packageName)) {
            return true;
        }
        Boolean privilegedPermissionAllowlistState = getPrivilegedPermissionAllowlistState(mutateStateScope, packageState, permission.getPermissionInfo().name);
        if (privilegedPermissionAllowlistState != null) {
            return privilegedPermissionAllowlistState.booleanValue();
        }
        if (packageState.isUpdatedSystemApp()) {
            return true;
        }
        if (!mutateStateScope.getNewState().getSystemState().isSystemReady() && !packageState.isApkInUpdatedApex()) {
            Log.w(LOG_TAG, "Privileged permission " + permission.getPermissionInfo().name + " for package " + packageState.getPackageName() + " (" + packageState.getPath() + ") not in privileged permission allowlist");
            if (RoSystemProperties.CONTROL_PRIVAPP_PERMISSIONS_ENFORCE) {
                this.privilegedPermissionAllowlistViolations.add(packageState.getPackageName() + " (" + packageState.getPath() + "): " + permission.getPermissionInfo().name);
            }
        }
        return !RoSystemProperties.CONTROL_PRIVAPP_PERMISSIONS_ENFORCE;
    }

    private final Boolean getPrivilegedPermissionAllowlistState(MutateStateScope mutateStateScope, PackageState packageState, String str) {
        PermissionAllowlist permissionAllowlist = mutateStateScope.getNewState().getSystemState().getPermissionAllowlist();
        String apexModuleName = packageState.getApexModuleName();
        String packageName = packageState.getPackageName();
        if (packageState.isVendor()) {
            return permissionAllowlist.getVendorPrivilegedAppAllowlistState(packageName, str);
        }
        if (packageState.isProduct()) {
            return permissionAllowlist.getProductPrivilegedAppAllowlistState(packageName, str);
        }
        if (packageState.isSystemExt()) {
            return permissionAllowlist.getSystemExtPrivilegedAppAllowlistState(packageName, str);
        }
        if (apexModuleName != null) {
            Boolean privilegedAppAllowlistState = permissionAllowlist.getPrivilegedAppAllowlistState(packageName, str);
            if (privilegedAppAllowlistState != null) {
                Log.w(LOG_TAG, "Package " + packageName + " is an APK in APEX but has permission allowlist on the system image, please bundle the allowlist in the " + apexModuleName + " APEX instead");
            }
            Boolean apexPrivilegedAppAllowlistState = permissionAllowlist.getApexPrivilegedAppAllowlistState(apexModuleName, packageName, str);
            return apexPrivilegedAppAllowlistState == null ? privilegedAppAllowlistState : apexPrivilegedAppAllowlistState;
        }
        return permissionAllowlist.getPrivilegedAppAllowlistState(packageName, str);
    }

    static /* synthetic */ int getAppIdTargetSdkVersion$default(UidPermissionPolicy uidPermissionPolicy, MutateStateScope mutateStateScope, int i, String str, AccessState accessState, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            accessState = mutateStateScope.getNewState();
        }
        return uidPermissionPolicy.getAppIdTargetSdkVersion(mutateStateScope, i, str, accessState);
    }

    private final int getAppIdTargetSdkVersion(MutateStateScope mutateStateScope, int i, String str, AccessState accessState) {
        IndexedListSet<String> indexedListSet = accessState.getSystemState().getAppIds().get(i);
        int size = indexedListSet.size();
        int i2 = 10000;
        for (int i3 = 0; i3 < size; i3++) {
            PackageState packageState = accessState.getSystemState().getPackageStates().get(indexedListSet.elementAt(i3));
            Intrinsics.checkNotNull(packageState);
            PackageState packageState2 = packageState;
            AndroidPackage androidPackage = packageState2.getAndroidPackage();
            if (androidPackage != null && androidPackage.getRequestedPermissions().contains(str)) {
                AndroidPackage androidPackage2 = packageState2.getAndroidPackage();
                Intrinsics.checkNotNull(androidPackage2);
                i2 = _Ranges.coerceAtMost(i2, androidPackage2.getTargetSdkVersion());
            }
        }
        return i2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x00c7, code lost:
    
        if (r3 != false) goto L35;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final boolean shouldGrantPermissionByProtectionFlags(MutateStateScope mutateStateScope, PackageState packageState, Permission permission) {
        boolean shouldGrantPrivilegedOrOemPermission;
        boolean contains;
        boolean contains2;
        boolean contains3;
        boolean contains4;
        boolean contains5;
        boolean contains6;
        boolean contains7;
        boolean contains8;
        boolean contains9;
        boolean contains10;
        boolean contains11;
        AndroidPackage androidPackage = packageState.getAndroidPackage();
        Intrinsics.checkNotNull(androidPackage);
        SparseArray<String[]> knownPackages = mutateStateScope.getNewState().getSystemState().getKnownPackages();
        String packageName = packageState.getPackageName();
        if ((IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), 16) || IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), 16384)) && packageState.isSystem()) {
            if (packageState.isUpdatedSystemApp()) {
                PackageState packageState2 = mutateStateScope.getNewState().getSystemState().getDisabledSystemPackageStates().get(packageState.getPackageName());
                AndroidPackage androidPackage2 = packageState2 != null ? packageState2.getAndroidPackage() : null;
                shouldGrantPrivilegedOrOemPermission = androidPackage2 != null && androidPackage2.getRequestedPermissions().contains(permission.getPermissionInfo().name) && shouldGrantPrivilegedOrOemPermission(mutateStateScope, packageState2, permission);
            } else {
                shouldGrantPrivilegedOrOemPermission = shouldGrantPrivilegedOrOemPermission(mutateStateScope, packageState, permission);
            }
            if (shouldGrantPrivilegedOrOemPermission) {
                return true;
            }
        }
        if (IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), 128) && androidPackage.getTargetSdkVersion() < 23) {
            return true;
        }
        if (IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), 256)) {
            contains10 = _Arrays.contains(knownPackages.get(2), packageName);
            if (!contains10) {
                contains11 = _Arrays.contains(knownPackages.get(7), packageName);
            }
            return true;
        }
        if (IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), 512)) {
            contains9 = _Arrays.contains(knownPackages.get(4), packageName);
            if (contains9) {
                return true;
            }
        }
        if (IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), 1024) && packageState.isSystem()) {
            return true;
        }
        if (IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), DumpState.DUMP_KNOWN_PACKAGES) && androidPackage.getSigningDetails().hasAncestorOrSelfWithDigest(permission.getPermissionInfo().knownCerts)) {
            return true;
        }
        if (IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), 2048)) {
            contains8 = _Arrays.contains(knownPackages.get(1), packageName);
            if (contains8) {
                return true;
            }
        }
        if (IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), 65536)) {
            contains7 = _Arrays.contains(knownPackages.get(6), packageName);
            if (contains7) {
                return true;
            }
        }
        if (IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), 524288)) {
            contains6 = _Arrays.contains(knownPackages.get(10), packageName);
            if (contains6) {
                return true;
            }
        }
        if (IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), 1048576)) {
            contains5 = _Arrays.contains(knownPackages.get(11), packageName);
            if (contains5) {
                return true;
            }
        }
        if (IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), DumpState.DUMP_COMPILER_STATS)) {
            contains4 = _Arrays.contains(knownPackages.get(12), packageName);
            if (contains4) {
                return true;
            }
        }
        if (IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), 8388608)) {
            contains3 = _Arrays.contains(knownPackages.get(15), packageName);
            if (contains3) {
                return true;
            }
        }
        if (IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), DumpState.DUMP_SERVICE_PERMISSIONS)) {
            contains2 = _Arrays.contains(knownPackages.get(16), packageName);
            if (contains2 && isDeviceOrProfileOwnerUid(mutateStateScope, packageState.getAppId())) {
                return true;
            }
        }
        if (IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), DumpState.DUMP_APEX)) {
            contains = _Arrays.contains(knownPackages.get(17), packageName);
            if (contains) {
                return true;
            }
        }
        return IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), DumpState.DUMP_CHANGES) && packageState.getApexModuleName() != null;
    }

    private final boolean isDeviceOrProfileOwnerUid(MutateStateScope mutateStateScope, int i) {
        PackageState packageState;
        int userId = UserHandle.getUserId(i);
        String str = mutateStateScope.getNewState().getSystemState().getDeviceAndProfileOwners().get(userId);
        return (str == null || (packageState = mutateStateScope.getNewState().getSystemState().getPackageStates().get(str)) == null || i != UserHandle.getUid(userId, packageState.getAppId())) ? false : true;
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void onSystemReady(@NotNull MutateStateScope mutateStateScope) {
        if (this.privilegedPermissionAllowlistViolations.isEmpty()) {
            return;
        }
        throw new IllegalStateException("Signature|privileged permissions not in privileged permission allowlist: " + this.privilegedPermissionAllowlistViolations);
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void parseSystemState(@NotNull BinaryXmlPullParser binaryXmlPullParser, @NotNull AccessState accessState) {
        this.persistence.parseSystemState(binaryXmlPullParser, accessState);
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void serializeSystemState(@NotNull BinaryXmlSerializer binaryXmlSerializer, @NotNull AccessState accessState) {
        this.persistence.serializeSystemState(binaryXmlSerializer, accessState);
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void parseUserState(@NotNull BinaryXmlPullParser binaryXmlPullParser, @NotNull AccessState accessState, int i) {
        this.persistence.parseUserState(binaryXmlPullParser, accessState, i);
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void serializeUserState(@NotNull BinaryXmlSerializer binaryXmlSerializer, @NotNull AccessState accessState, int i) {
        this.persistence.serializeUserState(binaryXmlSerializer, accessState, i);
    }

    @NotNull
    public final ArrayMap<String, Permission> getPermissionTrees(@NotNull GetStateScope getStateScope) {
        return getStateScope.getState().getSystemState().getPermissionTrees();
    }

    @Nullable
    public final Permission findPermissionTree(@NotNull GetStateScope getStateScope, @NotNull String str) {
        Permission permission;
        boolean startsWith$default;
        ArrayMap<String, Permission> permissionTrees = getStateScope.getState().getSystemState().getPermissionTrees();
        int size = permissionTrees.size();
        int i = 0;
        while (true) {
            permission = null;
            if (i >= size) {
                break;
            }
            String keyAt = permissionTrees.keyAt(i);
            Permission valueAt = permissionTrees.valueAt(i);
            String str2 = keyAt;
            startsWith$default = StringsJVM.startsWith$default(str, str2, false, 2, null);
            if (startsWith$default && str.length() > str2.length() && str.charAt(str2.length()) == '.') {
                permission = valueAt;
            }
            if (permission != null) {
                break;
            }
            i++;
        }
        return permission;
    }

    @NotNull
    public final ArrayMap<String, PermissionGroupInfo> getPermissionGroups(@NotNull GetStateScope getStateScope) {
        return getStateScope.getState().getSystemState().getPermissionGroups();
    }

    @NotNull
    public final ArrayMap<String, Permission> getPermissions(@NotNull GetStateScope getStateScope) {
        return getStateScope.getState().getSystemState().getPermissions();
    }

    public final void addPermission(@NotNull MutateStateScope mutateStateScope, @NotNull Permission permission, boolean z) {
        mutateStateScope.getNewState().getSystemState().getPermissions().put(permission.getPermissionInfo().name, permission);
        mutateStateScope.getNewState().getSystemState().requestWrite(z);
    }

    public final void removePermission(@NotNull MutateStateScope mutateStateScope, @NotNull Permission permission) {
        mutateStateScope.getNewState().getSystemState().getPermissions().remove(permission.getPermissionInfo().name);
        WritableState.requestWrite$default(mutateStateScope.getNewState().getSystemState(), false, 1, null);
    }

    @Nullable
    public final ArrayMap<String, Integer> getUidPermissionFlags(@NotNull GetStateScope getStateScope, int i, int i2) {
        SparseArray<ArrayMap<String, Integer>> uidPermissionFlags;
        UserState userState = getStateScope.getState().getUserStates().get(i2);
        if (userState == null || (uidPermissionFlags = userState.getUidPermissionFlags()) == null) {
            return null;
        }
        return uidPermissionFlags.get(i);
    }

    public final int getPermissionFlags(@NotNull GetStateScope getStateScope, int i, int i2, @NotNull String str) {
        return getPermissionFlags(getStateScope.getState(), i, i2, str);
    }

    private final int getOldStatePermissionFlags(MutateStateScope mutateStateScope, int i, int i2, String str) {
        return getPermissionFlags(mutateStateScope.getOldState(), i, i2, str);
    }

    private final int getPermissionFlags(AccessState accessState, int i, int i2, String str) {
        int indexOfKey;
        SparseArray<ArrayMap<String, Integer>> uidPermissionFlags;
        UserState userState = accessState.getUserStates().get(i2);
        ArrayMap<String, Integer> arrayMap = (userState == null || (uidPermissionFlags = userState.getUidPermissionFlags()) == null) ? null : uidPermissionFlags.get(i);
        Integer num = 0;
        if (arrayMap != null && (indexOfKey = arrayMap.indexOfKey(str)) >= 0) {
            num = arrayMap.valueAt(indexOfKey);
        }
        return num.intValue();
    }

    public final boolean setPermissionFlags(@NotNull MutateStateScope mutateStateScope, int i, int i2, @NotNull String str, int i3) {
        return updatePermissionFlags(mutateStateScope, i, i2, str, -1, i3);
    }

    public final boolean updatePermissionFlags(@NotNull MutateStateScope mutateStateScope, int i, int i2, @NotNull String str, int i3, int i4) {
        Integer num;
        int indexOfKey;
        UserState userState = mutateStateScope.getNewState().getUserStates().get(i2);
        SparseArray<ArrayMap<String, Integer>> uidPermissionFlags = userState.getUidPermissionFlags();
        ArrayMap<String, Integer> arrayMap = uidPermissionFlags.get(i);
        if (arrayMap != null && (indexOfKey = arrayMap.indexOfKey(str)) >= 0) {
            num = arrayMap.valueAt(indexOfKey);
        } else {
            num = 0;
        }
        int intValue = num.intValue();
        int andInv = IntExtensions.andInv(intValue, i3) | (i4 & i3);
        if (intValue == andInv) {
            return false;
        }
        if (arrayMap == null) {
            arrayMap = new ArrayMap<>();
            uidPermissionFlags.set(i, arrayMap);
        }
        Integer valueOf = Integer.valueOf(andInv);
        int indexOfKey2 = arrayMap.indexOfKey(str);
        if (indexOfKey2 >= 0) {
            if (!Intrinsics.areEqual(valueOf, arrayMap.valueAt(indexOfKey2))) {
                if (Intrinsics.areEqual(valueOf, 0)) {
                    arrayMap.removeAt(indexOfKey2);
                } else {
                    arrayMap.setValueAt(indexOfKey2, valueOf);
                }
            }
        } else if (!Intrinsics.areEqual(valueOf, 0)) {
            arrayMap.put(str, valueOf);
        }
        if (arrayMap.isEmpty()) {
            uidPermissionFlags.remove(i);
        }
        WritableState.requestWrite$default(userState, false, 1, null);
        IndexedListSet<OnPermissionFlagsChangedListener> indexedListSet = this.onPermissionFlagsChangedListeners;
        int size = indexedListSet.size();
        for (int i5 = 0; i5 < size; i5++) {
            indexedListSet.elementAt(i5).onPermissionFlagsChanged(i, i2, str, intValue, andInv);
        }
        return true;
    }

    public final void addOnPermissionFlagsChangedListener(@NotNull OnPermissionFlagsChangedListener onPermissionFlagsChangedListener) {
        synchronized (this.onPermissionFlagsChangedListenersLock) {
            IndexedListSet<OnPermissionFlagsChangedListener> copy = this.onPermissionFlagsChangedListeners.copy();
            copy.add(onPermissionFlagsChangedListener);
            this.onPermissionFlagsChangedListeners = copy;
            Unit unit = Unit.INSTANCE;
        }
    }

    /* compiled from: UidPermissionPolicy.kt */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    static {
        List asList;
        List asList2;
        List asList3;
        List asList4;
        asList = _ArraysJvm.asList(new String[]{"android.permission.ACCESS_MEDIA_LOCATION", "android.permission.ACTIVITY_RECOGNITION", "android.permission.READ_MEDIA_AUDIO", "android.permission.READ_MEDIA_IMAGES", "android.permission.READ_MEDIA_VIDEO"});
        RETAIN_IMPLICIT_FLAGS_PERMISSIONS = new ArraySet<>(asList);
        asList2 = _ArraysJvm.asList(new String[]{"android.permission.BLUETOOTH_ADVERTISE", "android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN", "android.permission.NEARBY_WIFI_DEVICES"});
        NEARBY_DEVICES_PERMISSIONS = new ArraySet<>(asList2);
        asList3 = _ArraysJvm.asList(new String[]{"android.permission.POST_NOTIFICATIONS"});
        NOTIFICATIONS_PERMISSIONS = new ArraySet<>(asList3);
        asList4 = _ArraysJvm.asList(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_MEDIA_AUDIO", "android.permission.READ_MEDIA_VIDEO", "android.permission.READ_MEDIA_IMAGES", "android.permission.ACCESS_MEDIA_LOCATION", "android.permission.READ_MEDIA_VISUAL_USER_SELECTED"});
        STORAGE_AND_MEDIA_PERMISSIONS = new ArraySet<>(asList4);
    }
}
