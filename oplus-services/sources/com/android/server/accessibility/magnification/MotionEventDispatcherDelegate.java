package com.android.server.accessibility.magnification;

import android.R;
import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class MotionEventDispatcherDelegate {
    private static final boolean DBG = Log.isLoggable(MotionEventDispatcherDelegate.class.getSimpleName(), 3);
    private static final String TAG = "MotionEventDispatcherDelegate";
    private final EventDispatcher mEventDispatcher;
    private long mLastDelegatedDownEventTime;
    private final int mMultiTapMaxDelay;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface EventDispatcher {
        void dispatchMotionEvent(MotionEvent motionEvent, MotionEvent motionEvent2, int i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MotionEventDispatcherDelegate(Context context, EventDispatcher eventDispatcher) {
        this.mEventDispatcher = eventDispatcher;
        this.mMultiTapMaxDelay = ViewConfiguration.getDoubleTapTimeout() + context.getResources().getInteger(R.integer.leanback_setup_translation_content_resting_point_v4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendDelayedMotionEvents(List<MotionEventInfo> list, long j) {
        if (list == null) {
            return;
        }
        long min = Math.min(SystemClock.uptimeMillis() - j, this.mMultiTapMaxDelay);
        for (MotionEventInfo motionEventInfo : list) {
            MotionEvent motionEvent = motionEventInfo.mEvent;
            motionEvent.setDownTime(motionEvent.getDownTime() + min);
            dispatchMotionEvent(motionEventInfo.mEvent, motionEventInfo.mRawEvent, motionEventInfo.mPolicyFlags);
            motionEventInfo.recycle();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dispatchMotionEvent(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        if (motionEvent.getActionMasked() == 0) {
            this.mLastDelegatedDownEventTime = motionEvent.getDownTime();
            if (DBG) {
                Log.d(TAG, "dispatchMotionEvent mLastDelegatedDownEventTime time = " + this.mLastDelegatedDownEventTime);
            }
        }
        if (DBG) {
            Log.d(TAG, "dispatchMotionEvent original down time = " + motionEvent.getDownTime());
        }
        motionEvent.setDownTime(this.mLastDelegatedDownEventTime);
        this.mEventDispatcher.dispatchMotionEvent(motionEvent, motionEvent2, i);
    }
}
