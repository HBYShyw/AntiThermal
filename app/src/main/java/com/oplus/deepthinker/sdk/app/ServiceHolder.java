package com.oplus.deepthinker.sdk.app;

import android.os.Bundle;
import android.os.IBinder;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.Event;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventConfig;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventCallback;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventQueryListener;
import com.oplus.deepthinker.sdk.app.aidl.proton.appactionpredict.PredictAABResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.appactionpredict.PredictResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.DeepSleepPredictResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.SleepRecord;
import com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.TotalPredictResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.intentdecision.IntentResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.intentdecision.ServiceResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.periodtopapps.PeriodTopAppsResult;
import i6.IDeepThinkerBridge;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/* loaded from: classes.dex */
public class ServiceHolder {
    private static IDeepThinkerBridge DEFAULT = new IDeepThinkerBridge() { // from class: com.oplus.deepthinker.sdk.app.ServiceHolder.1
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // i6.IDeepThinkerBridge
        public int availableState(int i10, String str) {
            return 0;
        }

        @Override // i6.IDeepThinkerBridge
        public Bundle call(String str, String str2, Bundle bundle) {
            return null;
        }

        @Override // i6.IDeepThinkerBridge
        public List capability() {
            return null;
        }

        @Override // i6.IDeepThinkerBridge
        public Map checkPermission(int i10, String str) {
            return null;
        }

        public IBinder exchange(String str, String str2, IBinder iBinder) {
            return null;
        }

        @Override // i6.IDeepThinkerBridge
        public List<PeriodTopAppsResult> getAllPeriodDurationTopApps(int i10) {
            return null;
        }

        @Override // i6.IDeepThinkerBridge
        public List<PeriodTopAppsResult> getAllPeriodFrequencyTopApps(int i10) {
            return null;
        }

        @Override // i6.IDeepThinkerBridge
        public PredictResult getAppPredictResult(String str) {
            return null;
        }

        @Override // i6.IDeepThinkerBridge
        public List<PredictResult> getAppPredictResultMap(String str) {
            return null;
        }

        @Override // i6.IDeepThinkerBridge
        public List<String> getAppQueueSortedByComplex() {
            return null;
        }

        @Override // i6.IDeepThinkerBridge
        public List<String> getAppQueueSortedByCount() {
            return null;
        }

        @Override // i6.IDeepThinkerBridge
        public List<String> getAppQueueSortedByTime() {
            return null;
        }

        @Override // i6.IDeepThinkerBridge
        public int getAppType(String str) {
            return 0;
        }

        @Override // i6.IDeepThinkerBridge
        public Map getAppTypeMap(List<String> list) {
            return null;
        }

        @Override // i6.IDeepThinkerBridge
        public PeriodTopAppsResult getCertainPeriodDurationTopApps(float f10, int i10) {
            return null;
        }

        @Override // i6.IDeepThinkerBridge
        public PeriodTopAppsResult getCertainPeriodFrequencyTopApps(float f10, int i10) {
            return null;
        }

        @Override // i6.IDeepThinkerBridge
        public DeepSleepPredictResult getDeepSleepPredictResult() {
            return null;
        }

        @Override // i6.IDeepThinkerBridge
        public String getDeepSleepPredictResultWithPercentile() {
            return null;
        }

        @Override // i6.IDeepThinkerBridge
        public TotalPredictResult getDeepSleepTotalPredictResult() {
            return null;
        }

        @Override // i6.IDeepThinkerBridge
        public int getIdleScreenResultInLongTime() {
            return 0;
        }

        @Override // i6.IDeepThinkerBridge
        public int getIdleScreenResultInMiddleTime() {
            return 0;
        }

        @Override // i6.IDeepThinkerBridge
        public int getIdleScreenResultInShortTime() {
            return 0;
        }

        @Override // i6.IDeepThinkerBridge
        public SleepRecord getLastDeepSleepRecord() {
            return null;
        }

        @Override // i6.IDeepThinkerBridge
        public int getPlatformVersion() {
            return 0;
        }

        @Override // i6.IDeepThinkerBridge
        public PredictAABResult getPredictAABResult() {
            return null;
        }

        @Override // i6.IDeepThinkerBridge
        public DeepSleepPredictResult getPredictResultWithFeedBack() {
            return null;
        }

        @Override // i6.IDeepThinkerBridge
        public List<String> getSmartGpsBssidList() {
            return null;
        }

        @Override // i6.IDeepThinkerBridge
        public void onewayCall(String str, String str2, Bundle bundle) {
        }

        @Override // i6.IDeepThinkerBridge
        public IntentResult queryAwarenessIntent(int i10) {
            return null;
        }

        @Override // i6.IDeepThinkerBridge
        public ServiceResult queryAwarenessService(int i10) {
            return null;
        }

        @Override // i6.IDeepThinkerBridge
        public void queryEvent(Event event, IEventQueryListener iEventQueryListener) {
        }

        @Override // i6.IDeepThinkerBridge
        public void queryEvents(EventConfig eventConfig, IEventQueryListener iEventQueryListener) {
        }

        @Override // i6.IDeepThinkerBridge
        public int registerEventCallback(String str, IEventCallback iEventCallback, EventConfig eventConfig) {
            return 0;
        }

        @Override // i6.IDeepThinkerBridge
        public void requestGrantPermission(String str) {
        }

        @Override // i6.IDeepThinkerBridge
        public IntentResult sortAwarenessIntent(IntentResult intentResult, boolean z10) {
            return null;
        }

        @Override // i6.IDeepThinkerBridge
        public ServiceResult sortAwarenessService(ServiceResult serviceResult, boolean z10) {
            return null;
        }

        @Override // i6.IDeepThinkerBridge
        public int unregisterEventCallback(String str) {
            return 0;
        }

        @Override // i6.IDeepThinkerBridge
        public int unregisterEventCallbackWithArgs(String str, EventConfig eventConfig) {
            return 0;
        }
    };
    private IDeepThinkerBridge mRemote;
    private Supplier<IDeepThinkerBridge> mRemoteGetter;

    public IDeepThinkerBridge getDeepThinkerBridge() {
        IDeepThinkerBridge iDeepThinkerBridge;
        IDeepThinkerBridge iDeepThinkerBridge2 = this.mRemote;
        if (iDeepThinkerBridge2 != null) {
            return iDeepThinkerBridge2;
        }
        Supplier<IDeepThinkerBridge> supplier = this.mRemoteGetter;
        if (supplier != null && (iDeepThinkerBridge = supplier.get()) != null) {
            return iDeepThinkerBridge;
        }
        SDKLog.w("ServiceHolder", "getRemote: call default!");
        return DEFAULT;
    }

    public IDeepThinkerBridge getRemote() {
        return this.mRemote;
    }

    public void setRemote(IDeepThinkerBridge iDeepThinkerBridge) {
        this.mRemote = iDeepThinkerBridge;
    }

    public void setRemoteGetter(Supplier<IDeepThinkerBridge> supplier) {
        this.mRemoteGetter = supplier;
    }
}
