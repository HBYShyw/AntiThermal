package com.coui.appcompat.scrollview;

import android.content.Context;
import android.content.res.TypedArray;
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
import android.view.ViewParent;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import com.support.appcompat.R$styleable;
import java.util.ArrayList;
import l3.ViewNative;
import m1.COUIPhysicalAnimationUtil;
import o2.COUIIOverScroller;
import o2.SpringOverScroller;

/* loaded from: classes.dex */
public class COUIHorizontalScrollView extends HorizontalScrollView {
    private boolean A;
    private boolean B;
    private boolean C;
    private float D;
    private boolean E;
    private boolean F;
    private boolean G;
    private boolean H;

    /* renamed from: e, reason: collision with root package name */
    private int f7332e;

    /* renamed from: f, reason: collision with root package name */
    private long f7333f;

    /* renamed from: g, reason: collision with root package name */
    private final Rect f7334g;

    /* renamed from: h, reason: collision with root package name */
    private COUIIOverScroller f7335h;

    /* renamed from: i, reason: collision with root package name */
    private SpringOverScroller f7336i;

    /* renamed from: j, reason: collision with root package name */
    private int f7337j;

    /* renamed from: k, reason: collision with root package name */
    private boolean f7338k;

    /* renamed from: l, reason: collision with root package name */
    private View f7339l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f7340m;

    /* renamed from: n, reason: collision with root package name */
    private VelocityTracker f7341n;

    /* renamed from: o, reason: collision with root package name */
    private boolean f7342o;

    /* renamed from: p, reason: collision with root package name */
    private boolean f7343p;

    /* renamed from: q, reason: collision with root package name */
    private int f7344q;

    /* renamed from: r, reason: collision with root package name */
    private int f7345r;

    /* renamed from: s, reason: collision with root package name */
    private int f7346s;

    /* renamed from: t, reason: collision with root package name */
    private int f7347t;

    /* renamed from: u, reason: collision with root package name */
    private int f7348u;

    /* renamed from: v, reason: collision with root package name */
    private float f7349v;

    /* renamed from: w, reason: collision with root package name */
    private int f7350w;

    /* renamed from: x, reason: collision with root package name */
    private long f7351x;

    /* renamed from: y, reason: collision with root package name */
    private int f7352y;

    /* renamed from: z, reason: collision with root package name */
    private boolean f7353z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class COUISavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<COUISavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        public int f7354e;

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

