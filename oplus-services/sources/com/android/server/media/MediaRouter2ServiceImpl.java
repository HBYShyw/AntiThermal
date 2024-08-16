package com.android.server.media;

import android.annotation.RequiresPermission;
import android.app.ActivityManager;
import android.app.ActivityThread;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.IMediaRouter2;
import android.media.IMediaRouter2Manager;
import android.media.MediaRoute2Info;
import android.media.MediaRoute2ProviderInfo;
import android.media.MediaRouter2Utils;
import android.media.RouteDiscoveryPreference;
import android.media.RouteListingPreference;
import android.media.RoutingSessionInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.function.HeptConsumer;
import com.android.internal.util.function.HexConsumer;
import com.android.internal.util.function.QuadConsumer;
import com.android.internal.util.function.TriConsumer;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.server.LocalServices;
import com.android.server.media.MediaRoute2Provider;
import com.android.server.media.MediaRoute2ProviderWatcher;
import com.android.server.media.MediaRouter2ServiceImpl;
import com.android.server.pm.UserManagerInternal;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class MediaRouter2ServiceImpl {
    private static final long DUMMY_REQUEST_ID = -1;
    final ActivityManager mActivityManager;
    private final Context mContext;
    private final ActivityManager.OnUidImportanceListener mOnUidImportanceListener;
    final PowerManager mPowerManager;
    private final BroadcastReceiver mScreenOnOffReceiver;
    private final UserManagerInternal mUserManagerInternal;
    private static final String TAG = "MR2ServiceImpl";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String[] BLUETOOTH_PERMISSIONS_FOR_SYSTEM_ROUTING = {"android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"};
    private static final String MEDIA_BETTER_TOGETHER_NAMESPACE = "media_better_together";
    private static final String KEY_SCANNING_PACKAGE_MINIMUM_IMPORTANCE = "scanning_package_minimum_importance";
    private static int sPackageImportanceForScanning = DeviceConfig.getInt(MEDIA_BETTER_TOGETHER_NAMESPACE, KEY_SCANNING_PACKAGE_MINIMUM_IMPORTANCE, 125);
    private final Object mLock = new Object();
    final AtomicInteger mNextRouterOrManagerId = new AtomicInteger(1);

    @GuardedBy({"mLock"})
    private final SparseArray<UserRecord> mUserRecords = new SparseArray<>();

    @GuardedBy({"mLock"})
    private final ArrayMap<IBinder, RouterRecord> mAllRouterRecords = new ArrayMap<>();

    @GuardedBy({"mLock"})
    private final ArrayMap<IBinder, ManagerRecord> mAllManagerRecords = new ArrayMap<>();

    @GuardedBy({"mLock"})
    private int mCurrentActiveUserId = -1;

    static int toOriginalRequestId(long j) {
        return (int) j;
    }

    static int toRequesterId(long j) {
        return (int) (j >> 32);
    }

    static long toUniqueRequestId(int i, int i2) {
        return i2 | (i << 32);
    }

    public /* synthetic */ void lambda$new$0(int i, int i2) {
        synchronized (this.mLock) {
            int size = this.mUserRecords.size();
            for (int i3 = 0; i3 < size; i3++) {
                this.mUserRecords.valueAt(i3).mHandler.maybeUpdateDiscoveryPreferenceForUid(i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.media.MediaRouter2ServiceImpl$1 */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass1 extends BroadcastReceiver {
        AnonymousClass1() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            synchronized (MediaRouter2ServiceImpl.this.mLock) {
                int size = MediaRouter2ServiceImpl.this.mUserRecords.size();
                for (int i = 0; i < size; i++) {
                    UserHandler userHandler = ((UserRecord) MediaRouter2ServiceImpl.this.mUserRecords.valueAt(i)).mHandler;
                    userHandler.sendMessage(PooledLambda.obtainMessage(new Consumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$1$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            ((MediaRouter2ServiceImpl.UserHandler) obj).updateDiscoveryPreferenceOnHandler();
                        }
                    }, userHandler));
                }
            }
        }
    }

    @RequiresPermission("android.permission.OBSERVE_GRANT_REVOKE_PERMISSIONS")
    public MediaRouter2ServiceImpl(Context context) {
        ActivityManager.OnUidImportanceListener onUidImportanceListener = new ActivityManager.OnUidImportanceListener() { // from class: com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda11
            public final void onUidImportance(int i, int i2) {
                MediaRouter2ServiceImpl.this.lambda$new$0(i, i2);
            }
        };
        this.mOnUidImportanceListener = onUidImportanceListener;
        AnonymousClass1 anonymousClass1 = new AnonymousClass1();
        this.mScreenOnOffReceiver = anonymousClass1;
        this.mContext = context;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ActivityManager.class);
        this.mActivityManager = activityManager;
        activityManager.addOnUidImportanceListener(onUidImportanceListener, sPackageImportanceForScanning);
        this.mPowerManager = (PowerManager) context.getSystemService(PowerManager.class);
        this.mUserManagerInternal = (UserManagerInternal) LocalServices.getService(UserManagerInternal.class);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        context.registerReceiver(anonymousClass1, intentFilter);
        context.getPackageManager().addOnPermissionsChangeListener(new PackageManager.OnPermissionsChangedListener() { // from class: com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda12
            public final void onPermissionsChanged(int i) {
                MediaRouter2ServiceImpl.this.onPermissionsChanged(i);
            }
        });
        DeviceConfig.addOnPropertiesChangedListener(MEDIA_BETTER_TOGETHER_NAMESPACE, ActivityThread.currentApplication().getMainExecutor(), new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda13
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                MediaRouter2ServiceImpl.this.onDeviceConfigChange(properties);
            }
        });
    }

    public void onPermissionsChanged(final int i) {
        synchronized (this.mLock) {
            Optional<RouterRecord> findFirst = this.mAllRouterRecords.values().stream().filter(new Predicate() { // from class: com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$onPermissionsChanged$1;
                    lambda$onPermissionsChanged$1 = MediaRouter2ServiceImpl.lambda$onPermissionsChanged$1(i, (MediaRouter2ServiceImpl.RouterRecord) obj);
                    return lambda$onPermissionsChanged$1;
                }
            }).findFirst();
            if (findFirst.isPresent()) {
                findFirst.get().maybeUpdateSystemRoutingPermissionLocked();
            }
        }
    }

    public static /* synthetic */ boolean lambda$onPermissionsChanged$1(int i, RouterRecord routerRecord) {
        return routerRecord.mUid == i;
    }

    public boolean verifyPackageExists(String str) {
        int callingPid = Binder.getCallingPid();
        int callingUid = Binder.getCallingUid();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mContext.enforcePermission("android.permission.MEDIA_CONTENT_CONTROL", callingPid, callingUid, "Must hold MEDIA_CONTENT_CONTROL permission.");
            this.mContext.getPackageManager().getPackageInfo(str, PackageManager.PackageInfoFlags.of(0L));
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return false;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }

    public List<MediaRoute2Info> getSystemRoutes() {
        Collection collection;
        int identifier = UserHandle.getUserHandleForUid(Binder.getCallingUid()).getIdentifier();
        boolean z = this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_AUDIO_ROUTING") == 0;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                UserRecord orCreateUserRecordLocked = getOrCreateUserRecordLocked(identifier);
                if (z) {
                    MediaRoute2ProviderInfo providerInfo = orCreateUserRecordLocked.mHandler.mSystemProvider.getProviderInfo();
                    if (providerInfo != null) {
                        collection = providerInfo.getRoutes();
                    } else {
                        collection = Collections.emptyList();
                    }
                } else {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(orCreateUserRecordLocked.mHandler.mSystemProvider.getDefaultRoute());
                    collection = arrayList;
                }
            }
            return new ArrayList(collection);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void registerRouter2(IMediaRouter2 iMediaRouter2, String str) {
        Objects.requireNonNull(iMediaRouter2, "router must not be null");
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("packageName must not be empty");
        }
        int callingUid = Binder.getCallingUid();
        int callingPid = Binder.getCallingPid();
        int identifier = UserHandle.getUserHandleForUid(callingUid).getIdentifier();
        boolean z = this.mContext.checkCallingOrSelfPermission("android.permission.CONFIGURE_WIFI_DISPLAY") == 0;
        boolean z2 = this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_AUDIO_ROUTING") == 0;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                registerRouter2Locked(iMediaRouter2, callingUid, callingPid, str, identifier, z, z2);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void unregisterRouter2(IMediaRouter2 iMediaRouter2) {
        Objects.requireNonNull(iMediaRouter2, "router must not be null");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                unregisterRouter2Locked(iMediaRouter2, false);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void setDiscoveryRequestWithRouter2(IMediaRouter2 iMediaRouter2, RouteDiscoveryPreference routeDiscoveryPreference) {
        Objects.requireNonNull(iMediaRouter2, "router must not be null");
        Objects.requireNonNull(routeDiscoveryPreference, "preference must not be null");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                RouterRecord routerRecord = this.mAllRouterRecords.get(iMediaRouter2.asBinder());
                if (routerRecord == null) {
                    Slog.w(TAG, "Ignoring updating discoveryRequest of null routerRecord.");
                } else {
                    setDiscoveryRequestWithRouter2Locked(routerRecord, routeDiscoveryPreference);
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void setRouteListingPreference(IMediaRouter2 iMediaRouter2, RouteListingPreference routeListingPreference) {
        ComponentName linkedItemComponentName = routeListingPreference != null ? routeListingPreference.getLinkedItemComponentName() : null;
        if (linkedItemComponentName != null) {
            MediaServerUtils.enforcePackageName(linkedItemComponentName.getPackageName(), Binder.getCallingUid());
            if (!MediaServerUtils.isValidActivityComponentName(this.mContext, linkedItemComponentName, "android.media.action.TRANSFER_MEDIA", Binder.getCallingUserHandle())) {
                throw new IllegalArgumentException("Unable to resolve " + linkedItemComponentName + " to a valid activity for android.media.action.TRANSFER_MEDIA");
            }
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                RouterRecord routerRecord = this.mAllRouterRecords.get(iMediaRouter2.asBinder());
                if (routerRecord == null) {
                    Slog.w(TAG, "Ignoring updating route listing of null routerRecord.");
                } else {
                    setRouteListingPreferenceLocked(routerRecord, routeListingPreference);
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void setRouteVolumeWithRouter2(IMediaRouter2 iMediaRouter2, MediaRoute2Info mediaRoute2Info, int i) {
        Objects.requireNonNull(iMediaRouter2, "router must not be null");
        Objects.requireNonNull(mediaRoute2Info, "route must not be null");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                setRouteVolumeWithRouter2Locked(iMediaRouter2, mediaRoute2Info, i);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void requestCreateSessionWithRouter2(IMediaRouter2 iMediaRouter2, int i, long j, RoutingSessionInfo routingSessionInfo, MediaRoute2Info mediaRoute2Info, Bundle bundle) {
        Objects.requireNonNull(iMediaRouter2, "router must not be null");
        Objects.requireNonNull(routingSessionInfo, "oldSession must not be null");
        Objects.requireNonNull(mediaRoute2Info, "route must not be null");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                requestCreateSessionWithRouter2Locked(i, j, iMediaRouter2, routingSessionInfo, mediaRoute2Info, bundle);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void selectRouteWithRouter2(IMediaRouter2 iMediaRouter2, String str, MediaRoute2Info mediaRoute2Info) {
        Objects.requireNonNull(iMediaRouter2, "router must not be null");
        Objects.requireNonNull(mediaRoute2Info, "route must not be null");
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("uniqueSessionId must not be empty");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                selectRouteWithRouter2Locked(iMediaRouter2, str, mediaRoute2Info);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void deselectRouteWithRouter2(IMediaRouter2 iMediaRouter2, String str, MediaRoute2Info mediaRoute2Info) {
        Objects.requireNonNull(iMediaRouter2, "router must not be null");
        Objects.requireNonNull(mediaRoute2Info, "route must not be null");
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("uniqueSessionId must not be empty");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                deselectRouteWithRouter2Locked(iMediaRouter2, str, mediaRoute2Info);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void transferToRouteWithRouter2(IMediaRouter2 iMediaRouter2, String str, MediaRoute2Info mediaRoute2Info) {
        Objects.requireNonNull(iMediaRouter2, "router must not be null");
        Objects.requireNonNull(mediaRoute2Info, "route must not be null");
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("uniqueSessionId must not be empty");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                transferToRouteWithRouter2Locked(iMediaRouter2, str, mediaRoute2Info);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void setSessionVolumeWithRouter2(IMediaRouter2 iMediaRouter2, String str, int i) {
        Objects.requireNonNull(iMediaRouter2, "router must not be null");
        Objects.requireNonNull(str, "uniqueSessionId must not be null");
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("uniqueSessionId must not be empty");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                setSessionVolumeWithRouter2Locked(iMediaRouter2, str, i);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void releaseSessionWithRouter2(IMediaRouter2 iMediaRouter2, String str) {
        Objects.requireNonNull(iMediaRouter2, "router must not be null");
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("uniqueSessionId must not be empty");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                releaseSessionWithRouter2Locked(iMediaRouter2, str);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public List<RoutingSessionInfo> getRemoteSessions(IMediaRouter2Manager iMediaRouter2Manager) {
        List<RoutingSessionInfo> remoteSessionsLocked;
        Objects.requireNonNull(iMediaRouter2Manager, "manager must not be null");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                remoteSessionsLocked = getRemoteSessionsLocked(iMediaRouter2Manager);
            }
            return remoteSessionsLocked;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void registerManager(IMediaRouter2Manager iMediaRouter2Manager, String str) {
        Objects.requireNonNull(iMediaRouter2Manager, "manager must not be null");
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("packageName must not be empty");
        }
        int callingUid = Binder.getCallingUid();
        int callingPid = Binder.getCallingPid();
        int identifier = UserHandle.getUserHandleForUid(callingUid).getIdentifier();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                registerManagerLocked(iMediaRouter2Manager, callingUid, callingPid, str, identifier);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void unregisterManager(IMediaRouter2Manager iMediaRouter2Manager) {
        Objects.requireNonNull(iMediaRouter2Manager, "manager must not be null");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                unregisterManagerLocked(iMediaRouter2Manager, false);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void startScan(IMediaRouter2Manager iMediaRouter2Manager) {
        Objects.requireNonNull(iMediaRouter2Manager, "manager must not be null");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                startScanLocked(iMediaRouter2Manager);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void stopScan(IMediaRouter2Manager iMediaRouter2Manager) {
        Objects.requireNonNull(iMediaRouter2Manager, "manager must not be null");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                stopScanLocked(iMediaRouter2Manager);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void setRouteVolumeWithManager(IMediaRouter2Manager iMediaRouter2Manager, int i, MediaRoute2Info mediaRoute2Info, int i2) {
        Objects.requireNonNull(iMediaRouter2Manager, "manager must not be null");
        Objects.requireNonNull(mediaRoute2Info, "route must not be null");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                setRouteVolumeWithManagerLocked(i, iMediaRouter2Manager, mediaRoute2Info, i2);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void requestCreateSessionWithManager(IMediaRouter2Manager iMediaRouter2Manager, int i, RoutingSessionInfo routingSessionInfo, MediaRoute2Info mediaRoute2Info) {
        Objects.requireNonNull(iMediaRouter2Manager, "manager must not be null");
        Objects.requireNonNull(routingSessionInfo, "oldSession must not be null");
        Objects.requireNonNull(mediaRoute2Info, "route must not be null");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                requestCreateSessionWithManagerLocked(i, iMediaRouter2Manager, routingSessionInfo, mediaRoute2Info);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void selectRouteWithManager(IMediaRouter2Manager iMediaRouter2Manager, int i, String str, MediaRoute2Info mediaRoute2Info) {
        Objects.requireNonNull(iMediaRouter2Manager, "manager must not be null");
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("uniqueSessionId must not be empty");
        }
        Objects.requireNonNull(mediaRoute2Info, "route must not be null");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                selectRouteWithManagerLocked(i, iMediaRouter2Manager, str, mediaRoute2Info);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void deselectRouteWithManager(IMediaRouter2Manager iMediaRouter2Manager, int i, String str, MediaRoute2Info mediaRoute2Info) {
        Objects.requireNonNull(iMediaRouter2Manager, "manager must not be null");
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("uniqueSessionId must not be empty");
        }
        Objects.requireNonNull(mediaRoute2Info, "route must not be null");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                deselectRouteWithManagerLocked(i, iMediaRouter2Manager, str, mediaRoute2Info);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void transferToRouteWithManager(IMediaRouter2Manager iMediaRouter2Manager, int i, String str, MediaRoute2Info mediaRoute2Info) {
        Objects.requireNonNull(iMediaRouter2Manager, "manager must not be null");
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("uniqueSessionId must not be empty");
        }
        Objects.requireNonNull(mediaRoute2Info, "route must not be null");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                transferToRouteWithManagerLocked(i, iMediaRouter2Manager, str, mediaRoute2Info);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void setSessionVolumeWithManager(IMediaRouter2Manager iMediaRouter2Manager, int i, String str, int i2) {
        Objects.requireNonNull(iMediaRouter2Manager, "manager must not be null");
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("uniqueSessionId must not be empty");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                setSessionVolumeWithManagerLocked(i, iMediaRouter2Manager, str, i2);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void releaseSessionWithManager(IMediaRouter2Manager iMediaRouter2Manager, int i, String str) {
        Objects.requireNonNull(iMediaRouter2Manager, "manager must not be null");
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("uniqueSessionId must not be empty");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                releaseSessionWithManagerLocked(i, iMediaRouter2Manager, str);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public RoutingSessionInfo getSystemSessionInfo(String str, boolean z) {
        RoutingSessionInfo build;
        int identifier = UserHandle.getUserHandleForUid(Binder.getCallingUid()).getIdentifier();
        boolean z2 = this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_AUDIO_ROUTING") == 0;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                UserRecord orCreateUserRecordLocked = getOrCreateUserRecordLocked(identifier);
                if (!z2) {
                    build = new RoutingSessionInfo.Builder(orCreateUserRecordLocked.mHandler.mSystemProvider.getDefaultSessionInfo()).setClientPackageName(str).build();
                } else if (z) {
                    build = orCreateUserRecordLocked.mHandler.mSystemProvider.generateDeviceRouteSelectedSessionInfo(str);
                } else {
                    List<RoutingSessionInfo> sessionInfos = orCreateUserRecordLocked.mHandler.mSystemProvider.getSessionInfos();
                    if (sessionInfos != null && !sessionInfos.isEmpty()) {
                        build = new RoutingSessionInfo.Builder(sessionInfos.get(0)).setClientPackageName(str).build();
                    } else {
                        Slog.w(TAG, "System provider does not have any session info.");
                        build = null;
                    }
                }
            }
            return build;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void dump(PrintWriter printWriter, String str) {
        printWriter.println(str + "MediaRouter2ServiceImpl");
        String str2 = str + "  ";
        synchronized (this.mLock) {
            printWriter.println(str2 + "mNextRouterOrManagerId=" + this.mNextRouterOrManagerId.get());
            printWriter.println(str2 + "mCurrentActiveUserId=" + this.mCurrentActiveUserId);
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append("UserRecords:");
            printWriter.println(sb.toString());
            if (this.mUserRecords.size() > 0) {
                for (int i = 0; i < this.mUserRecords.size(); i++) {
                    this.mUserRecords.valueAt(i).dump(printWriter, str2 + "  ");
                }
            } else {
                printWriter.println(str2 + "  <no user records>");
            }
        }
    }

    public void updateRunningUserAndProfiles(int i) {
        synchronized (this.mLock) {
            if (this.mCurrentActiveUserId != i) {
                Slog.i(TAG, TextUtils.formatSimple("switchUser | user: %d", new Object[]{Integer.valueOf(i)}));
                this.mCurrentActiveUserId = i;
                SparseArray<UserRecord> clone = this.mUserRecords.clone();
                for (int i2 = 0; i2 < clone.size(); i2++) {
                    int keyAt = clone.keyAt(i2);
                    UserRecord valueAt = clone.valueAt(i2);
                    if (isUserActiveLocked(keyAt)) {
                        valueAt.mHandler.sendMessage(PooledLambda.obtainMessage(new MediaRouter2ServiceImpl$$ExternalSyntheticLambda1(), valueAt.mHandler));
                    } else {
                        valueAt.mHandler.sendMessage(PooledLambda.obtainMessage(new MediaRouter2ServiceImpl$$ExternalSyntheticLambda10(), valueAt.mHandler));
                        disposeUserIfNeededLocked(valueAt);
                    }
                }
            }
        }
    }

    void routerDied(RouterRecord routerRecord) {
        synchronized (this.mLock) {
            unregisterRouter2Locked(routerRecord.mRouter, true);
        }
    }

    void managerDied(ManagerRecord managerRecord) {
        synchronized (this.mLock) {
            unregisterManagerLocked(managerRecord.mManager, true);
        }
    }

    @GuardedBy({"mLock"})
    private boolean isUserActiveLocked(int i) {
        return this.mUserManagerInternal.getProfileParentId(i) == this.mCurrentActiveUserId;
    }

    @GuardedBy({"mLock"})
    private void registerRouter2Locked(IMediaRouter2 iMediaRouter2, int i, int i2, String str, int i3, boolean z, boolean z2) {
        IBinder asBinder = iMediaRouter2.asBinder();
        if (this.mAllRouterRecords.get(asBinder) != null) {
            Slog.w(TAG, "registerRouter2Locked: Same router already exists. packageName=" + str);
            return;
        }
        UserRecord orCreateUserRecordLocked = getOrCreateUserRecordLocked(i3);
        RouterRecord routerRecord = new RouterRecord(orCreateUserRecordLocked, iMediaRouter2, i, i2, str, z, z2);
        try {
            asBinder.linkToDeath(routerRecord, 0);
            orCreateUserRecordLocked.mRouterRecords.add(routerRecord);
            this.mAllRouterRecords.put(asBinder, routerRecord);
            orCreateUserRecordLocked.mHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda7
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    ((MediaRouter2ServiceImpl.UserHandler) obj).notifyRouterRegistered((MediaRouter2ServiceImpl.RouterRecord) obj2);
                }
            }, orCreateUserRecordLocked.mHandler, routerRecord));
            Slog.i(TAG, TextUtils.formatSimple("registerRouter2 | package: %s, uid: %d, pid: %d, router id: %d", new Object[]{str, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(routerRecord.mRouterId)}));
        } catch (RemoteException e) {
            throw new RuntimeException("MediaRouter2 died prematurely.", e);
        }
    }

    @GuardedBy({"mLock"})
    private void unregisterRouter2Locked(IMediaRouter2 iMediaRouter2, boolean z) {
        RouterRecord remove = this.mAllRouterRecords.remove(iMediaRouter2.asBinder());
        if (remove == null) {
            Slog.w(TAG, "Ignoring unregistering unknown router2");
            return;
        }
        Slog.i(TAG, TextUtils.formatSimple("unregisterRouter2 | package: %s, router id: %d", new Object[]{remove.mPackageName, Integer.valueOf(remove.mRouterId)}));
        UserRecord userRecord = remove.mUserRecord;
        userRecord.mRouterRecords.remove(remove);
        remove.mUserRecord.mHandler.sendMessage(PooledLambda.obtainMessage(new MediaRouter2ServiceImpl$$ExternalSyntheticLambda16(), remove.mUserRecord.mHandler, remove.mPackageName, (Object) null));
        remove.mUserRecord.mHandler.sendMessage(PooledLambda.obtainMessage(new MediaRouter2ServiceImpl$$ExternalSyntheticLambda6(), remove.mUserRecord.mHandler, remove.mPackageName, (Object) null));
        userRecord.mHandler.sendMessage(PooledLambda.obtainMessage(new MediaRouter2ServiceImpl$$ExternalSyntheticLambda17(), userRecord.mHandler));
        remove.dispose();
        disposeUserIfNeededLocked(userRecord);
    }

    private void setDiscoveryRequestWithRouter2Locked(RouterRecord routerRecord, RouteDiscoveryPreference routeDiscoveryPreference) {
        if (routerRecord.mDiscoveryPreference.equals(routeDiscoveryPreference)) {
            return;
        }
        Slog.i(TAG, TextUtils.formatSimple("setDiscoveryRequestWithRouter2 | router: %s(id: %d), discovery request: %s", new Object[]{routerRecord.mPackageName, Integer.valueOf(routerRecord.mRouterId), routeDiscoveryPreference.toString()}));
        routerRecord.mDiscoveryPreference = routeDiscoveryPreference;
        routerRecord.mUserRecord.mHandler.sendMessage(PooledLambda.obtainMessage(new MediaRouter2ServiceImpl$$ExternalSyntheticLambda16(), routerRecord.mUserRecord.mHandler, routerRecord.mPackageName, routerRecord.mDiscoveryPreference));
        routerRecord.mUserRecord.mHandler.sendMessage(PooledLambda.obtainMessage(new MediaRouter2ServiceImpl$$ExternalSyntheticLambda17(), routerRecord.mUserRecord.mHandler));
    }

    @GuardedBy({"mLock"})
    private void setRouteListingPreferenceLocked(RouterRecord routerRecord, RouteListingPreference routeListingPreference) {
        routerRecord.mRouteListingPreference = routeListingPreference;
        Slog.i(TAG, TextUtils.formatSimple("setRouteListingPreference | router: %s(id: %d), route listing preference: [%s]", new Object[]{routerRecord.mPackageName, Integer.valueOf(routerRecord.mRouterId), routeListingPreference != null ? (String) routeListingPreference.getItems().stream().map(new Function() { // from class: com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda5
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((RouteListingPreference.Item) obj).getRouteId();
            }
        }).collect(Collectors.joining(",")) : null}));
        routerRecord.mUserRecord.mHandler.sendMessage(PooledLambda.obtainMessage(new MediaRouter2ServiceImpl$$ExternalSyntheticLambda6(), routerRecord.mUserRecord.mHandler, routerRecord.mPackageName, routeListingPreference));
    }

    private void setRouteVolumeWithRouter2Locked(IMediaRouter2 iMediaRouter2, MediaRoute2Info mediaRoute2Info, int i) {
        RouterRecord routerRecord = this.mAllRouterRecords.get(iMediaRouter2.asBinder());
        if (routerRecord != null) {
            Slog.i(TAG, TextUtils.formatSimple("setRouteVolumeWithRouter2 | router: %s(id: %d), volume: %d", new Object[]{routerRecord.mPackageName, Integer.valueOf(routerRecord.mRouterId), Integer.valueOf(i)}));
            routerRecord.mUserRecord.mHandler.sendMessage(PooledLambda.obtainMessage(new MediaRouter2ServiceImpl$$ExternalSyntheticLambda2(), routerRecord.mUserRecord.mHandler, -1L, mediaRoute2Info, Integer.valueOf(i)));
        }
    }

    private void requestCreateSessionWithRouter2Locked(int i, long j, IMediaRouter2 iMediaRouter2, RoutingSessionInfo routingSessionInfo, MediaRoute2Info mediaRoute2Info, Bundle bundle) {
        SessionCreationRequest sessionCreationRequest;
        RouterRecord routerRecord = this.mAllRouterRecords.get(iMediaRouter2.asBinder());
        if (routerRecord == null) {
            return;
        }
        if (j != 0) {
            ManagerRecord findManagerWithId = routerRecord.mUserRecord.mHandler.findManagerWithId(toRequesterId(j));
            if (findManagerWithId == null || (sessionCreationRequest = findManagerWithId.mLastSessionCreationRequest) == null) {
                Slog.w(TAG, "requestCreateSessionWithRouter2Locked: Ignoring unknown request.");
                routerRecord.mUserRecord.mHandler.notifySessionCreationFailedToRouter(routerRecord, i);
                return;
            }
            if (!TextUtils.equals(sessionCreationRequest.mOldSession.getId(), routingSessionInfo.getId())) {
                Slog.w(TAG, "requestCreateSessionWithRouter2Locked: Ignoring unmatched routing session.");
                routerRecord.mUserRecord.mHandler.notifySessionCreationFailedToRouter(routerRecord, i);
                return;
            }
            if (!TextUtils.equals(findManagerWithId.mLastSessionCreationRequest.mRoute.getId(), mediaRoute2Info.getId())) {
                if (!routerRecord.hasSystemRoutingPermission() && findManagerWithId.mLastSessionCreationRequest.mRoute.isSystemRoute() && mediaRoute2Info.isSystemRoute()) {
                    mediaRoute2Info = findManagerWithId.mLastSessionCreationRequest.mRoute;
                } else {
                    Slog.w(TAG, "requestCreateSessionWithRouter2Locked: Ignoring unmatched route.");
                    routerRecord.mUserRecord.mHandler.notifySessionCreationFailedToRouter(routerRecord, i);
                    return;
                }
            }
            findManagerWithId.mLastSessionCreationRequest = null;
        } else if (mediaRoute2Info.isSystemRoute() && !routerRecord.hasSystemRoutingPermission() && !TextUtils.equals(mediaRoute2Info.getId(), routerRecord.mUserRecord.mHandler.mSystemProvider.getDefaultRoute().getId())) {
            Slog.w(TAG, "MODIFY_AUDIO_ROUTING permission is required to transfer to" + mediaRoute2Info);
            routerRecord.mUserRecord.mHandler.notifySessionCreationFailedToRouter(routerRecord, i);
            return;
        }
        routerRecord.mUserRecord.mHandler.sendMessage(PooledLambda.obtainMessage(new HeptConsumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda20
            public final void accept(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7) {
                ((MediaRouter2ServiceImpl.UserHandler) obj).requestCreateSessionWithRouter2OnHandler(((Long) obj2).longValue(), ((Long) obj3).longValue(), (MediaRouter2ServiceImpl.RouterRecord) obj4, (RoutingSessionInfo) obj5, (MediaRoute2Info) obj6, (Bundle) obj7);
            }
        }, routerRecord.mUserRecord.mHandler, Long.valueOf(toUniqueRequestId(routerRecord.mRouterId, i)), Long.valueOf(j), routerRecord, routingSessionInfo, mediaRoute2Info, bundle));
    }

    private void selectRouteWithRouter2Locked(IMediaRouter2 iMediaRouter2, String str, MediaRoute2Info mediaRoute2Info) {
        RouterRecord routerRecord = this.mAllRouterRecords.get(iMediaRouter2.asBinder());
        if (routerRecord == null) {
            return;
        }
        Slog.i(TAG, TextUtils.formatSimple("selectRouteWithRouter2 | router: %s(id: %d), route: %s", new Object[]{routerRecord.mPackageName, Integer.valueOf(routerRecord.mRouterId), mediaRoute2Info.getId()}));
        routerRecord.mUserRecord.mHandler.sendMessage(PooledLambda.obtainMessage(new MediaRouter2ServiceImpl$$ExternalSyntheticLambda9(), routerRecord.mUserRecord.mHandler, -1L, routerRecord, str, mediaRoute2Info));
    }

    private void deselectRouteWithRouter2Locked(IMediaRouter2 iMediaRouter2, String str, MediaRoute2Info mediaRoute2Info) {
        RouterRecord routerRecord = this.mAllRouterRecords.get(iMediaRouter2.asBinder());
        if (routerRecord == null) {
            return;
        }
        Slog.i(TAG, TextUtils.formatSimple("deselectRouteWithRouter2 | router: %s(id: %d), route: %s", new Object[]{routerRecord.mPackageName, Integer.valueOf(routerRecord.mRouterId), mediaRoute2Info.getId()}));
        routerRecord.mUserRecord.mHandler.sendMessage(PooledLambda.obtainMessage(new MediaRouter2ServiceImpl$$ExternalSyntheticLambda8(), routerRecord.mUserRecord.mHandler, -1L, routerRecord, str, mediaRoute2Info));
    }

    private void transferToRouteWithRouter2Locked(IMediaRouter2 iMediaRouter2, String str, MediaRoute2Info mediaRoute2Info) {
        RouterRecord routerRecord = this.mAllRouterRecords.get(iMediaRouter2.asBinder());
        if (routerRecord == null) {
            return;
        }
        Slog.i(TAG, TextUtils.formatSimple("transferToRouteWithRouter2 | router: %s(id: %d), route: %s", new Object[]{routerRecord.mPackageName, Integer.valueOf(routerRecord.mRouterId), mediaRoute2Info.getId()}));
        String id = routerRecord.mUserRecord.mHandler.mSystemProvider.getDefaultRoute().getId();
        if (mediaRoute2Info.isSystemRoute() && !routerRecord.hasSystemRoutingPermission() && !TextUtils.equals(mediaRoute2Info.getId(), id)) {
            routerRecord.mUserRecord.mHandler.sendMessage(PooledLambda.obtainMessage(new TriConsumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda18
                public final void accept(Object obj, Object obj2, Object obj3) {
                    ((MediaRouter2ServiceImpl.UserHandler) obj).notifySessionCreationFailedToRouter((MediaRouter2ServiceImpl.RouterRecord) obj2, ((Integer) obj3).intValue());
                }
            }, routerRecord.mUserRecord.mHandler, routerRecord, Integer.valueOf(toOriginalRequestId(-1L))));
        } else {
            routerRecord.mUserRecord.mHandler.sendMessage(PooledLambda.obtainMessage(new MediaRouter2ServiceImpl$$ExternalSyntheticLambda19(), routerRecord.mUserRecord.mHandler, -1L, routerRecord, str, mediaRoute2Info));
        }
    }

    private void setSessionVolumeWithRouter2Locked(IMediaRouter2 iMediaRouter2, String str, int i) {
        RouterRecord routerRecord = this.mAllRouterRecords.get(iMediaRouter2.asBinder());
        if (routerRecord == null) {
            return;
        }
        Slog.i(TAG, TextUtils.formatSimple("setSessionVolumeWithRouter2 | router: %s(id: %d), session: %s, volume: %d", new Object[]{routerRecord.mPackageName, Integer.valueOf(routerRecord.mRouterId), str, Integer.valueOf(i)}));
        routerRecord.mUserRecord.mHandler.sendMessage(PooledLambda.obtainMessage(new MediaRouter2ServiceImpl$$ExternalSyntheticLambda4(), routerRecord.mUserRecord.mHandler, -1L, str, Integer.valueOf(i)));
    }

    private void releaseSessionWithRouter2Locked(IMediaRouter2 iMediaRouter2, String str) {
        RouterRecord routerRecord = this.mAllRouterRecords.get(iMediaRouter2.asBinder());
        if (routerRecord == null) {
            return;
        }
        Slog.i(TAG, TextUtils.formatSimple("releaseSessionWithRouter2 | router: %s(id: %d), session: %s", new Object[]{routerRecord.mPackageName, Integer.valueOf(routerRecord.mRouterId), str}));
        routerRecord.mUserRecord.mHandler.sendMessage(PooledLambda.obtainMessage(new MediaRouter2ServiceImpl$$ExternalSyntheticLambda3(), routerRecord.mUserRecord.mHandler, -1L, routerRecord, str));
    }

    private List<RoutingSessionInfo> getRemoteSessionsLocked(IMediaRouter2Manager iMediaRouter2Manager) {
        ManagerRecord managerRecord = this.mAllManagerRecords.get(iMediaRouter2Manager.asBinder());
        if (managerRecord == null) {
            Slog.w(TAG, "getRemoteSessionLocked: Ignoring unknown manager");
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        Iterator it = managerRecord.mUserRecord.mHandler.mRouteProviders.iterator();
        while (it.hasNext()) {
            MediaRoute2Provider mediaRoute2Provider = (MediaRoute2Provider) it.next();
            if (!mediaRoute2Provider.mIsSystemRouteProvider) {
                arrayList.addAll(mediaRoute2Provider.getSessionInfos());
            }
        }
        return arrayList;
    }

    @GuardedBy({"mLock"})
    private void registerManagerLocked(IMediaRouter2Manager iMediaRouter2Manager, int i, int i2, String str, int i3) {
        IBinder asBinder = iMediaRouter2Manager.asBinder();
        if (this.mAllManagerRecords.get(asBinder) != null) {
            Slog.w(TAG, "registerManagerLocked: Same manager already exists. packageName=" + str);
            return;
        }
        Slog.i(TAG, TextUtils.formatSimple("registerManager | uid: %d, pid: %d, package: %s, user: %d", new Object[]{Integer.valueOf(i), Integer.valueOf(i2), str, Integer.valueOf(i3)}));
        this.mContext.enforcePermission("android.permission.MEDIA_CONTENT_CONTROL", i2, i, "Must hold MEDIA_CONTENT_CONTROL permission.");
        UserRecord orCreateUserRecordLocked = getOrCreateUserRecordLocked(i3);
        ManagerRecord managerRecord = new ManagerRecord(orCreateUserRecordLocked, iMediaRouter2Manager, i, i2, str);
        try {
            asBinder.linkToDeath(managerRecord, 0);
            orCreateUserRecordLocked.mManagerRecords.add(managerRecord);
            this.mAllManagerRecords.put(asBinder, managerRecord);
            Iterator<RouterRecord> it = orCreateUserRecordLocked.mRouterRecords.iterator();
            while (it.hasNext()) {
                RouterRecord next = it.next();
                orCreateUserRecordLocked.mHandler.sendMessage(PooledLambda.obtainMessage(new MediaRouter2ServiceImpl$$ExternalSyntheticLambda6(), next.mUserRecord.mHandler, next.mPackageName, next.mRouteListingPreference));
                next.mUserRecord.mHandler.sendMessage(PooledLambda.obtainMessage(new TriConsumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda14
                    public final void accept(Object obj, Object obj2, Object obj3) {
                        ((MediaRouter2ServiceImpl.UserHandler) obj).notifyDiscoveryPreferenceChangedToManager((MediaRouter2ServiceImpl.RouterRecord) obj2, (IMediaRouter2Manager) obj3);
                    }
                }, next.mUserRecord.mHandler, next, iMediaRouter2Manager));
            }
            orCreateUserRecordLocked.mHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda15
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    ((MediaRouter2ServiceImpl.UserHandler) obj).notifyInitialRoutesToManager((IMediaRouter2Manager) obj2);
                }
            }, orCreateUserRecordLocked.mHandler, iMediaRouter2Manager));
        } catch (RemoteException e) {
            throw new RuntimeException("Media router manager died prematurely.", e);
        }
    }

    private void unregisterManagerLocked(IMediaRouter2Manager iMediaRouter2Manager, boolean z) {
        ManagerRecord remove = this.mAllManagerRecords.remove(iMediaRouter2Manager.asBinder());
        if (remove == null) {
            return;
        }
        UserRecord userRecord = remove.mUserRecord;
        Slog.i(TAG, TextUtils.formatSimple("unregisterManager | package: %s, user: %d, manager: %d", new Object[]{remove.mPackageName, Integer.valueOf(userRecord.mUserId), Integer.valueOf(remove.mManagerId)}));
        userRecord.mManagerRecords.remove(remove);
        remove.dispose();
        disposeUserIfNeededLocked(userRecord);
    }

    private void startScanLocked(IMediaRouter2Manager iMediaRouter2Manager) {
        ManagerRecord managerRecord = this.mAllManagerRecords.get(iMediaRouter2Manager.asBinder());
        if (managerRecord == null) {
            return;
        }
        Slog.i(TAG, TextUtils.formatSimple("startScan | manager: %d", new Object[]{Integer.valueOf(managerRecord.mManagerId)}));
        managerRecord.startScan();
    }

    private void stopScanLocked(IMediaRouter2Manager iMediaRouter2Manager) {
        ManagerRecord managerRecord = this.mAllManagerRecords.get(iMediaRouter2Manager.asBinder());
        if (managerRecord == null) {
            return;
        }
        Slog.i(TAG, TextUtils.formatSimple("stopScan | manager: %d", new Object[]{Integer.valueOf(managerRecord.mManagerId)}));
        managerRecord.stopScan();
    }

    private void setRouteVolumeWithManagerLocked(int i, IMediaRouter2Manager iMediaRouter2Manager, MediaRoute2Info mediaRoute2Info, int i2) {
        ManagerRecord managerRecord = this.mAllManagerRecords.get(iMediaRouter2Manager.asBinder());
        if (managerRecord == null) {
            return;
        }
        Slog.i(TAG, TextUtils.formatSimple("setRouteVolumeWithManager | manager: %d, route: %s, volume: %d", new Object[]{Integer.valueOf(managerRecord.mManagerId), mediaRoute2Info.getId(), Integer.valueOf(i2)}));
        managerRecord.mUserRecord.mHandler.sendMessage(PooledLambda.obtainMessage(new MediaRouter2ServiceImpl$$ExternalSyntheticLambda2(), managerRecord.mUserRecord.mHandler, Long.valueOf(toUniqueRequestId(managerRecord.mManagerId, i)), mediaRoute2Info, Integer.valueOf(i2)));
    }

    private void requestCreateSessionWithManagerLocked(int i, IMediaRouter2Manager iMediaRouter2Manager, RoutingSessionInfo routingSessionInfo, MediaRoute2Info mediaRoute2Info) {
        ManagerRecord managerRecord = this.mAllManagerRecords.get(iMediaRouter2Manager.asBinder());
        if (managerRecord == null) {
            return;
        }
        Slog.i(TAG, TextUtils.formatSimple("requestCreateSessionWithManager | manager: %d, route: %s", new Object[]{Integer.valueOf(managerRecord.mManagerId), mediaRoute2Info.getId()}));
        RouterRecord findRouterRecordLocked = managerRecord.mUserRecord.findRouterRecordLocked(routingSessionInfo.getClientPackageName());
        if (findRouterRecordLocked == null) {
            Slog.w(TAG, "requestCreateSessionWithManagerLocked: Ignoring session creation for unknown router.");
            try {
                managerRecord.mManager.notifyRequestFailed(i, 0);
                return;
            } catch (RemoteException unused) {
                Slog.w(TAG, "requestCreateSessionWithManagerLocked: Failed to notify failure. Manager probably died.");
                return;
            }
        }
        long uniqueRequestId = toUniqueRequestId(managerRecord.mManagerId, i);
        SessionCreationRequest sessionCreationRequest = managerRecord.mLastSessionCreationRequest;
        if (sessionCreationRequest != null) {
            managerRecord.mUserRecord.mHandler.notifyRequestFailedToManager(managerRecord.mManager, toOriginalRequestId(sessionCreationRequest.mManagerRequestId), 0);
            managerRecord.mLastSessionCreationRequest = null;
        }
        managerRecord.mLastSessionCreationRequest = new SessionCreationRequest(findRouterRecordLocked, 0L, uniqueRequestId, routingSessionInfo, mediaRoute2Info);
        findRouterRecordLocked.mUserRecord.mHandler.sendMessage(PooledLambda.obtainMessage(new HexConsumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$$ExternalSyntheticLambda21
            public final void accept(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
                ((MediaRouter2ServiceImpl.UserHandler) obj).requestRouterCreateSessionOnHandler(((Long) obj2).longValue(), (MediaRouter2ServiceImpl.RouterRecord) obj3, (MediaRouter2ServiceImpl.ManagerRecord) obj4, (RoutingSessionInfo) obj5, (MediaRoute2Info) obj6);
            }
        }, findRouterRecordLocked.mUserRecord.mHandler, Long.valueOf(uniqueRequestId), findRouterRecordLocked, managerRecord, routingSessionInfo, mediaRoute2Info));
    }

    private void selectRouteWithManagerLocked(int i, IMediaRouter2Manager iMediaRouter2Manager, String str, MediaRoute2Info mediaRoute2Info) {
        ManagerRecord managerRecord = this.mAllManagerRecords.get(iMediaRouter2Manager.asBinder());
        if (managerRecord == null) {
            return;
        }
        Slog.i(TAG, TextUtils.formatSimple("selectRouteWithManager | manager: %d, session: %s, route: %s", new Object[]{Integer.valueOf(managerRecord.mManagerId), str, mediaRoute2Info.getId()}));
        managerRecord.mUserRecord.mHandler.sendMessage(PooledLambda.obtainMessage(new MediaRouter2ServiceImpl$$ExternalSyntheticLambda9(), managerRecord.mUserRecord.mHandler, Long.valueOf(toUniqueRequestId(managerRecord.mManagerId, i)), managerRecord.mUserRecord.mHandler.findRouterWithSessionLocked(str), str, mediaRoute2Info));
    }

    private void deselectRouteWithManagerLocked(int i, IMediaRouter2Manager iMediaRouter2Manager, String str, MediaRoute2Info mediaRoute2Info) {
        ManagerRecord managerRecord = this.mAllManagerRecords.get(iMediaRouter2Manager.asBinder());
        if (managerRecord == null) {
            return;
        }
        Slog.i(TAG, TextUtils.formatSimple("deselectRouteWithManager | manager: %d, session: %s, route: %s", new Object[]{Integer.valueOf(managerRecord.mManagerId), str, mediaRoute2Info.getId()}));
        managerRecord.mUserRecord.mHandler.sendMessage(PooledLambda.obtainMessage(new MediaRouter2ServiceImpl$$ExternalSyntheticLambda8(), managerRecord.mUserRecord.mHandler, Long.valueOf(toUniqueRequestId(managerRecord.mManagerId, i)), managerRecord.mUserRecord.mHandler.findRouterWithSessionLocked(str), str, mediaRoute2Info));
    }

    private void transferToRouteWithManagerLocked(int i, IMediaRouter2Manager iMediaRouter2Manager, String str, MediaRoute2Info mediaRoute2Info) {
        ManagerRecord managerRecord = this.mAllManagerRecords.get(iMediaRouter2Manager.asBinder());
        if (managerRecord == null) {
            return;
        }
        Slog.i(TAG, TextUtils.formatSimple("transferToRouteWithManager | manager: %d, session: %s, route: %s", new Object[]{Integer.valueOf(managerRecord.mManagerId), str, mediaRoute2Info.getId()}));
        managerRecord.mUserRecord.mHandler.sendMessage(PooledLambda.obtainMessage(new MediaRouter2ServiceImpl$$ExternalSyntheticLambda19(), managerRecord.mUserRecord.mHandler, Long.valueOf(toUniqueRequestId(managerRecord.mManagerId, i)), managerRecord.mUserRecord.mHandler.findRouterWithSessionLocked(str), str, mediaRoute2Info));
    }

    private void setSessionVolumeWithManagerLocked(int i, IMediaRouter2Manager iMediaRouter2Manager, String str, int i2) {
        ManagerRecord managerRecord = this.mAllManagerRecords.get(iMediaRouter2Manager.asBinder());
        if (managerRecord == null) {
            return;
        }
        Slog.i(TAG, TextUtils.formatSimple("setSessionVolumeWithManager | manager: %d, session: %s, volume: %d", new Object[]{Integer.valueOf(managerRecord.mManagerId), str, Integer.valueOf(i2)}));
        managerRecord.mUserRecord.mHandler.sendMessage(PooledLambda.obtainMessage(new MediaRouter2ServiceImpl$$ExternalSyntheticLambda4(), managerRecord.mUserRecord.mHandler, Long.valueOf(toUniqueRequestId(managerRecord.mManagerId, i)), str, Integer.valueOf(i2)));
    }

    private void releaseSessionWithManagerLocked(int i, IMediaRouter2Manager iMediaRouter2Manager, String str) {
        ManagerRecord managerRecord = this.mAllManagerRecords.get(iMediaRouter2Manager.asBinder());
        if (managerRecord == null) {
            return;
        }
        Slog.i(TAG, TextUtils.formatSimple("releaseSessionWithManager | manager: %d, session: %s", new Object[]{Integer.valueOf(managerRecord.mManagerId), str}));
        managerRecord.mUserRecord.mHandler.sendMessage(PooledLambda.obtainMessage(new MediaRouter2ServiceImpl$$ExternalSyntheticLambda3(), managerRecord.mUserRecord.mHandler, Long.valueOf(toUniqueRequestId(managerRecord.mManagerId, i)), managerRecord.mUserRecord.mHandler.findRouterWithSessionLocked(str), str));
    }

    @GuardedBy({"mLock"})
    private UserRecord getOrCreateUserRecordLocked(int i) {
        UserRecord userRecord = this.mUserRecords.get(i);
        if (userRecord == null) {
            userRecord = new UserRecord(i);
            this.mUserRecords.put(i, userRecord);
            userRecord.init();
            if (isUserActiveLocked(i)) {
                userRecord.mHandler.sendMessage(PooledLambda.obtainMessage(new MediaRouter2ServiceImpl$$ExternalSyntheticLambda1(), userRecord.mHandler));
            }
        }
        return userRecord;
    }

    @GuardedBy({"mLock"})
    private void disposeUserIfNeededLocked(UserRecord userRecord) {
        if (!isUserActiveLocked(userRecord.mUserId) && userRecord.mRouterRecords.isEmpty() && userRecord.mManagerRecords.isEmpty()) {
            if (DEBUG) {
                Slog.d(TAG, userRecord + ": Disposed");
            }
            userRecord.mHandler.sendMessage(PooledLambda.obtainMessage(new MediaRouter2ServiceImpl$$ExternalSyntheticLambda10(), userRecord.mHandler));
            this.mUserRecords.remove(userRecord.mUserId);
        }
    }

    public void onDeviceConfigChange(DeviceConfig.Properties properties) {
        sPackageImportanceForScanning = properties.getInt(KEY_SCANNING_PACKAGE_MINIMUM_IMPORTANCE, 125);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class UserRecord {
        final UserHandler mHandler;
        public final int mUserId;
        final ArrayList<RouterRecord> mRouterRecords = new ArrayList<>();
        final ArrayList<ManagerRecord> mManagerRecords = new ArrayList<>();
        RouteDiscoveryPreference mCompositeDiscoveryPreference = RouteDiscoveryPreference.EMPTY;
        Set<String> mActivelyScanningPackages = Set.of();

        UserRecord(int i) {
            this.mUserId = i;
            this.mHandler = new UserHandler(MediaRouter2ServiceImpl.this, this);
        }

        void init() {
            this.mHandler.init();
        }

        RouterRecord findRouterRecordLocked(String str) {
            Iterator<RouterRecord> it = this.mRouterRecords.iterator();
            while (it.hasNext()) {
                RouterRecord next = it.next();
                if (TextUtils.equals(next.mPackageName, str)) {
                    return next;
                }
            }
            return null;
        }

        public void dump(final PrintWriter printWriter, String str) {
            printWriter.println(str + "UserRecord");
            final String str2 = str + "  ";
            printWriter.println(str2 + "mUserId=" + this.mUserId);
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append("Router Records:");
            printWriter.println(sb.toString());
            if (!this.mRouterRecords.isEmpty()) {
                Iterator<RouterRecord> it = this.mRouterRecords.iterator();
                while (it.hasNext()) {
                    it.next().dump(printWriter, str2 + "  ");
                }
            } else {
                printWriter.println(str2 + "<no router records>");
            }
            printWriter.println(str2 + "Manager Records:");
            if (!this.mManagerRecords.isEmpty()) {
                Iterator<ManagerRecord> it2 = this.mManagerRecords.iterator();
                while (it2.hasNext()) {
                    it2.next().dump(printWriter, str2 + "  ");
                }
            } else {
                printWriter.println(str2 + "<no manager records>");
            }
            printWriter.println(str2 + "Composite discovery preference:");
            this.mCompositeDiscoveryPreference.dump(printWriter, str2 + "  ");
            printWriter.println(str2 + "Packages actively scanning: " + String.join(", ", this.mActivelyScanningPackages));
            if (this.mHandler.runWithScissors(new Runnable() { // from class: com.android.server.media.MediaRouter2ServiceImpl$UserRecord$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    MediaRouter2ServiceImpl.UserRecord.this.lambda$dump$0(printWriter, str2);
                }
            }, 1000L)) {
                return;
            }
            printWriter.println(str2 + "<could not dump handler state>");
        }

        public /* synthetic */ void lambda$dump$0(PrintWriter printWriter, String str) {
            this.mHandler.dump(printWriter, str);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class RouterRecord implements IBinder.DeathRecipient {
        public final boolean mHasConfigureWifiDisplayPermission;
        public final boolean mHasModifyAudioRoutingPermission;
        public final String mPackageName;
        public final int mPid;
        public RouteListingPreference mRouteListingPreference;
        public final IMediaRouter2 mRouter;
        public final int mRouterId;
        public final int mUid;
        public final UserRecord mUserRecord;
        public final List<Integer> mSelectRouteSequenceNumbers = new ArrayList();
        public RouteDiscoveryPreference mDiscoveryPreference = RouteDiscoveryPreference.EMPTY;
        public final AtomicBoolean mHasBluetoothRoutingPermission = new AtomicBoolean(fetchBluetoothPermission());

        RouterRecord(UserRecord userRecord, IMediaRouter2 iMediaRouter2, int i, int i2, String str, boolean z, boolean z2) {
            this.mUserRecord = userRecord;
            this.mPackageName = str;
            this.mRouter = iMediaRouter2;
            this.mUid = i;
            this.mPid = i2;
            this.mHasConfigureWifiDisplayPermission = z;
            this.mHasModifyAudioRoutingPermission = z2;
            this.mRouterId = MediaRouter2ServiceImpl.this.mNextRouterOrManagerId.getAndIncrement();
        }

        private boolean fetchBluetoothPermission() {
            boolean z = true;
            for (String str : MediaRouter2ServiceImpl.BLUETOOTH_PERMISSIONS_FOR_SYSTEM_ROUTING) {
                z &= MediaRouter2ServiceImpl.this.mContext.checkPermission(str, this.mPid, this.mUid) == 0;
            }
            return z;
        }

        public boolean hasSystemRoutingPermission() {
            return this.mHasModifyAudioRoutingPermission || this.mHasBluetoothRoutingPermission.get();
        }

        public void maybeUpdateSystemRoutingPermissionLocked() {
            Map map;
            RoutingSessionInfo defaultSessionInfo;
            boolean hasSystemRoutingPermission = hasSystemRoutingPermission();
            this.mHasBluetoothRoutingPermission.set(fetchBluetoothPermission());
            boolean hasSystemRoutingPermission2 = hasSystemRoutingPermission();
            if (hasSystemRoutingPermission != hasSystemRoutingPermission2) {
                if (hasSystemRoutingPermission2) {
                    map = this.mUserRecord.mHandler.mLastNotifiedRoutesToPrivilegedRouters;
                } else {
                    map = this.mUserRecord.mHandler.mLastNotifiedRoutesToNonPrivilegedRouters;
                }
                notifyRoutesUpdated(map.values().stream().toList());
                List<RoutingSessionInfo> sessionInfos = this.mUserRecord.mHandler.mSystemProvider.getSessionInfos();
                if (hasSystemRoutingPermission2 && !sessionInfos.isEmpty()) {
                    defaultSessionInfo = sessionInfos.get(0);
                } else {
                    defaultSessionInfo = this.mUserRecord.mHandler.mSystemProvider.getDefaultSessionInfo();
                }
                notifySessionInfoChanged(defaultSessionInfo);
            }
        }

        public void dispose() {
            this.mRouter.asBinder().unlinkToDeath(this, 0);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            MediaRouter2ServiceImpl.this.routerDied(this);
        }

        public void dump(PrintWriter printWriter, String str) {
            printWriter.println(str + "RouterRecord");
            String str2 = str + "  ";
            printWriter.println(str2 + "mPackageName=" + this.mPackageName);
            printWriter.println(str2 + "mSelectRouteSequenceNumbers=" + this.mSelectRouteSequenceNumbers);
            printWriter.println(str2 + "mUid=" + this.mUid);
            printWriter.println(str2 + "mPid=" + this.mPid);
            printWriter.println(str2 + "mHasConfigureWifiDisplayPermission=" + this.mHasConfigureWifiDisplayPermission);
            printWriter.println(str2 + "mHasModifyAudioRoutingPermission=" + this.mHasModifyAudioRoutingPermission);
            printWriter.println(str2 + "mHasBluetoothRoutingPermission=" + this.mHasBluetoothRoutingPermission.get());
            printWriter.println(str2 + "hasSystemRoutingPermission=" + hasSystemRoutingPermission());
            printWriter.println(str2 + "mRouterId=" + this.mRouterId);
            this.mDiscoveryPreference.dump(printWriter, str2);
        }

        public void notifyRegistered(List<MediaRoute2Info> list, RoutingSessionInfo routingSessionInfo) {
            try {
                this.mRouter.notifyRouterRegistered(getVisibleRoutes(list), routingSessionInfo);
            } catch (RemoteException e) {
                Slog.w(MediaRouter2ServiceImpl.TAG, "Failed to notify router registered. Router probably died.", e);
            }
        }

        public void notifyRoutesUpdated(List<MediaRoute2Info> list) {
            try {
                this.mRouter.notifyRoutesUpdated(getVisibleRoutes(list));
            } catch (RemoteException e) {
                Slog.w(MediaRouter2ServiceImpl.TAG, "Failed to notify routes updated. Router probably died.", e);
            }
        }

        public void notifySessionInfoChanged(RoutingSessionInfo routingSessionInfo) {
            try {
                this.mRouter.notifySessionInfoChanged(routingSessionInfo);
            } catch (RemoteException e) {
                Slog.w(MediaRouter2ServiceImpl.TAG, "Failed to notify session info changed. Router probably died.", e);
            }
        }

        private List<MediaRoute2Info> getVisibleRoutes(List<MediaRoute2Info> list) {
            ArrayList arrayList = new ArrayList();
            for (MediaRoute2Info mediaRoute2Info : list) {
                if (mediaRoute2Info.isVisibleTo(this.mPackageName)) {
                    arrayList.add(mediaRoute2Info);
                }
            }
            return arrayList;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class ManagerRecord implements IBinder.DeathRecipient {
        public boolean mIsScanning;
        public SessionCreationRequest mLastSessionCreationRequest;
        public final IMediaRouter2Manager mManager;
        public final int mManagerId;
        public final String mPackageName;
        public final int mPid;
        public final int mUid;
        public final UserRecord mUserRecord;

        ManagerRecord(UserRecord userRecord, IMediaRouter2Manager iMediaRouter2Manager, int i, int i2, String str) {
            this.mUserRecord = userRecord;
            this.mManager = iMediaRouter2Manager;
            this.mUid = i;
            this.mPid = i2;
            this.mPackageName = str;
            this.mManagerId = MediaRouter2ServiceImpl.this.mNextRouterOrManagerId.getAndIncrement();
        }

        public void dispose() {
            this.mManager.asBinder().unlinkToDeath(this, 0);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            MediaRouter2ServiceImpl.this.managerDied(this);
        }

        public void dump(PrintWriter printWriter, String str) {
            printWriter.println(str + "ManagerRecord");
            String str2 = str + "  ";
            printWriter.println(str2 + "mPackageName=" + this.mPackageName);
            printWriter.println(str2 + "mManagerId=" + this.mManagerId);
            printWriter.println(str2 + "mUid=" + this.mUid);
            printWriter.println(str2 + "mPid=" + this.mPid);
            printWriter.println(str2 + "mIsScanning=" + this.mIsScanning);
            SessionCreationRequest sessionCreationRequest = this.mLastSessionCreationRequest;
            if (sessionCreationRequest != null) {
                sessionCreationRequest.dump(printWriter, str2);
            }
        }

        public void startScan() {
            if (this.mIsScanning) {
                return;
            }
            this.mIsScanning = true;
            this.mUserRecord.mHandler.sendMessage(PooledLambda.obtainMessage(new MediaRouter2ServiceImpl$ManagerRecord$$ExternalSyntheticLambda0(), this.mUserRecord.mHandler));
        }

        public void stopScan() {
            if (this.mIsScanning) {
                this.mIsScanning = false;
                this.mUserRecord.mHandler.sendMessage(PooledLambda.obtainMessage(new MediaRouter2ServiceImpl$ManagerRecord$$ExternalSyntheticLambda0(), this.mUserRecord.mHandler));
            }
        }

        public String toString() {
            return "Manager " + this.mPackageName + " (pid " + this.mPid + ")";
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class UserHandler extends Handler implements MediaRoute2ProviderWatcher.Callback, MediaRoute2Provider.Callback {
        private final Map<String, MediaRoute2Info> mLastNotifiedRoutesToNonPrivilegedRouters;
        private final Map<String, MediaRoute2Info> mLastNotifiedRoutesToPrivilegedRouters;
        private final List<MediaRoute2ProviderInfo> mLastProviderInfos;
        private final ArrayList<MediaRoute2Provider> mRouteProviders;
        private boolean mRunning;
        private final WeakReference<MediaRouter2ServiceImpl> mServiceRef;
        private final CopyOnWriteArrayList<SessionCreationRequest> mSessionCreationRequests;
        private final Map<String, RouterRecord> mSessionToRouterMap;
        private final SystemMediaRoute2Provider mSystemProvider;
        private final UserRecord mUserRecord;
        private final MediaRoute2ProviderWatcher mWatcher;

        UserHandler(MediaRouter2ServiceImpl mediaRouter2ServiceImpl, UserRecord userRecord) {
            super(Looper.getMainLooper(), null, true);
            ArrayList<MediaRoute2Provider> arrayList = new ArrayList<>();
            this.mRouteProviders = arrayList;
            this.mLastProviderInfos = new ArrayList();
            this.mSessionCreationRequests = new CopyOnWriteArrayList<>();
            this.mSessionToRouterMap = new ArrayMap();
            this.mLastNotifiedRoutesToPrivilegedRouters = new ArrayMap();
            this.mLastNotifiedRoutesToNonPrivilegedRouters = new ArrayMap();
            this.mServiceRef = new WeakReference<>(mediaRouter2ServiceImpl);
            this.mUserRecord = userRecord;
            SystemMediaRoute2Provider systemMediaRoute2Provider = new SystemMediaRoute2Provider(mediaRouter2ServiceImpl.mContext, UserHandle.of(userRecord.mUserId));
            this.mSystemProvider = systemMediaRoute2Provider;
            arrayList.add(systemMediaRoute2Provider);
            this.mWatcher = new MediaRoute2ProviderWatcher(mediaRouter2ServiceImpl.mContext, this, this, userRecord.mUserId);
        }

        void init() {
            this.mSystemProvider.setCallback(this);
        }

        public void start() {
            if (this.mRunning) {
                return;
            }
            this.mRunning = true;
            this.mSystemProvider.start();
            this.mWatcher.start();
        }

        public void stop() {
            if (this.mRunning) {
                this.mRunning = false;
                this.mWatcher.stop();
                this.mSystemProvider.stop();
            }
        }

        @Override // com.android.server.media.MediaRoute2ProviderWatcher.Callback
        public void onAddProviderService(MediaRoute2ProviderServiceProxy mediaRoute2ProviderServiceProxy) {
            mediaRoute2ProviderServiceProxy.setCallback(this);
            this.mRouteProviders.add(mediaRoute2ProviderServiceProxy);
            UserRecord userRecord = this.mUserRecord;
            mediaRoute2ProviderServiceProxy.updateDiscoveryPreference(userRecord.mActivelyScanningPackages, userRecord.mCompositeDiscoveryPreference);
        }

        @Override // com.android.server.media.MediaRoute2ProviderWatcher.Callback
        public void onRemoveProviderService(MediaRoute2ProviderServiceProxy mediaRoute2ProviderServiceProxy) {
            this.mRouteProviders.remove(mediaRoute2ProviderServiceProxy);
        }

        @Override // com.android.server.media.MediaRoute2Provider.Callback
        public void onProviderStateChanged(MediaRoute2Provider mediaRoute2Provider) {
            sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$UserHandler$$ExternalSyntheticLambda10
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    ((MediaRouter2ServiceImpl.UserHandler) obj).onProviderStateChangedOnHandler((MediaRoute2Provider) obj2);
                }
            }, this, mediaRoute2Provider));
        }

        @Override // com.android.server.media.MediaRoute2Provider.Callback
        public void onSessionCreated(MediaRoute2Provider mediaRoute2Provider, long j, RoutingSessionInfo routingSessionInfo) {
            sendMessage(PooledLambda.obtainMessage(new QuadConsumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$UserHandler$$ExternalSyntheticLambda11
                public final void accept(Object obj, Object obj2, Object obj3, Object obj4) {
                    ((MediaRouter2ServiceImpl.UserHandler) obj).onSessionCreatedOnHandler((MediaRoute2Provider) obj2, ((Long) obj3).longValue(), (RoutingSessionInfo) obj4);
                }
            }, this, mediaRoute2Provider, Long.valueOf(j), routingSessionInfo));
        }

        @Override // com.android.server.media.MediaRoute2Provider.Callback
        public void onSessionUpdated(MediaRoute2Provider mediaRoute2Provider, RoutingSessionInfo routingSessionInfo) {
            sendMessage(PooledLambda.obtainMessage(new TriConsumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$UserHandler$$ExternalSyntheticLambda9
                public final void accept(Object obj, Object obj2, Object obj3) {
                    ((MediaRouter2ServiceImpl.UserHandler) obj).onSessionInfoChangedOnHandler((MediaRoute2Provider) obj2, (RoutingSessionInfo) obj3);
                }
            }, this, mediaRoute2Provider, routingSessionInfo));
        }

        @Override // com.android.server.media.MediaRoute2Provider.Callback
        public void onSessionReleased(MediaRoute2Provider mediaRoute2Provider, RoutingSessionInfo routingSessionInfo) {
            sendMessage(PooledLambda.obtainMessage(new TriConsumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$UserHandler$$ExternalSyntheticLambda3
                public final void accept(Object obj, Object obj2, Object obj3) {
                    ((MediaRouter2ServiceImpl.UserHandler) obj).onSessionReleasedOnHandler((MediaRoute2Provider) obj2, (RoutingSessionInfo) obj3);
                }
            }, this, mediaRoute2Provider, routingSessionInfo));
        }

        @Override // com.android.server.media.MediaRoute2Provider.Callback
        public void onRequestFailed(MediaRoute2Provider mediaRoute2Provider, long j, int i) {
            sendMessage(PooledLambda.obtainMessage(new QuadConsumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$UserHandler$$ExternalSyntheticLambda4
                public final void accept(Object obj, Object obj2, Object obj3, Object obj4) {
                    ((MediaRouter2ServiceImpl.UserHandler) obj).onRequestFailedOnHandler((MediaRoute2Provider) obj2, ((Long) obj3).longValue(), ((Integer) obj4).intValue());
                }
            }, this, mediaRoute2Provider, Long.valueOf(j), Integer.valueOf(i)));
        }

        public RouterRecord findRouterWithSessionLocked(String str) {
            return this.mSessionToRouterMap.get(str);
        }

        public ManagerRecord findManagerWithId(int i) {
            for (ManagerRecord managerRecord : getManagerRecords()) {
                if (managerRecord.mManagerId == i) {
                    return managerRecord;
                }
            }
            return null;
        }

        public void maybeUpdateDiscoveryPreferenceForUid(final int i) {
            boolean anyMatch;
            MediaRouter2ServiceImpl mediaRouter2ServiceImpl = this.mServiceRef.get();
            if (mediaRouter2ServiceImpl == null) {
                return;
            }
            synchronized (mediaRouter2ServiceImpl.mLock) {
                anyMatch = this.mUserRecord.mManagerRecords.stream().anyMatch(new Predicate() { // from class: com.android.server.media.MediaRouter2ServiceImpl$UserHandler$$ExternalSyntheticLambda7
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        boolean lambda$maybeUpdateDiscoveryPreferenceForUid$1;
                        lambda$maybeUpdateDiscoveryPreferenceForUid$1 = MediaRouter2ServiceImpl.UserHandler.lambda$maybeUpdateDiscoveryPreferenceForUid$1(i, (MediaRouter2ServiceImpl.ManagerRecord) obj);
                        return lambda$maybeUpdateDiscoveryPreferenceForUid$1;
                    }
                }) | this.mUserRecord.mRouterRecords.stream().anyMatch(new Predicate() { // from class: com.android.server.media.MediaRouter2ServiceImpl$UserHandler$$ExternalSyntheticLambda6
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        boolean lambda$maybeUpdateDiscoveryPreferenceForUid$0;
                        lambda$maybeUpdateDiscoveryPreferenceForUid$0 = MediaRouter2ServiceImpl.UserHandler.lambda$maybeUpdateDiscoveryPreferenceForUid$0(i, (MediaRouter2ServiceImpl.RouterRecord) obj);
                        return lambda$maybeUpdateDiscoveryPreferenceForUid$0;
                    }
                });
            }
            if (anyMatch) {
                sendMessage(PooledLambda.obtainMessage(new Consumer() { // from class: com.android.server.media.MediaRouter2ServiceImpl$UserHandler$$ExternalSyntheticLambda8
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ((MediaRouter2ServiceImpl.UserHandler) obj).updateDiscoveryPreferenceOnHandler();
                    }
                }, this));
            }
        }

        public static /* synthetic */ boolean lambda$maybeUpdateDiscoveryPreferenceForUid$0(int i, RouterRecord routerRecord) {
            return routerRecord.mUid == i;
        }

        public static /* synthetic */ boolean lambda$maybeUpdateDiscoveryPreferenceForUid$1(int i, ManagerRecord managerRecord) {
            return managerRecord.mUid == i;
        }

        public void dump(PrintWriter printWriter, String str) {
            printWriter.println(str + "UserHandler");
            printWriter.println((str + "  ") + "mRunning=" + this.mRunning);
            this.mSystemProvider.dump(printWriter, str);
            this.mWatcher.dump(printWriter, str);
        }

        public void onProviderStateChangedOnHandler(MediaRoute2Provider mediaRoute2Provider) {
            Set emptySet;
            Collection<MediaRoute2Info> emptySet2;
            MediaRoute2ProviderInfo providerInfo = mediaRoute2Provider.getProviderInfo();
            int indexOfRouteProviderInfoByUniqueId = indexOfRouteProviderInfoByUniqueId(mediaRoute2Provider.getUniqueId(), this.mLastProviderInfos);
            MediaRoute2ProviderInfo mediaRoute2ProviderInfo = indexOfRouteProviderInfoByUniqueId == -1 ? null : this.mLastProviderInfos.get(indexOfRouteProviderInfoByUniqueId);
            this.mServiceRef.get();
            if (mediaRoute2ProviderInfo == providerInfo) {
                return;
            }
            if (providerInfo != null) {
                emptySet2 = providerInfo.getRoutes();
                emptySet = (Set) emptySet2.stream().map(new Function() { // from class: com.android.server.media.MediaRouter2ServiceImpl$UserHandler$$ExternalSyntheticLambda2
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return ((MediaRoute2Info) obj).getId();
                    }
                }).collect(Collectors.toSet());
                if (indexOfRouteProviderInfoByUniqueId >= 0) {
                    this.mLastProviderInfos.set(indexOfRouteProviderInfoByUniqueId, providerInfo);
                } else {
                    this.mLastProviderInfos.add(providerInfo);
                }
            } else {
                this.mLastProviderInfos.remove(mediaRoute2ProviderInfo);
                emptySet = Collections.emptySet();
                emptySet2 = Collections.emptySet();
            }
            ArrayList arrayList = new ArrayList();
            boolean z = false;
            boolean z2 = false;
            for (MediaRoute2Info mediaRoute2Info : emptySet2) {
                if (mediaRoute2Info.isValid()) {
                    if (!mediaRoute2Provider.mIsSystemRouteProvider) {
                        this.mLastNotifiedRoutesToNonPrivilegedRouters.put(mediaRoute2Info.getId(), mediaRoute2Info);
                    }
                    MediaRoute2Info put = this.mLastNotifiedRoutesToPrivilegedRouters.put(mediaRoute2Info.getId(), mediaRoute2Info);
                    z2 |= true ^ mediaRoute2Info.equals(put);
                    if (put == null) {
                        arrayList.add(mediaRoute2Info);
                    }
                } else {
                    Slog.w(MediaRouter2ServiceImpl.TAG, "onProviderStateChangedOnHandler: Ignoring invalid route : " + mediaRoute2Info);
                }
            }
            ArrayList arrayList2 = new ArrayList();
            for (MediaRoute2Info mediaRoute2Info2 : mediaRoute2ProviderInfo == null ? Collections.emptyList() : mediaRoute2ProviderInfo.getRoutes()) {
                String id = mediaRoute2Info2.getId();
                if (!emptySet.contains(id)) {
                    this.mLastNotifiedRoutesToPrivilegedRouters.remove(id);
                    this.mLastNotifiedRoutesToNonPrivilegedRouters.remove(id);
                    arrayList2.add(mediaRoute2Info2);
                    z = true;
                }
            }
            if (!arrayList.isEmpty()) {
                Slog.i(MediaRouter2ServiceImpl.TAG, toLoggingMessage("addProviderRoutes", providerInfo.getUniqueId(), arrayList));
            }
            if (!arrayList2.isEmpty()) {
                Slog.i(MediaRouter2ServiceImpl.TAG, toLoggingMessage("removeProviderRoutes", mediaRoute2ProviderInfo.getUniqueId(), arrayList2));
            }
            dispatchUpdates(z2, z, mediaRoute2Provider.mIsSystemRouteProvider, this.mSystemProvider.getDefaultRoute());
        }

        private static String toLoggingMessage(String str, String str2, ArrayList<MediaRoute2Info> arrayList) {
            return TextUtils.formatSimple("%s | provider: %s, routes: [%s]", new Object[]{str, str2, (String) arrayList.stream().map(new Function() { // from class: com.android.server.media.MediaRouter2ServiceImpl$UserHandler$$ExternalSyntheticLambda5
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    String lambda$toLoggingMessage$2;
                    lambda$toLoggingMessage$2 = MediaRouter2ServiceImpl.UserHandler.lambda$toLoggingMessage$2((MediaRoute2Info) obj);
                    return lambda$toLoggingMessage$2;
                }
            }).collect(Collectors.joining(", "))});
        }

        public static /* synthetic */ String lambda$toLoggingMessage$2(MediaRoute2Info mediaRoute2Info) {
            return String.format("%s | %s", mediaRoute2Info.getOriginalId(), mediaRoute2Info.getName());
        }

        private void dispatchUpdates(boolean z, boolean z2, boolean z3, MediaRoute2Info mediaRoute2Info) {
            if (z || z2) {
                List<RouterRecord> routerRecords = getRouterRecords(true);
                List<RouterRecord> routerRecords2 = getRouterRecords(false);
                notifyRoutesUpdatedToManagers(getManagers(), new ArrayList(this.mLastNotifiedRoutesToPrivilegedRouters.values()));
                notifyRoutesUpdatedToRouterRecords(routerRecords, new ArrayList(this.mLastNotifiedRoutesToPrivilegedRouters.values()));
                if (!z3) {
                    notifyRoutesUpdatedToRouterRecords(routerRecords2, new ArrayList(this.mLastNotifiedRoutesToNonPrivilegedRouters.values()));
                } else if (z) {
                    this.mLastNotifiedRoutesToNonPrivilegedRouters.put(mediaRoute2Info.getId(), mediaRoute2Info);
                    notifyRoutesUpdatedToRouterRecords(routerRecords2, new ArrayList(this.mLastNotifiedRoutesToNonPrivilegedRouters.values()));
                }
            }
        }

        private static int indexOfRouteProviderInfoByUniqueId(String str, List<MediaRoute2ProviderInfo> list) {
            for (int i = 0; i < list.size(); i++) {
                if (TextUtils.equals(list.get(i).getUniqueId(), str)) {
                    return i;
                }
            }
            return -1;
        }

        public void requestRouterCreateSessionOnHandler(long j, RouterRecord routerRecord, ManagerRecord managerRecord, RoutingSessionInfo routingSessionInfo, MediaRoute2Info mediaRoute2Info) {
            try {
                if (mediaRoute2Info.isSystemRoute() && !routerRecord.hasSystemRoutingPermission()) {
                    mediaRoute2Info = this.mSystemProvider.getDefaultRoute();
                }
                routerRecord.mRouter.requestCreateSessionByManager(j, routingSessionInfo, mediaRoute2Info);
            } catch (RemoteException e) {
                Slog.w(MediaRouter2ServiceImpl.TAG, "getSessionHintsForCreatingSessionOnHandler: Failed to request. Router probably died.", e);
                notifyRequestFailedToManager(managerRecord.mManager, MediaRouter2ServiceImpl.toOriginalRequestId(j), 0);
            }
        }

        public void requestCreateSessionWithRouter2OnHandler(long j, long j2, RouterRecord routerRecord, RoutingSessionInfo routingSessionInfo, MediaRoute2Info mediaRoute2Info, Bundle bundle) {
            MediaRoute2Provider findProvider = findProvider(mediaRoute2Info.getProviderId());
            if (findProvider == null) {
                Slog.w(MediaRouter2ServiceImpl.TAG, "requestCreateSessionWithRouter2OnHandler: Ignoring session creation request since no provider found for given route=" + mediaRoute2Info);
                notifySessionCreationFailedToRouter(routerRecord, MediaRouter2ServiceImpl.toOriginalRequestId(j));
                return;
            }
            this.mSessionCreationRequests.add(new SessionCreationRequest(routerRecord, j, j2, routingSessionInfo, mediaRoute2Info));
            findProvider.requestCreateSession(j, routerRecord.mPackageName, mediaRoute2Info.getOriginalId(), bundle);
        }

        public void selectRouteOnHandler(long j, RouterRecord routerRecord, String str, MediaRoute2Info mediaRoute2Info) {
            MediaRoute2Provider findProvider;
            if (checkArgumentsForSessionControl(routerRecord, str, mediaRoute2Info, "selecting") && (findProvider = findProvider(mediaRoute2Info.getProviderId())) != null) {
                findProvider.selectRoute(j, MediaRouter2Utils.getOriginalId(str), mediaRoute2Info.getOriginalId());
            }
        }

        public void deselectRouteOnHandler(long j, RouterRecord routerRecord, String str, MediaRoute2Info mediaRoute2Info) {
            MediaRoute2Provider findProvider;
            if (checkArgumentsForSessionControl(routerRecord, str, mediaRoute2Info, "deselecting") && (findProvider = findProvider(mediaRoute2Info.getProviderId())) != null) {
                findProvider.deselectRoute(j, MediaRouter2Utils.getOriginalId(str), mediaRoute2Info.getOriginalId());
            }
        }

        public void transferToRouteOnHandler(long j, RouterRecord routerRecord, String str, MediaRoute2Info mediaRoute2Info) {
            MediaRoute2Provider findProvider;
            if (checkArgumentsForSessionControl(routerRecord, str, mediaRoute2Info, "transferring to") && (findProvider = findProvider(mediaRoute2Info.getProviderId())) != null) {
                findProvider.transferToRoute(j, MediaRouter2Utils.getOriginalId(str), mediaRoute2Info.getOriginalId());
            }
        }

        private boolean checkArgumentsForSessionControl(RouterRecord routerRecord, String str, MediaRoute2Info mediaRoute2Info, String str2) {
            if (findProvider(mediaRoute2Info.getProviderId()) == null) {
                Slog.w(MediaRouter2ServiceImpl.TAG, "Ignoring " + str2 + " route since no provider found for given route=" + mediaRoute2Info);
                return false;
            }
            if (TextUtils.equals(MediaRouter2Utils.getProviderId(str), this.mSystemProvider.getUniqueId())) {
                return true;
            }
            if (this.mSessionToRouterMap.get(str) != routerRecord) {
                Slog.w(MediaRouter2ServiceImpl.TAG, "Ignoring " + str2 + " route from non-matching router. packageName=" + routerRecord.mPackageName + " route=" + mediaRoute2Info);
                return false;
            }
            if (MediaRouter2Utils.getOriginalId(str) != null) {
                return true;
            }
            Slog.w(MediaRouter2ServiceImpl.TAG, "Failed to get original session id from unique session id. uniqueSessionId=" + str);
            return false;
        }

        public void setRouteVolumeOnHandler(long j, MediaRoute2Info mediaRoute2Info, int i) {
            MediaRoute2Provider findProvider = findProvider(mediaRoute2Info.getProviderId());
            if (findProvider == null) {
                Slog.w(MediaRouter2ServiceImpl.TAG, "setRouteVolumeOnHandler: Couldn't find provider for route=" + mediaRoute2Info);
                return;
            }
            findProvider.setRouteVolume(j, mediaRoute2Info.getOriginalId(), i);
        }

        public void setSessionVolumeOnHandler(long j, String str, int i) {
            MediaRoute2Provider findProvider = findProvider(MediaRouter2Utils.getProviderId(str));
            if (findProvider == null) {
                Slog.w(MediaRouter2ServiceImpl.TAG, "setSessionVolumeOnHandler: Couldn't find provider for session id=" + str);
                return;
            }
            findProvider.setSessionVolume(j, MediaRouter2Utils.getOriginalId(str), i);
        }

        public void releaseSessionOnHandler(long j, RouterRecord routerRecord, String str) {
            if (this.mSessionToRouterMap.get(str) != routerRecord) {
                StringBuilder sb = new StringBuilder();
                sb.append("Ignoring releasing session from non-matching router. packageName=");
                sb.append(routerRecord == null ? null : routerRecord.mPackageName);
                sb.append(" uniqueSessionId=");
                sb.append(str);
                Slog.w(MediaRouter2ServiceImpl.TAG, sb.toString());
                return;
            }
            String providerId = MediaRouter2Utils.getProviderId(str);
            if (providerId == null) {
                Slog.w(MediaRouter2ServiceImpl.TAG, "Ignoring releasing session with invalid unique session ID. uniqueSessionId=" + str);
                return;
            }
            String originalId = MediaRouter2Utils.getOriginalId(str);
            if (originalId == null) {
                Slog.w(MediaRouter2ServiceImpl.TAG, "Ignoring releasing session with invalid unique session ID. uniqueSessionId=" + str + " providerId=" + providerId);
                return;
            }
            MediaRoute2Provider findProvider = findProvider(providerId);
            if (findProvider == null) {
                Slog.w(MediaRouter2ServiceImpl.TAG, "Ignoring releasing session since no provider found for given providerId=" + providerId);
                return;
            }
            findProvider.releaseSession(j, originalId);
        }

        public void onSessionCreatedOnHandler(MediaRoute2Provider mediaRoute2Provider, long j, RoutingSessionInfo routingSessionInfo) {
            SessionCreationRequest sessionCreationRequest;
            Iterator<SessionCreationRequest> it = this.mSessionCreationRequests.iterator();
            while (true) {
                if (!it.hasNext()) {
                    sessionCreationRequest = null;
                    break;
                }
                sessionCreationRequest = it.next();
                if (sessionCreationRequest.mUniqueRequestId == j && TextUtils.equals(sessionCreationRequest.mRoute.getProviderId(), mediaRoute2Provider.getUniqueId())) {
                    break;
                }
            }
            notifySessionCreatedToManagers(sessionCreationRequest == null ? 0L : sessionCreationRequest.mManagerRequestId, routingSessionInfo);
            if (sessionCreationRequest == null) {
                Slog.w(MediaRouter2ServiceImpl.TAG, "Ignoring session creation result for unknown request. uniqueRequestId=" + j + ", sessionInfo=" + routingSessionInfo);
                return;
            }
            this.mSessionCreationRequests.remove(sessionCreationRequest);
            MediaRoute2Provider findProvider = findProvider(sessionCreationRequest.mOldSession.getProviderId());
            if (findProvider != null) {
                findProvider.prepareReleaseSession(sessionCreationRequest.mOldSession.getId());
            } else {
                Slog.w(MediaRouter2ServiceImpl.TAG, "onSessionCreatedOnHandler: Can't find provider for an old session. session=" + sessionCreationRequest.mOldSession);
            }
            this.mSessionToRouterMap.put(routingSessionInfo.getId(), sessionCreationRequest.mRouterRecord);
            if (routingSessionInfo.isSystemSession() && !sessionCreationRequest.mRouterRecord.hasSystemRoutingPermission()) {
                routingSessionInfo = this.mSystemProvider.getDefaultSessionInfo();
            }
            notifySessionCreatedToRouter(sessionCreationRequest.mRouterRecord, MediaRouter2ServiceImpl.toOriginalRequestId(j), routingSessionInfo);
        }

        public void onSessionInfoChangedOnHandler(MediaRoute2Provider mediaRoute2Provider, RoutingSessionInfo routingSessionInfo) {
            notifySessionUpdatedToManagers(getManagers(), routingSessionInfo);
            if (mediaRoute2Provider == this.mSystemProvider) {
                if (this.mServiceRef.get() == null) {
                    return;
                }
                notifySessionInfoChangedToRouters(getRouterRecords(true), routingSessionInfo);
                notifySessionInfoChangedToRouters(getRouterRecords(false), this.mSystemProvider.getDefaultSessionInfo());
                return;
            }
            RouterRecord routerRecord = this.mSessionToRouterMap.get(routingSessionInfo.getId());
            if (routerRecord == null) {
                Slog.w(MediaRouter2ServiceImpl.TAG, "onSessionInfoChangedOnHandler: No matching router found for session=" + routingSessionInfo);
                return;
            }
            notifySessionInfoChangedToRouters(Arrays.asList(routerRecord), routingSessionInfo);
        }

        public void onSessionReleasedOnHandler(MediaRoute2Provider mediaRoute2Provider, RoutingSessionInfo routingSessionInfo) {
            notifySessionReleasedToManagers(getManagers(), routingSessionInfo);
            RouterRecord routerRecord = this.mSessionToRouterMap.get(routingSessionInfo.getId());
            if (routerRecord == null) {
                Slog.w(MediaRouter2ServiceImpl.TAG, "onSessionReleasedOnHandler: No matching router found for session=" + routingSessionInfo);
                return;
            }
            notifySessionReleasedToRouter(routerRecord, routingSessionInfo);
        }

        public void onRequestFailedOnHandler(MediaRoute2Provider mediaRoute2Provider, long j, int i) {
            ManagerRecord findManagerWithId;
            if (handleSessionCreationRequestFailed(mediaRoute2Provider, j, i) || (findManagerWithId = findManagerWithId(MediaRouter2ServiceImpl.toRequesterId(j))) == null) {
                return;
            }
            notifyRequestFailedToManager(findManagerWithId.mManager, MediaRouter2ServiceImpl.toOriginalRequestId(j), i);
        }

        private boolean handleSessionCreationRequestFailed(MediaRoute2Provider mediaRoute2Provider, long j, int i) {
            SessionCreationRequest sessionCreationRequest;
            Iterator<SessionCreationRequest> it = this.mSessionCreationRequests.iterator();
            while (true) {
                if (!it.hasNext()) {
                    sessionCreationRequest = null;
                    break;
                }
                sessionCreationRequest = it.next();
                if (sessionCreationRequest.mUniqueRequestId == j && TextUtils.equals(sessionCreationRequest.mRoute.getProviderId(), mediaRoute2Provider.getUniqueId())) {
                    break;
                }
            }
            if (sessionCreationRequest == null) {
                return false;
            }
            this.mSessionCreationRequests.remove(sessionCreationRequest);
            long j2 = sessionCreationRequest.mManagerRequestId;
            if (j2 == 0) {
                notifySessionCreationFailedToRouter(sessionCreationRequest.mRouterRecord, MediaRouter2ServiceImpl.toOriginalRequestId(j));
                return true;
            }
            ManagerRecord findManagerWithId = findManagerWithId(MediaRouter2ServiceImpl.toRequesterId(j2));
            if (findManagerWithId == null) {
                return true;
            }
            notifyRequestFailedToManager(findManagerWithId.mManager, MediaRouter2ServiceImpl.toOriginalRequestId(sessionCreationRequest.mManagerRequestId), i);
            return true;
        }

        private void notifySessionCreatedToRouter(RouterRecord routerRecord, int i, RoutingSessionInfo routingSessionInfo) {
            try {
                routerRecord.mRouter.notifySessionCreated(i, routingSessionInfo);
            } catch (RemoteException e) {
                Slog.w(MediaRouter2ServiceImpl.TAG, "Failed to notify router of the session creation. Router probably died.", e);
            }
        }

        public void notifySessionCreationFailedToRouter(RouterRecord routerRecord, int i) {
            try {
                routerRecord.mRouter.notifySessionCreated(i, (RoutingSessionInfo) null);
            } catch (RemoteException e) {
                Slog.w(MediaRouter2ServiceImpl.TAG, "Failed to notify router of the session creation failure. Router probably died.", e);
            }
        }

        private void notifySessionReleasedToRouter(RouterRecord routerRecord, RoutingSessionInfo routingSessionInfo) {
            try {
                routerRecord.mRouter.notifySessionReleased(routingSessionInfo);
            } catch (RemoteException e) {
                Slog.w(MediaRouter2ServiceImpl.TAG, "Failed to notify router of the session release. Router probably died.", e);
            }
        }

        private List<IMediaRouter2Manager> getManagers() {
            ArrayList arrayList = new ArrayList();
            MediaRouter2ServiceImpl mediaRouter2ServiceImpl = this.mServiceRef.get();
            if (mediaRouter2ServiceImpl == null) {
                return arrayList;
            }
            synchronized (mediaRouter2ServiceImpl.mLock) {
                Iterator<ManagerRecord> it = this.mUserRecord.mManagerRecords.iterator();
                while (it.hasNext()) {
                    arrayList.add(it.next().mManager);
                }
            }
            return arrayList;
        }

        private List<RouterRecord> getRouterRecords() {
            ArrayList arrayList;
            MediaRouter2ServiceImpl mediaRouter2ServiceImpl = this.mServiceRef.get();
            if (mediaRouter2ServiceImpl == null) {
                return Collections.emptyList();
            }
            synchronized (mediaRouter2ServiceImpl.mLock) {
                arrayList = new ArrayList(this.mUserRecord.mRouterRecords);
            }
            return arrayList;
        }

        private List<RouterRecord> getRouterRecords(boolean z) {
            MediaRouter2ServiceImpl mediaRouter2ServiceImpl = this.mServiceRef.get();
            ArrayList arrayList = new ArrayList();
            if (mediaRouter2ServiceImpl == null) {
                return arrayList;
            }
            synchronized (mediaRouter2ServiceImpl.mLock) {
                Iterator<RouterRecord> it = this.mUserRecord.mRouterRecords.iterator();
                while (it.hasNext()) {
                    RouterRecord next = it.next();
                    if (z == next.hasSystemRoutingPermission()) {
                        arrayList.add(next);
                    }
                }
            }
            return arrayList;
        }

        private List<ManagerRecord> getManagerRecords() {
            ArrayList arrayList;
            MediaRouter2ServiceImpl mediaRouter2ServiceImpl = this.mServiceRef.get();
            if (mediaRouter2ServiceImpl == null) {
                return Collections.emptyList();
            }
            synchronized (mediaRouter2ServiceImpl.mLock) {
                arrayList = new ArrayList(this.mUserRecord.mManagerRecords);
            }
            return arrayList;
        }

        public void notifyRouterRegistered(RouterRecord routerRecord) {
            RoutingSessionInfo defaultSessionInfo;
            ArrayList arrayList = new ArrayList();
            MediaRoute2ProviderInfo mediaRoute2ProviderInfo = null;
            for (MediaRoute2ProviderInfo mediaRoute2ProviderInfo2 : this.mLastProviderInfos) {
                if (TextUtils.equals(mediaRoute2ProviderInfo2.getUniqueId(), this.mSystemProvider.getUniqueId())) {
                    mediaRoute2ProviderInfo = mediaRoute2ProviderInfo2;
                } else {
                    arrayList.addAll(mediaRoute2ProviderInfo2.getRoutes());
                }
            }
            if (routerRecord.hasSystemRoutingPermission()) {
                if (mediaRoute2ProviderInfo != null) {
                    arrayList.addAll(mediaRoute2ProviderInfo.getRoutes());
                } else {
                    Slog.wtf(MediaRouter2ServiceImpl.TAG, "System route provider not found.");
                }
                defaultSessionInfo = this.mSystemProvider.getSessionInfos().get(0);
            } else {
                arrayList.add(this.mSystemProvider.getDefaultRoute());
                defaultSessionInfo = this.mSystemProvider.getDefaultSessionInfo();
            }
            if (arrayList.size() == 0) {
                return;
            }
            routerRecord.notifyRegistered(arrayList, defaultSessionInfo);
        }

        private static void notifyRoutesUpdatedToRouterRecords(List<RouterRecord> list, List<MediaRoute2Info> list2) {
            Iterator<RouterRecord> it = list.iterator();
            while (it.hasNext()) {
                it.next().notifyRoutesUpdated(list2);
            }
        }

        private void notifySessionInfoChangedToRouters(List<RouterRecord> list, RoutingSessionInfo routingSessionInfo) {
            Iterator<RouterRecord> it = list.iterator();
            while (it.hasNext()) {
                it.next().notifySessionInfoChanged(routingSessionInfo);
            }
        }

        public void notifyInitialRoutesToManager(IMediaRouter2Manager iMediaRouter2Manager) {
            if (this.mLastNotifiedRoutesToPrivilegedRouters.isEmpty()) {
                return;
            }
            try {
                iMediaRouter2Manager.notifyRoutesUpdated(new ArrayList(this.mLastNotifiedRoutesToPrivilegedRouters.values()));
            } catch (RemoteException e) {
                Slog.w(MediaRouter2ServiceImpl.TAG, "Failed to notify all routes. Manager probably died.", e);
            }
        }

        private void notifyRoutesUpdatedToManagers(List<IMediaRouter2Manager> list, List<MediaRoute2Info> list2) {
            Iterator<IMediaRouter2Manager> it = list.iterator();
            while (it.hasNext()) {
                try {
                    it.next().notifyRoutesUpdated(list2);
                } catch (RemoteException e) {
                    Slog.w(MediaRouter2ServiceImpl.TAG, "Failed to notify routes changed. Manager probably died.", e);
                }
            }
        }

        private void notifySessionCreatedToManagers(long j, RoutingSessionInfo routingSessionInfo) {
            int requesterId = MediaRouter2ServiceImpl.toRequesterId(j);
            int originalRequestId = MediaRouter2ServiceImpl.toOriginalRequestId(j);
            for (ManagerRecord managerRecord : getManagerRecords()) {
                try {
                    managerRecord.mManager.notifySessionCreated(managerRecord.mManagerId == requesterId ? originalRequestId : 0, routingSessionInfo);
                } catch (RemoteException e) {
                    Slog.w(MediaRouter2ServiceImpl.TAG, "notifySessionCreatedToManagers: Failed to notify. Manager probably died.", e);
                }
            }
        }

        private void notifySessionUpdatedToManagers(List<IMediaRouter2Manager> list, RoutingSessionInfo routingSessionInfo) {
            Iterator<IMediaRouter2Manager> it = list.iterator();
            while (it.hasNext()) {
                try {
                    it.next().notifySessionUpdated(routingSessionInfo);
                } catch (RemoteException e) {
                    Slog.w(MediaRouter2ServiceImpl.TAG, "notifySessionUpdatedToManagers: Failed to notify. Manager probably died.", e);
                }
            }
        }

        private void notifySessionReleasedToManagers(List<IMediaRouter2Manager> list, RoutingSessionInfo routingSessionInfo) {
            Iterator<IMediaRouter2Manager> it = list.iterator();
            while (it.hasNext()) {
                try {
                    it.next().notifySessionReleased(routingSessionInfo);
                } catch (RemoteException e) {
                    Slog.w(MediaRouter2ServiceImpl.TAG, "notifySessionReleasedToManagers: Failed to notify. Manager probably died.", e);
                }
            }
        }

        public void notifyDiscoveryPreferenceChangedToManager(RouterRecord routerRecord, IMediaRouter2Manager iMediaRouter2Manager) {
            try {
                iMediaRouter2Manager.notifyDiscoveryPreferenceChanged(routerRecord.mPackageName, routerRecord.mDiscoveryPreference);
            } catch (RemoteException e) {
                Slog.w(MediaRouter2ServiceImpl.TAG, "Failed to notify preferred features changed. Manager probably died.", e);
            }
        }

        public void notifyDiscoveryPreferenceChangedToManagers(String str, RouteDiscoveryPreference routeDiscoveryPreference) {
            MediaRouter2ServiceImpl mediaRouter2ServiceImpl = this.mServiceRef.get();
            if (mediaRouter2ServiceImpl == null) {
                return;
            }
            ArrayList arrayList = new ArrayList();
            synchronized (mediaRouter2ServiceImpl.mLock) {
                Iterator<ManagerRecord> it = this.mUserRecord.mManagerRecords.iterator();
                while (it.hasNext()) {
                    arrayList.add(it.next().mManager);
                }
            }
            Iterator it2 = arrayList.iterator();
            while (it2.hasNext()) {
                try {
                    ((IMediaRouter2Manager) it2.next()).notifyDiscoveryPreferenceChanged(str, routeDiscoveryPreference);
                } catch (RemoteException e) {
                    Slog.w(MediaRouter2ServiceImpl.TAG, "Failed to notify preferred features changed. Manager probably died.", e);
                }
            }
        }

        public void notifyRouteListingPreferenceChangeToManagers(String str, RouteListingPreference routeListingPreference) {
            MediaRouter2ServiceImpl mediaRouter2ServiceImpl = this.mServiceRef.get();
            if (mediaRouter2ServiceImpl == null) {
                return;
            }
            ArrayList arrayList = new ArrayList();
            synchronized (mediaRouter2ServiceImpl.mLock) {
                Iterator<ManagerRecord> it = this.mUserRecord.mManagerRecords.iterator();
                while (it.hasNext()) {
                    arrayList.add(it.next().mManager);
                }
            }
            Iterator it2 = arrayList.iterator();
            while (it2.hasNext()) {
                try {
                    ((IMediaRouter2Manager) it2.next()).notifyRouteListingPreferenceChange(str, routeListingPreference);
                } catch (RemoteException e) {
                    Slog.w(MediaRouter2ServiceImpl.TAG, "Failed to notify preferred features changed. Manager probably died.", e);
                }
            }
        }

        public void notifyRequestFailedToManager(IMediaRouter2Manager iMediaRouter2Manager, int i, int i2) {
            try {
                iMediaRouter2Manager.notifyRequestFailed(i, i2);
            } catch (RemoteException e) {
                Slog.w(MediaRouter2ServiceImpl.TAG, "Failed to notify manager of the request failure. Manager probably died.", e);
            }
        }

        public void updateDiscoveryPreferenceOnHandler() {
            boolean z;
            final MediaRouter2ServiceImpl mediaRouter2ServiceImpl = this.mServiceRef.get();
            if (mediaRouter2ServiceImpl == null) {
                return;
            }
            List<RouterRecord> emptyList = Collections.emptyList();
            List<RouterRecord> routerRecords = getRouterRecords();
            List<ManagerRecord> managerRecords = getManagerRecords();
            if (mediaRouter2ServiceImpl.mPowerManager.isInteractive()) {
                boolean anyMatch = managerRecords.stream().anyMatch(new Predicate() { // from class: com.android.server.media.MediaRouter2ServiceImpl$UserHandler$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        boolean lambda$updateDiscoveryPreferenceOnHandler$3;
                        lambda$updateDiscoveryPreferenceOnHandler$3 = MediaRouter2ServiceImpl.UserHandler.lambda$updateDiscoveryPreferenceOnHandler$3(MediaRouter2ServiceImpl.this, (MediaRouter2ServiceImpl.ManagerRecord) obj);
                        return lambda$updateDiscoveryPreferenceOnHandler$3;
                    }
                });
                if (!anyMatch) {
                    routerRecords = (List) routerRecords.stream().filter(new Predicate() { // from class: com.android.server.media.MediaRouter2ServiceImpl$UserHandler$$ExternalSyntheticLambda1
                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj) {
                            boolean lambda$updateDiscoveryPreferenceOnHandler$4;
                            lambda$updateDiscoveryPreferenceOnHandler$4 = MediaRouter2ServiceImpl.UserHandler.lambda$updateDiscoveryPreferenceOnHandler$4(MediaRouter2ServiceImpl.this, (MediaRouter2ServiceImpl.RouterRecord) obj);
                            return lambda$updateDiscoveryPreferenceOnHandler$4;
                        }
                    }).collect(Collectors.toList());
                }
                List<RouterRecord> list = routerRecords;
                z = anyMatch;
                emptyList = list;
            } else {
                z = false;
            }
            Iterator<MediaRoute2Provider> it = this.mRouteProviders.iterator();
            while (it.hasNext()) {
                MediaRoute2Provider next = it.next();
                if (next instanceof MediaRoute2ProviderServiceProxy) {
                    ((MediaRoute2ProviderServiceProxy) next).setManagerScanning(z);
                }
            }
            HashSet hashSet = new HashSet();
            HashSet hashSet2 = new HashSet();
            boolean z2 = false;
            for (RouterRecord routerRecord : emptyList) {
                RouteDiscoveryPreference routeDiscoveryPreference = routerRecord.mDiscoveryPreference;
                hashSet.addAll(routeDiscoveryPreference.getPreferredFeatures());
                if (routeDiscoveryPreference.shouldPerformActiveScan()) {
                    hashSet2.add(routerRecord.mPackageName);
                    z2 = true;
                }
            }
            RouteDiscoveryPreference build = new RouteDiscoveryPreference.Builder(List.copyOf(hashSet), z2 || z).build();
            synchronized (mediaRouter2ServiceImpl.mLock) {
                if (build.equals(this.mUserRecord.mCompositeDiscoveryPreference) && hashSet2.equals(this.mUserRecord.mActivelyScanningPackages)) {
                    return;
                }
                UserRecord userRecord = this.mUserRecord;
                userRecord.mCompositeDiscoveryPreference = build;
                userRecord.mActivelyScanningPackages = hashSet2;
                Iterator<MediaRoute2Provider> it2 = this.mRouteProviders.iterator();
                while (it2.hasNext()) {
                    it2.next().updateDiscoveryPreference(hashSet2, this.mUserRecord.mCompositeDiscoveryPreference);
                }
            }
        }

        public static /* synthetic */ boolean lambda$updateDiscoveryPreferenceOnHandler$3(MediaRouter2ServiceImpl mediaRouter2ServiceImpl, ManagerRecord managerRecord) {
            return managerRecord.mIsScanning && mediaRouter2ServiceImpl.mActivityManager.getPackageImportance(managerRecord.mPackageName) <= MediaRouter2ServiceImpl.sPackageImportanceForScanning;
        }

        public static /* synthetic */ boolean lambda$updateDiscoveryPreferenceOnHandler$4(MediaRouter2ServiceImpl mediaRouter2ServiceImpl, RouterRecord routerRecord) {
            return mediaRouter2ServiceImpl.mActivityManager.getPackageImportance(routerRecord.mPackageName) <= MediaRouter2ServiceImpl.sPackageImportanceForScanning;
        }

        private MediaRoute2Provider findProvider(String str) {
            Iterator<MediaRoute2Provider> it = this.mRouteProviders.iterator();
            while (it.hasNext()) {
                MediaRoute2Provider next = it.next();
                if (TextUtils.equals(next.getUniqueId(), str)) {
                    return next;
                }
            }
            return null;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class SessionCreationRequest {
        public final long mManagerRequestId;
        public final RoutingSessionInfo mOldSession;
        public final MediaRoute2Info mRoute;
        public final RouterRecord mRouterRecord;
        public final long mUniqueRequestId;

        SessionCreationRequest(RouterRecord routerRecord, long j, long j2, RoutingSessionInfo routingSessionInfo, MediaRoute2Info mediaRoute2Info) {
            this.mRouterRecord = routerRecord;
            this.mUniqueRequestId = j;
            this.mManagerRequestId = j2;
            this.mOldSession = routingSessionInfo;
            this.mRoute = mediaRoute2Info;
        }

        public void dump(PrintWriter printWriter, String str) {
            printWriter.println(str + "SessionCreationRequest");
            String str2 = str + "  ";
            printWriter.println(str2 + "mUniqueRequestId=" + this.mUniqueRequestId);
            printWriter.println(str2 + "mManagerRequestId=" + this.mManagerRequestId);
            this.mOldSession.dump(printWriter, str2);
            this.mRoute.dump(printWriter, str);
        }
    }
}
