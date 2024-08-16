package com.oplus.resolver;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

/* loaded from: classes.dex */
public class OplusResolverScanLoadingIcon extends FrameLayout {
    private static final int CIRCLE_APPEAR_DURATION = 750;
    private static final int CIRCLE_DISAPPEAR_DURATION = 250;
    private static final boolean DEBUG = true;
    private static final PathInterpolator DEFAULT_INTERPOLATOR = new PathInterpolator(0.33f, 0.0f, 0.67f, 1.0f);
    private static final int INNER_CIRCLE_DISAPPEAR_DELAY = 2000;
    private static final int OUTER_CIRCLE_APPEAR_DELAY = 167;
    private static final int OUTER_CIRCLE_DISAPPEAR_DELAY = 2167;
    private static final String TAG = "OplusResolverScanLoadingIcon";
    private final AnimatorSet mAnimatorSet;
    private boolean mCanStartAnimator;
    private Context mContext;

    public OplusResolverScanLoadingIcon(Context context) {
        this(context, null);
    }

    public OplusResolverScanLoadingIcon(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OplusResolverScanLoadingIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        inflate(context, 201917488, this);
        setBackground(this.mContext.getDrawable(201850948));
        ImageView outerCircle = (ImageView) findViewById(201457726);
        ImageView innerCircle = (ImageView) findViewById(201457727);
        ObjectAnimator innerCircleAppearAnimator = ObjectAnimator.ofFloat(innerCircle, "alpha", 0.0f, 1.0f);
        innerCircleAppearAnimator.setDuration(750L);
        ObjectAnimator innerCircleDisappearAnimator = ObjectAnimator.ofFloat(innerCircle, "alpha", 1.0f, 0.0f);
        innerCircleDisappearAnimator.setDuration(250L);
        innerCircleDisappearAnimator.setStartDelay(2000L);
        ObjectAnimator outerCircleAppearAnimator = ObjectAnimator.ofFloat(outerCircle, "alpha", 0.0f, 1.0f);
        outerCircleAppearAnimator.setDuration(750L);
        outerCircleAppearAnimator.setStartDelay(167L);
        ObjectAnimator outerCircleDisappearAnimator = ObjectAnimator.ofFloat(outerCircle, "alpha", 1.0f, 0.0f);
        outerCircleDisappearAnimator.setDuration(250L);
        outerCircleDisappearAnimator.setStartDelay(2167L);
        AnimatorSet animatorSet = new AnimatorSet();
        this.mAnimatorSet = animatorSet;
        animatorSet.setInterpolator(DEFAULT_INTERPOLATOR);
        animatorSet.playTogether(innerCircleAppearAnimator, innerCircleAppearAnimator, outerCircleAppearAnimator, outerCircleDisappearAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.oplus.resolver.OplusResolverScanLoadingIcon.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (OplusResolverScanLoadingIcon.this.mCanStartAnimator) {
                    Log.d(OplusResolverScanLoadingIcon.TAG, "scan icon restart animatorSet");
                    OplusResolverScanLoadingIcon.this.mAnimatorSet.start();
                } else {
                    OplusResolverScanLoadingIcon oplusResolverScanLoadingIcon = OplusResolverScanLoadingIcon.this;
                    oplusResolverScanLoadingIcon.setBackground(oplusResolverScanLoadingIcon.mContext.getDrawable(201850948));
                }
            }
        });
    }

    public void startAnimator() {
        setBackground(null);
        this.mCanStartAnimator = true;
        Log.d(TAG, "scan icon start animatorSet");
        if (this.mAnimatorSet.isRunning()) {
            return;
        }
        this.mAnimatorSet.start();
    }

    public void stopAnimator() {
        Log.d(TAG, "scan icon stop animatorSet");
        this.mCanStartAnimator = false;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "scan icon detach");
        this.mCanStartAnimator = false;
        if (this.mAnimatorSet.isRunning()) {
            Log.d(TAG, "scan icon cancel animatorSet");
            this.mAnimatorSet.cancel();
        }
    }
}
