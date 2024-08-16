package com.android.server.adb;

import android.R;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.debug.AdbNotifications;
import android.debug.PairDevice;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.os.InputConstants;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.SystemService;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.util.AtomicFile;
import android.util.Base64;
import android.util.Slog;
import android.util.Xml;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.XmlUtils;
import com.android.internal.util.dump.DualDumpOutputStream;
import com.android.internal.util.dump.DumpUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.FgThread;
import com.android.server.autofill.HintsHelper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AdbDebuggingManager {
    private static final String ADBD_SOCKET = "adbd";
    private static final String ADB_DIRECTORY = "misc/adb";
    private static final String ADB_KEYS_FILE = "adb_keys";
    private static final String ADB_TEMP_KEYS_FILE = "adb_temp_keys.xml";
    private static final int BUFFER_SIZE = 65536;
    private static final boolean DEBUG = false;
    private static final boolean MDNS_DEBUG = false;
    private static final int PAIRING_CODE_LENGTH = 6;
    private static final String TAG = "AdbDebuggingManager";
    private static final String WIFI_PERSISTENT_CONFIG_PROPERTY = "persist.adb.tls_server.enable";
    private static final String WIFI_PERSISTENT_GUID = "persist.adb.wifi.guid";
    private AdbConnectionInfo mAdbConnectionInfo;
    private boolean mAdbUsbEnabled;
    private boolean mAdbWifiEnabled;
    private final String mConfirmComponent;
    private final Map<String, Integer> mConnectedKeys;
    private AdbConnectionPortPoller mConnectionPortPoller;
    private final ContentResolver mContentResolver;
    private final Context mContext;
    private String mFingerprints;

    @VisibleForTesting
    final AdbDebuggingHandler mHandler;
    private PairingThread mPairingThread;
    private final PortListenerImpl mPortListener;
    private final File mTempKeysFile;
    private AdbDebuggingThread mThread;
    private final Ticker mTicker;
    private final File mUserKeyFile;
    private final Set<String> mWifiConnectedKeys;
    private static final Ticker SYSTEM_TICKER = new Ticker() { // from class: com.android.server.adb.AdbDebuggingManager$$ExternalSyntheticLambda0
        @Override // com.android.server.adb.AdbDebuggingManager.Ticker
        public final long currentTimeMillis() {
            long lambda$static$0;
            lambda$static$0 = AdbDebuggingManager.lambda$static$0();
            return lambda$static$0;
        }
    };
    private static final long ADBD_STATE_CHANGE_TIMEOUT = InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface AdbConnectionPortListener {
        void onPortReceived(int i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Ticker {
        long currentTimeMillis();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ long lambda$static$0() {
        return System.currentTimeMillis();
    }

    public AdbDebuggingManager(Context context) {
        this(context, null, getAdbFile(ADB_KEYS_FILE), getAdbFile(ADB_TEMP_KEYS_FILE), null, SYSTEM_TICKER);
    }

    @VisibleForTesting
    AdbDebuggingManager(Context context, String str, File file, File file2, AdbDebuggingThread adbDebuggingThread, Ticker ticker) {
        this.mAdbUsbEnabled = false;
        this.mAdbWifiEnabled = false;
        this.mConnectedKeys = new HashMap();
        this.mPairingThread = null;
        this.mWifiConnectedKeys = new HashSet();
        this.mAdbConnectionInfo = new AdbConnectionInfo();
        this.mPortListener = new PortListenerImpl();
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        this.mConfirmComponent = str;
        this.mUserKeyFile = file;
        this.mTempKeysFile = file2;
        this.mThread = adbDebuggingThread;
        this.mTicker = ticker;
        this.mHandler = new AdbDebuggingHandler(FgThread.get().getLooper(), this.mThread);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void sendBroadcastWithDebugPermission(Context context, Intent intent, UserHandle userHandle) {
        context.sendBroadcastAsUser(intent, userHandle, "android.permission.MANAGE_DEBUGGING");
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    class PairingThread extends Thread implements NsdManager.RegistrationListener {

        @VisibleForTesting
        static final String SERVICE_PROTOCOL = "adb-tls-pairing";
        private String mGuid;
        private NsdManager mNsdManager;
        private String mPairingCode;
        private int mPort;
        private String mPublicKey;
        private String mServiceName;
        private final String mServiceType;

        private native void native_pairing_cancel();

        private native int native_pairing_start(String str, String str2);

        private native boolean native_pairing_wait();

        @Override // android.net.nsd.NsdManager.RegistrationListener
        public void onServiceRegistered(NsdServiceInfo nsdServiceInfo) {
        }

        @Override // android.net.nsd.NsdManager.RegistrationListener
        public void onServiceUnregistered(NsdServiceInfo nsdServiceInfo) {
        }

        PairingThread(String str, String str2) {
            super(AdbDebuggingManager.TAG);
            this.mServiceType = String.format("_%s._tcp.", SERVICE_PROTOCOL);
            this.mPairingCode = str;
            this.mGuid = SystemProperties.get(AdbDebuggingManager.WIFI_PERSISTENT_GUID);
            this.mServiceName = str2;
            if (str2 == null || str2.isEmpty()) {
                this.mServiceName = this.mGuid;
            }
            this.mPort = -1;
            this.mNsdManager = (NsdManager) AdbDebuggingManager.this.mContext.getSystemService("servicediscovery");
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            if (this.mGuid.isEmpty()) {
                Slog.e(AdbDebuggingManager.TAG, "adbwifi guid was not set");
                return;
            }
            int native_pairing_start = native_pairing_start(this.mGuid, this.mPairingCode);
            this.mPort = native_pairing_start;
            if (native_pairing_start <= 0 || native_pairing_start > 65535) {
                Slog.e(AdbDebuggingManager.TAG, "Unable to start pairing server");
                return;
            }
            NsdServiceInfo nsdServiceInfo = new NsdServiceInfo();
            nsdServiceInfo.setServiceName(this.mServiceName);
            nsdServiceInfo.setServiceType(this.mServiceType);
            nsdServiceInfo.setPort(this.mPort);
            this.mNsdManager.registerService(nsdServiceInfo, 1, this);
            Message obtainMessage = AdbDebuggingManager.this.mHandler.obtainMessage(21);
            obtainMessage.obj = Integer.valueOf(this.mPort);
            AdbDebuggingManager.this.mHandler.sendMessage(obtainMessage);
            boolean native_pairing_wait = native_pairing_wait();
            this.mNsdManager.unregisterService(this);
            Bundle bundle = new Bundle();
            bundle.putString("publicKey", native_pairing_wait ? this.mPublicKey : null);
            AdbDebuggingManager.this.mHandler.sendMessage(Message.obtain(AdbDebuggingManager.this.mHandler, 20, bundle));
        }

        public void cancelPairing() {
            native_pairing_cancel();
        }

        @Override // android.net.nsd.NsdManager.RegistrationListener
        public void onRegistrationFailed(NsdServiceInfo nsdServiceInfo, int i) {
            Slog.e(AdbDebuggingManager.TAG, "Failed to register pairing service(err=" + i + "): " + nsdServiceInfo);
            cancelPairing();
        }

        @Override // android.net.nsd.NsdManager.RegistrationListener
        public void onUnregistrationFailed(NsdServiceInfo nsdServiceInfo, int i) {
            Slog.w(AdbDebuggingManager.TAG, "Failed to unregister pairing service(err=" + i + "): " + nsdServiceInfo);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class AdbConnectionPortPoller extends Thread {
        private AdbConnectionPortListener mListener;
        private final String mAdbPortProp = "service.adb.tls.port";
        private final int mDurationSecs = 10;
        private AtomicBoolean mCanceled = new AtomicBoolean(false);

        /* JADX INFO: Access modifiers changed from: package-private */
        public AdbConnectionPortPoller(AdbConnectionPortListener adbConnectionPortListener) {
            this.mListener = adbConnectionPortListener;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            for (int i = 0; i < 10; i++) {
                if (this.mCanceled.get()) {
                    return;
                }
                int i2 = SystemProperties.getInt("service.adb.tls.port", Integer.MAX_VALUE);
                if (i2 == -1 || (i2 > 0 && i2 <= 65535)) {
                    this.mListener.onPortReceived(i2);
                    return;
                }
                SystemClock.sleep(1000L);
            }
            Slog.w(AdbDebuggingManager.TAG, "Failed to receive adb connection port");
            this.mListener.onPortReceived(-1);
        }

        public void cancelAndWait() {
            this.mCanceled.set(true);
            if (isAlive()) {
                try {
                    join();
                } catch (InterruptedException unused) {
                }
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    class PortListenerImpl implements AdbConnectionPortListener {
        PortListenerImpl() {
        }

        @Override // com.android.server.adb.AdbDebuggingManager.AdbConnectionPortListener
        public void onPortReceived(int i) {
            Message obtainMessage = AdbDebuggingManager.this.mHandler.obtainMessage(i > 0 ? 24 : 25);
            obtainMessage.obj = Integer.valueOf(i);
            AdbDebuggingManager.this.mHandler.sendMessage(obtainMessage);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class AdbDebuggingThread extends Thread {
        private Handler mHandler;
        private InputStream mInputStream;
        private OutputStream mOutputStream;
        private LocalSocket mSocket;
        private boolean mStopped;

        @VisibleForTesting
        AdbDebuggingThread() {
            super(AdbDebuggingManager.TAG);
        }

        @VisibleForTesting
        void setHandler(Handler handler) {
            this.mHandler = handler;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (true) {
                synchronized (this) {
                    if (this.mStopped) {
                        return;
                    }
                    try {
                        openSocketLocked();
                    } catch (Exception unused) {
                        SystemClock.sleep(1000L);
                    }
                }
                try {
                    listenToSocket();
                } catch (Exception unused2) {
                    SystemClock.sleep(1000L);
                }
            }
        }

        private void openSocketLocked() throws IOException {
            try {
                LocalSocketAddress localSocketAddress = new LocalSocketAddress(AdbDebuggingManager.ADBD_SOCKET, LocalSocketAddress.Namespace.RESERVED);
                this.mInputStream = null;
                LocalSocket localSocket = new LocalSocket(3);
                this.mSocket = localSocket;
                localSocket.connect(localSocketAddress);
                this.mOutputStream = this.mSocket.getOutputStream();
                this.mInputStream = this.mSocket.getInputStream();
                this.mHandler.sendEmptyMessage(26);
            } catch (IOException e) {
                Slog.e(AdbDebuggingManager.TAG, "Caught an exception opening the socket: " + e);
                closeSocketLocked();
                throw e;
            }
        }

        private void listenToSocket() throws IOException {
            try {
                byte[] bArr = new byte[65536];
                while (true) {
                    int read = this.mInputStream.read(bArr);
                    if (read < 2) {
                        Slog.w(AdbDebuggingManager.TAG, "Read failed with count " + read);
                        break;
                    }
                    byte b = bArr[0];
                    if (b == 80 && bArr[1] == 75) {
                        String str = new String(Arrays.copyOfRange(bArr, 2, read));
                        Slog.d(AdbDebuggingManager.TAG, "Received public key: " + str);
                        Message obtainMessage = this.mHandler.obtainMessage(5);
                        obtainMessage.obj = str;
                        this.mHandler.sendMessage(obtainMessage);
                    } else if (b == 68 && bArr[1] == 67) {
                        String str2 = new String(Arrays.copyOfRange(bArr, 2, read));
                        Slog.d(AdbDebuggingManager.TAG, "Received disconnected message: " + str2);
                        Message obtainMessage2 = this.mHandler.obtainMessage(7);
                        obtainMessage2.obj = str2;
                        this.mHandler.sendMessage(obtainMessage2);
                    } else if (b == 67 && bArr[1] == 75) {
                        String str3 = new String(Arrays.copyOfRange(bArr, 2, read));
                        Slog.d(AdbDebuggingManager.TAG, "Received connected key message: " + str3);
                        Message obtainMessage3 = this.mHandler.obtainMessage(10);
                        obtainMessage3.obj = str3;
                        this.mHandler.sendMessage(obtainMessage3);
                    } else if (b == 87 && bArr[1] == 69) {
                        byte b2 = bArr[2];
                        String str4 = new String(Arrays.copyOfRange(bArr, 3, read));
                        if (b2 == 0) {
                            Slog.d(AdbDebuggingManager.TAG, "Received USB TLS connected key message: " + str4);
                            Message obtainMessage4 = this.mHandler.obtainMessage(10);
                            obtainMessage4.obj = str4;
                            this.mHandler.sendMessage(obtainMessage4);
                        } else if (b2 == 1) {
                            Slog.d(AdbDebuggingManager.TAG, "Received WIFI TLS connected key message: " + str4);
                            Message obtainMessage5 = this.mHandler.obtainMessage(22);
                            obtainMessage5.obj = str4;
                            this.mHandler.sendMessage(obtainMessage5);
                        } else {
                            Slog.e(AdbDebuggingManager.TAG, "Got unknown transport type from adbd (" + ((int) b2) + ")");
                        }
                    } else {
                        if (b != 87 || bArr[1] != 70) {
                            break;
                        }
                        byte b3 = bArr[2];
                        String str5 = new String(Arrays.copyOfRange(bArr, 3, read));
                        if (b3 == 0) {
                            Slog.d(AdbDebuggingManager.TAG, "Received USB TLS disconnect message: " + str5);
                            Message obtainMessage6 = this.mHandler.obtainMessage(7);
                            obtainMessage6.obj = str5;
                            this.mHandler.sendMessage(obtainMessage6);
                        } else if (b3 == 1) {
                            Slog.d(AdbDebuggingManager.TAG, "Received WIFI TLS disconnect key message: " + str5);
                            Message obtainMessage7 = this.mHandler.obtainMessage(23);
                            obtainMessage7.obj = str5;
                            this.mHandler.sendMessage(obtainMessage7);
                        } else {
                            Slog.e(AdbDebuggingManager.TAG, "Got unknown transport type from adbd (" + ((int) b3) + ")");
                        }
                    }
                }
                Slog.e(AdbDebuggingManager.TAG, "Wrong message: " + new String(Arrays.copyOfRange(bArr, 0, 2)));
                synchronized (this) {
                    closeSocketLocked();
                }
            } catch (Throwable th) {
                synchronized (this) {
                    closeSocketLocked();
                    throw th;
                }
            }
        }

        private void closeSocketLocked() {
            try {
                OutputStream outputStream = this.mOutputStream;
                if (outputStream != null) {
                    outputStream.close();
                    this.mOutputStream = null;
                }
            } catch (IOException e) {
                Slog.e(AdbDebuggingManager.TAG, "Failed closing output stream: " + e);
            }
            try {
                LocalSocket localSocket = this.mSocket;
                if (localSocket != null) {
                    localSocket.close();
                    this.mSocket = null;
                }
            } catch (IOException e2) {
                Slog.e(AdbDebuggingManager.TAG, "Failed closing socket: " + e2);
            }
            this.mHandler.sendEmptyMessage(27);
        }

        void stopListening() {
            synchronized (this) {
                this.mStopped = true;
                closeSocketLocked();
            }
        }

        void sendResponse(String str) {
            OutputStream outputStream;
            synchronized (this) {
                if (!this.mStopped && (outputStream = this.mOutputStream) != null) {
                    try {
                        outputStream.write(str.getBytes());
                    } catch (IOException e) {
                        Slog.e(AdbDebuggingManager.TAG, "Failed to write response:", e);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class AdbConnectionInfo {
        private String mBssid;
        private int mPort;
        private String mSsid;

        AdbConnectionInfo() {
            this.mBssid = "";
            this.mSsid = "";
            this.mPort = -1;
        }

        AdbConnectionInfo(String str, String str2) {
            this.mBssid = str;
            this.mSsid = str2;
        }

        AdbConnectionInfo(AdbConnectionInfo adbConnectionInfo) {
            this.mBssid = adbConnectionInfo.mBssid;
            this.mSsid = adbConnectionInfo.mSsid;
            this.mPort = adbConnectionInfo.mPort;
        }

        public String getBSSID() {
            return this.mBssid;
        }

        public String getSSID() {
            return this.mSsid;
        }

        public int getPort() {
            return this.mPort;
        }

        public void setPort(int i) {
            this.mPort = i;
        }

        public void clear() {
            this.mBssid = "";
            this.mSsid = "";
            this.mPort = -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAdbConnectionInfo(AdbConnectionInfo adbConnectionInfo) {
        synchronized (this.mAdbConnectionInfo) {
            if (adbConnectionInfo == null) {
                this.mAdbConnectionInfo.clear();
            } else {
                this.mAdbConnectionInfo = adbConnectionInfo;
            }
        }
    }

    private AdbConnectionInfo getAdbConnectionInfo() {
        AdbConnectionInfo adbConnectionInfo;
        synchronized (this.mAdbConnectionInfo) {
            adbConnectionInfo = new AdbConnectionInfo(this.mAdbConnectionInfo);
        }
        return adbConnectionInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AdbDebuggingHandler extends Handler {
        private static final String ADB_NOTIFICATION_CHANNEL_ID_TV = "usbdevicemanager.adb.tv";
        static final int MESSAGE_ADB_ALLOW = 3;
        static final int MESSAGE_ADB_CLEAR = 6;
        static final int MESSAGE_ADB_CONFIRM = 5;
        static final int MESSAGE_ADB_CONNECTED_KEY = 10;
        static final int MESSAGE_ADB_DENY = 4;
        static final int MESSAGE_ADB_DISABLED = 2;
        static final int MESSAGE_ADB_DISCONNECT = 7;
        static final int MESSAGE_ADB_ENABLED = 1;
        static final int MESSAGE_ADB_PERSIST_KEYSTORE = 8;
        static final int MESSAGE_ADB_UPDATE_KEYSTORE = 9;
        private static final int MESSAGE_KEY_FILES_UPDATED = 28;
        static final int MSG_ADBDWIFI_DISABLE = 12;
        static final int MSG_ADBDWIFI_ENABLE = 11;
        static final int MSG_ADBD_SOCKET_CONNECTED = 26;
        static final int MSG_ADBD_SOCKET_DISCONNECTED = 27;
        static final int MSG_ADBWIFI_ALLOW = 18;
        static final int MSG_ADBWIFI_DENY = 19;
        static final String MSG_DISABLE_ADBDWIFI = "DA";
        static final String MSG_DISCONNECT_DEVICE = "DD";
        static final int MSG_PAIRING_CANCEL = 14;
        static final int MSG_PAIR_PAIRING_CODE = 15;
        static final int MSG_PAIR_QR_CODE = 16;
        static final int MSG_REQ_UNPAIR = 17;
        static final int MSG_RESPONSE_PAIRING_PORT = 21;
        static final int MSG_RESPONSE_PAIRING_RESULT = 20;
        static final int MSG_SERVER_CONNECTED = 24;
        static final int MSG_SERVER_DISCONNECTED = 25;
        static final int MSG_WIFI_DEVICE_CONNECTED = 22;
        static final int MSG_WIFI_DEVICE_DISCONNECTED = 23;
        static final long UPDATE_KEYSTORE_JOB_INTERVAL = 86400000;
        static final long UPDATE_KEYSTORE_MIN_JOB_INTERVAL = 60000;
        private int mAdbEnabledRefCount;

        @VisibleForTesting
        AdbKeyStore mAdbKeyStore;
        private boolean mAdbNotificationShown;
        private ContentObserver mAuthTimeObserver;
        private final BroadcastReceiver mBroadcastReceiver;
        private NotificationManager mNotificationManager;

        private boolean isTv() {
            return AdbDebuggingManager.this.mContext.getPackageManager().hasSystemFeature("android.software.leanback");
        }

        private void setupNotifications() {
            if (this.mNotificationManager != null) {
                return;
            }
            NotificationManager notificationManager = (NotificationManager) AdbDebuggingManager.this.mContext.getSystemService("notification");
            this.mNotificationManager = notificationManager;
            if (notificationManager == null) {
                Slog.e(AdbDebuggingManager.TAG, "Unable to setup notifications for wireless debugging");
            } else if (isTv()) {
                this.mNotificationManager.createNotificationChannel(new NotificationChannel(ADB_NOTIFICATION_CHANNEL_ID_TV, AdbDebuggingManager.this.mContext.getString(R.string.app_blocked_message), 4));
            }
        }

        @VisibleForTesting
        AdbDebuggingHandler(Looper looper, AdbDebuggingThread adbDebuggingThread) {
            super(looper);
            this.mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.adb.AdbDebuggingManager.AdbDebuggingHandler.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if ("android.net.wifi.WIFI_STATE_CHANGED".equals(action)) {
                        if (intent.getIntExtra("wifi_state", 1) == 1) {
                            Slog.i(AdbDebuggingManager.TAG, "Wifi disabled. Disabling adbwifi.");
                            Settings.Global.putInt(AdbDebuggingManager.this.mContentResolver, "adb_wifi_enabled", 0);
                            return;
                        }
                        return;
                    }
                    if ("android.net.wifi.STATE_CHANGE".equals(action)) {
                        NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo", NetworkInfo.class);
                        if (networkInfo.getType() == 1) {
                            if (!networkInfo.isConnected()) {
                                Slog.i(AdbDebuggingManager.TAG, "Network disconnected. Disabling adbwifi.");
                                Settings.Global.putInt(AdbDebuggingManager.this.mContentResolver, "adb_wifi_enabled", 0);
                                return;
                            }
                            WifiInfo connectionInfo = ((WifiManager) AdbDebuggingManager.this.mContext.getSystemService("wifi")).getConnectionInfo();
                            if (connectionInfo == null || connectionInfo.getNetworkId() == -1) {
                                Slog.i(AdbDebuggingManager.TAG, "Not connected to any wireless network. Not enabling adbwifi.");
                                Settings.Global.putInt(AdbDebuggingManager.this.mContentResolver, "adb_wifi_enabled", 0);
                                return;
                            }
                            String bssid = connectionInfo.getBSSID();
                            if (bssid == null || bssid.isEmpty()) {
                                Slog.e(AdbDebuggingManager.TAG, "Unable to get the wifi ap's BSSID. Disabling adbwifi.");
                                Settings.Global.putInt(AdbDebuggingManager.this.mContentResolver, "adb_wifi_enabled", 0);
                                return;
                            }
                            synchronized (AdbDebuggingManager.this.mAdbConnectionInfo) {
                                if (!bssid.equals(AdbDebuggingManager.this.mAdbConnectionInfo.getBSSID())) {
                                    Slog.i(AdbDebuggingManager.TAG, "Detected wifi network change. Disabling adbwifi.");
                                    Settings.Global.putInt(AdbDebuggingManager.this.mContentResolver, "adb_wifi_enabled", 0);
                                }
                            }
                        }
                    }
                }
            };
            this.mAdbEnabledRefCount = 0;
            this.mAuthTimeObserver = new ContentObserver(this) { // from class: com.android.server.adb.AdbDebuggingManager.AdbDebuggingHandler.2
                @Override // android.database.ContentObserver
                public void onChange(boolean z, Uri uri) {
                    Slog.d(AdbDebuggingManager.TAG, "Received notification that uri " + uri + " was modified; rescheduling keystore job");
                    AdbDebuggingHandler.this.scheduleJobToUpdateAdbKeyStore();
                }
            };
            AdbDebuggingManager.this.mThread = adbDebuggingThread;
        }

        @VisibleForTesting
        void initKeyStore() {
            if (this.mAdbKeyStore == null) {
                this.mAdbKeyStore = new AdbKeyStore();
            }
        }

        public void showAdbConnectedNotification(boolean z) {
            if (z == this.mAdbNotificationShown) {
                return;
            }
            setupNotifications();
            if (!this.mAdbNotificationShown) {
                Notification createNotification = AdbNotifications.createNotification(AdbDebuggingManager.this.mContext, (byte) 1);
                this.mAdbNotificationShown = true;
                this.mNotificationManager.notifyAsUser(null, 62, createNotification, UserHandle.ALL);
            } else {
                this.mAdbNotificationShown = false;
                this.mNotificationManager.cancelAsUser(null, 62, UserHandle.ALL);
            }
        }

        private void startAdbDebuggingThread() {
            int i = this.mAdbEnabledRefCount + 1;
            this.mAdbEnabledRefCount = i;
            if (i > 1) {
                return;
            }
            registerForAuthTimeChanges();
            AdbDebuggingManager.this.mThread = new AdbDebuggingThread();
            AdbDebuggingManager.this.mThread.setHandler(AdbDebuggingManager.this.mHandler);
            AdbDebuggingManager.this.mThread.start();
            this.mAdbKeyStore.updateKeyStore();
            scheduleJobToUpdateAdbKeyStore();
        }

        private void stopAdbDebuggingThread() {
            int i = this.mAdbEnabledRefCount - 1;
            this.mAdbEnabledRefCount = i;
            if (i > 0) {
                return;
            }
            if (AdbDebuggingManager.this.mThread != null) {
                AdbDebuggingManager.this.mThread.stopListening();
                AdbDebuggingManager.this.mThread = null;
            }
            if (!AdbDebuggingManager.this.mConnectedKeys.isEmpty()) {
                Iterator it = AdbDebuggingManager.this.mConnectedKeys.entrySet().iterator();
                while (it.hasNext()) {
                    this.mAdbKeyStore.setLastConnectionTime((String) ((Map.Entry) it.next()).getKey(), AdbDebuggingManager.this.mTicker.currentTimeMillis());
                }
                AdbDebuggingManager.this.sendPersistKeyStoreMessage();
                AdbDebuggingManager.this.mConnectedKeys.clear();
                AdbDebuggingManager.this.mWifiConnectedKeys.clear();
            }
            scheduleJobToUpdateAdbKeyStore();
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            initKeyStore();
            switch (message.what) {
                case 1:
                    if (AdbDebuggingManager.this.mAdbUsbEnabled) {
                        return;
                    }
                    startAdbDebuggingThread();
                    AdbDebuggingManager.this.mAdbUsbEnabled = true;
                    return;
                case 2:
                    if (AdbDebuggingManager.this.mAdbUsbEnabled) {
                        stopAdbDebuggingThread();
                        AdbDebuggingManager.this.mAdbUsbEnabled = false;
                        return;
                    }
                    return;
                case 3:
                    String str = (String) message.obj;
                    String fingerprints = AdbDebuggingManager.this.getFingerprints(str);
                    if (!fingerprints.equals(AdbDebuggingManager.this.mFingerprints)) {
                        Slog.e(AdbDebuggingManager.TAG, "Fingerprints do not match. Got " + fingerprints + ", expected " + AdbDebuggingManager.this.mFingerprints);
                        return;
                    }
                    r4 = message.arg1 == 1;
                    if (AdbDebuggingManager.this.mThread != null) {
                        AdbDebuggingManager.this.mThread.sendResponse("OK");
                        if (r4) {
                            if (!AdbDebuggingManager.this.mConnectedKeys.containsKey(str)) {
                                AdbDebuggingManager.this.mConnectedKeys.put(str, 1);
                            }
                            this.mAdbKeyStore.setLastConnectionTime(str, AdbDebuggingManager.this.mTicker.currentTimeMillis());
                            AdbDebuggingManager.this.sendPersistKeyStoreMessage();
                            scheduleJobToUpdateAdbKeyStore();
                        }
                        logAdbConnectionChanged(str, 2, r4);
                        return;
                    }
                    return;
                case 4:
                    if (AdbDebuggingManager.this.mThread != null) {
                        Slog.w(AdbDebuggingManager.TAG, "Denying adb confirmation");
                        AdbDebuggingManager.this.mThread.sendResponse("NO");
                        logAdbConnectionChanged(null, 3, false);
                        return;
                    }
                    return;
                case 5:
                    String str2 = (String) message.obj;
                    if (SystemProperties.getInt("persist.sys.adb.engineermode", 1) == 0) {
                        if (AdbDebuggingManager.this.mThread != null) {
                            AdbDebuggingManager.this.mThread.sendResponse("OK");
                            logAdbConnectionChanged(str2, 2, false);
                            return;
                        }
                        return;
                    }
                    String fingerprints2 = AdbDebuggingManager.this.getFingerprints(str2);
                    if ("".equals(fingerprints2)) {
                        if (AdbDebuggingManager.this.mThread != null) {
                            AdbDebuggingManager.this.mThread.sendResponse("NO");
                            logAdbConnectionChanged(str2, 5, false);
                            return;
                        }
                        return;
                    }
                    logAdbConnectionChanged(str2, 1, false);
                    AdbDebuggingManager.this.mFingerprints = fingerprints2;
                    AdbDebuggingManager adbDebuggingManager = AdbDebuggingManager.this;
                    adbDebuggingManager.startConfirmationForKey(str2, adbDebuggingManager.mFingerprints);
                    return;
                case 6:
                    Slog.d(AdbDebuggingManager.TAG, "Received a request to clear the adb authorizations");
                    AdbDebuggingManager.this.mConnectedKeys.clear();
                    initKeyStore();
                    AdbDebuggingManager.this.mWifiConnectedKeys.clear();
                    this.mAdbKeyStore.deleteKeyStore();
                    cancelJobToUpdateAdbKeyStore();
                    if (Settings.Global.getInt(AdbDebuggingManager.this.mContentResolver, "adb_disconnect_sessions_on_revoke", 1) == 1 && AdbDebuggingManager.this.mAdbUsbEnabled) {
                        try {
                            SystemService.stop(AdbDebuggingManager.ADBD_SOCKET);
                            SystemService.waitForState(AdbDebuggingManager.ADBD_SOCKET, SystemService.State.STOPPED, AdbDebuggingManager.ADBD_STATE_CHANGE_TIMEOUT);
                            SystemService.start(AdbDebuggingManager.ADBD_SOCKET);
                            SystemService.waitForState(AdbDebuggingManager.ADBD_SOCKET, SystemService.State.RUNNING, AdbDebuggingManager.ADBD_STATE_CHANGE_TIMEOUT);
                            return;
                        } catch (TimeoutException e) {
                            Slog.e(AdbDebuggingManager.TAG, "Timeout occurred waiting for adbd to cycle: ", e);
                            Settings.Global.putInt(AdbDebuggingManager.this.mContentResolver, "adb_enabled", 0);
                            return;
                        }
                    }
                    return;
                case 7:
                    String str3 = (String) message.obj;
                    if (str3 != null && str3.length() > 0) {
                        if (AdbDebuggingManager.this.mConnectedKeys.containsKey(str3)) {
                            int intValue = ((Integer) AdbDebuggingManager.this.mConnectedKeys.get(str3)).intValue() - 1;
                            if (intValue == 0) {
                                this.mAdbKeyStore.setLastConnectionTime(str3, AdbDebuggingManager.this.mTicker.currentTimeMillis());
                                AdbDebuggingManager.this.sendPersistKeyStoreMessage();
                                scheduleJobToUpdateAdbKeyStore();
                                AdbDebuggingManager.this.mConnectedKeys.remove(str3);
                            } else {
                                AdbDebuggingManager.this.mConnectedKeys.put(str3, Integer.valueOf(intValue));
                            }
                            r4 = true;
                        }
                    } else {
                        Slog.w(AdbDebuggingManager.TAG, "Received a disconnected key message with an empty key");
                    }
                    logAdbConnectionChanged(str3, 7, r4);
                    return;
                case 8:
                    AdbKeyStore adbKeyStore = this.mAdbKeyStore;
                    if (adbKeyStore != null) {
                        adbKeyStore.persistKeyStore();
                        return;
                    }
                    return;
                case 9:
                    if (!AdbDebuggingManager.this.mConnectedKeys.isEmpty()) {
                        Iterator it = AdbDebuggingManager.this.mConnectedKeys.entrySet().iterator();
                        while (it.hasNext()) {
                            this.mAdbKeyStore.setLastConnectionTime((String) ((Map.Entry) it.next()).getKey(), AdbDebuggingManager.this.mTicker.currentTimeMillis());
                        }
                        AdbDebuggingManager.this.sendPersistKeyStoreMessage();
                        scheduleJobToUpdateAdbKeyStore();
                        return;
                    }
                    if (this.mAdbKeyStore.isEmpty()) {
                        return;
                    }
                    this.mAdbKeyStore.updateKeyStore();
                    scheduleJobToUpdateAdbKeyStore();
                    return;
                case 10:
                    String str4 = (String) message.obj;
                    if (str4 == null || str4.length() == 0) {
                        Slog.w(AdbDebuggingManager.TAG, "Received a connected key message with an empty key");
                        return;
                    }
                    if (!AdbDebuggingManager.this.mConnectedKeys.containsKey(str4)) {
                        AdbDebuggingManager.this.mConnectedKeys.put(str4, 1);
                    } else {
                        AdbDebuggingManager.this.mConnectedKeys.put(str4, Integer.valueOf(((Integer) AdbDebuggingManager.this.mConnectedKeys.get(str4)).intValue() + 1));
                    }
                    this.mAdbKeyStore.setLastConnectionTime(str4, AdbDebuggingManager.this.mTicker.currentTimeMillis());
                    AdbDebuggingManager.this.sendPersistKeyStoreMessage();
                    scheduleJobToUpdateAdbKeyStore();
                    logAdbConnectionChanged(str4, 4, true);
                    return;
                case 11:
                    if (AdbDebuggingManager.this.mAdbWifiEnabled) {
                        return;
                    }
                    AdbConnectionInfo currentWifiApInfo = getCurrentWifiApInfo();
                    if (currentWifiApInfo == null) {
                        Settings.Global.putInt(AdbDebuggingManager.this.mContentResolver, "adb_wifi_enabled", 0);
                        return;
                    }
                    if (!verifyWifiNetwork(currentWifiApInfo.getBSSID(), currentWifiApInfo.getSSID())) {
                        Settings.Global.putInt(AdbDebuggingManager.this.mContentResolver, "adb_wifi_enabled", 0);
                        return;
                    }
                    AdbDebuggingManager.this.setAdbConnectionInfo(currentWifiApInfo);
                    IntentFilter intentFilter = new IntentFilter("android.net.wifi.WIFI_STATE_CHANGED");
                    intentFilter.addAction("android.net.wifi.STATE_CHANGE");
                    AdbDebuggingManager.this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter);
                    SystemProperties.set(AdbDebuggingManager.WIFI_PERSISTENT_CONFIG_PROPERTY, "1");
                    AdbDebuggingManager.this.mConnectionPortPoller = new AdbConnectionPortPoller(AdbDebuggingManager.this.mPortListener);
                    AdbDebuggingManager.this.mConnectionPortPoller.start();
                    startAdbDebuggingThread();
                    AdbDebuggingManager.this.mAdbWifiEnabled = true;
                    return;
                case 12:
                    if (AdbDebuggingManager.this.mAdbWifiEnabled) {
                        AdbDebuggingManager.this.mAdbWifiEnabled = false;
                        AdbDebuggingManager.this.setAdbConnectionInfo(null);
                        AdbDebuggingManager.this.mContext.unregisterReceiver(this.mBroadcastReceiver);
                        if (AdbDebuggingManager.this.mThread != null) {
                            AdbDebuggingManager.this.mThread.sendResponse(MSG_DISABLE_ADBDWIFI);
                        }
                        onAdbdWifiServerDisconnected(-1);
                        stopAdbDebuggingThread();
                        return;
                    }
                    return;
                case 13:
                default:
                    return;
                case 14:
                    if (AdbDebuggingManager.this.mPairingThread != null) {
                        AdbDebuggingManager.this.mPairingThread.cancelPairing();
                        try {
                            AdbDebuggingManager.this.mPairingThread.join();
                        } catch (InterruptedException e2) {
                            Slog.w(AdbDebuggingManager.TAG, "Error while waiting for pairing thread to quit.");
                            e2.printStackTrace();
                        }
                        AdbDebuggingManager.this.mPairingThread = null;
                        return;
                    }
                    return;
                case 15:
                    String createPairingCode = createPairingCode(6);
                    updateUIPairCode(createPairingCode);
                    AdbDebuggingManager.this.mPairingThread = new PairingThread(createPairingCode, null);
                    AdbDebuggingManager.this.mPairingThread.start();
                    return;
                case 16:
                    Bundle bundle = (Bundle) message.obj;
                    AdbDebuggingManager.this.mPairingThread = new PairingThread(bundle.getString(HintsHelper.AUTOFILL_HINT_PASSWORD), bundle.getString("serviceName"));
                    AdbDebuggingManager.this.mPairingThread.start();
                    return;
                case 17:
                    String str5 = (String) message.obj;
                    String findKeyFromFingerprint = this.mAdbKeyStore.findKeyFromFingerprint(str5);
                    if (findKeyFromFingerprint == null || findKeyFromFingerprint.isEmpty()) {
                        Slog.e(AdbDebuggingManager.TAG, "Not a known fingerprint [" + str5 + "]");
                        return;
                    }
                    String str6 = MSG_DISCONNECT_DEVICE + findKeyFromFingerprint;
                    if (AdbDebuggingManager.this.mThread != null) {
                        AdbDebuggingManager.this.mThread.sendResponse(str6);
                    }
                    this.mAdbKeyStore.removeKey(findKeyFromFingerprint);
                    sendPairedDevicesToUI(this.mAdbKeyStore.getPairedDevices());
                    return;
                case 18:
                    if (AdbDebuggingManager.this.mAdbWifiEnabled) {
                        return;
                    }
                    String str7 = (String) message.obj;
                    if (message.arg1 == 1) {
                        this.mAdbKeyStore.addTrustedNetwork(str7);
                    }
                    AdbConnectionInfo currentWifiApInfo2 = getCurrentWifiApInfo();
                    if (currentWifiApInfo2 == null || !str7.equals(currentWifiApInfo2.getBSSID())) {
                        return;
                    }
                    AdbDebuggingManager.this.setAdbConnectionInfo(currentWifiApInfo2);
                    Settings.Global.putInt(AdbDebuggingManager.this.mContentResolver, "adb_wifi_enabled", 1);
                    IntentFilter intentFilter2 = new IntentFilter("android.net.wifi.WIFI_STATE_CHANGED");
                    intentFilter2.addAction("android.net.wifi.STATE_CHANGE");
                    AdbDebuggingManager.this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter2);
                    SystemProperties.set(AdbDebuggingManager.WIFI_PERSISTENT_CONFIG_PROPERTY, "1");
                    AdbDebuggingManager.this.mConnectionPortPoller = new AdbConnectionPortPoller(AdbDebuggingManager.this.mPortListener);
                    AdbDebuggingManager.this.mConnectionPortPoller.start();
                    startAdbDebuggingThread();
                    AdbDebuggingManager.this.mAdbWifiEnabled = true;
                    return;
                case 19:
                    Settings.Global.putInt(AdbDebuggingManager.this.mContentResolver, "adb_wifi_enabled", 0);
                    sendServerConnectionState(false, -1);
                    return;
                case 20:
                    onPairingResult(((Bundle) message.obj).getString("publicKey"));
                    sendPairedDevicesToUI(this.mAdbKeyStore.getPairedDevices());
                    return;
                case 21:
                    sendPairingPortToUI(((Integer) message.obj).intValue());
                    return;
                case 22:
                    if (AdbDebuggingManager.this.mWifiConnectedKeys.add((String) message.obj)) {
                        sendPairedDevicesToUI(this.mAdbKeyStore.getPairedDevices());
                        showAdbConnectedNotification(true);
                        return;
                    }
                    return;
                case 23:
                    if (AdbDebuggingManager.this.mWifiConnectedKeys.remove((String) message.obj)) {
                        sendPairedDevicesToUI(this.mAdbKeyStore.getPairedDevices());
                        if (AdbDebuggingManager.this.mWifiConnectedKeys.isEmpty()) {
                            showAdbConnectedNotification(false);
                            return;
                        }
                        return;
                    }
                    return;
                case 24:
                    int intValue2 = ((Integer) message.obj).intValue();
                    onAdbdWifiServerConnected(intValue2);
                    synchronized (AdbDebuggingManager.this.mAdbConnectionInfo) {
                        AdbDebuggingManager.this.mAdbConnectionInfo.setPort(intValue2);
                    }
                    Settings.Global.putInt(AdbDebuggingManager.this.mContentResolver, "adb_wifi_enabled", 1);
                    return;
                case 25:
                    if (AdbDebuggingManager.this.mAdbWifiEnabled) {
                        onAdbdWifiServerDisconnected(((Integer) message.obj).intValue());
                        Settings.Global.putInt(AdbDebuggingManager.this.mContentResolver, "adb_wifi_enabled", 0);
                        stopAdbDebuggingThread();
                        if (AdbDebuggingManager.this.mConnectionPortPoller != null) {
                            AdbDebuggingManager.this.mConnectionPortPoller.cancelAndWait();
                            AdbDebuggingManager.this.mConnectionPortPoller = null;
                            return;
                        }
                        return;
                    }
                    return;
                case 26:
                    if (AdbDebuggingManager.this.mAdbWifiEnabled) {
                        AdbDebuggingManager.this.mConnectionPortPoller = new AdbConnectionPortPoller(AdbDebuggingManager.this.mPortListener);
                        AdbDebuggingManager.this.mConnectionPortPoller.start();
                        return;
                    }
                    return;
                case 27:
                    if (AdbDebuggingManager.this.mConnectionPortPoller != null) {
                        AdbDebuggingManager.this.mConnectionPortPoller.cancelAndWait();
                        AdbDebuggingManager.this.mConnectionPortPoller = null;
                    }
                    if (AdbDebuggingManager.this.mAdbWifiEnabled) {
                        onAdbdWifiServerDisconnected(-1);
                        return;
                    }
                    return;
                case 28:
                    this.mAdbKeyStore.reloadKeyMap();
                    return;
            }
        }

        void registerForAuthTimeChanges() {
            AdbDebuggingManager.this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("adb_allowed_connection_time"), false, this.mAuthTimeObserver);
        }

        private void logAdbConnectionChanged(String str, int i, boolean z) {
            long lastConnectionTime = this.mAdbKeyStore.getLastConnectionTime(str);
            long allowedConnectionTime = this.mAdbKeyStore.getAllowedConnectionTime();
            Slog.d(AdbDebuggingManager.TAG, "Logging key " + str + ", state = " + i + ", alwaysAllow = " + z + ", lastConnectionTime = " + lastConnectionTime + ", authWindow = " + allowedConnectionTime);
            FrameworkStatsLog.write(144, lastConnectionTime, allowedConnectionTime, i, z);
        }

        @VisibleForTesting
        long scheduleJobToUpdateAdbKeyStore() {
            cancelJobToUpdateAdbKeyStore();
            long nextExpirationTime = this.mAdbKeyStore.getNextExpirationTime();
            if (nextExpirationTime == -1) {
                return -1L;
            }
            long max = nextExpirationTime != 0 ? Math.max(Math.min(86400000L, nextExpirationTime), 60000L) : 0L;
            sendMessageDelayed(obtainMessage(9), max);
            return max;
        }

        private void cancelJobToUpdateAdbKeyStore() {
            removeMessages(9);
        }

        private String createPairingCode(int i) {
            SecureRandom secureRandom = new SecureRandom();
            String str = "";
            for (int i2 = 0; i2 < i; i2++) {
                str = str + secureRandom.nextInt(10);
            }
            return str;
        }

        private void sendServerConnectionState(boolean z, int i) {
            Intent intent = new Intent("com.android.server.adb.WIRELESS_DEBUG_STATUS");
            intent.putExtra("status", z ? 4 : 5);
            intent.putExtra("adb_port", i);
            AdbDebuggingManager.sendBroadcastWithDebugPermission(AdbDebuggingManager.this.mContext, intent, UserHandle.ALL);
        }

        private void onAdbdWifiServerConnected(int i) {
            sendPairedDevicesToUI(this.mAdbKeyStore.getPairedDevices());
            sendServerConnectionState(true, i);
        }

        private void onAdbdWifiServerDisconnected(int i) {
            AdbDebuggingManager.this.mWifiConnectedKeys.clear();
            showAdbConnectedNotification(false);
            sendServerConnectionState(false, i);
        }

        private AdbConnectionInfo getCurrentWifiApInfo() {
            String passpointProviderFriendlyName;
            WifiManager wifiManager = (WifiManager) AdbDebuggingManager.this.mContext.getSystemService("wifi");
            WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo == null || connectionInfo.getNetworkId() == -1) {
                Slog.i(AdbDebuggingManager.TAG, "Not connected to any wireless network. Not enabling adbwifi.");
                return null;
            }
            if (connectionInfo.isPasspointAp() || connectionInfo.isOsuAp()) {
                passpointProviderFriendlyName = connectionInfo.getPasspointProviderFriendlyName();
            } else {
                passpointProviderFriendlyName = connectionInfo.getSSID();
                if (passpointProviderFriendlyName == null || "<unknown ssid>".equals(passpointProviderFriendlyName)) {
                    List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
                    int size = configuredNetworks.size();
                    for (int i = 0; i < size; i++) {
                        if (configuredNetworks.get(i).networkId == connectionInfo.getNetworkId()) {
                            passpointProviderFriendlyName = configuredNetworks.get(i).SSID;
                        }
                    }
                    if (passpointProviderFriendlyName == null) {
                        Slog.e(AdbDebuggingManager.TAG, "Unable to get ssid of the wifi AP.");
                        return null;
                    }
                }
            }
            String bssid = connectionInfo.getBSSID();
            if (bssid == null || bssid.isEmpty()) {
                Slog.e(AdbDebuggingManager.TAG, "Unable to get the wifi ap's BSSID.");
                return null;
            }
            return new AdbConnectionInfo(bssid, passpointProviderFriendlyName);
        }

        private boolean verifyWifiNetwork(String str, String str2) {
            if (this.mAdbKeyStore.isTrustedNetwork(str)) {
                return true;
            }
            AdbDebuggingManager.this.startConfirmationForNetwork(str2, str);
            return false;
        }

        private void onPairingResult(String str) {
            if (str == null) {
                Intent intent = new Intent("com.android.server.adb.WIRELESS_DEBUG_PAIRING_RESULT");
                intent.putExtra("status", 0);
                AdbDebuggingManager.sendBroadcastWithDebugPermission(AdbDebuggingManager.this.mContext, intent, UserHandle.ALL);
                return;
            }
            Intent intent2 = new Intent("com.android.server.adb.WIRELESS_DEBUG_PAIRING_RESULT");
            intent2.putExtra("status", 1);
            String fingerprints = AdbDebuggingManager.this.getFingerprints(str);
            String[] split = str.split("\\s+");
            String str2 = split.length > 1 ? split[1] : "nouser@nohostname";
            PairDevice pairDevice = new PairDevice();
            pairDevice.name = fingerprints;
            pairDevice.guid = str2;
            pairDevice.connected = false;
            intent2.putExtra("pair_device", (Parcelable) pairDevice);
            AdbDebuggingManager.sendBroadcastWithDebugPermission(AdbDebuggingManager.this.mContext, intent2, UserHandle.ALL);
            this.mAdbKeyStore.setLastConnectionTime(str, AdbDebuggingManager.this.mTicker.currentTimeMillis());
            AdbDebuggingManager.this.sendPersistKeyStoreMessage();
            scheduleJobToUpdateAdbKeyStore();
        }

        private void sendPairingPortToUI(int i) {
            Intent intent = new Intent("com.android.server.adb.WIRELESS_DEBUG_PAIRING_RESULT");
            intent.putExtra("status", 4);
            intent.putExtra("adb_port", i);
            AdbDebuggingManager.sendBroadcastWithDebugPermission(AdbDebuggingManager.this.mContext, intent, UserHandle.ALL);
        }

        private void sendPairedDevicesToUI(Map<String, PairDevice> map) {
            Intent intent = new Intent("com.android.server.adb.WIRELESS_DEBUG_PAIRED_DEVICES");
            intent.putExtra("devices_map", (HashMap) map);
            AdbDebuggingManager.sendBroadcastWithDebugPermission(AdbDebuggingManager.this.mContext, intent, UserHandle.ALL);
        }

        private void updateUIPairCode(String str) {
            Intent intent = new Intent("com.android.server.adb.WIRELESS_DEBUG_PAIRING_RESULT");
            intent.putExtra("pairing_code", str);
            intent.putExtra("status", 3);
            AdbDebuggingManager.sendBroadcastWithDebugPermission(AdbDebuggingManager.this.mContext, intent, UserHandle.ALL);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getFingerprints(String str) {
        StringBuilder sb = new StringBuilder();
        if (str == null) {
            return "";
        }
        try {
            try {
                byte[] digest = MessageDigest.getInstance("MD5").digest(Base64.decode(str.split("\\s+")[0].getBytes(), 0));
                for (int i = 0; i < digest.length; i++) {
                    sb.append("0123456789ABCDEF".charAt((digest[i] >> 4) & 15));
                    sb.append("0123456789ABCDEF".charAt(digest[i] & 15));
                    if (i < digest.length - 1) {
                        sb.append(":");
                    }
                }
                return sb.toString();
            } catch (IllegalArgumentException e) {
                Slog.e(TAG, "error doing base64 decoding", e);
                return "";
            }
        } catch (Exception e2) {
            Slog.e(TAG, "Error getting digester", e2);
            return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startConfirmationForNetwork(String str, String str2) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new AbstractMap.SimpleEntry("ssid", str));
        arrayList.add(new AbstractMap.SimpleEntry("bssid", str2));
        int currentUser = ActivityManager.getCurrentUser();
        String string = Resources.getSystem().getString(R.string.config_geocoderProviderPackageName);
        ComponentName unflattenFromString = ComponentName.unflattenFromString(string);
        UserInfo userInfo = UserManager.get(this.mContext).getUserInfo(currentUser);
        if (startConfirmationActivity(unflattenFromString, userInfo.getUserHandle(), arrayList) || startConfirmationService(unflattenFromString, userInfo.getUserHandle(), arrayList)) {
            return;
        }
        Slog.e(TAG, "Unable to start customAdbWifiNetworkConfirmation[SecondaryUser]Component " + string + " as an Activity or a Service");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startConfirmationForKey(String str, String str2) {
        String str3;
        ArrayList arrayList = new ArrayList();
        arrayList.add(new AbstractMap.SimpleEntry("key", str));
        arrayList.add(new AbstractMap.SimpleEntry("fingerprints", str2));
        int currentUser = ActivityManager.getCurrentUser();
        UserInfo userInfo = UserManager.get(this.mContext).getUserInfo(currentUser);
        if (userInfo.isAdmin() || currentUser == 888) {
            str3 = this.mConfirmComponent;
            if (str3 == null) {
                str3 = Resources.getSystem().getString(R.string.config_forceVoiceInteractionServicePackage);
            }
        } else {
            str3 = Resources.getSystem().getString(R.string.config_fusedLocationProviderPackageName);
        }
        ComponentName unflattenFromString = ComponentName.unflattenFromString(str3);
        if (startConfirmationActivity(unflattenFromString, userInfo.getUserHandle(), arrayList) || startConfirmationService(unflattenFromString, userInfo.getUserHandle(), arrayList)) {
            return;
        }
        Slog.e(TAG, "unable to start customAdbPublicKeyConfirmation[SecondaryUser]Component " + str3 + " as an Activity or a Service");
    }

    private boolean startConfirmationActivity(ComponentName componentName, UserHandle userHandle, List<Map.Entry<String, String>> list) {
        PackageManager packageManager = this.mContext.getPackageManager();
        Intent createConfirmationIntent = createConfirmationIntent(componentName, list);
        createConfirmationIntent.addFlags(AudioFormat.EVRC);
        if (packageManager.resolveActivity(createConfirmationIntent, 65536) == null) {
            return false;
        }
        try {
            this.mContext.startActivityAsUser(createConfirmationIntent, userHandle);
            return true;
        } catch (ActivityNotFoundException e) {
            Slog.e(TAG, "unable to start adb whitelist activity: " + componentName, e);
            return false;
        }
    }

    private boolean startConfirmationService(ComponentName componentName, UserHandle userHandle, List<Map.Entry<String, String>> list) {
        try {
            return this.mContext.startServiceAsUser(createConfirmationIntent(componentName, list), userHandle) != null;
        } catch (SecurityException e) {
            Slog.e(TAG, "unable to start adb whitelist service: " + componentName, e);
            return false;
        }
    }

    private Intent createConfirmationIntent(ComponentName componentName, List<Map.Entry<String, String>> list) {
        Intent intent = new Intent();
        intent.setClassName(componentName.getPackageName(), componentName.getClassName());
        for (Map.Entry<String, String> entry : list) {
            intent.putExtra(entry.getKey(), entry.getValue());
        }
        return intent;
    }

    private static File getAdbFile(String str) {
        File file = new File(Environment.getDataDirectory(), ADB_DIRECTORY);
        if (!file.exists()) {
            Slog.e(TAG, "ADB data directory does not exist");
            return null;
        }
        return new File(file, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public File getAdbTempKeysFile() {
        return this.mTempKeysFile;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public File getUserKeyFile() {
        return this.mUserKeyFile;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeKeys(Iterable<String> iterable) {
        FileOutputStream fileOutputStream;
        if (this.mUserKeyFile == null) {
            return;
        }
        AtomicFile atomicFile = new AtomicFile(this.mUserKeyFile);
        try {
            fileOutputStream = atomicFile.startWrite();
            try {
                Iterator<String> it = iterable.iterator();
                while (it.hasNext()) {
                    fileOutputStream.write(it.next().getBytes());
                    fileOutputStream.write(10);
                }
                atomicFile.finishWrite(fileOutputStream);
                FileUtils.setPermissions(this.mUserKeyFile.toString(), FrameworkStatsLog.DISPLAY_HBM_STATE_CHANGED, -1, -1);
            } catch (IOException e) {
                e = e;
                Slog.e(TAG, "Error writing keys: " + e);
                atomicFile.failWrite(fileOutputStream);
            }
        } catch (IOException e2) {
            e = e2;
            fileOutputStream = null;
        }
    }

    public void setAdbEnabled(boolean z, byte b) {
        if (b == 0) {
            this.mHandler.sendEmptyMessage(z ? 1 : 2);
        } else {
            if (b == 1) {
                this.mHandler.sendEmptyMessage(z ? 11 : 12);
                return;
            }
            throw new IllegalArgumentException("setAdbEnabled called with unimplemented transport type=" + ((int) b));
        }
    }

    public void allowDebugging(boolean z, String str) {
        Message obtainMessage = this.mHandler.obtainMessage(3);
        obtainMessage.arg1 = z ? 1 : 0;
        obtainMessage.obj = str;
        this.mHandler.sendMessage(obtainMessage);
    }

    public void denyDebugging() {
        this.mHandler.sendEmptyMessage(4);
    }

    public void clearDebuggingKeys() {
        this.mHandler.sendEmptyMessage(6);
    }

    public void allowWirelessDebugging(boolean z, String str) {
        Message obtainMessage = this.mHandler.obtainMessage(18);
        obtainMessage.arg1 = z ? 1 : 0;
        obtainMessage.obj = str;
        this.mHandler.sendMessage(obtainMessage);
    }

    public void denyWirelessDebugging() {
        this.mHandler.sendEmptyMessage(19);
    }

    public int getAdbWirelessPort() {
        AdbConnectionInfo adbConnectionInfo = getAdbConnectionInfo();
        if (adbConnectionInfo == null) {
            return 0;
        }
        return adbConnectionInfo.getPort();
    }

    public Map<String, PairDevice> getPairedDevices() {
        return new AdbKeyStore().getPairedDevices();
    }

    public void unpairDevice(String str) {
        this.mHandler.sendMessage(Message.obtain(this.mHandler, 17, str));
    }

    public void enablePairingByPairingCode() {
        this.mHandler.sendEmptyMessage(15);
    }

    public void enablePairingByQrCode(String str, String str2) {
        Bundle bundle = new Bundle();
        bundle.putString("serviceName", str);
        bundle.putString(HintsHelper.AUTOFILL_HINT_PASSWORD, str2);
        this.mHandler.sendMessage(Message.obtain(this.mHandler, 16, bundle));
    }

    public void disablePairing() {
        this.mHandler.sendEmptyMessage(14);
    }

    public boolean isAdbWifiEnabled() {
        return this.mAdbWifiEnabled;
    }

    public void notifyKeyFilesUpdated() {
        this.mHandler.sendEmptyMessage(28);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendPersistKeyStoreMessage() {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(8));
    }

    public void dump(DualDumpOutputStream dualDumpOutputStream, String str, long j) {
        long start = dualDumpOutputStream.start(str, j);
        dualDumpOutputStream.write("connected_to_adb", 1133871366145L, this.mThread != null);
        DumpUtils.writeStringIfNotNull(dualDumpOutputStream, "last_key_received", 1138166333442L, this.mFingerprints);
        try {
            dualDumpOutputStream.write("user_keys", 1138166333443L, FileUtils.readTextFile(new File("/data/misc/adb/adb_keys"), 0, null));
        } catch (IOException e) {
            Slog.i(TAG, "Cannot read user keys", e);
        }
        try {
            dualDumpOutputStream.write("system_keys", 1138166333444L, FileUtils.readTextFile(new File("/adb_keys"), 0, null));
        } catch (IOException e2) {
            Slog.i(TAG, "Cannot read system keys", e2);
        }
        try {
            dualDumpOutputStream.write("keystore", 1138166333445L, FileUtils.readTextFile(this.mTempKeysFile, 0, null));
        } catch (IOException e3) {
            Slog.i(TAG, "Cannot read keystore: ", e3);
        }
        dualDumpOutputStream.end(start);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AdbKeyStore {
        private static final int KEYSTORE_VERSION = 1;
        private static final int MAX_SUPPORTED_KEYSTORE_VERSION = 1;
        public static final long NO_PREVIOUS_CONNECTION = 0;
        private static final String SYSTEM_KEY_FILE = "/adb_keys";
        private static final String XML_ATTRIBUTE_KEY = "key";
        private static final String XML_ATTRIBUTE_LAST_CONNECTION = "lastConnection";
        private static final String XML_ATTRIBUTE_VERSION = "version";
        private static final String XML_ATTRIBUTE_WIFI_BSSID = "bssid";
        private static final String XML_KEYSTORE_START_TAG = "keyStore";
        private static final String XML_TAG_ADB_KEY = "adbKey";
        private static final String XML_TAG_WIFI_ACCESS_POINT = "wifiAP";
        private AtomicFile mAtomicKeyFile;
        private final Set<String> mSystemKeys;
        private final Map<String, Long> mKeyMap = new HashMap();
        private final List<String> mTrustedNetworks = new ArrayList();

        AdbKeyStore() {
            initKeyFile();
            readTempKeysFile();
            this.mSystemKeys = getSystemKeysFromFile(SYSTEM_KEY_FILE);
            addExistingUserKeysToKeyStore();
        }

        public void reloadKeyMap() {
            readTempKeysFile();
        }

        public void addTrustedNetwork(String str) {
            this.mTrustedNetworks.add(str);
            AdbDebuggingManager.this.sendPersistKeyStoreMessage();
        }

        public Map<String, PairDevice> getPairedDevices() {
            HashMap hashMap = new HashMap();
            for (Map.Entry<String, Long> entry : this.mKeyMap.entrySet()) {
                String fingerprints = AdbDebuggingManager.this.getFingerprints(entry.getKey());
                String[] split = entry.getKey().split("\\s+");
                String str = split.length > 1 ? split[1] : "nouser@nohostname";
                PairDevice pairDevice = new PairDevice();
                pairDevice.name = str;
                pairDevice.guid = fingerprints;
                pairDevice.connected = AdbDebuggingManager.this.mWifiConnectedKeys.contains(entry.getKey());
                hashMap.put(entry.getKey(), pairDevice);
            }
            return hashMap;
        }

        public String findKeyFromFingerprint(String str) {
            for (Map.Entry<String, Long> entry : this.mKeyMap.entrySet()) {
                if (str.equals(AdbDebuggingManager.this.getFingerprints(entry.getKey()))) {
                    return entry.getKey();
                }
            }
            return null;
        }

        public void removeKey(String str) {
            if (this.mKeyMap.containsKey(str)) {
                this.mKeyMap.remove(str);
                AdbDebuggingManager.this.sendPersistKeyStoreMessage();
            }
        }

        private void initKeyFile() {
            if (AdbDebuggingManager.this.mTempKeysFile != null) {
                this.mAtomicKeyFile = new AtomicFile(AdbDebuggingManager.this.mTempKeysFile);
            }
        }

        private Set<String> getSystemKeysFromFile(String str) {
            HashSet hashSet = new HashSet();
            File file = new File(str);
            if (file.exists()) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                    while (true) {
                        try {
                            String readLine = bufferedReader.readLine();
                            if (readLine == null) {
                                break;
                            }
                            String trim = readLine.trim();
                            if (trim.length() > 0) {
                                hashSet.add(trim);
                            }
                        } finally {
                        }
                    }
                    bufferedReader.close();
                } catch (IOException e) {
                    Slog.e(AdbDebuggingManager.TAG, "Caught an exception reading " + str + ": " + e);
                }
            }
            return hashSet;
        }

        public boolean isEmpty() {
            return this.mKeyMap.isEmpty();
        }

        public void updateKeyStore() {
            if (filterOutOldKeys()) {
                AdbDebuggingManager.this.sendPersistKeyStoreMessage();
            }
        }

        private void readTempKeysFile() {
            TypedXmlPullParser resolvePullParser;
            this.mKeyMap.clear();
            this.mTrustedNetworks.clear();
            if (this.mAtomicKeyFile == null) {
                initKeyFile();
                if (this.mAtomicKeyFile == null) {
                    Slog.e(AdbDebuggingManager.TAG, "Unable to obtain the key file, " + AdbDebuggingManager.this.mTempKeysFile + ", for reading");
                    return;
                }
            }
            if (this.mAtomicKeyFile.exists()) {
                try {
                    FileInputStream openRead = this.mAtomicKeyFile.openRead();
                    try {
                        try {
                            resolvePullParser = Xml.resolvePullParser(openRead);
                            XmlUtils.beginDocument(resolvePullParser, XML_KEYSTORE_START_TAG);
                            int attributeInt = resolvePullParser.getAttributeInt((String) null, XML_ATTRIBUTE_VERSION);
                            if (attributeInt > 1) {
                                Slog.e(AdbDebuggingManager.TAG, "Keystore version=" + attributeInt + " not supported (max_supported=1)");
                                if (openRead != null) {
                                    openRead.close();
                                    return;
                                }
                                return;
                            }
                        } catch (XmlPullParserException unused) {
                            resolvePullParser = Xml.resolvePullParser(openRead);
                        }
                        readKeyStoreContents(resolvePullParser);
                        if (openRead != null) {
                            openRead.close();
                        }
                    } finally {
                    }
                } catch (IOException e) {
                    Slog.e(AdbDebuggingManager.TAG, "Caught an IOException parsing the XML key file: ", e);
                } catch (XmlPullParserException e2) {
                    Slog.e(AdbDebuggingManager.TAG, "Caught XmlPullParserException parsing the XML key file: ", e2);
                }
            }
        }

        private void readKeyStoreContents(TypedXmlPullParser typedXmlPullParser) throws XmlPullParserException, IOException {
            while (typedXmlPullParser.next() != 1) {
                String name = typedXmlPullParser.getName();
                if (XML_TAG_ADB_KEY.equals(name)) {
                    addAdbKeyToKeyMap(typedXmlPullParser);
                } else if (XML_TAG_WIFI_ACCESS_POINT.equals(name)) {
                    addTrustedNetworkToTrustedNetworks(typedXmlPullParser);
                } else {
                    Slog.w(AdbDebuggingManager.TAG, "Ignoring tag '" + name + "'. Not recognized.");
                }
                XmlUtils.skipCurrentTag(typedXmlPullParser);
            }
        }

        private void addAdbKeyToKeyMap(TypedXmlPullParser typedXmlPullParser) {
            String attributeValue = typedXmlPullParser.getAttributeValue((String) null, XML_ATTRIBUTE_KEY);
            try {
                this.mKeyMap.put(attributeValue, Long.valueOf(typedXmlPullParser.getAttributeLong((String) null, XML_ATTRIBUTE_LAST_CONNECTION)));
            } catch (XmlPullParserException e) {
                Slog.e(AdbDebuggingManager.TAG, "Error reading adbKey attributes", e);
            }
        }

        private void addTrustedNetworkToTrustedNetworks(TypedXmlPullParser typedXmlPullParser) {
            this.mTrustedNetworks.add(typedXmlPullParser.getAttributeValue((String) null, XML_ATTRIBUTE_WIFI_BSSID));
        }

        private void addExistingUserKeysToKeyStore() {
            if (AdbDebuggingManager.this.mUserKeyFile == null || !AdbDebuggingManager.this.mUserKeyFile.exists()) {
                return;
            }
            boolean z = false;
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(AdbDebuggingManager.this.mUserKeyFile));
                while (true) {
                    try {
                        String readLine = bufferedReader.readLine();
                        if (readLine == null) {
                            break;
                        } else if (!this.mKeyMap.containsKey(readLine)) {
                            this.mKeyMap.put(readLine, Long.valueOf(AdbDebuggingManager.this.mTicker.currentTimeMillis()));
                            z = true;
                        }
                    } finally {
                    }
                }
                bufferedReader.close();
            } catch (IOException e) {
                Slog.e(AdbDebuggingManager.TAG, "Caught an exception reading " + AdbDebuggingManager.this.mUserKeyFile + ": " + e);
            }
            if (z) {
                AdbDebuggingManager.this.sendPersistKeyStoreMessage();
            }
        }

        public void persistKeyStore() {
            filterOutOldKeys();
            if (this.mKeyMap.isEmpty() && this.mTrustedNetworks.isEmpty()) {
                deleteKeyStore();
                return;
            }
            if (this.mAtomicKeyFile == null) {
                initKeyFile();
                if (this.mAtomicKeyFile == null) {
                    Slog.e(AdbDebuggingManager.TAG, "Unable to obtain the key file, " + AdbDebuggingManager.this.mTempKeysFile + ", for writing");
                    return;
                }
            }
            FileOutputStream fileOutputStream = null;
            try {
                FileOutputStream startWrite = this.mAtomicKeyFile.startWrite();
                try {
                    TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(startWrite);
                    resolveSerializer.startDocument((String) null, Boolean.TRUE);
                    resolveSerializer.startTag((String) null, XML_KEYSTORE_START_TAG);
                    resolveSerializer.attributeInt((String) null, XML_ATTRIBUTE_VERSION, 1);
                    for (Map.Entry<String, Long> entry : this.mKeyMap.entrySet()) {
                        resolveSerializer.startTag((String) null, XML_TAG_ADB_KEY);
                        resolveSerializer.attribute((String) null, XML_ATTRIBUTE_KEY, entry.getKey());
                        resolveSerializer.attributeLong((String) null, XML_ATTRIBUTE_LAST_CONNECTION, entry.getValue().longValue());
                        resolveSerializer.endTag((String) null, XML_TAG_ADB_KEY);
                    }
                    for (String str : this.mTrustedNetworks) {
                        resolveSerializer.startTag((String) null, XML_TAG_WIFI_ACCESS_POINT);
                        resolveSerializer.attribute((String) null, XML_ATTRIBUTE_WIFI_BSSID, str);
                        resolveSerializer.endTag((String) null, XML_TAG_WIFI_ACCESS_POINT);
                    }
                    resolveSerializer.endTag((String) null, XML_KEYSTORE_START_TAG);
                    resolveSerializer.endDocument();
                    this.mAtomicKeyFile.finishWrite(startWrite);
                } catch (IOException e) {
                    e = e;
                    fileOutputStream = startWrite;
                    Slog.e(AdbDebuggingManager.TAG, "Caught an exception writing the key map: ", e);
                    this.mAtomicKeyFile.failWrite(fileOutputStream);
                    AdbDebuggingManager.this.writeKeys(this.mKeyMap.keySet());
                }
            } catch (IOException e2) {
                e = e2;
            }
            AdbDebuggingManager.this.writeKeys(this.mKeyMap.keySet());
        }

        private boolean filterOutOldKeys() {
            long allowedConnectionTime = getAllowedConnectionTime();
            boolean z = false;
            if (allowedConnectionTime == 0) {
                return false;
            }
            long currentTimeMillis = AdbDebuggingManager.this.mTicker.currentTimeMillis();
            Iterator<Map.Entry<String, Long>> it = this.mKeyMap.entrySet().iterator();
            while (it.hasNext()) {
                if (currentTimeMillis > it.next().getValue().longValue() + allowedConnectionTime) {
                    it.remove();
                    z = true;
                }
            }
            if (z) {
                AdbDebuggingManager.this.writeKeys(this.mKeyMap.keySet());
            }
            return z;
        }

        public long getNextExpirationTime() {
            long allowedConnectionTime = getAllowedConnectionTime();
            if (allowedConnectionTime == 0) {
                return -1L;
            }
            long currentTimeMillis = AdbDebuggingManager.this.mTicker.currentTimeMillis();
            Iterator<Map.Entry<String, Long>> it = this.mKeyMap.entrySet().iterator();
            long j = -1;
            while (it.hasNext()) {
                long max = Math.max(0L, (it.next().getValue().longValue() + allowedConnectionTime) - currentTimeMillis);
                if (j == -1 || max < j) {
                    j = max;
                }
            }
            return j;
        }

        public void deleteKeyStore() {
            this.mKeyMap.clear();
            this.mTrustedNetworks.clear();
            if (AdbDebuggingManager.this.mUserKeyFile != null) {
                AdbDebuggingManager.this.mUserKeyFile.delete();
            }
            AtomicFile atomicFile = this.mAtomicKeyFile;
            if (atomicFile == null) {
                return;
            }
            atomicFile.delete();
        }

        public long getLastConnectionTime(String str) {
            return this.mKeyMap.getOrDefault(str, 0L).longValue();
        }

        public void setLastConnectionTime(String str, long j) {
            setLastConnectionTime(str, j, false);
        }

        @VisibleForTesting
        void setLastConnectionTime(String str, long j, boolean z) {
            if ((!this.mKeyMap.containsKey(str) || this.mKeyMap.get(str).longValue() < j || z) && !this.mSystemKeys.contains(str)) {
                this.mKeyMap.put(str, Long.valueOf(j));
            }
        }

        public long getAllowedConnectionTime() {
            return Settings.Global.getLong(AdbDebuggingManager.this.mContext.getContentResolver(), "adb_allowed_connection_time", 604800000L);
        }

        public boolean isKeyAuthorized(String str) {
            if (this.mSystemKeys.contains(str)) {
                return true;
            }
            long lastConnectionTime = getLastConnectionTime(str);
            if (lastConnectionTime == 0) {
                return false;
            }
            long allowedConnectionTime = getAllowedConnectionTime();
            return allowedConnectionTime == 0 || AdbDebuggingManager.this.mTicker.currentTimeMillis() < lastConnectionTime + allowedConnectionTime;
        }

        public boolean isTrustedNetwork(String str) {
            return this.mTrustedNetworks.contains(str);
        }
    }
}
