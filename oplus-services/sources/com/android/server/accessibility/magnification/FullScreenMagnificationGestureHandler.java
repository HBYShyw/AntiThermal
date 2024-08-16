package com.android.server.accessibility.magnification;

import android.R;
import android.accessibilityservice.MagnificationConfig;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PointF;
import android.graphics.Region;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.util.MathUtils;
import android.util.Slog;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewConfiguration;
import com.android.internal.accessibility.util.AccessibilityStatsLogUtils;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.accessibility.AccessibilityTraceManager;
import com.android.server.accessibility.gestures.GestureUtils;
import com.android.server.accessibility.magnification.FullScreenMagnificationController;
import com.android.server.accessibility.magnification.MagnificationGestureHandler;
import java.util.Arrays;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FullScreenMagnificationGestureHandler extends MagnificationGestureHandler {
    private static final boolean DEBUG_DETECTING;
    private static final boolean DEBUG_PANNING_SCALING;
    private static final boolean DEBUG_STATE_TRANSITIONS;
    private static final float MAX_SCALE = 8.0f;
    private static final float MIN_SCALE = 1.0f;

    @VisibleForTesting
    State mCurrentState;

    @VisibleForTesting
    final DelegatingState mDelegatingState;

    @VisibleForTesting
    final DetectingState mDetectingState;

    @VisibleForTesting
    final FullScreenMagnificationController mFullScreenMagnificationController;
    private final FullScreenMagnificationController.MagnificationInfoChangedCallback mMagnificationInfoChangedCallback;

    @VisibleForTesting
    final PanningScalingState mPanningScalingState;

    @VisibleForTesting
    State mPreviousState;
    private final WindowMagnificationPromptController mPromptController;
    private final ScreenStateReceiver mScreenStateReceiver;
    private MotionEvent.PointerCoords[] mTempPointerCoords;
    private MotionEvent.PointerProperties[] mTempPointerProperties;

    @VisibleForTesting
    final ViewportDraggingState mViewportDraggingState;

    @Override // com.android.server.accessibility.magnification.MagnificationGestureHandler
    public int getMode() {
        return 1;
    }

    static {
        boolean z = MagnificationGestureHandler.DEBUG_ALL;
        DEBUG_STATE_TRANSITIONS = z | false;
        DEBUG_DETECTING = z | false;
        DEBUG_PANNING_SCALING = z | false;
    }

    public FullScreenMagnificationGestureHandler(Context context, FullScreenMagnificationController fullScreenMagnificationController, AccessibilityTraceManager accessibilityTraceManager, MagnificationGestureHandler.Callback callback, boolean z, boolean z2, WindowMagnificationPromptController windowMagnificationPromptController, int i) {
        super(i, z, z2, accessibilityTraceManager, callback);
        if (MagnificationGestureHandler.DEBUG_ALL) {
            Log.i(this.mLogTag, "FullScreenMagnificationGestureHandler(detectTripleTap = " + z + ", detectShortcutTrigger = " + z2 + ")");
        }
        this.mFullScreenMagnificationController = fullScreenMagnificationController;
        FullScreenMagnificationController.MagnificationInfoChangedCallback magnificationInfoChangedCallback = new FullScreenMagnificationController.MagnificationInfoChangedCallback() { // from class: com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.1
            @Override // com.android.server.accessibility.magnification.FullScreenMagnificationController.MagnificationInfoChangedCallback
            public void onFullScreenMagnificationChanged(int i2, Region region, MagnificationConfig magnificationConfig) {
            }

            @Override // com.android.server.accessibility.magnification.FullScreenMagnificationController.MagnificationInfoChangedCallback
            public void onImeWindowVisibilityChanged(int i2, boolean z3) {
            }

            @Override // com.android.server.accessibility.magnification.FullScreenMagnificationController.MagnificationInfoChangedCallback
            public void onRequestMagnificationSpec(int i2, int i3) {
            }

            @Override // com.android.server.accessibility.magnification.FullScreenMagnificationController.MagnificationInfoChangedCallback
            public void onFullScreenMagnificationActivationState(int i2, boolean z3) {
                FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler = FullScreenMagnificationGestureHandler.this;
                if (i2 == fullScreenMagnificationGestureHandler.mDisplayId && !z3) {
                    fullScreenMagnificationGestureHandler.clearAndTransitionToStateDetecting();
                }
            }
        };
        this.mMagnificationInfoChangedCallback = magnificationInfoChangedCallback;
        fullScreenMagnificationController.addInfoChangedCallback(magnificationInfoChangedCallback);
        this.mPromptController = windowMagnificationPromptController;
        this.mDelegatingState = new DelegatingState();
        DetectingState detectingState = new DetectingState(context);
        this.mDetectingState = detectingState;
        this.mViewportDraggingState = new ViewportDraggingState();
        this.mPanningScalingState = new PanningScalingState(context);
        if (this.mDetectShortcutTrigger) {
            ScreenStateReceiver screenStateReceiver = new ScreenStateReceiver(context, this);
            this.mScreenStateReceiver = screenStateReceiver;
            screenStateReceiver.register();
        } else {
            this.mScreenStateReceiver = null;
        }
        transitionTo(detectingState);
    }

    @Override // com.android.server.accessibility.magnification.MagnificationGestureHandler
    void onMotionEventInternal(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        handleEventWith(this.mCurrentState, motionEvent, motionEvent2, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleEventWith(State state, MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
        this.mPanningScalingState.mScrollGestureDetector.onTouchEvent(motionEvent);
        this.mPanningScalingState.mScaleGestureDetector.onTouchEvent(motionEvent);
        try {
            state.onMotionEvent(motionEvent, motionEvent2, i);
        } catch (GestureException e) {
            Slog.e(this.mLogTag, "Error processing motion event", e);
            clearAndTransitionToStateDetecting();
        }
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void clearEvents(int i) {
        if (i == 4098) {
            clearAndTransitionToStateDetecting();
        }
        super.clearEvents(i);
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void onDestroy() {
        if (DEBUG_STATE_TRANSITIONS) {
            Slog.i(this.mLogTag, "onDestroy(); delayed = " + MotionEventInfo.toString(this.mDetectingState.mDelayedEventQueue));
        }
        ScreenStateReceiver screenStateReceiver = this.mScreenStateReceiver;
        if (screenStateReceiver != null) {
            screenStateReceiver.unregister();
        }
        this.mPromptController.onDestroy();
        this.mFullScreenMagnificationController.resetIfNeeded(this.mDisplayId, 0);
        this.mFullScreenMagnificationController.removeInfoChangedCallback(this.mMagnificationInfoChangedCallback);
        clearAndTransitionToStateDetecting();
    }

    @Override // com.android.server.accessibility.magnification.MagnificationGestureHandler
    public void handleShortcutTriggered() {
        if (this.mFullScreenMagnificationController.isActivated(this.mDisplayId)) {
            zoomOff();
            clearAndTransitionToStateDetecting();
        } else {
            this.mDetectingState.toggleShortcutTriggered();
        }
        if (this.mDetectingState.isShortcutTriggered()) {
            this.mPromptController.showNotificationIfNeeded();
            zoomToScale(1.0f, Float.NaN, Float.NaN);
        }
    }

    void clearAndTransitionToStateDetecting() {
        DetectingState detectingState = this.mDetectingState;
        this.mCurrentState = detectingState;
        detectingState.clear();
        this.mViewportDraggingState.clear();
        this.mPanningScalingState.clear();
    }

    private MotionEvent.PointerCoords[] getTempPointerCoordsWithMinSize(int i) {
        MotionEvent.PointerCoords[] pointerCoordsArr = this.mTempPointerCoords;
        int length = pointerCoordsArr != null ? pointerCoordsArr.length : 0;
        if (length < i) {
            MotionEvent.PointerCoords[] pointerCoordsArr2 = new MotionEvent.PointerCoords[i];
            this.mTempPointerCoords = pointerCoordsArr2;
            if (pointerCoordsArr != null) {
                System.arraycopy(pointerCoordsArr, 0, pointerCoordsArr2, 0, length);
            }
        }
        while (length < i) {
            this.mTempPointerCoords[length] = new MotionEvent.PointerCoords();
            length++;
        }
        return this.mTempPointerCoords;
    }

    private MotionEvent.PointerProperties[] getTempPointerPropertiesWithMinSize(int i) {
        MotionEvent.PointerProperties[] pointerPropertiesArr = this.mTempPointerProperties;
        int length = pointerPropertiesArr != null ? pointerPropertiesArr.length : 0;
        if (length < i) {
            MotionEvent.PointerProperties[] pointerPropertiesArr2 = new MotionEvent.PointerProperties[i];
            this.mTempPointerProperties = pointerPropertiesArr2;
            if (pointerPropertiesArr != null) {
                System.arraycopy(pointerPropertiesArr, 0, pointerPropertiesArr2, 0, length);
            }
        }
        while (length < i) {
            this.mTempPointerProperties[length] = new MotionEvent.PointerProperties();
            length++;
        }
        return this.mTempPointerProperties;
    }

    @VisibleForTesting
    void transitionTo(State state) {
        if (DEBUG_STATE_TRANSITIONS) {
            Slog.i(this.mLogTag, (State.nameOf(this.mCurrentState) + " -> " + State.nameOf(state) + " at " + Arrays.asList((StackTraceElement[]) Arrays.copyOfRange(new RuntimeException().getStackTrace(), 1, 5))).replace(getClass().getName(), ""));
        }
        this.mPreviousState = this.mCurrentState;
        PanningScalingState panningScalingState = this.mPanningScalingState;
        if (state == panningScalingState) {
            panningScalingState.prepareForState();
        }
        this.mCurrentState = state;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface State {
        default void clear() {
        }

        void onMotionEvent(MotionEvent motionEvent, MotionEvent motionEvent2, int i) throws GestureException;

        default String name() {
            return getClass().getSimpleName();
        }

        static String nameOf(State state) {
            return state != null ? state.name() : "null";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class PanningScalingState extends GestureDetector.SimpleOnGestureListener implements ScaleGestureDetector.OnScaleGestureListener, State {

        @VisibleForTesting
        static final float CHECK_DETECTING_PASS_PERSISTED_SCALE_THRESHOLD = 0.2f;

        @VisibleForTesting
        static final float PASSING_PERSISTED_SCALE_THRESHOLD = 0.01f;
        private final Context mContext;

        @VisibleForTesting
        boolean mDetectingPassPersistedScale;
        float mInitialScaleFactor = -1.0f;
        private final ScaleGestureDetector mScaleGestureDetector;

        @VisibleForTesting
        boolean mScaling;
        final float mScalingThreshold;
        private final GestureDetector mScrollGestureDetector;

        PanningScalingState(Context context) {
            TypedValue typedValue = new TypedValue();
            context.getResources().getValue(R.dimen.conversation_icon_size_badged, typedValue, false);
            this.mContext = context;
            this.mScalingThreshold = typedValue.getFloat();
            ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(context, this, Handler.getMain());
            this.mScaleGestureDetector = scaleGestureDetector;
            scaleGestureDetector.setQuickScaleEnabled(false);
            this.mScrollGestureDetector = new GestureDetector(context, this, Handler.getMain());
        }

        @Override // com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State
        public void onMotionEvent(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 6 && motionEvent.getPointerCount() == 2) {
                FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler = FullScreenMagnificationGestureHandler.this;
                State state = fullScreenMagnificationGestureHandler.mPreviousState;
                ViewportDraggingState viewportDraggingState = fullScreenMagnificationGestureHandler.mViewportDraggingState;
                if (state == viewportDraggingState) {
                    persistScaleAndTransitionTo(viewportDraggingState);
                    return;
                }
            }
            if (actionMasked == 1 || actionMasked == 3) {
                persistScaleAndTransitionTo(FullScreenMagnificationGestureHandler.this.mDetectingState);
            }
        }

        void prepareForState() {
            checkShouldDetectPassPersistedScale();
        }

        private void checkShouldDetectPassPersistedScale() {
            if (this.mDetectingPassPersistedScale) {
                return;
            }
            FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler = FullScreenMagnificationGestureHandler.this;
            float scale = fullScreenMagnificationGestureHandler.mFullScreenMagnificationController.getScale(fullScreenMagnificationGestureHandler.mDisplayId);
            FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler2 = FullScreenMagnificationGestureHandler.this;
            float persistedScale = fullScreenMagnificationGestureHandler2.mFullScreenMagnificationController.getPersistedScale(fullScreenMagnificationGestureHandler2.mDisplayId);
            this.mDetectingPassPersistedScale = Math.abs(scale - persistedScale) / persistedScale >= CHECK_DETECTING_PASS_PERSISTED_SCALE_THRESHOLD;
        }

        public void persistScaleAndTransitionTo(State state) {
            FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler = FullScreenMagnificationGestureHandler.this;
            fullScreenMagnificationGestureHandler.mFullScreenMagnificationController.persistScale(fullScreenMagnificationGestureHandler.mDisplayId);
            clear();
            FullScreenMagnificationGestureHandler.this.transitionTo(state);
        }

        /* JADX WARN: Removed duplicated region for block: B:11:0x003c  */
        @VisibleForTesting
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        void setScaleAndClearIfNeeded(float f, float f2, float f3) {
            float f4;
            if (this.mDetectingPassPersistedScale) {
                FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler = FullScreenMagnificationGestureHandler.this;
                float persistedScale = fullScreenMagnificationGestureHandler.mFullScreenMagnificationController.getPersistedScale(fullScreenMagnificationGestureHandler.mDisplayId);
                if (Math.abs(f - persistedScale) / persistedScale < 0.01f) {
                    Vibrator vibrator = (Vibrator) this.mContext.getSystemService(Vibrator.class);
                    if (vibrator != null) {
                        vibrator.vibrate(VibrationEffect.createPredefined(2));
                    }
                    clear();
                    f4 = persistedScale;
                    if (FullScreenMagnificationGestureHandler.DEBUG_PANNING_SCALING) {
                        Slog.i(FullScreenMagnificationGestureHandler.this.mLogTag, "Scaled content to: " + f4 + "x");
                    }
                    FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler2 = FullScreenMagnificationGestureHandler.this;
                    fullScreenMagnificationGestureHandler2.mFullScreenMagnificationController.setScale(fullScreenMagnificationGestureHandler2.mDisplayId, f4, f2, f3, false, 0);
                    checkShouldDetectPassPersistedScale();
                }
            }
            f4 = f;
            if (FullScreenMagnificationGestureHandler.DEBUG_PANNING_SCALING) {
            }
            FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler22 = FullScreenMagnificationGestureHandler.this;
            fullScreenMagnificationGestureHandler22.mFullScreenMagnificationController.setScale(fullScreenMagnificationGestureHandler22.mDisplayId, f4, f2, f3, false, 0);
            checkShouldDetectPassPersistedScale();
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler = FullScreenMagnificationGestureHandler.this;
            if (fullScreenMagnificationGestureHandler.mCurrentState != fullScreenMagnificationGestureHandler.mPanningScalingState) {
                return true;
            }
            if (FullScreenMagnificationGestureHandler.DEBUG_PANNING_SCALING) {
                Slog.i(FullScreenMagnificationGestureHandler.this.mLogTag, "Panned content by scrollX: " + f + " scrollY: " + f2);
            }
            FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler2 = FullScreenMagnificationGestureHandler.this;
            fullScreenMagnificationGestureHandler2.mFullScreenMagnificationController.offsetMagnifiedRegion(fullScreenMagnificationGestureHandler2.mDisplayId, f, f2, 0);
            return true;
        }

        /* JADX WARN: Code restructure failed: missing block: B:23:0x004d, code lost:
        
            if (r2 < r0) goto L18;
         */
        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            if (!this.mScaling) {
                if (this.mInitialScaleFactor < 0.0f) {
                    this.mInitialScaleFactor = scaleGestureDetector.getScaleFactor();
                    return false;
                }
                boolean z = Math.abs(scaleGestureDetector.getScaleFactor() - this.mInitialScaleFactor) > this.mScalingThreshold;
                this.mScaling = z;
                return z;
            }
            FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler = FullScreenMagnificationGestureHandler.this;
            float scale = fullScreenMagnificationGestureHandler.mFullScreenMagnificationController.getScale(fullScreenMagnificationGestureHandler.mDisplayId);
            float scaleFactor = scaleGestureDetector.getScaleFactor() * scale;
            float f = 8.0f;
            if (scaleFactor <= 8.0f || scaleFactor <= scale) {
                f = 1.0f;
                if (scaleFactor < 1.0f) {
                }
                setScaleAndClearIfNeeded(scaleFactor, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
                return true;
            }
            scaleFactor = f;
            setScaleAndClearIfNeeded(scaleFactor, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
            return true;
        }

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler = FullScreenMagnificationGestureHandler.this;
            return fullScreenMagnificationGestureHandler.mCurrentState == fullScreenMagnificationGestureHandler.mPanningScalingState;
        }

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            clear();
        }

        @Override // com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State
        public void clear() {
            this.mInitialScaleFactor = -1.0f;
            this.mScaling = false;
            this.mDetectingPassPersistedScale = false;
        }

        public String toString() {
            return "PanningScalingState{mInitialScaleFactor=" + this.mInitialScaleFactor + ", mScaling=" + this.mScaling + '}';
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class ViewportDraggingState implements State {
        private boolean mLastMoveOutsideMagnifiedRegion;

        @VisibleForTesting
        float mScaleToRecoverAfterDraggingEnd = Float.NaN;

        ViewportDraggingState() {
        }

        @Override // com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State
        public void onMotionEvent(MotionEvent motionEvent, MotionEvent motionEvent2, int i) throws GestureException {
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked != 0) {
                if (actionMasked != 1) {
                    if (actionMasked == 2) {
                        if (motionEvent.getPointerCount() != 1) {
                            throw new GestureException("Should have one pointer down.");
                        }
                        float x = motionEvent.getX();
                        float y = motionEvent.getY();
                        FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler = FullScreenMagnificationGestureHandler.this;
                        if (fullScreenMagnificationGestureHandler.mFullScreenMagnificationController.magnificationRegionContains(fullScreenMagnificationGestureHandler.mDisplayId, x, y)) {
                            FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler2 = FullScreenMagnificationGestureHandler.this;
                            fullScreenMagnificationGestureHandler2.mFullScreenMagnificationController.setCenter(fullScreenMagnificationGestureHandler2.mDisplayId, x, y, this.mLastMoveOutsideMagnifiedRegion, 0);
                            this.mLastMoveOutsideMagnifiedRegion = false;
                            return;
                        }
                        this.mLastMoveOutsideMagnifiedRegion = true;
                        return;
                    }
                    if (actionMasked != 3) {
                        if (actionMasked == 5) {
                            clearAndTransitToPanningScalingState();
                            return;
                        } else if (actionMasked != 6) {
                            return;
                        }
                    }
                }
                float f = this.mScaleToRecoverAfterDraggingEnd;
                if (f >= 1.0f) {
                    FullScreenMagnificationGestureHandler.this.zoomToScale(f, motionEvent.getX(), motionEvent.getY());
                } else {
                    FullScreenMagnificationGestureHandler.this.zoomOff();
                }
                clear();
                this.mScaleToRecoverAfterDraggingEnd = Float.NaN;
                FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler3 = FullScreenMagnificationGestureHandler.this;
                fullScreenMagnificationGestureHandler3.transitionTo(fullScreenMagnificationGestureHandler3.mDetectingState);
                return;
            }
            throw new GestureException("Unexpected event type: " + MotionEvent.actionToString(actionMasked));
        }

        private boolean isAlwaysOnMagnificationEnabled() {
            return FullScreenMagnificationGestureHandler.this.mFullScreenMagnificationController.isAlwaysOnMagnificationEnabled();
        }

        public void prepareForZoomInTemporary(boolean z) {
            boolean z2;
            float f;
            FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler = FullScreenMagnificationGestureHandler.this;
            if (fullScreenMagnificationGestureHandler.mFullScreenMagnificationController.isActivated(fullScreenMagnificationGestureHandler.mDisplayId)) {
                z2 = z ? isAlwaysOnMagnificationEnabled() : true;
            } else {
                z2 = false;
            }
            if (z2) {
                FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler2 = FullScreenMagnificationGestureHandler.this;
                f = fullScreenMagnificationGestureHandler2.mFullScreenMagnificationController.getScale(fullScreenMagnificationGestureHandler2.mDisplayId);
            } else {
                f = Float.NaN;
            }
            this.mScaleToRecoverAfterDraggingEnd = f;
        }

        private void clearAndTransitToPanningScalingState() {
            float f = this.mScaleToRecoverAfterDraggingEnd;
            clear();
            this.mScaleToRecoverAfterDraggingEnd = f;
            FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler = FullScreenMagnificationGestureHandler.this;
            fullScreenMagnificationGestureHandler.transitionTo(fullScreenMagnificationGestureHandler.mPanningScalingState);
        }

        @Override // com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State
        public void clear() {
            this.mLastMoveOutsideMagnifiedRegion = false;
            this.mScaleToRecoverAfterDraggingEnd = Float.NaN;
        }

        public String toString() {
            return "ViewportDraggingState{mScaleToRecoverAfterDraggingEnd=" + this.mScaleToRecoverAfterDraggingEnd + ", mLastMoveOutsideMagnifiedRegion=" + this.mLastMoveOutsideMagnifiedRegion + '}';
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class DelegatingState implements State {
        public long mLastDelegatedDownEventTime;

        DelegatingState() {
        }

        @Override // com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State
        public void onMotionEvent(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 0) {
                FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler = FullScreenMagnificationGestureHandler.this;
                fullScreenMagnificationGestureHandler.transitionTo(fullScreenMagnificationGestureHandler.mDelegatingState);
                this.mLastDelegatedDownEventTime = motionEvent.getDownTime();
            } else if (actionMasked == 1 || actionMasked == 3) {
                FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler2 = FullScreenMagnificationGestureHandler.this;
                fullScreenMagnificationGestureHandler2.transitionTo(fullScreenMagnificationGestureHandler2.mDetectingState);
            }
            if (FullScreenMagnificationGestureHandler.this.getNext() != null) {
                motionEvent.setDownTime(this.mLastDelegatedDownEventTime);
                FullScreenMagnificationGestureHandler.this.dispatchTransformedEvent(motionEvent, motionEvent2, i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class DetectingState implements State, Handler.Callback {
        private static final int MESSAGE_ON_TRIPLE_TAP_AND_HOLD = 1;
        private static final int MESSAGE_TRANSITION_TO_DELEGATING_STATE = 2;
        private static final int MESSAGE_TRANSITION_TO_PANNINGSCALING_STATE = 3;
        private MotionEventInfo mDelayedEventQueue;
        private long mLastDetectingDownEventTime;
        MotionEvent mLastDown;
        private MotionEvent mLastUp;
        final int mMultiTapMaxDelay;
        final int mMultiTapMaxDistance;
        private MotionEvent mPreLastDown;
        private MotionEvent mPreLastUp;

        @VisibleForTesting
        boolean mShortcutTriggered;
        final int mSwipeMinDistance;
        private PointF mSecondPointerDownLocation = new PointF(Float.NaN, Float.NaN);

        @VisibleForTesting
        Handler mHandler = new Handler(Looper.getMainLooper(), this);
        final int mLongTapMinDelay = ViewConfiguration.getLongPressTimeout();

        DetectingState(Context context) {
            this.mMultiTapMaxDelay = ViewConfiguration.getDoubleTapTimeout() + context.getResources().getInteger(R.integer.leanback_setup_translation_content_resting_point_v4);
            this.mSwipeMinDistance = ViewConfiguration.get(context).getScaledTouchSlop();
            this.mMultiTapMaxDistance = ViewConfiguration.get(context).getScaledDoubleTapSlop();
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                MotionEvent motionEvent = (MotionEvent) message.obj;
                transitionToViewportDraggingStateAndClear(motionEvent);
                motionEvent.recycle();
            } else if (i == 2) {
                transitionToDelegatingStateAndClear();
            } else if (i == 3) {
                transitToPanningScalingStateAndClear();
            } else {
                throw new IllegalArgumentException("Unknown message type: " + i);
            }
            return true;
        }

        @Override // com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State
        public void onMotionEvent(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
            cacheDelayedMotionEvent(motionEvent, motionEvent2, i);
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 0) {
                this.mLastDetectingDownEventTime = motionEvent.getDownTime();
                this.mHandler.removeMessages(2);
                FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler = FullScreenMagnificationGestureHandler.this;
                if (!fullScreenMagnificationGestureHandler.mFullScreenMagnificationController.magnificationRegionContains(fullScreenMagnificationGestureHandler.mDisplayId, motionEvent.getX(), motionEvent.getY())) {
                    transitionToDelegatingStateAndClear();
                    return;
                }
                if (isMultiTapTriggered(2)) {
                    afterLongTapTimeoutTransitionToDraggingState(motionEvent);
                    return;
                }
                if (isTapOutOfDistanceSlop()) {
                    transitionToDelegatingStateAndClear();
                    return;
                } else if (FullScreenMagnificationGestureHandler.this.mDetectTripleTap || isActivated()) {
                    afterMultiTapTimeoutTransitionToDelegatingState();
                    return;
                } else {
                    transitionToDelegatingStateAndClear();
                    return;
                }
            }
            if (actionMasked == 1) {
                this.mHandler.removeMessages(1);
                FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler2 = FullScreenMagnificationGestureHandler.this;
                if (!fullScreenMagnificationGestureHandler2.mFullScreenMagnificationController.magnificationRegionContains(fullScreenMagnificationGestureHandler2.mDisplayId, motionEvent.getX(), motionEvent.getY())) {
                    transitionToDelegatingStateAndClear();
                    return;
                }
                if (isMultiTapTriggered(3)) {
                    onTripleTap(motionEvent);
                    return;
                } else {
                    if (isFingerDown()) {
                        if (timeBetween(this.mLastDown, this.mLastUp) >= this.mLongTapMinDelay || GestureUtils.distance(this.mLastDown, this.mLastUp) >= this.mSwipeMinDistance) {
                            transitionToDelegatingStateAndClear();
                            return;
                        }
                        return;
                    }
                    return;
                }
            }
            if (actionMasked != 2) {
                if (actionMasked != 5) {
                    if (actionMasked != 6) {
                        return;
                    }
                    transitionToDelegatingStateAndClear();
                    return;
                } else if (isActivated() && motionEvent.getPointerCount() == 2) {
                    storeSecondPointerDownLocation(motionEvent);
                    this.mHandler.sendEmptyMessageDelayed(3, ViewConfiguration.getTapTimeout());
                    return;
                } else {
                    transitionToDelegatingStateAndClear();
                    return;
                }
            }
            if (isFingerDown() && GestureUtils.distance(this.mLastDown, motionEvent) > this.mSwipeMinDistance) {
                if (isMultiTapTriggered(2) && motionEvent.getPointerCount() == 1) {
                    transitionToViewportDraggingStateAndClear(motionEvent);
                    return;
                } else if (isActivated() && motionEvent.getPointerCount() == 2) {
                    transitToPanningScalingStateAndClear();
                    return;
                } else {
                    transitionToDelegatingStateAndClear();
                    return;
                }
            }
            if (isActivated() && secondPointerDownValid() && GestureUtils.distanceClosestPointerToPoint(this.mSecondPointerDownLocation, motionEvent) > this.mSwipeMinDistance) {
                transitToPanningScalingStateAndClear();
            }
        }

        private void storeSecondPointerDownLocation(MotionEvent motionEvent) {
            int actionIndex = motionEvent.getActionIndex();
            this.mSecondPointerDownLocation.set(motionEvent.getX(actionIndex), motionEvent.getY(actionIndex));
        }

        private boolean secondPointerDownValid() {
            return (Float.isNaN(this.mSecondPointerDownLocation.x) && Float.isNaN(this.mSecondPointerDownLocation.y)) ? false : true;
        }

        private void transitToPanningScalingStateAndClear() {
            FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler = FullScreenMagnificationGestureHandler.this;
            fullScreenMagnificationGestureHandler.transitionTo(fullScreenMagnificationGestureHandler.mPanningScalingState);
            clear();
        }

        public boolean isMultiTapTriggered(int i) {
            if (this.mShortcutTriggered) {
                return tapCount() + 2 >= i;
            }
            boolean z = FullScreenMagnificationGestureHandler.this.mDetectTripleTap && tapCount() >= i && isMultiTap(this.mPreLastDown, this.mLastDown) && isMultiTap(this.mPreLastUp, this.mLastUp);
            if (z && i > 2) {
                AccessibilityStatsLogUtils.logMagnificationTripleTap(isActivated());
            }
            return z;
        }

        private boolean isMultiTap(MotionEvent motionEvent, MotionEvent motionEvent2) {
            return GestureUtils.isMultiTap(motionEvent, motionEvent2, this.mMultiTapMaxDelay, this.mMultiTapMaxDistance);
        }

        public boolean isFingerDown() {
            return this.mLastDown != null;
        }

        private long timeBetween(MotionEvent motionEvent, MotionEvent motionEvent2) {
            if (motionEvent == null && motionEvent2 == null) {
                return 0L;
            }
            return Math.abs(timeOf(motionEvent) - timeOf(motionEvent2));
        }

        private long timeOf(MotionEvent motionEvent) {
            if (motionEvent != null) {
                return motionEvent.getEventTime();
            }
            return Long.MIN_VALUE;
        }

        public int tapCount() {
            return MotionEventInfo.countOf(this.mDelayedEventQueue, 1);
        }

        public void afterMultiTapTimeoutTransitionToDelegatingState() {
            this.mHandler.sendEmptyMessageDelayed(2, this.mMultiTapMaxDelay);
        }

        public void afterLongTapTimeoutTransitionToDraggingState(MotionEvent motionEvent) {
            Handler handler = this.mHandler;
            handler.sendMessageDelayed(handler.obtainMessage(1, MotionEvent.obtain(motionEvent)), ViewConfiguration.getLongPressTimeout());
        }

        @Override // com.android.server.accessibility.magnification.FullScreenMagnificationGestureHandler.State
        public void clear() {
            setShortcutTriggered(false);
            removePendingDelayedMessages();
            clearDelayedMotionEvents();
            this.mSecondPointerDownLocation.set(Float.NaN, Float.NaN);
        }

        private void removePendingDelayedMessages() {
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
        }

        private void cacheDelayedMotionEvent(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
            if (motionEvent.getActionMasked() == 0) {
                this.mPreLastDown = this.mLastDown;
                this.mLastDown = MotionEvent.obtain(motionEvent);
            } else if (motionEvent.getActionMasked() == 1) {
                this.mPreLastUp = this.mLastUp;
                this.mLastUp = MotionEvent.obtain(motionEvent);
            }
            MotionEventInfo obtain = MotionEventInfo.obtain(motionEvent, motionEvent2, i);
            MotionEventInfo motionEventInfo = this.mDelayedEventQueue;
            if (motionEventInfo == null) {
                this.mDelayedEventQueue = obtain;
                return;
            }
            while (motionEventInfo.mNext != null) {
                motionEventInfo = motionEventInfo.mNext;
            }
            motionEventInfo.mNext = obtain;
        }

        private void sendDelayedMotionEvents() {
            if (this.mDelayedEventQueue == null) {
                return;
            }
            long min = Math.min(SystemClock.uptimeMillis() - this.mLastDetectingDownEventTime, this.mMultiTapMaxDelay);
            do {
                MotionEventInfo motionEventInfo = this.mDelayedEventQueue;
                this.mDelayedEventQueue = motionEventInfo.mNext;
                MotionEvent motionEvent = motionEventInfo.event;
                motionEvent.setDownTime(motionEvent.getDownTime() + min);
                FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler = FullScreenMagnificationGestureHandler.this;
                fullScreenMagnificationGestureHandler.handleEventWith(fullScreenMagnificationGestureHandler.mDelegatingState, motionEventInfo.event, motionEventInfo.rawEvent, motionEventInfo.policyFlags);
                motionEventInfo.recycle();
            } while (this.mDelayedEventQueue != null);
        }

        private void clearDelayedMotionEvents() {
            while (true) {
                MotionEventInfo motionEventInfo = this.mDelayedEventQueue;
                if (motionEventInfo != null) {
                    this.mDelayedEventQueue = motionEventInfo.mNext;
                    motionEventInfo.recycle();
                } else {
                    this.mPreLastDown = null;
                    this.mPreLastUp = null;
                    this.mLastDown = null;
                    this.mLastUp = null;
                    return;
                }
            }
        }

        void transitionToDelegatingStateAndClear() {
            FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler = FullScreenMagnificationGestureHandler.this;
            fullScreenMagnificationGestureHandler.transitionTo(fullScreenMagnificationGestureHandler.mDelegatingState);
            sendDelayedMotionEvents();
            removePendingDelayedMessages();
            this.mSecondPointerDownLocation.set(Float.NaN, Float.NaN);
        }

        private void onTripleTap(MotionEvent motionEvent) {
            if (FullScreenMagnificationGestureHandler.DEBUG_DETECTING) {
                Slog.i(FullScreenMagnificationGestureHandler.this.mLogTag, "onTripleTap(); delayed: " + MotionEventInfo.toString(this.mDelayedEventQueue));
            }
            if (!isActivated() || this.mShortcutTriggered) {
                FullScreenMagnificationGestureHandler.this.mPromptController.showNotificationIfNeeded();
                FullScreenMagnificationGestureHandler.this.zoomOn(motionEvent.getX(), motionEvent.getY());
            } else {
                FullScreenMagnificationGestureHandler.this.zoomOff();
            }
            clear();
        }

        private boolean isActivated() {
            FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler = FullScreenMagnificationGestureHandler.this;
            return fullScreenMagnificationGestureHandler.mFullScreenMagnificationController.isActivated(fullScreenMagnificationGestureHandler.mDisplayId);
        }

        void transitionToViewportDraggingStateAndClear(MotionEvent motionEvent) {
            if (FullScreenMagnificationGestureHandler.DEBUG_DETECTING) {
                Slog.i(FullScreenMagnificationGestureHandler.this.mLogTag, "onTripleTapAndHold()");
            }
            boolean z = this.mShortcutTriggered;
            clear();
            AccessibilityStatsLogUtils.logMagnificationTripleTap(!isActivated());
            FullScreenMagnificationGestureHandler.this.mViewportDraggingState.prepareForZoomInTemporary(z);
            FullScreenMagnificationGestureHandler.this.zoomInTemporary(motionEvent.getX(), motionEvent.getY());
            FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler = FullScreenMagnificationGestureHandler.this;
            fullScreenMagnificationGestureHandler.transitionTo(fullScreenMagnificationGestureHandler.mViewportDraggingState);
        }

        public String toString() {
            return "DetectingState{tapCount()=" + tapCount() + ", mShortcutTriggered=" + this.mShortcutTriggered + ", mDelayedEventQueue=" + MotionEventInfo.toString(this.mDelayedEventQueue) + '}';
        }

        void toggleShortcutTriggered() {
            setShortcutTriggered(!this.mShortcutTriggered);
        }

        void setShortcutTriggered(boolean z) {
            if (this.mShortcutTriggered == z) {
                return;
            }
            if (FullScreenMagnificationGestureHandler.DEBUG_DETECTING) {
                Slog.i(FullScreenMagnificationGestureHandler.this.mLogTag, "setShortcutTriggered(" + z + ")");
            }
            this.mShortcutTriggered = z;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isShortcutTriggered() {
            return this.mShortcutTriggered;
        }

        boolean isTapOutOfDistanceSlop() {
            MotionEvent motionEvent;
            MotionEvent motionEvent2;
            if (FullScreenMagnificationGestureHandler.this.mDetectTripleTap && (motionEvent = this.mPreLastDown) != null && (motionEvent2 = this.mLastDown) != null) {
                boolean z = GestureUtils.distance(motionEvent, motionEvent2) > ((double) this.mMultiTapMaxDistance);
                if (tapCount() > 0) {
                    return z;
                }
                if (z && !GestureUtils.isTimedOut(this.mPreLastDown, this.mLastDown, this.mMultiTapMaxDelay)) {
                    return true;
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zoomInTemporary(float f, float f2) {
        float scale = this.mFullScreenMagnificationController.getScale(this.mDisplayId);
        float constrain = MathUtils.constrain(this.mFullScreenMagnificationController.getPersistedScale(this.mDisplayId), 1.0f, 8.0f);
        if (this.mFullScreenMagnificationController.isActivated(this.mDisplayId)) {
            constrain = scale + 1.0f;
        }
        zoomToScale(constrain, f, f2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zoomOn(float f, float f2) {
        if (DEBUG_DETECTING) {
            Slog.i(this.mLogTag, "zoomOn(" + f + ", " + f2 + ")");
        }
        zoomToScale(MathUtils.constrain(this.mFullScreenMagnificationController.getPersistedScale(this.mDisplayId), 1.0f, 8.0f), f, f2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zoomToScale(float f, float f2, float f3) {
        this.mFullScreenMagnificationController.setScaleAndCenter(this.mDisplayId, MathUtils.constrain(f, 1.0f, 8.0f), f2, f3, true, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zoomOff() {
        if (DEBUG_DETECTING) {
            Slog.i(this.mLogTag, "zoomOff()");
        }
        this.mFullScreenMagnificationController.reset(this.mDisplayId, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static MotionEvent recycleAndNullify(MotionEvent motionEvent) {
        if (motionEvent == null) {
            return null;
        }
        motionEvent.recycle();
        return null;
    }

    public String toString() {
        return "MagnificationGesture{mDetectingState=" + this.mDetectingState + ", mDelegatingState=" + this.mDelegatingState + ", mMagnifiedInteractionState=" + this.mPanningScalingState + ", mViewportDraggingState=" + this.mViewportDraggingState + ", mDetectTripleTap=" + this.mDetectTripleTap + ", mDetectShortcutTrigger=" + this.mDetectShortcutTrigger + ", mCurrentState=" + State.nameOf(this.mCurrentState) + ", mPreviousState=" + State.nameOf(this.mPreviousState) + ", mMagnificationController=" + this.mFullScreenMagnificationController + ", mDisplayId=" + this.mDisplayId + '}';
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class MotionEventInfo {
        private static final int MAX_POOL_SIZE = 10;
        private static final Object sLock = new Object();
        private static MotionEventInfo sPool;
        private static int sPoolSize;
        public MotionEvent event;
        private boolean mInPool;
        private MotionEventInfo mNext;
        public int policyFlags;
        public MotionEvent rawEvent;

        private MotionEventInfo() {
        }

        public static MotionEventInfo obtain(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
            MotionEventInfo obtainInternal;
            synchronized (sLock) {
                obtainInternal = obtainInternal();
                obtainInternal.initialize(motionEvent, motionEvent2, i);
            }
            return obtainInternal;
        }

        private static MotionEventInfo obtainInternal() {
            int i = sPoolSize;
            if (i > 0) {
                sPoolSize = i - 1;
                MotionEventInfo motionEventInfo = sPool;
                sPool = motionEventInfo.mNext;
                motionEventInfo.mNext = null;
                motionEventInfo.mInPool = false;
                return motionEventInfo;
            }
            return new MotionEventInfo();
        }

        private void initialize(MotionEvent motionEvent, MotionEvent motionEvent2, int i) {
            this.event = MotionEvent.obtain(motionEvent);
            this.rawEvent = MotionEvent.obtain(motionEvent2);
            this.policyFlags = i;
        }

        public void recycle() {
            synchronized (sLock) {
                if (this.mInPool) {
                    throw new IllegalStateException("Already recycled.");
                }
                clear();
                int i = sPoolSize;
                if (i < 10) {
                    sPoolSize = i + 1;
                    this.mNext = sPool;
                    sPool = this;
                    this.mInPool = true;
                }
            }
        }

        private void clear() {
            this.event = FullScreenMagnificationGestureHandler.recycleAndNullify(this.event);
            this.rawEvent = FullScreenMagnificationGestureHandler.recycleAndNullify(this.rawEvent);
            this.policyFlags = 0;
        }

        static int countOf(MotionEventInfo motionEventInfo, int i) {
            if (motionEventInfo == null) {
                return 0;
            }
            return (motionEventInfo.event.getAction() == i ? 1 : 0) + countOf(motionEventInfo.mNext, i);
        }

        public static String toString(MotionEventInfo motionEventInfo) {
            if (motionEventInfo == null) {
                return "";
            }
            return MotionEvent.actionToString(motionEventInfo.event.getAction()).replace("ACTION_", "") + " " + toString(motionEventInfo.mNext);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private static class ScreenStateReceiver extends BroadcastReceiver {
        private final Context mContext;
        private final FullScreenMagnificationGestureHandler mGestureHandler;
        private boolean mRegistered = false;

        ScreenStateReceiver(Context context, FullScreenMagnificationGestureHandler fullScreenMagnificationGestureHandler) {
            this.mContext = context;
            this.mGestureHandler = fullScreenMagnificationGestureHandler;
        }

        public void register() {
            if (this.mRegistered) {
                return;
            }
            this.mContext.registerReceiver(this, new IntentFilter("android.intent.action.SCREEN_OFF"));
            this.mRegistered = true;
        }

        public void unregister() {
            if (this.mRegistered) {
                this.mContext.unregisterReceiver(this);
                this.mRegistered = false;
            }
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            this.mGestureHandler.mDetectingState.setShortcutTriggered(false);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private static class GestureException extends Exception {
        GestureException(String str) {
            super(str);
        }
    }
}
