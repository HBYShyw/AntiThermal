package com.coui.appcompat.panel;

import android.R;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsets;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.customview.view.AbsSavedState;
import androidx.customview.widget.ViewDragHelper;
import c4.MaterialShapeDrawable;
import c4.ShapeAppearanceModel;
import com.google.android.material.R$attr;
import com.google.android.material.R$dimen;
import com.google.android.material.R$style;
import com.google.android.material.R$styleable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import f2.COUIPanelDragListener;
import f2.COUIPanelPullUpListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import z3.MaterialResources;

/* loaded from: classes.dex */
public class COUIGuideBehavior<V extends View> extends BottomSheetBehavior<V> {

    /* renamed from: a1, reason: collision with root package name */
    private static final int f6560a1 = R$style.Widget_Design_BottomSheet_Modal;
    int A0;
    float B0;
    boolean C0;
    private boolean D0;
    private boolean E0;
    int F0;
    ViewDragHelper G0;
    private boolean H0;
    private int I0;
    private boolean J0;
    int K0;
    int L0;
    WeakReference<V> M0;
    WeakReference<View> N0;
    private final ArrayList<e> O0;
    private VelocityTracker P0;
    int Q0;
    private int R0;
    private int S0;
    boolean T0;
    private Map<View, Integer> U0;
    COUIPanelDragListener V0;
    private COUIPanelPullUpListener W0;
    private boolean X0;
    private boolean Y0;
    private final ViewDragHelper.c Z0;

    /* renamed from: i0, reason: collision with root package name */
    private int f6561i0;

    /* renamed from: j0, reason: collision with root package name */
    private boolean f6562j0;

    /* renamed from: k0, reason: collision with root package name */
    private boolean f6563k0;

    /* renamed from: l0, reason: collision with root package name */
    private float f6564l0;

    /* renamed from: m0, reason: collision with root package name */
    private int f6565m0;

    /* renamed from: n0, reason: collision with root package name */
    private boolean f6566n0;

    /* renamed from: o0, reason: collision with root package name */
    private int f6567o0;

    /* renamed from: p0, reason: collision with root package name */
    private boolean f6568p0;

    /* renamed from: q0, reason: collision with root package name */
    private MaterialShapeDrawable f6569q0;

    /* renamed from: r0, reason: collision with root package name */
    private boolean f6570r0;

    /* renamed from: s0, reason: collision with root package name */
    private ShapeAppearanceModel f6571s0;

    /* renamed from: t0, reason: collision with root package name */
    private boolean f6572t0;

    /* renamed from: u0, reason: collision with root package name */
    private COUIGuideBehavior<V>.f f6573u0;

    /* renamed from: v0, reason: collision with root package name */
    private ValueAnimator f6574v0;

    /* renamed from: w0, reason: collision with root package name */
    int f6575w0;

    /* renamed from: x0, reason: collision with root package name */
    int f6576x0;

    /* renamed from: y0, reason: collision with root package name */
    int f6577y0;

    /* renamed from: z0, reason: collision with root package name */
    float f6578z0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ View f6584e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ int f6585f;

        a(View view, int i10) {
            this.f6584e = view;
            this.f6585f = i10;
        }

