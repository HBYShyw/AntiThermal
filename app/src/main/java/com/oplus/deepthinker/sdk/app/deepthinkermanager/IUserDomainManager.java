package com.oplus.deepthinker.sdk.app.deepthinkermanager;

import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.DeepSleepPredictResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.DeepSleepPredictResultWithPercentile;
import com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.SleepRecord;
import com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.TotalPredictResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.intentdecision.IntentResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.intentdecision.ServiceResult;
import com.oplus.deepthinker.sdk.app.feature.eventassociation.PerformanceOfLongCharging;
import com.oplus.deepthinker.sdk.app.userprofile.labels.CommuteLabel;
import com.oplus.deepthinker.sdk.app.userprofile.labels.LeaveHomeLabel;
import com.oplus.deepthinker.sdk.app.userprofile.labels.NotificationLabel;
import com.oplus.deepthinker.sdk.app.userprofile.labels.SleepHabitLabel;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public interface IUserDomainManager {
    default Map<String, NotificationLabel.Details> getAllNotificationLabelDetail() {
        return null;
    }

    default Map<String, Integer> getAllNotificationLabelResult() {
        return null;
    }

    default Map<Integer, Double> getCandidateTaxiApp() {
        return new HashMap();
    }

    default boolean getChargeStatus(Bundle bundle) {
        return false;
    }

    default CommuteLabel getCommuteLabel() {
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

    default String getHabitForTime(int i10) {
        return "";
    }

    default int getHabitStatus(Bundle bundle) {
        return -1;
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

    default SleepRecord getLastDeepSleepRecord() {
        return null;
    }

    default LeaveHomeLabel getLeaveHomeHabitResult() {
        return null;
    }

    default PerformanceOfLongCharging getPerformanceForLongCharging(int i10, float f10, int i11) {
        return null;
    }

    default DeepSleepPredictResult getPredictResultWithFeedBack() {
        return null;
    }

    default SleepHabitLabel getSleepHabitResult() {
        return null;
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

    default IntentResult sortAwarenessIntent(IntentResult intentResult, boolean z10) {
        return null;
    }

    default ServiceResult sortAwarenessService(ServiceResult serviceResult, boolean z10) {
        return null;
    }
}
