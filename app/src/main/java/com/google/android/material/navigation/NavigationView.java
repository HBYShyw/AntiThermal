package com.google.android.material.navigation;

import a4.RippleUtils;
import android.R;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import androidx.appcompat.R$attr;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.customview.view.AbsSavedState;
import androidx.drawerlayout.widget.DrawerLayout;
import c.AppCompatResources;
import c4.MaterialShapeDrawable;
import c4.MaterialShapeUtils;
import c4.ShapeAppearanceModel;
import c4.ShapeAppearancePathProvider;
import com.google.android.material.R$style;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.ContextUtils;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.internal.NavigationMenuPresenter;
import com.google.android.material.internal.ScrimInsetsFrameLayout;
import com.google.android.material.internal.ThemeEnforcement;
import d4.MaterialThemeOverlay;
import z3.MaterialResources;

/* loaded from: classes.dex */
public class NavigationView extends ScrimInsetsFrameLayout {

    /* renamed from: r, reason: collision with root package name */
    private static final int[] f9008r = {16842912};

    /* renamed from: s, reason: collision with root package name */
    private static final int[] f9009s = {-16842910};

    /* renamed from: t, reason: collision with root package name */
    private static final int f9010t = R$style.Widget_Design_NavigationView;

    /* renamed from: e, reason: collision with root package name */
    private final NavigationMenu f9011e;

    /* renamed from: f, reason: collision with root package name */
    private final NavigationMenuPresenter f9012f;

    /* renamed from: g, reason: collision with root package name */
    c f9013g;

    /* renamed from: h, reason: collision with root package name */
    private final int f9014h;

    /* renamed from: i, reason: collision with root package name */
    private final int[] f9015i;

    /* renamed from: j, reason: collision with root package name */
    private MenuInflater f9016j;

    /* renamed from: k, reason: collision with root package name */
    private ViewTreeObserver.OnGlobalLayoutListener f9017k;

    /* renamed from: l, reason: collision with root package name */
    private boolean f9018l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f9019m;

    /* renamed from: n, reason: collision with root package name */
    private int f9020n;

    /* renamed from: o, reason: collision with root package name */
    private int f9021o;

    /* renamed from: p, reason: collision with root package name */
    private Path f9022p;

    /* renamed from: q, reason: collision with root package name */
    private final RectF f9023q;

    /* loaded from: classes.dex */
    class a implements MenuBuilder.a {
        a() {
        }

        @Override // androidx.appcompat.view.menu.MenuBuilder.a
        public boolean a(MenuBuilder menuBuilder, MenuItem menuItem) {
            c cVar = NavigationView.this.f9013g;
            return cVar != null && cVar.a(menuItem);
        }

        @Override // androidx.appcompat.view.menu.MenuBuilder.a
        public void b(MenuBuilder menuBuilder) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements ViewTreeObserver.OnGlobalLayoutListener {
        b() {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            NavigationView navigationView = NavigationView.this;
            navigationView.getLocationOnScreen(navigationView.f9015i);
            boolean z10 = NavigationView.this.f9015i[1] == 0;
            NavigationView.this.f9012f.setBehindStatusBar(z10);
            NavigationView navigationView2 = NavigationView.this;
            navigationView2.setDrawTopInsetForeground(z10 && navigationView2.j());
            Activity activity = ContextUtils.getActivity(NavigationView.this.getContext());
            if (activity != null) {
                boolean z11 = activity.findViewById(R.id.content).getHeight() == NavigationView.this.getHeight();
                boolean z12 = Color.alpha(activity.getWindow().getNavigationBarColor()) != 0;
                NavigationView navigationView3 = NavigationView.this;
                navigationView3.setDrawBottomInsetForeground(z11 && z12 && navigationView3.i());
            }
        }
    }

    /* loaded from: classes.dex */
    public interface c {
        boolean a(MenuItem menuItem);
    }

    public NavigationView(Context context) {
        this(context, null);
    }

    private ColorStateList c(int i10) {
        TypedValue typedValue = new TypedValue();
        if (!getContext().getTheme().resolveAttribute(i10, typedValue, true)) {
            return null;
        }
        ColorStateList a10 = AppCompatResources.a(getContext(), typedValue.resourceId);
        if (!getContext().getTheme().resolveAttribute(R$attr.colorPrimary, typedValue, true)) {
            return null;
        }
        int i11 = typedValue.data;
        int defaultColor = a10.getDefaultColor();
        int[] iArr = f9009s;
        return new ColorStateList(new int[][]{iArr, f9008r, FrameLayout.EMPTY_STATE_SET}, new int[]{a10.getColorForState(iArr, defaultColor), i11, defaultColor});
    }

