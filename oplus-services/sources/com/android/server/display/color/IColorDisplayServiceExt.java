package com.android.server.display.color;

import android.content.Context;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IColorDisplayServiceExt {
    default int getColorMode() {
        return 0;
    }

    default List<String> getForceWcgPackage() {
        return null;
    }

    default int getWCGModeForAPP(String str) {
        return 0;
    }

    default void init(Context context) {
    }

    default boolean isSupportWCGManager() {
        return false;
    }

    default void onBootComplete() {
    }

    default void onSetUp(int i) {
    }

    default void onTearDown() {
    }

    default void setColorMatrix(int i, float[] fArr, DisplayTransformManager displayTransformManager) {
    }
}
