package com.oplus.deepthinker.sdk.app;

import android.content.Context;
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
import com.oplus.deepthinker.sdk.app.deepthinkermanager.PlatformManager;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.domainmanager.AppDomainManager;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.domainmanager.DeviceDomainManager;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.domainmanager.EnvironmentDomainManager;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.domainmanager.UserDomainManager;
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
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/* loaded from: classes.dex */
public class OplusDeepThinkerManager implements IOplusDeepThinkerManager {
    private final IAppDomainManager mAppDomainManager;
    private final IDeviceDomainManager mDeviceDomainManager;
    private final IEnvironmentDomainManager mEnvironmentDomainManager;
    private final IPlatformMananger mPlatformManager;
    private ServiceHolder mServiceHolder;
    private final IUserDomainManager mUserDomainManager;

    public OplusDeepThinkerManager(Context context) {
        ServiceHolder serviceHolder = new ServiceHolder();
        this.mServiceHolder = serviceHolder;
        this.mDeviceDomainManager = new DeviceDomainManager(context, serviceHolder);
        this.mEnvironmentDomainManager = new EnvironmentDomainManager(this.mServiceHolder);
        this.mUserDomainManager = new UserDomainManager(this.mServiceHolder);
        this.mAppDomainManager = new AppDomainManager(this.mServiceHolder);
        this.mPlatformManager = new PlatformManager(this.mServiceHolder);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public Map<String, Integer> checkPermission(int i10, String str) {
        return this.mPlatformManager.checkPermission(i10, str);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public int getAlgorithmPlatformVersion() {
        return this.mPlatformManager.getAlgorithmPlatformVersion();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public Map<String, Map<Double, Double>> getAllAppActivePeriod(int i10) {
        return this.mAppDomainManager.getAllAppActivePeriod(i10);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public Map<String, Integer> getAllAppImportance(Map<String, Integer> map) {
        return this.mDeviceDomainManager.getAllAppImportance(map);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public Map<String, Integer> getAllAppImportanceLocked(Map<String, Integer> map) {
        return this.mDeviceDomainManager.getAllAppImportanceLocked(map);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public Map<String, NotificationLabel.Details> getAllNotificationLabelDetail() {
        return this.mUserDomainManager.getAllNotificationLabelDetail();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public Map<String, Integer> getAllNotificationLabelResult() {
        return this.mUserDomainManager.getAllNotificationLabelResult();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public List<PeriodTopAppsResult> getAllPeriodDurationTopApps(int i10) {
        return this.mAppDomainManager.getAllPeriodDurationTopApps(i10);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public List<PeriodTopAppsResult> getAllPeriodFrequencyTopApps(int i10) {
        return this.mAppDomainManager.getAllPeriodFrequencyTopApps(i10);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public Map<Double, Double> getAppActivePeriod(int i10, String str) {
        return this.mAppDomainManager.getAppActivePeriod(i10, str);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public Map<String, Integer> getAppBgVectorLabelResult() {
        return this.mAppDomainManager.getAppBgVectorLabelResult();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public IAppDomainManager getAppDomainManager() {
        return this.mAppDomainManager;
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public PredictResult getAppPredictResult(String str) {
        return this.mAppDomainManager.getAppPredictResult(str);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public List<PredictResult> getAppPredictResultMap(String str) {
        return this.mAppDomainManager.getAppPredictResultMap(str);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public List<String> getAppQueueSortedByComplex() {
        return this.mAppDomainManager.getAppQueueSortedByComplex();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public List<String> getAppQueueSortedByCount() {
        return this.mAppDomainManager.getAppQueueSortedByCount();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public List<String> getAppQueueSortedByTime() {
        return this.mAppDomainManager.getAppQueueSortedByTime();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public int getAppType(String str) {
        return this.mDeviceDomainManager.getAppType(str);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public Map getAppTypeMap(List<String> list) {
        return this.mDeviceDomainManager.getAppTypeMap(list);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public List<Event> getAvailableEvent() {
        return this.mDeviceDomainManager.getAvailableEvent();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public Map<Integer, Double> getCandidateTaxiApp() {
        return this.mUserDomainManager.getCandidateTaxiApp();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public PeriodTopAppsResult getCertainPeriodDurationTopApps(float f10, int i10) {
        return this.mAppDomainManager.getCertainPeriodDurationTopApps(f10, i10);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public PeriodTopAppsResult getCertainPeriodFrequencyTopApps(float f10, int i10) {
        return this.mAppDomainManager.getCertainPeriodFrequencyTopApps(f10, i10);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public boolean getChargeStatus(Bundle bundle) {
        return this.mUserDomainManager.getChargeStatus(bundle);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public CommuteLabel getCommuteLabel() {
        return this.mUserDomainManager.getCommuteLabel();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public List<ResidenceLabel> getCompanyLabels() {
        return this.mEnvironmentDomainManager.getCompanyLabels();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public DeepSleepPredictResult getDeepSleepPredictResult() {
        return this.mUserDomainManager.getDeepSleepPredictResult();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public DeepSleepPredictResultWithPercentile getDeepSleepPredictResultWithPercentile() {
        return this.mUserDomainManager.getDeepSleepPredictResultWithPercentile();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public TotalPredictResult getDeepSleepTotalPredictResult() {
        return this.mUserDomainManager.getDeepSleepTotalPredictResult();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public IDeepThinkerBridge getDeepThinkerBridge() {
        return this.mServiceHolder.getDeepThinkerBridge();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public IDeviceDomainManager getDeviceDomainManager() {
        return this.mDeviceDomainManager;
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public IEnvironmentDomainManager getEnvironmentDomainManager() {
        return this.mEnvironmentDomainManager;
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public String getHabitForTime(int i10) {
        return this.mUserDomainManager.getHabitForTime(i10);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public int getHabitStatus(Bundle bundle) {
        return this.mUserDomainManager.getHabitStatus(bundle);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public List<ResidenceLabel> getHomeLabels() {
        return this.mEnvironmentDomainManager.getHomeLabels();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public int getIdleScreenResultInLongTime() {
        return this.mUserDomainManager.getIdleScreenResultInLongTime();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public int getIdleScreenResultInMiddleTime() {
        return this.mUserDomainManager.getIdleScreenResultInMiddleTime();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public int getIdleScreenResultInShortTime() {
        return this.mUserDomainManager.getIdleScreenResultInShortTime();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public int getInOutDoorState() {
        return this.mEnvironmentDomainManager.getInOutDoorState();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public SleepRecord getLastDeepSleepRecord() {
        return this.mUserDomainManager.getLastDeepSleepRecord();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public LeaveHomeLabel getLeaveHomeHabitResult() {
        return this.mUserDomainManager.getLeaveHomeHabitResult();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public PerformanceOfLongCharging getPerformanceForLongCharging(int i10, float f10, int i11) {
        return this.mUserDomainManager.getPerformanceForLongCharging(i10, f10, i11);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public IPlatformMananger getPlatformManager() {
        return this.mPlatformManager;
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public PredictAABResult getPredictAABResult() {
        return this.mAppDomainManager.getPredictAABResult();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public DeepSleepPredictResult getPredictResultWithFeedBack() {
        return this.mUserDomainManager.getPredictResultWithFeedBack();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public AppTypePreferenceLabel getRecentAppTypePreference() {
        return this.mDeviceDomainManager.getRecentAppTypePreference();
    }

    public IDeepThinkerBridge getRemote() {
        return this.mServiceHolder.getRemote();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public List<ResidenceLabel> getResidenceLabels() {
        return this.mEnvironmentDomainManager.getResidenceLabels();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public SleepHabitLabel getSleepHabitResult() {
        return this.mUserDomainManager.getSleepHabitResult();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public List<String> getSmartGpsBssidList() {
        return this.mEnvironmentDomainManager.getSmartGpsBssidList();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public List<StayLocationLabel> getStayLocationLabels() {
        return this.mEnvironmentDomainManager.getStayLocationLabels();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public IUserDomainManager getUserDomainManager() {
        return this.mUserDomainManager;
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public List<WifiLocationLabel> getWifiLocationLabels() {
        return this.mEnvironmentDomainManager.getWifiLocationLabels();
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public boolean isAvailableEvent(Event event) {
        return this.mDeviceDomainManager.isAvailableEvent(event);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public boolean isMeituanLover(Bundle bundle) {
        return this.mUserDomainManager.isMeituanLover(bundle);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public IntentResult queryAwarenessIntent(int i10) {
        return this.mUserDomainManager.queryAwarenessIntent(i10);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public ServiceResult queryAwarenessService(int i10) {
        return this.mUserDomainManager.queryAwarenessService(i10);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public void queryEvent(Event event, IEventQueryListener iEventQueryListener) {
        this.mDeviceDomainManager.queryEvent(event, iEventQueryListener);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public void queryEvents(EventConfig eventConfig, IEventQueryListener iEventQueryListener) {
        this.mDeviceDomainManager.queryEvents(eventConfig, iEventQueryListener);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public int registerEventCallback(IEventCallback iEventCallback, EventConfig eventConfig) {
        return this.mDeviceDomainManager.registerEventCallback(iEventCallback, eventConfig);
    }

    public void setRemote(IDeepThinkerBridge iDeepThinkerBridge) {
        this.mServiceHolder.setRemote(iDeepThinkerBridge);
    }

    public void setRemoteGetter(Supplier<IDeepThinkerBridge> supplier) {
        this.mServiceHolder.setRemoteGetter(supplier);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public IntentResult sortAwarenessIntent(IntentResult intentResult, boolean z10) {
        return this.mUserDomainManager.sortAwarenessIntent(intentResult, z10);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public ServiceResult sortAwarenessService(ServiceResult serviceResult, boolean z10) {
        return this.mUserDomainManager.sortAwarenessService(serviceResult, z10);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public void triggerHookEvent(TriggerEvent triggerEvent) {
        this.mDeviceDomainManager.triggerHookEvent(triggerEvent);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public void triggerHookEventAsync(Handler handler, int i10, int i11, String str, Bundle bundle) {
        this.mDeviceDomainManager.triggerHookEventAsync(handler, i10, i11, str, bundle);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public int unregisterEventCallback(IEventCallback iEventCallback) {
        return this.mDeviceDomainManager.unregisterEventCallback(iEventCallback);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public int unregisterEventCallbackWithArgs(IEventCallback iEventCallback, EventConfig eventConfig) {
        return this.mDeviceDomainManager.unregisterEventCallbackWithArgs(iEventCallback, eventConfig);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public int getInOutDoorState(Bundle bundle) {
        return this.mEnvironmentDomainManager.getInOutDoorState(bundle);
    }

    @Override // com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager
    public void triggerHookEvent(int i10, int i11, String str, Bundle bundle) {
        this.mDeviceDomainManager.triggerHookEvent(i10, i11, str, bundle);
    }
}
