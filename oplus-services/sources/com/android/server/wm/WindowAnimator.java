package com.android.server.wm;

import android.content.Context;
import android.os.Trace;
import android.util.Slog;
import android.util.TimeUtils;
import android.view.Choreographer;
import android.view.SurfaceControl;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.server.policy.WindowManagerPolicy;
import java.io.PrintWriter;
import java.util.ArrayList;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class WindowAnimator {
    private static final String TAG = "WindowManager";
    final Choreographer.FrameCallback mAnimationFrameCallback;
    private boolean mAnimationFrameCallbackScheduled;
    private Choreographer mChoreographer;
    final Context mContext;
    long mCurrentTime;
    private boolean mInExecuteAfterPrepareSurfacesRunnables;
    private boolean mLastRootAnimating;
    Object mLastWindowFreezeSource;
    final WindowManagerPolicy mPolicy;
    private boolean mRunningExpensiveAnimations;
    final WindowManagerService mService;
    private final SurfaceControl.Transaction mTransaction;
    int mBulkUpdateParams = 0;
    private IWindowAnimatorExt mWinAnimatorExt = (IWindowAnimatorExt) ExtLoader.type(IWindowAnimatorExt.class).base(this).create();
    private boolean mInitialized = false;
    boolean mNotifyWhenNoAnimation = false;
    private final ArrayList<Runnable> mAfterPrepareSurfacesRunnables = new ArrayList<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowAnimator(WindowManagerService windowManagerService) {
        this.mService = windowManagerService;
        this.mContext = windowManagerService.mContext;
        this.mPolicy = windowManagerService.mPolicy;
        this.mTransaction = windowManagerService.mTransactionFactory.get();
        windowManagerService.mAnimationHandler.runWithScissors(new Runnable() { // from class: com.android.server.wm.WindowAnimator$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                WindowAnimator.this.lambda$new$0();
            }
        }, 0L);
        this.mAnimationFrameCallback = new Choreographer.FrameCallback() { // from class: com.android.server.wm.WindowAnimator$$ExternalSyntheticLambda1
            @Override // android.view.Choreographer.FrameCallback
            public final void doFrame(long j) {
                WindowAnimator.this.lambda$new$1(j);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mChoreographer = Choreographer.getSfInstance();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(long j) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mAnimationFrameCallbackScheduled = false;
                animate(j);
                if (this.mNotifyWhenNoAnimation && !this.mLastRootAnimating) {
                    this.mService.mGlobalLock.notifyAll();
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void ready() {
        this.mInitialized = true;
    }

    private void animate(long j) {
        if (this.mInitialized) {
            scheduleAnimation();
            RootWindowContainer rootWindowContainer = this.mService.mRoot;
            this.mCurrentTime = j / 1000000;
            this.mBulkUpdateParams = 0;
            rootWindowContainer.mOrientationChangeComplete = true;
            if (WindowManagerDebugConfig.DEBUG_WINDOW_TRACE) {
                Slog.i(TAG, "!!! animate: entry time=" + this.mCurrentTime);
            }
            if (ProtoLogCache.WM_SHOW_TRANSACTIONS_enabled) {
                ProtoLogImpl.i(ProtoLogGroup.WM_SHOW_TRANSACTIONS, 1984782949, 0, (String) null, (Object[]) null);
            }
            this.mService.openSurfaceTransaction();
            try {
                rootWindowContainer.handleCompleteDeferredRemoval();
                AccessibilityController accessibilityController = this.mService.mAccessibilityController;
                int childCount = rootWindowContainer.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    DisplayContent childAt = rootWindowContainer.getChildAt(i);
                    childAt.updateWindowsForAnimator();
                    childAt.prepareSurfaces();
                }
                for (int i2 = 0; i2 < childCount; i2++) {
                    DisplayContent childAt2 = rootWindowContainer.getChildAt(i2);
                    childAt2.checkAppWindowsReadyToShow();
                    if (accessibilityController.hasCallbacks()) {
                        accessibilityController.drawMagnifiedRegionBorderIfNeeded(childAt2.mDisplayId);
                    }
                }
                cancelAnimation();
                Watermark watermark = this.mService.mWatermark;
                if (watermark != null) {
                    watermark.drawIfNeeded();
                }
                this.mWinAnimatorExt.animate();
            } catch (RuntimeException e) {
                Slog.wtf(TAG, "Unhandled exception in Window Manager", e);
            }
            boolean hasPendingLayoutChanges = rootWindowContainer.hasPendingLayoutChanges(this);
            boolean z = (this.mBulkUpdateParams != 0 || rootWindowContainer.mOrientationChangeComplete) && rootWindowContainer.copyAnimToLayoutParams();
            if (hasPendingLayoutChanges || z) {
                this.mService.mWindowPlacerLocked.requestTraversal();
            }
            boolean isAnimating = rootWindowContainer.isAnimating(5, -1);
            if (isAnimating && !this.mLastRootAnimating) {
                Trace.asyncTraceBegin(32L, "animating", 0);
            }
            if (!isAnimating && this.mLastRootAnimating) {
                this.mService.mWindowPlacerLocked.requestTraversal();
                Trace.asyncTraceEnd(32L, "animating", 0);
            }
            this.mLastRootAnimating = isAnimating;
            boolean isAnimating2 = rootWindowContainer.isAnimating(5, 11);
            if (isAnimating2 && !this.mRunningExpensiveAnimations) {
                this.mService.mSnapshotController.setPause(true);
                this.mTransaction.setEarlyWakeupStart();
            } else if (!isAnimating2 && this.mRunningExpensiveAnimations) {
                this.mService.mSnapshotController.setPause(false);
                this.mTransaction.setEarlyWakeupEnd();
            }
            this.mRunningExpensiveAnimations = isAnimating2;
            SurfaceControl.mergeToGlobalTransaction(this.mTransaction);
            this.mService.closeSurfaceTransaction("WindowAnimator");
            if (ProtoLogCache.WM_SHOW_TRANSACTIONS_enabled) {
                ProtoLogImpl.i(ProtoLogGroup.WM_SHOW_TRANSACTIONS, -545190927, 0, (String) null, (Object[]) null);
            }
            this.mService.mAtmService.mTaskOrganizerController.dispatchPendingEvents();
            executeAfterPrepareSurfacesRunnables();
            if (WindowManagerDebugConfig.DEBUG_WINDOW_TRACE) {
                Slog.i(TAG, "!!! animate: exit mBulkUpdateParams=" + Integer.toHexString(this.mBulkUpdateParams) + " hasPendingLayoutChanges=" + hasPendingLayoutChanges);
            }
        }
    }

    private static String bulkUpdateParamsToString(int i) {
        StringBuilder sb = new StringBuilder(128);
        if ((i & 1) != 0) {
            sb.append(" UPDATE_ROTATION");
        }
        if ((i & 2) != 0) {
            sb.append(" SET_WALLPAPER_ACTION_PENDING");
        }
        return sb.toString();
    }

    public void dumpLocked(PrintWriter printWriter, String str, boolean z) {
        String str2 = "  " + str;
        for (int i = 0; i < this.mService.mRoot.getChildCount(); i++) {
            DisplayContent childAt = this.mService.mRoot.getChildAt(i);
            printWriter.print(str);
            printWriter.print(childAt);
            printWriter.println(":");
            childAt.dumpWindowAnimators(printWriter, str2);
            printWriter.println();
        }
        printWriter.println();
        if (z) {
            printWriter.print(str);
            printWriter.print("mCurrentTime=");
            printWriter.println(TimeUtils.formatUptime(this.mCurrentTime));
        }
        if (this.mBulkUpdateParams != 0) {
            printWriter.print(str);
            printWriter.print("mBulkUpdateParams=0x");
            printWriter.print(Integer.toHexString(this.mBulkUpdateParams));
            printWriter.println(bulkUpdateParamsToString(this.mBulkUpdateParams));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleAnimation() {
        if (this.mAnimationFrameCallbackScheduled) {
            return;
        }
        this.mAnimationFrameCallbackScheduled = true;
        this.mChoreographer.postFrameCallback(this.mAnimationFrameCallback);
    }

    private void cancelAnimation() {
        if (this.mAnimationFrameCallbackScheduled) {
            this.mAnimationFrameCallbackScheduled = false;
            this.mChoreographer.removeFrameCallback(this.mAnimationFrameCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAnimationScheduled() {
        return this.mAnimationFrameCallbackScheduled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Choreographer getChoreographer() {
        return this.mChoreographer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addAfterPrepareSurfacesRunnable(Runnable runnable) {
        if (this.mInExecuteAfterPrepareSurfacesRunnables) {
            runnable.run();
        } else {
            this.mAfterPrepareSurfacesRunnables.add(runnable);
            scheduleAnimation();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void executeAfterPrepareSurfacesRunnables() {
        if (this.mInExecuteAfterPrepareSurfacesRunnables) {
            return;
        }
        this.mInExecuteAfterPrepareSurfacesRunnables = true;
        int size = this.mAfterPrepareSurfacesRunnables.size();
        for (int i = 0; i < size; i++) {
            this.mAfterPrepareSurfacesRunnables.get(i).run();
        }
        this.mAfterPrepareSurfacesRunnables.clear();
        this.mInExecuteAfterPrepareSurfacesRunnables = false;
    }
}
