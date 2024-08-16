package com.coui.appcompat.toolbar;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.R$attr;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.view.CollapsibleActionView;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.SubMenuBuilder;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.n0;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import c.AppCompatResources;
import com.coui.appcompat.grid.COUIResponsiveUtils;
import com.coui.appcompat.searchview.COUISearchViewAnimate;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$drawable;
import com.support.appcompat.R$id;
import com.support.appcompat.R$styleable;
import d.AnimatedStateListDrawableCompat;
import g2.COUISubMenuClickListener;
import g2.PopupListItem;
import java.util.ArrayList;
import java.util.List;
import z2.COUIChangeTextUtil;

/* loaded from: classes.dex */
public class COUIToolbar extends Toolbar {
    private int A;
    private int B;
    private int C;
    private int D;
    private int E;
    private int F;
    private CharSequence G;
    private CharSequence H;
    private int I;
    private int J;
    private boolean K;
    private boolean L;
    private Toolbar.g M;
    private d N;
    private MenuPresenter.a O;
    private MenuBuilder.a P;
    private boolean Q;
    private int R;
    private boolean S;
    private int T;
    private int[] U;
    private float V;
    private int W;

    /* renamed from: a0, reason: collision with root package name */
    private int f7974a0;

    /* renamed from: b0, reason: collision with root package name */
    private int f7975b0;

    /* renamed from: c0, reason: collision with root package name */
    private int f7976c0;

    /* renamed from: d0, reason: collision with root package name */
    private int f7977d0;

    /* renamed from: e, reason: collision with root package name */
    private final COUIRtlSpacingHelper f7978e;

    /* renamed from: e0, reason: collision with root package name */
    private int f7979e0;

    /* renamed from: f, reason: collision with root package name */
    private final ArrayList<View> f7980f;

    /* renamed from: f0, reason: collision with root package name */
    private int f7981f0;

    /* renamed from: g, reason: collision with root package name */
    private final int[] f7982g;

    /* renamed from: g0, reason: collision with root package name */
    private int f7983g0;

    /* renamed from: h, reason: collision with root package name */
    private final ActionMenuView.d f7984h;

    /* renamed from: h0, reason: collision with root package name */
    private boolean f7985h0;

    /* renamed from: i, reason: collision with root package name */
    private final int[] f7986i;

    /* renamed from: i0, reason: collision with root package name */
    private float f7987i0;

    /* renamed from: j, reason: collision with root package name */
    private final Runnable f7988j;

    /* renamed from: j0, reason: collision with root package name */
    private float f7989j0;

    /* renamed from: k, reason: collision with root package name */
    private COUIActionMenuView f7990k;

    /* renamed from: k0, reason: collision with root package name */
    private ArrayList<PopupListItem> f7991k0;

    /* renamed from: l, reason: collision with root package name */
    private TextView f7992l;

    /* renamed from: l0, reason: collision with root package name */
    private int f7993l0;

    /* renamed from: m, reason: collision with root package name */
    private TextView f7994m;

    /* renamed from: m0, reason: collision with root package name */
    private COUISubMenuClickListener f7995m0;

    /* renamed from: n, reason: collision with root package name */
    private ImageButton f7996n;

    /* renamed from: n0, reason: collision with root package name */
    private int f7997n0;

    /* renamed from: o, reason: collision with root package name */
    private ImageView f7998o;

    /* renamed from: o0, reason: collision with root package name */
    private boolean f7999o0;

    /* renamed from: p, reason: collision with root package name */
    private Drawable f8000p;

    /* renamed from: q, reason: collision with root package name */
    private CharSequence f8001q;

    /* renamed from: r, reason: collision with root package name */
    private ImageButton f8002r;

    /* renamed from: s, reason: collision with root package name */
    private View f8003s;

    /* renamed from: t, reason: collision with root package name */
    private Context f8004t;

    /* renamed from: u, reason: collision with root package name */
    private int f8005u;

    /* renamed from: v, reason: collision with root package name */
    private int f8006v;

    /* renamed from: w, reason: collision with root package name */
    private int f8007w;

    /* renamed from: x, reason: collision with root package name */
    private int f8008x;

    /* renamed from: y, reason: collision with root package name */
    private int f8009y;

