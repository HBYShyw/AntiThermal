package com.oplus.deepthinker.sdk.app.deepthinkermanager;

import com.oplus.deepthinker.sdk.app.ServiceStateObserver;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public interface IPlatformMananger {
    default int availableState(int i10, String str) {
        return -1;
    }

    default List capability() {
        return null;
    }

    default Map<String, Integer> checkPermission(int i10, String str) {
        return null;
    }

    default int getAlgorithmPlatformVersion() {
        return -1;
    }

    default int getServiceState() {
        return -1;
    }

    default void registerServiceStateObserver(ServiceStateObserver serviceStateObserver) {
    }

    default void requestGrantPermission(String str) {
    }
}
