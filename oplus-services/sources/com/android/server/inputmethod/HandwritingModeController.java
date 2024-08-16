package com.android.server.inputmethod;

import android.annotation.RequiresPermission;
import android.hardware.input.InputManagerGlobal;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Slog;
import android.view.BatchedInputEventReceiver;
import android.view.Choreographer;
import android.view.InputChannel;
import android.view.InputEvent;
import android.view.InputEventReceiver;
import android.view.MotionEvent;
import android.view.SurfaceControl;
import com.android.server.LocalServices;
import com.android.server.input.InputManagerInternal;
import com.android.server.wm.WindowManagerInternal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class HandwritingModeController {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    static boolean DEBUG = false;
    private static final int EVENT_BUFFER_SIZE = 100;
    private static final long HANDWRITING_DELEGATION_IDLE_TIMEOUT_MS = 3000;
    private static final int LONG_EVENT_BUFFER_SIZE = 2000;
    public static final String TAG = "HandwritingModeController";
    private String mDelegatePackageName;
    private Handler mDelegationIdleTimeoutHandler;
    private Runnable mDelegationIdleTimeoutRunnable;
    private String mDelegatorPackageName;
    private ArrayList<MotionEvent> mHandwritingBuffer;
    private InputEventReceiver mHandwritingEventReceiver;
    private HandwritingEventReceiverSurface mHandwritingSurface;
    private Runnable mInkWindowInitRunnable;
    private final Looper mLooper;
    private boolean mRecordingGesture;
    private int mCurrentDisplayId = -1;
    private final InputManagerInternal mInputManagerInternal = (InputManagerInternal) LocalServices.getService(InputManagerInternal.class);
    private final WindowManagerInternal mWindowManagerInternal = (WindowManagerInternal) LocalServices.getService(WindowManagerInternal.class);
    private int mCurrentRequestId = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public HandwritingModeController(Looper looper, Runnable runnable) {
        this.mLooper = looper;
        this.mInkWindowInitRunnable = runnable;
    }

    private static boolean isStylusEvent(MotionEvent motionEvent) {
        if (!motionEvent.isFromSource(16386)) {
            return false;
        }
        int toolType = motionEvent.getToolType(0);
        return toolType == 2 || toolType == 4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void initializeHandwritingSpy(int i) {
        reset(i == this.mCurrentDisplayId);
        this.mCurrentDisplayId = i;
        if (this.mHandwritingBuffer == null) {
            this.mHandwritingBuffer = new ArrayList<>(getHandwritingBufferSize());
        }
        if (DEBUG) {
            Slog.d(TAG, "Initializing handwriting spy monitor for display: " + i);
        }
        String str = "stylus-handwriting-event-receiver-" + i;
        InputChannel createInputChannel = this.mInputManagerInternal.createInputChannel(str);
        Objects.requireNonNull(createInputChannel, "Failed to create input channel");
        HandwritingEventReceiverSurface handwritingEventReceiverSurface = this.mHandwritingSurface;
        SurfaceControl surface = handwritingEventReceiverSurface != null ? handwritingEventReceiverSurface.getSurface() : this.mWindowManagerInternal.getHandwritingSurfaceForDisplay(i);
        if (surface == null) {
            Slog.e(TAG, "Failed to create input surface");
            return;
        }
        this.mHandwritingSurface = new HandwritingEventReceiverSurface(str, i, surface, createInputChannel);
        this.mHandwritingEventReceiver = new BatchedInputEventReceiver.SimpleBatchedInputEventReceiver(createInputChannel.dup(), this.mLooper, Choreographer.getInstance(), new BatchedInputEventReceiver.SimpleBatchedInputEventReceiver.InputEventListener() { // from class: com.android.server.inputmethod.HandwritingModeController$$ExternalSyntheticLambda2
            public final boolean onInputEvent(InputEvent inputEvent) {
                boolean onInputEvent;
                onInputEvent = HandwritingModeController.this.onInputEvent(inputEvent);
                return onInputEvent;
            }
        });
        this.mCurrentRequestId++;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public OptionalInt getCurrentRequestId() {
        if (this.mHandwritingSurface == null) {
            Slog.e(TAG, "Cannot get requestId: Handwriting was not initialized.");
            return OptionalInt.empty();
        }
        return OptionalInt.of(this.mCurrentRequestId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isStylusGestureOngoing() {
        return this.mRecordingGesture;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasOngoingStylusHandwritingSession() {
        HandwritingEventReceiverSurface handwritingEventReceiverSurface = this.mHandwritingSurface;
        return handwritingEventReceiverSurface != null && handwritingEventReceiverSurface.isIntercepting();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void prepareStylusHandwritingDelegation(String str, String str2) {
        this.mDelegatePackageName = str;
        this.mDelegatorPackageName = str2;
        ArrayList<MotionEvent> arrayList = this.mHandwritingBuffer;
        if (arrayList == null) {
            this.mHandwritingBuffer = new ArrayList<>(getHandwritingBufferSize());
        } else {
            arrayList.ensureCapacity(getHandwritingBufferSize());
        }
        scheduleHandwritingDelegationTimeout();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getDelegatePackageName() {
        return this.mDelegatePackageName;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getDelegatorPackageName() {
        return this.mDelegatorPackageName;
    }

    private void scheduleHandwritingDelegationTimeout() {
        Handler handler = this.mDelegationIdleTimeoutHandler;
        if (handler == null) {
            this.mDelegationIdleTimeoutHandler = new Handler(this.mLooper);
        } else {
            handler.removeCallbacks(this.mDelegationIdleTimeoutRunnable);
        }
        Runnable runnable = new Runnable() { // from class: com.android.server.inputmethod.HandwritingModeController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                HandwritingModeController.this.lambda$scheduleHandwritingDelegationTimeout$0();
            }
        };
        this.mDelegationIdleTimeoutRunnable = runnable;
        this.mDelegationIdleTimeoutHandler.postDelayed(runnable, HANDWRITING_DELEGATION_IDLE_TIMEOUT_MS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleHandwritingDelegationTimeout$0() {
        Slog.d(TAG, "Stylus handwriting delegation idle timed-out.");
        clearPendingHandwritingDelegation();
        ArrayList<MotionEvent> arrayList = this.mHandwritingBuffer;
        if (arrayList != null) {
            arrayList.forEach(new HandwritingModeController$$ExternalSyntheticLambda0());
            this.mHandwritingBuffer.clear();
            this.mHandwritingBuffer.trimToSize();
            this.mHandwritingBuffer.ensureCapacity(getHandwritingBufferSize());
        }
    }

    private int getHandwritingBufferSize() {
        return (this.mDelegatePackageName == null || this.mDelegatorPackageName == null) ? 100 : 2000;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearPendingHandwritingDelegation() {
        if (DEBUG) {
            Slog.d(TAG, "clearPendingHandwritingDelegation");
        }
        Handler handler = this.mDelegationIdleTimeoutHandler;
        if (handler != null) {
            handler.removeCallbacks(this.mDelegationIdleTimeoutRunnable);
            this.mDelegationIdleTimeoutHandler = null;
        }
        this.mDelegationIdleTimeoutRunnable = null;
        this.mDelegatorPackageName = null;
        this.mDelegatePackageName = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @RequiresPermission("android.permission.MONITOR_INPUT")
    public HandwritingSession startHandwritingSession(int i, int i2, int i3, IBinder iBinder) {
        if (this.mHandwritingSurface == null) {
            Slog.e(TAG, "Cannot start handwriting session: Handwriting was not initialized.");
            return null;
        }
        if (i != this.mCurrentRequestId) {
            Slog.e(TAG, "Cannot start handwriting session: Invalid request id: " + i);
            return null;
        }
        if (!this.mRecordingGesture || this.mHandwritingBuffer.isEmpty()) {
            Slog.e(TAG, "Cannot start handwriting session: No stylus gesture is being recorded.");
            return null;
        }
        Objects.requireNonNull(this.mHandwritingEventReceiver, "Handwriting session was already transferred to IME.");
        MotionEvent motionEvent = this.mHandwritingBuffer.get(0);
        if (!this.mWindowManagerInternal.isPointInsideWindow(iBinder, this.mCurrentDisplayId, motionEvent.getRawX(), motionEvent.getRawY())) {
            Slog.e(TAG, "Cannot start handwriting session: Stylus gesture did not start inside the focused window.");
            return null;
        }
        if (DEBUG) {
            Slog.d(TAG, "Starting handwriting session in display: " + this.mCurrentDisplayId);
        }
        InputManagerGlobal.getInstance().pilferPointers(this.mHandwritingSurface.getInputChannel().getToken());
        this.mHandwritingEventReceiver.dispose();
        this.mHandwritingEventReceiver = null;
        this.mRecordingGesture = false;
        if (this.mHandwritingSurface.isIntercepting()) {
            throw new IllegalStateException("Handwriting surface should not be already intercepting.");
        }
        this.mHandwritingSurface.startIntercepting(i2, i3);
        InputManagerGlobal.getInstance().setPointerIconType(1);
        return new HandwritingSession(this.mCurrentRequestId, this.mHandwritingSurface.getInputChannel(), this.mHandwritingBuffer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reset() {
        reset(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setInkWindowInitializer(Runnable runnable) {
        this.mInkWindowInitRunnable = runnable;
    }

    private void reset(boolean z) {
        InputEventReceiver inputEventReceiver = this.mHandwritingEventReceiver;
        if (inputEventReceiver != null) {
            inputEventReceiver.dispose();
            this.mHandwritingEventReceiver = null;
        }
        ArrayList<MotionEvent> arrayList = this.mHandwritingBuffer;
        if (arrayList != null) {
            arrayList.forEach(new HandwritingModeController$$ExternalSyntheticLambda0());
            this.mHandwritingBuffer.clear();
            if (!z) {
                this.mHandwritingBuffer = null;
            }
        }
        HandwritingEventReceiverSurface handwritingEventReceiverSurface = this.mHandwritingSurface;
        if (handwritingEventReceiverSurface != null) {
            handwritingEventReceiverSurface.getInputChannel().dispose();
            if (!z) {
                this.mHandwritingSurface.remove();
                this.mHandwritingSurface = null;
            }
        }
        clearPendingHandwritingDelegation();
        this.mRecordingGesture = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean onInputEvent(InputEvent inputEvent) {
        if (this.mHandwritingEventReceiver == null) {
            throw new IllegalStateException("Input Event should not be processed when IME has the spy channel.");
        }
        if (!(inputEvent instanceof MotionEvent)) {
            Slog.wtf(TAG, "Received non-motion event in stylus monitor.");
            return false;
        }
        MotionEvent motionEvent = (MotionEvent) inputEvent;
        if (!isStylusEvent(motionEvent)) {
            return false;
        }
        if (motionEvent.getDisplayId() != this.mCurrentDisplayId) {
            Slog.wtf(TAG, "Received stylus event associated with the incorrect display.");
            return false;
        }
        onStylusEvent(motionEvent);
        return true;
    }

    private void onStylusEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (this.mInkWindowInitRunnable != null && (actionMasked == 9 || motionEvent.getAction() == 9)) {
            this.mInkWindowInitRunnable.run();
            this.mInkWindowInitRunnable = null;
        }
        if (TextUtils.isEmpty(this.mDelegatePackageName) && (actionMasked == 1 || actionMasked == 3)) {
            this.mRecordingGesture = false;
            this.mHandwritingBuffer.clear();
            return;
        }
        if (actionMasked == 0) {
            this.mRecordingGesture = true;
        }
        if (this.mRecordingGesture) {
            if (this.mHandwritingBuffer.size() >= getHandwritingBufferSize()) {
                if (DEBUG) {
                    Slog.w(TAG, "Current gesture exceeds the buffer capacity. The rest of the gesture will not be recorded.");
                }
                this.mRecordingGesture = false;
                return;
            }
            this.mHandwritingBuffer.add(MotionEvent.obtain(motionEvent));
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    static final class HandwritingSession {
        private final InputChannel mHandwritingChannel;
        private final List<MotionEvent> mRecordedEvents;
        private final int mRequestId;

        private HandwritingSession(int i, InputChannel inputChannel, List<MotionEvent> list) {
            this.mRequestId = i;
            this.mHandwritingChannel = inputChannel;
            this.mRecordedEvents = list;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int getRequestId() {
            return this.mRequestId;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public InputChannel getHandwritingChannel() {
            return this.mHandwritingChannel;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public List<MotionEvent> getRecordedEvents() {
            return this.mRecordedEvents;
        }
    }
}
