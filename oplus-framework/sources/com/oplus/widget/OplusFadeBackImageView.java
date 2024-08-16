package com.oplus.widget;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.OplusBezierInterpolator;
import android.widget.ImageView;

/* loaded from: classes.dex */
public class OplusFadeBackImageView extends ImageView {
    private static final String TAG = "OplusFadeBackImageView";
    private static final int TOUCH_END_DURATION = 300;
    private static final int TOUCH_START_DURATION = 200;
    private ValueAnimator mAnimator;
    private float mCurrentScale;
    private OplusBezierInterpolator mTouchEndInterpolator;
    private OplusBezierInterpolator mTouchStartInterpolator;

    public OplusFadeBackImageView(Context context) {
        this(context, null);
    }

    public OplusFadeBackImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OplusFadeBackImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mTouchStartInterpolator = new OplusBezierInterpolator(0.25d, 0.1d, 0.1d, 1.0d, true);
        this.mTouchEndInterpolator = new OplusBezierInterpolator(0.25d, 0.1d, 0.25d, 1.0d, true);
        this.mCurrentScale = 1.0f;
    }

    @Override // android.view.View
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == 0) {
            performTouchStartAnim();
        } else if (event.getAction() == 3 || event.getAction() == 1) {
            performTouchEndAnim();
        }
        super.dispatchTouchEvent(event);
        return true;
    }

    private void performTouchStartAnim() {
        ValueAnimator valueAnimator = this.mAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mAnimator.cancel();
        }
        PropertyValuesHolder widthHolder = PropertyValuesHolder.ofFloat("scaleHolder", this.mCurrentScale, 0.9f);
        ValueAnimator ofPropertyValuesHolder = ValueAnimator.ofPropertyValuesHolder(widthHolder);
        this.mAnimator = ofPropertyValuesHolder;
        ofPropertyValuesHolder.setInterpolator(this.mTouchStartInterpolator);
        this.mAnimator.setDuration(200L);
        this.mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.oplus.widget.OplusFadeBackImageView$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                OplusFadeBackImageView.this.lambda$performTouchStartAnim$0(valueAnimator2);
            }
        });
        this.mAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performTouchStartAnim$0(ValueAnimator animation) {
        float floatValue = ((Float) animation.getAnimatedValue("scaleHolder")).floatValue();
        this.mCurrentScale = floatValue;
        setScaleX(floatValue);
        setScaleY(this.mCurrentScale);
    }

    private void performTouchEndAnim() {
        ValueAnimator valueAnimator = this.mAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mAnimator.cancel();
        }
        PropertyValuesHolder widthHolder = PropertyValuesHolder.ofFloat("scaleHolder", this.mCurrentScale, 1.0f);
        ValueAnimator ofPropertyValuesHolder = ValueAnimator.ofPropertyValuesHolder(widthHolder);
        this.mAnimator = ofPropertyValuesHolder;
        ofPropertyValuesHolder.setInterpolator(this.mTouchEndInterpolator);
        this.mAnimator.setDuration(300L);
        this.mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.oplus.widget.OplusFadeBackImageView$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                OplusFadeBackImageView.this.lambda$performTouchEndAnim$1(valueAnimator2);
            }
        });
        this.mAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performTouchEndAnim$1(ValueAnimator animation) {
        float floatValue = ((Float) animation.getAnimatedValue("scaleHolder")).floatValue();
        this.mCurrentScale = floatValue;
        setScaleX(floatValue);
        setScaleY(this.mCurrentScale);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ValueAnimator valueAnimator = this.mAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mAnimator.cancel();
            setScaleX(1.0f);
            setScaleY(1.0f);
        }
    }
}
