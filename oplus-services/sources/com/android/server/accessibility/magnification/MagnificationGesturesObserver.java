package com.android.server.accessibility.magnification;

import android.annotation.SuppressLint;
import android.util.Log;
import android.util.Slog;
import android.view.MotionEvent;
import com.android.server.accessibility.gestures.GestureMatcher;
import com.android.server.accessibility.magnification.GesturesObserver;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
class MagnificationGesturesObserver implements GesturesObserver.Listener {
    private final Callback mCallback;
    private List<MotionEventInfo> mDelayedEventQueue;
    private final GesturesObserver mGesturesObserver;
    private long mLastDownEventTime = 0;
    private MotionEvent mLastEvent;
    private static final String LOG_TAG = "MagnificationGesturesObserver";

    @SuppressLint({"LongLogTag"})
    private static final boolean DBG = Log.isLoggable(LOG_TAG, 3);

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Callback {
        void onGestureCancelled(long j, List<MotionEventInfo> list, MotionEvent motionEvent);

        void onGestureCompleted(int i, long j, List<MotionEventInfo> list, MotionEvent motionEvent);

        boolean shouldStopDetection(MotionEvent motionEvent);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MagnificationGesturesObserver(Callback callback, GestureMatcher... gestureMatcherArr) {
        this.mGesturesObserver = new GesturesObserver(this, gestureMatcherArr);
        this.mCallback = callback;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onMotionEvent(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        if (DBG) {
            Slog.d(LOG_TAG, "DetectGesture: event = " + motionEvent);
        }
        cacheDelayedMotionEvent(motionEvent, motionEvent2, i);
        if (this.mCallback.shouldStopDetection(motionEvent)) {
            notifyDetectionCancel();
            return false;
        }
        if (motionEvent.getActionMasked() == 0) {
            this.mLastDownEventTime = motionEvent.getDownTime();
        }
        return this.mGesturesObserver.onMotionEvent(motionEvent, motionEvent2, i);
    }

    @Override // com.android.server.accessibility.magnification.GesturesObserver.Listener
    public void onGestureCompleted(int i, MotionEvent motionEvent, MotionEvent motionEvent2, int i2) {
        if (DBG) {
            Slog.d(LOG_TAG, "onGestureCompleted: " + MagnificationGestureMatcher.gestureIdToString(i) + " event = " + motionEvent);
        }
        List<MotionEventInfo> list = this.mDelayedEventQueue;
        this.mDelayedEventQueue = null;
        this.mCallback.onGestureCompleted(i, this.mLastDownEventTime, list, motionEvent);
        clear();
    }

    @Override // com.android.server.accessibility.magnification.GesturesObserver.Listener
    public void onGestureCancelled(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        if (DBG) {
            Slog.d(LOG_TAG, "onGestureCancelled:  event = " + motionEvent);
        }
        notifyDetectionCancel();
    }

    private void notifyDetectionCancel() {
        List<MotionEventInfo> list = this.mDelayedEventQueue;
        this.mDelayedEventQueue = null;
        this.mCallback.onGestureCancelled(this.mLastDownEventTime, list, this.mLastEvent);
        clear();
    }

    private void clear() {
        if (DBG) {
            Slog.d(LOG_TAG, "clear:" + this.mDelayedEventQueue);
        }
        recycleLastEvent();
        this.mLastDownEventTime = 0L;
        List<MotionEventInfo> list = this.mDelayedEventQueue;
        if (list != null) {
            Iterator<MotionEventInfo> it = list.iterator();
            while (it.hasNext()) {
                it.next().recycle();
            }
            this.mDelayedEventQueue.clear();
            this.mDelayedEventQueue = null;
        }
    }

    private void recycleLastEvent() {
        MotionEvent motionEvent = this.mLastEvent;
        if (motionEvent == null) {
            return;
        }
        motionEvent.recycle();
        this.mLastEvent = null;
    }

    private void cacheDelayedMotionEvent(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        this.mLastEvent = MotionEvent.obtain(motionEvent);
        MotionEventInfo obtain = MotionEventInfo.obtain(motionEvent, motionEvent2, i);
        if (this.mDelayedEventQueue == null) {
            this.mDelayedEventQueue = new LinkedList();
        }
        this.mDelayedEventQueue.add(obtain);
    }

    public String toString() {
        return "MagnificationGesturesObserver{mDelayedEventQueue=" + this.mDelayedEventQueue + '}';
    }
}
