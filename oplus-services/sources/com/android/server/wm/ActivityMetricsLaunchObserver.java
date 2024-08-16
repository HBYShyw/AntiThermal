package com.android.server.wm;

import android.content.ComponentName;
import android.content.Intent;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ActivityMetricsLaunchObserver {
    public static final int TEMPERATURE_COLD = 1;
    public static final int TEMPERATURE_HOT = 3;
    public static final int TEMPERATURE_WARM = 2;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    @interface Temperature {
    }

    public void onActivityLaunchCancelled(long j) {
    }

    public void onActivityLaunchFinished(long j, ComponentName componentName, long j2) {
    }

    public void onActivityLaunched(long j, ComponentName componentName, int i) {
    }

    public void onIntentFailed(long j) {
    }

    public void onIntentStarted(Intent intent, long j) {
    }

    public void onReportFullyDrawn(long j, long j2) {
    }
}
