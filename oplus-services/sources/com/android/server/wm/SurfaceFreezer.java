package com.android.server.wm;

import android.graphics.GraphicBuffer;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.HardwareBuffer;
import android.util.Slog;
import android.view.SurfaceControl;
import android.window.ScreenCapture;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.server.wm.SurfaceAnimator;
import com.android.server.wm.SurfaceFreezer;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class SurfaceFreezer {
    private static final String TAG = "SurfaceFreezer";
    private final Freezable mAnimatable;

    @VisibleForTesting
    SurfaceControl mLeash;
    private final WindowManagerService mWmService;
    Snapshot mSnapshot = null;
    final Rect mFreezeBounds = new Rect();
    ISurfaceFreezerExt mSurfaceFreezerExt = (ISurfaceFreezerExt) ExtLoader.type(ISurfaceFreezerExt.class).base(this).create();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface Freezable extends SurfaceAnimator.Animatable {
        SurfaceControl getFreezeSnapshotTarget();

        void onUnfrozen();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SurfaceFreezer(Freezable freezable, WindowManagerService windowManagerService) {
        this.mAnimatable = freezable;
        this.mWmService = windowManagerService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void freeze(SurfaceControl.Transaction transaction, Rect rect, Point point, SurfaceControl surfaceControl) {
        reset(transaction);
        this.mFreezeBounds.set(rect);
        Freezable freezable = this.mAnimatable;
        SurfaceControl createAnimationLeash = SurfaceAnimator.createAnimationLeash(freezable, freezable.getSurfaceControl(), transaction, 2, rect.width(), rect.height(), point.x, point.y, false, this.mWmService.mTransactionFactory);
        this.mLeash = createAnimationLeash;
        this.mAnimatable.onAnimationLeashCreated(transaction, createAnimationLeash);
        if (surfaceControl == null) {
            surfaceControl = this.mAnimatable.getFreezeSnapshotTarget();
        }
        if (surfaceControl != null) {
            ScreenCapture.ScreenshotHardwareBuffer createFlexibleTaskSnapshotBuffer = this.mSurfaceFreezerExt.createFlexibleTaskSnapshotBuffer(surfaceControl);
            if (createFlexibleTaskSnapshotBuffer == null) {
                createFlexibleTaskSnapshotBuffer = createSnapshotBufferInner(surfaceControl, rect);
            }
            HardwareBuffer hardwareBuffer = createFlexibleTaskSnapshotBuffer == null ? null : createFlexibleTaskSnapshotBuffer.getHardwareBuffer();
            if (hardwareBuffer == null || hardwareBuffer.getWidth() <= 1 || hardwareBuffer.getHeight() <= 1) {
                Slog.w(TAG, "Failed to capture screenshot for " + this.mAnimatable);
                unfreeze(transaction);
                return;
            }
            this.mSnapshot = new Snapshot(transaction, createFlexibleTaskSnapshotBuffer, this.mLeash);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SurfaceControl takeLeashForAnimation() {
        SurfaceControl surfaceControl = this.mLeash;
        this.mLeash = null;
        return surfaceControl;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Snapshot takeSnapshotForAnimation() {
        Snapshot snapshot = this.mSnapshot;
        this.mSnapshot = null;
        return snapshot;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unfreeze(SurfaceControl.Transaction transaction) {
        unfreezeInner(transaction);
        this.mAnimatable.onUnfrozen();
        this.mSurfaceFreezerExt.resetFlexibleTaskInfo();
    }

    private void unfreezeInner(SurfaceControl.Transaction transaction) {
        Snapshot snapshot = this.mSnapshot;
        if (snapshot != null) {
            snapshot.cancelAnimation(transaction, false);
            this.mSnapshot = null;
        }
        SurfaceControl surfaceControl = this.mLeash;
        if (surfaceControl == null) {
            return;
        }
        this.mLeash = null;
        if (SurfaceAnimator.removeLeash(transaction, this.mAnimatable, surfaceControl, true)) {
            this.mWmService.scheduleAnimationLocked();
        }
    }

    private void reset(SurfaceControl.Transaction transaction) {
        Snapshot snapshot = this.mSnapshot;
        if (snapshot != null) {
            snapshot.destroy(transaction);
            this.mSnapshot = null;
        }
        SurfaceControl surfaceControl = this.mLeash;
        if (surfaceControl != null) {
            transaction.remove(surfaceControl);
            this.mLeash = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLayer(SurfaceControl.Transaction transaction, int i) {
        SurfaceControl surfaceControl = this.mLeash;
        if (surfaceControl != null) {
            transaction.setLayer(surfaceControl, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRelativeLayer(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, int i) {
        SurfaceControl surfaceControl2 = this.mLeash;
        if (surfaceControl2 != null) {
            transaction.setRelativeLayer(surfaceControl2, surfaceControl, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasLeash() {
        return this.mLeash != null;
    }

    private static ScreenCapture.ScreenshotHardwareBuffer createSnapshotBuffer(SurfaceControl surfaceControl, Rect rect) {
        Rect rect2;
        if (rect != null) {
            rect2 = new Rect(rect);
            rect2.offsetTo(0, 0);
        } else {
            rect2 = null;
        }
        return ScreenCapture.captureLayers(new ScreenCapture.LayerCaptureArgs.Builder(surfaceControl).setSourceCrop(rect2).setCaptureSecureLayers(true).setAllowProtected(true).build());
    }

    @VisibleForTesting
    ScreenCapture.ScreenshotHardwareBuffer createSnapshotBufferInner(SurfaceControl surfaceControl, Rect rect) {
        return createSnapshotBuffer(surfaceControl, rect);
    }

    @VisibleForTesting
    GraphicBuffer createFromHardwareBufferInner(ScreenCapture.ScreenshotHardwareBuffer screenshotHardwareBuffer) {
        return GraphicBuffer.createFromHardwareBuffer(screenshotHardwareBuffer.getHardwareBuffer());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class Snapshot {
        private AnimationAdapter mAnimation;
        private SurfaceControl mSurfaceControl;

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$startAnimation$0(int i, AnimationAdapter animationAdapter) {
        }

        Snapshot(SurfaceControl.Transaction transaction, ScreenCapture.ScreenshotHardwareBuffer screenshotHardwareBuffer, SurfaceControl surfaceControl) {
            GraphicBuffer createFromHardwareBufferInner = SurfaceFreezer.this.createFromHardwareBufferInner(screenshotHardwareBuffer);
            SurfaceControl build = SurfaceFreezer.this.mAnimatable.makeAnimationLeash().setName("snapshot anim: " + SurfaceFreezer.this.mAnimatable.toString()).setFormat(-3).setParent(surfaceControl).setSecure(screenshotHardwareBuffer.containsSecureLayers()).setCallsite("SurfaceFreezer.Snapshot").setBLASTLayer().build();
            this.mSurfaceControl = build;
            if (ProtoLogCache.WM_SHOW_TRANSACTIONS_enabled) {
                ProtoLogImpl.i(ProtoLogGroup.WM_SHOW_TRANSACTIONS, -668956537, 0, (String) null, new Object[]{String.valueOf(build)});
            }
            transaction.setBuffer(this.mSurfaceControl, createFromHardwareBufferInner);
            transaction.setColorSpace(this.mSurfaceControl, screenshotHardwareBuffer.getColorSpace());
            transaction.show(this.mSurfaceControl);
            transaction.setLayer(this.mSurfaceControl, Integer.MAX_VALUE);
        }

        void destroy(SurfaceControl.Transaction transaction) {
            SurfaceControl surfaceControl = this.mSurfaceControl;
            if (surfaceControl == null) {
                return;
            }
            transaction.remove(surfaceControl);
            this.mSurfaceControl = null;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void startAnimation(SurfaceControl.Transaction transaction, AnimationAdapter animationAdapter, int i) {
            cancelAnimation(transaction, true);
            this.mAnimation = animationAdapter;
            SurfaceControl surfaceControl = this.mSurfaceControl;
            if (surfaceControl == null) {
                cancelAnimation(transaction, false);
            } else {
                animationAdapter.startAnimation(surfaceControl, transaction, i, new SurfaceAnimator.OnAnimationFinishedCallback() { // from class: com.android.server.wm.SurfaceFreezer$Snapshot$$ExternalSyntheticLambda0
                    @Override // com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback
                    public final void onAnimationFinished(int i2, AnimationAdapter animationAdapter2) {
                        SurfaceFreezer.Snapshot.lambda$startAnimation$0(i2, animationAdapter2);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void cancelAnimation(SurfaceControl.Transaction transaction, boolean z) {
            SurfaceControl surfaceControl = this.mSurfaceControl;
            AnimationAdapter animationAdapter = this.mAnimation;
            this.mAnimation = null;
            if (animationAdapter != null) {
                animationAdapter.onAnimationCancelled(surfaceControl);
            }
            if (z) {
                return;
            }
            destroy(transaction);
        }
    }
}
