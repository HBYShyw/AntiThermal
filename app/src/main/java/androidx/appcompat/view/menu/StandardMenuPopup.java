package androidx.appcompat.view.menu;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.R$dimen;
import androidx.appcompat.R$layout;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.widget.MenuPopupWindow;
import androidx.core.view.ViewCompat;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: StandardMenuPopup.java */
/* renamed from: androidx.appcompat.view.menu.q, reason: use source file name */
/* loaded from: classes.dex */
public final class StandardMenuPopup extends MenuPopup implements PopupWindow.OnDismissListener, View.OnKeyListener {

    /* renamed from: z, reason: collision with root package name */
    private static final int f794z = R$layout.abc_popup_menu_item_layout;

    /* renamed from: f, reason: collision with root package name */
    private final Context f795f;

    /* renamed from: g, reason: collision with root package name */
    private final MenuBuilder f796g;

    /* renamed from: h, reason: collision with root package name */
    private final MenuAdapter f797h;

    /* renamed from: i, reason: collision with root package name */
    private final boolean f798i;

    /* renamed from: j, reason: collision with root package name */
    private final int f799j;

    /* renamed from: k, reason: collision with root package name */
    private final int f800k;

    /* renamed from: l, reason: collision with root package name */
    private final int f801l;

    /* renamed from: m, reason: collision with root package name */
    final MenuPopupWindow f802m;

    /* renamed from: p, reason: collision with root package name */
    private PopupWindow.OnDismissListener f805p;

    /* renamed from: q, reason: collision with root package name */
    private View f806q;

    /* renamed from: r, reason: collision with root package name */
    View f807r;

    /* renamed from: s, reason: collision with root package name */
    private MenuPresenter.a f808s;

    /* renamed from: t, reason: collision with root package name */
    ViewTreeObserver f809t;

    /* renamed from: u, reason: collision with root package name */
    private boolean f810u;

    /* renamed from: v, reason: collision with root package name */
    private boolean f811v;

    /* renamed from: w, reason: collision with root package name */
    private int f812w;

    /* renamed from: y, reason: collision with root package name */
    private boolean f814y;

    /* renamed from: n, reason: collision with root package name */
    final ViewTreeObserver.OnGlobalLayoutListener f803n = new a();

    /* renamed from: o, reason: collision with root package name */
    private final View.OnAttachStateChangeListener f804o = new b();

    /* renamed from: x, reason: collision with root package name */
    private int f813x = 0;

    /* compiled from: StandardMenuPopup.java */
    /* renamed from: androidx.appcompat.view.menu.q$a */
    /* loaded from: classes.dex */
    class a implements ViewTreeObserver.OnGlobalLayoutListener {
        a() {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (!StandardMenuPopup.this.a() || StandardMenuPopup.this.f802m.B()) {
                return;
            }
            View view = StandardMenuPopup.this.f807r;
            if (view != null && view.isShown()) {
                StandardMenuPopup.this.f802m.b();
            } else {
                StandardMenuPopup.this.dismiss();
            }
        }
    }

    /* compiled from: StandardMenuPopup.java */
    /* renamed from: androidx.appcompat.view.menu.q$b */
    /* loaded from: classes.dex */
    class b implements View.OnAttachStateChangeListener {
        b() {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            ViewTreeObserver viewTreeObserver = StandardMenuPopup.this.f809t;
            if (viewTreeObserver != null) {
                if (!viewTreeObserver.isAlive()) {
                    StandardMenuPopup.this.f809t = view.getViewTreeObserver();
                }
                StandardMenuPopup standardMenuPopup = StandardMenuPopup.this;
                standardMenuPopup.f809t.removeGlobalOnLayoutListener(standardMenuPopup.f803n);
            }
            view.removeOnAttachStateChangeListener(this);
        }
    }

    public StandardMenuPopup(Context context, MenuBuilder menuBuilder, View view, int i10, int i11, boolean z10) {
        this.f795f = context;
        this.f796g = menuBuilder;
        this.f798i = z10;
        this.f797h = new MenuAdapter(menuBuilder, LayoutInflater.from(context), z10, f794z);
        this.f800k = i10;
        this.f801l = i11;
        Resources resources = context.getResources();
        this.f799j = Math.max(resources.getDisplayMetrics().widthPixels / 2, resources.getDimensionPixelSize(R$dimen.abc_config_prefDialogWidth));
        this.f806q = view;
        this.f802m = new MenuPopupWindow(context, null, i10, i11);
        menuBuilder.addMenuPresenter(this, context);
    }

