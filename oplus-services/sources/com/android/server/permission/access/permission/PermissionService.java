package com.android.server.permission.access.permission;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.IActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.permission.SplitPermissionInfoParcelable;
import android.metrics.LogMaker;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.permission.IOnPermissionsChangeListener;
import android.permission.PermissionControllerManager;
import android.permission.PermissionManager;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.IntArray;
import android.util.Log;
import android.util.SparseBooleanArray;
import com.android.internal.compat.IPlatformCompat;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.Preconditions;
import com.android.server.FgThread;
import com.android.server.LocalManagerRegistry;
import com.android.server.LocalServices;
import com.android.server.PermissionThread;
import com.android.server.ServiceThread;
import com.android.server.SystemConfig;
import com.android.server.permission.access.AccessCheckingService;
import com.android.server.permission.access.AccessState;
import com.android.server.permission.access.GetStateScope;
import com.android.server.permission.access.MutateStateScope;
import com.android.server.permission.access.SchemePolicy;
import com.android.server.permission.access.appop.UidAppOpPolicy;
import com.android.server.permission.access.collection.IntSet;
import com.android.server.permission.access.permission.UidPermissionPolicy;
import com.android.server.permission.access.util.IntExtensions;
import com.android.server.permission.jarjar.kotlin.Unit;
import com.android.server.permission.jarjar.kotlin.collections.CollectionsKt__CollectionsKt;
import com.android.server.permission.jarjar.kotlin.collections.MutableCollections;
import com.android.server.permission.jarjar.kotlin.collections.SetsKt__SetsKt;
import com.android.server.permission.jarjar.kotlin.collections._Arrays;
import com.android.server.permission.jarjar.kotlin.collections._ArraysJvm;
import com.android.server.permission.jarjar.kotlin.collections._Collections;
import com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseable;
import com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker;
import com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics;
import com.android.server.permission.jarjar.kotlin.jvm.internal.Ref$IntRef;
import com.android.server.permission.jarjar.kotlin.jvm.internal.Ref$ObjectRef;
import com.android.server.pm.DumpState;
import com.android.server.pm.PackageInstallerService;
import com.android.server.pm.PackageManagerLocal;
import com.android.server.pm.UserManagerInternal;
import com.android.server.pm.UserManagerService;
import com.android.server.pm.parsing.pkg.AndroidPackageUtils;
import com.android.server.pm.permission.LegacyPermission;
import com.android.server.pm.permission.LegacyPermissionSettings;
import com.android.server.pm.permission.LegacyPermissionState;
import com.android.server.pm.permission.PermissionManagerServiceInterface;
import com.android.server.pm.permission.PermissionManagerServiceInternal;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageState;
import com.android.server.pm.pkg.PackageStateInternal;
import com.android.server.pm.pkg.parsing.ParsingPackageUtils;
import com.android.server.pm.verify.domain.DomainVerificationLegacySettings;
import com.android.server.policy.SoftRestrictedPermissionPolicy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import libcore.util.EmptyArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: PermissionService.kt */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class PermissionService implements PermissionManagerServiceInterface {
    private static final long BACKUP_TIMEOUT_MILLIS;

    @NotNull
    private static final ArrayMap<String, String> FULLER_PERMISSIONS;

    @NotNull
    private static final ArraySet<String> NOTIFICATIONS_PERMISSIONS;
    private final Context context;
    private Handler handler;
    private HandlerThread handlerThread;

    @NotNull
    private final SparseBooleanArray isDelayedPermissionBackupFinished;
    private MetricsLogger metricsLogger;

    @NotNull
    private final ArraySet<String> mountedStorageVolumes;
    private OnPermissionFlagsChangedListener onPermissionFlagsChangedListener;
    private OnPermissionsChangeListeners onPermissionsChangeListeners;
    private PackageManagerInternal packageManagerInternal;
    private PackageManagerLocal packageManagerLocal;
    private PermissionControllerManager permissionControllerManager;
    private IPlatformCompat platformCompat;

    @NotNull
    private final UidPermissionPolicy policy;

    @NotNull
    private final AccessCheckingService service;
    private SystemConfig systemConfig;
    private UserManagerInternal userManagerInternal;
    private UserManagerService userManagerService;

    @NotNull
    public static final Companion Companion = new Companion(null);
    private static final String LOG_TAG = PermissionService.class.getSimpleName();
    private static final long BACKGROUND_RATIONALE_CHANGE_ID = 147316723;

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void addOnRuntimePermissionStateChangedListener(@NotNull PermissionManagerServiceInternal.OnRuntimePermissionStateChangedListener onRuntimePermissionStateChangedListener) {
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageRemoved(@NotNull AndroidPackage androidPackage) {
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void readLegacyPermissionStateTEMP() {
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void removeOnRuntimePermissionStateChangedListener(@NotNull PermissionManagerServiceInternal.OnRuntimePermissionStateChangedListener onRuntimePermissionStateChangedListener) {
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void resetRuntimePermissions(@NotNull AndroidPackage androidPackage, int i) {
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void resetRuntimePermissionsForUser(int i) {
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void writeLegacyPermissionStateTEMP() {
    }

    public PermissionService(@NotNull AccessCheckingService accessCheckingService) {
        this.service = accessCheckingService;
        SchemePolicy schemePolicy$frameworks__base__services__permission__android_common__services_permission = accessCheckingService.getSchemePolicy$frameworks__base__services__permission__android_common__services_permission("uid", ParsingPackageUtils.TAG_PERMISSION);
        Intrinsics.checkNotNull(schemePolicy$frameworks__base__services__permission__android_common__services_permission, "null cannot be cast to non-null type com.android.server.permission.access.permission.UidPermissionPolicy");
        this.policy = (UidPermissionPolicy) schemePolicy$frameworks__base__services__permission__android_common__services_permission;
        this.context = accessCheckingService.getContext();
        this.mountedStorageVolumes = new ArraySet<>();
        this.isDelayedPermissionBackupFinished = new SparseBooleanArray();
    }

    public final void initialize() {
        this.metricsLogger = new MetricsLogger();
        this.packageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        this.packageManagerLocal = (PackageManagerLocal) LocalManagerRegistry.getManagerOrThrow(PackageManagerLocal.class);
        this.platformCompat = IPlatformCompat.Stub.asInterface(ServiceManager.getService("platform_compat"));
        this.systemConfig = SystemConfig.getInstance();
        this.userManagerInternal = (UserManagerInternal) LocalServices.getService(UserManagerInternal.class);
        this.userManagerService = UserManagerService.getInstance();
        ServiceThread serviceThread = new ServiceThread(LOG_TAG, 10, true);
        serviceThread.start();
        this.handlerThread = serviceThread;
        HandlerThread handlerThread = this.handlerThread;
        if (handlerThread == null) {
            Intrinsics.throwUninitializedPropertyAccessException("handlerThread");
            handlerThread = null;
        }
        this.handler = new Handler(handlerThread.getLooper());
        this.onPermissionsChangeListeners = new OnPermissionsChangeListeners(FgThread.get().getLooper());
        OnPermissionFlagsChangedListener onPermissionFlagsChangedListener = new OnPermissionFlagsChangedListener();
        this.onPermissionFlagsChangedListener = onPermissionFlagsChangedListener;
        this.policy.addOnPermissionFlagsChangedListener(onPermissionFlagsChangedListener);
    }

    private final List<LegacyPermission> toLegacyPermissions(ArrayMap<String, Permission> arrayMap) {
        ArrayList arrayList = new ArrayList();
        int size = arrayMap.size();
        for (int i = 0; i < size; i++) {
            arrayMap.keyAt(i);
            Permission valueAt = arrayMap.valueAt(i);
            arrayList.add(new LegacyPermission(valueAt.getPermissionInfo(), valueAt.getType(), 0, EmptyArray.INT));
        }
        return arrayList;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    @NotNull
    public List<PermissionGroupInfo> getAllPermissionGroups(int i) {
        List<PermissionGroupInfo> emptyList;
        PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        PackageManagerLocal.UnfilteredSnapshot withUnfilteredSnapshot = packageManagerLocal.withUnfilteredSnapshot();
        try {
            int callingUid = Binder.getCallingUid();
            if (!isUidInstantApp(withUnfilteredSnapshot, callingUid)) {
                AccessState accessState = this.service.state;
                if (accessState == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("state");
                    accessState = null;
                }
                ArrayMap<String, PermissionGroupInfo> permissionGroups = this.policy.getPermissionGroups(new GetStateScope(accessState));
                ArrayList arrayList = new ArrayList();
                int size = permissionGroups.size();
                for (int i2 = 0; i2 < size; i2++) {
                    String keyAt = permissionGroups.keyAt(i2);
                    PermissionGroupInfo valueAt = permissionGroups.valueAt(i2);
                    String str = keyAt;
                    PermissionGroupInfo generatePermissionGroupInfo = isPackageVisibleToUid(withUnfilteredSnapshot, valueAt.packageName, callingUid) ? generatePermissionGroupInfo(valueAt, i) : null;
                    if (generatePermissionGroupInfo != null) {
                        arrayList.add(generatePermissionGroupInfo);
                    }
                }
                AutoCloseable.closeFinally(withUnfilteredSnapshot, null);
                return arrayList;
            }
            emptyList = CollectionsKt__CollectionsKt.emptyList();
            AutoCloseable.closeFinally(withUnfilteredSnapshot, null);
            return emptyList;
        } finally {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v3, types: [android.content.pm.PermissionGroupInfo, T] */
    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    @Nullable
    public PermissionGroupInfo getPermissionGroupInfo(@NotNull String str, int i) {
        Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
        PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        PackageManagerLocal.UnfilteredSnapshot withUnfilteredSnapshot = packageManagerLocal.withUnfilteredSnapshot();
        try {
            int callingUid = Binder.getCallingUid();
            if (!isUidInstantApp(withUnfilteredSnapshot, callingUid)) {
                AccessState accessState = this.service.state;
                if (accessState == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("state");
                    accessState = null;
                }
                PermissionGroupInfo permissionGroupInfo = this.policy.getPermissionGroups(new GetStateScope(accessState)).get(str);
                if (permissionGroupInfo != 0) {
                    ref$ObjectRef.element = permissionGroupInfo;
                    PermissionGroupInfo permissionGroupInfo2 = permissionGroupInfo;
                    if (!isPackageVisibleToUid(withUnfilteredSnapshot, permissionGroupInfo.packageName, callingUid)) {
                        AutoCloseable.closeFinally(withUnfilteredSnapshot, null);
                        return null;
                    }
                    Unit unit = Unit.INSTANCE;
                    AutoCloseable.closeFinally(withUnfilteredSnapshot, null);
                    return generatePermissionGroupInfo((PermissionGroupInfo) ref$ObjectRef.element, i);
                }
                AutoCloseable.closeFinally(withUnfilteredSnapshot, null);
                return null;
            }
            AutoCloseable.closeFinally(withUnfilteredSnapshot, null);
            return null;
        } finally {
        }
    }

    private final PermissionGroupInfo generatePermissionGroupInfo(PermissionGroupInfo permissionGroupInfo, int i) {
        PermissionGroupInfo permissionGroupInfo2 = new PermissionGroupInfo(permissionGroupInfo);
        if (!IntExtensions.hasBits(i, 128)) {
            permissionGroupInfo2.metaData = null;
        }
        return permissionGroupInfo2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v3, types: [T, com.android.server.permission.access.permission.Permission] */
    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    @Nullable
    public PermissionInfo getPermissionInfo(@NotNull String str, int i, @NotNull String str2) {
        Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
        PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        PackageManagerLocal.UnfilteredSnapshot withUnfilteredSnapshot = packageManagerLocal.withUnfilteredSnapshot();
        try {
            int callingUid = Binder.getCallingUid();
            if (!isUidInstantApp(withUnfilteredSnapshot, callingUid)) {
                AccessState accessState = this.service.state;
                if (accessState == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("state");
                    accessState = null;
                }
                Permission permission = this.policy.getPermissions(new GetStateScope(accessState)).get(str);
                if (permission != 0) {
                    ref$ObjectRef.element = permission;
                    if (!isPackageVisibleToUid(withUnfilteredSnapshot, permission.getPermissionInfo().packageName, callingUid)) {
                        AutoCloseable.closeFinally(withUnfilteredSnapshot, null);
                        return null;
                    }
                    PackageState packageState = getPackageState(withUnfilteredSnapshot, str2);
                    AndroidPackage androidPackage = packageState != null ? packageState.getAndroidPackage() : null;
                    boolean isRootOrSystemOrShell = isRootOrSystemOrShell(callingUid);
                    int i2 = 10000;
                    if (!isRootOrSystemOrShell && androidPackage != null) {
                        i2 = androidPackage.getTargetSdkVersion();
                    }
                    Unit unit = Unit.INSTANCE;
                    AutoCloseable.closeFinally(withUnfilteredSnapshot, null);
                    return generatePermissionInfo((Permission) ref$ObjectRef.element, i, i2);
                }
                AutoCloseable.closeFinally(withUnfilteredSnapshot, null);
                return null;
            }
            AutoCloseable.closeFinally(withUnfilteredSnapshot, null);
            return null;
        } finally {
        }
    }

    static /* synthetic */ PermissionInfo generatePermissionInfo$default(PermissionService permissionService, Permission permission, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i2 = 10000;
        }
        return permissionService.generatePermissionInfo(permission, i, i2);
    }

    private final PermissionInfo generatePermissionInfo(Permission permission, int i, int i2) {
        int protection;
        PermissionInfo permissionInfo = new PermissionInfo(permission.getPermissionInfo());
        permissionInfo.flags |= 1073741824;
        if (!IntExtensions.hasBits(i, 128)) {
            permissionInfo.metaData = null;
        }
        if (i2 < 26 && (protection = permissionInfo.getProtection()) != 2) {
            permissionInfo.protectionLevel = protection;
        }
        return permissionInfo;
    }

    /* JADX WARN: Type inference failed for: r11v0, types: [T, android.util.ArrayMap] */
    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    @Nullable
    public List<PermissionInfo> queryPermissionsByGroup(@Nullable String str, int i) {
        PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        PackageManagerLocal.UnfilteredSnapshot withUnfilteredSnapshot = packageManagerLocal.withUnfilteredSnapshot();
        try {
            int callingUid = Binder.getCallingUid();
            if (isUidInstantApp(withUnfilteredSnapshot, callingUid)) {
                AutoCloseable.closeFinally(withUnfilteredSnapshot, null);
                return null;
            }
            Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
            AccessState accessState = this.service.state;
            if (accessState == null) {
                Intrinsics.throwUninitializedPropertyAccessException("state");
                accessState = null;
            }
            GetStateScope getStateScope = new GetStateScope(accessState);
            if (str != null) {
                PermissionGroupInfo permissionGroupInfo = this.policy.getPermissionGroups(getStateScope).get(str);
                if (permissionGroupInfo == null) {
                    AutoCloseable.closeFinally(withUnfilteredSnapshot, null);
                    return null;
                }
                if (!isPackageVisibleToUid(withUnfilteredSnapshot, permissionGroupInfo.packageName, callingUid)) {
                    AutoCloseable.closeFinally(withUnfilteredSnapshot, null);
                    return null;
                }
            }
            ?? permissions = this.policy.getPermissions(getStateScope);
            ref$ObjectRef.element = permissions;
            ArrayList arrayList = new ArrayList();
            int size = permissions.size();
            for (int i2 = 0; i2 < size; i2++) {
                Object keyAt = permissions.keyAt(i2);
                Permission permission = (Permission) permissions.valueAt(i2);
                PermissionInfo generatePermissionInfo$default = (Intrinsics.areEqual(permission.getPermissionInfo().group, str) && isPackageVisibleToUid(withUnfilteredSnapshot, permission.getPermissionInfo().packageName, callingUid)) ? generatePermissionInfo$default(this, permission, i, 0, 2, null) : null;
                if (generatePermissionInfo$default != null) {
                    arrayList.add(generatePermissionInfo$default);
                }
            }
            AutoCloseable.closeFinally(withUnfilteredSnapshot, null);
            return arrayList;
        } finally {
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    @NotNull
    public List<PermissionInfo> getAllPermissionsWithProtection(int i) {
        AccessState accessState = this.service.state;
        if (accessState == null) {
            Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        ArrayMap<String, Permission> permissions = this.policy.getPermissions(new GetStateScope(accessState));
        ArrayList arrayList = new ArrayList();
        int size = permissions.size();
        for (int i2 = 0; i2 < size; i2++) {
            permissions.keyAt(i2);
            Permission valueAt = permissions.valueAt(i2);
            PermissionInfo generatePermissionInfo$default = valueAt.getPermissionInfo().getProtection() == i ? generatePermissionInfo$default(this, valueAt, 0, 0, 2, null) : null;
            if (generatePermissionInfo$default != null) {
                arrayList.add(generatePermissionInfo$default);
            }
        }
        return arrayList;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    @NotNull
    public List<PermissionInfo> getAllPermissionsWithProtectionFlags(int i) {
        AccessState accessState = this.service.state;
        if (accessState == null) {
            Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        ArrayMap<String, Permission> permissions = this.policy.getPermissions(new GetStateScope(accessState));
        ArrayList arrayList = new ArrayList();
        int size = permissions.size();
        for (int i2 = 0; i2 < size; i2++) {
            permissions.keyAt(i2);
            Permission valueAt = permissions.valueAt(i2);
            PermissionInfo generatePermissionInfo$default = IntExtensions.hasBits(valueAt.getPermissionInfo().getProtectionFlags(), i) ? generatePermissionInfo$default(this, valueAt, 0, 0, 2, null) : null;
            if (generatePermissionInfo$default != null) {
                arrayList.add(generatePermissionInfo$default);
            }
        }
        return arrayList;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    @NotNull
    public int[] getPermissionGids(@NotNull String str, int i) {
        AccessState accessState = this.service.state;
        if (accessState == null) {
            Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        Permission permission = this.policy.getPermissions(new GetStateScope(accessState)).get(str);
        if (permission == null) {
            return EmptyArray.INT;
        }
        return permission.getGidsForUser(i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    @NotNull
    public Set<String> getInstalledPermissions(@NotNull String str) {
        if (str != null) {
            AccessState accessState = this.service.state;
            if (accessState == null) {
                Intrinsics.throwUninitializedPropertyAccessException("state");
                accessState = null;
            }
            ArrayMap<String, Permission> permissions = this.policy.getPermissions(new GetStateScope(accessState));
            ArraySet arraySet = new ArraySet();
            int size = permissions.size();
            for (int i = 0; i < size; i++) {
                permissions.keyAt(i);
                Permission valueAt = permissions.valueAt(i);
                String str2 = Intrinsics.areEqual(valueAt.getPermissionInfo().packageName, str) ? valueAt.getPermissionInfo().name : null;
                if (str2 != null) {
                    arraySet.add(str2);
                }
            }
            return arraySet;
        }
        throw new IllegalArgumentException("packageName cannot be null".toString());
    }

    /* JADX WARN: Type inference failed for: r4v6, types: [T, com.android.server.permission.access.permission.Permission] */
    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean addPermission(@NotNull PermissionInfo permissionInfo, boolean z) {
        String str = permissionInfo.name;
        if (str == null) {
            throw new IllegalArgumentException("permissionName cannot be null".toString());
        }
        int callingUid = Binder.getCallingUid();
        PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        AccessState accessState = null;
        if (packageManagerLocal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        PackageManagerLocal.UnfilteredSnapshot withUnfilteredSnapshot = packageManagerLocal.withUnfilteredSnapshot();
        try {
            boolean isUidInstantApp = isUidInstantApp(withUnfilteredSnapshot, callingUid);
            AutoCloseable.closeFinally(withUnfilteredSnapshot, null);
            if (isUidInstantApp) {
                throw new SecurityException("Instant apps cannot add permissions");
            }
            if (permissionInfo.labelRes == 0 && permissionInfo.nonLocalizedLabel == null) {
                throw new SecurityException("Label must be specified in permission");
            }
            Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
            AccessCheckingService accessCheckingService = this.service;
            synchronized (accessCheckingService.stateLock) {
                AccessState accessState2 = accessCheckingService.state;
                if (accessState2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("state");
                } else {
                    accessState = accessState2;
                }
                AccessState copy = accessState.copy();
                MutateStateScope mutateStateScope = new MutateStateScope(accessState, copy);
                Permission andEnforcePermissionTree = getAndEnforcePermissionTree(mutateStateScope, str);
                enforcePermissionTreeSize(mutateStateScope, permissionInfo, andEnforcePermissionTree);
                Permission permission = this.policy.getPermissions(mutateStateScope).get(str);
                ref$ObjectRef.element = permission;
                if (permission != 0) {
                    if (!(permission.getType() == 2)) {
                        throw new SecurityException("Not allowed to modify non-dynamic permission " + str);
                    }
                }
                permissionInfo.packageName = andEnforcePermissionTree.getPermissionInfo().packageName;
                permissionInfo.protectionLevel = PermissionInfo.fixProtectionLevel(permissionInfo.protectionLevel);
                this.policy.addPermission(mutateStateScope, new Permission(permissionInfo, true, 2, andEnforcePermissionTree.getAppId(), null, false, 48, null), !z);
                accessCheckingService.persistence.write(copy);
                accessCheckingService.state = copy;
                accessCheckingService.policy.onStateMutated(new GetStateScope(copy));
                Unit unit = Unit.INSTANCE;
            }
            return ref$ObjectRef.element == 0;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                AutoCloseable.closeFinally(withUnfilteredSnapshot, th);
                throw th2;
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void removePermission(@NotNull String str) {
        int callingUid = Binder.getCallingUid();
        PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        AccessState accessState = null;
        if (packageManagerLocal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        PackageManagerLocal.UnfilteredSnapshot withUnfilteredSnapshot = packageManagerLocal.withUnfilteredSnapshot();
        try {
            boolean isUidInstantApp = isUidInstantApp(withUnfilteredSnapshot, callingUid);
            AutoCloseable.closeFinally(withUnfilteredSnapshot, null);
            if (isUidInstantApp) {
                throw new SecurityException("Instant applications don't have access to this method");
            }
            AccessCheckingService accessCheckingService = this.service;
            synchronized (accessCheckingService.stateLock) {
                AccessState accessState2 = accessCheckingService.state;
                if (accessState2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("state");
                } else {
                    accessState = accessState2;
                }
                AccessState copy = accessState.copy();
                MutateStateScope mutateStateScope = new MutateStateScope(accessState, copy);
                getAndEnforcePermissionTree(mutateStateScope, str);
                Permission permission = this.policy.getPermissions(mutateStateScope).get(str);
                if (permission != null) {
                    if (permission.getType() == 2) {
                        this.policy.removePermission(mutateStateScope, permission);
                    } else {
                        throw new SecurityException("Not allowed to modify non-dynamic permission " + str);
                    }
                }
                accessCheckingService.persistence.write(copy);
                accessCheckingService.state = copy;
                accessCheckingService.policy.onStateMutated(new GetStateScope(copy));
                Unit unit = Unit.INSTANCE;
            }
        } finally {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Permission getAndEnforcePermissionTree(GetStateScope getStateScope, String str) {
        int callingUid = Binder.getCallingUid();
        Permission findPermissionTree = this.policy.findPermissionTree(getStateScope, str);
        if (findPermissionTree != null && findPermissionTree.getAppId() == UserHandle.getAppId(callingUid)) {
            return findPermissionTree;
        }
        throw new SecurityException("Calling UID " + callingUid + " is not allowed to add to or remove from the permission tree");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void enforcePermissionTreeSize(GetStateScope getStateScope, PermissionInfo permissionInfo, Permission permission) {
        if (permission.getAppId() != 1000 && calculatePermissionTreeFootprint(getStateScope, permission) + permissionInfo.calculateFootprint() > 32768) {
            throw new SecurityException("Permission tree size cap exceeded");
        }
    }

    private final int calculatePermissionTreeFootprint(GetStateScope getStateScope, Permission permission) {
        ArrayMap<String, Permission> permissions = this.policy.getPermissions(getStateScope);
        int size = permissions.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            Permission valueAt = permissions.valueAt(i2);
            if (permission.getAppId() == valueAt.getAppId()) {
                i += valueAt.getPermissionInfo().name.length() + valueAt.getPermissionInfo().calculateFootprint();
            }
        }
        return i;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int checkUidPermission(int i, @NotNull String str) {
        int userId = UserHandle.getUserId(i);
        UserManagerInternal userManagerInternal = this.userManagerInternal;
        AccessState accessState = null;
        if (userManagerInternal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("userManagerInternal");
            userManagerInternal = null;
        }
        if (!userManagerInternal.exists(userId)) {
            return -1;
        }
        PackageManagerInternal packageManagerInternal = this.packageManagerInternal;
        if (packageManagerInternal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
            packageManagerInternal = null;
        }
        AndroidPackage androidPackage = packageManagerInternal.getPackage(i);
        if (androidPackage == null) {
            return isSystemUidPermissionGranted(i, str) ? 0 : -1;
        }
        PackageManagerInternal packageManagerInternal2 = this.packageManagerInternal;
        if (packageManagerInternal2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
            packageManagerInternal2 = null;
        }
        PackageStateInternal packageStateInternal = packageManagerInternal2.getPackageStateInternal(androidPackage.getPackageName());
        if (packageStateInternal != null) {
            AccessState accessState2 = this.service.state;
            if (accessState2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("state");
            } else {
                accessState = accessState2;
            }
            return isPermissionGranted(new GetStateScope(accessState), packageStateInternal, userId, str) ? 0 : -1;
        }
        Log.e(LOG_TAG, "checkUidPermission: PackageState not found for AndroidPackage " + androidPackage);
        return -1;
    }

    private final boolean isSystemUidPermissionGranted(int i, String str) {
        SystemConfig systemConfig = this.systemConfig;
        if (systemConfig == null) {
            Intrinsics.throwUninitializedPropertyAccessException("systemConfig");
            systemConfig = null;
        }
        ArraySet arraySet = (ArraySet) systemConfig.getSystemPermissions().get(i);
        if (arraySet == null) {
            return false;
        }
        if (arraySet.contains(str)) {
            return true;
        }
        String str2 = FULLER_PERMISSIONS.get(str);
        return str2 != null && arraySet.contains(str2);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int checkPermission(@NotNull String str, @NotNull String str2, int i) {
        UserManagerInternal userManagerInternal = this.userManagerInternal;
        AccessState accessState = null;
        if (userManagerInternal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("userManagerInternal");
            userManagerInternal = null;
        }
        if (!userManagerInternal.exists(i)) {
            return -1;
        }
        PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        PackageManagerLocal.FilteredSnapshot withFilteredSnapshot = withFilteredSnapshot(packageManagerLocal, Binder.getCallingUid(), i);
        try {
            PackageState packageState = withFilteredSnapshot.getPackageState(str);
            AutoCloseable.closeFinally(withFilteredSnapshot, null);
            if (packageState == null) {
                return -1;
            }
            AccessState accessState2 = this.service.state;
            if (accessState2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("state");
            } else {
                accessState = accessState2;
            }
            return isPermissionGranted(new GetStateScope(accessState), packageState, i, str2) ? 0 : -1;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                AutoCloseable.closeFinally(withFilteredSnapshot, th);
                throw th2;
            }
        }
    }

    private final boolean isPermissionGranted(GetStateScope getStateScope, PackageState packageState, int i, String str) {
        int appId = packageState.getAppId();
        boolean isInstantApp = packageState.getUserStateOrDefault(i).isInstantApp();
        if (isSinglePermissionGranted(getStateScope, appId, i, isInstantApp, str)) {
            return true;
        }
        String str2 = FULLER_PERMISSIONS.get(str);
        return str2 != null && isSinglePermissionGranted(getStateScope, appId, i, isInstantApp, str2);
    }

    private final boolean isSinglePermissionGranted(GetStateScope getStateScope, int i, int i2, boolean z, String str) {
        if (!PermissionFlags.INSTANCE.isPermissionGranted(this.policy.getPermissionFlags(getStateScope, i, i2, str))) {
            return false;
        }
        if (!z) {
            return true;
        }
        Permission permission = this.policy.getPermissions(getStateScope).get(str);
        return permission != null && IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), 4096);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    @NotNull
    public Set<String> getGrantedPermissions(@NotNull String str, int i) {
        Set<String> emptySet;
        Set<String> emptySet2;
        if (str == null) {
            throw new IllegalArgumentException("packageName cannot be null".toString());
        }
        Preconditions.checkArgumentNonnegative(i, "userId");
        PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        PackageManagerLocal.UnfilteredSnapshot withUnfilteredSnapshot = packageManagerLocal.withUnfilteredSnapshot();
        try {
            PackageState packageState = getPackageState(withUnfilteredSnapshot, str);
            AutoCloseable.closeFinally(withUnfilteredSnapshot, null);
            if (packageState != null) {
                AccessState accessState = this.service.state;
                if (accessState == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("state");
                    accessState = null;
                }
                GetStateScope getStateScope = new GetStateScope(accessState);
                ArrayMap<String, Integer> uidPermissionFlags = this.policy.getUidPermissionFlags(getStateScope, packageState.getAppId(), i);
                if (uidPermissionFlags != null) {
                    ArraySet arraySet = new ArraySet();
                    int size = uidPermissionFlags.size();
                    for (int i2 = 0; i2 < size; i2++) {
                        String keyAt = uidPermissionFlags.keyAt(i2);
                        uidPermissionFlags.valueAt(i2).intValue();
                        String str2 = keyAt;
                        if (!isPermissionGranted(getStateScope, packageState, i, str2)) {
                            str2 = null;
                        }
                        if (str2 != null) {
                            arraySet.add(str2);
                        }
                    }
                    return arraySet;
                }
                emptySet = SetsKt__SetsKt.emptySet();
                return emptySet;
            }
            Log.w(LOG_TAG, "getGrantedPermissions: Unknown package " + str);
            emptySet2 = SetsKt__SetsKt.emptySet();
            return emptySet2;
        } finally {
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    @NotNull
    public int[] getGidsForUid(int i) {
        Permission permission;
        int appId = UserHandle.getAppId(i);
        int userId = UserHandle.getUserId(i);
        SystemConfig systemConfig = this.systemConfig;
        AccessState accessState = null;
        if (systemConfig == null) {
            Intrinsics.throwUninitializedPropertyAccessException("systemConfig");
            systemConfig = null;
        }
        int[] globalGids = systemConfig.getGlobalGids();
        AccessState accessState2 = this.service.state;
        if (accessState2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("state");
        } else {
            accessState = accessState2;
        }
        GetStateScope getStateScope = new GetStateScope(accessState);
        ArrayMap<String, Integer> uidPermissionFlags = this.policy.getUidPermissionFlags(getStateScope, appId, userId);
        if (uidPermissionFlags == null) {
            int[] copyOf = Arrays.copyOf(globalGids, globalGids.length);
            Intrinsics.checkNotNullExpressionValue(copyOf, "copyOf(this, size)");
            return copyOf;
        }
        IntArray wrap = IntArray.wrap(globalGids);
        int size = uidPermissionFlags.size();
        for (int i2 = 0; i2 < size; i2++) {
            String keyAt = uidPermissionFlags.keyAt(i2);
            if (PermissionFlags.INSTANCE.isPermissionGranted(uidPermissionFlags.valueAt(i2).intValue()) && (permission = this.policy.getPermissions(getStateScope).get(keyAt)) != null) {
                int[] gidsForUser = permission.getGidsForUser(userId);
                if (!(gidsForUser.length == 0)) {
                    wrap.addAll(gidsForUser);
                }
            }
        }
        return wrap.toArray();
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void grantRuntimePermission(@NotNull String str, @NotNull String str2, int i) {
        setRuntimePermissionGranted$default(this, str, i, str2, true, false, null, 48, null);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void revokeRuntimePermission(@NotNull String str, @NotNull String str2, int i, @Nullable String str3) {
        setRuntimePermissionGranted$default(this, str, i, str2, false, false, str3, 16, null);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void revokePostNotificationPermissionWithoutKillForTest(@NotNull String str, int i) {
        setRuntimePermissionGranted$default(this, str, i, "android.permission.POST_NOTIFICATIONS", false, true, null, 32, null);
    }

    static /* synthetic */ void setRuntimePermissionGranted$default(PermissionService permissionService, String str, int i, String str2, boolean z, boolean z2, String str3, int i2, Object obj) {
        if ((i2 & 16) != 0) {
            z2 = false;
        }
        boolean z3 = z2;
        if ((i2 & 32) != 0) {
            str3 = null;
        }
        permissionService.setRuntimePermissionGranted(str, i, str2, z, z3, str3);
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x00da  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00e2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r13v0, types: [com.android.server.pm.pkg.PackageState, T] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void setRuntimePermissionGranted(String str, int i, String str2, boolean z, boolean z2, String str3) {
        Object first;
        boolean z3;
        AccessCheckingService accessCheckingService;
        String str4 = z ? "grantRuntimePermission" : "revokeRuntimePermission";
        int callingUid = Binder.getCallingUid();
        UserManagerInternal userManagerInternal = this.userManagerInternal;
        OnPermissionFlagsChangedListener onPermissionFlagsChangedListener = null;
        if (userManagerInternal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("userManagerInternal");
            userManagerInternal = null;
        }
        if (!userManagerInternal.exists(i)) {
            Log.w(LOG_TAG, str4 + ": Unknown user " + i);
            return;
        }
        enforceCallingOrSelfCrossUserPermission(i, true, true, str4);
        this.context.enforceCallingOrSelfPermission(z ? "android.permission.GRANT_RUNTIME_PERMISSIONS" : "android.permission.REVOKE_RUNTIME_PERMISSIONS", str4);
        Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
        PackageManagerInternal packageManagerInternal = this.packageManagerInternal;
        if (packageManagerInternal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
            packageManagerInternal = null;
        }
        first = _Arrays.first(packageManagerInternal.getKnownPackageNames(7, 0));
        String str5 = (String) first;
        PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        PackageManagerLocal.UnfilteredSnapshot withUnfilteredSnapshot = packageManagerLocal.withUnfilteredSnapshot();
        try {
            PackageManagerLocal.FilteredSnapshot filtered = filtered(withUnfilteredSnapshot, callingUid, i);
            try {
                ?? packageState = filtered.getPackageState(str);
                AutoCloseable.closeFinally(filtered, null);
                ref$ObjectRef.element = packageState;
                PackageState packageState2 = getPackageState(withUnfilteredSnapshot, str5);
                Unit unit = Unit.INSTANCE;
                AutoCloseable.closeFinally(withUnfilteredSnapshot, null);
                PackageState packageState3 = (PackageState) ref$ObjectRef.element;
                if ((packageState3 != null ? packageState3.getAndroidPackage() : null) == null) {
                    Log.w(LOG_TAG, str4 + ": Unknown package " + str);
                    return;
                }
                if (!isRootOrSystem(callingUid)) {
                    int appId = UserHandle.getAppId(callingUid);
                    Intrinsics.checkNotNull(packageState2);
                    if (appId != packageState2.getAppId()) {
                        z3 = false;
                        boolean z4 = this.context.checkCallingOrSelfPermission("android.permission.ADJUST_RUNTIME_PERMISSIONS_POLICY") == 0;
                        accessCheckingService = this.service;
                        synchronized (accessCheckingService.stateLock) {
                            AccessState accessState = accessCheckingService.state;
                            if (accessState == null) {
                                Intrinsics.throwUninitializedPropertyAccessException("state");
                                accessState = null;
                            }
                            AccessState copy = accessState.copy();
                            MutateStateScope mutateStateScope = new MutateStateScope(accessState, copy);
                            OnPermissionFlagsChangedListener onPermissionFlagsChangedListener2 = this.onPermissionFlagsChangedListener;
                            if (onPermissionFlagsChangedListener2 == null) {
                                Intrinsics.throwUninitializedPropertyAccessException("onPermissionFlagsChangedListener");
                            } else {
                                onPermissionFlagsChangedListener = onPermissionFlagsChangedListener2;
                            }
                            if (z2) {
                                onPermissionFlagsChangedListener.skipKillRuntimePermissionRevokedUids(mutateStateScope);
                            }
                            if (str3 != null) {
                                onPermissionFlagsChangedListener.addKillRuntimePermissionRevokedUidsReason(mutateStateScope, str3);
                            }
                            setRuntimePermissionGranted(mutateStateScope, (PackageState) ref$ObjectRef.element, i, str2, z, z3, z4, true, str4);
                            accessCheckingService.persistence.write(copy);
                            accessCheckingService.state = copy;
                            accessCheckingService.policy.onStateMutated(new GetStateScope(copy));
                        }
                        return;
                    }
                }
                z3 = true;
                if (this.context.checkCallingOrSelfPermission("android.permission.ADJUST_RUNTIME_PERMISSIONS_POLICY") == 0) {
                }
                accessCheckingService = this.service;
                synchronized (accessCheckingService.stateLock) {
                }
            } finally {
            }
        } finally {
        }
    }

    private final void setRequestedPermissionStates(PackageState packageState, int i, ArrayMap<String, Integer> arrayMap) {
        Permission permission;
        int i2;
        int i3;
        AccessCheckingService accessCheckingService = this.service;
        synchronized (accessCheckingService.stateLock) {
            AccessState accessState = accessCheckingService.state;
            if (accessState == null) {
                Intrinsics.throwUninitializedPropertyAccessException("state");
                accessState = null;
            }
            AccessState copy = accessState.copy();
            MutateStateScope mutateStateScope = new MutateStateScope(accessState, copy);
            int size = arrayMap.size();
            int i4 = 0;
            while (i4 < size) {
                String keyAt = arrayMap.keyAt(i4);
                int intValue = arrayMap.valueAt(i4).intValue();
                String str = keyAt;
                if (intValue != 1 && intValue != 2) {
                    Log.w(LOG_TAG, "setRequestedPermissionStates: Unknown permission state " + intValue + " for permission " + str);
                } else {
                    AndroidPackage androidPackage = packageState.getAndroidPackage();
                    Intrinsics.checkNotNull(androidPackage);
                    if (androidPackage.getRequestedPermissions().contains(str) && (permission = this.policy.getPermissions(mutateStateScope).get(str)) != null) {
                        if (!IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), 32)) {
                            if (!(permission.getPermissionInfo().getProtection() == 1)) {
                                if (IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), 64) && PackageInstallerService.INSTALLER_CHANGEABLE_APP_OP_PERMISSIONS.contains(str)) {
                                    setAppOpPermissionGranted(mutateStateScope, packageState, i, str, intValue == 1);
                                }
                            }
                        }
                        if (intValue == 1) {
                            i2 = i4;
                            i3 = size;
                            setRuntimePermissionGranted(mutateStateScope, packageState, i, str, true, false, false, false, "setRequestedPermissionStates");
                            i4 = i2 + 1;
                            size = i3;
                        }
                    }
                }
                i2 = i4;
                i3 = size;
                i4 = i2 + 1;
                size = i3;
            }
            accessCheckingService.persistence.write(copy);
            accessCheckingService.state = copy;
            accessCheckingService.policy.onStateMutated(new GetStateScope(copy));
            Unit unit = Unit.INSTANCE;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setRuntimePermissionGranted(MutateStateScope mutateStateScope, PackageState packageState, int i, String str, boolean z, boolean z2, boolean z3, boolean z4, String str2) {
        Permission permission = this.policy.getPermissions(mutateStateScope).get(str);
        if (permission == null) {
            if (z4) {
                throw new IllegalArgumentException("Unknown permission " + str);
            }
            return;
        }
        AndroidPackage androidPackage = packageState.getAndroidPackage();
        Intrinsics.checkNotNull(androidPackage);
        String packageName = packageState.getPackageName();
        if (!IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), 32)) {
            if (!IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), 67108864)) {
                if (!(permission.getPermissionInfo().getProtection() == 1)) {
                    if (z4) {
                        throw new SecurityException("Permission " + str + " requested by package " + packageName + " is not a changeable permission type");
                    }
                    return;
                }
                if (androidPackage.getTargetSdkVersion() < 23) {
                    return;
                }
                if (z && packageState.getUserStateOrDefault(i).isInstantApp() && !IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), 4096)) {
                    if (z4) {
                        throw new SecurityException("Cannot grant non-instant permission " + str + " to package " + packageName);
                    }
                    return;
                }
            } else if (!z2) {
                if (z4) {
                    throw new SecurityException("Permission " + str + " is managed by role");
                }
                return;
            }
        }
        int appId = packageState.getAppId();
        int permissionFlags = this.policy.getPermissionFlags(mutateStateScope, appId, i, str);
        if (!androidPackage.getRequestedPermissions().contains(str) && permissionFlags == 0) {
            if (z4) {
                throw new SecurityException("Permission " + str + " isn't requested by package " + packageName);
            }
            return;
        }
        if (IntExtensions.hasBits(permissionFlags, 256)) {
            if (z4) {
                Log.e(LOG_TAG, str2 + ": Cannot change system fixed permission " + str + " for package " + packageName);
                return;
            }
            return;
        }
        if (IntExtensions.hasBits(permissionFlags, 128) && !z3) {
            if (z4) {
                Log.e(LOG_TAG, str2 + ": Cannot change policy fixed permission " + str + " for package " + packageName);
                return;
            }
            return;
        }
        if (z && IntExtensions.hasBits(permissionFlags, DumpState.DUMP_DOMAIN_PREFERRED)) {
            if (z4) {
                Log.e(LOG_TAG, str2 + ": Cannot grant hard-restricted non-exempt permission " + str + " to package " + packageName);
                return;
            }
            return;
        }
        if (z && IntExtensions.hasBits(permissionFlags, 524288) && !SoftRestrictedPermissionPolicy.forPermission(this.context, AndroidPackageUtils.generateAppInfoWithoutState(androidPackage), androidPackage, UserHandle.of(i), str).mayGrantPermission()) {
            if (z4) {
                Log.e(LOG_TAG, str2 + ": Cannot grant soft-restricted non-exempt permission " + str + " to package " + packageName);
                return;
            }
            return;
        }
        int updateRuntimePermissionGranted = PermissionFlags.INSTANCE.updateRuntimePermissionGranted(permissionFlags, z);
        if (permissionFlags == updateRuntimePermissionGranted) {
            return;
        }
        this.policy.setPermissionFlags(mutateStateScope, appId, i, str, updateRuntimePermissionGranted);
        if (permission.getPermissionInfo().getProtection() == 1) {
            LogMaker logMaker = new LogMaker(z ? 1243 : 1245);
            logMaker.setPackageName(packageName);
            logMaker.addTaggedData(1241, str);
            MetricsLogger metricsLogger = this.metricsLogger;
            if (metricsLogger == null) {
                Intrinsics.throwUninitializedPropertyAccessException("metricsLogger");
                metricsLogger = null;
            }
            metricsLogger.write(logMaker);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setAppOpPermissionGranted(MutateStateScope mutateStateScope, PackageState packageState, int i, String str, boolean z) {
        SchemePolicy schemePolicy$frameworks__base__services__permission__android_common__services_permission = this.service.getSchemePolicy$frameworks__base__services__permission__android_common__services_permission("uid", "app-op");
        Intrinsics.checkNotNull(schemePolicy$frameworks__base__services__permission__android_common__services_permission, "null cannot be cast to non-null type com.android.server.permission.access.appop.UidAppOpPolicy");
        ((UidAppOpPolicy) schemePolicy$frameworks__base__services__permission__android_common__services_permission).setAppOpMode(mutateStateScope, packageState.getAppId(), i, AppOpsManager.permissionToOp(str), z ? 0 : 2);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int getPermissionFlags(@NotNull String str, @NotNull String str2, int i) {
        UserManagerInternal userManagerInternal = this.userManagerInternal;
        AccessState accessState = null;
        if (userManagerInternal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("userManagerInternal");
            userManagerInternal = null;
        }
        if (!userManagerInternal.exists(i)) {
            Log.w(LOG_TAG, "getPermissionFlags: Unknown user " + i);
            return 0;
        }
        enforceCallingOrSelfCrossUserPermission(i, true, false, "getPermissionFlags");
        enforceCallingOrSelfAnyPermission("getPermissionFlags", "android.permission.GRANT_RUNTIME_PERMISSIONS", "android.permission.REVOKE_RUNTIME_PERMISSIONS", "android.permission.GET_RUNTIME_PERMISSIONS");
        PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        PackageManagerLocal.FilteredSnapshot withFilteredSnapshot = packageManagerLocal.withFilteredSnapshot();
        try {
            PackageState packageState = withFilteredSnapshot.getPackageState(str);
            AutoCloseable.closeFinally(withFilteredSnapshot, null);
            if (packageState != null) {
                AccessState accessState2 = this.service.state;
                if (accessState2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("state");
                } else {
                    accessState = accessState2;
                }
                GetStateScope getStateScope = new GetStateScope(accessState);
                if (this.policy.getPermissions(getStateScope).get(str2) == null) {
                    Log.w(LOG_TAG, "getPermissionFlags: Unknown permission " + str2);
                    return 0;
                }
                return PermissionFlags.INSTANCE.toApiFlags(this.policy.getPermissionFlags(getStateScope, packageState.getAppId(), i, str2));
            }
            Log.w(LOG_TAG, "getPermissionFlags: Unknown package " + str);
            return 0;
        } finally {
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean isPermissionRevokedByPolicy(@NotNull String str, @NotNull String str2, int i) {
        UserManagerInternal userManagerInternal = this.userManagerInternal;
        AccessState accessState = null;
        if (userManagerInternal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("userManagerInternal");
            userManagerInternal = null;
        }
        if (!userManagerInternal.exists(i)) {
            Log.w(LOG_TAG, "isPermissionRevokedByPolicy: Unknown user " + i);
            return false;
        }
        enforceCallingOrSelfCrossUserPermission(i, true, false, "isPermissionRevokedByPolicy");
        PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        PackageManagerLocal.FilteredSnapshot withFilteredSnapshot = withFilteredSnapshot(packageManagerLocal, Binder.getCallingUid(), i);
        try {
            PackageState packageState = withFilteredSnapshot.getPackageState(str);
            AutoCloseable.closeFinally(withFilteredSnapshot, null);
            if (packageState == null) {
                return false;
            }
            AccessState accessState2 = this.service.state;
            if (accessState2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("state");
            } else {
                accessState = accessState2;
            }
            GetStateScope getStateScope = new GetStateScope(accessState);
            if (isPermissionGranted(getStateScope, packageState, i, str2)) {
                return false;
            }
            return IntExtensions.hasBits(this.policy.getPermissionFlags(getStateScope, packageState.getAppId(), i, str2), 128);
        } finally {
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean isPermissionsReviewRequired(@NotNull String str, int i) {
        if (str == null) {
            throw new IllegalArgumentException("packageName cannot be null".toString());
        }
        PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        AccessState accessState = null;
        if (packageManagerLocal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        PackageManagerLocal.UnfilteredSnapshot withUnfilteredSnapshot = packageManagerLocal.withUnfilteredSnapshot();
        try {
            PackageState packageState = getPackageState(withUnfilteredSnapshot, str);
            AutoCloseable.closeFinally(withUnfilteredSnapshot, null);
            if (packageState == null) {
                return false;
            }
            AccessState accessState2 = this.service.state;
            if (accessState2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("state");
            } else {
                accessState = accessState2;
            }
            ArrayMap<String, Integer> uidPermissionFlags = this.policy.getUidPermissionFlags(new GetStateScope(accessState), packageState.getAppId(), i);
            if (uidPermissionFlags == null) {
                return false;
            }
            int size = uidPermissionFlags.size();
            for (int i2 = 0; i2 < size; i2++) {
                uidPermissionFlags.keyAt(i2);
                if (IntExtensions.hasBits(uidPermissionFlags.valueAt(i2).intValue(), 5120)) {
                    return true;
                }
            }
            return false;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                AutoCloseable.closeFinally(withUnfilteredSnapshot, th);
                throw th2;
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean shouldShowRequestPermissionRationale(@NotNull String str, @NotNull String str2, int i) {
        int appId;
        UserManagerInternal userManagerInternal = this.userManagerInternal;
        IPlatformCompat iPlatformCompat = null;
        if (userManagerInternal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("userManagerInternal");
            userManagerInternal = null;
        }
        boolean z = false;
        if (!userManagerInternal.exists(i)) {
            Log.w(LOG_TAG, "shouldShowRequestPermissionRationale: Unknown user " + i);
            return false;
        }
        enforceCallingOrSelfCrossUserPermission(i, true, false, "shouldShowRequestPermissionRationale");
        int callingUid = Binder.getCallingUid();
        PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        PackageManagerLocal.FilteredSnapshot withFilteredSnapshot = withFilteredSnapshot(packageManagerLocal, callingUid, i);
        try {
            PackageState packageState = withFilteredSnapshot.getPackageState(str);
            AutoCloseable.closeFinally(withFilteredSnapshot, null);
            if (packageState == null || UserHandle.getAppId(callingUid) != (appId = packageState.getAppId())) {
                return false;
            }
            Ref$IntRef ref$IntRef = new Ref$IntRef();
            AccessState accessState = this.service.state;
            if (accessState == null) {
                Intrinsics.throwUninitializedPropertyAccessException("state");
                accessState = null;
            }
            GetStateScope getStateScope = new GetStateScope(accessState);
            if (isPermissionGranted(getStateScope, packageState, i, str2)) {
                return false;
            }
            int intValue = Integer.valueOf(this.policy.getPermissionFlags(getStateScope, appId, i, str2)).intValue();
            ref$IntRef.element = intValue;
            if (IntExtensions.hasAnyBit(intValue, 262592)) {
                return false;
            }
            if (Intrinsics.areEqual(str2, "android.permission.ACCESS_BACKGROUND_LOCATION")) {
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    try {
                        IPlatformCompat iPlatformCompat2 = this.platformCompat;
                        if (iPlatformCompat2 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("platformCompat");
                        } else {
                            iPlatformCompat = iPlatformCompat2;
                        }
                        z = iPlatformCompat.isChangeEnabledByPackageName(BACKGROUND_RATIONALE_CHANGE_ID, str, i);
                    } catch (RemoteException e) {
                        Log.e(LOG_TAG, "shouldShowRequestPermissionRationale: Unable to check if compatibility change is enabled", e);
                    }
                    if (z) {
                        return true;
                    }
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
            return IntExtensions.hasBits(ref$IntRef.element, 32);
        } finally {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:42:0x010b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updatePermissionFlags(@NotNull String str, @NotNull String str2, int i, int i2, boolean z, int i3) {
        boolean z2;
        AccessCheckingService accessCheckingService;
        int callingUid = Binder.getCallingUid();
        UserManagerInternal userManagerInternal = this.userManagerInternal;
        AccessState accessState = null;
        if (userManagerInternal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("userManagerInternal");
            userManagerInternal = null;
        }
        if (!userManagerInternal.exists(i3)) {
            Log.w(LOG_TAG, "updatePermissionFlags: Unknown user " + i3);
            return;
        }
        enforceCallingOrSelfCrossUserPermission(i3, true, true, "updatePermissionFlags");
        enforceCallingOrSelfAnyPermission("updatePermissionFlags", "android.permission.GRANT_RUNTIME_PERMISSIONS", "android.permission.REVOKE_RUNTIME_PERMISSIONS");
        if (!isRootOrSystem(callingUid) && IntExtensions.hasBits(i, 4)) {
            if (z) {
                this.context.enforceCallingOrSelfPermission("android.permission.ADJUST_RUNTIME_PERMISSIONS_POLICY", "Need android.permission.ADJUST_RUNTIME_PERMISSIONS_POLICY to change policy flags");
            } else {
                PackageManagerInternal packageManagerInternal = this.packageManagerInternal;
                if (packageManagerInternal == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
                    packageManagerInternal = null;
                }
                if (!(packageManagerInternal.getUidTargetSdkVersion(callingUid) < 29)) {
                    throw new IllegalArgumentException("android.permission.ADJUST_RUNTIME_PERMISSIONS_POLICY needs to be checked for packages targeting 29 or later when changing policy flags".toString());
                }
            }
        }
        PackageManagerInternal packageManagerInternal2 = this.packageManagerInternal;
        if (packageManagerInternal2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
            packageManagerInternal2 = null;
        }
        PackageStateInternal packageStateInternal = packageManagerInternal2.getPackageStateInternal(str);
        AndroidPackage androidPackage = packageStateInternal != null ? packageStateInternal.getAndroidPackage() : null;
        if (androidPackage != null) {
            PackageManagerInternal packageManagerInternal3 = this.packageManagerInternal;
            if (packageManagerInternal3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
                packageManagerInternal3 = null;
            }
            if (!packageManagerInternal3.filterAppAccess(str, callingUid, i3, false)) {
                if (!androidPackage.getRequestedPermissions().contains(str2)) {
                    PackageManagerInternal packageManagerInternal4 = this.packageManagerInternal;
                    if (packageManagerInternal4 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
                        packageManagerInternal4 = null;
                    }
                    for (String str3 : packageManagerInternal4.getSharedUserPackagesForPackage(str, i3)) {
                        PackageManagerInternal packageManagerInternal5 = this.packageManagerInternal;
                        if (packageManagerInternal5 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
                            packageManagerInternal5 = null;
                        }
                        AndroidPackage androidPackage2 = packageManagerInternal5.getPackage(str3);
                        if (!(androidPackage2 != null && androidPackage2.getRequestedPermissions().contains(str2))) {
                        }
                    }
                    z2 = false;
                    int appId = packageStateInternal.getAppId();
                    accessCheckingService = this.service;
                    synchronized (accessCheckingService.stateLock) {
                        AccessState accessState2 = accessCheckingService.state;
                        if (accessState2 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("state");
                        } else {
                            accessState = accessState2;
                        }
                        AccessState copy = accessState.copy();
                        updatePermissionFlags(new MutateStateScope(accessState, copy), appId, i3, str2, i, i2, true, z2, "updatePermissionFlags", str);
                        accessCheckingService.persistence.write(copy);
                        accessCheckingService.state = copy;
                        accessCheckingService.policy.onStateMutated(new GetStateScope(copy));
                        Unit unit = Unit.INSTANCE;
                    }
                    return;
                }
                z2 = true;
                int appId2 = packageStateInternal.getAppId();
                accessCheckingService = this.service;
                synchronized (accessCheckingService.stateLock) {
                }
            }
        }
        Log.w(LOG_TAG, "updatePermissionFlags: Unknown package " + str);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void updatePermissionFlagsForAllApps(int i, int i2, int i3) {
        Binder.getCallingUid();
        UserManagerInternal userManagerInternal = this.userManagerInternal;
        AccessState accessState = null;
        if (userManagerInternal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("userManagerInternal");
            userManagerInternal = null;
        }
        if (!userManagerInternal.exists(i3)) {
            Log.w(LOG_TAG, "updatePermissionFlagsForAllApps: Unknown user " + i3);
            return;
        }
        enforceCallingOrSelfCrossUserPermission(i3, true, true, "updatePermissionFlagsForAllApps");
        enforceCallingOrSelfAnyPermission("updatePermissionFlagsForAllApps", "android.permission.GRANT_RUNTIME_PERMISSIONS", "android.permission.REVOKE_RUNTIME_PERMISSIONS");
        PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        PackageManagerLocal.UnfilteredSnapshot withUnfilteredSnapshot = packageManagerLocal.withUnfilteredSnapshot();
        try {
            Map<String, PackageState> packageStates = withUnfilteredSnapshot.getPackageStates();
            AutoCloseable.closeFinally(withUnfilteredSnapshot, null);
            AccessCheckingService accessCheckingService = this.service;
            synchronized (accessCheckingService.stateLock) {
                AccessState accessState2 = accessCheckingService.state;
                if (accessState2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("state");
                } else {
                    accessState = accessState2;
                }
                AccessState copy = accessState.copy();
                MutateStateScope mutateStateScope = new MutateStateScope(accessState, copy);
                for (Map.Entry<String, PackageState> entry : packageStates.entrySet()) {
                    String key = entry.getKey();
                    PackageState value = entry.getValue();
                    AndroidPackage androidPackage = value.getAndroidPackage();
                    if (androidPackage != null) {
                        Iterator<T> it = androidPackage.getRequestedPermissions().iterator();
                        while (it.hasNext()) {
                            updatePermissionFlags(mutateStateScope, value.getAppId(), i3, (String) it.next(), i, i2, false, true, "updatePermissionFlagsForAllApps", key);
                            mutateStateScope = mutateStateScope;
                        }
                    }
                    mutateStateScope = mutateStateScope;
                }
                accessCheckingService.persistence.write(copy);
                accessCheckingService.state = copy;
                accessCheckingService.policy.onStateMutated(new GetStateScope(copy));
                Unit unit = Unit.INSTANCE;
            }
        } finally {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updatePermissionFlags(MutateStateScope mutateStateScope, int i, int i2, String str, int i3, int i4, boolean z, boolean z2, String str2, String str3) {
        int i5;
        int i6;
        int callingUid = Binder.getCallingUid();
        if (isRootOrSystem(callingUid)) {
            i5 = i3;
            i6 = i4;
        } else {
            int i7 = ((isShell(callingUid) || NOTIFICATIONS_PERMISSIONS.contains(str)) ? 0 : 64) | 48 | 4096 | 2048 | 8192 | 16384;
            i5 = IntExtensions.andInv(i3, i7);
            i6 = IntExtensions.andInv(i4, i7);
        }
        Permission permission = this.policy.getPermissions(mutateStateScope).get(str);
        if (permission == null) {
            if (z) {
                throw new IllegalArgumentException("Unknown permission " + str);
            }
            return;
        }
        int permissionFlags = this.policy.getPermissionFlags(mutateStateScope, i, i2, str);
        if (!z2 && permissionFlags == 0) {
            Log.w(LOG_TAG, str2 + ": Permission " + str + " isn't requested by package " + str3);
            return;
        }
        this.policy.setPermissionFlags(mutateStateScope, i, i2, str, PermissionFlags.INSTANCE.updateFlags(permission, permissionFlags, i5, i6));
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    @Nullable
    public ArrayList<String> getAllowlistedRestrictedPermissions(@NotNull String str, int i, int i2) {
        AndroidPackage androidPackage;
        if (str == null) {
            throw new IllegalArgumentException("packageName cannot be null".toString());
        }
        Preconditions.checkFlagsArgument(i, 7);
        Preconditions.checkArgumentNonnegative(i2, "userId cannot be null");
        UserManagerInternal userManagerInternal = this.userManagerInternal;
        PackageManagerInternal packageManagerInternal = null;
        if (userManagerInternal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("userManagerInternal");
            userManagerInternal = null;
        }
        if (!userManagerInternal.exists(i2)) {
            Log.w(LOG_TAG, "AllowlistedRestrictedPermission api: Unknown user " + i2);
            return null;
        }
        enforceCallingOrSelfCrossUserPermission(i2, false, false, "getAllowlistedRestrictedPermissions");
        int callingUid = Binder.getCallingUid();
        PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        PackageManagerLocal.FilteredSnapshot withFilteredSnapshot = withFilteredSnapshot(packageManagerLocal, callingUid, i2);
        try {
            PackageState packageState = withFilteredSnapshot.getPackageState(str);
            AutoCloseable.closeFinally(withFilteredSnapshot, null);
            if (packageState == null || (androidPackage = packageState.getAndroidPackage()) == null) {
                return null;
            }
            boolean z = this.context.checkCallingOrSelfPermission("android.permission.WHITELIST_RESTRICTED_PERMISSIONS") == 0;
            if (IntExtensions.hasBits(i, 1) && !z) {
                throw new SecurityException("Querying system allowlist requires android.permission.WHITELIST_RESTRICTED_PERMISSIONS");
            }
            PackageManagerInternal packageManagerInternal2 = this.packageManagerInternal;
            if (packageManagerInternal2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
            } else {
                packageManagerInternal = packageManagerInternal2;
            }
            boolean isCallerInstallerOfRecord = packageManagerInternal.isCallerInstallerOfRecord(androidPackage, callingUid);
            if (IntExtensions.hasAnyBit(i, 6) && !z && !isCallerInstallerOfRecord) {
                throw new SecurityException("Querying upgrade or installer allowlist requires being installer on record or android.permission.WHITELIST_RESTRICTED_PERMISSIONS");
            }
            return getAllowlistedRestrictedPermissionsUnchecked(packageState.getAppId(), i, i2);
        } finally {
        }
    }

    private final ArrayList<String> getAllowlistedRestrictedPermissionsUnchecked(int i, int i2, int i3) {
        AccessState accessState = this.service.state;
        if (accessState == null) {
            Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        ArrayMap<String, Integer> uidPermissionFlags = this.policy.getUidPermissionFlags(new GetStateScope(accessState), i, i3);
        if (uidPermissionFlags == null) {
            return null;
        }
        int i4 = IntExtensions.hasBits(i2, 1) ? 65536 : 0;
        if (IntExtensions.hasBits(i2, 4)) {
            i4 |= 131072;
        }
        if (IntExtensions.hasBits(i2, 2)) {
            i4 |= 32768;
        }
        ArrayList<String> arrayList = new ArrayList<>();
        int size = uidPermissionFlags.size();
        for (int i5 = 0; i5 < size; i5++) {
            String keyAt = uidPermissionFlags.keyAt(i5);
            if (!IntExtensions.hasAnyBit(uidPermissionFlags.valueAt(i5).intValue(), i4)) {
                keyAt = null;
            }
            if (keyAt != null) {
                arrayList.add(keyAt);
            }
        }
        return arrayList;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean addAllowlistedRestrictedPermission(@NotNull String str, @NotNull String str2, int i, int i2) {
        if (str2 == null) {
            throw new IllegalArgumentException("permissionName cannot be null".toString());
        }
        if (!enforceRestrictedPermission(str2)) {
            return false;
        }
        ArrayList<String> allowlistedRestrictedPermissions = getAllowlistedRestrictedPermissions(str, i, i2);
        if (allowlistedRestrictedPermissions == null) {
            allowlistedRestrictedPermissions = new ArrayList<>(1);
        }
        ArrayList<String> arrayList = allowlistedRestrictedPermissions;
        if (arrayList.contains(str2)) {
            return false;
        }
        arrayList.add(str2);
        return setAllowlistedRestrictedPermissions(str, arrayList, i, i2, true);
    }

    private final void addAllowlistedRestrictedPermissionsUnchecked(AndroidPackage androidPackage, int i, List<String> list, int i2) {
        List<String> list2;
        List<String> list3;
        ArrayList<String> allowlistedRestrictedPermissionsUnchecked = getAllowlistedRestrictedPermissionsUnchecked(i, 2, i2);
        if (allowlistedRestrictedPermissionsUnchecked != null) {
            ArraySet arraySet = new ArraySet(list);
            MutableCollections.addAll(arraySet, allowlistedRestrictedPermissionsUnchecked);
            list3 = _Collections.toList(arraySet);
            if (list3 != null) {
                list2 = list3;
                setAllowlistedRestrictedPermissionsUnchecked(androidPackage, i, list2, 2, i2);
            }
        }
        list2 = list;
        setAllowlistedRestrictedPermissionsUnchecked(androidPackage, i, list2, 2, i2);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean removeAllowlistedRestrictedPermission(@NotNull String str, @NotNull String str2, int i, int i2) {
        ArrayList<String> allowlistedRestrictedPermissions;
        if (str2 == null) {
            throw new IllegalArgumentException("permissionName cannot be null".toString());
        }
        if (enforceRestrictedPermission(str2) && (allowlistedRestrictedPermissions = getAllowlistedRestrictedPermissions(str, i, i2)) != null && allowlistedRestrictedPermissions.remove(str2)) {
            return setAllowlistedRestrictedPermissions(str, allowlistedRestrictedPermissions, i, i2, false);
        }
        return false;
    }

    private final boolean enforceRestrictedPermission(String str) {
        AccessState accessState = this.service.state;
        if (accessState == null) {
            Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        Permission permission = this.policy.getPermissions(new GetStateScope(accessState)).get(str);
        boolean z = false;
        if (permission == null) {
            Log.w(LOG_TAG, "permission definition for " + str + " does not exist");
            return false;
        }
        PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        PackageManagerLocal.FilteredSnapshot withFilteredSnapshot = packageManagerLocal.withFilteredSnapshot();
        try {
            PackageState packageState = withFilteredSnapshot.getPackageState(permission.getPermissionInfo().packageName);
            AutoCloseable.closeFinally(withFilteredSnapshot, null);
            if (packageState == null) {
                return false;
            }
            if (IntExtensions.hasBits(permission.getPermissionInfo().flags, 12) && IntExtensions.hasBits(permission.getPermissionInfo().flags, 16)) {
                z = true;
            }
            if (!z || this.context.checkCallingOrSelfPermission("android.permission.WHITELIST_RESTRICTED_PERMISSIONS") == 0) {
                return true;
            }
            throw new SecurityException("Cannot modify allowlist of an immutably restricted permission: " + permission.getPermissionInfo().name);
        } finally {
        }
    }

    private final boolean setAllowlistedRestrictedPermissions(String str, List<String> list, int i, int i2, boolean z) {
        AndroidPackage androidPackage;
        Preconditions.checkArgument(Integer.bitCount(i) == 1);
        boolean z2 = this.context.checkCallingOrSelfPermission("android.permission.WHITELIST_RESTRICTED_PERMISSIONS") == 0;
        int callingUid = Binder.getCallingUid();
        PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        PackageManagerInternal packageManagerInternal = null;
        if (packageManagerLocal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        PackageManagerLocal.FilteredSnapshot withFilteredSnapshot = withFilteredSnapshot(packageManagerLocal, callingUid, i2);
        try {
            PackageState packageState = withFilteredSnapshot.getPackageStates().get(str);
            AutoCloseable.closeFinally(withFilteredSnapshot, null);
            if (packageState == null || (androidPackage = packageState.getAndroidPackage()) == null) {
                return false;
            }
            PackageManagerInternal packageManagerInternal2 = this.packageManagerInternal;
            if (packageManagerInternal2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
            } else {
                packageManagerInternal = packageManagerInternal2;
            }
            boolean isCallerInstallerOfRecord = packageManagerInternal.isCallerInstallerOfRecord(androidPackage, callingUid);
            if (IntExtensions.hasBits(i, 4)) {
                if (!z2 && !isCallerInstallerOfRecord) {
                    throw new SecurityException("Modifying upgrade allowlist requires being installer on record or android.permission.WHITELIST_RESTRICTED_PERMISSIONS");
                }
                if (z && !z2) {
                    throw new SecurityException("Adding to upgrade allowlist requiresandroid.permission.WHITELIST_RESTRICTED_PERMISSIONS");
                }
            }
            setAllowlistedRestrictedPermissionsUnchecked(androidPackage, packageState.getAppId(), list, i, i2);
            return true;
        } finally {
        }
    }

    private final void setAllowlistedRestrictedPermissionsUnchecked(AndroidPackage androidPackage, int i, List<String> list, int i2, int i3) {
        int i4;
        int i5;
        List<String> list2;
        ArrayMap<String, Permission> arrayMap;
        int i6;
        int i7;
        List<String> list3 = list;
        AccessCheckingService accessCheckingService = this.service;
        synchronized (accessCheckingService.stateLock) {
            AccessState accessState = accessCheckingService.state;
            if (accessState == null) {
                Intrinsics.throwUninitializedPropertyAccessException("state");
                accessState = null;
            }
            AccessState copy = accessState.copy();
            MutateStateScope mutateStateScope = new MutateStateScope(accessState, copy);
            UidPermissionPolicy uidPermissionPolicy = this.policy;
            ArrayMap<String, Integer> uidPermissionFlags = uidPermissionPolicy.getUidPermissionFlags(mutateStateScope, i, i3);
            if (uidPermissionFlags != null) {
                ArrayMap<String, Permission> permissions = uidPermissionPolicy.getPermissions(mutateStateScope);
                List<String> requestedPermissions = androidPackage.getRequestedPermissions();
                int size = requestedPermissions.size();
                int i8 = 0;
                while (i8 < size) {
                    String str = requestedPermissions.get(i8);
                    Permission permission = permissions.get(str);
                    if (permission != null && IntExtensions.hasBits(permission.getPermissionInfo().flags, 12)) {
                        Integer num = uidPermissionFlags.get(str);
                        if (num == null) {
                            num = 0;
                        }
                        int intValue = num.intValue();
                        boolean isPermissionGranted = PermissionFlags.INSTANCE.isPermissionGranted(intValue);
                        int i9 = i2;
                        int i10 = intValue;
                        int i11 = i8;
                        int i12 = 0;
                        while (i9 != 0) {
                            int i13 = size;
                            List<String> list4 = requestedPermissions;
                            int numberOfTrailingZeros = 1 << Integer.numberOfTrailingZeros(i9);
                            i9 &= ~numberOfTrailingZeros;
                            if (numberOfTrailingZeros == 1) {
                                i7 = 65536;
                                i12 |= 65536;
                                if (!list3.contains(str)) {
                                    i10 = IntExtensions.andInv(i10, 65536);
                                }
                            } else if (numberOfTrailingZeros == 2) {
                                i7 = 32768;
                                i12 |= 32768;
                                if (!list3.contains(str)) {
                                    i10 = IntExtensions.andInv(i10, 32768);
                                }
                            } else if (numberOfTrailingZeros == 4) {
                                i7 = 131072;
                                i12 |= 131072;
                                i10 = list3.contains(str) ? i10 | i7 : IntExtensions.andInv(i10, 131072);
                            }
                            size = i13;
                            requestedPermissions = list4;
                        }
                        int i14 = size;
                        List<String> list5 = requestedPermissions;
                        if (intValue == i10) {
                            i4 = i11;
                            i5 = i14;
                            list2 = list5;
                            arrayMap = permissions;
                            i8 = i4 + 1;
                            requestedPermissions = list2;
                            size = i5;
                            permissions = arrayMap;
                            list3 = list;
                        } else {
                            boolean hasAnyBit = IntExtensions.hasAnyBit(intValue, 229376);
                            boolean hasAnyBit2 = IntExtensions.hasAnyBit(i10, 229376);
                            if (IntExtensions.hasBits(intValue, 128) && !hasAnyBit2 && isPermissionGranted) {
                                i12 |= 128;
                                i10 = IntExtensions.andInv(i10, 128);
                            }
                            if (androidPackage.getTargetSdkVersion() >= 23 || hasAnyBit || !hasAnyBit2) {
                                i6 = i12;
                            } else {
                                i10 |= 4096;
                                i6 = i12 | 4096;
                            }
                            i4 = i11;
                            i5 = i14;
                            list2 = list5;
                            arrayMap = permissions;
                            uidPermissionPolicy.updatePermissionFlags(mutateStateScope, i, i3, str, i6, i10);
                            i8 = i4 + 1;
                            requestedPermissions = list2;
                            size = i5;
                            permissions = arrayMap;
                            list3 = list;
                        }
                    }
                    i4 = i8;
                    i5 = size;
                    list2 = requestedPermissions;
                    arrayMap = permissions;
                    i8 = i4 + 1;
                    requestedPermissions = list2;
                    size = i5;
                    permissions = arrayMap;
                    list3 = list;
                }
            }
            accessCheckingService.persistence.write(copy);
            accessCheckingService.state = copy;
            accessCheckingService.policy.onStateMutated(new GetStateScope(copy));
            Unit unit = Unit.INSTANCE;
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void addOnPermissionsChangeListener(@NotNull IOnPermissionsChangeListener iOnPermissionsChangeListener) {
        OnPermissionsChangeListeners onPermissionsChangeListeners = this.onPermissionsChangeListeners;
        if (onPermissionsChangeListeners == null) {
            Intrinsics.throwUninitializedPropertyAccessException("onPermissionsChangeListeners");
            onPermissionsChangeListeners = null;
        }
        onPermissionsChangeListeners.addListener(iOnPermissionsChangeListener);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void removeOnPermissionsChangeListener(@NotNull IOnPermissionsChangeListener iOnPermissionsChangeListener) {
        OnPermissionsChangeListeners onPermissionsChangeListeners = this.onPermissionsChangeListeners;
        if (onPermissionsChangeListeners == null) {
            Intrinsics.throwUninitializedPropertyAccessException("onPermissionsChangeListeners");
            onPermissionsChangeListeners = null;
        }
        onPermissionsChangeListeners.removeListener(iOnPermissionsChangeListener);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    @NotNull
    public List<SplitPermissionInfoParcelable> getSplitPermissions() {
        SystemConfig systemConfig = this.systemConfig;
        if (systemConfig == null) {
            Intrinsics.throwUninitializedPropertyAccessException("systemConfig");
            systemConfig = null;
        }
        return PermissionManager.splitPermissionInfoListToParcelableList(systemConfig.getSplitPermissions());
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    @NotNull
    public String[] getAppOpPermissionPackages(@NotNull String str) {
        if (str == null) {
            throw new IllegalArgumentException("permissionName cannot be null".toString());
        }
        ArraySet arraySet = new ArraySet();
        AccessState accessState = this.service.state;
        if (accessState == null) {
            Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        Permission permission = this.policy.getPermissions(new GetStateScope(accessState)).get(str);
        if (permission == null || !IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), 64)) {
            arraySet.toArray(new String[0]);
        }
        PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        PackageManagerLocal.UnfilteredSnapshot withUnfilteredSnapshot = packageManagerLocal.withUnfilteredSnapshot();
        try {
            Iterator<Map.Entry<String, PackageState>> it = withUnfilteredSnapshot.getPackageStates().entrySet().iterator();
            while (it.hasNext()) {
                AndroidPackage androidPackage = it.next().getValue().getAndroidPackage();
                if (androidPackage != null && androidPackage.getRequestedPermissions().contains(str)) {
                    arraySet.add(androidPackage.getPackageName());
                }
            }
            Unit unit = Unit.INSTANCE;
            AutoCloseable.closeFinally(withUnfilteredSnapshot, null);
            return (String[]) arraySet.toArray(new String[0]);
        } finally {
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    @NotNull
    public Map<String, Set<String>> getAllAppOpPermissionPackages() {
        ArrayMap arrayMap = new ArrayMap();
        AccessState accessState = this.service.state;
        if (accessState == null) {
            Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        ArrayMap<String, Permission> permissions = this.policy.getPermissions(new GetStateScope(accessState));
        PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        PackageManagerLocal.UnfilteredSnapshot withUnfilteredSnapshot = packageManagerLocal.withUnfilteredSnapshot();
        try {
            Iterator<Map.Entry<String, PackageState>> it = withUnfilteredSnapshot.getPackageStates().entrySet().iterator();
            while (it.hasNext()) {
                AndroidPackage androidPackage = it.next().getValue().getAndroidPackage();
                if (androidPackage != null) {
                    for (String str : androidPackage.getRequestedPermissions()) {
                        Permission permission = permissions.get(str);
                        if (permission != null && IntExtensions.hasBits(permission.getPermissionInfo().getProtectionFlags(), 64)) {
                            Object obj = arrayMap.get(str);
                            if (obj == null) {
                                obj = new ArraySet();
                                arrayMap.put(str, obj);
                            }
                            ((ArraySet) obj).add(androidPackage.getPackageName());
                        }
                    }
                }
            }
            Unit unit = Unit.INSTANCE;
            AutoCloseable.closeFinally(withUnfilteredSnapshot, null);
            return arrayMap;
        } finally {
        }
    }

    @Nullable
    public byte[] backupRuntimePermissions(int i) {
        Preconditions.checkArgumentNonnegative(i, "userId cannot be null");
        final CompletableFuture completableFuture = new CompletableFuture();
        PermissionControllerManager permissionControllerManager = this.permissionControllerManager;
        if (permissionControllerManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("permissionControllerManager");
            permissionControllerManager = null;
        }
        permissionControllerManager.getRuntimePermissionBackup(UserHandle.of(i), PermissionThread.getExecutor(), new Consumer() { // from class: com.android.server.permission.access.permission.PermissionService$backupRuntimePermissions$1
            @Override // java.util.function.Consumer
            public final void accept(byte[] bArr) {
                completableFuture.complete(bArr);
            }
        });
        try {
            return (byte[]) completableFuture.get(BACKUP_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            if (e instanceof TimeoutException ? true : e instanceof InterruptedException ? true : e instanceof ExecutionException) {
                Log.e(LOG_TAG, "Cannot create permission backup for user " + i, e);
                return null;
            }
            throw e;
        }
    }

    public void restoreRuntimePermissions(@NotNull byte[] bArr, int i) {
        if (bArr == null) {
            throw new IllegalArgumentException("backup".toString());
        }
        Preconditions.checkArgumentNonnegative(i, "userId");
        synchronized (this.isDelayedPermissionBackupFinished) {
            this.isDelayedPermissionBackupFinished.delete(i);
            Unit unit = Unit.INSTANCE;
        }
        PermissionControllerManager permissionControllerManager = this.permissionControllerManager;
        if (permissionControllerManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("permissionControllerManager");
            permissionControllerManager = null;
        }
        permissionControllerManager.stageAndApplyRuntimePermissionsBackup(bArr, UserHandle.of(i));
    }

    public void restoreDelayedRuntimePermissions(@NotNull String str, final int i) {
        if (str == null) {
            throw new IllegalArgumentException(DomainVerificationLegacySettings.ATTR_PACKAGE_NAME.toString());
        }
        Preconditions.checkArgumentNonnegative(i, "userId");
        synchronized (this.isDelayedPermissionBackupFinished) {
            if (this.isDelayedPermissionBackupFinished.get(i, false)) {
                return;
            }
            Unit unit = Unit.INSTANCE;
            PermissionControllerManager permissionControllerManager = this.permissionControllerManager;
            if (permissionControllerManager == null) {
                Intrinsics.throwUninitializedPropertyAccessException("permissionControllerManager");
                permissionControllerManager = null;
            }
            permissionControllerManager.applyStagedRuntimePermissionBackup(str, UserHandle.of(i), PermissionThread.getExecutor(), new Consumer() { // from class: com.android.server.permission.access.permission.PermissionService$restoreDelayedRuntimePermissions$3
                @Override // java.util.function.Consumer
                public final void accept(Boolean bool) {
                    SparseBooleanArray sparseBooleanArray;
                    SparseBooleanArray sparseBooleanArray2;
                    if (bool.booleanValue()) {
                        return;
                    }
                    sparseBooleanArray = PermissionService.this.isDelayedPermissionBackupFinished;
                    PermissionService permissionService = PermissionService.this;
                    int i2 = i;
                    synchronized (sparseBooleanArray) {
                        sparseBooleanArray2 = permissionService.isDelayedPermissionBackupFinished;
                        sparseBooleanArray2.put(i2, true);
                        Unit unit2 = Unit.INSTANCE;
                    }
                }
            });
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void dump(@NotNull FileDescriptor fileDescriptor, @NotNull PrintWriter printWriter, @Nullable String[] strArr) {
        if (DumpUtils.checkDumpPermission(this.context, LOG_TAG, printWriter)) {
            Object systemService = this.context.getSystemService((Class<Object>) PermissionControllerManager.class);
            Intrinsics.checkNotNull(systemService);
            ((PermissionControllerManager) systemService).dump(fileDescriptor, strArr);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    @Nullable
    public com.android.server.pm.permission.Permission getPermissionTEMP(@NotNull String str) {
        AccessState accessState = this.service.state;
        if (accessState == null) {
            Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        Permission permission = this.policy.getPermissions(new GetStateScope(accessState)).get(str);
        if (permission == null) {
            return null;
        }
        return new com.android.server.pm.permission.Permission(permission.getPermissionInfo(), permission.getType(), permission.isReconciled(), permission.getAppId(), permission.getGids(), permission.getAreGidsPerUser());
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    @NotNull
    public List<LegacyPermission> getLegacyPermissions() {
        AccessState accessState = this.service.state;
        if (accessState == null) {
            Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        ArrayMap<String, Permission> permissions = this.policy.getPermissions(new GetStateScope(accessState));
        ArrayList arrayList = new ArrayList();
        int size = permissions.size();
        for (int i = 0; i < size; i++) {
            permissions.keyAt(i);
            Permission valueAt = permissions.valueAt(i);
            arrayList.add(new LegacyPermission(valueAt.getPermissionInfo(), valueAt.getType(), valueAt.getAppId(), valueAt.getGids()));
        }
        return arrayList;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void readLegacyPermissionsTEMP(@NotNull LegacyPermissionSettings legacyPermissionSettings) {
        this.service.initialize();
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void writeLegacyPermissionsTEMP(@NotNull LegacyPermissionSettings legacyPermissionSettings) {
        AccessState accessState = this.service.state;
        if (accessState == null) {
            Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        GetStateScope getStateScope = new GetStateScope(accessState);
        legacyPermissionSettings.replacePermissions(toLegacyPermissions(this.policy.getPermissions(getStateScope)));
        legacyPermissionSettings.replacePermissionTrees(toLegacyPermissions(this.policy.getPermissionTrees(getStateScope)));
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    @NotNull
    public LegacyPermissionState getLegacyPermissionState(int i) {
        int[] iArr;
        PermissionService permissionService = this;
        LegacyPermissionState legacyPermissionState = new LegacyPermissionState();
        UserManagerService userManagerService = permissionService.userManagerService;
        AccessState accessState = null;
        if (userManagerService == null) {
            Intrinsics.throwUninitializedPropertyAccessException("userManagerService");
            userManagerService = null;
        }
        int[] userIdsIncludingPreCreated = userManagerService.getUserIdsIncludingPreCreated();
        AccessState accessState2 = permissionService.service.state;
        if (accessState2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("state");
        } else {
            accessState = accessState2;
        }
        GetStateScope getStateScope = new GetStateScope(accessState);
        ArrayMap<String, Permission> permissions = permissionService.policy.getPermissions(getStateScope);
        int length = userIdsIncludingPreCreated.length;
        int i2 = 0;
        while (i2 < length) {
            int i3 = userIdsIncludingPreCreated[i2];
            ArrayMap<String, Integer> uidPermissionFlags = permissionService.policy.getUidPermissionFlags(getStateScope, i, i3);
            if (uidPermissionFlags != null) {
                int size = uidPermissionFlags.size();
                int i4 = 0;
                while (i4 < size) {
                    String keyAt = uidPermissionFlags.keyAt(i4);
                    int intValue = uidPermissionFlags.valueAt(i4).intValue();
                    String str = keyAt;
                    Permission permission = permissions.get(str);
                    if (permission == null) {
                        iArr = userIdsIncludingPreCreated;
                    } else {
                        boolean z = permission.getPermissionInfo().getProtection() == 1;
                        PermissionFlags permissionFlags = PermissionFlags.INSTANCE;
                        iArr = userIdsIncludingPreCreated;
                        legacyPermissionState.putPermissionState(new LegacyPermissionState.PermissionState(str, z, permissionFlags.isPermissionGranted(intValue), permissionFlags.toApiFlags(intValue)), i3);
                    }
                    i4++;
                    userIdsIncludingPreCreated = iArr;
                }
            }
            i2++;
            permissionService = this;
            userIdsIncludingPreCreated = userIdsIncludingPreCreated;
        }
        return legacyPermissionState;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onSystemReady() {
        this.service.onSystemReady$frameworks__base__services__permission__android_common__services_permission();
        this.permissionControllerManager = new PermissionControllerManager(this.context, PermissionThread.getHandler());
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onUserCreated(int i) {
        this.service.onUserAdded$frameworks__base__services__permission__android_common__services_permission(i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onUserRemoved(int i) {
        this.service.onUserRemoved$frameworks__base__services__permission__android_common__services_permission(i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onStorageVolumeMounted(@NotNull String str, boolean z) {
        this.service.onStorageVolumeMounted$frameworks__base__services__permission__android_common__services_permission(str, z);
        synchronized (this.mountedStorageVolumes) {
            this.mountedStorageVolumes.add(str);
            Unit unit = Unit.INSTANCE;
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageAdded(@NotNull PackageState packageState, boolean z, @Nullable AndroidPackage androidPackage) {
        synchronized (this.mountedStorageVolumes) {
            if (this.mountedStorageVolumes.contains(packageState.getVolumeUuid())) {
                Unit unit = Unit.INSTANCE;
                this.service.onPackageAdded$frameworks__base__services__permission__android_common__services_permission(packageState.getPackageName());
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageInstalled(@NotNull AndroidPackage androidPackage, int i, @NotNull PermissionManagerServiceInternal.PackageInstalledParams packageInstalledParams, int i2) {
        int[] iArr;
        synchronized (this.mountedStorageVolumes) {
            if (this.mountedStorageVolumes.contains(androidPackage.getVolumeUuid())) {
                Unit unit = Unit.INSTANCE;
                if (i2 == -1) {
                    UserManagerService userManagerService = this.userManagerService;
                    if (userManagerService == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("userManagerService");
                        userManagerService = null;
                    }
                    iArr = userManagerService.getUserIdsIncludingPreCreated();
                } else {
                    iArr = new int[]{i2};
                }
                for (int i3 : iArr) {
                    this.service.onPackageInstalled$frameworks__base__services__permission__android_common__services_permission(androidPackage.getPackageName(), i3);
                }
                for (int i4 : iArr) {
                    PackageManagerInternal packageManagerInternal = this.packageManagerInternal;
                    if (packageManagerInternal == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
                        packageManagerInternal = null;
                    }
                    PackageStateInternal packageStateInternal = packageManagerInternal.getPackageStateInternal(androidPackage.getPackageName());
                    Intrinsics.checkNotNull(packageStateInternal);
                    addAllowlistedRestrictedPermissionsUnchecked(androidPackage, packageStateInternal.getAppId(), packageInstalledParams.getAllowlistedRestrictedPermissions(), i4);
                    setRequestedPermissionStates(packageStateInternal, i4, packageInstalledParams.getPermissionStates());
                }
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageUninstalled(@NotNull String str, int i, @NotNull PackageState packageState, @Nullable AndroidPackage androidPackage, @NotNull List<? extends AndroidPackage> list, int i2) {
        int[] iArr;
        PackageManagerInternal packageManagerInternal = null;
        if (i2 == -1) {
            UserManagerService userManagerService = this.userManagerService;
            if (userManagerService == null) {
                Intrinsics.throwUninitializedPropertyAccessException("userManagerService");
                userManagerService = null;
            }
            iArr = userManagerService.getUserIdsIncludingPreCreated();
        } else {
            iArr = new int[]{i2};
        }
        for (int i3 : iArr) {
            this.service.onPackageUninstalled$frameworks__base__services__permission__android_common__services_permission(str, i, i3);
        }
        PackageManagerInternal packageManagerInternal2 = this.packageManagerInternal;
        if (packageManagerInternal2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
        } else {
            packageManagerInternal = packageManagerInternal2;
        }
        if (((PackageStateInternal) packageManagerInternal.getPackageStates().get(str)) == null) {
            this.service.onPackageRemoved$frameworks__base__services__permission__android_common__services_permission(str, i);
        }
    }

    private final boolean isRootOrSystem(int i) {
        int appId = UserHandle.getAppId(i);
        return appId == 0 || appId == 1000;
    }

    private final boolean isShell(int i) {
        return UserHandle.getAppId(i) == 2000;
    }

    private final boolean isRootOrSystemOrShell(int i) {
        return isRootOrSystem(i) || isShell(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void killUid(int i, String str) {
        IActivityManager service = ActivityManager.getService();
        if (service != null) {
            int appId = UserHandle.getAppId(i);
            int userId = UserHandle.getUserId(i);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                try {
                    service.killUidForPermissionChange(appId, userId, str);
                } catch (RemoteException unused) {
                }
                Unit unit = Unit.INSTANCE;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    private final PackageManagerLocal.FilteredSnapshot withFilteredSnapshot(PackageManagerLocal packageManagerLocal, int i, int i2) {
        return packageManagerLocal.withFilteredSnapshot(i, UserHandle.of(i2));
    }

    private final PackageState getPackageState(PackageManagerLocal.UnfilteredSnapshot unfilteredSnapshot, String str) {
        return unfilteredSnapshot.getPackageStates().get(str);
    }

    private final boolean isUidInstantApp(PackageManagerLocal.UnfilteredSnapshot unfilteredSnapshot, int i) {
        PackageManagerInternal packageManagerInternal = this.packageManagerInternal;
        if (packageManagerInternal == null) {
            Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
            packageManagerInternal = null;
        }
        return packageManagerInternal.getInstantAppPackageName(i) != null;
    }

    private final boolean isPackageVisibleToUid(PackageManagerLocal.UnfilteredSnapshot unfilteredSnapshot, String str, int i) {
        return isPackageVisibleToUid(unfilteredSnapshot, str, UserHandle.getUserId(i), i);
    }

    private final boolean isPackageVisibleToUid(PackageManagerLocal.UnfilteredSnapshot unfilteredSnapshot, String str, int i, int i2) {
        PackageManagerLocal.FilteredSnapshot filtered = filtered(unfilteredSnapshot, i2, i);
        try {
            boolean z = filtered.getPackageState(str) != null;
            AutoCloseable.closeFinally(filtered, null);
            return z;
        } finally {
        }
    }

    private final PackageManagerLocal.FilteredSnapshot filtered(PackageManagerLocal.UnfilteredSnapshot unfilteredSnapshot, int i, int i2) {
        return unfilteredSnapshot.filtered(i, UserHandle.of(i2));
    }

    private final void enforceCallingOrSelfCrossUserPermission(int i, boolean z, boolean z2, String str) {
        if (!(i >= 0)) {
            throw new IllegalArgumentException(("userId " + i + " is invalid").toString());
        }
        int callingUid = Binder.getCallingUid();
        if (i != UserHandle.getUserId(callingUid)) {
            String str2 = z ? "android.permission.INTERACT_ACROSS_USERS_FULL" : "android.permission.INTERACT_ACROSS_USERS";
            if (this.context.checkCallingOrSelfPermission(str2) != 0) {
                StringBuilder sb = new StringBuilder();
                if (str != null) {
                    sb.append(str);
                    sb.append(": ");
                }
                sb.append("Neither user ");
                sb.append(Binder.getCallingUid());
                sb.append(" nor current process has ");
                sb.append(str2);
                sb.append(" to access user ");
                sb.append(i);
                String sb2 = sb.toString();
                Intrinsics.checkNotNullExpressionValue(sb2, "StringBuilder().apply(builderAction).toString()");
                throw new SecurityException(sb2);
            }
        }
        if (z2 && isShell(callingUid)) {
            UserManagerInternal userManagerInternal = this.userManagerInternal;
            if (userManagerInternal == null) {
                Intrinsics.throwUninitializedPropertyAccessException("userManagerInternal");
                userManagerInternal = null;
            }
            if (userManagerInternal.hasUserRestriction("no_debugging_features", i)) {
                StringBuilder sb3 = new StringBuilder();
                if (str != null) {
                    sb3.append(str);
                    sb3.append(": ");
                }
                sb3.append("Shell is disallowed to access user ");
                sb3.append(i);
                String sb4 = sb3.toString();
                Intrinsics.checkNotNullExpressionValue(sb4, "StringBuilder().apply(builderAction).toString()");
                throw new SecurityException(sb4);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: PermissionService.kt */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class OnPermissionFlagsChangedListener extends UidPermissionPolicy.OnPermissionFlagsChangedListener {
        private boolean isKillRuntimePermissionRevokedUidsSkipped;
        private boolean isPermissionFlagsChanged;

        @NotNull
        private final IntSet runtimePermissionChangedUids = new IntSet();

        @NotNull
        private final SparseBooleanArray runtimePermissionRevokedUids = new SparseBooleanArray();

        @NotNull
        private final IntSet gidsChangedUids = new IntSet();

        @NotNull
        private final ArraySet<String> killRuntimePermissionRevokedUidsReasons = new ArraySet<>();

        public OnPermissionFlagsChangedListener() {
        }

        public final void skipKillRuntimePermissionRevokedUids(@NotNull MutateStateScope mutateStateScope) {
            this.isKillRuntimePermissionRevokedUidsSkipped = true;
        }

        public final void addKillRuntimePermissionRevokedUidsReason(@NotNull MutateStateScope mutateStateScope, @NotNull String str) {
            this.killRuntimePermissionRevokedUidsReasons.add(str);
        }

        @Override // com.android.server.permission.access.permission.UidPermissionPolicy.OnPermissionFlagsChangedListener
        public void onPermissionFlagsChanged(int i, int i2, @NotNull String str, int i3, int i4) {
            boolean z;
            this.isPermissionFlagsChanged = true;
            int uid = UserHandle.getUid(i2, i);
            AccessCheckingService accessCheckingService = PermissionService.this.service;
            PermissionService permissionService = PermissionService.this;
            AccessState accessState = accessCheckingService.state;
            if (accessState == null) {
                Intrinsics.throwUninitializedPropertyAccessException("state");
                accessState = null;
            }
            Permission permission = permissionService.policy.getPermissions(new GetStateScope(accessState)).get(str);
            if (permission == null) {
                return;
            }
            PermissionFlags permissionFlags = PermissionFlags.INSTANCE;
            boolean isPermissionGranted = permissionFlags.isPermissionGranted(i3);
            boolean isPermissionGranted2 = permissionFlags.isPermissionGranted(i4);
            if (permission.getPermissionInfo().getProtection() == 1) {
                this.runtimePermissionChangedUids.add(uid);
                if (isPermissionGranted && !isPermissionGranted2) {
                    SparseBooleanArray sparseBooleanArray = this.runtimePermissionRevokedUids;
                    if (PermissionService.NOTIFICATIONS_PERMISSIONS.contains(str)) {
                        SparseBooleanArray sparseBooleanArray2 = this.runtimePermissionRevokedUids;
                        if (sparseBooleanArray2 == null ? true : sparseBooleanArray2.get(uid, true)) {
                            z = true;
                            sparseBooleanArray.put(uid, z);
                        }
                    }
                    z = false;
                    sparseBooleanArray.put(uid, z);
                }
            }
            if ((!(permission.getGids().length == 0)) && !isPermissionGranted && isPermissionGranted2) {
                this.gidsChangedUids.add(uid);
            }
        }

        @Override // com.android.server.permission.access.permission.UidPermissionPolicy.OnPermissionFlagsChangedListener
        public void onStateMutated() {
            Handler handler;
            OnPermissionsChangeListeners onPermissionsChangeListeners;
            if (this.isPermissionFlagsChanged) {
                PackageManager.invalidatePackageInfoCache();
                this.isPermissionFlagsChanged = false;
            }
            IntSet intSet = this.runtimePermissionChangedUids;
            PermissionService permissionService = PermissionService.this;
            int size = intSet.getSize();
            for (int i = 0; i < size; i++) {
                int elementAt = intSet.elementAt(i);
                OnPermissionsChangeListeners onPermissionsChangeListeners2 = permissionService.onPermissionsChangeListeners;
                if (onPermissionsChangeListeners2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("onPermissionsChangeListeners");
                    onPermissionsChangeListeners = null;
                } else {
                    onPermissionsChangeListeners = onPermissionsChangeListeners2;
                }
                onPermissionsChangeListeners.onPermissionsChanged(elementAt);
            }
            this.runtimePermissionChangedUids.clear();
            if (!this.isKillRuntimePermissionRevokedUidsSkipped) {
                final String joinToString$default = this.killRuntimePermissionRevokedUidsReasons.isEmpty() ^ true ? _Collections.joinToString$default(this.killRuntimePermissionRevokedUidsReasons, ", ", null, null, 0, null, null, 62, null) : "permissions revoked";
                SparseBooleanArray sparseBooleanArray = this.runtimePermissionRevokedUids;
                final PermissionService permissionService2 = PermissionService.this;
                int size2 = sparseBooleanArray.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    final int keyAt = sparseBooleanArray.keyAt(i2);
                    final boolean valueAt = sparseBooleanArray.valueAt(i2);
                    Handler handler2 = permissionService2.handler;
                    if (handler2 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("handler");
                        handler = null;
                    } else {
                        handler = handler2;
                    }
                    handler.post(new Runnable() { // from class: com.android.server.permission.access.permission.PermissionService$OnPermissionFlagsChangedListener$onStateMutated$2$1
                        @Override // java.lang.Runnable
                        public final void run() {
                            boolean isAppBackupAndRestoreRunning;
                            if (valueAt) {
                                isAppBackupAndRestoreRunning = this.isAppBackupAndRestoreRunning(keyAt);
                                if (isAppBackupAndRestoreRunning) {
                                    return;
                                }
                            }
                            permissionService2.killUid(keyAt, joinToString$default);
                        }
                    });
                }
            }
            this.runtimePermissionRevokedUids.clear();
            IntSet intSet2 = this.gidsChangedUids;
            final PermissionService permissionService3 = PermissionService.this;
            int size3 = intSet2.getSize();
            for (int i3 = 0; i3 < size3; i3++) {
                final int elementAt2 = intSet2.elementAt(i3);
                Handler handler3 = permissionService3.handler;
                if (handler3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("handler");
                    handler3 = null;
                }
                handler3.post(new Runnable() { // from class: com.android.server.permission.access.permission.PermissionService$OnPermissionFlagsChangedListener$onStateMutated$3$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        PermissionService.this.killUid(elementAt2, "permission grant or revoke changed gids");
                    }
                });
            }
            this.gidsChangedUids.clear();
            this.isKillRuntimePermissionRevokedUidsSkipped = false;
            this.killRuntimePermissionRevokedUidsReasons.clear();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean isAppBackupAndRestoreRunning(int i) {
            if (PermissionService.this.checkUidPermission(i, "android.permission.BACKUP") != 0) {
                return false;
            }
            try {
                ContentResolver contentResolver = PermissionService.this.context.getContentResolver();
                int userId = UserHandle.getUserId(i);
                return (Settings.Secure.getIntForUser(contentResolver, "user_setup_complete", userId) == 0) || (Settings.Secure.getIntForUser(contentResolver, "user_setup_personalization_state", userId) == 1);
            } catch (Settings.SettingNotFoundException e) {
                Log.w(PermissionService.LOG_TAG, "Failed to check if the user is in restore: " + e);
                return false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: PermissionService.kt */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class OnPermissionsChangeListeners extends Handler {

        @NotNull
        public static final Companion Companion = new Companion(null);

        @NotNull
        private final RemoteCallbackList<IOnPermissionsChangeListener> listeners;

        public OnPermissionsChangeListeners(@NotNull Looper looper) {
            super(looper);
            this.listeners = new RemoteCallbackList<>();
        }

        @Override // android.os.Handler
        public void handleMessage(@NotNull Message message) {
            if (message.what == 1) {
                handleOnPermissionsChanged(message.arg1);
            }
        }

        private final void handleOnPermissionsChanged(final int i) {
            this.listeners.broadcast(new Consumer() { // from class: com.android.server.permission.access.permission.PermissionService$OnPermissionsChangeListeners$handleOnPermissionsChanged$1
                @Override // java.util.function.Consumer
                public final void accept(IOnPermissionsChangeListener iOnPermissionsChangeListener) {
                    try {
                        iOnPermissionsChangeListener.onPermissionsChanged(i);
                    } catch (RemoteException e) {
                        Log.e(PermissionService.LOG_TAG, "Error when calling OnPermissionsChangeListener", e);
                    }
                }
            });
        }

        public final void addListener(@NotNull IOnPermissionsChangeListener iOnPermissionsChangeListener) {
            this.listeners.register(iOnPermissionsChangeListener);
        }

        public final void removeListener(@NotNull IOnPermissionsChangeListener iOnPermissionsChangeListener) {
            this.listeners.unregister(iOnPermissionsChangeListener);
        }

        public final void onPermissionsChanged(int i) {
            if (this.listeners.getRegisteredCallbackCount() > 0) {
                obtainMessage(1, i, 0).sendToTarget();
            }
        }

        /* compiled from: PermissionService.kt */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }
        }
    }

    /* compiled from: PermissionService.kt */
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
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        arrayMap.put("android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION");
        arrayMap.put("android.permission.INTERACT_ACROSS_USERS", "android.permission.INTERACT_ACROSS_USERS_FULL");
        FULLER_PERMISSIONS = arrayMap;
        asList = _ArraysJvm.asList(new String[]{"android.permission.POST_NOTIFICATIONS"});
        NOTIFICATIONS_PERMISSIONS = new ArraySet<>(asList);
        BACKUP_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(60L);
    }

    private final void enforceCallingOrSelfAnyPermission(String str, String... strArr) {
        int length = strArr.length;
        boolean z = false;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            if (this.context.checkCallingOrSelfPermission(strArr[i]) == 0) {
                z = true;
                break;
            }
            i++;
        }
        if (z) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        if (str != null) {
            sb.append(str);
            sb.append(": ");
        }
        sb.append("Neither user ");
        sb.append(Binder.getCallingUid());
        sb.append(" nor current process has any of ");
        _Arrays.joinTo$default(strArr, sb, ", ", null, null, 0, null, null, 124, null);
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "StringBuilder().apply(builderAction).toString()");
        throw new SecurityException(sb2);
    }
}
