package com.android.server.accessibility.magnification;

import android.content.Context;
import android.os.Handler;
import android.util.MathUtils;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import com.android.server.accessibility.gestures.GestureMatcher;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class TwoFingersDownOrSwipe extends GestureMatcher {
    private final int mDetectionDurationMillis;
    private final int mDoubleTapTimeout;
    private MotionEvent mFirstPointerDown;
    private MotionEvent mSecondPointerDown;
    private final int mSwipeMinDistance;

    /* JADX INFO: Access modifiers changed from: package-private */
    public TwoFingersDownOrSwipe(Context context) {
        super(101, new Handler(context.getMainLooper()), null);
        this.mDetectionDurationMillis = MagnificationGestureMatcher.getMagnificationMultiTapTimeout(context);
        this.mDoubleTapTimeout = ViewConfiguration.getDoubleTapTimeout();
        this.mSwipeMinDistance = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onDown(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        this.mFirstPointerDown = MotionEvent.obtain(motionEvent);
        cancelAfter(this.mDetectionDurationMillis, motionEvent, motionEvent2, i);
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onPointerDown(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        if (this.mFirstPointerDown == null) {
            cancelGesture(motionEvent, motionEvent2, i);
        }
        if (motionEvent.getPointerCount() == 2) {
            this.mSecondPointerDown = MotionEvent.obtain(motionEvent);
            completeAfter(this.mDoubleTapTimeout, motionEvent, motionEvent2, i);
        } else {
            cancelGesture(motionEvent, motionEvent2, i);
        }
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onMove(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        MotionEvent motionEvent3 = this.mFirstPointerDown;
        if (motionEvent3 == null || this.mSecondPointerDown == null) {
            return;
        }
        if (distance(motionEvent3, motionEvent) > this.mSwipeMinDistance) {
            completeGesture(motionEvent, motionEvent2, i);
        } else if (distance(this.mSecondPointerDown, motionEvent) > this.mSwipeMinDistance) {
            completeGesture(motionEvent, motionEvent2, i);
        }
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onPointerUp(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        cancelGesture(motionEvent, motionEvent2, i);
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onUp(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        cancelGesture(motionEvent, motionEvent2, i);
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    public void clear() {
        MotionEvent motionEvent = this.mFirstPointerDown;
        if (motionEvent != null) {
            motionEvent.recycle();
            this.mFirstPointerDown = null;
        }
        MotionEvent motionEvent2 = this.mSecondPointerDown;
        if (motionEvent2 != null) {
            motionEvent2.recycle();
            this.mSecondPointerDown = null;
        }
        super.clear();
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected String getGestureName() {
        return TwoFingersDownOrSwipe.class.getSimpleName();
    }

    private static double distance(MotionEvent motionEvent, MotionEvent motionEvent2) {
        if (motionEvent2.findPointerIndex(motionEvent.getPointerId(motionEvent.getActionIndex())) < 0) {
            return -1.0d;
        }
        return MathUtils.dist(motionEvent.getX(r0), motionEvent.getY(r0), motionEvent2.getX(r1), motionEvent2.getY(r1));
    }
}
