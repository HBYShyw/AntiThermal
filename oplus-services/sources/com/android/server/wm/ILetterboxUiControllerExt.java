package com.android.server.wm;

import android.graphics.Point;
import android.graphics.Rect;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ILetterboxUiControllerExt {
    default boolean shouldUseBlackLetterboxBackground(ActivityRecord activityRecord) {
        return false;
    }

    default void interceptLayoutLetterbox(Rect rect, Rect rect2, Point point, WindowState windowState, Letterbox letterbox) {
        letterbox.layout(rect, rect2, point);
    }
}
