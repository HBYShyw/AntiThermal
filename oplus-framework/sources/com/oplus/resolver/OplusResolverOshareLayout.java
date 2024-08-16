package com.oplus.resolver;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.PathInterpolator;
import android.widget.LinearLayout;

/* loaded from: classes.dex */
public class OplusResolverOshareLayout extends LinearLayout {
    private static final PathInterpolator DEFAUT_PATHINTERPOLATOR = new PathInterpolator(0.3f, 0.0f, 0.1f, 1.0f);
    private static final int PANEL_VIEW_APPEAR_AND_DISAPPEAR_DURATION = 133;
    public static final int STATUS_NEED_OPEN_OSHARE = 0;
    public static final int STATUS_OPENED_OSHARE = 1;
    public static final int STATUS_OSHARING = 2;
    private static final String TAG = "OplusResolverOshareLayout";
    private ValueAnimator mAnimator;
    private int mAnimatorCurrentHeight;
    private boolean mHasExecuteAnimator;
    private boolean mIsSplitScreenMode;
    private int mShareNeedOpenHeight;
    private int mShareOpenStatus;
    private int mShareOpenedHeight;
    private int mShareingHeight;

    public OplusResolverOshareLayout(Context context) {
        this(context, null);
    }

    public OplusResolverOshareLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OplusResolverOshareLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mHasExecuteAnimator = false;
        this.mShareOpenStatus = -1;
        this.mShareNeedOpenHeight = 0;
        this.mShareOpenedHeight = 0;
        this.mShareingHeight = 0;
        this.mAnimatorCurrentHeight = 0;
        this.mIsSplitScreenMode = false;
        this.mShareingHeight = (int) context.getResources().getDimension(201654446);
        this.mShareNeedOpenHeight = (int) context.getResources().getDimension(201654445);
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (getMeasuredHeight() > this.mShareNeedOpenHeight && this.mShareOpenedHeight == 0 && this.mShareOpenStatus == 1) {
            Log.d(TAG, "set mShareOpenedHeight");
            this.mShareOpenedHeight = getMeasuredHeight();
            if (!this.mIsSplitScreenMode) {
                startAnimatorIfNeeded();
            }
        }
    }

    public void setShareOpenStatus(int value) {
        if (this.mShareOpenStatus != value) {
            this.mShareOpenStatus = value;
            if (!this.mIsSplitScreenMode) {
                this.mHasExecuteAnimator = true;
                startAnimatorIfNeeded();
            }
        }
    }

    public void setIsSplitScreenMode(boolean value) {
        this.mIsSplitScreenMode = value;
        if (value) {
            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = -2;
            setLayoutParams(params);
        }
    }

    private void startAnimatorIfNeeded() {
        if (this.mHasExecuteAnimator) {
            ViewGroup.LayoutParams params = getLayoutParams();
            Log.d(TAG, "mShareOpenStatus=" + this.mShareOpenStatus + ",params.height=" + params.height + ",mShareingHeight=" + this.mShareingHeight + ",mShareOpenedHeight=" + this.mShareOpenedHeight + ",mShareNeedOpenHeight=" + this.mShareNeedOpenHeight);
            switch (this.mShareOpenStatus) {
                case 0:
                    if (params.height > this.mShareNeedOpenHeight) {
                        startLayoutHeightAnimator(params.height, this.mShareNeedOpenHeight);
                        return;
                    }
                    return;
                case 1:
                    int i = this.mShareOpenedHeight;
                    if (i > this.mShareNeedOpenHeight || (i != 0 && params.height > this.mShareOpenedHeight)) {
                        startLayoutHeightAnimator(Math.max(this.mShareNeedOpenHeight, params.height), this.mShareOpenedHeight);
                        return;
                    }
                    return;
                case 2:
                    if (this.mShareOpenedHeight == 0) {
                        this.mShareOpenedHeight = this.mShareNeedOpenHeight;
                    }
                    ValueAnimator valueAnimator = this.mAnimator;
                    if (valueAnimator != null && valueAnimator.isRunning()) {
                        this.mAnimator.cancel();
                        int i2 = this.mAnimatorCurrentHeight;
                        if (i2 == 0) {
                            i2 = this.mShareOpenedHeight;
                        }
                        startLayoutHeightAnimator(i2, this.mShareingHeight);
                        return;
                    }
                    startLayoutHeightAnimator(this.mShareOpenedHeight, this.mShareingHeight);
                    return;
                default:
                    return;
            }
        }
    }

    private void startLayoutHeightAnimator(int initHeight, int targetHeight) {
        Log.d(TAG, "startLayoutHeightAnimator initHeight=" + initHeight + ",targetHeight=" + targetHeight);
        this.mHasExecuteAnimator = false;
        ValueAnimator ofInt = ValueAnimator.ofInt(initHeight, targetHeight);
        this.mAnimator = ofInt;
        ofInt.setDuration(133L);
        this.mAnimator.setInterpolator(DEFAUT_PATHINTERPOLATOR);
        this.mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.oplus.resolver.OplusResolverOshareLayout$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                OplusResolverOshareLayout.this.lambda$startLayoutHeightAnimator$0(valueAnimator);
            }
        });
        this.mAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.oplus.resolver.OplusResolverOshareLayout.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                OplusResolverOshareLayout.this.mAnimatorCurrentHeight = 0;
                ViewGroup.LayoutParams layoutParams = OplusResolverOshareLayout.this.getLayoutParams();
                layoutParams.height = -2;
                OplusResolverOshareLayout.this.setLayoutParams(layoutParams);
            }
        });
        this.mAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startLayoutHeightAnimator$0(ValueAnimator animation) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = ((Integer) animation.getAnimatedValue()).intValue();
        this.mAnimatorCurrentHeight = layoutParams.height;
        setLayoutParams(layoutParams);
    }
}
