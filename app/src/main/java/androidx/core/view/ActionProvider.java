package androidx.core.view;

import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

/* compiled from: ActionProvider.java */
/* renamed from: androidx.core.view.b, reason: use source file name */
/* loaded from: classes.dex */
public abstract class ActionProvider {

    /* renamed from: a, reason: collision with root package name */
    private final Context f2350a;

    /* renamed from: b, reason: collision with root package name */
    private a f2351b;

    /* renamed from: c, reason: collision with root package name */
    private b f2352c;

    /* compiled from: ActionProvider.java */
    /* renamed from: androidx.core.view.b$a */
    /* loaded from: classes.dex */
    public interface a {
        void a(boolean z10);
    }

    /* compiled from: ActionProvider.java */
    /* renamed from: androidx.core.view.b$b */
    /* loaded from: classes.dex */
    public interface b {
        void onActionProviderVisibilityChanged(boolean z10);
    }

    public ActionProvider(Context context) {
        this.f2350a = context;
    }

    public boolean a() {
        return false;
    }

    public boolean b() {
        return true;
    }

    public abstract View c();

    public View d(MenuItem menuItem) {
        return c();
    }

    public boolean e() {
        return false;
    }

    public void f(SubMenu subMenu) {
    }

    public boolean g() {
        return false;
    }

    public void h() {
        this.f2352c = null;
        this.f2351b = null;
    }

    public void i(a aVar) {
        this.f2351b = aVar;
    }

    public void j(b bVar) {
        if (this.f2352c != null && bVar != null) {
            Log.w("ActionProvider(support)", "setVisibilityListener: Setting a new ActionProvider.VisibilityListener when one is already set. Are you reusing this " + getClass().getSimpleName() + " instance while it is still in use somewhere else?");
        }
        this.f2352c = bVar;
    }

    public void k(boolean z10) {
        a aVar = this.f2351b;
        if (aVar != null) {
            aVar.a(z10);
        }
    }
}
