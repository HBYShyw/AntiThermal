package com.android.server.usage;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.os.Handler;
import com.android.server.usage.AppIdleHistory;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IAppStandbyControllerExt {
    default void initConstructor(Context context, AppStandbyController appStandbyController, AppIdleHistory appIdleHistory, Handler handler) {
    }

    default boolean interceptReportEvent(UsageEvents.Event event, long j, int i) {
        return false;
    }

    default boolean isCustomizeDozeModeDisabled() {
        return false;
    }

    default boolean isSystemApp(String str, int i) {
        return false;
    }

    default boolean matchGoogleRestrictRule(String str) {
        return false;
    }

    default void printPredict(AppIdleHistory.AppUsageHistory appUsageHistory, String str, int i, boolean z) {
    }

    default void uploadAABPredictInfoWhenReportEvent(AppIdleHistory.AppUsageHistory appUsageHistory, String str, int i, int i2, int i3) {
    }

    default void uploadAABPredictInfoWhenSet(String str, int i, int i2, long j) {
    }
}
