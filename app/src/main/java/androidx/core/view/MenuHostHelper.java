package androidx.core.view;

import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.h;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/* compiled from: MenuHostHelper.java */
/* renamed from: androidx.core.view.k, reason: use source file name */
/* loaded from: classes.dex */
public class MenuHostHelper {

    /* renamed from: a, reason: collision with root package name */
    private final Runnable f2373a;

    /* renamed from: b, reason: collision with root package name */
    private final CopyOnWriteArrayList<MenuProvider> f2374b = new CopyOnWriteArrayList<>();

    /* renamed from: c, reason: collision with root package name */
    private final Map<MenuProvider, a> f2375c = new HashMap();

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: MenuHostHelper.java */
    /* renamed from: androidx.core.view.k$a */
    /* loaded from: classes.dex */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        final androidx.lifecycle.h f2376a;

        /* renamed from: b, reason: collision with root package name */
        private LifecycleEventObserver f2377b;

        a(androidx.lifecycle.h hVar, LifecycleEventObserver lifecycleEventObserver) {
            this.f2376a = hVar;
            this.f2377b = lifecycleEventObserver;
            hVar.a(lifecycleEventObserver);
        }

        void a() {
            this.f2376a.c(this.f2377b);
            this.f2377b = null;
        }
    }

    public MenuHostHelper(Runnable runnable) {
        this.f2373a = runnable;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void f(MenuProvider menuProvider, androidx.lifecycle.o oVar, h.b bVar) {
        if (bVar == h.b.ON_DESTROY) {
            l(menuProvider);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void g(h.c cVar, MenuProvider menuProvider, androidx.lifecycle.o oVar, h.b bVar) {
        if (bVar == h.b.d(cVar)) {
            c(menuProvider);
            return;
        }
        if (bVar == h.b.ON_DESTROY) {
            l(menuProvider);
        } else if (bVar == h.b.a(cVar)) {
            this.f2374b.remove(menuProvider);
            this.f2373a.run();
        }
    }

    public void c(MenuProvider menuProvider) {
        this.f2374b.add(menuProvider);
        this.f2373a.run();
    }

    public void d(final MenuProvider menuProvider, androidx.lifecycle.o oVar) {
        c(menuProvider);
        androidx.lifecycle.h lifecycle = oVar.getLifecycle();
        a remove = this.f2375c.remove(menuProvider);
        if (remove != null) {
            remove.a();
        }
        this.f2375c.put(menuProvider, new a(lifecycle, new LifecycleEventObserver() { // from class: androidx.core.view.i
            @Override // androidx.lifecycle.LifecycleEventObserver
            public final void a(androidx.lifecycle.o oVar2, h.b bVar) {
                MenuHostHelper.this.f(menuProvider, oVar2, bVar);
            }
        }));
    }

    @SuppressLint({"LambdaLast"})
    public void e(final MenuProvider menuProvider, androidx.lifecycle.o oVar, final h.c cVar) {
        androidx.lifecycle.h lifecycle = oVar.getLifecycle();
        a remove = this.f2375c.remove(menuProvider);
        if (remove != null) {
            remove.a();
        }
        this.f2375c.put(menuProvider, new a(lifecycle, new LifecycleEventObserver() { // from class: androidx.core.view.j
            @Override // androidx.lifecycle.LifecycleEventObserver
            public final void a(androidx.lifecycle.o oVar2, h.b bVar) {
                MenuHostHelper.this.g(cVar, menuProvider, oVar2, bVar);
            }
        }));
    }

    public void h(Menu menu, MenuInflater menuInflater) {
        Iterator<MenuProvider> it = this.f2374b.iterator();
        while (it.hasNext()) {
            it.next().c(menu, menuInflater);
        }
    }

    public void i(Menu menu) {
        Iterator<MenuProvider> it = this.f2374b.iterator();
        while (it.hasNext()) {
            it.next().b(menu);
        }
    }

    public boolean j(MenuItem menuItem) {
        Iterator<MenuProvider> it = this.f2374b.iterator();
        while (it.hasNext()) {
            if (it.next().a(menuItem)) {
                return true;
            }
        }
        return false;
    }

    public void k(Menu menu) {
        Iterator<MenuProvider> it = this.f2374b.iterator();
        while (it.hasNext()) {
            it.next().d(menu);
        }
    }

    public void l(MenuProvider menuProvider) {
        this.f2374b.remove(menuProvider);
        a remove = this.f2375c.remove(menuProvider);
        if (remove != null) {
            remove.a();
        }
        this.f2373a.run();
    }
}
