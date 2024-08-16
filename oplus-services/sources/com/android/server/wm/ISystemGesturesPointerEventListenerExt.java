package com.android.server.wm;

import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ISystemGesturesPointerEventListenerExt {
    default boolean checkSwipeFromBottom(float f, float f2, int i) {
        return true;
    }

    default Handler getOplusUiHandler(Handler handler) {
        return handler;
    }

    default void hookOnGlobalFlingGesture(int i) {
    }

    default boolean hookSwipeFromTop(float f, float f2) {
        return false;
    }

    default boolean inSplitHandleRegion(MotionEvent motionEvent) {
        return false;
    }

    default void init(Context context) {
    }

    default boolean isOnePuttHandleRegion(MotionEvent motionEvent) {
        return false;
    }

    default void notifyFlingGestureStatus(int i) {
    }

    default void notifyMotionDown() {
    }

    default void notifyMotionUpOrCancel() {
    }

    default void notifyScrollGestureStatus() {
    }

    default void updateDefaultSwipeDistance() {
    }
}
