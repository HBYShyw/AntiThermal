package com.google.android.material.progressindicator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.util.Property;
import g0.Animatable2Compat;
import p3.ArgbEvaluatorCompat;
import r3.MaterialColors;
import v.FastOutSlowInInterpolator;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: CircularIndeterminateAnimatorDelegate.java */
/* renamed from: com.google.android.material.progressindicator.c, reason: use source file name */
/* loaded from: classes.dex */
public final class CircularIndeterminateAnimatorDelegate extends IndeterminateAnimatorDelegate<ObjectAnimator> {

    /* renamed from: l, reason: collision with root package name */
    private static final int[] f9071l = {0, 1350, 2700, 4050};

    /* renamed from: m, reason: collision with root package name */
    private static final int[] f9072m = {667, 2017, 3367, 4717};

    /* renamed from: n, reason: collision with root package name */
    private static final int[] f9073n = {1000, 2350, 3700, 5050};

    /* renamed from: o, reason: collision with root package name */
    private static final Property<CircularIndeterminateAnimatorDelegate, Float> f9074o = new c(Float.class, "animationFraction");

    /* renamed from: p, reason: collision with root package name */
    private static final Property<CircularIndeterminateAnimatorDelegate, Float> f9075p = new d(Float.class, "completeEndFraction");

    /* renamed from: d, reason: collision with root package name */
    private ObjectAnimator f9076d;

    /* renamed from: e, reason: collision with root package name */
    private ObjectAnimator f9077e;

    /* renamed from: f, reason: collision with root package name */
    private final FastOutSlowInInterpolator f9078f;

    /* renamed from: g, reason: collision with root package name */
    private final BaseProgressIndicatorSpec f9079g;

    /* renamed from: h, reason: collision with root package name */
    private int f9080h;

    /* renamed from: i, reason: collision with root package name */
    private float f9081i;

    /* renamed from: j, reason: collision with root package name */
    private float f9082j;

    /* renamed from: k, reason: collision with root package name */
    Animatable2Compat f9083k;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: CircularIndeterminateAnimatorDelegate.java */
    /* renamed from: com.google.android.material.progressindicator.c$a */
    /* loaded from: classes.dex */
    public class a extends AnimatorListenerAdapter {
        a() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
            super.onAnimationRepeat(animator);
            CircularIndeterminateAnimatorDelegate circularIndeterminateAnimatorDelegate = CircularIndeterminateAnimatorDelegate.this;
            circularIndeterminateAnimatorDelegate.f9080h = (circularIndeterminateAnimatorDelegate.f9080h + 4) % CircularIndeterminateAnimatorDelegate.this.f9079g.f9063c.length;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: CircularIndeterminateAnimatorDelegate.java */
    /* renamed from: com.google.android.material.progressindicator.c$b */
    /* loaded from: classes.dex */
    public class b extends AnimatorListenerAdapter {
        b() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            CircularIndeterminateAnimatorDelegate.this.a();
            CircularIndeterminateAnimatorDelegate circularIndeterminateAnimatorDelegate = CircularIndeterminateAnimatorDelegate.this;
            Animatable2Compat animatable2Compat = circularIndeterminateAnimatorDelegate.f9083k;
            if (animatable2Compat != null) {
                animatable2Compat.a(circularIndeterminateAnimatorDelegate.f9111a);
            }
        }
    }

    /* compiled from: CircularIndeterminateAnimatorDelegate.java */
    /* renamed from: com.google.android.material.progressindicator.c$c */
    /* loaded from: classes.dex */
    class c extends Property<CircularIndeterminateAnimatorDelegate, Float> {
        c(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Float get(CircularIndeterminateAnimatorDelegate circularIndeterminateAnimatorDelegate) {
            return Float.valueOf(circularIndeterminateAnimatorDelegate.o());
        }

        @Override // android.util.Property
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void set(CircularIndeterminateAnimatorDelegate circularIndeterminateAnimatorDelegate, Float f10) {
            circularIndeterminateAnimatorDelegate.t(f10.floatValue());
        }
    }

