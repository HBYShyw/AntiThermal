package androidx.appcompat.view;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.ActionBarContextView;
import java.lang.ref.WeakReference;

/* compiled from: StandaloneActionMode.java */
/* renamed from: androidx.appcompat.view.e, reason: use source file name */
/* loaded from: classes.dex */
public class StandaloneActionMode extends ActionMode implements MenuBuilder.a {

    /* renamed from: g, reason: collision with root package name */
    private Context f565g;

    /* renamed from: h, reason: collision with root package name */
    private ActionBarContextView f566h;

    /* renamed from: i, reason: collision with root package name */
    private ActionMode.a f567i;

    /* renamed from: j, reason: collision with root package name */
    private WeakReference<View> f568j;

    /* renamed from: k, reason: collision with root package name */
    private boolean f569k;

    /* renamed from: l, reason: collision with root package name */
    private boolean f570l;

    /* renamed from: m, reason: collision with root package name */
    private MenuBuilder f571m;

    public StandaloneActionMode(Context context, ActionBarContextView actionBarContextView, ActionMode.a aVar, boolean z10) {
        this.f565g = context;
        this.f566h = actionBarContextView;
        this.f567i = aVar;
        MenuBuilder defaultShowAsAction = new MenuBuilder(actionBarContextView.getContext()).setDefaultShowAsAction(1);
        this.f571m = defaultShowAsAction;
        defaultShowAsAction.setCallback(this);
        this.f570l = z10;
    }

    @Override // androidx.appcompat.view.menu.MenuBuilder.a
    public boolean a(MenuBuilder menuBuilder, MenuItem menuItem) {
        return this.f567i.d(this, menuItem);
    }

    @Override // androidx.appcompat.view.menu.MenuBuilder.a
    public void b(MenuBuilder menuBuilder) {
        k();
        this.f566h.l();
    }

    @Override // androidx.appcompat.view.ActionMode
    public void c() {
        if (this.f569k) {
            return;
        }
        this.f569k = true;
        this.f567i.a(this);
    }

    @Override // androidx.appcompat.view.ActionMode
    public View d() {
        WeakReference<View> weakReference = this.f568j;
        if (weakReference != null) {
            return weakReference.get();
        }
        return null;
    }

    @Override // androidx.appcompat.view.ActionMode
    public Menu e() {
        return this.f571m;
    }

    @Override // androidx.appcompat.view.ActionMode
    public MenuInflater f() {
        return new SupportMenuInflater(this.f566h.getContext());
    }

    @Override // androidx.appcompat.view.ActionMode
    public CharSequence g() {
        return this.f566h.getSubtitle();
    }

    @Override // androidx.appcompat.view.ActionMode
    public CharSequence i() {
        return this.f566h.getTitle();
    }

    @Override // androidx.appcompat.view.ActionMode
    public void k() {
        this.f567i.c(this, this.f571m);
    }

    @Override // androidx.appcompat.view.ActionMode
    public boolean l() {
        return this.f566h.j();
    }

    @Override // androidx.appcompat.view.ActionMode
    public void m(View view) {
        this.f566h.setCustomView(view);
        this.f568j = view != null ? new WeakReference<>(view) : null;
    }

    @Override // androidx.appcompat.view.ActionMode
    public void n(int i10) {
        o(this.f565g.getString(i10));
    }

    @Override // androidx.appcompat.view.ActionMode
    public void o(CharSequence charSequence) {
        this.f566h.setSubtitle(charSequence);
    }

    @Override // androidx.appcompat.view.ActionMode
    public void q(int i10) {
        r(this.f565g.getString(i10));
    }

    @Override // androidx.appcompat.view.ActionMode
    public void r(CharSequence charSequence) {
        this.f566h.setTitle(charSequence);
    }

    @Override // androidx.appcompat.view.ActionMode
    public void s(boolean z10) {
        super.s(z10);
        this.f566h.setTitleOptional(z10);
    }
}
