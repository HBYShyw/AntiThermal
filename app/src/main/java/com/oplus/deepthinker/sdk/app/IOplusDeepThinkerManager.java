package com.oplus.deepthinker.sdk.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.Event;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventConfig;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventCallback;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventQueryListener;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.TriggerEvent;
import com.oplus.deepthinker.sdk.app.aidl.proton.appactionpredict.PredictAABResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.appactionpredict.PredictResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.DeepSleepPredictResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.DeepSleepPredictResultWithPercentile;
import com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.SleepRecord;
import com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.TotalPredictResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.intentdecision.IntentResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.intentdecision.ServiceResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.periodtopapps.PeriodTopAppsResult;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.IAppDomainManager;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.IDeviceDomainManager;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.IEnvironmentDomainManager;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.IPlatformMananger;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.IUserDomainManager;
import com.oplus.deepthinker.sdk.app.feature.eventassociation.PerformanceOfLongCharging;
import com.oplus.deepthinker.sdk.app.userprofile.labels.AppTypePreferenceLabel;
import com.oplus.deepthinker.sdk.app.userprofile.labels.CommuteLabel;
import com.oplus.deepthinker.sdk.app.userprofile.labels.LeaveHomeLabel;
import com.oplus.deepthinker.sdk.app.userprofile.labels.NotificationLabel;
import com.oplus.deepthinker.sdk.app.userprofile.labels.ResidenceLabel;
import com.oplus.deepthinker.sdk.app.userprofile.labels.SleepHabitLabel;
import com.oplus.deepthinker.sdk.app.userprofile.labels.StayLocationLabel;
import com.oplus.deepthinker.sdk.app.userprofile.labels.WifiLocationLabel;
import i6.IDeepThinkerBridge;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public interface IOplusDeepThinkerManager {
    public static final String SERVICE_ACTION = "deepthinker.intent.action.BIND_INTERFACE_SERVER";
    public static final String SERVICE_PKG = "com.oplus.deepthinker";

    static Intent getServiceIntent() {
        Intent intent = new Intent();
        intent.setAction("deepthinker.intent.action.BIND_INTERFACE_SERVER");
        intent.setPackage(SERVICE_PKG);
        return intent;
    }

    default Map<String, Integer> checkPermission(int i10, String str) {
        return null;
    }

    default int getAlgorithmPlatformVersion() {
        return -1;
    }

    default Map<String, Map<Double, Double>> getAllAppActivePeriod(int i10) {
        return null;
    }

    default Map<String, Integer> getAllAppImportance(Map<String, Integer> map) {
        return null;
    }

    default Map<String, Integer> getAllAppImportanceLocked(Map<String, Integer> map) {
        return null;
    }

    default Map<String, NotificationLabel.Details> getAllNotificationLabelDetail() {
        return null;
    }

    default Map<String, Integer> getAllNotificationLabelResult() {
        return null;
    }

    default List<PeriodTopAppsResult> getAllPeriodDurationTopApps(int i10) {
        return null;
    }

    default List<PeriodTopAppsResult> getAllPeriodFrequencyTopApps(int i10) {
        return null;
    }

    default Map<Double, Double> getAppActivePeriod(int i10, String str) {
        return null;
    }

    @Deprecated
    default Map<String, Integer> getAppBgVectorLabelResult() {
        return null;
    }

    default IAppDomainManager getAppDomainManager() {
        return null;
    }

    default PredictResult getAppPredictResult(String str) {
        return null;
    }

    default List<PredictResult> getAppPredictResultMap(String str) {
        return null;
    }

    default List<String> getAppQueueSortedByComplex() {
        return null;
    }

    default List<String> getAppQueueSortedByCount() {
        return null;
    }

    default List<String> getAppQueueSortedByTime() {
        return null;
    }

    default int getAppType(String str) {
        return -1;
    }

    default Map getAppTypeMap(List<String> list) {
        return null;
    }

    default List<Event> getAvailableEvent() {
        return null;
    }

    default Map<Integer, Double> getCandidateTaxiApp() {
        return new HashMap();
    }

    default PeriodTopAppsResult getCertainPeriodDurationTopApps(float f10, int i10) {
        return null;
    }

    default PeriodTopAppsResult getCertainPeriodFrequencyTopApps(float f10, int i10) {
        return null;
    }

    default boolean getChargeStatus(Bundle bundle) {
        return false;
    }

    default CommuteLabel getCommuteLabel() {
        return null;
    }

    default List<ResidenceLabel> getCompanyLabels() {
        return null;
    }

    default DeepSleepPredictResult getDeepSleepPredictResult() {
        return null;
    }

    default DeepSleepPredictResultWithPercentile getDeepSleepPredictResultWithPercentile() {
        return null;
    }

    default TotalPredictResult getDeepSleepTotalPredictResult() {
        return null;
    }

    default IDeepThinkerBridge getDeepThinkerBridge() {
        return null;
    }

    default IDeviceDomainManager getDeviceDomainManager() {
        return null;
    }

    default IEnvironmentDomainManager getEnvironmentDomainManager() {
        return null;
    }

    default String getHabitForTime(int i10) {
        return "";
    }

    default int getHabitStatus(Bundle bundle) {
        return -1;
    }

    default List<ResidenceLabel> getHomeLabels() {
        return null;
    }

    default int getIdleScreenResultInLongTime() {
        return -1;
    }

    default int getIdleScreenResultInMiddleTime() {
        return -1;
    }

    default int getIdleScreenResultInShortTime() {
        return -1;
    }

    default int getInOutDoorState() {
        return 0;
    }

    default int getInOutDoorState(Bundle bundle) {
        return 0;
    }

    default SleepRecord getLastDeepSleepRecord() {
        return null;
    }

    default LeaveHomeLabel getLeaveHomeHabitResult() {
        return null;
    }

    default PerformanceOfLongCharging getPerformanceForLongCharging(int i10, float f10, int i11) {
        return null;
    }

    default IPlatformMananger getPlatformManager() {
        return null;
    }

    default PredictAABResult getPredictAABResult() {
        return null;
    }

    default DeepSleepPredictResult getPredictResultWithFeedBack() {
        return null;
    }

    default AppTypePreferenceLabel getRecentAppTypePreference() {
        return null;
    }

    default List<ResidenceLabel> getResidenceLabels() {
        return null;
    }

    default SleepHabitLabel getSleepHabitResult() {
        return null;
    }

    default List<String> getSmartGpsBssidList() {
        return null;
    }

    @Deprecated
    default List<StayLocationLabel> getStayLocationLabels() {
        return null;
    }

    default IUserDomainManager getUserDomainManager() {
        return null;
    }

    default List<WifiLocationLabel> getWifiLocationLabels() {
        return null;
    }

    default boolean isAvailableEvent(Event event) {
        return false;
    }

    default boolean isMeituanLover(Bundle bundle) {
        return false;
    }

    default IntentResult queryAwarenessIntent(int i10) {
        return null;
    }

    default ServiceResult queryAwarenessService(int i10) {
        return null;
    }

    default void queryEvent(Event event, IEventQueryListener iEventQueryListener) {
    }

    default void queryEvents(EventConfig eventConfig, IEventQueryListener iEventQueryListener) {
    }

    default int registerEventCallback(IEventCallback iEventCallback, EventConfig eventConfig) {
        return 0;
    }

    default IntentResult sortAwarenessIntent(IntentResult intentResult, boolean z10) {
        return null;
    }

    default ServiceResult sortAwarenessService(ServiceResult serviceResult, boolean z10) {
        return null;
    }

    default void triggerHookEvent(int i10, int i11, String str, Bundle bundle) {
    }

    default void triggerHookEvent(TriggerEvent triggerEvent) {
    }

    default void triggerHookEventAsync(Handler handler, int i10, int i11, String str, Bundle bundle) {
    }

    default int unregisterEventCallback(IEventCallback iEventCallback) {
        return 0;
    }

    default int unregisterEventCallbackWithArgs(IEventCallback iEventCallback, EventConfig eventConfig) {
        return 0;
    }
}
