package com.google.android.material.behavior;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import p3.AnimationUtils;

/* loaded from: classes.dex */
public class HideBottomViewOnScrollBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    /* renamed from: a, reason: collision with root package name */
    private int f8316a;

    /* renamed from: b, reason: collision with root package name */
    private int f8317b;

    /* renamed from: c, reason: collision with root package name */
    private int f8318c;

    /* renamed from: d, reason: collision with root package name */
    private ViewPropertyAnimator f8319d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a extends AnimatorListenerAdapter {
        a() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            HideBottomViewOnScrollBehavior.this.f8319d = null;
        }
    }

    public HideBottomViewOnScrollBehavior() {
        this.f8316a = 0;
        this.f8317b = 2;
        this.f8318c = 0;
    }

    private void e(V v7, int i10, long j10, TimeInterpolator timeInterpolator) {
        this.f8319d = v7.animate().translationY(i10).setInterpolator(timeInterpolator).setDuration(j10).setListener(new a());
    }

    public boolean f() {
        return this.f8317b == 1;
    }

    public boolean g() {
        return this.f8317b == 2;
    }

    public void h(V v7, int i10) {
        this.f8318c = i10;
        if (this.f8317b == 1) {
            v7.setTranslationY(this.f8316a + i10);
        }
    }

    public void i(V v7) {
        j(v7, true);
    }

    public void j(V v7, boolean z10) {
        if (f()) {
            return;
        }
        ViewPropertyAnimator viewPropertyAnimator = this.f8319d;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
            v7.clearAnimation();
        }
        this.f8317b = 1;
        int i10 = this.f8316a + this.f8318c;
        if (z10) {
            e(v7, i10, 175L, AnimationUtils.f16557c);
        } else {
            v7.setTranslationY(i10);
        }
    }

    public void k(V v7) {
        l(v7, true);
    }

    public void l(V v7, boolean z10) {
        if (g()) {
            return;
        }
        ViewPropertyAnimator viewPropertyAnimator = this.f8319d;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
            v7.clearAnimation();
        }
        this.f8317b = 2;
        if (z10) {
            e(v7, 0, 225L, AnimationUtils.f16558d);
        } else {
            v7.setTranslationY(0);
        }
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, V v7, int i10) {
        this.f8316a = v7.getMeasuredHeight() + ((ViewGroup.MarginLayoutParams) v7.getLayoutParams()).bottomMargin;
        return super.onLayoutChild(coordinatorLayout, v7, i10);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, V v7, View view, int i10, int i11, int i12, int i13, int i14, int[] iArr) {
        if (i11 > 0) {
            i(v7);
        } else if (i11 < 0) {
            k(v7);
        }
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V v7, View view, View view2, int i10, int i11) {
        return i10 == 2;
    }

    public HideBottomViewOnScrollBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f8316a = 0;
        this.f8317b = 2;
        this.f8318c = 0;
    }
}
