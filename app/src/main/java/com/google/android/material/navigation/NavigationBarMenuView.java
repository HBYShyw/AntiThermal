package com.google.android.material.navigation;

import android.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.util.e;
import androidx.core.util.g;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;
import c.AppCompatResources;
import c4.MaterialShapeDrawable;
import c4.ShapeAppearanceModel;
import com.google.android.material.R$attr;
import com.google.android.material.R$integer;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.internal.TextScale;
import java.util.HashSet;
import p3.AnimationUtils;
import x3.MotionUtils;

/* loaded from: classes.dex */
public abstract class NavigationBarMenuView extends ViewGroup implements MenuView {
    private static final int[] G = {16842912};
    private static final int[] H = {-16842910};
    private int A;
    private ShapeAppearanceModel B;
    private boolean C;
    private ColorStateList D;
    private NavigationBarPresenter E;
    private MenuBuilder F;

    /* renamed from: e, reason: collision with root package name */
    private final TransitionSet f8970e;

    /* renamed from: f, reason: collision with root package name */
    private final View.OnClickListener f8971f;

    /* renamed from: g, reason: collision with root package name */
    private final e<NavigationBarItemView> f8972g;

    /* renamed from: h, reason: collision with root package name */
    private final SparseArray<View.OnTouchListener> f8973h;

    /* renamed from: i, reason: collision with root package name */
    private int f8974i;

    /* renamed from: j, reason: collision with root package name */
    private NavigationBarItemView[] f8975j;

    /* renamed from: k, reason: collision with root package name */
    private int f8976k;

    /* renamed from: l, reason: collision with root package name */
    private int f8977l;

    /* renamed from: m, reason: collision with root package name */
    private ColorStateList f8978m;

    /* renamed from: n, reason: collision with root package name */
    private int f8979n;

    /* renamed from: o, reason: collision with root package name */
    private ColorStateList f8980o;

    /* renamed from: p, reason: collision with root package name */
    private final ColorStateList f8981p;

    /* renamed from: q, reason: collision with root package name */
    private int f8982q;

    /* renamed from: r, reason: collision with root package name */
    private int f8983r;

    /* renamed from: s, reason: collision with root package name */
    private Drawable f8984s;

    /* renamed from: t, reason: collision with root package name */
    private int f8985t;

    /* renamed from: u, reason: collision with root package name */
    private final SparseArray<BadgeDrawable> f8986u;

    /* renamed from: v, reason: collision with root package name */
    private int f8987v;

    /* renamed from: w, reason: collision with root package name */
    private int f8988w;

    /* renamed from: x, reason: collision with root package name */
    private boolean f8989x;

    /* renamed from: y, reason: collision with root package name */
    private int f8990y;

    /* renamed from: z, reason: collision with root package name */
    private int f8991z;

