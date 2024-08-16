package com.android.server.connectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.INetdEventCallback;
import android.net.MacAddress;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.metrics.ConnectStats;
import android.net.metrics.DnsEvent;
import android.net.metrics.INetdEventListener;
import android.net.metrics.NetworkMetrics;
import android.net.metrics.WakeupEvent;
import android.net.metrics.WakeupStats;
import android.os.BatteryStatsInternal;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.BitUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.RingBuffer;
import com.android.internal.util.TokenBucket;
import com.android.net.module.util.BaseNetdEventListener;
import com.android.server.IOplusNecConnectMonitor;
import com.android.server.LocalServices;
import com.android.server.OplusServiceFactory;
import com.android.server.connectivity.metrics.nano.IpConnectivityLogClass;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class NetdEventListenerService extends BaseNetdEventListener {
    private static final int CONNECT_LATENCY_BURST_LIMIT = 5000;
    private static final int CONNECT_LATENCY_FILL_RATE = 15000;
    private static final boolean DBG = false;
    private static final int METRICS_SNAPSHOT_BUFFER_SIZE = 48;
    private static final long METRICS_SNAPSHOT_SPAN_MS = 300000;
    public static final String SERVICE_NAME = "netd_listener";

    @VisibleForTesting
    static final int WAKEUP_EVENT_BUFFER_LENGTH = 1024;

    @VisibleForTesting
    static final String WAKEUP_EVENT_PREFIX_DELIM = ":";
    final TransportForNetIdNetworkCallback mCallback;
    private final ConnectivityManager mCm;

    @GuardedBy({"this"})
    private final TokenBucket mConnectTb;
    private Context mContext;

    @GuardedBy({"this"})
    private long mLastSnapshot;

    @GuardedBy({"this"})
    private INetdEventCallback[] mNetdEventCallbackList;

    @GuardedBy({"this"})
    private final SparseArray<NetworkMetrics> mNetworkMetrics;

    @GuardedBy({"this"})
    private final RingBuffer<NetworkMetricsSnapshot> mNetworkMetricsSnapshots;

    @GuardedBy({"this"})
    private final RingBuffer<WakeupEvent> mWakeupEvents;

    @GuardedBy({"this"})
    private final ArrayMap<String, WakeupStats> mWakeupStats;
    private static final String TAG = NetdEventListenerService.class.getSimpleName();

    @GuardedBy({"this"})
    private static final int[] ALLOWED_CALLBACK_TYPES = {0, 1, 2};

    @Override // com.android.net.module.util.BaseNetdEventListener, android.net.metrics.INetdEventListener
    public String getInterfaceHash() {
        return INetdEventListener.HASH;
    }

    @Override // com.android.net.module.util.BaseNetdEventListener, android.net.metrics.INetdEventListener
    public int getInterfaceVersion() {
        return 1;
    }

    public synchronized boolean addNetdEventCallback(int i, INetdEventCallback iNetdEventCallback) {
        if (!isValidCallerType(i)) {
            Log.e(TAG, "Invalid caller type: " + i);
            return false;
        }
        this.mNetdEventCallbackList[i] = iNetdEventCallback;
        return true;
    }

    public synchronized boolean removeNetdEventCallback(int i) {
        if (!isValidCallerType(i)) {
            Log.e(TAG, "Invalid caller type: " + i);
            return false;
        }
        this.mNetdEventCallbackList[i] = null;
        return true;
    }

    private static boolean isValidCallerType(int i) {
        int i2 = 0;
        while (true) {
            int[] iArr = ALLOWED_CALLBACK_TYPES;
            if (i2 >= iArr.length) {
                return false;
            }
            if (i == iArr[i2]) {
                return true;
            }
            i2++;
        }
    }

    public NetdEventListenerService(Context context) {
        this((ConnectivityManager) context.getSystemService(ConnectivityManager.class));
        this.mContext = context;
    }

    @VisibleForTesting
    public NetdEventListenerService(ConnectivityManager connectivityManager) {
        this.mContext = null;
        this.mNetworkMetrics = new SparseArray<>();
        this.mNetworkMetricsSnapshots = new RingBuffer<>(NetworkMetricsSnapshot.class, 48);
        this.mLastSnapshot = 0L;
        this.mWakeupStats = new ArrayMap<>();
        this.mWakeupEvents = new RingBuffer<>(WakeupEvent.class, 1024);
        this.mConnectTb = new TokenBucket(15000, 5000);
        TransportForNetIdNetworkCallback transportForNetIdNetworkCallback = new TransportForNetIdNetworkCallback();
        this.mCallback = transportForNetIdNetworkCallback;
        this.mNetdEventCallbackList = new INetdEventCallback[ALLOWED_CALLBACK_TYPES.length];
        this.mCm = connectivityManager;
        connectivityManager.registerNetworkCallback(new NetworkRequest.Builder().clearCapabilities().build(), transportForNetIdNetworkCallback);
    }

    private static long projectSnapshotTime(long j) {
        return (j / 300000) * 300000;
    }

    private NetworkMetrics getMetricsForNetwork(long j, int i) {
        NetworkMetrics networkMetrics = this.mNetworkMetrics.get(i);
        NetworkCapabilities networkCapabilities = this.mCallback.getNetworkCapabilities(i);
        long packBits = networkCapabilities != null ? BitUtils.packBits(networkCapabilities.getTransportTypes()) : 0L;
        boolean z = (networkMetrics == null || networkCapabilities == null || networkMetrics.transports == packBits) ? false : true;
        collectPendingMetricsSnapshot(j, z);
        if (networkMetrics != null && !z) {
            return networkMetrics;
        }
        NetworkMetrics networkMetrics2 = new NetworkMetrics(i, packBits, this.mConnectTb);
        this.mNetworkMetrics.put(i, networkMetrics2);
        return networkMetrics2;
    }

    private NetworkMetricsSnapshot[] getNetworkMetricsSnapshots() {
        collectPendingMetricsSnapshot(System.currentTimeMillis(), false);
        return (NetworkMetricsSnapshot[]) this.mNetworkMetricsSnapshots.toArray();
    }

    private void collectPendingMetricsSnapshot(long j, boolean z) {
        if (z || Math.abs(j - this.mLastSnapshot) > 300000) {
            long projectSnapshotTime = projectSnapshotTime(j);
            this.mLastSnapshot = projectSnapshotTime;
            NetworkMetricsSnapshot collect = NetworkMetricsSnapshot.collect(projectSnapshotTime, this.mNetworkMetrics);
            if (collect.stats.isEmpty()) {
                return;
            }
            this.mNetworkMetricsSnapshots.append(collect);
        }
    }

    @Override // com.android.net.module.util.BaseNetdEventListener, android.net.metrics.INetdEventListener
    public synchronized void onDnsEvent(int i, int i2, int i3, int i4, String str, String[] strArr, int i5, int i6) {
        int i7;
        INetdEventCallback[] iNetdEventCallbackArr;
        int i8;
        long currentTimeMillis = System.currentTimeMillis();
        getMetricsForNetwork(currentTimeMillis, i).addDnsResult(i2, i3, i4);
        INetdEventCallback[] iNetdEventCallbackArr2 = this.mNetdEventCallbackList;
        int length = iNetdEventCallbackArr2.length;
        int i9 = 0;
        while (i9 < length) {
            INetdEventCallback iNetdEventCallback = iNetdEventCallbackArr2[i9];
            if (iNetdEventCallback != null) {
                i7 = i9;
                iNetdEventCallbackArr = iNetdEventCallbackArr2;
                i8 = length;
                try {
                    iNetdEventCallback.onDnsEvent(i, i2, i3, str, strArr, i5, currentTimeMillis, i6);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            } else {
                i7 = i9;
                iNetdEventCallbackArr = iNetdEventCallbackArr2;
                i8 = length;
            }
            i9 = i7 + 1;
            iNetdEventCallbackArr2 = iNetdEventCallbackArr;
            length = i8;
        }
        if (this.mContext != null) {
            OplusServiceFactory oplusServiceFactory = OplusServiceFactory.getInstance();
            IOplusNecConnectMonitor iOplusNecConnectMonitor = IOplusNecConnectMonitor.DEFAULT;
            ((IOplusNecConnectMonitor) oplusServiceFactory.getFeature(iOplusNecConnectMonitor, new Object[]{this.mContext})).addDnsRecord(i, i3, i4, 0L, 0L);
            ((IOplusNecConnectMonitor) OplusServiceFactory.getInstance().getFeature(iOplusNecConnectMonitor, new Object[]{this.mContext})).onDnsEvent(i, i2, i3, i4, str, strArr, i5, i6);
        }
    }

    @Override // com.android.net.module.util.BaseNetdEventListener, android.net.metrics.INetdEventListener
    public synchronized void onNat64PrefixEvent(int i, boolean z, String str, int i2) {
        for (INetdEventCallback iNetdEventCallback : this.mNetdEventCallbackList) {
            if (iNetdEventCallback != null) {
                try {
                    iNetdEventCallback.onNat64PrefixEvent(i, z, str, i2);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        }
    }

    @Override // com.android.net.module.util.BaseNetdEventListener, android.net.metrics.INetdEventListener
    public synchronized void onPrivateDnsValidationEvent(int i, String str, String str2, boolean z) {
        for (INetdEventCallback iNetdEventCallback : this.mNetdEventCallbackList) {
            if (iNetdEventCallback != null) {
                try {
                    iNetdEventCallback.onPrivateDnsValidationEvent(i, str, str2, z);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        }
    }

    @Override // com.android.net.module.util.BaseNetdEventListener, android.net.metrics.INetdEventListener
    public synchronized void onConnectEvent(int i, int i2, int i3, String str, int i4, int i5) {
        int i6;
        long currentTimeMillis = System.currentTimeMillis();
        getMetricsForNetwork(currentTimeMillis, i).addConnectResult(i2, i3, str);
        INetdEventCallback[] iNetdEventCallbackArr = this.mNetdEventCallbackList;
        int length = iNetdEventCallbackArr.length;
        int i7 = 0;
        while (i7 < length) {
            INetdEventCallback iNetdEventCallback = iNetdEventCallbackArr[i7];
            if (iNetdEventCallback != null) {
                i6 = i7;
                try {
                    iNetdEventCallback.onConnectEvent(str, i4, currentTimeMillis, i5);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            } else {
                i6 = i7;
            }
            i7 = i6 + 1;
        }
        if (this.mContext != null) {
            ((IOplusNecConnectMonitor) OplusServiceFactory.getInstance().getFeature(IOplusNecConnectMonitor.DEFAULT, new Object[]{this.mContext})).onConnectEvent(i, i2, i3, str, i4, i5);
        }
    }

    private boolean hasWifiTransport(Network network) {
        return this.mCm.getNetworkCapabilities(network).hasTransport(1);
    }

    @Override // com.android.net.module.util.BaseNetdEventListener, android.net.metrics.INetdEventListener
    public synchronized void onWakeupEvent(String str, int i, int i2, int i3, byte[] bArr, String str2, String str3, int i4, int i5, long j) {
        String[] split = str.split(WAKEUP_EVENT_PREFIX_DELIM);
        if (split.length != 2) {
            throw new IllegalArgumentException("Prefix " + str + " required in format <nethandle>:<interface>");
        }
        Network fromNetworkHandle = Network.fromNetworkHandle(Long.parseLong(split[0]));
        WakeupEvent wakeupEvent = new WakeupEvent();
        wakeupEvent.iface = split[1];
        wakeupEvent.uid = i;
        wakeupEvent.ethertype = i2;
        if (ArrayUtils.isEmpty(bArr)) {
            if (hasWifiTransport(fromNetworkHandle)) {
                Log.e(TAG, "Empty mac address on WiFi transport, network: " + fromNetworkHandle);
            }
            wakeupEvent.dstHwAddr = null;
        } else {
            wakeupEvent.dstHwAddr = MacAddress.fromBytes(bArr);
        }
        wakeupEvent.srcIp = str2;
        wakeupEvent.dstIp = str3;
        wakeupEvent.ipNextHeader = i3;
        wakeupEvent.srcPort = i4;
        wakeupEvent.dstPort = i5;
        if (j > 0) {
            wakeupEvent.timestampMs = j / 1000000;
        } else {
            wakeupEvent.timestampMs = System.currentTimeMillis();
        }
        addWakeupEvent(wakeupEvent);
        BatteryStatsInternal batteryStatsInternal = (BatteryStatsInternal) LocalServices.getService(BatteryStatsInternal.class);
        if (batteryStatsInternal != null) {
            batteryStatsInternal.noteCpuWakingNetworkPacket(fromNetworkHandle, (SystemClock.elapsedRealtime() + wakeupEvent.timestampMs) - System.currentTimeMillis(), wakeupEvent.uid);
        }
        FrameworkStatsLog.write(44, i, wakeupEvent.iface, i2, String.valueOf(wakeupEvent.dstHwAddr), str2, str3, i3, i4, i5);
    }

    @Override // com.android.net.module.util.BaseNetdEventListener, android.net.metrics.INetdEventListener
    public synchronized void onTcpSocketStatsEvent(int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, int[] iArr5) {
        int[] iArr6 = iArr;
        synchronized (this) {
            if (iArr6.length == iArr2.length && iArr6.length == iArr3.length && iArr6.length == iArr4.length && iArr6.length == iArr5.length) {
                long currentTimeMillis = System.currentTimeMillis();
                int i = 0;
                while (i < iArr6.length) {
                    int i2 = iArr6[i];
                    int i3 = iArr2[i];
                    int i4 = iArr3[i];
                    int i5 = iArr4[i];
                    getMetricsForNetwork(currentTimeMillis, i2).addTcpStatsResult(i3, i4, i5, iArr5[i]);
                    if (this.mContext != null) {
                        ((IOplusNecConnectMonitor) OplusServiceFactory.getInstance().getFeature(IOplusNecConnectMonitor.DEFAULT, new Object[]{this.mContext})).addTcpStateRecord(i2, i3, i4, i5 / 1000);
                    }
                    i++;
                    iArr6 = iArr;
                }
                return;
            }
            Log.e(TAG, "Mismatched lengths of TCP socket stats data arrays");
        }
    }

    private void addWakeupEvent(WakeupEvent wakeupEvent) {
        String str = wakeupEvent.iface;
        this.mWakeupEvents.append(wakeupEvent);
        WakeupStats wakeupStats = this.mWakeupStats.get(str);
        if (wakeupStats == null) {
            wakeupStats = new WakeupStats(str);
            this.mWakeupStats.put(str, wakeupStats);
        }
        wakeupStats.countEvent(wakeupEvent);
    }

    public synchronized void flushStatistics(List<IpConnectivityLogClass.IpConnectivityEvent> list) {
        for (int i = 0; i < this.mNetworkMetrics.size(); i++) {
            ConnectStats connectStats = this.mNetworkMetrics.valueAt(i).connectMetrics;
            if (connectStats.eventCount != 0) {
                list.add(IpConnectivityEventBuilder.toProto(connectStats));
            }
        }
        for (int i2 = 0; i2 < this.mNetworkMetrics.size(); i2++) {
            DnsEvent dnsEvent = this.mNetworkMetrics.valueAt(i2).dnsMetrics;
            if (dnsEvent.eventCount != 0) {
                list.add(IpConnectivityEventBuilder.toProto(dnsEvent));
            }
        }
        for (int i3 = 0; i3 < this.mWakeupStats.size(); i3++) {
            list.add(IpConnectivityEventBuilder.toProto(this.mWakeupStats.valueAt(i3)));
        }
        this.mNetworkMetrics.clear();
        this.mWakeupStats.clear();
    }

    public synchronized void list(PrintWriter printWriter) {
        printWriter.println("dns/connect events:");
        for (int i = 0; i < this.mNetworkMetrics.size(); i++) {
            printWriter.println(this.mNetworkMetrics.valueAt(i).connectMetrics);
        }
        for (int i2 = 0; i2 < this.mNetworkMetrics.size(); i2++) {
            printWriter.println(this.mNetworkMetrics.valueAt(i2).dnsMetrics);
        }
        printWriter.println("");
        printWriter.println("network statistics:");
        for (NetworkMetricsSnapshot networkMetricsSnapshot : getNetworkMetricsSnapshots()) {
            printWriter.println(networkMetricsSnapshot);
        }
        printWriter.println("");
        printWriter.println("packet wakeup events:");
        for (int i3 = 0; i3 < this.mWakeupStats.size(); i3++) {
            printWriter.println(this.mWakeupStats.valueAt(i3));
        }
        for (WakeupEvent wakeupEvent : (WakeupEvent[]) this.mWakeupEvents.toArray()) {
            printWriter.println(wakeupEvent);
        }
    }

    public synchronized List<IpConnectivityLogClass.IpConnectivityEvent> listAsProtos() {
        ArrayList arrayList;
        arrayList = new ArrayList();
        for (int i = 0; i < this.mNetworkMetrics.size(); i++) {
            arrayList.add(IpConnectivityEventBuilder.toProto(this.mNetworkMetrics.valueAt(i).connectMetrics));
        }
        for (int i2 = 0; i2 < this.mNetworkMetrics.size(); i2++) {
            arrayList.add(IpConnectivityEventBuilder.toProto(this.mNetworkMetrics.valueAt(i2).dnsMetrics));
        }
        for (int i3 = 0; i3 < this.mWakeupStats.size(); i3++) {
            arrayList.add(IpConnectivityEventBuilder.toProto(this.mWakeupStats.valueAt(i3)));
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class NetworkMetricsSnapshot {
        public List<NetworkMetrics.Summary> stats = new ArrayList();
        public long timeMs;

        NetworkMetricsSnapshot() {
        }

        static NetworkMetricsSnapshot collect(long j, SparseArray<NetworkMetrics> sparseArray) {
            NetworkMetricsSnapshot networkMetricsSnapshot = new NetworkMetricsSnapshot();
            networkMetricsSnapshot.timeMs = j;
            for (int i = 0; i < sparseArray.size(); i++) {
                NetworkMetrics.Summary pendingStats = sparseArray.valueAt(i).getPendingStats();
                if (pendingStats != null) {
                    networkMetricsSnapshot.stats.add(pendingStats);
                }
            }
            return networkMetricsSnapshot;
        }

        public String toString() {
            StringJoiner stringJoiner = new StringJoiner(", ");
            Iterator<NetworkMetrics.Summary> it = this.stats.iterator();
            while (it.hasNext()) {
                stringJoiner.add(it.next().toString());
            }
            return String.format("%tT.%tL: %s", Long.valueOf(this.timeMs), Long.valueOf(this.timeMs), stringJoiner.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class TransportForNetIdNetworkCallback extends ConnectivityManager.NetworkCallback {
        private final SparseArray<NetworkCapabilities> mCapabilities;

        private TransportForNetIdNetworkCallback() {
            this.mCapabilities = new SparseArray<>();
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            synchronized (this.mCapabilities) {
                this.mCapabilities.put(network.getNetId(), networkCapabilities);
            }
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            synchronized (this.mCapabilities) {
                this.mCapabilities.remove(network.getNetId());
            }
        }

        public NetworkCapabilities getNetworkCapabilities(int i) {
            NetworkCapabilities networkCapabilities;
            synchronized (this.mCapabilities) {
                networkCapabilities = this.mCapabilities.get(i);
            }
            return networkCapabilities;
        }
    }
}
