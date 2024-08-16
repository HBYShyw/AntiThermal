package com.android.server.wm;

import android.os.Debug;
import android.util.Slog;
import java.io.PrintWriter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class WindowSurfacePlacer {
    static final int SET_UPDATE_ROTATION = 1;
    static final int SET_WALLPAPER_ACTION_PENDING = 2;
    private static final String TAG = "WindowManager";
    private int mDeferredRequests;
    private int mLayoutRepeatCount;
    private final WindowManagerService mService;
    private boolean mTraversalScheduled;
    private boolean mInLayout = false;
    private int mDeferDepth = 0;
    private final Traverser mPerformSurfacePlacement = new Traverser();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class Traverser implements Runnable {
        private Traverser() {
        }

        @Override // java.lang.Runnable
        public void run() {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowSurfacePlacer.this.mService.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowSurfacePlacer.this.performSurfacePlacement();
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowSurfacePlacer(WindowManagerService windowManagerService) {
        this.mService = windowManagerService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void deferLayout() {
        this.mDeferDepth++;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void continueLayout(boolean z) {
        int i = this.mDeferDepth - 1;
        this.mDeferDepth = i;
        if (i > 0) {
            return;
        }
        if (z || this.mDeferredRequests > 0) {
            if (WindowManagerDebugConfig.DEBUG) {
                Slog.i(TAG, "continueLayout hasChanges=" + z + " deferredRequests=" + this.mDeferredRequests + " " + Debug.getCallers(2, 3));
            }
            performSurfacePlacement();
            this.mDeferredRequests = 0;
            return;
        }
        if (WindowManagerDebugConfig.DEBUG) {
            Slog.i(TAG, "Cancel continueLayout " + Debug.getCallers(2, 3));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isLayoutDeferred() {
        return this.mDeferDepth > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void performSurfacePlacementIfScheduled() {
        if (this.mTraversalScheduled) {
            performSurfacePlacement();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void performSurfacePlacement() {
        performSurfacePlacement(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void performSurfacePlacement(boolean z) {
        if (this.mDeferDepth > 0 && !z) {
            this.mDeferredRequests++;
            return;
        }
        int i = 6;
        do {
            this.mTraversalScheduled = false;
            performSurfacePlacementLoop();
            this.mService.mAnimationHandler.removeCallbacks(this.mPerformSurfacePlacement);
            i--;
            if (!this.mTraversalScheduled) {
                break;
            }
        } while (i > 0);
        this.mService.mRoot.mWallpaperActionPending = false;
    }

    private void performSurfacePlacementLoop() {
        if (this.mInLayout) {
            if (WindowManagerDebugConfig.DEBUG) {
                throw new RuntimeException("Recursive call!");
            }
            Slog.w(TAG, "performLayoutAndPlaceSurfacesLocked called while in layout. Callers=" + Debug.getCallers(3));
            return;
        }
        DisplayContent defaultDisplayContentLocked = this.mService.getDefaultDisplayContentLocked();
        if (defaultDisplayContentLocked == null || defaultDisplayContentLocked.mWaitingForConfig) {
            return;
        }
        WindowManagerService windowManagerService = this.mService;
        if (windowManagerService.mDisplayReady) {
            this.mInLayout = true;
            if (!windowManagerService.mForceRemoves.isEmpty()) {
                while (!this.mService.mForceRemoves.isEmpty()) {
                    WindowState remove = this.mService.mForceRemoves.remove(0);
                    Slog.i(TAG, "Force removing: " + remove);
                    remove.removeImmediately();
                }
                Slog.w(TAG, "Due to memory failure, waiting a bit for next layout");
                Object obj = new Object();
                synchronized (obj) {
                    try {
                        obj.wait(250L);
                    } catch (InterruptedException unused) {
                    }
                }
            }
            try {
                this.mService.mRoot.performSurfacePlacement();
                this.mInLayout = false;
                if (this.mService.mRoot.isLayoutNeeded()) {
                    int i = this.mLayoutRepeatCount + 1;
                    this.mLayoutRepeatCount = i;
                    if (i < 6) {
                        requestTraversal();
                    } else {
                        Slog.e(TAG, "Performed 6 layouts in a row. Skipping");
                        this.mLayoutRepeatCount = 0;
                    }
                } else {
                    this.mLayoutRepeatCount = 0;
                }
                WindowManagerService windowManagerService2 = this.mService;
                if (!windowManagerService2.mWindowsChanged || windowManagerService2.mWindowChangeListeners.isEmpty()) {
                    return;
                }
                this.mService.mH.removeMessages(19);
                this.mService.mH.sendEmptyMessage(19);
            } catch (RuntimeException e) {
                this.mInLayout = false;
                Slog.wtf(TAG, "Unhandled exception while laying out windows", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void debugLayoutRepeats(String str, int i) {
        if (this.mLayoutRepeatCount >= 4) {
            Slog.v(TAG, "Layouts looping: " + str + ", mPendingLayoutChanges = 0x" + Integer.toHexString(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isInLayout() {
        return this.mInLayout;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void requestTraversal() {
        if (this.mTraversalScheduled) {
            return;
        }
        this.mTraversalScheduled = true;
        if (this.mDeferDepth > 0) {
            this.mDeferredRequests++;
            if (WindowManagerDebugConfig.DEBUG) {
                Slog.i(TAG, "Defer requestTraversal " + Debug.getCallers(3));
                return;
            }
            return;
        }
        this.mService.mAnimationHandler.post(this.mPerformSurfacePlacement);
    }

    public void dump(PrintWriter printWriter, String str) {
        printWriter.println(str + "mTraversalScheduled=" + this.mTraversalScheduled);
        printWriter.println(str + "mDeferDepth=" + this.mDeferDepth);
        printWriter.println(str + "mInLayout=" + this.mInLayout);
        printWriter.println(str + "mDisplayReady=" + this.mService.mDisplayReady);
        printWriter.println(str + "mDeferredRequests=" + this.mDeferredRequests);
    }
}
