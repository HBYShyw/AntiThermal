package com.coui.appcompat.panel;

import android.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
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
import android.view.WindowInsets;
import android.view.animation.PathInterpolator;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.customview.view.AbsSavedState;
import androidx.customview.widget.ViewDragHelper;
import b2.COUILog;
import c4.MaterialShapeDrawable;
import c4.ShapeAppearanceModel;
import com.google.android.material.R$attr;
import com.google.android.material.R$style;
import com.google.android.material.R$styleable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.support.panel.R$dimen;
import com.support.panel.R$id;
import f2.COUIPanelDragListener;
import f2.COUIPanelPullUpListener;
import h3.UIUtil;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import m1.COUIOutEaseInterpolator;
import r7.AnimationListener;
import r7.AnimationUpdateListener;
import r7.DragBehavior;
import r7.PhysicalAnimator;
import s.COUIPanelDragToHiddenAnimation;
import s.DynamicAnimation;
import s.FloatPropertyCompat;
import z3.MaterialResources;

/* loaded from: classes.dex */
public class COUIBottomSheetBehavior<V extends View> extends BottomSheetBehavior<V> implements AnimationListener, AnimationUpdateListener {

    /* renamed from: p1, reason: collision with root package name */
    private static boolean f6493p1;

    /* renamed from: q1, reason: collision with root package name */
    private static final int f6494q1;
    float A0;
    int B0;
    float C0;
    boolean D0;
    private boolean E0;
    private boolean F0;
    int G0;
    ViewDragHelper H0;
    private boolean I0;
    private int J0;
    private boolean K0;
    int L0;
    int M0;
    int N0;
    WeakReference<V> O0;
    WeakReference<View> P0;
    private final ArrayList<i> Q0;
    private VelocityTracker R0;
    int S0;
    private int T0;
    private int U0;
    boolean V0;
    private Map<View, Integer> W0;
    COUIPanelDragListener X0;
    private COUIPanelPullUpListener Y0;
    private boolean Z0;

    /* renamed from: a1, reason: collision with root package name */
    private boolean f6495a1;

    /* renamed from: b1, reason: collision with root package name */
    private int f6496b1;

    /* renamed from: c1, reason: collision with root package name */
    private float f6497c1;

    /* renamed from: d1, reason: collision with root package name */
    private PhysicalAnimator f6498d1;

    /* renamed from: e1, reason: collision with root package name */
    private DragBehavior f6499e1;

    /* renamed from: f1, reason: collision with root package name */
    private r7.j f6500f1;

    /* renamed from: g1, reason: collision with root package name */
    private float f6501g1;

    /* renamed from: h1, reason: collision with root package name */
    private float f6502h1;

    /* renamed from: i0, reason: collision with root package name */
    private int f6503i0;

    /* renamed from: i1, reason: collision with root package name */
    private float f6504i1;

    /* renamed from: j0, reason: collision with root package name */
    private Context f6505j0;

    /* renamed from: j1, reason: collision with root package name */
    private boolean f6506j1;

    /* renamed from: k0, reason: collision with root package name */
    private boolean f6507k0;

    /* renamed from: k1, reason: collision with root package name */
    private View f6508k1;

    /* renamed from: l0, reason: collision with root package name */
    private boolean f6509l0;

    /* renamed from: l1, reason: collision with root package name */
    private boolean f6510l1;

    /* renamed from: m0, reason: collision with root package name */
    private float f6511m0;

    /* renamed from: m1, reason: collision with root package name */
    private Rect f6512m1;

    /* renamed from: n0, reason: collision with root package name */
    private int f6513n0;

    /* renamed from: n1, reason: collision with root package name */
    private boolean f6514n1;

    /* renamed from: o0, reason: collision with root package name */
    private boolean f6515o0;

    /* renamed from: o1, reason: collision with root package name */
    private final ViewDragHelper.c f6516o1;

    /* renamed from: p0, reason: collision with root package name */
    private int f6517p0;

    /* renamed from: q0, reason: collision with root package name */
    private boolean f6518q0;

    /* renamed from: r0, reason: collision with root package name */
    private MaterialShapeDrawable f6519r0;

    /* renamed from: s0, reason: collision with root package name */
    private boolean f6520s0;

    /* renamed from: t0, reason: collision with root package name */
    private ShapeAppearanceModel f6521t0;

    /* renamed from: u0, reason: collision with root package name */
    private boolean f6522u0;

    /* renamed from: v0, reason: collision with root package name */
    private COUIBottomSheetBehavior<V>.j f6523v0;

    /* renamed from: w0, reason: collision with root package name */
    private ValueAnimator f6524w0;

    /* renamed from: x0, reason: collision with root package name */
    int f6525x0;

    /* renamed from: y0, reason: collision with root package name */
    int f6526y0;

    /* renamed from: z0, reason: collision with root package name */
    int f6527z0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ View f6533e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ int f6534f;

        a(View view, int i10) {
            this.f6533e = view;
            this.f6534f = i10;
        }

