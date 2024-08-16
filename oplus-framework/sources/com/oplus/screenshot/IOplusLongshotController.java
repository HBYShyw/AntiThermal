package com.oplus.screenshot;

/* loaded from: classes.dex */
public interface IOplusLongshotController {
    boolean findInfo(OplusLongshotViewInfo oplusLongshotViewInfo);

    default boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent, int oldScrollY, boolean result) {
        return false;
    }

    default boolean isLongshotConnected() {
        return false;
    }

    default int getOverScrollMode(int overScrollMode) {
        return overScrollMode;
    }
}
