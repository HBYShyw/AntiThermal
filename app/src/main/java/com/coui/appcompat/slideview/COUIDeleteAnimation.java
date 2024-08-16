package com.coui.appcompat.slideview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import androidx.core.view.animation.PathInterpolatorCompat;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes.dex */
public class COUIDeleteAnimation implements Animator.AnimatorListener {
    private static final float ONE = 1.0f;
    private static final float POINT_133 = 0.133f;
    private static final float POINT_THREE = 0.3f;
    private static final float ZERO = 0.0f;
    private final AnimatorSet mAnimatorSet;
    public boolean mEnded;
    final float mStartDx;
    final float mStartDy;
    final float mTargetX;
    final float mTargetY;
    public View mView;
    public RecyclerView.c0 mViewHolder;

    public COUIDeleteAnimation(View view, View view2, float f10, float f11, float f12, float f13) {
        this.mEnded = false;
        this.mView = view;
        this.mStartDx = f10;
        this.mStartDy = f11;
        this.mTargetX = f12;
        this.mTargetY = f13;
        AnimatorSet animatorSet = new AnimatorSet();
        this.mAnimatorSet = animatorSet;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "translationX", 0.0f, f12);
        if (view2 != null) {
            animatorSet.play(ofFloat).with(ObjectAnimator.ofFloat(view2, "alpha", ONE, 0.0f));
        } else {
            animatorSet.play(ofFloat);
        }
        animatorSet.setInterpolator(PathInterpolatorCompat.a(POINT_133, 0.0f, POINT_THREE, ONE));
        animatorSet.addListener(this);
    }

    public void cancel() {
        this.mAnimatorSet.cancel();
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationCancel(Animator animator) {
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator animator) {
        this.mEnded = true;
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationRepeat(Animator animator) {
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationStart(Animator animator) {
    }

    public void setDuration(long j10) {
        this.mAnimatorSet.setDuration(j10);
    }

    public void start() {
        RecyclerView.c0 c0Var = this.mViewHolder;
        if (c0Var != null) {
            c0Var.setIsRecyclable(false);
        }
        this.mAnimatorSet.start();
    }

    public COUIDeleteAnimation(RecyclerView.c0 c0Var, float f10, float f11, float f12, float f13) {
        this.mEnded = false;
        this.mViewHolder = c0Var;
        this.mStartDx = f10;
        this.mStartDy = f11;
        this.mTargetX = f12;
        this.mTargetY = f13;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(c0Var.itemView, "translationX", 0.0f, f12);
        AnimatorSet animatorSet = new AnimatorSet();
        this.mAnimatorSet = animatorSet;
        animatorSet.play(ofFloat);
        animatorSet.setInterpolator(PathInterpolatorCompat.a(POINT_133, 0.0f, POINT_THREE, ONE));
        animatorSet.addListener(this);
    }

    public COUIDeleteAnimation(View view, float f10, float f11, float f12, float f13) {
        this(view, null, f10, f11, f12, f13);
    }
}