        @Override // java.lang.Runnable
        public void run() {
            COUIBottomSheetBehavior.this.c1(this.f6533e, this.f6534f);
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
            if (COUIBottomSheetBehavior.this.f6519r0 != null) {
                COUIBottomSheetBehavior.this.f6519r0.b0(floatValue);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ View f6537a;

        c(View view) {
            this.f6537a = view;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int floatValue = (int) ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.f6537a.offsetTopAndBottom(floatValue - COUIBottomSheetBehavior.this.f6496b1);
            COUIBottomSheetBehavior.this.C(this.f6537a.getTop());
            COUIBottomSheetBehavior.this.f6496b1 = floatValue;
            if (COUIBottomSheetBehavior.this.Y0 != null) {
                COUIBottomSheetBehavior.this.M0(this.f6537a);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d extends AnimatorListenerAdapter {
        d() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            COUIBottomSheetBehavior.this.g0(5);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class e extends FloatPropertyCompat {

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ View f6540b;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        e(String str, View view) {
            super(str);
            this.f6540b = view;
        }

        @Override // s.FloatPropertyCompat
        public float a(Object obj) {
            COUIBottomSheetBehavior.this.f6496b1 = 0;
            return COUIBottomSheetBehavior.this.f6496b1;
        }

        @Override // s.FloatPropertyCompat
        public void b(Object obj, float f10) {
            int i10 = (int) f10;
            ((View) obj).offsetTopAndBottom(i10 - COUIBottomSheetBehavior.this.f6496b1);
            COUIBottomSheetBehavior.this.C(this.f6540b.getTop());
            COUIBottomSheetBehavior.this.f6496b1 = i10;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class f implements DynamicAnimation.q {
        f() {
        }

        @Override // s.DynamicAnimation.q
        public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z10, float f10, float f11) {
            COUIBottomSheetBehavior.this.g0(5);
        }
    }

    /* loaded from: classes.dex */
    class g extends ViewDragHelper.c {
        g() {
        }

        private boolean n(View view) {
            int top = view.getTop();
            COUIBottomSheetBehavior cOUIBottomSheetBehavior = COUIBottomSheetBehavior.this;
            return top > (cOUIBottomSheetBehavior.M0 + cOUIBottomSheetBehavior.G()) / 2;
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public int a(View view, int i10, int i11) {
            return view.getLeft();
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public int b(View view, int i10, int i11) {
            if (COUIBottomSheetBehavior.this.Y0 != null) {
                COUIBottomSheetBehavior.this.Y0.d();
            }
            int i12 = 0;
            if (COUIBottomSheetBehavior.this.G0 == 1) {
                if (view.getTop() <= COUIBottomSheetBehavior.this.G()) {
                    if (COUIBottomSheetBehavior.this.f6506j1 && COUIBottomSheetBehavior.this.f6499e1.S()) {
                        COUIBottomSheetBehavior.this.f6499e1.O(0.0f);
                        COUIBottomSheetBehavior.this.f6508k1 = null;
                    }
                    if (COUIBottomSheetBehavior.this.Y0 != null && COUIBottomSheetBehavior.this.G() > 0) {
                        COUIBottomSheetBehavior.this.Z0 = true;
                        i12 = COUIBottomSheetBehavior.this.Y0.c(i11, COUIBottomSheetBehavior.this.G());
                    }
                } else {
                    int top = view.getTop();
                    if (COUIBottomSheetBehavior.this.f6506j1) {
                        COUIBottomSheetBehavior.this.P0(view, top + i11);
                    } else if (COUIBottomSheetBehavior.this.K() > 10000.0f) {
                        i10 = ((int) ((i11 * 0.5f) + 0.5f)) + top;
                    }
                }
            }
            COUIBottomSheetBehavior.this.M0(view);
            int G = COUIBottomSheetBehavior.this.G() - i12;
            COUIBottomSheetBehavior cOUIBottomSheetBehavior = COUIBottomSheetBehavior.this;
            return q.a.b(i10, G, cOUIBottomSheetBehavior.D0 ? cOUIBottomSheetBehavior.M0 : cOUIBottomSheetBehavior.B0);
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public int e(View view) {
            COUIBottomSheetBehavior cOUIBottomSheetBehavior = COUIBottomSheetBehavior.this;
            if (cOUIBottomSheetBehavior.D0) {
                return cOUIBottomSheetBehavior.M0;
            }
            return cOUIBottomSheetBehavior.B0;
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public void j(int i10) {
            if (i10 == 1 && COUIBottomSheetBehavior.this.F0) {
                COUIBottomSheetBehavior.this.g0(1);
            }
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public void k(View view, int i10, int i11, int i12, int i13) {
            COUIBottomSheetBehavior.this.C(i11);
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public void l(View view, float f10, float f11) {
            int i10;
            if (COUIBottomSheetBehavior.this.f6506j1 && COUIBottomSheetBehavior.this.f6499e1.S()) {
                COUIBottomSheetBehavior.this.f6499e1.O(0.0f);
                COUIBottomSheetBehavior.this.f6508k1 = null;
            }
            COUIBottomSheetBehavior.this.Z0 = false;
            if (COUIBottomSheetBehavior.this.Y0 != null) {
                COUIBottomSheetBehavior.this.Y0.e();
                float ratio = view instanceof COUIPanelPercentFrameLayout ? ((COUIPanelPercentFrameLayout) view).getRatio() : 1.0f;
                COUIBottomSheetBehavior cOUIBottomSheetBehavior = COUIBottomSheetBehavior.this;
                if (((int) (((cOUIBottomSheetBehavior.M0 - cOUIBottomSheetBehavior.Q0(view)) / ratio) - (view.getHeight() / ratio))) <= COUIBottomSheetBehavior.this.G() && view.getTop() < COUIBottomSheetBehavior.this.G()) {
                    COUIBottomSheetBehavior.this.Y0.b(COUIBottomSheetBehavior.this.G());
                    return;
                }
            }
            int i11 = 5;
            if (f11 < 0.0f) {
                if (COUIBottomSheetBehavior.this.f6507k0) {
                    i10 = COUIBottomSheetBehavior.this.f6526y0;
                } else {
                    int top = view.getTop();
                    COUIBottomSheetBehavior cOUIBottomSheetBehavior2 = COUIBottomSheetBehavior.this;
                    int i12 = cOUIBottomSheetBehavior2.f6527z0;
                    if (top > i12) {
                        i10 = i12;
                        i11 = 6;
                    } else {
                        i10 = cOUIBottomSheetBehavior2.f6525x0;
                    }
                }
                i11 = 3;
            } else {
                COUIBottomSheetBehavior cOUIBottomSheetBehavior3 = COUIBottomSheetBehavior.this;
                if (cOUIBottomSheetBehavior3.D0 && cOUIBottomSheetBehavior3.k0(view, f11)) {
                    COUIPanelDragListener cOUIPanelDragListener = COUIBottomSheetBehavior.this.X0;
                    if (cOUIPanelDragListener != null && cOUIPanelDragListener.a()) {
                        COUIBottomSheetBehavior cOUIBottomSheetBehavior4 = COUIBottomSheetBehavior.this;
                        int i13 = cOUIBottomSheetBehavior4.f6526y0;
                        cOUIBottomSheetBehavior4.f6495a1 = false;
                        i10 = i13;
                    } else if ((Math.abs(f10) < Math.abs(f11) && f11 > 500.0f) || n(view)) {
                        COUIBottomSheetBehavior cOUIBottomSheetBehavior5 = COUIBottomSheetBehavior.this;
                        int i14 = cOUIBottomSheetBehavior5.N0;
                        cOUIBottomSheetBehavior5.f6495a1 = true;
                        i10 = i14;
                    } else if (COUIBottomSheetBehavior.this.f6507k0) {
                        i10 = COUIBottomSheetBehavior.this.f6526y0;
                    } else if (Math.abs(view.getTop() - COUIBottomSheetBehavior.this.f6525x0) < Math.abs(view.getTop() - COUIBottomSheetBehavior.this.f6527z0)) {
                        i10 = COUIBottomSheetBehavior.this.f6525x0;
                    } else {
                        i10 = COUIBottomSheetBehavior.this.f6527z0;
                        i11 = 6;
                    }
                    i11 = 3;
                } else if (f11 != 0.0f && Math.abs(f10) <= Math.abs(f11)) {
                    if (COUIBottomSheetBehavior.this.f6507k0) {
                        COUIBottomSheetBehavior cOUIBottomSheetBehavior6 = COUIBottomSheetBehavior.this;
                        COUIPanelDragListener cOUIPanelDragListener2 = cOUIBottomSheetBehavior6.X0;
                        if (cOUIPanelDragListener2 != null) {
                            if (cOUIPanelDragListener2.a()) {
                                i10 = COUIBottomSheetBehavior.this.f6526y0;
                                i11 = 3;
                            } else {
                                i10 = COUIBottomSheetBehavior.this.N0;
                            }
                        } else {
                            i10 = cOUIBottomSheetBehavior6.B0;
                        }
                    } else {
                        int top2 = view.getTop();
                        if (Math.abs(top2 - COUIBottomSheetBehavior.this.f6527z0) < Math.abs(top2 - COUIBottomSheetBehavior.this.B0)) {
                            i10 = COUIBottomSheetBehavior.this.f6527z0;
                            i11 = 6;
                        } else {
                            i10 = COUIBottomSheetBehavior.this.B0;
                        }
                    }
                    i11 = 4;
                } else {
                    int top3 = view.getTop();
                    if (COUIBottomSheetBehavior.this.f6507k0) {
                        if (Math.abs(top3 - COUIBottomSheetBehavior.this.f6526y0) < Math.abs(top3 - COUIBottomSheetBehavior.this.B0)) {
                            i10 = COUIBottomSheetBehavior.this.f6526y0;
                            i11 = 3;
                        } else {
                            i10 = COUIBottomSheetBehavior.this.B0;
                            i11 = 4;
                        }
                    } else {
                        COUIBottomSheetBehavior cOUIBottomSheetBehavior7 = COUIBottomSheetBehavior.this;
                        int i15 = cOUIBottomSheetBehavior7.f6527z0;
                        if (top3 < i15) {
                            if (top3 < Math.abs(top3 - cOUIBottomSheetBehavior7.B0)) {
                                i10 = COUIBottomSheetBehavior.this.f6525x0;
                                i11 = 3;
                            } else {
                                i10 = COUIBottomSheetBehavior.this.f6527z0;
                            }
                        } else if (Math.abs(top3 - i15) < Math.abs(top3 - COUIBottomSheetBehavior.this.B0)) {
                            i10 = COUIBottomSheetBehavior.this.f6527z0;
                        } else {
                            i10 = COUIBottomSheetBehavior.this.B0;
                            i11 = 4;
                        }
                        i11 = 6;
                    }
                }
            }
            COUIBottomSheetBehavior.this.h1(view, i11, i10, true);
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public boolean m(View view, int i10) {
            COUIBottomSheetBehavior cOUIBottomSheetBehavior = COUIBottomSheetBehavior.this;
            int i11 = cOUIBottomSheetBehavior.G0;
            if (i11 == 1 || cOUIBottomSheetBehavior.V0) {
                return false;
            }
            if (i11 == 3 && cOUIBottomSheetBehavior.S0 == i10) {
                WeakReference<View> weakReference = cOUIBottomSheetBehavior.P0;
                View view2 = weakReference != null ? weakReference.get() : null;
                if (view2 != null && view2.canScrollVertically(-1)) {
                    return false;
                }
            }
            WeakReference<V> weakReference2 = COUIBottomSheetBehavior.this.O0;
            return weakReference2 != null && weakReference2.get() == view;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class h implements AccessibilityViewCommand {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f6544a;

        h(int i10) {
            this.f6544a = i10;
        }

        @Override // androidx.core.view.accessibility.AccessibilityViewCommand
        public boolean perform(View view, AccessibilityViewCommand.a aVar) {
            COUIBottomSheetBehavior.this.Z0(this.f6544a);
            return true;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class i {
        public abstract void a(View view, float f10);

        public abstract void b(View view, int i10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class j implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        private final View f6546e;

        /* renamed from: f, reason: collision with root package name */
        private boolean f6547f;

        /* renamed from: g, reason: collision with root package name */
        int f6548g;

        j(View view, int i10) {
            this.f6546e = view;
            this.f6548g = i10;
        }

        @Override // java.lang.Runnable
        public void run() {
            ViewDragHelper viewDragHelper = COUIBottomSheetBehavior.this.H0;
            if (viewDragHelper != null && viewDragHelper.n(true)) {
                COUIBottomSheetBehavior.this.M0(this.f6546e);
                ViewCompat.c0(this.f6546e, this);
            } else {
                COUIBottomSheetBehavior.this.g0(this.f6548g);
            }
            this.f6547f = false;
        }
    }

    static {
        f6493p1 = COUILog.f4543b || COUILog.d("BottomSheetBehavior", 3);
        f6494q1 = R$style.Widget_Design_BottomSheet_Modal;
    }

    public COUIBottomSheetBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        int i10;
        this.f6503i0 = 0;
        this.f6507k0 = true;
        this.f6509l0 = false;
        this.f6523v0 = null;
        this.A0 = 0.5f;
        this.C0 = -1.0f;
        this.F0 = true;
        this.G0 = 4;
        this.Q0 = new ArrayList<>();
        this.f6496b1 = 0;
        this.f6497c1 = 0.0f;
        this.f6502h1 = 16.0f;
        this.f6504i1 = 0.6f;
        this.f6506j1 = false;
        this.f6508k1 = null;
        this.f6510l1 = false;
        this.f6512m1 = new Rect();
        this.f6514n1 = true;
        this.f6516o1 = new g();
        this.f6505j0 = context;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.BottomSheetBehavior_Layout);
        this.f6518q0 = obtainStyledAttributes.hasValue(R$styleable.BottomSheetBehavior_Layout_shapeAppearance);
        int i11 = R$styleable.BottomSheetBehavior_Layout_backgroundTint;
        boolean hasValue = obtainStyledAttributes.hasValue(i11);
        if (hasValue) {
            O0(context, attributeSet, hasValue, MaterialResources.a(context, obtainStyledAttributes, i11));
        } else {
            N0(context, attributeSet, hasValue);
        }
        B();
        this.C0 = obtainStyledAttributes.getDimension(R$styleable.BottomSheetBehavior_Layout_android_elevation, -1.0f);
        int i12 = R$styleable.BottomSheetBehavior_Layout_behavior_peekHeight;
        TypedValue peekValue = obtainStyledAttributes.peekValue(i12);
        if (peekValue != null && (i10 = peekValue.data) == -1) {
            W0(i10);
        } else {
            W0(obtainStyledAttributes.getDimensionPixelSize(i12, -1));
        }
        Y(obtainStyledAttributes.getBoolean(R$styleable.BottomSheetBehavior_Layout_behavior_hideable, false));
        W(obtainStyledAttributes.getBoolean(R$styleable.BottomSheetBehavior_Layout_gestureInsetBottomIgnored, false));
        V(obtainStyledAttributes.getBoolean(R$styleable.BottomSheetBehavior_Layout_behavior_fitToContents, true));
        Y0(obtainStyledAttributes.getBoolean(R$styleable.BottomSheetBehavior_Layout_behavior_skipCollapsed, false));
        T(obtainStyledAttributes.getBoolean(R$styleable.BottomSheetBehavior_Layout_behavior_draggable, true));
        d0(obtainStyledAttributes.getInt(R$styleable.BottomSheetBehavior_Layout_behavior_saveFlags, -1));
        X(obtainStyledAttributes.getFloat(R$styleable.BottomSheetBehavior_Layout_behavior_halfExpandedRatio, 0.5f));
        int i13 = R$styleable.BottomSheetBehavior_Layout_behavior_expandedOffset;
        TypedValue peekValue2 = obtainStyledAttributes.peekValue(i13);
        if (peekValue2 != null && peekValue2.type == 16) {
            U(peekValue2.data);
        } else {
            U(obtainStyledAttributes.getDimensionPixelOffset(i13, 0));
        }
        obtainStyledAttributes.recycle();
        this.f6511m0 = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        this.f6495a1 = false;
    }

    private void B() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.f6524w0 = ofFloat;
        ofFloat.setDuration(500L);
        this.f6524w0.addUpdateListener(new b());
    }

    private void J0(V v7, AccessibilityNodeInfoCompat.a aVar, int i10) {
        ViewCompat.g0(v7, aVar, null, new h(i10));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float K() {
        VelocityTracker velocityTracker = this.R0;
        if (velocityTracker == null) {
            return 0.0f;
        }
        velocityTracker.computeCurrentVelocity(1000, this.f6511m0);
        return this.R0.getYVelocity(this.S0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void M0(View view) {
        float top = 1.0f - ((view.getTop() - G()) / this.M0);
        this.f6497c1 = top;
        COUIPanelPullUpListener cOUIPanelPullUpListener = this.Y0;
        if (cOUIPanelPullUpListener != null) {
            cOUIPanelPullUpListener.f(top);
        }
    }

    private void N0(Context context, AttributeSet attributeSet, boolean z10) {
        O0(context, attributeSet, z10, null);
    }

    private void O0(Context context, AttributeSet attributeSet, boolean z10, ColorStateList colorStateList) {
        if (this.f6518q0) {
            this.f6521t0 = ShapeAppearanceModel.e(context, attributeSet, R$attr.bottomSheetStyle, f6494q1).m();
            MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(this.f6521t0);
            this.f6519r0 = materialShapeDrawable;
            materialShapeDrawable.P(context);
            if (z10 && colorStateList != null) {
                this.f6519r0.a0(colorStateList);
                return;
            }
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.colorBackground, typedValue, true);
            this.f6519r0.setTint(typedValue.data);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void P0(View view, float f10) {
        if (this.f6499e1.S()) {
            this.f6499e1.T(f10);
            return;
        }
        this.f6508k1 = view;
        float top = view.getTop();
        this.f6500f1.c(top);
        this.f6499e1.J(top, top);
        this.f6501g1 = top;
    }

    private void Q() {
        this.S0 = -1;
        VelocityTracker velocityTracker = this.R0;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.R0 = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int Q0(View view) {
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                return ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
            }
        }
        return 0;
    }

    private boolean S0(View view, int i10, int i11) {
        View findViewById;
        if (!(view instanceof COUIPanelPercentFrameLayout) || (findViewById = view.findViewById(R$id.panel_drag_bar)) == null) {
            return false;
        }
        findViewById.getHitRect(this.f6512m1);
        return this.f6512m1.contains(i10, i11);
    }

    private void T0(SavedState savedState) {
        int i10 = this.f6503i0;
        if (i10 == 0) {
            return;
        }
        if (i10 == -1 || (i10 & 1) == 1) {
            this.f6513n0 = savedState.f6529f;
        }
        if (i10 == -1 || (i10 & 2) == 2) {
            this.f6507k0 = savedState.f6530g;
        }
        if (i10 == -1 || (i10 & 4) == 4) {
            this.D0 = savedState.f6531h;
        }
        if (i10 == -1 || (i10 & 8) == 8) {
            this.E0 = savedState.f6532i;
        }
    }

    private void X0(int i10, boolean z10) {
        V v7;
        boolean z11 = true;
        if (i10 == -1) {
            if (!this.f6515o0) {
                this.f6515o0 = true;
            }
            z11 = false;
        } else {
            if (this.f6515o0 || this.f6513n0 != i10) {
                this.f6515o0 = false;
                this.f6513n0 = Math.max(0, i10);
            }
            z11 = false;
        }
        if (!z11 || this.O0 == null) {
            return;
        }
        w();
        if (this.G0 != 4 || (v7 = this.O0.get()) == null) {
            return;
        }
        if (z10) {
            d1(this.G0);
        } else {
            v7.requestLayout();
        }
    }

    private void b1(CoordinatorLayout coordinatorLayout) {
        WindowInsets rootWindowInsets;
        if (L() || (rootWindowInsets = coordinatorLayout.getRootWindowInsets()) == null) {
            return;
        }
        this.f6513n0 += rootWindowInsets.getSystemGestureInsets().bottom;
    }

    private void d1(int i10) {
        V v7 = this.O0.get();
        if (v7 == null) {
            return;
        }
        ViewParent parent = v7.getParent();
        if (parent != null && parent.isLayoutRequested() && ViewCompat.P(v7)) {
            v7.post(new a(v7, i10));
        } else {
            c1(v7, i10);
        }
    }

    private void e1(View view) {
        e eVar = new e("offsetTopAndBottom", view);
        if (f6493p1) {
            COUILog.a("BottomSheetBehavior", "startDragToHiddenAnimation parentRootViewHeight:" + this.N0 + ",child.getTop():" + view.getTop());
        }
        new COUIPanelDragToHiddenAnimation(view, eVar).u(K()).r(5000.0f).t(0.0f).s(this.N0 - view.getTop()).a(new f()).n();
    }

    private void f1(View view, int i10, int i11, float f10, PathInterpolator pathInterpolator) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(i10, i11);
        ofFloat.setDuration(f10);
        ofFloat.setInterpolator(pathInterpolator);
        ofFloat.addUpdateListener(new c(view));
        ofFloat.addListener(new d());
        this.f6496b1 = view.getTop();
        view.offsetTopAndBottom(view.getTop());
        ofFloat.start();
    }

    private void g1(View view, int i10) {
        if (this.f6523v0 == null) {
            this.f6523v0 = new j(view, i10);
        }
        if (!((j) this.f6523v0).f6547f) {
            COUIBottomSheetBehavior<V>.j jVar = this.f6523v0;
            jVar.f6548g = i10;
            ViewCompat.c0(view, jVar);
            ((j) this.f6523v0).f6547f = true;
            return;
        }
        this.f6523v0.f6548g = i10;
    }

    private void o0() {
        V v7;
        WeakReference<V> weakReference = this.O0;
        if (weakReference == null || (v7 = weakReference.get()) == null) {
            return;
        }
        ViewCompat.e0(v7, 524288);
        ViewCompat.e0(v7, 262144);
        ViewCompat.e0(v7, 1048576);
        if (this.D0 && this.G0 != 5) {
            J0(v7, AccessibilityNodeInfoCompat.a.f2338y, 5);
        }
        int i10 = this.G0;
        if (i10 == 3) {
            J0(v7, AccessibilityNodeInfoCompat.a.f2337x, this.f6507k0 ? 4 : 6);
            return;
        }
        if (i10 == 4) {
            J0(v7, AccessibilityNodeInfoCompat.a.f2336w, this.f6507k0 ? 3 : 6);
        } else {
            if (i10 != 6) {
                return;
            }
            J0(v7, AccessibilityNodeInfoCompat.a.f2337x, 4);
            J0(v7, AccessibilityNodeInfoCompat.a.f2336w, 3);
        }
    }

    private void p0(int i10) {
        ValueAnimator valueAnimator;
        if (i10 == 2) {
            return;
        }
        boolean z10 = i10 == 3;
        if (this.f6522u0 != z10) {
            this.f6522u0 = z10;
            if (this.f6519r0 == null || (valueAnimator = this.f6524w0) == null) {
                return;
            }
            if (valueAnimator.isRunning()) {
                this.f6524w0.reverse();
                return;
            }
            float f10 = z10 ? 0.0f : 1.0f;
            this.f6524w0.setFloatValues(1.0f - f10, f10);
            this.f6524w0.start();
        }
    }

    private void q0(boolean z10) {
        Map<View, Integer> map;
        WeakReference<V> weakReference = this.O0;
        if (weakReference == null) {
            return;
        }
        ViewParent parent = weakReference.get().getParent();
        if (parent instanceof CoordinatorLayout) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) parent;
            int childCount = coordinatorLayout.getChildCount();
            if (z10) {
                if (this.W0 != null) {
                    return;
                } else {
                    this.W0 = new HashMap(childCount);
                }
            }
            for (int i10 = 0; i10 < childCount; i10++) {
                View childAt = coordinatorLayout.getChildAt(i10);
                if (childAt != this.O0.get()) {
                    if (z10) {
                        this.W0.put(childAt, Integer.valueOf(childAt.getImportantForAccessibility()));
                        if (this.f6509l0) {
                            ViewCompat.w0(childAt, 4);
                        }
                    } else if (this.f6509l0 && (map = this.W0) != null && map.containsKey(childAt)) {
                        ViewCompat.w0(childAt, this.W0.get(childAt).intValue());
                    }
                }
            }
            if (z10) {
                return;
            }
            this.W0 = null;
        }
    }

    private void w() {
        int y4 = y();
        if (this.f6507k0) {
            this.B0 = Math.max(this.M0 - y4, this.f6526y0);
        } else {
            this.B0 = this.M0 - y4;
        }
    }

    private void x() {
        this.f6527z0 = (int) (this.M0 * (1.0f - this.A0));
    }

    private int y() {
        if (this.f6515o0) {
            return Math.max(this.f6517p0, this.M0 - ((this.L0 * 9) / 16));
        }
        return this.f6513n0;
    }

    void C(int i10) {
        float f10;
        float f11;
        V v7 = this.O0.get();
        if (v7 == null || this.Q0.isEmpty()) {
            return;
        }
        int i11 = this.B0;
        if (i10 <= i11 && i11 != G()) {
            int i12 = this.B0;
            f10 = i12 - i10;
            f11 = i12 - G();
        } else {
            int i13 = this.B0;
            f10 = i13 - i10;
            f11 = this.M0 - i13;
        }
        float f12 = f10 / f11;
        for (int i14 = 0; i14 < this.Q0.size(); i14++) {
            this.Q0.get(i14).a(v7, f12);
        }
    }

    View D(View view) {
        if (ViewCompat.R(view) && view.getVisibility() == 0) {
            return view;
        }
        if (!(view instanceof ViewGroup) || view.getVisibility() != 0) {
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

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public int G() {
        return this.f6507k0 ? this.f6526y0 : this.f6525x0;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    @SuppressLint({"WrongConstant"})
    public int I() {
        return this.G0;
    }

    public void K0(i iVar) {
        if (this.Q0.contains(iVar)) {
            return;
        }
        this.Q0.add(iVar);
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public boolean L() {
        return this.f6520s0;
    }

    public void L0(float f10, float f11) {
        if (f10 != Float.MIN_VALUE && f11 != Float.MIN_VALUE) {
            this.f6506j1 = true;
            this.f6502h1 = f10;
            this.f6504i1 = f11;
            this.f6498d1 = PhysicalAnimator.e(this.f6505j0);
            this.f6500f1 = new r7.j(0.0f);
            DragBehavior dragBehavior = (DragBehavior) ((DragBehavior) new DragBehavior().I(this.f6500f1)).z(this.f6502h1, this.f6504i1).b(null);
            this.f6499e1 = dragBehavior;
            this.f6498d1.c(dragBehavior);
            this.f6498d1.a(this.f6499e1, this);
            this.f6498d1.b(this.f6499e1, this);
            return;
        }
        this.f6506j1 = false;
    }

    public boolean R0() {
        return this.f6495a1;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void T(boolean z10) {
        this.F0 = z10;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void U(int i10) {
        if (i10 >= 0) {
            this.f6525x0 = i10;
            return;
        }
        throw new IllegalArgumentException("offset must be greater than or equal to 0");
    }

    public void U0(boolean z10) {
        this.f6514n1 = z10;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void V(boolean z10) {
        if (this.f6507k0 == z10) {
            return;
        }
        this.f6507k0 = z10;
        if (this.O0 != null) {
            w();
        }
        g0((this.f6507k0 && this.G0 == 6) ? 3 : this.G0);
        o0();
    }

    public void V0(boolean z10) {
        this.f6510l1 = z10;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void W(boolean z10) {
        this.f6520s0 = z10;
    }

    public void W0(int i10) {
        X0(i10, false);
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void X(float f10) {
        if (f10 > 0.0f && f10 < 1.0f) {
            this.A0 = f10;
            if (this.O0 != null) {
                x();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("ratio must be a float value between 0 and 1");
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    @SuppressLint({"WrongConstant"})
    public void Y(boolean z10) {
        if (this.D0 != z10) {
            this.D0 = z10;
            if (!z10 && this.G0 == 5) {
                Z0(4);
            }
            o0();
        }
    }

    public void Y0(boolean z10) {
        this.E0 = z10;
    }

    public void Z0(int i10) {
        if (i10 == this.G0) {
            return;
        }
        if (this.O0 == null) {
            if (i10 == 4 || i10 == 3 || i10 == 6 || (this.D0 && i10 == 5)) {
                this.G0 = i10;
                return;
            }
            return;
        }
        d1(i10);
    }

    @Override // r7.AnimationListener
    public void a(r7.c cVar) {
    }

    public void a1(COUIPanelPullUpListener cOUIPanelPullUpListener) {
        this.Y0 = cOUIPanelPullUpListener;
    }

    @Override // r7.AnimationUpdateListener
    public void b(r7.c cVar) {
        this.f6501g1 = ((Float) cVar.n()).floatValue();
        if (this.f6508k1 != null) {
            ViewCompat.W(this.f6508k1, -((int) (r2.getTop() - this.f6501g1)));
            C(this.f6508k1.getTop());
        }
    }

    @Override // r7.AnimationListener
    public void c(r7.c cVar) {
    }

    void c1(View view, int i10) {
        int i11;
        int i12;
        if (i10 == 4) {
            i11 = this.B0;
        } else if (i10 == 6) {
            int i13 = this.f6527z0;
            if (!this.f6507k0 || i13 > (i12 = this.f6526y0)) {
                i11 = i13;
            } else {
                i10 = 3;
                i11 = i12;
            }
        } else if (i10 == 3) {
            i11 = G();
        } else if (this.D0 && i10 == 5) {
            i11 = this.N0;
        } else {
            throw new IllegalArgumentException("Illegal state argument: " + i10);
        }
        h1(view, i10, i11, false);
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void d0(int i10) {
        this.f6503i0 = i10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g0(int i10) {
        V v7;
        if (this.G0 == i10) {
            return;
        }
        this.G0 = i10;
        WeakReference<V> weakReference = this.O0;
        if (weakReference == null || (v7 = weakReference.get()) == null) {
            return;
        }
        if (i10 == 3) {
            q0(true);
        } else if (i10 == 6 || i10 == 5 || i10 == 4) {
            q0(false);
        }
        p0(i10);
        for (int i11 = 0; i11 < this.Q0.size(); i11++) {
            this.Q0.get(i11).b(v7, i10);
        }
        o0();
    }

    void h1(View view, int i10, int i11, boolean z10) {
        boolean R;
        if (z10 && I() == 1) {
            R = this.H0.P(view.getLeft(), i11);
        } else {
            R = this.H0.R(view, view.getLeft(), i11);
        }
        if (R) {
            g0(2);
            p0(i10);
            float K = K();
            if (this.f6510l1) {
                if (i10 == 5) {
                    f1(view, 0, this.f6505j0.getResources().getDimensionPixelOffset(R$dimen.coui_panel_max_height_tiny_screen), 333.0f, new COUIOutEaseInterpolator());
                    return;
                } else {
                    g1(view, i10);
                    return;
                }
            }
            if (i10 == 5 && K > 10000.0f) {
                e1(view);
                return;
            } else {
                g1(view, i10);
                return;
            }
        }
        g0(i10);
    }

    boolean k0(View view, float f10) {
        if (this.E0) {
            return true;
        }
        if (view.getTop() < this.B0) {
            return false;
        }
        return Math.abs((((float) view.getTop()) + (f10 * 0.1f)) - ((float) this.B0)) / ((float) y()) > 0.5f;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onAttachedToLayoutParams(CoordinatorLayout.e eVar) {
        super.onAttachedToLayoutParams(eVar);
        this.O0 = null;
        this.H0 = null;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams();
        this.O0 = null;
        this.H0 = null;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, V v7, MotionEvent motionEvent) {
        ViewDragHelper viewDragHelper;
        if (v7.isShown() && this.F0) {
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 0) {
                Q();
            }
            if (this.R0 == null) {
                this.R0 = VelocityTracker.obtain();
            }
            this.R0.addMovement(motionEvent);
            if (actionMasked == 0) {
                this.T0 = (int) motionEvent.getX();
                int y4 = (int) motionEvent.getY();
                this.U0 = y4;
                if (!this.f6514n1 && !S0(v7, this.T0, y4)) {
                    this.I0 = true;
                    return false;
                }
                this.I0 = false;
                if (this.G0 != 2) {
                    WeakReference<View> weakReference = this.P0;
                    View view = weakReference != null ? weakReference.get() : null;
                    if (view != null && coordinatorLayout.F(view, this.T0, this.U0)) {
                        this.S0 = motionEvent.getPointerId(UIUtil.a(motionEvent, motionEvent.getActionIndex()));
                        this.V0 = true;
                    }
                }
                this.I0 = this.S0 == -1 && !coordinatorLayout.F(v7, this.T0, this.U0);
            } else if (actionMasked == 1) {
                COUIPanelPullUpListener cOUIPanelPullUpListener = this.Y0;
                if (cOUIPanelPullUpListener != null) {
                    cOUIPanelPullUpListener.a();
                }
            } else if (actionMasked == 3) {
                this.V0 = false;
                this.S0 = -1;
                if (this.I0) {
                    this.I0 = false;
                    return false;
                }
            }
            if (!this.I0 && (viewDragHelper = this.H0) != null && viewDragHelper.Q(motionEvent)) {
                return true;
            }
            WeakReference<View> weakReference2 = this.P0;
            View view2 = weakReference2 != null ? weakReference2.get() : null;
            return view2 != null ? (actionMasked != 2 || this.I0 || this.G0 == 1 || coordinatorLayout.F(view2, this.T0, this.U0) || this.H0 == null || Math.abs(((float) this.U0) - motionEvent.getY()) <= ((float) this.H0.A())) ? false : true : (actionMasked != 2 || this.I0 || this.G0 == 1 || this.H0 == null || Math.abs(((float) this.U0) - motionEvent.getY()) <= ((float) this.H0.A())) ? false : true;
        }
        this.I0 = true;
        return false;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, V v7, int i10) {
        boolean z10;
        MaterialShapeDrawable materialShapeDrawable;
        if (ViewCompat.u(coordinatorLayout) && !ViewCompat.u(v7)) {
            v7.setFitsSystemWindows(true);
        }
        float f10 = 1.0f;
        if (this.O0 == null) {
            this.f6517p0 = coordinatorLayout.getResources().getDimensionPixelSize(com.google.android.material.R$dimen.design_bottom_sheet_peek_height_min);
            b1(coordinatorLayout);
            this.O0 = new WeakReference<>(v7);
            if (this.f6518q0 && (materialShapeDrawable = this.f6519r0) != null) {
                ViewCompat.p0(v7, materialShapeDrawable);
            }
            MaterialShapeDrawable materialShapeDrawable2 = this.f6519r0;
            if (materialShapeDrawable2 != null) {
                float f11 = this.C0;
                if (f11 == -1.0f) {
                    f11 = ViewCompat.t(v7);
                }
                materialShapeDrawable2.Z(f11);
                boolean z11 = this.G0 == 3;
                this.f6522u0 = z11;
                this.f6519r0.b0(z11 ? 0.0f : 1.0f);
            }
            o0();
            if (ViewCompat.v(v7) == 0) {
                ViewCompat.w0(v7, 1);
            }
        }
        if (this.H0 == null) {
            this.H0 = ViewDragHelper.p(coordinatorLayout, this.f6516o1);
        }
        int top = v7.getTop();
        coordinatorLayout.M(v7, i10);
        this.L0 = coordinatorLayout.getWidth();
        this.M0 = coordinatorLayout.getHeight();
        this.N0 = coordinatorLayout.getRootView().getHeight();
        if (v7 instanceof COUIPanelPercentFrameLayout) {
            COUIPanelPercentFrameLayout cOUIPanelPercentFrameLayout = (COUIPanelPercentFrameLayout) v7;
            f10 = cOUIPanelPercentFrameLayout.getRatio();
            z10 = cOUIPanelPercentFrameLayout.getHasAnchor();
        } else {
            z10 = false;
        }
        if (!this.Z0) {
            int Q0 = Q0(v7);
            if (z10) {
                this.f6526y0 = 0;
            } else {
                this.f6526y0 = (int) Math.max(0.0f, ((this.M0 - Q0) / f10) - (v7.getHeight() / f10));
            }
        }
        if (f6493p1) {
            Log.d("BottomSheetBehavior", "updateFollowHandPanelLocation fitToContentsOffset:" + this.f6526y0);
        }
        this.Z0 = false;
        x();
        w();
        int i11 = this.G0;
        if (i11 == 3) {
            ViewCompat.W(v7, G());
        } else if (i11 == 6) {
            ViewCompat.W(v7, this.f6527z0);
        } else if (this.D0 && i11 == 5) {
            ViewCompat.W(v7, this.M0);
        } else if (i11 == 4) {
            ViewCompat.W(v7, this.B0);
        } else if (i11 == 1 || i11 == 2) {
            ViewCompat.W(v7, top - v7.getTop());
        }
        if (f6493p1) {
            Log.e("BottomSheetBehavior", "behavior parentHeight: " + this.M0 + " marginBottom: " + Q0(v7) + "\n mDesignBottomSheetFrameLayout.getRatio()" + f10 + " fitToContentsOffset: " + this.f6526y0 + " H: " + v7.getMeasuredHeight() + "\n Y: " + v7.getY() + " getExpandedOffset" + G());
        }
        this.P0 = new WeakReference<>(D(v7));
        return true;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, V v7, View view, float f10, float f11) {
        WeakReference<View> weakReference = this.P0;
        if (weakReference == null || view != weakReference.get()) {
            return false;
        }
        return this.G0 != 3 || super.onNestedPreFling(coordinatorLayout, v7, view, f10, f11);
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, V v7, View view, int i10, int i11, int[] iArr, int i12) {
        if (i12 == 1) {
            return;
        }
        WeakReference<View> weakReference = this.P0;
        if (view != (weakReference != null ? weakReference.get() : null)) {
            return;
        }
        int top = v7.getTop();
        int i13 = top - i11;
        if (i11 > 0) {
            if (i13 < G()) {
                iArr[1] = top - G();
                M0(v7);
                if (this.f6506j1) {
                    P0(v7, G());
                } else {
                    ViewCompat.W(v7, -iArr[1]);
                }
                g0(3);
            } else {
                if (!this.F0) {
                    return;
                }
                M0(v7);
                iArr[1] = i11;
                if (this.f6506j1) {
                    P0(v7, i13);
                } else {
                    ViewCompat.W(v7, -i11);
                }
                g0(1);
            }
        } else if (i11 < 0 && !view.canScrollVertically(-1)) {
            if (i13 > this.B0 && !this.D0) {
                M0(v7);
                int i14 = this.B0;
                iArr[1] = top - i14;
                if (this.f6506j1) {
                    P0(v7, i14);
                } else {
                    ViewCompat.W(v7, -iArr[1]);
                }
                g0(4);
            } else {
                if (!this.F0) {
                    return;
                }
                iArr[1] = i11;
                if (i11 < -100) {
                    i11 = (int) (i11 * 0.5f);
                }
                M0(v7);
                if (this.f6506j1) {
                    P0(v7, i13);
                } else {
                    ViewCompat.W(v7, -i11);
                }
                g0(1);
            }
        }
        if (!this.f6506j1) {
            C(v7.getTop());
        }
        this.J0 = i11;
        this.K0 = true;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, V v7, View view, int i10, int i11, int i12, int i13, int i14, int[] iArr) {
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onRestoreInstanceState(CoordinatorLayout coordinatorLayout, V v7, Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(coordinatorLayout, v7, savedState.getSuperState());
        T0(savedState);
        int i10 = savedState.f6528e;
        if (i10 != 1 && i10 != 2) {
            this.G0 = i10;
        } else {
            this.G0 = 4;
        }
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public Parcelable onSaveInstanceState(CoordinatorLayout coordinatorLayout, V v7) {
        return new SavedState(super.onSaveInstanceState(coordinatorLayout, v7), (COUIBottomSheetBehavior<?>) this);
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V v7, View view, View view2, int i10, int i11) {
        this.J0 = 0;
        this.K0 = false;
        return (i10 & 2) != 0;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, V v7, View view, int i10) {
        int i11;
        if (this.f6506j1 && this.f6499e1.S()) {
            this.f6499e1.O(0.0f);
            this.f6508k1 = null;
        }
        int i12 = 3;
        if (v7.getTop() == G()) {
            g0(3);
            return;
        }
        WeakReference<View> weakReference = this.P0;
        if (weakReference != null && view == weakReference.get() && this.K0) {
            if (this.J0 > 0) {
                if (this.f6507k0) {
                    i11 = this.f6526y0;
                } else {
                    int top = v7.getTop();
                    int i13 = this.f6527z0;
                    if (top > i13) {
                        i11 = i13;
                        i12 = 6;
                    } else {
                        i11 = this.f6525x0;
                    }
                }
            } else if (this.D0 && k0(v7, K())) {
                COUIPanelDragListener cOUIPanelDragListener = this.X0;
                if (cOUIPanelDragListener != null && cOUIPanelDragListener.a()) {
                    i11 = this.f6526y0;
                    this.f6495a1 = false;
                } else {
                    i11 = this.N0;
                    this.f6495a1 = true;
                    i12 = 5;
                }
            } else if (this.J0 == 0) {
                int top2 = v7.getTop();
                if (this.f6507k0) {
                    if (Math.abs(top2 - this.f6526y0) < Math.abs(top2 - this.B0)) {
                        i11 = this.f6526y0;
                    } else {
                        i11 = this.B0;
                        i12 = 4;
                    }
                } else {
                    int i14 = this.f6527z0;
                    if (top2 < i14) {
                        if (top2 < Math.abs(top2 - this.B0)) {
                            i11 = this.f6525x0;
                        } else {
                            i11 = this.f6527z0;
                        }
                    } else if (Math.abs(top2 - i14) < Math.abs(top2 - this.B0)) {
                        i11 = this.f6527z0;
                    } else {
                        i11 = this.B0;
                        i12 = 4;
                    }
                    i12 = 6;
                }
            } else {
                if (this.f6507k0) {
                    COUIPanelDragListener cOUIPanelDragListener2 = this.X0;
                    if (cOUIPanelDragListener2 != null) {
                        if (cOUIPanelDragListener2.a()) {
                            i11 = this.f6526y0;
                        } else {
                            i11 = this.N0;
                            i12 = 5;
                        }
                    } else {
                        i11 = this.B0;
                    }
                } else {
                    int top3 = v7.getTop();
                    if (Math.abs(top3 - this.f6527z0) < Math.abs(top3 - this.B0)) {
                        i11 = this.f6527z0;
                        i12 = 6;
                    } else {
                        i11 = this.B0;
                    }
                }
                i12 = 4;
            }
            h1(v7, i12, i11, false);
            this.K0 = false;
        }
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onTouchEvent(CoordinatorLayout coordinatorLayout, V v7, MotionEvent motionEvent) {
        if (!v7.isShown()) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (this.G0 == 1 && actionMasked == 0) {
            return true;
        }
        ViewDragHelper viewDragHelper = this.H0;
        if (viewDragHelper != null) {
            try {
                viewDragHelper.G(motionEvent);
            } catch (Exception e10) {
                e10.printStackTrace();
                return true;
            }
        }
        if (actionMasked == 0) {
            Q();
        }
        if (this.R0 == null) {
            this.R0 = VelocityTracker.obtain();
        }
        this.R0.addMovement(motionEvent);
        if (actionMasked == 2 && !this.I0 && this.H0 != null && Math.abs(this.U0 - motionEvent.getY()) > this.H0.A()) {
            this.H0.c(v7, motionEvent.getPointerId(UIUtil.a(motionEvent, motionEvent.getActionIndex())));
        }
        return !this.I0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        final int f6528e;

        /* renamed from: f, reason: collision with root package name */
        int f6529f;

        /* renamed from: g, reason: collision with root package name */
        boolean f6530g;

        /* renamed from: h, reason: collision with root package name */
        boolean f6531h;

        /* renamed from: i, reason: collision with root package name */
        boolean f6532i;

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
            this.f6528e = parcel.readInt();
            this.f6529f = parcel.readInt();
            this.f6530g = parcel.readInt() == 1;
            this.f6531h = parcel.readInt() == 1;
            this.f6532i = parcel.readInt() == 1;
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeInt(this.f6528e);
            parcel.writeInt(this.f6529f);
            parcel.writeInt(this.f6530g ? 1 : 0);
            parcel.writeInt(this.f6531h ? 1 : 0);
            parcel.writeInt(this.f6532i ? 1 : 0);
        }

        public SavedState(Parcelable parcelable, COUIBottomSheetBehavior<?> cOUIBottomSheetBehavior) {
            super(parcelable);
            this.f6528e = cOUIBottomSheetBehavior.G0;
            this.f6529f = ((COUIBottomSheetBehavior) cOUIBottomSheetBehavior).f6513n0;
            this.f6530g = ((COUIBottomSheetBehavior) cOUIBottomSheetBehavior).f6507k0;
            this.f6531h = cOUIBottomSheetBehavior.D0;
            this.f6532i = ((COUIBottomSheetBehavior) cOUIBottomSheetBehavior).E0;
        }
    }
}
