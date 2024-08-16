package com.google.android.material.navigation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.PointerIconCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.widget.TextViewCompat;
import com.google.android.material.R$attr;
import com.google.android.material.R$dimen;
import com.google.android.material.R$drawable;
import com.google.android.material.R$id;
import com.google.android.material.R$integer;
import com.google.android.material.R$string;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.oplus.statistics.DataTypeConstants;
import p3.AnimationUtils;
import x3.MotionUtils;
import z3.MaterialResources;

/* loaded from: classes.dex */
public abstract class NavigationBarItemView extends FrameLayout implements MenuView.a {
    private static final int[] G = {16842912};
    private static final d H;
    private static final d I;
    private boolean A;
    private int B;
    private int C;
    private boolean D;
    private int E;
    private BadgeDrawable F;

    /* renamed from: e, reason: collision with root package name */
    private boolean f8943e;

    /* renamed from: f, reason: collision with root package name */
    private int f8944f;

    /* renamed from: g, reason: collision with root package name */
    private int f8945g;

    /* renamed from: h, reason: collision with root package name */
    private float f8946h;

    /* renamed from: i, reason: collision with root package name */
    private float f8947i;

    /* renamed from: j, reason: collision with root package name */
    private float f8948j;

    /* renamed from: k, reason: collision with root package name */
    private int f8949k;

    /* renamed from: l, reason: collision with root package name */
    private boolean f8950l;

    /* renamed from: m, reason: collision with root package name */
    private final FrameLayout f8951m;

    /* renamed from: n, reason: collision with root package name */
    private final View f8952n;

    /* renamed from: o, reason: collision with root package name */
    private final ImageView f8953o;

    /* renamed from: p, reason: collision with root package name */
    private final ViewGroup f8954p;

    /* renamed from: q, reason: collision with root package name */
    private final TextView f8955q;

    /* renamed from: r, reason: collision with root package name */
    private final TextView f8956r;

    /* renamed from: s, reason: collision with root package name */
    private int f8957s;

    /* renamed from: t, reason: collision with root package name */
    private MenuItemImpl f8958t;

    /* renamed from: u, reason: collision with root package name */
    private ColorStateList f8959u;

    /* renamed from: v, reason: collision with root package name */
    private Drawable f8960v;

    /* renamed from: w, reason: collision with root package name */
    private Drawable f8961w;

    /* renamed from: x, reason: collision with root package name */
    private ValueAnimator f8962x;

    /* renamed from: y, reason: collision with root package name */
    private d f8963y;

    /* renamed from: z, reason: collision with root package name */
    private float f8964z;

    /* loaded from: classes.dex */
    class a implements View.OnLayoutChangeListener {
        a() {
        }

        @Override // android.view.View.OnLayoutChangeListener
        public void onLayoutChange(View view, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17) {
            if (NavigationBarItemView.this.f8953o.getVisibility() == 0) {
                NavigationBarItemView navigationBarItemView = NavigationBarItemView.this;
                navigationBarItemView.s(navigationBarItemView.f8953o);
            }
        }
    }

    /* loaded from: classes.dex */
    class b implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ int f8966e;

        b(int i10) {
            this.f8966e = i10;
        }

