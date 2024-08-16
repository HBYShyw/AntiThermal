package com.android.server.wm;

import android.graphics.Point;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import android.view.SurfaceControl;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.server.wm.ISurfaceAnimatorExt;
import com.android.server.wm.SurfaceFreezer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.function.Supplier;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class SurfaceAnimator {
    public static final int ANIMATION_TYPE_ALL = -1;
    public static final int ANIMATION_TYPE_APP_TRANSITION = 1;
    public static final int ANIMATION_TYPE_DIMMER = 4;
    static final int ANIMATION_TYPE_GESTURE = 256;
    public static final int ANIMATION_TYPE_INSETS_CONTROL = 32;
    public static final int ANIMATION_TYPE_NONE = 0;
    public static final int ANIMATION_TYPE_PREDICT_BACK = 256;
    public static final int ANIMATION_TYPE_RECENTS = 8;
    public static final int ANIMATION_TYPE_SCREEN_ROTATION = 2;
    public static final int ANIMATION_TYPE_STARTING_REVEAL = 128;
    public static final int ANIMATION_TYPE_TOKEN_TRANSFORM = 64;
    public static final int ANIMATION_TYPE_WINDOW_ANIMATION = 16;
    private static final String TAG = "WindowManager";
    private static ISurfaceAnimatorExt.IStaticExt mStaticExt = (ISurfaceAnimatorExt.IStaticExt) ExtLoader.type(ISurfaceAnimatorExt.IStaticExt.class).create();

    @VisibleForTesting
    final Animatable mAnimatable;
    private AnimationAdapter mAnimation;
    private Runnable mAnimationCancelledCallback;
    private boolean mAnimationFinished;
    private boolean mAnimationStartDelayed;
    private int mAnimationType;

    @VisibleForTesting
    final OnAnimationFinishedCallback mInnerAnimationFinishedCallback;

    @VisibleForTesting
    SurfaceControl mLeash;
    private final WindowManagerService mService;

    @VisibleForTesting
    SurfaceFreezer.Snapshot mSnapshot;

    @VisibleForTesting
    final OnAnimationFinishedCallback mStaticAnimationFinishedCallback;
    private OnAnimationFinishedCallback mSurfaceAnimationFinishedCallback;
    private SurfaceAnimatorWrapper mSAWrapper = new SurfaceAnimatorWrapper();
    private ISurfaceAnimatorExt mSurfaceAnimatorExt = (ISurfaceAnimatorExt) ExtLoader.type(ISurfaceAnimatorExt.class).base(this).create();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface Animatable {
        void commitPendingTransaction();

        default SurfaceControl getAnimationLeash() {
            return null;
        }

        SurfaceControl getAnimationLeashParent();

        SurfaceControl getParentSurfaceControl();

        SurfaceControl.Transaction getPendingTransaction();

        SurfaceControl getSurfaceControl();

        int getSurfaceHeight();

        int getSurfaceWidth();

        SurfaceControl.Transaction getSyncTransaction();

        SurfaceControl.Builder makeAnimationLeash();

        void onAnimationLeashCreated(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl);

        void onAnimationLeashLost(SurfaceControl.Transaction transaction);

        default void onLeashAnimationStarting(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl) {
        }

        default boolean shouldDeferAnimationFinish(Runnable runnable) {
            return false;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    @interface AnimationType {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface OnAnimationFinishedCallback {
        void onAnimationFinished(int i, AnimationAdapter animationAdapter);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SurfaceAnimator(Animatable animatable, OnAnimationFinishedCallback onAnimationFinishedCallback, WindowManagerService windowManagerService) {
        this.mAnimatable = animatable;
        this.mService = windowManagerService;
        this.mStaticAnimationFinishedCallback = onAnimationFinishedCallback;
        this.mInnerAnimationFinishedCallback = getFinishedCallback(onAnimationFinishedCallback);
    }

    private OnAnimationFinishedCallback getFinishedCallback(final OnAnimationFinishedCallback onAnimationFinishedCallback) {
        return new OnAnimationFinishedCallback() { // from class: com.android.server.wm.SurfaceAnimator$$ExternalSyntheticLambda0
            @Override // com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback
            public final void onAnimationFinished(int i, AnimationAdapter animationAdapter) {
                SurfaceAnimator.this.lambda$getFinishedCallback$1(onAnimationFinishedCallback, i, animationAdapter);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getFinishedCallback$1(final OnAnimationFinishedCallback onAnimationFinishedCallback, final int i, final AnimationAdapter animationAdapter) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                SurfaceAnimator remove = this.mService.mAnimationTransferMap.remove(animationAdapter);
                if (remove != null) {
                    remove.mInnerAnimationFinishedCallback.onAnimationFinished(i, animationAdapter);
                    WindowManagerService.resetPriorityAfterLockedSection();
                } else {
                    if (animationAdapter != this.mAnimation) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    Runnable runnable = new Runnable() { // from class: com.android.server.wm.SurfaceAnimator$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            SurfaceAnimator.this.lambda$getFinishedCallback$0(animationAdapter, onAnimationFinishedCallback, i);
                        }
                    };
                    if (!this.mAnimatable.shouldDeferAnimationFinish(runnable) && !animationAdapter.shouldDeferAnimationFinish(runnable)) {
                        runnable.run();
                    } else {
                        this.mSurfaceAnimatorExt.setDeferAnimationFinish(this.mAnimatable, true);
                    }
                    this.mAnimationFinished = true;
                    WindowManagerService.resetPriorityAfterLockedSection();
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getFinishedCallback$0(AnimationAdapter animationAdapter, OnAnimationFinishedCallback onAnimationFinishedCallback, int i) {
        if (animationAdapter != this.mAnimation) {
            return;
        }
        OnAnimationFinishedCallback onAnimationFinishedCallback2 = this.mSurfaceAnimationFinishedCallback;
        if (!this.mSurfaceAnimatorExt.hookResetForTask(this, false)) {
            reset(this.mAnimatable.getSyncTransaction(), true);
        }
        if (onAnimationFinishedCallback != null) {
            onAnimationFinishedCallback.onAnimationFinished(i, animationAdapter);
        }
        if (onAnimationFinishedCallback2 != null) {
            onAnimationFinishedCallback2.onAnimationFinished(i, animationAdapter);
        }
        this.mSurfaceAnimatorExt.setDeferAnimationFinish(this.mAnimatable, false);
        if (this.mSurfaceAnimatorExt.cancelAnimThreadUxIfNeed(this.mAnimatable, i)) {
            this.mSurfaceAnimatorExt.callOrmsSetSceneActionForRemoteAnimation(false, this.mLeash, this.mAnimatable.getPendingTransaction(), i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startAnimation(SurfaceControl.Transaction transaction, AnimationAdapter animationAdapter, boolean z, int i, OnAnimationFinishedCallback onAnimationFinishedCallback, Runnable runnable, AnimationAdapter animationAdapter2, SurfaceFreezer surfaceFreezer) {
        String str;
        Point point;
        String str2;
        boolean z2;
        Point point2 = new Point(0, 0);
        this.mSurfaceAnimatorExt.setReuseLeash(this);
        cancelAnimation(transaction, true, true);
        this.mAnimation = animationAdapter;
        this.mAnimationType = i;
        this.mSurfaceAnimationFinishedCallback = onAnimationFinishedCallback;
        this.mAnimationCancelledCallback = runnable;
        SurfaceControl surfaceControl = this.mAnimatable.getSurfaceControl();
        if (surfaceControl == null || !surfaceControl.isValid()) {
            Slog.w(TAG, "Unable to start animation, surface is null or no children.");
            cancelAnimation();
            return;
        }
        if (!this.mSurfaceAnimatorExt.hookSetLeash(this.mAnimatable, this.mLeash)) {
            this.mLeash = surfaceFreezer != null ? surfaceFreezer.takeLeashForAnimation() : null;
        }
        if (this.mLeash == null) {
            boolean useGesturePosition = this.mSurfaceAnimatorExt.useGesturePosition(this, point2, false);
            if (WindowManagerDebugConfig.DEBUG_ANIM) {
                Slog.i(TAG, "useGesturePosition:" + useGesturePosition + " gesturePosition:" + point2);
            }
            Animatable animatable = this.mAnimatable;
            int surfaceWidth = animatable.getSurfaceWidth();
            int surfaceHeight = this.mAnimatable.getSurfaceHeight();
            int i2 = point2.x;
            int i3 = point2.y;
            Supplier<SurfaceControl.Transaction> supplier = this.mService.mTransactionFactory;
            point = point2;
            str2 = null;
            str = TAG;
            SurfaceControl createAnimationLeash = createAnimationLeash(animatable, surfaceControl, transaction, i, surfaceWidth, surfaceHeight, i2, i3, z, supplier);
            this.mLeash = createAnimationLeash;
            this.mAnimatable.onAnimationLeashCreated(transaction, createAnimationLeash);
            this.mSAWrapper.getExtImpl().showTaskIfNeed(this.mAnimatable, transaction);
            this.mSAWrapper.getExtImpl().boostLeashLayerIfNeed(this.mAnimatable, i, transaction, this.mLeash);
            z2 = useGesturePosition;
        } else {
            str = TAG;
            point = point2;
            str2 = null;
            z2 = false;
        }
        this.mAnimatable.onLeashAnimationStarting(transaction, this.mLeash);
        if (this.mAnimationStartDelayed) {
            if (ProtoLogCache.WM_DEBUG_ANIM_enabled) {
                ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_ANIM, 215077284, 0, str2, new Object[]{String.valueOf(this.mAnimatable)});
                return;
            }
            return;
        }
        this.mSurfaceAnimatorExt.callOrmsSetSceneActionForRemoteAnimation(true, this.mLeash, transaction, i);
        this.mAnimation.startAnimation(this.mLeash, transaction, i, this.mInnerAnimationFinishedCallback);
        if (ProtoLogImpl.isEnabled(ProtoLogGroup.WM_DEBUG_ANIM)) {
            StringWriter stringWriter = new StringWriter();
            this.mAnimation.dump(new PrintWriter(stringWriter), "");
            if (ProtoLogCache.WM_DEBUG_ANIM_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_ANIM, -1969928125, 0, str2, new Object[]{String.valueOf(this.mAnimatable), String.valueOf(stringWriter)});
            }
        }
        if (animationAdapter2 != null) {
            SurfaceFreezer.Snapshot takeSnapshotForAnimation = surfaceFreezer.takeSnapshotForAnimation();
            this.mSnapshot = takeSnapshotForAnimation;
            if (takeSnapshotForAnimation == null) {
                Slog.e(str, "No snapshot target to start animation on for " + this.mAnimatable);
                return;
            }
            takeSnapshotForAnimation.startAnimation(transaction, animationAdapter2, i);
        }
        if (z2) {
            Point point3 = point;
            transaction.setPosition(this.mLeash, point3.x, point3.y);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startAnimation(SurfaceControl.Transaction transaction, AnimationAdapter animationAdapter, boolean z, int i) {
        startAnimation(transaction, animationAdapter, z, i, null, null, null, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startDelayingAnimationStart() {
        if (isAnimating()) {
            return;
        }
        this.mAnimationStartDelayed = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void endDelayingAnimationStart() {
        AnimationAdapter animationAdapter;
        boolean z = this.mAnimationStartDelayed;
        this.mAnimationStartDelayed = false;
        if (!z || (animationAdapter = this.mAnimation) == null) {
            return;
        }
        animationAdapter.startAnimation(this.mLeash, this.mAnimatable.getSyncTransaction(), this.mAnimationType, this.mInnerAnimationFinishedCallback);
        this.mAnimatable.commitPendingTransaction();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAnimating() {
        return this.mAnimation != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getAnimationType() {
        return this.mAnimationType;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AnimationAdapter getAnimation() {
        return this.mAnimation;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cancelAnimation() {
        cancelAnimation(this.mAnimatable.getSyncTransaction(), false, true);
        this.mAnimatable.commitPendingTransaction();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLayer(SurfaceControl.Transaction transaction, int i) {
        SurfaceControl surfaceControl = this.mLeash;
        if (surfaceControl != null && (this.mSurfaceAnimatorExt.isDragZoomToSplitLeash(surfaceControl) || this.mSurfaceAnimatorExt.isDragSplitToFullLeash(this.mLeash))) {
            Slog.d(TAG, "don't change layer when drag zoom to split:" + this.mLeash);
            return;
        }
        SurfaceControl surfaceControl2 = this.mLeash;
        if (surfaceControl2 == null) {
            surfaceControl2 = this.mAnimatable.getSurfaceControl();
        }
        transaction.setLayer(surfaceControl2, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRelativeLayer(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, int i) {
        SurfaceControl surfaceControl2 = this.mLeash;
        if (surfaceControl2 == null) {
            surfaceControl2 = this.mAnimatable.getSurfaceControl();
        }
        transaction.setRelativeLayer(surfaceControl2, surfaceControl, i);
    }

    void reparent(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl) {
        SurfaceControl surfaceControl2 = this.mLeash;
        if (surfaceControl2 == null) {
            surfaceControl2 = this.mAnimatable.getSurfaceControl();
        }
        transaction.reparent(surfaceControl2, surfaceControl);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasLeash() {
        return this.mLeash != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void transferAnimation(SurfaceAnimator surfaceAnimator) {
        if (surfaceAnimator.mLeash == null) {
            return;
        }
        SurfaceControl surfaceControl = this.mAnimatable.getSurfaceControl();
        SurfaceControl animationLeashParent = this.mAnimatable.getAnimationLeashParent();
        if (surfaceControl == null || animationLeashParent == null) {
            Slog.w(TAG, "Unable to transfer animation, surface or parent is null");
            cancelAnimation();
            return;
        }
        if (surfaceAnimator.mAnimationFinished) {
            Slog.w(TAG, "Unable to transfer animation, because " + surfaceAnimator + " animation is finished");
            return;
        }
        endDelayingAnimationStart();
        SurfaceControl.Transaction syncTransaction = this.mAnimatable.getSyncTransaction();
        cancelAnimation(syncTransaction, true, true);
        this.mLeash = surfaceAnimator.mLeash;
        this.mAnimation = surfaceAnimator.mAnimation;
        this.mAnimationType = surfaceAnimator.mAnimationType;
        this.mSurfaceAnimationFinishedCallback = surfaceAnimator.mSurfaceAnimationFinishedCallback;
        this.mAnimationCancelledCallback = surfaceAnimator.mAnimationCancelledCallback;
        surfaceAnimator.cancelAnimation(syncTransaction, false, false);
        SurfaceControl surfaceControl2 = this.mLeash;
        if (surfaceControl2 == null) {
            return;
        }
        syncTransaction.reparent(surfaceControl, surfaceControl2);
        syncTransaction.reparent(this.mLeash, animationLeashParent);
        this.mAnimatable.onAnimationLeashCreated(syncTransaction, this.mLeash);
        this.mService.mAnimationTransferMap.put(this.mAnimation, this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAnimationStartDelayed() {
        return this.mAnimationStartDelayed;
    }

    private void cancelAnimation(SurfaceControl.Transaction transaction, boolean z, boolean z2) {
        if (ProtoLogCache.WM_DEBUG_ANIM_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_ANIM, 397862437, 3, (String) null, new Object[]{Boolean.valueOf(z), String.valueOf(this.mAnimatable)});
        }
        SurfaceControl surfaceControl = this.mLeash;
        AnimationAdapter animationAdapter = this.mAnimation;
        int i = this.mAnimationType;
        OnAnimationFinishedCallback onAnimationFinishedCallback = this.mSurfaceAnimationFinishedCallback;
        Runnable runnable = this.mAnimationCancelledCallback;
        SurfaceFreezer.Snapshot snapshot = this.mSnapshot;
        reset(transaction, false);
        if (animationAdapter != null) {
            if (!this.mAnimationStartDelayed && z2) {
                animationAdapter.onAnimationCancelled(surfaceControl);
                if (runnable != null) {
                    runnable.run();
                }
            }
            if (!z) {
                OnAnimationFinishedCallback onAnimationFinishedCallback2 = this.mStaticAnimationFinishedCallback;
                if (onAnimationFinishedCallback2 != null) {
                    onAnimationFinishedCallback2.onAnimationFinished(i, animationAdapter);
                }
                if (onAnimationFinishedCallback != null) {
                    onAnimationFinishedCallback.onAnimationFinished(i, animationAdapter);
                }
            }
        }
        if (z2) {
            if (snapshot != null) {
                snapshot.cancelAnimation(transaction, false);
            }
            if (surfaceControl != null && !this.mSurfaceAnimatorExt.isReuseLeash() && surfaceControl.isValid()) {
                this.mLeash = null;
                transaction.remove(surfaceControl);
                this.mService.scheduleAnimationLocked();
            }
        }
        if (z) {
            return;
        }
        this.mAnimationStartDelayed = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reset(SurfaceControl.Transaction transaction, boolean z) {
        this.mService.mAnimationTransferMap.remove(this.mAnimation);
        this.mAnimation = null;
        this.mSurfaceAnimationFinishedCallback = null;
        this.mAnimationType = 0;
        SurfaceFreezer.Snapshot snapshot = this.mSnapshot;
        this.mSnapshot = null;
        if (snapshot != null) {
            snapshot.cancelAnimation(transaction, !z);
        }
        SurfaceControl surfaceControl = this.mLeash;
        if (surfaceControl == null || this.mSurfaceAnimatorExt.hookReset(this, transaction)) {
            return;
        }
        this.mLeash = null;
        boolean removeLeash = removeLeash(transaction, this.mAnimatable, surfaceControl, z);
        this.mAnimationFinished = false;
        if (removeLeash) {
            this.mService.scheduleAnimationLocked();
            this.mSurfaceAnimatorExt.resetIfNeeded(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean removeLeash(SurfaceControl.Transaction transaction, Animatable animatable, SurfaceControl surfaceControl, boolean z) {
        SurfaceControl surfaceControl2 = animatable.getSurfaceControl();
        SurfaceControl parentSurfaceControl = animatable.getParentSurfaceControl();
        SurfaceControl animationLeash = animatable.getAnimationLeash();
        boolean z2 = false;
        boolean z3 = surfaceControl2 != null && (animationLeash == null || animationLeash.equals(surfaceControl));
        if (animatable instanceof Task) {
            Task task = (Task) animatable;
            if (((IMirageWindowManagerExt) ExtLoader.type(IMirageWindowManagerExt.class).create()).shouldReparentToNull(task.mTaskId)) {
                z3 = false;
            }
            if (task.getWrapper().getExtImpl().isTaskEmbedded()) {
                ((IFlexibleWindowManagerExt) ExtLoader.type(IFlexibleWindowManagerExt.class).create()).onFlexibleWindowTaskAppeared(task, task.getTaskInfo());
                z3 = false;
            }
        }
        if (z3) {
            if (ProtoLogCache.WM_DEBUG_ANIM_enabled) {
                ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_ANIM, -319689203, 0, (String) null, new Object[]{String.valueOf(parentSurfaceControl), String.valueOf(animatable)});
            }
            if (surfaceControl2.isValid() && parentSurfaceControl != null && parentSurfaceControl.isValid()) {
                transaction.reparent(surfaceControl2, parentSurfaceControl);
                z2 = true;
            }
        }
        if (z) {
            transaction.remove(surfaceControl);
            z2 = true;
        }
        if (!z3) {
            return z2;
        }
        animatable.onAnimationLeashLost(transaction);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SurfaceControl createAnimationLeash(Animatable animatable, SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, int i, int i2, int i3, int i4, int i5, boolean z, Supplier<SurfaceControl.Transaction> supplier) {
        if (ProtoLogCache.WM_DEBUG_ANIM_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_ANIM, -208664771, 0, (String) null, new Object[]{String.valueOf(animatable)});
        }
        SurfaceControl build = animatable.makeAnimationLeash().setParent(animatable.getAnimationLeashParent()).setName(surfaceControl + " - animation-leash of " + animationTypeToString(i)).setHidden(z).setEffectLayer().setCallsite("SurfaceAnimator.createAnimationLeash").build();
        mStaticExt.adjustAnimationLeashLayerIfNeeded(transaction, animatable, build);
        transaction.setWindowCrop(build, i2, i3);
        transaction.setPosition(build, (float) i4, (float) i5);
        transaction.show(build);
        transaction.setAlpha(build, z ? 0.0f : 1.0f);
        transaction.reparent(surfaceControl, build);
        return build;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        AnimationAdapter animationAdapter = this.mAnimation;
        if (animationAdapter != null) {
            animationAdapter.dumpDebug(protoOutputStream, 1146756268035L);
        }
        SurfaceControl surfaceControl = this.mLeash;
        if (surfaceControl != null) {
            surfaceControl.dumpDebug(protoOutputStream, 1146756268033L);
        }
        protoOutputStream.write(1133871366146L, this.mAnimationStartDelayed);
        protoOutputStream.end(start);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str) {
        printWriter.print(str);
        printWriter.print("mLeash=");
        printWriter.print(this.mLeash);
        printWriter.print(" mAnimationType=" + animationTypeToString(this.mAnimationType));
        printWriter.println(this.mAnimationStartDelayed ? " mAnimationStartDelayed=true" : "");
        printWriter.print(str);
        printWriter.print("Animation: ");
        printWriter.println(this.mAnimation);
        AnimationAdapter animationAdapter = this.mAnimation;
        if (animationAdapter != null) {
            animationAdapter.dump(printWriter, str + "  ");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String animationTypeToString(int i) {
        if (i == 0) {
            return "none";
        }
        if (i == 1) {
            return "app_transition";
        }
        if (i == 2) {
            return "screen_rotation";
        }
        if (i == 4) {
            return "dimmer";
        }
        if (i == 8) {
            return "recents_animation";
        }
        if (i == 16) {
            return "window_animation";
        }
        if (i == 32) {
            return "insets_animation";
        }
        if (i == 64) {
            return "token_transform";
        }
        if (i == 128) {
            return "starting_reveal";
        }
        if (i == 256) {
            return "predict_back";
        }
        return "unknown type:" + i;
    }

    public ISurfaceAnimatorWrapper getWrapper() {
        return this.mSAWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class SurfaceAnimatorWrapper implements ISurfaceAnimatorWrapper {
        private SurfaceAnimatorWrapper() {
        }

        @Override // com.android.server.wm.ISurfaceAnimatorWrapper
        public ISurfaceAnimatorExt getExtImpl() {
            return SurfaceAnimator.this.mSurfaceAnimatorExt;
        }

        @Override // com.android.server.wm.ISurfaceAnimatorWrapper
        public void reset(SurfaceControl.Transaction transaction, boolean z) {
            SurfaceAnimator.this.reset(transaction, z);
        }
    }
}
