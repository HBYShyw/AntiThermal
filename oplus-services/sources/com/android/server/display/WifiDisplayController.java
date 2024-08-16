package com.android.server.display;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.hardware.display.WifiDisplay;
import android.hardware.display.WifiDisplaySessionInfo;
import android.media.RemoteDisplay;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pWfdInfo;
import android.os.Build;
import android.os.Handler;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Slog;
import android.view.Surface;
import com.android.internal.util.DumpUtils;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Objects;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class WifiDisplayController implements DumpUtils.Dump {
    private static final int CONNECTION_TIMEOUT_SECONDS = 30;
    private static final int CONNECT_MAX_RETRIES = 3;
    private static final int CONNECT_RETRY_DELAY_MILLIS = 500;
    private static final boolean DEBUG;
    private static final boolean DEBUGV;
    private static final int DEFAULT_CONTROL_PORT = 7236;
    private static final int DISCOVER_PEERS_INTERVAL_MILLIS = 5000;
    private static final int MAX_THROUGHPUT = 50;
    private static final int RTSP_TIMEOUT_SECONDS = 30;
    private static final int RTSP_TIMEOUT_SECONDS_CERT_MODE = 120;
    private static final String TAG = "WifiDisplayController";
    private long connectedTime;
    private WifiDisplay mAdvertisedDisplay;
    private int mAdvertisedDisplayFlags;
    private int mAdvertisedDisplayHeight;
    private Surface mAdvertisedDisplaySurface;
    private int mAdvertisedDisplayWidth;
    private WifiP2pDevice mCancelingDevice;
    public WifiP2pDevice mConnectedDevice;
    private WifiP2pGroup mConnectedDeviceGroupInfo;
    public WifiP2pDevice mConnectingDevice;
    private int mConnectionRetriesLeft;
    private final Context mContext;
    private WifiP2pDevice mDesiredDevice;
    private WifiP2pDevice mDisconnectingDevice;
    private boolean mDiscoverPeersInProgress;
    private Object mExtRemoteDisplay;
    private final Handler mHandler;
    private final Listener mListener;
    private NetworkInfo mNetworkInfo;
    private OplusWifiDisplayUsageHelperWrapper mOwduhWrapper;
    private RemoteDisplay mRemoteDisplay;
    private boolean mRemoteDisplayConnected;
    private String mRemoteDisplayInterface;
    private boolean mScanRequested;
    private WifiP2pDevice mThisDevice;
    private WifiDisplayControllerWrapper mWdcWrapper;
    private boolean mWfdEnabled;
    private boolean mWfdEnabling;
    private boolean mWifiDisplayCertMode;
    private boolean mWifiDisplayOnSetting;
    private WifiP2pManager.Channel mWifiP2pChannel;
    private boolean mWifiP2pEnabled;
    private WifiP2pManager mWifiP2pManager;
    private final BroadcastReceiver mWifiP2pReceiver;
    public final ArrayList<WifiP2pDevice> mAvailableWifiDisplayPeers = new ArrayList<>();
    private int mWifiDisplayWpsConfig = 4;
    public IWifiDisplayControllerSocExt mWdcSocExt = (IWifiDisplayControllerSocExt) ExtLoader.type(IWifiDisplayControllerSocExt.class).base(this).create();
    private final Runnable mDiscoverPeers = new Runnable() { // from class: com.android.server.display.WifiDisplayController.16
        @Override // java.lang.Runnable
        public void run() {
            WifiDisplayController.this.tryDiscoverPeers();
        }
    };
    private final Runnable mConnectionTimeout = new Runnable() { // from class: com.android.server.display.WifiDisplayController.17
        @Override // java.lang.Runnable
        public void run() {
            WifiDisplayController wifiDisplayController = WifiDisplayController.this;
            WifiP2pDevice wifiP2pDevice = wifiDisplayController.mConnectingDevice;
            if (wifiP2pDevice == null || wifiP2pDevice != wifiDisplayController.mDesiredDevice) {
                return;
            }
            Slog.i(WifiDisplayController.TAG, "Timed out waiting for Wifi display connection after 30 seconds: " + WifiDisplayController.this.mConnectingDevice.deviceName);
            WifiDisplayController.this.handleConnectionFailure(true);
            IOplusWifiDisplayUsageHelperExt extImpl = WifiDisplayController.this.mOwduhWrapper.getExtImpl();
            WifiDisplayController wifiDisplayController2 = WifiDisplayController.this;
            extImpl.wfdConnectedFailed("P2P_Connection_Timeout", wifiDisplayController2.mConnectingDevice, wifiDisplayController2.mConnectedDeviceGroupInfo, WifiDisplayController.this.mContext);
        }
    };
    private final Runnable mRtspTimeout = new Runnable() { // from class: com.android.server.display.WifiDisplayController.18
        @Override // java.lang.Runnable
        public void run() {
            WifiDisplayController wifiDisplayController = WifiDisplayController.this;
            if (wifiDisplayController.mConnectedDevice != null) {
                if ((wifiDisplayController.mRemoteDisplay == null && WifiDisplayController.this.mExtRemoteDisplay == null) || WifiDisplayController.this.mRemoteDisplayConnected) {
                    return;
                }
                Slog.i(WifiDisplayController.TAG, "Timed out waiting for Wifi display RTSP connection after 30 seconds: " + WifiDisplayController.this.mConnectedDevice.deviceName);
                WifiDisplayController.this.handleConnectionFailure(true);
                IOplusWifiDisplayUsageHelperExt extImpl = WifiDisplayController.this.mOwduhWrapper.getExtImpl();
                WifiDisplayController wifiDisplayController2 = WifiDisplayController.this;
                extImpl.wfdConnectedFailed("RTSP_TimeOut", wifiDisplayController2.mConnectedDevice, wifiDisplayController2.mConnectedDeviceGroupInfo, WifiDisplayController.this.mContext);
            }
        }
    };

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Listener {
        void onDisplayChanged(WifiDisplay wifiDisplay);

        void onDisplayConnected(WifiDisplay wifiDisplay, Surface surface, int i, int i2, int i3);

        void onDisplayConnecting(WifiDisplay wifiDisplay);

        void onDisplayConnectionFailed();

        void onDisplayDisconnected();

        void onDisplaySessionInfo(WifiDisplaySessionInfo wifiDisplaySessionInfo);

        void onFeatureStateChanged(int i);

        void onScanFinished();

        void onScanResults(WifiDisplay[] wifiDisplayArr);

        void onScanStarted();
    }

    private static boolean isPrimarySinkDeviceType(int i) {
        return i == 1 || i == 3;
    }

    static {
        DEBUG = Build.isMtkPlatform() ? true : SystemProperties.getBoolean("persist.vendor.debug.wfdcdbg", false);
        DEBUGV = SystemProperties.getBoolean("persist.vendor.debug.wfdcdbgv", false);
    }

    public WifiDisplayController(Context context, Handler handler, Listener listener) {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.display.WifiDisplayController.22
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                if (action.equals("android.net.wifi.p2p.STATE_CHANGED")) {
                    boolean z = intent.getIntExtra("wifi_p2p_state", 1) == 2;
                    if (WifiDisplayController.DEBUG) {
                        Slog.d(WifiDisplayController.TAG, "Received WIFI_P2P_STATE_CHANGED_ACTION: enabled=" + z);
                    }
                    WifiDisplayController.this.handleStateChanged(z);
                    return;
                }
                if (action.equals("android.net.wifi.p2p.PEERS_CHANGED")) {
                    if (WifiDisplayController.DEBUG) {
                        Slog.d(WifiDisplayController.TAG, "Received WIFI_P2P_PEERS_CHANGED_ACTION.");
                    }
                    WifiDisplayController.this.handlePeersChanged();
                    return;
                }
                if (action.equals("android.net.wifi.p2p.CONNECTION_STATE_CHANGE")) {
                    NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo", NetworkInfo.class);
                    if (WifiDisplayController.DEBUG) {
                        Slog.d(WifiDisplayController.TAG, "Received WIFI_P2P_CONNECTION_CHANGED_ACTION: networkInfo=" + networkInfo);
                    }
                    WifiDisplayController.this.handleConnectionChanged(networkInfo);
                    return;
                }
                if (action.equals("android.net.wifi.p2p.THIS_DEVICE_CHANGED")) {
                    WifiDisplayController.this.mThisDevice = (WifiP2pDevice) intent.getParcelableExtra("wifiP2pDevice", WifiP2pDevice.class);
                    if (WifiDisplayController.DEBUG) {
                        Slog.d(WifiDisplayController.TAG, "Received WIFI_P2P_THIS_DEVICE_CHANGED_ACTION: mThisDevice= " + WifiDisplayController.this.mThisDevice);
                    }
                }
            }
        };
        this.mWifiP2pReceiver = broadcastReceiver;
        this.mWdcWrapper = new WifiDisplayControllerWrapper();
        this.mOwduhWrapper = new OplusWifiDisplayUsageHelperWrapper();
        this.mContext = context;
        this.mHandler = handler;
        this.mListener = listener;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.p2p.STATE_CHANGED");
        intentFilter.addAction("android.net.wifi.p2p.PEERS_CHANGED");
        intentFilter.addAction("android.net.wifi.p2p.CONNECTION_STATE_CHANGE");
        intentFilter.addAction("android.net.wifi.p2p.THIS_DEVICE_CHANGED");
        context.registerReceiver(broadcastReceiver, intentFilter, null, handler);
        ContentObserver contentObserver = new ContentObserver(handler) { // from class: com.android.server.display.WifiDisplayController.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z, Uri uri) {
                WifiDisplayController.this.updateSettings();
            }
        };
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.registerContentObserver(Settings.Global.getUriFor("wifi_display_on"), false, contentObserver);
        contentResolver.registerContentObserver(Settings.Global.getUriFor("wifi_display_certification_on"), false, contentObserver);
        contentResolver.registerContentObserver(Settings.Global.getUriFor("wifi_display_wps_config"), false, contentObserver);
        updateSettings();
        this.mWdcSocExt.initSocWifiDisplayController(context, handler, this);
        this.mWdcWrapper.getExtImpl().initWifiDisplayControllerExtImpl(context);
    }

    private void retrieveWifiP2pManagerAndChannel() {
        WifiP2pManager wifiP2pManager;
        if (this.mWifiP2pManager == null) {
            this.mWifiP2pManager = (WifiP2pManager) this.mContext.getSystemService("wifip2p");
        }
        if (this.mWifiP2pChannel != null || (wifiP2pManager = this.mWifiP2pManager) == null) {
            return;
        }
        this.mWifiP2pChannel = wifiP2pManager.initialize(this.mContext, this.mHandler.getLooper(), null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSettings() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        this.mWifiDisplayOnSetting = Settings.Global.getInt(contentResolver, "wifi_display_on", 0) != 0;
        boolean z = Settings.Global.getInt(contentResolver, "wifi_display_certification_on", 0) != 0;
        this.mWifiDisplayCertMode = z;
        this.mWifiDisplayWpsConfig = 4;
        if (z) {
            this.mWifiDisplayWpsConfig = Settings.Global.getInt(contentResolver, "wifi_display_wps_config", 4);
        }
        updateWfdEnableState();
    }

    public void dump(PrintWriter printWriter, String str) {
        printWriter.println("mWifiDisplayOnSetting=" + this.mWifiDisplayOnSetting);
        printWriter.println("mWifiP2pEnabled=" + this.mWifiP2pEnabled);
        printWriter.println("mWfdEnabled=" + this.mWfdEnabled);
        printWriter.println("mWfdEnabling=" + this.mWfdEnabling);
        printWriter.println("mNetworkInfo=" + this.mNetworkInfo);
        printWriter.println("mScanRequested=" + this.mScanRequested);
        printWriter.println("mDiscoverPeersInProgress=" + this.mDiscoverPeersInProgress);
        printWriter.println("mDesiredDevice=" + describeWifiP2pDevice(this.mDesiredDevice));
        printWriter.println("mConnectingDisplay=" + describeWifiP2pDevice(this.mConnectingDevice));
        printWriter.println("mDisconnectingDisplay=" + describeWifiP2pDevice(this.mDisconnectingDevice));
        printWriter.println("mCancelingDisplay=" + describeWifiP2pDevice(this.mCancelingDevice));
        printWriter.println("mConnectedDevice=" + describeWifiP2pDevice(this.mConnectedDevice));
        printWriter.println("mConnectionRetriesLeft=" + this.mConnectionRetriesLeft);
        printWriter.println("mRemoteDisplay=" + this.mRemoteDisplay);
        printWriter.println("mRemoteDisplayInterface=" + this.mRemoteDisplayInterface);
        printWriter.println("mRemoteDisplayConnected=" + this.mRemoteDisplayConnected);
        printWriter.println("mAdvertisedDisplay=" + this.mAdvertisedDisplay);
        printWriter.println("mAdvertisedDisplaySurface=" + this.mAdvertisedDisplaySurface);
        printWriter.println("mAdvertisedDisplayWidth=" + this.mAdvertisedDisplayWidth);
        printWriter.println("mAdvertisedDisplayHeight=" + this.mAdvertisedDisplayHeight);
        printWriter.println("mAdvertisedDisplayFlags=" + this.mAdvertisedDisplayFlags);
        printWriter.println("mAvailableWifiDisplayPeers: size=" + this.mAvailableWifiDisplayPeers.size());
        Iterator<WifiP2pDevice> it = this.mAvailableWifiDisplayPeers.iterator();
        while (it.hasNext()) {
            printWriter.println("  " + describeWifiP2pDevice(it.next()));
        }
    }

    private void dump() {
        Slog.d(TAG, "mWifiDisplayOnSetting=" + this.mWifiDisplayOnSetting);
        Slog.d(TAG, "mWifiP2pEnabled=" + this.mWifiP2pEnabled);
        Slog.d(TAG, "mWfdEnabled=" + this.mWfdEnabled);
        Slog.d(TAG, "mWfdEnabling=" + this.mWfdEnabling);
        Slog.d(TAG, "mNetworkInfo=" + this.mNetworkInfo);
        Slog.d(TAG, "mScanRequested=" + this.mScanRequested);
        Slog.d(TAG, "mDiscoverPeersInProgress=" + this.mDiscoverPeersInProgress);
        Slog.d(TAG, "mDesiredDevice=" + describeWifiP2pDevice(this.mDesiredDevice));
        Slog.d(TAG, "mConnectingDisplay=" + describeWifiP2pDevice(this.mConnectingDevice));
        Slog.d(TAG, "mDisconnectingDisplay=" + describeWifiP2pDevice(this.mDisconnectingDevice));
        Slog.d(TAG, "mCancelingDisplay=" + describeWifiP2pDevice(this.mCancelingDevice));
        Slog.d(TAG, "mConnectedDevice=" + describeWifiP2pDevice(this.mConnectedDevice));
        Slog.d(TAG, "mConnectionRetriesLeft=" + this.mConnectionRetriesLeft);
        Slog.d(TAG, "mRemoteDisplay=" + this.mRemoteDisplay);
        Slog.d(TAG, "mRemoteDisplayInterface=" + this.mRemoteDisplayInterface);
        Slog.d(TAG, "mRemoteDisplayConnected=" + this.mRemoteDisplayConnected);
        Slog.d(TAG, "mAdvertisedDisplay=" + this.mAdvertisedDisplay);
        Slog.d(TAG, "mAdvertisedDisplaySurface=" + this.mAdvertisedDisplaySurface);
        Slog.d(TAG, "mAdvertisedDisplayWidth=" + this.mAdvertisedDisplayWidth);
        Slog.d(TAG, "mAdvertisedDisplayHeight=" + this.mAdvertisedDisplayHeight);
        Slog.d(TAG, "mAdvertisedDisplayFlags=" + this.mAdvertisedDisplayFlags);
        Slog.d(TAG, "mAvailableWifiDisplayPeers: size=" + this.mAvailableWifiDisplayPeers.size());
        Iterator<WifiP2pDevice> it = this.mAvailableWifiDisplayPeers.iterator();
        while (it.hasNext()) {
            Slog.d(TAG, "  " + describeWifiP2pDevice(it.next()));
        }
    }

    public void requestStartScan() {
        if (!this.mScanRequested) {
            this.mScanRequested = true;
            updateScanState();
        }
        this.mWdcWrapper.getExtImpl().updateWFDControl();
    }

    public void requestStopScan() {
        if (this.mScanRequested) {
            this.mScanRequested = false;
            updateScanState();
        }
    }

    public void requestConnect(String str) {
        Iterator<WifiP2pDevice> it = this.mAvailableWifiDisplayPeers.iterator();
        while (it.hasNext()) {
            WifiP2pDevice next = it.next();
            if (next.deviceAddress.equals(str)) {
                connect(next);
            }
        }
    }

    public void requestPause() {
        RemoteDisplay remoteDisplay = this.mRemoteDisplay;
        if (remoteDisplay != null) {
            remoteDisplay.pause();
        }
    }

    public void requestResume() {
        RemoteDisplay remoteDisplay = this.mRemoteDisplay;
        if (remoteDisplay != null) {
            remoteDisplay.resume();
        }
    }

    public void requestDisconnect() {
        disconnect();
    }

    private void updateWfdEnableState() {
        if (this.mWifiDisplayOnSetting && this.mWifiP2pEnabled) {
            if (this.mWfdEnabled || this.mWfdEnabling) {
                return;
            }
            this.mWfdEnabling = true;
            WifiP2pWfdInfo wifiP2pWfdInfo = new WifiP2pWfdInfo();
            wifiP2pWfdInfo.setEnabled(true);
            wifiP2pWfdInfo.setDeviceType(0);
            wifiP2pWfdInfo.setSessionAvailable(true);
            wifiP2pWfdInfo.setControlPort(DEFAULT_CONTROL_PORT);
            wifiP2pWfdInfo.setMaxThroughput(50);
            this.mWifiP2pManager.setWfdInfo(this.mWifiP2pChannel, wifiP2pWfdInfo, new WifiP2pManager.ActionListener() { // from class: com.android.server.display.WifiDisplayController.2
                @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                public void onSuccess() {
                    if (WifiDisplayController.DEBUG) {
                        Slog.d(WifiDisplayController.TAG, "Successfully set WFD info.");
                    }
                    if (WifiDisplayController.this.mWfdEnabling) {
                        WifiDisplayController.this.mWfdEnabling = false;
                        WifiDisplayController.this.mWfdEnabled = true;
                        WifiDisplayController.this.reportFeatureState();
                        WifiDisplayController.this.updateScanState();
                    }
                }

                @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                public void onFailure(int i) {
                    if (WifiDisplayController.DEBUG) {
                        Slog.d(WifiDisplayController.TAG, "Failed to set WFD info with reason " + i + ".");
                    }
                    WifiDisplayController.this.mWfdEnabling = false;
                }
            });
            this.mOwduhWrapper.getExtImpl().reportWfdEnableState(wifiP2pWfdInfo, true, this.mContext);
            return;
        }
        if (this.mWfdEnabled || this.mWfdEnabling) {
            WifiP2pWfdInfo wifiP2pWfdInfo2 = new WifiP2pWfdInfo();
            wifiP2pWfdInfo2.setEnabled(false);
            this.mOwduhWrapper.getExtImpl().reportWfdEnableState(wifiP2pWfdInfo2, false, this.mContext);
            this.mWifiP2pManager.setWfdInfo(this.mWifiP2pChannel, wifiP2pWfdInfo2, new WifiP2pManager.ActionListener() { // from class: com.android.server.display.WifiDisplayController.3
                @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                public void onSuccess() {
                    if (WifiDisplayController.DEBUG) {
                        Slog.d(WifiDisplayController.TAG, "Successfully set WFD info.");
                    }
                }

                @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                public void onFailure(int i) {
                    if (WifiDisplayController.DEBUG) {
                        Slog.d(WifiDisplayController.TAG, "Failed to set WFD info with reason " + i + ".");
                    }
                }
            });
        }
        this.mWfdEnabling = false;
        this.mWfdEnabled = false;
        reportFeatureState();
        updateScanState();
        disconnect();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportFeatureState() {
        final int computeFeatureState = computeFeatureState();
        this.mHandler.post(new Runnable() { // from class: com.android.server.display.WifiDisplayController.4
            @Override // java.lang.Runnable
            public void run() {
                WifiDisplayController.this.mListener.onFeatureStateChanged(computeFeatureState);
            }
        });
    }

    private int computeFeatureState() {
        if (this.mWifiP2pEnabled) {
            return this.mWifiDisplayOnSetting ? 3 : 2;
        }
        if (!this.mWifiDisplayOnSetting) {
            return 1;
        }
        Slog.d(TAG, "Wifi p2p is disabled, update WIFI_DISPLAY_ON as false.");
        Settings.Global.putInt(this.mContext.getContentResolver(), "wifi_display_on", 0);
        this.mWifiDisplayOnSetting = false;
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateScanState() {
        boolean z = this.mScanRequested;
        if ((z && this.mWfdEnabled && this.mDesiredDevice == null && this.mConnectedDevice == null && this.mDisconnectingDevice == null) || (z && this.mWfdEnabled && this.mWdcSocExt.getRemoveGroupFlag())) {
            if (this.mDiscoverPeersInProgress) {
                return;
            }
            Slog.i(TAG, "Starting Wifi display scan.");
            this.mDiscoverPeersInProgress = true;
            handleScanStarted();
            this.mOwduhWrapper.getExtImpl().getWfdScanState(true, this.mContext);
            tryDiscoverPeers();
            return;
        }
        if (this.mDiscoverPeersInProgress) {
            this.mHandler.removeCallbacks(this.mDiscoverPeers);
            WifiP2pDevice wifiP2pDevice = this.mDesiredDevice;
            if (wifiP2pDevice == null || wifiP2pDevice == this.mConnectedDevice || this.mWdcSocExt.getRemoveGroupFlag()) {
                Slog.i(TAG, "Stopping Wifi display scan.");
                this.mDiscoverPeersInProgress = false;
                stopPeerDiscovery();
                handleScanFinished();
                this.mOwduhWrapper.getExtImpl().getWfdScanState(false, this.mContext);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryDiscoverPeers() {
        this.mWifiP2pManager.discoverPeers(this.mWifiP2pChannel, new WifiP2pManager.ActionListener() { // from class: com.android.server.display.WifiDisplayController.5
            @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
            public void onSuccess() {
                if (WifiDisplayController.DEBUG) {
                    Slog.d(WifiDisplayController.TAG, "Discover peers succeeded.  Requesting peers now.");
                }
                if (WifiDisplayController.this.mDiscoverPeersInProgress) {
                    WifiDisplayController.this.requestPeers();
                }
                WifiDisplayController.this.mOwduhWrapper.getExtImpl().getDiscoverPeersState(true, WifiDisplayController.this.mContext);
            }

            @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
            public void onFailure(int i) {
                if (WifiDisplayController.DEBUG) {
                    Slog.d(WifiDisplayController.TAG, "Discover peers failed with reason " + i + ".");
                }
            }
        });
        this.mHandler.postDelayed(this.mDiscoverPeers, 5000L);
    }

    private void stopPeerDiscovery() {
        this.mWifiP2pManager.stopPeerDiscovery(this.mWifiP2pChannel, new WifiP2pManager.ActionListener() { // from class: com.android.server.display.WifiDisplayController.6
            @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
            public void onSuccess() {
                if (WifiDisplayController.DEBUG) {
                    Slog.d(WifiDisplayController.TAG, "Stop peer discovery succeeded.");
                }
            }

            @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
            public void onFailure(int i) {
                if (WifiDisplayController.DEBUG) {
                    Slog.d(WifiDisplayController.TAG, "Stop peer discovery failed with reason " + i + ".");
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestPeers() {
        WifiP2pManager wifiP2pManager = this.mWifiP2pManager;
        if (wifiP2pManager == null) {
            Slog.d(TAG, "requestPeers failed, mWifiP2pManager is null");
        } else {
            wifiP2pManager.requestPeers(this.mWifiP2pChannel, new WifiP2pManager.PeerListListener() { // from class: com.android.server.display.WifiDisplayController.7
                @Override // android.net.wifi.p2p.WifiP2pManager.PeerListListener
                public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
                    if (WifiDisplayController.DEBUG) {
                        Slog.d(WifiDisplayController.TAG, "Received list of peers.");
                    }
                    WifiDisplayController.this.mAvailableWifiDisplayPeers.clear();
                    for (WifiP2pDevice wifiP2pDevice : wifiP2pDeviceList.getDeviceList()) {
                        if (WifiDisplayController.DEBUG) {
                            Slog.d(WifiDisplayController.TAG, "  " + WifiDisplayController.describeWifiP2pDevice(wifiP2pDevice));
                        }
                        if (WifiDisplayController.isWifiDisplay(wifiP2pDevice)) {
                            WifiDisplayController.this.mAvailableWifiDisplayPeers.add(wifiP2pDevice);
                            WifiDisplayController.this.mOwduhWrapper.getExtImpl().getPeers(WifiDisplayController.this.mContext);
                        }
                    }
                    if (WifiDisplayController.this.mDiscoverPeersInProgress) {
                        WifiDisplayController.this.handleScanResults();
                    }
                }
            });
        }
    }

    private void handleScanStarted() {
        this.mHandler.post(new Runnable() { // from class: com.android.server.display.WifiDisplayController.8
            @Override // java.lang.Runnable
            public void run() {
                WifiDisplayController.this.mListener.onScanStarted();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleScanResults() {
        int size = this.mAvailableWifiDisplayPeers.size();
        final WifiDisplay[] wifiDisplayArr = (WifiDisplay[]) WifiDisplay.CREATOR.newArray(size);
        for (int i = 0; i < size; i++) {
            WifiP2pDevice wifiP2pDevice = this.mAvailableWifiDisplayPeers.get(i);
            wifiDisplayArr[i] = createWifiDisplay(wifiP2pDevice);
            updateDesiredDevice(wifiP2pDevice);
        }
        this.mHandler.post(new Runnable() { // from class: com.android.server.display.WifiDisplayController.9
            @Override // java.lang.Runnable
            public void run() {
                WifiDisplayController.this.mListener.onScanResults(wifiDisplayArr);
            }
        });
    }

    private void handleScanFinished() {
        this.mHandler.post(new Runnable() { // from class: com.android.server.display.WifiDisplayController.10
            @Override // java.lang.Runnable
            public void run() {
                WifiDisplayController.this.mListener.onScanFinished();
            }
        });
    }

    private void updateDesiredDevice(WifiP2pDevice wifiP2pDevice) {
        String str = wifiP2pDevice.deviceAddress;
        WifiP2pDevice wifiP2pDevice2 = this.mDesiredDevice;
        if (wifiP2pDevice2 == null || !wifiP2pDevice2.deviceAddress.equals(str)) {
            return;
        }
        if (DEBUG) {
            Slog.d(TAG, "updateDesiredDevice: new information " + describeWifiP2pDevice(wifiP2pDevice));
        }
        this.mDesiredDevice.update(wifiP2pDevice);
        WifiDisplay wifiDisplay = this.mAdvertisedDisplay;
        if (wifiDisplay == null || !wifiDisplay.getDeviceAddress().equals(str)) {
            return;
        }
        readvertiseDisplay(createWifiDisplay(this.mDesiredDevice));
    }

    private void connect(WifiP2pDevice wifiP2pDevice) {
        WifiP2pDevice wifiP2pDevice2 = this.mDesiredDevice;
        if (wifiP2pDevice2 != null && !wifiP2pDevice2.deviceAddress.equals(wifiP2pDevice.deviceAddress)) {
            if (DEBUG) {
                Slog.d(TAG, "connect: nothing to do, already connecting to " + describeWifiP2pDevice(wifiP2pDevice));
                return;
            }
            return;
        }
        WifiP2pDevice wifiP2pDevice3 = this.mConnectedDevice;
        if (wifiP2pDevice3 != null && !wifiP2pDevice3.deviceAddress.equals(wifiP2pDevice.deviceAddress) && this.mDesiredDevice == null) {
            if (DEBUG) {
                Slog.d(TAG, "connect: nothing to do, already connected to " + describeWifiP2pDevice(wifiP2pDevice) + " and not part way through connecting to a different device.");
                return;
            }
            return;
        }
        if (!this.mWfdEnabled) {
            Slog.i(TAG, "Ignoring request to connect to Wifi display because the  feature is currently disabled: " + wifiP2pDevice.deviceName);
            return;
        }
        if (handlePreExistingConnection(wifiP2pDevice)) {
            Slog.i(TAG, "already handle the preexisting p2p connection status");
            return;
        }
        this.mDesiredDevice = wifiP2pDevice;
        this.mConnectionRetriesLeft = 3;
        updateConnection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disconnect() {
        this.mDesiredDevice = null;
        updateConnection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void retryConnection() {
        this.mDesiredDevice = new WifiP2pDevice(this.mDesiredDevice);
        updateConnection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateConnection() {
        WifiP2pDevice wifiP2pDevice;
        if (DEBUGV) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            for (int i = 2; i < stackTrace.length && i < 5; i++) {
                Slog.i(TAG, stackTrace[i].toString());
            }
            dump();
        }
        updateScanState();
        if (((this.mRemoteDisplay != null || this.mExtRemoteDisplay != null) && (wifiP2pDevice = this.mConnectedDevice) != null && !wifiP2pDevice.equals(this.mDesiredDevice)) || (this.mRemoteDisplayInterface != null && this.mConnectedDevice == null)) {
            Slog.i(TAG, "Stopped listening for RTSP connection on " + this.mRemoteDisplayInterface);
            this.mOwduhWrapper.getExtImpl().reportWfdConnectionTime(Long.valueOf(System.currentTimeMillis() - this.connectedTime), this.mConnectedDevice, this.mContext);
            this.mOwduhWrapper.getExtImpl().getWfdDisconnected(this.mContext);
            RemoteDisplay remoteDisplay = this.mRemoteDisplay;
            if (remoteDisplay != null) {
                remoteDisplay.dispose();
            } else {
                Object obj = this.mExtRemoteDisplay;
                if (obj != null) {
                    ExtendedRemoteDisplayHelper.dispose(obj);
                }
            }
            this.mExtRemoteDisplay = null;
            this.mRemoteDisplay = null;
            this.mRemoteDisplayInterface = null;
            this.mHandler.removeCallbacks(this.mRtspTimeout);
            this.mWifiP2pManager.setMiracastMode(0);
            unadvertiseDisplay();
        }
        if (this.mRemoteDisplayConnected || this.mDisconnectingDevice != null) {
            return;
        }
        WifiP2pDevice wifiP2pDevice2 = this.mConnectedDevice;
        if (wifiP2pDevice2 != null && !wifiP2pDevice2.equals(this.mDesiredDevice)) {
            Slog.i(TAG, "Disconnecting from Wifi display: " + this.mConnectedDevice.deviceName);
            this.mDisconnectingDevice = this.mConnectedDevice;
            this.mConnectedDevice = null;
            this.mConnectedDeviceGroupInfo = null;
            unadvertiseDisplay();
            final WifiP2pDevice wifiP2pDevice3 = this.mDisconnectingDevice;
            this.mWifiP2pManager.removeGroup(this.mWifiP2pChannel, new WifiP2pManager.ActionListener() { // from class: com.android.server.display.WifiDisplayController.11
                @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                public void onSuccess() {
                    Slog.i(WifiDisplayController.TAG, "Disconnected from Wifi display: " + wifiP2pDevice3.deviceName);
                    next();
                }

                @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                public void onFailure(int i2) {
                    Slog.i(WifiDisplayController.TAG, "Failed to disconnect from Wifi display: " + wifiP2pDevice3.deviceName + ", reason=" + i2);
                    next();
                }

                private void next() {
                    if (WifiDisplayController.this.mDisconnectingDevice == wifiP2pDevice3) {
                        WifiDisplayController.this.mDisconnectingDevice = null;
                        WifiDisplayController.this.updateConnection();
                    }
                }
            });
            return;
        }
        if (this.mCancelingDevice != null) {
            return;
        }
        WifiP2pDevice wifiP2pDevice4 = this.mConnectingDevice;
        if (wifiP2pDevice4 != null && wifiP2pDevice4 != this.mDesiredDevice) {
            Slog.i(TAG, "Canceling connection to Wifi display: " + this.mConnectingDevice.deviceName);
            this.mCancelingDevice = this.mConnectingDevice;
            this.mConnectingDevice = null;
            unadvertiseDisplay();
            this.mHandler.removeCallbacks(this.mConnectionTimeout);
            final WifiP2pDevice wifiP2pDevice5 = this.mCancelingDevice;
            this.mWifiP2pManager.cancelConnect(this.mWifiP2pChannel, new WifiP2pManager.ActionListener() { // from class: com.android.server.display.WifiDisplayController.12
                @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                public void onSuccess() {
                    Slog.i(WifiDisplayController.TAG, "Canceled connection to Wifi display: " + wifiP2pDevice5.deviceName);
                    next();
                }

                @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                public void onFailure(int i2) {
                    Slog.i(WifiDisplayController.TAG, "Failed to cancel connection to Wifi display: " + wifiP2pDevice5.deviceName + ", reason=" + i2);
                    next();
                }

                private void next() {
                    if (WifiDisplayController.this.mCancelingDevice == wifiP2pDevice5) {
                        WifiDisplayController.this.mCancelingDevice = null;
                        WifiDisplayController.this.updateConnection();
                    }
                }
            });
            return;
        }
        final WifiP2pDevice wifiP2pDevice6 = this.mDesiredDevice;
        if (wifiP2pDevice6 == null) {
            if (this.mWifiDisplayCertMode) {
                this.mListener.onDisplaySessionInfo(getSessionInfo(this.mConnectedDeviceGroupInfo, 0));
            }
            unadvertiseDisplay();
            return;
        }
        RemoteDisplay.Listener listener = new RemoteDisplay.Listener() { // from class: com.android.server.display.WifiDisplayController.13
            public void onDisplayConnected(Surface surface, int i2, int i3, int i4, int i5) {
                WifiDisplayController wifiDisplayController = WifiDisplayController.this;
                if (wifiDisplayController.mConnectedDevice == wifiP2pDevice6 && !wifiDisplayController.mRemoteDisplayConnected) {
                    Slog.i(WifiDisplayController.TAG, "Opened RTSP connection with Wifi display: " + WifiDisplayController.this.mConnectedDevice.deviceName);
                    WifiDisplayController.this.mRemoteDisplayConnected = true;
                    WifiDisplayController.this.mHandler.removeCallbacks(WifiDisplayController.this.mRtspTimeout);
                    if (WifiDisplayController.this.mWifiDisplayCertMode) {
                        Listener listener2 = WifiDisplayController.this.mListener;
                        WifiDisplayController wifiDisplayController2 = WifiDisplayController.this;
                        listener2.onDisplaySessionInfo(wifiDisplayController2.getSessionInfo(wifiDisplayController2.mConnectedDeviceGroupInfo, i5));
                    }
                    WifiDisplayController.this.advertiseDisplay(WifiDisplayController.createWifiDisplay(WifiDisplayController.this.mConnectedDevice), surface, i2, i3, i4);
                }
                IOplusWifiDisplayUsageHelperExt extImpl = WifiDisplayController.this.mOwduhWrapper.getExtImpl();
                WifiP2pGroup wifiP2pGroup = WifiDisplayController.this.mConnectedDeviceGroupInfo;
                WifiDisplayController wifiDisplayController3 = WifiDisplayController.this;
                extImpl.wfdConnecteSuceess(wifiP2pGroup, wifiDisplayController3.mConnectedDevice, wifiDisplayController3.mContext);
                WifiDisplayController.this.connectedTime = System.currentTimeMillis();
            }

            public void onDisplayDisconnected() {
                if (WifiDisplayController.this.mConnectedDevice == wifiP2pDevice6) {
                    Slog.i(WifiDisplayController.TAG, "Closed RTSP connection with Wifi display: " + WifiDisplayController.this.mConnectedDevice.deviceName);
                    WifiDisplayController.this.mHandler.removeCallbacks(WifiDisplayController.this.mRtspTimeout);
                    WifiDisplayController.this.mRemoteDisplayConnected = false;
                    WifiDisplayController.this.disconnect();
                }
            }

            public void onDisplayError(int i2) {
                if (WifiDisplayController.this.mConnectedDevice == wifiP2pDevice6) {
                    Slog.i(WifiDisplayController.TAG, "Lost RTSP connection with Wifi display due to error " + i2 + ": " + WifiDisplayController.this.mConnectedDevice.deviceName);
                    WifiDisplayController.this.mHandler.removeCallbacks(WifiDisplayController.this.mRtspTimeout);
                    WifiDisplayController.this.handleConnectionFailure(false);
                    IOplusWifiDisplayUsageHelperExt extImpl = WifiDisplayController.this.mOwduhWrapper.getExtImpl();
                    WifiDisplayController wifiDisplayController = WifiDisplayController.this;
                    extImpl.wfdConnectedFailed("Lost_RTSP_Connection", wifiDisplayController.mConnectedDevice, wifiDisplayController.mConnectedDeviceGroupInfo, WifiDisplayController.this.mContext);
                }
            }
        };
        WifiP2pDevice wifiP2pDevice7 = this.mConnectedDevice;
        if (wifiP2pDevice7 == null && this.mConnectingDevice == null) {
            Slog.i(TAG, "Connecting to Wifi display: " + this.mDesiredDevice.deviceName);
            this.mConnectingDevice = this.mDesiredDevice;
            WifiP2pConfig wifiP2pConfig = new WifiP2pConfig();
            WpsInfo wpsInfo = new WpsInfo();
            int i2 = this.mWifiDisplayWpsConfig;
            if (i2 != 4) {
                wpsInfo.setup = i2;
            } else if (this.mConnectingDevice.wpsPbcSupported()) {
                wpsInfo.setup = 0;
            } else if (this.mConnectingDevice.wpsDisplaySupported()) {
                wpsInfo.setup = 2;
            } else {
                wpsInfo.setup = 1;
            }
            wifiP2pConfig.wps = wpsInfo;
            wifiP2pConfig.deviceAddress = this.mConnectingDevice.deviceAddress;
            WifiP2pConfig generateConfigByGoIntent = this.mWdcWrapper.getExtImpl().generateConfigByGoIntent(wifiP2pConfig, this.mConnectingDevice);
            advertiseDisplay(createWifiDisplay(this.mConnectingDevice), null, 0, 0, 0);
            if (ExtendedRemoteDisplayHelper.isAvailable() && this.mExtRemoteDisplay == null) {
                String str = "255.255.255.255:" + getPortNumber(this.mDesiredDevice);
                this.mRemoteDisplayInterface = str;
                Slog.i(TAG, "Listening for RTSP connection on " + str + " from Wifi display: " + this.mDesiredDevice.deviceName);
                this.mExtRemoteDisplay = ExtendedRemoteDisplayHelper.listen(str, listener, this.mHandler, this.mContext);
            }
            final WifiP2pDevice wifiP2pDevice8 = this.mDesiredDevice;
            this.mWifiP2pManager.connect(this.mWifiP2pChannel, generateConfigByGoIntent, new WifiP2pManager.ActionListener() { // from class: com.android.server.display.WifiDisplayController.14
                @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                public void onSuccess() {
                    Slog.i(WifiDisplayController.TAG, "Initiated connection to Wifi display: " + wifiP2pDevice8.deviceName);
                    WifiDisplayController.this.mHandler.postDelayed(WifiDisplayController.this.mConnectionTimeout, 30000L);
                }

                @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                public void onFailure(int i3) {
                    if (WifiDisplayController.this.mConnectingDevice == wifiP2pDevice8) {
                        Slog.i(WifiDisplayController.TAG, "Failed to initiate connection to Wifi display: " + wifiP2pDevice8.deviceName + ", reason=" + i3);
                        WifiDisplayController wifiDisplayController = WifiDisplayController.this;
                        wifiDisplayController.mConnectingDevice = null;
                        wifiDisplayController.handleConnectionFailure(false);
                        IOplusWifiDisplayUsageHelperExt extImpl = WifiDisplayController.this.mOwduhWrapper.getExtImpl();
                        WifiDisplayController wifiDisplayController2 = WifiDisplayController.this;
                        extImpl.wfdConnectedFailed("P2P_Fail_Connect", wifiDisplayController2.mConnectingDevice, wifiDisplayController2.mConnectedDeviceGroupInfo, WifiDisplayController.this.mContext);
                    }
                }
            });
            return;
        }
        if (wifiP2pDevice7 == null || this.mRemoteDisplay != null) {
            return;
        }
        Inet4Address interfaceAddress = getInterfaceAddress(this.mConnectedDeviceGroupInfo);
        if (interfaceAddress == null) {
            Slog.i(TAG, "Failed to get local interface address for communicating with Wifi display: " + this.mConnectedDevice.deviceName);
            handleConnectionFailure(false);
            this.mOwduhWrapper.getExtImpl().wfdConnectedFailed("P2P_Addr_NULL", this.mConnectedDevice, this.mConnectedDeviceGroupInfo, this.mContext);
            return;
        }
        this.mWifiP2pManager.setMiracastMode(1);
        String str2 = interfaceAddress.getHostAddress() + ":" + getPortNumber(this.mConnectedDevice);
        this.mRemoteDisplayInterface = str2;
        if (!ExtendedRemoteDisplayHelper.isAvailable()) {
            Slog.i(TAG, "Listening for RTSP connection on " + str2 + " from Wifi display: " + this.mConnectedDevice.deviceName);
            this.mRemoteDisplay = RemoteDisplay.listen(str2, listener, this.mHandler, this.mContext.getOpPackageName());
        }
        this.mHandler.postDelayed(this.mRtspTimeout, (this.mWifiDisplayCertMode ? 120 : 30) * 1000);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public WifiDisplaySessionInfo getSessionInfo(WifiP2pGroup wifiP2pGroup, int i) {
        if (wifiP2pGroup == null || wifiP2pGroup.getOwner() == null) {
            return null;
        }
        Inet4Address interfaceAddress = getInterfaceAddress(wifiP2pGroup);
        WifiDisplaySessionInfo wifiDisplaySessionInfo = new WifiDisplaySessionInfo(!wifiP2pGroup.getOwner().deviceAddress.equals(this.mThisDevice.deviceAddress), i, wifiP2pGroup.getOwner().deviceAddress + " " + wifiP2pGroup.getNetworkName(), wifiP2pGroup.getPassphrase(), interfaceAddress != null ? interfaceAddress.getHostAddress() : "");
        if (DEBUG) {
            Slog.d(TAG, wifiDisplaySessionInfo.toString());
        }
        this.mOwduhWrapper.getExtImpl().getSessionInfo(!wifiP2pGroup.getOwner().deviceAddress.equals(this.mThisDevice.deviceAddress), wifiP2pGroup.getOwner().deviceAddress + " " + wifiP2pGroup.getNetworkName(), interfaceAddress != null ? interfaceAddress.getHostAddress() : "", this.mContext);
        return wifiDisplaySessionInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStateChanged(boolean z) {
        this.mWifiP2pEnabled = z;
        if (z) {
            retrieveWifiP2pManagerAndChannel();
        }
        updateWfdEnableState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePeersChanged() {
        requestPeers();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean contains(WifiP2pGroup wifiP2pGroup, WifiP2pDevice wifiP2pDevice) {
        return wifiP2pGroup.getOwner().equals(wifiP2pDevice) || wifiP2pGroup.getClientList().contains(wifiP2pDevice);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleConnectionChanged(NetworkInfo networkInfo) {
        this.mNetworkInfo = networkInfo;
        if (this.mWfdEnabled && networkInfo.isConnected()) {
            if (this.mDesiredDevice != null || this.mWifiDisplayCertMode) {
                this.mWifiP2pManager.requestGroupInfo(this.mWifiP2pChannel, new WifiP2pManager.GroupInfoListener() { // from class: com.android.server.display.WifiDisplayController.15
                    @Override // android.net.wifi.p2p.WifiP2pManager.GroupInfoListener
                    public void onGroupInfoAvailable(WifiP2pGroup wifiP2pGroup) {
                        if (wifiP2pGroup == null) {
                            return;
                        }
                        if (WifiDisplayController.DEBUG) {
                            Slog.d(WifiDisplayController.TAG, "Received group info: " + WifiDisplayController.describeWifiP2pGroup(wifiP2pGroup));
                        }
                        WifiP2pDevice wifiP2pDevice = WifiDisplayController.this.mConnectingDevice;
                        if (wifiP2pDevice != null && !WifiDisplayController.contains(wifiP2pGroup, wifiP2pDevice)) {
                            Slog.i(WifiDisplayController.TAG, "Aborting connection to Wifi display because the current P2P group does not contain the device we expected to find: " + WifiDisplayController.this.mConnectingDevice.deviceName + ", group info was: " + WifiDisplayController.describeWifiP2pGroup(wifiP2pGroup));
                            IOplusWifiDisplayUsageHelperExt extImpl = WifiDisplayController.this.mOwduhWrapper.getExtImpl();
                            WifiDisplayController wifiDisplayController = WifiDisplayController.this;
                            extImpl.wfdConnectedFailed("P2P_Group_Fail", wifiDisplayController.mConnectingDevice, wifiDisplayController.mConnectedDeviceGroupInfo, WifiDisplayController.this.mContext);
                            WifiDisplayController.this.handleConnectionFailure(false);
                            return;
                        }
                        if (WifiDisplayController.this.mDesiredDevice != null && !WifiDisplayController.contains(wifiP2pGroup, WifiDisplayController.this.mDesiredDevice)) {
                            WifiDisplayController.this.disconnect();
                            return;
                        }
                        if (WifiDisplayController.this.mWifiDisplayCertMode) {
                            boolean equals = wifiP2pGroup.getOwner() != null ? wifiP2pGroup.getOwner().deviceAddress.equals(WifiDisplayController.this.mThisDevice.deviceAddress) : false;
                            if (equals && wifiP2pGroup.getClientList().isEmpty()) {
                                WifiDisplayController wifiDisplayController2 = WifiDisplayController.this;
                                wifiDisplayController2.mDesiredDevice = null;
                                wifiDisplayController2.mConnectingDevice = null;
                                WifiDisplayController.this.mConnectedDeviceGroupInfo = wifiP2pGroup;
                                WifiDisplayController.this.updateConnection();
                            } else {
                                WifiDisplayController wifiDisplayController3 = WifiDisplayController.this;
                                if (wifiDisplayController3.mConnectingDevice == null && wifiDisplayController3.mDesiredDevice == null) {
                                    WifiDisplayController wifiDisplayController4 = WifiDisplayController.this;
                                    WifiP2pDevice next = equals ? wifiP2pGroup.getClientList().iterator().next() : wifiP2pGroup.getOwner();
                                    wifiDisplayController4.mDesiredDevice = next;
                                    wifiDisplayController4.mConnectingDevice = next;
                                }
                            }
                        }
                        WifiDisplayController wifiDisplayController5 = WifiDisplayController.this;
                        WifiP2pDevice wifiP2pDevice2 = wifiDisplayController5.mConnectingDevice;
                        if (wifiP2pDevice2 == null || !wifiP2pDevice2.equals(wifiDisplayController5.mDesiredDevice)) {
                            return;
                        }
                        Slog.i(WifiDisplayController.TAG, "Connected to Wifi display: " + WifiDisplayController.this.mConnectingDevice.deviceName);
                        WifiDisplayController.this.mHandler.removeCallbacks(WifiDisplayController.this.mConnectionTimeout);
                        WifiDisplayController.this.mConnectedDeviceGroupInfo = wifiP2pGroup;
                        WifiDisplayController wifiDisplayController6 = WifiDisplayController.this;
                        wifiDisplayController6.mConnectedDevice = wifiDisplayController6.mConnectingDevice;
                        wifiDisplayController6.mConnectingDevice = null;
                        wifiDisplayController6.updateConnection();
                    }
                });
                return;
            }
            return;
        }
        if (networkInfo.isConnectedOrConnecting()) {
            return;
        }
        this.mConnectedDeviceGroupInfo = null;
        if (this.mConnectingDevice != null || this.mConnectedDevice != null) {
            disconnect();
        }
        if (this.mDesiredDevice != null && !this.mWdcSocExt.getRemoveGroupFlag()) {
            Slog.i(TAG, "reconnect new device: " + this.mDesiredDevice.deviceName);
            updateConnection();
            return;
        }
        if (this.mWfdEnabled) {
            requestPeers();
            if (this.mWdcSocExt.getRemoveGroupFlag()) {
                this.mWdcSocExt.setReConnectDevice(this.mDesiredDevice);
            }
            this.mWdcSocExt.checkReConnect();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleConnectionFailure(boolean z) {
        Slog.i(TAG, "Wifi display connection failed!");
        final WifiP2pDevice wifiP2pDevice = this.mDesiredDevice;
        if (wifiP2pDevice != null) {
            if (this.mConnectionRetriesLeft > 0) {
                this.mHandler.postDelayed(new Runnable() { // from class: com.android.server.display.WifiDisplayController.19
                    @Override // java.lang.Runnable
                    public void run() {
                        if (WifiDisplayController.this.mDesiredDevice != wifiP2pDevice || WifiDisplayController.this.mConnectionRetriesLeft <= 0) {
                            return;
                        }
                        WifiDisplayController wifiDisplayController = WifiDisplayController.this;
                        wifiDisplayController.mConnectionRetriesLeft--;
                        Slog.i(WifiDisplayController.TAG, "Retrying Wifi display connection.  Retries left: " + WifiDisplayController.this.mConnectionRetriesLeft);
                        WifiDisplayController.this.retryConnection();
                    }
                }, z ? 0L : 500L);
            } else {
                disconnect();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void advertiseDisplay(final WifiDisplay wifiDisplay, final Surface surface, final int i, final int i2, final int i3) {
        if (Objects.equals(this.mAdvertisedDisplay, wifiDisplay) && this.mAdvertisedDisplaySurface == surface && this.mAdvertisedDisplayWidth == i && this.mAdvertisedDisplayHeight == i2 && this.mAdvertisedDisplayFlags == i3) {
            return;
        }
        final WifiDisplay wifiDisplay2 = this.mAdvertisedDisplay;
        final Surface surface2 = this.mAdvertisedDisplaySurface;
        this.mAdvertisedDisplay = wifiDisplay;
        this.mAdvertisedDisplaySurface = surface;
        this.mAdvertisedDisplayWidth = i;
        this.mAdvertisedDisplayHeight = i2;
        this.mAdvertisedDisplayFlags = i3;
        this.mHandler.post(new Runnable() { // from class: com.android.server.display.WifiDisplayController.20
            @Override // java.lang.Runnable
            public void run() {
                Surface surface3 = surface2;
                if (surface3 != null && surface != surface3) {
                    WifiDisplayController.this.mListener.onDisplayDisconnected();
                    WifiDisplayController.this.mWdcSocExt.setWFD(false);
                } else {
                    WifiDisplay wifiDisplay3 = wifiDisplay2;
                    if (wifiDisplay3 != null && !wifiDisplay3.hasSameAddress(wifiDisplay)) {
                        WifiDisplayController.this.mListener.onDisplayConnectionFailed();
                        WifiDisplayController.this.mWdcSocExt.setWFD(false);
                    }
                }
                WifiDisplay wifiDisplay4 = wifiDisplay;
                if (wifiDisplay4 != null) {
                    if (!wifiDisplay4.hasSameAddress(wifiDisplay2)) {
                        WifiDisplayController.this.mListener.onDisplayConnecting(wifiDisplay);
                    } else if (!wifiDisplay.equals(wifiDisplay2)) {
                        WifiDisplayController.this.mListener.onDisplayChanged(wifiDisplay);
                    }
                    Surface surface4 = surface;
                    if (surface4 == null || surface4 == surface2) {
                        return;
                    }
                    WifiDisplayController.this.mListener.onDisplayConnected(wifiDisplay, surface, i, i2, i3);
                    WifiDisplayController.this.mWdcSocExt.setWFD(true);
                }
            }
        });
    }

    private void unadvertiseDisplay() {
        advertiseDisplay(null, null, 0, 0, 0);
    }

    private void readvertiseDisplay(WifiDisplay wifiDisplay) {
        advertiseDisplay(wifiDisplay, this.mAdvertisedDisplaySurface, this.mAdvertisedDisplayWidth, this.mAdvertisedDisplayHeight, this.mAdvertisedDisplayFlags);
    }

    private boolean handlePreExistingConnection(final WifiP2pDevice wifiP2pDevice) {
        NetworkInfo networkInfo = this.mNetworkInfo;
        if (networkInfo == null || !networkInfo.isConnected() || this.mWifiDisplayCertMode) {
            return false;
        }
        Slog.i(TAG, "handle the preexisting p2p connection status");
        this.mWifiP2pManager.requestGroupInfo(this.mWifiP2pChannel, new WifiP2pManager.GroupInfoListener() { // from class: com.android.server.display.WifiDisplayController.21
            @Override // android.net.wifi.p2p.WifiP2pManager.GroupInfoListener
            public void onGroupInfoAvailable(WifiP2pGroup wifiP2pGroup) {
                if (wifiP2pGroup == null) {
                    return;
                }
                if (WifiDisplayController.contains(wifiP2pGroup, wifiP2pDevice)) {
                    Slog.i(WifiDisplayController.TAG, "already connected to the desired device: " + wifiP2pDevice.deviceName);
                    WifiDisplayController.this.updateConnection();
                    WifiDisplayController wifiDisplayController = WifiDisplayController.this;
                    wifiDisplayController.handleConnectionChanged(wifiDisplayController.mNetworkInfo);
                    return;
                }
                WifiDisplayController.this.mWdcSocExt.setRemoveGroupFlag(true);
                WifiDisplayController.this.mWifiP2pManager.removeGroup(WifiDisplayController.this.mWifiP2pChannel, new WifiP2pManager.ActionListener() { // from class: com.android.server.display.WifiDisplayController.21.1
                    @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                    public void onSuccess() {
                        Slog.i(WifiDisplayController.TAG, "disconnect the old device");
                    }

                    @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                    public void onFailure(int i) {
                        Slog.i(WifiDisplayController.TAG, "Failed to disconnect the old device: reason=" + i);
                    }
                });
            }
        });
        this.mDesiredDevice = wifiP2pDevice;
        this.mConnectionRetriesLeft = 3;
        return true;
    }

    private static Inet4Address getInterfaceAddress(WifiP2pGroup wifiP2pGroup) {
        try {
            Enumeration<InetAddress> inetAddresses = NetworkInterface.getByName(wifiP2pGroup.getInterface()).getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress nextElement = inetAddresses.nextElement();
                if (nextElement instanceof Inet4Address) {
                    return (Inet4Address) nextElement;
                }
            }
            Slog.w(TAG, "Could not obtain address of network interface " + wifiP2pGroup.getInterface() + " because it had no IPv4 addresses.");
            return null;
        } catch (SocketException e) {
            Slog.w(TAG, "Could not obtain address of network interface " + wifiP2pGroup.getInterface(), e);
            return null;
        }
    }

    private static int getPortNumber(WifiP2pDevice wifiP2pDevice) {
        if (wifiP2pDevice.deviceName.startsWith("DIRECT-") && wifiP2pDevice.deviceName.endsWith("Broadcom")) {
            return 8554;
        }
        return DEFAULT_CONTROL_PORT;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isWifiDisplay(WifiP2pDevice wifiP2pDevice) {
        WifiP2pWfdInfo wfdInfo = wifiP2pDevice.getWfdInfo();
        return wfdInfo != null && wfdInfo.isEnabled() && isPrimarySinkDeviceType(wfdInfo.getDeviceType());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String describeWifiP2pDevice(WifiP2pDevice wifiP2pDevice) {
        return wifiP2pDevice != null ? wifiP2pDevice.toString().replace('\n', ',') : "null";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String describeWifiP2pGroup(WifiP2pGroup wifiP2pGroup) {
        return wifiP2pGroup != null ? wifiP2pGroup.toString().replace('\n', ',') : "null";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static WifiDisplay createWifiDisplay(WifiP2pDevice wifiP2pDevice) {
        WifiP2pWfdInfo wfdInfo = wifiP2pDevice.getWfdInfo();
        if (wfdInfo != null) {
            return new WifiDisplay(wifiP2pDevice.deviceAddress, wifiP2pDevice.deviceName, (String) null, true, wfdInfo.isSessionAvailable(), false);
        }
        return new WifiDisplay(wifiP2pDevice.deviceAddress, wifiP2pDevice.deviceName, (String) null, true, false, false);
    }

    public IWifiDisplayControllerWrapper getWrapper() {
        return this.mWdcWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class WifiDisplayControllerWrapper implements IWifiDisplayControllerWrapper {
        private IWifiDisplayControllerExt mWdcExt;

        private WifiDisplayControllerWrapper() {
            this.mWdcExt = (IWifiDisplayControllerExt) ExtLoader.type(IWifiDisplayControllerExt.class).create();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public IWifiDisplayControllerExt getExtImpl() {
            return this.mWdcExt;
        }
    }

    public IOplusWifiDisplayUsageHelperWrapper getOplusWifiDisplayUsageHelperWrapper() {
        return this.mOwduhWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class OplusWifiDisplayUsageHelperWrapper implements IOplusWifiDisplayUsageHelperWrapper {
        private IOplusWifiDisplayUsageHelperExt mOwduhExt;

        private OplusWifiDisplayUsageHelperWrapper() {
            this.mOwduhExt = (IOplusWifiDisplayUsageHelperExt) ExtLoader.type(IOplusWifiDisplayUsageHelperExt.class).create();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public IOplusWifiDisplayUsageHelperExt getExtImpl() {
            return this.mOwduhExt;
        }
    }
}
