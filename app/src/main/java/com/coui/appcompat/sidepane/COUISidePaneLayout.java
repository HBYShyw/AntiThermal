package com.coui.appcompat.sidepane;

import android.R;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.PathInterpolator;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.view.AbsSavedState;
import androidx.customview.widget.ViewDragHelper;
import com.support.appcompat.R$color;
import com.support.bars.R$dimen;
import com.support.bars.R$layout;
import com.support.bars.R$styleable;
import h3.UIUtil;
import m1.COUIMoveEaseInterpolator;

/* loaded from: classes.dex */
public class COUISidePaneLayout extends RelativeLayout {
    private static final PathInterpolator F = new COUIMoveEaseInterpolator();
    private boolean A;
    private boolean B;
    private boolean C;
    private final Paint D;
    private boolean E;

    /* renamed from: e, reason: collision with root package name */
    private final int f7620e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f7621f;

    /* renamed from: g, reason: collision with root package name */
    private boolean f7622g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f7623h;

    /* renamed from: i, reason: collision with root package name */
    View f7624i;

    /* renamed from: j, reason: collision with root package name */
    float f7625j;

    /* renamed from: k, reason: collision with root package name */
    int f7626k;

    /* renamed from: l, reason: collision with root package name */
    boolean f7627l;

    /* renamed from: m, reason: collision with root package name */
    private h f7628m;

    /* renamed from: n, reason: collision with root package name */
    private g f7629n;

    /* renamed from: o, reason: collision with root package name */
    private h f7630o;

    /* renamed from: p, reason: collision with root package name */
    final ViewDragHelper f7631p;

    /* renamed from: q, reason: collision with root package name */
    private float f7632q;

    /* renamed from: r, reason: collision with root package name */
    private final float f7633r;

    /* renamed from: s, reason: collision with root package name */
    private float f7634s;

    /* renamed from: t, reason: collision with root package name */
    boolean f7635t;

    /* renamed from: u, reason: collision with root package name */
    private boolean f7636u;

    /* renamed from: v, reason: collision with root package name */
    private boolean f7637v;

    /* renamed from: w, reason: collision with root package name */
    private ValueAnimator f7638w;

    /* renamed from: x, reason: collision with root package name */
    private ValueAnimator f7639x;

    /* renamed from: y, reason: collision with root package name */
    private int f7640y;

    /* renamed from: z, reason: collision with root package name */
    private ImageButton f7641z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        boolean f7646e;

        /* renamed from: f, reason: collision with root package name */
        int f7647f;

        /* renamed from: g, reason: collision with root package name */
        boolean f7648g;

