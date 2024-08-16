package com.coui.appcompat.progressbar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import androidx.core.content.res.ResourcesCompat;
import com.support.appcompat.R$color;
import com.support.appcompat.R$dimen;
import m1.COUIInEaseInterpolator;
import m1.COUILinearInterpolator;
import m1.COUIMoveEaseInterpolator;
import m1.COUIOutEaseInterpolator;
import s.DynamicAnimation;
import s.FloatPropertyCompat;
import s.SpringAnimation;
import s.SpringForce;

/* compiled from: COUICircularProgressDrawable.java */
/* renamed from: com.coui.appcompat.progressbar.j, reason: use source file name */
/* loaded from: classes.dex */
public class COUICircularProgressDrawable extends Drawable {
    private static final Interpolator Y = new COUILinearInterpolator();
    private static final Interpolator Z = new COUIInEaseInterpolator();

    /* renamed from: a0, reason: collision with root package name */
    private static final Interpolator f7231a0 = new COUIOutEaseInterpolator();

    /* renamed from: b0, reason: collision with root package name */
    private static final Interpolator f7232b0 = new COUIMoveEaseInterpolator();

    /* renamed from: c0, reason: collision with root package name */
    private static final ArgbEvaluator f7233c0 = new ArgbEvaluator();

    /* renamed from: d0, reason: collision with root package name */
    private static final FloatPropertyCompat<COUICircularProgressDrawable> f7234d0 = new a("visualProgress");
    private float A;
    private float B;
    private float C;
    private float D;
    private View E;
    private Paint G;
    private Paint H;
    private Paint I;
    private SpringAnimation J;
    private AnimatorSet K;
    private AnimatorSet L;
    private AnimatorSet M;
    private AnimatorSet N;
    private ValueAnimator O;
    private ValueAnimator P;
    private ValueAnimator Q;
    private ValueAnimator R;
    private ValueAnimator S;
    private ValueAnimator T;
    private ValueAnimator U;
    private ValueAnimator V;
    private f W;
    private g X;

    /* renamed from: d, reason: collision with root package name */
    private int f7238d;

    /* renamed from: e, reason: collision with root package name */
    private int f7239e;

    /* renamed from: i, reason: collision with root package name */
    private h f7243i;

    /* renamed from: j, reason: collision with root package name */
    private h f7244j;

    /* renamed from: k, reason: collision with root package name */
    private float f7245k;

    /* renamed from: l, reason: collision with root package name */
    private int f7246l;

    /* renamed from: m, reason: collision with root package name */
    private float f7247m;

    /* renamed from: n, reason: collision with root package name */
    private float f7248n;

    /* renamed from: o, reason: collision with root package name */
    private float f7249o;

    /* renamed from: p, reason: collision with root package name */
    private float f7250p;

    /* renamed from: q, reason: collision with root package name */
    private float f7251q;

    /* renamed from: r, reason: collision with root package name */
    private float f7252r;

    /* renamed from: s, reason: collision with root package name */
    private float f7253s;

    /* renamed from: t, reason: collision with root package name */
    private float f7254t;

    /* renamed from: u, reason: collision with root package name */
    private float f7255u;

    /* renamed from: v, reason: collision with root package name */
    private float f7256v;

    /* renamed from: w, reason: collision with root package name */
    private float f7257w;

    /* renamed from: x, reason: collision with root package name */
    private float f7258x;

    /* renamed from: y, reason: collision with root package name */
    private float f7259y;

    /* renamed from: z, reason: collision with root package name */
    private float f7260z;

    /* renamed from: a, reason: collision with root package name */
    private int f7235a = 255;

    /* renamed from: b, reason: collision with root package name */
    private int f7236b = 255;

    /* renamed from: c, reason: collision with root package name */
    private int f7237c = 100;

    /* renamed from: f, reason: collision with root package name */
    private int f7240f = 0;

    /* renamed from: g, reason: collision with root package name */
    private int f7241g = 0;

