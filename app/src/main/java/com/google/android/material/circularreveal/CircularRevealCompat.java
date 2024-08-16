package com.google.android.material.circularreveal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.util.Property;
import android.view.View;
import android.view.ViewAnimationUtils;
import com.google.android.material.circularreveal.CircularRevealWidget;

/* compiled from: CircularRevealCompat.java */
/* renamed from: com.google.android.material.circularreveal.a, reason: use source file name */
/* loaded from: classes.dex */
public final class CircularRevealCompat {

    /* compiled from: CircularRevealCompat.java */
    /* renamed from: com.google.android.material.circularreveal.a$a */
    /* loaded from: classes.dex */
    class a extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ CircularRevealWidget f8620a;

        a(CircularRevealWidget circularRevealWidget) {
            this.f8620a = circularRevealWidget;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f8620a.b();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            this.f8620a.a();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static Animator a(CircularRevealWidget circularRevealWidget, float f10, float f11, float f12) {
        ObjectAnimator ofObject = ObjectAnimator.ofObject(circularRevealWidget, (Property<CircularRevealWidget, V>) CircularRevealWidget.c.f8633a, (TypeEvaluator) CircularRevealWidget.b.f8631b, (Object[]) new CircularRevealWidget.e[]{new CircularRevealWidget.e(f10, f11, f12)});
        CircularRevealWidget.e revealInfo = circularRevealWidget.getRevealInfo();
        if (revealInfo != null) {
            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal((View) circularRevealWidget, (int) f10, (int) f11, revealInfo.f8637c, f12);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ofObject, createCircularReveal);
            return animatorSet;
        }
        throw new IllegalStateException("Caller must set a non-null RevealInfo before calling this.");
    }

    public static Animator.AnimatorListener b(CircularRevealWidget circularRevealWidget) {
        return new a(circularRevealWidget);
    }
}
