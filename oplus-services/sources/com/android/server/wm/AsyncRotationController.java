package com.android.server.wm;

import android.R;
import android.os.Debug;
import android.os.HandlerExecutor;
import android.os.SystemProperties;
import android.util.ArrayMap;
import android.util.Slog;
import android.view.SurfaceControl;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.android.server.wm.BLASTSyncEngine;
import com.android.server.wm.SurfaceAnimator;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Set;
import java.util.function.Consumer;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class AsyncRotationController extends FadeAnimationController implements Consumer<WindowState> {
    private static final boolean DEBUG;
    private static final int DEBUG_DEPTH = 8;
    private static boolean DEBUG_PANIC = false;
    private static final int OP_APP_SWITCH = 1;
    private static final int OP_CHANGE = 2;
    private static final int OP_CHANGE_MAY_SEAMLESS = 3;
    private static final int OP_LEGACY = 0;
    private static final String TAG = "AsyncRotation";
    private static IAsyncRotationControllerExt sAsyncRotationControllerExt;
    private AsyncRotationControllerWrapper mARCWrapper;
    private boolean mAlwaysWaitForStartTransaction;
    private IAsyncRotationControllerSocExt mAsyncRotationControllerSocExt;
    private IFadeRotationAnimationControllerExt mExt;
    private final boolean mHasScreenRotationAnimation;
    private boolean mHideImmediately;
    private boolean mIsStartTransactionCommitted;
    private boolean mIsSyncDrawRequested;
    private WindowToken mNavBarToken;
    private Runnable mOnShowRunnable;
    private final int mOriginalRotation;
    private SeamlessRotator mRotator;
    private final WindowManagerService mService;
    private final ArrayMap<WindowToken, Operation> mTargetWindowTokens;
    private Runnable mTimeoutRunnable;
    private final int mTransitionOp;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    @interface TransitionOp {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$keepAppearanceInPreviousRotation$0(SurfaceControl.Transaction transaction) {
    }

    static {
        boolean z = SystemProperties.getBoolean("persist.sys.assert.panic", false);
        DEBUG_PANIC = z;
        DEBUG = z;
        sAsyncRotationControllerExt = (IAsyncRotationControllerExt) ExtLoader.type(IAsyncRotationControllerExt.class).create();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AsyncRotationController(DisplayContent displayContent) {
        super(displayContent);
        BLASTSyncEngine.SyncGroup syncSet;
        this.mTargetWindowTokens = new ArrayMap<>();
        this.mAsyncRotationControllerSocExt = (IAsyncRotationControllerSocExt) ExtLoader.type(IAsyncRotationControllerSocExt.class).base(this).create();
        this.mARCWrapper = new AsyncRotationControllerWrapper();
        this.mExt = (IFadeRotationAnimationControllerExt) ExtLoader.type(IFadeRotationAnimationControllerExt.class).base(this).create();
        this.mService = displayContent.mWmService;
        int rotation = displayContent.getWindowConfiguration().getRotation();
        this.mOriginalRotation = rotation;
        this.mAsyncRotationControllerSocExt.hookInitPerf();
        if (displayContent.mTransitionController.getCollectingTransitionType() == 6) {
            DisplayRotation displayRotation = displayContent.getDisplayRotation();
            WindowState topFullscreenOpaqueWindow = displayContent.getDisplayPolicy().getTopFullscreenOpaqueWindow();
            if (topFullscreenOpaqueWindow != null && topFullscreenOpaqueWindow.mAttrs.rotationAnimation == 3 && topFullscreenOpaqueWindow.getTask() != null && displayRotation.canRotateSeamlessly(rotation, displayRotation.getRotation())) {
                this.mTransitionOp = 3;
            } else {
                this.mTransitionOp = 2;
            }
        } else if (displayContent.mTransitionController.isShellTransitionsEnabled()) {
            this.mTransitionOp = 1;
        } else {
            this.mTransitionOp = 0;
        }
        boolean z = displayContent.getRotationAnimation() != null || this.mTransitionOp == 2;
        this.mHasScreenRotationAnimation = z;
        if (z) {
            this.mHideImmediately = true;
        }
        displayContent.forAllWindows((Consumer<WindowState>) this, true);
        if (this.mTransitionOp == 0) {
            this.mIsStartTransactionCommitted = true;
            return;
        }
        if (displayContent.mTransitionController.isCollecting(displayContent)) {
            Transition collectingTransition = this.mDisplayContent.mTransitionController.getCollectingTransition();
            if (collectingTransition != null && (syncSet = this.mDisplayContent.mWmService.mSyncEngine.getSyncSet(collectingTransition.getSyncId())) != null && syncSet.mSyncMethod == 1) {
                this.mAlwaysWaitForStartTransaction = true;
            }
            keepAppearanceInPreviousRotation();
        }
    }

    @Override // java.util.function.Consumer
    public void accept(WindowState windowState) {
        if (windowState.mHasSurface && canBeAsync(windowState.mToken)) {
            if (this.mTransitionOp == 0 && windowState.mForceSeamlesslyRotate) {
                return;
            }
            if (!this.mExt.hasSize(windowState)) {
                Slog.d(TAG, windowState + " size is zero");
                return;
            }
            if (!this.mExt.allowFadeRotationAnimation(windowState)) {
                Slog.d(TAG, windowState + " not allowFadeRotationAnimation");
                return;
            }
            int i = 1;
            if (windowState.mAttrs.type == 2019) {
                boolean navigationBarCanMove = this.mDisplayContent.getDisplayPolicy().navigationBarCanMove();
                int i2 = this.mTransitionOp;
                if (i2 == 0) {
                    this.mNavBarToken = windowState.mToken;
                    if (navigationBarCanMove) {
                        return;
                    }
                    RecentsAnimationController recentsAnimationController = this.mService.getRecentsAnimationController();
                    if (recentsAnimationController != null && recentsAnimationController.isNavigationBarAttachedToApp()) {
                        return;
                    }
                } else {
                    if (!navigationBarCanMove && i2 != 3) {
                        if (this.mDisplayContent.mTransitionController.mNavigationBarAttachedToApp) {
                            return;
                        }
                    }
                    this.mTargetWindowTokens.put(windowState.mToken, new Operation(i));
                    return;
                }
                i = 2;
                this.mTargetWindowTokens.put(windowState.mToken, new Operation(i));
                return;
            }
            if (this.mTransitionOp != 3 && !windowState.mForceSeamlesslyRotate) {
                i = 2;
            }
            this.mTargetWindowTokens.put(windowState.mToken, new Operation(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean canBeAsync(WindowToken windowToken) {
        int i = windowToken.windowType;
        return (i <= 99 || i == 2011 || i == 2013 || i == 2040 || !sAsyncRotationControllerExt.canBeAsync(windowToken)) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void keepAppearanceInPreviousRotation() {
        if (this.mIsSyncDrawRequested) {
            return;
        }
        for (int size = this.mTargetWindowTokens.size() - 1; size >= 0; size--) {
            if (!canDrawBeforeStartTransaction(this.mTargetWindowTokens.valueAt(size))) {
                WindowToken keyAt = this.mTargetWindowTokens.keyAt(size);
                for (int childCount = keyAt.getChildCount() - 1; childCount >= 0; childCount--) {
                    keyAt.getChildAt(childCount).applyWithNextDraw(new Consumer() { // from class: com.android.server.wm.AsyncRotationController$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            AsyncRotationController.lambda$keepAppearanceInPreviousRotation$0((SurfaceControl.Transaction) obj);
                        }
                    });
                    if (DEBUG) {
                        Slog.d(TAG, "Sync draw for " + keyAt.getChildAt(childCount));
                    }
                }
            }
        }
        this.mIsSyncDrawRequested = true;
        if (DEBUG) {
            Slog.d(TAG, "Requested to sync draw transaction");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateTargetWindows() {
        if (this.mTransitionOp == 0 || !this.mIsStartTransactionCommitted) {
            return;
        }
        for (int size = this.mTargetWindowTokens.size() - 1; size >= 0; size--) {
            Operation valueAt = this.mTargetWindowTokens.valueAt(size);
            if (!valueAt.mIsCompletionPending && valueAt.mAction != 1) {
                WindowToken keyAt = this.mTargetWindowTokens.keyAt(size);
                int childCount = keyAt.getChildCount();
                int i = 0;
                for (int i2 = childCount - 1; i2 >= 0; i2--) {
                    WindowState childAt = keyAt.getChildAt(i2);
                    if (childAt.isDrawn() || !childAt.mWinAnimator.getShown()) {
                        i++;
                    }
                }
                if (i == childCount) {
                    this.mDisplayContent.finishAsyncRotation(keyAt);
                }
            }
        }
    }

    private void finishOp(WindowToken windowToken) {
        SurfaceControl surfaceControl;
        Operation remove = this.mTargetWindowTokens.remove(windowToken);
        if (remove == null) {
            return;
        }
        boolean z = DEBUG;
        if (z) {
            Slog.d(TAG, " finishOp action=" + remove.mAction + ", leash=" + remove.mLeash + ", windowToken=" + windowToken + ", this=" + this + ", call by=" + Debug.getCallers(8));
        }
        if (remove.mDrawTransaction != null) {
            windowToken.getSyncTransaction().merge(remove.mDrawTransaction);
            remove.mDrawTransaction = null;
            if (z) {
                Slog.d(TAG, "finishOp merge transaction " + windowToken.getTopChild());
            }
        }
        int i = remove.mAction;
        if (i == 3) {
            if (z) {
                Slog.d(TAG, "finishOp fade-in IME " + windowToken.getTopChild());
            }
            fadeWindowToken(true, windowToken, 64, new SurfaceAnimator.OnAnimationFinishedCallback() { // from class: com.android.server.wm.AsyncRotationController$$ExternalSyntheticLambda1
                @Override // com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback
                public final void onAnimationFinished(int i2, AnimationAdapter animationAdapter) {
                    AsyncRotationController.this.lambda$finishOp$1(i2, animationAdapter);
                }
            });
            return;
        }
        if (i == 2) {
            if (z) {
                Slog.d(TAG, "finishOp fade-in " + windowToken.getTopChild());
            }
            fadeWindowToken(true, windowToken, 64);
            return;
        }
        if (i != 1 || this.mRotator == null || (surfaceControl = remove.mLeash) == null || !surfaceControl.isValid()) {
            return;
        }
        if (z) {
            Slog.d(TAG, "finishOp undo seamless " + windowToken.getTopChild());
        }
        this.mRotator.setIdentityMatrix(windowToken.getSyncTransaction(), remove.mLeash);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finishOp$1(int i, AnimationAdapter animationAdapter) {
        this.mDisplayContent.getInsetsStateController().getImeSourceProvider().reportImeDrawnForOrganizer();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void completeAll() {
        for (int size = this.mTargetWindowTokens.size() - 1; size >= 0; size--) {
            finishOp(this.mTargetWindowTokens.keyAt(size));
        }
        this.mTargetWindowTokens.clear();
        Runnable runnable = this.mTimeoutRunnable;
        if (runnable != null) {
            this.mService.mH.removeCallbacks(runnable);
        }
        Runnable runnable2 = this.mOnShowRunnable;
        if (runnable2 != null) {
            runnable2.run();
            this.mOnShowRunnable = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean completeRotation(WindowToken windowToken) {
        Operation operation;
        if (!this.mIsStartTransactionCommitted) {
            Operation operation2 = this.mTargetWindowTokens.get(windowToken);
            if (operation2 != null) {
                if (DEBUG) {
                    Slog.d(TAG, "Complete set pending " + windowToken.getTopChild());
                }
                operation2.mIsCompletionPending = true;
            }
            return false;
        }
        if (this.mTransitionOp == 1 && windowToken.mTransitionController.inTransition() && (operation = this.mTargetWindowTokens.get(windowToken)) != null && operation.mAction == 2) {
            if (DEBUG) {
                Slog.d(TAG, "Defer completion " + windowToken.getTopChild());
            }
            return false;
        }
        if (!isTargetToken(windowToken)) {
            return false;
        }
        if (this.mHasScreenRotationAnimation || this.mTransitionOp != 0) {
            if (DEBUG) {
                Slog.d(TAG, "Complete directly " + windowToken.getTopChild());
            }
            finishOp(windowToken);
            if (this.mTargetWindowTokens.isEmpty()) {
                this.mAsyncRotationControllerSocExt.hookPerfLockRelease();
                Runnable runnable = this.mTimeoutRunnable;
                if (runnable != null) {
                    this.mService.mH.removeCallbacks(runnable);
                }
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void start() {
        this.mAsyncRotationControllerSocExt.hookPerfHint();
        for (int size = this.mTargetWindowTokens.size() - 1; size >= 0; size--) {
            WindowToken keyAt = this.mTargetWindowTokens.keyAt(size);
            Operation valueAt = this.mTargetWindowTokens.valueAt(size);
            int i = valueAt.mAction;
            if (i == 2 || i == 3) {
                fadeWindowToken(false, keyAt, 64);
                valueAt.mLeash = keyAt.getAnimationLeash();
                if (DEBUG) {
                    Slog.d(TAG, "Start fade-out " + keyAt.getTopChild());
                }
            } else if (i == 1) {
                valueAt.mLeash = keyAt.mSurfaceControl;
                if (DEBUG) {
                    Slog.d(TAG, "Start seamless " + keyAt.getTopChild());
                }
            }
        }
        scheduleTimeout();
        if (DEBUG) {
            Slog.d(TAG, " start mTransitionOp=" + this.mTransitionOp + ", WaitForStartTransaction=" + this.mAlwaysWaitForStartTransaction + ", targetWindowTokens=" + this.mTargetWindowTokens + ", this=" + this + ", call by=" + Debug.getCallers(8));
        }
    }

    private void scheduleTimeout() {
        Runnable runnable = this.mTimeoutRunnable;
        if (runnable != null) {
            Slog.d(TAG, " don't scheduleTimeout again ,this=" + this);
            return;
        }
        if (runnable == null) {
            this.mTimeoutRunnable = new Runnable() { // from class: com.android.server.wm.AsyncRotationController$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    AsyncRotationController.this.lambda$scheduleTimeout$2();
                }
            };
        }
        this.mService.mH.postDelayed(this.mTimeoutRunnable, 2000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleTimeout$2() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("Async rotation timeout: ");
                sb.append(!this.mIsStartTransactionCommitted ? " start transaction is not committed" : this.mTargetWindowTokens);
                Slog.i(TAG, sb.toString());
                this.mIsStartTransactionCommitted = true;
                this.mDisplayContent.finishAsyncRotationIfPossible();
                this.mService.mWindowPlacerLocked.performSurfacePlacement();
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void hideImeImmediately() {
        WindowState windowState = this.mDisplayContent.mInputMethodWindow;
        if (windowState == null) {
            return;
        }
        WindowToken windowToken = windowState.mToken;
        if (isTargetToken(windowToken)) {
            return;
        }
        boolean z = this.mHideImmediately;
        this.mHideImmediately = true;
        Operation operation = new Operation(3);
        this.mTargetWindowTokens.put(windowToken, operation);
        fadeWindowToken(false, windowToken, 64);
        operation.mLeash = windowToken.getAnimationLeash();
        this.mHideImmediately = z;
        if (DEBUG) {
            Slog.d(TAG, "hideImeImmediately " + windowToken.getTopChild());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAsync(WindowState windowState) {
        WindowToken windowToken = windowState.mToken;
        return windowToken == this.mNavBarToken || (windowState.mForceSeamlesslyRotate && this.mTransitionOp == 0) || isTargetToken(windowToken);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isTargetToken(WindowToken windowToken) {
        return this.mTargetWindowTokens.containsKey(windowToken);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldFreezeInsetsPosition(WindowState windowState) {
        return TransitionController.SYNC_METHOD == 1 && this.mTransitionOp != 0 && !this.mIsStartTransactionCommitted && isTargetToken(windowState.mToken);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SurfaceControl.Transaction getDrawTransaction(WindowToken windowToken) {
        Operation operation;
        if (this.mTransitionOp == 0 || (operation = this.mTargetWindowTokens.get(windowToken)) == null) {
            return null;
        }
        if (operation.mDrawTransaction == null) {
            operation.mDrawTransaction = new SurfaceControl.Transaction();
        }
        return operation.mDrawTransaction;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOnShowRunnable(Runnable runnable) {
        this.mOnShowRunnable = runnable;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setupStartTransaction(SurfaceControl.Transaction transaction) {
        if (this.mIsStartTransactionCommitted) {
            return;
        }
        int size = this.mTargetWindowTokens.size();
        while (true) {
            size--;
            if (size < 0) {
                break;
            }
            Operation valueAt = this.mTargetWindowTokens.valueAt(size);
            SurfaceControl surfaceControl = valueAt.mLeash;
            if (surfaceControl != null && surfaceControl.isValid()) {
                if (this.mHasScreenRotationAnimation && valueAt.mAction == 2) {
                    transaction.setAlpha(surfaceControl, 0.0f);
                    if (DEBUG) {
                        Slog.d(TAG, "Setup alpha0 " + this.mTargetWindowTokens.keyAt(size).getTopChild());
                    }
                } else {
                    if (this.mRotator == null) {
                        this.mRotator = new SeamlessRotator(this.mOriginalRotation, this.mDisplayContent.getWindowConfiguration().getRotation(), this.mDisplayContent.getDisplayInfo(), false);
                    }
                    this.mRotator.applyTransform(transaction, surfaceControl);
                    if (DEBUG) {
                        Slog.d(TAG, "Setup unrotate " + this.mTargetWindowTokens.keyAt(size).getTopChild());
                    }
                }
            }
        }
        if (DEBUG) {
            Slog.d(TAG, " setupStartTransaction " + this + ", targetWindowTokens=" + this.mTargetWindowTokens + ", call by=" + Debug.getCallers(8));
        }
        transaction.addTransactionCommittedListener(new HandlerExecutor(this.mService.mH), new SurfaceControl.TransactionCommittedListener() { // from class: com.android.server.wm.AsyncRotationController$$ExternalSyntheticLambda3
            @Override // android.view.SurfaceControl.TransactionCommittedListener
            public final void onTransactionCommitted() {
                AsyncRotationController.this.lambda$setupStartTransaction$3();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupStartTransaction$3() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (DEBUG) {
                    Slog.d(TAG, "Start transaction is committed");
                }
                this.mIsStartTransactionCommitted = true;
                for (int size = this.mTargetWindowTokens.size() - 1; size >= 0; size--) {
                    if (this.mTargetWindowTokens.valueAt(size).mIsCompletionPending) {
                        if (DEBUG) {
                            Slog.d(TAG, "Continue pending completion " + this.mTargetWindowTokens.keyAt(size).getTopChild());
                        }
                        this.mDisplayContent.finishAsyncRotation(this.mTargetWindowTokens.keyAt(size));
                    }
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTransitionFinished() {
        if (this.mTransitionOp == 2) {
            return;
        }
        if (DEBUG || DEBUG_PANIC) {
            Slog.d(TAG, "onTransitionFinished " + this.mTargetWindowTokens);
        }
        for (int size = this.mTargetWindowTokens.size() - 1; size >= 0; size--) {
            WindowToken keyAt = this.mTargetWindowTokens.keyAt(size);
            if (keyAt.isVisible()) {
                int childCount = keyAt.getChildCount() - 1;
                while (true) {
                    if (childCount < 0) {
                        break;
                    }
                    if (keyAt.getChildAt(childCount).isDrawFinishedLw()) {
                        this.mDisplayContent.finishAsyncRotation(keyAt);
                        break;
                    }
                    childCount--;
                }
            } else {
                this.mDisplayContent.finishAsyncRotation(keyAt);
            }
        }
        if (this.mTargetWindowTokens.isEmpty()) {
            return;
        }
        scheduleTimeout();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean handleFinishDrawing(WindowState windowState, SurfaceControl.Transaction transaction) {
        Operation operation;
        if (this.mTransitionOp == 0 || (operation = this.mTargetWindowTokens.get(windowState.mToken)) == null) {
            return false;
        }
        if (DEBUG || DEBUG_PANIC) {
            Slog.d(TAG, "handleFinishDrawing " + windowState + ", postDrawTransaction=" + transaction + ", targetWindowTokens=" + this.mTargetWindowTokens + ", call by=" + Debug.getCallers(8));
        }
        if (transaction == null || !this.mIsSyncDrawRequested || canDrawBeforeStartTransaction(operation)) {
            this.mDisplayContent.finishAsyncRotation(windowState.mToken);
            return false;
        }
        SurfaceControl.Transaction transaction2 = operation.mDrawTransaction;
        if (transaction2 == null) {
            if (windowState.isClientLocal()) {
                SurfaceControl.Transaction transaction3 = this.mService.mTransactionFactory.get();
                operation.mDrawTransaction = transaction3;
                transaction3.merge(transaction);
            } else {
                operation.mDrawTransaction = transaction;
            }
        } else {
            transaction2.merge(transaction);
        }
        this.mDisplayContent.finishAsyncRotation(windowState.mToken);
        return true;
    }

    @Override // com.android.server.wm.FadeAnimationController
    public Animation getFadeInAnimation() {
        if (this.mHasScreenRotationAnimation) {
            return AnimationUtils.loadAnimation(this.mContext, R.anim.recent_enter);
        }
        return super.getFadeInAnimation();
    }

    @Override // com.android.server.wm.FadeAnimationController
    public Animation getFadeOutAnimation() {
        if (this.mHideImmediately) {
            float f = this.mTransitionOp == 2 ? 1.0f : 0.0f;
            return new AlphaAnimation(f, f);
        }
        return super.getFadeOutAnimation();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str) {
        WindowToken keyAt;
        String str2 = "  " + str;
        printWriter.println("AsyncRotationController =" + this);
        printWriter.println(str2 + "mTransitionOp =" + this.mTransitionOp);
        printWriter.println(str2 + "mIsStartTransactionCommitted =" + this.mIsStartTransactionCommitted);
        printWriter.println(str2 + "mAlwaysWaitForStartTransaction =" + this.mAlwaysWaitForStartTransaction);
        printWriter.println(str2 + "mIsSyncDrawRequested =" + this.mIsSyncDrawRequested);
        printWriter.println(str2 + "mHasScreenRotationAnimation =" + this.mHasScreenRotationAnimation);
        printWriter.println(str2 + "mRotator =" + this.mRotator);
        printWriter.println(str2 + "mTargetWindowTokens size =" + this.mTargetWindowTokens.size());
        for (int size = this.mTargetWindowTokens.size() + (-1); size >= 0; size--) {
            Operation valueAt = this.mTargetWindowTokens.valueAt(size);
            if (valueAt != null && (keyAt = this.mTargetWindowTokens.keyAt(size)) != null) {
                printWriter.println(str2 + "token=" + keyAt);
                printWriter.println(str2 + "  mAction=" + valueAt.mAction + ",mIsCompletionPending=" + valueAt.mIsCompletionPending);
                if (valueAt.mDrawTransaction != null && keyAt.getChildCount() > 0) {
                    printWriter.print(str2 + "  window =");
                    printWriter.println(keyAt.getTopChild());
                }
            }
        }
    }

    private boolean canDrawBeforeStartTransaction(Operation operation) {
        return (this.mAlwaysWaitForStartTransaction || operation.mAction == 1) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class Operation {
        static final int ACTION_FADE = 2;
        static final int ACTION_SEAMLESS = 1;
        static final int ACTION_TOGGLE_IME = 3;
        final int mAction;
        SurfaceControl.Transaction mDrawTransaction;
        boolean mIsCompletionPending;
        SurfaceControl mLeash;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        @interface Action {
        }

        Operation(int i) {
            this.mAction = i;
        }
    }

    public IAsyncRotationControllerWrapper getWrapper() {
        return this.mARCWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private class AsyncRotationControllerWrapper implements IAsyncRotationControllerWrapper {
        private AsyncRotationControllerWrapper() {
        }

        @Override // com.android.server.wm.IAsyncRotationControllerWrapper
        public Set<WindowToken> getTargetWindowTokens() {
            return AsyncRotationController.this.mTargetWindowTokens.keySet();
        }

        @Override // com.android.server.wm.IAsyncRotationControllerWrapper
        public String getAsyncRotationInfo() {
            StringBuilder sb = new StringBuilder();
            sb.append("AsyncRotationController{");
            sb.append(",mTransitionOp =" + AsyncRotationController.this.mTransitionOp);
            sb.append(",mIsStartTransactionCommitted =" + AsyncRotationController.this.mIsStartTransactionCommitted);
            sb.append(",mAlwaysWaitForStartTransaction =" + AsyncRotationController.this.mAlwaysWaitForStartTransaction);
            sb.append(",mIsSyncDrawRequested =" + AsyncRotationController.this.mIsSyncDrawRequested);
            sb.append(",mHasScreenRotationAnimation =" + AsyncRotationController.this.mHasScreenRotationAnimation);
            sb.append(",mTargetWindowTokens size =" + AsyncRotationController.this.mTargetWindowTokens.size());
            sb.append(",mTargetWindowTokens=" + AsyncRotationController.this.mTargetWindowTokens);
            sb.append("}");
            return sb.toString();
        }

        @Override // com.android.server.wm.IAsyncRotationControllerWrapper
        public void forceRemoveOp(WindowToken windowToken) {
            Operation operation = (Operation) AsyncRotationController.this.mTargetWindowTokens.remove(windowToken);
            if (operation == null || operation.mDrawTransaction == null) {
                return;
            }
            AsyncRotationController.this.mDisplayContent.getPendingTransaction().merge(operation.mDrawTransaction);
            operation.mDrawTransaction = null;
            Slog.d(AsyncRotationController.TAG, "forceRemoveOp merge transaction " + windowToken.getTopChild());
        }
    }
}
