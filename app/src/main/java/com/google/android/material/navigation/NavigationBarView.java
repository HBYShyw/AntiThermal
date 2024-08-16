package com.google.android.material.navigation;

import a4.RippleUtils;
import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.view.AbsSavedState;
import c4.MaterialShapeDrawable;
import c4.MaterialShapeUtils;
import c4.ShapeAppearanceModel;
import com.google.android.material.R$dimen;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.ThemeEnforcement;
import d4.MaterialThemeOverlay;
import z3.MaterialResources;

/* loaded from: classes.dex */
public abstract class NavigationBarView extends FrameLayout {

    /* renamed from: e, reason: collision with root package name */
    private final NavigationBarMenu f8999e;

    /* renamed from: f, reason: collision with root package name */
    private final NavigationBarMenuView f9000f;

    /* renamed from: g, reason: collision with root package name */
    private final NavigationBarPresenter f9001g;

    /* renamed from: h, reason: collision with root package name */
    private ColorStateList f9002h;

    /* renamed from: i, reason: collision with root package name */
    private MenuInflater f9003i;

    /* renamed from: j, reason: collision with root package name */
    private c f9004j;

    /* renamed from: k, reason: collision with root package name */
    private b f9005k;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        Bundle f9006e;

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

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private void j(Parcel parcel, ClassLoader classLoader) {
            this.f9006e = parcel.readBundle(classLoader);
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeBundle(this.f9006e);
        }

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            j(parcel, classLoader == null ? getClass().getClassLoader() : classLoader);
        }
    }

    /* loaded from: classes.dex */
    class a implements MenuBuilder.a {
        a() {
        }

        @Override // androidx.appcompat.view.menu.MenuBuilder.a
        public boolean a(MenuBuilder menuBuilder, MenuItem menuItem) {
            if (NavigationBarView.this.f9005k == null || menuItem.getItemId() != NavigationBarView.this.getSelectedItemId()) {
                return (NavigationBarView.this.f9004j == null || NavigationBarView.this.f9004j.a(menuItem)) ? false : true;
            }
            NavigationBarView.this.f9005k.a(menuItem);
            return true;
        }

        @Override // androidx.appcompat.view.menu.MenuBuilder.a
        public void b(MenuBuilder menuBuilder) {
        }
    }

    /* loaded from: classes.dex */
    public interface b {
        void a(MenuItem menuItem);
    }

    /* loaded from: classes.dex */
    public interface c {
        boolean a(MenuItem menuItem);
    }

    public NavigationBarView(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(MaterialThemeOverlay.c(context, attributeSet, i10, i11), attributeSet, i10);
        NavigationBarPresenter navigationBarPresenter = new NavigationBarPresenter();
        this.f9001g = navigationBarPresenter;
        Context context2 = getContext();
        int[] iArr = R$styleable.NavigationBarView;
        int i12 = R$styleable.NavigationBarView_itemTextAppearanceInactive;
        int i13 = R$styleable.NavigationBarView_itemTextAppearanceActive;
        TintTypedArray obtainTintedStyledAttributes = ThemeEnforcement.obtainTintedStyledAttributes(context2, attributeSet, iArr, i10, i11, i12, i13);
        NavigationBarMenu navigationBarMenu = new NavigationBarMenu(context2, getClass(), getMaxItemCount());
        this.f8999e = navigationBarMenu;
        NavigationBarMenuView d10 = d(context2);
        this.f9000f = d10;
        navigationBarPresenter.b(d10);
        navigationBarPresenter.a(1);
        d10.setPresenter(navigationBarPresenter);
        navigationBarMenu.addMenuPresenter(navigationBarPresenter);
        navigationBarPresenter.initForMenu(getContext(), navigationBarMenu);
        int i14 = R$styleable.NavigationBarView_itemIconTint;
        if (obtainTintedStyledAttributes.s(i14)) {
            d10.setIconTintList(obtainTintedStyledAttributes.c(i14));
        } else {
            d10.setIconTintList(d10.d(R.attr.textColorSecondary));
        }
        setItemIconSize(obtainTintedStyledAttributes.f(R$styleable.NavigationBarView_itemIconSize, getResources().getDimensionPixelSize(R$dimen.mtrl_navigation_bar_item_default_icon_size)));
        if (obtainTintedStyledAttributes.s(i12)) {
            setItemTextAppearanceInactive(obtainTintedStyledAttributes.n(i12, 0));
        }
        if (obtainTintedStyledAttributes.s(i13)) {
            setItemTextAppearanceActive(obtainTintedStyledAttributes.n(i13, 0));
        }
        int i15 = R$styleable.NavigationBarView_itemTextColor;
        if (obtainTintedStyledAttributes.s(i15)) {
            setItemTextColor(obtainTintedStyledAttributes.c(i15));
        }
        if (getBackground() == null || (getBackground() instanceof ColorDrawable)) {
            ViewCompat.p0(this, c(context2));
        }
        int i16 = R$styleable.NavigationBarView_itemPaddingTop;
        if (obtainTintedStyledAttributes.s(i16)) {
            setItemPaddingTop(obtainTintedStyledAttributes.f(i16, 0));
        }
        int i17 = R$styleable.NavigationBarView_itemPaddingBottom;
        if (obtainTintedStyledAttributes.s(i17)) {
            setItemPaddingBottom(obtainTintedStyledAttributes.f(i17, 0));
        }
        if (obtainTintedStyledAttributes.s(R$styleable.NavigationBarView_elevation)) {
            setElevation(obtainTintedStyledAttributes.f(r12, 0));
        }
        DrawableCompat.i(getBackground().mutate(), MaterialResources.b(context2, obtainTintedStyledAttributes, R$styleable.NavigationBarView_backgroundTint));
        setLabelVisibilityMode(obtainTintedStyledAttributes.l(R$styleable.NavigationBarView_labelVisibilityMode, -1));
        int n10 = obtainTintedStyledAttributes.n(R$styleable.NavigationBarView_itemBackground, 0);
        if (n10 != 0) {
            d10.setItemBackgroundRes(n10);
        } else {
            setItemRippleColor(MaterialResources.b(context2, obtainTintedStyledAttributes, R$styleable.NavigationBarView_itemRippleColor));
        }
        int n11 = obtainTintedStyledAttributes.n(R$styleable.NavigationBarView_itemActiveIndicatorStyle, 0);
        if (n11 != 0) {
            setItemActiveIndicatorEnabled(true);
            TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(n11, R$styleable.NavigationBarActiveIndicator);
            setItemActiveIndicatorWidth(obtainStyledAttributes.getDimensionPixelSize(R$styleable.NavigationBarActiveIndicator_android_width, 0));
            setItemActiveIndicatorHeight(obtainStyledAttributes.getDimensionPixelSize(R$styleable.NavigationBarActiveIndicator_android_height, 0));
            setItemActiveIndicatorMarginHorizontal(obtainStyledAttributes.getDimensionPixelOffset(R$styleable.NavigationBarActiveIndicator_marginHorizontal, 0));
            setItemActiveIndicatorColor(MaterialResources.a(context2, obtainStyledAttributes, R$styleable.NavigationBarActiveIndicator_android_color));
            setItemActiveIndicatorShapeAppearance(ShapeAppearanceModel.b(context2, obtainStyledAttributes.getResourceId(R$styleable.NavigationBarActiveIndicator_shapeAppearance, 0), 0).m());
            obtainStyledAttributes.recycle();
        }
        int i18 = R$styleable.NavigationBarView_menu;
        if (obtainTintedStyledAttributes.s(i18)) {
            e(obtainTintedStyledAttributes.n(i18, 0));
        }
        obtainTintedStyledAttributes.x();
        addView(d10);
        navigationBarMenu.setCallback(new a());
    }

    private MaterialShapeDrawable c(Context context) {
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
        Drawable background = getBackground();
        if (background instanceof ColorDrawable) {
            materialShapeDrawable.a0(ColorStateList.valueOf(((ColorDrawable) background).getColor()));
        }
        materialShapeDrawable.P(context);
        return materialShapeDrawable;
    }

    private MenuInflater getMenuInflater() {
        if (this.f9003i == null) {
            this.f9003i = new SupportMenuInflater(getContext());
        }
        return this.f9003i;
    }

    protected abstract NavigationBarMenuView d(Context context);

    public void e(int i10) {
        this.f9001g.c(true);
        getMenuInflater().inflate(i10, this.f8999e);
        this.f9001g.c(false);
        this.f9001g.updateMenuView(true);
    }

    public ColorStateList getItemActiveIndicatorColor() {
        return this.f9000f.getItemActiveIndicatorColor();
    }

    public int getItemActiveIndicatorHeight() {
        return this.f9000f.getItemActiveIndicatorHeight();
    }

    public int getItemActiveIndicatorMarginHorizontal() {
        return this.f9000f.getItemActiveIndicatorMarginHorizontal();
    }

    public ShapeAppearanceModel getItemActiveIndicatorShapeAppearance() {
        return this.f9000f.getItemActiveIndicatorShapeAppearance();
    }

    public int getItemActiveIndicatorWidth() {
        return this.f9000f.getItemActiveIndicatorWidth();
    }

    public Drawable getItemBackground() {
        return this.f9000f.getItemBackground();
    }

    @Deprecated
    public int getItemBackgroundResource() {
        return this.f9000f.getItemBackgroundRes();
    }

    public int getItemIconSize() {
        return this.f9000f.getItemIconSize();
    }

    public ColorStateList getItemIconTintList() {
        return this.f9000f.getIconTintList();
    }

    public int getItemPaddingBottom() {
        return this.f9000f.getItemPaddingBottom();
    }

    public int getItemPaddingTop() {
        return this.f9000f.getItemPaddingTop();
    }

    public ColorStateList getItemRippleColor() {
        return this.f9002h;
    }

    public int getItemTextAppearanceActive() {
        return this.f9000f.getItemTextAppearanceActive();
    }

    public int getItemTextAppearanceInactive() {
        return this.f9000f.getItemTextAppearanceInactive();
    }

    public ColorStateList getItemTextColor() {
        return this.f9000f.getItemTextColor();
    }

    public int getLabelVisibilityMode() {
        return this.f9000f.getLabelVisibilityMode();
    }

    public abstract int getMaxItemCount();

    public Menu getMenu() {
        return this.f8999e;
    }

    public MenuView getMenuView() {
        return this.f9000f;
    }

    public NavigationBarPresenter getPresenter() {
        return this.f9001g;
    }

    public int getSelectedItemId() {
        return this.f9000f.getSelectedItemId();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        MaterialShapeUtils.e(this);
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.f8999e.restorePresenterStates(savedState.f9006e);
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        Bundle bundle = new Bundle();
        savedState.f9006e = bundle;
        this.f8999e.savePresenterStates(bundle);
        return savedState;
    }

    @Override // android.view.View
    public void setElevation(float f10) {
        super.setElevation(f10);
        MaterialShapeUtils.d(this, f10);
    }

    public void setItemActiveIndicatorColor(ColorStateList colorStateList) {
        this.f9000f.setItemActiveIndicatorColor(colorStateList);
    }

    public void setItemActiveIndicatorEnabled(boolean z10) {
        this.f9000f.setItemActiveIndicatorEnabled(z10);
    }

    public void setItemActiveIndicatorHeight(int i10) {
        this.f9000f.setItemActiveIndicatorHeight(i10);
    }

    public void setItemActiveIndicatorMarginHorizontal(int i10) {
        this.f9000f.setItemActiveIndicatorMarginHorizontal(i10);
    }

    public void setItemActiveIndicatorShapeAppearance(ShapeAppearanceModel shapeAppearanceModel) {
        this.f9000f.setItemActiveIndicatorShapeAppearance(shapeAppearanceModel);
    }

    public void setItemActiveIndicatorWidth(int i10) {
        this.f9000f.setItemActiveIndicatorWidth(i10);
    }

    public void setItemBackground(Drawable drawable) {
        this.f9000f.setItemBackground(drawable);
        this.f9002h = null;
    }

    public void setItemBackgroundResource(int i10) {
        this.f9000f.setItemBackgroundRes(i10);
        this.f9002h = null;
    }

    public void setItemIconSize(int i10) {
        this.f9000f.setItemIconSize(i10);
    }

    public void setItemIconSizeRes(int i10) {
        setItemIconSize(getResources().getDimensionPixelSize(i10));
    }

    public void setItemIconTintList(ColorStateList colorStateList) {
        this.f9000f.setIconTintList(colorStateList);
    }

    public void setItemPaddingBottom(int i10) {
        this.f9000f.setItemPaddingBottom(i10);
    }

    public void setItemPaddingTop(int i10) {
        this.f9000f.setItemPaddingTop(i10);
    }

    public void setItemRippleColor(ColorStateList colorStateList) {
        if (this.f9002h == colorStateList) {
            if (colorStateList != null || this.f9000f.getItemBackground() == null) {
                return;
            }
            this.f9000f.setItemBackground(null);
            return;
        }
        this.f9002h = colorStateList;
        if (colorStateList == null) {
            this.f9000f.setItemBackground(null);
        } else {
            this.f9000f.setItemBackground(new RippleDrawable(RippleUtils.a(colorStateList), null, null));
        }
    }

    public void setItemTextAppearanceActive(int i10) {
        this.f9000f.setItemTextAppearanceActive(i10);
    }

    public void setItemTextAppearanceInactive(int i10) {
        this.f9000f.setItemTextAppearanceInactive(i10);
    }

    public void setItemTextColor(ColorStateList colorStateList) {
        this.f9000f.setItemTextColor(colorStateList);
    }

    public void setLabelVisibilityMode(int i10) {
        if (this.f9000f.getLabelVisibilityMode() != i10) {
            this.f9000f.setLabelVisibilityMode(i10);
            this.f9001g.updateMenuView(false);
        }
    }

    public void setOnItemReselectedListener(b bVar) {
        this.f9005k = bVar;
    }

    public void setOnItemSelectedListener(c cVar) {
        this.f9004j = cVar;
    }

    public void setSelectedItemId(int i10) {
        MenuItem findItem = this.f8999e.findItem(i10);
        if (findItem == null || this.f8999e.performItemAction(findItem, this.f9001g, 0)) {
            return;
        }
        findItem.setChecked(true);
    }
}
