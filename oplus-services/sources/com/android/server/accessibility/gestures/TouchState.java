package com.android.server.accessibility.gestures;

import android.util.Slog;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityEvent;
import com.android.server.accessibility.AccessibilityManagerService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class TouchState {
    public static final int ALL_POINTER_ID_BITS = -1;
    private static final String LOG_TAG = "TouchState";
    public static final int MAX_POINTER_COUNT = 32;
    public static final int STATE_CLEAR = 0;
    public static final int STATE_DELEGATING = 4;
    public static final int STATE_DRAGGING = 3;
    public static final int STATE_GESTURE_DETECTING = 5;
    public static final int STATE_TOUCH_EXPLORING = 2;
    public static final int STATE_TOUCH_INTERACTING = 1;
    private AccessibilityManagerService mAms;
    private int mDisplayId;
    private int mInjectedPointersDown;
    private long mLastInjectedDownEventTime;
    private MotionEvent mLastInjectedHoverEvent;
    private MotionEvent mLastInjectedHoverEventForClick;
    private MotionEvent mLastReceivedEvent;
    int mLastReceivedPolicyFlags;
    private MotionEvent mLastReceivedRawEvent;
    private int mLastTouchedWindowId;
    private int mState = 0;
    private boolean mServiceDetectsGestures = false;
    private boolean mServiceDetectsGesturesRequested = false;
    private final ReceivedPointerTracker mReceivedPointerTracker = new ReceivedPointerTracker();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface State {
    }

    public TouchState(int i, AccessibilityManagerService accessibilityManagerService) {
        this.mDisplayId = i;
        this.mAms = accessibilityManagerService;
    }

    public void clear() {
        setState(0);
        this.mServiceDetectsGestures = this.mServiceDetectsGesturesRequested;
        MotionEvent motionEvent = this.mLastReceivedEvent;
        if (motionEvent != null) {
            motionEvent.recycle();
            this.mLastReceivedEvent = null;
        }
        this.mReceivedPointerTracker.clear();
        this.mInjectedPointersDown = 0;
    }

    public void onReceivedMotionEvent(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        if (isClear() && motionEvent.getActionMasked() == 0) {
            clear();
        }
        MotionEvent motionEvent3 = this.mLastReceivedEvent;
        if (motionEvent3 != null) {
            motionEvent3.recycle();
        }
        MotionEvent motionEvent4 = this.mLastReceivedRawEvent;
        if (motionEvent4 != null) {
            motionEvent4.recycle();
        }
        this.mLastReceivedEvent = MotionEvent.obtain(motionEvent);
        this.mLastReceivedRawEvent = MotionEvent.obtain(motionEvent2);
        this.mLastReceivedPolicyFlags = i;
        this.mReceivedPointerTracker.onMotionEvent(motionEvent2);
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x006a  */
    /* JADX WARN: Removed duplicated region for block: B:26:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onInjectedMotionEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        int pointerId = 1 << motionEvent.getPointerId(motionEvent.getActionIndex());
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked != 5) {
                    if (actionMasked != 6) {
                        if (actionMasked == 7 || actionMasked == 9) {
                            MotionEvent motionEvent2 = this.mLastInjectedHoverEvent;
                            if (motionEvent2 != null) {
                                motionEvent2.recycle();
                            }
                            this.mLastInjectedHoverEvent = MotionEvent.obtain(motionEvent);
                        } else if (actionMasked == 10) {
                            MotionEvent motionEvent3 = this.mLastInjectedHoverEvent;
                            if (motionEvent3 != null) {
                                motionEvent3.recycle();
                            }
                            this.mLastInjectedHoverEvent = MotionEvent.obtain(motionEvent);
                            MotionEvent motionEvent4 = this.mLastInjectedHoverEventForClick;
                            if (motionEvent4 != null) {
                                motionEvent4.recycle();
                            }
                            this.mLastInjectedHoverEventForClick = MotionEvent.obtain(motionEvent);
                        }
                        if (TouchExplorer.DEBUG) {
                            Slog.i(LOG_TAG, "Injected pointer:\n" + toString());
                            return;
                        }
                        return;
                    }
                }
            }
            int i = this.mInjectedPointersDown & (~pointerId);
            this.mInjectedPointersDown = i;
            if (i == 0) {
                this.mLastInjectedDownEventTime = 0L;
            }
            if (TouchExplorer.DEBUG) {
            }
        }
        this.mInjectedPointersDown |= pointerId;
        this.mLastInjectedDownEventTime = motionEvent.getDownTime();
        if (TouchExplorer.DEBUG) {
        }
    }

    public void onReceivedAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        int eventType = accessibilityEvent.getEventType();
        if (eventType != 32) {
            if (eventType == 128 || eventType == 256) {
                this.mLastTouchedWindowId = accessibilityEvent.getWindowId();
                return;
            } else if (eventType != 32768) {
                if (eventType != 2097152) {
                    return;
                }
                this.mAms.moveNonProxyTopFocusedDisplayToTopIfNeeded();
                return;
            }
        }
        MotionEvent motionEvent = this.mLastInjectedHoverEventForClick;
        if (motionEvent != null) {
            motionEvent.recycle();
            this.mLastInjectedHoverEventForClick = null;
        }
        this.mLastTouchedWindowId = -1;
    }

    public void onInjectedAccessibilityEvent(int i) {
        if (i == 512) {
            startTouchExploring();
            return;
        }
        if (i == 1024) {
            startTouchInteracting();
            return;
        }
        if (i == 262144) {
            startGestureDetecting();
            return;
        }
        if (i == 524288) {
            clear();
        } else if (i == 1048576) {
            startTouchInteracting();
        } else {
            if (i != 2097152) {
                return;
            }
            setState(0);
        }
    }

    @State
    public int getState() {
        return this.mState;
    }

    public void setState(@State int i) {
        if (this.mState == i) {
            return;
        }
        if (TouchExplorer.DEBUG) {
            Slog.i(LOG_TAG, getStateSymbolicName(this.mState) + "->" + getStateSymbolicName(i));
        }
        this.mState = i;
        if (this.mServiceDetectsGestures) {
            this.mAms.onTouchStateChanged(this.mDisplayId, i);
        }
    }

    public boolean isTouchExploring() {
        return this.mState == 2;
    }

    public void startTouchExploring() {
        setState(2);
    }

    public boolean isDelegating() {
        return this.mState == 4;
    }

    public void startDelegating() {
        setState(4);
    }

    public boolean isGestureDetecting() {
        return this.mState == 5;
    }

    public void startGestureDetecting() {
        setState(5);
    }

    public boolean isDragging() {
        return this.mState == 3;
    }

    public void startDragging() {
        setState(3);
    }

    public boolean isTouchInteracting() {
        return this.mState == 1;
    }

    public void startTouchInteracting() {
        setState(1);
    }

    public boolean isClear() {
        return this.mState == 0;
    }

    public String toString() {
        return "TouchState { mState: " + getStateSymbolicName(this.mState) + " }";
    }

    public static String getStateSymbolicName(int i) {
        if (i == 0) {
            return "STATE_CLEAR";
        }
        if (i == 1) {
            return "STATE_TOUCH_INTERACTING";
        }
        if (i == 2) {
            return "STATE_TOUCH_EXPLORING";
        }
        if (i == 3) {
            return "STATE_DRAGGING";
        }
        if (i == 4) {
            return "STATE_DELEGATING";
        }
        if (i == 5) {
            return "STATE_GESTURE_DETECTING";
        }
        return "Unknown state: " + i;
    }

    public ReceivedPointerTracker getReceivedPointerTracker() {
        return this.mReceivedPointerTracker;
    }

    public MotionEvent getLastReceivedEvent() {
        return this.mLastReceivedEvent;
    }

    public int getLastReceivedPolicyFlags() {
        return this.mLastReceivedPolicyFlags;
    }

    public MotionEvent getLastReceivedRawEvent() {
        return this.mLastReceivedRawEvent;
    }

    public MotionEvent getLastInjectedHoverEvent() {
        return this.mLastInjectedHoverEvent;
    }

    public long getLastInjectedDownEventTime() {
        return this.mLastInjectedDownEventTime;
    }

    public int getLastTouchedWindowId() {
        return this.mLastTouchedWindowId;
    }

    public int getInjectedPointerDownCount() {
        return Integer.bitCount(this.mInjectedPointersDown);
    }

    public int getInjectedPointersDown() {
        return this.mInjectedPointersDown;
    }

    public boolean isInjectedPointerDown(int i) {
        return (this.mInjectedPointersDown & (1 << i)) != 0;
    }

    public MotionEvent getLastInjectedHoverEventForClick() {
        return this.mLastInjectedHoverEventForClick;
    }

    public boolean isServiceDetectingGestures() {
        return this.mServiceDetectsGestures;
    }

    public void setServiceDetectsGestures(boolean z) {
        if (TouchExplorer.DEBUG) {
            Slog.d(LOG_TAG, "serviceDetectsGestures: " + z);
        }
        this.mServiceDetectsGesturesRequested = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class ReceivedPointerTracker {
        private static final String LOG_TAG_RECEIVED_POINTER_TRACKER = "ReceivedPointerTracker";
        private int mLastReceivedDownEdgeFlags;
        private int mPrimaryPointerId;
        private final PointerDownInfo[] mReceivedPointers = new PointerDownInfo[32];
        private int mReceivedPointersDown;

        ReceivedPointerTracker() {
            clear();
        }

        public void clear() {
            this.mReceivedPointersDown = 0;
            this.mPrimaryPointerId = 0;
            for (int i = 0; i < 32; i++) {
                this.mReceivedPointers[i] = new PointerDownInfo();
            }
        }

        public void onMotionEvent(MotionEvent motionEvent) {
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 0) {
                handleReceivedPointerDown(motionEvent.getActionIndex(), motionEvent);
            } else if (actionMasked == 1) {
                handleReceivedPointerUp(motionEvent.getActionIndex(), motionEvent);
            } else if (actionMasked == 5) {
                handleReceivedPointerDown(motionEvent.getActionIndex(), motionEvent);
            } else if (actionMasked == 6) {
                handleReceivedPointerUp(motionEvent.getActionIndex(), motionEvent);
            }
            if (TouchExplorer.DEBUG) {
                Slog.i(LOG_TAG_RECEIVED_POINTER_TRACKER, "Received pointer:\n" + toString());
            }
        }

        public int getReceivedPointerDownCount() {
            return Integer.bitCount(this.mReceivedPointersDown);
        }

        public boolean isReceivedPointerDown(int i) {
            return (this.mReceivedPointersDown & (1 << i)) != 0;
        }

        public float getReceivedPointerDownX(int i) {
            return this.mReceivedPointers[i].mX;
        }

        public float getReceivedPointerDownY(int i) {
            return this.mReceivedPointers[i].mY;
        }

        public long getReceivedPointerDownTime(int i) {
            return this.mReceivedPointers[i].mTime;
        }

        public int getPrimaryPointerId() {
            if (this.mPrimaryPointerId == -1) {
                this.mPrimaryPointerId = findPrimaryPointerId();
            }
            return this.mPrimaryPointerId;
        }

        public int getLastReceivedDownEdgeFlags() {
            return this.mLastReceivedDownEdgeFlags;
        }

        private void handleReceivedPointerDown(int i, MotionEvent motionEvent) {
            int pointerId = motionEvent.getPointerId(i);
            this.mLastReceivedDownEdgeFlags = motionEvent.getEdgeFlags();
            this.mReceivedPointersDown = (1 << pointerId) | this.mReceivedPointersDown;
            this.mReceivedPointers[pointerId].set(motionEvent.getX(i), motionEvent.getY(i), motionEvent.getEventTime());
            this.mPrimaryPointerId = pointerId;
        }

        private void handleReceivedPointerUp(int i, MotionEvent motionEvent) {
            int pointerId = motionEvent.getPointerId(i);
            this.mReceivedPointersDown = (~(1 << pointerId)) & this.mReceivedPointersDown;
            this.mReceivedPointers[pointerId].clear();
            if (this.mPrimaryPointerId == pointerId) {
                this.mPrimaryPointerId = -1;
            }
        }

        private int findPrimaryPointerId() {
            int i = this.mReceivedPointersDown;
            int i2 = -1;
            long j = Long.MAX_VALUE;
            while (i > 0) {
                int numberOfTrailingZeros = Integer.numberOfTrailingZeros(i);
                i &= ~(1 << numberOfTrailingZeros);
                long j2 = this.mReceivedPointers[numberOfTrailingZeros].mTime;
                if (j2 < j) {
                    i2 = numberOfTrailingZeros;
                    j = j2;
                }
            }
            return i2;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("=========================");
            sb.append("\nDown pointers #");
            sb.append(getReceivedPointerDownCount());
            sb.append(" [ ");
            for (int i = 0; i < 32; i++) {
                if (isReceivedPointerDown(i)) {
                    sb.append(i);
                    sb.append(" ");
                }
            }
            sb.append("]");
            sb.append("\nPrimary pointer id [ ");
            sb.append(getPrimaryPointerId());
            sb.append(" ]");
            sb.append("\n=========================");
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class PointerDownInfo {
        private long mTime;
        private float mX;
        private float mY;

        PointerDownInfo() {
        }

        public void set(float f, float f2, long j) {
            this.mX = f;
            this.mY = f2;
            this.mTime = j;
        }

        public void clear() {
            this.mX = 0.0f;
            this.mY = 0.0f;
            this.mTime = 0L;
        }
    }
}