        public COUISavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.f7354e = parcel.readInt();
        }

        public String toString() {
            return "HorizontalScrollView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " scrollPosition=" + this.f7354e + "}";
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeInt(this.f7354e);
        }
    }

    public COUIHorizontalScrollView(Context context) {
        this(context, null);
    }

    private boolean a() {
        View childAt = getChildAt(0);
        if (childAt != null) {
            return getWidth() < (childAt.getWidth() + getPaddingLeft()) + getPaddingRight();
        }
        return false;
    }

    private boolean b(View view, MotionEvent motionEvent) {
        int[] iArr = {0, 1};
        boolean z10 = true;
        for (int i10 = 0; i10 < 2; i10++) {
            motionEvent.setAction(iArr[i10]);
            z10 &= view.dispatchTouchEvent(motionEvent);
        }
        return z10;
    }

    private void c(int i10) {
        if (i10 != 0) {
            if (this.f7343p) {
                u(i10, 0);
            } else {
                scrollBy(i10, 0);
            }
        }
    }

    private View d(boolean z10, int i10, int i11) {
        ArrayList focusables = getFocusables(2);
        int size = focusables.size();
        View view = null;
        boolean z11 = false;
        for (int i12 = 0; i12 < size; i12++) {
            View view2 = (View) focusables.get(i12);
            int left = view2.getLeft();
            int right = view2.getRight();
            if (i10 < right && left < i11) {
                boolean z12 = i10 < left && right < i11;
                if (view == null) {
                    view = view2;
                    z11 = z12;
                } else {
                    boolean z13 = (z10 && left < view.getLeft()) || (!z10 && right > view.getRight());
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

    private View e(boolean z10, int i10, View view) {
        int horizontalFadingEdgeLength = getHorizontalFadingEdgeLength() / 2;
        int i11 = i10 + horizontalFadingEdgeLength;
        int width = (i10 + getWidth()) - horizontalFadingEdgeLength;
        return (view == null || view.getLeft() >= width || view.getRight() <= i11) ? d(z10, i11, width) : view;
    }

    private View f(MotionEvent motionEvent) {
        View view = null;
        if (!k(motionEvent)) {
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
                if (contains && b(childAt, obtain)) {
                    view = childAt;
                }
            }
        }
        return view;
    }

    private boolean g(int i10, int i11) {
        if (getChildCount() <= 0) {
            return false;
        }
        int scrollX = getScrollX();
        View childAt = getChildAt(0);
        return i11 >= childAt.getTop() && i11 < childAt.getBottom() && i10 >= childAt.getLeft() - scrollX && i10 < childAt.getRight() - scrollX;
    }

    private int getScrollRange() {
        if (getChildCount() > 0) {
            return Math.max(0, getChildAt(0).getWidth() - ((getWidth() - getPaddingLeft()) - getPaddingRight()));
        }
        return 0;
    }

    private void h(Context context) {
        if (this.f7335h == null) {
            SpringOverScroller springOverScroller = new SpringOverScroller(context);
            this.f7336i = springOverScroller;
            springOverScroller.F(3.2f);
            this.f7336i.C(true);
            this.f7335h = this.f7336i;
            setEnableFlingSpeedIncrease(true);
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.f7344q = viewConfiguration.getScaledTouchSlop();
        this.f7345r = viewConfiguration.getScaledMinimumFlingVelocity();
        this.f7346s = viewConfiguration.getScaledMaximumFlingVelocity();
        int i10 = displayMetrics.widthPixels;
        this.f7347t = i10;
        this.f7348u = i10;
        this.f7332e = i10;
        this.f7349v = viewConfiguration.getScaledHorizontalScrollFactor();
        setOverScrollMode(0);
    }

    private void i() {
        VelocityTracker velocityTracker = this.f7341n;
        if (velocityTracker == null) {
            this.f7341n = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
    }

    private void j() {
        if (this.f7341n == null) {
            this.f7341n = VelocityTracker.obtain();
        }
    }

    private boolean k(MotionEvent motionEvent) {
        int x10 = (int) (motionEvent.getX() - this.f7352y);
        return System.currentTimeMillis() - this.f7351x < 100 && ((int) Math.sqrt((double) (x10 * x10))) < 10;
    }

    private boolean l(View view) {
        return !n(view, 0);
    }

    private boolean m() {
        return getScrollX() < 0 || getScrollX() > getScrollRange();
    }

    private boolean n(View view, int i10) {
        view.getDrawingRect(this.f7334g);
        offsetDescendantRectToMyCoords(view, this.f7334g);
        return this.f7334g.right + i10 >= getScrollX() && this.f7334g.left - i10 <= getScrollX() + getWidth();
    }

    private void o(MotionEvent motionEvent) {
        int action = (motionEvent.getAction() & 65280) >> 8;
        if (motionEvent.getPointerId(action) == this.f7350w) {
            int i10 = action == 0 ? 1 : 0;
            int x10 = (int) motionEvent.getX(i10);
            this.f7337j = x10;
            this.f7352y = x10;
            this.f7350w = motionEvent.getPointerId(i10);
            VelocityTracker velocityTracker = this.f7341n;
            if (velocityTracker != null) {
                velocityTracker.clear();
            }
        }
    }

    private void p() {
        if (this.H) {
            performHapticFeedback(307);
        }
    }

    private void q() {
        VelocityTracker velocityTracker = this.f7341n;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.f7341n = null;
        }
    }

    private boolean r(int i10, int i11, int i12) {
        int width = getWidth();
        int scrollX = getScrollX();
        int i13 = width + scrollX;
        boolean z10 = false;
        boolean z11 = i10 == 17;
        View d10 = d(z11, i11, i12);
        if (d10 == null) {
            d10 = this;
        }
        if (i11 < scrollX || i12 > i13) {
            c(z11 ? i11 - scrollX : i12 - i13);
            z10 = true;
        }
        if (d10 != findFocus()) {
            d10.requestFocus(i10);
        }
        return z10;
    }

    private void s(View view) {
        view.getDrawingRect(this.f7334g);
        offsetDescendantRectToMyCoords(view, this.f7334g);
        int computeScrollDeltaToGetChildRectOnScreen = computeScrollDeltaToGetChildRectOnScreen(this.f7334g);
        if (computeScrollDeltaToGetChildRectOnScreen != 0) {
            scrollBy(computeScrollDeltaToGetChildRectOnScreen, 0);
        }
    }

    private boolean t(Rect rect, boolean z10) {
        int computeScrollDeltaToGetChildRectOnScreen = computeScrollDeltaToGetChildRectOnScreen(rect);
        boolean z11 = computeScrollDeltaToGetChildRectOnScreen != 0;
        if (z11) {
            if (z10) {
                scrollBy(computeScrollDeltaToGetChildRectOnScreen, 0);
            } else {
                u(computeScrollDeltaToGetChildRectOnScreen, 0);
            }
        }
        return z11;
    }

    @Override // android.widget.HorizontalScrollView
    public boolean arrowScroll(int i10) {
        int right;
        View findFocus = findFocus();
        if (findFocus == this) {
            findFocus = null;
        }
        View findNextFocus = FocusFinder.getInstance().findNextFocus(this, findFocus, i10);
        int maxScrollAmount = getMaxScrollAmount();
        if (findNextFocus != null && n(findNextFocus, maxScrollAmount)) {
            findNextFocus.getDrawingRect(this.f7334g);
            offsetDescendantRectToMyCoords(findNextFocus, this.f7334g);
            c(computeScrollDeltaToGetChildRectOnScreen(this.f7334g));
            findNextFocus.requestFocus(i10);
        } else {
            if (i10 == 17 && getScrollX() < maxScrollAmount) {
                maxScrollAmount = getScrollX();
            } else if (i10 == 66 && getChildCount() > 0 && (right = getChildAt(0).getRight() - (getScrollX() + getWidth())) < maxScrollAmount) {
                maxScrollAmount = right;
            }
            if (maxScrollAmount == 0) {
                return false;
            }
            if (i10 != 66) {
                maxScrollAmount = -maxScrollAmount;
            }
            c(maxScrollAmount);
        }
        if (findFocus == null || !findFocus.isFocused() || !l(findFocus)) {
            return true;
        }
        int descendantFocusability = getDescendantFocusability();
        setDescendantFocusability(131072);
        requestFocus();
        setDescendantFocusability(descendantFocusability);
        return true;
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    public void computeScroll() {
        if (this.f7335h.computeScrollOffset()) {
            int scrollX = getScrollX();
            int scrollY = getScrollY();
            int b10 = this.f7335h.b();
            int g6 = this.f7335h.g();
            if (scrollX != b10 || scrollY != g6) {
                overScrollBy(b10 - scrollX, g6 - scrollY, scrollX, scrollY, getScrollRange(), 0, this.f7348u, 0, false);
                onScrollChanged(getScrollX(), getScrollY(), scrollX, scrollY);
            }
            if (awakenScrollBars()) {
                return;
            }
            postInvalidateOnAnimation();
            return;
        }
        if (this.F) {
            this.F = false;
        }
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent) || executeKeyEvent(keyEvent);
    }

    @Override // android.widget.HorizontalScrollView
    public boolean executeKeyEvent(KeyEvent keyEvent) {
        this.f7334g.setEmpty();
        if (!a()) {
            if (!isFocused()) {
                return false;
            }
            View findFocus = findFocus();
            if (findFocus == this) {
                findFocus = null;
            }
            View findNextFocus = FocusFinder.getInstance().findNextFocus(this, findFocus, 66);
            return (findNextFocus == null || findNextFocus == this || !findNextFocus.requestFocus(66)) ? false : true;
        }
        if (keyEvent.getAction() != 0) {
            return false;
        }
        int keyCode = keyEvent.getKeyCode();
        if (keyCode == 21) {
            if (!keyEvent.isAltPressed()) {
                return arrowScroll(17);
            }
            return fullScroll(17);
        }
        if (keyCode == 22) {
            if (!keyEvent.isAltPressed()) {
                return arrowScroll(66);
            }
            return fullScroll(66);
        }
        if (keyCode != 62) {
            return false;
        }
        pageScroll(keyEvent.isShiftPressed() ? 17 : 66);
        return false;
    }

    @Override // android.widget.HorizontalScrollView
    public void fling(int i10) {
        this.D = i10;
        if (getChildCount() > 0) {
            int width = (getWidth() - getPaddingRight()) - getPaddingLeft();
            this.f7335h.fling(getScrollX(), getScrollY(), i10, 0, 0, Math.max(0, (getChildAt(0).getRight() - getPaddingLeft()) - width), 0, 0, width / 2, 0);
            if (!this.F) {
                this.F = true;
            }
            boolean z10 = i10 > 0;
            View findFocus = findFocus();
            View e10 = e(z10, this.f7335h.f(), findFocus);
            if (e10 == null) {
                e10 = this;
            }
            if (e10 != findFocus) {
                e10.requestFocus(z10 ? 66 : 17);
            }
            postInvalidateOnAnimation();
        }
    }

    @Override // android.widget.HorizontalScrollView
    public boolean fullScroll(int i10) {
        boolean z10 = i10 == 66;
        int width = getWidth();
        Rect rect = this.f7334g;
        rect.left = 0;
        rect.right = width;
        if (z10 && getChildCount() > 0) {
            this.f7334g.right = getChildAt(0).getRight();
            Rect rect2 = this.f7334g;
            rect2.left = rect2.right - width;
        }
        Rect rect3 = this.f7334g;
        return r(i10, rect3.left, rect3.right);
    }

    public int getScrollableRange() {
        return (getWidth() - getPaddingLeft()) - getPaddingRight();
    }

    protected void invalidateParentIfNeeded() {
        if (isHardwareAccelerated() && (getParent() instanceof View)) {
            ((View) getParent()).invalidate();
        }
    }

    @Override // android.widget.HorizontalScrollView
    public boolean isFillViewport() {
        return this.f7342o;
    }

    @Override // android.widget.HorizontalScrollView
    public boolean isSmoothScrollingEnabled() {
        return this.f7343p;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.E) {
            this.E = false;
        }
        if (this.F) {
            this.F = false;
        }
        this.f7336i.u();
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    public boolean onGenericMotionEvent(MotionEvent motionEvent) {
        float axisValue;
        if (motionEvent.getAction() == 8 && !this.f7340m) {
            if (motionEvent.isFromSource(2)) {
                if ((motionEvent.getMetaState() & 1) != 0) {
                    axisValue = -motionEvent.getAxisValue(9);
                } else {
                    axisValue = motionEvent.getAxisValue(10);
                }
            } else {
                axisValue = motionEvent.isFromSource(4194304) ? motionEvent.getAxisValue(26) : 0.0f;
            }
            int round = Math.round(axisValue * this.f7349v);
            if (round != 0) {
                int scrollRange = getScrollRange();
                int scrollX = getScrollX();
                int i10 = round + scrollX;
                if (i10 < 0) {
                    scrollRange = 0;
                } else if (i10 <= scrollRange) {
                    scrollRange = i10;
                }
                if (scrollRange != scrollX) {
                    super.scrollTo(scrollRange, getScrollY());
                    return true;
                }
            }
        }
        return super.onGenericMotionEvent(motionEvent);
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if ((action == 2 && this.f7340m) || super.onInterceptTouchEvent(motionEvent)) {
            return true;
        }
        int i10 = action & 255;
        if (i10 != 0) {
            if (i10 != 1) {
                if (i10 == 2) {
                    int i11 = this.f7350w;
                    if (i11 != -1) {
                        int findPointerIndex = motionEvent.findPointerIndex(i11);
                        if (findPointerIndex == -1) {
                            Log.e("COUIHorizontalScrollView", "Invalid pointerId=" + i11 + " in onInterceptTouchEvent");
                        } else {
                            int x10 = (int) motionEvent.getX(findPointerIndex);
                            if (Math.abs(x10 - this.f7337j) > this.f7344q) {
                                this.f7340m = true;
                                this.f7337j = x10;
                                j();
                                this.f7341n.addMovement(motionEvent);
                                ViewParent parent = getParent();
                                if (parent != null) {
                                    parent.requestDisallowInterceptTouchEvent(true);
                                }
                            }
                        }
                    }
                } else if (i10 != 3) {
                    if (i10 == 5) {
                        int actionIndex = motionEvent.getActionIndex();
                        int x11 = (int) motionEvent.getX(actionIndex);
                        this.f7337j = x11;
                        this.f7352y = x11;
                        this.f7350w = motionEvent.getPointerId(actionIndex);
                    } else if (i10 == 6) {
                        o(motionEvent);
                        int x12 = (int) motionEvent.getX(motionEvent.findPointerIndex(this.f7350w));
                        this.f7337j = x12;
                        this.f7352y = x12;
                    }
                }
            }
            this.f7340m = false;
            this.f7350w = -1;
            if (this.f7335h.springBack(getScrollX(), getScrollY(), 0, getScrollRange(), 0, 0)) {
                postInvalidateOnAnimation();
            }
        } else {
            COUIIOverScroller cOUIIOverScroller = this.f7335h;
            float a10 = cOUIIOverScroller != null ? cOUIIOverScroller.a() : 0.0f;
            this.f7353z = Math.abs(a10) > 0.0f && Math.abs(a10) < 250.0f && ((Math.abs(this.D) > 1500.0f ? 1 : (Math.abs(this.D) == 1500.0f ? 0 : -1)) > 0);
            this.A = m();
            this.f7351x = System.currentTimeMillis();
            int x13 = (int) motionEvent.getX();
            if (!g(x13, (int) motionEvent.getY())) {
                this.f7340m = false;
                q();
            } else {
                this.f7337j = x13;
                this.f7352y = x13;
                this.f7350w = motionEvent.getPointerId(0);
                i();
                this.f7341n.addMovement(motionEvent);
                this.f7340m = !this.f7335h.j();
            }
        }
        return this.f7340m;
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        int paddingLeft;
        int paddingTop;
        int paddingBottom;
        super.onMeasure(i10, i11);
        if (this.f7342o && View.MeasureSpec.getMode(i10) != 0 && getChildCount() > 0) {
            View childAt = getChildAt(0);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
            if (getContext().getApplicationInfo().targetSdkVersion >= 23) {
                paddingLeft = getPaddingLeft() + getPaddingRight() + layoutParams.leftMargin + layoutParams.rightMargin;
                paddingTop = getPaddingTop() + getPaddingBottom() + layoutParams.topMargin;
                paddingBottom = layoutParams.bottomMargin;
            } else {
                paddingLeft = getPaddingLeft() + getPaddingRight();
                paddingTop = getPaddingTop();
                paddingBottom = getPaddingBottom();
            }
            int i12 = paddingTop + paddingBottom;
            int measuredWidth = getMeasuredWidth() - paddingLeft;
            if (childAt.getMeasuredWidth() < measuredWidth) {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(measuredWidth, 1073741824), HorizontalScrollView.getChildMeasureSpec(i11, i12, layoutParams.height));
            }
        }
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    protected void onOverScrolled(int i10, int i11, boolean z10, boolean z11) {
        if (getScrollY() == i11 && getScrollX() == i10) {
            return;
        }
        if (m() && this.F) {
            int scrollRange = i10 >= getScrollRange() ? getScrollRange() : 0;
            i10 = COUIPhysicalAnimationUtil.a(scrollRange, i10 - scrollRange, this.f7332e);
        }
        if (getOverScrollMode() == 2 || (getOverScrollMode() == 1 && getChildAt(0).getWidth() <= getScrollableRange())) {
            i10 = Math.min(Math.max(i10, 0), getScrollRange());
        }
        if (getScrollX() >= 0 && i10 < 0 && this.F) {
            p();
            this.f7336i.notifyHorizontalEdgeReached(i10, 0, this.f7348u);
        }
        if (getScrollX() <= getScrollRange() && i10 > getScrollRange() && this.F) {
            p();
            this.f7336i.notifyHorizontalEdgeReached(i10, getScrollRange(), this.f7348u);
        }
        ViewNative.b(this, i10);
        ViewNative.c(this, i11);
        invalidateParentIfNeeded();
        awakenScrollBars();
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup
    protected boolean onRequestFocusInDescendants(int i10, Rect rect) {
        View findNextFocusFromRect;
        if (i10 == 2) {
            i10 = 66;
        } else if (i10 == 1) {
            i10 = 17;
        }
        if (rect == null) {
            findNextFocusFromRect = FocusFinder.getInstance().findNextFocus(this, null, i10);
        } else {
            findNextFocusFromRect = FocusFinder.getInstance().findNextFocusFromRect(this, rect, i10);
        }
        if (findNextFocusFromRect == null || l(findNextFocusFromRect)) {
            return false;
        }
        return findNextFocusFromRect.requestFocus(i10, rect);
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    protected void onSizeChanged(int i10, int i11, int i12, int i13) {
        super.onSizeChanged(i10, i11, i12, i13);
        int i14 = getContext().getResources().getDisplayMetrics().widthPixels;
        this.f7347t = i14;
        this.f7348u = i14;
        this.f7332e = i14;
        View findFocus = findFocus();
        if (findFocus == null || this == findFocus || !n(findFocus, getRight() - getLeft())) {
            return;
        }
        findFocus.getDrawingRect(this.f7334g);
        offsetDescendantRectToMyCoords(findFocus, this.f7334g);
        c(computeScrollDeltaToGetChildRectOnScreen(this.f7334g));
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        ViewParent parent;
        j();
        this.f7341n.addMovement(motionEvent);
        int action = motionEvent.getAction() & 255;
        if (action != 0) {
            if (action == 1) {
                boolean m10 = m();
                boolean z10 = this.B && this.f7353z;
                boolean z11 = this.C && this.A && m10;
                if (z10 || z11) {
                    f(motionEvent);
                }
                if (this.f7340m) {
                    j();
                    VelocityTracker velocityTracker = this.f7341n;
                    velocityTracker.computeCurrentVelocity(1000, this.f7346s);
                    int xVelocity = (int) velocityTracker.getXVelocity(this.f7350w);
                    if (Math.abs(xVelocity) > this.f7345r) {
                        int i10 = -xVelocity;
                        this.f7335h.d(i10);
                        if (getScrollX() < 0) {
                            if (xVelocity > -1500) {
                                if (this.f7335h.springBack(getScrollX(), getScrollY(), 0, getScrollRange(), 0, 0)) {
                                    postInvalidateOnAnimation();
                                }
                            } else {
                                fling(i10);
                            }
                        } else if (getScrollX() <= getScrollRange()) {
                            fling(i10);
                        } else if (xVelocity < 1500) {
                            if (this.f7335h.springBack(getScrollX(), getScrollY(), 0, getScrollRange(), 0, 0)) {
                                postInvalidateOnAnimation();
                            }
                        } else {
                            fling(i10);
                        }
                    } else if (this.f7335h.springBack(getScrollX(), getScrollY(), 0, getScrollRange(), 0, 0)) {
                        postInvalidateOnAnimation();
                    }
                    if (getScrollX() < 0 || getScrollX() > getScrollRange()) {
                        p();
                    }
                    this.f7350w = -1;
                    this.f7340m = false;
                    q();
                } else if (this.f7335h.springBack(getScrollX(), getScrollY(), 0, getScrollRange(), 0, 0)) {
                    postInvalidateOnAnimation();
                }
            } else if (action == 2) {
                COUIIOverScroller cOUIIOverScroller = this.f7335h;
                if ((cOUIIOverScroller instanceof SpringOverScroller) && this.G) {
                    ((SpringOverScroller) cOUIIOverScroller).G();
                }
                int findPointerIndex = motionEvent.findPointerIndex(this.f7350w);
                if (findPointerIndex == -1) {
                    Log.e("COUIHorizontalScrollView", "Invalid pointerId=" + this.f7350w + " in onTouchEvent");
                } else {
                    int x10 = (int) motionEvent.getX(findPointerIndex);
                    int i11 = this.f7337j - x10;
                    if (!this.f7340m && Math.abs(i11) > this.f7344q) {
                        ViewParent parent2 = getParent();
                        if (parent2 != null) {
                            parent2.requestDisallowInterceptTouchEvent(true);
                        }
                        this.f7340m = true;
                        i11 = i11 > 0 ? i11 - this.f7344q : i11 + this.f7344q;
                    }
                    if (this.f7340m) {
                        this.f7337j = x10;
                        int scrollRange = getScrollRange();
                        if (getScrollX() < 0) {
                            i11 = COUIPhysicalAnimationUtil.b(i11, getScrollX(), this.f7347t);
                        } else if (getScrollX() > getScrollRange()) {
                            i11 = COUIPhysicalAnimationUtil.b(i11, getScrollX() - getScrollRange(), this.f7347t);
                        }
                        if (overScrollBy(i11, 0, getScrollX(), 0, scrollRange, 0, this.f7347t, 0, true) && !hasNestedScrollingParent()) {
                            this.f7341n.clear();
                        }
                    }
                }
            } else if (action != 3) {
                if (action == 6) {
                    o(motionEvent);
                }
            } else if (this.f7340m && getChildCount() > 0) {
                if (this.f7335h.springBack(getScrollX(), getScrollY(), 0, getScrollRange(), 0, 0)) {
                    postInvalidateOnAnimation();
                }
                this.f7350w = -1;
                this.f7340m = false;
                q();
            }
        } else {
            if (getChildCount() == 0) {
                return false;
            }
            if (!this.f7335h.j() && (parent = getParent()) != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
            if (!this.f7335h.j()) {
                this.f7335h.abortAnimation();
                if (this.F) {
                    this.F = false;
                }
            }
            int x11 = (int) motionEvent.getX();
            this.f7337j = x11;
            this.f7352y = x11;
            this.f7350w = motionEvent.getPointerId(0);
        }
        return true;
    }

    @Override // android.view.View
    protected void onVisibilityChanged(View view, int i10) {
        SpringOverScroller springOverScroller;
        super.onVisibilityChanged(view, i10);
        if (i10 == 0 || (springOverScroller = this.f7336i) == null) {
            return;
        }
        springOverScroller.abortAnimation();
        this.f7336i.u();
    }

    @Override // android.view.View
    protected boolean overScrollBy(int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, boolean z10) {
        onOverScrolled(i12 + i10, i13 + i11, false, false);
        return false;
    }

    @Override // android.widget.HorizontalScrollView
    public boolean pageScroll(int i10) {
        boolean z10 = i10 == 66;
        int width = getWidth();
        if (z10) {
            this.f7334g.left = getScrollX() + width;
            if (getChildCount() > 0) {
                View childAt = getChildAt(0);
                if (this.f7334g.left + width > childAt.getRight()) {
                    this.f7334g.left = childAt.getRight() - width;
                }
            }
        } else {
            this.f7334g.left = getScrollX() - width;
            Rect rect = this.f7334g;
            if (rect.left < 0) {
                rect.left = 0;
            }
        }
        Rect rect2 = this.f7334g;
        int i11 = rect2.left;
        int i12 = width + i11;
        rect2.right = i12;
        return r(i10, i11, i12);
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup, android.view.ViewParent
    public void requestChildFocus(View view, View view2) {
        if (view2 != null && view2.getRevealOnFocusHint()) {
            if (!this.f7338k) {
                s(view2);
            } else {
                this.f7339l = view2;
            }
        }
        super.requestChildFocus(view, view2);
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup, android.view.ViewParent
    public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z10) {
        rect.offset(view.getLeft() - view.getScrollX(), view.getTop() - view.getScrollY());
        return t(rect, z10);
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean z10) {
        if (z10) {
            q();
        }
        super.requestDisallowInterceptTouchEvent(z10);
    }

    @Override // android.widget.HorizontalScrollView, android.view.View, android.view.ViewParent
    public void requestLayout() {
        this.f7338k = true;
        super.requestLayout();
    }

    public void setEnableFlingSpeedIncrease(boolean z10) {
        SpringOverScroller springOverScroller = this.f7336i;
        if (springOverScroller != null) {
            springOverScroller.B(z10);
        }
    }

    @Override // android.widget.HorizontalScrollView
    public void setFillViewport(boolean z10) {
        if (z10 != this.f7342o) {
            this.f7342o = z10;
            requestLayout();
        }
    }

    public void setIsUseOptimizedScroll(boolean z10) {
        this.G = z10;
    }

    public void setItemClickableWhileOverScrolling(boolean z10) {
        this.C = z10;
    }

    public void setItemClickableWhileSlowScrolling(boolean z10) {
        this.B = z10;
    }

    @Override // android.widget.HorizontalScrollView
    public void setSmoothScrollingEnabled(boolean z10) {
        this.f7343p = z10;
    }

    public void setSpringOverScrollerDebug(boolean z10) {
        this.f7336i.A(z10);
    }

    public final void u(int i10, int i11) {
        if (getChildCount() == 0) {
            return;
        }
        if (AnimationUtils.currentAnimationTimeMillis() - this.f7333f > 250) {
            int max = Math.max(0, getChildAt(0).getWidth() - ((getWidth() - getPaddingRight()) - getPaddingLeft()));
            int scrollX = getScrollX();
            this.f7335h.startScroll(scrollX, getScrollY(), Math.max(0, Math.min(i10 + scrollX, max)) - scrollX, 0);
            postInvalidateOnAnimation();
        } else {
            if (!this.f7335h.j()) {
                this.f7335h.abortAnimation();
                if (this.F) {
                    this.F = false;
                }
            }
            scrollBy(i10, i11);
        }
        this.f7333f = AnimationUtils.currentAnimationTimeMillis();
    }

    public COUIHorizontalScrollView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIHorizontalScrollView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f7332e = 0;
        this.f7334g = new Rect();
        this.f7335h = null;
        this.f7336i = null;
        this.f7338k = true;
        this.f7339l = null;
        this.f7340m = false;
        this.f7343p = true;
        this.f7350w = -1;
        this.B = true;
        this.C = true;
        this.E = false;
        this.F = false;
        this.G = true;
        this.H = true;
        h(context);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIScrollView, i10, 0);
        this.H = obtainStyledAttributes.getBoolean(R$styleable.COUIScrollView_couiScrollViewEnableVibrator, true);
        obtainStyledAttributes.recycle();
    }

    public COUIHorizontalScrollView(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f7332e = 0;
        this.f7334g = new Rect();
        this.f7335h = null;
        this.f7336i = null;
        this.f7338k = true;
        this.f7339l = null;
        this.f7340m = false;
        this.f7343p = true;
        this.f7350w = -1;
        this.B = true;
        this.C = true;
        this.E = false;
        this.F = false;
        this.G = true;
        this.H = true;
        h(context);
    }
}
