package com.android.server.companion;

import android.util.ArrayMap;
import com.android.internal.util.FrameworkStatsLog;
import java.util.Collections;
import java.util.Map;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class MetricUtils {
    private static final Map<String, Integer> METRIC_DEVICE_PROFILE;

    MetricUtils() {
    }

    static {
        ArrayMap arrayMap = new ArrayMap();
        arrayMap.put(null, 0);
        arrayMap.put("android.app.role.COMPANION_DEVICE_WATCH", 1);
        arrayMap.put("android.app.role.COMPANION_DEVICE_APP_STREAMING", 2);
        arrayMap.put("android.app.role.SYSTEM_AUTOMOTIVE_PROJECTION", 3);
        arrayMap.put("android.app.role.COMPANION_DEVICE_COMPUTER", 4);
        arrayMap.put("android.app.role.COMPANION_DEVICE_GLASSES", 5);
        arrayMap.put("android.app.role.COMPANION_DEVICE_NEARBY_DEVICE_STREAMING", 6);
        METRIC_DEVICE_PROFILE = Collections.unmodifiableMap(arrayMap);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void logCreateAssociation(String str) {
        FrameworkStatsLog.write(FrameworkStatsLog.CDM_ASSOCIATION_ACTION, 1, METRIC_DEVICE_PROFILE.get(str).intValue());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void logRemoveAssociation(String str) {
        FrameworkStatsLog.write(FrameworkStatsLog.CDM_ASSOCIATION_ACTION, 2, METRIC_DEVICE_PROFILE.get(str).intValue());
    }
}
