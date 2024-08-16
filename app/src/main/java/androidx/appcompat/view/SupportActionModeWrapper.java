package androidx.appcompat.view;

import android.content.Context;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.menu.MenuItemWrapperICS;
import androidx.appcompat.view.menu.MenuWrapperICS;
import j.SimpleArrayMap;
import java.util.ArrayList;
import p.SupportMenu;
import p.SupportMenuItem;

/* compiled from: SupportActionModeWrapper.java */
/* renamed from: androidx.appcompat.view.f, reason: use source file name */
/* loaded from: classes.dex */
public class SupportActionModeWrapper extends ActionMode {

    /* renamed from: a, reason: collision with root package name */
    final Context f572a;

    /* renamed from: b, reason: collision with root package name */
    final ActionMode f573b;

    /* compiled from: SupportActionModeWrapper.java */
    /* renamed from: androidx.appcompat.view.f$a */
    /* loaded from: classes.dex */
    public static class a implements ActionMode.a {

        /* renamed from: a, reason: collision with root package name */
        final ActionMode.Callback f574a;

        /* renamed from: b, reason: collision with root package name */
        final Context f575b;

        /* renamed from: c, reason: collision with root package name */
        final ArrayList<SupportActionModeWrapper> f576c = new ArrayList<>();

        /* renamed from: d, reason: collision with root package name */
        final SimpleArrayMap<Menu, Menu> f577d = new SimpleArrayMap<>();

        public a(Context context, ActionMode.Callback callback) {
            this.f575b = context;
            this.f574a = callback;
        }

        private Menu f(Menu menu) {
            Menu menu2 = this.f577d.get(menu);
            if (menu2 != null) {
                return menu2;
            }
            MenuWrapperICS menuWrapperICS = new MenuWrapperICS(this.f575b, (SupportMenu) menu);
            this.f577d.put(menu, menuWrapperICS);
            return menuWrapperICS;
        }

        @Override // androidx.appcompat.view.ActionMode.a
        public void a(ActionMode actionMode) {
            this.f574a.onDestroyActionMode(e(actionMode));
        }

        @Override // androidx.appcompat.view.ActionMode.a
        public boolean b(ActionMode actionMode, Menu menu) {
            return this.f574a.onCreateActionMode(e(actionMode), f(menu));
        }

        @Override // androidx.appcompat.view.ActionMode.a
        public boolean c(ActionMode actionMode, Menu menu) {
            return this.f574a.onPrepareActionMode(e(actionMode), f(menu));
        }

        @Override // androidx.appcompat.view.ActionMode.a
        public boolean d(ActionMode actionMode, MenuItem menuItem) {
            return this.f574a.onActionItemClicked(e(actionMode), new MenuItemWrapperICS(this.f575b, (SupportMenuItem) menuItem));
        }

        public android.view.ActionMode e(ActionMode actionMode) {
            int size = this.f576c.size();
            for (int i10 = 0; i10 < size; i10++) {
                SupportActionModeWrapper supportActionModeWrapper = this.f576c.get(i10);
                if (supportActionModeWrapper != null && supportActionModeWrapper.f573b == actionMode) {
                    return supportActionModeWrapper;
                }
            }
            SupportActionModeWrapper supportActionModeWrapper2 = new SupportActionModeWrapper(this.f575b, actionMode);
            this.f576c.add(supportActionModeWrapper2);
            return supportActionModeWrapper2;
        }
    }

    public SupportActionModeWrapper(Context context, ActionMode actionMode) {
        this.f572a = context;
        this.f573b = actionMode;
    }

    @Override // android.view.ActionMode
    public void finish() {
        this.f573b.c();
    }

    @Override // android.view.ActionMode
    public View getCustomView() {
        return this.f573b.d();
    }

    @Override // android.view.ActionMode
    public Menu getMenu() {
        return new MenuWrapperICS(this.f572a, (SupportMenu) this.f573b.e());
    }

    @Override // android.view.ActionMode
    public MenuInflater getMenuInflater() {
        return this.f573b.f();
    }

    @Override // android.view.ActionMode
    public CharSequence getSubtitle() {
        return this.f573b.g();
    }

    @Override // android.view.ActionMode
    public Object getTag() {
        return this.f573b.h();
    }

    @Override // android.view.ActionMode
    public CharSequence getTitle() {
        return this.f573b.i();
    }

    @Override // android.view.ActionMode
    public boolean getTitleOptionalHint() {
        return this.f573b.j();
    }

    @Override // android.view.ActionMode
    public void invalidate() {
        this.f573b.k();
    }

    @Override // android.view.ActionMode
    public boolean isTitleOptional() {
        return this.f573b.l();
    }

    @Override // android.view.ActionMode
    public void setCustomView(View view) {
        this.f573b.m(view);
    }

    @Override // android.view.ActionMode
    public void setSubtitle(CharSequence charSequence) {
        this.f573b.o(charSequence);
    }

    @Override // android.view.ActionMode
    public void setTag(Object obj) {
        this.f573b.p(obj);
    }

    @Override // android.view.ActionMode
    public void setTitle(CharSequence charSequence) {
        this.f573b.r(charSequence);
    }

    @Override // android.view.ActionMode
    public void setTitleOptionalHint(boolean z10) {
        this.f573b.s(z10);
    }

    @Override // android.view.ActionMode
    public void setSubtitle(int i10) {
        this.f573b.n(i10);
    }

    @Override // android.view.ActionMode
    public void setTitle(int i10) {
        this.f573b.q(i10);
    }
}