        /* loaded from: classes.dex */
        class a implements Parcelable.ClassLoaderCreator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            @Override // android.os.Parcelable.ClassLoaderCreator
            /* renamed from: b, reason: merged with bridge method [inline-methods] */
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, null);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: c, reason: merged with bridge method [inline-methods] */
            public SavedState[] newArray(int i10) {
                return new SavedState[i10];
            }
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeInt(this.f7646e ? 1 : 0);
            parcel.writeInt(this.f7648g ? 1 : 0);
            parcel.writeInt(this.f7647f);
        }

        SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.f7646e = parcel.readInt() != 0;
            this.f7648g = parcel.readInt() != 0;
            this.f7647f = parcel.readInt();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements View.OnClickListener {
        a() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (COUISidePaneLayout.this.q()) {
                COUISidePaneLayout.this.g();
            } else {
                COUISidePaneLayout.this.t();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements ValueAnimator.AnimatorUpdateListener {
        b() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if (COUISidePaneLayout.this.getChildAt(0) != null) {
                if (COUISidePaneLayout.this.f7640y == 1) {
                    View childAt = COUISidePaneLayout.this.getChildAt(0);
                    boolean p10 = COUISidePaneLayout.this.p();
                    float f10 = COUISidePaneLayout.this.f7634s;
                    if (!p10) {
                        f10 = -f10;
                    }
                    childAt.setTranslationX(f10 * ((Float) valueAnimator.getAnimatedValue()).floatValue());
                    return;
                }
                if (COUISidePaneLayout.this.f7640y == 0) {
                    View childAt2 = COUISidePaneLayout.this.getChildAt(0);
                    boolean p11 = COUISidePaneLayout.this.p();
                    float f11 = COUISidePaneLayout.this.f7634s;
                    if (!p11) {
                        f11 = -f11;
                    }
                    childAt2.setTranslationX(f11 * (1.0f - ((Float) valueAnimator.getAnimatedValue()).floatValue()));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements Animator.AnimatorListener {
        c() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            COUISidePaneLayout.this.C = false;
            if (COUISidePaneLayout.this.f7628m != null) {
                if (COUISidePaneLayout.this.f7640y == 1) {
                    COUISidePaneLayout.this.f7628m.d(1);
                } else if (COUISidePaneLayout.this.f7640y == 0) {
                    COUISidePaneLayout.this.f7628m.d(0);
                }
            }
            if (COUISidePaneLayout.this.f7630o != null) {
                if (COUISidePaneLayout.this.f7640y == 1) {
                    COUISidePaneLayout.this.f7630o.d(1);
                } else if (COUISidePaneLayout.this.f7640y == 0) {
                    COUISidePaneLayout.this.f7630o.d(0);
                }
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (COUISidePaneLayout.this.f7628m != null) {
                if (COUISidePaneLayout.this.f7640y == 1) {
                    COUISidePaneLayout.this.f7628m.a(1);
                } else if (COUISidePaneLayout.this.f7640y == 0) {
                    COUISidePaneLayout.this.f7628m.a(0);
                }
                COUISidePaneLayout.this.C = false;
            }
            if (COUISidePaneLayout.this.f7630o != null) {
                if (COUISidePaneLayout.this.f7640y == 1) {
                    COUISidePaneLayout.this.f7630o.a(1);
                } else if (COUISidePaneLayout.this.f7640y == 0) {
                    COUISidePaneLayout.this.f7630o.a(0);
                }
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            COUISidePaneLayout.this.C = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ float f7652a;

        d(float f10) {
            this.f7652a = f10;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float f10;
            float animatedFraction = valueAnimator.getAnimatedFraction();
            if (this.f7652a == 1.0f) {
                f10 = COUISidePaneLayout.this.f7632q;
            } else {
                f10 = COUISidePaneLayout.this.f7632q;
                animatedFraction = 1.0f - animatedFraction;
            }
            int i10 = (int) (f10 * animatedFraction);
            float animatedFraction2 = COUISidePaneLayout.this.f7640y == 1 ? 1.0f - valueAnimator.getAnimatedFraction() : valueAnimator.getAnimatedFraction();
            COUISidePaneLayout cOUISidePaneLayout = COUISidePaneLayout.this;
            if (cOUISidePaneLayout.f7624i != null) {
                if (cOUISidePaneLayout.f7628m != null) {
                    COUISidePaneLayout.this.f7628m.c(COUISidePaneLayout.this.f7624i, animatedFraction2);
                }
                if (COUISidePaneLayout.this.f7630o != null) {
                    COUISidePaneLayout.this.f7630o.c(COUISidePaneLayout.this.f7624i, animatedFraction2);
                }
            }
            COUISidePaneLayout.this.s(i10);
        }
    }

    /* loaded from: classes.dex */
    class e extends AccessibilityDelegateCompat {

        /* renamed from: a, reason: collision with root package name */
        private final Rect f7654a = new Rect();

        e() {
        }

        private void a(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2) {
            Rect rect = this.f7654a;
            accessibilityNodeInfoCompat2.j(rect);
            accessibilityNodeInfoCompat.Q(rect);
            accessibilityNodeInfoCompat2.k(rect);
            accessibilityNodeInfoCompat.R(rect);
            accessibilityNodeInfoCompat.A0(accessibilityNodeInfoCompat2.I());
            accessibilityNodeInfoCompat.l0(accessibilityNodeInfoCompat2.r());
            accessibilityNodeInfoCompat.V(accessibilityNodeInfoCompat2.m());
            accessibilityNodeInfoCompat.Z(accessibilityNodeInfoCompat2.o());
            accessibilityNodeInfoCompat.b0(accessibilityNodeInfoCompat2.A());
            accessibilityNodeInfoCompat.W(accessibilityNodeInfoCompat2.z());
            accessibilityNodeInfoCompat.d0(accessibilityNodeInfoCompat2.B());
            accessibilityNodeInfoCompat.e0(accessibilityNodeInfoCompat2.C());
            accessibilityNodeInfoCompat.P(accessibilityNodeInfoCompat2.w());
            accessibilityNodeInfoCompat.t0(accessibilityNodeInfoCompat2.G());
            accessibilityNodeInfoCompat.i0(accessibilityNodeInfoCompat2.D());
            accessibilityNodeInfoCompat.a(accessibilityNodeInfoCompat2.i());
            accessibilityNodeInfoCompat.k0(accessibilityNodeInfoCompat2.q());
        }

        public boolean b(View view) {
            return COUISidePaneLayout.this.o(view);
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            accessibilityEvent.setClassName(COUISidePaneLayout.class.getName());
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            AccessibilityNodeInfoCompat L = AccessibilityNodeInfoCompat.L(accessibilityNodeInfoCompat);
            super.onInitializeAccessibilityNodeInfo(view, L);
            a(accessibilityNodeInfoCompat, L);
            L.N();
            accessibilityNodeInfoCompat.V(COUISidePaneLayout.class.getName());
            accessibilityNodeInfoCompat.v0(view);
            Object D = ViewCompat.D(view);
            if (D instanceof View) {
                accessibilityNodeInfoCompat.n0((View) D);
            }
            int childCount = COUISidePaneLayout.this.getChildCount();
            for (int i10 = 1; i10 < childCount; i10++) {
                View childAt = COUISidePaneLayout.this.getChildAt(i10);
                if (!b(childAt) && childAt.getVisibility() == 0) {
                    ViewCompat.w0(childAt, 1);
                    accessibilityNodeInfoCompat.c(childAt);
                }
            }
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            if (b(view)) {
                return false;
            }
            return super.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
        }
    }

    /* loaded from: classes.dex */
    private class f extends ViewDragHelper.c {
        f() {
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public int a(View view, int i10, int i11) {
            LayoutParams layoutParams = (LayoutParams) COUISidePaneLayout.this.f7624i.getLayoutParams();
            if (COUISidePaneLayout.this.p()) {
                int width = COUISidePaneLayout.this.getWidth() - ((COUISidePaneLayout.this.getPaddingRight() + ((RelativeLayout.LayoutParams) layoutParams).rightMargin) + COUISidePaneLayout.this.f7624i.getWidth());
                return Math.max(Math.min(i10, width), width - COUISidePaneLayout.this.f7626k);
            }
            int paddingLeft = COUISidePaneLayout.this.getPaddingLeft() + ((RelativeLayout.LayoutParams) layoutParams).leftMargin;
            return Math.min(Math.max(i10, paddingLeft), COUISidePaneLayout.this.f7626k + paddingLeft);
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public int b(View view, int i10, int i11) {
            return view.getTop();
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public int d(View view) {
            return COUISidePaneLayout.this.f7626k;
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public void f(int i10, int i11) {
            COUISidePaneLayout cOUISidePaneLayout = COUISidePaneLayout.this;
            cOUISidePaneLayout.f7631p.c(cOUISidePaneLayout.f7624i, i11);
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public void i(View view, int i10) {
            COUISidePaneLayout.this.w();
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public void j(int i10) {
            if (COUISidePaneLayout.this.f7631p.B() == 0) {
                COUISidePaneLayout cOUISidePaneLayout = COUISidePaneLayout.this;
                if (cOUISidePaneLayout.f7625j == 0.0f) {
                    cOUISidePaneLayout.y(cOUISidePaneLayout.f7624i);
                    COUISidePaneLayout cOUISidePaneLayout2 = COUISidePaneLayout.this;
                    cOUISidePaneLayout2.j(cOUISidePaneLayout2.f7624i);
                    COUISidePaneLayout.this.f7635t = false;
                    return;
                }
                cOUISidePaneLayout.k(cOUISidePaneLayout.f7624i);
                COUISidePaneLayout.this.f7635t = true;
            }
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public void k(View view, int i10, int i11, int i12, int i13) {
            COUISidePaneLayout cOUISidePaneLayout = COUISidePaneLayout.this;
            if (cOUISidePaneLayout.f7624i == null) {
                cOUISidePaneLayout.f7625j = 0.0f;
                return;
            }
            if (cOUISidePaneLayout.p()) {
                i10 = (COUISidePaneLayout.this.getWidth() - i10) - COUISidePaneLayout.this.f7624i.getWidth();
            }
            COUISidePaneLayout.this.s(i10);
            COUISidePaneLayout.this.invalidate();
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public void l(View view, float f10, float f11) {
            int paddingLeft;
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            if (COUISidePaneLayout.this.p()) {
                int paddingRight = COUISidePaneLayout.this.getPaddingRight() + ((RelativeLayout.LayoutParams) layoutParams).rightMargin;
                if (f10 < 0.0f || (f10 == 0.0f && COUISidePaneLayout.this.f7625j > 0.5f)) {
                    paddingRight += COUISidePaneLayout.this.f7626k;
                }
                paddingLeft = (COUISidePaneLayout.this.getWidth() - paddingRight) - COUISidePaneLayout.this.f7624i.getWidth();
            } else {
                paddingLeft = ((RelativeLayout.LayoutParams) layoutParams).leftMargin + COUISidePaneLayout.this.getPaddingLeft();
                if (f10 > 0.0f || (f10 == 0.0f && COUISidePaneLayout.this.f7625j > 0.5f)) {
                    paddingLeft += COUISidePaneLayout.this.f7626k;
                }
            }
            COUISidePaneLayout.this.f7631p.P(paddingLeft, view.getTop());
            COUISidePaneLayout.this.invalidate();
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public boolean m(View view, int i10) {
            if (COUISidePaneLayout.this.f7627l) {
                return false;
            }
            return ((LayoutParams) view.getLayoutParams()).f7644b;
        }
    }

    /* loaded from: classes.dex */
    public interface g {
        void a();
    }

    /* loaded from: classes.dex */
    public interface h {
        void a(int i10);

        void b(int i10);

        void c(View view, float f10);

        void d(int i10);
    }

    public COUISidePaneLayout(Context context) {
        this(context, null);
    }

    private boolean h(View view, int i10) {
        if (!this.f7636u && !x(0.0f, i10)) {
            return false;
        }
        this.f7635t = false;
        return true;
    }

    private void i() {
        this.f7641z = (ImageButton) LayoutInflater.from(getContext()).inflate(R$layout.coui_sliding_icon_layout, (ViewGroup) null);
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        ((RelativeLayout.LayoutParams) layoutParams).topMargin = getResources().getDimensionPixelOffset(R$dimen.coui_side_pane_layout_icon_margin_top);
        layoutParams.setMarginStart(getResources().getDimensionPixelOffset(R$dimen.coui_side_pane_layout_icon_margin_start));
        this.f7641z.setOnClickListener(new a());
        addViewInLayout(this.f7641z, 2, layoutParams);
    }

    private void m() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.f7639x = ofFloat;
        ofFloat.setDuration(483L);
        ValueAnimator valueAnimator = this.f7639x;
        PathInterpolator pathInterpolator = F;
        valueAnimator.setInterpolator(pathInterpolator);
        this.f7639x.addUpdateListener(new b());
        this.f7639x.addListener(new c());
        ValueAnimator valueAnimator2 = new ValueAnimator();
        this.f7638w = valueAnimator2;
        valueAnimator2.setFloatValues(0.0f, 1.0f);
        this.f7638w.setDuration(483L);
        this.f7638w.setInterpolator(pathInterpolator);
    }

    private boolean u(View view, int i10) {
        if (!this.f7636u && !x(1.0f, i10)) {
            return false;
        }
        this.f7635t = true;
        return true;
    }

    private static boolean z(View view) {
        return view.isOpaque();
    }

    @Override // android.widget.RelativeLayout, android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return (layoutParams instanceof LayoutParams) && super.checkLayoutParams(layoutParams);
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.f7631p.n(true)) {
            if (!this.f7621f) {
                this.f7631p.a();
            } else {
                ViewCompat.b0(this);
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int a10 = UIUtil.a(motionEvent, motionEvent.getActionIndex());
        boolean z10 = false;
        if (!p() ? getChildAt(0).getRight() <= motionEvent.getX(a10) : getChildAt(0).getLeft() > motionEvent.getX(a10)) {
            z10 = true;
        }
        if (q() && z10 && this.B && (motionEvent.getAction() & 15) == 5) {
            g gVar = this.f7629n;
            if (gVar != null) {
                gVar.a();
            }
            return true;
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j10) {
        boolean drawChild = super.drawChild(canvas, view, j10);
        if (this.A || this.B) {
            boolean n10 = n(view);
            int right = getChildAt(1).getRight();
            int right2 = (int) (getChildAt(0).getRight() * this.f7625j);
            int width = getWidth();
            int color = getContext().getResources().getColor(R$color.coui_color_mask);
            float f10 = this.f7625j;
            int i10 = (int) (right + ((width - right) * (1.0f - f10)));
            if (f10 > 0.0f && n10) {
                this.D.setColor((((int) ((((-16777216) & color) >>> 24) * f10)) << 24) | (color & 16777215));
                if (p()) {
                    canvas.drawRect(getPaddingEnd(), 0.0f, i10, getHeight(), this.D);
                } else {
                    canvas.drawRect(right2, 0.0f, width, getHeight(), this.D);
                }
            }
        }
        return drawChild;
    }

    public boolean g() {
        this.f7639x.cancel();
        this.f7640y = 1;
        this.f7637v = false;
        this.f7639x.setCurrentFraction(1.0f - this.f7625j);
        this.f7639x.start();
        h hVar = this.f7628m;
        if (hVar != null) {
            hVar.b(1);
        }
        h hVar2 = this.f7630o;
        if (hVar2 != null) {
            hVar2.b(1);
        }
        return h(this.f7624i, 0);
    }

    @Override // android.widget.RelativeLayout, android.view.ViewGroup
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    @Override // android.view.ViewGroup
    protected int getChildDrawingOrder(int i10, int i11) {
        return (i10 < 3 || i11 >= 2 || !this.A) ? super.getChildDrawingOrder(i10, i11) : (i10 - i11) - 2;
    }

    public ImageButton getIconView() {
        return this.f7641z;
    }

    @Override // android.view.ViewGroup
    protected boolean isChildrenDrawingOrderEnabled() {
        return this.A || super.isChildrenDrawingOrderEnabled();
    }

    void j(View view) {
        sendAccessibilityEvent(32);
    }

    void k(View view) {
        sendAccessibilityEvent(32);
    }

    void l(View view) {
        v();
    }

    boolean n(View view) {
        return view == getChildAt(1);
    }

    boolean o(View view) {
        if (view == null) {
            return false;
        }
        return this.f7621f && ((LayoutParams) view.getLayoutParams()).f7645c && this.f7625j > 0.0f;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.f7636u = true;
        if (this.f7622g && this.f7640y == 0) {
            this.f7637v = true;
            u(this.f7624i, 0);
        } else {
            g();
        }
        if (this.f7623h && this.f7641z == null) {
            i();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.f7636u = true;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean z10 = false;
        if (getChildAt(0) != null && (this.B || this.A)) {
            if (!p() ? getChildAt(0).getRight() <= motionEvent.getX() : getChildAt(0).getLeft() > motionEvent.getX()) {
                z10 = true;
            }
            if (q() && z10 && this.B && motionEvent.getAction() == 0) {
                g gVar = this.f7629n;
                if (gVar != null) {
                    gVar.a();
                }
                return true;
            }
            if (z10 && q() && this.E && this.A) {
                g();
                return true;
            }
            return super.onInterceptTouchEvent(motionEvent);
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.widget.RelativeLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        int i14;
        int i15;
        int i16;
        int i17;
        int i18;
        int i19;
        char c10;
        boolean p10 = p();
        int i20 = 1;
        if (p10) {
            this.f7631p.N(2);
        } else {
            this.f7631p.N(1);
        }
        int i21 = i12 - i10;
        int paddingRight = p10 ? getPaddingRight() : getPaddingLeft();
        int paddingLeft = p10 ? getPaddingLeft() : getPaddingRight();
        int paddingTop = getPaddingTop();
        int childCount = getChildCount();
        float f10 = 0.0f;
        float f11 = 1.0f;
        if (this.f7636u) {
            this.f7625j = this.f7635t ? 1.0f : 0.0f;
        }
        int i22 = 0;
        int i23 = paddingRight;
        int i24 = 0;
        while (i24 < childCount) {
            View childAt = getChildAt(i24);
            if (childAt.getVisibility() == 8) {
                i19 = i20;
                c10 = 2;
            } else {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                int measuredWidth = childAt.getMeasuredWidth();
                if (i24 == i20) {
                    if (this.A) {
                        measuredWidth = Math.min(getWidth(), childAt.getMeasuredWidth());
                    } else {
                        float f12 = this.f7625j;
                        if (f12 == f10) {
                            float f13 = this.f7632q;
                            Resources resources = getResources();
                            int i25 = R$dimen.coui_sliding_pane_width;
                            if (f13 == resources.getDimensionPixelOffset(i25)) {
                                measuredWidth = Math.max(getWidth(), childAt.getMeasuredWidth());
                            } else {
                                measuredWidth = (int) Math.max((getWidth() - this.f7632q) + getResources().getDimensionPixelOffset(i25), childAt.getMeasuredWidth());
                            }
                        } else if (f12 == f11) {
                            measuredWidth = Math.max(getWidth() - getChildAt(i22).getMeasuredWidth(), childAt.getMeasuredWidth());
                        }
                    }
                    measuredWidth = Math.min(getWidth(), measuredWidth);
                }
                if (layoutParams.f7644b) {
                    int i26 = i21 - paddingLeft;
                    int min = (Math.min(i23, i26 - this.f7620e) - paddingRight) - (((RelativeLayout.LayoutParams) layoutParams).leftMargin + ((RelativeLayout.LayoutParams) layoutParams).rightMargin);
                    this.f7626k = min;
                    int i27 = p10 ? ((RelativeLayout.LayoutParams) layoutParams).rightMargin : ((RelativeLayout.LayoutParams) layoutParams).leftMargin;
                    layoutParams.f7645c = ((paddingRight + i27) + min) + (measuredWidth / 2) > i26;
                    int i28 = (int) (min * this.f7625j);
                    paddingRight += i27 + i28;
                    this.f7625j = i28 / min;
                } else {
                    paddingRight = i23;
                }
                if (p10) {
                    if (layoutParams.f7644b) {
                        i18 = (this.A && i24 == 1) ? i21 : i21 - ((int) (paddingRight + ((this.f7632q - this.f7634s) * (1.0f - this.f7625j))));
                    } else {
                        i18 = i21 - paddingRight;
                    }
                    i17 = i18 - measuredWidth;
                } else {
                    if (!layoutParams.f7644b) {
                        i14 = paddingRight + measuredWidth;
                        i15 = paddingRight;
                    } else if (this.A && i24 == 1) {
                        i14 = (int) (((paddingRight + measuredWidth) + this.f7632q) - this.f7634s);
                        i15 = 0;
                        i16 = 1;
                        if (i24 == i16 || COUISidePaneUtils.c((Activity) getContext())) {
                            int i29 = i14;
                            i17 = i15;
                            i18 = i29;
                        } else {
                            i17 = i15;
                            i18 = i21;
                        }
                    } else {
                        i15 = (int) (paddingRight + ((this.f7632q - this.f7634s) * (1.0f - this.f7625j)));
                        i14 = i15 + measuredWidth;
                    }
                    i16 = 1;
                    if (i24 == i16) {
                    }
                    int i292 = i14;
                    i17 = i15;
                    i18 = i292;
                }
                int measuredHeight = childAt.getMeasuredHeight() + paddingTop;
                if (i24 == 2) {
                    if (p10) {
                        childAt.layout((i21 - layoutParams.getMarginStart()) - measuredWidth, ((RelativeLayout.LayoutParams) layoutParams).topMargin, i21 - layoutParams.getMarginStart(), ((RelativeLayout.LayoutParams) layoutParams).topMargin + measuredWidth);
                    } else {
                        childAt.layout(layoutParams.getMarginStart(), ((RelativeLayout.LayoutParams) layoutParams).topMargin, layoutParams.getMarginStart() + measuredWidth, ((RelativeLayout.LayoutParams) layoutParams).topMargin + measuredWidth);
                    }
                    i19 = 1;
                } else {
                    i19 = 1;
                    if (i24 == 1 && p10) {
                        childAt.layout(0, paddingTop, i18, measuredHeight);
                    } else {
                        childAt.layout(i17, paddingTop, i18, measuredHeight);
                    }
                }
                c10 = 2;
                if (i24 < 2) {
                    i23 += childAt.getWidth();
                }
            }
            i24++;
            i22 = 0;
            f10 = 0.0f;
            f11 = 1.0f;
            i20 = i19;
        }
        if (this.f7636u) {
            y(this.f7624i);
        }
        this.f7636u = false;
    }

    @Override // android.widget.RelativeLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        int paddingTop;
        int i12;
        int makeMeasureSpec;
        int i13;
        int i14;
        int makeMeasureSpec2;
        float f10;
        float f11;
        int i15;
        int i16;
        int makeMeasureSpec3;
        int i17;
        int makeMeasureSpec4;
        int i18;
        int i19;
        int i20;
        float max;
        int mode = View.MeasureSpec.getMode(i10);
        int size = View.MeasureSpec.getSize(i10);
        int mode2 = View.MeasureSpec.getMode(i11);
        int size2 = View.MeasureSpec.getSize(i11);
        if (mode != 1073741824) {
            if (!isInEditMode()) {
                throw new IllegalStateException("Width must have an exact value or MATCH_PARENT");
            }
            if (mode != Integer.MIN_VALUE && mode == 0) {
                size = 300;
            }
        } else if (mode2 == 0) {
            if (!isInEditMode()) {
                throw new IllegalStateException("Height must not be UNSPECIFIED");
            }
            if (mode2 == 0) {
                size2 = 300;
                mode2 = Integer.MIN_VALUE;
            }
        }
        boolean z10 = false;
        if (mode2 != Integer.MIN_VALUE) {
            i12 = mode2 != 1073741824 ? 0 : (size2 - getPaddingTop()) - getPaddingBottom();
            paddingTop = i12;
        } else {
            paddingTop = (size2 - getPaddingTop()) - getPaddingBottom();
            i12 = 0;
        }
        int paddingLeft = (size - getPaddingLeft()) - getPaddingRight();
        int childCount = getChildCount();
        if (childCount > 3) {
            Log.e("COUISidePaneLayout", "onMeasure: More than two child views are not supported.");
        }
        this.f7624i = null;
        int i21 = 0;
        boolean z11 = false;
        int i22 = paddingLeft;
        float f12 = 0.0f;
        while (i21 < childCount) {
            View childAt = getChildAt(i21);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            if (childAt.getVisibility() == 8) {
                layoutParams.f7645c = z10;
            } else {
                float f13 = layoutParams.f7643a;
                if (f13 > 0.0f) {
                    f12 += f13;
                    if (((RelativeLayout.LayoutParams) layoutParams).width == 0) {
                    }
                }
                int i23 = ((RelativeLayout.LayoutParams) layoutParams).leftMargin + ((RelativeLayout.LayoutParams) layoutParams).rightMargin;
                int i24 = ((RelativeLayout.LayoutParams) layoutParams).width;
                if (i24 == -2 || i24 == -1) {
                    i24 = paddingLeft - i23;
                }
                if (i21 == 1 && this.f7637v && !this.A) {
                    i24 = (int) (i24 - this.f7632q);
                    f10 = this.f7634s;
                } else {
                    f10 = 0.0f;
                }
                if (i21 == 1) {
                    if (this.A) {
                        i24 = paddingLeft;
                        f11 = f12;
                    } else {
                        float f14 = this.f7625j;
                        if (f14 == 0.0f) {
                            float f15 = this.f7632q;
                            Resources resources = getResources();
                            f11 = f12;
                            int i25 = R$dimen.coui_sliding_pane_width;
                            if (f15 == resources.getDimensionPixelOffset(i25)) {
                                max = Math.max(paddingLeft - (this.f7632q - this.f7634s), childAt.getMeasuredWidth());
                            } else {
                                max = Math.max((paddingLeft - this.f7634s) + getResources().getDimensionPixelOffset(i25), childAt.getMeasuredWidth());
                            }
                            i24 = (int) max;
                        } else {
                            f11 = f12;
                            if (f14 == 1.0f) {
                                i24 = Math.max(paddingLeft - getChildAt(0).getMeasuredWidth(), i24);
                            }
                        }
                    }
                    if (!this.A) {
                        i24 = Math.min(paddingLeft, i24);
                    }
                    i16 = !COUISidePaneUtils.c((Activity) getContext()) ? paddingLeft : i24;
                    i15 = 1;
                } else {
                    f11 = f12;
                    int i26 = i24;
                    i15 = 1;
                    i16 = i26;
                }
                if (i21 == i15 && i16 <= 0) {
                    i16 = Math.max(paddingLeft - (this.f7640y == 0 ? getChildAt(0).getMeasuredWidth() : 0), ((RelativeLayout.LayoutParams) layoutParams).width);
                }
                int i27 = ((RelativeLayout.LayoutParams) layoutParams).width;
                if (i27 == -2) {
                    makeMeasureSpec3 = View.MeasureSpec.makeMeasureSpec(i16, Integer.MIN_VALUE);
                } else if (i27 == -1) {
                    makeMeasureSpec3 = View.MeasureSpec.makeMeasureSpec(i16, 1073741824);
                } else {
                    makeMeasureSpec3 = View.MeasureSpec.makeMeasureSpec(i16, 1073741824);
                }
                int i28 = ((RelativeLayout.LayoutParams) layoutParams).height;
                if (i28 == -2) {
                    makeMeasureSpec4 = View.MeasureSpec.makeMeasureSpec(paddingTop, Integer.MIN_VALUE);
                    i18 = makeMeasureSpec3;
                    i19 = 3;
                    i17 = 1073741824;
                } else {
                    if (i28 == -1) {
                        i17 = 1073741824;
                        makeMeasureSpec4 = View.MeasureSpec.makeMeasureSpec(paddingTop, 1073741824);
                    } else {
                        i17 = 1073741824;
                        makeMeasureSpec4 = View.MeasureSpec.makeMeasureSpec(i28, 1073741824);
                    }
                    i18 = makeMeasureSpec3;
                    i19 = 3;
                }
                if (i21 == i19) {
                    i20 = View.MeasureSpec.makeMeasureSpec(getResources().getDimensionPixelSize(R$dimen.coui_side_pane_layout_icon_size), i17);
                    makeMeasureSpec4 = View.MeasureSpec.makeMeasureSpec(paddingTop, i17);
                } else {
                    i20 = i18;
                }
                childAt.measure(i20, makeMeasureSpec4);
                if (i21 < 2) {
                    int measuredWidth = (int) (childAt.getMeasuredWidth() + f10);
                    int measuredHeight = childAt.getMeasuredHeight();
                    if (mode2 == Integer.MIN_VALUE && measuredHeight > i12) {
                        i12 = Math.min(measuredHeight, paddingTop);
                    }
                    i22 -= measuredWidth;
                    boolean z12 = i22 <= 0;
                    layoutParams.f7644b = z12;
                    z11 |= z12;
                    if (z12) {
                        this.f7624i = childAt;
                    }
                }
                f12 = f11;
            }
            i21++;
            z10 = false;
        }
        int i29 = 8;
        if (z11 || f12 > 0.0f) {
            int i30 = paddingLeft - this.f7620e;
            int i31 = 0;
            while (i31 < childCount) {
                View childAt2 = getChildAt(i31);
                if (childAt2.getVisibility() != i29) {
                    LayoutParams layoutParams2 = (LayoutParams) childAt2.getLayoutParams();
                    if (childAt2.getVisibility() != i29) {
                        boolean z13 = ((RelativeLayout.LayoutParams) layoutParams2).width == 0 && layoutParams2.f7643a > 0.0f;
                        int measuredWidth2 = z13 ? 0 : childAt2.getMeasuredWidth();
                        if (!z11 || childAt2 == this.f7624i) {
                            if (layoutParams2.f7643a > 0.0f) {
                                if (((RelativeLayout.LayoutParams) layoutParams2).width == 0) {
                                    int i32 = ((RelativeLayout.LayoutParams) layoutParams2).height;
                                    if (i32 == -2) {
                                        makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(paddingTop, Integer.MIN_VALUE);
                                    } else if (i32 == -1) {
                                        makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(paddingTop, 1073741824);
                                    } else {
                                        makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i32, 1073741824);
                                    }
                                } else {
                                    makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(childAt2.getMeasuredHeight(), 1073741824);
                                }
                                if (z11) {
                                    int i33 = paddingLeft - (((RelativeLayout.LayoutParams) layoutParams2).leftMargin + ((RelativeLayout.LayoutParams) layoutParams2).rightMargin);
                                    i13 = i30;
                                    int makeMeasureSpec5 = View.MeasureSpec.makeMeasureSpec(i33, 1073741824);
                                    if (measuredWidth2 != i33) {
                                        childAt2.measure(makeMeasureSpec5, makeMeasureSpec);
                                    }
                                    i31++;
                                    i30 = i13;
                                    i29 = 8;
                                } else {
                                    i13 = i30;
                                    childAt2.measure(View.MeasureSpec.makeMeasureSpec(measuredWidth2 + ((int) ((layoutParams2.f7643a * Math.max(0, i22)) / f12)), 1073741824), makeMeasureSpec);
                                    i31++;
                                    i30 = i13;
                                    i29 = 8;
                                }
                            }
                        } else if (((RelativeLayout.LayoutParams) layoutParams2).width < 0 && (measuredWidth2 > i30 || layoutParams2.f7643a > 0.0f)) {
                            if (z13) {
                                int i34 = ((RelativeLayout.LayoutParams) layoutParams2).height;
                                if (i34 == -2) {
                                    makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(paddingTop, Integer.MIN_VALUE);
                                    i14 = 1073741824;
                                } else if (i34 == -1) {
                                    i14 = 1073741824;
                                    makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(paddingTop, 1073741824);
                                } else {
                                    i14 = 1073741824;
                                    makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(i34, 1073741824);
                                }
                            } else {
                                i14 = 1073741824;
                                makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(childAt2.getMeasuredHeight(), 1073741824);
                            }
                            childAt2.measure(View.MeasureSpec.makeMeasureSpec(i30, i14), makeMeasureSpec2);
                        }
                    }
                }
                i13 = i30;
                i31++;
                i30 = i13;
                i29 = 8;
            }
        }
        setMeasuredDimension(size, i12 + getPaddingTop() + getPaddingBottom());
        this.f7621f = z11;
        if (this.f7631p.B() == 0 || z11) {
            return;
        }
        this.f7631p.a();
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        boolean z10 = this.f7622g;
        boolean z11 = savedState.f7648g;
        if (z10 != z11) {
            if (z11) {
                return;
            }
            this.f7637v = true;
            t();
            this.f7635t = true;
            this.f7640y = 0;
            return;
        }
        if (savedState.f7646e) {
            this.f7637v = true;
            t();
        } else {
            g();
        }
        this.f7635t = savedState.f7646e;
        this.f7640y = savedState.f7647f;
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.f7646e = r() ? q() : this.f7635t;
        savedState.f7648g = this.f7622g;
        savedState.f7647f = this.f7640y;
        return savedState;
    }

    @Override // android.view.View
    protected void onSizeChanged(int i10, int i11, int i12, int i13) {
        super.onSizeChanged(i10, i11, i12, i13);
        if (i10 != i12) {
            this.f7636u = true;
        }
    }

    boolean p() {
        return ViewCompat.x(this) == 1;
    }

    public boolean q() {
        return this.f7640y == 0;
    }

    public boolean r() {
        return this.f7621f;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestChildFocus(View view, View view2) {
        super.requestChildFocus(view, view2);
        if (isInTouchMode() || this.f7621f) {
            return;
        }
        this.f7635t = view == this.f7624i;
    }

    void s(int i10) {
        boolean p10 = p();
        View view = this.f7624i;
        if (view == null) {
            return;
        }
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        this.f7625j = (i10 - ((p10 ? getPaddingRight() : getPaddingLeft()) + (p10 ? ((RelativeLayout.LayoutParams) layoutParams).rightMargin : ((RelativeLayout.LayoutParams) layoutParams).leftMargin))) / this.f7626k;
        l(this.f7624i);
    }

    public void setAlwaysShowMask(boolean z10) {
        this.B = z10;
        invalidate();
    }

    public void setCoverStyle(boolean z10) {
        this.A = z10;
    }

    public void setCreateIcon(boolean z10) {
        this.f7623h = z10;
    }

    public void setDefaultShowPane(Boolean bool) {
        this.f7622g = bool.booleanValue();
        if (!bool.booleanValue()) {
            if (getChildCount() > 0) {
                getChildAt(0).setVisibility(8);
                ViewGroup.LayoutParams layoutParams = getChildAt(1).getLayoutParams();
                if (this.A) {
                    layoutParams.width = getWidth();
                } else {
                    layoutParams.width = (int) ((getWidth() - this.f7632q) - (this.f7634s * (this.f7625j - 1.0f)));
                }
            }
            setIconViewVisible(8);
            return;
        }
        if (getChildCount() > 0) {
            getChildAt(0).setVisibility(0);
            ViewGroup.LayoutParams layoutParams2 = getChildAt(1).getLayoutParams();
            if (this.A) {
                layoutParams2.width = getWidth();
            } else {
                layoutParams2.width = (int) ((getWidth() - this.f7632q) - (this.f7634s * (this.f7625j - 1.0f)));
            }
            if (this.f7641z == null) {
                i();
            } else {
                setIconViewVisible(0);
            }
        }
    }

    public void setFirstViewWidth(int i10) {
        this.f7632q = i10;
    }

    public void setIconViewVisible(int i10) {
        ImageButton imageButton = this.f7641z;
        if (imageButton != null) {
            imageButton.setVisibility(i10);
        }
    }

    public void setLifeCycleObserverListener(h hVar) {
        this.f7630o = hVar;
    }

    public void setOnMaskClickListener(g gVar) {
        this.f7629n = gVar;
    }

    public void setPanelSlideListener(h hVar) {
        this.f7628m = hVar;
    }

    public void setSlideDistance(float f10) {
        this.f7634s = f10;
    }

    public void setTouchContentEnable(boolean z10) {
        this.E = z10;
    }

    public boolean t() {
        this.f7639x.cancel();
        this.f7640y = 0;
        this.f7639x.setCurrentFraction(this.f7625j);
        this.f7639x.start();
        h hVar = this.f7628m;
        if (hVar != null) {
            hVar.b(0);
        }
        h hVar2 = this.f7630o;
        if (hVar2 != null) {
            hVar2.b(0);
        }
        return u(this.f7624i, 0);
    }

    public void v() {
        if (getChildAt(1) != null) {
            ViewGroup.LayoutParams layoutParams = getChildAt(1).getLayoutParams();
            if (this.A) {
                layoutParams.width = getWidth();
            } else {
                layoutParams.width = (int) ((getWidth() - this.f7632q) - (this.f7634s * (this.f7625j - 1.0f)));
            }
            getChildAt(1).setLayoutParams(layoutParams);
            getChildAt(1).requestLayout();
        }
    }

    void w() {
        int childCount = getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = getChildAt(i10);
            if (childAt.getVisibility() == 4) {
                childAt.setVisibility(0);
            }
        }
    }

    @SuppressLint({"Recycle"})
    boolean x(float f10, int i10) {
        if (!this.f7621f) {
            return false;
        }
        this.f7638w.cancel();
        this.f7638w.removeAllUpdateListeners();
        if (f10 == 0.0f) {
            this.f7638w.setCurrentFraction(1.0f - this.f7625j);
        } else {
            this.f7638w.setCurrentFraction(this.f7625j);
        }
        this.f7638w.addUpdateListener(new d(f10));
        this.f7638w.start();
        w();
        ViewCompat.b0(this);
        return true;
    }

    void y(View view) {
        int i10;
        int i11;
        int i12;
        int i13;
        boolean z10;
        View view2 = view;
        boolean p10 = p();
        int width = p10 ? getWidth() - getPaddingRight() : getPaddingLeft();
        int paddingLeft = p10 ? getPaddingLeft() : getWidth() - getPaddingRight();
        int paddingTop = getPaddingTop();
        int height = getHeight() - getPaddingBottom();
        if (view2 == null || !z(view)) {
            i10 = 0;
            i11 = 0;
            i12 = 0;
            i13 = 0;
        } else {
            i10 = view.getLeft();
            i11 = view.getRight();
            i12 = view.getTop();
            i13 = view.getBottom();
        }
        int childCount = getChildCount();
        int i14 = 0;
        while (i14 < childCount) {
            View childAt = getChildAt(i14);
            if (childAt == view2) {
                return;
            }
            if (childAt.getVisibility() == 8) {
                z10 = p10;
            } else {
                z10 = p10;
                childAt.setVisibility((Math.max(p10 ? paddingLeft : width, childAt.getLeft()) < i10 || Math.max(paddingTop, childAt.getTop()) < i12 || Math.min(p10 ? width : paddingLeft, childAt.getRight()) > i11 || Math.min(height, childAt.getBottom()) > i13) ? 0 : 4);
            }
            i14++;
            view2 = view;
            p10 = z10;
        }
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends RelativeLayout.LayoutParams {

        /* renamed from: d, reason: collision with root package name */
        private static final int[] f7642d = {R.attr.layout_weight};

        /* renamed from: a, reason: collision with root package name */
        public float f7643a;

        /* renamed from: b, reason: collision with root package name */
        boolean f7644b;

        /* renamed from: c, reason: collision with root package name */
        boolean f7645c;

        public LayoutParams() {
            super(-1, -1);
            this.f7643a = 0.0f;
        }

        public LayoutParams(int i10, int i11) {
            super(i10, i11);
            this.f7643a = 0.0f;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.f7643a = 0.0f;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.f7643a = 0.0f;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.f7643a = 0.0f;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, f7642d);
            this.f7643a = obtainStyledAttributes.getFloat(0, 0.0f);
            obtainStyledAttributes.recycle();
        }
    }

    public COUISidePaneLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @Override // android.widget.RelativeLayout, android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams) layoutParams);
        }
        return new LayoutParams(layoutParams);
    }

    public COUISidePaneLayout(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f7622g = true;
        this.f7623h = true;
        this.f7636u = true;
        this.f7637v = false;
        this.B = false;
        this.E = true;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUISidePaneLayout, i10, 0);
        float f10 = context.getResources().getDisplayMetrics().density;
        this.f7620e = (int) ((32.0f * f10) + 0.5f);
        int i11 = R$styleable.COUISidePaneLayout_firstPaneWidth;
        Resources resources = getResources();
        int i12 = R$dimen.coui_sliding_pane_width;
        this.f7632q = obtainStyledAttributes.getDimension(i11, resources.getDimensionPixelOffset(i12));
        float dimension = obtainStyledAttributes.getDimension(R$styleable.COUISidePaneLayout_expandPaneWidth, getResources().getDimensionPixelOffset(i12));
        this.f7633r = dimension;
        this.A = obtainStyledAttributes.getBoolean(R$styleable.COUISidePaneLayout_coverStyle, false);
        this.f7634s = dimension;
        this.D = new Paint();
        this.f7640y = 0;
        setWillNotDraw(false);
        ViewCompat.l0(this, new e());
        ViewCompat.w0(this, 1);
        ViewDragHelper o10 = ViewDragHelper.o(this, 0.5f, new f());
        this.f7631p = o10;
        o10.O(f10 * 400.0f);
        m();
        obtainStyledAttributes.recycle();
    }

    @Override // android.widget.RelativeLayout, android.view.ViewGroup
    public RelativeLayout.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }
}