        @Override // java.lang.Runnable
        public void run() {
            NavigationBarItemView.this.t(this.f8966e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ float f8968a;

        c(float f10) {
            this.f8968a = f10;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            NavigationBarItemView.this.m(((Float) valueAnimator.getAnimatedValue()).floatValue(), this.f8968a);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class d {
        private d() {
        }

        /* synthetic */ d(a aVar) {
            this();
        }

        protected float a(float f10, float f11) {
            return AnimationUtils.b(0.0f, 1.0f, f11 == 0.0f ? 0.8f : 0.0f, f11 == 0.0f ? 1.0f : 0.2f, f10);
        }

        protected float b(float f10, float f11) {
            return AnimationUtils.a(0.4f, 1.0f, f10);
        }

        protected float c(float f10, float f11) {
            return 1.0f;
        }

        public void d(float f10, float f11, View view) {
            view.setScaleX(b(f10, f11));
            view.setScaleY(c(f10, f11));
            view.setAlpha(a(f10, f11));
        }
    }

    /* loaded from: classes.dex */
    private static class e extends d {
        private e() {
            super(null);
        }

        @Override // com.google.android.material.navigation.NavigationBarItemView.d
        protected float c(float f10, float f11) {
            return b(f10, f11);
        }

        /* synthetic */ e(a aVar) {
            this();
        }
    }

    static {
        a aVar = null;
        H = new d(aVar);
        I = new e(aVar);
    }

    public NavigationBarItemView(Context context) {
        super(context);
        this.f8943e = false;
        this.f8957s = -1;
        this.f8963y = H;
        this.f8964z = 0.0f;
        this.A = false;
        this.B = 0;
        this.C = 0;
        this.D = false;
        this.E = 0;
        LayoutInflater.from(context).inflate(getItemLayoutResId(), (ViewGroup) this, true);
        this.f8951m = (FrameLayout) findViewById(R$id.navigation_bar_item_icon_container);
        this.f8952n = findViewById(R$id.navigation_bar_item_active_indicator_view);
        ImageView imageView = (ImageView) findViewById(R$id.navigation_bar_item_icon_view);
        this.f8953o = imageView;
        ViewGroup viewGroup = (ViewGroup) findViewById(R$id.navigation_bar_item_labels_group);
        this.f8954p = viewGroup;
        TextView textView = (TextView) findViewById(R$id.navigation_bar_item_small_label_view);
        this.f8955q = textView;
        TextView textView2 = (TextView) findViewById(R$id.navigation_bar_item_large_label_view);
        this.f8956r = textView2;
        setBackgroundResource(getItemBackgroundResId());
        this.f8944f = getResources().getDimensionPixelSize(getItemDefaultMarginResId());
        this.f8945g = viewGroup.getPaddingBottom();
        ViewCompat.w0(textView, 2);
        ViewCompat.w0(textView2, 2);
        setFocusable(true);
        e(textView.getTextSize(), textView2.getTextSize());
        if (imageView != null) {
            imageView.addOnLayoutChangeListener(new a());
        }
    }

    private void e(float f10, float f11) {
        this.f8946h = f10 - f11;
        this.f8947i = (f11 * 1.0f) / f10;
        this.f8948j = (f10 * 1.0f) / f11;
    }

    private FrameLayout g(View view) {
        ImageView imageView = this.f8953o;
        if (view == imageView && BadgeUtils.f8315a) {
            return (FrameLayout) imageView.getParent();
        }
        return null;
    }

    private View getIconOrContainer() {
        FrameLayout frameLayout = this.f8951m;
        return frameLayout != null ? frameLayout : this.f8953o;
    }

    private int getItemVisiblePosition() {
        ViewGroup viewGroup = (ViewGroup) getParent();
        int indexOfChild = viewGroup.indexOfChild(this);
        int i10 = 0;
        for (int i11 = 0; i11 < indexOfChild; i11++) {
            View childAt = viewGroup.getChildAt(i11);
            if ((childAt instanceof NavigationBarItemView) && childAt.getVisibility() == 0) {
                i10++;
            }
        }
        return i10;
    }

    private int getSuggestedIconHeight() {
        BadgeDrawable badgeDrawable = this.F;
        int minimumHeight = badgeDrawable != null ? badgeDrawable.getMinimumHeight() / 2 : 0;
        return Math.max(minimumHeight, ((FrameLayout.LayoutParams) getIconOrContainer().getLayoutParams()).topMargin) + this.f8953o.getMeasuredWidth() + minimumHeight;
    }

    private int getSuggestedIconWidth() {
        BadgeDrawable badgeDrawable = this.F;
        int minimumWidth = badgeDrawable == null ? 0 : badgeDrawable.getMinimumWidth() - this.F.h();
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getIconOrContainer().getLayoutParams();
        return Math.max(minimumWidth, layoutParams.leftMargin) + this.f8953o.getMeasuredWidth() + Math.max(minimumWidth, layoutParams.rightMargin);
    }

    private boolean h() {
        return this.F != null;
    }

    private boolean i() {
        return this.D && this.f8949k == 2;
    }

    private void j(float f10) {
        if (this.A && this.f8943e && ViewCompat.P(this)) {
            ValueAnimator valueAnimator = this.f8962x;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.f8962x = null;
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.f8964z, f10);
            this.f8962x = ofFloat;
            ofFloat.addUpdateListener(new c(f10));
            this.f8962x.setInterpolator(MotionUtils.e(getContext(), R$attr.motionEasingStandard, AnimationUtils.f16556b));
            this.f8962x.setDuration(MotionUtils.d(getContext(), R$attr.motionDurationLong1, getResources().getInteger(R$integer.material_motion_duration_long_1)));
            this.f8962x.start();
            return;
        }
        m(f10, f10);
    }

    private void k() {
        MenuItemImpl menuItemImpl = this.f8958t;
        if (menuItemImpl != null) {
            setChecked(menuItemImpl.isChecked());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void m(float f10, float f11) {
        View view = this.f8952n;
        if (view != null) {
            this.f8963y.d(f10, f11, view);
        }
        this.f8964z = f10;
    }

    private static void n(TextView textView, int i10) {
        TextViewCompat.n(textView, i10);
        int h10 = MaterialResources.h(textView.getContext(), i10, 0);
        if (h10 != 0) {
            textView.setTextSize(0, h10);
        }
    }

    private static void o(View view, float f10, float f11, int i10) {
        view.setScaleX(f10);
        view.setScaleY(f11);
        view.setVisibility(i10);
    }

    private static void p(View view, int i10, int i11) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        layoutParams.topMargin = i10;
        layoutParams.bottomMargin = i10;
        layoutParams.gravity = i11;
        view.setLayoutParams(layoutParams);
    }

    private void q(View view) {
        if (h() && view != null) {
            setClipChildren(false);
            setClipToPadding(false);
            BadgeUtils.a(this.F, view, g(view));
        }
    }

    private void r(View view) {
        if (h()) {
            if (view != null) {
                setClipChildren(true);
                setClipToPadding(true);
                BadgeUtils.d(this.F, view);
            }
            this.F = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void s(View view) {
        if (h()) {
            BadgeUtils.e(this.F, view, g(view));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void t(int i10) {
        if (this.f8952n == null) {
            return;
        }
        int min = Math.min(this.B, i10 - (this.E * 2));
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.f8952n.getLayoutParams();
        layoutParams.height = i() ? min : this.C;
        layoutParams.width = min;
        this.f8952n.setLayoutParams(layoutParams);
    }

    private void u() {
        if (i()) {
            this.f8963y = I;
        } else {
            this.f8963y = H;
        }
    }

    private static void v(View view, int i10) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), i10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f() {
        l();
        this.f8958t = null;
        this.f8964z = 0.0f;
        this.f8943e = false;
    }

    public Drawable getActiveIndicatorDrawable() {
        View view = this.f8952n;
        if (view == null) {
            return null;
        }
        return view.getBackground();
    }

    public BadgeDrawable getBadge() {
        return this.F;
    }

    protected int getItemBackgroundResId() {
        return R$drawable.mtrl_navigation_bar_item_background;
    }

    @Override // androidx.appcompat.view.menu.MenuView.a
    public MenuItemImpl getItemData() {
        return this.f8958t;
    }

    protected int getItemDefaultMarginResId() {
        return R$dimen.mtrl_navigation_bar_item_default_margin;
    }

    protected abstract int getItemLayoutResId();

    public int getItemPosition() {
        return this.f8957s;
    }

    @Override // android.view.View
    protected int getSuggestedMinimumHeight() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.f8954p.getLayoutParams();
        return getSuggestedIconHeight() + layoutParams.topMargin + this.f8954p.getMeasuredHeight() + layoutParams.bottomMargin;
    }

    @Override // android.view.View
    protected int getSuggestedMinimumWidth() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.f8954p.getLayoutParams();
        return Math.max(getSuggestedIconWidth(), layoutParams.leftMargin + this.f8954p.getMeasuredWidth() + layoutParams.rightMargin);
    }

    @Override // androidx.appcompat.view.menu.MenuView.a
    public void initialize(MenuItemImpl menuItemImpl, int i10) {
        CharSequence title;
        this.f8958t = menuItemImpl;
        setCheckable(menuItemImpl.isCheckable());
        setChecked(menuItemImpl.isChecked());
        setEnabled(menuItemImpl.isEnabled());
        setIcon(menuItemImpl.getIcon());
        setTitle(menuItemImpl.getTitle());
        setId(menuItemImpl.getItemId());
        if (!TextUtils.isEmpty(menuItemImpl.getContentDescription())) {
            setContentDescription(menuItemImpl.getContentDescription());
        }
        if (!TextUtils.isEmpty(menuItemImpl.getTooltipText())) {
            title = menuItemImpl.getTooltipText();
        } else {
            title = menuItemImpl.getTitle();
        }
        TooltipCompat.a(this, title);
        setVisibility(menuItemImpl.isVisible() ? 0 : 8);
        this.f8943e = true;
    }

    void l() {
        r(this.f8953o);
    }

    @Override // android.view.ViewGroup, android.view.View
    public int[] onCreateDrawableState(int i10) {
        int[] onCreateDrawableState = super.onCreateDrawableState(i10 + 1);
        MenuItemImpl menuItemImpl = this.f8958t;
        if (menuItemImpl != null && menuItemImpl.isCheckable() && this.f8958t.isChecked()) {
            FrameLayout.mergeDrawableStates(onCreateDrawableState, G);
        }
        return onCreateDrawableState;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        BadgeDrawable badgeDrawable = this.F;
        if (badgeDrawable != null && badgeDrawable.isVisible()) {
            CharSequence title = this.f8958t.getTitle();
            if (!TextUtils.isEmpty(this.f8958t.getContentDescription())) {
                title = this.f8958t.getContentDescription();
            }
            accessibilityNodeInfo.setContentDescription(((Object) title) + ", " + ((Object) this.F.f()));
        }
        AccessibilityNodeInfoCompat C0 = AccessibilityNodeInfoCompat.C0(accessibilityNodeInfo);
        C0.Y(AccessibilityNodeInfoCompat.c.a(0, 1, getItemVisiblePosition(), 1, false, isSelected()));
        if (isSelected()) {
            C0.W(false);
            C0.O(AccessibilityNodeInfoCompat.a.f2322i);
        }
        C0.q0(getResources().getString(R$string.item_view_role_description));
    }

    @Override // android.view.View
    protected void onSizeChanged(int i10, int i11, int i12, int i13) {
        super.onSizeChanged(i10, i11, i12, i13);
        post(new b(i10));
    }

    @Override // androidx.appcompat.view.menu.MenuView.a
    public boolean prefersCondensedTitle() {
        return false;
    }

    public void setActiveIndicatorDrawable(Drawable drawable) {
        View view = this.f8952n;
        if (view == null) {
            return;
        }
        view.setBackgroundDrawable(drawable);
    }

    public void setActiveIndicatorEnabled(boolean z10) {
        this.A = z10;
        View view = this.f8952n;
        if (view != null) {
            view.setVisibility(z10 ? 0 : 8);
            requestLayout();
        }
    }

    public void setActiveIndicatorHeight(int i10) {
        this.C = i10;
        t(getWidth());
    }

    public void setActiveIndicatorMarginHorizontal(int i10) {
        this.E = i10;
        t(getWidth());
    }

    public void setActiveIndicatorResizeable(boolean z10) {
        this.D = z10;
    }

    public void setActiveIndicatorWidth(int i10) {
        this.B = i10;
        t(getWidth());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBadge(BadgeDrawable badgeDrawable) {
        if (this.F == badgeDrawable) {
            return;
        }
        if (h() && this.f8953o != null) {
            Log.w("NavigationBar", "Multiple badges shouldn't be attached to one item.");
            r(this.f8953o);
        }
        this.F = badgeDrawable;
        ImageView imageView = this.f8953o;
        if (imageView != null) {
            q(imageView);
        }
    }

    public void setCheckable(boolean z10) {
        refreshDrawableState();
    }

    public void setChecked(boolean z10) {
        this.f8956r.setPivotX(r0.getWidth() / 2);
        this.f8956r.setPivotY(r0.getBaseline());
        this.f8955q.setPivotX(r0.getWidth() / 2);
        this.f8955q.setPivotY(r0.getBaseline());
        j(z10 ? 1.0f : 0.0f);
        int i10 = this.f8949k;
        if (i10 != -1) {
            if (i10 == 0) {
                if (z10) {
                    p(getIconOrContainer(), this.f8944f, 49);
                    v(this.f8954p, this.f8945g);
                    this.f8956r.setVisibility(0);
                } else {
                    p(getIconOrContainer(), this.f8944f, 17);
                    v(this.f8954p, 0);
                    this.f8956r.setVisibility(4);
                }
                this.f8955q.setVisibility(4);
            } else if (i10 == 1) {
                v(this.f8954p, this.f8945g);
                if (z10) {
                    p(getIconOrContainer(), (int) (this.f8944f + this.f8946h), 49);
                    o(this.f8956r, 1.0f, 1.0f, 0);
                    TextView textView = this.f8955q;
                    float f10 = this.f8947i;
                    o(textView, f10, f10, 4);
                } else {
                    p(getIconOrContainer(), this.f8944f, 49);
                    TextView textView2 = this.f8956r;
                    float f11 = this.f8948j;
                    o(textView2, f11, f11, 4);
                    o(this.f8955q, 1.0f, 1.0f, 0);
                }
            } else if (i10 == 2) {
                p(getIconOrContainer(), this.f8944f, 17);
                this.f8956r.setVisibility(8);
                this.f8955q.setVisibility(8);
            }
        } else if (this.f8950l) {
            if (z10) {
                p(getIconOrContainer(), this.f8944f, 49);
                v(this.f8954p, this.f8945g);
                this.f8956r.setVisibility(0);
            } else {
                p(getIconOrContainer(), this.f8944f, 17);
                v(this.f8954p, 0);
                this.f8956r.setVisibility(4);
            }
            this.f8955q.setVisibility(4);
        } else {
            v(this.f8954p, this.f8945g);
            if (z10) {
                p(getIconOrContainer(), (int) (this.f8944f + this.f8946h), 49);
                o(this.f8956r, 1.0f, 1.0f, 0);
                TextView textView3 = this.f8955q;
                float f12 = this.f8947i;
                o(textView3, f12, f12, 4);
            } else {
                p(getIconOrContainer(), this.f8944f, 49);
                TextView textView4 = this.f8956r;
                float f13 = this.f8948j;
                o(textView4, f13, f13, 4);
                o(this.f8955q, 1.0f, 1.0f, 0);
            }
        }
        refreshDrawableState();
        setSelected(z10);
    }

    @Override // android.view.View
    public void setEnabled(boolean z10) {
        super.setEnabled(z10);
        this.f8955q.setEnabled(z10);
        this.f8956r.setEnabled(z10);
        this.f8953o.setEnabled(z10);
        if (z10) {
            ViewCompat.B0(this, PointerIconCompat.b(getContext(), DataTypeConstants.APP_LOG));
        } else {
            ViewCompat.B0(this, null);
        }
    }

    public void setIcon(Drawable drawable) {
        if (drawable == this.f8960v) {
            return;
        }
        this.f8960v = drawable;
        if (drawable != null) {
            Drawable.ConstantState constantState = drawable.getConstantState();
            if (constantState != null) {
                drawable = constantState.newDrawable();
            }
            drawable = DrawableCompat.l(drawable).mutate();
            this.f8961w = drawable;
            ColorStateList colorStateList = this.f8959u;
            if (colorStateList != null) {
                DrawableCompat.i(drawable, colorStateList);
            }
        }
        this.f8953o.setImageDrawable(drawable);
    }

    public void setIconSize(int i10) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.f8953o.getLayoutParams();
        layoutParams.width = i10;
        layoutParams.height = i10;
        this.f8953o.setLayoutParams(layoutParams);
    }

    public void setIconTintList(ColorStateList colorStateList) {
        Drawable drawable;
        this.f8959u = colorStateList;
        if (this.f8958t == null || (drawable = this.f8961w) == null) {
            return;
        }
        DrawableCompat.i(drawable, colorStateList);
        this.f8961w.invalidateSelf();
    }

    public void setItemBackground(int i10) {
        setItemBackground(i10 == 0 ? null : ContextCompat.e(getContext(), i10));
    }

    public void setItemPaddingBottom(int i10) {
        if (this.f8945g != i10) {
            this.f8945g = i10;
            k();
        }
    }

    public void setItemPaddingTop(int i10) {
        if (this.f8944f != i10) {
            this.f8944f = i10;
            k();
        }
    }

    public void setItemPosition(int i10) {
        this.f8957s = i10;
    }

    public void setLabelVisibilityMode(int i10) {
        if (this.f8949k != i10) {
            this.f8949k = i10;
            u();
            t(getWidth());
            k();
        }
    }

    public void setShifting(boolean z10) {
        if (this.f8950l != z10) {
            this.f8950l = z10;
            k();
        }
    }

    public void setTextAppearanceActive(int i10) {
        n(this.f8956r, i10);
        e(this.f8955q.getTextSize(), this.f8956r.getTextSize());
    }

    public void setTextAppearanceInactive(int i10) {
        n(this.f8955q, i10);
        e(this.f8955q.getTextSize(), this.f8956r.getTextSize());
    }

    public void setTextColor(ColorStateList colorStateList) {
        if (colorStateList != null) {
            this.f8955q.setTextColor(colorStateList);
            this.f8956r.setTextColor(colorStateList);
        }
    }

    public void setTitle(CharSequence charSequence) {
        this.f8955q.setText(charSequence);
        this.f8956r.setText(charSequence);
        MenuItemImpl menuItemImpl = this.f8958t;
        if (menuItemImpl == null || TextUtils.isEmpty(menuItemImpl.getContentDescription())) {
            setContentDescription(charSequence);
        }
        MenuItemImpl menuItemImpl2 = this.f8958t;
        if (menuItemImpl2 != null && !TextUtils.isEmpty(menuItemImpl2.getTooltipText())) {
            charSequence = this.f8958t.getTooltipText();
        }
        TooltipCompat.a(this, charSequence);
    }

    public void setItemBackground(Drawable drawable) {
        if (drawable != null && drawable.getConstantState() != null) {
            drawable = drawable.getConstantState().newDrawable().mutate();
        }
        ViewCompat.p0(this, drawable);
    }
}
