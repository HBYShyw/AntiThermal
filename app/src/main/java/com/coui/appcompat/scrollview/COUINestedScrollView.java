package com.coui.appcompat.scrollview;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import com.support.appcompat.R$styleable;
import java.util.ArrayList;
import l3.ViewNative;
import m1.COUIPhysicalAnimationUtil;
import o2.COUIIOverScroller;
import o2.SpringOverScroller;

/* loaded from: classes.dex */
public class COUINestedScrollView extends NestedScrollView {
    private int I;
    private long J;
    private int K;
    private boolean L;
    private boolean M;
    private boolean N;
    private boolean O;
    private float P;
    private Paint Q;
    private boolean R;
    private ArrayList<a> S;
    private boolean T;
    private final Rect U;
    private COUIIOverScroller V;
    private SpringOverScroller W;

    /* renamed from: a0, reason: collision with root package name */
    private int f7355a0;

    /* renamed from: b0, reason: collision with root package name */
    private boolean f7356b0;

    /* renamed from: c0, reason: collision with root package name */
    private boolean f7357c0;

    /* renamed from: d0, reason: collision with root package name */
    private View f7358d0;

    /* renamed from: e0, reason: collision with root package name */
    private boolean f7359e0;

    /* renamed from: f0, reason: collision with root package name */
    private VelocityTracker f7360f0;

    /* renamed from: g0, reason: collision with root package name */
    private boolean f7361g0;

    /* renamed from: h0, reason: collision with root package name */
    private int f7362h0;

    /* renamed from: i0, reason: collision with root package name */
    private int f7363i0;

    /* renamed from: j0, reason: collision with root package name */
    private int f7364j0;

    /* renamed from: k0, reason: collision with root package name */
    private int f7365k0;

    /* renamed from: l0, reason: collision with root package name */
    private final int[] f7366l0;

    /* renamed from: m0, reason: collision with root package name */
    private final int[] f7367m0;

    /* renamed from: n0, reason: collision with root package name */
    private int f7368n0;

    /* renamed from: o0, reason: collision with root package name */
    private int f7369o0;

    /* renamed from: p0, reason: collision with root package name */
    private int f7370p0;

    /* renamed from: q0, reason: collision with root package name */
    private int f7371q0;

    /* renamed from: r0, reason: collision with root package name */
    private COUISavedState f7372r0;

    /* renamed from: s0, reason: collision with root package name */
    private float f7373s0;

    /* renamed from: t0, reason: collision with root package name */
    private boolean f7374t0;

    /* renamed from: u0, reason: collision with root package name */
    private b f7375u0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class COUISavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<COUISavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        public int f7376e;

        /* loaded from: classes.dex */
        class a implements Parcelable.Creator<COUISavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public COUISavedState createFromParcel(Parcel parcel) {
                return new COUISavedState(parcel, COUISavedState.class.getClassLoader());
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b, reason: merged with bridge method [inline-methods] */
            public COUISavedState[] newArray(int i10) {
                return new COUISavedState[i10];
            }
        }

        COUISavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public String toString() {
            return "NestedScrollView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " scrollPosition=" + this.f7376e + "}";
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeInt(this.f7376e);
        }

        COUISavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.f7376e = parcel.readInt();
        }
    }

    /* loaded from: classes.dex */
    public interface a {
        void a(int i10, int i11, int i12, int i13);
    }

    /* loaded from: classes.dex */
    public interface b {
        void a(COUINestedScrollView cOUINestedScrollView, int i10, int i11, int i12, int i13);
    }

    public COUINestedScrollView(Context context) {
        this(context, null);
    }

    private void B() {
        if (this.f7360f0 == null) {
            this.f7360f0 = VelocityTracker.obtain();
        }
    }

    private boolean C(View view) {
        return !E(view, 0, getHeight());
    }

    private static boolean D(View view, View view2) {
        if (view == view2) {
            return true;
        }
        Object parent = view.getParent();
        return (parent instanceof ViewGroup) && D((View) parent, view2);
    }

    private boolean E(View view, int i10, int i11) {
        view.getDrawingRect(this.U);
        offsetDescendantRectToMyCoords(view, this.U);
        return this.U.bottom + i10 >= getScrollY() && this.U.top - i10 <= getScrollY() + i11;
    }

    private void F(int i10, int i11, int[] iArr) {
        int scrollY = getScrollY();
        if (getOverScrollMode() == 2 || (getOverScrollMode() == 1 && getChildAt(0).getHeight() <= getHeight())) {
            if (getScrollY() + i10 < 0) {
                i10 = -getScrollY();
            } else if (getScrollY() + i10 > getScrollRange()) {
                i10 = getScrollRange() - getScrollY();
            }
        }
        scrollBy(0, i10);
        int scrollY2 = getScrollY() - scrollY;
        if (iArr != null) {
            iArr[1] = iArr[1] + scrollY2;
        }
        i(0, scrollY2, 0, i10 - scrollY2, null, i11, iArr);
    }

    private void G(MotionEvent motionEvent) {
        int actionIndex = motionEvent.getActionIndex();
        if (motionEvent.getPointerId(actionIndex) == this.f7365k0) {
            int i10 = actionIndex == 0 ? 1 : 0;
            int y4 = (int) motionEvent.getY(i10);
            this.f7355a0 = y4;
            this.K = y4;
            this.f7365k0 = motionEvent.getPointerId(i10);
            VelocityTracker velocityTracker = this.f7360f0;
            if (velocityTracker != null) {
                velocityTracker.clear();
            }
        }
    }

    private void J() {
        VelocityTracker velocityTracker = this.f7360f0;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.f7360f0 = null;
        }
    }

    private void L(boolean z10) {
        if (z10) {
            U(2, 1);
        } else {
            W(1);
        }
        this.f7369o0 = getScrollY();
        ViewCompat.b0(this);
    }

    private boolean M(int i10, int i11, int i12) {
        int height = getHeight();
        int scrollY = getScrollY();
        int i13 = height + scrollY;
        boolean z10 = false;
        boolean z11 = i10 == 33;
        View t7 = t(z11, i11, i12);
        if (t7 == null) {
            t7 = this;
        }
        if (i11 < scrollY || i12 > i13) {
            p(z11 ? i11 - scrollY : i12 - i13);
            z10 = true;
        }
        if (t7 != findFocus()) {
            t7.requestFocus(i10);
        }
        return z10;
    }

    private void N(View view) {
        view.getDrawingRect(this.U);
        offsetDescendantRectToMyCoords(view, this.U);
        int f10 = f(this.U);
        if (f10 != 0) {
            scrollBy(0, f10);
        }
    }

    private boolean X(View view, MotionEvent motionEvent) {
        int[] iArr = {0, 1};
        boolean z10 = true;
        for (int i10 = 0; i10 < 2; i10++) {
            motionEvent.setAction(iArr[i10]);
            z10 &= view.dispatchTouchEvent(motionEvent);
        }
        return z10;
    }

    private View Y(MotionEvent motionEvent) {
        View view = null;
        if (!a0(motionEvent)) {
            return null;
        }
        Rect rect = new Rect();
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = getChildAt(childCount);
            if (childAt.getVisibility() == 0 || childAt.getAnimation() != null) {
                childAt.getHitRect(rect);
                boolean contains = rect.contains(((int) motionEvent.getX()) + getScrollX(), ((int) motionEvent.getY()) + getScrollY());
                MotionEvent obtain = MotionEvent.obtain(motionEvent);
                obtain.offsetLocation(getScrollX() - childAt.getLeft(), getScrollY() - childAt.getTop());
                if (contains && X(childAt, obtain)) {
                    view = childAt;
                }
            }
        }
        Log.d("COUINestedScrollView", "findViewToDispatchClickEvent: target: " + view);
        return view;
    }

    private void Z(Context context) {
        if (this.V == null) {
            SpringOverScroller springOverScroller = new SpringOverScroller(context);
            this.W = springOverScroller;
            springOverScroller.F(2.15f);
            this.W.C(true);
            this.V = this.W;
            setEnableFlingSpeedIncrease(true);
        }
        setFocusable(true);
        setDescendantFocusability(262144);
        setWillNotDraw(false);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        this.f7362h0 = viewConfiguration.getScaledTouchSlop();
        this.f7363i0 = viewConfiguration.getScaledMinimumFlingVelocity();
        this.f7364j0 = viewConfiguration.getScaledMaximumFlingVelocity();
        int i10 = displayMetrics.heightPixels;
        this.f7370p0 = i10;
        this.f7371q0 = i10;
        this.I = i10;
    }

    private void a() {
        this.V.abortAnimation();
        W(1);
    }

    private boolean a0(MotionEvent motionEvent) {
        int y4 = (int) (motionEvent.getY() - this.K);
        return System.currentTimeMillis() - this.J < 100 && ((int) Math.sqrt((double) (y4 * y4))) < 10;
    }

    private boolean b0() {
        return getScrollY() < 0 || getScrollY() > getScrollRange();
    }

    private void c0() {
        if (this.T) {
            performHapticFeedback(307);
        }
    }

    private boolean d() {
        if (getChildCount() <= 0) {
            return false;
        }
        View childAt = getChildAt(0);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
        return (childAt.getHeight() + layoutParams.topMargin) + layoutParams.bottomMargin > (getHeight() - getPaddingTop()) - getPaddingBottom();
    }

    private void d0(int i10, int i11) {
        if (getChildCount() > 0) {
            View childAt = getChildAt(0);
            int e10 = e(i10, (getWidth() - getPaddingRight()) - getPaddingLeft(), childAt.getWidth());
            int e11 = e(i11, (getHeight() - getPaddingBottom()) - getPaddingTop(), childAt.getHeight());
            if (e10 == getScrollX() && e11 == getScrollY()) {
                return;
            }
            scrollTo(e10, e11);
        }
    }

    private static int e(int i10, int i11, int i12) {
        if (i11 >= i12 || i10 < 0) {
            return 0;
        }
        return i11 + i10 > i12 ? i12 - i11 : i10;
    }

    private float getVerticalScrollFactorCompat() {
        if (this.f7373s0 == 0.0f) {
            TypedValue typedValue = new TypedValue();
            Context context = getContext();
            if (context.getTheme().resolveAttribute(R.attr.listPreferredItemHeight, typedValue, true)) {
                this.f7373s0 = typedValue.getDimension(context.getResources().getDisplayMetrics());
            } else {
                throw new IllegalStateException("Expected theme to define listPreferredItemHeight.");
            }
        }
        return this.f7373s0;
    }

    private void p(int i10) {
        if (i10 != 0) {
            if (this.f7361g0) {
                Q(0, i10);
            } else {
                scrollBy(0, i10);
            }
        }
    }

    private void r() {
        this.f7359e0 = false;
        J();
        W(0);
    }

    private View t(boolean z10, int i10, int i11) {
        ArrayList focusables = getFocusables(2);
        int size = focusables.size();
        View view = null;
        boolean z11 = false;
        for (int i12 = 0; i12 < size; i12++) {
            View view2 = (View) focusables.get(i12);
            int top = view2.getTop();
            int bottom = view2.getBottom();
            if (i10 < bottom && top < i11) {
                boolean z12 = i10 < top && bottom < i11;
                if (view == null) {
                    view = view2;
                    z11 = z12;
                } else {
                    boolean z13 = (z10 && top < view.getTop()) || (!z10 && bottom > view.getBottom());
                    if (z11) {
                        if (z12) {
                            if (!z13) {
                            }
                            view = view2;
                        }
                    } else if (z12) {
                        view = view2;
                        z11 = true;
                    } else {
                        if (!z13) {
                        }
                        view = view2;
                    }
                }
            }
        }
        return view;
    }

    private boolean y(int i10, int i11) {
        if (getChildCount() <= 0) {
            return false;
        }
        int scrollY = getScrollY();
        View childAt = getChildAt(0);
        return i11 >= childAt.getTop() - scrollY && i11 < childAt.getBottom() - scrollY && i10 >= childAt.getLeft() && i10 < childAt.getRight();
    }

    private void z() {
        VelocityTracker velocityTracker = this.f7360f0;
        if (velocityTracker == null) {
            this.f7360f0 = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x006e  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x009d A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    boolean H(int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, boolean z10) {
        boolean z11;
        boolean z12;
        int overScrollMode = getOverScrollMode();
        boolean z13 = computeHorizontalScrollRange() > (computeHorizontalScrollExtent() - getPaddingLeft()) - getPaddingRight();
        boolean z14 = computeVerticalScrollRange() > (computeVerticalScrollExtent() - getPaddingTop()) - getPaddingBottom();
        boolean z15 = overScrollMode == 0 || (overScrollMode == 1 && z13);
        boolean z16 = overScrollMode == 0 || (overScrollMode == 1 && z14);
        int i18 = i12 + i10;
        int i19 = !z15 ? 0 : i16;
        int i20 = i13 + i11;
        int i21 = !z16 ? 0 : i17;
        int i22 = -i19;
        int i23 = i19 + i14;
        int i24 = -i21;
        int i25 = i21 + i15;
        if (!z15) {
            if (i18 > i23) {
                z11 = true;
                i18 = i23;
            } else if (i18 < i22) {
                z11 = true;
                i18 = i22;
            }
            if (!z16) {
                if (i20 > i25) {
                    z12 = true;
                    i20 = i25;
                } else if (i20 < i24) {
                    z12 = true;
                    i20 = i24;
                }
                if (z12 && !x(1)) {
                    this.V.springBack(i18, i20, 0, 0, 0, getScrollRange());
                }
                onOverScrolled(i18, i20, z11, z12);
                return !z11 || z12;
            }
            z12 = false;
            if (z12) {
                this.V.springBack(i18, i20, 0, 0, 0, getScrollRange());
            }
            onOverScrolled(i18, i20, z11, z12);
            if (z11) {
            }
        }
        z11 = false;
        if (!z16) {
        }
        z12 = false;
        if (z12) {
        }
        onOverScrolled(i18, i20, z11, z12);
        if (z11) {
        }
    }

    @Override // androidx.core.widget.NestedScrollView
    public boolean I(int i10) {
        boolean z10 = i10 == 130;
        int height = getHeight();
        if (z10) {
            this.U.top = getScrollY() + height;
            int childCount = getChildCount();
            if (childCount > 0) {
                View childAt = getChildAt(childCount - 1);
                int bottom = childAt.getBottom() + ((FrameLayout.LayoutParams) childAt.getLayoutParams()).bottomMargin + getPaddingBottom();
                Rect rect = this.U;
                if (rect.top + height > bottom) {
                    rect.top = bottom - height;
                }
            }
        } else {
            this.U.top = getScrollY() - height;
            Rect rect2 = this.U;
            if (rect2.top < 0) {
                rect2.top = 0;
            }
        }
        Rect rect3 = this.U;
        int i11 = rect3.top;
        int i12 = height + i11;
        rect3.bottom = i12;
        return M(i10, i11, i12);
    }

    @Override // androidx.core.widget.NestedScrollView
    public void W(int i10) {
        super.W(i10);
    }

    @Override // androidx.core.widget.NestedScrollView
    public boolean b(int i10) {
        View findFocus = findFocus();
        if (findFocus == this) {
            findFocus = null;
        }
        View findNextFocus = FocusFinder.getInstance().findNextFocus(this, findFocus, i10);
        int maxScrollAmount = getMaxScrollAmount();
        if (findNextFocus != null && E(findNextFocus, maxScrollAmount, getHeight())) {
            findNextFocus.getDrawingRect(this.U);
            offsetDescendantRectToMyCoords(findNextFocus, this.U);
            p(f(this.U));
            findNextFocus.requestFocus(i10);
        } else {
            if (i10 == 33 && getScrollY() < maxScrollAmount) {
                maxScrollAmount = getScrollY();
            } else if (i10 == 130 && getChildCount() > 0) {
                View childAt = getChildAt(0);
                maxScrollAmount = Math.min((childAt.getBottom() + ((FrameLayout.LayoutParams) childAt.getLayoutParams()).bottomMargin) - ((getScrollY() + getHeight()) - getPaddingBottom()), maxScrollAmount);
            }
            if (maxScrollAmount == 0) {
                return false;
            }
            if (i10 != 130) {
                maxScrollAmount = -maxScrollAmount;
            }
            p(maxScrollAmount);
        }
        if (findFocus == null || !findFocus.isFocused() || !C(findFocus)) {
            return true;
        }
        int descendantFocusability = getDescendantFocusability();
        setDescendantFocusability(131072);
        requestFocus();
        setDescendantFocusability(descendantFocusability);
        return true;
    }

    @Override // androidx.core.widget.NestedScrollView, android.view.View
    public void computeScroll() {
        if (this.V.computeScrollOffset()) {
            int g6 = this.V.g();
            if (!d() && getOverScrollMode() != 0 && (g6 < 0 || g6 > getScrollRange())) {
                a();
                this.V.abortAnimation();
                return;
            }
            int i10 = g6 - this.f7369o0;
            this.f7369o0 = g6;
            int[] iArr = this.f7367m0;
            iArr[1] = 0;
            h(0, i10, iArr, null, 1);
            int i11 = i10 - this.f7367m0[1];
            int scrollRange = getScrollRange();
            if (i11 != 0) {
                int scrollY = getScrollY();
                H(0, i11, getScrollX(), scrollY, 0, scrollRange, 0, this.f7371q0, false);
                int scrollY2 = getScrollY() - scrollY;
                int[] iArr2 = this.f7367m0;
                iArr2[1] = 0;
                i(0, scrollY2, 0, i11 - scrollY2, this.f7366l0, 1, iArr2);
                int i12 = this.f7367m0[1];
            }
            if (!this.V.j()) {
                ViewCompat.b0(this);
                return;
            } else {
                W(1);
                return;
            }
        }
        if (this.f7374t0) {
            this.f7374t0 = false;
        }
    }

    @Override // androidx.core.widget.NestedScrollView, android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent) || s(keyEvent);
    }

    public int getCOUIScrollRange() {
        if (getChildCount() <= 0) {
            return 0;
        }
        View childAt = getChildAt(0);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
        return Math.max(0, ((childAt.getHeight() + layoutParams.topMargin) + layoutParams.bottomMargin) - ((getHeight() - getPaddingTop()) - getPaddingBottom()));
    }

    int getScrollRange() {
        if (getChildCount() <= 0) {
            return 0;
        }
        View childAt = getChildAt(0);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
        return Math.max(0, ((childAt.getHeight() + layoutParams.topMargin) + layoutParams.bottomMargin) - ((getHeight() - getPaddingTop()) - getPaddingBottom()));
    }

    public int getScrollableRange() {
        return (getHeight() - getPaddingTop()) - getPaddingBottom();
    }

    protected void invalidateParentIfNeeded() {
        if (isHardwareAccelerated() && (getParent() instanceof View)) {
            ((View) getParent()).invalidate();
        }
    }

    @Override // androidx.core.widget.NestedScrollView, androidx.core.view.NestedScrollingParent3
    public void j(View view, int i10, int i11, int i12, int i13, int i14, int[] iArr) {
        F(i13, i14, iArr);
    }

    @Override // androidx.core.widget.NestedScrollView, androidx.core.view.NestedScrollingParent2
    public void k(View view, int i10, int i11, int i12, int i13, int i14) {
        F(i13, i14, null);
    }

    @Override // androidx.core.widget.NestedScrollView, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.f7357c0 = false;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.W.u();
    }

    @Override // androidx.core.widget.NestedScrollView, android.view.View
    public boolean onGenericMotionEvent(MotionEvent motionEvent) {
        if ((motionEvent.getSource() & 2) != 0 && motionEvent.getAction() == 8 && !this.f7359e0) {
            float axisValue = motionEvent.getAxisValue(9);
            if (axisValue != 0.0f) {
                int verticalScrollFactorCompat = (int) (axisValue * getVerticalScrollFactorCompat());
                int scrollRange = getScrollRange();
                int scrollY = getScrollY();
                int i10 = scrollY - verticalScrollFactorCompat;
                if (i10 < 0) {
                    scrollRange = 0;
                } else if (i10 <= scrollRange) {
                    scrollRange = i10;
                }
                if (scrollRange != scrollY) {
                    scrollTo(getScrollX(), scrollRange);
                    return true;
                }
            }
        }
        return false;
    }

    @Override // androidx.core.widget.NestedScrollView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 2 && this.f7359e0) {
            return true;
        }
        int i10 = action & 255;
        if (i10 != 0) {
            if (i10 != 1) {
                if (i10 == 2) {
                    int i11 = this.f7365k0;
                    if (i11 != -1) {
                        int findPointerIndex = motionEvent.findPointerIndex(i11);
                        if (findPointerIndex == -1) {
                            Log.e("COUINestedScrollView", "Invalid pointerId=" + i11 + " in onInterceptTouchEvent");
                        } else {
                            int y4 = (int) motionEvent.getY(findPointerIndex);
                            if (Math.abs(y4 - this.f7355a0) > this.f7362h0 && (2 & getNestedScrollAxes()) == 0) {
                                this.f7359e0 = true;
                                this.f7355a0 = y4;
                                B();
                                this.f7360f0.addMovement(motionEvent);
                                this.f7368n0 = 0;
                                ViewParent parent = getParent();
                                if (parent != null) {
                                    parent.requestDisallowInterceptTouchEvent(true);
                                }
                            }
                        }
                    }
                } else if (i10 != 3) {
                    if (i10 == 5) {
                        this.K = (int) motionEvent.getY(0);
                    } else if (i10 == 6) {
                        G(motionEvent);
                    }
                }
            }
            this.f7359e0 = false;
            this.f7365k0 = -1;
            J();
            if (this.V.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange())) {
                ViewCompat.b0(this);
            }
            W(0);
        } else {
            COUIIOverScroller cOUIIOverScroller = this.V;
            float e10 = cOUIIOverScroller != null ? cOUIIOverScroller.e() : 0.0f;
            boolean z10 = Math.abs(this.P) > 1500.0f;
            this.L = Math.abs(e10) > 0.0f && Math.abs(e10) < 250.0f && z10;
            this.M = b0();
            this.J = System.currentTimeMillis();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onInterceptTouchEvent: ACTION_DOWN, isFastFlingY = ");
            sb2.append(z10);
            sb2.append(", isSlowScrolling = ");
            sb2.append(this.L);
            sb2.append(", \nMath.abs(scrollVelocityY) > 0 = ");
            sb2.append(Math.abs(e10) > 0.0f);
            sb2.append(", \nscrollVelocityY = ");
            sb2.append(e10);
            sb2.append(", \nMath.abs(scrollVelocityY) < SLOW_SCROLL_THRESHOLD = ");
            sb2.append(Math.abs(e10) < 250.0f);
            sb2.append(", \nisOverScrolling = ");
            sb2.append(this.M);
            sb2.append(", scrollVelocityY = ");
            sb2.append(Math.abs(e10));
            sb2.append(", mFlingVelocityY = ");
            sb2.append(this.P);
            Log.d("COUINestedScrollView", sb2.toString());
            int y10 = (int) motionEvent.getY();
            if (!y((int) motionEvent.getX(), y10)) {
                this.f7359e0 = false;
                J();
            } else {
                this.f7355a0 = y10;
                this.K = y10;
                this.f7365k0 = motionEvent.getPointerId(0);
                z();
                this.f7360f0.addMovement(motionEvent);
                this.V.computeScrollOffset();
                this.f7359e0 = !this.V.j();
                U(2, 0);
            }
        }
        return this.f7359e0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.core.widget.NestedScrollView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        int scrollY = getScrollY();
        super.onLayout(z10, i10, i11, i12, i13);
        this.f7356b0 = false;
        View view = this.f7358d0;
        if (view != null && D(view, this)) {
            N(this.f7358d0);
        }
        this.f7358d0 = null;
        if (!this.f7357c0) {
            if (this.f7372r0 != null) {
                scrollTo(getScrollX(), this.f7372r0.f7376e);
                this.f7372r0 = null;
            }
            ViewNative.c(this, scrollY);
        }
        this.V.abortAnimation();
        ViewNative.c(this, scrollY);
        d0(getScrollX(), getScrollY());
        this.f7357c0 = true;
    }

    @Override // androidx.core.widget.NestedScrollView, android.view.ViewGroup, android.view.ViewParent
    public void onNestedScroll(View view, int i10, int i11, int i12, int i13) {
        F(i13, 0, null);
        this.f7369o0 += i13;
    }

    @Override // androidx.core.widget.NestedScrollView, android.view.View
    protected void onOverScrolled(int i10, int i11, boolean z10, boolean z11) {
        if (getScrollY() == i11 && getScrollX() == i10) {
            return;
        }
        if (b0() && this.f7374t0) {
            int scrollRange = i11 >= getScrollRange() ? getScrollRange() : 0;
            i11 = COUIPhysicalAnimationUtil.a(scrollRange, i11 - scrollRange, this.I);
        }
        if (getOverScrollMode() == 2 || (getOverScrollMode() == 1 && getChildAt(0).getHeight() <= getScrollableRange())) {
            i11 = Math.min(Math.max(i11, 0), getScrollRange());
        }
        if (getScrollY() >= 0 && i11 < 0 && this.f7374t0) {
            c0();
            this.W.notifyVerticalEdgeReached(i11, 0, this.f7371q0);
        }
        if (getScrollY() <= getScrollRange() && i11 > getScrollRange() && this.f7374t0) {
            c0();
            this.W.notifyVerticalEdgeReached(i11, getScrollRange(), this.f7371q0);
        }
        this.f7369o0 = i11;
        scrollTo(i10, i11);
        invalidateParentIfNeeded();
        awakenScrollBars();
    }

    @Override // androidx.core.widget.NestedScrollView, android.view.ViewGroup
    protected boolean onRequestFocusInDescendants(int i10, Rect rect) {
        View findNextFocusFromRect;
        if (i10 == 2) {
            i10 = 130;
        } else if (i10 == 1) {
            i10 = 33;
        }
        if (rect == null) {
            findNextFocusFromRect = FocusFinder.getInstance().findNextFocus(this, null, i10);
        } else {
            findNextFocusFromRect = FocusFinder.getInstance().findNextFocusFromRect(this, rect, i10);
        }
        if (findNextFocusFromRect == null || C(findNextFocusFromRect)) {
            return false;
        }
        return findNextFocusFromRect.requestFocus(i10, rect);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.core.widget.NestedScrollView, android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof COUISavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        COUISavedState cOUISavedState = (COUISavedState) parcelable;
        super.onRestoreInstanceState(cOUISavedState.getSuperState());
        this.f7372r0 = cOUISavedState;
        requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.core.widget.NestedScrollView, android.view.View
    public Parcelable onSaveInstanceState() {
        COUISavedState cOUISavedState = new COUISavedState(super.onSaveInstanceState());
        cOUISavedState.f7376e = getScrollY();
        return cOUISavedState;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.core.widget.NestedScrollView, android.view.View
    public void onScrollChanged(int i10, int i11, int i12, int i13) {
        super.onScrollChanged(i10, i11, i12, i13);
        b bVar = this.f7375u0;
        if (bVar != null) {
            bVar.a(this, i10, i11, i12, i13);
        }
        for (int i14 = 0; i14 < this.S.size(); i14++) {
            this.S.get(i14).a(i10, i11, i12, i13);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.core.widget.NestedScrollView, android.view.View
    public void onSizeChanged(int i10, int i11, int i12, int i13) {
        super.onSizeChanged(i10, i11, i12, i13);
        this.I = getContext().getResources().getDisplayMetrics().heightPixels;
        if (getScrollY() > getScrollRange()) {
            ViewNative.c(this, getScrollRange());
            scrollTo(getScrollX(), getScrollY());
        }
        View findFocus = findFocus();
        if (findFocus == null || this == findFocus || !E(findFocus, 0, i13)) {
            return;
        }
        findFocus.getDrawingRect(this.U);
        offsetDescendantRectToMyCoords(findFocus, this.U);
        p(f(this.U));
    }

    @Override // androidx.core.widget.NestedScrollView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        ViewParent parent;
        B();
        int actionMasked = motionEvent.getActionMasked();
        boolean z10 = false;
        if (actionMasked == 0) {
            this.f7368n0 = 0;
        }
        MotionEvent obtain = MotionEvent.obtain(motionEvent);
        obtain.offsetLocation(0.0f, this.f7368n0);
        if (actionMasked != 0) {
            if (actionMasked == 1) {
                boolean b02 = b0();
                boolean z11 = this.N && this.L;
                if (this.O && this.M && b02) {
                    z10 = true;
                }
                if (z11 || z10) {
                    Y(motionEvent);
                }
                if (this.f7359e0) {
                    B();
                    VelocityTracker velocityTracker = this.f7360f0;
                    velocityTracker.computeCurrentVelocity(1000, this.f7364j0);
                    int yVelocity = (int) velocityTracker.getYVelocity(this.f7365k0);
                    if (Math.abs(yVelocity) > this.f7363i0) {
                        int i10 = -yVelocity;
                        float f10 = i10;
                        this.V.h(f10);
                        if (getScrollY() < 0) {
                            if (yVelocity > -1500) {
                                if (this.V.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange())) {
                                    ViewCompat.b0(this);
                                }
                            } else if (!dispatchNestedPreFling(0.0f, f10)) {
                                dispatchNestedFling(0.0f, f10, true);
                                u(i10);
                            }
                        } else if (getScrollY() > getScrollRange()) {
                            if (yVelocity < 1500) {
                                if (this.V.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange())) {
                                    ViewCompat.b0(this);
                                }
                            } else if (!dispatchNestedPreFling(0.0f, f10)) {
                                dispatchNestedFling(0.0f, f10, true);
                                u(i10);
                            }
                        } else if (!dispatchNestedPreFling(0.0f, f10)) {
                            dispatchNestedFling(0.0f, f10, true);
                            u(i10);
                        }
                    } else if (this.V.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange())) {
                        ViewCompat.b0(this);
                    }
                    if (getScrollY() < 0 || getScrollY() > getScrollRange()) {
                        c0();
                    }
                    this.f7365k0 = -1;
                    r();
                }
            } else if (actionMasked == 2) {
                COUIIOverScroller cOUIIOverScroller = this.V;
                if ((cOUIIOverScroller instanceof SpringOverScroller) && this.R) {
                    ((SpringOverScroller) cOUIIOverScroller).G();
                }
                int findPointerIndex = motionEvent.findPointerIndex(this.f7365k0);
                if (findPointerIndex == -1) {
                    Log.e("COUINestedScrollView", "Invalid pointerId=" + this.f7365k0 + " in onTouchEvent");
                } else {
                    int y4 = (int) motionEvent.getY(findPointerIndex);
                    int i11 = this.f7355a0 - y4;
                    if (!this.f7359e0 && Math.abs(i11) > this.f7362h0) {
                        ViewParent parent2 = getParent();
                        if (parent2 != null) {
                            parent2.requestDisallowInterceptTouchEvent(true);
                        }
                        this.f7359e0 = true;
                        i11 = i11 > 0 ? i11 - this.f7362h0 : i11 + this.f7362h0;
                    }
                    int i12 = i11;
                    if (this.f7359e0) {
                        if (h(0, i12, this.f7367m0, this.f7366l0, 0)) {
                            i12 -= this.f7367m0[1];
                            this.f7368n0 += this.f7366l0[1];
                        }
                        this.f7355a0 = y4 - this.f7366l0[1];
                        int scrollY = getScrollY();
                        int scrollRange = getScrollRange();
                        if (getScrollY() < 0) {
                            i12 = COUIPhysicalAnimationUtil.b(i12, getScrollY(), this.f7370p0);
                        } else if (getScrollY() > getScrollRange()) {
                            i12 = COUIPhysicalAnimationUtil.b(i12, getScrollY() - getScrollRange(), this.f7370p0);
                        }
                        int i13 = i12;
                        if (H(0, i13, 0, getScrollY(), 0, scrollRange, 0, this.f7371q0, true) && !x(0)) {
                            this.f7360f0.clear();
                        }
                        int scrollY2 = getScrollY() - scrollY;
                        int[] iArr = this.f7367m0;
                        iArr[1] = 0;
                        i(0, scrollY2, 0, i13 - scrollY2, this.f7366l0, 0, iArr);
                        int i14 = this.f7355a0;
                        int[] iArr2 = this.f7366l0;
                        this.f7355a0 = i14 - iArr2[1];
                        this.f7368n0 += iArr2[1];
                    }
                }
            } else if (actionMasked == 3) {
                if (this.f7359e0 && getChildCount() > 0 && this.V.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange())) {
                    ViewCompat.b0(this);
                }
                this.f7365k0 = -1;
                r();
            } else if (actionMasked == 5) {
                int actionIndex = motionEvent.getActionIndex();
                int y10 = (int) motionEvent.getY(actionIndex);
                this.f7355a0 = y10;
                this.K = y10;
                this.f7365k0 = motionEvent.getPointerId(actionIndex);
            } else if (actionMasked == 6) {
                G(motionEvent);
                this.f7355a0 = (int) motionEvent.getY(motionEvent.findPointerIndex(this.f7365k0));
            }
        } else {
            if (getChildCount() == 0) {
                return false;
            }
            boolean z12 = !this.V.j();
            this.f7359e0 = z12;
            if (z12 && (parent = getParent()) != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
            if (!this.V.j()) {
                this.V.abortAnimation();
                if (this.f7374t0) {
                    this.f7374t0 = false;
                }
            }
            int y11 = (int) motionEvent.getY();
            this.f7355a0 = y11;
            this.K = y11;
            this.f7365k0 = motionEvent.getPointerId(0);
            U(2, 0);
        }
        VelocityTracker velocityTracker2 = this.f7360f0;
        if (velocityTracker2 != null) {
            velocityTracker2.addMovement(obtain);
        }
        obtain.recycle();
        return true;
    }

    @Override // android.view.View
    protected void onVisibilityChanged(View view, int i10) {
        super.onVisibilityChanged(view, i10);
        if (i10 != 0) {
            this.W.abortAnimation();
            this.W.u();
        }
    }

    @Override // androidx.core.widget.NestedScrollView, android.view.ViewGroup, android.view.ViewParent
    public void requestChildFocus(View view, View view2) {
        if (!this.f7356b0) {
            N(view2);
        } else {
            this.f7358d0 = view2;
        }
        super.requestChildFocus(view, view2);
    }

    @Override // androidx.core.widget.NestedScrollView, android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean z10) {
        if (z10) {
            J();
        }
        super.requestDisallowInterceptTouchEvent(z10);
    }

    @Override // androidx.core.widget.NestedScrollView, android.view.View, android.view.ViewParent
    public void requestLayout() {
        this.f7356b0 = true;
        super.requestLayout();
    }

    @Override // androidx.core.widget.NestedScrollView
    public boolean s(KeyEvent keyEvent) {
        this.U.setEmpty();
        if (!d()) {
            if (!isFocused() || keyEvent.getKeyCode() == 4) {
                return false;
            }
            View findFocus = findFocus();
            if (findFocus == this) {
                findFocus = null;
            }
            View findNextFocus = FocusFinder.getInstance().findNextFocus(this, findFocus, 130);
            return (findNextFocus == null || findNextFocus == this || !findNextFocus.requestFocus(130)) ? false : true;
        }
        if (keyEvent.getAction() != 0) {
            return false;
        }
        int keyCode = keyEvent.getKeyCode();
        if (keyCode == 19) {
            if (!keyEvent.isAltPressed()) {
                return b(33);
            }
            return v(33);
        }
        if (keyCode == 20) {
            if (!keyEvent.isAltPressed()) {
                return b(130);
            }
            return v(130);
        }
        if (keyCode != 62) {
            return false;
        }
        I(keyEvent.isShiftPressed() ? 33 : 130);
        return false;
    }

    @Override // androidx.core.widget.NestedScrollView, android.view.View
    public void scrollTo(int i10, int i11) {
        if (getChildCount() > 0) {
            if (getScrollX() == i10 && getScrollY() == i11) {
                return;
            }
            int scrollX = getScrollX();
            int scrollY = getScrollY();
            ViewNative.b(this, i10);
            ViewNative.c(this, i11);
            onScrollChanged(i10, i11, scrollX, scrollY);
            if (awakenScrollBars()) {
                return;
            }
            postInvalidateOnAnimation();
        }
    }

    public void setEnableFlingSpeedIncrease(boolean z10) {
        SpringOverScroller springOverScroller = this.W;
        if (springOverScroller != null) {
            springOverScroller.B(z10);
        }
    }

    public void setIsUseOptimizedScroll(boolean z10) {
        this.R = z10;
    }

    public void setItemClickableWhileOverScrolling(boolean z10) {
        this.O = z10;
    }

    public void setItemClickableWhileSlowScrolling(boolean z10) {
        this.N = z10;
    }

    public void setOnScrollChangeListener(b bVar) {
        this.f7375u0 = bVar;
    }

    @Override // androidx.core.widget.NestedScrollView
    public void setSmoothScrollingEnabled(boolean z10) {
        this.f7361g0 = z10;
    }

    public void setSpringOverScrollerDebug(boolean z10) {
        this.W.A(z10);
    }

    @Override // androidx.core.widget.NestedScrollView
    public void u(int i10) {
        this.P = i10;
        if (getChildCount() > 0) {
            this.V.fling(getScrollX(), getScrollY(), 0, i10, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
            L(true);
            if (this.f7374t0) {
                return;
            }
            this.f7374t0 = true;
        }
    }

    @Override // androidx.core.widget.NestedScrollView
    public boolean v(int i10) {
        int childCount;
        boolean z10 = i10 == 130;
        int height = getHeight();
        Rect rect = this.U;
        rect.top = 0;
        rect.bottom = height;
        if (z10 && (childCount = getChildCount()) > 0) {
            View childAt = getChildAt(childCount - 1);
            this.U.bottom = childAt.getBottom() + ((FrameLayout.LayoutParams) childAt.getLayoutParams()).bottomMargin + getPaddingBottom();
            Rect rect2 = this.U;
            rect2.top = rect2.bottom - height;
        }
        Rect rect3 = this.U;
        return M(i10, rect3.top, rect3.bottom);
    }

    public COUINestedScrollView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUINestedScrollView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.I = 0;
        this.N = true;
        this.O = true;
        this.Q = new Paint();
        this.R = true;
        this.S = new ArrayList<>();
        this.T = true;
        this.U = new Rect();
        this.V = null;
        this.W = null;
        this.f7356b0 = true;
        this.f7357c0 = false;
        this.f7358d0 = null;
        this.f7359e0 = false;
        this.f7361g0 = true;
        this.f7365k0 = -1;
        this.f7366l0 = new int[2];
        this.f7367m0 = new int[2];
        this.f7374t0 = false;
        Z(context);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIScrollView, i10, 0);
        this.T = obtainStyledAttributes.getBoolean(R$styleable.COUIScrollView_couiScrollViewEnableVibrator, true);
        obtainStyledAttributes.recycle();
    }
}