    private boolean r() {
        View view;
        if (a()) {
            return true;
        }
        if (this.f810u || (view = this.f806q) == null) {
            return false;
        }
        this.f807r = view;
        this.f802m.K(this);
        this.f802m.L(this);
        this.f802m.J(true);
        View view2 = this.f807r;
        boolean z10 = this.f809t == null;
        ViewTreeObserver viewTreeObserver = view2.getViewTreeObserver();
        this.f809t = viewTreeObserver;
        if (z10) {
            viewTreeObserver.addOnGlobalLayoutListener(this.f803n);
        }
        view2.addOnAttachStateChangeListener(this.f804o);
        this.f802m.D(view2);
        this.f802m.G(this.f813x);
        if (!this.f811v) {
            this.f812w = MenuPopup.f(this.f797h, null, this.f795f, this.f799j);
            this.f811v = true;
        }
        this.f802m.F(this.f812w);
        this.f802m.I(2);
        this.f802m.H(e());
        this.f802m.b();
        ListView j10 = this.f802m.j();
        j10.setOnKeyListener(this);
        if (this.f814y && this.f796g.getHeaderTitle() != null) {
            FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(this.f795f).inflate(R$layout.abc_popup_menu_header_item_layout, (ViewGroup) j10, false);
            TextView textView = (TextView) frameLayout.findViewById(R.id.title);
            if (textView != null) {
                textView.setText(this.f796g.getHeaderTitle());
            }
            frameLayout.setEnabled(false);
            j10.addHeaderView(frameLayout, null, false);
        }
        this.f802m.p(this.f797h);
        this.f802m.b();
        return true;
    }

    @Override // androidx.appcompat.view.menu.ShowableListMenu
    public boolean a() {
        return !this.f810u && this.f802m.a();
    }

    @Override // androidx.appcompat.view.menu.ShowableListMenu
    public void b() {
        if (!r()) {
            throw new IllegalStateException("StandardMenuPopup cannot be used without an anchor");
        }
    }

    @Override // androidx.appcompat.view.menu.MenuPopup
    public void c(MenuBuilder menuBuilder) {
    }

    @Override // androidx.appcompat.view.menu.ShowableListMenu
    public void dismiss() {
        if (a()) {
            this.f802m.dismiss();
        }
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public boolean flagActionItems() {
        return false;
    }

    @Override // androidx.appcompat.view.menu.MenuPopup
    public void g(View view) {
        this.f806q = view;
    }

    @Override // androidx.appcompat.view.menu.MenuPopup
    public void i(boolean z10) {
        this.f797h.d(z10);
    }

    @Override // androidx.appcompat.view.menu.ShowableListMenu
    public ListView j() {
        return this.f802m.j();
    }

    @Override // androidx.appcompat.view.menu.MenuPopup
    public void k(int i10) {
        this.f813x = i10;
    }

    @Override // androidx.appcompat.view.menu.MenuPopup
    public void l(int i10) {
        this.f802m.f(i10);
    }

    @Override // androidx.appcompat.view.menu.MenuPopup
    public void m(PopupWindow.OnDismissListener onDismissListener) {
        this.f805p = onDismissListener;
    }

    @Override // androidx.appcompat.view.menu.MenuPopup
    public void n(boolean z10) {
        this.f814y = z10;
    }

    @Override // androidx.appcompat.view.menu.MenuPopup
    public void o(int i10) {
        this.f802m.l(i10);
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public void onCloseMenu(MenuBuilder menuBuilder, boolean z10) {
        if (menuBuilder != this.f796g) {
            return;
        }
        dismiss();
        MenuPresenter.a aVar = this.f808s;
        if (aVar != null) {
            aVar.onCloseMenu(menuBuilder, z10);
        }
    }

    @Override // android.widget.PopupWindow.OnDismissListener
    public void onDismiss() {
        this.f810u = true;
        this.f796g.close();
        ViewTreeObserver viewTreeObserver = this.f809t;
        if (viewTreeObserver != null) {
            if (!viewTreeObserver.isAlive()) {
                this.f809t = this.f807r.getViewTreeObserver();
            }
            this.f809t.removeGlobalOnLayoutListener(this.f803n);
            this.f809t = null;
        }
        this.f807r.removeOnAttachStateChangeListener(this.f804o);
        PopupWindow.OnDismissListener onDismissListener = this.f805p;
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
        }
    }

    @Override // android.view.View.OnKeyListener
    public boolean onKey(View view, int i10, KeyEvent keyEvent) {
        if (keyEvent.getAction() != 1 || i10 != 82) {
            return false;
        }
        dismiss();
        return true;
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
        if (subMenuBuilder.hasVisibleItems()) {
            MenuPopupHelper menuPopupHelper = new MenuPopupHelper(this.f795f, subMenuBuilder, this.f807r, this.f798i, this.f800k, this.f801l);
            menuPopupHelper.j(this.f808s);
            menuPopupHelper.g(MenuPopup.p(subMenuBuilder));
            menuPopupHelper.i(this.f805p);
            this.f805p = null;
            this.f796g.close(false);
            int d10 = this.f802m.d();
            int o10 = this.f802m.o();
            if ((Gravity.getAbsoluteGravity(this.f813x, ViewCompat.x(this.f806q)) & 7) == 5) {
                d10 += this.f806q.getWidth();
            }
            if (menuPopupHelper.n(d10, o10)) {
                MenuPresenter.a aVar = this.f808s;
                if (aVar == null) {
                    return true;
                }
                aVar.a(subMenuBuilder);
                return true;
            }
        }
        return false;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public void setCallback(MenuPresenter.a aVar) {
        this.f808s = aVar;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public void updateMenuView(boolean z10) {
        this.f811v = false;
        MenuAdapter menuAdapter = this.f797h;
        if (menuAdapter != null) {
            menuAdapter.notifyDataSetChanged();
        }
    }
}
