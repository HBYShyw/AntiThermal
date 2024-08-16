package com.android.internal.os;

import android.os.IBinder;
import android.os.ServiceManager;
import android.util.Slog;
import com.android.internal.os.IOplusBatteryStatsService;

/* loaded from: classes.dex */
public class OplusBatteryStatsManager {
    private static final String TAG = "OplusBatteryStatsServiceManager";
    private IOplusBatteryStatsService mOplusBatteryStatsService;

    private void getBatteryDataService() {
        IBinder binder = ServiceManager.getService("oplus_battery_stats_service");
        if (binder == null) {
            Slog.d(TAG, "service oplus_battery_stats_service is null");
        } else if (this.mOplusBatteryStatsService != IOplusBatteryStatsService.Stub.asInterface(binder)) {
            this.mOplusBatteryStatsService = IOplusBatteryStatsService.Stub.asInterface(binder);
        }
    }

    public UidSipper[] getUidSipper(int[] uid, long elapsedRealtimeMs, int which, boolean updateCpu, boolean updateModem) {
        getBatteryDataService();
        IOplusBatteryStatsService iOplusBatteryStatsService = this.mOplusBatteryStatsService;
        if (iOplusBatteryStatsService == null) {
            Slog.d(TAG, "mOplusBatteryStatsService is null");
            return null;
        }
        try {
            return iOplusBatteryStatsService.getUidSipper(uid, elapsedRealtimeMs, which, updateCpu, updateModem);
        } catch (Exception e) {
            Slog.e(TAG, "err = " + e.toString());
            return null;
        }
    }
}