    /* renamed from: z, reason: collision with root package name */
    private int f8010z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements ActionMenuView.d {
        a() {
        }

        @Override // androidx.appcompat.widget.ActionMenuView.d
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (COUIToolbar.this.M != null) {
                return COUIToolbar.this.M.onMenuItemClick(menuItem);
            }
            return false;
        }
    }

    /* loaded from: classes.dex */
    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUIToolbar.this.showOverflowMenu();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements View.OnClickListener {
        c() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            COUIToolbar.this.collapseActionView();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class d implements MenuPresenter {

        /* renamed from: e, reason: collision with root package name */
        MenuBuilder f8016e;

        /* renamed from: f, reason: collision with root package name */
        MenuItemImpl f8017f;

        private d() {
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
            if (COUIToolbar.this.f8003s instanceof CollapsibleActionView) {
                ((CollapsibleActionView) COUIToolbar.this.f8003s).onActionViewCollapsed();
            }
            COUIToolbar cOUIToolbar = COUIToolbar.this;
            cOUIToolbar.removeView(cOUIToolbar.f8003s);
            COUIToolbar cOUIToolbar2 = COUIToolbar.this;
            cOUIToolbar2.removeView(cOUIToolbar2.f8002r);
            COUIToolbar.this.f8003s = null;
            COUIToolbar.this.setChildVisibilityForExpandedActionView(false);
            this.f8017f = null;
            COUIToolbar.this.requestLayout();
            menuItemImpl.q(false);
            return true;
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
            COUIToolbar.this.ensureCollapseButtonView();
            ViewParent parent = COUIToolbar.this.f8002r.getParent();
            COUIToolbar cOUIToolbar = COUIToolbar.this;
            if (parent != cOUIToolbar) {
                cOUIToolbar.addView(cOUIToolbar.f8002r);
            }
            COUIToolbar.this.f8003s = menuItemImpl.getActionView();
            this.f8017f = menuItemImpl;
            ViewParent parent2 = COUIToolbar.this.f8003s.getParent();
            COUIToolbar cOUIToolbar2 = COUIToolbar.this;
            if (parent2 != cOUIToolbar2) {
                LayoutParams generateDefaultLayoutParams = cOUIToolbar2.generateDefaultLayoutParams();
                generateDefaultLayoutParams.f320a = 8388611 | (COUIToolbar.this.f8008x & 112);
                generateDefaultLayoutParams.f8011c = 2;
                COUIToolbar.this.f8003s.setLayoutParams(generateDefaultLayoutParams);
                COUIToolbar cOUIToolbar3 = COUIToolbar.this;
                cOUIToolbar3.addView(cOUIToolbar3.f8003s);
            }
            COUIToolbar.this.setChildVisibilityForExpandedActionView(true);
            COUIToolbar.this.requestLayout();
            menuItemImpl.q(true);
            if (COUIToolbar.this.f8003s instanceof CollapsibleActionView) {
                ((CollapsibleActionView) COUIToolbar.this.f8003s).onActionViewExpanded();
            }
            return true;
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public boolean flagActionItems() {
            return false;
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public int getId() {
            return 0;
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public void initForMenu(Context context, MenuBuilder menuBuilder) {
            MenuItemImpl menuItemImpl;
            MenuBuilder menuBuilder2 = this.f8016e;
            if (menuBuilder2 != null && (menuItemImpl = this.f8017f) != null) {
                menuBuilder2.collapseItemActionView(menuItemImpl);
            }
            this.f8016e = menuBuilder;
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public void onCloseMenu(MenuBuilder menuBuilder, boolean z10) {
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public void onRestoreInstanceState(Parcelable parcelable) {
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public Parcelable onSaveInstanceState() {
            return null;
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
            return false;
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter
        public void updateMenuView(boolean z10) {
            if (this.f8017f != null) {
                MenuBuilder menuBuilder = this.f8016e;
                boolean z11 = false;
                if (menuBuilder != null) {
                    int size = menuBuilder.size();
                    int i10 = 0;
                    while (true) {
                        if (i10 >= size) {
                            break;
                        }
                        if (this.f8016e.getItem(i10) == this.f8017f) {
                            z11 = true;
                            break;
                        }
                        i10++;
                    }
                }
                if (z11) {
                    return;
                }
                collapseItemActionView(this.f8016e, this.f8017f);
            }
        }

        /* synthetic */ d(COUIToolbar cOUIToolbar, a aVar) {
            this();
        }
    }

    public COUIToolbar(Context context) {
        this(context, null);
    }

    private void addCustomViewsWithGravity(List<View> list, int i10) {
        boolean z10 = ViewCompat.x(this) == 1;
        int childCount = getChildCount();
        int b10 = GravityCompat.b(i10, ViewCompat.x(this));
        list.clear();
        if (!z10) {
            for (int i11 = 0; i11 < childCount; i11++) {
                View childAt = getChildAt(i11);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams.f8011c == 0 && shouldLayout(childAt) && getChildHorizontalGravity(layoutParams.f320a) == b10) {
                    list.add(childAt);
                }
            }
            return;
        }
        for (int i12 = childCount - 1; i12 >= 0; i12--) {
            View childAt2 = getChildAt(i12);
            LayoutParams layoutParams2 = (LayoutParams) childAt2.getLayoutParams();
            if (layoutParams2.f8011c == 0 && shouldLayout(childAt2) && getChildHorizontalGravity(layoutParams2.f320a) == b10) {
                list.add(childAt2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ensureCollapseButtonView() {
        if (this.f8002r == null) {
            ImageButton imageButton = new ImageButton(getContext(), null, R$attr.toolbarNavigationButtonStyle);
            this.f8002r = imageButton;
            imageButton.setImageDrawable(this.f8000p);
            this.f8002r.setContentDescription(this.f8001q);
            LayoutParams generateDefaultLayoutParams = generateDefaultLayoutParams();
            generateDefaultLayoutParams.f320a = 8388611 | (this.f8008x & 112);
            generateDefaultLayoutParams.f8011c = 2;
            this.f8002r.setLayoutParams(generateDefaultLayoutParams);
            this.f8002r.setOnClickListener(new c());
        }
    }

    private void ensureLogoView() {
        if (this.f7998o == null) {
            this.f7998o = new ImageView(getContext());
        }
    }

    private void ensureMenu() {
        ensureMenuView();
        if (this.f7990k.n() == null) {
            MenuBuilder menuBuilder = (MenuBuilder) this.f7990k.getMenu();
            if (this.N == null) {
                this.N = new d(this, null);
            }
            this.f7990k.setExpandedActionViewsExclusive(true);
            menuBuilder.addMenuPresenter(this.N, this.f8004t);
        }
    }

    private void ensureMenuView() {
        if (this.f7990k == null) {
            COUIActionMenuView cOUIActionMenuView = new COUIActionMenuView(getContext());
            this.f7990k = cOUIActionMenuView;
            cOUIActionMenuView.H(this.f7991k0, this.f7993l0);
            COUISubMenuClickListener cOUISubMenuClickListener = this.f7995m0;
            if (cOUISubMenuClickListener != null) {
                this.f7990k.setSubMenuClickListener(cOUISubMenuClickListener);
            }
            this.f7990k.setId(R$id.coui_toolbar_more_view);
            this.f7990k.setPopupTheme(this.f8005u);
            this.f7990k.setOnMenuItemClickListener(this.f7984h);
            this.f7990k.o(this.O, this.P);
            LayoutParams generateDefaultLayoutParams = generateDefaultLayoutParams();
            if (this.S) {
                ((ViewGroup.MarginLayoutParams) generateDefaultLayoutParams).width = -1;
            } else {
                ((ViewGroup.MarginLayoutParams) generateDefaultLayoutParams).width = -2;
            }
            generateDefaultLayoutParams.f320a = 8388613 | (this.f8008x & 112);
            this.f7990k.setLayoutParams(generateDefaultLayoutParams);
            h(this.f7990k);
        }
    }

    private void ensureNavButtonView() {
        if (this.f7996n == null) {
            ImageButton imageButton = new ImageButton(getContext(), null, R$attr.toolbarNavigationButtonStyle);
            this.f7996n = imageButton;
            imageButton.setId(R$id.coui_toolbar_back_view);
            LayoutParams generateDefaultLayoutParams = generateDefaultLayoutParams();
            generateDefaultLayoutParams.f320a = 8388611 | (this.f8008x & 112);
            this.f7996n.setLayoutParams(generateDefaultLayoutParams);
            this.f7996n.setBackgroundResource(R$drawable.coui_toolbar_menu_bg);
        }
    }

    private int getChildHorizontalGravity(int i10) {
        int x10 = ViewCompat.x(this);
        int b10 = GravityCompat.b(i10, x10) & 7;
        return (b10 == 1 || b10 == 3 || b10 == 5) ? b10 : x10 == 1 ? 5 : 3;
    }

    private int getChildTop(View view, int i10) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int measuredHeight = view.getMeasuredHeight();
        int i11 = i10 > 0 ? (measuredHeight - i10) / 2 : 0;
        int childVerticalGravity = getChildVerticalGravity(layoutParams.f320a);
        if (childVerticalGravity == 48) {
            return getPaddingTop() - i11;
        }
        if (childVerticalGravity != 80) {
            int paddingTop = getPaddingTop();
            int paddingBottom = getPaddingBottom();
            int height = getHeight();
            int i12 = (((height - paddingTop) - paddingBottom) - measuredHeight) / 2;
            int i13 = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
            if (i12 < i13) {
                i12 = i13;
            } else {
                int i14 = (((height - paddingBottom) - measuredHeight) - i12) - paddingTop;
                int i15 = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
                if (i14 < i15) {
                    i12 = Math.max(0, i12 - (i15 - i14));
                }
            }
            return paddingTop + i12;
        }
        return (((getHeight() - getPaddingBottom()) - measuredHeight) - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin) - i11;
    }

    private int getChildVerticalGravity(int i10) {
        int i11 = i10 & 112;
        return (i11 == 16 || i11 == 48 || i11 == 80) ? i11 : this.F & 112;
    }

    private int getHorizontalMargins(View view) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return MarginLayoutParamsCompat.b(marginLayoutParams) + MarginLayoutParamsCompat.a(marginLayoutParams);
    }

    private int getMinimumHeightCompat() {
        return ViewCompat.z(this);
    }

    private int getVerticalMargins(View view) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
    }

    private int getViewListMeasuredWidth(List<View> list, int[] iArr) {
        int i10 = iArr[0];
        int i11 = iArr[1];
        int size = list.size();
        int i12 = 0;
        int i13 = 0;
        while (i12 < size) {
            View view = list.get(i12);
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            int i14 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin - i10;
            int i15 = ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin - i11;
            int max = Math.max(0, i14);
            int max2 = Math.max(0, i15);
            int max3 = Math.max(0, -i14);
            int max4 = Math.max(0, -i15);
            i13 += max + view.getMeasuredWidth() + max2;
            i12++;
            i11 = max4;
            i10 = max3;
        }
        return i13;
    }

    private void h(View view) {
        LayoutParams layoutParams;
        ViewGroup.LayoutParams layoutParams2 = view.getLayoutParams();
        if (layoutParams2 == null) {
            layoutParams = generateDefaultLayoutParams();
        } else if (!checkLayoutParams(layoutParams2)) {
            layoutParams = generateLayoutParams(layoutParams2);
        } else {
            layoutParams = (LayoutParams) layoutParams2;
        }
        layoutParams.f8011c = 1;
        addView(view, layoutParams);
    }

    private void i(int[] iArr) {
        int measuredWidth;
        int i10;
        boolean z10 = ViewCompat.x(this) == 1;
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.coui_actionbar_menuitemview_item_spacing);
        iArr[0] = Math.max(this.f7978e.b(), getPaddingLeft());
        iArr[1] = getMeasuredWidth() - Math.max(this.f7978e.c(), getPaddingRight());
        if (!shouldLayout(this.f7990k) || this.f7990k.getChildCount() == 0) {
            return;
        }
        if (this.f7990k.getChildCount() == 1) {
            i10 = this.f7990k.getChildAt(0).getMeasuredWidth() + dimensionPixelSize + 0;
            measuredWidth = 0;
        } else {
            measuredWidth = this.f7990k.getChildAt(0).getMeasuredWidth() + dimensionPixelSize + 0;
            int i11 = 0;
            for (int i12 = 1; i12 < this.f7990k.getChildCount(); i12++) {
                i11 += this.f7990k.getChildAt(i12).getMeasuredWidth() + dimensionPixelSize;
            }
            i10 = i11;
        }
        if (z10) {
            iArr[0] = iArr[0] + i10;
            iArr[1] = iArr[1] - measuredWidth;
        } else {
            iArr[0] = iArr[0] + measuredWidth;
            iArr[1] = iArr[1] - i10;
        }
    }

    private void j(MenuBuilder menuBuilder, ImageButton imageButton, boolean z10, int i10) {
        if (menuBuilder == null && imageButton == null) {
            return;
        }
        boolean shouldLayout = shouldLayout(this.f7996n);
        if (getChildCount() > 0) {
            boolean z11 = getChildAt(0) instanceof COUISearchViewAnimate;
        }
        if (COUIResponsiveUtils.i(getContext(), View.MeasureSpec.getSize(i10))) {
            this.W = getContext().getResources().getDimensionPixelOffset(shouldLayout ? R$dimen.toolbar_normal_menu_padding_left_compat : R$dimen.toolbar_normal_padding_left_compat);
            this.f7974a0 = getContext().getResources().getDimensionPixelOffset(R$dimen.toolbar_normal_menu_padding_right_compat);
        } else if (COUIResponsiveUtils.h(getContext(), View.MeasureSpec.getSize(i10))) {
            this.W = getContext().getResources().getDimensionPixelOffset(shouldLayout ? R$dimen.toolbar_normal_menu_padding_left_medium : R$dimen.toolbar_normal_padding_left_medium);
            this.f7974a0 = getContext().getResources().getDimensionPixelOffset(R$dimen.toolbar_normal_menu_padding_right_medium);
        } else if (COUIResponsiveUtils.g(getContext(), View.MeasureSpec.getSize(i10))) {
            this.W = getContext().getResources().getDimensionPixelOffset(shouldLayout ? R$dimen.toolbar_normal_menu_padding_left_expanded : R$dimen.toolbar_normal_padding_left_expanded);
            this.f7974a0 = getContext().getResources().getDimensionPixelOffset(R$dimen.toolbar_normal_menu_padding_right_expanded);
        }
        if (menuBuilder == null || menuBuilder.getNonActionItems().isEmpty()) {
            if (z10) {
                setPadding(this.S ? this.f7976c0 : this.f7974a0, getPaddingTop(), this.S ? this.f7975b0 : this.W, getPaddingBottom());
                return;
            } else {
                setPadding(this.S ? this.f7975b0 : this.W, getPaddingTop(), this.S ? this.f7976c0 : this.f7974a0, getPaddingBottom());
                return;
            }
        }
        if (z10) {
            setPadding(this.S ? this.f7976c0 : this.f7974a0, getPaddingTop(), this.S ? this.f7975b0 : this.W, getPaddingBottom());
        } else {
            setPadding(this.S ? this.f7975b0 : this.W, getPaddingTop(), this.S ? this.f7976c0 : this.f7974a0, getPaddingBottom());
        }
    }

    private int layoutChildLeft(View view, int i10, int[] iArr, int i11) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int i12 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin - iArr[0];
        int max = i10 + Math.max(0, i12);
        iArr[0] = Math.max(0, -i12);
        int childTop = getChildTop(view, i11);
        int measuredWidth = view.getMeasuredWidth();
        view.layout(max, childTop, max + measuredWidth, view.getMeasuredHeight() + childTop);
        return max + measuredWidth + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
    }

    private int layoutChildRight(View view, int i10, int[] iArr, int i11) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int i12 = ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin - iArr[1];
        int max = i10 - Math.max(0, i12);
        iArr[1] = Math.max(0, -i12);
        int childTop = getChildTop(view, i11);
        int measuredWidth = view.getMeasuredWidth();
        view.layout(max - measuredWidth, childTop, max, view.getMeasuredHeight() + childTop);
        return max - (measuredWidth + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin);
    }

    private int measureChildCollapseMargins(View view, int i10, int i11, int i12, int i13, int[] iArr) {
        int childMeasureSpec;
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        int i14 = marginLayoutParams.leftMargin - iArr[0];
        int i15 = marginLayoutParams.rightMargin - iArr[1];
        int max = Math.max(0, i14) + Math.max(0, i15);
        iArr[0] = Math.max(0, -i14);
        iArr[1] = Math.max(0, -i15);
        boolean z10 = (marginLayoutParams instanceof LayoutParams) && ((LayoutParams) marginLayoutParams).f8012d && this.f7985h0;
        if (z10) {
            childMeasureSpec = ViewGroup.getChildMeasureSpec(i10, max, marginLayoutParams.width);
        } else {
            childMeasureSpec = ViewGroup.getChildMeasureSpec(i10, getPaddingLeft() + getPaddingRight() + max + i11, marginLayoutParams.width);
        }
        int childMeasureSpec2 = ViewGroup.getChildMeasureSpec(i12, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + i13, marginLayoutParams.height);
        view.measure(childMeasureSpec, childMeasureSpec2);
        if (z10) {
            COUIActionMenuView cOUIActionMenuView = this.f7990k;
            if (cOUIActionMenuView != null && cOUIActionMenuView.getVisibility() != 8) {
                view.measure(ViewGroup.getChildMeasureSpec(i10, max, ((view.getMeasuredWidth() - this.f7990k.getMeasuredWidth()) - (this.f7990k.getMeasuredWidth() != 0 ? getPaddingEnd() : 0)) - this.f7979e0), childMeasureSpec2);
            }
            return max;
        }
        return view.getMeasuredWidth() + max;
    }

    private void measureChildConstrained(View view, int i10, int i11, int i12, int i13, int i14) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        int childMeasureSpec = ViewGroup.getChildMeasureSpec(i10, getPaddingLeft() + getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + i11, marginLayoutParams.width);
        int childMeasureSpec2 = ViewGroup.getChildMeasureSpec(i12, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + i13, marginLayoutParams.height);
        int mode = View.MeasureSpec.getMode(childMeasureSpec2);
        if (mode != 1073741824 && i14 >= 0) {
            if (mode != 0) {
                i14 = Math.min(View.MeasureSpec.getSize(childMeasureSpec2), i14);
            }
            childMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(i14, 1073741824);
        }
        view.measure(childMeasureSpec, childMeasureSpec2);
    }

    private void o(View view) {
        if (((LayoutParams) view.getLayoutParams()).f8011c == 2 || view == this.f7990k) {
            return;
        }
        view.setVisibility(this.f8003s != null ? 8 : 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setChildVisibilityForExpandedActionView(boolean z10) {
        int childCount = getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = getChildAt(i10);
            if (((LayoutParams) childAt.getLayoutParams()).f8011c != 2 && childAt != this.f7990k) {
                childAt.setVisibility(z10 ? 8 : 0);
            }
        }
    }

    private boolean shouldCollapse() {
        if (!this.Q) {
            return false;
        }
        int childCount = getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = getChildAt(i10);
            if (shouldLayout(childAt) && childAt.getMeasuredWidth() > 0 && childAt.getMeasuredHeight() > 0) {
                return false;
            }
        }
        return true;
    }

    private boolean shouldLayout(View view) {
        return (view == null || view.getParent() != this || view.getVisibility() == 8) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup
    public boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return super.checkLayoutParams(layoutParams) && (layoutParams instanceof LayoutParams);
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void collapseActionView() {
        d dVar = this.N;
        MenuItemImpl menuItemImpl = dVar == null ? null : dVar.f8017f;
        if (menuItemImpl != null) {
            menuItemImpl.collapseActionView();
        }
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void dismissPopupMenus() {
        COUIActionMenuView cOUIActionMenuView = this.f7990k;
        if (cOUIActionMenuView != null) {
            cOUIActionMenuView.b();
        }
    }

    @Override // androidx.appcompat.widget.Toolbar
    public int getContentInsetEnd() {
        return this.f7978e.a();
    }

    @Override // androidx.appcompat.widget.Toolbar
    public int getContentInsetLeft() {
        return this.f7978e.b();
    }

    @Override // androidx.appcompat.widget.Toolbar
    public int getContentInsetRight() {
        return this.f7978e.c();
    }

    @Override // androidx.appcompat.widget.Toolbar
    public int getContentInsetStart() {
        return this.f7978e.d();
    }

    public boolean getIsTitleCenterStyle() {
        return this.S;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public Drawable getLogo() {
        ImageView imageView = this.f7998o;
        if (imageView != null) {
            return imageView.getDrawable();
        }
        return null;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public CharSequence getLogoDescription() {
        ImageView imageView = this.f7998o;
        if (imageView != null) {
            return imageView.getContentDescription();
        }
        return null;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public Menu getMenu() {
        ensureMenu();
        return this.f7990k.getMenu();
    }

    @Override // androidx.appcompat.widget.Toolbar
    public CharSequence getNavigationContentDescription() {
        ImageButton imageButton = this.f7996n;
        if (imageButton != null) {
            return imageButton.getContentDescription();
        }
        return null;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public Drawable getNavigationIcon() {
        ImageButton imageButton = this.f7996n;
        if (imageButton != null) {
            return imageButton.getDrawable();
        }
        return null;
    }

    public View getOverFlowMenuButton() {
        COUIActionMenuView cOUIActionMenuView = this.f7990k;
        if (cOUIActionMenuView != null) {
            return cOUIActionMenuView.getOverFlowMenuButton();
        }
        return null;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public Drawable getOverflowIcon() {
        ensureMenu();
        return this.f7990k.getOverflowIcon();
    }

    @Override // androidx.appcompat.widget.Toolbar
    public int getPopupTheme() {
        return this.f8005u;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public CharSequence getSubtitle() {
        return this.H;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public CharSequence getTitle() {
        return this.G;
    }

    public View getTitleView() {
        return this.f7992l;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void inflateMenu(int i10) {
        super.inflateMenu(i10);
        this.f7997n0 = i10;
        COUIActionMenuView cOUIActionMenuView = this.f7990k;
        if (cOUIActionMenuView instanceof COUIActionMenuView) {
            cOUIActionMenuView.B();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup
    /* renamed from: k, reason: merged with bridge method [inline-methods] */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup
    /* renamed from: l, reason: merged with bridge method [inline-methods] */
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup
    /* renamed from: m, reason: merged with bridge method [inline-methods] */
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LayoutParams) {
            return new LayoutParams((LayoutParams) layoutParams);
        }
        if (layoutParams instanceof ActionBar.LayoutParams) {
            return new LayoutParams((ActionBar.LayoutParams) layoutParams);
        }
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams) layoutParams);
        }
        return new LayoutParams(layoutParams);
    }

    public void n(View view, LayoutParams layoutParams) {
        if (view == null) {
            this.f7985h0 = false;
            return;
        }
        this.f7985h0 = true;
        LayoutParams layoutParams2 = new LayoutParams(layoutParams);
        layoutParams2.f8012d = true;
        layoutParams2.f8011c = 0;
        addView(view, 0, layoutParams2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this.f7988j);
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        int a10 = MotionEventCompat.a(motionEvent);
        if (a10 == 9) {
            this.L = false;
        }
        if (!this.L) {
            boolean onHoverEvent = super.onHoverEvent(motionEvent);
            if (a10 == 9 && !onHoverEvent) {
                this.L = true;
            }
        }
        if (a10 == 10 || a10 == 3) {
            this.L = false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:103:0x0166  */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0055  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x01b7  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x027c  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x006c  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x01a4  */
    /* JADX WARN: Removed duplicated region for block: B:175:0x0133  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x012c  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x0116  */
    /* JADX WARN: Removed duplicated region for block: B:178:0x00f9  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x00ac  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00c3  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00e0  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00fe  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0384  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x03ed  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0463  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0473 A[LOOP:2: B:78:0x0471->B:79:0x0473, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0433  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x03c9  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0129  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0130  */
    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        int i14;
        int i15;
        boolean shouldLayout;
        boolean shouldLayout2;
        int i16;
        int i17;
        int i18;
        int i19;
        boolean z11;
        int i20;
        int i21;
        int i22;
        int i23;
        int paddingTop;
        int max;
        int i24;
        int i25;
        int max2;
        int i26;
        int i27;
        int i28;
        int i29;
        int i30;
        int i31;
        int size;
        int i32;
        int i33;
        int layoutChildRight;
        int i34;
        boolean z12 = ViewCompat.x(this) == 1;
        int width = getWidth();
        int height = getHeight();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop2 = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int i35 = width - paddingRight;
        int[] iArr = this.f7982g;
        iArr[0] = 0;
        iArr[1] = 0;
        int minimumHeightCompat = getMinimumHeightCompat();
        if (!shouldLayout(this.f7996n)) {
            i14 = paddingLeft;
        } else {
            if (z12) {
                i15 = layoutChildRight(this.f7996n, i35, iArr, minimumHeightCompat);
                i14 = paddingLeft;
                if (shouldLayout(this.f8002r)) {
                    if (z12) {
                        i15 = layoutChildRight(this.f8002r, i15, iArr, minimumHeightCompat);
                    } else {
                        i14 = layoutChildLeft(this.f8002r, i14, iArr, minimumHeightCompat);
                    }
                }
                if (shouldLayout(this.f7990k)) {
                    if (z12) {
                        i14 = layoutChildLeft(this.f7990k, i14, iArr, minimumHeightCompat);
                    } else {
                        i15 = layoutChildRight(this.f7990k, i15, iArr, minimumHeightCompat);
                    }
                }
                iArr[0] = Math.max(0, getContentInsetLeft() - i14);
                iArr[1] = Math.max(0, getContentInsetRight() - (i35 - i15));
                int max3 = Math.max(i14, getContentInsetLeft());
                int min = Math.min(i15, width - getContentInsetRight());
                if (shouldLayout(this.f8003s)) {
                    if (z12) {
                        min = layoutChildRight(this.f8003s, min, iArr, minimumHeightCompat);
                    } else {
                        max3 = layoutChildLeft(this.f8003s, max3, iArr, minimumHeightCompat);
                    }
                }
                if (shouldLayout(this.f7998o)) {
                    if (z12) {
                        min = layoutChildRight(this.f7998o, min, iArr, minimumHeightCompat);
                    } else {
                        max3 = layoutChildLeft(this.f7998o, max3, iArr, minimumHeightCompat);
                    }
                }
                shouldLayout = shouldLayout(this.f7992l);
                shouldLayout2 = shouldLayout(this.f7994m);
                if (shouldLayout) {
                    i16 = paddingRight;
                    i17 = 0;
                } else {
                    LayoutParams layoutParams = (LayoutParams) this.f7992l.getLayoutParams();
                    i16 = paddingRight;
                    i17 = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + this.f7992l.getMeasuredHeight() + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin + 0;
                }
                if (shouldLayout2) {
                    i18 = paddingLeft;
                } else {
                    LayoutParams layoutParams2 = (LayoutParams) this.f7994m.getLayoutParams();
                    i18 = paddingLeft;
                    i17 += ((ViewGroup.MarginLayoutParams) layoutParams2).topMargin + this.f7994m.getMeasuredHeight() + ((ViewGroup.MarginLayoutParams) layoutParams2).bottomMargin;
                }
                if (!shouldLayout || shouldLayout2) {
                    TextView textView = !shouldLayout ? this.f7992l : this.f7994m;
                    TextView textView2 = !shouldLayout2 ? this.f7994m : this.f7992l;
                    LayoutParams layoutParams3 = (LayoutParams) textView.getLayoutParams();
                    LayoutParams layoutParams4 = (LayoutParams) textView2.getLayoutParams();
                    if ((shouldLayout || this.f7992l.getMeasuredWidth() <= 0) && (!shouldLayout2 || this.f7994m.getMeasuredWidth() <= 0)) {
                        i19 = width;
                        z11 = false;
                    } else {
                        i19 = width;
                        z11 = true;
                    }
                    i20 = this.F & 112;
                    i21 = minimumHeightCompat;
                    if (i20 != 48) {
                        i22 = max3;
                        i23 = min;
                        paddingTop = getPaddingTop() + ((ViewGroup.MarginLayoutParams) layoutParams3).topMargin + this.B;
                    } else if (i20 != 80) {
                        int i36 = (((height - paddingTop2) - paddingBottom) - i17) / 2;
                        int i37 = ((ViewGroup.MarginLayoutParams) layoutParams3).topMargin;
                        i22 = max3;
                        int i38 = this.B;
                        i23 = min;
                        if (i36 < i37 + i38) {
                            i36 = i37 + i38;
                        } else {
                            int i39 = (((height - paddingBottom) - i17) - i36) - paddingTop2;
                            int i40 = ((ViewGroup.MarginLayoutParams) layoutParams3).bottomMargin;
                            int i41 = this.C;
                            if (i39 < i40 + i41) {
                                i36 = Math.max(0, i36 - ((((ViewGroup.MarginLayoutParams) layoutParams4).bottomMargin + i41) - i39));
                            }
                        }
                        paddingTop = paddingTop2 + i36;
                    } else {
                        i22 = max3;
                        i23 = min;
                        paddingTop = (((height - paddingBottom) - ((ViewGroup.MarginLayoutParams) layoutParams4).bottomMargin) - this.C) - i17;
                    }
                    if (this.S) {
                        if (z12) {
                            int i42 = ((z11 ? this.f8010z : 0) - iArr[1]) + (shouldLayout(this.f7996n) ? this.f7981f0 : 0);
                            max2 = i23 - Math.max(0, i42);
                            iArr[1] = Math.max(0, -i42);
                            if (shouldLayout) {
                                LayoutParams layoutParams5 = (LayoutParams) this.f7992l.getLayoutParams();
                                int measuredWidth = max2 - this.f7992l.getMeasuredWidth();
                                int measuredHeight = this.f7992l.getMeasuredHeight() + paddingTop;
                                this.f7992l.layout(measuredWidth, paddingTop, max2, measuredHeight);
                                i26 = measuredWidth - this.A;
                                paddingTop = measuredHeight + ((ViewGroup.MarginLayoutParams) layoutParams5).bottomMargin;
                            } else {
                                i26 = max2;
                            }
                            if (shouldLayout2) {
                                int i43 = paddingTop + ((ViewGroup.MarginLayoutParams) ((LayoutParams) this.f7994m.getLayoutParams())).topMargin;
                                this.f7994m.layout(max2 - this.f7994m.getMeasuredWidth(), i43, max2, this.f7994m.getMeasuredHeight() + i43);
                                i27 = max2 - this.A;
                            } else {
                                i27 = max2;
                            }
                            if (z11) {
                                max2 = Math.min(i26, i27);
                            }
                            max = i22;
                            addCustomViewsWithGravity(this.f7980f, 3);
                            int size2 = this.f7980f.size();
                            if (this.f7985h0) {
                                int i44 = 0;
                                while (i44 < size2) {
                                    ViewGroup.LayoutParams layoutParams6 = this.f7980f.get(i44).getLayoutParams();
                                    if (layoutParams6 != null && (layoutParams6 instanceof LayoutParams) && ((LayoutParams) layoutParams6).f8012d) {
                                        max = layoutChildLeft(this.f7980f.get(i44), 0, this.f7986i, 0);
                                        i34 = i21;
                                    } else {
                                        i34 = i21;
                                        max = layoutChildLeft(this.f7980f.get(i44), max, iArr, i34);
                                    }
                                    i44++;
                                    i21 = i34;
                                }
                                i28 = i21;
                            } else {
                                i28 = i21;
                                for (int i45 = 0; i45 < size2; i45++) {
                                    max = layoutChildLeft(this.f7980f.get(i45), max, iArr, i28);
                                }
                            }
                            addCustomViewsWithGravity(this.f7980f, 5);
                            int size3 = this.f7980f.size();
                            if (this.f7985h0) {
                                int i46 = 0;
                                while (i46 < size3) {
                                    ViewGroup.LayoutParams layoutParams7 = this.f7980f.get(i46).getLayoutParams();
                                    if (layoutParams7 != null && (layoutParams7 instanceof LayoutParams) && ((LayoutParams) layoutParams7).f8012d) {
                                        i33 = i19;
                                        layoutChildRight = layoutChildRight(this.f7980f.get(i46), i33, this.f7986i, 0);
                                    } else {
                                        i33 = i19;
                                        layoutChildRight = layoutChildRight(this.f7980f.get(i46), max2, iArr, i28);
                                    }
                                    max2 = layoutChildRight;
                                    i46++;
                                    i19 = i33;
                                }
                                i29 = i19;
                                i30 = 0;
                            } else {
                                i29 = i19;
                                i30 = 0;
                                for (int i47 = 0; i47 < size3; i47++) {
                                    max2 = layoutChildRight(this.f7980f.get(i47), max2, iArr, i28);
                                }
                            }
                            addCustomViewsWithGravity(this.f7980f, 1);
                            int viewListMeasuredWidth = getViewListMeasuredWidth(this.f7980f, iArr);
                            i31 = (i18 + (((i29 - i18) - i16) / 2)) - (viewListMeasuredWidth / 2);
                            int i48 = viewListMeasuredWidth + i31;
                            if (i31 >= max) {
                                max = i48 > max2 ? i31 - (i48 - max2) : i31;
                            }
                            size = this.f7980f.size();
                            for (i32 = i30; i32 < size; i32++) {
                                max = layoutChildLeft(this.f7980f.get(i32), max, iArr, i28);
                            }
                            this.f7980f.clear();
                            return;
                        }
                        int i49 = ((z11 ? this.f8010z : 0) - iArr[0]) + (shouldLayout(this.f7996n) ? this.f7981f0 : 0);
                        max = i22 + Math.max(0, i49);
                        iArr[0] = Math.max(0, -i49);
                        if (shouldLayout) {
                            LayoutParams layoutParams8 = (LayoutParams) this.f7992l.getLayoutParams();
                            int measuredWidth2 = this.f7992l.getMeasuredWidth() + max;
                            int measuredHeight2 = this.f7992l.getMeasuredHeight() + paddingTop;
                            this.f7992l.layout(max, paddingTop, measuredWidth2, measuredHeight2);
                            i24 = measuredWidth2 + this.A;
                            paddingTop = measuredHeight2 + ((ViewGroup.MarginLayoutParams) layoutParams8).bottomMargin;
                        } else {
                            i24 = max;
                        }
                        if (shouldLayout2) {
                            int i50 = paddingTop + ((ViewGroup.MarginLayoutParams) ((LayoutParams) this.f7994m.getLayoutParams())).topMargin;
                            int measuredWidth3 = this.f7994m.getMeasuredWidth() + max;
                            this.f7994m.layout(max, i50, measuredWidth3, this.f7994m.getMeasuredHeight() + i50);
                            i25 = measuredWidth3 + this.A;
                        } else {
                            i25 = max;
                        }
                        if (z11) {
                            max = Math.max(i24, i25);
                        }
                        max2 = i23;
                        addCustomViewsWithGravity(this.f7980f, 3);
                        int size22 = this.f7980f.size();
                        if (this.f7985h0) {
                        }
                        addCustomViewsWithGravity(this.f7980f, 5);
                        int size32 = this.f7980f.size();
                        if (this.f7985h0) {
                        }
                        addCustomViewsWithGravity(this.f7980f, 1);
                        int viewListMeasuredWidth2 = getViewListMeasuredWidth(this.f7980f, iArr);
                        i31 = (i18 + (((i29 - i18) - i16) / 2)) - (viewListMeasuredWidth2 / 2);
                        int i482 = viewListMeasuredWidth2 + i31;
                        if (i31 >= max) {
                        }
                        size = this.f7980f.size();
                        while (i32 < size) {
                        }
                        this.f7980f.clear();
                        return;
                    }
                    int max4 = Math.max(shouldLayout ? this.f7992l.getMeasuredWidth() : 0, shouldLayout2 ? this.f7994m.getMeasuredWidth() : 0);
                    int width2 = getWidth() - (Math.max(this.U[0], getWidth() - this.U[1]) * 2);
                    int[] iArr2 = this.U;
                    int i51 = iArr2[1] - iArr2[0];
                    if (shouldLayout) {
                        LayoutParams layoutParams9 = (LayoutParams) this.f7992l.getLayoutParams();
                        int measuredWidth4 = this.f7992l.getMeasuredWidth();
                        int width3 = (getWidth() - measuredWidth4) / 2;
                        int i52 = width3 + measuredWidth4;
                        int measuredHeight3 = this.f7992l.getMeasuredHeight() + paddingTop;
                        if (width2 < max4) {
                            if (measuredWidth4 >= i51) {
                                int[] iArr3 = this.U;
                                width3 = iArr3[0];
                                i52 = iArr3[1];
                            } else {
                                width3 = this.U[0] + ((i51 - measuredWidth4) / 2);
                                i52 = width3 + measuredWidth4;
                            }
                        }
                        this.f7992l.layout(width3, paddingTop, i52, measuredHeight3);
                        paddingTop = measuredHeight3 + ((ViewGroup.MarginLayoutParams) layoutParams9).bottomMargin;
                    }
                    if (shouldLayout2) {
                        int i53 = paddingTop + ((ViewGroup.MarginLayoutParams) ((LayoutParams) this.f7994m.getLayoutParams())).topMargin;
                        int measuredWidth5 = this.f7994m.getMeasuredWidth();
                        int width4 = (getWidth() - measuredWidth5) / 2;
                        int i54 = width4 + measuredWidth5;
                        int measuredHeight4 = this.f7994m.getMeasuredHeight() + i53;
                        if (width2 < max4) {
                            if (measuredWidth5 >= i51) {
                                int[] iArr4 = this.U;
                                width4 = iArr4[0];
                                i54 = iArr4[1];
                            } else {
                                width4 = this.U[0] + ((i51 - measuredWidth5) / 2);
                                i54 = width4 + measuredWidth5;
                            }
                        }
                        this.f7994m.layout(width4, i53, i54, measuredHeight4);
                    }
                } else {
                    i19 = width;
                    i22 = max3;
                    i21 = minimumHeightCompat;
                    i23 = min;
                }
                max = i22;
                max2 = i23;
                addCustomViewsWithGravity(this.f7980f, 3);
                int size222 = this.f7980f.size();
                if (this.f7985h0) {
                }
                addCustomViewsWithGravity(this.f7980f, 5);
                int size322 = this.f7980f.size();
                if (this.f7985h0) {
                }
                addCustomViewsWithGravity(this.f7980f, 1);
                int viewListMeasuredWidth22 = getViewListMeasuredWidth(this.f7980f, iArr);
                i31 = (i18 + (((i29 - i18) - i16) / 2)) - (viewListMeasuredWidth22 / 2);
                int i4822 = viewListMeasuredWidth22 + i31;
                if (i31 >= max) {
                }
                size = this.f7980f.size();
                while (i32 < size) {
                }
                this.f7980f.clear();
                return;
            }
            i14 = layoutChildLeft(this.f7996n, paddingLeft, iArr, minimumHeightCompat);
        }
        i15 = i35;
        if (shouldLayout(this.f8002r)) {
        }
        if (shouldLayout(this.f7990k)) {
        }
        iArr[0] = Math.max(0, getContentInsetLeft() - i14);
        iArr[1] = Math.max(0, getContentInsetRight() - (i35 - i15));
        int max32 = Math.max(i14, getContentInsetLeft());
        int min2 = Math.min(i15, width - getContentInsetRight());
        if (shouldLayout(this.f8003s)) {
        }
        if (shouldLayout(this.f7998o)) {
        }
        shouldLayout = shouldLayout(this.f7992l);
        shouldLayout2 = shouldLayout(this.f7994m);
        if (shouldLayout) {
        }
        if (shouldLayout2) {
        }
        if (shouldLayout) {
        }
        if (!shouldLayout) {
        }
        if (!shouldLayout2) {
        }
        LayoutParams layoutParams32 = (LayoutParams) textView.getLayoutParams();
        LayoutParams layoutParams42 = (LayoutParams) textView2.getLayoutParams();
        if (shouldLayout) {
        }
        i19 = width;
        z11 = false;
        i20 = this.F & 112;
        i21 = minimumHeightCompat;
        if (i20 != 48) {
        }
        if (this.S) {
        }
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.View
    protected void onMeasure(int i10, int i11) {
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        MenuBuilder menuBuilder;
        int i17;
        int i18;
        int i19;
        int i20;
        int i21;
        int i22;
        int i23;
        int i24;
        int i25;
        int i26;
        int i27;
        int i28;
        int i29;
        boolean z10 = ViewCompat.x(this) == 1;
        if (this.S) {
            int[] iArr = this.f7982g;
            boolean b10 = n0.b(this);
            int i30 = !b10 ? 1 : 0;
            int contentInsetStart = getContentInsetStart();
            int max = Math.max(contentInsetStart, 0) + 0;
            iArr[b10 ? 1 : 0] = Math.max(0, contentInsetStart - 0);
            if (shouldLayout(this.f7990k)) {
                j((MenuBuilder) this.f7990k.getMenu(), null, z10, i10);
                measureChildConstrained(this.f7990k, i10, 0, i11, 0, this.f8009y);
                i21 = this.f7990k.getMeasuredWidth() + getHorizontalMargins(this.f7990k);
                i22 = Math.max(0, this.f7990k.getMeasuredHeight() + getVerticalMargins(this.f7990k));
                i23 = View.combineMeasuredStates(0, ViewCompat.y(this.f7990k));
            } else {
                i21 = 0;
                i22 = 0;
                i23 = 0;
            }
            int contentInsetEnd = getContentInsetEnd();
            int max2 = max + Math.max(contentInsetEnd, i21);
            iArr[i30] = Math.max(0, contentInsetEnd - i21);
            if (shouldLayout(this.f8003s)) {
                max2 += measureChildCollapseMargins(this.f8003s, i10, max2, i11, 0, iArr);
                i22 = Math.max(i22, this.f8003s.getMeasuredHeight() + getVerticalMargins(this.f8003s));
                i23 = View.combineMeasuredStates(i23, ViewCompat.y(this.f8003s));
            }
            int childCount = getChildCount();
            int i31 = 0;
            while (i31 < childCount) {
                View childAt = getChildAt(i31);
                if (((LayoutParams) childAt.getLayoutParams()).f8011c == 0 && shouldLayout(childAt)) {
                    i28 = i31;
                    i29 = childCount;
                    max2 += measureChildCollapseMargins(childAt, i10, max2, i11, 0, iArr);
                    i22 = Math.max(i22, childAt.getMeasuredHeight() + getVerticalMargins(childAt));
                    i23 = View.combineMeasuredStates(i23, ViewCompat.y(childAt));
                } else {
                    i28 = i31;
                    i29 = childCount;
                }
                i31 = i28 + 1;
                childCount = i29;
            }
            int i32 = this.B + this.C;
            if (shouldLayout(this.f7992l)) {
                this.f7992l.getLayoutParams().width = -2;
                this.f7992l.setTextSize(0, this.V);
                i24 = -2;
                measureChildCollapseMargins(this.f7992l, i10, 0, i11, i32, iArr);
                int measuredWidth = this.f7992l.getMeasuredWidth() + getHorizontalMargins(this.f7992l);
                int measuredHeight = this.f7992l.getMeasuredHeight() + getVerticalMargins(this.f7992l);
                i23 = View.combineMeasuredStates(i23, ViewCompat.y(this.f7992l));
                i26 = measuredWidth;
                i25 = measuredHeight;
            } else {
                i24 = -2;
                i25 = 0;
                i26 = 0;
            }
            if (shouldLayout(this.f7994m)) {
                this.f7994m.getLayoutParams().width = i24;
                i27 = i25;
                i26 = Math.max(i26, measureChildCollapseMargins(this.f7994m, i10, 0, i11, i25 + i32, iArr));
                i23 = View.combineMeasuredStates(i23, ViewCompat.y(this.f7994m));
            } else {
                i27 = i25;
            }
            int max3 = Math.max(i22, i27);
            int paddingLeft = max2 + i26 + getPaddingLeft() + getPaddingRight();
            int paddingTop = max3 + getPaddingTop() + getPaddingBottom();
            int i02 = ViewCompat.i0(Math.max(paddingLeft, getSuggestedMinimumWidth()), i10, i23 & (-16777216));
            int i03 = ViewCompat.i0(Math.max(paddingTop, getSuggestedMinimumHeight()), i11, i23 << 16);
            if (shouldCollapse()) {
                i03 = 0;
            }
            setMeasuredDimension(i02, i03);
            i(this.U);
            int[] iArr2 = this.U;
            int i33 = iArr2[1] - iArr2[0];
            if (shouldLayout(this.f7992l)) {
                int measuredWidth2 = this.f7992l.getMeasuredWidth();
                int[] iArr3 = this.U;
                if (measuredWidth2 > iArr3[1] - iArr3[0]) {
                    measureChildCollapseMargins(this.f7992l, View.MeasureSpec.makeMeasureSpec(i33, Integer.MIN_VALUE), 0, i11, i32, iArr);
                }
            }
            if (shouldLayout(this.f7994m)) {
                int measuredWidth3 = this.f7994m.getMeasuredWidth();
                int[] iArr4 = this.U;
                if (measuredWidth3 > iArr4[1] - iArr4[0]) {
                    measureChildCollapseMargins(this.f7994m, View.MeasureSpec.makeMeasureSpec(i33, Integer.MIN_VALUE), 0, i11, i27 + i32, iArr);
                    return;
                }
                return;
            }
            return;
        }
        int[] iArr5 = this.f7982g;
        boolean b11 = n0.b(this);
        int i34 = !b11 ? 1 : 0;
        if (shouldLayout(this.f7996n)) {
            measureChildConstrained(this.f7996n, i10, 0, i11, 0, this.f8009y);
            i12 = this.f7996n.getMeasuredWidth() + getHorizontalMargins(this.f7996n);
            i14 = Math.max(0, this.f7996n.getMeasuredHeight() + getVerticalMargins(this.f7996n));
            i13 = View.combineMeasuredStates(0, ViewCompat.y(this.f7996n));
        } else {
            i12 = 0;
            i13 = 0;
            i14 = 0;
        }
        if (shouldLayout(this.f8002r)) {
            measureChildConstrained(this.f8002r, i10, 0, i11, 0, this.f8009y);
            i12 = this.f8002r.getMeasuredWidth() + getHorizontalMargins(this.f8002r);
            i14 = Math.max(i14, this.f8002r.getMeasuredHeight() + getVerticalMargins(this.f8002r));
            i13 = View.combineMeasuredStates(i13, ViewCompat.y(this.f8002r));
        }
        int i35 = i13;
        int i36 = i14;
        int contentInsetStart2 = getContentInsetStart();
        int max4 = 0 + Math.max(contentInsetStart2, i12);
        iArr5[b11 ? 1 : 0] = Math.max(0, contentInsetStart2 - i12);
        if (shouldLayout(this.f7990k)) {
            MenuBuilder menuBuilder2 = (MenuBuilder) this.f7990k.getMenu();
            measureChildConstrained(this.f7990k, i10, max4, i11, 0, this.f8009y);
            i16 = this.f7990k.getMeasuredWidth() + getHorizontalMargins(this.f7990k);
            i36 = Math.max(i36, this.f7990k.getMeasuredHeight() + getVerticalMargins(this.f7990k));
            int combineMeasuredStates = View.combineMeasuredStates(i35, ViewCompat.y(this.f7990k));
            menuBuilder = menuBuilder2;
            i15 = combineMeasuredStates;
        } else {
            i15 = i35;
            i16 = 0;
            menuBuilder = null;
        }
        j(menuBuilder, this.f7996n, z10, i10);
        int contentInsetEnd2 = getContentInsetEnd();
        int max5 = max4 + Math.max(contentInsetEnd2, i16);
        iArr5[i34] = Math.max(0, contentInsetEnd2 - i16);
        if (shouldLayout(this.f8003s)) {
            max5 += measureChildCollapseMargins(this.f8003s, i10, max5, i11, 0, iArr5);
            i36 = Math.max(i36, this.f8003s.getMeasuredHeight() + getVerticalMargins(this.f8003s));
            i15 = View.combineMeasuredStates(i15, ViewCompat.y(this.f8003s));
        }
        if (shouldLayout(this.f7998o)) {
            max5 += measureChildCollapseMargins(this.f7998o, i10, max5, i11, 0, iArr5);
            i36 = Math.max(i36, this.f7998o.getMeasuredHeight() + getVerticalMargins(this.f7998o));
            i15 = View.combineMeasuredStates(i15, ViewCompat.y(this.f7998o));
        }
        int childCount2 = getChildCount();
        int i37 = i36;
        int i38 = i15;
        for (int i39 = 0; i39 < childCount2; i39++) {
            View childAt2 = getChildAt(i39);
            if (((LayoutParams) childAt2.getLayoutParams()).f8011c == 0 && shouldLayout(childAt2)) {
                max5 += measureChildCollapseMargins(childAt2, i10, max5, i11, 0, iArr5);
                i37 = Math.max(i37, childAt2.getMeasuredHeight() + getVerticalMargins(childAt2));
                i38 = View.combineMeasuredStates(i38, ViewCompat.y(childAt2));
            }
        }
        int i40 = this.B + this.C;
        int i41 = this.f8010z + this.A;
        if (shouldLayout(this.f7992l)) {
            this.f7992l.getLayoutParams().width = -1;
            this.f7992l.setTextSize(0, this.V);
            i17 = 0;
            measureChildCollapseMargins(this.f7992l, i10, (shouldLayout(this.f7996n) ? this.f7981f0 : 0) + max5 + i41, i11, i40, iArr5);
            int measuredWidth4 = this.f7992l.getMeasuredWidth() + getHorizontalMargins(this.f7992l);
            i20 = this.f7992l.getMeasuredHeight() + getVerticalMargins(this.f7992l);
            i18 = View.combineMeasuredStates(i38, ViewCompat.y(this.f7992l));
            i19 = measuredWidth4;
        } else {
            i17 = 0;
            i18 = i38;
            i19 = 0;
            i20 = 0;
        }
        if (shouldLayout(this.f7994m)) {
            this.f7994m.getLayoutParams().width = -1;
            i19 = Math.max(i19, measureChildCollapseMargins(this.f7994m, i10, (shouldLayout(this.f7996n) ? this.f7981f0 : i17) + max5 + i41, i11, i20 + i40, iArr5));
            i20 += this.f7994m.getMeasuredHeight() + getVerticalMargins(this.f7994m);
            i18 = View.combineMeasuredStates(i18, ViewCompat.y(this.f7994m));
        }
        setMeasuredDimension(ViewCompat.i0(Math.max(max5 + i19 + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), i10, (-16777216) & i18), shouldCollapse() ? i17 : ViewCompat.i0(Math.max(Math.max(i37, i20) + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), i11, i18 << 16));
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.View
    public void onRtlPropertiesChanged(int i10) {
        super.onRtlPropertiesChanged(i10);
        COUIRtlSpacingHelper cOUIRtlSpacingHelper = this.f7978e;
        if (cOUIRtlSpacingHelper != null) {
            cOUIRtlSpacingHelper.f(i10 == 1);
        }
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int a10 = MotionEventCompat.a(motionEvent);
        if (a10 == 0) {
            this.K = false;
        }
        if (!this.K) {
            boolean onTouchEvent = super.onTouchEvent(motionEvent);
            if (a10 == 0 && !onTouchEvent) {
                this.K = true;
            }
        }
        if (a10 == 1 || a10 == 3) {
            this.K = false;
        }
        return true;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setCollapsible(boolean z10) {
        this.Q = z10;
        requestLayout();
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setContentInsetsAbsolute(int i10, int i11) {
        this.f7978e.e(i10, i11);
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setContentInsetsRelative(int i10, int i11) {
        this.f7978e.g(i10, i11);
    }

    public void setIsTitleCenterStyle(boolean z10) {
        ensureMenuView();
        this.S = z10;
        LayoutParams layoutParams = (LayoutParams) this.f7990k.getLayoutParams();
        if (this.S) {
            ((ViewGroup.MarginLayoutParams) layoutParams).width = -1;
        } else {
            ((ViewGroup.MarginLayoutParams) layoutParams).width = -2;
        }
        this.f7990k.setLayoutParams(layoutParams);
        requestLayout();
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setLogo(int i10) {
        setLogo(AppCompatResources.b(getContext(), i10));
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setLogoDescription(int i10) {
        setLogoDescription(getContext().getText(i10));
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setMenuCallbacks(MenuPresenter.a aVar, MenuBuilder.a aVar2) {
        this.O = aVar;
        this.P = aVar2;
    }

    public void setMenuViewColor(int i10) {
        Drawable overflowIcon;
        COUIActionMenuView cOUIActionMenuView = this.f7990k;
        if (cOUIActionMenuView == null || (overflowIcon = cOUIActionMenuView.getOverflowIcon()) == null || (overflowIcon instanceof AnimatedStateListDrawableCompat)) {
            return;
        }
        DrawableCompat.h(overflowIcon, i10);
        this.f7990k.setOverflowIcon(overflowIcon);
    }

    public void setMinTitleTextSize(float f10) {
        float f11 = this.f7987i0;
        if (f10 > f11) {
            f10 = f11;
        }
        this.f7989j0 = f10;
    }

    @Override // android.view.View
    public void setMinimumHeight(int i10) {
        this.R = i10;
        super.setMinimumHeight(i10);
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setNavigationContentDescription(int i10) {
        setNavigationContentDescription(i10 != 0 ? getContext().getText(i10) : null);
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setNavigationIcon(int i10) {
        setNavigationIcon(AppCompatResources.b(getContext(), i10));
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setNavigationOnClickListener(View.OnClickListener onClickListener) {
        ensureNavButtonView();
        this.f7996n.setOnClickListener(onClickListener);
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setOnMenuItemClickListener(Toolbar.g gVar) {
        this.M = gVar;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setOverflowIcon(Drawable drawable) {
        ensureMenu();
        this.f7990k.setOverflowIcon(drawable);
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setPopupTheme(int i10) {
        if (this.f8005u != i10) {
            this.f8005u = i10;
            if (i10 == 0) {
                this.f8004t = getContext();
            } else {
                this.f8004t = new ContextThemeWrapper(getContext(), i10);
            }
        }
    }

    public void setPopupWindowOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        COUIActionMenuView cOUIActionMenuView = this.f7990k;
        if (cOUIActionMenuView instanceof COUIActionMenuView) {
            cOUIActionMenuView.setPopupWindowOnDismissListener(onDismissListener);
        }
    }

    public void setSearchView(View view) {
        n(view, view != null ? new LayoutParams(view.getLayoutParams()) : null);
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setSubtitle(int i10) {
        setSubtitle(getContext().getText(i10));
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setSubtitleTextAppearance(Context context, int i10) {
        this.f8007w = i10;
        TextView textView = this.f7994m;
        if (textView != null) {
            textView.setTextAppearance(context, i10);
        }
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setSubtitleTextColor(int i10) {
        this.J = i10;
        TextView textView = this.f7994m;
        if (textView != null) {
            textView.setTextColor(i10);
        }
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setTitle(int i10) {
        setTitle(getContext().getText(i10));
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setTitleMarginStart(int i10) {
        this.f8010z = i10;
        requestLayout();
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setTitleTextAppearance(Context context, int i10) {
        this.f8006v = i10;
        TextView textView = this.f7992l;
        if (textView != null) {
            textView.setTextAppearance(context, i10);
            if (this.T == 1) {
                this.f7992l.setTextSize(0, COUIChangeTextUtil.e(this.f7992l.getTextSize(), getContext().getResources().getConfiguration().fontScale, 2));
            }
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(this.f8006v, new int[]{R.attr.minHeight});
            if (obtainStyledAttributes != null) {
                this.f7992l.setMinHeight(obtainStyledAttributes.getDimensionPixelSize(0, 0));
                obtainStyledAttributes.recycle();
            }
            TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(this.f8006v, new int[]{R.attr.lineSpacingMultiplier});
            if (obtainStyledAttributes2 != null) {
                float f10 = obtainStyledAttributes2.getFloat(0, 1.4f);
                TextView textView2 = this.f7992l;
                textView2.setLineSpacing(textView2.getLineSpacingExtra(), f10);
                obtainStyledAttributes2.recycle();
            }
            TypedArray obtainStyledAttributes3 = context.obtainStyledAttributes(this.f8006v, new int[]{R.attr.maxWidth});
            if (obtainStyledAttributes3 != null) {
                int dimensionPixelSize = obtainStyledAttributes3.getDimensionPixelSize(0, -1);
                if (dimensionPixelSize >= 0) {
                    this.f7992l.setMaxWidth(dimensionPixelSize);
                }
                obtainStyledAttributes3.recycle();
            }
            TypedArray obtainStyledAttributes4 = context.obtainStyledAttributes(this.f8006v, new int[]{R.attr.textAlignment});
            if (obtainStyledAttributes4 != null) {
                int integer = obtainStyledAttributes4.getInteger(0, 5);
                if (integer >= 0) {
                    this.f7992l.setTextAlignment(integer);
                }
                obtainStyledAttributes4.recycle();
            }
            TypedArray obtainStyledAttributes5 = context.obtainStyledAttributes(this.f8006v, new int[]{R.attr.maxLines});
            if (obtainStyledAttributes5 != null) {
                int integer2 = obtainStyledAttributes5.getInteger(0, 1);
                if (integer2 >= 1) {
                    this.f7992l.setSingleLine(false);
                    this.f7992l.setMaxLines(integer2);
                }
                obtainStyledAttributes5.recycle();
            }
            this.f7987i0 = this.f7992l.getTextSize();
            this.V = this.f7992l.getTextSize();
        }
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setTitleTextColor(int i10) {
        this.I = i10;
        TextView textView = this.f7992l;
        if (textView != null) {
            textView.setTextColor(i10);
        }
    }

    public void setTitleTextSize(float f10) {
        TextView textView = this.f7992l;
        if (textView != null) {
            textView.setTextSize(f10);
            this.V = TypedValue.applyDimension(1, f10, getResources().getDisplayMetrics());
        }
    }

    public void setTitleTextViewTypeface(Typeface typeface) {
        this.f7992l.setTypeface(typeface);
    }

    @Override // androidx.appcompat.widget.Toolbar
    public boolean showOverflowMenu() {
        COUIActionMenuView cOUIActionMenuView = this.f7990k;
        if ((cOUIActionMenuView instanceof COUIActionMenuView) && cOUIActionMenuView.getWindowToken() != null) {
            return this.f7990k.p();
        }
        return super.showOverflowMenu();
    }

    public COUIToolbar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.toolbarStyle);
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setLogo(Drawable drawable) {
        if (drawable != null) {
            ensureLogoView();
            if (this.f7998o.getParent() == null) {
                h(this.f7998o);
                o(this.f7998o);
            }
        } else {
            ImageView imageView = this.f7998o;
            if (imageView != null && imageView.getParent() != null) {
                removeView(this.f7998o);
            }
        }
        ImageView imageView2 = this.f7998o;
        if (imageView2 != null) {
            imageView2.setImageDrawable(drawable);
        }
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setLogoDescription(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            ensureLogoView();
        }
        ImageView imageView = this.f7998o;
        if (imageView != null) {
            imageView.setContentDescription(charSequence);
        }
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setNavigationContentDescription(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            ensureNavButtonView();
        }
        ImageButton imageButton = this.f7996n;
        if (imageButton != null) {
            imageButton.setContentDescription(charSequence);
        }
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setNavigationIcon(Drawable drawable) {
        if (drawable != null) {
            ensureNavButtonView();
            if (this.f7996n.getParent() == null) {
                h(this.f7996n);
                o(this.f7996n);
            }
        } else {
            ImageButton imageButton = this.f7996n;
            if (imageButton != null && imageButton.getParent() != null) {
                removeView(this.f7996n);
            }
        }
        ImageButton imageButton2 = this.f7996n;
        if (imageButton2 != null) {
            imageButton2.setImageDrawable(drawable);
        }
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setSubtitle(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            if (this.f7994m == null) {
                Context context = getContext();
                TextView textView = new TextView(context);
                this.f7994m = textView;
                textView.setSingleLine();
                this.f7994m.setEllipsize(TextUtils.TruncateAt.END);
                int i10 = this.f8007w;
                if (i10 != 0) {
                    this.f7994m.setTextAppearance(context, i10);
                }
                int i11 = this.J;
                if (i11 != 0) {
                    this.f7994m.setTextColor(i11);
                }
                this.f7994m.setTranslationY(context.getResources().getDimensionPixelOffset(R$dimen.coui_toolbar_subtitle_offset_top));
            }
            if (this.f7994m.getParent() == null) {
                h(this.f7994m);
                o(this.f7994m);
            }
        } else {
            TextView textView2 = this.f7994m;
            if (textView2 != null && textView2.getParent() != null) {
                removeView(this.f7994m);
            }
        }
        TextView textView3 = this.f7994m;
        if (textView3 != null) {
            textView3.setTextAlignment(5);
            this.f7994m.setText(charSequence);
        }
        this.H = charSequence;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setTitle(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            if (this.f7992l == null) {
                Context context = getContext();
                TextView textView = new TextView(context);
                this.f7992l = textView;
                textView.setPaddingRelative(0, this.D, 0, this.E);
                LayoutParams generateDefaultLayoutParams = generateDefaultLayoutParams();
                ((ViewGroup.MarginLayoutParams) generateDefaultLayoutParams).bottomMargin = this.C;
                generateDefaultLayoutParams.f320a = 8388613 | (this.f8008x & 112);
                this.f7992l.setLayoutParams(generateDefaultLayoutParams);
                this.f7992l.setSingleLine();
                this.f7992l.setEllipsize(TextUtils.TruncateAt.END);
                int i10 = this.f8006v;
                if (i10 != 0) {
                    setTitleTextAppearance(context, i10);
                }
                int i11 = this.I;
                if (i11 != 0) {
                    this.f7992l.setTextColor(i11);
                }
                if (this.T == 1) {
                    this.f7992l.setTextSize(0, COUIChangeTextUtil.e(this.f7992l.getTextSize(), getContext().getResources().getConfiguration().fontScale, 2));
                }
            }
            if (this.f7992l.getParent() == null) {
                h(this.f7992l);
                o(this.f7992l);
            }
        } else {
            TextView textView2 = this.f7992l;
            if (textView2 != null && textView2.getParent() != null) {
                removeView(this.f7992l);
            }
        }
        TextView textView3 = this.f7992l;
        if (textView3 != null) {
            textView3.setText(charSequence);
            this.V = this.f7992l.getTextSize();
        }
        this.G = charSequence;
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends Toolbar.LayoutParams {

        /* renamed from: c, reason: collision with root package name */
        int f8011c;

        /* renamed from: d, reason: collision with root package name */
        boolean f8012d;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.f8011c = 0;
            this.f8012d = false;
        }

        void a(ViewGroup.MarginLayoutParams marginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) this).leftMargin = marginLayoutParams.leftMargin;
            ((ViewGroup.MarginLayoutParams) this).topMargin = marginLayoutParams.topMargin;
            ((ViewGroup.MarginLayoutParams) this).rightMargin = marginLayoutParams.rightMargin;
            ((ViewGroup.MarginLayoutParams) this).bottomMargin = marginLayoutParams.bottomMargin;
        }

        public LayoutParams(int i10, int i11) {
            super(i10, i11);
            this.f8011c = 0;
            this.f8012d = false;
            this.f320a = 8388627;
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((Toolbar.LayoutParams) layoutParams);
            this.f8011c = 0;
            this.f8012d = false;
            this.f8011c = layoutParams.f8011c;
        }

        public LayoutParams(ActionBar.LayoutParams layoutParams) {
            super(layoutParams);
            this.f8011c = 0;
            this.f8012d = false;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.f8011c = 0;
            this.f8012d = false;
            a(marginLayoutParams);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.f8011c = 0;
            this.f8012d = false;
        }
    }

    public COUIToolbar(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        COUIRtlSpacingHelper cOUIRtlSpacingHelper = new COUIRtlSpacingHelper();
        this.f7978e = cOUIRtlSpacingHelper;
        this.f7980f = new ArrayList<>();
        this.f7982g = new int[2];
        this.f7984h = new a();
        this.f7986i = new int[2];
        this.f7988j = new b();
        this.F = 8388627;
        this.S = false;
        this.U = new int[2];
        this.V = 0.0f;
        this.f7985h0 = false;
        this.f7991k0 = null;
        this.f7993l0 = -1;
        setClipToPadding(false);
        setClipChildren(false);
        if (attributeSet != null) {
            int styleAttribute = attributeSet.getStyleAttribute();
            this.f7983g0 = styleAttribute;
            if (styleAttribute == 0) {
                this.f7983g0 = i10;
            }
        } else {
            this.f7983g0 = 0;
        }
        TintTypedArray w10 = TintTypedArray.w(getContext(), attributeSet, R$styleable.COUIToolbar, i10, 0);
        int i11 = R$styleable.COUIToolbar_titleType;
        if (w10.s(i11)) {
            this.T = w10.k(i11, 0);
        }
        this.f8006v = w10.n(R$styleable.COUIToolbar_supportTitleTextAppearance, 0);
        this.f8007w = w10.n(R$styleable.COUIToolbar_supportSubtitleTextAppearance, 0);
        this.F = w10.l(R$styleable.COUIToolbar_android_gravity, this.F);
        this.f8008x = w10.l(R$styleable.COUIToolbar_supportButtonGravity, 48);
        this.f8010z = w10.e(R$styleable.COUIToolbar_supportTitleMargins, 0);
        this.f7999o0 = w10.a(R$styleable.COUIToolbar_supportIsTiny, false);
        int i12 = this.f8010z;
        this.A = i12;
        this.B = i12;
        this.C = i12;
        int e10 = w10.e(R$styleable.COUIToolbar_supportTitleMarginStart, getContext().getResources().getDimensionPixelSize(R$dimen.coui_toolbar_support_margin_start));
        if (e10 >= 0) {
            this.f8010z = e10;
        }
        int e11 = w10.e(R$styleable.COUIToolbar_supportTitleMarginEnd, -1);
        if (e11 >= 0) {
            this.A = e11;
        }
        int e12 = w10.e(R$styleable.COUIToolbar_supportTitleMarginTop, -1);
        if (e12 >= 0) {
            this.B = e12;
        }
        int e13 = w10.e(R$styleable.COUIToolbar_supportTitleMarginBottom, -1);
        if (e13 >= 0) {
            this.C = e13;
        }
        this.D = w10.f(R$styleable.COUIToolbar_supportTitlePaddingTop, 0);
        this.E = w10.f(R$styleable.COUIToolbar_supportTitlePaddingBottom, 0);
        this.f8009y = w10.f(R$styleable.COUIToolbar_supportMaxButtonHeight, -1);
        int e14 = w10.e(R$styleable.COUIToolbar_supportContentInsetStart, Integer.MIN_VALUE);
        int e15 = w10.e(R$styleable.COUIToolbar_supportContentInsetEnd, Integer.MIN_VALUE);
        cOUIRtlSpacingHelper.e(w10.f(R$styleable.COUIToolbar_supportContentInsetLeft, 0), w10.f(R$styleable.COUIToolbar_supportContentInsetRight, 0));
        if (e14 != Integer.MIN_VALUE || e15 != Integer.MIN_VALUE) {
            cOUIRtlSpacingHelper.g(e14, e15);
        }
        this.f8000p = w10.g(R$styleable.COUIToolbar_supportCollapseIcon);
        this.f8001q = w10.p(R$styleable.COUIToolbar_supportCollapseContentDescription);
        CharSequence p10 = w10.p(R$styleable.COUIToolbar_supportTitle);
        if (!TextUtils.isEmpty(p10)) {
            setTitle(p10);
        }
        CharSequence p11 = w10.p(R$styleable.COUIToolbar_supportSubtitle);
        if (!TextUtils.isEmpty(p11)) {
            setSubtitle(p11);
        }
        this.f8004t = getContext();
        setPopupTheme(w10.n(R$styleable.COUIToolbar_supportPopupTheme, 0));
        Drawable g6 = w10.g(R$styleable.COUIToolbar_supportNavigationIcon);
        if (g6 != null) {
            setNavigationIcon(g6);
        }
        CharSequence p12 = w10.p(R$styleable.COUIToolbar_supportNavigationContentDescription);
        if (!TextUtils.isEmpty(p12)) {
            setNavigationContentDescription(p12);
        }
        this.R = w10.f(androidx.appcompat.R$styleable.Toolbar_android_minHeight, 0);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        if (w10.s(R$styleable.COUIToolbar_minTitleTextSize)) {
            this.f7989j0 = w10.f(r0, (int) (displayMetrics.scaledDensity * 16.0f));
        } else {
            this.f7989j0 = displayMetrics.scaledDensity * 16.0f;
        }
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(this.f8006v, new int[]{R.attr.textSize});
        if (obtainStyledAttributes != null) {
            this.f7987i0 = obtainStyledAttributes.getDimensionPixelSize(0, (int) (displayMetrics.scaledDensity * 24.0f));
            obtainStyledAttributes.recycle();
        }
        if (this.T == 1) {
            this.f7987i0 = COUIChangeTextUtil.e(this.f7987i0, getResources().getConfiguration().fontScale, 2);
            this.f7989j0 = COUIChangeTextUtil.e(this.f7989j0, getResources().getConfiguration().fontScale, 2);
        }
        this.W = getContext().getResources().getDimensionPixelOffset(R$dimen.toolbar_normal_menu_padding_left);
        if (this.f7999o0) {
            this.f7974a0 = getContext().getResources().getDimensionPixelOffset(R$dimen.toolbar_normal_menu_padding_tiny_right);
        } else {
            this.f7974a0 = getContext().getResources().getDimensionPixelOffset(R$dimen.toolbar_normal_menu_padding_right);
        }
        this.f7975b0 = getContext().getResources().getDimensionPixelOffset(R$dimen.toolbar_center_title_padding_left);
        this.f7976c0 = getContext().getResources().getDimensionPixelOffset(R$dimen.toolbar_center_title_padding_right);
        this.f7977d0 = getContext().getResources().getDimensionPixelOffset(R$dimen.toolbar_overflow_menu_padding);
        this.f7979e0 = getContext().getResources().getDimensionPixelOffset(R$dimen.coui_toolbar_text_menu_bg_padding_horizontal);
        this.f7981f0 = getContext().getResources().getDimensionPixelOffset(R$dimen.coui_toolbar_gap_between_navigation_and_title);
        int i13 = R$styleable.COUIToolbar_titleCenter;
        if (w10.s(i13)) {
            this.S = w10.a(i13, false);
        }
        setWillNotDraw(false);
        w10.x();
    }
}
