package com.coui.appcompat.floatingactionbutton;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.PathInterpolator;
import m1.COUIMoveEaseInterpolator;

/* compiled from: COUIFABPressFeedbackUtil.java */
/* renamed from: com.coui.appcompat.floatingactionbutton.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUIFABPressFeedbackUtil {

    /* renamed from: a, reason: collision with root package name */
    private static final PathInterpolator f6059a = new COUIMoveEaseInterpolator();

    public static COUIFloatingButtonTouchAnimation a(View view) {
        if (view != null) {
            COUIFloatingButtonTouchAnimation cOUIFloatingButtonTouchAnimation = new COUIFloatingButtonTouchAnimation(1.0f, 0.9f, view.getWidth() / 2.0f, view.getHeight() / 2.0f);
            cOUIFloatingButtonTouchAnimation.setDuration(200L);
            cOUIFloatingButtonTouchAnimation.setFillAfter(true);
            cOUIFloatingButtonTouchAnimation.setInterpolator(f6059a);
            cOUIFloatingButtonTouchAnimation.c(view);
            return cOUIFloatingButtonTouchAnimation;
        }
        throw new IllegalArgumentException("The given view is empty. Please provide a valid view.");
    }

    public static ValueAnimator b() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.9f);
        ofFloat.setDuration(200L);
        ofFloat.setInterpolator(f6059a);
        return ofFloat;
    }

    public static COUIFloatingButtonTouchAnimation c(View view, float f10) {
        if (view != null) {
            COUIFloatingButtonTouchAnimation cOUIFloatingButtonTouchAnimation = new COUIFloatingButtonTouchAnimation(f10, 1.0f, view.getWidth() / 2.0f, view.getHeight() / 2.0f);
            cOUIFloatingButtonTouchAnimation.setDuration(340L);
            cOUIFloatingButtonTouchAnimation.setFillAfter(true);
            cOUIFloatingButtonTouchAnimation.setInterpolator(f6059a);
            cOUIFloatingButtonTouchAnimation.c(view);
            return cOUIFloatingButtonTouchAnimation;
        }
        throw new IllegalArgumentException("The given view is empty. Please provide a valid view.");
    }
}
