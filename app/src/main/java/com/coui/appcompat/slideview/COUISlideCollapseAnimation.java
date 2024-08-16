package com.coui.appcompat.slideview;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import androidx.core.view.animation.PathInterpolatorCompat;

/* loaded from: classes.dex */
public abstract class COUISlideCollapseAnimation extends Animation implements Animation.AnimationListener {
    private static final int ANIM_DURATION = 200;
    private static final float ONT = 1.0f;
    private static final float POINT_133 = 0.133f;
    private static final float POINT_THREE = 0.3f;
    private static final float ZERO = 0.0f;
    int mInitialHeight;
    View mView;

    public COUISlideCollapseAnimation(View view) {
        this.mView = view;
        this.mInitialHeight = view.getMeasuredHeight();
        setInterpolator(PathInterpolatorCompat.a(POINT_133, 0.0f, POINT_THREE, ONT));
        setDuration(200L);
        setAnimationListener(this);
    }

    @Override // android.view.animation.Animation
    protected void applyTransformation(float f10, Transformation transformation) {
        if (f10 == ONT) {
            this.mView.setVisibility(8);
            return;
        }
        ViewGroup.LayoutParams layoutParams = this.mView.getLayoutParams();
        int i10 = this.mInitialHeight;
        layoutParams.height = i10 - ((int) (i10 * f10));
        this.mView.requestLayout();
    }

    @Override // android.view.animation.Animation.AnimationListener
    public void onAnimationEnd(Animation animation) {
        onItemDelete();
    }

    @Override // android.view.animation.Animation.AnimationListener
    public void onAnimationRepeat(Animation animation) {
    }

    @Override // android.view.animation.Animation.AnimationListener
    public void onAnimationStart(Animation animation) {
    }

    public abstract void onItemDelete();

    @Override // android.view.animation.Animation
    public boolean willChangeBounds() {
        return true;
    }
}
