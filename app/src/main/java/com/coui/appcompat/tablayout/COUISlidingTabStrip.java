package com.coui.appcompat.tablayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import m1.COUIEaseInterpolator;

/* loaded from: classes.dex */
public class COUISlidingTabStrip extends LinearLayout {

    /* renamed from: e, reason: collision with root package name */
    private final Paint f7743e;

    /* renamed from: f, reason: collision with root package name */
    private final Paint f7744f;

    /* renamed from: g, reason: collision with root package name */
    private final Paint f7745g;

    /* renamed from: h, reason: collision with root package name */
    int f7746h;

    /* renamed from: i, reason: collision with root package name */
    float f7747i;

    /* renamed from: j, reason: collision with root package name */
    float f7748j;

    /* renamed from: k, reason: collision with root package name */
    float f7749k;

    /* renamed from: l, reason: collision with root package name */
    protected int f7750l;

    /* renamed from: m, reason: collision with root package name */
    private int f7751m;

    /* renamed from: n, reason: collision with root package name */
    private int f7752n;

    /* renamed from: o, reason: collision with root package name */
    private int f7753o;

    /* renamed from: p, reason: collision with root package name */
    private int f7754p;

    /* renamed from: q, reason: collision with root package name */
    private ValueAnimator f7755q;

    /* renamed from: r, reason: collision with root package name */
    private int f7756r;

    /* renamed from: s, reason: collision with root package name */
    private int f7757s;

    /* renamed from: t, reason: collision with root package name */
    private int f7758t;

    /* renamed from: u, reason: collision with root package name */
    private float f7759u;

    /* renamed from: v, reason: collision with root package name */
    private int f7760v;

    /* renamed from: w, reason: collision with root package name */
    private COUITabLayout f7761w;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ TextView f7762a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ ArgbEvaluator f7763b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f7764c;

        /* renamed from: d, reason: collision with root package name */
        final /* synthetic */ COUITabView f7765d;

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ int f7766e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ int f7767f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ int f7768g;

        /* renamed from: h, reason: collision with root package name */
        final /* synthetic */ int f7769h;

        /* renamed from: i, reason: collision with root package name */
        final /* synthetic */ int f7770i;

        /* renamed from: j, reason: collision with root package name */
        final /* synthetic */ int f7771j;

        /* renamed from: k, reason: collision with root package name */
        final /* synthetic */ int f7772k;

