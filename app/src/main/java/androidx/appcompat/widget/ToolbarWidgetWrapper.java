package androidx.appcompat.widget;

import android.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import androidx.appcompat.R$attr;
import androidx.appcompat.R$drawable;
import androidx.appcompat.R$id;
import androidx.appcompat.R$string;
import androidx.appcompat.R$styleable;
import androidx.appcompat.view.menu.ActionMenuItem;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;
import c.AppCompatResources;

/* compiled from: ToolbarWidgetWrapper.java */
/* renamed from: androidx.appcompat.widget.k0, reason: use source file name */
/* loaded from: classes.dex */
public class ToolbarWidgetWrapper implements DecorToolbar {

    /* renamed from: a, reason: collision with root package name */
    Toolbar f1239a;

    /* renamed from: b, reason: collision with root package name */
    private int f1240b;

    /* renamed from: c, reason: collision with root package name */
    private View f1241c;

    /* renamed from: d, reason: collision with root package name */
    private View f1242d;

    /* renamed from: e, reason: collision with root package name */
    private Drawable f1243e;

    /* renamed from: f, reason: collision with root package name */
    private Drawable f1244f;

    /* renamed from: g, reason: collision with root package name */
    private Drawable f1245g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f1246h;

    /* renamed from: i, reason: collision with root package name */
    CharSequence f1247i;

    /* renamed from: j, reason: collision with root package name */
    private CharSequence f1248j;

    /* renamed from: k, reason: collision with root package name */
    private CharSequence f1249k;

    /* renamed from: l, reason: collision with root package name */
    Window.Callback f1250l;

    /* renamed from: m, reason: collision with root package name */
    boolean f1251m;

    /* renamed from: n, reason: collision with root package name */
    private ActionMenuPresenter f1252n;

    /* renamed from: o, reason: collision with root package name */
    private int f1253o;

    /* renamed from: p, reason: collision with root package name */
    private int f1254p;

    /* renamed from: q, reason: collision with root package name */
    private Drawable f1255q;

    /* compiled from: ToolbarWidgetWrapper.java */
    /* renamed from: androidx.appcompat.widget.k0$a */
    /* loaded from: classes.dex */
    class a implements View.OnClickListener {

        /* renamed from: e, reason: collision with root package name */
        final ActionMenuItem f1256e;

        a() {
            this.f1256e = new ActionMenuItem(ToolbarWidgetWrapper.this.f1239a.getContext(), 0, R.id.home, 0, 0, ToolbarWidgetWrapper.this.f1247i);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ToolbarWidgetWrapper toolbarWidgetWrapper = ToolbarWidgetWrapper.this;
            Window.Callback callback = toolbarWidgetWrapper.f1250l;
            if (callback == null || !toolbarWidgetWrapper.f1251m) {
                return;
            }
            callback.onMenuItemSelected(0, this.f1256e);
        }
    }

    /* compiled from: ToolbarWidgetWrapper.java */
    /* renamed from: androidx.appcompat.widget.k0$b */
    /* loaded from: classes.dex */
    class b extends ViewPropertyAnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        private boolean f1258a = false;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ int f1259b;

        b(int i10) {
            this.f1259b = i10;
        }

        @Override // androidx.core.view.ViewPropertyAnimatorListenerAdapter, androidx.core.view.ViewPropertyAnimatorListener
        public void a(View view) {
            this.f1258a = true;
        }

        @Override // androidx.core.view.ViewPropertyAnimatorListener
        public void b(View view) {
            if (this.f1258a) {
                return;
            }
            ToolbarWidgetWrapper.this.f1239a.setVisibility(this.f1259b);
        }

