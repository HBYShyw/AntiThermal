package androidx.appcompat.app;

import android.content.Context;
import android.content.res.Configuration;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDelegateImpl;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.widget.DecorToolbar;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.ToolbarWidgetWrapper;
import androidx.core.util.Preconditions;
import androidx.core.view.ViewCompat;
import java.util.ArrayList;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ToolbarActionBar.java */
/* renamed from: androidx.appcompat.app.n, reason: use source file name */
/* loaded from: classes.dex */
public class ToolbarActionBar extends ActionBar {

    /* renamed from: a, reason: collision with root package name */
    final DecorToolbar f497a;

    /* renamed from: b, reason: collision with root package name */
    final Window.Callback f498b;

    /* renamed from: c, reason: collision with root package name */
    final AppCompatDelegateImpl.g f499c;

    /* renamed from: d, reason: collision with root package name */
    boolean f500d;

    /* renamed from: e, reason: collision with root package name */
    private boolean f501e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f502f;

    /* renamed from: g, reason: collision with root package name */
    private ArrayList<ActionBar.a> f503g = new ArrayList<>();

    /* renamed from: h, reason: collision with root package name */
    private final Runnable f504h = new a();

    /* renamed from: i, reason: collision with root package name */
    private final Toolbar.g f505i;

    /* compiled from: ToolbarActionBar.java */
    /* renamed from: androidx.appcompat.app.n$a */
    /* loaded from: classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ToolbarActionBar.this.x();
        }
    }

    /* compiled from: ToolbarActionBar.java */
    /* renamed from: androidx.appcompat.app.n$b */
    /* loaded from: classes.dex */
    class b implements Toolbar.g {
        b() {
        }