        a(TextView textView, ArgbEvaluator argbEvaluator, int i10, COUITabView cOUITabView, int i11, int i12, int i13, int i14, int i15, int i16, int i17) {
            this.f7762a = textView;
            this.f7763b = argbEvaluator;
            this.f7764c = i10;
            this.f7765d = cOUITabView;
            this.f7766e = i11;
            this.f7767f = i12;
            this.f7768g = i13;
            this.f7769h = i14;
            this.f7770i = i15;
            this.f7771j = i16;
            this.f7772k = i17;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int i10;
            int i11;
            float animatedFraction = valueAnimator.getAnimatedFraction();
            this.f7762a.setTextColor(((Integer) this.f7763b.evaluate(animatedFraction, Integer.valueOf(this.f7764c), Integer.valueOf(COUISlidingTabStrip.this.f7761w.O))).intValue());
            this.f7765d.getTextView().setTextColor(((Integer) this.f7763b.evaluate(animatedFraction, Integer.valueOf(this.f7766e), Integer.valueOf(COUISlidingTabStrip.this.f7761w.N))).intValue());
            COUISlidingTabStrip cOUISlidingTabStrip = COUISlidingTabStrip.this;
            if (cOUISlidingTabStrip.f7749k == 0.0f) {
                cOUISlidingTabStrip.f7749k = animatedFraction;
            }
            if (animatedFraction - cOUISlidingTabStrip.f7749k > 0.0f) {
                int i12 = this.f7767f;
                i10 = (int) ((i12 - r2) + (this.f7769h * animatedFraction));
                i11 = (int) (this.f7768g + (this.f7770i * animatedFraction));
            } else {
                int i13 = this.f7771j;
                float f10 = 1.0f - animatedFraction;
                i10 = (int) ((i13 - r2) - (this.f7769h * f10));
                i11 = (int) (this.f7772k - (this.f7770i * f10));
            }
            cOUISlidingTabStrip.j(i11, i10 + i11);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f7774a;

        b(int i10) {
            this.f7774a = i10;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUISlidingTabStrip cOUISlidingTabStrip = COUISlidingTabStrip.this;
            cOUISlidingTabStrip.f7746h = this.f7774a;
            cOUISlidingTabStrip.f7747i = 0.0f;
            cOUISlidingTabStrip.o();
            COUISlidingTabStrip.this.f7761w.Z();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f7776a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ int f7777b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f7778c;

        /* renamed from: d, reason: collision with root package name */
        final /* synthetic */ int f7779d;

        c(int i10, int i11, int i12, int i13) {
            this.f7776a = i10;
            this.f7777b = i11;
            this.f7778c = i12;
            this.f7779d = i13;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float animatedFraction = valueAnimator.getAnimatedFraction();
            COUISlidingTabStrip.this.j(COUIAnimationUtils.a(this.f7776a, this.f7777b, animatedFraction), COUIAnimationUtils.a(this.f7778c, this.f7779d, animatedFraction));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f7781a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ COUITabView f7782b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ COUITabView f7783c;

        d(int i10, COUITabView cOUITabView, COUITabView cOUITabView2) {
            this.f7781a = i10;
            this.f7782b = cOUITabView;
            this.f7783c = cOUITabView2;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUISlidingTabStrip cOUISlidingTabStrip = COUISlidingTabStrip.this;
            cOUISlidingTabStrip.f7746h = this.f7781a;
            cOUISlidingTabStrip.f7747i = 0.0f;
            if (this.f7782b.getTextView() != null) {
                this.f7782b.getTextView().setTextColor(COUISlidingTabStrip.this.f7761w.O);
            }
            if (this.f7783c.getTextView() != null) {
                this.f7783c.getTextView().setTextColor(COUISlidingTabStrip.this.f7761w.N);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public COUISlidingTabStrip(Context context, COUITabLayout cOUITabLayout) {
        super(context);
        this.f7746h = -1;
        this.f7751m = -1;
        this.f7752n = -1;
        this.f7753o = -1;
        this.f7754p = 0;
        this.f7760v = -1;
        this.f7761w = cOUITabLayout;
        setWillNotDraw(false);
        this.f7743e = new Paint();
        this.f7744f = new Paint();
        this.f7745g = new Paint();
        setGravity(17);
    }

    private int e(int i10) {
        int width = ((this.f7761w.getWidth() - this.f7761w.getPaddingLeft()) - this.f7761w.getPaddingRight()) - getWidth();
        return (!g() || width <= 0) ? i10 : i10 + width;
    }

    private int f(int i10) {
        int width = ((this.f7761w.getWidth() - this.f7761w.getPaddingLeft()) - this.f7761w.getPaddingRight()) - getWidth();
        return (!g() || width <= 0) ? i10 : i10 + width;
    }

    private boolean g() {
        return ViewCompat.x(this) == 1;
    }

    private void h(int i10, int i11) {
        int tabMinMargin;
        int childCount = getChildCount();
        int i12 = i10 - i11;
        int i13 = i12 / (childCount + 1);
        if (i13 >= this.f7761w.getTabMinMargin()) {
            tabMinMargin = i13 / 2;
            l(tabMinMargin, tabMinMargin);
        } else {
            tabMinMargin = ((i12 - (this.f7761w.getTabMinMargin() * 2)) / (childCount - 1)) / 2;
            l(this.f7761w.getTabMinMargin() - tabMinMargin, this.f7761w.getTabMinMargin() - tabMinMargin);
        }
        for (int i14 = 0; i14 < childCount; i14++) {
            View childAt = getChildAt(i14);
            n(childAt, tabMinMargin, tabMinMargin, childAt.getMeasuredWidth());
        }
    }

    private void i(COUITabView cOUITabView, int i10, int i11) {
        if (cOUITabView.getTextView() != null) {
            cOUITabView.getTextView().getLayoutParams().width = -2;
        }
        if (cOUITabView.getTextView() != null && cOUITabView.getHintRedDot() != null && cOUITabView.getHintRedDot().getVisibility() != 8) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) cOUITabView.getHintRedDot().getLayoutParams();
            if (cOUITabView.getHintRedDot().getPointMode() != 0) {
                if (g()) {
                    layoutParams.rightMargin = this.f7761w.f7791d0;
                } else {
                    layoutParams.leftMargin = this.f7761w.f7791d0;
                }
                cOUITabView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), i11);
                if (cOUITabView.getMeasuredWidth() > this.f7761w.f7788a0) {
                    cOUITabView.getTextView().getLayoutParams().width = ((this.f7761w.f7788a0 - cOUITabView.getHintRedDot().getMeasuredWidth()) - layoutParams.getMarginStart()) + layoutParams.getMarginEnd();
                    cOUITabView.measure(i10, i11);
                    return;
                }
                return;
            }
            layoutParams.leftMargin = 0;
            layoutParams.rightMargin = 0;
            cOUITabView.measure(i10, i11);
            return;
        }
        cOUITabView.measure(i10, i11);
    }

    private void l(int i10, int i11) {
        if (getParent() == null || !(getParent() instanceof COUITabLayout)) {
            return;
        }
        ((COUITabLayout) getParent()).c0(i10, i11);
    }

    private void m(View view, int i10, int i11) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        ViewCompat.A0(view, 0, view.getPaddingTop(), 0, view.getPaddingBottom());
        layoutParams.setMarginStart(i10);
        layoutParams.setMarginEnd(i11);
    }

