package com.android.server.display;

import android.content.Context;
import android.os.Handler;
import android.util.SparseArray;
import android.view.DisplayInfo;
import com.android.server.display.layout.Layout;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface ILogicalDisplayMapperExt {
    default boolean avoidRemoveInternalDisplay(int i) {
        return false;
    }

    default void fastFreezeOnWakeup(int i, int i2) {
    }

    default boolean filterSecondaryDisplay(Context context, int i, int i2, int i3) {
        return false;
    }

    default boolean getOverrideState(boolean z, LogicalDisplay logicalDisplay) {
        return z;
    }

    default boolean hasFoldRemapDisplayDisableFeature() {
        return false;
    }

    default void initDvMultiDisplay() {
    }

    default int interceptBaseDeviceState(int i, int i2, boolean z) {
        return i2;
    }

    default boolean isRemapDisabledSecondaryDisplayId(int i) {
        return false;
    }

    default void notifyDisplaySwaped() {
    }

    default void resetPowerModeChanged(LogicalDisplay logicalDisplay) {
    }

    default void screenOnCpuBoost(int i, int i2) {
    }

    default void setDisableDisplayOff(boolean z, DisplayInfo displayInfo, DisplayDevice displayDevice, Handler handler) {
    }

    default void setDisplayLayout(SparseArray<Layout> sparseArray) {
    }

    default void setMainDisplayUniqueId(String str) {
    }

    default void setPowerHandler(Handler handler) {
    }

    default void setUxOnWakeup(int i, int i2) {
    }

    default void transitionToPendingStateLocked() {
    }

    default void updateDvsParam(int i) {
    }

    default boolean updateLogicalDisplaysLocked(LogicalDisplay logicalDisplay) {
        return false;
    }
}
