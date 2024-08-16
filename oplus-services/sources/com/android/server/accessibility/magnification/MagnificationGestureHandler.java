package com.android.server.accessibility.magnification;

import android.util.Log;
import android.util.Slog;
import android.view.MotionEvent;
import com.android.server.accessibility.AccessibilityTraceManager;
import com.android.server.accessibility.BaseEventStreamTransformation;
import java.util.ArrayDeque;
import java.util.Queue;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class MagnificationGestureHandler extends BaseEventStreamTransformation {
    protected static final boolean DEBUG_ALL;
    protected static final boolean DEBUG_EVENT_STREAM;
    protected final Callback mCallback;
    private final Queue<MotionEvent> mDebugInputEventHistory;
    private final Queue<MotionEvent> mDebugOutputEventHistory;
    protected final boolean mDetectShortcutTrigger;
    protected final boolean mDetectTripleTap;
    protected final int mDisplayId;
    protected final String mLogTag = getClass().getSimpleName();
    private final AccessibilityTraceManager mTrace;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Callback {
        void onTouchInteractionEnd(int i, int i2);

        void onTouchInteractionStart(int i, int i2);
    }

    public abstract int getMode();

    abstract void handleShortcutTriggered();

    abstract void onMotionEventInternal(MotionEvent motionEvent, MotionEvent motionEvent2, int i);

    static {
        boolean isLoggable = Log.isLoggable("MagnificationGestureHandler", 3);
        DEBUG_ALL = isLoggable;
        DEBUG_EVENT_STREAM = isLoggable | false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public MagnificationGestureHandler(int i, boolean z, boolean z2, AccessibilityTraceManager accessibilityTraceManager, Callback callback) {
        this.mDisplayId = i;
        this.mDetectTripleTap = z;
        this.mDetectShortcutTrigger = z2;
        this.mTrace = accessibilityTraceManager;
        this.mCallback = callback;
        boolean z3 = DEBUG_EVENT_STREAM;
        this.mDebugInputEventHistory = z3 ? new ArrayDeque() : null;
        this.mDebugOutputEventHistory = z3 ? new ArrayDeque() : null;
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public final void onMotionEvent(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        if (DEBUG_ALL) {
            Slog.i(this.mLogTag, "onMotionEvent(" + motionEvent + ")");
        }
        if (this.mTrace.isA11yTracingEnabledForTypes(12288L)) {
            this.mTrace.logTrace("MagnificationGestureHandler.onMotionEvent", 12288L, "event=" + motionEvent + ";rawEvent=" + motionEvent2 + ";policyFlags=" + i);
        }
        if (DEBUG_EVENT_STREAM) {
            storeEventInto(this.mDebugInputEventHistory, motionEvent);
        }
        if (shouldDispatchTransformedEvent(motionEvent)) {
            dispatchTransformedEvent(motionEvent, motionEvent2, i);
            return;
        }
        onMotionEventInternal(motionEvent, motionEvent2, i);
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mCallback.onTouchInteractionStart(this.mDisplayId, getMode());
        } else if (action == 1 || action == 3) {
            this.mCallback.onTouchInteractionEnd(this.mDisplayId, getMode());
        }
    }

    private boolean shouldDispatchTransformedEvent(MotionEvent motionEvent) {
        return ((this.mDetectTripleTap || this.mDetectShortcutTrigger) && motionEvent.isFromSource(4098)) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void dispatchTransformedEvent(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        if (DEBUG_EVENT_STREAM) {
            storeEventInto(this.mDebugOutputEventHistory, motionEvent);
            try {
                super.onMotionEvent(motionEvent, motionEvent2, i);
                return;
            } catch (Exception e) {
                throw new RuntimeException("Exception downstream following input events: " + this.mDebugInputEventHistory + "\nTransformed into output events: " + this.mDebugOutputEventHistory, e);
            }
        }
        super.onMotionEvent(motionEvent, motionEvent2, i);
    }

    private static void storeEventInto(Queue<MotionEvent> queue, MotionEvent motionEvent) {
        queue.add(MotionEvent.obtain(motionEvent));
        while (!queue.isEmpty() && motionEvent.getEventTime() - queue.peek().getEventTime() > 5000) {
            queue.remove().recycle();
        }
    }

    public void notifyShortcutTriggered() {
        if (DEBUG_ALL) {
            Slog.i(this.mLogTag, "notifyShortcutTriggered():");
        }
        if (this.mDetectShortcutTrigger) {
            handleShortcutTriggered();
        }
    }
}