    private Drawable d(TintTypedArray tintTypedArray) {
        return e(tintTypedArray, MaterialResources.b(getContext(), tintTypedArray, R$styleable.NavigationView_itemShapeFillColor));
    }

    private Drawable e(TintTypedArray tintTypedArray, ColorStateList colorStateList) {
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(ShapeAppearanceModel.b(getContext(), tintTypedArray.n(R$styleable.NavigationView_itemShapeAppearance, 0), tintTypedArray.n(R$styleable.NavigationView_itemShapeAppearanceOverlay, 0)).m());
        materialShapeDrawable.a0(colorStateList);
        return new InsetDrawable((Drawable) materialShapeDrawable, tintTypedArray.f(R$styleable.NavigationView_itemShapeInsetStart, 0), tintTypedArray.f(R$styleable.NavigationView_itemShapeInsetTop, 0), tintTypedArray.f(R$styleable.NavigationView_itemShapeInsetEnd, 0), tintTypedArray.f(R$styleable.NavigationView_itemShapeInsetBottom, 0));
    }

    private boolean f(TintTypedArray tintTypedArray) {
        return tintTypedArray.s(R$styleable.NavigationView_itemShapeAppearance) || tintTypedArray.s(R$styleable.NavigationView_itemShapeAppearanceOverlay);
    }

    private MenuInflater getMenuInflater() {
        if (this.f9016j == null) {
            this.f9016j = new SupportMenuInflater(getContext());
        }
        return this.f9016j;
    }

    private void k(int i10, int i11) {
        if ((getParent() instanceof DrawerLayout) && this.f9021o > 0 && (getBackground() instanceof MaterialShapeDrawable)) {
            MaterialShapeDrawable materialShapeDrawable = (MaterialShapeDrawable) getBackground();
            ShapeAppearanceModel.b v7 = materialShapeDrawable.D().v();
            if (GravityCompat.b(this.f9020n, ViewCompat.x(this)) == 3) {
                v7.O(this.f9021o);
                v7.B(this.f9021o);
            } else {
                v7.J(this.f9021o);
                v7.w(this.f9021o);
            }
            materialShapeDrawable.setShapeAppearanceModel(v7.m());
            if (this.f9022p == null) {
                this.f9022p = new Path();
            }
            this.f9022p.reset();
            this.f9023q.set(0.0f, 0.0f, i10, i11);
            ShapeAppearancePathProvider.k().d(materialShapeDrawable.D(), materialShapeDrawable.x(), this.f9023q, this.f9022p);
            invalidate();
            return;
        }
        this.f9022p = null;
        this.f9023q.setEmpty();
    }

    private void l() {
        this.f9017k = new b();
        getViewTreeObserver().addOnGlobalLayoutListener(this.f9017k);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        if (this.f9022p == null) {
            super.dispatchDraw(canvas);
            return;
        }
        int save = canvas.save();
        canvas.clipPath(this.f9022p);
        super.dispatchDraw(canvas);
        canvas.restoreToCount(save);
    }

    public View g(int i10) {
        return this.f9012f.inflateHeaderView(i10);
    }

    public MenuItem getCheckedItem() {
        return this.f9012f.getCheckedItem();
    }

    public int getDividerInsetEnd() {
        return this.f9012f.getDividerInsetEnd();
    }

    public int getDividerInsetStart() {
        return this.f9012f.getDividerInsetStart();
    }

    public int getHeaderCount() {
        return this.f9012f.getHeaderCount();
    }

    public Drawable getItemBackground() {
        return this.f9012f.getItemBackground();
    }

    public int getItemHorizontalPadding() {
        return this.f9012f.getItemHorizontalPadding();
    }

    public int getItemIconPadding() {
        return this.f9012f.getItemIconPadding();
    }

    public ColorStateList getItemIconTintList() {
        return this.f9012f.getItemTintList();
    }

    public int getItemMaxLines() {
        return this.f9012f.getItemMaxLines();
    }

    public ColorStateList getItemTextColor() {
        return this.f9012f.getItemTextColor();
    }

