package androidx.appcompat.app;

import android.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import androidx.appcompat.R$attr;
import androidx.appcompat.R$id;
import androidx.appcompat.R$styleable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.view.ActionBarPolicy;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.ViewPropertyAnimatorCompatSet;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.ActionBarContainer;
import androidx.appcompat.widget.ActionBarContextView;
import androidx.appcompat.widget.ActionBarOverlayLayout;
import androidx.appcompat.widget.DecorToolbar;
import androidx.appcompat.widget.ScrollingTabContainerView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;
import androidx.core.view.ViewPropertyAnimatorUpdateListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/* compiled from: WindowDecorActionBar.java */
/* renamed from: androidx.appcompat.app.q, reason: use source file name */
/* loaded from: classes.dex */
public class WindowDecorActionBar extends ActionBar implements ActionBarOverlayLayout.d {
    private static final Interpolator E = new AccelerateInterpolator();
    private static final Interpolator F = new DecelerateInterpolator();
    boolean A;

    /* renamed from: a, reason: collision with root package name */
    Context f522a;

    /* renamed from: b, reason: collision with root package name */
    private Context f523b;

    /* renamed from: c, reason: collision with root package name */
    private Activity f524c;

    /* renamed from: d, reason: collision with root package name */
    ActionBarOverlayLayout f525d;

    /* renamed from: e, reason: collision with root package name */
    ActionBarContainer f526e;

    /* renamed from: f, reason: collision with root package name */
    DecorToolbar f527f;

    /* renamed from: g, reason: collision with root package name */
    ActionBarContextView f528g;

    /* renamed from: h, reason: collision with root package name */
    View f529h;

    /* renamed from: i, reason: collision with root package name */
    ScrollingTabContainerView f530i;

    /* renamed from: l, reason: collision with root package name */
    private boolean f533l;

    /* renamed from: m, reason: collision with root package name */
    d f534m;

    /* renamed from: n, reason: collision with root package name */
    ActionMode f535n;

    /* renamed from: o, reason: collision with root package name */
    ActionMode.a f536o;

    /* renamed from: p, reason: collision with root package name */
    private boolean f537p;

    /* renamed from: r, reason: collision with root package name */
    private boolean f539r;

    /* renamed from: u, reason: collision with root package name */
    boolean f542u;

    /* renamed from: v, reason: collision with root package name */
    boolean f543v;

    /* renamed from: w, reason: collision with root package name */
    private boolean f544w;

    /* renamed from: y, reason: collision with root package name */
    ViewPropertyAnimatorCompatSet f546y;

    /* renamed from: z, reason: collision with root package name */
    private boolean f547z;

    /* renamed from: j, reason: collision with root package name */
    private ArrayList<Object> f531j = new ArrayList<>();

    /* renamed from: k, reason: collision with root package name */
    private int f532k = -1;

    /* renamed from: q, reason: collision with root package name */
    private ArrayList<ActionBar.a> f538q = new ArrayList<>();

    /* renamed from: s, reason: collision with root package name */
    private int f540s = 0;

    /* renamed from: t, reason: collision with root package name */
    boolean f541t = true;

    /* renamed from: x, reason: collision with root package name */
    private boolean f545x = true;
    final ViewPropertyAnimatorListener B = new a();
    final ViewPropertyAnimatorListener C = new b();
    final ViewPropertyAnimatorUpdateListener D = new c();

    /* compiled from: WindowDecorActionBar.java */
    /* renamed from: androidx.appcompat.app.q$a */
    /* loaded from: classes.dex */
    class a extends ViewPropertyAnimatorListenerAdapter {
        a() {
        }

        @Override // androidx.core.view.ViewPropertyAnimatorListener
        public void b(View view) {
            View view2;
            WindowDecorActionBar windowDecorActionBar = WindowDecorActionBar.this;
            if (windowDecorActionBar.f541t && (view2 = windowDecorActionBar.f529h) != null) {
                view2.setTranslationY(0.0f);
                WindowDecorActionBar.this.f526e.setTranslationY(0.0f);
            }
            WindowDecorActionBar.this.f526e.setVisibility(8);
            WindowDecorActionBar.this.f526e.setTransitioning(false);
            WindowDecorActionBar windowDecorActionBar2 = WindowDecorActionBar.this;
            windowDecorActionBar2.f546y = null;
            windowDecorActionBar2.y();
            ActionBarOverlayLayout actionBarOverlayLayout = WindowDecorActionBar.this.f525d;
            if (actionBarOverlayLayout != null) {
                ViewCompat.h0(actionBarOverlayLayout);
            }
        }
    }

