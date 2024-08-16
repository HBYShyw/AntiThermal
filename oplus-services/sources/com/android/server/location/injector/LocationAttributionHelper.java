package com.android.server.location.injector;

import android.location.util.identity.CallerIdentity;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.server.location.LocationManagerService;
import java.util.Map;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class LocationAttributionHelper {
    private final AppOpsHelper mAppOpsHelper;

    @GuardedBy({"this"})
    private final Map<CallerIdentity, Integer> mAttributions = new ArrayMap();

    @GuardedBy({"this"})
    private final Map<CallerIdentity, Integer> mHighPowerAttributions = new ArrayMap();

    public LocationAttributionHelper(AppOpsHelper appOpsHelper) {
        this.mAppOpsHelper = appOpsHelper;
    }

    public synchronized void reportLocationStart(CallerIdentity callerIdentity) {
        CallerIdentity forAggregation = CallerIdentity.forAggregation(callerIdentity);
        int intValue = this.mAttributions.getOrDefault(forAggregation, 0).intValue();
        if (intValue == 0) {
            if (this.mAppOpsHelper.startOpNoThrow(41, forAggregation)) {
                this.mAttributions.put(forAggregation, 1);
            }
        } else {
            this.mAttributions.put(forAggregation, Integer.valueOf(intValue + 1));
        }
    }

    public synchronized void reportLocationStop(CallerIdentity callerIdentity) {
        CallerIdentity forAggregation = CallerIdentity.forAggregation(callerIdentity);
        int intValue = this.mAttributions.getOrDefault(forAggregation, 0).intValue();
        if (intValue == 1) {
            this.mAttributions.remove(forAggregation);
            this.mAppOpsHelper.finishOp(41, forAggregation);
        } else if (intValue > 1) {
            this.mAttributions.put(forAggregation, Integer.valueOf(intValue - 1));
        }
    }

    public synchronized void reportHighPowerLocationStart(CallerIdentity callerIdentity) {
        CallerIdentity forAggregation = CallerIdentity.forAggregation(callerIdentity);
        int intValue = this.mHighPowerAttributions.getOrDefault(forAggregation, 0).intValue();
        if (intValue == 0) {
            if (LocationManagerService.D) {
                Log.v(LocationManagerService.TAG, "starting high power location attribution for " + forAggregation);
            }
            if (this.mAppOpsHelper.startOpNoThrow(42, forAggregation)) {
                this.mHighPowerAttributions.put(forAggregation, 1);
            }
        } else {
            this.mHighPowerAttributions.put(forAggregation, Integer.valueOf(intValue + 1));
        }
    }

    public synchronized void reportHighPowerLocationStop(CallerIdentity callerIdentity) {
        CallerIdentity forAggregation = CallerIdentity.forAggregation(callerIdentity);
        int intValue = this.mHighPowerAttributions.getOrDefault(forAggregation, 0).intValue();
        if (intValue == 1) {
            if (LocationManagerService.D) {
                Log.v(LocationManagerService.TAG, "stopping high power location attribution for " + forAggregation);
            }
            this.mHighPowerAttributions.remove(forAggregation);
            this.mAppOpsHelper.finishOp(42, forAggregation);
        } else if (intValue > 1) {
            this.mHighPowerAttributions.put(forAggregation, Integer.valueOf(intValue - 1));
        }
    }
}