    public int getItemVerticalPadding() {
        return this.f9012f.getItemVerticalPadding();
    }

    public Menu getMenu() {
        return this.f9011e;
    }

    public int getSubheaderInsetEnd() {
        return this.f9012f.getSubheaderInsetEnd();
    }

    public int getSubheaderInsetStart() {
        return this.f9012f.getSubheaderInsetStart();
    }

    public void h(int i10) {
        this.f9012f.setUpdateSuspended(true);
        getMenuInflater().inflate(i10, this.f9011e);
        this.f9012f.setUpdateSuspended(false);
        this.f9012f.updateMenuView(false);
    }

    public boolean i() {
        return this.f9019m;
    }

    public boolean j() {
        return this.f9018l;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.material.internal.ScrimInsetsFrameLayout, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        MaterialShapeUtils.e(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.material.internal.ScrimInsetsFrameLayout, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this.f9017k);
    }

    @Override // com.google.android.material.internal.ScrimInsetsFrameLayout
    protected void onInsetsChanged(WindowInsetsCompat windowInsetsCompat) {
        this.f9012f.dispatchApplyWindowInsets(windowInsetsCompat);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        int mode = View.MeasureSpec.getMode(i10);
        if (mode == Integer.MIN_VALUE) {
            i10 = View.MeasureSpec.makeMeasureSpec(Math.min(View.MeasureSpec.getSize(i10), this.f9014h), 1073741824);
        } else if (mode == 0) {
            i10 = View.MeasureSpec.makeMeasureSpec(this.f9014h, 1073741824);
        }
        super.onMeasure(i10, i11);
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.f9011e.restorePresenterStates(savedState.f9024e);
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        Bundle bundle = new Bundle();
        savedState.f9024e = bundle;
        this.f9011e.savePresenterStates(bundle);
        return savedState;
    }

    @Override // android.view.View
    protected void onSizeChanged(int i10, int i11, int i12, int i13) {
        super.onSizeChanged(i10, i11, i12, i13);
        k(i10, i11);
    }

    public void setBottomInsetScrimEnabled(boolean z10) {
        this.f9019m = z10;
    }

    public void setCheckedItem(int i10) {
        MenuItem findItem = this.f9011e.findItem(i10);
        if (findItem != null) {
            this.f9012f.setCheckedItem((MenuItemImpl) findItem);
        }
    }

    public void setDividerInsetEnd(int i10) {
        this.f9012f.setDividerInsetEnd(i10);
    }

    public void setDividerInsetStart(int i10) {
        this.f9012f.setDividerInsetStart(i10);
    }

    @Override // android.view.View
    public void setElevation(float f10) {
        super.setElevation(f10);
        MaterialShapeUtils.d(this, f10);
    }

    public void setItemBackground(Drawable drawable) {
        this.f9012f.setItemBackground(drawable);
    }

    public void setItemBackgroundResource(int i10) {
        setItemBackground(ContextCompat.e(getContext(), i10));
    }

    public void setItemHorizontalPadding(int i10) {
        this.f9012f.setItemHorizontalPadding(i10);
    }

    public void setItemHorizontalPaddingResource(int i10) {
        this.f9012f.setItemHorizontalPadding(getResources().getDimensionPixelSize(i10));
    }

    public void setItemIconPadding(int i10) {
        this.f9012f.setItemIconPadding(i10);
    }

    public void setItemIconPaddingResource(int i10) {
        this.f9012f.setItemIconPadding(getResources().getDimensionPixelSize(i10));
    }

    public void setItemIconSize(int i10) {
        this.f9012f.setItemIconSize(i10);
    }

    public void setItemIconTintList(ColorStateList colorStateList) {
        this.f9012f.setItemIconTintList(colorStateList);
    }

    public void setItemMaxLines(int i10) {
        this.f9012f.setItemMaxLines(i10);
    }

    public void setItemTextAppearance(int i10) {
        this.f9012f.setItemTextAppearance(i10);
    }

    public void setItemTextColor(ColorStateList colorStateList) {
        this.f9012f.setItemTextColor(colorStateList);
    }

    public void setItemVerticalPadding(int i10) {
        this.f9012f.setItemVerticalPadding(i10);
    }

    public void setItemVerticalPaddingResource(int i10) {
        this.f9012f.setItemVerticalPadding(getResources().getDimensionPixelSize(i10));
    }

