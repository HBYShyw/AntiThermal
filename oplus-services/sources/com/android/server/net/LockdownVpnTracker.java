package com.android.server.net;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.net.VpnConfig;
import com.android.internal.net.VpnProfile;
import com.android.server.connectivity.Vpn;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class LockdownVpnTracker {
    public static final String ACTION_LOCKDOWN_RESET = "com.android.server.action.LOCKDOWN_RESET";
    private static final String TAG = "LockdownVpnTracker";
    private String mAcceptedEgressIface;
    private final ConnectivityManager mCm;
    private final PendingIntent mConfigIntent;
    private final Context mContext;
    private final Handler mHandler;
    private final NotificationManager mNotificationManager;
    private final VpnProfile mProfile;
    private final PendingIntent mResetIntent;
    private final Vpn mVpn;
    private final Object mStateLock = new Object();
    private final NetworkCallback mDefaultNetworkCallback = new NetworkCallback();
    private final VpnNetworkCallback mVpnNetworkCallback = new VpnNetworkCallback();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class NetworkCallback extends ConnectivityManager.NetworkCallback {
        private LinkProperties mLinkProperties;
        private Network mNetwork;

        private NetworkCallback() {
            this.mNetwork = null;
            this.mLinkProperties = null;
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
            boolean z;
            if (network.equals(this.mNetwork)) {
                z = false;
            } else {
                this.mNetwork = network;
                z = true;
            }
            this.mLinkProperties = linkProperties;
            if (z) {
                synchronized (LockdownVpnTracker.this.mStateLock) {
                    LockdownVpnTracker.this.handleStateChangedLocked();
                }
            }
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            this.mNetwork = null;
            this.mLinkProperties = null;
            synchronized (LockdownVpnTracker.this.mStateLock) {
                LockdownVpnTracker.this.handleStateChangedLocked();
            }
        }

        public Network getNetwork() {
            return this.mNetwork;
        }

        public LinkProperties getLinkProperties() {
            return this.mLinkProperties;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class VpnNetworkCallback extends NetworkCallback {
        private VpnNetworkCallback() {
            super();
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(Network network) {
            synchronized (LockdownVpnTracker.this.mStateLock) {
                LockdownVpnTracker.this.handleStateChangedLocked();
            }
        }

        @Override // com.android.server.net.LockdownVpnTracker.NetworkCallback, android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            onAvailable(network);
        }
    }

    public LockdownVpnTracker(Context context, Handler handler, Vpn vpn, VpnProfile vpnProfile) {
        Objects.requireNonNull(context);
        this.mContext = context;
        this.mCm = (ConnectivityManager) context.getSystemService(ConnectivityManager.class);
        Objects.requireNonNull(handler);
        this.mHandler = handler;
        Objects.requireNonNull(vpn);
        this.mVpn = vpn;
        Objects.requireNonNull(vpnProfile);
        this.mProfile = vpnProfile;
        this.mNotificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
        this.mConfigIntent = PendingIntent.getActivity(context, 0, new Intent("android.settings.VPN_SETTINGS"), 67108864);
        Intent intent = new Intent(ACTION_LOCKDOWN_RESET);
        intent.addFlags(1073741824);
        this.mResetIntent = PendingIntent.getBroadcast(context, 0, intent, 67108864);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStateChangedLocked() {
        Network network = this.mDefaultNetworkCallback.getNetwork();
        LinkProperties linkProperties = this.mDefaultNetworkCallback.getLinkProperties();
        NetworkInfo networkInfo = this.mVpn.getNetworkInfo();
        VpnConfig legacyVpnConfig = this.mVpn.getLegacyVpnConfig();
        boolean z = true;
        boolean z2 = network == null;
        if (linkProperties != null && TextUtils.equals(this.mAcceptedEgressIface, linkProperties.getInterfaceName())) {
            z = false;
        }
        String interfaceName = linkProperties == null ? null : linkProperties.getInterfaceName();
        Log.d(TAG, "handleStateChanged: egress=" + this.mAcceptedEgressIface + "->" + interfaceName);
        if (z2 || z) {
            this.mAcceptedEgressIface = null;
            this.mVpn.stopVpnRunnerPrivileged();
        }
        if (z2) {
            hideNotification();
            return;
        }
        if (!networkInfo.isConnectedOrConnecting()) {
            if (!this.mProfile.isValidLockdownProfile()) {
                Log.e(TAG, "Invalid VPN profile; requires IP-based server and DNS");
                showNotification(17041844, 17303920);
                return;
            }
            Log.d(TAG, "Active network connected; starting VPN");
            showNotification(17041842, 17303920);
            this.mAcceptedEgressIface = interfaceName;
            try {
                this.mVpn.startLegacyVpnPrivileged(this.mProfile, network, linkProperties);
                return;
            } catch (IllegalStateException e) {
                this.mAcceptedEgressIface = null;
                Log.e(TAG, "Failed to start VPN", e);
                showNotification(17041844, 17303920);
                return;
            }
        }
        if (!networkInfo.isConnected() || legacyVpnConfig == null) {
            return;
        }
        Log.d(TAG, "VPN connected using iface=" + legacyVpnConfig.interfaze + ", sourceAddr=" + legacyVpnConfig.addresses.toString());
        showNotification(17041841, 17303919);
    }

    public void init() {
        synchronized (this.mStateLock) {
            initLocked();
        }
    }

    private void initLocked() {
        Log.d(TAG, "initLocked()");
        this.mVpn.setEnableTeardown(false);
        this.mVpn.setLockdown(true);
        this.mCm.setLegacyLockdownVpnEnabled(true);
        handleStateChangedLocked();
        this.mCm.registerSystemDefaultNetworkCallback(this.mDefaultNetworkCallback, this.mHandler);
        this.mCm.registerNetworkCallback(new NetworkRequest.Builder().clearCapabilities().addTransportType(4).build(), this.mVpnNetworkCallback, this.mHandler);
    }

    public void shutdown() {
        synchronized (this.mStateLock) {
            shutdownLocked();
        }
    }

    private void shutdownLocked() {
        Log.d(TAG, "shutdownLocked()");
        this.mAcceptedEgressIface = null;
        this.mVpn.stopVpnRunnerPrivileged();
        this.mVpn.setLockdown(false);
        this.mCm.setLegacyLockdownVpnEnabled(false);
        hideNotification();
        this.mVpn.setEnableTeardown(true);
        this.mCm.unregisterNetworkCallback(this.mDefaultNetworkCallback);
        this.mCm.unregisterNetworkCallback(this.mVpnNetworkCallback);
    }

    public void reset() {
        Log.d(TAG, "reset()");
        synchronized (this.mStateLock) {
            shutdownLocked();
            initLocked();
            handleStateChangedLocked();
        }
    }

    private void showNotification(int i, int i2) {
        this.mNotificationManager.notify(null, 20, new Notification.Builder(this.mContext, "VPN").setWhen(0L).setSmallIcon(i2).setContentTitle(this.mContext.getString(i)).setContentText(this.mContext.getString(17041840)).setContentIntent(this.mConfigIntent).setOngoing(true).addAction(R.drawable.ic_perm_group_messages, this.mContext.getString(R.string.zen_mode_alarm), this.mResetIntent).setColor(this.mContext.getColor(R.color.system_notification_accent_color)).build());
    }

    private void hideNotification() {
        this.mNotificationManager.cancel(null, 20);
    }
}
