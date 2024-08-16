package com.android.server.accessibility;

import android.accessibilityservice.GestureDescription;
import android.accessibilityservice.IAccessibilityServiceClient;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.IntArray;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import com.android.internal.os.SomeArgs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class MotionEventInjector extends BaseEventStreamTransformation implements Handler.Callback {
    private static final int EVENT_BUTTON_STATE = 0;
    private static final int EVENT_EDGE_FLAGS = 0;
    private static final int EVENT_FLAGS = 0;
    private static final int EVENT_META_STATE = 0;
    private static final int EVENT_SOURCE = 4098;
    private static final float EVENT_X_PRECISION = 1.0f;
    private static final float EVENT_Y_PRECISION = 1.0f;
    private static final String LOG_TAG = "MotionEventInjector";
    private static final int MESSAGE_INJECT_EVENTS = 2;
    private static final int MESSAGE_SEND_MOTION_EVENT = 1;
    private static MotionEvent.PointerCoords[] sPointerCoords;
    private static MotionEvent.PointerProperties[] sPointerProps;
    private long mDownTime;
    private final Handler mHandler;
    private long mLastScheduledEventTime;
    private GestureDescription.TouchPoint[] mLastTouchPoints;
    private int mNumLastTouchPoints;
    private IAccessibilityServiceClient mServiceInterfaceForCurrentGesture;
    private final AccessibilityTraceManager mTrace;
    private final SparseArray<Boolean> mOpenGesturesInProgress = new SparseArray<>();
    private IntArray mSequencesInProgress = new IntArray(5);
    private boolean mIsDestroyed = false;
    private SparseIntArray mStrokeIdToPointerId = new SparseIntArray(5);

    public MotionEventInjector(Looper looper, AccessibilityTraceManager accessibilityTraceManager) {
        this.mHandler = new Handler(looper, this);
        this.mTrace = accessibilityTraceManager;
    }

    public MotionEventInjector(Handler handler, AccessibilityTraceManager accessibilityTraceManager) {
        this.mHandler = handler;
        this.mTrace = accessibilityTraceManager;
    }

    public void injectEvents(List<GestureDescription.GestureStep> list, IAccessibilityServiceClient iAccessibilityServiceClient, int i, int i2) {
        SomeArgs obtain = SomeArgs.obtain();
        obtain.arg1 = list;
        obtain.arg2 = iAccessibilityServiceClient;
        obtain.argi1 = i;
        obtain.argi2 = i2;
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(2, obtain));
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void onMotionEvent(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        if (this.mTrace.isA11yTracingEnabledForTypes(12288L)) {
            this.mTrace.logTrace("MotionEventInjector.onMotionEvent", 12288L, "event=" + motionEvent + ";rawEvent=" + motionEvent2 + ";policyFlags=" + i);
        }
        if (motionEvent.isFromSource(8194) && motionEvent.getActionMasked() == 7 && this.mOpenGesturesInProgress.get(4098, Boolean.FALSE).booleanValue()) {
            return;
        }
        cancelAnyPendingInjectedEvents();
        sendMotionEventToNext(motionEvent, motionEvent2, i | 131072);
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void clearEvents(int i) {
        if (this.mHandler.hasMessages(1)) {
            return;
        }
        this.mOpenGesturesInProgress.put(i, Boolean.FALSE);
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void onDestroy() {
        cancelAnyPendingInjectedEvents();
        this.mIsDestroyed = true;
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        int i = message.what;
        if (i == 2) {
            SomeArgs someArgs = (SomeArgs) message.obj;
            injectEventsMainThread((List) someArgs.arg1, (IAccessibilityServiceClient) someArgs.arg2, someArgs.argi1, someArgs.argi2);
            someArgs.recycle();
            return true;
        }
        if (i != 1) {
            Slog.e(LOG_TAG, "Unknown message: " + message.what);
            return false;
        }
        MotionEvent motionEvent = (MotionEvent) message.obj;
        sendMotionEventToNext(motionEvent, motionEvent, 1073872896);
        if (message.arg1 != 0) {
            notifyService(this.mServiceInterfaceForCurrentGesture, this.mSequencesInProgress.get(0), true);
            this.mSequencesInProgress.remove(0);
        }
        return true;
    }

    private void injectEventsMainThread(List<GestureDescription.GestureStep> list, IAccessibilityServiceClient iAccessibilityServiceClient, int i, int i2) {
        if (this.mIsDestroyed) {
            if (iAccessibilityServiceClient != null) {
                try {
                    iAccessibilityServiceClient.onPerformGestureResult(i, false);
                    return;
                } catch (RemoteException e) {
                    Slog.e(LOG_TAG, "Error sending status with mIsDestroyed to " + iAccessibilityServiceClient, e);
                    return;
                }
            }
            return;
        }
        if (getNext() == null) {
            notifyService(iAccessibilityServiceClient, i, false);
            return;
        }
        boolean newGestureTriesToContinueOldOne = newGestureTriesToContinueOldOne(list);
        if (newGestureTriesToContinueOldOne && (iAccessibilityServiceClient != this.mServiceInterfaceForCurrentGesture || !prepareToContinueOldGesture(list))) {
            cancelAnyPendingInjectedEvents();
            notifyService(iAccessibilityServiceClient, i, false);
            return;
        }
        if (!newGestureTriesToContinueOldOne) {
            cancelAnyPendingInjectedEvents();
            cancelAnyGestureInProgress(4098);
        }
        this.mServiceInterfaceForCurrentGesture = iAccessibilityServiceClient;
        long uptimeMillis = SystemClock.uptimeMillis();
        List<MotionEvent> motionEventsFromGestureSteps = getMotionEventsFromGestureSteps(list, this.mSequencesInProgress.size() == 0 ? uptimeMillis : this.mLastScheduledEventTime);
        if (motionEventsFromGestureSteps.isEmpty()) {
            notifyService(iAccessibilityServiceClient, i, false);
            return;
        }
        this.mSequencesInProgress.add(i);
        int i3 = 0;
        while (i3 < motionEventsFromGestureSteps.size()) {
            MotionEvent motionEvent = motionEventsFromGestureSteps.get(i3);
            motionEvent.setDisplayId(i2);
            Message obtainMessage = this.mHandler.obtainMessage(1, i3 == motionEventsFromGestureSteps.size() - 1 ? 1 : 0, 0, motionEvent);
            this.mLastScheduledEventTime = motionEvent.getEventTime();
            this.mHandler.sendMessageDelayed(obtainMessage, Math.max(0L, motionEvent.getEventTime() - uptimeMillis));
            i3++;
        }
    }

    private boolean newGestureTriesToContinueOldOne(List<GestureDescription.GestureStep> list) {
        if (list.isEmpty()) {
            return false;
        }
        GestureDescription.GestureStep gestureStep = list.get(0);
        for (int i = 0; i < gestureStep.numTouchPoints; i++) {
            if (!gestureStep.touchPoints[i].mIsStartOfPath) {
                return true;
            }
        }
        return false;
    }

    private boolean prepareToContinueOldGesture(List<GestureDescription.GestureStep> list) {
        if (list.isEmpty() || this.mLastTouchPoints == null || this.mNumLastTouchPoints == 0) {
            return false;
        }
        GestureDescription.GestureStep gestureStep = list.get(0);
        int i = 0;
        for (int i2 = 0; i2 < gestureStep.numTouchPoints; i2++) {
            GestureDescription.TouchPoint touchPoint = gestureStep.touchPoints[i2];
            if (!touchPoint.mIsStartOfPath) {
                int i3 = this.mStrokeIdToPointerId.get(touchPoint.mContinuedStrokeId, -1);
                if (i3 == -1) {
                    Slog.w(LOG_TAG, "Can't continue gesture due to unknown continued stroke id in " + touchPoint);
                    return false;
                }
                this.mStrokeIdToPointerId.put(touchPoint.mStrokeId, i3);
                int findPointByStrokeId = findPointByStrokeId(this.mLastTouchPoints, this.mNumLastTouchPoints, touchPoint.mContinuedStrokeId);
                if (findPointByStrokeId < 0) {
                    Slog.w(LOG_TAG, "Can't continue gesture due continued gesture id of " + touchPoint + " not matching any previous strokes in " + Arrays.asList(this.mLastTouchPoints));
                    return false;
                }
                GestureDescription.TouchPoint touchPoint2 = this.mLastTouchPoints[findPointByStrokeId];
                if (touchPoint2.mIsEndOfPath || touchPoint2.mX != touchPoint.mX || touchPoint2.mY != touchPoint.mY) {
                    Slog.w(LOG_TAG, "Can't continue gesture due to points mismatch between " + this.mLastTouchPoints[findPointByStrokeId] + " and " + touchPoint);
                    return false;
                }
                touchPoint2.mStrokeId = touchPoint.mStrokeId;
            }
            i++;
        }
        for (int i4 = 0; i4 < this.mNumLastTouchPoints; i4++) {
            if (!this.mLastTouchPoints[i4].mIsEndOfPath) {
                i--;
            }
        }
        return i == 0;
    }

    private void sendMotionEventToNext(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        if (getNext() != null) {
            super.onMotionEvent(motionEvent, motionEvent2, i);
            if (motionEvent.getActionMasked() == 0) {
                this.mOpenGesturesInProgress.put(motionEvent.getSource(), Boolean.TRUE);
            }
            if (motionEvent.getActionMasked() == 1 || motionEvent.getActionMasked() == 3) {
                this.mOpenGesturesInProgress.put(motionEvent.getSource(), Boolean.FALSE);
            }
        }
    }

    private void cancelAnyGestureInProgress(int i) {
        if (getNext() != null) {
            SparseArray<Boolean> sparseArray = this.mOpenGesturesInProgress;
            Boolean bool = Boolean.FALSE;
            if (sparseArray.get(i, bool).booleanValue()) {
                long uptimeMillis = SystemClock.uptimeMillis();
                MotionEvent obtainMotionEvent = obtainMotionEvent(uptimeMillis, uptimeMillis, 3, getLastTouchPoints(), 1);
                sendMotionEventToNext(obtainMotionEvent, obtainMotionEvent, 1073872896);
                this.mOpenGesturesInProgress.put(i, bool);
            }
        }
    }

    private void cancelAnyPendingInjectedEvents() {
        if (this.mHandler.hasMessages(1)) {
            this.mHandler.removeMessages(1);
            cancelAnyGestureInProgress(4098);
            for (int size = this.mSequencesInProgress.size() - 1; size >= 0; size--) {
                notifyService(this.mServiceInterfaceForCurrentGesture, this.mSequencesInProgress.get(size), false);
                this.mSequencesInProgress.remove(size);
            }
        } else if (this.mNumLastTouchPoints != 0) {
            cancelAnyGestureInProgress(4098);
        }
        this.mNumLastTouchPoints = 0;
        this.mStrokeIdToPointerId.clear();
    }

    private void notifyService(IAccessibilityServiceClient iAccessibilityServiceClient, int i, boolean z) {
        try {
            iAccessibilityServiceClient.onPerformGestureResult(i, z);
        } catch (Exception e) {
            Slog.e(LOG_TAG, "Error sending motion event injection status to " + this.mServiceInterfaceForCurrentGesture, e);
        }
    }

    private List<MotionEvent> getMotionEventsFromGestureSteps(List<GestureDescription.GestureStep> list, long j) {
        ArrayList arrayList = new ArrayList();
        GestureDescription.TouchPoint[] lastTouchPoints = getLastTouchPoints();
        for (int i = 0; i < list.size(); i++) {
            GestureDescription.GestureStep gestureStep = list.get(i);
            int i2 = gestureStep.numTouchPoints;
            if (i2 > lastTouchPoints.length) {
                this.mNumLastTouchPoints = 0;
                arrayList.clear();
                return arrayList;
            }
            appendMoveEventIfNeeded(arrayList, gestureStep.touchPoints, i2, j + gestureStep.timeSinceGestureStart);
            appendUpEvents(arrayList, gestureStep.touchPoints, i2, j + gestureStep.timeSinceGestureStart);
            appendDownEvents(arrayList, gestureStep.touchPoints, i2, j + gestureStep.timeSinceGestureStart);
        }
        return arrayList;
    }

    private GestureDescription.TouchPoint[] getLastTouchPoints() {
        if (this.mLastTouchPoints == null) {
            int maxStrokeCount = GestureDescription.getMaxStrokeCount();
            this.mLastTouchPoints = new GestureDescription.TouchPoint[maxStrokeCount];
            for (int i = 0; i < maxStrokeCount; i++) {
                this.mLastTouchPoints[i] = new GestureDescription.TouchPoint();
            }
        }
        return this.mLastTouchPoints;
    }

    private void appendMoveEventIfNeeded(List<MotionEvent> list, GestureDescription.TouchPoint[] touchPointArr, int i, long j) {
        GestureDescription.TouchPoint[] lastTouchPoints = getLastTouchPoints();
        boolean z = false;
        for (int i2 = 0; i2 < i; i2++) {
            int findPointByStrokeId = findPointByStrokeId(lastTouchPoints, this.mNumLastTouchPoints, touchPointArr[i2].mStrokeId);
            if (findPointByStrokeId >= 0) {
                GestureDescription.TouchPoint touchPoint = lastTouchPoints[findPointByStrokeId];
                float f = touchPoint.mX;
                GestureDescription.TouchPoint touchPoint2 = touchPointArr[i2];
                z |= (f == touchPoint2.mX && touchPoint.mY == touchPoint2.mY) ? false : true;
                touchPoint.copyFrom(touchPoint2);
            }
        }
        if (z) {
            list.add(obtainMotionEvent(this.mDownTime, j, 2, lastTouchPoints, this.mNumLastTouchPoints));
        }
    }

    private void appendUpEvents(List<MotionEvent> list, GestureDescription.TouchPoint[] touchPointArr, int i, long j) {
        int findPointByStrokeId;
        int i2;
        GestureDescription.TouchPoint[] lastTouchPoints = getLastTouchPoints();
        for (int i3 = 0; i3 < i; i3++) {
            GestureDescription.TouchPoint touchPoint = touchPointArr[i3];
            if (touchPoint.mIsEndOfPath && (findPointByStrokeId = findPointByStrokeId(lastTouchPoints, this.mNumLastTouchPoints, touchPoint.mStrokeId)) >= 0) {
                int i4 = this.mNumLastTouchPoints;
                list.add(obtainMotionEvent(this.mDownTime, j, (i4 != 1 ? 6 : 1) | (findPointByStrokeId << 8), lastTouchPoints, i4));
                while (true) {
                    i2 = this.mNumLastTouchPoints;
                    if (findPointByStrokeId >= i2 - 1) {
                        break;
                    }
                    GestureDescription.TouchPoint touchPoint2 = lastTouchPoints[findPointByStrokeId];
                    findPointByStrokeId++;
                    touchPoint2.copyFrom(this.mLastTouchPoints[findPointByStrokeId]);
                }
                int i5 = i2 - 1;
                this.mNumLastTouchPoints = i5;
                if (i5 == 0) {
                    this.mStrokeIdToPointerId.clear();
                }
            }
        }
    }

    private void appendDownEvents(List<MotionEvent> list, GestureDescription.TouchPoint[] touchPointArr, int i, long j) {
        GestureDescription.TouchPoint[] lastTouchPoints = getLastTouchPoints();
        for (int i2 = 0; i2 < i; i2++) {
            GestureDescription.TouchPoint touchPoint = touchPointArr[i2];
            if (touchPoint.mIsStartOfPath) {
                int i3 = this.mNumLastTouchPoints;
                this.mNumLastTouchPoints = i3 + 1;
                lastTouchPoints[i3].copyFrom(touchPoint);
                int i4 = this.mNumLastTouchPoints;
                int i5 = i4 == 1 ? 0 : 5;
                if (i5 == 0) {
                    this.mDownTime = j;
                }
                list.add(obtainMotionEvent(this.mDownTime, j, i5 | (i2 << 8), lastTouchPoints, i4));
            }
        }
    }

    private MotionEvent obtainMotionEvent(long j, long j2, int i, GestureDescription.TouchPoint[] touchPointArr, int i2) {
        MotionEvent.PointerCoords[] pointerCoordsArr = sPointerCoords;
        if (pointerCoordsArr == null || pointerCoordsArr.length < i2) {
            sPointerCoords = new MotionEvent.PointerCoords[i2];
            for (int i3 = 0; i3 < i2; i3++) {
                sPointerCoords[i3] = new MotionEvent.PointerCoords();
            }
        }
        MotionEvent.PointerProperties[] pointerPropertiesArr = sPointerProps;
        if (pointerPropertiesArr == null || pointerPropertiesArr.length < i2) {
            sPointerProps = new MotionEvent.PointerProperties[i2];
            for (int i4 = 0; i4 < i2; i4++) {
                sPointerProps[i4] = new MotionEvent.PointerProperties();
            }
        }
        for (int i5 = 0; i5 < i2; i5++) {
            int i6 = this.mStrokeIdToPointerId.get(touchPointArr[i5].mStrokeId, -1);
            if (i6 == -1) {
                i6 = getUnusedPointerId();
                this.mStrokeIdToPointerId.put(touchPointArr[i5].mStrokeId, i6);
            }
            MotionEvent.PointerProperties pointerProperties = sPointerProps[i5];
            pointerProperties.id = i6;
            pointerProperties.toolType = 0;
            sPointerCoords[i5].clear();
            MotionEvent.PointerCoords pointerCoords = sPointerCoords[i5];
            pointerCoords.pressure = 1.0f;
            pointerCoords.size = 1.0f;
            GestureDescription.TouchPoint touchPoint = touchPointArr[i5];
            pointerCoords.x = touchPoint.mX;
            pointerCoords.y = touchPoint.mY;
        }
        return MotionEvent.obtain(j, j2, i, i2, sPointerProps, sPointerCoords, 0, 0, 1.0f, 1.0f, -1, 0, 4098, 0);
    }

    private static int findPointByStrokeId(GestureDescription.TouchPoint[] touchPointArr, int i, int i2) {
        for (int i3 = 0; i3 < i; i3++) {
            if (touchPointArr[i3].mStrokeId == i2) {
                return i3;
            }
        }
        return -1;
    }

    private int getUnusedPointerId() {
        int i = 0;
        while (this.mStrokeIdToPointerId.indexOfValue(i) >= 0) {
            i++;
            if (i >= 10) {
                return 10;
            }
        }
        return i;
    }
}
