package com.oplus.screencast;

import android.os.Bundle;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusScreenCastContentManager {
    public static final String DESCRIPTOR = "android.app.IActivityTaskManager";
    public static final int GET_SCREEN_CAST_MODE = 30004;
    public static final int REGISTER_SCREEN_CAST_OBSERVER = 30005;
    public static final int REQUEST_SCREEN_CAST_MODE = 30002;
    public static final int RESET_SCREEN_CAST_MODE = 30003;
    public static final int UNREGISTER_SCREEN_CAST_OBSERVER = 30006;

    default boolean requestScreenCastContentMode(int castMode, int castFlag) throws RemoteException {
        return false;
    }

    default boolean requestScreenCastContentMode(int castMode, int castFlag, Bundle options) throws RemoteException {
        return false;
    }

    default void resetScreenCastContentMode() throws RemoteException {
    }

    default OplusScreenCastInfo getScreenCastContentMode() throws RemoteException {
        return null;
    }

    default boolean registerScreenCastStateObserver(String pkgName, IOplusScreenCastStateObserver observer) {
        return false;
    }

    default boolean unregisterScreenCastStateObserver(String pkgName, IOplusScreenCastStateObserver observer) {
        return false;
    }
}
