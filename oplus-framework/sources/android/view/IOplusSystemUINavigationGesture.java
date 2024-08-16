package android.view;

import android.common.IOplusCommonFeature;
import android.graphics.Rect;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusSystemUINavigationGesture extends IOplusCommonFeature {
    public static final IOplusSystemUINavigationGesture DEFAULT = new IOplusSystemUINavigationGesture() { // from class: android.view.IOplusSystemUINavigationGesture.1
    };

    default void checkKeyguardAndConfig(String tag) {
    }

    default void handleGestureMotionDown(View view) {
    }

    default void handleGestureConfigCheck() {
    }

    default void setSystemGestureExclusionRegion(List<Rect> rects) {
    }

    default boolean processGestureEvent(MotionEvent event) {
        return false;
    }

    default void unRegisterNavGestureListener() {
    }
}
