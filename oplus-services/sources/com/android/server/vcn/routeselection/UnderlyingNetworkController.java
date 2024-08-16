package com.android.server.vcn.routeselection;

import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.TelephonyNetworkSpecifier;
import android.net.vcn.VcnCellUnderlyingNetworkTemplate;
import android.net.vcn.VcnGatewayConnectionConfig;
import android.net.vcn.VcnUnderlyingNetworkTemplate;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.ParcelUuid;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.IndentingPrintWriter;
import com.android.server.VcnManagementService;
import com.android.server.vcn.TelephonySubscriptionTracker;
import com.android.server.vcn.VcnContext;
import com.android.server.vcn.routeselection.UnderlyingNetworkRecord;
import com.android.server.vcn.util.LogUtils;
import com.android.server.vcn.util.PersistableBundleUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class UnderlyingNetworkController {
    private static final String TAG = "UnderlyingNetworkController";
    private final TelephonyCallback mActiveDataSubIdListener;
    private PersistableBundleUtils.PersistableBundleWrapper mCarrierConfig;
    private final UnderlyingNetworkControllerCallback mCb;
    private final List<ConnectivityManager.NetworkCallback> mCellBringupCallbacks;
    private final VcnGatewayConnectionConfig mConnectionConfig;
    private final ConnectivityManager mConnectivityManager;
    private UnderlyingNetworkRecord mCurrentRecord;
    private final Dependencies mDeps;
    private final Handler mHandler;
    private boolean mIsQuitting;
    private TelephonySubscriptionTracker.TelephonySubscriptionSnapshot mLastSnapshot;
    private UnderlyingNetworkRecord.Builder mRecordInProgress;
    private UnderlyingNetworkListener mRouteSelectionCallback;
    private final ParcelUuid mSubscriptionGroup;
    private final VcnContext mVcnContext;
    private ConnectivityManager.NetworkCallback mWifiBringupCallback;
    private ConnectivityManager.NetworkCallback mWifiEntryRssiThresholdCallback;
    private ConnectivityManager.NetworkCallback mWifiExitRssiThresholdCallback;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface UnderlyingNetworkControllerCallback {
        void onSelectedUnderlyingNetworkChanged(UnderlyingNetworkRecord underlyingNetworkRecord);
    }

    public UnderlyingNetworkController(VcnContext vcnContext, VcnGatewayConnectionConfig vcnGatewayConnectionConfig, ParcelUuid parcelUuid, TelephonySubscriptionTracker.TelephonySubscriptionSnapshot telephonySubscriptionSnapshot, UnderlyingNetworkControllerCallback underlyingNetworkControllerCallback) {
        this(vcnContext, vcnGatewayConnectionConfig, parcelUuid, telephonySubscriptionSnapshot, underlyingNetworkControllerCallback, new Dependencies());
    }

    private UnderlyingNetworkController(VcnContext vcnContext, VcnGatewayConnectionConfig vcnGatewayConnectionConfig, ParcelUuid parcelUuid, TelephonySubscriptionTracker.TelephonySubscriptionSnapshot telephonySubscriptionSnapshot, UnderlyingNetworkControllerCallback underlyingNetworkControllerCallback, Dependencies dependencies) {
        VcnActiveDataSubscriptionIdListener vcnActiveDataSubscriptionIdListener = new VcnActiveDataSubscriptionIdListener();
        this.mActiveDataSubIdListener = vcnActiveDataSubscriptionIdListener;
        this.mCellBringupCallbacks = new ArrayList();
        this.mIsQuitting = false;
        Objects.requireNonNull(vcnContext, "Missing vcnContext");
        this.mVcnContext = vcnContext;
        Objects.requireNonNull(vcnGatewayConnectionConfig, "Missing connectionConfig");
        this.mConnectionConfig = vcnGatewayConnectionConfig;
        Objects.requireNonNull(parcelUuid, "Missing subscriptionGroup");
        this.mSubscriptionGroup = parcelUuid;
        Objects.requireNonNull(telephonySubscriptionSnapshot, "Missing snapshot");
        this.mLastSnapshot = telephonySubscriptionSnapshot;
        Objects.requireNonNull(underlyingNetworkControllerCallback, "Missing cb");
        this.mCb = underlyingNetworkControllerCallback;
        Objects.requireNonNull(dependencies, "Missing deps");
        this.mDeps = dependencies;
        Handler handler = new Handler(vcnContext.getLooper());
        this.mHandler = handler;
        this.mConnectivityManager = (ConnectivityManager) vcnContext.getContext().getSystemService(ConnectivityManager.class);
        ((TelephonyManager) vcnContext.getContext().getSystemService(TelephonyManager.class)).registerTelephonyCallback(new HandlerExecutor(handler), vcnActiveDataSubscriptionIdListener);
        this.mCarrierConfig = this.mLastSnapshot.getCarrierConfigForSubGrp(parcelUuid);
        registerOrUpdateNetworkRequests();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class CapabilityMatchCriteria {
        public final int capability;
        public final int matchCriteria;

        CapabilityMatchCriteria(int i, int i2) {
            this.capability = i;
            this.matchCriteria = i2;
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.capability), Integer.valueOf(this.matchCriteria));
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof CapabilityMatchCriteria)) {
                return false;
            }
            CapabilityMatchCriteria capabilityMatchCriteria = (CapabilityMatchCriteria) obj;
            return this.capability == capabilityMatchCriteria.capability && this.matchCriteria == capabilityMatchCriteria.matchCriteria;
        }
    }

    private static Set<Set<CapabilityMatchCriteria>> dedupAndGetCapRequirementsForCell(VcnGatewayConnectionConfig vcnGatewayConnectionConfig) {
        ArraySet arraySet = new ArraySet();
        for (VcnUnderlyingNetworkTemplate vcnUnderlyingNetworkTemplate : vcnGatewayConnectionConfig.getVcnUnderlyingNetworkPriorities()) {
            if (vcnUnderlyingNetworkTemplate instanceof VcnCellUnderlyingNetworkTemplate) {
                ArraySet arraySet2 = new ArraySet();
                for (Map.Entry entry : ((VcnCellUnderlyingNetworkTemplate) vcnUnderlyingNetworkTemplate).getCapabilitiesMatchCriteria().entrySet()) {
                    int intValue = ((Integer) entry.getKey()).intValue();
                    int intValue2 = ((Integer) entry.getValue()).intValue();
                    if (intValue2 != 0) {
                        arraySet2.add(new CapabilityMatchCriteria(intValue, intValue2));
                    }
                }
                arraySet.add(arraySet2);
            }
        }
        arraySet.add(Collections.singleton(new CapabilityMatchCriteria(12, 1)));
        return arraySet;
    }

    private void registerOrUpdateNetworkRequests() {
        UnderlyingNetworkListener underlyingNetworkListener = this.mRouteSelectionCallback;
        ConnectivityManager.NetworkCallback networkCallback = this.mWifiBringupCallback;
        ConnectivityManager.NetworkCallback networkCallback2 = this.mWifiEntryRssiThresholdCallback;
        ConnectivityManager.NetworkCallback networkCallback3 = this.mWifiExitRssiThresholdCallback;
        ArrayList arrayList = new ArrayList(this.mCellBringupCallbacks);
        this.mCellBringupCallbacks.clear();
        if (!this.mIsQuitting) {
            this.mRouteSelectionCallback = new UnderlyingNetworkListener();
            this.mConnectivityManager.registerNetworkCallback(getRouteSelectionRequest(), this.mRouteSelectionCallback, this.mHandler);
            this.mWifiEntryRssiThresholdCallback = new NetworkBringupCallback();
            this.mConnectivityManager.registerNetworkCallback(getWifiEntryRssiThresholdNetworkRequest(), this.mWifiEntryRssiThresholdCallback, this.mHandler);
            this.mWifiExitRssiThresholdCallback = new NetworkBringupCallback();
            this.mConnectivityManager.registerNetworkCallback(getWifiExitRssiThresholdNetworkRequest(), this.mWifiExitRssiThresholdCallback, this.mHandler);
            this.mWifiBringupCallback = new NetworkBringupCallback();
            this.mConnectivityManager.requestBackgroundNetwork(getWifiNetworkRequest(), this.mWifiBringupCallback, this.mHandler);
            Iterator<Integer> it = this.mLastSnapshot.getAllSubIdsInGroup(this.mSubscriptionGroup).iterator();
            while (it.hasNext()) {
                int intValue = it.next().intValue();
                for (Set<CapabilityMatchCriteria> set : dedupAndGetCapRequirementsForCell(this.mConnectionConfig)) {
                    NetworkBringupCallback networkBringupCallback = new NetworkBringupCallback();
                    this.mCellBringupCallbacks.add(networkBringupCallback);
                    this.mConnectivityManager.requestBackgroundNetwork(getCellNetworkRequestForSubId(intValue, set), networkBringupCallback, this.mHandler);
                }
            }
        } else {
            this.mRouteSelectionCallback = null;
            this.mWifiBringupCallback = null;
            this.mWifiEntryRssiThresholdCallback = null;
            this.mWifiExitRssiThresholdCallback = null;
        }
        if (underlyingNetworkListener != null) {
            this.mConnectivityManager.unregisterNetworkCallback(underlyingNetworkListener);
        }
        if (networkCallback != null) {
            this.mConnectivityManager.unregisterNetworkCallback(networkCallback);
        }
        if (networkCallback2 != null) {
            this.mConnectivityManager.unregisterNetworkCallback(networkCallback2);
        }
        if (networkCallback3 != null) {
            this.mConnectivityManager.unregisterNetworkCallback(networkCallback3);
        }
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            this.mConnectivityManager.unregisterNetworkCallback((ConnectivityManager.NetworkCallback) it2.next());
        }
    }

    private NetworkRequest getRouteSelectionRequest() {
        if (this.mVcnContext.isInTestMode()) {
            return getTestNetworkRequest(this.mLastSnapshot.getAllSubIdsInGroup(this.mSubscriptionGroup));
        }
        return getBaseNetworkRequestBuilder().addCapability(16).addCapability(21).setSubscriptionIds(this.mLastSnapshot.getAllSubIdsInGroup(this.mSubscriptionGroup)).build();
    }

    private NetworkRequest.Builder getBaseWifiNetworkRequestBuilder() {
        return getBaseNetworkRequestBuilder().addTransportType(1).addCapability(12).setSubscriptionIds(this.mLastSnapshot.getAllSubIdsInGroup(this.mSubscriptionGroup));
    }

    private NetworkRequest getWifiNetworkRequest() {
        return getBaseWifiNetworkRequestBuilder().build();
    }

    private NetworkRequest getWifiEntryRssiThresholdNetworkRequest() {
        return getBaseWifiNetworkRequestBuilder().setSignalStrength(NetworkPriorityClassifier.getWifiEntryRssiThreshold(this.mCarrierConfig)).build();
    }

    private NetworkRequest getWifiExitRssiThresholdNetworkRequest() {
        return getBaseWifiNetworkRequestBuilder().setSignalStrength(NetworkPriorityClassifier.getWifiExitRssiThreshold(this.mCarrierConfig)).build();
    }

    private NetworkRequest getCellNetworkRequestForSubId(int i, Set<CapabilityMatchCriteria> set) {
        NetworkRequest.Builder networkSpecifier = getBaseNetworkRequestBuilder().addTransportType(0).setNetworkSpecifier(new TelephonyNetworkSpecifier(i));
        for (CapabilityMatchCriteria capabilityMatchCriteria : set) {
            int i2 = capabilityMatchCriteria.capability;
            int i3 = capabilityMatchCriteria.matchCriteria;
            if (i3 == 1) {
                networkSpecifier.addCapability(i2);
            } else if (i3 == 2) {
                networkSpecifier.addForbiddenCapability(i2);
            }
        }
        return networkSpecifier.build();
    }

    private NetworkRequest.Builder getBaseNetworkRequestBuilder() {
        return new NetworkRequest.Builder().removeCapability(14).removeCapability(13).removeCapability(28);
    }

    private NetworkRequest getTestNetworkRequest(Set<Integer> set) {
        return new NetworkRequest.Builder().clearCapabilities().addTransportType(7).setSubscriptionIds(set).build();
    }

    public void updateSubscriptionSnapshot(TelephonySubscriptionTracker.TelephonySubscriptionSnapshot telephonySubscriptionSnapshot) {
        Objects.requireNonNull(telephonySubscriptionSnapshot, "Missing newSnapshot");
        TelephonySubscriptionTracker.TelephonySubscriptionSnapshot telephonySubscriptionSnapshot2 = this.mLastSnapshot;
        this.mLastSnapshot = telephonySubscriptionSnapshot;
        this.mCarrierConfig = telephonySubscriptionSnapshot.getCarrierConfigForSubGrp(this.mSubscriptionGroup);
        if (telephonySubscriptionSnapshot2.getAllSubIdsInGroup(this.mSubscriptionGroup).equals(telephonySubscriptionSnapshot.getAllSubIdsInGroup(this.mSubscriptionGroup))) {
            return;
        }
        registerOrUpdateNetworkRequests();
    }

    public void teardown() {
        this.mVcnContext.ensureRunningOnLooperThread();
        this.mIsQuitting = true;
        registerOrUpdateNetworkRequests();
        ((TelephonyManager) this.mVcnContext.getContext().getSystemService(TelephonyManager.class)).unregisterTelephonyCallback(this.mActiveDataSubIdListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reevaluateNetworks() {
        UnderlyingNetworkListener underlyingNetworkListener;
        if (this.mIsQuitting || (underlyingNetworkListener = this.mRouteSelectionCallback) == null) {
            return;
        }
        TreeSet sortedUnderlyingNetworks = underlyingNetworkListener.getSortedUnderlyingNetworks();
        UnderlyingNetworkRecord underlyingNetworkRecord = sortedUnderlyingNetworks.isEmpty() ? null : (UnderlyingNetworkRecord) sortedUnderlyingNetworks.first();
        if (Objects.equals(this.mCurrentRecord, underlyingNetworkRecord)) {
            return;
        }
        Iterator it = sortedUnderlyingNetworks.iterator();
        String str = "";
        while (it.hasNext()) {
            UnderlyingNetworkRecord underlyingNetworkRecord2 = (UnderlyingNetworkRecord) it.next();
            if (!str.isEmpty()) {
                str = str + ", ";
            }
            str = str + underlyingNetworkRecord2.network + ": " + underlyingNetworkRecord2.priorityClass;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Selected network changed to ");
        sb.append(underlyingNetworkRecord != null ? underlyingNetworkRecord.network : null);
        sb.append(", selected from list: ");
        sb.append(str);
        logInfo(sb.toString());
        this.mCurrentRecord = underlyingNetworkRecord;
        this.mCb.onSelectedUnderlyingNetworkChanged(underlyingNetworkRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class NetworkBringupCallback extends ConnectivityManager.NetworkCallback {
        NetworkBringupCallback() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class UnderlyingNetworkListener extends ConnectivityManager.NetworkCallback {
        private final Map<Network, UnderlyingNetworkRecord.Builder> mUnderlyingNetworkRecordBuilders;

        UnderlyingNetworkListener() {
            super(1);
            this.mUnderlyingNetworkRecordBuilders = new ArrayMap();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public TreeSet<UnderlyingNetworkRecord> getSortedUnderlyingNetworks() {
            TreeSet<UnderlyingNetworkRecord> treeSet = new TreeSet<>(UnderlyingNetworkRecord.getComparator());
            for (UnderlyingNetworkRecord.Builder builder : this.mUnderlyingNetworkRecordBuilders.values()) {
                if (builder.isValid()) {
                    UnderlyingNetworkRecord build = builder.build(UnderlyingNetworkController.this.mVcnContext, UnderlyingNetworkController.this.mConnectionConfig.getVcnUnderlyingNetworkPriorities(), UnderlyingNetworkController.this.mSubscriptionGroup, UnderlyingNetworkController.this.mLastSnapshot, UnderlyingNetworkController.this.mCurrentRecord, UnderlyingNetworkController.this.mCarrierConfig);
                    if (build.priorityClass != -1) {
                        treeSet.add(build);
                    }
                }
            }
            return treeSet;
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(Network network) {
            this.mUnderlyingNetworkRecordBuilders.put(network, new UnderlyingNetworkRecord.Builder(network));
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            this.mUnderlyingNetworkRecordBuilders.remove(network);
            UnderlyingNetworkController.this.reevaluateNetworks();
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            UnderlyingNetworkRecord.Builder builder = this.mUnderlyingNetworkRecordBuilders.get(network);
            if (builder == null) {
                UnderlyingNetworkController.this.logWtf("Got capabilities change for unknown key: " + network);
                return;
            }
            builder.setNetworkCapabilities(networkCapabilities);
            if (builder.isValid()) {
                UnderlyingNetworkController.this.reevaluateNetworks();
            }
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
            UnderlyingNetworkRecord.Builder builder = this.mUnderlyingNetworkRecordBuilders.get(network);
            if (builder == null) {
                UnderlyingNetworkController.this.logWtf("Got link properties change for unknown key: " + network);
                return;
            }
            builder.setLinkProperties(linkProperties);
            if (builder.isValid()) {
                UnderlyingNetworkController.this.reevaluateNetworks();
            }
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onBlockedStatusChanged(Network network, boolean z) {
            UnderlyingNetworkRecord.Builder builder = this.mUnderlyingNetworkRecordBuilders.get(network);
            if (builder == null) {
                UnderlyingNetworkController.this.logWtf("Got blocked status change for unknown key: " + network);
                return;
            }
            builder.setIsBlocked(z);
            if (builder.isValid()) {
                UnderlyingNetworkController.this.reevaluateNetworks();
            }
        }
    }

    private String getLogPrefix() {
        return "(" + LogUtils.getHashedSubscriptionGroup(this.mSubscriptionGroup) + "-" + this.mConnectionConfig.getGatewayConnectionName() + "-" + System.identityHashCode(this) + ") ";
    }

    private String getTagLogPrefix() {
        return "[ " + TAG + " " + getLogPrefix() + "]";
    }

    private void logInfo(String str) {
        Slog.i(TAG, getLogPrefix() + str);
        VcnManagementService.LOCAL_LOG.log("[INFO] " + getTagLogPrefix() + str);
    }

    private void logInfo(String str, Throwable th) {
        Slog.i(TAG, getLogPrefix() + str, th);
        VcnManagementService.LOCAL_LOG.log("[INFO] " + getTagLogPrefix() + str + th);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logWtf(String str) {
        String str2 = TAG;
        Slog.wtf(str2, str);
        VcnManagementService.LOCAL_LOG.log(str2 + "[WTF ] " + getTagLogPrefix() + str);
    }

    private void logWtf(String str, Throwable th) {
        String str2 = TAG;
        Slog.wtf(str2, str, th);
        VcnManagementService.LOCAL_LOG.log(str2 + "[WTF ] " + getTagLogPrefix() + str + th);
    }

    public void dump(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.println("UnderlyingNetworkController:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("Carrier WiFi Entry Threshold: " + NetworkPriorityClassifier.getWifiEntryRssiThreshold(this.mCarrierConfig));
        indentingPrintWriter.println("Carrier WiFi Exit Threshold: " + NetworkPriorityClassifier.getWifiExitRssiThreshold(this.mCarrierConfig));
        StringBuilder sb = new StringBuilder();
        sb.append("Currently selected: ");
        UnderlyingNetworkRecord underlyingNetworkRecord = this.mCurrentRecord;
        sb.append(underlyingNetworkRecord == null ? null : underlyingNetworkRecord.network);
        indentingPrintWriter.println(sb.toString());
        indentingPrintWriter.println("VcnUnderlyingNetworkTemplate list:");
        indentingPrintWriter.increaseIndent();
        int i = 0;
        for (VcnUnderlyingNetworkTemplate vcnUnderlyingNetworkTemplate : this.mConnectionConfig.getVcnUnderlyingNetworkPriorities()) {
            indentingPrintWriter.println("Priority index: " + i);
            vcnUnderlyingNetworkTemplate.dump(indentingPrintWriter);
            i++;
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println();
        indentingPrintWriter.println("Underlying networks:");
        indentingPrintWriter.increaseIndent();
        UnderlyingNetworkListener underlyingNetworkListener = this.mRouteSelectionCallback;
        if (underlyingNetworkListener != null) {
            Iterator it = underlyingNetworkListener.getSortedUnderlyingNetworks().iterator();
            while (it.hasNext()) {
                ((UnderlyingNetworkRecord) it.next()).dump(this.mVcnContext, indentingPrintWriter, this.mConnectionConfig.getVcnUnderlyingNetworkPriorities(), this.mSubscriptionGroup, this.mLastSnapshot, this.mCurrentRecord, this.mCarrierConfig);
            }
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println();
        indentingPrintWriter.decreaseIndent();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class VcnActiveDataSubscriptionIdListener extends TelephonyCallback implements TelephonyCallback.ActiveDataSubscriptionIdListener {
        private VcnActiveDataSubscriptionIdListener() {
        }

        @Override // android.telephony.TelephonyCallback.ActiveDataSubscriptionIdListener
        public void onActiveDataSubscriptionIdChanged(int i) {
            UnderlyingNetworkController.this.reevaluateNetworks();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class Dependencies {
        private Dependencies() {
        }
    }
}
