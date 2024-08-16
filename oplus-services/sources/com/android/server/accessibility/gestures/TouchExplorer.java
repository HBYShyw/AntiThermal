package com.android.server.accessibility.gestures;

import android.accessibilityservice.AccessibilityGestureEvent;
import android.content.Context;
import android.graphics.Region;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.os.Handler;
import android.util.Log;
import android.util.Slog;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.accessibility.AccessibilityManagerService;
import com.android.server.accessibility.BaseEventStreamTransformation;
import com.android.server.accessibility.EventStreamTransformation;
import com.android.server.accessibility.gestures.GestureManifold;
import com.android.server.accessibility.gestures.TouchState;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class TouchExplorer extends BaseEventStreamTransformation implements GestureManifold.Listener {
    private static final float EDGE_SWIPE_HEIGHT_CM = 0.25f;
    private static final int EXIT_GESTURE_DETECTION_TIMEOUT = 2000;
    private static final long LOGGING_FLAGS = 12288;
    private static final float MAX_DRAGGING_ANGLE_COS = 0.52532196f;
    private final AccessibilityManagerService mAms;
    private final Context mContext;
    private final int mDetermineUserIntentTimeout;
    private final EventDispatcher mDispatcher;
    private int mDisplayId;
    private final int mDoubleTapSlop;
    private int mDraggingPointerId;
    private final float mEdgeSwipeHeightPixels;
    private final ExitGestureDetectionModeDelayed mExitGestureDetectionModeDelayed;
    private Region mGestureDetectionPassthroughRegion;
    private final GestureManifold mGestureDetector;
    private final Handler mHandler;
    private final TouchState.ReceivedPointerTracker mReceivedPointerTracker;
    private final SendHoverEnterAndMoveDelayed mSendHoverEnterAndMoveDelayed;
    private final SendHoverExitDelayed mSendHoverExitDelayed;
    private final SendAccessibilityEventDelayed mSendTouchExplorationEndDelayed;
    private final SendAccessibilityEventDelayed mSendTouchInteractionEndDelayed;
    private TouchState mState;
    private Region mTouchExplorationPassthroughRegion;
    private final int mTouchSlop;
    private static final String LOG_TAG = "TouchExplorer";
    static final boolean DEBUG = Log.isLoggable(LOG_TAG, 3);

    public TouchExplorer(Context context, AccessibilityManagerService accessibilityManagerService) {
        this(context, accessibilityManagerService, null);
    }

    public TouchExplorer(Context context, AccessibilityManagerService accessibilityManagerService, GestureManifold gestureManifold) {
        this(context, accessibilityManagerService, gestureManifold, new Handler(context.getMainLooper()));
    }

    @VisibleForTesting
    TouchExplorer(Context context, AccessibilityManagerService accessibilityManagerService, GestureManifold gestureManifold, Handler handler) {
        this.mDisplayId = -1;
        this.mContext = context;
        int displayId = context.getDisplayId();
        this.mDisplayId = displayId;
        this.mAms = accessibilityManagerService;
        TouchState touchState = new TouchState(displayId, accessibilityManagerService);
        this.mState = touchState;
        this.mReceivedPointerTracker = touchState.getReceivedPointerTracker();
        this.mDispatcher = new EventDispatcher(context, accessibilityManagerService, super.getNext(), this.mState);
        int doubleTapTimeout = ViewConfiguration.getDoubleTapTimeout();
        this.mDetermineUserIntentTimeout = doubleTapTimeout;
        this.mDoubleTapSlop = ViewConfiguration.get(context).getScaledDoubleTapSlop();
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mEdgeSwipeHeightPixels = (context.getResources().getDisplayMetrics().ydpi / GestureUtils.CM_PER_INCH) * EDGE_SWIPE_HEIGHT_CM;
        this.mHandler = handler;
        this.mExitGestureDetectionModeDelayed = new ExitGestureDetectionModeDelayed();
        this.mSendHoverEnterAndMoveDelayed = new SendHoverEnterAndMoveDelayed();
        this.mSendHoverExitDelayed = new SendHoverExitDelayed();
        this.mSendTouchExplorationEndDelayed = new SendAccessibilityEventDelayed(1024, doubleTapTimeout);
        this.mSendTouchInteractionEndDelayed = new SendAccessibilityEventDelayed(AudioDevice.OUT_AUX_LINE, doubleTapTimeout);
        if (gestureManifold == null) {
            this.mGestureDetector = new GestureManifold(context, this, this.mState, handler);
        } else {
            this.mGestureDetector = gestureManifold;
        }
        this.mGestureDetectionPassthroughRegion = new Region();
        this.mTouchExplorationPassthroughRegion = new Region();
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void clearEvents(int i) {
        if (i == 4098) {
            clear();
        }
        super.clearEvents(i);
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void onDestroy() {
        clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clear() {
        MotionEvent lastReceivedEvent = this.mState.getLastReceivedEvent();
        if (lastReceivedEvent != null) {
            clear(lastReceivedEvent, 33554432);
        }
    }

    private void clear(MotionEvent motionEvent, int i) {
        if (this.mState.isTouchExploring()) {
            sendHoverExitAndTouchExplorationGestureEndIfNeeded(i);
        }
        this.mDraggingPointerId = -1;
        this.mDispatcher.sendUpForInjectedDownPointers(motionEvent, i);
        this.mSendHoverEnterAndMoveDelayed.cancel();
        this.mSendHoverExitDelayed.cancel();
        this.mExitGestureDetectionModeDelayed.cancel();
        this.mSendTouchExplorationEndDelayed.cancel();
        this.mSendTouchInteractionEndDelayed.cancel();
        this.mGestureDetector.clear();
        this.mDispatcher.clear();
        this.mState.clear();
        this.mAms.onTouchInteractionEnd();
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void onMotionEvent(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        if (this.mAms.getTraceManager().isA11yTracingEnabledForTypes(LOGGING_FLAGS)) {
            this.mAms.getTraceManager().logTrace("TouchExplorer.onMotionEvent", LOGGING_FLAGS, "event=" + motionEvent + ";rawEvent=" + motionEvent2 + ";policyFlags=" + i);
        }
        if (!motionEvent.isFromSource(4098)) {
            super.onMotionEvent(motionEvent, motionEvent2, i);
            return;
        }
        try {
            checkForMalformedEvent(motionEvent);
            checkForMalformedEvent(motionEvent2);
            if (DEBUG) {
                Slog.d(LOG_TAG, "Received event: " + motionEvent + ", policyFlags=0x" + Integer.toHexString(i));
                Slog.d(LOG_TAG, this.mState.toString());
            }
            this.mState.onReceivedMotionEvent(motionEvent, motionEvent2, i);
            if (shouldPerformGestureDetection(motionEvent) && this.mGestureDetector.onMotionEvent(motionEvent, motionEvent2, i)) {
                return;
            }
            if (motionEvent.getActionMasked() == 3) {
                clear(motionEvent, i);
                return;
            }
            if (this.mState.isClear()) {
                handleMotionEventStateClear(motionEvent, motionEvent2, i);
                return;
            }
            if (this.mState.isTouchInteracting()) {
                handleMotionEventStateTouchInteracting(motionEvent, motionEvent2, i);
                return;
            }
            if (this.mState.isTouchExploring()) {
                handleMotionEventStateTouchExploring(motionEvent, motionEvent2, i);
                return;
            }
            if (this.mState.isDragging()) {
                handleMotionEventStateDragging(motionEvent, motionEvent2, i);
                return;
            }
            if (this.mState.isDelegating()) {
                handleMotionEventStateDelegating(motionEvent, motionEvent2, i);
                return;
            }
            if (this.mState.isGestureDetecting()) {
                this.mSendTouchInteractionEndDelayed.cancel();
                if (this.mState.isServiceDetectingGestures()) {
                    this.mAms.sendMotionEventToListeningServices(motionEvent2);
                    return;
                }
                return;
            }
            Slog.e(LOG_TAG, "Illegal state: " + this.mState);
            clear(motionEvent, i);
        } catch (IllegalArgumentException e) {
            Slog.e(LOG_TAG, "Ignoring malformed event: " + motionEvent.toString(), e);
        }
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (this.mAms.getTraceManager().isA11yTracingEnabledForTypes(LOGGING_FLAGS)) {
            this.mAms.getTraceManager().logTrace("TouchExplorer.onAccessibilityEvent", LOGGING_FLAGS, "event=" + accessibilityEvent);
        }
        if (accessibilityEvent.getEventType() == 256) {
            sendsPendingA11yEventsIfNeeded();
        }
        this.mState.onReceivedAccessibilityEvent(accessibilityEvent);
        super.onAccessibilityEvent(accessibilityEvent);
    }

    private void sendsPendingA11yEventsIfNeeded() {
        if (this.mSendHoverExitDelayed.isPending()) {
            return;
        }
        if (this.mSendTouchExplorationEndDelayed.isPending()) {
            this.mSendTouchExplorationEndDelayed.cancel();
            this.mDispatcher.sendAccessibilityEvent(1024);
        }
        if (this.mSendTouchInteractionEndDelayed.isPending()) {
            this.mSendTouchInteractionEndDelayed.cancel();
            this.mDispatcher.sendAccessibilityEvent(AudioDevice.OUT_AUX_LINE);
        }
    }

    @Override // com.android.server.accessibility.gestures.GestureManifold.Listener
    public void onDoubleTapAndHold(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        if (this.mAms.getTraceManager().isA11yTracingEnabledForTypes(LOGGING_FLAGS)) {
            this.mAms.getTraceManager().logTrace("TouchExplorer.onDoubleTapAndHold", LOGGING_FLAGS, "event=" + motionEvent + ";rawEvent=" + motionEvent2 + ";policyFlags=" + i);
        }
        if (this.mDispatcher.longPressWithTouchEvents(motionEvent, i)) {
            sendHoverExitAndTouchExplorationGestureEndIfNeeded(i);
            if (isSendMotionEventsEnabled()) {
                dispatchGesture(new AccessibilityGestureEvent(18, this.mDisplayId, this.mGestureDetector.getMotionEvents()));
            }
            this.mState.startDelegating();
        }
    }

    @Override // com.android.server.accessibility.gestures.GestureManifold.Listener
    public boolean onDoubleTap(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        if (this.mAms.getTraceManager().isA11yTracingEnabledForTypes(LOGGING_FLAGS)) {
            this.mAms.getTraceManager().logTrace("TouchExplorer.onDoubleTap", LOGGING_FLAGS, "event=" + motionEvent + ";rawEvent=" + motionEvent2 + ";policyFlags=" + i);
        }
        this.mAms.onTouchInteractionEnd();
        this.mSendHoverEnterAndMoveDelayed.cancel();
        this.mSendHoverExitDelayed.cancel();
        if (isSendMotionEventsEnabled()) {
            dispatchGesture(new AccessibilityGestureEvent(17, this.mDisplayId, this.mGestureDetector.getMotionEvents()));
        }
        if (this.mSendTouchExplorationEndDelayed.isPending()) {
            this.mSendTouchExplorationEndDelayed.forceSendAndRemove();
        }
        this.mDispatcher.sendAccessibilityEvent(AudioDevice.OUT_AUX_LINE);
        this.mSendTouchInteractionEndDelayed.cancel();
        AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK.setDisplayID(this.mDisplayId);
        if (!this.mAms.performActionOnAccessibilityFocusedItem(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK)) {
            Slog.e(LOG_TAG, "ACTION_CLICK failed. Dispatching motion events to simulate click.");
            if (motionEvent != null && motionEvent2 != null) {
                this.mDispatcher.clickWithTouchEvents(motionEvent, motionEvent2, i);
            }
        }
        return true;
    }

    public void onDoubleTap() {
        onDoubleTap(this.mState.getLastReceivedEvent(), this.mState.getLastReceivedRawEvent(), this.mState.getLastReceivedPolicyFlags());
    }

    public void onDoubleTapAndHold() {
        onDoubleTapAndHold(this.mState.getLastReceivedEvent(), this.mState.getLastReceivedRawEvent(), this.mState.getLastReceivedPolicyFlags());
    }

    @Override // com.android.server.accessibility.gestures.GestureManifold.Listener
    public boolean onGestureStarted() {
        if (this.mAms.getTraceManager().isA11yTracingEnabledForTypes(LOGGING_FLAGS)) {
            this.mAms.getTraceManager().logTrace("TouchExplorer.onGestureStarted", LOGGING_FLAGS);
        }
        this.mSendHoverEnterAndMoveDelayed.cancel();
        this.mSendHoverExitDelayed.cancel();
        this.mExitGestureDetectionModeDelayed.post();
        this.mDispatcher.sendAccessibilityEvent(262144);
        return false;
    }

    @Override // com.android.server.accessibility.gestures.GestureManifold.Listener
    public boolean onGestureCompleted(AccessibilityGestureEvent accessibilityGestureEvent) {
        if (this.mAms.getTraceManager().isA11yTracingEnabledForTypes(LOGGING_FLAGS)) {
            this.mAms.getTraceManager().logTrace("TouchExplorer.onGestureCompleted", LOGGING_FLAGS, "event=" + accessibilityGestureEvent);
        }
        endGestureDetection(true);
        this.mSendTouchInteractionEndDelayed.cancel();
        dispatchGesture(accessibilityGestureEvent);
        return true;
    }

    @Override // com.android.server.accessibility.gestures.GestureManifold.Listener
    public boolean onGestureCancelled(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        if (this.mAms.getTraceManager().isA11yTracingEnabledForTypes(LOGGING_FLAGS)) {
            this.mAms.getTraceManager().logTrace("TouchExplorer.onGestureCancelled", LOGGING_FLAGS, "event=" + motionEvent + ";rawEvent=" + motionEvent2 + ";policyFlags=" + i);
        }
        if (this.mState.isGestureDetecting()) {
            endGestureDetection(motionEvent.getActionMasked() == 1);
            return true;
        }
        if (this.mState.isTouchExploring() && motionEvent.getActionMasked() == 2) {
            int primaryPointerId = 1 << this.mReceivedPointerTracker.getPrimaryPointerId();
            this.mSendHoverEnterAndMoveDelayed.addEvent(motionEvent, this.mState.getLastReceivedEvent());
            this.mSendHoverEnterAndMoveDelayed.forceSendAndRemove();
            this.mSendHoverExitDelayed.cancel();
            this.mDispatcher.sendMotionEvent(motionEvent, 7, motionEvent, primaryPointerId, i);
            return true;
        }
        if (isSendMotionEventsEnabled()) {
            dispatchGesture(new AccessibilityGestureEvent(0, this.mDisplayId, this.mGestureDetector.getMotionEvents()));
        }
        return false;
    }

    private void handleMotionEventStateClear(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        if (motionEvent.getActionMasked() != 0) {
            return;
        }
        handleActionDown(motionEvent, motionEvent2, i);
    }

    private void handleActionDown(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        this.mAms.onTouchInteractionStart();
        this.mSendHoverEnterAndMoveDelayed.cancel();
        this.mSendHoverEnterAndMoveDelayed.clear();
        this.mSendHoverExitDelayed.cancel();
        if (this.mState.isTouchExploring()) {
            sendHoverExitAndTouchExplorationGestureEndIfNeeded(i);
        }
        if (this.mState.isClear()) {
            if (!this.mSendHoverEnterAndMoveDelayed.isPending()) {
                int primaryPointerId = 1 << this.mReceivedPointerTracker.getPrimaryPointerId();
                if (this.mState.isServiceDetectingGestures()) {
                    this.mSendHoverEnterAndMoveDelayed.setPointerIdBits(primaryPointerId);
                    this.mSendHoverEnterAndMoveDelayed.setPolicyFlags(i);
                    this.mSendHoverEnterAndMoveDelayed.addEvent(motionEvent, motionEvent2);
                } else {
                    this.mSendHoverEnterAndMoveDelayed.post(motionEvent, motionEvent2, primaryPointerId, i);
                }
            } else {
                this.mSendHoverEnterAndMoveDelayed.addEvent(motionEvent, motionEvent2);
            }
            this.mSendTouchExplorationEndDelayed.forceSendAndRemove();
            this.mSendTouchInteractionEndDelayed.forceSendAndRemove();
            this.mDispatcher.sendAccessibilityEvent(AudioDevice.OUT_FM);
            if (this.mTouchExplorationPassthroughRegion.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                this.mState.startDelegating();
                MotionEvent obtainNoHistory = MotionEvent.obtainNoHistory(motionEvent);
                this.mDispatcher.sendMotionEvent(obtainNoHistory, obtainNoHistory.getAction(), motionEvent2, -1, i);
                this.mSendHoverEnterAndMoveDelayed.cancel();
            } else if (this.mGestureDetectionPassthroughRegion.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                this.mSendHoverEnterAndMoveDelayed.forceSendAndRemove();
            }
        } else {
            this.mSendTouchInteractionEndDelayed.cancel();
        }
        if (this.mState.isServiceDetectingGestures()) {
            this.mAms.sendMotionEventToListeningServices(motionEvent2);
        }
    }

    private void handleMotionEventStateTouchInteracting(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mSendTouchInteractionEndDelayed.cancel();
            handleActionDown(motionEvent, motionEvent2, i);
            return;
        }
        if (actionMasked == 1) {
            handleActionUp(motionEvent, motionEvent2, i);
            return;
        }
        if (actionMasked == 2) {
            handleActionMoveStateTouchInteracting(motionEvent, motionEvent2, i);
            return;
        }
        if (actionMasked == 5) {
            handleActionPointerDown(motionEvent, motionEvent2, i);
        } else if (actionMasked == 6 && this.mState.isServiceDetectingGestures()) {
            this.mAms.sendMotionEventToListeningServices(motionEvent2);
        }
    }

    private void handleMotionEventStateTouchExploring(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 1) {
            handleActionUp(motionEvent, motionEvent2, i);
        } else if (actionMasked == 2) {
            handleActionMoveStateTouchExploring(motionEvent, motionEvent2, i);
        } else {
            if (actionMasked != 5) {
                return;
            }
            handleActionPointerDown(motionEvent, motionEvent2, i);
        }
    }

    private void handleActionPointerDown(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        if (this.mSendHoverEnterAndMoveDelayed.isPending()) {
            this.mSendHoverEnterAndMoveDelayed.cancel();
            this.mSendHoverExitDelayed.cancel();
        } else {
            sendHoverExitAndTouchExplorationGestureEndIfNeeded(i);
        }
        if (this.mState.isServiceDetectingGestures()) {
            this.mAms.sendMotionEventToListeningServices(motionEvent2);
        }
    }

    private void handleActionMoveStateTouchInteracting(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        int findPointerIndex = motionEvent.findPointerIndex(this.mReceivedPointerTracker.getPrimaryPointerId());
        if (this.mState.isServiceDetectingGestures()) {
            this.mAms.sendMotionEventToListeningServices(motionEvent2);
            this.mSendHoverEnterAndMoveDelayed.addEvent(motionEvent, motionEvent2);
            return;
        }
        int pointerCount = motionEvent.getPointerCount();
        if (pointerCount == 1) {
            if (this.mSendHoverEnterAndMoveDelayed.isPending()) {
                this.mSendHoverEnterAndMoveDelayed.addEvent(motionEvent, motionEvent2);
                return;
            }
            return;
        }
        if (pointerCount == 2) {
            if (!this.mGestureDetector.isMultiFingerGesturesEnabled() || this.mGestureDetector.isTwoFingerPassthroughEnabled()) {
                this.mSendHoverEnterAndMoveDelayed.cancel();
                this.mSendHoverExitDelayed.cancel();
                if (this.mGestureDetector.isMultiFingerGesturesEnabled() && this.mGestureDetector.isTwoFingerPassthroughEnabled()) {
                    if (findPointerIndex < 0) {
                        return;
                    }
                    for (int i2 = 0; i2 < motionEvent.getPointerCount(); i2++) {
                        int pointerId = motionEvent.getPointerId(i2);
                        if (!this.mReceivedPointerTracker.isReceivedPointerDown(pointerId)) {
                            Slog.e(LOG_TAG, "Invalid pointer id: " + pointerId);
                        }
                        if (Math.hypot(this.mReceivedPointerTracker.getReceivedPointerDownX(pointerId) - motionEvent2.getX(i2), this.mReceivedPointerTracker.getReceivedPointerDownY(pointerId) - motionEvent2.getY(i2)) < this.mTouchSlop * 2) {
                            return;
                        }
                    }
                }
                MotionEvent obtainNoHistory = MotionEvent.obtainNoHistory(motionEvent);
                if (isDraggingGesture(obtainNoHistory)) {
                    if (isSendMotionEventsEnabled()) {
                        dispatchGesture(new AccessibilityGestureEvent(-1, this.mDisplayId, this.mGestureDetector.getMotionEvents()));
                    }
                    computeDraggingPointerIdIfNeeded(obtainNoHistory);
                    int i3 = 1 << this.mDraggingPointerId;
                    obtainNoHistory.setEdgeFlags(this.mReceivedPointerTracker.getLastReceivedDownEdgeFlags());
                    MotionEvent computeDownEventForDrag = computeDownEventForDrag(obtainNoHistory);
                    if (computeDownEventForDrag != null) {
                        this.mDispatcher.sendMotionEvent(computeDownEventForDrag, 0, motionEvent2, i3, i);
                        this.mDispatcher.sendMotionEvent(obtainNoHistory, 2, motionEvent2, i3, i);
                    } else {
                        this.mDispatcher.sendMotionEvent(obtainNoHistory, 0, motionEvent2, i3, i);
                    }
                    this.mState.startDragging();
                    return;
                }
                if (isSendMotionEventsEnabled()) {
                    dispatchGesture(new AccessibilityGestureEvent(-1, this.mDisplayId, this.mGestureDetector.getMotionEvents()));
                }
                this.mState.startDelegating();
                this.mDispatcher.sendDownForAllNotInjectedPointers(obtainNoHistory, i);
                return;
            }
            return;
        }
        if (this.mGestureDetector.isMultiFingerGesturesEnabled()) {
            if (this.mGestureDetector.isTwoFingerPassthroughEnabled() && motionEvent.getPointerCount() == 3 && allPointersDownOnBottomEdge(motionEvent)) {
                if (DEBUG) {
                    Slog.d(LOG_TAG, "Three-finger edge swipe detected.");
                }
                if (isSendMotionEventsEnabled()) {
                    dispatchGesture(new AccessibilityGestureEvent(-1, this.mDisplayId, this.mGestureDetector.getMotionEvents()));
                }
                this.mState.startDelegating();
                if (this.mState.isTouchExploring()) {
                    this.mDispatcher.sendDownForAllNotInjectedPointers(motionEvent, i);
                    return;
                } else {
                    this.mDispatcher.sendDownForAllNotInjectedPointersWithOriginalDown(motionEvent, i);
                    return;
                }
            }
            return;
        }
        if (isSendMotionEventsEnabled()) {
            dispatchGesture(new AccessibilityGestureEvent(-1, this.mDisplayId, this.mGestureDetector.getMotionEvents()));
        }
        this.mState.startDelegating();
        this.mDispatcher.sendDownForAllNotInjectedPointers(MotionEvent.obtainNoHistory(motionEvent), i);
    }

    private void handleActionUp(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        if (this.mState.isServiceDetectingGestures() && this.mState.isTouchInteracting()) {
            this.mAms.sendMotionEventToListeningServices(motionEvent2);
        }
        this.mAms.onTouchInteractionEnd();
        int pointerId = 1 << motionEvent.getPointerId(motionEvent.getActionIndex());
        if (this.mSendHoverEnterAndMoveDelayed.isPending()) {
            this.mSendHoverExitDelayed.post(motionEvent, motionEvent2, pointerId, i);
        } else {
            sendHoverExitAndTouchExplorationGestureEndIfNeeded(i);
        }
        if (this.mSendTouchInteractionEndDelayed.isPending()) {
            return;
        }
        this.mSendTouchInteractionEndDelayed.post();
    }

    private void handleActionMoveStateTouchExploring(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        int primaryPointerId = this.mReceivedPointerTracker.getPrimaryPointerId();
        int i2 = 1 << primaryPointerId;
        int findPointerIndex = motionEvent.findPointerIndex(primaryPointerId);
        int pointerCount = motionEvent.getPointerCount();
        if (pointerCount == 1) {
            sendTouchExplorationGestureStartAndHoverEnterIfNeeded(i);
            this.mDispatcher.sendMotionEvent(motionEvent, 7, motionEvent2, i2, i);
            return;
        }
        if (pointerCount == 2) {
            if (!this.mGestureDetector.isMultiFingerGesturesEnabled() || this.mGestureDetector.isTwoFingerPassthroughEnabled()) {
                if (this.mSendHoverEnterAndMoveDelayed.isPending()) {
                    this.mSendHoverEnterAndMoveDelayed.cancel();
                    this.mSendHoverExitDelayed.cancel();
                }
                if (Math.hypot(this.mReceivedPointerTracker.getReceivedPointerDownX(primaryPointerId) - motionEvent2.getX(findPointerIndex), this.mReceivedPointerTracker.getReceivedPointerDownY(primaryPointerId) - motionEvent2.getY(findPointerIndex)) > this.mDoubleTapSlop) {
                    handleActionMoveStateTouchInteracting(motionEvent, motionEvent2, i);
                    return;
                } else {
                    sendHoverExitAndTouchExplorationGestureEndIfNeeded(i);
                    return;
                }
            }
            return;
        }
        if (this.mGestureDetector.isMultiFingerGesturesEnabled()) {
            return;
        }
        if (this.mSendHoverEnterAndMoveDelayed.isPending()) {
            this.mSendHoverEnterAndMoveDelayed.cancel();
            this.mSendHoverExitDelayed.cancel();
        } else {
            sendHoverExitAndTouchExplorationGestureEndIfNeeded(i);
        }
        handleActionMoveStateTouchInteracting(motionEvent, motionEvent2, i);
    }

    private void handleMotionEventStateDragging(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        int i2;
        if (!this.mGestureDetector.isMultiFingerGesturesEnabled() || this.mGestureDetector.isTwoFingerPassthroughEnabled()) {
            if (motionEvent.findPointerIndex(this.mDraggingPointerId) == -1) {
                Slog.e(LOG_TAG, "mDraggingPointerId doesn't match any pointers on current event. mDraggingPointerId: " + Integer.toString(this.mDraggingPointerId) + ", Event: " + motionEvent);
                this.mDraggingPointerId = -1;
                i2 = 0;
            } else {
                i2 = 1 << this.mDraggingPointerId;
            }
            int i3 = i2;
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 0) {
                Slog.e(LOG_TAG, "Dragging state can be reached only if two pointers are already down");
                clear(motionEvent, i);
                return;
            }
            if (actionMasked == 1) {
                if (motionEvent.getPointerId(GestureUtils.getActionIndex(motionEvent)) == this.mDraggingPointerId) {
                    this.mDraggingPointerId = -1;
                    this.mDispatcher.sendMotionEvent(motionEvent, 1, motionEvent2, i3, i);
                }
                this.mAms.onTouchInteractionEnd();
                this.mDispatcher.sendAccessibilityEvent(AudioDevice.OUT_AUX_LINE);
                return;
            }
            if (actionMasked != 2) {
                if (actionMasked != 5) {
                    if (actionMasked != 6) {
                        return;
                    }
                    this.mDraggingPointerId = -1;
                    this.mDispatcher.sendMotionEvent(motionEvent, 1, motionEvent2, i3, i);
                    return;
                }
                if (this.mState.isServiceDetectingGestures()) {
                    this.mAms.sendMotionEventToListeningServices(motionEvent2);
                    return;
                }
                this.mState.startDelegating();
                if (this.mDraggingPointerId != -1) {
                    this.mDispatcher.sendMotionEvent(motionEvent, 1, motionEvent2, i3, i);
                }
                this.mDispatcher.sendDownForAllNotInjectedPointers(motionEvent, i);
                return;
            }
            if (this.mDraggingPointerId == -1) {
                return;
            }
            if (this.mState.isServiceDetectingGestures()) {
                this.mAms.sendMotionEventToListeningServices(motionEvent2);
                computeDraggingPointerIdIfNeeded(motionEvent);
                this.mDispatcher.sendMotionEvent(motionEvent, 2, motionEvent2, i3, i);
                return;
            }
            int pointerCount = motionEvent.getPointerCount();
            if (pointerCount != 1) {
                if (pointerCount == 2) {
                    if (isDraggingGesture(motionEvent)) {
                        computeDraggingPointerIdIfNeeded(motionEvent);
                        this.mDispatcher.sendMotionEvent(motionEvent, 2, motionEvent2, i3, i);
                        return;
                    }
                    this.mState.startDelegating();
                    this.mDraggingPointerId = -1;
                    MotionEvent obtainNoHistory = MotionEvent.obtainNoHistory(motionEvent);
                    this.mDispatcher.sendMotionEvent(obtainNoHistory, 1, motionEvent2, i3, i);
                    this.mDispatcher.sendDownForAllNotInjectedPointers(obtainNoHistory, i);
                    return;
                }
                if (this.mState.isServiceDetectingGestures()) {
                    this.mAms.sendMotionEventToListeningServices(motionEvent2);
                    return;
                }
                this.mState.startDelegating();
                this.mDraggingPointerId = -1;
                MotionEvent obtainNoHistory2 = MotionEvent.obtainNoHistory(motionEvent);
                this.mDispatcher.sendMotionEvent(obtainNoHistory2, 1, motionEvent2, i3, i);
                this.mDispatcher.sendDownForAllNotInjectedPointers(obtainNoHistory2, i);
            }
        }
    }

    private void handleMotionEventStateDelegating(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            Slog.e(LOG_TAG, "Delegating state can only be reached if there is at least one pointer down!");
            clear(motionEvent, i);
        } else {
            if (actionMasked == 1) {
                this.mDispatcher.sendMotionEvent(motionEvent, motionEvent.getAction(), motionEvent2, -1, i);
                this.mAms.onTouchInteractionEnd();
                this.mDispatcher.clear();
                this.mDispatcher.sendAccessibilityEvent(AudioDevice.OUT_AUX_LINE);
                return;
            }
            this.mDispatcher.sendMotionEvent(motionEvent, motionEvent.getAction(), motionEvent2, -1, i);
        }
    }

    private void endGestureDetection(boolean z) {
        this.mAms.onTouchInteractionEnd();
        this.mDispatcher.sendAccessibilityEvent(524288);
        if (z) {
            this.mDispatcher.sendAccessibilityEvent(AudioDevice.OUT_AUX_LINE);
        }
        this.mExitGestureDetectionModeDelayed.cancel();
    }

    private void sendHoverExitAndTouchExplorationGestureEndIfNeeded(int i) {
        MotionEvent lastInjectedHoverEvent = this.mState.getLastInjectedHoverEvent();
        if (lastInjectedHoverEvent == null || lastInjectedHoverEvent.getActionMasked() == 10) {
            return;
        }
        int pointerIdBits = lastInjectedHoverEvent.getPointerIdBits();
        if (!this.mSendTouchExplorationEndDelayed.isPending()) {
            this.mSendTouchExplorationEndDelayed.post();
        }
        this.mDispatcher.sendMotionEvent(lastInjectedHoverEvent, 10, this.mState.getLastReceivedEvent(), pointerIdBits, i);
    }

    private void sendTouchExplorationGestureStartAndHoverEnterIfNeeded(int i) {
        MotionEvent lastInjectedHoverEvent = this.mState.getLastInjectedHoverEvent();
        if (lastInjectedHoverEvent == null || lastInjectedHoverEvent.getActionMasked() != 10) {
            return;
        }
        this.mDispatcher.sendMotionEvent(lastInjectedHoverEvent, 9, this.mState.getLastReceivedEvent(), lastInjectedHoverEvent.getPointerIdBits(), i);
    }

    private boolean isDraggingGesture(MotionEvent motionEvent) {
        return GestureUtils.isDraggingGesture(this.mReceivedPointerTracker.getReceivedPointerDownX(0), this.mReceivedPointerTracker.getReceivedPointerDownY(0), this.mReceivedPointerTracker.getReceivedPointerDownX(1), this.mReceivedPointerTracker.getReceivedPointerDownY(1), motionEvent.getX(0), motionEvent.getY(0), motionEvent.getX(1), motionEvent.getY(1), MAX_DRAGGING_ANGLE_COS);
    }

    private void computeDraggingPointerIdIfNeeded(MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() != 2) {
            this.mDraggingPointerId = -1;
            return;
        }
        int i = this.mDraggingPointerId;
        if (i == -1 || motionEvent.findPointerIndex(motionEvent.findPointerIndex(i)) < 0) {
            float x = motionEvent.getX(0);
            float y = motionEvent.getY(0);
            int pointerId = motionEvent.getPointerId(0);
            float x2 = motionEvent.getX(1);
            float y2 = motionEvent.getY(1);
            int pointerId2 = motionEvent.getPointerId(1);
            if (getDistanceToClosestEdge(x, y) >= getDistanceToClosestEdge(x2, y2)) {
                pointerId = pointerId2;
            }
            this.mDraggingPointerId = pointerId;
        }
    }

    private float getDistanceToClosestEdge(float f, float f2) {
        long j = this.mContext.getResources().getDisplayMetrics().widthPixels;
        long j2 = this.mContext.getResources().getDisplayMetrics().heightPixels;
        float f3 = ((float) j) - f;
        if (f >= f3) {
            f = f3;
        }
        if (f > f2) {
            f = f2;
        }
        float f4 = ((float) j2) - f2;
        return f > f4 ? f4 : f;
    }

    private MotionEvent computeDownEventForDrag(MotionEvent motionEvent) {
        int i;
        if (this.mState.isTouchExploring() || (i = this.mDraggingPointerId) == -1 || motionEvent == null) {
            return null;
        }
        float receivedPointerDownX = this.mReceivedPointerTracker.getReceivedPointerDownX(i);
        float receivedPointerDownY = this.mReceivedPointerTracker.getReceivedPointerDownY(this.mDraggingPointerId);
        long receivedPointerDownTime = this.mReceivedPointerTracker.getReceivedPointerDownTime(this.mDraggingPointerId);
        MotionEvent.PointerCoords[] pointerCoordsArr = {new MotionEvent.PointerCoords()};
        MotionEvent.PointerCoords pointerCoords = pointerCoordsArr[0];
        pointerCoords.x = receivedPointerDownX;
        pointerCoords.y = receivedPointerDownY;
        MotionEvent.PointerProperties[] pointerPropertiesArr = {new MotionEvent.PointerProperties()};
        MotionEvent.PointerProperties pointerProperties = pointerPropertiesArr[0];
        pointerProperties.id = this.mDraggingPointerId;
        pointerProperties.toolType = 1;
        MotionEvent obtain = MotionEvent.obtain(receivedPointerDownTime, receivedPointerDownTime, 0, 1, pointerPropertiesArr, pointerCoordsArr, motionEvent.getMetaState(), motionEvent.getButtonState(), motionEvent.getXPrecision(), motionEvent.getYPrecision(), motionEvent.getDeviceId(), motionEvent.getEdgeFlags(), motionEvent.getSource(), motionEvent.getDisplayId(), motionEvent.getFlags());
        motionEvent.setDownTime(receivedPointerDownTime);
        return obtain;
    }

    private boolean allPointersDownOnBottomEdge(MotionEvent motionEvent) {
        long j = this.mContext.getResources().getDisplayMetrics().heightPixels;
        for (int i = 0; i < motionEvent.getPointerCount(); i++) {
            float receivedPointerDownY = this.mReceivedPointerTracker.getReceivedPointerDownY(motionEvent.getPointerId(i));
            if (receivedPointerDownY < ((float) j) - this.mEdgeSwipeHeightPixels) {
                if (DEBUG) {
                    Slog.d(LOG_TAG, "The pointer is not on the bottom edge" + receivedPointerDownY);
                }
                return false;
            }
        }
        return true;
    }

    public TouchState getState() {
        return this.mState;
    }

    @Override // com.android.server.accessibility.BaseEventStreamTransformation, com.android.server.accessibility.EventStreamTransformation
    public void setNext(EventStreamTransformation eventStreamTransformation) {
        this.mDispatcher.setReceiver(eventStreamTransformation);
        super.setNext(eventStreamTransformation);
    }

    public void setServiceHandlesDoubleTap(boolean z) {
        this.mGestureDetector.setServiceHandlesDoubleTap(z);
    }

    public void setMultiFingerGesturesEnabled(boolean z) {
        this.mGestureDetector.setMultiFingerGesturesEnabled(z);
    }

    public void setTwoFingerPassthroughEnabled(boolean z) {
        this.mGestureDetector.setTwoFingerPassthroughEnabled(z);
    }

    public void setGestureDetectionPassthroughRegion(Region region) {
        this.mGestureDetectionPassthroughRegion = region;
    }

    public void setTouchExplorationPassthroughRegion(Region region) {
        this.mTouchExplorationPassthroughRegion = region;
    }

    public void setSendMotionEventsEnabled(boolean z) {
        this.mGestureDetector.setSendMotionEventsEnabled(z);
    }

    public boolean isSendMotionEventsEnabled() {
        return this.mGestureDetector.isSendMotionEventsEnabled();
    }

    public void setServiceDetectsGestures(boolean z) {
        this.mState.setServiceDetectsGestures(z);
    }

    private boolean shouldPerformGestureDetection(MotionEvent motionEvent) {
        if (this.mState.isServiceDetectingGestures() || this.mState.isDelegating() || this.mState.isDragging()) {
            return false;
        }
        if (motionEvent.getActionMasked() != 0) {
            return true;
        }
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        return (this.mTouchExplorationPassthroughRegion.contains(x, y) || this.mGestureDetectionPassthroughRegion.contains(x, y)) ? false : true;
    }

    public void requestTouchExploration() {
        MotionEvent lastReceivedEvent;
        if (DEBUG) {
            Slog.d(LOG_TAG, "Starting touch explorer from service.");
        }
        if (this.mState.isServiceDetectingGestures() && this.mState.isTouchInteracting()) {
            this.mHandler.removeCallbacks(this.mSendHoverEnterAndMoveDelayed);
            int primaryPointerId = this.mReceivedPointerTracker.getPrimaryPointerId();
            if (primaryPointerId == -1 && (lastReceivedEvent = this.mState.getLastReceivedEvent()) != null) {
                primaryPointerId = lastReceivedEvent.getPointerId(0);
            }
            if (primaryPointerId == -1) {
                Slog.e(LOG_TAG, "Unable to find a valid pointer for touch exploration.");
                return;
            }
            sendHoverExitAndTouchExplorationGestureEndIfNeeded(primaryPointerId);
            int lastReceivedPolicyFlags = this.mState.getLastReceivedPolicyFlags();
            this.mSendHoverEnterAndMoveDelayed.setPointerIdBits(1 << primaryPointerId);
            this.mSendHoverEnterAndMoveDelayed.setPolicyFlags(lastReceivedPolicyFlags);
            this.mSendHoverEnterAndMoveDelayed.run();
            this.mSendHoverEnterAndMoveDelayed.clear();
            if (this.mReceivedPointerTracker.getReceivedPointerDownCount() == 0) {
                sendHoverExitAndTouchExplorationGestureEndIfNeeded(lastReceivedPolicyFlags);
            }
        }
    }

    public void requestDragging(int i) {
        if (this.mState.isServiceDetectingGestures()) {
            if (i < 0 || i > 32 || !this.mReceivedPointerTracker.isReceivedPointerDown(i)) {
                Slog.e(LOG_TAG, "Trying to drag with invalid pointer: " + i);
                return;
            }
            if (this.mState.isTouchExploring()) {
                if (this.mSendHoverExitDelayed.isPending()) {
                    this.mSendHoverExitDelayed.forceSendAndRemove();
                }
                if (this.mSendTouchExplorationEndDelayed.isPending()) {
                    this.mSendTouchExplorationEndDelayed.forceSendAndRemove();
                }
            }
            if (!this.mState.isTouchInteracting()) {
                Slog.e(LOG_TAG, "Error: Trying to drag from " + TouchState.getStateSymbolicName(this.mState.getState()));
                return;
            }
            this.mDraggingPointerId = i;
            if (DEBUG) {
                Slog.d(LOG_TAG, "Drag requested on pointer " + this.mDraggingPointerId);
            }
            MotionEvent lastReceivedEvent = this.mState.getLastReceivedEvent();
            MotionEvent lastReceivedRawEvent = this.mState.getLastReceivedRawEvent();
            if (lastReceivedEvent == null || lastReceivedRawEvent == null) {
                Slog.e(LOG_TAG, "Unable to start dragging: unable to get last event.");
                return;
            }
            int lastReceivedPolicyFlags = this.mState.getLastReceivedPolicyFlags();
            int i2 = 1 << this.mDraggingPointerId;
            lastReceivedEvent.setEdgeFlags(this.mReceivedPointerTracker.getLastReceivedDownEdgeFlags());
            MotionEvent computeDownEventForDrag = computeDownEventForDrag(lastReceivedEvent);
            this.mState.startDragging();
            if (computeDownEventForDrag != null) {
                this.mDispatcher.sendMotionEvent(computeDownEventForDrag, 0, lastReceivedRawEvent, i2, lastReceivedPolicyFlags);
                this.mDispatcher.sendMotionEvent(lastReceivedEvent, 2, lastReceivedRawEvent, i2, lastReceivedPolicyFlags);
            } else {
                this.mDispatcher.sendMotionEvent(lastReceivedEvent, 0, lastReceivedRawEvent, i2, lastReceivedPolicyFlags);
            }
        }
    }

    public void requestDelegating() {
        if (this.mState.isServiceDetectingGestures()) {
            if (this.mState.isTouchExploring()) {
                if (this.mSendHoverExitDelayed.isPending()) {
                    this.mSendHoverExitDelayed.forceSendAndRemove();
                }
                if (this.mSendTouchExplorationEndDelayed.isPending()) {
                    this.mSendTouchExplorationEndDelayed.forceSendAndRemove();
                }
            }
            if (!this.mState.isTouchInteracting() && !this.mState.isDragging()) {
                Slog.e(LOG_TAG, "Error: Trying to delegate from " + TouchState.getStateSymbolicName(this.mState.getState()));
                return;
            }
            MotionEvent lastReceivedEvent = this.mState.getLastReceivedEvent();
            MotionEvent lastReceivedRawEvent = this.mState.getLastReceivedRawEvent();
            if (lastReceivedEvent == null || lastReceivedRawEvent == null) {
                Slog.d(LOG_TAG, "Unable to start delegating: unable to get last received event.");
                return;
            }
            int lastReceivedPolicyFlags = this.mState.getLastReceivedPolicyFlags();
            if (this.mState.isDragging()) {
                this.mDispatcher.sendMotionEvent(lastReceivedEvent, 1, lastReceivedRawEvent, -1, lastReceivedPolicyFlags);
            }
            this.mState.startDelegating();
            this.mDispatcher.sendDownForAllNotInjectedPointers(lastReceivedEvent, lastReceivedPolicyFlags);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class ExitGestureDetectionModeDelayed implements Runnable {
        private ExitGestureDetectionModeDelayed() {
        }

        public void post() {
            TouchExplorer.this.mHandler.postDelayed(this, 2000L);
        }

        public void cancel() {
            TouchExplorer.this.mHandler.removeCallbacks(this);
        }

        @Override // java.lang.Runnable
        public void run() {
            TouchExplorer.this.mDispatcher.sendAccessibilityEvent(524288);
            TouchExplorer.this.clear();
        }
    }

    private static void checkForMalformedEvent(MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() < 0) {
            throw new IllegalArgumentException("Invalid pointer count: " + motionEvent.getPointerCount());
        }
        for (int i = 0; i < motionEvent.getPointerCount(); i++) {
            try {
                motionEvent.getPointerId(i);
                float x = motionEvent.getX(i);
                float y = motionEvent.getY(i);
                if (Float.isNaN(x) || Float.isNaN(y) || x < 0.0f || y < 0.0f) {
                    throw new IllegalArgumentException("Invalid coordinates: (" + x + ", " + y + ")");
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Encountered exception getting details of pointer " + i + " / " + motionEvent.getPointerCount(), e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class SendHoverEnterAndMoveDelayed implements Runnable {
        private int mPointerIdBits;
        private int mPolicyFlags;
        private final String LOG_TAG_SEND_HOVER_DELAYED = "SendHoverEnterAndMoveDelayed";
        private final List<MotionEvent> mEvents = new ArrayList();
        private final List<MotionEvent> mRawEvents = new ArrayList();

        SendHoverEnterAndMoveDelayed() {
        }

        public void post(MotionEvent motionEvent, MotionEvent motionEvent2, int i, int i2) {
            cancel();
            addEvent(motionEvent, motionEvent2);
            this.mPointerIdBits = i;
            this.mPolicyFlags = i2;
            TouchExplorer.this.mHandler.postDelayed(this, TouchExplorer.this.mDetermineUserIntentTimeout);
        }

        public void addEvent(MotionEvent motionEvent, MotionEvent motionEvent2) {
            this.mEvents.add(MotionEvent.obtain(motionEvent));
            this.mRawEvents.add(MotionEvent.obtain(motionEvent2));
        }

        public void cancel() {
            if (isPending()) {
                TouchExplorer.this.mHandler.removeCallbacks(this);
                clear();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isPending() {
            return TouchExplorer.this.mHandler.hasCallbacks(this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clear() {
            this.mPointerIdBits = -1;
            this.mPolicyFlags = 0;
            for (int size = this.mEvents.size() - 1; size >= 0; size--) {
                this.mEvents.remove(size).recycle();
            }
            for (int size2 = this.mRawEvents.size() - 1; size2 >= 0; size2--) {
                this.mRawEvents.remove(size2).recycle();
            }
        }

        public void forceSendAndRemove() {
            if (isPending()) {
                run();
                cancel();
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            if (TouchExplorer.this.mReceivedPointerTracker.getReceivedPointerDownCount() > 1) {
                Slog.e(TouchExplorer.LOG_TAG, "Attempted touch exploration with " + TouchExplorer.this.mReceivedPointerTracker.getReceivedPointerDownCount() + " pointers down.");
                return;
            }
            TouchExplorer.this.mDispatcher.sendAccessibilityEvent(512);
            if (TouchExplorer.this.isSendMotionEventsEnabled()) {
                TouchExplorer.this.dispatchGesture(new AccessibilityGestureEvent(-2, TouchExplorer.this.mState.getLastReceivedEvent().getDisplayId(), TouchExplorer.this.mGestureDetector.getMotionEvents()));
            }
            if (!this.mEvents.isEmpty() && !this.mRawEvents.isEmpty()) {
                TouchExplorer.this.mDispatcher.sendMotionEvent(this.mEvents.get(0), 9, this.mRawEvents.get(0), this.mPointerIdBits, this.mPolicyFlags);
                if (TouchExplorer.DEBUG) {
                    Slog.d("SendHoverEnterAndMoveDelayed", "Injecting motion event: ACTION_HOVER_ENTER");
                }
                int size = this.mEvents.size();
                for (int i = 1; i < size; i++) {
                    TouchExplorer.this.mDispatcher.sendMotionEvent(this.mEvents.get(i), 7, this.mRawEvents.get(i), this.mPointerIdBits, this.mPolicyFlags);
                    if (TouchExplorer.DEBUG) {
                        Slog.d("SendHoverEnterAndMoveDelayed", "Injecting motion event: ACTION_HOVER_MOVE");
                    }
                }
            }
            clear();
        }

        public void setPointerIdBits(int i) {
            this.mPointerIdBits = i;
        }

        public void setPolicyFlags(int i) {
            this.mPolicyFlags = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class SendHoverExitDelayed implements Runnable {
        private final String LOG_TAG_SEND_HOVER_DELAYED = "SendHoverExitDelayed";
        private int mPointerIdBits;
        private int mPolicyFlags;
        private MotionEvent mPrototype;
        private MotionEvent mRawEvent;

        SendHoverExitDelayed() {
        }

        public void post(MotionEvent motionEvent, MotionEvent motionEvent2, int i, int i2) {
            cancel();
            this.mPrototype = MotionEvent.obtain(motionEvent);
            this.mRawEvent = MotionEvent.obtain(motionEvent2);
            this.mPointerIdBits = i;
            this.mPolicyFlags = i2;
            TouchExplorer.this.mHandler.postDelayed(this, TouchExplorer.this.mDetermineUserIntentTimeout);
        }

        public void cancel() {
            if (isPending()) {
                TouchExplorer.this.mHandler.removeCallbacks(this);
                clear();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isPending() {
            return TouchExplorer.this.mHandler.hasCallbacks(this);
        }

        private void clear() {
            MotionEvent motionEvent = this.mPrototype;
            if (motionEvent != null) {
                motionEvent.recycle();
            }
            MotionEvent motionEvent2 = this.mRawEvent;
            if (motionEvent2 != null) {
                motionEvent2.recycle();
            }
            this.mPrototype = null;
            this.mRawEvent = null;
            this.mPointerIdBits = -1;
            this.mPolicyFlags = 0;
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
                Slog.d("SendHoverExitDelayed", "Injecting motion event: ACTION_HOVER_EXIT");
            }
            TouchExplorer.this.mDispatcher.sendMotionEvent(this.mPrototype, 10, this.mRawEvent, this.mPointerIdBits, this.mPolicyFlags);
            if (!TouchExplorer.this.mSendTouchExplorationEndDelayed.isPending()) {
                TouchExplorer.this.mSendTouchExplorationEndDelayed.cancel();
                TouchExplorer.this.mSendTouchExplorationEndDelayed.post();
            }
            if (TouchExplorer.this.mSendTouchInteractionEndDelayed.isPending()) {
                TouchExplorer.this.mSendTouchInteractionEndDelayed.cancel();
                TouchExplorer.this.mSendTouchInteractionEndDelayed.post();
            }
            clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class SendAccessibilityEventDelayed implements Runnable {
        private final int mDelay;
        private final int mEventType;

        public SendAccessibilityEventDelayed(int i, int i2) {
            this.mEventType = i;
            this.mDelay = i2;
        }

        public void cancel() {
            TouchExplorer.this.mHandler.removeCallbacks(this);
        }

        public void post() {
            TouchExplorer.this.mHandler.postDelayed(this, this.mDelay);
        }

        public boolean isPending() {
            return TouchExplorer.this.mHandler.hasCallbacks(this);
        }

        public void forceSendAndRemove() {
            if (isPending()) {
                run();
                cancel();
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            TouchExplorer.this.mDispatcher.sendAccessibilityEvent(this.mEventType);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchGesture(AccessibilityGestureEvent accessibilityGestureEvent) {
        if (DEBUG) {
            Slog.d(LOG_TAG, "Dispatching gesture event:" + accessibilityGestureEvent.toString());
        }
        this.mAms.onGesture(accessibilityGestureEvent);
    }

    public String toString() {
        return "TouchExplorer { mTouchState: " + this.mState + ", mDetermineUserIntentTimeout: " + this.mDetermineUserIntentTimeout + ", mDoubleTapSlop: " + this.mDoubleTapSlop + ", mDraggingPointerId: " + this.mDraggingPointerId + " }";
    }
}
