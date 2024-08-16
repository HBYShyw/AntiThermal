package androidx.drawerlayout.widget;

import android.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.customview.view.AbsSavedState;
import androidx.customview.widget.ViewDragHelper;
import androidx.drawerlayout.R$attr;
import androidx.drawerlayout.R$dimen;
import androidx.drawerlayout.R$styleable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class DrawerLayout extends ViewGroup {
    private static final int[] P = {R.attr.colorPrimaryDark};
    static final int[] Q = {R.attr.layout_gravity};
    static final boolean R = true;
    private static final boolean S = true;
    private static boolean T = true;
    private Drawable A;
    private Drawable B;
    private Drawable C;
    private CharSequence D;
    private CharSequence E;
    private Object F;
    private boolean G;
    private Drawable H;
    private Drawable I;
    private Drawable J;
    private Drawable K;
    private final ArrayList<View> L;
    private Rect M;
    private Matrix N;
    private final AccessibilityViewCommand O;

    /* renamed from: e, reason: collision with root package name */
    private final d f2530e;

    /* renamed from: f, reason: collision with root package name */
    private float f2531f;

    /* renamed from: g, reason: collision with root package name */
    private int f2532g;

    /* renamed from: h, reason: collision with root package name */
    private int f2533h;

    /* renamed from: i, reason: collision with root package name */
    private float f2534i;

    /* renamed from: j, reason: collision with root package name */
    private Paint f2535j;

    /* renamed from: k, reason: collision with root package name */
    private final ViewDragHelper f2536k;

    /* renamed from: l, reason: collision with root package name */
    private final ViewDragHelper f2537l;

    /* renamed from: m, reason: collision with root package name */
    private final f f2538m;

    /* renamed from: n, reason: collision with root package name */
    private final f f2539n;

    /* renamed from: o, reason: collision with root package name */
    private int f2540o;

    /* renamed from: p, reason: collision with root package name */
    private boolean f2541p;

    /* renamed from: q, reason: collision with root package name */
    private boolean f2542q;

    /* renamed from: r, reason: collision with root package name */
    private int f2543r;

    /* renamed from: s, reason: collision with root package name */
    private int f2544s;

    /* renamed from: t, reason: collision with root package name */
    private int f2545t;

    /* renamed from: u, reason: collision with root package name */
    private int f2546u;

    /* renamed from: v, reason: collision with root package name */
    private boolean f2547v;

    /* renamed from: w, reason: collision with root package name */
    private e f2548w;

    /* renamed from: x, reason: collision with root package name */
    private List<e> f2549x;

    /* renamed from: y, reason: collision with root package name */
    private float f2550y;

    /* renamed from: z, reason: collision with root package name */
    private float f2551z;

    /* loaded from: classes.dex */
    class a implements AccessibilityViewCommand {
        a() {
        }

        @Override // androidx.core.view.accessibility.AccessibilityViewCommand
        public boolean perform(View view, AccessibilityViewCommand.a aVar) {
            if (!DrawerLayout.this.A(view) || DrawerLayout.this.p(view) == 2) {
                return false;
            }
            DrawerLayout.this.d(view);
            return true;
        }
    }

    /* loaded from: classes.dex */
    class b implements View.OnApplyWindowInsetsListener {
        b() {
        }

        @Override // android.view.View.OnApplyWindowInsetsListener
        public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
            ((DrawerLayout) view).M(windowInsets, windowInsets.getSystemWindowInsetTop() > 0);
            return windowInsets.consumeSystemWindowInsets();
        }
    }

    /* loaded from: classes.dex */
    class c extends AccessibilityDelegateCompat {

        /* renamed from: a, reason: collision with root package name */
        private final Rect f2563a = new Rect();

        c() {
        }

        private void a(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, ViewGroup viewGroup) {
            int childCount = viewGroup.getChildCount();
            for (int i10 = 0; i10 < childCount; i10++) {
                View childAt = viewGroup.getChildAt(i10);
                if (DrawerLayout.y(childAt)) {
                    accessibilityNodeInfoCompat.c(childAt);
                }
            }
        }

        private void b(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2) {
            Rect rect = this.f2563a;
            accessibilityNodeInfoCompat2.k(rect);
            accessibilityNodeInfoCompat.R(rect);
            accessibilityNodeInfoCompat.A0(accessibilityNodeInfoCompat2.I());
            accessibilityNodeInfoCompat.l0(accessibilityNodeInfoCompat2.r());
            accessibilityNodeInfoCompat.V(accessibilityNodeInfoCompat2.m());
            accessibilityNodeInfoCompat.Z(accessibilityNodeInfoCompat2.o());
            accessibilityNodeInfoCompat.b0(accessibilityNodeInfoCompat2.A());
            accessibilityNodeInfoCompat.e0(accessibilityNodeInfoCompat2.C());
            accessibilityNodeInfoCompat.P(accessibilityNodeInfoCompat2.w());
            accessibilityNodeInfoCompat.t0(accessibilityNodeInfoCompat2.G());
            accessibilityNodeInfoCompat.a(accessibilityNodeInfoCompat2.i());
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            if (accessibilityEvent.getEventType() == 32) {
                List<CharSequence> text = accessibilityEvent.getText();
                View n10 = DrawerLayout.this.n();
                if (n10 == null) {
                    return true;
                }
                CharSequence q10 = DrawerLayout.this.q(DrawerLayout.this.r(n10));
                if (q10 == null) {
                    return true;
                }
                text.add(q10);
                return true;
            }
            return super.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            accessibilityEvent.setClassName("androidx.drawerlayout.widget.DrawerLayout");
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            if (DrawerLayout.R) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            } else {
                AccessibilityNodeInfoCompat L = AccessibilityNodeInfoCompat.L(accessibilityNodeInfoCompat);
                super.onInitializeAccessibilityNodeInfo(view, L);
                accessibilityNodeInfoCompat.v0(view);
                Object D = ViewCompat.D(view);
                if (D instanceof View) {
                    accessibilityNodeInfoCompat.n0((View) D);
                }
                b(accessibilityNodeInfoCompat, L);
                L.N();
                a(accessibilityNodeInfoCompat, (ViewGroup) view);
            }
            accessibilityNodeInfoCompat.V("androidx.drawerlayout.widget.DrawerLayout");
            accessibilityNodeInfoCompat.d0(false);
            accessibilityNodeInfoCompat.e0(false);
            accessibilityNodeInfoCompat.O(AccessibilityNodeInfoCompat.a.f2318e);
            accessibilityNodeInfoCompat.O(AccessibilityNodeInfoCompat.a.f2319f);
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            if (DrawerLayout.R || DrawerLayout.y(view)) {
                return super.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
            }
            return false;
        }
    }

    /* loaded from: classes.dex */
    static final class d extends AccessibilityDelegateCompat {
        d() {
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            if (DrawerLayout.y(view)) {
                return;
            }
            accessibilityNodeInfoCompat.n0(null);
        }
    }

    /* loaded from: classes.dex */
    public interface e {
        void a(View view);

        void b(View view);

        void c(int i10);

        void d(View view, float f10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class f extends ViewDragHelper.c {

        /* renamed from: a, reason: collision with root package name */
        private final int f2565a;

        /* renamed from: b, reason: collision with root package name */
        private ViewDragHelper f2566b;

        /* renamed from: c, reason: collision with root package name */
        private final Runnable f2567c = new a();

        /* loaded from: classes.dex */
        class a implements Runnable {
            a() {
            }

            @Override // java.lang.Runnable
            public void run() {
                f.this.o();
            }
        }

        f(int i10) {
            this.f2565a = i10;
        }

        private void n() {
            View l10 = DrawerLayout.this.l(this.f2565a == 3 ? 5 : 3);
            if (l10 != null) {
                DrawerLayout.this.d(l10);
            }
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public int a(View view, int i10, int i11) {
            if (DrawerLayout.this.c(view, 3)) {
                return Math.max(-view.getWidth(), Math.min(i10, 0));
            }
            int width = DrawerLayout.this.getWidth();
            return Math.max(width - view.getWidth(), Math.min(i10, width));
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public int b(View view, int i10, int i11) {
            return view.getTop();
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public int d(View view) {
            if (DrawerLayout.this.B(view)) {
                return view.getWidth();
            }
            return 0;
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public void f(int i10, int i11) {
            View l10;
            if ((i10 & 1) == 1) {
                l10 = DrawerLayout.this.l(3);
            } else {
                l10 = DrawerLayout.this.l(5);
            }
            if (l10 == null || DrawerLayout.this.p(l10) != 0) {
                return;
            }
            this.f2566b.c(l10, i11);
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public boolean g(int i10) {
            return false;
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public void h(int i10, int i11) {
            DrawerLayout.this.postDelayed(this.f2567c, 160L);
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public void i(View view, int i10) {
            ((LayoutParams) view.getLayoutParams()).f2554c = false;
            n();
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public void j(int i10) {
            DrawerLayout.this.R(i10, this.f2566b.w());
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public void k(View view, int i10, int i11, int i12, int i13) {
            float width = (DrawerLayout.this.c(view, 3) ? i10 + r3 : DrawerLayout.this.getWidth() - i10) / view.getWidth();
            DrawerLayout.this.O(view, width);
            view.setVisibility(width == 0.0f ? 4 : 0);
            DrawerLayout.this.invalidate();
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public void l(View view, float f10, float f11) {
            int i10;
            float s7 = DrawerLayout.this.s(view);
            int width = view.getWidth();
            if (DrawerLayout.this.c(view, 3)) {
                i10 = (f10 > 0.0f || (f10 == 0.0f && s7 > 0.5f)) ? 0 : -width;
            } else {
                int width2 = DrawerLayout.this.getWidth();
                if (f10 < 0.0f || (f10 == 0.0f && s7 > 0.5f)) {
                    width2 -= width;
                }
                i10 = width2;
            }
            this.f2566b.P(i10, view.getTop());
            DrawerLayout.this.invalidate();
        }

        @Override // androidx.customview.widget.ViewDragHelper.c
        public boolean m(View view, int i10) {
            return DrawerLayout.this.B(view) && DrawerLayout.this.c(view, this.f2565a) && DrawerLayout.this.p(view) == 0;
        }

        void o() {
            View l10;
            int width;
            int y4 = this.f2566b.y();
            boolean z10 = this.f2565a == 3;
            if (z10) {
                l10 = DrawerLayout.this.l(3);
                width = (l10 != null ? -l10.getWidth() : 0) + y4;
            } else {
                l10 = DrawerLayout.this.l(5);
                width = DrawerLayout.this.getWidth() - y4;
            }
            if (l10 != null) {
                if (((!z10 || l10.getLeft() >= width) && (z10 || l10.getLeft() <= width)) || DrawerLayout.this.p(l10) != 0) {
                    return;
                }
                LayoutParams layoutParams = (LayoutParams) l10.getLayoutParams();
                this.f2566b.R(l10, width, l10.getTop());
                layoutParams.f2554c = true;
                DrawerLayout.this.invalidate();
                n();
                DrawerLayout.this.b();
            }
        }

        public void p() {
            DrawerLayout.this.removeCallbacks(this.f2567c);
        }

        public void q(ViewDragHelper viewDragHelper) {
            this.f2566b = viewDragHelper;
        }
    }

    public DrawerLayout(Context context) {
        this(context, null);
    }

    private boolean D(float f10, float f11, View view) {
        if (this.M == null) {
            this.M = new Rect();
        }
        view.getHitRect(this.M);
        return this.M.contains((int) f10, (int) f11);
    }

    private void E(Drawable drawable, int i10) {
        if (drawable == null || !DrawableCompat.c(drawable)) {
            return;
        }
        DrawableCompat.g(drawable, i10);
    }

    private Drawable J() {
        int x10 = ViewCompat.x(this);
        if (x10 == 0) {
            Drawable drawable = this.H;
            if (drawable != null) {
                E(drawable, x10);
                return this.H;
            }
        } else {
            Drawable drawable2 = this.I;
            if (drawable2 != null) {
                E(drawable2, x10);
                return this.I;
            }
        }
        return this.J;
    }

    private Drawable K() {
        int x10 = ViewCompat.x(this);
        if (x10 == 0) {
            Drawable drawable = this.I;
            if (drawable != null) {
                E(drawable, x10);
                return this.I;
            }
        } else {
            Drawable drawable2 = this.H;
            if (drawable2 != null) {
                E(drawable2, x10);
                return this.H;
            }
        }
        return this.K;
    }

    private void L() {
        if (S) {
            return;
        }
        this.B = J();
        this.C = K();
    }

    private void P(View view) {
        AccessibilityNodeInfoCompat.a aVar = AccessibilityNodeInfoCompat.a.f2338y;
        ViewCompat.e0(view, aVar.b());
        if (!A(view) || p(view) == 2) {
            return;
        }
        ViewCompat.g0(view, aVar, null, this.O);
    }

    private void Q(View view, boolean z10) {
        int childCount = getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = getChildAt(i10);
            if ((!z10 && !B(childAt)) || (z10 && childAt == view)) {
                ViewCompat.w0(childAt, 1);
            } else {
                ViewCompat.w0(childAt, 4);
            }
        }
    }

    private boolean k(MotionEvent motionEvent, View view) {
        if (!view.getMatrix().isIdentity()) {
            MotionEvent t7 = t(motionEvent, view);
            boolean dispatchGenericMotionEvent = view.dispatchGenericMotionEvent(t7);
            t7.recycle();
            return dispatchGenericMotionEvent;
        }
        float scrollX = getScrollX() - view.getLeft();
        float scrollY = getScrollY() - view.getTop();
        motionEvent.offsetLocation(scrollX, scrollY);
        boolean dispatchGenericMotionEvent2 = view.dispatchGenericMotionEvent(motionEvent);
        motionEvent.offsetLocation(-scrollX, -scrollY);
        return dispatchGenericMotionEvent2;
    }

    private MotionEvent t(MotionEvent motionEvent, View view) {
        float scrollX = getScrollX() - view.getLeft();
        float scrollY = getScrollY() - view.getTop();
        MotionEvent obtain = MotionEvent.obtain(motionEvent);
        obtain.offsetLocation(scrollX, scrollY);
        Matrix matrix = view.getMatrix();
        if (!matrix.isIdentity()) {
            if (this.N == null) {
                this.N = new Matrix();
            }
            matrix.invert(this.N);
            obtain.transform(this.N);
        }
        return obtain;
    }

    static String u(int i10) {
        return (i10 & 3) == 3 ? "LEFT" : (i10 & 5) == 5 ? "RIGHT" : Integer.toHexString(i10);
    }

    private static boolean v(View view) {
        Drawable background = view.getBackground();
        return background != null && background.getOpacity() == -1;
    }

    private boolean w() {
        int childCount = getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            if (((LayoutParams) getChildAt(i10).getLayoutParams()).f2554c) {
                return true;
            }
        }
        return false;
    }

    private boolean x() {
        return n() != null;
    }

    static boolean y(View view) {
        return (ViewCompat.v(view) == 4 || ViewCompat.v(view) == 2) ? false : true;
    }

    public boolean A(View view) {
        if (B(view)) {
            return (((LayoutParams) view.getLayoutParams()).f2555d & 1) == 1;
        }
        throw new IllegalArgumentException("View " + view + " is not a drawer");
    }

    boolean B(View view) {
        int b10 = GravityCompat.b(((LayoutParams) view.getLayoutParams()).f2552a, ViewCompat.x(view));
        return ((b10 & 3) == 0 && (b10 & 5) == 0) ? false : true;
    }

    public boolean C(View view) {
        if (B(view)) {
            return ((LayoutParams) view.getLayoutParams()).f2553b > 0.0f;
        }
        throw new IllegalArgumentException("View " + view + " is not a drawer");
    }

    void F(View view, float f10) {
        float s7 = s(view);
        float width = view.getWidth();
        int i10 = ((int) (width * f10)) - ((int) (s7 * width));
        if (!c(view, 3)) {
            i10 = -i10;
        }
        view.offsetLeftAndRight(i10);
        O(view, f10);
    }

    public void G(View view) {
        H(view, true);
    }

    public void H(View view, boolean z10) {
        if (B(view)) {
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            if (this.f2542q) {
                layoutParams.f2553b = 1.0f;
                layoutParams.f2555d = 1;
                Q(view, true);
                P(view);
            } else if (z10) {
                layoutParams.f2555d |= 2;
                if (c(view, 3)) {
                    this.f2536k.R(view, 0, view.getTop());
                } else {
                    this.f2537l.R(view, getWidth() - view.getWidth(), view.getTop());
                }
            } else {
                F(view, 1.0f);
                R(0, view);
                view.setVisibility(0);
            }
            invalidate();
            return;
        }
        throw new IllegalArgumentException("View " + view + " is not a sliding drawer");
    }

    public void I(e eVar) {
        List<e> list;
        if (eVar == null || (list = this.f2549x) == null) {
            return;
        }
        list.remove(eVar);
    }

    public void M(Object obj, boolean z10) {
        this.F = obj;
        this.G = z10;
        setWillNotDraw(!z10 && getBackground() == null);
        requestLayout();
    }

    public void N(int i10, int i11) {
        View l10;
        int b10 = GravityCompat.b(i11, ViewCompat.x(this));
        if (i11 == 3) {
            this.f2543r = i10;
        } else if (i11 == 5) {
            this.f2544s = i10;
        } else if (i11 == 8388611) {
            this.f2545t = i10;
        } else if (i11 == 8388613) {
            this.f2546u = i10;
        }
        if (i10 != 0) {
            (b10 == 3 ? this.f2536k : this.f2537l).b();
        }
        if (i10 != 1) {
            if (i10 == 2 && (l10 = l(b10)) != null) {
                G(l10);
                return;
            }
            return;
        }
        View l11 = l(b10);
        if (l11 != null) {
            d(l11);
        }
    }

    void O(View view, float f10) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        if (f10 == layoutParams.f2553b) {
            return;
        }
        layoutParams.f2553b = f10;
        j(view, f10);
    }

    void R(int i10, View view) {
        int B = this.f2536k.B();
        int B2 = this.f2537l.B();
        int i11 = 2;
        if (B == 1 || B2 == 1) {
            i11 = 1;
        } else if (B != 2 && B2 != 2) {
            i11 = 0;
        }
        if (view != null && i10 == 0) {
            float f10 = ((LayoutParams) view.getLayoutParams()).f2553b;
            if (f10 == 0.0f) {
                h(view);
            } else if (f10 == 1.0f) {
                i(view);
            }
        }
        if (i11 != this.f2540o) {
            this.f2540o = i11;
            List<e> list = this.f2549x;
            if (list != null) {
                for (int size = list.size() - 1; size >= 0; size--) {
                    this.f2549x.get(size).c(i11);
                }
            }
        }
    }

    public void a(e eVar) {
        if (eVar == null) {
            return;
        }
        if (this.f2549x == null) {
            this.f2549x = new ArrayList();
        }
        this.f2549x.add(eVar);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void addFocusables(ArrayList<View> arrayList, int i10, int i11) {
        if (getDescendantFocusability() == 393216) {
            return;
        }
        int childCount = getChildCount();
        boolean z10 = false;
        for (int i12 = 0; i12 < childCount; i12++) {
            View childAt = getChildAt(i12);
            if (B(childAt)) {
                if (A(childAt)) {
                    childAt.addFocusables(arrayList, i10, i11);
                    z10 = true;
                }
            } else {
                this.L.add(childAt);
            }
        }
        if (!z10) {
            int size = this.L.size();
            for (int i13 = 0; i13 < size; i13++) {
                View view = this.L.get(i13);
                if (view.getVisibility() == 0) {
                    view.addFocusables(arrayList, i10, i11);
                }
            }
        }
        this.L.clear();
    }

    @Override // android.view.ViewGroup
    public void addView(View view, int i10, ViewGroup.LayoutParams layoutParams) {
        super.addView(view, i10, layoutParams);
        if (m() == null && !B(view)) {
            ViewCompat.w0(view, 1);
        } else {
            ViewCompat.w0(view, 4);
        }
        if (R) {
            return;
        }
        ViewCompat.l0(view, this.f2530e);
    }

    void b() {
        if (this.f2547v) {
            return;
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
        int childCount = getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            getChildAt(i10).dispatchTouchEvent(obtain);
        }
        obtain.recycle();
        this.f2547v = true;
    }

    boolean c(View view, int i10) {
        return (r(view) & i10) == i10;
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return (layoutParams instanceof LayoutParams) && super.checkLayoutParams(layoutParams);
    }

    @Override // android.view.View
    public void computeScroll() {
        int childCount = getChildCount();
        float f10 = 0.0f;
        for (int i10 = 0; i10 < childCount; i10++) {
            f10 = Math.max(f10, ((LayoutParams) getChildAt(i10).getLayoutParams()).f2553b);
        }
        this.f2534i = f10;
        boolean n10 = this.f2536k.n(true);
        boolean n11 = this.f2537l.n(true);
        if (n10 || n11) {
            ViewCompat.b0(this);
        }
    }

    public void d(View view) {
        e(view, true);
    }

    @Override // android.view.View
    public boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
        if ((motionEvent.getSource() & 2) != 0 && motionEvent.getAction() != 10 && this.f2534i > 0.0f) {
            int childCount = getChildCount();
            if (childCount == 0) {
                return false;
            }
            float x10 = motionEvent.getX();
            float y4 = motionEvent.getY();
            for (int i10 = childCount - 1; i10 >= 0; i10--) {
                View childAt = getChildAt(i10);
                if (D(x10, y4, childAt) && !z(childAt) && k(motionEvent, childAt)) {
                    return true;
                }
            }
            return false;
        }
        return super.dispatchGenericMotionEvent(motionEvent);
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j10) {
        int height = getHeight();
        boolean z10 = z(view);
        int width = getWidth();
        int save = canvas.save();
        int i10 = 0;
        if (z10) {
            int childCount = getChildCount();
            int i11 = 0;
            for (int i12 = 0; i12 < childCount; i12++) {
                View childAt = getChildAt(i12);
                if (childAt != view && childAt.getVisibility() == 0 && v(childAt) && B(childAt) && childAt.getHeight() >= height) {
                    if (c(childAt, 3)) {
                        int right = childAt.getRight();
                        if (right > i11) {
                            i11 = right;
                        }
                    } else {
                        int left = childAt.getLeft();
                        if (left < width) {
                            width = left;
                        }
                    }
                }
            }
            canvas.clipRect(i11, 0, width, getHeight());
            i10 = i11;
        }
        boolean drawChild = super.drawChild(canvas, view, j10);
        canvas.restoreToCount(save);
        float f10 = this.f2534i;
        if (f10 > 0.0f && z10) {
            this.f2535j.setColor((this.f2533h & 16777215) | (((int) ((((-16777216) & r2) >>> 24) * f10)) << 24));
            canvas.drawRect(i10, 0.0f, width, getHeight(), this.f2535j);
        } else if (this.B != null && c(view, 3)) {
            int intrinsicWidth = this.B.getIntrinsicWidth();
            int right2 = view.getRight();
            float max = Math.max(0.0f, Math.min(right2 / this.f2536k.y(), 1.0f));
            this.B.setBounds(right2, view.getTop(), intrinsicWidth + right2, view.getBottom());
            this.B.setAlpha((int) (max * 255.0f));
            this.B.draw(canvas);
        } else if (this.C != null && c(view, 5)) {
            int intrinsicWidth2 = this.C.getIntrinsicWidth();
            int left2 = view.getLeft();
            float max2 = Math.max(0.0f, Math.min((getWidth() - left2) / this.f2537l.y(), 1.0f));
            this.C.setBounds(left2 - intrinsicWidth2, view.getTop(), left2, view.getBottom());
            this.C.setAlpha((int) (max2 * 255.0f));
            this.C.draw(canvas);
        }
        return drawChild;
    }

    public void e(View view, boolean z10) {
        if (B(view)) {
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            if (this.f2542q) {
                layoutParams.f2553b = 0.0f;
                layoutParams.f2555d = 0;
            } else if (z10) {
                layoutParams.f2555d |= 4;
                if (c(view, 3)) {
                    this.f2536k.R(view, -view.getWidth(), view.getTop());
                } else {
                    this.f2537l.R(view, getWidth(), view.getTop());
                }
            } else {
                F(view, 0.0f);
                R(0, view);
                view.setVisibility(4);
            }
            invalidate();
            return;
        }
        throw new IllegalArgumentException("View " + view + " is not a sliding drawer");
    }

    public void f() {
        g(false);
    }

    void g(boolean z10) {
        boolean R2;
        int childCount = getChildCount();
        boolean z11 = false;
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = getChildAt(i10);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            if (B(childAt) && (!z10 || layoutParams.f2554c)) {
                int width = childAt.getWidth();
                if (c(childAt, 3)) {
                    R2 = this.f2536k.R(childAt, -width, childAt.getTop());
                } else {
                    R2 = this.f2537l.R(childAt, getWidth(), childAt.getTop());
                }
                z11 |= R2;
                layoutParams.f2554c = false;
            }
        }
        this.f2538m.p();
        this.f2539n.p();
        if (z11) {
            invalidate();
        }
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LayoutParams) {
            return new LayoutParams((LayoutParams) layoutParams);
        }
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams) layoutParams);
        }
        return new LayoutParams(layoutParams);
    }

    public float getDrawerElevation() {
        if (S) {
            return this.f2531f;
        }
        return 0.0f;
    }

    public Drawable getStatusBarBackgroundDrawable() {
        return this.A;
    }

    void h(View view) {
        View rootView;
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        if ((layoutParams.f2555d & 1) == 1) {
            layoutParams.f2555d = 0;
            List<e> list = this.f2549x;
            if (list != null) {
                for (int size = list.size() - 1; size >= 0; size--) {
                    this.f2549x.get(size).b(view);
                }
            }
            Q(view, false);
            P(view);
            if (!hasWindowFocus() || (rootView = getRootView()) == null) {
                return;
            }
            rootView.sendAccessibilityEvent(32);
        }
    }

    void i(View view) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        if ((layoutParams.f2555d & 1) == 0) {
            layoutParams.f2555d = 1;
            List<e> list = this.f2549x;
            if (list != null) {
                for (int size = list.size() - 1; size >= 0; size--) {
                    this.f2549x.get(size).a(view);
                }
            }
            Q(view, true);
            P(view);
            if (hasWindowFocus()) {
                sendAccessibilityEvent(32);
            }
        }
    }

    void j(View view, float f10) {
        List<e> list = this.f2549x;
        if (list != null) {
            for (int size = list.size() - 1; size >= 0; size--) {
                this.f2549x.get(size).d(view, f10);
            }
        }
    }

    View l(int i10) {
        int b10 = GravityCompat.b(i10, ViewCompat.x(this)) & 7;
        int childCount = getChildCount();
        for (int i11 = 0; i11 < childCount; i11++) {
            View childAt = getChildAt(i11);
            if ((r(childAt) & 7) == b10) {
                return childAt;
            }
        }
        return null;
    }

    View m() {
        int childCount = getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = getChildAt(i10);
            if ((((LayoutParams) childAt.getLayoutParams()).f2555d & 1) == 1) {
                return childAt;
            }
        }
        return null;
    }

    View n() {
        int childCount = getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = getChildAt(i10);
            if (B(childAt) && C(childAt)) {
                return childAt;
            }
        }
        return null;
    }

    public int o(int i10) {
        int x10 = ViewCompat.x(this);
        if (i10 == 3) {
            int i11 = this.f2543r;
            if (i11 != 3) {
                return i11;
            }
            int i12 = x10 == 0 ? this.f2545t : this.f2546u;
            if (i12 != 3) {
                return i12;
            }
            return 0;
        }
        if (i10 == 5) {
            int i13 = this.f2544s;
            if (i13 != 3) {
                return i13;
            }
            int i14 = x10 == 0 ? this.f2546u : this.f2545t;
            if (i14 != 3) {
                return i14;
            }
            return 0;
        }
        if (i10 == 8388611) {
            int i15 = this.f2545t;
            if (i15 != 3) {
                return i15;
            }
            int i16 = x10 == 0 ? this.f2543r : this.f2544s;
            if (i16 != 3) {
                return i16;
            }
            return 0;
        }
        if (i10 != 8388613) {
            return 0;
        }
        int i17 = this.f2546u;
        if (i17 != 3) {
            return i17;
        }
        int i18 = x10 == 0 ? this.f2544s : this.f2543r;
        if (i18 != 3) {
            return i18;
        }
        return 0;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.f2542q = true;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.f2542q = true;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!this.G || this.A == null) {
            return;
        }
        Object obj = this.F;
        int systemWindowInsetTop = obj != null ? ((WindowInsets) obj).getSystemWindowInsetTop() : 0;
        if (systemWindowInsetTop > 0) {
            this.A.setBounds(0, 0, getWidth(), systemWindowInsetTop);
            this.A.draw(canvas);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:6:0x001b, code lost:
    
        if (r0 != 3) goto L13;
     */
    @Override // android.view.ViewGroup
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean z10;
        View u7;
        int actionMasked = motionEvent.getActionMasked();
        boolean Q2 = this.f2536k.Q(motionEvent) | this.f2537l.Q(motionEvent);
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    if (this.f2536k.e(3)) {
                        this.f2538m.p();
                        this.f2539n.p();
                    }
                }
                z10 = false;
            }
            g(true);
            this.f2547v = false;
            z10 = false;
        } else {
            float x10 = motionEvent.getX();
            float y4 = motionEvent.getY();
            this.f2550y = x10;
            this.f2551z = y4;
            z10 = this.f2534i > 0.0f && (u7 = this.f2536k.u((int) x10, (int) y4)) != null && z(u7);
            this.f2547v = false;
        }
        return Q2 || z10 || w() || this.f2547v;
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i10, KeyEvent keyEvent) {
        if (i10 == 4 && x()) {
            keyEvent.startTracking();
            return true;
        }
        return super.onKeyDown(i10, keyEvent);
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i10, KeyEvent keyEvent) {
        if (i10 == 4) {
            View n10 = n();
            if (n10 != null && p(n10) == 0) {
                f();
            }
            return n10 != null;
        }
        return super.onKeyUp(i10, keyEvent);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        WindowInsets rootWindowInsets;
        float f10;
        int i14;
        boolean z11 = true;
        this.f2541p = true;
        int i15 = i12 - i10;
        int childCount = getChildCount();
        int i16 = 0;
        while (i16 < childCount) {
            View childAt = getChildAt(i16);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (z(childAt)) {
                    int i17 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin;
                    childAt.layout(i17, ((ViewGroup.MarginLayoutParams) layoutParams).topMargin, childAt.getMeasuredWidth() + i17, ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + childAt.getMeasuredHeight());
                } else {
                    int measuredWidth = childAt.getMeasuredWidth();
                    int measuredHeight = childAt.getMeasuredHeight();
                    if (c(childAt, 3)) {
                        float f11 = measuredWidth;
                        i14 = (-measuredWidth) + ((int) (layoutParams.f2553b * f11));
                        f10 = (measuredWidth + i14) / f11;
                    } else {
                        float f12 = measuredWidth;
                        f10 = (i15 - r11) / f12;
                        i14 = i15 - ((int) (layoutParams.f2553b * f12));
                    }
                    boolean z12 = f10 != layoutParams.f2553b ? z11 : false;
                    int i18 = layoutParams.f2552a & 112;
                    if (i18 == 16) {
                        int i19 = i13 - i11;
                        int i20 = (i19 - measuredHeight) / 2;
                        int i21 = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
                        if (i20 < i21) {
                            i20 = i21;
                        } else {
                            int i22 = i20 + measuredHeight;
                            int i23 = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
                            if (i22 > i19 - i23) {
                                i20 = (i19 - i23) - measuredHeight;
                            }
                        }
                        childAt.layout(i14, i20, measuredWidth + i14, measuredHeight + i20);
                    } else if (i18 != 80) {
                        int i24 = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
                        childAt.layout(i14, i24, measuredWidth + i14, measuredHeight + i24);
                    } else {
                        int i25 = i13 - i11;
                        childAt.layout(i14, (i25 - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin) - childAt.getMeasuredHeight(), measuredWidth + i14, i25 - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin);
                    }
                    if (z12) {
                        O(childAt, f10);
                    }
                    int i26 = layoutParams.f2553b > 0.0f ? 0 : 4;
                    if (childAt.getVisibility() != i26) {
                        childAt.setVisibility(i26);
                    }
                }
            }
            i16++;
            z11 = true;
        }
        if (T && (rootWindowInsets = getRootWindowInsets()) != null) {
            Insets h10 = WindowInsetsCompat.v(rootWindowInsets).h();
            ViewDragHelper viewDragHelper = this.f2536k;
            viewDragHelper.M(Math.max(viewDragHelper.x(), h10.f2185a));
            ViewDragHelper viewDragHelper2 = this.f2537l;
            viewDragHelper2.M(Math.max(viewDragHelper2.x(), h10.f2187c));
        }
        this.f2541p = false;
        this.f2542q = false;
    }

    @Override // android.view.View
    @SuppressLint({"WrongConstant"})
    protected void onMeasure(int i10, int i11) {
        int mode = View.MeasureSpec.getMode(i10);
        int mode2 = View.MeasureSpec.getMode(i11);
        int size = View.MeasureSpec.getSize(i10);
        int size2 = View.MeasureSpec.getSize(i11);
        if (mode != 1073741824 || mode2 != 1073741824) {
            if (!isInEditMode()) {
                throw new IllegalArgumentException("DrawerLayout must be measured with MeasureSpec.EXACTLY.");
            }
            if (mode == 0) {
                size = 300;
            }
            if (mode2 == 0) {
                size2 = 300;
            }
        }
        setMeasuredDimension(size, size2);
        int i12 = 0;
        boolean z10 = this.F != null && ViewCompat.u(this);
        int x10 = ViewCompat.x(this);
        int childCount = getChildCount();
        int i13 = 0;
        boolean z11 = false;
        boolean z12 = false;
        while (i13 < childCount) {
            View childAt = getChildAt(i13);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (z10) {
                    int b10 = GravityCompat.b(layoutParams.f2552a, x10);
                    if (ViewCompat.u(childAt)) {
                        WindowInsets windowInsets = (WindowInsets) this.F;
                        if (b10 == 3) {
                            windowInsets = windowInsets.replaceSystemWindowInsets(windowInsets.getSystemWindowInsetLeft(), windowInsets.getSystemWindowInsetTop(), i12, windowInsets.getSystemWindowInsetBottom());
                        } else if (b10 == 5) {
                            windowInsets = windowInsets.replaceSystemWindowInsets(i12, windowInsets.getSystemWindowInsetTop(), windowInsets.getSystemWindowInsetRight(), windowInsets.getSystemWindowInsetBottom());
                        }
                        childAt.dispatchApplyWindowInsets(windowInsets);
                    } else {
                        WindowInsets windowInsets2 = (WindowInsets) this.F;
                        if (b10 == 3) {
                            windowInsets2 = windowInsets2.replaceSystemWindowInsets(windowInsets2.getSystemWindowInsetLeft(), windowInsets2.getSystemWindowInsetTop(), i12, windowInsets2.getSystemWindowInsetBottom());
                        } else if (b10 == 5) {
                            windowInsets2 = windowInsets2.replaceSystemWindowInsets(i12, windowInsets2.getSystemWindowInsetTop(), windowInsets2.getSystemWindowInsetRight(), windowInsets2.getSystemWindowInsetBottom());
                        }
                        ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin = windowInsets2.getSystemWindowInsetLeft();
                        ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = windowInsets2.getSystemWindowInsetTop();
                        ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin = windowInsets2.getSystemWindowInsetRight();
                        ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = windowInsets2.getSystemWindowInsetBottom();
                    }
                }
                if (z(childAt)) {
                    childAt.measure(View.MeasureSpec.makeMeasureSpec((size - ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin) - ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin, 1073741824), View.MeasureSpec.makeMeasureSpec((size2 - ((ViewGroup.MarginLayoutParams) layoutParams).topMargin) - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin, 1073741824));
                } else if (B(childAt)) {
                    if (S) {
                        float t7 = ViewCompat.t(childAt);
                        float f10 = this.f2531f;
                        if (t7 != f10) {
                            ViewCompat.t0(childAt, f10);
                        }
                    }
                    int r10 = r(childAt) & 7;
                    int i14 = r10 == 3 ? 1 : i12;
                    if ((i14 != 0 && z11) || (i14 == 0 && z12)) {
                        throw new IllegalStateException("Child drawer has absolute gravity " + u(r10) + " but this DrawerLayout already has a drawer view along that edge");
                    }
                    if (i14 != 0) {
                        z11 = true;
                    } else {
                        z12 = true;
                    }
                    childAt.measure(ViewGroup.getChildMeasureSpec(i10, this.f2532g + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin, ((ViewGroup.MarginLayoutParams) layoutParams).width), ViewGroup.getChildMeasureSpec(i11, ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin, ((ViewGroup.MarginLayoutParams) layoutParams).height));
                    i13++;
                    i12 = 0;
                } else {
                    throw new IllegalStateException("Child " + childAt + " at index " + i13 + " does not have a valid layout_gravity - must be Gravity.LEFT, Gravity.RIGHT or Gravity.NO_GRAVITY");
                }
            }
            i13++;
            i12 = 0;
        }
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        View l10;
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        int i10 = savedState.f2556e;
        if (i10 != 0 && (l10 = l(i10)) != null) {
            G(l10);
        }
        int i11 = savedState.f2557f;
        if (i11 != 3) {
            N(i11, 3);
        }
        int i12 = savedState.f2558g;
        if (i12 != 3) {
            N(i12, 5);
        }
        int i13 = savedState.f2559h;
        if (i13 != 3) {
            N(i13, 8388611);
        }
        int i14 = savedState.f2560i;
        if (i14 != 3) {
            N(i14, 8388613);
        }
    }

    @Override // android.view.View
    public void onRtlPropertiesChanged(int i10) {
        L();
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        int childCount = getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            LayoutParams layoutParams = (LayoutParams) getChildAt(i10).getLayoutParams();
            int i11 = layoutParams.f2555d;
            boolean z10 = i11 == 1;
            boolean z11 = i11 == 2;
            if (z10 || z11) {
                savedState.f2556e = layoutParams.f2552a;
                break;
            }
        }
        savedState.f2557f = this.f2543r;
        savedState.f2558g = this.f2544s;
        savedState.f2559h = this.f2545t;
        savedState.f2560i = this.f2546u;
        return savedState;
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0058, code lost:
    
        if (p(r7) != 2) goto L20;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.f2536k.G(motionEvent);
        this.f2537l.G(motionEvent);
        int action = motionEvent.getAction() & 255;
        boolean z10 = false;
        if (action == 0) {
            float x10 = motionEvent.getX();
            float y4 = motionEvent.getY();
            this.f2550y = x10;
            this.f2551z = y4;
            this.f2547v = false;
        } else if (action == 1) {
            float x11 = motionEvent.getX();
            float y10 = motionEvent.getY();
            View u7 = this.f2536k.u((int) x11, (int) y10);
            if (u7 != null && z(u7)) {
                float f10 = x11 - this.f2550y;
                float f11 = y10 - this.f2551z;
                int A = this.f2536k.A();
                if ((f10 * f10) + (f11 * f11) < A * A) {
                    View m10 = m();
                    if (m10 != null) {
                    }
                }
            }
            z10 = true;
            g(z10);
        } else if (action == 3) {
            g(true);
            this.f2547v = false;
        }
        return true;
    }

    public int p(View view) {
        if (B(view)) {
            return o(((LayoutParams) view.getLayoutParams()).f2552a);
        }
        throw new IllegalArgumentException("View " + view + " is not a drawer");
    }

    public CharSequence q(int i10) {
        int b10 = GravityCompat.b(i10, ViewCompat.x(this));
        if (b10 == 3) {
            return this.D;
        }
        if (b10 == 5) {
            return this.E;
        }
        return null;
    }

    int r(View view) {
        return GravityCompat.b(((LayoutParams) view.getLayoutParams()).f2552a, ViewCompat.x(this));
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean z10) {
        super.requestDisallowInterceptTouchEvent(z10);
        if (z10) {
            g(true);
        }
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (this.f2541p) {
            return;
        }
        super.requestLayout();
    }

    float s(View view) {
        return ((LayoutParams) view.getLayoutParams()).f2553b;
    }

    public void setDrawerElevation(float f10) {
        this.f2531f = f10;
        for (int i10 = 0; i10 < getChildCount(); i10++) {
            View childAt = getChildAt(i10);
            if (B(childAt)) {
                ViewCompat.t0(childAt, this.f2531f);
            }
        }
    }

    @Deprecated
    public void setDrawerListener(e eVar) {
        e eVar2 = this.f2548w;
        if (eVar2 != null) {
            I(eVar2);
        }
        if (eVar != null) {
            a(eVar);
        }
        this.f2548w = eVar;
    }

    public void setDrawerLockMode(int i10) {
        N(i10, 3);
        N(i10, 5);
    }

    public void setScrimColor(int i10) {
        this.f2533h = i10;
        invalidate();
    }

    public void setStatusBarBackground(Drawable drawable) {
        this.A = drawable;
        invalidate();
    }

    public void setStatusBarBackgroundColor(int i10) {
        this.A = new ColorDrawable(i10);
        invalidate();
    }

    boolean z(View view) {
        return ((LayoutParams) view.getLayoutParams()).f2552a == 0;
    }

    public DrawerLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.drawerLayoutStyle);
    }

    public DrawerLayout(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f2530e = new d();
        this.f2533h = -1728053248;
        this.f2535j = new Paint();
        this.f2542q = true;
        this.f2543r = 3;
        this.f2544s = 3;
        this.f2545t = 3;
        this.f2546u = 3;
        this.H = null;
        this.I = null;
        this.J = null;
        this.K = null;
        this.O = new a();
        setDescendantFocusability(262144);
        float f10 = getResources().getDisplayMetrics().density;
        this.f2532g = (int) ((64.0f * f10) + 0.5f);
        float f11 = f10 * 400.0f;
        f fVar = new f(3);
        this.f2538m = fVar;
        f fVar2 = new f(5);
        this.f2539n = fVar2;
        ViewDragHelper o10 = ViewDragHelper.o(this, 1.0f, fVar);
        this.f2536k = o10;
        o10.N(1);
        o10.O(f11);
        fVar.q(o10);
        ViewDragHelper o11 = ViewDragHelper.o(this, 1.0f, fVar2);
        this.f2537l = o11;
        o11.N(2);
        o11.O(f11);
        fVar2.q(o11);
        setFocusableInTouchMode(true);
        ViewCompat.w0(this, 1);
        ViewCompat.l0(this, new c());
        setMotionEventSplittingEnabled(false);
        if (ViewCompat.u(this)) {
            setOnApplyWindowInsetsListener(new b());
            setSystemUiVisibility(1280);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(P);
            try {
                this.A = obtainStyledAttributes.getDrawable(0);
            } finally {
                obtainStyledAttributes.recycle();
            }
        }
        TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, R$styleable.DrawerLayout, i10, 0);
        try {
            int i11 = R$styleable.DrawerLayout_elevation;
            if (obtainStyledAttributes2.hasValue(i11)) {
                this.f2531f = obtainStyledAttributes2.getDimension(i11, 0.0f);
            } else {
                this.f2531f = getResources().getDimension(R$dimen.def_drawer_elevation);
            }
            obtainStyledAttributes2.recycle();
            this.L = new ArrayList<>();
        } catch (Throwable th) {
            obtainStyledAttributes2.recycle();
            throw th;
        }
    }

    public void setStatusBarBackground(int i10) {
        this.A = i10 != 0 ? ContextCompat.e(getContext(), i10) : null;
        invalidate();
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {

        /* renamed from: a, reason: collision with root package name */
        public int f2552a;

        /* renamed from: b, reason: collision with root package name */
        float f2553b;

        /* renamed from: c, reason: collision with root package name */
        boolean f2554c;

        /* renamed from: d, reason: collision with root package name */
        int f2555d;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.f2552a = 0;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, DrawerLayout.Q);
            this.f2552a = obtainStyledAttributes.getInt(0, 0);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(int i10, int i11) {
            super(i10, i11);
            this.f2552a = 0;
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.MarginLayoutParams) layoutParams);
            this.f2552a = 0;
            this.f2552a = layoutParams.f2552a;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.f2552a = 0;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.f2552a = 0;
        }
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        int f2556e;

        /* renamed from: f, reason: collision with root package name */
        int f2557f;

        /* renamed from: g, reason: collision with root package name */
        int f2558g;

        /* renamed from: h, reason: collision with root package name */
        int f2559h;

        /* renamed from: i, reason: collision with root package name */
        int f2560i;

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
            this.f2556e = 0;
            this.f2556e = parcel.readInt();
            this.f2557f = parcel.readInt();
            this.f2558g = parcel.readInt();
            this.f2559h = parcel.readInt();
            this.f2560i = parcel.readInt();
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeInt(this.f2556e);
            parcel.writeInt(this.f2557f);
            parcel.writeInt(this.f2558g);
            parcel.writeInt(this.f2559h);
            parcel.writeInt(this.f2560i);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
            this.f2556e = 0;
        }
    }
}
