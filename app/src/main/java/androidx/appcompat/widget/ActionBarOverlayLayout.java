package androidx.appcompat.widget;

import android.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowInsets;
import android.widget.OverScroller;
import androidx.appcompat.R$attr;
import androidx.appcompat.R$id;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.core.graphics.Insets;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

@SuppressLint({"UnknownNullness"})
/* loaded from: classes.dex */
public class ActionBarOverlayLayout extends ViewGroup implements DecorContentParent, NestedScrollingParent2, NestedScrollingParent3 {
    static final int[] J = {R$attr.actionBarSize, R.attr.windowContentOverlay};
    private WindowInsetsCompat A;
    private WindowInsetsCompat B;
    private d C;
    private OverScroller D;
    ViewPropertyAnimator E;
    final AnimatorListenerAdapter F;
    private final Runnable G;
    private final Runnable H;
    private final NestedScrollingParentHelper I;

    /* renamed from: e, reason: collision with root package name */
    private int f842e;

    /* renamed from: f, reason: collision with root package name */
    private int f843f;

    /* renamed from: g, reason: collision with root package name */
    private ContentFrameLayout f844g;

    /* renamed from: h, reason: collision with root package name */
    ActionBarContainer f845h;

    /* renamed from: i, reason: collision with root package name */
    private DecorToolbar f846i;

    /* renamed from: j, reason: collision with root package name */
    private Drawable f847j;

    /* renamed from: k, reason: collision with root package name */
    private boolean f848k;

    /* renamed from: l, reason: collision with root package name */
    private boolean f849l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f850m;

    /* renamed from: n, reason: collision with root package name */
    private boolean f851n;

    /* renamed from: o, reason: collision with root package name */
    boolean f852o;

    /* renamed from: p, reason: collision with root package name */
    private int f853p;

    /* renamed from: q, reason: collision with root package name */
    private int f854q;

    /* renamed from: r, reason: collision with root package name */
    private final Rect f855r;

    /* renamed from: s, reason: collision with root package name */
    private final Rect f856s;

    /* renamed from: t, reason: collision with root package name */
    private final Rect f857t;

    /* renamed from: u, reason: collision with root package name */
    private final Rect f858u;

    /* renamed from: v, reason: collision with root package name */
    private final Rect f859v;

    /* renamed from: w, reason: collision with root package name */
    private final Rect f860w;

    /* renamed from: x, reason: collision with root package name */
    private final Rect f861x;

    /* renamed from: y, reason: collision with root package name */
    private WindowInsetsCompat f862y;

    /* renamed from: z, reason: collision with root package name */
    private WindowInsetsCompat f863z;

