package com.android.server.camera;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface ICameraServiceProxyExt {
    public static final int MSG_FLOAT_WINDOW_SHOW = 2002;
    public static final int MSG_RIGISTER_OBSERVER = 2000;

    default boolean checkCameraFloatWindow() {
        return false;
    }

    default void extendNotifyCameraState(int i, String str, int i2, String str2) {
    }

    default boolean getIsRegistered() {
        return false;
    }

    default boolean getNfcSwitchState() {
        return false;
    }

    default int getRegisterTimes() {
        return 0;
    }

    default void registerAppSwitchObserver() {
    }

    default void setNfcSwitchState(boolean z) {
    }

    default void unregisterAppSwitchObserver() {
    }
}
