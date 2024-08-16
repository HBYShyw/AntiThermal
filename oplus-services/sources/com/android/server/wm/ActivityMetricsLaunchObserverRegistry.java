package com.android.server.wm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ActivityMetricsLaunchObserverRegistry {
    void registerLaunchObserver(ActivityMetricsLaunchObserver activityMetricsLaunchObserver);

    void unregisterLaunchObserver(ActivityMetricsLaunchObserver activityMetricsLaunchObserver);
}