    /* compiled from: WindowDecorActionBar.java */
    /* renamed from: androidx.appcompat.app.q$b */
    /* loaded from: classes.dex */
    class b extends ViewPropertyAnimatorListenerAdapter {
        b() {
        }

        @Override // androidx.core.view.ViewPropertyAnimatorListener
        public void b(View view) {
            WindowDecorActionBar windowDecorActionBar = WindowDecorActionBar.this;
            windowDecorActionBar.f546y = null;
            windowDecorActionBar.f526e.requestLayout();
        }
    }

    /* compiled from: WindowDecorActionBar.java */
    /* renamed from: androidx.appcompat.app.q$c */
    /* loaded from: classes.dex */
    class c implements ViewPropertyAnimatorUpdateListener {
        c() {
        }

        @Override // androidx.core.view.ViewPropertyAnimatorUpdateListener
        public void a(View view) {
            ((View) WindowDecorActionBar.this.f526e.getParent()).invalidate();
        }
    }

    /* compiled from: WindowDecorActionBar.java */
    /* renamed from: androidx.appcompat.app.q$d */
    /* loaded from: classes.dex */
    public class d extends ActionMode implements MenuBuilder.a {

        /* renamed from: g, reason: collision with root package name */
        private final Context f551g;

        /* renamed from: h, reason: collision with root package name */
        private final MenuBuilder f552h;

        /* renamed from: i, reason: collision with root package name */
        private ActionMode.a f553i;

        /* renamed from: j, reason: collision with root package name */
        private WeakReference<View> f554j;

        public d(Context context, ActionMode.a aVar) {
            this.f551g = context;
            this.f553i = aVar;
            MenuBuilder defaultShowAsAction = new MenuBuilder(context).setDefaultShowAsAction(1);
            this.f552h = defaultShowAsAction;
            defaultShowAsAction.setCallback(this);
        }

        @Override // androidx.appcompat.view.menu.MenuBuilder.a
        public boolean a(MenuBuilder menuBuilder, MenuItem menuItem) {
            ActionMode.a aVar = this.f553i;
            if (aVar != null) {
                return aVar.d(this, menuItem);
            }
            return false;
        }

        @Override // androidx.appcompat.view.menu.MenuBuilder.a
        public void b(MenuBuilder menuBuilder) {
            if (this.f553i == null) {
                return;
            }
            k();
            WindowDecorActionBar.this.f528g.l();
        }

        @Override // androidx.appcompat.view.ActionMode
        public void c() {
            WindowDecorActionBar windowDecorActionBar = WindowDecorActionBar.this;
            if (windowDecorActionBar.f534m != this) {
                return;
            }
            if (!WindowDecorActionBar.x(windowDecorActionBar.f542u, windowDecorActionBar.f543v, false)) {
                WindowDecorActionBar windowDecorActionBar2 = WindowDecorActionBar.this;
                windowDecorActionBar2.f535n = this;
                windowDecorActionBar2.f536o = this.f553i;
            } else {
                this.f553i.a(this);
            }
            this.f553i = null;
            WindowDecorActionBar.this.w(false);
            WindowDecorActionBar.this.f528g.g();
            WindowDecorActionBar windowDecorActionBar3 = WindowDecorActionBar.this;
            windowDecorActionBar3.f525d.setHideOnContentScrollEnabled(windowDecorActionBar3.A);
            WindowDecorActionBar.this.f534m = null;
        }

        @Override // androidx.appcompat.view.ActionMode
        public View d() {
            WeakReference<View> weakReference = this.f554j;
            if (weakReference != null) {
                return weakReference.get();
            }
            return null;
        }

