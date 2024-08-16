package com.android.server.wm;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.InputConstants;
import android.os.RemoteException;
import android.os.Trace;
import android.util.DisplayMetrics;
import android.util.Slog;
import android.view.BatchedInputEventReceiver;
import android.view.InputApplicationHandle;
import android.view.InputChannel;
import android.view.InputEvent;
import android.view.InputEventReceiver;
import android.view.InputWindowHandle;
import android.view.MotionEvent;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.policy.TaskResizingAlgorithm;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import java.util.concurrent.CompletableFuture;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class TaskPositioner implements IBinder.DeathRecipient {
    private static final boolean DEBUG_ORIENTATION_VIOLATIONS = false;
    public static final float RESIZING_HINT_ALPHA = 0.5f;
    public static final int RESIZING_HINT_DURATION_MS = 0;
    private static final String TAG = "WindowManager";
    private static final String TAG_LOCAL = "TaskPositioner";
    private static Factory sFactory;
    IBinder mClientCallback;
    InputChannel mClientChannel;
    private DisplayContent mDisplayContent;
    InputApplicationHandle mDragApplicationHandle;

    @VisibleForTesting
    boolean mDragEnded;
    InputWindowHandle mDragWindowHandle;
    private InputEventReceiver mInputEventReceiver;
    private int mMinVisibleHeight;
    private int mMinVisibleWidth;
    private boolean mPreserveOrientation;
    private boolean mResizing;
    private final WindowManagerService mService;
    private float mStartDragX;
    private float mStartDragY;
    private boolean mStartOrientationWasLandscape;

    @VisibleForTesting
    Task mTask;
    WindowState mWindow;
    private Rect mTmpRect = new Rect();
    private final Rect mWindowOriginalBounds = new Rect();
    private final Rect mWindowDragBounds = new Rect();
    private final Point mMaxVisibleSize = new Point();
    private int mCtrlType = 0;

    private void checkBoundsForOrientationViolations(Rect rect) {
    }

    public String toShortString() {
        return TAG;
    }

    @VisibleForTesting
    TaskPositioner(WindowManagerService windowManagerService) {
        this.mService = windowManagerService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean onInputEvent(InputEvent inputEvent) {
        if (!(inputEvent instanceof MotionEvent) || (inputEvent.getSource() & 2) == 0) {
            return false;
        }
        MotionEvent motionEvent = (MotionEvent) inputEvent;
        if (this.mDragEnded) {
            return true;
        }
        float rawX = motionEvent.getRawX();
        float rawY = motionEvent.getRawY();
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action == 1) {
                if (WindowManagerDebugConfig.DEBUG_TASK_POSITIONING) {
                    Slog.w(TAG, "ACTION_UP @ {" + rawX + ", " + rawY + "}");
                }
                this.mDragEnded = true;
            } else if (action == 2) {
                if (WindowManagerDebugConfig.DEBUG_TASK_POSITIONING) {
                    Slog.w(TAG, "ACTION_MOVE @ {" + rawX + ", " + rawY + "}");
                }
                WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
                WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        this.mDragEnded = notifyMoveLocked(rawX, rawY);
                        this.mTask.getDimBounds(this.mTmpRect);
                    } finally {
                    }
                }
                WindowManagerService.resetPriorityAfterLockedSection();
                if (!this.mTmpRect.equals(this.mWindowDragBounds)) {
                    Trace.traceBegin(32L, "wm.TaskPositioner.resizeTask");
                    this.mService.mAtmService.resizeTask(this.mTask.mTaskId, this.mWindowDragBounds, 1);
                    Trace.traceEnd(32L);
                }
            } else if (action == 3) {
                if (WindowManagerDebugConfig.DEBUG_TASK_POSITIONING) {
                    Slog.w(TAG, "ACTION_CANCEL @ {" + rawX + ", " + rawY + "}");
                }
                this.mDragEnded = true;
            }
        } else if (WindowManagerDebugConfig.DEBUG_TASK_POSITIONING) {
            Slog.w(TAG, "ACTION_DOWN @ {" + rawX + ", " + rawY + "}");
        }
        if (this.mDragEnded) {
            boolean z = this.mResizing;
            WindowManagerGlobalLock windowManagerGlobalLock2 = this.mService.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock2) {
                try {
                    endDragLocked();
                    this.mTask.getDimBounds(this.mTmpRect);
                } finally {
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
            if (z && !this.mTmpRect.equals(this.mWindowDragBounds)) {
                this.mService.mAtmService.resizeTask(this.mTask.mTaskId, this.mWindowDragBounds, 3);
            }
            this.mService.mTaskPositioningController.finishTaskPositioning();
        }
        return true;
    }

    @VisibleForTesting
    Rect getWindowDragBounds() {
        return this.mWindowDragBounds;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CompletableFuture<Void> register(final DisplayContent displayContent, final WindowState windowState) {
        if (WindowManagerDebugConfig.DEBUG_TASK_POSITIONING) {
            Slog.d(TAG, "Registering task positioner");
        }
        if (this.mClientChannel != null) {
            Slog.e(TAG, "Task positioner already registered");
            return CompletableFuture.completedFuture(null);
        }
        this.mDisplayContent = displayContent;
        this.mClientChannel = this.mService.mInputManager.createInputChannel(TAG);
        this.mInputEventReceiver = new BatchedInputEventReceiver.SimpleBatchedInputEventReceiver(this.mClientChannel, this.mService.mAnimationHandler.getLooper(), this.mService.mAnimator.getChoreographer(), new BatchedInputEventReceiver.SimpleBatchedInputEventReceiver.InputEventListener() { // from class: com.android.server.wm.TaskPositioner$$ExternalSyntheticLambda0
            public final boolean onInputEvent(InputEvent inputEvent) {
                boolean onInputEvent;
                onInputEvent = TaskPositioner.this.onInputEvent(inputEvent);
                return onInputEvent;
            }
        });
        this.mDragApplicationHandle = new InputApplicationHandle(new Binder(), TAG, InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS);
        InputWindowHandle inputWindowHandle = new InputWindowHandle(this.mDragApplicationHandle, displayContent.getDisplayId());
        this.mDragWindowHandle = inputWindowHandle;
        inputWindowHandle.name = TAG;
        inputWindowHandle.token = this.mClientChannel.getToken();
        InputWindowHandle inputWindowHandle2 = this.mDragWindowHandle;
        inputWindowHandle2.layoutParamsType = 2016;
        inputWindowHandle2.dispatchingTimeoutMillis = InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS;
        inputWindowHandle2.ownerPid = WindowManagerService.MY_PID;
        inputWindowHandle2.ownerUid = WindowManagerService.MY_UID;
        inputWindowHandle2.scaleFactor = 1.0f;
        inputWindowHandle2.inputConfig = 4;
        inputWindowHandle2.touchableRegion.setEmpty();
        if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_ORIENTATION, 791468751, 0, (String) null, (Object[]) null);
        }
        this.mDisplayContent.getDisplayRotation().pause();
        return this.mService.mTaskPositioningController.showInputSurface(windowState.getDisplayId()).thenRun(new Runnable() { // from class: com.android.server.wm.TaskPositioner$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                TaskPositioner.this.lambda$register$0(displayContent, windowState);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$register$0(DisplayContent displayContent, WindowState windowState) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                Rect rect = this.mTmpRect;
                displayContent.getBounds(rect);
                DisplayMetrics displayMetrics = displayContent.getDisplayMetrics();
                this.mMinVisibleWidth = WindowManagerService.dipToPixel(48, displayMetrics);
                this.mMinVisibleHeight = WindowManagerService.dipToPixel(32, displayMetrics);
                this.mMaxVisibleSize.set(rect.width(), rect.height());
                this.mDragEnded = false;
                try {
                    IBinder asBinder = windowState.mClient.asBinder();
                    this.mClientCallback = asBinder;
                    asBinder.linkToDeath(this, 0);
                    this.mWindow = windowState;
                    this.mTask = windowState.getTask();
                } catch (RemoteException unused) {
                    this.mService.mTaskPositioningController.finishTaskPositioning();
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregister() {
        if (WindowManagerDebugConfig.DEBUG_TASK_POSITIONING) {
            Slog.d(TAG, "Unregistering task positioner");
        }
        if (this.mClientChannel == null) {
            Slog.e(TAG, "Task positioner not registered");
            return;
        }
        this.mService.mTaskPositioningController.hideInputSurface(this.mDisplayContent.getDisplayId());
        this.mService.mInputManager.removeInputChannel(this.mClientChannel.getToken());
        this.mInputEventReceiver.dispose();
        this.mInputEventReceiver = null;
        this.mClientChannel.dispose();
        this.mClientChannel = null;
        this.mDragWindowHandle = null;
        this.mDragApplicationHandle = null;
        this.mDragEnded = true;
        this.mDisplayContent.getInputMonitor().updateInputWindowsLw(true);
        if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_ORIENTATION, 1422781269, 0, (String) null, (Object[]) null);
        }
        this.mDisplayContent.getDisplayRotation().resume();
        this.mDisplayContent = null;
        IBinder iBinder = this.mClientCallback;
        if (iBinder != null) {
            iBinder.unlinkToDeath(this, 0);
        }
        this.mWindow = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startDrag(boolean z, boolean z2, float f, float f2) {
        if (WindowManagerDebugConfig.DEBUG_TASK_POSITIONING) {
            Slog.d(TAG, "startDrag: win=" + this.mWindow + ", resize=" + z + ", preserveOrientation=" + z2 + ", {" + f + ", " + f2 + "}");
        }
        final Rect rect = this.mTmpRect;
        this.mTask.getBounds(rect);
        this.mCtrlType = 0;
        this.mStartDragX = f;
        this.mStartDragY = f2;
        this.mPreserveOrientation = z2;
        if (z) {
            if (f < rect.left) {
                this.mCtrlType = 0 | 1;
            }
            if (f > rect.right) {
                this.mCtrlType |= 2;
            }
            if (f2 < rect.top) {
                this.mCtrlType |= 4;
            }
            if (f2 > rect.bottom) {
                this.mCtrlType |= 8;
            }
            this.mResizing = this.mCtrlType != 0;
        }
        this.mStartOrientationWasLandscape = rect.width() >= rect.height();
        this.mWindowOriginalBounds.set(rect);
        if (this.mResizing) {
            notifyMoveLocked(f, f2);
            this.mService.mH.post(new Runnable() { // from class: com.android.server.wm.TaskPositioner$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    TaskPositioner.this.lambda$startDrag$1(rect);
                }
            });
        }
        this.mWindowDragBounds.set(rect);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startDrag$1(Rect rect) {
        this.mService.mAtmService.resizeTask(this.mTask.mTaskId, rect, 3);
    }

    private void endDragLocked() {
        this.mResizing = false;
        this.mTask.setDragResizing(false);
    }

    @VisibleForTesting
    boolean notifyMoveLocked(float f, float f2) {
        if (WindowManagerDebugConfig.DEBUG_TASK_POSITIONING) {
            Slog.d(TAG, "notifyMoveLocked: {" + f + "," + f2 + "}");
        }
        if (this.mCtrlType != 0) {
            resizeDrag(f, f2);
            this.mTask.setDragResizing(true);
            return false;
        }
        this.mDisplayContent.getStableRect(this.mTmpRect);
        this.mTmpRect.intersect(this.mTask.getRootTask().getParent().getBounds());
        int i = (int) f;
        int i2 = (int) f2;
        if (!this.mTmpRect.contains(i, i2)) {
            i = Math.min(Math.max(i, this.mTmpRect.left), this.mTmpRect.right);
            i2 = Math.min(Math.max(i2, this.mTmpRect.top), this.mTmpRect.bottom);
        }
        updateWindowDragBounds(i, i2, this.mTmpRect);
        return false;
    }

    @VisibleForTesting
    void resizeDrag(float f, float f2) {
        updateDraggedBounds(TaskResizingAlgorithm.resizeDrag(f, f2, this.mStartDragX, this.mStartDragY, this.mWindowOriginalBounds, this.mCtrlType, this.mMinVisibleWidth, this.mMinVisibleHeight, this.mMaxVisibleSize, this.mPreserveOrientation, this.mStartOrientationWasLandscape));
    }

    private void updateDraggedBounds(Rect rect) {
        this.mWindowDragBounds.set(rect);
        checkBoundsForOrientationViolations(this.mWindowDragBounds);
    }

    private void updateWindowDragBounds(int i, int i2, Rect rect) {
        int round = Math.round(i - this.mStartDragX);
        int round2 = Math.round(i2 - this.mStartDragY);
        this.mWindowDragBounds.set(this.mWindowOriginalBounds);
        int i3 = rect.right;
        int i4 = this.mMinVisibleWidth;
        this.mWindowDragBounds.offsetTo(Math.min(Math.max(this.mWindowOriginalBounds.left + round, (rect.left + i4) - this.mWindowOriginalBounds.width()), i3 - i4), Math.min(Math.max(this.mWindowOriginalBounds.top + round2, rect.top), rect.bottom - this.mMinVisibleHeight));
        if (WindowManagerDebugConfig.DEBUG_TASK_POSITIONING) {
            Slog.d(TAG, "updateWindowDragBounds: " + this.mWindowDragBounds);
        }
    }

    static void setFactory(Factory factory) {
        sFactory = factory;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static TaskPositioner create(WindowManagerService windowManagerService) {
        if (sFactory == null) {
            sFactory = new Factory() { // from class: com.android.server.wm.TaskPositioner.1
            };
        }
        return sFactory.create(windowManagerService);
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        this.mService.mTaskPositioningController.finishTaskPositioning();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface Factory {
        default TaskPositioner create(WindowManagerService windowManagerService) {
            return new TaskPositioner(windowManagerService);
        }
    }
}
