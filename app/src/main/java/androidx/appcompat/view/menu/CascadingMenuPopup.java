package androidx.appcompat.view.menu;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.R$dimen;
import androidx.appcompat.R$layout;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.widget.MenuItemHoverListener;
import androidx.appcompat.widget.MenuPopupWindow;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: CascadingMenuPopup.java */
/* renamed from: androidx.appcompat.view.menu.d, reason: use source file name */
/* loaded from: classes.dex */
public final class CascadingMenuPopup extends MenuPopup implements View.OnKeyListener, PopupWindow.OnDismissListener {
    private static final int F = R$layout.abc_cascading_menu_item_layout;
    private boolean A;
    private MenuPresenter.a B;
    ViewTreeObserver C;
    private PopupWindow.OnDismissListener D;
    boolean E;

    /* renamed from: f, reason: collision with root package name */
    private final Context f688f;

    /* renamed from: g, reason: collision with root package name */
    private final int f689g;

    /* renamed from: h, reason: collision with root package name */
    private final int f690h;

    /* renamed from: i, reason: collision with root package name */
    private final int f691i;

    /* renamed from: j, reason: collision with root package name */
    private final boolean f692j;

    /* renamed from: k, reason: collision with root package name */
    final Handler f693k;

    /* renamed from: s, reason: collision with root package name */
    private View f701s;

    /* renamed from: t, reason: collision with root package name */
    View f702t;

    /* renamed from: v, reason: collision with root package name */
    private boolean f704v;

    /* renamed from: w, reason: collision with root package name */
    private boolean f705w;

    /* renamed from: x, reason: collision with root package name */
    private int f706x;

    /* renamed from: y, reason: collision with root package name */
    private int f707y;

    /* renamed from: l, reason: collision with root package name */
    private final List<MenuBuilder> f694l = new ArrayList();

    /* renamed from: m, reason: collision with root package name */
    final List<d> f695m = new ArrayList();

    /* renamed from: n, reason: collision with root package name */
    final ViewTreeObserver.OnGlobalLayoutListener f696n = new a();

    /* renamed from: o, reason: collision with root package name */
    private final View.OnAttachStateChangeListener f697o = new b();

    /* renamed from: p, reason: collision with root package name */
    private final MenuItemHoverListener f698p = new c();

    /* renamed from: q, reason: collision with root package name */
    private int f699q = 0;

    /* renamed from: r, reason: collision with root package name */
    private int f700r = 0;

    /* renamed from: z, reason: collision with root package name */
    private boolean f708z = false;

    /* renamed from: u, reason: collision with root package name */
    private int f703u = v();

    /* compiled from: CascadingMenuPopup.java */
    /* renamed from: androidx.appcompat.view.menu.d$a */
    /* loaded from: classes.dex */
    class a implements ViewTreeObserver.OnGlobalLayoutListener {
        a() {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (!CascadingMenuPopup.this.a() || CascadingMenuPopup.this.f695m.size() <= 0 || CascadingMenuPopup.this.f695m.get(0).f716a.B()) {
                return;
            }
            View view = CascadingMenuPopup.this.f702t;
            if (view != null && view.isShown()) {
                Iterator<d> it = CascadingMenuPopup.this.f695m.iterator();
                while (it.hasNext()) {
                    it.next().f716a.b();
                }
                return;
            }
            CascadingMenuPopup.this.dismiss();
        }
    }

    /* compiled from: CascadingMenuPopup.java */
    /* renamed from: androidx.appcompat.view.menu.d$b */
    /* loaded from: classes.dex */
    class b implements View.OnAttachStateChangeListener {
        b() {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            ViewTreeObserver viewTreeObserver = CascadingMenuPopup.this.C;
            if (viewTreeObserver != null) {
                if (!viewTreeObserver.isAlive()) {
                    CascadingMenuPopup.this.C = view.getViewTreeObserver();
                }
                CascadingMenuPopup cascadingMenuPopup = CascadingMenuPopup.this;
                cascadingMenuPopup.C.removeGlobalOnLayoutListener(cascadingMenuPopup.f696n);
            }
            view.removeOnAttachStateChangeListener(this);
        }
    }

    /* compiled from: CascadingMenuPopup.java */
    /* renamed from: androidx.appcompat.view.menu.d$c */
    /* loaded from: classes.dex */
    class c implements MenuItemHoverListener {

        /* compiled from: CascadingMenuPopup.java */
        /* renamed from: androidx.appcompat.view.menu.d$c$a */
        /* loaded from: classes.dex */
        class a implements Runnable {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ d f712e;

            /* renamed from: f, reason: collision with root package name */
            final /* synthetic */ MenuItem f713f;

            /* renamed from: g, reason: collision with root package name */
            final /* synthetic */ MenuBuilder f714g;

