package com.android.server.bluetooth;

import android.annotation.RequiresPermission;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.BroadcastOptions;
import android.app.admin.DevicePolicyManager;
import android.app.compat.CompatChanges;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.IBluetooth;
import android.bluetooth.IBluetoothCallback;
import android.bluetooth.IBluetoothGatt;
import android.bluetooth.IBluetoothManager;
import android.bluetooth.IBluetoothManagerCallback;
import android.bluetooth.IBluetoothProfileServiceConnection;
import android.bluetooth.IBluetoothStateChangeCallback;
import android.content.AttributionSource;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.permission.PermissionManager;
import android.provider.Settings;
import android.sysprop.BluetoothProperties;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.proto.ProtoOutputStream;
import com.android.bluetooth.BluetoothStatsLog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.modules.utils.SynchronousResultReceiver;
import com.android.server.IDeviceIdleControllerExt;
import com.android.server.backup.BackupManagerConstants;
import com.android.server.clipboard.ClipboardService;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BluetoothManagerService extends IBluetoothManager.Stub {
    private static final int ACTIVE_LOG_MAX_SIZE = 20;
    private static final int ADD_PROXY_DELAY_MS = 100;
    private static final int BLUETOOTH_OFF = 0;
    private static final int BLUETOOTH_OFF_APM = 0;

    @VisibleForTesting
    static final int BLUETOOTH_ON_AIRPLANE = 2;
    private static final int BLUETOOTH_ON_APM = 1;
    private static final int BLUETOOTH_ON_BLUETOOTH = 1;
    private static final String BLUETOOTH_PRIVILEGED = "android.permission.BLUETOOTH_PRIVILEGED";
    private static final int BOOT_COMPLETE_AUTO_ENABLE_DELAY = 0;
    private static final int CRASH_LOG_MAX_SIZE = 100;
    private static final int DEFAULT_REBIND_COUNT = 3;
    private static final int DELAY_BEFORE_RESTART_DUE_TO_INIT_FLAGS_CHANGED_MS = 300;
    private static final int DELAY_FOR_RETRY_INIT_FLAG_CHECK_MS = 86400000;
    private static final int ENABLE_DISABLE_DELAY_MS = 300;
    private static final int ERROR_RESTART_TIME_MS = 3000;
    private static final int FLAGS_SYSTEM_APP = 129;
    private static final int MAX_ERROR_RESTART_RETRIES = 6;
    private static final int MAX_WAIT_FOR_ENABLE_DISABLE_RETRIES = 10;
    private static final int MESSAGE_ADD_PROXY_DELAYED = 400;
    private static final int MESSAGE_BIND_PROFILE_SERVICE = 401;
    private static final int MESSAGE_BLUETOOTH_SERVICE_CONNECTED = 40;
    private static final int MESSAGE_BLUETOOTH_SERVICE_DISCONNECTED = 41;
    private static final int MESSAGE_BLUETOOTH_STATE_CHANGE = 60;

    @VisibleForTesting
    static final int MESSAGE_DISABLE = 2;
    private static final int MESSAGE_ENABLE = 1;
    private static final int MESSAGE_GET_NAME_AND_ADDRESS = 200;
    private static final int MESSAGE_HANDLE_DISABLE_DELAYED = 4;
    private static final int MESSAGE_HANDLE_ENABLE_DELAYED = 3;
    private static final int MESSAGE_INIT_FLAGS_CHANGED = 600;
    private static final int MESSAGE_REGISTER_STATE_CHANGE_CALLBACK = 30;
    private static final int MESSAGE_RESTART_BLUETOOTH_SERVICE = 42;
    private static final int MESSAGE_RESTORE_USER_SETTING = 500;
    private static final int MESSAGE_TIMEOUT_BIND = 100;
    private static final int MESSAGE_UNREGISTER_STATE_CHANGE_CALLBACK = 31;
    private static final int MESSAGE_USER_SWITCHED = 300;
    private static final int MESSAGE_USER_UNLOCKED = 301;
    private static final String PACKAGE_NAME_OSHARE = "com.coloros.oshare";
    private static final int RESTORE_SETTING_TO_OFF = 0;
    private static final int RESTORE_SETTING_TO_ON = 1;
    static final long RESTRICT_ENABLE_DISABLE = 218493289;
    private static final int SERVICE_IBLUETOOTH = 1;
    private static final int SERVICE_IBLUETOOTHGATT = 2;
    private static final int SERVICE_RESTART_TIME_MS = 400;

    @VisibleForTesting
    static final String SETTINGS_SATELLITE_MODE_ENABLED = "satellite_mode_enabled";

    @VisibleForTesting
    static final String SETTINGS_SATELLITE_MODE_RADIOS = "satellite_mode_radios";
    private static final String TAG = "BluetoothManagerService";
    private static final int TIMEOUT_BIND_MS = 3000;
    private static final int USER_SWITCHED_TIME_MS = 200;
    private boolean DBG;
    private final LinkedList<ActiveLog> mActiveLogs;
    private String mAddress;
    private AppOpsManager mAppOps;
    private boolean mBinding;
    private Map<IBinder, ClientDeathRecipient> mBleApps;

    @GuardedBy({"mBluetoothLock"})
    private IBluetooth mBluetooth;
    private BluetoothAirplaneModeListener mBluetoothAirplaneModeListener;
    private IBinder mBluetoothBinder;
    private final IBluetoothCallback mBluetoothCallback;
    private BluetoothDeviceConfigListener mBluetoothDeviceConfigListener;
    private IBluetoothGatt mBluetoothGatt;
    private final HandlerThread mBluetoothHandlerThread;
    private final ReentrantReadWriteLock mBluetoothLock;
    private BluetoothModeChangeHelper mBluetoothModeChangeHelper;
    private BluetoothNotificationManager mBluetoothNotificationManager;
    private BluetoothSatelliteModeListener mBluetoothSatelliteModeListener;
    private BluetoothManagerServiceWrapper mBmsWrapper;
    private final RemoteCallbackList<IBluetoothManagerCallback> mCallbacks;
    private BluetoothServiceConnection mConnection;
    private final ContentResolver mContentResolver;
    private final Context mContext;
    private final LinkedList<Long> mCrashTimestamps;
    private int mCrashes;
    private boolean mEnable;
    private boolean mEnableExternal;
    private int mErrorRecoveryRetryCounter;
    private final BluetoothHandler mHandler;
    private boolean mIsHearingAidProfileSupported;
    private long mLastEnabledTime;
    private String mName;
    IOplusBluetoothManagerServiceExt mOplusBms;
    private final Map<Integer, ProfileServiceConnections> mProfileServices;
    private boolean mQuietEnable;
    private boolean mQuietEnableExternal;
    private final BroadcastReceiver mReceiver;
    private boolean mShutdownInProgress;
    private int mState;
    private final RemoteCallbackList<IBluetoothStateChangeCallback> mStateChangeCallbacks;
    private List<Integer> mSupportedProfileList;
    private final int mSystemUiUid;
    private boolean mUnbinding;

    @GuardedBy({"mProfileServices"})
    private boolean mUnbindingAll;
    private final UserManager mUserManager;
    private static final Duration SYNC_CALLS_TIMEOUT = Duration.ofSeconds(3);
    private static final UserHandle USER_HANDLE_CURRENT_OR_SELF = UserHandle.of(-3);
    private static final UserHandle USER_HANDLE_NULL = UserHandle.of(-10000);
    private static final Object ON_AIRPLANE_MODE_CHANGED_TOKEN = new Object();
    private static final Object ON_SATELLITE_MODE_CHANGED_TOKEN = new Object();
    private static final Object ON_SWITCH_USER_TOKEN = new Object();

    private boolean isBleState(int i) {
        switch (i) {
            case 14:
            case 15:
            case 16:
                return true;
            default:
                return false;
        }
    }

    private static boolean isCallerRoot(int i) {
        return i == 0;
    }

    private static boolean isCallerShell(int i) {
        return i == 2000;
    }

    private static boolean isCallerSystem(int i) {
        return i == 1000;
    }

    @VisibleForTesting
    public void onInitFlagsChanged() {
    }

    private static Duration getSyncTimeout() {
        return SYNC_CALLS_TIMEOUT;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String timeToLog(long j) {
        return DateTimeFormatter.ofPattern("MM-dd HH:mm:ss.SSS").withZone(ZoneId.systemDefault()).format(Instant.ofEpochMilli(j));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class ActiveLog {
        private boolean mEnable;
        private String mPackageName;
        private int mReason;
        private long mTimestamp;

        ActiveLog(int i, String str, boolean z, long j) {
            this.mReason = i;
            this.mPackageName = str;
            this.mEnable = z;
            this.mTimestamp = j;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(BluetoothManagerService.timeToLog(this.mTimestamp));
            sb.append(this.mEnable ? "  Enabled " : " Disabled ");
            sb.append(" due to ");
            sb.append(BluetoothManagerService.getEnableDisableReasonString(this.mReason));
            sb.append(" by ");
            sb.append(this.mPackageName);
            return sb.toString();
        }

        void dump(ProtoOutputStream protoOutputStream) {
            protoOutputStream.write(1112396529665L, this.mTimestamp);
            protoOutputStream.write(1133871366146L, this.mEnable);
            protoOutputStream.write(1138166333443L, this.mPackageName);
            protoOutputStream.write(1159641169924L, this.mReason);
        }
    }

    public void onUserRestrictionsChanged(UserHandle userHandle) {
        boolean hasUserRestrictionForUser = this.mUserManager.hasUserRestrictionForUser("no_bluetooth", userHandle);
        updateOppLauncherComponentState(userHandle, this.mUserManager.hasUserRestrictionForUser("no_bluetooth_sharing", userHandle) || hasUserRestrictionForUser);
        if (userHandle == UserHandle.SYSTEM && hasUserRestrictionForUser) {
            sendDisableMsg(3, this.mContext.getPackageName());
        }
    }

    public boolean onFactoryReset(AttributionSource attributionSource) {
        this.mContext.enforceCallingOrSelfPermission(BLUETOOTH_PRIVILEGED, "Need BLUETOOTH_PRIVILEGED permission");
        int state = getState();
        if ((state == 14 || state == 11 || state == 13) && !waitForState(Set.of(15, 12))) {
            return false;
        }
        clearBleApps();
        int state2 = getState();
        this.mBluetoothLock.readLock().lock();
        try {
            try {
            } catch (RemoteException | TimeoutException e) {
                Log.e(TAG, "Unable to shutdown Bluetooth", e);
                this.mBluetoothLock.readLock().unlock();
                Log.d(TAG, "run oplus onFactoryReset()");
            }
            if (this.mBluetooth == null) {
                this.mBluetoothLock.readLock().unlock();
                Log.d(TAG, "run oplus onFactoryReset()");
                return false;
            }
            if (state2 == 15) {
                addActiveLog(10, this.mContext.getPackageName(), false);
                synchronousOnBrEdrDown(attributionSource);
                return true;
            }
            if (state2 != 12) {
                this.mBluetoothLock.readLock().unlock();
                Log.d(TAG, "run oplus onFactoryReset()");
                if (waitForState(Set.of(10)) || this.mBluetooth == null) {
                    this.mOplusBms.oplusFactoryReset();
                }
                return false;
            }
            addActiveLog(10, this.mContext.getPackageName(), false);
            synchronousDisable(attributionSource);
            this.mBluetoothLock.readLock().unlock();
            Log.d(TAG, "run oplus onFactoryReset()");
            if (waitForState(Set.of(10)) || this.mBluetooth == null) {
                this.mOplusBms.oplusFactoryReset();
            }
            return true;
        } finally {
            this.mBluetoothLock.readLock().unlock();
            Log.d(TAG, "run oplus onFactoryReset()");
            if (waitForState(Set.of(10)) || this.mBluetooth == null) {
                this.mOplusBms.oplusFactoryReset();
            }
        }
    }

    private int estimateBusyTime(int i) {
        if (i == 15 && isBluetoothPersistedStateOn()) {
            return 400;
        }
        if (i == 12 || i == 10 || i == 15) {
            return (this.mHandler.hasMessages(1) || this.mHandler.hasMessages(2) || this.mHandler.hasMessages(3) || this.mHandler.hasMessages(4) || this.mHandler.hasMessages(42) || this.mHandler.hasMessages(100) || this.mHandler.hasMessages(MESSAGE_BIND_PROFILE_SERVICE)) ? 400 : 0;
        }
        return 100;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: delayModeChangedIfNeeded, reason: merged with bridge method [inline-methods] */
    public void lambda$delayModeChangedIfNeeded$0(final Object obj, final Runnable runnable, final String str) {
        this.mHandler.removeCallbacksAndMessages(obj);
        int state = getState();
        int estimateBusyTime = estimateBusyTime(state);
        Log.d(TAG, "delayModeChangedIfNeeded(" + str + "): state=" + BluetoothAdapter.nameForState(state) + ", isAirplaneModeOn()=" + isAirplaneModeOn() + ", isSatelliteModeSensitive()=" + isSatelliteModeSensitive() + ", isSatelliteModeOn()=" + isSatelliteModeOn() + ", delayed=" + estimateBusyTime + "ms");
        if (estimateBusyTime > 0) {
            this.mHandler.postDelayed(new Runnable() { // from class: com.android.server.bluetooth.BluetoothManagerService$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    BluetoothManagerService.this.lambda$delayModeChangedIfNeeded$0(obj, runnable, str);
                }
            }, obj, estimateBusyTime);
        } else {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @RequiresPermission(BLUETOOTH_PRIVILEGED)
    public void onAirplaneModeChanged() {
        lambda$delayModeChangedIfNeeded$0(ON_AIRPLANE_MODE_CHANGED_TOKEN, new Runnable() { // from class: com.android.server.bluetooth.BluetoothManagerService$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                BluetoothManagerService.this.lambda$onAirplaneModeChanged$1();
            }
        }, "onAirplaneModeChanged");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @RequiresPermission(BLUETOOTH_PRIVILEGED)
    public void onSatelliteModeChanged() {
        lambda$delayModeChangedIfNeeded$0(ON_SATELLITE_MODE_CHANGED_TOKEN, new Runnable() { // from class: com.android.server.bluetooth.BluetoothManagerService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                BluetoothManagerService.this.lambda$onSatelliteModeChanged$2();
            }
        }, "onSatelliteModeChanged");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @RequiresPermission(BLUETOOTH_PRIVILEGED)
    public void onSwitchUser(final UserHandle userHandle) {
        lambda$delayModeChangedIfNeeded$0(ON_SWITCH_USER_TOKEN, new Runnable() { // from class: com.android.server.bluetooth.BluetoothManagerService$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                BluetoothManagerService.this.lambda$onSwitchUser$3(userHandle);
            }
        }, "onSwitchUser");
    }

    /* JADX INFO: Access modifiers changed from: private */
    @RequiresPermission(BLUETOOTH_PRIVILEGED)
    /* renamed from: handleAirplaneModeChanged, reason: merged with bridge method [inline-methods] */
    public void lambda$onAirplaneModeChanged$1() {
        ReentrantReadWriteLock.ReadLock readLock;
        synchronized (this) {
            if (isBluetoothPersistedStateOn()) {
                if (isAirplaneModeOn()) {
                    persistBluetoothSetting(2);
                } else {
                    persistBluetoothSetting(1);
                }
            }
            try {
                try {
                    this.mBluetoothLock.readLock().lock();
                    int synchronousGetState = synchronousGetState();
                    this.mBluetoothLock.readLock().unlock();
                    Log.d(TAG, "Airplane Mode change - current state:  " + BluetoothAdapter.nameForState(synchronousGetState) + ", isAirplaneModeOn()=" + isAirplaneModeOn());
                    if (isAirplaneModeOn()) {
                        clearBleApps();
                        if (synchronousGetState == 15) {
                            this.mBluetoothLock.readLock().lock();
                            try {
                                try {
                                    if (this.mBluetooth != null) {
                                        addActiveLog(2, this.mContext.getPackageName(), false);
                                        synchronousOnBrEdrDown(this.mContext.getAttributionSource());
                                        this.mEnable = false;
                                        this.mEnableExternal = false;
                                    }
                                    readLock = this.mBluetoothLock.readLock();
                                } finally {
                                }
                            } catch (RemoteException | TimeoutException e) {
                                Log.e(TAG, "Unable to call onBrEdrDown", e);
                                readLock = this.mBluetoothLock.readLock();
                            }
                            readLock.unlock();
                        } else if (synchronousGetState == 12) {
                            sendDisableMsg(2, this.mContext.getPackageName());
                        }
                    } else if (this.mEnableExternal) {
                        sendEnableMsg(this.mQuietEnableExternal, 2, this.mContext.getPackageName());
                    }
                } catch (RemoteException | TimeoutException e2) {
                    Log.e(TAG, "Unable to call getState", e2);
                }
            } finally {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: handleSatelliteModeChanged, reason: merged with bridge method [inline-methods] */
    public void lambda$onSatelliteModeChanged$2() {
        if (shouldBluetoothBeOn() && getState() != 12) {
            sendEnableMsg(this.mQuietEnableExternal, 12, this.mContext.getPackageName());
        } else {
            if (shouldBluetoothBeOn() || getState() == 10) {
                return;
            }
            sendDisableMsg(12, this.mContext.getPackageName());
        }
    }

    private boolean shouldBluetoothBeOn() {
        if (!isBluetoothPersistedStateOn()) {
            Log.d(TAG, "shouldBluetoothBeOn: User want BT off.");
            return false;
        }
        if (isSatelliteModeOn()) {
            Log.d(TAG, "shouldBluetoothBeOn: BT should be off as satellite mode is on.");
            return false;
        }
        if (isAirplaneModeOn() && isBluetoothPersistedStateOnAirplane()) {
            Log.d(TAG, "shouldBluetoothBeOn: BT should be off as airplaneMode is on.");
            return false;
        }
        Log.d(TAG, "shouldBluetoothBeOn: BT should be on.");
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BluetoothManagerService(Context context) {
        this.DBG = !SystemProperties.getBoolean("ro.build.release_type", false) || SystemProperties.getBoolean("persist.sys.assert.panic", false);
        this.mOplusBms = null;
        this.mBluetoothLock = new ReentrantReadWriteLock();
        this.mSupportedProfileList = new ArrayList();
        this.mQuietEnable = false;
        this.mShutdownInProgress = false;
        this.mActiveLogs = new LinkedList<>();
        this.mCrashTimestamps = new LinkedList<>();
        this.mBleApps = new ConcurrentHashMap();
        this.mProfileServices = new HashMap();
        this.mUnbindingAll = false;
        this.mBluetoothCallback = new IBluetoothCallback.Stub() { // from class: com.android.server.bluetooth.BluetoothManagerService.1
            public void onBluetoothStateChange(int i, int i2) throws RemoteException {
                BluetoothManagerService.this.mHandler.sendMessage(BluetoothManagerService.this.mHandler.obtainMessage(60, i, i2));
            }
        };
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.bluetooth.BluetoothManagerService.2
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r5v8, types: [java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock] */
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                if ("android.bluetooth.adapter.action.LOCAL_NAME_CHANGED".equals(action)) {
                    String stringExtra = intent.getStringExtra("android.bluetooth.adapter.extra.LOCAL_NAME");
                    if (BluetoothManagerService.this.DBG) {
                        Log.d(BluetoothManagerService.TAG, "Bluetooth Adapter name changed to " + stringExtra + " by " + BluetoothManagerService.this.mContext.getPackageName());
                    }
                    if (stringExtra != null) {
                        BluetoothManagerService.this.storeNameAndAddress(stringExtra, null);
                        return;
                    }
                    return;
                }
                if ("android.bluetooth.adapter.action.BLUETOOTH_ADDRESS_CHANGED".equals(action)) {
                    String stringExtra2 = intent.getStringExtra("android.bluetooth.adapter.extra.BLUETOOTH_ADDRESS");
                    if (stringExtra2 != null) {
                        if (BluetoothManagerService.this.DBG) {
                            Log.d(BluetoothManagerService.TAG, "Bluetooth Adapter address changed to " + stringExtra2);
                        }
                        BluetoothManagerService.this.storeNameAndAddress(null, stringExtra2);
                        return;
                    }
                    if (BluetoothManagerService.this.DBG) {
                        Log.e(BluetoothManagerService.TAG, "No Bluetooth Adapter address parameter found");
                        return;
                    }
                    return;
                }
                if ("android.os.action.SETTING_RESTORED".equals(action)) {
                    if ("bluetooth_on".equals(intent.getStringExtra("setting_name"))) {
                        String stringExtra3 = intent.getStringExtra("previous_value");
                        String stringExtra4 = intent.getStringExtra("new_value");
                        if (BluetoothManagerService.this.DBG) {
                            Log.d(BluetoothManagerService.TAG, "ACTION_SETTING_RESTORED with BLUETOOTH_ON, prevValue=" + stringExtra3 + ", newValue=" + stringExtra4);
                        }
                        if (stringExtra4 == null || stringExtra3 == null || stringExtra3.equals(stringExtra4)) {
                            return;
                        }
                        BluetoothManagerService.this.mHandler.sendMessage(BluetoothManagerService.this.mHandler.obtainMessage(500, !stringExtra4.equals("0") ? 1 : 0, 0));
                        return;
                    }
                    return;
                }
                if ("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED".equals(action) || "android.bluetooth.hearingaid.profile.action.CONNECTION_STATE_CHANGED".equals(action) || "android.bluetooth.action.LE_AUDIO_CONNECTION_STATE_CHANGED".equals(action)) {
                    int intExtra = intent.getIntExtra("android.bluetooth.profile.extra.STATE", 2);
                    if (BluetoothManagerService.this.mHandler.hasMessages(600) && intExtra == 0 && !BluetoothManagerService.this.mBluetoothModeChangeHelper.isMediaProfileConnected()) {
                        Log.i(BluetoothManagerService.TAG, "Device disconnected, reactivating pending flag changes");
                        BluetoothManagerService.this.onInitFlagsChanged();
                        return;
                    }
                    return;
                }
                if (action.equals("android.intent.action.ACTION_SHUTDOWN")) {
                    Log.i(BluetoothManagerService.TAG, "Device is shutting down.");
                    BluetoothManagerService.this.mShutdownInProgress = true;
                    BluetoothManagerService.this.mBluetoothLock.readLock().lock();
                    try {
                        try {
                            BluetoothManagerService.this.mEnable = false;
                            BluetoothManagerService.this.mEnableExternal = false;
                            if (BluetoothManagerService.this.mBluetooth != null && BluetoothManagerService.this.mState == 15) {
                                BluetoothManagerService bluetoothManagerService = BluetoothManagerService.this;
                                bluetoothManagerService.synchronousOnBrEdrDown(bluetoothManagerService.mContext.getAttributionSource());
                            } else if (BluetoothManagerService.this.mBluetooth != null && BluetoothManagerService.this.mState == 12) {
                                BluetoothManagerService bluetoothManagerService2 = BluetoothManagerService.this;
                                bluetoothManagerService2.synchronousDisable(bluetoothManagerService2.mContext.getAttributionSource());
                            }
                        } catch (RemoteException | TimeoutException e) {
                            Log.e(BluetoothManagerService.TAG, "Unable to shutdown Bluetooth", e);
                        }
                    } finally {
                        BluetoothManagerService.this.mBluetoothLock.readLock().unlock();
                    }
                }
            }
        };
        this.mReceiver = broadcastReceiver;
        this.mConnection = new BluetoothServiceConnection();
        this.mBmsWrapper = new BluetoothManagerServiceWrapper();
        HandlerThread createHandlerThread = BluetoothServerProxy.getInstance().createHandlerThread(TAG);
        this.mBluetoothHandlerThread = createHandlerThread;
        createHandlerThread.start();
        this.mHandler = BluetoothServerProxy.getInstance().newBluetoothHandler(new BluetoothHandler(createHandlerThread.getLooper()));
        this.mContext = context;
        this.mCrashes = 0;
        this.mBluetooth = null;
        this.mBluetoothBinder = null;
        this.mBluetoothGatt = null;
        this.mBinding = false;
        this.mUnbinding = false;
        this.mEnable = false;
        this.mState = 10;
        this.mQuietEnableExternal = false;
        this.mEnableExternal = false;
        this.mAddress = null;
        this.mName = null;
        this.mErrorRecoveryRetryCounter = 0;
        ContentResolver contentResolver = context.getContentResolver();
        this.mContentResolver = contentResolver;
        registerForBleScanModeChange();
        this.mCallbacks = new RemoteCallbackList<>();
        this.mStateChangeCallbacks = new RemoteCallbackList<>();
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
        this.mBluetoothNotificationManager = new BluetoothNotificationManager(context);
        if (!isBleSupported(context)) {
            this.mIsHearingAidProfileSupported = false;
        } else {
            this.mIsHearingAidProfileSupported = ((Boolean) BluetoothProperties.isProfileAshaCentralEnabled().orElse(Boolean.valueOf((isAutomotive(context) || isWatch(context) || isTv(context)) ? false : true))).booleanValue();
        }
        String str = SystemProperties.get("persist.sys.fflag.override.settings_bluetooth_hearing_aid");
        if (!TextUtils.isEmpty(str)) {
            boolean parseBoolean = Boolean.parseBoolean(str);
            Log.v(TAG, "set feature flag HEARING_AID_SETTINGS to " + parseBoolean);
            if (parseBoolean && !this.mIsHearingAidProfileSupported) {
                this.mIsHearingAidProfileSupported = true;
            }
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.bluetooth.adapter.action.LOCAL_NAME_CHANGED");
        intentFilter.addAction("android.bluetooth.adapter.action.BLUETOOTH_ADDRESS_CHANGED");
        intentFilter.addAction("android.os.action.SETTING_RESTORED");
        intentFilter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
        intentFilter.addAction("android.bluetooth.hearingaid.profile.action.CONNECTION_STATE_CHANGED");
        intentFilter.addAction("android.intent.action.ACTION_SHUTDOWN");
        intentFilter.setPriority(1000);
        context.registerReceiver(broadcastReceiver, intentFilter);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.os.action.USER_RESTRICTIONS_CHANGED");
        intentFilter2.addAction("android.intent.action.USER_SWITCHED");
        intentFilter2.setPriority(1000);
        context.registerReceiverForAllUsers(new BroadcastReceiver() { // from class: com.android.server.bluetooth.BluetoothManagerService.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                action.hashCode();
                if (!action.equals("android.intent.action.USER_SWITCHED")) {
                    if (action.equals("android.os.action.USER_RESTRICTIONS_CHANGED")) {
                        BluetoothManagerService.this.onUserRestrictionsChanged(getSendingUser());
                        return;
                    } else {
                        Log.e(BluetoothManagerService.TAG, "Unknown broadcast received in BluetoothManagerService receiver registered across all users");
                        return;
                    }
                }
                int intExtra = intent.getIntExtra("android.intent.extra.user_handle", 0);
                IOplusBluetoothManagerServiceExt iOplusBluetoothManagerServiceExt = BluetoothManagerService.this.mOplusBms;
                if (iOplusBluetoothManagerServiceExt == null || !iOplusBluetoothManagerServiceExt.oplusPropagateForegroundUserId(intExtra)) {
                    BluetoothManagerService.this.propagateForegroundUserId(intExtra);
                }
            }
        }, intentFilter2, null, null);
        loadStoredNameAndAddress();
        if (isBluetoothPersistedStateOn()) {
            if (this.DBG) {
                Log.d(TAG, "Startup: Bluetooth persisted state is ON.");
            }
            this.mEnableExternal = true;
        }
        String string = Settings.Global.getString(contentResolver, "airplane_mode_radios");
        if (string == null || string.contains("bluetooth")) {
            this.mBluetoothAirplaneModeListener = new BluetoothAirplaneModeListener(this, createHandlerThread.getLooper(), context, this.mBluetoothNotificationManager);
        }
        int i = -1;
        try {
            i = context.createContextAsUser(UserHandle.SYSTEM, 0).getPackageManager().getPackageUid("com.android.systemui", PackageManager.PackageInfoFlags.of(1048576L));
            Log.d(TAG, "Detected SystemUiUid: " + Integer.toString(i));
        } catch (PackageManager.NameNotFoundException unused) {
            Log.w(TAG, "Unable to resolve SystemUI's UID.");
        }
        this.mSystemUiUid = i;
        this.mBluetoothSatelliteModeListener = new BluetoothSatelliteModeListener(this, this.mBluetoothHandlerThread.getLooper(), context);
        IOplusBluetoothManagerServiceExt iOplusBluetoothManagerServiceExt = (IOplusBluetoothManagerServiceExt) ExtLoader.type(IOplusBluetoothManagerServiceExt.class).base(this).create();
        this.mOplusBms = iOplusBluetoothManagerServiceExt;
        iOplusBluetoothManagerServiceExt.setContext(context);
    }

    private boolean isAirplaneModeOn() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
    }

    private boolean isSatelliteModeSensitive() {
        String string = Settings.Global.getString(this.mContext.getContentResolver(), SETTINGS_SATELLITE_MODE_RADIOS);
        return string != null && string.contains("bluetooth");
    }

    private boolean isSatelliteModeOn() {
        return isSatelliteModeSensitive() && Settings.Global.getInt(this.mContext.getContentResolver(), SETTINGS_SATELLITE_MODE_ENABLED, 0) == 1;
    }

    private boolean isApmEnhancementOn() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), BluetoothAirplaneModeListener.APM_ENHANCEMENT, 0) == 1;
    }

    private boolean supportBluetoothPersistedState() {
        return ((Boolean) BluetoothProperties.isSupportPersistedStateEnabled().orElse(Boolean.TRUE)).booleanValue();
    }

    private boolean isBluetoothPersistedStateOn() {
        if (!supportBluetoothPersistedState()) {
            return false;
        }
        int i = Settings.Global.getInt(this.mContentResolver, "bluetooth_on", -1);
        if (this.DBG) {
            Log.d(TAG, "Bluetooth persisted state: " + i);
        }
        return i != 0;
    }

    private boolean isBluetoothPersistedStateOnAirplane() {
        if (!supportBluetoothPersistedState()) {
            return false;
        }
        int i = Settings.Global.getInt(this.mContentResolver, "bluetooth_on", -1);
        if (this.DBG) {
            Log.d(TAG, "Bluetooth persisted state: " + i);
        }
        return i == 2;
    }

    private boolean isBluetoothPersistedStateOnBluetooth() {
        return supportBluetoothPersistedState() && Settings.Global.getInt(this.mContentResolver, "bluetooth_on", 1) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void persistBluetoothSetting(int i) {
        if (this.DBG) {
            Log.d(TAG, "Persisting Bluetooth Setting: " + i);
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Settings.Global.putInt(this.mContext.getContentResolver(), "bluetooth_on", i);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void setSettingsSecureInt(String str, int i) {
        if (this.DBG) {
            Log.d(TAG, "Persisting Settings Secure Int: " + str + "=" + i);
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Settings.Secure.putInt(this.mContext.createContextAsUser(UserHandle.of(ActivityManager.getCurrentUser()), 0).getContentResolver(), str, i);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private boolean isFirstTimeNotification(String str) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return Settings.Secure.getInt(this.mContext.createContextAsUser(UserHandle.of(ActivityManager.getCurrentUser()), 0).getContentResolver(), str, 0) == 0;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNameAndAddressSet() {
        String str = this.mName;
        return str != null && this.mAddress != null && str.length() > 0 && this.mAddress.length() > 0;
    }

    private void loadStoredNameAndAddress() {
        if (this.DBG) {
            Log.d(TAG, "Loading stored name and address");
        }
        if (((Boolean) BluetoothProperties.isAdapterAddressValidationEnabled().orElse(Boolean.FALSE)).booleanValue() && Settings.Secure.getInt(this.mContentResolver, "bluetooth_addr_valid", 0) == 0) {
            if (this.DBG) {
                Log.d(TAG, "invalid bluetooth name and address stored");
                return;
            }
            return;
        }
        this.mName = BluetoothServerProxy.getInstance().settingsSecureGetString(this.mContentResolver, "bluetooth_name");
        this.mAddress = BluetoothServerProxy.getInstance().settingsSecureGetString(this.mContentResolver, "bluetooth_address");
        if (this.DBG) {
            Log.d(TAG, "Stored bluetooth Name=" + this.mName + ",Address=" + this.mAddress);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void storeNameAndAddress(String str, String str2) {
        if (str != null) {
            Settings.Secure.putString(this.mContentResolver, "bluetooth_name", str);
            this.mName = str;
            if (this.DBG) {
                Log.d(TAG, "Stored Bluetooth name: " + Settings.Secure.getString(this.mContentResolver, "bluetooth_name"));
            }
        }
        if (str2 != null) {
            Settings.Secure.putString(this.mContentResolver, "bluetooth_address", str2);
            this.mAddress = str2;
            if (this.DBG) {
                Log.d(TAG, "Stored Bluetoothaddress: " + Settings.Secure.getString(this.mContentResolver, "bluetooth_address"));
            }
        }
        if (str == null || str2 == null) {
            return;
        }
        Settings.Secure.putInt(this.mContentResolver, "bluetooth_addr_valid", 1);
    }

    public IBluetooth registerAdapter(IBluetoothManagerCallback iBluetoothManagerCallback) {
        if (iBluetoothManagerCallback == null) {
            Log.w(TAG, "Callback is null in registerAdapter");
            return null;
        }
        synchronized (this.mCallbacks) {
            this.mCallbacks.register(iBluetoothManagerCallback);
        }
        return this.mBluetooth;
    }

    public void unregisterAdapter(IBluetoothManagerCallback iBluetoothManagerCallback) {
        if (iBluetoothManagerCallback == null) {
            Log.w(TAG, "Callback is null in unregisterAdapter");
            return;
        }
        synchronized (this.mCallbacks) {
            this.mCallbacks.unregister(iBluetoothManagerCallback);
        }
    }

    public void registerStateChangeCallback(IBluetoothStateChangeCallback iBluetoothStateChangeCallback) {
        if (iBluetoothStateChangeCallback == null) {
            Log.w(TAG, "registerStateChangeCallback: Callback is null!");
            return;
        }
        Message obtainMessage = this.mHandler.obtainMessage(30);
        obtainMessage.obj = iBluetoothStateChangeCallback;
        this.mHandler.sendMessage(obtainMessage);
    }

    public void unregisterStateChangeCallback(IBluetoothStateChangeCallback iBluetoothStateChangeCallback) {
        if (iBluetoothStateChangeCallback == null) {
            Log.w(TAG, "unregisterStateChangeCallback: Callback is null!");
            return;
        }
        Message obtainMessage = this.mHandler.obtainMessage(31);
        obtainMessage.obj = iBluetoothStateChangeCallback;
        this.mHandler.sendMessage(obtainMessage);
    }

    public boolean isEnabled() {
        return getState() == 12;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mBluetoothLock"})
    public boolean synchronousDisable(AttributionSource attributionSource) throws RemoteException, TimeoutException {
        if (this.mBluetooth == null) {
            return false;
        }
        SynchronousResultReceiver synchronousResultReceiver = SynchronousResultReceiver.get();
        this.mBluetooth.disable(attributionSource, synchronousResultReceiver);
        return ((Boolean) synchronousResultReceiver.awaitResultNoInterrupt(getSyncTimeout()).getValue(Boolean.FALSE)).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mBluetoothLock"})
    public boolean synchronousEnable(boolean z, AttributionSource attributionSource) throws RemoteException, TimeoutException {
        if (this.mBluetooth == null) {
            return false;
        }
        SynchronousResultReceiver synchronousResultReceiver = SynchronousResultReceiver.get();
        this.mBluetooth.enable(z, attributionSource, synchronousResultReceiver);
        return ((Boolean) synchronousResultReceiver.awaitResultNoInterrupt(getSyncTimeout()).getValue(Boolean.FALSE)).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mBluetoothLock"})
    public String synchronousGetAddress(AttributionSource attributionSource) throws RemoteException, TimeoutException {
        if (this.mBluetooth == null) {
            return null;
        }
        SynchronousResultReceiver synchronousResultReceiver = SynchronousResultReceiver.get();
        this.mBluetooth.getAddress(attributionSource, synchronousResultReceiver);
        return (String) synchronousResultReceiver.awaitResultNoInterrupt(getSyncTimeout()).getValue((Object) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mBluetoothLock"})
    public String synchronousGetName(AttributionSource attributionSource) throws RemoteException, TimeoutException {
        if (this.mBluetooth == null) {
            return null;
        }
        SynchronousResultReceiver synchronousResultReceiver = SynchronousResultReceiver.get();
        this.mBluetooth.getName(attributionSource, synchronousResultReceiver);
        return (String) synchronousResultReceiver.awaitResultNoInterrupt(getSyncTimeout()).getValue((Object) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mBluetoothLock"})
    public int synchronousGetState() throws RemoteException, TimeoutException {
        if (this.mBluetooth == null) {
            return 10;
        }
        SynchronousResultReceiver synchronousResultReceiver = SynchronousResultReceiver.get();
        this.mBluetooth.getState(synchronousResultReceiver);
        return ((Integer) synchronousResultReceiver.awaitResultNoInterrupt(getSyncTimeout()).getValue(10)).intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mBluetoothLock"})
    public void synchronousOnBrEdrDown(AttributionSource attributionSource) throws RemoteException, TimeoutException {
        if (this.mBluetooth == null) {
            return;
        }
        SynchronousResultReceiver synchronousResultReceiver = SynchronousResultReceiver.get();
        this.mBluetooth.onBrEdrDown(attributionSource, synchronousResultReceiver);
        synchronousResultReceiver.awaitResultNoInterrupt(getSyncTimeout()).getValue((Object) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mBluetoothLock"})
    public void synchronousOnLeServiceUp(AttributionSource attributionSource) throws RemoteException, TimeoutException {
        if (this.mBluetooth == null) {
            return;
        }
        SynchronousResultReceiver synchronousResultReceiver = SynchronousResultReceiver.get();
        this.mBluetooth.onLeServiceUp(attributionSource, synchronousResultReceiver);
        synchronousResultReceiver.awaitResultNoInterrupt(getSyncTimeout()).getValue((Object) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mBluetoothLock"})
    public void synchronousRegisterCallback(IBluetoothCallback iBluetoothCallback, AttributionSource attributionSource) throws RemoteException, TimeoutException {
        if (this.mBluetooth == null) {
            return;
        }
        SynchronousResultReceiver synchronousResultReceiver = SynchronousResultReceiver.get();
        this.mBluetooth.registerCallback(iBluetoothCallback, attributionSource, synchronousResultReceiver);
        synchronousResultReceiver.awaitResultNoInterrupt(getSyncTimeout()).getValue((Object) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mBluetoothLock"})
    public void synchronousUnregisterCallback(IBluetoothCallback iBluetoothCallback, AttributionSource attributionSource) throws RemoteException, TimeoutException {
        if (this.mBluetooth == null) {
            return;
        }
        SynchronousResultReceiver synchronousResultReceiver = SynchronousResultReceiver.get();
        this.mBluetooth.unregisterCallback(iBluetoothCallback, attributionSource, synchronousResultReceiver);
        synchronousResultReceiver.awaitResultNoInterrupt(getSyncTimeout()).getValue((Object) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mBluetoothLock"})
    public List<Integer> synchronousGetSupportedProfiles(AttributionSource attributionSource) throws RemoteException, TimeoutException {
        ArrayList arrayList = new ArrayList();
        if (this.mBluetooth == null) {
            return arrayList;
        }
        SynchronousResultReceiver synchronousResultReceiver = SynchronousResultReceiver.get();
        this.mBluetooth.getSupportedProfiles(attributionSource, synchronousResultReceiver);
        long longValue = ((Long) synchronousResultReceiver.awaitResultNoInterrupt(getSyncTimeout()).getValue(0L)).longValue();
        for (int i = 0; i <= 39; i++) {
            if (((1 << i) & longValue) != 0) {
                arrayList.add(Integer.valueOf(i));
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v5, types: [java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock] */
    public void propagateForegroundUserId(int i) {
        this.mBluetoothLock.readLock().lock();
        try {
            try {
                IBluetooth iBluetooth = this.mBluetooth;
                if (iBluetooth != null) {
                    iBluetooth.setForegroundUserId(i, this.mContext.getAttributionSource());
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Unable to set foreground user id", e);
            }
        } finally {
            this.mBluetoothLock.readLock().unlock();
        }
    }

    public int getState() {
        if (!isCallerSystem(getCallingAppId()) && !checkIfCallerIsForegroundUser()) {
            Log.w(TAG, "getState(): report OFF for non-active and non system user");
            return 10;
        }
        this.mBluetoothLock.readLock().lock();
        try {
            try {
                if (this.mBluetooth != null) {
                    return synchronousGetState();
                }
            } catch (RemoteException | TimeoutException e) {
                Log.e(TAG, "getState()", e);
            }
            return 10;
        } finally {
            this.mBluetoothLock.readLock().unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class ClientDeathRecipient implements IBinder.DeathRecipient {
        private String mPackageName;

        ClientDeathRecipient(String str) {
            this.mPackageName = str;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            if (BluetoothManagerService.this.DBG) {
                Log.d(BluetoothManagerService.TAG, "Binder is dead - unregister " + this.mPackageName);
            }
            for (Map.Entry entry : BluetoothManagerService.this.mBleApps.entrySet()) {
                IBinder iBinder = (IBinder) entry.getKey();
                if (((ClientDeathRecipient) entry.getValue()).equals(this)) {
                    BluetoothManagerService.this.updateBleAppCount(iBinder, false, this.mPackageName);
                    return;
                }
            }
        }

        public String getPackageName() {
            return this.mPackageName;
        }
    }

    public boolean isBleScanAlwaysAvailable() {
        if (isAirplaneModeOn() && !this.mEnable) {
            return false;
        }
        try {
            return Settings.Global.getInt(this.mContentResolver, "ble_scan_always_enabled") != 0;
        } catch (Settings.SettingNotFoundException unused) {
            return false;
        }
    }

    public boolean isHearingAidProfileSupported() {
        return this.mIsHearingAidProfileSupported;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDeviceProvisioned() {
        return Settings.Global.getInt(this.mContentResolver, "device_provisioned", 0) != 0;
    }

    private void registerForProvisioningStateChange() {
        this.mContentResolver.registerContentObserver(Settings.Global.getUriFor("device_provisioned"), false, new ContentObserver(null) { // from class: com.android.server.bluetooth.BluetoothManagerService.4
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                if (!BluetoothManagerService.this.isDeviceProvisioned()) {
                    if (BluetoothManagerService.this.DBG) {
                        Log.d(BluetoothManagerService.TAG, "DEVICE_PROVISIONED setting changed, but device is not provisioned");
                    }
                } else if (BluetoothManagerService.this.mHandler.hasMessages(600)) {
                    Log.i(BluetoothManagerService.TAG, "Device provisioned, reactivating pending flag changes");
                    BluetoothManagerService.this.onInitFlagsChanged();
                }
            }
        });
    }

    private void registerForBleScanModeChange() {
        this.mContentResolver.registerContentObserver(Settings.Global.getUriFor("ble_scan_always_enabled"), false, new ContentObserver(null) { // from class: com.android.server.bluetooth.BluetoothManagerService.5
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r3v7, types: [java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock] */
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                if (BluetoothManagerService.this.isBleScanAlwaysAvailable()) {
                    return;
                }
                BluetoothManagerService.this.disableBleScanMode();
                BluetoothManagerService.this.clearBleApps();
                BluetoothManagerService.this.mBluetoothLock.readLock().lock();
                try {
                    try {
                        if (BluetoothManagerService.this.mBluetooth != null) {
                            BluetoothManagerService bluetoothManagerService = BluetoothManagerService.this;
                            bluetoothManagerService.addActiveLog(1, bluetoothManagerService.mContext.getPackageName(), false);
                            BluetoothManagerService bluetoothManagerService2 = BluetoothManagerService.this;
                            bluetoothManagerService2.synchronousOnBrEdrDown(bluetoothManagerService2.mContext.getAttributionSource());
                        }
                    } catch (RemoteException | TimeoutException e) {
                        Log.e(BluetoothManagerService.TAG, "error when disabling bluetooth", e);
                    }
                } finally {
                    BluetoothManagerService.this.mBluetoothLock.readLock().unlock();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disableBleScanMode() {
        this.mBluetoothLock.writeLock().lock();
        try {
            try {
                if (this.mBluetooth != null && synchronousGetState() != 12) {
                    if (this.DBG) {
                        Log.d(TAG, "Resetting the mEnable flag for clean disable");
                    }
                    this.mEnable = false;
                }
            } catch (RemoteException | TimeoutException e) {
                Log.e(TAG, "getState()", e);
            }
        } finally {
            this.mBluetoothLock.writeLock().unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int updateBleAppCount(IBinder iBinder, boolean z, String str) {
        ClientDeathRecipient clientDeathRecipient = this.mBleApps.get(iBinder);
        if (clientDeathRecipient == null && z) {
            ClientDeathRecipient clientDeathRecipient2 = new ClientDeathRecipient(str);
            try {
                iBinder.linkToDeath(clientDeathRecipient2, 0);
                this.mBleApps.put(iBinder, clientDeathRecipient2);
                if (this.DBG) {
                    Log.d(TAG, "Registered for death of " + str);
                }
            } catch (RemoteException unused) {
                throw new IllegalArgumentException("BLE app (" + str + ") already dead!");
            }
        } else if (!z && clientDeathRecipient != null) {
            try {
                iBinder.unlinkToDeath(clientDeathRecipient, 0);
            } catch (NoSuchElementException e) {
                Log.e(TAG, "updateBleAppCount(), Unable to unlinkToDeath", e);
            }
            this.mBleApps.remove(iBinder);
            if (this.DBG) {
                Log.d(TAG, "Unregistered for death of " + str);
            }
        }
        int size = this.mBleApps.size();
        this.mOplusBms.oplusDcsEventReport(4, size, 0, null, null);
        if (this.DBG) {
            Log.d(TAG, size + " registered Ble Apps");
        }
        return size;
    }

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    private boolean checkBluetoothPermissions(AttributionSource attributionSource, String str, boolean z) {
        if (isBluetoothDisallowed()) {
            if (this.DBG) {
                Log.d(TAG, "checkBluetoothPermissions: bluetooth disallowed");
            }
            return false;
        }
        int callingAppId = getCallingAppId();
        if (isCallerSystem(callingAppId) || isCallerShell(callingAppId) || isCallerRoot(callingAppId)) {
            return true;
        }
        checkPackage(attributionSource.getPackageName());
        if (!z || checkIfCallerIsForegroundUser()) {
            return checkConnectPermissionForDataDelivery(this.mContext, attributionSource, str);
        }
        Log.w(TAG, "Not allowed for non-active and non system user");
        return false;
    }

    public boolean enableBle(AttributionSource attributionSource, IBinder iBinder) throws RemoteException {
        String packageName = attributionSource.getPackageName();
        if (!checkBluetoothPermissions(attributionSource, "enableBle", false) || isAirplaneModeOn()) {
            if (this.DBG) {
                Log.d(TAG, "enableBle(): bluetooth disallowed");
            }
            return false;
        }
        if (this.DBG) {
            Log.d(TAG, "enableBle(" + packageName + "):  mBluetooth =" + this.mBluetooth + " mBinding = " + this.mBinding + " mState = " + BluetoothAdapter.nameForState(this.mState));
        }
        if (isSatelliteModeOn()) {
            Log.d(TAG, "enableBle(): not enabling - satellite mode is on.");
            return false;
        }
        updateBleAppCount(iBinder, true, packageName);
        int i = this.mState;
        if (i == 12 || i == 15 || i == 11 || i == 13 || i == 14) {
            Log.d(TAG, "enableBLE(): Bluetooth is already enabled or is turning on");
            return true;
        }
        synchronized (this.mReceiver) {
            sendEnableMsg(false, 1, packageName, true);
        }
        return true;
    }

    @RequiresPermission(BLUETOOTH_PRIVILEGED)
    public boolean disableBle(AttributionSource attributionSource, IBinder iBinder) throws RemoteException {
        String packageName = attributionSource.getPackageName();
        if (!checkBluetoothPermissions(attributionSource, "disableBle", false)) {
            if (this.DBG) {
                Log.d(TAG, "disableBLE(): bluetooth disallowed");
            }
            return false;
        }
        if (this.DBG) {
            Log.d(TAG, "disableBle(" + packageName + "):  mBluetooth =" + this.mBluetooth + " mBinding = " + this.mBinding + " mState = " + BluetoothAdapter.nameForState(this.mState));
        }
        if (isSatelliteModeOn()) {
            Log.d(TAG, "disableBle(): not disabling - satellite mode is on.");
            return false;
        }
        if (this.mState == 10) {
            Log.d(TAG, "disableBLE(): Already disabled");
            return false;
        }
        updateBleAppCount(iBinder, false, packageName);
        if (this.mState == 15 && !isBleAppPresent()) {
            if (this.mEnable) {
                disableBleScanMode();
            }
            if (!this.mEnableExternal) {
                addActiveLog(1, packageName, false);
                sendBrEdrDownCallback(attributionSource);
                this.mOplusBms.oplusDcsEventReport(1, 21, 1, packageName, null);
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearBleApps() {
        this.mBleApps.clear();
        this.mOplusBms.oplusDcsEventReport(4, 0, 0, null, null);
    }

    public boolean isBleAppPresent() {
        if (this.DBG) {
            Log.d(TAG, "isBleAppPresent() count: " + this.mBleApps.size());
        }
        return this.mBleApps.size() > 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @RequiresPermission(BLUETOOTH_PRIVILEGED)
    public void continueFromBleOnState() {
        if (this.DBG) {
            Log.d(TAG, "continueFromBleOnState()");
        }
        this.mBluetoothLock.readLock().lock();
        try {
            try {
            } catch (RemoteException | TimeoutException e) {
                Log.e(TAG, "Unable to call onServiceUp", e);
            }
            if (this.mBluetooth == null) {
                Log.e(TAG, "onBluetoothServiceUp: mBluetooth is null!");
            } else if (!this.mEnableExternal && !isBleAppPresent()) {
                Log.i(TAG, "Bluetooth was disabled while enabling BLE, disable BLE now");
                this.mEnable = false;
                synchronousOnBrEdrDown(this.mContext.getAttributionSource());
            } else {
                if (isBluetoothPersistedStateOnBluetooth() || !isBleAppPresent()) {
                    synchronousOnLeServiceUp(this.mContext.getAttributionSource());
                    persistBluetoothSetting(1);
                }
            }
        } finally {
            this.mBluetoothLock.readLock().unlock();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v9, types: [java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock] */
    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_CONNECT", BLUETOOTH_PRIVILEGED})
    private void sendBrEdrDownCallback(AttributionSource attributionSource) {
        if (this.DBG) {
            Log.d(TAG, "Calling sendBrEdrDownCallback callbacks");
        }
        if (this.mBluetooth == null) {
            Log.w(TAG, "Bluetooth handle is null");
            return;
        }
        if (isBleAppPresent()) {
            try {
                SynchronousResultReceiver synchronousResultReceiver = SynchronousResultReceiver.get();
                IBluetoothGatt iBluetoothGatt = this.mBluetoothGatt;
                if (iBluetoothGatt != null) {
                    iBluetoothGatt.unregAll(attributionSource, synchronousResultReceiver);
                }
                synchronousResultReceiver.awaitResultNoInterrupt(getSyncTimeout()).getValue((Object) null);
                return;
            } catch (RemoteException | TimeoutException e) {
                Log.e(TAG, "Unable to disconnect all apps.", e);
                return;
            }
        }
        this.mBluetoothLock.readLock().lock();
        try {
            try {
                if (this.mBluetooth != null) {
                    synchronousOnBrEdrDown(attributionSource);
                }
            } catch (RemoteException | TimeoutException e2) {
                Log.e(TAG, "Call to onBrEdrDown() failed.", e2);
            }
        } finally {
            this.mBluetoothLock.readLock().unlock();
        }
    }

    public boolean enableNoAutoConnect(AttributionSource attributionSource) {
        this.DBG = !SystemProperties.getBoolean("ro.build.release_type", false) || SystemProperties.getBoolean("persist.sys.assert.panic", false);
        String packageName = attributionSource.getPackageName();
        if (!checkBluetoothPermissions(attributionSource, "enableNoAutoConnect", false)) {
            if (this.DBG) {
                Log.d(TAG, "enableNoAutoConnect(): not enabling - bluetooth disallowed");
            }
            return false;
        }
        if (this.DBG) {
            Log.d(TAG, "enableNoAutoConnect():  mBluetooth =" + this.mBluetooth + " mBinding = " + this.mBinding);
        }
        int appId = UserHandle.getAppId(Binder.getCallingUid());
        String nameForUid = this.mContext.getPackageManager().getNameForUid(Binder.getCallingUid());
        if (this.DBG) {
            Log.d(TAG, "callingApp = " + nameForUid);
        }
        if (appId != 1027 && !PACKAGE_NAME_OSHARE.equals(nameForUid)) {
            throw new SecurityException("no permission to enable Bluetooth quietly");
        }
        if (isSatelliteModeOn()) {
            Log.d(TAG, "enableNoAutoConnect(): not enabling - satellite mode is on.");
            return false;
        }
        synchronized (this.mReceiver) {
            this.mQuietEnableExternal = true;
            this.mEnableExternal = true;
            sendEnableMsg(true, 1, packageName);
        }
        return true;
    }

    public boolean enable(AttributionSource attributionSource) throws RemoteException {
        BluetoothAirplaneModeListener bluetoothAirplaneModeListener;
        this.DBG = !SystemProperties.getBoolean("ro.build.release_type", false) || SystemProperties.getBoolean("persist.sys.assert.panic", false);
        String packageName = attributionSource.getPackageName();
        if (!checkBluetoothPermissions(attributionSource, IOplusBluetoothManagerServiceExt.FLAG_ENABLE, true)) {
            if (this.DBG) {
                Log.d(TAG, "enable(): not enabling - bluetooth disallowed");
            }
            return false;
        }
        int callingUid = Binder.getCallingUid();
        int callingPid = Binder.getCallingPid();
        if (CompatChanges.isChangeEnabled(RESTRICT_ENABLE_DISABLE, callingUid) && !isPrivileged(callingPid, callingUid) && !isSystem(packageName, callingUid) && !isDeviceOwner(callingUid, packageName) && !isProfileOwner(callingUid, packageName)) {
            Log.d(TAG, "enable(): not enabling - Caller is not one of: privileged | system | deviceOwner | profileOwner");
            return false;
        }
        if (isSatelliteModeOn()) {
            Log.d(TAG, "enable(): not enabling - satellite mode is on.");
            return false;
        }
        if (this.DBG) {
            Log.d(TAG, "enable(" + packageName + "):  mBluetooth=" + this.mBluetooth + " mBinding=" + this.mBinding + " mState=" + BluetoothAdapter.nameForState(this.mState));
        }
        synchronized (this.mReceiver) {
            this.mQuietEnableExternal = false;
            this.mEnableExternal = true;
            if (isAirplaneModeOn() && (bluetoothAirplaneModeListener = this.mBluetoothAirplaneModeListener) != null) {
                bluetoothAirplaneModeListener.updateBluetoothToggledTime();
                if (isApmEnhancementOn()) {
                    setSettingsSecureInt(BluetoothAirplaneModeListener.BLUETOOTH_APM_STATE, 1);
                    setSettingsSecureInt(BluetoothAirplaneModeListener.APM_USER_TOGGLED_BLUETOOTH, 1);
                    if (isFirstTimeNotification(BluetoothAirplaneModeListener.APM_BT_ENABLED_NOTIFICATION)) {
                        long clearCallingIdentity = Binder.clearCallingIdentity();
                        try {
                            try {
                                this.mBluetoothAirplaneModeListener.sendApmNotification("bluetooth_enabled_apm_title", "bluetooth_enabled_apm_message", BluetoothAirplaneModeListener.APM_BT_ENABLED_NOTIFICATION);
                            } catch (Exception unused) {
                                Log.e(TAG, "APM enhancement BT enabled notification not shown");
                            }
                        } finally {
                            Binder.restoreCallingIdentity(clearCallingIdentity);
                        }
                    }
                }
            }
            sendEnableMsg(false, 1, packageName);
        }
        if (this.DBG) {
            Log.d(TAG, "enable returning");
        }
        return true;
    }

    public boolean disable(AttributionSource attributionSource, boolean z) throws RemoteException {
        BluetoothAirplaneModeListener bluetoothAirplaneModeListener;
        if (!z) {
            this.mContext.enforceCallingOrSelfPermission(BLUETOOTH_PRIVILEGED, "Need BLUETOOTH_PRIVILEGED permission");
        }
        String packageName = attributionSource.getPackageName();
        if (!checkBluetoothPermissions(attributionSource, "disable", true)) {
            if (this.DBG) {
                Log.d(TAG, "disable(): not disabling - bluetooth disallowed");
            }
            return false;
        }
        int callingUid = Binder.getCallingUid();
        int callingPid = Binder.getCallingPid();
        if (CompatChanges.isChangeEnabled(RESTRICT_ENABLE_DISABLE, callingUid) && !isPrivileged(callingPid, callingUid) && !isSystem(packageName, callingUid) && !isDeviceOwner(callingUid, packageName) && !isProfileOwner(callingUid, packageName)) {
            Log.d(TAG, "disable(): not disabling - Caller is not one of: privileged | system | deviceOwner | profileOwner");
            return false;
        }
        if (isSatelliteModeOn()) {
            Log.d(TAG, "disable: not disabling - satellite mode is on.");
            return false;
        }
        if (this.DBG) {
            Log.d(TAG, "disable(): mBluetooth=" + this.mBluetooth + ", persist=" + z + ", mBinding=" + this.mBinding + ", mEnable=" + this.mEnable + ", mEnableExternal=" + this.mEnableExternal);
        }
        synchronized (this.mReceiver) {
            if (isAirplaneModeOn() && (bluetoothAirplaneModeListener = this.mBluetoothAirplaneModeListener) != null) {
                bluetoothAirplaneModeListener.updateBluetoothToggledTime();
                if (isApmEnhancementOn()) {
                    setSettingsSecureInt(BluetoothAirplaneModeListener.BLUETOOTH_APM_STATE, 0);
                    setSettingsSecureInt(BluetoothAirplaneModeListener.APM_USER_TOGGLED_BLUETOOTH, 1);
                }
            }
            if (z) {
                persistBluetoothSetting(0);
                this.mOplusBms.oplusClearBleApp(packageName);
            }
            this.mEnableExternal = false;
            sendDisableMsg(1, packageName);
        }
        return true;
    }

    private void checkPackage(String str) {
        int callingUid = Binder.getCallingUid();
        AppOpsManager appOpsManager = this.mAppOps;
        if (appOpsManager == null) {
            Log.w(TAG, "checkPackage(): called before system boot up, uid " + callingUid + ", packageName " + str);
            throw new IllegalStateException("System has not boot yet");
        }
        if (str == null) {
            Log.w(TAG, "checkPackage(): called with null packageName from " + callingUid);
            return;
        }
        try {
            appOpsManager.checkPackage(callingUid, str);
        } catch (SecurityException e) {
            Log.w(TAG, "checkPackage(): " + str + " does not belong to uid " + callingUid);
            throw new SecurityException(e.getMessage());
        }
    }

    @SuppressLint({"AndroidFrameworkRequiresPermission"})
    private boolean checkBluetoothPermissionWhenWirelessConsentRequired() {
        return this.mContext.checkCallingPermission("android.permission.MANAGE_BLUETOOTH_WHEN_WIRELESS_CONSENT_REQUIRED") == 0;
    }

    @RequiresPermission(BLUETOOTH_PRIVILEGED)
    public void unbindAndFinish() {
        if (this.DBG) {
            Log.d(TAG, "unbindAndFinish(): " + this.mBluetooth + " mBinding = " + this.mBinding + " mUnbinding = " + this.mUnbinding);
        }
        this.mBluetoothLock.writeLock().lock();
        try {
            if (this.mUnbinding) {
                return;
            }
            this.mUnbinding = true;
            this.mHandler.removeMessages(60);
            this.mHandler.removeMessages(MESSAGE_BIND_PROFILE_SERVICE);
            if (this.mBluetooth != null) {
                try {
                    synchronousUnregisterCallback(this.mBluetoothCallback, this.mContext.getAttributionSource());
                } catch (RemoteException | TimeoutException e) {
                    Log.e(TAG, "Unable to unregister BluetoothCallback", e);
                }
                this.mBluetoothBinder = null;
                this.mBluetooth = null;
                this.mContext.unbindService(this.mConnection);
                this.mUnbinding = false;
                this.mBinding = false;
            } else {
                this.mUnbinding = false;
            }
            this.mBluetoothGatt = null;
        } finally {
            this.mBluetoothLock.writeLock().unlock();
        }
    }

    public IBluetoothGatt getBluetoothGatt() {
        return this.mBluetoothGatt;
    }

    public boolean isBluetoothAvailableForBinding() {
        try {
            this.mBluetoothLock.readLock().lock();
            int state = getState();
            if (this.mBluetooth == null || !(state == 12 || state == 11)) {
                this.mBluetoothLock.readLock().unlock();
                return false;
            }
            this.mBluetoothLock.readLock().unlock();
            return true;
        } catch (Throwable th) {
            this.mBluetoothLock.readLock().unlock();
            throw th;
        }
    }

    public boolean bindBluetoothProfileService(int i, String str, IBluetoothProfileServiceConnection iBluetoothProfileServiceConnection) {
        ProfileServiceConnections profileServiceConnections;
        if (!isBluetoothAvailableForBinding()) {
            Log.w(TAG, "bindBluetoothProfileService:Trying to bind to profile: " + i + ", while Bluetooth is disabled");
            return false;
        }
        if (this.DBG) {
            Log.d(TAG, "bindBluetoothProfileService, profile: " + i);
        }
        synchronized (this.mProfileServices) {
            if (!this.mSupportedProfileList.contains(Integer.valueOf(i))) {
                Log.w(TAG, "Cannot bind profile: " + i + ", not in supported profiles list");
                return false;
            }
            if (this.mProfileServices.get(Integer.valueOf(i)) == null) {
                if (this.DBG) {
                    Log.d(TAG, "Creating new ProfileServiceConnections object for profile: " + i);
                }
                profileServiceConnections = new ProfileServiceConnections(new Intent(str));
                this.mProfileServices.put(new Integer(i), profileServiceConnections);
            } else {
                profileServiceConnections = null;
            }
            if (profileServiceConnections != null && !profileServiceConnections.bindService(3)) {
                synchronized (this.mProfileServices) {
                    this.mProfileServices.remove(new Integer(i));
                }
                return false;
            }
            Message obtainMessage = this.mHandler.obtainMessage(400);
            obtainMessage.arg1 = i;
            obtainMessage.obj = iBluetoothProfileServiceConnection;
            this.mHandler.sendMessageDelayed(obtainMessage, 100L);
            return true;
        }
    }

    public void unbindBluetoothProfileService(int i, IBluetoothProfileServiceConnection iBluetoothProfileServiceConnection) {
        synchronized (this.mProfileServices) {
            Integer num = new Integer(i);
            ProfileServiceConnections profileServiceConnections = this.mProfileServices.get(num);
            if (profileServiceConnections == null) {
                return;
            }
            profileServiceConnections.removeProxy(iBluetoothProfileServiceConnection);
            if (profileServiceConnections.isEmpty()) {
                try {
                    this.mContext.unbindService(profileServiceConnections);
                } catch (IllegalArgumentException e) {
                    Log.e(TAG, "Unable to unbind service with intent: " + profileServiceConnections.mIntent, e);
                }
                if (!this.mUnbindingAll) {
                    this.mProfileServices.remove(num);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unbindAllBluetoothProfileServices() {
        synchronized (this.mProfileServices) {
            this.mUnbindingAll = true;
            Iterator<Integer> it = this.mProfileServices.keySet().iterator();
            while (it.hasNext()) {
                ProfileServiceConnections profileServiceConnections = this.mProfileServices.get(it.next());
                try {
                    this.mContext.unbindService(profileServiceConnections);
                } catch (IllegalArgumentException e) {
                    Log.e(TAG, "Unable to unbind service with intent: " + profileServiceConnections.mIntent, e);
                }
                profileServiceConnections.removeAllProxies();
            }
            this.mUnbindingAll = false;
            this.mProfileServices.clear();
        }
    }

    public void handleOnBootPhase() {
        if (this.DBG) {
            Log.d(TAG, "Bluetooth boot completed");
        }
        this.mOplusBms.oplusHandleOnBootPhase();
        this.mAppOps = (AppOpsManager) this.mContext.getSystemService(AppOpsManager.class);
        if (isBluetoothDisallowed()) {
            return;
        }
        boolean isSafeMode = this.mContext.getPackageManager().isSafeMode();
        if (this.mEnableExternal && isBluetoothPersistedStateOnBluetooth() && !isSafeMode) {
            if (this.DBG) {
                Log.d(TAG, "Auto-enabling Bluetooth.");
            }
            sendEnableMsg(this.mQuietEnableExternal, 6, this.mContext.getPackageName());
        } else if (!isNameAndAddressSet()) {
            if (this.DBG) {
                Log.d(TAG, "Getting adapter name and address");
            }
            this.mHandler.sendMessage(this.mHandler.obtainMessage(200));
        }
        BluetoothModeChangeHelper bluetoothModeChangeHelper = new BluetoothModeChangeHelper(this.mContext);
        this.mBluetoothModeChangeHelper = bluetoothModeChangeHelper;
        BluetoothAirplaneModeListener bluetoothAirplaneModeListener = this.mBluetoothAirplaneModeListener;
        if (bluetoothAirplaneModeListener != null) {
            bluetoothAirplaneModeListener.start(bluetoothModeChangeHelper);
        }
        registerForProvisioningStateChange();
        this.mBluetoothDeviceConfigListener = new BluetoothDeviceConfigListener(this, this.DBG);
        loadApmEnhancementStateFromResource();
    }

    @VisibleForTesting
    void setBluetoothModeChangeHelper(BluetoothModeChangeHelper bluetoothModeChangeHelper) {
        this.mBluetoothModeChangeHelper = bluetoothModeChangeHelper;
    }

    @VisibleForTesting
    void loadApmEnhancementStateFromResource() {
        String bluetoothPackageName = this.mBluetoothModeChangeHelper.getBluetoothPackageName();
        if (bluetoothPackageName == null) {
            Log.e(TAG, "Unable to find Bluetooth package name with APM resources");
            return;
        }
        try {
            Resources resourcesForApplication = this.mContext.getPackageManager().getResourcesForApplication(bluetoothPackageName);
            Settings.Global.putInt(this.mContext.getContentResolver(), BluetoothAirplaneModeListener.APM_ENHANCEMENT, resourcesForApplication.getBoolean(resourcesForApplication.getIdentifier("config_bluetooth_apm_enhancement_enabled", "bool", bluetoothPackageName)) ? 1 : 0);
        } catch (Exception unused) {
            Log.e(TAG, "Unable to set whether APM enhancement should be enabled");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: handleSwitchUser, reason: merged with bridge method [inline-methods] */
    public void lambda$onSwitchUser$3(UserHandle userHandle) {
        if (this.DBG) {
            Log.d(TAG, "User " + userHandle + " switched");
        }
        this.mHandler.obtainMessage(300, userHandle.getIdentifier(), 0).sendToTarget();
    }

    public void handleOnUnlockUser(UserHandle userHandle) {
        if (this.DBG) {
            Log.d(TAG, "User " + userHandle + " unlocked");
        }
        this.mHandler.obtainMessage(301, userHandle.getIdentifier(), 0).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class ProfileServiceConnections implements ServiceConnection, IBinder.DeathRecipient {
        Intent mIntent;
        final RemoteCallbackList<IBluetoothProfileServiceConnection> mProxies = new RemoteCallbackList<>();
        IBinder mService = null;
        ComponentName mClassName = null;

        ProfileServiceConnections(Intent intent) {
            this.mIntent = intent;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean bindService(int i) {
            try {
                try {
                    BluetoothManagerService.this.mBluetoothLock.readLock().lock();
                    int synchronousGetState = BluetoothManagerService.this.synchronousGetState();
                    BluetoothManagerService.this.mBluetoothLock.readLock().unlock();
                    if (synchronousGetState != 12) {
                        if (BluetoothManagerService.this.DBG) {
                            Log.d(BluetoothManagerService.TAG, "Unable to bindService while Bluetooth is disabled");
                        }
                        return false;
                    }
                    Intent intent = this.mIntent;
                    if (intent != null && this.mService == null && BluetoothManagerService.this.doBind(intent, this, 0, BluetoothManagerService.USER_HANDLE_CURRENT_OR_SELF)) {
                        Message obtainMessage = BluetoothManagerService.this.mHandler.obtainMessage(BluetoothManagerService.MESSAGE_BIND_PROFILE_SERVICE);
                        obtainMessage.obj = this;
                        obtainMessage.arg1 = i;
                        BluetoothManagerService.this.mHandler.sendMessageDelayed(obtainMessage, 3000L);
                        return true;
                    }
                    Log.w(BluetoothManagerService.TAG, "Unable to bind with intent: " + this.mIntent);
                    return false;
                } catch (RemoteException | TimeoutException e) {
                    Log.e(BluetoothManagerService.TAG, "Unable to call getState", e);
                    BluetoothManagerService.this.mBluetoothLock.readLock().unlock();
                    return false;
                }
            } catch (Throwable th) {
                BluetoothManagerService.this.mBluetoothLock.readLock().unlock();
                throw th;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addProxy(IBluetoothProfileServiceConnection iBluetoothProfileServiceConnection) {
            this.mProxies.register(iBluetoothProfileServiceConnection);
            IBinder iBinder = this.mService;
            if (iBinder != null) {
                try {
                    iBluetoothProfileServiceConnection.onServiceConnected(this.mClassName, iBinder);
                    return;
                } catch (RemoteException e) {
                    Log.e(BluetoothManagerService.TAG, "Unable to connect to proxy", e);
                    return;
                }
            }
            if (!BluetoothManagerService.this.isBluetoothAvailableForBinding()) {
                Log.w(BluetoothManagerService.TAG, "addProxy: Trying to bind to profile: " + this.mClassName + ", while Bluetooth is disabled");
                this.mProxies.unregister(iBluetoothProfileServiceConnection);
                return;
            }
            if (BluetoothManagerService.this.mHandler.hasMessages(BluetoothManagerService.MESSAGE_BIND_PROFILE_SERVICE, this)) {
                return;
            }
            Message obtainMessage = BluetoothManagerService.this.mHandler.obtainMessage(BluetoothManagerService.MESSAGE_BIND_PROFILE_SERVICE);
            obtainMessage.obj = this;
            obtainMessage.arg1 = 3;
            BluetoothManagerService.this.mHandler.sendMessage(obtainMessage);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeProxy(IBluetoothProfileServiceConnection iBluetoothProfileServiceConnection) {
            if (iBluetoothProfileServiceConnection != null) {
                if (this.mProxies.unregister(iBluetoothProfileServiceConnection)) {
                    try {
                        iBluetoothProfileServiceConnection.onServiceDisconnected(this.mClassName);
                    } catch (RemoteException e) {
                        Log.e(BluetoothManagerService.TAG, "Unable to disconnect proxy", e);
                    }
                }
                Log.w(BluetoothManagerService.TAG, "removing the proxy, count is " + this.mProxies.getRegisteredCallbackCount());
                return;
            }
            Log.w(BluetoothManagerService.TAG, "Trying to remove a null proxy");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeAllProxies() {
            onServiceDisconnected(this.mClassName);
            this.mProxies.kill();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isEmpty() {
            return this.mProxies.getRegisteredCallbackCount() == 0;
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            BluetoothManagerService.this.mHandler.removeMessages(BluetoothManagerService.MESSAGE_BIND_PROFILE_SERVICE, this);
            this.mService = iBinder;
            this.mClassName = componentName;
            try {
                iBinder.linkToDeath(this, 0);
            } catch (RemoteException e) {
                Log.e(BluetoothManagerService.TAG, "Unable to linkToDeath", e);
            }
            synchronized (this.mProxies) {
                int beginBroadcast = this.mProxies.beginBroadcast();
                for (int i = 0; i < beginBroadcast; i++) {
                    try {
                        try {
                            this.mProxies.getBroadcastItem(i).onServiceConnected(componentName, iBinder);
                        } catch (RemoteException e2) {
                            Log.e(BluetoothManagerService.TAG, "Unable to connect to proxy", e2);
                        }
                    } finally {
                        this.mProxies.finishBroadcast();
                    }
                }
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            IBinder iBinder = this.mService;
            if (iBinder == null) {
                return;
            }
            try {
                iBinder.unlinkToDeath(this, 0);
            } catch (NoSuchElementException e) {
                Log.e(BluetoothManagerService.TAG, "error unlinking to death", e);
            }
            this.mService = null;
            this.mClassName = null;
            synchronized (this.mProxies) {
                int beginBroadcast = this.mProxies.beginBroadcast();
                for (int i = 0; i < beginBroadcast; i++) {
                    try {
                        try {
                            this.mProxies.getBroadcastItem(i).onServiceDisconnected(componentName);
                        } finally {
                            this.mProxies.finishBroadcast();
                        }
                    } catch (RemoteException e2) {
                        Log.e(BluetoothManagerService.TAG, "Unable to disconnect from proxy #" + i, e2);
                    }
                }
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            if (BluetoothManagerService.this.DBG) {
                Log.w(BluetoothManagerService.TAG, "Profile service for profile: " + this.mClassName + " died.");
            }
            onServiceDisconnected(this.mClassName);
            if (!BluetoothManagerService.this.isBluetoothAvailableForBinding()) {
                Log.w(BluetoothManagerService.TAG, "binderDied: Trying to bind to profile: " + this.mClassName + ", while Bluetooth is disabled");
                return;
            }
            Message obtainMessage = BluetoothManagerService.this.mHandler.obtainMessage(BluetoothManagerService.MESSAGE_BIND_PROFILE_SERVICE);
            obtainMessage.obj = this;
            BluetoothManagerService.this.mHandler.sendMessageDelayed(obtainMessage, 3000L);
        }
    }

    private void sendBluetoothStateCallback(boolean z) {
        synchronized (this.mStateChangeCallbacks) {
            try {
                int beginBroadcast = this.mStateChangeCallbacks.beginBroadcast();
                if (this.DBG) {
                    Log.d(TAG, "Broadcasting onBluetoothStateChange(" + z + ") to " + beginBroadcast + " receivers.");
                }
                for (int i = 0; i < beginBroadcast; i++) {
                    try {
                        try {
                            this.mStateChangeCallbacks.getBroadcastItem(i).onBluetoothStateChange(z);
                        } catch (SecurityException e) {
                            Log.e(TAG, "Unable to call onBluetoothStateChange() on callback #" + i, e);
                        }
                    } catch (RemoteException e2) {
                        Log.e(TAG, "Unable to call onBluetoothStateChange() on callback #" + i, e2);
                    }
                }
            } finally {
                this.mStateChangeCallbacks.finishBroadcast();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendBluetoothServiceUpCallback() {
        synchronized (this.mCallbacks) {
            this.mBluetoothLock.readLock().lock();
            try {
                int beginBroadcast = this.mCallbacks.beginBroadcast();
                Log.d(TAG, "Broadcasting onBluetoothServiceUp() to " + beginBroadcast + " receivers.");
                for (int i = 0; i < beginBroadcast; i++) {
                    try {
                        try {
                            this.mCallbacks.getBroadcastItem(i).onBluetoothServiceUp(this.mBluetooth);
                        } catch (RemoteException e) {
                            Log.e(TAG, "Unable to call onBluetoothServiceUp() on callback #" + i, e);
                        }
                    } catch (SecurityException e2) {
                        Log.e(TAG, "Unable to call onBluetoothServiceUp() on callback #" + i, e2);
                    }
                }
            } finally {
                this.mCallbacks.finishBroadcast();
                this.mBluetoothLock.readLock().unlock();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendBluetoothServiceDownCallback() {
        synchronized (this.mCallbacks) {
            try {
                int beginBroadcast = this.mCallbacks.beginBroadcast();
                Log.d(TAG, "Broadcasting onBluetoothServiceDown() to " + beginBroadcast + " receivers.");
                for (int i = 0; i < beginBroadcast; i++) {
                    try {
                        this.mCallbacks.getBroadcastItem(i).onBluetoothServiceDown();
                    } catch (RemoteException e) {
                        Log.e(TAG, "Unable to call onBluetoothServiceDown() on callback #" + i, e);
                    } catch (SecurityException e2) {
                        Log.e(TAG, "Unable to call onBluetoothServiceDown() on callback #" + i, e2);
                    }
                }
            } finally {
                this.mCallbacks.finishBroadcast();
            }
        }
    }

    public String getAddress(AttributionSource attributionSource) {
        int i;
        if (!checkConnectPermissionForDataDelivery(this.mContext, attributionSource, "getAddress")) {
            return null;
        }
        if (!isCallerSystem(getCallingAppId()) && !checkIfCallerIsForegroundUser()) {
            Log.w(TAG, "getAddress(): not allowed for non-active and non system user");
            return null;
        }
        if (this.mContext.checkCallingOrSelfPermission("android.permission.LOCAL_MAC_ADDRESS") != 0) {
            return "02:00:00:00:00:00";
        }
        this.mBluetoothLock.readLock().lock();
        try {
            try {
                if (this.mBluetooth != null && (i = this.mState) != 14 && i != 16) {
                    return synchronousGetAddress(attributionSource);
                }
            } catch (RemoteException | TimeoutException e) {
                Log.e(TAG, "getAddress(): Unable to retrieve address remotely. Returning cached address", e);
            }
            return this.mAddress;
        } finally {
            this.mBluetoothLock.readLock().unlock();
        }
    }

    public String getName(AttributionSource attributionSource) {
        if (!checkConnectPermissionForDataDelivery(this.mContext, attributionSource, "getName")) {
            return null;
        }
        if (!isCallerSystem(getCallingAppId()) && !checkIfCallerIsForegroundUser()) {
            Log.w(TAG, "getName(): not allowed for non-active and non system user");
            return null;
        }
        this.mBluetoothLock.readLock().lock();
        try {
            try {
                if (this.mBluetooth != null) {
                    return synchronousGetName(attributionSource);
                }
            } catch (RemoteException | TimeoutException e) {
                Log.e(TAG, "getName(): Unable to retrieve name remotely. Returning cached name", e);
            }
            return this.mName;
        } finally {
            this.mBluetoothLock.readLock().unlock();
        }
    }

    public boolean factoryReset() {
        boolean z = false;
        if (!(UserHandle.getAppId(Binder.getCallingUid()) == 1000)) {
            if (!checkIfCallerIsForegroundUser()) {
                Log.w(TAG, "factoryReset(): not allowed for non-active and non system user");
                return false;
            }
            this.mContext.enforceCallingOrSelfPermission(BLUETOOTH_PRIVILEGED, "Need BLUETOOTH PRIVILEGED permission");
        }
        persistBluetoothSetting(1);
        int state = getState();
        if ((state == 14 || state == 11 || state == 13) && !waitForState(Set.of(15, 12))) {
            return false;
        }
        clearBleApps();
        try {
            try {
                this.mBluetoothLock.writeLock().lock();
                IBluetooth iBluetooth = this.mBluetooth;
                if (iBluetooth == null) {
                    this.mEnable = true;
                    handleEnable(this.mQuietEnable);
                } else {
                    if (state == 10) {
                        this.mEnable = true;
                        iBluetooth.factoryReset(this.mContext.getAttributionSource(), SynchronousResultReceiver.get());
                        handleEnable(this.mQuietEnable);
                    } else if (state == 15) {
                        addActiveLog(10, this.mContext.getPackageName(), false);
                        synchronousOnBrEdrDown(this.mContext.getAttributionSource());
                        this.mBluetooth.factoryReset(this.mContext.getAttributionSource(), SynchronousResultReceiver.get());
                    } else if (state == 12) {
                        addActiveLog(10, this.mContext.getPackageName(), false);
                        handleDisable();
                        this.mBluetooth.factoryReset(this.mContext.getAttributionSource(), SynchronousResultReceiver.get());
                    }
                    z = true;
                }
                this.mBluetoothLock.writeLock().unlock();
                Log.d(TAG, "run oplus factoryReset()");
                if (z && (waitForState(Set.of(10)) || this.mBluetooth == null)) {
                    this.mOplusBms.oplusFactoryReset();
                }
                return true;
            } catch (RemoteException | TimeoutException e) {
                Log.e(TAG, "factoryReset(): Unable to do factoryReset.", e);
                this.mBluetoothLock.writeLock().unlock();
                Log.d(TAG, "run oplus factoryReset()");
                return false;
            }
        } catch (Throwable th) {
            this.mBluetoothLock.writeLock().unlock();
            Log.d(TAG, "run oplus factoryReset()");
            if (waitForState(Set.of(10)) || this.mBluetooth == null) {
                this.mOplusBms.oplusFactoryReset();
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class BluetoothServiceConnection implements ServiceConnection {
        private BluetoothServiceConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            String className = componentName.getClassName();
            if (BluetoothManagerService.this.DBG) {
                Log.d(BluetoothManagerService.TAG, "BluetoothServiceConnection: " + className);
            }
            Message obtainMessage = BluetoothManagerService.this.mHandler.obtainMessage(40);
            if (className.equals("com.android.bluetooth.btservice.AdapterService")) {
                obtainMessage.arg1 = 1;
            } else if (className.equals("com.android.bluetooth.gatt.GattService")) {
                obtainMessage.arg1 = 2;
            } else {
                Log.e(BluetoothManagerService.TAG, "Unknown service connected: " + className);
                return;
            }
            obtainMessage.obj = iBinder;
            BluetoothManagerService.this.mHandler.sendMessage(obtainMessage);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            String className = componentName.getClassName();
            if (BluetoothManagerService.this.DBG) {
                Log.d(BluetoothManagerService.TAG, "BluetoothServiceConnection, disconnected: " + className);
            }
            Message obtainMessage = BluetoothManagerService.this.mHandler.obtainMessage(41);
            if (className.equals("com.android.bluetooth.btservice.AdapterService")) {
                obtainMessage.arg1 = 1;
            } else if (className.equals("com.android.bluetooth.gatt.GattService")) {
                obtainMessage.arg1 = 2;
            } else {
                Log.e(BluetoothManagerService.TAG, "Unknown service disconnected: " + className);
                return;
            }
            BluetoothManagerService.this.mHandler.sendMessage(obtainMessage);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class BluetoothHandler extends Handler {
        boolean mGetNameAddressOnly;
        private int mWaitForDisableRetry;
        private int mWaitForEnableRetry;

        BluetoothHandler(Looper looper) {
            super(looper);
            this.mGetNameAddressOnly = false;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            boolean z;
            int i = message.what;
            if (i == 1) {
                int i2 = message.arg1;
                int i3 = message.arg2;
                if (BluetoothManagerService.this.mShutdownInProgress) {
                    Log.d(BluetoothManagerService.TAG, "Skip Bluetooth Enable in device shutdown process");
                    return;
                }
                if (BluetoothManagerService.this.mHandler.hasMessages(4) || BluetoothManagerService.this.mHandler.hasMessages(3)) {
                    BluetoothManagerService.this.mHandler.sendMessageDelayed(BluetoothManagerService.this.mHandler.obtainMessage(1, i2, i3), 300L);
                    return;
                }
                if (BluetoothManagerService.this.DBG) {
                    Log.d(BluetoothManagerService.TAG, "MESSAGE_ENABLE(" + i2 + "): mBluetooth =" + BluetoothManagerService.this.mBluetooth);
                }
                BluetoothManagerService.this.mHandler.removeMessages(42);
                BluetoothManagerService.this.mEnable = true;
                if (i3 == 0) {
                    BluetoothManagerService.this.persistBluetoothSetting(1);
                }
                BluetoothManagerService.this.mBluetoothLock.readLock().lock();
                try {
                    try {
                        if (BluetoothManagerService.this.mBluetooth != null) {
                            switch (BluetoothManagerService.this.synchronousGetState()) {
                                case 11:
                                case 12:
                                case 14:
                                    Log.i(BluetoothManagerService.TAG, "MESSAGE_ENABLE: already enabled");
                                    z = true;
                                    break;
                                case 13:
                                default:
                                    z = false;
                                    break;
                                case 15:
                                    if (i3 == 1) {
                                        Log.i(BluetoothManagerService.TAG, "Already at BLE_ON State");
                                    } else {
                                        Log.w(BluetoothManagerService.TAG, "BT Enable in BLE_ON State, going to ON");
                                        BluetoothManagerService bluetoothManagerService = BluetoothManagerService.this;
                                        bluetoothManagerService.synchronousOnLeServiceUp(bluetoothManagerService.mContext.getAttributionSource());
                                    }
                                    z = true;
                                    break;
                            }
                            if (z) {
                                return;
                            }
                        }
                    } catch (RemoteException | TimeoutException e) {
                        Log.e(BluetoothManagerService.TAG, "", e);
                    }
                    BluetoothManagerService.this.mQuietEnable = i2 == 1;
                    if (BluetoothManagerService.this.mBluetooth == null) {
                        BluetoothManagerService bluetoothManagerService2 = BluetoothManagerService.this;
                        bluetoothManagerService2.handleEnable(bluetoothManagerService2.mQuietEnable);
                        return;
                    } else {
                        this.mWaitForEnableRetry = 0;
                        BluetoothManagerService.this.mHandler.sendMessageDelayed(BluetoothManagerService.this.mHandler.obtainMessage(3), 300L);
                        return;
                    }
                } finally {
                    BluetoothManagerService.this.mBluetoothLock.readLock().unlock();
                }
            }
            if (i == 2) {
                if (BluetoothManagerService.this.mHandler.hasMessages(4) || BluetoothManagerService.this.mBinding || BluetoothManagerService.this.mHandler.hasMessages(3)) {
                    BluetoothManagerService.this.mHandler.sendMessageDelayed(BluetoothManagerService.this.mHandler.obtainMessage(2), 300L);
                    return;
                }
                if (BluetoothManagerService.this.DBG) {
                    Log.d(BluetoothManagerService.TAG, "MESSAGE_DISABLE: mBluetooth =" + BluetoothManagerService.this.mBluetooth + ", mBinding = " + BluetoothManagerService.this.mBinding);
                }
                BluetoothManagerService.this.mHandler.removeMessages(42);
                if (!BluetoothManagerService.this.mEnable || BluetoothManagerService.this.mBluetooth == null) {
                    BluetoothManagerService.this.mEnable = false;
                    BluetoothManagerService.this.handleDisable();
                    return;
                } else {
                    this.mWaitForDisableRetry = 0;
                    BluetoothManagerService.this.mHandler.sendMessageDelayed(BluetoothManagerService.this.mHandler.obtainMessage(4, 0, 0), 300L);
                    return;
                }
            }
            if (i == 3) {
                if (BluetoothManagerService.this.mState != 10) {
                    int i4 = this.mWaitForEnableRetry;
                    if (i4 < 10) {
                        this.mWaitForEnableRetry = i4 + 1;
                        BluetoothManagerService.this.mHandler.sendMessageDelayed(BluetoothManagerService.this.mHandler.obtainMessage(3), 300L);
                        return;
                    }
                    Log.e(BluetoothManagerService.TAG, "Wait for STATE_OFF timeout");
                }
                this.mWaitForEnableRetry = 0;
                BluetoothManagerService.this.mHandler.sendMessageDelayed(BluetoothManagerService.this.mHandler.obtainMessage(42), BluetoothManagerService.this.getServiceRestartMs());
                Log.d(BluetoothManagerService.TAG, "Handle enable is finished");
                return;
            }
            if (i == 4) {
                boolean z2 = message.arg1 == 1;
                Log.d(BluetoothManagerService.TAG, "MESSAGE_HANDLE_DISABLE_DELAYED: disabling:" + z2);
                if (z2) {
                    if (BluetoothManagerService.this.mState == 12) {
                        int i5 = this.mWaitForDisableRetry;
                        if (i5 < 10) {
                            this.mWaitForDisableRetry = i5 + 1;
                            BluetoothManagerService.this.mHandler.sendMessageDelayed(BluetoothManagerService.this.mHandler.obtainMessage(4, 1, 0), 300L);
                            return;
                        }
                        Log.e(BluetoothManagerService.TAG, "Wait for exiting STATE_ON timeout");
                    }
                    Log.d(BluetoothManagerService.TAG, "Handle disable is finished");
                    return;
                }
                if (BluetoothManagerService.this.mState != 12) {
                    int i6 = this.mWaitForDisableRetry;
                    if (i6 < 10) {
                        this.mWaitForDisableRetry = i6 + 1;
                        BluetoothManagerService.this.mHandler.sendMessageDelayed(BluetoothManagerService.this.mHandler.obtainMessage(4, 0, 0), 300L);
                        return;
                    }
                    Log.e(BluetoothManagerService.TAG, "Wait for STATE_ON timeout");
                }
                this.mWaitForDisableRetry = 0;
                BluetoothManagerService.this.mEnable = false;
                BluetoothManagerService.this.handleDisable();
                BluetoothManagerService.this.mHandler.sendMessageDelayed(BluetoothManagerService.this.mHandler.obtainMessage(4, 1, 0), 300L);
                return;
            }
            if (i == 30) {
                BluetoothManagerService.this.mStateChangeCallbacks.register((IBluetoothStateChangeCallback) message.obj);
                return;
            }
            if (i == 31) {
                BluetoothManagerService.this.mStateChangeCallbacks.unregister((IBluetoothStateChangeCallback) message.obj);
                return;
            }
            if (i == 60) {
                int i7 = message.arg1;
                int i8 = message.arg2;
                if (BluetoothManagerService.this.DBG) {
                    Log.d(BluetoothManagerService.TAG, "MESSAGE_BLUETOOTH_STATE_CHANGE: " + BluetoothAdapter.nameForState(i7) + " > " + BluetoothAdapter.nameForState(i8));
                }
                BluetoothManagerService.this.mState = i8;
                BluetoothManagerService.this.bluetoothStateChangeHandler(i7, i8);
                if (i7 == 14 && i8 == 10 && BluetoothManagerService.this.mBluetooth != null && BluetoothManagerService.this.mEnable) {
                    BluetoothManagerService.this.recoverBluetoothServiceFromError(false);
                }
                if (i7 == 11 && i8 == 15 && BluetoothManagerService.this.mBluetooth != null && BluetoothManagerService.this.mEnable) {
                    BluetoothManagerService.this.recoverBluetoothServiceFromError(true);
                }
                if (i7 == 16 && i8 == 10 && BluetoothManagerService.this.mEnable) {
                    Log.d(BluetoothManagerService.TAG, "Entering STATE_OFF but mEnabled is true; restarting.");
                    BluetoothManagerService.this.waitForState(Set.of(10));
                    BluetoothManagerService.this.mHandler.sendMessageDelayed(BluetoothManagerService.this.mHandler.obtainMessage(42), BluetoothManagerService.this.getServiceRestartMs());
                }
                if ((i8 == 12 || (!BluetoothManagerService.this.mEnableExternal && i8 == 15)) && BluetoothManagerService.this.mErrorRecoveryRetryCounter != 0) {
                    Log.w(BluetoothManagerService.TAG, "bluetooth is recovered from error");
                    BluetoothManagerService.this.mErrorRecoveryRetryCounter = 0;
                    return;
                }
                return;
            }
            if (i == 100) {
                Log.e(BluetoothManagerService.TAG, "MESSAGE_TIMEOUT_BIND");
                BluetoothManagerService.this.mBluetoothLock.writeLock().lock();
                BluetoothManagerService.this.mBinding = false;
                return;
            }
            if (i == 200) {
                if (BluetoothManagerService.this.DBG) {
                    Log.d(BluetoothManagerService.TAG, "MESSAGE_GET_NAME_AND_ADDRESS");
                }
                BluetoothManagerService.this.mBluetoothLock.writeLock().lock();
                try {
                    if (BluetoothManagerService.this.mBluetooth == null && !BluetoothManagerService.this.mBinding) {
                        if (BluetoothManagerService.this.DBG) {
                            Log.d(BluetoothManagerService.TAG, "Binding to service to get name and address");
                        }
                        this.mGetNameAddressOnly = true;
                        BluetoothManagerService.this.mHandler.sendMessageDelayed(BluetoothManagerService.this.mHandler.obtainMessage(100), 3000L);
                        Intent intent = new Intent(IBluetooth.class.getName());
                        BluetoothManagerService bluetoothManagerService3 = BluetoothManagerService.this;
                        if (bluetoothManagerService3.doBind(intent, bluetoothManagerService3.mConnection, 65, UserHandle.CURRENT)) {
                            BluetoothManagerService.this.mBinding = true;
                        } else {
                            BluetoothManagerService.this.mHandler.removeMessages(100);
                            BluetoothManagerService.this.mOplusBms.oplusDcsEventReport(3, 16, 0, null, null);
                        }
                    } else if (!BluetoothManagerService.this.mOplusBms.oplusSaveRemoteNameAndAddress() && BluetoothManagerService.this.mBluetooth != null) {
                        try {
                            BluetoothManagerService bluetoothManagerService4 = BluetoothManagerService.this;
                            String synchronousGetName = bluetoothManagerService4.synchronousGetName(bluetoothManagerService4.mContext.getAttributionSource());
                            BluetoothManagerService bluetoothManagerService5 = BluetoothManagerService.this;
                            bluetoothManagerService4.storeNameAndAddress(synchronousGetName, bluetoothManagerService5.synchronousGetAddress(bluetoothManagerService5.mContext.getAttributionSource()));
                        } catch (RemoteException | TimeoutException e2) {
                            Log.e(BluetoothManagerService.TAG, "Unable to grab names", e2);
                        }
                        if (this.mGetNameAddressOnly && !BluetoothManagerService.this.mEnable) {
                            BluetoothManagerService.this.unbindAndFinish();
                        }
                        this.mGetNameAddressOnly = false;
                    }
                    return;
                } finally {
                }
            }
            if (i == 500) {
                if (message.arg1 == 0 && BluetoothManagerService.this.mEnable) {
                    if (BluetoothManagerService.this.DBG) {
                        Log.d(BluetoothManagerService.TAG, "Restore Bluetooth state to disabled");
                    }
                    BluetoothManagerService.this.persistBluetoothSetting(0);
                    BluetoothManagerService.this.mEnableExternal = false;
                    BluetoothManagerService bluetoothManagerService6 = BluetoothManagerService.this;
                    bluetoothManagerService6.sendDisableMsg(9, bluetoothManagerService6.mContext.getPackageName());
                    return;
                }
                if (message.arg1 != 1 || BluetoothManagerService.this.mEnable) {
                    return;
                }
                if (BluetoothManagerService.this.DBG) {
                    Log.d(BluetoothManagerService.TAG, "Restore Bluetooth state to enabled");
                }
                BluetoothManagerService.this.mQuietEnableExternal = false;
                BluetoothManagerService.this.mEnableExternal = true;
                BluetoothManagerService bluetoothManagerService7 = BluetoothManagerService.this;
                bluetoothManagerService7.sendEnableMsg(false, 9, bluetoothManagerService7.mContext.getPackageName());
                return;
            }
            if (i == 600) {
                if (BluetoothManagerService.this.DBG) {
                    Log.d(BluetoothManagerService.TAG, "MESSAGE_INIT_FLAGS_CHANGED");
                }
                BluetoothManagerService.this.mHandler.removeMessages(600);
                if (BluetoothManagerService.this.mBluetoothModeChangeHelper.isMediaProfileConnected()) {
                    Log.i(BluetoothManagerService.TAG, "Delaying MESSAGE_INIT_FLAGS_CHANGED by 86400000 ms due to existing connections");
                    BluetoothManagerService.this.mHandler.sendEmptyMessageDelayed(600, BackupManagerConstants.DEFAULT_FULL_BACKUP_INTERVAL_MILLISECONDS);
                    return;
                } else if (!BluetoothManagerService.this.isDeviceProvisioned()) {
                    Log.i(BluetoothManagerService.TAG, "Delaying MESSAGE_INIT_FLAGS_CHANGED by 86400000ms because device is not provisioned");
                    BluetoothManagerService.this.mHandler.sendEmptyMessageDelayed(600, BackupManagerConstants.DEFAULT_FULL_BACKUP_INTERVAL_MILLISECONDS);
                    return;
                } else {
                    if (BluetoothManagerService.this.mBluetooth == null || !BluetoothManagerService.this.isEnabled()) {
                        return;
                    }
                    Log.i(BluetoothManagerService.TAG, "Restarting Bluetooth due to init flag change");
                    restartForReason(11);
                    return;
                }
            }
            if (i == 300) {
                if (BluetoothManagerService.this.DBG) {
                    Log.d(BluetoothManagerService.TAG, "MESSAGE_USER_SWITCHED");
                }
                BluetoothManagerService.this.mHandler.removeMessages(300);
                BluetoothManagerService.this.mBluetoothNotificationManager.createNotificationChannels();
                try {
                    int state = BluetoothManagerService.this.getState();
                    if (BluetoothManagerService.this.mBluetooth == null || !(state == 12 || (state == 15 && BluetoothManagerService.this.isBleAppPresent()))) {
                        if (BluetoothManagerService.this.mBinding || BluetoothManagerService.this.mBluetooth != null) {
                            Message obtainMessage = BluetoothManagerService.this.mHandler.obtainMessage(300);
                            obtainMessage.arg2 = message.arg2 + 1;
                            BluetoothManagerService.this.mHandler.sendMessageDelayed(obtainMessage, 200L);
                            if (BluetoothManagerService.this.DBG) {
                                Log.d(BluetoothManagerService.TAG, "Retry MESSAGE_USER_SWITCHED " + obtainMessage.arg2);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    if (state == 12) {
                        restartForReason(8);
                        return;
                    }
                    if (BluetoothManagerService.this.DBG) {
                        Log.d(BluetoothManagerService.TAG, "Turn off from BLE state");
                    }
                    BluetoothManagerService.this.clearBleApps();
                    BluetoothManagerService bluetoothManagerService8 = BluetoothManagerService.this;
                    bluetoothManagerService8.addActiveLog(8, bluetoothManagerService8.mContext.getPackageName(), false);
                    BluetoothManagerService.this.mBluetoothLock.writeLock().lock();
                    BluetoothManagerService.this.mEnable = false;
                    BluetoothManagerService bluetoothManagerService9 = BluetoothManagerService.this;
                    bluetoothManagerService9.synchronousOnBrEdrDown(bluetoothManagerService9.mContext.getAttributionSource());
                    BluetoothManagerService.this.mBluetoothLock.writeLock().unlock();
                    return;
                } catch (RemoteException | TimeoutException e3) {
                    Log.e(BluetoothManagerService.TAG, "MESSAGE_USER_SWITCHED: Remote exception", e3);
                    return;
                }
            }
            if (i == 301) {
                if (BluetoothManagerService.this.DBG) {
                    Log.d(BluetoothManagerService.TAG, "MESSAGE_USER_UNLOCKED");
                }
                BluetoothManagerService.this.mHandler.removeMessages(300);
                if (BluetoothManagerService.this.mEnable && !BluetoothManagerService.this.mBinding && BluetoothManagerService.this.mBluetooth == null) {
                    if (BluetoothManagerService.this.DBG) {
                        Log.d(BluetoothManagerService.TAG, "Enabled but not bound; retrying after unlock");
                    }
                    BluetoothManagerService bluetoothManagerService10 = BluetoothManagerService.this;
                    bluetoothManagerService10.handleEnable(bluetoothManagerService10.mQuietEnable);
                    return;
                }
                return;
            }
            if (i == 400) {
                if (BluetoothManagerService.this.DBG) {
                    Log.d(BluetoothManagerService.TAG, "MESSAGE_ADD_PROXY_DELAYED, profile id: " + message.arg1);
                }
                ProfileServiceConnections profileServiceConnections = (ProfileServiceConnections) BluetoothManagerService.this.mProfileServices.get(Integer.valueOf(message.arg1));
                if (profileServiceConnections == null) {
                    return;
                }
                profileServiceConnections.addProxy((IBluetoothProfileServiceConnection) message.obj);
                return;
            }
            if (i == BluetoothManagerService.MESSAGE_BIND_PROFILE_SERVICE) {
                Log.d(BluetoothManagerService.TAG, "MESSAGE_BIND_PROFILE_SERVICE");
                Object obj = message.obj;
                ProfileServiceConnections profileServiceConnections2 = (ProfileServiceConnections) obj;
                removeMessages(BluetoothManagerService.MESSAGE_BIND_PROFILE_SERVICE, obj);
                if (profileServiceConnections2 != null && message.arg1 > 0) {
                    try {
                        BluetoothManagerService.this.mContext.unbindService(profileServiceConnections2);
                    } catch (IllegalArgumentException e4) {
                        Log.e(BluetoothManagerService.TAG, "Unable to unbind service with intent: " + profileServiceConnections2.mIntent, e4);
                    }
                    profileServiceConnections2.bindService(message.arg1 - 1);
                    return;
                }
                return;
            }
            int[] iArr = null;
            switch (i) {
                case 40:
                    if (BluetoothManagerService.this.DBG) {
                        Log.d(BluetoothManagerService.TAG, "MESSAGE_BLUETOOTH_SERVICE_CONNECTED: " + message.arg1);
                    }
                    IBinder iBinder = (IBinder) message.obj;
                    BluetoothManagerService.this.mBluetoothLock.writeLock().lock();
                    try {
                        if (message.arg1 == 2) {
                            BluetoothManagerService.this.mBluetoothGatt = IBluetoothGatt.Stub.asInterface(iBinder);
                            BluetoothManagerService.this.continueFromBleOnState();
                            return;
                        }
                        BluetoothManagerService.this.mHandler.removeMessages(100);
                        BluetoothManagerService.this.mBinding = false;
                        BluetoothManagerService.this.mBluetoothBinder = iBinder;
                        BluetoothManagerService.this.mBluetooth = IBluetooth.Stub.asInterface(iBinder);
                        BluetoothManagerService.this.propagateForegroundUserId(ActivityManager.getCurrentUser());
                        if (!BluetoothManagerService.this.isNameAndAddressSet()) {
                            BluetoothManagerService.this.mHandler.sendMessage(BluetoothManagerService.this.mHandler.obtainMessage(200));
                            if (this.mGetNameAddressOnly && !BluetoothManagerService.this.mEnable) {
                                return;
                            }
                        }
                        try {
                            BluetoothManagerService bluetoothManagerService11 = BluetoothManagerService.this;
                            bluetoothManagerService11.synchronousRegisterCallback(bluetoothManagerService11.mBluetoothCallback, BluetoothManagerService.this.mContext.getAttributionSource());
                        } catch (RemoteException | TimeoutException e5) {
                            Log.e(BluetoothManagerService.TAG, "Unable to register BluetoothCallback", e5);
                        }
                        BluetoothManagerService.this.sendBluetoothServiceUpCallback();
                        try {
                            BluetoothManagerService bluetoothManagerService12 = BluetoothManagerService.this;
                            bluetoothManagerService12.mSupportedProfileList = bluetoothManagerService12.synchronousGetSupportedProfiles(bluetoothManagerService12.mContext.getAttributionSource());
                        } catch (RemoteException | TimeoutException e6) {
                            Log.e(BluetoothManagerService.TAG, "Unable to get the supported profiles list", e6);
                        }
                        try {
                            BluetoothManagerService bluetoothManagerService13 = BluetoothManagerService.this;
                            if (!bluetoothManagerService13.synchronousEnable(bluetoothManagerService13.mQuietEnable, BluetoothManagerService.this.mContext.getAttributionSource())) {
                                Log.e(BluetoothManagerService.TAG, "IBluetooth.enable() returned false");
                            }
                        } catch (RemoteException | TimeoutException e7) {
                            Log.e(BluetoothManagerService.TAG, "Unable to call enable()", e7);
                        }
                        BluetoothManagerService.this.mBluetoothLock.writeLock().unlock();
                        if (BluetoothManagerService.this.mEnable) {
                            return;
                        }
                        BluetoothManagerService.this.waitForState(Set.of(12));
                        BluetoothManagerService.this.handleDisable();
                        BluetoothManagerService.this.waitForState(Set.of(10, 11, 13, 14, 15, 16));
                        return;
                    } finally {
                    }
                case 41:
                    Log.e(BluetoothManagerService.TAG, "MESSAGE_BLUETOOTH_SERVICE_DISCONNECTED(" + message.arg1 + ")");
                    BluetoothManagerService.this.mBluetoothLock.writeLock().lock();
                    try {
                        int i9 = message.arg1;
                        if (i9 == 1) {
                            BluetoothManagerService.this.mHandler.removeMessages(200);
                            BluetoothManagerService.this.mOplusBms.oplusRemoveSaveRemoteNameAndAddressMsg();
                            if (BluetoothManagerService.this.mBluetooth != null) {
                                BluetoothManagerService.this.mBluetooth = null;
                                BluetoothManagerService.this.mSupportedProfileList.clear();
                                try {
                                    iArr = (int[]) Class.forName("android.os.Process").getMethod("getPidsForCommands", String[].class).invoke(null, new String[]{"com.android.bluetooth"});
                                } catch (Exception e8) {
                                    e8.printStackTrace();
                                    Log.e(BluetoothManagerService.TAG, "Error to call getPidsForCommands" + e8.getMessage());
                                }
                                if (iArr != null && iArr.length > 0) {
                                    for (int i10 : iArr) {
                                        Log.e(BluetoothManagerService.TAG, "Killing BT process with PID = " + i10);
                                        Process.killProcess(i10);
                                    }
                                }
                                BluetoothManagerService.this.addCrashLog();
                                BluetoothManagerService bluetoothManagerService14 = BluetoothManagerService.this;
                                bluetoothManagerService14.addActiveLog(7, bluetoothManagerService14.mContext.getPackageName(), false);
                                BluetoothManagerService bluetoothManagerService15 = BluetoothManagerService.this;
                                bluetoothManagerService15.mOplusBms.oplusDcsEventReport(3, 17, bluetoothManagerService15.mErrorRecoveryRetryCounter < 7 ? 0 : 1, null, null);
                                if (BluetoothManagerService.this.mEnable) {
                                    BluetoothManagerService.this.mEnable = false;
                                    BluetoothManagerService bluetoothManagerService16 = BluetoothManagerService.this;
                                    int i11 = bluetoothManagerService16.mErrorRecoveryRetryCounter;
                                    bluetoothManagerService16.mErrorRecoveryRetryCounter = i11 + 1;
                                    if (i11 < 6) {
                                        BluetoothManagerService.this.mHandler.sendMessageDelayed(BluetoothManagerService.this.mHandler.obtainMessage(42), BluetoothManagerService.this.getServiceRestartMs());
                                    } else {
                                        Log.e(BluetoothManagerService.TAG, "Times of recover bluetooth are exceed for service died!");
                                    }
                                }
                                BluetoothManagerService.this.sendBluetoothServiceDownCallback();
                                if (BluetoothManagerService.this.mState == 11 || BluetoothManagerService.this.mState == 12) {
                                    BluetoothManagerService.this.bluetoothStateChangeHandler(12, 13);
                                    BluetoothManagerService.this.mState = 13;
                                }
                                if (BluetoothManagerService.this.mState == 13) {
                                    BluetoothManagerService.this.bluetoothStateChangeHandler(13, 10);
                                }
                                BluetoothManagerService.this.mHandler.removeMessages(60);
                                BluetoothManagerService.this.mState = 10;
                                return;
                            }
                        } else if (i9 == 2) {
                            BluetoothManagerService.this.mBluetoothGatt = null;
                        } else {
                            Log.e(BluetoothManagerService.TAG, "Unknown argument for service disconnect!");
                        }
                        return;
                    } finally {
                    }
                case 42:
                    BluetoothManagerService.this.mErrorRecoveryRetryCounter++;
                    Log.d(BluetoothManagerService.TAG, "MESSAGE_RESTART_BLUETOOTH_SERVICE: retry count=" + BluetoothManagerService.this.mErrorRecoveryRetryCounter);
                    if (BluetoothManagerService.this.mErrorRecoveryRetryCounter >= 6) {
                        BluetoothManagerService.this.mBluetoothLock.writeLock().lock();
                        BluetoothManagerService.this.mBluetooth = null;
                        BluetoothManagerService.this.mBluetoothLock.writeLock().unlock();
                        Log.e(BluetoothManagerService.TAG, "Reach maximum retry to restart Bluetooth!");
                        return;
                    }
                    BluetoothManagerService.this.mEnable = true;
                    BluetoothManagerService bluetoothManagerService17 = BluetoothManagerService.this;
                    bluetoothManagerService17.addActiveLog(4, bluetoothManagerService17.mContext.getPackageName(), true);
                    BluetoothManagerService bluetoothManagerService18 = BluetoothManagerService.this;
                    bluetoothManagerService18.mOplusBms.oplusDcsEventReport(1, 1, 4, bluetoothManagerService18.mContext.getPackageName(), null);
                    BluetoothManagerService bluetoothManagerService19 = BluetoothManagerService.this;
                    bluetoothManagerService19.handleEnable(bluetoothManagerService19.mQuietEnable);
                    return;
                default:
                    return;
            }
        }

        @RequiresPermission(allOf = {"android.permission.BLUETOOTH_CONNECT", BluetoothManagerService.BLUETOOTH_PRIVILEGED})
        private void restartForReason(int i) {
            BluetoothManagerService.this.mBluetoothLock.readLock().lock();
            try {
                try {
                    if (BluetoothManagerService.this.mBluetooth != null) {
                        BluetoothManagerService bluetoothManagerService = BluetoothManagerService.this;
                        bluetoothManagerService.synchronousUnregisterCallback(bluetoothManagerService.mBluetoothCallback, BluetoothManagerService.this.mContext.getAttributionSource());
                    }
                } catch (RemoteException | TimeoutException e) {
                    Log.e(BluetoothManagerService.TAG, "Unable to unregister", e);
                }
                if (BluetoothManagerService.this.mState == 13) {
                    BluetoothManagerService bluetoothManagerService2 = BluetoothManagerService.this;
                    bluetoothManagerService2.bluetoothStateChangeHandler(bluetoothManagerService2.mState, 10);
                    BluetoothManagerService.this.mState = 10;
                }
                if (BluetoothManagerService.this.mState == 10) {
                    BluetoothManagerService bluetoothManagerService3 = BluetoothManagerService.this;
                    bluetoothManagerService3.bluetoothStateChangeHandler(bluetoothManagerService3.mState, 11);
                    BluetoothManagerService.this.mState = 11;
                }
                BluetoothManagerService.this.waitForState(Set.of(12));
                if (BluetoothManagerService.this.mState == 11) {
                    BluetoothManagerService bluetoothManagerService4 = BluetoothManagerService.this;
                    bluetoothManagerService4.bluetoothStateChangeHandler(bluetoothManagerService4.mState, 12);
                }
                BluetoothManagerService.this.unbindAllBluetoothProfileServices();
                BluetoothManagerService bluetoothManagerService5 = BluetoothManagerService.this;
                bluetoothManagerService5.addActiveLog(i, bluetoothManagerService5.mContext.getPackageName(), false);
                BluetoothManagerService bluetoothManagerService6 = BluetoothManagerService.this;
                bluetoothManagerService6.mOplusBms.oplusDcsEventReport(1, 2, i, bluetoothManagerService6.mContext.getPackageName(), null);
                BluetoothManagerService.this.handleDisable();
                BluetoothManagerService.this.bluetoothStateChangeHandler(12, 13);
                boolean z = !BluetoothManagerService.this.waitForState(Set.of(10));
                BluetoothManagerService.this.bluetoothStateChangeHandler(13, 10);
                BluetoothManagerService.this.sendBluetoothServiceDownCallback();
                BluetoothManagerService.this.mBluetoothLock.writeLock().lock();
                try {
                    if (BluetoothManagerService.this.mBluetooth != null) {
                        BluetoothManagerService.this.mBluetooth = null;
                        BluetoothManagerService.this.mContext.unbindService(BluetoothManagerService.this.mConnection);
                    }
                    BluetoothManagerService.this.mBluetoothGatt = null;
                    if (z) {
                        SystemClock.sleep(3000L);
                    } else {
                        SystemClock.sleep(100L);
                    }
                    BluetoothManagerService.this.mHandler.removeMessages(60);
                    BluetoothManagerService.this.mState = 10;
                    BluetoothManagerService bluetoothManagerService7 = BluetoothManagerService.this;
                    bluetoothManagerService7.addActiveLog(i, bluetoothManagerService7.mContext.getPackageName(), true);
                    BluetoothManagerService bluetoothManagerService8 = BluetoothManagerService.this;
                    bluetoothManagerService8.mOplusBms.oplusDcsEventReport(1, 1, i, bluetoothManagerService8.mContext.getPackageName(), null);
                    BluetoothManagerService.this.mEnable = true;
                    BluetoothManagerService bluetoothManagerService9 = BluetoothManagerService.this;
                    bluetoothManagerService9.handleEnable(bluetoothManagerService9.mQuietEnable);
                } finally {
                    BluetoothManagerService.this.mBluetoothLock.writeLock().unlock();
                }
            } finally {
                BluetoothManagerService.this.mBluetoothLock.readLock().unlock();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    public void handleEnable(boolean z) {
        this.mQuietEnable = z;
        this.mBluetoothLock.writeLock().lock();
        try {
            IBluetooth iBluetooth = this.mBluetooth;
            if (iBluetooth == null && !this.mBinding) {
                Log.d(TAG, "binding Bluetooth service");
                this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(100), 3000L);
                if (!doBind(new Intent(IBluetooth.class.getName()), this.mConnection, 65, UserHandle.CURRENT)) {
                    this.mHandler.removeMessages(100);
                } else {
                    this.mBinding = true;
                }
            } else if (iBluetooth != null) {
                try {
                    if (!synchronousEnable(this.mQuietEnable, this.mContext.getAttributionSource())) {
                        Log.e(TAG, "IBluetooth.enable() returned false");
                    } else {
                        this.mOplusBms.oplusDcsEventReport(1, 18, 0, "null", null);
                    }
                } catch (RemoteException | TimeoutException e) {
                    Log.e(TAG, "Unable to call enable()", e);
                }
            }
        } finally {
            this.mBluetoothLock.writeLock().unlock();
        }
    }

    boolean doBind(Intent intent, ServiceConnection serviceConnection, int i, UserHandle userHandle) {
        ComponentName resolveSystemService = resolveSystemService(intent, this.mContext.getPackageManager(), 0);
        if (this.DBG) {
            Log.d(TAG, "doBind(), " + resolveSystemService);
        }
        intent.setComponent(resolveSystemService);
        if (resolveSystemService == null || !this.mContext.bindServiceAsUser(intent, serviceConnection, i, userHandle)) {
            Log.e(TAG, "Fail to bind to: " + intent);
            this.mOplusBms.oplusDcsEventReport(3, 16, 0, null, null);
            return false;
        }
        Log.d(TAG, "doBind(), done");
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    public void handleDisable() {
        if (this.mQuietEnable) {
            this.mQuietEnable = false;
        }
        this.mBluetoothLock.readLock().lock();
        try {
            try {
                if (this.mBluetooth != null) {
                    if (this.DBG) {
                        Log.d(TAG, "Sending off request.");
                    }
                    if (!synchronousDisable(this.mContext.getAttributionSource())) {
                        Log.e(TAG, "IBluetooth.disable() returned false");
                    } else {
                        this.mOplusBms.oplusDcsEventReport(1, 19, 0, "null", null);
                    }
                }
            } catch (RemoteException | TimeoutException e) {
                Log.e(TAG, "Unable to call disable()", e);
            }
        } finally {
            this.mBluetoothLock.readLock().unlock();
        }
    }

    private static int getCallingAppId() {
        return UserHandle.getAppId(Binder.getCallingUid());
    }

    private boolean checkIfCallerIsForegroundUser() {
        boolean z;
        int callingUid = Binder.getCallingUid();
        UserHandle userHandleForUid = UserHandle.getUserHandleForUid(callingUid);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        UserHandle profileParent = ((UserManager) this.mContext.getSystemService(UserManager.class)).getProfileParent(userHandleForUid);
        if (profileParent == null) {
            profileParent = USER_HANDLE_NULL;
        }
        int appId = UserHandle.getAppId(callingUid);
        try {
            UserHandle of = UserHandle.of(ActivityManager.getCurrentUser());
            if (userHandleForUid != of && profileParent != of && callingUid != 999 && appId != 1027 && appId != this.mSystemUiUid && appId != 2000) {
                z = false;
                if (this.DBG && !z) {
                    Log.d(TAG, "checkIfCallerIsForegroundUser: valid=" + z + " callingUser=" + userHandleForUid + " parentUser=" + profileParent + " foregroundUser=" + of);
                }
                return z;
            }
            z = true;
            if (this.DBG) {
                Log.d(TAG, "checkIfCallerIsForegroundUser: valid=" + z + " callingUser=" + userHandleForUid + " parentUser=" + profileParent + " foregroundUser=" + of);
            }
            return z;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void sendBleStateChanged(int i, int i2) {
        if (this.DBG) {
            Log.d(TAG, "Sending BLE State Change: " + BluetoothAdapter.nameForState(i) + " > " + BluetoothAdapter.nameForState(i2));
        }
        Intent intent = new Intent("android.bluetooth.adapter.action.BLE_STATE_CHANGED");
        intent.putExtra("android.bluetooth.adapter.extra.PREVIOUS_STATE", i);
        intent.putExtra("android.bluetooth.adapter.extra.STATE", i2);
        intent.addFlags(67108864);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, null, getTempAllowlistBroadcastOptions());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:14:0x00d8  */
    /* JADX WARN: Removed duplicated region for block: B:24:? A[RETURN, SYNTHETIC] */
    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_CONNECT", BLUETOOTH_PRIVILEGED})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void bluetoothStateChangeHandler(int i, int i2) {
        if (i == i2) {
            return;
        }
        this.mOplusBms.oplusDcsEventReport(2, i, i2, null, null);
        if (i2 == 15 || i2 == 10) {
            boolean z = i == 13 && i2 == 15;
            if (i2 == 10) {
                if (this.DBG) {
                    Log.d(TAG, "Bluetooth is complete send Service Down");
                }
                sendBluetoothServiceDownCallback();
                unbindAndFinish();
                sendBleStateChanged(i, i2);
                r4 = !isBleState(i);
            } else if (!z) {
                if (this.DBG) {
                    Log.d(TAG, "Bluetooth is in LE only mode");
                }
                if (this.mBluetoothGatt != null || !this.mContext.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
                    continueFromBleOnState();
                } else {
                    if (this.DBG) {
                        Log.d(TAG, "Binding Bluetooth GATT service");
                    }
                    doBind(new Intent(IBluetoothGatt.class.getName()), this.mConnection, 65, UserHandle.CURRENT);
                }
                sendBleStateChanged(i, i2);
            } else if (z) {
                if (this.DBG) {
                    Log.d(TAG, "Intermediate off, back to LE only mode");
                }
                sendBleStateChanged(i, i2);
                sendBluetoothStateCallback(false);
                sendBrEdrDownCallback(this.mContext.getAttributionSource());
                i2 = 10;
            }
            if (r4) {
                if (i == 15) {
                    i = 10;
                }
                if (this.DBG) {
                    Log.d(TAG, "Sending State Change: " + BluetoothAdapter.nameForState(i) + " > " + BluetoothAdapter.nameForState(i2));
                }
                Intent intent = new Intent("android.bluetooth.adapter.action.STATE_CHANGED");
                intent.putExtra("android.bluetooth.adapter.extra.PREVIOUS_STATE", i);
                intent.putExtra("android.bluetooth.adapter.extra.STATE", i2);
                intent.addFlags(67108864);
                intent.addFlags(16777216);
                intent.addFlags(AudioFormat.EVRC);
                if (this.DBG) {
                    Log.d(TAG, "bluetoothStateChangeHandler() - Broadcast Adapter State: " + i + " > " + i2);
                }
                this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, null, getTempAllowlistBroadcastOptions());
                return;
            }
            return;
        }
        if (i2 == 12) {
            sendBluetoothStateCallback(i2 == 12);
            sendBleStateChanged(i, i2);
        } else {
            if (i2 == 14) {
                sendBleStateChanged(i, i2);
            } else if (i2 == 16) {
                sendBleStateChanged(i, i2);
                if (i == 13) {
                    sendBrEdrDownCallback(this.mContext.getAttributionSource());
                    i2 = 10;
                }
            } else if (i2 == 11 || i2 == 13) {
                sendBleStateChanged(i, i2);
            }
            if (r4) {
            }
        }
        r4 = true;
        if (r4) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean waitForManagerState(int i) {
        return waitForState(Set.of(Integer.valueOf(i)), false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean waitForState(Set<Integer> set) {
        return waitForState(set, true);
    }

    private boolean waitForState(Set<Integer> set, boolean z) {
        for (int i = 0; i < 10; i++) {
            this.mBluetoothLock.readLock().lock();
            try {
                IBluetooth iBluetooth = this.mBluetooth;
                if (iBluetooth == null && z) {
                    Log.e(TAG, "waitForState " + set + " Bluetooth is not unbind");
                    return false;
                }
                if ((iBluetooth == null && set.contains(10)) || (this.mBluetooth != null && set.contains(Integer.valueOf(synchronousGetState())))) {
                    return true;
                }
                this.mBluetoothLock.readLock().unlock();
                SystemClock.sleep(300L);
            } catch (RemoteException | TimeoutException e) {
                Log.e(TAG, "getState()", e);
            } finally {
                this.mBluetoothLock.readLock().unlock();
            }
        }
        Log.e(TAG, "waitForState " + set + " time out");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendDisableMsg(int i, String str) {
        BluetoothServerProxy.getInstance().handlerSendWhatMessage(this.mHandler, 2);
        addActiveLog(i, str, false);
        this.mOplusBms.oplusDcsEventReport(1, 2, i, str, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendEnableMsg(boolean z, int i, String str) {
        sendEnableMsg(z, i, str, false);
    }

    private void sendEnableMsg(boolean z, int i, String str, boolean z2) {
        int i2 = i == 6 ? SystemProperties.getInt("persist.bluetooth.auto.enable.delay", 0) : 0;
        BluetoothHandler bluetoothHandler = this.mHandler;
        bluetoothHandler.sendMessageDelayed(bluetoothHandler.obtainMessage(1, z ? 1 : 0, z2 ? 1 : 0), i2);
        addActiveLog(i, str, true);
        this.mLastEnabledTime = SystemClock.elapsedRealtime();
        this.mOplusBms.oplusDcsEventReport(1, 1, i, str, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addActiveLog(int i, String str, boolean z) {
        synchronized (this.mActiveLogs) {
            if (this.mActiveLogs.size() > 20) {
                this.mActiveLogs.remove();
            }
            this.mActiveLogs.add(new ActiveLog(i, str, z, System.currentTimeMillis()));
            BluetoothStatsLog.write_non_chained(67, Binder.getCallingUid(), null, z ? 1 : 2, i, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addCrashLog() {
        synchronized (this.mCrashTimestamps) {
            if (this.mCrashTimestamps.size() == 100) {
                this.mCrashTimestamps.removeFirst();
            }
            this.mCrashTimestamps.add(Long.valueOf(System.currentTimeMillis()));
            this.mCrashes++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_CONNECT", BLUETOOTH_PRIVILEGED})
    public void recoverBluetoothServiceFromError(boolean z) {
        boolean z2;
        Log.e(TAG, "recoverBluetoothServiceFromError");
        BluetoothHandler bluetoothHandler = this.mHandler;
        Object obj = ON_AIRPLANE_MODE_CHANGED_TOKEN;
        if (bluetoothHandler.hasMessages(0, obj)) {
            this.mHandler.removeCallbacksAndMessages(obj);
            z2 = true;
        } else {
            z2 = false;
        }
        this.mBluetoothLock.readLock().lock();
        try {
            try {
                if (this.mBluetooth != null) {
                    synchronousUnregisterCallback(this.mBluetoothCallback, this.mContext.getAttributionSource());
                }
            } catch (RemoteException | TimeoutException e) {
                Log.e(TAG, "Unable to unregister", e);
            }
            SystemClock.sleep(500L);
            addActiveLog(5, this.mContext.getPackageName(), false);
            handleDisable();
            waitForState(Set.of(10));
            sendBluetoothServiceDownCallback();
            this.mBluetoothLock.writeLock().lock();
            try {
                if (this.mBluetooth != null) {
                    this.mBluetooth = null;
                    this.mContext.unbindService(this.mConnection);
                }
                this.mBluetoothGatt = null;
                this.mBluetoothLock.writeLock().unlock();
                this.mHandler.removeMessages(60);
                this.mState = 10;
                if (z) {
                    clearBleApps();
                }
                this.mEnable = false;
                this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(42), 3000L);
                if (z2) {
                    onAirplaneModeChanged();
                }
            } catch (Throwable th) {
                this.mBluetoothLock.writeLock().unlock();
                throw th;
            }
        } finally {
            this.mBluetoothLock.readLock().unlock();
        }
    }

    private boolean isBluetoothDisallowed() {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return ((UserManager) this.mContext.getSystemService(UserManager.class)).hasUserRestrictionForUser("no_bluetooth", UserHandle.SYSTEM);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void updateOppLauncherComponentState(UserHandle userHandle, boolean z) {
        int i;
        int i2 = 0;
        if (z) {
            i = 2;
        } else {
            try {
                i = ((Boolean) BluetoothProperties.isProfileOppEnabled().orElse(Boolean.FALSE)).booleanValue() ? 1 : 0;
            } catch (Exception e) {
                Log.e(TAG, "updateOppLauncherComponentState failed: " + e);
                return;
            }
        }
        List of = List.of("com.android.bluetooth.opp.BluetoothOppLauncherActivity", "com.android.bluetooth.opp.BluetoothOppBtEnableActivity", "com.android.bluetooth.opp.BluetoothOppBtEnablingActivity", "com.android.bluetooth.opp.BluetoothOppBtErrorActivity");
        PackageManager packageManager = this.mContext.getPackageManager();
        PackageManager packageManager2 = this.mContext.createContextAsUser(userHandle, 0).getPackageManager();
        String[] packagesForUid = packageManager.getPackagesForUid(1002);
        int length = packagesForUid.length;
        int i3 = 0;
        while (i3 < length) {
            String str = packagesForUid[i3];
            Log.v(TAG, "Searching package " + str);
            try {
                ActivityInfo[] activityInfoArr = packageManager.getPackageInfo(str, PackageManager.PackageInfoFlags.of(4203009L)).activities;
                if (activityInfoArr != null) {
                    int length2 = activityInfoArr.length;
                    for (int i4 = i2; i4 < length2; i4++) {
                        ActivityInfo activityInfo = activityInfoArr[i4];
                        Log.v(TAG, "Checking activity " + activityInfo.name);
                        if (of.contains(activityInfo.name)) {
                            Iterator it = of.iterator();
                            while (it.hasNext()) {
                                packageManager2.setComponentEnabledSetting(new ComponentName(str, (String) it.next()), i, 1);
                            }
                            return;
                        }
                    }
                }
            } catch (PackageManager.NameNotFoundException unused) {
                Log.e(TAG, "Could not find package " + str);
            } catch (Exception e2) {
                Log.e(TAG, "Error while loading package" + e2);
            }
            i3++;
            i2 = 0;
        }
        Log.e(TAG, "Cannot toggle Bluetooth OPP activities, could not find them in any package");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getServiceRestartMs() {
        return (this.mErrorRecoveryRetryCounter + 1) * 400;
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str;
        if (this.mContext.checkCallingOrSelfPermission("android.permission.DUMP") != 0) {
            return;
        }
        if (strArr.length > 0 && strArr[0].startsWith("--proto")) {
            dumpProto(fileDescriptor);
            return;
        }
        printWriter.println("Bluetooth Status");
        printWriter.println("  enabled: " + isEnabled());
        printWriter.println("  state: " + BluetoothAdapter.nameForState(this.mState));
        printWriter.println("  address: " + this.mAddress);
        printWriter.println("  name: " + this.mName);
        if (this.mEnable) {
            long elapsedRealtime = SystemClock.elapsedRealtime() - this.mLastEnabledTime;
            printWriter.println("  time since enabled: " + String.format(Locale.US, "%02d:%02d:%02d.%03d", Integer.valueOf((int) (elapsedRealtime / ClipboardService.DEFAULT_CLIPBOARD_TIMEOUT_MILLIS)), Integer.valueOf((int) ((elapsedRealtime / 60000) % 60)), Integer.valueOf((int) ((elapsedRealtime / 1000) % 60)), Integer.valueOf((int) (elapsedRealtime % 1000))));
        }
        if (this.mActiveLogs.size() == 0) {
            printWriter.println("\nBluetooth never enabled!");
        } else {
            printWriter.println("\nEnable log:");
            Iterator<ActiveLog> it = this.mActiveLogs.iterator();
            while (it.hasNext()) {
                printWriter.println("  " + it.next());
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\nBluetooth crashed ");
        sb.append(this.mCrashes);
        sb.append(" time");
        sb.append(this.mCrashes == 1 ? "" : "s");
        printWriter.println(sb.toString());
        if (this.mCrashes == 100) {
            printWriter.println("(last 100)");
        }
        Iterator<Long> it2 = this.mCrashTimestamps.iterator();
        while (it2.hasNext()) {
            printWriter.println("  " + timeToLog(it2.next().longValue()));
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("\n");
        sb2.append(this.mBleApps.size());
        sb2.append(" BLE app");
        sb2.append(this.mBleApps.size() == 1 ? "" : "s");
        sb2.append(" registered");
        printWriter.println(sb2.toString());
        Iterator<ClientDeathRecipient> it3 = this.mBleApps.values().iterator();
        while (it3.hasNext()) {
            printWriter.println("  " + it3.next().getPackageName());
        }
        printWriter.println("\nBluetoothManagerService:");
        printWriter.println("  mEnable:" + this.mEnable);
        printWriter.println("  mQuietEnable:" + this.mQuietEnable);
        printWriter.println("  mEnableExternal:" + this.mEnableExternal);
        printWriter.println("  mQuietEnableExternal:" + this.mQuietEnableExternal);
        printWriter.println("");
        printWriter.flush();
        if (strArr.length == 0) {
            strArr = new String[]{"--print"};
        }
        IBinder iBinder = this.mBluetoothBinder;
        if (iBinder == null) {
            str = "Bluetooth Service not connected";
        } else {
            try {
                iBinder.dump(fileDescriptor, strArr);
                str = null;
            } catch (RemoteException unused) {
                str = "RemoteException while dumping Bluetooth Service";
            }
        }
        if (str != null) {
            printWriter.println(str);
        }
    }

    private void dumpProto(FileDescriptor fileDescriptor) {
        ProtoOutputStream protoOutputStream = new ProtoOutputStream(new FileOutputStream(fileDescriptor));
        protoOutputStream.write(1133871366145L, isEnabled());
        protoOutputStream.write(1120986464258L, this.mState);
        protoOutputStream.write(1138166333443L, BluetoothAdapter.nameForState(this.mState));
        protoOutputStream.write(1138166333444L, this.mAddress);
        protoOutputStream.write(1138166333445L, this.mName);
        if (this.mEnable) {
            protoOutputStream.write(1112396529670L, this.mLastEnabledTime);
        }
        protoOutputStream.write(1112396529671L, SystemClock.elapsedRealtime());
        Iterator<ActiveLog> it = this.mActiveLogs.iterator();
        while (it.hasNext()) {
            ActiveLog next = it.next();
            long start = protoOutputStream.start(2246267895816L);
            next.dump(protoOutputStream);
            protoOutputStream.end(start);
        }
        protoOutputStream.write(1120986464265L, this.mCrashes);
        protoOutputStream.write(1133871366154L, this.mCrashes == 100);
        Iterator<Long> it2 = this.mCrashTimestamps.iterator();
        while (it2.hasNext()) {
            protoOutputStream.write(2211908157451L, it2.next().longValue());
        }
        protoOutputStream.write(1120986464268L, this.mBleApps.size());
        Iterator<ClientDeathRecipient> it3 = this.mBleApps.values().iterator();
        while (it3.hasNext()) {
            protoOutputStream.write(2237677961229L, it3.next().getPackageName());
        }
        protoOutputStream.flush();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getEnableDisableReasonString(int i) {
        switch (i) {
            case 1:
                return "APPLICATION_REQUEST";
            case 2:
                return "AIRPLANE_MODE";
            case 3:
                return "DISALLOWED";
            case 4:
                return "RESTARTED";
            case 5:
                return "START_ERROR";
            case 6:
                return "SYSTEM_BOOT";
            case 7:
                return "CRASH";
            case 8:
                return "USER_SWITCH";
            case 9:
                return "RESTORE_USER_SETTING";
            case 10:
                return "FACTORY_RESET";
            case 11:
                return "INIT_FLAGS_CHANGED";
            default:
                return "UNKNOWN[" + i + "]";
        }
    }

    @SuppressLint({"AndroidFrameworkRequiresPermission"})
    private static boolean checkPermissionForDataDelivery(Context context, String str, AttributionSource attributionSource, String str2) {
        PermissionManager permissionManager = (PermissionManager) context.getSystemService(PermissionManager.class);
        if (permissionManager == null) {
            return false;
        }
        int checkPermissionForDataDeliveryFromDataSource = permissionManager.checkPermissionForDataDeliveryFromDataSource(str, new AttributionSource.Builder(context.getAttributionSource()).setNext(attributionSource).build(), str2);
        if (checkPermissionForDataDeliveryFromDataSource == 0) {
            return true;
        }
        String str3 = "Need " + str + " permission for " + attributionSource + ": " + str2;
        if (checkPermissionForDataDeliveryFromDataSource == 2) {
            throw new SecurityException(str3);
        }
        Log.w(TAG, str3);
        return false;
    }

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    @SuppressLint({"AndroidFrameworkRequiresPermission"})
    public static boolean checkConnectPermissionForDataDelivery(Context context, AttributionSource attributionSource, String str) {
        return checkPermissionForDataDelivery(context, "android.permission.BLUETOOTH_CONNECT", attributionSource, str);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int handleShellCommand(ParcelFileDescriptor parcelFileDescriptor, ParcelFileDescriptor parcelFileDescriptor2, ParcelFileDescriptor parcelFileDescriptor3, String[] strArr) {
        return new BluetoothShellCommand(this, this.mContext).exec(this, parcelFileDescriptor.getFileDescriptor(), parcelFileDescriptor2.getFileDescriptor(), parcelFileDescriptor3.getFileDescriptor(), strArr);
    }

    @SuppressLint({"NewApi"})
    static Bundle getTempAllowlistBroadcastOptions() {
        BroadcastOptions makeBasic = BroadcastOptions.makeBasic();
        makeBasic.setTemporaryAppAllowlist(IDeviceIdleControllerExt.ADVANCE_TIME, 0, 203, "");
        return makeBasic.toBundle();
    }

    private ComponentName resolveSystemService(Intent intent, PackageManager packageManager, int i) {
        List<ResolveInfo> queryIntentServices = packageManager.queryIntentServices(intent, i);
        ComponentName componentName = null;
        if (queryIntentServices == null) {
            return null;
        }
        for (int i2 = 0; i2 < queryIntentServices.size(); i2++) {
            ResolveInfo resolveInfo = queryIntentServices.get(i2);
            if ((resolveInfo.serviceInfo.applicationInfo.flags & 1) != 0) {
                ServiceInfo serviceInfo = resolveInfo.serviceInfo;
                ComponentName componentName2 = new ComponentName(serviceInfo.applicationInfo.packageName, serviceInfo.name);
                if (componentName != null) {
                    throw new IllegalStateException("Multiple system services handle " + intent + ": " + componentName + ", " + componentName2);
                }
                componentName = componentName2;
            }
        }
        return componentName;
    }

    private boolean isPrivileged(int i, int i2) {
        return this.mContext.checkPermission(BLUETOOTH_PRIVILEGED, i, i2) == 0 || this.mContext.getPackageManager().checkSignatures(i2, 1000) == 0;
    }

    private Pair<UserHandle, ComponentName> getDeviceOwner() {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) this.mContext.getSystemService(DevicePolicyManager.class);
        if (devicePolicyManager == null) {
            return null;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            UserHandle deviceOwnerUser = devicePolicyManager.getDeviceOwnerUser();
            ComponentName deviceOwnerComponentOnAnyUser = devicePolicyManager.getDeviceOwnerComponentOnAnyUser();
            if (deviceOwnerUser == null || deviceOwnerComponentOnAnyUser == null || deviceOwnerComponentOnAnyUser.getPackageName() == null) {
                return null;
            }
            return new Pair<>(deviceOwnerUser, deviceOwnerComponentOnAnyUser);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private boolean isDeviceOwner(int i, String str) {
        if (str == null) {
            Log.e(TAG, "isDeviceOwner: packageName is null, returning false");
            return false;
        }
        Pair<UserHandle, ComponentName> deviceOwner = getDeviceOwner();
        return deviceOwner != null && ((UserHandle) deviceOwner.first).equals(UserHandle.getUserHandleForUid(i)) && ((ComponentName) deviceOwner.second).getPackageName().equals(str);
    }

    private boolean isProfileOwner(int i, String str) {
        try {
            Context context = this.mContext;
            Context createPackageContextAsUser = context.createPackageContextAsUser(context.getPackageName(), 0, UserHandle.getUserHandleForUid(i));
            if (createPackageContextAsUser == null) {
                Log.e(TAG, "Unable to retrieve user context for " + i);
                return false;
            }
            DevicePolicyManager devicePolicyManager = (DevicePolicyManager) createPackageContextAsUser.getSystemService(DevicePolicyManager.class);
            if (devicePolicyManager == null) {
                Log.w(TAG, "Error retrieving DPM service");
                return false;
            }
            return devicePolicyManager.isProfileOwnerApp(str);
        } catch (PackageManager.NameNotFoundException unused) {
            Log.e(TAG, "Unknown package name");
            return false;
        }
    }

    public boolean isSystem(String str, int i) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return (this.mContext.getPackageManager().getApplicationInfoAsUser(str, 0, UserHandle.getUserHandleForUid(i)).flags & 129) != 0;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @RequiresPermission(BLUETOOTH_PRIVILEGED)
    public int setBtHciSnoopLogMode(int i) {
        BluetoothProperties.snoop_log_mode_values snoop_log_mode_valuesVar;
        this.mContext.enforceCallingOrSelfPermission(BLUETOOTH_PRIVILEGED, "Need BLUETOOTH_PRIVILEGED permission");
        if (i == 0) {
            snoop_log_mode_valuesVar = BluetoothProperties.snoop_log_mode_values.DISABLED;
        } else if (i == 1) {
            snoop_log_mode_valuesVar = BluetoothProperties.snoop_log_mode_values.FILTERED;
        } else if (i == 2) {
            snoop_log_mode_valuesVar = BluetoothProperties.snoop_log_mode_values.FULL;
        } else {
            Log.e(TAG, "setBtHciSnoopLogMode: Not a valid mode:" + i);
            return 21;
        }
        try {
            BluetoothProperties.snoop_log_mode(snoop_log_mode_valuesVar);
            return 0;
        } catch (RuntimeException e) {
            Log.e(TAG, "setBtHciSnoopLogMode: Failed to set mode to " + i + ": " + e);
            return Integer.MAX_VALUE;
        }
    }

    @RequiresPermission(BLUETOOTH_PRIVILEGED)
    public int getBtHciSnoopLogMode() {
        this.mContext.enforceCallingOrSelfPermission(BLUETOOTH_PRIVILEGED, "Need BLUETOOTH_PRIVILEGED permission");
        BluetoothProperties.snoop_log_mode_values snoop_log_mode_valuesVar = (BluetoothProperties.snoop_log_mode_values) BluetoothProperties.snoop_log_mode().orElse(BluetoothProperties.snoop_log_mode_values.DISABLED);
        if (snoop_log_mode_valuesVar == BluetoothProperties.snoop_log_mode_values.FILTERED) {
            return 1;
        }
        return snoop_log_mode_valuesVar == BluetoothProperties.snoop_log_mode_values.FULL ? 2 : 0;
    }

    private static boolean isBleSupported(Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le");
    }

    private static boolean isAutomotive(Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.type.automotive");
    }

    private static boolean isWatch(Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.type.watch");
    }

    private static boolean isTv(Context context) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.hasSystemFeature("android.hardware.type.television") || packageManager.hasSystemFeature("android.software.leanback");
    }

    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (super.onTransact(i, parcel, parcel2, i2)) {
            return true;
        }
        if (this.DBG) {
            Log.d(TAG, "onTransact will goto OplusBluetoothManagerServiceExtImpl");
        }
        return this.mOplusBms.oplusOnTransact(i, parcel, parcel2, i2);
    }

    public IBluetoothManagerServiceWrapper getWrapper() {
        return this.mBmsWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class BluetoothManagerServiceWrapper implements IBluetoothManagerServiceWrapper {
        private BluetoothManagerServiceWrapper() {
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public void setNameAddressOnly(boolean z) {
            BluetoothManagerService.this.mHandler.mGetNameAddressOnly = z;
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public boolean getNameAddressOnly() {
            return BluetoothManagerService.this.mHandler.mGetNameAddressOnly;
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public boolean getQuietEnable() {
            return BluetoothManagerService.this.mQuietEnable;
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public boolean getEnable() {
            return BluetoothManagerService.this.mEnable;
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public ReentrantReadWriteLock getBluetoothLock() {
            return BluetoothManagerService.this.mBluetoothLock;
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public IBluetooth getBluetooth() {
            return BluetoothManagerService.this.mBluetooth;
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public Object getHandler() {
            return BluetoothManagerService.this.mHandler;
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public void storeNameAndAddress(String str, String str2) {
            BluetoothManagerService.this.storeNameAndAddress(str, str2);
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public void setEnableExternal(boolean z) {
            BluetoothManagerService.this.mEnableExternal = z;
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public void clearBleApps() {
            BluetoothManagerService.this.clearBleApps();
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public void handleDisable() {
            BluetoothManagerService.this.handleDisable();
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public void handleEnable(boolean z) {
            BluetoothManagerService.this.handleEnable(z);
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public boolean waitForState(Set<Integer> set) {
            return BluetoothManagerService.this.waitForState(set);
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public void unbindAndFinish() {
            BluetoothManagerService.this.unbindAndFinish();
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public void persistBluetoothSetting(int i) {
            BluetoothManagerService.this.persistBluetoothSetting(i);
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public void propagateForegroundUserId(int i) {
            BluetoothManagerService.this.propagateForegroundUserId(i);
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public void OnBrEdrDown(AttributionSource attributionSource) {
            try {
                BluetoothManagerService.this.synchronousOnBrEdrDown(attributionSource);
            } catch (RemoteException | TimeoutException e) {
                Log.e(BluetoothManagerService.TAG, "Unable to call disable()", e);
            }
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public void enableBluetooth(boolean z, AttributionSource attributionSource) {
            try {
                BluetoothManagerService.this.synchronousEnable(z, attributionSource);
            } catch (RemoteException | TimeoutException e) {
                Log.e(BluetoothManagerService.TAG, "Unable to call disable()", e);
            }
        }

        @Override // com.android.server.bluetooth.IBluetoothManagerServiceWrapper
        public Bundle syncEnableDisableFlag() {
            Bundle bundle = new Bundle();
            bundle.putBoolean(IOplusBluetoothManagerServiceExt.FLAG_ENABLE, BluetoothManagerService.this.mEnable);
            bundle.putBoolean(IOplusBluetoothManagerServiceExt.FLAG_QUITE_ENABLE, BluetoothManagerService.this.mQuietEnable);
            bundle.putBoolean(IOplusBluetoothManagerServiceExt.FLAG_ENABLE_EXTERNAL, BluetoothManagerService.this.mEnableExternal);
            return bundle;
        }
    }
}
