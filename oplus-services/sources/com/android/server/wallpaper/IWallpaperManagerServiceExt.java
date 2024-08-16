package com.android.server.wallpaper;

import android.app.ActivityManager;
import android.app.IWallpaperManagerCallback;
import android.app.WallpaperInfo;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.ImageDecoder;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.os.IRemoteCallback;
import android.util.SparseArray;
import android.view.Display;
import com.android.server.wallpaper.WallpaperManagerService;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IWallpaperManagerServiceExt {
    public static final int[] ALL_PHYSICAL_DISPLAY_IDS = {1, 2};
    public static final int PHYSICAL_DISPLAY_MAIN = 1;
    public static final int PHYSICAL_DISPLAY_SUB = 2;

    default boolean bindSharedWallpaperComponentLocked(ComponentName componentName, boolean z, boolean z2, WallpaperData wallpaperData, IRemoteCallback iRemoteCallback) {
        return false;
    }

    default boolean bindWallpaperComponentCheck(ComponentName componentName, WallpaperData wallpaperData, boolean z) {
        return false;
    }

    default boolean bindWallpaperComponentLocked(ComponentName componentName, boolean z, boolean z2, WallpaperData wallpaperData, IRemoteCallback iRemoteCallback, int i) {
        return false;
    }

    default boolean bindWallpaperComponentLockedForAllPhysicalDisplays(ComponentName componentName, boolean z, boolean z2, WallpaperData wallpaperData, IRemoteCallback iRemoteCallback) {
        return false;
    }

    default boolean bindWallpaperComponentOnImageChangedLocked(WallpaperData wallpaperData, IRemoteCallback iRemoteCallback) {
        return false;
    }

    default boolean canNotifySysChangedLocked(WallpaperData wallpaperData, boolean z) {
        return true;
    }

    default void checkSysChangedWhenSysAndLockIsLive(WallpaperData wallpaperData, SparseArray<WallpaperData> sparseArray) {
    }

    default void clearFlipClockTextColorIfNeed(Context context, int i, int i2) {
    }

    default void clearFlipClockTextStyleIfNeed(Context context, int i, int i2) {
    }

    default void clearSubDisplayWallpaperComponent(int i, int i2) {
    }

    default boolean clearWallpaperComponentLockedOnPackageUpdated(WallpaperData wallpaperData) {
        return false;
    }

    default boolean clearWallpaperLocked(boolean z, int i, int i2, IRemoteCallback iRemoteCallback, int i3) {
        return false;
    }

    default boolean clearWallpaperLockedForComponent(boolean z, int i, int i2, IRemoteCallback iRemoteCallback, ComponentName componentName) {
        return false;
    }

    default boolean clearWallpaperLockedForMultiPhysicalDisplays(boolean z, int i, int i2, IRemoteCallback iRemoteCallback) {
        return false;
    }

    default void copyComponentNameToOtherPhysicalDisplaysLocked(WallpaperData wallpaperData) {
    }

    default void copyLockWallpaperToOtherPhysicalDisplaysLocked(WallpaperData wallpaperData, boolean z) {
    }

    default void copySysWallpaperToOtherPhysicalDisplaysLocked(WallpaperData wallpaperData) {
    }

    default void copyWallpaperColorsToOtherPhysicalDisplaysLocked(WallpaperData wallpaperData) {
    }

    default boolean delayRebindOnCrashTimeout(WallpaperData wallpaperData, WallpaperManagerService.WallpaperConnection wallpaperConnection) {
        return false;
    }

    default boolean deleteSubDisplayFile(int i, int i2, int i3) {
        return false;
    }

    default void deleteSubDisplayFiles(int i) {
    }

    default boolean detachLastWallpaperLocked(int i, WallpaperData wallpaperData, WallpaperData wallpaperData2) {
        return false;
    }

    default void detachOtherPhysicalDisplaysWallpaper(int i, WallpaperData wallpaperData) {
    }

    default boolean detachSharedWallpaperLocked(WallpaperData wallpaperData) {
        return false;
    }

    default void finalizeSubDisplay() {
    }

    default WallpaperData findWallpaperAtDisplay(int i, int i2, int i3, WallpaperData wallpaperData) {
        return wallpaperData;
    }

    default boolean forEachAvailableDisplayConnector(SparseArray<WallpaperManagerService.DisplayConnector> sparseArray, WallpaperManagerService.WallpaperConnection wallpaperConnection, Consumer<WallpaperManagerService.DisplayConnector> consumer) {
        return false;
    }

    default void forEachPhysicalDisplayWallpaperLocked(int i, int i2, Consumer<WallpaperData> consumer) {
    }

    default int formatWhichPending(int i) {
        return i;
    }

    default int getBindWallpaperServiceFlag(int i, WallpaperData wallpaperData) {
        return i;
    }

    default int getCachePhysicalDisplayId() {
        return 1;
    }

    default int getCurrentPhysicalDisplayIdLocked() {
        return 1;
    }

    default SparseArray<WallpaperData> getCurrentWallpaperMap(int i, SparseArray<WallpaperData> sparseArray) {
        return sparseArray;
    }

    default ComponentName getDefaultWallpaperComponent(Context context, int i) {
        return null;
    }

    default int getDisplayIdFromPhysicalDisplayId(int i, DisplayManager displayManager) {
        return 0;
    }

    default WallpaperInfo getFoldWallpaperInfo(int i, int i2) {
        return null;
    }

    default int getPhysicalDisplayIdFromDisplayIdLocked(int i) {
        return 1;
    }

    default int getPhysicalDisplayIdLocked(int i) {
        return 1;
    }

    default File getRecordFile(WallpaperData wallpaperData, String str, File file) {
        return file;
    }

    default int getServiceUserId(int i, ComponentName componentName, ComponentName componentName2) {
        return i;
    }

    default File getWallpaperDir(int i, int i2, File file) {
        return file;
    }

    default WallpaperData getWallpaperForWallpaperColors(int i, int i2, int i3) {
        return null;
    }

    default SparseArray<WallpaperData> getWallpaperMap(int i, int i2, SparseArray<WallpaperData> sparseArray) {
        return sparseArray;
    }

    default SparseArray<WallpaperData> getWallpaperMap(int i, SparseArray<WallpaperData> sparseArray) {
        return sparseArray;
    }

    default WallpaperData getWallpaperSafeLocked(int i, int i2) {
        return null;
    }

    default WallpaperData getWallpaperSafeLocked(int i, int i2, int i3) {
        return null;
    }

    default int getWhichValue(int i, int i2) {
        return i;
    }

    default int getWhichValue(int i, int[] iArr) {
        return i;
    }

    default boolean hasNamedSubWallpaperForUser(int i, String str) {
        return false;
    }

    default boolean hasSubDisplayWallpaperLocked(int i, int i2, int i3) {
        return false;
    }

    default boolean ignoreFileEventForCopyLocked(WallpaperData wallpaperData, int i) {
        return false;
    }

    default void initCustomizeWallpaper(Context context) {
    }

    default void initExt() {
    }

    default void initOnUserSwitch(int i) {
    }

    default void initSeparateWallpaperForMultiDisplay(Context context) {
    }

    default void initSubDisplayOnLoadSettingsLocked(int i) {
    }

    default void initWallpaperBitmap() {
    }

    default void initWallpaperOnBindWallpaperComponentLocked(WallpaperData wallpaperData) {
    }

    default boolean isAvailableFallbackDisplay(Display display) {
        return true;
    }

    default boolean isAvailableSetWallpaperFlagOnBind(WallpaperData wallpaperData, WallpaperInfo wallpaperInfo) {
        return true;
    }

    default boolean isCurPhysicalDisplayWallpaperChangeLocked(WallpaperData wallpaperData) {
        return true;
    }

    default boolean isCurrentPhysicalDisplayWallpaper(WallpaperData wallpaperData) {
        return true;
    }

    default boolean isCurrentPhysicalDisplayWallpaperChangedLocked(int i) {
        return true;
    }

    default boolean isNotAvailableWhichWithSinglePhysicalDisplayFlag(int i) {
        return (i == 1 || i == 2) ? false : true;
    }

    default boolean isSetWallpaperAllowed(String str, Context context) {
        return false;
    }

    default boolean isUsableDisplay(Display display) {
        return false;
    }

    default boolean isWallpaperSupportBackup(int i, int i2) {
        return false;
    }

    default boolean loadSettingsLocked(int i, boolean z, int i2, int i3) {
        return false;
    }

    default boolean loadSettingsLockedForAllPhysicalDisplay(int i, boolean z) {
        return false;
    }

    default boolean needAttachService(WallpaperData wallpaperData) {
        return true;
    }

    default boolean needDefaultImageWallpaper(Context context, int i) {
        return true;
    }

    default boolean needScaleUp(int i, int i2) {
        return false;
    }

    default boolean notifyCurrentWallpaperCallbacksLocked(int i, int i2) {
        return false;
    }

    default boolean notifyWallpaperColorsChanged(WallpaperData wallpaperData, int i) {
        return false;
    }

    default void onLoadSettingsEnd(WallpaperData wallpaperData) {
    }

    default void onLockWallpaperChanged(Context context, int i) {
    }

    default boolean onServiceAttachedLocked(boolean z, int i, ComponentName componentName) {
        return false;
    }

    default boolean onServiceDisconnected(WallpaperData wallpaperData) {
        return false;
    }

    default void onWrittenEventEnd(WallpaperData wallpaperData) {
    }

    default void onWrittenEventStart(WallpaperData wallpaperData) {
    }

    default boolean rebindWallpaperComponent(ComponentName componentName, int i) {
        return false;
    }

    default void recycleBitmapDecoderAndDeleteCrop(WallpaperData wallpaperData, BitmapRegionDecoder bitmapRegionDecoder) {
    }

    default void registerLogSwitchObserver(Context context) {
    }

    default void registerWallpaperCallbacksToOtherPhysicalDisplays(int i, IWallpaperManagerCallback iWallpaperManagerCallback, WallpaperData wallpaperData) {
    }

    default void removeDisplayData(int i, WallpaperManagerService wallpaperManagerService) {
    }

    default void removeLastWallpaperLocked(WallpaperData wallpaperData) {
    }

    default void resetOnPackageUpdatedLocked() {
    }

    default void resetSubDisplayWallpaperDataOnClearWallpaperLocked(int i, int i2) {
    }

    default void restoreconSubDisplayFiles(int i) {
    }

    default void saveMaximumSizeDimension(int i, int i2) {
    }

    default boolean saveSettingsLocked(int i, int i2) {
        return false;
    }

    default boolean saveSettingsLockedForAffectedPhysicalDisplays(int i, int i2) {
        return false;
    }

    default boolean saveSettingsLockedOnServiceConnected(int i, int i2) {
        return false;
    }

    default boolean setDecoderSampleSize(ImageDecoder imageDecoder, int i, BitmapFactory.Options options) {
        return false;
    }

    default void setFoldWallpaperComponentChecked(ComponentName componentName, String str, int i, int i2) {
    }

    default void setFromForegroundAppLocked(WallpaperData wallpaperData) {
    }

    default void setIsMainDisplayWallpaperChangeLocked(WallpaperData wallpaperData, int i) {
    }

    default boolean setLastWallpaper(int i, WallpaperData wallpaperData, WallpaperData wallpaperData2) {
        return false;
    }

    default boolean setWallpaperComponent(ComponentName componentName, int i) {
        return false;
    }

    default boolean setWallpaperComponent(ComponentName componentName, int i, String str, ActivityManager activityManager) {
        return false;
    }

    default boolean shouldCommetOriginCode() {
        return false;
    }

    default boolean shouldNotifyCallbacks(WallpaperData wallpaperData) {
        return false;
    }

    default void stopSubDisplayObserversLocked(int i) {
    }

    default void subDisplayErrorCheck(int i, int i2, String str) {
    }

    default void switchWallpaperForOtherPhysicalDisplay(int i, boolean z) {
    }

    default boolean unBindServiceForSeparateWallpaper(Context context, WallpaperData wallpaperData) {
        return false;
    }

    default void updateWallpaperBeforeTryingToRebind(WallpaperData wallpaperData) {
    }

    default void updateWallpaperBitmap() {
    }

    default Handler getEventHandler(Context context) {
        return context.getMainThreadHandler();
    }

    default List<WallpaperData> getWallpaperForAllPhysicalDisplay(int i, int i2) {
        return Collections.emptyList();
    }

    default void copySysWallpaperToLockLocked(WallpaperData wallpaperData, boolean z, SparseArray<WallpaperData> sparseArray) {
        if (wallpaperData != null) {
            sparseArray.remove(wallpaperData.userId);
        }
    }

    default WallpaperData newDirectBootAwareFallbackWallpaper(WallpaperData wallpaperData, Supplier<WallpaperData> supplier) {
        return supplier.get();
    }

    default Display[] getDisplays(DisplayManager displayManager, WallpaperData wallpaperData) {
        return displayManager == null ? new Display[0] : displayManager.getDisplays();
    }

    default List<WallpaperData> getWallpapersLocked(int i, SparseArray<WallpaperData> sparseArray) {
        if (sparseArray == null) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList(sparseArray.size());
        for (int i2 = 0; i2 < sparseArray.size(); i2++) {
            arrayList.add(sparseArray.valueAt(i2));
        }
        return arrayList;
    }

    default void removeEngineIfDisconnected(WallpaperManagerService.DisplayConnector displayConnector) {
        if (displayConnector != null) {
            displayConnector.mEngine = null;
        }
    }
}
