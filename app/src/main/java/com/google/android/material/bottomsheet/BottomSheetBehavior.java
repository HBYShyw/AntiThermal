package com.google.android.material.bottomsheet;

import android.R;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.customview.view.AbsSavedState;
import androidx.customview.widget.ViewDragHelper;
import c4.MaterialShapeDrawable;
import c4.ShapeAppearanceModel;
import com.google.android.material.R$attr;
import com.google.android.material.R$dimen;
import com.google.android.material.R$string;
import com.google.android.material.R$style;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.ViewUtils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import z3.MaterialResources;

/* loaded from: classes.dex */
public class BottomSheetBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    /* renamed from: h0, reason: collision with root package name */
    private static final int f8392h0 = R$style.Widget_Design_BottomSheet_Modal;
    private int A;
    private ShapeAppearanceModel B;
    private boolean C;
    private final BottomSheetBehavior<V>.g D;
    private ValueAnimator E;
    int F;
    int G;
    int H;
    float I;
    int J;
    float K;
    boolean L;
    private boolean M;
    private boolean N;
    int O;
    int P;
    ViewDragHelper Q;
    private boolean R;
    private int S;
    private boolean T;
    private int U;
    int V;
    int W;
    WeakReference<V> X;
    WeakReference<View> Y;
    private final ArrayList<f> Z;

    /* renamed from: a0, reason: collision with root package name */
    private VelocityTracker f8393a0;

    /* renamed from: b0, reason: collision with root package name */
    int f8394b0;

    /* renamed from: c0, reason: collision with root package name */
    private int f8395c0;

    /* renamed from: d0, reason: collision with root package name */
    boolean f8396d0;

    /* renamed from: e, reason: collision with root package name */
    private int f8397e;

    /* renamed from: e0, reason: collision with root package name */
    private Map<View, Integer> f8398e0;

    /* renamed from: f, reason: collision with root package name */
    private boolean f8399f;

    /* renamed from: f0, reason: collision with root package name */
    private int f8400f0;

    /* renamed from: g, reason: collision with root package name */
    private boolean f8401g;

    /* renamed from: g0, reason: collision with root package name */
    private final ViewDragHelper.c f8402g0;

    /* renamed from: h, reason: collision with root package name */
    private float f8403h;

    /* renamed from: i, reason: collision with root package name */
    private int f8404i;

    /* renamed from: j, reason: collision with root package name */
    private boolean f8405j;

    /* renamed from: k, reason: collision with root package name */
    private int f8406k;

    /* renamed from: l, reason: collision with root package name */
    private int f8407l;

    /* renamed from: m, reason: collision with root package name */
    private MaterialShapeDrawable f8408m;

    /* renamed from: n, reason: collision with root package name */
    private ColorStateList f8409n;

    /* renamed from: o, reason: collision with root package name */
    private int f8410o;

    /* renamed from: p, reason: collision with root package name */
    private int f8411p;

    /* renamed from: q, reason: collision with root package name */
    private int f8412q;

    /* renamed from: r, reason: collision with root package name */
    private boolean f8413r;

    /* renamed from: s, reason: collision with root package name */
    private boolean f8414s;

    /* renamed from: t, reason: collision with root package name */
    private boolean f8415t;

    /* renamed from: u, reason: collision with root package name */
    private boolean f8416u;

    /* renamed from: v, reason: collision with root package name */
    private boolean f8417v;

    /* renamed from: w, reason: collision with root package name */
    private boolean f8418w;

    /* renamed from: x, reason: collision with root package name */
    private boolean f8419x;

    /* renamed from: y, reason: collision with root package name */
    private boolean f8420y;

    /* renamed from: z, reason: collision with root package name */
    private int f8421z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ View f8427e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ int f8428f;

        a(View view, int i10) {
            this.f8427e = view;
            this.f8428f = i10;
        }

        @Override // java.lang.Runnable
        public void run() {
            BottomSheetBehavior.this.n0(this.f8427e, this.f8428f, false);
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
            if (BottomSheetBehavior.this.f8408m != null) {
                BottomSheetBehavior.this.f8408m.b0(floatValue);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements ViewUtils.OnApplyWindowInsetsListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ boolean f8431a;

        c(boolean z10) {
            this.f8431a = z10;
        }

        /* JADX WARN: Removed duplicated region for block: B:23:0x0089  */
        /* JADX WARN: Removed duplicated region for block: B:28:0x009a  */
        /* JADX WARN: Removed duplicated region for block: B:32:0x00a6  */
        /* JADX WARN: Removed duplicated region for block: B:35:0x00b4  */
        /* JADX WARN: Removed duplicated region for block: B:38:0x00c3  */
        @Override // com.google.android.material.internal.ViewUtils.OnApplyWindowInsetsListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat, ViewUtils.RelativePadding relativePadding) {
            boolean z10;
            Insets f10 = windowInsetsCompat.f(WindowInsetsCompat.l.c());
            Insets f11 = windowInsetsCompat.f(WindowInsetsCompat.l.b());
            BottomSheetBehavior.this.A = f10.f2186b;
            boolean isLayoutRtl = ViewUtils.isLayoutRtl(view);
            int paddingBottom = view.getPaddingBottom();
            int paddingLeft = view.getPaddingLeft();
            int paddingRight = view.getPaddingRight();
            if (BottomSheetBehavior.this.f8414s) {
                BottomSheetBehavior.this.f8421z = windowInsetsCompat.i();
                paddingBottom = relativePadding.bottom + BottomSheetBehavior.this.f8421z;
            }
            if (BottomSheetBehavior.this.f8415t) {
                paddingLeft = (isLayoutRtl ? relativePadding.end : relativePadding.start) + f10.f2185a;
            }
            if (BottomSheetBehavior.this.f8416u) {
                paddingRight = (isLayoutRtl ? relativePadding.start : relativePadding.end) + f10.f2187c;
            }
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            boolean z11 = true;
            if (BottomSheetBehavior.this.f8418w) {
                int i10 = marginLayoutParams.leftMargin;
                int i11 = f10.f2185a;
                if (i10 != i11) {
                    marginLayoutParams.leftMargin = i11;
                    z10 = true;
                    if (BottomSheetBehavior.this.f8419x) {
                        int i12 = marginLayoutParams.rightMargin;
                        int i13 = f10.f2187c;
                        if (i12 != i13) {
                            marginLayoutParams.rightMargin = i13;
                            z10 = true;
                        }
                    }
                    if (BottomSheetBehavior.this.f8420y) {
                        int i14 = marginLayoutParams.topMargin;
                        int i15 = f10.f2186b;
                        if (i14 != i15) {
                            marginLayoutParams.topMargin = i15;
                            if (z11) {
                                view.setLayoutParams(marginLayoutParams);
                            }
                            view.setPadding(paddingLeft, view.getPaddingTop(), paddingRight, paddingBottom);
                            if (this.f8431a) {
                                BottomSheetBehavior.this.f8412q = f11.f2188d;
                            }
                            if (!BottomSheetBehavior.this.f8414s || this.f8431a) {
                                BottomSheetBehavior.this.r0(false);
                            }
                            return windowInsetsCompat;
                        }
                    }
                    z11 = z10;
                    if (z11) {
                    }
                    view.setPadding(paddingLeft, view.getPaddingTop(), paddingRight, paddingBottom);
                    if (this.f8431a) {
                    }
                    if (!BottomSheetBehavior.this.f8414s) {
                    }
                    BottomSheetBehavior.this.r0(false);
                    return windowInsetsCompat;
                }
            }
            z10 = false;
            if (BottomSheetBehavior.this.f8419x) {
            }
            if (BottomSheetBehavior.this.f8420y) {
            }
            z11 = z10;
            if (z11) {
            }
            view.setPadding(paddingLeft, view.getPaddingTop(), paddingRight, paddingBottom);
            if (this.f8431a) {
            }
            if (!BottomSheetBehavior.this.f8414s) {
            }
            BottomSheetBehavior.this.r0(false);
            return windowInsetsCompat;
        }
    }

    /* loaded from: classes.dex */
    class d extends ViewDragHelper.c {

        /* renamed from: a, reason: collision with root package name */
        private long f8433a;

        d() {
        }

        private boolean n(View view) {
            int top = view.getTop();
            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.this;
            return top > (bottomSheetBehavior.W + bottomSheetBehavior.G()) / 2;
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public int a(View view, int i10, int i11) {
            return view.getLeft();
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public int b(View view, int i10, int i11) {
            int G = BottomSheetBehavior.this.G();
            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.this;
            return q.a.b(i10, G, bottomSheetBehavior.L ? bottomSheetBehavior.W : bottomSheetBehavior.J);
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public int e(View view) {
            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.this;
            if (bottomSheetBehavior.L) {
                return bottomSheetBehavior.W;
            }
            return bottomSheetBehavior.J;
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public void j(int i10) {
            if (i10 == 1 && BottomSheetBehavior.this.N) {
                BottomSheetBehavior.this.g0(1);
            }
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public void k(View view, int i10, int i11, int i12, int i13) {
            BottomSheetBehavior.this.C(i11);
        }

        /* JADX WARN: Code restructure failed: missing block: B:12:0x0034, code lost:
        
            if (r7.f8434b.i0(r0, (r9 * 100.0f) / r10.W) != false) goto L6;
         */
        /* JADX WARN: Code restructure failed: missing block: B:15:0x003b, code lost:
        
            if (r9 > r7.f8434b.H) goto L63;
         */
        /* JADX WARN: Code restructure failed: missing block: B:30:0x008b, code lost:
        
            if (java.lang.Math.abs(r8.getTop() - r7.f8434b.G()) < java.lang.Math.abs(r8.getTop() - r7.f8434b.H)) goto L6;
         */
        /* JADX WARN: Code restructure failed: missing block: B:40:0x00c9, code lost:
        
            if (r7.f8434b.l0() == false) goto L63;
         */
        /* JADX WARN: Code restructure failed: missing block: B:44:0x00eb, code lost:
        
            if (java.lang.Math.abs(r9 - r7.f8434b.G) < java.lang.Math.abs(r9 - r7.f8434b.J)) goto L6;
         */
        /* JADX WARN: Code restructure failed: missing block: B:50:0x0107, code lost:
        
            if (r7.f8434b.l0() != false) goto L39;
         */
        /* JADX WARN: Code restructure failed: missing block: B:54:0x0121, code lost:
        
            if (r7.f8434b.l0() == false) goto L63;
         */
        @Override // androidx.customview.widget.ViewDragHelper.c
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void l(View view, float f10, float f11) {
            int i10 = 6;
            if (f11 < 0.0f) {
                if (!BottomSheetBehavior.this.f8399f) {
                    int top = view.getTop();
                    long currentTimeMillis = System.currentTimeMillis() - this.f8433a;
                    if (BottomSheetBehavior.this.l0()) {
                    }
                }
                i10 = 3;
            } else {
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.this;
                if (bottomSheetBehavior.L && bottomSheetBehavior.k0(view, f11)) {
                    if ((Math.abs(f10) >= Math.abs(f11) || f11 <= 500.0f) && !n(view)) {
                        if (!BottomSheetBehavior.this.f8399f) {
                        }
                        i10 = 3;
                    } else {
                        i10 = 5;
                    }
                } else if (f11 != 0.0f && Math.abs(f10) <= Math.abs(f11)) {
                    if (!BottomSheetBehavior.this.f8399f) {
                        int top2 = view.getTop();
                        if (Math.abs(top2 - BottomSheetBehavior.this.H) < Math.abs(top2 - BottomSheetBehavior.this.J)) {
                        }
                    }
                    i10 = 4;
                } else {
                    int top3 = view.getTop();
                    if (!BottomSheetBehavior.this.f8399f) {
                        BottomSheetBehavior bottomSheetBehavior2 = BottomSheetBehavior.this;
                        int i11 = bottomSheetBehavior2.H;
                        if (top3 < i11) {
                            if (top3 >= Math.abs(top3 - bottomSheetBehavior2.J)) {
                            }
                            i10 = 3;
                        } else {
                            if (Math.abs(top3 - i11) < Math.abs(top3 - BottomSheetBehavior.this.J)) {
                            }
                            i10 = 4;
                        }
                    }
                }
            }
            BottomSheetBehavior bottomSheetBehavior3 = BottomSheetBehavior.this;
            bottomSheetBehavior3.n0(view, i10, bottomSheetBehavior3.m0());
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public boolean m(View view, int i10) {
            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.this;
            int i11 = bottomSheetBehavior.O;
            if (i11 == 1 || bottomSheetBehavior.f8396d0) {
                return false;
            }
            if (i11 == 3 && bottomSheetBehavior.f8394b0 == i10) {
                WeakReference<View> weakReference = bottomSheetBehavior.Y;
                View view2 = weakReference != null ? weakReference.get() : null;
                if (view2 != null && view2.canScrollVertically(-1)) {
                    return false;
                }
            }
            this.f8433a = System.currentTimeMillis();
            WeakReference<V> weakReference2 = BottomSheetBehavior.this.X;
            return weakReference2 != null && weakReference2.get() == view;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class e implements AccessibilityViewCommand {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f8435a;

        e(int i10) {
            this.f8435a = i10;
        }

        @Override // androidx.core.view.accessibility.AccessibilityViewCommand
        public boolean perform(View view, AccessibilityViewCommand.a aVar) {
            BottomSheetBehavior.this.f0(this.f8435a);
            return true;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class f {
        void a(View view) {
        }

        public abstract void b(View view, float f10);

        public abstract void c(View view, int i10);
    }

    public BottomSheetBehavior() {
        this.f8397e = 0;
        this.f8399f = true;
        this.f8401g = false;
        this.f8410o = -1;
        this.f8411p = -1;
        this.D = new g(this, null);
        this.I = 0.5f;
        this.K = -1.0f;
        this.N = true;
        this.O = 4;
        this.P = 4;
        this.Z = new ArrayList<>();
        this.f8400f0 = -1;
        this.f8402g0 = new d();
    }

    private void A(Context context) {
        if (this.B == null) {
            return;
        }
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(this.B);
        this.f8408m = materialShapeDrawable;
        materialShapeDrawable.P(context);
        ColorStateList colorStateList = this.f8409n;
        if (colorStateList != null) {
            this.f8408m.a0(colorStateList);
            return;
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorBackground, typedValue, true);
        this.f8408m.setTint(typedValue.data);
    }

    private void B() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.E = ofFloat;
        ofFloat.setDuration(500L);
        this.E.addUpdateListener(new b());
    }

    public static <V extends View> BottomSheetBehavior<V> E(V v7) {
        ViewGroup.LayoutParams layoutParams = v7.getLayoutParams();
        if (layoutParams instanceof CoordinatorLayout.e) {
            CoordinatorLayout.Behavior f10 = ((CoordinatorLayout.e) layoutParams).f();
            if (f10 instanceof BottomSheetBehavior) {
                return (BottomSheetBehavior) f10;
            }
            throw new IllegalArgumentException("The view is not associated with BottomSheetBehavior");
        }
        throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
    }

    private int F(int i10, int i11, int i12, int i13) {
        int childMeasureSpec = ViewGroup.getChildMeasureSpec(i10, i11, i13);
        if (i12 == -1) {
            return childMeasureSpec;
        }
        int mode = View.MeasureSpec.getMode(childMeasureSpec);
        int size = View.MeasureSpec.getSize(childMeasureSpec);
        if (mode != 1073741824) {
            if (size != 0) {
                i12 = Math.min(size, i12);
            }
            return View.MeasureSpec.makeMeasureSpec(i12, Integer.MIN_VALUE);
        }
        return View.MeasureSpec.makeMeasureSpec(Math.min(size, i12), 1073741824);
    }

    private int J(int i10) {
        if (i10 == 3) {
            return G();
        }
        if (i10 == 4) {
            return this.J;
        }
        if (i10 == 5) {
            return this.W;
        }
        if (i10 == 6) {
            return this.H;
        }
        throw new IllegalArgumentException("Invalid state to get top offset: " + i10);
    }

    private float K() {
        VelocityTracker velocityTracker = this.f8393a0;
        if (velocityTracker == null) {
            return 0.0f;
        }
        velocityTracker.computeCurrentVelocity(1000, this.f8403h);
        return this.f8393a0.getYVelocity(this.f8394b0);
    }

    private boolean M(V v7) {
        ViewParent parent = v7.getParent();
        return parent != null && parent.isLayoutRequested() && ViewCompat.P(v7);
    }

    private void P(V v7, AccessibilityNodeInfoCompat.a aVar, int i10) {
        ViewCompat.g0(v7, aVar, null, z(i10));
    }

    private void Q() {
        this.f8394b0 = -1;
        VelocityTracker velocityTracker = this.f8393a0;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.f8393a0 = null;
        }
    }

    private void R(SavedState savedState) {
        int i10 = this.f8397e;
        if (i10 == 0) {
            return;
        }
        if (i10 == -1 || (i10 & 1) == 1) {
            this.f8404i = savedState.f8423f;
        }
        if (i10 == -1 || (i10 & 2) == 2) {
            this.f8399f = savedState.f8424g;
        }
        if (i10 == -1 || (i10 & 4) == 4) {
            this.L = savedState.f8425h;
        }
        if (i10 == -1 || (i10 & 8) == 8) {
            this.M = savedState.f8426i;
        }
    }

    private void S(V v7, Runnable runnable) {
        if (M(v7)) {
            v7.post(runnable);
        } else {
            runnable.run();
        }
    }

    private void h0(View view) {
        boolean z10 = (L() || this.f8405j) ? false : true;
        if (this.f8414s || this.f8415t || this.f8416u || this.f8418w || this.f8419x || this.f8420y || z10) {
            ViewUtils.doOnApplyWindowInsets(view, new c(z10));
        }
    }

    private boolean j0() {
        return this.Q != null && (this.N || this.O == 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void n0(View view, int i10, boolean z10) {
        int J = J(i10);
        ViewDragHelper viewDragHelper = this.Q;
        if (viewDragHelper != null && (!z10 ? !viewDragHelper.R(view, view.getLeft(), J) : !viewDragHelper.P(view.getLeft(), J))) {
            g0(2);
            p0(i10);
            this.D.c(i10);
            return;
        }
        g0(i10);
    }

    private void o0() {
        V v7;
        WeakReference<V> weakReference = this.X;
        if (weakReference == null || (v7 = weakReference.get()) == null) {
            return;
        }
        ViewCompat.e0(v7, 524288);
        ViewCompat.e0(v7, 262144);
        ViewCompat.e0(v7, 1048576);
        int i10 = this.f8400f0;
        if (i10 != -1) {
            ViewCompat.e0(v7, i10);
        }
        if (!this.f8399f && this.O != 6) {
            this.f8400f0 = u(v7, R$string.bottomsheet_action_expand_halfway, 6);
        }
        if (this.L && this.O != 5) {
            P(v7, AccessibilityNodeInfoCompat.a.f2338y, 5);
        }
        int i11 = this.O;
        if (i11 == 3) {
            P(v7, AccessibilityNodeInfoCompat.a.f2337x, this.f8399f ? 4 : 6);
            return;
        }
        if (i11 == 4) {
            P(v7, AccessibilityNodeInfoCompat.a.f2336w, this.f8399f ? 3 : 6);
        } else {
            if (i11 != 6) {
                return;
            }
            P(v7, AccessibilityNodeInfoCompat.a.f2337x, 4);
            P(v7, AccessibilityNodeInfoCompat.a.f2336w, 3);
        }
    }

    private void p0(int i10) {
        ValueAnimator valueAnimator;
        if (i10 == 2) {
            return;
        }
        boolean z10 = i10 == 3;
        if (this.C != z10) {
            this.C = z10;
            if (this.f8408m == null || (valueAnimator = this.E) == null) {
                return;
            }
            if (valueAnimator.isRunning()) {
                this.E.reverse();
                return;
            }
            float f10 = z10 ? 0.0f : 1.0f;
            this.E.setFloatValues(1.0f - f10, f10);
            this.E.start();
        }
    }

    private void q0(boolean z10) {
        Map<View, Integer> map;
        WeakReference<V> weakReference = this.X;
        if (weakReference == null) {
            return;
        }
        ViewParent parent = weakReference.get().getParent();
        if (parent instanceof CoordinatorLayout) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) parent;
            int childCount = coordinatorLayout.getChildCount();
            if (z10) {
                if (this.f8398e0 != null) {
                    return;
                } else {
                    this.f8398e0 = new HashMap(childCount);
                }
            }
            for (int i10 = 0; i10 < childCount; i10++) {
                View childAt = coordinatorLayout.getChildAt(i10);
                if (childAt != this.X.get()) {
                    if (z10) {
                        this.f8398e0.put(childAt, Integer.valueOf(childAt.getImportantForAccessibility()));
                        if (this.f8401g) {
                            ViewCompat.w0(childAt, 4);
                        }
                    } else if (this.f8401g && (map = this.f8398e0) != null && map.containsKey(childAt)) {
                        ViewCompat.w0(childAt, this.f8398e0.get(childAt).intValue());
                    }
                }
            }
            if (!z10) {
                this.f8398e0 = null;
            } else if (this.f8401g) {
                this.X.get().sendAccessibilityEvent(8);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void r0(boolean z10) {
        V v7;
        if (this.X != null) {
            w();
            if (this.O != 4 || (v7 = this.X.get()) == null) {
                return;
            }
            if (z10) {
                f0(4);
            } else {
                v7.requestLayout();
            }
        }
    }

    private int u(V v7, int i10, int i11) {
        return ViewCompat.b(v7, v7.getResources().getString(i10), z(i11));
    }

    private void w() {
        int y4 = y();
        if (this.f8399f) {
            this.J = Math.max(this.W - y4, this.G);
        } else {
            this.J = this.W - y4;
        }
    }

    private void x() {
        this.H = (int) (this.W * (1.0f - this.I));
    }

    private int y() {
        int i10;
        int i11;
        int i12;
        if (this.f8405j) {
            i10 = Math.min(Math.max(this.f8406k, this.W - ((this.V * 9) / 16)), this.U);
            i11 = this.f8421z;
        } else {
            if (!this.f8413r && !this.f8414s && (i12 = this.f8412q) > 0) {
                return Math.max(this.f8404i, i12 + this.f8407l);
            }
            i10 = this.f8404i;
            i11 = this.f8421z;
        }
        return i10 + i11;
    }

    private AccessibilityViewCommand z(int i10) {
        return new e(i10);
    }

    void C(int i10) {
        float f10;
        float f11;
        V v7 = this.X.get();
        if (v7 == null || this.Z.isEmpty()) {
            return;
        }
        int i11 = this.J;
        if (i10 <= i11 && i11 != G()) {
            int i12 = this.J;
            f10 = i12 - i10;
            f11 = i12 - G();
        } else {
            int i13 = this.J;
            f10 = i13 - i10;
            f11 = this.W - i13;
        }
        float f12 = f10 / f11;
        for (int i14 = 0; i14 < this.Z.size(); i14++) {
            this.Z.get(i14).b(v7, f12);
        }
    }

    View D(View view) {
        if (ViewCompat.R(view)) {
            return view;
        }
        if (!(view instanceof ViewGroup)) {
            return null;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            View D = D(viewGroup.getChildAt(i10));
            if (D != null) {
                return D;
            }
        }
        return null;
    }

    public int G() {
        if (this.f8399f) {
            return this.G;
        }
        return Math.max(this.F, this.f8417v ? 0 : this.A);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MaterialShapeDrawable H() {
        return this.f8408m;
    }

    public int I() {
        return this.O;
    }

    public boolean L() {
        return this.f8413r;
    }

    public boolean N() {
        return true;
    }

    public void O(f fVar) {
        this.Z.remove(fVar);
    }

    public void T(boolean z10) {
        this.N = z10;
    }

    public void U(int i10) {
        if (i10 >= 0) {
            this.F = i10;
            return;
        }
        throw new IllegalArgumentException("offset must be greater than or equal to 0");
    }

    public void V(boolean z10) {
        if (this.f8399f == z10) {
            return;
        }
        this.f8399f = z10;
        if (this.X != null) {
            w();
        }
        g0((this.f8399f && this.O == 6) ? 3 : this.O);
        o0();
    }

    public void W(boolean z10) {
        this.f8413r = z10;
    }

    public void X(float f10) {
        if (f10 > 0.0f && f10 < 1.0f) {
            this.I = f10;
            if (this.X != null) {
                x();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("ratio must be a float value between 0 and 1");
    }

    public void Y(boolean z10) {
        if (this.L != z10) {
            this.L = z10;
            if (!z10 && this.O == 5) {
                f0(4);
            }
            o0();
        }
    }

    public void Z(int i10) {
        this.f8411p = i10;
    }

    public void a0(int i10) {
        this.f8410o = i10;
    }

    public void b0(int i10) {
        c0(i10, false);
    }

    public final void c0(int i10, boolean z10) {
        boolean z11 = true;
        if (i10 == -1) {
            if (!this.f8405j) {
                this.f8405j = true;
            }
            z11 = false;
        } else {
            if (this.f8405j || this.f8404i != i10) {
                this.f8405j = false;
                this.f8404i = Math.max(0, i10);
            }
            z11 = false;
        }
        if (z11) {
            r0(z10);
        }
    }

    public void d0(int i10) {
        this.f8397e = i10;
    }

    public void e0(boolean z10) {
        this.M = z10;
    }

    public void f0(int i10) {
        if (i10 != 1 && i10 != 2) {
            if (!this.L && i10 == 5) {
                Log.w("BottomSheetBehavior", "Cannot set state: " + i10);
                return;
            }
            int i11 = (i10 == 6 && this.f8399f && J(i10) <= this.G) ? 3 : i10;
            WeakReference<V> weakReference = this.X;
            if (weakReference != null && weakReference.get() != null) {
                V v7 = this.X.get();
                S(v7, new a(v7, i11));
                return;
            } else {
                g0(i10);
                return;
            }
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("STATE_");
        sb2.append(i10 == 1 ? "DRAGGING" : "SETTLING");
        sb2.append(" should not be set externally.");
        throw new IllegalArgumentException(sb2.toString());
    }

    void g0(int i10) {
        V v7;
        if (this.O == i10) {
            return;
        }
        this.O = i10;
        if (i10 == 4 || i10 == 3 || i10 == 6 || (this.L && i10 == 5)) {
            this.P = i10;
        }
        WeakReference<V> weakReference = this.X;
        if (weakReference == null || (v7 = weakReference.get()) == null) {
            return;
        }
        if (i10 == 3) {
            q0(true);
        } else if (i10 == 6 || i10 == 5 || i10 == 4) {
            q0(false);
        }
        p0(i10);
        for (int i11 = 0; i11 < this.Z.size(); i11++) {
            this.Z.get(i11).c(v7, i10);
        }
        o0();
    }

    public boolean i0(long j10, float f10) {
        return false;
    }

    boolean k0(View view, float f10) {
        if (this.M) {
            return true;
        }
        if (view.getTop() < this.J) {
            return false;
        }
        return Math.abs((((float) view.getTop()) + (f10 * 0.1f)) - ((float) this.J)) / ((float) y()) > 0.5f;
    }

    public boolean l0() {
        return false;
    }

    public boolean m0() {
        return true;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onAttachedToLayoutParams(CoordinatorLayout.e eVar) {
        super.onAttachedToLayoutParams(eVar);
        this.X = null;
        this.Q = null;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams();
        this.X = null;
        this.Q = null;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, V v7, MotionEvent motionEvent) {
        ViewDragHelper viewDragHelper;
        if (v7.isShown() && this.N) {
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 0) {
                Q();
            }
            if (this.f8393a0 == null) {
                this.f8393a0 = VelocityTracker.obtain();
            }
            this.f8393a0.addMovement(motionEvent);
            if (actionMasked == 0) {
                int x10 = (int) motionEvent.getX();
                this.f8395c0 = (int) motionEvent.getY();
                if (this.O != 2) {
                    WeakReference<View> weakReference = this.Y;
                    View view = weakReference != null ? weakReference.get() : null;
                    if (view != null && coordinatorLayout.F(view, x10, this.f8395c0)) {
                        this.f8394b0 = motionEvent.getPointerId(motionEvent.getActionIndex());
                        this.f8396d0 = true;
                    }
                }
                this.R = this.f8394b0 == -1 && !coordinatorLayout.F(v7, x10, this.f8395c0);
            } else if (actionMasked == 1 || actionMasked == 3) {
                this.f8396d0 = false;
                this.f8394b0 = -1;
                if (this.R) {
                    this.R = false;
                    return false;
                }
            }
            if (!this.R && (viewDragHelper = this.Q) != null && viewDragHelper.Q(motionEvent)) {
                return true;
            }
            WeakReference<View> weakReference2 = this.Y;
            View view2 = weakReference2 != null ? weakReference2.get() : null;
            return (actionMasked != 2 || view2 == null || this.R || this.O == 1 || coordinatorLayout.F(view2, (int) motionEvent.getX(), (int) motionEvent.getY()) || this.Q == null || Math.abs(((float) this.f8395c0) - motionEvent.getY()) <= ((float) this.Q.A())) ? false : true;
        }
        this.R = true;
        return false;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, V v7, int i10) {
        if (ViewCompat.u(coordinatorLayout) && !ViewCompat.u(v7)) {
            v7.setFitsSystemWindows(true);
        }
        if (this.X == null) {
            this.f8406k = coordinatorLayout.getResources().getDimensionPixelSize(R$dimen.design_bottom_sheet_peek_height_min);
            h0(v7);
            this.X = new WeakReference<>(v7);
            MaterialShapeDrawable materialShapeDrawable = this.f8408m;
            if (materialShapeDrawable != null) {
                ViewCompat.p0(v7, materialShapeDrawable);
                MaterialShapeDrawable materialShapeDrawable2 = this.f8408m;
                float f10 = this.K;
                if (f10 == -1.0f) {
                    f10 = ViewCompat.t(v7);
                }
                materialShapeDrawable2.Z(f10);
                boolean z10 = this.O == 3;
                this.C = z10;
                this.f8408m.b0(z10 ? 0.0f : 1.0f);
            } else {
                ColorStateList colorStateList = this.f8409n;
                if (colorStateList != null) {
                    ViewCompat.q0(v7, colorStateList);
                }
            }
            o0();
            if (ViewCompat.v(v7) == 0) {
                ViewCompat.w0(v7, 1);
            }
        }
        if (this.Q == null) {
            this.Q = ViewDragHelper.p(coordinatorLayout, this.f8402g0);
        }
        int top = v7.getTop();
        coordinatorLayout.M(v7, i10);
        this.V = coordinatorLayout.getWidth();
        this.W = coordinatorLayout.getHeight();
        int height = v7.getHeight();
        this.U = height;
        int i11 = this.W;
        int i12 = i11 - height;
        int i13 = this.A;
        if (i12 < i13) {
            if (this.f8417v) {
                this.U = i11;
            } else {
                this.U = i11 - i13;
            }
        }
        this.G = Math.max(0, i11 - this.U);
        x();
        w();
        int i14 = this.O;
        if (i14 == 3) {
            ViewCompat.W(v7, G());
        } else if (i14 == 6) {
            ViewCompat.W(v7, this.H);
        } else if (this.L && i14 == 5) {
            ViewCompat.W(v7, this.W);
        } else if (i14 == 4) {
            ViewCompat.W(v7, this.J);
        } else if (i14 == 1 || i14 == 2) {
            ViewCompat.W(v7, top - v7.getTop());
        }
        this.Y = new WeakReference<>(D(v7));
        for (int i15 = 0; i15 < this.Z.size(); i15++) {
            this.Z.get(i15).a(v7);
        }
        return true;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onMeasureChild(CoordinatorLayout coordinatorLayout, V v7, int i10, int i11, int i12, int i13) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) v7.getLayoutParams();
        v7.measure(F(i10, coordinatorLayout.getPaddingLeft() + coordinatorLayout.getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + i11, this.f8410o, marginLayoutParams.width), F(i12, coordinatorLayout.getPaddingTop() + coordinatorLayout.getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + i13, this.f8411p, marginLayoutParams.height));
        return true;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, V v7, View view, float f10, float f11) {
        WeakReference<View> weakReference;
        if (N() && (weakReference = this.Y) != null && view == weakReference.get()) {
            return this.O != 3 || super.onNestedPreFling(coordinatorLayout, v7, view, f10, f11);
        }
        return false;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, V v7, View view, int i10, int i11, int[] iArr, int i12) {
        if (i12 == 1) {
            return;
        }
        WeakReference<View> weakReference = this.Y;
        View view2 = weakReference != null ? weakReference.get() : null;
        if (!N() || view == view2) {
            int top = v7.getTop();
            int i13 = top - i11;
            if (i11 > 0) {
                if (i13 < G()) {
                    iArr[1] = top - G();
                    ViewCompat.W(v7, -iArr[1]);
                    g0(3);
                } else {
                    if (!this.N) {
                        return;
                    }
                    iArr[1] = i11;
                    ViewCompat.W(v7, -i11);
                    g0(1);
                }
            } else if (i11 < 0 && !view.canScrollVertically(-1)) {
                int i14 = this.J;
                if (i13 > i14 && !this.L) {
                    iArr[1] = top - i14;
                    ViewCompat.W(v7, -iArr[1]);
                    g0(4);
                } else {
                    if (!this.N) {
                        return;
                    }
                    iArr[1] = i11;
                    ViewCompat.W(v7, -i11);
                    g0(1);
                }
            }
            C(v7.getTop());
            this.S = i11;
            this.T = true;
        }
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, V v7, View view, int i10, int i11, int i12, int i13, int i14, int[] iArr) {
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onRestoreInstanceState(CoordinatorLayout coordinatorLayout, V v7, Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(coordinatorLayout, v7, savedState.getSuperState());
        R(savedState);
        int i10 = savedState.f8422e;
        if (i10 != 1 && i10 != 2) {
            this.O = i10;
            this.P = i10;
        } else {
            this.O = 4;
            this.P = 4;
        }
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public Parcelable onSaveInstanceState(CoordinatorLayout coordinatorLayout, V v7) {
        return new SavedState(super.onSaveInstanceState(coordinatorLayout, v7), (BottomSheetBehavior<?>) this);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V v7, View view, View view2, int i10, int i11) {
        this.S = 0;
        this.T = false;
        return (i10 & 2) != 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0036, code lost:
    
        if (r4.getTop() <= r2.H) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00a9, code lost:
    
        r0 = 6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0065, code lost:
    
        if (java.lang.Math.abs(r3 - r2.G) < java.lang.Math.abs(r3 - r2.J)) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x007b, code lost:
    
        if (l0() != false) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x008b, code lost:
    
        if (java.lang.Math.abs(r3 - r1) < java.lang.Math.abs(r3 - r2.J)) goto L51;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00a7, code lost:
    
        if (java.lang.Math.abs(r3 - r2.H) < java.lang.Math.abs(r3 - r2.J)) goto L51;
     */
    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, V v7, View view, int i10) {
        WeakReference<View> weakReference;
        int i11 = 3;
        if (v7.getTop() == G()) {
            g0(3);
            return;
        }
        if (!N() || ((weakReference = this.Y) != null && view == weakReference.get() && this.T)) {
            if (this.S > 0) {
                if (!this.f8399f) {
                }
                n0(v7, i11, false);
                this.T = false;
            }
            if (this.L && k0(v7, K())) {
                i11 = 5;
            } else if (this.S == 0) {
                int top = v7.getTop();
                if (!this.f8399f) {
                    int i12 = this.H;
                    if (top < i12) {
                        if (top >= Math.abs(top - this.J)) {
                        }
                    }
                }
            } else {
                if (!this.f8399f) {
                    int top2 = v7.getTop();
                }
                i11 = 4;
            }
            n0(v7, i11, false);
            this.T = false;
        }
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onTouchEvent(CoordinatorLayout coordinatorLayout, V v7, MotionEvent motionEvent) {
        if (!v7.isShown()) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (this.O == 1 && actionMasked == 0) {
            return true;
        }
        if (j0()) {
            this.Q.G(motionEvent);
        }
        if (actionMasked == 0) {
            Q();
        }
        if (this.f8393a0 == null) {
            this.f8393a0 = VelocityTracker.obtain();
        }
        this.f8393a0.addMovement(motionEvent);
        if (j0() && actionMasked == 2 && !this.R && Math.abs(this.f8395c0 - motionEvent.getY()) > this.Q.A()) {
            this.Q.c(v7, motionEvent.getPointerId(motionEvent.getActionIndex()));
        }
        return !this.R;
    }

    public void v(f fVar) {
        if (this.Z.contains(fVar)) {
            return;
        }
        this.Z.add(fVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class g {

        /* renamed from: a, reason: collision with root package name */
        private int f8437a;

        /* renamed from: b, reason: collision with root package name */
        private boolean f8438b;

        /* renamed from: c, reason: collision with root package name */
        private final Runnable f8439c;

        /* loaded from: classes.dex */
        class a implements Runnable {
            a() {
            }

            @Override // java.lang.Runnable
            public void run() {
                g.this.f8438b = false;
                ViewDragHelper viewDragHelper = BottomSheetBehavior.this.Q;
                if (viewDragHelper != null && viewDragHelper.n(true)) {
                    g gVar = g.this;
                    gVar.c(gVar.f8437a);
                    return;
                }
                g gVar2 = g.this;
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.this;
                if (bottomSheetBehavior.O == 2) {
                    bottomSheetBehavior.g0(gVar2.f8437a);
                }
            }
        }

        private g() {
            this.f8439c = new a();
        }

        void c(int i10) {
            WeakReference<V> weakReference = BottomSheetBehavior.this.X;
            if (weakReference == null || weakReference.get() == null) {
                return;
            }
            this.f8437a = i10;
            if (this.f8438b) {
                return;
            }
            ViewCompat.c0(BottomSheetBehavior.this.X.get(), this.f8439c);
            this.f8438b = true;
        }

        /* synthetic */ g(BottomSheetBehavior bottomSheetBehavior, a aVar) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        final int f8422e;

        /* renamed from: f, reason: collision with root package name */
        int f8423f;

        /* renamed from: g, reason: collision with root package name */
        boolean f8424g;

        /* renamed from: h, reason: collision with root package name */
        boolean f8425h;

        /* renamed from: i, reason: collision with root package name */
        boolean f8426i;

        /* loaded from: classes.dex */
        class a implements Parcelable.ClassLoaderCreator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, (ClassLoader) null);
            }

            @Override // android.os.Parcelable.ClassLoaderCreator
            /* renamed from: b, reason: merged with bridge method [inline-methods] */
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: c, reason: merged with bridge method [inline-methods] */
            public SavedState[] newArray(int i10) {
                return new SavedState[i10];
            }
        }

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.f8422e = parcel.readInt();
            this.f8423f = parcel.readInt();
            this.f8424g = parcel.readInt() == 1;
            this.f8425h = parcel.readInt() == 1;
            this.f8426i = parcel.readInt() == 1;
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeInt(this.f8422e);
            parcel.writeInt(this.f8423f);
            parcel.writeInt(this.f8424g ? 1 : 0);
            parcel.writeInt(this.f8425h ? 1 : 0);
            parcel.writeInt(this.f8426i ? 1 : 0);
        }

        public SavedState(Parcelable parcelable, BottomSheetBehavior<?> bottomSheetBehavior) {
            super(parcelable);
            this.f8422e = bottomSheetBehavior.O;
            this.f8423f = ((BottomSheetBehavior) bottomSheetBehavior).f8404i;
            this.f8424g = ((BottomSheetBehavior) bottomSheetBehavior).f8399f;
            this.f8425h = bottomSheetBehavior.L;
            this.f8426i = ((BottomSheetBehavior) bottomSheetBehavior).M;
        }
    }

    public BottomSheetBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        int i10;
        this.f8397e = 0;
        this.f8399f = true;
        this.f8401g = false;
        this.f8410o = -1;
        this.f8411p = -1;
        this.D = new g(this, null);
        this.I = 0.5f;
        this.K = -1.0f;
        this.N = true;
        this.O = 4;
        this.P = 4;
        this.Z = new ArrayList<>();
        this.f8400f0 = -1;
        this.f8402g0 = new d();
        this.f8407l = context.getResources().getDimensionPixelSize(R$dimen.mtrl_min_touch_target_size);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.BottomSheetBehavior_Layout);
        int i11 = R$styleable.BottomSheetBehavior_Layout_backgroundTint;
        if (obtainStyledAttributes.hasValue(i11)) {
            this.f8409n = MaterialResources.a(context, obtainStyledAttributes, i11);
        }
        if (obtainStyledAttributes.hasValue(R$styleable.BottomSheetBehavior_Layout_shapeAppearance)) {
            this.B = ShapeAppearanceModel.e(context, attributeSet, R$attr.bottomSheetStyle, f8392h0).m();
        }
        A(context);
        B();
        this.K = obtainStyledAttributes.getDimension(R$styleable.BottomSheetBehavior_Layout_android_elevation, -1.0f);
        int i12 = R$styleable.BottomSheetBehavior_Layout_android_maxWidth;
        if (obtainStyledAttributes.hasValue(i12)) {
            a0(obtainStyledAttributes.getDimensionPixelSize(i12, -1));
        }
        int i13 = R$styleable.BottomSheetBehavior_Layout_android_maxHeight;
        if (obtainStyledAttributes.hasValue(i13)) {
            Z(obtainStyledAttributes.getDimensionPixelSize(i13, -1));
        }
        int i14 = R$styleable.BottomSheetBehavior_Layout_behavior_peekHeight;
        TypedValue peekValue = obtainStyledAttributes.peekValue(i14);
        if (peekValue != null && (i10 = peekValue.data) == -1) {
            b0(i10);
        } else {
            b0(obtainStyledAttributes.getDimensionPixelSize(i14, -1));
        }
        Y(obtainStyledAttributes.getBoolean(R$styleable.BottomSheetBehavior_Layout_behavior_hideable, false));
        W(obtainStyledAttributes.getBoolean(R$styleable.BottomSheetBehavior_Layout_gestureInsetBottomIgnored, false));
        V(obtainStyledAttributes.getBoolean(R$styleable.BottomSheetBehavior_Layout_behavior_fitToContents, true));
        e0(obtainStyledAttributes.getBoolean(R$styleable.BottomSheetBehavior_Layout_behavior_skipCollapsed, false));
        T(obtainStyledAttributes.getBoolean(R$styleable.BottomSheetBehavior_Layout_behavior_draggable, true));
        d0(obtainStyledAttributes.getInt(R$styleable.BottomSheetBehavior_Layout_behavior_saveFlags, 0));
        X(obtainStyledAttributes.getFloat(R$styleable.BottomSheetBehavior_Layout_behavior_halfExpandedRatio, 0.5f));
        int i15 = R$styleable.BottomSheetBehavior_Layout_behavior_expandedOffset;
        TypedValue peekValue2 = obtainStyledAttributes.peekValue(i15);
        if (peekValue2 != null && peekValue2.type == 16) {
            U(peekValue2.data);
        } else {
            U(obtainStyledAttributes.getDimensionPixelOffset(i15, 0));
        }
        this.f8414s = obtainStyledAttributes.getBoolean(R$styleable.BottomSheetBehavior_Layout_paddingBottomSystemWindowInsets, false);
        this.f8415t = obtainStyledAttributes.getBoolean(R$styleable.BottomSheetBehavior_Layout_paddingLeftSystemWindowInsets, false);
        this.f8416u = obtainStyledAttributes.getBoolean(R$styleable.BottomSheetBehavior_Layout_paddingRightSystemWindowInsets, false);
        this.f8417v = obtainStyledAttributes.getBoolean(R$styleable.BottomSheetBehavior_Layout_paddingTopSystemWindowInsets, true);
        this.f8418w = obtainStyledAttributes.getBoolean(R$styleable.BottomSheetBehavior_Layout_marginLeftSystemWindowInsets, false);
        this.f8419x = obtainStyledAttributes.getBoolean(R$styleable.BottomSheetBehavior_Layout_marginRightSystemWindowInsets, false);
        this.f8420y = obtainStyledAttributes.getBoolean(R$styleable.BottomSheetBehavior_Layout_marginTopSystemWindowInsets, false);
        obtainStyledAttributes.recycle();
        this.f8403h = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
    }
}
