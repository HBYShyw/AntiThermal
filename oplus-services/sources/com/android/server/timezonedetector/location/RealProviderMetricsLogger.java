package com.android.server.timezonedetector.location;

import com.android.internal.util.FrameworkStatsLog;
import com.android.server.timezonedetector.location.LocationTimeZoneProvider;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class RealProviderMetricsLogger implements LocationTimeZoneProvider.ProviderMetricsLogger {
    private final int mProviderIndex;

    private static int metricsProviderState(int i) {
        switch (i) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            default:
                return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RealProviderMetricsLogger(int i) {
        this.mProviderIndex = i;
    }

    @Override // com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderMetricsLogger
    public void onProviderStateChanged(int i) {
        FrameworkStatsLog.write(359, this.mProviderIndex, metricsProviderState(i));
    }
}
