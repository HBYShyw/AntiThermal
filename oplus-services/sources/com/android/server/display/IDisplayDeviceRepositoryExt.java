package com.android.server.display;

import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IDisplayDeviceRepositoryExt {
    default void handleDisplayDeviceAdded(DisplayDevice displayDevice) {
    }

    default void handleDisplayDeviceAdded(String str, int i) {
    }

    default void handleDisplayDeviceChanged(DisplayDevice displayDevice) {
    }

    default void handleDisplayDeviceRemoved(DisplayDevice displayDevice) {
    }

    default void handleDisplayDeviceRemoved(String str, int i) {
    }

    default boolean interceptDisplayDeviceAdded(List<DisplayDevice> list, DisplayDeviceInfo displayDeviceInfo) {
        return false;
    }

    default void onDisplayDeviceEvent(DisplayDevice displayDevice, int i, long j) {
    }

    default void onDisplayRemoved(DisplayDevice displayDevice) {
    }
}
