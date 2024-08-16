package com.android.server.accessibility.magnification;

import android.R;
import android.content.Context;
import android.view.ViewConfiguration;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
class MagnificationGestureMatcher {
    private static final int GESTURE_BASE = 100;
    public static final int GESTURE_SINGLE_TAP = 103;
    public static final int GESTURE_SINGLE_TAP_AND_HOLD = 104;
    public static final int GESTURE_SWIPE = 102;
    public static final int GESTURE_TRIPLE_TAP = 105;
    public static final int GESTURE_TRIPLE_TAP_AND_HOLD = 106;
    public static final int GESTURE_TWO_FINGERS_DOWN_OR_SWIPE = 101;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    @interface GestureId {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String gestureIdToString(int i) {
        switch (i) {
            case 101:
                return "GESTURE_TWO_FINGERS_DOWN_OR_SWIPE";
            case 102:
                return "GESTURE_SWIPE";
            case 103:
                return "GESTURE_SINGLE_TAP";
            case 104:
                return "GESTURE_SINGLE_TAP_AND_HOLD";
            case 105:
                return "GESTURE_TRIPLE_TAP";
            case 106:
                return "GESTURE_TRIPLE_TAP_AND_HOLD";
            default:
                return "none";
        }
    }

    MagnificationGestureMatcher() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getMagnificationMultiTapTimeout(Context context) {
        return ViewConfiguration.getDoubleTapTimeout() + context.getResources().getInteger(R.integer.leanback_setup_translation_content_resting_point_v4);
    }
}
