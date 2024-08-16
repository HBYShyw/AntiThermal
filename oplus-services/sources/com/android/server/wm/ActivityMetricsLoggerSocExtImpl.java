package com.android.server.wm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ActivityMetricsLoggerSocExtImpl implements IActivityMetricsLoggerSocExt {
    ActivityMetricsLogger mActivityMetricsLogger;

    @Override // com.android.server.wm.IActivityMetricsLoggerSocExt
    public void hookLogAppDisplayed(WindowProcessController windowProcessController, String str, int i, String str2) {
    }

    @Override // com.android.server.wm.IActivityMetricsLoggerSocExt
    public void hookLogAppTransitionFinished(ActivityRecord activityRecord) {
    }

    public ActivityMetricsLoggerSocExtImpl(Object obj) {
        this.mActivityMetricsLogger = (ActivityMetricsLogger) obj;
    }
}
