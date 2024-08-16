package com.oplus.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;

/* loaded from: classes.dex */
public class OplusAnimatorWrapper {
    private final Animator mAnimation;
    private final OnSetValuesCallback mCallback;
    protected final Class<?> mTagClass = getClass();

    /* loaded from: classes.dex */
    public interface OnSetValuesCallback {
        float getEndValue(View view);

        float getPivotXValue(View view);

        float getPivotYValue(View view);

        float getStartValue(View view);

        void initialize(View view);
    }

    public OplusAnimatorWrapper(Animator animation, OnSetValuesCallback callback) {
        this.mAnimation = animation;
        this.mCallback = callback;
    }

    public Animator getAnimation() {
        return this.mAnimation;
    }

    public void initialize() {
        View target;
        if (this.mCallback == null || !(this.mAnimation instanceof ValueAnimator) || (target = getTarget()) == null) {
            return;
        }
        target.setVisibility(0);
        this.mCallback.initialize(target);
        target.setPivotX(this.mCallback.getPivotXValue(target));
        target.setPivotY(this.mCallback.getPivotYValue(target));
        float startValue = this.mCallback.getStartValue(target);
        float endValue = this.mCallback.getEndValue(target);
        ((ValueAnimator) this.mAnimation).setFloatValues(startValue, endValue);
    }

    private View getTarget() {
        Animator animator = this.mAnimation;
        if (animator instanceof ObjectAnimator) {
            Object target = ((ObjectAnimator) animator).getTarget();
            if (target instanceof View) {
                return (View) target;
            }
            return null;
        }
        return null;
    }

    /* loaded from: classes.dex */
    public static class OnSetValuesCallbackAdapter implements OnSetValuesCallback {
        @Override // com.oplus.animation.OplusAnimatorWrapper.OnSetValuesCallback
        public void initialize(View target) {
        }

        @Override // com.oplus.animation.OplusAnimatorWrapper.OnSetValuesCallback
        public float getPivotXValue(View target) {
            return target.getPivotX();
        }

        @Override // com.oplus.animation.OplusAnimatorWrapper.OnSetValuesCallback
        public float getPivotYValue(View target) {
            return target.getPivotY();
        }

        @Override // com.oplus.animation.OplusAnimatorWrapper.OnSetValuesCallback
        public float getStartValue(View target) {
            return 0.0f;
        }

        @Override // com.oplus.animation.OplusAnimatorWrapper.OnSetValuesCallback
        public float getEndValue(View target) {
            return 0.0f;
        }
    }
}