            a(d dVar, MenuItem menuItem, MenuBuilder menuBuilder) {
                this.f712e = dVar;
                this.f713f = menuItem;
                this.f714g = menuBuilder;
            }

            @Override // java.lang.Runnable
            public void run() {
                d dVar = this.f712e;
                if (dVar != null) {
                    CascadingMenuPopup.this.E = true;
                    dVar.f717b.close(false);
                    CascadingMenuPopup.this.E = false;
                }
                if (this.f713f.isEnabled() && this.f713f.hasSubMenu()) {
                    this.f714g.performItemAction(this.f713f, 4);
                }
            }
        }

        c() {
        }

        @Override // androidx.appcompat.widget.MenuItemHoverListener
        public void e(MenuBuilder menuBuilder, MenuItem menuItem) {
            CascadingMenuPopup.this.f693k.removeCallbacksAndMessages(null);
            int size = CascadingMenuPopup.this.f695m.size();
            int i10 = 0;
            while (true) {
                if (i10 >= size) {
                    i10 = -1;
                    break;
                } else if (menuBuilder == CascadingMenuPopup.this.f695m.get(i10).f717b) {
                    break;
                } else {
                    i10++;
                }
            }
            if (i10 == -1) {
                return;
            }
            int i11 = i10 + 1;
            CascadingMenuPopup.this.f693k.postAtTime(new a(i11 < CascadingMenuPopup.this.f695m.size() ? CascadingMenuPopup.this.f695m.get(i11) : null, menuItem, menuBuilder), menuBuilder, SystemClock.uptimeMillis() + 200);
        }

