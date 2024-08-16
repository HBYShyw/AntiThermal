package com.coui.appcompat.indicator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import b2.COUILog;
import com.support.control.R$attr;
import com.support.control.R$drawable;
import com.support.control.R$id;
import com.support.control.R$layout;
import com.support.control.R$style;
import com.support.control.R$styleable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import m1.COUIEaseInterpolator;
import v1.COUIContextUtil;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class COUIPageIndicator extends FrameLayout {

    /* renamed from: o0, reason: collision with root package name */
    private static final float f6178o0;

    /* renamed from: p0, reason: collision with root package name */
    private static final float f6179p0;

    /* renamed from: q0, reason: collision with root package name */
    private static final float f6180q0;

    /* renamed from: r0, reason: collision with root package name */
    private static final float f6181r0;

    /* renamed from: s0, reason: collision with root package name */
    private static final float f6182s0;

    /* renamed from: t0, reason: collision with root package name */
    private static final float f6183t0;

    /* renamed from: u0, reason: collision with root package name */
    private static final float f6184u0;

    /* renamed from: v0, reason: collision with root package name */
    private static final boolean f6185v0;
    private float A;
    private float B;
    private float C;
    private float D;
    private float E;
    private boolean F;
    private boolean G;
    private boolean H;
    private boolean I;
    private boolean J;
    private boolean K;
    private boolean L;
    private boolean M;
    private LinearLayout N;
    private List<View> O;
    private Paint P;
    private Paint Q;
    private Paint R;
    private Paint S;
    private Paint T;
    private Path U;
    private Path V;
    private RectF W;

    /* renamed from: a0, reason: collision with root package name */
    private RectF f6186a0;

    /* renamed from: b0, reason: collision with root package name */
    private RectF f6187b0;

    /* renamed from: c0, reason: collision with root package name */
    private ValueAnimator f6188c0;

    /* renamed from: d0, reason: collision with root package name */
    private Handler f6189d0;

    /* renamed from: e, reason: collision with root package name */
    private final List<e> f6190e;

    /* renamed from: e0, reason: collision with root package name */
    private int f6191e0;

    /* renamed from: f, reason: collision with root package name */
    private int f6192f;

    /* renamed from: f0, reason: collision with root package name */
    private f f6193f0;

    /* renamed from: g, reason: collision with root package name */
    private int f6194g;

    /* renamed from: g0, reason: collision with root package name */
    private int f6195g0;

    /* renamed from: h, reason: collision with root package name */
    private int f6196h;

    /* renamed from: h0, reason: collision with root package name */
    private Context f6197h0;

    /* renamed from: i, reason: collision with root package name */
    private int f6198i;

    /* renamed from: i0, reason: collision with root package name */
    private int f6199i0;

    /* renamed from: j, reason: collision with root package name */
    private int f6200j;

    /* renamed from: j0, reason: collision with root package name */
    private float f6201j0;

    /* renamed from: k, reason: collision with root package name */
    private int f6202k;

    /* renamed from: k0, reason: collision with root package name */
    private View f6203k0;

    /* renamed from: l, reason: collision with root package name */
    private int f6204l;

    /* renamed from: l0, reason: collision with root package name */
    private View f6205l0;

    /* renamed from: m, reason: collision with root package name */
    private int f6206m;

    /* renamed from: m0, reason: collision with root package name */
    private boolean f6207m0;

    /* renamed from: n, reason: collision with root package name */
    private int f6208n;

    /* renamed from: n0, reason: collision with root package name */
    private IndicatorSavedState f6209n0;

    /* renamed from: o, reason: collision with root package name */
    private int f6210o;

    /* renamed from: p, reason: collision with root package name */
    private int f6211p;

    /* renamed from: q, reason: collision with root package name */
    private int f6212q;

    /* renamed from: r, reason: collision with root package name */
    private int f6213r;

    /* renamed from: s, reason: collision with root package name */
    private int f6214s;

    /* renamed from: t, reason: collision with root package name */
    private int f6215t;

    /* renamed from: u, reason: collision with root package name */
    private float f6216u;

    /* renamed from: v, reason: collision with root package name */
    private float f6217v;

    /* renamed from: w, reason: collision with root package name */
    private float f6218w;

    /* renamed from: x, reason: collision with root package name */
    private float f6219x;

    /* renamed from: y, reason: collision with root package name */
    private float f6220y;

    /* renamed from: z, reason: collision with root package name */
    private float f6221z;

    /* loaded from: classes.dex */
    class a extends Handler {
        a() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 17) {
                COUIPageIndicator.this.w0();
            }
            super.handleMessage(message);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements ValueAnimator.AnimatorUpdateListener {
        b() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            if (COUIPageIndicator.this.J) {
                return;
            }
            float f10 = COUIPageIndicator.this.f6216u - COUIPageIndicator.this.f6218w;
            float f11 = COUIPageIndicator.this.f6217v - COUIPageIndicator.this.f6219x;
            float f12 = COUIPageIndicator.this.f6216u - (f10 * floatValue);
            if (f12 > COUIPageIndicator.this.W.right - COUIPageIndicator.this.f6192f) {
                f12 = COUIPageIndicator.this.W.right - COUIPageIndicator.this.f6192f;
            }
            float f13 = COUIPageIndicator.this.f6217v - (f11 * floatValue);
            if (f13 < COUIPageIndicator.this.W.left + COUIPageIndicator.this.f6192f) {
                f13 = COUIPageIndicator.this.f6192f + COUIPageIndicator.this.W.left;
            }
            if (!COUIPageIndicator.this.L) {
                if (COUIPageIndicator.this.H) {
                    COUIPageIndicator.this.W.right = f13;
                } else {
                    COUIPageIndicator.this.W.left = f12;
                }
            } else {
                COUIPageIndicator.this.W.left = f12;
                COUIPageIndicator.this.W.right = f13;
            }
            if (COUIPageIndicator.this.H) {
                COUIPageIndicator cOUIPageIndicator = COUIPageIndicator.this;
                cOUIPageIndicator.A = cOUIPageIndicator.W.right - (COUIPageIndicator.this.f6192f * 0.5f);
            } else {
                COUIPageIndicator cOUIPageIndicator2 = COUIPageIndicator.this;
                cOUIPageIndicator2.A = cOUIPageIndicator2.W.left + (COUIPageIndicator.this.f6192f * 0.5f);
            }
            COUIPageIndicator cOUIPageIndicator3 = COUIPageIndicator.this;
            cOUIPageIndicator3.B = cOUIPageIndicator3.f6187b0.left + (COUIPageIndicator.this.f6192f * 0.5f);
            COUIPageIndicator cOUIPageIndicator4 = COUIPageIndicator.this;
            cOUIPageIndicator4.V = cOUIPageIndicator4.O(cOUIPageIndicator4.f6214s, COUIPageIndicator.this.A, COUIPageIndicator.this.B, COUIPageIndicator.this.f6192f * 0.5f, false);
            COUIPageIndicator.this.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c extends AnimatorListenerAdapter {
        c() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            COUIPageIndicator.this.U(false);
            if (COUIPageIndicator.this.J) {
                return;
            }
            COUIPageIndicator.this.W.right = COUIPageIndicator.this.W.left + COUIPageIndicator.this.f6192f;
            COUIPageIndicator.this.L = false;
            COUIPageIndicator.this.I = true;
            COUIPageIndicator.this.invalidate();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            super.onAnimationStart(animator);
            COUIPageIndicator.this.J = false;
            COUIPageIndicator cOUIPageIndicator = COUIPageIndicator.this;
            cOUIPageIndicator.f6216u = cOUIPageIndicator.W.left;
            COUIPageIndicator cOUIPageIndicator2 = COUIPageIndicator.this;
            cOUIPageIndicator2.f6217v = cOUIPageIndicator2.W.right;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d implements View.OnClickListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ int f6226e;

        d(int i10) {
            this.f6226e = i10;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (COUIPageIndicator.this.f6193f0 == null || COUIPageIndicator.this.f6211p == this.f6226e) {
                return;
            }
            COUIPageIndicator.this.L = true;
            COUIPageIndicator.this.I = false;
            COUIPageIndicator cOUIPageIndicator = COUIPageIndicator.this;
            cOUIPageIndicator.f6210o = cOUIPageIndicator.f6211p;
            COUIPageIndicator.this.x0();
            COUIPageIndicator.this.f6193f0.a(this.f6226e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum e {
        LARGE,
        MEDIUM,
        SMALL,
        GONE
    }

    /* loaded from: classes.dex */
    public interface f {
        void a(int i10);
    }

    static {
        float sqrt = (float) Math.sqrt(2.0d);
        f6178o0 = sqrt;
        f6179p0 = 7.5f - (2.5f * sqrt);
        f6180q0 = (7.5f * sqrt) - 21.0f;
        f6181r0 = sqrt * 0.5f;
        f6182s0 = 0.625f * sqrt;
        f6183t0 = (-1.25f) * sqrt;
        f6184u0 = sqrt * 0.5f;
        f6185v0 = COUILog.f4543b || COUILog.d("COUIPageIndicator", 3);
    }

    public COUIPageIndicator(Context context) {
        this(context, null);
    }

    private void A0(int i10) {
        if (i10 >= this.N.getChildCount()) {
            return;
        }
        boolean z10 = isLayoutRtl() == (this.f6210o > i10);
        int measuredWidth = this.N.getMeasuredWidth() > 0 ? this.N.getMeasuredWidth() : this.f6191e0;
        if (this.f6208n < 6) {
            if (isLayoutRtl()) {
                this.f6219x = measuredWidth - (this.f6198i + (this.f6215t * i10));
            } else {
                this.f6219x = this.f6198i + this.f6192f + (this.f6215t * i10);
            }
        } else {
            B0(i10, z10);
        }
        this.f6218w = this.f6219x - this.f6192f;
        if (f6185v0) {
            Log.d("COUIPageIndicator", "verifyFinalPosition position =：" + i10 + ",mFinalRight" + this.f6219x + ",mFinalLeft =:" + this.f6218w + ",mWidth =:" + this.f6191e0 + ",isRtl = :" + isLayoutRtl());
        }
    }

    private void B0(int i10, boolean z10) {
        View childAt = this.N.getChildAt(i10);
        if (childAt == null) {
            childAt = this.N.getChildAt(this.f6210o);
        }
        if (childAt == null) {
            Log.e("COUIPageIndicator", "Illegal Operation: postion = " + i10 + " current position = " + this.f6210o);
            return;
        }
        int i11 = R$id.page_indicator_dot;
        View findViewById = childAt.findViewById(i11);
        int measuredWidth = this.N.getMeasuredWidth() > 0 ? this.N.getMeasuredWidth() : this.f6191e0;
        if (z10) {
            if (isLayoutRtl()) {
                if (i10 == 0) {
                    this.f6219x = measuredWidth - this.f6198i;
                    return;
                } else {
                    if (childAt.getX() < this.f6198i || childAt.getX() <= 0.0f) {
                        return;
                    }
                    this.f6219x = childAt.getX() + findViewById.getMeasuredWidth() + this.f6198i;
                    return;
                }
            }
            if (i10 == 0) {
                this.f6219x = this.f6198i + this.f6192f;
                return;
            } else {
                if (childAt.getX() < this.f6198i || childAt.getX() <= 0.0f) {
                    return;
                }
                this.f6219x = childAt.getX() + findViewById.getMeasuredWidth() + this.f6198i;
                return;
            }
        }
        if (!isLayoutRtl()) {
            if (i10 == 0) {
                this.f6219x = this.f6198i + this.f6192f;
                return;
            } else {
                if (childAt.getX() < this.f6198i || childAt.getX() <= 0.0f) {
                    return;
                }
                this.f6219x = childAt.getX() + findViewById.getMeasuredWidth() + this.f6198i;
                return;
            }
        }
        if (i10 == this.f6208n - 1) {
            this.f6219x = this.f6198i + this.f6192f;
            return;
        }
        View childAt2 = this.N.getChildAt(i10);
        if (childAt2 == null) {
            childAt2 = this.N.getChildAt(this.f6210o);
        }
        if (childAt2 == null) {
            Log.e("COUIPageIndicator", "Illegal Operation: postion = " + i10 + " current position = " + this.f6210o);
            return;
        }
        View findViewById2 = childAt2.findViewById(i11);
        if (childAt2.getX() < this.f6198i || childAt2.getX() <= 0.0f) {
            return;
        }
        this.f6219x = childAt2.getX() + findViewById2.getMeasuredWidth() + this.f6198i;
    }

    private void C0() {
        int i10;
        int i11 = this.f6208n;
        if (i11 < 1 || (i10 = this.f6199i0) < 1) {
            return;
        }
        int i12 = this.f6215t;
        this.f6191e0 = i12 * i10;
        if (i11 >= 6) {
            this.f6191e0 = (i12 * (i10 - 2)) + this.f6196h + this.f6194g + (this.f6198i * 4);
        }
        requestLayout();
    }

    private void D0(int i10, boolean z10) {
        RectF rectF = this.f6186a0;
        rectF.top = 0.0f;
        rectF.bottom = this.f6192f;
        if (z10) {
            View childAt = this.N.getChildAt(i10);
            if (isLayoutRtl()) {
                childAt = this.N.getChildAt(i10 - 1);
            }
            if (childAt != null) {
                View findViewById = childAt.findViewById(R$id.page_indicator_dot);
                this.f6186a0.right = childAt.getX() + findViewById.getMeasuredWidth() + this.f6198i;
            }
        } else if (isLayoutRtl()) {
            View childAt2 = this.N.getChildAt(i10);
            if (childAt2 != null) {
                View findViewById2 = childAt2.findViewById(R$id.page_indicator_dot);
                this.f6186a0.right = childAt2.getX() + findViewById2.getMeasuredWidth() + this.f6198i;
            }
        } else {
            View childAt3 = this.N.getChildAt(i10 + 1);
            if (childAt3 != null) {
                View findViewById3 = childAt3.findViewById(R$id.page_indicator_dot);
                this.f6186a0.right = ((childAt3.getX() + findViewById3.getMeasuredWidth()) + this.f6198i) - this.f6215t;
            }
        }
        RectF rectF2 = this.f6186a0;
        rectF2.left = rectF2.right - this.f6192f;
    }

    private void E0(int i10, boolean z10, boolean z11) {
        if (z10) {
            D0(i10, z11);
        } else {
            z0(i10, z11);
        }
        if (f6185v0) {
            Log.d("COUIPageIndicator", "verifyStickyPosition pos: " + i10 + " portRect: " + this.f6186a0 + " depart: " + this.f6187b0 + " isPortStickyPath: " + z10);
        }
    }

    private void H(int i10) {
        if (f6185v0) {
            Log.d("COUIPageIndicator", "addDotLevel: dotSize = " + i10);
        }
        if (Math.abs(i10 - this.f6192f) <= 1) {
            this.f6190e.add(e.LARGE);
            return;
        }
        if (Math.abs(i10 - this.f6196h) <= 1) {
            this.f6190e.add(e.MEDIUM);
        } else if (Math.abs(i10 - this.f6194g) <= 1) {
            this.f6190e.add(e.SMALL);
        } else {
            this.f6190e.add(e.GONE);
        }
    }

    private void I(int i10) {
        for (int i11 = 0; i11 < i10; i11++) {
            View K = K(this.G, this.f6200j, i11);
            if (this.F) {
                K.setOnClickListener(new d(i11));
            }
            this.O.add(K.findViewById(R$id.page_indicator_dot));
            this.N.addView(K);
        }
    }

    private void J() {
        this.f6188c0.addListener(new c());
    }

    @TargetApi(21)
    private View K(boolean z10, int i10, int i11) {
        View inflate = LayoutInflater.from(getContext()).inflate(R$layout.coui_page_indicator_dot_layout, (ViewGroup) this, false);
        View findViewById = inflate.findViewById(R$id.page_indicator_dot);
        findViewById.setBackground(getContext().getResources().getDrawable(z10 ? R$drawable.coui_page_indicator_dot_stroke : R$drawable.coui_page_indicator_dot));
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) findViewById.getLayoutParams();
        int i12 = this.f6192f;
        if (this.f6199i0 >= 6) {
            i12 = N(this.f6190e.size());
            if (i11 >= 6 && i12 == 0) {
                inflate.setVisibility(8);
            }
        }
        layoutParams.width = i12;
        layoutParams.height = i12;
        findViewById.setLayoutParams(layoutParams);
        int i13 = this.f6198i;
        layoutParams.setMargins(i13, 0, i13, 0);
        u0(z10, findViewById, i10);
        H(i12);
        return inflate;
    }

    private void L() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.f6188c0 = ofFloat;
        ofFloat.setDuration(300L);
        this.f6188c0.setInterpolator(new COUIEaseInterpolator());
        this.f6188c0.addUpdateListener(new b());
        J();
    }

    private void M(float f10, float f11) {
        this.C = Math.max(Math.min(((-1.0f) * f10) + (3.0f * f11), 1.0f * f11), f11 * 0.0f);
        float f12 = 1.5f * f11;
        this.D = f12;
        this.E = 0.0f;
        if (f10 >= 2.8f * f11) {
            float max = Math.max(Math.min((f6179p0 * f10) + (f6180q0 * f11), f12), f6181r0 * f11);
            this.D = max;
            this.E = ((f10 - (max * 2.0f)) * f11) / ((f6178o0 * f10) - (f11 * 2.0f));
        } else {
            this.D = Math.max(Math.min((f6182s0 * f10) + (f6183t0 * f11), f6184u0 * f11), 0.0f);
            this.E = (float) Math.sqrt(Math.pow(f11, 2.0d) - Math.pow(this.D, 2.0d));
        }
    }

    private int N(int i10) {
        if (i10 >= 6) {
            return 0;
        }
        int i11 = this.f6210o;
        if (i10 != i11 && this.f6208n >= 6) {
            int i12 = this.f6199i0;
            if (i11 < i12 - 2) {
                if (i10 == i12 - 2) {
                    return this.f6196h;
                }
                if (i10 == i12 - 1) {
                    return this.f6194g;
                }
                return this.f6192f;
            }
            if (i11 == i12 - 2) {
                if (i10 != 0 && i10 != i12 - 1) {
                    return this.f6192f;
                }
                return this.f6196h;
            }
            if (i11 != i12 - 1) {
                return this.f6192f;
            }
            if (i10 == 0) {
                return this.f6194g;
            }
            if (i10 == 1) {
                return this.f6196h;
            }
            return this.f6192f;
        }
        return this.f6192f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Path O(int i10, float f10, float f11, float f12, boolean z10) {
        Path path;
        if (z10) {
            path = this.U;
        } else {
            path = this.V;
        }
        path.reset();
        float abs = Math.abs(f10 - f11);
        if (abs < this.f6215t && i10 != -1) {
            M(abs, f12);
            float f13 = f6178o0;
            float f14 = f13 * 0.5f * f12;
            float f15 = f13 * 0.5f * f12;
            if (f10 > f11) {
                this.D = -this.D;
                f14 = -f14;
            }
            if (abs >= 2.8f * f12) {
                float f16 = f10 + f14;
                float f17 = f12 + f15;
                path.moveTo(f16, f17);
                path.lineTo(this.D + f10, this.E + f12);
                float f18 = (f10 + f11) * 0.5f;
                path.quadTo(f18, this.C + f12, f11 - this.D, this.E + f12);
                float f19 = f11 - f14;
                path.lineTo(f19, f17);
                float f20 = f12 - f15;
                path.lineTo(f19, f20);
                path.lineTo(f11 - this.D, f12 - this.E);
                path.quadTo(f18, f12 - this.C, f10 + this.D, f12 - this.E);
                path.lineTo(f16, f20);
                path.lineTo(f16, f17);
            } else {
                path.moveTo(this.D + f10, this.E + f12);
                float f21 = (f10 + f11) * 0.5f;
                path.quadTo(f21, this.C + f12, f11 - this.D, this.E + f12);
                path.lineTo(f11 - this.D, f12 - this.E);
                path.quadTo(f21, f12 - this.C, this.D + f10, f12 - this.E);
                path.lineTo(f10 + this.D, f12 + this.E);
            }
            return path;
        }
        U(z10);
        return path;
    }

    private void P(int i10, float f10, boolean z10) {
        if (z10) {
            if (isLayoutRtl()) {
                R(i10 + 1, f10);
            } else {
                R(i10, f10);
            }
        } else {
            Q(i10, f10);
        }
        RectF rectF = this.W;
        this.f6216u = rectF.left;
        this.f6217v = rectF.right;
    }

    private void Q(int i10, float f10) {
        View findViewById;
        float f11;
        float x10;
        if (isLayoutRtl()) {
            this.f6213r = i10 + 1;
        } else {
            this.f6213r = i10;
            if (i10 == this.f6208n - 1) {
                return;
            }
        }
        View childAt = this.N.getChildAt(i10);
        if (childAt == null || !childAt.isLaidOut() || (findViewById = childAt.findViewById(R$id.page_indicator_dot)) == null) {
            return;
        }
        float x11 = childAt.getX() + findViewById.getX();
        float measuredWidth = childAt.getMeasuredWidth() + x11;
        if (isLayoutRtl()) {
            float x12 = childAt.getX() + this.f6198i;
            View childAt2 = this.N.getChildAt(i10 + 1);
            if (childAt2 == null) {
                x10 = this.f6198i;
            } else {
                x10 = childAt2.getX() + this.f6198i;
            }
            f11 = x12 - ((x12 - x10) * f10);
        } else {
            f11 = measuredWidth + ((x11 - measuredWidth) * (1.0f - f10));
        }
        if (i10 == 0 && f10 == 0.0f) {
            int measuredWidth2 = this.N.getMeasuredWidth() > 0 ? this.N.getMeasuredWidth() : this.f6191e0;
            if (isLayoutRtl()) {
                int i11 = this.f6198i;
                f11 = measuredWidth2 - ((i11 + r2) + (this.f6192f * f10));
            }
        }
        this.W.left = f11;
        g0(f11);
        if (f10 == 0.0f) {
            RectF rectF = this.W;
            rectF.right = rectF.left + this.f6192f;
        }
    }

    private void R(int i10, float f10) {
        float f11;
        float f12;
        if (isLayoutRtl()) {
            this.f6213r = i10;
        } else {
            this.f6213r = i10 + 1;
        }
        View childAt = this.N.getChildAt(i10);
        if (childAt == null || !childAt.isLaidOut()) {
            return;
        }
        float x10 = childAt.getX();
        View findViewById = childAt.findViewById(R$id.page_indicator_dot);
        if (findViewById != null) {
            float x11 = x10 + findViewById.getX() + this.f6192f;
            float measuredWidth = childAt.getMeasuredWidth() + x11;
            int measuredWidth2 = this.N.getMeasuredWidth() > 0 ? this.N.getMeasuredWidth() : this.f6191e0;
            if (isLayoutRtl()) {
                if (i10 == 0 && f10 == 0.0f) {
                    f11 = measuredWidth2 - this.f6198i;
                } else {
                    float x12 = childAt.getX() + this.f6198i + this.f6192f;
                    View childAt2 = this.N.getChildAt(i10 - 1);
                    if (childAt2 != null) {
                        f12 = childAt2.getX() + this.f6198i + this.f6192f;
                    } else {
                        f12 = measuredWidth2 - this.f6198i;
                    }
                    f11 = ((f12 - x12) * (1.0f - f10)) + x12;
                }
            } else if (i10 == 0 && f10 == 0.0f) {
                int i11 = this.f6198i;
                f11 = i11 + r0 + (this.f6192f * f10);
            } else {
                f11 = x11 + ((measuredWidth - x11) * f10);
            }
            this.W.right = f11;
            f0(f11);
            if (f10 == 0.0f) {
                RectF rectF = this.W;
                rectF.left = rectF.right - this.f6192f;
            }
        }
    }

    private void S() {
        int i10;
        if (this.f6190e.size() > 0 && this.f6210o <= this.f6190e.size() - 1 && (i10 = this.f6210o) >= 0) {
            if (this.f6190e.get(i10) != e.LARGE) {
                Log.e("COUIPageIndicator", "checkWrongState: state wrong? current position = " + this.f6210o);
                for (int i11 = 0; i11 < this.f6190e.size(); i11++) {
                    Log.e("COUIPageIndicator", "dot " + i11 + " level = " + this.f6190e.get(i11));
                }
                if (isInLayout()) {
                    post(new Runnable() { // from class: com.coui.appcompat.indicator.a
                        @Override // java.lang.Runnable
                        public final void run() {
                            COUIPageIndicator.this.Z();
                        }
                    });
                    return;
                } else {
                    b0(this.f6210o, 0.0f, this.f6207m0);
                    return;
                }
            }
            return;
        }
        Log.e("COUIPageIndicator", "checkWrongState: no dots. mIndicatorDotLevel.size: " + this.f6190e.size() + "  mCurrentPosition: " + this.f6210o);
    }

    private void T() {
        U(true);
        U(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void U(boolean z10) {
        if (z10) {
            this.f6213r = -1;
            this.f6186a0.setEmpty();
            this.U.reset();
        } else {
            this.f6214s = -1;
            this.f6187b0.setEmpty();
            this.V.reset();
        }
    }

    private void V(int i10, boolean z10, float f10) {
        if (f6185v0) {
            Log.d("COUIPageIndicator", "executeDotAnim position: " + i10 + "first visible child:  curr: " + this.f6210o);
        }
        if (z10) {
            if (isLayoutRtl()) {
                X(i10, f10);
                return;
            } else {
                W(i10, f10);
                return;
            }
        }
        if (isLayoutRtl()) {
            W(i10, f10);
        } else {
            X(i10, f10);
        }
    }

    private void W(int i10, float f10) {
        if (f6185v0) {
            Log.e("COUIPageIndicator", "executeScrollLeftDotAnim pos: " + i10 + "mCurrent: " + this.f6210o);
        }
        if (this.f6208n == 6) {
            if (i10 == 3 && this.f6190e.get(0) == e.LARGE) {
                r0(0, this.f6192f, this.f6196h, f10);
                r0(i10 + 1, this.f6196h, this.f6192f, f10);
                r0(i10 + 2, this.f6194g, this.f6196h, f10);
                return;
            } else {
                if (i10 == this.f6208n - 2 && this.f6190e.get(0) == e.MEDIUM) {
                    r0(0, this.f6196h, this.f6194g, f10);
                    r0(i10 - 3, this.f6192f, this.f6196h, f10);
                    r0(this.N.getChildCount() - 1, this.f6196h, this.f6192f, f10);
                    return;
                }
                return;
            }
        }
        if (i10 == 3 && this.f6190e.get(i10 + 1) == e.MEDIUM) {
            n0(f10, i10);
            return;
        }
        if (i10 >= 4 && i10 < this.f6208n - 2) {
            if (this.f6190e.get(r1.size() - 1) == e.GONE && getFirstVisiblePosition() + 4 == i10) {
                m0(f10, i10);
                return;
            }
        }
        if (i10 == this.f6208n - 2 && this.f6190e.get(i10 - 4) == e.MEDIUM) {
            l0(f10, i10);
        }
    }

    private void X(int i10, float f10) {
        if (f6185v0) {
            Log.d("COUIPageIndicator", "executeScrollRightDotAnim pos: " + i10 + "mCurrent: " + this.f6210o + " offset:" + f10);
        }
        float f11 = 1.0f - f10;
        int i11 = this.f6208n;
        if (i11 == 6) {
            if (i10 == i11 - 5 && this.f6190e.get(0) == e.SMALL) {
                r0(0, this.f6194g, this.f6196h, f11);
                r0(1, this.f6196h, this.f6192f, f11);
                r0(this.N.getChildCount() - 1, this.f6192f, this.f6196h, f11);
                return;
            } else {
                if (i10 == 0 && this.f6190e.get(0) == e.MEDIUM) {
                    r0(0, this.f6196h, this.f6192f, f11);
                    r0(this.N.getChildCount() - 2, this.f6192f, this.f6196h, f11);
                    r0(this.N.getChildCount() - 1, this.f6196h, this.f6194g, f11);
                    return;
                }
                return;
            }
        }
        if (i10 == i11 - 5 && this.f6190e.get(i10) == e.MEDIUM) {
            q0(f11, i10);
            return;
        }
        if (i10 > 0 && i10 < this.f6208n - 5 && this.f6190e.get(0) == e.GONE && this.f6190e.get(i10 + 5) == e.MEDIUM) {
            p0(f11, i10);
        } else if (i10 == 0 && this.f6190e.get(i10 + 4) == e.LARGE) {
            o0(f11, i10);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Y, reason: merged with bridge method [inline-methods] */
    public void b0(final int i10, final float f10, final boolean z10) {
        if (this.f6208n < 6 || this.f6190e.size() <= 0) {
            return;
        }
        this.f6212q = Math.min(this.f6212q, this.f6208n - 4);
        this.f6190e.clear();
        int i11 = this.f6210o;
        if (i11 < this.f6212q) {
            this.f6212q = i11;
        }
        if (i11 > this.f6212q + 3) {
            this.f6212q = i11 - 3;
        }
        int i12 = this.f6212q;
        if (i12 == 0 && i11 >= i12 && i11 <= i12 + 3) {
            for (int i13 = 0; i13 < this.f6208n; i13++) {
                int i14 = this.f6212q;
                if (i13 <= i14 + 3) {
                    this.f6190e.add(e.LARGE);
                } else if (i13 == i14 + 4) {
                    this.f6190e.add(e.MEDIUM);
                } else if (i13 == i14 + 5) {
                    this.f6190e.add(e.SMALL);
                } else {
                    this.f6190e.add(e.GONE);
                }
            }
        } else {
            int i15 = this.f6208n;
            if (i12 == i15 - 4 && i11 >= i12 && i11 <= i12 + 3) {
                for (int i16 = 0; i16 < this.f6208n; i16++) {
                    int i17 = this.f6212q;
                    if (i16 >= i17) {
                        this.f6190e.add(e.LARGE);
                    } else if (i16 == i17 - 1) {
                        this.f6190e.add(e.MEDIUM);
                    } else if (i16 == i17 - 2) {
                        this.f6190e.add(e.SMALL);
                    } else {
                        this.f6190e.add(e.GONE);
                    }
                }
            } else if (i12 > 0 && i12 < i15 - 4 && i11 >= i12 && i11 <= i12 + 3) {
                for (int i18 = 0; i18 < this.f6208n; i18++) {
                    int i19 = this.f6212q;
                    if (i18 <= i19 - 2 || i18 >= i19 + 5) {
                        this.f6190e.add(e.GONE);
                    } else if (i18 == i19 - 1 || i18 == i19 + 4) {
                        this.f6190e.add(e.MEDIUM);
                    } else if (i18 >= i19 && i18 <= i19 + 3) {
                        this.f6190e.add(e.LARGE);
                    }
                }
            } else {
                Log.e("COUIPageIndicator", "Illegal state: First large dot index = " + this.f6212q + " Current position = " + this.f6210o);
                for (int i20 = 0; i20 < this.f6208n; i20++) {
                    this.f6190e.add(e.LARGE);
                }
            }
        }
        for (int i21 = 0; i21 < this.f6208n; i21++) {
            e eVar = this.f6190e.get(i21);
            int i22 = this.f6192f;
            if (eVar != e.LARGE) {
                if (eVar == e.MEDIUM) {
                    i22 = this.f6196h;
                } else {
                    i22 = eVar == e.SMALL ? this.f6194g : 0;
                }
            }
            if (f6185v0) {
                Log.d("COUIPageIndicator", "fixDotsLevel: i = " + i21 + " dotsize = " + i22 + " isInLayout = " + isInLayout());
            }
            View childAt = this.N.getChildAt(i21);
            if (childAt != null) {
                if (i22 == 0) {
                    childAt.setVisibility(8);
                } else {
                    View findViewById = childAt.findViewById(R$id.page_indicator_dot);
                    if (findViewById != null) {
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) findViewById.getLayoutParams();
                        layoutParams.width = i22;
                        layoutParams.height = i22;
                        findViewById.setLayoutParams(layoutParams);
                        childAt.setVisibility(0);
                    }
                }
            }
        }
        this.N.post(new Runnable() { // from class: com.coui.appcompat.indicator.e
            @Override // java.lang.Runnable
            public final void run() {
                COUIPageIndicator.this.a0(i10, f10, z10);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void Z() {
        b0(this.f6210o, 0.0f, this.f6207m0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void a0(int i10, float f10, boolean z10) {
        A0(i10);
        P(i10, f10, z10);
        this.f6213r = i10;
        this.f6214s = i10;
        RectF rectF = this.f6186a0;
        rectF.left = this.f6218w;
        rectF.right = this.f6219x;
        this.f6209n0 = null;
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void c0(boolean z10) {
        E0(this.f6213r, true, z10);
    }

    private void f0(float f10) {
        if (this.K) {
            if (!this.f6188c0.isRunning() && this.I) {
                this.W.left = f10 - this.f6192f;
                return;
            }
            RectF rectF = this.W;
            float f11 = f10 - rectF.left;
            int i10 = this.f6192f;
            if (f11 < i10) {
                rectF.left = f10 - i10;
                return;
            }
            return;
        }
        if (!this.I && this.f6209n0 == null) {
            RectF rectF2 = this.W;
            float f12 = f10 - rectF2.left;
            int i11 = this.f6192f;
            if (f12 < i11) {
                rectF2.left = f10 - i11;
                return;
            }
            return;
        }
        this.W.left = f10 - this.f6192f;
    }

    private void g0(float f10) {
        if (this.K) {
            if (!this.f6188c0.isRunning() && this.I) {
                this.W.right = f10 + this.f6192f;
                return;
            }
            RectF rectF = this.W;
            float f11 = rectF.right - f10;
            int i10 = this.f6192f;
            if (f11 < i10) {
                rectF.right = f10 + i10;
                return;
            }
            return;
        }
        if (!this.I && this.f6209n0 == null) {
            RectF rectF2 = this.W;
            float f12 = rectF2.right - f10;
            int i11 = this.f6192f;
            if (f12 < i11) {
                rectF2.right = f10 + i11;
                return;
            }
            return;
        }
        this.W.right = f10 + this.f6192f;
    }

    private int getFirstVisiblePosition() {
        for (int i10 = 0; i10 < this.f6190e.size(); i10++) {
            if (this.f6190e.get(i10) != e.GONE) {
                return i10;
            }
        }
        return 0;
    }

    private void k0() {
        this.K = true;
    }

    private void l0(float f10, int i10) {
        int i11;
        int i12 = 0;
        while (true) {
            i11 = i10 - 4;
            if (i12 >= i11) {
                break;
            }
            r0(i12, 0, 0, f10);
            i12++;
        }
        r0(i11, this.f6196h, this.f6194g, f10);
        int i13 = i10 - 3;
        r0(i13, this.f6192f, this.f6196h, f10);
        while (true) {
            i13++;
            if (i13 >= this.f6208n) {
                break;
            }
            int i14 = this.f6192f;
            r0(i13, i14, i14, f10);
        }
        r0(this.N.getChildCount() - 1, this.f6196h, this.f6192f, f10);
        P(i10, f10, this.f6207m0);
        View childAt = this.N.getChildAt(r0.getChildCount() - 1);
        RectF rectF = this.f6186a0;
        float x10 = childAt.getX();
        int i15 = this.f6192f;
        rectF.right = x10 + i15 + this.f6198i;
        RectF rectF2 = this.f6186a0;
        rectF2.left = rectF2.right - i15;
        RectF rectF3 = this.f6187b0;
        float x11 = this.N.getChildAt(i10).getX();
        int i16 = this.f6192f;
        rectF3.right = x11 + i16 + this.f6198i;
        RectF rectF4 = this.f6187b0;
        rectF4.left = rectF4.right - i16;
        float f11 = this.W.right - (i16 * 0.5f);
        this.f6220y = f11;
        float f12 = this.f6186a0.left + (i16 * 0.5f);
        this.f6221z = f12;
        this.U = O(this.f6213r, f11, f12, i16 * 0.5f, true);
        if (f10 == 1.0f) {
            float x12 = childAt.getX();
            int i17 = this.f6192f;
            float f13 = x12 + i17 + this.f6198i;
            this.f6219x = f13;
            this.f6218w = f13 - i17;
        }
        if (f6185v0) {
            Log.d("COUIPageIndicator", "resizeDotsWhenAboveSixScrollLeftAtLastPos position： " + i10 + " curr: " + this.f6210o + "\n mPortRect: " + this.f6186a0 + "\n mTrace: " + this.f6187b0);
        }
    }

    private void m0(float f10, int i10) {
        float f11;
        int i11 = i10 - 4;
        View childAt = this.N.getChildAt(i11);
        for (int i12 = 0; i12 < i11; i12++) {
            r0(i12, 0, 0, f10);
        }
        int r02 = r0(i11, this.f6196h, 0, f10);
        int i13 = i10 - 3;
        r0(i13, this.f6192f, this.f6196h, f10);
        int i14 = i10 + 2;
        View childAt2 = this.N.getChildAt(i14);
        while (true) {
            i13++;
            if (i13 >= i14) {
                break;
            }
            int i15 = this.f6192f;
            r0(i13, i15, i15, f10);
        }
        childAt2.setVisibility(0);
        if (childAt.getVisibility() == 8 || r02 > 3 || childAt2.getVisibility() != 0) {
            f11 = 0.0f;
        } else {
            childAt.setVisibility(8);
            f11 = this.f6192f;
        }
        int i16 = i10 + 1;
        View childAt3 = this.N.getChildAt(i16);
        r0(i16, this.f6196h, this.f6192f, f10);
        r0(i14, 0, this.f6196h, f10);
        while (true) {
            i14++;
            if (i14 >= this.f6208n) {
                break;
            } else {
                r0(i14, 0, 0, f10);
            }
        }
        this.f6203k0 = childAt3;
        this.f6205l0 = this.N.getChildAt(i10);
        P(i10, f10, this.f6207m0);
        RectF rectF = this.W;
        rectF.right -= f11;
        rectF.left -= f11;
        RectF rectF2 = this.f6186a0;
        float x10 = this.f6203k0.getX();
        int i17 = this.f6192f;
        rectF2.right = ((x10 + i17) + this.f6198i) - f11;
        RectF rectF3 = this.f6186a0;
        rectF3.left = rectF3.right - i17;
        RectF rectF4 = this.f6187b0;
        float x11 = this.f6205l0.getX();
        int i18 = this.f6192f;
        rectF4.right = ((x11 + i18) + this.f6198i) - f11;
        RectF rectF5 = this.f6187b0;
        rectF5.left = rectF5.right - i18;
        float f12 = this.W.right - (i18 * 0.5f);
        this.f6220y = f12;
        float f13 = this.f6186a0.left + (i18 * 0.5f);
        this.f6221z = f13;
        this.U = O(this.f6213r, f12, f13, i18 * 0.5f, true);
        if (f10 == 1.0f) {
            float x12 = this.f6203k0.getX();
            int i19 = this.f6192f;
            float f14 = ((x12 + i19) + this.f6198i) - f11;
            this.f6219x = f14;
            this.f6218w = f14 - i19;
        }
        if (f6185v0) {
            Log.d("COUIPageIndicator", "resizeDotsWhenAboveSixScrollLeftAtMidPos position： " + i10 + " curr: " + this.f6210o + "\n mPortRect: " + this.f6186a0 + "\n mTrace: " + this.f6187b0);
        }
    }

    private void n0(float f10, int i10) {
        r0(0, this.f6192f, this.f6196h, f10);
        int i11 = i10 + 1;
        for (int i12 = 1; i12 < i11; i12++) {
            int i13 = this.f6192f;
            r0(i12, i13, i13, f10);
        }
        r0(i11, this.f6196h, this.f6192f, f10);
        int i14 = i10 + 2;
        r0(i14, this.f6194g, this.f6196h, f10);
        for (int i15 = i14 + 1; i15 < this.f6208n; i15++) {
            r0(i15, 0, 0, f10);
        }
        P(i10, f10, this.f6207m0);
        View childAt = this.N.getChildAt(i11);
        RectF rectF = this.f6186a0;
        float x10 = childAt.getX();
        int i16 = this.f6192f;
        rectF.right = x10 + i16 + this.f6198i;
        RectF rectF2 = this.f6186a0;
        rectF2.left = rectF2.right - i16;
        RectF rectF3 = this.f6187b0;
        float x11 = this.N.getChildAt(i10).getX();
        int i17 = this.f6192f;
        rectF3.right = x11 + i17 + this.f6198i;
        RectF rectF4 = this.f6187b0;
        rectF4.left = rectF4.right - i17;
        float f11 = this.W.right - (i17 * 0.5f);
        this.f6220y = f11;
        float f12 = this.f6186a0.left + (i17 * 0.5f);
        this.f6221z = f12;
        this.U = O(this.f6213r, f11, f12, i17 * 0.5f, true);
        if (f10 == 1.0f) {
            float x12 = childAt.getX();
            int i18 = this.f6192f;
            float f13 = x12 + i18 + this.f6198i;
            this.f6219x = f13;
            this.f6218w = f13 - i18;
        }
        if (f6185v0) {
            Log.d("COUIPageIndicator", "resizeDotsWhenAboveSixScrollLeftAtPosThree position： " + i10 + " curr: " + this.f6210o + "\n mPortRect: " + this.f6186a0 + "\n mTrace: " + this.f6187b0);
        }
    }

    private void o0(float f10, int i10) {
        int i11;
        int i12 = i10 + 1;
        View childAt = this.N.getChildAt(0);
        View childAt2 = this.N.getChildAt(i12);
        r0(0, this.f6196h, this.f6192f, f10);
        int i13 = 1;
        while (true) {
            i11 = i12 + 3;
            if (i13 >= i11) {
                break;
            }
            int i14 = this.f6192f;
            r0(i13, i14, i14, f10);
            i13++;
        }
        r0(i11, this.f6192f, this.f6196h, f10);
        int i15 = i12 + 4;
        r0(i15, this.f6196h, this.f6194g, f10);
        for (int i16 = i15 + 1; i16 < this.f6208n; i16++) {
            r0(i16, 0, 0, f10);
        }
        this.f6186a0.left = childAt.getX() + this.f6198i;
        RectF rectF = this.f6186a0;
        rectF.right = rectF.left + this.f6192f;
        this.f6187b0.left = childAt2.getX() + this.f6198i;
        RectF rectF2 = this.f6187b0;
        rectF2.right = rectF2.left + this.f6192f;
        if (f10 == 1.0f) {
            float x10 = childAt.getX() + this.f6198i;
            this.f6218w = x10;
            this.f6219x = x10 + this.f6192f;
        }
        if (f6185v0) {
            Log.d("COUIPageIndicator", "resizeDotsWhenAboveSixScrollRightAtFirstPos position： " + i10 + " curr: " + this.f6210o + "\n mPortRect: " + this.f6186a0 + "\n mTrace: " + this.f6187b0);
        }
    }

    private void p0(float f10, int i10) {
        float f11;
        int i11;
        int i12 = i10 + 1;
        int i13 = i12 - 2;
        View childAt = this.N.getChildAt(i13);
        for (int i14 = 0; i14 < i13; i14++) {
            r0(i14, 0, 0, f10);
        }
        r0(i13, 0, this.f6196h, f10);
        if (childAt.getVisibility() == 8) {
            childAt.setVisibility(0);
            f11 = this.f6192f;
        } else {
            f11 = 0.0f;
        }
        int i15 = i12 - 1;
        View childAt2 = this.N.getChildAt(i15);
        r0(i15, this.f6196h, this.f6192f, f10);
        while (true) {
            i15++;
            i11 = i12 + 3;
            if (i15 >= i11) {
                break;
            }
            int i16 = this.f6192f;
            r0(i15, i16, i16, f10);
        }
        r0(i11, this.f6192f, this.f6196h, f10);
        int i17 = i12 + 4;
        View childAt3 = this.N.getChildAt(i17);
        if (childAt3 != null) {
            float r02 = r0(i17, this.f6196h, 0, f10);
            if (childAt3.getVisibility() != 8 && r02 <= 3.0f && childAt.getVisibility() == 0) {
                childAt3.setVisibility(8);
            }
        }
        while (true) {
            i17++;
            if (i17 >= this.f6208n) {
                break;
            } else {
                r0(i17, 0, 0, f10);
            }
        }
        RectF rectF = this.W;
        rectF.right += f11;
        rectF.left += f11;
        this.f6186a0.left = childAt2.getX() + this.f6198i + f11;
        RectF rectF2 = this.f6186a0;
        rectF2.right = rectF2.left + this.f6192f;
        this.f6187b0.left = this.N.getChildAt(i12).getX() + this.f6198i + f11;
        RectF rectF3 = this.f6187b0;
        rectF3.right = rectF3.left + this.f6192f;
        if (f10 == 1.0f) {
            float x10 = childAt2.getX() + this.f6198i + f11;
            this.f6218w = x10;
            this.f6219x = x10 + this.f6192f;
        }
        if (f6185v0) {
            Log.d("COUIPageIndicator", "resizeDotsWhenAboveSixScrollRightAtMidPos position： " + i10 + " curr: " + this.f6210o + "\n mPortRect: " + this.f6186a0 + "\n mTrace: " + this.f6187b0);
        }
    }

    private void q0(float f10, int i10) {
        int i11;
        int i12;
        int i13 = i10 + 1;
        int i14 = 0;
        while (true) {
            i11 = i13 - 2;
            if (i14 >= i11) {
                break;
            }
            r0(i14, 0, 0, f10);
            i14++;
        }
        r0(i11, this.f6194g, this.f6196h, f10);
        View childAt = this.N.getChildAt(i10);
        r0(i10, this.f6196h, this.f6192f, f10);
        int i15 = i13;
        while (true) {
            i12 = i13 + 3;
            if (i15 >= i12) {
                break;
            }
            int i16 = this.f6192f;
            r0(i15, i16, i16, f10);
            i15++;
        }
        r0(i12, this.f6192f, this.f6196h, f10);
        this.f6186a0.left = childAt.getX() + this.f6198i;
        RectF rectF = this.f6186a0;
        rectF.right = rectF.left + this.f6192f;
        this.f6187b0.left = this.N.getChildAt(i13).getX() + this.f6198i;
        RectF rectF2 = this.f6187b0;
        rectF2.right = rectF2.left + this.f6192f;
        if (f10 == 1.0f) {
            float x10 = childAt.getX() + this.f6198i;
            this.f6218w = x10;
            this.f6219x = x10 + this.f6192f;
        }
        if (f6185v0) {
            Log.d("COUIPageIndicator", "resizeDotsWhenAboveSixScrollRightAtThirdPos position： " + i10 + " curr: " + this.f6210o + "\n mPortRect: " + this.f6186a0 + "\n mTrace: " + this.f6187b0);
        }
    }

    private int r0(int i10, int i11, int i12, float f10) {
        View childAt = this.N.getChildAt(i10);
        if (childAt != null) {
            float f11 = f10 >= 0.75f ? 1.0f : f10;
            if (f10 < 0.25f) {
                f11 = 0.0f;
            }
            View findViewById = childAt.findViewById(R$id.page_indicator_dot);
            float f12 = i11 + ((i12 - i11) * f11);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) findViewById.getLayoutParams();
            int i13 = (int) f12;
            layoutParams.width = i13;
            layoutParams.height = i13;
            findViewById.setLayoutParams(layoutParams);
            if (f12 < 3.0f && i12 == 0) {
                childAt.setVisibility(8);
            }
            if (f10 > 0.75f) {
                f10 = 1.0f;
            }
            if ((f10 >= 0.25f ? f10 : 0.0f) == 1.0f) {
                y0(i10, i12);
            }
            r1 = f12;
        } else if (f6185v0) {
            Log.e("COUIPageIndicator", "resize specific position dot error, out of bounds: " + i10);
        }
        return (int) r1;
    }

    private void s0() {
        this.K = false;
    }

    private void t0(float f10, boolean z10) {
        int i10;
        int i11;
        if (z10) {
            if (f10 < 0.4f) {
                i11 = (int) (f10 * 2.5f * 255.0f);
                this.Q.setAlpha(i11);
                this.R.setAlpha(i11);
            } else if (f10 >= 0.8f) {
                i10 = (int) ((1.0f - f10) * 5.0f * 255.0f);
                this.P.setAlpha((int) (f10 * 255.0f));
                this.S.setAlpha(i10);
                this.T.setAlpha(i10);
                i11 = i10;
            } else {
                this.P.setAlpha(255);
                this.Q.setAlpha(255);
                this.R.setAlpha(255);
                this.S.setAlpha(255);
                this.T.setAlpha(255);
                i11 = 0;
            }
        } else if (f10 > 0.6f) {
            i11 = (int) ((1.0f - f10) * 2.5f * 255.0f);
            this.Q.setAlpha(i11);
            this.R.setAlpha(i11);
        } else if (f10 <= 0.19999999f) {
            i10 = (int) (f10 * 5.0f * 255.0f);
            this.P.setAlpha((int) ((1.0f - f10) * 5.0f * 255.0f));
            this.S.setAlpha(i10);
            this.T.setAlpha(i10);
            i11 = i10;
        } else {
            this.P.setAlpha(255);
            this.Q.setAlpha(255);
            this.R.setAlpha(255);
            this.S.setAlpha(255);
            this.T.setAlpha(255);
            i11 = 0;
        }
        if (f6185v0) {
            Log.d("COUIPageIndicator", "setDotAlpha alpha is " + i11);
        }
    }

    private void u0(boolean z10, View view, int i10) {
        GradientDrawable gradientDrawable = (GradientDrawable) view.getBackground();
        if (z10) {
            gradientDrawable.setStroke(this.f6202k, i10);
        } else {
            gradientDrawable.setColor(i10);
        }
        gradientDrawable.setCornerRadius(this.f6204l);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void w0() {
        if (this.f6188c0 == null) {
            return;
        }
        x0();
        this.f6188c0.start();
    }

    private void y0(int i10, int i11) {
        if (this.f6190e.size() <= i10) {
            this.f6190e.set(i10, e.LARGE);
            return;
        }
        if (Math.abs(i11 - this.f6192f) == 0) {
            this.f6190e.set(i10, e.LARGE);
            return;
        }
        if (Math.abs(i11 - this.f6196h) == 0) {
            this.f6190e.set(i10, e.MEDIUM);
        } else if (Math.abs(i11 - this.f6194g) == 0) {
            this.f6190e.set(i10, e.SMALL);
        } else {
            this.f6190e.set(i10, e.GONE);
        }
    }

    private void z0(int i10, boolean z10) {
        RectF rectF = this.f6187b0;
        rectF.top = 0.0f;
        rectF.bottom = this.f6192f;
        int i11 = this.f6208n;
        if (i11 == 1) {
            rectF.setEmpty();
        } else if (z10) {
            if (i10 == i11 - 1) {
                rectF.setEmpty();
            } else {
                View childAt = this.N.getChildAt(i10 + 1);
                if (childAt != null) {
                    this.f6187b0.right = ((childAt.getX() + childAt.findViewById(R$id.page_indicator_dot).getMeasuredWidth()) + this.f6198i) - (isLayoutRtl() ? -this.f6215t : this.f6215t);
                }
            }
        } else if (i10 == 0) {
            rectF.setEmpty();
        } else {
            View childAt2 = this.N.getChildAt(i10 - 1);
            if (childAt2 != null) {
                this.f6187b0.right = childAt2.getX() + childAt2.findViewById(R$id.page_indicator_dot).getMeasuredWidth() + this.f6198i + (isLayoutRtl() ? -this.f6215t : this.f6215t);
            }
        }
        if (!this.f6187b0.isEmpty()) {
            RectF rectF2 = this.f6187b0;
            rectF2.left = rectF2.right - this.f6192f;
        } else {
            RectF rectF3 = this.f6187b0;
            rectF3.left = rectF3.right;
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        RectF rectF = this.W;
        int i10 = this.f6204l;
        canvas.drawRoundRect(rectF, i10, i10, this.P);
        RectF rectF2 = this.f6186a0;
        int i11 = this.f6204l;
        canvas.drawRoundRect(rectF2, i11, i11, this.Q);
        canvas.drawPath(this.U, this.R);
        RectF rectF3 = this.f6187b0;
        int i12 = this.f6204l;
        canvas.drawRoundRect(rectF3, i12, i12, this.S);
        canvas.drawPath(this.V, this.T);
    }

    public int getDotsCount() {
        return this.f6208n;
    }

    public void h0(int i10) {
        if (f6185v0) {
            Log.d("COUIPageIndicator", "onPageScrollStateChanged state =：" + i10 + ",mTranceCutTailRight" + this.H);
        }
        if (i10 == 1) {
            k0();
            U(false);
            this.f6188c0.pause();
            if (this.I) {
                this.I = false;
            }
        } else if (i10 == 2) {
            s0();
            this.f6188c0.resume();
        } else if (i10 == 0 && (this.K || !this.M)) {
            if (this.f6189d0.hasMessages(17)) {
                this.f6189d0.removeMessages(17);
            }
            x0();
            this.f6189d0.sendEmptyMessageDelayed(17, 0L);
        }
        if (i10 == 0) {
            S();
        }
        this.M = false;
    }

    public void i0(final int i10, final float f10, int i11) {
        boolean isLayoutRtl = isLayoutRtl();
        if (f6185v0) {
            Log.d("COUIPageIndicator", "onPageScrolled position =:" + i10 + ",mCurrentPosition =:" + this.f6210o + ",mLastPosition =:" + this.f6211p + ",positionOffset =:" + f10 + ",mFinalRight =:" + this.f6219x + ",mFinalLeft =:" + this.f6218w + "mTraceLeft =:" + this.f6216u + ",mTraceRight =:" + this.f6217v + "\n mTraceRect: " + this.W + "\nmIsAnimated =:" + this.I + ",mIsAnimatorCanceled =:" + this.J + ",mNeedSettlePositionTemp =:" + this.L + ",mIsPaused =:" + this.K + ",mIsRtl =:" + isLayoutRtl);
        }
        final boolean z10 = !isLayoutRtl ? this.f6210o > i10 : this.f6210o <= i10;
        this.f6207m0 = z10;
        if (i10 == this.f6210o) {
            this.f6201j0 = f10;
            if (f10 > 0.75f) {
                this.f6201j0 = 1.0f;
            }
        }
        if (f10 == 0.0f && this.f6209n0 != null) {
            this.N.post(new Runnable() { // from class: com.coui.appcompat.indicator.d
                @Override // java.lang.Runnable
                public final void run() {
                    COUIPageIndicator.this.b0(i10, f10, z10);
                }
            });
        }
        P(i10, f10, z10);
        if (z10) {
            this.f6220y = this.W.right - (this.f6192f * 0.5f);
        } else {
            this.f6220y = this.W.left + (this.f6192f * 0.5f);
        }
        if (isInLayout()) {
            post(new Runnable() { // from class: com.coui.appcompat.indicator.f
                @Override // java.lang.Runnable
                public final void run() {
                    COUIPageIndicator.this.c0(z10);
                }
            });
        } else {
            E0(this.f6213r, true, z10);
        }
        float f11 = this.f6186a0.left;
        int i12 = this.f6192f;
        float f12 = (i12 * 0.5f) + f11;
        this.f6221z = f12;
        this.U = O(this.f6213r, this.f6220y, f12, i12 * 0.5f, true);
        if (f10 == 0.0f) {
            this.f6210o = i10;
            U(true);
        } else {
            if (isLayoutRtl()) {
                if (this.f6207m0) {
                    int i13 = i10 + 1;
                    if (this.f6210o > i13) {
                        this.f6210o = i13;
                    }
                } else if (this.f6210o != i10) {
                    this.f6210o = i10;
                }
            } else if (this.f6207m0) {
                if (this.f6210o != i10) {
                    this.f6210o = i10;
                }
            } else {
                int i14 = i10 + 1;
                if (this.f6210o > i14) {
                    this.f6210o = i14;
                }
            }
            if (isLayoutRtl()) {
                t0(1.0f - f10, z10);
            } else {
                t0(f10, z10);
            }
            if (this.f6208n >= 6 && this.N.getChildCount() >= 6) {
                V(i10, z10, f10);
            }
        }
        invalidate();
    }

    public boolean isLayoutRtl() {
        return getLayoutDirection() == 1;
    }

    public void j0(int i10) {
        this.M = true;
        if (this.f6211p != i10 && this.I) {
            this.I = false;
        }
        this.H = !isLayoutRtl() ? this.f6211p <= i10 : this.f6211p > i10;
        int abs = Math.abs(this.f6211p - i10);
        if (abs < 1) {
            abs = 1;
        }
        this.f6188c0.setDuration(abs * 300);
        A0(i10);
        int i11 = this.f6211p;
        this.f6214s = i11;
        E0(i11, false, i11 < i10);
        if (f6185v0) {
            Log.d("COUIPageIndicator", "onPageSelected position =：" + i10 + ",mCurrentPosition = " + this.f6210o + ",mLastPosition = " + this.f6211p + "\n mDepartRect: " + this.f6187b0 + "\n mTraceRect: " + this.W);
        }
        if (this.f6211p != i10) {
            if (this.f6189d0.hasMessages(17)) {
                this.f6189d0.removeMessages(17);
            }
            x0();
            this.f6189d0.sendEmptyMessageDelayed(17, 0L);
        } else if (this.f6189d0.hasMessages(17)) {
            this.f6189d0.removeMessages(17);
        }
        this.f6211p = i10;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        super.onMeasure(this.f6191e0, this.f6192f);
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        IndicatorSavedState indicatorSavedState = (IndicatorSavedState) parcelable;
        super.onRestoreInstanceState(indicatorSavedState.getSuperState());
        this.f6209n0 = indicatorSavedState;
        new ArrayList();
        List<String> list = indicatorSavedState.f6222e;
        this.f6190e.clear();
        for (String str : list) {
            e eVar = e.LARGE;
            if (str.equals(eVar.toString())) {
                this.f6190e.add(eVar);
            } else {
                e eVar2 = e.MEDIUM;
                if (str.equals(eVar2.toString())) {
                    this.f6190e.add(eVar2);
                } else {
                    e eVar3 = e.SMALL;
                    if (str.equals(eVar3.toString())) {
                        this.f6190e.add(eVar3);
                    } else {
                        this.f6190e.add(e.GONE);
                    }
                }
            }
        }
        this.f6208n = this.f6190e.size();
        if (f6185v0) {
            Log.d("COUIPageIndicator", "onRestoreInstanceState " + this.f6190e + " indicatorDotLevel: " + list + " visivle: " + getVisibility());
        }
        invalidate();
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        IndicatorSavedState indicatorSavedState = new IndicatorSavedState(super.onSaveInstanceState());
        ArrayList arrayList = new ArrayList(this.f6190e.size());
        Iterator<e> it = this.f6190e.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().toString());
        }
        indicatorSavedState.f6222e = arrayList;
        this.f6209n0 = null;
        if (f6185v0) {
            Log.d("COUIPageIndicator", "onSaveInstanceState " + indicatorSavedState.f6222e + " indicatorDotLevel: " + arrayList);
        }
        return indicatorSavedState;
    }

    /* renamed from: setCurrentPosition, reason: merged with bridge method [inline-methods] */
    public void d0(final int i10) {
        if (isInLayout()) {
            Log.w("COUIPageIndicator", "WARNING: Avoid calling this method during performLayout()!\nMaybe you can try post(() -> setCurrentPosition(newCount));");
            post(new Runnable() { // from class: com.coui.appcompat.indicator.c
                @Override // java.lang.Runnable
                public final void run() {
                    COUIPageIndicator.this.d0(i10);
                }
            });
            return;
        }
        if (f6185v0) {
            Log.d("COUIPageIndicator", "setCurrentPosition: " + i10 + " total dots = " + this.f6208n);
        }
        int i11 = this.f6208n;
        if (i10 >= i11) {
            return;
        }
        this.f6211p = i10;
        this.f6210o = i10;
        if (i11 >= 6) {
            int i12 = 0;
            while (true) {
                if (i12 >= this.f6190e.size()) {
                    break;
                }
                if (this.f6190e.get(i12) == e.LARGE) {
                    this.f6212q = i12;
                    break;
                }
                i12++;
            }
            int i13 = this.f6212q;
            if (i10 < i13) {
                this.f6212q = i10;
            } else if (i10 > i13 + 3) {
                this.f6212q = i10 - 3;
            }
            b0(i10, 0.0f, true);
        }
        e0();
    }

    public void setDotCornerRadius(int i10) {
        this.f6204l = i10;
    }

    public void setDotSize(int i10) {
        this.f6192f = i10;
    }

    public void setDotSpacing(int i10) {
        this.f6198i = i10;
    }

    public void setDotStrokeWidth(int i10) {
        this.f6202k = i10;
    }

    public void setDotsCount(int i10) {
        if (isInLayout()) {
            Log.w("COUIPageIndicator", "WARNING: Avoid calling this method during performLayout()!\nMaybe you can try post(() -> setDotsCount(newCount));");
        }
        int i11 = 0;
        while (true) {
            if (i11 >= this.f6190e.size()) {
                break;
            }
            if (this.f6190e.get(i11) == e.LARGE) {
                this.f6212q = i11;
                break;
            }
            i11++;
        }
        boolean z10 = f6185v0;
        if (z10) {
            Log.d("COUIPageIndicator", "setDotsCount: current dot count = " + this.f6208n + " set count = " + i10);
            Log.w("COUIPageIndicator", "Before setDotsCount, First large dot index = " + this.f6212q + " Current position = " + this.f6210o);
        }
        this.N.removeAllViews();
        this.O.clear();
        this.f6190e.clear();
        this.f6208n = i10;
        this.f6199i0 = i10;
        if (i10 >= 6) {
            this.f6199i0 = 6;
        }
        if (this.f6210o >= i10) {
            this.f6210o = Math.max(0, i10 - 1);
        }
        int i12 = this.f6210o;
        this.f6213r = i12;
        this.f6211p = i12;
        C0();
        if (this.f6208n == 0) {
            return;
        }
        I(i10);
        b0(this.f6210o, 0.0f, true);
        post(new Runnable() { // from class: com.coui.appcompat.indicator.b
            @Override // java.lang.Runnable
            public final void run() {
                COUIPageIndicator.this.e0();
            }
        });
        if (z10) {
            Log.d("COUIPageIndicator", "setDotsCount =：" + i10 + ",mWidth = :" + this.f6191e0 + ",rtl =:" + isLayoutRtl());
        }
    }

    public void setIsClickable(boolean z10) {
        this.F = z10;
    }

    public void setOnDotClickListener(f fVar) {
        this.f6193f0 = fVar;
    }

    public void setPageIndicatorDotsColor(int i10) {
        this.f6200j = i10;
        List<View> list = this.O;
        if (list == null || list.isEmpty()) {
            return;
        }
        Iterator<View> it = this.O.iterator();
        while (it.hasNext()) {
            u0(this.G, it.next(), i10);
        }
    }

    public void setTraceDotColor(int i10) {
        this.f6206m = i10;
        this.P.setColor(i10);
        this.Q.setColor(i10);
        this.R.setColor(i10);
        this.S.setColor(i10);
        this.T.setColor(i10);
    }

    /* renamed from: v0, reason: merged with bridge method [inline-methods] */
    public void e0() {
        A0(this.f6210o);
        RectF rectF = this.W;
        float f10 = this.f6218w;
        rectF.left = f10;
        float f11 = this.f6219x;
        rectF.right = f11;
        RectF rectF2 = this.f6186a0;
        rectF2.left = f10;
        rectF2.right = f11;
        this.f6187b0.setEmpty();
        T();
        if (this.f6189d0.hasMessages(17)) {
            this.f6189d0.removeMessages(17);
        }
        invalidate();
    }

    public void x0() {
        if (!this.J) {
            this.J = true;
        }
        ValueAnimator valueAnimator = this.f6188c0;
        if (valueAnimator == null || !valueAnimator.isRunning()) {
            return;
        }
        this.f6188c0.cancel();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class IndicatorSavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<IndicatorSavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        List<String> f6222e;

        /* loaded from: classes.dex */
        class a implements Parcelable.Creator<IndicatorSavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public IndicatorSavedState createFromParcel(Parcel parcel) {
                return new IndicatorSavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b, reason: merged with bridge method [inline-methods] */
            public IndicatorSavedState[] newArray(int i10) {
                return new IndicatorSavedState[i10];
            }
        }

        public IndicatorSavedState(Parcel parcel) {
            super(parcel);
            j(parcel);
        }

        private void j(Parcel parcel) {
            this.f6222e = parcel.createStringArrayList();
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeStringList(this.f6222e);
        }

        public IndicatorSavedState(Parcelable parcelable) {
            super(parcelable);
        }
    }

    public COUIPageIndicator(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiPageIndicatorStyle);
    }

    public COUIPageIndicator(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, COUIContextUtil.e(context) ? R$style.Widget_COUI_COUIPageIndicator_Dark : R$style.Widget_COUI_COUIPageIndicator);
    }

    @TargetApi(21)
    public COUIPageIndicator(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f6212q = 0;
        this.f6215t = 0;
        this.f6216u = 0.0f;
        this.f6217v = 0.0f;
        this.f6218w = 0.0f;
        this.f6219x = 0.0f;
        this.f6220y = 0.0f;
        this.f6221z = 0.0f;
        this.A = 0.0f;
        this.B = 0.0f;
        this.C = 0.0f;
        this.D = 0.0f;
        this.E = 0.0f;
        this.H = false;
        this.I = false;
        this.J = false;
        this.K = false;
        this.L = false;
        this.M = false;
        this.U = new Path();
        this.V = new Path();
        this.W = new RectF();
        this.f6186a0 = new RectF();
        this.f6187b0 = new RectF();
        if (attributeSet != null && attributeSet.getStyleAttribute() != 0) {
            this.f6195g0 = attributeSet.getStyleAttribute();
        } else {
            this.f6195g0 = i10;
        }
        this.f6197h0 = context;
        COUIDarkModeUtil.b(this, false);
        this.O = new ArrayList();
        this.f6190e = new ArrayList();
        this.G = false;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIPageIndicator, i10, i11);
            this.f6206m = obtainStyledAttributes.getColor(R$styleable.COUIPageIndicator_traceDotColor, 0);
            this.f6200j = obtainStyledAttributes.getColor(R$styleable.COUIPageIndicator_dotColor, 0);
            this.f6192f = (int) obtainStyledAttributes.getDimension(R$styleable.COUIPageIndicator_dotSize, 0.0f);
            this.f6194g = (int) obtainStyledAttributes.getDimension(R$styleable.COUIPageIndicator_dotSizeSmall, 0.0f);
            this.f6196h = (int) obtainStyledAttributes.getDimension(R$styleable.COUIPageIndicator_dotSizeMedium, 0.0f);
            this.f6198i = (int) obtainStyledAttributes.getDimension(R$styleable.COUIPageIndicator_dotSpacing, 0.0f);
            this.f6204l = (int) obtainStyledAttributes.getDimension(R$styleable.COUIPageIndicator_dotCornerRadius, this.f6192f * 0.5f);
            this.F = obtainStyledAttributes.getBoolean(R$styleable.COUIPageIndicator_dotClickable, true);
            this.f6202k = (int) obtainStyledAttributes.getDimension(R$styleable.COUIPageIndicator_dotStrokeWidth, 0.0f);
            obtainStyledAttributes.recycle();
        }
        RectF rectF = this.W;
        rectF.top = 0.0f;
        rectF.bottom = this.f6192f;
        L();
        Paint paint = new Paint(1);
        this.P = paint;
        paint.setStyle(Paint.Style.FILL);
        this.P.setColor(this.f6206m);
        Paint paint2 = new Paint(1);
        this.Q = paint2;
        paint2.setStyle(Paint.Style.FILL);
        this.Q.setColor(this.f6206m);
        Paint paint3 = new Paint(1);
        this.R = paint3;
        paint3.setStyle(Paint.Style.FILL);
        this.R.setColor(this.f6206m);
        Paint paint4 = new Paint(1);
        this.S = paint4;
        paint4.setStyle(Paint.Style.FILL);
        this.S.setColor(this.f6206m);
        Paint paint5 = new Paint(1);
        this.T = paint5;
        paint5.setStyle(Paint.Style.FILL);
        this.T.setColor(this.f6206m);
        this.f6215t = this.f6192f + (this.f6198i * 2);
        this.f6189d0 = new a();
        this.N = new LinearLayout(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        this.N.setGravity(16);
        this.N.setLayoutParams(layoutParams);
        this.N.setOrientation(0);
        addView(this.N);
    }
}
