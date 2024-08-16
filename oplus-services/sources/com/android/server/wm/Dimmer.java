package com.android.server.wm;

import android.graphics.Rect;
import android.util.Log;
import android.util.proto.ProtoOutputStream;
import android.view.Surface;
import android.view.SurfaceControl;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.wm.Dimmer;
import com.android.server.wm.LocalAnimationAdapter;
import com.android.server.wm.SurfaceAnimator;
import java.io.PrintWriter;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class Dimmer {
    private static final int DEFAULT_DIM_ANIM_DURATION = 200;
    private static final String TAG = "WindowManager";

    @VisibleForTesting
    DimState mDimState;
    private IDimmerExt mDimmerExt;
    private DimmerWrapper mDimmerWrapper;
    private WindowContainer mHost;
    private WindowContainer mLastRequestedDimContainer;
    private final SurfaceAnimatorStarter mSurfaceAnimatorStarter;

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface SurfaceAnimatorStarter {
        void startAnimation(SurfaceAnimator surfaceAnimator, SurfaceControl.Transaction transaction, AnimationAdapter animationAdapter, boolean z, int i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class DimAnimatable implements SurfaceAnimator.Animatable {
        private SurfaceControl mDimLayer;

        @Override // com.android.server.wm.SurfaceAnimator.Animatable
        public void onAnimationLeashCreated(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl) {
        }

        @Override // com.android.server.wm.SurfaceAnimator.Animatable
        public void onAnimationLeashLost(SurfaceControl.Transaction transaction) {
        }

        private DimAnimatable(SurfaceControl surfaceControl) {
            this.mDimLayer = surfaceControl;
        }

        @Override // com.android.server.wm.SurfaceAnimator.Animatable
        public SurfaceControl.Transaction getSyncTransaction() {
            return Dimmer.this.mHost.getSyncTransaction();
        }

        @Override // com.android.server.wm.SurfaceAnimator.Animatable
        public SurfaceControl.Transaction getPendingTransaction() {
            return Dimmer.this.mHost.getPendingTransaction();
        }

        @Override // com.android.server.wm.SurfaceAnimator.Animatable
        public void commitPendingTransaction() {
            Dimmer.this.mHost.commitPendingTransaction();
        }

        @Override // com.android.server.wm.SurfaceAnimator.Animatable
        public SurfaceControl.Builder makeAnimationLeash() {
            return Dimmer.this.mHost.makeAnimationLeash();
        }

        @Override // com.android.server.wm.SurfaceAnimator.Animatable
        public SurfaceControl getAnimationLeashParent() {
            return Dimmer.this.mHost.getSurfaceControl();
        }

        @Override // com.android.server.wm.SurfaceAnimator.Animatable
        public SurfaceControl getSurfaceControl() {
            return this.mDimLayer;
        }

        @Override // com.android.server.wm.SurfaceAnimator.Animatable
        public SurfaceControl getParentSurfaceControl() {
            return Dimmer.this.mHost.getSurfaceControl();
        }

        @Override // com.android.server.wm.SurfaceAnimator.Animatable
        public int getSurfaceWidth() {
            return Dimmer.this.mHost.getSurfaceWidth();
        }

        @Override // com.android.server.wm.SurfaceAnimator.Animatable
        public int getSurfaceHeight() {
            return Dimmer.this.mHost.getSurfaceHeight();
        }

        void removeSurface() {
            SurfaceControl surfaceControl = this.mDimLayer;
            if (surfaceControl != null && surfaceControl.isValid()) {
                getSyncTransaction().remove(this.mDimLayer);
            }
            this.mDimLayer = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class DimState {
        boolean isVisible;
        SurfaceControl mDimLayer;
        boolean mDontReset;
        SurfaceAnimator mSurfaceAnimator;
        final Rect mDimBounds = new Rect();
        boolean mAnimateExit = true;
        boolean mDimming = true;

        DimState(SurfaceControl surfaceControl) {
            this.mDimLayer = surfaceControl;
            final DimAnimatable dimAnimatable = new DimAnimatable(surfaceControl);
            this.mSurfaceAnimator = new SurfaceAnimator(dimAnimatable, new SurfaceAnimator.OnAnimationFinishedCallback() { // from class: com.android.server.wm.Dimmer$DimState$$ExternalSyntheticLambda0
                @Override // com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback
                public final void onAnimationFinished(int i, AnimationAdapter animationAdapter) {
                    Dimmer.DimState.this.lambda$new$0(dimAnimatable, i, animationAdapter);
                }
            }, Dimmer.this.mHost.mWmService);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(DimAnimatable dimAnimatable, int i, AnimationAdapter animationAdapter) {
            if (this.mDimming) {
                return;
            }
            dimAnimatable.removeSurface();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Dimmer(WindowContainer windowContainer) {
        this(windowContainer, new SurfaceAnimatorStarter() { // from class: com.android.server.wm.Dimmer$$ExternalSyntheticLambda0
            @Override // com.android.server.wm.Dimmer.SurfaceAnimatorStarter
            public final void startAnimation(SurfaceAnimator surfaceAnimator, SurfaceControl.Transaction transaction, AnimationAdapter animationAdapter, boolean z, int i) {
                surfaceAnimator.startAnimation(transaction, animationAdapter, z, i);
            }
        });
    }

    Dimmer(WindowContainer windowContainer, SurfaceAnimatorStarter surfaceAnimatorStarter) {
        this.mDimmerExt = (IDimmerExt) ExtLoader.type(IDimmerExt.class).base(this).create();
        this.mDimmerWrapper = new DimmerWrapper();
        this.mHost = windowContainer;
        this.mSurfaceAnimatorStarter = surfaceAnimatorStarter;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowContainer<?> getHost() {
        return this.mHost;
    }

    private SurfaceControl makeDimLayer() {
        return this.mHost.makeChildSurface(null).setParent(this.mHost.getSurfaceControl()).setColorLayer().setName("Dim Layer for - " + this.mHost.getName()).setCallsite("Dimmer.makeDimLayer").build();
    }

    private DimState getDimState(WindowContainer windowContainer) {
        if (this.mDimState == null) {
            try {
                DimState dimState = new DimState(makeDimLayer());
                this.mDimState = dimState;
                if (windowContainer == null) {
                    dimState.mDontReset = true;
                }
            } catch (Surface.OutOfResourcesException unused) {
                Log.w(TAG, "OutOfResourcesException creating dim surface");
            }
        }
        this.mLastRequestedDimContainer = windowContainer;
        return this.mDimState;
    }

    private void dim(WindowContainer windowContainer, int i, float f, int i2) {
        DimState dimState = getDimState(windowContainer);
        if (dimState == null) {
            return;
        }
        SurfaceControl.Transaction pendingTransaction = this.mHost.getPendingTransaction();
        pendingTransaction.setRelativeLayer(dimState.mDimLayer, windowContainer.getSurfaceControl(), i);
        pendingTransaction.setAlpha(dimState.mDimLayer, f);
        pendingTransaction.setBackgroundBlurRadius(dimState.mDimLayer, i2);
        dimState.mDimming = true;
    }

    void dimAbove(WindowContainer windowContainer, float f) {
        dim(windowContainer, 1, f, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dimBelow(WindowContainer windowContainer, float f, int i) {
        dim(windowContainer, -1, f, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetDimStates() {
        DimState dimState = this.mDimState;
        if (dimState == null || dimState.mDontReset) {
            return;
        }
        dimState.mDimming = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect getDimBounds() {
        DimState dimState = this.mDimState;
        if (dimState != null) {
            return dimState.mDimBounds;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dontAnimateExit() {
        DimState dimState = this.mDimState;
        if (dimState != null) {
            dimState.mAnimateExit = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateDims(SurfaceControl.Transaction transaction) {
        DimState dimState = this.mDimState;
        if (dimState == null) {
            return false;
        }
        if (!dimState.mDimming) {
            if (!dimState.mAnimateExit || this.mDimmerExt.skipDimAnimation(this.mHost)) {
                if (this.mDimState.mDimLayer.isValid()) {
                    transaction.remove(this.mDimState.mDimLayer);
                }
            } else {
                startDimExit(this.mLastRequestedDimContainer, this.mDimState.mSurfaceAnimator, transaction);
            }
            this.mDimState = null;
            return false;
        }
        Rect rect = dimState.mDimBounds;
        this.mDimmerExt.updateDims(this.mLastRequestedDimContainer, rect, dimState.mDimLayer, transaction);
        transaction.setPosition(this.mDimState.mDimLayer, rect.left, rect.top);
        transaction.setWindowCrop(this.mDimState.mDimLayer, rect.width(), rect.height());
        DimState dimState2 = this.mDimState;
        if (!dimState2.isVisible) {
            dimState2.isVisible = true;
            transaction.show(dimState2.mDimLayer);
            startDimEnter(this.mLastRequestedDimContainer, this.mDimState.mSurfaceAnimator, transaction);
        }
        return true;
    }

    private void startDimEnter(WindowContainer windowContainer, SurfaceAnimator surfaceAnimator, SurfaceControl.Transaction transaction) {
        startAnim(windowContainer, surfaceAnimator, transaction, 0.0f, 1.0f);
    }

    private void startDimExit(WindowContainer windowContainer, SurfaceAnimator surfaceAnimator, SurfaceControl.Transaction transaction) {
        startAnim(windowContainer, surfaceAnimator, transaction, 1.0f, 0.0f);
    }

    private void startAnim(WindowContainer windowContainer, SurfaceAnimator surfaceAnimator, SurfaceControl.Transaction transaction, float f, float f2) {
        this.mSurfaceAnimatorStarter.startAnimation(surfaceAnimator, transaction, new LocalAnimationAdapter(new AlphaAnimationSpec(f, f2, this.mDimmerExt.useSpeceficDurationForDim(windowContainer, this.mHost, f2) ? 0L : getDimDuration(windowContainer)), this.mHost.mWmService.mSurfaceAnimationRunner), false, 4);
    }

    private long getDimDuration(WindowContainer windowContainer) {
        if (windowContainer == null) {
            return 0L;
        }
        AnimationAdapter animation = windowContainer.mSurfaceAnimator.getAnimation();
        if (animation == null) {
            return 200L;
        }
        return animation.getDurationHint();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class AlphaAnimationSpec implements LocalAnimationAdapter.AnimationSpec {
        private final long mDuration;
        private final float mFromAlpha;
        private final float mToAlpha;

        AlphaAnimationSpec(float f, float f2, long j) {
            this.mFromAlpha = f;
            this.mToAlpha = f2;
            this.mDuration = j;
        }

        @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
        public long getDuration() {
            return this.mDuration;
        }

        @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
        public void apply(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, long j) {
            float fraction = getFraction((float) j);
            float f = this.mToAlpha;
            float f2 = this.mFromAlpha;
            transaction.setAlpha(surfaceControl, (fraction * (f - f2)) + f2);
        }

        @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
        public void dump(PrintWriter printWriter, String str) {
            printWriter.print(str);
            printWriter.print("from=");
            printWriter.print(this.mFromAlpha);
            printWriter.print(" to=");
            printWriter.print(this.mToAlpha);
            printWriter.print(" duration=");
            printWriter.println(this.mDuration);
        }

        @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
        public void dumpDebugInner(ProtoOutputStream protoOutputStream) {
            long start = protoOutputStream.start(1146756268035L);
            protoOutputStream.write(1108101562369L, this.mFromAlpha);
            protoOutputStream.write(1108101562370L, this.mToAlpha);
            protoOutputStream.write(1112396529667L, this.mDuration);
            protoOutputStream.end(start);
        }
    }

    public IDimmerWrapper getWrapper() {
        return this.mDimmerWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private class DimmerWrapper implements IDimmerWrapper {
        private DimmerWrapper() {
        }

        @Override // com.android.server.wm.IDimmerWrapper
        public IDimmerExt getExtImpl() {
            return Dimmer.this.mDimmerExt;
        }

        @Override // com.android.server.wm.IDimmerWrapper
        public WindowContainer getLastRequestedDimContainer() {
            return Dimmer.this.mLastRequestedDimContainer;
        }
    }
}