        @Override // androidx.appcompat.view.ActionMode
        public Menu e() {
            return this.f552h;
        }

        @Override // androidx.appcompat.view.ActionMode
        public MenuInflater f() {
            return new SupportMenuInflater(this.f551g);
        }

        @Override // androidx.appcompat.view.ActionMode
        public CharSequence g() {
            return WindowDecorActionBar.this.f528g.getSubtitle();
        }

        @Override // androidx.appcompat.view.ActionMode
        public CharSequence i() {
            return WindowDecorActionBar.this.f528g.getTitle();
        }

        @Override // androidx.appcompat.view.ActionMode
        public void k() {
            if (WindowDecorActionBar.this.f534m != this) {
                return;
            }
            this.f552h.stopDispatchingItemsChanged();
            try {
                this.f553i.c(this, this.f552h);
            } finally {
                this.f552h.startDispatchingItemsChanged();
            }
        }

        @Override // androidx.appcompat.view.ActionMode
        public boolean l() {
            return WindowDecorActionBar.this.f528g.j();
        }

        @Override // androidx.appcompat.view.ActionMode
        public void m(View view) {
            WindowDecorActionBar.this.f528g.setCustomView(view);
            this.f554j = new WeakReference<>(view);
        }

        @Override // androidx.appcompat.view.ActionMode
        public void n(int i10) {
            o(WindowDecorActionBar.this.f522a.getResources().getString(i10));
        }

        @Override // androidx.appcompat.view.ActionMode
        public void o(CharSequence charSequence) {
            WindowDecorActionBar.this.f528g.setSubtitle(charSequence);
        }

        @Override // androidx.appcompat.view.ActionMode
        public void q(int i10) {
            r(WindowDecorActionBar.this.f522a.getResources().getString(i10));
        }

        @Override // androidx.appcompat.view.ActionMode
        public void r(CharSequence charSequence) {
            WindowDecorActionBar.this.f528g.setTitle(charSequence);
        }

        @Override // androidx.appcompat.view.ActionMode
        public void s(boolean z10) {
            super.s(z10);
            WindowDecorActionBar.this.f528g.setTitleOptional(z10);
        }

        public boolean t() {
            this.f552h.stopDispatchingItemsChanged();
            try {
                return this.f553i.b(this, this.f552h);
            } finally {
                this.f552h.startDispatchingItemsChanged();
            }
        }
    }

