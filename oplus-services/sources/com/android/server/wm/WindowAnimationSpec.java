package com.android.server.wm;

import android.graphics.Insets;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.proto.ProtoOutputStream;
import android.view.SurfaceControl;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import com.android.server.wm.LocalAnimationAdapter;
import java.io.PrintWriter;
import java.util.function.Supplier;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class WindowAnimationSpec implements LocalAnimationAdapter.AnimationSpec {
    private Animation mAnimation;
    private final boolean mCanSkipFirstFrame;
    private final boolean mIsAppAnimation;
    private final Point mPosition;
    private final Rect mRootTaskBounds;
    private int mRootTaskClipMode;
    private final ThreadLocal<TmpValues> mThreadLocalTmps;
    private final Rect mTmpRect;
    public IWindowAnimationSpecExt mWindowAnimationSpecExt;
    private final float mWindowCornerRadius;

    public static /* synthetic */ TmpValues $r8$lambda$LPYhkojuA7Rcm_KU4DHewFkMvcY() {
        return new TmpValues();
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public WindowAnimationSpec asWindowAnimationSpec() {
        return this;
    }

    public WindowAnimationSpec(Animation animation, Point point, boolean z, float f) {
        this(animation, point, null, z, 1, false, f);
    }

    public WindowAnimationSpec(Animation animation, Point point, Rect rect, boolean z, int i, boolean z2, float f) {
        Point point2 = new Point();
        this.mPosition = point2;
        this.mThreadLocalTmps = ThreadLocal.withInitial(new Supplier() { // from class: com.android.server.wm.WindowAnimationSpec$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                return WindowAnimationSpec.$r8$lambda$LPYhkojuA7Rcm_KU4DHewFkMvcY();
            }
        });
        Rect rect2 = new Rect();
        this.mRootTaskBounds = rect2;
        this.mTmpRect = new Rect();
        this.mWindowAnimationSpecExt = (IWindowAnimationSpecExt) ExtLoader.type(IWindowAnimationSpecExt.class).base(this).create();
        this.mAnimation = animation;
        if (point != null) {
            point2.set(point.x, point.y);
        }
        this.mWindowCornerRadius = f;
        this.mCanSkipFirstFrame = z;
        this.mIsAppAnimation = z2;
        this.mRootTaskClipMode = i;
        if (rect != null) {
            rect2.set(rect);
        }
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public boolean getShowWallpaper() {
        return this.mAnimation.getShowWallpaper();
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public boolean getShowBackground() {
        return this.mAnimation.getShowBackdrop();
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public int getBackgroundColor() {
        return this.mAnimation.getBackdropColor();
    }

    public boolean hasExtension() {
        if (this.mWindowAnimationSpecExt.useExtendAnimation()) {
            return this.mAnimation.hasExtension();
        }
        return false;
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public long getDuration() {
        return this.mAnimation.computeDurationHint();
    }

    public Rect getRootTaskBounds() {
        return this.mRootTaskBounds;
    }

    public Animation getAnimation() {
        return this.mAnimation;
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public void apply(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, long j) {
        TmpValues tmpValues = this.mThreadLocalTmps.get();
        tmpValues.transformation.clear();
        this.mAnimation.getTransformation(j, tmpValues.transformation);
        Matrix matrix = tmpValues.transformation.getMatrix();
        Point point = this.mPosition;
        matrix.postTranslate(point.x, point.y);
        transaction.setMatrix(surfaceControl, tmpValues.transformation.getMatrix(), tmpValues.floats);
        transaction.setAlpha(surfaceControl, tmpValues.transformation.getAlpha());
        boolean z = true;
        if (this.mRootTaskClipMode == 1) {
            if (tmpValues.transformation.hasClipRect()) {
                Rect clipRect = tmpValues.transformation.getClipRect();
                accountForExtension(tmpValues.transformation, clipRect);
                transaction.setWindowCrop(surfaceControl, clipRect);
            } else {
                z = false;
            }
        } else {
            this.mTmpRect.set(this.mRootTaskBounds);
            IWindowAnimationSpecExt iWindowAnimationSpecExt = this.mWindowAnimationSpecExt;
            iWindowAnimationSpecExt.clipTmpRect(iWindowAnimationSpecExt.getmClipSide(), this.mTmpRect, tmpValues.floats[2], transaction, surfaceControl);
            if (tmpValues.transformation.hasClipRect()) {
                this.mTmpRect.intersect(tmpValues.transformation.getClipRect());
            }
            accountForExtension(tmpValues.transformation, this.mTmpRect);
            this.mWindowAnimationSpecExt.adjustCropRect(this.mAnimation, this.mTmpRect, tmpValues.transformation, transaction);
            transaction.setWindowCrop(surfaceControl, this.mTmpRect);
        }
        if (z && this.mAnimation.hasRoundedCorners()) {
            float f = this.mWindowCornerRadius;
            if (f > 0.0f) {
                transaction.setCornerRadius(surfaceControl, f);
            }
        }
    }

    private void accountForExtension(Transformation transformation, Rect rect) {
        Insets min = Insets.min(transformation.getInsets(), Insets.NONE);
        if (min.equals(Insets.NONE)) {
            return;
        }
        rect.inset(min);
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public long calculateStatusBarTransitionStartTime() {
        long uptimeMillis;
        long j;
        TranslateAnimation findTranslateAnimation = findTranslateAnimation(this.mAnimation);
        if (findTranslateAnimation != null) {
            if (findTranslateAnimation.isXAxisTransition() && findTranslateAnimation.isFullWidthTranslate()) {
                uptimeMillis = SystemClock.uptimeMillis() + findTranslateAnimation.getStartOffset() + (((float) findTranslateAnimation.getDuration()) * findMiddleOfTranslationFraction(findTranslateAnimation.getInterpolator()));
                j = 60;
            } else {
                uptimeMillis = SystemClock.uptimeMillis() + findTranslateAnimation.getStartOffset() + (((float) findTranslateAnimation.getDuration()) * findAlmostThereFraction(findTranslateAnimation.getInterpolator()));
                j = 120;
            }
            return uptimeMillis - j;
        }
        return SystemClock.uptimeMillis();
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public boolean canSkipFirstFrame() {
        return this.mCanSkipFirstFrame;
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public boolean needsEarlyWakeup() {
        return this.mIsAppAnimation;
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public void dump(PrintWriter printWriter, String str) {
        printWriter.print(str);
        printWriter.println(this.mAnimation);
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public void dumpDebugInner(ProtoOutputStream protoOutputStream) {
        long start = protoOutputStream.start(1146756268033L);
        protoOutputStream.write(1138166333441L, this.mAnimation.toString());
        protoOutputStream.end(start);
    }

    private static TranslateAnimation findTranslateAnimation(Animation animation) {
        if (animation instanceof TranslateAnimation) {
            return (TranslateAnimation) animation;
        }
        if (!(animation instanceof AnimationSet)) {
            return null;
        }
        AnimationSet animationSet = (AnimationSet) animation;
        for (int i = 0; i < animationSet.getAnimations().size(); i++) {
            Animation animation2 = animationSet.getAnimations().get(i);
            if (animation2 instanceof TranslateAnimation) {
                return (TranslateAnimation) animation2;
            }
        }
        return null;
    }

    private static float findAlmostThereFraction(Interpolator interpolator) {
        return findInterpolationAdjustedTargetFraction(interpolator, 0.99f, 0.01f);
    }

    private float findMiddleOfTranslationFraction(Interpolator interpolator) {
        return findInterpolationAdjustedTargetFraction(interpolator, 0.5f, 0.01f);
    }

    private static float findInterpolationAdjustedTargetFraction(Interpolator interpolator, float f, float f2) {
        float f3 = 0.5f;
        for (float f4 = 0.25f; f4 >= f2; f4 /= 2.0f) {
            f3 = interpolator.getInterpolation(f3) < f ? f3 + f4 : f3 - f4;
        }
        return f3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class TmpValues {
        final float[] floats;
        final Transformation transformation;

        private TmpValues() {
            this.transformation = new Transformation();
            this.floats = new float[9];
        }
    }
}
