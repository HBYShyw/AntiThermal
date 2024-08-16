package androidx.recyclerview.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.PathInterpolator;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.oplus.os.LinearmotorVibrator;
import com.oplus.sceneservice.sdk.dataprovider.bean.SceneStatusInfo;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$drawable;
import com.support.appcompat.R$string;
import k3.VibrateUtils;
import m1.COUIEaseInterpolator;
import v1.COUIContextUtil;

/* compiled from: COUIFastScroller.java */
/* renamed from: androidx.recyclerview.widget.b, reason: use source file name */
/* loaded from: classes.dex */
public class COUIFastScroller extends RecyclerView.o implements RecyclerView.s {
    private ValueAnimator A;
    private h B;
    private g C;
    private f D;
    private PropertyValuesHolder E;
    private PropertyValuesHolder F;
    private PropertyValuesHolder G;
    private TextPaint J;
    private float N;
    private float O;
    private float P;
    private boolean Q;
    private final Drawable R;
    private final int S;
    private final int T;
    private final int U;
    private final int V;
    private final int W;
    private final int X;

    /* renamed from: a, reason: collision with root package name */
    private final int f3609a;

    /* renamed from: b, reason: collision with root package name */
    private int f3611b;

    /* renamed from: c, reason: collision with root package name */
    private int f3613c;

    /* renamed from: d, reason: collision with root package name */
    private final Drawable f3615d;

    /* renamed from: e, reason: collision with root package name */
    private final int f3617e;

    /* renamed from: f, reason: collision with root package name */
    private final int f3619f;

    /* renamed from: g, reason: collision with root package name */
    private final Drawable f3621g;

    /* renamed from: h, reason: collision with root package name */
    private final int f3623h;

    /* renamed from: h0, reason: collision with root package name */
    private boolean f3624h0;

    /* renamed from: i, reason: collision with root package name */
    private final int f3625i;

    /* renamed from: j, reason: collision with root package name */
    private final float f3627j;

    /* renamed from: j0, reason: collision with root package name */
    private VelocityTracker f3628j0;

    /* renamed from: l, reason: collision with root package name */
    private final int f3631l;

    /* renamed from: l0, reason: collision with root package name */
    private RecyclerView f3632l0;

    /* renamed from: m, reason: collision with root package name */
    private final String f3633m;

    /* renamed from: n, reason: collision with root package name */
    private final int f3635n;

    /* renamed from: o, reason: collision with root package name */
    private final int f3637o;

    /* renamed from: p, reason: collision with root package name */
    private final float f3639p;

    /* renamed from: p0, reason: collision with root package name */
    private int f3640p0;

    /* renamed from: q0, reason: collision with root package name */
    private int f3642q0;

    /* renamed from: r, reason: collision with root package name */
    private int f3643r;

    /* renamed from: r0, reason: collision with root package name */
    private int f3644r0;

    /* renamed from: s, reason: collision with root package name */
    private float f3645s;

    /* renamed from: s0, reason: collision with root package name */
    private int f3646s0;

    /* renamed from: t0, reason: collision with root package name */
    private int f3648t0;

    /* renamed from: u0, reason: collision with root package name */
    private float f3650u0;

    /* renamed from: v, reason: collision with root package name */
    private final int f3651v;

    /* renamed from: v0, reason: collision with root package name */
    private float f3652v0;

    /* renamed from: w, reason: collision with root package name */
    private float f3653w;

    /* renamed from: w0, reason: collision with root package name */
    private float f3654w0;

    /* renamed from: x0, reason: collision with root package name */
    private int f3656x0;

    /* renamed from: z, reason: collision with root package name */
    private ValueAnimator f3659z;

    /* renamed from: k, reason: collision with root package name */
    private float f3629k = 1.0f;

    /* renamed from: q, reason: collision with root package name */
    private float f3641q = 1.0f;

    /* renamed from: t, reason: collision with root package name */
    private float f3647t = 0.0f;

    /* renamed from: u, reason: collision with root package name */
    private float f3649u = 0.0f;