    public WindowDecorActionBar(Activity activity, boolean z10) {
        this.f524c = activity;
        View decorView = activity.getWindow().getDecorView();
        E(decorView);
        if (z10) {
            return;
        }
        this.f529h = decorView.findViewById(R.id.content);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private DecorToolbar B(View view) {
        if (view instanceof DecorToolbar) {
            return (DecorToolbar) view;
        }
        if (view instanceof Toolbar) {
            return ((Toolbar) view).getWrapper();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Can't make a decor toolbar out of ");
        sb2.append(view != 0 ? view.getClass().getSimpleName() : "null");
        throw new IllegalStateException(sb2.toString());
    }

    private void D() {
        if (this.f544w) {
            this.f544w = false;
            ActionBarOverlayLayout actionBarOverlayLayout = this.f525d;
            if (actionBarOverlayLayout != null) {
                actionBarOverlayLayout.setShowingForActionMode(false);
            }
            M(false);
        }
    }

    private void E(View view) {
        ActionBarOverlayLayout actionBarOverlayLayout = (ActionBarOverlayLayout) view.findViewById(R$id.decor_content_parent);
        this.f525d = actionBarOverlayLayout;
        if (actionBarOverlayLayout != null) {
            actionBarOverlayLayout.setActionBarVisibilityCallback(this);
        }
        this.f527f = B(view.findViewById(R$id.action_bar));
        this.f528g = (ActionBarContextView) view.findViewById(R$id.action_context_bar);
        ActionBarContainer actionBarContainer = (ActionBarContainer) view.findViewById(R$id.action_bar_container);
        this.f526e = actionBarContainer;
        DecorToolbar decorToolbar = this.f527f;
        if (decorToolbar != null && this.f528g != null && actionBarContainer != null) {
            this.f522a = decorToolbar.getContext();
            boolean z10 = (this.f527f.s() & 4) != 0;
            if (z10) {
                this.f533l = true;
            }
            ActionBarPolicy b10 = ActionBarPolicy.b(this.f522a);
            J(b10.a() || z10);
            H(b10.g());
            TypedArray obtainStyledAttributes = this.f522a.obtainStyledAttributes(null, R$styleable.ActionBar, R$attr.actionBarStyle, 0);
            if (obtainStyledAttributes.getBoolean(R$styleable.ActionBar_hideOnContentScroll, false)) {
                I(true);
            }
            int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.ActionBar_elevation, 0);
            if (dimensionPixelSize != 0) {
                G(dimensionPixelSize);
            }
            obtainStyledAttributes.recycle();
            return;
        }
        throw new IllegalStateException(getClass().getSimpleName() + " can only be used with a compatible window decor layout");
    }

    private void H(boolean z10) {
        this.f539r = z10;
        if (!z10) {
            this.f527f.i(null);
            this.f526e.setTabContainer(this.f530i);
        } else {
            this.f526e.setTabContainer(null);
            this.f527f.i(this.f530i);
        }
        boolean z11 = C() == 2;
        ScrollingTabContainerView scrollingTabContainerView = this.f530i;
        if (scrollingTabContainerView != null) {
            if (z11) {
                scrollingTabContainerView.setVisibility(0);
                ActionBarOverlayLayout actionBarOverlayLayout = this.f525d;
                if (actionBarOverlayLayout != null) {
                    ViewCompat.h0(actionBarOverlayLayout);
                }
            } else {
                scrollingTabContainerView.setVisibility(8);
            }
        }
        this.f527f.v(!this.f539r && z11);
        this.f525d.setHasNonEmbeddedTabs(!this.f539r && z11);
    }

    private boolean K() {
        return ViewCompat.Q(this.f526e);
    }

    private void L() {
        if (this.f544w) {
            return;
        }
        this.f544w = true;
        ActionBarOverlayLayout actionBarOverlayLayout = this.f525d;
        if (actionBarOverlayLayout != null) {
            actionBarOverlayLayout.setShowingForActionMode(true);
        }
        M(false);
    }

    private void M(boolean z10) {
        if (x(this.f542u, this.f543v, this.f544w)) {
            if (this.f545x) {
                return;
            }
            this.f545x = true;
            A(z10);
            return;
        }
        if (this.f545x) {
            this.f545x = false;
            z(z10);
        }
    }

    static boolean x(boolean z10, boolean z11, boolean z12) {
        if (z12) {
            return true;
        }
        return (z10 || z11) ? false : true;
    }

    public void A(boolean z10) {
        View view;
        View view2;
        ViewPropertyAnimatorCompatSet viewPropertyAnimatorCompatSet = this.f546y;
        if (viewPropertyAnimatorCompatSet != null) {
            viewPropertyAnimatorCompatSet.a();
        }
        this.f526e.setVisibility(0);
        if (this.f540s == 0 && (this.f547z || z10)) {
            this.f526e.setTranslationY(0.0f);
            float f10 = -this.f526e.getHeight();
            if (z10) {
                this.f526e.getLocationInWindow(new int[]{0, 0});
                f10 -= r5[1];
            }
            this.f526e.setTranslationY(f10);
            ViewPropertyAnimatorCompatSet viewPropertyAnimatorCompatSet2 = new ViewPropertyAnimatorCompatSet();
            ViewPropertyAnimatorCompat n10 = ViewCompat.d(this.f526e).n(0.0f);
            n10.l(this.D);
            viewPropertyAnimatorCompatSet2.c(n10);
            if (this.f541t && (view2 = this.f529h) != null) {
                view2.setTranslationY(f10);
                viewPropertyAnimatorCompatSet2.c(ViewCompat.d(this.f529h).n(0.0f));
            }
            viewPropertyAnimatorCompatSet2.f(F);
            viewPropertyAnimatorCompatSet2.e(250L);
            viewPropertyAnimatorCompatSet2.g(this.C);
            this.f546y = viewPropertyAnimatorCompatSet2;
            viewPropertyAnimatorCompatSet2.h();
        } else {
            this.f526e.setAlpha(1.0f);
            this.f526e.setTranslationY(0.0f);
            if (this.f541t && (view = this.f529h) != null) {
                view.setTranslationY(0.0f);
            }
            this.C.b(null);
        }
        ActionBarOverlayLayout actionBarOverlayLayout = this.f525d;
        if (actionBarOverlayLayout != null) {
            ViewCompat.h0(actionBarOverlayLayout);
        }
    }

    public int C() {
        return this.f527f.n();
    }

    public void F(int i10, int i11) {
        int s7 = this.f527f.s();
        if ((i11 & 4) != 0) {
            this.f533l = true;
        }
        this.f527f.k((i10 & i11) | ((~i11) & s7));
    }

    public void G(float f10) {
        ViewCompat.t0(this.f526e, f10);
    }

    public void I(boolean z10) {
        if (z10 && !this.f525d.w()) {
            throw new IllegalStateException("Action bar must be in overlay mode (Window.FEATURE_OVERLAY_ACTION_BAR) to enable hide on content scroll");
        }
        this.A = z10;
        this.f525d.setHideOnContentScrollEnabled(z10);
    }

    public void J(boolean z10) {
        this.f527f.r(z10);
    }

    @Override // androidx.appcompat.widget.ActionBarOverlayLayout.d
    public void a() {
        if (this.f543v) {
            this.f543v = false;
            M(true);
        }
    }

    @Override // androidx.appcompat.widget.ActionBarOverlayLayout.d
    public void b() {
    }

    @Override // androidx.appcompat.widget.ActionBarOverlayLayout.d
    public void c(boolean z10) {
        this.f541t = z10;
    }

    @Override // androidx.appcompat.widget.ActionBarOverlayLayout.d
    public void d() {
        if (this.f543v) {
            return;
        }
        this.f543v = true;
        M(true);
    }

    @Override // androidx.appcompat.widget.ActionBarOverlayLayout.d
    public void e() {
        ViewPropertyAnimatorCompatSet viewPropertyAnimatorCompatSet = this.f546y;
        if (viewPropertyAnimatorCompatSet != null) {
            viewPropertyAnimatorCompatSet.a();
            this.f546y = null;
        }
    }

    @Override // androidx.appcompat.widget.ActionBarOverlayLayout.d
    public void f(int i10) {
        this.f540s = i10;
    }

    @Override // androidx.appcompat.app.ActionBar
    public boolean h() {
        DecorToolbar decorToolbar = this.f527f;
        if (decorToolbar == null || !decorToolbar.j()) {
            return false;
        }
        this.f527f.collapseActionView();
        return true;
    }

    @Override // androidx.appcompat.app.ActionBar
    public void i(boolean z10) {
        if (z10 == this.f537p) {
            return;
        }
        this.f537p = z10;
        int size = this.f538q.size();
        for (int i10 = 0; i10 < size; i10++) {
            this.f538q.get(i10).a(z10);
        }
    }

    @Override // androidx.appcompat.app.ActionBar
    public int j() {
        return this.f527f.s();
    }

    @Override // androidx.appcompat.app.ActionBar
    public Context k() {
        if (this.f523b == null) {
            TypedValue typedValue = new TypedValue();
            this.f522a.getTheme().resolveAttribute(R$attr.actionBarWidgetTheme, typedValue, true);
            int i10 = typedValue.resourceId;
            if (i10 != 0) {
                this.f523b = new ContextThemeWrapper(this.f522a, i10);
            } else {
                this.f523b = this.f522a;
            }
        }
        return this.f523b;
    }

    @Override // androidx.appcompat.app.ActionBar
    public void m(Configuration configuration) {
        H(ActionBarPolicy.b(this.f522a).g());
    }

    @Override // androidx.appcompat.app.ActionBar
    public boolean o(int i10, KeyEvent keyEvent) {
        Menu e10;
        d dVar = this.f534m;
        if (dVar == null || (e10 = dVar.e()) == null) {
            return false;
        }
        e10.setQwertyMode(KeyCharacterMap.load(keyEvent != null ? keyEvent.getDeviceId() : -1).getKeyboardType() != 1);
        return e10.performShortcut(i10, keyEvent, 0);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void r(boolean z10) {
        if (this.f533l) {
            return;
        }
        s(z10);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void s(boolean z10) {
        F(z10 ? 4 : 0, 4);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void t(boolean z10) {
        ViewPropertyAnimatorCompatSet viewPropertyAnimatorCompatSet;
        this.f547z = z10;
        if (z10 || (viewPropertyAnimatorCompatSet = this.f546y) == null) {
            return;
        }
        viewPropertyAnimatorCompatSet.a();
    }

    @Override // androidx.appcompat.app.ActionBar
    public void u(CharSequence charSequence) {
        this.f527f.setWindowTitle(charSequence);
    }

    @Override // androidx.appcompat.app.ActionBar
    public ActionMode v(ActionMode.a aVar) {
        d dVar = this.f534m;
        if (dVar != null) {
            dVar.c();
        }
        this.f525d.setHideOnContentScrollEnabled(false);
        this.f528g.k();
        d dVar2 = new d(this.f528g.getContext(), aVar);
        if (!dVar2.t()) {
            return null;
        }
        this.f534m = dVar2;
        dVar2.k();
        this.f528g.h(dVar2);
        w(true);
        return dVar2;
    }

    public void w(boolean z10) {
        ViewPropertyAnimatorCompat f10;
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat;
        if (z10) {
            L();
        } else {
            D();
        }
        if (!K()) {
            if (z10) {
                this.f527f.setVisibility(4);
                this.f528g.setVisibility(0);
                return;
            } else {
                this.f527f.setVisibility(0);
                this.f528g.setVisibility(8);
                return;
            }
        }
        if (z10) {
            f10 = this.f527f.o(4, 100L);
            viewPropertyAnimatorCompat = this.f528g.f(0, 200L);
        } else {
            ViewPropertyAnimatorCompat o10 = this.f527f.o(0, 200L);
            f10 = this.f528g.f(8, 100L);
            viewPropertyAnimatorCompat = o10;
        }
        ViewPropertyAnimatorCompatSet viewPropertyAnimatorCompatSet = new ViewPropertyAnimatorCompatSet();
        viewPropertyAnimatorCompatSet.d(f10, viewPropertyAnimatorCompat);
        viewPropertyAnimatorCompatSet.h();
    }

    void y() {
        ActionMode.a aVar = this.f536o;
        if (aVar != null) {
            aVar.a(this.f535n);
            this.f535n = null;
            this.f536o = null;
        }
    }

    public void z(boolean z10) {
        View view;
        ViewPropertyAnimatorCompatSet viewPropertyAnimatorCompatSet = this.f546y;
        if (viewPropertyAnimatorCompatSet != null) {
            viewPropertyAnimatorCompatSet.a();
        }
        if (this.f540s == 0 && (this.f547z || z10)) {
            this.f526e.setAlpha(1.0f);
            this.f526e.setTransitioning(true);
            ViewPropertyAnimatorCompatSet viewPropertyAnimatorCompatSet2 = new ViewPropertyAnimatorCompatSet();
            float f10 = -this.f526e.getHeight();
            if (z10) {
                this.f526e.getLocationInWindow(new int[]{0, 0});
                f10 -= r5[1];
            }
            ViewPropertyAnimatorCompat n10 = ViewCompat.d(this.f526e).n(f10);
            n10.l(this.D);
            viewPropertyAnimatorCompatSet2.c(n10);
            if (this.f541t && (view = this.f529h) != null) {
                viewPropertyAnimatorCompatSet2.c(ViewCompat.d(view).n(f10));
            }
            viewPropertyAnimatorCompatSet2.f(E);
            viewPropertyAnimatorCompatSet2.e(250L);
            viewPropertyAnimatorCompatSet2.g(this.B);
            this.f546y = viewPropertyAnimatorCompatSet2;
            viewPropertyAnimatorCompatSet2.h();
            return;
        }
        this.B.b(null);
    }

    public WindowDecorActionBar(Dialog dialog) {
        E(dialog.getWindow().getDecorView());
    }
}
