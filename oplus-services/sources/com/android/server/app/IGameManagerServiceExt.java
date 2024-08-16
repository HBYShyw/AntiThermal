package com.android.server.app;

import android.content.Context;
import android.os.Looper;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.server.SystemService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IGameManagerServiceExt {
    default Looper getLooper() {
        return null;
    }

    default void init(Context context, GameManagerService gameManagerService) {
    }

    default boolean isGamePadInterceptEnable() {
        return false;
    }

    default boolean isGamePkg(String str) {
        return false;
    }

    default boolean isInGameMode() {
        return false;
    }

    default int notifyBacklightAnimFinished(int i, float f) {
        return 0;
    }

    default void onBootPhase(int i) {
    }

    default void onFGChange(String str, String str2, int i, int i2) {
    }

    default void onPackageChange(String str, String str2) {
    }

    default boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        return false;
    }

    default void onUserUnlocked(SystemService.TargetUser targetUser) {
    }

    default void setLooper(Looper looper) {
    }
}
