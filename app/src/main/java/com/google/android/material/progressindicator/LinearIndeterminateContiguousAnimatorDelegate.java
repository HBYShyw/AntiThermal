package com.google.android.material.progressindicator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.util.Property;
import g0.Animatable2Compat;
import java.util.Arrays;
import r3.MaterialColors;
import v.FastOutSlowInInterpolator;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: LinearIndeterminateContiguousAnimatorDelegate.java */
/* renamed from: com.google.android.material.progressindicator.j, reason: use source file name */
/* loaded from: classes.dex */
public final class LinearIndeterminateContiguousAnimatorDelegate extends IndeterminateAnimatorDelegate<ObjectAnimator> {

    /* renamed from: j, reason: collision with root package name */
    private static final Property<LinearIndeterminateContiguousAnimatorDelegate, Float> f9119j = new b(Float.class, "animationFraction");

    /* renamed from: d, reason: collision with root package name */
    private ObjectAnimator f9120d;

    /* renamed from: e, reason: collision with root package name */
    private FastOutSlowInInterpolator f9121e;

    /* renamed from: f, reason: collision with root package name */
    private final BaseProgressIndicatorSpec f9122f;

    /* renamed from: g, reason: collision with root package name */
    private int f9123g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f9124h;

    /* renamed from: i, reason: collision with root package name */
    private float f9125i;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: LinearIndeterminateContiguousAnimatorDelegate.java */
    /* renamed from: com.google.android.material.progressindicator.j$a */
    /* loaded from: classes.dex */
    public class a extends AnimatorListenerAdapter {
        a() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
            super.onAnimationRepeat(animator);
            LinearIndeterminateContiguousAnimatorDelegate linearIndeterminateContiguousAnimatorDelegate = LinearIndeterminateContiguousAnimatorDelegate.this;
            linearIndeterminateContiguousAnimatorDelegate.f9123g = (linearIndeterminateContiguousAnimatorDelegate.f9123g + 1) % LinearIndeterminateContiguousAnimatorDelegate.this.f9122f.f9063c.length;
            LinearIndeterminateContiguousAnimatorDelegate.this.f9124h = true;
        }
    }

    /* compiled from: LinearIndeterminateContiguousAnimatorDelegate.java */
    /* renamed from: com.google.android.material.progressindicator.j$b */
    /* loaded from: classes.dex */
    class b extends Property<LinearIndeterminateContiguousAnimatorDelegate, Float> {
        b(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Float get(LinearIndeterminateContiguousAnimatorDelegate linearIndeterminateContiguousAnimatorDelegate) {
            return Float.valueOf(linearIndeterminateContiguousAnimatorDelegate.n());
        }

        @Override // android.util.Property
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void set(LinearIndeterminateContiguousAnimatorDelegate linearIndeterminateContiguousAnimatorDelegate, Float f10) {
            linearIndeterminateContiguousAnimatorDelegate.r(f10.floatValue());
        }
    }

    public LinearIndeterminateContiguousAnimatorDelegate(LinearProgressIndicatorSpec linearProgressIndicatorSpec) {
        super(3);
        this.f9123g = 1;
        this.f9122f = linearProgressIndicatorSpec;
        this.f9121e = new FastOutSlowInInterpolator();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float n() {
        return this.f9125i;
    }

    private void o() {
        if (this.f9120d == null) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, f9119j, 0.0f, 1.0f);
            this.f9120d = ofFloat;
            ofFloat.setDuration(333L);
            this.f9120d.setInterpolator(null);
            this.f9120d.setRepeatCount(-1);
            this.f9120d.addListener(new a());
        }
    }

    private void p() {
        if (!this.f9124h || this.f9112b[3] >= 1.0f) {
            return;
        }
        int[] iArr = this.f9113c;
        iArr[2] = iArr[1];
        iArr[1] = iArr[0];
        iArr[0] = MaterialColors.a(this.f9122f.f9063c[this.f9123g], this.f9111a.getAlpha());
        this.f9124h = false;
    }

    private void s(int i10) {
        this.f9112b[0] = 0.0f;
        float b10 = b(i10, 0, 667);
        float[] fArr = this.f9112b;
        float interpolation = this.f9121e.getInterpolation(b10);
        fArr[2] = interpolation;
        fArr[1] = interpolation;
        float[] fArr2 = this.f9112b;
        float interpolation2 = this.f9121e.getInterpolation(b10 + 0.49925038f);
        fArr2[4] = interpolation2;
        fArr2[3] = interpolation2;
        this.f9112b[5] = 1.0f;
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void a() {
        ObjectAnimator objectAnimator = this.f9120d;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void c() {
        q();
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void d(Animatable2Compat animatable2Compat) {
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void f() {
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void g() {
        o();
        q();
        this.f9120d.start();
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void h() {
    }

    void q() {
        this.f9124h = true;
        this.f9123g = 1;
        Arrays.fill(this.f9113c, MaterialColors.a(this.f9122f.f9063c[0], this.f9111a.getAlpha()));
    }

    void r(float f10) {
        this.f9125i = f10;
        s((int) (f10 * 333.0f));
        p();
        this.f9111a.invalidateSelf();
    }
}
