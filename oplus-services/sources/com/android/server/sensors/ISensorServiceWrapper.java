package com.android.server.sensors;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ISensorServiceWrapper {
    default void cleanUpProxEventsInternal() {
    }

    default long[] getProximityEventsInternal() {
        return null;
    }

    default String[] getProximityOwnerInternal() {
        return null;
    }

    default String[] getUltrasonicProximityUsage() {
        return null;
    }

    default ISensorServiceExt getExtImpl() {
        return new ISensorServiceExt() { // from class: com.android.server.sensors.ISensorServiceWrapper.1
        };
    }
}
