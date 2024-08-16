package com.google.android.material.progressindicator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Property;
import android.view.animation.Interpolator;
import com.google.android.material.R$animator;
import g0.Animatable2Compat;
import g0.AnimationUtilsCompat;
import java.util.Arrays;
import r3.MaterialColors;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: LinearIndeterminateDisjointAnimatorDelegate.java */
/* renamed from: com.google.android.material.progressindicator.k, reason: use source file name */
/* loaded from: classes.dex */
public final class LinearIndeterminateDisjointAnimatorDelegate extends IndeterminateAnimatorDelegate<ObjectAnimator> {

    /* renamed from: l, reason: collision with root package name */
    private static final int[] f9127l = {533, 567, 850, 750};

    /* renamed from: m, reason: collision with root package name */
    private static final int[] f9128m = {1267, 1000, 333, 0};

    /* renamed from: n, reason: collision with root package name */
    private static final Property<LinearIndeterminateDisjointAnimatorDelegate, Float> f9129n = new c(Float.class, "animationFraction");

    /* renamed from: d, reason: collision with root package name */
    private ObjectAnimator f9130d;

    /* renamed from: e, reason: collision with root package name */
    private ObjectAnimator f9131e;

    /* renamed from: f, reason: collision with root package name */
    private final Interpolator[] f9132f;

    /* renamed from: g, reason: collision with root package name */
    private final BaseProgressIndicatorSpec f9133g;

    /* renamed from: h, reason: collision with root package name */
    private int f9134h;

    /* renamed from: i, reason: collision with root package name */
    private boolean f9135i;

    /* renamed from: j, reason: collision with root package name */
    private float f9136j;

    /* renamed from: k, reason: collision with root package name */
    Animatable2Compat f9137k;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: LinearIndeterminateDisjointAnimatorDelegate.java */
    /* renamed from: com.google.android.material.progressindicator.k$a */
    /* loaded from: classes.dex */
    public class a extends AnimatorListenerAdapter {
        a() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
            super.onAnimationRepeat(animator);
            LinearIndeterminateDisjointAnimatorDelegate linearIndeterminateDisjointAnimatorDelegate = LinearIndeterminateDisjointAnimatorDelegate.this;
            linearIndeterminateDisjointAnimatorDelegate.f9134h = (linearIndeterminateDisjointAnimatorDelegate.f9134h + 1) % LinearIndeterminateDisjointAnimatorDelegate.this.f9133g.f9063c.length;
            LinearIndeterminateDisjointAnimatorDelegate.this.f9135i = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: LinearIndeterminateDisjointAnimatorDelegate.java */
    /* renamed from: com.google.android.material.progressindicator.k$b */
    /* loaded from: classes.dex */
    public class b extends AnimatorListenerAdapter {
        b() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            LinearIndeterminateDisjointAnimatorDelegate.this.a();
            LinearIndeterminateDisjointAnimatorDelegate linearIndeterminateDisjointAnimatorDelegate = LinearIndeterminateDisjointAnimatorDelegate.this;
            Animatable2Compat animatable2Compat = linearIndeterminateDisjointAnimatorDelegate.f9137k;
            if (animatable2Compat != null) {
                animatable2Compat.a(linearIndeterminateDisjointAnimatorDelegate.f9111a);
            }
        }
    }

    /* compiled from: LinearIndeterminateDisjointAnimatorDelegate.java */
    /* renamed from: com.google.android.material.progressindicator.k$c */
    /* loaded from: classes.dex */
    class c extends Property<LinearIndeterminateDisjointAnimatorDelegate, Float> {
        c(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Float get(LinearIndeterminateDisjointAnimatorDelegate linearIndeterminateDisjointAnimatorDelegate) {
            return Float.valueOf(linearIndeterminateDisjointAnimatorDelegate.n());
        }

        @Override // android.util.Property
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void set(LinearIndeterminateDisjointAnimatorDelegate linearIndeterminateDisjointAnimatorDelegate, Float f10) {
            linearIndeterminateDisjointAnimatorDelegate.r(f10.floatValue());
        }
    }

    public LinearIndeterminateDisjointAnimatorDelegate(Context context, LinearProgressIndicatorSpec linearProgressIndicatorSpec) {
        super(2);
        this.f9134h = 0;
        this.f9137k = null;
        this.f9133g = linearProgressIndicatorSpec;
        this.f9132f = new Interpolator[]{AnimationUtilsCompat.a(context, R$animator.linear_indeterminate_line1_head_interpolator), AnimationUtilsCompat.a(context, R$animator.linear_indeterminate_line1_tail_interpolator), AnimationUtilsCompat.a(context, R$animator.linear_indeterminate_line2_head_interpolator), AnimationUtilsCompat.a(context, R$animator.linear_indeterminate_line2_tail_interpolator)};
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float n() {
        return this.f9136j;
    }

    private void o() {
        if (this.f9130d == null) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, f9129n, 0.0f, 1.0f);
            this.f9130d = ofFloat;
            ofFloat.setDuration(1800L);
            this.f9130d.setInterpolator(null);
            this.f9130d.setRepeatCount(-1);
            this.f9130d.addListener(new a());
        }
        if (this.f9131e == null) {
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, f9129n, 1.0f);
            this.f9131e = ofFloat2;
            ofFloat2.setDuration(1800L);
            this.f9131e.setInterpolator(null);
            this.f9131e.addListener(new b());
        }
    }

    private void p() {
        if (this.f9135i) {
            Arrays.fill(this.f9113c, MaterialColors.a(this.f9133g.f9063c[this.f9134h], this.f9111a.getAlpha()));
            this.f9135i = false;
        }
    }

    private void s(int i10) {
        for (int i11 = 0; i11 < 4; i11++) {
            this.f9112b[i11] = Math.max(0.0f, Math.min(1.0f, this.f9132f[i11].getInterpolation(b(i10, f9128m[i11], f9127l[i11]))));
        }
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void a() {
        ObjectAnimator objectAnimator = this.f9130d;
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
        this.f9137k = animatable2Compat;
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void f() {
        ObjectAnimator objectAnimator = this.f9131e;
        if (objectAnimator == null || objectAnimator.isRunning()) {
            return;
        }
        a();
        if (this.f9111a.isVisible()) {
            this.f9131e.setFloatValues(this.f9136j, 1.0f);
            this.f9131e.setDuration((1.0f - this.f9136j) * 1800.0f);
            this.f9131e.start();
        }
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void g() {
        o();
        q();
        this.f9130d.start();
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void h() {
        this.f9137k = null;
    }

    void q() {
        this.f9134h = 0;
        int a10 = MaterialColors.a(this.f9133g.f9063c[0], this.f9111a.getAlpha());
        int[] iArr = this.f9113c;
        iArr[0] = a10;
        iArr[1] = a10;
    }

    void r(float f10) {
        this.f9136j = f10;
        s((int) (f10 * 1800.0f));
        p();
        this.f9111a.invalidateSelf();
    }
}
