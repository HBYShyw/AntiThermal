package com.coui.appcompat.scrollview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import com.support.appcompat.R$styleable;
import java.util.ArrayList;
import l3.ViewNative;
import m1.COUIPhysicalAnimationUtil;
import o2.COUIIOverScroller;
import o2.SpringOverScroller;

/* loaded from: classes.dex */
public class COUIScrollView extends ScrollView {
    private boolean A;
    private boolean B;
    private COUISavedState C;
    private long D;
    private int E;
    private boolean F;
    private boolean G;
    private boolean H;
    private boolean I;
    private float J;
    private Paint K;
    private boolean L;
    private boolean M;

    /* renamed from: e, reason: collision with root package name */
    private int f7377e;

    /* renamed from: f, reason: collision with root package name */
    private long f7378f;

    /* renamed from: g, reason: collision with root package name */
    private final Rect f7379g;

    /* renamed from: h, reason: collision with root package name */
    private COUIIOverScroller f7380h;

    /* renamed from: i, reason: collision with root package name */
    private SpringOverScroller f7381i;

    /* renamed from: j, reason: collision with root package name */
    private int f7382j;

    /* renamed from: k, reason: collision with root package name */
    private boolean f7383k;

    /* renamed from: l, reason: collision with root package name */
    private View f7384l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f7385m;

    /* renamed from: n, reason: collision with root package name */
    private VelocityTracker f7386n;

    /* renamed from: o, reason: collision with root package name */
    private boolean f7387o;

    /* renamed from: p, reason: collision with root package name */
    private boolean f7388p;

    /* renamed from: q, reason: collision with root package name */
    private int f7389q;

    /* renamed from: r, reason: collision with root package name */
    private int f7390r;

    /* renamed from: s, reason: collision with root package name */
    private int f7391s;

    /* renamed from: t, reason: collision with root package name */
    private int f7392t;

    /* renamed from: u, reason: collision with root package name */
    private int f7393u;

    /* renamed from: v, reason: collision with root package name */
    private float f7394v;

    /* renamed from: w, reason: collision with root package name */
    private int f7395w;

    /* renamed from: x, reason: collision with root package name */
    private final int[] f7396x;

    /* renamed from: y, reason: collision with root package name */
    private final int[] f7397y;