    /* renamed from: x, reason: collision with root package name */
    private float f3655x = 0.0f;

    /* renamed from: y, reason: collision with root package name */
    private final PathInterpolator f3657y = new COUIEaseInterpolator();
    private AnimatorSet H = new AnimatorSet();
    private int I = 0;
    private float K = 0.0f;
    private String L = "";
    private String M = "";
    private boolean Y = false;
    private int Z = 0;

    /* renamed from: a0, reason: collision with root package name */
    private int f3610a0 = 0;

    /* renamed from: b0, reason: collision with root package name */
    private int f3612b0 = SceneStatusInfo.SceneConstant.TRIP_ARRIVE_END_STATION_IN_TIME_AND_LOCATION;

    /* renamed from: c0, reason: collision with root package name */
    private int f3614c0 = SceneStatusInfo.SceneConstant.TRIP_GO_TO_STATION;

    /* renamed from: d0, reason: collision with root package name */
    private int f3616d0 = SceneStatusInfo.SceneConstant.TRIP_IN_JOURNEY;

    /* renamed from: e0, reason: collision with root package name */
    private int f3618e0 = 1000;

    /* renamed from: f0, reason: collision with root package name */
    private long f3620f0 = -1;

    /* renamed from: g0, reason: collision with root package name */
    private Object f3622g0 = null;

    /* renamed from: i0, reason: collision with root package name */
    private boolean f3626i0 = true;

    /* renamed from: k0, reason: collision with root package name */
    private boolean f3630k0 = true;

    /* renamed from: m0, reason: collision with root package name */
    private boolean f3634m0 = false;

    /* renamed from: n0, reason: collision with root package name */
    private int f3636n0 = 0;

    /* renamed from: o0, reason: collision with root package name */
    private int f3638o0 = 0;

    /* renamed from: y0, reason: collision with root package name */
    private float f3658y0 = 1.0f;