    /* loaded from: classes.dex */
    class a implements View.OnClickListener {
        a() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            MenuItemImpl itemData = ((NavigationBarItemView) view).getItemData();
            if (NavigationBarMenuView.this.F.performItemAction(itemData, NavigationBarMenuView.this.E, 0)) {
                return;
            }
            itemData.setChecked(true);
        }
    }

    public NavigationBarMenuView(Context context) {
        super(context);
        this.f8972g = new g(5);
        this.f8973h = new SparseArray<>(5);
        this.f8976k = 0;
        this.f8977l = 0;
        this.f8986u = new SparseArray<>(5);
        this.f8987v = -1;
        this.f8988w = -1;
        this.C = false;
        this.f8981p = d(R.attr.textColorSecondary);
        if (isInEditMode()) {
            this.f8970e = null;
        } else {
            AutoTransition autoTransition = new AutoTransition();
            this.f8970e = autoTransition;
            autoTransition.r(0);
            autoTransition.setDuration(MotionUtils.d(getContext(), R$attr.motionDurationLong1, getResources().getInteger(R$integer.material_motion_duration_long_1)));
            autoTransition.setInterpolator(MotionUtils.e(getContext(), R$attr.motionEasingStandard, AnimationUtils.f16556b));
            autoTransition.f(new TextScale());
        }
        this.f8971f = new a();
        ViewCompat.w0(this, 1);
    }

    private Drawable e() {
        if (this.B == null || this.D == null) {
            return null;
        }
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(this.B);
        materialShapeDrawable.a0(this.D);
        return materialShapeDrawable;
    }

    private NavigationBarItemView getNewItem() {
        NavigationBarItemView b10 = this.f8972g.b();
        return b10 == null ? f(getContext()) : b10;
    }

    private boolean i(int i10) {
        return i10 != -1;
    }

    private void j() {
        HashSet hashSet = new HashSet();
        for (int i10 = 0; i10 < this.F.size(); i10++) {
            hashSet.add(Integer.valueOf(this.F.getItem(i10).getItemId()));
        }
        for (int i11 = 0; i11 < this.f8986u.size(); i11++) {
            int keyAt = this.f8986u.keyAt(i11);
            if (!hashSet.contains(Integer.valueOf(keyAt))) {
                this.f8986u.delete(keyAt);
            }
        }
    }

    private void n(int i10) {
        if (i(i10)) {
            return;
        }
        throw new IllegalArgumentException(i10 + " is not a valid view id");
    }

    private void setBadgeIfNeeded(NavigationBarItemView navigationBarItemView) {
        BadgeDrawable badgeDrawable;
        int id2 = navigationBarItemView.getId();
        if (i(id2) && (badgeDrawable = this.f8986u.get(id2)) != null) {
            navigationBarItemView.setBadge(badgeDrawable);
        }
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public void c() {
        removeAllViews();
        NavigationBarItemView[] navigationBarItemViewArr = this.f8975j;
        if (navigationBarItemViewArr != null) {
            for (NavigationBarItemView navigationBarItemView : navigationBarItemViewArr) {
                if (navigationBarItemView != null) {
                    this.f8972g.a(navigationBarItemView);
                    navigationBarItemView.f();
                }
            }
        }
        if (this.F.size() == 0) {
            this.f8976k = 0;
            this.f8977l = 0;
            this.f8975j = null;
            return;
        }
        j();
        this.f8975j = new NavigationBarItemView[this.F.size()];
        boolean h10 = h(this.f8974i, this.F.getVisibleItems().size());
        for (int i10 = 0; i10 < this.F.size(); i10++) {
            this.E.c(true);
            this.F.getItem(i10).setCheckable(true);
            this.E.c(false);
            NavigationBarItemView newItem = getNewItem();
            this.f8975j[i10] = newItem;
            newItem.setIconTintList(this.f8978m);
            newItem.setIconSize(this.f8979n);
            newItem.setTextColor(this.f8981p);
            newItem.setTextAppearanceInactive(this.f8982q);
            newItem.setTextAppearanceActive(this.f8983r);
            newItem.setTextColor(this.f8980o);
            int i11 = this.f8987v;
            if (i11 != -1) {
                newItem.setItemPaddingTop(i11);
            }
            int i12 = this.f8988w;
            if (i12 != -1) {
                newItem.setItemPaddingBottom(i12);
            }
            newItem.setActiveIndicatorWidth(this.f8990y);
            newItem.setActiveIndicatorHeight(this.f8991z);
            newItem.setActiveIndicatorMarginHorizontal(this.A);
            newItem.setActiveIndicatorDrawable(e());
            newItem.setActiveIndicatorResizeable(this.C);
            newItem.setActiveIndicatorEnabled(this.f8989x);
            Drawable drawable = this.f8984s;
            if (drawable != null) {
                newItem.setItemBackground(drawable);
            } else {
                newItem.setItemBackground(this.f8985t);
            }
            newItem.setShifting(h10);
            newItem.setLabelVisibilityMode(this.f8974i);
            MenuItemImpl menuItemImpl = (MenuItemImpl) this.F.getItem(i10);
            newItem.initialize(menuItemImpl, 0);
            newItem.setItemPosition(i10);
            int itemId = menuItemImpl.getItemId();
            newItem.setOnTouchListener(this.f8973h.get(itemId));
            newItem.setOnClickListener(this.f8971f);
            int i13 = this.f8976k;
            if (i13 != 0 && itemId == i13) {
                this.f8977l = i10;
            }
            setBadgeIfNeeded(newItem);
            addView(newItem);
        }
        int min = Math.min(this.F.size() - 1, this.f8977l);
        this.f8977l = min;
        this.F.getItem(min).setChecked(true);
    }

    public ColorStateList d(int i10) {
        TypedValue typedValue = new TypedValue();
        if (!getContext().getTheme().resolveAttribute(i10, typedValue, true)) {
            return null;
        }
        ColorStateList a10 = AppCompatResources.a(getContext(), typedValue.resourceId);
        if (!getContext().getTheme().resolveAttribute(androidx.appcompat.R$attr.colorPrimary, typedValue, true)) {
            return null;
        }
        int i11 = typedValue.data;
        int defaultColor = a10.getDefaultColor();
        int[] iArr = H;
        return new ColorStateList(new int[][]{iArr, G, ViewGroup.EMPTY_STATE_SET}, new int[]{a10.getColorForState(iArr, defaultColor), i11, defaultColor});
    }

    protected abstract NavigationBarItemView f(Context context);

    public NavigationBarItemView g(int i10) {
        n(i10);
        NavigationBarItemView[] navigationBarItemViewArr = this.f8975j;
        if (navigationBarItemViewArr == null) {
            return null;
        }
        for (NavigationBarItemView navigationBarItemView : navigationBarItemViewArr) {
            if (navigationBarItemView.getId() == i10) {
                return navigationBarItemView;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SparseArray<BadgeDrawable> getBadgeDrawables() {
        return this.f8986u;
    }

    public ColorStateList getIconTintList() {
        return this.f8978m;
    }

    public ColorStateList getItemActiveIndicatorColor() {
        return this.D;
    }

    public boolean getItemActiveIndicatorEnabled() {
        return this.f8989x;
    }

    public int getItemActiveIndicatorHeight() {
        return this.f8991z;
    }

    public int getItemActiveIndicatorMarginHorizontal() {
        return this.A;
    }

    public ShapeAppearanceModel getItemActiveIndicatorShapeAppearance() {
        return this.B;
    }

    public int getItemActiveIndicatorWidth() {
        return this.f8990y;
    }

    public Drawable getItemBackground() {
        NavigationBarItemView[] navigationBarItemViewArr = this.f8975j;
        if (navigationBarItemViewArr != null && navigationBarItemViewArr.length > 0) {
            return navigationBarItemViewArr[0].getBackground();
        }
        return this.f8984s;
    }

    @Deprecated
    public int getItemBackgroundRes() {
        return this.f8985t;
    }

    public int getItemIconSize() {
        return this.f8979n;
    }

    public int getItemPaddingBottom() {
        return this.f8988w;
    }

    public int getItemPaddingTop() {
        return this.f8987v;
    }

    public int getItemTextAppearanceActive() {
        return this.f8983r;
    }

    public int getItemTextAppearanceInactive() {
        return this.f8982q;
    }

    public ColorStateList getItemTextColor() {
        return this.f8980o;
    }

    public int getLabelVisibilityMode() {
        return this.f8974i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public MenuBuilder getMenu() {
        return this.F;
    }

    public int getSelectedItemId() {
        return this.f8976k;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getSelectedItemPosition() {
        return this.f8977l;
    }

    public int getWindowAnimations() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean h(int i10, int i11) {
        if (i10 == -1) {
            if (i11 > 3) {
                return true;
            }
        } else if (i10 == 0) {
            return true;
        }
        return false;
    }

    @Override // androidx.appcompat.view.menu.MenuView
    public void initialize(MenuBuilder menuBuilder) {
        this.F = menuBuilder;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k(SparseArray<BadgeDrawable> sparseArray) {
        for (int i10 = 0; i10 < sparseArray.size(); i10++) {
            int keyAt = sparseArray.keyAt(i10);
            if (this.f8986u.indexOfKey(keyAt) < 0) {
                this.f8986u.append(keyAt, sparseArray.get(keyAt));
            }
        }
        NavigationBarItemView[] navigationBarItemViewArr = this.f8975j;
        if (navigationBarItemViewArr != null) {
            for (NavigationBarItemView navigationBarItemView : navigationBarItemViewArr) {
                navigationBarItemView.setBadge(this.f8986u.get(navigationBarItemView.getId()));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void l(int i10) {
        int size = this.F.size();
        for (int i11 = 0; i11 < size; i11++) {
            MenuItem item = this.F.getItem(i11);
            if (i10 == item.getItemId()) {
                this.f8976k = i10;
                this.f8977l = i11;
                item.setChecked(true);
                return;
            }
        }
    }

    public void m() {
        TransitionSet transitionSet;
        MenuBuilder menuBuilder = this.F;
        if (menuBuilder == null || this.f8975j == null) {
            return;
        }
        int size = menuBuilder.size();
        if (size != this.f8975j.length) {
            c();
            return;
        }
        int i10 = this.f8976k;
        for (int i11 = 0; i11 < size; i11++) {
            MenuItem item = this.F.getItem(i11);
            if (item.isChecked()) {
                this.f8976k = item.getItemId();
                this.f8977l = i11;
            }
        }
        if (i10 != this.f8976k && (transitionSet = this.f8970e) != null) {
            TransitionManager.a(this, transitionSet);
        }
        boolean h10 = h(this.f8974i, this.F.getVisibleItems().size());
        for (int i12 = 0; i12 < size; i12++) {
            this.E.c(true);
            this.f8975j[i12].setLabelVisibilityMode(this.f8974i);
            this.f8975j[i12].setShifting(h10);
            this.f8975j[i12].initialize((MenuItemImpl) this.F.getItem(i12), 0);
            this.E.c(false);
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        AccessibilityNodeInfoCompat.C0(accessibilityNodeInfo).X(AccessibilityNodeInfoCompat.b.b(1, this.F.getVisibleItems().size(), false, 1));
    }

    public void setIconTintList(ColorStateList colorStateList) {
        this.f8978m = colorStateList;
        NavigationBarItemView[] navigationBarItemViewArr = this.f8975j;
        if (navigationBarItemViewArr != null) {
            for (NavigationBarItemView navigationBarItemView : navigationBarItemViewArr) {
                navigationBarItemView.setIconTintList(colorStateList);
            }
        }
    }

    public void setItemActiveIndicatorColor(ColorStateList colorStateList) {
        this.D = colorStateList;
        NavigationBarItemView[] navigationBarItemViewArr = this.f8975j;
        if (navigationBarItemViewArr != null) {
            for (NavigationBarItemView navigationBarItemView : navigationBarItemViewArr) {
                navigationBarItemView.setActiveIndicatorDrawable(e());
            }
        }
    }

    public void setItemActiveIndicatorEnabled(boolean z10) {
        this.f8989x = z10;
        NavigationBarItemView[] navigationBarItemViewArr = this.f8975j;
        if (navigationBarItemViewArr != null) {
            for (NavigationBarItemView navigationBarItemView : navigationBarItemViewArr) {
                navigationBarItemView.setActiveIndicatorEnabled(z10);
            }
        }
    }

    public void setItemActiveIndicatorHeight(int i10) {
        this.f8991z = i10;
        NavigationBarItemView[] navigationBarItemViewArr = this.f8975j;
        if (navigationBarItemViewArr != null) {
            for (NavigationBarItemView navigationBarItemView : navigationBarItemViewArr) {
                navigationBarItemView.setActiveIndicatorHeight(i10);
            }
        }
    }

    public void setItemActiveIndicatorMarginHorizontal(int i10) {
        this.A = i10;
        NavigationBarItemView[] navigationBarItemViewArr = this.f8975j;
        if (navigationBarItemViewArr != null) {
            for (NavigationBarItemView navigationBarItemView : navigationBarItemViewArr) {
                navigationBarItemView.setActiveIndicatorMarginHorizontal(i10);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setItemActiveIndicatorResizeable(boolean z10) {
        this.C = z10;
        NavigationBarItemView[] navigationBarItemViewArr = this.f8975j;
        if (navigationBarItemViewArr != null) {
            for (NavigationBarItemView navigationBarItemView : navigationBarItemViewArr) {
                navigationBarItemView.setActiveIndicatorResizeable(z10);
            }
        }
    }

    public void setItemActiveIndicatorShapeAppearance(ShapeAppearanceModel shapeAppearanceModel) {
        this.B = shapeAppearanceModel;
        NavigationBarItemView[] navigationBarItemViewArr = this.f8975j;
        if (navigationBarItemViewArr != null) {
            for (NavigationBarItemView navigationBarItemView : navigationBarItemViewArr) {
                navigationBarItemView.setActiveIndicatorDrawable(e());
            }
        }
    }

    public void setItemActiveIndicatorWidth(int i10) {
        this.f8990y = i10;
        NavigationBarItemView[] navigationBarItemViewArr = this.f8975j;
        if (navigationBarItemViewArr != null) {
            for (NavigationBarItemView navigationBarItemView : navigationBarItemViewArr) {
                navigationBarItemView.setActiveIndicatorWidth(i10);
            }
        }
    }

    public void setItemBackground(Drawable drawable) {
        this.f8984s = drawable;
        NavigationBarItemView[] navigationBarItemViewArr = this.f8975j;
        if (navigationBarItemViewArr != null) {
            for (NavigationBarItemView navigationBarItemView : navigationBarItemViewArr) {
                navigationBarItemView.setItemBackground(drawable);
            }
        }
    }

    public void setItemBackgroundRes(int i10) {
        this.f8985t = i10;
        NavigationBarItemView[] navigationBarItemViewArr = this.f8975j;
        if (navigationBarItemViewArr != null) {
            for (NavigationBarItemView navigationBarItemView : navigationBarItemViewArr) {
                navigationBarItemView.setItemBackground(i10);
            }
        }
    }

    public void setItemIconSize(int i10) {
        this.f8979n = i10;
        NavigationBarItemView[] navigationBarItemViewArr = this.f8975j;
        if (navigationBarItemViewArr != null) {
            for (NavigationBarItemView navigationBarItemView : navigationBarItemViewArr) {
                navigationBarItemView.setIconSize(i10);
            }
        }
    }

    public void setItemPaddingBottom(int i10) {
        this.f8988w = i10;
        NavigationBarItemView[] navigationBarItemViewArr = this.f8975j;
        if (navigationBarItemViewArr != null) {
            for (NavigationBarItemView navigationBarItemView : navigationBarItemViewArr) {
                navigationBarItemView.setItemPaddingBottom(i10);
            }
        }
    }

    public void setItemPaddingTop(int i10) {
        this.f8987v = i10;
        NavigationBarItemView[] navigationBarItemViewArr = this.f8975j;
        if (navigationBarItemViewArr != null) {
            for (NavigationBarItemView navigationBarItemView : navigationBarItemViewArr) {
                navigationBarItemView.setItemPaddingTop(i10);
            }
        }
    }

    public void setItemTextAppearanceActive(int i10) {
        this.f8983r = i10;
        NavigationBarItemView[] navigationBarItemViewArr = this.f8975j;
        if (navigationBarItemViewArr != null) {
            for (NavigationBarItemView navigationBarItemView : navigationBarItemViewArr) {
                navigationBarItemView.setTextAppearanceActive(i10);
                ColorStateList colorStateList = this.f8980o;
                if (colorStateList != null) {
                    navigationBarItemView.setTextColor(colorStateList);
                }
            }
        }
    }

    public void setItemTextAppearanceInactive(int i10) {
        this.f8982q = i10;
        NavigationBarItemView[] navigationBarItemViewArr = this.f8975j;
        if (navigationBarItemViewArr != null) {
            for (NavigationBarItemView navigationBarItemView : navigationBarItemViewArr) {
                navigationBarItemView.setTextAppearanceInactive(i10);
                ColorStateList colorStateList = this.f8980o;
                if (colorStateList != null) {
                    navigationBarItemView.setTextColor(colorStateList);
                }
            }
        }
    }

    public void setItemTextColor(ColorStateList colorStateList) {
        this.f8980o = colorStateList;
        NavigationBarItemView[] navigationBarItemViewArr = this.f8975j;
        if (navigationBarItemViewArr != null) {
            for (NavigationBarItemView navigationBarItemView : navigationBarItemViewArr) {
                navigationBarItemView.setTextColor(colorStateList);
            }
        }
    }

    public void setLabelVisibilityMode(int i10) {
        this.f8974i = i10;
    }

    public void setPresenter(NavigationBarPresenter navigationBarPresenter) {
        this.E = navigationBarPresenter;
    }
}
