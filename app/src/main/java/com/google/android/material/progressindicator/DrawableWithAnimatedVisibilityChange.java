package com.google.android.material.progressindicator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.Property;
import g0.Animatable2Compat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import p3.AnimationUtils;
import y3.AnimatorDurationScaleProvider;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: DrawableWithAnimatedVisibilityChange.java */
/* renamed from: com.google.android.material.progressindicator.e, reason: use source file name */
/* loaded from: classes.dex */
public abstract class DrawableWithAnimatedVisibilityChange extends Drawable implements Animatable {

    /* renamed from: s, reason: collision with root package name */
    private static final Property<DrawableWithAnimatedVisibilityChange, Float> f9092s = new c(Float.class, "growFraction");

    /* renamed from: e, reason: collision with root package name */
    final Context f9093e;

    /* renamed from: f, reason: collision with root package name */
    final BaseProgressIndicatorSpec f9094f;

    /* renamed from: h, reason: collision with root package name */
    private ValueAnimator f9096h;

    /* renamed from: i, reason: collision with root package name */
    private ValueAnimator f9097i;

    /* renamed from: j, reason: collision with root package name */
    private boolean f9098j;

    /* renamed from: k, reason: collision with root package name */
    private boolean f9099k;

    /* renamed from: l, reason: collision with root package name */
    private float f9100l;

    /* renamed from: m, reason: collision with root package name */
    private List<Animatable2Compat> f9101m;

    /* renamed from: n, reason: collision with root package name */
    private Animatable2Compat f9102n;

    /* renamed from: o, reason: collision with root package name */
    private boolean f9103o;

    /* renamed from: p, reason: collision with root package name */
    private float f9104p;

    /* renamed from: r, reason: collision with root package name */
    private int f9106r;

    /* renamed from: q, reason: collision with root package name */
    final Paint f9105q = new Paint();

    /* renamed from: g, reason: collision with root package name */
    AnimatorDurationScaleProvider f9095g = new AnimatorDurationScaleProvider();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DrawableWithAnimatedVisibilityChange.java */
    /* renamed from: com.google.android.material.progressindicator.e$a */
    /* loaded from: classes.dex */
    public class a extends AnimatorListenerAdapter {
        a() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            super.onAnimationStart(animator);
            DrawableWithAnimatedVisibilityChange.this.e();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DrawableWithAnimatedVisibilityChange.java */
    /* renamed from: com.google.android.material.progressindicator.e$b */
    /* loaded from: classes.dex */
    public class b extends AnimatorListenerAdapter {
        b() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            DrawableWithAnimatedVisibilityChange.super.setVisible(false, false);
            DrawableWithAnimatedVisibilityChange.this.d();
        }
    }

