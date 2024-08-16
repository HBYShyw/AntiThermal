package com.android.server.vcn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.vcn.VcnManager;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.ParcelUuid;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.IndentingPrintWriter;
import com.android.server.vcn.util.PersistableBundleUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class TelephonySubscriptionTracker extends BroadcastReceiver {
    private static final boolean LOG_DBG = false;
    private static final String TAG = TelephonySubscriptionTracker.class.getSimpleName();
    private final ActiveDataSubscriptionIdListener mActiveDataSubIdListener;
    private final TelephonySubscriptionTrackerCallback mCallback;
    private final CarrierConfigManager.CarrierConfigChangeListener mCarrierConfigChangeListener;
    private final CarrierConfigManager mCarrierConfigManager;
    private final List<TelephonyManager.CarrierPrivilegesCallback> mCarrierPrivilegesCallbacks;
    private final Context mContext;
    private TelephonySubscriptionSnapshot mCurrentSnapshot;
    private final Dependencies mDeps;
    private final Handler mHandler;
    private final Map<Integer, Integer> mReadySubIdsBySlotId;
    private final Map<Integer, PersistableBundleUtils.PersistableBundleWrapper> mSubIdToCarrierConfigMap;
    private final SubscriptionManager.OnSubscriptionsChangedListener mSubscriptionChangedListener;
    private final SubscriptionManager mSubscriptionManager;
    private final TelephonyManager mTelephonyManager;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface TelephonySubscriptionTrackerCallback {
        void onNewSnapshot(TelephonySubscriptionSnapshot telephonySubscriptionSnapshot);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, int i2, int i3, int i4) {
        handleActionCarrierConfigChanged(i, i2);
    }

    public TelephonySubscriptionTracker(Context context, Handler handler, TelephonySubscriptionTrackerCallback telephonySubscriptionTrackerCallback) {
        this(context, handler, telephonySubscriptionTrackerCallback, new Dependencies());
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    TelephonySubscriptionTracker(Context context, Handler handler, TelephonySubscriptionTrackerCallback telephonySubscriptionTrackerCallback, Dependencies dependencies) {
        this.mReadySubIdsBySlotId = new HashMap();
        this.mSubIdToCarrierConfigMap = new HashMap();
        this.mCarrierPrivilegesCallbacks = new ArrayList();
        this.mCarrierConfigChangeListener = new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.server.vcn.TelephonySubscriptionTracker$$ExternalSyntheticLambda0
            @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
            public final void onCarrierConfigChanged(int i, int i2, int i3, int i4) {
                TelephonySubscriptionTracker.this.lambda$new$0(i, i2, i3, i4);
            }
        };
        Objects.requireNonNull(context, "Missing context");
        this.mContext = context;
        Objects.requireNonNull(handler, "Missing handler");
        this.mHandler = handler;
        Objects.requireNonNull(telephonySubscriptionTrackerCallback, "Missing callback");
        this.mCallback = telephonySubscriptionTrackerCallback;
        Objects.requireNonNull(dependencies, "Missing deps");
        this.mDeps = dependencies;
        this.mTelephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        this.mCarrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
        this.mActiveDataSubIdListener = new ActiveDataSubscriptionIdListener();
        this.mSubscriptionChangedListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.server.vcn.TelephonySubscriptionTracker.1
            @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
            public void onSubscriptionsChanged() {
                TelephonySubscriptionTracker.this.handleSubscriptionsChanged();
            }
        };
    }

    public void register() {
        Executor handlerExecutor = new HandlerExecutor(this.mHandler);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.telephony.action.MULTI_SIM_CONFIG_CHANGED");
        this.mContext.registerReceiver(this, intentFilter, null, this.mHandler);
        this.mSubscriptionManager.addOnSubscriptionsChangedListener(handlerExecutor, this.mSubscriptionChangedListener);
        this.mTelephonyManager.registerTelephonyCallback(handlerExecutor, this.mActiveDataSubIdListener);
        this.mCarrierConfigManager.registerCarrierConfigChangeListener(handlerExecutor, this.mCarrierConfigChangeListener);
        registerCarrierPrivilegesCallbacks();
    }

    private void registerCarrierPrivilegesCallbacks() {
        Executor handlerExecutor = new HandlerExecutor(this.mHandler);
        int activeModemCount = this.mTelephonyManager.getActiveModemCount();
        for (int i = 0; i < activeModemCount; i++) {
            try {
                TelephonyManager.CarrierPrivilegesCallback carrierPrivilegesCallback = new TelephonyManager.CarrierPrivilegesCallback() { // from class: com.android.server.vcn.TelephonySubscriptionTracker.2
                    public void onCarrierPrivilegesChanged(Set<String> set, Set<Integer> set2) {
                        TelephonySubscriptionTracker.this.handleSubscriptionsChanged();
                    }
                };
                this.mTelephonyManager.registerCarrierPrivilegesCallback(i, handlerExecutor, carrierPrivilegesCallback);
                this.mCarrierPrivilegesCallbacks.add(carrierPrivilegesCallback);
            } catch (IllegalArgumentException e) {
                Slog.wtf(TAG, "Encounted exception registering carrier privileges listeners", e);
                return;
            }
        }
    }

    public void unregister() {
        this.mContext.unregisterReceiver(this);
        this.mSubscriptionManager.removeOnSubscriptionsChangedListener(this.mSubscriptionChangedListener);
        this.mTelephonyManager.unregisterTelephonyCallback(this.mActiveDataSubIdListener);
        this.mCarrierConfigManager.unregisterCarrierConfigChangeListener(this.mCarrierConfigChangeListener);
        unregisterCarrierPrivilegesCallbacks();
    }

    private void unregisterCarrierPrivilegesCallbacks() {
        Iterator<TelephonyManager.CarrierPrivilegesCallback> it = this.mCarrierPrivilegesCallbacks.iterator();
        while (it.hasNext()) {
            this.mTelephonyManager.unregisterCarrierPrivilegesCallback(it.next());
        }
        this.mCarrierPrivilegesCallbacks.clear();
    }

    public void handleSubscriptionsChanged() {
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        List<SubscriptionInfo> allSubscriptionInfoList = this.mSubscriptionManager.getAllSubscriptionInfoList();
        if (allSubscriptionInfoList == null) {
            return;
        }
        for (SubscriptionInfo subscriptionInfo : allSubscriptionInfoList) {
            if (subscriptionInfo.getGroupUuid() != null) {
                hashMap2.put(Integer.valueOf(subscriptionInfo.getSubscriptionId()), subscriptionInfo);
                if (subscriptionInfo.getSimSlotIndex() != -1 && this.mReadySubIdsBySlotId.values().contains(Integer.valueOf(subscriptionInfo.getSubscriptionId()))) {
                    TelephonyManager createForSubscriptionId = this.mTelephonyManager.createForSubscriptionId(subscriptionInfo.getSubscriptionId());
                    ParcelUuid groupUuid = subscriptionInfo.getGroupUuid();
                    Set set = (Set) hashMap.getOrDefault(groupUuid, new ArraySet());
                    set.addAll(createForSubscriptionId.getPackagesWithCarrierPrivileges());
                    hashMap.put(groupUuid, set);
                }
            }
        }
        final TelephonySubscriptionSnapshot telephonySubscriptionSnapshot = new TelephonySubscriptionSnapshot(this.mDeps.getActiveDataSubscriptionId(), hashMap2, this.mSubIdToCarrierConfigMap, hashMap);
        if (telephonySubscriptionSnapshot.equals(this.mCurrentSnapshot)) {
            return;
        }
        this.mCurrentSnapshot = telephonySubscriptionSnapshot;
        this.mHandler.post(new Runnable() { // from class: com.android.server.vcn.TelephonySubscriptionTracker$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                TelephonySubscriptionTracker.this.lambda$handleSubscriptionsChanged$1(telephonySubscriptionSnapshot);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleSubscriptionsChanged$1(TelephonySubscriptionSnapshot telephonySubscriptionSnapshot) {
        this.mCallback.onNewSnapshot(telephonySubscriptionSnapshot);
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        action.hashCode();
        if (action.equals("android.telephony.action.MULTI_SIM_CONFIG_CHANGED")) {
            handleActionMultiSimConfigChanged(context, intent);
            return;
        }
        Slog.v(TAG, "Unknown intent received with action: " + intent.getAction());
    }

    private void handleActionMultiSimConfigChanged(Context context, Intent intent) {
        unregisterCarrierPrivilegesCallbacks();
        int activeModemCount = this.mTelephonyManager.getActiveModemCount();
        Iterator<Integer> it = this.mReadySubIdsBySlotId.keySet().iterator();
        while (it.hasNext()) {
            if (it.next().intValue() >= activeModemCount) {
                it.remove();
            }
        }
        registerCarrierPrivilegesCallbacks();
        handleSubscriptionsChanged();
    }

    private void handleActionCarrierConfigChanged(int i, int i2) {
        if (i == -1) {
            return;
        }
        if (SubscriptionManager.isValidSubscriptionId(i2)) {
            PersistableBundle configForSubId = this.mCarrierConfigManager.getConfigForSubId(i2, VcnManager.VCN_RELATED_CARRIER_CONFIG_KEYS);
            if (this.mDeps.isConfigForIdentifiedCarrier(configForSubId)) {
                this.mReadySubIdsBySlotId.put(Integer.valueOf(i), Integer.valueOf(i2));
                if (!configForSubId.isEmpty()) {
                    this.mSubIdToCarrierConfigMap.put(Integer.valueOf(i2), new PersistableBundleUtils.PersistableBundleWrapper(configForSubId));
                }
                handleSubscriptionsChanged();
                return;
            }
            return;
        }
        Integer remove = this.mReadySubIdsBySlotId.remove(Integer.valueOf(i));
        if (remove != null) {
            this.mSubIdToCarrierConfigMap.remove(remove);
        }
        handleSubscriptionsChanged();
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    void setReadySubIdsBySlotId(Map<Integer, Integer> map) {
        this.mReadySubIdsBySlotId.clear();
        this.mReadySubIdsBySlotId.putAll(map);
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    void setSubIdToCarrierConfigMap(Map<Integer, PersistableBundleUtils.PersistableBundleWrapper> map) {
        this.mSubIdToCarrierConfigMap.clear();
        this.mSubIdToCarrierConfigMap.putAll(map);
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    Map<Integer, Integer> getReadySubIdsBySlotId() {
        return Collections.unmodifiableMap(this.mReadySubIdsBySlotId);
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    Map<Integer, PersistableBundleUtils.PersistableBundleWrapper> getSubIdToCarrierConfigMap() {
        return Collections.unmodifiableMap(this.mSubIdToCarrierConfigMap);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class TelephonySubscriptionSnapshot {
        public static final TelephonySubscriptionSnapshot EMPTY_SNAPSHOT = new TelephonySubscriptionSnapshot(-1, Collections.emptyMap(), Collections.emptyMap(), Collections.emptyMap());
        private final int mActiveDataSubId;
        private final Map<ParcelUuid, Set<String>> mPrivilegedPackages;
        private final Map<Integer, PersistableBundleUtils.PersistableBundleWrapper> mSubIdToCarrierConfigMap;
        private final Map<Integer, SubscriptionInfo> mSubIdToInfoMap;

        @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
        TelephonySubscriptionSnapshot(int i, Map<Integer, SubscriptionInfo> map, Map<Integer, PersistableBundleUtils.PersistableBundleWrapper> map2, Map<ParcelUuid, Set<String>> map3) {
            this.mActiveDataSubId = i;
            Objects.requireNonNull(map, "subIdToInfoMap was null");
            Objects.requireNonNull(map3, "privilegedPackages was null");
            Objects.requireNonNull(map2, "subIdToCarrierConfigMap was null");
            this.mSubIdToInfoMap = Collections.unmodifiableMap(new HashMap(map));
            this.mSubIdToCarrierConfigMap = Collections.unmodifiableMap(new HashMap(map2));
            ArrayMap arrayMap = new ArrayMap();
            for (Map.Entry<ParcelUuid, Set<String>> entry : map3.entrySet()) {
                arrayMap.put(entry.getKey(), Collections.unmodifiableSet(entry.getValue()));
            }
            this.mPrivilegedPackages = Collections.unmodifiableMap(arrayMap);
        }

        public int getActiveDataSubscriptionId() {
            return this.mActiveDataSubId;
        }

        public ParcelUuid getActiveDataSubscriptionGroup() {
            SubscriptionInfo subscriptionInfo = this.mSubIdToInfoMap.get(Integer.valueOf(getActiveDataSubscriptionId()));
            if (subscriptionInfo == null) {
                return null;
            }
            return subscriptionInfo.getGroupUuid();
        }

        public Set<ParcelUuid> getActiveSubscriptionGroups() {
            return this.mPrivilegedPackages.keySet();
        }

        public boolean packageHasPermissionsForSubscriptionGroup(ParcelUuid parcelUuid, String str) {
            Set<String> set = this.mPrivilegedPackages.get(parcelUuid);
            return set != null && set.contains(str);
        }

        public ParcelUuid getGroupForSubId(int i) {
            if (this.mSubIdToInfoMap.containsKey(Integer.valueOf(i))) {
                return this.mSubIdToInfoMap.get(Integer.valueOf(i)).getGroupUuid();
            }
            return null;
        }

        public Set<Integer> getAllSubIdsInGroup(ParcelUuid parcelUuid) {
            ArraySet arraySet = new ArraySet();
            for (Map.Entry<Integer, SubscriptionInfo> entry : this.mSubIdToInfoMap.entrySet()) {
                if (parcelUuid.equals(entry.getValue().getGroupUuid())) {
                    arraySet.add(entry.getKey());
                }
            }
            return arraySet;
        }

        public boolean isOpportunistic(int i) {
            if (this.mSubIdToInfoMap.containsKey(Integer.valueOf(i))) {
                return this.mSubIdToInfoMap.get(Integer.valueOf(i)).isOpportunistic();
            }
            return false;
        }

        public PersistableBundleUtils.PersistableBundleWrapper getCarrierConfigForSubGrp(ParcelUuid parcelUuid) {
            Iterator<Integer> it = getAllSubIdsInGroup(parcelUuid).iterator();
            PersistableBundleUtils.PersistableBundleWrapper persistableBundleWrapper = null;
            while (it.hasNext()) {
                int intValue = it.next().intValue();
                PersistableBundleUtils.PersistableBundleWrapper persistableBundleWrapper2 = this.mSubIdToCarrierConfigMap.get(Integer.valueOf(intValue));
                if (persistableBundleWrapper2 != null) {
                    if (!isOpportunistic(intValue)) {
                        return persistableBundleWrapper2;
                    }
                    persistableBundleWrapper = persistableBundleWrapper2;
                }
            }
            return persistableBundleWrapper;
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.mActiveDataSubId), this.mSubIdToInfoMap, this.mSubIdToCarrierConfigMap, this.mPrivilegedPackages);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof TelephonySubscriptionSnapshot)) {
                return false;
            }
            TelephonySubscriptionSnapshot telephonySubscriptionSnapshot = (TelephonySubscriptionSnapshot) obj;
            return this.mActiveDataSubId == telephonySubscriptionSnapshot.mActiveDataSubId && this.mSubIdToInfoMap.equals(telephonySubscriptionSnapshot.mSubIdToInfoMap) && this.mSubIdToCarrierConfigMap.equals(telephonySubscriptionSnapshot.mSubIdToCarrierConfigMap) && this.mPrivilegedPackages.equals(telephonySubscriptionSnapshot.mPrivilegedPackages);
        }

        public void dump(IndentingPrintWriter indentingPrintWriter) {
            indentingPrintWriter.println("TelephonySubscriptionSnapshot:");
            indentingPrintWriter.increaseIndent();
            indentingPrintWriter.println("mActiveDataSubId: " + this.mActiveDataSubId);
            indentingPrintWriter.println("mSubIdToInfoMap: " + this.mSubIdToInfoMap);
            indentingPrintWriter.println("mSubIdToCarrierConfigMap: " + this.mSubIdToCarrierConfigMap);
            indentingPrintWriter.println("mPrivilegedPackages: " + this.mPrivilegedPackages);
            indentingPrintWriter.decreaseIndent();
        }

        public String toString() {
            return "TelephonySubscriptionSnapshot{ mActiveDataSubId=" + this.mActiveDataSubId + ", mSubIdToInfoMap=" + this.mSubIdToInfoMap + ", mSubIdToCarrierConfigMap=" + this.mSubIdToCarrierConfigMap + ", mPrivilegedPackages=" + this.mPrivilegedPackages + " }";
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class ActiveDataSubscriptionIdListener extends TelephonyCallback implements TelephonyCallback.ActiveDataSubscriptionIdListener {
        private ActiveDataSubscriptionIdListener() {
        }

        @Override // android.telephony.TelephonyCallback.ActiveDataSubscriptionIdListener
        public void onActiveDataSubscriptionIdChanged(int i) {
            TelephonySubscriptionTracker.this.handleSubscriptionsChanged();
        }
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Dependencies {
        public boolean isConfigForIdentifiedCarrier(PersistableBundle persistableBundle) {
            return CarrierConfigManager.isConfigForIdentifiedCarrier(persistableBundle);
        }

        public int getActiveDataSubscriptionId() {
            return SubscriptionManager.getActiveDataSubscriptionId();
        }
    }
}
