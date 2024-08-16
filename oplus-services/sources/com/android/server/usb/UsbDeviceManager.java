package com.android.server.usb;

import android.R;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.common.OplusFeatureCache;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.debug.AdbManagerInternal;
import android.debug.IAdbTransport;
import android.hardware.usb.ParcelableUsbPort;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbConfiguration;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbPort;
import android.hardware.usb.UsbPortStatus;
import android.hardware.usb.gadget.V1_0.GadgetFunction;
import android.hidl.manager.V1_0.IServiceNotification;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.os.IHwBinder;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UEventObserver;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.Settings;
import android.util.Pair;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.os.SomeArgs;
import com.android.internal.usb.DumpUtils;
import com.android.internal.util.IndentingPrintWriter;
import com.android.internal.util.dump.DualDumpOutputStream;
import com.android.server.FgThread;
import com.android.server.LocalServices;
import com.android.server.engineer.IOplusEngineerServiceExt;
import com.android.server.integrity.AppIntegrityManagerServiceImpl;
import com.android.server.job.controllers.JobStatus;
import com.android.server.policy.IInputExt;
import com.android.server.usb.descriptors.UsbDescriptor;
import com.android.server.usb.descriptors.UsbTerminalTypes;
import com.android.server.usb.hal.gadget.UsbGadgetHal;
import com.android.server.usb.hal.gadget.UsbGadgetHalInstance;
import com.android.server.utils.EventLogger;
import com.android.server.wm.ActivityTaskManagerInternal;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class UsbDeviceManager implements ActivityTaskManagerInternal.ScreenObserver {
    private static final int ACCESSORY_HANDSHAKE_TIMEOUT = 10000;
    private static final int ACCESSORY_REQUEST_TIMEOUT = 10000;
    private static final String ACCESSORY_START_MATCH = "DEVPATH=/devices/virtual/misc/usb_accessory";
    private static final String ADB_NOTIFICATION_CHANNEL_ID_TV = "usbdevicemanager.adb.tv";
    private static final int AUDIO_MODE_SOURCE = 1;
    private static final String AUDIO_SOURCE_PCM_PATH = "/sys/class/android_usb/android0/f_audio_source/pcm";
    private static final String BOOT_MODE_PROPERTY = "ro.bootmode";
    private static final boolean DEBUG = true;
    private static final int DEVICE_STATE_UPDATE_DELAY = 1000;
    private static final int DEVICE_STATE_UPDATE_DELAY_EXT = 1500;
    private static final int DUMPSYS_LOG_BUFFER = 200;
    private static final String FUNCTIONS_PATH = "/sys/class/android_usb/android0/functions";
    private static final int HOST_STATE_UPDATE_DELAY = 1000;
    private static final String MIDI_ALSA_PATH = "/sys/class/android_usb/android0/f_midi/alsa";
    private static final int MSG_ACCESSORY_HANDSHAKE_TIMEOUT = 20;
    private static final int MSG_ACCESSORY_MODE_ENTER_TIMEOUT = 8;
    private static final int MSG_BOOT_COMPLETED = 4;
    private static final int MSG_ENABLE_ADB = 1;
    private static final int MSG_FUNCTION_SWITCH_TIMEOUT = 17;
    private static final int MSG_GADGET_HAL_REGISTERED = 18;
    private static final int MSG_GET_CURRENT_USB_FUNCTIONS = 16;
    private static final int MSG_INCREASE_SENDSTRING_COUNT = 21;
    private static final int MSG_LOCALE_CHANGED = 11;
    private static final int MSG_RESET_USB_GADGET = 19;
    private static final int MSG_SET_CHARGING_FUNCTIONS = 14;
    private static final int MSG_SET_CURRENT_FUNCTIONS = 2;
    private static final int MSG_SET_FUNCTIONS_TIMEOUT = 15;
    private static final int MSG_SET_SCREEN_UNLOCKED_FUNCTIONS = 12;
    private static final int MSG_SYSTEM_READY = 3;
    private static final int MSG_UPDATE_CHARGING_STATE = 9;
    private static final int MSG_UPDATE_HAL_VERSION = 23;
    private static final int MSG_UPDATE_HOST_STATE = 10;
    private static final int MSG_UPDATE_PORT_STATE = 7;
    private static final int MSG_UPDATE_SCREEN_LOCK = 13;
    private static final int MSG_UPDATE_STATE = 0;
    private static final int MSG_UPDATE_USB_SPEED = 22;
    private static final int MSG_UPDATE_USER_RESTRICTIONS = 6;
    private static final int MSG_USER_SWITCHED = 5;
    private static final String NORMAL_BOOT = "normal";
    private static final String RNDIS_ETH_ADDR_PATH = "/sys/class/android_usb/android0/f_rndis/ethaddr";
    private static final String STATE_PATH = "/sys/class/android_usb/android0/state";
    private static final String TAG = "UsbDeviceManager";
    static final String UNLOCKED_CONFIG_PREF = "usb-screen-unlocked-config-%d";
    private static final String USB_PREFS_XML = "UsbDeviceManagerPrefs.xml";
    private static final String USB_STATE_MATCH = "DEVPATH=/devices/virtual/android_usb/android0";
    private static final String USB_STATE_MATCH_SEC = "DEVPATH=/devices/virtual/android_usb/android1";
    private static IOplusUsbDeviceFeature mIOplusUsbDeviceFeature;
    private static UsbGadgetHal mUsbGadgetHal;
    private static Set<Integer> sDenyInterfaces;
    private static EventLogger sEventLogger;

    @GuardedBy({"mLock"})
    private String[] mAccessoryStrings;
    private final ContentResolver mContentResolver;
    private final Context mContext;
    private HashMap<Long, FileDescriptor> mControlFds;

    @GuardedBy({"mLock"})
    private UsbProfileGroupSettingsManager mCurrentSettings;
    private UsbHandler mHandler;
    private final boolean mHasUsbAccessory;
    private final UEventObserver mUEventObserver;
    private static final UsbDeviceManagerWrapper USB_DEVICE_MANAGER_WRAPPER = new UsbDeviceManagerWrapper();
    private static final AtomicInteger sUsbOperationCount = new AtomicInteger();
    private final Object mLock = new Object();
    private IOplusUsbDeviceManagerCallback mUsbDeviceManagerCallback = new IOplusUsbDeviceManagerCallback() { // from class: com.android.server.usb.UsbDeviceManager.5
        @Override // com.android.server.usb.IOplusUsbDeviceManagerCallback
        public Context getUsbDeviceManagerContext() {
            return UsbDeviceManager.this.mContext;
        }
    };

    private native String[] nativeGetAccessoryStrings();

    private native int nativeGetAudioMode();

    private native boolean nativeIsStartRequested();

    private native ParcelFileDescriptor nativeOpenAccessory();

    private native FileDescriptor nativeOpenControl(String str);

    public void onAwakeStateChanged(boolean z) {
    }

    static {
        HashSet hashSet = new HashSet();
        sDenyInterfaces = hashSet;
        hashSet.add(1);
        sDenyInterfaces.add(2);
        sDenyInterfaces.add(3);
        sDenyInterfaces.add(7);
        sDenyInterfaces.add(8);
        sDenyInterfaces.add(9);
        sDenyInterfaces.add(10);
        sDenyInterfaces.add(11);
        sDenyInterfaces.add(13);
        sDenyInterfaces.add(14);
        sDenyInterfaces.add(Integer.valueOf(UsbDescriptor.CLASSID_WIRELESS));
        mIOplusUsbDeviceFeature = null;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class UsbUEventObserver extends UEventObserver {
        private UsbUEventObserver() {
        }

        public void onUEvent(UEventObserver.UEvent uEvent) {
            Slog.v(UsbDeviceManager.TAG, "USB UEVENT: " + uEvent.toString());
            if (UsbDeviceManager.sEventLogger != null) {
                UsbDeviceManager.sEventLogger.enqueue(new EventLogger.StringEvent("USB UEVENT: " + uEvent.toString()));
            } else {
                Slog.d(UsbDeviceManager.TAG, "sEventLogger == null");
            }
            String str = uEvent.get("USB_STATE");
            String str2 = uEvent.get("ACCESSORY");
            if (str != null) {
                UsbDeviceManager.this.mHandler.updateState(str);
                return;
            }
            if ("GETPROTOCOL".equals(str2)) {
                Slog.d(UsbDeviceManager.TAG, "got accessory get protocol");
                UsbDeviceManager.this.mHandler.setAccessoryUEventTime(SystemClock.elapsedRealtime());
                UsbDeviceManager.this.resetAccessoryHandshakeTimeoutHandler();
            } else if ("SENDSTRING".equals(str2)) {
                Slog.d(UsbDeviceManager.TAG, "got accessory send string");
                UsbDeviceManager.this.mHandler.sendEmptyMessage(UsbDeviceManager.MSG_INCREASE_SENDSTRING_COUNT);
                UsbDeviceManager.this.resetAccessoryHandshakeTimeoutHandler();
            } else if ("START".equals(str2)) {
                Slog.d(UsbDeviceManager.TAG, "got accessory start");
                UsbDeviceManager.getOplusUsbDeviceFeature().setUsbAccessoryStartFlag();
                UsbDeviceManager.this.mHandler.removeMessages(20);
                UsbDeviceManager.this.mHandler.setStartAccessoryTrue();
                UsbDeviceManager.this.startAccessoryMode();
            }
        }
    }

    public void onKeyguardStateChanged(boolean z) {
        int currentUser = ActivityManager.getCurrentUser();
        boolean isDeviceSecure = ((KeyguardManager) this.mContext.getSystemService(KeyguardManager.class)).isDeviceSecure(currentUser);
        Slog.v(TAG, "onKeyguardStateChanged: isShowing:" + z + " secure:" + isDeviceSecure + " user:" + currentUser);
        this.mHandler.sendMessage(13, z && isDeviceSecure);
    }

    public void onUnlockUser(int i) {
        onKeyguardStateChanged(false);
    }

    public UsbDeviceManager(Context context, UsbAlsaManager usbAlsaManager, UsbSettingsManager usbSettingsManager, UsbPermissionManager usbPermissionManager) {
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        this.mHasUsbAccessory = context.getPackageManager().hasSystemFeature("android.hardware.usb.accessory");
        initRndisAddress();
        int incrementAndGet = sUsbOperationCount.incrementAndGet();
        mUsbGadgetHal = UsbGadgetHalInstance.getInstance(this, null);
        String str = TAG;
        Slog.d(str, "getInstance done");
        this.mControlFds = new HashMap<>();
        FileDescriptor nativeOpenControl = nativeOpenControl("mtp");
        if (nativeOpenControl == null) {
            Slog.e(str, "Failed to open control for mtp");
        }
        this.mControlFds.put(4L, nativeOpenControl);
        FileDescriptor nativeOpenControl2 = nativeOpenControl("ptp");
        if (nativeOpenControl2 == null) {
            Slog.e(str, "Failed to open control for ptp");
        }
        this.mControlFds.put(16L, nativeOpenControl2);
        if (mUsbGadgetHal == null) {
            this.mHandler = new UsbHandlerLegacy(FgThread.get().getLooper(), context, this, usbAlsaManager, usbPermissionManager);
        } else {
            this.mHandler = new UsbHandlerHal(FgThread.get().getLooper(), context, this, usbAlsaManager, usbPermissionManager);
        }
        this.mHandler.handlerInitDone(incrementAndGet);
        if (nativeIsStartRequested()) {
            Slog.d(str, "accessory attached at boot");
            startAccessoryMode();
        }
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.usb.UsbDeviceManager.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                ParcelableUsbPort parcelableUsbPort = (ParcelableUsbPort) intent.getParcelableExtra("port", ParcelableUsbPort.class);
                UsbDeviceManager.this.mHandler.updateHostState(parcelableUsbPort.getUsbPort((UsbManager) context2.getSystemService(UsbManager.class)), (UsbPortStatus) intent.getParcelableExtra("portStatus", UsbPortStatus.class));
            }
        };
        BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() { // from class: com.android.server.usb.UsbDeviceManager.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                UsbDeviceManager.this.mHandler.sendMessage(9, intent.getIntExtra("plugged", -1) == 2);
            }
        };
        BroadcastReceiver broadcastReceiver3 = new BroadcastReceiver() { // from class: com.android.server.usb.UsbDeviceManager.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                Iterator<Map.Entry<String, UsbDevice>> it = ((UsbManager) context2.getSystemService("usb")).getDeviceList().entrySet().iterator();
                if (intent.getAction().equals("android.hardware.usb.action.USB_DEVICE_ATTACHED")) {
                    UsbDeviceManager.this.mHandler.sendMessage(10, (Object) it, true);
                } else {
                    UsbDeviceManager.this.mHandler.sendMessage(10, (Object) it, false);
                }
            }
        };
        BroadcastReceiver broadcastReceiver4 = new BroadcastReceiver() { // from class: com.android.server.usb.UsbDeviceManager.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                UsbDeviceManager.this.mHandler.sendEmptyMessage(11);
            }
        };
        context.registerReceiver(broadcastReceiver, new IntentFilter("android.hardware.usb.action.USB_PORT_CHANGED"));
        IntentFilter intentFilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
        intentFilter.addCategory("oplusBrEx@android.intent.action.BATTERY_CHANGED@BATTERYSTATE=CHARGING_CHANGED");
        context.registerReceiver(broadcastReceiver2, intentFilter);
        IntentFilter intentFilter2 = new IntentFilter("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        intentFilter2.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        context.registerReceiver(broadcastReceiver3, intentFilter2);
        context.registerReceiver(broadcastReceiver4, new IntentFilter("android.intent.action.LOCALE_CHANGED"));
        UsbUEventObserver usbUEventObserver = new UsbUEventObserver();
        this.mUEventObserver = usbUEventObserver;
        usbUEventObserver.startObserving(USB_STATE_MATCH);
        usbUEventObserver.startObserving(USB_STATE_MATCH_SEC);
        usbUEventObserver.startObserving(ACCESSORY_START_MATCH);
        sEventLogger = new EventLogger(200, "UsbDeviceManager activity");
    }

    UsbProfileGroupSettingsManager getCurrentSettings() {
        UsbProfileGroupSettingsManager usbProfileGroupSettingsManager;
        synchronized (this.mLock) {
            usbProfileGroupSettingsManager = this.mCurrentSettings;
        }
        return usbProfileGroupSettingsManager;
    }

    String[] getAccessoryStrings() {
        String[] strArr;
        synchronized (this.mLock) {
            strArr = this.mAccessoryStrings;
        }
        return strArr;
    }

    public void systemReady() {
        Slog.d(TAG, "systemReady");
        ((ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class)).registerScreenObserver(this);
        this.mHandler.sendEmptyMessage(3);
    }

    public void bootCompleted() {
        Slog.d(TAG, "boot completed");
        this.mHandler.sendEmptyMessage(4);
    }

    public void setCurrentUser(int i, UsbProfileGroupSettingsManager usbProfileGroupSettingsManager) {
        synchronized (this.mLock) {
            this.mCurrentSettings = usbProfileGroupSettingsManager;
            this.mHandler.obtainMessage(5, i, 0).sendToTarget();
        }
    }

    public void updateUserRestrictions() {
        this.mHandler.sendEmptyMessage(6);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetAccessoryHandshakeTimeoutHandler() {
        if ((getCurrentFunctions() & 2) == 0) {
            this.mHandler.removeMessages(20);
            UsbHandler usbHandler = this.mHandler;
            usbHandler.sendMessageDelayed(usbHandler.obtainMessage(20), JobStatus.DEFAULT_TRIGGER_UPDATE_DELAY);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startAccessoryMode() {
        if (this.mHasUsbAccessory) {
            int incrementAndGet = sUsbOperationCount.incrementAndGet();
            this.mAccessoryStrings = nativeGetAccessoryStrings();
            boolean z = false;
            boolean z2 = nativeGetAudioMode() == 1;
            String[] strArr = this.mAccessoryStrings;
            if (strArr != null && strArr[0] != null && strArr[1] != null) {
                z = true;
            }
            long j = z ? 2L : 0L;
            if (z2) {
                j |= 64;
            }
            if (j != 0) {
                UsbHandler usbHandler = this.mHandler;
                usbHandler.sendMessageDelayed(usbHandler.obtainMessage(8), JobStatus.DEFAULT_TRIGGER_UPDATE_DELAY);
                UsbHandler usbHandler2 = this.mHandler;
                usbHandler2.sendMessageDelayed(usbHandler2.obtainMessage(20), JobStatus.DEFAULT_TRIGGER_UPDATE_DELAY);
                setCurrentFunctions(j, incrementAndGet);
            }
        }
    }

    private static void initRndisAddress() {
        int[] iArr = new int[6];
        iArr[0] = 2;
        String str = SystemProperties.get("ro.serialno", "1234567890ABCDEF");
        int length = str.length();
        for (int i = 0; i < length; i++) {
            int i2 = (i % 5) + 1;
            iArr[i2] = iArr[i2] ^ str.charAt(i);
        }
        try {
            FileUtils.stringToFile(RNDIS_ETH_ADDR_PATH, String.format(Locale.US, "%02X:%02X:%02X:%02X:%02X:%02X", Integer.valueOf(iArr[0]), Integer.valueOf(iArr[1]), Integer.valueOf(iArr[2]), Integer.valueOf(iArr[3]), Integer.valueOf(iArr[4]), Integer.valueOf(iArr[5])));
        } catch (IOException unused) {
            Slog.i(TAG, "failed to write to /sys/class/android_usb/android0/f_rndis/ethaddr");
        }
    }

    public static void logAndPrint(int i, IndentingPrintWriter indentingPrintWriter, String str) {
        Slog.println(i, TAG, str);
        if (indentingPrintWriter != null) {
            indentingPrintWriter.println(str);
        }
    }

    public static void logAndPrintException(IndentingPrintWriter indentingPrintWriter, String str, Exception exc) {
        Slog.e(TAG, str, exc);
        if (indentingPrintWriter != null) {
            indentingPrintWriter.println(str + exc);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static abstract class UsbHandler extends Handler {
        protected static final String USB_PERSISTENT_CONFIG_PROPERTY = "persist.sys.usb.config";
        private long mAccessoryConnectionStartTime;
        private boolean mAdbNotificationShown;
        private boolean mAudioAccessoryConnected;
        private boolean mAudioAccessorySupported;
        private boolean mAudioSourceEnabled;
        protected boolean mBootCompleted;
        private Intent mBroadcastedIntent;
        protected boolean mConfigured;
        protected boolean mConnected;
        private boolean mConnectedToDataDisabledPort;
        protected final ContentResolver mContentResolver;
        private final Context mContext;
        private UsbAccessory mCurrentAccessory;
        protected long mCurrentFunctions;
        protected boolean mCurrentFunctionsApplied;
        protected int mCurrentGadgetHalVersion;
        protected boolean mCurrentUsbFunctionsReceived;
        protected int mCurrentUser;
        private boolean mHideUsbNotification;
        private boolean mHostConnected;
        private boolean mInHostModeWithNoAccessoryConnected;
        private int mMidiCard;
        private int mMidiDevice;
        private boolean mMidiEnabled;
        private NotificationManager mNotificationManager;
        protected boolean mPendingBootAccessoryHandshakeBroadcast;
        private boolean mPendingBootBroadcast;
        private final UsbPermissionManager mPermissionManager;
        private int mPowerBrickConnectionStatus;
        protected boolean mResetUsbGadgetDisableDebounce;
        private boolean mScreenLocked;
        protected long mScreenUnlockedFunctions;
        private int mSendStringCount;
        protected SharedPreferences mSettings;
        private boolean mSinkPower;
        private boolean mSourcePower;
        private boolean mStartAccessory;
        private boolean mSupportsAllCombinations;
        private boolean mSystemReady;
        private boolean mUsbAccessoryConnected;
        private final UsbAlsaManager mUsbAlsaManager;
        private boolean mUsbCharging;
        protected final UsbDeviceManager mUsbDeviceManager;
        private int mUsbNotificationId;
        protected int mUsbSpeed;
        protected boolean mUseUsbNotification;

        public abstract void getUsbSpeedCb(int i);

        public abstract void handlerInitDone(int i);

        protected boolean isUsbDataTransferActive(long j) {
            return ((4 & j) == 0 && (j & 16) == 0) ? false : true;
        }

        public abstract void resetCb(int i);

        public abstract void setCurrentUsbFunctionsCb(long j, int i, int i2, long j2, boolean z);

        protected abstract void setEnabledFunctions(long j, boolean z, int i);

        protected void updateAdbNotification(boolean z) {
        }

        protected void updateUsbNotification(boolean z) {
        }

        UsbHandler(Looper looper, Context context, UsbDeviceManager usbDeviceManager, UsbAlsaManager usbAlsaManager, UsbPermissionManager usbPermissionManager) {
            super(looper);
            this.mAccessoryConnectionStartTime = 0L;
            boolean z = false;
            this.mSendStringCount = 0;
            this.mStartAccessory = false;
            this.mContext = context;
            this.mUsbDeviceManager = usbDeviceManager;
            this.mUsbAlsaManager = usbAlsaManager;
            this.mPermissionManager = usbPermissionManager;
            this.mContentResolver = context.getContentResolver();
            this.mCurrentUser = ActivityManager.getCurrentUser();
            this.mScreenLocked = true;
            SharedPreferences pinnedSharedPrefs = getPinnedSharedPrefs(context);
            this.mSettings = pinnedSharedPrefs;
            if (pinnedSharedPrefs == null) {
                Slog.e(UsbDeviceManager.TAG, "Couldn't load shared preferences");
            } else {
                this.mScreenUnlockedFunctions = UsbManager.usbFunctionsFromString(pinnedSharedPrefs.getString(String.format(Locale.ENGLISH, UsbDeviceManager.UNLOCKED_CONFIG_PREF, Integer.valueOf(this.mCurrentUser)), ""));
            }
            StorageManager from = StorageManager.from(context);
            StorageVolume primaryVolume = from != null ? from.getPrimaryVolume() : null;
            if (!(primaryVolume != null && primaryVolume.allowMassStorage()) && context.getResources().getBoolean(17891878)) {
                z = true;
            }
            this.mUseUsbNotification = z;
        }

        public void sendMessage(int i, boolean z) {
            removeMessages(i);
            Message obtain = Message.obtain(this, i);
            obtain.arg1 = z ? 1 : 0;
            sendMessage(obtain);
        }

        public boolean sendMessage(int i) {
            removeMessages(i);
            return sendMessageDelayed(Message.obtain(this, i), 0L);
        }

        public void sendMessage(int i, int i2) {
            removeMessages(i);
            Message obtain = Message.obtain(this, i);
            obtain.arg1 = i2;
            sendMessage(obtain);
        }

        public void sendMessage(int i, Object obj) {
            removeMessages(i);
            Message obtain = Message.obtain(this, i);
            obtain.obj = obj;
            sendMessage(obtain);
        }

        public void sendMessage(int i, Object obj, int i2) {
            removeMessages(i);
            Message obtain = Message.obtain(this, i);
            obtain.obj = obj;
            obtain.arg1 = i2;
            sendMessage(obtain);
        }

        public void sendMessage(int i, boolean z, int i2) {
            removeMessages(i);
            Message obtain = Message.obtain(this, i);
            obtain.arg1 = z ? 1 : 0;
            obtain.arg2 = i2;
            sendMessage(obtain);
        }

        public void sendMessage(int i, Object obj, boolean z) {
            removeMessages(i);
            Message obtain = Message.obtain(this, i);
            obtain.obj = obj;
            obtain.arg1 = z ? 1 : 0;
            sendMessage(obtain);
        }

        public void sendMessage(int i, long j, boolean z, int i2) {
            removeMessages(i);
            Message obtain = Message.obtain(this, i);
            obtain.obj = Long.valueOf(j);
            obtain.arg1 = z ? 1 : 0;
            obtain.arg2 = i2;
            sendMessage(obtain);
        }

        public void sendMessage(int i, boolean z, boolean z2) {
            removeMessages(i);
            Message obtain = Message.obtain(this, i);
            obtain.arg1 = z ? 1 : 0;
            obtain.arg2 = z2 ? 1 : 0;
            sendMessage(obtain);
        }

        public void sendMessageDelayed(int i, boolean z, long j) {
            removeMessages(i);
            Message obtain = Message.obtain(this, i);
            obtain.arg1 = z ? 1 : 0;
            sendMessageDelayed(obtain, j);
        }

        /* JADX WARN: Removed duplicated region for block: B:10:0x0060  */
        /* JADX WARN: Removed duplicated region for block: B:16:0x0068  */
        /* JADX WARN: Removed duplicated region for block: B:7:0x0026  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void updateState(String str) {
            int i;
            int i2;
            long j;
            if ("DISCONNECTED".equals(str)) {
                i = 0;
            } else if (!"CONNECTED".equals(str)) {
                if (!"CONFIGURED".equals(str)) {
                    Slog.e(UsbDeviceManager.TAG, "unknown state " + str);
                    return;
                }
                i = 1;
            } else {
                i = 1;
                i2 = 0;
                if (i == 1) {
                    removeMessages(17);
                }
                Message obtain = Message.obtain(this, 0);
                obtain.arg1 = i;
                obtain.arg2 = i2;
                Slog.i(UsbDeviceManager.TAG, "mResetUsbGadgetDisableDebounce:" + this.mResetUsbGadgetDisableDebounce + " connected:" + i + "configured:" + i2);
                if (!this.mResetUsbGadgetDisableDebounce) {
                    sendMessage(obtain);
                    if (i == 1) {
                        this.mResetUsbGadgetDisableDebounce = false;
                        return;
                    }
                    return;
                }
                if (i2 == 0) {
                    removeMessages(0);
                    Slog.i(UsbDeviceManager.TAG, "removeMessages MSG_UPDATE_STATE");
                }
                if (i == 1) {
                    removeMessages(17);
                }
                if (i == 0) {
                    j = this.mScreenLocked ? 1000 : UsbDeviceManager.DEVICE_STATE_UPDATE_DELAY_EXT;
                } else {
                    j = 0;
                }
                sendMessageDelayed(obtain, j);
                return;
            }
            i2 = i;
            if (i == 1) {
            }
            Message obtain2 = Message.obtain(this, 0);
            obtain2.arg1 = i;
            obtain2.arg2 = i2;
            Slog.i(UsbDeviceManager.TAG, "mResetUsbGadgetDisableDebounce:" + this.mResetUsbGadgetDisableDebounce + " connected:" + i + "configured:" + i2);
            if (!this.mResetUsbGadgetDisableDebounce) {
            }
        }

        public void updateHostState(UsbPort usbPort, UsbPortStatus usbPortStatus) {
            Slog.i(UsbDeviceManager.TAG, "updateHostState " + usbPort + " status=" + usbPortStatus);
            SomeArgs obtain = SomeArgs.obtain();
            obtain.arg1 = usbPort;
            obtain.arg2 = usbPortStatus;
            removeMessages(7);
            sendMessageDelayed(obtainMessage(7, obtain), 1000L);
        }

        private void setAdbEnabled(boolean z, int i) {
            Slog.d(UsbDeviceManager.TAG, "setAdbEnabled: " + z);
            if (z) {
                setSystemProperty(USB_PERSISTENT_CONFIG_PROPERTY, AppIntegrityManagerServiceImpl.ADB_INSTALLER);
            } else {
                setSystemProperty(USB_PERSISTENT_CONFIG_PROPERTY, "");
            }
            setEnabledFunctions(this.mCurrentFunctions, true, i);
            updateAdbNotification(false);
        }

        protected boolean isUsbTransferAllowed() {
            return !((UserManager) this.mContext.getSystemService("user")).hasUserRestriction("no_usb_file_transfer");
        }

        private void updateCurrentAccessory() {
            int incrementAndGet = UsbDeviceManager.sUsbOperationCount.incrementAndGet();
            boolean hasMessages = hasMessages(8);
            if (!this.mConfigured || !hasMessages) {
                if (!hasMessages) {
                    notifyAccessoryModeExit(incrementAndGet);
                    return;
                } else {
                    Slog.v(UsbDeviceManager.TAG, "Debouncing accessory mode exit");
                    return;
                }
            }
            String[] accessoryStrings = this.mUsbDeviceManager.getAccessoryStrings();
            if (accessoryStrings != null && accessoryStrings[0] != null) {
                UsbSerialReader usbSerialReader = new UsbSerialReader(this.mContext, this.mPermissionManager, accessoryStrings[5]);
                UsbAccessory usbAccessory = new UsbAccessory(accessoryStrings[0], accessoryStrings[1], accessoryStrings[2], accessoryStrings[3], accessoryStrings[4], usbSerialReader);
                this.mCurrentAccessory = usbAccessory;
                usbSerialReader.setDevice(usbAccessory);
                Slog.d(UsbDeviceManager.TAG, "entering USB accessory mode: " + this.mCurrentAccessory);
                if (this.mBootCompleted) {
                    this.mUsbDeviceManager.getCurrentSettings().accessoryAttached(this.mCurrentAccessory);
                    removeMessages(20);
                    broadcastUsbAccessoryHandshake();
                    return;
                }
                return;
            }
            Slog.e(UsbDeviceManager.TAG, "nativeGetAccessoryStrings failed");
        }

        private void notifyAccessoryModeExit(int i) {
            Slog.d(UsbDeviceManager.TAG, "exited USB accessory mode");
            setEnabledFunctions(8L, false, i);
            UsbDeviceManager.getOplusUsbDeviceFeature().initUsbAccessoryStartFlag();
            UsbAccessory usbAccessory = this.mCurrentAccessory;
            if (usbAccessory != null) {
                if (this.mBootCompleted) {
                    this.mPermissionManager.usbAccessoryRemoved(usbAccessory);
                }
                this.mCurrentAccessory = null;
            }
        }

        protected SharedPreferences getPinnedSharedPrefs(Context context) {
            return context.createDeviceProtectedStorageContext().getSharedPreferences(new File(Environment.getDataSystemDeDirectory(0), UsbDeviceManager.USB_PREFS_XML), 0);
        }

        private boolean isUsbStateChanged(Intent intent) {
            Set<String> keySet = intent.getExtras().keySet();
            Intent intent2 = this.mBroadcastedIntent;
            if (intent2 == null) {
                Iterator<String> it = keySet.iterator();
                while (it.hasNext()) {
                    if (intent.getBooleanExtra(it.next(), false)) {
                        return true;
                    }
                }
            } else {
                if (!keySet.equals(intent2.getExtras().keySet())) {
                    return true;
                }
                for (String str : keySet) {
                    if (intent.getBooleanExtra(str, false) != this.mBroadcastedIntent.getBooleanExtra(str, false)) {
                        return true;
                    }
                }
            }
            return false;
        }

        private void broadcastUsbAccessoryHandshake() {
            Intent putExtra = new Intent("android.hardware.usb.action.USB_ACCESSORY_HANDSHAKE").addFlags(285212672).putExtra("android.hardware.usb.extra.ACCESSORY_UEVENT_TIME", this.mAccessoryConnectionStartTime).putExtra("android.hardware.usb.extra.ACCESSORY_STRING_COUNT", this.mSendStringCount).putExtra("android.hardware.usb.extra.ACCESSORY_START", this.mStartAccessory).putExtra("android.hardware.usb.extra.ACCESSORY_HANDSHAKE_END", SystemClock.elapsedRealtime());
            Slog.d(UsbDeviceManager.TAG, "broadcasting " + putExtra + " extras: " + putExtra.getExtras());
            sendStickyBroadcast(putExtra);
            resetUsbAccessoryHandshakeDebuggingInfo();
        }

        protected void updateUsbStateBroadcastIfNeeded(long j) {
            Intent intent = new Intent("android.hardware.usb.action.USB_STATE");
            intent.addFlags(822083584);
            intent.addFlags(1048576);
            intent.putExtra("connected", this.mConnected);
            intent.putExtra("host_connected", this.mHostConnected);
            intent.putExtra("configured", this.mConfigured);
            intent.putExtra("unlocked", isUsbTransferAllowed() && isUsbDataTransferActive(this.mCurrentFunctions));
            while (j != 0) {
                intent.putExtra(UsbManager.usbFunctionsToString(Long.highestOneBit(j)), true);
                j -= Long.highestOneBit(j);
            }
            if (!isUsbStateChanged(intent)) {
                Slog.d(UsbDeviceManager.TAG, "skip broadcasting " + intent + " extras: " + intent.getExtras());
                return;
            }
            Slog.d(UsbDeviceManager.TAG, "broadcasting " + intent + " extras: " + intent.getExtras());
            sendStickyBroadcast(intent);
            this.mBroadcastedIntent = intent;
        }

        protected void sendStickyBroadcast(Intent intent) {
            this.mContext.sendStickyBroadcastAsUser(intent, UserHandle.ALL);
            UsbDeviceManager.sEventLogger.enqueue(new EventLogger.StringEvent("USB intent: " + intent));
        }

        private void updateUsbFunctions() {
            updateMidiFunction();
        }

        private void updateMidiFunction() {
            boolean z = (this.mCurrentFunctions & 8) != 0;
            if (z != this.mMidiEnabled) {
                if (z) {
                    Scanner scanner = null;
                    Scanner scanner2 = null;
                    try {
                        try {
                            Scanner scanner3 = new Scanner(new File(UsbDeviceManager.MIDI_ALSA_PATH));
                            try {
                                this.mMidiCard = scanner3.nextInt();
                                int nextInt = scanner3.nextInt();
                                this.mMidiDevice = nextInt;
                                scanner3.close();
                                scanner = nextInt;
                            } catch (FileNotFoundException | NoSuchElementException e) {
                                e = e;
                                scanner2 = scanner3;
                                Slog.e(UsbDeviceManager.TAG, "could not open MIDI file", e);
                                if (scanner2 != null) {
                                    scanner2.close();
                                }
                                z = false;
                                scanner = scanner2;
                                this.mMidiEnabled = z;
                                this.mUsbAlsaManager.setPeripheralMidiState(!this.mMidiEnabled && this.mConfigured, this.mMidiCard, this.mMidiDevice);
                            } catch (Throwable th) {
                                th = th;
                                scanner = scanner3;
                                if (scanner != null) {
                                    scanner.close();
                                }
                                throw th;
                            }
                        } catch (FileNotFoundException | NoSuchElementException e2) {
                            e = e2;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                    }
                }
                this.mMidiEnabled = z;
            }
            this.mUsbAlsaManager.setPeripheralMidiState(!this.mMidiEnabled && this.mConfigured, this.mMidiCard, this.mMidiDevice);
        }

        private void setScreenUnlockedFunctions(int i) {
            setEnabledFunctions(this.mScreenUnlockedFunctions, false, i);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public static class AdbTransport extends IAdbTransport.Stub {
            private final UsbHandler mHandler;

            AdbTransport(UsbHandler usbHandler) {
                this.mHandler = usbHandler;
            }

            public void onAdbEnabled(boolean z, byte b) {
                if (b == 0) {
                    this.mHandler.sendMessage(1, z, UsbDeviceManager.sUsbOperationCount.incrementAndGet());
                }
                UsbDeviceManager.USB_DEVICE_MANAGER_WRAPPER.getExtImpl().onAdbEnabled(z);
            }
        }

        long getAppliedFunctions(long j) {
            if (j == 0) {
                return getChargingFunctions();
            }
            return isAdbEnabled() ? j | 1 : j;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 20) {
                Slog.v(UsbDeviceManager.TAG, "Accessory handshake timeout");
                if (this.mBootCompleted) {
                    broadcastUsbAccessoryHandshake();
                    return;
                } else {
                    Slog.v(UsbDeviceManager.TAG, "Pending broadcasting intent as not boot completed yet.");
                    this.mPendingBootAccessoryHandshakeBroadcast = true;
                    return;
                }
            }
            if (i != UsbDeviceManager.MSG_INCREASE_SENDSTRING_COUNT) {
                switch (i) {
                    case 0:
                        int incrementAndGet = UsbDeviceManager.sUsbOperationCount.incrementAndGet();
                        this.mConnected = message.arg1 == 1;
                        this.mConfigured = message.arg2 == 1;
                        UsbDeviceManager.getOplusUsbDeviceFeature().setUsbPlugFlag(this.mConnected);
                        Slog.i(UsbDeviceManager.TAG, "handleMessage MSG_UPDATE_STATE mConnected:" + this.mConnected + " mConfigured:" + this.mConfigured);
                        updateUsbNotification(false);
                        updateAdbNotification(false);
                        if (this.mBootCompleted) {
                            updateUsbStateBroadcastIfNeeded(getAppliedFunctions(this.mCurrentFunctions));
                        }
                        if ((2 & this.mCurrentFunctions) != 0) {
                            updateCurrentAccessory();
                        }
                        if (this.mBootCompleted) {
                            if (!this.mConnected && !hasMessages(8) && !hasMessages(17)) {
                                Slog.i(UsbDeviceManager.TAG, "usb plug out");
                                UsbDeviceManager.getOplusUsbDeviceFeature().initUsbAccessoryStartFlag();
                                if (!this.mScreenLocked && this.mScreenUnlockedFunctions != 0) {
                                    setScreenUnlockedFunctions(incrementAndGet);
                                } else {
                                    setEnabledFunctions(UsbDeviceManager.getOplusUsbDeviceFeature().usbFunctionsShuoldUseDefault(UsbManager.usbFunctionsToString(getEnabledFunctions())) ? getChargingFunctions() : 0L, false, incrementAndGet);
                                }
                            }
                            updateUsbFunctions();
                        } else {
                            this.mPendingBootBroadcast = true;
                        }
                        updateUsbSpeed();
                        return;
                    case 1:
                        setAdbEnabled(message.arg1 == 1, message.arg2);
                        return;
                    case 2:
                        long longValue = ((Long) message.obj).longValue();
                        int i2 = message.arg1;
                        if (UsbDeviceManager.getOplusUsbDeviceFeature().getUsbAccessoryStartFlag(longValue) || (hasMessages(8) && !UsbManager.usbFunctionsToString(longValue).toLowerCase().contains("accessory"))) {
                            Slog.i(UsbDeviceManager.TAG, "still entering accessory mode");
                            return;
                        } else {
                            setEnabledFunctions(longValue, false, i2);
                            return;
                        }
                    case 3:
                        int incrementAndGet2 = UsbDeviceManager.sUsbOperationCount.incrementAndGet();
                        this.mNotificationManager = (NotificationManager) this.mContext.getSystemService("notification");
                        ((AdbManagerInternal) LocalServices.getService(AdbManagerInternal.class)).registerTransport(new AdbTransport(this));
                        if (isTv()) {
                            this.mNotificationManager.createNotificationChannel(new NotificationChannel(UsbDeviceManager.ADB_NOTIFICATION_CHANNEL_ID_TV, this.mContext.getString(R.string.app_blocked_message), 4));
                        }
                        this.mSystemReady = true;
                        finishBoot(incrementAndGet2);
                        return;
                    case 4:
                        int incrementAndGet3 = UsbDeviceManager.sUsbOperationCount.incrementAndGet();
                        this.mBootCompleted = true;
                        finishBoot(incrementAndGet3);
                        return;
                    case 5:
                        int incrementAndGet4 = UsbDeviceManager.sUsbOperationCount.incrementAndGet();
                        if (this.mCurrentUser != message.arg1) {
                            Slog.v(UsbDeviceManager.TAG, "Current user switched to " + message.arg1);
                            int i3 = message.arg1;
                            this.mCurrentUser = i3;
                            this.mScreenLocked = true;
                            this.mScreenUnlockedFunctions = 0L;
                            SharedPreferences sharedPreferences = this.mSettings;
                            if (sharedPreferences != null) {
                                this.mScreenUnlockedFunctions = UsbManager.usbFunctionsFromString(sharedPreferences.getString(String.format(Locale.ENGLISH, UsbDeviceManager.UNLOCKED_CONFIG_PREF, Integer.valueOf(i3)), ""));
                            }
                            if (!SystemProperties.getBoolean("persist.sys.permission.enable", true)) {
                                setEnabledFunctions(0L, false, incrementAndGet4);
                                return;
                            } else {
                                setEnabledFunctions(0L, true, incrementAndGet4);
                                return;
                            }
                        }
                        return;
                    case 6:
                        int incrementAndGet5 = UsbDeviceManager.sUsbOperationCount.incrementAndGet();
                        if (!isUsbDataTransferActive(this.mCurrentFunctions) || isUsbTransferAllowed()) {
                            return;
                        }
                        setEnabledFunctions(0L, true, incrementAndGet5);
                        return;
                    case 7:
                        SomeArgs someArgs = (SomeArgs) message.obj;
                        boolean z = this.mHostConnected;
                        UsbPort usbPort = (UsbPort) someArgs.arg1;
                        UsbPortStatus usbPortStatus = (UsbPortStatus) someArgs.arg2;
                        if (usbPortStatus != null) {
                            this.mHostConnected = usbPortStatus.getCurrentDataRole() == 1;
                            this.mSourcePower = usbPortStatus.getCurrentPowerRole() == 1;
                            this.mSinkPower = usbPortStatus.getCurrentPowerRole() == 2;
                            this.mAudioAccessoryConnected = usbPortStatus.getCurrentMode() == 4;
                            this.mSupportsAllCombinations = usbPortStatus.isRoleCombinationSupported(1, 1) && usbPortStatus.isRoleCombinationSupported(2, 1) && usbPortStatus.isRoleCombinationSupported(1, 2) && usbPortStatus.isRoleCombinationSupported(2, 2);
                            this.mConnectedToDataDisabledPort = usbPortStatus.isConnected() && (usbPortStatus.getUsbDataStatus() != 1);
                            this.mPowerBrickConnectionStatus = usbPortStatus.getPowerBrickConnectionStatus();
                        } else {
                            this.mHostConnected = false;
                            this.mSourcePower = false;
                            this.mSinkPower = false;
                            this.mAudioAccessoryConnected = false;
                            this.mSupportsAllCombinations = false;
                            this.mConnectedToDataDisabledPort = false;
                            this.mPowerBrickConnectionStatus = 0;
                        }
                        if (this.mHostConnected) {
                            if (!this.mUsbAccessoryConnected) {
                                this.mInHostModeWithNoAccessoryConnected = true;
                            } else {
                                this.mInHostModeWithNoAccessoryConnected = false;
                            }
                        } else {
                            this.mInHostModeWithNoAccessoryConnected = false;
                        }
                        this.mAudioAccessorySupported = usbPort.isModeSupported(4);
                        someArgs.recycle();
                        updateUsbNotification(false);
                        if (this.mBootCompleted) {
                            if (this.mHostConnected || z) {
                                updateUsbStateBroadcastIfNeeded(getAppliedFunctions(this.mCurrentFunctions));
                                return;
                            }
                            return;
                        }
                        this.mPendingBootBroadcast = true;
                        return;
                    case 8:
                        int incrementAndGet6 = UsbDeviceManager.sUsbOperationCount.incrementAndGet();
                        Slog.v(UsbDeviceManager.TAG, "Accessory mode enter timeout: " + this.mConnected + " ,operationId: " + incrementAndGet6);
                        if (!this.mConnected || (this.mCurrentFunctions & 2) == 0) {
                            notifyAccessoryModeExit(incrementAndGet6);
                            return;
                        }
                        return;
                    case 9:
                        this.mUsbCharging = message.arg1 == 1;
                        updateUsbNotification(false);
                        return;
                    case 10:
                        Iterator it = (Iterator) message.obj;
                        this.mUsbAccessoryConnected = message.arg1 == 1;
                        Slog.i(UsbDeviceManager.TAG, "HOST_STATE connected:" + this.mUsbAccessoryConnected);
                        if (!it.hasNext()) {
                            this.mInHostModeWithNoAccessoryConnected = true;
                        } else {
                            this.mInHostModeWithNoAccessoryConnected = false;
                        }
                        this.mHideUsbNotification = false;
                        while (it.hasNext()) {
                            Map.Entry entry = (Map.Entry) it.next();
                            Slog.i(UsbDeviceManager.TAG, entry.getKey() + " = " + entry.getValue());
                            UsbDevice usbDevice = (UsbDevice) entry.getValue();
                            int configurationCount = usbDevice.getConfigurationCount() - 1;
                            while (configurationCount >= 0) {
                                UsbConfiguration configuration = usbDevice.getConfiguration(configurationCount);
                                configurationCount--;
                                int interfaceCount = configuration.getInterfaceCount() - 1;
                                while (true) {
                                    if (interfaceCount >= 0) {
                                        UsbInterface usbInterface = configuration.getInterface(interfaceCount);
                                        interfaceCount--;
                                        if (UsbDeviceManager.sDenyInterfaces.contains(Integer.valueOf(usbInterface.getInterfaceClass()))) {
                                            this.mHideUsbNotification = true;
                                        }
                                    }
                                }
                            }
                        }
                        updateUsbNotification(false);
                        return;
                    case 11:
                        updateAdbNotification(true);
                        updateUsbNotification(true);
                        return;
                    case 12:
                        int incrementAndGet7 = UsbDeviceManager.sUsbOperationCount.incrementAndGet();
                        this.mScreenUnlockedFunctions = ((Long) message.obj).longValue();
                        SharedPreferences sharedPreferences2 = this.mSettings;
                        if (sharedPreferences2 != null) {
                            SharedPreferences.Editor edit = sharedPreferences2.edit();
                            edit.putString(String.format(Locale.ENGLISH, UsbDeviceManager.UNLOCKED_CONFIG_PREF, Integer.valueOf(this.mCurrentUser)), UsbManager.usbFunctionsToString(this.mScreenUnlockedFunctions));
                            edit.commit();
                        }
                        if (!this.mScreenLocked && this.mScreenUnlockedFunctions != 0) {
                            setScreenUnlockedFunctions(incrementAndGet7);
                            return;
                        } else {
                            setEnabledFunctions(0L, false, incrementAndGet7);
                            return;
                        }
                    case 13:
                        int incrementAndGet8 = UsbDeviceManager.sUsbOperationCount.incrementAndGet();
                        int i4 = message.arg1;
                        if ((i4 == 1) == this.mScreenLocked) {
                            return;
                        }
                        boolean z2 = i4 == 1;
                        this.mScreenLocked = z2;
                        if (this.mBootCompleted) {
                            if (z2) {
                                if (this.mConnected) {
                                    return;
                                }
                                setEnabledFunctions(0L, false, incrementAndGet8);
                                return;
                            } else {
                                if (this.mScreenUnlockedFunctions == 0 || this.mCurrentFunctions != 0) {
                                    return;
                                }
                                setScreenUnlockedFunctions(incrementAndGet8);
                                return;
                            }
                        }
                        return;
                    default:
                        return;
                }
            }
            this.mSendStringCount++;
        }

        protected void finishBoot(int i) {
            UsbDeviceManager.getOplusUsbDeviceFeature().printFinishBootInfo(new OplusUsbDeviceFinishBootInfo(this.mConnected, this.mBootCompleted, this.mCurrentUsbFunctionsReceived, this.mSystemReady, this.mPendingBootBroadcast, this.mScreenLocked, UsbManager.usbFunctionsToString(this.mScreenUnlockedFunctions), isAdbEnabled()));
            if (this.mBootCompleted && this.mCurrentUsbFunctionsReceived && this.mSystemReady) {
                if (this.mPendingBootBroadcast) {
                    updateUsbStateBroadcastIfNeeded(getAppliedFunctions(this.mCurrentFunctions));
                    this.mPendingBootBroadcast = false;
                }
                if (!this.mScreenLocked && this.mScreenUnlockedFunctions != 0) {
                    setScreenUnlockedFunctions(i);
                } else {
                    UsbDeviceManager.getOplusUsbDeviceFeature().processUserTestHarnessIfNeed(this.mContext);
                    setEnabledFunctions(0L, UsbDeviceManager.getOplusUsbDeviceFeature().usbFunctionsShouldForceStart(), i);
                }
                if (this.mCurrentAccessory != null) {
                    this.mUsbDeviceManager.getCurrentSettings().accessoryAttached(this.mCurrentAccessory);
                    broadcastUsbAccessoryHandshake();
                } else if (this.mPendingBootAccessoryHandshakeBroadcast) {
                    broadcastUsbAccessoryHandshake();
                }
                this.mPendingBootAccessoryHandshakeBroadcast = false;
                updateUsbNotification(false);
                updateAdbNotification(false);
                updateUsbFunctions();
            }
        }

        public UsbAccessory getCurrentAccessory() {
            return this.mCurrentAccessory;
        }

        protected void updateUsbGadgetHalVersion() {
            sendMessage(23, (Object) null);
        }

        protected void updateUsbSpeed() {
            if (this.mCurrentGadgetHalVersion < 10) {
                this.mUsbSpeed = -1;
            } else if (this.mConnected && this.mConfigured) {
                sendMessage(UsbDeviceManager.MSG_UPDATE_USB_SPEED, (Object) null);
            } else {
                this.mUsbSpeed = -1;
            }
        }

        protected boolean isAdbEnabled() {
            return ((AdbManagerInternal) LocalServices.getService(AdbManagerInternal.class)).isAdbEnabled((byte) 0);
        }

        private boolean isTv() {
            return this.mContext.getPackageManager().hasSystemFeature("android.software.leanback");
        }

        protected long getChargingFunctions() {
            if (isAdbEnabled()) {
                return 1L;
            }
            return UsbDeviceManager.getOplusUsbDeviceFeature().getChargingFunctions();
        }

        protected void setSystemProperty(String str, String str2) {
            try {
                SystemProperties.set(str, str2);
            } catch (Exception unused) {
                Slog.w(UsbDeviceManager.TAG, "Failed to set property.");
            }
        }

        protected String getSystemProperty(String str, String str2) {
            return SystemProperties.get(str, str2);
        }

        protected void putGlobalSettings(ContentResolver contentResolver, String str, int i) {
            Settings.Global.putInt(contentResolver, str, i);
        }

        public long getEnabledFunctions() {
            return this.mCurrentFunctions;
        }

        public long getScreenUnlockedFunctions() {
            return this.mScreenUnlockedFunctions;
        }

        public int getUsbSpeed() {
            return this.mUsbSpeed;
        }

        public int getGadgetHalVersion() {
            return this.mCurrentGadgetHalVersion;
        }

        private void dumpFunctions(DualDumpOutputStream dualDumpOutputStream, String str, long j, long j2) {
            for (int i = 0; i < 63; i++) {
                long j3 = 1 << i;
                if ((j2 & j3) != 0) {
                    if (dualDumpOutputStream.isProto()) {
                        dualDumpOutputStream.write(str, j, j3);
                    } else {
                        dualDumpOutputStream.write(str, j, GadgetFunction.toString(j3));
                    }
                }
            }
        }

        public void dump(DualDumpOutputStream dualDumpOutputStream, String str, long j) {
            long start = dualDumpOutputStream.start(str, j);
            dumpFunctions(dualDumpOutputStream, "current_functions", 2259152797697L, this.mCurrentFunctions);
            dualDumpOutputStream.write("current_functions_applied", 1133871366146L, this.mCurrentFunctionsApplied);
            dumpFunctions(dualDumpOutputStream, "screen_unlocked_functions", 2259152797699L, this.mScreenUnlockedFunctions);
            dualDumpOutputStream.write("screen_locked", 1133871366148L, this.mScreenLocked);
            dualDumpOutputStream.write("connected", 1133871366149L, this.mConnected);
            dualDumpOutputStream.write("configured", 1133871366150L, this.mConfigured);
            UsbAccessory usbAccessory = this.mCurrentAccessory;
            if (usbAccessory != null) {
                DumpUtils.writeAccessory(dualDumpOutputStream, "current_accessory", 1146756268039L, usbAccessory);
            }
            dualDumpOutputStream.write("host_connected", 1133871366152L, this.mHostConnected);
            dualDumpOutputStream.write("source_power", 1133871366153L, this.mSourcePower);
            dualDumpOutputStream.write("sink_power", 1133871366154L, this.mSinkPower);
            dualDumpOutputStream.write("usb_charging", 1133871366155L, this.mUsbCharging);
            dualDumpOutputStream.write("hide_usb_notification", 1133871366156L, this.mHideUsbNotification);
            dualDumpOutputStream.write("audio_accessory_connected", 1133871366157L, this.mAudioAccessoryConnected);
            try {
                com.android.internal.util.dump.DumpUtils.writeStringIfNotNull(dualDumpOutputStream, "kernel_state", 1138166333455L, FileUtils.readTextFile(new File(UsbDeviceManager.STATE_PATH), 0, null).trim());
            } catch (FileNotFoundException unused) {
                Slog.w(UsbDeviceManager.TAG, "Ignore missing legacy kernel path in bugreport dump: kernel state:/sys/class/android_usb/android0/state");
            } catch (Exception e) {
                Slog.e(UsbDeviceManager.TAG, "Could not read kernel state", e);
            }
            try {
                com.android.internal.util.dump.DumpUtils.writeStringIfNotNull(dualDumpOutputStream, "kernel_function_list", 1138166333456L, FileUtils.readTextFile(new File(UsbDeviceManager.FUNCTIONS_PATH), 0, null).trim());
            } catch (FileNotFoundException unused2) {
                Slog.w(UsbDeviceManager.TAG, "Ignore missing legacy kernel path in bugreport dump: kernel function list:/sys/class/android_usb/android0/functions");
            } catch (Exception e2) {
                Slog.e(UsbDeviceManager.TAG, "Could not read kernel function list", e2);
            }
            dualDumpOutputStream.end(start);
        }

        public void setAccessoryUEventTime(long j) {
            this.mAccessoryConnectionStartTime = j;
        }

        public void setStartAccessoryTrue() {
            this.mStartAccessory = true;
        }

        public void resetUsbAccessoryHandshakeDebuggingInfo() {
            this.mAccessoryConnectionStartTime = 0L;
            this.mSendStringCount = 0;
            this.mStartAccessory = false;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static final class UsbHandlerLegacy extends UsbHandler {
        private static final String USB_CONFIG_PROPERTY = "sys.usb.config";
        private static final String USB_STATE_PROPERTY = "sys.usb.state";
        private String mCurrentFunctionsStr;
        private String mCurrentOemFunctions;
        private int mCurrentRequest;
        private HashMap<String, HashMap<String, Pair<String, String>>> mOemModeMap;
        private boolean mUsbDataUnlocked;

        @Override // com.android.server.usb.UsbDeviceManager.UsbHandler
        public void getUsbSpeedCb(int i) {
        }

        @Override // com.android.server.usb.UsbDeviceManager.UsbHandler
        public void handlerInitDone(int i) {
        }

        @Override // com.android.server.usb.UsbDeviceManager.UsbHandler
        public void resetCb(int i) {
        }

        @Override // com.android.server.usb.UsbDeviceManager.UsbHandler
        public void setCurrentUsbFunctionsCb(long j, int i, int i2, long j2, boolean z) {
        }

        UsbHandlerLegacy(Looper looper, Context context, UsbDeviceManager usbDeviceManager, UsbAlsaManager usbAlsaManager, UsbPermissionManager usbPermissionManager) {
            super(looper, context, usbDeviceManager, usbAlsaManager, usbPermissionManager);
            this.mCurrentRequest = 0;
            try {
                readOemUsbOverrideConfig(context);
                this.mCurrentOemFunctions = getSystemProperty(getPersistProp(false), "none");
                if (isNormalBoot()) {
                    String systemProperty = getSystemProperty(USB_CONFIG_PROPERTY, "none");
                    this.mCurrentFunctionsStr = systemProperty;
                    this.mCurrentFunctionsApplied = systemProperty.equals(getSystemProperty(USB_STATE_PROPERTY, "none"));
                } else {
                    this.mCurrentFunctionsStr = getSystemProperty(getPersistProp(true), "none");
                    if (!UsbDeviceManager.getOplusUsbDeviceFeature().usbFunctionsShouldForceStart()) {
                        this.mCurrentFunctionsApplied = false;
                    } else {
                        this.mCurrentFunctionsApplied = getSystemProperty(USB_CONFIG_PROPERTY, "none").equals(getSystemProperty(USB_STATE_PROPERTY, "none"));
                    }
                }
                this.mCurrentFunctions = 0L;
                this.mCurrentUsbFunctionsReceived = true;
                this.mUsbSpeed = -1;
                this.mCurrentGadgetHalVersion = -1;
                updateState(FileUtils.readTextFile(new File(UsbDeviceManager.STATE_PATH), 0, null).trim());
            } catch (Exception e) {
                Slog.e(UsbDeviceManager.TAG, "Error initializing UsbHandler", e);
            }
        }

        private void readOemUsbOverrideConfig(Context context) {
            String[] stringArray = context.getResources().getStringArray(R.array.preloaded_color_state_lists);
            if (stringArray != null) {
                for (String str : stringArray) {
                    String[] split = str.split(":");
                    if (split.length == 3 || split.length == 4) {
                        if (this.mOemModeMap == null) {
                            this.mOemModeMap = new HashMap<>();
                        }
                        HashMap<String, Pair<String, String>> hashMap = this.mOemModeMap.get(split[0]);
                        if (hashMap == null) {
                            hashMap = new HashMap<>();
                            this.mOemModeMap.put(split[0], hashMap);
                        }
                        if (!hashMap.containsKey(split[1])) {
                            if (split.length == 3) {
                                hashMap.put(split[1], new Pair<>(split[2], ""));
                            } else {
                                hashMap.put(split[1], new Pair<>(split[2], split[3]));
                            }
                        }
                    }
                }
            }
        }

        private String applyOemOverrideFunction(String str) {
            String str2;
            if (str != null && this.mOemModeMap != null) {
                String systemProperty = getSystemProperty(UsbDeviceManager.BOOT_MODE_PROPERTY, "unknown");
                Slog.d(UsbDeviceManager.TAG, "applyOemOverride usbfunctions=" + str + " bootmode=" + systemProperty);
                HashMap<String, Pair<String, String>> hashMap = this.mOemModeMap.get(systemProperty);
                if (hashMap != null && !systemProperty.equals(UsbDeviceManager.NORMAL_BOOT) && !systemProperty.equals("unknown")) {
                    Pair<String, String> pair = hashMap.get(str);
                    if (pair != null) {
                        Slog.d(UsbDeviceManager.TAG, "OEM USB override: " + str + " ==> " + ((String) pair.first) + " persist across reboot " + ((String) pair.second));
                        if (!((String) pair.second).equals("")) {
                            if (isAdbEnabled()) {
                                str2 = addFunction((String) pair.second, AppIntegrityManagerServiceImpl.ADB_INSTALLER);
                            } else {
                                str2 = (String) pair.second;
                            }
                            Slog.d(UsbDeviceManager.TAG, "OEM USB override persisting: " + str2 + "in prop: " + getPersistProp(false));
                            setSystemProperty(getPersistProp(false), str2);
                        }
                        return (String) pair.first;
                    }
                    if (isAdbEnabled()) {
                        setSystemProperty(getPersistProp(false), addFunction("none", AppIntegrityManagerServiceImpl.ADB_INSTALLER));
                    } else {
                        setSystemProperty(getPersistProp(false), "none");
                    }
                }
            }
            return str;
        }

        private boolean waitForState(String str) {
            String str2 = null;
            for (int i = 0; i < 20; i++) {
                str2 = getSystemProperty(USB_STATE_PROPERTY, "");
                if (str.equals(str2)) {
                    return true;
                }
                SystemClock.sleep(120L);
            }
            Slog.e(UsbDeviceManager.TAG, "waitForState(" + str + ") FAILED: got " + str2);
            return false;
        }

        private void setUsbConfig(String str) {
            Slog.d(UsbDeviceManager.TAG, "setUsbConfig(" + str + ")");
            setSystemProperty(USB_CONFIG_PROPERTY, str);
        }

        @Override // com.android.server.usb.UsbDeviceManager.UsbHandler
        protected void setEnabledFunctions(long j, boolean z, int i) {
            boolean isUsbDataTransferActive = isUsbDataTransferActive(j);
            Slog.d(UsbDeviceManager.TAG, "setEnabledFunctions functions=" + j + " ,forceRestart=" + z + " ,usbDataUnlocked=" + isUsbDataTransferActive + " ,mUsbDataUnlocked=" + this.mUsbDataUnlocked + " ,operationId=" + i);
            if (isUsbDataTransferActive != this.mUsbDataUnlocked) {
                this.mUsbDataUnlocked = isUsbDataTransferActive;
                updateUsbNotification(false);
                z = true;
            }
            long j2 = this.mCurrentFunctions;
            boolean z2 = this.mCurrentFunctionsApplied;
            if (trySetEnabledFunctions(j, z)) {
                return;
            }
            if (z2 && j2 != j) {
                Slog.e(UsbDeviceManager.TAG, "Failsafe 1: Restoring previous USB functions.");
                if (trySetEnabledFunctions(j2, false)) {
                    return;
                }
            }
            Slog.e(UsbDeviceManager.TAG, "Failsafe 2: Restoring default USB functions.");
            if (trySetEnabledFunctions(0L, false)) {
                return;
            }
            Slog.e(UsbDeviceManager.TAG, "Failsafe 3: Restoring empty function list (with ADB if enabled).");
            if (trySetEnabledFunctions(0L, false)) {
                return;
            }
            Slog.e(UsbDeviceManager.TAG, "Unable to set any USB functions!");
        }

        private boolean isNormalBoot() {
            getSystemProperty(UsbDeviceManager.BOOT_MODE_PROPERTY, "unknown");
            return UsbDeviceManager.getOplusUsbDeviceFeature().isNormalBoot();
        }

        protected String applyAdbFunction(String str) {
            if (str == null) {
                str = "";
            }
            if (isAdbEnabled()) {
                return addFunction(str, AppIntegrityManagerServiceImpl.ADB_INSTALLER);
            }
            return removeFunction(str, AppIntegrityManagerServiceImpl.ADB_INSTALLER);
        }

        /* JADX WARN: Removed duplicated region for block: B:58:0x0054  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private boolean trySetEnabledFunctions(long j, boolean z) {
            long usbTetheringSwitchOffFunctions = UsbDeviceManager.getOplusUsbDeviceFeature().usbTetheringSwitchOffFunctions(j, this.mCurrentFunctionsStr);
            String usbFunctionsToString = usbTetheringSwitchOffFunctions != 0 ? UsbManager.usbFunctionsToString(usbTetheringSwitchOffFunctions) : null;
            this.mCurrentFunctions = usbTetheringSwitchOffFunctions;
            if (usbFunctionsToString == null || applyAdbFunction(usbFunctionsToString).equals("none")) {
                String systemProperty = getSystemProperty(getPersistProp(true), "none");
                if (usbFunctionsToString == null) {
                    if (containsFunction(systemProperty, "mtp")) {
                        systemProperty = removeFunction(systemProperty, "mtp");
                    }
                    if (containsFunction(systemProperty, "ptp")) {
                        usbFunctionsToString = removeFunction(systemProperty, "ptp");
                        if (usbFunctionsToString.equals("none")) {
                            usbFunctionsToString = UsbManager.usbFunctionsToString(getChargingFunctions());
                        }
                    }
                }
                usbFunctionsToString = systemProperty;
                if (usbFunctionsToString.equals("none")) {
                }
            }
            String applyAdbFunction = applyAdbFunction(usbFunctionsToString);
            if (UsbDeviceManager.getOplusUsbDeviceFeature().isTelecomRequirement(applyAdbFunction)) {
                applyAdbFunction = "rndis,serial_cdev,diag,adb";
                z = true;
            }
            String applyOemOverrideFunction = applyOemOverrideFunction(applyAdbFunction);
            if (!isNormalBoot() && !this.mCurrentFunctionsStr.equals(applyAdbFunction)) {
                setSystemProperty(getPersistProp(true), applyAdbFunction);
            } else if (isNormalBoot() && applyAdbFunction.equals("midi")) {
                z = true;
            }
            UsbDeviceManager.getOplusUsbDeviceFeature().printFunctionsForDebug(new OplusUsbDeviceFunctionInfo(applyAdbFunction, applyOemOverrideFunction, this.mCurrentFunctionsStr, this.mCurrentFunctionsApplied, z, this.mCurrentOemFunctions));
            if ((!applyAdbFunction.equals(applyOemOverrideFunction) && !this.mCurrentOemFunctions.equals(applyOemOverrideFunction)) || !this.mCurrentFunctionsStr.equals(applyAdbFunction) || !this.mCurrentFunctionsApplied || z) {
                this.mCurrentFunctionsStr = applyAdbFunction;
                this.mCurrentOemFunctions = applyOemOverrideFunction;
                this.mCurrentFunctionsApplied = false;
                setUsbConfig("none");
                if (!waitForState("none")) {
                    Slog.e(UsbDeviceManager.TAG, "Failed to kick USB config");
                    return false;
                }
                setUsbConfig(applyOemOverrideFunction);
                if (this.mBootCompleted && (containsFunction(applyAdbFunction, "mtp") || containsFunction(applyAdbFunction, "ptp"))) {
                    updateUsbStateBroadcastIfNeeded(getAppliedFunctions(this.mCurrentFunctions));
                    UsbDeviceManager.getOplusUsbDeviceFeature().usbConfigRecord(applyAdbFunction);
                }
                if (!waitForState(applyOemOverrideFunction)) {
                    Slog.e(UsbDeviceManager.TAG, "Failed to switch USB config to " + applyAdbFunction);
                    return false;
                }
                this.mCurrentFunctionsApplied = true;
            }
            return true;
        }

        private String getPersistProp(boolean z) {
            String systemProperty = getSystemProperty(UsbDeviceManager.BOOT_MODE_PROPERTY, "unknown");
            if ((systemProperty.equals("reboot") && SystemProperties.getBoolean("persist.sys.allcommode", false)) || systemProperty.equals(UsbDeviceManager.NORMAL_BOOT) || systemProperty.equals("unknown")) {
                return "persist.sys.usb.config";
            }
            if (z) {
                return "persist.sys.usb." + systemProperty + ".func";
            }
            return "persist.sys.usb." + systemProperty + ".config";
        }

        private static String addFunction(String str, String str2) {
            if ("none".equals(str)) {
                return str2;
            }
            if (containsFunction(str, str2)) {
                return str;
            }
            if (str.length() > 0) {
                str = str + ",";
            }
            return str + str2;
        }

        private static String removeFunction(String str, String str2) {
            String[] split = str.split(",");
            for (int i = 0; i < split.length; i++) {
                if (str2.equals(split[i])) {
                    split[i] = null;
                }
            }
            if (split.length == 1 && split[0] == null) {
                return "none";
            }
            StringBuilder sb = new StringBuilder();
            for (String str3 : split) {
                if (str3 != null) {
                    if (sb.length() > 0) {
                        sb.append(",");
                    }
                    sb.append(str3);
                }
            }
            return sb.toString();
        }

        static boolean containsFunction(String str, String str2) {
            int indexOf = str.indexOf(str2);
            if (indexOf < 0) {
                return false;
            }
            if (indexOf > 0 && str.charAt(indexOf - 1) != ',') {
                return false;
            }
            int length = indexOf + str2.length();
            return length >= str.length() || str.charAt(length) == ',';
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static final class UsbHandlerHal extends UsbHandler {
        private static final int ENUMERATION_TIME_OUT_MS = 2000;
        protected static final String GADGET_HAL_FQ_NAME = "android.hardware.usb.gadget@1.0::IUsbGadget";
        private static final int SET_FUNCTIONS_LEEWAY_MS = 500;
        private static final int SET_FUNCTIONS_TIMEOUT_MS = 3000;
        private static final int USB_GADGET_HAL_DEATH_COOKIE = 2000;
        private int mCurrentRequest;
        protected boolean mCurrentUsbFunctionsRequested;
        private final Object mGadgetProxyLock;

        UsbHandlerHal(Looper looper, Context context, UsbDeviceManager usbDeviceManager, UsbAlsaManager usbAlsaManager, UsbPermissionManager usbPermissionManager) {
            super(looper, context, usbDeviceManager, usbAlsaManager, usbPermissionManager);
            Object obj = new Object();
            this.mGadgetProxyLock = obj;
            this.mCurrentRequest = 0;
            UsbDeviceManager.sUsbOperationCount.incrementAndGet();
            try {
                synchronized (obj) {
                    this.mCurrentFunctions = 0L;
                    this.mCurrentUsbFunctionsRequested = true;
                    this.mUsbSpeed = -1;
                    this.mCurrentGadgetHalVersion = 10;
                    updateUsbGadgetHalVersion();
                }
                updateState(FileUtils.readTextFile(new File(UsbDeviceManager.STATE_PATH), 0, null).trim());
            } catch (NoSuchElementException e) {
                Slog.e(UsbDeviceManager.TAG, "Usb gadget hal not found", e);
            } catch (Exception e2) {
                Slog.e(UsbDeviceManager.TAG, "Error initializing UsbHandler", e2);
            }
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        final class UsbGadgetDeathRecipient implements IHwBinder.DeathRecipient {
            UsbGadgetDeathRecipient() {
            }

            public void serviceDied(long j) {
                if (j == 2000) {
                    Slog.e(UsbDeviceManager.TAG, "Usb Gadget hal service died cookie: " + j);
                    synchronized (UsbHandlerHal.this.mGadgetProxyLock) {
                        UsbDeviceManager.mUsbGadgetHal = null;
                    }
                }
            }
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        final class ServiceNotification extends IServiceNotification.Stub {
            ServiceNotification() {
            }

            public void onRegistration(String str, String str2, boolean z) {
                Slog.i(UsbDeviceManager.TAG, "Usb gadget hal service started " + str + " " + str2);
                if (!str.equals(UsbHandlerHal.GADGET_HAL_FQ_NAME)) {
                    Slog.e(UsbDeviceManager.TAG, "fqName does not match");
                } else {
                    UsbHandlerHal.this.sendMessage(18, z);
                }
            }
        }

        @Override // com.android.server.usb.UsbDeviceManager.UsbHandler, android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 14:
                    setEnabledFunctions(0L, false, UsbDeviceManager.sUsbOperationCount.incrementAndGet());
                    return;
                case 15:
                    int incrementAndGet = UsbDeviceManager.sUsbOperationCount.incrementAndGet();
                    Slog.e(UsbDeviceManager.TAG, "Set functions timed out! no reply from usb hal ,operationId:" + incrementAndGet);
                    if (message.arg1 != 1) {
                        setEnabledFunctions(this.mScreenUnlockedFunctions, false, incrementAndGet);
                        return;
                    }
                    return;
                case 16:
                    Slog.i(UsbDeviceManager.TAG, "processing MSG_GET_CURRENT_USB_FUNCTIONS");
                    this.mCurrentUsbFunctionsReceived = true;
                    int i = message.arg2;
                    if (this.mCurrentUsbFunctionsRequested) {
                        Slog.i(UsbDeviceManager.TAG, "updating mCurrentFunctions");
                        this.mCurrentFunctions = ((Long) message.obj).longValue() & (-2);
                        Slog.i(UsbDeviceManager.TAG, "mCurrentFunctions:" + this.mCurrentFunctions + "applied:" + message.arg1);
                        this.mCurrentFunctionsApplied = message.arg1 == 1;
                    }
                    finishBoot(i);
                    return;
                case 17:
                    int incrementAndGet2 = UsbDeviceManager.sUsbOperationCount.incrementAndGet();
                    if (message.arg1 != 1) {
                        setEnabledFunctions(this.mScreenUnlockedFunctions, false, incrementAndGet2);
                        return;
                    }
                    return;
                case 18:
                    boolean z = message.arg1 == 1;
                    int incrementAndGet3 = UsbDeviceManager.sUsbOperationCount.incrementAndGet();
                    synchronized (this.mGadgetProxyLock) {
                        try {
                            UsbDeviceManager.mUsbGadgetHal = UsbGadgetHalInstance.getInstance(this.mUsbDeviceManager, null);
                            if (!this.mCurrentFunctionsApplied && !z) {
                                setEnabledFunctions(this.mCurrentFunctions, false, incrementAndGet3);
                            }
                        } catch (NoSuchElementException e) {
                            Slog.e(UsbDeviceManager.TAG, "Usb gadget hal not found", e);
                        }
                    }
                    return;
                case 19:
                    int incrementAndGet4 = UsbDeviceManager.sUsbOperationCount.incrementAndGet();
                    synchronized (this.mGadgetProxyLock) {
                        if (UsbDeviceManager.mUsbGadgetHal == null) {
                            Slog.e(UsbDeviceManager.TAG, "reset Usb Gadget mUsbGadgetHal is null");
                            return;
                        }
                        try {
                            removeMessages(8);
                            if (this.mConfigured) {
                                this.mResetUsbGadgetDisableDebounce = true;
                            }
                            UsbDeviceManager.mUsbGadgetHal.reset(incrementAndGet4);
                        } catch (Exception e2) {
                            Slog.e(UsbDeviceManager.TAG, "reset Usb Gadget failed", e2);
                            this.mResetUsbGadgetDisableDebounce = false;
                        }
                        return;
                    }
                case 20:
                case UsbDeviceManager.MSG_INCREASE_SENDSTRING_COUNT /* 21 */:
                default:
                    super.handleMessage(message);
                    return;
                case UsbDeviceManager.MSG_UPDATE_USB_SPEED /* 22 */:
                    int incrementAndGet5 = UsbDeviceManager.sUsbOperationCount.incrementAndGet();
                    if (UsbDeviceManager.mUsbGadgetHal == null) {
                        Slog.e(UsbDeviceManager.TAG, "mGadgetHal is null, operationId:" + incrementAndGet5);
                        return;
                    }
                    try {
                        UsbDeviceManager.mUsbGadgetHal.getUsbSpeed(incrementAndGet5);
                        return;
                    } catch (Exception e3) {
                        Slog.e(UsbDeviceManager.TAG, "get UsbSpeed failed", e3);
                        return;
                    }
                case 23:
                    if (UsbDeviceManager.mUsbGadgetHal == null) {
                        Slog.e(UsbDeviceManager.TAG, "mUsbGadgetHal is null");
                        return;
                    }
                    try {
                        this.mCurrentGadgetHalVersion = UsbDeviceManager.mUsbGadgetHal.getGadgetHalVersion();
                        return;
                    } catch (RemoteException e4) {
                        Slog.e(UsbDeviceManager.TAG, "update Usb gadget version failed", e4);
                        return;
                    }
            }
        }

        @Override // com.android.server.usb.UsbDeviceManager.UsbHandler
        public void setCurrentUsbFunctionsCb(long j, int i, int i2, long j2, boolean z) {
            if (this.mCurrentRequest == i2 && hasMessages(15) && j2 == j) {
                removeMessages(15);
                Slog.i(UsbDeviceManager.TAG, "notifyCurrentFunction request:" + i2 + " status:" + i);
                if (i == 0) {
                    this.mCurrentFunctionsApplied = true;
                } else {
                    if (z) {
                        return;
                    }
                    Slog.e(UsbDeviceManager.TAG, "Setting default fuctions");
                    sendEmptyMessage(14);
                }
            }
        }

        @Override // com.android.server.usb.UsbDeviceManager.UsbHandler
        public void getUsbSpeedCb(int i) {
            this.mUsbSpeed = i;
        }

        @Override // com.android.server.usb.UsbDeviceManager.UsbHandler
        public void resetCb(int i) {
            if (i != 0) {
                Slog.e(UsbDeviceManager.TAG, "resetCb fail");
            }
        }

        private void setUsbConfig(long j, boolean z, int i) {
            String str = UsbDeviceManager.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("setUsbConfig(");
            sb.append(j);
            sb.append(") request:");
            int i2 = this.mCurrentRequest + 1;
            this.mCurrentRequest = i2;
            sb.append(i2);
            Slog.d(str, sb.toString());
            removeMessages(17);
            removeMessages(15);
            removeMessages(14);
            synchronized (this.mGadgetProxyLock) {
                if (UsbDeviceManager.mUsbGadgetHal == null) {
                    Slog.e(UsbDeviceManager.TAG, "setUsbConfig mUsbGadgetHal is null");
                    return;
                }
                try {
                    if ((1 & j) != 0) {
                        ((AdbManagerInternal) LocalServices.getService(AdbManagerInternal.class)).startAdbdForTransport((byte) 0);
                    } else {
                        ((AdbManagerInternal) LocalServices.getService(AdbManagerInternal.class)).stopAdbdForTransport((byte) 0);
                    }
                    UsbDeviceManager.mUsbGadgetHal.setCurrentUsbFunctions(this.mCurrentRequest, j, z, IInputExt.DEFAULT_LONG_PRESS_POWERON_DISPLAY_TIME, i);
                    sendMessageDelayed(15, z, 3000L);
                    if (this.mConnected) {
                        sendMessageDelayed(17, z, 5000L);
                    }
                    Slog.d(UsbDeviceManager.TAG, "timeout message queued");
                } catch (Exception e) {
                    Slog.e(UsbDeviceManager.TAG, "Remoteexception while calling setCurrentUsbFunctions", e);
                }
            }
        }

        @Override // com.android.server.usb.UsbDeviceManager.UsbHandler
        protected void setEnabledFunctions(long j, boolean z, int i) {
            Slog.d(UsbDeviceManager.TAG, "setEnabledFunctionsi functions=" + j + ", forceRestart=" + z + ", operationId=" + i);
            if (this.mCurrentGadgetHalVersion < 12 && (1024 & j) != 0) {
                Slog.e(UsbDeviceManager.TAG, "Could not set unsupported function for the GadgetHal");
                return;
            }
            if (this.mCurrentFunctions == j && this.mCurrentFunctionsApplied && !z) {
                return;
            }
            Slog.i(UsbDeviceManager.TAG, "Setting USB config to " + UsbManager.usbFunctionsToString(j));
            this.mCurrentFunctions = j;
            this.mCurrentFunctionsApplied = false;
            this.mCurrentUsbFunctionsRequested = false;
            boolean z2 = j == 0;
            long appliedFunctions = getAppliedFunctions(j);
            setUsbConfig(0L, z2, i);
            setUsbConfig(appliedFunctions, z2, i);
            if (this.mBootCompleted && isUsbDataTransferActive(appliedFunctions)) {
                updateUsbStateBroadcastIfNeeded(appliedFunctions);
            }
        }

        @Override // com.android.server.usb.UsbDeviceManager.UsbHandler
        public void handlerInitDone(int i) {
            UsbDeviceManager.mUsbGadgetHal.getCurrentUsbFunctions(i);
        }
    }

    public UsbAccessory getCurrentAccessory() {
        return this.mHandler.getCurrentAccessory();
    }

    public ParcelFileDescriptor openAccessory(UsbAccessory usbAccessory, UsbUserPermissionManager usbUserPermissionManager, int i, int i2) {
        UsbAccessory currentAccessory = this.mHandler.getCurrentAccessory();
        if (currentAccessory == null) {
            throw new IllegalArgumentException("no accessory attached");
        }
        if (!currentAccessory.equals(usbAccessory)) {
            throw new IllegalArgumentException(usbAccessory.toString() + " does not match current accessory " + currentAccessory);
        }
        usbUserPermissionManager.checkPermission(usbAccessory, i, i2);
        return nativeOpenAccessory();
    }

    public long getCurrentFunctions() {
        return this.mHandler.getEnabledFunctions();
    }

    public int getCurrentUsbSpeed() {
        return this.mHandler.getUsbSpeed();
    }

    public int getGadgetHalVersion() {
        return this.mHandler.getGadgetHalVersion();
    }

    public void setCurrentUsbFunctionsCb(long j, int i, int i2, long j2, boolean z) {
        this.mHandler.setCurrentUsbFunctionsCb(j, i, i2, j2, z);
    }

    public void getCurrentUsbFunctionsCb(long j, int i) {
        this.mHandler.sendMessage(16, Long.valueOf(j), i == 2);
    }

    public void getUsbSpeedCb(int i) {
        this.mHandler.getUsbSpeedCb(i);
    }

    public void resetCb(int i) {
        this.mHandler.resetCb(i);
    }

    public ParcelFileDescriptor getControlFd(long j) {
        FileDescriptor fileDescriptor = this.mControlFds.get(Long.valueOf(j));
        if (fileDescriptor == null) {
            return null;
        }
        try {
            return ParcelFileDescriptor.dup(fileDescriptor);
        } catch (IOException unused) {
            Slog.e(TAG, "Could not dup fd for " + j);
            return null;
        }
    }

    public long getScreenUnlockedFunctions() {
        return this.mHandler.getScreenUnlockedFunctions();
    }

    public void setCurrentFunctions(long j, int i) {
        Slog.d(TAG, "setCurrentFunctions(" + UsbManager.usbFunctionsToString(j) + ")");
        if (j == 0) {
            MetricsLogger.action(this.mContext, 1275);
        } else if (j == 4) {
            MetricsLogger.action(this.mContext, 1276);
        } else if (j == 16) {
            MetricsLogger.action(this.mContext, 1277);
        } else if (j == 8) {
            MetricsLogger.action(this.mContext, 1279);
        } else if (j == 32) {
            if (((IOplusUsbDeviceFeature) OplusFeatureCache.getOrCreate(IOplusUsbDeviceFeature.DEFAULT, new Object[0])).isUsbTetheringDisabled(this.mContext)) {
                return;
            } else {
                MetricsLogger.action(this.mContext, 1278);
            }
        } else if (j == 2) {
            MetricsLogger.action(this.mContext, UsbTerminalTypes.TERMINAL_TELE_UNDEFINED);
        }
        this.mHandler.sendMessage(2, Long.valueOf(j), i);
    }

    public void setScreenUnlockedFunctions(long j) {
        Slog.d(TAG, "setScreenUnlockedFunctions(" + UsbManager.usbFunctionsToString(j) + ")");
        this.mHandler.sendMessage(12, Long.valueOf(j));
    }

    public void resetUsbGadget() {
        Slog.d(TAG, "reset Usb Gadget");
        this.mHandler.sendMessage(19, (Object) null);
    }

    private void onAdbEnabled(boolean z) {
        this.mHandler.sendMessage(1, z, sUsbOperationCount.incrementAndGet());
    }

    public void dump(DualDumpOutputStream dualDumpOutputStream, String str, long j) {
        long start = dualDumpOutputStream.start(str, j);
        UsbHandler usbHandler = this.mHandler;
        if (usbHandler != null) {
            usbHandler.dump(dualDumpOutputStream, "handler", 1146756268033L);
            sEventLogger.dump(new DualOutputStreamDumpSink(dualDumpOutputStream, 1138166333457L));
        }
        dualDumpOutputStream.end(start);
    }

    public static IOplusUsbDeviceFeature getOplusUsbDeviceFeature() {
        if (mIOplusUsbDeviceFeature == null) {
            mIOplusUsbDeviceFeature = (IOplusUsbDeviceFeature) OplusFeatureCache.getOrCreate(IOplusUsbDeviceFeature.DEFAULT, new Object[0]);
        }
        return mIOplusUsbDeviceFeature;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class UsbDeviceManagerWrapper {
        private final IOplusEngineerServiceExt mEngineerServiceExt;

        private UsbDeviceManagerWrapper() {
            this.mEngineerServiceExt = (IOplusEngineerServiceExt) ExtLoader.type(IOplusEngineerServiceExt.class).create();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public IOplusEngineerServiceExt getExtImpl() {
            return this.mEngineerServiceExt;
        }
    }
}