    /* renamed from: z, reason: collision with root package name */
    private int f7398z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class COUISavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<COUISavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        public int f7399e;

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
            return "ScrollView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " scrollPosition=" + this.f7399e + "}";
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeInt(this.f7399e);
        }

        public COUISavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.f7399e = parcel.readInt();
        }
    }

    public COUIScrollView(Context context) {
        super(context);
        this.f7377e = 0;
        this.f7379g = new Rect();
        this.f7380h = null;
        this.f7381i = null;
        this.f7383k = true;
        this.f7384l = null;
        this.f7385m = false;
        this.f7388p = true;
        this.f7395w = -1;
        this.f7396x = new int[2];
        this.f7397y = new int[2];
        this.A = false;
        this.B = false;
        this.H = true;
        this.I = true;
        this.K = new Paint();
        this.L = true;
        this.M = true;
        j(context);
    }

    private boolean a() {
        View childAt = getChildAt(0);
        if (childAt != null) {
            return getHeight() < (childAt.getHeight() + getPaddingTop()) + getPaddingBottom();
        }
        return false;
    }

    private static int b(int i10, int i11, int i12) {
        if (i11 >= i12 || i10 < 0) {
            return 0;
        }
        return i11 + i10 > i12 ? i12 - i11 : i10;
    }

    private boolean c(View view, MotionEvent motionEvent) {
        int[] iArr = {0, 1};
        boolean z10 = true;
        for (int i10 = 0; i10 < 2; i10++) {
            motionEvent.setAction(iArr[i10]);
            z10 &= view.dispatchTouchEvent(motionEvent);
        }
        return z10;
    }

    private void d(int i10) {
        if (i10 != 0) {
            if (this.f7388p) {
                x(0, i10);
            } else {
                scrollBy(0, i10);
            }
        }
    }

    private void e() {
        this.f7385m = false;
        t();
        if (this.A) {
            this.A = false;
        }
    }

    private View f(boolean z10, int i10, int i11) {
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

    private View g(MotionEvent motionEvent) {
        View view = null;
        if (!m(motionEvent)) {
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
                if (contains && c(childAt, obtain)) {
                    view = childAt;
                }
            }
        }
        Log.d("COUIScrollView", "findViewToDispatchClickEvent: target: " + view);
        return view;
    }

    private int getScrollRange() {
        if (getChildCount() > 0) {
            return Math.max(0, getChildAt(0).getHeight() - ((getHeight() - getPaddingBottom()) - getPaddingTop()));
        }
        return 0;
    }

    private void h(int i10) {
        boolean z10 = (getScrollY() > 0 || i10 > 0) && (getScrollY() < getScrollRange() || i10 < 0);
        float f10 = i10;
        if (dispatchNestedPreFling(0.0f, f10)) {
            return;
        }
        dispatchNestedFling(0.0f, f10, z10);
        if (z10) {
            fling(i10);
        }
    }

    private boolean i(int i10, int i11) {
        if (getChildCount() <= 0) {
            return false;
        }
        int scrollY = getScrollY();
        View childAt = getChildAt(0);
        return i11 >= childAt.getTop() - scrollY && i11 < childAt.getBottom() - scrollY && i10 >= childAt.getLeft() && i10 < childAt.getRight();
    }

    private void j(Context context) {
        if (this.f7380h == null) {
            SpringOverScroller springOverScroller = new SpringOverScroller(context);
            this.f7381i = springOverScroller;
            springOverScroller.F(2.15f);
            this.f7381i.C(true);
            this.f7380h = this.f7381i;
            setEnableFlingSpeedIncrease(true);
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.f7389q = viewConfiguration.getScaledTouchSlop();
        this.f7390r = viewConfiguration.getScaledMinimumFlingVelocity();
        this.f7391s = viewConfiguration.getScaledMaximumFlingVelocity();
        int i10 = displayMetrics.heightPixels;
        this.f7392t = i10;
        this.f7393u = i10;
        this.f7394v = viewConfiguration.getScaledVerticalScrollFactor();
        this.f7377e = displayMetrics.heightPixels;
    }

    private void k() {
        VelocityTracker velocityTracker = this.f7386n;
        if (velocityTracker == null) {
            this.f7386n = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
    }

    private void l() {
        if (this.f7386n == null) {
            this.f7386n = VelocityTracker.obtain();
        }
    }

    private boolean m(MotionEvent motionEvent) {
        int y4 = (int) (motionEvent.getY() - this.E);
        return System.currentTimeMillis() - this.D < 100 && ((int) Math.sqrt((double) (y4 * y4))) < 10;
    }

    private boolean n(View view) {
        return !q(view, 0, getHeight());
    }

    private boolean o() {
        return getScrollY() < 0 || getScrollY() > getScrollRange();
    }

    private static boolean p(View view, View view2) {
        if (view == view2) {
            return true;
        }
        Object parent = view.getParent();
        return (parent instanceof ViewGroup) && p((View) parent, view2);
    }

    private boolean q(View view, int i10, int i11) {
        view.getDrawingRect(this.f7379g);
        offsetDescendantRectToMyCoords(view, this.f7379g);
        return this.f7379g.bottom + i10 >= getScrollY() && this.f7379g.top - i10 <= getScrollY() + i11;
    }

    private void r(MotionEvent motionEvent) {
        int action = (motionEvent.getAction() & 65280) >> 8;
        if (motionEvent.getPointerId(action) == this.f7395w) {
            int i10 = action == 0 ? 1 : 0;
            int y4 = (int) motionEvent.getY(i10);
            this.f7382j = y4;
            this.E = y4;
            this.f7395w = motionEvent.getPointerId(i10);
            VelocityTracker velocityTracker = this.f7386n;
            if (velocityTracker != null) {
                velocityTracker.clear();
            }
        }
    }

    private void s() {
        if (this.M) {
            performHapticFeedback(307);
        }
    }

    private void t() {
        VelocityTracker velocityTracker = this.f7386n;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.f7386n = null;
        }
    }

    private boolean u(int i10, int i11, int i12) {
        int height = getHeight();
        int scrollY = getScrollY();
        int i13 = height + scrollY;
        boolean z10 = false;
        boolean z11 = i10 == 33;
        View f10 = f(z11, i11, i12);
        if (f10 == null) {
            f10 = this;
        }
        if (i11 < scrollY || i12 > i13) {
            d(z11 ? i11 - scrollY : i12 - i13);
            z10 = true;
        }
        if (f10 != findFocus()) {
            f10.requestFocus(i10);
        }
        return z10;
    }

    private boolean v(Rect rect, boolean z10) {
        int computeScrollDeltaToGetChildRectOnScreen = computeScrollDeltaToGetChildRectOnScreen(rect);
        boolean z11 = computeScrollDeltaToGetChildRectOnScreen != 0;
        if (z11) {
            if (z10) {
                scrollBy(0, computeScrollDeltaToGetChildRectOnScreen);
            } else {
                x(0, computeScrollDeltaToGetChildRectOnScreen);
            }
        }
        return z11;
    }

    private void w(int i10, int i11) {
        if (getChildCount() > 0) {
            View childAt = getChildAt(0);
            int b10 = b(i10, (getWidth() - getPaddingRight()) - getPaddingLeft(), childAt.getWidth());
            int b11 = b(i11, (getHeight() - getPaddingBottom()) - getPaddingTop(), childAt.getHeight());
            if (b10 == getScrollX() && b11 == getScrollY()) {
                return;
            }
            scrollTo(b10, b11);
        }
    }

    @Override // android.widget.ScrollView
    public boolean arrowScroll(int i10) {
        int bottom;
        View findFocus = findFocus();
        if (findFocus == this) {
            findFocus = null;
        }
        View findNextFocus = FocusFinder.getInstance().findNextFocus(this, findFocus, i10);
        int maxScrollAmount = getMaxScrollAmount();
        if (findNextFocus != null && q(findNextFocus, maxScrollAmount, getHeight())) {
            findNextFocus.getDrawingRect(this.f7379g);
            offsetDescendantRectToMyCoords(findNextFocus, this.f7379g);
            d(computeScrollDeltaToGetChildRectOnScreen(this.f7379g));
            findNextFocus.requestFocus(i10);
        } else {
            if (i10 == 33 && getScrollY() < maxScrollAmount) {
                maxScrollAmount = getScrollY();
            } else if (i10 == 130 && getChildCount() > 0 && (bottom = getChildAt(0).getBottom() - ((getScrollY() + getHeight()) - getPaddingBottom())) < maxScrollAmount) {
                maxScrollAmount = bottom;
            }
            if (maxScrollAmount == 0) {
                return false;
            }
            if (i10 != 130) {
                maxScrollAmount = -maxScrollAmount;
            }
            d(maxScrollAmount);
        }
        if (findFocus == null || !findFocus.isFocused() || !n(findFocus)) {
            return true;
        }
        int descendantFocusability = getDescendantFocusability();
        setDescendantFocusability(131072);
        requestFocus();
        setDescendantFocusability(descendantFocusability);
        return true;
    }

    @Override // android.widget.ScrollView, android.view.View
    public void computeScroll() {
        if (this.f7380h.computeScrollOffset()) {
            int scrollX = getScrollX();
            int scrollY = getScrollY();
            int b10 = this.f7380h.b();
            int g6 = this.f7380h.g();
            if (scrollX != b10 || scrollY != g6) {
                overScrollBy(b10 - scrollX, g6 - scrollY, scrollX, scrollY, 0, getScrollRange(), 0, this.f7393u, false);
            }
            if (awakenScrollBars()) {
                return;
            }
            postInvalidateOnAnimation();
            return;
        }
        if (this.B) {
            this.B = false;
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent) || executeKeyEvent(keyEvent);
    }

    @Override // android.widget.ScrollView
    public boolean executeKeyEvent(KeyEvent keyEvent) {
        this.f7379g.setEmpty();
        if (!a()) {
            if (!isFocused() || keyEvent.getKeyCode() == 4 || keyEvent.getKeyCode() == 111) {
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
                return arrowScroll(33);
            }
            return fullScroll(33);
        }
        if (keyCode == 20) {
            if (!keyEvent.isAltPressed()) {
                return arrowScroll(130);
            }
            return fullScroll(130);
        }
        if (keyCode != 62) {
            return false;
        }
        pageScroll(keyEvent.isShiftPressed() ? 33 : 130);
        return false;
    }

    @Override // android.widget.ScrollView
    public void fling(int i10) {
        this.J = i10;
        if (getChildCount() > 0) {
            int height = (getHeight() - getPaddingBottom()) - getPaddingTop();
            this.f7380h.fling(getScrollX(), getScrollY(), 0, i10, 0, 0, 0, Math.max(0, getChildAt(0).getHeight() - height), 0, height / 2);
            if (!this.B) {
                this.B = true;
            }
            postInvalidateOnAnimation();
        }
    }

    @Override // android.widget.ScrollView
    public boolean fullScroll(int i10) {
        int childCount;
        boolean z10 = i10 == 130;
        int height = getHeight();
        Rect rect = this.f7379g;
        rect.top = 0;
        rect.bottom = height;
        if (z10 && (childCount = getChildCount()) > 0) {
            this.f7379g.bottom = getChildAt(childCount - 1).getBottom() + getPaddingBottom();
            Rect rect2 = this.f7379g;
            rect2.top = rect2.bottom - height;
        }
        Rect rect3 = this.f7379g;
        return u(i10, rect3.top, rect3.bottom);
    }

    public int getScrollableRange() {
        return (getHeight() - getPaddingTop()) - getPaddingBottom();
    }

    protected void invalidateParentIfNeeded() {
        if (isHardwareAccelerated() && (getParent() instanceof View)) {
            ((View) getParent()).invalidate();
        }
    }

    @Override // android.widget.ScrollView
    public boolean isFillViewport() {
        return this.f7387o;
    }

    @Override // android.widget.ScrollView
    public boolean isSmoothScrollingEnabled() {
        return this.f7388p;
    }

    @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.A) {
            this.A = false;
        }
        if (this.B) {
            this.B = false;
        }
        this.f7381i.u();
    }

    @Override // android.widget.ScrollView, android.view.View
    public boolean onGenericMotionEvent(MotionEvent motionEvent) {
        float axisValue;
        if (motionEvent.getAction() == 8) {
            if (motionEvent.isFromSource(2)) {
                axisValue = motionEvent.getAxisValue(9);
            } else {
                axisValue = motionEvent.isFromSource(4194304) ? motionEvent.getAxisValue(26) : 0.0f;
            }
            int round = Math.round(axisValue * this.f7394v);
            if (round != 0) {
                int scrollRange = getScrollRange();
                int scrollY = getScrollY();
                int i10 = scrollY - round;
                if (i10 < 0) {
                    scrollRange = 0;
                } else if (i10 <= scrollRange) {
                    scrollRange = i10;
                }
                if (scrollRange != scrollY) {
                    super.scrollTo(getScrollX(), scrollRange);
                    return true;
                }
            }
        }
        return super.onGenericMotionEvent(motionEvent);
    }

    @Override // android.widget.ScrollView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if ((action == 2 && this.f7385m) || super.onInterceptTouchEvent(motionEvent)) {
            return true;
        }
        int i10 = action & 255;
        if (i10 != 0) {
            if (i10 != 1) {
                if (i10 == 2) {
                    int i11 = this.f7395w;
                    if (i11 != -1) {
                        int findPointerIndex = motionEvent.findPointerIndex(i11);
                        if (findPointerIndex == -1) {
                            Log.e("COUIScrollView", "Invalid pointerId=" + i11 + " in onInterceptTouchEvent");
                        } else {
                            int y4 = (int) motionEvent.getY(findPointerIndex);
                            if (Math.abs(y4 - this.f7382j) > this.f7389q && (2 & getNestedScrollAxes()) == 0) {
                                this.f7385m = true;
                                this.f7382j = y4;
                                l();
                                this.f7386n.addMovement(motionEvent);
                                this.f7398z = 0;
                                if (!this.A) {
                                    this.A = true;
                                }
                                ViewParent parent = getParent();
                                if (parent != null) {
                                    parent.requestDisallowInterceptTouchEvent(true);
                                }
                            }
                        }
                    }
                } else if (i10 != 3) {
                    if (i10 == 5) {
                        this.E = (int) motionEvent.getY(0);
                    } else if (i10 == 6) {
                        r(motionEvent);
                    }
                }
            }
            this.f7385m = false;
            this.f7395w = -1;
            t();
            if (this.f7380h.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange())) {
                postInvalidateOnAnimation();
            }
            stopNestedScroll();
        } else {
            COUIIOverScroller cOUIIOverScroller = this.f7380h;
            float e10 = cOUIIOverScroller != null ? cOUIIOverScroller.e() : 0.0f;
            this.F = Math.abs(e10) > 0.0f && Math.abs(e10) < 250.0f && ((Math.abs(this.J) > 1500.0f ? 1 : (Math.abs(this.J) == 1500.0f ? 0 : -1)) > 0);
            this.G = o();
            this.D = System.currentTimeMillis();
            int y10 = (int) motionEvent.getY();
            if (!i((int) motionEvent.getX(), y10)) {
                this.f7385m = false;
                t();
            } else {
                this.f7382j = y10;
                this.E = y10;
                this.f7395w = motionEvent.getPointerId(0);
                k();
                this.f7386n.addMovement(motionEvent);
                this.f7380h.computeScrollOffset();
                boolean z10 = !this.f7380h.j();
                this.f7385m = z10;
                if (z10 && !this.A) {
                    this.A = true;
                }
                startNestedScroll(2);
            }
        }
        return this.f7385m;
    }

    @Override // android.widget.ScrollView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        super.onLayout(z10, i10, i11, i12, i13);
        this.f7383k = false;
        View view = this.f7384l;
        if (view != null && p(view, this)) {
            scrollToDescendant(this.f7384l);
        }
        this.f7384l = null;
        if (!isLaidOut()) {
            COUISavedState cOUISavedState = this.C;
            if (cOUISavedState != null) {
                ViewNative.c(this, cOUISavedState.f7399e);
                this.C = null;
            }
            int max = Math.max(0, (getChildCount() > 0 ? getChildAt(0).getMeasuredHeight() : 0) - (((i13 - i11) - getPaddingBottom()) - getPaddingTop()));
            if (getScrollY() > max) {
                ViewNative.c(this, max);
            } else if (getScrollY() < 0) {
                ViewNative.c(this, 0);
            }
        }
        this.f7380h.abortAnimation();
        w(getScrollX(), getScrollY());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.ScrollView, android.widget.FrameLayout, android.view.View
    public void onMeasure(int i10, int i11) {
        int paddingLeft;
        int paddingTop;
        int paddingBottom;
        super.onMeasure(i10, i11);
        if (this.f7387o && View.MeasureSpec.getMode(i11) != 0 && getChildCount() > 0) {
            View childAt = getChildAt(0);
            int i12 = getContext().getApplicationInfo().targetSdkVersion;
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
            if (i12 >= 23) {
                paddingLeft = getPaddingLeft() + getPaddingRight() + layoutParams.leftMargin + layoutParams.rightMargin;
                paddingTop = getPaddingTop() + getPaddingBottom() + layoutParams.topMargin;
                paddingBottom = layoutParams.bottomMargin;
            } else {
                paddingLeft = getPaddingLeft() + getPaddingRight();
                paddingTop = getPaddingTop();
                paddingBottom = getPaddingBottom();
            }
            int measuredHeight = getMeasuredHeight() - (paddingTop + paddingBottom);
            if (childAt.getMeasuredHeight() < measuredHeight) {
                childAt.measure(ScrollView.getChildMeasureSpec(i10, paddingLeft, layoutParams.width), View.MeasureSpec.makeMeasureSpec(measuredHeight, 1073741824));
            }
        }
    }

    @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.ViewParent
    public boolean onNestedFling(View view, float f10, float f11, boolean z10) {
        if (z10) {
            return false;
        }
        h((int) f11);
        return true;
    }

    @Override // android.widget.ScrollView, android.view.View
    protected void onOverScrolled(int i10, int i11, boolean z10, boolean z11) {
        if (getScrollY() == i11 && getScrollX() == i10) {
            return;
        }
        if (o() && this.B) {
            int scrollRange = i11 >= getScrollRange() ? getScrollRange() : 0;
            i11 = COUIPhysicalAnimationUtil.a(scrollRange, i11 - scrollRange, this.f7377e);
        }
        if (getOverScrollMode() == 2 || (getOverScrollMode() == 1 && getChildAt(0).getHeight() <= getScrollableRange())) {
            i11 = Math.min(Math.max(i11, 0), getScrollRange());
        }
        if (getScrollY() >= 0 && i11 < 0 && this.B) {
            s();
            this.f7381i.notifyVerticalEdgeReached(i11, 0, this.f7393u);
        }
        if (getScrollY() <= getScrollRange() && i11 > getScrollRange() && this.B) {
            s();
            this.f7381i.notifyVerticalEdgeReached(i11, getScrollRange(), this.f7393u);
        }
        scrollTo(i10, i11);
        invalidateParentIfNeeded();
        awakenScrollBars();
    }

    @Override // android.widget.ScrollView, android.view.ViewGroup
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
        if (findNextFocusFromRect == null || n(findNextFocusFromRect)) {
            return false;
        }
        return findNextFocusFromRect.requestFocus(i10, rect);
    }

    @Override // android.widget.ScrollView, android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (getContext().getApplicationInfo().targetSdkVersion <= 18) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        COUISavedState cOUISavedState = (COUISavedState) parcelable;
        super.onRestoreInstanceState(cOUISavedState.getSuperState());
        this.C = cOUISavedState;
        requestLayout();
    }

    @Override // android.widget.ScrollView, android.view.View
    protected Parcelable onSaveInstanceState() {
        if (getContext().getApplicationInfo().targetSdkVersion <= 18) {
            return super.onSaveInstanceState();
        }
        COUISavedState cOUISavedState = new COUISavedState(super.onSaveInstanceState());
        cOUISavedState.f7399e = getScrollY();
        return cOUISavedState;
    }

    @Override // android.widget.ScrollView, android.view.View
    protected void onSizeChanged(int i10, int i11, int i12, int i13) {
        super.onSizeChanged(i10, i11, i12, i13);
        this.f7377e = getContext().getResources().getDisplayMetrics().heightPixels;
        View findFocus = findFocus();
        if (findFocus == null || this == findFocus || !q(findFocus, 0, i13)) {
            return;
        }
        findFocus.getDrawingRect(this.f7379g);
        offsetDescendantRectToMyCoords(findFocus, this.f7379g);
        d(computeScrollDeltaToGetChildRectOnScreen(this.f7379g));
    }

    /* JADX WARN: Removed duplicated region for block: B:66:0x0179  */
    @Override // android.widget.ScrollView, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        ViewParent parent;
        int i10;
        int b10;
        int scrollY;
        l();
        MotionEvent obtain = MotionEvent.obtain(motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        boolean z10 = false;
        if (actionMasked == 0) {
            this.f7398z = 0;
        }
        obtain.offsetLocation(0.0f, this.f7398z);
        if (actionMasked != 0) {
            if (actionMasked == 1) {
                boolean o10 = o();
                boolean z11 = this.H && this.F;
                if (this.I && this.G && o10) {
                    z10 = true;
                }
                if (z11 || z10) {
                    g(motionEvent);
                }
                if (this.f7385m) {
                    l();
                    VelocityTracker velocityTracker = this.f7386n;
                    velocityTracker.computeCurrentVelocity(1000, this.f7391s);
                    int yVelocity = (int) velocityTracker.getYVelocity(this.f7395w);
                    if (Math.abs(yVelocity) > this.f7390r) {
                        int i11 = -yVelocity;
                        this.f7380h.h(i11);
                        if (getScrollY() < 0) {
                            if (yVelocity > -1500) {
                                if (this.f7380h.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange())) {
                                    postInvalidateOnAnimation();
                                }
                            } else {
                                h(i11);
                            }
                        } else if (getScrollY() <= getScrollRange()) {
                            h(i11);
                        } else if (yVelocity < 1500) {
                            if (this.f7380h.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange())) {
                                postInvalidateOnAnimation();
                            }
                        } else {
                            h(i11);
                        }
                    } else if (this.f7380h.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange())) {
                        postInvalidateOnAnimation();
                    }
                    if (getScrollY() < 0 || getScrollY() > getScrollRange()) {
                        s();
                    }
                    this.f7395w = -1;
                    e();
                } else if (this.f7380h.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange())) {
                    postInvalidateOnAnimation();
                }
            } else if (actionMasked == 2) {
                COUIIOverScroller cOUIIOverScroller = this.f7380h;
                if ((cOUIIOverScroller instanceof SpringOverScroller) && this.L) {
                    ((SpringOverScroller) cOUIIOverScroller).G();
                }
                int findPointerIndex = motionEvent.findPointerIndex(this.f7395w);
                if (findPointerIndex == -1) {
                    Log.e("COUIScrollView", "Invalid pointerId=" + this.f7395w + " in onTouchEvent");
                } else {
                    int y4 = (int) motionEvent.getY(findPointerIndex);
                    int i12 = this.f7382j - y4;
                    if (dispatchNestedPreScroll(0, i12, this.f7397y, this.f7396x)) {
                        i12 -= this.f7397y[1];
                        obtain.offsetLocation(0.0f, this.f7396x[1]);
                        this.f7398z += this.f7396x[1];
                    }
                    if (!this.f7385m && Math.abs(i12) > this.f7389q) {
                        ViewParent parent2 = getParent();
                        if (parent2 != null) {
                            parent2.requestDisallowInterceptTouchEvent(true);
                        }
                        this.f7385m = true;
                        if (i12 > 0) {
                            i12 -= this.f7389q;
                        } else {
                            i12 += this.f7389q;
                        }
                    }
                    int i13 = i12;
                    if (this.f7385m) {
                        this.f7382j = y4 - this.f7396x[1];
                        int scrollY2 = getScrollY();
                        int scrollRange = getScrollRange();
                        getOverScrollMode();
                        if (getScrollY() < 0) {
                            b10 = COUIPhysicalAnimationUtil.b(i13, getScrollY(), this.f7392t);
                        } else if (getScrollY() > getScrollRange()) {
                            b10 = COUIPhysicalAnimationUtil.b(i13, getScrollY() - getScrollRange(), this.f7392t);
                        } else {
                            i10 = i13;
                            if (overScrollBy(0, i10, 0, getScrollY(), 0, scrollRange, 0, this.f7392t, true) && !hasNestedScrollingParent()) {
                                this.f7386n.clear();
                            }
                            scrollY = getScrollY() - scrollY2;
                            if (dispatchNestedScroll(0, scrollY, 0, i13 - scrollY, this.f7396x)) {
                                this.f7382j = this.f7382j - this.f7396x[1];
                                obtain.offsetLocation(0.0f, r1[1]);
                                this.f7398z += this.f7396x[1];
                            }
                        }
                        i10 = b10;
                        if (overScrollBy(0, i10, 0, getScrollY(), 0, scrollRange, 0, this.f7392t, true)) {
                            this.f7386n.clear();
                        }
                        scrollY = getScrollY() - scrollY2;
                        if (dispatchNestedScroll(0, scrollY, 0, i13 - scrollY, this.f7396x)) {
                        }
                    }
                }
            } else if (actionMasked != 3) {
                if (actionMasked == 5) {
                    int actionIndex = motionEvent.getActionIndex();
                    int y10 = (int) motionEvent.getY(actionIndex);
                    this.f7382j = y10;
                    this.E = y10;
                    this.f7395w = motionEvent.getPointerId(actionIndex);
                } else if (actionMasked == 6) {
                    r(motionEvent);
                    this.f7382j = (int) motionEvent.getY(motionEvent.findPointerIndex(this.f7395w));
                }
            } else if (this.f7385m && getChildCount() > 0) {
                if (this.f7380h.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange())) {
                    postInvalidateOnAnimation();
                }
                this.f7395w = -1;
                e();
            }
        } else {
            if (getChildCount() == 0) {
                return false;
            }
            if (!this.f7380h.j() && (parent = getParent()) != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
            if (!this.f7380h.j()) {
                this.f7380h.abortAnimation();
                if (this.B) {
                    this.B = false;
                }
            }
            int y11 = (int) motionEvent.getY();
            this.f7382j = y11;
            this.E = y11;
            this.f7395w = motionEvent.getPointerId(0);
            startNestedScroll(2);
        }
        VelocityTracker velocityTracker2 = this.f7386n;
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
            this.f7381i.abortAnimation();
            this.f7381i.u();
        }
    }

    @Override // android.view.View
    protected boolean overScrollBy(int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, boolean z10) {
        onOverScrolled(i12 + i10, i13 + i11, false, false);
        return false;
    }

    @Override // android.widget.ScrollView
    public boolean pageScroll(int i10) {
        boolean z10 = i10 == 130;
        int height = getHeight();
        if (z10) {
            this.f7379g.top = getScrollY() + height;
            int childCount = getChildCount();
            if (childCount > 0) {
                View childAt = getChildAt(childCount - 1);
                if (this.f7379g.top + height > childAt.getBottom()) {
                    this.f7379g.top = childAt.getBottom() - height;
                }
            }
        } else {
            this.f7379g.top = getScrollY() - height;
            Rect rect = this.f7379g;
            if (rect.top < 0) {
                rect.top = 0;
            }
        }
        Rect rect2 = this.f7379g;
        int i11 = rect2.top;
        int i12 = height + i11;
        rect2.bottom = i12;
        return u(i10, i11, i12);
    }

    @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.ViewParent
    public void requestChildFocus(View view, View view2) {
        if (view2 != null && view2.getRevealOnFocusHint()) {
            if (!this.f7383k) {
                scrollToDescendant(view2);
            } else {
                this.f7384l = view2;
            }
        }
        super.requestChildFocus(view, view2);
    }

    @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.ViewParent
    public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z10) {
        rect.offset(view.getLeft() - view.getScrollX(), view.getTop() - view.getScrollY());
        return v(rect, z10);
    }

    @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean z10) {
        if (z10) {
            t();
        }
        super.requestDisallowInterceptTouchEvent(z10);
    }

    @Override // android.widget.ScrollView, android.view.View, android.view.ViewParent
    public void requestLayout() {
        this.f7383k = true;
        super.requestLayout();
    }

    @Override // android.widget.ScrollView, android.view.View
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

    @Override // android.widget.ScrollView
    public void scrollToDescendant(View view) {
        if (!this.f7383k) {
            view.getDrawingRect(this.f7379g);
            offsetDescendantRectToMyCoords(view, this.f7379g);
            int computeScrollDeltaToGetChildRectOnScreen = computeScrollDeltaToGetChildRectOnScreen(this.f7379g);
            if (computeScrollDeltaToGetChildRectOnScreen != 0) {
                scrollBy(0, computeScrollDeltaToGetChildRectOnScreen);
                return;
            }
            return;
        }
        this.f7384l = view;
    }

    public void setEnableFlingSpeedIncrease(boolean z10) {
        SpringOverScroller springOverScroller = this.f7381i;
        if (springOverScroller != null) {
            springOverScroller.B(z10);
        }
    }

    @Override // android.widget.ScrollView
    public void setFillViewport(boolean z10) {
        if (z10 != this.f7387o) {
            this.f7387o = z10;
            requestLayout();
        }
    }

    public void setIsUseOptimizedScroll(boolean z10) {
        this.L = z10;
    }

    public void setItemClickableWhileOverScrolling(boolean z10) {
        this.I = z10;
    }

    public void setItemClickableWhileSlowScrolling(boolean z10) {
        this.H = z10;
    }

    @Override // android.widget.ScrollView
    public void setSmoothScrollingEnabled(boolean z10) {
        this.f7388p = z10;
    }

    public void setSpringOverScrollerDebug(boolean z10) {
        this.f7381i.A(z10);
    }

    public final void x(int i10, int i11) {
        if (getChildCount() == 0) {
            return;
        }
        if (AnimationUtils.currentAnimationTimeMillis() - this.f7378f > 250) {
            int max = Math.max(0, getChildAt(0).getHeight() - ((getHeight() - getPaddingBottom()) - getPaddingTop()));
            int scrollY = getScrollY();
            this.f7380h.startScroll(getScrollX(), scrollY, 0, Math.max(0, Math.min(i11 + scrollY, max)) - scrollY);
            postInvalidateOnAnimation();
        } else {
            if (!this.f7380h.j()) {
                this.f7380h.abortAnimation();
                if (this.B) {
                    this.B = false;
                }
            }
            scrollBy(i10, i11);
        }
        this.f7378f = AnimationUtils.currentAnimationTimeMillis();
    }

    public COUIScrollView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIScrollView(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    public COUIScrollView(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f7377e = 0;
        this.f7379g = new Rect();
        this.f7380h = null;
        this.f7381i = null;
        this.f7383k = true;
        this.f7384l = null;
        this.f7385m = false;
        this.f7388p = true;
        this.f7395w = -1;
        this.f7396x = new int[2];
        this.f7397y = new int[2];
        this.A = false;
        this.B = false;
        this.H = true;
        this.I = true;
        this.K = new Paint();
        this.L = true;
        this.M = true;
        j(context);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIScrollView, i10, 0);
        this.M = obtainStyledAttributes.getBoolean(R$styleable.COUIScrollView_couiScrollViewEnableVibrator, true);
        obtainStyledAttributes.recycle();
    }
}
