package com.android.server.accessibility.magnification;

import android.view.MotionEvent;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class MotionEventInfo {
    public MotionEvent mEvent;
    public int mPolicyFlags;
    public MotionEvent mRawEvent;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static MotionEventInfo obtain(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        return new MotionEventInfo(MotionEvent.obtain(motionEvent), MotionEvent.obtain(motionEvent2), i);
    }

    MotionEventInfo(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        this.mEvent = motionEvent;
        this.mRawEvent = motionEvent2;
        this.mPolicyFlags = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void recycle() {
        this.mEvent = recycleAndNullify(this.mEvent);
        this.mRawEvent = recycleAndNullify(this.mRawEvent);
    }

    public String toString() {
        return MotionEvent.actionToString(this.mEvent.getAction()).replace("ACTION_", "");
    }

    private static MotionEvent recycleAndNullify(MotionEvent motionEvent) {
        if (motionEvent == null) {
            return null;
        }
        motionEvent.recycle();
        return null;
    }
}
