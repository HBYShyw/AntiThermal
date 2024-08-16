package com.android.server.wm;

import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.util.Size;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
class LaunchParamsUtil {
    private static final boolean DEBUG = false;
    private static final int DEFAULT_LANDSCAPE_FREEFORM_HEIGHT_DP = 600;
    private static final int DEFAULT_LANDSCAPE_FREEFORM_WIDTH_DP = 1064;
    static final int DEFAULT_PORTRAIT_FREEFORM_HEIGHT_DP = 732;
    static final int DEFAULT_PORTRAIT_FREEFORM_WIDTH_DP = 412;
    private static final int DISPLAY_EDGE_OFFSET_DP = 27;
    private static final String TAG = "ActivityTaskManager";
    private static final Rect TMP_STABLE_BOUNDS = new Rect();

    private LaunchParamsUtil() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void centerBounds(TaskDisplayArea taskDisplayArea, int i, int i2, Rect rect) {
        if (rect.isEmpty()) {
            taskDisplayArea.getStableRect(rect);
        }
        int centerX = rect.centerX() - (i / 2);
        int centerY = rect.centerY() - (i2 / 2);
        rect.set(centerX, centerY, i + centerX, i2 + centerY);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Size getDefaultFreeformSize(ActivityRecord activityRecord, TaskDisplayArea taskDisplayArea, ActivityInfo.WindowLayout windowLayout, int i, Rect rect) {
        float f = taskDisplayArea.getConfiguration().densityDpi / 160.0f;
        int i2 = (int) (((i == 0 ? DEFAULT_LANDSCAPE_FREEFORM_WIDTH_DP : DEFAULT_PORTRAIT_FREEFORM_WIDTH_DP) * f) + 0.5f);
        int i3 = (int) (((i == 0 ? DEFAULT_LANDSCAPE_FREEFORM_HEIGHT_DP : DEFAULT_PORTRAIT_FREEFORM_HEIGHT_DP) * f) + 0.5f);
        int i4 = windowLayout == null ? -1 : windowLayout.minWidth;
        int i5 = windowLayout != null ? windowLayout.minHeight : -1;
        int min = Math.min(rect.width(), rect.height());
        int max = (min * min) / Math.max(rect.width(), rect.height());
        int i6 = i == 0 ? min : max;
        if (i == 0) {
            min = max;
        }
        int min2 = Math.min(i6, Math.max(i2, i4));
        int min3 = Math.min(min, Math.max(i3, i5));
        float max2 = Math.max(min2, min3) / Math.min(min2, min3);
        float minAspectRatio = activityRecord.getMinAspectRatio();
        float maxAspectRatio = activityRecord.info.getMaxAspectRatio();
        if (minAspectRatio < 1.0f || max2 >= minAspectRatio) {
            if (maxAspectRatio >= 1.0f && max2 > maxAspectRatio) {
                if (i == 0) {
                    min3 = (int) ((min2 / maxAspectRatio) + 0.5f);
                } else {
                    min2 = (int) ((min3 / maxAspectRatio) + 0.5f);
                }
            }
        } else if (i == 0) {
            min3 = (int) ((min2 / minAspectRatio) + 0.5f);
        } else {
            min2 = (int) ((min3 / minAspectRatio) + 0.5f);
        }
        return new Size(min2, min3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void adjustBoundsToFitInDisplayArea(TaskDisplayArea taskDisplayArea, int i, ActivityInfo.WindowLayout windowLayout, Rect rect) {
        int i2;
        Rect rect2 = TMP_STABLE_BOUNDS;
        taskDisplayArea.getStableRect(rect2);
        int i3 = (int) (((taskDisplayArea.getConfiguration().densityDpi / 160.0f) * 27.0f) + 0.5f);
        rect2.inset(i3, i3);
        if (rect2.width() < rect.width() || rect2.height() < rect.height()) {
            float min = Math.min(rect2.width() / rect.width(), rect2.height() / rect.height());
            int i4 = windowLayout == null ? -1 : windowLayout.minWidth;
            int i5 = windowLayout != null ? windowLayout.minHeight : -1;
            int max = Math.max(i4, (int) (rect.width() * min));
            int max2 = Math.max(i5, (int) (rect.height() * min));
            if (rect2.width() < max || rect2.height() < max2) {
                if (i == 1) {
                    i2 = rect2.right - max;
                } else {
                    i2 = rect2.left;
                }
                int i6 = rect2.top;
                rect.set(i2, i6, max + i2, max2 + i6);
                return;
            }
            int i7 = rect.left;
            int i8 = rect.top;
            rect.set(i7, i8, max + i7, max2 + i8);
        }
        int i9 = rect.right;
        int i10 = rect2.right;
        int i11 = 0;
        int i12 = (i9 <= i10 && (i9 = rect.left) >= (i10 = rect2.left)) ? 0 : i10 - i9;
        int i13 = rect.top;
        int i14 = rect2.top;
        if (i13 < i14) {
            i11 = i14 - i13;
        } else {
            int i15 = rect.bottom;
            int i16 = rect2.bottom;
            if (i15 > i16) {
                i11 = i16 - i15;
            }
        }
        rect.offset(i12, i11);
    }
}
