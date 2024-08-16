package com.oplus.wrapper.net.wifi;

import android.net.wifi.WifiManager;
import com.oplus.wrapper.net.wifi.WifiManager;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/* loaded from: classes.dex */
public class WifiManager {
    private final Map<ActionListener, WifiManager.ActionListener> mListenerMap = new ConcurrentHashMap();
    private final android.net.wifi.WifiManager mWifiManager;
    public static final String EXTRA_WIFI_AP_STATE = getExtraWifiApState();
    public static final int WIFI_AP_STATE_FAILED = getWifiApStateFailed();
    public static final int WIFI_AP_STATE_ENABLED = getWifiApStateEnabled();

    /* loaded from: classes.dex */
    public interface ActionListener {
        void onFailure(int i);

        void onSuccess();
    }

    private static String getExtraWifiApState() {
        return "wifi_state";
    }

    private static int getWifiApStateFailed() {
        return 14;
    }

    private static int getWifiApStateEnabled() {
        return 13;
    }

    public WifiManager(android.net.wifi.WifiManager wifiManager) {
        this.mWifiManager = wifiManager;
    }

    public void connect(int networkId, ActionListener listener) {
        WifiManager.ActionListener actionListener = getActionListener(listener);
        this.mWifiManager.connect(networkId, actionListener);
    }

    public void connect(android.net.wifi.WifiConfiguration config, ActionListener listener) {
        WifiManager.ActionListener actionListener = getActionListener(listener);
        this.mWifiManager.connect(config, actionListener);
    }

    private WifiManager.ActionListener getActionListener(final ActionListener wrapperActionListener) {
        if (wrapperActionListener == null) {
            return null;
        }
        return this.mListenerMap.computeIfAbsent(wrapperActionListener, new Function() { // from class: com.oplus.wrapper.net.wifi.WifiManager$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                WifiManager.ActionListener lambda$getActionListener$0;
                lambda$getActionListener$0 = WifiManager.this.lambda$getActionListener$0(wrapperActionListener, (WifiManager.ActionListener) obj);
                return lambda$getActionListener$0;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ WifiManager.ActionListener lambda$getActionListener$0(final ActionListener wrapperActionListener, ActionListener actionListener) {
        return new WifiManager.ActionListener() { // from class: com.oplus.wrapper.net.wifi.WifiManager.1
            public void onSuccess() {
                wrapperActionListener.onSuccess();
            }

            public void onFailure(int reason) {
                wrapperActionListener.onFailure(reason);
            }
        };
    }

    public void forget(int netId, ActionListener listener) {
        WifiManager.ActionListener actionListener = getActionListener(listener);
        this.mWifiManager.forget(netId, actionListener);
    }

    public boolean isWifiApEnabled() {
        return this.mWifiManager.isWifiApEnabled();
    }

    public int getWifiApState() {
        return this.mWifiManager.getWifiApState();
    }

    public boolean setSoftApConfiguration(android.net.wifi.SoftApConfiguration softApConfig) {
        return this.mWifiManager.setSoftApConfiguration(softApConfig);
    }

    public android.net.wifi.SoftApConfiguration getSoftApConfiguration() {
        return this.mWifiManager.getSoftApConfiguration();
    }

    public byte[] retrieveSoftApBackupData() {
        return this.mWifiManager.retrieveSoftApBackupData();
    }

    public byte[] retrieveBackupData() {
        return this.mWifiManager.retrieveBackupData();
    }

    public void restoreSupplicantBackupData(byte[] supplicantData, byte[] ipConfigData) {
        this.mWifiManager.restoreSupplicantBackupData(supplicantData, ipConfigData);
    }

    public android.net.wifi.SoftApConfiguration restoreSoftApBackupData(byte[] data) {
        return this.mWifiManager.restoreSoftApBackupData(data);
    }

    public void restoreBackupData(byte[] data) {
        this.mWifiManager.restoreBackupData(data);
    }

    public List<android.net.wifi.WifiConfiguration> getPrivilegedConfiguredNetworks() {
        return this.mWifiManager.getPrivilegedConfiguredNetworks();
    }

    public android.net.wifi.WifiConfiguration getWifiApConfiguration() {
        return this.mWifiManager.getWifiApConfiguration();
    }

    public void setScanAlwaysAvailable(boolean isAvailable) {
        this.mWifiManager.setScanAlwaysAvailable(isAvailable);
    }
}
