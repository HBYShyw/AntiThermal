package com.android.server.om;

import android.content.Context;
import android.content.pm.overlay.OverlayPaths;
import android.os.Handler;
import android.os.IBinder;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IOverlayManagerServiceExt {
    default boolean handleOnSwitchUser(int i, IOverlayPackageCacheExt iOverlayPackageCacheExt) {
        return false;
    }

    default void hookAffectedPackages(List<String> list, boolean z) {
    }

    default void hookAffectedPackages(List<String> list, boolean z, int i, int i2) {
    }

    default void init() {
    }

    default void init(Context context, OverlayManagerService overlayManagerService, Object obj, OverlayManagerServiceImpl overlayManagerServiceImpl, IBinder iBinder) {
    }

    default void initLanguageManager(boolean z, OverlayManagerService overlayManagerService, OverlayManagerServiceImpl overlayManagerServiceImpl, IOverlayPackageCacheExt iOverlayPackageCacheExt, Object obj) {
    }

    default void onStart() {
    }

    default void onSwitchUserWrap(int i, IOverlayPackageCacheExt iOverlayPackageCacheExt) {
    }

    default boolean postAtFrontOfQueue(Handler handler, Runnable runnable, boolean z, int i) {
        return false;
    }

    default void systemReady() {
    }

    default void updateAssetsForSwitchUser(int i, List<String> list) {
    }

    default void updateLanguagePath(String str, int i, Map<String, OverlayPaths> map, Collection<String> collection) {
    }
}
