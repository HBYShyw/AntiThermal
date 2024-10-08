package com.android.server.appwidget;

import android.R;
import android.annotation.RequiresPermission;
import android.app.ActivityManager;
import android.app.ActivityManagerInternal;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.AppGlobals;
import android.app.AppOpsManager;
import android.app.AppOpsManagerInternal;
import android.app.BroadcastOptions;
import android.app.IApplicationThread;
import android.app.IServiceConnection;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManagerInternal;
import android.app.usage.UsageStatsManagerInternal;
import android.appwidget.AppWidgetManagerInternal;
import android.appwidget.AppWidgetProviderInfo;
import android.appwidget.PendingHostUpdate;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.content.pm.ParceledListSlice;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.ShortcutServiceInternal;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Point;
import android.graphics.drawable.Icon;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.DeviceConfig;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.AtomicFile;
import android.util.AttributeSet;
import android.util.IntArray;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import android.util.TypedValue;
import android.util.Xml;
import android.util.proto.ProtoOutputStream;
import android.view.Display;
import android.widget.RemoteViews;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.app.SuspendedAppActivity;
import com.android.internal.app.UnlaunchableAppActivity;
import com.android.internal.appwidget.IAppWidgetHost;
import com.android.internal.appwidget.IAppWidgetService;
import com.android.internal.os.BackgroundThread;
import com.android.internal.os.SomeArgs;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.DumpUtils;
import com.android.internal.widget.IRemoteViewsFactory;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.LocalServices;
import com.android.server.ServiceThread;
import com.android.server.WidgetBackupProvider;
import com.android.server.am.HostingRecord;
import com.android.server.appwidget.AppWidgetServiceImpl;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import org.xmlpull.v1.XmlPullParserException;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AppWidgetServiceImpl extends IAppWidgetService.Stub implements WidgetBackupProvider, DevicePolicyManagerInternal.OnCrossProfileWidgetProvidersChangeListener {
    private static final int CURRENT_VERSION = 1;
    static final boolean DEBUG_PROVIDER_INFO_CACHE = true;
    private static final int ID_PROVIDER_CHANGED = 1;
    private static final int ID_VIEWS_UPDATE = 0;
    private static final int KEYGUARD_HOST_ID = 1262836039;
    private static final int MIN_UPDATE_PERIOD = 1800000;
    private static final String NEW_KEYGUARD_HOST_PACKAGE = "com.android.keyguard";
    private static final String OLD_KEYGUARD_HOST_PACKAGE = "android";
    private static final String STATE_FILENAME = "appwidgets.xml";
    private static final String TAG = "AppWidgetServiceImpl";
    private static final int TAG_UNDEFINED = -1;
    private static final int UNKNOWN_UID = -1;
    private static final int UNKNOWN_USER_ID = -10;
    private ActivityManagerInternal mActivityManagerInternal;
    private AlarmManager mAlarmManager;
    private AppOpsManager mAppOpsManager;
    private AppOpsManagerInternal mAppOpsManagerInternal;
    private BackupRestoreController mBackupRestoreController;
    private Handler mCallbackHandler;
    private final Context mContext;
    private DevicePolicyManagerInternal mDevicePolicyManagerInternal;
    private Bundle mInteractiveBroadcast;
    private boolean mIsCombinedBroadcastEnabled;
    private boolean mIsProviderInfoPersisted;
    private KeyguardManager mKeyguardManager;
    private int mMaxWidgetBitmapMemory;
    private IPackageManager mPackageManager;
    private PackageManagerInternal mPackageManagerInternal;
    private boolean mSafeMode;
    private Handler mSaveStateHandler;
    private SecurityPolicy mSecurityPolicy;
    private UsageStatsManagerInternal mUsageStatsManagerInternal;
    private UserManager mUserManager;
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final AtomicLong UPDATE_COUNTER = new AtomicLong();
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.appwidget.AppWidgetServiceImpl.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int intExtra = intent.getIntExtra("android.intent.extra.user_handle", -10000);
            if (AppWidgetServiceImpl.DEBUG) {
                Slog.i(AppWidgetServiceImpl.TAG, "Received broadcast: " + action + " on user " + intExtra);
            }
            action.hashCode();
            char c = 65535;
            switch (action.hashCode()) {
                case -1238404651:
                    if (action.equals("android.intent.action.MANAGED_PROFILE_UNAVAILABLE")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1001645458:
                    if (action.equals("android.intent.action.PACKAGES_SUSPENDED")) {
                        c = 1;
                        break;
                    }
                    break;
                case -864107122:
                    if (action.equals("android.intent.action.MANAGED_PROFILE_AVAILABLE")) {
                        c = 2;
                        break;
                    }
                    break;
                case 1290767157:
                    if (action.equals("android.intent.action.PACKAGES_UNSUSPENDED")) {
                        c = 3;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                case 2:
                    synchronized (AppWidgetServiceImpl.this.mLock) {
                        AppWidgetServiceImpl.this.reloadWidgetsMaskedState(intExtra);
                    }
                    return;
                case 1:
                    AppWidgetServiceImpl.this.onPackageBroadcastReceived(intent, getSendingUserId());
                    AppWidgetServiceImpl.this.updateWidgetPackageSuspensionMaskedState(intent, true, getSendingUserId());
                    return;
                case 3:
                    AppWidgetServiceImpl.this.onPackageBroadcastReceived(intent, getSendingUserId());
                    AppWidgetServiceImpl.this.updateWidgetPackageSuspensionMaskedState(intent, false, getSendingUserId());
                    return;
                default:
                    AppWidgetServiceImpl.this.onPackageBroadcastReceived(intent, getSendingUserId());
                    return;
            }
        }
    };
    private final HashMap<Pair<Integer, Intent.FilterComparison>, HashSet<Integer>> mRemoteViewsServicesAppWidgets = new HashMap<>();
    private final Object mLock = new Object();
    private final ArrayList<Widget> mWidgets = new ArrayList<>();
    private final ArrayList<Host> mHosts = new ArrayList<>();
    private final ArrayList<Provider> mProviders = new ArrayList<>();
    private final ArraySet<Pair<Integer, String>> mPackagesWithBindWidgetPermission = new ArraySet<>();
    private final SparseBooleanArray mLoadedUserIds = new SparseBooleanArray();
    private final Object mWidgetPackagesLock = new Object();
    private final SparseArray<ArraySet<String>> mWidgetPackages = new SparseArray<>();
    private final SparseIntArray mNextAppWidgetIds = new SparseIntArray();
    private IAppWidgetServiceImplExt mAppWidgetServiceExt = (IAppWidgetServiceImplExt) ExtLoader.type(IAppWidgetServiceImplExt.class).base(this).create();

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppWidgetServiceImpl(Context context) {
        this.mContext = context;
    }

    @RequiresPermission("android.permission.READ_DEVICE_CONFIG")
    public void onStart() {
        this.mPackageManager = AppGlobals.getPackageManager();
        this.mAlarmManager = (AlarmManager) this.mContext.getSystemService(HostingRecord.TRIGGER_TYPE_ALARM);
        this.mUserManager = (UserManager) this.mContext.getSystemService("user");
        this.mAppOpsManager = (AppOpsManager) this.mContext.getSystemService("appops");
        this.mKeyguardManager = (KeyguardManager) this.mContext.getSystemService("keyguard");
        this.mDevicePolicyManagerInternal = (DevicePolicyManagerInternal) LocalServices.getService(DevicePolicyManagerInternal.class);
        this.mPackageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        this.mSaveStateHandler = BackgroundThread.getHandler();
        ServiceThread serviceThread = new ServiceThread(TAG, -2, false);
        serviceThread.start();
        this.mCallbackHandler = new CallbackHandler(serviceThread.getLooper());
        byte b = 0;
        this.mBackupRestoreController = new BackupRestoreController();
        this.mSecurityPolicy = new SecurityPolicy();
        this.mIsProviderInfoPersisted = !ActivityManager.isLowRamDeviceStatic() && DeviceConfig.getBoolean("systemui", "persists_widget_provider_info", true);
        this.mIsCombinedBroadcastEnabled = DeviceConfig.getBoolean("systemui", "combined_broadcast_enabled", true);
        if (!this.mIsProviderInfoPersisted) {
            Slog.d(TAG, "App widget provider info will not be persisted on this device");
        }
        BroadcastOptions makeBasic = BroadcastOptions.makeBasic();
        makeBasic.setBackgroundActivityStartsAllowed(false);
        makeBasic.setInteractive(true);
        this.mInteractiveBroadcast = makeBasic.toBundle();
        computeMaximumWidgetBitmapMemory();
        registerBroadcastReceiver();
        registerOnCrossProfileProvidersChangedListener();
        LocalServices.addService(AppWidgetManagerInternal.class, new AppWidgetManagerLocal());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void systemServicesReady() {
        this.mActivityManagerInternal = (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);
        this.mAppOpsManagerInternal = (AppOpsManagerInternal) LocalServices.getService(AppOpsManagerInternal.class);
        this.mUsageStatsManagerInternal = (UsageStatsManagerInternal) LocalServices.getService(UsageStatsManagerInternal.class);
    }

    private void computeMaximumWidgetBitmapMemory() {
        Display displayNoVerify = this.mContext.getDisplayNoVerify();
        Point point = new Point();
        displayNoVerify.getRealSize(point);
        this.mMaxWidgetBitmapMemory = point.x * 6 * point.y;
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addDataScheme("package");
        this.mContext.registerReceiverAsUser(this.mBroadcastReceiver, UserHandle.ALL, intentFilter, null, this.mCallbackHandler);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE");
        intentFilter2.addAction("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE");
        this.mContext.registerReceiverAsUser(this.mBroadcastReceiver, UserHandle.ALL, intentFilter2, null, this.mCallbackHandler);
        IntentFilter intentFilter3 = new IntentFilter();
        intentFilter3.addAction("android.intent.action.MANAGED_PROFILE_AVAILABLE");
        intentFilter3.addAction("android.intent.action.MANAGED_PROFILE_UNAVAILABLE");
        this.mContext.registerReceiverAsUser(this.mBroadcastReceiver, UserHandle.ALL, intentFilter3, null, this.mCallbackHandler);
        IntentFilter intentFilter4 = new IntentFilter();
        intentFilter4.addAction("android.intent.action.PACKAGES_SUSPENDED");
        intentFilter4.addAction("android.intent.action.PACKAGES_UNSUSPENDED");
        this.mContext.registerReceiverAsUser(this.mBroadcastReceiver, UserHandle.ALL, intentFilter4, null, this.mCallbackHandler);
    }

    private void registerOnCrossProfileProvidersChangedListener() {
        DevicePolicyManagerInternal devicePolicyManagerInternal = this.mDevicePolicyManagerInternal;
        if (devicePolicyManagerInternal != null) {
            devicePolicyManagerInternal.addOnCrossProfileWidgetProvidersChangeListener(this);
        }
    }

    public void setSafeMode(boolean z) {
        this.mSafeMode = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00a6, code lost:
    
        r8 = r1.length;
        r0 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00a8, code lost:
    
        if (r3 >= r8) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00aa, code lost:
    
        r0 = r0 | removeHostsAndProvidersForPackageLocked(r1[r3], r9);
        r3 = r3 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00b6, code lost:
    
        if (r8 == null) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00be, code lost:
    
        if (r8.getBoolean("android.intent.extra.REPLACING", false) != false) goto L62;
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:15:0x003d. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0074  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00e1 A[Catch: all -> 0x00f0, TryCatch #0 {, blocks: (B:30:0x007c, B:32:0x0084, B:35:0x008c, B:40:0x009a, B:45:0x00a6, B:47:0x00aa, B:51:0x00e1, B:52:0x00ec, B:56:0x00b8, B:59:0x00c2, B:61:0x00c6, B:64:0x00d2, B:66:0x00d8, B:68:0x00db, B:74:0x00ee), top: B:29:0x007c }] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x00f3 A[ADDED_TO_REGION, ORIG_RETURN, RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onPackageBroadcastReceived(Intent intent, int i) {
        boolean z;
        String[] stringArrayExtra;
        boolean z2;
        int uidForPackage;
        String schemeSpecificPart;
        String action = intent.getAction();
        action.hashCode();
        boolean z3 = true;
        int i2 = 0;
        boolean z4 = false;
        char c = 65535;
        switch (action.hashCode()) {
            case -1403934493:
                if (action.equals("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE")) {
                    c = 0;
                    break;
                }
                break;
            case -1338021860:
                if (action.equals("android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE")) {
                    c = 1;
                    break;
                }
                break;
            case -1001645458:
                if (action.equals("android.intent.action.PACKAGES_SUSPENDED")) {
                    c = 2;
                    break;
                }
                break;
            case 1290767157:
                if (action.equals("android.intent.action.PACKAGES_UNSUSPENDED")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                z = false;
                stringArrayExtra = intent.getStringArrayExtra("android.intent.extra.changed_package_list");
                z2 = false;
                if (stringArrayExtra != null || stringArrayExtra.length == 0) {
                    return;
                }
                synchronized (this.mLock) {
                    if (this.mUserManager.isUserUnlockingOrUnlocked(i) && !isProfileWithLockedParent(i)) {
                        ensureGroupStateLoadedLocked(i, false);
                        Bundle extras = intent.getExtras();
                        if (!z && !z2) {
                            if (extras != null && extras.getBoolean("android.intent.extra.REPLACING", false)) {
                                z3 = false;
                                break;
                            }
                            if (z4) {
                                saveGroupStateAsync(i);
                                scheduleNotifyGroupHostsForProvidersChangedLocked(i);
                                this.mBackupRestoreController.widgetComponentsChanged(i);
                            }
                            return;
                        }
                        z3 = false;
                        boolean z5 = false;
                        for (String str : stringArrayExtra) {
                            z5 |= updateProvidersForPackageLocked(str, i, null);
                            if (z3 && i == 0 && (uidForPackage = getUidForPackage(str, i)) >= 0) {
                                resolveHostUidLocked(str, uidForPackage);
                            }
                        }
                        z4 = z5;
                        if (z4) {
                        }
                        return;
                    }
                    return;
                }
            case 1:
                z = true;
                stringArrayExtra = intent.getStringArrayExtra("android.intent.extra.changed_package_list");
                z2 = false;
                if (stringArrayExtra != null) {
                    return;
                } else {
                    return;
                }
            case 2:
            case 3:
                stringArrayExtra = intent.getStringArrayExtra("android.intent.extra.changed_package_list");
                z2 = true;
                z = false;
                if (stringArrayExtra != null) {
                }
                break;
            default:
                Uri data = intent.getData();
                if (data == null || (schemeSpecificPart = data.getSchemeSpecificPart()) == null) {
                    return;
                }
                stringArrayExtra = new String[]{schemeSpecificPart};
                z = "android.intent.action.PACKAGE_ADDED".equals(action);
                z2 = "android.intent.action.PACKAGE_CHANGED".equals(action);
                if (stringArrayExtra != null) {
                }
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reloadWidgetsMaskedStateForGroup(int i) {
        if (this.mUserManager.isUserUnlockingOrUnlocked(i)) {
            synchronized (this.mLock) {
                reloadWidgetsMaskedState(i);
                for (int i2 : this.mUserManager.getEnabledProfileIds(i)) {
                    reloadWidgetsMaskedState(i2);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reloadWidgetsMaskedState(int i) {
        boolean z;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            UserInfo userInfo = this.mUserManager.getUserInfo(i);
            boolean z2 = !this.mUserManager.isUserUnlockingOrUnlocked(i);
            boolean isQuietModeEnabled = userInfo.isQuietModeEnabled();
            int size = this.mProviders.size();
            for (int i2 = 0; i2 < size; i2++) {
                Provider provider = this.mProviders.get(i2);
                if (provider.getUserId() == i) {
                    boolean maskedByLockedProfileLocked = provider.setMaskedByLockedProfileLocked(z2) | provider.setMaskedByQuietProfileLocked(isQuietModeEnabled);
                    try {
                        try {
                            z = this.mPackageManager.isPackageSuspendedForUser(provider.id.componentName.getPackageName(), provider.getUserId());
                        } catch (RemoteException e) {
                            Slog.e(TAG, "Failed to query application info", e);
                        }
                    } catch (IllegalArgumentException unused) {
                        z = false;
                    }
                    maskedByLockedProfileLocked |= provider.setMaskedBySuspendedPackageLocked(z);
                    if (maskedByLockedProfileLocked) {
                        if (provider.isMaskedLocked()) {
                            maskWidgetsViewsLocked(provider, null);
                        } else {
                            unmaskWidgetsViewsLocked(provider);
                        }
                    }
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateWidgetPackageSuspensionMaskedState(Intent intent, boolean z, int i) {
        String[] stringArrayExtra = intent.getStringArrayExtra("android.intent.extra.changed_package_list");
        if (stringArrayExtra == null) {
            return;
        }
        ArraySet arraySet = new ArraySet(Arrays.asList(stringArrayExtra));
        synchronized (this.mLock) {
            int size = this.mProviders.size();
            for (int i2 = 0; i2 < size; i2++) {
                Provider provider = this.mProviders.get(i2);
                if (provider.getUserId() == i && arraySet.contains(provider.id.componentName.getPackageName()) && provider.setMaskedBySuspendedPackageLocked(z)) {
                    if (provider.isMaskedLocked()) {
                        maskWidgetsViewsLocked(provider, null);
                    } else {
                        unmaskWidgetsViewsLocked(provider);
                    }
                }
            }
        }
    }

    private void maskWidgetsViewsLocked(Provider provider, Widget widget) {
        Intent createConfirmDeviceCredentialIntent;
        Icon createWithResource;
        int size = provider.widgets.size();
        if (size == 0) {
            return;
        }
        RemoteViews remoteViews = new RemoteViews(this.mContext.getPackageName(), 17367384);
        ApplicationInfo applicationInfo = provider.info.providerInfo.applicationInfo;
        int userId = provider.getUserId();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            boolean z = true;
            if (provider.maskedByQuietProfile) {
                createConfirmDeviceCredentialIntent = UnlaunchableAppActivity.createInQuietModeDialogIntent(userId);
            } else if (provider.maskedBySuspendedPackage) {
                boolean hasBadge = this.mUserManager.hasBadge(userId);
                String suspendingPackage = this.mPackageManagerInternal.getSuspendingPackage(applicationInfo.packageName, userId);
                if (OLD_KEYGUARD_HOST_PACKAGE.equals(suspendingPackage)) {
                    createConfirmDeviceCredentialIntent = this.mDevicePolicyManagerInternal.createShowAdminSupportIntent(userId, true);
                } else {
                    createConfirmDeviceCredentialIntent = SuspendedAppActivity.createSuspendedAppInterceptIntent(applicationInfo.packageName, suspendingPackage, this.mPackageManagerInternal.getSuspendedDialogInfo(applicationInfo.packageName, suspendingPackage, userId), (Bundle) null, (IntentSender) null, userId);
                }
                z = hasBadge;
            } else {
                createConfirmDeviceCredentialIntent = this.mKeyguardManager.createConfirmDeviceCredentialIntent(null, null, userId);
                if (createConfirmDeviceCredentialIntent != null) {
                    createConfirmDeviceCredentialIntent.setFlags(276824064);
                }
            }
            int i = applicationInfo.icon;
            if (i != 0) {
                createWithResource = Icon.createWithResource(applicationInfo.packageName, i);
            } else {
                createWithResource = Icon.createWithResource(this.mContext, R.drawable.sym_def_app_icon);
            }
            remoteViews.setImageViewIcon(16909745, createWithResource);
            if (!z) {
                remoteViews.setViewVisibility(16909746, 4);
            }
            for (int i2 = 0; i2 < size; i2++) {
                Widget widget2 = provider.widgets.get(i2);
                if (widget == null || widget == widget2) {
                    if (createConfirmDeviceCredentialIntent != null) {
                        remoteViews.setOnClickPendingIntent(R.id.background, PendingIntent.getActivity(this.mContext, widget2.appWidgetId, createConfirmDeviceCredentialIntent, AudioFormat.DTS_HD));
                    }
                    if (widget2.replaceWithMaskedViewsLocked(remoteViews)) {
                        scheduleNotifyUpdateAppWidgetLocked(widget2, widget2.getEffectiveViewsLocked());
                    }
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void unmaskWidgetsViewsLocked(Provider provider) {
        int size = provider.widgets.size();
        for (int i = 0; i < size; i++) {
            Widget widget = provider.widgets.get(i);
            if (widget.clearMaskedViewsLocked()) {
                scheduleNotifyUpdateAppWidgetLocked(widget, widget.getEffectiveViewsLocked());
            }
        }
    }

    private void resolveHostUidLocked(String str, int i) {
        int size = this.mHosts.size();
        for (int i2 = 0; i2 < size; i2++) {
            Host host = this.mHosts.get(i2);
            HostId hostId = host.id;
            if (hostId.uid == -1 && str.equals(hostId.packageName)) {
                if (DEBUG) {
                    Slog.i(TAG, "host " + host.id + " resolved to uid " + i);
                }
                HostId hostId2 = host.id;
                host.id = new HostId(i, hostId2.hostId, hostId2.packageName);
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void ensureGroupStateLoadedLocked(int i) {
        ensureGroupStateLoadedLocked(i, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void ensureGroupStateLoadedLocked(int i, boolean z) {
        if (z && !isUserRunningAndUnlocked(i)) {
            throw new IllegalStateException("User " + i + " must be unlocked for widgets to be available");
        }
        if (z && isProfileWithLockedParent(i)) {
            throw new IllegalStateException("Profile " + i + " must have unlocked parent");
        }
        int[] enabledGroupProfileIds = this.mSecurityPolicy.getEnabledGroupProfileIds(i);
        IntArray intArray = new IntArray(1);
        for (int i2 : enabledGroupProfileIds) {
            if (!this.mLoadedUserIds.get(i2)) {
                this.mLoadedUserIds.put(i2, true);
                intArray.add(i2);
            }
        }
        if (intArray.size() <= 0) {
            return;
        }
        int[] array = intArray.toArray();
        clearProvidersAndHostsTagsLocked();
        loadGroupWidgetProvidersLocked(array);
        loadGroupStateLocked(array);
    }

    private boolean isUserRunningAndUnlocked(int i) {
        return this.mUserManager.isUserUnlockingOrUnlocked(i);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (DumpUtils.checkDumpPermission(this.mContext, TAG, printWriter)) {
            synchronized (this.mLock) {
                if (strArr.length > 0 && "--proto".equals(strArr[0])) {
                    dumpProto(fileDescriptor);
                } else {
                    dumpInternalLocked(printWriter);
                }
            }
        }
    }

    private void dumpProto(FileDescriptor fileDescriptor) {
        Slog.i(TAG, "dump proto for " + this.mWidgets.size() + " widgets");
        ProtoOutputStream protoOutputStream = new ProtoOutputStream(fileDescriptor);
        int size = this.mWidgets.size();
        for (int i = 0; i < size; i++) {
            dumpProtoWidget(protoOutputStream, this.mWidgets.get(i));
        }
        protoOutputStream.flush();
    }

    private void dumpProtoWidget(ProtoOutputStream protoOutputStream, Widget widget) {
        if (widget.host == null || widget.provider == null) {
            Slog.d(TAG, "skip dumping widget because host or provider is null: widget.host=" + widget.host + " widget.provider=" + widget.provider);
            return;
        }
        long start = protoOutputStream.start(2246267895809L);
        protoOutputStream.write(1133871366145L, widget.host.getUserId() != widget.provider.getUserId());
        protoOutputStream.write(1133871366146L, widget.host.callbacks == null);
        protoOutputStream.write(1138166333443L, widget.host.id.packageName);
        protoOutputStream.write(1138166333444L, widget.provider.id.componentName.getPackageName());
        protoOutputStream.write(1138166333445L, widget.provider.id.componentName.getClassName());
        Bundle bundle = widget.options;
        if (bundle != null) {
            protoOutputStream.write(1133871366154L, bundle.getBoolean("appWidgetRestoreCompleted"));
            protoOutputStream.write(1120986464262L, widget.options.getInt("appWidgetMinWidth", 0));
            protoOutputStream.write(1120986464263L, widget.options.getInt("appWidgetMinHeight", 0));
            protoOutputStream.write(1120986464264L, widget.options.getInt("appWidgetMaxWidth", 0));
            protoOutputStream.write(1120986464265L, widget.options.getInt("appWidgetMaxHeight", 0));
        }
        protoOutputStream.end(start);
    }

    private void dumpInternalLocked(PrintWriter printWriter) {
        int size = this.mProviders.size();
        printWriter.println("Providers:");
        for (int i = 0; i < size; i++) {
            dumpProviderLocked(this.mProviders.get(i), i, printWriter);
        }
        int size2 = this.mWidgets.size();
        printWriter.println(" ");
        printWriter.println("Widgets:");
        for (int i2 = 0; i2 < size2; i2++) {
            dumpWidget(this.mWidgets.get(i2), i2, printWriter);
        }
        int size3 = this.mHosts.size();
        printWriter.println(" ");
        printWriter.println("Hosts:");
        for (int i3 = 0; i3 < size3; i3++) {
            dumpHost(this.mHosts.get(i3), i3, printWriter);
        }
        int size4 = this.mPackagesWithBindWidgetPermission.size();
        printWriter.println(" ");
        printWriter.println("Grants:");
        for (int i4 = 0; i4 < size4; i4++) {
            dumpGrant(this.mPackagesWithBindWidgetPermission.valueAt(i4), i4, printWriter);
        }
    }

    public ParceledListSlice<PendingHostUpdate> startListening(IAppWidgetHost iAppWidgetHost, String str, int i, int[] iArr) {
        int callingUserId = UserHandle.getCallingUserId();
        if (DEBUG) {
            Slog.i(TAG, "startListening() " + callingUserId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(str);
        synchronized (this.mLock) {
            if (this.mSecurityPolicy.isInstantAppLocked(str, callingUserId)) {
                Slog.w(TAG, "Instant package " + str + " cannot host app widgets");
                return ParceledListSlice.emptyList();
            }
            ensureGroupStateLoadedLocked(callingUserId);
            Host lookupOrAddHostLocked = lookupOrAddHostLocked(new HostId(Binder.getCallingUid(), i, str));
            lookupOrAddHostLocked.callbacks = iAppWidgetHost;
            long incrementAndGet = UPDATE_COUNTER.incrementAndGet();
            ArrayList arrayList = new ArrayList(iArr.length);
            LongSparseArray<PendingHostUpdate> longSparseArray = new LongSparseArray<>();
            for (int i2 : iArr) {
                longSparseArray.clear();
                lookupOrAddHostLocked.getPendingUpdatesForIdLocked(this.mContext, i2, longSparseArray);
                int size = longSparseArray.size();
                for (int i3 = 0; i3 < size; i3++) {
                    arrayList.add(longSparseArray.valueAt(i3));
                }
            }
            lookupOrAddHostLocked.lastWidgetUpdateSequenceNo = incrementAndGet;
            return new ParceledListSlice<>(arrayList);
        }
    }

    public void stopListening(String str, int i) {
        int callingUserId = UserHandle.getCallingUserId();
        if (DEBUG) {
            Slog.i(TAG, "stopListening() " + callingUserId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(str);
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(callingUserId, false);
            Host lookupHostLocked = lookupHostLocked(new HostId(Binder.getCallingUid(), i, str));
            if (lookupHostLocked != null) {
                lookupHostLocked.callbacks = null;
                pruneHostLocked(lookupHostLocked);
                this.mAppOpsManagerInternal.updateAppWidgetVisibility(lookupHostLocked.getWidgetUidsIfBound(), false);
            }
        }
    }

    public int allocateAppWidgetId(String str, int i) {
        int callingUserId = UserHandle.getCallingUserId();
        boolean z = DEBUG;
        if (z) {
            Slog.i(TAG, "allocateAppWidgetId() " + callingUserId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(str);
        synchronized (this.mLock) {
            if (this.mSecurityPolicy.isInstantAppLocked(str, callingUserId)) {
                Slog.w(TAG, "Instant package " + str + " cannot host app widgets");
                return 0;
            }
            ensureGroupStateLoadedLocked(callingUserId);
            if (this.mNextAppWidgetIds.indexOfKey(callingUserId) < 0) {
                this.mNextAppWidgetIds.put(callingUserId, 1);
            }
            int incrementAndGetAppWidgetIdLocked = incrementAndGetAppWidgetIdLocked(callingUserId);
            Host lookupOrAddHostLocked = lookupOrAddHostLocked(new HostId(Binder.getCallingUid(), i, str));
            Widget widget = new Widget();
            widget.appWidgetId = incrementAndGetAppWidgetIdLocked;
            widget.host = lookupOrAddHostLocked;
            lookupOrAddHostLocked.widgets.add(widget);
            addWidgetLocked(widget);
            saveGroupStateAsync(callingUserId);
            if (z) {
                Slog.i(TAG, "Allocated widget id " + incrementAndGetAppWidgetIdLocked + " for host " + lookupOrAddHostLocked.id);
            }
            return incrementAndGetAppWidgetIdLocked;
        }
    }

    public void setAppWidgetHidden(String str, int i) {
        int callingUserId = UserHandle.getCallingUserId();
        if (DEBUG) {
            Slog.i(TAG, "setAppWidgetHidden() " + callingUserId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(str);
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(callingUserId, false);
            Host lookupHostLocked = lookupHostLocked(new HostId(Binder.getCallingUid(), i, str));
            if (lookupHostLocked != null) {
                this.mAppOpsManagerInternal.updateAppWidgetVisibility(lookupHostLocked.getWidgetUidsIfBound(), false);
            }
        }
    }

    public void deleteAppWidgetId(String str, int i) {
        int callingUserId = UserHandle.getCallingUserId();
        boolean z = DEBUG;
        if (z) {
            Slog.i(TAG, "deleteAppWidgetId() " + callingUserId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(str);
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(callingUserId);
            Widget lookupWidgetLocked = lookupWidgetLocked(i, Binder.getCallingUid(), str);
            if (lookupWidgetLocked == null) {
                return;
            }
            deleteAppWidgetLocked(lookupWidgetLocked);
            saveGroupStateAsync(callingUserId);
            if (z) {
                Slog.i(TAG, "Deleted widget id " + i + " for host " + lookupWidgetLocked.host.id);
            }
        }
    }

    public boolean hasBindAppWidgetPermission(String str, int i) {
        if (DEBUG) {
            Slog.i(TAG, "hasBindAppWidgetPermission() " + UserHandle.getCallingUserId());
        }
        this.mSecurityPolicy.enforceModifyAppWidgetBindPermissions(str);
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(i);
            if (getUidForPackage(str, i) < 0) {
                return false;
            }
            return this.mPackagesWithBindWidgetPermission.contains(Pair.create(Integer.valueOf(i), str));
        }
    }

    public void setBindAppWidgetPermission(String str, int i, boolean z) {
        if (DEBUG) {
            Slog.i(TAG, "setBindAppWidgetPermission() " + UserHandle.getCallingUserId());
        }
        this.mSecurityPolicy.enforceModifyAppWidgetBindPermissions(str);
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(i);
            if (getUidForPackage(str, i) < 0) {
                return;
            }
            Pair<Integer, String> create = Pair.create(Integer.valueOf(i), str);
            if (z) {
                this.mPackagesWithBindWidgetPermission.add(create);
            } else {
                this.mPackagesWithBindWidgetPermission.remove(create);
            }
            saveGroupStateAsync(i);
        }
    }

    public IntentSender createAppWidgetConfigIntentSender(String str, int i, int i2) {
        IntentSender intentSender;
        int callingUserId = UserHandle.getCallingUserId();
        if (DEBUG) {
            Slog.i(TAG, "createAppWidgetConfigIntentSender() " + callingUserId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(str);
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(callingUserId);
            Widget lookupWidgetLocked = lookupWidgetLocked(i, Binder.getCallingUid(), str);
            if (lookupWidgetLocked == null) {
                throw new IllegalArgumentException("Bad widget id " + i);
            }
            Provider provider = lookupWidgetLocked.provider;
            if (provider == null) {
                throw new IllegalArgumentException("Widget not bound " + i);
            }
            Intent intent = new Intent("android.appwidget.action.APPWIDGET_CONFIGURE");
            intent.putExtra("appWidgetId", i);
            intent.setComponent(provider.getInfoLocked(this.mContext).configure);
            intent.setFlags(i2 & (-196));
            ActivityOptions pendingIntentCreatorBackgroundActivityStartMode = ActivityOptions.makeBasic().setPendingIntentCreatorBackgroundActivityStartMode(2);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                intentSender = PendingIntent.getActivityAsUser(this.mContext, 0, intent, 1409286144, pendingIntentCreatorBackgroundActivityStartMode.toBundle(), new UserHandle(provider.getUserId())).getIntentSender();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
        return intentSender;
    }

    public boolean bindAppWidgetId(String str, int i, int i2, ComponentName componentName, Bundle bundle) {
        ComponentName componentName2;
        ProviderId providerId;
        int callingUserId = UserHandle.getCallingUserId();
        if (DEBUG) {
            Slog.i(TAG, "bindAppWidgetId() " + callingUserId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(str);
        if (!this.mSecurityPolicy.isEnabledGroupProfile(i2) || !this.mSecurityPolicy.isProviderInCallerOrInProfileAndWhitelListed(componentName.getPackageName(), i2)) {
            return false;
        }
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(callingUserId);
            if (!this.mSecurityPolicy.hasCallerBindPermissionOrBindWhiteListedLocked(str)) {
                return false;
            }
            Widget lookupWidgetLocked = lookupWidgetLocked(i, Binder.getCallingUid(), str);
            if (lookupWidgetLocked == null) {
                Slog.e(TAG, "Bad widget id " + i);
                return false;
            }
            if (lookupWidgetLocked.provider != null) {
                Slog.e(TAG, "Widget id " + i + " already bound to: " + lookupWidgetLocked.provider.id);
                return false;
            }
            int uidForPackage = getUidForPackage(componentName.getPackageName(), i2);
            if (uidForPackage < 0) {
                Slog.e(TAG, "Package " + componentName.getPackageName() + " not installed  for profile " + i2);
                return false;
            }
            Provider lookupProviderLocked = lookupProviderLocked(new ProviderId(uidForPackage, componentName));
            if (lookupProviderLocked == null) {
                Slog.e(TAG, "No widget provider " + componentName + " for profile " + i2);
                return false;
            }
            if (lookupProviderLocked.zombie) {
                Slog.e(TAG, "Can't bind to a 3rd party provider in safe mode " + lookupProviderLocked);
                return false;
            }
            lookupWidgetLocked.provider = lookupProviderLocked;
            Bundle cloneIfLocalBinder = bundle != null ? cloneIfLocalBinder(bundle) : new Bundle();
            lookupWidgetLocked.options = cloneIfLocalBinder;
            if (!cloneIfLocalBinder.containsKey("appWidgetCategory")) {
                lookupWidgetLocked.options.putInt("appWidgetCategory", 1);
            }
            lookupProviderLocked.widgets.add(lookupWidgetLocked);
            onWidgetProviderAddedOrChangedLocked(lookupWidgetLocked);
            if (lookupProviderLocked.widgets.size() == 1) {
                sendEnableAndUpdateIntentLocked(lookupProviderLocked, new int[]{i});
            } else {
                sendUpdateIntentLocked(lookupProviderLocked, new int[]{i}, true);
            }
            registerForBroadcastsLocked(lookupProviderLocked, getWidgetIds(lookupProviderLocked.widgets));
            saveGroupStateAsync(callingUserId);
            Slog.i(TAG, "Bound widget " + i + " to provider " + lookupProviderLocked.id);
            AppWidgetProviderInfo appWidgetProviderInfo = lookupProviderLocked.info;
            if (appWidgetProviderInfo != null && (componentName2 = appWidgetProviderInfo.provider) != null && (providerId = lookupProviderLocked.id) != null) {
                Provider provider = lookupWidgetLocked.provider;
                if (provider.info.provider != null && provider.id != null) {
                    this.mAppWidgetServiceExt.hookUpdateWidgetSate(providerId.uid, componentName2.getPackageName(), true);
                }
            }
            return true;
        }
    }

    public int[] getAppWidgetIds(ComponentName componentName) {
        int callingUserId = UserHandle.getCallingUserId();
        if (DEBUG) {
            Slog.i(TAG, "getAppWidgetIds() " + callingUserId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(componentName.getPackageName());
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(callingUserId);
            Provider lookupProviderLocked = lookupProviderLocked(new ProviderId(Binder.getCallingUid(), componentName));
            if (lookupProviderLocked != null) {
                return getWidgetIds(lookupProviderLocked.widgets);
            }
            return new int[0];
        }
    }

    public int[] getAppWidgetIdsForHost(String str, int i) {
        int callingUserId = UserHandle.getCallingUserId();
        if (DEBUG) {
            Slog.i(TAG, "getAppWidgetIdsForHost() " + callingUserId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(str);
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(callingUserId);
            Host lookupHostLocked = lookupHostLocked(new HostId(Binder.getCallingUid(), i, str));
            if (lookupHostLocked != null) {
                return getWidgetIds(lookupHostLocked.widgets);
            }
            return new int[0];
        }
    }

    public boolean bindRemoteViewsService(String str, int i, Intent intent, IApplicationThread iApplicationThread, IBinder iBinder, IServiceConnection iServiceConnection, long j) {
        int callingUserId = UserHandle.getCallingUserId();
        if (DEBUG) {
            Slog.i(TAG, "bindRemoteViewsService() " + callingUserId);
        }
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(callingUserId);
            Widget lookupWidgetLocked = lookupWidgetLocked(i, Binder.getCallingUid(), str);
            if (lookupWidgetLocked == null) {
                throw new IllegalArgumentException("Bad widget id");
            }
            if (lookupWidgetLocked.provider == null) {
                throw new IllegalArgumentException("No provider for widget " + i);
            }
            ComponentName component = intent.getComponent();
            if (!component.getPackageName().equals(lookupWidgetLocked.provider.id.componentName.getPackageName())) {
                throw new SecurityException("The taget service not in the same package as the widget provider");
            }
            this.mSecurityPolicy.enforceServiceExistsAndRequiresBindRemoteViewsPermission(component, lookupWidgetLocked.provider.getUserId());
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                if (ActivityManager.getService().bindService(iApplicationThread, iBinder, intent, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), iServiceConnection, j & 33554433, this.mContext.getOpPackageName(), lookupWidgetLocked.provider.getUserId()) != 0) {
                    incrementAppWidgetServiceRefCount(i, Pair.create(Integer.valueOf(lookupWidgetLocked.provider.id.uid), new Intent.FilterComparison(intent)));
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    return true;
                }
            } catch (RemoteException unused) {
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                throw th;
            }
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return false;
        }
    }

    public void deleteHost(String str, int i) {
        int callingUserId = UserHandle.getCallingUserId();
        boolean z = DEBUG;
        if (z) {
            Slog.i(TAG, "deleteHost() " + callingUserId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(str);
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(callingUserId);
            Host lookupHostLocked = lookupHostLocked(new HostId(Binder.getCallingUid(), i, str));
            if (lookupHostLocked == null) {
                return;
            }
            deleteHostLocked(lookupHostLocked);
            saveGroupStateAsync(callingUserId);
            if (z) {
                Slog.i(TAG, "Deleted host " + lookupHostLocked.id);
            }
        }
    }

    public void deleteAllHosts() {
        int callingUserId = UserHandle.getCallingUserId();
        if (DEBUG) {
            Slog.i(TAG, "deleteAllHosts() " + callingUserId);
        }
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(callingUserId);
            boolean z = false;
            for (int size = this.mHosts.size() - 1; size >= 0; size--) {
                Host host = this.mHosts.get(size);
                if (host.id.uid == Binder.getCallingUid()) {
                    deleteHostLocked(host);
                    if (DEBUG) {
                        Slog.i(TAG, "Deleted host " + host.id);
                    }
                    z = true;
                }
            }
            if (z) {
                saveGroupStateAsync(callingUserId);
            }
        }
    }

    public AppWidgetProviderInfo getAppWidgetInfo(String str, int i) {
        Provider provider;
        int callingUserId = UserHandle.getCallingUserId();
        if (DEBUG) {
            Slog.i(TAG, "getAppWidgetInfo() " + callingUserId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(str);
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(callingUserId);
            Widget lookupWidgetLocked = lookupWidgetLocked(i, Binder.getCallingUid(), str);
            if (lookupWidgetLocked == null || (provider = lookupWidgetLocked.provider) == null || provider.zombie) {
                return null;
            }
            return cloneIfLocalBinder(provider.getInfoLocked(this.mContext));
        }
    }

    public RemoteViews getAppWidgetViews(String str, int i) {
        int callingUserId = UserHandle.getCallingUserId();
        if (DEBUG) {
            Slog.i(TAG, "getAppWidgetViews() " + callingUserId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(str);
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(callingUserId);
            Widget lookupWidgetLocked = lookupWidgetLocked(i, Binder.getCallingUid(), str);
            if (lookupWidgetLocked == null) {
                return null;
            }
            return cloneIfLocalBinder(lookupWidgetLocked.getEffectiveViewsLocked());
        }
    }

    public void updateAppWidgetOptions(String str, int i, Bundle bundle) {
        int callingUserId = UserHandle.getCallingUserId();
        if (DEBUG) {
            Slog.i(TAG, "updateAppWidgetOptions() " + callingUserId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(str);
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(callingUserId);
            Widget lookupWidgetLocked = lookupWidgetLocked(i, Binder.getCallingUid(), str);
            if (lookupWidgetLocked == null) {
                return;
            }
            lookupWidgetLocked.options.putAll(bundle);
            sendOptionsChangedIntentLocked(lookupWidgetLocked);
            this.mAppWidgetServiceExt.notifyUpdateAppWidgetTimeLocked(Binder.getCallingUid());
            saveGroupStateAsync(callingUserId);
        }
    }

    public Bundle getAppWidgetOptions(String str, int i) {
        Bundle bundle;
        int callingUserId = UserHandle.getCallingUserId();
        if (DEBUG) {
            Slog.i(TAG, "getAppWidgetOptions() " + callingUserId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(str);
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(callingUserId);
            Widget lookupWidgetLocked = lookupWidgetLocked(i, Binder.getCallingUid(), str);
            if (lookupWidgetLocked != null && (bundle = lookupWidgetLocked.options) != null) {
                return cloneIfLocalBinder(bundle);
            }
            return Bundle.EMPTY;
        }
    }

    public void updateAppWidgetIds(String str, int[] iArr, RemoteViews remoteViews) {
        if (DEBUG) {
            Slog.i(TAG, "updateAppWidgetIds() " + UserHandle.getCallingUserId());
        }
        updateAppWidgetIds(str, iArr, remoteViews, false);
    }

    public void partiallyUpdateAppWidgetIds(String str, int[] iArr, RemoteViews remoteViews) {
        if (DEBUG) {
            Slog.i(TAG, "partiallyUpdateAppWidgetIds() " + UserHandle.getCallingUserId());
        }
        updateAppWidgetIds(str, iArr, remoteViews, true);
    }

    public void notifyProviderInheritance(ComponentName[] componentNameArr) {
        AppWidgetProviderInfo appWidgetProviderInfo;
        int callingUserId = UserHandle.getCallingUserId();
        if (DEBUG) {
            Slog.i(TAG, "notifyProviderInheritance() " + callingUserId);
        }
        if (componentNameArr == null) {
            return;
        }
        for (ComponentName componentName : componentNameArr) {
            if (componentName == null) {
                return;
            }
            this.mSecurityPolicy.enforceCallFromPackage(componentName.getPackageName());
        }
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(callingUserId);
            for (ComponentName componentName2 : componentNameArr) {
                Provider lookupProviderLocked = lookupProviderLocked(new ProviderId(Binder.getCallingUid(), componentName2));
                if (lookupProviderLocked != null && (appWidgetProviderInfo = lookupProviderLocked.info) != null) {
                    appWidgetProviderInfo.isExtendedFromAppWidgetProvider = true;
                }
                return;
            }
            saveGroupStateAsync(callingUserId);
        }
    }

    public void notifyAppWidgetViewDataChanged(String str, int[] iArr, int i) {
        int callingUserId = UserHandle.getCallingUserId();
        if (DEBUG) {
            Slog.i(TAG, "notifyAppWidgetViewDataChanged() " + callingUserId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(str);
        if (iArr == null || iArr.length == 0) {
            return;
        }
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(callingUserId);
            for (int i2 : iArr) {
                Widget lookupWidgetLocked = lookupWidgetLocked(i2, Binder.getCallingUid(), str);
                if (lookupWidgetLocked != null) {
                    scheduleNotifyAppWidgetViewDataChanged(lookupWidgetLocked, i);
                    this.mAppWidgetServiceExt.notifyUpdateAppWidgetTimeLocked(Binder.getCallingUid());
                }
            }
        }
    }

    public void updateAppWidgetProvider(ComponentName componentName, RemoteViews remoteViews) {
        int callingUserId = UserHandle.getCallingUserId();
        if (DEBUG) {
            Slog.i(TAG, "updateAppWidgetProvider() " + callingUserId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(componentName.getPackageName());
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(callingUserId);
            ProviderId providerId = new ProviderId(Binder.getCallingUid(), componentName);
            Provider lookupProviderLocked = lookupProviderLocked(providerId);
            if (lookupProviderLocked == null) {
                Slog.w(TAG, "Provider doesn't exist " + providerId);
                return;
            }
            ArrayList<Widget> arrayList = lookupProviderLocked.widgets;
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                updateAppWidgetInstanceLocked(arrayList.get(i), remoteViews, false);
            }
            this.mAppWidgetServiceExt.notifyUpdateAppWidgetTimeLocked(Binder.getCallingUid());
        }
    }

    public void updateAppWidgetProviderInfo(ComponentName componentName, String str) {
        int callingUserId = UserHandle.getCallingUserId();
        if (DEBUG) {
            Slog.i(TAG, "updateAppWidgetProvider() " + callingUserId);
        }
        this.mSecurityPolicy.enforceCallFromPackage(componentName.getPackageName());
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(callingUserId);
            ProviderId providerId = new ProviderId(Binder.getCallingUid(), componentName);
            Provider lookupProviderLocked = lookupProviderLocked(providerId);
            if (lookupProviderLocked == null) {
                throw new IllegalArgumentException(componentName + " is not a valid AppWidget provider");
            }
            if (Objects.equals(lookupProviderLocked.infoTag, str)) {
                return;
            }
            String str2 = str == null ? "android.appwidget.provider" : str;
            AppWidgetProviderInfo parseAppWidgetProviderInfo = parseAppWidgetProviderInfo(this.mContext, providerId, lookupProviderLocked.getPartialInfoLocked().providerInfo, str2);
            if (parseAppWidgetProviderInfo == null) {
                throw new IllegalArgumentException("Unable to parse " + str2 + " meta-data to a valid AppWidget provider");
            }
            lookupProviderLocked.setInfoLocked(parseAppWidgetProviderInfo);
            lookupProviderLocked.infoTag = str;
            int size = lookupProviderLocked.widgets.size();
            for (int i = 0; i < size; i++) {
                Widget widget = lookupProviderLocked.widgets.get(i);
                scheduleNotifyProviderChangedLocked(widget);
                updateAppWidgetInstanceLocked(widget, widget.views, false);
            }
            saveGroupStateAsync(callingUserId);
            scheduleNotifyGroupHostsForProvidersChangedLocked(callingUserId);
        }
    }

    public boolean isRequestPinAppWidgetSupported() {
        synchronized (this.mLock) {
            if (this.mSecurityPolicy.isCallerInstantAppLocked()) {
                Slog.w(TAG, "Instant uid " + Binder.getCallingUid() + " query information about app widgets");
                return false;
            }
            return ((ShortcutServiceInternal) LocalServices.getService(ShortcutServiceInternal.class)).isRequestPinItemSupported(UserHandle.getCallingUserId(), 2);
        }
    }

    public boolean requestPinAppWidget(String str, ComponentName componentName, Bundle bundle, IntentSender intentSender) {
        int callingUid = Binder.getCallingUid();
        int userId = UserHandle.getUserId(callingUid);
        if (DEBUG) {
            Slog.i(TAG, "requestPinAppWidget() " + userId);
        }
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(userId);
            Provider lookupProviderLocked = lookupProviderLocked(new ProviderId(callingUid, componentName));
            if (lookupProviderLocked != null && !lookupProviderLocked.zombie) {
                AppWidgetProviderInfo infoLocked = lookupProviderLocked.getInfoLocked(this.mContext);
                if ((infoLocked.widgetCategory & 1) == 0) {
                    return false;
                }
                return ((ShortcutServiceInternal) LocalServices.getService(ShortcutServiceInternal.class)).requestPinAppWidget(str, infoLocked, bundle, intentSender, userId);
            }
            return false;
        }
    }

    public ParceledListSlice<AppWidgetProviderInfo> getInstalledProvidersForProfile(int i, int i2, String str) {
        boolean z;
        AppWidgetProviderInfo infoLocked;
        int identifier;
        int callingUserId = UserHandle.getCallingUserId();
        int callingUid = Binder.getCallingUid();
        if (DEBUG) {
            Slog.i(TAG, "getInstalledProvidersForProfiles() " + callingUserId);
        }
        if (!this.mSecurityPolicy.isEnabledGroupProfile(i2)) {
            return null;
        }
        synchronized (this.mLock) {
            if (this.mSecurityPolicy.isCallerInstantAppLocked()) {
                Slog.w(TAG, "Instant uid " + callingUid + " cannot access widget providers");
                return ParceledListSlice.emptyList();
            }
            ensureGroupStateLoadedLocked(callingUserId);
            ArrayList arrayList = new ArrayList();
            int size = this.mProviders.size();
            for (int i3 = 0; i3 < size; i3++) {
                Provider provider = this.mProviders.get(i3);
                String packageName = provider.id.componentName.getPackageName();
                if (str != null && !packageName.equals(str)) {
                    z = false;
                    if (!provider.zombie && z) {
                        infoLocked = provider.getInfoLocked(this.mContext);
                        if ((infoLocked.widgetCategory & i) != 0 && (identifier = infoLocked.getProfile().getIdentifier()) == i2 && this.mSecurityPolicy.isProviderInCallerOrInProfileAndWhitelListed(packageName, identifier) && !this.mPackageManagerInternal.filterAppAccess(packageName, callingUid, i2)) {
                            arrayList.add(cloneIfLocalBinder(infoLocked));
                        }
                    }
                }
                z = true;
                if (!provider.zombie) {
                    infoLocked = provider.getInfoLocked(this.mContext);
                    if ((infoLocked.widgetCategory & i) != 0) {
                        arrayList.add(cloneIfLocalBinder(infoLocked));
                    }
                }
            }
            return new ParceledListSlice<>(arrayList);
        }
    }

    private void updateAppWidgetIds(String str, int[] iArr, RemoteViews remoteViews, boolean z) {
        int callingUserId = UserHandle.getCallingUserId();
        if (iArr == null || iArr.length == 0) {
            return;
        }
        this.mSecurityPolicy.enforceCallFromPackage(str);
        if (DEBUG) {
            Log.d(TAG, "updateAppWidgetIds remoteViews = " + remoteViews.getViewId() + "\naction size = " + remoteViews.getSequenceNumber());
        }
        if (Trace.isTagEnabled(8L)) {
            Trace.traceBegin(8L, "updateAppWidgetIds:" + remoteViews.getViewId());
        }
        synchronized (this.mLock) {
            ensureGroupStateLoadedLocked(callingUserId);
            for (int i : iArr) {
                Widget lookupWidgetLocked = lookupWidgetLocked(i, Binder.getCallingUid(), str);
                if (lookupWidgetLocked != null) {
                    updateAppWidgetInstanceLocked(lookupWidgetLocked, remoteViews, z);
                    this.mAppWidgetServiceExt.notifyUpdateAppWidgetTimeLocked(Binder.getCallingUid());
                }
            }
        }
        Trace.traceEnd(8L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int incrementAndGetAppWidgetIdLocked(int i) {
        int peekNextAppWidgetIdLocked = peekNextAppWidgetIdLocked(i) + 1;
        this.mNextAppWidgetIds.put(i, peekNextAppWidgetIdLocked);
        return peekNextAppWidgetIdLocked;
    }

    private void setMinAppWidgetIdLocked(int i, int i2) {
        if (peekNextAppWidgetIdLocked(i) < i2) {
            this.mNextAppWidgetIds.put(i, i2);
        }
    }

    private int peekNextAppWidgetIdLocked(int i) {
        if (this.mNextAppWidgetIds.indexOfKey(i) < 0) {
            return 1;
        }
        return this.mNextAppWidgetIds.get(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Host lookupOrAddHostLocked(HostId hostId) {
        Host lookupHostLocked = lookupHostLocked(hostId);
        if (lookupHostLocked != null) {
            return lookupHostLocked;
        }
        Host host = new Host();
        host.id = hostId;
        this.mHosts.add(host);
        return host;
    }

    private void deleteHostLocked(Host host) {
        for (int size = host.widgets.size() - 1; size >= 0; size--) {
            deleteAppWidgetLocked(host.widgets.remove(size));
        }
        this.mHosts.remove(host);
        host.callbacks = null;
    }

    private void deleteAppWidgetLocked(Widget widget) {
        decrementAppWidgetServiceRefCount(widget);
        Host host = widget.host;
        host.widgets.remove(widget);
        pruneHostLocked(host);
        removeWidgetLocked(widget);
        Provider provider = widget.provider;
        if (provider != null) {
            provider.widgets.remove(widget);
            if (provider.zombie) {
                return;
            }
            sendDeletedIntentLocked(widget);
            if (provider.widgets.isEmpty()) {
                cancelBroadcastsLocked(provider);
                sendDisabledIntentLocked(provider);
            }
        }
    }

    private void cancelBroadcastsLocked(Provider provider) {
        if (DEBUG) {
            Slog.i(TAG, "cancelBroadcastsLocked() for " + provider);
        }
        final PendingIntent pendingIntent = provider.broadcast;
        if (pendingIntent != null) {
            this.mSaveStateHandler.post(new Runnable() { // from class: com.android.server.appwidget.AppWidgetServiceImpl$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    AppWidgetServiceImpl.this.lambda$cancelBroadcastsLocked$0(pendingIntent);
                }
            });
            provider.broadcast = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelBroadcastsLocked$0(PendingIntent pendingIntent) {
        this.mAlarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    private void destroyRemoteViewsService(final Intent intent, Widget widget) {
        ServiceConnection serviceConnection = new ServiceConnection() { // from class: com.android.server.appwidget.AppWidgetServiceImpl.2
            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName componentName) {
            }

            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                try {
                    IRemoteViewsFactory.Stub.asInterface(iBinder).onDestroy(intent);
                } catch (RemoteException e) {
                    Slog.e(AppWidgetServiceImpl.TAG, "Error calling remove view factory", e);
                }
                AppWidgetServiceImpl.this.mContext.unbindService(this);
            }
        };
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mContext.bindServiceAsUser(intent, serviceConnection, 33554433, widget.provider.id.getProfile());
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void incrementAppWidgetServiceRefCount(int i, Pair<Integer, Intent.FilterComparison> pair) {
        HashSet<Integer> hashSet;
        if (this.mRemoteViewsServicesAppWidgets.containsKey(pair)) {
            hashSet = this.mRemoteViewsServicesAppWidgets.get(pair);
        } else {
            HashSet<Integer> hashSet2 = new HashSet<>();
            this.mRemoteViewsServicesAppWidgets.put(pair, hashSet2);
            hashSet = hashSet2;
        }
        hashSet.add(Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void decrementAppWidgetServiceRefCount(Widget widget) {
        Iterator<Pair<Integer, Intent.FilterComparison>> it = this.mRemoteViewsServicesAppWidgets.keySet().iterator();
        while (it.hasNext()) {
            Pair<Integer, Intent.FilterComparison> next = it.next();
            HashSet<Integer> hashSet = this.mRemoteViewsServicesAppWidgets.get(next);
            if (hashSet.remove(Integer.valueOf(widget.appWidgetId)) && hashSet.isEmpty()) {
                destroyRemoteViewsService(((Intent.FilterComparison) next.second).getIntent(), widget);
                it.remove();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveGroupStateAsync(int i) {
        this.mSaveStateHandler.post(new SaveStateRunnable(i));
    }

    private void updateAppWidgetInstanceLocked(Widget widget, RemoteViews remoteViews, boolean z) {
        Provider provider;
        RemoteViews remoteViews2;
        int estimateMemoryUsage;
        RemoteViews remoteViews3;
        if (widget == null || (provider = widget.provider) == null || provider.zombie || widget.host.zombie) {
            return;
        }
        if (z && (remoteViews3 = widget.views) != null) {
            remoteViews3.mergeRemoteViews(remoteViews);
        } else {
            widget.views = remoteViews;
        }
        if (UserHandle.getAppId(Binder.getCallingUid()) != 1000 && (remoteViews2 = widget.views) != null && (estimateMemoryUsage = remoteViews2.estimateMemoryUsage()) > this.mMaxWidgetBitmapMemory) {
            widget.views = null;
            throw new IllegalArgumentException("RemoteViews for widget update exceeds maximum bitmap memory usage (used: " + estimateMemoryUsage + ", max: " + this.mMaxWidgetBitmapMemory + ")");
        }
        scheduleNotifyUpdateAppWidgetLocked(widget, widget.getEffectiveViewsLocked());
    }

    private void scheduleNotifyAppWidgetViewDataChanged(Widget widget, int i) {
        Host host;
        Provider provider;
        if (i == 0 || i == 1) {
            return;
        }
        long incrementAndGet = UPDATE_COUNTER.incrementAndGet();
        if (widget != null) {
            widget.updateSequenceNos.put(i, incrementAndGet);
        }
        if (widget == null || (host = widget.host) == null || host.zombie || host.callbacks == null || (provider = widget.provider) == null || provider.zombie) {
            return;
        }
        SomeArgs obtain = SomeArgs.obtain();
        Host host2 = widget.host;
        obtain.arg1 = host2;
        obtain.arg2 = host2.callbacks;
        obtain.arg3 = Long.valueOf(incrementAndGet);
        obtain.argi1 = widget.appWidgetId;
        obtain.argi2 = i;
        this.mCallbackHandler.obtainMessage(4, obtain).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNotifyAppWidgetViewDataChanged(Host host, IAppWidgetHost iAppWidgetHost, int i, int i2, long j) {
        try {
            iAppWidgetHost.viewDataChanged(i, i2);
            host.lastWidgetUpdateSequenceNo = j;
        } catch (RemoteException unused) {
            iAppWidgetHost = null;
        }
        synchronized (this.mLock) {
            if (iAppWidgetHost == null) {
                host.callbacks = null;
                for (Pair<Integer, Intent.FilterComparison> pair : this.mRemoteViewsServicesAppWidgets.keySet()) {
                    if (this.mRemoteViewsServicesAppWidgets.get(pair).contains(Integer.valueOf(i))) {
                        bindService(((Intent.FilterComparison) pair.second).getIntent(), new ServiceConnection() { // from class: com.android.server.appwidget.AppWidgetServiceImpl.3
                            @Override // android.content.ServiceConnection
                            public void onServiceDisconnected(ComponentName componentName) {
                            }

                            @Override // android.content.ServiceConnection
                            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                                try {
                                    IRemoteViewsFactory.Stub.asInterface(iBinder).onDataSetChangedAsync();
                                } catch (RemoteException e) {
                                    Slog.e(AppWidgetServiceImpl.TAG, "Error calling onDataSetChangedAsync()", e);
                                }
                                AppWidgetServiceImpl.this.mContext.unbindService(this);
                            }
                        }, new UserHandle(UserHandle.getUserId(((Integer) pair.first).intValue())));
                    }
                }
            }
        }
    }

    private void scheduleNotifyUpdateAppWidgetLocked(Widget widget, RemoteViews remoteViews) {
        Provider provider;
        long incrementAndGet = UPDATE_COUNTER.incrementAndGet();
        if (widget != null) {
            if (widget.trackingUpdate) {
                widget.trackingUpdate = false;
                Log.i(TAG, "Widget update received " + widget.toString());
                Trace.asyncTraceEnd(64L, "appwidget update-intent " + widget.provider.id.toString(), widget.appWidgetId);
            }
            widget.updateSequenceNos.put(0, incrementAndGet);
        }
        if (widget == null || (provider = widget.provider) == null || provider.zombie) {
            return;
        }
        Host host = widget.host;
        if (host.callbacks == null || host.zombie) {
            return;
        }
        if (remoteViews != null) {
            RemoteViews remoteViews2 = new RemoteViews(remoteViews);
            remoteViews2.setProviderInstanceId(incrementAndGet);
            remoteViews = remoteViews2;
        }
        SomeArgs obtain = SomeArgs.obtain();
        Host host2 = widget.host;
        obtain.arg1 = host2;
        obtain.arg2 = host2.callbacks;
        obtain.arg3 = remoteViews;
        obtain.arg4 = Long.valueOf(incrementAndGet);
        obtain.argi1 = widget.appWidgetId;
        this.mCallbackHandler.obtainMessage(1, obtain).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNotifyUpdateAppWidget(Host host, IAppWidgetHost iAppWidgetHost, int i, RemoteViews remoteViews, long j) {
        if (remoteViews != null && remoteViews.mApplication != null) {
            Slog.i(TAG, "handleNotifyUpdateAppWidget,appWidgetId: " + i + ",packageName:" + remoteViews.mApplication.packageName);
        }
        try {
            iAppWidgetHost.updateAppWidget(i, remoteViews);
            host.lastWidgetUpdateSequenceNo = j;
        } catch (RemoteException e) {
            synchronized (this.mLock) {
                Slog.e(TAG, "Widget host dead: " + host.id, e);
                host.callbacks = null;
            }
        }
    }

    @GuardedBy({"mLock"})
    private void scheduleNotifyProviderChangedLocked(Widget widget) {
        Provider provider;
        long incrementAndGet = UPDATE_COUNTER.incrementAndGet();
        if (widget != null) {
            widget.updateSequenceNos.clear();
            widget.updateSequenceNos.append(1, incrementAndGet);
        }
        if (widget == null || (provider = widget.provider) == null || provider.zombie) {
            return;
        }
        Host host = widget.host;
        if (host.callbacks == null || host.zombie) {
            return;
        }
        SomeArgs obtain = SomeArgs.obtain();
        Host host2 = widget.host;
        obtain.arg1 = host2;
        obtain.arg2 = host2.callbacks;
        obtain.arg3 = widget.provider.getInfoLocked(this.mContext);
        obtain.arg4 = Long.valueOf(incrementAndGet);
        obtain.argi1 = widget.appWidgetId;
        this.mCallbackHandler.obtainMessage(2, obtain).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNotifyProviderChanged(Host host, IAppWidgetHost iAppWidgetHost, int i, AppWidgetProviderInfo appWidgetProviderInfo, long j) {
        try {
            iAppWidgetHost.providerChanged(i, appWidgetProviderInfo);
            host.lastWidgetUpdateSequenceNo = j;
        } catch (RemoteException e) {
            synchronized (this.mLock) {
                Slog.e(TAG, "Widget host dead: " + host.id, e);
                host.callbacks = null;
            }
        }
    }

    private void scheduleNotifyAppWidgetRemovedLocked(Widget widget) {
        Provider provider;
        long incrementAndGet = UPDATE_COUNTER.incrementAndGet();
        if (widget != null) {
            if (widget.trackingUpdate) {
                widget.trackingUpdate = false;
                Log.i(TAG, "Widget removed " + widget.toString());
                Trace.asyncTraceEnd(64L, "appwidget update-intent " + widget.provider.id.toString(), widget.appWidgetId);
            }
            widget.updateSequenceNos.clear();
        }
        if (widget == null || (provider = widget.provider) == null || provider.zombie) {
            return;
        }
        Host host = widget.host;
        if (host.callbacks == null || host.zombie) {
            return;
        }
        SomeArgs obtain = SomeArgs.obtain();
        Host host2 = widget.host;
        obtain.arg1 = host2;
        obtain.arg2 = host2.callbacks;
        obtain.arg3 = Long.valueOf(incrementAndGet);
        obtain.argi1 = widget.appWidgetId;
        this.mCallbackHandler.obtainMessage(5, obtain).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNotifyAppWidgetRemoved(Host host, IAppWidgetHost iAppWidgetHost, int i, long j) {
        try {
            iAppWidgetHost.appWidgetRemoved(i);
            host.lastWidgetUpdateSequenceNo = j;
        } catch (RemoteException e) {
            synchronized (this.mLock) {
                Slog.e(TAG, "Widget host dead: " + host.id, e);
                host.callbacks = null;
            }
        }
    }

    private void scheduleNotifyGroupHostsForProvidersChangedLocked(int i) {
        int[] enabledGroupProfileIds = this.mSecurityPolicy.getEnabledGroupProfileIds(i);
        for (int size = this.mHosts.size() - 1; size >= 0; size--) {
            Host host = this.mHosts.get(size);
            int length = enabledGroupProfileIds.length;
            boolean z = false;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    break;
                }
                if (host.getUserId() == enabledGroupProfileIds[i2]) {
                    z = true;
                    break;
                }
                i2++;
            }
            if (z && host != null && !host.zombie && host.callbacks != null) {
                SomeArgs obtain = SomeArgs.obtain();
                obtain.arg1 = host;
                obtain.arg2 = host.callbacks;
                this.mCallbackHandler.obtainMessage(3, obtain).sendToTarget();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNotifyProvidersChanged(Host host, IAppWidgetHost iAppWidgetHost) {
        try {
            iAppWidgetHost.providersChanged();
        } catch (RemoteException e) {
            synchronized (this.mLock) {
                Slog.e(TAG, "Widget host dead: " + host.id, e);
                host.callbacks = null;
            }
        }
    }

    private static boolean isLocalBinder() {
        return Process.myPid() == Binder.getCallingPid();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static RemoteViews cloneIfLocalBinder(RemoteViews remoteViews) {
        return (!isLocalBinder() || remoteViews == null) ? remoteViews : remoteViews.clone();
    }

    private static AppWidgetProviderInfo cloneIfLocalBinder(AppWidgetProviderInfo appWidgetProviderInfo) {
        return (!isLocalBinder() || appWidgetProviderInfo == null) ? appWidgetProviderInfo : appWidgetProviderInfo.clone();
    }

    private static Bundle cloneIfLocalBinder(Bundle bundle) {
        return (!isLocalBinder() || bundle == null) ? bundle : (Bundle) bundle.clone();
    }

    private Widget lookupWidgetLocked(int i, int i2, String str) {
        int size = this.mWidgets.size();
        for (int i3 = 0; i3 < size; i3++) {
            Widget widget = this.mWidgets.get(i3);
            if (widget.appWidgetId == i && this.mSecurityPolicy.canAccessAppWidget(widget, i2, str)) {
                return widget;
            }
        }
        return null;
    }

    private Provider lookupProviderLocked(ProviderId providerId) {
        int size = this.mProviders.size();
        for (int i = 0; i < size; i++) {
            Provider provider = this.mProviders.get(i);
            if (provider.id.equals(providerId)) {
                return provider;
            }
        }
        return null;
    }

    private Host lookupHostLocked(HostId hostId) {
        int size = this.mHosts.size();
        for (int i = 0; i < size; i++) {
            Host host = this.mHosts.get(i);
            if (host.id.equals(hostId)) {
                return host;
            }
        }
        return null;
    }

    private void pruneHostLocked(Host host) {
        if (host.widgets.size() == 0 && host.callbacks == null) {
            if (DEBUG) {
                Slog.i(TAG, "Pruning host " + host.id);
            }
            this.mHosts.remove(host);
        }
    }

    @GuardedBy({"mLock"})
    private void loadGroupWidgetProvidersLocked(int[] iArr) {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
        ArrayList arrayList = null;
        for (int i : iArr) {
            List<ResolveInfo> queryIntentReceivers = queryIntentReceivers(intent, i);
            if (queryIntentReceivers != null && !queryIntentReceivers.isEmpty()) {
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                arrayList.addAll(queryIntentReceivers);
            }
        }
        int size = arrayList == null ? 0 : arrayList.size();
        for (int i2 = 0; i2 < size; i2++) {
            addProviderLocked((ResolveInfo) arrayList.get(i2));
        }
    }

    private boolean addProviderLocked(ResolveInfo resolveInfo) {
        if ((resolveInfo.activityInfo.applicationInfo.flags & 262144) != 0) {
            return false;
        }
        ActivityInfo activityInfo = resolveInfo.activityInfo;
        ComponentName componentName = new ComponentName(activityInfo.packageName, activityInfo.name);
        byte b = 0;
        byte b2 = 0;
        ProviderId providerId = new ProviderId(resolveInfo.activityInfo.applicationInfo.uid, componentName);
        Provider lookupProviderLocked = lookupProviderLocked(providerId);
        if (lookupProviderLocked == null) {
            lookupProviderLocked = lookupProviderLocked(new ProviderId(-1, componentName));
        }
        AppWidgetProviderInfo createPartialProviderInfo = createPartialProviderInfo(providerId, resolveInfo, lookupProviderLocked);
        if (createPartialProviderInfo == null) {
            return false;
        }
        if (lookupProviderLocked != null) {
            if (!lookupProviderLocked.zombie || this.mSafeMode) {
                return true;
            }
            lookupProviderLocked.id = providerId;
            lookupProviderLocked.zombie = false;
            lookupProviderLocked.setPartialInfoLocked(createPartialProviderInfo);
            if (!DEBUG) {
                return true;
            }
            Slog.i(TAG, "Provider placeholder now reified: " + lookupProviderLocked);
            return true;
        }
        Provider provider = new Provider();
        provider.id = providerId;
        provider.setPartialInfoLocked(createPartialProviderInfo);
        this.mProviders.add(provider);
        return true;
    }

    private void deleteWidgetsLocked(Provider provider, int i) {
        for (int size = provider.widgets.size() - 1; size >= 0; size--) {
            Widget widget = provider.widgets.get(size);
            if (i == -1 || i == widget.host.getUserId()) {
                provider.widgets.remove(size);
                updateAppWidgetInstanceLocked(widget, null, false);
                widget.host.widgets.remove(widget);
                removeWidgetLocked(widget);
                widget.provider = null;
                pruneHostLocked(widget.host);
                widget.host = null;
            }
        }
    }

    private void deleteProviderLocked(Provider provider) {
        deleteWidgetsLocked(provider, -1);
        this.mProviders.remove(provider);
        cancelBroadcastsLocked(provider);
    }

    private void sendEnableAndUpdateIntentLocked(Provider provider, int[] iArr) {
        AppWidgetProviderInfo appWidgetProviderInfo;
        if (!(this.mIsCombinedBroadcastEnabled && (appWidgetProviderInfo = provider.info) != null && appWidgetProviderInfo.isExtendedFromAppWidgetProvider)) {
            sendEnableIntentLocked(provider);
            sendUpdateIntentLocked(provider, iArr, true);
        } else {
            Intent intent = new Intent("android.appwidget.action.APPWIDGET_ENABLE_AND_UPDATE");
            intent.putExtra("appWidgetIds", iArr);
            intent.setComponent(provider.id.componentName);
            sendBroadcastAsUser(intent, provider.id.getProfile(), true);
        }
    }

    private void sendEnableIntentLocked(Provider provider) {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_ENABLED");
        intent.setComponent(provider.id.componentName);
        Slog.d(TAG, "sendEnableIntentLocked_ACTION_APPWIDGET_ENABLED");
        intent.setFlags(AudioFormat.EVRC);
        sendBroadcastAsUser(intent, provider.id.getProfile(), true);
    }

    private void sendUpdateIntentLocked(Provider provider, int[] iArr, boolean z) {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
        intent.putExtra("appWidgetIds", iArr);
        intent.setComponent(provider.id.componentName);
        Slog.d(TAG, "sendUpdateIntentLocked_ACTION_APPWIDGET_UPDATE");
        intent.setFlags(AudioFormat.EVRC);
        sendBroadcastAsUser(intent, provider.id.getProfile(), z);
    }

    private void sendDeletedIntentLocked(Widget widget) {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_DELETED");
        intent.setComponent(widget.provider.id.componentName);
        intent.putExtra("appWidgetId", widget.appWidgetId);
        sendBroadcastAsUser(intent, widget.provider.id.getProfile(), false);
    }

    private void sendDisabledIntentLocked(Provider provider) {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_DISABLED");
        intent.setComponent(provider.id.componentName);
        sendBroadcastAsUser(intent, provider.id.getProfile(), false);
    }

    public void sendOptionsChangedIntentLocked(Widget widget) {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE_OPTIONS");
        intent.setComponent(widget.provider.id.componentName);
        intent.putExtra("appWidgetId", widget.appWidgetId);
        intent.putExtra("appWidgetOptions", widget.options);
        sendBroadcastAsUser(intent, widget.provider.id.getProfile(), true);
    }

    @GuardedBy({"mLock"})
    private void registerForBroadcastsLocked(Provider provider, int[] iArr) {
        AppWidgetProviderInfo infoLocked = provider.getInfoLocked(this.mContext);
        if (infoLocked.updatePeriodMillis > 0) {
            boolean z = provider.broadcast != null;
            Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
            intent.putExtra("appWidgetIds", iArr);
            intent.setComponent(infoLocked.provider);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                provider.broadcast = PendingIntent.getBroadcastAsUser(this.mContext, 1, intent, AudioFormat.DTS_HD, infoLocked.getProfile());
                if (z) {
                    return;
                }
                final long max = Math.max(infoLocked.updatePeriodMillis, 1800000);
                final PendingIntent pendingIntent = provider.broadcast;
                this.mSaveStateHandler.post(new Runnable() { // from class: com.android.server.appwidget.AppWidgetServiceImpl$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        AppWidgetServiceImpl.this.lambda$registerForBroadcastsLocked$1(max, pendingIntent);
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerForBroadcastsLocked$1(long j, PendingIntent pendingIntent) {
        this.mAlarmManager.setInexactRepeating(this.mAppWidgetServiceExt.hookGetRepeatAlarmType(2), SystemClock.elapsedRealtime() + j, j, pendingIntent);
    }

    private static int[] getWidgetIds(ArrayList<Widget> arrayList) {
        int size = arrayList.size();
        int[] iArr = new int[size];
        for (int i = 0; i < size; i++) {
            iArr[i] = arrayList.get(i).appWidgetId;
        }
        return iArr;
    }

    private static void dumpProviderLocked(Provider provider, int i, PrintWriter printWriter) {
        AppWidgetProviderInfo partialInfoLocked = provider.getPartialInfoLocked();
        printWriter.print("  [");
        printWriter.print(i);
        printWriter.print("] provider ");
        printWriter.println(provider.id);
        printWriter.print("    min=(");
        printWriter.print(partialInfoLocked.minWidth);
        printWriter.print("x");
        printWriter.print(partialInfoLocked.minHeight);
        printWriter.print(")   minResize=(");
        printWriter.print(partialInfoLocked.minResizeWidth);
        printWriter.print("x");
        printWriter.print(partialInfoLocked.minResizeHeight);
        printWriter.print(") updatePeriodMillis=");
        printWriter.print(partialInfoLocked.updatePeriodMillis);
        printWriter.print(" resizeMode=");
        printWriter.print(partialInfoLocked.resizeMode);
        printWriter.print(" widgetCategory=");
        printWriter.print(partialInfoLocked.widgetCategory);
        printWriter.print(" autoAdvanceViewId=");
        printWriter.print(partialInfoLocked.autoAdvanceViewId);
        printWriter.print(" initialLayout=#");
        printWriter.print(Integer.toHexString(partialInfoLocked.initialLayout));
        printWriter.print(" initialKeyguardLayout=#");
        printWriter.print(Integer.toHexString(partialInfoLocked.initialKeyguardLayout));
        printWriter.print("   zombie=");
        printWriter.println(provider.zombie);
    }

    private static void dumpHost(Host host, int i, PrintWriter printWriter) {
        printWriter.print("  [");
        printWriter.print(i);
        printWriter.print("] hostId=");
        printWriter.println(host.id);
        printWriter.print("    callbacks=");
        printWriter.println(host.callbacks);
        printWriter.print("    widgets.size=");
        printWriter.print(host.widgets.size());
        printWriter.print(" zombie=");
        printWriter.println(host.zombie);
    }

    private static void dumpGrant(Pair<Integer, String> pair, int i, PrintWriter printWriter) {
        printWriter.print("  [");
        printWriter.print(i);
        printWriter.print(']');
        printWriter.print(" user=");
        printWriter.print(pair.first);
        printWriter.print(" package=");
        printWriter.println((String) pair.second);
    }

    private static void dumpWidget(Widget widget, int i, PrintWriter printWriter) {
        printWriter.print("  [");
        printWriter.print(i);
        printWriter.print("] id=");
        printWriter.println(widget.appWidgetId);
        printWriter.print("    host=");
        printWriter.println(widget.host.id);
        if (widget.provider != null) {
            printWriter.print("    provider=");
            printWriter.println(widget.provider.id);
        }
        if (widget.host != null) {
            printWriter.print("    host.callbacks=");
            printWriter.println(widget.host.callbacks);
        }
        if (widget.views != null) {
            printWriter.print("    views=");
            printWriter.println(widget.views);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void serializeProvider(TypedXmlSerializer typedXmlSerializer, Provider provider) throws IOException {
        Objects.requireNonNull(typedXmlSerializer);
        Objects.requireNonNull(provider);
        serializeProviderInner(typedXmlSerializer, provider, false);
    }

    private static void serializeProviderWithProviderInfo(TypedXmlSerializer typedXmlSerializer, Provider provider) throws IOException {
        Objects.requireNonNull(typedXmlSerializer);
        Objects.requireNonNull(provider);
        serializeProviderInner(typedXmlSerializer, provider, true);
    }

    private static void serializeProviderInner(TypedXmlSerializer typedXmlSerializer, Provider provider, boolean z) throws IOException {
        Objects.requireNonNull(typedXmlSerializer);
        Objects.requireNonNull(provider);
        typedXmlSerializer.startTag((String) null, "p");
        typedXmlSerializer.attribute((String) null, "pkg", provider.id.componentName.getPackageName());
        typedXmlSerializer.attribute((String) null, "cl", provider.id.componentName.getClassName());
        typedXmlSerializer.attributeIntHex((String) null, "tag", provider.tag);
        if (!TextUtils.isEmpty(provider.infoTag)) {
            typedXmlSerializer.attribute((String) null, "info_tag", provider.infoTag);
        }
        if (z && !provider.mInfoParsed) {
            Slog.d(TAG, "Provider info from " + provider.id.componentName + " won't be persisted.");
        }
        if (z && provider.mInfoParsed) {
            AppWidgetXmlUtil.writeAppWidgetProviderInfoLocked(typedXmlSerializer, provider.info);
        }
        typedXmlSerializer.endTag((String) null, "p");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void serializeHost(TypedXmlSerializer typedXmlSerializer, Host host) throws IOException {
        typedXmlSerializer.startTag((String) null, "h");
        typedXmlSerializer.attribute((String) null, "pkg", host.id.packageName);
        typedXmlSerializer.attributeIntHex((String) null, "id", host.id.hostId);
        typedXmlSerializer.attributeIntHex((String) null, "tag", host.tag);
        typedXmlSerializer.endTag((String) null, "h");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void serializeAppWidget(TypedXmlSerializer typedXmlSerializer, Widget widget, boolean z) throws IOException {
        typedXmlSerializer.startTag((String) null, "g");
        typedXmlSerializer.attributeIntHex((String) null, "id", widget.appWidgetId);
        typedXmlSerializer.attributeIntHex((String) null, "rid", widget.restoredId);
        typedXmlSerializer.attributeIntHex((String) null, "h", widget.host.tag);
        Provider provider = widget.provider;
        if (provider != null) {
            typedXmlSerializer.attributeIntHex((String) null, "p", provider.tag);
        }
        Bundle bundle = widget.options;
        if (bundle != null) {
            int i = bundle.getInt("appWidgetMinWidth");
            int i2 = widget.options.getInt("appWidgetMinHeight");
            int i3 = widget.options.getInt("appWidgetMaxWidth");
            int i4 = widget.options.getInt("appWidgetMaxHeight");
            if (i <= 0) {
                i = 0;
            }
            typedXmlSerializer.attributeIntHex((String) null, "min_width", i);
            if (i2 <= 0) {
                i2 = 0;
            }
            typedXmlSerializer.attributeIntHex((String) null, "min_height", i2);
            if (i3 <= 0) {
                i3 = 0;
            }
            typedXmlSerializer.attributeIntHex((String) null, "max_width", i3);
            if (i4 <= 0) {
                i4 = 0;
            }
            typedXmlSerializer.attributeIntHex((String) null, "max_height", i4);
            typedXmlSerializer.attributeIntHex((String) null, "host_category", widget.options.getInt("appWidgetCategory"));
            if (z) {
                typedXmlSerializer.attributeBoolean((String) null, "restore_completed", widget.options.getBoolean("appWidgetRestoreCompleted"));
            }
        }
        typedXmlSerializer.endTag((String) null, "g");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Bundle parseWidgetIdOptions(TypedXmlPullParser typedXmlPullParser) {
        Bundle bundle = new Bundle();
        if (typedXmlPullParser.getAttributeBoolean((String) null, "restore_completed", false)) {
            bundle.putBoolean("appWidgetRestoreCompleted", true);
        }
        int attributeIntHex = typedXmlPullParser.getAttributeIntHex((String) null, "min_width", -1);
        if (attributeIntHex != -1) {
            bundle.putInt("appWidgetMinWidth", attributeIntHex);
        }
        int attributeIntHex2 = typedXmlPullParser.getAttributeIntHex((String) null, "min_height", -1);
        if (attributeIntHex2 != -1) {
            bundle.putInt("appWidgetMinHeight", attributeIntHex2);
        }
        int attributeIntHex3 = typedXmlPullParser.getAttributeIntHex((String) null, "max_width", -1);
        if (attributeIntHex3 != -1) {
            bundle.putInt("appWidgetMaxWidth", attributeIntHex3);
        }
        int attributeIntHex4 = typedXmlPullParser.getAttributeIntHex((String) null, "max_height", -1);
        if (attributeIntHex4 != -1) {
            bundle.putInt("appWidgetMaxHeight", attributeIntHex4);
        }
        int attributeIntHex5 = typedXmlPullParser.getAttributeIntHex((String) null, "host_category", -1);
        if (attributeIntHex5 != -1) {
            bundle.putInt("appWidgetCategory", attributeIntHex5);
        }
        return bundle;
    }

    public List<String> getWidgetParticipants(int i) {
        return this.mBackupRestoreController.getWidgetParticipants(i);
    }

    public byte[] getWidgetState(String str, int i) {
        return this.mBackupRestoreController.getWidgetState(str, i);
    }

    public void systemRestoreStarting(int i) {
        this.mBackupRestoreController.systemRestoreStarting(i);
    }

    public void restoreWidgetState(String str, byte[] bArr, int i) {
        this.mBackupRestoreController.restoreWidgetState(str, bArr, i);
    }

    public void systemRestoreFinished(int i) {
        this.mBackupRestoreController.systemRestoreFinished(i);
    }

    private AppWidgetProviderInfo createPartialProviderInfo(ProviderId providerId, ResolveInfo resolveInfo, Provider provider) {
        Bundle bundle = resolveInfo.activityInfo.metaData;
        if (bundle == null) {
            return null;
        }
        if (!((provider == null || TextUtils.isEmpty(provider.infoTag) || bundle.getInt(provider.infoTag) == 0) ? false : true) && !(bundle.getInt("android.appwidget.provider") != 0)) {
            return null;
        }
        AppWidgetProviderInfo appWidgetProviderInfo = new AppWidgetProviderInfo();
        appWidgetProviderInfo.provider = providerId.componentName;
        appWidgetProviderInfo.providerInfo = resolveInfo.activityInfo;
        return appWidgetProviderInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static AppWidgetProviderInfo parseAppWidgetProviderInfo(Context context, ProviderId providerId, ActivityInfo activityInfo, String str) {
        int next;
        PackageManager packageManager = context.getPackageManager();
        try {
            XmlResourceParser loadXmlMetaData = activityInfo.loadXmlMetaData(packageManager, str);
            try {
                if (loadXmlMetaData == null) {
                    Slog.w(TAG, "No " + str + " meta-data for AppWidget provider '" + providerId + '\'');
                    if (loadXmlMetaData != null) {
                        loadXmlMetaData.close();
                    }
                    return null;
                }
                AttributeSet asAttributeSet = Xml.asAttributeSet(loadXmlMetaData);
                do {
                    next = loadXmlMetaData.next();
                    if (next == 1) {
                        break;
                    }
                } while (next != 2);
                if (!"appwidget-provider".equals(loadXmlMetaData.getName())) {
                    Slog.w(TAG, "Meta-data does not start with appwidget-provider tag for AppWidget provider " + providerId.componentName + " for user " + providerId.uid);
                    loadXmlMetaData.close();
                    return null;
                }
                AppWidgetProviderInfo appWidgetProviderInfo = new AppWidgetProviderInfo();
                appWidgetProviderInfo.provider = providerId.componentName;
                appWidgetProviderInfo.providerInfo = activityInfo;
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    Resources resourcesForApplication = packageManager.getResourcesForApplication(packageManager.getApplicationInfoAsUser(activityInfo.packageName, 0, UserHandle.getUserId(providerId.uid)));
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    TypedArray obtainAttributes = resourcesForApplication.obtainAttributes(asAttributeSet, com.android.internal.R.styleable.AppWidgetProviderInfo);
                    TypedValue peekValue = obtainAttributes.peekValue(1);
                    appWidgetProviderInfo.minWidth = peekValue != null ? peekValue.data : 0;
                    TypedValue peekValue2 = obtainAttributes.peekValue(2);
                    appWidgetProviderInfo.minHeight = peekValue2 != null ? peekValue2.data : 0;
                    TypedValue peekValue3 = obtainAttributes.peekValue(9);
                    appWidgetProviderInfo.minResizeWidth = peekValue3 != null ? peekValue3.data : appWidgetProviderInfo.minWidth;
                    TypedValue peekValue4 = obtainAttributes.peekValue(10);
                    appWidgetProviderInfo.minResizeHeight = peekValue4 != null ? peekValue4.data : appWidgetProviderInfo.minHeight;
                    TypedValue peekValue5 = obtainAttributes.peekValue(15);
                    appWidgetProviderInfo.maxResizeWidth = peekValue5 != null ? peekValue5.data : 0;
                    TypedValue peekValue6 = obtainAttributes.peekValue(16);
                    appWidgetProviderInfo.maxResizeHeight = peekValue6 != null ? peekValue6.data : 0;
                    appWidgetProviderInfo.targetCellWidth = obtainAttributes.getInt(17, 0);
                    appWidgetProviderInfo.targetCellHeight = obtainAttributes.getInt(18, 0);
                    appWidgetProviderInfo.updatePeriodMillis = obtainAttributes.getInt(3, 0);
                    appWidgetProviderInfo.initialLayout = obtainAttributes.getResourceId(4, 0);
                    appWidgetProviderInfo.initialKeyguardLayout = obtainAttributes.getResourceId(11, 0);
                    String string = obtainAttributes.getString(5);
                    if (string != null) {
                        appWidgetProviderInfo.configure = new ComponentName(providerId.componentName.getPackageName(), string);
                    }
                    appWidgetProviderInfo.label = activityInfo.loadLabel(packageManager).toString();
                    appWidgetProviderInfo.icon = activityInfo.getIconResource();
                    appWidgetProviderInfo.previewImage = obtainAttributes.getResourceId(6, 0);
                    appWidgetProviderInfo.previewLayout = obtainAttributes.getResourceId(14, 0);
                    appWidgetProviderInfo.autoAdvanceViewId = obtainAttributes.getResourceId(7, -1);
                    appWidgetProviderInfo.resizeMode = obtainAttributes.getInt(8, 0);
                    appWidgetProviderInfo.widgetCategory = obtainAttributes.getInt(12, 1);
                    appWidgetProviderInfo.widgetFeatures = obtainAttributes.getInt(13, 0);
                    appWidgetProviderInfo.descriptionRes = obtainAttributes.getResourceId(0, 0);
                    obtainAttributes.recycle();
                    loadXmlMetaData.close();
                    return appWidgetProviderInfo;
                } catch (Throwable th) {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    throw th;
                }
            } catch (Throwable th2) {
                if (loadXmlMetaData != null) {
                    try {
                        loadXmlMetaData.close();
                    } catch (Throwable th3) {
                        th2.addSuppressed(th3);
                    }
                }
                throw th2;
            }
        } catch (PackageManager.NameNotFoundException | IOException | XmlPullParserException e) {
            Slog.w(TAG, "XML parsing failed for AppWidget provider " + providerId.componentName + " for user " + providerId.uid, e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getUidForPackage(String str, int i) {
        PackageInfo packageInfo;
        ApplicationInfo applicationInfo;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            packageInfo = this.mPackageManager.getPackageInfo(str, 0L, i);
            Binder.restoreCallingIdentity(clearCallingIdentity);
        } catch (RemoteException unused) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            packageInfo = null;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
        if (packageInfo == null || (applicationInfo = packageInfo.applicationInfo) == null) {
            return -1;
        }
        return applicationInfo.uid;
    }

    private ActivityInfo getProviderInfo(ComponentName componentName, int i) {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
        intent.setComponent(componentName);
        List<ResolveInfo> queryIntentReceivers = queryIntentReceivers(intent, i);
        if (queryIntentReceivers.isEmpty()) {
            return null;
        }
        return queryIntentReceivers.get(0).activityInfo;
    }

    private List<ResolveInfo> queryIntentReceivers(Intent intent, int i) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            int hookqueryIntent = this.mAppWidgetServiceExt.hookqueryIntent(128) | AudioFormat.EVRC;
            if (isProfileWithUnlockedParent(i)) {
                hookqueryIntent |= 786432;
            }
            return this.mPackageManager.queryIntentReceivers(intent, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), hookqueryIntent | 1024, i).getList();
        } catch (RemoteException unused) {
            return Collections.emptyList();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    void handleUserUnlocked(int i) {
        if (isProfileWithLockedParent(i)) {
            return;
        }
        if (!this.mUserManager.isUserUnlockingOrUnlocked(i)) {
            Slog.w(TAG, "User " + i + " is no longer unlocked - exiting");
            return;
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        synchronized (this.mLock) {
            Trace.traceBegin(64L, "appwidget ensure");
            ensureGroupStateLoadedLocked(i);
            Trace.traceEnd(64L);
            Trace.traceBegin(64L, "appwidget reload");
            reloadWidgetsMaskedStateForGroup(this.mSecurityPolicy.getGroupParent(i));
            Trace.traceEnd(64L);
            int size = this.mProviders.size();
            for (int i2 = 0; i2 < size; i2++) {
                final Provider provider = this.mProviders.get(i2);
                if (provider.getUserId() == i && provider.widgets.size() > 0) {
                    Trace.traceBegin(64L, "appwidget init " + provider.id.componentName.getPackageName());
                    provider.widgets.forEach(new Consumer() { // from class: com.android.server.appwidget.AppWidgetServiceImpl$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            AppWidgetServiceImpl.lambda$handleUserUnlocked$2(AppWidgetServiceImpl.Provider.this, (AppWidgetServiceImpl.Widget) obj);
                        }
                    });
                    int[] widgetIds = getWidgetIds(provider.widgets);
                    sendEnableAndUpdateIntentLocked(provider, widgetIds);
                    registerForBroadcastsLocked(provider, widgetIds);
                    Trace.traceEnd(64L);
                }
            }
        }
        Slog.i(TAG, "Processing of handleUserUnlocked u" + i + " took " + (SystemClock.elapsedRealtime() - elapsedRealtime) + " ms");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$handleUserUnlocked$2(Provider provider, Widget widget) {
        widget.trackingUpdate = true;
        Trace.asyncTraceBegin(64L, "appwidget update-intent " + provider.id.toString(), widget.appWidgetId);
        Log.i(TAG, "Widget update scheduled on unlock " + widget.toString());
    }

    @GuardedBy({"mLock"})
    private void loadGroupStateLocked(int[] iArr) {
        ArrayList arrayList = new ArrayList();
        int i = 0;
        for (int i2 : iArr) {
            try {
                FileInputStream openRead = getSavedStateFile(i2).openRead();
                try {
                    i = readProfileStateFromFileLocked(openRead, i2, arrayList);
                    if (openRead != null) {
                        openRead.close();
                    }
                } catch (Throwable th) {
                    if (openRead != null) {
                        try {
                            openRead.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                    break;
                }
            } catch (IOException e) {
                Slog.w(TAG, "Failed to read state: " + e);
            }
        }
        if (i >= 0) {
            bindLoadedWidgetsLocked(arrayList);
            performUpgradeLocked(i);
            return;
        }
        Slog.w(TAG, "Failed to read state, clearing widgets and hosts.");
        clearWidgetsLocked();
        this.mHosts.clear();
        int size = this.mProviders.size();
        for (int i3 = 0; i3 < size; i3++) {
            this.mProviders.get(i3).widgets.clear();
        }
    }

    private void bindLoadedWidgetsLocked(List<LoadedWidgetState> list) {
        AppWidgetProviderInfo appWidgetProviderInfo;
        ComponentName componentName;
        ProviderId providerId;
        for (int size = list.size() - 1; size >= 0; size--) {
            LoadedWidgetState remove = list.remove(size);
            Widget widget = remove.widget;
            Provider findProviderByTag = findProviderByTag(remove.providerTag);
            widget.provider = findProviderByTag;
            if (findProviderByTag != null) {
                Host findHostByTag = findHostByTag(remove.hostTag);
                widget.host = findHostByTag;
                if (findHostByTag != null) {
                    widget.provider.widgets.add(widget);
                    widget.host.widgets.add(widget);
                    addWidgetLocked(widget);
                    Provider provider = widget.provider;
                    if (provider != null && (appWidgetProviderInfo = provider.info) != null && (componentName = appWidgetProviderInfo.provider) != null && (providerId = provider.id) != null) {
                        this.mAppWidgetServiceExt.hookUpdateWidgetSate(providerId.uid, componentName.getPackageName(), true);
                    }
                }
            }
        }
    }

    private Provider findProviderByTag(int i) {
        if (i < 0) {
            return null;
        }
        int size = this.mProviders.size();
        for (int i2 = 0; i2 < size; i2++) {
            Provider provider = this.mProviders.get(i2);
            if (provider.tag == i) {
                return provider;
            }
        }
        return null;
    }

    private Host findHostByTag(int i) {
        if (i < 0) {
            return null;
        }
        int size = this.mHosts.size();
        for (int i2 = 0; i2 < size; i2++) {
            Host host = this.mHosts.get(i2);
            if (host.tag == i) {
                return host;
            }
        }
        return null;
    }

    void addWidgetLocked(Widget widget) {
        this.mWidgets.add(widget);
        onWidgetProviderAddedOrChangedLocked(widget);
    }

    void onWidgetProviderAddedOrChangedLocked(Widget widget) {
        Provider provider = widget.provider;
        if (provider == null) {
            return;
        }
        int userId = provider.getUserId();
        synchronized (this.mWidgetPackagesLock) {
            ArraySet<String> arraySet = this.mWidgetPackages.get(userId);
            if (arraySet == null) {
                SparseArray<ArraySet<String>> sparseArray = this.mWidgetPackages;
                ArraySet<String> arraySet2 = new ArraySet<>();
                sparseArray.put(userId, arraySet2);
                arraySet = arraySet2;
            }
            arraySet.add(widget.provider.id.componentName.getPackageName());
        }
        if (widget.provider.isMaskedLocked()) {
            maskWidgetsViewsLocked(widget.provider, widget);
        } else {
            widget.clearMaskedViewsLocked();
        }
        IAppWidgetServiceImplExt iAppWidgetServiceImplExt = this.mAppWidgetServiceExt;
        int hashCode = widget.hashCode();
        Provider provider2 = widget.provider;
        iAppWidgetServiceImplExt.notifyOnWidgetProviderAddedOrChangedLocked(hashCode, provider2.id.uid, provider2.info.provider.getPackageName(), true);
    }

    void removeWidgetLocked(Widget widget) {
        Provider provider;
        AppWidgetProviderInfo appWidgetProviderInfo;
        ComponentName componentName;
        ProviderId providerId;
        Provider provider2;
        AppWidgetProviderInfo appWidgetProviderInfo2;
        this.mWidgets.remove(widget);
        onWidgetRemovedLocked(widget);
        scheduleNotifyAppWidgetRemovedLocked(widget);
        if (widget != null && (provider2 = widget.provider) != null && (appWidgetProviderInfo2 = provider2.info) != null && appWidgetProviderInfo2.provider != null && provider2.id != null) {
            this.mAppWidgetServiceExt.notifyRemoveAppWidget(widget.hashCode(), widget.provider.info.provider.getPackageName(), widget.provider.id.uid);
        }
        if (widget == null || (provider = widget.provider) == null || (appWidgetProviderInfo = provider.info) == null || (componentName = appWidgetProviderInfo.provider) == null || (providerId = provider.id) == null) {
            return;
        }
        this.mAppWidgetServiceExt.hookUpdateWidgetSate(providerId.uid, componentName.getPackageName(), false);
    }

    private void onWidgetRemovedLocked(Widget widget) {
        Provider provider = widget.provider;
        if (provider == null) {
            return;
        }
        int userId = provider.getUserId();
        String packageName = widget.provider.id.componentName.getPackageName();
        synchronized (this.mWidgetPackagesLock) {
            ArraySet<String> arraySet = this.mWidgetPackages.get(userId);
            if (arraySet == null) {
                return;
            }
            int size = this.mWidgets.size();
            for (int i = 0; i < size; i++) {
                Widget widget2 = this.mWidgets.get(i);
                Provider provider2 = widget2.provider;
                if (provider2 != null && provider2.getUserId() == userId && packageName.equals(widget2.provider.id.componentName.getPackageName())) {
                    return;
                }
            }
            arraySet.remove(packageName);
        }
    }

    void clearWidgetsLocked() {
        this.mWidgets.clear();
        onWidgetsClearedLocked();
        this.mAppWidgetServiceExt.notifyClearWidgetsLocked();
    }

    private void onWidgetsClearedLocked() {
        synchronized (this.mWidgetPackagesLock) {
            this.mWidgetPackages.clear();
        }
    }

    public boolean isBoundWidgetPackage(String str, int i) {
        if (Binder.getCallingUid() != 1000) {
            throw new SecurityException("Only the system process can call this");
        }
        synchronized (this.mWidgetPackagesLock) {
            ArraySet<String> arraySet = this.mWidgetPackages.get(i);
            if (arraySet == null) {
                return false;
            }
            return arraySet.contains(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void saveStateLocked(int i) {
        tagProvidersAndHosts();
        for (int i2 : this.mSecurityPolicy.getEnabledGroupProfileIds(i)) {
            AtomicFile savedStateFile = getSavedStateFile(i2);
            try {
                FileOutputStream startWrite = savedStateFile.startWrite();
                if (writeProfileStateToFileLocked(startWrite, i2)) {
                    savedStateFile.finishWrite(startWrite);
                } else {
                    savedStateFile.failWrite(startWrite);
                    Slog.w(TAG, "Failed to save state, restoring backup.");
                }
            } catch (IOException e) {
                Slog.w(TAG, "Failed open state file for write: " + e);
            }
        }
    }

    private void tagProvidersAndHosts() {
        int size = this.mProviders.size();
        for (int i = 0; i < size; i++) {
            this.mProviders.get(i).tag = i;
        }
        int size2 = this.mHosts.size();
        for (int i2 = 0; i2 < size2; i2++) {
            this.mHosts.get(i2).tag = i2;
        }
    }

    private void clearProvidersAndHostsTagsLocked() {
        int size = this.mProviders.size();
        for (int i = 0; i < size; i++) {
            this.mProviders.get(i).tag = -1;
        }
        int size2 = this.mHosts.size();
        for (int i2 = 0; i2 < size2; i2++) {
            this.mHosts.get(i2).tag = -1;
        }
    }

    @GuardedBy({"mLock"})
    private boolean writeProfileStateToFileLocked(FileOutputStream fileOutputStream, int i) {
        try {
            TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(fileOutputStream);
            resolveSerializer.startDocument((String) null, Boolean.TRUE);
            resolveSerializer.startTag((String) null, "gs");
            resolveSerializer.attributeInt((String) null, "version", 1);
            int size = this.mProviders.size();
            for (int i2 = 0; i2 < size; i2++) {
                Provider provider = this.mProviders.get(i2);
                if (provider.getUserId() == i) {
                    if (this.mIsProviderInfoPersisted) {
                        serializeProviderWithProviderInfo(resolveSerializer, provider);
                    } else if (provider.shouldBePersisted()) {
                        serializeProvider(resolveSerializer, provider);
                    }
                }
            }
            int size2 = this.mHosts.size();
            for (int i3 = 0; i3 < size2; i3++) {
                Host host = this.mHosts.get(i3);
                if (host.getUserId() == i) {
                    serializeHost(resolveSerializer, host);
                }
            }
            int size3 = this.mWidgets.size();
            for (int i4 = 0; i4 < size3; i4++) {
                Widget widget = this.mWidgets.get(i4);
                if (widget.host.getUserId() == i) {
                    serializeAppWidget(resolveSerializer, widget, true);
                }
            }
            Iterator<Pair<Integer, String>> it = this.mPackagesWithBindWidgetPermission.iterator();
            while (it.hasNext()) {
                Pair<Integer, String> next = it.next();
                if (((Integer) next.first).intValue() == i) {
                    resolveSerializer.startTag((String) null, "b");
                    resolveSerializer.attribute((String) null, "packageName", (String) next.second);
                    resolveSerializer.endTag((String) null, "b");
                }
            }
            resolveSerializer.endTag((String) null, "gs");
            resolveSerializer.endDocument();
            return true;
        } catch (IOException e) {
            Slog.w(TAG, "Failed to write state: " + e);
            return false;
        }
    }

    @GuardedBy({"mLock"})
    private int readProfileStateFromFileLocked(FileInputStream fileInputStream, int i, List<LoadedWidgetState> list) {
        int next;
        int i2;
        int uidForPackage;
        ComponentName componentName;
        ActivityInfo providerInfo;
        try {
            TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(fileInputStream);
            int i3 = -1;
            int i4 = -1;
            int i5 = -1;
            do {
                next = resolvePullParser.next();
                if (next == 2) {
                    String name = resolvePullParser.getName();
                    byte b = 0;
                    if ("gs".equals(name)) {
                        i5 = resolvePullParser.getAttributeInt((String) null, "version", 0);
                    } else if ("p".equals(name)) {
                        i3++;
                        String attributeValue = resolvePullParser.getAttributeValue((String) null, "pkg");
                        String attributeValue2 = resolvePullParser.getAttributeValue((String) null, "cl");
                        String canonicalPackageName = getCanonicalPackageName(attributeValue, attributeValue2, i);
                        if (canonicalPackageName != null && (uidForPackage = getUidForPackage(canonicalPackageName, i)) >= 0 && (providerInfo = getProviderInfo((componentName = new ComponentName(canonicalPackageName, attributeValue2)), i)) != null) {
                            ProviderId providerId = new ProviderId(uidForPackage, componentName);
                            Provider lookupProviderLocked = lookupProviderLocked(providerId);
                            if (lookupProviderLocked == null && this.mSafeMode) {
                                AppWidgetProviderInfo appWidgetProviderInfo = new AppWidgetProviderInfo();
                                appWidgetProviderInfo.provider = providerId.componentName;
                                appWidgetProviderInfo.providerInfo = providerInfo;
                                Provider provider = new Provider();
                                provider.setPartialInfoLocked(appWidgetProviderInfo);
                                provider.zombie = true;
                                provider.id = providerId;
                                this.mProviders.add(provider);
                                i2 = i5;
                                lookupProviderLocked = provider;
                            } else if (this.mIsProviderInfoPersisted) {
                                AppWidgetProviderInfo readAppWidgetProviderInfoLocked = AppWidgetXmlUtil.readAppWidgetProviderInfoLocked(resolvePullParser);
                                if (readAppWidgetProviderInfoLocked == null) {
                                    StringBuilder sb = new StringBuilder();
                                    i2 = i5;
                                    sb.append("Unable to load widget provider info from xml for ");
                                    sb.append(providerId.componentName);
                                    Slog.d(TAG, sb.toString());
                                } else {
                                    i2 = i5;
                                }
                                if (readAppWidgetProviderInfoLocked != null) {
                                    readAppWidgetProviderInfoLocked.provider = providerId.componentName;
                                    readAppWidgetProviderInfoLocked.providerInfo = providerInfo;
                                    lookupProviderLocked.setInfoLocked(readAppWidgetProviderInfoLocked);
                                }
                            } else {
                                i2 = i5;
                            }
                            lookupProviderLocked.tag = resolvePullParser.getAttributeIntHex((String) null, "tag", i3);
                            lookupProviderLocked.infoTag = resolvePullParser.getAttributeValue((String) null, "info_tag");
                        }
                        i2 = i5;
                    } else {
                        i2 = i5;
                        if ("h".equals(name)) {
                            i4++;
                            Host host = new Host();
                            String hookHostPackageNameOnReadProfileState = this.mAppWidgetServiceExt.hookHostPackageNameOnReadProfileState(resolvePullParser.getAttributeValue((String) null, "pkg"));
                            int uidForPackage2 = getUidForPackage(hookHostPackageNameOnReadProfileState, i);
                            if (uidForPackage2 < 0) {
                                host.zombie = true;
                            }
                            if (!host.zombie || this.mSafeMode) {
                                int attributeIntHex = resolvePullParser.getAttributeIntHex((String) null, "id");
                                host.tag = resolvePullParser.getAttributeIntHex((String) null, "tag", i4);
                                host.id = new HostId(uidForPackage2, attributeIntHex, hookHostPackageNameOnReadProfileState);
                                this.mHosts.add(host);
                            }
                        } else if ("b".equals(name)) {
                            String attributeValue3 = resolvePullParser.getAttributeValue((String) null, "packageName");
                            if (getUidForPackage(attributeValue3, i) >= 0) {
                                this.mPackagesWithBindWidgetPermission.add(Pair.create(Integer.valueOf(i), attributeValue3));
                            }
                        } else if ("g".equals(name)) {
                            Widget widget = new Widget();
                            int attributeIntHex2 = resolvePullParser.getAttributeIntHex((String) null, "id");
                            widget.appWidgetId = attributeIntHex2;
                            setMinAppWidgetIdLocked(i, attributeIntHex2 + 1);
                            widget.restoredId = resolvePullParser.getAttributeIntHex((String) null, "rid", 0);
                            widget.options = parseWidgetIdOptions(resolvePullParser);
                            list.add(new LoadedWidgetState(widget, resolvePullParser.getAttributeIntHex((String) null, "h"), resolvePullParser.getAttributeValue((String) null, "p") != null ? resolvePullParser.getAttributeIntHex((String) null, "p") : -1));
                        }
                    }
                } else {
                    i2 = i5;
                }
                i5 = i2;
            } while (next != 1);
            return i5;
        } catch (IOException | IndexOutOfBoundsException | NullPointerException | NumberFormatException | XmlPullParserException e) {
            Slog.w(TAG, "failed parsing " + e);
            return -1;
        }
    }

    private void performUpgradeLocked(int i) {
        int uidForPackage;
        if (i < 1) {
            Slog.v(TAG, "Upgrading widget database from " + i + " to 1");
        }
        if (i == 0) {
            Host lookupHostLocked = lookupHostLocked(new HostId(Process.myUid(), KEYGUARD_HOST_ID, OLD_KEYGUARD_HOST_PACKAGE));
            if (lookupHostLocked != null && (uidForPackage = getUidForPackage(NEW_KEYGUARD_HOST_PACKAGE, 0)) >= 0) {
                lookupHostLocked.id = new HostId(uidForPackage, KEYGUARD_HOST_ID, NEW_KEYGUARD_HOST_PACKAGE);
            }
            i = 1;
        }
        if (i != 1) {
            throw new IllegalStateException("Failed to upgrade widget database");
        }
    }

    private static File getStateFile(int i) {
        return new File(Environment.getUserSystemDirectory(i), STATE_FILENAME);
    }

    private static AtomicFile getSavedStateFile(int i) {
        File userSystemDirectory = Environment.getUserSystemDirectory(i);
        File stateFile = getStateFile(i);
        if (!stateFile.exists() && i == 0) {
            if (!userSystemDirectory.exists()) {
                userSystemDirectory.mkdirs();
            }
            new File("/data/system/appwidgets.xml").renameTo(stateFile);
        }
        return new AtomicFile(stateFile);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onUserStopped(int i) {
        synchronized (this.mLock) {
            int size = this.mWidgets.size() - 1;
            while (true) {
                boolean z = false;
                if (size < 0) {
                    break;
                }
                Widget widget = this.mWidgets.get(size);
                boolean z2 = widget.host.getUserId() == i;
                Provider provider = widget.provider;
                boolean z3 = provider != null;
                if (z3 && provider.getUserId() == i) {
                    z = true;
                }
                if (z2 && (!z3 || z)) {
                    removeWidgetLocked(widget);
                    widget.host.widgets.remove(widget);
                    widget.host = null;
                    if (z3) {
                        widget.provider.widgets.remove(widget);
                        widget.provider = null;
                    }
                }
                size--;
            }
            boolean z4 = false;
            for (int size2 = this.mHosts.size() - 1; size2 >= 0; size2--) {
                Host host = this.mHosts.get(size2);
                if (host.getUserId() == i) {
                    z4 |= !host.widgets.isEmpty();
                    deleteHostLocked(host);
                }
            }
            for (int size3 = this.mPackagesWithBindWidgetPermission.size() - 1; size3 >= 0; size3--) {
                if (((Integer) this.mPackagesWithBindWidgetPermission.valueAt(size3).first).intValue() == i) {
                    this.mPackagesWithBindWidgetPermission.removeAt(size3);
                }
            }
            int indexOfKey = this.mLoadedUserIds.indexOfKey(i);
            if (indexOfKey >= 0) {
                this.mLoadedUserIds.removeAt(indexOfKey);
            }
            int indexOfKey2 = this.mNextAppWidgetIds.indexOfKey(i);
            if (indexOfKey2 >= 0) {
                this.mNextAppWidgetIds.removeAt(indexOfKey2);
            }
            if (z4) {
                saveGroupStateAsync(i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyResourceOverlaysToWidgetsLocked(Set<String> set, int i, boolean z) {
        ApplicationInfo applicationInfo;
        AppWidgetProviderInfo appWidgetProviderInfo;
        ActivityInfo activityInfo;
        int size = this.mProviders.size();
        for (int i2 = 0; i2 < size; i2++) {
            Provider provider = this.mProviders.get(i2);
            if (provider.getUserId() == i) {
                String packageName = provider.id.componentName.getPackageName();
                if (z || set.contains(packageName)) {
                    try {
                        applicationInfo = this.mPackageManager.getApplicationInfo(packageName, 1024L, i);
                    } catch (RemoteException e) {
                        Slog.w(TAG, "Failed to retrieve app info for " + packageName + " userId=" + i, e);
                        applicationInfo = null;
                    }
                    if (applicationInfo != null && (appWidgetProviderInfo = provider.info) != null && (activityInfo = appWidgetProviderInfo.providerInfo) != null) {
                        if (applicationInfo.overlayPaths == null) {
                            Slog.w(TAG, "newAppInfo.overlayPaths is null, so return");
                        } else if (applicationInfo.resourceDirs == null) {
                            Slog.w(TAG, "newAppInfo.resourceDirs is null, so return");
                        } else {
                            ApplicationInfo applicationInfo2 = activityInfo.applicationInfo;
                            if (applicationInfo2 != null && applicationInfo.sourceDir.equals(applicationInfo2.sourceDir)) {
                                ApplicationInfo applicationInfo3 = new ApplicationInfo(applicationInfo2);
                                String[] strArr = applicationInfo.overlayPaths;
                                applicationInfo3.overlayPaths = strArr == null ? null : (String[]) strArr.clone();
                                String[] strArr2 = applicationInfo.resourceDirs;
                                applicationInfo3.resourceDirs = strArr2 != null ? (String[]) strArr2.clone() : null;
                                provider.info.providerInfo.applicationInfo = applicationInfo3;
                                int size2 = provider.widgets.size();
                                for (int i3 = 0; i3 < size2; i3++) {
                                    Widget widget = provider.widgets.get(i3);
                                    RemoteViews remoteViews = widget.views;
                                    if (remoteViews != null) {
                                        remoteViews.updateAppInfo(applicationInfo3);
                                    }
                                    RemoteViews remoteViews2 = widget.maskedViews;
                                    if (remoteViews2 != null) {
                                        remoteViews2.updateAppInfo(applicationInfo3);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @GuardedBy({"mLock"})
    private boolean updateProvidersForPackageLocked(String str, int i, Set<ProviderId> set) {
        HashSet hashSet = new HashSet();
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
        intent.setPackage(str);
        List<ResolveInfo> queryIntentReceivers = queryIntentReceivers(intent, i);
        int size = queryIntentReceivers == null ? 0 : queryIntentReceivers.size();
        boolean z = false;
        for (int i2 = 0; i2 < size; i2++) {
            ResolveInfo resolveInfo = queryIntentReceivers.get(i2);
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            if ((activityInfo.applicationInfo.flags & 262144) == 0 && str.equals(activityInfo.packageName)) {
                ProviderId providerId = new ProviderId(activityInfo.applicationInfo.uid, new ComponentName(activityInfo.packageName, activityInfo.name));
                Provider lookupProviderLocked = lookupProviderLocked(providerId);
                if (lookupProviderLocked == null) {
                    if (addProviderLocked(resolveInfo)) {
                        hashSet.add(providerId);
                    }
                } else {
                    AppWidgetProviderInfo createPartialProviderInfo = createPartialProviderInfo(providerId, resolveInfo, lookupProviderLocked);
                    if (createPartialProviderInfo != null) {
                        hashSet.add(providerId);
                        lookupProviderLocked.setPartialInfoLocked(createPartialProviderInfo);
                        int size2 = lookupProviderLocked.widgets.size();
                        if (size2 > 0) {
                            int[] widgetIds = getWidgetIds(lookupProviderLocked.widgets);
                            cancelBroadcastsLocked(lookupProviderLocked);
                            registerForBroadcastsLocked(lookupProviderLocked, widgetIds);
                            for (int i3 = 0; i3 < size2; i3++) {
                                Widget widget = lookupProviderLocked.widgets.get(i3);
                                widget.views = null;
                                scheduleNotifyProviderChangedLocked(widget);
                            }
                            sendUpdateIntentLocked(lookupProviderLocked, widgetIds, false);
                        }
                    }
                }
                z = true;
            }
        }
        for (int size3 = this.mProviders.size() - 1; size3 >= 0; size3--) {
            Provider provider = this.mProviders.get(size3);
            if (str.equals(provider.id.componentName.getPackageName()) && provider.getUserId() == i && !hashSet.contains(provider.id)) {
                if (set != null) {
                    set.add(provider.id);
                }
                deleteProviderLocked(provider);
                z = true;
            }
        }
        return z;
    }

    private void removeWidgetsForPackageLocked(String str, int i, int i2) {
        int size = this.mProviders.size();
        for (int i3 = 0; i3 < size; i3++) {
            Provider provider = this.mProviders.get(i3);
            if (str.equals(provider.id.componentName.getPackageName()) && provider.getUserId() == i && provider.widgets.size() > 0) {
                deleteWidgetsLocked(provider, i2);
            }
        }
    }

    private boolean removeProvidersForPackageLocked(String str, int i) {
        boolean z = false;
        for (int size = this.mProviders.size() - 1; size >= 0; size--) {
            Provider provider = this.mProviders.get(size);
            if (str.equals(provider.id.componentName.getPackageName()) && provider.getUserId() == i) {
                deleteProviderLocked(provider);
                z = true;
            }
        }
        return z;
    }

    private boolean removeHostsAndProvidersForPackageLocked(String str, int i) {
        boolean removeProvidersForPackageLocked = removeProvidersForPackageLocked(str, i);
        for (int size = this.mHosts.size() - 1; size >= 0; size--) {
            Host host = this.mHosts.get(size);
            if (str.equals(host.id.packageName) && host.getUserId() == i) {
                deleteHostLocked(host);
                removeProvidersForPackageLocked = true;
            }
        }
        return removeProvidersForPackageLocked;
    }

    private String getCanonicalPackageName(String str, String str2, int i) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            AppGlobals.getPackageManager().getReceiverInfo(new ComponentName(str, str2), 0L, i);
            return str;
        } catch (RemoteException unused) {
            String[] currentToCanonicalPackageNames = this.mContext.getPackageManager().currentToCanonicalPackageNames(new String[]{str});
            if (currentToCanonicalPackageNames != null && currentToCanonicalPackageNames.length > 0) {
                return currentToCanonicalPackageNames[0];
            }
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return null;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendBroadcastAsUser(Intent intent, UserHandle userHandle, boolean z) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mContext.sendBroadcastAsUser(intent, userHandle, null, z ? this.mInteractiveBroadcast : null);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void bindService(Intent intent, ServiceConnection serviceConnection, UserHandle userHandle) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mContext.bindServiceAsUser(intent, serviceConnection, 33554433, userHandle);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void unbindService(ServiceConnection serviceConnection) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mContext.unbindService(serviceConnection);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void onCrossProfileWidgetProvidersChanged(int i, List<String> list) {
        int profileParent = this.mSecurityPolicy.getProfileParent(i);
        if (profileParent != i) {
            synchronized (this.mLock) {
                ArraySet arraySet = new ArraySet();
                int size = this.mProviders.size();
                for (int i2 = 0; i2 < size; i2++) {
                    Provider provider = this.mProviders.get(i2);
                    if (provider.getUserId() == i) {
                        arraySet.add(provider.id.componentName.getPackageName());
                    }
                }
                int size2 = list.size();
                boolean z = false;
                for (int i3 = 0; i3 < size2; i3++) {
                    String str = list.get(i3);
                    arraySet.remove(str);
                    z |= updateProvidersForPackageLocked(str, i, null);
                }
                int size3 = arraySet.size();
                for (int i4 = 0; i4 < size3; i4++) {
                    removeWidgetsForPackageLocked((String) arraySet.valueAt(i4), i, profileParent);
                }
                if (z || size3 > 0) {
                    saveGroupStateAsync(i);
                    scheduleNotifyGroupHostsForProvidersChangedLocked(i);
                }
            }
        }
    }

    private boolean isProfileWithLockedParent(int i) {
        UserInfo profileParent;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            UserInfo userInfo = this.mUserManager.getUserInfo(i);
            if (userInfo != null && userInfo.isProfile() && (profileParent = this.mUserManager.getProfileParent(i)) != null) {
                if (!isUserRunningAndUnlocked(profileParent.getUserHandle().getIdentifier())) {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    return true;
                }
            }
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return false;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }

    private boolean isProfileWithUnlockedParent(int i) {
        UserInfo profileParent;
        UserInfo userInfo = this.mUserManager.getUserInfo(i);
        return userInfo != null && userInfo.isProfile() && (profileParent = this.mUserManager.getProfileParent(i)) != null && this.mUserManager.isUserUnlockingOrUnlocked(profileParent.getUserHandle());
    }

    public void noteAppWidgetTapped(String str, int i) {
        this.mSecurityPolicy.enforceCallFromPackage(str);
        int callingUid = Binder.getCallingUid();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (this.mActivityManagerInternal.getUidProcessState(callingUid) > 2) {
                return;
            }
            synchronized (this.mLock) {
                Widget lookupWidgetLocked = lookupWidgetLocked(i, callingUid, str);
                if (lookupWidgetLocked == null) {
                    return;
                }
                ProviderId providerId = lookupWidgetLocked.provider.id;
                String packageName = providerId.componentName.getPackageName();
                if (packageName == null) {
                    return;
                }
                SparseArray sparseArray = new SparseArray();
                sparseArray.put(providerId.uid, packageName);
                this.mAppOpsManagerInternal.updateAppWidgetVisibility(sparseArray, true);
                this.mUsageStatsManagerInternal.reportEvent(packageName, UserHandle.getUserId(providerId.uid), 7);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class CallbackHandler extends Handler {
        public static final int MSG_NOTIFY_APP_WIDGET_REMOVED = 5;
        public static final int MSG_NOTIFY_PROVIDERS_CHANGED = 3;
        public static final int MSG_NOTIFY_PROVIDER_CHANGED = 2;
        public static final int MSG_NOTIFY_UPDATE_APP_WIDGET = 1;
        public static final int MSG_NOTIFY_VIEW_DATA_CHANGED = 4;

        public CallbackHandler(Looper looper) {
            super(looper, null, false);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                SomeArgs someArgs = (SomeArgs) message.obj;
                Host host = (Host) someArgs.arg1;
                IAppWidgetHost iAppWidgetHost = (IAppWidgetHost) someArgs.arg2;
                RemoteViews remoteViews = (RemoteViews) someArgs.arg3;
                long longValue = ((Long) someArgs.arg4).longValue();
                int i2 = someArgs.argi1;
                someArgs.recycle();
                AppWidgetServiceImpl.this.handleNotifyUpdateAppWidget(host, iAppWidgetHost, i2, remoteViews, longValue);
                return;
            }
            if (i == 2) {
                SomeArgs someArgs2 = (SomeArgs) message.obj;
                Host host2 = (Host) someArgs2.arg1;
                IAppWidgetHost iAppWidgetHost2 = (IAppWidgetHost) someArgs2.arg2;
                AppWidgetProviderInfo appWidgetProviderInfo = (AppWidgetProviderInfo) someArgs2.arg3;
                long longValue2 = ((Long) someArgs2.arg4).longValue();
                int i3 = someArgs2.argi1;
                someArgs2.recycle();
                AppWidgetServiceImpl.this.handleNotifyProviderChanged(host2, iAppWidgetHost2, i3, appWidgetProviderInfo, longValue2);
                return;
            }
            if (i == 3) {
                SomeArgs someArgs3 = (SomeArgs) message.obj;
                Host host3 = (Host) someArgs3.arg1;
                IAppWidgetHost iAppWidgetHost3 = (IAppWidgetHost) someArgs3.arg2;
                someArgs3.recycle();
                AppWidgetServiceImpl.this.handleNotifyProvidersChanged(host3, iAppWidgetHost3);
                return;
            }
            if (i == 4) {
                SomeArgs someArgs4 = (SomeArgs) message.obj;
                Host host4 = (Host) someArgs4.arg1;
                IAppWidgetHost iAppWidgetHost4 = (IAppWidgetHost) someArgs4.arg2;
                long longValue3 = ((Long) someArgs4.arg3).longValue();
                int i4 = someArgs4.argi1;
                int i5 = someArgs4.argi2;
                someArgs4.recycle();
                AppWidgetServiceImpl.this.handleNotifyAppWidgetViewDataChanged(host4, iAppWidgetHost4, i4, i5, longValue3);
                return;
            }
            if (i != 5) {
                return;
            }
            SomeArgs someArgs5 = (SomeArgs) message.obj;
            Host host5 = (Host) someArgs5.arg1;
            IAppWidgetHost iAppWidgetHost5 = (IAppWidgetHost) someArgs5.arg2;
            long longValue4 = ((Long) someArgs5.arg3).longValue();
            int i6 = someArgs5.argi1;
            someArgs5.recycle();
            AppWidgetServiceImpl.this.handleNotifyAppWidgetRemoved(host5, iAppWidgetHost5, i6, longValue4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class SecurityPolicy {
        private SecurityPolicy() {
        }

        public boolean isEnabledGroupProfile(int i) {
            return isParentOrProfile(UserHandle.getCallingUserId(), i) && isProfileEnabled(i);
        }

        public int[] getEnabledGroupProfileIds(int i) {
            int groupParent = getGroupParent(i);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return AppWidgetServiceImpl.this.mUserManager.getEnabledProfileIds(groupParent);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void enforceServiceExistsAndRequiresBindRemoteViewsPermission(ComponentName componentName, int i) {
            ServiceInfo serviceInfo;
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                serviceInfo = AppWidgetServiceImpl.this.mPackageManager.getServiceInfo(componentName, 4096L, i);
            } catch (RemoteException unused) {
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                throw th;
            }
            if (serviceInfo == null) {
                throw new SecurityException("Service " + componentName + " not installed for user " + i);
            }
            if (!"android.permission.BIND_REMOTEVIEWS".equals(serviceInfo.permission)) {
                throw new SecurityException("Service " + componentName + " in user " + i + "does not require android.permission.BIND_REMOTEVIEWS");
            }
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }

        public void enforceModifyAppWidgetBindPermissions(String str) {
            AppWidgetServiceImpl.this.mContext.enforceCallingPermission("android.permission.MODIFY_APPWIDGET_BIND_PERMISSIONS", "hasBindAppWidgetPermission packageName=" + str);
        }

        public boolean isCallerInstantAppLocked() {
            int callingUid = Binder.getCallingUid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                String[] packagesForUid = AppWidgetServiceImpl.this.mPackageManager.getPackagesForUid(callingUid);
                if (!ArrayUtils.isEmpty(packagesForUid)) {
                    boolean isInstantApp = AppWidgetServiceImpl.this.mPackageManager.isInstantApp(packagesForUid[0], UserHandle.getUserId(callingUid));
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    return isInstantApp;
                }
            } catch (RemoteException unused) {
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                throw th;
            }
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return false;
        }

        public boolean isInstantAppLocked(String str, int i) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                boolean isInstantApp = AppWidgetServiceImpl.this.mPackageManager.isInstantApp(str, i);
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return isInstantApp;
            } catch (RemoteException unused) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return false;
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                throw th;
            }
        }

        public void enforceCallFromPackage(String str) {
            AppWidgetServiceImpl.this.mAppOpsManager.checkPackage(Binder.getCallingUid(), str);
        }

        public boolean hasCallerBindPermissionOrBindWhiteListedLocked(String str) {
            try {
                AppWidgetServiceImpl.this.mContext.enforceCallingOrSelfPermission("android.permission.BIND_APPWIDGET", null);
                return true;
            } catch (SecurityException unused) {
                return isCallerBindAppWidgetWhiteListedLocked(str);
            }
        }

        private boolean isCallerBindAppWidgetWhiteListedLocked(String str) {
            int callingUserId = UserHandle.getCallingUserId();
            if (AppWidgetServiceImpl.this.getUidForPackage(str, callingUserId) < 0) {
                throw new IllegalArgumentException("No package " + str + " for user " + callingUserId);
            }
            synchronized (AppWidgetServiceImpl.this.mLock) {
                AppWidgetServiceImpl.this.ensureGroupStateLoadedLocked(callingUserId);
                return AppWidgetServiceImpl.this.mPackagesWithBindWidgetPermission.contains(Pair.create(Integer.valueOf(callingUserId), str));
            }
        }

        public boolean canAccessAppWidget(Widget widget, int i, String str) {
            Provider provider;
            if (isHostInPackageForUid(widget.host, i, str) || isProviderInPackageForUid(widget.provider, i, str) || isHostAccessingProvider(widget.host, widget.provider, i, str)) {
                return true;
            }
            int userId = UserHandle.getUserId(i);
            return (widget.host.getUserId() == userId || ((provider = widget.provider) != null && provider.getUserId() == userId)) && AppWidgetServiceImpl.this.mContext.checkCallingPermission("android.permission.BIND_APPWIDGET") == 0;
        }

        private boolean isParentOrProfile(int i, int i2) {
            return i == i2 || getProfileParent(i2) == i;
        }

        public boolean isProviderInCallerOrInProfileAndWhitelListed(String str, int i) {
            int callingUserId = UserHandle.getCallingUserId();
            if (i == callingUserId) {
                return true;
            }
            if (getProfileParent(i) != callingUserId) {
                return false;
            }
            return isProviderWhiteListed(str, i);
        }

        public boolean isProviderWhiteListed(String str, int i) {
            if (AppWidgetServiceImpl.this.mDevicePolicyManagerInternal == null) {
                return false;
            }
            return AppWidgetServiceImpl.this.mDevicePolicyManagerInternal.getCrossProfileWidgetProviders(i).contains(str);
        }

        public int getProfileParent(int i) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                UserInfo profileParent = AppWidgetServiceImpl.this.mUserManager.getProfileParent(i);
                if (profileParent != null) {
                    return profileParent.getUserHandle().getIdentifier();
                }
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return AppWidgetServiceImpl.UNKNOWN_USER_ID;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public int getGroupParent(int i) {
            int profileParent = AppWidgetServiceImpl.this.mSecurityPolicy.getProfileParent(i);
            return profileParent != AppWidgetServiceImpl.UNKNOWN_USER_ID ? profileParent : i;
        }

        public boolean isHostInPackageForUid(Host host, int i, String str) {
            HostId hostId = host.id;
            return hostId.uid == i && hostId.packageName.equals(str);
        }

        public boolean isProviderInPackageForUid(Provider provider, int i, String str) {
            if (provider != null) {
                ProviderId providerId = provider.id;
                if (providerId.uid == i && providerId.componentName.getPackageName().equals(str)) {
                    return true;
                }
            }
            return false;
        }

        public boolean isHostAccessingProvider(Host host, Provider provider, int i, String str) {
            return host.id.uid == i && provider != null && provider.id.componentName.getPackageName().equals(str);
        }

        private boolean isProfileEnabled(int i) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                UserInfo userInfo = AppWidgetServiceImpl.this.mUserManager.getUserInfo(i);
                if (userInfo != null) {
                    if (userInfo.isEnabled()) {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        return true;
                    }
                }
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return false;
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class Provider {
        PendingIntent broadcast;
        ProviderId id;
        AppWidgetProviderInfo info;
        String infoTag;
        boolean mInfoParsed;
        boolean maskedByLockedProfile;
        boolean maskedByQuietProfile;
        boolean maskedBySuspendedPackage;
        int tag;
        ArrayList<Widget> widgets;
        boolean zombie;

        private Provider() {
            this.widgets = new ArrayList<>();
            this.mInfoParsed = false;
            this.tag = -1;
        }

        public int getUserId() {
            return UserHandle.getUserId(this.id.uid);
        }

        public boolean isInPackageForUser(String str, int i) {
            return getUserId() == i && this.id.componentName.getPackageName().equals(str);
        }

        public boolean hostedByPackageForUser(String str, int i) {
            int size = this.widgets.size();
            for (int i2 = 0; i2 < size; i2++) {
                Widget widget = this.widgets.get(i2);
                if (str.equals(widget.host.id.packageName) && widget.host.getUserId() == i) {
                    return true;
                }
            }
            return false;
        }

        @GuardedBy({"mLock"})
        public AppWidgetProviderInfo getInfoLocked(Context context) {
            if (!this.mInfoParsed) {
                if (!this.zombie) {
                    AppWidgetProviderInfo parseAppWidgetProviderInfo = !TextUtils.isEmpty(this.infoTag) ? AppWidgetServiceImpl.parseAppWidgetProviderInfo(context, this.id, this.info.providerInfo, this.infoTag) : null;
                    if (parseAppWidgetProviderInfo == null) {
                        parseAppWidgetProviderInfo = AppWidgetServiceImpl.parseAppWidgetProviderInfo(context, this.id, this.info.providerInfo, "android.appwidget.provider");
                    }
                    if (parseAppWidgetProviderInfo != null) {
                        this.info = parseAppWidgetProviderInfo;
                    }
                }
                this.mInfoParsed = true;
            }
            return this.info;
        }

        @GuardedBy({"mLock"})
        public AppWidgetProviderInfo getPartialInfoLocked() {
            return this.info;
        }

        @GuardedBy({"mLock"})
        public void setPartialInfoLocked(AppWidgetProviderInfo appWidgetProviderInfo) {
            this.info = appWidgetProviderInfo;
            this.mInfoParsed = false;
        }

        @GuardedBy({"mLock"})
        public void setInfoLocked(AppWidgetProviderInfo appWidgetProviderInfo) {
            this.info = appWidgetProviderInfo;
            this.mInfoParsed = true;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Provider{");
            sb.append(this.id);
            sb.append(this.zombie ? " Z" : "");
            sb.append('}');
            return sb.toString();
        }

        public boolean setMaskedByQuietProfileLocked(boolean z) {
            boolean z2 = this.maskedByQuietProfile;
            this.maskedByQuietProfile = z;
            return z != z2;
        }

        public boolean setMaskedByLockedProfileLocked(boolean z) {
            boolean z2 = this.maskedByLockedProfile;
            this.maskedByLockedProfile = z;
            return z != z2;
        }

        public boolean setMaskedBySuspendedPackageLocked(boolean z) {
            boolean z2 = this.maskedBySuspendedPackage;
            this.maskedBySuspendedPackage = z;
            return z != z2;
        }

        public boolean isMaskedLocked() {
            return this.maskedByQuietProfile || this.maskedByLockedProfile || this.maskedBySuspendedPackage;
        }

        public boolean shouldBePersisted() {
            return (this.widgets.isEmpty() && TextUtils.isEmpty(this.infoTag)) ? false : true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class ProviderId {
        final ComponentName componentName;
        final int uid;

        private ProviderId(int i, ComponentName componentName) {
            this.uid = i;
            this.componentName = componentName;
        }

        public UserHandle getProfile() {
            return UserHandle.getUserHandleForUid(this.uid);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || ProviderId.class != obj.getClass()) {
                return false;
            }
            ProviderId providerId = (ProviderId) obj;
            if (this.uid != providerId.uid) {
                return false;
            }
            ComponentName componentName = this.componentName;
            if (componentName == null) {
                if (providerId.componentName != null) {
                    return false;
                }
            } else if (!componentName.equals(providerId.componentName)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            int i = this.uid * 31;
            ComponentName componentName = this.componentName;
            return i + (componentName != null ? componentName.hashCode() : 0);
        }

        public String toString() {
            return "ProviderId{user:" + UserHandle.getUserId(this.uid) + ", app:" + UserHandle.getAppId(this.uid) + ", cmp:" + this.componentName + '}';
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class Host {
        private static final boolean DEBUG = true;
        private static final String TAG = "AppWidgetServiceHost";
        IAppWidgetHost callbacks;
        HostId id;
        long lastWidgetUpdateSequenceNo;
        int tag;
        ArrayList<Widget> widgets;
        boolean zombie;

        private Host() {
            this.widgets = new ArrayList<>();
            this.tag = -1;
        }

        public int getUserId() {
            return UserHandle.getUserId(this.id.uid);
        }

        public boolean isInPackageForUser(String str, int i) {
            return getUserId() == i && this.id.packageName.equals(str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean hostsPackageForUser(String str, int i) {
            int size = this.widgets.size();
            for (int i2 = 0; i2 < size; i2++) {
                Provider provider = this.widgets.get(i2).provider;
                if (provider != null && provider.getUserId() == i && str.equals(provider.id.componentName.getPackageName())) {
                    return true;
                }
            }
            return false;
        }

        @GuardedBy({"mLock"})
        public void getPendingUpdatesForIdLocked(Context context, int i, LongSparseArray<PendingHostUpdate> longSparseArray) {
            PendingHostUpdate updateAppWidget;
            long j = this.lastWidgetUpdateSequenceNo;
            int size = this.widgets.size();
            for (int i2 = 0; i2 < size; i2++) {
                Widget widget = this.widgets.get(i2);
                if (widget.appWidgetId == i) {
                    for (int size2 = widget.updateSequenceNos.size() - 1; size2 >= 0; size2--) {
                        long valueAt = widget.updateSequenceNos.valueAt(size2);
                        if (valueAt > j) {
                            int keyAt = widget.updateSequenceNos.keyAt(size2);
                            if (keyAt == 0) {
                                updateAppWidget = PendingHostUpdate.updateAppWidget(i, AppWidgetServiceImpl.cloneIfLocalBinder(widget.getEffectiveViewsLocked()));
                            } else if (keyAt == 1) {
                                updateAppWidget = PendingHostUpdate.providerChanged(i, widget.provider.getInfoLocked(context));
                            } else {
                                updateAppWidget = PendingHostUpdate.viewDataChanged(i, keyAt);
                            }
                            longSparseArray.put(valueAt, updateAppWidget);
                        }
                    }
                    return;
                }
            }
            longSparseArray.put(this.lastWidgetUpdateSequenceNo, PendingHostUpdate.appWidgetRemoved(i));
        }

        public SparseArray<String> getWidgetUidsIfBound() {
            SparseArray<String> sparseArray = new SparseArray<>();
            for (int size = this.widgets.size() - 1; size >= 0; size--) {
                Widget widget = this.widgets.get(size);
                Provider provider = widget.provider;
                if (provider == null) {
                    Slog.i(TAG, "getWidgetUids,widget.provider is null,widget:" + widget);
                } else {
                    ProviderId providerId = provider.id;
                    sparseArray.put(providerId.uid, providerId.componentName.getPackageName());
                }
            }
            return sparseArray;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Host{");
            sb.append(this.id);
            sb.append(this.zombie ? " Z" : "");
            sb.append('}');
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class HostId {
        final int hostId;
        final String packageName;
        final int uid;

        public HostId(int i, int i2, String str) {
            this.uid = i;
            this.hostId = i2;
            this.packageName = str;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || HostId.class != obj.getClass()) {
                return false;
            }
            HostId hostId = (HostId) obj;
            if (this.uid != hostId.uid || this.hostId != hostId.hostId) {
                return false;
            }
            String str = this.packageName;
            if (str == null) {
                if (hostId.packageName != null) {
                    return false;
                }
            } else if (!str.equals(hostId.packageName)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            int i = ((this.uid * 31) + this.hostId) * 31;
            String str = this.packageName;
            return i + (str != null ? str.hashCode() : 0);
        }

        public String toString() {
            return "HostId{user:" + UserHandle.getUserId(this.uid) + ", app:" + UserHandle.getAppId(this.uid) + ", hostId:" + this.hostId + ", pkg:" + this.packageName + '}';
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class Widget {
        int appWidgetId;
        Host host;
        RemoteViews maskedViews;
        Bundle options;
        Provider provider;
        int restoredId;
        boolean trackingUpdate;
        SparseLongArray updateSequenceNos;
        RemoteViews views;

        private Widget() {
            this.updateSequenceNos = new SparseLongArray(2);
            this.trackingUpdate = false;
        }

        public String toString() {
            return "AppWidgetId{" + this.appWidgetId + ':' + this.host + ':' + this.provider + '}';
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean replaceWithMaskedViewsLocked(RemoteViews remoteViews) {
            this.maskedViews = remoteViews;
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean clearMaskedViewsLocked() {
            if (this.maskedViews == null) {
                return false;
            }
            this.maskedViews = null;
            return true;
        }

        public RemoteViews getEffectiveViewsLocked() {
            RemoteViews remoteViews = this.maskedViews;
            return remoteViews != null ? remoteViews : this.views;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class LoadedWidgetState {
        final int hostTag;
        final int providerTag;
        final Widget widget;

        public LoadedWidgetState(Widget widget, int i, int i2) {
            this.widget = widget;
            this.hostTag = i;
            this.providerTag = i2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class SaveStateRunnable implements Runnable {
        final int mUserId;

        public SaveStateRunnable(int i) {
            this.mUserId = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (AppWidgetServiceImpl.this.mLock) {
                AppWidgetServiceImpl.this.ensureGroupStateLoadedLocked(this.mUserId, false);
                AppWidgetServiceImpl.this.saveStateLocked(this.mUserId);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class BackupRestoreController {
        private static final boolean DEBUG = true;
        private static final String TAG = "BackupRestoreController";
        private static final int WIDGET_STATE_VERSION = 2;

        @GuardedBy({"mLock"})
        private boolean mHasSystemRestoreFinished;
        private final SparseArray<Set<String>> mPrunedAppsPerUser;
        private final HashMap<Host, ArrayList<RestoreUpdateRecord>> mUpdatesByHost;
        private final HashMap<Provider, ArrayList<RestoreUpdateRecord>> mUpdatesByProvider;

        private BackupRestoreController() {
            this.mPrunedAppsPerUser = new SparseArray<>();
            this.mUpdatesByProvider = new HashMap<>();
            this.mUpdatesByHost = new HashMap<>();
        }

        public List<String> getWidgetParticipants(int i) {
            Slog.i(TAG, "Getting widget participants for user: " + i);
            HashSet hashSet = new HashSet();
            synchronized (AppWidgetServiceImpl.this.mLock) {
                int size = AppWidgetServiceImpl.this.mWidgets.size();
                for (int i2 = 0; i2 < size; i2++) {
                    Widget widget = (Widget) AppWidgetServiceImpl.this.mWidgets.get(i2);
                    if (isProviderAndHostInUser(widget, i)) {
                        hashSet.add(widget.host.id.packageName);
                        Provider provider = widget.provider;
                        if (provider != null) {
                            hashSet.add(provider.id.componentName.getPackageName());
                        }
                    }
                }
            }
            return new ArrayList(hashSet);
        }

        public byte[] getWidgetState(String str, int i) {
            Slog.i(TAG, "Getting widget state for user: " + i);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            synchronized (AppWidgetServiceImpl.this.mLock) {
                if (!packageNeedsWidgetBackupLocked(str, i)) {
                    return null;
                }
                try {
                    TypedXmlSerializer newFastSerializer = Xml.newFastSerializer();
                    newFastSerializer.setOutput(byteArrayOutputStream, StandardCharsets.UTF_8.name());
                    newFastSerializer.startDocument((String) null, Boolean.TRUE);
                    newFastSerializer.startTag((String) null, "ws");
                    newFastSerializer.attributeInt((String) null, "version", 2);
                    newFastSerializer.attribute((String) null, "pkg", str);
                    int size = AppWidgetServiceImpl.this.mProviders.size();
                    int i2 = 0;
                    for (int i3 = 0; i3 < size; i3++) {
                        Provider provider = (Provider) AppWidgetServiceImpl.this.mProviders.get(i3);
                        if (provider.shouldBePersisted() && (provider.isInPackageForUser(str, i) || provider.hostedByPackageForUser(str, i))) {
                            provider.tag = i2;
                            AppWidgetServiceImpl.serializeProvider(newFastSerializer, provider);
                            i2++;
                        }
                    }
                    int size2 = AppWidgetServiceImpl.this.mHosts.size();
                    int i4 = 0;
                    for (int i5 = 0; i5 < size2; i5++) {
                        Host host = (Host) AppWidgetServiceImpl.this.mHosts.get(i5);
                        if (!host.widgets.isEmpty() && (host.isInPackageForUser(str, i) || host.hostsPackageForUser(str, i))) {
                            host.tag = i4;
                            AppWidgetServiceImpl.serializeHost(newFastSerializer, host);
                            i4++;
                        }
                    }
                    int size3 = AppWidgetServiceImpl.this.mWidgets.size();
                    for (int i6 = 0; i6 < size3; i6++) {
                        Widget widget = (Widget) AppWidgetServiceImpl.this.mWidgets.get(i6);
                        Provider provider2 = widget.provider;
                        if (widget.host.isInPackageForUser(str, i) || (provider2 != null && provider2.isInPackageForUser(str, i))) {
                            AppWidgetServiceImpl.serializeAppWidget(newFastSerializer, widget, false);
                        }
                    }
                    newFastSerializer.endTag((String) null, "ws");
                    newFastSerializer.endDocument();
                    return byteArrayOutputStream.toByteArray();
                } catch (IOException unused) {
                    Slog.w(TAG, "Unable to save widget state for " + str);
                    return null;
                }
            }
        }

        public void systemRestoreStarting(int i) {
            Slog.i(TAG, "System restore starting for user: " + i);
            synchronized (AppWidgetServiceImpl.this.mLock) {
                this.mHasSystemRestoreFinished = false;
                getPrunedAppsLocked(i).clear();
                this.mUpdatesByProvider.clear();
                this.mUpdatesByHost.clear();
            }
        }

        public void restoreWidgetState(String str, byte[] bArr, int i) {
            ArrayList arrayList;
            ArrayList arrayList2;
            TypedXmlPullParser newFastPullParser;
            int next;
            AppWidgetProviderInfo appWidgetProviderInfo;
            Slog.i(TAG, "Restoring widget state for user:" + i + " package: " + str);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
            try {
                try {
                    arrayList = new ArrayList();
                    arrayList2 = new ArrayList();
                    newFastPullParser = Xml.newFastPullParser();
                    newFastPullParser.setInput(byteArrayInputStream, StandardCharsets.UTF_8.name());
                } catch (IOException | XmlPullParserException unused) {
                    Slog.w(TAG, "Unable to restore widget state for " + str);
                }
                synchronized (AppWidgetServiceImpl.this.mLock) {
                    do {
                        next = newFastPullParser.next();
                        if (next == 2) {
                            String name = newFastPullParser.getName();
                            byte b = 0;
                            byte b2 = 0;
                            if ("ws".equals(name)) {
                                int attributeInt = newFastPullParser.getAttributeInt((String) null, "version");
                                if (attributeInt > 2) {
                                    Slog.w(TAG, "Unable to process state version " + attributeInt);
                                } else if (!str.equals(newFastPullParser.getAttributeValue((String) null, "pkg"))) {
                                    Slog.w(TAG, "Package mismatch in ws");
                                }
                                return;
                            }
                            int i2 = -1;
                            if ("p".equals(name)) {
                                ComponentName componentName = new ComponentName(newFastPullParser.getAttributeValue((String) null, "pkg"), newFastPullParser.getAttributeValue((String) null, "cl"));
                                Provider findProviderLocked = findProviderLocked(componentName, i);
                                if (findProviderLocked == null) {
                                    AppWidgetProviderInfo appWidgetProviderInfo2 = new AppWidgetProviderInfo();
                                    appWidgetProviderInfo2.provider = componentName;
                                    Provider provider = new Provider();
                                    provider.id = new ProviderId(i2, componentName);
                                    provider.setPartialInfoLocked(appWidgetProviderInfo2);
                                    provider.zombie = true;
                                    AppWidgetServiceImpl.this.mProviders.add(provider);
                                    findProviderLocked = provider;
                                }
                                Slog.i(TAG, "   provider " + findProviderLocked.id);
                                arrayList.add(findProviderLocked);
                            } else if ("h".equals(name)) {
                                String attributeValue = newFastPullParser.getAttributeValue((String) null, "pkg");
                                Host lookupOrAddHostLocked = AppWidgetServiceImpl.this.lookupOrAddHostLocked(new HostId(AppWidgetServiceImpl.this.getUidForPackage(attributeValue, i), newFastPullParser.getAttributeIntHex((String) null, "id"), attributeValue));
                                arrayList2.add(lookupOrAddHostLocked);
                                Slog.i(TAG, "   host[" + arrayList2.size() + "]: {" + lookupOrAddHostLocked.id + "}");
                            } else if ("g".equals(name)) {
                                int attributeIntHex = newFastPullParser.getAttributeIntHex((String) null, "id");
                                Host host = (Host) arrayList2.get(newFastPullParser.getAttributeIntHex((String) null, "h"));
                                int attributeIntHex2 = newFastPullParser.getAttributeIntHex((String) null, "p", -1);
                                Provider provider2 = attributeIntHex2 != -1 ? (Provider) arrayList.get(attributeIntHex2) : null;
                                pruneWidgetStateLocked(host.id.packageName, i);
                                if (provider2 != null) {
                                    pruneWidgetStateLocked(provider2.id.componentName.getPackageName(), i);
                                }
                                Widget findRestoredWidgetLocked = findRestoredWidgetLocked(attributeIntHex, host, provider2);
                                if (findRestoredWidgetLocked == null) {
                                    findRestoredWidgetLocked = new Widget();
                                    findRestoredWidgetLocked.appWidgetId = AppWidgetServiceImpl.this.incrementAndGetAppWidgetIdLocked(i);
                                    findRestoredWidgetLocked.restoredId = attributeIntHex;
                                    findRestoredWidgetLocked.options = AppWidgetServiceImpl.parseWidgetIdOptions(newFastPullParser);
                                    findRestoredWidgetLocked.host = host;
                                    host.widgets.add(findRestoredWidgetLocked);
                                    findRestoredWidgetLocked.provider = provider2;
                                    if (provider2 != null) {
                                        provider2.widgets.add(findRestoredWidgetLocked);
                                    }
                                    Slog.i(TAG, "New restored id " + attributeIntHex + " now " + findRestoredWidgetLocked);
                                    AppWidgetServiceImpl.this.addWidgetLocked(findRestoredWidgetLocked);
                                }
                                Provider provider3 = findRestoredWidgetLocked.provider;
                                if (provider3 != null && provider3.getPartialInfoLocked() != null) {
                                    stashProviderRestoreUpdateLocked(findRestoredWidgetLocked.provider, attributeIntHex, findRestoredWidgetLocked.appWidgetId);
                                } else {
                                    Slog.w(TAG, "Missing provider for restored widget " + findRestoredWidgetLocked);
                                }
                                stashHostRestoreUpdateLocked(findRestoredWidgetLocked.host, attributeIntHex, findRestoredWidgetLocked.appWidgetId);
                                Provider provider4 = findRestoredWidgetLocked.provider;
                                if (provider4 != null && (appWidgetProviderInfo = provider4.info) != null && appWidgetProviderInfo.provider != null && provider4.id != null) {
                                    IAppWidgetServiceImplExt iAppWidgetServiceImplExt = AppWidgetServiceImpl.this.mAppWidgetServiceExt;
                                    Provider provider5 = findRestoredWidgetLocked.provider;
                                    iAppWidgetServiceImplExt.hookUpdateWidgetSate(provider5.id.uid, provider5.info.provider.getPackageName(), true);
                                }
                                Slog.i(TAG, "   instance: " + attributeIntHex + " -> " + findRestoredWidgetLocked.appWidgetId + " :: p=" + findRestoredWidgetLocked.provider);
                            }
                        }
                    } while (next != 1);
                }
            } finally {
                AppWidgetServiceImpl.this.saveGroupStateAsync(i);
            }
        }

        public void systemRestoreFinished(int i) {
            Slog.i(TAG, "systemRestoreFinished for " + i);
            synchronized (AppWidgetServiceImpl.this.mLock) {
                this.mHasSystemRestoreFinished = true;
                maybeSendWidgetRestoreBroadcastsLocked(i);
            }
        }

        public void widgetComponentsChanged(int i) {
            synchronized (AppWidgetServiceImpl.this.mLock) {
                if (this.mHasSystemRestoreFinished) {
                    maybeSendWidgetRestoreBroadcastsLocked(i);
                }
            }
        }

        @GuardedBy({"mLock"})
        private void maybeSendWidgetRestoreBroadcastsLocked(int i) {
            ArrayList<RestoreUpdateRecord> arrayList;
            Slog.i(TAG, "maybeSendWidgetRestoreBroadcasts for " + i);
            UserHandle userHandle = new UserHandle(i);
            Iterator<Map.Entry<Provider, ArrayList<RestoreUpdateRecord>>> it = this.mUpdatesByProvider.entrySet().iterator();
            while (true) {
                boolean z = true;
                if (!it.hasNext()) {
                    break;
                }
                Map.Entry<Provider, ArrayList<RestoreUpdateRecord>> next = it.next();
                Provider key = next.getKey();
                if (!key.zombie) {
                    ArrayList<RestoreUpdateRecord> value = next.getValue();
                    int countPendingUpdates = countPendingUpdates(value);
                    Slog.i(TAG, "Provider " + key + " pending: " + countPendingUpdates);
                    if (countPendingUpdates > 0) {
                        int[] iArr = new int[countPendingUpdates];
                        int[] iArr2 = new int[countPendingUpdates];
                        int size = value.size();
                        int i2 = 0;
                        int i3 = 0;
                        while (i3 < size) {
                            RestoreUpdateRecord restoreUpdateRecord = value.get(i3);
                            if (!restoreUpdateRecord.notified) {
                                restoreUpdateRecord.notified = z;
                                iArr[i2] = restoreUpdateRecord.oldId;
                                iArr2[i2] = restoreUpdateRecord.newId;
                                i2++;
                                Slog.i(TAG, "   " + restoreUpdateRecord.oldId + " => " + restoreUpdateRecord.newId);
                            }
                            i3++;
                            z = true;
                        }
                        sendWidgetRestoreBroadcastLocked("android.appwidget.action.APPWIDGET_RESTORED", key, null, iArr, iArr2, userHandle);
                    }
                }
            }
            for (Map.Entry<Host, ArrayList<RestoreUpdateRecord>> entry : this.mUpdatesByHost.entrySet()) {
                Host key2 = entry.getKey();
                if (key2.id.uid != -1) {
                    ArrayList<RestoreUpdateRecord> value2 = entry.getValue();
                    int countPendingUpdates2 = countPendingUpdates(value2);
                    Slog.i(TAG, "Host " + key2 + " pending: " + countPendingUpdates2);
                    if (countPendingUpdates2 > 0) {
                        int[] iArr3 = new int[countPendingUpdates2];
                        int[] iArr4 = new int[countPendingUpdates2];
                        int size2 = value2.size();
                        int i4 = 0;
                        int i5 = 0;
                        while (i4 < size2) {
                            RestoreUpdateRecord restoreUpdateRecord2 = value2.get(i4);
                            if (restoreUpdateRecord2.notified) {
                                arrayList = value2;
                            } else {
                                restoreUpdateRecord2.notified = true;
                                iArr3[i5] = restoreUpdateRecord2.oldId;
                                iArr4[i5] = restoreUpdateRecord2.newId;
                                i5++;
                                StringBuilder sb = new StringBuilder();
                                sb.append("   ");
                                arrayList = value2;
                                sb.append(restoreUpdateRecord2.oldId);
                                sb.append(" => ");
                                sb.append(restoreUpdateRecord2.newId);
                                Slog.i(TAG, sb.toString());
                            }
                            i4++;
                            value2 = arrayList;
                        }
                        sendWidgetRestoreBroadcastLocked("android.appwidget.action.APPWIDGET_HOST_RESTORED", null, key2, iArr3, iArr4, userHandle);
                    }
                }
            }
        }

        private Provider findProviderLocked(ComponentName componentName, int i) {
            int size = AppWidgetServiceImpl.this.mProviders.size();
            for (int i2 = 0; i2 < size; i2++) {
                Provider provider = (Provider) AppWidgetServiceImpl.this.mProviders.get(i2);
                if (provider.getUserId() == i && provider.id.componentName.equals(componentName)) {
                    return provider;
                }
            }
            return null;
        }

        private Widget findRestoredWidgetLocked(int i, Host host, Provider provider) {
            Slog.i(TAG, "Find restored widget: id=" + i + " host=" + host + " provider=" + provider);
            if (provider != null && host != null) {
                int size = AppWidgetServiceImpl.this.mWidgets.size();
                for (int i2 = 0; i2 < size; i2++) {
                    Widget widget = (Widget) AppWidgetServiceImpl.this.mWidgets.get(i2);
                    if (widget.restoredId == i && widget.host.id.equals(host.id) && widget.provider.id.equals(provider.id)) {
                        Slog.i(TAG, "   Found at " + i2 + " : " + widget);
                        return widget;
                    }
                }
            }
            return null;
        }

        private boolean packageNeedsWidgetBackupLocked(String str, int i) {
            int size = AppWidgetServiceImpl.this.mWidgets.size();
            for (int i2 = 0; i2 < size; i2++) {
                Widget widget = (Widget) AppWidgetServiceImpl.this.mWidgets.get(i2);
                if (isProviderAndHostInUser(widget, i)) {
                    if (widget.host.isInPackageForUser(str, i)) {
                        return true;
                    }
                    Provider provider = widget.provider;
                    if (provider != null && provider.isInPackageForUser(str, i)) {
                        return true;
                    }
                }
            }
            return false;
        }

        private void stashProviderRestoreUpdateLocked(Provider provider, int i, int i2) {
            ArrayList<RestoreUpdateRecord> arrayList = this.mUpdatesByProvider.get(provider);
            if (arrayList == null) {
                arrayList = new ArrayList<>();
                this.mUpdatesByProvider.put(provider, arrayList);
            } else if (alreadyStashed(arrayList, i, i2)) {
                Slog.i(TAG, "ID remap " + i + " -> " + i2 + " already stashed for " + provider);
                return;
            }
            arrayList.add(new RestoreUpdateRecord(i, i2));
        }

        private boolean alreadyStashed(ArrayList<RestoreUpdateRecord> arrayList, int i, int i2) {
            int size = arrayList.size();
            for (int i3 = 0; i3 < size; i3++) {
                RestoreUpdateRecord restoreUpdateRecord = arrayList.get(i3);
                if (restoreUpdateRecord.oldId == i && restoreUpdateRecord.newId == i2) {
                    return true;
                }
            }
            return false;
        }

        private void stashHostRestoreUpdateLocked(Host host, int i, int i2) {
            ArrayList<RestoreUpdateRecord> arrayList = this.mUpdatesByHost.get(host);
            if (arrayList == null) {
                arrayList = new ArrayList<>();
                this.mUpdatesByHost.put(host, arrayList);
            } else if (alreadyStashed(arrayList, i, i2)) {
                Slog.i(TAG, "ID remap " + i + " -> " + i2 + " already stashed for " + host);
                return;
            }
            arrayList.add(new RestoreUpdateRecord(i, i2));
        }

        private void sendWidgetRestoreBroadcastLocked(String str, Provider provider, Host host, int[] iArr, int[] iArr2, UserHandle userHandle) {
            Intent intent = new Intent(str);
            intent.putExtra("appWidgetOldIds", iArr);
            intent.putExtra("appWidgetIds", iArr2);
            if (provider != null) {
                intent.setComponent(provider.id.componentName);
                AppWidgetServiceImpl.this.sendBroadcastAsUser(intent, userHandle, true);
            }
            if (host != null) {
                intent.setComponent(null);
                intent.setPackage(host.id.packageName);
                intent.putExtra("hostId", host.id.hostId);
                AppWidgetServiceImpl.this.sendBroadcastAsUser(intent, userHandle, true);
            }
        }

        @GuardedBy({"mLock"})
        private void pruneWidgetStateLocked(String str, int i) {
            Set<String> prunedAppsLocked = getPrunedAppsLocked(i);
            if (!prunedAppsLocked.contains(str)) {
                Slog.i(TAG, "pruning widget state for restoring package " + str);
                for (int size = AppWidgetServiceImpl.this.mWidgets.size() + (-1); size >= 0; size--) {
                    Widget widget = (Widget) AppWidgetServiceImpl.this.mWidgets.get(size);
                    Host host = widget.host;
                    Provider provider = widget.provider;
                    if (host.hostsPackageForUser(str, i) || (provider != null && provider.isInPackageForUser(str, i))) {
                        host.widgets.remove(widget);
                        provider.widgets.remove(widget);
                        AppWidgetServiceImpl.this.decrementAppWidgetServiceRefCount(widget);
                        AppWidgetServiceImpl.this.removeWidgetLocked(widget);
                    }
                }
                prunedAppsLocked.add(str);
                return;
            }
            Slog.i(TAG, "already pruned " + str + ", continuing normally");
        }

        @GuardedBy({"mLock"})
        private Set<String> getPrunedAppsLocked(int i) {
            if (!this.mPrunedAppsPerUser.contains(i)) {
                this.mPrunedAppsPerUser.set(i, new ArraySet());
            }
            return this.mPrunedAppsPerUser.get(i);
        }

        private boolean isProviderAndHostInUser(Widget widget, int i) {
            Provider provider;
            return widget.host.getUserId() == i && ((provider = widget.provider) == null || provider.getUserId() == i);
        }

        private int countPendingUpdates(ArrayList<RestoreUpdateRecord> arrayList) {
            int size = arrayList.size();
            int i = 0;
            for (int i2 = 0; i2 < size; i2++) {
                if (!arrayList.get(i2).notified) {
                    i++;
                }
            }
            return i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        public class RestoreUpdateRecord {
            public int newId;
            public boolean notified = false;
            public int oldId;

            public RestoreUpdateRecord(int i, int i2) {
                this.oldId = i;
                this.newId = i2;
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class AppWidgetManagerLocal extends AppWidgetManagerInternal {
        private AppWidgetManagerLocal() {
        }

        public ArraySet<String> getHostedWidgetPackages(int i) {
            ArraySet<String> arraySet;
            synchronized (AppWidgetServiceImpl.this.mLock) {
                int size = AppWidgetServiceImpl.this.mWidgets.size();
                arraySet = null;
                for (int i2 = 0; i2 < size; i2++) {
                    Widget widget = (Widget) AppWidgetServiceImpl.this.mWidgets.get(i2);
                    if (widget.host.id.uid == i && widget.provider != null) {
                        if (arraySet == null) {
                            arraySet = new ArraySet<>();
                        }
                        arraySet.add(widget.provider.id.componentName.getPackageName());
                    }
                }
            }
            return arraySet;
        }

        public void unlockUser(int i) {
            AppWidgetServiceImpl.this.handleUserUnlocked(i);
        }

        public void applyResourceOverlaysToWidgets(Set<String> set, int i, boolean z) {
            synchronized (AppWidgetServiceImpl.this.mLock) {
                AppWidgetServiceImpl.this.applyResourceOverlaysToWidgetsLocked(new HashSet(set), i, z);
            }
        }
    }
}
