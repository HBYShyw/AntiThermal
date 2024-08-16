package androidx.appcompat.view;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

/* compiled from: ActionMode.java */
/* renamed from: androidx.appcompat.view.b, reason: use source file name */
/* loaded from: classes.dex */
public abstract class ActionMode {

    /* renamed from: e, reason: collision with root package name */
    private Object f557e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f558f;

    /* compiled from: ActionMode.java */
    /* renamed from: androidx.appcompat.view.b$a */
    /* loaded from: classes.dex */
    public interface a {
        void a(ActionMode actionMode);

        boolean b(ActionMode actionMode, Menu menu);

        boolean c(ActionMode actionMode, Menu menu);

        boolean d(ActionMode actionMode, MenuItem menuItem);
    }

    public abstract void c();

    public abstract View d();

    public abstract Menu e();

    public abstract MenuInflater f();

    public abstract CharSequence g();

    public Object h() {
        return this.f557e;
    }

    public abstract CharSequence i();

    public boolean j() {
        return this.f558f;
    }

    public abstract void k();

    public abstract boolean l();

    public abstract void m(View view);

    public abstract void n(int i10);

    public abstract void o(CharSequence charSequence);

    public void p(Object obj) {
        this.f557e = obj;
    }

    public abstract void q(int i10);

    public abstract void r(CharSequence charSequence);

    public void s(boolean z10) {
        this.f558f = z10;
    }
}