    /* compiled from: DrawableWithAnimatedVisibilityChange.java */
    /* renamed from: com.google.android.material.progressindicator.e$c */
    /* loaded from: classes.dex */
    class c extends Property<DrawableWithAnimatedVisibilityChange, Float> {
        c(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Float get(DrawableWithAnimatedVisibilityChange drawableWithAnimatedVisibilityChange) {
            return Float.valueOf(drawableWithAnimatedVisibilityChange.g());
        }

        @Override // android.util.Property
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void set(DrawableWithAnimatedVisibilityChange drawableWithAnimatedVisibilityChange, Float f10) {
            drawableWithAnimatedVisibilityChange.m(f10.floatValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DrawableWithAnimatedVisibilityChange(Context context, BaseProgressIndicatorSpec baseProgressIndicatorSpec) {
        this.f9093e = context;
        this.f9094f = baseProgressIndicatorSpec;
        setAlpha(255);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        Animatable2Compat animatable2Compat = this.f9102n;
        if (animatable2Compat != null) {
            animatable2Compat.a(this);
        }
        List<Animatable2Compat> list = this.f9101m;
        if (list == null || this.f9103o) {
            return;
        }
        Iterator<Animatable2Compat> it = list.iterator();
        while (it.hasNext()) {
            it.next().a(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        Animatable2Compat animatable2Compat = this.f9102n;
        if (animatable2Compat != null) {
            animatable2Compat.b(this);
        }
        List<Animatable2Compat> list = this.f9101m;
        if (list == null || this.f9103o) {
            return;
        }
        Iterator<Animatable2Compat> it = list.iterator();
        while (it.hasNext()) {
            it.next().b(this);
        }
    }

    private void f(ValueAnimator... valueAnimatorArr) {
        boolean z10 = this.f9103o;
        this.f9103o = true;
        for (ValueAnimator valueAnimator : valueAnimatorArr) {
            valueAnimator.end();
        }
        this.f9103o = z10;
    }

    private void k() {
        if (this.f9096h == null) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, f9092s, 0.0f, 1.0f);
            this.f9096h = ofFloat;
            ofFloat.setDuration(500L);
            this.f9096h.setInterpolator(AnimationUtils.f16556b);
            o(this.f9096h);
        }
        if (this.f9097i == null) {
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, f9092s, 1.0f, 0.0f);
            this.f9097i = ofFloat2;
            ofFloat2.setDuration(500L);
            this.f9097i.setInterpolator(AnimationUtils.f16556b);
            n(this.f9097i);
        }
    }

    private void n(ValueAnimator valueAnimator) {
        ValueAnimator valueAnimator2 = this.f9097i;
        if (valueAnimator2 != null && valueAnimator2.isRunning()) {
            throw new IllegalArgumentException("Cannot set hideAnimator while the current hideAnimator is running.");
        }
        this.f9097i = valueAnimator;
        valueAnimator.addListener(new b());
    }

    private void o(ValueAnimator valueAnimator) {
        ValueAnimator valueAnimator2 = this.f9096h;
        if (valueAnimator2 != null && valueAnimator2.isRunning()) {
            throw new IllegalArgumentException("Cannot set showAnimator while the current showAnimator is running.");
        }
        this.f9096h = valueAnimator;
        valueAnimator.addListener(new a());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float g() {
        if (!this.f9094f.b() && !this.f9094f.a()) {
            return 1.0f;
        }
        if (!this.f9099k && !this.f9098j) {
            return this.f9104p;
        }
        return this.f9100l;
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.f9106r;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    public boolean h() {
        return p(false, false, false);
    }

    public boolean i() {
        ValueAnimator valueAnimator = this.f9097i;
        return (valueAnimator != null && valueAnimator.isRunning()) || this.f9099k;
    }

    public boolean isRunning() {
        return j() || i();
    }

    public boolean j() {
        ValueAnimator valueAnimator = this.f9096h;
        return (valueAnimator != null && valueAnimator.isRunning()) || this.f9098j;
    }

    public void l(Animatable2Compat animatable2Compat) {
        if (this.f9101m == null) {
            this.f9101m = new ArrayList();
        }
        if (this.f9101m.contains(animatable2Compat)) {
            return;
        }
        this.f9101m.add(animatable2Compat);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void m(float f10) {
        if (this.f9104p != f10) {
            this.f9104p = f10;
            invalidateSelf();
        }
    }

    public boolean p(boolean z10, boolean z11, boolean z12) {
        return q(z10, z11, z12 && this.f9095g.a(this.f9093e.getContentResolver()) > 0.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean q(boolean z10, boolean z11, boolean z12) {
        k();
        if (!isVisible() && !z10) {
            return false;
        }
        ValueAnimator valueAnimator = z10 ? this.f9096h : this.f9097i;
        if (!z12) {
            if (valueAnimator.isRunning()) {
                valueAnimator.end();
            } else {
                f(valueAnimator);
            }
            return super.setVisible(z10, false);
        }
        if (z12 && valueAnimator.isRunning()) {
            return false;
        }
        boolean z13 = !z10 || super.setVisible(z10, false);
        if (!(z10 ? this.f9094f.b() : this.f9094f.a())) {
            f(valueAnimator);
            return z13;
        }
        if (!z11 && valueAnimator.isPaused()) {
            valueAnimator.resume();
        } else {
            valueAnimator.start();
        }
        return z13;
    }

    public boolean r(Animatable2Compat animatable2Compat) {
        List<Animatable2Compat> list = this.f9101m;
        if (list == null || !list.contains(animatable2Compat)) {
            return false;
        }
        this.f9101m.remove(animatable2Compat);
        if (!this.f9101m.isEmpty()) {
            return true;
        }
        this.f9101m = null;
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i10) {
        this.f9106r = i10;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.f9105q.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean setVisible(boolean z10, boolean z11) {
        return p(z10, z11, true);
    }

    public void start() {
        q(true, true, false);
    }

    public void stop() {
        q(false, true, false);
    }
}