    /* renamed from: h, reason: collision with root package name */
    private int f7242h = 0;
    private boolean F = false;

    /* compiled from: COUICircularProgressDrawable.java */
    /* renamed from: com.coui.appcompat.progressbar.j$a */
    /* loaded from: classes.dex */
    class a extends FloatPropertyCompat<COUICircularProgressDrawable> {
        a(String str) {
            super(str);
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public float a(COUICircularProgressDrawable cOUICircularProgressDrawable) {
            return cOUICircularProgressDrawable.r();
        }

        @Override // s.FloatPropertyCompat
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public void b(COUICircularProgressDrawable cOUICircularProgressDrawable, float f10) {
            cOUICircularProgressDrawable.Y(f10);
            cOUICircularProgressDrawable.K();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUICircularProgressDrawable.java */
    /* renamed from: com.coui.appcompat.progressbar.j$b */
    /* loaded from: classes.dex */
    public class b implements Animator.AnimatorListener {
        b() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            COUICircularProgressDrawable.this.F = false;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUICircularProgressDrawable.this.F = false;
            if (COUICircularProgressDrawable.this.X != null) {
                COUICircularProgressDrawable.this.X.a();
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUICircularProgressDrawable.this.F = true;
            if (COUICircularProgressDrawable.this.X != null) {
                COUICircularProgressDrawable.this.X.b();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUICircularProgressDrawable.java */
    /* renamed from: com.coui.appcompat.progressbar.j$c */
    /* loaded from: classes.dex */
    public class c implements Animator.AnimatorListener {
        c() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            COUICircularProgressDrawable.this.F = false;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUICircularProgressDrawable.this.F = false;
            if (COUICircularProgressDrawable.this.X != null) {
                COUICircularProgressDrawable.this.X.h();
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUICircularProgressDrawable.this.F = true;
            if (COUICircularProgressDrawable.this.X != null) {
                COUICircularProgressDrawable.this.X.d();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUICircularProgressDrawable.java */
    /* renamed from: com.coui.appcompat.progressbar.j$d */
    /* loaded from: classes.dex */
    public class d implements Animator.AnimatorListener {
        d() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            COUICircularProgressDrawable.this.F = false;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUICircularProgressDrawable.this.F = false;
            if (COUICircularProgressDrawable.this.X != null) {
                COUICircularProgressDrawable.this.X.f();
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUICircularProgressDrawable.this.F = true;
            if (COUICircularProgressDrawable.this.X != null) {
                COUICircularProgressDrawable.this.X.c();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUICircularProgressDrawable.java */
    /* renamed from: com.coui.appcompat.progressbar.j$e */
    /* loaded from: classes.dex */
    public class e implements Animator.AnimatorListener {
        e() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            COUICircularProgressDrawable.this.F = false;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUICircularProgressDrawable.this.F = false;
            if (COUICircularProgressDrawable.this.X != null) {
                COUICircularProgressDrawable.this.X.g();
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUICircularProgressDrawable.this.F = true;
            if (COUICircularProgressDrawable.this.X != null) {
                COUICircularProgressDrawable.this.X.e();
            }
        }
    }

    /* compiled from: COUICircularProgressDrawable.java */
    /* renamed from: com.coui.appcompat.progressbar.j$f */
    /* loaded from: classes.dex */
    public interface f {
        default void a(int i10) {
        }

        default void b(float f10) {
        }
    }

    /* compiled from: COUICircularProgressDrawable.java */
    /* renamed from: com.coui.appcompat.progressbar.j$g */
    /* loaded from: classes.dex */
    public interface g {
        default void a() {
        }

        default void b() {
        }

        default void c() {
        }

        default void d() {
        }

        default void e() {
        }

        default void f() {
        }

        default void g() {
        }

        default void h() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: COUICircularProgressDrawable.java */
    /* renamed from: com.coui.appcompat.progressbar.j$h */
    /* loaded from: classes.dex */
    public static class h {

        /* renamed from: e, reason: collision with root package name */
        private float f7269e;

        /* renamed from: f, reason: collision with root package name */
        private float f7270f;

        /* renamed from: j, reason: collision with root package name */
        private int f7274j;

        /* renamed from: k, reason: collision with root package name */
        private int f7275k;

        /* renamed from: g, reason: collision with root package name */
        private float f7271g = Float.MIN_VALUE;

        /* renamed from: h, reason: collision with root package name */
        private float f7272h = Float.MIN_VALUE;

        /* renamed from: a, reason: collision with root package name */
        private float f7265a = 0.0f;

        /* renamed from: b, reason: collision with root package name */
        private float f7266b = 0.0f;

        /* renamed from: c, reason: collision with root package name */
        private float f7267c = 0.0f;

        /* renamed from: d, reason: collision with root package name */
        private float f7268d = 0.0f;

        /* renamed from: i, reason: collision with root package name */
        private int f7273i = 0;

        h() {
        }

        public int a() {
            return this.f7274j;
        }

        public float b() {
            return this.f7265a;
        }

        public float c() {
            return this.f7266b;
        }

        public int d() {
            return this.f7273i;
        }

        public float e() {
            return this.f7270f;
        }

        public float f() {
            return this.f7269e;
        }

        public int g() {
            return this.f7275k;
        }

        public float h() {
            if (this.f7272h == Float.MIN_VALUE) {
                return this.f7268d;
            }
            return this.f7271g;
        }

        public float i() {
            float f10 = this.f7272h;
            return f10 == Float.MIN_VALUE ? this.f7267c : f10;
        }

        public float j() {
            return this.f7268d;
        }

        public float k() {
            return this.f7267c;
        }

        public void l(int i10) {
            this.f7274j = i10;
        }

        public void m(float f10) {
            this.f7265a = f10;
        }

        public void n(float f10) {
            this.f7266b = f10;
        }

        public void o(int i10) {
            this.f7273i = i10;
            this.f7274j = i10;
        }

        public void p(float f10) {
            if (f10 < 0.0f) {
                Log.w("COUICircularDrawable", "Progress bar outer diameter should be greater than 0 !");
            }
            this.f7270f = Math.max(0.0f, f10);
        }

        public void q(float f10) {
            if (f10 < 0.0f) {
                Log.w("COUICircularDrawable", "Progress bar stroke width should be greater than 0 !");
            }
            this.f7269e = Math.max(0.0f, f10);
        }

        public void r(int i10) {
            this.f7275k = i10;
        }

        public void s(float f10) {
            if (f10 < 0.0f) {
                Log.w("COUICircularDrawable", "Progress bar outer diameter should be greater than 0 !");
            }
            this.f7271g = Math.max(0.0f, f10);
        }

        public void t(float f10) {
            if (f10 < 0.0f) {
                Log.w("COUICircularDrawable", "Progress bar stroke width should be greater than 0 !");
            }
            this.f7272h = Math.max(0.0f, f10);
        }

        public void u(float f10) {
            if (f10 < 0.0f) {
                Log.w("COUICircularDrawable", "Progress bar outer diameter should be greater than 0 !");
            }
            this.f7268d = Math.max(0.0f, f10);
        }

        public void v(float f10) {
            if (f10 < 0.0f) {
                Log.w("COUICircularDrawable", "Progress bar stroke width should be greater than 0 !");
            }
            this.f7267c = Math.max(0.0f, f10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public COUICircularProgressDrawable(Context context) {
        t(context);
        v();
        s();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void A(ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        float f10 = 1.0f - animatedFraction;
        float h10 = this.f7244j.h() + ((this.f7244j.j() - this.f7244j.h()) * f10);
        float h11 = this.f7243i.h() + ((this.f7243i.j() - this.f7243i.h()) * f10);
        float i10 = this.f7244j.i() + ((this.f7244j.k() - this.f7244j.i()) * f10);
        float i11 = this.f7243i.i() + (f10 * (this.f7243i.k() - this.f7243i.i()));
        ArgbEvaluator argbEvaluator = f7233c0;
        int intValue = ((Integer) argbEvaluator.evaluate(animatedFraction, Integer.valueOf(this.f7243i.d()), Integer.valueOf(this.f7243i.g()))).intValue();
        int intValue2 = ((Integer) argbEvaluator.evaluate(animatedFraction, Integer.valueOf(this.f7244j.d()), Integer.valueOf(this.f7244j.g()))).intValue();
        this.f7244j.p(h10);
        this.f7244j.q(i10);
        this.f7244j.l(intValue2);
        this.f7243i.p(h11);
        this.f7243i.q(i11);
        this.f7243i.l(intValue);
        invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void B(ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        this.f7252r = (0.3f * animatedFraction) + 0.7f;
        this.f7241g = (int) (animatedFraction * 255.0f);
        invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void C(ValueAnimator valueAnimator) {
        this.f7236b = (int) ((1.0f - valueAnimator.getAnimatedFraction()) * 255.0f);
        invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void D(ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        this.f7240f = (int) (255.0f * animatedFraction);
        this.f7251q = (animatedFraction * 0.3f) + 0.7f;
        invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void E(ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        float h10 = this.f7244j.h() + ((this.f7244j.j() - this.f7244j.h()) * animatedFraction);
        float h11 = this.f7243i.h() + ((this.f7243i.j() - this.f7243i.h()) * animatedFraction);
        float i10 = this.f7244j.i() + ((this.f7244j.k() - this.f7244j.i()) * animatedFraction);
        float i11 = this.f7243i.i() + ((this.f7243i.k() - this.f7243i.i()) * animatedFraction);
        ArgbEvaluator argbEvaluator = f7233c0;
        int intValue = ((Integer) argbEvaluator.evaluate(animatedFraction, Integer.valueOf(this.f7243i.g()), Integer.valueOf(this.f7243i.d()))).intValue();
        int intValue2 = ((Integer) argbEvaluator.evaluate(animatedFraction, Integer.valueOf(this.f7244j.g()), Integer.valueOf(this.f7244j.d()))).intValue();
        this.f7244j.p(h10);
        this.f7244j.q(i10);
        this.f7244j.l(intValue2);
        this.f7243i.p(h11);
        this.f7243i.q(i11);
        this.f7243i.l(intValue);
        invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void F(ValueAnimator valueAnimator) {
        float animatedFraction = 1.0f - valueAnimator.getAnimatedFraction();
        this.f7252r = (0.3f * animatedFraction) + 0.7f;
        this.f7241g = (int) (animatedFraction * 255.0f);
        invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void G(ValueAnimator valueAnimator) {
        this.f7236b = (int) (valueAnimator.getAnimatedFraction() * 255.0f);
        invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void H(ValueAnimator valueAnimator) {
        this.f7240f = (int) ((1.0f - valueAnimator.getAnimatedFraction()) * 255.0f);
        this.f7251q = 1.0f;
        invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void I(DynamicAnimation dynamicAnimation, float f10, float f11) {
        invalidateSelf();
    }

    private void J() {
        f fVar = this.W;
        if (fVar != null) {
            fVar.a(this.f7246l);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void K() {
        f fVar = this.W;
        if (fVar != null) {
            fVar.b(this.f7245k);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void Y(float f10) {
        this.f7245k = f10;
        invalidateSelf();
    }

    private float n(float f10) {
        return (((int) ((f10 * 100.0f) / r2)) / 100.0f) * this.f7237c;
    }

    private void o(Canvas canvas) {
        int i10 = this.f7241g;
        if (i10 != 0) {
            canvas.saveLayerAlpha(0.0f, 0.0f, this.f7247m * 2.0f, this.f7248n * 2.0f, i10);
            float f10 = this.f7252r;
            canvas.scale(f10, f10, this.f7247m, this.f7248n);
            this.I.setColor(this.f7239e);
            float f11 = this.f7247m;
            float f12 = this.f7257w;
            float f13 = this.f7248n;
            float f14 = this.f7259y;
            canvas.drawRect(f11 - (f12 / 2.0f), f13 - f14, f11 + (f12 / 2.0f), (f13 - f14) + this.f7258x, this.I);
            canvas.drawCircle(this.f7247m, this.f7248n + this.A, this.f7260z, this.I);
            canvas.restore();
        }
    }

    private void p(Canvas canvas) {
        int i10 = this.f7240f;
        if (i10 != 0) {
            canvas.saveLayerAlpha(0.0f, 0.0f, this.f7247m * 2.0f, this.f7248n * 2.0f, i10);
            float f10 = this.f7251q;
            canvas.scale(f10, f10, this.f7247m, this.f7248n);
            this.I.setColor(this.f7238d);
            float f11 = this.f7247m;
            float f12 = f11 - this.f7253s;
            float f13 = this.f7256v;
            float f14 = f12 - (f13 / 2.0f);
            float f15 = this.f7248n;
            float f16 = this.f7254t;
            float f17 = this.f7255u;
            canvas.drawRoundRect(f14, f15 - (f16 / 2.0f), f11 - (f13 / 2.0f), f15 + (f16 / 2.0f), f17, f17, this.I);
            float f18 = this.f7247m;
            float f19 = this.f7256v;
            float f20 = this.f7248n;
            float f21 = this.f7254t;
            float f22 = this.f7255u;
            canvas.drawRoundRect(f18 + (f19 / 2.0f), f20 - (f21 / 2.0f), f18 + this.f7253s + (f19 / 2.0f), f20 + (f21 / 2.0f), f22, f22, this.I);
            canvas.restore();
        }
    }

    private void q(Canvas canvas) {
        float e10 = (this.f7243i.e() - this.f7243i.f()) / 2.0f;
        float e11 = (this.f7244j.e() - this.f7244j.f()) / 2.0f;
        int i10 = this.f7236b;
        if (i10 != 255) {
            canvas.saveLayerAlpha(0.0f, 0.0f, this.f7247m * 2.0f, this.f7248n * 2.0f, i10);
        } else {
            canvas.save();
        }
        canvas.drawCircle(this.f7247m, this.f7248n, e10, this.G);
        canvas.rotate(-90.0f, this.f7247m, this.f7248n);
        canvas.drawArc(this.f7244j.b() - e11, this.f7244j.c() - e11, this.f7244j.b() + e11, this.f7244j.c() + e11, 0.0f, Math.max(1.0E-4f, (this.f7245k * 360.0f) / this.f7237c), false, this.H);
        canvas.restore();
    }

    private void s() {
        z();
        w();
        y();
        u();
        x();
    }

    private void t(Context context) {
        this.f7253s = context.getResources().getDimension(R$dimen.coui_circular_progress_pause_icon_rect_width);
        this.f7254t = context.getResources().getDimension(R$dimen.coui_circular_progress_pause_icon_rect_height);
        this.f7255u = context.getResources().getDimension(R$dimen.coui_circular_progress_pause_icon_rect_radius);
        this.f7256v = context.getResources().getDimension(R$dimen.coui_circular_progress_pause_icon_rect_gap);
        this.f7257w = context.getResources().getDimension(R$dimen.coui_circular_progress_error_icon_rect_width);
        this.f7258x = context.getResources().getDimension(R$dimen.coui_circular_progress_error_icon_rect_height);
        this.f7259y = context.getResources().getDimension(R$dimen.coui_circular_progress_error_icon_rect_bias);
        this.f7260z = context.getResources().getDimension(R$dimen.coui_circular_progress_error_icon_circle_radius);
        this.A = context.getResources().getDimension(R$dimen.coui_circular_progress_error_icon_circle_bias);
        this.B = context.getResources().getDimension(R$dimen.coui_circular_progress_shadow_radius);
        this.C = context.getResources().getDimension(R$dimen.coui_circular_progress_shadow_x_bias);
        this.D = context.getResources().getDimension(R$dimen.coui_circular_progress_shadow_y_bias);
        this.f7242h = ResourcesCompat.d(context.getResources(), R$color.coui_circular_progress_shadow_color, context.getTheme());
    }

    private void u() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.S = ofFloat;
        ofFloat.setDuration(350L);
        ValueAnimator valueAnimator = this.S;
        Interpolator interpolator = f7232b0;
        valueAnimator.setInterpolator(interpolator);
        this.S.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.coui.appcompat.progressbar.c
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                COUICircularProgressDrawable.this.A(valueAnimator2);
            }
        });
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.U = ofFloat2;
        ofFloat2.setDuration(350L);
        this.U.setInterpolator(interpolator);
        this.U.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.coui.appcompat.progressbar.h
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                COUICircularProgressDrawable.this.B(valueAnimator2);
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        this.M = animatorSet;
        animatorSet.playTogether(this.U, this.S);
        this.M.addListener(new d());
    }

    private void v() {
        Paint paint = new Paint(1);
        this.G = paint;
        paint.setStyle(Paint.Style.STROKE);
        this.f7243i = new h();
        Paint paint2 = new Paint(1);
        this.H = paint2;
        paint2.setStrokeCap(Paint.Cap.ROUND);
        this.H.setStyle(Paint.Style.STROKE);
        this.H.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        this.f7244j = new h();
        Paint paint3 = new Paint(1);
        this.I = paint3;
        paint3.setStyle(Paint.Style.FILL);
    }

    private void w() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.O = ofFloat;
        ofFloat.setDuration(200L);
        this.O.setInterpolator(Y);
        this.O.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.coui.appcompat.progressbar.a
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                COUICircularProgressDrawable.this.C(valueAnimator);
            }
        });
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.P = ofFloat2;
        ofFloat2.setStartDelay(200L);
        this.P.setDuration(300L);
        this.P.setInterpolator(Z);
        this.P.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.coui.appcompat.progressbar.e
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                COUICircularProgressDrawable.this.D(valueAnimator);
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        this.K = animatorSet;
        animatorSet.playTogether(this.P, this.O);
        this.K.addListener(new b());
    }

    private void x() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.T = ofFloat;
        ofFloat.setDuration(350L);
        ValueAnimator valueAnimator = this.T;
        Interpolator interpolator = f7232b0;
        valueAnimator.setInterpolator(interpolator);
        this.T.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.coui.appcompat.progressbar.g
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                COUICircularProgressDrawable.this.E(valueAnimator2);
            }
        });
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.V = ofFloat2;
        ofFloat2.setDuration(350L);
        this.V.setInterpolator(interpolator);
        this.V.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.coui.appcompat.progressbar.f
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                COUICircularProgressDrawable.this.F(valueAnimator2);
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        this.N = animatorSet;
        animatorSet.playTogether(this.V, this.T);
        this.N.addListener(new e());
    }

    private void y() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.Q = ofFloat;
        ofFloat.setStartDelay(200L);
        this.Q.setDuration(200L);
        this.Q.setInterpolator(f7231a0);
        this.Q.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.coui.appcompat.progressbar.d
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                COUICircularProgressDrawable.this.G(valueAnimator);
            }
        });
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.R = ofFloat2;
        ofFloat2.setDuration(200L);
        this.R.setInterpolator(Y);
        this.R.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.coui.appcompat.progressbar.b
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                COUICircularProgressDrawable.this.H(valueAnimator);
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        this.L = animatorSet;
        animatorSet.playTogether(this.Q, this.R);
        this.L.addListener(new c());
    }

    private void z() {
        SpringForce springForce = new SpringForce();
        springForce.d(1.0f);
        springForce.f(50.0f);
        SpringAnimation springAnimation = new SpringAnimation(this, f7234d0);
        this.J = springAnimation;
        springAnimation.v(springForce);
        this.J.b(new DynamicAnimation.r() { // from class: com.coui.appcompat.progressbar.i
            @Override // s.DynamicAnimation.r
            public final void a(DynamicAnimation dynamicAnimation, float f10, float f11) {
                COUICircularProgressDrawable.this.I(dynamicAnimation, f10, f11);
            }
        });
    }

    public void L() {
        this.E = null;
    }

    public void M(int i10) {
        this.f7239e = i10;
        this.f7243i.r(i10);
        this.f7244j.r(i10);
    }

    public void N(View view) {
        this.E = view;
    }

    public void O(boolean z10) {
        if (z10) {
            this.G.setShadowLayer(this.B, this.C, this.D, this.f7242h);
            this.I.setShadowLayer(this.B, this.C, this.D, this.f7242h);
        } else {
            this.G.clearShadowLayer();
            this.I.clearShadowLayer();
        }
    }

    public void P(int i10) {
        if (i10 < 0) {
            Log.w("COUICircularDrawable", "Max value should not lesser than 0!");
            i10 = 0;
        }
        if (i10 != this.f7237c) {
            if (i10 < this.f7246l) {
                this.f7246l = i10;
                this.f7245k = i10;
            }
            this.f7237c = i10;
        }
        invalidateSelf();
    }

    public void Q(f fVar) {
        this.W = fVar;
    }

    public void R(g gVar) {
        this.X = gVar;
    }

    public void S(int i10) {
        this.f7238d = i10;
    }

    public void T(int i10, boolean z10) {
        Log.d("COUICircularDrawable", "setProgress: " + i10 + "\nmActualProgress = " + this.f7246l + "\nmVisualProgress = " + this.f7245k + "\nanimate = " + z10);
        this.f7246l = i10;
        float n10 = n((float) i10);
        if (z10) {
            float f10 = this.f7245k;
            if (f10 != n10) {
                this.J.l(f10);
                this.J.q(n10);
                J();
            }
        }
        this.f7245k = n10;
        K();
        invalidateSelf();
        J();
    }

    public void U(float f10, float f11) {
        this.f7244j.s(f10);
        this.f7244j.t(f11);
        this.f7243i.s(f10);
        this.f7243i.t(f11);
    }

    public void V(int i10) {
        this.f7244j.o(i10);
        invalidateSelf();
    }

    public void W(float f10, float f11, float f12, float f13) {
        this.f7247m = f10;
        this.f7248n = f11;
        this.f7249o = f12;
        this.f7250p = f13;
        this.f7243i.m(f10);
        this.f7243i.n(this.f7248n);
        this.f7243i.u(this.f7249o);
        this.f7243i.v(this.f7250p);
        this.f7243i.p(this.f7249o);
        this.f7243i.q(this.f7250p);
        this.f7244j.m(this.f7247m);
        this.f7244j.n(this.f7248n);
        this.f7244j.u(this.f7249o);
        this.f7244j.v(this.f7250p);
        this.f7244j.p(this.f7249o);
        this.f7244j.q(this.f7250p);
        this.G.setStrokeWidth(this.f7243i.k());
        this.H.setStrokeWidth(this.f7244j.k());
    }

    public void X(int i10) {
        this.f7243i.o(i10);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        this.G.setColor(this.f7243i.a());
        this.G.setStrokeWidth(this.f7243i.f());
        this.H.setColor(this.f7244j.a());
        this.H.setStrokeWidth(this.f7244j.f());
        canvas.saveLayerAlpha(0.0f, 0.0f, this.f7247m * 2.0f, this.f7248n * 2.0f, this.f7235a);
        q(canvas);
        p(canvas);
        o(canvas);
        canvas.restore();
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.f7235a;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public void invalidateSelf() {
        super.invalidateSelf();
        View view = this.E;
        if (view != null) {
            view.invalidate();
        }
    }

    public float r() {
        return this.f7245k;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i10) {
        this.f7235a = i10;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        invalidateSelf();
    }
}
