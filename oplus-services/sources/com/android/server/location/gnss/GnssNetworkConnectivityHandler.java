package com.android.server.location.gnss;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.telephony.PhoneStateListener;
import android.telephony.PreciseCallState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.location.GpsNetInitiatedHandler;
import com.android.server.location.common.OplusLbsFactory;
import com.android.server.location.interfaces.IOplusConfigListener;
import com.android.server.location.interfaces.IOplusLBSMainClass;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class GnssNetworkConnectivityHandler {
    private static final int AGNSS_NET_CAPABILITY_NOT_METERED = 1;
    private static final int AGNSS_NET_CAPABILITY_NOT_ROAMING = 2;
    private static final int AGPS_DATA_CONNECTION_CLOSED = 0;
    private static final int AGPS_DATA_CONNECTION_OPEN = 2;
    private static final int AGPS_DATA_CONNECTION_OPENING = 1;
    public static final int AGPS_TYPE_C2K = 2;
    private static final int AGPS_TYPE_EIMS = 3;
    private static final int AGPS_TYPE_IMS = 4;
    public static final int AGPS_TYPE_SUPL = 1;
    private static final int APN_INVALID = 0;
    private static final int APN_IPV4 = 1;
    private static final int APN_IPV4V6 = 3;
    private static final int APN_IPV6 = 2;
    private static final int GPS_AGPS_DATA_CONNECTED = 3;
    private static final int GPS_AGPS_DATA_CONN_DONE = 4;
    private static final int GPS_AGPS_DATA_CONN_FAILED = 5;
    private static final int GPS_RELEASE_AGPS_DATA_CONN = 2;
    private static final int GPS_REQUEST_AGPS_DATA_CONN = 1;
    private static final int HASH_MAP_INITIAL_CAPACITY_TO_TRACK_CONNECTED_NETWORKS = 5;
    private static final int SUPL_NETWORK_REQUEST_TIMEOUT_MILLIS = 20000;
    static final String TAG = "GnssNetworkConnectivityHandler";
    private static final String WAKELOCK_KEY = "GnssNetworkConnectivityHandler";
    private static final long WAKELOCK_TIMEOUT_MILLIS = 60000;
    private InetAddress mAGpsDataConnectionIpAddr;
    private int mAGpsDataConnectionState;
    private int mAGpsType;
    private final ConnectivityManager mConnMgr;
    private final Context mContext;
    private final GnssNetworkListener mGnssNetworkListener;
    private final Handler mHandler;
    private ConnectivityManager.NetworkCallback mNetworkConnectivityCallback;
    private final GpsNetInitiatedHandler mNiHandler;
    private final SubscriptionManager.OnSubscriptionsChangedListener mOnSubscriptionsChangeListener;
    private HashMap<Integer, SubIdPhoneStateListener> mPhoneStateListeners;
    private ConnectivityManager.NetworkCallback mSuplConnectivityCallback;
    private final PowerManager.WakeLock mWakeLock;
    private static boolean DEBUG = Log.isLoggable("GnssNetworkConnectivityHandler", 3);
    private static boolean VERBOSE = Log.isLoggable("GnssNetworkConnectivityHandler", 2);
    private HashMap<Network, NetworkAttributes> mAvailableNetworkAttributes = new HashMap<>(5);
    private int mActiveSubId = -1;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface GnssNetworkListener {
        void onNetworkAvailable();
    }

    private native void native_agps_data_conn_closed();

    private native void native_agps_data_conn_failed();

    private native void native_agps_data_conn_open(long j, String str, int i);

    private static native boolean native_is_agps_ril_supported();

    private native void native_update_network_state(boolean z, int i, boolean z2, boolean z3, String str, long j, short s);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class NetworkAttributes {
        private String mApn;
        private NetworkCapabilities mCapabilities;
        private int mType;

        private NetworkAttributes() {
            this.mType = -1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean hasCapabilitiesChanged(NetworkCapabilities networkCapabilities, NetworkCapabilities networkCapabilities2) {
            return networkCapabilities == null || networkCapabilities2 == null || hasCapabilityChanged(networkCapabilities, networkCapabilities2, 18) || hasCapabilityChanged(networkCapabilities, networkCapabilities2, 11);
        }

        private static boolean hasCapabilityChanged(NetworkCapabilities networkCapabilities, NetworkCapabilities networkCapabilities2, int i) {
            return networkCapabilities.hasCapability(i) != networkCapabilities2.hasCapability(i);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static short getCapabilityFlags(NetworkCapabilities networkCapabilities) {
            short s = networkCapabilities.hasCapability(18) ? (short) 2 : (short) 0;
            return networkCapabilities.hasCapability(11) ? (short) (s | 1) : s;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public GnssNetworkConnectivityHandler(Context context, GnssNetworkListener gnssNetworkListener, Looper looper, GpsNetInitiatedHandler gpsNetInitiatedHandler) {
        SubscriptionManager.OnSubscriptionsChangedListener onSubscriptionsChangedListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.server.location.gnss.GnssNetworkConnectivityHandler.2
            @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
            public void onSubscriptionsChanged() {
                TelephonyManager createForSubscriptionId;
                if (GnssNetworkConnectivityHandler.this.mPhoneStateListeners == null) {
                    GnssNetworkConnectivityHandler.this.mPhoneStateListeners = new HashMap(2, 1.0f);
                }
                SubscriptionManager subscriptionManager = (SubscriptionManager) GnssNetworkConnectivityHandler.this.mContext.getSystemService(SubscriptionManager.class);
                TelephonyManager telephonyManager = (TelephonyManager) GnssNetworkConnectivityHandler.this.mContext.getSystemService(TelephonyManager.class);
                if (subscriptionManager == null || telephonyManager == null) {
                    return;
                }
                List<SubscriptionInfo> activeSubscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
                HashSet hashSet = new HashSet();
                if (activeSubscriptionInfoList != null) {
                    if (GnssNetworkConnectivityHandler.DEBUG) {
                        Log.d("GnssNetworkConnectivityHandler", "Active Sub List size: " + activeSubscriptionInfoList.size());
                    }
                    for (SubscriptionInfo subscriptionInfo : activeSubscriptionInfoList) {
                        hashSet.add(Integer.valueOf(subscriptionInfo.getSubscriptionId()));
                        if (!GnssNetworkConnectivityHandler.this.mPhoneStateListeners.containsKey(Integer.valueOf(subscriptionInfo.getSubscriptionId())) && (createForSubscriptionId = telephonyManager.createForSubscriptionId(subscriptionInfo.getSubscriptionId())) != null) {
                            if (GnssNetworkConnectivityHandler.DEBUG) {
                                Log.d("GnssNetworkConnectivityHandler", "Listener sub" + subscriptionInfo.getSubscriptionId());
                            }
                            SubIdPhoneStateListener subIdPhoneStateListener = new SubIdPhoneStateListener(Integer.valueOf(subscriptionInfo.getSubscriptionId()));
                            GnssNetworkConnectivityHandler.this.mPhoneStateListeners.put(Integer.valueOf(subscriptionInfo.getSubscriptionId()), subIdPhoneStateListener);
                            createForSubscriptionId.listen(subIdPhoneStateListener, 2048);
                        }
                    }
                }
                Iterator it = GnssNetworkConnectivityHandler.this.mPhoneStateListeners.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    if (!hashSet.contains(entry.getKey())) {
                        TelephonyManager createForSubscriptionId2 = telephonyManager.createForSubscriptionId(((Integer) entry.getKey()).intValue());
                        if (createForSubscriptionId2 != null) {
                            if (GnssNetworkConnectivityHandler.DEBUG) {
                                Log.d("GnssNetworkConnectivityHandler", "unregister listener sub " + entry.getKey());
                            }
                            createForSubscriptionId2.listen((PhoneStateListener) entry.getValue(), 0);
                            it.remove();
                        } else {
                            Log.e("GnssNetworkConnectivityHandler", "Telephony Manager for Sub " + entry.getKey() + " null");
                        }
                    }
                }
                if (hashSet.contains(Integer.valueOf(GnssNetworkConnectivityHandler.this.mActiveSubId))) {
                    return;
                }
                GnssNetworkConnectivityHandler.this.mActiveSubId = -1;
            }
        };
        this.mOnSubscriptionsChangeListener = onSubscriptionsChangedListener;
        this.mContext = context;
        this.mGnssNetworkListener = gnssNetworkListener;
        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        if (subscriptionManager != null) {
            subscriptionManager.addOnSubscriptionsChangedListener(onSubscriptionsChangedListener);
        }
        this.mWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(1, "GnssNetworkConnectivityHandler");
        this.mHandler = new Handler(looper);
        this.mNiHandler = gpsNetInitiatedHandler;
        this.mConnMgr = (ConnectivityManager) context.getSystemService("connectivity");
        this.mSuplConnectivityCallback = null;
        ((IOplusLBSMainClass) OplusLbsFactory.getInstance().getFeature(IOplusLBSMainClass.DEFAULT, context)).registerLbsConfigListener(new IOplusConfigListener() { // from class: com.android.server.location.gnss.GnssNetworkConnectivityHandler.1
            @Override // com.android.server.location.interfaces.IOplusConfigListener
            public void onDebugLevelChanged(int i) {
                GnssNetworkConnectivityHandler.DEBUG = i > 0;
                GnssNetworkConnectivityHandler.VERBOSE = i > 0;
            }
        });
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class SubIdPhoneStateListener extends PhoneStateListener {
        private Integer mSubId;

        SubIdPhoneStateListener(Integer num) {
            this.mSubId = num;
        }

        public void onPreciseCallStateChanged(PreciseCallState preciseCallState) {
            if (1 == preciseCallState.getForegroundCallState() || 3 == preciseCallState.getForegroundCallState()) {
                GnssNetworkConnectivityHandler.this.mActiveSubId = this.mSubId.intValue();
                if (GnssNetworkConnectivityHandler.DEBUG) {
                    Log.d("GnssNetworkConnectivityHandler", "mActiveSubId: " + GnssNetworkConnectivityHandler.this.mActiveSubId);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerNetworkCallbacks() {
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addCapability(12);
        builder.addCapability(16);
        builder.removeCapability(15);
        NetworkRequest build = builder.build();
        ConnectivityManager.NetworkCallback createNetworkConnectivityCallback = createNetworkConnectivityCallback();
        this.mNetworkConnectivityCallback = createNetworkConnectivityCallback;
        this.mConnMgr.registerNetworkCallback(build, createNetworkConnectivityCallback, this.mHandler);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterNetworkCallbacks() {
        this.mConnMgr.unregisterNetworkCallback(this.mNetworkConnectivityCallback);
        this.mNetworkConnectivityCallback = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDataNetworkConnected() {
        NetworkInfo activeNetworkInfo = this.mConnMgr.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getActiveSubId() {
        return this.mActiveSubId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onReportAGpsStatus(final int i, int i2, final byte[] bArr) {
        if (DEBUG) {
            Log.d("GnssNetworkConnectivityHandler", "AGPS_DATA_CONNECTION: " + agpsDataConnStatusAsString(i2));
        }
        if (i2 == 1) {
            runOnHandler(new Runnable() { // from class: com.android.server.location.gnss.GnssNetworkConnectivityHandler$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    GnssNetworkConnectivityHandler.this.lambda$onReportAGpsStatus$0(i, bArr);
                }
            });
            return;
        }
        if (i2 == 2) {
            runOnHandler(new Runnable() { // from class: com.android.server.location.gnss.GnssNetworkConnectivityHandler$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    GnssNetworkConnectivityHandler.this.lambda$onReportAGpsStatus$1();
                }
            });
            return;
        }
        if (i2 == 3 || i2 == 4 || i2 == 5) {
            return;
        }
        Log.w("GnssNetworkConnectivityHandler", "Received unknown AGPS status: " + i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onReportAGpsStatus$1() {
        handleReleaseSuplConnection(2);
    }

    private ConnectivityManager.NetworkCallback createNetworkConnectivityCallback() {
        return new ConnectivityManager.NetworkCallback() { // from class: com.android.server.location.gnss.GnssNetworkConnectivityHandler.3
            private HashMap<Network, NetworkCapabilities> mAvailableNetworkCapabilities = new HashMap<>(5);

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                if (!NetworkAttributes.hasCapabilitiesChanged(this.mAvailableNetworkCapabilities.get(network), networkCapabilities)) {
                    if (GnssNetworkConnectivityHandler.VERBOSE) {
                        Log.v("GnssNetworkConnectivityHandler", "Relevant network capabilities unchanged. Capabilities: " + networkCapabilities);
                        return;
                    }
                    return;
                }
                this.mAvailableNetworkCapabilities.put(network, networkCapabilities);
                if (GnssNetworkConnectivityHandler.DEBUG) {
                    Log.d("GnssNetworkConnectivityHandler", "Network connected/capabilities updated. Available networks count: " + this.mAvailableNetworkCapabilities.size());
                }
                GnssNetworkConnectivityHandler.this.mGnssNetworkListener.onNetworkAvailable();
                GnssNetworkConnectivityHandler.this.handleUpdateNetworkState(network, true, networkCapabilities);
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLost(Network network) {
                if (this.mAvailableNetworkCapabilities.remove(network) == null) {
                    Log.w("GnssNetworkConnectivityHandler", "Incorrectly received network callback onLost() before onCapabilitiesChanged() for network: " + network);
                    return;
                }
                Log.i("GnssNetworkConnectivityHandler", "Network connection lost. Available networks count: " + this.mAvailableNetworkCapabilities.size());
                GnssNetworkConnectivityHandler.this.handleUpdateNetworkState(network, false, null);
            }
        };
    }

    private ConnectivityManager.NetworkCallback createSuplConnectivityCallback() {
        return new ConnectivityManager.NetworkCallback() { // from class: com.android.server.location.gnss.GnssNetworkConnectivityHandler.4
            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
                if (GnssNetworkConnectivityHandler.DEBUG) {
                    Log.d("GnssNetworkConnectivityHandler", "SUPL network connection available.");
                }
                GnssNetworkConnectivityHandler.this.handleSuplConnectionAvailable(network, linkProperties);
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLost(Network network) {
                Log.i("GnssNetworkConnectivityHandler", "SUPL network connection lost.");
                GnssNetworkConnectivityHandler.this.handleReleaseSuplConnection(2);
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onUnavailable() {
                Log.i("GnssNetworkConnectivityHandler", "SUPL network connection request timed out.");
                GnssNetworkConnectivityHandler.this.handleReleaseSuplConnection(5);
            }
        };
    }

    private void runOnHandler(Runnable runnable) {
        this.mWakeLock.acquire(WAKELOCK_TIMEOUT_MILLIS);
        if (this.mHandler.post(runEventAndReleaseWakeLock(runnable))) {
            return;
        }
        this.mWakeLock.release();
    }

    private Runnable runEventAndReleaseWakeLock(final Runnable runnable) {
        return new Runnable() { // from class: com.android.server.location.gnss.GnssNetworkConnectivityHandler$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                GnssNetworkConnectivityHandler.this.lambda$runEventAndReleaseWakeLock$2(runnable);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runEventAndReleaseWakeLock$2(Runnable runnable) {
        try {
            runnable.run();
        } finally {
            this.mWakeLock.release();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUpdateNetworkState(Network network, boolean z, NetworkCapabilities networkCapabilities) {
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
        boolean z2 = false;
        if (telephonyManager != null && z) {
            try {
                if (telephonyManager.getDataEnabled()) {
                    z2 = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        boolean z3 = z2;
        NetworkAttributes updateTrackedNetworksState = updateTrackedNetworksState(z, network, networkCapabilities);
        String str = updateTrackedNetworksState.mApn;
        int i = updateTrackedNetworksState.mType;
        NetworkCapabilities networkCapabilities2 = updateTrackedNetworksState.mCapabilities;
        Log.i("GnssNetworkConnectivityHandler", String.format("updateNetworkState, state=%s, connected=%s, network=%s, capabilities=%s, availableNetworkCount: %d", agpsDataConnStateAsString(), Boolean.valueOf(z), network, networkCapabilities2, Integer.valueOf(this.mAvailableNetworkAttributes.size())));
        if (native_is_agps_ril_supported()) {
            boolean z4 = !networkCapabilities2.hasTransport(18);
            if (str == null) {
                str = "";
            }
            native_update_network_state(z, i, z4, z3, str, network.getNetworkHandle(), NetworkAttributes.getCapabilityFlags(networkCapabilities2));
            return;
        }
        if (DEBUG) {
            Log.d("GnssNetworkConnectivityHandler", "Skipped network state update because GPS HAL AGPS-RIL is not  supported");
        }
    }

    private NetworkAttributes updateTrackedNetworksState(boolean z, Network network, NetworkCapabilities networkCapabilities) {
        if (!z) {
            return this.mAvailableNetworkAttributes.remove(network);
        }
        NetworkAttributes networkAttributes = this.mAvailableNetworkAttributes.get(network);
        if (networkAttributes != null) {
            networkAttributes.mCapabilities = networkCapabilities;
            return networkAttributes;
        }
        NetworkAttributes networkAttributes2 = new NetworkAttributes();
        networkAttributes2.mCapabilities = networkCapabilities;
        NetworkInfo networkInfo = this.mConnMgr.getNetworkInfo(network);
        if (networkInfo != null) {
            networkAttributes2.mApn = networkInfo.getExtraInfo();
            networkAttributes2.mType = networkInfo.getType();
        }
        this.mAvailableNetworkAttributes.put(network, networkAttributes2);
        return networkAttributes2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSuplConnectionAvailable(Network network, LinkProperties linkProperties) {
        NetworkInfo networkInfo = this.mConnMgr.getNetworkInfo(network);
        String extraInfo = networkInfo != null ? networkInfo.getExtraInfo() : null;
        if (DEBUG) {
            Log.d("GnssNetworkConnectivityHandler", String.format("handleSuplConnectionAvailable: state=%s, suplNetwork=%s, info=%s", agpsDataConnStateAsString(), network, networkInfo));
        }
        if (this.mAGpsDataConnectionState == 1) {
            if (extraInfo == null) {
                extraInfo = "dummy-apn";
            }
            if (this.mAGpsDataConnectionIpAddr != null) {
                setRouting();
            }
            int linkIpType = getLinkIpType(linkProperties);
            if (DEBUG) {
                Log.d("GnssNetworkConnectivityHandler", String.format("native_agps_data_conn_open: mAgpsApn=%s, mApnIpType=%s", extraInfo, Integer.valueOf(linkIpType)));
            }
            native_agps_data_conn_open(network.getNetworkHandle(), extraInfo, linkIpType);
            this.mAGpsDataConnectionState = 2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: handleRequestSuplConnection, reason: merged with bridge method [inline-methods] */
    public void lambda$onReportAGpsStatus$0(int i, byte[] bArr) {
        this.mAGpsDataConnectionIpAddr = null;
        this.mAGpsType = i;
        if (bArr != null) {
            if (VERBOSE) {
                Log.v("GnssNetworkConnectivityHandler", "Received SUPL IP addr[]: " + Arrays.toString(bArr));
            }
            try {
                this.mAGpsDataConnectionIpAddr = InetAddress.getByAddress(bArr);
                if (DEBUG) {
                    Log.d("GnssNetworkConnectivityHandler", "IP address converted to: " + this.mAGpsDataConnectionIpAddr);
                }
            } catch (UnknownHostException e) {
                Log.e("GnssNetworkConnectivityHandler", "Bad IP Address: " + Arrays.toString(bArr), e);
            }
        }
        if (DEBUG) {
            Log.d("GnssNetworkConnectivityHandler", String.format("requestSuplConnection, state=%s, agpsType=%s, address=%s", agpsDataConnStateAsString(), agpsTypeAsString(i), this.mAGpsDataConnectionIpAddr));
        }
        if (this.mAGpsDataConnectionState != 0) {
            return;
        }
        this.mAGpsDataConnectionState = 1;
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addCapability(getNetworkCapability(this.mAGpsType));
        builder.addTransportType(0);
        if (this.mNiHandler.getInEmergency() && this.mActiveSubId >= 0) {
            if (DEBUG) {
                Log.d("GnssNetworkConnectivityHandler", "Adding Network Specifier: " + Integer.toString(this.mActiveSubId));
            }
            builder.setNetworkSpecifier(Integer.toString(this.mActiveSubId));
            builder.removeCapability(13);
        }
        NetworkRequest build = builder.build();
        ConnectivityManager.NetworkCallback networkCallback = this.mSuplConnectivityCallback;
        if (networkCallback != null) {
            this.mConnMgr.unregisterNetworkCallback(networkCallback);
        }
        ConnectivityManager.NetworkCallback createSuplConnectivityCallback = createSuplConnectivityCallback();
        this.mSuplConnectivityCallback = createSuplConnectivityCallback;
        try {
            this.mConnMgr.requestNetwork(build, createSuplConnectivityCallback, this.mHandler, SUPL_NETWORK_REQUEST_TIMEOUT_MILLIS);
        } catch (RuntimeException e2) {
            Log.e("GnssNetworkConnectivityHandler", "Failed to request network.", e2);
            this.mSuplConnectivityCallback = null;
            handleReleaseSuplConnection(5);
        }
    }

    private int getNetworkCapability(int i) {
        if (i == 1 || i == 2) {
            return 1;
        }
        if (i == 3) {
            return 10;
        }
        if (i == 4) {
            return 4;
        }
        throw new IllegalArgumentException("agpsType: " + i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleReleaseSuplConnection(int i) {
        if (DEBUG) {
            Log.d("GnssNetworkConnectivityHandler", String.format("releaseSuplConnection, state=%s, status=%s", agpsDataConnStateAsString(), agpsDataConnStatusAsString(i)));
        }
        if (this.mAGpsDataConnectionState == 0) {
            return;
        }
        this.mAGpsDataConnectionState = 0;
        ConnectivityManager.NetworkCallback networkCallback = this.mSuplConnectivityCallback;
        if (networkCallback != null) {
            this.mConnMgr.unregisterNetworkCallback(networkCallback);
            this.mSuplConnectivityCallback = null;
        }
        if (i == 2) {
            native_agps_data_conn_closed();
            return;
        }
        if (i == 5) {
            native_agps_data_conn_failed();
            return;
        }
        Log.e("GnssNetworkConnectivityHandler", "Invalid status to release SUPL connection: " + i);
    }

    private void setRouting() {
        if (!this.mConnMgr.requestRouteToHostAddress(3, this.mAGpsDataConnectionIpAddr)) {
            Log.e("GnssNetworkConnectivityHandler", "Error requesting route to host: " + this.mAGpsDataConnectionIpAddr);
            return;
        }
        if (DEBUG) {
            Log.d("GnssNetworkConnectivityHandler", "Successfully requested route to host: " + this.mAGpsDataConnectionIpAddr);
        }
    }

    private void ensureInHandlerThread() {
        if (this.mHandler == null || Looper.myLooper() != this.mHandler.getLooper()) {
            throw new IllegalStateException("This method must run on the Handler thread.");
        }
    }

    private String agpsDataConnStateAsString() {
        int i = this.mAGpsDataConnectionState;
        if (i == 0) {
            return "CLOSED";
        }
        if (i == 1) {
            return "OPENING";
        }
        if (i == 2) {
            return "OPEN";
        }
        return "<Unknown>(" + this.mAGpsDataConnectionState + ")";
    }

    private String agpsDataConnStatusAsString(int i) {
        if (i == 1) {
            return "REQUEST";
        }
        if (i == 2) {
            return "RELEASE";
        }
        if (i == 3) {
            return "CONNECTED";
        }
        if (i == 4) {
            return "DONE";
        }
        if (i == 5) {
            return "FAILED";
        }
        return "<Unknown>(" + i + ")";
    }

    private String agpsTypeAsString(int i) {
        if (i == 1) {
            return "SUPL";
        }
        if (i == 2) {
            return "C2K";
        }
        if (i == 3) {
            return "EIMS";
        }
        if (i == 4) {
            return "IMS";
        }
        return "<Unknown>(" + i + ")";
    }

    private int getLinkIpType(LinkProperties linkProperties) {
        ensureInHandlerThread();
        Iterator<LinkAddress> it = linkProperties.getLinkAddresses().iterator();
        boolean z = false;
        boolean z2 = false;
        while (it.hasNext()) {
            InetAddress address = it.next().getAddress();
            if (address instanceof Inet4Address) {
                z = true;
            } else if (address instanceof Inet6Address) {
                z2 = true;
            }
            if (DEBUG) {
                Log.d("GnssNetworkConnectivityHandler", "LinkAddress : " + address.toString());
            }
        }
        if (z && z2) {
            return 3;
        }
        if (z) {
            return 1;
        }
        return z2 ? 2 : 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isNativeAgpsRilSupported() {
        return native_is_agps_ril_supported();
    }
}
