package com.android.server.am;

import android.R;
import android.app.ActivityManager;
import android.app.ActivityManagerInternal;
import android.app.AppGlobals;
import android.app.AppOpsManager;
import android.app.BackgroundStartPrivileges;
import android.app.ForegroundServiceDelegationOptions;
import android.app.ForegroundServiceStartNotAllowedException;
import android.app.ForegroundServiceTypePolicy;
import android.app.IApplicationThread;
import android.app.IForegroundServiceObserver;
import android.app.IServiceConnection;
import android.app.InvalidForegroundServiceTypeException;
import android.app.MissingForegroundServiceTypeException;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteServiceException;
import android.app.ServiceStartArgs;
import android.app.StartForegroundCalledOnStoppedServiceException;
import android.app.admin.DevicePolicyEventLogger;
import android.app.compat.CompatChanges;
import android.appwidget.AppWidgetManagerInternal;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.content.pm.ParceledListSlice;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.CompatibilityInfo;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.os.Binder;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.os.PowerExemptionManager;
import android.os.Process;
import android.os.RemoteCallback;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.TransactionTooLargeException;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.EventLog;
import android.util.Pair;
import android.util.PrintWriterPrinter;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import android.webkit.WebViewZygote;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.app.procstats.ServiceState;
import com.android.internal.notification.SystemNotificationChannels;
import com.android.internal.os.SomeArgs;
import com.android.internal.os.TimeoutRecord;
import com.android.internal.os.TransferPipe;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.FastPrintWriter;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.AppStateTracker;
import com.android.server.LocalServices;
import com.android.server.am.ActivityManagerService;
import com.android.server.am.ComponentAliasResolver;
import com.android.server.am.ServiceRecord;
import com.android.server.appop.AppOpsService;
import com.android.server.uri.NeededUriGrants;
import com.android.server.wm.ActivityServiceConnectionsHolder;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class ActiveServices {
    private static final SimpleDateFormat DATE_FORMATTER;
    private static boolean DEBUG_DELAYED_SERVICE = false;
    private static boolean DEBUG_DELAYED_STARTS = false;
    private static boolean DEBUG_PANIC_FLAG = false;
    private static final boolean DEBUG_SHORT_SERVICE;
    static final long FGS_BG_START_RESTRICTION_CHANGE_ID = 170668199;
    static final int FGS_IMMEDIATE_DISPLAY_MASK = 54;
    static final long FGS_START_EXCEPTION_CHANGE_ID = 174041399;
    static final int FGS_STOP_REASON_STOP_FOREGROUND = 1;
    static final int FGS_STOP_REASON_STOP_SERVICE = 2;
    static final int FGS_STOP_REASON_UNKNOWN = 0;
    static final long FGS_TYPE_CHECK_FOR_INSTANT_APPS = 261055255;
    static final int LAST_ANR_LIFETIME_DURATION_MSECS = 7200000;
    private static boolean LOG_SERVICE_START_STOP = false;
    private static final boolean SHOW_DUNGEON_NOTIFICATION = false;
    private static final String TAG = "ActivityManager";
    private static final String TAG_MU = "ActivityManager_MU";
    private static final String TAG_SERVICE = "ActivityManager" + ActivityManagerDebugConfig.POSTFIX_SERVICE;
    private static final String TAG_SERVICE_EXECUTING = "ActivityManager" + ActivityManagerDebugConfig.POSTFIX_SERVICE_EXECUTING;
    static final AtomicReference<Pair<Integer, Integer>> sNumForegroundServices;
    final ActivityManagerService mAm;
    AppStateTracker mAppStateTracker;
    AppWidgetManagerInternal mAppWidgetManagerInternal;
    String mCachedDeviceProvisioningPackage;
    private final ForegroundServiceTypeLoggerModule mFGSLogger;
    String mLastAnrDump;
    final int mMaxStartingBackground;
    final SparseArray<ServiceMap> mServiceMap = new SparseArray<>();
    final ArrayMap<IBinder, ArrayList<ConnectionRecord>> mServiceConnections = new ArrayMap<>();
    final ArrayList<ServiceRecord> mPendingServices = new ArrayList<>();
    final ArrayList<ServiceRecord> mRestartingServices = new ArrayList<>();
    final ArrayList<ServiceRecord> mDestroyingServices = new ArrayList<>();
    final ArrayList<ServiceRecord> mPendingFgsNotifications = new ArrayList<>();
    final ArrayMap<ForegroundServiceDelegation, ServiceRecord> mFgsDelegations = new ArrayMap<>();

    @GuardedBy({"mAm"})
    private long mBindServiceSeqCounter = 0;
    private boolean mFgsDeferralRateLimited = true;
    final SparseLongArray mFgsDeferralEligible = new SparseLongArray();
    final RemoteCallbackList<IForegroundServiceObserver> mFgsObservers = new RemoteCallbackList<>();
    private ArrayMap<ServiceRecord, ArrayList<Runnable>> mPendingBringups = new ArrayMap<>();
    private ArrayList<ServiceRecord> mTmpCollectionResults = null;

    @GuardedBy({"mAm"})
    private final SparseArray<AppOpCallback> mFgsAppOpCallbacks = new SparseArray<>();

    @GuardedBy({"mAm"})
    private final ArraySet<String> mRestartBackoffDisabledPackages = new ArraySet<>();
    boolean mScreenOn = true;
    ArraySet<String> mAllowListWhileInUsePermissionInFgs = new ArraySet<>();
    final Runnable mLastAnrDumpClearer = new Runnable() { // from class: com.android.server.am.ActiveServices.1
        @Override // java.lang.Runnable
        public void run() {
            ActivityManagerService activityManagerService = ActiveServices.this.mAm;
            ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    ActiveServices.this.mLastAnrDump = null;
                } catch (Throwable th) {
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            ActivityManagerService.resetPriorityAfterLockedSection();
        }
    };
    private final Runnable mPostDeferredFGSNotifications = new Runnable() { // from class: com.android.server.am.ActiveServices.5
        @Override // java.lang.Runnable
        public void run() {
            if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                Slog.d(ActiveServices.TAG_SERVICE, "+++ evaluating deferred FGS notifications +++");
            }
            long uptimeMillis = SystemClock.uptimeMillis();
            ActivityManagerService activityManagerService = ActiveServices.this.mAm;
            ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    for (int size = ActiveServices.this.mPendingFgsNotifications.size() - 1; size >= 0; size--) {
                        ServiceRecord serviceRecord = ActiveServices.this.mPendingFgsNotifications.get(size);
                        if (serviceRecord.fgDisplayTime <= uptimeMillis) {
                            if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                                Slog.d(ActiveServices.TAG_SERVICE, "FGS " + serviceRecord + " handling deferred notification now");
                            }
                            ActiveServices.this.mPendingFgsNotifications.remove(size);
                            if (serviceRecord.isForeground && serviceRecord.app != null) {
                                serviceRecord.postNotification(true);
                                serviceRecord.mFgsNotificationShown = true;
                            } else if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                                Slog.d(ActiveServices.TAG_SERVICE, "  - service no longer running/fg, ignoring");
                            }
                        }
                    }
                    if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                        Slog.d(ActiveServices.TAG_SERVICE, "Done evaluating deferred FGS notifications; " + ActiveServices.this.mPendingFgsNotifications.size() + " remaining");
                    }
                } catch (Throwable th) {
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            ActivityManagerService.resetPriorityAfterLockedSection();
        }
    };
    private ActiveServicesWrapper mActiveServicesWrapper = new ActiveServicesWrapper();
    private IActiveServicesExt mActiveServicesExt = (IActiveServicesExt) ExtLoader.type(IActiveServicesExt.class).base(this).create();

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    @interface FgsStopReason {
    }

    private static String fgsStopReasonToString(int i) {
        return i != 1 ? i != 2 ? "UNKNOWN" : "STOP_SERVICE" : "STOP_FOREGROUND";
    }

    private static boolean isFgsBgStart(int i) {
        return (i == 10 || i == 11 || i == 12 || i == 50) ? false : true;
    }

    static {
        boolean z = ActivityManagerDebugConfig.DEBUG_SERVICE;
        DEBUG_DELAYED_SERVICE = z;
        DEBUG_DELAYED_STARTS = z;
        LOG_SERVICE_START_STOP = z;
        DEBUG_SHORT_SERVICE = z;
        DEBUG_PANIC_FLAG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
        sNumForegroundServices = new AtomicReference<>(new Pair(0, 0));
        DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class BackgroundRestrictedListener implements AppStateTracker.BackgroundRestrictedAppListener {
        BackgroundRestrictedListener() {
        }

        public void updateBackgroundRestrictedForUidPackage(int i, String str, boolean z) {
            ActivityManagerService activityManagerService = ActiveServices.this.mAm;
            ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    ActiveServices.this.mAm.mProcessList.updateBackgroundRestrictedForUidPackageLocked(i, str, z);
                    if (!ActiveServices.this.isForegroundServiceAllowedInBackgroundRestricted(i, str) && !ActiveServices.this.isTempAllowedByAlarmClock(i)) {
                        ActiveServices.this.stopAllForegroundServicesLocked(i, str);
                    }
                } catch (Throwable th) {
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            ActivityManagerService.resetPriorityAfterLockedSection();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopAllForegroundServicesLocked(int i, String str) {
        ServiceMap serviceMapLocked = getServiceMapLocked(UserHandle.getUserId(i));
        int size = serviceMapLocked.mServicesByInstanceName.size();
        ArrayList arrayList = new ArrayList(size);
        for (int i2 = 0; i2 < size; i2++) {
            ServiceRecord valueAt = serviceMapLocked.mServicesByInstanceName.valueAt(i2);
            ServiceInfo serviceInfo = valueAt.serviceInfo;
            if ((i == serviceInfo.applicationInfo.uid || str.equals(serviceInfo.packageName)) && valueAt.isForeground && valueAt.mAllowStartForegroundAtEntering != 301 && !isDeviceProvisioningPackage(valueAt.packageName)) {
                arrayList.add(valueAt);
            }
        }
        int size2 = arrayList.size();
        if (size2 > 0 && ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
            Slog.i("ActivityManager", "Package " + str + "/" + i + " in FAS with foreground services");
        }
        for (int i3 = 0; i3 < size2; i3++) {
            ServiceRecord serviceRecord = (ServiceRecord) arrayList.get(i3);
            if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                Slog.i("ActivityManager", "  Stopping fg for service " + serviceRecord);
            }
            setServiceForegroundInnerLocked(serviceRecord, 0, null, 0, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class ActiveForegroundApp {
        boolean mAppOnTop;
        long mEndTime;
        long mHideTime;
        CharSequence mLabel;
        int mNumActive;
        String mPackageName;
        boolean mShownWhileScreenOn;
        boolean mShownWhileTop;
        long mStartTime;
        long mStartVisibleTime;
        int mUid;

        ActiveForegroundApp() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class ServiceMap extends Handler {
        static final int MSG_BG_START_TIMEOUT = 1;
        static final int MSG_ENSURE_NOT_START_BG = 3;
        static final int MSG_UPDATE_FOREGROUND_APPS = 2;
        final ArrayMap<String, ActiveForegroundApp> mActiveForegroundApps;
        boolean mActiveForegroundAppsChanged;
        final ArrayList<ServiceRecord> mDelayedStartList;
        final ArrayList<String> mPendingRemoveForegroundApps;
        final ArrayMap<ComponentName, ServiceRecord> mServicesByInstanceName;
        final ArrayMap<Intent.FilterComparison, ServiceRecord> mServicesByIntent;
        final ArrayList<ServiceRecord> mStartingBackground;
        final int mUserId;

        ServiceMap(Looper looper, int i) {
            super(looper);
            this.mServicesByInstanceName = new ArrayMap<>();
            this.mServicesByIntent = new ArrayMap<>();
            this.mDelayedStartList = new ArrayList<>();
            this.mStartingBackground = new ArrayList<>();
            this.mActiveForegroundApps = new ArrayMap<>();
            this.mPendingRemoveForegroundApps = new ArrayList<>();
            this.mUserId = i;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                ActivityManagerService activityManagerService = ActiveServices.this.mAm;
                ActivityManagerService.boostPriorityForLockedSection();
                synchronized (activityManagerService) {
                    try {
                        rescheduleDelayedStartsLocked();
                    } finally {
                    }
                }
                ActivityManagerService.resetPriorityAfterLockedSection();
                return;
            }
            if (i == 2) {
                ActiveServices.this.updateForegroundApps(this);
                return;
            }
            if (i != 3) {
                return;
            }
            ActivityManagerService activityManagerService2 = ActiveServices.this.mAm;
            ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService2) {
                try {
                    rescheduleDelayedStartsLocked();
                } finally {
                }
            }
            ActivityManagerService.resetPriorityAfterLockedSection();
        }

        void ensureNotStartingBackgroundLocked(ServiceRecord serviceRecord) {
            if (this.mStartingBackground.remove(serviceRecord)) {
                if (ActiveServices.DEBUG_DELAYED_STARTS) {
                    Slog.v(ActiveServices.TAG_SERVICE, "No longer background starting: " + serviceRecord);
                }
                removeMessages(3);
                sendMessage(obtainMessage(3));
            }
            if (this.mDelayedStartList.remove(serviceRecord) && ActiveServices.DEBUG_DELAYED_STARTS) {
                Slog.v(ActiveServices.TAG_SERVICE, "No longer delaying start: " + serviceRecord);
            }
        }

        void rescheduleDelayedStartsLocked() {
            String str;
            String str2;
            removeMessages(1);
            long uptimeMillis = SystemClock.uptimeMillis();
            int size = this.mStartingBackground.size();
            int i = 0;
            while (true) {
                str = "ActivityManager";
                if (i >= size) {
                    break;
                }
                ServiceRecord serviceRecord = this.mStartingBackground.get(i);
                if (serviceRecord.startingBgTimeout <= uptimeMillis) {
                    Slog.i("ActivityManager", "Waited long enough for: " + serviceRecord);
                    this.mStartingBackground.remove(i);
                    size += -1;
                    i += -1;
                }
                i++;
            }
            while (this.mDelayedStartList.size() > 0 && this.mStartingBackground.size() < ActiveServices.this.mMaxStartingBackground) {
                ServiceRecord remove = this.mDelayedStartList.remove(0);
                if (ActiveServices.DEBUG_DELAYED_STARTS) {
                    Slog.v(ActiveServices.TAG_SERVICE, "REM FR DELAY LIST (exec next): " + remove);
                }
                if (ActiveServices.DEBUG_DELAYED_SERVICE && this.mDelayedStartList.size() > 0) {
                    Slog.v(ActiveServices.TAG_SERVICE, "Remaining delayed list:");
                    for (int i2 = 0; i2 < this.mDelayedStartList.size(); i2++) {
                        Slog.v(ActiveServices.TAG_SERVICE, "  #" + i2 + ": " + this.mDelayedStartList.get(i2));
                    }
                }
                remove.delayed = false;
                if (remove.pendingStarts.size() <= 0) {
                    Slog.wtf(str, "**** NO PENDING STARTS! " + remove + " startReq=" + remove.startRequested + " delayedStop=" + remove.delayedStop);
                } else {
                    try {
                        ServiceRecord.StartItem startItem = remove.pendingStarts.get(0);
                        str2 = str;
                        try {
                            ActiveServices.this.startServiceInnerLocked(this, startItem.intent, remove, false, true, startItem.callingId, startItem.mCallingProcessName, startItem.mCallingProcessState, remove.startRequested, startItem.mCallingPackageName);
                        } catch (TransactionTooLargeException unused) {
                        }
                    } catch (TransactionTooLargeException unused2) {
                    }
                    str = str2;
                }
                str2 = str;
                str = str2;
            }
            if (this.mStartingBackground.size() > 0) {
                ServiceRecord serviceRecord2 = this.mStartingBackground.get(0);
                long j = serviceRecord2.startingBgTimeout;
                if (j > uptimeMillis) {
                    uptimeMillis = j;
                }
                if (ActiveServices.DEBUG_DELAYED_SERVICE) {
                    Slog.v(ActiveServices.TAG_SERVICE, "Top bg start is " + serviceRecord2 + ", can delay others up to " + uptimeMillis);
                }
                sendMessageAtTime(obtainMessage(1), uptimeMillis);
            }
            int size2 = this.mStartingBackground.size();
            ActiveServices activeServices = ActiveServices.this;
            if (size2 < activeServices.mMaxStartingBackground) {
                activeServices.mAm.backgroundServicesFinishedLocked(this.mUserId);
            }
        }
    }

    public ActiveServices(ActivityManagerService activityManagerService) {
        int i;
        int i2 = 1;
        this.mAm = activityManagerService;
        try {
            i = Integer.parseInt(SystemProperties.get("ro.config.max_starting_bg", "0"));
        } catch (RuntimeException unused) {
            i = 0;
        }
        if (i > 0) {
            i2 = i;
        } else if (!ActivityManager.isLowRamDeviceStatic()) {
            i2 = 8;
        }
        this.mMaxStartingBackground = i2;
        ServiceManager.getService("platform_compat");
        this.mFGSLogger = new ForegroundServiceTypeLoggerModule();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void systemServicesReady() {
        getAppStateTracker().addBackgroundRestrictedAppListener(new BackgroundRestrictedListener());
        this.mAppWidgetManagerInternal = (AppWidgetManagerInternal) LocalServices.getService(AppWidgetManagerInternal.class);
        setAllowListWhileInUsePermissionInFgs();
        initSystemExemptedFgsTypePermission();
        initMediaProjectFgsTypeCustomPermission();
    }

    private AppStateTracker getAppStateTracker() {
        if (this.mAppStateTracker == null) {
            this.mAppStateTracker = (AppStateTracker) LocalServices.getService(AppStateTracker.class);
        }
        return this.mAppStateTracker;
    }

    private void setAllowListWhileInUsePermissionInFgs() {
        String attentionServicePackageName = this.mAm.mContext.getPackageManager().getAttentionServicePackageName();
        if (!TextUtils.isEmpty(attentionServicePackageName)) {
            this.mAllowListWhileInUsePermissionInFgs.add(attentionServicePackageName);
        }
        String systemCaptionsServicePackageName = this.mAm.mContext.getPackageManager().getSystemCaptionsServicePackageName();
        if (TextUtils.isEmpty(systemCaptionsServicePackageName)) {
            return;
        }
        this.mAllowListWhileInUsePermissionInFgs.add(systemCaptionsServicePackageName);
    }

    ServiceRecord getServiceByNameLocked(ComponentName componentName, int i) {
        if (ActivityManagerDebugConfig.DEBUG_MU) {
            Slog.v(TAG_MU, "getServiceByNameLocked(" + componentName + "), callingUser = " + i);
        }
        return getServiceMapLocked(i).mServicesByInstanceName.get(componentName);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasBackgroundServicesLocked(int i) {
        ServiceMap serviceMap = this.mServiceMap.get(i);
        return serviceMap != null && serviceMap.mStartingBackground.size() >= this.mMaxStartingBackground;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasForegroundServiceNotificationLocked(String str, int i, String str2) {
        ServiceMap serviceMap = this.mServiceMap.get(i);
        if (serviceMap != null) {
            for (int i2 = 0; i2 < serviceMap.mServicesByInstanceName.size(); i2++) {
                ServiceRecord valueAt = serviceMap.mServicesByInstanceName.valueAt(i2);
                if (valueAt.appInfo.packageName.equals(str) && valueAt.isForeground && Objects.equals(valueAt.foregroundNoti.getChannelId(), str2)) {
                    if (!ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                        return true;
                    }
                    Slog.d(TAG_SERVICE, "Channel u" + i + "/pkg=" + str + "/channelId=" + str2 + " has fg service notification");
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ServiceMap getServiceMapLocked(int i) {
        ServiceMap serviceMap = this.mServiceMap.get(i);
        if (serviceMap != null) {
            return serviceMap;
        }
        ServiceMap serviceMap2 = new ServiceMap(this.mAm.mHandler.getLooper(), i);
        this.mServiceMap.put(i, serviceMap2);
        return serviceMap2;
    }

    ArrayMap<ComponentName, ServiceRecord> getServicesLocked(int i) {
        return getServiceMapLocked(i).mServicesByInstanceName;
    }

    private boolean appRestrictedAnyInBackground(int i, String str) {
        AppStateTracker appStateTracker = getAppStateTracker();
        if (appStateTracker != null) {
            return appStateTracker.isAppBackgroundRestricted(i, str);
        }
        return false;
    }

    void updateAppRestrictedAnyInBackgroundLocked(int i, String str) {
        ProcessRecord processInPackage;
        boolean appRestrictedAnyInBackground = appRestrictedAnyInBackground(i, str);
        UidRecord uidRecordLOSP = this.mAm.mProcessList.getUidRecordLOSP(i);
        if (uidRecordLOSP == null || (processInPackage = uidRecordLOSP.getProcessInPackage(str)) == null) {
            return;
        }
        processInPackage.mState.setBackgroundRestricted(appRestrictedAnyInBackground);
    }

    static String getProcessNameForService(ServiceInfo serviceInfo, ComponentName componentName, String str, String str2, boolean z, boolean z2) {
        if (z) {
            return str2;
        }
        if ((serviceInfo.flags & 2) == 0) {
            return serviceInfo.processName;
        }
        if (z2) {
            return str + ":ishared:" + str2;
        }
        return serviceInfo.processName + ":" + componentName.getClassName();
    }

    private static void traceInstant(String str, ServiceRecord serviceRecord) {
        if (Trace.isTagEnabled(64L)) {
            Trace.instant(64L, str + (serviceRecord.getComponentName() != null ? serviceRecord.getComponentName().toShortString() : "(?)"));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ComponentName startServiceLocked(IApplicationThread iApplicationThread, Intent intent, String str, int i, int i2, boolean z, String str2, String str3, int i3, boolean z2, int i4, String str4, String str5) throws TransactionTooLargeException {
        return startServiceLocked(iApplicationThread, intent, str, i, i2, z, str2, str3, i3, BackgroundStartPrivileges.NONE, z2, i4, str4, str5);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ComponentName startServiceLocked(IApplicationThread iApplicationThread, Intent intent, String str, int i, int i2, boolean z, String str2, String str3, int i3, BackgroundStartPrivileges backgroundStartPrivileges) throws TransactionTooLargeException {
        return startServiceLocked(iApplicationThread, intent, str, i, i2, z, str2, str3, i3, backgroundStartPrivileges, false, -1, null, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:131:0x028c  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x019d  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x01ff  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x020c  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0258  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0322 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0323  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    ComponentName startServiceLocked(IApplicationThread iApplicationThread, Intent intent, String str, int i, int i2, boolean z, String str2, String str3, int i3, BackgroundStartPrivileges backgroundStartPrivileges, boolean z2, int i4, String str4, String str5) throws TransactionTooLargeException {
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        Intent intent2;
        int i5;
        boolean z7;
        boolean z8;
        ServiceLookupResult serviceLookupResult;
        int i6;
        boolean z9;
        int i7;
        boolean z10;
        int appStartModeLOSP;
        boolean z11;
        boolean z12;
        ProcessRecord processRecord;
        int checkOpNoThrow;
        if (DEBUG_DELAYED_STARTS) {
            Slog.v(TAG_SERVICE, "startService: " + intent + " type=" + str + " args=" + intent.getExtras());
        }
        this.mActiveServicesExt.hookStartServiceLockedBegin(this.mAm.mContext, intent, i3);
        boolean z13 = false;
        if (iApplicationThread != null) {
            ProcessRecord recordForAppLOSP = this.mAm.getRecordForAppLOSP(iApplicationThread);
            if (recordForAppLOSP == null) {
                throw new SecurityException("Unable to find app for caller " + iApplicationThread + " (pid=" + i + ") when starting service " + intent);
            }
            boolean z14 = recordForAppLOSP.mState.getSetSchedGroup() != 0;
            if (this.mActiveServicesExt.interceptStartServiceLockedIfCallerNotNull(intent, recordForAppLOSP, str2)) {
                return null;
            }
            z3 = z14;
        } else {
            z3 = true;
        }
        boolean z15 = z3;
        ServiceLookupResult retrieveServiceLocked = retrieveServiceLocked(intent, str5, z2, i4, str4, str, str2, i, i2, i3, true, z15, false, false, null, false);
        if (retrieveServiceLocked == null) {
            return null;
        }
        ServiceRecord serviceRecord = retrieveServiceLocked.record;
        if (serviceRecord == null) {
            String str6 = retrieveServiceLocked.permission;
            if (str6 == null) {
                str6 = "private to package";
            }
            return new ComponentName("!", str6);
        }
        this.mActiveServicesExt.setCallerAppPackage(serviceRecord, str2);
        traceInstant("startService(): ", serviceRecord);
        setFgsRestrictionLocked(str2, i, i2, intent, serviceRecord, i3, backgroundStartPrivileges, false, !z);
        if (!this.mAm.mUserController.exists(serviceRecord.userId)) {
            Slog.w("ActivityManager", "Trying to start service with non-existent user! " + serviceRecord.userId);
            return null;
        }
        int i8 = z2 ? i4 : serviceRecord.appInfo.uid;
        String str7 = z2 ? str4 : serviceRecord.packageName;
        int i9 = serviceRecord.appInfo.targetSdkVersion;
        if (z2) {
            try {
                try {
                    i9 = AppGlobals.getPackageManager().getApplicationInfo(str7, 1024L, i3).targetSdkVersion;
                } catch (RemoteException unused) {
                }
            } catch (RemoteException unused2) {
            }
            int i10 = i9;
            z4 = !this.mAm.isUidActiveLOSP(i8);
            if (z4 || !appRestrictedAnyInBackground(i8, str7) || isTempAllowedByAlarmClock(i8) || isDeviceProvisioningPackage(str7)) {
                z5 = z15;
                z6 = false;
            } else {
                if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Forcing bg-only service start only for ");
                    sb.append(serviceRecord.shortInstanceName);
                    sb.append(" : bgLaunch=");
                    sb.append(z4);
                    sb.append(" callerFg=");
                    z5 = z15;
                    sb.append(z5);
                    Slog.d("ActivityManager", sb.toString());
                } else {
                    z5 = z15;
                }
                z6 = true;
            }
            if (z) {
                logFgsBackgroundStart(serviceRecord);
                if (serviceRecord.mAllowStartForeground == -1 && isBgFgsRestrictionEnabled(serviceRecord)) {
                    String str8 = "startForegroundService() not allowed due to mAllowStartForeground false: service " + serviceRecord.shortInstanceName;
                    Slog.w("ActivityManager", str8);
                    showFgsBgRestrictedNotificationLocked(serviceRecord);
                    logFGSStateChangeLocked(serviceRecord, 3, 0, 0, 0);
                    if (CompatChanges.isChangeEnabled(FGS_START_EXCEPTION_CHANGE_ID, i2)) {
                        throw new ForegroundServiceStartNotAllowedException(str8);
                    }
                    return null;
                }
            }
            if (z && (checkOpNoThrow = this.mAm.getAppOpsManager().checkOpNoThrow(76, i8, str7)) != 0) {
                if (checkOpNoThrow != 1) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("startForegroundService not allowed due to app op: service ");
                    intent2 = intent;
                    sb2.append(intent2);
                    sb2.append(" to ");
                    sb2.append(serviceRecord.shortInstanceName);
                    sb2.append(" from pid=");
                    i5 = i;
                    sb2.append(i5);
                    sb2.append(" uid=");
                    sb2.append(i2);
                    sb2.append(" pkg=");
                    sb2.append(str2);
                    Slog.w("ActivityManager", sb2.toString());
                    z8 = true;
                    z7 = false;
                    if (z6 && (serviceRecord.startRequested || z7)) {
                        z10 = z5;
                        z11 = true;
                        serviceLookupResult = retrieveServiceLocked;
                        i7 = -1;
                        i6 = i5;
                    } else {
                        serviceLookupResult = retrieveServiceLocked;
                        i6 = i5;
                        Intent intent3 = intent2;
                        z9 = z7;
                        i7 = -1;
                        z10 = z5;
                        appStartModeLOSP = this.mAm.getAppStartModeLOSP(i8, str7, i10, i, false, false, z6);
                        if (appStartModeLOSP == 0 && !this.mActiveServicesExt.checkAllowIfAppStartModeNotNormal(str2, intent3, serviceRecord)) {
                            Slog.w("ActivityManager", "Background start not allowed: service " + intent3 + " to " + serviceRecord.shortInstanceName + " from pid=" + i6 + " uid=" + i2 + " pkg=" + str2 + " startFg?=" + z9);
                            if (appStartModeLOSP == 1 || z8) {
                                return null;
                            }
                            if (z6 && z9) {
                                if (ActivityManagerDebugConfig.DEBUG_BACKGROUND_CHECK) {
                                    Slog.v("ActivityManager", "Silently dropping foreground service launch due to FAS");
                                }
                                return null;
                            }
                            return new ComponentName("?", "app is in background uid " + this.mAm.mProcessList.getUidRecordLOSP(i8));
                        }
                        z7 = z9;
                        z11 = true;
                    }
                    if (this.mActiveServicesExt.interceptStartServiceLockedAfterStartMode(i, i2, str2, serviceRecord, intent)) {
                        return null;
                    }
                    if (i10 >= 26 || !z7) {
                        z12 = z7;
                    } else {
                        if (ActivityManagerDebugConfig.DEBUG_BACKGROUND_CHECK || ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                            Slog.i("ActivityManager", "startForegroundService() but host targets " + i10 + " - not requiring startForeground()");
                        }
                        z12 = false;
                    }
                    synchronized (this.mAm.mPidsSelfLocked) {
                        processRecord = this.mAm.mPidsSelfLocked.get(i6);
                    }
                    String str9 = processRecord != null ? processRecord.processName : str2;
                    if (processRecord != null && processRecord.getThread() != null && !processRecord.isKilled()) {
                        i7 = processRecord.mState.getCurProcState();
                    }
                    serviceRecord.updateProcessStateOnRequest();
                    boolean z16 = z11;
                    ServiceLookupResult serviceLookupResult2 = serviceLookupResult;
                    if (deferServiceBringupIfFrozenLocked(serviceRecord, intent, str2, str3, i2, i, str9, i7, z12, z10, i3, backgroundStartPrivileges, false, null) || !requestStartTargetPermissionsReviewIfNeededLocked(serviceRecord, str2, str3, i2, intent, z10, i3, false, null) || this.mActiveServicesExt.interceptStartServiceLockedBeforeStartInner(this.mAm, serviceRecord, iApplicationThread, intent, str, z12, str3, i3, i2, i, str2)) {
                        return null;
                    }
                    boolean contains = this.mPendingServices.contains(serviceRecord);
                    ComponentName startServiceInnerLocked = startServiceInnerLocked(serviceRecord, intent, i2, i, str9, i7, z12, z10, backgroundStartPrivileges, str2);
                    IActiveServicesExt iActiveServicesExt = this.mActiveServicesExt;
                    if (!contains && this.mPendingServices.contains(serviceRecord)) {
                        z13 = z16;
                    }
                    iActiveServicesExt.handleAfterStartInnerService(startServiceInnerLocked, serviceRecord, str2, i2, z13);
                    return (serviceLookupResult2.aliasComponent == null || startServiceInnerLocked.getPackageName().startsWith("!") || startServiceInnerLocked.getPackageName().startsWith("?")) ? startServiceInnerLocked : serviceLookupResult2.aliasComponent;
                }
                if (checkOpNoThrow != 3) {
                    return new ComponentName("!!", "foreground not allowed as per app op");
                }
            }
            intent2 = intent;
            i5 = i;
            z7 = z;
            z8 = false;
            if (z6) {
            }
            serviceLookupResult = retrieveServiceLocked;
            i6 = i5;
            Intent intent32 = intent2;
            z9 = z7;
            i7 = -1;
            z10 = z5;
            appStartModeLOSP = this.mAm.getAppStartModeLOSP(i8, str7, i10, i, false, false, z6);
            if (appStartModeLOSP == 0) {
            }
            z7 = z9;
            z11 = true;
            if (this.mActiveServicesExt.interceptStartServiceLockedAfterStartMode(i, i2, str2, serviceRecord, intent)) {
            }
        }
        int i102 = i9;
        z4 = !this.mAm.isUidActiveLOSP(i8);
        if (z4) {
        }
        z5 = z15;
        z6 = false;
        if (z) {
        }
        if (z) {
            if (checkOpNoThrow != 1) {
            }
        }
        intent2 = intent;
        i5 = i;
        z7 = z;
        z8 = false;
        if (z6) {
        }
        serviceLookupResult = retrieveServiceLocked;
        i6 = i5;
        Intent intent322 = intent2;
        z9 = z7;
        i7 = -1;
        z10 = z5;
        appStartModeLOSP = this.mAm.getAppStartModeLOSP(i8, str7, i102, i, false, false, z6);
        if (appStartModeLOSP == 0) {
        }
        z7 = z9;
        z11 = true;
        if (this.mActiveServicesExt.interceptStartServiceLockedAfterStartMode(i, i2, str2, serviceRecord, intent)) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ComponentName startServiceInnerLocked(ServiceRecord serviceRecord, Intent intent, int i, int i2, String str, int i3, boolean z, boolean z2, BackgroundStartPrivileges backgroundStartPrivileges, String str2) throws TransactionTooLargeException {
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean z7;
        NeededUriGrants checkGrantUriPermissionFromIntent = this.mAm.mUgmInternal.checkGrantUriPermissionFromIntent(intent, i, serviceRecord.packageName, serviceRecord.userId);
        if (unscheduleServiceRestartLocked(serviceRecord, i, false) && ActivityManagerDebugConfig.DEBUG_SERVICE) {
            Slog.v(TAG_SERVICE, "START SERVICE WHILE RESTART PENDING: " + serviceRecord);
        }
        boolean z8 = serviceRecord.startRequested;
        serviceRecord.lastActivity = SystemClock.uptimeMillis();
        serviceRecord.startRequested = true;
        serviceRecord.delayedStop = false;
        serviceRecord.fgRequired = z;
        serviceRecord.pendingStarts.add(new ServiceRecord.StartItem(serviceRecord, false, serviceRecord.makeNextStartId(), intent, checkGrantUriPermissionFromIntent, i, str, str2, i3));
        if (serviceRecord.isForeground || serviceRecord.fgRequired) {
            if (!ActivityManagerService.doesReasonCodeAllowSchedulingUserInitiatedJobs(serviceRecord.mAllowWhileInUsePermissionInFgsReason) && !this.mAm.canScheduleUserInitiatedJobs(i, i2, str2)) {
                z7 = false;
                serviceRecord.updateAllowUiJobScheduling(z7);
                z3 = false;
            }
            z7 = true;
            serviceRecord.updateAllowUiJobScheduling(z7);
            z3 = false;
        } else {
            z3 = false;
            serviceRecord.updateAllowUiJobScheduling(false);
        }
        if (z) {
            synchronized (this.mAm.mProcessStats.mLock) {
                ServiceState tracker = serviceRecord.getTracker();
                if (tracker != null) {
                    z4 = true;
                    tracker.setForeground(true, this.mAm.mProcessStats.getMemFactorLocked(), SystemClock.uptimeMillis());
                } else {
                    z4 = true;
                }
            }
            AppOpsService appOpsService = this.mAm.mAppOpsService;
            appOpsService.startOperation(AppOpsManager.getToken(appOpsService), 76, serviceRecord.appInfo.uid, serviceRecord.packageName, null, true, false, null, false, 0, -1);
        } else {
            z4 = true;
        }
        ServiceMap serviceMapLocked = getServiceMapLocked(serviceRecord.userId);
        if (!z2 && !z && serviceRecord.app == null && this.mAm.mUserController.hasStartedUserState(serviceRecord.userId)) {
            ProcessRecord processRecordLocked = this.mAm.getProcessRecordLocked(serviceRecord.processName, serviceRecord.appInfo.uid);
            if (processRecordLocked == null || processRecordLocked.mState.getCurProcState() > 11) {
                if (DEBUG_DELAYED_SERVICE) {
                    Slog.v(TAG_SERVICE, "Potential start delay of " + serviceRecord + " in " + processRecordLocked);
                }
                if (serviceRecord.delayed) {
                    if (DEBUG_DELAYED_STARTS) {
                        Slog.v(TAG_SERVICE, "Continuing to delay: " + serviceRecord);
                    }
                    return serviceRecord.name;
                }
                if (serviceMapLocked.mStartingBackground.size() >= this.mMaxStartingBackground) {
                    Slog.i(TAG_SERVICE, "Delaying start of: " + serviceRecord);
                    serviceMapLocked.mDelayedStartList.add(serviceRecord);
                    serviceRecord.delayed = z4;
                    return serviceRecord.name;
                }
                if (DEBUG_DELAYED_STARTS) {
                    Slog.v(TAG_SERVICE, "Not delaying: " + serviceRecord);
                }
            } else if (processRecordLocked.mState.getCurProcState() >= 10) {
                if (DEBUG_DELAYED_STARTS) {
                    Slog.v(TAG_SERVICE, "Not delaying, but counting as bg: " + serviceRecord);
                }
            } else {
                if (DEBUG_DELAYED_STARTS) {
                    StringBuilder sb = new StringBuilder(128);
                    sb.append("Not potential delay (state=");
                    sb.append(processRecordLocked.mState.getCurProcState());
                    sb.append(' ');
                    sb.append(processRecordLocked.mState.getAdjType());
                    String makeAdjReason = processRecordLocked.mState.makeAdjReason();
                    if (makeAdjReason != null) {
                        sb.append(' ');
                        sb.append(makeAdjReason);
                    }
                    sb.append("): ");
                    sb.append(serviceRecord.toString());
                    Slog.v(TAG_SERVICE, sb.toString());
                }
                z6 = z3;
                z5 = z6;
            }
            z6 = z4;
            z5 = z6;
        } else {
            if (DEBUG_DELAYED_STARTS) {
                if (z2 || z) {
                    Slog.v(TAG_SERVICE, "Not potential delay (callerFg=" + z2 + " uid=" + i + " pid=" + i2 + " fgRequired=" + z + "): " + serviceRecord);
                } else if (serviceRecord.app != null) {
                    Slog.v(TAG_SERVICE, "Not potential delay (cur app=" + serviceRecord.app + "): " + serviceRecord);
                } else {
                    Slog.v(TAG_SERVICE, "Not potential delay (user " + serviceRecord.userId + " not started): " + serviceRecord);
                }
            }
            z5 = z3;
        }
        if (backgroundStartPrivileges.allowsAny()) {
            serviceRecord.allowBgActivityStartsOnServiceStart(backgroundStartPrivileges);
        }
        return startServiceInnerLocked(serviceMapLocked, intent, serviceRecord, z2, z5, i, str, i3, z8, str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean requestStartTargetPermissionsReviewIfNeededLocked(final ServiceRecord serviceRecord, String str, String str2, int i, final Intent intent, final boolean z, final int i2, boolean z2, final IServiceConnection iServiceConnection) {
        if (!this.mAm.getPackageManagerInternal().isPermissionsReviewRequired(serviceRecord.packageName, serviceRecord.userId)) {
            return true;
        }
        if (!z) {
            StringBuilder sb = new StringBuilder();
            sb.append("u");
            sb.append(serviceRecord.userId);
            sb.append(z2 ? " Binding" : " Starting");
            sb.append(" a service in package");
            sb.append(serviceRecord.packageName);
            sb.append(" requires a permissions review");
            Slog.w("ActivityManager", sb.toString());
            return false;
        }
        final Intent intent2 = new Intent("android.intent.action.REVIEW_PERMISSIONS");
        intent2.addFlags(411041792);
        intent2.putExtra("android.intent.extra.PACKAGE_NAME", serviceRecord.packageName);
        if (z2) {
            intent2.putExtra("android.intent.extra.REMOTE_CALLBACK", (Parcelable) new RemoteCallback(new RemoteCallback.OnResultListener() { // from class: com.android.server.am.ActiveServices.2
                public void onResult(Bundle bundle) {
                    ActivityManagerService activityManagerService;
                    ActivityManagerService activityManagerService2 = ActiveServices.this.mAm;
                    ActivityManagerService.boostPriorityForLockedSection();
                    synchronized (activityManagerService2) {
                        try {
                            long clearCallingIdentity = Binder.clearCallingIdentity();
                            try {
                                if (!ActiveServices.this.mPendingServices.contains(serviceRecord)) {
                                    ActivityManagerService.resetPriorityAfterLockedSection();
                                    return;
                                }
                                PackageManagerInternal packageManagerInternal = ActiveServices.this.mAm.getPackageManagerInternal();
                                ServiceRecord serviceRecord2 = serviceRecord;
                                if (!packageManagerInternal.isPermissionsReviewRequired(serviceRecord2.packageName, serviceRecord2.userId)) {
                                    try {
                                        ActiveServices.this.bringUpServiceLocked(serviceRecord, intent.getFlags(), z, false, false, false, true);
                                        activityManagerService = ActiveServices.this.mAm;
                                    } catch (RemoteException unused) {
                                        activityManagerService = ActiveServices.this.mAm;
                                    } catch (Throwable th) {
                                        ActiveServices.this.mAm.updateOomAdjPendingTargetsLocked(6);
                                        throw th;
                                    }
                                    activityManagerService.updateOomAdjPendingTargetsLocked(6);
                                } else {
                                    ActiveServices.this.unbindServiceLocked(iServiceConnection);
                                }
                                ActivityManagerService.resetPriorityAfterLockedSection();
                            } finally {
                                Binder.restoreCallingIdentity(clearCallingIdentity);
                            }
                        } catch (Throwable th2) {
                            ActivityManagerService.resetPriorityAfterLockedSection();
                            throw th2;
                        }
                    }
                }
            }));
        } else {
            ActivityManagerService activityManagerService = this.mAm;
            intent2.putExtra("android.intent.extra.INTENT", new IntentSender(activityManagerService.mPendingIntentController.getIntentSender(4, str, str2, i, i2, null, null, 0, new Intent[]{intent}, new String[]{intent.resolveType(activityManagerService.mContext.getContentResolver())}, 1409286144, null)));
        }
        if (ActivityManagerDebugConfig.DEBUG_PERMISSIONS_REVIEW) {
            Slog.i("ActivityManager", "u" + serviceRecord.userId + " Launching permission review for package " + serviceRecord.packageName);
        }
        this.mAm.mHandler.post(new Runnable() { // from class: com.android.server.am.ActiveServices.3
            @Override // java.lang.Runnable
            public void run() {
                ActiveServices.this.mAm.mContext.startActivityAsUser(intent2, new UserHandle(i2));
            }
        });
        return false;
    }

    @GuardedBy({"mAm"})
    private boolean deferServiceBringupIfFrozenLocked(final ServiceRecord serviceRecord, final Intent intent, final String str, final String str2, final int i, final int i2, final String str3, final int i3, final boolean z, final boolean z2, final int i4, final BackgroundStartPrivileges backgroundStartPrivileges, final boolean z3, final IServiceConnection iServiceConnection) {
        if (!this.mAm.getPackageManagerInternal().isPackageFrozen(serviceRecord.packageName, i, serviceRecord.userId)) {
            return false;
        }
        ArrayList<Runnable> arrayList = this.mPendingBringups.get(serviceRecord);
        if (arrayList == null) {
            arrayList = new ArrayList<>();
            this.mPendingBringups.put(serviceRecord, arrayList);
        }
        arrayList.add(new Runnable() { // from class: com.android.server.am.ActiveServices.4
            @Override // java.lang.Runnable
            public void run() {
                ActivityManagerService activityManagerService;
                ActivityManagerService activityManagerService2 = ActiveServices.this.mAm;
                ActivityManagerService.boostPriorityForLockedSection();
                synchronized (activityManagerService2) {
                    try {
                        if (!ActiveServices.this.mPendingBringups.containsKey(serviceRecord)) {
                            ActivityManagerService.resetPriorityAfterLockedSection();
                            return;
                        }
                        if (!ActiveServices.this.requestStartTargetPermissionsReviewIfNeededLocked(serviceRecord, str, str2, i, intent, z2, i4, z3, iServiceConnection)) {
                            ActivityManagerService.resetPriorityAfterLockedSection();
                            return;
                        }
                        if (z3) {
                            try {
                                ActiveServices.this.bringUpServiceLocked(serviceRecord, intent.getFlags(), z2, false, false, false, true);
                                activityManagerService = ActiveServices.this.mAm;
                            } catch (TransactionTooLargeException unused) {
                                activityManagerService = ActiveServices.this.mAm;
                            } catch (Throwable th) {
                                ActiveServices.this.mAm.updateOomAdjPendingTargetsLocked(6);
                                throw th;
                            }
                            activityManagerService.updateOomAdjPendingTargetsLocked(6);
                        } else {
                            try {
                                ActiveServices.this.startServiceInnerLocked(serviceRecord, intent, i, i2, str3, i3, z, z2, backgroundStartPrivileges, str);
                            } catch (TransactionTooLargeException unused2) {
                            }
                        }
                        ActivityManagerService.resetPriorityAfterLockedSection();
                    } catch (Throwable th2) {
                        ActivityManagerService.resetPriorityAfterLockedSection();
                        throw th2;
                    }
                }
            }
        });
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mAm"})
    public void schedulePendingServiceStartLocked(String str, int i) {
        int size = this.mPendingBringups.size();
        while (true) {
            for (int i2 = size - 1; i2 >= 0 && size > 0; i2--) {
                ServiceRecord keyAt = this.mPendingBringups.keyAt(i2);
                if (keyAt.userId == i && TextUtils.equals(keyAt.packageName, str)) {
                    ArrayList<Runnable> valueAt = this.mPendingBringups.valueAt(i2);
                    if (valueAt != null) {
                        for (int size2 = valueAt.size() - 1; size2 >= 0; size2--) {
                            valueAt.get(size2).run();
                        }
                        valueAt.clear();
                    }
                    int size3 = this.mPendingBringups.size();
                    this.mPendingBringups.remove(keyAt);
                    if (size != size3) {
                        break;
                    } else {
                        size = this.mPendingBringups.size();
                    }
                }
            }
            return;
            size = this.mPendingBringups.size();
        }
    }

    ComponentName startServiceInnerLocked(ServiceMap serviceMap, Intent intent, ServiceRecord serviceRecord, boolean z, boolean z2, int i, String str, int i2, boolean z3, String str2) throws TransactionTooLargeException {
        String str3;
        int i3;
        int i4;
        synchronized (this.mAm.mProcessStats.mLock) {
            ServiceState tracker = serviceRecord.getTracker();
            if (tracker != null) {
                tracker.setStarted(true, this.mAm.mProcessStats.getMemFactorLocked(), SystemClock.uptimeMillis());
            }
        }
        serviceRecord.callStart = false;
        int i5 = serviceRecord.appInfo.uid;
        String packageName = serviceRecord.name.getPackageName();
        String className = serviceRecord.name.getClassName();
        FrameworkStatsLog.write(99, i5, packageName, className, 1);
        this.mAm.mBatteryStatsService.noteServiceStartRunning(i5, packageName, className);
        String bringUpServiceLocked = bringUpServiceLocked(serviceRecord, intent.getFlags(), z, false, false, false, true);
        this.mAm.updateOomAdjPendingTargetsLocked(6);
        if (bringUpServiceLocked != null) {
            return new ComponentName("!!", bringUpServiceLocked);
        }
        int i6 = (serviceRecord.appInfo.flags & AudioDevice.OUT_AUX_LINE) != 0 ? 2 : 1;
        String action = intent.getAction();
        ProcessRecord processRecord = serviceRecord.app;
        if (processRecord == null || processRecord.getThread() == null) {
            str3 = str;
            i3 = 3;
            i4 = i;
        } else if (z3 || !serviceRecord.getConnections().isEmpty()) {
            i4 = i;
            i3 = 2;
            str3 = str;
        } else {
            i4 = i;
            str3 = str;
            i3 = 1;
        }
        FrameworkStatsLog.write(FrameworkStatsLog.SERVICE_REQUEST_EVENT_REPORTED, i5, i, action, 1, false, i3, getShortProcessNameForStats(i4, str3), getShortServiceNameForStats(serviceRecord), i6, packageName, str2, i2, serviceRecord.mProcessStateOnRequest);
        if (serviceRecord.startRequested && z2) {
            boolean z4 = serviceMap.mStartingBackground.size() == 0;
            serviceMap.mStartingBackground.add(serviceRecord);
            serviceRecord.startingBgTimeout = SystemClock.uptimeMillis() + this.mAm.mConstants.BG_START_TIMEOUT;
            if (DEBUG_DELAYED_SERVICE) {
                RuntimeException runtimeException = new RuntimeException("here");
                runtimeException.fillInStackTrace();
                Slog.v(TAG_SERVICE, "Starting background (first=" + z4 + "): " + serviceRecord, runtimeException);
            } else if (DEBUG_DELAYED_STARTS) {
                Slog.v(TAG_SERVICE, "Starting background (first=" + z4 + "): " + serviceRecord);
            }
            if (z4) {
                serviceMap.rescheduleDelayedStartsLocked();
            }
        } else if (z || serviceRecord.fgRequired) {
            serviceMap.ensureNotStartingBackgroundLocked(serviceRecord);
        }
        return serviceRecord.name;
    }

    private String getShortProcessNameForStats(int i, String str) {
        String[] packagesForUid = this.mAm.mContext.getPackageManager().getPackagesForUid(i);
        if (packagesForUid != null && packagesForUid.length == 1) {
            if (TextUtils.equals(packagesForUid[0], str)) {
                return null;
            }
            if (str != null && str.startsWith(packagesForUid[0])) {
                return str.substring(packagesForUid[0].length());
            }
        }
        return str;
    }

    private String getShortServiceNameForStats(ServiceRecord serviceRecord) {
        ComponentName componentName = serviceRecord.getComponentName();
        if (componentName != null) {
            return componentName.getShortClassName();
        }
        return null;
    }

    private void stopServiceLocked(ServiceRecord serviceRecord, boolean z) {
        traceInstant("stopService(): ", serviceRecord);
        try {
            Trace.traceBegin(64L, "stopServiceLocked()");
            if (serviceRecord.delayed) {
                if (DEBUG_DELAYED_STARTS) {
                    Slog.v(TAG_SERVICE, "Delaying stop of pending: " + serviceRecord);
                }
                serviceRecord.delayedStop = true;
                return;
            }
            maybeStopShortFgsTimeoutLocked(serviceRecord);
            int i = serviceRecord.appInfo.uid;
            String packageName = serviceRecord.name.getPackageName();
            String className = serviceRecord.name.getClassName();
            FrameworkStatsLog.write(99, i, packageName, className, 2);
            this.mAm.mBatteryStatsService.noteServiceStopRunning(i, packageName, className);
            serviceRecord.startRequested = false;
            if (serviceRecord.tracker != null) {
                synchronized (this.mAm.mProcessStats.mLock) {
                    serviceRecord.tracker.setStarted(false, this.mAm.mProcessStats.getMemFactorLocked(), SystemClock.uptimeMillis());
                }
            }
            serviceRecord.callStart = false;
            bringDownServiceIfNeededLocked(serviceRecord, false, false, z, "stopService");
        } finally {
            Trace.traceEnd(64L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int stopServiceLocked(IApplicationThread iApplicationThread, Intent intent, String str, int i, boolean z, int i2, String str2, String str3) {
        if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
            Slog.v(TAG_SERVICE, "stopService: " + intent + " type=" + str);
        }
        ProcessRecord recordForAppLOSP = this.mAm.getRecordForAppLOSP(iApplicationThread);
        if (iApplicationThread != null && recordForAppLOSP == null) {
            throw new SecurityException("Unable to find app for caller " + iApplicationThread + " (pid=" + Binder.getCallingPid() + ") when stopping service " + intent);
        }
        ServiceLookupResult retrieveServiceLocked = retrieveServiceLocked(intent, str3, z, i2, str2, str, null, Binder.getCallingPid(), Binder.getCallingUid(), i, false, false, false, false, null, false);
        if (retrieveServiceLocked == null) {
            return 0;
        }
        if (retrieveServiceLocked.record == null) {
            return -1;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            stopServiceLocked(retrieveServiceLocked.record, false);
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return 1;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopInBackgroundLocked(int i) {
        ApplicationInfo applicationInfo;
        int i2;
        ServiceMap serviceMap = this.mServiceMap.get(UserHandle.getUserId(i));
        if (serviceMap != null) {
            ArrayList arrayList = null;
            for (int size = serviceMap.mServicesByInstanceName.size() - 1; size >= 0; size--) {
                ServiceRecord valueAt = serviceMap.mServicesByInstanceName.valueAt(size);
                String str = valueAt.packageName;
                if (str != null && str.equals("com.oplus.autotest.qetest")) {
                    Slog.d("ActivityManager", "stopInBackgroundLocked ignore package:" + valueAt.packageName);
                } else if (!this.mActiveServicesExt.skipStopInBackgroundBegin(valueAt, i) && (i2 = (applicationInfo = valueAt.appInfo).uid) == i && valueAt.startRequested && this.mAm.getAppStartModeLOSP(i2, valueAt.packageName, applicationInfo.targetSdkVersion, -1, false, false, false) != 0) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    String str2 = valueAt.shortInstanceName;
                    EventLogTags.writeAmStopIdleService(valueAt.appInfo.uid, str2);
                    StringBuilder sb = new StringBuilder(64);
                    sb.append("Stopping service due to app idle: ");
                    UserHandle.formatUid(sb, valueAt.appInfo.uid);
                    sb.append(" ");
                    TimeUtils.formatDuration(valueAt.createRealTime - SystemClock.elapsedRealtime(), sb);
                    sb.append(" ");
                    sb.append(str2);
                    Slog.w("ActivityManager", sb.toString());
                    arrayList.add(valueAt);
                    if (appRestrictedAnyInBackground(valueAt.appInfo.uid, valueAt.packageName)) {
                        cancelForegroundNotificationLocked(valueAt);
                    }
                }
            }
            if (arrayList != null) {
                int size2 = arrayList.size();
                for (int i3 = size2 - 1; i3 >= 0; i3--) {
                    ServiceRecord serviceRecord = (ServiceRecord) arrayList.get(i3);
                    serviceRecord.delayed = false;
                    serviceMap.ensureNotStartingBackgroundLocked(serviceRecord);
                    stopServiceLocked(serviceRecord, true);
                }
                if (size2 > 0) {
                    this.mAm.updateOomAdjPendingTargetsLocked(18);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void killMisbehavingService(ServiceRecord serviceRecord, int i, int i2, String str, int i3) {
        ActivityManagerService activityManagerService = this.mAm;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                if (!serviceRecord.destroying) {
                    stopServiceLocked(serviceRecord, false);
                } else {
                    ServiceRecord remove = getServiceMapLocked(serviceRecord.userId).mServicesByInstanceName.remove(serviceRecord.instanceName);
                    if (remove != null) {
                        stopServiceLocked(remove, false);
                    }
                }
                this.mAm.crashApplicationWithType(i, i2, str, -1, "Bad notification for startForeground", true, i3);
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IBinder peekServiceLocked(Intent intent, String str, String str2) {
        ServiceLookupResult retrieveServiceLocked = retrieveServiceLocked(intent, null, str, str2, Binder.getCallingPid(), Binder.getCallingUid(), UserHandle.getCallingUserId(), false, false, false, false, false);
        if (retrieveServiceLocked != null) {
            ServiceRecord serviceRecord = retrieveServiceLocked.record;
            if (serviceRecord == null) {
                throw new SecurityException("Permission Denial: Accessing service from pid=" + Binder.getCallingPid() + ", uid=" + Binder.getCallingUid() + " requires " + retrieveServiceLocked.permission);
            }
            IntentBindRecord intentBindRecord = serviceRecord.bindings.get(serviceRecord.intent);
            if (intentBindRecord != null) {
                return intentBindRecord.binder;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:10:0x0044, code lost:
    
        if (r2.deliveredStarts.size() <= 0) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0046, code lost:
    
        r0 = r2.deliveredStarts.remove(0);
        r0.removeUriPermissionsLocked();
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0051, code lost:
    
        if (r0 != r9) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0057, code lost:
    
        if (r2.getLastStartId() == r10) goto L17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0059, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0060, code lost:
    
        if (r2.deliveredStarts.size() <= 0) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0062, code lost:
    
        android.util.Slog.w("ActivityManager", "stopServiceToken startId " + r10 + " is last, but have " + r2.deliveredStarts.size() + " remaining args");
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x003c, code lost:
    
        if (r9 != null) goto L10;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean stopServiceTokenLocked(ComponentName componentName, IBinder iBinder, int i) {
        if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
            Slog.v(TAG_SERVICE, "stopServiceToken: " + componentName + " " + iBinder + " startId=" + i);
        }
        ServiceRecord findServiceLocked = findServiceLocked(componentName, iBinder, UserHandle.getCallingUserId());
        if (findServiceLocked == null) {
            return false;
        }
        if (i >= 0) {
            ServiceRecord.StartItem findDeliveredStart = findServiceLocked.findDeliveredStart(i, false, false);
        }
        maybeStopShortFgsTimeoutLocked(findServiceLocked);
        int i2 = findServiceLocked.appInfo.uid;
        String packageName = findServiceLocked.name.getPackageName();
        String className = findServiceLocked.name.getClassName();
        FrameworkStatsLog.write(99, i2, packageName, className, 2);
        this.mAm.mBatteryStatsService.noteServiceStopRunning(i2, packageName, className);
        findServiceLocked.startRequested = false;
        if (findServiceLocked.tracker != null) {
            synchronized (this.mAm.mProcessStats.mLock) {
                findServiceLocked.tracker.setStarted(false, this.mAm.mProcessStats.getMemFactorLocked(), SystemClock.uptimeMillis());
            }
        }
        findServiceLocked.callStart = false;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        bringDownServiceIfNeededLocked(findServiceLocked, false, false, false, "stopServiceToken");
        Binder.restoreCallingIdentity(clearCallingIdentity);
        return true;
    }

    @GuardedBy({"mAm"})
    public void setServiceForegroundLocked(ComponentName componentName, IBinder iBinder, int i, Notification notification, int i2, int i3) {
        int callingUserId = UserHandle.getCallingUserId();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ServiceRecord findServiceLocked = findServiceLocked(componentName, iBinder, callingUserId);
            if (findServiceLocked != null) {
                setServiceForegroundInnerLocked(findServiceLocked, i, notification, i2, i3);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public int getForegroundServiceTypeLocked(ComponentName componentName, IBinder iBinder) {
        int callingUserId = UserHandle.getCallingUserId();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ServiceRecord findServiceLocked = findServiceLocked(componentName, iBinder, callingUserId);
            return findServiceLocked != null ? findServiceLocked.foregroundServiceType : 0;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    boolean foregroundAppShownEnoughLocked(ActiveForegroundApp activeForegroundApp, long j) {
        long j2;
        if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
            Slog.d("ActivityManager", "Shown enough: pkg=" + activeForegroundApp.mPackageName + ", uid=" + activeForegroundApp.mUid);
        }
        activeForegroundApp.mHideTime = Long.MAX_VALUE;
        if (activeForegroundApp.mShownWhileTop) {
            if (!ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                return true;
            }
            Slog.d("ActivityManager", "YES - shown while on top");
            return true;
        }
        if (this.mScreenOn || activeForegroundApp.mShownWhileScreenOn) {
            long j3 = activeForegroundApp.mStartVisibleTime;
            if (activeForegroundApp.mStartTime != j3) {
                j2 = this.mAm.mConstants.FGSERVICE_SCREEN_ON_AFTER_TIME;
            } else {
                j2 = this.mAm.mConstants.FGSERVICE_MIN_SHOWN_TIME;
            }
            long j4 = j3 + j2;
            if (j >= j4) {
                if (!ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                    return true;
                }
                Slog.d("ActivityManager", "YES - shown long enough with screen on");
                return true;
            }
            long j5 = this.mAm.mConstants.FGSERVICE_MIN_REPORT_TIME + j;
            if (j5 > j4) {
                j4 = j5;
            }
            activeForegroundApp.mHideTime = j4;
            if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                Slog.d("ActivityManager", "NO -- wait " + (activeForegroundApp.mHideTime - j) + " with screen on");
            }
        } else {
            long j6 = activeForegroundApp.mEndTime + this.mAm.mConstants.FGSERVICE_SCREEN_ON_BEFORE_TIME;
            if (j >= j6) {
                if (!ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                    return true;
                }
                Slog.d("ActivityManager", "YES - gone long enough with screen off");
                return true;
            }
            activeForegroundApp.mHideTime = j6;
            if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                Slog.d("ActivityManager", "NO -- wait " + (activeForegroundApp.mHideTime - j) + " with screen off");
            }
        }
        return false;
    }

    void updateForegroundApps(ServiceMap serviceMap) {
        ActivityManagerService activityManagerService = this.mAm;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                if (serviceMap != null) {
                    if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                        Slog.d("ActivityManager", "Updating foreground apps for user " + serviceMap.mUserId);
                    }
                    serviceMap.mPendingRemoveForegroundApps.clear();
                    long j = Long.MAX_VALUE;
                    for (int size = serviceMap.mActiveForegroundApps.size() - 1; size >= 0; size--) {
                        ActiveForegroundApp valueAt = serviceMap.mActiveForegroundApps.valueAt(size);
                        if (valueAt.mEndTime != 0) {
                            if (foregroundAppShownEnoughLocked(valueAt, elapsedRealtime)) {
                                serviceMap.mPendingRemoveForegroundApps.add(serviceMap.mActiveForegroundApps.keyAt(size));
                                serviceMap.mActiveForegroundAppsChanged = true;
                            } else {
                                long j2 = valueAt.mHideTime;
                                if (j2 < j) {
                                    j = j2;
                                }
                            }
                        }
                        if (!valueAt.mAppOnTop) {
                            if (isForegroundServiceAllowedInBackgroundRestricted(valueAt.mUid, valueAt.mPackageName)) {
                                if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                                    Slog.d("ActivityManager", "Adding active: pkg=" + valueAt.mPackageName + ", uid=" + valueAt.mUid);
                                }
                            } else {
                                if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                                    Slog.d("ActivityManager", "bg-restricted app " + valueAt.mPackageName + "/" + valueAt.mUid + " exiting top; demoting fg services ");
                                }
                                stopAllForegroundServicesLocked(valueAt.mUid, valueAt.mPackageName);
                            }
                        }
                    }
                    for (int size2 = serviceMap.mPendingRemoveForegroundApps.size() - 1; size2 >= 0; size2--) {
                        serviceMap.mActiveForegroundApps.remove(serviceMap.mPendingRemoveForegroundApps.get(size2));
                    }
                    serviceMap.removeMessages(2);
                    if (j < Long.MAX_VALUE) {
                        if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                            Slog.d("ActivityManager", "Next update time in: " + (j - elapsedRealtime));
                        }
                        serviceMap.sendMessageAtTime(serviceMap.obtainMessage(2), (j + SystemClock.uptimeMillis()) - SystemClock.elapsedRealtime());
                    }
                }
                serviceMap.mActiveForegroundAppsChanged = false;
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    private void requestUpdateActiveForegroundAppsLocked(ServiceMap serviceMap, long j) {
        Message obtainMessage = serviceMap.obtainMessage(2);
        if (j != 0) {
            serviceMap.sendMessageAtTime(obtainMessage, (j + SystemClock.uptimeMillis()) - SystemClock.elapsedRealtime());
        } else {
            serviceMap.mActiveForegroundAppsChanged = true;
            serviceMap.sendMessage(obtainMessage);
        }
    }

    private void decActiveForegroundAppLocked(ServiceMap serviceMap, ServiceRecord serviceRecord) {
        ActiveForegroundApp activeForegroundApp = serviceMap.mActiveForegroundApps.get(serviceRecord.packageName);
        if (activeForegroundApp != null) {
            int i = activeForegroundApp.mNumActive - 1;
            activeForegroundApp.mNumActive = i;
            if (i <= 0) {
                activeForegroundApp.mEndTime = SystemClock.elapsedRealtime();
                if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                    Slog.d("ActivityManager", "Ended running of service");
                }
                if (foregroundAppShownEnoughLocked(activeForegroundApp, activeForegroundApp.mEndTime)) {
                    serviceMap.mActiveForegroundApps.remove(serviceRecord.packageName);
                    serviceMap.mActiveForegroundAppsChanged = true;
                    requestUpdateActiveForegroundAppsLocked(serviceMap, 0L);
                } else {
                    long j = activeForegroundApp.mHideTime;
                    if (j < Long.MAX_VALUE) {
                        requestUpdateActiveForegroundAppsLocked(serviceMap, j);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateScreenStateLocked(boolean z) {
        if (this.mScreenOn != z) {
            this.mScreenOn = z;
            if (z) {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                    Slog.d("ActivityManager", "Screen turned on");
                }
                for (int size = this.mServiceMap.size() - 1; size >= 0; size--) {
                    ServiceMap valueAt = this.mServiceMap.valueAt(size);
                    boolean z2 = false;
                    long j = Long.MAX_VALUE;
                    for (int size2 = valueAt.mActiveForegroundApps.size() - 1; size2 >= 0; size2--) {
                        ActiveForegroundApp valueAt2 = valueAt.mActiveForegroundApps.valueAt(size2);
                        if (valueAt2.mEndTime == 0) {
                            if (!valueAt2.mShownWhileScreenOn) {
                                valueAt2.mShownWhileScreenOn = true;
                                valueAt2.mStartVisibleTime = elapsedRealtime;
                            }
                        } else {
                            if (!valueAt2.mShownWhileScreenOn && valueAt2.mStartVisibleTime == valueAt2.mStartTime) {
                                valueAt2.mStartVisibleTime = elapsedRealtime;
                                valueAt2.mEndTime = elapsedRealtime;
                            }
                            if (foregroundAppShownEnoughLocked(valueAt2, elapsedRealtime)) {
                                valueAt.mActiveForegroundApps.remove(valueAt2.mPackageName);
                                valueAt.mActiveForegroundAppsChanged = true;
                                z2 = true;
                            } else {
                                long j2 = valueAt2.mHideTime;
                                if (j2 < j) {
                                    j = j2;
                                }
                            }
                        }
                    }
                    if (z2) {
                        requestUpdateActiveForegroundAppsLocked(valueAt, 0L);
                    } else if (j < Long.MAX_VALUE) {
                        requestUpdateActiveForegroundAppsLocked(valueAt, j);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void foregroundServiceProcStateChangedLocked(UidRecord uidRecord) {
        ServiceMap serviceMap = this.mServiceMap.get(UserHandle.getUserId(uidRecord.getUid()));
        if (serviceMap != null) {
            boolean z = false;
            for (int size = serviceMap.mActiveForegroundApps.size() - 1; size >= 0; size--) {
                ActiveForegroundApp valueAt = serviceMap.mActiveForegroundApps.valueAt(size);
                if (valueAt.mUid == uidRecord.getUid()) {
                    if (uidRecord.getCurProcState() <= 2) {
                        if (!valueAt.mAppOnTop) {
                            valueAt.mAppOnTop = true;
                            z = true;
                        }
                        valueAt.mShownWhileTop = true;
                    } else if (valueAt.mAppOnTop) {
                        valueAt.mAppOnTop = false;
                        z = true;
                    }
                }
            }
            if (z) {
                requestUpdateActiveForegroundAppsLocked(serviceMap, 0L);
            }
        }
    }

    private boolean isForegroundServiceAllowedInBackgroundRestricted(ProcessRecord processRecord) {
        ProcessStateRecord processStateRecord = processRecord.mState;
        if (!isDeviceProvisioningPackage(processRecord.info.packageName) && processStateRecord.isBackgroundRestricted() && processStateRecord.getSetProcState() > 3) {
            return processStateRecord.getSetProcState() == 4 && processStateRecord.isSetBoundByNonBgRestrictedApp();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isForegroundServiceAllowedInBackgroundRestricted(int i, String str) {
        ProcessRecord processInPackage;
        UidRecord uidRecordLOSP = this.mAm.mProcessList.getUidRecordLOSP(i);
        return (uidRecordLOSP == null || (processInPackage = uidRecordLOSP.getProcessInPackage(str)) == null || !isForegroundServiceAllowedInBackgroundRestricted(processInPackage)) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isTempAllowedByAlarmClock(int i) {
        ActivityManagerService.FgsTempAllowListItem isAllowlistedForFgsStartLOSP = this.mAm.isAllowlistedForFgsStartLOSP(i);
        return isAllowlistedForFgsStartLOSP != null && isAllowlistedForFgsStartLOSP.mReasonCode == 301;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void logFgsApiBeginLocked(int i, int i2, int i3) {
        synchronized (this.mFGSLogger) {
            this.mFGSLogger.logForegroundServiceApiEventBegin(i, i2, i3, "");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void logFgsApiEndLocked(int i, int i2, int i3) {
        synchronized (this.mFGSLogger) {
            this.mFGSLogger.logForegroundServiceApiEventEnd(i, i2, i3);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void logFgsApiStateChangedLocked(int i, int i2, int i3, int i4) {
        synchronized (this.mFGSLogger) {
            this.mFGSLogger.logForegroundServiceApiStateChanged(i, i2, i3, i4);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:113:0x040c  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0479 A[Catch: all -> 0x05fe, TryCatch #7 {all -> 0x05fe, blocks: (B:43:0x0184, B:48:0x019e, B:49:0x01a5, B:50:0x01a6, B:52:0x01c3, B:54:0x01cb, B:56:0x01d5, B:57:0x01f3, B:60:0x01fb, B:63:0x0200, B:64:0x0208, B:65:0x0209, B:68:0x0214, B:72:0x0224, B:74:0x0228, B:77:0x023e, B:79:0x0254, B:83:0x025a, B:115:0x040f, B:116:0x0442, B:120:0x0479, B:122:0x047d, B:123:0x0482, B:125:0x0490, B:127:0x0498, B:129:0x04a4, B:131:0x04bb, B:133:0x04c1, B:136:0x04cb, B:137:0x04d1, B:138:0x04e7, B:139:0x04ee, B:141:0x0505, B:142:0x050b, B:149:0x0528, B:150:0x0567, B:154:0x0572, B:155:0x057a, B:157:0x0584, B:158:0x0587, B:179:0x0578, B:183:0x0525, B:190:0x059d, B:192:0x05a1, B:193:0x0450, B:194:0x0466, B:195:0x0415, B:198:0x041e, B:200:0x0427, B:202:0x0439, B:144:0x050c, B:146:0x0512, B:147:0x0521, B:152:0x0568, B:153:0x0571), top: B:42:0x0184, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:160:0x05b9  */
    /* JADX WARN: Removed duplicated region for block: B:173:0x05dc  */
    /* JADX WARN: Removed duplicated region for block: B:175:0x0721 A[ORIG_RETURN, RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:190:0x059d A[Catch: all -> 0x05fe, TryCatch #7 {all -> 0x05fe, blocks: (B:43:0x0184, B:48:0x019e, B:49:0x01a5, B:50:0x01a6, B:52:0x01c3, B:54:0x01cb, B:56:0x01d5, B:57:0x01f3, B:60:0x01fb, B:63:0x0200, B:64:0x0208, B:65:0x0209, B:68:0x0214, B:72:0x0224, B:74:0x0228, B:77:0x023e, B:79:0x0254, B:83:0x025a, B:115:0x040f, B:116:0x0442, B:120:0x0479, B:122:0x047d, B:123:0x0482, B:125:0x0490, B:127:0x0498, B:129:0x04a4, B:131:0x04bb, B:133:0x04c1, B:136:0x04cb, B:137:0x04d1, B:138:0x04e7, B:139:0x04ee, B:141:0x0505, B:142:0x050b, B:149:0x0528, B:150:0x0567, B:154:0x0572, B:155:0x057a, B:157:0x0584, B:158:0x0587, B:179:0x0578, B:183:0x0525, B:190:0x059d, B:192:0x05a1, B:193:0x0450, B:194:0x0466, B:195:0x0415, B:198:0x041e, B:200:0x0427, B:202:0x0439, B:144:0x050c, B:146:0x0512, B:147:0x0521, B:152:0x0568, B:153:0x0571), top: B:42:0x0184, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:208:0x0467  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x02d0  */
    /* JADX WARN: Removed duplicated region for block: B:264:0x046e  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x01f9  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x02cc  */
    /* JADX WARN: Type inference failed for: r0v57, types: [com.android.server.am.ActivityManagerService] */
    /* JADX WARN: Type inference failed for: r10v10 */
    /* JADX WARN: Type inference failed for: r10v11 */
    /* JADX WARN: Type inference failed for: r10v13 */
    /* JADX WARN: Type inference failed for: r10v14 */
    /* JADX WARN: Type inference failed for: r10v17 */
    /* JADX WARN: Type inference failed for: r15v15 */
    /* JADX WARN: Type inference failed for: r15v16 */
    /* JADX WARN: Type inference failed for: r15v4 */
    /* JADX WARN: Type inference failed for: r15v5, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r32v0, types: [com.android.server.am.ActiveServices] */
    /* JADX WARN: Type inference failed for: r33v0, types: [java.lang.Object, com.android.server.am.ServiceRecord] */
    @GuardedBy({"mAm"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void setServiceForegroundInnerLocked(ServiceRecord serviceRecord, int i, Notification notification, int i2, int i3) {
        boolean z;
        int checkOpNoThrow;
        boolean z2;
        boolean z3;
        int i4;
        ?? r15;
        ProcessServiceRecord processServiceRecord;
        boolean z4;
        int i5;
        boolean z5;
        boolean z6;
        int i6;
        boolean z7;
        UidRecord uidRecord;
        ProcessServiceRecord processServiceRecord2;
        int i7;
        boolean z8;
        boolean z9;
        boolean z10;
        boolean z11;
        Pair<Integer, RuntimeException> pair;
        boolean z12;
        boolean z13;
        boolean z14;
        boolean z15 = false;
        z15 = false;
        if (i == 0) {
            if (serviceRecord.isForeground) {
                traceInstant("stopForeground(): ", serviceRecord);
                ServiceMap serviceMapLocked = getServiceMapLocked(serviceRecord.userId);
                if (serviceMapLocked != null) {
                    decActiveForegroundAppLocked(serviceMapLocked, serviceRecord);
                }
                maybeStopShortFgsTimeoutLocked(serviceRecord);
                if ((i2 & 1) != 0) {
                    cancelForegroundNotificationLocked(serviceRecord);
                    serviceRecord.foregroundId = 0;
                    serviceRecord.foregroundNoti = null;
                } else if (serviceRecord.appInfo.targetSdkVersion >= 21) {
                    if (!serviceRecord.mFgsNotificationShown && serviceRecord.app != null) {
                        serviceRecord.postNotification(false);
                    }
                    dropFgsNotificationStateLocked(serviceRecord);
                    if ((i2 & 2) != 0) {
                        serviceRecord.foregroundId = 0;
                        serviceRecord.foregroundNoti = null;
                    }
                }
                serviceRecord.isForeground = false;
                serviceRecord.mFgsExitTime = SystemClock.uptimeMillis();
                synchronized (this.mAm.mProcessStats.mLock) {
                    ServiceState tracker = serviceRecord.getTracker();
                    if (tracker != null) {
                        tracker.setForeground(false, this.mAm.mProcessStats.getMemFactorLocked(), SystemClock.uptimeMillis());
                    }
                }
                AppOpsService appOpsService = this.mAm.mAppOpsService;
                appOpsService.finishOperation(AppOpsManager.getToken(appOpsService), 76, serviceRecord.appInfo.uid, serviceRecord.packageName, null);
                unregisterAppOpCallbackLocked(serviceRecord);
                long j = serviceRecord.mFgsExitTime;
                long j2 = serviceRecord.mFgsEnterTime;
                logFGSStateChangeLocked(serviceRecord, 2, j > j2 ? (int) (j - j2) : 0, 1, 0);
                serviceRecord.foregroundServiceType = 0;
                synchronized (this.mFGSLogger) {
                    this.mFGSLogger.logForegroundServiceStop(serviceRecord.appInfo.uid, serviceRecord);
                }
                serviceRecord.mFgsNotificationWasDeferred = false;
                signalForegroundServiceObserversLocked(serviceRecord);
                resetFgsRestrictionLocked(serviceRecord);
                this.mAm.updateForegroundServiceUsageStats(serviceRecord.name, serviceRecord.userId, false);
                ProcessRecord processRecord = serviceRecord.app;
                if (processRecord != null) {
                    this.mAm.updateLruProcessLocked(processRecord, false, null);
                    updateServiceForegroundLocked(serviceRecord.app.mServices, true);
                }
                updateNumForegroundServicesLocked();
                return;
            }
            return;
        }
        if (notification == null) {
            throw new IllegalArgumentException("null notification");
        }
        traceInstant("startForeground(): ", serviceRecord);
        if (serviceRecord.appInfo.isInstantApp()) {
            AppOpsManager appOpsManager = this.mAm.getAppOpsManager();
            ApplicationInfo applicationInfo = serviceRecord.appInfo;
            int checkOpNoThrow2 = appOpsManager.checkOpNoThrow(68, applicationInfo.uid, applicationInfo.packageName);
            if (checkOpNoThrow2 != 0) {
                if (checkOpNoThrow2 == 1) {
                    Slog.w("ActivityManager", "Instant app " + serviceRecord.appInfo.packageName + " does not have permission to create foreground services, ignoring.");
                    return;
                }
                if (checkOpNoThrow2 == 2) {
                    throw new SecurityException("Instant app " + serviceRecord.appInfo.packageName + " does not have permission to create foreground services");
                }
                this.mAm.enforcePermission("android.permission.INSTANT_APP_FOREGROUND_SERVICE", serviceRecord.app.getPid(), serviceRecord.appInfo.uid, "startForeground");
            }
        } else if (serviceRecord.appInfo.targetSdkVersion >= 28) {
            this.mAm.enforcePermission("android.permission.FOREGROUND_SERVICE", serviceRecord.app.getPid(), serviceRecord.appInfo.uid, "startForeground");
        }
        int foregroundServiceType = serviceRecord.serviceInfo.getForegroundServiceType();
        int i8 = i3 == -1 ? foregroundServiceType : i3;
        if ((i8 & foregroundServiceType) != i8 && !SystemProperties.getBoolean("debug.skip_fgs_manifest_type_check", false)) {
            String str = "foregroundServiceType " + String.format("0x%08X", Integer.valueOf(i8)) + " is not a subset of foregroundServiceType attribute " + String.format("0x%08X", Integer.valueOf(foregroundServiceType)) + " in service element of manifest file";
            if (!serviceRecord.appInfo.isInstantApp() || CompatChanges.isChangeEnabled(FGS_TYPE_CHECK_FOR_INSTANT_APPS, serviceRecord.appInfo.uid)) {
                throw new IllegalArgumentException(str);
            }
            Slog.w("ActivityManager", str + "\nThis will be an exception once the target SDK level is UDC");
        }
        if ((i8 & 2048) != 0 && i8 != 2048) {
            Slog.w(TAG_SERVICE, "startForeground(): FOREGROUND_SERVICE_TYPE_SHORT_SERVICE is combined with other types. SHORT_SERVICE will be ignored.");
            i8 &= -2049;
        }
        int i9 = i8;
        if (serviceRecord.fgRequired) {
            if (ActivityManagerDebugConfig.DEBUG_SERVICE || ActivityManagerDebugConfig.DEBUG_BACKGROUND_CHECK) {
                Slog.i("ActivityManager", "Service called startForeground() as required: " + ((Object) serviceRecord));
            }
            serviceRecord.fgRequired = false;
            serviceRecord.fgWaiting = false;
            this.mAm.mHandler.removeMessages(66, serviceRecord);
            this.mActiveServicesExt.updateExecutingComponent(serviceRecord.appInfo.uid, "fg-service", 2);
            z = true;
        } else {
            z = false;
        }
        boolean z16 = z;
        ProcessServiceRecord processServiceRecord3 = serviceRecord.app.mServices;
        try {
            checkOpNoThrow = this.mAm.getAppOpsManager().checkOpNoThrow(76, serviceRecord.appInfo.uid, serviceRecord.packageName);
        } catch (Throwable th) {
            th = th;
        }
        if (checkOpNoThrow != 0) {
            if (checkOpNoThrow == 1) {
                Slog.w("ActivityManager", "Service.startForeground() not allowed due to app op: service " + serviceRecord.shortInstanceName);
                z2 = true;
                if (!z2 || isForegroundServiceAllowedInBackgroundRestricted(serviceRecord.app) || isTempAllowedByAlarmClock(serviceRecord.app.uid)) {
                    z3 = z2;
                } else {
                    Slog.w("ActivityManager", "Service.startForeground() not allowed due to bg restriction: service " + serviceRecord.shortInstanceName);
                    updateServiceForegroundLocked(processServiceRecord3, false);
                    z3 = true;
                }
                boolean isBgFgsRestrictionEnabled = isBgFgsRestrictionEnabled(serviceRecord);
                if (z3) {
                    if (i9 == 2048 && !serviceRecord.startRequested) {
                        throw new StartForegroundCalledOnStoppedServiceException("startForeground(SHORT_SERVICE) called on a service that's not started.");
                    }
                    boolean isShortFgs = serviceRecord.isShortFgs();
                    boolean z17 = i9 == 2048;
                    boolean shouldTriggerShortFgsTimeout = serviceRecord.shouldTriggerShortFgsTimeout(SystemClock.uptimeMillis());
                    if (serviceRecord.isForeground && ((isShortFgs || z17) && DEBUG_SHORT_SERVICE)) {
                        String str2 = TAG_SERVICE;
                        Object[] objArr = new Object[4];
                        objArr[0] = Integer.valueOf(serviceRecord.foregroundServiceType);
                        objArr[1] = shouldTriggerShortFgsTimeout ? "(timed out short FGS)" : "";
                        objArr[2] = Integer.valueOf(i3);
                        objArr[3] = serviceRecord.toString();
                        Slog.i(str2, String.format("FGS type changing from %x%s to %x: %s", objArr));
                    }
                    try {
                    } catch (Throwable th2) {
                        th = th2;
                        z15 = false;
                    }
                    if (!serviceRecord.isForeground || !isShortFgs) {
                        processServiceRecord2 = processServiceRecord3;
                        i7 = i9;
                        z8 = z3;
                        ?? r10 = 65535;
                        z9 = true;
                        int i10 = serviceRecord.mStartForegroundCount;
                        try {
                            if (i10 == 0) {
                                if (!serviceRecord.fgRequired) {
                                    long elapsedRealtime = SystemClock.elapsedRealtime() - serviceRecord.createRealTime;
                                    if (elapsedRealtime > this.mAm.mConstants.mFgsStartForegroundTimeoutMs) {
                                        resetFgsRestrictionLocked(serviceRecord);
                                        setFgsRestrictionLocked(serviceRecord.serviceInfo.packageName, serviceRecord.app.getPid(), serviceRecord.appInfo.uid, serviceRecord.intent.getIntent(), serviceRecord, serviceRecord.userId, BackgroundStartPrivileges.NONE, false, false);
                                        String str3 = "startForegroundDelayMs:" + elapsedRealtime;
                                        if (serviceRecord.mInfoAllowStartForeground != null) {
                                            serviceRecord.mInfoAllowStartForeground += "; " + str3;
                                        } else {
                                            serviceRecord.mInfoAllowStartForeground = str3;
                                        }
                                        r10 = 0;
                                        serviceRecord.mLoggedInfoAllowStartForeground = false;
                                    }
                                }
                                z5 = false;
                                z10 = false;
                            } else {
                                if (i10 >= 1) {
                                    r10 = 0;
                                    setFgsRestrictionLocked(serviceRecord.serviceInfo.packageName, serviceRecord.app.getPid(), serviceRecord.appInfo.uid, serviceRecord.intent.getIntent(), serviceRecord, serviceRecord.userId, BackgroundStartPrivileges.NONE, false, false);
                                }
                                z5 = false;
                                z10 = false;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            z15 = r10;
                        }
                        th = th;
                        if (z16) {
                            synchronized (this.mAm.mProcessStats.mLock) {
                                ServiceState tracker2 = serviceRecord.getTracker();
                                if (tracker2 != null) {
                                    tracker2.setForeground(z15, this.mAm.mProcessStats.getMemFactorLocked(), SystemClock.uptimeMillis());
                                }
                            }
                        }
                        if (z) {
                            AppOpsService appOpsService2 = this.mAm.mAppOpsService;
                            appOpsService2.finishOperation(AppOpsManager.getToken(appOpsService2), 76, serviceRecord.appInfo.uid, serviceRecord.packageName, null);
                        }
                        throw th;
                    }
                    serviceRecord.mAllowStartForeground = -1;
                    processServiceRecord2 = processServiceRecord3;
                    i7 = i9;
                    z8 = z3;
                    z9 = true;
                    setFgsRestrictionLocked(serviceRecord.serviceInfo.packageName, serviceRecord.app.getPid(), serviceRecord.appInfo.uid, serviceRecord.intent.getIntent(), serviceRecord, serviceRecord.userId, BackgroundStartPrivileges.NONE, false, false);
                    if (serviceRecord.mAllowStartForeground == -1) {
                        Slog.w(TAG_SERVICE, "FGS type change to/from SHORT_SERVICE:  BFSL DENIED.");
                    } else if (DEBUG_SHORT_SERVICE) {
                        Slog.w(TAG_SERVICE, "FGS type change to/from SHORT_SERVICE:  BFSL Allowed: " + PowerExemptionManager.reasonCodeToString(serviceRecord.mAllowStartForeground));
                    }
                    if (isBgFgsRestrictionEnabled && serviceRecord.mAllowStartForeground == -1) {
                        z12 = false;
                        if (z12) {
                            if (z17) {
                                z13 = true;
                                z14 = false;
                                z5 = z14;
                                z10 = z13;
                            }
                        } else if (z17) {
                            z14 = true;
                            z13 = false;
                            z5 = z14;
                            z10 = z13;
                        }
                        z14 = false;
                        z13 = false;
                        z5 = z14;
                        z10 = z13;
                    }
                    z12 = true;
                    if (z12) {
                    }
                    z14 = false;
                    z13 = false;
                    z5 = z14;
                    z10 = z13;
                    if (!serviceRecord.mAllowWhileInUsePermissionInFgs) {
                        Slog.w("ActivityManager", "Foreground service started from background can not have location/camera/microphone access: service " + serviceRecord.shortInstanceName);
                    }
                    if (!z10) {
                        logFgsBackgroundStart(serviceRecord);
                        if (serviceRecord.mAllowStartForeground == -1 && isBgFgsRestrictionEnabled) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Service.startForeground() not allowed due to mAllowStartForeground false: service ");
                            sb.append(serviceRecord.shortInstanceName);
                            sb.append(isShortFgs ? " (Called on SHORT_SERVICE)" : "");
                            String sb2 = sb.toString();
                            Slog.w("ActivityManager", sb2);
                            showFgsBgRestrictedNotificationLocked(serviceRecord);
                            processServiceRecord = processServiceRecord2;
                            updateServiceForegroundLocked(processServiceRecord, z9);
                            logFGSStateChangeLocked(serviceRecord, 3, 0, 0, 0);
                            if (CompatChanges.isChangeEnabled(FGS_START_EXCEPTION_CHANGE_ID, serviceRecord.appInfo.uid)) {
                                throw new ForegroundServiceStartNotAllowedException(sb2);
                            }
                            z11 = z9 ? 1 : 0;
                            i4 = i7;
                            if (z11) {
                                if (i4 != 0) {
                                    z15 = false;
                                    z15 = false;
                                    int i11 = 1073741824;
                                    if ((i4 & 1073741824) == 0) {
                                        i11 = 0;
                                    }
                                    int highestOneBit = Integer.highestOneBit(i4);
                                    int i12 = i4;
                                    Pair<Integer, RuntimeException> pair2 = null;
                                    while (true) {
                                        if (highestOneBit == 0) {
                                            pair = pair2;
                                            break;
                                        }
                                        Pair<Integer, RuntimeException> validateForegroundServiceType = validateForegroundServiceType(serviceRecord, highestOneBit, i11, i3);
                                        i12 &= ~highestOneBit;
                                        if (((Integer) validateForegroundServiceType.first).intValue() != z9) {
                                            pair = validateForegroundServiceType;
                                            break;
                                        } else {
                                            highestOneBit = Integer.highestOneBit(i12);
                                            pair2 = validateForegroundServiceType;
                                        }
                                    }
                                } else {
                                    z15 = false;
                                    pair = validateForegroundServiceType(serviceRecord, i4, 0, i3);
                                }
                                int intValue = ((Integer) pair.first).intValue();
                                if (pair.second != null) {
                                    logFGSStateChangeLocked(serviceRecord, 3, 0, 0, ((Integer) pair.first).intValue());
                                    throw ((RuntimeException) pair.second);
                                }
                                i5 = intValue;
                                z4 = z11;
                                r15 = z9;
                            } else {
                                z15 = false;
                                i5 = 0;
                                z4 = z11;
                                r15 = z9;
                            }
                        }
                    }
                    processServiceRecord = processServiceRecord2;
                    z11 = z8;
                    i4 = i7;
                    if (z11) {
                    }
                } else {
                    i4 = i9;
                    boolean z18 = z3;
                    r15 = 1;
                    processServiceRecord = processServiceRecord3;
                    z4 = z18 ? 1 : 0;
                    i5 = 0;
                    z5 = false;
                }
                if (z4) {
                    if (serviceRecord.foregroundId != i) {
                        cancelForegroundNotificationLocked(serviceRecord);
                        serviceRecord.foregroundId = i;
                    }
                    notification.flags |= 64;
                    serviceRecord.foregroundNoti = notification;
                    serviceRecord.foregroundServiceType = i4;
                    if (serviceRecord.isForeground) {
                        i6 = 2;
                        z7 = z16;
                    } else {
                        ServiceMap serviceMapLocked2 = getServiceMapLocked(serviceRecord.userId);
                        if (serviceMapLocked2 != null) {
                            ActiveForegroundApp activeForegroundApp = serviceMapLocked2.mActiveForegroundApps.get(serviceRecord.packageName);
                            if (activeForegroundApp == null) {
                                activeForegroundApp = new ActiveForegroundApp();
                                activeForegroundApp.mPackageName = serviceRecord.packageName;
                                activeForegroundApp.mUid = serviceRecord.appInfo.uid;
                                activeForegroundApp.mShownWhileScreenOn = this.mScreenOn;
                                ProcessRecord processRecord2 = serviceRecord.app;
                                if (processRecord2 == null || (uidRecord = processRecord2.getUidRecord()) == null) {
                                    i6 = 2;
                                } else {
                                    i6 = 2;
                                    boolean z19 = uidRecord.getCurProcState() <= 2 ? r15 : z15 ? 1 : 0;
                                    activeForegroundApp.mShownWhileTop = z19;
                                    activeForegroundApp.mAppOnTop = z19;
                                }
                                long elapsedRealtime2 = SystemClock.elapsedRealtime();
                                activeForegroundApp.mStartVisibleTime = elapsedRealtime2;
                                activeForegroundApp.mStartTime = elapsedRealtime2;
                                serviceMapLocked2.mActiveForegroundApps.put(serviceRecord.packageName, activeForegroundApp);
                                requestUpdateActiveForegroundAppsLocked(serviceMapLocked2, 0L);
                            } else {
                                i6 = 2;
                            }
                            activeForegroundApp.mNumActive += r15;
                        } else {
                            i6 = 2;
                        }
                        serviceRecord.isForeground = r15;
                        serviceRecord.mAllowStartForegroundAtEntering = serviceRecord.mAllowStartForeground;
                        serviceRecord.mAllowWhileInUsePermissionInFgsAtEntering = serviceRecord.mAllowWhileInUsePermissionInFgs;
                        serviceRecord.mStartForegroundCount += r15;
                        serviceRecord.mFgsEnterTime = SystemClock.uptimeMillis();
                        if (z16) {
                            z16 = z15 ? 1 : 0;
                        } else {
                            synchronized (this.mAm.mProcessStats.mLock) {
                                ServiceState tracker3 = serviceRecord.getTracker();
                                if (tracker3 != null) {
                                    tracker3.setForeground((boolean) r15, this.mAm.mProcessStats.getMemFactorLocked(), SystemClock.uptimeMillis());
                                }
                            }
                            z16 = z16;
                        }
                        AppOpsService appOpsService3 = this.mAm.mAppOpsService;
                        appOpsService3.startOperation(AppOpsManager.getToken(appOpsService3), 76, serviceRecord.appInfo.uid, serviceRecord.packageName, null, true, false, "", false, 0, -1);
                        registerAppOpCallbackLocked(serviceRecord);
                        this.mAm.updateForegroundServiceUsageStats(serviceRecord.name, serviceRecord.userId, r15);
                        logFGSStateChangeLocked(serviceRecord, 1, 0, 0, i5);
                        synchronized (this.mFGSLogger) {
                            this.mFGSLogger.logForegroundServiceStart(serviceRecord.appInfo.uid, z15 ? 1 : 0, serviceRecord);
                        }
                        updateNumForegroundServicesLocked();
                        z7 = z16;
                    }
                    signalForegroundServiceObserversLocked(serviceRecord);
                    serviceRecord.postNotification(r15);
                    if (serviceRecord.app != null) {
                        updateServiceForegroundLocked(processServiceRecord, r15);
                    }
                    getServiceMapLocked(serviceRecord.userId).ensureNotStartingBackgroundLocked(serviceRecord);
                    this.mAm.notifyPackageUse(serviceRecord.serviceInfo.packageName, i6);
                    maybeUpdateShortFgsTrackingLocked(serviceRecord, z5);
                    z6 = z7;
                } else {
                    z6 = z16;
                    if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                        Slog.d("ActivityManager", "Suppressing startForeground() for FAS " + ((Object) serviceRecord));
                        z6 = z16;
                    }
                }
                if (z6) {
                    synchronized (this.mAm.mProcessStats.mLock) {
                        ServiceState tracker4 = serviceRecord.getTracker();
                        if (tracker4 != null) {
                            tracker4.setForeground(z15, this.mAm.mProcessStats.getMemFactorLocked(), SystemClock.uptimeMillis());
                        }
                    }
                }
                if (z) {
                    return;
                }
                AppOpsService appOpsService4 = this.mAm.mAppOpsService;
                appOpsService4.finishOperation(AppOpsManager.getToken(appOpsService4), 76, serviceRecord.appInfo.uid, serviceRecord.packageName, null);
                return;
            }
            if (checkOpNoThrow != 3) {
                throw new SecurityException("Foreground not allowed as per app op");
            }
        }
        z2 = false;
        if (z2) {
        }
        z3 = z2;
        boolean isBgFgsRestrictionEnabled2 = isBgFgsRestrictionEnabled(serviceRecord);
        if (z3) {
        }
        if (z4) {
        }
        if (z6) {
        }
        if (z) {
        }
    }

    private boolean withinFgsDeferRateLimit(ServiceRecord serviceRecord, long j) {
        if (j < serviceRecord.fgDisplayTime) {
            if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                Slog.d(TAG_SERVICE, "FGS transition for " + serviceRecord + " within deferral period, no rate limit applied");
            }
            return false;
        }
        int i = serviceRecord.appInfo.uid;
        long j2 = this.mFgsDeferralEligible.get(i, 0L);
        if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE && j < j2) {
            Slog.d(TAG_SERVICE, "FGS transition for uid " + i + " within rate limit, showing immediately");
        }
        return j < j2;
    }

    private Pair<Integer, RuntimeException> validateForegroundServiceType(ServiceRecord serviceRecord, int i, int i2, int i3) {
        Object obj;
        ForegroundServiceTypePolicy defaultPolicy = ForegroundServiceTypePolicy.getDefaultPolicy();
        ForegroundServiceTypePolicy.ForegroundServiceTypePolicyInfo foregroundServiceTypePolicyInfo = defaultPolicy.getForegroundServiceTypePolicyInfo(i, i2);
        Context context = this.mAm.mContext;
        String str = serviceRecord.packageName;
        ProcessRecord processRecord = serviceRecord.app;
        int checkForegroundServiceTypePolicy = defaultPolicy.checkForegroundServiceTypePolicy(context, str, processRecord.uid, processRecord.getPid(), serviceRecord.mAllowWhileInUsePermissionInFgs, foregroundServiceTypePolicyInfo);
        if (checkForegroundServiceTypePolicy == 2) {
            String str2 = "Starting FGS with type " + ServiceInfo.foregroundServiceTypeToLabel(i) + " code=" + checkForegroundServiceTypePolicy + " callerApp=" + serviceRecord.app + " targetSDK=" + serviceRecord.app.info.targetSdkVersion;
            Slog.wtfQuiet("ActivityManager", str2);
            Slog.w("ActivityManager", str2);
        } else {
            if (checkForegroundServiceTypePolicy != 3) {
                if (checkForegroundServiceTypePolicy == 4) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Starting FGS with type ");
                    sb.append(ServiceInfo.foregroundServiceTypeToLabel(i));
                    sb.append(" code=");
                    sb.append(checkForegroundServiceTypePolicy);
                    sb.append(" callerApp=");
                    sb.append(serviceRecord.app);
                    sb.append(" targetSDK=");
                    sb.append(serviceRecord.app.info.targetSdkVersion);
                    sb.append(" requiredPermissions=");
                    sb.append(foregroundServiceTypePolicyInfo.toPermissionString());
                    sb.append(foregroundServiceTypePolicyInfo.hasForegroundOnlyPermission() ? " and the app must be in the eligible state/exemptions to access the foreground only permission" : "");
                    String sb2 = sb.toString();
                    Slog.wtfQuiet("ActivityManager", sb2);
                    Slog.w("ActivityManager", sb2);
                } else if (checkForegroundServiceTypePolicy == 5) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Starting FGS with type ");
                    sb3.append(ServiceInfo.foregroundServiceTypeToLabel(i));
                    sb3.append(" callerApp=");
                    sb3.append(serviceRecord.app);
                    sb3.append(" targetSDK=");
                    sb3.append(serviceRecord.app.info.targetSdkVersion);
                    sb3.append(" requires permissions: ");
                    sb3.append(foregroundServiceTypePolicyInfo.toPermissionString());
                    sb3.append(foregroundServiceTypePolicyInfo.hasForegroundOnlyPermission() ? " and the app must be in the eligible state/exemptions to access the foreground only permission" : "");
                    obj = new SecurityException(sb3.toString());
                }
            } else if (i3 == -1 && i == 0) {
                obj = new MissingForegroundServiceTypeException("Starting FGS without a type  callerApp=" + serviceRecord.app + " targetSDK=" + serviceRecord.app.info.targetSdkVersion);
            } else {
                obj = new InvalidForegroundServiceTypeException("Starting FGS with type " + ServiceInfo.foregroundServiceTypeToLabel(i) + " callerApp=" + serviceRecord.app + " targetSDK=" + serviceRecord.app.info.targetSdkVersion + " has been prohibited");
            }
            return Pair.create(Integer.valueOf(checkForegroundServiceTypePolicy), obj);
        }
        obj = null;
        return Pair.create(Integer.valueOf(checkForegroundServiceTypePolicy), obj);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class SystemExemptedFgsTypePermission extends ForegroundServiceTypePolicy.ForegroundServiceTypePermission {
        SystemExemptedFgsTypePermission() {
            super("System exempted");
        }

        public int checkPermission(Context context, int i, int i2, String str, boolean z) {
            AppRestrictionController appRestrictionController = ActiveServices.this.mAm.mAppRestrictionController;
            int potentialSystemExemptionReason = appRestrictionController.getPotentialSystemExemptionReason(i);
            if (potentialSystemExemptionReason == -1 && (potentialSystemExemptionReason = appRestrictionController.getPotentialSystemExemptionReason(i, str)) == -1) {
                potentialSystemExemptionReason = appRestrictionController.getPotentialUserAllowedExemptionReason(i, str);
            }
            if (potentialSystemExemptionReason == -1 && ArrayUtils.contains(ActiveServices.this.mAm.getPackageManagerInternal().getKnownPackageNames(2, 0), str)) {
                potentialSystemExemptionReason = 326;
            }
            if (potentialSystemExemptionReason != 10 && potentialSystemExemptionReason != 11 && potentialSystemExemptionReason != 51 && potentialSystemExemptionReason != 63 && potentialSystemExemptionReason != 65 && potentialSystemExemptionReason != 300 && potentialSystemExemptionReason != 55 && potentialSystemExemptionReason != 56 && potentialSystemExemptionReason != 326 && potentialSystemExemptionReason != 327) {
                switch (potentialSystemExemptionReason) {
                    case 319:
                    case 320:
                    case 321:
                    case 322:
                    case 323:
                    case FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_ACTIVE_DEVICE_ADMIN /* 324 */:
                        break;
                    default:
                        return -1;
                }
            }
            return 0;
        }
    }

    private void initSystemExemptedFgsTypePermission() {
        ForegroundServiceTypePolicy.ForegroundServiceTypePolicyInfo foregroundServiceTypePolicyInfo = ForegroundServiceTypePolicy.getDefaultPolicy().getForegroundServiceTypePolicyInfo(1024, 0);
        if (foregroundServiceTypePolicyInfo != null) {
            foregroundServiceTypePolicyInfo.setCustomPermission(new SystemExemptedFgsTypePermission());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class MediaProjectionFgsTypeCustomPermission extends ForegroundServiceTypePolicy.ForegroundServiceTypePermission {
        MediaProjectionFgsTypeCustomPermission() {
            super("Media projection screen capture permission");
        }

        public int checkPermission(Context context, int i, int i2, String str, boolean z) {
            return ActiveServices.this.mAm.isAllowedMediaProjectionNoOpCheck(i) ? 0 : -1;
        }
    }

    private void initMediaProjectFgsTypeCustomPermission() {
        ForegroundServiceTypePolicy.ForegroundServiceTypePolicyInfo foregroundServiceTypePolicyInfo = ForegroundServiceTypePolicy.getDefaultPolicy().getForegroundServiceTypePolicyInfo(32, 0);
        if (foregroundServiceTypePolicyInfo != null) {
            foregroundServiceTypePolicyInfo.setCustomPermission(new MediaProjectionFgsTypeCustomPermission());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityManagerInternal.ServiceNotificationPolicy applyForegroundServiceNotificationLocked(Notification notification, String str, int i, String str2, int i2) {
        if (str != null) {
            return ActivityManagerInternal.ServiceNotificationPolicy.NOT_FOREGROUND_SERVICE;
        }
        if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
            Slog.d(TAG_SERVICE, "Evaluating FGS policy for id=" + i + " pkg=" + str2 + " not=" + notification);
        }
        ServiceMap serviceMap = this.mServiceMap.get(i2);
        if (serviceMap == null) {
            return ActivityManagerInternal.ServiceNotificationPolicy.NOT_FOREGROUND_SERVICE;
        }
        for (int i3 = 0; i3 < serviceMap.mServicesByInstanceName.size(); i3++) {
            ServiceRecord valueAt = serviceMap.mServicesByInstanceName.valueAt(i3);
            if (valueAt.isForeground && i == valueAt.foregroundId && str2.equals(valueAt.appInfo.packageName)) {
                if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                    Slog.d(TAG_SERVICE, "   FOUND: notification is for " + valueAt);
                }
                notification.flags |= 64;
                valueAt.foregroundNoti = notification;
                if (shouldShowFgsNotificationLocked(valueAt)) {
                    if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                        Slog.d(TAG_SERVICE, "   Showing immediately due to policy");
                    }
                    valueAt.mFgsNotificationDeferred = false;
                    return ActivityManagerInternal.ServiceNotificationPolicy.SHOW_IMMEDIATELY;
                }
                if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                    Slog.d(TAG_SERVICE, "   Deferring / update-only");
                }
                startFgsDeferralTimerLocked(valueAt);
                return ActivityManagerInternal.ServiceNotificationPolicy.UPDATE_ONLY;
            }
        }
        return ActivityManagerInternal.ServiceNotificationPolicy.NOT_FOREGROUND_SERVICE;
    }

    private boolean shouldShowFgsNotificationLocked(ServiceRecord serviceRecord) {
        long uptimeMillis = SystemClock.uptimeMillis();
        if (!this.mAm.mConstants.mFlagFgsNotificationDeferralEnabled) {
            return true;
        }
        if (serviceRecord.mFgsNotificationDeferred && uptimeMillis >= serviceRecord.fgDisplayTime) {
            if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                Slog.d("ActivityManager", "FGS reached end of deferral period: " + serviceRecord);
            }
            return true;
        }
        if (withinFgsDeferRateLimit(serviceRecord, uptimeMillis)) {
            return true;
        }
        if (this.mAm.mConstants.mFlagFgsNotificationDeferralApiGated) {
            if (serviceRecord.appInfo.targetSdkVersion < 31) {
                return true;
            }
        }
        if (serviceRecord.mFgsNotificationShown) {
            return true;
        }
        if (!serviceRecord.foregroundNoti.isForegroundDisplayForceDeferred()) {
            if (serviceRecord.foregroundNoti.shouldShowForegroundImmediately()) {
                if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                    Slog.d(TAG_SERVICE, "FGS " + serviceRecord + " notification policy says show immediately");
                }
                return true;
            }
            if ((serviceRecord.foregroundServiceType & 54) != 0) {
                if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                    Slog.d(TAG_SERVICE, "FGS " + serviceRecord + " type gets immediate display");
                }
                return true;
            }
        } else if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
            Slog.d(TAG_SERVICE, "FGS " + serviceRecord + " notification is app deferred");
        }
        return false;
    }

    private void startFgsDeferralTimerLocked(ServiceRecord serviceRecord) {
        long uptimeMillis = SystemClock.uptimeMillis();
        int i = serviceRecord.appInfo.uid;
        long j = (serviceRecord.isShortFgs() ? this.mAm.mConstants.mFgsNotificationDeferralIntervalForShort : this.mAm.mConstants.mFgsNotificationDeferralInterval) + uptimeMillis;
        for (int i2 = 0; i2 < this.mPendingFgsNotifications.size(); i2++) {
            ServiceRecord serviceRecord2 = this.mPendingFgsNotifications.get(i2);
            if (serviceRecord2 == serviceRecord) {
                if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                    Slog.d(TAG_SERVICE, "FGS " + serviceRecord + " already pending notification display");
                    return;
                }
                return;
            }
            if (i == serviceRecord2.appInfo.uid) {
                j = Math.min(j, serviceRecord2.fgDisplayTime);
            }
        }
        if (this.mFgsDeferralRateLimited) {
            this.mFgsDeferralEligible.put(i, (serviceRecord.isShortFgs() ? this.mAm.mConstants.mFgsNotificationDeferralExclusionTimeForShort : this.mAm.mConstants.mFgsNotificationDeferralExclusionTime) + j);
        }
        serviceRecord.fgDisplayTime = j;
        serviceRecord.mFgsNotificationDeferred = true;
        serviceRecord.mFgsNotificationWasDeferred = true;
        serviceRecord.mFgsNotificationShown = false;
        this.mPendingFgsNotifications.add(serviceRecord);
        if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
            Slog.d(TAG_SERVICE, "FGS " + serviceRecord + " notification in " + (j - uptimeMillis) + " ms");
        }
        if (serviceRecord.appInfo.targetSdkVersion < 31) {
            Slog.i(TAG_SERVICE, "Deferring FGS notification in legacy app " + serviceRecord.appInfo.packageName + "/" + UserHandle.formatUid(serviceRecord.appInfo.uid) + " : " + serviceRecord.foregroundNoti);
        }
        this.mAm.mHandler.postAtTime(this.mPostDeferredFGSNotifications, j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean enableFgsNotificationRateLimitLocked(boolean z) {
        if (z != this.mFgsDeferralRateLimited) {
            this.mFgsDeferralRateLimited = z;
            if (!z) {
                this.mFgsDeferralEligible.clear();
            }
        }
        return z;
    }

    private void removeServiceNotificationDeferralsLocked(String str, int i) {
        for (int size = this.mPendingFgsNotifications.size() - 1; size >= 0; size--) {
            ServiceRecord serviceRecord = this.mPendingFgsNotifications.get(size);
            if (i == serviceRecord.userId && serviceRecord.appInfo.packageName.equals(str)) {
                this.mPendingFgsNotifications.remove(size);
                if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                    Slog.d(TAG_SERVICE, "Removing notification deferral for " + serviceRecord);
                }
            }
        }
    }

    public void onForegroundServiceNotificationUpdateLocked(boolean z, Notification notification, int i, String str, int i2) {
        int i3;
        int size = this.mPendingFgsNotifications.size() - 1;
        while (true) {
            if (size < 0) {
                break;
            }
            ServiceRecord serviceRecord = this.mPendingFgsNotifications.get(size);
            if (i2 == serviceRecord.userId && i == serviceRecord.foregroundId && serviceRecord.appInfo.packageName.equals(str)) {
                if (z) {
                    if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                        Slog.d(TAG_SERVICE, "Notification shown; canceling deferral of " + serviceRecord);
                    }
                    serviceRecord.mFgsNotificationShown = true;
                    serviceRecord.mFgsNotificationDeferred = false;
                    this.mPendingFgsNotifications.remove(size);
                } else if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                    Slog.d(TAG_SERVICE, "FGS notification deferred for " + serviceRecord);
                }
            }
            size--;
        }
        ServiceMap serviceMap = this.mServiceMap.get(i2);
        if (serviceMap != null) {
            for (i3 = 0; i3 < serviceMap.mServicesByInstanceName.size(); i3++) {
                ServiceRecord valueAt = serviceMap.mServicesByInstanceName.valueAt(i3);
                if (valueAt.isForeground && i == valueAt.foregroundId && valueAt.appInfo.packageName.equals(str)) {
                    if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                        Slog.d(TAG_SERVICE, "Recording shown notification for " + valueAt);
                    }
                    valueAt.foregroundNoti = notification;
                }
            }
        }
    }

    private void registerAppOpCallbackLocked(ServiceRecord serviceRecord) {
        if (serviceRecord.app == null) {
            return;
        }
        int i = serviceRecord.appInfo.uid;
        AppOpCallback appOpCallback = this.mFgsAppOpCallbacks.get(i);
        if (appOpCallback == null) {
            appOpCallback = new AppOpCallback(serviceRecord.app, this.mAm.getAppOpsManager());
            this.mFgsAppOpCallbacks.put(i, appOpCallback);
        }
        appOpCallback.registerLocked();
    }

    private void unregisterAppOpCallbackLocked(ServiceRecord serviceRecord) {
        int i = serviceRecord.appInfo.uid;
        AppOpCallback appOpCallback = this.mFgsAppOpCallbacks.get(i);
        if (appOpCallback != null) {
            appOpCallback.unregisterLocked();
            if (appOpCallback.isObsoleteLocked()) {
                this.mFgsAppOpCallbacks.remove(i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class AppOpCallback {
        private static final int[] LOGGED_AP_OPS = {0, 1, 27, 26};
        private final AppOpsManager mAppOpsManager;
        private final ProcessRecord mProcessRecord;

        @GuardedBy({"mCounterLock"})
        private final SparseIntArray mAcceptedOps = new SparseIntArray();

        @GuardedBy({"mCounterLock"})
        private final SparseIntArray mRejectedOps = new SparseIntArray();
        private final Object mCounterLock = new Object();
        private final SparseIntArray mAppOpModes = new SparseIntArray();

        @GuardedBy({"mAm"})
        private int mNumFgs = 0;

        @GuardedBy({"mAm"})
        private boolean mDestroyed = false;
        private final AppOpsManager.OnOpNotedInternalListener mOpNotedCallback = new AppOpsManager.OnOpNotedInternalListener() { // from class: com.android.server.am.ActiveServices.AppOpCallback.1
            public void onOpNoted(int i, int i2, String str, String str2, int i3, int i4) {
                AppOpCallback.this.incrementOpCountIfNeeded(i, i2, i4);
            }
        };
        private final AppOpsManager.OnOpStartedListener mOpStartedCallback = new AppOpsManager.OnOpStartedListener() { // from class: com.android.server.am.ActiveServices.AppOpCallback.2
            public void onOpStarted(int i, int i2, String str, String str2, int i3, int i4) {
                AppOpCallback.this.incrementOpCountIfNeeded(i, i2, i4);
            }
        };

        private static int modeToEnum(int i) {
            if (i == 0) {
                return 1;
            }
            if (i != 1) {
                return i != 4 ? 0 : 3;
            }
            return 2;
        }

        AppOpCallback(ProcessRecord processRecord, AppOpsManager appOpsManager) {
            this.mProcessRecord = processRecord;
            this.mAppOpsManager = appOpsManager;
            for (int i : LOGGED_AP_OPS) {
                this.mAppOpModes.put(i, appOpsManager.unsafeCheckOpRawNoThrow(i, processRecord.uid, processRecord.info.packageName));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void incrementOpCountIfNeeded(int i, int i2, int i3) {
            if (i2 == this.mProcessRecord.uid && isNotTop()) {
                incrementOpCount(i, i3 == 0);
            }
        }

        private boolean isNotTop() {
            return this.mProcessRecord.mState.getCurProcState() != 2;
        }

        private void incrementOpCount(int i, boolean z) {
            synchronized (this.mCounterLock) {
                SparseIntArray sparseIntArray = z ? this.mAcceptedOps : this.mRejectedOps;
                int indexOfKey = sparseIntArray.indexOfKey(i);
                if (indexOfKey < 0) {
                    sparseIntArray.put(i, 1);
                } else {
                    sparseIntArray.setValueAt(indexOfKey, sparseIntArray.valueAt(indexOfKey) + 1);
                }
            }
        }

        void registerLocked() {
            if (isObsoleteLocked()) {
                Slog.wtf("ActivityManager", "Trying to register on a stale AppOpCallback.");
                return;
            }
            int i = this.mNumFgs + 1;
            this.mNumFgs = i;
            if (i == 1) {
                AppOpsManager appOpsManager = this.mAppOpsManager;
                int[] iArr = LOGGED_AP_OPS;
                appOpsManager.startWatchingNoted(iArr, this.mOpNotedCallback);
                this.mAppOpsManager.startWatchingStarted(iArr, this.mOpStartedCallback);
            }
        }

        void unregisterLocked() {
            int i = this.mNumFgs - 1;
            this.mNumFgs = i;
            if (i <= 0) {
                this.mDestroyed = true;
                logFinalValues();
                this.mAppOpsManager.stopWatchingNoted(this.mOpNotedCallback);
                this.mAppOpsManager.stopWatchingStarted(this.mOpStartedCallback);
            }
        }

        boolean isObsoleteLocked() {
            return this.mDestroyed;
        }

        private void logFinalValues() {
            synchronized (this.mCounterLock) {
                for (int i : LOGGED_AP_OPS) {
                    int i2 = this.mAcceptedOps.get(i);
                    int i3 = this.mRejectedOps.get(i);
                    if (i2 > 0 || i3 > 0) {
                        FrameworkStatsLog.write(256, this.mProcessRecord.uid, i, modeToEnum(this.mAppOpModes.get(i)), i2, i3);
                    }
                }
            }
        }
    }

    private void cancelForegroundNotificationLocked(ServiceRecord serviceRecord) {
        if (serviceRecord.foregroundNoti != null) {
            ServiceMap serviceMapLocked = getServiceMapLocked(serviceRecord.userId);
            if (serviceMapLocked != null) {
                for (int size = serviceMapLocked.mServicesByInstanceName.size() - 1; size >= 0; size--) {
                    ServiceRecord valueAt = serviceMapLocked.mServicesByInstanceName.valueAt(size);
                    if (valueAt != serviceRecord && valueAt.isForeground && valueAt.foregroundId == serviceRecord.foregroundId && valueAt.packageName.equals(serviceRecord.packageName)) {
                        if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                            Slog.i(TAG_SERVICE, "FGS notification for " + serviceRecord + " shared by " + valueAt + " (isForeground=" + valueAt.isForeground + ") - NOT cancelling");
                            return;
                        }
                        return;
                    }
                }
            }
            serviceRecord.cancelNotification();
        }
    }

    private void updateServiceForegroundLocked(ProcessServiceRecord processServiceRecord, boolean z) {
        int i = 0;
        boolean z2 = false;
        boolean z3 = false;
        for (int numberOfRunningServices = processServiceRecord.numberOfRunningServices() - 1; numberOfRunningServices >= 0; numberOfRunningServices--) {
            ServiceRecord runningServiceAt = processServiceRecord.getRunningServiceAt(numberOfRunningServices);
            if (runningServiceAt.isForeground || runningServiceAt.fgRequired) {
                int i2 = runningServiceAt.foregroundServiceType;
                int i3 = i | i2;
                if (i2 == 0) {
                    z2 = true;
                    z3 = true;
                } else {
                    z3 = true;
                }
                i = i3;
            }
        }
        this.mAm.updateProcessForegroundLocked(processServiceRecord.mApp, z3, i, z2, z);
        processServiceRecord.setHasReportedForegroundServices(z3);
    }

    void unscheduleShortFgsTimeoutLocked(ServiceRecord serviceRecord) {
        this.mAm.mHandler.removeMessages(78, serviceRecord);
        this.mAm.mHandler.removeMessages(77, serviceRecord);
        this.mAm.mHandler.removeMessages(76, serviceRecord);
    }

    private void maybeUpdateShortFgsTrackingLocked(ServiceRecord serviceRecord, boolean z) {
        if (!serviceRecord.isShortFgs()) {
            serviceRecord.clearShortFgsInfo();
            unscheduleShortFgsTimeoutLocked(serviceRecord);
            return;
        }
        boolean hasShortFgsInfo = serviceRecord.hasShortFgsInfo();
        if (z || !hasShortFgsInfo) {
            if (DEBUG_SHORT_SERVICE) {
                if (hasShortFgsInfo) {
                    Slog.i(TAG_SERVICE, "Extending SHORT_SERVICE time out: " + serviceRecord);
                } else {
                    Slog.i(TAG_SERVICE, "Short FGS started: " + serviceRecord);
                }
            }
            traceInstant("short FGS start/extend: ", serviceRecord);
            serviceRecord.setShortFgsInfo(SystemClock.uptimeMillis());
            unscheduleShortFgsTimeoutLocked(serviceRecord);
            this.mAm.mHandler.sendMessageAtTime(this.mAm.mHandler.obtainMessage(76, serviceRecord), serviceRecord.getShortFgsInfo().getTimeoutTime());
            return;
        }
        if (DEBUG_SHORT_SERVICE) {
            Slog.w(TAG_SERVICE, "NOT extending SHORT_SERVICE time out: " + serviceRecord);
        }
        serviceRecord.getShortFgsInfo().update();
    }

    private void maybeStopShortFgsTimeoutLocked(ServiceRecord serviceRecord) {
        serviceRecord.clearShortFgsInfo();
        if (serviceRecord.isShortFgs()) {
            if (DEBUG_SHORT_SERVICE) {
                Slog.i(TAG_SERVICE, "Stop short FGS timeout: " + serviceRecord);
            }
            unscheduleShortFgsTimeoutLocked(serviceRecord);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onShortFgsTimeout(ServiceRecord serviceRecord) {
        ActivityManagerService activityManagerService = this.mAm;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                long uptimeMillis = SystemClock.uptimeMillis();
                if (!serviceRecord.shouldTriggerShortFgsTimeout(uptimeMillis)) {
                    if (DEBUG_SHORT_SERVICE) {
                        Slog.d(TAG_SERVICE, "[STALE] Short FGS timed out: " + serviceRecord + " " + serviceRecord.getShortFgsTimedEventDescription(uptimeMillis));
                    }
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                Slog.e(TAG_SERVICE, "Short FGS timed out: " + serviceRecord);
                traceInstant("short FGS timeout: ", serviceRecord);
                long j = serviceRecord.mFgsEnterTime;
                logFGSStateChangeLocked(serviceRecord, 5, uptimeMillis > j ? (int) (uptimeMillis - j) : 0, 0, 0);
                try {
                    serviceRecord.app.getThread().scheduleTimeoutService(serviceRecord, serviceRecord.getShortFgsInfo().getStartId());
                } catch (RemoteException e) {
                    Slog.w(TAG_SERVICE, "Exception from scheduleTimeoutService: " + e.toString());
                }
                this.mAm.mHandler.sendMessageAtTime(this.mAm.mHandler.obtainMessage(77, serviceRecord), serviceRecord.getShortFgsInfo().getProcStateDemoteTime());
                this.mAm.mHandler.sendMessageAtTime(this.mAm.mHandler.obtainMessage(78, serviceRecord), serviceRecord.getShortFgsInfo().getAnrTime());
                ActivityManagerService.resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldServiceTimeOutLocked(ComponentName componentName, IBinder iBinder) {
        int callingUserId = UserHandle.getCallingUserId();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ServiceRecord findServiceLocked = findServiceLocked(componentName, iBinder, callingUserId);
            if (findServiceLocked != null) {
                return findServiceLocked.shouldTriggerShortFgsTimeout(SystemClock.uptimeMillis());
            }
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return false;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onShortFgsProcstateTimeout(ServiceRecord serviceRecord) {
        ActivityManagerService activityManagerService = this.mAm;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                long uptimeMillis = SystemClock.uptimeMillis();
                if (!serviceRecord.shouldDemoteShortFgsProcState(uptimeMillis)) {
                    if (DEBUG_SHORT_SERVICE) {
                        Slog.d(TAG_SERVICE, "[STALE] Short FGS procstate demotion: " + serviceRecord + " " + serviceRecord.getShortFgsTimedEventDescription(uptimeMillis));
                    }
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                Slog.e(TAG_SERVICE, "Short FGS procstate demoted: " + serviceRecord);
                traceInstant("short FGS demote: ", serviceRecord);
                this.mAm.updateOomAdjLocked(serviceRecord.app, 13);
                ActivityManagerService.resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onShortFgsAnrTimeout(ServiceRecord serviceRecord) {
        TimeoutRecord forShortFgsTimeout = TimeoutRecord.forShortFgsTimeout("A foreground service of FOREGROUND_SERVICE_TYPE_SHORT_SERVICE did not stop within a timeout: " + serviceRecord.getComponentName());
        forShortFgsTimeout.mLatencyTracker.waitingOnAMSLockStarted();
        ActivityManagerService activityManagerService = this.mAm;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                forShortFgsTimeout.mLatencyTracker.waitingOnAMSLockEnded();
                long uptimeMillis = SystemClock.uptimeMillis();
                if (!serviceRecord.shouldTriggerShortFgsAnr(uptimeMillis)) {
                    if (DEBUG_SHORT_SERVICE) {
                        Slog.d(TAG_SERVICE, "[STALE] Short FGS ANR'ed: " + serviceRecord + " " + serviceRecord.getShortFgsTimedEventDescription(uptimeMillis));
                    }
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                String str = "Short FGS ANR'ed: " + serviceRecord;
                if (DEBUG_SHORT_SERVICE) {
                    Slog.wtf(TAG_SERVICE, str);
                } else {
                    Slog.e(TAG_SERVICE, str);
                }
                traceInstant("short FGS ANR: ", serviceRecord);
                this.mAm.appNotResponding(serviceRecord.app, forShortFgsTimeout);
                ActivityManagerService.resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private void updateAllowlistManagerLocked(ProcessServiceRecord processServiceRecord) {
        processServiceRecord.mAllowlistManager = false;
        for (int numberOfRunningServices = processServiceRecord.numberOfRunningServices() - 1; numberOfRunningServices >= 0; numberOfRunningServices--) {
            if (processServiceRecord.getRunningServiceAt(numberOfRunningServices).allowlistManager) {
                processServiceRecord.mAllowlistManager = true;
                return;
            }
        }
    }

    private void stopServiceAndUpdateAllowlistManagerLocked(ServiceRecord serviceRecord) {
        maybeStopShortFgsTimeoutLocked(serviceRecord);
        ProcessServiceRecord processServiceRecord = serviceRecord.app.mServices;
        processServiceRecord.stopService(serviceRecord);
        processServiceRecord.updateBoundClientUids();
        if (serviceRecord.allowlistManager) {
            updateAllowlistManagerLocked(processServiceRecord);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateServiceConnectionActivitiesLocked(ProcessServiceRecord processServiceRecord) {
        ArraySet arraySet = null;
        for (int i = 0; i < processServiceRecord.numberOfConnections(); i++) {
            ProcessRecord processRecord = processServiceRecord.getConnectionAt(i).binding.service.app;
            if (processRecord != null && processRecord != processServiceRecord.mApp) {
                if (arraySet == null) {
                    arraySet = new ArraySet();
                } else if (arraySet.contains(processRecord)) {
                }
                arraySet.add(processRecord);
                updateServiceClientActivitiesLocked(processRecord.mServices, null, false);
            }
        }
    }

    private boolean updateServiceClientActivitiesLocked(ProcessServiceRecord processServiceRecord, ConnectionRecord connectionRecord, boolean z) {
        ProcessRecord processRecord;
        if (connectionRecord != null && (processRecord = connectionRecord.binding.client) != null && !processRecord.hasActivities()) {
            return false;
        }
        boolean z2 = false;
        for (int numberOfRunningServices = processServiceRecord.numberOfRunningServices() - 1; numberOfRunningServices >= 0 && !z2; numberOfRunningServices--) {
            ArrayMap<IBinder, ArrayList<ConnectionRecord>> connections = processServiceRecord.getRunningServiceAt(numberOfRunningServices).getConnections();
            for (int size = connections.size() - 1; size >= 0 && !z2; size--) {
                ArrayList<ConnectionRecord> valueAt = connections.valueAt(size);
                int size2 = valueAt.size() - 1;
                while (true) {
                    if (size2 < 0) {
                        break;
                    }
                    ProcessRecord processRecord2 = valueAt.get(size2).binding.client;
                    if (processRecord2 != null && processRecord2 != processServiceRecord.mApp && processRecord2.hasActivities()) {
                        z2 = true;
                        break;
                    }
                    size2--;
                }
            }
        }
        if (z2 == processServiceRecord.hasClientActivities()) {
            return false;
        }
        processServiceRecord.setHasClientActivities(z2);
        if (z) {
            this.mAm.updateLruProcessLocked(processServiceRecord.mApp, z2, null);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0397 A[Catch: all -> 0x0721, TryCatch #0 {all -> 0x0721, blocks: (B:104:0x032d, B:106:0x033b, B:108:0x033f, B:109:0x0355, B:111:0x035d, B:113:0x0369, B:114:0x036f, B:121:0x038e, B:123:0x0397, B:124:0x03a0, B:127:0x03ae, B:130:0x03bf, B:132:0x0414, B:134:0x0446, B:136:0x0464, B:138:0x0497, B:139:0x049a, B:140:0x04af, B:142:0x04c7, B:143:0x04cd, B:145:0x04d5, B:146:0x04d7, B:148:0x04df, B:149:0x04e2, B:151:0x04eb, B:152:0x04ed, B:154:0x04f2, B:156:0x04f6, B:158:0x04fc, B:160:0x0504, B:161:0x050a, B:163:0x050e, B:166:0x0517, B:167:0x0524, B:169:0x0527, B:171:0x0534, B:173:0x0556, B:177:0x0565, B:179:0x0569, B:182:0x0578, B:184:0x059a, B:186:0x05a4, B:187:0x05a7, B:189:0x05ab, B:190:0x05ad, B:192:0x05b7, B:197:0x05d4, B:199:0x05e6, B:202:0x05f2, B:204:0x0607, B:211:0x0619, B:213:0x0641, B:214:0x0687, B:216:0x068b, B:218:0x0691, B:222:0x069a, B:233:0x06a3, B:223:0x06dc, B:225:0x06e6, B:227:0x06ec, B:228:0x06fd, B:234:0x0698, B:235:0x06f2, B:237:0x06fa, B:241:0x05c1, B:243:0x05cb, B:259:0x038c, B:116:0x0370, B:118:0x0376, B:119:0x0388), top: B:103:0x032d, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:126:0x03a9  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x03ba  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0414 A[Catch: all -> 0x0721, TryCatch #0 {all -> 0x0721, blocks: (B:104:0x032d, B:106:0x033b, B:108:0x033f, B:109:0x0355, B:111:0x035d, B:113:0x0369, B:114:0x036f, B:121:0x038e, B:123:0x0397, B:124:0x03a0, B:127:0x03ae, B:130:0x03bf, B:132:0x0414, B:134:0x0446, B:136:0x0464, B:138:0x0497, B:139:0x049a, B:140:0x04af, B:142:0x04c7, B:143:0x04cd, B:145:0x04d5, B:146:0x04d7, B:148:0x04df, B:149:0x04e2, B:151:0x04eb, B:152:0x04ed, B:154:0x04f2, B:156:0x04f6, B:158:0x04fc, B:160:0x0504, B:161:0x050a, B:163:0x050e, B:166:0x0517, B:167:0x0524, B:169:0x0527, B:171:0x0534, B:173:0x0556, B:177:0x0565, B:179:0x0569, B:182:0x0578, B:184:0x059a, B:186:0x05a4, B:187:0x05a7, B:189:0x05ab, B:190:0x05ad, B:192:0x05b7, B:197:0x05d4, B:199:0x05e6, B:202:0x05f2, B:204:0x0607, B:211:0x0619, B:213:0x0641, B:214:0x0687, B:216:0x068b, B:218:0x0691, B:222:0x069a, B:233:0x06a3, B:223:0x06dc, B:225:0x06e6, B:227:0x06ec, B:228:0x06fd, B:234:0x0698, B:235:0x06f2, B:237:0x06fa, B:241:0x05c1, B:243:0x05cb, B:259:0x038c, B:116:0x0370, B:118:0x0376, B:119:0x0388), top: B:103:0x032d, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:136:0x0464 A[Catch: all -> 0x0721, TryCatch #0 {all -> 0x0721, blocks: (B:104:0x032d, B:106:0x033b, B:108:0x033f, B:109:0x0355, B:111:0x035d, B:113:0x0369, B:114:0x036f, B:121:0x038e, B:123:0x0397, B:124:0x03a0, B:127:0x03ae, B:130:0x03bf, B:132:0x0414, B:134:0x0446, B:136:0x0464, B:138:0x0497, B:139:0x049a, B:140:0x04af, B:142:0x04c7, B:143:0x04cd, B:145:0x04d5, B:146:0x04d7, B:148:0x04df, B:149:0x04e2, B:151:0x04eb, B:152:0x04ed, B:154:0x04f2, B:156:0x04f6, B:158:0x04fc, B:160:0x0504, B:161:0x050a, B:163:0x050e, B:166:0x0517, B:167:0x0524, B:169:0x0527, B:171:0x0534, B:173:0x0556, B:177:0x0565, B:179:0x0569, B:182:0x0578, B:184:0x059a, B:186:0x05a4, B:187:0x05a7, B:189:0x05ab, B:190:0x05ad, B:192:0x05b7, B:197:0x05d4, B:199:0x05e6, B:202:0x05f2, B:204:0x0607, B:211:0x0619, B:213:0x0641, B:214:0x0687, B:216:0x068b, B:218:0x0691, B:222:0x069a, B:233:0x06a3, B:223:0x06dc, B:225:0x06e6, B:227:0x06ec, B:228:0x06fd, B:234:0x0698, B:235:0x06f2, B:237:0x06fa, B:241:0x05c1, B:243:0x05cb, B:259:0x038c, B:116:0x0370, B:118:0x0376, B:119:0x0388), top: B:103:0x032d, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:142:0x04c7 A[Catch: all -> 0x0721, TryCatch #0 {all -> 0x0721, blocks: (B:104:0x032d, B:106:0x033b, B:108:0x033f, B:109:0x0355, B:111:0x035d, B:113:0x0369, B:114:0x036f, B:121:0x038e, B:123:0x0397, B:124:0x03a0, B:127:0x03ae, B:130:0x03bf, B:132:0x0414, B:134:0x0446, B:136:0x0464, B:138:0x0497, B:139:0x049a, B:140:0x04af, B:142:0x04c7, B:143:0x04cd, B:145:0x04d5, B:146:0x04d7, B:148:0x04df, B:149:0x04e2, B:151:0x04eb, B:152:0x04ed, B:154:0x04f2, B:156:0x04f6, B:158:0x04fc, B:160:0x0504, B:161:0x050a, B:163:0x050e, B:166:0x0517, B:167:0x0524, B:169:0x0527, B:171:0x0534, B:173:0x0556, B:177:0x0565, B:179:0x0569, B:182:0x0578, B:184:0x059a, B:186:0x05a4, B:187:0x05a7, B:189:0x05ab, B:190:0x05ad, B:192:0x05b7, B:197:0x05d4, B:199:0x05e6, B:202:0x05f2, B:204:0x0607, B:211:0x0619, B:213:0x0641, B:214:0x0687, B:216:0x068b, B:218:0x0691, B:222:0x069a, B:233:0x06a3, B:223:0x06dc, B:225:0x06e6, B:227:0x06ec, B:228:0x06fd, B:234:0x0698, B:235:0x06f2, B:237:0x06fa, B:241:0x05c1, B:243:0x05cb, B:259:0x038c, B:116:0x0370, B:118:0x0376, B:119:0x0388), top: B:103:0x032d, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:145:0x04d5 A[Catch: all -> 0x0721, TryCatch #0 {all -> 0x0721, blocks: (B:104:0x032d, B:106:0x033b, B:108:0x033f, B:109:0x0355, B:111:0x035d, B:113:0x0369, B:114:0x036f, B:121:0x038e, B:123:0x0397, B:124:0x03a0, B:127:0x03ae, B:130:0x03bf, B:132:0x0414, B:134:0x0446, B:136:0x0464, B:138:0x0497, B:139:0x049a, B:140:0x04af, B:142:0x04c7, B:143:0x04cd, B:145:0x04d5, B:146:0x04d7, B:148:0x04df, B:149:0x04e2, B:151:0x04eb, B:152:0x04ed, B:154:0x04f2, B:156:0x04f6, B:158:0x04fc, B:160:0x0504, B:161:0x050a, B:163:0x050e, B:166:0x0517, B:167:0x0524, B:169:0x0527, B:171:0x0534, B:173:0x0556, B:177:0x0565, B:179:0x0569, B:182:0x0578, B:184:0x059a, B:186:0x05a4, B:187:0x05a7, B:189:0x05ab, B:190:0x05ad, B:192:0x05b7, B:197:0x05d4, B:199:0x05e6, B:202:0x05f2, B:204:0x0607, B:211:0x0619, B:213:0x0641, B:214:0x0687, B:216:0x068b, B:218:0x0691, B:222:0x069a, B:233:0x06a3, B:223:0x06dc, B:225:0x06e6, B:227:0x06ec, B:228:0x06fd, B:234:0x0698, B:235:0x06f2, B:237:0x06fa, B:241:0x05c1, B:243:0x05cb, B:259:0x038c, B:116:0x0370, B:118:0x0376, B:119:0x0388), top: B:103:0x032d, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:148:0x04df A[Catch: all -> 0x0721, TryCatch #0 {all -> 0x0721, blocks: (B:104:0x032d, B:106:0x033b, B:108:0x033f, B:109:0x0355, B:111:0x035d, B:113:0x0369, B:114:0x036f, B:121:0x038e, B:123:0x0397, B:124:0x03a0, B:127:0x03ae, B:130:0x03bf, B:132:0x0414, B:134:0x0446, B:136:0x0464, B:138:0x0497, B:139:0x049a, B:140:0x04af, B:142:0x04c7, B:143:0x04cd, B:145:0x04d5, B:146:0x04d7, B:148:0x04df, B:149:0x04e2, B:151:0x04eb, B:152:0x04ed, B:154:0x04f2, B:156:0x04f6, B:158:0x04fc, B:160:0x0504, B:161:0x050a, B:163:0x050e, B:166:0x0517, B:167:0x0524, B:169:0x0527, B:171:0x0534, B:173:0x0556, B:177:0x0565, B:179:0x0569, B:182:0x0578, B:184:0x059a, B:186:0x05a4, B:187:0x05a7, B:189:0x05ab, B:190:0x05ad, B:192:0x05b7, B:197:0x05d4, B:199:0x05e6, B:202:0x05f2, B:204:0x0607, B:211:0x0619, B:213:0x0641, B:214:0x0687, B:216:0x068b, B:218:0x0691, B:222:0x069a, B:233:0x06a3, B:223:0x06dc, B:225:0x06e6, B:227:0x06ec, B:228:0x06fd, B:234:0x0698, B:235:0x06f2, B:237:0x06fa, B:241:0x05c1, B:243:0x05cb, B:259:0x038c, B:116:0x0370, B:118:0x0376, B:119:0x0388), top: B:103:0x032d, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:151:0x04eb A[Catch: all -> 0x0721, TryCatch #0 {all -> 0x0721, blocks: (B:104:0x032d, B:106:0x033b, B:108:0x033f, B:109:0x0355, B:111:0x035d, B:113:0x0369, B:114:0x036f, B:121:0x038e, B:123:0x0397, B:124:0x03a0, B:127:0x03ae, B:130:0x03bf, B:132:0x0414, B:134:0x0446, B:136:0x0464, B:138:0x0497, B:139:0x049a, B:140:0x04af, B:142:0x04c7, B:143:0x04cd, B:145:0x04d5, B:146:0x04d7, B:148:0x04df, B:149:0x04e2, B:151:0x04eb, B:152:0x04ed, B:154:0x04f2, B:156:0x04f6, B:158:0x04fc, B:160:0x0504, B:161:0x050a, B:163:0x050e, B:166:0x0517, B:167:0x0524, B:169:0x0527, B:171:0x0534, B:173:0x0556, B:177:0x0565, B:179:0x0569, B:182:0x0578, B:184:0x059a, B:186:0x05a4, B:187:0x05a7, B:189:0x05ab, B:190:0x05ad, B:192:0x05b7, B:197:0x05d4, B:199:0x05e6, B:202:0x05f2, B:204:0x0607, B:211:0x0619, B:213:0x0641, B:214:0x0687, B:216:0x068b, B:218:0x0691, B:222:0x069a, B:233:0x06a3, B:223:0x06dc, B:225:0x06e6, B:227:0x06ec, B:228:0x06fd, B:234:0x0698, B:235:0x06f2, B:237:0x06fa, B:241:0x05c1, B:243:0x05cb, B:259:0x038c, B:116:0x0370, B:118:0x0376, B:119:0x0388), top: B:103:0x032d, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:154:0x04f2 A[Catch: all -> 0x0721, TryCatch #0 {all -> 0x0721, blocks: (B:104:0x032d, B:106:0x033b, B:108:0x033f, B:109:0x0355, B:111:0x035d, B:113:0x0369, B:114:0x036f, B:121:0x038e, B:123:0x0397, B:124:0x03a0, B:127:0x03ae, B:130:0x03bf, B:132:0x0414, B:134:0x0446, B:136:0x0464, B:138:0x0497, B:139:0x049a, B:140:0x04af, B:142:0x04c7, B:143:0x04cd, B:145:0x04d5, B:146:0x04d7, B:148:0x04df, B:149:0x04e2, B:151:0x04eb, B:152:0x04ed, B:154:0x04f2, B:156:0x04f6, B:158:0x04fc, B:160:0x0504, B:161:0x050a, B:163:0x050e, B:166:0x0517, B:167:0x0524, B:169:0x0527, B:171:0x0534, B:173:0x0556, B:177:0x0565, B:179:0x0569, B:182:0x0578, B:184:0x059a, B:186:0x05a4, B:187:0x05a7, B:189:0x05ab, B:190:0x05ad, B:192:0x05b7, B:197:0x05d4, B:199:0x05e6, B:202:0x05f2, B:204:0x0607, B:211:0x0619, B:213:0x0641, B:214:0x0687, B:216:0x068b, B:218:0x0691, B:222:0x069a, B:233:0x06a3, B:223:0x06dc, B:225:0x06e6, B:227:0x06ec, B:228:0x06fd, B:234:0x0698, B:235:0x06f2, B:237:0x06fa, B:241:0x05c1, B:243:0x05cb, B:259:0x038c, B:116:0x0370, B:118:0x0376, B:119:0x0388), top: B:103:0x032d, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:163:0x050e A[Catch: all -> 0x0721, TryCatch #0 {all -> 0x0721, blocks: (B:104:0x032d, B:106:0x033b, B:108:0x033f, B:109:0x0355, B:111:0x035d, B:113:0x0369, B:114:0x036f, B:121:0x038e, B:123:0x0397, B:124:0x03a0, B:127:0x03ae, B:130:0x03bf, B:132:0x0414, B:134:0x0446, B:136:0x0464, B:138:0x0497, B:139:0x049a, B:140:0x04af, B:142:0x04c7, B:143:0x04cd, B:145:0x04d5, B:146:0x04d7, B:148:0x04df, B:149:0x04e2, B:151:0x04eb, B:152:0x04ed, B:154:0x04f2, B:156:0x04f6, B:158:0x04fc, B:160:0x0504, B:161:0x050a, B:163:0x050e, B:166:0x0517, B:167:0x0524, B:169:0x0527, B:171:0x0534, B:173:0x0556, B:177:0x0565, B:179:0x0569, B:182:0x0578, B:184:0x059a, B:186:0x05a4, B:187:0x05a7, B:189:0x05ab, B:190:0x05ad, B:192:0x05b7, B:197:0x05d4, B:199:0x05e6, B:202:0x05f2, B:204:0x0607, B:211:0x0619, B:213:0x0641, B:214:0x0687, B:216:0x068b, B:218:0x0691, B:222:0x069a, B:233:0x06a3, B:223:0x06dc, B:225:0x06e6, B:227:0x06ec, B:228:0x06fd, B:234:0x0698, B:235:0x06f2, B:237:0x06fa, B:241:0x05c1, B:243:0x05cb, B:259:0x038c, B:116:0x0370, B:118:0x0376, B:119:0x0388), top: B:103:0x032d, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:165:0x0515  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x0534 A[Catch: all -> 0x0721, TryCatch #0 {all -> 0x0721, blocks: (B:104:0x032d, B:106:0x033b, B:108:0x033f, B:109:0x0355, B:111:0x035d, B:113:0x0369, B:114:0x036f, B:121:0x038e, B:123:0x0397, B:124:0x03a0, B:127:0x03ae, B:130:0x03bf, B:132:0x0414, B:134:0x0446, B:136:0x0464, B:138:0x0497, B:139:0x049a, B:140:0x04af, B:142:0x04c7, B:143:0x04cd, B:145:0x04d5, B:146:0x04d7, B:148:0x04df, B:149:0x04e2, B:151:0x04eb, B:152:0x04ed, B:154:0x04f2, B:156:0x04f6, B:158:0x04fc, B:160:0x0504, B:161:0x050a, B:163:0x050e, B:166:0x0517, B:167:0x0524, B:169:0x0527, B:171:0x0534, B:173:0x0556, B:177:0x0565, B:179:0x0569, B:182:0x0578, B:184:0x059a, B:186:0x05a4, B:187:0x05a7, B:189:0x05ab, B:190:0x05ad, B:192:0x05b7, B:197:0x05d4, B:199:0x05e6, B:202:0x05f2, B:204:0x0607, B:211:0x0619, B:213:0x0641, B:214:0x0687, B:216:0x068b, B:218:0x0691, B:222:0x069a, B:233:0x06a3, B:223:0x06dc, B:225:0x06e6, B:227:0x06ec, B:228:0x06fd, B:234:0x0698, B:235:0x06f2, B:237:0x06fa, B:241:0x05c1, B:243:0x05cb, B:259:0x038c, B:116:0x0370, B:118:0x0376, B:119:0x0388), top: B:103:0x032d, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:179:0x0569 A[Catch: all -> 0x0721, TryCatch #0 {all -> 0x0721, blocks: (B:104:0x032d, B:106:0x033b, B:108:0x033f, B:109:0x0355, B:111:0x035d, B:113:0x0369, B:114:0x036f, B:121:0x038e, B:123:0x0397, B:124:0x03a0, B:127:0x03ae, B:130:0x03bf, B:132:0x0414, B:134:0x0446, B:136:0x0464, B:138:0x0497, B:139:0x049a, B:140:0x04af, B:142:0x04c7, B:143:0x04cd, B:145:0x04d5, B:146:0x04d7, B:148:0x04df, B:149:0x04e2, B:151:0x04eb, B:152:0x04ed, B:154:0x04f2, B:156:0x04f6, B:158:0x04fc, B:160:0x0504, B:161:0x050a, B:163:0x050e, B:166:0x0517, B:167:0x0524, B:169:0x0527, B:171:0x0534, B:173:0x0556, B:177:0x0565, B:179:0x0569, B:182:0x0578, B:184:0x059a, B:186:0x05a4, B:187:0x05a7, B:189:0x05ab, B:190:0x05ad, B:192:0x05b7, B:197:0x05d4, B:199:0x05e6, B:202:0x05f2, B:204:0x0607, B:211:0x0619, B:213:0x0641, B:214:0x0687, B:216:0x068b, B:218:0x0691, B:222:0x069a, B:233:0x06a3, B:223:0x06dc, B:225:0x06e6, B:227:0x06ec, B:228:0x06fd, B:234:0x0698, B:235:0x06f2, B:237:0x06fa, B:241:0x05c1, B:243:0x05cb, B:259:0x038c, B:116:0x0370, B:118:0x0376, B:119:0x0388), top: B:103:0x032d, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:184:0x059a A[Catch: all -> 0x0721, TryCatch #0 {all -> 0x0721, blocks: (B:104:0x032d, B:106:0x033b, B:108:0x033f, B:109:0x0355, B:111:0x035d, B:113:0x0369, B:114:0x036f, B:121:0x038e, B:123:0x0397, B:124:0x03a0, B:127:0x03ae, B:130:0x03bf, B:132:0x0414, B:134:0x0446, B:136:0x0464, B:138:0x0497, B:139:0x049a, B:140:0x04af, B:142:0x04c7, B:143:0x04cd, B:145:0x04d5, B:146:0x04d7, B:148:0x04df, B:149:0x04e2, B:151:0x04eb, B:152:0x04ed, B:154:0x04f2, B:156:0x04f6, B:158:0x04fc, B:160:0x0504, B:161:0x050a, B:163:0x050e, B:166:0x0517, B:167:0x0524, B:169:0x0527, B:171:0x0534, B:173:0x0556, B:177:0x0565, B:179:0x0569, B:182:0x0578, B:184:0x059a, B:186:0x05a4, B:187:0x05a7, B:189:0x05ab, B:190:0x05ad, B:192:0x05b7, B:197:0x05d4, B:199:0x05e6, B:202:0x05f2, B:204:0x0607, B:211:0x0619, B:213:0x0641, B:214:0x0687, B:216:0x068b, B:218:0x0691, B:222:0x069a, B:233:0x06a3, B:223:0x06dc, B:225:0x06e6, B:227:0x06ec, B:228:0x06fd, B:234:0x0698, B:235:0x06f2, B:237:0x06fa, B:241:0x05c1, B:243:0x05cb, B:259:0x038c, B:116:0x0370, B:118:0x0376, B:119:0x0388), top: B:103:0x032d, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:199:0x05e6 A[Catch: all -> 0x0721, TryCatch #0 {all -> 0x0721, blocks: (B:104:0x032d, B:106:0x033b, B:108:0x033f, B:109:0x0355, B:111:0x035d, B:113:0x0369, B:114:0x036f, B:121:0x038e, B:123:0x0397, B:124:0x03a0, B:127:0x03ae, B:130:0x03bf, B:132:0x0414, B:134:0x0446, B:136:0x0464, B:138:0x0497, B:139:0x049a, B:140:0x04af, B:142:0x04c7, B:143:0x04cd, B:145:0x04d5, B:146:0x04d7, B:148:0x04df, B:149:0x04e2, B:151:0x04eb, B:152:0x04ed, B:154:0x04f2, B:156:0x04f6, B:158:0x04fc, B:160:0x0504, B:161:0x050a, B:163:0x050e, B:166:0x0517, B:167:0x0524, B:169:0x0527, B:171:0x0534, B:173:0x0556, B:177:0x0565, B:179:0x0569, B:182:0x0578, B:184:0x059a, B:186:0x05a4, B:187:0x05a7, B:189:0x05ab, B:190:0x05ad, B:192:0x05b7, B:197:0x05d4, B:199:0x05e6, B:202:0x05f2, B:204:0x0607, B:211:0x0619, B:213:0x0641, B:214:0x0687, B:216:0x068b, B:218:0x0691, B:222:0x069a, B:233:0x06a3, B:223:0x06dc, B:225:0x06e6, B:227:0x06ec, B:228:0x06fd, B:234:0x0698, B:235:0x06f2, B:237:0x06fa, B:241:0x05c1, B:243:0x05cb, B:259:0x038c, B:116:0x0370, B:118:0x0376, B:119:0x0388), top: B:103:0x032d, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:201:0x05ed  */
    /* JADX WARN: Removed duplicated region for block: B:204:0x0607 A[Catch: all -> 0x0721, TryCatch #0 {all -> 0x0721, blocks: (B:104:0x032d, B:106:0x033b, B:108:0x033f, B:109:0x0355, B:111:0x035d, B:113:0x0369, B:114:0x036f, B:121:0x038e, B:123:0x0397, B:124:0x03a0, B:127:0x03ae, B:130:0x03bf, B:132:0x0414, B:134:0x0446, B:136:0x0464, B:138:0x0497, B:139:0x049a, B:140:0x04af, B:142:0x04c7, B:143:0x04cd, B:145:0x04d5, B:146:0x04d7, B:148:0x04df, B:149:0x04e2, B:151:0x04eb, B:152:0x04ed, B:154:0x04f2, B:156:0x04f6, B:158:0x04fc, B:160:0x0504, B:161:0x050a, B:163:0x050e, B:166:0x0517, B:167:0x0524, B:169:0x0527, B:171:0x0534, B:173:0x0556, B:177:0x0565, B:179:0x0569, B:182:0x0578, B:184:0x059a, B:186:0x05a4, B:187:0x05a7, B:189:0x05ab, B:190:0x05ad, B:192:0x05b7, B:197:0x05d4, B:199:0x05e6, B:202:0x05f2, B:204:0x0607, B:211:0x0619, B:213:0x0641, B:214:0x0687, B:216:0x068b, B:218:0x0691, B:222:0x069a, B:233:0x06a3, B:223:0x06dc, B:225:0x06e6, B:227:0x06ec, B:228:0x06fd, B:234:0x0698, B:235:0x06f2, B:237:0x06fa, B:241:0x05c1, B:243:0x05cb, B:259:0x038c, B:116:0x0370, B:118:0x0376, B:119:0x0388), top: B:103:0x032d, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:213:0x0641 A[Catch: all -> 0x0721, TryCatch #0 {all -> 0x0721, blocks: (B:104:0x032d, B:106:0x033b, B:108:0x033f, B:109:0x0355, B:111:0x035d, B:113:0x0369, B:114:0x036f, B:121:0x038e, B:123:0x0397, B:124:0x03a0, B:127:0x03ae, B:130:0x03bf, B:132:0x0414, B:134:0x0446, B:136:0x0464, B:138:0x0497, B:139:0x049a, B:140:0x04af, B:142:0x04c7, B:143:0x04cd, B:145:0x04d5, B:146:0x04d7, B:148:0x04df, B:149:0x04e2, B:151:0x04eb, B:152:0x04ed, B:154:0x04f2, B:156:0x04f6, B:158:0x04fc, B:160:0x0504, B:161:0x050a, B:163:0x050e, B:166:0x0517, B:167:0x0524, B:169:0x0527, B:171:0x0534, B:173:0x0556, B:177:0x0565, B:179:0x0569, B:182:0x0578, B:184:0x059a, B:186:0x05a4, B:187:0x05a7, B:189:0x05ab, B:190:0x05ad, B:192:0x05b7, B:197:0x05d4, B:199:0x05e6, B:202:0x05f2, B:204:0x0607, B:211:0x0619, B:213:0x0641, B:214:0x0687, B:216:0x068b, B:218:0x0691, B:222:0x069a, B:233:0x06a3, B:223:0x06dc, B:225:0x06e6, B:227:0x06ec, B:228:0x06fd, B:234:0x0698, B:235:0x06f2, B:237:0x06fa, B:241:0x05c1, B:243:0x05cb, B:259:0x038c, B:116:0x0370, B:118:0x0376, B:119:0x0388), top: B:103:0x032d, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:216:0x068b A[Catch: all -> 0x0721, TryCatch #0 {all -> 0x0721, blocks: (B:104:0x032d, B:106:0x033b, B:108:0x033f, B:109:0x0355, B:111:0x035d, B:113:0x0369, B:114:0x036f, B:121:0x038e, B:123:0x0397, B:124:0x03a0, B:127:0x03ae, B:130:0x03bf, B:132:0x0414, B:134:0x0446, B:136:0x0464, B:138:0x0497, B:139:0x049a, B:140:0x04af, B:142:0x04c7, B:143:0x04cd, B:145:0x04d5, B:146:0x04d7, B:148:0x04df, B:149:0x04e2, B:151:0x04eb, B:152:0x04ed, B:154:0x04f2, B:156:0x04f6, B:158:0x04fc, B:160:0x0504, B:161:0x050a, B:163:0x050e, B:166:0x0517, B:167:0x0524, B:169:0x0527, B:171:0x0534, B:173:0x0556, B:177:0x0565, B:179:0x0569, B:182:0x0578, B:184:0x059a, B:186:0x05a4, B:187:0x05a7, B:189:0x05ab, B:190:0x05ad, B:192:0x05b7, B:197:0x05d4, B:199:0x05e6, B:202:0x05f2, B:204:0x0607, B:211:0x0619, B:213:0x0641, B:214:0x0687, B:216:0x068b, B:218:0x0691, B:222:0x069a, B:233:0x06a3, B:223:0x06dc, B:225:0x06e6, B:227:0x06ec, B:228:0x06fd, B:234:0x0698, B:235:0x06f2, B:237:0x06fa, B:241:0x05c1, B:243:0x05cb, B:259:0x038c, B:116:0x0370, B:118:0x0376, B:119:0x0388), top: B:103:0x032d, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:237:0x06fa A[Catch: all -> 0x0721, TryCatch #0 {all -> 0x0721, blocks: (B:104:0x032d, B:106:0x033b, B:108:0x033f, B:109:0x0355, B:111:0x035d, B:113:0x0369, B:114:0x036f, B:121:0x038e, B:123:0x0397, B:124:0x03a0, B:127:0x03ae, B:130:0x03bf, B:132:0x0414, B:134:0x0446, B:136:0x0464, B:138:0x0497, B:139:0x049a, B:140:0x04af, B:142:0x04c7, B:143:0x04cd, B:145:0x04d5, B:146:0x04d7, B:148:0x04df, B:149:0x04e2, B:151:0x04eb, B:152:0x04ed, B:154:0x04f2, B:156:0x04f6, B:158:0x04fc, B:160:0x0504, B:161:0x050a, B:163:0x050e, B:166:0x0517, B:167:0x0524, B:169:0x0527, B:171:0x0534, B:173:0x0556, B:177:0x0565, B:179:0x0569, B:182:0x0578, B:184:0x059a, B:186:0x05a4, B:187:0x05a7, B:189:0x05ab, B:190:0x05ad, B:192:0x05b7, B:197:0x05d4, B:199:0x05e6, B:202:0x05f2, B:204:0x0607, B:211:0x0619, B:213:0x0641, B:214:0x0687, B:216:0x068b, B:218:0x0691, B:222:0x069a, B:233:0x06a3, B:223:0x06dc, B:225:0x06e6, B:227:0x06ec, B:228:0x06fd, B:234:0x0698, B:235:0x06f2, B:237:0x06fa, B:241:0x05c1, B:243:0x05cb, B:259:0x038c, B:116:0x0370, B:118:0x0376, B:119:0x0388), top: B:103:0x032d, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:240:0x05f0  */
    /* JADX WARN: Removed duplicated region for block: B:246:0x05e2  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x0563  */
    /* JADX WARN: Removed duplicated region for block: B:249:0x04cc  */
    /* JADX WARN: Removed duplicated region for block: B:250:0x04a5  */
    /* JADX WARN: Removed duplicated region for block: B:252:0x0452  */
    /* JADX WARN: Removed duplicated region for block: B:253:0x03bd  */
    /* JADX WARN: Removed duplicated region for block: B:254:0x03ac  */
    /* JADX WARN: Type inference failed for: r5v10 */
    /* JADX WARN: Type inference failed for: r5v8 */
    /* JADX WARN: Type inference failed for: r5v9, types: [int, boolean] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int bindServiceLocked(IApplicationThread iApplicationThread, IBinder iBinder, Intent intent, String str, IServiceConnection iServiceConnection, long j, String str2, boolean z, int i, String str3, IApplicationThread iApplicationThread2, String str4, int i2) throws TransactionTooLargeException {
        ActivityServiceConnectionsHolder<ConnectionRecord> activityServiceConnectionsHolder;
        int i3;
        PendingIntent pendingIntent;
        ProcessRecord processRecord;
        boolean z2;
        boolean z3;
        ArrayList<ConnectionRecord> arrayList;
        Intent intent2;
        boolean z4;
        ServiceRecord serviceRecord;
        boolean z5;
        boolean z6;
        ProcessServiceRecord processServiceRecord;
        IBinder iBinder2;
        ConnectionRecord connectionRecord;
        boolean z7;
        ProcessRecord processRecord2;
        AppBindRecord appBindRecord;
        ServiceLookupResult serviceLookupResult;
        ActiveServices activeServices;
        ConnectionRecord connectionRecord2;
        ServiceRecord serviceRecord2;
        ?? r5;
        ProcessRecord processRecord3;
        ProcessRecord processRecord4;
        boolean contains;
        boolean z8;
        boolean z9;
        int i4;
        boolean z10;
        ProcessRecord processRecord5;
        ProcessRecord processRecord6;
        ProcessRecord processRecord7;
        int i5;
        IntentBindRecord intentBindRecord;
        boolean z11;
        ArrayList<ConnectionRecord> arrayList2;
        ProcessStateRecord processStateRecord;
        Intent intent3 = intent;
        if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
            Slog.v(TAG_SERVICE, "bindService: " + intent3 + " type=" + str + " conn=" + iServiceConnection.asBinder() + " flags=0x" + Long.toHexString(j));
        }
        int callingPid = Binder.getCallingPid();
        int callingUid = Binder.getCallingUid();
        ProcessRecord recordForAppLOSP = this.mAm.getRecordForAppLOSP(iApplicationThread);
        if (recordForAppLOSP == null) {
            throw new SecurityException("Unable to find app for caller " + iApplicationThread + " (pid=" + callingPid + ") when binding service " + intent3);
        }
        if (this.mActiveServicesExt.interceptBindServiceLockedBegin(this.mAm.mContext, intent, i2, recordForAppLOSP, str4)) {
            return 0;
        }
        ConnectionRecord connectionRecord3 = null;
        if (iBinder != null) {
            ActivityServiceConnectionsHolder<ConnectionRecord> serviceConnectionsHolder = this.mAm.mAtmInternal.getServiceConnectionsHolder(iBinder);
            if (serviceConnectionsHolder == null) {
                Slog.w("ActivityManager", "Binding with unknown activity: " + iBinder);
                return 0;
            }
            activityServiceConnectionsHolder = serviceConnectionsHolder;
        } else {
            activityServiceConnectionsHolder = null;
        }
        boolean z12 = recordForAppLOSP.info.uid == 1000;
        if (z12) {
            intent3.setDefusable(true);
            PendingIntent pendingIntent2 = (PendingIntent) intent3.getParcelableExtra("android.intent.extra.client_intent");
            if (pendingIntent2 != null) {
                int intExtra = intent3.getIntExtra("android.intent.extra.client_label", 0);
                if (intExtra != 0) {
                    intent3 = intent.cloneFilter();
                }
                pendingIntent = pendingIntent2;
                i3 = intExtra;
            } else {
                pendingIntent = pendingIntent2;
                i3 = 0;
            }
        } else {
            i3 = 0;
            pendingIntent = null;
        }
        if ((j & 134217728) != 0) {
            this.mAm.enforceCallingPermission("android.permission.MANAGE_ACTIVITY_TASKS", "BIND_TREAT_LIKE_ACTIVITY");
        }
        if ((j & 524288) != 0 && !z12) {
            throw new SecurityException("Non-system caller (pid=" + callingPid + ") set BIND_SCHEDULE_LIKE_TOP_APP when binding service " + intent3);
        }
        if ((j & 16777216) != 0 && !z12) {
            throw new SecurityException("Non-system caller " + iApplicationThread + " (pid=" + callingPid + ") set BIND_ALLOW_WHITELIST_MANAGEMENT when binding service " + intent3);
        }
        long j2 = j & 4194304;
        if (j2 != 0 && !z12) {
            throw new SecurityException("Non-system caller " + iApplicationThread + " (pid=" + callingPid + ") set BIND_ALLOW_INSTANT when binding service " + intent3);
        }
        if ((j & 65536) != 0 && !z12) {
            throw new SecurityException("Non-system caller (pid=" + callingPid + ") set BIND_ALMOST_PERCEPTIBLE when binding service " + intent3);
        }
        if ((j & 1048576) != 0) {
            this.mAm.enforceCallingPermission("android.permission.START_ACTIVITIES_FROM_BACKGROUND", "BIND_ALLOW_BACKGROUND_ACTIVITY_STARTS");
        }
        if ((j & 262144) != 0) {
            this.mAm.enforceCallingPermission("android.permission.START_FOREGROUND_SERVICES_FROM_BACKGROUND", "BIND_ALLOW_FOREGROUND_SERVICE_STARTS_FROM_BACKGROUND");
        }
        boolean z13 = recordForAppLOSP.mState.getSetSchedGroup() != 0;
        boolean z14 = ((j & Integer.toUnsignedLong(Integer.MIN_VALUE)) == 0 && (j & 4611686018427387904L) == 0) ? false : true;
        boolean z15 = j2 != 0;
        boolean z16 = (j & 8192) != 0;
        ProcessRecord recordForAppLOSP2 = i > 0 ? this.mAm.getRecordForAppLOSP(iApplicationThread2) : null;
        boolean z17 = z13;
        ActivityServiceConnectionsHolder<ConnectionRecord> activityServiceConnectionsHolder2 = activityServiceConnectionsHolder;
        Intent intent4 = intent3;
        ServiceLookupResult retrieveServiceLocked = retrieveServiceLocked(intent3, str2, z, i, str3, str, str4, callingPid, callingUid, i2, true, z17, z14, z15, null, z16);
        if (retrieveServiceLocked == null) {
            return 0;
        }
        ServiceRecord serviceRecord3 = retrieveServiceLocked.record;
        if (serviceRecord3 == null) {
            return -1;
        }
        AppBindRecord retrieveAppBindingLocked = serviceRecord3.retrieveAppBindingLocked(intent4, recordForAppLOSP, recordForAppLOSP2);
        ProcessServiceRecord processServiceRecord2 = retrieveAppBindingLocked.client.mServices;
        if (processServiceRecord2.numberOfConnections() >= this.mAm.mConstants.mMaxServiceConnectionsPerProcess) {
            Slog.w("ActivityManager", "bindService exceeded max service connection number per process, callerApp:" + recordForAppLOSP.processName + " intent:" + intent4);
            return 0;
        }
        if (this.mActiveServicesExt.interceptBindServiceLockedBeforeConnection(this.mAm, Binder.getCallingPid(), Binder.getCallingUid(), str4, serviceRecord3, iApplicationThread, iBinder, intent4, str, iServiceConnection, j, str2, i2, recordForAppLOSP)) {
            return 0;
        }
        synchronized (this.mAm.mPidsSelfLocked) {
            processRecord = this.mAm.mPidsSelfLocked.get(callingPid);
        }
        String str5 = processRecord != null ? processRecord.processName : str4;
        int curProcState = (processRecord == null || processRecord.getThread() == null || processRecord.isKilled()) ? -1 : processRecord.mState.getCurProcState();
        serviceRecord3.updateProcessStateOnRequest();
        boolean deferServiceBringupIfFrozenLocked = deferServiceBringupIfFrozenLocked(serviceRecord3, intent4, str4, null, callingUid, callingPid, str5, curProcState, false, z17, i2, BackgroundStartPrivileges.NONE, true, iServiceConnection);
        boolean z18 = (deferServiceBringupIfFrozenLocked || requestStartTargetPermissionsReviewIfNeededLocked(serviceRecord3, str4, null, callingUid, intent4, z17, i2, true, iServiceConnection)) ? false : true;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (unscheduleServiceRestartLocked(serviceRecord3, recordForAppLOSP.info.uid, false) && ActivityManagerDebugConfig.DEBUG_SERVICE) {
                Slog.v(TAG_SERVICE, "BIND SERVICE WHILE RESTART PENDING: " + serviceRecord3);
            }
            if ((j & 1) != 0) {
                serviceRecord3.lastActivity = SystemClock.uptimeMillis();
                if (!serviceRecord3.hasAutoCreateConnections()) {
                    synchronized (this.mAm.mProcessStats.mLock) {
                        ServiceState tracker = serviceRecord3.getTracker();
                        if (tracker != null) {
                            z2 = true;
                            tracker.setBound(true, this.mAm.mProcessStats.getMemFactorLocked(), SystemClock.uptimeMillis());
                        } else {
                            z2 = true;
                        }
                    }
                    if ((j & 2097152) != 0) {
                        this.mAm.requireAllowedAssociationsLocked(serviceRecord3.appInfo.packageName);
                    }
                    z3 = (serviceRecord3.appInfo.flags & AudioDevice.OUT_AUX_LINE) == 0 ? z2 : false;
                    boolean z19 = serviceRecord3.startRequested;
                    boolean z20 = serviceRecord3.getConnections().isEmpty() ? z2 : false;
                    ActivityManagerService activityManagerService = this.mAm;
                    int i6 = recordForAppLOSP.uid;
                    String str6 = recordForAppLOSP.processName;
                    int curProcState2 = recordForAppLOSP.mState.getCurProcState();
                    ApplicationInfo applicationInfo = serviceRecord3.appInfo;
                    activityManagerService.startAssociationLocked(i6, str6, curProcState2, applicationInfo.uid, applicationInfo.longVersionCode, serviceRecord3.instanceName, serviceRecord3.processName);
                    this.mActiveServicesExt.noteAssociation(recordForAppLOSP.uid, serviceRecord3.appInfo.uid, z2);
                    this.mAm.grantImplicitAccess(recordForAppLOSP.userId, intent4, recordForAppLOSP.uid, UserHandle.getAppId(serviceRecord3.appInfo.uid));
                    IBinder asBinder = iServiceConnection.asBinder();
                    arrayList = this.mServiceConnections.get(asBinder);
                    if (arrayList == null) {
                        z5 = deferServiceBringupIfFrozenLocked;
                        processServiceRecord = processServiceRecord2;
                        z6 = z18;
                        iBinder2 = asBinder;
                        intent2 = intent4;
                        z4 = z19;
                        serviceRecord = serviceRecord3;
                        connectionRecord3 = this.mActiveServicesExt.retrieveConnectionRecordLocked(arrayList, asBinder, retrieveAppBindingLocked, activityServiceConnectionsHolder2, j, i3, pendingIntent, recordForAppLOSP.uid, recordForAppLOSP.processName, str4);
                        if (connectionRecord3 != null) {
                            Slog.v("ActivityManager", "Has totaly same connectionRecord, just reuse it!");
                            connectionRecord = connectionRecord3;
                            z7 = false;
                            if (z7) {
                                serviceLookupResult = retrieveServiceLocked;
                                processRecord2 = recordForAppLOSP;
                                ConnectionRecord connectionRecord4 = new ConnectionRecord(retrieveAppBindingLocked, activityServiceConnectionsHolder2, iServiceConnection, j, i3, pendingIntent, recordForAppLOSP.uid, recordForAppLOSP.processName, str4, retrieveServiceLocked.aliasComponent);
                                serviceRecord.addConnection(iBinder2, connectionRecord4);
                                appBindRecord = retrieveAppBindingLocked;
                                appBindRecord.connections.add(connectionRecord4);
                                if (activityServiceConnectionsHolder2 != null) {
                                    activityServiceConnectionsHolder2.addConnection(connectionRecord4);
                                }
                                processServiceRecord.addConnection(connectionRecord4);
                                connectionRecord4.startAssociationIfNeeded();
                                activeServices = this;
                                serviceRecord2 = serviceRecord;
                                connectionRecord2 = connectionRecord4;
                            } else {
                                processRecord2 = recordForAppLOSP;
                                appBindRecord = retrieveAppBindingLocked;
                                serviceLookupResult = retrieveServiceLocked;
                                activeServices = this;
                                connectionRecord2 = connectionRecord;
                                serviceRecord2 = serviceRecord;
                            }
                            activeServices.mActiveServicesExt.onServiceConnectionInfoCollect(str4, processServiceRecord.numberOfConnections());
                            activeServices.mActiveServicesExt.hookBindServiceAfterStartAssociation(connectionRecord2, appBindRecord, serviceRecord2);
                            if (connectionRecord2.hasFlag(8)) {
                                r5 = 1;
                                processServiceRecord.setHasAboveClient(true);
                            } else {
                                r5 = 1;
                            }
                            if (connectionRecord2.hasFlag(16777216)) {
                                serviceRecord2.allowlistManager = r5;
                            }
                            if (connectionRecord2.hasFlag(AudioDevice.OUT_FM)) {
                                serviceRecord2.setAllowedBgActivityStartsByBinding(r5);
                            }
                            if (connectionRecord2.hasFlag(32768)) {
                                serviceRecord2.isNotAppComponentUsage = r5;
                            }
                            processRecord3 = serviceRecord2.app;
                            int i7 = 2;
                            if (processRecord3 != null && (processStateRecord = processRecord3.mState) != null && processStateRecord.getCurProcState() <= 2 && connectionRecord2.hasFlag(65536)) {
                                serviceRecord2.lastTopAlmostPerceptibleBindRequestUptimeMs = SystemClock.uptimeMillis();
                            }
                            processRecord4 = serviceRecord2.app;
                            if (processRecord4 != null) {
                                activeServices.updateServiceClientActivitiesLocked(processRecord4.mServices, connectionRecord2, r5);
                            }
                            if (z7) {
                                if (arrayList == null) {
                                    arrayList2 = new ArrayList<>();
                                    activeServices.mServiceConnections.put(iBinder2, arrayList2);
                                } else {
                                    arrayList2 = arrayList;
                                }
                                arrayList2.add(connectionRecord2);
                            }
                            contains = activeServices.mPendingServices.contains(serviceRecord2);
                            if (connectionRecord2.hasFlag((int) r5)) {
                                serviceRecord2.lastActivity = SystemClock.uptimeMillis();
                                if (bringUpServiceLocked(serviceRecord2, intent2.getFlags(), z17, false, z6, z5, true) != null) {
                                    activeServices.mAm.updateOomAdjPendingTargetsLocked(4);
                                    Binder.restoreCallingIdentity(clearCallingIdentity);
                                    return 0;
                                }
                                z8 = false;
                                z9 = r5;
                            } else {
                                z8 = false;
                                z9 = false;
                            }
                            IActiveServicesExt iActiveServicesExt = activeServices.mActiveServicesExt;
                            if (contains && activeServices.mPendingServices.contains(serviceRecord2)) {
                                i4 = callingUid;
                                z10 = r5;
                            } else {
                                i4 = callingUid;
                                z10 = z8;
                            }
                            iActiveServicesExt.handleAfterBindInnerService(serviceRecord2, str4, i4, z10);
                            setFgsRestrictionLocked(str4, callingPid, i4, intent2, serviceRecord2, i2, BackgroundStartPrivileges.NONE, true, false);
                            processRecord5 = serviceRecord2.app;
                            if (processRecord5 != null) {
                                ProcessServiceRecord processServiceRecord3 = processRecord5.mServices;
                                if (connectionRecord2.hasFlag(AudioFormat.OPUS)) {
                                    processServiceRecord3.setTreatLikeActivity(r5);
                                }
                                if (serviceRecord2.allowlistManager) {
                                    processServiceRecord3.mAllowlistManager = r5;
                                }
                                ActivityManagerService activityManagerService2 = activeServices.mAm;
                                ProcessRecord processRecord8 = serviceRecord2.app;
                                if (processRecord2.hasActivitiesOrRecentTasks() && processServiceRecord3.hasClientActivities()) {
                                    processRecord6 = processRecord2;
                                    z11 = r5;
                                    activityManagerService2.updateLruProcessLocked(processRecord8, z11, appBindRecord.client);
                                    activeServices.mAm.enqueueOomAdjTargetLocked(serviceRecord2.app);
                                    z9 = r5;
                                }
                                processRecord6 = processRecord2;
                                if (processRecord6.mState.getCurProcState() > 2 || !connectionRecord2.hasFlag(AudioFormat.OPUS)) {
                                    z11 = z8;
                                    activityManagerService2.updateLruProcessLocked(processRecord8, z11, appBindRecord.client);
                                    activeServices.mAm.enqueueOomAdjTargetLocked(serviceRecord2.app);
                                    z9 = r5;
                                }
                                z11 = r5;
                                activityManagerService2.updateLruProcessLocked(processRecord8, z11, appBindRecord.client);
                                activeServices.mAm.enqueueOomAdjTargetLocked(serviceRecord2.app);
                                z9 = r5;
                            } else {
                                processRecord6 = processRecord2;
                            }
                            if (z9) {
                                activeServices.mAm.updateOomAdjPendingTargetsLocked(4);
                            }
                            int i8 = z3 ? 2 : r5;
                            int i9 = serviceRecord2.appInfo.uid;
                            String shortAction = ActivityManagerService.getShortAction(intent2.getAction());
                            processRecord7 = serviceRecord2.app;
                            if (processRecord7 != null && processRecord7.getThread() != null) {
                                if (!z4 && !z20) {
                                    i5 = r5;
                                    ProcessRecord processRecord9 = processRecord6;
                                    FrameworkStatsLog.write(FrameworkStatsLog.SERVICE_REQUEST_EVENT_REPORTED, i9, i4, shortAction, 2, false, i5, activeServices.getShortProcessNameForStats(i4, processRecord6.processName), activeServices.getShortServiceNameForStats(serviceRecord2), i8, serviceRecord2.packageName, processRecord6.info.packageName, processRecord6.mState.getCurProcState(), serviceRecord2.mProcessStateOnRequest);
                                    if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                                        Slog.v(TAG_SERVICE, "Bind " + serviceRecord2 + " with " + appBindRecord + ": received=" + appBindRecord.intent.received + " apps=" + appBindRecord.intent.apps.size() + " doRebind=" + appBindRecord.intent.doRebind);
                                    }
                                    if (serviceRecord2.app != null) {
                                        IntentBindRecord intentBindRecord2 = appBindRecord.intent;
                                        if (intentBindRecord2.received) {
                                            ComponentName componentName = serviceLookupResult.aliasComponent;
                                            if (componentName == null) {
                                                componentName = serviceRecord2.name;
                                            }
                                            try {
                                                connectionRecord2.conn.connected(componentName, intentBindRecord2.binder, z8);
                                            } catch (Exception e) {
                                                Slog.w("ActivityManager", "Failure sending service " + serviceRecord2.shortInstanceName + " to connection " + connectionRecord2.conn.asBinder() + " (in " + connectionRecord2.binding.client.processName + ")", e);
                                            }
                                            if (appBindRecord.intent.apps.size() == r5) {
                                                IntentBindRecord intentBindRecord3 = appBindRecord.intent;
                                                if (intentBindRecord3.doRebind) {
                                                    activeServices.requestServiceBindingLocked(serviceRecord2, intentBindRecord3, z17, r5);
                                                }
                                            }
                                            activeServices.maybeLogBindCrossProfileService(i2, str4, processRecord9.info.uid);
                                            activeServices.getServiceMapLocked(serviceRecord2.userId).ensureNotStartingBackgroundLocked(serviceRecord2);
                                            Binder.restoreCallingIdentity(clearCallingIdentity);
                                            activeServices.mActiveServicesExt.hookUpdateServiceBindStatus(serviceRecord2, intent2.getAction(), r5);
                                            activeServices.notifyBindingServiceEventLocked(processRecord9, str4);
                                            return r5;
                                        }
                                    }
                                    intentBindRecord = appBindRecord.intent;
                                    if (!intentBindRecord.requested) {
                                        activeServices.requestServiceBindingLocked(serviceRecord2, intentBindRecord, z17, z8);
                                    }
                                    activeServices.maybeLogBindCrossProfileService(i2, str4, processRecord9.info.uid);
                                    activeServices.getServiceMapLocked(serviceRecord2.userId).ensureNotStartingBackgroundLocked(serviceRecord2);
                                    Binder.restoreCallingIdentity(clearCallingIdentity);
                                    activeServices.mActiveServicesExt.hookUpdateServiceBindStatus(serviceRecord2, intent2.getAction(), r5);
                                    activeServices.notifyBindingServiceEventLocked(processRecord9, str4);
                                    return r5;
                                }
                                i5 = i7;
                                ProcessRecord processRecord92 = processRecord6;
                                FrameworkStatsLog.write(FrameworkStatsLog.SERVICE_REQUEST_EVENT_REPORTED, i9, i4, shortAction, 2, false, i5, activeServices.getShortProcessNameForStats(i4, processRecord6.processName), activeServices.getShortServiceNameForStats(serviceRecord2), i8, serviceRecord2.packageName, processRecord6.info.packageName, processRecord6.mState.getCurProcState(), serviceRecord2.mProcessStateOnRequest);
                                if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                                }
                                if (serviceRecord2.app != null) {
                                }
                                intentBindRecord = appBindRecord.intent;
                                if (!intentBindRecord.requested) {
                                }
                                activeServices.maybeLogBindCrossProfileService(i2, str4, processRecord92.info.uid);
                                activeServices.getServiceMapLocked(serviceRecord2.userId).ensureNotStartingBackgroundLocked(serviceRecord2);
                                Binder.restoreCallingIdentity(clearCallingIdentity);
                                activeServices.mActiveServicesExt.hookUpdateServiceBindStatus(serviceRecord2, intent2.getAction(), r5);
                                activeServices.notifyBindingServiceEventLocked(processRecord92, str4);
                                return r5;
                            }
                            i7 = 3;
                            i5 = i7;
                            ProcessRecord processRecord922 = processRecord6;
                            FrameworkStatsLog.write(FrameworkStatsLog.SERVICE_REQUEST_EVENT_REPORTED, i9, i4, shortAction, 2, false, i5, activeServices.getShortProcessNameForStats(i4, processRecord6.processName), activeServices.getShortServiceNameForStats(serviceRecord2), i8, serviceRecord2.packageName, processRecord6.info.packageName, processRecord6.mState.getCurProcState(), serviceRecord2.mProcessStateOnRequest);
                            if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                            }
                            if (serviceRecord2.app != null) {
                            }
                            intentBindRecord = appBindRecord.intent;
                            if (!intentBindRecord.requested) {
                            }
                            activeServices.maybeLogBindCrossProfileService(i2, str4, processRecord922.info.uid);
                            activeServices.getServiceMapLocked(serviceRecord2.userId).ensureNotStartingBackgroundLocked(serviceRecord2);
                            Binder.restoreCallingIdentity(clearCallingIdentity);
                            activeServices.mActiveServicesExt.hookUpdateServiceBindStatus(serviceRecord2, intent2.getAction(), r5);
                            activeServices.notifyBindingServiceEventLocked(processRecord922, str4);
                            return r5;
                        }
                    } else {
                        intent2 = intent4;
                        z4 = z19;
                        serviceRecord = serviceRecord3;
                        z5 = deferServiceBringupIfFrozenLocked;
                        z6 = z18;
                        processServiceRecord = processServiceRecord2;
                        iBinder2 = asBinder;
                    }
                    connectionRecord = connectionRecord3;
                    z7 = true;
                    if (z7) {
                    }
                    activeServices.mActiveServicesExt.onServiceConnectionInfoCollect(str4, processServiceRecord.numberOfConnections());
                    activeServices.mActiveServicesExt.hookBindServiceAfterStartAssociation(connectionRecord2, appBindRecord, serviceRecord2);
                    if (connectionRecord2.hasFlag(8)) {
                    }
                    if (connectionRecord2.hasFlag(16777216)) {
                    }
                    if (connectionRecord2.hasFlag(AudioDevice.OUT_FM)) {
                    }
                    if (connectionRecord2.hasFlag(32768)) {
                    }
                    processRecord3 = serviceRecord2.app;
                    int i72 = 2;
                    if (processRecord3 != null) {
                        serviceRecord2.lastTopAlmostPerceptibleBindRequestUptimeMs = SystemClock.uptimeMillis();
                    }
                    processRecord4 = serviceRecord2.app;
                    if (processRecord4 != null) {
                    }
                    if (z7) {
                    }
                    contains = activeServices.mPendingServices.contains(serviceRecord2);
                    if (connectionRecord2.hasFlag((int) r5)) {
                    }
                    IActiveServicesExt iActiveServicesExt2 = activeServices.mActiveServicesExt;
                    if (contains) {
                    }
                    i4 = callingUid;
                    z10 = z8;
                    iActiveServicesExt2.handleAfterBindInnerService(serviceRecord2, str4, i4, z10);
                    setFgsRestrictionLocked(str4, callingPid, i4, intent2, serviceRecord2, i2, BackgroundStartPrivileges.NONE, true, false);
                    processRecord5 = serviceRecord2.app;
                    if (processRecord5 != null) {
                    }
                    if (z9) {
                    }
                    if (z3) {
                    }
                    int i92 = serviceRecord2.appInfo.uid;
                    String shortAction2 = ActivityManagerService.getShortAction(intent2.getAction());
                    processRecord7 = serviceRecord2.app;
                    if (processRecord7 != null) {
                        if (!z4) {
                            i5 = r5;
                            ProcessRecord processRecord9222 = processRecord6;
                            FrameworkStatsLog.write(FrameworkStatsLog.SERVICE_REQUEST_EVENT_REPORTED, i92, i4, shortAction2, 2, false, i5, activeServices.getShortProcessNameForStats(i4, processRecord6.processName), activeServices.getShortServiceNameForStats(serviceRecord2), i8, serviceRecord2.packageName, processRecord6.info.packageName, processRecord6.mState.getCurProcState(), serviceRecord2.mProcessStateOnRequest);
                            if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                            }
                            if (serviceRecord2.app != null) {
                            }
                            intentBindRecord = appBindRecord.intent;
                            if (!intentBindRecord.requested) {
                            }
                            activeServices.maybeLogBindCrossProfileService(i2, str4, processRecord9222.info.uid);
                            activeServices.getServiceMapLocked(serviceRecord2.userId).ensureNotStartingBackgroundLocked(serviceRecord2);
                            Binder.restoreCallingIdentity(clearCallingIdentity);
                            activeServices.mActiveServicesExt.hookUpdateServiceBindStatus(serviceRecord2, intent2.getAction(), r5);
                            activeServices.notifyBindingServiceEventLocked(processRecord9222, str4);
                            return r5;
                        }
                        i5 = i72;
                        ProcessRecord processRecord92222 = processRecord6;
                        FrameworkStatsLog.write(FrameworkStatsLog.SERVICE_REQUEST_EVENT_REPORTED, i92, i4, shortAction2, 2, false, i5, activeServices.getShortProcessNameForStats(i4, processRecord6.processName), activeServices.getShortServiceNameForStats(serviceRecord2), i8, serviceRecord2.packageName, processRecord6.info.packageName, processRecord6.mState.getCurProcState(), serviceRecord2.mProcessStateOnRequest);
                        if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                        }
                        if (serviceRecord2.app != null) {
                        }
                        intentBindRecord = appBindRecord.intent;
                        if (!intentBindRecord.requested) {
                        }
                        activeServices.maybeLogBindCrossProfileService(i2, str4, processRecord92222.info.uid);
                        activeServices.getServiceMapLocked(serviceRecord2.userId).ensureNotStartingBackgroundLocked(serviceRecord2);
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        activeServices.mActiveServicesExt.hookUpdateServiceBindStatus(serviceRecord2, intent2.getAction(), r5);
                        activeServices.notifyBindingServiceEventLocked(processRecord92222, str4);
                        return r5;
                    }
                    i72 = 3;
                    i5 = i72;
                    ProcessRecord processRecord922222 = processRecord6;
                    FrameworkStatsLog.write(FrameworkStatsLog.SERVICE_REQUEST_EVENT_REPORTED, i92, i4, shortAction2, 2, false, i5, activeServices.getShortProcessNameForStats(i4, processRecord6.processName), activeServices.getShortServiceNameForStats(serviceRecord2), i8, serviceRecord2.packageName, processRecord6.info.packageName, processRecord6.mState.getCurProcState(), serviceRecord2.mProcessStateOnRequest);
                    if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                    }
                    if (serviceRecord2.app != null) {
                    }
                    intentBindRecord = appBindRecord.intent;
                    if (!intentBindRecord.requested) {
                    }
                    activeServices.maybeLogBindCrossProfileService(i2, str4, processRecord922222.info.uid);
                    activeServices.getServiceMapLocked(serviceRecord2.userId).ensureNotStartingBackgroundLocked(serviceRecord2);
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    activeServices.mActiveServicesExt.hookUpdateServiceBindStatus(serviceRecord2, intent2.getAction(), r5);
                    activeServices.notifyBindingServiceEventLocked(processRecord922222, str4);
                    return r5;
                }
            }
            z2 = true;
            if ((j & 2097152) != 0) {
            }
            if ((serviceRecord3.appInfo.flags & AudioDevice.OUT_AUX_LINE) == 0) {
            }
            boolean z192 = serviceRecord3.startRequested;
            if (serviceRecord3.getConnections().isEmpty()) {
            }
            ActivityManagerService activityManagerService3 = this.mAm;
            int i62 = recordForAppLOSP.uid;
            String str62 = recordForAppLOSP.processName;
            int curProcState22 = recordForAppLOSP.mState.getCurProcState();
            ApplicationInfo applicationInfo2 = serviceRecord3.appInfo;
            activityManagerService3.startAssociationLocked(i62, str62, curProcState22, applicationInfo2.uid, applicationInfo2.longVersionCode, serviceRecord3.instanceName, serviceRecord3.processName);
            this.mActiveServicesExt.noteAssociation(recordForAppLOSP.uid, serviceRecord3.appInfo.uid, z2);
            this.mAm.grantImplicitAccess(recordForAppLOSP.userId, intent4, recordForAppLOSP.uid, UserHandle.getAppId(serviceRecord3.appInfo.uid));
            IBinder asBinder2 = iServiceConnection.asBinder();
            arrayList = this.mServiceConnections.get(asBinder2);
            if (arrayList == null) {
            }
            connectionRecord = connectionRecord3;
            z7 = true;
            if (z7) {
            }
            activeServices.mActiveServicesExt.onServiceConnectionInfoCollect(str4, processServiceRecord.numberOfConnections());
            activeServices.mActiveServicesExt.hookBindServiceAfterStartAssociation(connectionRecord2, appBindRecord, serviceRecord2);
            if (connectionRecord2.hasFlag(8)) {
            }
            if (connectionRecord2.hasFlag(16777216)) {
            }
            if (connectionRecord2.hasFlag(AudioDevice.OUT_FM)) {
            }
            if (connectionRecord2.hasFlag(32768)) {
            }
            processRecord3 = serviceRecord2.app;
            int i722 = 2;
            if (processRecord3 != null) {
            }
            processRecord4 = serviceRecord2.app;
            if (processRecord4 != null) {
            }
            if (z7) {
            }
            contains = activeServices.mPendingServices.contains(serviceRecord2);
            if (connectionRecord2.hasFlag((int) r5)) {
            }
            IActiveServicesExt iActiveServicesExt22 = activeServices.mActiveServicesExt;
            if (contains) {
            }
            i4 = callingUid;
            z10 = z8;
            iActiveServicesExt22.handleAfterBindInnerService(serviceRecord2, str4, i4, z10);
            setFgsRestrictionLocked(str4, callingPid, i4, intent2, serviceRecord2, i2, BackgroundStartPrivileges.NONE, true, false);
            processRecord5 = serviceRecord2.app;
            if (processRecord5 != null) {
            }
            if (z9) {
            }
            if (z3) {
            }
            int i922 = serviceRecord2.appInfo.uid;
            String shortAction22 = ActivityManagerService.getShortAction(intent2.getAction());
            processRecord7 = serviceRecord2.app;
            if (processRecord7 != null) {
            }
            i722 = 3;
            i5 = i722;
            ProcessRecord processRecord9222222 = processRecord6;
            FrameworkStatsLog.write(FrameworkStatsLog.SERVICE_REQUEST_EVENT_REPORTED, i922, i4, shortAction22, 2, false, i5, activeServices.getShortProcessNameForStats(i4, processRecord6.processName), activeServices.getShortServiceNameForStats(serviceRecord2), i8, serviceRecord2.packageName, processRecord6.info.packageName, processRecord6.mState.getCurProcState(), serviceRecord2.mProcessStateOnRequest);
            if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
            }
            if (serviceRecord2.app != null) {
            }
            intentBindRecord = appBindRecord.intent;
            if (!intentBindRecord.requested) {
            }
            activeServices.maybeLogBindCrossProfileService(i2, str4, processRecord9222222.info.uid);
            activeServices.getServiceMapLocked(serviceRecord2.userId).ensureNotStartingBackgroundLocked(serviceRecord2);
            Binder.restoreCallingIdentity(clearCallingIdentity);
            activeServices.mActiveServicesExt.hookUpdateServiceBindStatus(serviceRecord2, intent2.getAction(), r5);
            activeServices.notifyBindingServiceEventLocked(processRecord9222222, str4);
            return r5;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }

    @GuardedBy({"mAm"})
    private void notifyBindingServiceEventLocked(ProcessRecord processRecord, String str) {
        ApplicationInfo applicationInfo = processRecord.info;
        if (applicationInfo != null) {
            str = applicationInfo.packageName;
        }
        if (str != null) {
            this.mAm.mHandler.obtainMessage(75, processRecord.uid, 0, str).sendToTarget();
        }
    }

    private void maybeLogBindCrossProfileService(int i, String str, int i2) {
        int userId;
        if (UserHandle.isCore(i2) || (userId = UserHandle.getUserId(i2)) == i || !this.mAm.mUserController.isSameProfileGroup(userId, i)) {
            return;
        }
        DevicePolicyEventLogger.createEvent(151).setStrings(new String[]{str}).write();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void publishServiceLocked(ServiceRecord serviceRecord, Intent intent, IBinder iBinder) {
        long j;
        boolean z;
        boolean z2;
        Intent.FilterComparison filterComparison;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            String str = ": ";
            if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                Slog.v(TAG_SERVICE, "PUBLISHING " + serviceRecord + " " + intent + ": " + iBinder);
            }
            if (serviceRecord != null) {
                Intent.FilterComparison filterComparison2 = new Intent.FilterComparison(intent);
                IntentBindRecord intentBindRecord = serviceRecord.bindings.get(filterComparison2);
                if (intentBindRecord != null && !intentBindRecord.received) {
                    intentBindRecord.binder = iBinder;
                    boolean z3 = true;
                    intentBindRecord.requested = true;
                    intentBindRecord.received = true;
                    ArrayMap<IBinder, ArrayList<ConnectionRecord>> connections = serviceRecord.getConnections();
                    int i = 1000;
                    if (connections.size() > 1000) {
                        Slog.v("ActivityManager", "too many connections: PUBLISHING " + serviceRecord + " " + intent + ": " + iBinder);
                        z = true;
                    } else {
                        z = false;
                    }
                    int size = connections.size() - 1;
                    while (size >= 0) {
                        ArrayList<ConnectionRecord> valueAt = connections.valueAt(size);
                        if (valueAt.size() > i) {
                            Slog.v("ActivityManager", "too many clist: PUBLISHING " + serviceRecord + " " + intent + str + iBinder);
                            z2 = z3;
                        } else {
                            z2 = false;
                        }
                        int i2 = 0;
                        while (i2 < valueAt.size()) {
                            String str2 = str;
                            ConnectionRecord connectionRecord = valueAt.get(i2);
                            if (!filterComparison2.equals(connectionRecord.binding.intent.intent)) {
                                if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                                    String str3 = TAG_SERVICE;
                                    filterComparison = filterComparison2;
                                    StringBuilder sb = new StringBuilder();
                                    j = clearCallingIdentity;
                                    try {
                                        sb.append("Not publishing to: ");
                                        sb.append(connectionRecord);
                                        Slog.v(str3, sb.toString());
                                    } catch (Throwable th) {
                                        th = th;
                                        Binder.restoreCallingIdentity(j);
                                        throw th;
                                    }
                                } else {
                                    filterComparison = filterComparison2;
                                    j = clearCallingIdentity;
                                }
                                if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                                    Slog.v(TAG_SERVICE, "Bound intent: " + connectionRecord.binding.intent.intent);
                                }
                                if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                                    Slog.v(TAG_SERVICE, "Published intent: " + intent);
                                }
                            } else {
                                filterComparison = filterComparison2;
                                j = clearCallingIdentity;
                                if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                                    Slog.v(TAG_SERVICE, "Publishing to: " + connectionRecord);
                                }
                                ComponentName componentName = connectionRecord.aliasComponent;
                                if (componentName == null) {
                                    componentName = serviceRecord.name;
                                }
                                if (z && size > connections.size() - 20 && size < connections.size() - 1) {
                                    Slog.v("ActivityManager", "Publishing to: " + size + " " + connectionRecord);
                                }
                                if (z2 && i2 > valueAt.size() - 20) {
                                    if (i2 < valueAt.size() - 1) {
                                        Slog.v("ActivityManager", "Publishing to: " + valueAt + " " + connectionRecord);
                                    }
                                }
                                try {
                                    try {
                                        connectionRecord.conn.connected(componentName, iBinder, false);
                                    } catch (Exception e) {
                                        e = e;
                                        Slog.w("ActivityManager", "Failure sending service " + serviceRecord.shortInstanceName + " to connection " + connectionRecord.conn.asBinder() + " (in " + connectionRecord.binding.client.processName + ")", e);
                                        i2++;
                                        str = str2;
                                        filterComparison2 = filterComparison;
                                        clearCallingIdentity = j;
                                    }
                                } catch (Exception e2) {
                                    e = e2;
                                }
                            }
                            i2++;
                            str = str2;
                            filterComparison2 = filterComparison;
                            clearCallingIdentity = j;
                        }
                        size--;
                        z3 = true;
                        clearCallingIdentity = clearCallingIdentity;
                        i = 1000;
                    }
                }
                j = clearCallingIdentity;
                serviceDoneExecutingLocked(serviceRecord, this.mDestroyingServices.contains(serviceRecord), false, false, 20);
            } else {
                j = clearCallingIdentity;
            }
            Binder.restoreCallingIdentity(j);
        } catch (Throwable th2) {
            th = th2;
            j = clearCallingIdentity;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateServiceGroupLocked(IServiceConnection iServiceConnection, int i, int i2) {
        IBinder asBinder = iServiceConnection.asBinder();
        if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
            Slog.v(TAG_SERVICE, "updateServiceGroup: conn=" + asBinder);
        }
        ArrayList<ConnectionRecord> arrayList = this.mServiceConnections.get(asBinder);
        if (arrayList == null) {
            throw new IllegalArgumentException("Could not find connection for " + iServiceConnection.asBinder());
        }
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            ServiceRecord serviceRecord = arrayList.get(size).binding.service;
            if (serviceRecord != null && (serviceRecord.serviceInfo.flags & 2) != 0) {
                ProcessRecord processRecord = serviceRecord.app;
                if (processRecord != null) {
                    ProcessServiceRecord processServiceRecord = processRecord.mServices;
                    if (i > 0) {
                        processServiceRecord.setConnectionService(serviceRecord);
                        processServiceRecord.setConnectionGroup(i);
                        processServiceRecord.setConnectionImportance(i2);
                    } else {
                        processServiceRecord.setConnectionService(null);
                        processServiceRecord.setConnectionGroup(0);
                        processServiceRecord.setConnectionImportance(0);
                    }
                } else if (i > 0) {
                    serviceRecord.pendingConnectionGroup = i;
                    serviceRecord.pendingConnectionImportance = i2;
                } else {
                    serviceRecord.pendingConnectionGroup = 0;
                    serviceRecord.pendingConnectionImportance = 0;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean unbindServiceLocked(IServiceConnection iServiceConnection) {
        String num;
        IBinder asBinder = iServiceConnection.asBinder();
        if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
            Slog.v(TAG_SERVICE, "unbindService: conn=" + asBinder);
        }
        ArrayList<ConnectionRecord> arrayList = this.mServiceConnections.get(asBinder);
        if (arrayList == null) {
            Slog.w("ActivityManager", "Unbind failed: could not find connection for " + iServiceConnection.asBinder());
            return false;
        }
        int callingPid = Binder.getCallingPid();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (Trace.isTagEnabled(64L)) {
                if (arrayList.size() > 0) {
                    ConnectionRecord connectionRecord = arrayList.get(0);
                    num = connectionRecord.binding.service.shortInstanceName + " from " + connectionRecord.clientProcessName;
                } else {
                    num = Integer.toString(callingPid);
                }
                Trace.traceBegin(64L, "unbindServiceLocked: " + num);
            }
            while (arrayList.size() > 0) {
                ConnectionRecord connectionRecord2 = arrayList.get(0);
                removeConnectionLocked(connectionRecord2, null, null, true);
                if (arrayList.size() > 0 && arrayList.get(0) == connectionRecord2) {
                    Slog.wtf("ActivityManager", "Connection " + connectionRecord2 + " not removed for binder " + asBinder);
                    arrayList.remove(0);
                }
                ProcessRecord processRecord = connectionRecord2.binding.service.app;
                if (processRecord != null) {
                    ProcessServiceRecord processServiceRecord = processRecord.mServices;
                    if (processServiceRecord.mAllowlistManager) {
                        updateAllowlistManagerLocked(processServiceRecord);
                    }
                    if (connectionRecord2.hasFlag(AudioFormat.OPUS)) {
                        processServiceRecord.setTreatLikeActivity(true);
                        this.mAm.updateLruProcessLocked(processRecord, true, null);
                    }
                    this.mAm.enqueueOomAdjTargetLocked(processRecord);
                }
            }
            this.mAm.updateOomAdjPendingTargetsLocked(5);
            return true;
        } finally {
            Trace.traceEnd(64L);
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unbindFinishedLocked(ServiceRecord serviceRecord, Intent intent, boolean z) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        if (serviceRecord != null) {
            try {
                IntentBindRecord intentBindRecord = serviceRecord.bindings.get(new Intent.FilterComparison(intent));
                boolean z2 = false;
                if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                    String str = TAG_SERVICE;
                    StringBuilder sb = new StringBuilder();
                    sb.append("unbindFinished in ");
                    sb.append(serviceRecord);
                    sb.append(" at ");
                    sb.append(intentBindRecord);
                    sb.append(": apps=");
                    sb.append(intentBindRecord != null ? intentBindRecord.apps.size() : 0);
                    Slog.v(str, sb.toString());
                }
                boolean contains = this.mDestroyingServices.contains(serviceRecord);
                if (intentBindRecord != null) {
                    if (intentBindRecord.apps.size() > 0 && !contains) {
                        for (int size = intentBindRecord.apps.size() - 1; size >= 0; size--) {
                            ProcessRecord processRecord = intentBindRecord.apps.valueAt(size).client;
                            if (processRecord != null && processRecord.mState.getSetSchedGroup() != 0) {
                                z2 = true;
                                break;
                            }
                        }
                        try {
                            requestServiceBindingLocked(serviceRecord, intentBindRecord, z2, true);
                        } catch (TransactionTooLargeException unused) {
                        }
                    } else {
                        intentBindRecord.doRebind = true;
                    }
                }
                serviceDoneExecutingLocked(serviceRecord, contains, false, false, 5);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    private final ServiceRecord findServiceLocked(ComponentName componentName, IBinder iBinder, int i) {
        ServiceRecord serviceByNameLocked = getServiceByNameLocked(componentName, i);
        if (serviceByNameLocked == iBinder) {
            return serviceByNameLocked;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class ServiceLookupResult {
        final ComponentName aliasComponent;
        final String permission;
        final ServiceRecord record;

        ServiceLookupResult(ServiceRecord serviceRecord, ComponentName componentName) {
            this.record = serviceRecord;
            this.permission = null;
            this.aliasComponent = componentName;
        }

        ServiceLookupResult(String str) {
            this.record = null;
            this.permission = str;
            this.aliasComponent = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class ServiceRestarter implements Runnable {
        private ServiceRecord mService;

        private ServiceRestarter() {
        }

        void setService(ServiceRecord serviceRecord) {
            this.mService = serviceRecord;
        }

        @Override // java.lang.Runnable
        public void run() {
            ActivityManagerService activityManagerService = ActiveServices.this.mAm;
            ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    ActiveServices.this.performServiceRestartLocked(this.mService);
                } catch (Throwable th) {
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            ActivityManagerService.resetPriorityAfterLockedSection();
        }
    }

    private ServiceLookupResult retrieveServiceLocked(Intent intent, String str, String str2, String str3, int i, int i2, int i3, boolean z, boolean z2, boolean z3, boolean z4, boolean z5) {
        return retrieveServiceLocked(intent, str, false, -1, null, str2, str3, i, i2, i3, z, z2, z3, z4, null, z5);
    }

    private String generateAdditionalSeInfoFromService(Intent intent) {
        return (intent == null || intent.getAction() == null) ? "" : (intent.getAction().equals("android.service.voice.HotwordDetectionService") || intent.getAction().equals("android.service.voice.VisualQueryDetectionService") || intent.getAction().equals("android.service.wearable.WearableSensingService")) ? ":isolatedComputeApp" : "";
    }

    /* JADX WARN: Code restructure failed: missing block: B:93:0x0392, code lost:
    
        if ((r6.flags & 2) != 0) goto L113;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x039c, code lost:
    
        throw new java.lang.IllegalArgumentException("Service cannot be both sdk sandbox and isolated");
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:215:0x078b  */
    /* JADX WARN: Removed duplicated region for block: B:255:0x095d A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:317:0x077a  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x02a0  */
    /* JADX WARN: Type inference failed for: r14v5, types: [com.android.server.am.ActivityManagerService] */
    /* JADX WARN: Type inference failed for: r18v10 */
    /* JADX WARN: Type inference failed for: r18v12 */
    /* JADX WARN: Type inference failed for: r18v13 */
    /* JADX WARN: Type inference failed for: r18v14 */
    /* JADX WARN: Type inference failed for: r18v16 */
    /* JADX WARN: Type inference failed for: r18v19 */
    /* JADX WARN: Type inference failed for: r18v2 */
    /* JADX WARN: Type inference failed for: r18v21 */
    /* JADX WARN: Type inference failed for: r18v3 */
    /* JADX WARN: Type inference failed for: r18v4 */
    /* JADX WARN: Type inference failed for: r18v5 */
    /* JADX WARN: Type inference failed for: r18v7 */
    /* JADX WARN: Type inference failed for: r18v9 */
    /* JADX WARN: Type inference failed for: r7v11, types: [int] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private ServiceLookupResult retrieveServiceLocked(Intent intent, String str, boolean z, int i, String str2, String str3, String str4, int i2, int i3, int i4, boolean z2, boolean z3, boolean z4, boolean z5, ForegroundServiceDelegationOptions foregroundServiceDelegationOptions, boolean z6) {
        ComponentName componentName;
        ServiceRecord serviceRecord;
        String str5;
        ComponentName componentName2;
        ServiceRecord serviceRecord2;
        String str6;
        ServiceRecord serviceRecord3;
        String str7;
        String str8;
        String str9;
        ComponentAliasResolver.Resolution<ComponentName> resolution;
        String str10;
        ServiceRecord serviceRecord4;
        int permissionToOpCode;
        long j;
        ComponentName componentName3;
        ServiceMap serviceMapLocked;
        Intent intent2;
        String str11;
        int i5;
        ServiceInfo serviceInfo;
        String str12;
        String str13;
        ComponentName componentName4;
        ComponentName componentName5;
        String str14;
        String str15;
        ServiceMap serviceMap;
        ServiceInfo serviceInfo2;
        ServiceRecord serviceRecord5;
        ServiceMap serviceMap2;
        int i6;
        String str16;
        ApplicationInfo applicationInfo;
        ServiceRecord serviceRecord6;
        if (z && str == null) {
            throw new IllegalArgumentException("No instanceName provided for sdk sandbox process");
        }
        intent.getIntentExt().setCallingUid(i3);
        if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
            Slog.v(TAG_SERVICE, "retrieveServiceLocked: " + intent + " type=" + str3 + " callingUid=" + i3);
        }
        int handleIncomingUser = this.mAm.mUserController.handleIncomingUser(i2, i3, i4, false, getAllowMode(intent, str4), HostingRecord.HOSTING_TYPE_SERVICE, str4);
        ServiceMap serviceMapLocked2 = getServiceMapLocked(handleIncomingUser);
        ComponentAliasResolver.Resolution<ComponentName> resolveService = this.mAm.mComponentAliasResolver.resolveService(intent, str3, 0, handleIncomingUser, i3);
        if (str == null) {
            componentName = intent.getComponent();
        } else {
            ComponentName component = intent.getComponent();
            if (component == null) {
                throw new IllegalArgumentException("Can't use custom instance name '" + str + "' without expicit component in Intent");
            }
            componentName = new ComponentName(component.getPackageName(), component.getClassName() + ":" + str);
        }
        if (componentName != null) {
            serviceRecord = serviceMapLocked2.mServicesByInstanceName.get(componentName);
            if (ActivityManagerDebugConfig.DEBUG_SERVICE && serviceRecord != null) {
                Slog.v(TAG_SERVICE, "Retrieved by component: " + serviceRecord);
            }
        } else {
            serviceRecord = null;
        }
        if (serviceRecord == null && !z4 && str == null) {
            ServiceRecord serviceRecord7 = serviceRecord;
            try {
                serviceRecord2 = serviceMapLocked2.mServicesByIntent.get(new Intent.FilterComparison(intent));
            } catch (ConcurrentModificationException unused) {
                Slog.e("ActivityManager", "smap.mServicesByIntent occur to ConcurrentModificationException");
                serviceRecord2 = serviceRecord7;
            }
            if (!ActivityManagerDebugConfig.DEBUG_SERVICE || serviceRecord2 == null) {
                str5 = "ActivityManager";
                componentName2 = componentName;
            } else {
                String str17 = TAG_SERVICE;
                str5 = "ActivityManager";
                StringBuilder sb = new StringBuilder();
                componentName2 = componentName;
                sb.append("Retrieved by intent: ");
                sb.append(serviceRecord2);
                Slog.v(str17, sb.toString());
            }
        } else {
            str5 = "ActivityManager";
            componentName2 = componentName;
            serviceRecord2 = serviceRecord;
        }
        if (serviceRecord2 != null) {
            str6 = ":";
            if (this.mAm.getPackageManagerInternal().filterAppAccess(serviceRecord2.packageName, i3, handleIncomingUser)) {
                Slog.w(TAG_SERVICE, "Unable to start service " + intent + " U=" + handleIncomingUser + ": not found");
                return null;
            }
            if ((serviceRecord2.serviceInfo.flags & 4) != 0 && !str4.equals(serviceRecord2.packageName)) {
                if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                    Slog.v(TAG_SERVICE, "Whoops, can't use existing external service");
                }
                serviceRecord3 = null;
                if (serviceRecord3 != null && foregroundServiceDelegationOptions != null) {
                    ServiceInfo serviceInfo3 = new ServiceInfo();
                    try {
                        applicationInfo = AppGlobals.getPackageManager().getApplicationInfo(foregroundServiceDelegationOptions.mClientPackageName, 1024L, handleIncomingUser);
                    } catch (RemoteException unused2) {
                        applicationInfo = null;
                    }
                    if (applicationInfo == null) {
                        throw new SecurityException("startForegroundServiceDelegate failed, could not resolve client package " + str4);
                    }
                    if (applicationInfo.uid != foregroundServiceDelegationOptions.mClientUid) {
                        throw new SecurityException("startForegroundServiceDelegate failed, uid:" + applicationInfo.uid + " does not match clientUid:" + foregroundServiceDelegationOptions.mClientUid);
                    }
                    serviceInfo3.applicationInfo = applicationInfo;
                    serviceInfo3.packageName = applicationInfo.packageName;
                    serviceInfo3.mForegroundServiceType = foregroundServiceDelegationOptions.mForegroundServiceTypes;
                    serviceInfo3.processName = applicationInfo.processName;
                    ComponentName component2 = intent.getComponent();
                    serviceInfo3.name = component2.getClassName();
                    if (z2) {
                        Intent.FilterComparison filterComparison = new Intent.FilterComparison(intent.cloneFilter());
                        ServiceRestarter serviceRestarter = new ServiceRestarter();
                        String processNameForService = getProcessNameForService(serviceInfo3, component2, str4, null, false, false);
                        ActivityManagerService activityManagerService = this.mAm;
                        ApplicationInfo applicationInfo2 = serviceInfo3.applicationInfo;
                        serviceRecord6 = new ServiceRecord(activityManagerService, component2, component2, applicationInfo2.packageName, applicationInfo2.uid, filterComparison, serviceInfo3, z3, serviceRestarter, processNameForService, -1, null, false);
                        serviceRestarter.setService(serviceRecord6);
                        serviceMapLocked2.mServicesByInstanceName.put(component2, serviceRecord6);
                        serviceMapLocked2.mServicesByIntent.put(filterComparison, serviceRecord6);
                        if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                            Slog.v(TAG_SERVICE, "Retrieve created new service: " + serviceRecord6);
                        }
                        serviceRecord6.mRecentCallingPackage = str4;
                        serviceRecord6.mRecentCallingUid = i3;
                    } else {
                        serviceRecord6 = serviceRecord3;
                    }
                    StringBuilder sb2 = new StringBuilder();
                    ApplicationInfo applicationInfo3 = serviceRecord6.appInfo;
                    sb2.append(applicationInfo3.seInfo);
                    sb2.append(generateAdditionalSeInfoFromService(intent));
                    applicationInfo3.seInfo = sb2.toString();
                    return new ServiceLookupResult(serviceRecord6, resolveService.getAlias());
                }
                if (serviceRecord3 != null) {
                    try {
                        j = z5 ? 276825088 : 268436480;
                        resolution = resolveService;
                        componentName3 = componentName2;
                        str10 = " and ";
                        str8 = str4;
                        try {
                            ResolveInfo resolveService2 = this.mAm.getPackageManagerInternal().resolveService(intent, str3, j, handleIncomingUser, i3);
                            if (this.mActiveServicesExt.hookRetrieveServiceChangeUserIdToSystemIfNeed(resolveService2, i3, handleIncomingUser)) {
                                try {
                                    resolveService2 = this.mAm.getPackageManagerInternal().resolveService(intent, str3, j, 0, i3);
                                    serviceMapLocked = getServiceMapLocked(0);
                                    String str18 = TAG_SERVICE;
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append("multi app -> generateServiceInfo of [");
                                    intent2 = intent;
                                    str11 = "Retrieve created new service: ";
                                    try {
                                        sb3.append(intent2);
                                        sb3.append("] for ");
                                        sb3.append(i3);
                                        sb3.append(")");
                                        Slog.d(str18, sb3.toString());
                                        i5 = i3;
                                        handleIncomingUser = 0;
                                    } catch (RemoteException unused3) {
                                        str6 = "association not allowed between packages ";
                                        str9 = "Service lookup failed: ";
                                        str7 = str10;
                                        str10 = str5;
                                        serviceRecord4 = serviceRecord3;
                                        if (serviceRecord4 != null) {
                                        }
                                    }
                                } catch (RemoteException unused4) {
                                    str6 = "association not allowed between packages ";
                                    str9 = "Service lookup failed: ";
                                    str7 = str10;
                                    str10 = str5;
                                    serviceRecord4 = serviceRecord3;
                                    if (serviceRecord4 != null) {
                                    }
                                }
                            } else {
                                i5 = i3;
                                str11 = "Retrieve created new service: ";
                                intent2 = intent;
                                serviceMapLocked = serviceMapLocked2;
                            }
                            serviceInfo = resolveService2 != null ? resolveService2.serviceInfo : null;
                        } catch (RemoteException unused5) {
                        }
                    } catch (RemoteException unused6) {
                        str7 = " and ";
                        str8 = str4;
                        str6 = "association not allowed between packages ";
                        str9 = "Service lookup failed: ";
                        resolution = resolveService;
                    }
                    if (serviceInfo == null) {
                        Slog.w(TAG_SERVICE, "Unable to start service " + intent2 + " U=" + handleIncomingUser + ": not found");
                        return null;
                    }
                    if (str != null && (serviceInfo.flags & 2) == 0 && !z) {
                        throw new IllegalArgumentException("Can't use instance name '" + str + "' with non-isolated non-sdk sandbox service '" + serviceInfo.name + "'");
                    }
                    ComponentName componentName6 = new ComponentName(serviceInfo.applicationInfo.packageName, serviceInfo.name);
                    ComponentName componentName7 = componentName3 != null ? componentName3 : componentName6;
                    ?? r14 = this.mAm;
                    ServiceMap serviceMap3 = serviceMapLocked;
                    String packageName = componentName7.getPackageName();
                    String str19 = str11;
                    ?? r7 = serviceInfo.applicationInfo.uid;
                    try {
                        try {
                        } catch (RemoteException unused7) {
                            str6 = "association not allowed between packages ";
                            str12 = str10;
                            str9 = r14;
                        }
                    } catch (RemoteException unused8) {
                        str6 = "association not allowed between packages ";
                        str7 = str10;
                        str9 = r14;
                        str10 = r7;
                        serviceRecord4 = serviceRecord3;
                        if (serviceRecord4 != null) {
                        }
                    }
                    if (!r14.validateAssociationAllowedLocked(str8, i5, packageName, r7)) {
                        String str20 = "association not allowed between packages " + str8 + str10 + componentName7.getPackageName();
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("Service lookup failed: ");
                        sb4.append(str20);
                        Slog.w(str5, sb4.toString());
                        return new ServiceLookupResult(str20);
                    }
                    String str21 = str5;
                    ApplicationInfo applicationInfo4 = serviceInfo.applicationInfo;
                    str5 = str21;
                    String str22 = applicationInfo4.packageName;
                    int i7 = applicationInfo4.uid;
                    int i8 = serviceInfo.flags;
                    str7 = i8 & 4;
                    str9 = "Service lookup failed: ";
                    try {
                        try {
                            if (str7 == 0) {
                                str7 = str10;
                                str13 = ": not found";
                                if (z4) {
                                    throw new SecurityException("BIND_EXTERNAL_SERVICE failed, " + componentName7 + " is not an externalService");
                                }
                                componentName4 = componentName6;
                                componentName5 = componentName7;
                            } else if (z4) {
                                str7 = str10;
                                if (!serviceInfo.exported) {
                                    throw new SecurityException("BIND_EXTERNAL_SERVICE failed, " + componentName6 + " is not exported");
                                }
                                if ((i8 & 2) == 0) {
                                    throw new SecurityException("BIND_EXTERNAL_SERVICE failed, " + componentName6 + " is not an isolatedProcess");
                                }
                                if (!this.mAm.getPackageManagerInternal().isSameApp(str8, i5, handleIncomingUser)) {
                                    throw new SecurityException("BIND_EXTERNAL_SERVICE failed, calling package not owned by calling UID ");
                                }
                                str13 = ": not found";
                                ApplicationInfo applicationInfo5 = AppGlobals.getPackageManager().getApplicationInfo(str8, 1024L, handleIncomingUser);
                                if (applicationInfo5 == null) {
                                    throw new SecurityException("BIND_EXTERNAL_SERVICE failed, could not resolve client package " + str8);
                                }
                                ServiceInfo serviceInfo4 = new ServiceInfo(serviceInfo);
                                ApplicationInfo applicationInfo6 = new ApplicationInfo(serviceInfo4.applicationInfo);
                                serviceInfo4.applicationInfo = applicationInfo6;
                                applicationInfo6.packageName = applicationInfo5.packageName;
                                applicationInfo6.uid = applicationInfo5.uid;
                                ComponentName componentName8 = new ComponentName(applicationInfo5.packageName, componentName7.getClassName());
                                ComponentName componentName9 = new ComponentName(applicationInfo5.packageName, str == null ? componentName6.getClassName() : componentName6.getClassName() + str6 + str);
                                intent2.setComponent(componentName8);
                                componentName4 = componentName9;
                                componentName5 = componentName8;
                                serviceInfo = serviceInfo4;
                            } else {
                                throw new SecurityException("BIND_EXTERNAL_SERVICE required for " + componentName7);
                            }
                            if (z6) {
                                int i9 = serviceInfo.flags;
                                if ((i9 & 2) == 0) {
                                    throw new SecurityException("BIND_SHARED_ISOLATED_PROCESS failed, " + componentName4 + " is not an isolatedProcess");
                                }
                                if ((i9 & 16) == 0) {
                                    throw new SecurityException("BIND_SHARED_ISOLATED_PROCESS failed, " + componentName4 + " has not set the allowSharedIsolatedProcess  attribute.");
                                }
                                if (str == null) {
                                    throw new IllegalArgumentException("instanceName must be provided for binding a service into a shared isolated process.");
                                }
                            }
                            if (handleIncomingUser > 0) {
                                try {
                                    if (this.mAm.isSingleton(serviceInfo.processName, serviceInfo.applicationInfo, serviceInfo.name, serviceInfo.flags) && this.mAm.isValidSingletonCall(i5, serviceInfo.applicationInfo.uid)) {
                                        ServiceMap serviceMapLocked3 = getServiceMapLocked(0);
                                        long clearCallingIdentity = Binder.clearCallingIdentity();
                                        try {
                                            str16 = str13;
                                            str10 = str5;
                                            str14 = str22;
                                            str15 = str19;
                                            str6 = "association not allowed between packages ";
                                        } catch (Throwable th) {
                                            th = th;
                                        }
                                        try {
                                            ResolveInfo resolveService3 = this.mAm.getPackageManagerInternal().resolveService(intent, str3, j, 0, i3);
                                            if (resolveService3 == null) {
                                                Slog.w(TAG_SERVICE, "Unable to resolve service " + intent + " U=0" + str16);
                                                Binder.restoreCallingIdentity(clearCallingIdentity);
                                                return null;
                                            }
                                            serviceInfo = resolveService3.serviceInfo;
                                            Binder.restoreCallingIdentity(clearCallingIdentity);
                                            serviceMap2 = serviceMapLocked3;
                                            i6 = 0;
                                        } catch (Throwable th2) {
                                            th = th2;
                                            Binder.restoreCallingIdentity(clearCallingIdentity);
                                            throw th;
                                        }
                                    } else {
                                        str6 = "association not allowed between packages ";
                                        int i10 = handleIncomingUser;
                                        str10 = str5;
                                        str14 = str22;
                                        str15 = str19;
                                        serviceMap2 = serviceMap3;
                                        i6 = i10;
                                    }
                                    ServiceInfo serviceInfo5 = new ServiceInfo(serviceInfo);
                                    serviceInfo5.applicationInfo = this.mAm.getAppInfoForUser(serviceInfo5.applicationInfo, i6);
                                    serviceInfo2 = serviceInfo5;
                                    serviceMap = serviceMap2;
                                } catch (RemoteException unused9) {
                                    str6 = "association not allowed between packages ";
                                    str7 = str7;
                                }
                            } else {
                                str6 = "association not allowed between packages ";
                                str10 = str5;
                                str14 = str22;
                                str15 = str19;
                                serviceMap = serviceMap3;
                                serviceInfo2 = serviceInfo;
                            }
                            serviceRecord5 = serviceMap.mServicesByInstanceName.get(componentName5);
                            try {
                                if (ActivityManagerDebugConfig.DEBUG_SERVICE && serviceRecord5 != null) {
                                    Slog.v(TAG_SERVICE, "Retrieved via pm by intent: " + serviceRecord5);
                                }
                            } catch (RemoteException unused10) {
                            }
                        } catch (RemoteException unused11) {
                            str6 = "association not allowed between packages ";
                            str12 = str7;
                            str10 = str5;
                            str7 = str12;
                            serviceRecord4 = serviceRecord3;
                            if (serviceRecord4 != null) {
                            }
                        }
                    } catch (RemoteException unused12) {
                    }
                    if (serviceRecord5 == null && z2) {
                        Intent.FilterComparison filterComparison2 = new Intent.FilterComparison(intent.cloneFilter());
                        ServiceRestarter serviceRestarter2 = new ServiceRestarter();
                        ServiceRecord serviceRecord8 = new ServiceRecord(this.mAm, componentName4, componentName5, str14, i7, filterComparison2, serviceInfo2, z3, serviceRestarter2, getProcessNameForService(serviceInfo2, componentName5, str4, str, z, z6), i, str2, z6);
                        try {
                            serviceRestarter2.setService(serviceRecord8);
                            serviceMap.mServicesByInstanceName.put(componentName5, serviceRecord8);
                            try {
                                serviceMap.mServicesByIntent.put(filterComparison2, serviceRecord8);
                            } catch (ConcurrentModificationException unused13) {
                                Slog.e(str10, "smap.mServicesByIntent occur to ConcurrentModificationException");
                            }
                            for (int size = this.mPendingServices.size() - 1; size >= 0; size--) {
                                ServiceRecord serviceRecord9 = this.mPendingServices.get(size);
                                if (serviceRecord9.serviceInfo.applicationInfo.uid == serviceInfo2.applicationInfo.uid && serviceRecord9.instanceName.equals(componentName5)) {
                                    if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                                        Slog.v(TAG_SERVICE, "Remove pending: " + serviceRecord9);
                                    }
                                    this.mPendingServices.remove(size);
                                }
                            }
                            for (int size2 = this.mPendingBringups.size() - 1; size2 >= 0; size2--) {
                                ServiceRecord keyAt = this.mPendingBringups.keyAt(size2);
                                if (keyAt.serviceInfo.applicationInfo.uid == serviceInfo2.applicationInfo.uid && keyAt.instanceName.equals(componentName5)) {
                                    if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                                        Slog.v(TAG_SERVICE, "Remove pending bringup: " + keyAt);
                                    }
                                    this.mPendingBringups.removeAt(size2);
                                }
                            }
                            if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                                Slog.v(TAG_SERVICE, str15 + serviceRecord8);
                            }
                        } catch (RemoteException unused14) {
                        }
                        serviceRecord3 = serviceRecord8;
                        serviceRecord4 = serviceRecord3;
                        if (serviceRecord4 != null) {
                            return null;
                        }
                        serviceRecord4.mRecentCallingPackage = str8;
                        serviceRecord4.mRecentCallingUid = i3;
                        try {
                            serviceRecord4.mRecentCallerApplicationInfo = this.mAm.mContext.getPackageManager().getApplicationInfoAsUser(str8, 0, UserHandle.getUserId(i3));
                        } catch (PackageManager.NameNotFoundException unused15) {
                        }
                        if (!this.mAm.validateAssociationAllowedLocked(str8, i3, serviceRecord4.packageName, serviceRecord4.appInfo.uid)) {
                            String str23 = str6 + str8 + str7 + serviceRecord4.packageName;
                            Slog.w(str10, str9 + str23);
                            return new ServiceLookupResult(str23);
                        }
                        if (!this.mAm.mIntentFirewall.checkService(serviceRecord4.name, intent, i3, i2, str3, serviceRecord4.appInfo)) {
                            return new ServiceLookupResult("blocked by firewall");
                        }
                        String str24 = str10;
                        if (ActivityManagerService.checkComponentPermission(serviceRecord4.permission, i2, i3, serviceRecord4.appInfo.uid, serviceRecord4.exported) != 0) {
                            if (!serviceRecord4.exported) {
                                Slog.w(str24, "Permission Denial: Accessing service " + serviceRecord4.shortInstanceName + " from pid=" + i2 + ", uid=" + i3 + " that is not exported from uid " + serviceRecord4.appInfo.uid);
                                return new ServiceLookupResult("not exported from uid " + serviceRecord4.appInfo.uid);
                            }
                            Slog.w(str24, "Permission Denial: Accessing service " + serviceRecord4.shortInstanceName + " from pid=" + i2 + ", uid=" + i3 + " requires " + serviceRecord4.permission);
                            return new ServiceLookupResult(serviceRecord4.permission);
                        }
                        if (("android.permission.BIND_HOTWORD_DETECTION_SERVICE".equals(serviceRecord4.permission) || "android.permission.BIND_VISUAL_QUERY_DETECTION_SERVICE".equals(serviceRecord4.permission)) && i3 != 1000) {
                            Slog.w(str24, "Permission Denial: Accessing service " + serviceRecord4.shortInstanceName + " from pid=" + i2 + ", uid=" + i3 + " requiring permission " + serviceRecord4.permission + " can only be bound to from the system.");
                            return new ServiceLookupResult("can only be bound to by the system.");
                        }
                        String str25 = serviceRecord4.permission;
                        if (str25 != null && str8 != null && (permissionToOpCode = AppOpsManager.permissionToOpCode(str25)) != -1 && this.mAm.getAppOpsManager().checkOpNoThrow(permissionToOpCode, i3, str8) != 0) {
                            Slog.w(str24, "Appop Denial: Accessing service " + serviceRecord4.shortInstanceName + " from pid=" + i2 + ", uid=" + i3 + " requires appop " + AppOpsManager.opToName(permissionToOpCode));
                            return null;
                        }
                        StringBuilder sb5 = new StringBuilder();
                        ApplicationInfo applicationInfo7 = serviceRecord4.appInfo;
                        sb5.append(applicationInfo7.seInfo);
                        sb5.append(generateAdditionalSeInfoFromService(intent));
                        applicationInfo7.seInfo = sb5.toString();
                        return new ServiceLookupResult(serviceRecord4, resolution.getAlias());
                    }
                    serviceRecord3 = serviceRecord5;
                    serviceRecord4 = serviceRecord3;
                    if (serviceRecord4 != null) {
                    }
                } else {
                    str7 = " and ";
                    str8 = str4;
                    str6 = "association not allowed between packages ";
                    str9 = "Service lookup failed: ";
                    resolution = resolveService;
                }
                str10 = str5;
                serviceRecord4 = serviceRecord3;
                if (serviceRecord4 != null) {
                }
            }
        } else {
            str6 = ":";
        }
        serviceRecord3 = serviceRecord2;
        if (serviceRecord3 != null) {
        }
        if (serviceRecord3 != null) {
        }
        str10 = str5;
        serviceRecord4 = serviceRecord3;
        if (serviceRecord4 != null) {
        }
    }

    private int getAllowMode(Intent intent, String str) {
        return (str == null || intent.getComponent() == null || !str.equals(intent.getComponent().getPackageName())) ? 1 : 3;
    }

    private boolean bumpServiceExecutingLocked(ServiceRecord serviceRecord, boolean z, String str, int i) {
        boolean z2;
        ProcessRecord processRecord;
        ProcessRecord processRecord2;
        if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
            Slog.v(TAG_SERVICE, ">>> EXECUTING " + str + " of " + serviceRecord + " in app " + serviceRecord.app);
        } else if (ActivityManagerDebugConfig.DEBUG_SERVICE_EXECUTING) {
            Slog.v(TAG_SERVICE_EXECUTING, ">>> EXECUTING " + str + " of " + serviceRecord.shortInstanceName);
        }
        boolean z3 = false;
        if (this.mAm.mBootPhase >= 600 || (processRecord2 = serviceRecord.app) == null || processRecord2.getPid() != ActivityManagerService.MY_PID) {
            z2 = true;
        } else {
            Slog.w("ActivityManager", "Too early to start/bind service in system_server: Phase=" + this.mAm.mBootPhase + " " + serviceRecord.getComponentName());
            z2 = false;
        }
        if (z2 && this.mActiveServicesExt.setTimeoutNeededToFalseIfNeed(serviceRecord, z, str)) {
            z2 = false;
        }
        if (serviceRecord.executeNesting == 0) {
            serviceRecord.executeFg = z;
            synchronized (this.mAm.mProcessStats.mLock) {
                ServiceState tracker = serviceRecord.getTracker();
                if (tracker != null) {
                    tracker.setExecuting(true, this.mAm.mProcessStats.getMemFactorLocked(), SystemClock.uptimeMillis());
                }
            }
            ProcessRecord processRecord3 = serviceRecord.app;
            if (processRecord3 != null) {
                ProcessServiceRecord processServiceRecord = processRecord3.mServices;
                processServiceRecord.startExecutingService(serviceRecord);
                processServiceRecord.setExecServicesFg(processServiceRecord.shouldExecServicesFg() || z);
                if (z2 && processServiceRecord.numberOfExecutingServices() == 1) {
                    scheduleServiceTimeoutLocked(serviceRecord.app);
                }
            }
        } else {
            ProcessRecord processRecord4 = serviceRecord.app;
            if (processRecord4 != null && z) {
                ProcessServiceRecord processServiceRecord2 = processRecord4.mServices;
                if (!processServiceRecord2.shouldExecServicesFg()) {
                    processServiceRecord2.setExecServicesFg(true);
                    if (z2) {
                        scheduleServiceTimeoutLocked(serviceRecord.app);
                    }
                }
            }
        }
        if (i != 0 && (processRecord = serviceRecord.app) != null && processRecord.mState.getCurProcState() > 10) {
            this.mAm.enqueueOomAdjTargetLocked(serviceRecord.app);
            this.mAm.updateOomAdjPendingTargetsLocked(i);
            z3 = true;
        }
        serviceRecord.executeFg |= z;
        serviceRecord.executeNesting++;
        serviceRecord.executingStart = SystemClock.uptimeMillis();
        return z3;
    }

    private final boolean requestServiceBindingLocked(ServiceRecord serviceRecord, IntentBindRecord intentBindRecord, boolean z, boolean z2) throws TransactionTooLargeException {
        ProcessRecord processRecord = serviceRecord.app;
        if (processRecord == null || processRecord.getThread() == null) {
            return false;
        }
        if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
            Slog.d(TAG_SERVICE, "requestBind " + intentBindRecord + ": requested=" + intentBindRecord.requested + " rebind=" + z2);
        }
        if ((!intentBindRecord.requested || z2) && intentBindRecord.apps.size() > 0) {
            try {
                bumpServiceExecutingLocked(serviceRecord, z, "bind", 4);
                if (Trace.isTagEnabled(64L)) {
                    Trace.instant(64L, "requestServiceBinding=" + intentBindRecord.intent.getIntent() + ". bindSeq=" + this.mBindServiceSeqCounter);
                }
                IApplicationThread thread = serviceRecord.app.getThread();
                Intent intent = intentBindRecord.intent.getIntent();
                int reportedProcState = serviceRecord.app.mState.getReportedProcState();
                long j = this.mBindServiceSeqCounter;
                this.mBindServiceSeqCounter = 1 + j;
                thread.scheduleBindService(serviceRecord, intent, z2, reportedProcState, j);
                if (!z2) {
                    intentBindRecord.requested = true;
                }
                intentBindRecord.hasBound = true;
                intentBindRecord.doRebind = false;
            } catch (TransactionTooLargeException e) {
                if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                    Slog.v(TAG_SERVICE, "Crashed while binding " + serviceRecord, e);
                }
                boolean contains = this.mDestroyingServices.contains(serviceRecord);
                serviceDoneExecutingLocked(serviceRecord, contains, contains, false, 5);
                throw e;
            } catch (RemoteException unused) {
                if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                    Slog.v(TAG_SERVICE, "Crashed while binding " + serviceRecord);
                }
                boolean contains2 = this.mDestroyingServices.contains(serviceRecord);
                serviceDoneExecutingLocked(serviceRecord, contains2, contains2, false, 5);
                return false;
            }
        }
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v12 */
    /* JADX WARN: Type inference failed for: r2v13, types: [int] */
    /* JADX WARN: Type inference failed for: r2v63 */
    private final boolean scheduleServiceRestartLocked(ServiceRecord serviceRecord, boolean z) {
        boolean z2;
        long j;
        String str;
        boolean z3;
        long j2;
        long j3;
        boolean z4;
        String str2;
        boolean z5;
        boolean z6;
        boolean z7;
        long j4;
        int i = 0;
        if (this.mAm.mAtmInternal.isShuttingDown()) {
            Slog.w("ActivityManager", "Not scheduling restart of crashed service " + serviceRecord.shortInstanceName + " - system is shutting down");
            return false;
        }
        ServiceMap serviceMapLocked = getServiceMapLocked(serviceRecord.userId);
        if (serviceMapLocked.mServicesByInstanceName.get(serviceRecord.instanceName) != serviceRecord) {
            Slog.wtf("ActivityManager", "Attempting to schedule restart of " + serviceRecord + " when found in map: " + serviceMapLocked.mServicesByInstanceName.get(serviceRecord.instanceName));
            return false;
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        int indexOf = this.mRestartingServices.indexOf(serviceRecord);
        boolean z8 = indexOf != -1;
        if ((serviceRecord.serviceInfo.applicationInfo.flags & 8) == 0) {
            if (this.mActiveServicesExt.delayRestartServices(serviceRecord, this.mAm)) {
                j2 = this.mAm.mConstants.SERVICE_RESTART_DURATION_LOW;
            } else {
                j2 = this.mAm.mConstants.SERVICE_RESTART_DURATION;
            }
            long j5 = this.mAm.mConstants.SERVICE_RESET_RUN_DURATION;
            int size = serviceRecord.deliveredStarts.size();
            if (DEBUG_DELAYED_SERVICE) {
                Slog.w("ActivityManager", " scheduleServiceRestartLocked  N " + size + " now " + uptimeMillis + " r " + serviceRecord + " minDuration " + j2);
            }
            if (size > 0) {
                int i2 = size - 1;
                z4 = false;
                while (i2 >= 0) {
                    ServiceRecord.StartItem startItem = serviceRecord.deliveredStarts.get(i2);
                    startItem.removeUriPermissionsLocked();
                    if (startItem.intent == null) {
                        j4 = uptimeMillis;
                    } else if (!z || (startItem.deliveryCount < 3 && startItem.doneExecutingCount < 6)) {
                        serviceRecord.pendingStarts.add(i, startItem);
                        j4 = uptimeMillis;
                        long uptimeMillis2 = (SystemClock.uptimeMillis() - startItem.deliveredTime) * 2;
                        if (j2 < uptimeMillis2) {
                            j2 = uptimeMillis2;
                        }
                        if (j5 < uptimeMillis2) {
                            j5 = uptimeMillis2;
                        }
                    } else {
                        Slog.w("ActivityManager", "Canceling start item " + startItem.intent + " in service " + serviceRecord.shortInstanceName);
                        j4 = uptimeMillis;
                        z4 = true;
                    }
                    i2--;
                    uptimeMillis = j4;
                    i = 0;
                }
                j3 = uptimeMillis;
                serviceRecord.deliveredStarts.clear();
            } else {
                j3 = uptimeMillis;
                z4 = false;
            }
            if (z) {
                boolean canStopIfKilled = serviceRecord.canStopIfKilled(z4);
                if (canStopIfKilled && !serviceRecord.hasAutoCreateConnections()) {
                    return false;
                }
                str2 = (!serviceRecord.startRequested || canStopIfKilled) ? "connection" : "start-requested";
            } else {
                str2 = "always";
            }
            String str3 = str2;
            serviceRecord.totalRestartCount++;
            long j6 = serviceRecord.restartDelay;
            if (j6 == 0) {
                serviceRecord.restartCount++;
                serviceRecord.restartDelay = j2;
            } else {
                if (serviceRecord.crashCount > 1) {
                    serviceRecord.restartDelay = this.mAm.mConstants.BOUND_SERVICE_CRASH_RESTART_DURATION * (r2 - 1);
                } else if (j3 > serviceRecord.restartTime + j5) {
                    serviceRecord.restartCount = 1;
                    serviceRecord.restartDelay = j2;
                } else {
                    long j7 = j6 * this.mAm.mConstants.SERVICE_RESTART_DURATION_FACTOR;
                    serviceRecord.restartDelay = j7;
                    if (j7 < j2) {
                        serviceRecord.restartDelay = j2;
                    }
                }
            }
            if (isServiceRestartBackoffEnabledLocked(serviceRecord.packageName)) {
                long j8 = j3 + serviceRecord.restartDelay;
                serviceRecord.mEarliestRestartTime = j8;
                serviceRecord.nextRestartTime = j8;
                if (z8) {
                    this.mRestartingServices.remove(indexOf);
                    z6 = false;
                } else {
                    z6 = z8;
                }
                if (this.mRestartingServices.isEmpty()) {
                    long max = Math.max(j3 + getExtraRestartTimeInBetweenLocked(), serviceRecord.nextRestartTime);
                    serviceRecord.nextRestartTime = max;
                    serviceRecord.restartDelay = max - j3;
                } else {
                    long extraRestartTimeInBetweenLocked = this.mAm.mConstants.SERVICE_MIN_RESTART_TIME_BETWEEN + getExtraRestartTimeInBetweenLocked();
                    if (DEBUG_DELAYED_SERVICE || DEBUG_PANIC_FLAG) {
                        Slog.d("ActivityManager", "scheduleServiceRestartLocked do-while start mRestartingServices.size =" + this.mRestartingServices.size());
                    }
                    do {
                        long j9 = serviceRecord.nextRestartTime;
                        for (int size2 = this.mRestartingServices.size() - 1; size2 >= 0; size2--) {
                            long j10 = this.mRestartingServices.get(size2).nextRestartTime;
                            if (j9 >= j10 - extraRestartTimeInBetweenLocked) {
                                long j11 = j10 + extraRestartTimeInBetweenLocked;
                                if (j9 < j11) {
                                    serviceRecord.nextRestartTime = j11;
                                    serviceRecord.restartDelay = j11 - j3;
                                    z7 = true;
                                    break;
                                }
                            }
                            if (j9 >= j10 + extraRestartTimeInBetweenLocked) {
                                break;
                            }
                        }
                        z7 = false;
                    } while (z7);
                    if (DEBUG_DELAYED_SERVICE || DEBUG_PANIC_FLAG) {
                        Slog.d("ActivityManager", "scheduleServiceRestartLocked do-while end");
                    }
                }
                z5 = z6;
            } else {
                long j12 = this.mAm.mConstants.SERVICE_RESTART_DURATION;
                serviceRecord.restartDelay = j12;
                serviceRecord.nextRestartTime = j3 + j12;
                z5 = z8;
            }
            this.mActiveServicesExt.hookScheduleServiceRestart(serviceRecord, j3, j2);
            str = str3;
            z8 = z5;
            j = j3;
            z2 = false;
        } else {
            serviceRecord.totalRestartCount++;
            z2 = false;
            serviceRecord.restartCount = 0;
            serviceRecord.restartDelay = 0L;
            serviceRecord.mEarliestRestartTime = 0L;
            j = uptimeMillis;
            serviceRecord.nextRestartTime = j;
            str = "persistent";
        }
        serviceRecord.mRestartSchedulingTime = j;
        if (!z8) {
            if (indexOf == -1) {
                serviceRecord.createdFromFg = z2;
                synchronized (this.mAm.mProcessStats.mLock) {
                    serviceRecord.makeRestarting(this.mAm.mProcessStats.getMemFactorLocked(), SystemClock.uptimeMillis());
                }
            }
            int size3 = this.mRestartingServices.size();
            ?? r2 = z2;
            while (true) {
                if (r2 >= size3) {
                    z3 = z2;
                    break;
                }
                if (this.mRestartingServices.get(r2).nextRestartTime > serviceRecord.nextRestartTime) {
                    this.mRestartingServices.add(r2, serviceRecord);
                    z3 = true;
                    break;
                }
                r2++;
            }
            if (!z3) {
                this.mRestartingServices.add(serviceRecord);
            }
        }
        cancelForegroundNotificationLocked(serviceRecord);
        if (DEBUG_DELAYED_SERVICE) {
            Slog.w("ActivityManager", "r " + serviceRecord + " r.restartDelay " + serviceRecord.restartDelay + " r.nextRestartTime " + serviceRecord.nextRestartTime);
        }
        performScheduleRestartLocked(serviceRecord, "Scheduling", str, j);
        return true;
    }

    @GuardedBy({"mAm"})
    void performScheduleRestartLocked(ServiceRecord serviceRecord, String str, String str2, long j) {
        if (serviceRecord.fgRequired && serviceRecord.fgWaiting) {
            this.mAm.mHandler.removeMessages(66, serviceRecord);
            serviceRecord.fgWaiting = false;
            this.mActiveServicesExt.updateExecutingComponent(serviceRecord.appInfo.uid, "fg-service", 2);
        }
        this.mAm.mHandler.removeCallbacks(serviceRecord.restarter);
        this.mAm.mHandler.postAtTime(serviceRecord.restarter, serviceRecord.nextRestartTime);
        serviceRecord.nextRestartTime = j + serviceRecord.restartDelay;
        if (DEBUG_DELAYED_SERVICE || DEBUG_PANIC_FLAG) {
            Slog.w("ActivityManager", str + " restart of crashed service " + serviceRecord.shortInstanceName + " in " + serviceRecord.restartDelay + "ms for " + str2);
        }
        EventLog.writeEvent(EventLogTags.AM_SCHEDULE_SERVICE_RESTART, Integer.valueOf(serviceRecord.userId), serviceRecord.shortInstanceName, Long.valueOf(serviceRecord.restartDelay));
        if (DEBUG_DELAYED_SERVICE) {
            Slog.v("ActivityManager", "scheduleServiceRestartLocked r " + serviceRecord + " call by " + Debug.getCallers(8));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mAm"})
    public void rescheduleServiceRestartOnMemoryPressureIfNeededLocked(int i, int i2, String str, long j) {
        ActivityManagerConstants activityManagerConstants = this.mAm.mConstants;
        if (activityManagerConstants.mEnableExtraServiceRestartDelayOnMemPressure) {
            long[] jArr = activityManagerConstants.mExtraServiceRestartDelayOnMemPressure;
            performRescheduleServiceRestartOnMemoryPressureLocked(jArr[i], jArr[i2], str, j);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mAm"})
    public void rescheduleServiceRestartOnMemoryPressureIfNeededLocked(boolean z, boolean z2, long j) {
        if (z == z2) {
            return;
        }
        long j2 = this.mAm.mConstants.mExtraServiceRestartDelayOnMemPressure[this.mAm.mAppProfiler.getLastMemoryLevelLocked()];
        long j3 = z ? j2 : 0L;
        if (!z2) {
            j2 = 0;
        }
        performRescheduleServiceRestartOnMemoryPressureLocked(j3, j2, "config", j);
    }

    @GuardedBy({"mAm"})
    void rescheduleServiceRestartIfPossibleLocked(long j, long j2, String str, long j3) {
        long j4;
        long j5;
        long j6;
        long j7 = j + j2;
        long j8 = j7 * 2;
        int size = this.mRestartingServices.size();
        int i = -1;
        int i2 = 0;
        long j9 = j3;
        while (i2 < size) {
            ServiceRecord serviceRecord = this.mRestartingServices.get(i2);
            if ((serviceRecord.serviceInfo.applicationInfo.flags & 8) != 0 || !isServiceRestartBackoffEnabledLocked(serviceRecord.packageName)) {
                j4 = j7;
                j5 = j8;
                j9 = serviceRecord.nextRestartTime;
                i = i2;
            } else {
                long j10 = j9 + j7;
                long j11 = j8;
                long j12 = serviceRecord.mEarliestRestartTime;
                if (j10 <= j12) {
                    serviceRecord.nextRestartTime = Math.max(j3, Math.max(j12, i2 > 0 ? this.mRestartingServices.get(i2 - 1).nextRestartTime + j7 : 0L));
                } else {
                    if (j9 <= j3) {
                        serviceRecord.nextRestartTime = Math.max(j3, Math.max(j12, serviceRecord.mRestartSchedulingTime + j));
                    } else {
                        serviceRecord.nextRestartTime = Math.max(j3, j10);
                    }
                    int i3 = i + 1;
                    if (i2 > i3) {
                        this.mRestartingServices.remove(i2);
                        this.mRestartingServices.add(i3, serviceRecord);
                    }
                }
                int i4 = i;
                long j13 = j9;
                int i5 = i + 1;
                while (true) {
                    if (i5 > i2) {
                        j4 = j7;
                        j5 = j11;
                        break;
                    }
                    ServiceRecord serviceRecord2 = this.mRestartingServices.get(i5);
                    long j14 = serviceRecord2.nextRestartTime;
                    if (i5 == 0) {
                        j4 = j7;
                        j6 = j13;
                    } else {
                        j4 = j7;
                        j6 = this.mRestartingServices.get(i5 - 1).nextRestartTime;
                    }
                    long j15 = j14 - j6;
                    j5 = j11;
                    if (j15 >= j5) {
                        break;
                    }
                    i4 = i5;
                    j13 = serviceRecord2.nextRestartTime;
                    i5++;
                    j11 = j5;
                    j7 = j4;
                }
                serviceRecord.restartDelay = serviceRecord.nextRestartTime - j3;
                performScheduleRestartLocked(serviceRecord, "Rescheduling", str, j3);
                i = i4;
                j9 = j13;
            }
            i2++;
            j8 = j5;
            j7 = j4;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0055, code lost:
    
        if (r3 != r0) goto L21;
     */
    @GuardedBy({"mAm"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void performRescheduleServiceRestartOnMemoryPressureLocked(long j, long j2, String str, long j3) {
        boolean z;
        long j4 = j2 - j;
        if (j4 == 0) {
            return;
        }
        if (j4 <= 0) {
            if (j4 < 0) {
                rescheduleServiceRestartIfPossibleLocked(j2, this.mAm.mConstants.SERVICE_MIN_RESTART_TIME_BETWEEN, str, j3);
                return;
            }
            return;
        }
        long j5 = this.mAm.mConstants.SERVICE_MIN_RESTART_TIME_BETWEEN + j2;
        int size = this.mRestartingServices.size();
        long j6 = j3;
        for (int i = 0; i < size; i++) {
            ServiceRecord serviceRecord = this.mRestartingServices.get(i);
            if ((serviceRecord.serviceInfo.applicationInfo.flags & 8) != 0 || !isServiceRestartBackoffEnabledLocked(serviceRecord.packageName)) {
                j6 = serviceRecord.nextRestartTime;
            } else {
                if (j6 <= j3) {
                    long j7 = serviceRecord.nextRestartTime;
                    long max = Math.max(j3, Math.max(serviceRecord.mEarliestRestartTime, serviceRecord.mRestartSchedulingTime + j2));
                    serviceRecord.nextRestartTime = max;
                } else {
                    if (serviceRecord.nextRestartTime - j6 < j5) {
                        serviceRecord.nextRestartTime = Math.max(j6 + j5, j3);
                        z = true;
                    }
                    z = false;
                }
                long j8 = serviceRecord.nextRestartTime;
                serviceRecord.restartDelay = j8 - j3;
                if (z) {
                    performScheduleRestartLocked(serviceRecord, "Rescheduling", str, j3);
                }
                j6 = j8;
            }
        }
    }

    @GuardedBy({"mAm"})
    long getExtraRestartTimeInBetweenLocked() {
        ActivityManagerService activityManagerService = this.mAm;
        if (!activityManagerService.mConstants.mEnableExtraServiceRestartDelayOnMemPressure) {
            return 0L;
        }
        return this.mAm.mConstants.mExtraServiceRestartDelayOnMemPressure[activityManagerService.mAppProfiler.getLastMemoryLevelLocked()];
    }

    final void performServiceRestartLocked(ServiceRecord serviceRecord) {
        if (this.mRestartingServices.contains(serviceRecord)) {
            serviceRecord.mServiceRecordExt.setExceptionWhenBringUp(false);
            if (!isServiceNeededLocked(serviceRecord, false, false)) {
                Slog.wtf("ActivityManager", "Restarting service that is not needed: " + serviceRecord);
                return;
            }
            try {
                this.mActiveServicesExt.hookPerformRestartServiceBegin(serviceRecord);
                bringUpServiceLocked(serviceRecord, serviceRecord.intent.getIntent().getFlags(), serviceRecord.createdFromFg, true, false, false, true);
            } catch (TransactionTooLargeException unused) {
            } catch (Throwable th) {
                this.mAm.updateOomAdjPendingTargetsLocked(6);
                throw th;
            }
            this.mAm.updateOomAdjPendingTargetsLocked(6);
        }
    }

    private final boolean unscheduleServiceRestartLocked(ServiceRecord serviceRecord, int i, boolean z) {
        if (!z && serviceRecord.restartDelay == 0) {
            return false;
        }
        boolean remove = this.mRestartingServices.remove(serviceRecord);
        if (remove || i != serviceRecord.appInfo.uid) {
            serviceRecord.resetRestartCounter();
        }
        if (remove) {
            clearRestartingIfNeededLocked(serviceRecord);
        }
        serviceRecord.mServiceRecordExt.setExceptionWhenBringUp(false);
        this.mAm.mHandler.removeCallbacks(serviceRecord.restarter);
        return true;
    }

    private void clearRestartingIfNeededLocked(ServiceRecord serviceRecord) {
        if (serviceRecord.restartTracker != null) {
            boolean z = true;
            int size = this.mRestartingServices.size() - 1;
            while (true) {
                if (size < 0) {
                    z = false;
                    break;
                } else if (this.mRestartingServices.get(size).restartTracker == serviceRecord.restartTracker) {
                    break;
                } else {
                    size--;
                }
            }
            if (z) {
                return;
            }
            synchronized (this.mAm.mProcessStats.mLock) {
                serviceRecord.restartTracker.setRestarting(false, this.mAm.mProcessStats.getMemFactorLocked(), SystemClock.uptimeMillis());
            }
            serviceRecord.restartTracker = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mAm"})
    public void setServiceRestartBackoffEnabledLocked(String str, boolean z, String str2) {
        if (!z) {
            if (this.mRestartBackoffDisabledPackages.contains(str)) {
                return;
            }
            this.mRestartBackoffDisabledPackages.add(str);
            long uptimeMillis = SystemClock.uptimeMillis();
            int size = this.mRestartingServices.size();
            for (int i = 0; i < size; i++) {
                ServiceRecord serviceRecord = this.mRestartingServices.get(i);
                if (TextUtils.equals(serviceRecord.packageName, str)) {
                    long j = serviceRecord.nextRestartTime - uptimeMillis;
                    long j2 = this.mAm.mConstants.SERVICE_RESTART_DURATION;
                    if (j > j2) {
                        serviceRecord.restartDelay = j2;
                        serviceRecord.nextRestartTime = j2 + uptimeMillis;
                        performScheduleRestartLocked(serviceRecord, "Rescheduling", str2, uptimeMillis);
                    }
                }
                Collections.sort(this.mRestartingServices, new Comparator() { // from class: com.android.server.am.ActiveServices$$ExternalSyntheticLambda3
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        int lambda$setServiceRestartBackoffEnabledLocked$0;
                        lambda$setServiceRestartBackoffEnabledLocked$0 = ActiveServices.lambda$setServiceRestartBackoffEnabledLocked$0((ServiceRecord) obj, (ServiceRecord) obj2);
                        return lambda$setServiceRestartBackoffEnabledLocked$0;
                    }
                });
            }
            return;
        }
        removeServiceRestartBackoffEnabledLocked(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$setServiceRestartBackoffEnabledLocked$0(ServiceRecord serviceRecord, ServiceRecord serviceRecord2) {
        return (int) (serviceRecord.nextRestartTime - serviceRecord2.nextRestartTime);
    }

    @GuardedBy({"mAm"})
    private void removeServiceRestartBackoffEnabledLocked(String str) {
        this.mRestartBackoffDisabledPackages.remove(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mAm"})
    public boolean isServiceRestartBackoffEnabledLocked(String str) {
        return !this.mRestartBackoffDisabledPackages.contains(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String bringUpServiceLocked(ServiceRecord serviceRecord, int i, boolean z, boolean z2, boolean z3, boolean z4, boolean z5) throws TransactionTooLargeException {
        try {
            if (Trace.isTagEnabled(64L)) {
                Trace.traceBegin(64L, "bringUpServiceLocked: " + serviceRecord.shortInstanceName);
            }
            return bringUpServiceInnerLocked(serviceRecord, i, z, z2, z3, z4, z5);
        } finally {
            Trace.traceEnd(64L);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:105:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0328 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x03c0  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0405  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x040e  */
    /* JADX WARN: Type inference failed for: r16v0, types: [com.android.server.am.HostingRecord] */
    /* JADX WARN: Type inference failed for: r16v2 */
    /* JADX WARN: Type inference failed for: r16v7 */
    /* JADX WARN: Type inference failed for: r20v0, types: [int] */
    /* JADX WARN: Type inference failed for: r20v10 */
    /* JADX WARN: Type inference failed for: r20v5 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String bringUpServiceInnerLocked(ServiceRecord serviceRecord, int i, boolean z, boolean z2, boolean z3, boolean z4, boolean z5) throws TransactionTooLargeException {
        String str;
        String str2;
        String str3;
        String str4;
        ProcessRecord processRecord;
        HostingRecord hostingRecord;
        HostingRecord byAppZygote;
        ProcessRecord processRecord2;
        long j;
        long j2;
        boolean z6;
        String str5;
        ProcessRecord startProcessLocked;
        ProcessRecord processRecord3;
        long j3;
        String str6;
        ProcessRecord processRecord4 = serviceRecord.app;
        if (processRecord4 != null && processRecord4.getThread() != null) {
            sendServiceArgsLocked(serviceRecord, z, false);
            return null;
        }
        if (!z2 && this.mRestartingServices.contains(serviceRecord)) {
            return null;
        }
        if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
            Slog.v(TAG_SERVICE, "Bringing up " + serviceRecord + " " + serviceRecord.intent + " fg=" + serviceRecord.fgRequired);
        }
        if (this.mRestartingServices.remove(serviceRecord)) {
            clearRestartingIfNeededLocked(serviceRecord);
        }
        if (serviceRecord.delayed) {
            if (DEBUG_DELAYED_STARTS) {
                Slog.v(TAG_SERVICE, "REM FR DELAY LIST (bring up): " + serviceRecord);
            }
            getServiceMapLocked(serviceRecord.userId).mDelayedStartList.remove(serviceRecord);
            serviceRecord.delayed = false;
        }
        if (!this.mAm.mUserController.hasStartedUserState(serviceRecord.userId)) {
            String str7 = "Unable to launch app " + serviceRecord.appInfo.packageName + "/" + serviceRecord.appInfo.uid + " for service " + serviceRecord.intent.getIntent() + ": user " + serviceRecord.userId + " is stopped";
            Slog.w("ActivityManager", str7);
            bringDownServiceLocked(serviceRecord, z5);
            return str7;
        }
        if (this.mActiveServicesExt.interceptBringUpServices(serviceRecord, this.mAm, Binder.getCallingUid(), Binder.getCallingPid())) {
            bringDownServiceLocked(serviceRecord, z5);
            return null;
        }
        if (!serviceRecord.appInfo.packageName.equals(serviceRecord.mRecentCallingPackage) && !serviceRecord.isNotAppComponentUsage) {
            this.mAm.mUsageStatsService.reportEvent(serviceRecord.packageName, serviceRecord.userId, 31);
        }
        try {
            this.mAm.mPackageManagerInt.setPackageStoppedState(serviceRecord.packageName, false, serviceRecord.userId);
        } catch (IllegalArgumentException e) {
            Slog.w("ActivityManager", "Failed trying to unstop package " + serviceRecord.packageName + ": " + e);
        }
        ServiceInfo serviceInfo = serviceRecord.serviceInfo;
        boolean z7 = (serviceInfo.flags & 2) != 0;
        String str8 = serviceRecord.processName;
        ComponentName componentName = serviceRecord.instanceName;
        String str9 = serviceRecord.definingPackageName;
        long j4 = serviceRecord.definingUid;
        long hostingRecord2 = new HostingRecord(HostingRecord.HOSTING_TYPE_SERVICE, componentName, str9, j4, serviceInfo.processName, getHostingRecordTriggerType(serviceRecord));
        if (!z7) {
            ProcessRecord processRecordLocked = this.mAm.getProcessRecordLocked(str8, serviceRecord.appInfo.uid);
            if (ActivityManagerDebugConfig.DEBUG_MU) {
                Slog.v(TAG_MU, "bringUpServiceLocked: appInfo.uid=" + serviceRecord.appInfo.uid + " app=" + processRecordLocked);
            }
            if (processRecordLocked != null) {
                IApplicationThread thread = processRecordLocked.getThread();
                int pid = processRecordLocked.getPid();
                UidRecord uidRecord = processRecordLocked.getUidRecord();
                try {
                    if (thread != null) {
                        try {
                            if (Trace.isTagEnabled(64L)) {
                                Trace.traceBegin(64L, "realStartServiceLocked: " + serviceRecord.shortInstanceName);
                            }
                            ApplicationInfo applicationInfo = serviceRecord.appInfo;
                            try {
                                processRecordLocked.addPackage(applicationInfo.packageName, applicationInfo.longVersionCode, this.mAm.mProcessStats);
                                j3 = 64;
                                processRecord3 = processRecordLocked;
                                str6 = "Exception when starting service ";
                                str = str8;
                                str2 = "ActivityManager";
                                str3 = " for service ";
                                str4 = "Unable to launch app ";
                            } catch (TransactionTooLargeException e2) {
                                throw e2;
                            } catch (RemoteException e3) {
                                e = e3;
                                str = str8;
                                str2 = "ActivityManager";
                                processRecord3 = processRecordLocked;
                                str3 = " for service ";
                                j3 = 64;
                                str6 = "Exception when starting service ";
                                str4 = "Unable to launch app ";
                                Slog.w(str2, str6 + serviceRecord.shortInstanceName, e);
                                Trace.traceEnd(j3);
                                processRecord = processRecord3;
                                byAppZygote = hostingRecord;
                                if (processRecord == null) {
                                }
                                z6 = z5;
                                str5 = str2;
                                if (serviceRecord.fgRequired) {
                                }
                                if (!this.mPendingServices.contains(serviceRecord)) {
                                }
                                if (serviceRecord.delayedStop) {
                                }
                            } catch (Throwable th) {
                                th = th;
                                j4 = 64;
                                Trace.traceEnd(j4);
                                throw th;
                            }
                        } catch (TransactionTooLargeException e4) {
                            throw e4;
                        } catch (RemoteException e5) {
                            e = e5;
                            j3 = 64;
                            str = str8;
                            str2 = "ActivityManager";
                            processRecord3 = processRecordLocked;
                            str3 = " for service ";
                        } catch (Throwable th2) {
                            th = th2;
                            j4 = 64;
                        }
                        try {
                            realStartServiceLocked(serviceRecord, processRecordLocked, thread, pid, uidRecord, z, z5);
                            Trace.traceEnd(64L);
                            return null;
                        } catch (TransactionTooLargeException e6) {
                            throw e6;
                        } catch (RemoteException e7) {
                            e = e7;
                            Slog.w(str2, str6 + serviceRecord.shortInstanceName, e);
                            Trace.traceEnd(j3);
                            processRecord = processRecord3;
                            byAppZygote = hostingRecord;
                            if (processRecord == null) {
                            }
                            z6 = z5;
                            str5 = str2;
                            if (serviceRecord.fgRequired) {
                            }
                            if (!this.mPendingServices.contains(serviceRecord)) {
                            }
                            if (serviceRecord.delayedStop) {
                            }
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
            }
            str = str8;
            str2 = "ActivityManager";
            processRecord3 = processRecordLocked;
            str3 = " for service ";
            str4 = "Unable to launch app ";
            processRecord = processRecord3;
        } else {
            str = str8;
            str2 = "ActivityManager";
            str3 = " for service ";
            str4 = "Unable to launch app ";
            if (serviceRecord.inSharedIsolatedProcess) {
                ProcessList processList = this.mAm.mProcessList;
                ApplicationInfo applicationInfo2 = serviceRecord.appInfo;
                ProcessRecord sharedIsolatedProcess = processList.getSharedIsolatedProcess(str, applicationInfo2.uid, applicationInfo2.packageName);
                if (sharedIsolatedProcess != null) {
                    IApplicationThread thread2 = sharedIsolatedProcess.getThread();
                    int pid2 = sharedIsolatedProcess.getPid();
                    UidRecord uidRecord2 = sharedIsolatedProcess.getUidRecord();
                    serviceRecord.isolationHostProc = sharedIsolatedProcess;
                    try {
                        if (thread2 != null) {
                            try {
                                if (Trace.isTagEnabled(64L)) {
                                    j2 = 64;
                                    try {
                                        Trace.traceBegin(64L, "realStartServiceLocked: " + serviceRecord.shortInstanceName);
                                    } catch (TransactionTooLargeException e8) {
                                        throw e8;
                                    } catch (RemoteException e9) {
                                        e = e9;
                                        j = 64;
                                        processRecord2 = sharedIsolatedProcess;
                                        Slog.w(str2, "Exception when starting service " + serviceRecord.shortInstanceName, e);
                                        Trace.traceEnd(j);
                                        processRecord = processRecord2;
                                        byAppZygote = hostingRecord;
                                        if (processRecord == null) {
                                        }
                                        z6 = z5;
                                        str5 = str2;
                                        if (serviceRecord.fgRequired) {
                                        }
                                        if (!this.mPendingServices.contains(serviceRecord)) {
                                        }
                                        if (serviceRecord.delayedStop) {
                                        }
                                    } catch (Throwable th4) {
                                        th = th4;
                                        hostingRecord2 = 64;
                                        Trace.traceEnd(hostingRecord2);
                                        throw th;
                                    }
                                } else {
                                    j2 = 64;
                                }
                                j = j2;
                                processRecord2 = sharedIsolatedProcess;
                            } catch (TransactionTooLargeException e10) {
                                throw e10;
                            } catch (RemoteException e11) {
                                e = e11;
                                processRecord2 = sharedIsolatedProcess;
                                j = 64;
                            } catch (Throwable th5) {
                                th = th5;
                                hostingRecord2 = 64;
                            }
                            try {
                                realStartServiceLocked(serviceRecord, sharedIsolatedProcess, thread2, pid2, uidRecord2, z, z5);
                                Trace.traceEnd(j);
                                return null;
                            } catch (TransactionTooLargeException e12) {
                                throw e12;
                            } catch (RemoteException e13) {
                                e = e13;
                                Slog.w(str2, "Exception when starting service " + serviceRecord.shortInstanceName, e);
                                Trace.traceEnd(j);
                                processRecord = processRecord2;
                                byAppZygote = hostingRecord;
                                if (processRecord == null) {
                                }
                                z6 = z5;
                                str5 = str2;
                                if (serviceRecord.fgRequired) {
                                }
                                if (!this.mPendingServices.contains(serviceRecord)) {
                                }
                                if (serviceRecord.delayedStop) {
                                }
                            }
                        }
                    } catch (Throwable th6) {
                        th = th6;
                    }
                }
                processRecord2 = sharedIsolatedProcess;
                processRecord = processRecord2;
            } else {
                processRecord = serviceRecord.isolationHostProc;
                if (WebViewZygote.isMultiprocessEnabled() && serviceRecord.serviceInfo.packageName.equals(WebViewZygote.getPackageName())) {
                    hostingRecord = HostingRecord.byWebviewZygote(serviceRecord.instanceName, serviceRecord.definingPackageName, serviceRecord.definingUid, serviceRecord.serviceInfo.processName);
                }
                ServiceInfo serviceInfo2 = serviceRecord.serviceInfo;
                if ((serviceInfo2.flags & 8) != 0) {
                    byAppZygote = HostingRecord.byAppZygote(serviceRecord.instanceName, serviceRecord.definingPackageName, serviceRecord.definingUid, serviceInfo2.processName);
                    if (processRecord == null || z3 || z4) {
                        z6 = z5;
                        str5 = str2;
                    } else {
                        if (serviceRecord.isSdkSandbox) {
                            startProcessLocked = this.mAm.startSdkSandboxProcessLocked(str, serviceRecord.appInfo, true, i, byAppZygote, 0, Process.toSdkSandboxUid(serviceRecord.sdkSandboxClientAppUid), serviceRecord.sdkSandboxClientAppPackage);
                            serviceRecord.isolationHostProc = startProcessLocked;
                        } else {
                            startProcessLocked = this.mAm.startProcessLocked(str, serviceRecord.appInfo, true, i, byAppZygote, 0, false, z7);
                        }
                        if (startProcessLocked == null) {
                            String str10 = str4 + serviceRecord.appInfo.packageName + "/" + serviceRecord.appInfo.uid + str3 + serviceRecord.intent.getIntent() + ": process is bad";
                            Slog.w(str2, str10);
                            bringDownServiceLocked(serviceRecord, z5);
                            return str10;
                        }
                        z6 = z5;
                        str5 = str2;
                        if (z7) {
                            serviceRecord.isolationHostProc = startProcessLocked;
                        }
                        this.mActiveServicesExt.hookBringUpServicesAfterStartProc(serviceRecord, Binder.getCallingUid(), Binder.getCallingPid());
                    }
                    if (serviceRecord.fgRequired) {
                        if (ActivityManagerDebugConfig.DEBUG_FOREGROUND_SERVICE) {
                            Slog.v(str5, "Allowlisting " + UserHandle.formatUid(serviceRecord.appInfo.uid) + " for fg-service launch");
                        }
                        this.mAm.tempAllowlistUidLocked(serviceRecord.appInfo.uid, r11.mConstants.mServiceStartForegroundTimeoutMs, FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_SERVICE_LAUNCH, "fg-service-launch", 0, serviceRecord.mRecentCallingUid);
                    }
                    if (!this.mPendingServices.contains(serviceRecord)) {
                        this.mPendingServices.add(serviceRecord);
                    }
                    if (serviceRecord.delayedStop) {
                        return null;
                    }
                    serviceRecord.delayedStop = false;
                    if (!serviceRecord.startRequested) {
                        return null;
                    }
                    if (DEBUG_DELAYED_STARTS) {
                        Slog.v(TAG_SERVICE, "Applying delayed stop (in bring up): " + serviceRecord);
                    }
                    stopServiceLocked(serviceRecord, z6);
                    return null;
                }
            }
        }
        byAppZygote = hostingRecord;
        if (processRecord == null) {
        }
        z6 = z5;
        str5 = str2;
        if (serviceRecord.fgRequired) {
        }
        if (!this.mPendingServices.contains(serviceRecord)) {
        }
        if (serviceRecord.delayedStop) {
        }
    }

    private String getHostingRecordTriggerType(ServiceRecord serviceRecord) {
        return ("android.permission.BIND_JOB_SERVICE".equals(serviceRecord.permission) && serviceRecord.mRecentCallingUid == 1000) ? HostingRecord.TRIGGER_TYPE_JOB : "unknown";
    }

    private final void requestServiceBindingsLocked(ServiceRecord serviceRecord, boolean z) throws TransactionTooLargeException {
        for (int size = serviceRecord.bindings.size() - 1; size >= 0 && requestServiceBindingLocked(serviceRecord, serviceRecord.bindings.valueAt(size), z, false); size--) {
        }
    }

    private void realStartServiceLocked(ServiceRecord serviceRecord, ProcessRecord processRecord, IApplicationThread iApplicationThread, int i, UidRecord uidRecord, boolean z, boolean z2) throws RemoteException {
        boolean z3;
        String str;
        if (iApplicationThread == null) {
            throw new RemoteException();
        }
        if (ActivityManagerDebugConfig.DEBUG_MU) {
            Slog.v(TAG_MU, "realStartServiceLocked, ServiceRecord.uid = " + serviceRecord.appInfo.uid + ", ProcessRecord.uid = " + processRecord.uid);
        }
        serviceRecord.setProcess(processRecord, iApplicationThread, i, uidRecord);
        long uptimeMillis = SystemClock.uptimeMillis();
        serviceRecord.lastActivity = uptimeMillis;
        serviceRecord.restartTime = uptimeMillis;
        ProcessServiceRecord processServiceRecord = processRecord.mServices;
        boolean startService = processServiceRecord.startService(serviceRecord);
        bumpServiceExecutingLocked(serviceRecord, z, "create", 0);
        this.mAm.updateLruProcessLocked(processRecord, false, null);
        updateServiceForegroundLocked(processServiceRecord, false);
        this.mAm.enqueueOomAdjTargetLocked(processRecord);
        this.mAm.updateOomAdjLocked(processRecord, 6);
        try {
            try {
                if (LOG_SERVICE_START_STOP) {
                    int lastIndexOf = serviceRecord.shortInstanceName.lastIndexOf(46);
                    if (lastIndexOf >= 0) {
                        str = serviceRecord.shortInstanceName.substring(lastIndexOf);
                    } else {
                        str = serviceRecord.shortInstanceName;
                    }
                    EventLogTags.writeAmCreateService(serviceRecord.userId, System.identityHashCode(serviceRecord), str, serviceRecord.app.uid, i);
                }
                int i2 = serviceRecord.appInfo.uid;
                String packageName = serviceRecord.name.getPackageName();
                String className = serviceRecord.name.getClassName();
                FrameworkStatsLog.write(100, i2, packageName, className);
                this.mAm.mBatteryStatsService.noteServiceStartLaunch(i2, packageName, className);
                this.mAm.notifyPackageUse(serviceRecord.serviceInfo.packageName, 1);
                iApplicationThread.scheduleCreateService(serviceRecord, serviceRecord.serviceInfo, (CompatibilityInfo) null, processRecord.mState.getReportedProcState());
                serviceRecord.postNotification(false);
                if (serviceRecord.allowlistManager) {
                    processServiceRecord.mAllowlistManager = true;
                }
                requestServiceBindingsLocked(serviceRecord, z);
                updateServiceClientActivitiesLocked(processServiceRecord, null, true);
                if (startService) {
                    processServiceRecord.addBoundClientUidsOfNewService(serviceRecord);
                }
                if (serviceRecord.startRequested && serviceRecord.callStart && serviceRecord.pendingStarts.size() == 0) {
                    serviceRecord.pendingStarts.add(new ServiceRecord.StartItem(serviceRecord, false, serviceRecord.makeNextStartId(), null, null, 0, null, null, -1));
                }
                sendServiceArgsLocked(serviceRecord, z, true);
                if (serviceRecord.delayed) {
                    if (DEBUG_DELAYED_STARTS) {
                        Slog.v(TAG_SERVICE, "REM FR DELAY LIST (new proc): " + serviceRecord);
                    }
                    getServiceMapLocked(serviceRecord.userId).mDelayedStartList.remove(serviceRecord);
                    z3 = false;
                    serviceRecord.delayed = false;
                } else {
                    z3 = false;
                }
                if (serviceRecord.delayedStop) {
                    serviceRecord.delayedStop = z3;
                    if (serviceRecord.startRequested) {
                        if (DEBUG_DELAYED_STARTS) {
                            Slog.v(TAG_SERVICE, "Applying delayed stop (from start): " + serviceRecord);
                        }
                        stopServiceLocked(serviceRecord, z2);
                    }
                }
            } catch (DeadObjectException e) {
                this.mActiveServicesExt.handleExceptionWhenBringUpService(serviceRecord, z);
                Slog.w("ActivityManager", "Application dead when creating service " + serviceRecord);
                this.mAm.appDiedLocked(processRecord, "Died when creating service");
                throw e;
            }
        } catch (Throwable th) {
            boolean contains = this.mDestroyingServices.contains(serviceRecord);
            serviceDoneExecutingLocked(serviceRecord, contains, contains, false, 19);
            if (startService) {
                processServiceRecord.stopService(serviceRecord);
                serviceRecord.app = null;
            }
            if (!contains) {
                scheduleServiceRestartLocked(serviceRecord, false);
            }
            throw th;
        }
    }

    private final void sendServiceArgsLocked(ServiceRecord serviceRecord, boolean z, boolean z2) throws TransactionTooLargeException {
        int i;
        int size = serviceRecord.pendingStarts.size();
        if (size == 0) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        while (true) {
            if (serviceRecord.pendingStarts.size() <= 0) {
                break;
            }
            ServiceRecord.StartItem remove = serviceRecord.pendingStarts.remove(0);
            if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                Slog.v(TAG_SERVICE, "Sending arguments to: " + serviceRecord + " " + serviceRecord.intent + " args=" + remove.intent);
            }
            if (remove.intent != null || size <= 1) {
                remove.deliveredTime = SystemClock.uptimeMillis();
                serviceRecord.deliveredStarts.add(remove);
                remove.deliveryCount++;
                NeededUriGrants neededUriGrants = remove.neededGrants;
                if (neededUriGrants != null) {
                    this.mAm.mUgmInternal.grantUriPermissionUncheckedFromIntent(neededUriGrants, remove.getUriPermissionsLocked());
                }
                this.mAm.grantImplicitAccess(serviceRecord.userId, remove.intent, remove.callingId, UserHandle.getAppId(serviceRecord.appInfo.uid));
                bumpServiceExecutingLocked(serviceRecord, z, "start", 0);
                if (serviceRecord.fgRequired && !serviceRecord.fgWaiting) {
                    if (!serviceRecord.isForeground) {
                        if (ActivityManagerDebugConfig.DEBUG_BACKGROUND_CHECK) {
                            Slog.i("ActivityManager", "Launched service must call startForeground() within timeout: " + serviceRecord);
                        }
                        scheduleServiceForegroundTransitionTimeoutLocked(serviceRecord);
                    } else {
                        if (ActivityManagerDebugConfig.DEBUG_BACKGROUND_CHECK) {
                            Slog.i("ActivityManager", "Service already foreground; no new timeout: " + serviceRecord);
                        }
                        serviceRecord.fgRequired = false;
                    }
                }
                i = remove.deliveryCount > 1 ? 2 : 0;
                if (remove.doneExecutingCount > 0) {
                    i |= 1;
                }
                arrayList.add(new ServiceStartArgs(remove.taskRemoved, remove.id, i, remove.intent));
            }
        }
        if (!z2) {
            this.mAm.enqueueOomAdjTargetLocked(serviceRecord.app);
            this.mAm.updateOomAdjPendingTargetsLocked(6);
        }
        ParceledListSlice parceledListSlice = new ParceledListSlice(arrayList);
        parceledListSlice.setInlineCountLimit(4);
        try {
            serviceRecord.app.getThread().scheduleServiceArgs(serviceRecord, parceledListSlice);
            e = null;
        } catch (TransactionTooLargeException e) {
            e = e;
            if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                Slog.v(TAG_SERVICE, "Transaction too large for " + arrayList.size() + " args, first: " + ((ServiceStartArgs) arrayList.get(0)).args);
            }
            Slog.w("ActivityManager", "Failed delivering service starts", e);
        } catch (RemoteException e2) {
            e = e2;
            if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                Slog.v(TAG_SERVICE, "Crashed while sending args: " + serviceRecord);
            }
            Slog.w("ActivityManager", "Failed delivering service starts", e);
        } catch (Exception e3) {
            e = e3;
            Slog.w("ActivityManager", "Unexpected exception", e);
        }
        if (e != null) {
            boolean contains = this.mDestroyingServices.contains(serviceRecord);
            int size2 = arrayList.size();
            while (i < size2) {
                serviceDoneExecutingLocked(serviceRecord, contains, contains, true, 19);
                i++;
            }
            this.mAm.updateOomAdjPendingTargetsLocked(19);
            if (e instanceof TransactionTooLargeException) {
                throw ((TransactionTooLargeException) e);
            }
        }
    }

    private final boolean isServiceNeededLocked(ServiceRecord serviceRecord, boolean z, boolean z2) {
        if (serviceRecord.startRequested) {
            return true;
        }
        if (!z) {
            z2 = serviceRecord.hasAutoCreateConnections();
        }
        return z2;
    }

    private void bringDownServiceIfNeededLocked(ServiceRecord serviceRecord, boolean z, boolean z2, boolean z3, String str) {
        if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
            Slog.i("ActivityManager", "Bring down service for " + str + " :" + serviceRecord.toString());
        }
        if (isServiceNeededLocked(serviceRecord, z, z2) || this.mPendingServices.contains(serviceRecord)) {
            return;
        }
        ServiceRecord remove = getServiceMapLocked(serviceRecord.userId).mServicesByInstanceName.remove(serviceRecord.instanceName);
        if (remove != null && remove != serviceRecord) {
            Slog.i("ActivityManager", "trying to bring down a service record that has been brought down once " + serviceRecord);
            return;
        }
        if (this.mActiveServicesExt.interceptBringDownServiceIfNeeded(getServiceMapLocked(serviceRecord.userId), serviceRecord)) {
            return;
        }
        bringDownServiceLocked(serviceRecord, z3);
    }

    private void bringDownServiceLocked(final ServiceRecord serviceRecord, boolean z) {
        boolean z2;
        boolean z3;
        boolean z4;
        if (serviceRecord.isShortFgs()) {
            Slog.w(TAG_SERVICE, "Short FGS brought down without stopping: " + serviceRecord);
            maybeStopShortFgsTimeoutLocked(serviceRecord);
        }
        ArrayMap<IBinder, ArrayList<ConnectionRecord>> connections = serviceRecord.getConnections();
        int i = 1000;
        if (connections.size() > 1000) {
            Slog.v("ActivityManager", "too many connections: Bring down service " + serviceRecord);
            z2 = true;
        } else {
            z2 = false;
        }
        int size = connections.size() - 1;
        boolean z5 = false;
        while (size >= 0) {
            ArrayList<ConnectionRecord> valueAt = connections.valueAt(size);
            if (valueAt.size() > i) {
                Slog.v("ActivityManager", "too many connections: Bring down service " + serviceRecord);
                z4 = true;
            } else {
                z4 = false;
            }
            for (int i2 = 0; i2 < valueAt.size(); i2++) {
                ConnectionRecord connectionRecord = valueAt.get(i2);
                connectionRecord.serviceDead = true;
                connectionRecord.stopAssociation();
                if (z2 && size > connections.size() - 20 && size < connections.size() - 1) {
                    Slog.v("ActivityManager", "Bring down service  to: " + size + " " + connectionRecord);
                }
                if (z4 && i2 > valueAt.size() - 20 && i2 < valueAt.size() - 1) {
                    Slog.v("ActivityManager", "Bring down service  to: " + i2 + " " + connectionRecord);
                }
                try {
                    connectionRecord.conn.connected(serviceRecord.name, (IBinder) null, true);
                } catch (Exception e) {
                    Slog.w("ActivityManager", "Failure disconnecting service " + serviceRecord.shortInstanceName + " to connection " + valueAt.get(i2).conn.asBinder() + " (in " + valueAt.get(i2).binding.client.processName + ")", e);
                    z5 = true;
                }
                removeConnectionLocked(connectionRecord, null, null, true);
            }
            size--;
            i = 1000;
        }
        ProcessRecord processRecord = serviceRecord.app;
        if (processRecord == null || processRecord.getThread() == null || z5) {
            z3 = false;
        } else {
            boolean z6 = false;
            for (int size2 = serviceRecord.bindings.size() - 1; size2 >= 0; size2--) {
                IntentBindRecord valueAt2 = serviceRecord.bindings.valueAt(size2);
                if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                    Slog.v(TAG_SERVICE, "Bringing down binding " + valueAt2 + ": hasBound=" + valueAt2.hasBound);
                }
                if (valueAt2.hasBound) {
                    try {
                        z6 |= bumpServiceExecutingLocked(serviceRecord, false, "bring down unbind", 5);
                        valueAt2.hasBound = false;
                        valueAt2.requested = false;
                        serviceRecord.app.getThread().scheduleUnbindService(serviceRecord, valueAt2.intent.getIntent());
                    } catch (Exception e2) {
                        Slog.w("ActivityManager", "Exception when unbinding service " + serviceRecord.shortInstanceName, e2);
                        serviceProcessGoneLocked(serviceRecord, z);
                    }
                }
            }
            z3 = z6;
        }
        if (serviceRecord.fgRequired) {
            Slog.w(TAG_SERVICE, "Bringing down service while still waiting for start foreground: " + serviceRecord);
            serviceRecord.fgRequired = false;
            serviceRecord.fgWaiting = false;
            synchronized (this.mAm.mProcessStats.mLock) {
                ServiceState tracker = serviceRecord.getTracker();
                if (tracker != null) {
                    tracker.setForeground(false, this.mAm.mProcessStats.getMemFactorLocked(), SystemClock.uptimeMillis());
                }
            }
            AppOpsService appOpsService = this.mAm.mAppOpsService;
            appOpsService.finishOperation(AppOpsManager.getToken(appOpsService), 76, serviceRecord.appInfo.uid, serviceRecord.packageName, null);
            this.mAm.mHandler.removeMessages(66, serviceRecord);
            this.mActiveServicesExt.updateExecutingComponent(serviceRecord.appInfo.uid, "fg-service", 2);
            if (serviceRecord.app != null) {
                Message obtainMessage = this.mAm.mHandler.obtainMessage(69);
                SomeArgs obtain = SomeArgs.obtain();
                obtain.arg1 = serviceRecord.app;
                obtain.arg2 = serviceRecord.toString();
                obtain.arg3 = serviceRecord.getComponentName();
                obtainMessage.obj = obtain;
                this.mAm.mHandler.sendMessage(obtainMessage);
            }
        }
        if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
            RuntimeException runtimeException = new RuntimeException();
            runtimeException.fillInStackTrace();
            Slog.v(TAG_SERVICE, "Bringing down " + serviceRecord + " " + serviceRecord.intent, runtimeException);
        }
        serviceRecord.destroyTime = SystemClock.uptimeMillis();
        if (LOG_SERVICE_START_STOP) {
            int i3 = serviceRecord.userId;
            int identityHashCode = System.identityHashCode(serviceRecord);
            ProcessRecord processRecord2 = serviceRecord.app;
            EventLogTags.writeAmDestroyService(i3, identityHashCode, processRecord2 != null ? processRecord2.getPid() : -1);
        }
        ServiceMap serviceMapLocked = getServiceMapLocked(serviceRecord.userId);
        ServiceRecord remove = serviceMapLocked.mServicesByInstanceName.remove(serviceRecord.instanceName);
        if (remove != null && remove != serviceRecord) {
            serviceMapLocked.mServicesByInstanceName.put(serviceRecord.instanceName, remove);
            throw new IllegalStateException("Bringing down " + serviceRecord + " but actually running " + remove);
        }
        try {
            serviceMapLocked.mServicesByIntent.remove(serviceRecord.intent);
        } catch (ConcurrentModificationException unused) {
            Slog.e("ActivityManager", "smap.mServicesByIntent occur to ConcurrentModificationException");
        }
        serviceRecord.totalRestartCount = 0;
        unscheduleServiceRestartLocked(serviceRecord, 0, true);
        for (int size3 = this.mPendingServices.size() - 1; size3 >= 0; size3--) {
            if (this.mPendingServices.get(size3) == serviceRecord) {
                this.mPendingServices.remove(size3);
                if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                    Slog.v(TAG_SERVICE, "Removed pending: " + serviceRecord);
                }
            }
        }
        if (this.mPendingBringups.remove(serviceRecord) != null && ActivityManagerDebugConfig.DEBUG_SERVICE) {
            Slog.v(TAG_SERVICE, "Removed pending bringup: " + serviceRecord);
        }
        cancelForegroundNotificationLocked(serviceRecord);
        boolean z7 = serviceRecord.isForeground;
        if (z7) {
            maybeStopShortFgsTimeoutLocked(serviceRecord);
            decActiveForegroundAppLocked(serviceMapLocked, serviceRecord);
            synchronized (this.mAm.mProcessStats.mLock) {
                ServiceState tracker2 = serviceRecord.getTracker();
                if (tracker2 != null) {
                    tracker2.setForeground(false, this.mAm.mProcessStats.getMemFactorLocked(), SystemClock.uptimeMillis());
                }
            }
            AppOpsService appOpsService2 = this.mAm.mAppOpsService;
            appOpsService2.finishOperation(AppOpsManager.getToken(appOpsService2), 76, serviceRecord.appInfo.uid, serviceRecord.packageName, null);
            unregisterAppOpCallbackLocked(serviceRecord);
            long uptimeMillis = SystemClock.uptimeMillis();
            serviceRecord.mFgsExitTime = uptimeMillis;
            long j = serviceRecord.mFgsEnterTime;
            logFGSStateChangeLocked(serviceRecord, 2, uptimeMillis > j ? (int) (uptimeMillis - j) : 0, 2, 0);
            synchronized (this.mFGSLogger) {
                this.mFGSLogger.logForegroundServiceStop(serviceRecord.appInfo.uid, serviceRecord);
            }
            this.mAm.updateForegroundServiceUsageStats(serviceRecord.name, serviceRecord.userId, false);
        }
        serviceRecord.isForeground = false;
        serviceRecord.mFgsNotificationWasDeferred = false;
        dropFgsNotificationStateLocked(serviceRecord);
        serviceRecord.foregroundId = 0;
        serviceRecord.foregroundNoti = null;
        resetFgsRestrictionLocked(serviceRecord);
        if (z7) {
            signalForegroundServiceObserversLocked(serviceRecord);
        }
        serviceRecord.clearDeliveredStartsLocked();
        serviceRecord.pendingStarts.clear();
        serviceMapLocked.mDelayedStartList.remove(serviceRecord);
        if (serviceRecord.app != null) {
            this.mAm.mBatteryStatsService.noteServiceStopLaunch(serviceRecord.appInfo.uid, serviceRecord.name.getPackageName(), serviceRecord.name.getClassName());
            stopServiceAndUpdateAllowlistManagerLocked(serviceRecord);
            if (serviceRecord.app.getThread() != null) {
                this.mAm.updateLruProcessLocked(serviceRecord.app, false, null);
                updateServiceForegroundLocked(serviceRecord.app.mServices, false);
                if (serviceRecord.mIsFgsDelegate) {
                    if (serviceRecord.mFgsDelegation.mConnection != null) {
                        this.mAm.mHandler.post(new Runnable() { // from class: com.android.server.am.ActiveServices$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                ActiveServices.lambda$bringDownServiceLocked$1(ServiceRecord.this);
                            }
                        });
                    }
                    int size4 = this.mFgsDelegations.size() - 1;
                    while (true) {
                        if (size4 < 0) {
                            break;
                        }
                        if (this.mFgsDelegations.valueAt(size4) == serviceRecord) {
                            this.mFgsDelegations.removeAt(size4);
                            break;
                        }
                        size4--;
                    }
                } else {
                    try {
                        z3 |= bumpServiceExecutingLocked(serviceRecord, false, "destroy", z3 ? 0 : 19);
                        this.mDestroyingServices.add(serviceRecord);
                        serviceRecord.destroying = true;
                        serviceRecord.app.getThread().scheduleStopService(serviceRecord);
                    } catch (Exception e3) {
                        Slog.w("ActivityManager", "Exception when destroying service " + serviceRecord.shortInstanceName, e3);
                        serviceProcessGoneLocked(serviceRecord, z);
                    }
                }
            } else if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                Slog.v(TAG_SERVICE, "Removed service that has no process: " + serviceRecord);
            }
        } else if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
            Slog.v(TAG_SERVICE, "Removed service that is not running: " + serviceRecord);
        }
        if (!z3) {
            this.mAm.enqueueOomAdjTargetLocked(serviceRecord.app);
            if (!z) {
                this.mAm.updateOomAdjPendingTargetsLocked(19);
            }
        }
        if (serviceRecord.bindings.size() > 0) {
            serviceRecord.bindings.clear();
        }
        Runnable runnable = serviceRecord.restarter;
        if (runnable instanceof ServiceRestarter) {
            ((ServiceRestarter) runnable).setService(null);
        }
        serviceRecord.mServiceRecordExt.setExceptionWhenBringUp(false);
        serviceRecord.mServiceRecordExt.resetRestartDelayPromoteCount();
        synchronized (this.mAm.mProcessStats.mLock) {
            int memFactorLocked = this.mAm.mProcessStats.getMemFactorLocked();
            if (serviceRecord.tracker != null) {
                long uptimeMillis2 = SystemClock.uptimeMillis();
                serviceRecord.tracker.setStarted(false, memFactorLocked, uptimeMillis2);
                serviceRecord.tracker.setBound(false, memFactorLocked, uptimeMillis2);
                if (serviceRecord.executeNesting == 0) {
                    serviceRecord.tracker.clearCurrentOwner(serviceRecord, false);
                    serviceRecord.tracker = null;
                }
            }
        }
        serviceMapLocked.ensureNotStartingBackgroundLocked(serviceRecord);
        updateNumForegroundServicesLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$bringDownServiceLocked$1(ServiceRecord serviceRecord) {
        ForegroundServiceDelegation foregroundServiceDelegation = serviceRecord.mFgsDelegation;
        foregroundServiceDelegation.mConnection.onServiceDisconnected(foregroundServiceDelegation.mOptions.getComponentName());
    }

    private void dropFgsNotificationStateLocked(ServiceRecord serviceRecord) {
        if (serviceRecord.foregroundNoti == null) {
            return;
        }
        ServiceMap serviceMap = this.mServiceMap.get(serviceRecord.userId);
        boolean z = false;
        if (serviceMap != null) {
            int size = serviceMap.mServicesByInstanceName.size();
            int i = 0;
            while (true) {
                if (i >= size) {
                    break;
                }
                ServiceRecord valueAt = serviceMap.mServicesByInstanceName.valueAt(i);
                if (valueAt != serviceRecord && valueAt.isForeground && serviceRecord.foregroundId == valueAt.foregroundId && serviceRecord.appInfo.packageName.equals(valueAt.appInfo.packageName)) {
                    z = true;
                    break;
                }
                i++;
            }
        } else {
            Slog.wtf("ActivityManager", "FGS " + serviceRecord + " not found!");
        }
        if (z) {
            return;
        }
        serviceRecord.stripForegroundServiceFlagFromNotification();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeConnectionLocked(ConnectionRecord connectionRecord, ProcessRecord processRecord, ActivityServiceConnectionsHolder activityServiceConnectionsHolder, boolean z) {
        ProcessRecord processRecord2;
        IBinder asBinder = connectionRecord.conn.asBinder();
        AppBindRecord appBindRecord = connectionRecord.binding;
        ServiceRecord serviceRecord = appBindRecord.service;
        ArrayList<ConnectionRecord> arrayList = serviceRecord.getConnections().get(asBinder);
        if (arrayList != null) {
            arrayList.remove(connectionRecord);
            if (arrayList.size() == 0) {
                serviceRecord.removeConnection(asBinder);
            }
        }
        appBindRecord.connections.remove(connectionRecord);
        connectionRecord.stopAssociation();
        ActivityServiceConnectionsHolder<ConnectionRecord> activityServiceConnectionsHolder2 = connectionRecord.activity;
        if (activityServiceConnectionsHolder2 != null && activityServiceConnectionsHolder2 != activityServiceConnectionsHolder) {
            activityServiceConnectionsHolder2.removeConnection(connectionRecord);
        }
        ProcessRecord processRecord3 = appBindRecord.client;
        if (processRecord3 != processRecord) {
            ProcessServiceRecord processServiceRecord = processRecord3.mServices;
            processServiceRecord.removeConnection(connectionRecord);
            if (connectionRecord.hasFlag(8)) {
                processServiceRecord.updateHasAboveClientLocked();
            }
            if (connectionRecord.hasFlag(16777216)) {
                serviceRecord.updateAllowlistManager();
                if (!serviceRecord.allowlistManager && (processRecord2 = serviceRecord.app) != null) {
                    updateAllowlistManagerLocked(processRecord2.mServices);
                }
            }
            if (connectionRecord.hasFlag(AudioDevice.OUT_FM)) {
                serviceRecord.updateIsAllowedBgActivityStartsByBinding();
            }
            if (connectionRecord.hasFlag(65536)) {
                processServiceRecord.updateHasTopStartedAlmostPerceptibleServices();
            }
            ProcessRecord processRecord4 = serviceRecord.app;
            if (processRecord4 != null) {
                updateServiceClientActivitiesLocked(processRecord4.mServices, connectionRecord, true);
            }
        }
        ArrayList<ConnectionRecord> arrayList2 = this.mServiceConnections.get(asBinder);
        if (arrayList2 != null) {
            arrayList2.remove(connectionRecord);
            if (arrayList2.size() == 0) {
                this.mServiceConnections.remove(asBinder);
            }
        }
        this.mActiveServicesExt.hookUpdateServiceBindStatus(serviceRecord, appBindRecord.intent.intent.getIntent().getAction(), false);
        ActivityManagerService activityManagerService = this.mAm;
        ProcessRecord processRecord5 = appBindRecord.client;
        int i = processRecord5.uid;
        String str = processRecord5.processName;
        ApplicationInfo applicationInfo = serviceRecord.appInfo;
        activityManagerService.stopAssociationLocked(i, str, applicationInfo.uid, applicationInfo.longVersionCode, serviceRecord.instanceName, serviceRecord.processName);
        this.mActiveServicesExt.noteAssociation(appBindRecord.client.uid, serviceRecord.appInfo.uid, false);
        if (appBindRecord.connections.size() == 0) {
            appBindRecord.intent.apps.remove(appBindRecord.client);
        }
        if (connectionRecord.serviceDead) {
            return;
        }
        if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
            Slog.v(TAG_SERVICE, "Disconnecting binding " + appBindRecord.intent + ": shouldUnbind=" + appBindRecord.intent.hasBound);
        }
        ProcessRecord processRecord6 = serviceRecord.app;
        if (processRecord6 != null && processRecord6.getThread() != null && appBindRecord.intent.apps.size() == 0 && appBindRecord.intent.hasBound) {
            try {
                bumpServiceExecutingLocked(serviceRecord, false, "unbind", 5);
                if (appBindRecord.client != serviceRecord.app && connectionRecord.notHasFlag(32) && serviceRecord.app.mState.getSetProcState() <= 13) {
                    this.mAm.updateLruProcessLocked(serviceRecord.app, false, null);
                }
                IntentBindRecord intentBindRecord = appBindRecord.intent;
                intentBindRecord.hasBound = false;
                intentBindRecord.doRebind = false;
                serviceRecord.app.getThread().scheduleUnbindService(serviceRecord, appBindRecord.intent.intent.getIntent());
            } catch (Exception e) {
                Slog.w("ActivityManager", "Exception when unbinding service " + serviceRecord.shortInstanceName, e);
                serviceProcessGoneLocked(serviceRecord, z);
            }
        }
        if (serviceRecord.getConnections().isEmpty()) {
            this.mPendingServices.remove(serviceRecord);
            this.mPendingBringups.remove(serviceRecord);
        }
        if (connectionRecord.hasFlag(1)) {
            boolean hasAutoCreateConnections = serviceRecord.hasAutoCreateConnections();
            if (!hasAutoCreateConnections && serviceRecord.tracker != null) {
                synchronized (this.mAm.mProcessStats.mLock) {
                    serviceRecord.tracker.setBound(false, this.mAm.mProcessStats.getMemFactorLocked(), SystemClock.uptimeMillis());
                }
            }
            bringDownServiceIfNeededLocked(serviceRecord, true, hasAutoCreateConnections, z, "removeConnection");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void serviceDoneExecutingLocked(ServiceRecord serviceRecord, int i, int i2, int i3, boolean z) {
        boolean contains = this.mDestroyingServices.contains(serviceRecord);
        if (serviceRecord != null) {
            if (i == 1) {
                serviceRecord.callStart = true;
                if (i3 != 1000) {
                    serviceRecord.startCommandResult = i3;
                }
                if (i3 == 0 || i3 == 1) {
                    serviceRecord.findDeliveredStart(i2, false, true);
                    serviceRecord.stopIfKilled = false;
                } else if (i3 == 2) {
                    serviceRecord.findDeliveredStart(i2, false, true);
                    if (serviceRecord.getLastStartId() == i2) {
                        serviceRecord.stopIfKilled = true;
                    }
                } else if (i3 == 3) {
                    ServiceRecord.StartItem findDeliveredStart = serviceRecord.findDeliveredStart(i2, false, false);
                    if (findDeliveredStart != null) {
                        findDeliveredStart.deliveryCount = 0;
                        findDeliveredStart.doneExecutingCount++;
                        serviceRecord.stopIfKilled = true;
                    }
                } else if (i3 == 1000) {
                    serviceRecord.findDeliveredStart(i2, true, true);
                } else {
                    throw new IllegalArgumentException("Unknown service start result: " + i3);
                }
                if (i3 == 0) {
                    serviceRecord.callStart = false;
                }
            } else if (i == 2) {
                if (!contains) {
                    if (serviceRecord.app != null) {
                        Slog.w("ActivityManager", "Service done with onDestroy, but not inDestroying: " + serviceRecord + ", app=" + serviceRecord.app);
                    }
                } else if (serviceRecord.executeNesting != 1) {
                    Slog.w("ActivityManager", "Service done with onDestroy, but executeNesting=" + serviceRecord.executeNesting + ": " + serviceRecord);
                    serviceRecord.executeNesting = 1;
                }
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            serviceDoneExecutingLocked(serviceRecord, contains, contains, z, 20);
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return;
        }
        Slog.w("ActivityManager", "Done executing unknown service from pid " + Binder.getCallingPid());
    }

    private void serviceProcessGoneLocked(ServiceRecord serviceRecord, boolean z) {
        if (serviceRecord.tracker != null) {
            synchronized (this.mAm.mProcessStats.mLock) {
                int memFactorLocked = this.mAm.mProcessStats.getMemFactorLocked();
                long uptimeMillis = SystemClock.uptimeMillis();
                serviceRecord.tracker.setExecuting(false, memFactorLocked, uptimeMillis);
                serviceRecord.tracker.setForeground(false, memFactorLocked, uptimeMillis);
                serviceRecord.tracker.setBound(false, memFactorLocked, uptimeMillis);
                serviceRecord.tracker.setStarted(false, memFactorLocked, uptimeMillis);
            }
        }
        serviceDoneExecutingLocked(serviceRecord, true, true, z, 12);
    }

    private void serviceDoneExecutingLocked(ServiceRecord serviceRecord, boolean z, boolean z2, boolean z3, int i) {
        if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
            Slog.v(TAG_SERVICE, "<<< DONE EXECUTING " + serviceRecord + ": nesting=" + serviceRecord.executeNesting + ", inDestroying=" + z + ", app=" + serviceRecord.app);
        } else if (ActivityManagerDebugConfig.DEBUG_SERVICE_EXECUTING) {
            Slog.v(TAG_SERVICE_EXECUTING, "<<< DONE EXECUTING " + serviceRecord.shortInstanceName);
        }
        int i2 = serviceRecord.executeNesting - 1;
        serviceRecord.executeNesting = i2;
        if (i2 <= 0) {
            ProcessRecord processRecord = serviceRecord.app;
            if (processRecord != null) {
                ProcessServiceRecord processServiceRecord = processRecord.mServices;
                if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                    Slog.v(TAG_SERVICE, "Nesting at 0 of " + serviceRecord.shortInstanceName);
                }
                processServiceRecord.setExecServicesFg(false);
                processServiceRecord.stopExecutingService(serviceRecord);
                if (processServiceRecord.numberOfExecutingServices() == 0) {
                    if (ActivityManagerDebugConfig.DEBUG_SERVICE || ActivityManagerDebugConfig.DEBUG_SERVICE_EXECUTING) {
                        Slog.v(TAG_SERVICE_EXECUTING, "No more executingServices of " + serviceRecord.shortInstanceName);
                    }
                    this.mAm.mHandler.removeMessages(12, serviceRecord.app);
                } else if (serviceRecord.executeFg) {
                    int numberOfExecutingServices = processServiceRecord.numberOfExecutingServices() - 1;
                    while (true) {
                        if (numberOfExecutingServices < 0) {
                            break;
                        }
                        if (processServiceRecord.getExecutingServiceAt(numberOfExecutingServices).executeFg) {
                            processServiceRecord.setExecServicesFg(true);
                            break;
                        }
                        numberOfExecutingServices--;
                    }
                }
                if (z) {
                    if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                        Slog.v(TAG_SERVICE, "doneExecuting remove destroying " + serviceRecord);
                    }
                    this.mDestroyingServices.remove(serviceRecord);
                    serviceRecord.bindings.clear();
                }
                if (z3) {
                    this.mAm.enqueueOomAdjTargetLocked(serviceRecord.app);
                } else {
                    this.mAm.updateOomAdjLocked(serviceRecord.app, i);
                }
            }
            serviceRecord.executeFg = false;
            if (serviceRecord.tracker != null) {
                synchronized (this.mAm.mProcessStats.mLock) {
                    int memFactorLocked = this.mAm.mProcessStats.getMemFactorLocked();
                    long uptimeMillis = SystemClock.uptimeMillis();
                    serviceRecord.tracker.setExecuting(false, memFactorLocked, uptimeMillis);
                    serviceRecord.tracker.setForeground(false, memFactorLocked, uptimeMillis);
                    if (z2) {
                        serviceRecord.tracker.clearCurrentOwner(serviceRecord, false);
                        serviceRecord.tracker = null;
                    }
                }
            }
            if (z2) {
                ProcessRecord processRecord2 = serviceRecord.app;
                if (processRecord2 != null && !processRecord2.isPersistent()) {
                    stopServiceAndUpdateAllowlistManagerLocked(serviceRecord);
                }
                serviceRecord.setProcess(null, null, 0, null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean attachApplicationLocked(ProcessRecord processRecord, String str) throws RemoteException {
        boolean z;
        long j;
        processRecord.mState.setBackgroundRestricted(appRestrictedAnyInBackground(processRecord.uid, processRecord.info.packageName));
        if (this.mPendingServices.size() > 0) {
            ServiceRecord serviceRecord = null;
            int i = 0;
            z = false;
            while (i < this.mPendingServices.size()) {
                try {
                    ServiceRecord serviceRecord2 = this.mPendingServices.get(i);
                    try {
                        if (processRecord == serviceRecord2.isolationHostProc || (processRecord.uid == serviceRecord2.appInfo.uid && str.equals(serviceRecord2.processName))) {
                            IApplicationThread thread = processRecord.getThread();
                            int pid = processRecord.getPid();
                            UidRecord uidRecord = processRecord.getUidRecord();
                            this.mPendingServices.remove(i);
                            int i2 = i - 1;
                            ApplicationInfo applicationInfo = serviceRecord2.appInfo;
                            processRecord.addPackage(applicationInfo.packageName, applicationInfo.longVersionCode, this.mAm.mProcessStats);
                            try {
                                if (Trace.isTagEnabled(64L)) {
                                    Trace.traceBegin(64L, "realStartServiceLocked: " + serviceRecord2.shortInstanceName);
                                }
                                j = 64;
                            } catch (Throwable th) {
                                th = th;
                                j = 64;
                            }
                            try {
                                realStartServiceLocked(serviceRecord2, processRecord, thread, pid, uidRecord, serviceRecord2.createdFromFg, true);
                                Trace.traceEnd(64L);
                                if (!isServiceNeededLocked(serviceRecord2, false, false)) {
                                    bringDownServiceLocked(serviceRecord2, true);
                                }
                                this.mAm.updateOomAdjPendingTargetsLocked(6);
                                z = true;
                                i = i2;
                            } catch (Throwable th2) {
                                th = th2;
                                Trace.traceEnd(j);
                                throw th;
                            }
                        }
                        i++;
                        serviceRecord = serviceRecord2;
                    } catch (RemoteException e) {
                        e = e;
                        serviceRecord = serviceRecord2;
                        Slog.w("ActivityManager", "Exception in new application when starting service " + serviceRecord.shortInstanceName, e);
                        throw e;
                    }
                } catch (RemoteException e2) {
                    e = e2;
                }
            }
        } else {
            z = false;
        }
        if (this.mRestartingServices.size() > 0) {
            boolean z2 = false;
            for (int i3 = 0; i3 < this.mRestartingServices.size(); i3++) {
                ServiceRecord serviceRecord3 = this.mRestartingServices.get(i3);
                if (processRecord == serviceRecord3.isolationHostProc || (processRecord.uid == serviceRecord3.appInfo.uid && str.equals(serviceRecord3.processName))) {
                    this.mAm.mHandler.removeCallbacks(serviceRecord3.restarter);
                    this.mAm.mHandler.post(serviceRecord3.restarter);
                    z2 = true;
                }
            }
            if (z2) {
                this.mAm.mHandler.post(new Runnable() { // from class: com.android.server.am.ActiveServices$$ExternalSyntheticLambda7
                    @Override // java.lang.Runnable
                    public final void run() {
                        ActiveServices.this.lambda$attachApplicationLocked$2();
                    }
                });
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$attachApplicationLocked$2() {
        long uptimeMillis = SystemClock.uptimeMillis();
        ActivityManagerService activityManagerService = this.mAm;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                rescheduleServiceRestartIfPossibleLocked(getExtraRestartTimeInBetweenLocked(), this.mAm.mConstants.SERVICE_MIN_RESTART_TIME_BETWEEN, "other", uptimeMillis);
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void processStartTimedOutLocked(ProcessRecord processRecord) {
        int size = this.mPendingServices.size();
        int i = 0;
        boolean z = false;
        while (i < size) {
            ServiceRecord serviceRecord = this.mPendingServices.get(i);
            if ((processRecord.uid == serviceRecord.appInfo.uid && processRecord.processName.equals(serviceRecord.processName)) || serviceRecord.isolationHostProc == processRecord) {
                Slog.w("ActivityManager", "Forcing bringing down service: " + serviceRecord);
                serviceRecord.isolationHostProc = null;
                this.mPendingServices.remove(i);
                size = this.mPendingServices.size();
                i--;
                ServiceRecord remove = getServiceMapLocked(serviceRecord.userId).mServicesByInstanceName.remove(serviceRecord.instanceName);
                if (remove != null && remove != serviceRecord) {
                    Slog.i("ActivityManager", "trying to bring down a service record that has been brought down once " + serviceRecord);
                } else if (!this.mActiveServicesExt.interceptProcessStartTimedOutBeforeBringDown(getServiceMapLocked(serviceRecord.userId), serviceRecord)) {
                    bringDownServiceLocked(serviceRecord, true);
                }
                z = true;
            }
            i++;
        }
        if (z) {
            this.mAm.updateOomAdjPendingTargetsLocked(12);
        }
    }

    private boolean collectPackageServicesLocked(String str, Set<String> set, boolean z, boolean z2, ArrayMap<ComponentName, ServiceRecord> arrayMap) {
        ProcessRecord processRecord;
        boolean z3 = false;
        for (int size = arrayMap.size() - 1; size >= 0; size--) {
            ServiceRecord valueAt = arrayMap.valueAt(size);
            if ((str == null || (valueAt.packageName.equals(str) && (set == null || set.contains(valueAt.name.getClassName())))) && ((processRecord = valueAt.app) == null || z || !processRecord.isPersistent())) {
                if (!z2) {
                    return true;
                }
                Slog.i("ActivityManager", "  Force stopping service " + valueAt);
                ProcessRecord processRecord2 = valueAt.app;
                if (processRecord2 != null && !processRecord2.isPersistent()) {
                    stopServiceAndUpdateAllowlistManagerLocked(valueAt);
                }
                valueAt.setProcess(null, null, 0, null);
                valueAt.isolationHostProc = null;
                if (this.mTmpCollectionResults == null) {
                    this.mTmpCollectionResults = new ArrayList<>();
                }
                this.mTmpCollectionResults.add(valueAt);
                z3 = true;
            }
        }
        return z3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean bringDownDisabledPackageServicesLocked(String str, Set<String> set, int i, boolean z, boolean z2, boolean z3) {
        ArrayList<ServiceRecord> arrayList = this.mTmpCollectionResults;
        if (arrayList != null) {
            arrayList.clear();
        }
        if (i == -1) {
            for (int size = this.mServiceMap.size() - 1; size >= 0; size--) {
                r2 |= collectPackageServicesLocked(str, set, z, z3, this.mServiceMap.valueAt(size).mServicesByInstanceName);
                if (!z3 && r2) {
                    return true;
                }
                if (z3 && set == null) {
                    forceStopPackageLocked(str, this.mServiceMap.valueAt(size).mUserId);
                }
            }
        } else {
            ServiceMap serviceMap = this.mServiceMap.get(i);
            r2 = serviceMap != null ? collectPackageServicesLocked(str, set, z, z3, serviceMap.mServicesByInstanceName) : false;
            if (z3 && set == null) {
                forceStopPackageLocked(str, i);
            }
        }
        ArrayList<ServiceRecord> arrayList2 = this.mTmpCollectionResults;
        if (arrayList2 != null) {
            int size2 = arrayList2.size();
            for (int i2 = size2 - 1; i2 >= 0; i2--) {
                ServiceRecord serviceRecord = this.mTmpCollectionResults.get(i2);
                ServiceRecord remove = getServiceMapLocked(serviceRecord.userId).mServicesByInstanceName.remove(serviceRecord.instanceName);
                if (remove != null && remove != serviceRecord) {
                    Slog.i("ActivityManager", "trying to bring down a service record that has been brought down once " + serviceRecord);
                } else if (!this.mActiveServicesExt.interceptBringDownDisabledPackageServicesBeforeBringDown(getServiceMapLocked(this.mTmpCollectionResults.get(i2).userId), this.mTmpCollectionResults.get(i2))) {
                    bringDownServiceLocked(this.mTmpCollectionResults.get(i2), true);
                }
            }
            if (size2 > 0) {
                this.mAm.updateOomAdjPendingTargetsLocked(22);
            }
            if (z2 && !this.mTmpCollectionResults.isEmpty()) {
                final ArrayList arrayList3 = (ArrayList) this.mTmpCollectionResults.clone();
                this.mAm.mHandler.postDelayed(new Runnable() { // from class: com.android.server.am.ActiveServices$$ExternalSyntheticLambda8
                    @Override // java.lang.Runnable
                    public final void run() {
                        ActiveServices.lambda$bringDownDisabledPackageServicesLocked$3(arrayList3);
                    }
                }, 250L);
            }
            this.mTmpCollectionResults.clear();
        }
        return r2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$bringDownDisabledPackageServicesLocked$3(ArrayList arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            ((ServiceRecord) arrayList.get(i)).cancelNotification();
        }
    }

    @GuardedBy({"mAm"})
    private void signalForegroundServiceObserversLocked(ServiceRecord serviceRecord) {
        int beginBroadcast = this.mFgsObservers.beginBroadcast();
        for (int i = 0; i < beginBroadcast; i++) {
            try {
                this.mFgsObservers.getBroadcastItem(i).onForegroundStateChanged(serviceRecord, serviceRecord.appInfo.packageName, serviceRecord.userId, serviceRecord.isForeground);
            } catch (RemoteException unused) {
            }
        }
        this.mFgsObservers.finishBroadcast();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mAm"})
    public boolean registerForegroundServiceObserverLocked(int i, IForegroundServiceObserver iForegroundServiceObserver) {
        try {
            int size = this.mServiceMap.size();
            for (int i2 = 0; i2 < size; i2++) {
                ServiceMap valueAt = this.mServiceMap.valueAt(i2);
                if (valueAt != null) {
                    int size2 = valueAt.mServicesByInstanceName.size();
                    for (int i3 = 0; i3 < size2; i3++) {
                        ServiceRecord valueAt2 = valueAt.mServicesByInstanceName.valueAt(i3);
                        if (valueAt2.isForeground) {
                            ApplicationInfo applicationInfo = valueAt2.appInfo;
                            if (i == applicationInfo.uid) {
                                iForegroundServiceObserver.onForegroundStateChanged(valueAt2, applicationInfo.packageName, valueAt2.userId, true);
                            }
                        }
                    }
                }
            }
            this.mFgsObservers.register(iForegroundServiceObserver);
            return true;
        } catch (RemoteException unused) {
            Slog.e(TAG_SERVICE, "Bad FGS observer from uid " + i);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forceStopPackageLocked(String str, int i) {
        ServiceMap serviceMap = this.mServiceMap.get(i);
        if (serviceMap != null && serviceMap.mActiveForegroundApps.size() > 0) {
            for (int size = serviceMap.mActiveForegroundApps.size() - 1; size >= 0; size--) {
                if (serviceMap.mActiveForegroundApps.valueAt(size).mPackageName.equals(str)) {
                    serviceMap.mActiveForegroundApps.removeAt(size);
                    serviceMap.mActiveForegroundAppsChanged = true;
                }
            }
            if (serviceMap.mActiveForegroundAppsChanged) {
                requestUpdateActiveForegroundAppsLocked(serviceMap, 0L);
            }
        }
        for (int size2 = this.mPendingBringups.size() - 1; size2 >= 0; size2--) {
            ServiceRecord keyAt = this.mPendingBringups.keyAt(size2);
            if (TextUtils.equals(keyAt.packageName, str) && keyAt.userId == i) {
                this.mPendingBringups.removeAt(size2);
            }
        }
        removeServiceRestartBackoffEnabledLocked(str);
        removeServiceNotificationDeferralsLocked(str, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cleanUpServices(int i, ComponentName componentName, Intent intent) {
        ArrayList arrayList = new ArrayList();
        ArrayMap<ComponentName, ServiceRecord> servicesLocked = getServicesLocked(i);
        boolean z = true;
        for (int size = servicesLocked.size() - 1; size >= 0; size--) {
            ServiceRecord valueAt = servicesLocked.valueAt(size);
            if (valueAt.packageName.equals(componentName.getPackageName())) {
                arrayList.add(valueAt);
            }
        }
        boolean z2 = false;
        for (int size2 = arrayList.size() - 1; size2 >= 0; size2--) {
            ServiceRecord serviceRecord = (ServiceRecord) arrayList.get(size2);
            if (serviceRecord.startRequested) {
                if ((serviceRecord.serviceInfo.flags & (z ? 1 : 0)) != 0) {
                    Slog.i("ActivityManager", "Stopping service " + serviceRecord.shortInstanceName + ": remove task");
                    stopServiceLocked(serviceRecord, z);
                    z2 = z ? 1 : 0;
                } else {
                    serviceRecord.pendingStarts.add(new ServiceRecord.StartItem(serviceRecord, true, serviceRecord.getLastStartId(), intent, null, 0, null, null, -1));
                    ProcessRecord processRecord = serviceRecord.app;
                    if (processRecord == null || processRecord.getThread() == null) {
                        z = true;
                    } else {
                        z = true;
                        z = true;
                        try {
                            sendServiceArgsLocked(serviceRecord, true, false);
                        } catch (TransactionTooLargeException unused) {
                        }
                    }
                }
            }
        }
        if (z2) {
            this.mAm.updateOomAdjPendingTargetsLocked(17);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void killServicesLocked(ProcessRecord processRecord, boolean z) {
        ProcessServiceRecord processServiceRecord = processRecord.mServices;
        int numberOfConnections = processServiceRecord.numberOfConnections();
        if (numberOfConnections > 1000) {
            Slog.d("ActivityManager", "killServicesLocked app:" + processRecord + ", connection size:" + numberOfConnections);
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        for (int numberOfConnections2 = processServiceRecord.numberOfConnections() - 1; numberOfConnections2 >= 0; numberOfConnections2--) {
            try {
                removeConnectionLocked(processServiceRecord.getConnectionAt(numberOfConnections2), processRecord, null, true);
            } catch (ArrayIndexOutOfBoundsException e) {
                Slog.e("ActivityManager", "Failed to get connection record!", e);
            }
        }
        this.mActiveServicesExt.hookKillServicesWhenRemoveServiceConnection(processRecord, SystemClock.uptimeMillis() - uptimeMillis);
        updateServiceConnectionActivitiesLocked(processServiceRecord);
        processServiceRecord.removeAllConnections();
        processServiceRecord.mAllowlistManager = false;
        for (int numberOfRunningServices = processServiceRecord.numberOfRunningServices() - 1; numberOfRunningServices >= 0; numberOfRunningServices--) {
            ServiceRecord runningServiceAt = processServiceRecord.getRunningServiceAt(numberOfRunningServices);
            this.mAm.mBatteryStatsService.noteServiceStopLaunch(runningServiceAt.appInfo.uid, runningServiceAt.name.getPackageName(), runningServiceAt.name.getClassName());
            ProcessRecord processRecord2 = runningServiceAt.app;
            if (processRecord2 != processRecord && processRecord2 != null && !processRecord2.isPersistent()) {
                runningServiceAt.app.mServices.stopService(runningServiceAt);
                runningServiceAt.app.mServices.updateBoundClientUids();
            }
            runningServiceAt.setProcess(null, null, 0, null);
            runningServiceAt.isolationHostProc = null;
            runningServiceAt.executeNesting = 0;
            synchronized (this.mAm.mProcessStats.mLock) {
                runningServiceAt.forceClearTracker();
            }
            if (this.mDestroyingServices.remove(runningServiceAt) && ActivityManagerDebugConfig.DEBUG_SERVICE) {
                Slog.v(TAG_SERVICE, "killServices remove destroying " + runningServiceAt);
            }
            for (int size = runningServiceAt.bindings.size() - 1; size >= 0; size--) {
                IntentBindRecord valueAt = runningServiceAt.bindings.valueAt(size);
                if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                    Slog.v(TAG_SERVICE, "Killing binding " + valueAt + ": shouldUnbind=" + valueAt.hasBound);
                }
                valueAt.binder = null;
                valueAt.hasBound = false;
                valueAt.received = false;
                valueAt.requested = false;
                for (int size2 = valueAt.apps.size() - 1; size2 >= 0; size2--) {
                    ProcessRecord keyAt = valueAt.apps.keyAt(size2);
                    if (!keyAt.isKilledByAm() && keyAt.getThread() != null) {
                        AppBindRecord valueAt2 = valueAt.apps.valueAt(size2);
                        for (int size3 = valueAt2.connections.size() - 1; size3 >= 0; size3--) {
                            ConnectionRecord valueAt3 = valueAt2.connections.valueAt(size3);
                            if (!valueAt3.hasFlag(1) || !valueAt3.notHasFlag(48)) {
                            }
                        }
                    }
                }
            }
        }
        ServiceMap serviceMapLocked = getServiceMapLocked(processRecord.userId);
        for (int numberOfRunningServices2 = processServiceRecord.numberOfRunningServices() - 1; numberOfRunningServices2 >= 0; numberOfRunningServices2--) {
            ServiceRecord runningServiceAt2 = processServiceRecord.getRunningServiceAt(numberOfRunningServices2);
            if (!processRecord.isPersistent()) {
                processServiceRecord.stopService(runningServiceAt2);
                processServiceRecord.updateBoundClientUids();
            }
            ServiceRecord serviceRecord = serviceMapLocked.mServicesByInstanceName.get(runningServiceAt2.instanceName);
            if (serviceRecord != runningServiceAt2) {
                if (serviceRecord != null) {
                    Slog.e("ActivityManager", "Service " + runningServiceAt2 + " in process " + processRecord + " not same as in map: " + serviceRecord);
                }
            } else if (z && runningServiceAt2.crashCount >= this.mAm.mConstants.BOUND_SERVICE_MAX_CRASH_RETRY && (runningServiceAt2.serviceInfo.applicationInfo.flags & 8) == 0) {
                Slog.w("ActivityManager", "Service crashed " + runningServiceAt2.crashCount + " times, stopping: " + runningServiceAt2);
                Object[] objArr = new Object[4];
                objArr[0] = Integer.valueOf(runningServiceAt2.userId);
                objArr[1] = Integer.valueOf(runningServiceAt2.crashCount);
                objArr[2] = runningServiceAt2.shortInstanceName;
                ProcessRecord processRecord3 = runningServiceAt2.app;
                objArr[3] = Integer.valueOf(processRecord3 != null ? processRecord3.getPid() : -1);
                EventLog.writeEvent(EventLogTags.AM_SERVICE_CRASHED_TOO_MUCH, objArr);
                bringDownServiceLocked(runningServiceAt2, true);
            } else if (!z || !this.mAm.mUserController.isUserRunning(runningServiceAt2.userId, 0)) {
                bringDownServiceLocked(runningServiceAt2, true);
            } else if (!scheduleServiceRestartLocked(runningServiceAt2, true)) {
                bringDownServiceLocked(runningServiceAt2, true);
            } else if (runningServiceAt2.canStopIfKilled(false)) {
                runningServiceAt2.startRequested = false;
                if (runningServiceAt2.tracker != null) {
                    synchronized (this.mAm.mProcessStats.mLock) {
                        runningServiceAt2.tracker.setStarted(false, this.mAm.mProcessStats.getMemFactorLocked(), SystemClock.uptimeMillis());
                    }
                } else {
                    continue;
                }
            } else {
                continue;
            }
        }
        this.mAm.updateOomAdjPendingTargetsLocked(19);
        if (!z) {
            processServiceRecord.stopAllServices();
            processServiceRecord.clearBoundClientUids();
            for (int size4 = this.mRestartingServices.size() - 1; size4 >= 0; size4--) {
                ServiceRecord serviceRecord2 = this.mRestartingServices.get(size4);
                if (serviceRecord2.processName.equals(processRecord.processName) && serviceRecord2.serviceInfo.applicationInfo.uid == processRecord.info.uid) {
                    this.mRestartingServices.remove(size4);
                    clearRestartingIfNeededLocked(serviceRecord2);
                }
            }
            for (int size5 = this.mPendingServices.size() - 1; size5 >= 0; size5--) {
                ServiceRecord serviceRecord3 = this.mPendingServices.get(size5);
                if (serviceRecord3.processName.equals(processRecord.processName) && serviceRecord3.serviceInfo.applicationInfo.uid == processRecord.info.uid) {
                    this.mPendingServices.remove(size5);
                }
            }
            for (int size6 = this.mPendingBringups.size() - 1; size6 >= 0; size6--) {
                ServiceRecord keyAt2 = this.mPendingBringups.keyAt(size6);
                if (keyAt2.processName.equals(processRecord.processName) && keyAt2.serviceInfo.applicationInfo.uid == processRecord.info.uid) {
                    this.mPendingBringups.removeAt(size6);
                }
            }
        }
        int size7 = this.mDestroyingServices.size();
        while (size7 > 0) {
            size7--;
            ServiceRecord serviceRecord4 = this.mDestroyingServices.get(size7);
            if (serviceRecord4.app == processRecord) {
                synchronized (this.mAm.mProcessStats.mLock) {
                    serviceRecord4.forceClearTracker();
                }
                this.mDestroyingServices.remove(size7);
                if (ActivityManagerDebugConfig.DEBUG_SERVICE) {
                    Slog.v(TAG_SERVICE, "killServices remove destroying " + serviceRecord4);
                }
            }
        }
        processServiceRecord.stopAllExecutingServices();
    }

    ActivityManager.RunningServiceInfo makeRunningServiceInfoLocked(ServiceRecord serviceRecord) {
        ActivityManager.RunningServiceInfo runningServiceInfo = new ActivityManager.RunningServiceInfo();
        runningServiceInfo.service = serviceRecord.name;
        ProcessRecord processRecord = serviceRecord.app;
        if (processRecord != null) {
            runningServiceInfo.pid = processRecord.getPid();
        }
        runningServiceInfo.uid = serviceRecord.appInfo.uid;
        runningServiceInfo.process = serviceRecord.processName;
        runningServiceInfo.foreground = serviceRecord.isForeground;
        runningServiceInfo.activeSince = serviceRecord.createRealTime;
        runningServiceInfo.started = serviceRecord.startRequested;
        runningServiceInfo.clientCount = serviceRecord.getConnections().size();
        runningServiceInfo.crashCount = serviceRecord.crashCount;
        runningServiceInfo.lastActivityTime = serviceRecord.lastActivity;
        if (serviceRecord.isForeground) {
            runningServiceInfo.flags |= 2;
        }
        if (serviceRecord.startRequested) {
            runningServiceInfo.flags |= 1;
        }
        ProcessRecord processRecord2 = serviceRecord.app;
        if (processRecord2 != null && processRecord2.getPid() == ActivityManagerService.MY_PID) {
            runningServiceInfo.flags |= 4;
        }
        ProcessRecord processRecord3 = serviceRecord.app;
        if (processRecord3 != null && processRecord3.isPersistent()) {
            runningServiceInfo.flags |= 8;
        }
        ArrayMap<IBinder, ArrayList<ConnectionRecord>> connections = serviceRecord.getConnections();
        for (int size = connections.size() - 1; size >= 0; size--) {
            ArrayList<ConnectionRecord> valueAt = connections.valueAt(size);
            for (int i = 0; i < valueAt.size(); i++) {
                ConnectionRecord connectionRecord = valueAt.get(i);
                if (connectionRecord.clientLabel != 0) {
                    runningServiceInfo.clientPackage = connectionRecord.binding.client.info.packageName;
                    runningServiceInfo.clientLabel = connectionRecord.clientLabel;
                    return runningServiceInfo;
                }
            }
        }
        return runningServiceInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<ActivityManager.RunningServiceInfo> getRunningServiceInfoLocked(int i, int i2, int i3, boolean z, boolean z2) {
        ProcessRecord processRecord;
        ProcessRecord processRecord2;
        ArrayList arrayList = new ArrayList();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        int i4 = 0;
        try {
            if (z2) {
                int[] users = this.mAm.mUserController.getUsers();
                for (int i5 = 0; i5 < users.length && arrayList.size() < i; i5++) {
                    ArrayMap<ComponentName, ServiceRecord> servicesLocked = getServicesLocked(users[i5]);
                    for (int i6 = 0; i6 < servicesLocked.size() && arrayList.size() < i; i6++) {
                        arrayList.add(makeRunningServiceInfoLocked(servicesLocked.valueAt(i6)));
                    }
                }
                while (i4 < this.mRestartingServices.size() && arrayList.size() < i) {
                    ServiceRecord serviceRecord = this.mRestartingServices.get(i4);
                    ActivityManager.RunningServiceInfo makeRunningServiceInfoLocked = makeRunningServiceInfoLocked(serviceRecord);
                    makeRunningServiceInfoLocked.restarting = serviceRecord.nextRestartTime;
                    arrayList.add(makeRunningServiceInfoLocked);
                    i4++;
                }
            } else {
                int userId = UserHandle.getUserId(i3);
                ArrayMap<ComponentName, ServiceRecord> servicesLocked2 = getServicesLocked(userId);
                for (int i7 = 0; i7 < servicesLocked2.size() && arrayList.size() < i; i7++) {
                    ServiceRecord valueAt = servicesLocked2.valueAt(i7);
                    if (z || ((processRecord2 = valueAt.app) != null && processRecord2.uid == i3)) {
                        arrayList.add(makeRunningServiceInfoLocked(valueAt));
                    }
                }
                while (i4 < this.mRestartingServices.size() && arrayList.size() < i) {
                    ServiceRecord serviceRecord2 = this.mRestartingServices.get(i4);
                    if (serviceRecord2.userId == userId && (z || ((processRecord = serviceRecord2.app) != null && processRecord.uid == i3))) {
                        ActivityManager.RunningServiceInfo makeRunningServiceInfoLocked2 = makeRunningServiceInfoLocked(serviceRecord2);
                        makeRunningServiceInfoLocked2.restarting = serviceRecord2.nextRestartTime;
                        arrayList.add(makeRunningServiceInfoLocked2);
                    }
                    i4++;
                }
            }
            return arrayList;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public PendingIntent getRunningServiceControlPanelLocked(ComponentName componentName) {
        ServiceRecord serviceByNameLocked = getServiceByNameLocked(componentName, UserHandle.getUserId(Binder.getCallingUid()));
        if (serviceByNameLocked == null) {
            return null;
        }
        ArrayMap<IBinder, ArrayList<ConnectionRecord>> connections = serviceByNameLocked.getConnections();
        for (int size = connections.size() - 1; size >= 0; size--) {
            ArrayList<ConnectionRecord> valueAt = connections.valueAt(size);
            for (int i = 0; i < valueAt.size(); i++) {
                if (valueAt.get(i).clientIntent != null) {
                    return valueAt.get(i).clientIntent;
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Finally extract failed */
    public void serviceTimeout(ProcessRecord processRecord) {
        long j;
        TimeoutRecord timeoutRecord;
        ServiceRecord serviceRecord;
        long j2;
        try {
            Trace.traceBegin(64L, "serviceTimeout()");
            ActivityManagerService activityManagerService = this.mAm;
            ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    if (!processRecord.isDebugging()) {
                        ProcessServiceRecord processServiceRecord = processRecord.mServices;
                        if (processServiceRecord.numberOfExecutingServices() != 0 && processRecord.getThread() != null && !processRecord.isKilled()) {
                            long uptimeMillis = SystemClock.uptimeMillis();
                            if (processServiceRecord.shouldExecServicesFg()) {
                                j = this.mAm.mConstants.SERVICE_TIMEOUT;
                            } else {
                                j = this.mAm.mConstants.SERVICE_BACKGROUND_TIMEOUT;
                            }
                            long j3 = uptimeMillis - j;
                            int numberOfExecutingServices = processServiceRecord.numberOfExecutingServices() - 1;
                            long j4 = 0;
                            while (true) {
                                timeoutRecord = null;
                                if (numberOfExecutingServices < 0) {
                                    serviceRecord = null;
                                    break;
                                }
                                serviceRecord = processServiceRecord.getExecutingServiceAt(numberOfExecutingServices);
                                long j5 = serviceRecord.executingStart;
                                if (j5 < j3) {
                                    break;
                                }
                                if (j5 > j4) {
                                    j4 = j5;
                                }
                                numberOfExecutingServices--;
                            }
                            if (serviceRecord != null && this.mAm.mProcessList.isInLruListLOSP(processRecord)) {
                                Slog.w("ActivityManager", "Timeout executing service: " + serviceRecord);
                                StringWriter stringWriter = new StringWriter();
                                FastPrintWriter fastPrintWriter = new FastPrintWriter(stringWriter, false, 1024);
                                fastPrintWriter.println(serviceRecord);
                                serviceRecord.dump((PrintWriter) fastPrintWriter, "    ");
                                fastPrintWriter.close();
                                this.mLastAnrDump = stringWriter.toString();
                                this.mAm.mHandler.removeCallbacks(this.mLastAnrDumpClearer);
                                this.mAm.mHandler.postDelayed(this.mLastAnrDumpClearer, 7200000L);
                                timeoutRecord = TimeoutRecord.forServiceExec("executing service " + serviceRecord.shortInstanceName);
                            } else {
                                Message obtainMessage = this.mAm.mHandler.obtainMessage(12);
                                obtainMessage.obj = processRecord;
                                ActivityManagerService.MainHandler mainHandler = this.mAm.mHandler;
                                if (processServiceRecord.shouldExecServicesFg()) {
                                    j2 = this.mAm.mConstants.SERVICE_TIMEOUT;
                                } else {
                                    j2 = this.mAm.mConstants.SERVICE_BACKGROUND_TIMEOUT;
                                }
                                mainHandler.sendMessageAtTime(obtainMessage, j4 + j2);
                            }
                            ActivityManagerService.resetPriorityAfterLockedSection();
                            if (timeoutRecord != null) {
                                this.mAm.mAnrHelper.appNotResponding(processRecord, timeoutRecord);
                            }
                            return;
                        }
                    }
                    ActivityManagerService.resetPriorityAfterLockedSection();
                } catch (Throwable th) {
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Trace.traceEnd(64L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Finally extract failed */
    public void serviceForegroundTimeout(ServiceRecord serviceRecord) {
        try {
            Trace.traceBegin(64L, "serviceForegroundTimeout()");
            TimeoutRecord forServiceStartWithEndTime = TimeoutRecord.forServiceStartWithEndTime("Context.startForegroundService() did not then call Service.startForeground(): " + serviceRecord, SystemClock.uptimeMillis());
            forServiceStartWithEndTime.mLatencyTracker.waitingOnAMSLockStarted();
            ActivityManagerService activityManagerService = this.mAm;
            ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    forServiceStartWithEndTime.mLatencyTracker.waitingOnAMSLockEnded();
                    this.mActiveServicesExt.updateExecutingComponent(serviceRecord.appInfo.uid, "fg-service", 2);
                    if (serviceRecord.fgRequired && serviceRecord.fgWaiting && !serviceRecord.destroying) {
                        ProcessRecord processRecord = serviceRecord.app;
                        if (processRecord == null || !processRecord.isDebugging()) {
                            if (ActivityManagerDebugConfig.DEBUG_BACKGROUND_CHECK) {
                                Slog.i("ActivityManager", "Service foreground-required timeout for " + serviceRecord);
                            }
                            serviceRecord.fgWaiting = false;
                            stopServiceLocked(serviceRecord, false);
                            ActivityManagerService.resetPriorityAfterLockedSection();
                            if (processRecord != null) {
                                Message obtainMessage = this.mAm.mHandler.obtainMessage(67);
                                SomeArgs obtain = SomeArgs.obtain();
                                obtain.arg1 = processRecord;
                                obtain.arg2 = forServiceStartWithEndTime;
                                obtainMessage.obj = obtain;
                                this.mAm.mHandler.sendMessageDelayed(obtainMessage, r8.mConstants.mServiceStartForegroundAnrDelayMs);
                            }
                            return;
                        }
                        ActivityManagerService.resetPriorityAfterLockedSection();
                    }
                    ActivityManagerService.resetPriorityAfterLockedSection();
                } catch (Throwable th) {
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Trace.traceEnd(64L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void serviceForegroundTimeoutANR(ProcessRecord processRecord, TimeoutRecord timeoutRecord) {
        this.mAm.mAnrHelper.appNotResponding(processRecord, timeoutRecord);
    }

    public void updateServiceApplicationInfoLocked(ApplicationInfo applicationInfo) {
        ServiceMap serviceMap = this.mServiceMap.get(UserHandle.getUserId(applicationInfo.uid));
        if (serviceMap != null) {
            ArrayMap<ComponentName, ServiceRecord> arrayMap = serviceMap.mServicesByInstanceName;
            for (int size = arrayMap.size() - 1; size >= 0; size--) {
                ServiceRecord valueAt = arrayMap.valueAt(size);
                if (applicationInfo.packageName.equals(valueAt.appInfo.packageName)) {
                    valueAt.appInfo = applicationInfo;
                    valueAt.serviceInfo.applicationInfo = applicationInfo;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void serviceForegroundCrash(ProcessRecord processRecord, String str, ComponentName componentName) {
        this.mAm.crashApplicationWithTypeWithExtras(processRecord.uid, processRecord.getPid(), processRecord.info.packageName, processRecord.userId, "Context.startForegroundService() did not then call Service.startForeground(): " + str, false, 1, RemoteServiceException.ForegroundServiceDidNotStartInTimeException.createExtrasForService(componentName));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleServiceTimeoutLocked(ProcessRecord processRecord) {
        if (processRecord.mServices.numberOfExecutingServices() == 0 || processRecord.getThread() == null) {
            return;
        }
        Message obtainMessage = this.mAm.mHandler.obtainMessage(12);
        obtainMessage.obj = processRecord;
        this.mAm.mHandler.sendMessageDelayed(obtainMessage, processRecord.mServices.shouldExecServicesFg() ? this.mAm.mConstants.SERVICE_TIMEOUT : this.mAm.mConstants.SERVICE_BACKGROUND_TIMEOUT);
    }

    void scheduleServiceForegroundTransitionTimeoutLocked(ServiceRecord serviceRecord) {
        if (serviceRecord.app.mServices.numberOfExecutingServices() == 0 || serviceRecord.app.getThread() == null) {
            return;
        }
        Message obtainMessage = this.mAm.mHandler.obtainMessage(66);
        obtainMessage.obj = serviceRecord;
        serviceRecord.fgWaiting = true;
        this.mAm.mHandler.sendMessageDelayed(obtainMessage, r2.mConstants.mServiceStartForegroundTimeoutMs);
        this.mActiveServicesExt.updateExecutingComponent(serviceRecord.appInfo.uid, "fg-service", 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class ServiceDumper {
        private final String[] args;
        private final boolean dumpAll;
        private final String dumpPackage;
        private final FileDescriptor fd;
        private final ActivityManagerService.ItemMatcher matcher;
        private final PrintWriter pw;
        private final ArrayList<ServiceRecord> services = new ArrayList<>();
        private final long nowReal = SystemClock.elapsedRealtime();
        private boolean needSep = false;
        private boolean printedAnything = false;
        private boolean printed = false;

        ServiceDumper(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, int i, boolean z, String str) {
            this.fd = fileDescriptor;
            this.pw = printWriter;
            this.args = strArr;
            this.dumpAll = z;
            this.dumpPackage = str;
            ActivityManagerService.ItemMatcher itemMatcher = new ActivityManagerService.ItemMatcher();
            this.matcher = itemMatcher;
            itemMatcher.build(strArr, i);
            for (int i2 : ActiveServices.this.mAm.mUserController.getUsers()) {
                ServiceMap serviceMapLocked = ActiveServices.this.getServiceMapLocked(i2);
                if (serviceMapLocked.mServicesByInstanceName.size() > 0) {
                    for (int i3 = 0; i3 < serviceMapLocked.mServicesByInstanceName.size(); i3++) {
                        ServiceRecord valueAt = serviceMapLocked.mServicesByInstanceName.valueAt(i3);
                        if (this.matcher.match(valueAt, valueAt.name) && (str == null || str.equals(valueAt.appInfo.packageName))) {
                            this.services.add(valueAt);
                        }
                    }
                }
            }
        }

        private void dumpHeaderLocked() {
            this.pw.println("ACTIVITY MANAGER SERVICES (dumpsys activity services)");
            if (ActiveServices.this.mLastAnrDump != null) {
                this.pw.println("  Last ANR service:");
                this.pw.print(ActiveServices.this.mLastAnrDump);
                this.pw.println();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void dumpLocked() {
            dumpHeaderLocked();
            try {
                for (int i : ActiveServices.this.mAm.mUserController.getUsers()) {
                    int i2 = 0;
                    while (i2 < this.services.size() && this.services.get(i2).userId != i) {
                        i2++;
                    }
                    this.printed = false;
                    if (i2 < this.services.size()) {
                        this.needSep = false;
                        while (i2 < this.services.size()) {
                            ServiceRecord serviceRecord = this.services.get(i2);
                            i2++;
                            if (serviceRecord.userId != i) {
                                break;
                            } else {
                                dumpServiceLocalLocked(serviceRecord);
                            }
                        }
                        this.needSep |= this.printed;
                    }
                    dumpUserRemainsLocked(i);
                }
            } catch (Exception e) {
                Slog.w("ActivityManager", "Exception in dumpServicesLocked", e);
            }
            dumpRemainsLocked();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void dumpWithClient() {
            ActivityManagerService activityManagerService = ActiveServices.this.mAm;
            ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    dumpHeaderLocked();
                } finally {
                    ActivityManagerService.resetPriorityAfterLockedSection();
                }
            }
            ActivityManagerService.resetPriorityAfterLockedSection();
            try {
                for (int i : ActiveServices.this.mAm.mUserController.getUsers()) {
                    int i2 = 0;
                    while (i2 < this.services.size() && this.services.get(i2).userId != i) {
                        i2++;
                    }
                    this.printed = false;
                    if (i2 < this.services.size()) {
                        this.needSep = false;
                        while (i2 < this.services.size()) {
                            ServiceRecord serviceRecord = this.services.get(i2);
                            i2++;
                            if (serviceRecord.userId != i) {
                                break;
                            }
                            ActivityManagerService activityManagerService2 = ActiveServices.this.mAm;
                            ActivityManagerService.boostPriorityForLockedSection();
                            synchronized (activityManagerService2) {
                                try {
                                    dumpServiceLocalLocked(serviceRecord);
                                } finally {
                                }
                            }
                            ActivityManagerService.resetPriorityAfterLockedSection();
                            dumpServiceClient(serviceRecord);
                        }
                        this.needSep |= this.printed;
                    }
                    ActivityManagerService activityManagerService3 = ActiveServices.this.mAm;
                    ActivityManagerService.boostPriorityForLockedSection();
                    synchronized (activityManagerService3) {
                        try {
                            dumpUserRemainsLocked(i);
                        } finally {
                        }
                    }
                    ActivityManagerService.resetPriorityAfterLockedSection();
                }
            } catch (Exception e) {
                Slog.w("ActivityManager", "Exception in dumpServicesLocked", e);
            }
            ActivityManagerService activityManagerService4 = ActiveServices.this.mAm;
            ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService4) {
                try {
                    dumpRemainsLocked();
                } finally {
                    ActivityManagerService.resetPriorityAfterLockedSection();
                }
            }
            ActivityManagerService.resetPriorityAfterLockedSection();
        }

        private void dumpUserHeaderLocked(int i) {
            if (!this.printed) {
                if (this.printedAnything) {
                    this.pw.println();
                }
                this.pw.println("  User " + i + " active services:");
                this.printed = true;
            }
            this.printedAnything = true;
            if (this.needSep) {
                this.pw.println();
            }
        }

        private void dumpServiceLocalLocked(ServiceRecord serviceRecord) {
            dumpUserHeaderLocked(serviceRecord.userId);
            this.pw.print("  * ");
            this.pw.println(serviceRecord);
            if (this.dumpAll) {
                serviceRecord.dump(this.pw, "    ");
                this.needSep = true;
                return;
            }
            this.pw.print("    app=");
            this.pw.println(serviceRecord.app);
            this.pw.print("    created=");
            TimeUtils.formatDuration(serviceRecord.createRealTime, this.nowReal, this.pw);
            this.pw.print(" started=");
            this.pw.print(serviceRecord.startRequested);
            this.pw.print(" connections=");
            ArrayMap<IBinder, ArrayList<ConnectionRecord>> connections = serviceRecord.getConnections();
            this.pw.println(connections.size());
            if (connections.size() > 0) {
                this.pw.println("    Connections:");
                for (int i = 0; i < connections.size(); i++) {
                    ArrayList<ConnectionRecord> valueAt = connections.valueAt(i);
                    for (int i2 = 0; i2 < valueAt.size(); i2++) {
                        ConnectionRecord connectionRecord = valueAt.get(i2);
                        this.pw.print("      ");
                        this.pw.print(connectionRecord.binding.intent.intent.getIntent().toShortString(false, false, false, false));
                        this.pw.print(" -> ");
                        ProcessRecord processRecord = connectionRecord.binding.client;
                        this.pw.println(processRecord != null ? processRecord.toShortString() : "null");
                    }
                }
            }
        }

        private void dumpServiceClient(ServiceRecord serviceRecord) {
            IApplicationThread thread;
            TransferPipe transferPipe;
            ProcessRecord processRecord = serviceRecord.app;
            if (processRecord == null || (thread = processRecord.getThread()) == null) {
                return;
            }
            this.pw.println("    Client:");
            this.pw.flush();
            try {
                transferPipe = new TransferPipe();
            } catch (RemoteException unused) {
                this.pw.println("      Got a RemoteException while dumping the service");
            } catch (IOException e) {
                this.pw.println("      Failure while dumping the service: " + e);
            }
            try {
                thread.dumpService(transferPipe.getWriteFd(), serviceRecord, this.args);
                transferPipe.setBufferPrefix("      ");
                transferPipe.go(this.fd, 2000L);
                transferPipe.kill();
                this.needSep = true;
            } catch (Throwable th) {
                transferPipe.kill();
                throw th;
            }
        }

        private void dumpUserRemainsLocked(int i) {
            String str;
            String str2;
            ServiceMap serviceMapLocked = ActiveServices.this.getServiceMapLocked(i);
            this.printed = false;
            int size = serviceMapLocked.mDelayedStartList.size();
            for (int i2 = 0; i2 < size; i2++) {
                ServiceRecord serviceRecord = serviceMapLocked.mDelayedStartList.get(i2);
                if (this.matcher.match(serviceRecord, serviceRecord.name) && ((str2 = this.dumpPackage) == null || str2.equals(serviceRecord.appInfo.packageName))) {
                    if (!this.printed) {
                        if (this.printedAnything) {
                            this.pw.println();
                        }
                        this.pw.println("  User " + i + " delayed start services:");
                        this.printed = true;
                    }
                    this.printedAnything = true;
                    this.pw.print("  * Delayed start ");
                    this.pw.println(serviceRecord);
                }
            }
            this.printed = false;
            int size2 = serviceMapLocked.mStartingBackground.size();
            for (int i3 = 0; i3 < size2; i3++) {
                ServiceRecord serviceRecord2 = serviceMapLocked.mStartingBackground.get(i3);
                if (this.matcher.match(serviceRecord2, serviceRecord2.name) && ((str = this.dumpPackage) == null || str.equals(serviceRecord2.appInfo.packageName))) {
                    if (!this.printed) {
                        if (this.printedAnything) {
                            this.pw.println();
                        }
                        this.pw.println("  User " + i + " starting in background:");
                        this.printed = true;
                    }
                    this.printedAnything = true;
                    this.pw.print("  * Starting bg ");
                    this.pw.println(serviceRecord2);
                }
            }
        }

        private void dumpRemainsLocked() {
            String str;
            ProcessRecord processRecord;
            String str2;
            String str3;
            String str4;
            boolean z = false;
            if (ActiveServices.this.mPendingServices.size() > 0) {
                this.printed = false;
                for (int i = 0; i < ActiveServices.this.mPendingServices.size(); i++) {
                    ServiceRecord serviceRecord = ActiveServices.this.mPendingServices.get(i);
                    if (this.matcher.match(serviceRecord, serviceRecord.name) && ((str4 = this.dumpPackage) == null || str4.equals(serviceRecord.appInfo.packageName))) {
                        this.printedAnything = true;
                        if (!this.printed) {
                            if (this.needSep) {
                                this.pw.println();
                            }
                            this.needSep = true;
                            this.pw.println("  Pending services:");
                            this.printed = true;
                        }
                        this.pw.print("  * Pending ");
                        this.pw.println(serviceRecord);
                        serviceRecord.dump(this.pw, "    ");
                    }
                }
                this.needSep = true;
            }
            if (ActiveServices.this.mRestartingServices.size() > 0) {
                this.printed = false;
                for (int i2 = 0; i2 < ActiveServices.this.mRestartingServices.size(); i2++) {
                    ServiceRecord serviceRecord2 = ActiveServices.this.mRestartingServices.get(i2);
                    if (this.matcher.match(serviceRecord2, serviceRecord2.name) && ((str3 = this.dumpPackage) == null || str3.equals(serviceRecord2.appInfo.packageName))) {
                        this.printedAnything = true;
                        if (!this.printed) {
                            if (this.needSep) {
                                this.pw.println();
                            }
                            this.needSep = true;
                            this.pw.println("  Restarting services:");
                            this.printed = true;
                        }
                        this.pw.print("  * Restarting ");
                        this.pw.println(serviceRecord2);
                        serviceRecord2.dump(this.pw, "    ");
                    }
                }
                this.needSep = true;
            }
            if (ActiveServices.this.mDestroyingServices.size() > 0) {
                this.printed = false;
                for (int i3 = 0; i3 < ActiveServices.this.mDestroyingServices.size(); i3++) {
                    ServiceRecord serviceRecord3 = ActiveServices.this.mDestroyingServices.get(i3);
                    if (this.matcher.match(serviceRecord3, serviceRecord3.name) && ((str2 = this.dumpPackage) == null || str2.equals(serviceRecord3.appInfo.packageName))) {
                        this.printedAnything = true;
                        if (!this.printed) {
                            if (this.needSep) {
                                this.pw.println();
                            }
                            this.needSep = true;
                            this.pw.println("  Destroying services:");
                            this.printed = true;
                        }
                        this.pw.print("  * Destroy ");
                        this.pw.println(serviceRecord3);
                        serviceRecord3.dump(this.pw, "    ");
                    }
                }
                this.needSep = true;
            }
            if (this.dumpAll) {
                this.printed = false;
                for (int i4 = 0; i4 < ActiveServices.this.mServiceConnections.size(); i4++) {
                    ArrayList<ConnectionRecord> valueAt = ActiveServices.this.mServiceConnections.valueAt(i4);
                    for (int i5 = 0; i5 < valueAt.size(); i5++) {
                        ConnectionRecord connectionRecord = valueAt.get(i5);
                        ActivityManagerService.ItemMatcher itemMatcher = this.matcher;
                        ServiceRecord serviceRecord4 = connectionRecord.binding.service;
                        if (itemMatcher.match(serviceRecord4, serviceRecord4.name) && ((str = this.dumpPackage) == null || ((processRecord = connectionRecord.binding.client) != null && str.equals(processRecord.info.packageName)))) {
                            this.printedAnything = true;
                            if (!this.printed) {
                                if (this.needSep) {
                                    this.pw.println();
                                }
                                this.needSep = true;
                                this.pw.println("  Connection bindings to services:");
                                this.printed = true;
                            }
                            this.pw.print("  * ");
                            this.pw.println(connectionRecord);
                            connectionRecord.dump(this.pw, "    ");
                        }
                    }
                }
            }
            if (this.matcher.all) {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                int[] users = ActiveServices.this.mAm.mUserController.getUsers();
                int length = users.length;
                int i6 = 0;
                while (i6 < length) {
                    int i7 = users[i6];
                    ServiceMap serviceMap = ActiveServices.this.mServiceMap.get(i7);
                    if (serviceMap != null) {
                        boolean z2 = z;
                        for (int size = serviceMap.mActiveForegroundApps.size() - 1; size >= 0; size--) {
                            ActiveForegroundApp valueAt2 = serviceMap.mActiveForegroundApps.valueAt(size);
                            String str5 = this.dumpPackage;
                            if (str5 == null || str5.equals(valueAt2.mPackageName)) {
                                if (!z2) {
                                    this.printedAnything = true;
                                    if (this.needSep) {
                                        this.pw.println();
                                    }
                                    this.needSep = true;
                                    this.pw.print("Active foreground apps - user ");
                                    this.pw.print(i7);
                                    this.pw.println(":");
                                    z2 = true;
                                }
                                this.pw.print("  #");
                                this.pw.print(size);
                                this.pw.print(": ");
                                this.pw.println(valueAt2.mPackageName);
                                if (valueAt2.mLabel != null) {
                                    this.pw.print("    mLabel=");
                                    this.pw.println(valueAt2.mLabel);
                                }
                                this.pw.print("    mNumActive=");
                                this.pw.print(valueAt2.mNumActive);
                                this.pw.print(" mAppOnTop=");
                                this.pw.print(valueAt2.mAppOnTop);
                                this.pw.print(" mShownWhileTop=");
                                this.pw.print(valueAt2.mShownWhileTop);
                                this.pw.print(" mShownWhileScreenOn=");
                                this.pw.println(valueAt2.mShownWhileScreenOn);
                                this.pw.print("    mStartTime=");
                                boolean z3 = z2;
                                TimeUtils.formatDuration(valueAt2.mStartTime - elapsedRealtime, this.pw);
                                this.pw.print(" mStartVisibleTime=");
                                TimeUtils.formatDuration(valueAt2.mStartVisibleTime - elapsedRealtime, this.pw);
                                this.pw.println();
                                if (valueAt2.mEndTime != 0) {
                                    this.pw.print("    mEndTime=");
                                    TimeUtils.formatDuration(valueAt2.mEndTime - elapsedRealtime, this.pw);
                                    this.pw.println();
                                }
                                z2 = z3;
                            }
                        }
                        if (serviceMap.hasMessagesOrCallbacks()) {
                            if (this.needSep) {
                                this.pw.println();
                            }
                            this.printedAnything = true;
                            this.needSep = true;
                            this.pw.print("  Handler - user ");
                            this.pw.print(i7);
                            this.pw.println(":");
                            serviceMap.dumpMine(new PrintWriterPrinter(this.pw), "    ");
                        }
                    }
                    i6++;
                    z = false;
                }
            }
            if (this.printedAnything) {
                return;
            }
            this.pw.println("  (nothing)");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ServiceDumper newServiceDumperLocked(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, int i, boolean z, String str) {
        return new ServiceDumper(fileDescriptor, printWriter, strArr, i, z, str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        ActivityManagerService activityManagerService = this.mAm;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                long start = protoOutputStream.start(j);
                for (int i : this.mAm.mUserController.getUsers()) {
                    ServiceMap serviceMap = this.mServiceMap.get(i);
                    if (serviceMap != null) {
                        long start2 = protoOutputStream.start(2246267895809L);
                        protoOutputStream.write(1120986464257L, i);
                        ArrayMap<ComponentName, ServiceRecord> arrayMap = serviceMap.mServicesByInstanceName;
                        for (int i2 = 0; i2 < arrayMap.size(); i2++) {
                            arrayMap.valueAt(i2).dumpDebug(protoOutputStream, 2246267895810L);
                        }
                        protoOutputStream.end(start2);
                    }
                }
                protoOutputStream.end(start);
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean dumpService(FileDescriptor fileDescriptor, PrintWriter printWriter, String str, int[] iArr, String[] strArr, int i, boolean z) {
        int[] users;
        try {
            boolean z2 = false;
            this.mAm.mOomAdjuster.mCachedAppOptimizer.enableFreezer(false);
            ArrayList arrayList = new ArrayList();
            Predicate filterRecord = DumpUtils.filterRecord(str);
            ActivityManagerService activityManagerService = this.mAm;
            ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                if (iArr == null) {
                    try {
                        users = this.mAm.mUserController.getUsers();
                    } catch (Throwable th) {
                        ActivityManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                } else {
                    users = iArr;
                }
                for (int i2 : users) {
                    ServiceMap serviceMap = this.mServiceMap.get(i2);
                    if (serviceMap != null) {
                        ArrayMap<ComponentName, ServiceRecord> arrayMap = serviceMap.mServicesByInstanceName;
                        for (int i3 = 0; i3 < arrayMap.size(); i3++) {
                            ServiceRecord valueAt = arrayMap.valueAt(i3);
                            if (filterRecord.test(valueAt)) {
                                arrayList.add(valueAt);
                            }
                        }
                    }
                }
            }
            ActivityManagerService.resetPriorityAfterLockedSection();
            if (arrayList.size() <= 0) {
                return false;
            }
            arrayList.sort(Comparator.comparing(new Function() { // from class: com.android.server.am.ActiveServices$$ExternalSyntheticLambda4
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return ((ServiceRecord) obj).getComponentName();
                }
            }));
            int i4 = 0;
            while (i4 < arrayList.size()) {
                if (z2) {
                    printWriter.println();
                }
                dumpService("", fileDescriptor, printWriter, (ServiceRecord) arrayList.get(i4), strArr, z);
                i4++;
                z2 = true;
            }
            return true;
        } finally {
            this.mAm.mOomAdjuster.mCachedAppOptimizer.enableFreezer(true);
        }
    }

    private void dumpService(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, ServiceRecord serviceRecord, String[] strArr, boolean z) {
        IApplicationThread thread;
        String str2 = str + "  ";
        ActivityManagerService activityManagerService = this.mAm;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                printWriter.print(str);
                printWriter.print("SERVICE ");
                printWriter.print(serviceRecord.shortInstanceName);
                printWriter.print(" ");
                printWriter.print(Integer.toHexString(System.identityHashCode(serviceRecord)));
                printWriter.print(" pid=");
                ProcessRecord processRecord = serviceRecord.app;
                if (processRecord != null) {
                    printWriter.print(processRecord.getPid());
                    printWriter.print(" user=");
                    printWriter.println(serviceRecord.userId);
                } else {
                    printWriter.println("(not running)");
                }
                if (z) {
                    serviceRecord.dump(printWriter, str2);
                }
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
        ProcessRecord processRecord2 = serviceRecord.app;
        if (processRecord2 == null || (thread = processRecord2.getThread()) == null) {
            return;
        }
        printWriter.print(str);
        printWriter.println("  Client:");
        printWriter.flush();
        try {
            TransferPipe transferPipe = new TransferPipe();
            try {
                thread.dumpService(transferPipe.getWriteFd(), serviceRecord, strArr);
                transferPipe.setBufferPrefix(str + "    ");
                transferPipe.go(fileDescriptor);
                transferPipe.kill();
            } catch (Throwable th2) {
                transferPipe.kill();
                throw th2;
            }
        } catch (RemoteException unused) {
            printWriter.println(str + "    Got a RemoteException while dumping the service");
        } catch (IOException e) {
            printWriter.println(str + "    Failure while dumping the service: " + e);
        }
    }

    private void setFgsRestrictionLocked(String str, int i, int i2, Intent intent, ServiceRecord serviceRecord, int i3, BackgroundStartPrivileges backgroundStartPrivileges, boolean z, boolean z2) {
        if (!this.mAm.mConstants.mFlagBackgroundFgsStartRestrictionEnabled) {
            if (!serviceRecord.mAllowWhileInUsePermissionInFgs) {
                serviceRecord.mAllowWhileInUsePermissionInFgsReason = 1;
            }
            serviceRecord.mAllowWhileInUsePermissionInFgs = true;
        }
        if (!serviceRecord.mAllowWhileInUsePermissionInFgs || serviceRecord.mAllowStartForeground == -1) {
            int shouldAllowFgsWhileInUsePermissionLocked = shouldAllowFgsWhileInUsePermissionLocked(str, i, i2, serviceRecord.app, backgroundStartPrivileges);
            if (!serviceRecord.mAllowWhileInUsePermissionInFgs) {
                serviceRecord.mAllowWhileInUsePermissionInFgs = shouldAllowFgsWhileInUsePermissionLocked != -1;
                serviceRecord.mAllowWhileInUsePermissionInFgsReason = shouldAllowFgsWhileInUsePermissionLocked;
            }
            if (serviceRecord.mAllowStartForeground == -1) {
                serviceRecord.mAllowStartForeground = shouldAllowFgsStartForegroundWithBindingCheckLocked(shouldAllowFgsWhileInUsePermissionLocked, str, i, i2, intent, serviceRecord, backgroundStartPrivileges, z);
            }
        }
    }

    void resetFgsRestrictionLocked(ServiceRecord serviceRecord) {
        serviceRecord.mAllowWhileInUsePermissionInFgs = false;
        serviceRecord.mAllowWhileInUsePermissionInFgsReason = -1;
        serviceRecord.mAllowStartForeground = -1;
        serviceRecord.mInfoAllowStartForeground = null;
        serviceRecord.mInfoTempFgsAllowListReason = null;
        serviceRecord.mLoggedInfoAllowStartForeground = false;
        serviceRecord.updateAllowUiJobScheduling(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canStartForegroundServiceLocked(int i, int i2, String str) {
        if (!this.mAm.mConstants.mFlagBackgroundFgsStartRestrictionEnabled) {
            return true;
        }
        int shouldAllowFgsStartForegroundNoBindingCheckLocked = shouldAllowFgsStartForegroundNoBindingCheckLocked(shouldAllowFgsWhileInUsePermissionLocked(str, i, i2, null, BackgroundStartPrivileges.NONE), i, i2, str, null, BackgroundStartPrivileges.NONE);
        if (shouldAllowFgsStartForegroundNoBindingCheckLocked == -1 && canBindingClientStartFgsLocked(i2) != null) {
            shouldAllowFgsStartForegroundNoBindingCheckLocked = 54;
        }
        return shouldAllowFgsStartForegroundNoBindingCheckLocked != -1;
    }

    private int shouldAllowFgsWhileInUsePermissionLocked(String str, int i, final int i2, ProcessRecord processRecord, BackgroundStartPrivileges backgroundStartPrivileges) {
        ActiveInstrumentation activeInstrumentation;
        Integer num;
        int uidStateLocked = this.mAm.getUidStateLocked(i2);
        int reasonCodeFromProcState = uidStateLocked <= 2 ? PowerExemptionManager.getReasonCodeFromProcState(uidStateLocked) : -1;
        if (reasonCodeFromProcState == -1 && this.mAm.mAtmInternal.isUidForeground(i2)) {
            reasonCodeFromProcState = 50;
        }
        if (reasonCodeFromProcState == -1 && backgroundStartPrivileges.allowsBackgroundActivityStarts()) {
            reasonCodeFromProcState = 53;
        }
        if (reasonCodeFromProcState == -1) {
            int appId = UserHandle.getAppId(i2);
            if (appId == 0 || appId == 1000 || appId == 1027 || appId == 2000) {
                reasonCodeFromProcState = 51;
            }
        }
        if (reasonCodeFromProcState == -1 && (num = (Integer) this.mAm.mProcessList.searchEachLruProcessesLOSP(false, new Function() { // from class: com.android.server.am.ActiveServices$$ExternalSyntheticLambda9
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Integer lambda$shouldAllowFgsWhileInUsePermissionLocked$4;
                lambda$shouldAllowFgsWhileInUsePermissionLocked$4 = ActiveServices.lambda$shouldAllowFgsWhileInUsePermissionLocked$4(i2, (ProcessRecord) obj);
                return lambda$shouldAllowFgsWhileInUsePermissionLocked$4;
            }
        })) != null) {
            reasonCodeFromProcState = num.intValue();
        }
        if (reasonCodeFromProcState == -1 && this.mAm.mInternal.isTempAllowlistedForFgsWhileInUse(i2)) {
            return 70;
        }
        if (reasonCodeFromProcState == -1 && processRecord != null && (activeInstrumentation = processRecord.getActiveInstrumentation()) != null && activeInstrumentation.mHasBackgroundActivityStartsPermission) {
            reasonCodeFromProcState = 60;
        }
        if (reasonCodeFromProcState == -1 && this.mAm.checkPermission("android.permission.START_ACTIVITIES_FROM_BACKGROUND", i, i2) == 0) {
            reasonCodeFromProcState = 58;
        }
        if (reasonCodeFromProcState == -1) {
            if (verifyPackage(str, i2)) {
                if (this.mAllowListWhileInUsePermissionInFgs.contains(str)) {
                    reasonCodeFromProcState = 65;
                }
            } else {
                EventLog.writeEvent(1397638484, "215003903", Integer.valueOf(i2), "callingPackage:" + str + " does not belong to callingUid:" + i2);
            }
        }
        if (reasonCodeFromProcState == -1 && this.mAm.mInternal.isDeviceOwner(i2)) {
            return 55;
        }
        return reasonCodeFromProcState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$shouldAllowFgsWhileInUsePermissionLocked$4(int i, ProcessRecord processRecord) {
        return (processRecord.uid == i && processRecord.getWindowProcessController().areBackgroundFgsStartsAllowed()) ? 52 : null;
    }

    private int shouldAllowFgsWhileInUsePermissionByBindingsLocked(final int i) {
        final ArraySet arraySet = new ArraySet();
        Integer num = (Integer) this.mAm.mProcessList.searchEachLruProcessesLOSP(false, new Function() { // from class: com.android.server.am.ActiveServices$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Integer lambda$shouldAllowFgsWhileInUsePermissionByBindingsLocked$5;
                lambda$shouldAllowFgsWhileInUsePermissionByBindingsLocked$5 = ActiveServices.this.lambda$shouldAllowFgsWhileInUsePermissionByBindingsLocked$5(i, arraySet, (ProcessRecord) obj);
                return lambda$shouldAllowFgsWhileInUsePermissionByBindingsLocked$5;
            }
        });
        if (num == null) {
            return -1;
        }
        return num.intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Integer lambda$shouldAllowFgsWhileInUsePermissionByBindingsLocked$5(int i, ArraySet arraySet, ProcessRecord processRecord) {
        if (processRecord.uid != i) {
            return null;
        }
        ProcessServiceRecord processServiceRecord = processRecord.mServices;
        int size = processServiceRecord.mServices.size();
        for (int i2 = 0; i2 < size; i2++) {
            ArrayMap<IBinder, ArrayList<ConnectionRecord>> connections = processServiceRecord.mServices.valueAt(i2).getConnections();
            int size2 = connections.size();
            for (int i3 = 0; i3 < size2; i3++) {
                ArrayList<ConnectionRecord> valueAt = connections.valueAt(i3);
                for (int i4 = 0; i4 < valueAt.size(); i4++) {
                    ConnectionRecord connectionRecord = valueAt.get(i4);
                    int i5 = connectionRecord.binding.client.uid;
                    if (i5 != i && !arraySet.contains(Integer.valueOf(i5))) {
                        int uidStateLocked = this.mAm.getUidStateLocked(i);
                        boolean z = uidStateLocked == 2;
                        boolean z2 = uidStateLocked < 2 && connectionRecord.hasFlag(AudioDevice.OUT_FM);
                        if (z || z2) {
                            return Integer.valueOf(PowerExemptionManager.getReasonCodeFromProcState(uidStateLocked));
                        }
                        arraySet.add(Integer.valueOf(i5));
                    }
                }
            }
        }
        return null;
    }

    private String canBindingClientStartFgsLocked(final int i) {
        final ArraySet arraySet = new ArraySet();
        Pair pair = (Pair) this.mAm.mProcessList.searchEachLruProcessesLOSP(false, new Function() { // from class: com.android.server.am.ActiveServices$$ExternalSyntheticLambda6
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Pair lambda$canBindingClientStartFgsLocked$6;
                lambda$canBindingClientStartFgsLocked$6 = ActiveServices.this.lambda$canBindingClientStartFgsLocked$6(i, arraySet, (ProcessRecord) obj);
                return lambda$canBindingClientStartFgsLocked$6;
            }
        });
        if (pair != null) {
            return (String) pair.second;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Pair lambda$canBindingClientStartFgsLocked$6(int i, ArraySet arraySet, ProcessRecord processRecord) {
        if (processRecord.uid != i) {
            return null;
        }
        ProcessServiceRecord processServiceRecord = processRecord.mServices;
        int size = processServiceRecord.mServices.size();
        for (int i2 = 0; i2 < size; i2++) {
            ArrayMap<IBinder, ArrayList<ConnectionRecord>> connections = processServiceRecord.mServices.valueAt(i2).getConnections();
            int size2 = connections.size();
            for (int i3 = 0; i3 < size2; i3++) {
                ArrayList<ConnectionRecord> valueAt = connections.valueAt(i3);
                for (int i4 = 0; i4 < valueAt.size(); i4++) {
                    ConnectionRecord connectionRecord = valueAt.get(i4);
                    ProcessRecord processRecord2 = connectionRecord.binding.client;
                    if (!processRecord2.isPersistent()) {
                        int i5 = processRecord2.mPid;
                        int i6 = processRecord2.uid;
                        if (i6 != i && !arraySet.contains(Integer.valueOf(i6))) {
                            String str = connectionRecord.clientPackageName;
                            int shouldAllowFgsStartForegroundNoBindingCheckLocked = shouldAllowFgsStartForegroundNoBindingCheckLocked(shouldAllowFgsWhileInUsePermissionLocked(str, i5, i6, null, BackgroundStartPrivileges.NONE), i5, i6, str, null, BackgroundStartPrivileges.NONE);
                            if (shouldAllowFgsStartForegroundNoBindingCheckLocked != -1) {
                                return new Pair(Integer.valueOf(shouldAllowFgsStartForegroundNoBindingCheckLocked), str);
                            }
                            arraySet.add(Integer.valueOf(i6));
                        }
                    }
                }
            }
        }
        return null;
    }

    private int shouldAllowFgsStartForegroundWithBindingCheckLocked(int i, String str, int i2, int i3, Intent intent, ServiceRecord serviceRecord, BackgroundStartPrivileges backgroundStartPrivileges, boolean z) {
        String str2;
        ActivityManagerService.FgsTempAllowListItem isAllowlistedForFgsStartLOSP = this.mAm.isAllowlistedForFgsStartLOSP(i3);
        serviceRecord.mInfoTempFgsAllowListReason = isAllowlistedForFgsStartLOSP;
        int shouldAllowFgsStartForegroundNoBindingCheckLocked = shouldAllowFgsStartForegroundNoBindingCheckLocked(i, i2, i3, str, serviceRecord, backgroundStartPrivileges);
        String str3 = null;
        int i4 = -1;
        if (shouldAllowFgsStartForegroundNoBindingCheckLocked == -1) {
            str2 = canBindingClientStartFgsLocked(i3);
            if (str2 != null) {
                shouldAllowFgsStartForegroundNoBindingCheckLocked = 54;
            }
        } else {
            str2 = null;
        }
        int uidStateLocked = this.mAm.getUidStateLocked(i3);
        try {
            i4 = this.mAm.mContext.getPackageManager().getTargetSdkVersion(str);
        } catch (PackageManager.NameNotFoundException unused) {
        }
        boolean z2 = (this.mAm.getUidProcessCapabilityLocked(i3) & 16) != 0;
        StringBuilder sb = new StringBuilder();
        sb.append("[callingPackage: ");
        sb.append(str);
        sb.append("; callingUid: ");
        sb.append(i3);
        sb.append("; uidState: ");
        sb.append(ProcessList.makeProcStateString(uidStateLocked));
        sb.append("; uidBFSL: ");
        sb.append(z2 ? "[BFSL]" : "n/a");
        sb.append("; intent: ");
        sb.append(intent);
        sb.append("; code:");
        sb.append(PowerExemptionManager.reasonCodeToString(shouldAllowFgsStartForegroundNoBindingCheckLocked));
        sb.append("; tempAllowListReason:<");
        if (isAllowlistedForFgsStartLOSP != null) {
            str3 = isAllowlistedForFgsStartLOSP.mReason + ",reasonCode:" + PowerExemptionManager.reasonCodeToString(isAllowlistedForFgsStartLOSP.mReasonCode) + ",duration:" + isAllowlistedForFgsStartLOSP.mDuration + ",callingUid:" + isAllowlistedForFgsStartLOSP.mCallingUid;
        }
        sb.append(str3);
        sb.append(">; targetSdkVersion:");
        sb.append(serviceRecord.appInfo.targetSdkVersion);
        sb.append("; callerTargetSdkVersion:");
        sb.append(i4);
        sb.append("; startForegroundCount:");
        sb.append(serviceRecord.mStartForegroundCount);
        sb.append("; bindFromPackage:");
        sb.append(str2);
        sb.append(": isBindService:");
        sb.append(z);
        sb.append("]");
        String sb2 = sb.toString();
        if (!sb2.equals(serviceRecord.mInfoAllowStartForeground)) {
            serviceRecord.mLoggedInfoAllowStartForeground = false;
            serviceRecord.mInfoAllowStartForeground = sb2;
        }
        return shouldAllowFgsStartForegroundNoBindingCheckLocked;
    }

    private int shouldAllowFgsStartForegroundNoBindingCheckLocked(int i, int i2, final int i3, String str, ServiceRecord serviceRecord, BackgroundStartPrivileges backgroundStartPrivileges) {
        String stringForUser;
        ComponentName unflattenFromString;
        ActivityManagerService.FgsTempAllowListItem isAllowlistedForFgsStartLOSP;
        int uidStateLocked;
        if (i == -1 && (uidStateLocked = this.mAm.getUidStateLocked(i3)) <= 2) {
            i = PowerExemptionManager.getReasonCodeFromProcState(uidStateLocked);
        }
        if (i == -1) {
            final boolean z = (this.mAm.getUidProcessCapabilityLocked(i3) & 16) != 0;
            Integer num = (Integer) this.mAm.mProcessList.searchEachLruProcessesLOSP(false, new Function() { // from class: com.android.server.am.ActiveServices$$ExternalSyntheticLambda5
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    Integer lambda$shouldAllowFgsStartForegroundNoBindingCheckLocked$7;
                    lambda$shouldAllowFgsStartForegroundNoBindingCheckLocked$7 = ActiveServices.this.lambda$shouldAllowFgsStartForegroundNoBindingCheckLocked$7(i3, z, (ProcessRecord) obj);
                    return lambda$shouldAllowFgsStartForegroundNoBindingCheckLocked$7;
                }
            });
            if (num != null) {
                i = num.intValue();
            }
        }
        if (i == -1 && this.mAm.checkPermission("android.permission.START_FOREGROUND_SERVICES_FROM_BACKGROUND", i2, i3) == 0) {
            i = 59;
        }
        if (i == -1 && backgroundStartPrivileges.allowsBackgroundFgsStarts()) {
            i = 53;
        }
        if (i == -1 && this.mAm.mAtmInternal.hasSystemAlertWindowPermission(i3, i2, str)) {
            i = 62;
        }
        if (i == -1 && this.mAm.mInternal.isAssociatedCompanionApp(UserHandle.getUserId(i3), i3) && (isPermissionGranted("android.permission.REQUEST_COMPANION_START_FOREGROUND_SERVICES_FROM_BACKGROUND", i2, i3) || isPermissionGranted("android.permission.REQUEST_COMPANION_RUN_IN_BACKGROUND", i2, i3))) {
            i = 57;
        }
        if (i == -1 && (isAllowlistedForFgsStartLOSP = this.mAm.isAllowlistedForFgsStartLOSP(i3)) != null) {
            i = isAllowlistedForFgsStartLOSP == ActivityManagerService.FAKE_TEMP_ALLOW_LIST_ITEM ? 300 : isAllowlistedForFgsStartLOSP.mReasonCode;
        }
        if (i == -1 && UserManager.isDeviceInDemoMode(this.mAm.mContext)) {
            i = 63;
        }
        if (i == -1 && this.mAm.mInternal.isProfileOwner(i3)) {
            i = 56;
        }
        if (i == -1) {
            AppOpsManager appOpsManager = this.mAm.getAppOpsManager();
            if (this.mAm.mConstants.mFlagSystemExemptPowerRestrictionsEnabled && appOpsManager.checkOpNoThrow(128, i3, str) == 0) {
                i = FrameworkStatsLog.TIF_TUNE_CHANGED;
            }
        }
        if (i == -1) {
            AppOpsManager appOpsManager2 = this.mAm.getAppOpsManager();
            if (appOpsManager2.checkOpNoThrow(47, i3, str) == 0) {
                i = 68;
            } else if (appOpsManager2.checkOpNoThrow(94, i3, str) == 0) {
                i = 69;
            }
        }
        if (i == -1 && (stringForUser = Settings.Secure.getStringForUser(this.mAm.mContext.getContentResolver(), "default_input_method", UserHandle.getUserId(i3))) != null && (unflattenFromString = ComponentName.unflattenFromString(stringForUser)) != null && unflattenFromString.getPackageName().equals(str)) {
            i = 71;
        }
        if (i == -1 && this.mAm.mConstants.mFgsAllowOptOut && serviceRecord != null && serviceRecord.appInfo.hasRequestForegroundServiceExemption()) {
            return 1000;
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Integer lambda$shouldAllowFgsStartForegroundNoBindingCheckLocked$7(int i, boolean z, ProcessRecord processRecord) {
        if (processRecord.uid != i) {
            return null;
        }
        int curProcState = processRecord.mState.getCurProcState();
        if (curProcState <= 3 || (z && curProcState <= 5)) {
            return Integer.valueOf(PowerExemptionManager.getReasonCodeFromProcState(curProcState));
        }
        ActiveInstrumentation activeInstrumentation = processRecord.getActiveInstrumentation();
        if (activeInstrumentation != null && activeInstrumentation.mHasBackgroundForegroundServiceStartsPermission) {
            return 61;
        }
        long lastInvisibleTime = processRecord.mState.getLastInvisibleTime();
        return (lastInvisibleTime <= 0 || lastInvisibleTime >= Long.MAX_VALUE || SystemClock.elapsedRealtime() - lastInvisibleTime >= this.mAm.mConstants.mFgToBgFgsGraceDuration) ? null : 67;
    }

    private boolean isPermissionGranted(String str, int i, int i2) {
        return this.mAm.checkPermission(str, i, i2) == 0;
    }

    private void showFgsBgRestrictedNotificationLocked(ServiceRecord serviceRecord) {
        if (this.mAm.mConstants.mFgsStartRestrictionNotificationEnabled) {
            Context context = this.mAm.mContext;
            String str = "App restricted: " + serviceRecord.mRecentCallingPackage;
            long currentTimeMillis = System.currentTimeMillis();
            ((NotificationManager) context.getSystemService(NotificationManager.class)).notifyAsUser(Long.toString(currentTimeMillis), 61, new Notification.Builder(context, SystemNotificationChannels.ALERTS).setGroup("com.android.fgs-bg-restricted").setSmallIcon(R.drawable.textfield_disabled_focused_holo_dark).setWhen(0L).setColor(context.getColor(R.color.system_notification_accent_color)).setTicker("Foreground Service BG-Launch Restricted").setContentTitle("Foreground Service BG-Launch Restricted").setContentText(str).setStyle(new Notification.BigTextStyle().bigText(DATE_FORMATTER.format(Long.valueOf(currentTimeMillis)) + " " + serviceRecord.mInfoAllowStartForeground)).build(), UserHandle.ALL);
        }
    }

    private boolean isBgFgsRestrictionEnabled(ServiceRecord serviceRecord) {
        return this.mAm.mConstants.mFlagFgsStartRestrictionEnabled && CompatChanges.isChangeEnabled(FGS_BG_START_RESTRICTION_CHANGE_ID, serviceRecord.appInfo.uid) && (!this.mAm.mConstants.mFgsStartRestrictionCheckCallerTargetSdk || CompatChanges.isChangeEnabled(FGS_BG_START_RESTRICTION_CHANGE_ID, serviceRecord.mRecentCallingUid));
    }

    private void logFgsBackgroundStart(ServiceRecord serviceRecord) {
        if (serviceRecord.mLoggedInfoAllowStartForeground) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Background started FGS: ");
        sb.append(serviceRecord.mAllowStartForeground != -1 ? "Allowed " : "Disallowed ");
        sb.append(serviceRecord.mInfoAllowStartForeground);
        sb.append(serviceRecord.isShortFgs() ? " (Called on SHORT_SERVICE)" : "");
        String sb2 = sb.toString();
        if (serviceRecord.mAllowStartForeground != -1) {
            if (this.mActiveServicesExt.logFgsBackgroundStart() && ActivityManagerUtils.shouldSamplePackageForAtom(serviceRecord.packageName, this.mAm.mConstants.mFgsStartAllowedLogSampleRate)) {
                Slog.wtfQuiet("ActivityManager", sb2);
            }
            Slog.i("ActivityManager", sb2);
        } else {
            if (this.mActiveServicesExt.logFgsBackgroundStart()) {
                Slog.wtfQuiet("ActivityManager", sb2);
            }
            Slog.w("ActivityManager", sb2);
        }
        serviceRecord.mLoggedInfoAllowStartForeground = true;
    }

    private void logFGSStateChangeLocked(ServiceRecord serviceRecord, int i, int i2, int i3, int i4) {
        boolean z;
        int i5;
        char c;
        char c2;
        int i6;
        if (ActivityManagerUtils.shouldSamplePackageForAtom(serviceRecord.packageName, this.mAm.mConstants.mFgsAtomSampleRate)) {
            if (i == 1 || i == 2 || i == 5) {
                z = serviceRecord.mAllowWhileInUsePermissionInFgsAtEntering;
                i5 = serviceRecord.mAllowStartForegroundAtEntering;
            } else {
                z = serviceRecord.mAllowWhileInUsePermissionInFgs;
                i5 = serviceRecord.mAllowStartForeground;
            }
            boolean z2 = z;
            int i7 = i5;
            ApplicationInfo applicationInfo = serviceRecord.mRecentCallerApplicationInfo;
            int i8 = applicationInfo != null ? applicationInfo.targetSdkVersion : 0;
            ApplicationInfo applicationInfo2 = serviceRecord.appInfo;
            int i9 = applicationInfo2.uid;
            String str = serviceRecord.shortInstanceName;
            int i10 = applicationInfo2.targetSdkVersion;
            int i11 = serviceRecord.mRecentCallingUid;
            ActivityManagerService.FgsTempAllowListItem fgsTempAllowListItem = serviceRecord.mInfoTempFgsAllowListReason;
            int i12 = fgsTempAllowListItem != null ? fgsTempAllowListItem.mCallingUid : -1;
            boolean z3 = serviceRecord.mFgsNotificationWasDeferred;
            boolean z4 = serviceRecord.mFgsNotificationShown;
            int i13 = serviceRecord.mStartForegroundCount;
            int hashComponentNameForAtom = ActivityManagerUtils.hashComponentNameForAtom(str);
            boolean z5 = serviceRecord.mFgsHasNotificationPermission;
            int i14 = serviceRecord.foregroundServiceType;
            boolean z6 = serviceRecord.mIsFgsDelegate;
            ForegroundServiceDelegation foregroundServiceDelegation = serviceRecord.mFgsDelegation;
            FrameworkStatsLog.write(60, i9, str, i, z2, i7, i10, i11, i8, i12, z3, z4, i2, i13, hashComponentNameForAtom, z5, i14, i4, z6, foregroundServiceDelegation != null ? foregroundServiceDelegation.mOptions.mClientUid : -1, foregroundServiceDelegation != null ? foregroundServiceDelegation.mOptions.mDelegationService : 0, 0, null, null, this.mAm.getUidStateLocked(serviceRecord.appInfo.uid), this.mAm.getUidProcessCapabilityLocked(serviceRecord.appInfo.uid), this.mAm.getUidStateLocked(serviceRecord.mRecentCallingUid), this.mAm.getUidProcessCapabilityLocked(serviceRecord.mRecentCallingUid), 0L, 0L);
            if (i == 1) {
                i6 = EventLogTags.AM_FOREGROUND_SERVICE_START;
                c = 2;
            } else {
                c = 2;
                if (i == 2) {
                    i6 = EventLogTags.AM_FOREGROUND_SERVICE_STOP;
                } else {
                    if (i != 3) {
                        c2 = 5;
                        if (i == 5) {
                            i6 = EventLogTags.AM_FOREGROUND_SERVICE_TIMED_OUT;
                            Object[] objArr = new Object[12];
                            objArr[0] = Integer.valueOf(serviceRecord.userId);
                            objArr[1] = serviceRecord.shortInstanceName;
                            objArr[c] = Integer.valueOf(z2 ? 1 : 0);
                            objArr[3] = PowerExemptionManager.reasonCodeToString(i7);
                            objArr[4] = Integer.valueOf(serviceRecord.appInfo.targetSdkVersion);
                            objArr[c2] = Integer.valueOf(i8);
                            objArr[6] = Integer.valueOf(serviceRecord.mFgsNotificationWasDeferred ? 1 : 0);
                            objArr[7] = Integer.valueOf(serviceRecord.mFgsNotificationShown ? 1 : 0);
                            objArr[8] = Integer.valueOf(i2);
                            objArr[9] = Integer.valueOf(serviceRecord.mStartForegroundCount);
                            objArr[10] = fgsStopReasonToString(i3);
                            objArr[11] = Integer.valueOf(serviceRecord.foregroundServiceType);
                            EventLog.writeEvent(i6, objArr);
                        }
                        return;
                    }
                    i6 = EventLogTags.AM_FOREGROUND_SERVICE_DENIED;
                }
            }
            c2 = 5;
            Object[] objArr2 = new Object[12];
            objArr2[0] = Integer.valueOf(serviceRecord.userId);
            objArr2[1] = serviceRecord.shortInstanceName;
            objArr2[c] = Integer.valueOf(z2 ? 1 : 0);
            objArr2[3] = PowerExemptionManager.reasonCodeToString(i7);
            objArr2[4] = Integer.valueOf(serviceRecord.appInfo.targetSdkVersion);
            objArr2[c2] = Integer.valueOf(i8);
            objArr2[6] = Integer.valueOf(serviceRecord.mFgsNotificationWasDeferred ? 1 : 0);
            objArr2[7] = Integer.valueOf(serviceRecord.mFgsNotificationShown ? 1 : 0);
            objArr2[8] = Integer.valueOf(i2);
            objArr2[9] = Integer.valueOf(serviceRecord.mStartForegroundCount);
            objArr2[10] = fgsStopReasonToString(i3);
            objArr2[11] = Integer.valueOf(serviceRecord.foregroundServiceType);
            EventLog.writeEvent(i6, objArr2);
        }
    }

    private void updateNumForegroundServicesLocked() {
        sNumForegroundServices.set(this.mAm.mProcessList.getNumForegroundServices());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canAllowWhileInUsePermissionInFgsLocked(int i, int i2, String str) {
        return shouldAllowFgsWhileInUsePermissionLocked(str, i, i2, null, BackgroundStartPrivileges.NONE) != -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canAllowWhileInUsePermissionInFgsLocked(int i, int i2, String str, ProcessRecord processRecord, BackgroundStartPrivileges backgroundStartPrivileges) {
        return shouldAllowFgsWhileInUsePermissionLocked(str, i, i2, processRecord, backgroundStartPrivileges) != -1;
    }

    private boolean verifyPackage(String str, int i) {
        if (i == 0 || i == 1000) {
            return true;
        }
        return this.mAm.getPackageManagerInternal().isSameApp(str, i, UserHandle.getUserId(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean startForegroundServiceDelegateLocked(ForegroundServiceDelegationOptions foregroundServiceDelegationOptions, final ServiceConnection serviceConnection) {
        ProcessRecord processRecord;
        IApplicationThread thread;
        ProcessRecord processRecord2;
        IApplicationThread iApplicationThread;
        ServiceRecord serviceRecord;
        Slog.v("ActivityManager", "startForegroundServiceDelegateLocked " + foregroundServiceDelegationOptions.getDescription());
        final ComponentName componentName = foregroundServiceDelegationOptions.getComponentName();
        for (int size = this.mFgsDelegations.size() - 1; size >= 0; size--) {
            if (this.mFgsDelegations.keyAt(size).mOptions.isSameDelegate(foregroundServiceDelegationOptions)) {
                Slog.e("ActivityManager", "startForegroundServiceDelegate " + foregroundServiceDelegationOptions.getDescription() + " already exists, multiple connections are not allowed");
                return false;
            }
        }
        int i = foregroundServiceDelegationOptions.mClientPid;
        int i2 = foregroundServiceDelegationOptions.mClientUid;
        int userId = UserHandle.getUserId(i2);
        String str = foregroundServiceDelegationOptions.mClientPackageName;
        if (!canStartForegroundServiceLocked(i, i2, str)) {
            Slog.d("ActivityManager", "startForegroundServiceDelegateLocked aborted, app is in the background");
            return false;
        }
        IApplicationThread iApplicationThread2 = foregroundServiceDelegationOptions.mClientAppThread;
        if (iApplicationThread2 != null) {
            iApplicationThread = iApplicationThread2;
            processRecord2 = this.mAm.getRecordForAppLOSP(iApplicationThread2);
        } else {
            synchronized (this.mAm.mPidsSelfLocked) {
                processRecord = this.mAm.mPidsSelfLocked.get(i);
                thread = processRecord.getThread();
            }
            processRecord2 = processRecord;
            iApplicationThread = thread;
        }
        if (processRecord2 == null) {
            throw new SecurityException("Unable to find app for caller " + iApplicationThread + " (pid=" + i + ") when startForegroundServiceDelegateLocked " + componentName);
        }
        Intent intent = new Intent();
        intent.setComponent(componentName);
        ProcessRecord processRecord3 = processRecord2;
        IApplicationThread iApplicationThread3 = iApplicationThread;
        ServiceLookupResult retrieveServiceLocked = retrieveServiceLocked(intent, null, false, -1, null, null, str, i, i2, userId, true, false, false, false, foregroundServiceDelegationOptions, false);
        if (retrieveServiceLocked == null || (serviceRecord = retrieveServiceLocked.record) == null) {
            Slog.d("ActivityManager", "startForegroundServiceDelegateLocked retrieveServiceLocked returns null");
            return false;
        }
        serviceRecord.setProcess(processRecord3, iApplicationThread3, i, null);
        serviceRecord.mIsFgsDelegate = true;
        final ForegroundServiceDelegation foregroundServiceDelegation = new ForegroundServiceDelegation(foregroundServiceDelegationOptions, serviceConnection);
        serviceRecord.mFgsDelegation = foregroundServiceDelegation;
        this.mFgsDelegations.put(foregroundServiceDelegation, serviceRecord);
        serviceRecord.isForeground = true;
        serviceRecord.mFgsEnterTime = SystemClock.uptimeMillis();
        serviceRecord.foregroundServiceType = foregroundServiceDelegationOptions.mForegroundServiceTypes;
        setFgsRestrictionLocked(str, i, i2, intent, serviceRecord, userId, BackgroundStartPrivileges.NONE, false, false);
        ProcessServiceRecord processServiceRecord = processRecord3.mServices;
        processServiceRecord.startService(serviceRecord);
        updateServiceForegroundLocked(processServiceRecord, true);
        synchronized (this.mAm.mProcessStats.mLock) {
            ServiceState tracker = serviceRecord.getTracker();
            if (tracker != null) {
                tracker.setForeground(true, this.mAm.mProcessStats.getMemFactorLocked(), SystemClock.uptimeMillis());
            }
        }
        this.mAm.mBatteryStatsService.noteServiceStartRunning(i2, str, componentName.getClassName());
        AppOpsService appOpsService = this.mAm.mAppOpsService;
        appOpsService.startOperation(AppOpsManager.getToken(appOpsService), 76, serviceRecord.appInfo.uid, serviceRecord.packageName, null, true, false, null, false, 0, -1);
        registerAppOpCallbackLocked(serviceRecord);
        synchronized (this.mFGSLogger) {
            this.mFGSLogger.logForegroundServiceStart(serviceRecord.appInfo.uid, 0, serviceRecord);
        }
        logFGSStateChangeLocked(serviceRecord, 1, 0, 0, 0);
        if (serviceConnection != null) {
            this.mAm.mHandler.post(new Runnable() { // from class: com.android.server.am.ActiveServices$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ActiveServices.lambda$startForegroundServiceDelegateLocked$8(serviceConnection, componentName, foregroundServiceDelegation);
                }
            });
        }
        signalForegroundServiceObserversLocked(serviceRecord);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$startForegroundServiceDelegateLocked$8(ServiceConnection serviceConnection, ComponentName componentName, ForegroundServiceDelegation foregroundServiceDelegation) {
        serviceConnection.onServiceConnected(componentName, foregroundServiceDelegation.mBinder);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopForegroundServiceDelegateLocked(ForegroundServiceDelegationOptions foregroundServiceDelegationOptions) {
        ServiceRecord serviceRecord;
        int size = this.mFgsDelegations.size();
        while (true) {
            size--;
            if (size < 0) {
                serviceRecord = null;
                break;
            } else if (this.mFgsDelegations.keyAt(size).mOptions.isSameDelegate(foregroundServiceDelegationOptions)) {
                Slog.d("ActivityManager", "stopForegroundServiceDelegateLocked " + foregroundServiceDelegationOptions.getDescription());
                serviceRecord = this.mFgsDelegations.valueAt(size);
                break;
            }
        }
        if (serviceRecord != null) {
            bringDownServiceLocked(serviceRecord, false);
            return;
        }
        Slog.e("ActivityManager", "stopForegroundServiceDelegateLocked delegate does not exist " + foregroundServiceDelegationOptions.getDescription());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopForegroundServiceDelegateLocked(ServiceConnection serviceConnection) {
        ServiceRecord serviceRecord;
        int size = this.mFgsDelegations.size();
        while (true) {
            size--;
            if (size < 0) {
                serviceRecord = null;
                break;
            }
            ForegroundServiceDelegation keyAt = this.mFgsDelegations.keyAt(size);
            if (keyAt.mConnection == serviceConnection) {
                Slog.d("ActivityManager", "stopForegroundServiceDelegateLocked " + keyAt.mOptions.getDescription());
                serviceRecord = this.mFgsDelegations.valueAt(size);
                break;
            }
        }
        if (serviceRecord != null) {
            bringDownServiceLocked(serviceRecord, false);
        } else {
            Slog.e("ActivityManager", "stopForegroundServiceDelegateLocked delegate does not exist");
        }
    }

    private static void getClientPackages(ServiceRecord serviceRecord, ArraySet<String> arraySet) {
        ArrayMap<IBinder, ArrayList<ConnectionRecord>> connections = serviceRecord.getConnections();
        for (int size = connections.size() - 1; size >= 0; size--) {
            ArrayList<ConnectionRecord> valueAt = connections.valueAt(size);
            int size2 = valueAt.size();
            for (int i = 0; i < size2; i++) {
                ProcessRecord processRecord = valueAt.get(i).binding.client;
                if (processRecord != null) {
                    arraySet.add(processRecord.info.packageName);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArraySet<String> getClientPackagesLocked(String str) {
        ArraySet<String> arraySet = new ArraySet<>();
        for (int i : this.mAm.mUserController.getUsers()) {
            ArrayMap<ComponentName, ServiceRecord> servicesLocked = getServicesLocked(i);
            int size = servicesLocked.size();
            for (int i2 = 0; i2 < size; i2++) {
                ServiceRecord valueAt = servicesLocked.valueAt(i2);
                if (valueAt.name.getPackageName().equals(str)) {
                    getClientPackages(valueAt, arraySet);
                }
            }
        }
        return arraySet;
    }

    private boolean isDeviceProvisioningPackage(String str) {
        if (this.mCachedDeviceProvisioningPackage == null) {
            this.mCachedDeviceProvisioningPackage = this.mAm.mContext.getResources().getString(R.string.config_timeZoneRulesUpdaterPackage);
        }
        String str2 = this.mCachedDeviceProvisioningPackage;
        return str2 != null && str2.equals(str);
    }

    public IActiveServicesWrapper getWrapper() {
        return this.mActiveServicesWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class ActiveServicesWrapper implements IActiveServicesWrapper {
        private ActiveServicesWrapper() {
        }

        @Override // com.android.server.am.IActiveServicesWrapper
        public IActiveServicesExt getExtImpl() {
            return ActiveServices.this.mActiveServicesExt;
        }

        @Override // com.android.server.am.IActiveServicesWrapper
        public void setDynamicalLogEnable(boolean z) {
            ActiveServices.DEBUG_DELAYED_SERVICE = z;
            ActiveServices.DEBUG_DELAYED_STARTS = z;
            ActiveServices.LOG_SERVICE_START_STOP = z;
        }
    }
}
