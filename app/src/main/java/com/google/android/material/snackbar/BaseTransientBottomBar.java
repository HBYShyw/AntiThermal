package com.google.android.material.snackbar;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.R$attr;
import com.google.android.material.R$dimen;
import com.google.android.material.R$styleable;
import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.SnackbarManager;
import d4.MaterialThemeOverlay;
import java.util.List;
import p3.AnimationUtils;
import r3.MaterialColors;
import z3.MaterialResources;

/* loaded from: classes.dex */
public abstract class BaseTransientBottomBar<B extends BaseTransientBottomBar<B>> {

    /* renamed from: a, reason: collision with root package name */
    private final ViewGroup f9198a;

    /* renamed from: b, reason: collision with root package name */
    protected final SnackbarBaseLayout f9199b;

    /* renamed from: c, reason: collision with root package name */
    private final ContentViewCallback f9200c;

    /* renamed from: d, reason: collision with root package name */
    private boolean f9201d;

    /* renamed from: e, reason: collision with root package name */
    private final Runnable f9202e;

    /* renamed from: f, reason: collision with root package name */
    private int f9203f;

    /* renamed from: g, reason: collision with root package name */
    private int f9204g;

    /* renamed from: h, reason: collision with root package name */
    private int f9205h;

    /* renamed from: i, reason: collision with root package name */
    private int f9206i;

    /* renamed from: j, reason: collision with root package name */
    private int f9207j;

    /* renamed from: k, reason: collision with root package name */
    private boolean f9208k;

    /* renamed from: l, reason: collision with root package name */
    private List<m<B>> f9209l;

    /* renamed from: m, reason: collision with root package name */
    private Behavior f9210m;

    /* renamed from: n, reason: collision with root package name */
    private final AccessibilityManager f9211n;

    /* renamed from: o, reason: collision with root package name */
    SnackbarManager.b f9212o;

    /* renamed from: q, reason: collision with root package name */
    private static final boolean f9195q = false;

    /* renamed from: r, reason: collision with root package name */
    private static final int[] f9196r = {R$attr.snackbarStyle};

    /* renamed from: s, reason: collision with root package name */
    private static final String f9197s = BaseTransientBottomBar.class.getSimpleName();

    /* renamed from: p, reason: collision with root package name */
    static final Handler f9194p = new Handler(Looper.getMainLooper(), new h());

    /* loaded from: classes.dex */
    public static class Behavior extends SwipeDismissBehavior<View> {

        /* renamed from: k, reason: collision with root package name */
        private final n f9213k = new n(this);

        /* JADX INFO: Access modifiers changed from: private */
        public void o(BaseTransientBottomBar<?> baseTransientBottomBar) {
            this.f9213k.c(baseTransientBottomBar);
        }

        @Override // com.google.android.material.behavior.SwipeDismissBehavior
        public boolean d(View view) {
            return this.f9213k.a(view);
        }

