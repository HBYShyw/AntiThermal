package androidx.fragment.app;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.FragmentManager;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: FragmentLifecycleCallbacksDispatcher.java */
/* renamed from: androidx.fragment.app.j, reason: use source file name */
/* loaded from: classes.dex */
public class FragmentLifecycleCallbacksDispatcher {

    /* renamed from: a, reason: collision with root package name */
    private final CopyOnWriteArrayList<a> f2908a = new CopyOnWriteArrayList<>();

    /* renamed from: b, reason: collision with root package name */
    private final FragmentManager f2909b;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: FragmentLifecycleCallbacksDispatcher.java */
    /* renamed from: androidx.fragment.app.j$a */
    /* loaded from: classes.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        final FragmentManager.l f2910a;

        /* renamed from: b, reason: collision with root package name */
        final boolean f2911b;

        a(FragmentManager.l lVar, boolean z10) {
            this.f2910a = lVar;
            this.f2911b = z10;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FragmentLifecycleCallbacksDispatcher(FragmentManager fragmentManager) {
        this.f2909b = fragmentManager;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Fragment fragment, Bundle bundle, boolean z10) {
        Fragment y02 = this.f2909b.y0();
        if (y02 != null) {
            y02.getParentFragmentManager().x0().a(fragment, bundle, true);
        }
        Iterator<a> it = this.f2908a.iterator();
        while (it.hasNext()) {
            a next = it.next();
            if (!z10 || next.f2911b) {
                next.f2910a.a(this.f2909b, fragment, bundle);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(Fragment fragment, boolean z10) {
        Context f10 = this.f2909b.v0().f();
        Fragment y02 = this.f2909b.y0();
        if (y02 != null) {
            y02.getParentFragmentManager().x0().b(fragment, true);
        }
        Iterator<a> it = this.f2908a.iterator();
        while (it.hasNext()) {
            a next = it.next();
            if (!z10 || next.f2911b) {
                next.f2910a.b(this.f2909b, fragment, f10);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(Fragment fragment, Bundle bundle, boolean z10) {
        Fragment y02 = this.f2909b.y0();
        if (y02 != null) {
            y02.getParentFragmentManager().x0().c(fragment, bundle, true);
        }
        Iterator<a> it = this.f2908a.iterator();
        while (it.hasNext()) {
            a next = it.next();
            if (!z10 || next.f2911b) {
                next.f2910a.c(this.f2909b, fragment, bundle);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(Fragment fragment, boolean z10) {
        Fragment y02 = this.f2909b.y0();
        if (y02 != null) {
            y02.getParentFragmentManager().x0().d(fragment, true);
        }
        Iterator<a> it = this.f2908a.iterator();
        while (it.hasNext()) {
            a next = it.next();
            if (!z10 || next.f2911b) {
                next.f2910a.d(this.f2909b, fragment);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(Fragment fragment, boolean z10) {
        Fragment y02 = this.f2909b.y0();
        if (y02 != null) {
            y02.getParentFragmentManager().x0().e(fragment, true);
        }
        Iterator<a> it = this.f2908a.iterator();
        while (it.hasNext()) {
            a next = it.next();
            if (!z10 || next.f2911b) {
                next.f2910a.e(this.f2909b, fragment);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f(Fragment fragment, boolean z10) {
        Fragment y02 = this.f2909b.y0();
        if (y02 != null) {
            y02.getParentFragmentManager().x0().f(fragment, true);
        }
        Iterator<a> it = this.f2908a.iterator();
        while (it.hasNext()) {
            a next = it.next();
            if (!z10 || next.f2911b) {
                next.f2910a.f(this.f2909b, fragment);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g(Fragment fragment, boolean z10) {
        Context f10 = this.f2909b.v0().f();
        Fragment y02 = this.f2909b.y0();
        if (y02 != null) {
            y02.getParentFragmentManager().x0().g(fragment, true);
        }
        Iterator<a> it = this.f2908a.iterator();
        while (it.hasNext()) {
            a next = it.next();
            if (!z10 || next.f2911b) {
                next.f2910a.g(this.f2909b, fragment, f10);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void h(Fragment fragment, Bundle bundle, boolean z10) {
        Fragment y02 = this.f2909b.y0();
        if (y02 != null) {
            y02.getParentFragmentManager().x0().h(fragment, bundle, true);
        }
        Iterator<a> it = this.f2908a.iterator();
        while (it.hasNext()) {
            a next = it.next();
            if (!z10 || next.f2911b) {
                next.f2910a.h(this.f2909b, fragment, bundle);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void i(Fragment fragment, boolean z10) {
        Fragment y02 = this.f2909b.y0();
        if (y02 != null) {
            y02.getParentFragmentManager().x0().i(fragment, true);
        }
        Iterator<a> it = this.f2908a.iterator();
        while (it.hasNext()) {
            a next = it.next();
            if (!z10 || next.f2911b) {
                next.f2910a.i(this.f2909b, fragment);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void j(Fragment fragment, Bundle bundle, boolean z10) {
        Fragment y02 = this.f2909b.y0();
        if (y02 != null) {
            y02.getParentFragmentManager().x0().j(fragment, bundle, true);
        }
        Iterator<a> it = this.f2908a.iterator();
        while (it.hasNext()) {
            a next = it.next();
            if (!z10 || next.f2911b) {
                next.f2910a.j(this.f2909b, fragment, bundle);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k(Fragment fragment, boolean z10) {
        Fragment y02 = this.f2909b.y0();
        if (y02 != null) {
            y02.getParentFragmentManager().x0().k(fragment, true);
        }
        Iterator<a> it = this.f2908a.iterator();
        while (it.hasNext()) {
            a next = it.next();
            if (!z10 || next.f2911b) {
                next.f2910a.k(this.f2909b, fragment);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void l(Fragment fragment, boolean z10) {
        Fragment y02 = this.f2909b.y0();
        if (y02 != null) {
            y02.getParentFragmentManager().x0().l(fragment, true);
        }
        Iterator<a> it = this.f2908a.iterator();
        while (it.hasNext()) {
            a next = it.next();
            if (!z10 || next.f2911b) {
                next.f2910a.l(this.f2909b, fragment);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void m(Fragment fragment, View view, Bundle bundle, boolean z10) {
        Fragment y02 = this.f2909b.y0();
        if (y02 != null) {
            y02.getParentFragmentManager().x0().m(fragment, view, bundle, true);
        }
        Iterator<a> it = this.f2908a.iterator();
        while (it.hasNext()) {
            a next = it.next();
            if (!z10 || next.f2911b) {
                next.f2910a.m(this.f2909b, fragment, view, bundle);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void n(Fragment fragment, boolean z10) {
        Fragment y02 = this.f2909b.y0();
        if (y02 != null) {
            y02.getParentFragmentManager().x0().n(fragment, true);
        }
        Iterator<a> it = this.f2908a.iterator();
        while (it.hasNext()) {
            a next = it.next();
            if (!z10 || next.f2911b) {
                next.f2910a.n(this.f2909b, fragment);
            }
        }
    }

    public void o(FragmentManager.l lVar, boolean z10) {
        this.f2908a.add(new a(lVar, z10));
    }

    public void p(FragmentManager.l lVar) {
        synchronized (this.f2908a) {
            int i10 = 0;
            int size = this.f2908a.size();
            while (true) {
                if (i10 >= size) {
                    break;
                }
                if (this.f2908a.get(i10).f2910a == lVar) {
                    this.f2908a.remove(i10);
                    break;
                }
                i10++;
            }
        }
    }
}
