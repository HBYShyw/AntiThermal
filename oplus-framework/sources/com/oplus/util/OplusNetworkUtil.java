package com.oplus.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothPan;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes.dex */
public class OplusNetworkUtil {
    public static final int AIRPLANE_MODE_ON_STR = 0;
    private static final String DEFAULT_HTTP_URL = "http://connectivitycheck.gstatic.com/generate_204";
    private static final String KEY_NETWORK_MONITOR_AVAILABLE = "oplus.comm.network.monitor.available";
    private static final String KEY_NETWORK_MONITOR_PORTAL = "oplus.comm.network.monitor.portal";
    private static final String KEY_NETWORK_MONITOR_SSID = "oplus.comm.network.monitor.ssid";
    public static final int MOBILE_AND_WLAN_NETWORK_NOT_CONNECT_STR = 1;
    public static final int NETWORK_CONNECT_OK_STR = -1;
    public static final int NO_NETWORK_CONNECT_STR = 3;
    public static final String TAG = "OplusNetworkUtil";
    public static final int WLAN_NEED_LOGIN_STR = 2;
    private static String mCurrSSID;
    private static BluetoothPan mService;
    private static BluetoothProfile.ServiceListener mProfileServiceListener = new ProfileServiceListener();
    private static boolean mIsBluetoothTetherConnected = false;
    private static int sNetworkId = -1;

    public static boolean isWifiConnected(Context context) {
        WifiInfo wifiInfo;
        ConnectivityManager connect = (ConnectivityManager) context.getSystemService("connectivity");
        if (connect.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            if (wifiManager != null && (wifiInfo = wifiManager.getConnectionInfo()) != null) {
                mCurrSSID = wifiInfo.getSSID();
                sNetworkId = wifiInfo.getNetworkId();
            }
            return true;
        }
        return false;
    }

    public static boolean isMobileDataConnected(Context context) {
        ConnectivityManager connect = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo networkInfo = connect.getNetworkInfo(0);
        if (networkInfo == null || networkInfo.getState() != NetworkInfo.State.CONNECTED) {
            return false;
        }
        return true;
    }

    public static boolean isSimInserted(Context context, int slotId) {
        TelephonyManager telMgr = (TelephonyManager) context.getSystemService("phone");
        return telMgr.hasIccCard();
    }

    /* JADX WARN: Code restructure failed: missing block: B:6:0x000d, code lost:
    
        if (isSimInserted(r4, 1) != false) goto L7;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static boolean hasSimCard(Context context) {
        boolean z = false;
        try {
            if (!isSimInserted(context, 0)) {
            }
            z = true;
            boolean hasSimCard = z;
            return hasSimCard;
        } catch (Exception e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public static void onClickLoginBtn(Context context) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(DEFAULT_HTTP_URL));
        intent.setFlags(272629760);
        context.startActivity(intent);
    }

    public static Boolean isAirplaneMode(Context context) {
        return Boolean.valueOf(Settings.Global.getInt(context.getContentResolver(), "airplane_mode_on", 0) != 0);
    }

    private static int getCaptivePortalStr(Context context, String url) {
        boolean isAvailable = false;
        boolean isPortal = false;
        Log.d(TAG, "current network id = " + sNetworkId);
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        if (cm != null) {
            Network[] networks = cm.getAllNetworks();
            if (networks == null || networks.length == 0) {
                return 3;
            }
            for (Network network : networks) {
                NetworkCapabilities nc = cm.getNetworkCapabilities(network);
                if (nc != null && nc.hasTransport(1)) {
                    if (nc.hasCapability(16)) {
                        isAvailable = true;
                    } else if (nc.hasCapability(17)) {
                        isPortal = true;
                    }
                }
            }
        }
        if (mCurrSSID == null) {
            return 3;
        }
        if (isAvailable) {
            return -1;
        }
        if (!isPortal) {
            return 3;
        }
        return 2;
    }

    public static int getErrorString(Context context, String url) {
        getBluetoothTether(context);
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
        }
        closeProxy();
        if (isAirplaneMode(context).booleanValue() && !isWifiConnected(context) && !mIsBluetoothTetherConnected) {
            return 0;
        }
        if (isWifiConnected(context) || mIsBluetoothTetherConnected) {
            return getCaptivePortalStr(context, url);
        }
        if (hasSimCard(context)) {
            if (isMobileDataConnected(context)) {
                return -1;
            }
            return 1;
        }
        return 3;
    }

    public static void getBluetoothTether(Context context) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            int btState = adapter.getState();
            if (btState == 12) {
                adapter.getProfileProxy(context.getApplicationContext(), mProfileServiceListener, 5);
            }
        }
    }

    /* loaded from: classes.dex */
    private static class ProfileServiceListener implements BluetoothProfile.ServiceListener {
        private ProfileServiceListener() {
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            List<BluetoothDevice> connectedDevicesList;
            AtomicReference<BluetoothPan> sBluetoothPan = new AtomicReference<>();
            sBluetoothPan.set((BluetoothPan) proxy);
            OplusNetworkUtil.mService = (BluetoothPan) proxy;
            BluetoothPan bluetoothPan = sBluetoothPan.get();
            if (bluetoothPan != null && (connectedDevicesList = bluetoothPan.getDevicesMatchingConnectionStates(new int[]{2})) != null && connectedDevicesList.size() > 0) {
                OplusNetworkUtil.mIsBluetoothTetherConnected = true;
            }
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int profile) {
            OplusNetworkUtil.mIsBluetoothTetherConnected = false;
        }
    }

    private static void closeProxy() {
        if (mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(5, mService);
                mService = null;
            } catch (Throwable t) {
                Log.e(TAG, "Error cleaning up PAN proxy", t);
            }
        }
    }
}