        @Override // androidx.appcompat.widget.MenuItemHoverListener
        public void h(MenuBuilder menuBuilder, MenuItem menuItem) {
            CascadingMenuPopup.this.f693k.removeCallbacksAndMessages(menuBuilder);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: CascadingMenuPopup.java */
    /* renamed from: androidx.appcompat.view.menu.d$d */
    /* loaded from: classes.dex */
    public static class d {

        /* renamed from: a, reason: collision with root package name */
        public final MenuPopupWindow f716a;

        /* renamed from: b, reason: collision with root package name */
        public final MenuBuilder f717b;

        /* renamed from: c, reason: collision with root package name */
        public final int f718c;

        public d(MenuPopupWindow menuPopupWindow, MenuBuilder menuBuilder, int i10) {
            this.f716a = menuPopupWindow;
            this.f717b = menuBuilder;
            this.f718c = i10;
        }

        public ListView a() {
            return this.f716a.j();
        }
    }

    public CascadingMenuPopup(Context context, View view, int i10, int i11, boolean z10) {
        this.f688f = context;
        this.f701s = view;
        this.f690h = i10;
        this.f691i = i11;
        this.f692j = z10;
        Resources resources = context.getResources();
        this.f689g = Math.max(resources.getDisplayMetrics().widthPixels / 2, resources.getDimensionPixelSize(R$dimen.abc_config_prefDialogWidth));
        this.f693k = new Handler();
    }

    private MenuPopupWindow r() {
        MenuPopupWindow menuPopupWindow = new MenuPopupWindow(this.f688f, null, this.f690h, this.f691i);
        menuPopupWindow.T(this.f698p);
        menuPopupWindow.L(this);
        menuPopupWindow.K(this);
        menuPopupWindow.D(this.f701s);
        menuPopupWindow.G(this.f700r);
        menuPopupWindow.J(true);
        menuPopupWindow.I(2);
        return menuPopupWindow;
    }

    private int s(MenuBuilder menuBuilder) {
        int size = this.f695m.size();
        for (int i10 = 0; i10 < size; i10++) {
            if (menuBuilder == this.f695m.get(i10).f717b) {
                return i10;
            }
        }
        return -1;
    }

    private MenuItem t(MenuBuilder menuBuilder, MenuBuilder menuBuilder2) {
        int size = menuBuilder.size();
        for (int i10 = 0; i10 < size; i10++) {
            MenuItem item = menuBuilder.getItem(i10);
            if (item.hasSubMenu() && menuBuilder2 == item.getSubMenu()) {
                return item;
            }
        }
        return null;
    }

    private View u(d dVar, MenuBuilder menuBuilder) {
        MenuAdapter menuAdapter;
        int i10;
        int firstVisiblePosition;
        MenuItem t7 = t(dVar.f717b, menuBuilder);
        if (t7 == null) {
            return null;
        }
        ListView a10 = dVar.a();
        ListAdapter adapter = a10.getAdapter();
        int i11 = 0;
        if (adapter instanceof HeaderViewListAdapter) {
            HeaderViewListAdapter headerViewListAdapter = (HeaderViewListAdapter) adapter;
            i10 = headerViewListAdapter.getHeadersCount();
            menuAdapter = (MenuAdapter) headerViewListAdapter.getWrappedAdapter();
        } else {
            menuAdapter = (MenuAdapter) adapter;
            i10 = 0;
        }
        int count = menuAdapter.getCount();
        while (true) {
            if (i11 >= count) {
                i11 = -1;
                break;
            }
            if (t7 == menuAdapter.getItem(i11)) {
                break;
            }
            i11++;
        }
        if (i11 != -1 && (firstVisiblePosition = (i11 + i10) - a10.getFirstVisiblePosition()) >= 0 && firstVisiblePosition < a10.getChildCount()) {
            return a10.getChildAt(firstVisiblePosition);
        }
        return null;
    }

    private int v() {
        return ViewCompat.x(this.f701s) == 1 ? 0 : 1;
    }

    private int w(int i10) {
        List<d> list = this.f695m;
        ListView a10 = list.get(list.size() - 1).a();
        int[] iArr = new int[2];
        a10.getLocationOnScreen(iArr);
        Rect rect = new Rect();
        this.f702t.getWindowVisibleDisplayFrame(rect);
        return this.f703u == 1 ? (iArr[0] + a10.getWidth()) + i10 > rect.right ? 0 : 1 : iArr[0] - i10 < 0 ? 1 : 0;
    }

    private void x(MenuBuilder menuBuilder) {
        d dVar;
        View view;
        int i10;
        LayoutInflater from = LayoutInflater.from(this.f688f);
        MenuAdapter menuAdapter = new MenuAdapter(menuBuilder, from, this.f692j, F);
        if (!a() && this.f708z) {
            menuAdapter.d(true);
        } else if (a()) {
            menuAdapter.d(MenuPopup.p(menuBuilder));
        }
        int f10 = MenuPopup.f(menuAdapter, null, this.f688f, this.f689g);
        MenuPopupWindow r10 = r();
        r10.p(menuAdapter);
        r10.F(f10);
        r10.G(this.f700r);
        if (this.f695m.size() > 0) {
            List<d> list = this.f695m;
            dVar = list.get(list.size() - 1);
            view = u(dVar, menuBuilder);
        } else {
            dVar = null;
            view = null;
        }
        if (view != null) {
            r10.U(false);
            r10.R(null);
            int w10 = w(f10);
            boolean z10 = w10 == 1;
            this.f703u = w10;
            r10.D(view);
            if ((this.f700r & 5) == 5) {
                if (!z10) {
                    f10 = view.getWidth();
                    i10 = 0 - f10;
                }
                i10 = f10 + 0;
            } else {
                if (z10) {
                    f10 = view.getWidth();
                    i10 = f10 + 0;
                }
                i10 = 0 - f10;
            }
            r10.f(i10);
            r10.M(true);
            r10.l(0);
        } else {
            if (this.f704v) {
                r10.f(this.f706x);
            }
            if (this.f705w) {
                r10.l(this.f707y);
            }
            r10.H(e());
        }
        this.f695m.add(new d(r10, menuBuilder, this.f703u));
        r10.b();
        ListView j10 = r10.j();
        j10.setOnKeyListener(this);
        if (dVar == null && this.A && menuBuilder.getHeaderTitle() != null) {
            FrameLayout frameLayout = (FrameLayout) from.inflate(R$layout.abc_popup_menu_header_item_layout, (ViewGroup) j10, false);
            TextView textView = (TextView) frameLayout.findViewById(R.id.title);
            frameLayout.setEnabled(false);
            textView.setText(menuBuilder.getHeaderTitle());
            j10.addHeaderView(frameLayout, null, false);
            r10.b();
        }
    }

    @Override // androidx.appcompat.view.menu.ShowableListMenu
    public boolean a() {
        return this.f695m.size() > 0 && this.f695m.get(0).f716a.a();
    }

    @Override // androidx.appcompat.view.menu.ShowableListMenu
    public void b() {
        if (a()) {
            return;
        }
        Iterator<MenuBuilder> it = this.f694l.iterator();
        while (it.hasNext()) {
            x(it.next());
        }
        this.f694l.clear();
        View view = this.f701s;
        this.f702t = view;
        if (view != null) {
            boolean z10 = this.C == null;
            ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
            this.C = viewTreeObserver;
            if (z10) {
                viewTreeObserver.addOnGlobalLayoutListener(this.f696n);
            }
            this.f702t.addOnAttachStateChangeListener(this.f697o);
        }
    }

    @Override // androidx.appcompat.view.menu.MenuPopup
    public void c(MenuBuilder menuBuilder) {
        menuBuilder.addMenuPresenter(this, this.f688f);
        if (a()) {
            x(menuBuilder);
        } else {
            this.f694l.add(menuBuilder);
        }
    }

    @Override // androidx.appcompat.view.menu.MenuPopup
    protected boolean d() {
        return false;
    }

    @Override // androidx.appcompat.view.menu.ShowableListMenu
    public void dismiss() {
        int size = this.f695m.size();
        if (size > 0) {
            d[] dVarArr = (d[]) this.f695m.toArray(new d[size]);
            for (int i10 = size - 1; i10 >= 0; i10--) {
                d dVar = dVarArr[i10];
                if (dVar.f716a.a()) {
                    dVar.f716a.dismiss();
                }
            }
        }
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public boolean flagActionItems() {
        return false;
    }

    @Override // androidx.appcompat.view.menu.MenuPopup
    public void g(View view) {
        if (this.f701s != view) {
            this.f701s = view;
            this.f700r = GravityCompat.b(this.f699q, ViewCompat.x(view));
        }
    }

    @Override // androidx.appcompat.view.menu.MenuPopup
    public void i(boolean z10) {
        this.f708z = z10;
    }

    @Override // androidx.appcompat.view.menu.ShowableListMenu
    public ListView j() {
        if (this.f695m.isEmpty()) {
            return null;
        }
        return this.f695m.get(r1.size() - 1).a();
    }

    @Override // androidx.appcompat.view.menu.MenuPopup
    public void k(int i10) {
        if (this.f699q != i10) {
            this.f699q = i10;
            this.f700r = GravityCompat.b(i10, ViewCompat.x(this.f701s));
        }
    }

    @Override // androidx.appcompat.view.menu.MenuPopup
    public void l(int i10) {
        this.f704v = true;
        this.f706x = i10;
    }

    @Override // androidx.appcompat.view.menu.MenuPopup
    public void m(PopupWindow.OnDismissListener onDismissListener) {
        this.D = onDismissListener;
    }

    @Override // androidx.appcompat.view.menu.MenuPopup
    public void n(boolean z10) {
        this.A = z10;
    }

    @Override // androidx.appcompat.view.menu.MenuPopup
    public void o(int i10) {
        this.f705w = true;
        this.f707y = i10;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public void onCloseMenu(MenuBuilder menuBuilder, boolean z10) {
        int s7 = s(menuBuilder);
        if (s7 < 0) {
            return;
        }
        int i10 = s7 + 1;
        if (i10 < this.f695m.size()) {
            this.f695m.get(i10).f717b.close(false);
        }
        d remove = this.f695m.remove(s7);
        remove.f717b.removeMenuPresenter(this);
        if (this.E) {
            remove.f716a.S(null);
            remove.f716a.E(0);
        }
        remove.f716a.dismiss();
        int size = this.f695m.size();
        if (size > 0) {
            this.f703u = this.f695m.get(size - 1).f718c;
        } else {
            this.f703u = v();
        }
        if (size != 0) {
            if (z10) {
                this.f695m.get(0).f717b.close(false);
                return;
            }
            return;
        }
        dismiss();
        MenuPresenter.a aVar = this.B;
        if (aVar != null) {
            aVar.onCloseMenu(menuBuilder, true);
        }
        ViewTreeObserver viewTreeObserver = this.C;
        if (viewTreeObserver != null) {
            if (viewTreeObserver.isAlive()) {
                this.C.removeGlobalOnLayoutListener(this.f696n);
            }
            this.C = null;
        }
        this.f702t.removeOnAttachStateChangeListener(this.f697o);
        this.D.onDismiss();
    }

    @Override // android.widget.PopupWindow.OnDismissListener
    public void onDismiss() {
        d dVar;
        int size = this.f695m.size();
        int i10 = 0;
        while (true) {
            if (i10 >= size) {
                dVar = null;
                break;
            }
            dVar = this.f695m.get(i10);
            if (!dVar.f716a.a()) {
                break;
            } else {
                i10++;
            }
        }
        if (dVar != null) {
            dVar.f717b.close(false);
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
        for (d dVar : this.f695m) {
            if (subMenuBuilder == dVar.f717b) {
                dVar.a().requestFocus();
                return true;
            }
        }
        if (!subMenuBuilder.hasVisibleItems()) {
            return false;
        }
        c(subMenuBuilder);
        MenuPresenter.a aVar = this.B;
        if (aVar != null) {
            aVar.a(subMenuBuilder);
        }
        return true;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public void setCallback(MenuPresenter.a aVar) {
        this.B = aVar;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public void updateMenuView(boolean z10) {
        Iterator<d> it = this.f695m.iterator();
        while (it.hasNext()) {
            MenuPopup.q(it.next().a().getAdapter()).notifyDataSetChanged();
        }
    }
}
