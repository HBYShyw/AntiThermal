package com.android.server;

import android.util.ArrayMap;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IDeviceIdleControllerWrapper {
    default void addPowerSaveWhitelistApps(ArrayMap<String, Integer> arrayMap) {
    }

    default int addPowerSaveWhitelistAppsInternal(List<String> list) {
        return 0;
    }

    default boolean getDeepEnabled() {
        return false;
    }

    default boolean getLightEnabled() {
        return false;
    }

    default int getState() {
        return 0;
    }

    default void setActiveReason(int i) {
    }

    default void setDeepEnabled(boolean z) {
    }

    default void setLightEnabled(boolean z) {
    }

    default void setState(int i) {
    }

    default ArrayMap<String, Integer> getPowerSaveWhitelistUserApps() {
        return new ArrayMap<>();
    }
}
