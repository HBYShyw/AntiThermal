package com.android.server.accessibility.gestures;

import android.os.Handler;
import android.util.Slog;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class GestureMatcher {
    public static final int STATE_CLEAR = 0;
    public static final int STATE_GESTURE_CANCELED = 3;
    public static final int STATE_GESTURE_COMPLETED = 2;
    public static final int STATE_GESTURE_STARTED = 1;
    private final int mGestureId;
    private final Handler mHandler;
    private StateChangeListener mListener;

    @State
    private int mState = 0;
    protected final DelayedTransition mDelayedTransition = new DelayedTransition();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface State {
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface StateChangeListener {
        void onStateChanged(int i, int i2, MotionEvent motionEvent, MotionEvent motionEvent2, int i3);
    }

    protected abstract String getGestureName();

    protected void onDown(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
    }

    protected void onMove(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
    }

    protected void onPointerDown(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
    }

    protected void onPointerUp(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
    }

    protected void onUp(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public GestureMatcher(int i, Handler handler, StateChangeListener stateChangeListener) {
        this.mGestureId = i;
        this.mHandler = handler;
        this.mListener = stateChangeListener;
    }

    public void clear() {
        this.mState = 0;
        cancelPendingTransitions();
    }

    public final int getState() {
        return this.mState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setState(@State int i, MotionEvent motionEvent, MotionEvent motionEvent2, int i2) {
        this.mState = i;
        cancelPendingTransitions();
        StateChangeListener stateChangeListener = this.mListener;
        if (stateChangeListener != null) {
            stateChangeListener.onStateChanged(this.mGestureId, this.mState, motionEvent, motionEvent2, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void startGesture(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        setState(1, motionEvent, motionEvent2, i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void cancelGesture(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        setState(3, motionEvent, motionEvent2, i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void completeGesture(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        setState(2, motionEvent, motionEvent2, i);
    }

    public final void setListener(StateChangeListener stateChangeListener) {
        this.mListener = stateChangeListener;
    }

    public int getGestureId() {
        return this.mGestureId;
    }

    public final int onMotionEvent(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        int i2 = this.mState;
        if (i2 == 3 || i2 == 2) {
            return i2;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            onDown(motionEvent, motionEvent2, i);
        } else if (actionMasked == 1) {
            onUp(motionEvent, motionEvent2, i);
        } else if (actionMasked == 2) {
            onMove(motionEvent, motionEvent2, i);
        } else if (actionMasked == 5) {
            onPointerDown(motionEvent, motionEvent2, i);
        } else if (actionMasked == 6) {
            onPointerUp(motionEvent, motionEvent2, i);
        } else {
            setState(3, motionEvent, motionEvent2, i);
        }
        return this.mState;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void cancelAfterTapTimeout(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        cancelAfter(ViewConfiguration.getTapTimeout(), motionEvent, motionEvent2, i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void cancelAfterDoubleTapTimeout(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        cancelAfter(ViewConfiguration.getDoubleTapTimeout(), motionEvent, motionEvent2, i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void cancelAfter(long j, MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        this.mDelayedTransition.cancel();
        this.mDelayedTransition.post(3, j, motionEvent, motionEvent2, i);
    }

    protected final void cancelPendingTransitions() {
        this.mDelayedTransition.cancel();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void completeAfterLongPressTimeout(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        completeAfter(ViewConfiguration.getLongPressTimeout(), motionEvent, motionEvent2, i);
    }

    protected final void completeAfterTapTimeout(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        completeAfter(ViewConfiguration.getTapTimeout(), motionEvent, motionEvent2, i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void completeAfter(long j, MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        this.mDelayedTransition.cancel();
        this.mDelayedTransition.post(2, j, motionEvent, motionEvent2, i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void completeAfterDoubleTapTimeout(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        completeAfter(ViewConfiguration.getDoubleTapTimeout(), motionEvent, motionEvent2, i);
    }

    static String getStateSymbolicName(@State int i) {
        if (i == 0) {
            return "STATE_CLEAR";
        }
        if (i == 1) {
            return "STATE_GESTURE_STARTED";
        }
        if (i == 2) {
            return "STATE_GESTURE_COMPLETED";
        }
        if (i == 3) {
            return "STATE_GESTURE_CANCELED";
        }
        return "Unknown state: " + i;
    }

    public String toString() {
        return getGestureName() + ":" + getStateSymbolicName(this.mState);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class DelayedTransition implements Runnable {
        private static final String LOG_TAG = "GestureMatcher.DelayedTransition";
        MotionEvent mEvent;
        int mPolicyFlags;
        MotionEvent mRawEvent;
        int mTargetState;

        protected DelayedTransition() {
        }

        public void cancel() {
            if (TouchExplorer.DEBUG && isPending()) {
                Slog.d(LOG_TAG, GestureMatcher.this.getGestureName() + ": canceling delayed transition to " + GestureMatcher.getStateSymbolicName(this.mTargetState));
            }
            GestureMatcher.this.mHandler.removeCallbacks(this);
        }

        public void post(int i, long j, MotionEvent motionEvent, MotionEvent motionEvent2, int i2) {
            this.mTargetState = i;
            this.mEvent = motionEvent;
            this.mRawEvent = motionEvent2;
            this.mPolicyFlags = i2;
            GestureMatcher.this.mHandler.postDelayed(this, j);
            if (TouchExplorer.DEBUG) {
                Slog.d(LOG_TAG, GestureMatcher.this.getGestureName() + ": posting delayed transition to " + GestureMatcher.getStateSymbolicName(this.mTargetState));
            }
        }

        public boolean isPending() {
            return GestureMatcher.this.mHandler.hasCallbacks(this);
        }

        public void forceSendAndRemove() {
            if (isPending()) {
                run();
                cancel();
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            if (TouchExplorer.DEBUG) {
                Slog.d(LOG_TAG, GestureMatcher.this.getGestureName() + ": executing delayed transition to " + GestureMatcher.getStateSymbolicName(this.mTargetState));
            }
            GestureMatcher.this.setState(this.mTargetState, this.mEvent, this.mRawEvent, this.mPolicyFlags);
        }
    }
}