    public void setNavigationItemSelectedListener(c cVar) {
        this.f9013g = cVar;
    }

    @Override // android.view.View
    public void setOverScrollMode(int i10) {
        super.setOverScrollMode(i10);
        NavigationMenuPresenter navigationMenuPresenter = this.f9012f;
        if (navigationMenuPresenter != null) {
            navigationMenuPresenter.setOverScrollMode(i10);
        }
    }

    public void setSubheaderInsetEnd(int i10) {
        this.f9012f.setSubheaderInsetStart(i10);
    }

    public void setSubheaderInsetStart(int i10) {
        this.f9012f.setSubheaderInsetStart(i10);
    }

    public void setTopInsetScrimEnabled(boolean z10) {
        this.f9018l = z10;
    }

    /* loaded from: classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        public Bundle f9024e;

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
            this.f9024e = parcel.readBundle(classLoader);
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeBundle(this.f9024e);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }
    }

    public NavigationView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, com.google.android.material.R$attr.navigationViewStyle);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public NavigationView(Context context, AttributeSet attributeSet, int i10) {
        super(MaterialThemeOverlay.c(context, attributeSet, i10, r9), attributeSet, i10);
        ColorStateList c10;
        int i11 = f9010t;
        NavigationMenuPresenter navigationMenuPresenter = new NavigationMenuPresenter();
        this.f9012f = navigationMenuPresenter;
        this.f9015i = new int[2];
        this.f9018l = true;
        this.f9019m = true;
        this.f9020n = 0;
        this.f9021o = 0;
        this.f9023q = new RectF();
        Context context2 = getContext();
        NavigationMenu navigationMenu = new NavigationMenu(context2);
        this.f9011e = navigationMenu;
        TintTypedArray obtainTintedStyledAttributes = ThemeEnforcement.obtainTintedStyledAttributes(context2, attributeSet, R$styleable.NavigationView, i10, i11, new int[0]);
        int i12 = R$styleable.NavigationView_android_background;
        if (obtainTintedStyledAttributes.s(i12)) {
            ViewCompat.p0(this, obtainTintedStyledAttributes.g(i12));
        }
        this.f9021o = obtainTintedStyledAttributes.f(R$styleable.NavigationView_drawerLayoutCornerSize, 0);
        this.f9020n = obtainTintedStyledAttributes.k(R$styleable.NavigationView_android_layout_gravity, 0);
        if (getBackground() == null || (getBackground() instanceof ColorDrawable)) {
            ShapeAppearanceModel m10 = ShapeAppearanceModel.e(context2, attributeSet, i10, i11).m();
            Drawable background = getBackground();
            MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(m10);
            if (background instanceof ColorDrawable) {
                materialShapeDrawable.a0(ColorStateList.valueOf(((ColorDrawable) background).getColor()));
            }
            materialShapeDrawable.P(context2);
            ViewCompat.p0(this, materialShapeDrawable);
        }
        if (obtainTintedStyledAttributes.s(R$styleable.NavigationView_elevation)) {
            setElevation(obtainTintedStyledAttributes.f(r2, 0));
        }
        setFitsSystemWindows(obtainTintedStyledAttributes.a(R$styleable.NavigationView_android_fitsSystemWindows, false));
        this.f9014h = obtainTintedStyledAttributes.f(R$styleable.NavigationView_android_maxWidth, 0);
        int i13 = R$styleable.NavigationView_subheaderColor;
        ColorStateList c11 = obtainTintedStyledAttributes.s(i13) ? obtainTintedStyledAttributes.c(i13) : null;
        int i14 = R$styleable.NavigationView_subheaderTextAppearance;
        int n10 = obtainTintedStyledAttributes.s(i14) ? obtainTintedStyledAttributes.n(i14, 0) : 0;
        if (n10 == 0 && c11 == null) {
            c11 = c(R.attr.textColorSecondary);
        }
        int i15 = R$styleable.NavigationView_itemIconTint;
        if (obtainTintedStyledAttributes.s(i15)) {
            c10 = obtainTintedStyledAttributes.c(i15);
        } else {
            c10 = c(R.attr.textColorSecondary);
        }
        int i16 = R$styleable.NavigationView_itemTextAppearance;
        int n11 = obtainTintedStyledAttributes.s(i16) ? obtainTintedStyledAttributes.n(i16, 0) : 0;
        int i17 = R$styleable.NavigationView_itemIconSize;
        if (obtainTintedStyledAttributes.s(i17)) {
            setItemIconSize(obtainTintedStyledAttributes.f(i17, 0));
        }
        int i18 = R$styleable.NavigationView_itemTextColor;
        ColorStateList c12 = obtainTintedStyledAttributes.s(i18) ? obtainTintedStyledAttributes.c(i18) : null;
        if (n11 == 0 && c12 == null) {
            c12 = c(R.attr.textColorPrimary);
        }
        Drawable g6 = obtainTintedStyledAttributes.g(R$styleable.NavigationView_itemBackground);
        if (g6 == null && f(obtainTintedStyledAttributes)) {
            g6 = d(obtainTintedStyledAttributes);
            ColorStateList b10 = MaterialResources.b(context2, obtainTintedStyledAttributes, R$styleable.NavigationView_itemRippleColor);
            if (b10 != null) {
                navigationMenuPresenter.setItemForeground(new RippleDrawable(RippleUtils.d(b10), null, e(obtainTintedStyledAttributes, null)));
            }
        }
        int i19 = R$styleable.NavigationView_itemHorizontalPadding;
        if (obtainTintedStyledAttributes.s(i19)) {
            setItemHorizontalPadding(obtainTintedStyledAttributes.f(i19, 0));
        }
        int i20 = R$styleable.NavigationView_itemVerticalPadding;
        if (obtainTintedStyledAttributes.s(i20)) {
            setItemVerticalPadding(obtainTintedStyledAttributes.f(i20, 0));
        }
        setDividerInsetStart(obtainTintedStyledAttributes.f(R$styleable.NavigationView_dividerInsetStart, 0));
        setDividerInsetEnd(obtainTintedStyledAttributes.f(R$styleable.NavigationView_dividerInsetEnd, 0));
        setSubheaderInsetStart(obtainTintedStyledAttributes.f(R$styleable.NavigationView_subheaderInsetStart, 0));
        setSubheaderInsetEnd(obtainTintedStyledAttributes.f(R$styleable.NavigationView_subheaderInsetEnd, 0));
        setTopInsetScrimEnabled(obtainTintedStyledAttributes.a(R$styleable.NavigationView_topInsetScrimEnabled, this.f9018l));
        setBottomInsetScrimEnabled(obtainTintedStyledAttributes.a(R$styleable.NavigationView_bottomInsetScrimEnabled, this.f9019m));
        int f10 = obtainTintedStyledAttributes.f(R$styleable.NavigationView_itemIconPadding, 0);
        setItemMaxLines(obtainTintedStyledAttributes.k(R$styleable.NavigationView_itemMaxLines, 1));
        navigationMenu.setCallback(new a());
        navigationMenuPresenter.setId(1);
        navigationMenuPresenter.initForMenu(context2, navigationMenu);
        if (n10 != 0) {
            navigationMenuPresenter.setSubheaderTextAppearance(n10);
        }
        navigationMenuPresenter.setSubheaderColor(c11);
        navigationMenuPresenter.setItemIconTintList(c10);
        navigationMenuPresenter.setOverScrollMode(getOverScrollMode());
        if (n11 != 0) {
            navigationMenuPresenter.setItemTextAppearance(n11);
        }
        navigationMenuPresenter.setItemTextColor(c12);
        navigationMenuPresenter.setItemBackground(g6);
        navigationMenuPresenter.setItemIconPadding(f10);
        navigationMenu.addMenuPresenter(navigationMenuPresenter);
        addView((View) navigationMenuPresenter.getMenuView(this));
        int i21 = R$styleable.NavigationView_menu;
        if (obtainTintedStyledAttributes.s(i21)) {
            h(obtainTintedStyledAttributes.n(i21, 0));
        }
        int i22 = R$styleable.NavigationView_headerLayout;
        if (obtainTintedStyledAttributes.s(i22)) {
            g(obtainTintedStyledAttributes.n(i22, 0));
        }
        obtainTintedStyledAttributes.x();
        l();
    }

    public void setCheckedItem(MenuItem menuItem) {
        MenuItem findItem = this.f9011e.findItem(menuItem.getItemId());
        if (findItem != null) {
            this.f9012f.setCheckedItem((MenuItemImpl) findItem);
            return;
        }
        throw new IllegalArgumentException("Called setCheckedItem(MenuItem) with an item that is not in the current menu.");
    }
}
