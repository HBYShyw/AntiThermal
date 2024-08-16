package com.coui.appcompat.bottomnavigation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Property;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.navigation.NavigationBarMenuView;
import com.google.android.material.navigation.NavigationBarView;
import com.support.appcompat.R$attr;
import com.support.bars.R$color;
import com.support.bars.R$dimen;
import com.support.bars.R$drawable;
import com.support.bars.R$style;
import com.support.bars.R$styleable;
import m1.COUIInEaseInterpolator;
import m1.COUILinearInterpolator;
import m1.COUIOutEaseInterpolator;
import v1.COUIContextUtil;
import w1.COUIDarkModeUtil;
import z2.COUIChangeTextUtil;

/* loaded from: classes.dex */
public class COUINavigationView extends BottomNavigationView {
    private int A;
    private int B;
    private boolean C;
    private boolean D;

    /* renamed from: l, reason: collision with root package name */
    private Animator f5466l;

    /* renamed from: m, reason: collision with root package name */
    private Animator f5467m;

    /* renamed from: n, reason: collision with root package name */
    private ValueAnimator f5468n;

    /* renamed from: o, reason: collision with root package name */
    private ValueAnimator f5469o;

    /* renamed from: p, reason: collision with root package name */
    private ValueAnimator f5470p;

    /* renamed from: q, reason: collision with root package name */
    private int f5471q;

    /* renamed from: r, reason: collision with root package name */
    private l f5472r;

    /* renamed from: s, reason: collision with root package name */
    private m f5473s;

    /* renamed from: t, reason: collision with root package name */
    private k f5474t;

    /* renamed from: u, reason: collision with root package name */
    private j f5475u;

    /* renamed from: v, reason: collision with root package name */
    private COUINavigationMenuView f5476v;

    /* renamed from: w, reason: collision with root package name */
    private FrameLayout f5477w;

    /* renamed from: x, reason: collision with root package name */
    private int f5478x;

    /* renamed from: y, reason: collision with root package name */
    private int f5479y;

