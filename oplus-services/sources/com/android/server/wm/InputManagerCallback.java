package com.android.server.wm;

import android.os.Debug;
import android.os.IBinder;
import android.util.Slog;
import android.view.InputApplicationHandle;
import android.view.KeyEvent;
import android.view.SurfaceControl;
import com.android.internal.os.TimeoutRecord;
import com.android.internal.util.function.TriConsumer;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.server.input.InputManagerService;
import com.android.server.wm.WindowManagerService;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class InputManagerCallback implements InputManagerService.WindowManagerCallbacks {
    private static final String TAG = "WindowManager";
    private boolean mInputDevicesReady;
    private boolean mInputDispatchEnabled;
    private boolean mInputDispatchFrozen;
    private final WindowManagerService mService;
    private final Object mInputDevicesReadyMonitor = new Object();
    private String mInputFreezeReason = null;

    public InputManagerCallback(WindowManagerService windowManagerService) {
        this.mService = windowManagerService;
    }

    public void notifyInputChannelBroken(IBinder iBinder) {
        if (iBinder == null) {
            return;
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                WindowState windowState = this.mService.mInputToWindowMap.get(iBinder);
                if (windowState != null) {
                    Slog.i(TAG, "WINDOW DIED " + windowState);
                    windowState.removeIfPossible();
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void notifyNoFocusedWindowAnr(InputApplicationHandle inputApplicationHandle) {
        this.mService.mAnrController.notifyAppUnresponsive(inputApplicationHandle, TimeoutRecord.forInputDispatchNoFocusedWindow(timeoutMessage("Application does not have a focused window")));
    }

    public void notifyWindowUnresponsive(IBinder iBinder, OptionalInt optionalInt, String str) {
        this.mService.mAnrController.notifyWindowUnresponsive(iBinder, optionalInt, TimeoutRecord.forInputDispatchWindowUnresponsive(timeoutMessage(str)));
    }

    public void notifyWindowResponsive(IBinder iBinder, OptionalInt optionalInt) {
        this.mService.mAnrController.notifyWindowResponsive(iBinder, optionalInt);
    }

    public void notifyConfigurationChanged() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mService.mRoot.forAllDisplays(new Consumer() { // from class: com.android.server.wm.InputManagerCallback$$ExternalSyntheticLambda2
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ((DisplayContent) obj).sendNewConfiguration();
                    }
                });
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        synchronized (this.mInputDevicesReadyMonitor) {
            if (!this.mInputDevicesReady) {
                this.mInputDevicesReady = true;
                this.mInputDevicesReadyMonitor.notifyAll();
            }
        }
    }

    public void notifyLidSwitchChanged(long j, boolean z) {
        this.mService.mPolicy.notifyLidSwitchChanged(j, z);
    }

    public void notifyCameraLensCoverSwitchChanged(long j, boolean z) {
        this.mService.mPolicy.notifyCameraLensCoverSwitchChanged(j, z);
    }

    public int interceptKeyBeforeQueueing(KeyEvent keyEvent, int i) {
        return this.mService.mPolicy.interceptKeyBeforeQueueing(keyEvent, i);
    }

    public int interceptMotionBeforeQueueingNonInteractive(int i, long j, int i2) {
        return this.mService.mPolicy.interceptMotionBeforeQueueingNonInteractive(i, j, i2);
    }

    public long interceptKeyBeforeDispatching(IBinder iBinder, KeyEvent keyEvent, int i) {
        return this.mService.mPolicy.interceptKeyBeforeDispatching(iBinder, keyEvent, i);
    }

    public KeyEvent dispatchUnhandledKey(IBinder iBinder, KeyEvent keyEvent, int i) {
        return this.mService.mPolicy.dispatchUnhandledKey(iBinder, keyEvent, i);
    }

    public int getPointerLayer() {
        return (this.mService.mPolicy.getWindowLayerFromTypeLw(2018) * 10000) + 1000;
    }

    public int getPointerDisplayId() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                WindowManagerService windowManagerService = this.mService;
                int i = 0;
                if (!windowManagerService.mForceDesktopModeOnExternalDisplays) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return 0;
                }
                for (int size = windowManagerService.mRoot.mChildren.size() - 1; size >= 0; size--) {
                    DisplayContent displayContent = (DisplayContent) this.mService.mRoot.mChildren.get(size);
                    if (displayContent.getDisplayInfo().state != 1) {
                        if (displayContent.getWindowingMode() == 5) {
                            int displayId = displayContent.getDisplayId();
                            WindowManagerService.resetPriorityAfterLockedSection();
                            return displayId;
                        }
                        if (i == 0 && displayContent.getDisplayId() != 0) {
                            i = displayContent.getDisplayId();
                        }
                    }
                }
                WindowManagerService.resetPriorityAfterLockedSection();
                return i;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void onPointerDownOutsideFocus(IBinder iBinder) {
        this.mService.mH.obtainMessage(62, iBinder).sendToTarget();
    }

    public void notifyFocusChanged(IBinder iBinder, IBinder iBinder2) {
        final WindowManagerService windowManagerService = this.mService;
        WindowManagerService.H h = windowManagerService.mH;
        Objects.requireNonNull(windowManagerService);
        h.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.wm.InputManagerCallback$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                WindowManagerService.this.reportFocusChanged((IBinder) obj, (IBinder) obj2);
            }
        }, iBinder, iBinder2));
    }

    public void notifyDropWindow(IBinder iBinder, float f, float f2) {
        WindowManagerService windowManagerService = this.mService;
        WindowManagerService.H h = windowManagerService.mH;
        final DragDropController dragDropController = windowManagerService.mDragDropController;
        Objects.requireNonNull(dragDropController);
        h.sendMessage(PooledLambda.obtainMessage(new TriConsumer() { // from class: com.android.server.wm.InputManagerCallback$$ExternalSyntheticLambda1
            public final void accept(Object obj, Object obj2, Object obj3) {
                DragDropController.this.reportDropWindow((IBinder) obj, ((Float) obj2).floatValue(), ((Float) obj3).floatValue());
            }
        }, iBinder, Float.valueOf(f), Float.valueOf(f2)));
    }

    public SurfaceControl getParentSurfaceForPointers(int i) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mService.mRoot.getDisplayContent(i);
                if (displayContent == null) {
                    Slog.e(TAG, "Failed to get parent surface for pointers on display " + i + " - DisplayContent not found.");
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return null;
                }
                SurfaceControl overlayLayer = displayContent.getOverlayLayer();
                WindowManagerService.resetPriorityAfterLockedSection();
                return overlayLayer;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public boolean isAnimating() {
        boolean isAppTransitioning;
        boolean z;
        TransitionController transitionController = this.mService.mRoot.getDefaultDisplay().mTransitionController;
        if (transitionController.isShellTransitionsEnabled()) {
            z = transitionController.inTransition();
            isAppTransitioning = false;
        } else {
            boolean isRunning = this.mService.mRoot.getDefaultDisplay().mAppTransition.isRunning();
            isAppTransitioning = this.mService.mRoot.isAppTransitioning();
            z = isRunning;
        }
        Slog.d(TAG, "isAnimating: isTransitionRunning " + z + " isAppTransition: " + isAppTransitioning);
        return z || isAppTransitioning;
    }

    public SurfaceControl createSurfaceForGestureMonitor(String str, int i) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mService.mRoot.getDisplayContent(i);
                if (displayContent == null) {
                    Slog.e(TAG, "Failed to create a gesture monitor on display: " + i + " - DisplayContent not found.");
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return null;
                }
                SurfaceControl inputOverlayLayer = displayContent.getInputOverlayLayer();
                if (inputOverlayLayer == null) {
                    Slog.e(TAG, "Failed to create a gesture monitor on display: " + i + " - Input overlay layer is not initialized.");
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return null;
                }
                SurfaceControl build = this.mService.makeSurfaceBuilder(displayContent.getSession()).setContainerLayer().setName(str).setCallsite("createSurfaceForGestureMonitor").setParent(inputOverlayLayer).build();
                WindowManagerService.resetPriorityAfterLockedSection();
                return build;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void notifyPointerDisplayIdChanged(int i, float f, float f2) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mService.setMousePointerDisplayId(i);
                if (i == -1) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                DisplayContent displayContent = this.mService.mRoot.getDisplayContent(i);
                if (displayContent == null) {
                    Slog.wtf(TAG, "The mouse pointer was moved to display " + i + " that does not have a valid DisplayContent.");
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                this.mService.restorePointerIconLocked(displayContent, f, f2);
                WindowManagerService.resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public boolean waitForInputDevicesReady(long j) {
        boolean z;
        synchronized (this.mInputDevicesReadyMonitor) {
            if (!this.mInputDevicesReady) {
                try {
                    this.mInputDevicesReadyMonitor.wait(j);
                } catch (InterruptedException unused) {
                }
            }
            z = this.mInputDevicesReady;
        }
        return z;
    }

    public void freezeInputDispatchingLw() {
        if (this.mInputDispatchFrozen) {
            return;
        }
        if (WindowManagerDebugConfig.DEBUG_INPUT) {
            Slog.v(TAG, "Freezing input dispatching");
        }
        this.mInputDispatchFrozen = true;
        if (WindowManagerDebugConfig.DEBUG_INPUT) {
            this.mInputFreezeReason = Debug.getCallers(6);
        }
        updateInputDispatchModeLw();
    }

    public void thawInputDispatchingLw() {
        if (this.mInputDispatchFrozen) {
            if (WindowManagerDebugConfig.DEBUG_INPUT) {
                Slog.v(TAG, "Thawing input dispatching");
            }
            this.mInputDispatchFrozen = false;
            this.mInputFreezeReason = null;
            updateInputDispatchModeLw();
        }
    }

    public void setEventDispatchingLw(boolean z) {
        if (this.mInputDispatchEnabled != z) {
            if (WindowManagerDebugConfig.DEBUG_INPUT) {
                Slog.v(TAG, "Setting event dispatching to " + z);
            }
            this.mInputDispatchEnabled = z;
            updateInputDispatchModeLw();
        }
    }

    private void updateInputDispatchModeLw() {
        this.mService.mInputManager.setInputDispatchMode(this.mInputDispatchEnabled, this.mInputDispatchFrozen);
    }

    private String timeoutMessage(String str) {
        if (str == null) {
            return "Input dispatching timed out";
        }
        return "Input dispatching timed out (" + str + ")";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str) {
        if (this.mInputFreezeReason != null) {
            printWriter.println(str + "mInputFreezeReason=" + this.mInputFreezeReason);
        }
    }
}
