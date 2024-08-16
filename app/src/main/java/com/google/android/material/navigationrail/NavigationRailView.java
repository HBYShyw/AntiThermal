package com.google.android.material.navigationrail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.R$attr;
import com.google.android.material.R$dimen;
import com.google.android.material.R$style;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.navigation.NavigationBarView;

/* loaded from: classes.dex */
public class NavigationRailView extends NavigationBarView {

    /* renamed from: l, reason: collision with root package name */
    private final int f9029l;

    /* renamed from: m, reason: collision with root package name */
    private View f9030m;

    /* renamed from: n, reason: collision with root package name */
    private Boolean f9031n;

    /* renamed from: o, reason: collision with root package name */
    private Boolean f9032o;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements ViewUtils.OnApplyWindowInsetsListener {
        a() {
        }

        @Override // com.google.android.material.internal.ViewUtils.OnApplyWindowInsetsListener
        public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat, ViewUtils.RelativePadding relativePadding) {
            NavigationRailView navigationRailView = NavigationRailView.this;
            if (navigationRailView.p(navigationRailView.f9031n)) {
                relativePadding.top += windowInsetsCompat.f(WindowInsetsCompat.l.c()).f2186b;
            }
            NavigationRailView navigationRailView2 = NavigationRailView.this;
            if (navigationRailView2.p(navigationRailView2.f9032o)) {
                relativePadding.bottom += windowInsetsCompat.f(WindowInsetsCompat.l.c()).f2188d;
            }
            boolean z10 = ViewCompat.x(view) == 1;
            int j10 = windowInsetsCompat.j();
            int k10 = windowInsetsCompat.k();
            int i10 = relativePadding.start;
            if (z10) {
                j10 = k10;
            }
            relativePadding.start = i10 + j10;
            relativePadding.applyToView(view);
            return windowInsetsCompat;
        }
    }

    public NavigationRailView(Context context) {
        this(context, null);
    }

    private NavigationRailMenuView getNavigationRailMenuView() {
        return (NavigationRailMenuView) getMenuView();
    }

    private void k() {
        ViewUtils.doOnApplyWindowInsets(this, new a());
    }

    private boolean m() {
        View view = this.f9030m;
        return (view == null || view.getVisibility() == 8) ? false : true;
    }

    private int n(int i10) {
        int suggestedMinimumWidth = getSuggestedMinimumWidth();
        if (View.MeasureSpec.getMode(i10) == 1073741824 || suggestedMinimumWidth <= 0) {
            return i10;
        }
        return View.MeasureSpec.makeMeasureSpec(Math.min(View.MeasureSpec.getSize(i10), suggestedMinimumWidth + getPaddingLeft() + getPaddingRight()), 1073741824);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean p(Boolean bool) {
        return bool != null ? bool.booleanValue() : ViewCompat.u(this);
    }

    public View getHeaderView() {
        return this.f9030m;
    }

    public int getItemMinimumHeight() {
        return ((NavigationRailMenuView) getMenuView()).getItemMinimumHeight();
    }

    @Override // com.google.android.material.navigation.NavigationBarView
    public int getMaxItemCount() {
        return 7;
    }

    public int getMenuGravity() {
        return getNavigationRailMenuView().getMenuGravity();
    }

    public void i(int i10) {
        j(LayoutInflater.from(getContext()).inflate(i10, (ViewGroup) this, false));
    }

    public void j(View view) {
        o();
        this.f9030m = view;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 49;
        layoutParams.topMargin = this.f9029l;
        addView(view, 0, layoutParams);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.material.navigation.NavigationBarView
    /* renamed from: l, reason: merged with bridge method [inline-methods] */
    public NavigationRailMenuView d(Context context) {
        return new NavigationRailMenuView(context);
    }

    public void o() {
        View view = this.f9030m;
        if (view != null) {
            removeView(view);
            this.f9030m = null;
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        super.onLayout(z10, i10, i11, i12, i13);
        NavigationRailMenuView navigationRailMenuView = getNavigationRailMenuView();
        int i14 = 0;
        if (m()) {
            int bottom = this.f9030m.getBottom() + this.f9029l;
            int top = navigationRailMenuView.getTop();
            if (top < bottom) {
                i14 = bottom - top;
            }
        } else if (navigationRailMenuView.o()) {
            i14 = this.f9029l;
        }
        if (i14 > 0) {
            navigationRailMenuView.layout(navigationRailMenuView.getLeft(), navigationRailMenuView.getTop() + i14, navigationRailMenuView.getRight(), navigationRailMenuView.getBottom() + i14);
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        int n10 = n(i10);
        super.onMeasure(n10, i11);
        if (m()) {
            measureChild(getNavigationRailMenuView(), n10, View.MeasureSpec.makeMeasureSpec((getMeasuredHeight() - this.f9030m.getMeasuredHeight()) - this.f9029l, Integer.MIN_VALUE));
        }
    }

    public void setItemMinimumHeight(int i10) {
        ((NavigationRailMenuView) getMenuView()).setItemMinimumHeight(i10);
    }

    public void setMenuGravity(int i10) {
        getNavigationRailMenuView().setMenuGravity(i10);
    }

    public NavigationRailView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.navigationRailStyle);
    }

    public NavigationRailView(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, R$style.Widget_MaterialComponents_NavigationRailView);
    }

    public NavigationRailView(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f9031n = null;
        this.f9032o = null;
        this.f9029l = getResources().getDimensionPixelSize(R$dimen.mtrl_navigation_rail_margin);
        TintTypedArray obtainTintedStyledAttributes = ThemeEnforcement.obtainTintedStyledAttributes(getContext(), attributeSet, R$styleable.NavigationRailView, i10, i11, new int[0]);
        int n10 = obtainTintedStyledAttributes.n(R$styleable.NavigationRailView_headerLayout, 0);
        if (n10 != 0) {
            i(n10);
        }
        setMenuGravity(obtainTintedStyledAttributes.k(R$styleable.NavigationRailView_menuGravity, 49));
        int i12 = R$styleable.NavigationRailView_itemMinHeight;
        if (obtainTintedStyledAttributes.s(i12)) {
            setItemMinimumHeight(obtainTintedStyledAttributes.f(i12, -1));
        }
        int i13 = R$styleable.NavigationRailView_paddingTopSystemWindowInsets;
        if (obtainTintedStyledAttributes.s(i13)) {
            this.f9031n = Boolean.valueOf(obtainTintedStyledAttributes.a(i13, false));
        }
        int i14 = R$styleable.NavigationRailView_paddingBottomSystemWindowInsets;
        if (obtainTintedStyledAttributes.s(i14)) {
            this.f9032o = Boolean.valueOf(obtainTintedStyledAttributes.a(i14, false));
        }
        obtainTintedStyledAttributes.x();
        k();
    }
}