    /* renamed from: z, reason: collision with root package name */
    private View f5480z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements ViewUtils.OnApplyWindowInsetsListener {
        a() {
        }

        @Override // com.google.android.material.internal.ViewUtils.OnApplyWindowInsetsListener
        public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat, ViewUtils.RelativePadding relativePadding) {
            boolean z10 = ViewCompat.x(view) == 1;
            int i10 = windowInsetsCompat.f(WindowInsetsCompat.l.c()).f2185a;
            int i11 = windowInsetsCompat.f(WindowInsetsCompat.l.c()).f2187c;
            relativePadding.start += z10 ? i11 : i10;
            int i12 = relativePadding.end;
            if (!z10) {
                i10 = i11;
            }
            relativePadding.end = i12 + i10;
            relativePadding.applyToView(view);
            return windowInsetsCompat;
        }
    }

    /* loaded from: classes.dex */
    class b implements NavigationBarView.c {
        b() {
        }

        @Override // com.google.android.material.navigation.NavigationBarView.c
        public boolean a(MenuItem menuItem) {
            COUINavigationView cOUINavigationView = COUINavigationView.this;
            cOUINavigationView.D = cOUINavigationView.f5476v.getEnlargeId() == menuItem.getItemId();
            COUINavigationView.this.f5474t.a(COUINavigationView.this.D, menuItem);
            COUINavigationView.this.u();
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements Animator.AnimatorListener {
        c() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (COUINavigationView.this.f5472r != null) {
                COUINavigationView.this.f5472r.b();
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (COUINavigationView.this.f5479y != 0) {
                COUINavigationView cOUINavigationView = COUINavigationView.this;
                cOUINavigationView.e(cOUINavigationView.f5479y);
                COUINavigationView.this.f5479y = 0;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d implements Animator.AnimatorListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ AnimatorSet f5484a;

        d(AnimatorSet animatorSet) {
            this.f5484a = animatorSet;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (COUINavigationView.this.f5479y != 0) {
                COUINavigationView.this.f5476v.setTranslationY(-COUINavigationView.this.getHeight());
                this.f5484a.start();
            }
            if (COUINavigationView.this.f5472r != null) {
                COUINavigationView.this.f5472r.a();
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class e implements ValueAnimator.AnimatorUpdateListener {
        e() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUINavigationView.this.f5476v.setTranslationY(((Float) valueAnimator.getAnimatedValue()).floatValue() * COUINavigationView.this.getMeasuredHeight());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class f implements Animator.AnimatorListener {
        f() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (COUINavigationView.this.f5473s != null) {
                COUINavigationView.this.f5473s.a();
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (COUINavigationView.this.f5473s != null) {
                COUINavigationView.this.f5473s.f();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class g implements ValueAnimator.AnimatorUpdateListener {
        g() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) COUINavigationView.this.getLayoutParams();
            marginLayoutParams.bottomMargin = (int) (((Float) valueAnimator.getAnimatedValue()).floatValue() * COUINavigationView.this.getMeasuredHeight() * (-1.0f));
            COUINavigationView.this.setLayoutParams(marginLayoutParams);
            if (COUINavigationView.this.f5473s != null) {
                COUINavigationView.this.f5473s.d(marginLayoutParams.bottomMargin);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class h implements Animator.AnimatorListener {
        h() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (COUINavigationView.this.f5473s != null) {
                COUINavigationView.this.f5473s.e();
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (COUINavigationView.this.f5473s != null) {
                COUINavigationView.this.f5473s.b();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class i implements ValueAnimator.AnimatorUpdateListener {
        i() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) COUINavigationView.this.getLayoutParams();
            marginLayoutParams.bottomMargin = (int) (((Float) valueAnimator.getAnimatedValue()).floatValue() * COUINavigationView.this.getMeasuredHeight() * (-1.0f));
            COUINavigationView.this.setLayoutParams(marginLayoutParams);
            if (COUINavigationView.this.f5473s != null) {
                COUINavigationView.this.f5473s.c(marginLayoutParams.bottomMargin);
            }
        }
    }

    /* loaded from: classes.dex */
    public interface j {
        void a(Configuration configuration);
    }

    /* loaded from: classes.dex */
    public interface k {
        void a(boolean z10, MenuItem menuItem);
    }

    /* loaded from: classes.dex */
    public interface l {
        void a();

        void b();
    }

    /* loaded from: classes.dex */
    public interface m {
        void a();

        void b();

        void c(int i10);

        void d(int i10);

        void e();

        void f();
    }

    public COUINavigationView(Context context) {
        this(context, null);
    }

    private void f(Context context) {
        View view = new View(context);
        this.f5480z = view;
        COUIDarkModeUtil.b(view, false);
        this.f5480z.setBackgroundColor(COUIContextUtil.a(context, R$attr.couiColorDivider));
        this.f5480z.setLayoutParams(new FrameLayout.LayoutParams(-1, getResources().getDimensionPixelSize(R$dimen.coui_navigation_shadow_height)));
        if (this.C) {
            addView(this.f5480z, 0);
        } else {
            addView(this.f5480z);
            this.f5476v.setTop(0);
        }
    }

    @SuppressLint({"RestrictedApi"})
    private void g() {
        ViewUtils.doOnApplyWindowInsets(this, new a());
    }

    private void s() {
        this.f5477w = new FrameLayout(getContext());
        this.f5477w.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        addView(this.f5477w, 0);
        ViewCompat.p0(this.f5477w, new ColorDrawable(ContextCompat.c(getContext(), R$color.coui_navigation_enlarge_default_bg)));
    }

    private void t(Context context) {
        int dimensionPixelOffset = context.getResources().getDimensionPixelOffset(R$dimen.coui_navigation_item_text_size);
        if (this.A != 0) {
            dimensionPixelOffset = getContext().getResources().getDimensionPixelOffset(this.A);
        } else if (this.f5478x == 1) {
            dimensionPixelOffset = context.getResources().getDimensionPixelOffset(R$dimen.coui_navigation_item_small_text_size);
        }
        this.f5476v.setTextSize(dimensionPixelOffset);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"RestrictedApi"})
    public void u() {
        if (this.B == 0) {
            return;
        }
        if (this.D) {
            w();
            this.f5477w.setBackgroundResource(this.B);
        } else {
            this.f5476v.p();
            this.f5476v.setItemTextColor(getItemTextColor());
            this.f5477w.setBackgroundColor(getResources().getColor(R$color.coui_navigation_enlarge_default_bg));
        }
    }

    private void v() {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.f5476v, (Property<COUINavigationMenuView, Float>) View.ALPHA, 0.0f, 1.0f);
        this.f5466l = ofFloat;
        ofFloat.setInterpolator(new COUIInEaseInterpolator());
        this.f5466l.setDuration(100L);
        this.f5466l.addListener(new c());
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.f5476v, (Property<COUINavigationMenuView, Float>) View.ALPHA, 1.0f, 0.0f);
        this.f5467m = ofFloat2;
        ofFloat2.setInterpolator(new COUILinearInterpolator());
        this.f5467m.setDuration(100L);
        this.f5467m.addListener(new d(animatorSet));
        ValueAnimator ofFloat3 = ValueAnimator.ofFloat(1.0f, 0.0f);
        this.f5468n = ofFloat3;
        ofFloat3.setInterpolator(new COUIInEaseInterpolator());
        this.f5468n.setDuration(350L);
        this.f5468n.addUpdateListener(new e());
        animatorSet.playTogether(this.f5466l, this.f5468n);
        ValueAnimator ofFloat4 = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.f5470p = ofFloat4;
        ofFloat4.setInterpolator(new COUIOutEaseInterpolator());
        this.f5470p.setDuration(200L);
        this.f5470p.addListener(new f());
        this.f5470p.addUpdateListener(new g());
        ValueAnimator ofFloat5 = ValueAnimator.ofFloat(1.0f, 0.0f);
        this.f5469o = ofFloat5;
        ofFloat5.setInterpolator(new COUIInEaseInterpolator());
        this.f5469o.setDuration(250L);
        this.f5469o.addListener(new h());
        this.f5469o.addUpdateListener(new i());
    }

    private void w() {
        this.f5476v.t();
    }

    private void z(COUINavigationItemView cOUINavigationItemView, String str, int i10) {
        if (cOUINavigationItemView != null) {
            if (i10 == 1) {
                cOUINavigationItemView.getCOUIHintRedDot().setVisibility(0);
                cOUINavigationItemView.getCOUIHintRedDot().setPointMode(1);
            } else {
                if (i10 != 2) {
                    cOUINavigationItemView.getCOUIHintRedDot().setVisibility(4);
                    return;
                }
                cOUINavigationItemView.getCOUIHintRedDot().setVisibility(0);
                cOUINavigationItemView.getCOUIHintRedDot().setPointMode(2);
                cOUINavigationItemView.getCOUIHintRedDot().setPointText(str);
            }
        }
    }

    @Override // com.google.android.material.bottomnavigation.BottomNavigationView, com.google.android.material.navigation.NavigationBarView
    protected NavigationBarMenuView d(Context context) {
        return new COUINavigationMenuView(new ContextThemeWrapper(context, R$style.COUINavigationView_NoAnimation));
    }

    @Override // com.google.android.material.navigation.NavigationBarView
    public void e(int i10) {
        if (getMenu().size() > 0) {
            getMenu().clear();
        }
        super.e(i10);
    }

    public COUINavigationMenuView getCOUINavigationMenuView() {
        return this.f5476v;
    }

    public View getDividerView() {
        return this.f5480z;
    }

    public FrameLayout getEnlargeBgView() {
        return this.f5477w;
    }

    @Override // com.google.android.material.bottomnavigation.BottomNavigationView, com.google.android.material.navigation.NavigationBarView
    public int getMaxItemCount() {
        return 10;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.material.navigation.NavigationBarView, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.C) {
            s();
        }
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        j jVar = this.f5475u;
        if (jVar != null) {
            jVar.a(configuration);
        }
        t(getContext().createConfigurationContext(configuration));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.material.bottomnavigation.BottomNavigationView, android.widget.FrameLayout, android.view.View
    public void onMeasure(int i10, int i11) {
        int mode = View.MeasureSpec.getMode(i11);
        int size = View.MeasureSpec.getSize(i11);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.coui_tool_navigation_item_height);
        if (mode == Integer.MIN_VALUE) {
            i11 = View.MeasureSpec.makeMeasureSpec(dimensionPixelSize, 1073741824);
        } else if (mode == 1073741824) {
            i11 = View.MeasureSpec.makeMeasureSpec(size, 1073741824);
        }
        super.onMeasure(i10, i11);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        return true;
    }

    public void setAnimationType(int i10) {
        if (i10 == 1) {
            this.f5466l.start();
        } else if (i10 == 2) {
            this.f5467m.start();
        }
    }

    public void setEnlargeIndex(int i10) {
        this.f5476v.s(this.C, i10);
    }

    public void setItemLayoutType(int i10) {
        this.f5478x = i10;
        t(getContext());
        this.f5476v.setItemLayoutType(this.f5478x);
    }

    @Deprecated
    public void setNeedTextAnim(boolean z10) {
    }

    public void setOnAnimatorListener(l lVar) {
        this.f5472r = lVar;
    }

    public void setOnAnimatorShowHideListener(m mVar) {
        this.f5473s = mVar;
    }

    public void setOnConfigChangedListener(j jVar) {
        this.f5475u = jVar;
    }

    public void setOnEnlargeSelectListener(k kVar) {
        this.f5474t = kVar;
        setOnItemSelectedListener(new b());
    }

    @SuppressLint({"RestrictedApi"})
    public void x(int i10, int i11, int i12) {
        if (i10 >= this.f5476v.getVisibleItems().size()) {
            return;
        }
        y(i10, String.valueOf(i11), i12);
    }

    public void y(int i10, String str, int i11) {
        if (i10 >= this.f5476v.getVisibleItems().size()) {
            return;
        }
        z((COUINavigationItemView) this.f5476v.g(getCOUINavigationMenuView().q(i10).getItemId()), str, i11);
    }

    public COUINavigationView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, com.support.bars.R$attr.couiNavigationViewStyle);
    }

    public COUINavigationView(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, R$style.Widget_COUI_COUINavigationView);
    }

    @SuppressLint({"RestrictedApi"})
    public COUINavigationView(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f5479y = 0;
        this.A = 0;
        this.C = false;
        this.D = false;
        TintTypedArray w10 = TintTypedArray.w(context, attributeSet, R$styleable.COUINavigationMenuView, i10, i11);
        this.f5476v = (COUINavigationMenuView) getMenuView();
        int i12 = R$styleable.COUINavigationMenuView_couiNaviTextColor;
        if (w10.s(i12)) {
            setItemTextColor(w10.c(i12));
        } else {
            setItemTextColor(getResources().getColorStateList(R$color.coui_bottom_tool_navigation_item_selector));
        }
        this.f5476v.setIconTintList(w10.c(R$styleable.COUINavigationMenuView_couiNaviIconTint));
        int k10 = w10.k(R$styleable.COUINavigationMenuView_navigationType, 0);
        this.f5471q = k10;
        int n10 = w10.n(R$styleable.COUINavigationMenuView_couiNaviBackground, k10 == 0 ? R$drawable.coui_bottom_tool_navigation_item_bg : 0);
        if (this.f5471q == 0) {
            this.f5476v.setItemBackgroundRes(n10);
        }
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.coui_navigation_item_text_size);
        int i13 = R$styleable.COUINavigationMenuView_couiNaviTextSize;
        int f10 = w10.f(i13, dimensionPixelSize);
        this.A = w10.n(i13, 0);
        this.f5476v.setTextSize((int) COUIChangeTextUtil.e(f10, getResources().getConfiguration().fontScale, 2));
        int l10 = w10.l(R$styleable.COUINavigationMenuView_couiNaviTipsType, -1);
        int l11 = w10.l(R$styleable.COUINavigationMenuView_couiNaviTipsNumber, 0);
        int i14 = R$styleable.COUINavigationMenuView_couiNaviMenu;
        if (w10.s(i14)) {
            e(w10.n(i14, 0));
            x(0, l11, l10);
        }
        int n11 = w10.n(R$styleable.COUINavigationMenuView_couiToolNavigationViewBg, 0);
        int n12 = w10.n(R$styleable.COUINavigationMenuView_couiTabNavigationViewBg, 0);
        this.B = w10.n(R$styleable.COUINavigationMenuView_couiEnlargeNavigationViewBg, 0);
        int i15 = this.f5471q;
        if (i15 == 2) {
            this.C = true;
            setBackgroundColor(0);
            this.f5476v.r();
        } else if (i15 == 0) {
            setBackgroundResource(n11);
        } else {
            setBackgroundResource(n12);
        }
        int i16 = R$styleable.COUINavigationMenuView_couiItemLayoutType;
        if (w10.s(i16)) {
            setItemLayoutType(w10.l(i16, 0));
        }
        setLabelVisibilityMode(1);
        setClipChildren(false);
        setClipToPadding(false);
        f(context);
        setElevation(0.0f);
        w10.x();
        v();
        g();
        COUIDarkModeUtil.b(this, false);
    }
}
