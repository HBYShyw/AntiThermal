package com.android.server.accessibility.gestures;

import android.content.Context;
import android.graphics.Point;
import android.util.Slog;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import com.android.server.accessibility.AccessibilityManagerService;
import com.android.server.accessibility.EventStreamTransformation;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class EventDispatcher {
    private static final int CLICK_LOCATION_ACCESSIBILITY_FOCUS = 1;
    private static final int CLICK_LOCATION_LAST_TOUCH_EXPLORED = 2;
    private static final int CLICK_LOCATION_NONE = 0;
    private static final String LOG_TAG = "EventDispatcher";
    private final AccessibilityManagerService mAms;
    private Context mContext;
    private int mLongPressingPointerDeltaX;
    private int mLongPressingPointerDeltaY;
    private EventStreamTransformation mReceiver;
    private TouchState mState;
    private int mLongPressingPointerId = -1;
    private final Point mTempPoint = new Point();

    /* JADX INFO: Access modifiers changed from: package-private */
    public EventDispatcher(Context context, AccessibilityManagerService accessibilityManagerService, EventStreamTransformation eventStreamTransformation, TouchState touchState) {
        this.mContext = context;
        this.mAms = accessibilityManagerService;
        this.mReceiver = eventStreamTransformation;
        this.mState = touchState;
    }

    public void setReceiver(EventStreamTransformation eventStreamTransformation) {
        this.mReceiver = eventStreamTransformation;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendMotionEvent(MotionEvent motionEvent, int i, MotionEvent motionEvent2, int i2, int i3) {
        MotionEvent split;
        motionEvent.setAction(i);
        if (i2 == -1) {
            split = motionEvent;
        } else {
            try {
                split = motionEvent.split(i2);
            } catch (IllegalArgumentException e) {
                Slog.e(LOG_TAG, "sendMotionEvent: Failed to split motion event: " + e);
                return;
            }
        }
        if (i == 0) {
            split.setDownTime(split.getEventTime());
        } else {
            split.setDownTime(this.mState.getLastInjectedDownEventTime());
        }
        if (this.mLongPressingPointerId >= 0) {
            split = offsetEvent(split, -this.mLongPressingPointerDeltaX, -this.mLongPressingPointerDeltaY);
        }
        if (TouchExplorer.DEBUG) {
            Slog.d(LOG_TAG, "Injecting event: " + split + ", policyFlags=0x" + Integer.toHexString(i3));
        }
        int i4 = 1073741824 | i3;
        EventStreamTransformation eventStreamTransformation = this.mReceiver;
        if (eventStreamTransformation != null) {
            eventStreamTransformation.onMotionEvent(split, motionEvent2, i4);
        } else {
            Slog.e(LOG_TAG, "Error sending event: no receiver specified.");
        }
        this.mState.onInjectedMotionEvent(split);
        if (split != motionEvent) {
            split.recycle();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendAccessibilityEvent(int i) {
        AccessibilityManager accessibilityManager = AccessibilityManager.getInstance(this.mContext);
        if (accessibilityManager.isEnabled()) {
            AccessibilityEvent obtain = AccessibilityEvent.obtain(i);
            obtain.setWindowId(this.mAms.getActiveWindowId());
            accessibilityManager.sendAccessibilityEvent(obtain);
            if (TouchExplorer.DEBUG) {
                Slog.d(LOG_TAG, "Sending accessibility event" + AccessibilityEvent.eventTypeToString(i));
            }
        }
        this.mState.onInjectedAccessibilityEvent(i);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=========================");
        sb.append("\nDown pointers #");
        sb.append(Integer.bitCount(this.mState.getInjectedPointersDown()));
        sb.append(" [ ");
        for (int i = 0; i < 32; i++) {
            if (this.mState.isInjectedPointerDown(i)) {
                sb.append(i);
                sb.append(" ");
            }
        }
        sb.append("]");
        sb.append("\n=========================");
        return sb.toString();
    }

    private MotionEvent offsetEvent(MotionEvent motionEvent, int i, int i2) {
        if (i == 0 && i2 == 0) {
            return motionEvent;
        }
        int findPointerIndex = motionEvent.findPointerIndex(this.mLongPressingPointerId);
        int pointerCount = motionEvent.getPointerCount();
        MotionEvent.PointerProperties[] createArray = MotionEvent.PointerProperties.createArray(pointerCount);
        MotionEvent.PointerCoords[] createArray2 = MotionEvent.PointerCoords.createArray(pointerCount);
        for (int i3 = 0; i3 < pointerCount; i3++) {
            motionEvent.getPointerProperties(i3, createArray[i3]);
            motionEvent.getPointerCoords(i3, createArray2[i3]);
            if (i3 == findPointerIndex) {
                MotionEvent.PointerCoords pointerCoords = createArray2[i3];
                pointerCoords.x += i;
                pointerCoords.y += i2;
            }
        }
        return MotionEvent.obtain(motionEvent.getDownTime(), motionEvent.getEventTime(), motionEvent.getAction(), motionEvent.getPointerCount(), createArray, createArray2, motionEvent.getMetaState(), motionEvent.getButtonState(), 1.0f, 1.0f, motionEvent.getDeviceId(), motionEvent.getEdgeFlags(), motionEvent.getSource(), motionEvent.getDisplayId(), motionEvent.getFlags());
    }

    private int computeInjectionAction(int i, int i2) {
        int i3 = 5;
        if (i != 0 && i != 5) {
            i3 = 6;
            if (i != 6) {
                return i;
            }
            if (this.mState.getInjectedPointerDownCount() == 1) {
                return 1;
            }
        } else if (this.mState.getInjectedPointerDownCount() == 0) {
            return 0;
        }
        return (i2 << 8) | i3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendDownForAllNotInjectedPointers(MotionEvent motionEvent, int i) {
        int pointerCount = motionEvent.getPointerCount();
        int i2 = 0;
        for (int i3 = 0; i3 < pointerCount; i3++) {
            try {
                int pointerId = motionEvent.getPointerId(i3);
                if (!this.mState.isInjectedPointerDown(pointerId)) {
                    i2 |= 1 << pointerId;
                    sendMotionEvent(motionEvent, computeInjectionAction(0, i3), this.mState.getLastReceivedEvent(), i2, i);
                }
            } catch (IllegalArgumentException e) {
                Slog.e(LOG_TAG, "sendDownForAllNotInjectedPointers: ignore invalid pointers: " + e);
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendDownForAllNotInjectedPointersWithOriginalDown(MotionEvent motionEvent, int i) {
        int pointerCount = motionEvent.getPointerCount();
        MotionEvent computeInjectionDownEvent = computeInjectionDownEvent(motionEvent);
        int i2 = 0;
        for (int i3 = 0; i3 < pointerCount; i3++) {
            int pointerId = motionEvent.getPointerId(i3);
            if (!this.mState.isInjectedPointerDown(pointerId)) {
                int i4 = i2 | (1 << pointerId);
                sendMotionEvent(computeInjectionDownEvent, computeInjectionAction(0, i3), this.mState.getLastReceivedEvent(), i4, i);
                i2 = i4;
            }
        }
    }

    private MotionEvent computeInjectionDownEvent(MotionEvent motionEvent) {
        int pointerCount = motionEvent.getPointerCount();
        if (pointerCount != this.mState.getReceivedPointerTracker().getReceivedPointerDownCount()) {
            Slog.w(LOG_TAG, "The pointer count doesn't match the received count.");
            return MotionEvent.obtain(motionEvent);
        }
        MotionEvent.PointerCoords[] pointerCoordsArr = new MotionEvent.PointerCoords[pointerCount];
        MotionEvent.PointerProperties[] pointerPropertiesArr = new MotionEvent.PointerProperties[pointerCount];
        for (int i = 0; i < pointerCount; i++) {
            int pointerId = motionEvent.getPointerId(i);
            float receivedPointerDownX = this.mState.getReceivedPointerTracker().getReceivedPointerDownX(pointerId);
            float receivedPointerDownY = this.mState.getReceivedPointerTracker().getReceivedPointerDownY(pointerId);
            MotionEvent.PointerCoords pointerCoords = new MotionEvent.PointerCoords();
            pointerCoordsArr[i] = pointerCoords;
            pointerCoords.x = receivedPointerDownX;
            pointerCoords.y = receivedPointerDownY;
            MotionEvent.PointerProperties pointerProperties = new MotionEvent.PointerProperties();
            pointerPropertiesArr[i] = pointerProperties;
            pointerProperties.id = pointerId;
            pointerProperties.toolType = 1;
        }
        return MotionEvent.obtain(motionEvent.getDownTime(), motionEvent.getDownTime(), motionEvent.getAction(), pointerCount, pointerPropertiesArr, pointerCoordsArr, motionEvent.getMetaState(), motionEvent.getButtonState(), motionEvent.getXPrecision(), motionEvent.getYPrecision(), motionEvent.getDeviceId(), motionEvent.getEdgeFlags(), motionEvent.getSource(), motionEvent.getFlags());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendUpForInjectedDownPointers(MotionEvent motionEvent, int i) {
        int pointerIdBits = motionEvent.getPointerIdBits();
        int pointerCount = motionEvent.getPointerCount();
        for (int i2 = 0; i2 < pointerCount; i2++) {
            int pointerId = motionEvent.getPointerId(i2);
            if (this.mState.isInjectedPointerDown(pointerId)) {
                sendMotionEvent(motionEvent, computeInjectionAction(6, i2), this.mState.getLastReceivedEvent(), pointerIdBits, i);
                pointerIdBits &= ~(1 << pointerId);
            }
        }
    }

    public boolean longPressWithTouchEvents(MotionEvent motionEvent, int i) {
        Point point = this.mTempPoint;
        if (computeClickLocation(point) == 0 || motionEvent == null) {
            return false;
        }
        int actionIndex = motionEvent.getActionIndex();
        this.mLongPressingPointerId = motionEvent.getPointerId(actionIndex);
        this.mLongPressingPointerDeltaX = ((int) motionEvent.getX(actionIndex)) - point.x;
        this.mLongPressingPointerDeltaY = ((int) motionEvent.getY(actionIndex)) - point.y;
        sendDownForAllNotInjectedPointers(motionEvent, i);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clear() {
        this.mLongPressingPointerId = -1;
        this.mLongPressingPointerDeltaX = 0;
        this.mLongPressingPointerDeltaY = 0;
    }

    public void clickWithTouchEvents(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        int i2;
        int actionIndex = motionEvent.getActionIndex();
        motionEvent.getPointerId(actionIndex);
        int computeClickLocation = computeClickLocation(this.mTempPoint);
        if (computeClickLocation == 0) {
            Slog.e(LOG_TAG, "Unable to compute click location.");
            return;
        }
        MotionEvent.PointerProperties[] pointerPropertiesArr = {new MotionEvent.PointerProperties()};
        motionEvent.getPointerProperties(actionIndex, pointerPropertiesArr[0]);
        MotionEvent.PointerCoords[] pointerCoordsArr = {new MotionEvent.PointerCoords()};
        MotionEvent.PointerCoords pointerCoords = pointerCoordsArr[0];
        pointerCoords.x = r3.x;
        pointerCoords.y = r3.y;
        MotionEvent obtain = MotionEvent.obtain(motionEvent.getDownTime(), motionEvent.getEventTime(), 0, 1, pointerPropertiesArr, pointerCoordsArr, 0, 0, 1.0f, 1.0f, motionEvent.getDeviceId(), 0, motionEvent.getSource(), motionEvent.getDisplayId(), motionEvent.getFlags());
        boolean z = true;
        if (computeClickLocation == 1) {
            i2 = i;
        } else {
            i2 = i;
            z = false;
        }
        sendActionDownAndUp(obtain, motionEvent2, i2, z);
        obtain.recycle();
    }

    private int computeClickLocation(Point point) {
        if (this.mState.getLastInjectedHoverEventForClick() != null) {
            int actionIndex = this.mState.getLastInjectedHoverEventForClick().getActionIndex();
            point.x = (int) this.mState.getLastInjectedHoverEventForClick().getX(actionIndex);
            point.y = (int) this.mState.getLastInjectedHoverEventForClick().getY(actionIndex);
            if (!this.mAms.accessibilityFocusOnlyInActiveWindow() || this.mState.getLastTouchedWindowId() == this.mAms.getActiveWindowId()) {
                return this.mAms.getAccessibilityFocusClickPointInScreen(point) ? 1 : 2;
            }
        }
        return this.mAms.getAccessibilityFocusClickPointInScreen(point) ? 1 : 0;
    }

    private void sendActionDownAndUp(MotionEvent motionEvent, MotionEvent motionEvent2, int i, boolean z) {
        int pointerId = 1 << motionEvent.getPointerId(motionEvent.getActionIndex());
        motionEvent.setTargetAccessibilityFocus(z);
        sendMotionEvent(motionEvent, 0, motionEvent2, pointerId, i);
        motionEvent.setTargetAccessibilityFocus(z);
        sendMotionEvent(motionEvent, 1, motionEvent2, pointerId, i);
    }
}