    private void n(View view, int i10, int i11, int i12) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        layoutParams.width = i12 + i11 + i10;
        view.setPaddingRelative(i10, view.getPaddingTop(), i11, view.getPaddingBottom());
        view.measure(View.MeasureSpec.makeMeasureSpec(layoutParams.width, 1073741824), View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), 1073741824));
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0028  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x002f  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0033  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void b(int i10, int i11) {
        boolean z10;
        View childAt;
        COUISlidingTabStrip cOUISlidingTabStrip;
        int i12;
        int i13;
        ValueAnimator valueAnimator = this.f7755q;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            if (i10 != this.f7754p) {
                this.f7755q.end();
            } else {
                this.f7755q.cancel();
                z10 = true;
                boolean z11 = ViewCompat.x(this) == 1;
                childAt = getChildAt(i10);
                if (childAt != null) {
                    o();
                    return;
                }
                COUITabView cOUITabView = (COUITabView) childAt;
                COUITabView cOUITabView2 = (COUITabView) getChildAt(this.f7761w.getSelectedTabPosition());
                if (cOUITabView.getTextView() != null && cOUITabView.f7828i == null) {
                    TextView textView = cOUITabView.getTextView();
                    int i14 = this.f7752n;
                    int i15 = this.f7753o;
                    int indicatorPadding = this.f7761w.getIndicatorPadding();
                    int e10 = e((cOUITabView.getLeft() + textView.getLeft()) - indicatorPadding);
                    int f10 = f(cOUITabView.getLeft() + textView.getRight() + indicatorPadding);
                    int i16 = (f10 - e10) - (i15 - i14);
                    int i17 = e10 - i14;
                    int R = this.f7761w.R(i10, this.f7746h);
                    int i18 = this.f7760v;
                    if (i18 != -1) {
                        R = i18;
                    }
                    ValueAnimator valueAnimator2 = new ValueAnimator();
                    this.f7755q = valueAnimator2;
                    valueAnimator2.setDuration(R);
                    valueAnimator2.setInterpolator(new COUIEaseInterpolator());
                    valueAnimator2.setIntValues(0, 1);
                    valueAnimator2.addUpdateListener(new a(textView, new ArgbEvaluator(), z10 ? textView.getCurrentTextColor() : this.f7761w.N, cOUITabView2, z10 ? cOUITabView2.getTextView().getCurrentTextColor() : this.f7761w.O, i15, i14, i16, i17, f10, e10));
                    cOUISlidingTabStrip = this;
                    valueAnimator2.addListener(new b(i10));
                    valueAnimator2.start();
                } else {
                    cOUISlidingTabStrip = this;
                    int e11 = cOUISlidingTabStrip.e(cOUITabView.getLeft() + cOUITabView.f7828i.getLeft());
                    int f11 = cOUISlidingTabStrip.f(cOUITabView.getLeft() + cOUITabView.f7828i.getRight());
                    if (Math.abs(i10 - cOUISlidingTabStrip.f7746h) <= 1) {
                        i12 = cOUISlidingTabStrip.f7752n;
                        i13 = cOUISlidingTabStrip.f7753o;
                    } else {
                        int d10 = cOUISlidingTabStrip.d(24);
                        i12 = (i10 >= cOUISlidingTabStrip.f7746h ? !z11 : z11) ? e11 - d10 : d10 + f11;
                        i13 = i12;
                    }
                    if (i12 != e11 || i13 != f11) {
                        ValueAnimator valueAnimator3 = new ValueAnimator();
                        cOUISlidingTabStrip.f7755q = valueAnimator3;
                        valueAnimator3.setInterpolator(COUIAnimationUtils.f7835b);
                        valueAnimator3.setDuration(i11);
                        valueAnimator3.setFloatValues(0.0f, 1.0f);
                        valueAnimator3.addUpdateListener(new c(i12, e11, i13, f11));
                        valueAnimator3.addListener(new d(i10, cOUITabView, cOUITabView2));
                        valueAnimator3.start();
                    }
                }
                cOUISlidingTabStrip.f7754p = cOUISlidingTabStrip.f7761w.getSelectedTabPosition();
                return;
            }
        }
        z10 = false;
        if (ViewCompat.x(this) == 1) {
        }
        childAt = getChildAt(i10);
        if (childAt != null) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean c() {
        int childCount = getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            if (getChildAt(i10).getWidth() <= 0) {
                return true;
            }
        }
        return false;
    }

    int d(int i10) {
        return Math.round(getResources().getDisplayMetrics().density * i10);
    }

    public Paint getBottomDividerPaint() {
        return this.f7744f;
    }

    public int getIndicatorAnimTime() {
        return this.f7760v;
    }

    public int getIndicatorBackgroundHeight() {
        return this.f7756r;
    }

    public int getIndicatorBackgroundPaddingLeft() {
        return this.f7757s;
    }

    public int getIndicatorBackgroundPaddingRight() {
        return this.f7758t;
    }

    public Paint getIndicatorBackgroundPaint() {
        return this.f7745g;
    }

    public int getIndicatorLeft() {
        return this.f7752n;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getIndicatorPosition() {
        return this.f7746h + this.f7747i;
    }

    public int getIndicatorRight() {
        return this.f7753o;
    }

    public float getIndicatorWidthRatio() {
        return this.f7759u;
    }

    public Paint getSelectedIndicatorPaint() {
        return this.f7743e;
    }

    public void j(int i10, int i11) {
        int i12 = (i10 + i11) / 2;
        int max = Math.max(i11 - i10, d(32)) / 2;
        int i13 = i12 - max;
        int i14 = i12 + max;
        if (i13 == this.f7752n && i14 == this.f7753o) {
            return;
        }
        this.f7752n = i13;
        this.f7753o = i14;
        ViewCompat.b0(this.f7761w);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k(int i10, float f10) {
        ValueAnimator valueAnimator = this.f7755q;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.f7755q.cancel();
        }
        this.f7746h = i10;
        this.f7747i = f10;
        o();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void o() {
        int right;
        int i10;
        int left;
        int right2;
        int i11;
        float f10;
        int left2;
        int right3;
        int i12;
        float f11;
        View childAt = getChildAt(this.f7746h);
        COUITabView cOUITabView = (COUITabView) getChildAt(this.f7746h);
        boolean z10 = false;
        boolean z11 = (cOUITabView == null || cOUITabView.getTextView() == null || cOUITabView.f7828i != null) ? false : true;
        if (cOUITabView != null && cOUITabView.f7828i != null) {
            z10 = true;
        }
        int i13 = -1;
        if (z11) {
            TextView textView = cOUITabView.getTextView();
            if (textView.getWidth() > 0) {
                int left3 = (cOUITabView.getLeft() + textView.getLeft()) - this.f7761w.getIndicatorPadding();
                int left4 = cOUITabView.getLeft() + textView.getRight() + this.f7761w.getIndicatorPadding();
                if (this.f7747i > 0.0f && this.f7746h < getChildCount() - 1) {
                    COUITabView cOUITabView2 = (COUITabView) getChildAt(this.f7746h + 1);
                    View view = cOUITabView2.f7828i;
                    if (view == null) {
                        view = cOUITabView2.getTextView();
                    }
                    if (view != null) {
                        left2 = (cOUITabView2.getLeft() + view.getLeft()) - this.f7761w.getIndicatorPadding();
                        right3 = cOUITabView2.getLeft() + view.getRight() + this.f7761w.getIndicatorPadding();
                    } else {
                        left2 = cOUITabView2.getLeft();
                        right3 = cOUITabView2.getRight();
                    }
                    int i14 = right3 - left2;
                    int i15 = left4 - left3;
                    int i16 = i14 - i15;
                    int i17 = left2 - left3;
                    if (this.f7748j == 0.0f) {
                        this.f7748j = this.f7747i;
                    }
                    float f12 = this.f7747i;
                    if (f12 - this.f7748j > 0.0f) {
                        i12 = (int) (i15 + (i16 * f12));
                        f11 = left3 + (i17 * f12);
                    } else {
                        i12 = (int) (i14 - (i16 * (1.0f - f12)));
                        f11 = left2 - (i17 * (1.0f - f12));
                    }
                    left3 = (int) f11;
                    left4 = left3 + i12;
                    this.f7748j = f12;
                }
                i13 = e(left3);
                right = f(left4);
            }
            right = -1;
        } else if (z10) {
            View view2 = cOUITabView.f7828i;
            if (view2.getWidth() > 0) {
                int left5 = (cOUITabView.getLeft() + view2.getLeft()) - this.f7761w.getIndicatorPadding();
                int left6 = cOUITabView.getLeft() + view2.getRight() + this.f7761w.getIndicatorPadding();
                if (this.f7747i > 0.0f && this.f7746h < getChildCount() - 1) {
                    COUITabView cOUITabView3 = (COUITabView) getChildAt(this.f7746h + 1);
                    View view3 = cOUITabView3.f7828i;
                    if (view3 == null) {
                        view3 = cOUITabView3.getTextView();
                    }
                    if (view3 != null) {
                        left = (cOUITabView3.getLeft() + view3.getLeft()) - this.f7761w.getIndicatorPadding();
                        right2 = cOUITabView3.getLeft() + view3.getRight() + this.f7761w.getIndicatorPadding();
                    } else {
                        left = cOUITabView3.getLeft();
                        right2 = cOUITabView3.getRight();
                    }
                    int i18 = right2 - left;
                    int i19 = left6 - left5;
                    int i20 = i18 - i19;
                    int i21 = left - left5;
                    if (this.f7748j == 0.0f) {
                        this.f7748j = this.f7747i;
                    }
                    float f13 = this.f7747i;
                    if (f13 - this.f7748j > 0.0f) {
                        i11 = (int) (i19 + (i20 * f13));
                        f10 = left5 + (i21 * f13);
                    } else {
                        i11 = (int) (i18 - (i20 * (1.0f - f13)));
                        f10 = left - (i21 * (1.0f - f13));
                    }
                    left5 = (int) f10;
                    left6 = left5 + i11;
                    this.f7748j = f13;
                }
                int e10 = e(left5);
                i10 = f(left6);
                i13 = e10;
            } else {
                i10 = -1;
            }
            right = i10;
        } else {
            if (childAt != null && childAt.getWidth() > 0) {
                i13 = childAt.getLeft();
                right = childAt.getRight();
                if (this.f7747i > 0.0f && this.f7746h < getChildCount() - 1) {
                    View childAt2 = getChildAt(this.f7746h + 1);
                    float left7 = this.f7747i * childAt2.getLeft();
                    float f14 = this.f7747i;
                    i13 = (int) (left7 + ((1.0f - f14) * i13));
                    right = (int) ((f14 * childAt2.getRight()) + ((1.0f - this.f7747i) * right));
                }
            }
            right = -1;
        }
        j(i13, right);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        super.onLayout(z10, i10, i11, i12, i13);
        if (this.f7761w.T()) {
            o();
        }
        if (this.f7761w.f7789b0) {
            return;
        }
        ValueAnimator valueAnimator = this.f7755q;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.f7755q.cancel();
            b(this.f7746h, Math.round((1.0f - this.f7755q.getAnimatedFraction()) * ((float) this.f7755q.getDuration())));
        }
        COUITabLayout cOUITabLayout = this.f7761w;
        cOUITabLayout.f7789b0 = true;
        cOUITabLayout.f0(this.f7746h, 0.0f, true, true);
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        if (View.MeasureSpec.getMode(i10) == 0) {
            return;
        }
        int size = View.MeasureSpec.getSize(i10);
        int childCount = getChildCount();
        if (childCount == 0) {
            super.onMeasure(i10, i11);
            return;
        }
        if (this.f7761w.getTabMode() == 1) {
            this.f7759u = this.f7761w.getDefaultIndicatoRatio();
            int tabMinDivider = (size - ((childCount - 1) * this.f7761w.getTabMinDivider())) - (this.f7761w.getTabMinMargin() * 2);
            int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(this.f7761w.f7788a0, Integer.MIN_VALUE);
            int i12 = 0;
            for (int i13 = 0; i13 < childCount; i13++) {
                COUITabView cOUITabView = (COUITabView) getChildAt(i13);
                m(cOUITabView, 0, 0);
                i(cOUITabView, makeMeasureSpec, i11);
                i12 += cOUITabView.getMeasuredWidth();
            }
            if (i12 <= tabMinDivider) {
                h(size, i12);
            } else {
                int tabMinDivider2 = this.f7761w.getTabMinDivider() / 2;
                l(this.f7761w.getTabMinMargin() - tabMinDivider2, this.f7761w.getTabMinMargin() - tabMinDivider2);
                for (int i14 = 0; i14 < childCount; i14++) {
                    View childAt = getChildAt(i14);
                    n(childAt, tabMinDivider2, tabMinDivider2, childAt.getMeasuredWidth());
                }
            }
        } else {
            int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(this.f7761w.f7788a0, Integer.MIN_VALUE);
            int tabMinDivider3 = this.f7761w.getTabMinDivider() / 2;
            l(this.f7761w.getTabMinMargin() - tabMinDivider3, this.f7761w.getTabMinMargin() - tabMinDivider3);
            for (int i15 = 0; i15 < childCount; i15++) {
                View childAt2 = getChildAt(i15);
                m(childAt2, 0, 0);
                i((COUITabView) childAt2, makeMeasureSpec2, i11);
                n(childAt2, tabMinDivider3, tabMinDivider3, childAt2.getMeasuredWidth());
            }
        }
        int i16 = 0;
        for (int i17 = 0; i17 < childCount; i17++) {
            i16 += getChildAt(i17).getMeasuredWidth();
        }
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(i16, 1073741824), i11);
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onRtlPropertiesChanged(int i10) {
        super.onRtlPropertiesChanged(i10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBottomDividerColor(int i10) {
        this.f7744f.setColor(i10);
        ViewCompat.b0(this.f7761w);
    }

    public void setIndicatorAnimTime(int i10) {
        this.f7760v = i10;
    }

    public void setIndicatorBackgroundHeight(int i10) {
        this.f7756r = i10;
    }

    public void setIndicatorBackgroundPaddingLeft(int i10) {
        this.f7757s = i10;
    }

    public void setIndicatorBackgroundPaddingRight(int i10) {
        this.f7758t = i10;
    }

    public void setIndicatorLeft(int i10) {
        this.f7752n = i10;
    }

    public void setIndicatorRight(int i10) {
        this.f7753o = i10;
    }

    public void setIndicatorWidthRatio(float f10) {
        this.f7759u = f10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSelectedIndicatorColor(int i10) {
        this.f7743e.setColor(i10);
        ViewCompat.b0(this.f7761w);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSelectedIndicatorHeight(int i10) {
        if (this.f7750l != i10) {
            this.f7750l = i10;
            ViewCompat.b0(this.f7761w);
        }
    }
}
