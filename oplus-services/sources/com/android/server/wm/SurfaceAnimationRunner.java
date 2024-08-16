package com.android.server.wm;

import android.animation.AnimationHandler;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Insets;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Debug;
import android.os.Handler;
import android.os.PowerManagerInternal;
import android.os.Trace;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Slog;
import android.view.Choreographer;
import android.view.IChoreographerExt;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.window.ScreenCapture;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.graphics.SfVsyncFrameCallbackProvider;
import com.android.server.AnimationThread;
import com.android.server.wm.LocalAnimationAdapter;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import system.ext.loader.core.ExtLoader;
import vendor.oplus.hardware.charger.ChargerErrorCode;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class SurfaceAnimationRunner {
    private static final long EDGEEXTENSION_TIME_OUT_DURATION = 5000;
    private static final String TAG = "SurfaceAnimationRunner";
    private final AnimationHandler mAnimationHandler;

    @GuardedBy({"mLock"})
    private boolean mAnimationStartDeferred;
    private final Handler mAnimationThreadHandler;
    private final AnimatorFactory mAnimatorFactory;
    private boolean mApplyScheduled;
    private final Runnable mApplyTransactionRunnable;
    private final Object mCancelLock;

    @VisibleForTesting
    Choreographer mChoreographer;
    private final ExecutorService mEdgeExtensionExecutor;
    private final Object mEdgeExtensionLock;

    @GuardedBy({"mEdgeExtensionLock"})
    private final ArrayMap<SurfaceControl, ArrayList<SurfaceControl>> mEdgeExtensions;
    private final SurfaceControl.Transaction mFrameTransaction;
    private final Object mLock;

    @GuardedBy({"mLock"})
    @VisibleForTesting
    final ArrayMap<SurfaceControl, RunningAnimation> mPendingAnimations;
    private final PowerManagerInternal mPowerManagerInternal;

    @GuardedBy({"mLock"})
    @VisibleForTesting
    final ArrayMap<SurfaceControl, RunningAnimation> mPreProcessingAnimations;

    @GuardedBy({"mLock"})
    @VisibleForTesting
    final ArrayMap<SurfaceControl, RunningAnimation> mRunningAnimations;
    private final Handler mSurfaceAnimationHandler;
    private ISurfaceAnimationRunnerExt mSurfaceAnimationRunnerExt;

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface AnimatorFactory {
        ValueAnimator makeAnimator();
    }

    public SurfaceAnimationRunner(Supplier<SurfaceControl.Transaction> supplier, PowerManagerInternal powerManagerInternal) {
        this(null, null, supplier.get(), powerManagerInternal);
    }

    @VisibleForTesting
    SurfaceAnimationRunner(AnimationHandler.AnimationFrameCallbackProvider animationFrameCallbackProvider, AnimatorFactory animatorFactory, SurfaceControl.Transaction transaction, PowerManagerInternal powerManagerInternal) {
        IChoreographerExt iChoreographerExt;
        this.mLock = new Object();
        this.mCancelLock = new Object();
        this.mEdgeExtensionLock = new Object();
        this.mAnimationThreadHandler = AnimationThread.getHandler();
        Handler handler = SurfaceAnimationThread.getHandler();
        this.mSurfaceAnimationHandler = handler;
        this.mApplyTransactionRunnable = new Runnable() { // from class: com.android.server.wm.SurfaceAnimationRunner$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                SurfaceAnimationRunner.this.applyTransaction();
            }
        };
        this.mEdgeExtensionExecutor = Executors.newFixedThreadPool(2);
        this.mPendingAnimations = new ArrayMap<>();
        this.mPreProcessingAnimations = new ArrayMap<>();
        this.mRunningAnimations = new ArrayMap<>();
        this.mEdgeExtensions = new ArrayMap<>();
        this.mSurfaceAnimationRunnerExt = (ISurfaceAnimationRunnerExt) ExtLoader.type(ISurfaceAnimationRunnerExt.class).base(this).create();
        handler.runWithScissors(new Runnable() { // from class: com.android.server.wm.SurfaceAnimationRunner$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                SurfaceAnimationRunner.this.lambda$new$0();
            }
        }, 0L);
        this.mFrameTransaction = transaction;
        AnimationHandler animationHandler = new AnimationHandler();
        this.mAnimationHandler = animationHandler;
        animationHandler.setProvider(animationFrameCallbackProvider == null ? new SfVsyncFrameCallbackProvider(this.mChoreographer) : animationFrameCallbackProvider);
        this.mAnimatorFactory = animatorFactory == null ? new AnimatorFactory() { // from class: com.android.server.wm.SurfaceAnimationRunner$$ExternalSyntheticLambda6
            @Override // com.android.server.wm.SurfaceAnimationRunner.AnimatorFactory
            public final ValueAnimator makeAnimator() {
                ValueAnimator lambda$new$1;
                lambda$new$1 = SurfaceAnimationRunner.this.lambda$new$1();
                return lambda$new$1;
            }
        } : animatorFactory;
        this.mPowerManagerInternal = powerManagerInternal;
        Choreographer choreographer = this.mChoreographer;
        if (choreographer == null || (iChoreographerExt = choreographer.mChoreographerExt) == null) {
            return;
        }
        iChoreographerExt.setIsSFChoregrapher(true);
    }

    public /* synthetic */ void lambda$new$0() {
        this.mChoreographer = Choreographer.getSfInstance();
    }

    public /* synthetic */ ValueAnimator lambda$new$1() {
        return new SfValueAnimator();
    }

    public void deferStartingAnimations() {
        synchronized (this.mLock) {
            this.mAnimationStartDeferred = true;
        }
    }

    public void continueStartingAnimations() {
        synchronized (this.mLock) {
            this.mAnimationStartDeferred = false;
            if (!this.mPendingAnimations.isEmpty() && this.mPreProcessingAnimations.isEmpty()) {
                this.mChoreographer.postFrameCallback(new SurfaceAnimationRunner$$ExternalSyntheticLambda0(this));
            }
        }
    }

    public void onTransactionCommitted(LocalAnimationAdapter.AnimationSpec animationSpec, SurfaceControl surfaceControl, RunningAnimation runningAnimation) {
        if (animationSpec == null || surfaceControl == null || runningAnimation == null) {
            return;
        }
        WindowAnimationSpec asWindowAnimationSpec = animationSpec.asWindowAnimationSpec();
        SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
        edgeExtendWindow(surfaceControl, asWindowAnimationSpec.getRootTaskBounds(), asWindowAnimationSpec.getAnimation(), transaction);
        synchronized (this.mLock) {
            Slog.i(TAG, "startAnimation EdgeExtensionExecutor onTransactionCommitted timeout, animationLeash=" + surfaceControl + ", runningAnim=" + runningAnimation + ", mPreProcessingAnimations.get(animationLeash)=" + this.mPreProcessingAnimations.get(surfaceControl) + ", mPreProcessingAnimations.containsValue(runningAnim)=" + this.mPreProcessingAnimations.containsValue(runningAnimation) + ", mPreProcessingAnimations=" + this.mPreProcessingAnimations);
            if (this.mPreProcessingAnimations.get(surfaceControl) == runningAnimation || this.mPreProcessingAnimations.containsValue(runningAnimation)) {
                synchronized (this.mEdgeExtensionLock) {
                    if (!this.mEdgeExtensions.isEmpty()) {
                        transaction.apply();
                    }
                }
                this.mPreProcessingAnimations.remove(surfaceControl);
                this.mPendingAnimations.put(surfaceControl, runningAnimation);
                if (!this.mAnimationStartDeferred && this.mPreProcessingAnimations.isEmpty()) {
                    this.mChoreographer.postFrameCallback(new SurfaceAnimationRunner$$ExternalSyntheticLambda0(this));
                }
            }
        }
    }

    public void startAnimation(final LocalAnimationAdapter.AnimationSpec animationSpec, final SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, Runnable runnable) {
        synchronized (this.mLock) {
            if (surfaceControl != null) {
                if (surfaceControl.isValid()) {
                    final RunningAnimation runningAnimation = new RunningAnimation(animationSpec, surfaceControl, runnable);
                    boolean requiresEdgeExtension = requiresEdgeExtension(animationSpec);
                    Slog.i(TAG, "startAnimation requiresEdgeExtension=" + requiresEdgeExtension + ", animationLeash=" + surfaceControl + ", runningAnim=" + runningAnimation + ", mPreProcessingAnimations=" + this.mPreProcessingAnimations);
                    if (requiresEdgeExtension) {
                        ArrayList<SurfaceControl> arrayList = new ArrayList<>();
                        synchronized (this.mEdgeExtensionLock) {
                            this.mEdgeExtensions.put(surfaceControl, arrayList);
                        }
                        this.mPreProcessingAnimations.put(surfaceControl, runningAnimation);
                        final C1CommitCallback c1CommitCallback = new Runnable() { // from class: com.android.server.wm.SurfaceAnimationRunner.1CommitCallback
                            final /* synthetic */ LocalAnimationAdapter.AnimationSpec val$a;
                            final /* synthetic */ SurfaceControl val$animationLeash;
                            final /* synthetic */ RunningAnimation val$runningAnim;

                            C1CommitCallback(final LocalAnimationAdapter.AnimationSpec animationSpec2, final SurfaceControl surfaceControl2, final RunningAnimation runningAnimation2) {
                                r2 = animationSpec2;
                                r3 = surfaceControl2;
                                r4 = runningAnimation2;
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                SurfaceAnimationRunner.this.onTransactionCommitted(r2, r3, r4);
                            }
                        };
                        this.mSurfaceAnimationHandler.postDelayed(c1CommitCallback, EDGEEXTENSION_TIME_OUT_DURATION);
                        transaction.addTransactionCommittedListener(this.mEdgeExtensionExecutor, new SurfaceControl.TransactionCommittedListener() { // from class: com.android.server.wm.SurfaceAnimationRunner$$ExternalSyntheticLambda2
                            @Override // android.view.SurfaceControl.TransactionCommittedListener
                            public final void onTransactionCommitted() {
                                SurfaceAnimationRunner.this.lambda$startAnimation$2(c1CommitCallback, animationSpec2, surfaceControl2, runningAnimation2);
                            }
                        });
                    }
                    if (!requiresEdgeExtension) {
                        this.mPendingAnimations.put(surfaceControl2, runningAnimation2);
                        if (!this.mAnimationStartDeferred && this.mPreProcessingAnimations.isEmpty()) {
                            this.mChoreographer.postFrameCallback(new SurfaceAnimationRunner$$ExternalSyntheticLambda0(this));
                        }
                        this.mSurfaceAnimationRunnerExt.trySaveAnimationLeashHashAndReinitializeAnimParams(animationSpec2, surfaceControl2.hashCode());
                    }
                    applyTransformation(runningAnimation2, transaction, 0L);
                }
            }
        }
    }

    /* renamed from: com.android.server.wm.SurfaceAnimationRunner$1CommitCallback */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class C1CommitCallback implements Runnable {
        final /* synthetic */ LocalAnimationAdapter.AnimationSpec val$a;
        final /* synthetic */ SurfaceControl val$animationLeash;
        final /* synthetic */ RunningAnimation val$runningAnim;

        C1CommitCallback(final LocalAnimationAdapter.AnimationSpec animationSpec2, final SurfaceControl surfaceControl2, final RunningAnimation runningAnimation2) {
            r2 = animationSpec2;
            r3 = surfaceControl2;
            r4 = runningAnimation2;
        }

        @Override // java.lang.Runnable
        public void run() {
            SurfaceAnimationRunner.this.onTransactionCommitted(r2, r3, r4);
        }
    }

    public /* synthetic */ void lambda$startAnimation$2(C1CommitCallback c1CommitCallback, LocalAnimationAdapter.AnimationSpec animationSpec, SurfaceControl surfaceControl, RunningAnimation runningAnimation) {
        this.mSurfaceAnimationHandler.removeCallbacks(c1CommitCallback);
        WindowAnimationSpec asWindowAnimationSpec = animationSpec.asWindowAnimationSpec();
        SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
        edgeExtendWindow(surfaceControl, asWindowAnimationSpec.getRootTaskBounds(), asWindowAnimationSpec.getAnimation(), transaction);
        synchronized (this.mLock) {
            Slog.i(TAG, "startAnimation EdgeExtensionExecutor, animationLeash=" + surfaceControl + ", runningAnim=" + runningAnimation + ", mPreProcessingAnimations.get(animationLeash)=" + this.mPreProcessingAnimations.get(surfaceControl) + ", mPreProcessingAnimations.containsValue(runningAnim)=" + this.mPreProcessingAnimations.containsValue(runningAnimation) + ", mPreProcessingAnimations=" + this.mPreProcessingAnimations);
            if (this.mPreProcessingAnimations.get(surfaceControl) == runningAnimation || this.mPreProcessingAnimations.containsValue(runningAnimation)) {
                synchronized (this.mEdgeExtensionLock) {
                    if (!this.mEdgeExtensions.isEmpty()) {
                        transaction.apply();
                    }
                }
                this.mPreProcessingAnimations.remove(surfaceControl);
                this.mPendingAnimations.put(surfaceControl, runningAnimation);
                if (!this.mAnimationStartDeferred && this.mPreProcessingAnimations.isEmpty()) {
                    this.mChoreographer.postFrameCallback(new SurfaceAnimationRunner$$ExternalSyntheticLambda0(this));
                }
            }
        }
    }

    private boolean requiresEdgeExtension(LocalAnimationAdapter.AnimationSpec animationSpec) {
        return animationSpec.asWindowAnimationSpec() != null && animationSpec.asWindowAnimationSpec().hasExtension();
    }

    public void onAnimationCancelled(SurfaceControl surfaceControl) {
        synchronized (this.mLock) {
            if (this.mPendingAnimations.containsKey(surfaceControl)) {
                this.mPendingAnimations.remove(surfaceControl);
                return;
            }
            if (this.mPreProcessingAnimations.containsKey(surfaceControl)) {
                this.mPreProcessingAnimations.remove(surfaceControl);
                return;
            }
            final RunningAnimation runningAnimation = this.mRunningAnimations.get(surfaceControl);
            if (runningAnimation != null) {
                this.mRunningAnimations.remove(surfaceControl);
                synchronized (this.mCancelLock) {
                    runningAnimation.mCancelled = true;
                }
                LocalAnimationAdapter.AnimationSpec animationSpec = runningAnimation.mAnimSpec;
                if (animationSpec != null) {
                    this.mSurfaceAnimationRunnerExt.tryClearAnimPointsWhenCancelled(animationSpec, surfaceControl.hashCode());
                }
                LocalAnimationAdapter.AnimationSpec animationSpec2 = runningAnimation.mAnimSpec;
                if (animationSpec2 != null) {
                    this.mSurfaceAnimationRunnerExt.onAnimationEnd(animationSpec2, this.mChoreographer);
                }
                Slog.d(TAG, "onAnimationCancelled:" + surfaceControl + " " + Debug.getCallers(5));
                this.mSurfaceAnimationHandler.post(new Runnable() { // from class: com.android.server.wm.SurfaceAnimationRunner$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        SurfaceAnimationRunner.this.lambda$onAnimationCancelled$3(runningAnimation);
                    }
                });
            }
        }
    }

    public /* synthetic */ void lambda$onAnimationCancelled$3(RunningAnimation runningAnimation) {
        runningAnimation.mAnim.cancel();
        applyTransaction();
    }

    @GuardedBy({"mLock"})
    private void startPendingAnimationsLocked() {
        for (int size = this.mPendingAnimations.size() - 1; size >= 0; size--) {
            startAnimationLocked(this.mPendingAnimations.valueAt(size));
        }
        this.mPendingAnimations.clear();
    }

    @GuardedBy({"mLock"})
    private void startAnimationLocked(final RunningAnimation runningAnimation) {
        final ValueAnimator makeAnimator = this.mAnimatorFactory.makeAnimator();
        this.mSurfaceAnimationRunnerExt.computeAnimHashForstartAnimationLocked(runningAnimation.mAnimSpec);
        ISurfaceAnimationRunnerExt iSurfaceAnimationRunnerExt = this.mSurfaceAnimationRunnerExt;
        LocalAnimationAdapter.AnimationSpec animationSpec = runningAnimation.mAnimSpec;
        iSurfaceAnimationRunnerExt.onAnimationStart(animationSpec, animationSpec.getDuration(), this.mChoreographer);
        makeAnimator.overrideDurationScale(1.0f);
        makeAnimator.setDuration(runningAnimation.mAnimSpec.getDuration());
        makeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.server.wm.SurfaceAnimationRunner$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                SurfaceAnimationRunner.this.lambda$startAnimationLocked$4(runningAnimation, makeAnimator, valueAnimator);
            }
        });
        makeAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.server.wm.SurfaceAnimationRunner.1
            final /* synthetic */ RunningAnimation val$a;

            AnonymousClass1(final RunningAnimation runningAnimation2) {
                r2 = runningAnimation2;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                synchronized (SurfaceAnimationRunner.this.mCancelLock) {
                    if (!r2.mCancelled) {
                        Slog.d(SurfaceAnimationRunner.TAG, "onAnimationStart:" + r2.mLeash);
                        SurfaceAnimationRunner.this.mFrameTransaction.setAlpha(r2.mLeash, 1.0f);
                    }
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                synchronized (SurfaceAnimationRunner.this.mLock) {
                    if (!SurfaceAnimationRunner.this.mSurfaceAnimationRunnerExt.hookonAnimationEndRemove(true, r2.mLeash)) {
                        SurfaceAnimationRunner.this.mRunningAnimations.remove(r2.mLeash);
                    }
                    synchronized (SurfaceAnimationRunner.this.mCancelLock) {
                        if (!r2.mCancelled) {
                            SurfaceAnimationRunner.this.mSurfaceAnimationRunnerExt.hookonAnimationEndRemove(false, r2.mLeash);
                            SurfaceAnimationRunner.this.mAnimationThreadHandler.post(r2.mFinishCallback);
                            SurfaceAnimationRunner.this.mSurfaceAnimationRunnerExt.onWindowAnimationEnded(r2.mLeash.hashCode());
                            SurfaceAnimationRunner.this.mSurfaceAnimationRunnerExt.onAnimationEnd(r2.mAnimSpec, SurfaceAnimationRunner.this.mChoreographer);
                        }
                    }
                    Slog.d(SurfaceAnimationRunner.TAG, "onAnimationEnd:" + r2.mLeash);
                }
            }
        });
        runningAnimation2.mAnim = makeAnimator;
        this.mRunningAnimations.put(runningAnimation2.mLeash, runningAnimation2);
        makeAnimator.start();
        if (runningAnimation2.mAnimSpec.canSkipFirstFrame()) {
            makeAnimator.setCurrentPlayTime(this.mChoreographer.getFrameIntervalNanos() / 1000000);
        }
        makeAnimator.doAnimationFrame(this.mChoreographer.getFrameTime());
    }

    public /* synthetic */ void lambda$startAnimationLocked$4(RunningAnimation runningAnimation, ValueAnimator valueAnimator, ValueAnimator valueAnimator2) {
        synchronized (this.mCancelLock) {
            if (!runningAnimation.mCancelled) {
                long duration = valueAnimator.getDuration();
                long currentPlayTime = valueAnimator.getCurrentPlayTime();
                if (currentPlayTime <= duration) {
                    duration = currentPlayTime;
                }
                applyTransformation(runningAnimation, this.mFrameTransaction, duration);
                this.mSurfaceAnimationRunnerExt.recordCurrentAnimationPoints(duration);
            }
        }
        scheduleApplyTransaction();
    }

    /* renamed from: com.android.server.wm.SurfaceAnimationRunner$1 */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class AnonymousClass1 extends AnimatorListenerAdapter {
        final /* synthetic */ RunningAnimation val$a;

        AnonymousClass1(final RunningAnimation runningAnimation2) {
            r2 = runningAnimation2;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            synchronized (SurfaceAnimationRunner.this.mCancelLock) {
                if (!r2.mCancelled) {
                    Slog.d(SurfaceAnimationRunner.TAG, "onAnimationStart:" + r2.mLeash);
                    SurfaceAnimationRunner.this.mFrameTransaction.setAlpha(r2.mLeash, 1.0f);
                }
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            synchronized (SurfaceAnimationRunner.this.mLock) {
                if (!SurfaceAnimationRunner.this.mSurfaceAnimationRunnerExt.hookonAnimationEndRemove(true, r2.mLeash)) {
                    SurfaceAnimationRunner.this.mRunningAnimations.remove(r2.mLeash);
                }
                synchronized (SurfaceAnimationRunner.this.mCancelLock) {
                    if (!r2.mCancelled) {
                        SurfaceAnimationRunner.this.mSurfaceAnimationRunnerExt.hookonAnimationEndRemove(false, r2.mLeash);
                        SurfaceAnimationRunner.this.mAnimationThreadHandler.post(r2.mFinishCallback);
                        SurfaceAnimationRunner.this.mSurfaceAnimationRunnerExt.onWindowAnimationEnded(r2.mLeash.hashCode());
                        SurfaceAnimationRunner.this.mSurfaceAnimationRunnerExt.onAnimationEnd(r2.mAnimSpec, SurfaceAnimationRunner.this.mChoreographer);
                    }
                }
                Slog.d(SurfaceAnimationRunner.TAG, "onAnimationEnd:" + r2.mLeash);
            }
        }
    }

    private void applyTransformation(RunningAnimation runningAnimation, SurfaceControl.Transaction transaction, long j) {
        SurfaceControl surfaceControl;
        if (runningAnimation != null && (surfaceControl = runningAnimation.mLeash) != null && surfaceControl.isValid()) {
            runningAnimation.mAnimSpec.apply(transaction, runningAnimation.mLeash, j);
            return;
        }
        Slog.e(TAG, "applyTransformation failed, RunningAnimation = " + runningAnimation);
    }

    public void startAnimations(long j) {
        synchronized (this.mLock) {
            if (!this.mPreProcessingAnimations.isEmpty()) {
                Slog.d(TAG, "startAnimations mPreProcessingAnimations is not empty");
            } else {
                startPendingAnimationsLocked();
                this.mPowerManagerInternal.setPowerBoost(0, 0);
            }
        }
    }

    private void scheduleApplyTransaction() {
        if (this.mApplyScheduled) {
            return;
        }
        this.mChoreographer.postCallback(3, this.mApplyTransactionRunnable, null);
        this.mApplyScheduled = true;
    }

    public void applyTransaction() {
        this.mFrameTransaction.setAnimationTransaction();
        this.mFrameTransaction.setFrameTimelineVsync(this.mChoreographer.getVsyncId());
        this.mFrameTransaction.apply();
        this.mApplyScheduled = false;
    }

    private void edgeExtendWindow(SurfaceControl surfaceControl, Rect rect, Animation animation, SurfaceControl.Transaction transaction) {
        Transformation transformation = new Transformation();
        animation.getTransformationAt(0.0f, transformation);
        Transformation transformation2 = new Transformation();
        animation.getTransformationAt(1.0f, transformation2);
        Insets min = Insets.min(transformation.getInsets(), transformation2.getInsets());
        int height = rect.height();
        int width = rect.width();
        if (min.left < 0) {
            int i = rect.left;
            createExtensionSurface(surfaceControl, new Rect(i, rect.top, i + 1, rect.bottom), new Rect(0, 0, -min.left, height), rect.left + min.left, rect.top, "Left Edge Extension", transaction);
        }
        if (min.top < 0) {
            int i2 = rect.left;
            int i3 = rect.top;
            createExtensionSurface(surfaceControl, new Rect(i2, i3, width, i3 + 1), new Rect(0, 0, width, -min.top), rect.left, rect.top + min.top, "Top Edge Extension", transaction);
        }
        if (min.right < 0) {
            int i4 = rect.right;
            createExtensionSurface(surfaceControl, new Rect(i4 - 1, rect.top, i4, rect.bottom), new Rect(0, 0, -min.right, height), rect.right, rect.top, "Right Edge Extension", transaction);
        }
        if (min.bottom < 0) {
            int i5 = rect.left;
            int i6 = rect.bottom;
            createExtensionSurface(surfaceControl, new Rect(i5, i6 - 1, rect.right, i6), new Rect(0, 0, width, -min.bottom), rect.left, rect.bottom, "Bottom Edge Extension", transaction);
        }
    }

    private void createExtensionSurface(SurfaceControl surfaceControl, Rect rect, Rect rect2, int i, int i2, String str, SurfaceControl.Transaction transaction) {
        Trace.traceBegin(32L, "createExtensionSurface");
        doCreateExtensionSurface(surfaceControl, rect, rect2, i, i2, str, transaction);
        Trace.traceEnd(32L);
    }

    private void doCreateExtensionSurface(SurfaceControl surfaceControl, Rect rect, Rect rect2, int i, int i2, String str, SurfaceControl.Transaction transaction) {
        ScreenCapture.ScreenshotHardwareBuffer captureLayers = ScreenCapture.captureLayers(new ScreenCapture.LayerCaptureArgs.Builder(surfaceControl).setSourceCrop(rect).setFrameScale(1.0f).setPixelFormat(1).setChildrenOnly(true).setAllowProtected(true).setCaptureSecureLayers(true).build());
        if (captureLayers == null) {
            Log.e(TAG, "Failed to create edge extension - edge buffer is null");
            return;
        }
        SurfaceControl build = new SurfaceControl.Builder().setName(str).setHidden(true).setCallsite("DefaultTransitionHandler#startAnimation").setOpaque(true).setBufferSize(rect2.width(), rect2.height()).build();
        Bitmap asBitmap = captureLayers.asBitmap();
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        BitmapShader bitmapShader = new BitmapShader(asBitmap, tileMode, tileMode);
        Paint paint = new Paint();
        paint.setShader(bitmapShader);
        Surface surface = new Surface(build);
        Canvas lockHardwareCanvas = surface.lockHardwareCanvas();
        lockHardwareCanvas.drawRect(rect2, paint);
        surface.unlockCanvasAndPost(lockHardwareCanvas);
        surface.release();
        synchronized (this.mEdgeExtensionLock) {
            if (!this.mEdgeExtensions.containsKey(surfaceControl)) {
                transaction.remove(build);
                return;
            }
            transaction.reparent(build, surfaceControl);
            transaction.setLayer(build, ChargerErrorCode.ERR_FILE_FAILURE_ACCESS);
            transaction.setPosition(build, i, i2);
            transaction.setVisibility(build, true);
            this.mEdgeExtensions.get(surfaceControl).add(build);
        }
    }

    private float getScaleXForExtensionSurface(Rect rect, Rect rect2) {
        if (rect.width() == rect2.width()) {
            return 1.0f;
        }
        if (rect.width() == 1) {
            return rect2.width();
        }
        throw new RuntimeException("Unexpected edgeBounds and extensionRect widths");
    }

    private float getScaleYForExtensionSurface(Rect rect, Rect rect2) {
        if (rect.height() == rect2.height()) {
            return 1.0f;
        }
        if (rect.height() == 1) {
            return rect2.height();
        }
        throw new RuntimeException("Unexpected edgeBounds and extensionRect heights");
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class RunningAnimation {
        ValueAnimator mAnim;
        final LocalAnimationAdapter.AnimationSpec mAnimSpec;

        @GuardedBy({"mCancelLock"})
        private boolean mCancelled;
        final Runnable mFinishCallback;
        final SurfaceControl mLeash;

        RunningAnimation(LocalAnimationAdapter.AnimationSpec animationSpec, SurfaceControl surfaceControl, Runnable runnable) {
            this.mAnimSpec = animationSpec;
            this.mLeash = surfaceControl;
            this.mFinishCallback = runnable;
        }
    }

    public void onAnimationLeashLost(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
        synchronized (this.mEdgeExtensionLock) {
            if (this.mEdgeExtensions.containsKey(surfaceControl)) {
                ArrayList<SurfaceControl> arrayList = this.mEdgeExtensions.get(surfaceControl);
                for (int i = 0; i < arrayList.size(); i++) {
                    transaction.remove(arrayList.get(i));
                }
                this.mEdgeExtensions.remove(surfaceControl);
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class SfValueAnimator extends ValueAnimator {
        SfValueAnimator() {
            setFloatValues(0.0f, 1.0f);
        }

        public AnimationHandler getAnimationHandler() {
            return SurfaceAnimationRunner.this.mAnimationHandler;
        }
    }
}