        @Override // com.google.android.material.behavior.SwipeDismissBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, View view, MotionEvent motionEvent) {
            this.f9213k.b(coordinatorLayout, view, motionEvent);
            return super.onInterceptTouchEvent(coordinatorLayout, view, motionEvent);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static class SnackbarBaseLayout extends FrameLayout {

        /* renamed from: o, reason: collision with root package name */
        private static final View.OnTouchListener f9214o = new a();

        /* renamed from: e, reason: collision with root package name */
        private BaseTransientBottomBar<?> f9215e;

        /* renamed from: f, reason: collision with root package name */
        private int f9216f;

        /* renamed from: g, reason: collision with root package name */
        private final float f9217g;

        /* renamed from: h, reason: collision with root package name */
        private final float f9218h;

        /* renamed from: i, reason: collision with root package name */
        private final int f9219i;

        /* renamed from: j, reason: collision with root package name */
        private final int f9220j;

        /* renamed from: k, reason: collision with root package name */
        private ColorStateList f9221k;

        /* renamed from: l, reason: collision with root package name */
        private PorterDuff.Mode f9222l;

        /* renamed from: m, reason: collision with root package name */
        private Rect f9223m;

        /* renamed from: n, reason: collision with root package name */
        private boolean f9224n;

        /* loaded from: classes.dex */
        class a implements View.OnTouchListener {
            a() {
            }

            @Override // android.view.View.OnTouchListener
            @SuppressLint({"ClickableViewAccessibility"})
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public SnackbarBaseLayout(Context context) {
            this(context, null);
        }

        private Drawable c() {
            float dimension = getResources().getDimension(R$dimen.mtrl_snackbar_background_corner_radius);
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setShape(0);
            gradientDrawable.setCornerRadius(dimension);
            gradientDrawable.setColor(MaterialColors.i(this, R$attr.colorSurface, R$attr.colorOnSurface, getBackgroundOverlayColorAlpha()));
            if (this.f9221k != null) {
                Drawable l10 = DrawableCompat.l(gradientDrawable);
                DrawableCompat.i(l10, this.f9221k);
                return l10;
            }
            return DrawableCompat.l(gradientDrawable);
        }

        private void d(ViewGroup.MarginLayoutParams marginLayoutParams) {
            this.f9223m = new Rect(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, marginLayoutParams.rightMargin, marginLayoutParams.bottomMargin);
        }

        private void setBaseTransientBottomBar(BaseTransientBottomBar<?> baseTransientBottomBar) {
            this.f9215e = baseTransientBottomBar;
        }

        void b(ViewGroup viewGroup) {
            this.f9224n = true;
            viewGroup.addView(this);
            this.f9224n = false;
        }

        float getActionTextColorAlpha() {
            return this.f9218h;
        }

        int getAnimationMode() {
            return this.f9216f;
        }

        float getBackgroundOverlayColorAlpha() {
            return this.f9217g;
        }

        int getMaxInlineActionWidth() {
            return this.f9220j;
        }

        int getMaxWidth() {
            return this.f9219i;
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            BaseTransientBottomBar<?> baseTransientBottomBar = this.f9215e;
            if (baseTransientBottomBar != null) {
                baseTransientBottomBar.r();
            }
            ViewCompat.h0(this);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            BaseTransientBottomBar<?> baseTransientBottomBar = this.f9215e;
            if (baseTransientBottomBar != null) {
                baseTransientBottomBar.s();
            }
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
            super.onLayout(z10, i10, i11, i12, i13);
            BaseTransientBottomBar<?> baseTransientBottomBar = this.f9215e;
            if (baseTransientBottomBar != null) {
                baseTransientBottomBar.t();
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.widget.FrameLayout, android.view.View
        public void onMeasure(int i10, int i11) {
            super.onMeasure(i10, i11);
            if (this.f9219i > 0) {
                int measuredWidth = getMeasuredWidth();
                int i12 = this.f9219i;
                if (measuredWidth > i12) {
                    super.onMeasure(View.MeasureSpec.makeMeasureSpec(i12, 1073741824), i11);
                }
            }
        }

        void setAnimationMode(int i10) {
            this.f9216f = i10;
        }

        @Override // android.view.View
        public void setBackground(Drawable drawable) {
            setBackgroundDrawable(drawable);
        }

        @Override // android.view.View
        public void setBackgroundDrawable(Drawable drawable) {
            if (drawable != null && this.f9221k != null) {
                drawable = DrawableCompat.l(drawable.mutate());
                DrawableCompat.i(drawable, this.f9221k);
                DrawableCompat.j(drawable, this.f9222l);
            }
            super.setBackgroundDrawable(drawable);
        }

        @Override // android.view.View
        public void setBackgroundTintList(ColorStateList colorStateList) {
            this.f9221k = colorStateList;
            if (getBackground() != null) {
                Drawable l10 = DrawableCompat.l(getBackground().mutate());
                DrawableCompat.i(l10, colorStateList);
                DrawableCompat.j(l10, this.f9222l);
                if (l10 != getBackground()) {
                    super.setBackgroundDrawable(l10);
                }
            }
        }

        @Override // android.view.View
        public void setBackgroundTintMode(PorterDuff.Mode mode) {
            this.f9222l = mode;
            if (getBackground() != null) {
                Drawable l10 = DrawableCompat.l(getBackground().mutate());
                DrawableCompat.j(l10, mode);
                if (l10 != getBackground()) {
                    super.setBackgroundDrawable(l10);
                }
            }
        }

        @Override // android.view.View
        public void setLayoutParams(ViewGroup.LayoutParams layoutParams) {
            super.setLayoutParams(layoutParams);
            if (this.f9224n || !(layoutParams instanceof ViewGroup.MarginLayoutParams)) {
                return;
            }
            d((ViewGroup.MarginLayoutParams) layoutParams);
            BaseTransientBottomBar<?> baseTransientBottomBar = this.f9215e;
            if (baseTransientBottomBar != null) {
                baseTransientBottomBar.G();
            }
        }

        @Override // android.view.View
        public void setOnClickListener(View.OnClickListener onClickListener) {
            setOnTouchListener(onClickListener != null ? null : f9214o);
            super.setOnClickListener(onClickListener);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public SnackbarBaseLayout(Context context, AttributeSet attributeSet) {
            super(MaterialThemeOverlay.c(context, attributeSet, 0, 0), attributeSet);
            Context context2 = getContext();
            TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(attributeSet, R$styleable.SnackbarLayout);
            if (obtainStyledAttributes.hasValue(R$styleable.SnackbarLayout_elevation)) {
                ViewCompat.t0(this, obtainStyledAttributes.getDimensionPixelSize(r1, 0));
            }
            this.f9216f = obtainStyledAttributes.getInt(R$styleable.SnackbarLayout_animationMode, 0);
            this.f9217g = obtainStyledAttributes.getFloat(R$styleable.SnackbarLayout_backgroundOverlayColorAlpha, 1.0f);
            setBackgroundTintList(MaterialResources.a(context2, obtainStyledAttributes, R$styleable.SnackbarLayout_backgroundTint));
            setBackgroundTintMode(ViewUtils.parseTintMode(obtainStyledAttributes.getInt(R$styleable.SnackbarLayout_backgroundTintMode, -1), PorterDuff.Mode.SRC_IN));
            this.f9218h = obtainStyledAttributes.getFloat(R$styleable.SnackbarLayout_actionTextColorAlpha, 1.0f);
            this.f9219i = obtainStyledAttributes.getDimensionPixelSize(R$styleable.SnackbarLayout_android_maxWidth, -1);
            this.f9220j = obtainStyledAttributes.getDimensionPixelSize(R$styleable.SnackbarLayout_maxActionInlineWidth, -1);
            obtainStyledAttributes.recycle();
            setOnTouchListener(f9214o);
            setFocusable(true);
            if (getBackground() == null) {
                ViewCompat.p0(this, c());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f9225a;

        a(int i10) {
            this.f9225a = i10;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            BaseTransientBottomBar.this.u(this.f9225a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements ValueAnimator.AnimatorUpdateListener {
        b() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            BaseTransientBottomBar.this.f9199b.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements ValueAnimator.AnimatorUpdateListener {
        c() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            BaseTransientBottomBar.this.f9199b.setScaleX(floatValue);
            BaseTransientBottomBar.this.f9199b.setScaleY(floatValue);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d extends AnimatorListenerAdapter {
        d() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            BaseTransientBottomBar.this.v();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            BaseTransientBottomBar.this.f9200c.a(70, 180);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class e implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a, reason: collision with root package name */
        private int f9230a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ int f9231b;

        e(int i10) {
            this.f9231b = i10;
            this.f9230a = i10;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
            if (BaseTransientBottomBar.f9195q) {
                ViewCompat.W(BaseTransientBottomBar.this.f9199b, intValue - this.f9230a);
            } else {
                BaseTransientBottomBar.this.f9199b.setTranslationY(intValue);
            }
            this.f9230a = intValue;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class f extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f9233a;

        f(int i10) {
            this.f9233a = i10;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            BaseTransientBottomBar.this.u(this.f9233a);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            BaseTransientBottomBar.this.f9200c.b(0, 180);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class g implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a, reason: collision with root package name */
        private int f9235a = 0;

        g() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
            if (BaseTransientBottomBar.f9195q) {
                ViewCompat.W(BaseTransientBottomBar.this.f9199b, intValue - this.f9235a);
            } else {
                BaseTransientBottomBar.this.f9199b.setTranslationY(intValue);
            }
            this.f9235a = intValue;
        }
    }

    /* loaded from: classes.dex */
    class h implements Handler.Callback {
        h() {
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            int i10 = message.what;
            if (i10 == 0) {
                ((BaseTransientBottomBar) message.obj).A();
                return true;
            }
            if (i10 != 1) {
                return false;
            }
            ((BaseTransientBottomBar) message.obj).o(message.arg1);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class i implements Runnable {
        i() {
        }

        @Override // java.lang.Runnable
        public void run() {
            BaseTransientBottomBar.this.u(3);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class j implements SwipeDismissBehavior.c {
        j() {
        }

        @Override // com.google.android.material.behavior.SwipeDismissBehavior.c
        public void a(View view) {
            if (view.getParent() != null) {
                view.setVisibility(8);
            }
            BaseTransientBottomBar.this.i(0);
        }

        @Override // com.google.android.material.behavior.SwipeDismissBehavior.c
        public void b(int i10) {
            if (i10 == 0) {
                SnackbarManager.c().k(BaseTransientBottomBar.this.f9212o);
            } else if (i10 == 1 || i10 == 2) {
                SnackbarManager.c().j(BaseTransientBottomBar.this.f9212o);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class k implements Runnable {
        k() {
        }

        @Override // java.lang.Runnable
        public void run() {
            SnackbarBaseLayout snackbarBaseLayout = BaseTransientBottomBar.this.f9199b;
            if (snackbarBaseLayout == null) {
                return;
            }
            if (snackbarBaseLayout.getParent() != null) {
                BaseTransientBottomBar.this.f9199b.setVisibility(0);
            }
            if (BaseTransientBottomBar.this.f9199b.getAnimationMode() == 1) {
                BaseTransientBottomBar.this.C();
            } else {
                BaseTransientBottomBar.this.E();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class l extends AnimatorListenerAdapter {
        l() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            BaseTransientBottomBar.this.v();
        }
    }

    /* loaded from: classes.dex */
    public static abstract class m<B> {
        public void a(B b10, int i10) {
        }

        public void b(B b10) {
        }
    }

    /* loaded from: classes.dex */
    public static class n {

        /* renamed from: a, reason: collision with root package name */
        private SnackbarManager.b f9241a;

        public n(SwipeDismissBehavior<?> swipeDismissBehavior) {
            swipeDismissBehavior.k(0.1f);
            swipeDismissBehavior.i(0.6f);
            swipeDismissBehavior.l(0);
        }

        public boolean a(View view) {
            return view instanceof SnackbarBaseLayout;
        }

        public void b(CoordinatorLayout coordinatorLayout, View view, MotionEvent motionEvent) {
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 0) {
                if (coordinatorLayout.F(view, (int) motionEvent.getX(), (int) motionEvent.getY())) {
                    SnackbarManager.c().j(this.f9241a);
                }
            } else if (actionMasked == 1 || actionMasked == 3) {
                SnackbarManager.c().k(this.f9241a);
            }
        }

        public void c(BaseTransientBottomBar<?> baseTransientBottomBar) {
            this.f9241a = baseTransientBottomBar.f9212o;
        }
    }

    private void B() {
        if (y()) {
            f();
            return;
        }
        if (this.f9199b.getParent() != null) {
            this.f9199b.setVisibility(0);
        }
        v();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void C() {
        ValueAnimator j10 = j(0.0f, 1.0f);
        ValueAnimator m10 = m(0.8f, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(j10, m10);
        animatorSet.setDuration(150L);
        animatorSet.addListener(new l());
        animatorSet.start();
    }

    private void D(int i10) {
        ValueAnimator j10 = j(1.0f, 0.0f);
        j10.setDuration(75L);
        j10.addListener(new a(i10));
        j10.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void E() {
        int n10 = n();
        if (f9195q) {
            ViewCompat.W(this.f9199b, n10);
        } else {
            this.f9199b.setTranslationY(n10);
        }
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(n10, 0);
        valueAnimator.setInterpolator(AnimationUtils.f16556b);
        valueAnimator.setDuration(250L);
        valueAnimator.addListener(new d());
        valueAnimator.addUpdateListener(new e(n10));
        valueAnimator.start();
    }

    private void F(int i10) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(0, n());
        valueAnimator.setInterpolator(AnimationUtils.f16556b);
        valueAnimator.setDuration(250L);
        valueAnimator.addListener(new f(i10));
        valueAnimator.addUpdateListener(new g());
        valueAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void G() {
        ViewGroup.LayoutParams layoutParams = this.f9199b.getLayoutParams();
        if ((layoutParams instanceof ViewGroup.MarginLayoutParams) && this.f9199b.f9223m != null) {
            if (this.f9199b.getParent() == null) {
                return;
            }
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            marginLayoutParams.bottomMargin = this.f9199b.f9223m.bottom + (k() != null ? this.f9207j : this.f9203f);
            marginLayoutParams.leftMargin = this.f9199b.f9223m.left + this.f9204g;
            marginLayoutParams.rightMargin = this.f9199b.f9223m.right + this.f9205h;
            marginLayoutParams.topMargin = this.f9199b.f9223m.top;
            this.f9199b.requestLayout();
            if (z()) {
                this.f9199b.removeCallbacks(this.f9202e);
                this.f9199b.post(this.f9202e);
                return;
            }
            return;
        }
        Log.w(f9197s, "Unable to update margins because layout params are not MarginLayoutParams");
    }

    private void g(int i10) {
        if (this.f9199b.getAnimationMode() == 1) {
            D(i10);
        } else {
            F(i10);
        }
    }

    private int h() {
        if (k() == null) {
            return 0;
        }
        int[] iArr = new int[2];
        k().getLocationOnScreen(iArr);
        int i10 = iArr[1];
        int[] iArr2 = new int[2];
        this.f9198a.getLocationOnScreen(iArr2);
        return (iArr2[1] + this.f9198a.getHeight()) - i10;
    }

    private ValueAnimator j(float... fArr) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
        ofFloat.setInterpolator(AnimationUtils.f16555a);
        ofFloat.addUpdateListener(new b());
        return ofFloat;
    }

    private ValueAnimator m(float... fArr) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
        ofFloat.setInterpolator(AnimationUtils.f16558d);
        ofFloat.addUpdateListener(new c());
        return ofFloat;
    }

    private int n() {
        int height = this.f9199b.getHeight();
        ViewGroup.LayoutParams layoutParams = this.f9199b.getLayoutParams();
        return layoutParams instanceof ViewGroup.MarginLayoutParams ? height + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin : height;
    }

    private boolean q() {
        ViewGroup.LayoutParams layoutParams = this.f9199b.getLayoutParams();
        return (layoutParams instanceof CoordinatorLayout.e) && (((CoordinatorLayout.e) layoutParams).f() instanceof SwipeDismissBehavior);
    }

    private void w() {
        this.f9207j = h();
        G();
    }

    private void x(CoordinatorLayout.e eVar) {
        SwipeDismissBehavior<? extends View> swipeDismissBehavior = this.f9210m;
        if (swipeDismissBehavior == null) {
            swipeDismissBehavior = l();
        }
        if (swipeDismissBehavior instanceof Behavior) {
            ((Behavior) swipeDismissBehavior).o(this);
        }
        swipeDismissBehavior.j(new j());
        eVar.o(swipeDismissBehavior);
        if (k() == null) {
            eVar.f2068g = 80;
        }
    }

    private boolean z() {
        return this.f9206i > 0 && !this.f9201d && q();
    }

    final void A() {
        if (this.f9199b.getParent() == null) {
            ViewGroup.LayoutParams layoutParams = this.f9199b.getLayoutParams();
            if (layoutParams instanceof CoordinatorLayout.e) {
                x((CoordinatorLayout.e) layoutParams);
            }
            this.f9199b.b(this.f9198a);
            w();
            this.f9199b.setVisibility(4);
        }
        if (ViewCompat.Q(this.f9199b)) {
            B();
        } else {
            this.f9208k = true;
        }
    }

    void f() {
        this.f9199b.post(new k());
    }

    protected void i(int i10) {
        SnackbarManager.c().b(this.f9212o, i10);
    }

    public View k() {
        return null;
    }

    protected SwipeDismissBehavior<? extends View> l() {
        return new Behavior();
    }

    final void o(int i10) {
        if (y() && this.f9199b.getVisibility() == 0) {
            g(i10);
        } else {
            u(i10);
        }
    }

    public boolean p() {
        return SnackbarManager.c().e(this.f9212o);
    }

    void r() {
        WindowInsets rootWindowInsets = this.f9199b.getRootWindowInsets();
        if (rootWindowInsets != null) {
            this.f9206i = rootWindowInsets.getMandatorySystemGestureInsets().bottom;
            G();
        }
    }

    void s() {
        if (p()) {
            f9194p.post(new i());
        }
    }

    void t() {
        if (this.f9208k) {
            B();
            this.f9208k = false;
        }
    }

    void u(int i10) {
        SnackbarManager.c().h(this.f9212o);
        List<m<B>> list = this.f9209l;
        if (list != null) {
            for (int size = list.size() - 1; size >= 0; size--) {
                this.f9209l.get(size).a(this, i10);
            }
        }
        ViewParent parent = this.f9199b.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(this.f9199b);
        }
    }

    void v() {
        SnackbarManager.c().i(this.f9212o);
        List<m<B>> list = this.f9209l;
        if (list != null) {
            for (int size = list.size() - 1; size >= 0; size--) {
                this.f9209l.get(size).b(this);
            }
        }
    }

    boolean y() {
        AccessibilityManager accessibilityManager = this.f9211n;
        if (accessibilityManager == null) {
            return true;
        }
        List<AccessibilityServiceInfo> enabledAccessibilityServiceList = accessibilityManager.getEnabledAccessibilityServiceList(1);
        return enabledAccessibilityServiceList != null && enabledAccessibilityServiceList.isEmpty();
    }
}