        @Override // androidx.appcompat.widget.Toolbar.g
        public boolean onMenuItemClick(MenuItem menuItem) {
            return ToolbarActionBar.this.f498b.onMenuItemSelected(0, menuItem);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ToolbarActionBar.java */
    /* renamed from: androidx.appcompat.app.n$c */
    /* loaded from: classes.dex */
    public final class c implements MenuPresenter.a {

        /* renamed from: e, reason: collision with root package name */
        private boolean f508e;

        c() {
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter.a
        public boolean a(MenuBuilder menuBuilder) {
            ToolbarActionBar.this.f498b.onMenuOpened(108, menuBuilder);
            return true;
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter.a
        public void onCloseMenu(MenuBuilder menuBuilder, boolean z10) {
            if (this.f508e) {
                return;
            }
            this.f508e = true;
            ToolbarActionBar.this.f497a.h();
            ToolbarActionBar.this.f498b.onPanelClosed(108, menuBuilder);
            this.f508e = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ToolbarActionBar.java */
    /* renamed from: androidx.appcompat.app.n$d */
    /* loaded from: classes.dex */
    public final class d implements MenuBuilder.a {
        d() {
        }

        @Override // androidx.appcompat.view.menu.MenuBuilder.a
        public boolean a(MenuBuilder menuBuilder, MenuItem menuItem) {
            return false;
        }

        @Override // androidx.appcompat.view.menu.MenuBuilder.a
        public void b(MenuBuilder menuBuilder) {
            if (ToolbarActionBar.this.f497a.b()) {
                ToolbarActionBar.this.f498b.onPanelClosed(108, menuBuilder);
            } else if (ToolbarActionBar.this.f498b.onPreparePanel(0, null, menuBuilder)) {
                ToolbarActionBar.this.f498b.onMenuOpened(108, menuBuilder);
            }
        }
    }

    /* compiled from: ToolbarActionBar.java */
    /* renamed from: androidx.appcompat.app.n$e */
    /* loaded from: classes.dex */
    private class e implements AppCompatDelegateImpl.g {
        e() {
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.g
        public boolean a(int i10) {
            if (i10 != 0) {
                return false;
            }
            ToolbarActionBar toolbarActionBar = ToolbarActionBar.this;
            if (toolbarActionBar.f500d) {
                return false;
            }
            toolbarActionBar.f497a.c();
            ToolbarActionBar.this.f500d = true;
            return false;
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.g
        public View onCreatePanelView(int i10) {
            if (i10 == 0) {
                return new View(ToolbarActionBar.this.f497a.getContext());
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ToolbarActionBar(Toolbar toolbar, CharSequence charSequence, Window.Callback callback) {
        b bVar = new b();
        this.f505i = bVar;
        Preconditions.d(toolbar);
        ToolbarWidgetWrapper toolbarWidgetWrapper = new ToolbarWidgetWrapper(toolbar, false);
        this.f497a = toolbarWidgetWrapper;
        this.f498b = (Window.Callback) Preconditions.d(callback);
        toolbarWidgetWrapper.setWindowCallback(callback);
        toolbar.setOnMenuItemClickListener(bVar);
        toolbarWidgetWrapper.setWindowTitle(charSequence);
        this.f499c = new e();
    }

    private Menu w() {
        if (!this.f501e) {
            this.f497a.p(new c(), new d());
            this.f501e = true;
        }
        return this.f497a.l();
    }

    @Override // androidx.appcompat.app.ActionBar
    public boolean g() {
        return this.f497a.f();
    }

    @Override // androidx.appcompat.app.ActionBar
    public boolean h() {
        if (!this.f497a.j()) {
            return false;
        }
        this.f497a.collapseActionView();
        return true;
    }

    @Override // androidx.appcompat.app.ActionBar
    public void i(boolean z10) {
        if (z10 == this.f502f) {
            return;
        }
        this.f502f = z10;
        int size = this.f503g.size();
        for (int i10 = 0; i10 < size; i10++) {
            this.f503g.get(i10).a(z10);
        }
    }

    @Override // androidx.appcompat.app.ActionBar
    public int j() {
        return this.f497a.s();
    }

    @Override // androidx.appcompat.app.ActionBar
    public Context k() {
        return this.f497a.getContext();
    }

    @Override // androidx.appcompat.app.ActionBar
    public boolean l() {
        this.f497a.q().removeCallbacks(this.f504h);
        ViewCompat.c0(this.f497a.q(), this.f504h);
        return true;
    }

    @Override // androidx.appcompat.app.ActionBar
    public void m(Configuration configuration) {
        super.m(configuration);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.appcompat.app.ActionBar
    public void n() {
        this.f497a.q().removeCallbacks(this.f504h);
    }

    @Override // androidx.appcompat.app.ActionBar
    public boolean o(int i10, KeyEvent keyEvent) {
        Menu w10 = w();
        if (w10 == null) {
            return false;
        }
        w10.setQwertyMode(KeyCharacterMap.load(keyEvent != null ? keyEvent.getDeviceId() : -1).getKeyboardType() != 1);
        return w10.performShortcut(i10, keyEvent, 0);
    }

    @Override // androidx.appcompat.app.ActionBar
    public boolean p(KeyEvent keyEvent) {
        if (keyEvent.getAction() == 1) {
            q();
        }
        return true;
    }

    @Override // androidx.appcompat.app.ActionBar
    public boolean q() {
        return this.f497a.g();
    }

    @Override // androidx.appcompat.app.ActionBar
    public void r(boolean z10) {
    }

    @Override // androidx.appcompat.app.ActionBar
    public void s(boolean z10) {
        y(z10 ? 4 : 0, 4);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void t(boolean z10) {
    }

    @Override // androidx.appcompat.app.ActionBar
    public void u(CharSequence charSequence) {
        this.f497a.setWindowTitle(charSequence);
    }

    void x() {
        Menu w10 = w();
        MenuBuilder menuBuilder = w10 instanceof MenuBuilder ? (MenuBuilder) w10 : null;
        if (menuBuilder != null) {
            menuBuilder.stopDispatchingItemsChanged();
        }
        try {
            w10.clear();
            if (!this.f498b.onCreatePanelMenu(0, w10) || !this.f498b.onPreparePanel(0, null, w10)) {
                w10.clear();
            }
        } finally {
            if (menuBuilder != null) {
                menuBuilder.startDispatchingItemsChanged();
            }
        }
    }

    public void y(int i10, int i11) {
        this.f497a.k((i10 & i11) | ((~i11) & this.f497a.s()));
    }
}