        @Override // androidx.core.view.ViewPropertyAnimatorListenerAdapter, androidx.core.view.ViewPropertyAnimatorListener
        public void c(View view) {
            ToolbarWidgetWrapper.this.f1239a.setVisibility(0);
        }
    }

    public ToolbarWidgetWrapper(Toolbar toolbar, boolean z10) {
        this(toolbar, z10, R$string.abc_action_bar_up_description, R$drawable.abc_ic_ab_back_material);
    }

    private void F(CharSequence charSequence) {
        this.f1247i = charSequence;
        if ((this.f1240b & 8) != 0) {
            this.f1239a.setTitle(charSequence);
            if (this.f1246h) {
                ViewCompat.o0(this.f1239a.getRootView(), charSequence);
            }
        }
    }

    private void G() {
        if ((this.f1240b & 4) != 0) {
            if (TextUtils.isEmpty(this.f1249k)) {
                this.f1239a.setNavigationContentDescription(this.f1254p);
            } else {
                this.f1239a.setNavigationContentDescription(this.f1249k);
            }
        }
    }

    private void H() {
        if ((this.f1240b & 4) != 0) {
            Toolbar toolbar = this.f1239a;
            Drawable drawable = this.f1245g;
            if (drawable == null) {
                drawable = this.f1255q;
            }
            toolbar.setNavigationIcon(drawable);
            return;
        }
        this.f1239a.setNavigationIcon((Drawable) null);
    }

    private void I() {
        Drawable drawable;
        int i10 = this.f1240b;
        if ((i10 & 2) == 0) {
            drawable = null;
        } else if ((i10 & 1) != 0) {
            drawable = this.f1244f;
            if (drawable == null) {
                drawable = this.f1243e;
            }
        } else {
            drawable = this.f1243e;
        }
        this.f1239a.setLogo(drawable);
    }

    private int w() {
        if (this.f1239a.getNavigationIcon() == null) {
            return 11;
        }
        this.f1255q = this.f1239a.getNavigationIcon();
        return 15;
    }

    public void A(int i10) {
        B(i10 == 0 ? null : getContext().getString(i10));
    }

    public void B(CharSequence charSequence) {
        this.f1249k = charSequence;
        G();
    }

    public void C(Drawable drawable) {
        this.f1245g = drawable;
        H();
    }

    public void D(CharSequence charSequence) {
        this.f1248j = charSequence;
        if ((this.f1240b & 8) != 0) {
            this.f1239a.setSubtitle(charSequence);
        }
    }

    public void E(CharSequence charSequence) {
        this.f1246h = true;
        F(charSequence);
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public void a(Menu menu, MenuPresenter.a aVar) {
        if (this.f1252n == null) {
            ActionMenuPresenter actionMenuPresenter = new ActionMenuPresenter(this.f1239a.getContext());
            this.f1252n = actionMenuPresenter;
            actionMenuPresenter.i(R$id.action_menu_presenter);
        }
        this.f1252n.setCallback(aVar);
        this.f1239a.setMenu((MenuBuilder) menu, this.f1252n);
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public boolean b() {
        return this.f1239a.isOverflowMenuShowing();
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public void c() {
        this.f1251m = true;
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public void collapseActionView() {
        this.f1239a.collapseActionView();
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public boolean d() {
        return this.f1239a.canShowOverflowMenu();
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public boolean e() {
        return this.f1239a.isOverflowMenuShowPending();
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public boolean f() {
        return this.f1239a.hideOverflowMenu();
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public boolean g() {
        return this.f1239a.showOverflowMenu();
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public Context getContext() {
        return this.f1239a.getContext();
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public CharSequence getTitle() {
        return this.f1239a.getTitle();
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public void h() {
        this.f1239a.dismissPopupMenus();
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public void i(ScrollingTabContainerView scrollingTabContainerView) {
        View view = this.f1241c;
        if (view != null) {
            ViewParent parent = view.getParent();
            Toolbar toolbar = this.f1239a;
            if (parent == toolbar) {
                toolbar.removeView(this.f1241c);
            }
        }
        this.f1241c = scrollingTabContainerView;
        if (scrollingTabContainerView == null || this.f1253o != 2) {
            return;
        }
        this.f1239a.addView(scrollingTabContainerView, 0);
        Toolbar.LayoutParams layoutParams = (Toolbar.LayoutParams) this.f1241c.getLayoutParams();
        ((ViewGroup.MarginLayoutParams) layoutParams).width = -2;
        ((ViewGroup.MarginLayoutParams) layoutParams).height = -2;
        layoutParams.f320a = 8388691;
        scrollingTabContainerView.setAllowCollapse(true);
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public boolean j() {
        return this.f1239a.hasExpandedActionView();
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public void k(int i10) {
        View view;
        int i11 = this.f1240b ^ i10;
        this.f1240b = i10;
        if (i11 != 0) {
            if ((i11 & 4) != 0) {
                if ((i10 & 4) != 0) {
                    G();
                }
                H();
            }
            if ((i11 & 3) != 0) {
                I();
            }
            if ((i11 & 8) != 0) {
                if ((i10 & 8) != 0) {
                    this.f1239a.setTitle(this.f1247i);
                    this.f1239a.setSubtitle(this.f1248j);
                } else {
                    this.f1239a.setTitle((CharSequence) null);
                    this.f1239a.setSubtitle((CharSequence) null);
                }
            }
            if ((i11 & 16) == 0 || (view = this.f1242d) == null) {
                return;
            }
            if ((i10 & 16) != 0) {
                this.f1239a.addView(view);
            } else {
                this.f1239a.removeView(view);
            }
        }
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public Menu l() {
        return this.f1239a.getMenu();
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public void m(int i10) {
        z(i10 != 0 ? AppCompatResources.b(getContext(), i10) : null);
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public int n() {
        return this.f1253o;
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public ViewPropertyAnimatorCompat o(int i10, long j10) {
        return ViewCompat.d(this.f1239a).b(i10 == 0 ? 1.0f : 0.0f).g(j10).i(new b(i10));
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public void p(MenuPresenter.a aVar, MenuBuilder.a aVar2) {
        this.f1239a.setMenuCallbacks(aVar, aVar2);
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public ViewGroup q() {
        return this.f1239a;
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public void r(boolean z10) {
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public int s() {
        return this.f1240b;
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public void setIcon(int i10) {
        setIcon(i10 != 0 ? AppCompatResources.b(getContext(), i10) : null);
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public void setVisibility(int i10) {
        this.f1239a.setVisibility(i10);
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public void setWindowCallback(Window.Callback callback) {
        this.f1250l = callback;
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public void setWindowTitle(CharSequence charSequence) {
        if (this.f1246h) {
            return;
        }
        F(charSequence);
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public void t() {
        Log.i("ToolbarWidgetWrapper", "Progress display unsupported");
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public void u() {
        Log.i("ToolbarWidgetWrapper", "Progress display unsupported");
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public void v(boolean z10) {
        this.f1239a.setCollapsible(z10);
    }

    public void x(View view) {
        View view2 = this.f1242d;
        if (view2 != null && (this.f1240b & 16) != 0) {
            this.f1239a.removeView(view2);
        }
        this.f1242d = view;
        if (view == null || (this.f1240b & 16) == 0) {
            return;
        }
        this.f1239a.addView(view);
    }

    public void y(int i10) {
        if (i10 == this.f1254p) {
            return;
        }
        this.f1254p = i10;
        if (TextUtils.isEmpty(this.f1239a.getNavigationContentDescription())) {
            A(this.f1254p);
        }
    }

    public void z(Drawable drawable) {
        this.f1244f = drawable;
        I();
    }

    public ToolbarWidgetWrapper(Toolbar toolbar, boolean z10, int i10, int i11) {
        Drawable drawable;
        this.f1253o = 0;
        this.f1254p = 0;
        this.f1239a = toolbar;
        this.f1247i = toolbar.getTitle();
        this.f1248j = toolbar.getSubtitle();
        this.f1246h = this.f1247i != null;
        this.f1245g = toolbar.getNavigationIcon();
        TintTypedArray w10 = TintTypedArray.w(toolbar.getContext(), null, R$styleable.ActionBar, R$attr.actionBarStyle, 0);
        this.f1255q = w10.g(R$styleable.ActionBar_homeAsUpIndicator);
        if (z10) {
            CharSequence p10 = w10.p(R$styleable.ActionBar_title);
            if (!TextUtils.isEmpty(p10)) {
                E(p10);
            }
            CharSequence p11 = w10.p(R$styleable.ActionBar_subtitle);
            if (!TextUtils.isEmpty(p11)) {
                D(p11);
            }
            Drawable g6 = w10.g(R$styleable.ActionBar_logo);
            if (g6 != null) {
                z(g6);
            }
            Drawable g10 = w10.g(R$styleable.ActionBar_icon);
            if (g10 != null) {
                setIcon(g10);
            }
            if (this.f1245g == null && (drawable = this.f1255q) != null) {
                C(drawable);
            }
            k(w10.k(R$styleable.ActionBar_displayOptions, 0));
            int n10 = w10.n(R$styleable.ActionBar_customNavigationLayout, 0);
            if (n10 != 0) {
                x(LayoutInflater.from(this.f1239a.getContext()).inflate(n10, (ViewGroup) this.f1239a, false));
                k(this.f1240b | 16);
            }
            int m10 = w10.m(R$styleable.ActionBar_height, 0);
            if (m10 > 0) {
                ViewGroup.LayoutParams layoutParams = this.f1239a.getLayoutParams();
                layoutParams.height = m10;
                this.f1239a.setLayoutParams(layoutParams);
            }
            int e10 = w10.e(R$styleable.ActionBar_contentInsetStart, -1);
            int e11 = w10.e(R$styleable.ActionBar_contentInsetEnd, -1);
            if (e10 >= 0 || e11 >= 0) {
                this.f1239a.setContentInsetsRelative(Math.max(e10, 0), Math.max(e11, 0));
            }
            int n11 = w10.n(R$styleable.ActionBar_titleTextStyle, 0);
            if (n11 != 0) {
                Toolbar toolbar2 = this.f1239a;
                toolbar2.setTitleTextAppearance(toolbar2.getContext(), n11);
            }
            int n12 = w10.n(R$styleable.ActionBar_subtitleTextStyle, 0);
            if (n12 != 0) {
                Toolbar toolbar3 = this.f1239a;
                toolbar3.setSubtitleTextAppearance(toolbar3.getContext(), n12);
            }
            int n13 = w10.n(R$styleable.ActionBar_popupTheme, 0);
            if (n13 != 0) {
                this.f1239a.setPopupTheme(n13);
            }
        } else {
            this.f1240b = w();
        }
        w10.x();
        y(i10);
        this.f1249k = this.f1239a.getNavigationContentDescription();
        this.f1239a.setNavigationOnClickListener(new a());
    }

    @Override // androidx.appcompat.widget.DecorToolbar
    public void setIcon(Drawable drawable) {
        this.f1243e = drawable;
        I();
    }
}
