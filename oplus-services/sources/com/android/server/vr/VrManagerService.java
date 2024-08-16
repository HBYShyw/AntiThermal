package com.android.server.vr;

import android.app.ActivityManager;
import android.app.ActivityManagerInternal;
import android.app.AppOpsManager;
import android.app.INotificationManager;
import android.app.NotificationManager;
import android.app.Vr2dDisplayProperties;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.os.PackageTagsList;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.service.vr.IPersistentVrStateCallbacks;
import android.service.vr.IVrListener;
import android.service.vr.IVrManager;
import android.service.vr.IVrStateCallbacks;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.util.DumpUtils;
import com.android.server.FgThread;
import com.android.server.LocalServices;
import com.android.server.SystemConfig;
import com.android.server.SystemService;
import com.android.server.timezonedetector.ServiceConfigAccessor;
import com.android.server.utils.ManagedApplicationService;
import com.android.server.vr.EnabledComponentsObserver;
import com.android.server.wm.ActivityTaskManagerInternal;
import com.android.server.wm.WindowManagerInternal;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class VrManagerService extends SystemService implements EnabledComponentsObserver.EnabledComponentChangeListener, ActivityTaskManagerInternal.ScreenObserver {
    static final boolean DBG = false;
    private static final int EVENT_LOG_SIZE = 64;
    private static final int FLAG_ALL = 7;
    private static final int FLAG_AWAKE = 1;
    private static final int FLAG_KEYGUARD_UNLOCKED = 4;
    private static final int FLAG_NONE = 0;
    private static final int FLAG_SCREEN_ON = 2;
    private static final int INVALID_APPOPS_MODE = -1;
    private static final int MSG_PENDING_VR_STATE_CHANGE = 1;
    private static final int MSG_PERSISTENT_VR_MODE_STATE_CHANGE = 2;
    private static final int MSG_VR_STATE_CHANGE = 0;
    private static final int PENDING_STATE_DELAY_MS = 300;
    public static final String TAG = "VrManagerService";
    private static final ManagedApplicationService.BinderChecker sBinderChecker = new ManagedApplicationService.BinderChecker() { // from class: com.android.server.vr.VrManagerService.3
        @Override // com.android.server.utils.ManagedApplicationService.BinderChecker
        public IInterface asInterface(IBinder iBinder) {
            return IVrListener.Stub.asInterface(iBinder);
        }

        @Override // com.android.server.utils.ManagedApplicationService.BinderChecker
        public boolean checkType(IInterface iInterface) {
            return iInterface instanceof IVrListener;
        }
    };
    private boolean mBootsToVr;
    private EnabledComponentsObserver mComponentObserver;
    private Context mContext;
    private ManagedApplicationService mCurrentVrCompositorService;
    private ComponentName mCurrentVrModeComponent;
    private int mCurrentVrModeUser;
    private ManagedApplicationService mCurrentVrService;
    private ComponentName mDefaultVrService;
    private final ManagedApplicationService.EventCallback mEventCallback;
    private boolean mGuard;
    private final Handler mHandler;
    private final Object mLock;
    private boolean mLogLimitHit;
    private final ArrayDeque<ManagedApplicationService.LogFormattable> mLoggingDeque;
    private final NotificationAccessManager mNotifAccessManager;
    private INotificationManager mNotificationManager;
    private final IBinder mOverlayToken;
    private VrState mPendingState;
    private boolean mPersistentVrModeEnabled;
    private final RemoteCallbackList<IPersistentVrStateCallbacks> mPersistentVrStateRemoteCallbacks;
    private int mPreviousCoarseLocationMode;
    private int mPreviousManageOverlayMode;
    private boolean mRunning2dInVr;
    private boolean mStandby;
    private int mSystemSleepFlags;
    private boolean mUseStandbyToExitVrMode;
    private boolean mUserUnlocked;
    private Vr2dDisplay mVr2dDisplay;
    private int mVrAppProcessId;
    private final IVrManager mVrManager;
    private boolean mVrModeAllowed;
    private boolean mVrModeEnabled;
    private final RemoteCallbackList<IVrStateCallbacks> mVrStateRemoteCallbacks;
    private boolean mWasDefaultGranted;

    private static native void initializeNative();

    private static native void setVrModeNative(boolean z);

    private void updateVrModeAllowedLocked() {
        ManagedApplicationService managedApplicationService;
        boolean z = this.mBootsToVr;
        boolean z2 = (this.mSystemSleepFlags == 7 || (z && this.mUseStandbyToExitVrMode)) && this.mUserUnlocked && !(this.mStandby && this.mUseStandbyToExitVrMode);
        if (this.mVrModeAllowed != z2) {
            this.mVrModeAllowed = z2;
            if (z2) {
                if (z) {
                    setPersistentVrModeEnabled(true);
                }
                if (!this.mBootsToVr || this.mVrModeEnabled) {
                    return;
                }
                setVrMode(true, this.mDefaultVrService, 0, -1, null);
                return;
            }
            setPersistentModeAndNotifyListenersLocked(false);
            boolean z3 = this.mVrModeEnabled;
            this.mPendingState = (!z3 || (managedApplicationService = this.mCurrentVrService) == null) ? null : new VrState(z3, this.mRunning2dInVr, managedApplicationService.getComponent(), this.mCurrentVrService.getUserId(), this.mVrAppProcessId, this.mCurrentVrModeComponent);
            updateCurrentVrServiceLocked(false, false, null, 0, -1, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setScreenOn(boolean z) {
        setSystemState(2, z);
    }

    public void onAwakeStateChanged(boolean z) {
        setSystemState(1, z);
    }

    public void onKeyguardStateChanged(boolean z) {
        setSystemState(4, !z);
    }

    private void setSystemState(int i, boolean z) {
        synchronized (this.mLock) {
            int i2 = this.mSystemSleepFlags;
            if (z) {
                this.mSystemSleepFlags = i | i2;
            } else {
                this.mSystemSleepFlags = (~i) & i2;
            }
            if (i2 != this.mSystemSleepFlags) {
                updateVrModeAllowedLocked();
            }
        }
    }

    private String getStateAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append((this.mSystemSleepFlags & 1) != 0 ? "awake, " : "");
        sb.append((this.mSystemSleepFlags & 2) != 0 ? "screen_on, " : "");
        sb.append((this.mSystemSleepFlags & 4) != 0 ? "keyguard_off" : "");
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setUserUnlocked() {
        synchronized (this.mLock) {
            this.mUserUnlocked = true;
            updateVrModeAllowedLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setStandbyEnabled(boolean z) {
        synchronized (this.mLock) {
            if (!this.mBootsToVr) {
                Slog.e(TAG, "Attempting to set standby mode on a non-standalone device");
            } else {
                this.mStandby = z;
                updateVrModeAllowedLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class SettingEvent implements ManagedApplicationService.LogFormattable {
        public final long timestamp = System.currentTimeMillis();
        public final String what;

        SettingEvent(String str) {
            this.what = str;
        }

        @Override // com.android.server.utils.ManagedApplicationService.LogFormattable
        public String toLogString(SimpleDateFormat simpleDateFormat) {
            return simpleDateFormat.format(new Date(this.timestamp)) + "   " + this.what;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class VrState implements ManagedApplicationService.LogFormattable {
        final ComponentName callingPackage;
        final boolean defaultPermissionsGranted;
        final boolean enabled;
        final int processId;
        final boolean running2dInVr;
        final ComponentName targetPackageName;
        final long timestamp;
        final int userId;

        VrState(boolean z, boolean z2, ComponentName componentName, int i, int i2, ComponentName componentName2) {
            this.enabled = z;
            this.running2dInVr = z2;
            this.userId = i;
            this.processId = i2;
            this.targetPackageName = componentName;
            this.callingPackage = componentName2;
            this.defaultPermissionsGranted = false;
            this.timestamp = System.currentTimeMillis();
        }

        VrState(boolean z, boolean z2, ComponentName componentName, int i, int i2, ComponentName componentName2, boolean z3) {
            this.enabled = z;
            this.running2dInVr = z2;
            this.userId = i;
            this.processId = i2;
            this.targetPackageName = componentName;
            this.callingPackage = componentName2;
            this.defaultPermissionsGranted = z3;
            this.timestamp = System.currentTimeMillis();
        }

        @Override // com.android.server.utils.ManagedApplicationService.LogFormattable
        public String toLogString(SimpleDateFormat simpleDateFormat) {
            StringBuilder sb = new StringBuilder(simpleDateFormat.format(new Date(this.timestamp)));
            sb.append("  ");
            sb.append("State changed to:");
            sb.append("  ");
            sb.append(this.enabled ? "ENABLED" : "DISABLED");
            sb.append("\n");
            if (this.enabled) {
                sb.append("  ");
                sb.append("User=");
                sb.append(this.userId);
                sb.append("\n");
                sb.append("  ");
                sb.append("Current VR Activity=");
                ComponentName componentName = this.callingPackage;
                sb.append(componentName == null ? "None" : componentName.flattenToString());
                sb.append("\n");
                sb.append("  ");
                sb.append("Bound VrListenerService=");
                ComponentName componentName2 = this.targetPackageName;
                sb.append(componentName2 != null ? componentName2.flattenToString() : "None");
                sb.append("\n");
                if (this.defaultPermissionsGranted) {
                    sb.append("  ");
                    sb.append("Default permissions granted to the bound VrListenerService.");
                    sb.append("\n");
                }
            }
            return sb.toString();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class NotificationAccessManager {
        private final SparseArray<ArraySet<String>> mAllowedPackages;
        private final ArrayMap<String, Integer> mNotificationAccessPackageToUserId;

        private NotificationAccessManager() {
            this.mAllowedPackages = new SparseArray<>();
            this.mNotificationAccessPackageToUserId = new ArrayMap<>();
        }

        public void update(Collection<String> collection) {
            int currentUser = ActivityManager.getCurrentUser();
            ArraySet<String> arraySet = this.mAllowedPackages.get(currentUser);
            if (arraySet == null) {
                arraySet = new ArraySet<>();
            }
            for (int size = this.mNotificationAccessPackageToUserId.size() - 1; size >= 0; size--) {
                int intValue = this.mNotificationAccessPackageToUserId.valueAt(size).intValue();
                if (intValue != currentUser) {
                    String keyAt = this.mNotificationAccessPackageToUserId.keyAt(size);
                    VrManagerService.this.revokeNotificationListenerAccess(keyAt, intValue);
                    VrManagerService.this.revokeNotificationPolicyAccess(keyAt);
                    VrManagerService.this.revokeCoarseLocationPermissionIfNeeded(keyAt, intValue);
                    this.mNotificationAccessPackageToUserId.removeAt(size);
                }
            }
            Iterator<String> it = arraySet.iterator();
            while (it.hasNext()) {
                String next = it.next();
                if (!collection.contains(next)) {
                    VrManagerService.this.revokeNotificationListenerAccess(next, currentUser);
                    VrManagerService.this.revokeNotificationPolicyAccess(next);
                    VrManagerService.this.revokeCoarseLocationPermissionIfNeeded(next, currentUser);
                    this.mNotificationAccessPackageToUserId.remove(next);
                }
            }
            for (String str : collection) {
                if (!arraySet.contains(str)) {
                    VrManagerService.this.grantNotificationPolicyAccess(str);
                    VrManagerService.this.grantNotificationListenerAccess(str, currentUser);
                    VrManagerService.this.grantCoarseLocationPermissionIfNeeded(str, currentUser);
                    this.mNotificationAccessPackageToUserId.put(str, Integer.valueOf(currentUser));
                }
            }
            arraySet.clear();
            arraySet.addAll(collection);
            this.mAllowedPackages.put(currentUser, arraySet);
        }
    }

    @Override // com.android.server.vr.EnabledComponentsObserver.EnabledComponentChangeListener
    public void onEnabledComponentChanged() {
        synchronized (this.mLock) {
            ArraySet<ComponentName> enabled = this.mComponentObserver.getEnabled(ActivityManager.getCurrentUser());
            ArraySet arraySet = new ArraySet();
            Iterator<ComponentName> it = enabled.iterator();
            while (it.hasNext()) {
                ComponentName next = it.next();
                if (isDefaultAllowed(next.getPackageName())) {
                    arraySet.add(next.getPackageName());
                }
            }
            this.mNotifAccessManager.update(arraySet);
            if (this.mVrModeAllowed) {
                consumeAndApplyPendingStateLocked(false);
                ManagedApplicationService managedApplicationService = this.mCurrentVrService;
                if (managedApplicationService == null) {
                    return;
                }
                updateCurrentVrServiceLocked(this.mVrModeEnabled, this.mRunning2dInVr, managedApplicationService.getComponent(), this.mCurrentVrService.getUserId(), this.mVrAppProcessId, this.mCurrentVrModeComponent);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceCallerPermissionAnyOf(String... strArr) {
        for (String str : strArr) {
            if (this.mContext.checkCallingOrSelfPermission(str) == 0) {
                return;
            }
        }
        throw new SecurityException("Caller does not hold at least one of the permissions: " + Arrays.toString(strArr));
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class LocalService extends VrManagerInternal {
        private LocalService() {
        }

        @Override // com.android.server.vr.VrManagerInternal
        public void setVrMode(boolean z, ComponentName componentName, int i, int i2, ComponentName componentName2) {
            VrManagerService.this.setVrMode(z, componentName, i, i2, componentName2);
        }

        @Override // com.android.server.vr.VrManagerInternal
        public void onScreenStateChanged(boolean z) {
            VrManagerService.this.setScreenOn(z);
        }

        @Override // com.android.server.vr.VrManagerInternal
        public boolean isCurrentVrListener(String str, int i) {
            return VrManagerService.this.isCurrentVrListener(str, i);
        }

        @Override // com.android.server.vr.VrManagerInternal
        public int hasVrPackage(ComponentName componentName, int i) {
            return VrManagerService.this.hasVrPackage(componentName, i);
        }

        @Override // com.android.server.vr.VrManagerInternal
        public void setPersistentVrModeEnabled(boolean z) {
            VrManagerService.this.setPersistentVrModeEnabled(z);
        }

        @Override // com.android.server.vr.VrManagerInternal
        public void setVr2dDisplayProperties(Vr2dDisplayProperties vr2dDisplayProperties) {
            VrManagerService.this.setVr2dDisplayProperties(vr2dDisplayProperties);
        }

        @Override // com.android.server.vr.VrManagerInternal
        public int getVr2dDisplayId() {
            return VrManagerService.this.getVr2dDisplayId();
        }

        @Override // com.android.server.vr.VrManagerInternal
        public void addPersistentVrModeStateListener(IPersistentVrStateCallbacks iPersistentVrStateCallbacks) {
            VrManagerService.this.addPersistentStateCallback(iPersistentVrStateCallbacks);
        }
    }

    public VrManagerService(Context context) {
        super(context);
        this.mLock = new Object();
        this.mOverlayToken = new Binder();
        this.mVrStateRemoteCallbacks = new RemoteCallbackList<>();
        this.mPersistentVrStateRemoteCallbacks = new RemoteCallbackList<>();
        this.mPreviousCoarseLocationMode = -1;
        this.mPreviousManageOverlayMode = -1;
        this.mLoggingDeque = new ArrayDeque<>(64);
        this.mNotifAccessManager = new NotificationAccessManager();
        this.mSystemSleepFlags = 5;
        this.mEventCallback = new ManagedApplicationService.EventCallback() { // from class: com.android.server.vr.VrManagerService.1
            @Override // com.android.server.utils.ManagedApplicationService.EventCallback
            public void onServiceEvent(ManagedApplicationService.LogEvent logEvent) {
                ComponentName component;
                int i;
                VrManagerService.this.logEvent(logEvent);
                synchronized (VrManagerService.this.mLock) {
                    component = VrManagerService.this.mCurrentVrService == null ? null : VrManagerService.this.mCurrentVrService.getComponent();
                    if (component != null && component.equals(logEvent.component) && ((i = logEvent.event) == 2 || i == 3)) {
                        VrManagerService.this.callFocusedActivityChangedLocked();
                    }
                }
                if (VrManagerService.this.mBootsToVr || logEvent.event != 4) {
                    return;
                }
                if (component == null || component.equals(logEvent.component)) {
                    Slog.e(VrManagerService.TAG, "VrListenerSevice has died permanently, leaving system VR mode.");
                    VrManagerService.this.setPersistentVrModeEnabled(false);
                }
            }
        };
        this.mHandler = new Handler() { // from class: com.android.server.vr.VrManagerService.2
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                boolean z;
                int i = message.what;
                if (i == 0) {
                    z = message.arg1 == 1;
                    int beginBroadcast = VrManagerService.this.mVrStateRemoteCallbacks.beginBroadcast();
                    while (beginBroadcast > 0) {
                        beginBroadcast--;
                        try {
                            VrManagerService.this.mVrStateRemoteCallbacks.getBroadcastItem(beginBroadcast).onVrStateChanged(z);
                        } catch (RemoteException unused) {
                        }
                    }
                    VrManagerService.this.mVrStateRemoteCallbacks.finishBroadcast();
                    return;
                }
                if (i == 1) {
                    synchronized (VrManagerService.this.mLock) {
                        if (VrManagerService.this.mVrModeAllowed) {
                            VrManagerService.this.consumeAndApplyPendingStateLocked();
                        }
                    }
                } else {
                    if (i == 2) {
                        z = message.arg1 == 1;
                        int beginBroadcast2 = VrManagerService.this.mPersistentVrStateRemoteCallbacks.beginBroadcast();
                        while (beginBroadcast2 > 0) {
                            beginBroadcast2--;
                            try {
                                VrManagerService.this.mPersistentVrStateRemoteCallbacks.getBroadcastItem(beginBroadcast2).onPersistentVrStateChanged(z);
                            } catch (RemoteException unused2) {
                            }
                        }
                        VrManagerService.this.mPersistentVrStateRemoteCallbacks.finishBroadcast();
                        return;
                    }
                    throw new IllegalStateException("Unknown message type: " + message.what);
                }
            }
        };
        this.mVrManager = new IVrManager.Stub() { // from class: com.android.server.vr.VrManagerService.4
            public void registerListener(IVrStateCallbacks iVrStateCallbacks) {
                VrManagerService.this.enforceCallerPermissionAnyOf("android.permission.ACCESS_VR_MANAGER", "android.permission.ACCESS_VR_STATE");
                if (iVrStateCallbacks == null) {
                    throw new IllegalArgumentException("Callback binder object is null.");
                }
                VrManagerService.this.addStateCallback(iVrStateCallbacks);
            }

            public void unregisterListener(IVrStateCallbacks iVrStateCallbacks) {
                VrManagerService.this.enforceCallerPermissionAnyOf("android.permission.ACCESS_VR_MANAGER", "android.permission.ACCESS_VR_STATE");
                if (iVrStateCallbacks == null) {
                    throw new IllegalArgumentException("Callback binder object is null.");
                }
                VrManagerService.this.removeStateCallback(iVrStateCallbacks);
            }

            public void registerPersistentVrStateListener(IPersistentVrStateCallbacks iPersistentVrStateCallbacks) {
                VrManagerService.this.enforceCallerPermissionAnyOf("android.permission.ACCESS_VR_MANAGER", "android.permission.ACCESS_VR_STATE");
                if (iPersistentVrStateCallbacks == null) {
                    throw new IllegalArgumentException("Callback binder object is null.");
                }
                VrManagerService.this.addPersistentStateCallback(iPersistentVrStateCallbacks);
            }

            public void unregisterPersistentVrStateListener(IPersistentVrStateCallbacks iPersistentVrStateCallbacks) {
                VrManagerService.this.enforceCallerPermissionAnyOf("android.permission.ACCESS_VR_MANAGER", "android.permission.ACCESS_VR_STATE");
                if (iPersistentVrStateCallbacks == null) {
                    throw new IllegalArgumentException("Callback binder object is null.");
                }
                VrManagerService.this.removePersistentStateCallback(iPersistentVrStateCallbacks);
            }

            public boolean getVrModeState() {
                VrManagerService.this.enforceCallerPermissionAnyOf("android.permission.ACCESS_VR_MANAGER", "android.permission.ACCESS_VR_STATE");
                return VrManagerService.this.getVrMode();
            }

            public boolean getPersistentVrModeEnabled() {
                VrManagerService.this.enforceCallerPermissionAnyOf("android.permission.ACCESS_VR_MANAGER", "android.permission.ACCESS_VR_STATE");
                return VrManagerService.this.getPersistentVrMode();
            }

            public void setPersistentVrModeEnabled(boolean z) {
                VrManagerService.this.enforceCallerPermissionAnyOf("android.permission.RESTRICTED_VR_ACCESS");
                VrManagerService.this.setPersistentVrModeEnabled(z);
            }

            public void setVr2dDisplayProperties(Vr2dDisplayProperties vr2dDisplayProperties) {
                VrManagerService.this.enforceCallerPermissionAnyOf("android.permission.RESTRICTED_VR_ACCESS");
                VrManagerService.this.setVr2dDisplayProperties(vr2dDisplayProperties);
            }

            public int getVr2dDisplayId() {
                return VrManagerService.this.getVr2dDisplayId();
            }

            public void setAndBindCompositor(String str) {
                VrManagerService.this.enforceCallerPermissionAnyOf("android.permission.RESTRICTED_VR_ACCESS");
                VrManagerService.this.setAndBindCompositor(str == null ? null : ComponentName.unflattenFromString(str));
            }

            public void setStandbyEnabled(boolean z) {
                VrManagerService.this.enforceCallerPermissionAnyOf("android.permission.ACCESS_VR_MANAGER");
                VrManagerService.this.setStandbyEnabled(z);
            }

            protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
                if (DumpUtils.checkDumpPermission(VrManagerService.this.mContext, VrManagerService.TAG, printWriter)) {
                    printWriter.println("********* Dump of VrManagerService *********");
                    StringBuilder sb = new StringBuilder();
                    sb.append("VR mode is currently: ");
                    sb.append(VrManagerService.this.mVrModeAllowed ? "allowed" : "disallowed");
                    printWriter.println(sb.toString());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Persistent VR mode is currently: ");
                    sb2.append(VrManagerService.this.mPersistentVrModeEnabled ? ServiceConfigAccessor.PROVIDER_MODE_ENABLED : ServiceConfigAccessor.PROVIDER_MODE_DISABLED);
                    printWriter.println(sb2.toString());
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Currently bound VR listener service: ");
                    sb3.append(VrManagerService.this.mCurrentVrService == null ? "None" : VrManagerService.this.mCurrentVrService.getComponent().flattenToString());
                    printWriter.println(sb3.toString());
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Currently bound VR compositor service: ");
                    sb4.append(VrManagerService.this.mCurrentVrCompositorService == null ? "None" : VrManagerService.this.mCurrentVrCompositorService.getComponent().flattenToString());
                    printWriter.println(sb4.toString());
                    printWriter.println("Previous state transitions:\n");
                    VrManagerService.this.dumpStateTransitions(printWriter);
                    printWriter.println("\n\nRemote Callbacks:");
                    int beginBroadcast = VrManagerService.this.mVrStateRemoteCallbacks.beginBroadcast();
                    while (true) {
                        int i = beginBroadcast - 1;
                        if (beginBroadcast <= 0) {
                            break;
                        }
                        printWriter.print("  ");
                        printWriter.print(VrManagerService.this.mVrStateRemoteCallbacks.getBroadcastItem(i));
                        if (i > 0) {
                            printWriter.println(",");
                        }
                        beginBroadcast = i;
                    }
                    VrManagerService.this.mVrStateRemoteCallbacks.finishBroadcast();
                    printWriter.println("\n\nPersistent Vr State Remote Callbacks:");
                    int beginBroadcast2 = VrManagerService.this.mPersistentVrStateRemoteCallbacks.beginBroadcast();
                    while (true) {
                        int i2 = beginBroadcast2 - 1;
                        if (beginBroadcast2 <= 0) {
                            break;
                        }
                        printWriter.print("  ");
                        printWriter.print(VrManagerService.this.mPersistentVrStateRemoteCallbacks.getBroadcastItem(i2));
                        if (i2 > 0) {
                            printWriter.println(",");
                        }
                        beginBroadcast2 = i2;
                    }
                    VrManagerService.this.mPersistentVrStateRemoteCallbacks.finishBroadcast();
                    printWriter.println("\n");
                    printWriter.println("Installed VrListenerService components:");
                    int i3 = VrManagerService.this.mCurrentVrModeUser;
                    ArraySet<ComponentName> installed = VrManagerService.this.mComponentObserver.getInstalled(i3);
                    if (installed == null || installed.size() == 0) {
                        printWriter.println("None");
                    } else {
                        Iterator<ComponentName> it = installed.iterator();
                        while (it.hasNext()) {
                            ComponentName next = it.next();
                            printWriter.print("  ");
                            printWriter.println(next.flattenToString());
                        }
                    }
                    printWriter.println("Enabled VrListenerService components:");
                    ArraySet<ComponentName> enabled = VrManagerService.this.mComponentObserver.getEnabled(i3);
                    if (enabled == null || enabled.size() == 0) {
                        printWriter.println("None");
                    } else {
                        Iterator<ComponentName> it2 = enabled.iterator();
                        while (it2.hasNext()) {
                            ComponentName next2 = it2.next();
                            printWriter.print("  ");
                            printWriter.println(next2.flattenToString());
                        }
                    }
                    printWriter.println("\n");
                    printWriter.println("********* End of VrManagerService Dump *********");
                }
            }
        };
    }

    public void onStart() {
        synchronized (this.mLock) {
            initializeNative();
            this.mContext = getContext();
        }
        boolean z = false;
        boolean z2 = SystemProperties.getBoolean("ro.boot.vr", false);
        this.mBootsToVr = z2;
        if (z2 && SystemProperties.getBoolean("persist.vr.use_standby_to_exit_vr_mode", true)) {
            z = true;
        }
        this.mUseStandbyToExitVrMode = z;
        publishLocalService(VrManagerInternal.class, new LocalService());
        publishBinderService("vrmanager", this.mVrManager.asBinder());
    }

    public void onBootPhase(int i) {
        if (i == 500) {
            ((ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class)).registerScreenObserver(this);
            this.mNotificationManager = INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
            synchronized (this.mLock) {
                Looper mainLooper = Looper.getMainLooper();
                Handler handler = new Handler(mainLooper);
                ArrayList arrayList = new ArrayList();
                arrayList.add(this);
                EnabledComponentsObserver build = EnabledComponentsObserver.build(this.mContext, handler, "enabled_vr_listeners", mainLooper, "android.permission.BIND_VR_LISTENER_SERVICE", "android.service.vr.VrListenerService", this.mLock, arrayList);
                this.mComponentObserver = build;
                build.rebuildAll();
            }
            ArraySet defaultVrComponents = SystemConfig.getInstance().getDefaultVrComponents();
            if (defaultVrComponents.size() > 0) {
                this.mDefaultVrService = (ComponentName) defaultVrComponents.valueAt(0);
            } else {
                Slog.i(TAG, "No default vr listener service found.");
            }
            Vr2dDisplay vr2dDisplay = new Vr2dDisplay((DisplayManager) getContext().getSystemService("display"), (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class), (WindowManagerInternal) LocalServices.getService(WindowManagerInternal.class), this.mVrManager);
            this.mVr2dDisplay = vr2dDisplay;
            vr2dDisplay.init(getContext(), this.mBootsToVr);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.USER_UNLOCKED");
            getContext().registerReceiver(new BroadcastReceiver() { // from class: com.android.server.vr.VrManagerService.5
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    if ("android.intent.action.USER_UNLOCKED".equals(intent.getAction())) {
                        VrManagerService.this.setUserUnlocked();
                    }
                }
            }, intentFilter);
        }
    }

    public void onUserStarting(SystemService.TargetUser targetUser) {
        synchronized (this.mLock) {
            this.mComponentObserver.onUsersChanged();
        }
    }

    public void onUserSwitching(SystemService.TargetUser targetUser, SystemService.TargetUser targetUser2) {
        FgThread.getHandler().post(new Runnable() { // from class: com.android.server.vr.VrManagerService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                VrManagerService.this.lambda$onUserSwitching$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUserSwitching$0() {
        synchronized (this.mLock) {
            this.mComponentObserver.onUsersChanged();
        }
    }

    public void onUserStopping(SystemService.TargetUser targetUser) {
        synchronized (this.mLock) {
            this.mComponentObserver.onUsersChanged();
        }
    }

    public void onUserStopped(SystemService.TargetUser targetUser) {
        synchronized (this.mLock) {
            this.mComponentObserver.onUsersChanged();
        }
    }

    private void updateOverlayStateLocked(String str, int i, int i2) {
        AppOpsManager appOpsManager = (AppOpsManager) getContext().getSystemService(AppOpsManager.class);
        if (i2 != i) {
            appOpsManager.setUserRestrictionForUser(24, false, this.mOverlayToken, null, i2);
        }
        appOpsManager.setUserRestrictionForUser(24, this.mVrModeEnabled, this.mOverlayToken, str != null ? new PackageTagsList.Builder(1).add(str).build() : null, i);
    }

    private void updateDependentAppOpsLocked(String str, int i, String str2, int i2) {
        if (Objects.equals(str, str2)) {
            return;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            updateOverlayStateLocked(str, i, i2);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private boolean updateCurrentVrServiceLocked(boolean z, boolean z2, ComponentName componentName, int i, int i2, ComponentName componentName2) {
        boolean z3;
        boolean z4;
        boolean z5;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            boolean z6 = this.mComponentObserver.isValid(componentName, i) == 0;
            boolean z7 = z6 && z;
            if (!this.mVrModeEnabled && !z7) {
                return z6;
            }
            ManagedApplicationService managedApplicationService = this.mCurrentVrService;
            String packageName = managedApplicationService != null ? managedApplicationService.getComponent().getPackageName() : null;
            int i3 = this.mCurrentVrModeUser;
            changeVrModeLocked(z7);
            if (!z7) {
                if (this.mCurrentVrService != null) {
                    Slog.i(TAG, "Leaving VR mode, disconnecting " + this.mCurrentVrService.getComponent() + " for user " + this.mCurrentVrService.getUserId());
                    this.mCurrentVrService.disconnect();
                    updateCompositorServiceLocked(-10000, null);
                    this.mCurrentVrService = null;
                    z3 = false;
                    z4 = false;
                }
                z3 = false;
                z4 = true;
            } else {
                ManagedApplicationService managedApplicationService2 = this.mCurrentVrService;
                if (managedApplicationService2 != null) {
                    if (managedApplicationService2.disconnectIfNotMatching(componentName, i)) {
                        Slog.i(TAG, "VR mode component changed to " + componentName + ", disconnecting " + this.mCurrentVrService.getComponent() + " for user " + this.mCurrentVrService.getUserId());
                        updateCompositorServiceLocked(-10000, null);
                        createAndConnectService(componentName, i);
                    }
                    z3 = false;
                    z4 = true;
                } else {
                    createAndConnectService(componentName, i);
                }
                z3 = true;
                z4 = false;
            }
            if (((componentName2 != null || this.mPersistentVrModeEnabled) && !Objects.equals(componentName2, this.mCurrentVrModeComponent)) || this.mRunning2dInVr != z2) {
                z3 = true;
            }
            this.mCurrentVrModeComponent = componentName2;
            this.mRunning2dInVr = z2;
            this.mVrAppProcessId = i2;
            if (this.mCurrentVrModeUser != i) {
                this.mCurrentVrModeUser = i;
                z5 = true;
            } else {
                z5 = z3;
            }
            ManagedApplicationService managedApplicationService3 = this.mCurrentVrService;
            updateDependentAppOpsLocked(managedApplicationService3 != null ? managedApplicationService3.getComponent().getPackageName() : null, this.mCurrentVrModeUser, packageName, i3);
            if (this.mCurrentVrService != null && z5) {
                callFocusedActivityChangedLocked();
            }
            if (!z4) {
                logStateLocked();
            }
            return z6;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callFocusedActivityChangedLocked() {
        final ComponentName componentName = this.mCurrentVrModeComponent;
        final boolean z = this.mRunning2dInVr;
        final int i = this.mVrAppProcessId;
        this.mCurrentVrService.sendEvent(new ManagedApplicationService.PendingEvent() { // from class: com.android.server.vr.VrManagerService.6
            @Override // com.android.server.utils.ManagedApplicationService.PendingEvent
            public void runEvent(IInterface iInterface) throws RemoteException {
                ((IVrListener) iInterface).focusedActivityChanged(componentName, z, i);
            }
        });
    }

    private boolean isDefaultAllowed(String str) {
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = this.mContext.getPackageManager().getApplicationInfo(str, 128);
        } catch (PackageManager.NameNotFoundException unused) {
            applicationInfo = null;
        }
        if (applicationInfo != null) {
            return applicationInfo.isSystemApp() || applicationInfo.isUpdatedSystemApp();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void grantNotificationPolicyAccess(String str) {
        ((NotificationManager) this.mContext.getSystemService(NotificationManager.class)).setNotificationPolicyAccessGranted(str, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void revokeNotificationPolicyAccess(String str) {
        NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService(NotificationManager.class);
        notificationManager.removeAutomaticZenRules(str);
        notificationManager.setNotificationPolicyAccessGranted(str, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void grantNotificationListenerAccess(String str, int i) {
        NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService(NotificationManager.class);
        Iterator<ComponentName> it = EnabledComponentsObserver.loadComponentNames(this.mContext.getPackageManager(), i, "android.service.notification.NotificationListenerService", "android.permission.BIND_NOTIFICATION_LISTENER_SERVICE").iterator();
        while (it.hasNext()) {
            ComponentName next = it.next();
            if (Objects.equals(next.getPackageName(), str)) {
                try {
                    notificationManager.setNotificationListenerAccessGrantedForUser(next, i, true);
                } catch (Exception e) {
                    Slog.w(TAG, "Could not grant NLS access to package " + str, e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void revokeNotificationListenerAccess(String str, int i) {
        NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService(NotificationManager.class);
        for (ComponentName componentName : notificationManager.getEnabledNotificationListeners(i)) {
            if (componentName != null && componentName.getPackageName().equals(str)) {
                notificationManager.setNotificationListenerAccessGrantedForUser(componentName, i, false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void grantCoarseLocationPermissionIfNeeded(String str, int i) {
        if (isPermissionUserUpdated("android.permission.ACCESS_COARSE_LOCATION", str, i)) {
            return;
        }
        try {
            this.mContext.getPackageManager().grantRuntimePermission(str, "android.permission.ACCESS_COARSE_LOCATION", new UserHandle(i));
        } catch (IllegalArgumentException unused) {
            Slog.w(TAG, "Could not grant coarse location permission, package " + str + " was removed.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void revokeCoarseLocationPermissionIfNeeded(String str, int i) {
        if (isPermissionUserUpdated("android.permission.ACCESS_COARSE_LOCATION", str, i)) {
            return;
        }
        try {
            this.mContext.getPackageManager().revokeRuntimePermission(str, "android.permission.ACCESS_COARSE_LOCATION", new UserHandle(i));
        } catch (IllegalArgumentException unused) {
            Slog.w(TAG, "Could not revoke coarse location permission, package " + str + " was removed.");
        }
    }

    private boolean isPermissionUserUpdated(String str, String str2, int i) {
        return (this.mContext.getPackageManager().getPermissionFlags(str, str2, new UserHandle(i)) & 3) != 0;
    }

    private ArraySet<String> getNotificationListeners(ContentResolver contentResolver, int i) {
        String stringForUser = Settings.Secure.getStringForUser(contentResolver, "enabled_notification_listeners", i);
        ArraySet<String> arraySet = new ArraySet<>();
        if (stringForUser != null) {
            for (String str : stringForUser.split(":")) {
                if (!TextUtils.isEmpty(str)) {
                    arraySet.add(str);
                }
            }
        }
        return arraySet;
    }

    private static String formatSettings(Collection<String> collection) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        for (String str : collection) {
            if (!"".equals(str)) {
                if (!z) {
                    sb.append(':');
                }
                sb.append(str);
                z = false;
            }
        }
        return sb.toString();
    }

    private void createAndConnectService(ComponentName componentName, int i) {
        ManagedApplicationService createVrListenerService = createVrListenerService(componentName, i);
        this.mCurrentVrService = createVrListenerService;
        createVrListenerService.connect();
        Slog.i(TAG, "Connecting " + componentName + " for user " + i);
    }

    private void changeVrModeLocked(boolean z) {
        if (this.mVrModeEnabled != z) {
            this.mVrModeEnabled = z;
            StringBuilder sb = new StringBuilder();
            sb.append("VR mode ");
            sb.append(this.mVrModeEnabled ? ServiceConfigAccessor.PROVIDER_MODE_ENABLED : ServiceConfigAccessor.PROVIDER_MODE_DISABLED);
            Slog.i(TAG, sb.toString());
            setVrModeNative(this.mVrModeEnabled);
            onVrModeChangedLocked();
        }
    }

    private void onVrModeChangedLocked() {
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(0, this.mVrModeEnabled ? 1 : 0, 0));
    }

    private ManagedApplicationService createVrListenerService(ComponentName componentName, int i) {
        return ManagedApplicationService.build(this.mContext, componentName, i, 17041849, "android.settings.VR_LISTENER_SETTINGS", sBinderChecker, true, this.mBootsToVr ? 1 : 2, this.mHandler, this.mEventCallback);
    }

    private ManagedApplicationService createVrCompositorService(ComponentName componentName, int i) {
        return ManagedApplicationService.build(this.mContext, componentName, i, 0, null, null, true, this.mBootsToVr ? 1 : 3, this.mHandler, this.mEventCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void consumeAndApplyPendingStateLocked() {
        consumeAndApplyPendingStateLocked(true);
    }

    private void consumeAndApplyPendingStateLocked(boolean z) {
        VrState vrState = this.mPendingState;
        if (vrState != null) {
            updateCurrentVrServiceLocked(vrState.enabled, vrState.running2dInVr, vrState.targetPackageName, vrState.userId, vrState.processId, vrState.callingPackage);
            this.mPendingState = null;
        } else if (z) {
            updateCurrentVrServiceLocked(false, false, null, 0, -1, null);
        }
    }

    private void logStateLocked() {
        ManagedApplicationService managedApplicationService = this.mCurrentVrService;
        logEvent(new VrState(this.mVrModeEnabled, this.mRunning2dInVr, managedApplicationService == null ? null : managedApplicationService.getComponent(), this.mCurrentVrModeUser, this.mVrAppProcessId, this.mCurrentVrModeComponent, this.mWasDefaultGranted));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logEvent(ManagedApplicationService.LogFormattable logFormattable) {
        synchronized (this.mLoggingDeque) {
            if (this.mLoggingDeque.size() == 64) {
                this.mLoggingDeque.removeFirst();
                this.mLogLimitHit = true;
            }
            this.mLoggingDeque.add(logFormattable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpStateTransitions(PrintWriter printWriter) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
        synchronized (this.mLoggingDeque) {
            if (this.mLoggingDeque.size() == 0) {
                printWriter.print("  ");
                printWriter.println("None");
            }
            if (this.mLogLimitHit) {
                printWriter.println("...");
            }
            Iterator<ManagedApplicationService.LogFormattable> it = this.mLoggingDeque.iterator();
            while (it.hasNext()) {
                printWriter.println(it.next().toLogString(simpleDateFormat));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:11:0x001f A[Catch: all -> 0x0010, TryCatch #0 {all -> 0x0010, blocks: (B:33:0x0009, B:7:0x0016, B:11:0x001f, B:12:0x0025, B:14:0x0038, B:15:0x003a, B:19:0x003e, B:21:0x0042, B:23:0x0046, B:24:0x004d, B:25:0x004f, B:27:0x0051, B:28:0x0067), top: B:32:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0038 A[Catch: all -> 0x0010, TryCatch #0 {all -> 0x0010, blocks: (B:33:0x0009, B:7:0x0016, B:11:0x001f, B:12:0x0025, B:14:0x0038, B:15:0x003a, B:19:0x003e, B:21:0x0042, B:23:0x0046, B:24:0x004d, B:25:0x004f, B:27:0x0051, B:28:0x0067), top: B:32:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x003c  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0023  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setVrMode(boolean z, ComponentName componentName, int i, int i2, ComponentName componentName2) {
        boolean z2;
        synchronized (this.mLock) {
            if (!z) {
                try {
                    if (!this.mPersistentVrModeEnabled) {
                        z2 = false;
                        boolean z3 = z && this.mPersistentVrModeEnabled;
                        ComponentName componentName3 = !z3 ? this.mDefaultVrService : componentName;
                        VrState vrState = new VrState(z2, z3, componentName3, i, i2, componentName2);
                        if (this.mVrModeAllowed) {
                            this.mPendingState = vrState;
                            return;
                        }
                        if (!z2 && this.mCurrentVrService != null) {
                            if (this.mPendingState == null) {
                                this.mHandler.sendEmptyMessageDelayed(1, 300L);
                            }
                            this.mPendingState = vrState;
                            return;
                        } else {
                            this.mHandler.removeMessages(1);
                            this.mPendingState = null;
                            updateCurrentVrServiceLocked(z2, z3, componentName3, i, i2, componentName2);
                            return;
                        }
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            z2 = true;
            if (z) {
            }
            if (!z3) {
            }
            VrState vrState2 = new VrState(z2, z3, componentName3, i, i2, componentName2);
            if (this.mVrModeAllowed) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPersistentVrModeEnabled(boolean z) {
        synchronized (this.mLock) {
            setPersistentModeAndNotifyListenersLocked(z);
            if (!z) {
                setVrMode(false, null, 0, -1, null);
            }
        }
    }

    public void setVr2dDisplayProperties(Vr2dDisplayProperties vr2dDisplayProperties) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Vr2dDisplay vr2dDisplay = this.mVr2dDisplay;
            if (vr2dDisplay != null) {
                vr2dDisplay.setVirtualDisplayProperties(vr2dDisplayProperties);
            } else {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                Slog.w(TAG, "Vr2dDisplay is null!");
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getVr2dDisplayId() {
        Vr2dDisplay vr2dDisplay = this.mVr2dDisplay;
        if (vr2dDisplay != null) {
            return vr2dDisplay.getVirtualDisplayId();
        }
        Slog.w(TAG, "Vr2dDisplay is null!");
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAndBindCompositor(ComponentName componentName) {
        int callingUserId = UserHandle.getCallingUserId();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                updateCompositorServiceLocked(callingUserId, componentName);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void updateCompositorServiceLocked(int i, ComponentName componentName) {
        ManagedApplicationService managedApplicationService = this.mCurrentVrCompositorService;
        if (managedApplicationService != null && managedApplicationService.disconnectIfNotMatching(componentName, i)) {
            Slog.i(TAG, "Disconnecting compositor service: " + this.mCurrentVrCompositorService.getComponent());
            this.mCurrentVrCompositorService = null;
        }
        if (componentName == null || this.mCurrentVrCompositorService != null) {
            return;
        }
        Slog.i(TAG, "Connecting compositor service: " + componentName);
        ManagedApplicationService createVrCompositorService = createVrCompositorService(componentName, i);
        this.mCurrentVrCompositorService = createVrCompositorService;
        createVrCompositorService.connect();
    }

    private void setPersistentModeAndNotifyListenersLocked(boolean z) {
        if (this.mPersistentVrModeEnabled == z) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Persistent VR mode ");
        sb.append(z ? ServiceConfigAccessor.PROVIDER_MODE_ENABLED : ServiceConfigAccessor.PROVIDER_MODE_DISABLED);
        String sb2 = sb.toString();
        Slog.i(TAG, sb2);
        logEvent(new SettingEvent(sb2));
        this.mPersistentVrModeEnabled = z;
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(2, z ? 1 : 0, 0));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int hasVrPackage(ComponentName componentName, int i) {
        int isValid;
        synchronized (this.mLock) {
            isValid = this.mComponentObserver.isValid(componentName, i);
        }
        return isValid;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCurrentVrListener(String str, int i) {
        synchronized (this.mLock) {
            ManagedApplicationService managedApplicationService = this.mCurrentVrService;
            boolean z = false;
            if (managedApplicationService == null) {
                return false;
            }
            if (managedApplicationService.getComponent().getPackageName().equals(str) && i == this.mCurrentVrService.getUserId()) {
                z = true;
            }
            return z;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addStateCallback(IVrStateCallbacks iVrStateCallbacks) {
        this.mVrStateRemoteCallbacks.register(iVrStateCallbacks);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeStateCallback(IVrStateCallbacks iVrStateCallbacks) {
        this.mVrStateRemoteCallbacks.unregister(iVrStateCallbacks);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addPersistentStateCallback(IPersistentVrStateCallbacks iPersistentVrStateCallbacks) {
        this.mPersistentVrStateRemoteCallbacks.register(iPersistentVrStateCallbacks);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removePersistentStateCallback(IPersistentVrStateCallbacks iPersistentVrStateCallbacks) {
        this.mPersistentVrStateRemoteCallbacks.unregister(iPersistentVrStateCallbacks);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getVrMode() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mVrModeEnabled;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getPersistentVrMode() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mPersistentVrModeEnabled;
        }
        return z;
    }
}
