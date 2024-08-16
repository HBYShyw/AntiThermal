package com.oplus.wrapper.net;

import android.net.ConnectivityManager;
import android.os.Handler;

/* loaded from: classes.dex */
public class ConnectivityManager {
    public static final int TETHERING_WIFI = getTetheringWifi();
    private final android.net.ConnectivityManager mConnectivityManager;

    public ConnectivityManager(android.net.ConnectivityManager connectivityManager) {
        this.mConnectivityManager = connectivityManager;
    }

    public void startTethering(int type, boolean showProvisioningUi, OnStartTetheringCallback callback) {
        this.mConnectivityManager.startTethering(type, showProvisioningUi, callback.getCallBack());
    }

    public void startTethering(int type, boolean showProvisioningUi, OnStartTetheringCallback callback, Handler handler) {
        this.mConnectivityManager.startTethering(type, showProvisioningUi, callback.getCallBack(), handler);
    }

    public void stopTethering(int type) {
        this.mConnectivityManager.stopTethering(type);
    }

    /* loaded from: classes.dex */
    public static abstract class OnStartTetheringCallback {
        private final ConnectivityManager.OnStartTetheringCallback mOnStartTetheringCallback = new ConnectivityManager.OnStartTetheringCallback() { // from class: com.oplus.wrapper.net.ConnectivityManager.OnStartTetheringCallback.1
            public void onTetheringStarted() {
                OnStartTetheringCallback.this.onTetheringStarted();
            }

            public void onTetheringFailed() {
                OnStartTetheringCallback.this.onTetheringFailed();
            }
        };

        ConnectivityManager.OnStartTetheringCallback getCallBack() {
            return this.mOnStartTetheringCallback;
        }

        public void onTetheringStarted() {
        }

        public void onTetheringFailed() {
        }
    }

    private static int getTetheringWifi() {
        return 0;
    }
}