    /* compiled from: CircularIndeterminateAnimatorDelegate.java */
    /* renamed from: com.google.android.material.progressindicator.c$d */
    /* loaded from: classes.dex */
    class d extends Property<CircularIndeterminateAnimatorDelegate, Float> {
        d(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Float get(CircularIndeterminateAnimatorDelegate circularIndeterminateAnimatorDelegate) {
            return Float.valueOf(circularIndeterminateAnimatorDelegate.p());
        }

        @Override // android.util.Property
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void set(CircularIndeterminateAnimatorDelegate circularIndeterminateAnimatorDelegate, Float f10) {
            circularIndeterminateAnimatorDelegate.u(f10.floatValue());
        }
    }

    public CircularIndeterminateAnimatorDelegate(CircularProgressIndicatorSpec circularProgressIndicatorSpec) {
        super(1);
        this.f9080h = 0;
        this.f9083k = null;
        this.f9079g = circularProgressIndicatorSpec;
        this.f9078f = new FastOutSlowInInterpolator();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float o() {
        return this.f9081i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float p() {
        return this.f9082j;
    }

    private void q() {
        if (this.f9076d == null) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, f9074o, 0.0f, 1.0f);
            this.f9076d = ofFloat;
            ofFloat.setDuration(5400L);
            this.f9076d.setInterpolator(null);
            this.f9076d.setRepeatCount(-1);
            this.f9076d.addListener(new a());
        }
        if (this.f9077e == null) {
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, f9075p, 0.0f, 1.0f);
            this.f9077e = ofFloat2;
            ofFloat2.setDuration(333L);
            this.f9077e.setInterpolator(this.f9078f);
            this.f9077e.addListener(new b());
        }
    }

    private void r(int i10) {
        for (int i11 = 0; i11 < 4; i11++) {
            float b10 = b(i10, f9073n[i11], 333);
            if (b10 >= 0.0f && b10 <= 1.0f) {
                int i12 = i11 + this.f9080h;
                int[] iArr = this.f9079g.f9063c;
                int length = i12 % iArr.length;
                int length2 = (length + 1) % iArr.length;
                this.f9113c[0] = ArgbEvaluatorCompat.b().evaluate(this.f9078f.getInterpolation(b10), Integer.valueOf(MaterialColors.a(iArr[length], this.f9111a.getAlpha())), Integer.valueOf(MaterialColors.a(this.f9079g.f9063c[length2], this.f9111a.getAlpha()))).intValue();
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void u(float f10) {
        this.f9082j = f10;
    }

    private void v(int i10) {
        float[] fArr = this.f9112b;
        float f10 = this.f9081i;
        fArr[0] = (f10 * 1520.0f) - 20.0f;
        fArr[1] = f10 * 1520.0f;
        for (int i11 = 0; i11 < 4; i11++) {
            float b10 = b(i10, f9071l[i11], 667);
            float[] fArr2 = this.f9112b;
            fArr2[1] = fArr2[1] + (this.f9078f.getInterpolation(b10) * 250.0f);
            float b11 = b(i10, f9072m[i11], 667);
            float[] fArr3 = this.f9112b;
            fArr3[0] = fArr3[0] + (this.f9078f.getInterpolation(b11) * 250.0f);
        }
        float[] fArr4 = this.f9112b;
        fArr4[0] = fArr4[0] + ((fArr4[1] - fArr4[0]) * this.f9082j);
        fArr4[0] = fArr4[0] / 360.0f;
        fArr4[1] = fArr4[1] / 360.0f;
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    void a() {
        ObjectAnimator objectAnimator = this.f9076d;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void c() {
        s();
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void d(Animatable2Compat animatable2Compat) {
        this.f9083k = animatable2Compat;
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    void f() {
        ObjectAnimator objectAnimator = this.f9077e;
        if (objectAnimator == null || objectAnimator.isRunning()) {
            return;
        }
        if (this.f9111a.isVisible()) {
            this.f9077e.start();
        } else {
            a();
        }
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    void g() {
        q();
        s();
        this.f9076d.start();
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void h() {
        this.f9083k = null;
    }

    void s() {
        this.f9080h = 0;
        this.f9113c[0] = MaterialColors.a(this.f9079g.f9063c[0], this.f9111a.getAlpha());
        this.f9082j = 0.0f;
    }

    void t(float f10) {
        this.f9081i = f10;
        int i10 = (int) (f10 * 5400.0f);
        v(i10);
        r(i10);
        this.f9111a.invalidateSelf();
    }
}
