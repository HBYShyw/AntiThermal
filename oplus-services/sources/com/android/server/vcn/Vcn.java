package com.android.server.vcn;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.NetworkScore;
import android.net.Uri;
import android.net.vcn.VcnConfig;
import android.net.vcn.VcnGatewayConnectionConfig;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Message;
import android.os.ParcelUuid;
import android.provider.Settings;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.IndentingPrintWriter;
import com.android.server.VcnManagementService;
import com.android.server.timezonedetector.ServiceConfigAccessor;
import com.android.server.vcn.TelephonySubscriptionTracker;
import com.android.server.vcn.VcnNetworkProvider;
import com.android.server.vcn.util.LogUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class Vcn extends Handler {
    private static final int MSG_CMD_BASE = 100;
    private static final int MSG_CMD_TEARDOWN = 100;
    private static final int MSG_EVENT_BASE = 0;
    private static final int MSG_EVENT_CONFIG_UPDATED = 0;
    private static final int MSG_EVENT_GATEWAY_CONNECTION_QUIT = 3;
    private static final int MSG_EVENT_MOBILE_DATA_TOGGLED = 5;
    private static final int MSG_EVENT_NETWORK_REQUESTED = 1;
    private static final int MSG_EVENT_SAFE_MODE_STATE_CHANGED = 4;
    private static final int MSG_EVENT_SUBSCRIPTIONS_CHANGED = 2;
    private static final int VCN_LEGACY_SCORE_INT = 52;
    private VcnConfig mConfig;
    private final VcnContentResolver mContentResolver;
    private volatile int mCurrentStatus;
    private final Dependencies mDeps;
    private boolean mIsMobileDataEnabled;
    private TelephonySubscriptionTracker.TelephonySubscriptionSnapshot mLastSnapshot;
    private final ContentObserver mMobileDataSettingsObserver;
    private final Map<Integer, VcnUserMobileDataStateListener> mMobileDataStateListeners;
    private final VcnNetworkRequestListener mRequestListener;
    private final ParcelUuid mSubscriptionGroup;
    private final VcnManagementService.VcnCallback mVcnCallback;
    private final VcnContext mVcnContext;
    private final Map<VcnGatewayConnectionConfig, VcnGatewayConnection> mVcnGatewayConnections;
    private static final String TAG = Vcn.class.getSimpleName();
    private static final List<Integer> CAPS_REQUIRING_MOBILE_DATA = Arrays.asList(12, 2);

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface VcnGatewayStatusCallback {
        void onGatewayConnectionError(String str, int i, String str2, String str3);

        void onQuit();

        void onSafeModeStatusChanged();
    }

    private void logVdbg(String str) {
    }

    public Vcn(VcnContext vcnContext, ParcelUuid parcelUuid, VcnConfig vcnConfig, TelephonySubscriptionTracker.TelephonySubscriptionSnapshot telephonySubscriptionSnapshot, VcnManagementService.VcnCallback vcnCallback) {
        this(vcnContext, parcelUuid, vcnConfig, telephonySubscriptionSnapshot, vcnCallback, new Dependencies());
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public Vcn(VcnContext vcnContext, ParcelUuid parcelUuid, VcnConfig vcnConfig, TelephonySubscriptionTracker.TelephonySubscriptionSnapshot telephonySubscriptionSnapshot, VcnManagementService.VcnCallback vcnCallback, Dependencies dependencies) {
        super(vcnContext.getLooper());
        Objects.requireNonNull(vcnContext, "Missing vcnContext");
        this.mMobileDataStateListeners = new ArrayMap();
        this.mVcnGatewayConnections = new HashMap();
        this.mCurrentStatus = 2;
        this.mIsMobileDataEnabled = false;
        this.mVcnContext = vcnContext;
        Objects.requireNonNull(parcelUuid, "Missing subscriptionGroup");
        this.mSubscriptionGroup = parcelUuid;
        Objects.requireNonNull(vcnCallback, "Missing vcnCallback");
        this.mVcnCallback = vcnCallback;
        Objects.requireNonNull(dependencies, "Missing deps");
        this.mDeps = dependencies;
        VcnNetworkRequestListener vcnNetworkRequestListener = new VcnNetworkRequestListener();
        this.mRequestListener = vcnNetworkRequestListener;
        VcnContentResolver newVcnContentResolver = dependencies.newVcnContentResolver(vcnContext);
        this.mContentResolver = newVcnContentResolver;
        VcnMobileDataContentObserver vcnMobileDataContentObserver = new VcnMobileDataContentObserver(this);
        this.mMobileDataSettingsObserver = vcnMobileDataContentObserver;
        newVcnContentResolver.registerContentObserver(Settings.Global.getUriFor("mobile_data"), true, vcnMobileDataContentObserver);
        Objects.requireNonNull(vcnConfig, "Missing config");
        this.mConfig = vcnConfig;
        Objects.requireNonNull(telephonySubscriptionSnapshot, "Missing snapshot");
        this.mLastSnapshot = telephonySubscriptionSnapshot;
        this.mIsMobileDataEnabled = getMobileDataStatus();
        updateMobileDataStateListeners();
        vcnContext.getVcnNetworkProvider().registerListener(vcnNetworkRequestListener);
    }

    public void updateConfig(VcnConfig vcnConfig) {
        Objects.requireNonNull(vcnConfig, "Missing config");
        sendMessage(obtainMessage(0, vcnConfig));
    }

    public void updateSubscriptionSnapshot(TelephonySubscriptionTracker.TelephonySubscriptionSnapshot telephonySubscriptionSnapshot) {
        Objects.requireNonNull(telephonySubscriptionSnapshot, "Missing snapshot");
        sendMessage(obtainMessage(2, telephonySubscriptionSnapshot));
    }

    public void teardownAsynchronously() {
        sendMessageAtFrontOfQueue(obtainMessage(100));
    }

    public int getStatus() {
        return this.mCurrentStatus;
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public void setStatus(int i) {
        this.mCurrentStatus = i;
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public Set<VcnGatewayConnection> getVcnGatewayConnections() {
        return Collections.unmodifiableSet(new HashSet(this.mVcnGatewayConnections.values()));
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public Map<VcnGatewayConnectionConfig, VcnGatewayConnection> getVcnGatewayConnectionConfigMap() {
        return Collections.unmodifiableMap(new HashMap(this.mVcnGatewayConnections));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class VcnNetworkRequestListener implements VcnNetworkProvider.NetworkRequestListener {
        private VcnNetworkRequestListener() {
        }

        @Override // com.android.server.vcn.VcnNetworkProvider.NetworkRequestListener
        public void onNetworkRequested(NetworkRequest networkRequest) {
            Objects.requireNonNull(networkRequest, "Missing request");
            Vcn vcn = Vcn.this;
            vcn.sendMessage(vcn.obtainMessage(1, networkRequest));
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        if (this.mCurrentStatus == 2 || this.mCurrentStatus == 3) {
            int i = message.what;
            if (i == 0) {
                handleConfigUpdated((VcnConfig) message.obj);
                return;
            }
            if (i == 1) {
                handleNetworkRequested((NetworkRequest) message.obj);
                return;
            }
            if (i == 2) {
                handleSubscriptionsChanged((TelephonySubscriptionTracker.TelephonySubscriptionSnapshot) message.obj);
                return;
            }
            if (i == 3) {
                handleGatewayConnectionQuit((VcnGatewayConnectionConfig) message.obj);
                return;
            }
            if (i == 4) {
                handleSafeModeStatusChanged();
                return;
            }
            if (i == 5) {
                handleMobileDataToggled();
                return;
            }
            if (i == 100) {
                handleTeardown();
                return;
            }
            logWtf("Unknown msg.what: " + message.what);
        }
    }

    private void handleConfigUpdated(VcnConfig vcnConfig) {
        logDbg("Config updated: old = " + this.mConfig.hashCode() + "; new = " + vcnConfig.hashCode());
        this.mConfig = vcnConfig;
        for (Map.Entry<VcnGatewayConnectionConfig, VcnGatewayConnection> entry : this.mVcnGatewayConnections.entrySet()) {
            VcnGatewayConnectionConfig key = entry.getKey();
            VcnGatewayConnection value = entry.getValue();
            if (!this.mConfig.getGatewayConnectionConfigs().contains(key)) {
                if (value == null) {
                    logWtf("Found gatewayConnectionConfig without GatewayConnection");
                } else {
                    logInfo("Config updated, restarting gateway " + value.getLogPrefix());
                    value.teardownAsynchronously();
                }
            }
        }
        this.mVcnContext.getVcnNetworkProvider().resendAllRequests(this.mRequestListener);
    }

    private void handleTeardown() {
        logDbg("Tearing down");
        this.mVcnContext.getVcnNetworkProvider().unregisterListener(this.mRequestListener);
        Iterator<VcnGatewayConnection> it = this.mVcnGatewayConnections.values().iterator();
        while (it.hasNext()) {
            it.next().teardownAsynchronously();
        }
        Iterator<VcnUserMobileDataStateListener> it2 = this.mMobileDataStateListeners.values().iterator();
        while (it2.hasNext()) {
            getTelephonyManager().unregisterTelephonyCallback(it2.next());
        }
        this.mMobileDataStateListeners.clear();
        this.mCurrentStatus = 1;
    }

    private void handleSafeModeStatusChanged() {
        boolean z;
        logVdbg("VcnGatewayConnection safe mode status changed");
        Iterator<VcnGatewayConnection> it = this.mVcnGatewayConnections.values().iterator();
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            } else if (it.next().isInSafeMode()) {
                z = true;
                break;
            }
        }
        int i = this.mCurrentStatus;
        this.mCurrentStatus = z ? 3 : 2;
        if (i != this.mCurrentStatus) {
            this.mVcnCallback.onSafeModeStatusChanged(z);
            StringBuilder sb = new StringBuilder();
            sb.append("Safe mode ");
            sb.append(this.mCurrentStatus == 3 ? "entered" : "exited");
            logInfo(sb.toString());
        }
    }

    private void handleNetworkRequested(NetworkRequest networkRequest) {
        logVdbg("Received request " + networkRequest);
        Iterator<VcnGatewayConnectionConfig> it = this.mVcnGatewayConnections.keySet().iterator();
        while (it.hasNext()) {
            if (isRequestSatisfiedByGatewayConnectionConfig(networkRequest, it.next())) {
                logVdbg("Request already satisfied by existing VcnGatewayConnection: " + networkRequest);
                return;
            }
        }
        for (VcnGatewayConnectionConfig vcnGatewayConnectionConfig : this.mConfig.getGatewayConnectionConfigs()) {
            if (isRequestSatisfiedByGatewayConnectionConfig(networkRequest, vcnGatewayConnectionConfig) && !getExposedCapabilitiesForMobileDataState(vcnGatewayConnectionConfig).isEmpty()) {
                if (this.mVcnGatewayConnections.containsKey(vcnGatewayConnectionConfig)) {
                    logWtf("Attempted to bring up VcnGatewayConnection for config with existing VcnGatewayConnection");
                    return;
                }
                logInfo("Bringing up new VcnGatewayConnection for request " + networkRequest);
                this.mVcnGatewayConnections.put(vcnGatewayConnectionConfig, this.mDeps.newVcnGatewayConnection(this.mVcnContext, this.mSubscriptionGroup, this.mLastSnapshot, vcnGatewayConnectionConfig, new VcnGatewayStatusCallbackImpl(vcnGatewayConnectionConfig), this.mIsMobileDataEnabled));
                return;
            }
        }
        logVdbg("Request could not be fulfilled by VCN: " + networkRequest);
    }

    private Set<Integer> getExposedCapabilitiesForMobileDataState(VcnGatewayConnectionConfig vcnGatewayConnectionConfig) {
        if (this.mIsMobileDataEnabled) {
            return vcnGatewayConnectionConfig.getAllExposedCapabilities();
        }
        ArraySet arraySet = new ArraySet(vcnGatewayConnectionConfig.getAllExposedCapabilities());
        arraySet.removeAll(CAPS_REQUIRING_MOBILE_DATA);
        return arraySet;
    }

    private void handleGatewayConnectionQuit(VcnGatewayConnectionConfig vcnGatewayConnectionConfig) {
        logInfo("VcnGatewayConnection quit: " + vcnGatewayConnectionConfig);
        this.mVcnGatewayConnections.remove(vcnGatewayConnectionConfig);
        this.mVcnContext.getVcnNetworkProvider().resendAllRequests(this.mRequestListener);
    }

    private void handleSubscriptionsChanged(TelephonySubscriptionTracker.TelephonySubscriptionSnapshot telephonySubscriptionSnapshot) {
        this.mLastSnapshot = telephonySubscriptionSnapshot;
        Iterator<VcnGatewayConnection> it = this.mVcnGatewayConnections.values().iterator();
        while (it.hasNext()) {
            it.next().updateSubscriptionSnapshot(this.mLastSnapshot);
        }
        updateMobileDataStateListeners();
        handleMobileDataToggled();
    }

    private void updateMobileDataStateListeners() {
        Set<Integer> allSubIdsInGroup = this.mLastSnapshot.getAllSubIdsInGroup(this.mSubscriptionGroup);
        HandlerExecutor handlerExecutor = new HandlerExecutor(this);
        Iterator<Integer> it = allSubIdsInGroup.iterator();
        while (it.hasNext()) {
            int intValue = it.next().intValue();
            if (!this.mMobileDataStateListeners.containsKey(Integer.valueOf(intValue))) {
                VcnUserMobileDataStateListener vcnUserMobileDataStateListener = new VcnUserMobileDataStateListener();
                getTelephonyManagerForSubid(intValue).registerTelephonyCallback(handlerExecutor, vcnUserMobileDataStateListener);
                this.mMobileDataStateListeners.put(Integer.valueOf(intValue), vcnUserMobileDataStateListener);
            }
        }
        Iterator<Map.Entry<Integer, VcnUserMobileDataStateListener>> it2 = this.mMobileDataStateListeners.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry<Integer, VcnUserMobileDataStateListener> next = it2.next();
            if (!allSubIdsInGroup.contains(next.getKey())) {
                getTelephonyManager().unregisterTelephonyCallback(next.getValue());
                it2.remove();
            }
        }
    }

    private void handleMobileDataToggled() {
        boolean z = this.mIsMobileDataEnabled;
        boolean mobileDataStatus = getMobileDataStatus();
        this.mIsMobileDataEnabled = mobileDataStatus;
        if (z != mobileDataStatus) {
            for (Map.Entry<VcnGatewayConnectionConfig, VcnGatewayConnection> entry : this.mVcnGatewayConnections.entrySet()) {
                VcnGatewayConnectionConfig key = entry.getKey();
                VcnGatewayConnection value = entry.getValue();
                Set allExposedCapabilities = key.getAllExposedCapabilities();
                if (allExposedCapabilities.contains(12) || allExposedCapabilities.contains(2)) {
                    if (value == null) {
                        logWtf("Found gatewayConnectionConfig without GatewayConnection");
                    } else {
                        value.teardownAsynchronously();
                    }
                }
            }
            this.mVcnContext.getVcnNetworkProvider().resendAllRequests(this.mRequestListener);
            StringBuilder sb = new StringBuilder();
            sb.append("Mobile data ");
            sb.append(this.mIsMobileDataEnabled ? ServiceConfigAccessor.PROVIDER_MODE_ENABLED : ServiceConfigAccessor.PROVIDER_MODE_DISABLED);
            logInfo(sb.toString());
        }
    }

    private boolean getMobileDataStatus() {
        Iterator<Integer> it = this.mLastSnapshot.getAllSubIdsInGroup(this.mSubscriptionGroup).iterator();
        while (it.hasNext()) {
            if (getTelephonyManagerForSubid(it.next().intValue()).isDataEnabled()) {
                return true;
            }
        }
        return false;
    }

    private boolean isRequestSatisfiedByGatewayConnectionConfig(NetworkRequest networkRequest, VcnGatewayConnectionConfig vcnGatewayConnectionConfig) {
        NetworkCapabilities.Builder builder = new NetworkCapabilities.Builder();
        builder.addTransportType(0);
        builder.addCapability(28);
        Iterator<Integer> it = getExposedCapabilitiesForMobileDataState(vcnGatewayConnectionConfig).iterator();
        while (it.hasNext()) {
            builder.addCapability(it.next().intValue());
        }
        return networkRequest.canBeSatisfiedBy(builder.build());
    }

    private TelephonyManager getTelephonyManager() {
        return (TelephonyManager) this.mVcnContext.getContext().getSystemService(TelephonyManager.class);
    }

    private TelephonyManager getTelephonyManagerForSubid(int i) {
        return getTelephonyManager().createForSubscriptionId(i);
    }

    private String getLogPrefix() {
        return "(" + LogUtils.getHashedSubscriptionGroup(this.mSubscriptionGroup) + "-" + System.identityHashCode(this) + ") ";
    }

    private void logDbg(String str) {
        Slog.d(TAG, getLogPrefix() + str);
    }

    private void logDbg(String str, Throwable th) {
        Slog.d(TAG, getLogPrefix() + str, th);
    }

    private void logInfo(String str) {
        Slog.i(TAG, getLogPrefix() + str);
        VcnManagementService.LOCAL_LOG.log(getLogPrefix() + "INFO: " + str);
    }

    private void logInfo(String str, Throwable th) {
        Slog.i(TAG, getLogPrefix() + str, th);
        VcnManagementService.LOCAL_LOG.log(getLogPrefix() + "INFO: " + str + th);
    }

    private void logErr(String str) {
        Slog.e(TAG, getLogPrefix() + str);
        VcnManagementService.LOCAL_LOG.log(getLogPrefix() + "ERR: " + str);
    }

    private void logErr(String str, Throwable th) {
        Slog.e(TAG, getLogPrefix() + str, th);
        VcnManagementService.LOCAL_LOG.log(getLogPrefix() + "ERR: " + str + th);
    }

    private void logWtf(String str) {
        Slog.wtf(TAG, getLogPrefix() + str);
        VcnManagementService.LOCAL_LOG.log(getLogPrefix() + "WTF: " + str);
    }

    private void logWtf(String str, Throwable th) {
        Slog.wtf(TAG, getLogPrefix() + str, th);
        VcnManagementService.LOCAL_LOG.log(getLogPrefix() + "WTF: " + str + th);
    }

    public void dump(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.println("Vcn (" + this.mSubscriptionGroup + "):");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("mCurrentStatus: " + this.mCurrentStatus);
        indentingPrintWriter.println("mIsMobileDataEnabled: " + this.mIsMobileDataEnabled);
        indentingPrintWriter.println();
        indentingPrintWriter.println("mVcnGatewayConnections:");
        indentingPrintWriter.increaseIndent();
        Iterator<VcnGatewayConnection> it = this.mVcnGatewayConnections.values().iterator();
        while (it.hasNext()) {
            it.next().dump(indentingPrintWriter);
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println();
        indentingPrintWriter.decreaseIndent();
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public boolean isMobileDataEnabled() {
        return this.mIsMobileDataEnabled;
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public void setMobileDataEnabled(boolean z) {
        this.mIsMobileDataEnabled = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static NetworkScore getNetworkScore() {
        return new NetworkScore.Builder().setLegacyInt(52).setTransportPrimary(true).build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class VcnGatewayStatusCallbackImpl implements VcnGatewayStatusCallback {
        public final VcnGatewayConnectionConfig mGatewayConnectionConfig;

        VcnGatewayStatusCallbackImpl(VcnGatewayConnectionConfig vcnGatewayConnectionConfig) {
            this.mGatewayConnectionConfig = vcnGatewayConnectionConfig;
        }

        @Override // com.android.server.vcn.Vcn.VcnGatewayStatusCallback
        public void onQuit() {
            Vcn vcn = Vcn.this;
            vcn.sendMessage(vcn.obtainMessage(3, this.mGatewayConnectionConfig));
        }

        @Override // com.android.server.vcn.Vcn.VcnGatewayStatusCallback
        public void onSafeModeStatusChanged() {
            Vcn vcn = Vcn.this;
            vcn.sendMessage(vcn.obtainMessage(4));
        }

        @Override // com.android.server.vcn.Vcn.VcnGatewayStatusCallback
        public void onGatewayConnectionError(String str, int i, String str2, String str3) {
            Vcn.this.mVcnCallback.onGatewayConnectionError(str, i, str2, str3);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class VcnMobileDataContentObserver extends ContentObserver {
        private VcnMobileDataContentObserver(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            Vcn vcn = Vcn.this;
            vcn.sendMessage(vcn.obtainMessage(5));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class VcnUserMobileDataStateListener extends TelephonyCallback implements TelephonyCallback.UserMobileDataStateListener {
        VcnUserMobileDataStateListener() {
        }

        @Override // android.telephony.TelephonyCallback.UserMobileDataStateListener
        public void onUserMobileDataStateChanged(boolean z) {
            Vcn vcn = Vcn.this;
            vcn.sendMessage(vcn.obtainMessage(5));
        }
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Dependencies {
        public VcnGatewayConnection newVcnGatewayConnection(VcnContext vcnContext, ParcelUuid parcelUuid, TelephonySubscriptionTracker.TelephonySubscriptionSnapshot telephonySubscriptionSnapshot, VcnGatewayConnectionConfig vcnGatewayConnectionConfig, VcnGatewayStatusCallback vcnGatewayStatusCallback, boolean z) {
            return new VcnGatewayConnection(vcnContext, parcelUuid, telephonySubscriptionSnapshot, vcnGatewayConnectionConfig, vcnGatewayStatusCallback, z);
        }

        public VcnContentResolver newVcnContentResolver(VcnContext vcnContext) {
            return new VcnContentResolver(vcnContext);
        }
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class VcnContentResolver {
        private final ContentResolver mImpl;

        public VcnContentResolver(VcnContext vcnContext) {
            this.mImpl = vcnContext.getContext().getContentResolver();
        }

        public void registerContentObserver(Uri uri, boolean z, ContentObserver contentObserver) {
            this.mImpl.registerContentObserver(uri, z, contentObserver);
        }
    }
}
