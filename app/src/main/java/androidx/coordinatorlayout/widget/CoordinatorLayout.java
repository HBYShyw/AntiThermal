package androidx.coordinatorlayout.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import androidx.coordinatorlayout.R$attr;
import androidx.coordinatorlayout.R$style;
import androidx.coordinatorlayout.R$styleable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.util.ObjectsCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.t;
import androidx.customview.view.AbsSavedState;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class CoordinatorLayout extends ViewGroup implements NestedScrollingParent2, NestedScrollingParent3 {
    static final ThreadLocal<Map<String, Constructor<Behavior>>> A;
    static final Comparator<View> B;
    private static final androidx.core.util.e<Rect> C;

    /* renamed from: y, reason: collision with root package name */
    static final String f2037y;

    /* renamed from: z, reason: collision with root package name */
    static final Class<?>[] f2038z;

    /* renamed from: e, reason: collision with root package name */
    private final List<View> f2039e;

    /* renamed from: f, reason: collision with root package name */
    private final DirectedAcyclicGraph<View> f2040f;

    /* renamed from: g, reason: collision with root package name */
    private final List<View> f2041g;

    /* renamed from: h, reason: collision with root package name */
    private final List<View> f2042h;

    /* renamed from: i, reason: collision with root package name */
    private Paint f2043i;

    /* renamed from: j, reason: collision with root package name */
    private final int[] f2044j;

    /* renamed from: k, reason: collision with root package name */
    private final int[] f2045k;

    /* renamed from: l, reason: collision with root package name */
    private boolean f2046l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f2047m;

    /* renamed from: n, reason: collision with root package name */
    private int[] f2048n;

    /* renamed from: o, reason: collision with root package name */
    private View f2049o;

    /* renamed from: p, reason: collision with root package name */
    private View f2050p;

    /* renamed from: q, reason: collision with root package name */
    private f f2051q;

    /* renamed from: r, reason: collision with root package name */
    private boolean f2052r;

    /* renamed from: s, reason: collision with root package name */
    private WindowInsetsCompat f2053s;

    /* renamed from: t, reason: collision with root package name */
    private boolean f2054t;

    /* renamed from: u, reason: collision with root package name */
    private Drawable f2055u;

    /* renamed from: v, reason: collision with root package name */
    ViewGroup.OnHierarchyChangeListener f2056v;

    /* renamed from: w, reason: collision with root package name */
    private t f2057w;

    /* renamed from: x, reason: collision with root package name */
    private final NestedScrollingParentHelper f2058x;

    /* loaded from: classes.dex */
    public static abstract class Behavior<V extends View> {
        public Behavior() {
        }

        public static Object getTag(View view) {
            return ((e) view.getLayoutParams()).f2079r;
        }

        public static void setTag(View view, Object obj) {
            ((e) view.getLayoutParams()).f2079r = obj;
        }

        public boolean blocksInteractionBelow(CoordinatorLayout coordinatorLayout, V v7) {
            return getScrimOpacity(coordinatorLayout, v7) > 0.0f;
        }

        public boolean getInsetDodgeRect(CoordinatorLayout coordinatorLayout, V v7, Rect rect) {
            return false;
        }

        public int getScrimColor(CoordinatorLayout coordinatorLayout, V v7) {
            return -16777216;
        }

        public float getScrimOpacity(CoordinatorLayout coordinatorLayout, V v7) {
            return 0.0f;
        }

        public boolean layoutDependsOn(CoordinatorLayout coordinatorLayout, V v7, View view) {
            return false;
        }

        public WindowInsetsCompat onApplyWindowInsets(CoordinatorLayout coordinatorLayout, V v7, WindowInsetsCompat windowInsetsCompat) {
            return windowInsetsCompat;
        }

        public void onAttachedToLayoutParams(e eVar) {
        }

        public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, V v7, View view) {
            return false;
        }

        public void onDependentViewRemoved(CoordinatorLayout coordinatorLayout, V v7, View view) {
        }

        public void onDetachedFromLayoutParams() {
        }

        public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, V v7, MotionEvent motionEvent) {
            return false;
        }

        public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, V v7, int i10) {
            return false;
        }

        public boolean onMeasureChild(CoordinatorLayout coordinatorLayout, V v7, int i10, int i11, int i12, int i13) {
            return false;
        }

        public boolean onNestedFling(CoordinatorLayout coordinatorLayout, V v7, View view, float f10, float f11, boolean z10) {
            return false;
        }

        public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, V v7, View view, float f10, float f11) {
            return false;
        }

        @Deprecated
        public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, V v7, View view, int i10, int i11, int[] iArr) {
        }

        public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, V v7, View view, int i10, int i11, int[] iArr, int i12) {
            if (i12 == 0) {
                onNestedPreScroll(coordinatorLayout, v7, view, i10, i11, iArr);
            }
        }

        @Deprecated
        public void onNestedScroll(CoordinatorLayout coordinatorLayout, V v7, View view, int i10, int i11, int i12, int i13) {
        }

        @Deprecated
        public void onNestedScroll(CoordinatorLayout coordinatorLayout, V v7, View view, int i10, int i11, int i12, int i13, int i14) {
            if (i14 == 0) {
                onNestedScroll(coordinatorLayout, v7, view, i10, i11, i12, i13);
            }
        }

        @Deprecated
        public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, V v7, View view, View view2, int i10) {
        }

        public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, V v7, View view, View view2, int i10, int i11) {
            if (i11 == 0) {
                onNestedScrollAccepted(coordinatorLayout, v7, view, view2, i10);
            }
        }

        public boolean onRequestChildRectangleOnScreen(CoordinatorLayout coordinatorLayout, V v7, Rect rect, boolean z10) {
            return false;
        }

        public void onRestoreInstanceState(CoordinatorLayout coordinatorLayout, V v7, Parcelable parcelable) {
        }

        public Parcelable onSaveInstanceState(CoordinatorLayout coordinatorLayout, V v7) {
            return View.BaseSavedState.EMPTY_STATE;
        }

        @Deprecated
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V v7, View view, View view2, int i10) {
            return false;
        }

        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V v7, View view, View view2, int i10, int i11) {
            if (i11 == 0) {
                return onStartNestedScroll(coordinatorLayout, v7, view, view2, i10);
            }
            return false;
        }

        @Deprecated
        public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, V v7, View view) {
        }

        public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, V v7, View view, int i10) {
            if (i10 == 0) {
                onStopNestedScroll(coordinatorLayout, v7, view);
            }
        }

        public boolean onTouchEvent(CoordinatorLayout coordinatorLayout, V v7, MotionEvent motionEvent) {
            return false;
        }

        public Behavior(Context context, AttributeSet attributeSet) {
        }

        public void onNestedScroll(CoordinatorLayout coordinatorLayout, V v7, View view, int i10, int i11, int i12, int i13, int i14, int[] iArr) {
            iArr[0] = iArr[0] + i12;
            iArr[1] = iArr[1] + i13;
            onNestedScroll(coordinatorLayout, v7, view, i10, i11, i12, i13, i14);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements t {
        a() {
        }

        @Override // androidx.core.view.t
        public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
            return CoordinatorLayout.this.a0(windowInsetsCompat);
        }
    }

    /* loaded from: classes.dex */
    public interface b {
        Behavior getBehavior();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Deprecated
    /* loaded from: classes.dex */
    public @interface c {
        Class<? extends Behavior> value();
    }

    /* loaded from: classes.dex */
    private class d implements ViewGroup.OnHierarchyChangeListener {
        d() {
        }

        @Override // android.view.ViewGroup.OnHierarchyChangeListener
        public void onChildViewAdded(View view, View view2) {
            ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener = CoordinatorLayout.this.f2056v;
            if (onHierarchyChangeListener != null) {
                onHierarchyChangeListener.onChildViewAdded(view, view2);
            }
        }

        @Override // android.view.ViewGroup.OnHierarchyChangeListener
        public void onChildViewRemoved(View view, View view2) {
            CoordinatorLayout.this.L(2);
            ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener = CoordinatorLayout.this.f2056v;
            if (onHierarchyChangeListener != null) {
                onHierarchyChangeListener.onChildViewRemoved(view, view2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class f implements ViewTreeObserver.OnPreDrawListener {
        f() {
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            CoordinatorLayout.this.L(0);
            return true;
        }
    }

    /* loaded from: classes.dex */
    static class g implements Comparator<View> {
        g() {
        }

        @Override // java.util.Comparator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(View view, View view2) {
            float J = ViewCompat.J(view);
            float J2 = ViewCompat.J(view2);
            if (J > J2) {
                return -1;
            }
            return J < J2 ? 1 : 0;
        }
    }

    static {
        Package r02 = CoordinatorLayout.class.getPackage();
        f2037y = r02 != null ? r02.getName() : null;
        B = new g();
        f2038z = new Class[]{Context.class, AttributeSet.class};
        A = new ThreadLocal<>();
        C = new androidx.core.util.g(12);
    }

    public CoordinatorLayout(Context context) {
        this(context, null);
    }

    private int A(int i10) {
        int[] iArr = this.f2048n;
        if (iArr == null) {
            Log.e("CoordinatorLayout", "No keylines defined for " + this + " - attempted index lookup " + i10);
            return 0;
        }
        if (i10 >= 0 && i10 < iArr.length) {
            return iArr[i10];
        }
        Log.e("CoordinatorLayout", "Keyline index " + i10 + " out of range for " + this);
        return 0;
    }

    private void D(List<View> list) {
        list.clear();
        boolean isChildrenDrawingOrderEnabled = isChildrenDrawingOrderEnabled();
        int childCount = getChildCount();
        for (int i10 = childCount - 1; i10 >= 0; i10--) {
            list.add(getChildAt(isChildrenDrawingOrderEnabled ? getChildDrawingOrder(childCount, i10) : i10));
        }
        Comparator<View> comparator = B;
        if (comparator != null) {
            Collections.sort(list, comparator);
        }
    }

    private boolean E(View view) {
        return this.f2040f.j(view);
    }

    private void G(View view, int i10) {
        e eVar = (e) view.getLayoutParams();
        Rect e10 = e();
        e10.set(getPaddingLeft() + ((ViewGroup.MarginLayoutParams) eVar).leftMargin, getPaddingTop() + ((ViewGroup.MarginLayoutParams) eVar).topMargin, (getWidth() - getPaddingRight()) - ((ViewGroup.MarginLayoutParams) eVar).rightMargin, (getHeight() - getPaddingBottom()) - ((ViewGroup.MarginLayoutParams) eVar).bottomMargin);
        if (this.f2053s != null && ViewCompat.u(this) && !ViewCompat.u(view)) {
            e10.left += this.f2053s.j();
            e10.top += this.f2053s.l();
            e10.right -= this.f2053s.k();
            e10.bottom -= this.f2053s.i();
        }
        Rect e11 = e();
        GravityCompat.a(W(eVar.f2064c), view.getMeasuredWidth(), view.getMeasuredHeight(), e10, e11, i10);
        view.layout(e11.left, e11.top, e11.right, e11.bottom);
        S(e10);
        S(e11);
    }

    private void H(View view, View view2, int i10) {
        Rect e10 = e();
        Rect e11 = e();
        try {
            x(view2, e10);
            y(view, i10, e10, e11);
            view.layout(e11.left, e11.top, e11.right, e11.bottom);
        } finally {
            S(e10);
            S(e11);
        }
    }

    private void I(View view, int i10, int i11) {
        e eVar = (e) view.getLayoutParams();
        int b10 = GravityCompat.b(X(eVar.f2064c), i11);
        int i12 = b10 & 7;
        int i13 = b10 & 112;
        int width = getWidth();
        int height = getHeight();
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        if (i11 == 1) {
            i10 = width - i10;
        }
        int A2 = A(i10) - measuredWidth;
        int i14 = 0;
        if (i12 == 1) {
            A2 += measuredWidth / 2;
        } else if (i12 == 5) {
            A2 += measuredWidth;
        }
        if (i13 == 16) {
            i14 = 0 + (measuredHeight / 2);
        } else if (i13 == 80) {
            i14 = measuredHeight + 0;
        }
        int max = Math.max(getPaddingLeft() + ((ViewGroup.MarginLayoutParams) eVar).leftMargin, Math.min(A2, ((width - getPaddingRight()) - measuredWidth) - ((ViewGroup.MarginLayoutParams) eVar).rightMargin));
        int max2 = Math.max(getPaddingTop() + ((ViewGroup.MarginLayoutParams) eVar).topMargin, Math.min(i14, ((height - getPaddingBottom()) - measuredHeight) - ((ViewGroup.MarginLayoutParams) eVar).bottomMargin));
        view.layout(max, max2, measuredWidth + max, measuredHeight + max2);
    }

    private void J(View view, Rect rect, int i10) {
        boolean z10;
        boolean z11;
        int width;
        int i11;
        int i12;
        int i13;
        int height;
        int i14;
        int i15;
        int i16;
        if (ViewCompat.Q(view) && view.getWidth() > 0 && view.getHeight() > 0) {
            e eVar = (e) view.getLayoutParams();
            Behavior f10 = eVar.f();
            Rect e10 = e();
            Rect e11 = e();
            e11.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
            if (f10 != null && f10.getInsetDodgeRect(this, view, e10)) {
                if (!e11.contains(e10)) {
                    throw new IllegalArgumentException("Rect should be within the child's bounds. Rect:" + e10.toShortString() + " | Bounds:" + e11.toShortString());
                }
            } else {
                e10.set(e11);
            }
            S(e11);
            if (e10.isEmpty()) {
                S(e10);
                return;
            }
            int b10 = GravityCompat.b(eVar.f2069h, i10);
            boolean z12 = true;
            if ((b10 & 48) != 48 || (i15 = (e10.top - ((ViewGroup.MarginLayoutParams) eVar).topMargin) - eVar.f2071j) >= (i16 = rect.top)) {
                z10 = false;
            } else {
                Z(view, i16 - i15);
                z10 = true;
            }
            if ((b10 & 80) == 80 && (height = ((getHeight() - e10.bottom) - ((ViewGroup.MarginLayoutParams) eVar).bottomMargin) + eVar.f2071j) < (i14 = rect.bottom)) {
                Z(view, height - i14);
                z10 = true;
            }
            if (!z10) {
                Z(view, 0);
            }
            if ((b10 & 3) != 3 || (i12 = (e10.left - ((ViewGroup.MarginLayoutParams) eVar).leftMargin) - eVar.f2070i) >= (i13 = rect.left)) {
                z11 = false;
            } else {
                Y(view, i13 - i12);
                z11 = true;
            }
            if ((b10 & 5) != 5 || (width = ((getWidth() - e10.right) - ((ViewGroup.MarginLayoutParams) eVar).rightMargin) + eVar.f2070i) >= (i11 = rect.right)) {
                z12 = z11;
            } else {
                Y(view, width - i11);
            }
            if (!z12) {
                Y(view, 0);
            }
            S(e10);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static Behavior O(Context context, AttributeSet attributeSet, String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (str.startsWith(".")) {
            str = context.getPackageName() + str;
        } else if (str.indexOf(46) < 0) {
            String str2 = f2037y;
            if (!TextUtils.isEmpty(str2)) {
                str = str2 + '.' + str;
            }
        }
        try {
            ThreadLocal<Map<String, Constructor<Behavior>>> threadLocal = A;
            Map<String, Constructor<Behavior>> map = threadLocal.get();
            if (map == null) {
                map = new HashMap<>();
                threadLocal.set(map);
            }
            Constructor<Behavior> constructor = map.get(str);
            if (constructor == null) {
                constructor = Class.forName(str, false, context.getClassLoader()).getConstructor(f2038z);
                constructor.setAccessible(true);
                map.put(str, constructor);
            }
            return constructor.newInstance(context, attributeSet);
        } catch (Exception e10) {
            throw new RuntimeException("Could not inflate Behavior subclass " + str, e10);
        }
    }

    private boolean P(MotionEvent motionEvent, int i10) {
        int actionMasked = motionEvent.getActionMasked();
        List<View> list = this.f2041g;
        D(list);
        int size = list.size();
        MotionEvent motionEvent2 = null;
        boolean z10 = false;
        boolean z11 = false;
        for (int i11 = 0; i11 < size; i11++) {
            View view = list.get(i11);
            e eVar = (e) view.getLayoutParams();
            Behavior f10 = eVar.f();
            if (!(z10 || z11) || actionMasked == 0) {
                if (!z10 && f10 != null) {
                    if (i10 == 0) {
                        z10 = f10.onInterceptTouchEvent(this, view, motionEvent);
                    } else if (i10 == 1) {
                        z10 = f10.onTouchEvent(this, view, motionEvent);
                    }
                    if (z10) {
                        this.f2049o = view;
                    }
                }
                boolean c10 = eVar.c();
                boolean i12 = eVar.i(this, view);
                z11 = i12 && !c10;
                if (i12 && !z11) {
                    break;
                }
            } else if (f10 != null) {
                if (motionEvent2 == null) {
                    long uptimeMillis = SystemClock.uptimeMillis();
                    motionEvent2 = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
                }
                if (i10 == 0) {
                    f10.onInterceptTouchEvent(this, view, motionEvent2);
                } else if (i10 == 1) {
                    f10.onTouchEvent(this, view, motionEvent2);
                }
            }
        }
        list.clear();
        return z10;
    }

    private void Q() {
        this.f2039e.clear();
        this.f2040f.c();
        int childCount = getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = getChildAt(i10);
            e C2 = C(childAt);
            C2.d(this, childAt);
            this.f2040f.b(childAt);
            for (int i11 = 0; i11 < childCount; i11++) {
                if (i11 != i10) {
                    View childAt2 = getChildAt(i11);
                    if (C2.b(this, childAt, childAt2)) {
                        if (!this.f2040f.d(childAt2)) {
                            this.f2040f.b(childAt2);
                        }
                        this.f2040f.a(childAt2, childAt);
                    }
                }
            }
        }
        this.f2039e.addAll(this.f2040f.i());
        Collections.reverse(this.f2039e);
    }

    private static void S(Rect rect) {
        rect.setEmpty();
        C.a(rect);
    }

    private void U(boolean z10) {
        int childCount = getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = getChildAt(i10);
            Behavior f10 = ((e) childAt.getLayoutParams()).f();
            if (f10 != null) {
                long uptimeMillis = SystemClock.uptimeMillis();
                MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
                if (z10) {
                    f10.onInterceptTouchEvent(this, childAt, obtain);
                } else {
                    f10.onTouchEvent(this, childAt, obtain);
                }
                obtain.recycle();
            }
        }
        for (int i11 = 0; i11 < childCount; i11++) {
            ((e) getChildAt(i11).getLayoutParams()).m();
        }
        this.f2049o = null;
        this.f2046l = false;
    }

    private static int V(int i10) {
        if (i10 == 0) {
            return 17;
        }
        return i10;
    }

    private static int W(int i10) {
        if ((i10 & 7) == 0) {
            i10 |= 8388611;
        }
        return (i10 & 112) == 0 ? i10 | 48 : i10;
    }

    private static int X(int i10) {
        if (i10 == 0) {
            return 8388661;
        }
        return i10;
    }

    private void Y(View view, int i10) {
        e eVar = (e) view.getLayoutParams();
        int i11 = eVar.f2070i;
        if (i11 != i10) {
            ViewCompat.V(view, i10 - i11);
            eVar.f2070i = i10;
        }
    }

    private void Z(View view, int i10) {
        e eVar = (e) view.getLayoutParams();
        int i11 = eVar.f2071j;
        if (i11 != i10) {
            ViewCompat.W(view, i10 - i11);
            eVar.f2071j = i10;
        }
    }

    private void b0() {
        if (ViewCompat.u(this)) {
            if (this.f2057w == null) {
                this.f2057w = new a();
            }
            ViewCompat.z0(this, this.f2057w);
            setSystemUiVisibility(1280);
            return;
        }
        ViewCompat.z0(this, null);
    }

    private static Rect e() {
        Rect b10 = C.b();
        return b10 == null ? new Rect() : b10;
    }

    private static int g(int i10, int i11, int i12) {
        return i10 < i11 ? i11 : i10 > i12 ? i12 : i10;
    }

    private void h(e eVar, Rect rect, int i10, int i11) {
        int width = getWidth();
        int height = getHeight();
        int max = Math.max(getPaddingLeft() + ((ViewGroup.MarginLayoutParams) eVar).leftMargin, Math.min(rect.left, ((width - getPaddingRight()) - i10) - ((ViewGroup.MarginLayoutParams) eVar).rightMargin));
        int max2 = Math.max(getPaddingTop() + ((ViewGroup.MarginLayoutParams) eVar).topMargin, Math.min(rect.top, ((height - getPaddingBottom()) - i11) - ((ViewGroup.MarginLayoutParams) eVar).bottomMargin));
        rect.set(max, max2, i10 + max, i11 + max2);
    }

    private WindowInsetsCompat i(WindowInsetsCompat windowInsetsCompat) {
        Behavior f10;
        if (windowInsetsCompat.p()) {
            return windowInsetsCompat;
        }
        int childCount = getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = getChildAt(i10);
            if (ViewCompat.u(childAt) && (f10 = ((e) childAt.getLayoutParams()).f()) != null) {
                windowInsetsCompat = f10.onApplyWindowInsets(this, childAt, windowInsetsCompat);
                if (windowInsetsCompat.p()) {
                    break;
                }
            }
        }
        return windowInsetsCompat;
    }

    private void z(View view, int i10, Rect rect, Rect rect2, e eVar, int i11, int i12) {
        int width;
        int height;
        int b10 = GravityCompat.b(V(eVar.f2064c), i10);
        int b11 = GravityCompat.b(W(eVar.f2065d), i10);
        int i13 = b10 & 7;
        int i14 = b10 & 112;
        int i15 = b11 & 7;
        int i16 = b11 & 112;
        if (i15 == 1) {
            width = rect.left + (rect.width() / 2);
        } else if (i15 != 5) {
            width = rect.left;
        } else {
            width = rect.right;
        }
        if (i16 == 16) {
            height = rect.top + (rect.height() / 2);
        } else if (i16 != 80) {
            height = rect.top;
        } else {
            height = rect.bottom;
        }
        if (i13 == 1) {
            width -= i11 / 2;
        } else if (i13 != 5) {
            width -= i11;
        }
        if (i14 == 16) {
            height -= i12 / 2;
        } else if (i14 != 80) {
            height -= i12;
        }
        rect2.set(width, height, i11 + width, i12 + height);
    }

    void B(View view, Rect rect) {
        rect.set(((e) view.getLayoutParams()).h());
    }

    /* JADX WARN: Multi-variable type inference failed */
    e C(View view) {
        e eVar = (e) view.getLayoutParams();
        if (!eVar.f2063b) {
            if (view instanceof b) {
                Behavior behavior = ((b) view).getBehavior();
                if (behavior == null) {
                    Log.e("CoordinatorLayout", "Attached behavior class is null");
                }
                eVar.o(behavior);
                eVar.f2063b = true;
            } else {
                c cVar = null;
                for (Class<?> cls = view.getClass(); cls != null; cls = cls.getSuperclass()) {
                    cVar = (c) cls.getAnnotation(c.class);
                    if (cVar != null) {
                        break;
                    }
                }
                if (cVar != null) {
                    try {
                        eVar.o(cVar.value().getDeclaredConstructor(new Class[0]).newInstance(new Object[0]));
                    } catch (Exception e10) {
                        Log.e("CoordinatorLayout", "Default behavior class " + cVar.value().getName() + " could not be instantiated. Did you forget a default constructor?", e10);
                    }
                }
                eVar.f2063b = true;
            }
        }
        return eVar;
    }

    public boolean F(View view, int i10, int i11) {
        Rect e10 = e();
        x(view, e10);
        try {
            return e10.contains(i10, i11);
        } finally {
            S(e10);
        }
    }

    void K(View view, int i10) {
        Behavior f10;
        e eVar = (e) view.getLayoutParams();
        if (eVar.f2072k != null) {
            Rect e10 = e();
            Rect e11 = e();
            Rect e12 = e();
            x(eVar.f2072k, e10);
            u(view, false, e11);
            int measuredWidth = view.getMeasuredWidth();
            int measuredHeight = view.getMeasuredHeight();
            z(view, i10, e10, e12, eVar, measuredWidth, measuredHeight);
            boolean z10 = (e12.left == e11.left && e12.top == e11.top) ? false : true;
            h(eVar, e12, measuredWidth, measuredHeight);
            int i11 = e12.left - e11.left;
            int i12 = e12.top - e11.top;
            if (i11 != 0) {
                ViewCompat.V(view, i11);
            }
            if (i12 != 0) {
                ViewCompat.W(view, i12);
            }
            if (z10 && (f10 = eVar.f()) != null) {
                f10.onDependentViewChanged(this, view, eVar.f2072k);
            }
            S(e10);
            S(e11);
            S(e12);
        }
    }

    final void L(int i10) {
        boolean z10;
        int x10 = ViewCompat.x(this);
        int size = this.f2039e.size();
        Rect e10 = e();
        Rect e11 = e();
        Rect e12 = e();
        for (int i11 = 0; i11 < size; i11++) {
            View view = this.f2039e.get(i11);
            e eVar = (e) view.getLayoutParams();
            if (i10 != 0 || view.getVisibility() != 8) {
                for (int i12 = 0; i12 < i11; i12++) {
                    if (eVar.f2073l == this.f2039e.get(i12)) {
                        K(view, x10);
                    }
                }
                u(view, true, e11);
                if (eVar.f2068g != 0 && !e11.isEmpty()) {
                    int b10 = GravityCompat.b(eVar.f2068g, x10);
                    int i13 = b10 & 112;
                    if (i13 == 48) {
                        e10.top = Math.max(e10.top, e11.bottom);
                    } else if (i13 == 80) {
                        e10.bottom = Math.max(e10.bottom, getHeight() - e11.top);
                    }
                    int i14 = b10 & 7;
                    if (i14 == 3) {
                        e10.left = Math.max(e10.left, e11.right);
                    } else if (i14 == 5) {
                        e10.right = Math.max(e10.right, getWidth() - e11.left);
                    }
                }
                if (eVar.f2069h != 0 && view.getVisibility() == 0) {
                    J(view, e10, x10);
                }
                if (i10 != 2) {
                    B(view, e12);
                    if (!e12.equals(e11)) {
                        R(view, e11);
                    }
                }
                for (int i15 = i11 + 1; i15 < size; i15++) {
                    View view2 = this.f2039e.get(i15);
                    e eVar2 = (e) view2.getLayoutParams();
                    Behavior f10 = eVar2.f();
                    if (f10 != null && f10.layoutDependsOn(this, view2, view)) {
                        if (i10 == 0 && eVar2.g()) {
                            eVar2.k();
                        } else {
                            if (i10 != 2) {
                                z10 = f10.onDependentViewChanged(this, view2, view);
                            } else {
                                f10.onDependentViewRemoved(this, view2, view);
                                z10 = true;
                            }
                            if (i10 == 1) {
                                eVar2.p(z10);
                            }
                        }
                    }
                }
            }
        }
        S(e10);
        S(e11);
        S(e12);
    }

    public void M(View view, int i10) {
        e eVar = (e) view.getLayoutParams();
        if (!eVar.a()) {
            View view2 = eVar.f2072k;
            if (view2 != null) {
                H(view, view2, i10);
                return;
            }
            int i11 = eVar.f2066e;
            if (i11 >= 0) {
                I(view, i11, i10);
                return;
            } else {
                G(view, i10);
                return;
            }
        }
        throw new IllegalStateException("An anchor may not be changed after CoordinatorLayout measurement begins before layout is complete.");
    }

    public void N(View view, int i10, int i11, int i12, int i13) {
        measureChildWithMargins(view, i10, i11, i12, i13);
    }

    void R(View view, Rect rect) {
        ((e) view.getLayoutParams()).q(rect);
    }

    void T() {
        if (this.f2047m && this.f2051q != null) {
            getViewTreeObserver().removeOnPreDrawListener(this.f2051q);
        }
        this.f2052r = false;
    }

    final WindowInsetsCompat a0(WindowInsetsCompat windowInsetsCompat) {
        if (ObjectsCompat.a(this.f2053s, windowInsetsCompat)) {
            return windowInsetsCompat;
        }
        this.f2053s = windowInsetsCompat;
        boolean z10 = windowInsetsCompat != null && windowInsetsCompat.l() > 0;
        this.f2054t = z10;
        setWillNotDraw(!z10 && getBackground() == null);
        WindowInsetsCompat i10 = i(windowInsetsCompat);
        requestLayout();
        return i10;
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return (layoutParams instanceof e) && super.checkLayoutParams(layoutParams);
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j10) {
        e eVar = (e) view.getLayoutParams();
        Behavior behavior = eVar.f2062a;
        if (behavior != null) {
            float scrimOpacity = behavior.getScrimOpacity(this, view);
            if (scrimOpacity > 0.0f) {
                if (this.f2043i == null) {
                    this.f2043i = new Paint();
                }
                this.f2043i.setColor(eVar.f2062a.getScrimColor(this, view));
                this.f2043i.setAlpha(g(Math.round(scrimOpacity * 255.0f), 0, 255));
                int save = canvas.save();
                if (view.isOpaque()) {
                    canvas.clipRect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom(), Region.Op.DIFFERENCE);
                }
                canvas.drawRect(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom(), this.f2043i);
                canvas.restoreToCount(save);
            }
        }
        return super.drawChild(canvas, view, j10);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        int[] drawableState = getDrawableState();
        Drawable drawable = this.f2055u;
        boolean z10 = false;
        if (drawable != null && drawable.isStateful()) {
            z10 = false | drawable.setState(drawableState);
        }
        if (z10) {
            invalidate();
        }
    }

    void f() {
        if (this.f2047m) {
            if (this.f2051q == null) {
                this.f2051q = new f();
            }
            getViewTreeObserver().addOnPreDrawListener(this.f2051q);
        }
        this.f2052r = true;
    }

    final List<View> getDependencySortedChildren() {
        Q();
        return Collections.unmodifiableList(this.f2039e);
    }

    public final WindowInsetsCompat getLastWindowInsets() {
        return this.f2053s;
    }

    @Override // android.view.ViewGroup
    public int getNestedScrollAxes() {
        return this.f2058x.a();
    }

    public Drawable getStatusBarBackground() {
        return this.f2055u;
    }

    @Override // android.view.View
    protected int getSuggestedMinimumHeight() {
        return Math.max(super.getSuggestedMinimumHeight(), getPaddingTop() + getPaddingBottom());
    }

    @Override // android.view.View
    protected int getSuggestedMinimumWidth() {
        return Math.max(super.getSuggestedMinimumWidth(), getPaddingLeft() + getPaddingRight());
    }

    @Override // androidx.core.view.NestedScrollingParent3
    public void j(View view, int i10, int i11, int i12, int i13, int i14, int[] iArr) {
        Behavior f10;
        boolean z10;
        int min;
        int childCount = getChildCount();
        boolean z11 = false;
        int i15 = 0;
        int i16 = 0;
        for (int i17 = 0; i17 < childCount; i17++) {
            View childAt = getChildAt(i17);
            if (childAt.getVisibility() != 8) {
                e eVar = (e) childAt.getLayoutParams();
                if (eVar.j(i14) && (f10 = eVar.f()) != null) {
                    int[] iArr2 = this.f2044j;
                    iArr2[0] = 0;
                    iArr2[1] = 0;
                    f10.onNestedScroll(this, childAt, view, i10, i11, i12, i13, i14, iArr2);
                    int[] iArr3 = this.f2044j;
                    i15 = i12 > 0 ? Math.max(i15, iArr3[0]) : Math.min(i15, iArr3[0]);
                    if (i13 > 0) {
                        z10 = true;
                        min = Math.max(i16, this.f2044j[1]);
                    } else {
                        z10 = true;
                        min = Math.min(i16, this.f2044j[1]);
                    }
                    i16 = min;
                    z11 = z10;
                }
            }
        }
        iArr[0] = iArr[0] + i15;
        iArr[1] = iArr[1] + i16;
        if (z11) {
            L(1);
        }
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void k(View view, int i10, int i11, int i12, int i13, int i14) {
        j(view, i10, i11, i12, i13, 0, this.f2045k);
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public boolean l(View view, View view2, int i10, int i11) {
        int childCount = getChildCount();
        boolean z10 = false;
        for (int i12 = 0; i12 < childCount; i12++) {
            View childAt = getChildAt(i12);
            if (childAt.getVisibility() != 8) {
                e eVar = (e) childAt.getLayoutParams();
                Behavior f10 = eVar.f();
                if (f10 != null) {
                    boolean onStartNestedScroll = f10.onStartNestedScroll(this, childAt, view, view2, i10, i11);
                    z10 |= onStartNestedScroll;
                    eVar.r(i11, onStartNestedScroll);
                } else {
                    eVar.r(i11, false);
                }
            }
        }
        return z10;
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void m(View view, View view2, int i10, int i11) {
        Behavior f10;
        this.f2058x.c(view, view2, i10, i11);
        this.f2050p = view2;
        int childCount = getChildCount();
        for (int i12 = 0; i12 < childCount; i12++) {
            View childAt = getChildAt(i12);
            e eVar = (e) childAt.getLayoutParams();
            if (eVar.j(i11) && (f10 = eVar.f()) != null) {
                f10.onNestedScrollAccepted(this, childAt, view, view2, i10, i11);
            }
        }
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void n(View view, int i10) {
        this.f2058x.d(view, i10);
        int childCount = getChildCount();
        for (int i11 = 0; i11 < childCount; i11++) {
            View childAt = getChildAt(i11);
            e eVar = (e) childAt.getLayoutParams();
            if (eVar.j(i10)) {
                Behavior f10 = eVar.f();
                if (f10 != null) {
                    f10.onStopNestedScroll(this, childAt, view, i10);
                }
                eVar.l(i10);
                eVar.k();
            }
        }
        this.f2050p = null;
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void o(View view, int i10, int i11, int[] iArr, int i12) {
        Behavior f10;
        int childCount = getChildCount();
        boolean z10 = false;
        int i13 = 0;
        int i14 = 0;
        for (int i15 = 0; i15 < childCount; i15++) {
            View childAt = getChildAt(i15);
            if (childAt.getVisibility() != 8) {
                e eVar = (e) childAt.getLayoutParams();
                if (eVar.j(i12) && (f10 = eVar.f()) != null) {
                    int[] iArr2 = this.f2044j;
                    iArr2[0] = 0;
                    iArr2[1] = 0;
                    f10.onNestedPreScroll(this, childAt, view, i10, i11, iArr2, i12);
                    int[] iArr3 = this.f2044j;
                    i13 = i10 > 0 ? Math.max(i13, iArr3[0]) : Math.min(i13, iArr3[0]);
                    int[] iArr4 = this.f2044j;
                    i14 = i11 > 0 ? Math.max(i14, iArr4[1]) : Math.min(i14, iArr4[1]);
                    z10 = true;
                }
            }
        }
        iArr[0] = i13;
        iArr[1] = i14;
        if (z10) {
            L(1);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        U(false);
        if (this.f2052r) {
            if (this.f2051q == null) {
                this.f2051q = new f();
            }
            getViewTreeObserver().addOnPreDrawListener(this.f2051q);
        }
        if (this.f2053s == null && ViewCompat.u(this)) {
            ViewCompat.h0(this);
        }
        this.f2047m = true;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        U(false);
        if (this.f2052r && this.f2051q != null) {
            getViewTreeObserver().removeOnPreDrawListener(this.f2051q);
        }
        View view = this.f2050p;
        if (view != null) {
            onStopNestedScroll(view);
        }
        this.f2047m = false;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!this.f2054t || this.f2055u == null) {
            return;
        }
        WindowInsetsCompat windowInsetsCompat = this.f2053s;
        int l10 = windowInsetsCompat != null ? windowInsetsCompat.l() : 0;
        if (l10 > 0) {
            this.f2055u.setBounds(0, 0, getWidth(), l10);
            this.f2055u.draw(canvas);
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            U(true);
        }
        boolean P = P(motionEvent, 0);
        if (actionMasked == 1 || actionMasked == 3) {
            U(true);
        }
        return P;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        Behavior f10;
        int x10 = ViewCompat.x(this);
        int size = this.f2039e.size();
        for (int i14 = 0; i14 < size; i14++) {
            View view = this.f2039e.get(i14);
            if (view.getVisibility() != 8 && ((f10 = ((e) view.getLayoutParams()).f()) == null || !f10.onLayoutChild(this, view, x10))) {
                M(view, x10);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x011a, code lost:
    
        if (r0.onMeasureChild(r30, r20, r11, r21, r23, 0) == false) goto L46;
     */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00fb  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x011d  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onMeasure(int i10, int i11) {
        int i12;
        int i13;
        int i14;
        int i15;
        Behavior f10;
        e eVar;
        int i16;
        int i17;
        int i18;
        int i19;
        int i20;
        int i21;
        Q();
        q();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int x10 = ViewCompat.x(this);
        boolean z10 = x10 == 1;
        int mode = View.MeasureSpec.getMode(i10);
        int size = View.MeasureSpec.getSize(i10);
        int mode2 = View.MeasureSpec.getMode(i11);
        int size2 = View.MeasureSpec.getSize(i11);
        int i22 = paddingLeft + paddingRight;
        int i23 = paddingTop + paddingBottom;
        int suggestedMinimumWidth = getSuggestedMinimumWidth();
        int suggestedMinimumHeight = getSuggestedMinimumHeight();
        boolean z11 = this.f2053s != null && ViewCompat.u(this);
        int size3 = this.f2039e.size();
        int i24 = suggestedMinimumWidth;
        int i25 = suggestedMinimumHeight;
        int i26 = 0;
        int i27 = 0;
        while (i27 < size3) {
            View view = this.f2039e.get(i27);
            if (view.getVisibility() == 8) {
                i20 = i27;
                i17 = size3;
                i18 = paddingLeft;
            } else {
                e eVar2 = (e) view.getLayoutParams();
                int i28 = eVar2.f2066e;
                if (i28 < 0 || mode == 0) {
                    i12 = i26;
                } else {
                    int A2 = A(i28);
                    int b10 = GravityCompat.b(X(eVar2.f2064c), x10) & 7;
                    i12 = i26;
                    if ((b10 == 3 && !z10) || (b10 == 5 && z10)) {
                        i13 = Math.max(0, (size - paddingRight) - A2);
                    } else if ((b10 == 5 && !z10) || (b10 == 3 && z10)) {
                        i13 = Math.max(0, A2 - paddingLeft);
                    }
                    if (z11 || ViewCompat.u(view)) {
                        i14 = i10;
                        i15 = i11;
                    } else {
                        int j10 = this.f2053s.j() + this.f2053s.k();
                        int l10 = this.f2053s.l() + this.f2053s.i();
                        i14 = View.MeasureSpec.makeMeasureSpec(size - j10, mode);
                        i15 = View.MeasureSpec.makeMeasureSpec(size2 - l10, mode2);
                    }
                    f10 = eVar2.f();
                    if (f10 == null) {
                        eVar = eVar2;
                        i19 = i12;
                        i20 = i27;
                        i16 = i25;
                        i18 = paddingLeft;
                        i21 = i24;
                        i17 = size3;
                    } else {
                        eVar = eVar2;
                        i16 = i25;
                        i17 = size3;
                        i18 = paddingLeft;
                        i19 = i12;
                        i20 = i27;
                        i21 = i24;
                    }
                    N(view, i14, i13, i15, 0);
                    e eVar3 = eVar;
                    int max = Math.max(i21, i22 + view.getMeasuredWidth() + ((ViewGroup.MarginLayoutParams) eVar3).leftMargin + ((ViewGroup.MarginLayoutParams) eVar3).rightMargin);
                    int max2 = Math.max(i16, i23 + view.getMeasuredHeight() + ((ViewGroup.MarginLayoutParams) eVar3).topMargin + ((ViewGroup.MarginLayoutParams) eVar3).bottomMargin);
                    i26 = View.combineMeasuredStates(i19, view.getMeasuredState());
                    i24 = max;
                    i25 = max2;
                }
                i13 = 0;
                if (z11) {
                }
                i14 = i10;
                i15 = i11;
                f10 = eVar2.f();
                if (f10 == null) {
                }
                N(view, i14, i13, i15, 0);
                e eVar32 = eVar;
                int max3 = Math.max(i21, i22 + view.getMeasuredWidth() + ((ViewGroup.MarginLayoutParams) eVar32).leftMargin + ((ViewGroup.MarginLayoutParams) eVar32).rightMargin);
                int max22 = Math.max(i16, i23 + view.getMeasuredHeight() + ((ViewGroup.MarginLayoutParams) eVar32).topMargin + ((ViewGroup.MarginLayoutParams) eVar32).bottomMargin);
                i26 = View.combineMeasuredStates(i19, view.getMeasuredState());
                i24 = max3;
                i25 = max22;
            }
            i27 = i20 + 1;
            paddingLeft = i18;
            size3 = i17;
        }
        int i29 = i26;
        setMeasuredDimension(View.resolveSizeAndState(i24, i10, (-16777216) & i29), View.resolveSizeAndState(i25, i11, i29 << 16));
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean onNestedFling(View view, float f10, float f11, boolean z10) {
        Behavior f12;
        int childCount = getChildCount();
        boolean z11 = false;
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = getChildAt(i10);
            if (childAt.getVisibility() != 8) {
                e eVar = (e) childAt.getLayoutParams();
                if (eVar.j(0) && (f12 = eVar.f()) != null) {
                    z11 |= f12.onNestedFling(this, childAt, view, f10, f11, z10);
                }
            }
        }
        if (z11) {
            L(1);
        }
        return z11;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean onNestedPreFling(View view, float f10, float f11) {
        Behavior f12;
        int childCount = getChildCount();
        boolean z10 = false;
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = getChildAt(i10);
            if (childAt.getVisibility() != 8) {
                e eVar = (e) childAt.getLayoutParams();
                if (eVar.j(0) && (f12 = eVar.f()) != null) {
                    z10 |= f12.onNestedPreFling(this, childAt, view, f10, f11);
                }
            }
        }
        return z10;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void onNestedPreScroll(View view, int i10, int i11, int[] iArr) {
        o(view, i10, i11, iArr, 0);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void onNestedScroll(View view, int i10, int i11, int i12, int i13) {
        k(view, i10, i11, i12, i13, 0);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void onNestedScrollAccepted(View view, View view2, int i10) {
        m(view, view2, i10, 0);
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        Parcelable parcelable2;
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        SparseArray<Parcelable> sparseArray = savedState.f2059e;
        int childCount = getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = getChildAt(i10);
            int id2 = childAt.getId();
            Behavior f10 = C(childAt).f();
            if (id2 != -1 && f10 != null && (parcelable2 = sparseArray.get(id2)) != null) {
                f10.onRestoreInstanceState(this, childAt, parcelable2);
            }
        }
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState;
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        SparseArray<Parcelable> sparseArray = new SparseArray<>();
        int childCount = getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = getChildAt(i10);
            int id2 = childAt.getId();
            Behavior f10 = ((e) childAt.getLayoutParams()).f();
            if (id2 != -1 && f10 != null && (onSaveInstanceState = f10.onSaveInstanceState(this, childAt)) != null) {
                sparseArray.append(id2, onSaveInstanceState);
            }
        }
        savedState.f2059e = sparseArray;
        return savedState;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean onStartNestedScroll(View view, View view2, int i10) {
        return l(view, view2, i10, 0);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void onStopNestedScroll(View view) {
        n(view, 0);
    }

    /* JADX WARN: Code restructure failed: missing block: B:4:0x0012, code lost:
    
        if (r3 != false) goto L8;
     */
    /* JADX WARN: Removed duplicated region for block: B:10:0x004c  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0037  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0031  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z10;
        boolean onTouchEvent;
        MotionEvent motionEvent2;
        int actionMasked = motionEvent.getActionMasked();
        if (this.f2049o == null) {
            z10 = P(motionEvent, 1);
        } else {
            z10 = false;
        }
        Behavior f10 = ((e) this.f2049o.getLayoutParams()).f();
        if (f10 != null) {
            onTouchEvent = f10.onTouchEvent(this, this.f2049o, motionEvent);
            motionEvent2 = null;
            if (this.f2049o != null) {
                onTouchEvent |= super.onTouchEvent(motionEvent);
            } else if (z10) {
                long uptimeMillis = SystemClock.uptimeMillis();
                motionEvent2 = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
                super.onTouchEvent(motionEvent2);
            }
            if (motionEvent2 != null) {
                motionEvent2.recycle();
            }
            if (actionMasked != 1 || actionMasked == 3) {
                U(false);
            }
            return onTouchEvent;
        }
        onTouchEvent = false;
        motionEvent2 = null;
        if (this.f2049o != null) {
        }
        if (motionEvent2 != null) {
        }
        if (actionMasked != 1) {
        }
        U(false);
        return onTouchEvent;
    }

    public void p(View view) {
        List g6 = this.f2040f.g(view);
        if (g6 == null || g6.isEmpty()) {
            return;
        }
        for (int i10 = 0; i10 < g6.size(); i10++) {
            View view2 = (View) g6.get(i10);
            Behavior f10 = ((e) view2.getLayoutParams()).f();
            if (f10 != null) {
                f10.onDependentViewChanged(this, view2, view);
            }
        }
    }

    void q() {
        int childCount = getChildCount();
        boolean z10 = false;
        int i10 = 0;
        while (true) {
            if (i10 >= childCount) {
                break;
            }
            if (E(getChildAt(i10))) {
                z10 = true;
                break;
            }
            i10++;
        }
        if (z10 != this.f2052r) {
            if (z10) {
                f();
            } else {
                T();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    /* renamed from: r, reason: merged with bridge method [inline-methods] */
    public e generateDefaultLayoutParams() {
        return new e(-2, -2);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z10) {
        Behavior f10 = ((e) view.getLayoutParams()).f();
        if (f10 == null || !f10.onRequestChildRectangleOnScreen(this, view, rect, z10)) {
            return super.requestChildRectangleOnScreen(view, rect, z10);
        }
        return true;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean z10) {
        super.requestDisallowInterceptTouchEvent(z10);
        if (!z10 || this.f2046l) {
            return;
        }
        U(false);
        this.f2046l = true;
    }

    @Override // android.view.ViewGroup
    /* renamed from: s, reason: merged with bridge method [inline-methods] */
    public e generateLayoutParams(AttributeSet attributeSet) {
        return new e(getContext(), attributeSet);
    }

    @Override // android.view.View
    public void setFitsSystemWindows(boolean z10) {
        super.setFitsSystemWindows(z10);
        b0();
    }

    @Override // android.view.ViewGroup
    public void setOnHierarchyChangeListener(ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener) {
        this.f2056v = onHierarchyChangeListener;
    }

    public void setStatusBarBackground(Drawable drawable) {
        Drawable drawable2 = this.f2055u;
        if (drawable2 != drawable) {
            if (drawable2 != null) {
                drawable2.setCallback(null);
            }
            Drawable mutate = drawable != null ? drawable.mutate() : null;
            this.f2055u = mutate;
            if (mutate != null) {
                if (mutate.isStateful()) {
                    this.f2055u.setState(getDrawableState());
                }
                DrawableCompat.g(this.f2055u, ViewCompat.x(this));
                this.f2055u.setVisible(getVisibility() == 0, false);
                this.f2055u.setCallback(this);
            }
            ViewCompat.b0(this);
        }
    }

    public void setStatusBarBackgroundColor(int i10) {
        setStatusBarBackground(new ColorDrawable(i10));
    }

    public void setStatusBarBackgroundResource(int i10) {
        setStatusBarBackground(i10 != 0 ? ContextCompat.e(getContext(), i10) : null);
    }

    @Override // android.view.View
    public void setVisibility(int i10) {
        super.setVisibility(i10);
        boolean z10 = i10 == 0;
        Drawable drawable = this.f2055u;
        if (drawable == null || drawable.isVisible() == z10) {
            return;
        }
        this.f2055u.setVisible(z10, false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    /* renamed from: t, reason: merged with bridge method [inline-methods] */
    public e generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof e) {
            return new e((e) layoutParams);
        }
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return new e((ViewGroup.MarginLayoutParams) layoutParams);
        }
        return new e(layoutParams);
    }

    void u(View view, boolean z10, Rect rect) {
        if (view.isLayoutRequested() || view.getVisibility() == 8) {
            rect.setEmpty();
        } else if (z10) {
            x(view, rect);
        } else {
            rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        }
    }

    public List<View> v(View view) {
        List<View> h10 = this.f2040f.h(view);
        this.f2042h.clear();
        if (h10 != null) {
            this.f2042h.addAll(h10);
        }
        return this.f2042h;
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.f2055u;
    }

    public List<View> w(View view) {
        List g6 = this.f2040f.g(view);
        this.f2042h.clear();
        if (g6 != null) {
            this.f2042h.addAll(g6);
        }
        return this.f2042h;
    }

    void x(View view, Rect rect) {
        androidx.coordinatorlayout.widget.b.a(this, view, rect);
    }

    void y(View view, int i10, Rect rect, Rect rect2) {
        e eVar = (e) view.getLayoutParams();
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        z(view, i10, rect, rect2, eVar, measuredWidth, measuredHeight);
        h(eVar, rect2, measuredWidth, measuredHeight);
    }

    public CoordinatorLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.coordinatorLayoutStyle);
    }

    public CoordinatorLayout(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        TypedArray obtainStyledAttributes;
        this.f2039e = new ArrayList();
        this.f2040f = new DirectedAcyclicGraph<>();
        this.f2041g = new ArrayList();
        this.f2042h = new ArrayList();
        this.f2044j = new int[2];
        this.f2045k = new int[2];
        this.f2058x = new NestedScrollingParentHelper(this);
        if (i10 == 0) {
            obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.CoordinatorLayout, 0, R$style.Widget_Support_CoordinatorLayout);
        } else {
            obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.CoordinatorLayout, i10, 0);
        }
        if (i10 == 0) {
            saveAttributeDataForStyleable(context, R$styleable.CoordinatorLayout, attributeSet, obtainStyledAttributes, 0, R$style.Widget_Support_CoordinatorLayout);
        } else {
            saveAttributeDataForStyleable(context, R$styleable.CoordinatorLayout, attributeSet, obtainStyledAttributes, i10, 0);
        }
        int resourceId = obtainStyledAttributes.getResourceId(R$styleable.CoordinatorLayout_keylines, 0);
        if (resourceId != 0) {
            Resources resources = context.getResources();
            this.f2048n = resources.getIntArray(resourceId);
            float f10 = resources.getDisplayMetrics().density;
            int length = this.f2048n.length;
            for (int i11 = 0; i11 < length; i11++) {
                this.f2048n[i11] = (int) (r12[i11] * f10);
            }
        }
        this.f2055u = obtainStyledAttributes.getDrawable(R$styleable.CoordinatorLayout_statusBarBackground);
        obtainStyledAttributes.recycle();
        b0();
        super.setOnHierarchyChangeListener(new d());
        if (ViewCompat.v(this) == 0) {
            ViewCompat.w0(this, 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        SparseArray<Parcelable> f2059e;

        /* loaded from: classes.dex */
        static class a implements Parcelable.ClassLoaderCreator<SavedState> {
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
            int readInt = parcel.readInt();
            int[] iArr = new int[readInt];
            parcel.readIntArray(iArr);
            Parcelable[] readParcelableArray = parcel.readParcelableArray(classLoader);
            this.f2059e = new SparseArray<>(readInt);
            for (int i10 = 0; i10 < readInt; i10++) {
                this.f2059e.append(iArr[i10], readParcelableArray[i10]);
            }
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            SparseArray<Parcelable> sparseArray = this.f2059e;
            int size = sparseArray != null ? sparseArray.size() : 0;
            parcel.writeInt(size);
            int[] iArr = new int[size];
            Parcelable[] parcelableArr = new Parcelable[size];
            for (int i11 = 0; i11 < size; i11++) {
                iArr[i11] = this.f2059e.keyAt(i11);
                parcelableArr[i11] = this.f2059e.valueAt(i11);
            }
            parcel.writeIntArray(iArr);
            parcel.writeParcelableArray(parcelableArr, i10);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }
    }

    /* loaded from: classes.dex */
    public static class e extends ViewGroup.MarginLayoutParams {

        /* renamed from: a, reason: collision with root package name */
        Behavior f2062a;

        /* renamed from: b, reason: collision with root package name */
        boolean f2063b;

        /* renamed from: c, reason: collision with root package name */
        public int f2064c;

        /* renamed from: d, reason: collision with root package name */
        public int f2065d;

        /* renamed from: e, reason: collision with root package name */
        public int f2066e;

        /* renamed from: f, reason: collision with root package name */
        int f2067f;

        /* renamed from: g, reason: collision with root package name */
        public int f2068g;

        /* renamed from: h, reason: collision with root package name */
        public int f2069h;

        /* renamed from: i, reason: collision with root package name */
        int f2070i;

        /* renamed from: j, reason: collision with root package name */
        int f2071j;

        /* renamed from: k, reason: collision with root package name */
        View f2072k;

        /* renamed from: l, reason: collision with root package name */
        View f2073l;

        /* renamed from: m, reason: collision with root package name */
        private boolean f2074m;

        /* renamed from: n, reason: collision with root package name */
        private boolean f2075n;

        /* renamed from: o, reason: collision with root package name */
        private boolean f2076o;

        /* renamed from: p, reason: collision with root package name */
        private boolean f2077p;

        /* renamed from: q, reason: collision with root package name */
        final Rect f2078q;

        /* renamed from: r, reason: collision with root package name */
        Object f2079r;

        public e(int i10, int i11) {
            super(i10, i11);
            this.f2063b = false;
            this.f2064c = 0;
            this.f2065d = 0;
            this.f2066e = -1;
            this.f2067f = -1;
            this.f2068g = 0;
            this.f2069h = 0;
            this.f2078q = new Rect();
        }

        private void n(View view, CoordinatorLayout coordinatorLayout) {
            View findViewById = coordinatorLayout.findViewById(this.f2067f);
            this.f2072k = findViewById;
            if (findViewById == null) {
                if (coordinatorLayout.isInEditMode()) {
                    this.f2073l = null;
                    this.f2072k = null;
                    return;
                }
                throw new IllegalStateException("Could not find CoordinatorLayout descendant view with id " + coordinatorLayout.getResources().getResourceName(this.f2067f) + " to anchor view " + view);
            }
            if (findViewById == coordinatorLayout) {
                if (coordinatorLayout.isInEditMode()) {
                    this.f2073l = null;
                    this.f2072k = null;
                    return;
                }
                throw new IllegalStateException("View can not be anchored to the the parent CoordinatorLayout");
            }
            for (ViewParent parent = findViewById.getParent(); parent != coordinatorLayout && parent != null; parent = parent.getParent()) {
                if (parent == view) {
                    if (coordinatorLayout.isInEditMode()) {
                        this.f2073l = null;
                        this.f2072k = null;
                        return;
                    }
                    throw new IllegalStateException("Anchor must not be a descendant of the anchored view");
                }
                if (parent instanceof View) {
                    findViewById = parent;
                }
            }
            this.f2073l = findViewById;
        }

        private boolean s(View view, int i10) {
            int b10 = GravityCompat.b(((e) view.getLayoutParams()).f2068g, i10);
            return b10 != 0 && (GravityCompat.b(this.f2069h, i10) & b10) == b10;
        }

        private boolean t(View view, CoordinatorLayout coordinatorLayout) {
            if (this.f2072k.getId() != this.f2067f) {
                return false;
            }
            View view2 = this.f2072k;
            for (ViewParent parent = view2.getParent(); parent != coordinatorLayout; parent = parent.getParent()) {
                if (parent != null && parent != view) {
                    if (parent instanceof View) {
                        view2 = parent;
                    }
                } else {
                    this.f2073l = null;
                    this.f2072k = null;
                    return false;
                }
            }
            this.f2073l = view2;
            return true;
        }

        boolean a() {
            return this.f2072k == null && this.f2067f != -1;
        }

        boolean b(CoordinatorLayout coordinatorLayout, View view, View view2) {
            Behavior behavior;
            return view2 == this.f2073l || s(view2, ViewCompat.x(coordinatorLayout)) || ((behavior = this.f2062a) != null && behavior.layoutDependsOn(coordinatorLayout, view, view2));
        }

        boolean c() {
            if (this.f2062a == null) {
                this.f2074m = false;
            }
            return this.f2074m;
        }

        View d(CoordinatorLayout coordinatorLayout, View view) {
            if (this.f2067f == -1) {
                this.f2073l = null;
                this.f2072k = null;
                return null;
            }
            if (this.f2072k == null || !t(view, coordinatorLayout)) {
                n(view, coordinatorLayout);
            }
            return this.f2072k;
        }

        public int e() {
            return this.f2067f;
        }

        public Behavior f() {
            return this.f2062a;
        }

        boolean g() {
            return this.f2077p;
        }

        Rect h() {
            return this.f2078q;
        }

        boolean i(CoordinatorLayout coordinatorLayout, View view) {
            boolean z10 = this.f2074m;
            if (z10) {
                return true;
            }
            Behavior behavior = this.f2062a;
            boolean blocksInteractionBelow = (behavior != null ? behavior.blocksInteractionBelow(coordinatorLayout, view) : false) | z10;
            this.f2074m = blocksInteractionBelow;
            return blocksInteractionBelow;
        }

        boolean j(int i10) {
            if (i10 == 0) {
                return this.f2075n;
            }
            if (i10 != 1) {
                return false;
            }
            return this.f2076o;
        }

        void k() {
            this.f2077p = false;
        }

        void l(int i10) {
            r(i10, false);
        }

        void m() {
            this.f2074m = false;
        }

        public void o(Behavior behavior) {
            Behavior behavior2 = this.f2062a;
            if (behavior2 != behavior) {
                if (behavior2 != null) {
                    behavior2.onDetachedFromLayoutParams();
                }
                this.f2062a = behavior;
                this.f2079r = null;
                this.f2063b = true;
                if (behavior != null) {
                    behavior.onAttachedToLayoutParams(this);
                }
            }
        }

        void p(boolean z10) {
            this.f2077p = z10;
        }

        void q(Rect rect) {
            this.f2078q.set(rect);
        }

        void r(int i10, boolean z10) {
            if (i10 == 0) {
                this.f2075n = z10;
            } else {
                if (i10 != 1) {
                    return;
                }
                this.f2076o = z10;
            }
        }

        e(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.f2063b = false;
            this.f2064c = 0;
            this.f2065d = 0;
            this.f2066e = -1;
            this.f2067f = -1;
            this.f2068g = 0;
            this.f2069h = 0;
            this.f2078q = new Rect();
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.CoordinatorLayout_Layout);
            this.f2064c = obtainStyledAttributes.getInteger(R$styleable.CoordinatorLayout_Layout_android_layout_gravity, 0);
            this.f2067f = obtainStyledAttributes.getResourceId(R$styleable.CoordinatorLayout_Layout_layout_anchor, -1);
            this.f2065d = obtainStyledAttributes.getInteger(R$styleable.CoordinatorLayout_Layout_layout_anchorGravity, 0);
            this.f2066e = obtainStyledAttributes.getInteger(R$styleable.CoordinatorLayout_Layout_layout_keyline, -1);
            this.f2068g = obtainStyledAttributes.getInt(R$styleable.CoordinatorLayout_Layout_layout_insetEdge, 0);
            this.f2069h = obtainStyledAttributes.getInt(R$styleable.CoordinatorLayout_Layout_layout_dodgeInsetEdges, 0);
            int i10 = R$styleable.CoordinatorLayout_Layout_layout_behavior;
            boolean hasValue = obtainStyledAttributes.hasValue(i10);
            this.f2063b = hasValue;
            if (hasValue) {
                this.f2062a = CoordinatorLayout.O(context, attributeSet, obtainStyledAttributes.getString(i10));
            }
            obtainStyledAttributes.recycle();
            Behavior behavior = this.f2062a;
            if (behavior != null) {
                behavior.onAttachedToLayoutParams(this);
            }
        }

        public e(e eVar) {
            super((ViewGroup.MarginLayoutParams) eVar);
            this.f2063b = false;
            this.f2064c = 0;
            this.f2065d = 0;
            this.f2066e = -1;
            this.f2067f = -1;
            this.f2068g = 0;
            this.f2069h = 0;
            this.f2078q = new Rect();
        }

        public e(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.f2063b = false;
            this.f2064c = 0;
            this.f2065d = 0;
            this.f2066e = -1;
            this.f2067f = -1;
            this.f2068g = 0;
            this.f2069h = 0;
            this.f2078q = new Rect();
        }

        public e(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.f2063b = false;
            this.f2064c = 0;
            this.f2065d = 0;
            this.f2066e = -1;
            this.f2067f = -1;
            this.f2068g = 0;
            this.f2069h = 0;
            this.f2078q = new Rect();
        }
    }
}
