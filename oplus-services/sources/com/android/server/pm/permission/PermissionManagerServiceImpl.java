package com.android.server.pm.permission;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.IActivityManager;
import android.app.admin.DevicePolicyManagerInternal;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.SigningDetails;
import android.content.pm.permission.SplitPermissionInfoParcelable;
import android.metrics.LogMaker;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Debug;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.permission.IOnPermissionsChangeListener;
import android.permission.PermissionControllerManager;
import android.permission.PermissionManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.EventLog;
import android.util.Log;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.compat.IPlatformCompat;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.os.RoSystemProperties;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.CollectionUtils;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.IntPair;
import com.android.internal.util.Preconditions;
import com.android.internal.util.function.TriConsumer;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.server.FgThread;
import com.android.server.LocalServices;
import com.android.server.PermissionThread;
import com.android.server.ServiceThread;
import com.android.server.SystemConfig;
import com.android.server.Watchdog;
import com.android.server.pm.ApexManager;
import com.android.server.pm.PackageInstallerService;
import com.android.server.pm.PackageManagerService;
import com.android.server.pm.PackageSetting;
import com.android.server.pm.UserManagerInternal;
import com.android.server.pm.UserManagerService;
import com.android.server.pm.parsing.PackageInfoUtils;
import com.android.server.pm.parsing.pkg.AndroidPackageInternal;
import com.android.server.pm.parsing.pkg.AndroidPackageUtils;
import com.android.server.pm.permission.LegacyPermissionState;
import com.android.server.pm.permission.PermissionManagerServiceImpl;
import com.android.server.pm.permission.PermissionManagerServiceInternal;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageState;
import com.android.server.pm.pkg.PackageStateInternal;
import com.android.server.pm.pkg.SharedUserApi;
import com.android.server.pm.pkg.component.ComponentMutateUtils;
import com.android.server.pm.pkg.component.ParsedPermission;
import com.android.server.pm.pkg.component.ParsedPermissionGroup;
import com.android.server.pm.pkg.component.ParsedPermissionUtils;
import com.android.server.pm.verify.domain.DomainVerificationLegacySettings;
import com.android.server.policy.PermissionPolicyInternal;
import com.android.server.policy.SoftRestrictedPermissionPolicy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Predicate;
import libcore.util.EmptyArray;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PermissionManagerServiceImpl implements PermissionManagerServiceInterface {
    private static final long BACKGROUND_RATIONALE_CHANGE_ID = 147316723;
    private static final int BLOCKING_PERMISSION_FLAGS = 52;
    private static final Map<String, String> FULLER_PERMISSION_MAP;
    private static final int MAX_PERMISSION_TREE_FOOTPRINT = 32768;
    private static final List<String> NEARBY_DEVICES_PERMISSIONS;
    private static final List<String> NOTIFICATION_PERMISSIONS;
    private static final Set<String> READ_MEDIA_AURAL_PERMISSIONS;
    private static final Set<String> READ_MEDIA_VISUAL_PERMISSIONS;
    private static final String SKIP_KILL_APP_REASON_NOTIFICATION_TEST = "skip permission revoke app kill for notification test";
    private static final List<String> STORAGE_PERMISSIONS;
    private static final String TAG = "PermissionManager";
    private static final int UPDATE_PERMISSIONS_ALL = 1;
    private static final int UPDATE_PERMISSIONS_REPLACE_ALL = 4;
    private static final int UPDATE_PERMISSIONS_REPLACE_PKG = 2;
    private static final int USER_PERMISSION_FLAGS = 3;
    private final int USER_ID_MULTI_APP;
    private final ApexManager mApexManager;
    private final Context mContext;
    private final PermissionCallback mDefaultPermissionCallback;
    private final int[] mGlobalGids;
    private final Handler mHandler;
    private final HandlerThread mHandlerThread;

    @GuardedBy({"mLock"})
    private final SparseBooleanArray mHasNoDelayedPermBackup;
    private final boolean mIsLeanback;
    private final Object mLock;
    private final MetricsLogger mMetricsLogger;
    private final OnPermissionChangeListeners mOnPermissionChangeListeners;
    private final PackageManagerInternal mPackageManagerInt;
    private PermissionControllerManager mPermissionControllerManager;
    public IPermissionManagerServiceExt mPermissionManagerServiceExt;

    @GuardedBy({"mLock"})
    private PermissionPolicyInternal mPermissionPolicyInternal;
    private final IPlatformCompat mPlatformCompat;

    @GuardedBy({"mLock"})
    private ArraySet<String> mPrivappPermissionsViolations;
    private final ArraySet<String> mPrivilegedPermissionAllowlistSourcePackageNames;

    @GuardedBy({"mLock"})
    private final PermissionRegistry mRegistry;

    @GuardedBy({"mLock"})
    private final ArrayList<PermissionManagerServiceInternal.OnRuntimePermissionStateChangedListener> mRuntimePermissionStateChangedListeners;

    @GuardedBy({"mLock"})
    private final DevicePermissionState mState;
    private final SparseArray<ArraySet<String>> mSystemPermissions;

    @GuardedBy({"mLock"})
    private boolean mSystemReady;
    private final UserManagerInternal mUserManagerInt;
    private static final String LOG_TAG = PermissionManagerServiceImpl.class.getSimpleName();
    private static final long BACKUP_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(60);
    private static final int[] EMPTY_INT_ARRAY = new int[0];

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private @interface UpdatePermissionFlags {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: -$$Nest$smkillUid, reason: not valid java name */
    public static /* bridge */ /* synthetic */ void m2388$$Nest$smkillUid(int i, int i2, String str) {
        killUid(i, i2, str);
    }

    static {
        ArrayList arrayList = new ArrayList();
        STORAGE_PERMISSIONS = arrayList;
        ArraySet arraySet = new ArraySet();
        READ_MEDIA_AURAL_PERMISSIONS = arraySet;
        ArraySet arraySet2 = new ArraySet();
        READ_MEDIA_VISUAL_PERMISSIONS = arraySet2;
        ArrayList arrayList2 = new ArrayList();
        NEARBY_DEVICES_PERMISSIONS = arrayList2;
        ArrayList arrayList3 = new ArrayList();
        NOTIFICATION_PERMISSIONS = arrayList3;
        HashMap hashMap = new HashMap();
        FULLER_PERMISSION_MAP = hashMap;
        hashMap.put("android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION");
        hashMap.put("android.permission.INTERACT_ACROSS_USERS", "android.permission.INTERACT_ACROSS_USERS_FULL");
        arrayList.add("android.permission.READ_EXTERNAL_STORAGE");
        arrayList.add("android.permission.WRITE_EXTERNAL_STORAGE");
        arraySet.add("android.permission.READ_MEDIA_AUDIO");
        arraySet2.add("android.permission.READ_MEDIA_VIDEO");
        arraySet2.add("android.permission.READ_MEDIA_IMAGES");
        arraySet2.add("android.permission.ACCESS_MEDIA_LOCATION");
        arraySet2.add("android.permission.READ_MEDIA_VISUAL_USER_SELECTED");
        arrayList2.add("android.permission.BLUETOOTH_ADVERTISE");
        arrayList2.add("android.permission.BLUETOOTH_CONNECT");
        arrayList2.add("android.permission.BLUETOOTH_SCAN");
        arrayList3.add("android.permission.POST_NOTIFICATIONS");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.pm.permission.PermissionManagerServiceImpl$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass1 extends PermissionCallback {
        AnonymousClass1() {
            super();
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
        public void onGidsChanged(final int i, final int i2) {
            PermissionManagerServiceImpl.this.mHandler.post(new Runnable() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$1$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    PermissionManagerServiceImpl.m2388$$Nest$smkillUid(i, i2, "permission grant or revoke changed gids");
                }
            });
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
        public void onPermissionGranted(int i, int i2) {
            PermissionManagerServiceImpl.this.mOnPermissionChangeListeners.onPermissionsChanged(i);
            PermissionManagerServiceImpl.this.mPackageManagerInt.writeSettings(true);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
        public void onInstallPermissionGranted() {
            PermissionManagerServiceImpl.this.mPackageManagerInt.writeSettings(true);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
        public void onPermissionRevoked(final int i, final int i2, final String str, boolean z, final String str2) {
            PermissionManagerServiceImpl.this.mOnPermissionChangeListeners.onPermissionsChanged(i);
            PermissionManagerServiceImpl.this.mPackageManagerInt.writeSettings(false);
            if (z) {
                return;
            }
            PermissionManagerServiceImpl.this.mHandler.post(new Runnable() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PermissionManagerServiceImpl.AnonymousClass1.this.lambda$onPermissionRevoked$1(str2, i, str, i2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPermissionRevoked$1(String str, int i, String str2, int i2) {
            if ("android.permission.POST_NOTIFICATIONS".equals(str) && isAppBackupAndRestoreRunning(i)) {
                return;
            }
            int appId = UserHandle.getAppId(i);
            if (str2 == null) {
                PermissionManagerServiceImpl.killUid(appId, i2, "permissions revoked");
            } else {
                PermissionManagerServiceImpl.killUid(appId, i2, str2);
            }
        }

        private boolean isAppBackupAndRestoreRunning(int i) {
            if (PermissionManagerServiceImpl.this.checkUidPermission(i, "android.permission.BACKUP") != 0) {
                return false;
            }
            try {
                int userId = UserHandle.getUserId(i);
                return (Settings.Secure.getIntForUser(PermissionManagerServiceImpl.this.mContext.getContentResolver(), "user_setup_complete", userId) == 0) || (Settings.Secure.getIntForUser(PermissionManagerServiceImpl.this.mContext.getContentResolver(), "user_setup_personalization_state", userId) == 1);
            } catch (Settings.SettingNotFoundException e) {
                Slog.w(PermissionManagerServiceImpl.LOG_TAG, "Failed to check if the user is in restore: " + e);
                return false;
            }
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
        public void onInstallPermissionRevoked() {
            PermissionManagerServiceImpl.this.mPackageManagerInt.writeSettings(true);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
        public void onPermissionUpdated(int[] iArr, boolean z) {
            PermissionManagerServiceImpl.this.mPackageManagerInt.writePermissionSettings(iArr, !z);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
        public void onInstallPermissionUpdated() {
            PermissionManagerServiceImpl.this.mPackageManagerInt.writeSettings(true);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
        public void onPermissionRemoved() {
            PermissionManagerServiceImpl.this.mPackageManagerInt.writeSettings(false);
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
        public void onPermissionUpdatedNotifyListener(int[] iArr, boolean z, int i) {
            onPermissionUpdated(iArr, z);
            for (int i2 : iArr) {
                PermissionManagerServiceImpl.this.mOnPermissionChangeListeners.onPermissionsChanged(UserHandle.getUid(i2, UserHandle.getAppId(i)));
            }
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
        public void onInstallPermissionUpdatedNotifyListener(int i) {
            onInstallPermissionUpdated();
            PermissionManagerServiceImpl.this.mOnPermissionChangeListeners.onPermissionsChanged(i);
        }
    }

    public PermissionManagerServiceImpl(Context context, ArrayMap<String, FeatureInfo> arrayMap) {
        String str;
        ArraySet<String> arraySet = new ArraySet<>();
        this.mPrivilegedPermissionAllowlistSourcePackageNames = arraySet;
        Object obj = new Object();
        this.mLock = obj;
        this.mState = new DevicePermissionState();
        this.mMetricsLogger = new MetricsLogger();
        this.mPlatformCompat = IPlatformCompat.Stub.asInterface(ServiceManager.getService("platform_compat"));
        this.mRegistry = new PermissionRegistry();
        this.mHasNoDelayedPermBackup = new SparseBooleanArray();
        this.mRuntimePermissionStateChangedListeners = new ArrayList<>();
        this.mPermissionManagerServiceExt = (IPermissionManagerServiceExt) ExtLoader.type(IPermissionManagerServiceExt.class).base(this).create();
        this.USER_ID_MULTI_APP = 999;
        this.mDefaultPermissionCallback = new AnonymousClass1();
        PackageManager.invalidatePackageInfoCache();
        PermissionManager.disablePackageNamePermissionCache();
        this.mContext = context;
        this.mPackageManagerInt = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        this.mUserManagerInt = (UserManagerInternal) LocalServices.getService(UserManagerInternal.class);
        this.mIsLeanback = arrayMap.containsKey("android.software.leanback");
        this.mApexManager = ApexManager.getInstance();
        arraySet.add(PackageManagerService.PLATFORM_PACKAGE_NAME);
        if (arrayMap.containsKey("android.hardware.type.automotive") && (str = SystemProperties.get("ro.android.car.carservice.package", (String) null)) != null) {
            arraySet.add(str);
        }
        ServiceThread serviceThread = new ServiceThread(TAG, 10, true);
        this.mHandlerThread = serviceThread;
        serviceThread.start();
        Handler handler = new Handler(serviceThread.getLooper());
        this.mHandler = handler;
        Watchdog.getInstance().addThread(handler);
        SystemConfig systemConfig = SystemConfig.getInstance();
        this.mSystemPermissions = systemConfig.getSystemPermissions();
        this.mGlobalGids = systemConfig.getGlobalGids();
        this.mOnPermissionChangeListeners = new OnPermissionChangeListeners(FgThread.get().getLooper());
        ArrayMap permissions = SystemConfig.getInstance().getPermissions();
        synchronized (obj) {
            for (int i = 0; i < permissions.size(); i++) {
                SystemConfig.PermissionEntry permissionEntry = (SystemConfig.PermissionEntry) permissions.valueAt(i);
                Permission permission = this.mRegistry.getPermission(permissionEntry.name);
                if (permission == null) {
                    permission = new Permission(permissionEntry.name, PackageManagerService.PLATFORM_PACKAGE_NAME, 1);
                    this.mRegistry.addPermission(permission);
                }
                int[] iArr = permissionEntry.gids;
                if (iArr != null) {
                    permission.setGids(iArr, permissionEntry.perUser);
                }
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (DumpUtils.checkDumpPermission(this.mContext, TAG, printWriter)) {
            ((PermissionControllerManager) this.mContext.getSystemService(PermissionControllerManager.class)).dump(fileDescriptor, strArr);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void killUid(int i, int i2, String str) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            IActivityManager service = ActivityManager.getService();
            if (service != null) {
                try {
                    service.killUidForPermissionChange(i, i2, str);
                } catch (RemoteException unused) {
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private String[] getAppOpPermissionPackagesInternal(String str) {
        synchronized (this.mLock) {
            ArraySet<String> appOpPermissionPackages = this.mRegistry.getAppOpPermissionPackages(str);
            if (appOpPermissionPackages == null) {
                return EmptyArray.STRING;
            }
            this.mPermissionManagerServiceExt.adjustGetAppOpPermissionPackagesInternal(appOpPermissionPackages);
            return (String[]) appOpPermissionPackages.toArray(new String[0]);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public List<PermissionGroupInfo> getAllPermissionGroups(int i) {
        final int callingUid = Binder.getCallingUid();
        if (this.mPackageManagerInt.getInstantAppPackageName(callingUid) != null) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        synchronized (this.mLock) {
            Iterator<ParsedPermissionGroup> it = this.mRegistry.getPermissionGroups().iterator();
            while (it.hasNext()) {
                arrayList.add(PackageInfoUtils.generatePermissionGroupInfo(it.next(), i));
            }
        }
        final int userId = UserHandle.getUserId(callingUid);
        arrayList.removeIf(new Predicate() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda7
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getAllPermissionGroups$0;
                lambda$getAllPermissionGroups$0 = PermissionManagerServiceImpl.this.lambda$getAllPermissionGroups$0(callingUid, userId, (PermissionGroupInfo) obj);
                return lambda$getAllPermissionGroups$0;
            }
        });
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getAllPermissionGroups$0(int i, int i2, PermissionGroupInfo permissionGroupInfo) {
        return this.mPackageManagerInt.filterAppAccess(permissionGroupInfo.packageName, i, i2, false);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public PermissionGroupInfo getPermissionGroupInfo(String str, int i) {
        int callingUid = Binder.getCallingUid();
        if (this.mPackageManagerInt.getInstantAppPackageName(callingUid) != null) {
            return null;
        }
        synchronized (this.mLock) {
            ParsedPermissionGroup permissionGroup = this.mRegistry.getPermissionGroup(str);
            if (permissionGroup == null) {
                return null;
            }
            PermissionGroupInfo generatePermissionGroupInfo = PackageInfoUtils.generatePermissionGroupInfo(permissionGroup, i);
            if (!this.mPackageManagerInt.filterAppAccess(generatePermissionGroupInfo.packageName, callingUid, UserHandle.getUserId(callingUid), false)) {
                return generatePermissionGroupInfo;
            }
            EventLog.writeEvent(1397638484, "186113473", Integer.valueOf(callingUid), str);
            return null;
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public PermissionInfo getPermissionInfo(String str, int i, String str2) {
        int callingUid = Binder.getCallingUid();
        if (this.mPackageManagerInt.getInstantAppPackageName(callingUid) != null) {
            return null;
        }
        int permissionInfoCallingTargetSdkVersion = getPermissionInfoCallingTargetSdkVersion(this.mPackageManagerInt.getPackage(str2), callingUid);
        synchronized (this.mLock) {
            Permission permission = this.mRegistry.getPermission(str);
            if (permission == null) {
                return null;
            }
            PermissionInfo generatePermissionInfo = permission.generatePermissionInfo(i, permissionInfoCallingTargetSdkVersion);
            if (!this.mPackageManagerInt.filterAppAccess(generatePermissionInfo.packageName, callingUid, UserHandle.getUserId(callingUid), false)) {
                return generatePermissionInfo;
            }
            EventLog.writeEvent(1397638484, "183122164", Integer.valueOf(callingUid), str);
            return null;
        }
    }

    private int getPermissionInfoCallingTargetSdkVersion(AndroidPackage androidPackage, int i) {
        int appId = UserHandle.getAppId(i);
        if (appId == 0 || appId == 1000 || appId == 2000 || androidPackage == null) {
            return 10000;
        }
        return androidPackage.getTargetSdkVersion();
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public List<PermissionInfo> queryPermissionsByGroup(String str, int i) {
        final int callingUid = Binder.getCallingUid();
        if (this.mPackageManagerInt.getInstantAppPackageName(callingUid) != null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(10);
        synchronized (this.mLock) {
            ParsedPermissionGroup permissionGroup = this.mRegistry.getPermissionGroup(str);
            if (str != null && permissionGroup == null) {
                return null;
            }
            for (Permission permission : this.mRegistry.getPermissions()) {
                if (Objects.equals(permission.getGroup(), str)) {
                    arrayList.add(permission.generatePermissionInfo(i));
                }
            }
            final int userId = UserHandle.getUserId(callingUid);
            if (permissionGroup != null && this.mPackageManagerInt.filterAppAccess(permissionGroup.getPackageName(), callingUid, userId, false)) {
                return null;
            }
            arrayList.removeIf(new Predicate() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda5
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$queryPermissionsByGroup$1;
                    lambda$queryPermissionsByGroup$1 = PermissionManagerServiceImpl.this.lambda$queryPermissionsByGroup$1(callingUid, userId, (PermissionInfo) obj);
                    return lambda$queryPermissionsByGroup$1;
                }
            });
            return arrayList;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$queryPermissionsByGroup$1(int i, int i2, PermissionInfo permissionInfo) {
        return this.mPackageManagerInt.filterAppAccess(permissionInfo.packageName, i, i2, false);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean addPermission(PermissionInfo permissionInfo, boolean z) {
        boolean z2;
        boolean addToTree;
        int callingUid = Binder.getCallingUid();
        if (this.mPackageManagerInt.getInstantAppPackageName(callingUid) != null) {
            throw new SecurityException("Instant apps can't add permissions");
        }
        if (permissionInfo.labelRes == 0 && permissionInfo.nonLocalizedLabel == null) {
            throw new SecurityException("Label must be specified in permission");
        }
        synchronized (this.mLock) {
            Permission enforcePermissionTree = this.mRegistry.enforcePermissionTree(permissionInfo.name, callingUid);
            Permission permission = this.mRegistry.getPermission(permissionInfo.name);
            z2 = permission == null;
            int fixProtectionLevel = PermissionInfo.fixProtectionLevel(permissionInfo.protectionLevel);
            enforcePermissionCapLocked(permissionInfo, enforcePermissionTree);
            if (z2) {
                permission = new Permission(permissionInfo.name, enforcePermissionTree.getPackageName(), 2);
            } else if (!permission.isDynamic()) {
                throw new SecurityException("Not allowed to modify non-dynamic permission " + permissionInfo.name);
            }
            addToTree = permission.addToTree(fixProtectionLevel, permissionInfo, enforcePermissionTree);
            if (z2) {
                this.mRegistry.addPermission(permission);
            }
        }
        if (addToTree) {
            this.mPackageManagerInt.writeSettings(z);
        }
        return z2;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void removePermission(String str) {
        int callingUid = Binder.getCallingUid();
        if (this.mPackageManagerInt.getInstantAppPackageName(callingUid) != null) {
            throw new SecurityException("Instant applications don't have access to this method");
        }
        synchronized (this.mLock) {
            this.mRegistry.enforcePermissionTree(str, callingUid);
            Permission permission = this.mRegistry.getPermission(str);
            if (permission == null) {
                return;
            }
            if (!permission.isDynamic()) {
                Slog.wtf(TAG, "Not allowed to modify non-dynamic permission " + str);
            }
            this.mRegistry.removePermission(str);
            this.mPackageManagerInt.writeSettings(false);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int getPermissionFlags(String str, String str2, int i) {
        return getPermissionFlagsInternal(str, str2, Binder.getCallingUid(), i);
    }

    private int getPermissionFlagsInternal(String str, String str2, int i, int i2) {
        if (!this.mUserManagerInt.exists(i2)) {
            return 0;
        }
        enforceGrantRevokeGetRuntimePermissionPermissions("getPermissionFlags");
        enforceCrossUserPermission(i, i2, true, false, "getPermissionFlags");
        AndroidPackage androidPackage = this.mPackageManagerInt.getPackage(str);
        if (androidPackage == null || this.mPackageManagerInt.filterAppAccess(str, i, i2, false)) {
            return 0;
        }
        synchronized (this.mLock) {
            if (this.mRegistry.getPermission(str2) == null) {
                return 0;
            }
            UidPermissionState uidStateLocked = getUidStateLocked(androidPackage, i2);
            if (uidStateLocked == null) {
                Slog.e(TAG, "Missing permissions state for " + str + " and user " + i2);
                return 0;
            }
            return uidStateLocked.getPermissionFlags(str2);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void updatePermissionFlags(String str, String str2, int i, int i2, boolean z, int i3) {
        boolean z2;
        int callingUid = Binder.getCallingUid();
        if (callingUid == 1000 || callingUid == 0 || (i & 4) == 0) {
            z2 = false;
        } else {
            if (z) {
                this.mContext.enforceCallingOrSelfPermission("android.permission.ADJUST_RUNTIME_PERMISSIONS_POLICY", "Need android.permission.ADJUST_RUNTIME_PERMISSIONS_POLICY to change policy flags");
            } else if (this.mPackageManagerInt.getUidTargetSdkVersion(callingUid) >= 29) {
                throw new IllegalArgumentException("android.permission.ADJUST_RUNTIME_PERMISSIONS_POLICY needs  to be checked for packages targeting 29 or later when changing policy flags");
            }
            z2 = true;
        }
        updatePermissionFlagsInternal(str, str2, i, i2, callingUid, i3, z2, this.mDefaultPermissionCallback);
    }

    private void updatePermissionFlagsInternal(String str, String str2, int i, int i2, int i3, int i4, boolean z, PermissionCallback permissionCallback) {
        if (this.mUserManagerInt.exists(i4)) {
            enforceGrantRevokeRuntimePermissionPermissions("updatePermissionFlags");
            enforceCrossUserPermission(i3, i4, true, true, "updatePermissionFlags");
            if ((i & 4) != 0 && !z) {
                throw new SecurityException("updatePermissionFlags requires android.permission.ADJUST_RUNTIME_PERMISSIONS_POLICY");
            }
            if (i3 != 1000) {
                i = i & (-17) & (-33);
                i2 = i2 & (-17) & (-33) & (-4097) & (-2049) & (-8193) & (-16385);
                if (!"android.permission.POST_NOTIFICATIONS".equals(str2) && i3 != 2000 && i3 != 0) {
                    i2 &= -65;
                }
            }
            AndroidPackage androidPackage = this.mPackageManagerInt.getPackage(str);
            if (androidPackage == null) {
                Log.e(TAG, "Unknown package: " + str);
                return;
            }
            if (this.mPackageManagerInt.filterAppAccess(str, i3, i4, false)) {
                throw new IllegalArgumentException("Unknown package: " + str);
            }
            boolean contains = androidPackage.getRequestedPermissions().contains(str2);
            if (!contains) {
                String[] sharedUserPackagesForPackage = this.mPackageManagerInt.getSharedUserPackagesForPackage(str, i4);
                int length = sharedUserPackagesForPackage.length;
                int i5 = 0;
                while (true) {
                    if (i5 >= length) {
                        break;
                    }
                    AndroidPackage androidPackage2 = this.mPackageManagerInt.getPackage(sharedUserPackagesForPackage[i5]);
                    if (androidPackage2 != null && androidPackage2.getRequestedPermissions().contains(str2)) {
                        contains = true;
                        break;
                    }
                    i5++;
                }
            }
            synchronized (this.mLock) {
                Permission permission = this.mRegistry.getPermission(str2);
                if (permission == null) {
                    throw new IllegalArgumentException("Unknown permission: " + str2);
                }
                boolean isRuntime = permission.isRuntime();
                UidPermissionState uidStateLocked = getUidStateLocked(androidPackage, i4);
                if (uidStateLocked == null) {
                    Slog.e(TAG, "Missing permissions state for " + str + " and user " + i4);
                    return;
                }
                if (!uidStateLocked.hasPermissionState(str2) && !contains) {
                    Log.e(TAG, "Permission " + str2 + " isn't requested by package " + str);
                    return;
                }
                boolean updatePermissionFlags = uidStateLocked.updatePermissionFlags(permission, i, i2);
                if (updatePermissionFlags && isRuntime) {
                    notifyRuntimePermissionStateChanged(str, i4);
                }
                if (!updatePermissionFlags || permissionCallback == null) {
                    return;
                }
                if (!isRuntime) {
                    permissionCallback.onInstallPermissionUpdatedNotifyListener(UserHandle.getUid(i4, androidPackage.getUid()));
                } else {
                    permissionCallback.onPermissionUpdatedNotifyListener(new int[]{i4}, false, androidPackage.getUid());
                }
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void updatePermissionFlagsForAllApps(int i, int i2, final int i3) {
        int callingUid = Binder.getCallingUid();
        if (this.mUserManagerInt.exists(i3)) {
            enforceGrantRevokeRuntimePermissionPermissions("updatePermissionFlagsForAllApps");
            enforceCrossUserPermission(callingUid, i3, true, true, "updatePermissionFlagsForAllApps");
            final int i4 = callingUid != 1000 ? i : i & (-17);
            final int i5 = callingUid != 1000 ? i2 : i2 & (-17);
            final boolean[] zArr = new boolean[1];
            this.mPackageManagerInt.forEachPackage(new Consumer() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    PermissionManagerServiceImpl.this.lambda$updatePermissionFlagsForAllApps$2(i3, zArr, i4, i5, (AndroidPackage) obj);
                }
            });
            if (zArr[0]) {
                this.mPackageManagerInt.writePermissionSettings(new int[]{i3}, true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePermissionFlagsForAllApps$2(int i, boolean[] zArr, int i2, int i3, AndroidPackage androidPackage) {
        synchronized (this.mLock) {
            UidPermissionState uidStateLocked = getUidStateLocked(androidPackage, i);
            if (uidStateLocked == null) {
                Slog.e(TAG, "Missing permissions state for " + androidPackage.getPackageName() + " and user " + i);
                return;
            }
            zArr[0] = uidStateLocked.updatePermissionFlagsForAllPermissions(i2, i3) | zArr[0];
            this.mOnPermissionChangeListeners.onPermissionsChanged(androidPackage.getUid());
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int checkPermission(String str, String str2, int i) {
        AndroidPackage androidPackage;
        if (this.mUserManagerInt.exists(i) && (androidPackage = this.mPackageManagerInt.getPackage(str)) != null) {
            return checkPermissionInternal(androidPackage, true, str2, i);
        }
        return -1;
    }

    private int checkPermissionInternal(AndroidPackage androidPackage, boolean z, String str, int i) {
        int callingUid = Binder.getCallingUid();
        if (z || androidPackage.getSharedUserId() == null) {
            if (this.mPackageManagerInt.filterAppAccess(androidPackage.getPackageName(), callingUid, i, false)) {
                return -1;
            }
        } else if (this.mPackageManagerInt.getInstantAppPackageName(callingUid) != null) {
            return -1;
        }
        boolean z2 = this.mPackageManagerInt.getInstantAppPackageName(UserHandle.getUid(i, androidPackage.getUid())) != null;
        synchronized (this.mLock) {
            UidPermissionState uidStateLocked = getUidStateLocked(androidPackage, i);
            if (uidStateLocked == null) {
                Slog.e(TAG, "Missing permissions state for " + androidPackage.getPackageName() + " and user " + i);
                return -1;
            }
            if (checkSinglePermissionInternalLocked(uidStateLocked, str, z2)) {
                return 0;
            }
            String str2 = FULLER_PERMISSION_MAP.get(str);
            return (str2 == null || !checkSinglePermissionInternalLocked(uidStateLocked, str2, z2)) ? -1 : 0;
        }
    }

    @GuardedBy({"mLock"})
    private boolean checkSinglePermissionInternalLocked(UidPermissionState uidPermissionState, String str, boolean z) {
        if (!uidPermissionState.isPermissionGranted(str)) {
            return false;
        }
        if (!z) {
            return true;
        }
        Permission permission = this.mRegistry.getPermission(str);
        return permission != null && permission.isInstant();
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int checkUidPermission(int i, String str) {
        if (!this.mUserManagerInt.exists(UserHandle.getUserId(i))) {
            return -1;
        }
        if (2000 == i && this.mPermissionManagerServiceExt.hookCheckUidPermissionImpl(this.mContext, str, i)) {
            return -1;
        }
        return checkUidPermissionInternal(this.mPackageManagerInt.getPackage(i), i, str);
    }

    private int checkUidPermissionInternal(AndroidPackage androidPackage, int i, String str) {
        if (androidPackage != null) {
            return checkPermissionInternal(androidPackage, false, str, UserHandle.getUserId(i));
        }
        synchronized (this.mLock) {
            if (checkSingleUidPermissionInternalLocked(i, str)) {
                return 0;
            }
            String str2 = FULLER_PERMISSION_MAP.get(str);
            return (str2 == null || !checkSingleUidPermissionInternalLocked(i, str2)) ? -1 : 0;
        }
    }

    @GuardedBy({"mLock"})
    private boolean checkSingleUidPermissionInternalLocked(int i, String str) {
        ArraySet<String> arraySet = this.mSystemPermissions.get(i);
        return arraySet != null && arraySet.contains(str);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void addOnPermissionsChangeListener(IOnPermissionsChangeListener iOnPermissionsChangeListener) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.OBSERVE_GRANT_REVOKE_PERMISSIONS", "addOnPermissionsChangeListener");
        this.mOnPermissionChangeListeners.addListener(iOnPermissionsChangeListener);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void removeOnPermissionsChangeListener(IOnPermissionsChangeListener iOnPermissionsChangeListener) {
        if (this.mPackageManagerInt.getInstantAppPackageName(Binder.getCallingUid()) != null) {
            throw new SecurityException("Instant applications don't have access to this method");
        }
        this.mOnPermissionChangeListeners.removeListener(iOnPermissionsChangeListener);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public List<String> getAllowlistedRestrictedPermissions(String str, int i, int i2) {
        Objects.requireNonNull(str);
        Preconditions.checkFlagsArgument(i, 7);
        Preconditions.checkArgumentNonNegative(i2, (String) null);
        if (UserHandle.getCallingUserId() != i2) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS", "getAllowlistedRestrictedPermissions for user " + i2);
        }
        AndroidPackage androidPackage = this.mPackageManagerInt.getPackage(str);
        if (androidPackage == null) {
            return null;
        }
        int callingUid = Binder.getCallingUid();
        if (this.mPackageManagerInt.filterAppAccess(str, callingUid, UserHandle.getCallingUserId(), false)) {
            return null;
        }
        boolean z = this.mContext.checkCallingOrSelfPermission("android.permission.WHITELIST_RESTRICTED_PERMISSIONS") == 0;
        boolean isCallerInstallerOfRecord = this.mPackageManagerInt.isCallerInstallerOfRecord(androidPackage, callingUid);
        if ((i & 1) != 0 && !z) {
            throw new SecurityException("Querying system allowlist requires android.permission.WHITELIST_RESTRICTED_PERMISSIONS");
        }
        if ((i & 6) != 0 && !z && !isCallerInstallerOfRecord) {
            throw new SecurityException("Querying upgrade or installer allowlist requires being installer on record or android.permission.WHITELIST_RESTRICTED_PERMISSIONS");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return getAllowlistedRestrictedPermissionsInternal(androidPackage, i, i2);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private List<String> getAllowlistedRestrictedPermissionsInternal(AndroidPackage androidPackage, int i, int i2) {
        synchronized (this.mLock) {
            UidPermissionState uidStateLocked = getUidStateLocked(androidPackage, i2);
            ArrayList arrayList = null;
            if (uidStateLocked == null) {
                Slog.e(TAG, "Missing permissions state for " + androidPackage.getPackageName() + " and user " + i2);
                return null;
            }
            int i3 = (i & 1) != 0 ? 4096 : 0;
            if ((i & 4) != 0) {
                i3 |= 8192;
            }
            if ((i & 2) != 0) {
                i3 |= 2048;
            }
            int size = ArrayUtils.size(androidPackage.getRequestedPermissions());
            for (int i4 = 0; i4 < size; i4++) {
                String str = androidPackage.getRequestedPermissions().get(i4);
                if ((uidStateLocked.getPermissionFlags(str) & i3) != 0) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add(str);
                }
            }
            return arrayList;
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean addAllowlistedRestrictedPermission(String str, String str2, int i, int i2) {
        Objects.requireNonNull(str2);
        if (!checkExistsAndEnforceCannotModifyImmutablyRestrictedPermission(str2)) {
            return false;
        }
        List<String> allowlistedRestrictedPermissions = getAllowlistedRestrictedPermissions(str, i, i2);
        if (allowlistedRestrictedPermissions == null) {
            allowlistedRestrictedPermissions = new ArrayList<>(1);
        }
        if (allowlistedRestrictedPermissions.indexOf(str2) >= 0) {
            return false;
        }
        allowlistedRestrictedPermissions.add(str2);
        return setAllowlistedRestrictedPermissions(str, allowlistedRestrictedPermissions, i, i2);
    }

    private boolean checkExistsAndEnforceCannotModifyImmutablyRestrictedPermission(String str) {
        synchronized (this.mLock) {
            Permission permission = this.mRegistry.getPermission(str);
            if (permission == null) {
                Slog.w(TAG, "No such permissions: " + str);
                return false;
            }
            String packageName = permission.getPackageName();
            boolean z = permission.isHardOrSoftRestricted() && permission.isImmutablyRestricted();
            int callingUid = Binder.getCallingUid();
            if (this.mPackageManagerInt.filterAppAccess(packageName, callingUid, UserHandle.getUserId(callingUid), false)) {
                EventLog.writeEvent(1397638484, "186404356", Integer.valueOf(callingUid), str);
                return false;
            }
            if (!z || this.mContext.checkCallingOrSelfPermission("android.permission.WHITELIST_RESTRICTED_PERMISSIONS") == 0) {
                return true;
            }
            throw new SecurityException("Cannot modify allowlisting of an immutably restricted permission: " + str);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean removeAllowlistedRestrictedPermission(String str, String str2, int i, int i2) {
        List<String> allowlistedRestrictedPermissions;
        Objects.requireNonNull(str2);
        if (checkExistsAndEnforceCannotModifyImmutablyRestrictedPermission(str2) && (allowlistedRestrictedPermissions = getAllowlistedRestrictedPermissions(str, i, i2)) != null && allowlistedRestrictedPermissions.remove(str2)) {
            return setAllowlistedRestrictedPermissions(str, allowlistedRestrictedPermissions, i, i2);
        }
        return false;
    }

    private boolean setAllowlistedRestrictedPermissions(String str, List<String> list, int i, int i2) {
        Objects.requireNonNull(str);
        Preconditions.checkFlagsArgument(i, 7);
        Preconditions.checkArgument(Integer.bitCount(i) == 1);
        Preconditions.checkArgumentNonNegative(i2, (String) null);
        if (UserHandle.getCallingUserId() != i2) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS", "setAllowlistedRestrictedPermissions for user " + i2);
        }
        AndroidPackage androidPackage = this.mPackageManagerInt.getPackage(str);
        if (androidPackage == null) {
            return false;
        }
        int callingUid = Binder.getCallingUid();
        if (this.mPackageManagerInt.filterAppAccess(str, callingUid, UserHandle.getCallingUserId(), false)) {
            return false;
        }
        boolean z = this.mContext.checkCallingOrSelfPermission("android.permission.WHITELIST_RESTRICTED_PERMISSIONS") == 0;
        boolean isCallerInstallerOfRecord = this.mPackageManagerInt.isCallerInstallerOfRecord(androidPackage, callingUid);
        if ((i & 1) != 0 && !z) {
            throw new SecurityException("Modifying system allowlist requires android.permission.WHITELIST_RESTRICTED_PERMISSIONS");
        }
        if ((i & 4) != 0) {
            if (!z && !isCallerInstallerOfRecord) {
                throw new SecurityException("Modifying upgrade allowlist requires being installer on record or android.permission.WHITELIST_RESTRICTED_PERMISSIONS");
            }
            List<String> allowlistedRestrictedPermissions = getAllowlistedRestrictedPermissions(androidPackage.getPackageName(), i, i2);
            if (list == null || list.isEmpty()) {
                if (allowlistedRestrictedPermissions == null || allowlistedRestrictedPermissions.isEmpty()) {
                    return true;
                }
            } else {
                int size = list.size();
                for (int i3 = 0; i3 < size; i3++) {
                    if ((allowlistedRestrictedPermissions == null || !allowlistedRestrictedPermissions.contains(list.get(i3))) && !z) {
                        throw new SecurityException("Adding to upgrade allowlist requiresandroid.permission.WHITELIST_RESTRICTED_PERMISSIONS");
                    }
                }
            }
        }
        if ((i & 2) != 0 && !z && !isCallerInstallerOfRecord) {
            throw new SecurityException("Modifying installer allowlist requires being installer on record or android.permission.WHITELIST_RESTRICTED_PERMISSIONS");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            setAllowlistedRestrictedPermissionsInternal(androidPackage, list, i, i2);
            return true;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void grantRuntimePermission(String str, String str2, int i) {
        int callingUid = Binder.getCallingUid();
        grantRuntimePermissionInternal(str, str2, checkUidPermission(callingUid, "android.permission.ADJUST_RUNTIME_PERMISSIONS_POLICY") == 0, callingUid, i, this.mDefaultPermissionCallback);
    }

    /* JADX WARN: Removed duplicated region for block: B:110:0x0269  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x0278  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0290  */
    /* JADX WARN: Removed duplicated region for block: B:121:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void grantRuntimePermissionInternal(String str, String str2, boolean z, int i, int i2, PermissionCallback permissionCallback) {
        boolean isRole;
        boolean isSoftRestricted;
        if (!this.mUserManagerInt.exists(i2)) {
            Log.e(TAG, "No such user:" + i2);
            return;
        }
        this.mContext.enforceCallingOrSelfPermission("android.permission.GRANT_RUNTIME_PERMISSIONS", "grantRuntimePermission");
        enforceCrossUserPermission(i, i2, true, true, "grantRuntimePermission");
        AndroidPackage androidPackage = this.mPackageManagerInt.getPackage(str);
        PackageStateInternal packageStateInternal = this.mPackageManagerInt.getPackageStateInternal(str);
        if (androidPackage == null || packageStateInternal == null) {
            Log.e(TAG, "Unknown package: " + str);
            return;
        }
        boolean z2 = false;
        if (this.mPackageManagerInt.filterAppAccess(str, i, i2, false)) {
            throw new IllegalArgumentException("Unknown package: " + str);
        }
        synchronized (this.mLock) {
            Permission permission = this.mRegistry.getPermission(str2);
            if (permission == null) {
                throw new IllegalArgumentException("Unknown permission: " + str2);
            }
            isRole = permission.isRole();
            isSoftRestricted = permission.isSoftRestricted();
        }
        boolean z3 = isRole && mayManageRolePermission(i);
        if (isSoftRestricted && SoftRestrictedPermissionPolicy.forPermission(this.mContext, AndroidPackageUtils.generateAppInfoWithoutState(androidPackage), androidPackage, UserHandle.of(i2), str2).mayGrantPermission()) {
            z2 = true;
        }
        synchronized (this.mLock) {
            Permission permission2 = this.mRegistry.getPermission(str2);
            if (permission2 == null) {
                throw new IllegalArgumentException("Unknown permission: " + str2);
            }
            boolean isRuntime = permission2.isRuntime();
            boolean hasGids = permission2.hasGids();
            if (!isRuntime && !permission2.isDevelopment()) {
                if (!permission2.isRole()) {
                    throw new SecurityException("Permission " + str2 + " requested by " + androidPackage.getPackageName() + " is not a changeable permission type");
                }
                if (!z3) {
                    throw new SecurityException("Permission " + str2 + " is managed by role");
                }
            }
            UidPermissionState uidStateLocked = getUidStateLocked(androidPackage, i2);
            if (uidStateLocked == null) {
                Slog.e(TAG, "Missing permissions state for " + androidPackage.getPackageName() + " and user " + i2);
                return;
            }
            if (!uidStateLocked.hasPermissionState(str2) && !androidPackage.getRequestedPermissions().contains(str2)) {
                throw new SecurityException("Package " + androidPackage.getPackageName() + " has not requested permission " + str2);
            }
            if (androidPackage.getTargetSdkVersion() < 23 && permission2.isRuntime()) {
                return;
            }
            int permissionFlags = uidStateLocked.getPermissionFlags(str2);
            if ((permissionFlags & 16) != 0) {
                Log.e(TAG, "Cannot grant system fixed permission " + str2 + " for package " + str);
                return;
            }
            if (!z && (permissionFlags & 4) != 0) {
                Log.e(TAG, "Cannot grant policy fixed permission " + str2 + " for package " + str);
                return;
            }
            if (permission2.isHardRestricted() && (permissionFlags & 14336) == 0) {
                Log.e(TAG, "Cannot grant hard restricted non-exempt permission " + str2 + " for package " + str);
                return;
            }
            if (permission2.isSoftRestricted() && !z2) {
                Log.e(TAG, "Cannot grant soft restricted permission " + str2 + " for package " + str);
                return;
            }
            if (!permission2.isDevelopment() && !permission2.isRole()) {
                if (packageStateInternal.getUserStateOrDefault(i2).isInstantApp() && !permission2.isInstant()) {
                    throw new SecurityException("Cannot grant non-ephemeral permission " + str2 + " for package " + str);
                }
                if (androidPackage.getTargetSdkVersion() < 23) {
                    Slog.w(TAG, "Cannot grant runtime permission to a legacy app");
                    return;
                }
                if (!uidStateLocked.grantPermission(permission2)) {
                    return;
                }
                if (isRuntime) {
                    logPermission(1243, str2, str);
                }
                int uid = UserHandle.getUid(i2, androidPackage.getUid());
                if (permissionCallback != null) {
                    if (isRuntime) {
                        permissionCallback.onPermissionGranted(uid, i2);
                    } else {
                        permissionCallback.onInstallPermissionGranted();
                    }
                    if (hasGids) {
                        permissionCallback.onGidsChanged(UserHandle.getAppId(androidPackage.getUid()), i2);
                    }
                }
                if (isRuntime) {
                    return;
                }
                notifyRuntimePermissionStateChanged(str, i2);
                return;
            }
            if (!uidStateLocked.grantPermission(permission2)) {
                return;
            }
            if (isRuntime) {
            }
            int uid2 = UserHandle.getUid(i2, androidPackage.getUid());
            if (permissionCallback != null) {
            }
            if (isRuntime) {
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void revokeRuntimePermission(String str, String str2, int i, String str3) {
        int callingUid = Binder.getCallingUid();
        revokeRuntimePermissionInternal(str, str2, checkUidPermission(callingUid, "android.permission.ADJUST_RUNTIME_PERMISSIONS_POLICY") == 0, callingUid, i, str3, this.mDefaultPermissionCallback);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void revokePostNotificationPermissionWithoutKillForTest(String str, int i) {
        int callingUid = Binder.getCallingUid();
        boolean z = checkUidPermission(callingUid, "android.permission.ADJUST_RUNTIME_PERMISSIONS_POLICY") == 0;
        this.mContext.enforceCallingPermission("android.permission.REVOKE_POST_NOTIFICATIONS_WITHOUT_KILL", "");
        revokeRuntimePermissionInternal(str, "android.permission.POST_NOTIFICATIONS", z, true, callingUid, i, SKIP_KILL_APP_REASON_NOTIFICATION_TEST, this.mDefaultPermissionCallback);
    }

    private void revokeRuntimePermissionInternal(String str, String str2, boolean z, int i, int i2, String str3, PermissionCallback permissionCallback) {
        revokeRuntimePermissionInternal(str, str2, z, false, i, i2, str3, permissionCallback);
    }

    /* JADX WARN: Code restructure failed: missing block: B:70:0x018b, code lost:
    
        if ((r5 & 4) != 0) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x01ac, code lost:
    
        throw new java.lang.SecurityException("Cannot revoke policy fixed permission " + r14 + " for package " + r13);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void revokeRuntimePermissionInternal(String str, String str2, boolean z, boolean z2, int i, int i2, String str3, PermissionCallback permissionCallback) {
        boolean isRole;
        if (!this.mUserManagerInt.exists(i2)) {
            Log.e(TAG, "No such user:" + i2);
            return;
        }
        this.mContext.enforceCallingOrSelfPermission("android.permission.REVOKE_RUNTIME_PERMISSIONS", "revokeRuntimePermission");
        enforceCrossUserPermission(i, i2, true, true, "revokeRuntimePermission");
        AndroidPackage androidPackage = this.mPackageManagerInt.getPackage(str);
        if (androidPackage == null) {
            Log.e(TAG, "Unknown package: " + str);
            return;
        }
        boolean z3 = false;
        if (this.mPackageManagerInt.filterAppAccess(str, i, i2, false)) {
            throw new IllegalArgumentException("Unknown package: " + str);
        }
        synchronized (this.mLock) {
            Permission permission = this.mRegistry.getPermission(str2);
            if (permission == null) {
                throw new IllegalArgumentException("Unknown permission: " + str2);
            }
            isRole = permission.isRole();
        }
        if (isRole && (i == Process.myUid() || mayManageRolePermission(i))) {
            z3 = true;
        }
        synchronized (this.mLock) {
            Permission permission2 = this.mRegistry.getPermission(str2);
            if (permission2 == null) {
                throw new IllegalArgumentException("Unknown permission: " + str2);
            }
            boolean isRuntime = permission2.isRuntime();
            if (!isRuntime && !permission2.isDevelopment()) {
                if (!permission2.isRole()) {
                    throw new SecurityException("Permission " + str2 + " requested by " + androidPackage.getPackageName() + " is not a changeable permission type");
                }
                if (!z3) {
                    throw new SecurityException("Permission " + str2 + " is managed by role");
                }
            }
            UidPermissionState uidStateLocked = getUidStateLocked(androidPackage, i2);
            if (uidStateLocked == null) {
                Slog.e(TAG, "Missing permissions state for " + androidPackage.getPackageName() + " and user " + i2);
                return;
            }
            if (!uidStateLocked.hasPermissionState(str2) && !androidPackage.getRequestedPermissions().contains(str2)) {
                throw new SecurityException("Package " + androidPackage.getPackageName() + " has not requested permission " + str2);
            }
            if (androidPackage.getTargetSdkVersion() >= 23 || !permission2.isRuntime()) {
                int permissionFlags = uidStateLocked.getPermissionFlags(str2);
                if ((permissionFlags & 16) != 0 && UserHandle.getCallingAppId() != 1000) {
                    throw new SecurityException("Non-System UID cannot revoke system fixed permission " + str2 + " for package " + str);
                }
                if (uidStateLocked.revokePermission(permission2)) {
                    if (isRuntime) {
                        logPermission(1245, str2, str);
                    }
                    if (permissionCallback != null) {
                        if (isRuntime) {
                            permissionCallback.onPermissionRevoked(UserHandle.getUid(i2, androidPackage.getUid()), i2, str3, z2, str2);
                        } else {
                            this.mDefaultPermissionCallback.onInstallPermissionRevoked();
                        }
                    }
                    if (isRuntime) {
                        notifyRuntimePermissionStateChanged(str, i2);
                    }
                }
            }
        }
    }

    private boolean mayManageRolePermission(int i) {
        PackageManager packageManager = this.mContext.getPackageManager();
        String[] packagesForUid = packageManager.getPackagesForUid(i);
        if (packagesForUid == null) {
            return false;
        }
        return Arrays.asList(packagesForUid).contains(packageManager.getPermissionControllerPackageName());
    }

    private void resetRuntimePermissionsInternal(AndroidPackage androidPackage, final int i) {
        final boolean[] zArr = new boolean[1];
        final ArraySet arraySet = new ArraySet();
        final ArraySet arraySet2 = new ArraySet();
        final ArraySet arraySet3 = new ArraySet();
        final PermissionCallback permissionCallback = new PermissionCallback() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
            public void onGidsChanged(int i2, int i3) {
                PermissionManagerServiceImpl.this.mDefaultPermissionCallback.onGidsChanged(i2, i3);
            }

            @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
            public void onPermissionChanged() {
                PermissionManagerServiceImpl.this.mDefaultPermissionCallback.onPermissionChanged();
            }

            @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
            public void onPermissionGranted(int i2, int i3) {
                PermissionManagerServiceImpl.this.mDefaultPermissionCallback.onPermissionGranted(i2, i3);
            }

            @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
            public void onInstallPermissionGranted() {
                PermissionManagerServiceImpl.this.mDefaultPermissionCallback.onInstallPermissionGranted();
            }

            @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
            public void onPermissionRevoked(int i2, int i3, String str, boolean z, String str2) {
                arraySet.add(Long.valueOf(IntPair.of(i2, i3)));
                arraySet2.add(Integer.valueOf(i3));
            }

            @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
            public void onInstallPermissionRevoked() {
                PermissionManagerServiceImpl.this.mDefaultPermissionCallback.onInstallPermissionRevoked();
            }

            @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
            public void onPermissionUpdated(int[] iArr, boolean z) {
                for (int i2 : iArr) {
                    if (z) {
                        arraySet2.add(Integer.valueOf(i2));
                        arraySet3.remove(Integer.valueOf(i2));
                    } else if (arraySet2.indexOf(Integer.valueOf(i2)) == -1) {
                        arraySet3.add(Integer.valueOf(i2));
                    }
                }
            }

            @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
            public void onPermissionRemoved() {
                zArr[0] = true;
            }

            @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
            public void onInstallPermissionUpdated() {
                PermissionManagerServiceImpl.this.mDefaultPermissionCallback.onInstallPermissionUpdated();
            }

            @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
            public void onPermissionUpdatedNotifyListener(int[] iArr, boolean z, int i2) {
                onPermissionUpdated(iArr, z);
                PermissionManagerServiceImpl.this.mOnPermissionChangeListeners.onPermissionsChanged(i2);
            }

            @Override // com.android.server.pm.permission.PermissionManagerServiceImpl.PermissionCallback
            public void onInstallPermissionUpdatedNotifyListener(int i2) {
                PermissionManagerServiceImpl.this.mDefaultPermissionCallback.onInstallPermissionUpdatedNotifyListener(i2);
            }
        };
        if (androidPackage != null) {
            lambda$resetRuntimePermissionsInternal$3(androidPackage, i, permissionCallback);
        } else {
            this.mPackageManagerInt.forEachPackage(new Consumer() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda9
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    PermissionManagerServiceImpl.this.lambda$resetRuntimePermissionsInternal$3(i, permissionCallback, (AndroidPackage) obj);
                }
            });
        }
        if (zArr[0]) {
            this.mDefaultPermissionCallback.onPermissionRemoved();
        }
        if (!arraySet.isEmpty()) {
            int size = arraySet.size();
            for (int i2 = 0; i2 < size; i2++) {
                final int first = IntPair.first(((Long) arraySet.valueAt(i2)).longValue());
                final int second = IntPair.second(((Long) arraySet.valueAt(i2)).longValue());
                this.mOnPermissionChangeListeners.onPermissionsChanged(first);
                this.mHandler.post(new Runnable() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda10
                    @Override // java.lang.Runnable
                    public final void run() {
                        PermissionManagerServiceImpl.lambda$resetRuntimePermissionsInternal$4(first, second);
                    }
                });
            }
        }
        this.mPackageManagerInt.writePermissionSettings(ArrayUtils.convertToIntArray(arraySet2), false);
        this.mPackageManagerInt.writePermissionSettings(ArrayUtils.convertToIntArray(arraySet3), true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$resetRuntimePermissionsInternal$4(int i, int i2) {
        killUid(UserHandle.getAppId(i), i2, "permissions revoked");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: resetRuntimePermissionsInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$resetRuntimePermissionsInternal$3(AndroidPackage androidPackage, int i, PermissionCallback permissionCallback) {
        boolean z;
        String packageName = androidPackage.getPackageName();
        int size = ArrayUtils.size(androidPackage.getRequestedPermissions());
        for (int i2 = 0; i2 < size; i2++) {
            String str = androidPackage.getRequestedPermissions().get(i2);
            synchronized (this.mLock) {
                Permission permission = this.mRegistry.getPermission(str);
                if (permission != null) {
                    if (!permission.isRemoved()) {
                        boolean isRuntime = permission.isRuntime();
                        String[] sharedUserPackagesForPackage = this.mPackageManagerInt.getSharedUserPackagesForPackage(androidPackage.getPackageName(), i);
                        if (sharedUserPackagesForPackage.length > 0) {
                            int length = sharedUserPackagesForPackage.length;
                            int i3 = 0;
                            while (true) {
                                if (i3 >= length) {
                                    z = false;
                                    break;
                                }
                                AndroidPackage androidPackage2 = this.mPackageManagerInt.getPackage(sharedUserPackagesForPackage[i3]);
                                if (androidPackage2 != null && !androidPackage2.getPackageName().equals(packageName) && androidPackage2.getRequestedPermissions().contains(str)) {
                                    z = true;
                                    break;
                                }
                                i3++;
                            }
                            if (z) {
                            }
                        }
                        int permissionFlagsInternal = getPermissionFlagsInternal(packageName, str, 1000, i);
                        int uidTargetSdkVersion = this.mPackageManagerInt.getUidTargetSdkVersion(this.mPackageManagerInt.getPackageUid(packageName, 0L, i));
                        int i4 = (uidTargetSdkVersion >= 23 || !isRuntime) ? 0 : 72;
                        updatePermissionFlagsInternal(packageName, str, 589899, i4, 1000, i, false, permissionCallback);
                        if (isRuntime && (permissionFlagsInternal & 20) == 0) {
                            if ((permissionFlagsInternal & 32) != 0 || (permissionFlagsInternal & 32768) != 0) {
                                grantRuntimePermissionInternal(packageName, str, false, 1000, i, permissionCallback);
                            } else if ((i4 & 64) == 0 && !isPermissionSplitFromNonRuntime(str, uidTargetSdkVersion)) {
                                revokeRuntimePermissionInternal(packageName, str, false, 1000, i, null, permissionCallback);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isPermissionSplitFromNonRuntime(String str, int i) {
        List<PermissionManager.SplitPermissionInfo> splitPermissionInfos = getSplitPermissionInfos();
        int size = splitPermissionInfos.size();
        boolean z = false;
        for (int i2 = 0; i2 < size; i2++) {
            PermissionManager.SplitPermissionInfo splitPermissionInfo = splitPermissionInfos.get(i2);
            if (i < splitPermissionInfo.getTargetSdk() && splitPermissionInfo.getNewPermissions().contains(str)) {
                synchronized (this.mLock) {
                    Permission permission = this.mRegistry.getPermission(splitPermissionInfo.getSplitPermission());
                    if (permission != null && !permission.isRuntime()) {
                        z = true;
                    }
                }
                return z;
            }
        }
        return false;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean shouldShowRequestPermissionRationale(String str, String str2, int i) {
        int callingUid = Binder.getCallingUid();
        if (UserHandle.getCallingUserId() != i) {
            this.mContext.enforceCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "canShowRequestPermissionRationale for user " + i);
        }
        if (UserHandle.getAppId(callingUid) != UserHandle.getAppId(this.mPackageManagerInt.getPackageUid(str, 268435456L, i)) || checkPermission(str, str2, i) == 0) {
            return false;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            int permissionFlagsInternal = getPermissionFlagsInternal(str, str2, callingUid, i);
            Binder.restoreCallingIdentity(clearCallingIdentity);
            if ((permissionFlagsInternal & 22) != 0) {
                return false;
            }
            synchronized (this.mLock) {
                Permission permission = this.mRegistry.getPermission(str2);
                if (permission == null) {
                    return false;
                }
                if (permission.isHardRestricted() && (permissionFlagsInternal & 14336) == 0) {
                    return false;
                }
                clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    try {
                        if (str2.equals("android.permission.ACCESS_BACKGROUND_LOCATION")) {
                            if (this.mPlatformCompat.isChangeEnabledByPackageName(BACKGROUND_RATIONALE_CHANGE_ID, str, i)) {
                                return true;
                            }
                        }
                    } catch (RemoteException e) {
                        Log.e(TAG, "Unable to check if compatibility change is enabled.", e);
                    }
                    return (permissionFlagsInternal & 1) != 0;
                } finally {
                }
            }
        } finally {
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean isPermissionRevokedByPolicy(String str, String str2, int i) {
        if (UserHandle.getCallingUserId() != i) {
            this.mContext.enforceCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "isPermissionRevokedByPolicy for user " + i);
        }
        if (checkPermission(str, str2, i) == 0) {
            return false;
        }
        int callingUid = Binder.getCallingUid();
        if (this.mPackageManagerInt.filterAppAccess(str, callingUid, i, false)) {
            return false;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return (getPermissionFlagsInternal(str, str2, callingUid, i) & 4) != 0;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public byte[] backupRuntimePermissions(int i) {
        Preconditions.checkArgumentNonNegative(i, "userId");
        final CompletableFuture completableFuture = new CompletableFuture();
        this.mPermissionControllerManager.getRuntimePermissionBackup(UserHandle.of(i), PermissionThread.getExecutor(), new Consumer() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                completableFuture.complete((byte[]) obj);
            }
        });
        try {
            return (byte[]) completableFuture.get(BACKUP_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Slog.e(TAG, "Cannot create permission backup for user " + i, e);
            return null;
        }
    }

    public void restoreRuntimePermissions(byte[] bArr, int i) {
        Objects.requireNonNull(bArr, "backup");
        Preconditions.checkArgumentNonNegative(i, "userId");
        synchronized (this.mLock) {
            this.mHasNoDelayedPermBackup.delete(i);
        }
        this.mPermissionControllerManager.stageAndApplyRuntimePermissionsBackup(bArr, UserHandle.of(i));
    }

    public void restoreDelayedRuntimePermissions(String str, final int i) {
        Objects.requireNonNull(str, DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
        Preconditions.checkArgumentNonNegative(i, "userId");
        synchronized (this.mLock) {
            if (this.mHasNoDelayedPermBackup.get(i, false)) {
                return;
            }
            this.mPermissionControllerManager.applyStagedRuntimePermissionBackup(str, UserHandle.of(i), PermissionThread.getExecutor(), new Consumer() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda16
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    PermissionManagerServiceImpl.this.lambda$restoreDelayedRuntimePermissions$5(i, (Boolean) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$restoreDelayedRuntimePermissions$5(int i, Boolean bool) {
        if (bool.booleanValue()) {
            return;
        }
        synchronized (this.mLock) {
            this.mHasNoDelayedPermBackup.put(i, true);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void addOnRuntimePermissionStateChangedListener(PermissionManagerServiceInternal.OnRuntimePermissionStateChangedListener onRuntimePermissionStateChangedListener) {
        synchronized (this.mLock) {
            this.mRuntimePermissionStateChangedListeners.add(onRuntimePermissionStateChangedListener);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void removeOnRuntimePermissionStateChangedListener(PermissionManagerServiceInternal.OnRuntimePermissionStateChangedListener onRuntimePermissionStateChangedListener) {
        synchronized (this.mLock) {
            this.mRuntimePermissionStateChangedListeners.remove(onRuntimePermissionStateChangedListener);
        }
    }

    private void notifyRuntimePermissionStateChanged(String str, int i) {
        FgThread.getHandler().sendMessage(PooledLambda.obtainMessage(new TriConsumer() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda17
            public final void accept(Object obj, Object obj2, Object obj3) {
                ((PermissionManagerServiceImpl) obj).doNotifyRuntimePermissionStateChanged((String) obj2, ((Integer) obj3).intValue());
            }
        }, this, str, Integer.valueOf(i)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doNotifyRuntimePermissionStateChanged(String str, int i) {
        synchronized (this.mLock) {
            if (this.mRuntimePermissionStateChangedListeners.isEmpty()) {
                return;
            }
            ArrayList arrayList = new ArrayList(this.mRuntimePermissionStateChangedListeners);
            int size = arrayList.size();
            for (int i2 = 0; i2 < size; i2++) {
                ((PermissionManagerServiceInternal.OnRuntimePermissionStateChangedListener) arrayList.get(i2)).onRuntimePermissionStateChanged(str, i);
            }
            if (i == 999) {
                this.mPermissionManagerServiceExt.hookNotifyMultiAppPermissionChanged(str);
            }
        }
    }

    private void revokeStoragePermissionsIfScopeExpandedInternal(AndroidPackage androidPackage, AndroidPackage androidPackage2) {
        PermissionInfo permissionInfo;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        boolean z = androidPackage2.getTargetSdkVersion() >= 29 && androidPackage.getTargetSdkVersion() < 29;
        boolean z2 = ((androidPackage2.getTargetSdkVersion() < 29 && androidPackage.getTargetSdkVersion() >= 29) || androidPackage2.isRequestLegacyExternalStorage() || !androidPackage.isRequestLegacyExternalStorage()) ? false : true;
        if (z2 || z) {
            int callingUid = Binder.getCallingUid();
            int[] allUserIds = getAllUserIds();
            int length = allUserIds.length;
            int i7 = 0;
            while (i7 < length) {
                int i8 = allUserIds[i7];
                int size = androidPackage.getRequestedPermissions().size();
                int i9 = 0;
                while (i9 < size) {
                    PermissionInfo permissionInfo2 = getPermissionInfo(androidPackage.getRequestedPermissions().get(i9), 0, androidPackage.getPackageName());
                    if (permissionInfo2 != null) {
                        if (STORAGE_PERMISSIONS.contains(permissionInfo2.name) || READ_MEDIA_AURAL_PERMISSIONS.contains(permissionInfo2.name) || READ_MEDIA_VISUAL_PERMISSIONS.contains(permissionInfo2.name)) {
                            EventLog.writeEvent(1397638484, "171430330", Integer.valueOf(androidPackage.getUid()), "Revoking permission " + permissionInfo2.name + " from package " + androidPackage.getPackageName() + " as either the sdk downgraded " + z + " or newly requested legacy full storage " + z2);
                            try {
                                permissionInfo = permissionInfo2;
                                i = i9;
                                i2 = size;
                                i3 = i8;
                                i4 = i7;
                                i5 = length;
                            } catch (IllegalStateException | SecurityException e) {
                                e = e;
                                permissionInfo = permissionInfo2;
                                i = i9;
                                i2 = size;
                                i3 = i8;
                                i4 = i7;
                                i5 = length;
                            }
                            try {
                                revokeRuntimePermissionInternal(androidPackage.getPackageName(), permissionInfo2.name, false, callingUid, i8, null, this.mDefaultPermissionCallback);
                                i6 = i3;
                            } catch (IllegalStateException | SecurityException e2) {
                                e = e2;
                                StringBuilder sb = new StringBuilder();
                                sb.append("unable to revoke ");
                                sb.append(permissionInfo.name);
                                sb.append(" for ");
                                sb.append(androidPackage.getPackageName());
                                sb.append(" user ");
                                i6 = i3;
                                sb.append(i6);
                                Log.e(TAG, sb.toString(), e);
                                i9 = i + 1;
                                i8 = i6;
                                size = i2;
                                length = i5;
                                i7 = i4;
                            }
                            i9 = i + 1;
                            i8 = i6;
                            size = i2;
                            length = i5;
                            i7 = i4;
                        }
                    }
                    i = i9;
                    i2 = size;
                    i6 = i8;
                    i4 = i7;
                    i5 = length;
                    i9 = i + 1;
                    i8 = i6;
                    size = i2;
                    length = i5;
                    i7 = i4;
                }
                i7++;
            }
        }
    }

    private void revokeSystemAlertWindowIfUpgradedPast23(AndroidPackage androidPackage, AndroidPackage androidPackage2) {
        Permission permission;
        if (androidPackage2.getTargetSdkVersion() >= 23 || androidPackage.getTargetSdkVersion() < 23 || !androidPackage.getRequestedPermissions().contains("android.permission.SYSTEM_ALERT_WINDOW")) {
            return;
        }
        synchronized (this.mLock) {
            permission = this.mRegistry.getPermission("android.permission.SYSTEM_ALERT_WINDOW");
        }
        if (shouldGrantPermissionByProtectionFlags(androidPackage, this.mPackageManagerInt.getPackageStateInternal(androidPackage.getPackageName()), permission, new ArraySet<>()) || shouldGrantPermissionBySignature(androidPackage, permission)) {
            return;
        }
        for (int i : getAllUserIds()) {
            try {
                revokePermissionFromPackageForUser(androidPackage.getPackageName(), "android.permission.SYSTEM_ALERT_WINDOW", false, i, this.mDefaultPermissionCallback);
            } catch (IllegalStateException | SecurityException e) {
                Log.e(TAG, "unable to revoke SYSTEM_ALERT_WINDOW for " + androidPackage.getPackageName() + " user " + i, e);
            }
        }
    }

    private void revokeRuntimePermissionsIfGroupChangedInternal(final AndroidPackage androidPackage, AndroidPackage androidPackage2) {
        int size = ArrayUtils.size(androidPackage2.getPermissions());
        ArrayMap arrayMap = new ArrayMap(size);
        for (int i = 0; i < size; i++) {
            ParsedPermission parsedPermission = androidPackage2.getPermissions().get(i);
            if (parsedPermission.getParsedPermissionGroup() != null) {
                arrayMap.put(parsedPermission.getName(), parsedPermission.getParsedPermissionGroup().getName());
            }
        }
        final int callingUid = Binder.getCallingUid();
        int size2 = ArrayUtils.size(androidPackage.getPermissions());
        for (int i2 = 0; i2 < size2; i2++) {
            ParsedPermission parsedPermission2 = androidPackage.getPermissions().get(i2);
            if ((ParsedPermissionUtils.getProtection(parsedPermission2) & 1) != 0) {
                final String name = parsedPermission2.getName();
                final String name2 = parsedPermission2.getParsedPermissionGroup() == null ? null : parsedPermission2.getParsedPermissionGroup().getName();
                final String str = (String) arrayMap.get(name);
                if (name2 != null && !name2.equals(str)) {
                    final int[] userIds = this.mUserManagerInt.getUserIds();
                    this.mPackageManagerInt.forEachPackage(new Consumer() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda4
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            PermissionManagerServiceImpl.this.lambda$revokeRuntimePermissionsIfGroupChangedInternal$6(userIds, name, androidPackage, str, name2, callingUid, (AndroidPackage) obj);
                        }
                    });
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$revokeRuntimePermissionsIfGroupChangedInternal$6(int[] iArr, String str, AndroidPackage androidPackage, String str2, String str3, int i, AndroidPackage androidPackage2) {
        String packageName = androidPackage2.getPackageName();
        for (int i2 : iArr) {
            if (checkPermission(packageName, str, i2) == 0) {
                EventLog.writeEvent(1397638484, "72710897", Integer.valueOf(androidPackage.getUid()), "Revoking permission " + str + " from package " + packageName + " as the group changed from " + str2 + " to " + str3);
                try {
                    revokeRuntimePermissionInternal(packageName, str, false, i, i2, null, this.mDefaultPermissionCallback);
                } catch (IllegalArgumentException e) {
                    Slog.e(TAG, "Could not revoke " + str + " from " + packageName, e);
                }
            }
        }
    }

    private void revokeRuntimePermissionsIfPermissionDefinitionChangedInternal(List<String> list) {
        final int[] userIds = this.mUserManagerInt.getUserIds();
        int size = list.size();
        final int callingUid = Binder.getCallingUid();
        for (int i = 0; i < size; i++) {
            final String str = list.get(i);
            synchronized (this.mLock) {
                Permission permission = this.mRegistry.getPermission(str);
                if (permission != null && (permission.isInternal() || permission.isRuntime())) {
                    final boolean isInternal = permission.isInternal();
                    this.mPackageManagerInt.forEachPackage(new Consumer() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            PermissionManagerServiceImpl.this.lambda$revokeRuntimePermissionsIfPermissionDefinitionChangedInternal$7(userIds, str, isInternal, callingUid, (AndroidPackage) obj);
                        }
                    });
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$revokeRuntimePermissionsIfPermissionDefinitionChangedInternal$7(int[] iArr, String str, boolean z, int i, AndroidPackage androidPackage) {
        String str2;
        PermissionManagerServiceImpl permissionManagerServiceImpl = this;
        String packageName = androidPackage.getPackageName();
        int uid = androidPackage.getUid();
        if (uid < 10000) {
            return;
        }
        int length = iArr.length;
        int i2 = 0;
        while (i2 < length) {
            int i3 = iArr[i2];
            int checkPermission = permissionManagerServiceImpl.checkPermission(packageName, str, i3);
            int permissionFlags = permissionManagerServiceImpl.getPermissionFlags(packageName, str, i3);
            if (checkPermission == 0 && (32820 & permissionFlags) == 0) {
                int uid2 = UserHandle.getUid(i3, uid);
                if (z) {
                    EventLog.writeEvent(1397638484, "195338390", Integer.valueOf(uid2), "Revoking permission " + str + " from package " + packageName + " due to definition change");
                } else {
                    EventLog.writeEvent(1397638484, "154505240", Integer.valueOf(uid2), "Revoking permission " + str + " from package " + packageName + " due to definition change");
                    EventLog.writeEvent(1397638484, "168319670", Integer.valueOf(uid2), "Revoking permission " + str + " from package " + packageName + " due to definition change");
                }
                Slog.e(TAG, "Revoking permission " + str + " from package " + packageName + " due to definition change");
                try {
                    PermissionCallback permissionCallback = permissionManagerServiceImpl.mDefaultPermissionCallback;
                    str2 = TAG;
                    try {
                        revokeRuntimePermissionInternal(packageName, str, false, i, i3, null, permissionCallback);
                    } catch (Exception e) {
                        e = e;
                        Slog.e(str2, "Could not revoke " + str + " from " + packageName, e);
                        i2++;
                        permissionManagerServiceImpl = this;
                    }
                } catch (Exception e2) {
                    e = e2;
                    str2 = TAG;
                }
            }
            i2++;
            permissionManagerServiceImpl = this;
        }
    }

    private List<String> addAllPermissionsInternal(PackageState packageState, AndroidPackage androidPackage) {
        PermissionInfo generatePermissionInfo;
        Permission permissionTree;
        int size = ArrayUtils.size(androidPackage.getPermissions());
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < size; i++) {
            ParsedPermission parsedPermission = androidPackage.getPermissions().get(i);
            synchronized (this.mLock) {
                if (androidPackage.getTargetSdkVersion() > 22) {
                    ComponentMutateUtils.setParsedPermissionGroup(parsedPermission, this.mRegistry.getPermissionGroup(parsedPermission.getGroup()));
                    if (PackageManagerService.DEBUG_PERMISSIONS && parsedPermission.getGroup() != null && parsedPermission.getParsedPermissionGroup() == null) {
                        Slog.i(TAG, "Permission " + parsedPermission.getName() + " from package " + parsedPermission.getPackageName() + " in an unknown group " + parsedPermission.getGroup());
                    }
                }
                generatePermissionInfo = PackageInfoUtils.generatePermissionInfo(parsedPermission, 128L);
                permissionTree = parsedPermission.isTree() ? this.mRegistry.getPermissionTree(parsedPermission.getName()) : this.mRegistry.getPermission(parsedPermission.getName());
            }
            boolean isOverridingSystemPermission = Permission.isOverridingSystemPermission(permissionTree, generatePermissionInfo, this.mPackageManagerInt);
            synchronized (this.mLock) {
                Permission createOrUpdate = Permission.createOrUpdate(permissionTree, generatePermissionInfo, packageState, this.mRegistry.getPermissionTrees(), isOverridingSystemPermission);
                if (parsedPermission.isTree()) {
                    this.mRegistry.addPermissionTree(createOrUpdate);
                } else {
                    this.mRegistry.addPermission(createOrUpdate);
                }
                if (createOrUpdate.isDefinitionChanged()) {
                    arrayList.add(parsedPermission.getName());
                    createOrUpdate.setDefinitionChanged(false);
                }
            }
        }
        return arrayList;
    }

    private void addAllPermissionGroupsInternal(AndroidPackage androidPackage) {
        synchronized (this.mLock) {
            int size = ArrayUtils.size(androidPackage.getPermissionGroups());
            StringBuilder sb = null;
            for (int i = 0; i < size; i++) {
                ParsedPermissionGroup parsedPermissionGroup = androidPackage.getPermissionGroups().get(i);
                ParsedPermissionGroup permissionGroup = this.mRegistry.getPermissionGroup(parsedPermissionGroup.getName());
                boolean equals = parsedPermissionGroup.getPackageName().equals(permissionGroup == null ? null : permissionGroup.getPackageName());
                if (permissionGroup != null && !equals) {
                    Slog.w(TAG, "Permission group " + parsedPermissionGroup.getName() + " from package " + parsedPermissionGroup.getPackageName() + " ignored: original from " + permissionGroup.getPackageName());
                    if (PackageManagerService.DEBUG_PACKAGE_SCANNING) {
                        if (sb == null) {
                            sb = new StringBuilder(256);
                        } else {
                            sb.append(' ');
                        }
                        sb.append("DUP:");
                        sb.append(parsedPermissionGroup.getName());
                    }
                }
                this.mRegistry.addPermissionGroup(parsedPermissionGroup);
                if (PackageManagerService.DEBUG_PACKAGE_SCANNING) {
                    if (sb == null) {
                        sb = new StringBuilder(256);
                    } else {
                        sb.append(' ');
                    }
                    if (equals) {
                        sb.append("UPD:");
                    }
                    sb.append(parsedPermissionGroup.getName());
                }
            }
            if (sb != null && PackageManagerService.DEBUG_PACKAGE_SCANNING) {
                Log.d(TAG, "  Permission Groups: " + ((Object) sb));
            }
        }
    }

    private void removeAllPermissionsInternal(AndroidPackage androidPackage) {
        synchronized (this.mLock) {
            int size = ArrayUtils.size(androidPackage.getPermissions());
            StringBuilder sb = null;
            for (int i = 0; i < size; i++) {
                ParsedPermission parsedPermission = androidPackage.getPermissions().get(i);
                Permission permission = this.mRegistry.getPermission(parsedPermission.getName());
                if (permission == null) {
                    permission = this.mRegistry.getPermissionTree(parsedPermission.getName());
                }
                if (permission != null && permission.isPermission(parsedPermission)) {
                    permission.setPermissionInfo(null);
                    if (PackageManagerService.DEBUG_REMOVE) {
                        if (sb == null) {
                            sb = new StringBuilder(256);
                        } else {
                            sb.append(' ');
                        }
                        sb.append(parsedPermission.getName());
                    }
                }
                if (ParsedPermissionUtils.isAppOp(parsedPermission)) {
                    this.mRegistry.removeAppOpPermissionPackage(parsedPermission.getName(), androidPackage.getPackageName());
                }
            }
            if (sb != null && PackageManagerService.DEBUG_REMOVE) {
                Log.d(TAG, "  Permissions: " + ((Object) sb));
            }
            int size2 = androidPackage.getRequestedPermissions().size();
            for (int i2 = 0; i2 < size2; i2++) {
                String str = androidPackage.getRequestedPermissions().get(i2);
                Permission permission2 = this.mRegistry.getPermission(str);
                if (permission2 != null && permission2.isAppOp()) {
                    this.mRegistry.removeAppOpPermissionPackage(str, androidPackage.getPackageName());
                }
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onUserRemoved(int i) {
        Preconditions.checkArgumentNonNegative(i, "userId");
        synchronized (this.mLock) {
            this.mState.removeUserState(i);
        }
    }

    private Set<String> getGrantedPermissionsInternal(String str, final int i) {
        final PackageStateInternal packageStateInternal = this.mPackageManagerInt.getPackageStateInternal(str);
        if (packageStateInternal == null) {
            return Collections.emptySet();
        }
        synchronized (this.mLock) {
            UidPermissionState uidStateLocked = getUidStateLocked(packageStateInternal, i);
            if (uidStateLocked == null) {
                Slog.e(TAG, "Missing permissions state for " + str + " and user " + i);
                return Collections.emptySet();
            }
            if (!packageStateInternal.getUserStateOrDefault(i).isInstantApp()) {
                return uidStateLocked.getGrantedPermissions();
            }
            ArraySet arraySet = new ArraySet(uidStateLocked.getGrantedPermissions());
            arraySet.removeIf(new Predicate() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda19
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$getGrantedPermissionsInternal$8;
                    lambda$getGrantedPermissionsInternal$8 = PermissionManagerServiceImpl.this.lambda$getGrantedPermissionsInternal$8(i, packageStateInternal, (String) obj);
                    return lambda$getGrantedPermissionsInternal$8;
                }
            });
            return arraySet;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getGrantedPermissionsInternal$8(int i, PackageStateInternal packageStateInternal, String str) {
        Permission permission = this.mRegistry.getPermission(str);
        if (permission == null) {
            return true;
        }
        if (permission.isInstant()) {
            return false;
        }
        EventLog.writeEvent(1397638484, "140256621", Integer.valueOf(UserHandle.getUid(i, packageStateInternal.getAppId())), str);
        return true;
    }

    private int[] getPermissionGidsInternal(String str, int i) {
        synchronized (this.mLock) {
            Permission permission = this.mRegistry.getPermission(str);
            if (permission == null) {
                return EmptyArray.INT;
            }
            return permission.computeGids(i);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:239:0x0411, code lost:
    
        if (r4 == false) goto L194;
     */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x0566, code lost:
    
        if (r15.isRole() == false) goto L295;
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x059e, code lost:
    
        if (com.android.internal.util.CollectionUtils.contains(r9, r7) == false) goto L307;
     */
    /* JADX WARN: Code restructure failed: missing block: B:311:0x05b0, code lost:
    
        if (r13.isPermissionGranted(r7) == false) goto L319;
     */
    /* JADX WARN: Code restructure failed: missing block: B:330:0x05aa, code lost:
    
        if (r15.isRole() == false) goto L319;
     */
    /* JADX WARN: Code restructure failed: missing block: B:368:0x0697, code lost:
    
        if (r12.isSystem() == false) goto L352;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0236 A[Catch: all -> 0x0716, TryCatch #1 {all -> 0x0716, blocks: (B:71:0x0150, B:73:0x016c, B:74:0x0170, B:76:0x0176, B:79:0x018b, B:81:0x0199, B:83:0x019f, B:85:0x01a5, B:87:0x01ab, B:91:0x01ba, B:92:0x01b1, B:100:0x01c7, B:102:0x01da, B:104:0x01e8, B:105:0x0202, B:107:0x0236, B:110:0x0255, B:112:0x0259, B:115:0x025f, B:118:0x0292, B:124:0x064a, B:125:0x029c, B:127:0x02a0, B:128:0x02c8, B:130:0x02ce, B:132:0x02d8, B:134:0x02df, B:135:0x02f8, B:138:0x0300, B:140:0x0304, B:141:0x0327, B:143:0x0331, B:144:0x033a, B:146:0x0340, B:148:0x0346, B:150:0x034c, B:152:0x0356, B:154:0x035c, B:155:0x0368, B:157:0x036c, B:158:0x0393, B:160:0x0399, B:162:0x039f, B:165:0x03a7, B:167:0x03ad, B:169:0x03c1, B:170:0x03cd, B:173:0x03de, B:181:0x03f7, B:183:0x03fd, B:188:0x0413, B:189:0x041a, B:191:0x0422, B:193:0x0426, B:194:0x042a, B:196:0x042e, B:198:0x0438, B:200:0x0452, B:202:0x0456, B:204:0x045c, B:206:0x0461, B:208:0x0467, B:216:0x04b3, B:218:0x04b7, B:222:0x04be, B:223:0x04c8, B:229:0x0445, B:231:0x044b, B:241:0x0470, B:243:0x047c, B:245:0x0482, B:246:0x0487, B:248:0x0491, B:256:0x04a2, B:262:0x04d1, B:263:0x050e, B:268:0x05b2, B:272:0x0636, B:274:0x063c, B:275:0x0642, B:280:0x0527, B:282:0x052d, B:284:0x0533, B:287:0x0543, B:289:0x054b, B:291:0x0551, B:294:0x055c, B:296:0x0562, B:298:0x057a, B:300:0x0580, B:302:0x0586, B:304:0x058c, B:306:0x0594, B:308:0x059a, B:310:0x05ac, B:312:0x05c7, B:314:0x05cb, B:316:0x05d5, B:318:0x05db, B:321:0x05e9, B:322:0x062d, B:327:0x05a0, B:329:0x05a6, B:332:0x0568, B:351:0x0699, B:353:0x069f, B:357:0x06af, B:359:0x06ba, B:365:0x0687, B:367:0x0693, B:369:0x01f2, B:371:0x01f8), top: B:70:0x0150 }] */
    /* JADX WARN: Removed duplicated region for block: B:213:0x04ab A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:218:0x04b7 A[Catch: all -> 0x0716, TryCatch #1 {all -> 0x0716, blocks: (B:71:0x0150, B:73:0x016c, B:74:0x0170, B:76:0x0176, B:79:0x018b, B:81:0x0199, B:83:0x019f, B:85:0x01a5, B:87:0x01ab, B:91:0x01ba, B:92:0x01b1, B:100:0x01c7, B:102:0x01da, B:104:0x01e8, B:105:0x0202, B:107:0x0236, B:110:0x0255, B:112:0x0259, B:115:0x025f, B:118:0x0292, B:124:0x064a, B:125:0x029c, B:127:0x02a0, B:128:0x02c8, B:130:0x02ce, B:132:0x02d8, B:134:0x02df, B:135:0x02f8, B:138:0x0300, B:140:0x0304, B:141:0x0327, B:143:0x0331, B:144:0x033a, B:146:0x0340, B:148:0x0346, B:150:0x034c, B:152:0x0356, B:154:0x035c, B:155:0x0368, B:157:0x036c, B:158:0x0393, B:160:0x0399, B:162:0x039f, B:165:0x03a7, B:167:0x03ad, B:169:0x03c1, B:170:0x03cd, B:173:0x03de, B:181:0x03f7, B:183:0x03fd, B:188:0x0413, B:189:0x041a, B:191:0x0422, B:193:0x0426, B:194:0x042a, B:196:0x042e, B:198:0x0438, B:200:0x0452, B:202:0x0456, B:204:0x045c, B:206:0x0461, B:208:0x0467, B:216:0x04b3, B:218:0x04b7, B:222:0x04be, B:223:0x04c8, B:229:0x0445, B:231:0x044b, B:241:0x0470, B:243:0x047c, B:245:0x0482, B:246:0x0487, B:248:0x0491, B:256:0x04a2, B:262:0x04d1, B:263:0x050e, B:268:0x05b2, B:272:0x0636, B:274:0x063c, B:275:0x0642, B:280:0x0527, B:282:0x052d, B:284:0x0533, B:287:0x0543, B:289:0x054b, B:291:0x0551, B:294:0x055c, B:296:0x0562, B:298:0x057a, B:300:0x0580, B:302:0x0586, B:304:0x058c, B:306:0x0594, B:308:0x059a, B:310:0x05ac, B:312:0x05c7, B:314:0x05cb, B:316:0x05d5, B:318:0x05db, B:321:0x05e9, B:322:0x062d, B:327:0x05a0, B:329:0x05a6, B:332:0x0568, B:351:0x0699, B:353:0x069f, B:357:0x06af, B:359:0x06ba, B:365:0x0687, B:367:0x0693, B:369:0x01f2, B:371:0x01f8), top: B:70:0x0150 }] */
    /* JADX WARN: Removed duplicated region for block: B:222:0x04be A[Catch: all -> 0x0716, TryCatch #1 {all -> 0x0716, blocks: (B:71:0x0150, B:73:0x016c, B:74:0x0170, B:76:0x0176, B:79:0x018b, B:81:0x0199, B:83:0x019f, B:85:0x01a5, B:87:0x01ab, B:91:0x01ba, B:92:0x01b1, B:100:0x01c7, B:102:0x01da, B:104:0x01e8, B:105:0x0202, B:107:0x0236, B:110:0x0255, B:112:0x0259, B:115:0x025f, B:118:0x0292, B:124:0x064a, B:125:0x029c, B:127:0x02a0, B:128:0x02c8, B:130:0x02ce, B:132:0x02d8, B:134:0x02df, B:135:0x02f8, B:138:0x0300, B:140:0x0304, B:141:0x0327, B:143:0x0331, B:144:0x033a, B:146:0x0340, B:148:0x0346, B:150:0x034c, B:152:0x0356, B:154:0x035c, B:155:0x0368, B:157:0x036c, B:158:0x0393, B:160:0x0399, B:162:0x039f, B:165:0x03a7, B:167:0x03ad, B:169:0x03c1, B:170:0x03cd, B:173:0x03de, B:181:0x03f7, B:183:0x03fd, B:188:0x0413, B:189:0x041a, B:191:0x0422, B:193:0x0426, B:194:0x042a, B:196:0x042e, B:198:0x0438, B:200:0x0452, B:202:0x0456, B:204:0x045c, B:206:0x0461, B:208:0x0467, B:216:0x04b3, B:218:0x04b7, B:222:0x04be, B:223:0x04c8, B:229:0x0445, B:231:0x044b, B:241:0x0470, B:243:0x047c, B:245:0x0482, B:246:0x0487, B:248:0x0491, B:256:0x04a2, B:262:0x04d1, B:263:0x050e, B:268:0x05b2, B:272:0x0636, B:274:0x063c, B:275:0x0642, B:280:0x0527, B:282:0x052d, B:284:0x0533, B:287:0x0543, B:289:0x054b, B:291:0x0551, B:294:0x055c, B:296:0x0562, B:298:0x057a, B:300:0x0580, B:302:0x0586, B:304:0x058c, B:306:0x0594, B:308:0x059a, B:310:0x05ac, B:312:0x05c7, B:314:0x05cb, B:316:0x05d5, B:318:0x05db, B:321:0x05e9, B:322:0x062d, B:327:0x05a0, B:329:0x05a6, B:332:0x0568, B:351:0x0699, B:353:0x069f, B:357:0x06af, B:359:0x06ba, B:365:0x0687, B:367:0x0693, B:369:0x01f2, B:371:0x01f8), top: B:70:0x0150 }] */
    /* JADX WARN: Removed duplicated region for block: B:224:0x04c5  */
    /* JADX WARN: Removed duplicated region for block: B:270:0x05b8  */
    /* JADX WARN: Removed duplicated region for block: B:274:0x063c A[Catch: all -> 0x0716, TryCatch #1 {all -> 0x0716, blocks: (B:71:0x0150, B:73:0x016c, B:74:0x0170, B:76:0x0176, B:79:0x018b, B:81:0x0199, B:83:0x019f, B:85:0x01a5, B:87:0x01ab, B:91:0x01ba, B:92:0x01b1, B:100:0x01c7, B:102:0x01da, B:104:0x01e8, B:105:0x0202, B:107:0x0236, B:110:0x0255, B:112:0x0259, B:115:0x025f, B:118:0x0292, B:124:0x064a, B:125:0x029c, B:127:0x02a0, B:128:0x02c8, B:130:0x02ce, B:132:0x02d8, B:134:0x02df, B:135:0x02f8, B:138:0x0300, B:140:0x0304, B:141:0x0327, B:143:0x0331, B:144:0x033a, B:146:0x0340, B:148:0x0346, B:150:0x034c, B:152:0x0356, B:154:0x035c, B:155:0x0368, B:157:0x036c, B:158:0x0393, B:160:0x0399, B:162:0x039f, B:165:0x03a7, B:167:0x03ad, B:169:0x03c1, B:170:0x03cd, B:173:0x03de, B:181:0x03f7, B:183:0x03fd, B:188:0x0413, B:189:0x041a, B:191:0x0422, B:193:0x0426, B:194:0x042a, B:196:0x042e, B:198:0x0438, B:200:0x0452, B:202:0x0456, B:204:0x045c, B:206:0x0461, B:208:0x0467, B:216:0x04b3, B:218:0x04b7, B:222:0x04be, B:223:0x04c8, B:229:0x0445, B:231:0x044b, B:241:0x0470, B:243:0x047c, B:245:0x0482, B:246:0x0487, B:248:0x0491, B:256:0x04a2, B:262:0x04d1, B:263:0x050e, B:268:0x05b2, B:272:0x0636, B:274:0x063c, B:275:0x0642, B:280:0x0527, B:282:0x052d, B:284:0x0533, B:287:0x0543, B:289:0x054b, B:291:0x0551, B:294:0x055c, B:296:0x0562, B:298:0x057a, B:300:0x0580, B:302:0x0586, B:304:0x058c, B:306:0x0594, B:308:0x059a, B:310:0x05ac, B:312:0x05c7, B:314:0x05cb, B:316:0x05d5, B:318:0x05db, B:321:0x05e9, B:322:0x062d, B:327:0x05a0, B:329:0x05a6, B:332:0x0568, B:351:0x0699, B:353:0x069f, B:357:0x06af, B:359:0x06ba, B:365:0x0687, B:367:0x0693, B:369:0x01f2, B:371:0x01f8), top: B:70:0x0150 }] */
    /* JADX WARN: Removed duplicated region for block: B:277:0x0641  */
    /* JADX WARN: Removed duplicated region for block: B:278:0x05bf  */
    /* JADX WARN: Removed duplicated region for block: B:334:0x056e  */
    /* JADX WARN: Removed duplicated region for block: B:355:0x06ab  */
    /* JADX WARN: Removed duplicated region for block: B:363:0x06b9  */
    /* JADX WARN: Removed duplicated region for block: B:364:0x06a8  */
    /* JADX WARN: Removed duplicated region for block: B:367:0x0693 A[Catch: all -> 0x0716, TryCatch #1 {all -> 0x0716, blocks: (B:71:0x0150, B:73:0x016c, B:74:0x0170, B:76:0x0176, B:79:0x018b, B:81:0x0199, B:83:0x019f, B:85:0x01a5, B:87:0x01ab, B:91:0x01ba, B:92:0x01b1, B:100:0x01c7, B:102:0x01da, B:104:0x01e8, B:105:0x0202, B:107:0x0236, B:110:0x0255, B:112:0x0259, B:115:0x025f, B:118:0x0292, B:124:0x064a, B:125:0x029c, B:127:0x02a0, B:128:0x02c8, B:130:0x02ce, B:132:0x02d8, B:134:0x02df, B:135:0x02f8, B:138:0x0300, B:140:0x0304, B:141:0x0327, B:143:0x0331, B:144:0x033a, B:146:0x0340, B:148:0x0346, B:150:0x034c, B:152:0x0356, B:154:0x035c, B:155:0x0368, B:157:0x036c, B:158:0x0393, B:160:0x0399, B:162:0x039f, B:165:0x03a7, B:167:0x03ad, B:169:0x03c1, B:170:0x03cd, B:173:0x03de, B:181:0x03f7, B:183:0x03fd, B:188:0x0413, B:189:0x041a, B:191:0x0422, B:193:0x0426, B:194:0x042a, B:196:0x042e, B:198:0x0438, B:200:0x0452, B:202:0x0456, B:204:0x045c, B:206:0x0461, B:208:0x0467, B:216:0x04b3, B:218:0x04b7, B:222:0x04be, B:223:0x04c8, B:229:0x0445, B:231:0x044b, B:241:0x0470, B:243:0x047c, B:245:0x0482, B:246:0x0487, B:248:0x0491, B:256:0x04a2, B:262:0x04d1, B:263:0x050e, B:268:0x05b2, B:272:0x0636, B:274:0x063c, B:275:0x0642, B:280:0x0527, B:282:0x052d, B:284:0x0533, B:287:0x0543, B:289:0x054b, B:291:0x0551, B:294:0x055c, B:296:0x0562, B:298:0x057a, B:300:0x0580, B:302:0x0586, B:304:0x058c, B:306:0x0594, B:308:0x059a, B:310:0x05ac, B:312:0x05c7, B:314:0x05cb, B:316:0x05d5, B:318:0x05db, B:321:0x05e9, B:322:0x062d, B:327:0x05a0, B:329:0x05a6, B:332:0x0568, B:351:0x0699, B:353:0x069f, B:357:0x06af, B:359:0x06ba, B:365:0x0687, B:367:0x0693, B:369:0x01f2, B:371:0x01f8), top: B:70:0x0150 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void restorePermissionState(AndroidPackage androidPackage, boolean z, String str, PermissionCallback permissionCallback, int i) {
        int[] iArr;
        ArraySet arraySet;
        ArraySet<String> arraySet2;
        ArraySet arraySet3;
        Collection collection;
        Collection collection2;
        int i2;
        ArraySet arraySet4;
        ArraySet arraySet5;
        SparseBooleanArray sparseBooleanArray;
        UidPermissionState uidPermissionState;
        int i3;
        UserPermissionState userPermissionState;
        boolean z2;
        UserPermissionState userPermissionState2;
        boolean z3;
        int[] iArr2;
        int i4;
        int[] iArr3;
        ArraySet<String> arraySet6;
        UserPermissionState userPermissionState3;
        SparseBooleanArray sparseBooleanArray2;
        ArraySet arraySet7;
        ArraySet<String> arraySet8;
        ArraySet arraySet9;
        ArraySet arraySet10;
        AndroidPackage androidPackage2;
        String str2;
        boolean isPermissionGranted;
        boolean z4;
        int[] iArr4;
        int i5;
        boolean z5;
        boolean z6;
        Permission permission;
        AndroidPackage androidPackage3 = androidPackage;
        boolean z7 = z;
        String str3 = str;
        PackageStateInternal packageStateInternal = this.mPackageManagerInt.getPackageStateInternal(androidPackage.getPackageName());
        if (packageStateInternal == null) {
            return;
        }
        if (i == -1) {
            iArr = getAllUserIds();
        } else {
            iArr = new int[]{i};
        }
        int[] iArr5 = iArr;
        int[] iArr6 = EMPTY_INT_ARRAY;
        ArraySet<String> arraySet11 = new ArraySet<>();
        List<String> requestedPermissions = androidPackage.getRequestedPermissions();
        int size = requestedPermissions.size();
        ArraySet arraySet12 = null;
        ArraySet arraySet13 = null;
        ArraySet arraySet14 = null;
        int i6 = 0;
        while (i6 < size) {
            String str4 = androidPackage.getRequestedPermissions().get(i6);
            int[] iArr7 = iArr6;
            synchronized (this.mLock) {
                permission = this.mRegistry.getPermission(str4);
            }
            if (permission != null) {
                if (permission.isPrivileged() && checkPrivilegedPermissionAllowlist(androidPackage3, packageStateInternal, permission)) {
                    if (arraySet14 == null) {
                        arraySet14 = new ArraySet();
                    }
                    arraySet14.add(str4);
                }
                if (permission.isSignature() && (shouldGrantPermissionBySignature(androidPackage3, permission) || shouldGrantPermissionByProtectionFlags(androidPackage3, packageStateInternal, permission, arraySet11))) {
                    if (arraySet12 == null) {
                        arraySet12 = new ArraySet();
                    }
                    arraySet12.add(str4);
                }
                if (permission.isInternal() && shouldGrantPermissionByProtectionFlags(androidPackage3, packageStateInternal, permission, arraySet11)) {
                    if (arraySet13 == null) {
                        arraySet13 = new ArraySet();
                    }
                    arraySet13.add(str4);
                }
            }
            i6++;
            iArr6 = iArr7;
        }
        int[] iArr8 = iArr6;
        SparseBooleanArray sparseBooleanArray3 = new SparseBooleanArray();
        if (this.mPermissionPolicyInternal != null) {
            int length = iArr5.length;
            int i7 = 0;
            while (i7 < length) {
                int i8 = iArr5[i7];
                int i9 = length;
                if (this.mPermissionPolicyInternal.isInitialized(i8)) {
                    sparseBooleanArray3.put(i8, true);
                }
                i7++;
                length = i9;
            }
        }
        if (!packageStateInternal.hasSharedUser()) {
            Collection requestedPermissions2 = androidPackage.getRequestedPermissions();
            Collection implicitPermissions = androidPackage.getImplicitPermissions();
            i2 = androidPackage.getTargetSdkVersion();
            collection2 = implicitPermissions;
            arraySet3 = arraySet12;
            arraySet = arraySet13;
            arraySet2 = arraySet11;
            collection = requestedPermissions2;
        } else {
            Collection arraySet15 = new ArraySet();
            Collection arraySet16 = new ArraySet();
            arraySet = arraySet13;
            ArraySet sharedUserPackages = this.mPackageManagerInt.getSharedUserPackages(packageStateInternal.getSharedUserAppId());
            int size2 = sharedUserPackages.size();
            arraySet2 = arraySet11;
            int i10 = 0;
            arraySet3 = arraySet12;
            int i11 = 10000;
            while (i10 < size2) {
                AndroidPackage androidPackage4 = ((PackageStateInternal) sharedUserPackages.valueAt(i10)).getAndroidPackage();
                if (androidPackage4 == null) {
                    arraySet4 = sharedUserPackages;
                } else {
                    arraySet4 = sharedUserPackages;
                    arraySet15.addAll(androidPackage4.getRequestedPermissions());
                    arraySet16.addAll(androidPackage4.getImplicitPermissions());
                    i11 = Math.min(i11, androidPackage4.getTargetSdkVersion());
                }
                i10++;
                sharedUserPackages = arraySet4;
            }
            collection = arraySet15;
            collection2 = arraySet16;
            i2 = i11;
        }
        Object obj = this.mLock;
        synchronized (obj) {
            try {
                try {
                    int length2 = iArr5.length;
                    String str5 = obj;
                    int[] iArr9 = iArr8;
                    boolean z8 = false;
                    int i12 = 0;
                    boolean z9 = false;
                    while (i12 < length2) {
                        int i13 = length2;
                        try {
                            int i14 = iArr5[i12];
                            int i15 = i12;
                            UserPermissionState orCreateUserState = this.mState.getOrCreateUserState(i14);
                            boolean z10 = z8;
                            UidPermissionState orCreateUidState = orCreateUserState.getOrCreateUidState(packageStateInternal.getAppId());
                            int[] iArr10 = iArr5;
                            if (orCreateUidState.isMissing()) {
                                Iterator<String> it = collection.iterator();
                                while (it.hasNext()) {
                                    ArraySet arraySet17 = arraySet14;
                                    Permission permission2 = this.mRegistry.getPermission(it.next());
                                    if (permission2 == null) {
                                        arraySet14 = arraySet17;
                                    } else {
                                        SparseBooleanArray sparseBooleanArray4 = sparseBooleanArray3;
                                        if (Objects.equals(permission2.getPackageName(), PackageManagerService.PLATFORM_PACKAGE_NAME) && permission2.isRuntime() && !permission2.isRemoved()) {
                                            if (permission2.isHardOrSoftRestricted() || permission2.isImmutablyRestricted()) {
                                                orCreateUidState.updatePermissionFlags(permission2, 8192, 8192);
                                            }
                                            if (i2 < 23) {
                                                orCreateUidState.updatePermissionFlags(permission2, 72, 72);
                                                orCreateUidState.grantPermission(permission2);
                                            }
                                        }
                                        arraySet14 = arraySet17;
                                        sparseBooleanArray3 = sparseBooleanArray4;
                                    }
                                }
                                arraySet5 = arraySet14;
                                sparseBooleanArray = sparseBooleanArray3;
                                orCreateUidState.setMissing(false);
                                iArr9 = ArrayUtils.appendInt(iArr9, i14);
                            } else {
                                arraySet5 = arraySet14;
                                sparseBooleanArray = sparseBooleanArray3;
                            }
                            if (z7) {
                                orCreateUserState.setInstallPermissionsFixed(packageStateInternal.getPackageName(), false);
                                if (!packageStateInternal.hasSharedUser()) {
                                    UidPermissionState uidPermissionState2 = new UidPermissionState(orCreateUidState);
                                    orCreateUidState.reset();
                                    uidPermissionState = uidPermissionState2;
                                } else if (revokeUnusedSharedUserPermissionsLocked(collection, orCreateUidState)) {
                                    iArr9 = ArrayUtils.appendInt(iArr9, i14);
                                    uidPermissionState = orCreateUidState;
                                    z9 = true;
                                }
                                ArraySet<String> arraySet18 = new ArraySet<>();
                                StringBuilder sb = new StringBuilder();
                                int[] iArr11 = iArr9;
                                sb.append(androidPackage.getPackageName());
                                sb.append("(");
                                sb.append(androidPackage.getUid());
                                sb.append(")");
                                String sb2 = sb.toString();
                                int i16 = i2;
                                i3 = 0;
                                boolean z11 = false;
                                Collection collection3 = collection;
                                int[] iArr12 = iArr11;
                                while (i3 < size) {
                                    int i17 = size;
                                    String str6 = requestedPermissions.get(i3);
                                    List<String> list = requestedPermissions;
                                    Permission permission3 = this.mRegistry.getPermission(str6);
                                    boolean z12 = z11;
                                    boolean z13 = androidPackage.getTargetSdkVersion() >= 23;
                                    if (PackageManagerService.DEBUG_PERMISSIONS && PackageManagerService.DEBUG_INSTALL && permission3 != null) {
                                        i4 = i3;
                                        StringBuilder sb3 = new StringBuilder();
                                        iArr3 = iArr12;
                                        sb3.append("Package ");
                                        sb3.append(sb2);
                                        sb3.append(" checking ");
                                        sb3.append(str6);
                                        sb3.append(": ");
                                        sb3.append(permission3);
                                        Log.i(TAG, sb3.toString());
                                    } else {
                                        i4 = i3;
                                        iArr3 = iArr12;
                                    }
                                    if (permission3 == null) {
                                        if ((str3 == null || str3.equals(androidPackage.getPackageName())) && PackageManagerService.DEBUG_PERMISSIONS) {
                                            Slog.i(TAG, "Unknown permission " + str6 + " in package " + sb2);
                                        }
                                    } else {
                                        if (!uidPermissionState.hasPermissionState(str6) && androidPackage.getImplicitPermissions().contains(str6)) {
                                            arraySet18.add(str6);
                                            if (PackageManagerService.DEBUG_PERMISSIONS) {
                                                Slog.i(TAG, str6 + " is newly added for " + sb2);
                                            }
                                        }
                                        if (permission3.isRuntimeOnly() && !z13) {
                                            if (PackageManagerService.DEBUG_PERMISSIONS) {
                                                Log.i(TAG, "Denying runtime-only permission " + permission3.getName() + " for package " + sb2);
                                            }
                                        } else {
                                            String name = permission3.getName();
                                            if (permission3.isAppOp()) {
                                                this.mRegistry.addAppOpPermissionPackage(name, androidPackage.getPackageName());
                                            }
                                            boolean hookShouldGrantNormalPermission = (!permission3.isNormal() || uidPermissionState.isPermissionGranted(name) || packageStateInternal.isSystem() || !orCreateUserState.areInstallPermissionsFixed(packageStateInternal.getPackageName()) || isCompatPlatformPermissionForPackage(name, androidPackage3)) ? true : this.mPermissionManagerServiceExt.hookShouldGrantNormalPermission(androidPackage.getPackageName());
                                            if (PackageManagerService.DEBUG_PERMISSIONS) {
                                                arraySet6 = arraySet18;
                                                Slog.i(TAG, "Considering granting permission " + name + " to package " + androidPackage.getPackageName());
                                            } else {
                                                arraySet6 = arraySet18;
                                            }
                                            if (!permission3.isNormal() && !permission3.isSignature() && !permission3.isInternal()) {
                                                if (permission3.isRuntime()) {
                                                    boolean isHardRestricted = permission3.isHardRestricted();
                                                    boolean isSoftRestricted = permission3.isSoftRestricted();
                                                    SparseBooleanArray sparseBooleanArray5 = sparseBooleanArray;
                                                    boolean z14 = sparseBooleanArray5.get(i14);
                                                    PermissionState permissionState = uidPermissionState.getPermissionState(name);
                                                    if (permissionState != null) {
                                                        sparseBooleanArray2 = sparseBooleanArray5;
                                                        i5 = permissionState.getFlags();
                                                    } else {
                                                        sparseBooleanArray2 = sparseBooleanArray5;
                                                        i5 = 0;
                                                    }
                                                    userPermissionState3 = orCreateUserState;
                                                    boolean z15 = (uidPermissionState.getPermissionFlags(permission3.getName()) & 14336) != 0;
                                                    boolean z16 = (uidPermissionState.getPermissionFlags(permission3.getName()) & 16384) != 0;
                                                    if (z13) {
                                                        if (z14 && isHardRestricted) {
                                                            if (!z15) {
                                                                z6 = permissionState != null && permissionState.isGranted() && orCreateUidState.revokePermission(permission3);
                                                                if (!z16) {
                                                                    i5 |= 16384;
                                                                    z6 = true;
                                                                }
                                                            }
                                                            z6 = false;
                                                        } else {
                                                            if (z14) {
                                                                if (isSoftRestricted) {
                                                                    if (!z15) {
                                                                    }
                                                                }
                                                            }
                                                            z6 = false;
                                                        }
                                                        List<String> list2 = NOTIFICATION_PERMISSIONS;
                                                        if (!list2.contains(name) && (i5 & 64) != 0) {
                                                            i5 &= -65;
                                                            z6 = true;
                                                        }
                                                        if ((i5 & 8) == 0 || isPermissionSplitFromNonRuntime(str6, androidPackage.getTargetSdkVersion())) {
                                                            if ((!z14 || !isHardRestricted || z15) && permissionState != null && permissionState.isGranted() && !orCreateUidState.grantPermission(permission3)) {
                                                            }
                                                            if (this.mIsLeanback && list2.contains(str6)) {
                                                                orCreateUidState.grantPermission(permission3);
                                                                if ((permissionState != null || !permissionState.isGranted()) && orCreateUidState.grantPermission(permission3)) {
                                                                    z6 = true;
                                                                }
                                                            }
                                                            if (z14 && (((!isHardRestricted && !isSoftRestricted) || z15) && z16)) {
                                                                int i18 = i5 & (-16385);
                                                                if (!z13) {
                                                                    i18 |= 64;
                                                                }
                                                                i5 = i18;
                                                                z6 = true;
                                                            }
                                                            iArr12 = !z6 ? ArrayUtils.appendInt(iArr3, i14) : iArr3;
                                                            orCreateUidState.updatePermissionFlags(permission3, 261119, i5);
                                                            androidPackage2 = androidPackage;
                                                        } else {
                                                            i5 &= -9;
                                                        }
                                                        z6 = true;
                                                        if (this.mIsLeanback) {
                                                            orCreateUidState.grantPermission(permission3);
                                                            if (permissionState != null) {
                                                            }
                                                            z6 = true;
                                                        }
                                                        if (z14) {
                                                            int i182 = i5 & (-16385);
                                                            if (!z13) {
                                                            }
                                                            i5 = i182;
                                                            z6 = true;
                                                        }
                                                        if (!z6) {
                                                        }
                                                        orCreateUidState.updatePermissionFlags(permission3, 261119, i5);
                                                        androidPackage2 = androidPackage;
                                                    } else {
                                                        if (permissionState == null && PackageManagerService.PLATFORM_PACKAGE_NAME.equals(permission3.getPackageName()) && !permission3.isRemoved()) {
                                                            i5 |= 72;
                                                            z5 = true;
                                                        } else {
                                                            z5 = false;
                                                        }
                                                        if (!orCreateUidState.isPermissionGranted(permission3.getName()) && orCreateUidState.grantPermission(permission3)) {
                                                            z5 = true;
                                                        }
                                                        if (!z14 || ((!isHardRestricted && !isSoftRestricted) || z15 || z16)) {
                                                            z6 = z5;
                                                            if (z14) {
                                                            }
                                                            if (!z6) {
                                                            }
                                                            orCreateUidState.updatePermissionFlags(permission3, 261119, i5);
                                                            androidPackage2 = androidPackage;
                                                        } else {
                                                            i5 |= 16384;
                                                            z6 = true;
                                                            if (z14) {
                                                            }
                                                            if (!z6) {
                                                            }
                                                            orCreateUidState.updatePermissionFlags(permission3, 261119, i5);
                                                            androidPackage2 = androidPackage;
                                                        }
                                                    }
                                                    arraySet10 = arraySet;
                                                    arraySet7 = arraySet3;
                                                    arraySet8 = arraySet2;
                                                    arraySet9 = arraySet5;
                                                    z11 = z12;
                                                    str2 = sb2;
                                                    i3 = i4 + 1;
                                                    arraySet5 = arraySet9;
                                                    arraySet2 = arraySet8;
                                                    arraySet = arraySet10;
                                                    sb2 = str2;
                                                    requestedPermissions = list;
                                                    arraySet18 = arraySet6;
                                                    sparseBooleanArray = sparseBooleanArray2;
                                                    orCreateUserState = userPermissionState3;
                                                    str3 = str;
                                                    arraySet3 = arraySet7;
                                                    androidPackage3 = androidPackage2;
                                                    size = i17;
                                                } else {
                                                    userPermissionState3 = orCreateUserState;
                                                    sparseBooleanArray2 = sparseBooleanArray;
                                                    iArr4 = iArr3;
                                                    Slog.wtf(LOG_TAG, "Unknown permission protection " + permission3.getProtection() + " for permission " + permission3.getName());
                                                    androidPackage2 = androidPackage;
                                                    iArr12 = iArr4;
                                                    arraySet10 = arraySet;
                                                    arraySet7 = arraySet3;
                                                    arraySet8 = arraySet2;
                                                    arraySet9 = arraySet5;
                                                    z11 = z12;
                                                    str2 = sb2;
                                                    i3 = i4 + 1;
                                                    arraySet5 = arraySet9;
                                                    arraySet2 = arraySet8;
                                                    arraySet = arraySet10;
                                                    sb2 = str2;
                                                    requestedPermissions = list;
                                                    arraySet18 = arraySet6;
                                                    sparseBooleanArray = sparseBooleanArray2;
                                                    orCreateUserState = userPermissionState3;
                                                    str3 = str;
                                                    arraySet3 = arraySet7;
                                                    androidPackage3 = androidPackage2;
                                                    size = i17;
                                                }
                                            }
                                            userPermissionState3 = orCreateUserState;
                                            sparseBooleanArray2 = sparseBooleanArray;
                                            int[] iArr13 = iArr3;
                                            if (permission3.isNormal() && hookShouldGrantNormalPermission) {
                                                arraySet10 = arraySet;
                                                arraySet7 = arraySet3;
                                                arraySet8 = arraySet2;
                                                arraySet9 = arraySet5;
                                                if (orCreateUidState.grantPermission(permission3)) {
                                                    androidPackage2 = androidPackage;
                                                    str2 = sb2;
                                                    z4 = true;
                                                    PermissionState permissionState2 = uidPermissionState.getPermissionState(name);
                                                    orCreateUidState.updatePermissionFlags(permission3, 261119, permissionState2 == null ? permissionState2.getFlags() : 0);
                                                    z11 = z4;
                                                    iArr12 = iArr13;
                                                    i3 = i4 + 1;
                                                    arraySet5 = arraySet9;
                                                    arraySet2 = arraySet8;
                                                    arraySet = arraySet10;
                                                    sb2 = str2;
                                                    requestedPermissions = list;
                                                    arraySet18 = arraySet6;
                                                    sparseBooleanArray = sparseBooleanArray2;
                                                    orCreateUserState = userPermissionState3;
                                                    str3 = str;
                                                    arraySet3 = arraySet7;
                                                    androidPackage3 = androidPackage2;
                                                    size = i17;
                                                } else {
                                                    androidPackage2 = androidPackage;
                                                    str2 = sb2;
                                                    z4 = z12;
                                                    PermissionState permissionState22 = uidPermissionState.getPermissionState(name);
                                                    orCreateUidState.updatePermissionFlags(permission3, 261119, permissionState22 == null ? permissionState22.getFlags() : 0);
                                                    z11 = z4;
                                                    iArr12 = iArr13;
                                                    i3 = i4 + 1;
                                                    arraySet5 = arraySet9;
                                                    arraySet2 = arraySet8;
                                                    arraySet = arraySet10;
                                                    sb2 = str2;
                                                    requestedPermissions = list;
                                                    arraySet18 = arraySet6;
                                                    sparseBooleanArray = sparseBooleanArray2;
                                                    orCreateUserState = userPermissionState3;
                                                    str3 = str;
                                                    arraySet3 = arraySet7;
                                                    androidPackage3 = androidPackage2;
                                                    size = i17;
                                                }
                                            }
                                            if (permission3.isSignature()) {
                                                if (permission3.isPrivileged()) {
                                                    arraySet9 = arraySet5;
                                                    if (!CollectionUtils.contains(arraySet9, str6)) {
                                                        arraySet7 = arraySet3;
                                                        arraySet8 = arraySet2;
                                                    }
                                                } else {
                                                    arraySet9 = arraySet5;
                                                }
                                                arraySet7 = arraySet3;
                                                if (CollectionUtils.contains(arraySet7, str6)) {
                                                    arraySet8 = arraySet2;
                                                } else {
                                                    if (permission3.isPrivileged()) {
                                                        arraySet8 = arraySet2;
                                                        if (!CollectionUtils.contains(arraySet8, str6)) {
                                                        }
                                                        if (!uidPermissionState.isPermissionGranted(str6)) {
                                                        }
                                                    } else {
                                                        arraySet8 = arraySet2;
                                                    }
                                                    if (!permission3.isDevelopment()) {
                                                    }
                                                    if (!uidPermissionState.isPermissionGranted(str6)) {
                                                    }
                                                }
                                                arraySet10 = arraySet;
                                                if (orCreateUidState.grantPermission(permission3)) {
                                                }
                                            } else {
                                                arraySet7 = arraySet3;
                                                arraySet8 = arraySet2;
                                                arraySet9 = arraySet5;
                                            }
                                            if (!permission3.isInternal() || (permission3.isPrivileged() && !CollectionUtils.contains(arraySet9, str6))) {
                                                arraySet10 = arraySet;
                                            } else {
                                                arraySet10 = arraySet;
                                                if (!CollectionUtils.contains(arraySet10, str6)) {
                                                    if (permission3.isPrivileged()) {
                                                    }
                                                    if (!permission3.isDevelopment()) {
                                                    }
                                                }
                                                if (orCreateUidState.grantPermission(permission3)) {
                                                }
                                            }
                                            if (PackageManagerService.DEBUG_PERMISSIONS && ((isPermissionGranted = orCreateUidState.isPermissionGranted(permission3.getName())) || permission3.isAppOp())) {
                                                StringBuilder sb4 = new StringBuilder();
                                                sb4.append(isPermissionGranted ? "Un-granting" : "Not granting");
                                                sb4.append(" permission ");
                                                sb4.append(name);
                                                sb4.append(" from package ");
                                                sb4.append(sb2);
                                                sb4.append(" (protectionLevel=");
                                                sb4.append(permission3.getProtectionLevel());
                                                sb4.append(" flags=0x");
                                                androidPackage2 = androidPackage;
                                                str2 = sb2;
                                                sb4.append(Integer.toHexString(PackageInfoUtils.appInfoFlags(androidPackage2, packageStateInternal)));
                                                sb4.append(")");
                                                Slog.i(TAG, sb4.toString());
                                            } else {
                                                androidPackage2 = androidPackage;
                                                str2 = sb2;
                                            }
                                            if (orCreateUidState.revokePermission(permission3)) {
                                                z4 = true;
                                                PermissionState permissionState222 = uidPermissionState.getPermissionState(name);
                                                orCreateUidState.updatePermissionFlags(permission3, 261119, permissionState222 == null ? permissionState222.getFlags() : 0);
                                                z11 = z4;
                                                iArr12 = iArr13;
                                                i3 = i4 + 1;
                                                arraySet5 = arraySet9;
                                                arraySet2 = arraySet8;
                                                arraySet = arraySet10;
                                                sb2 = str2;
                                                requestedPermissions = list;
                                                arraySet18 = arraySet6;
                                                sparseBooleanArray = sparseBooleanArray2;
                                                orCreateUserState = userPermissionState3;
                                                str3 = str;
                                                arraySet3 = arraySet7;
                                                androidPackage3 = androidPackage2;
                                                size = i17;
                                            }
                                            z4 = z12;
                                            PermissionState permissionState2222 = uidPermissionState.getPermissionState(name);
                                            orCreateUidState.updatePermissionFlags(permission3, 261119, permissionState2222 == null ? permissionState2222.getFlags() : 0);
                                            z11 = z4;
                                            iArr12 = iArr13;
                                            i3 = i4 + 1;
                                            arraySet5 = arraySet9;
                                            arraySet2 = arraySet8;
                                            arraySet = arraySet10;
                                            sb2 = str2;
                                            requestedPermissions = list;
                                            arraySet18 = arraySet6;
                                            sparseBooleanArray = sparseBooleanArray2;
                                            orCreateUserState = userPermissionState3;
                                            str3 = str;
                                            arraySet3 = arraySet7;
                                            androidPackage3 = androidPackage2;
                                            size = i17;
                                        }
                                    }
                                    userPermissionState3 = orCreateUserState;
                                    arraySet6 = arraySet18;
                                    sparseBooleanArray2 = sparseBooleanArray;
                                    iArr4 = iArr3;
                                    androidPackage2 = androidPackage;
                                    iArr12 = iArr4;
                                    arraySet10 = arraySet;
                                    arraySet7 = arraySet3;
                                    arraySet8 = arraySet2;
                                    arraySet9 = arraySet5;
                                    z11 = z12;
                                    str2 = sb2;
                                    i3 = i4 + 1;
                                    arraySet5 = arraySet9;
                                    arraySet2 = arraySet8;
                                    arraySet = arraySet10;
                                    sb2 = str2;
                                    requestedPermissions = list;
                                    arraySet18 = arraySet6;
                                    sparseBooleanArray = sparseBooleanArray2;
                                    orCreateUserState = userPermissionState3;
                                    str3 = str;
                                    arraySet3 = arraySet7;
                                    androidPackage3 = androidPackage2;
                                    size = i17;
                                }
                                userPermissionState = orCreateUserState;
                                ArraySet<String> arraySet19 = arraySet18;
                                int i19 = size;
                                AndroidPackage androidPackage5 = androidPackage3;
                                z2 = z11;
                                int[] iArr14 = iArr12;
                                List<String> list3 = requestedPermissions;
                                ArraySet arraySet20 = arraySet;
                                ArraySet arraySet21 = arraySet3;
                                ArraySet<String> arraySet22 = arraySet2;
                                ArraySet arraySet23 = arraySet5;
                                SparseBooleanArray sparseBooleanArray6 = sparseBooleanArray;
                                if (!z2 && !z) {
                                    userPermissionState2 = userPermissionState;
                                    if (!packageStateInternal.isUpdatedSystemApp()) {
                                        z3 = true;
                                        if (z2) {
                                            iArr2 = (str == null || !z) ? iArr14 : ArrayUtils.appendInt(iArr14, i14);
                                            z10 = z3;
                                        } else {
                                            iArr2 = iArr14;
                                        }
                                        arraySet3 = arraySet21;
                                        String str7 = str5;
                                        iArr9 = setInitialGrantForNewImplicitPermissionsLocked(uidPermissionState, orCreateUidState, androidPackage, arraySet19, i14, revokePermissionsNoLongerImplicitLocked(orCreateUidState, androidPackage.getPackageName(), collection2, i16, i14, iArr2));
                                        i12 = i15 + 1;
                                        length2 = i13;
                                        arraySet2 = arraySet22;
                                        z7 = z;
                                        androidPackage3 = androidPackage5;
                                        arraySet = arraySet20;
                                        arraySet14 = arraySet23;
                                        z8 = z10;
                                        collection = collection3;
                                        iArr5 = iArr10;
                                        i2 = i16;
                                        size = i19;
                                        requestedPermissions = list3;
                                        sparseBooleanArray3 = sparseBooleanArray6;
                                        str5 = str7;
                                        str3 = str;
                                    }
                                    z3 = true;
                                    userPermissionState2.setInstallPermissionsFixed(packageStateInternal.getPackageName(), true);
                                    if (z2) {
                                    }
                                    arraySet3 = arraySet21;
                                    String str72 = str5;
                                    iArr9 = setInitialGrantForNewImplicitPermissionsLocked(uidPermissionState, orCreateUidState, androidPackage, arraySet19, i14, revokePermissionsNoLongerImplicitLocked(orCreateUidState, androidPackage.getPackageName(), collection2, i16, i14, iArr2));
                                    i12 = i15 + 1;
                                    length2 = i13;
                                    arraySet2 = arraySet22;
                                    z7 = z;
                                    androidPackage3 = androidPackage5;
                                    arraySet = arraySet20;
                                    arraySet14 = arraySet23;
                                    z8 = z10;
                                    collection = collection3;
                                    iArr5 = iArr10;
                                    i2 = i16;
                                    size = i19;
                                    requestedPermissions = list3;
                                    sparseBooleanArray3 = sparseBooleanArray6;
                                    str5 = str72;
                                    str3 = str;
                                }
                                userPermissionState2 = userPermissionState;
                                if (!userPermissionState2.areInstallPermissionsFixed(packageStateInternal.getPackageName())) {
                                }
                                if (!packageStateInternal.isUpdatedSystemApp()) {
                                }
                                z3 = true;
                                userPermissionState2.setInstallPermissionsFixed(packageStateInternal.getPackageName(), true);
                                if (z2) {
                                }
                                arraySet3 = arraySet21;
                                String str722 = str5;
                                iArr9 = setInitialGrantForNewImplicitPermissionsLocked(uidPermissionState, orCreateUidState, androidPackage, arraySet19, i14, revokePermissionsNoLongerImplicitLocked(orCreateUidState, androidPackage.getPackageName(), collection2, i16, i14, iArr2));
                                i12 = i15 + 1;
                                length2 = i13;
                                arraySet2 = arraySet22;
                                z7 = z;
                                androidPackage3 = androidPackage5;
                                arraySet = arraySet20;
                                arraySet14 = arraySet23;
                                z8 = z10;
                                collection = collection3;
                                iArr5 = iArr10;
                                i2 = i16;
                                size = i19;
                                requestedPermissions = list3;
                                sparseBooleanArray3 = sparseBooleanArray6;
                                str5 = str722;
                                str3 = str;
                            }
                            uidPermissionState = orCreateUidState;
                            ArraySet<String> arraySet182 = new ArraySet<>();
                            StringBuilder sb5 = new StringBuilder();
                            int[] iArr112 = iArr9;
                            sb5.append(androidPackage.getPackageName());
                            sb5.append("(");
                            sb5.append(androidPackage.getUid());
                            sb5.append(")");
                            String sb22 = sb5.toString();
                            int i162 = i2;
                            i3 = 0;
                            boolean z112 = false;
                            Collection collection32 = collection;
                            int[] iArr122 = iArr112;
                            while (i3 < size) {
                            }
                            userPermissionState = orCreateUserState;
                            ArraySet<String> arraySet192 = arraySet182;
                            int i192 = size;
                            AndroidPackage androidPackage52 = androidPackage3;
                            z2 = z112;
                            int[] iArr142 = iArr122;
                            List<String> list32 = requestedPermissions;
                            ArraySet arraySet202 = arraySet;
                            ArraySet arraySet212 = arraySet3;
                            ArraySet<String> arraySet222 = arraySet2;
                            ArraySet arraySet232 = arraySet5;
                            SparseBooleanArray sparseBooleanArray62 = sparseBooleanArray;
                            if (!z2) {
                                userPermissionState2 = userPermissionState;
                                if (!packageStateInternal.isUpdatedSystemApp()) {
                                }
                                z3 = true;
                                userPermissionState2.setInstallPermissionsFixed(packageStateInternal.getPackageName(), true);
                                if (z2) {
                                }
                                arraySet3 = arraySet212;
                                String str7222 = str5;
                                iArr9 = setInitialGrantForNewImplicitPermissionsLocked(uidPermissionState, orCreateUidState, androidPackage, arraySet192, i14, revokePermissionsNoLongerImplicitLocked(orCreateUidState, androidPackage.getPackageName(), collection2, i162, i14, iArr2));
                                i12 = i15 + 1;
                                length2 = i13;
                                arraySet2 = arraySet222;
                                z7 = z;
                                androidPackage3 = androidPackage52;
                                arraySet = arraySet202;
                                arraySet14 = arraySet232;
                                z8 = z10;
                                collection = collection32;
                                iArr5 = iArr10;
                                i2 = i162;
                                size = i192;
                                requestedPermissions = list32;
                                sparseBooleanArray3 = sparseBooleanArray62;
                                str5 = str7222;
                                str3 = str;
                            }
                            userPermissionState2 = userPermissionState;
                            if (!userPermissionState2.areInstallPermissionsFixed(packageStateInternal.getPackageName())) {
                            }
                            if (!packageStateInternal.isUpdatedSystemApp()) {
                            }
                            z3 = true;
                            userPermissionState2.setInstallPermissionsFixed(packageStateInternal.getPackageName(), true);
                            if (z2) {
                            }
                            arraySet3 = arraySet212;
                            String str72222 = str5;
                            iArr9 = setInitialGrantForNewImplicitPermissionsLocked(uidPermissionState, orCreateUidState, androidPackage, arraySet192, i14, revokePermissionsNoLongerImplicitLocked(orCreateUidState, androidPackage.getPackageName(), collection2, i162, i14, iArr2));
                            i12 = i15 + 1;
                            length2 = i13;
                            arraySet2 = arraySet222;
                            z7 = z;
                            androidPackage3 = androidPackage52;
                            arraySet = arraySet202;
                            arraySet14 = arraySet232;
                            z8 = z10;
                            collection = collection32;
                            iArr5 = iArr10;
                            i2 = i162;
                            size = i192;
                            requestedPermissions = list32;
                            sparseBooleanArray3 = sparseBooleanArray62;
                            str5 = str72222;
                            str3 = str;
                        } catch (Throwable th) {
                            th = th;
                            str3 = str5;
                            throw th;
                        }
                    }
                    boolean z17 = z8;
                    AndroidPackage androidPackage6 = androidPackage3;
                    boolean z18 = z7;
                    int[] iArr15 = iArr5;
                    int[] checkIfLegacyStorageOpsNeedToBeUpdated = checkIfLegacyStorageOpsNeedToBeUpdated(androidPackage6, z18, iArr15, iArr9);
                    if (permissionCallback != null) {
                        permissionCallback.onPermissionUpdated(checkIfLegacyStorageOpsNeedToBeUpdated, (str != null && z18 && z17) || z9);
                    }
                    for (int i20 : checkIfLegacyStorageOpsNeedToBeUpdated) {
                        notifyRuntimePermissionStateChanged(androidPackage.getPackageName(), i20);
                    }
                } catch (Throwable th2) {
                    th = th2;
                    str3 = obj;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        }
    }

    private int[] getAllUserIds() {
        return UserManagerService.getInstance().getUserIdsIncludingPreCreated();
    }

    @GuardedBy({"mLock"})
    private int[] revokePermissionsNoLongerImplicitLocked(UidPermissionState uidPermissionState, String str, Collection<String> collection, int i, int i2, int[] iArr) {
        Permission permission;
        int i3;
        boolean z = i >= 23;
        for (String str2 : uidPermissionState.getGrantedPermissions()) {
            if (!collection.contains(str2) && (permission = this.mRegistry.getPermission(str2)) != null && permission.isRuntime()) {
                int permissionFlags = uidPermissionState.getPermissionFlags(str2);
                if ((permissionFlags & 128) != 0) {
                    boolean z2 = ArrayUtils.contains(NEARBY_DEVICES_PERMISSIONS, str2) && uidPermissionState.isPermissionGranted("android.permission.ACCESS_BACKGROUND_LOCATION") && (uidPermissionState.getPermissionFlags("android.permission.ACCESS_BACKGROUND_LOCATION") & 136) == 0;
                    if ((permissionFlags & 52) == 0 && z && !z2) {
                        if (uidPermissionState.revokePermission(permission) && PackageManagerService.DEBUG_PERMISSIONS) {
                            Slog.i(TAG, "Revoking runtime permission " + str2 + " for " + str + " as it is now requested");
                        }
                        i3 = 131;
                    } else {
                        i3 = 128;
                    }
                    uidPermissionState.updatePermissionFlags(permission, i3, 0);
                    iArr = ArrayUtils.appendInt(iArr, i2);
                }
            }
        }
        return iArr;
    }

    @GuardedBy({"mLock"})
    private void inheritPermissionStateToNewImplicitPermissionLocked(ArraySet<String> arraySet, String str, UidPermissionState uidPermissionState, AndroidPackage androidPackage) {
        String packageName = androidPackage.getPackageName();
        int size = arraySet.size();
        boolean z = false;
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            String valueAt = arraySet.valueAt(i2);
            if (uidPermissionState.isPermissionGranted(valueAt)) {
                if (!z) {
                    i = 0;
                }
                i |= uidPermissionState.getPermissionFlags(valueAt);
                z = true;
            } else if (!z) {
                i |= uidPermissionState.getPermissionFlags(valueAt);
            }
        }
        if (z) {
            if (PackageManagerService.DEBUG_PERMISSIONS) {
                Slog.i(TAG, str + " inherits runtime perm grant from " + arraySet + " for " + packageName);
            }
            uidPermissionState.grantPermission(this.mRegistry.getPermission(str));
        }
        uidPermissionState.updatePermissionFlags(this.mRegistry.getPermission(str), i, i);
    }

    private int[] checkIfLegacyStorageOpsNeedToBeUpdated(AndroidPackage androidPackage, boolean z, int[] iArr, int[] iArr2) {
        return (z && androidPackage.isRequestLegacyExternalStorage() && (androidPackage.getRequestedPermissions().contains("android.permission.READ_EXTERNAL_STORAGE") || androidPackage.getRequestedPermissions().contains("android.permission.WRITE_EXTERNAL_STORAGE"))) ? (int[]) iArr.clone() : iArr2;
    }

    @GuardedBy({"mLock"})
    private int[] setInitialGrantForNewImplicitPermissionsLocked(UidPermissionState uidPermissionState, UidPermissionState uidPermissionState2, AndroidPackage androidPackage, ArraySet<String> arraySet, int i, int[] iArr) {
        boolean z;
        String packageName = androidPackage.getPackageName();
        ArrayMap arrayMap = new ArrayMap();
        List<PermissionManager.SplitPermissionInfo> splitPermissionInfos = getSplitPermissionInfos();
        int size = splitPermissionInfos.size();
        for (int i2 = 0; i2 < size; i2++) {
            PermissionManager.SplitPermissionInfo splitPermissionInfo = splitPermissionInfos.get(i2);
            List newPermissions = splitPermissionInfo.getNewPermissions();
            int size2 = newPermissions.size();
            for (int i3 = 0; i3 < size2; i3++) {
                String str = (String) newPermissions.get(i3);
                ArraySet arraySet2 = (ArraySet) arrayMap.get(str);
                if (arraySet2 == null) {
                    arraySet2 = new ArraySet();
                    arrayMap.put(str, arraySet2);
                }
                arraySet2.add(splitPermissionInfo.getSplitPermission());
            }
        }
        int size3 = arraySet.size();
        int[] iArr2 = iArr;
        for (int i4 = 0; i4 < size3; i4++) {
            String valueAt = arraySet.valueAt(i4);
            ArraySet<String> arraySet3 = (ArraySet) arrayMap.get(valueAt);
            if (arraySet3 != null) {
                Permission permission = this.mRegistry.getPermission(valueAt);
                if (permission == null) {
                    throw new IllegalStateException("Unknown new permission in split permission: " + valueAt);
                }
                if (permission.isRuntime()) {
                    if (!valueAt.equals("android.permission.ACTIVITY_RECOGNITION") && !READ_MEDIA_AURAL_PERMISSIONS.contains(valueAt) && !READ_MEDIA_VISUAL_PERMISSIONS.contains(valueAt)) {
                        uidPermissionState2.updatePermissionFlags(permission, 128, 128);
                    }
                    iArr2 = ArrayUtils.appendInt(iArr2, i);
                    if (!uidPermissionState.hasPermissionState(arraySet3)) {
                        int i5 = 0;
                        while (true) {
                            if (i5 >= arraySet3.size()) {
                                z = false;
                                break;
                            }
                            String valueAt2 = arraySet3.valueAt(i5);
                            Permission permission2 = this.mRegistry.getPermission(valueAt2);
                            if (permission2 == null) {
                                throw new IllegalStateException("Unknown source permission in split permission: " + valueAt2);
                            }
                            if (!permission2.isRuntime()) {
                                z = true;
                                break;
                            }
                            i5++;
                        }
                        if (!z) {
                            if (PackageManagerService.DEBUG_PERMISSIONS) {
                                Slog.i(TAG, valueAt + " does not inherit from " + arraySet3 + " for " + packageName + " as split permission is also new");
                            }
                        }
                    }
                    inheritPermissionStateToNewImplicitPermissionLocked(arraySet3, valueAt, uidPermissionState2, androidPackage);
                }
            }
        }
        return iArr2;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public List<SplitPermissionInfoParcelable> getSplitPermissions() {
        return PermissionManager.splitPermissionInfoListToParcelableList(getSplitPermissionInfos());
    }

    private List<PermissionManager.SplitPermissionInfo> getSplitPermissionInfos() {
        return SystemConfig.getInstance().getSplitPermissions();
    }

    private static boolean isCompatPlatformPermissionForPackage(String str, AndroidPackage androidPackage) {
        int length = CompatibilityPermissionInfo.COMPAT_PERMS.length;
        for (int i = 0; i < length; i++) {
            CompatibilityPermissionInfo compatibilityPermissionInfo = CompatibilityPermissionInfo.COMPAT_PERMS[i];
            if (compatibilityPermissionInfo.getName().equals(str) && androidPackage.getTargetSdkVersion() < compatibilityPermissionInfo.getSdkVersion()) {
                Log.i(TAG, "Auto-granting " + str + " to old pkg " + androidPackage.getPackageName());
                return true;
            }
        }
        return false;
    }

    private boolean checkPrivilegedPermissionAllowlist(AndroidPackage androidPackage, PackageStateInternal packageStateInternal, Permission permission) {
        if (RoSystemProperties.CONTROL_PRIVAPP_PERMISSIONS_DISABLE) {
            return true;
        }
        String packageName = androidPackage.getPackageName();
        if (Objects.equals(packageName, PackageManagerService.PLATFORM_PACKAGE_NAME) || !packageStateInternal.isSystem() || !packageStateInternal.isPrivileged() || !this.mPrivilegedPermissionAllowlistSourcePackageNames.contains(permission.getPackageName())) {
            return true;
        }
        String name = permission.getName();
        Boolean privilegedPermissionAllowlistState = getPrivilegedPermissionAllowlistState(packageStateInternal, name, this.mApexManager.getActiveApexPackageNameContainingPackage(packageName));
        if (privilegedPermissionAllowlistState != null) {
            return privilegedPermissionAllowlistState.booleanValue();
        }
        if (packageStateInternal.isUpdatedSystemApp()) {
            return true;
        }
        if (!this.mSystemReady && !packageStateInternal.isApkInUpdatedApex()) {
            Slog.w(TAG, "Privileged permission " + name + " for package " + packageName + " (" + androidPackage.getPath() + ") not in privapp-permissions allowlist");
            if (RoSystemProperties.CONTROL_PRIVAPP_PERMISSIONS_ENFORCE) {
                synchronized (this.mLock) {
                    if (this.mPrivappPermissionsViolations == null) {
                        this.mPrivappPermissionsViolations = new ArraySet<>();
                    }
                    this.mPrivappPermissionsViolations.add(packageName + " (" + androidPackage.getPath() + "): " + name);
                }
            }
        }
        return !RoSystemProperties.CONTROL_PRIVAPP_PERMISSIONS_ENFORCE;
    }

    private Boolean getPrivilegedPermissionAllowlistState(PackageState packageState, String str, String str2) {
        PermissionAllowlist permissionAllowlist = SystemConfig.getInstance().getPermissionAllowlist();
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
        if (str2 != null) {
            Boolean privilegedAppAllowlistState = permissionAllowlist.getPrivilegedAppAllowlistState(packageName, str);
            if (privilegedAppAllowlistState != null) {
                Slog.w(TAG, "Package " + packageName + " is an APK in APEX, but has permission allowlist on the system image. Please bundle the allowlist in the " + str2 + " APEX instead.");
            }
            Boolean apexPrivilegedAppAllowlistState = permissionAllowlist.getApexPrivilegedAppAllowlistState(this.mApexManager.getApexModuleNameForPackageName(str2), packageName, str);
            return apexPrivilegedAppAllowlistState != null ? apexPrivilegedAppAllowlistState : privilegedAppAllowlistState;
        }
        return permissionAllowlist.getPrivilegedAppAllowlistState(packageName, str);
    }

    private boolean shouldGrantPermissionBySignature(AndroidPackage androidPackage, Permission permission) {
        AndroidPackage androidPackage2 = this.mPackageManagerInt.getPackage((String) ArrayUtils.firstOrNull(this.mPackageManagerInt.getKnownPackageNames(0, 0)));
        boolean z = getSourcePackageSigningDetails(permission).hasCommonSignerWithCapability(androidPackage.getSigningDetails(), 4) || androidPackage.getSigningDetails().hasAncestorOrSelf(androidPackage2.getSigningDetails()) || androidPackage2.getSigningDetails().checkCapability(androidPackage.getSigningDetails(), 4);
        if (z || !this.mPermissionManagerServiceExt.hookShouldGrantPermissionBySignature(androidPackage, permission.getName(), false, permission.getPackageName())) {
            return z;
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:123:0x0059, code lost:
    
        if (r10.isPrivileged() != false) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:126:0x0061, code lost:
    
        if (canGrantOemPermission(r10, r4) != false) goto L22;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean shouldGrantPermissionByProtectionFlags(AndroidPackage androidPackage, PackageStateInternal packageStateInternal, Permission permission, ArraySet<String> arraySet) {
        boolean z;
        boolean isPrivileged = permission.isPrivileged();
        boolean isOem = permission.isOem();
        if ((isPrivileged || isOem) && packageStateInternal.isSystem()) {
            String name = permission.getName();
            if (packageStateInternal.isUpdatedSystemApp()) {
                PackageStateInternal disabledSystemPackage = this.mPackageManagerInt.getDisabledSystemPackage(androidPackage.getPackageName());
                AndroidPackageInternal pkg = disabledSystemPackage == null ? null : disabledSystemPackage.getPkg();
                if (pkg != null && ((isPrivileged && disabledSystemPackage.isPrivileged()) || (isOem && canGrantOemPermission(disabledSystemPackage, name)))) {
                    if (!pkg.getRequestedPermissions().contains(name)) {
                        arraySet.add(name);
                    }
                    z = true;
                }
                z = false;
            } else {
                if (isPrivileged) {
                }
                if (isOem) {
                }
                z = false;
            }
            if (z && isPrivileged && !permission.isVendorPrivileged() && packageStateInternal.isVendor()) {
                Slog.w(TAG, "Permission " + name + " cannot be granted to privileged vendor apk " + androidPackage.getPackageName() + " because it isn't a 'vendorPrivileged' permission.");
            }
            if (!z && permission.isPre23() && androidPackage.getTargetSdkVersion() < 23) {
                z = true;
            }
            if (!z && permission.isInstaller() && (ArrayUtils.contains(this.mPackageManagerInt.getKnownPackageNames(2, 0), androidPackage.getPackageName()) || ArrayUtils.contains(this.mPackageManagerInt.getKnownPackageNames(7, 0), androidPackage.getPackageName()))) {
                z = true;
            }
            if (!z && permission.isVerifier() && ArrayUtils.contains(this.mPackageManagerInt.getKnownPackageNames(4, 0), androidPackage.getPackageName())) {
                z = true;
            }
            if (!z && permission.isPreInstalled() && packageStateInternal.isSystem()) {
                z = true;
            }
            if (!z && permission.isKnownSigner()) {
                z = androidPackage.getSigningDetails().hasAncestorOrSelfWithDigest(permission.getKnownCerts());
            }
            if (!z && permission.isSetup() && ArrayUtils.contains(this.mPackageManagerInt.getKnownPackageNames(1, 0), androidPackage.getPackageName())) {
                z = true;
            }
            if (!z && permission.isSystemTextClassifier() && ArrayUtils.contains(this.mPackageManagerInt.getKnownPackageNames(6, 0), androidPackage.getPackageName())) {
                z = true;
            }
            if (!z && permission.isConfigurator() && ArrayUtils.contains(this.mPackageManagerInt.getKnownPackageNames(10, 0), androidPackage.getPackageName())) {
                z = true;
            }
            if (!z && permission.isIncidentReportApprover() && ArrayUtils.contains(this.mPackageManagerInt.getKnownPackageNames(11, 0), androidPackage.getPackageName())) {
                z = true;
            }
            if (!z && permission.isAppPredictor() && ArrayUtils.contains(this.mPackageManagerInt.getKnownPackageNames(12, 0), androidPackage.getPackageName())) {
                z = true;
            }
            if (!z && permission.isCompanion() && ArrayUtils.contains(this.mPackageManagerInt.getKnownPackageNames(15, 0), androidPackage.getPackageName())) {
                z = true;
            }
            if (!z && permission.isRetailDemo() && ArrayUtils.contains(this.mPackageManagerInt.getKnownPackageNames(16, 0), androidPackage.getPackageName()) && isProfileOwner(androidPackage.getUid())) {
                z = true;
            }
            if (!z && permission.isRecents() && ArrayUtils.contains(this.mPackageManagerInt.getKnownPackageNames(17, 0), androidPackage.getPackageName())) {
                z = true;
            }
            if (z && permission.isModule() && this.mApexManager.getActiveApexPackageNameContainingPackage(androidPackage.getPackageName()) != null) {
                return true;
            }
            return z;
        }
        z = false;
        if (!z) {
            z = true;
        }
        if (!z) {
            z = true;
        }
        if (!z) {
            z = true;
        }
        if (!z) {
            z = true;
        }
        if (!z) {
            z = androidPackage.getSigningDetails().hasAncestorOrSelfWithDigest(permission.getKnownCerts());
        }
        if (!z) {
            z = true;
        }
        if (!z) {
            z = true;
        }
        if (!z) {
            z = true;
        }
        if (!z) {
            z = true;
        }
        if (!z) {
            z = true;
        }
        if (!z) {
            z = true;
        }
        if (!z) {
            z = true;
        }
        if (!z) {
            z = true;
        }
        if (z) {
        }
        return z;
    }

    private SigningDetails getSourcePackageSigningDetails(Permission permission) {
        PackageStateInternal sourcePackageSetting = getSourcePackageSetting(permission);
        if (sourcePackageSetting == null) {
            return SigningDetails.UNKNOWN;
        }
        return sourcePackageSetting.getSigningDetails();
    }

    private PackageStateInternal getSourcePackageSetting(Permission permission) {
        return this.mPackageManagerInt.getPackageStateInternal(permission.getPackageName());
    }

    private static boolean canGrantOemPermission(PackageState packageState, String str) {
        if (!packageState.isOem()) {
            return false;
        }
        String packageName = packageState.getPackageName();
        Boolean oemAppAllowlistState = SystemConfig.getInstance().getPermissionAllowlist().getOemAppAllowlistState(packageState.getPackageName(), str);
        if (oemAppAllowlistState != null) {
            return Boolean.TRUE == oemAppAllowlistState;
        }
        throw new IllegalStateException("OEM permission " + str + " requested by package " + packageName + " must be explicitly declared granted or not");
    }

    private static boolean isProfileOwner(int i) {
        DevicePolicyManagerInternal devicePolicyManagerInternal = (DevicePolicyManagerInternal) LocalServices.getService(DevicePolicyManagerInternal.class);
        if (devicePolicyManagerInternal != null) {
            return devicePolicyManagerInternal.isActiveProfileOwner(i) || devicePolicyManagerInternal.isActiveDeviceOwner(i);
        }
        return false;
    }

    private boolean isPermissionsReviewRequiredInternal(String str, int i) {
        AndroidPackage androidPackage = this.mPackageManagerInt.getPackage(str);
        if (androidPackage == null || this.mPermissionManagerServiceExt.hookIsPermissionsReviewRequiredInternal() || androidPackage.getTargetSdkVersion() >= 23) {
            return false;
        }
        synchronized (this.mLock) {
            UidPermissionState uidStateLocked = getUidStateLocked(androidPackage, i);
            if (uidStateLocked == null) {
                Slog.e(TAG, "Missing permissions state for " + androidPackage.getPackageName() + " and user " + i);
                return false;
            }
            return uidStateLocked.isPermissionsReviewRequired();
        }
    }

    private void grantRequestedPermissionsInternal(AndroidPackage androidPackage, ArrayMap<String, Integer> arrayMap, int i) {
        boolean z = androidPackage.getTargetSdkVersion() >= 23;
        boolean isInstantApp = this.mPackageManagerInt.isInstantApp(androidPackage.getPackageName(), i);
        int myUid = Process.myUid();
        for (String str : androidPackage.getRequestedPermissions()) {
            Integer num = arrayMap.get(str);
            if (num != null && num.intValue() != 0) {
                synchronized (this.mLock) {
                    Permission permission = this.mRegistry.getPermission(str);
                    if (permission != null) {
                        boolean z2 = (permission.isRuntime() || permission.isDevelopment()) && (!isInstantApp || permission.isInstant()) && ((z || !permission.isRuntimeOnly()) && num.intValue() == 1);
                        boolean isAppOp = permission.isAppOp();
                        int permissionFlagsInternal = getPermissionFlagsInternal(androidPackage.getPackageName(), str, myUid, i);
                        if (z2) {
                            if (z) {
                                if ((permissionFlagsInternal & 20) == 0) {
                                    grantRuntimePermissionInternal(androidPackage.getPackageName(), str, false, myUid, i, this.mDefaultPermissionCallback);
                                }
                            } else if ((permissionFlagsInternal & 72) != 0) {
                                updatePermissionFlagsInternal(androidPackage.getPackageName(), str, 72, 0, myUid, i, false, this.mDefaultPermissionCallback);
                            }
                        } else if (isAppOp && PackageInstallerService.INSTALLER_CHANGEABLE_APP_OP_PERMISSIONS.contains(str) && (permissionFlagsInternal & 1) == 0) {
                            final int i2 = num.intValue() == 1 ? 0 : 2;
                            final int uid = UserHandle.getUid(i, androidPackage.getUid());
                            final String permissionToOp = AppOpsManager.permissionToOp(str);
                            this.mHandler.post(new Runnable() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda14
                                @Override // java.lang.Runnable
                                public final void run() {
                                    PermissionManagerServiceImpl.this.lambda$grantRequestedPermissionsInternal$9(permissionToOp, uid, i2);
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$grantRequestedPermissionsInternal$9(String str, int i, int i2) {
        ((AppOpsManager) this.mContext.getSystemService(AppOpsManager.class)).setUidMode(str, i, i2);
    }

    private void setAllowlistedRestrictedPermissionsInternal(AndroidPackage androidPackage, List<String> list, int i, int i2) {
        int i3;
        int i4;
        int i5;
        int size = androidPackage.getRequestedPermissions().size();
        int myUid = Process.myUid();
        boolean z = false;
        int i6 = 0;
        ArraySet arraySet = null;
        while (i6 < size) {
            String str = androidPackage.getRequestedPermissions().get(i6);
            synchronized (this.mLock) {
                Permission permission = this.mRegistry.getPermission(str);
                if (permission != null && permission.isHardOrSoftRestricted()) {
                    UidPermissionState uidStateLocked = getUidStateLocked(androidPackage, i2);
                    if (uidStateLocked == null) {
                        Slog.e(TAG, "Missing permissions state for " + androidPackage.getPackageName() + " and user " + i2);
                        i3 = i6;
                    } else {
                        boolean isPermissionGranted = uidStateLocked.isPermissionGranted(str);
                        if (isPermissionGranted) {
                            if (arraySet == null) {
                                arraySet = new ArraySet();
                            }
                            arraySet.add(str);
                        }
                        ArraySet arraySet2 = arraySet;
                        int permissionFlagsInternal = getPermissionFlagsInternal(androidPackage.getPackageName(), str, myUid, i2);
                        int i7 = i;
                        int i8 = permissionFlagsInternal;
                        int i9 = 0;
                        while (i7 != 0) {
                            int numberOfTrailingZeros = 1 << Integer.numberOfTrailingZeros(i7);
                            i7 &= ~numberOfTrailingZeros;
                            if (numberOfTrailingZeros == 1) {
                                i9 |= 4096;
                                i8 = (list == null || !list.contains(str)) ? i8 & (-4097) : i8 | 4096;
                            } else if (numberOfTrailingZeros == 2) {
                                i9 |= 2048;
                                i8 = (list == null || !list.contains(str)) ? i8 & (-2049) : i8 | 2048;
                            } else if (numberOfTrailingZeros == 4) {
                                i9 |= 8192;
                                i8 = (list == null || !list.contains(str)) ? i8 & (-8193) : i8 | 8192;
                            }
                        }
                        if (permissionFlagsInternal == i8) {
                            i3 = i6;
                            arraySet = arraySet2;
                        } else {
                            boolean z2 = (permissionFlagsInternal & 14336) != 0;
                            boolean z3 = (i8 & 14336) != 0;
                            if ((permissionFlagsInternal & 4) != 0 && !z3 && isPermissionGranted) {
                                i9 |= 4;
                                i8 &= -5;
                            }
                            if (androidPackage.getTargetSdkVersion() >= 23 || z2 || !z3) {
                                i4 = i8;
                                i5 = i9;
                            } else {
                                i5 = i9 | 64;
                                i4 = i8 | 64;
                            }
                            i3 = i6;
                            updatePermissionFlagsInternal(androidPackage.getPackageName(), str, i5, i4, myUid, i2, false, null);
                            arraySet = arraySet2;
                            z = true;
                        }
                    }
                }
                i3 = i6;
            }
            i6 = i3 + 1;
        }
        if (z) {
            restorePermissionState(androidPackage, false, androidPackage.getPackageName(), this.mDefaultPermissionCallback, i2);
            if (arraySet == null) {
                return;
            }
            int size2 = arraySet.size();
            for (int i10 = 0; i10 < size2; i10++) {
                String str2 = (String) arraySet.valueAt(i10);
                synchronized (this.mLock) {
                    UidPermissionState uidStateLocked2 = getUidStateLocked(androidPackage, i2);
                    if (uidStateLocked2 == null) {
                        Slog.e(TAG, "Missing permissions state for " + androidPackage.getPackageName() + " and user " + i2);
                    } else {
                        boolean isPermissionGranted2 = uidStateLocked2.isPermissionGranted(str2);
                        if (!isPermissionGranted2) {
                            this.mDefaultPermissionCallback.onPermissionRevoked(androidPackage.getUid(), i2, null);
                            return;
                        }
                    }
                }
            }
        }
    }

    private void revokeSharedUserPermissionsForLeavingPackageInternal(AndroidPackage androidPackage, final int i, List<AndroidPackage> list, int i2) {
        boolean z;
        if (androidPackage == null) {
            Slog.i(TAG, "Trying to update info for null package. Just ignoring");
            return;
        }
        if (list.isEmpty()) {
            return;
        }
        PackageStateInternal disabledSystemPackage = this.mPackageManagerInt.getDisabledSystemPackage(androidPackage.getPackageName());
        boolean z2 = disabledSystemPackage != null && disabledSystemPackage.getAppId() == androidPackage.getUid();
        boolean z3 = false;
        for (String str : androidPackage.getRequestedPermissions()) {
            Iterator<AndroidPackage> it = list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    z = false;
                    break;
                }
                AndroidPackage next = it.next();
                if (next != null && !next.getPackageName().equals(androidPackage.getPackageName()) && next.getRequestedPermissions().contains(str)) {
                    z = true;
                    break;
                }
            }
            if (!z && (!z2 || !disabledSystemPackage.getPkg().getRequestedPermissions().contains(str))) {
                synchronized (this.mLock) {
                    UidPermissionState uidStateLocked = getUidStateLocked(i, i2);
                    if (uidStateLocked == null) {
                        Slog.e(TAG, "Missing permissions state for " + androidPackage.getPackageName() + " and user " + i2);
                    } else {
                        Permission permission = this.mRegistry.getPermission(str);
                        if (permission != null) {
                            if (uidStateLocked.removePermissionState(permission.getName()) && permission.hasGids()) {
                                z3 = true;
                            }
                        }
                    }
                }
            }
        }
        if (z3) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    PermissionManagerServiceImpl.killUid(i, -1, "permission grant or revoke changed gids");
                }
            });
        }
    }

    @GuardedBy({"mLock"})
    private boolean revokeUnusedSharedUserPermissionsLocked(Collection<String> collection, UidPermissionState uidPermissionState) {
        Permission permission;
        List<PermissionState> permissionStates = uidPermissionState.getPermissionStates();
        boolean z = false;
        for (int size = permissionStates.size() - 1; size >= 0; size--) {
            PermissionState permissionState = permissionStates.get(size);
            if (!collection.contains(permissionState.getName()) && (permission = this.mRegistry.getPermission(permissionState.getName())) != null && uidPermissionState.removePermissionState(permission.getName()) && permission.isRuntime()) {
                z = true;
            }
        }
        return z;
    }

    private void updatePermissions(String str, AndroidPackage androidPackage) {
        updatePermissions(str, androidPackage, getVolumeUuidForPackage(androidPackage), androidPackage == null ? 3 : 2, this.mDefaultPermissionCallback);
    }

    private void updateAllPermissions(String str, boolean z) {
        PackageManager.corkPackageInfoCache();
        try {
            updatePermissions(null, null, str, 1 | (z ? 6 : 0), this.mDefaultPermissionCallback);
        } finally {
            PackageManager.uncorkPackageInfoCache();
        }
    }

    private void updatePermissions(final String str, final AndroidPackage androidPackage, final String str2, int i, final PermissionCallback permissionCallback) {
        int i2;
        if (updatePermissionTreeSourcePackage(str, androidPackage) || updatePermissionSourcePackage(str, permissionCallback)) {
            Slog.i(TAG, "Permission ownership changed. Updating all permissions.");
            i2 = i | 1;
        } else {
            i2 = i;
        }
        updatePermissionGroupSourcePackage(str);
        Trace.traceBegin(262144L, "restorePermissionState");
        if ((i2 & 1) != 0) {
            final boolean z = (i2 & 4) != 0;
            this.mPackageManagerInt.forEachPackage(new Consumer() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda11
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    PermissionManagerServiceImpl.this.lambda$updatePermissions$11(androidPackage, z, str2, str, permissionCallback, (AndroidPackage) obj);
                }
            });
        }
        if (androidPackage != null) {
            restorePermissionState(androidPackage, (i2 & 2) != 0 && Objects.equals(str2, getVolumeUuidForPackage(androidPackage)), str, permissionCallback, -1);
        }
        Trace.traceEnd(262144L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePermissions$11(AndroidPackage androidPackage, boolean z, String str, String str2, PermissionCallback permissionCallback, AndroidPackage androidPackage2) {
        if (androidPackage2 == androidPackage) {
            return;
        }
        restorePermissionState(androidPackage2, z && Objects.equals(str, getVolumeUuidForPackage(androidPackage2)), str2, permissionCallback, -1);
    }

    private boolean updatePermissionSourcePackage(String str, final PermissionCallback permissionCallback) {
        ArraySet<Permission> arraySet;
        boolean z;
        if (str == null) {
            return true;
        }
        synchronized (this.mLock) {
            arraySet = null;
            z = false;
            for (Permission permission : this.mRegistry.getPermissions()) {
                if (permission.isDynamic()) {
                    permission.updateDynamicPermission(this.mRegistry.getPermissionTrees());
                }
                if (str.equals(permission.getPackageName())) {
                    if (arraySet == null) {
                        arraySet = new ArraySet();
                    }
                    arraySet.add(permission);
                    z = true;
                }
            }
        }
        if (arraySet != null) {
            AndroidPackage androidPackage = this.mPackageManagerInt.getPackage(str);
            for (final Permission permission2 : arraySet) {
                if (androidPackage == null || !hasPermission(androidPackage, permission2.getName())) {
                    if (!isPermissionDeclaredByDisabledSystemPkg(permission2)) {
                        Slog.i(TAG, "Removing permission " + permission2.getName() + " that used to be declared by " + permission2.getPackageName());
                        if (permission2.isRuntime()) {
                            for (final int i : this.mUserManagerInt.getUserIds()) {
                                this.mPackageManagerInt.forEachPackage(new Consumer() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda12
                                    @Override // java.util.function.Consumer
                                    public final void accept(Object obj) {
                                        PermissionManagerServiceImpl.this.lambda$updatePermissionSourcePackage$12(permission2, i, permissionCallback, (AndroidPackage) obj);
                                    }
                                });
                            }
                        } else {
                            this.mPackageManagerInt.forEachPackage(new Consumer() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda13
                                @Override // java.util.function.Consumer
                                public final void accept(Object obj) {
                                    PermissionManagerServiceImpl.this.lambda$updatePermissionSourcePackage$13(permission2, (AndroidPackage) obj);
                                }
                            });
                        }
                    }
                    synchronized (this.mLock) {
                        this.mRegistry.removePermission(permission2.getName());
                    }
                } else {
                    AndroidPackage androidPackage2 = this.mPackageManagerInt.getPackage(permission2.getPackageName());
                    PackageStateInternal packageStateInternal = this.mPackageManagerInt.getPackageStateInternal(permission2.getPackageName());
                    synchronized (this.mLock) {
                        if (androidPackage2 == null || packageStateInternal == null) {
                            Slog.w(TAG, "Removing dangling permission: " + permission2.getName() + " from package " + permission2.getPackageName());
                            this.mRegistry.removePermission(permission2.getName());
                        }
                    }
                }
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePermissionSourcePackage$12(Permission permission, int i, PermissionCallback permissionCallback, AndroidPackage androidPackage) {
        revokePermissionFromPackageForUser(androidPackage.getPackageName(), permission.getName(), true, i, permissionCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePermissionSourcePackage$13(Permission permission, AndroidPackage androidPackage) {
        int[] userIds = this.mUserManagerInt.getUserIds();
        synchronized (this.mLock) {
            for (int i : userIds) {
                UidPermissionState uidStateLocked = getUidStateLocked(androidPackage, i);
                if (uidStateLocked == null) {
                    Slog.e(TAG, "Missing permissions state for " + androidPackage.getPackageName() + " and user " + i);
                } else {
                    uidStateLocked.removePermissionState(permission.getName());
                }
            }
        }
    }

    private void updatePermissionGroupSourcePackage(String str) {
        ArraySet<ParsedPermissionGroup> arraySet;
        if (str == null) {
            return;
        }
        synchronized (this.mLock) {
            arraySet = null;
            for (ParsedPermissionGroup parsedPermissionGroup : this.mRegistry.getPermissionGroups()) {
                if (str.equals(parsedPermissionGroup.getPackageName())) {
                    if (arraySet == null) {
                        arraySet = new ArraySet();
                    }
                    arraySet.add(parsedPermissionGroup);
                }
            }
        }
        if (arraySet != null) {
            AndroidPackage androidPackage = this.mPackageManagerInt.getPackage(str);
            for (ParsedPermissionGroup parsedPermissionGroup2 : arraySet) {
                if (androidPackage == null) {
                    synchronized (this.mLock) {
                        Slog.w(TAG, "Removing permission group: " + parsedPermissionGroup2.getName() + " from package " + str + " that was not existed");
                        this.mRegistry.removePermissionGroup(parsedPermissionGroup2.getName());
                    }
                } else {
                    AndroidPackage androidPackage2 = this.mPackageManagerInt.getPackage(parsedPermissionGroup2.getPackageName());
                    PackageStateInternal packageStateInternal = this.mPackageManagerInt.getPackageStateInternal(parsedPermissionGroup2.getPackageName());
                    synchronized (this.mLock) {
                        if (androidPackage2 == null || packageStateInternal == null) {
                            Slog.w(TAG, "Removing dangling permission group: " + parsedPermissionGroup2.getName() + " from package " + parsedPermissionGroup2.getPackageName());
                            this.mRegistry.removePermissionGroup(parsedPermissionGroup2.getName());
                        }
                    }
                }
            }
        }
    }

    private boolean isPermissionDeclaredByDisabledSystemPkg(Permission permission) {
        PackageStateInternal disabledSystemPackage = this.mPackageManagerInt.getDisabledSystemPackage(permission.getPackageName());
        if (disabledSystemPackage == null || disabledSystemPackage.getPkg() == null) {
            return false;
        }
        String name = permission.getName();
        for (ParsedPermission parsedPermission : disabledSystemPackage.getPkg().getPermissions()) {
            if (TextUtils.equals(name, parsedPermission.getName()) && permission.getProtectionLevel() == parsedPermission.getProtectionLevel()) {
                return true;
            }
        }
        return false;
    }

    private void revokePermissionFromPackageForUser(String str, String str2, boolean z, int i, PermissionCallback permissionCallback) {
        ApplicationInfo applicationInfo = this.mPackageManagerInt.getApplicationInfo(str, 0L, 1000, 0);
        if ((applicationInfo == null || applicationInfo.targetSdkVersion >= 23) && checkPermission(str, str2, i) == 0) {
            try {
                revokeRuntimePermissionInternal(str, str2, z, 1000, i, null, permissionCallback);
            } catch (IllegalArgumentException e) {
                Slog.e(TAG, "Failed to revoke " + str2 + " from " + str, e);
            }
        }
    }

    private boolean updatePermissionTreeSourcePackage(String str, AndroidPackage androidPackage) {
        boolean z;
        if (str == null) {
            return true;
        }
        synchronized (this.mLock) {
            Iterator<Permission> it = this.mRegistry.getPermissionTrees().iterator();
            z = false;
            while (it.hasNext()) {
                Permission next = it.next();
                if (str.equals(next.getPackageName())) {
                    if (androidPackage == null || !hasPermission(androidPackage, next.getName())) {
                        Slog.i(TAG, "Removing permission tree " + next.getName() + " that used to be declared by " + next.getPackageName());
                        it.remove();
                    }
                    z = true;
                }
            }
        }
        return z;
    }

    private void enforceGrantRevokeRuntimePermissionPermissions(String str) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.GRANT_RUNTIME_PERMISSIONS") == 0 || this.mContext.checkCallingOrSelfPermission("android.permission.REVOKE_RUNTIME_PERMISSIONS") == 0) {
            return;
        }
        throw new SecurityException(str + " requires android.permission.GRANT_RUNTIME_PERMISSIONS or android.permission.REVOKE_RUNTIME_PERMISSIONS");
    }

    private void enforceGrantRevokeGetRuntimePermissionPermissions(String str) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.GET_RUNTIME_PERMISSIONS") == 0 || this.mContext.checkCallingOrSelfPermission("android.permission.GRANT_RUNTIME_PERMISSIONS") == 0 || this.mContext.checkCallingOrSelfPermission("android.permission.REVOKE_RUNTIME_PERMISSIONS") == 0) {
            return;
        }
        throw new SecurityException(str + " requires android.permission.GRANT_RUNTIME_PERMISSIONS or android.permission.REVOKE_RUNTIME_PERMISSIONS or android.permission.GET_RUNTIME_PERMISSIONS");
    }

    private void enforceCrossUserPermission(int i, int i2, boolean z, boolean z2, String str) {
        if (i2 < 0) {
            throw new IllegalArgumentException("Invalid userId " + i2);
        }
        if (z2) {
            enforceShellRestriction("no_debugging_features", i, i2);
        }
        if (checkCrossUserPermission(i, UserHandle.getUserId(i), i2, z)) {
            return;
        }
        String buildInvalidCrossUserPermissionMessage = buildInvalidCrossUserPermissionMessage(i, i2, str, z);
        Slog.w(TAG, buildInvalidCrossUserPermissionMessage);
        throw new SecurityException(buildInvalidCrossUserPermissionMessage);
    }

    private void enforceShellRestriction(String str, int i, int i2) {
        if (i == 2000) {
            if (i2 >= 0 && this.mUserManagerInt.hasUserRestriction(str, i2)) {
                throw new SecurityException("Shell does not have permission to access user " + i2);
            }
            if (i2 < 0) {
                Slog.e(LOG_TAG, "Unable to check shell permission for user " + i2 + "\n\t" + Debug.getCallers(3));
            }
        }
    }

    private boolean checkCrossUserPermission(int i, int i2, int i3, boolean z) {
        if (i3 == i2 || i == 1000 || i == 0) {
            return true;
        }
        if (z) {
            return checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL");
        }
        return checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL") || checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS");
    }

    private boolean checkCallingOrSelfPermission(String str) {
        return this.mContext.checkCallingOrSelfPermission(str) == 0;
    }

    private static String buildInvalidCrossUserPermissionMessage(int i, int i2, String str, boolean z) {
        StringBuilder sb = new StringBuilder();
        if (str != null) {
            sb.append(str);
            sb.append(": ");
        }
        sb.append("UID ");
        sb.append(i);
        sb.append(" requires ");
        sb.append("android.permission.INTERACT_ACROSS_USERS_FULL");
        if (!z) {
            sb.append(" or ");
            sb.append("android.permission.INTERACT_ACROSS_USERS");
        }
        sb.append(" to access user ");
        sb.append(i2);
        sb.append(".");
        return sb.toString();
    }

    @GuardedBy({"mLock"})
    private int calculateCurrentPermissionFootprintLocked(Permission permission) {
        Iterator<Permission> it = this.mRegistry.getPermissions().iterator();
        int i = 0;
        while (it.hasNext()) {
            i += permission.calculateFootprint(it.next());
        }
        return i;
    }

    @GuardedBy({"mLock"})
    private void enforcePermissionCapLocked(PermissionInfo permissionInfo, Permission permission) {
        if (permission.getUid() != 1000 && calculateCurrentPermissionFootprintLocked(permission) + permissionInfo.calculateFootprint() > 32768) {
            throw new SecurityException("Permission tree size cap exceeded");
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onSystemReady() {
        updateAllPermissions(StorageManager.UUID_PRIVATE_INTERNAL, false);
        ((PermissionPolicyInternal) LocalServices.getService(PermissionPolicyInternal.class)).setOnInitializedCallback(new PermissionPolicyInternal.OnInitializedCallback() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda18
            @Override // com.android.server.policy.PermissionPolicyInternal.OnInitializedCallback
            public final void onInitialized(int i) {
                PermissionManagerServiceImpl.this.lambda$onSystemReady$14(i);
            }
        });
        synchronized (this.mLock) {
            this.mSystemReady = true;
            ArraySet<String> arraySet = this.mPrivappPermissionsViolations;
            if (arraySet != null) {
                this.mPermissionManagerServiceExt.hookShouldDowngradePrivPermViolation(arraySet);
            }
        }
        this.mPermissionControllerManager = new PermissionControllerManager(this.mContext, PermissionThread.getHandler());
        this.mPermissionPolicyInternal = (PermissionPolicyInternal) LocalServices.getService(PermissionPolicyInternal.class);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSystemReady$14(int i) {
        updateAllPermissions(StorageManager.UUID_PRIVATE_INTERNAL, false);
    }

    private static String getVolumeUuidForPackage(AndroidPackage androidPackage) {
        if (androidPackage == null) {
            return StorageManager.UUID_PRIVATE_INTERNAL;
        }
        if (androidPackage.isExternalStorage()) {
            return TextUtils.isEmpty(androidPackage.getVolumeUuid()) ? "primary_physical" : androidPackage.getVolumeUuid();
        }
        return StorageManager.UUID_PRIVATE_INTERNAL;
    }

    private static boolean hasPermission(AndroidPackage androidPackage, String str) {
        if (androidPackage.getPermissions().isEmpty()) {
            return false;
        }
        for (int size = androidPackage.getPermissions().size() - 1; size >= 0; size--) {
            if (androidPackage.getPermissions().get(size).getName().equals(str)) {
                return true;
            }
        }
        return false;
    }

    private void logPermission(int i, String str, String str2) {
        LogMaker logMaker = new LogMaker(i);
        logMaker.setPackageName(str2);
        logMaker.addTaggedData(1241, str);
        this.mMetricsLogger.write(logMaker);
    }

    @GuardedBy({"mLock"})
    private UidPermissionState getUidStateLocked(PackageStateInternal packageStateInternal, int i) {
        return getUidStateLocked(packageStateInternal.getAppId(), i);
    }

    @GuardedBy({"mLock"})
    private UidPermissionState getUidStateLocked(AndroidPackage androidPackage, int i) {
        return getUidStateLocked(androidPackage.getUid(), i);
    }

    @GuardedBy({"mLock"})
    private UidPermissionState getUidStateLocked(int i, int i2) {
        UserPermissionState userState = this.mState.getUserState(i2);
        if (userState == null) {
            return null;
        }
        return userState.getUidState(i);
    }

    private void removeUidStateAndResetPackageInstallPermissionsFixed(int i, String str, int i2) {
        synchronized (this.mLock) {
            UserPermissionState userState = this.mState.getUserState(i2);
            if (userState == null) {
                return;
            }
            userState.removeUidState(i);
            userState.setInstallPermissionsFixed(str, false);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void readLegacyPermissionStateTEMP() {
        final int[] allUserIds = getAllUserIds();
        this.mPackageManagerInt.forEachPackageState(new Consumer() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda15
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                PermissionManagerServiceImpl.this.lambda$readLegacyPermissionStateTEMP$15(allUserIds, (PackageStateInternal) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$readLegacyPermissionStateTEMP$15(int[] iArr, PackageStateInternal packageStateInternal) {
        LegacyPermissionState legacyPermissionState;
        int appId = packageStateInternal.getAppId();
        if (packageStateInternal.hasSharedUser()) {
            int sharedUserAppId = packageStateInternal.getSharedUserAppId();
            SharedUserApi sharedUserApi = this.mPackageManagerInt.getSharedUserApi(sharedUserAppId);
            if (sharedUserApi == null) {
                Slog.wtf(TAG, "Missing shared user Api for " + sharedUserAppId);
                return;
            }
            legacyPermissionState = sharedUserApi.getSharedUserLegacyPermissionState();
        } else {
            legacyPermissionState = packageStateInternal.getLegacyPermissionState();
        }
        synchronized (this.mLock) {
            for (int i : iArr) {
                UserPermissionState orCreateUserState = this.mState.getOrCreateUserState(i);
                orCreateUserState.setInstallPermissionsFixed(packageStateInternal.getPackageName(), packageStateInternal.isInstallPermissionsFixed());
                UidPermissionState orCreateUidState = orCreateUserState.getOrCreateUidState(appId);
                orCreateUidState.reset();
                orCreateUidState.setMissing(legacyPermissionState.isMissing(i));
                readLegacyPermissionStatesLocked(orCreateUidState, legacyPermissionState.getPermissionStates(i));
            }
        }
    }

    @GuardedBy({"mLock"})
    private void readLegacyPermissionStatesLocked(UidPermissionState uidPermissionState, Collection<LegacyPermissionState.PermissionState> collection) {
        for (LegacyPermissionState.PermissionState permissionState : collection) {
            String name = permissionState.getName();
            Permission permission = this.mRegistry.getPermission(name);
            if (permission == null) {
                Slog.w(TAG, "Unknown permission: " + name);
            } else {
                uidPermissionState.putPermissionState(permission, permissionState.isGranted(), permissionState.getFlags());
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void writeLegacyPermissionStateTEMP() {
        final int[] userIds;
        synchronized (this.mLock) {
            userIds = this.mState.getUserIds();
        }
        this.mPackageManagerInt.forEachPackageSetting(new Consumer() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                PermissionManagerServiceImpl.this.lambda$writeLegacyPermissionStateTEMP$16(userIds, (PackageSetting) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$writeLegacyPermissionStateTEMP$16(int[] iArr, PackageSetting packageSetting) {
        LegacyPermissionState legacyPermissionState;
        PermissionManagerServiceImpl permissionManagerServiceImpl = this;
        int i = 0;
        packageSetting.setInstallPermissionsFixed(false);
        if (packageSetting.hasSharedUser()) {
            int sharedUserAppId = packageSetting.getSharedUserAppId();
            SharedUserApi sharedUserApi = permissionManagerServiceImpl.mPackageManagerInt.getSharedUserApi(sharedUserAppId);
            if (sharedUserApi == null) {
                Slog.wtf(TAG, "Missing shared user Api for " + sharedUserAppId);
                return;
            }
            legacyPermissionState = sharedUserApi.getSharedUserLegacyPermissionState();
        } else {
            legacyPermissionState = packageSetting.getLegacyPermissionState();
        }
        legacyPermissionState.reset();
        int appId = packageSetting.getAppId();
        synchronized (permissionManagerServiceImpl.mLock) {
            int length = iArr.length;
            int i2 = 0;
            while (i2 < length) {
                int i3 = iArr[i2];
                UserPermissionState userState = permissionManagerServiceImpl.mState.getUserState(i3);
                if (userState == null) {
                    Slog.e(TAG, "Missing user state for " + i3);
                } else {
                    if (userState.areInstallPermissionsFixed(packageSetting.getPackageName())) {
                        packageSetting.setInstallPermissionsFixed(true);
                    }
                    UidPermissionState uidState = userState.getUidState(appId);
                    if (uidState == null) {
                        Slog.e(TAG, "Missing permission state for " + packageSetting.getPackageName() + " and user " + i3);
                    } else {
                        legacyPermissionState.setMissing(uidState.isMissing(), i3);
                        List<PermissionState> permissionStates = uidState.getPermissionStates();
                        int size = permissionStates.size();
                        for (int i4 = i; i4 < size; i4++) {
                            PermissionState permissionState = permissionStates.get(i4);
                            legacyPermissionState.putPermissionState(new LegacyPermissionState.PermissionState(permissionState.getName(), permissionState.getPermission().isRuntime(), permissionState.isGranted(), permissionState.getFlags()), i3);
                        }
                    }
                }
                i2++;
                permissionManagerServiceImpl = this;
                i = 0;
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void readLegacyPermissionsTEMP(LegacyPermissionSettings legacyPermissionSettings) {
        List<LegacyPermission> permissionTrees;
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                permissionTrees = legacyPermissionSettings.getPermissions();
            } else {
                permissionTrees = legacyPermissionSettings.getPermissionTrees();
            }
            synchronized (this.mLock) {
                int size = permissionTrees.size();
                for (int i2 = 0; i2 < size; i2++) {
                    LegacyPermission legacyPermission = permissionTrees.get(i2);
                    Permission permission = new Permission(legacyPermission.getPermissionInfo(), legacyPermission.getType());
                    if (i == 0) {
                        Permission permission2 = this.mRegistry.getPermission(permission.getName());
                        if (permission2 != null && permission2.getType() == 1) {
                            permission.setGids(permission2.getRawGids(), permission2.areGidsPerUser());
                        }
                        this.mRegistry.addPermission(permission);
                    } else {
                        this.mRegistry.addPermissionTree(permission);
                    }
                }
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void writeLegacyPermissionsTEMP(LegacyPermissionSettings legacyPermissionSettings) {
        int i = 0;
        while (i < 2) {
            ArrayList arrayList = new ArrayList();
            synchronized (this.mLock) {
                for (Permission permission : i == 0 ? this.mRegistry.getPermissions() : this.mRegistry.getPermissionTrees()) {
                    arrayList.add(new LegacyPermission(permission.getPermissionInfo(), permission.getType(), 0, EmptyArray.INT));
                }
            }
            if (i == 0) {
                legacyPermissionSettings.replacePermissions(arrayList);
            } else {
                legacyPermissionSettings.replacePermissionTrees(arrayList);
            }
            i++;
        }
    }

    private void onPackageAddedInternal(PackageState packageState, final AndroidPackage androidPackage, boolean z, final AndroidPackage androidPackage2) {
        List<String> addAllPermissionsInternal;
        if (!androidPackage.getAdoptPermissions().isEmpty()) {
            for (int size = androidPackage.getAdoptPermissions().size() - 1; size >= 0; size--) {
                String str = androidPackage.getAdoptPermissions().get(size);
                if (canAdoptPermissionsInternal(str, androidPackage)) {
                    Slog.i(TAG, "Adopting permissions from " + str + " to " + androidPackage.getPackageName());
                    synchronized (this.mLock) {
                        this.mRegistry.transferPermissions(str, androidPackage.getPackageName());
                    }
                }
            }
        }
        if (z) {
            Slog.w(TAG, "Permission groups from package " + androidPackage.getPackageName() + " ignored: instant apps cannot define new permission groups.");
        } else {
            addAllPermissionGroupsInternal(androidPackage);
        }
        if (z) {
            Slog.w(TAG, "Permissions from package " + androidPackage.getPackageName() + " ignored: instant apps cannot define new permissions.");
            addAllPermissionsInternal = null;
        } else {
            addAllPermissionsInternal = addAllPermissionsInternal(packageState, androidPackage);
        }
        final List<String> list = addAllPermissionsInternal;
        final boolean z2 = androidPackage2 != null;
        final boolean z3 = !CollectionUtils.isEmpty(list);
        if (z2 || z3) {
            AsyncTask.execute(new Runnable() { // from class: com.android.server.pm.permission.PermissionManagerServiceImpl$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    PermissionManagerServiceImpl.this.lambda$onPackageAddedInternal$17(z2, androidPackage, androidPackage2, z3, list);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPackageAddedInternal$17(boolean z, AndroidPackage androidPackage, AndroidPackage androidPackage2, boolean z2, List list) {
        if (z) {
            revokeRuntimePermissionsIfGroupChangedInternal(androidPackage, androidPackage2);
            revokeStoragePermissionsIfScopeExpandedInternal(androidPackage, androidPackage2);
            revokeSystemAlertWindowIfUpgradedPast23(androidPackage, androidPackage2);
        }
        if (z2) {
            revokeRuntimePermissionsIfPermissionDefinitionChangedInternal(list);
        }
    }

    private boolean canAdoptPermissionsInternal(String str, AndroidPackage androidPackage) {
        PackageStateInternal packageStateInternal = this.mPackageManagerInt.getPackageStateInternal(str);
        if (packageStateInternal == null) {
            return false;
        }
        if (!packageStateInternal.isSystem()) {
            Slog.w(TAG, "Unable to update from " + packageStateInternal.getPackageName() + " to " + androidPackage.getPackageName() + ": old package not in system partition");
            return false;
        }
        if (this.mPackageManagerInt.getPackage(packageStateInternal.getPackageName()) == null) {
            return true;
        }
        Slog.w(TAG, "Unable to update from " + packageStateInternal.getPackageName() + " to " + androidPackage.getPackageName() + ": old package still exists");
        return false;
    }

    private boolean isEffectivelyGranted(PermissionState permissionState) {
        int flags = permissionState.getFlags();
        if ((flags & 16) != 0) {
            return true;
        }
        if ((flags & 4) != 0) {
            return (flags & 8) == 0 && permissionState.isGranted();
        }
        if ((flags & 65608) != 0) {
            return false;
        }
        return permissionState.isGranted();
    }

    /* JADX WARN: Code restructure failed: missing block: B:48:0x0072, code lost:
    
        if (r2 == false) goto L51;
     */
    /* JADX WARN: Removed duplicated region for block: B:22:0x008b  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0096  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x009c  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0092  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private Pair<Boolean, Integer> mergePermissionState(int i, PermissionState permissionState, PermissionState permissionState2) {
        int i2;
        int flags = permissionState2.getFlags();
        boolean isEffectivelyGranted = isEffectivelyGranted(permissionState2);
        int flags2 = permissionState.getFlags();
        boolean isEffectivelyGranted2 = isEffectivelyGranted(permissionState);
        int i3 = flags | flags2;
        int i4 = (524291 & flags) | 0 | (i3 & 14336);
        if ((i4 & 14336) == 0) {
            i4 |= 16384;
        }
        int i5 = i3 & 32820;
        int i6 = i4 | i5;
        if (i5 == 0) {
            i6 |= i3 & 128;
        }
        if ((i6 & 20) == 0) {
            if ((557091 & i6) == 0 && NOTIFICATION_PERMISSIONS.contains(permissionState.getName())) {
                i2 = i3 & 64;
            } else if ((32820 & i6) == 0) {
                i2 = flags & 64;
            }
            i6 |= i2;
        }
        boolean z = true;
        if ((i6 & 16) == 0) {
            if ((flags & 4) == 0) {
                if ((flags2 & 4) != 0) {
                    r5 = isEffectivelyGranted || isEffectivelyGranted2;
                    if (isEffectivelyGranted != isEffectivelyGranted2) {
                        i6 &= -5;
                    }
                } else if ((flags & 32800) == 0) {
                    if ((32800 & flags2) != 0) {
                        if (!isEffectivelyGranted) {
                        }
                    } else if ((flags & 128) == 0 && (flags2 & 128) != 0) {
                        r5 = isEffectivelyGranted || isEffectivelyGranted2;
                        if (isEffectivelyGranted) {
                            i6 &= -129;
                        }
                    }
                }
                int i7 = !r5 ? ((131072 & i3) | i6) & (-129) : i6 & (-65);
                if (r5 != isEffectivelyGranted) {
                    i7 &= -524292;
                }
                if (r5 && isPermissionSplitFromNonRuntime(permissionState.getName(), this.mPackageManagerInt.getUidTargetSdkVersion(i))) {
                    i7 |= 8;
                } else {
                    z = r5;
                }
                return new Pair<>(Boolean.valueOf(z), Integer.valueOf(i7));
            }
            r5 = isEffectivelyGranted;
            if (!r5) {
            }
            if (r5 != isEffectivelyGranted) {
            }
            if (r5) {
            }
            z = r5;
            return new Pair<>(Boolean.valueOf(z), Integer.valueOf(i7));
        }
        r5 = true;
        if (!r5) {
        }
        if (r5 != isEffectivelyGranted) {
        }
        if (r5) {
        }
        z = r5;
        return new Pair<>(Boolean.valueOf(z), Integer.valueOf(i7));
    }

    private void handleAppIdMigration(AndroidPackage androidPackage, int i) {
        UidPermissionState uidState;
        PackageStateInternal packageStateInternal = this.mPackageManagerInt.getPackageStateInternal(androidPackage.getPackageName());
        if (packageStateInternal.hasSharedUser()) {
            synchronized (this.mLock) {
                for (int i2 : getAllUserIds()) {
                    UserPermissionState orCreateUserState = this.mState.getOrCreateUserState(i2);
                    UidPermissionState uidState2 = orCreateUserState.getUidState(i);
                    if (uidState2 != null) {
                        UidPermissionState uidState3 = orCreateUserState.getUidState(packageStateInternal.getAppId());
                        if (uidState3 == null) {
                            orCreateUserState.createUidStateWithExisting(packageStateInternal.getAppId(), uidState2);
                        } else {
                            List<PermissionState> permissionStates = uidState2.getPermissionStates();
                            int size = permissionStates.size();
                            for (int i3 = 0; i3 < size; i3++) {
                                PermissionState permissionState = permissionStates.get(i3);
                                PermissionState permissionState2 = uidState3.getPermissionState(permissionState.getName());
                                if (permissionState2 != null) {
                                    Pair<Boolean, Integer> mergePermissionState = mergePermissionState(packageStateInternal.getAppId(), permissionState, permissionState2);
                                    uidState3.putPermissionState(permissionState.getPermission(), ((Boolean) mergePermissionState.first).booleanValue(), ((Integer) mergePermissionState.second).intValue());
                                } else {
                                    uidState3.putPermissionState(permissionState.getPermission(), permissionState.isGranted(), permissionState.getFlags());
                                }
                            }
                        }
                        orCreateUserState.removeUidState(i);
                    }
                }
            }
            return;
        }
        List<AndroidPackage> packagesForAppId = this.mPackageManagerInt.getPackagesForAppId(i);
        synchronized (this.mLock) {
            for (int i4 : getAllUserIds()) {
                UserPermissionState userState = this.mState.getUserState(i4);
                if (userState != null && (uidState = userState.getUidState(i)) != null) {
                    userState.createUidStateWithExisting(packageStateInternal.getAppId(), uidState);
                    if (packagesForAppId.isEmpty()) {
                        removeUidStateAndResetPackageInstallPermissionsFixed(i, androidPackage.getPackageName(), i4);
                    } else {
                        revokeSharedUserPermissionsForLeavingPackageInternal(androidPackage, i, packagesForAppId, i4);
                    }
                }
            }
        }
    }

    private void onPackageInstalledInternal(AndroidPackage androidPackage, int i, PermissionManagerServiceInternal.PackageInstalledParams packageInstalledParams, int[] iArr) {
        if (i != -1) {
            handleAppIdMigration(androidPackage, i);
        }
        updatePermissions(androidPackage.getPackageName(), androidPackage);
        for (int i2 : iArr) {
            addAllowlistedRestrictedPermissionsInternal(androidPackage, packageInstalledParams.getAllowlistedRestrictedPermissions(), 2, i2);
            grantRequestedPermissionsInternal(androidPackage, packageInstalledParams.getPermissionStates(), i2);
        }
    }

    private void addAllowlistedRestrictedPermissionsInternal(AndroidPackage androidPackage, List<String> list, int i, int i2) {
        List<String> allowlistedRestrictedPermissionsInternal = getAllowlistedRestrictedPermissionsInternal(androidPackage, i, i2);
        if (allowlistedRestrictedPermissionsInternal != null) {
            ArraySet arraySet = new ArraySet(allowlistedRestrictedPermissionsInternal);
            arraySet.addAll(list);
            list = new ArrayList<>(arraySet);
        }
        setAllowlistedRestrictedPermissionsInternal(androidPackage, list, i, i2);
    }

    private void onPackageRemovedInternal(AndroidPackage androidPackage) {
        removeAllPermissionsInternal(androidPackage);
    }

    private void onPackageUninstalledInternal(String str, int i, PackageState packageState, AndroidPackage androidPackage, List<AndroidPackage> list, int[] iArr) {
        int i2 = 0;
        if (packageState.isSystem() && androidPackage != null && this.mPackageManagerInt.getPackage(str) != null) {
            int length = iArr.length;
            while (i2 < length) {
                resetRuntimePermissionsInternal(androidPackage, iArr[i2]);
                i2++;
            }
            return;
        }
        updatePermissions(str, null);
        int length2 = iArr.length;
        while (i2 < length2) {
            int i3 = iArr[i2];
            if (list.isEmpty()) {
                removeUidStateAndResetPackageInstallPermissionsFixed(i, str, i3);
            } else {
                revokeSharedUserPermissionsForLeavingPackageInternal(androidPackage, i, list, i3);
            }
            i2++;
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public List<LegacyPermission> getLegacyPermissions() {
        ArrayList arrayList;
        synchronized (this.mLock) {
            arrayList = new ArrayList();
            for (Permission permission : this.mRegistry.getPermissions()) {
                arrayList.add(new LegacyPermission(permission.getPermissionInfo(), permission.getType(), permission.getUid(), permission.getRawGids()));
            }
        }
        return arrayList;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public Map<String, Set<String>> getAllAppOpPermissionPackages() {
        ArrayMap arrayMap;
        synchronized (this.mLock) {
            ArrayMap<String, ArraySet<String>> allAppOpPermissionPackages = this.mRegistry.getAllAppOpPermissionPackages();
            arrayMap = new ArrayMap();
            int size = allAppOpPermissionPackages.size();
            for (int i = 0; i < size; i++) {
                arrayMap.put(allAppOpPermissionPackages.keyAt(i), new ArraySet((ArraySet) allAppOpPermissionPackages.valueAt(i)));
            }
        }
        return arrayMap;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public LegacyPermissionState getLegacyPermissionState(int i) {
        LegacyPermissionState legacyPermissionState = new LegacyPermissionState();
        synchronized (this.mLock) {
            for (int i2 : this.mState.getUserIds()) {
                UidPermissionState uidStateLocked = getUidStateLocked(i, i2);
                if (uidStateLocked == null) {
                    Slog.e(TAG, "Missing permissions state for app ID " + i + " and user ID " + i2);
                } else {
                    List<PermissionState> permissionStates = uidStateLocked.getPermissionStates();
                    int size = permissionStates.size();
                    for (int i3 = 0; i3 < size; i3++) {
                        PermissionState permissionState = permissionStates.get(i3);
                        legacyPermissionState.putPermissionState(new LegacyPermissionState.PermissionState(permissionState.getName(), permissionState.getPermission().isRuntime(), permissionState.isGranted(), permissionState.getFlags()), i2);
                    }
                }
            }
        }
        return legacyPermissionState;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int[] getGidsForUid(int i) {
        int appId = UserHandle.getAppId(i);
        int userId = UserHandle.getUserId(i);
        synchronized (this.mLock) {
            UidPermissionState uidStateLocked = getUidStateLocked(appId, userId);
            if (uidStateLocked == null) {
                Slog.e(TAG, "Missing permissions state for app ID " + appId + " and user ID " + userId);
                return EMPTY_INT_ARRAY;
            }
            return uidStateLocked.computeGids(this.mGlobalGids, userId);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean isPermissionsReviewRequired(String str, int i) {
        Objects.requireNonNull(str, DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
        return isPermissionsReviewRequiredInternal(str, i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public Set<String> getInstalledPermissions(String str) {
        Objects.requireNonNull(str, DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
        ArraySet arraySet = new ArraySet();
        synchronized (this.mLock) {
            for (Permission permission : this.mRegistry.getPermissions()) {
                if (Objects.equals(permission.getPackageName(), str)) {
                    arraySet.add(permission.getName());
                }
            }
        }
        return arraySet;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public Set<String> getGrantedPermissions(String str, int i) {
        Objects.requireNonNull(str, DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
        Preconditions.checkArgumentNonNegative(i, "userId");
        return getGrantedPermissionsInternal(str, i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int[] getPermissionGids(String str, int i) {
        Objects.requireNonNull(str, "permissionName");
        Preconditions.checkArgumentNonNegative(i, "userId");
        return getPermissionGidsInternal(str, i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public String[] getAppOpPermissionPackages(String str) {
        Objects.requireNonNull(str, "permissionName");
        return getAppOpPermissionPackagesInternal(str);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onStorageVolumeMounted(String str, boolean z) {
        updateAllPermissions(str, z);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void resetRuntimePermissions(AndroidPackage androidPackage, int i) {
        Objects.requireNonNull(androidPackage, "pkg");
        Preconditions.checkArgumentNonNegative(i, "userId");
        resetRuntimePermissionsInternal(androidPackage, i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void resetRuntimePermissionsForUser(int i) {
        Preconditions.checkArgumentNonNegative(i, "userId");
        resetRuntimePermissionsInternal(null, i);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public Permission getPermissionTEMP(String str) {
        Permission permission;
        synchronized (this.mLock) {
            permission = this.mRegistry.getPermission(str);
        }
        return permission;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public List<PermissionInfo> getAllPermissionsWithProtection(int i) {
        ArrayList arrayList = new ArrayList();
        synchronized (this.mLock) {
            for (Permission permission : this.mRegistry.getPermissions()) {
                if (permission.getProtection() == i) {
                    arrayList.add(permission.generatePermissionInfo(0));
                }
            }
        }
        return arrayList;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public List<PermissionInfo> getAllPermissionsWithProtectionFlags(int i) {
        ArrayList arrayList = new ArrayList();
        synchronized (this.mLock) {
            for (Permission permission : this.mRegistry.getPermissions()) {
                if ((permission.getProtectionFlags() & i) == i) {
                    arrayList.add(permission.generatePermissionInfo(0));
                }
            }
        }
        return arrayList;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onUserCreated(int i) {
        Preconditions.checkArgumentNonNegative(i, "userId");
        updateAllPermissions(StorageManager.UUID_PRIVATE_INTERNAL, true);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageAdded(PackageState packageState, boolean z, AndroidPackage androidPackage) {
        Objects.requireNonNull(packageState);
        AndroidPackage androidPackage2 = packageState.getAndroidPackage();
        Objects.requireNonNull(androidPackage2);
        onPackageAddedInternal(packageState, androidPackage2, z, androidPackage);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageInstalled(AndroidPackage androidPackage, int i, PermissionManagerServiceInternal.PackageInstalledParams packageInstalledParams, int i2) {
        int[] iArr;
        Objects.requireNonNull(androidPackage, "pkg");
        Objects.requireNonNull(packageInstalledParams, "params");
        Preconditions.checkArgument(i2 >= 0 || i2 == -1, "userId");
        if (i2 == -1) {
            iArr = getAllUserIds();
        } else {
            iArr = new int[]{i2};
        }
        onPackageInstalledInternal(androidPackage, i, packageInstalledParams, iArr);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageRemoved(AndroidPackage androidPackage) {
        Objects.requireNonNull(androidPackage);
        onPackageRemovedInternal(androidPackage);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0028 A[Catch: all -> 0x0041, TryCatch #0 {all -> 0x0041, blocks: (B:3:0x0005, B:8:0x0020, B:10:0x0028, B:11:0x0031, B:16:0x002d), top: B:2:0x0005 }] */
    /* JADX WARN: Removed duplicated region for block: B:16:0x002d A[Catch: all -> 0x0041, TryCatch #0 {all -> 0x0041, blocks: (B:3:0x0005, B:8:0x0020, B:10:0x0028, B:11:0x0031, B:16:0x002d), top: B:2:0x0005 }] */
    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onPackageUninstalled(String str, int i, PackageState packageState, AndroidPackage androidPackage, List<AndroidPackage> list, int i2) {
        boolean z;
        int[] iArr;
        this.mPermissionManagerServiceExt.beforeOnPackageUninstalled();
        try {
            Objects.requireNonNull(packageState, "packageState");
            Objects.requireNonNull(str, DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
            Objects.requireNonNull(list, "sharedUserPkgs");
            if (i2 < 0 && i2 != -1) {
                z = false;
                Preconditions.checkArgument(z, "userId");
                if (i2 != -1) {
                    iArr = getAllUserIds();
                } else {
                    iArr = new int[]{i2};
                }
                onPackageUninstalledInternal(str, i, packageState, androidPackage, list, iArr);
            }
            z = true;
            Preconditions.checkArgument(z, "userId");
            if (i2 != -1) {
            }
            onPackageUninstalledInternal(str, i, packageState, androidPackage, list, iArr);
        } finally {
            this.mPermissionManagerServiceExt.afterOnPackageUninstalled();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class PermissionCallback {
        public void onGidsChanged(int i, int i2) {
        }

        public void onInstallPermissionGranted() {
        }

        public void onInstallPermissionRevoked() {
        }

        public void onInstallPermissionUpdated() {
        }

        public void onPermissionChanged() {
        }

        public void onPermissionGranted(int i, int i2) {
        }

        public void onPermissionRemoved() {
        }

        public void onPermissionRevoked(int i, int i2, String str, boolean z, String str2) {
        }

        public void onPermissionUpdated(int[] iArr, boolean z) {
        }

        private PermissionCallback() {
        }

        public void onPermissionRevoked(int i, int i2, String str) {
            onPermissionRevoked(i, i2, str, false);
        }

        public void onPermissionRevoked(int i, int i2, String str, boolean z) {
            onPermissionRevoked(i, i2, str, false, null);
        }

        public void onPermissionUpdatedNotifyListener(int[] iArr, boolean z, int i) {
            onPermissionUpdated(iArr, z);
        }

        public void onInstallPermissionUpdatedNotifyListener(int i) {
            onInstallPermissionUpdated();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class OnPermissionChangeListeners extends Handler {
        private static final int MSG_ON_PERMISSIONS_CHANGED = 1;
        private final RemoteCallbackList<IOnPermissionsChangeListener> mPermissionListeners;

        OnPermissionChangeListeners(Looper looper) {
            super(looper);
            this.mPermissionListeners = new RemoteCallbackList<>();
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            handleOnPermissionsChanged(message.arg1);
        }

        public void addListener(IOnPermissionsChangeListener iOnPermissionsChangeListener) {
            this.mPermissionListeners.register(iOnPermissionsChangeListener);
        }

        public void removeListener(IOnPermissionsChangeListener iOnPermissionsChangeListener) {
            this.mPermissionListeners.unregister(iOnPermissionsChangeListener);
        }

        public void onPermissionsChanged(int i) {
            if (this.mPermissionListeners.getRegisteredCallbackCount() > 0) {
                obtainMessage(1, i, 0).sendToTarget();
            }
        }

        private void handleOnPermissionsChanged(int i) {
            int beginBroadcast = this.mPermissionListeners.beginBroadcast();
            for (int i2 = 0; i2 < beginBroadcast; i2++) {
                try {
                    try {
                        this.mPermissionListeners.getBroadcastItem(i2).onPermissionsChanged(i);
                    } catch (RemoteException e) {
                        Log.e(PermissionManagerServiceImpl.TAG, "Permission listener is dead", e);
                    }
                } finally {
                    this.mPermissionListeners.finishBroadcast();
                }
            }
        }
    }
}