    /* renamed from: z0, reason: collision with root package name */
    private final int[] f3660z0 = new int[2];
    final ValueAnimator A0 = ValueAnimator.ofFloat(0.0f, 1.0f);
    int B0 = 0;
    private final Runnable C0 = new a();
    private final RecyclerView.t D0 = new b();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIFastScroller.java */
    /* renamed from: androidx.recyclerview.widget.b$a */
    /* loaded from: classes.dex */
    public class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (COUIFastScroller.this.Y) {
                return;
            }
            COUIFastScroller.this.P(160);
        }
    }

    /* compiled from: COUIFastScroller.java */
    /* renamed from: androidx.recyclerview.widget.b$b */
    /* loaded from: classes.dex */
    class b extends RecyclerView.t {
        b() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.t
        public void onScrolled(RecyclerView recyclerView, int i10, int i11) {
            if (COUIFastScroller.this.f3630k0) {
                COUIFastScroller.this.Z();
                COUIFastScroller.this.m0(recyclerView.computeHorizontalScrollOffset(), recyclerView.computeVerticalScrollOffset());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIFastScroller.java */
    /* renamed from: androidx.recyclerview.widget.b$c */
    /* loaded from: classes.dex */
    public class c implements View.OnAttachStateChangeListener {
        c() {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
            VibrateUtils.i(COUIFastScroller.this.f3632l0.getContext());
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            VibrateUtils.l();
            COUIFastScroller.this.f3632l0.removeOnAttachStateChangeListener(this);
            COUIFastScroller.this.G();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: COUIFastScroller.java */
    /* renamed from: androidx.recyclerview.widget.b$d */
    /* loaded from: classes.dex */
    public class d extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        private boolean f3664a = false;

        d() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.f3664a = true;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (this.f3664a) {
                this.f3664a = false;
                return;
            }
            if (((Float) COUIFastScroller.this.A0.getAnimatedValue()).floatValue() == 0.0f) {
                COUIFastScroller cOUIFastScroller = COUIFastScroller.this;
                cOUIFastScroller.B0 = 0;
                cOUIFastScroller.i0(0);
            } else {
                COUIFastScroller cOUIFastScroller2 = COUIFastScroller.this;
                cOUIFastScroller2.B0 = 2;
                cOUIFastScroller2.c0();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: COUIFastScroller.java */
    /* renamed from: androidx.recyclerview.widget.b$e */
    /* loaded from: classes.dex */
    public class e implements ValueAnimator.AnimatorUpdateListener {
        e() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int floatValue = (int) (((Float) valueAnimator.getAnimatedValue()).floatValue() * 255.0f);
            COUIFastScroller.this.f3615d.setAlpha(floatValue);
            COUIFastScroller.this.f3621g.setAlpha(floatValue);
            COUIFastScroller.this.c0();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: COUIFastScroller.java */
    /* renamed from: androidx.recyclerview.widget.b$f */
    /* loaded from: classes.dex */
    public class f implements ValueAnimator.AnimatorUpdateListener {
        private f() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIFastScroller.this.K = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            int i10 = (int) (COUIFastScroller.this.K * 255.0f);
            COUIFastScroller.this.R.setAlpha(i10);
            COUIFastScroller.this.J.setAlpha(i10);
            COUIFastScroller.this.c0();
        }

        /* synthetic */ f(COUIFastScroller cOUIFastScroller, a aVar) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: COUIFastScroller.java */
    /* renamed from: androidx.recyclerview.widget.b$g */
    /* loaded from: classes.dex */
    public class g extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        private boolean f3668a = false;

        g() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.f3668a = true;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (this.f3668a) {
                this.f3668a = false;
            } else if (COUIFastScroller.this.f3629k != 1.0f) {
                COUIFastScroller.this.I = 2;
            } else {
                COUIFastScroller.this.I = 0;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: COUIFastScroller.java */
    /* renamed from: androidx.recyclerview.widget.b$h */
    /* loaded from: classes.dex */
    public class h implements ValueAnimator.AnimatorUpdateListener {
        private h() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIFastScroller.this.f3641q = ((Float) valueAnimator.getAnimatedValue("HEIGHT_ANIM_HOLDER")).floatValue();
            COUIFastScroller.this.f3629k = ((Float) valueAnimator.getAnimatedValue("WIDTH_ANIM_HOLDER")).floatValue();
            COUIFastScroller.this.f3649u = ((Float) valueAnimator.getAnimatedValue("THUMB_TRANSLATE_X_HOLDER")).floatValue();
            COUIFastScroller cOUIFastScroller = COUIFastScroller.this;
            cOUIFastScroller.f3647t = cOUIFastScroller.f3649u * COUIFastScroller.this.f3651v;
            COUIFastScroller cOUIFastScroller2 = COUIFastScroller.this;
            cOUIFastScroller2.f3655x = cOUIFastScroller2.f3649u * COUIFastScroller.this.f3653w;
            COUIFastScroller.this.c0();
        }

        /* synthetic */ h(COUIFastScroller cOUIFastScroller, a aVar) {
            this();
        }
    }

    public COUIFastScroller(RecyclerView recyclerView, Context context) {
        this.f3653w = 0.0f;
        this.f3624h0 = true;
        this.f3640p0 = 0;
        this.f3642q0 = 0;
        this.f3644r0 = 0;
        this.f3646s0 = 0;
        this.f3648t0 = 0;
        this.f3650u0 = 0.0f;
        this.f3652v0 = 0.0f;
        this.f3654w0 = 0.0f;
        int dimensionPixelOffset = context.getResources().getDimensionPixelOffset(R$dimen.coui_fast_scroller_default_width);
        this.f3623h = dimensionPixelOffset;
        int dimensionPixelOffset2 = context.getResources().getDimensionPixelOffset(R$dimen.coui_fast_scroller_default_height);
        this.f3635n = dimensionPixelOffset2;
        this.f3631l = context.getResources().getDimensionPixelOffset(R$dimen.coui_fast_scroller_default_vertical_margin_end);
        int dimensionPixelOffset3 = context.getResources().getDimensionPixelOffset(R$dimen.coui_fast_scroller_scale_end_width);
        this.f3625i = dimensionPixelOffset3;
        int dimensionPixelOffset4 = context.getResources().getDimensionPixelOffset(R$dimen.coui_fast_scroller_scale_end_height);
        this.f3637o = dimensionPixelOffset4;
        this.f3617e = context.getResources().getDimensionPixelOffset(R$dimen.coui_fast_scroller_bar_background_scale_x_offset);
        this.f3644r0 = context.getResources().getDimensionPixelOffset(R$dimen.coui_fast_scroller_scale_shadow_padding_end);
        this.f3648t0 = context.getResources().getDimensionPixelOffset(R$dimen.coui_fast_scroller_scale_shadow_padding_top);
        this.f3619f = dimensionPixelOffset2 / 2;
        this.f3627j = dimensionPixelOffset3 / dimensionPixelOffset;
        this.f3639p = dimensionPixelOffset4 / dimensionPixelOffset2;
        Drawable drawable = context.getDrawable(R$drawable.coui_fast_scroller_slide_bar_background);
        this.f3615d = drawable;
        drawable.setBounds(0, 0, dimensionPixelOffset, dimensionPixelOffset2);
        drawable.setAlpha(255);
        Drawable drawable2 = context.getDrawable(R$drawable.coui_fast_scroller_union);
        this.f3621g = drawable2;
        this.f3651v = context.getResources().getDimensionPixelOffset(R$dimen.coui_fast_scroller_bar_thumb_translate_x);
        this.f3653w = context.getResources().getDimensionPixelOffset(R$dimen.coui_fast_scroller_bar_thumb_translate_y);
        this.f3650u0 = context.getResources().getDimensionPixelOffset(R$dimen.coui_fast_scroller_bar_thumb_shadow_padding_y);
        this.f3652v0 = context.getResources().getDimensionPixelOffset(R$dimen.coui_fast_scroller_bar_thumb_shadow_padding_x);
        int dimensionPixelOffset5 = context.getResources().getDimensionPixelOffset(R$dimen.coui_fast_scroller_union_width);
        int dimensionPixelOffset6 = context.getResources().getDimensionPixelOffset(R$dimen.coui_fast_scroller_union_height);
        int i10 = (dimensionPixelOffset - dimensionPixelOffset5) / 2;
        int i11 = (dimensionPixelOffset2 - dimensionPixelOffset6) / 2;
        drawable2.setBounds(i10, i11, dimensionPixelOffset5 + i10, dimensionPixelOffset6 + i11);
        drawable2.setAlpha(255);
        Drawable drawable3 = context.getDrawable(R$drawable.coui_fast_scroller_message_background);
        this.R = drawable3;
        drawable3.setAlpha(0);
        this.S = context.getResources().getDimensionPixelOffset(R$dimen.coui_fast_scroller_message_text_padding);
        this.U = context.getResources().getDimensionPixelOffset(R$dimen.coui_fast_scroller_message_background_internal_padding);
        this.T = context.getResources().getDimensionPixelOffset(R$dimen.coui_fast_scroller_message_background_top_offset);
        this.f3640p0 = context.getResources().getDimensionPixelOffset(R$dimen.coui_fast_scroller_message_minimum_height);
        this.f3642q0 = context.getResources().getDimensionPixelOffset(R$dimen.coui_fast_scroller_message_shadow_padding_end);
        this.f3646s0 = context.getResources().getDimensionPixelOffset(R$dimen.coui_fast_scroller_message_shadow_padding_top);
        this.f3654w0 = context.getResources().getDimensionPixelOffset(R$dimen.coui_fast_scroller_message_text_shadow_padding_top);
        this.V = context.getResources().getDimensionPixelOffset(R$dimen.coui_fast_scroller_max_message_width);
        this.W = context.getResources().getDimensionPixelOffset(R$dimen.coui_fast_scroller_message_minimum_width);
        this.X = context.getResources().getDimensionPixelOffset(R$dimen.coui_fast_scroller_message_margin_end);
        this.f3633m = context.getString(R$string.fast_scroller_dots);
        this.f3609a = context.getResources().getDimensionPixelOffset(R$dimen.coui_fast_scroller_minimum_range);
        this.f3611b = context.getResources().getDimensionPixelOffset(R$dimen.coui_fast_scroller_thumb_top_margin);
        this.f3613c = context.getResources().getDimensionPixelOffset(R$dimen.coui_fast_scroller_thumb_bottom_margin);
        this.f3624h0 = VibrateUtils.h(context);
        R(context);
        Q();
        F(recyclerView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void G() {
        this.f3632l0.removeCallbacks(this.C0);
    }

    private void H(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action != 1) {
                if (action == 2) {
                    T();
                    this.f3628j0.addMovement(motionEvent);
                    return;
                } else if (action != 3) {
                    return;
                }
            }
            b0();
            return;
        }
        S();
        this.f3628j0.addMovement(motionEvent);
    }

    private void I() {
        this.f3632l0.removeItemDecoration(this);
        this.f3632l0.removeOnItemTouchListener(this);
        this.f3632l0.removeOnScrollListener(this.D0);
        G();
    }

    private void J(Canvas canvas) {
        int i10;
        int i11;
        float f10;
        float f11;
        float f12;
        this.f3615d.mutate();
        this.f3621g.mutate();
        int i12 = this.Z;
        int i13 = this.f3643r;
        int i14 = (i13 - (this.f3635n / 2)) + this.f3648t0;
        float f13 = ((i13 - (this.f3637o / 2.0f)) - this.T) + this.f3646s0;
        float f14 = -this.f3655x;
        float f15 = -this.f3650u0;
        float f16 = -this.f3654w0;
        if (V()) {
            int i15 = this.f3631l;
            i10 = i15 - this.f3644r0;
            f10 = ((i15 + this.f3625i) - this.X) - this.f3642q0;
            f11 = this.f3647t;
            f12 = -this.f3652v0;
            i11 = this.f3617e - i10;
        } else {
            int i16 = i12 - this.f3623h;
            int i17 = this.f3631l;
            i10 = (i16 - i17) + this.f3644r0;
            float f17 = this.f3642q0 + ((((i12 - this.N) - this.f3625i) - i17) - this.X);
            float f18 = -this.f3647t;
            float f19 = this.f3652v0;
            i11 = (i12 - i10) - this.f3617e;
            f10 = f17;
            f11 = f18;
            f12 = f19;
        }
        int save = canvas.save();
        canvas.translate(i10, i14);
        int save2 = canvas.save();
        float f20 = i11;
        canvas.scale(this.f3629k, this.f3641q, f20, this.f3619f);
        this.f3615d.draw(canvas);
        canvas.restoreToCount(save2);
        canvas.translate(f12, f15);
        canvas.translate(f11, f14);
        canvas.scale(this.f3629k, this.f3641q, f20, this.f3619f);
        this.f3621g.draw(canvas);
        canvas.restoreToCount(save);
        if (!this.Q || this.K == 0.0f) {
            return;
        }
        int save3 = canvas.save();
        canvas.translate(f10, f13);
        this.R.draw(canvas);
        canvas.translate(0.0f, f16);
        canvas.drawText(this.M, this.P, this.O, this.J);
        canvas.restoreToCount(save3);
    }

    private void K(boolean z10) {
        PropertyValuesHolder propertyValuesHolder = this.E;
        float[] fArr = new float[2];
        fArr[0] = this.f3629k;
        fArr[1] = z10 ? this.f3627j : 1.0f;
        propertyValuesHolder.setFloatValues(fArr);
        PropertyValuesHolder propertyValuesHolder2 = this.F;
        float[] fArr2 = new float[2];
        fArr2[0] = this.f3641q;
        fArr2[1] = z10 ? this.f3639p : 1.0f;
        propertyValuesHolder2.setFloatValues(fArr2);
        PropertyValuesHolder propertyValuesHolder3 = this.G;
        float[] fArr3 = new float[2];
        fArr3[0] = this.f3649u;
        fArr3[1] = z10 ? 1.0f : 0.0f;
        propertyValuesHolder3.setFloatValues(fArr3);
        if (this.Q) {
            ValueAnimator valueAnimator = this.A;
            float[] fArr4 = new float[2];
            fArr4[0] = this.K;
            fArr4[1] = z10 ? 1.0f : 0.0f;
            valueAnimator.setFloatValues(fArr4);
        }
        this.H.start();
    }

    private boolean L() {
        if (this.f3620f0 == -1) {
            this.f3620f0 = System.currentTimeMillis();
            return false;
        }
        if (System.currentTimeMillis() - this.f3620f0 < 100) {
            return true;
        }
        this.f3620f0 = System.currentTimeMillis();
        return false;
    }

    private int[] O() {
        int[] iArr = this.f3660z0;
        iArr[0] = this.f3611b;
        iArr[1] = this.f3610a0 - this.f3613c;
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void P(int i10) {
        int i11 = this.B0;
        if (i11 == 1) {
            this.A0.cancel();
        } else if (i11 != 2) {
            return;
        }
        this.B0 = 3;
        ValueAnimator valueAnimator = this.A0;
        valueAnimator.setFloatValues(((Float) valueAnimator.getAnimatedValue()).floatValue(), 0.0f);
        this.A0.setDuration(i10);
        this.A0.start();
    }

    private void Q() {
        this.A0.addListener(new d());
        this.A0.addUpdateListener(new e());
        this.A0.setInterpolator(this.f3657y);
        a aVar = null;
        this.B = new h(this, aVar);
        this.C = new g();
        this.D = new f(this, aVar);
        this.E = PropertyValuesHolder.ofFloat("WIDTH_ANIM_HOLDER", 0.0f, 0.0f);
        this.F = PropertyValuesHolder.ofFloat("HEIGHT_ANIM_HOLDER", 0.0f, 0.0f);
        PropertyValuesHolder ofFloat = PropertyValuesHolder.ofFloat("THUMB_TRANSLATE_X_HOLDER", 0.0f, 0.0f);
        this.G = ofFloat;
        ValueAnimator ofPropertyValuesHolder = ValueAnimator.ofPropertyValuesHolder(this.E, this.F, ofFloat);
        this.f3659z = ofPropertyValuesHolder;
        ofPropertyValuesHolder.setDuration(200L);
        this.f3659z.setInterpolator(this.f3657y);
        this.f3659z.addUpdateListener(this.B);
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(new float[0]);
        this.A = ofFloat2;
        ofFloat2.addUpdateListener(this.D);
        this.A.setDuration(160L);
        this.A.setInterpolator(this.f3657y);
        e0(false);
    }

    private void R(Context context) {
        TextPaint textPaint = new TextPaint();
        this.J = textPaint;
        textPaint.setAntiAlias(true);
        this.J.setTextSize(context.getResources().getDimensionPixelSize(R$dimen.coui_fast_scroller_message_text_size));
        this.J.setTypeface(Typeface.create("sans-serif-medium", 0));
        this.J.setColor(COUIContextUtil.a(context, R$attr.couiColorPrimaryNeutral));
        this.J.setAlpha(0);
        Paint.FontMetrics fontMetrics = this.J.getFontMetrics();
        float f10 = fontMetrics.bottom;
        this.O = ((this.f3637o + (f10 - fontMetrics.top)) / 2.0f) - f10;
    }

    private void S() {
        VelocityTracker velocityTracker = this.f3628j0;
        if (velocityTracker == null) {
            this.f3628j0 = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
    }

    private void T() {
        if (this.f3628j0 == null) {
            this.f3628j0 = VelocityTracker.obtain();
        }
    }

    private boolean V() {
        return ViewCompat.x(this.f3632l0) == 1;
    }

    private void X() {
        int i10 = this.I;
        if (i10 == 1) {
            this.H.cancel();
        } else if (i10 != 2) {
            return;
        }
        this.I = 3;
        K(false);
    }

    private boolean Y() {
        VelocityTracker velocityTracker;
        if (this.f3622g0 == null) {
            LinearmotorVibrator e10 = VibrateUtils.e(this.f3632l0.getContext());
            this.f3622g0 = e10;
            this.f3624h0 = e10 != null;
        }
        if (this.f3622g0 == null || (velocityTracker = this.f3628j0) == null) {
            return false;
        }
        velocityTracker.computeCurrentVelocity(this.f3618e0, this.f3612b0);
        int abs = (int) Math.abs(this.f3628j0.getYVelocity());
        int i10 = abs > this.f3616d0 ? 0 : 1;
        if ((abs > 70 && abs < 250 && L()) || abs < 70) {
            return true;
        }
        VibrateUtils.k((LinearmotorVibrator) this.f3622g0, i10, abs, this.f3612b0, 1200, 1600, this.f3656x0, this.f3658y0);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void Z() {
        if (this.f3624h0 && this.f3626i0) {
            Y();
        }
    }

    private void a0() {
        int i10 = this.I;
        if (i10 != 0) {
            if (i10 != 3) {
                return;
            } else {
                this.H.cancel();
            }
        }
        this.I = 1;
        K(true);
    }

    private void b0() {
        VelocityTracker velocityTracker = this.f3628j0;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.f3628j0 = null;
        }
    }

    private void d0(int i10) {
        G();
        if (this.Y) {
            return;
        }
        this.f3632l0.postDelayed(this.C0, i10);
    }

    private void e0(boolean z10) {
        AnimatorSet animatorSet = new AnimatorSet();
        this.H = animatorSet;
        animatorSet.play(this.f3659z);
        this.H.addListener(this.C);
        if (z10) {
            this.H.playTogether(this.A);
        }
    }

    private int f0(float f10, float f11, int[] iArr, int i10) {
        int N = (iArr[1] - iArr[0]) - N();
        if (N == 0) {
            return 0;
        }
        return (int) (((f11 - f10) / N) * (i10 - this.f3610a0));
    }

    private void k0() {
        this.f3632l0.addItemDecoration(this);
        this.f3632l0.addOnItemTouchListener(this);
        this.f3632l0.addOnScrollListener(this.D0);
        this.f3632l0.addOnAttachStateChangeListener(new c());
    }

    private void l0() {
        int i10 = this.B0;
        if (i10 != 0) {
            if (i10 != 3) {
                return;
            } else {
                this.A0.cancel();
            }
        }
        this.B0 = 1;
        ValueAnimator valueAnimator = this.A0;
        valueAnimator.setFloatValues(((Float) valueAnimator.getAnimatedValue()).floatValue(), 1.0f);
        this.A0.setDuration(160L);
        this.A0.start();
    }

    private void n0(float f10) {
        int f02;
        int[] O = O();
        float max = Math.max(O[0], Math.min(O[1], f10));
        if (Math.abs(this.f3643r - max) >= 2.0f && (f02 = f0(this.f3645s, max, O, this.f3632l0.computeVerticalScrollRange())) != 0) {
            this.f3632l0.scrollBy(0, f02);
            this.f3645s = max;
        }
    }

    public void F(RecyclerView recyclerView) {
        RecyclerView recyclerView2 = this.f3632l0;
        if (recyclerView2 == recyclerView) {
            return;
        }
        if (recyclerView2 != null) {
            I();
        }
        this.f3632l0 = recyclerView;
        if (recyclerView != null) {
            k0();
        }
    }

    public boolean M() {
        return this.f3630k0;
    }

    public int N() {
        return this.f3613c;
    }

    public boolean U() {
        return this.f3636n0 == 2;
    }

    boolean W(float f10, float f11) {
        int i10 = this.f3623h;
        int i11 = this.f3631l;
        int i12 = this.f3644r0;
        float f12 = (i10 + i11) - i12;
        float f13 = ((this.Z - i10) - i11) + i12;
        int i13 = this.f3643r;
        int i14 = this.f3635n;
        int i15 = this.f3648t0;
        float f14 = (i13 - (i14 / 2.0f)) + i15;
        float f15 = (i13 + (i14 / 2.0f)) - i15;
        if (!V() ? f10 >= f13 : f10 <= f12) {
            if (f11 >= f14 && f11 <= f15) {
                return true;
            }
        }
        return false;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.s
    public void a(RecyclerView recyclerView, MotionEvent motionEvent) {
        if (this.f3636n0 == 0) {
            return;
        }
        if (this.f3626i0) {
            H(motionEvent);
        }
        if (motionEvent.getAction() == 0) {
            if (W(motionEvent.getX(), motionEvent.getY())) {
                this.f3638o0 = 2;
                this.f3645s = (int) motionEvent.getY();
                i0(2);
                return;
            }
            return;
        }
        if (motionEvent.getAction() == 1 && this.f3636n0 == 2) {
            this.f3645s = 0.0f;
            i0(1);
            this.f3638o0 = 0;
        } else if (motionEvent.getAction() == 2 && this.f3636n0 == 2) {
            l0();
            if (this.f3638o0 == 2) {
                n0(motionEvent.getY());
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.s
    public boolean b(RecyclerView recyclerView, MotionEvent motionEvent) {
        int i10 = this.f3636n0;
        if (i10 == 1) {
            boolean W = W(motionEvent.getX(), motionEvent.getY());
            if (motionEvent.getAction() != 0 || !W) {
                return false;
            }
            this.f3638o0 = 2;
            this.f3645s = (int) motionEvent.getY();
            i0(2);
        } else if (i10 != 2) {
            return false;
        }
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.s
    public void c(boolean z10) {
    }

    void c0() {
        this.f3632l0.invalidate();
    }

    public void g0(boolean z10) {
        this.f3630k0 = z10;
        if (z10 || this.f3636n0 == 0) {
            return;
        }
        P(160);
    }

    public void h0(boolean z10) {
        if (this.Q != z10) {
            e0(z10);
            this.Q = z10;
            c0();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void i(Canvas canvas, RecyclerView recyclerView, RecyclerView.z zVar) {
        if (this.Z == this.f3632l0.getWidth() && this.f3610a0 == this.f3632l0.getHeight()) {
            if (this.B0 == 0 || !this.f3634m0) {
                return;
            }
            J(canvas);
            return;
        }
        this.Z = this.f3632l0.getWidth();
        this.f3610a0 = this.f3632l0.getHeight();
        i0(0);
    }

    void i0(int i10) {
        if (i10 == 2 && this.f3636n0 != 2) {
            a0();
            G();
        }
        if (i10 == 0) {
            c0();
        } else {
            l0();
        }
        if (this.f3636n0 == 2 && i10 != 2) {
            d0(2000);
            X();
        } else if (i10 == 1) {
            d0(2000);
        }
        this.f3636n0 = i10;
    }

    public void j0(int i10) {
        this.f3611b = i10;
    }

    void m0(int i10, int i11) {
        int[] O = O();
        int computeVerticalScrollRange = this.f3632l0.computeVerticalScrollRange();
        int i12 = O[1] - O[0];
        boolean z10 = computeVerticalScrollRange - i12 > 0 && this.f3610a0 >= this.f3609a;
        this.f3634m0 = z10;
        if (!z10) {
            if (this.f3636n0 != 0) {
                i0(0);
                return;
            }
            return;
        }
        float f10 = i11 / (computeVerticalScrollRange - this.f3610a0);
        if (f10 > 1.0f) {
            this.f3643r = i12 + O[0];
        } else {
            this.f3643r = (int) ((f10 * i12) + O[0]);
        }
        int i13 = this.f3636n0;
        if (i13 == 0 || i13 == 1) {
            i0(1);
        }
    }
}