        @Override // java.lang.Runnable
        public void run() {
            COUIGuideBehavior.this.G0(this.f6584e, this.f6585f);
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
            if (COUIGuideBehavior.this.f6569q0 != null) {
                COUIGuideBehavior.this.f6569q0.b0(floatValue);
            }
        }
    }

    /* loaded from: classes.dex */
    class c extends ViewDragHelper.c {
        c() {
        }

        private boolean n(View view) {
            int top = view.getTop();
            COUIGuideBehavior cOUIGuideBehavior = COUIGuideBehavior.this;
            return top > (cOUIGuideBehavior.L0 + cOUIGuideBehavior.G()) / 2;
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public int a(View view, int i10, int i11) {
            return view.getLeft();
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public int b(View view, int i10, int i11) {
            int i12 = 0;
            if (COUIGuideBehavior.this.W0 != null) {
                int i13 = COUIGuideBehavior.this.F0;
                if (i13 == 3 || (i13 == 1 && view.getTop() <= COUIGuideBehavior.this.G())) {
                    COUIGuideBehavior.this.X0 = true;
                    i12 = COUIGuideBehavior.this.W0.c(i11, COUIGuideBehavior.this.G());
                }
            }
            int G = COUIGuideBehavior.this.G() - i12;
            COUIGuideBehavior cOUIGuideBehavior = COUIGuideBehavior.this;
            return q.a.b(i10, G, cOUIGuideBehavior.C0 ? cOUIGuideBehavior.L0 : cOUIGuideBehavior.A0);
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public int e(View view) {
            COUIGuideBehavior cOUIGuideBehavior = COUIGuideBehavior.this;
            if (cOUIGuideBehavior.C0) {
                return cOUIGuideBehavior.L0;
            }
            return cOUIGuideBehavior.A0;
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public void j(int i10) {
            if (i10 == 1 && COUIGuideBehavior.this.E0) {
                COUIGuideBehavior.this.g0(1);
            }
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public void k(View view, int i10, int i11, int i12, int i13) {
            COUIGuideBehavior.this.C(i11);
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public void l(View view, float f10, float f11) {
            int i10;
            if (COUIGuideBehavior.this.W0 != null && COUIGuideBehavior.this.L0 - view.getHeight() < COUIGuideBehavior.this.G() && view.getTop() < COUIGuideBehavior.this.G()) {
                COUIGuideBehavior.this.W0.b(COUIGuideBehavior.this.G());
                return;
            }
            int i11 = 4;
            if (f11 < 0.0f) {
                if (COUIGuideBehavior.this.f6562j0) {
                    i10 = COUIGuideBehavior.this.f6576x0;
                } else {
                    int top = view.getTop();
                    COUIGuideBehavior cOUIGuideBehavior = COUIGuideBehavior.this;
                    int i12 = cOUIGuideBehavior.f6577y0;
                    if (top > i12) {
                        i10 = i12;
                        i11 = 6;
                    } else {
                        i10 = cOUIGuideBehavior.f6575w0;
                    }
                }
                i11 = 3;
            } else {
                COUIGuideBehavior cOUIGuideBehavior2 = COUIGuideBehavior.this;
                if (cOUIGuideBehavior2.C0 && cOUIGuideBehavior2.k0(view, f11)) {
                    COUIPanelDragListener cOUIPanelDragListener = COUIGuideBehavior.this.V0;
                    if (cOUIPanelDragListener != null && cOUIPanelDragListener.a()) {
                        COUIGuideBehavior cOUIGuideBehavior3 = COUIGuideBehavior.this;
                        int i13 = cOUIGuideBehavior3.f6576x0;
                        cOUIGuideBehavior3.Y0 = false;
                        i10 = i13;
                    } else if ((Math.abs(f10) < Math.abs(f11) && f11 > 500.0f) || n(view)) {
                        COUIGuideBehavior cOUIGuideBehavior4 = COUIGuideBehavior.this;
                        int i14 = cOUIGuideBehavior4.L0;
                        i11 = 5;
                        cOUIGuideBehavior4.Y0 = true;
                        i10 = i14;
                    } else if (COUIGuideBehavior.this.f6562j0) {
                        i10 = COUIGuideBehavior.this.f6576x0;
                    } else if (Math.abs(view.getTop() - COUIGuideBehavior.this.f6575w0) < Math.abs(view.getTop() - COUIGuideBehavior.this.f6577y0)) {
                        i10 = COUIGuideBehavior.this.f6575w0;
                    } else {
                        i10 = COUIGuideBehavior.this.f6577y0;
                        i11 = 6;
                    }
                    i11 = 3;
                } else if (f11 != 0.0f && Math.abs(f10) <= Math.abs(f11)) {
                    if (COUIGuideBehavior.this.f6562j0) {
                        i10 = COUIGuideBehavior.this.A0;
                    } else {
                        int top2 = view.getTop();
                        if (Math.abs(top2 - COUIGuideBehavior.this.f6577y0) < Math.abs(top2 - COUIGuideBehavior.this.A0)) {
                            i10 = COUIGuideBehavior.this.f6577y0;
                            i11 = 6;
                        } else {
                            i10 = COUIGuideBehavior.this.A0;
                        }
                    }
                } else {
                    int top3 = view.getTop();
                    if (COUIGuideBehavior.this.f6562j0) {
                        if (Math.abs(top3 - COUIGuideBehavior.this.f6576x0) < Math.abs(top3 - COUIGuideBehavior.this.A0)) {
                            i10 = COUIGuideBehavior.this.f6576x0;
                            i11 = 3;
                        } else {
                            i10 = COUIGuideBehavior.this.A0;
                        }
                    } else {
                        COUIGuideBehavior cOUIGuideBehavior5 = COUIGuideBehavior.this;
                        int i15 = cOUIGuideBehavior5.f6577y0;
                        if (top3 < i15) {
                            if (top3 < Math.abs(top3 - cOUIGuideBehavior5.A0)) {
                                i10 = COUIGuideBehavior.this.f6575w0;
                                i11 = 3;
                            } else {
                                i10 = COUIGuideBehavior.this.f6577y0;
                            }
                        } else if (Math.abs(top3 - i15) < Math.abs(top3 - COUIGuideBehavior.this.A0)) {
                            i10 = COUIGuideBehavior.this.f6577y0;
                        } else {
                            i10 = COUIGuideBehavior.this.A0;
                        }
                        i11 = 6;
                    }
                }
            }
            COUIGuideBehavior.this.I0(view, i11, i10, true);
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public boolean m(View view, int i10) {
            COUIGuideBehavior cOUIGuideBehavior = COUIGuideBehavior.this;
            int i11 = cOUIGuideBehavior.F0;
            if (i11 == 1 || cOUIGuideBehavior.T0) {
                return false;
            }
            if (i11 == 3 && cOUIGuideBehavior.Q0 == i10) {
                WeakReference<View> weakReference = cOUIGuideBehavior.N0;
                View view2 = weakReference != null ? weakReference.get() : null;
                if (view2 != null && view2.canScrollVertically(-1)) {
                    return false;
                }
            }
            WeakReference<V> weakReference2 = COUIGuideBehavior.this.M0;
            return weakReference2 != null && weakReference2.get() == view;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d implements AccessibilityViewCommand {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f6589a;

        d(int i10) {
            this.f6589a = i10;
        }

        @Override // androidx.core.view.accessibility.AccessibilityViewCommand
        public boolean perform(View view, AccessibilityViewCommand.a aVar) {
            COUIGuideBehavior.this.f0(this.f6589a);
            return true;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class e {
        public abstract void a(View view, float f10);

        public abstract void b(View view, int i10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class f implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        private final View f6591e;

        /* renamed from: f, reason: collision with root package name */
        private boolean f6592f;

        /* renamed from: g, reason: collision with root package name */
        int f6593g;

        f(View view, int i10) {
            this.f6591e = view;
            this.f6593g = i10;
        }

        @Override // java.lang.Runnable
        public void run() {
            ViewDragHelper viewDragHelper = COUIGuideBehavior.this.G0;
            if (viewDragHelper != null && viewDragHelper.n(true)) {
                ViewCompat.c0(this.f6591e, this);
            } else {
                COUIGuideBehavior.this.g0(this.f6593g);
            }
            this.f6592f = false;
        }
    }

    public COUIGuideBehavior() {
        this.f6561i0 = 0;
        this.f6562j0 = true;
        this.f6563k0 = false;
        this.f6573u0 = null;
        this.f6578z0 = 0.5f;
        this.B0 = -1.0f;
        this.E0 = true;
        this.F0 = 4;
        this.O0 = new ArrayList<>();
        this.Z0 = new c();
    }

    private void A0(V v7, AccessibilityNodeInfoCompat.a aVar, int i10) {
        ViewCompat.g0(v7, aVar, null, new d(i10));
    }

    private void B() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.f6574v0 = ofFloat;
        ofFloat.setDuration(500L);
        this.f6574v0.addUpdateListener(new b());
    }

    private void B0(Context context, AttributeSet attributeSet, boolean z10) {
        C0(context, attributeSet, z10, null);
    }

    private void C0(Context context, AttributeSet attributeSet, boolean z10, ColorStateList colorStateList) {
        if (this.f6568p0) {
            this.f6571s0 = ShapeAppearanceModel.e(context, attributeSet, R$attr.bottomSheetStyle, f6560a1).m();
            MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(this.f6571s0);
            this.f6569q0 = materialShapeDrawable;
            materialShapeDrawable.P(context);
            if (z10 && colorStateList != null) {
                this.f6569q0.a0(colorStateList);
                return;
            }
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.colorBackground, typedValue, true);
            this.f6569q0.setTint(typedValue.data);
        }
    }

    private void D0(SavedState savedState) {
        int i10 = this.f6561i0;
        if (i10 == 0) {
            return;
        }
        if (i10 == -1 || (i10 & 1) == 1) {
            this.f6565m0 = savedState.f6580f;
        }
        if (i10 == -1 || (i10 & 2) == 2) {
            this.f6562j0 = savedState.f6581g;
        }
        if (i10 == -1 || (i10 & 4) == 4) {
            this.C0 = savedState.f6582h;
        }
        if (i10 == -1 || (i10 & 8) == 8) {
            this.D0 = savedState.f6583i;
        }
    }

    private void E0(int i10, boolean z10) {
        V v7;
        boolean z11 = true;
        if (i10 == -1) {
            if (!this.f6566n0) {
                this.f6566n0 = true;
            }
            z11 = false;
        } else {
            if (this.f6566n0 || this.f6565m0 != i10) {
                this.f6566n0 = false;
                this.f6565m0 = Math.max(0, i10);
            }
            z11 = false;
        }
        if (!z11 || this.M0 == null) {
            return;
        }
        w();
        if (this.F0 != 4 || (v7 = this.M0.get()) == null) {
            return;
        }
        if (z10) {
            H0(this.F0);
        } else {
            v7.requestLayout();
        }
    }

    private void F0(CoordinatorLayout coordinatorLayout) {
        WindowInsets rootWindowInsets;
        if (L() || (rootWindowInsets = coordinatorLayout.getRootWindowInsets()) == null) {
            return;
        }
        this.f6565m0 += rootWindowInsets.getSystemGestureInsets().bottom;
    }

    private void H0(int i10) {
        V v7 = this.M0.get();
        if (v7 == null) {
            return;
        }
        ViewParent parent = v7.getParent();
        if (parent != null && parent.isLayoutRequested() && ViewCompat.P(v7)) {
            v7.post(new a(v7, i10));
        } else {
            G0(v7, i10);
        }
    }

    private float K() {
        VelocityTracker velocityTracker = this.P0;
        if (velocityTracker == null) {
            return 0.0f;
        }
        velocityTracker.computeCurrentVelocity(1000, this.f6564l0);
        return this.P0.getYVelocity(this.Q0);
    }

    private void Q() {
        this.Q0 = -1;
        VelocityTracker velocityTracker = this.P0;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.P0 = null;
        }
    }

    private void o0() {
        V v7;
        WeakReference<V> weakReference = this.M0;
        if (weakReference == null || (v7 = weakReference.get()) == null) {
            return;
        }
        ViewCompat.e0(v7, 524288);
        ViewCompat.e0(v7, 262144);
        ViewCompat.e0(v7, 1048576);
        if (this.C0 && this.F0 != 5) {
            A0(v7, AccessibilityNodeInfoCompat.a.f2338y, 5);
        }
        int i10 = this.F0;
        if (i10 == 3) {
            A0(v7, AccessibilityNodeInfoCompat.a.f2337x, this.f6562j0 ? 4 : 6);
            return;
        }
        if (i10 == 4) {
            A0(v7, AccessibilityNodeInfoCompat.a.f2336w, this.f6562j0 ? 3 : 6);
        } else {
            if (i10 != 6) {
                return;
            }
            A0(v7, AccessibilityNodeInfoCompat.a.f2337x, 4);
            A0(v7, AccessibilityNodeInfoCompat.a.f2336w, 3);
        }
    }

    private void p0(int i10) {
        ValueAnimator valueAnimator;
        if (i10 == 2) {
            return;
        }
        boolean z10 = i10 == 3;
        if (this.f6572t0 != z10) {
            this.f6572t0 = z10;
            if (this.f6569q0 == null || (valueAnimator = this.f6574v0) == null) {
                return;
            }
            if (valueAnimator.isRunning()) {
                this.f6574v0.reverse();
                return;
            }
            float f10 = z10 ? 0.0f : 1.0f;
            this.f6574v0.setFloatValues(1.0f - f10, f10);
            this.f6574v0.start();
        }
    }

    private void q0(boolean z10) {
        Map<View, Integer> map;
        WeakReference<V> weakReference = this.M0;
        if (weakReference == null) {
            return;
        }
        ViewParent parent = weakReference.get().getParent();
        if (parent instanceof CoordinatorLayout) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) parent;
            int childCount = coordinatorLayout.getChildCount();
            if (z10) {
                if (this.U0 != null) {
                    return;
                } else {
                    this.U0 = new HashMap(childCount);
                }
            }
            for (int i10 = 0; i10 < childCount; i10++) {
                View childAt = coordinatorLayout.getChildAt(i10);
                if (childAt != this.M0.get()) {
                    if (z10) {
                        this.U0.put(childAt, Integer.valueOf(childAt.getImportantForAccessibility()));
                        if (this.f6563k0) {
                            ViewCompat.w0(childAt, 4);
                        }
                    } else if (this.f6563k0 && (map = this.U0) != null && map.containsKey(childAt)) {
                        ViewCompat.w0(childAt, this.U0.get(childAt).intValue());
                    }
                }
            }
            if (z10) {
                return;
            }
            this.U0 = null;
        }
    }

    private void w() {
        int y4 = y();
        if (this.f6562j0) {
            this.A0 = Math.max(this.L0 - y4, this.f6576x0);
        } else {
            this.A0 = this.L0 - y4;
        }
    }

    private void x() {
        this.f6577y0 = (int) (this.L0 * (1.0f - this.f6578z0));
    }

    private int y() {
        if (this.f6566n0) {
            return Math.max(this.f6567o0, this.L0 - ((this.K0 * 9) / 16));
        }
        return this.f6565m0;
    }

    void C(int i10) {
        float f10;
        float f11;
        V v7 = this.M0.get();
        if (v7 == null || this.O0.isEmpty()) {
            return;
        }
        int i11 = this.A0;
        if (i10 <= i11 && i11 != G()) {
            int i12 = this.A0;
            f10 = i12 - i10;
            f11 = i12 - G();
        } else {
            int i13 = this.A0;
            f10 = i13 - i10;
            f11 = this.L0 - i13;
        }
        float f12 = f10 / f11;
        for (int i14 = 0; i14 < this.O0.size(); i14++) {
            this.O0.get(i14).a(v7, f12);
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
        return this.f6562j0 ? this.f6576x0 : this.f6575w0;
    }

    void G0(View view, int i10) {
        int i11;
        int i12;
        if (i10 == 4) {
            i11 = this.A0;
        } else if (i10 == 6) {
            int i13 = this.f6577y0;
            if (!this.f6562j0 || i13 > (i12 = this.f6576x0)) {
                i11 = i13;
            } else {
                i10 = 3;
                i11 = i12;
            }
        } else if (i10 == 3) {
            i11 = G();
        } else if (this.C0 && i10 == 5) {
            i11 = this.L0;
        } else {
            throw new IllegalArgumentException("Illegal state argument: " + i10);
        }
        I0(view, i10, i11, false);
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    @SuppressLint({"WrongConstant"})
    public int I() {
        return this.F0;
    }

    void I0(View view, int i10, int i11, boolean z10) {
        boolean R;
        if (z10) {
            R = this.G0.P(view.getLeft(), i11);
        } else {
            R = this.G0.R(view, view.getLeft(), i11);
        }
        if (R) {
            g0(2);
            p0(i10);
            if (this.f6573u0 == null) {
                this.f6573u0 = new f(view, i10);
            }
            if (!((f) this.f6573u0).f6592f) {
                COUIGuideBehavior<V>.f fVar = this.f6573u0;
                fVar.f6593g = i10;
                ViewCompat.c0(view, fVar);
                ((f) this.f6573u0).f6592f = true;
                return;
            }
            this.f6573u0.f6593g = i10;
            return;
        }
        g0(i10);
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public boolean L() {
        return this.f6570r0;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void T(boolean z10) {
        this.E0 = z10;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void U(int i10) {
        if (i10 >= 0) {
            this.f6575w0 = i10;
            return;
        }
        throw new IllegalArgumentException("offset must be greater than or equal to 0");
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void V(boolean z10) {
        if (this.f6562j0 == z10) {
            return;
        }
        this.f6562j0 = z10;
        if (this.M0 != null) {
            w();
        }
        g0((this.f6562j0 && this.F0 == 6) ? 3 : this.F0);
        o0();
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void W(boolean z10) {
        this.f6570r0 = z10;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void X(float f10) {
        if (f10 > 0.0f && f10 < 1.0f) {
            this.f6578z0 = f10;
            if (this.M0 != null) {
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
        if (this.C0 != z10) {
            this.C0 = z10;
            if (!z10 && this.F0 == 5) {
                f0(4);
            }
            o0();
        }
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void b0(int i10) {
        E0(i10, false);
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void d0(int i10) {
        this.f6561i0 = i10;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void e0(boolean z10) {
        this.D0 = z10;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior
    public void f0(int i10) {
        if (i10 == this.F0) {
            return;
        }
        if (this.M0 == null) {
            if (i10 == 4 || i10 == 3 || i10 == 6 || (this.C0 && i10 == 5)) {
                this.F0 = i10;
                return;
            }
            return;
        }
        H0(i10);
    }

    void g0(int i10) {
        V v7;
        if (this.F0 == i10) {
            return;
        }
        this.F0 = i10;
        WeakReference<V> weakReference = this.M0;
        if (weakReference == null || (v7 = weakReference.get()) == null) {
            return;
        }
        if (i10 == 3) {
            q0(true);
        } else if (i10 == 6 || i10 == 5 || i10 == 4) {
            q0(false);
        }
        p0(i10);
        for (int i11 = 0; i11 < this.O0.size(); i11++) {
            this.O0.get(i11).b(v7, i10);
        }
        o0();
    }

    boolean k0(View view, float f10) {
        if (this.D0) {
            return true;
        }
        if (view.getTop() < this.A0) {
            return false;
        }
        return Math.abs((((float) view.getTop()) + (f10 * 0.1f)) - ((float) this.A0)) / ((float) y()) > 0.5f;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onAttachedToLayoutParams(CoordinatorLayout.e eVar) {
        super.onAttachedToLayoutParams(eVar);
        this.M0 = null;
        this.G0 = null;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams();
        this.M0 = null;
        this.G0 = null;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, V v7, MotionEvent motionEvent) {
        ViewDragHelper viewDragHelper;
        if (v7.isShown() && this.E0) {
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 0) {
                Q();
            }
            if (this.P0 == null) {
                this.P0 = VelocityTracker.obtain();
            }
            this.P0.addMovement(motionEvent);
            if (actionMasked == 0) {
                this.R0 = (int) motionEvent.getX();
                this.S0 = (int) motionEvent.getY();
                if (this.F0 != 2) {
                    WeakReference<View> weakReference = this.N0;
                    View view = weakReference != null ? weakReference.get() : null;
                    if (view != null && coordinatorLayout.F(view, this.R0, this.S0)) {
                        this.Q0 = motionEvent.getPointerId(motionEvent.getActionIndex());
                        this.T0 = true;
                    }
                }
                this.H0 = this.Q0 == -1 && !coordinatorLayout.F(v7, this.R0, this.S0);
            } else if (actionMasked == 1) {
                COUIPanelPullUpListener cOUIPanelPullUpListener = this.W0;
                if (cOUIPanelPullUpListener != null) {
                    cOUIPanelPullUpListener.a();
                }
            } else if (actionMasked == 3) {
                this.T0 = false;
                this.Q0 = -1;
                if (this.H0) {
                    this.H0 = false;
                    return false;
                }
            }
            if (!this.H0 && (viewDragHelper = this.G0) != null && viewDragHelper.Q(motionEvent)) {
                return true;
            }
            if (Math.abs(this.S0 - motionEvent.getY()) > Math.abs(this.R0 - motionEvent.getX()) * 2.0f && this.G0 != null && Math.abs(this.S0 - motionEvent.getY()) > this.G0.A()) {
                return true;
            }
            WeakReference<View> weakReference2 = this.N0;
            View view2 = weakReference2 != null ? weakReference2.get() : null;
            return (actionMasked != 2 || view2 == null || this.H0 || this.F0 == 1 || coordinatorLayout.F(view2, (int) motionEvent.getX(), (int) motionEvent.getY()) || this.G0 == null || Math.abs(((float) this.S0) - motionEvent.getY()) <= ((float) this.G0.A())) ? false : true;
        }
        this.H0 = true;
        return false;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, V v7, int i10) {
        MaterialShapeDrawable materialShapeDrawable;
        if (ViewCompat.u(coordinatorLayout) && !ViewCompat.u(v7)) {
            v7.setFitsSystemWindows(true);
        }
        if (this.M0 == null) {
            this.f6567o0 = coordinatorLayout.getResources().getDimensionPixelSize(R$dimen.design_bottom_sheet_peek_height_min);
            F0(coordinatorLayout);
            this.M0 = new WeakReference<>(v7);
            if (this.f6568p0 && (materialShapeDrawable = this.f6569q0) != null) {
                ViewCompat.p0(v7, materialShapeDrawable);
            }
            MaterialShapeDrawable materialShapeDrawable2 = this.f6569q0;
            if (materialShapeDrawable2 != null) {
                float f10 = this.B0;
                if (f10 == -1.0f) {
                    f10 = ViewCompat.t(v7);
                }
                materialShapeDrawable2.Z(f10);
                boolean z10 = this.F0 == 3;
                this.f6572t0 = z10;
                this.f6569q0.b0(z10 ? 0.0f : 1.0f);
            }
            o0();
            if (ViewCompat.v(v7) == 0) {
                ViewCompat.w0(v7, 1);
            }
        }
        if (this.G0 == null) {
            this.G0 = ViewDragHelper.p(coordinatorLayout, this.Z0);
        }
        int top = v7.getTop();
        coordinatorLayout.M(v7, i10);
        this.K0 = coordinatorLayout.getWidth();
        int height = coordinatorLayout.getHeight();
        this.L0 = height;
        if (!this.X0) {
            this.f6576x0 = Math.max(0, height - v7.getHeight());
        }
        this.X0 = false;
        x();
        w();
        int i11 = this.F0;
        if (i11 == 3) {
            ViewCompat.W(v7, G());
        } else if (i11 == 6) {
            ViewCompat.W(v7, this.f6577y0);
        } else if (this.C0 && i11 == 5) {
            ViewCompat.W(v7, this.L0);
        } else if (i11 == 4) {
            ViewCompat.W(v7, this.A0);
        } else if (i11 == 1 || i11 == 2) {
            ViewCompat.W(v7, top - v7.getTop());
        }
        this.N0 = new WeakReference<>(D(v7));
        return true;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, V v7, View view, float f10, float f11) {
        WeakReference<View> weakReference = this.N0;
        if (weakReference == null || view != weakReference.get()) {
            return false;
        }
        return this.F0 != 3 || super.onNestedPreFling(coordinatorLayout, v7, view, f10, f11);
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, V v7, View view, int i10, int i11, int[] iArr, int i12) {
        if (i12 == 1) {
            return;
        }
        WeakReference<View> weakReference = this.N0;
        if (view != (weakReference != null ? weakReference.get() : null)) {
            return;
        }
        int top = v7.getTop();
        int i13 = top - i11;
        if (i11 > 0) {
            if (i13 < G()) {
                iArr[1] = top - G();
                ViewCompat.W(v7, -iArr[1]);
                g0(3);
            } else {
                if (!this.E0) {
                    return;
                }
                iArr[1] = i11;
                ViewCompat.W(v7, -i11);
                g0(1);
            }
        } else if (i11 < 0 && !view.canScrollVertically(-1)) {
            int i14 = this.A0;
            if (i13 > i14 && !this.C0) {
                iArr[1] = top - i14;
                ViewCompat.W(v7, -iArr[1]);
                g0(4);
            } else {
                if (!this.E0) {
                    return;
                }
                iArr[1] = i11;
                ViewCompat.W(v7, -i11);
                g0(1);
            }
        }
        C(v7.getTop());
        this.I0 = i11;
        this.J0 = true;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, V v7, View view, int i10, int i11, int i12, int i13, int i14, int[] iArr) {
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onRestoreInstanceState(CoordinatorLayout coordinatorLayout, V v7, Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(coordinatorLayout, v7, savedState.getSuperState());
        D0(savedState);
        int i10 = savedState.f6579e;
        if (i10 != 1 && i10 != 2) {
            this.F0 = i10;
        } else {
            this.F0 = 4;
        }
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public Parcelable onSaveInstanceState(CoordinatorLayout coordinatorLayout, V v7) {
        return new SavedState(super.onSaveInstanceState(coordinatorLayout, v7), (COUIGuideBehavior<?>) this);
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V v7, View view, View view2, int i10, int i11) {
        this.I0 = 0;
        this.J0 = false;
        return (i10 & 2) != 0;
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, V v7, View view, int i10) {
        int i11;
        int i12 = 3;
        if (v7.getTop() == G()) {
            g0(3);
            return;
        }
        WeakReference<View> weakReference = this.N0;
        if (weakReference != null && view == weakReference.get() && this.J0) {
            if (this.I0 > 0) {
                if (this.f6562j0) {
                    i11 = this.f6576x0;
                } else {
                    int top = v7.getTop();
                    int i13 = this.f6577y0;
                    if (top > i13) {
                        i11 = i13;
                        i12 = 6;
                    } else {
                        i11 = this.f6575w0;
                    }
                }
            } else if (this.C0 && k0(v7, K())) {
                COUIPanelDragListener cOUIPanelDragListener = this.V0;
                if (cOUIPanelDragListener != null && cOUIPanelDragListener.a()) {
                    i11 = this.f6576x0;
                    this.Y0 = false;
                } else {
                    i11 = this.L0;
                    i12 = 5;
                    this.Y0 = true;
                }
            } else if (this.I0 == 0) {
                int top2 = v7.getTop();
                if (this.f6562j0) {
                    if (Math.abs(top2 - this.f6576x0) < Math.abs(top2 - this.A0)) {
                        i11 = this.f6576x0;
                    } else {
                        i11 = this.A0;
                        i12 = 4;
                    }
                } else {
                    int i14 = this.f6577y0;
                    if (top2 < i14) {
                        if (top2 < Math.abs(top2 - this.A0)) {
                            i11 = this.f6575w0;
                        } else {
                            i11 = this.f6577y0;
                        }
                    } else if (Math.abs(top2 - i14) < Math.abs(top2 - this.A0)) {
                        i11 = this.f6577y0;
                    } else {
                        i11 = this.A0;
                        i12 = 4;
                    }
                    i12 = 6;
                }
            } else {
                if (this.f6562j0) {
                    i11 = this.A0;
                } else {
                    int top3 = v7.getTop();
                    if (Math.abs(top3 - this.f6577y0) < Math.abs(top3 - this.A0)) {
                        i11 = this.f6577y0;
                        i12 = 6;
                    } else {
                        i11 = this.A0;
                    }
                }
                i12 = 4;
            }
            I0(v7, i12, i11, false);
            this.J0 = false;
        }
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onTouchEvent(CoordinatorLayout coordinatorLayout, V v7, MotionEvent motionEvent) {
        if (!v7.isShown()) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (this.F0 == 1 && actionMasked == 0) {
            return true;
        }
        ViewDragHelper viewDragHelper = this.G0;
        if (viewDragHelper != null) {
            viewDragHelper.G(motionEvent);
        }
        if (actionMasked == 0) {
            Q();
        }
        if (this.P0 == null) {
            this.P0 = VelocityTracker.obtain();
        }
        this.P0.addMovement(motionEvent);
        if (actionMasked == 2 && !this.H0 && this.G0 != null && Math.abs(this.S0 - motionEvent.getY()) > this.G0.A()) {
            this.G0.c(v7, motionEvent.getPointerId(motionEvent.getActionIndex()));
        }
        return !this.H0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        final int f6579e;

        /* renamed from: f, reason: collision with root package name */
        int f6580f;

        /* renamed from: g, reason: collision with root package name */
        boolean f6581g;

        /* renamed from: h, reason: collision with root package name */
        boolean f6582h;

        /* renamed from: i, reason: collision with root package name */
        boolean f6583i;

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
            this.f6579e = parcel.readInt();
            this.f6580f = parcel.readInt();
            this.f6581g = parcel.readInt() == 1;
            this.f6582h = parcel.readInt() == 1;
            this.f6583i = parcel.readInt() == 1;
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeInt(this.f6579e);
            parcel.writeInt(this.f6580f);
            parcel.writeInt(this.f6581g ? 1 : 0);
            parcel.writeInt(this.f6582h ? 1 : 0);
            parcel.writeInt(this.f6583i ? 1 : 0);
        }

        public SavedState(Parcelable parcelable, COUIGuideBehavior<?> cOUIGuideBehavior) {
            super(parcelable);
            this.f6579e = cOUIGuideBehavior.F0;
            this.f6580f = ((COUIGuideBehavior) cOUIGuideBehavior).f6565m0;
            this.f6581g = ((COUIGuideBehavior) cOUIGuideBehavior).f6562j0;
            this.f6582h = cOUIGuideBehavior.C0;
            this.f6583i = ((COUIGuideBehavior) cOUIGuideBehavior).D0;
        }
    }

    public COUIGuideBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        int i10;
        this.f6561i0 = 0;
        this.f6562j0 = true;
        this.f6563k0 = false;
        this.f6573u0 = null;
        this.f6578z0 = 0.5f;
        this.B0 = -1.0f;
        this.E0 = true;
        this.F0 = 4;
        this.O0 = new ArrayList<>();
        this.Z0 = new c();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.BottomSheetBehavior_Layout);
        this.f6568p0 = obtainStyledAttributes.hasValue(R$styleable.BottomSheetBehavior_Layout_shapeAppearance);
        int i11 = R$styleable.BottomSheetBehavior_Layout_backgroundTint;
        boolean hasValue = obtainStyledAttributes.hasValue(i11);
        if (hasValue) {
            C0(context, attributeSet, hasValue, MaterialResources.a(context, obtainStyledAttributes, i11));
        } else {
            B0(context, attributeSet, hasValue);
        }
        B();
        this.B0 = obtainStyledAttributes.getDimension(R$styleable.BottomSheetBehavior_Layout_android_elevation, -1.0f);
        int i12 = R$styleable.BottomSheetBehavior_Layout_behavior_peekHeight;
        TypedValue peekValue = obtainStyledAttributes.peekValue(i12);
        if (peekValue != null && (i10 = peekValue.data) == -1) {
            b0(i10);
        } else {
            b0(obtainStyledAttributes.getDimensionPixelSize(i12, -1));
        }
        Y(obtainStyledAttributes.getBoolean(R$styleable.BottomSheetBehavior_Layout_behavior_hideable, false));
        W(obtainStyledAttributes.getBoolean(R$styleable.BottomSheetBehavior_Layout_gestureInsetBottomIgnored, false));
        V(obtainStyledAttributes.getBoolean(R$styleable.BottomSheetBehavior_Layout_behavior_fitToContents, true));
        e0(obtainStyledAttributes.getBoolean(R$styleable.BottomSheetBehavior_Layout_behavior_skipCollapsed, false));
        T(obtainStyledAttributes.getBoolean(R$styleable.BottomSheetBehavior_Layout_behavior_draggable, true));
        d0(obtainStyledAttributes.getInt(R$styleable.BottomSheetBehavior_Layout_behavior_saveFlags, 0));
        X(obtainStyledAttributes.getFloat(R$styleable.BottomSheetBehavior_Layout_behavior_halfExpandedRatio, 0.5f));
        int i13 = R$styleable.BottomSheetBehavior_Layout_behavior_expandedOffset;
        TypedValue peekValue2 = obtainStyledAttributes.peekValue(i13);
        if (peekValue2 != null && peekValue2.type == 16) {
            U(peekValue2.data);
        } else {
            U(obtainStyledAttributes.getDimensionPixelOffset(i13, 0));
        }
        obtainStyledAttributes.recycle();
        this.f6564l0 = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        this.Y0 = false;
    }
}
