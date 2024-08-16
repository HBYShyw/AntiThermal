package com.mediatek.server.display;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Handler;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Slog;
import android.widget.Toast;
import com.android.server.display.WifiDisplayController;
import com.mediatek.server.MtkSystemServiceFactory;
import com.mediatek.server.powerhal.PowerHalManager;
import java.lang.reflect.Field;
import java.util.Iterator;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class MtkWifiDisplayController {
    private static final int CONNECTION_TIMEOUT_SECONDS = 30;
    private static boolean DEBUG = true;
    private static final int RECONNECT_RETRY_DELAY_MILLIS = 1000;
    private static final String TAG = "MtkWifiDisplayController";
    private static final long WIFI_SCAN_TIMER = 100000;
    private static final String goIntent = SystemProperties.get("wfd.source.go_intent", String.valueOf(14));
    private int WFDCONTROLLER_DISPLAY_POWER_SAVING_DELAY;
    private int WFDCONTROLLER_DISPLAY_POWER_SAVING_OPTION;
    private AlarmManager mAlarmManager;
    private final Context mContext;
    private WifiDisplayController mController;
    private final Handler mHandler;
    private WifiP2pDevice mReConnectDevice;
    private int mReConnection_Timeout_Remain_Seconds;
    private final BroadcastReceiver mWifiReceiver;
    private PowerHalManager mPowerHalManager = MtkSystemServiceFactory.getInstance().makePowerHalManager();
    private boolean mRemoveGroupFlag = false;
    private final Runnable mReConnect = new Runnable() { // from class: com.mediatek.server.display.MtkWifiDisplayController.1
        @Override // java.lang.Runnable
        public void run() {
            if (MtkWifiDisplayController.this.mReConnectDevice == null) {
                Slog.d(MtkWifiDisplayController.TAG, "WFD connect failed, stop scan.");
                return;
            }
            Iterator it = MtkWifiDisplayController.this.mController.mAvailableWifiDisplayPeers.iterator();
            while (it.hasNext()) {
                WifiP2pDevice wifiP2pDevice = (WifiP2pDevice) it.next();
                if (MtkWifiDisplayController.DEBUG) {
                    Slog.d(MtkWifiDisplayController.TAG, "\t" + MtkWifiDisplayController.describeWifiP2pDevice(wifiP2pDevice));
                }
                if (wifiP2pDevice.deviceAddress.equals(MtkWifiDisplayController.this.mReConnectDevice.deviceAddress)) {
                    Slog.i(MtkWifiDisplayController.TAG, "connect() in mReConnect. Set mReConnecting as true");
                    MtkWifiDisplayController.this.mReConnectDevice = null;
                    MtkWifiDisplayController.this.mController.requestConnect(wifiP2pDevice.deviceAddress);
                    return;
                }
            }
            MtkWifiDisplayController mtkWifiDisplayController = MtkWifiDisplayController.this;
            mtkWifiDisplayController.mReConnection_Timeout_Remain_Seconds--;
            if (MtkWifiDisplayController.this.mReConnection_Timeout_Remain_Seconds > 0) {
                Slog.i(MtkWifiDisplayController.TAG, "post mReconnect, s:" + MtkWifiDisplayController.this.mReConnection_Timeout_Remain_Seconds);
                MtkWifiDisplayController.this.mHandler.postDelayed(MtkWifiDisplayController.this.mReConnect, 1000L);
                return;
            }
            Slog.e(MtkWifiDisplayController.TAG, "reconnect timeout!");
            Toast.makeText(MtkWifiDisplayController.this.mContext, MtkWifiDisplayController.this.getMtkStringResourceId("wifi_display_disconnected"), 0).show();
            MtkWifiDisplayController.this.mReConnectDevice = null;
            MtkWifiDisplayController.this.mReConnection_Timeout_Remain_Seconds = 0;
            MtkWifiDisplayController.this.mHandler.removeCallbacks(MtkWifiDisplayController.this.mReConnect);
        }
    };
    private final AlarmManager.OnAlarmListener mWifiScanTimerListener = new AlarmManager.OnAlarmListener() { // from class: com.mediatek.server.display.MtkWifiDisplayController.2
        @Override // android.app.AlarmManager.OnAlarmListener
        public void onAlarm() {
            Slog.i(MtkWifiDisplayController.TAG, "Stop WiFi scan/reconnect due to scan timer timeout");
            MtkWifiDisplayController.this.stopWifiScan(true);
        }
    };
    public boolean mStopWifiScan = false;

    public MtkWifiDisplayController(Context context, Handler handler, WifiDisplayController wifiDisplayController) {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.mediatek.server.display.MtkWifiDisplayController.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (intent.getAction().equals("android.net.wifi.STATE_CHANGE")) {
                    NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                    if (MtkWifiDisplayController.this.mController.mConnectedDevice != null) {
                        NetworkInfo.State state = networkInfo.getState();
                        if (state == NetworkInfo.State.DISCONNECTED && MtkWifiDisplayController.this.mStopWifiScan) {
                            Slog.i(MtkWifiDisplayController.TAG, "Resume WiFi scan/reconnect if WiFi is disconnected");
                            MtkWifiDisplayController.this.stopWifiScan(false);
                            MtkWifiDisplayController.this.mAlarmManager.cancel(MtkWifiDisplayController.this.mWifiScanTimerListener);
                            MtkWifiDisplayController.this.mAlarmManager.set(2, MtkWifiDisplayController.WIFI_SCAN_TIMER + SystemClock.elapsedRealtime(), "Set WiFi scan timer", MtkWifiDisplayController.this.mWifiScanTimerListener, MtkWifiDisplayController.this.mHandler);
                            return;
                        }
                        if (state != NetworkInfo.State.CONNECTED || MtkWifiDisplayController.this.mStopWifiScan) {
                            return;
                        }
                        Slog.i(MtkWifiDisplayController.TAG, "Stop WiFi scan/reconnect if WiFi is connected");
                        MtkWifiDisplayController.this.mAlarmManager.cancel(MtkWifiDisplayController.this.mWifiScanTimerListener);
                        MtkWifiDisplayController.this.stopWifiScan(true);
                    }
                }
            }
        };
        this.mWifiReceiver = broadcastReceiver;
        this.mContext = context;
        this.mHandler = handler;
        this.mController = wifiDisplayController;
        registerEMObserver();
        this.mAlarmManager = (AlarmManager) context.getSystemService("alarm");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        context.registerReceiver(broadcastReceiver, intentFilter, null, handler);
    }

    public WifiP2pConfig overWriteConfig(WifiP2pConfig wifiP2pConfig) {
        WifiP2pConfig wifiP2pConfig2 = new WifiP2pConfig(wifiP2pConfig);
        Slog.i(TAG, "oldConfig:" + wifiP2pConfig);
        wifiP2pConfig2.groupOwnerIntent = Integer.valueOf(goIntent).intValue();
        Slog.i(TAG, "config:" + wifiP2pConfig2);
        stopWifiScan(true);
        return wifiP2pConfig2;
    }

    public void setWFD(boolean z) {
        Slog.d(TAG, "setWFD(), enable: " + z);
        this.mPowerHalManager.setWFD(z);
        stopWifiScan(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String describeWifiP2pDevice(WifiP2pDevice wifiP2pDevice) {
        return wifiP2pDevice != null ? wifiP2pDevice.toString().replace('\n', ',') : "null";
    }

    public void checkReConnect() {
        if (this.mReConnectDevice != null) {
            Slog.i(TAG, "requestStartScan() for resolution change.");
            this.mController.requestStartScan();
            this.mReConnection_Timeout_Remain_Seconds = 30;
            this.mHandler.postDelayed(this.mReConnect, 1000L);
        }
    }

    private void registerEMObserver() {
        this.WFDCONTROLLER_DISPLAY_POWER_SAVING_OPTION = this.mContext.getResources().getInteger(getMtkIntegerResourceId("wfd_display_power_saving_option", 1));
        this.WFDCONTROLLER_DISPLAY_POWER_SAVING_DELAY = this.mContext.getResources().getInteger(getMtkIntegerResourceId("wfd_display_power_saving_delay", 10));
        Slog.d(TAG, "registerObserver() ps:" + this.WFDCONTROLLER_DISPLAY_POWER_SAVING_OPTION + ",psd:" + this.WFDCONTROLLER_DISPLAY_POWER_SAVING_DELAY);
        Settings.Global.putInt(this.mContext.getContentResolver(), getMtkSettingsExtGlobalSetting("WIFI_DISPLAY_POWER_SAVING_OPTION"), this.WFDCONTROLLER_DISPLAY_POWER_SAVING_OPTION);
        Settings.Global.putInt(this.mContext.getContentResolver(), getMtkSettingsExtGlobalSetting("WIFI_DISPLAY_POWER_SAVING_DELAY"), this.WFDCONTROLLER_DISPLAY_POWER_SAVING_DELAY);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getMtkStringResourceId(String str) {
        try {
            Field field = Class.forName("com.mediatek.internal.R$string", false, ClassLoader.getSystemClassLoader()).getField(str);
            field.setAccessible(true);
            return field.getInt(null);
        } catch (Exception e) {
            Slog.e(TAG, "Cannot get MTK resource - " + e);
            return 0;
        }
    }

    private String getMtkSettingsExtGlobalSetting(String str) {
        try {
            Class<?> cls = Class.forName("com.mediatek.provider.MtkSettingsExt$Global", false, ClassLoader.getSystemClassLoader());
            Field field = cls.getField(str);
            field.setAccessible(true);
            return (String) field.get(cls);
        } catch (Exception e) {
            Slog.e(TAG, "Cannot get MTK settings - " + e);
            return "";
        }
    }

    private int getMtkIntegerResourceId(String str, int i) {
        try {
            Field field = Class.forName("com.mediatek.internal.R$integer", false, ClassLoader.getSystemClassLoader()).getField(str);
            field.setAccessible(true);
            return field.getInt(null);
        } catch (Exception e) {
            Slog.e(TAG, "Cannot get MTK resource - " + e);
            return i;
        }
    }

    public void stopWifiScan(boolean z) {
        if (!z) {
            this.mAlarmManager.cancel(this.mWifiScanTimerListener);
        }
        if (this.mStopWifiScan != z) {
            Slog.i(TAG, "stopWifiScan()," + z);
            ((WifiManager) this.mContext.getSystemService("wifi")).allowAutojoinGlobal(z ^ true);
            this.mStopWifiScan = z;
        }
    }

    public void setRemoveGroupFlag(boolean z) {
        Slog.i(TAG, "set remove group flag");
        this.mRemoveGroupFlag = z;
    }

    public boolean getRemoveGroupFlag() {
        return this.mRemoveGroupFlag;
    }

    public void setReConnectDevice(WifiP2pDevice wifiP2pDevice) {
        if (this.mRemoveGroupFlag) {
            this.mReConnectDevice = wifiP2pDevice;
        }
    }
}
