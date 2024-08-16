package com.android.server.timezonedetector;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
interface DeviceActivityMonitor extends Dumpable {

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface Listener {
        void onFlightComplete();
    }

    void addListener(Listener listener);
}
