package com.android.server.display;

import android.content.Context;
import android.hardware.display.DisplayManagerInternal;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.view.DisplayInfo;
import com.android.server.display.DisplayManagerService;
import com.android.server.wm.WindowManagerInternal;
import java.io.PrintWriter;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IDisplayManagerServiceExt {
    default void addDisplayPowerControllerLocked(LogicalDisplay logicalDisplay, DisplayPowerController displayPowerController) {
    }

    default boolean addProxyBinder(IBinder iBinder, int i, int i2) {
        return false;
    }

    default void adjustDisplayPowerRequest(DisplayManagerInternal.DisplayPowerRequest displayPowerRequest, int i) {
    }

    default boolean dynamicallyConfigDebug(PrintWriter printWriter, String[] strArr) {
        return false;
    }

    default void enterDCMode(WindowManagerInternal windowManagerInternal, float f) {
    }

    default DisplayInfo getBacklightTypeDisplayInfo(DisplayInfo displayInfo, int i) {
        return null;
    }

    default DisplayInfo getZoomModeDisplayInfo(DisplayInfo displayInfo, int i, int i2) {
        return null;
    }

    default void handleLogicalDisplayAddedLocked(LogicalDisplay logicalDisplay) {
    }

    default void handleLogicalDisplayChangedLocked(LogicalDisplay logicalDisplay) {
    }

    default void handleLogicalDisplayDeviceStateTransitionLocked(LogicalDisplay logicalDisplay) {
    }

    default void handleLogicalDisplayRemovedLocked(LogicalDisplay logicalDisplay) {
    }

    default void handleLogicalDisplaySwappedLocked(LogicalDisplay logicalDisplay) {
    }

    default void hookUpdateScreenRecorderState(int i, String str, boolean z) {
    }

    default void init(Context context) {
    }

    default void initPowerManagement(DisplayPowerController displayPowerController) {
    }

    default boolean isBoostDisplayRefreshRateForAnim() {
        return false;
    }

    default void notifyDisplayModeSpecsChanged(int i, float f) {
    }

    default void onBootComplete(int i, DisplayPowerController displayPowerController, DisplayManagerService.SyncRoot syncRoot) {
    }

    default boolean onDisplayStateChange(int i, int i2, int i3, LogicalDisplayMapper logicalDisplayMapper) {
        return false;
    }

    default void onStart(Binder binder) {
    }

    default void onSystemReady() {
    }

    default float oplusAdjustBrightness(float f) {
        return f;
    }

    default boolean oplusAutoBrightnessAdjustmentSkipCheck(Context context, int i) {
        return false;
    }

    default boolean removeProxyBinder(IBinder iBinder, int i) {
        return false;
    }

    default boolean requestPowerState(LogicalDisplayMapper logicalDisplayMapper, int i, DisplayManagerInternal.DisplayPowerRequest displayPowerRequest, boolean z) {
        return false;
    }

    default void scheduleTraversalLocked(boolean z) {
    }

    default void setActivityPreloadDisplayAdapter(ArrayList<DisplayAdapter> arrayList) {
    }

    default boolean setBrightnessByAccessibility(int i) {
        return false;
    }

    default void setBrightnessInfoUid(int i) {
    }

    default void setBrightnessUid(int i) {
    }

    default void setLogicalDisplayMapper(LogicalDisplayMapper logicalDisplayMapper) {
    }

    default void setSpecBrightness(int i, String str, int i2) {
    }

    default void setTemporaryAutoBrightnessAdjustment(float f) {
    }

    default void setUiHandler(Handler handler) {
    }
}
