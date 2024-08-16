package com.android.server.wm;

import android.content.ClipData;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Slog;
import android.view.IWindow;
import android.view.SurfaceControl;
import android.view.accessibility.AccessibilityManager;
import com.android.server.wm.DragState;
import com.android.server.wm.IDragDropControllerExt;
import com.android.server.wm.WindowManagerInternal;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class DragDropController {
    private static final int A11Y_DRAG_TIMEOUT_DEFAULT_MS = 60000;
    private static final float DRAG_SHADOW_ALPHA_TRANSPARENT = 0.7071f;
    static final long DRAG_TIMEOUT_MS = 5000;
    static final int MSG_ANIMATION_END = 2;
    static final int MSG_DRAG_END_TIMEOUT = 0;
    static final int MSG_REMOVE_DRAG_SURFACE_TIMEOUT = 3;
    static final int MSG_TEAR_DOWN_DRAG_AND_DROP_INPUT = 1;
    private DragState mDragState;
    private final Handler mHandler;
    private WindowManagerService mService;
    public IDragDropControllerExt mDragDropControllerExt = (IDragDropControllerExt) ExtLoader.type(IDragDropControllerExt.class).base(this).create();
    private AtomicReference<WindowManagerInternal.IDragDropCallback> mCallback = new AtomicReference<>(new WindowManagerInternal.IDragDropCallback() { // from class: com.android.server.wm.DragDropController.1
    });

    /* JADX INFO: Access modifiers changed from: package-private */
    public DragDropController(WindowManagerService windowManagerService, Looper looper) {
        this.mService = windowManagerService;
        this.mHandler = new DragHandler(windowManagerService, looper);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean dragDropActiveLocked() {
        DragState dragState = this.mDragState;
        return (dragState == null || dragState.isClosing()) ? false : true;
    }

    boolean dragSurfaceRelinquishedToDropTarget() {
        DragState dragState = this.mDragState;
        return dragState != null && dragState.mRelinquishDragSurfaceToDropTarget;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerCallback(WindowManagerInternal.IDragDropCallback iDragDropCallback) {
        Objects.requireNonNull(iDragDropCallback);
        this.mCallback.set(iDragDropCallback);
        if (iDragDropCallback instanceof IDragDropControllerExt.IOplusDragDropControllerExtCallback) {
            Slog.d("WindowManager", " controllerExt registerCallback ");
            this.mDragDropControllerExt.registerCallback((IDragDropControllerExt.IOplusDragDropControllerExtCallback) iDragDropCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendDragStartedIfNeededLocked(WindowState windowState) {
        this.mDragState.sendDragStartedIfNeededLocked(windowState);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Finally extract failed */
    public IBinder performDrag(int i, int i2, IWindow iWindow, int i3, SurfaceControl surfaceControl, int i4, float f, float f2, float f3, float f4, ClipData clipData) {
        SurfaceControl surfaceControl2;
        boolean z;
        if (WindowManagerDebugConfig.DEBUG_DRAG) {
            Slog.d("WindowManager", "perform drag: win=" + iWindow + " surface=" + surfaceControl + " flags=" + Integer.toHexString(i3) + " data=" + clipData + " touch(" + f + "," + f2 + ") thumb center(" + f3 + "," + f4 + ")");
        }
        Binder binder = new Binder();
        boolean prePerformDrag = this.mCallback.get().prePerformDrag(iWindow, binder, i4, f, f2, f3, f4, clipData);
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    try {
                        if (prePerformDrag) {
                            try {
                                if (!dragDropActiveLocked()) {
                                    WindowState windowForClientLocked = this.mService.windowForClientLocked((Session) null, iWindow, false);
                                    if (windowForClientLocked != null && windowForClientLocked.canReceiveTouchInput()) {
                                        DisplayContent displayContent = windowForClientLocked.getDisplayContent();
                                        if (displayContent != null) {
                                            float f5 = (i3 & 512) == 0 ? DRAG_SHADOW_ALPHA_TRANSPARENT : 1.0f;
                                            DragState dragState = new DragState(this.mService, this, new Binder(), surfaceControl, i3, iWindow.asBinder());
                                            this.mDragState = dragState;
                                            try {
                                                dragState.mPid = i;
                                                dragState.mUid = i2;
                                                dragState.mOriginalAlpha = f5;
                                                dragState.mAnimatedScale = windowForClientLocked.mGlobalScale;
                                                dragState.mToken = binder;
                                                dragState.mDisplayContent = displayContent;
                                                dragState.mData = clipData;
                                                if ((i3 & 1024) == 0) {
                                                    CompletableFuture<Boolean> registerInputChannel = this.mCallback.get().registerInputChannel(this.mDragState, displayContent.getDisplay(), this.mService.mInputManager, windowForClientLocked.mInputChannel);
                                                    WindowManagerService.resetPriorityAfterLockedSection();
                                                    try {
                                                        z = registerInputChannel.get(DRAG_TIMEOUT_MS, TimeUnit.MILLISECONDS).booleanValue();
                                                    } catch (Exception e) {
                                                        Slog.e("WindowManager", "Exception thrown while waiting for touch focus transfer", e);
                                                        z = false;
                                                    }
                                                    WindowManagerGlobalLock windowManagerGlobalLock2 = this.mService.mGlobalLock;
                                                    WindowManagerService.boostPriorityForLockedSection();
                                                    synchronized (windowManagerGlobalLock2) {
                                                        try {
                                                            if (!z) {
                                                                Slog.e("WindowManager", "Unable to transfer touch focus");
                                                                this.mDragState.closeLocked();
                                                                WindowManagerService.resetPriorityAfterLockedSection();
                                                                this.mCallback.get().postPerformDrag();
                                                                return null;
                                                            }
                                                            DragState dragState2 = this.mDragState;
                                                            SurfaceControl surfaceControl3 = dragState2.mSurfaceControl;
                                                            dragState2.broadcastDragStartedLocked(f, f2);
                                                            this.mDragState.overridePointerIconLocked(i4);
                                                            DragState dragState3 = this.mDragState;
                                                            dragState3.mThumbOffsetX = f3;
                                                            dragState3.mThumbOffsetY = f4;
                                                            if (WindowManagerDebugConfig.SHOW_LIGHT_TRANSACTIONS) {
                                                                Slog.i("WindowManager", ">>> OPEN TRANSACTION performDrag");
                                                            }
                                                            DragState dragState4 = this.mDragState;
                                                            SurfaceControl.Transaction transaction = dragState4.mTransaction;
                                                            transaction.setAlpha(surfaceControl3, dragState4.mOriginalAlpha);
                                                            transaction.show(surfaceControl3);
                                                            displayContent.reparentToOverlay(transaction, surfaceControl3);
                                                            this.mDragState.updateDragSurfaceLocked(true, f, f2);
                                                            if (WindowManagerDebugConfig.SHOW_LIGHT_TRANSACTIONS) {
                                                                Slog.i("WindowManager", "<<< CLOSE TRANSACTION performDrag");
                                                            }
                                                            WindowManagerService.resetPriorityAfterLockedSection();
                                                            this.mDragDropControllerExt.postPerSuccessformDrag(iWindow, this.mDragState.mSurfaceControl, i4, new float[]{f, f2, f3, f4}, clipData);
                                                        } catch (Throwable th) {
                                                            WindowManagerService.resetPriorityAfterLockedSection();
                                                            throw th;
                                                        }
                                                    }
                                                } else {
                                                    dragState.broadcastDragStartedLocked(f, f2);
                                                    sendTimeoutMessage(0, windowForClientLocked.mClient.asBinder(), getAccessibilityManager().getRecommendedTimeoutMillis(60000, 4));
                                                    WindowManagerService.resetPriorityAfterLockedSection();
                                                }
                                                return binder;
                                            } catch (Throwable th2) {
                                                th = th2;
                                                surfaceControl2 = null;
                                                if (surfaceControl2 != null) {
                                                    surfaceControl2.release();
                                                }
                                                throw th;
                                            }
                                        }
                                        Slog.w("WindowManager", "display content is null");
                                        if (surfaceControl != null) {
                                            surfaceControl.release();
                                        }
                                    }
                                    Slog.w("WindowManager", "Bad requesting window " + iWindow);
                                    if (surfaceControl != null) {
                                        surfaceControl.release();
                                    }
                                    WindowManagerService.resetPriorityAfterLockedSection();
                                    this.mCallback.get().postPerformDrag();
                                    return null;
                                }
                                Slog.w("WindowManager", "Drag already in progress");
                                if (surfaceControl != null) {
                                    surfaceControl.release();
                                }
                            } catch (Throwable th3) {
                                th = th3;
                                surfaceControl2 = surfaceControl;
                            }
                        } else {
                            Slog.w("WindowManager", "IDragDropCallback rejects the performDrag request");
                            if (surfaceControl != null) {
                                surfaceControl.release();
                            }
                        }
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return null;
                    } catch (Throwable th4) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        throw th4;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    surfaceControl2 = surfaceControl;
                }
            }
        } finally {
            this.mCallback.get().postPerformDrag();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Finally extract failed */
    public void reportDropResult(IWindow iWindow, boolean z) {
        IBinder asBinder = iWindow.asBinder();
        if (WindowManagerDebugConfig.DEBUG_DRAG) {
            Slog.d("WindowManager", "Drop result=" + z + " reported by " + asBinder);
        }
        this.mCallback.get().preReportDropResult(iWindow, z);
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DragState dragState = this.mDragState;
                    if (dragState == null) {
                        Slog.w("WindowManager", "Drop result given but no drag in progress");
                    } else {
                        if (dragState.mToken != asBinder) {
                            Slog.w("WindowManager", "Invalid drop-result claim by " + iWindow);
                            throw new IllegalStateException("reportDropResult() by non-recipient");
                        }
                        boolean z2 = false;
                        this.mHandler.removeMessages(0, iWindow.asBinder());
                        WindowState windowForClientLocked = this.mService.windowForClientLocked((Session) null, iWindow, false);
                        if (windowForClientLocked == null) {
                            Slog.w("WindowManager", "Bad result-reporting window " + iWindow);
                            this.mDragState.endDragLocked();
                        } else {
                            this.mDragState.mDragResult = z;
                            boolean consumedResult = this.mDragDropControllerExt.getConsumedResult();
                            if (consumedResult != z) {
                                this.mDragState.mDragResult = consumedResult;
                            }
                            DragState dragState2 = this.mDragState;
                            if (z && dragState2.targetInterceptsGlobalDrag(windowForClientLocked)) {
                                z2 = true;
                            }
                            dragState2.mRelinquishDragSurfaceToDropTarget = z2;
                            this.mDragState.endDragLocked();
                            WindowManagerService.resetPriorityAfterLockedSection();
                            return;
                        }
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            this.mCallback.get().postReportDropResult();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cancelDragAndDrop(IBinder iBinder, boolean z) {
        if (WindowManagerDebugConfig.DEBUG_DRAG) {
            Slog.d("WindowManager", "cancelDragAndDrop");
        }
        this.mCallback.get().preCancelDragAndDrop(iBinder);
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DragState dragState = this.mDragState;
                    if (dragState == null) {
                        Slog.w("WindowManager", "cancelDragAndDrop() without prepareDrag()");
                        throw new IllegalStateException("cancelDragAndDrop() without prepareDrag()");
                    }
                    if (dragState.mToken != iBinder) {
                        Slog.w("WindowManager", "cancelDragAndDrop() does not match prepareDrag()");
                        throw new IllegalStateException("cancelDragAndDrop() does not match prepareDrag()");
                    }
                    dragState.mDragResult = false;
                    dragState.cancelDragLocked(z);
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            this.mCallback.get().postCancelDragAndDrop();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleMotionEvent(boolean z, float f, float f2) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (!dragDropActiveLocked()) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                } else {
                    this.mDragState.updateDragSurfaceLocked(z, f, f2);
                    WindowManagerService.resetPriorityAfterLockedSection();
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dragRecipientEntered(IWindow iWindow) {
        if (WindowManagerDebugConfig.DEBUG_DRAG) {
            Slog.d("WindowManager", "Drag into new candidate view @ " + iWindow.asBinder());
        }
        this.mCallback.get().dragRecipientEntered(iWindow);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dragRecipientExited(IWindow iWindow) {
        if (WindowManagerDebugConfig.DEBUG_DRAG) {
            Slog.d("WindowManager", "Drag from old candidate view @ " + iWindow.asBinder());
        }
        this.mCallback.get().dragRecipientExited(iWindow);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendHandlerMessage(int i, Object obj) {
        this.mHandler.obtainMessage(i, obj).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendTimeoutMessage(int i, Object obj, long j) {
        this.mHandler.removeMessages(i, obj);
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(i, obj), j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDragStateClosedLocked(DragState dragState) {
        if (this.mDragState != dragState) {
            Slog.wtf("WindowManager", "Unknown drag state is closed");
        } else {
            this.mDragState = null;
            this.mDragDropControllerExt.postEndDrag();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportDropWindow(IBinder iBinder, float f, float f2) {
        if (this.mDragState == null) {
            Slog.w("WindowManager", "Drag state is closed.");
            return;
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DragState dragState = this.mDragState;
                if (dragState != null) {
                    dragState.reportDropWindowLock(iBinder, f, f2);
                } else {
                    Slog.w("WindowManager", "reportDropWindow mDragState is null!");
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean dropForAccessibility(IWindow iWindow, float f, float f2) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                boolean isEnabled = getAccessibilityManager().isEnabled();
                if (!dragDropActiveLocked()) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                }
                if (!this.mDragState.isAccessibilityDragDrop() || !isEnabled) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                }
                WindowState windowForClientLocked = this.mService.windowForClientLocked((Session) null, iWindow, false);
                if (!this.mDragState.isWindowNotified(windowForClientLocked)) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                }
                boolean reportDropWindowLock = this.mDragState.reportDropWindowLock(windowForClientLocked.mInputChannelToken, f, f2);
                WindowManagerService.resetPriorityAfterLockedSection();
                return reportDropWindowLock;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    AccessibilityManager getAccessibilityManager() {
        return (AccessibilityManager) this.mService.mContext.getSystemService("accessibility");
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private class DragHandler extends Handler {
        private final WindowManagerService mService;

        DragHandler(WindowManagerService windowManagerService, Looper looper) {
            super(looper);
            this.mService = windowManagerService;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                IBinder iBinder = (IBinder) message.obj;
                if (WindowManagerDebugConfig.DEBUG_DRAG) {
                    Slog.w("WindowManager", "Timeout ending drag to win " + iBinder);
                }
                WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
                WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        if (DragDropController.this.mDragState != null) {
                            DragDropController.this.mDragState.mDragResult = false;
                            DragDropController.this.mDragState.endDragLocked();
                        }
                    } finally {
                        WindowManagerService.resetPriorityAfterLockedSection();
                    }
                }
                WindowManagerService.resetPriorityAfterLockedSection();
                return;
            }
            if (i == 1) {
                if (WindowManagerDebugConfig.DEBUG_DRAG) {
                    Slog.d("WindowManager", "Drag ending; tearing down input channel");
                }
                DragState.InputInterceptor inputInterceptor = (DragState.InputInterceptor) message.obj;
                if (inputInterceptor == null) {
                    return;
                }
                WindowManagerGlobalLock windowManagerGlobalLock2 = this.mService.mGlobalLock;
                WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock2) {
                    try {
                        inputInterceptor.tearDown();
                    } finally {
                        WindowManagerService.resetPriorityAfterLockedSection();
                    }
                }
                WindowManagerService.resetPriorityAfterLockedSection();
                return;
            }
            if (i != 2) {
                if (i != 3) {
                    return;
                }
                WindowManagerGlobalLock windowManagerGlobalLock3 = this.mService.mGlobalLock;
                WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock3) {
                    try {
                        this.mService.mTransactionFactory.get().reparent((SurfaceControl) message.obj, null).apply();
                    } finally {
                    }
                }
                WindowManagerService.resetPriorityAfterLockedSection();
                return;
            }
            WindowManagerGlobalLock windowManagerGlobalLock4 = this.mService.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock4) {
                try {
                    if (DragDropController.this.mDragState == null) {
                        Slog.wtf("WindowManager", "mDragState unexpectedly became null while playing animation");
                        return;
                    }
                    if (!DragDropController.this.mDragState.isClosing() && (DragDropController.this.mDragState.mAnimator == null || DragDropController.this.mDragState.mAnimationCompleted)) {
                        DragDropController.this.mDragState.closeLocked();
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    if (WindowManagerDebugConfig.DEBUG_DRAG) {
                        Slog.w("WindowManager", "If mDragState is closing or mAnimator is not completed, return ");
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                } finally {
                    WindowManagerService.resetPriorityAfterLockedSection();
                }
            }
        }
    }
}
