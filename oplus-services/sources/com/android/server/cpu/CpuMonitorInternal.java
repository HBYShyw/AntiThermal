package com.android.server.cpu;

import java.util.concurrent.Executor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class CpuMonitorInternal {

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface CpuAvailabilityCallback {
        void onAvailabilityChanged(CpuAvailabilityInfo cpuAvailabilityInfo);

        void onMonitoringIntervalChanged(long j);
    }

    public abstract void addCpuAvailabilityCallback(Executor executor, CpuAvailabilityMonitoringConfig cpuAvailabilityMonitoringConfig, CpuAvailabilityCallback cpuAvailabilityCallback);

    public abstract void removeCpuAvailabilityCallback(CpuAvailabilityCallback cpuAvailabilityCallback);
}
