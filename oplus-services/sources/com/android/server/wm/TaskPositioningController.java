package com.android.server.wm;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Slog;
import android.view.Display;
import android.view.IWindow;
import android.view.InputWindowHandle;
import android.view.SurfaceControl;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class TaskPositioningController {
    private SurfaceControl mInputSurface;
    private DisplayContent mPositioningDisplay;
    private final WindowManagerService mService;
    private TaskPositioner mTaskPositioner;
    final SurfaceControl.Transaction mTransaction;
    private final Rect mTmpClipRect = new Rect();
    private ITaskPositioningControllerExt mTaskPositioningControllerExt = (ITaskPositioningControllerExt) ExtLoader.type(ITaskPositioningControllerExt.class).base(this).create();

    boolean isPositioningLocked() {
        return this.mTaskPositioner != null;
    }

    InputWindowHandle getDragWindowHandleLocked() {
        TaskPositioner taskPositioner = this.mTaskPositioner;
        if (taskPositioner != null) {
            return taskPositioner.mDragWindowHandle;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TaskPositioningController(WindowManagerService windowManagerService) {
        this.mService = windowManagerService;
        this.mTransaction = windowManagerService.mTransactionFactory.get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void hideInputSurface(int i) {
        SurfaceControl surfaceControl;
        DisplayContent displayContent = this.mPositioningDisplay;
        if (displayContent == null || displayContent.getDisplayId() != i || (surfaceControl = this.mInputSurface) == null) {
            return;
        }
        this.mTransaction.hide(surfaceControl).apply();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CompletableFuture<Void> showInputSurface(int i) {
        DisplayContent displayContent = this.mPositioningDisplay;
        if (displayContent == null || displayContent.getDisplayId() != i) {
            return CompletableFuture.completedFuture(null);
        }
        DisplayContent displayContent2 = this.mService.mRoot.getDisplayContent(i);
        if (this.mInputSurface == null) {
            this.mInputSurface = this.mService.makeSurfaceBuilder(displayContent2.getSession()).setContainerLayer().setName("Drag and Drop Input Consumer").setCallsite("TaskPositioningController.showInputSurface").setParent(displayContent2.getOverlayLayer()).build();
        }
        InputWindowHandle dragWindowHandleLocked = getDragWindowHandleLocked();
        if (dragWindowHandleLocked == null) {
            Slog.w("WindowManager", "Drag is in progress but there is no drag window handle.");
            return CompletableFuture.completedFuture(null);
        }
        Display display = displayContent2.getDisplay();
        Point point = new Point();
        display.getRealSize(point);
        this.mTmpClipRect.set(0, 0, point.x, point.y);
        final CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        this.mTransaction.show(this.mInputSurface).setInputWindowInfo(this.mInputSurface, dragWindowHandleLocked).setLayer(this.mInputSurface, Integer.MAX_VALUE).setPosition(this.mInputSurface, 0.0f, 0.0f).setCrop(this.mInputSurface, this.mTmpClipRect).addWindowInfosReportedListener(new Runnable() { // from class: com.android.server.wm.TaskPositioningController$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                completableFuture.complete(null);
            }
        }).apply();
        return completableFuture;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean startMovingTask(IWindow iWindow, float f, float f2) {
        WindowState windowForClientLocked;
        CompletableFuture<Boolean> startPositioningLocked;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                windowForClientLocked = this.mService.windowForClientLocked((Session) null, iWindow, false);
                startPositioningLocked = startPositioningLocked(windowForClientLocked, false, false, f, f2);
            } finally {
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        try {
            if (!startPositioningLocked.get().booleanValue()) {
                return false;
            }
            WindowManagerGlobalLock windowManagerGlobalLock2 = this.mService.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock2) {
                try {
                    this.mService.mAtmService.setFocusedTask(windowForClientLocked.getTask().mTaskId);
                } finally {
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
            return true;
        } catch (Exception e) {
            Slog.e("WindowManager", "Exception thrown while waiting for startPositionLocked future", e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleTapOutsideTask(final DisplayContent displayContent, final int i, final int i2) {
        this.mService.mH.post(new Runnable() { // from class: com.android.server.wm.TaskPositioningController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                TaskPositioningController.this.lambda$handleTapOutsideTask$1(displayContent, i, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleTapOutsideTask$1(DisplayContent displayContent, int i, int i2) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                Task findTaskForResizePoint = displayContent.findTaskForResizePoint(i, i2);
                if (findTaskForResizePoint != null && findTaskForResizePoint.isResizeable()) {
                    CompletableFuture<Boolean> startPositioningLocked = startPositioningLocked(findTaskForResizePoint.getTopVisibleAppMainWindow(), true, findTaskForResizePoint.preserveOrientationOnResize(), i, i2);
                    try {
                        if (startPositioningLocked.get().booleanValue()) {
                            WindowManagerGlobalLock windowManagerGlobalLock2 = this.mService.mGlobalLock;
                            WindowManagerService.boostPriorityForLockedSection();
                            synchronized (windowManagerGlobalLock2) {
                                try {
                                    this.mService.mAtmService.setFocusedTask(findTaskForResizePoint.mTaskId);
                                } finally {
                                }
                            }
                            WindowManagerService.resetPriorityAfterLockedSection();
                            return;
                        }
                        return;
                    } catch (Exception e) {
                        Slog.e("WindowManager", "Exception thrown while waiting for startPositionLocked future", e);
                        return;
                    }
                }
                WindowManagerService.resetPriorityAfterLockedSection();
            } finally {
                WindowManagerService.resetPriorityAfterLockedSection();
            }
        }
    }

    private CompletableFuture<Boolean> startPositioningLocked(final WindowState windowState, final boolean z, final boolean z2, final float f, final float f2) {
        if (WindowManagerDebugConfig.DEBUG_TASK_POSITIONING) {
            Slog.d("WindowManager", "startPositioningLocked: win=" + windowState + ", resize=" + z + ", preserveOrientation=" + z2 + ", {" + f + ", " + f2 + "}");
        }
        if (windowState == null || windowState.mActivityRecord == null) {
            Slog.w("WindowManager", "startPositioningLocked: Bad window " + windowState);
            return CompletableFuture.completedFuture(Boolean.FALSE);
        }
        if (windowState.mInputChannel == null) {
            Slog.wtf("WindowManager", "startPositioningLocked: " + windowState + " has no input channel,  probably being removed");
            return CompletableFuture.completedFuture(Boolean.FALSE);
        }
        final DisplayContent displayContent = windowState.getDisplayContent();
        if (displayContent == null) {
            Slog.w("WindowManager", "startPositioningLocked: Invalid display content " + windowState);
            return CompletableFuture.completedFuture(Boolean.FALSE);
        }
        this.mPositioningDisplay = displayContent;
        TaskPositioner create = TaskPositioner.create(this.mService);
        this.mTaskPositioner = create;
        return create.register(displayContent, windowState).thenApply(new Function() { // from class: com.android.server.wm.TaskPositioningController$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Boolean lambda$startPositioningLocked$2;
                lambda$startPositioningLocked$2 = TaskPositioningController.this.lambda$startPositioningLocked$2(windowState, displayContent, z, z2, f, f2, (Void) obj);
                return lambda$startPositioningLocked$2;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$startPositioningLocked$2(WindowState windowState, DisplayContent displayContent, boolean z, boolean z2, float f, float f2, Void r9) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                WindowState windowState2 = displayContent.mCurrentFocus;
                if (windowState2 != null && windowState2 != windowState && windowState2.mActivityRecord == windowState.mActivityRecord) {
                    windowState = windowState2;
                }
                if (!this.mService.mInputManager.transferTouchFocus(windowState.mInputChannel, this.mTaskPositioner.mClientChannel, false)) {
                    Slog.e("WindowManager", "startPositioningLocked: Unable to transfer touch focus");
                    cleanUpTaskPositioner();
                    Boolean bool = Boolean.FALSE;
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return bool;
                }
                this.mTaskPositioner.startDrag(z, z2, f, f2);
                Boolean bool2 = Boolean.TRUE;
                WindowManagerService.resetPriorityAfterLockedSection();
                return bool2;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void finishTaskPositioning(IWindow iWindow) {
        TaskPositioner taskPositioner = this.mTaskPositioner;
        if (taskPositioner == null || taskPositioner.mClientCallback != iWindow.asBinder()) {
            return;
        }
        finishTaskPositioning();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishTaskPositioning() {
        this.mService.mAnimationHandler.post(new Runnable() { // from class: com.android.server.wm.TaskPositioningController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                TaskPositioningController.this.lambda$finishTaskPositioning$3();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finishTaskPositioning$3() {
        if (WindowManagerDebugConfig.DEBUG_TASK_POSITIONING) {
            Slog.d("WindowManager", "finishPositioning");
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                cleanUpTaskPositioner();
                this.mPositioningDisplay = null;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    private void cleanUpTaskPositioner() {
        TaskPositioner taskPositioner = this.mTaskPositioner;
        if (taskPositioner == null) {
            return;
        }
        this.mTaskPositioner = null;
        taskPositioner.unregister();
    }
}
