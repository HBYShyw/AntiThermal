package com.oplus.deepthinker.sdk.app.deepthinkermanager;

import com.oplus.deepthinker.sdk.app.aidl.proton.appactionpredict.PredictAABResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.appactionpredict.PredictResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.periodtopapps.PeriodTopAppsResult;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public interface IAppDomainManager {
    default Map<String, Map<Double, Double>> getAllAppActivePeriod(int i10) {
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

    default Map<String, Integer> getAppBgVectorLabelResult() {
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

    default PeriodTopAppsResult getCertainPeriodDurationTopApps(float f10, int i10) {
        return null;
    }

    default PeriodTopAppsResult getCertainPeriodFrequencyTopApps(float f10, int i10) {
        return null;
    }

    default PredictAABResult getPredictAABResult() {
        return null;
    }
}
