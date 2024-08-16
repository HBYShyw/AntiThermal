package com.android.server.wallpaper;

import android.app.ActivityManager;
import android.app.IWallpaperManagerCallback;
import android.content.ComponentName;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.RemoteCallbackList;
import android.util.SparseArray;
import com.android.server.wallpaper.WallpaperManagerService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IWallpaperManagerServiceWrapper {
    default boolean changingToSame(ComponentName componentName, WallpaperData wallpaperData) {
        return false;
    }

    default void clearWallpaperComponentLocked(WallpaperData wallpaperData) {
    }

    default void detachWallpaperLocked(WallpaperData wallpaperData) {
    }

    default WallpaperData findWallpaperAtDisplay(int i, int i2) {
        return null;
    }

    default ActivityManager getActivityManager() {
        return null;
    }

    default Context getContext() {
        return null;
    }

    default int getCurrentUserId() {
        return -10000;
    }

    default ComponentName getDefaultWallpaperComponent() {
        return null;
    }

    default DisplayManager getDisplayManager() {
        return null;
    }

    default IWallpaperManagerServiceExt getExtImpl() {
        return null;
    }

    default WallpaperData getFallbackWallpaper() {
        return null;
    }

    default String[] getPerUserFiles() {
        return new String[0];
    }

    default boolean hasPermission(String str) {
        return false;
    }

    default void initDisplayState(WallpaperManagerService.WallpaperConnection wallpaperConnection) {
    }

    default void loadSettingsLocked(int i, boolean z, int i2) {
    }

    default void notifyCallbacksLocked(WallpaperData wallpaperData) {
    }

    default void notifyLockWallpaperChanged() {
    }

    default void notifyWallpaperColorsChangedOnDisplay(WallpaperData wallpaperData, int i, int i2) {
    }

    default void removeDisplayData(int i) {
    }

    default void setWallpaperComponent(ComponentName componentName, int i) {
    }

    default void updateFallbackConnection() {
    }

    default void updateLogState(boolean z) {
    }

    default Object getLock() {
        return new Object();
    }

    default ComponentName getImageWallpaper() {
        return new ComponentName("", "");
    }

    default SparseArray<WallpaperData> getWallpaperMap() {
        return new SparseArray<>();
    }

    default SparseArray<WallpaperData> getLockWallpaperMap() {
        return new SparseArray<>();
    }

    default RemoteCallbackList<IWallpaperManagerCallback> getWallpaperCallbacks(WallpaperData wallpaperData) {
        return new RemoteCallbackList<>();
    }

    default SparseArray<WallpaperManagerService.DisplayConnector> getDisplayConnectors(WallpaperManagerService.WallpaperConnection wallpaperConnection) {
        return new SparseArray<>();
    }
}