    /* loaded from: classes.dex */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(int i10, int i11) {
            super(i10, i11);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }
    }

    /* loaded from: classes.dex */
    class a extends AnimatorListenerAdapter {
        a() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            ActionBarOverlayLayout actionBarOverlayLayout = ActionBarOverlayLayout.this;
            actionBarOverlayLayout.E = null;
            actionBarOverlayLayout.f852o = false;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ActionBarOverlayLayout actionBarOverlayLayout = ActionBarOverlayLayout.this;
            actionBarOverlayLayout.E = null;
            actionBarOverlayLayout.f852o = false;
        }
    }

    /* loaded from: classes.dex */
    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ActionBarOverlayLayout.this.u();
            ActionBarOverlayLayout actionBarOverlayLayout = ActionBarOverlayLayout.this;
            actionBarOverlayLayout.E = actionBarOverlayLayout.f845h.animate().translationY(0.0f).setListener(ActionBarOverlayLayout.this.F);
        }
    }

    /* loaded from: classes.dex */
    class c implements Runnable {
        c() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ActionBarOverlayLayout.this.u();
            ActionBarOverlayLayout actionBarOverlayLayout = ActionBarOverlayLayout.this;
            actionBarOverlayLayout.E = actionBarOverlayLayout.f845h.animate().translationY(-ActionBarOverlayLayout.this.f845h.getHeight()).setListener(ActionBarOverlayLayout.this.F);
        }
    }

    /* loaded from: classes.dex */
    public interface d {
        void a();

        void b();

        void c(boolean z10);

        void d();

        void e();

        void f(int i10);
    }

    public ActionBarOverlayLayout(Context context) {
        this(context, null);
    }

    private void A() {
        u();
        this.G.run();
    }

    private boolean B(float f10) {
        this.D.fling(0, 0, 0, (int) f10, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return this.D.getFinalY() > this.f845h.getHeight();
    }

    private void p() {
        u();
        this.H.run();
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0021  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x002c  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0016  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean q(View view, Rect rect, boolean z10, boolean z11, boolean z12, boolean z13) {
        boolean z14;
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        if (z10) {
            int i10 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin;
            int i11 = rect.left;
            if (i10 != i11) {
                ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin = i11;
                z14 = true;
                if (z11) {
                    int i12 = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
                    int i13 = rect.top;
                    if (i12 != i13) {
                        ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = i13;
                        z14 = true;
                    }
                }
                if (z13) {
                    int i14 = ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
                    int i15 = rect.right;
                    if (i14 != i15) {
                        ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin = i15;
                        z14 = true;
                    }
                }
                if (z12) {
                    int i16 = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
                    int i17 = rect.bottom;
                    if (i16 != i17) {
                        ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = i17;
                        return true;
                    }
                }
                return z14;
            }
        }
        z14 = false;
        if (z11) {
        }
        if (z13) {
        }
        if (z12) {
        }
        return z14;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private DecorToolbar t(View view) {
        if (view instanceof DecorToolbar) {
            return (DecorToolbar) view;
        }
        if (view instanceof Toolbar) {
            return ((Toolbar) view).getWrapper();
        }
        throw new IllegalStateException("Can't make a decor toolbar out of " + view.getClass().getSimpleName());
    }

    private void v(Context context) {
        TypedArray obtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(J);
        this.f842e = obtainStyledAttributes.getDimensionPixelSize(0, 0);
        Drawable drawable = obtainStyledAttributes.getDrawable(1);
        this.f847j = drawable;
        setWillNotDraw(drawable == null);
        obtainStyledAttributes.recycle();
        this.f848k = context.getApplicationInfo().targetSdkVersion < 19;
        this.D = new OverScroller(context);
    }

    private void x() {
        u();
        postDelayed(this.H, 600L);
    }

    private void y() {
        u();
        postDelayed(this.G, 600L);
    }

    @Override // androidx.appcompat.widget.DecorContentParent
    public void a(Menu menu, MenuPresenter.a aVar) {
        z();
        this.f846i.a(menu, aVar);
    }

    @Override // androidx.appcompat.widget.DecorContentParent
    public boolean b() {
        z();
        return this.f846i.b();
    }

    @Override // androidx.appcompat.widget.DecorContentParent
    public void c() {
        z();
        this.f846i.c();
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    @Override // androidx.appcompat.widget.DecorContentParent
    public boolean d() {
        z();
        return this.f846i.d();
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.f847j == null || this.f848k) {
            return;
        }
        int bottom = this.f845h.getVisibility() == 0 ? (int) (this.f845h.getBottom() + this.f845h.getTranslationY() + 0.5f) : 0;
        this.f847j.setBounds(0, bottom, getWidth(), this.f847j.getIntrinsicHeight() + bottom);
        this.f847j.draw(canvas);
    }

    @Override // androidx.appcompat.widget.DecorContentParent
    public boolean e() {
        z();
        return this.f846i.e();
    }

    @Override // androidx.appcompat.widget.DecorContentParent
    public boolean f() {
        z();
        return this.f846i.f();
    }

    @Override // android.view.View
    protected boolean fitSystemWindows(Rect rect) {
        return super.fitSystemWindows(rect);
    }

    @Override // androidx.appcompat.widget.DecorContentParent
    public boolean g() {
        z();
        return this.f846i.g();
    }

    public int getActionBarHideOffset() {
        ActionBarContainer actionBarContainer = this.f845h;
        if (actionBarContainer != null) {
            return -((int) actionBarContainer.getTranslationY());
        }
        return 0;
    }

    @Override // android.view.ViewGroup
    public int getNestedScrollAxes() {
        return this.I.a();
    }

    public CharSequence getTitle() {
        z();
        return this.f846i.getTitle();
    }

    @Override // androidx.appcompat.widget.DecorContentParent
    public void h(int i10) {
        z();
        if (i10 == 2) {
            this.f846i.t();
        } else if (i10 == 5) {
            this.f846i.u();
        } else {
            if (i10 != 109) {
                return;
            }
            setOverlayMode(true);
        }
    }

    @Override // androidx.appcompat.widget.DecorContentParent
    public void i() {
        z();
        this.f846i.h();
    }

    @Override // androidx.core.view.NestedScrollingParent3
    public void j(View view, int i10, int i11, int i12, int i13, int i14, int[] iArr) {
        k(view, i10, i11, i12, i13, i14);
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void k(View view, int i10, int i11, int i12, int i13, int i14) {
        if (i14 == 0) {
            onNestedScroll(view, i10, i11, i12, i13);
        }
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public boolean l(View view, View view2, int i10, int i11) {
        return i11 == 0 && onStartNestedScroll(view, view2, i10);
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void m(View view, View view2, int i10, int i11) {
        if (i11 == 0) {
            onNestedScrollAccepted(view, view2, i10);
        }
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void n(View view, int i10) {
        if (i10 == 0) {
            onStopNestedScroll(view);
        }
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void o(View view, int i10, int i11, int[] iArr, int i12) {
        if (i12 == 0) {
            onNestedPreScroll(view, i10, i11, iArr);
        }
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        z();
        WindowInsetsCompat w10 = WindowInsetsCompat.w(windowInsets, this);
        boolean q10 = q(this.f845h, new Rect(w10.j(), w10.l(), w10.k(), w10.i()), true, true, false, true);
        ViewCompat.e(this, w10, this.f855r);
        Rect rect = this.f855r;
        WindowInsetsCompat n10 = w10.n(rect.left, rect.top, rect.right, rect.bottom);
        this.f862y = n10;
        boolean z10 = true;
        if (!this.f863z.equals(n10)) {
            this.f863z = this.f862y;
            q10 = true;
        }
        if (this.f856s.equals(this.f855r)) {
            z10 = q10;
        } else {
            this.f856s.set(this.f855r);
        }
        if (z10) {
            requestLayout();
        }
        return w10.a().c().b().u();
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        v(getContext());
        ViewCompat.h0(this);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        u();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        int childCount = getChildCount();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        for (int i14 = 0; i14 < childCount; i14++) {
            View childAt = getChildAt(i14);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                int i15 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + paddingLeft;
                int i16 = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + paddingTop;
                childAt.layout(i15, i16, measuredWidth + i15, measuredHeight + i16);
            }
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i10, int i11) {
        int measuredHeight;
        z();
        measureChildWithMargins(this.f845h, i10, 0, i11, 0);
        LayoutParams layoutParams = (LayoutParams) this.f845h.getLayoutParams();
        int max = Math.max(0, this.f845h.getMeasuredWidth() + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin);
        int max2 = Math.max(0, this.f845h.getMeasuredHeight() + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin);
        int combineMeasuredStates = View.combineMeasuredStates(0, this.f845h.getMeasuredState());
        boolean z10 = (ViewCompat.I(this) & 256) != 0;
        if (z10) {
            measuredHeight = this.f842e;
            if (this.f850m && this.f845h.getTabContainer() != null) {
                measuredHeight += this.f842e;
            }
        } else {
            measuredHeight = this.f845h.getVisibility() != 8 ? this.f845h.getMeasuredHeight() : 0;
        }
        this.f857t.set(this.f855r);
        WindowInsetsCompat windowInsetsCompat = this.f862y;
        this.A = windowInsetsCompat;
        if (!this.f849l && !z10) {
            Rect rect = this.f857t;
            rect.top += measuredHeight;
            rect.bottom += 0;
            this.A = windowInsetsCompat.n(0, measuredHeight, 0, 0);
        } else {
            this.A = new WindowInsetsCompat.b(this.A).c(Insets.b(windowInsetsCompat.j(), this.A.l() + measuredHeight, this.A.k(), this.A.i() + 0)).a();
        }
        q(this.f844g, this.f857t, true, true, true, true);
        if (!this.B.equals(this.A)) {
            WindowInsetsCompat windowInsetsCompat2 = this.A;
            this.B = windowInsetsCompat2;
            ViewCompat.f(this.f844g, windowInsetsCompat2);
        }
        measureChildWithMargins(this.f844g, i10, 0, i11, 0);
        LayoutParams layoutParams2 = (LayoutParams) this.f844g.getLayoutParams();
        int max3 = Math.max(max, this.f844g.getMeasuredWidth() + ((ViewGroup.MarginLayoutParams) layoutParams2).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams2).rightMargin);
        int max4 = Math.max(max2, this.f844g.getMeasuredHeight() + ((ViewGroup.MarginLayoutParams) layoutParams2).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams2).bottomMargin);
        int combineMeasuredStates2 = View.combineMeasuredStates(combineMeasuredStates, this.f844g.getMeasuredState());
        setMeasuredDimension(View.resolveSizeAndState(Math.max(max3 + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), i10, combineMeasuredStates2), View.resolveSizeAndState(Math.max(max4 + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), i11, combineMeasuredStates2 << 16));
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean onNestedFling(View view, float f10, float f11, boolean z10) {
        if (!this.f851n || !z10) {
            return false;
        }
        if (B(f11)) {
            p();
        } else {
            A();
        }
        this.f852o = true;
        return true;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean onNestedPreFling(View view, float f10, float f11) {
        return false;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void onNestedPreScroll(View view, int i10, int i11, int[] iArr) {
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void onNestedScroll(View view, int i10, int i11, int i12, int i13) {
        int i14 = this.f853p + i11;
        this.f853p = i14;
        setActionBarHideOffset(i14);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void onNestedScrollAccepted(View view, View view2, int i10) {
        this.I.b(view, view2, i10);
        this.f853p = getActionBarHideOffset();
        u();
        d dVar = this.C;
        if (dVar != null) {
            dVar.e();
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean onStartNestedScroll(View view, View view2, int i10) {
        if ((i10 & 2) == 0 || this.f845h.getVisibility() != 0) {
            return false;
        }
        return this.f851n;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void onStopNestedScroll(View view) {
        if (this.f851n && !this.f852o) {
            if (this.f853p <= this.f845h.getHeight()) {
                y();
            } else {
                x();
            }
        }
        d dVar = this.C;
        if (dVar != null) {
            dVar.b();
        }
    }

    @Override // android.view.View
    @Deprecated
    public void onWindowSystemUiVisibilityChanged(int i10) {
        super.onWindowSystemUiVisibilityChanged(i10);
        z();
        int i11 = this.f854q ^ i10;
        this.f854q = i10;
        boolean z10 = (i10 & 4) == 0;
        boolean z11 = (i10 & 256) != 0;
        d dVar = this.C;
        if (dVar != null) {
            dVar.c(!z11);
            if (!z10 && z11) {
                this.C.d();
            } else {
                this.C.a();
            }
        }
        if ((i11 & 256) == 0 || this.C == null) {
            return;
        }
        ViewCompat.h0(this);
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int i10) {
        super.onWindowVisibilityChanged(i10);
        this.f843f = i10;
        d dVar = this.C;
        if (dVar != null) {
            dVar.f(i10);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    /* renamed from: r, reason: merged with bridge method [inline-methods] */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    @Override // android.view.ViewGroup
    /* renamed from: s, reason: merged with bridge method [inline-methods] */
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    public void setActionBarHideOffset(int i10) {
        u();
        this.f845h.setTranslationY(-Math.max(0, Math.min(i10, this.f845h.getHeight())));
    }

    public void setActionBarVisibilityCallback(d dVar) {
        this.C = dVar;
        if (getWindowToken() != null) {
            this.C.f(this.f843f);
            int i10 = this.f854q;
            if (i10 != 0) {
                onWindowSystemUiVisibilityChanged(i10);
                ViewCompat.h0(this);
            }
        }
    }

    public void setHasNonEmbeddedTabs(boolean z10) {
        this.f850m = z10;
    }

    public void setHideOnContentScrollEnabled(boolean z10) {
        if (z10 != this.f851n) {
            this.f851n = z10;
            if (z10) {
                return;
            }
            u();
            setActionBarHideOffset(0);
        }
    }

    public void setIcon(int i10) {
        z();
        this.f846i.setIcon(i10);
    }

    public void setLogo(int i10) {
        z();
        this.f846i.m(i10);
    }

    public void setOverlayMode(boolean z10) {
        this.f849l = z10;
        this.f848k = z10 && getContext().getApplicationInfo().targetSdkVersion < 19;
    }

    public void setShowingForActionMode(boolean z10) {
    }

    public void setUiOptions(int i10) {
    }

    @Override // androidx.appcompat.widget.DecorContentParent
    public void setWindowCallback(Window.Callback callback) {
        z();
        this.f846i.setWindowCallback(callback);
    }

    @Override // androidx.appcompat.widget.DecorContentParent
    public void setWindowTitle(CharSequence charSequence) {
        z();
        this.f846i.setWindowTitle(charSequence);
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    void u() {
        removeCallbacks(this.G);
        removeCallbacks(this.H);
        ViewPropertyAnimator viewPropertyAnimator = this.E;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
        }
    }

    public boolean w() {
        return this.f849l;
    }

    void z() {
        if (this.f844g == null) {
            this.f844g = (ContentFrameLayout) findViewById(R$id.action_bar_activity_content);
            this.f845h = (ActionBarContainer) findViewById(R$id.action_bar_container);
            this.f846i = t(findViewById(R$id.action_bar));
        }
    }

    public ActionBarOverlayLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f843f = 0;
        this.f855r = new Rect();
        this.f856s = new Rect();
        this.f857t = new Rect();
        this.f858u = new Rect();
        this.f859v = new Rect();
        this.f860w = new Rect();
        this.f861x = new Rect();
        WindowInsetsCompat windowInsetsCompat = WindowInsetsCompat.f2378b;
        this.f862y = windowInsetsCompat;
        this.f863z = windowInsetsCompat;
        this.A = windowInsetsCompat;
        this.B = windowInsetsCompat;
        this.F = new a();
        this.G = new b();
        this.H = new c();
        v(context);
        this.I = new NestedScrollingParentHelper(this);
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    public void setIcon(Drawable drawable) {
        z();
        this.f846i.setIcon(drawable);
    }
}
