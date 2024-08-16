package com.android.server.camera;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.admin.DevicePolicyManager;
import android.app.compat.CompatChanges;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ParceledListSlice;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.CameraExtensionSessionStats;
import android.hardware.CameraSessionStats;
import android.hardware.CameraStreamStats;
import android.hardware.ICameraService;
import android.hardware.ICameraServiceProxy;
import android.hardware.devicestate.DeviceStateManager;
import android.hardware.display.DisplayManager;
import android.hardware.usb.UsbDevice;
import android.media.AudioManager;
import android.nfc.IAppCallback;
import android.nfc.INfcAdapter;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.stats.camera.nano.CameraProtos;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.Slog;
import android.view.Display;
import android.view.IDisplayWindowListener;
import android.view.WindowManagerGlobal;
import com.android.framework.protobuf.nano.MessageNano;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.LocalServices;
import com.android.server.ServiceThread;
import com.android.server.SystemService;
import com.android.server.wm.WindowManagerInternal;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class CameraServiceProxy extends SystemService implements Handler.Callback, IBinder.DeathRecipient {
    private static final String CAMERA_SERVICE_BINDER_NAME = "media.camera";
    public static final String CAMERA_SERVICE_PROXY_BINDER_NAME = "media.camera.proxy";
    private static final boolean DEBUG = false;
    public static final int DISABLE_POLLING_FLAGS = 4096;
    public static final int ENABLE_POLLING_FLAGS = 0;
    private static final float MAX_PREVIEW_FPS = 60.0f;
    private static final int MAX_STREAM_STATISTICS = 5;
    private static final int MAX_USAGE_HISTORY = 20;
    private static final float MIN_PREVIEW_FPS = 30.0f;
    private static final int MSG_NFC_STATE_CHANGED = 3000;
    private static final int MSG_NOTIFY_DEVICE_STATE = 2;
    private static final int MSG_SWITCH_USER = 1;
    private static final String NFC_NOTIFICATION_PROP = "ro.camera.notify_nfc";
    private static final String NFC_SERVICE_BINDER_NAME = "nfc";
    public static final long OVERRIDE_CAMERA_RESIZABLE_AND_SDK_CHECK = 191513214;
    public static final long OVERRIDE_CAMERA_ROTATE_AND_CROP_DEFAULTS = 189229956;
    private static final int RETRY_DELAY_TIME = 20;
    private static final int RETRY_TIMES = 60;
    private static final String TAG = "CameraService_proxy";
    private static final IBinder nfcInterfaceToken = new Binder();
    private final ArrayMap<String, CameraUsageEvent> mActiveCameraUsage;
    private final ICameraServiceProxy.Stub mCameraServiceProxy;
    private ICameraServiceProxyExt mCameraServiceProxyExt;
    private ICameraServiceProxyWrapper mCameraServiceProxyWrapper;
    private ICameraService mCameraServiceRaw;
    private final List<CameraUsageEvent> mCameraUsageHistory;
    private final Context mContext;

    @GuardedBy({"mLock"})
    private int mDeviceState;
    private final DisplayWindowListener mDisplayWindowListener;
    private Set<Integer> mEnabledCameraUsers;
    private final DeviceStateManager.FoldStateListener mFoldStateListener;
    private final Handler mHandler;
    private final ServiceThread mHandlerThread;
    private final BroadcastReceiver mIntentReceiver;
    private volatile boolean mIsSmallWindow;

    @GuardedBy({"mLock"})
    private int mLastReportedDeviceState;
    private int mLastUser;
    private final Object mLock;
    private ScheduledThreadPoolExecutor mLogWriterService;
    private final boolean mNotifyNfc;
    private UserManager mUserManager;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    @interface DeviceStateFlags {
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class TaskInfo {
        public int displayId;
        public int frontTaskId;
        public boolean isFixedOrientationLandscape;
        public boolean isFixedOrientationPortrait;
        public boolean isResizeable;
        public int userId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String cameraFacingToString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? "CAMERA_FACING_UNKNOWN" : "CAMERA_FACING_EXTERNAL" : "CAMERA_FACING_FRONT" : "CAMERA_FACING_BACK";
    }

    private static String cameraHistogramTypeToString(int i) {
        return i != 1 ? "HISTOGRAM_TYPE_UNKNOWN" : "HISTOGRAM_TYPE_CAPTURE_LATENCY";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String cameraStateToString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 3 ? "CAMERA_STATE_UNKNOWN" : "CAMERA_STATE_CLOSED" : "CAMERA_STATE_IDLE" : "CAMERA_STATE_ACTIVE" : "CAMERA_STATE_OPEN";
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class CameraUsageEvent {
        public final int mAPILevel;
        public final int mAction;
        public final int mCameraFacing;
        public final String mCameraId;
        public final String mClientName;
        public boolean mDeviceError;
        public int mInternalReconfigure;
        public final boolean mIsNdk;
        public final int mLatencyMs;
        public final long mLogId;
        public final int mOperatingMode;
        public long mRequestCount;
        public long mResultErrorCount;
        public final int mSessionIndex;
        public List<CameraStreamStats> mStreamStats;
        public String mUserTag;
        public int mVideoStabilizationMode;
        public CameraExtensionSessionStats mExtSessionStats = null;
        private long mDurationOrStartTimeMs = SystemClock.elapsedRealtime();
        private boolean mCompleted = false;

        CameraUsageEvent(String str, int i, String str2, int i2, boolean z, int i3, int i4, int i5, boolean z2, long j, int i6) {
            this.mCameraId = str;
            this.mCameraFacing = i;
            this.mClientName = str2;
            this.mAPILevel = i2;
            this.mIsNdk = z;
            this.mAction = i3;
            this.mLatencyMs = i4;
            this.mOperatingMode = i5;
            this.mDeviceError = z2;
            this.mLogId = j;
            this.mSessionIndex = i6;
        }

        public void markCompleted(int i, long j, long j2, boolean z, List<CameraStreamStats> list, String str, int i2, CameraExtensionSessionStats cameraExtensionSessionStats) {
            if (this.mCompleted) {
                return;
            }
            this.mCompleted = true;
            this.mDurationOrStartTimeMs = SystemClock.elapsedRealtime() - this.mDurationOrStartTimeMs;
            this.mInternalReconfigure = i;
            this.mRequestCount = j;
            this.mResultErrorCount = j2;
            this.mDeviceError = z;
            this.mStreamStats = list;
            this.mUserTag = str;
            this.mVideoStabilizationMode = i2;
            this.mExtSessionStats = cameraExtensionSessionStats;
        }

        public long getDuration() {
            if (this.mCompleted) {
                return this.mDurationOrStartTimeMs;
            }
            return 0L;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class DisplayWindowListener extends IDisplayWindowListener.Stub {
        public void onDisplayAdded(int i) {
        }

        public void onDisplayRemoved(int i) {
        }

        public void onFixedRotationFinished(int i) {
        }

        public void onFixedRotationStarted(int i, int i2) {
        }

        public void onKeepClearAreasChanged(int i, List<Rect> list, List<Rect> list2) {
        }

        private DisplayWindowListener() {
        }

        public void onDisplayConfigurationChanged(int i, Configuration configuration) {
            ICameraService cameraServiceRawLocked = CameraServiceProxy.this.getCameraServiceRawLocked();
            if (cameraServiceRawLocked == null) {
                return;
            }
            try {
                cameraServiceRawLocked.notifyDisplayConfigurationChange();
            } catch (RemoteException e) {
                Slog.w(CameraServiceProxy.TAG, "Could not notify cameraserver, remote exception: " + e);
            }
        }
    }

    private static boolean isMOrBelow(Context context, String str) {
        try {
            return context.getPackageManager().getPackageInfo(str, 0).applicationInfo.targetSdkVersion <= 23;
        } catch (PackageManager.NameNotFoundException unused) {
            Slog.e(TAG, "Package name not found!");
            return false;
        }
    }

    public static int getCropRotateScale(Context context, String str, TaskInfo taskInfo, int i, int i2, boolean z) {
        int i3;
        if (taskInfo == null) {
            return 0;
        }
        if (context.getResources().getBoolean(17891726)) {
            Slog.v(TAG, "Disable Rotate and Crop to avoid conflicts with WM force rotation treatment.");
            return 0;
        }
        if (i2 != 0 && i2 != 1) {
            Log.v(TAG, "lensFacing=" + i2 + ". Crop-rotate-scale is disabled.");
            return 0;
        }
        if (!z && !isMOrBelow(context, str) && taskInfo.isResizeable) {
            Slog.v(TAG, "The activity is N or above and claims to support resizeable-activity. Crop-rotate-scale is disabled.");
            return 0;
        }
        if (!taskInfo.isFixedOrientationPortrait && !taskInfo.isFixedOrientationLandscape) {
            Log.v(TAG, "Non-fixed orientation activity. Crop-rotate-scale is disabled.");
            return 0;
        }
        if (i == 0) {
            i3 = 0;
        } else if (i == 1) {
            i3 = 90;
        } else if (i == 2) {
            i3 = 180;
        } else {
            if (i != 3) {
                Log.e(TAG, "Unsupported display rotation: " + i);
                return 0;
            }
            i3 = 270;
        }
        Slog.v(TAG, "Display.getRotation()=" + i3 + " isFixedOrientationPortrait=" + taskInfo.isFixedOrientationPortrait + " isFixedOrientationLandscape=" + taskInfo.isFixedOrientationLandscape);
        if (i3 == 0) {
            return 0;
        }
        if (i2 == 0) {
            i3 = 360 - i3;
        }
        if (i3 == 90) {
            return 1;
        }
        if (i3 != 180) {
            return i3 != 270 ? 0 : 3;
        }
        return 2;
    }

    public CameraServiceProxy(Context context) {
        super(context);
        this.mLock = new Object();
        this.mActiveCameraUsage = new ArrayMap<>();
        this.mCameraUsageHistory = new ArrayList();
        this.mLogWriterService = new ScheduledThreadPoolExecutor(1);
        this.mIsSmallWindow = false;
        this.mDisplayWindowListener = new DisplayWindowListener();
        this.mIntentReceiver = new BroadcastReceiver() { // from class: com.android.server.camera.CameraServiceProxy.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                char c = 65535;
                switch (action.hashCode()) {
                    case -2114103349:
                        if (action.equals("android.hardware.usb.action.USB_DEVICE_ATTACHED")) {
                            c = 0;
                            break;
                        }
                        break;
                    case -2061058799:
                        if (action.equals("android.intent.action.USER_REMOVED")) {
                            c = 1;
                            break;
                        }
                        break;
                    case -1608292967:
                        if (action.equals("android.hardware.usb.action.USB_DEVICE_DETACHED")) {
                            c = 2;
                            break;
                        }
                        break;
                    case -385593787:
                        if (action.equals("android.intent.action.MANAGED_PROFILE_ADDED")) {
                            c = 3;
                            break;
                        }
                        break;
                    case -201513518:
                        if (action.equals("android.intent.action.USER_INFO_CHANGED")) {
                            c = 4;
                            break;
                        }
                        break;
                    case 1051477093:
                        if (action.equals("android.intent.action.MANAGED_PROFILE_REMOVED")) {
                            c = 5;
                            break;
                        }
                        break;
                    case 1121780209:
                        if (action.equals("android.intent.action.USER_ADDED")) {
                            c = 6;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                    case 2:
                        synchronized (CameraServiceProxy.this.mLock) {
                            UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra("device", UsbDevice.class);
                            if (usbDevice != null) {
                                CameraServiceProxy.this.notifyUsbDeviceHotplugLocked(usbDevice, action.equals("android.hardware.usb.action.USB_DEVICE_ATTACHED"));
                            }
                        }
                        return;
                    case 1:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        synchronized (CameraServiceProxy.this.mLock) {
                            if (CameraServiceProxy.this.mEnabledCameraUsers == null) {
                                return;
                            }
                            CameraServiceProxy cameraServiceProxy = CameraServiceProxy.this;
                            cameraServiceProxy.switchUserLocked(cameraServiceProxy.mLastUser);
                            return;
                        }
                    default:
                        return;
                }
            }
        };
        this.mCameraServiceProxy = new ICameraServiceProxy.Stub() { // from class: com.android.server.camera.CameraServiceProxy.2
            public int getAutoframingOverride(String str) {
                return 0;
            }

            public int getRotateAndCropOverride(String str, int i, int i2) {
                TaskInfo taskInfo;
                boolean z;
                if (Binder.getCallingUid() != 1047) {
                    Slog.e(CameraServiceProxy.TAG, "Calling UID: " + Binder.getCallingUid() + " doesn't match expected  camera service UID!");
                    return 0;
                }
                try {
                    ParceledListSlice recentTasks = ActivityTaskManager.getService().getRecentTasks(2, 0, i2);
                    if (recentTasks != null && !recentTasks.getList().isEmpty()) {
                        Iterator it = recentTasks.getList().iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                taskInfo = null;
                                break;
                            }
                            ActivityManager.RecentTaskInfo recentTaskInfo = (ActivityManager.RecentTaskInfo) it.next();
                            ActivityInfo activityInfo = recentTaskInfo.topActivityInfo;
                            if (activityInfo != null && str.equals(activityInfo.packageName)) {
                                taskInfo = new TaskInfo();
                                taskInfo.frontTaskId = recentTaskInfo.taskId;
                                ActivityInfo activityInfo2 = recentTaskInfo.topActivityInfo;
                                taskInfo.isResizeable = activityInfo2.resizeMode != 0;
                                taskInfo.displayId = recentTaskInfo.displayId;
                                taskInfo.userId = recentTaskInfo.userId;
                                taskInfo.isFixedOrientationLandscape = ActivityInfo.isFixedOrientationLandscape(activityInfo2.screenOrientation);
                                taskInfo.isFixedOrientationPortrait = ActivityInfo.isFixedOrientationPortrait(recentTaskInfo.topActivityInfo.screenOrientation);
                            }
                        }
                        TaskInfo taskInfo2 = taskInfo;
                        if (taskInfo2 == null) {
                            Log.e(CameraServiceProxy.TAG, "Recent tasks don't include camera client package name: " + str);
                            return 0;
                        }
                        if (CompatChanges.isChangeEnabled(CameraServiceProxy.OVERRIDE_CAMERA_ROTATE_AND_CROP_DEFAULTS, str, UserHandle.getUserHandleForUid(taskInfo2.userId))) {
                            Slog.v(CameraServiceProxy.TAG, "OVERRIDE_CAMERA_ROTATE_AND_CROP_DEFAULTS enabled!");
                            return 0;
                        }
                        if (CompatChanges.isChangeEnabled(CameraServiceProxy.OVERRIDE_CAMERA_RESIZABLE_AND_SDK_CHECK, str, UserHandle.getUserHandleForUid(taskInfo2.userId))) {
                            Slog.v(CameraServiceProxy.TAG, "OVERRIDE_CAMERA_RESIZABLE_AND_SDK_CHECK enabled!");
                            z = true;
                        } else {
                            z = false;
                        }
                        DisplayManager displayManager = (DisplayManager) CameraServiceProxy.this.mContext.getSystemService(DisplayManager.class);
                        if (displayManager != null) {
                            Display display = displayManager.getDisplay(taskInfo2.displayId);
                            if (display == null) {
                                Slog.e(CameraServiceProxy.TAG, "Invalid display id: " + taskInfo2.displayId);
                                return 0;
                            }
                            return CameraServiceProxy.getCropRotateScale(CameraServiceProxy.this.mContext, str, taskInfo2, display.getRotation(), i, z);
                        }
                        Slog.e(CameraServiceProxy.TAG, "Failed to query display manager!");
                        return 0;
                    }
                    Log.e(CameraServiceProxy.TAG, "Recent task list is empty!");
                    return 0;
                } catch (RemoteException unused) {
                    Log.e(CameraServiceProxy.TAG, "Failed to query recent tasks!");
                    return 0;
                }
            }

            public void pingForUserUpdate() {
                if (Binder.getCallingUid() != 1047) {
                    Slog.e(CameraServiceProxy.TAG, "Calling UID: " + Binder.getCallingUid() + " doesn't match expected  camera service UID!");
                    return;
                }
                CameraServiceProxy.this.notifySwitchWithRetries(60);
                CameraServiceProxy.this.notifyDeviceStateWithRetries(60);
            }

            public void notifyCameraState(CameraSessionStats cameraSessionStats) {
                if (Binder.getCallingUid() != 1047) {
                    Slog.e(CameraServiceProxy.TAG, "Calling UID: " + Binder.getCallingUid() + " doesn't match expected  camera service UID!");
                    return;
                }
                CameraServiceProxy.cameraStateToString(cameraSessionStats.getNewCameraState());
                CameraServiceProxy.cameraFacingToString(cameraSessionStats.getFacing());
                CameraServiceProxy.this.updateActivityCount(cameraSessionStats);
            }

            public boolean isCameraDisabled(int i) {
                DevicePolicyManager devicePolicyManager = (DevicePolicyManager) CameraServiceProxy.this.mContext.getSystemService(DevicePolicyManager.class);
                if (devicePolicyManager == null) {
                    Slog.e(CameraServiceProxy.TAG, "Failed to get the device policy manager service");
                    return false;
                }
                try {
                    return devicePolicyManager.getCameraDisabled(null, i);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        };
        this.mCameraServiceProxyWrapper = new CameraServiceProxyWrapper();
        this.mContext = context;
        ServiceThread serviceThread = new ServiceThread(TAG, -4, false);
        this.mHandlerThread = serviceThread;
        serviceThread.start();
        Handler handler = new Handler(serviceThread.getLooper(), this);
        this.mHandler = handler;
        this.mCameraServiceProxyExt = (ICameraServiceProxyExt) ExtLoader.type(ICameraServiceProxyExt.class).base(this).create();
        this.mNotifyNfc = SystemProperties.getInt(NFC_NOTIFICATION_PROP, 0) > 0;
        this.mLogWriterService.setKeepAliveTime(1L, TimeUnit.SECONDS);
        this.mLogWriterService.allowCoreThreadTimeOut(true);
        this.mFoldStateListener = new DeviceStateManager.FoldStateListener(context, new Consumer() { // from class: com.android.server.camera.CameraServiceProxy$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                CameraServiceProxy.this.lambda$new$0((Boolean) obj);
            }
        });
        Settings.System.putIntForUser(context.getContentResolver(), "oplus_camera_3rd_activity", 0, -2);
        this.mCameraServiceProxyExt.registerAppSwitchObserver();
        if (this.mCameraServiceProxyExt.getIsRegistered() || this.mCameraServiceProxyExt.getRegisterTimes() >= 5) {
            return;
        }
        handler.sendMessageDelayed(handler.obtainMessage(2000), 60000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Boolean bool) {
        if (bool.booleanValue()) {
            setDeviceStateFlags(4);
        } else {
            clearDeviceStateFlags(4);
        }
    }

    private void setDeviceStateFlags(int i) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(2);
            int i2 = i | this.mDeviceState;
            this.mDeviceState = i2;
            if (i2 != this.mLastReportedDeviceState) {
                notifyDeviceStateWithRetriesLocked(60);
            }
        }
    }

    private void clearDeviceStateFlags(int i) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(2);
            int i2 = (~i) & this.mDeviceState;
            this.mDeviceState = i2;
            if (i2 != this.mLastReportedDeviceState) {
                notifyDeviceStateWithRetriesLocked(60);
            }
        }
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        int i = message.what;
        if (i == 1) {
            notifySwitchWithRetries(message.arg1);
        } else if (i == 2) {
            notifyDeviceStateWithRetries(message.arg1);
        } else if (i == 2000) {
            this.mCameraServiceProxyExt.registerAppSwitchObserver();
            if (!this.mCameraServiceProxyExt.getIsRegistered()) {
                this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(2000), 60000L);
            }
        } else if (i == 2002) {
            synchronized (this.mLock) {
                this.mIsSmallWindow = this.mCameraServiceProxyExt.checkCameraFloatWindow();
            }
        } else if (i != 3000) {
            Slog.e(TAG, "CameraServiceProxy error, invalid message: " + message.what);
        } else if (!this.mActiveCameraUsage.isEmpty() && this.mNotifyNfc && !this.mCameraServiceProxyExt.getNfcSwitchState()) {
            for (int i2 = 0; i2 < this.mActiveCameraUsage.size(); i2++) {
                if (this.mActiveCameraUsage.valueAt(i2).mCameraFacing == 0) {
                    notifyNfcService(false);
                } else {
                    notifyNfcService(true);
                }
            }
        }
        return true;
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        UserManager userManager = UserManager.get(this.mContext);
        this.mUserManager = userManager;
        if (userManager == null) {
            throw new IllegalStateException("UserManagerService must start before CameraServiceProxy!");
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_ADDED");
        intentFilter.addAction("android.intent.action.USER_REMOVED");
        intentFilter.addAction("android.intent.action.USER_INFO_CHANGED");
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_ADDED");
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_REMOVED");
        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter);
        publishBinderService(CAMERA_SERVICE_PROXY_BINDER_NAME, this.mCameraServiceProxy);
        publishLocalService(CameraServiceProxy.class, this);
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int i) {
        if (i == 1000) {
            CameraStatsJobService.schedule(this.mContext);
            try {
                for (int i2 : WindowManagerGlobal.getWindowManagerService().registerDisplayWindowListener(this.mDisplayWindowListener)) {
                    this.mDisplayWindowListener.onDisplayAdded(i2);
                }
            } catch (RemoteException unused) {
                Log.e(TAG, "Failed to register display window listener!");
            }
            ((DeviceStateManager) this.mContext.getSystemService(DeviceStateManager.class)).registerCallback(new HandlerExecutor(this.mHandler), this.mFoldStateListener);
        }
    }

    @Override // com.android.server.SystemService
    public void onUserStarting(SystemService.TargetUser targetUser) {
        synchronized (this.mLock) {
            if (this.mEnabledCameraUsers == null) {
                switchUserLocked(targetUser.getUserIdentifier());
            }
        }
    }

    @Override // com.android.server.SystemService
    public void onUserSwitching(SystemService.TargetUser targetUser, SystemService.TargetUser targetUser2) {
        synchronized (this.mLock) {
            switchUserLocked(targetUser2.getUserIdentifier());
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        synchronized (this.mLock) {
            this.mCameraServiceRaw = null;
            boolean isEmpty = this.mActiveCameraUsage.isEmpty();
            this.mActiveCameraUsage.clear();
            if (this.mNotifyNfc && !isEmpty && !this.mCameraServiceProxyExt.getNfcSwitchState()) {
                notifyNfcService(true);
            }
            this.mCameraServiceProxyExt.unregisterAppSwitchObserver();
            this.mCameraServiceProxyExt.extendNotifyCameraState(-1, null, 1, "-1");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class EventWriterTask implements Runnable {
        private static final long WRITER_SLEEP_MS = 100;
        private ArrayList<CameraUsageEvent> mEventList;

        public EventWriterTask(ArrayList<CameraUsageEvent> arrayList) {
            this.mEventList = arrayList;
        }

        @Override // java.lang.Runnable
        public void run() {
            ArrayList<CameraUsageEvent> arrayList = this.mEventList;
            if (arrayList != null) {
                Iterator<CameraUsageEvent> it = arrayList.iterator();
                while (it.hasNext()) {
                    logCameraUsageEvent(it.next());
                    try {
                        Thread.sleep(WRITER_SLEEP_MS);
                    } catch (InterruptedException unused) {
                    }
                }
                this.mEventList.clear();
            }
        }

        private void logCameraUsageEvent(CameraUsageEvent cameraUsageEvent) {
            int i;
            boolean z;
            int i2 = cameraUsageEvent.mCameraFacing;
            if (i2 == 0) {
                i = 1;
            } else if (i2 == 1) {
                i = 2;
            } else if (i2 != 2) {
                Slog.w(CameraServiceProxy.TAG, "Unknown camera facing: " + cameraUsageEvent.mCameraFacing);
                i = 0;
            } else {
                i = 3;
            }
            CameraExtensionSessionStats cameraExtensionSessionStats = cameraUsageEvent.mExtSessionStats;
            int i3 = -1;
            if (cameraExtensionSessionStats != null) {
                int i4 = cameraExtensionSessionStats.type;
                if (i4 == 0) {
                    i3 = 0;
                } else if (i4 == 1) {
                    i3 = 1;
                } else if (i4 == 2) {
                    i3 = 2;
                } else if (i4 == 3) {
                    i3 = 3;
                } else if (i4 != 4) {
                    Slog.w(CameraServiceProxy.TAG, "Unknown extension type: " + cameraUsageEvent.mExtSessionStats.type);
                } else {
                    i3 = 4;
                }
                z = cameraUsageEvent.mExtSessionStats.isAdvanced;
            } else {
                z = false;
            }
            int i5 = i3;
            List<CameraStreamStats> list = cameraUsageEvent.mStreamStats;
            int size = list != null ? list.size() : 0;
            CameraProtos.CameraStreamProto[] cameraStreamProtoArr = new CameraProtos.CameraStreamProto[5];
            for (int i6 = 0; i6 < 5; i6++) {
                cameraStreamProtoArr[i6] = new CameraProtos.CameraStreamProto();
                if (i6 < size) {
                    CameraStreamStats cameraStreamStats = cameraUsageEvent.mStreamStats.get(i6);
                    cameraStreamProtoArr[i6].width = cameraStreamStats.getWidth();
                    cameraStreamProtoArr[i6].height = cameraStreamStats.getHeight();
                    cameraStreamProtoArr[i6].format = cameraStreamStats.getFormat();
                    cameraStreamProtoArr[i6].dataSpace = cameraStreamStats.getDataSpace();
                    cameraStreamProtoArr[i6].usage = cameraStreamStats.getUsage();
                    cameraStreamProtoArr[i6].requestCount = cameraStreamStats.getRequestCount();
                    cameraStreamProtoArr[i6].errorCount = cameraStreamStats.getErrorCount();
                    cameraStreamProtoArr[i6].firstCaptureLatencyMillis = cameraStreamStats.getStartLatencyMs();
                    cameraStreamProtoArr[i6].maxHalBuffers = cameraStreamStats.getMaxHalBuffers();
                    cameraStreamProtoArr[i6].maxAppBuffers = cameraStreamStats.getMaxAppBuffers();
                    cameraStreamProtoArr[i6].histogramType = cameraStreamStats.getHistogramType();
                    cameraStreamProtoArr[i6].histogramBins = cameraStreamStats.getHistogramBins();
                    cameraStreamProtoArr[i6].histogramCounts = cameraStreamStats.getHistogramCounts();
                    cameraStreamProtoArr[i6].dynamicRangeProfile = cameraStreamStats.getDynamicRangeProfile();
                    cameraStreamProtoArr[i6].streamUseCase = cameraStreamStats.getStreamUseCase();
                    cameraStreamProtoArr[i6].colorSpace = cameraStreamStats.getColorSpace();
                }
            }
            FrameworkStatsLog.write(FrameworkStatsLog.CAMERA_ACTION_EVENT, cameraUsageEvent.getDuration(), cameraUsageEvent.mAPILevel, cameraUsageEvent.mClientName, i, cameraUsageEvent.mCameraId, cameraUsageEvent.mAction, cameraUsageEvent.mIsNdk, cameraUsageEvent.mLatencyMs, cameraUsageEvent.mOperatingMode, cameraUsageEvent.mInternalReconfigure, cameraUsageEvent.mRequestCount, cameraUsageEvent.mResultErrorCount, cameraUsageEvent.mDeviceError, size, MessageNano.toByteArray(cameraStreamProtoArr[0]), MessageNano.toByteArray(cameraStreamProtoArr[1]), MessageNano.toByteArray(cameraStreamProtoArr[2]), MessageNano.toByteArray(cameraStreamProtoArr[3]), MessageNano.toByteArray(cameraStreamProtoArr[4]), cameraUsageEvent.mUserTag, cameraUsageEvent.mVideoStabilizationMode, cameraUsageEvent.mLogId, cameraUsageEvent.mSessionIndex, i5, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpUsageEvents() {
        synchronized (this.mLock) {
            Collections.shuffle(this.mCameraUsageHistory);
            this.mLogWriterService.execute(new EventWriterTask(new ArrayList(this.mCameraUsageHistory)));
            this.mCameraUsageHistory.clear();
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            CameraStatsJobService.schedule(this.mContext);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ICameraService getCameraServiceRawLocked() {
        if (this.mCameraServiceRaw == null) {
            IBinder binderService = getBinderService(CAMERA_SERVICE_BINDER_NAME);
            if (binderService == null) {
                return null;
            }
            try {
                binderService.linkToDeath(this, 0);
                this.mCameraServiceRaw = ICameraService.Stub.asInterface(binderService);
            } catch (RemoteException unused) {
                Slog.w(TAG, "Could not link to death of native camera service");
                return null;
            }
        }
        return this.mCameraServiceRaw;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchUserLocked(int i) {
        Set<Integer> enabledUserHandles = getEnabledUserHandles(i);
        this.mLastUser = i;
        Set<Integer> set = this.mEnabledCameraUsers;
        if (set == null || !set.equals(enabledUserHandles)) {
            this.mEnabledCameraUsers = enabledUserHandles;
            notifySwitchWithRetriesLocked(60);
        }
    }

    private Set<Integer> getEnabledUserHandles(int i) {
        int[] enabledProfileIds = this.mUserManager.getEnabledProfileIds(i);
        ArraySet arraySet = new ArraySet(enabledProfileIds.length);
        for (int i2 : enabledProfileIds) {
            arraySet.add(Integer.valueOf(i2));
        }
        return arraySet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifySwitchWithRetries(int i) {
        synchronized (this.mLock) {
            notifySwitchWithRetriesLocked(i);
        }
    }

    private void notifySwitchWithRetriesLocked(int i) {
        Set<Integer> set = this.mEnabledCameraUsers;
        if (set == null) {
            return;
        }
        if (notifyCameraserverLocked(1, set)) {
            i = 0;
        }
        if (i <= 0) {
            return;
        }
        Slog.i(TAG, "Could not notify camera service of user switch, retrying...");
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(1, i - 1, 0, null), 20L);
    }

    private boolean notifyCameraserverLocked(int i, Set<Integer> set) {
        if (getCameraServiceRawLocked() == null) {
            Slog.w(TAG, "Could not notify cameraserver, camera service not available.");
            return false;
        }
        try {
            this.mCameraServiceRaw.notifySystemEvent(i, toArray(set));
            return true;
        } catch (RemoteException e) {
            Slog.w(TAG, "Could not notify cameraserver, remote exception: " + e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDeviceStateWithRetries(int i) {
        synchronized (this.mLock) {
            notifyDeviceStateWithRetriesLocked(i);
        }
    }

    private void notifyDeviceStateWithRetriesLocked(int i) {
        if (!notifyDeviceStateChangeLocked(this.mDeviceState) && i > 0) {
            Slog.i(TAG, "Could not notify camera service of device state change, retrying...");
            Handler handler = this.mHandler;
            handler.sendMessageDelayed(handler.obtainMessage(2, i - 1, 0, null), 20L);
        }
    }

    private boolean notifyDeviceStateChangeLocked(int i) {
        if (getCameraServiceRawLocked() == null) {
            Slog.w(TAG, "Could not notify cameraserver, camera service not available.");
            return false;
        }
        try {
            this.mCameraServiceRaw.notifyDeviceStateChange(i);
            this.mLastReportedDeviceState = i;
            return true;
        } catch (RemoteException e) {
            Slog.w(TAG, "Could not notify cameraserver, remote exception: " + e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean notifyUsbDeviceHotplugLocked(UsbDevice usbDevice, boolean z) {
        if (usbDevice.getHasVideoCapture()) {
            if (getCameraServiceRawLocked() == null) {
                Slog.w(TAG, "Could not notify cameraserver, camera service not available.");
                return false;
            }
            try {
                this.mCameraServiceRaw.notifySystemEvent(z ? 2 : 3, new int[]{usbDevice.getDeviceId()});
                return true;
            } catch (RemoteException e) {
                Slog.w(TAG, "Could not notify cameraserver, remote exception: " + e);
            }
        }
        return false;
    }

    private float getMinFps(CameraSessionStats cameraSessionStats) {
        return Math.max(Math.min(cameraSessionStats.getMaxPreviewFps(), MAX_PREVIEW_FPS), MIN_PREVIEW_FPS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateActivityCount(CameraSessionStats cameraSessionStats) {
        boolean z;
        boolean z2;
        String str;
        Object obj;
        String str2;
        int i;
        int i2;
        String str3;
        boolean z3;
        boolean z4;
        boolean z5;
        String cameraId = cameraSessionStats.getCameraId();
        int newCameraState = cameraSessionStats.getNewCameraState();
        int facing = cameraSessionStats.getFacing();
        String clientName = cameraSessionStats.getClientName();
        int apiLevel = cameraSessionStats.getApiLevel();
        boolean isNdk = cameraSessionStats.isNdk();
        int sessionType = cameraSessionStats.getSessionType();
        int internalReconfigureCount = cameraSessionStats.getInternalReconfigureCount();
        int latencyMs = cameraSessionStats.getLatencyMs();
        long requestCount = cameraSessionStats.getRequestCount();
        long resultErrorCount = cameraSessionStats.getResultErrorCount();
        boolean deviceErrorFlag = cameraSessionStats.getDeviceErrorFlag();
        List<CameraStreamStats> streamStats = cameraSessionStats.getStreamStats();
        String userTag = cameraSessionStats.getUserTag();
        int videoStabilizationMode = cameraSessionStats.getVideoStabilizationMode();
        long logId = cameraSessionStats.getLogId();
        int sessionIndex = cameraSessionStats.getSessionIndex();
        CameraExtensionSessionStats extensionSessionStats = cameraSessionStats.getExtensionSessionStats();
        Object obj2 = this.mLock;
        synchronized (obj2) {
            try {
            } catch (Throwable th) {
                th = th;
            }
            try {
                boolean isEmpty = this.mActiveCameraUsage.isEmpty();
                if (newCameraState != 0) {
                    if (newCameraState == 1) {
                        z = false;
                        z2 = isEmpty;
                        String str4 = clientName;
                        obj = obj2;
                        i2 = facing;
                        int i3 = 0;
                        while (true) {
                            if (i3 >= this.mActiveCameraUsage.size()) {
                                str3 = str4;
                                z3 = false;
                                break;
                            }
                            str3 = str4;
                            if (this.mActiveCameraUsage.valueAt(i3).mClientName.equals(str3)) {
                                z3 = true;
                                break;
                            } else {
                                i3++;
                                str4 = str3;
                            }
                        }
                        if (!z3) {
                            ((WindowManagerInternal) LocalServices.getService(WindowManagerInternal.class)).addRefreshRateRangeForPackage(str3, getMinFps(cameraSessionStats), MAX_PREVIEW_FPS);
                        }
                        str = str3;
                        CameraUsageEvent put = this.mActiveCameraUsage.put(cameraId, new CameraUsageEvent(cameraId, i2, str3, apiLevel, isNdk, 3, latencyMs, sessionType, deviceErrorFlag, logId, sessionIndex));
                        if (put != null) {
                            Slog.w(TAG, "Camera " + cameraId + " was already marked as active");
                            put.markCompleted(0, 0L, 0L, false, streamStats, "", -1, new CameraExtensionSessionStats());
                            this.mCameraUsageHistory.add(put);
                        }
                    } else if (newCameraState == 2 || newCameraState == 3) {
                        CameraUsageEvent remove = this.mActiveCameraUsage.remove(cameraId);
                        if (remove != null) {
                            remove.markCompleted(internalReconfigureCount, requestCount, resultErrorCount, deviceErrorFlag, streamStats, userTag, videoStabilizationMode, extensionSessionStats);
                            this.mCameraUsageHistory.add(remove);
                            int i4 = 0;
                            while (true) {
                                if (i4 >= this.mActiveCameraUsage.size()) {
                                    z5 = false;
                                    break;
                                } else {
                                    if (this.mActiveCameraUsage.valueAt(i4).mClientName.equals(clientName)) {
                                        z5 = true;
                                        break;
                                    }
                                    i4++;
                                }
                            }
                            if (!z5) {
                                ((WindowManagerInternal) LocalServices.getService(WindowManagerInternal.class)).removeRefreshRateRangeForPackage(clientName);
                            }
                            deviceErrorFlag = false;
                        }
                        if (newCameraState == 3) {
                            if (this.mIsSmallWindow) {
                                long clearCallingIdentity = Binder.clearCallingIdentity();
                                try {
                                    z4 = isEmpty;
                                    Settings.System.putIntForUser(this.mContext.getContentResolver(), "oplus_float_window_show", 0, -2);
                                    Binder.restoreCallingIdentity(clearCallingIdentity);
                                } catch (Throwable th2) {
                                    Binder.restoreCallingIdentity(clearCallingIdentity);
                                    throw th2;
                                }
                            } else {
                                z4 = isEmpty;
                            }
                            z = false;
                            z2 = z4;
                            str = clientName;
                            obj = obj2;
                            i2 = facing;
                            this.mCameraUsageHistory.add(new CameraUsageEvent(cameraId, facing, clientName, apiLevel, isNdk, 2, latencyMs, sessionType, deviceErrorFlag, logId, sessionIndex));
                        } else {
                            z = false;
                            z2 = isEmpty;
                            str = clientName;
                            obj = obj2;
                            i2 = facing;
                        }
                        if (this.mCameraUsageHistory.size() > 20) {
                            dumpUsageEvents();
                        }
                    } else {
                        z = false;
                        z2 = isEmpty;
                        str = clientName;
                        obj = obj2;
                        str2 = cameraId;
                        i = facing;
                    }
                    int i5 = i2;
                    str2 = cameraId;
                    i = i5;
                } else {
                    z = false;
                    z2 = isEmpty;
                    str = clientName;
                    obj = obj2;
                    AudioManager audioManager = (AudioManager) getContext().getSystemService(AudioManager.class);
                    if (audioManager != null) {
                        audioManager.setParameters("cameraFacing=" + (facing == 0 ? "back" : "front"));
                    }
                    str2 = cameraId;
                    i = facing;
                    this.mCameraUsageHistory.add(new CameraUsageEvent(cameraId, facing, str, apiLevel, isNdk, 1, latencyMs, sessionType, deviceErrorFlag, logId, sessionIndex));
                }
                boolean isEmpty2 = this.mActiveCameraUsage.isEmpty();
                if ((this.mNotifyNfc && z2 != isEmpty2 && i == 0) || isNeedDisableNfc(i)) {
                    if (newCameraState == 1 && isNeedDisableNfc(i)) {
                        isEmpty2 = z;
                    }
                    this.mCameraServiceProxyExt.setNfcSwitchState(isEmpty2);
                    notifyNfcService(isEmpty2);
                }
                this.mCameraServiceProxyExt.extendNotifyCameraState(newCameraState, str, i, str2);
                return;
            } catch (Throwable th3) {
                th = th3;
                throw th;
            }
        }
        throw th;
    }

    private boolean isNeedDisableNfc(int i) {
        return i == SystemProperties.getInt("ro.oplus.camera.facing.front.need.disable.nfc", -1) && i == 1;
    }

    private void notifyNfcService(boolean z) {
        IBinder binderService = getBinderService(NFC_SERVICE_BINDER_NAME);
        if (binderService == null) {
            Slog.w(TAG, "Could not connect to NFC service to notify it of camera state");
            return;
        }
        try {
            INfcAdapter.Stub.asInterface(binderService).setReaderMode(nfcInterfaceToken, (IAppCallback) null, z ? 0 : 4096, (Bundle) null);
        } catch (RemoteException e) {
            Slog.w(TAG, "Could not notify NFC service, remote exception: " + e);
        }
    }

    private static int[] toArray(Collection<Integer> collection) {
        int[] iArr = new int[collection.size()];
        Iterator<Integer> it = collection.iterator();
        int i = 0;
        while (it.hasNext()) {
            iArr[i] = it.next().intValue();
            i++;
        }
        return iArr;
    }

    public ICameraServiceProxyWrapper getWrapper() {
        return this.mCameraServiceProxyWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class CameraServiceProxyWrapper implements ICameraServiceProxyWrapper {
        private CameraServiceProxyWrapper() {
        }

        @Override // com.android.server.camera.ICameraServiceProxyWrapper
        public Handler getHandler() {
            return CameraServiceProxy.this.mHandler;
        }
    }
}
