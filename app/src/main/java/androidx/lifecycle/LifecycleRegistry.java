package androidx.lifecycle;

import android.annotation.SuppressLint;
import androidx.lifecycle.h;
import g.ArchTaskExecutor;
import h.FastSafeIterableMap;
import h.SafeIterableMap;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/* compiled from: LifecycleRegistry.java */
/* renamed from: androidx.lifecycle.q, reason: use source file name */
/* loaded from: classes.dex */
public class LifecycleRegistry extends h {

    /* renamed from: b, reason: collision with root package name */
    private FastSafeIterableMap<LifecycleObserver, a> f3207b;

    /* renamed from: c, reason: collision with root package name */
    private h.c f3208c;

    /* renamed from: d, reason: collision with root package name */
    private final WeakReference<o> f3209d;

    /* renamed from: e, reason: collision with root package name */
    private int f3210e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f3211f;

    /* renamed from: g, reason: collision with root package name */
    private boolean f3212g;

    /* renamed from: h, reason: collision with root package name */
    private ArrayList<h.c> f3213h;

    /* renamed from: i, reason: collision with root package name */
    private final boolean f3214i;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: LifecycleRegistry.java */
    /* renamed from: androidx.lifecycle.q$a */
    /* loaded from: classes.dex */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        h.c f3215a;

        /* renamed from: b, reason: collision with root package name */
        LifecycleEventObserver f3216b;

        a(LifecycleObserver lifecycleObserver, h.c cVar) {
            this.f3216b = Lifecycling.f(lifecycleObserver);
            this.f3215a = cVar;
        }

        void a(o oVar, h.b bVar) {
            h.c b10 = bVar.b();
            this.f3215a = LifecycleRegistry.k(this.f3215a, b10);
            this.f3216b.a(oVar, bVar);
            this.f3215a = b10;
        }
    }

    public LifecycleRegistry(o oVar) {
        this(oVar, true);
    }

    private void d(o oVar) {
        Iterator<Map.Entry<LifecycleObserver, a>> c10 = this.f3207b.c();
        while (c10.hasNext() && !this.f3212g) {
            Map.Entry<LifecycleObserver, a> next = c10.next();
            a value = next.getValue();
            while (value.f3215a.compareTo(this.f3208c) > 0 && !this.f3212g && this.f3207b.contains(next.getKey())) {
                h.b a10 = h.b.a(value.f3215a);
                if (a10 != null) {
                    n(a10.b());
                    value.a(oVar, a10);
                    m();
                } else {
                    throw new IllegalStateException("no event down from " + value.f3215a);
                }
            }
        }
    }

    private h.c e(LifecycleObserver lifecycleObserver) {
        Map.Entry<LifecycleObserver, a> l10 = this.f3207b.l(lifecycleObserver);
        h.c cVar = null;
        h.c cVar2 = l10 != null ? l10.getValue().f3215a : null;
        if (!this.f3213h.isEmpty()) {
            cVar = this.f3213h.get(r0.size() - 1);
        }
        return k(k(this.f3208c, cVar2), cVar);
    }

    @SuppressLint({"RestrictedApi"})
    private void f(String str) {
        if (!this.f3214i || ArchTaskExecutor.f().c()) {
            return;
        }
        throw new IllegalStateException("Method " + str + " must be called on the main thread");
    }

    private void g(o oVar) {
        SafeIterableMap<LifecycleObserver, a>.d f10 = this.f3207b.f();
        while (f10.hasNext() && !this.f3212g) {
            Map.Entry next = f10.next();
            a aVar = (a) next.getValue();
            while (aVar.f3215a.compareTo(this.f3208c) < 0 && !this.f3212g && this.f3207b.contains((LifecycleObserver) next.getKey())) {
                n(aVar.f3215a);
                h.b c10 = h.b.c(aVar.f3215a);
                if (c10 != null) {
                    aVar.a(oVar, c10);
                    m();
                } else {
                    throw new IllegalStateException("no event up from " + aVar.f3215a);
                }
            }
        }
    }

    private boolean i() {
        if (this.f3207b.size() == 0) {
            return true;
        }
        h.c cVar = this.f3207b.d().getValue().f3215a;
        h.c cVar2 = this.f3207b.g().getValue().f3215a;
        return cVar == cVar2 && this.f3208c == cVar2;
    }

    static h.c k(h.c cVar, h.c cVar2) {
        return (cVar2 == null || cVar2.compareTo(cVar) >= 0) ? cVar : cVar2;
    }

    private void l(h.c cVar) {
        h.c cVar2 = this.f3208c;
        if (cVar2 == cVar) {
            return;
        }
        if (cVar2 == h.c.INITIALIZED && cVar == h.c.DESTROYED) {
            throw new IllegalStateException("no event down from " + this.f3208c);
        }
        this.f3208c = cVar;
        if (!this.f3211f && this.f3210e == 0) {
            this.f3211f = true;
            p();
            this.f3211f = false;
            if (this.f3208c == h.c.DESTROYED) {
                this.f3207b = new FastSafeIterableMap<>();
                return;
            }
            return;
        }
        this.f3212g = true;
    }

    private void m() {
        this.f3213h.remove(r1.size() - 1);
    }

    private void n(h.c cVar) {
        this.f3213h.add(cVar);
    }

    private void p() {
        o oVar = this.f3209d.get();
        if (oVar != null) {
            while (!i()) {
                this.f3212g = false;
                if (this.f3208c.compareTo(this.f3207b.d().getValue().f3215a) < 0) {
                    d(oVar);
                }
                Map.Entry<LifecycleObserver, a> g6 = this.f3207b.g();
                if (!this.f3212g && g6 != null && this.f3208c.compareTo(g6.getValue().f3215a) > 0) {
                    g(oVar);
                }
            }
            this.f3212g = false;
            return;
        }
        throw new IllegalStateException("LifecycleOwner of this LifecycleRegistry is alreadygarbage collected. It is too late to change lifecycle state.");
    }

    @Override // androidx.lifecycle.h
    public void a(LifecycleObserver lifecycleObserver) {
        o oVar;
        f("addObserver");
        h.c cVar = this.f3208c;
        h.c cVar2 = h.c.DESTROYED;
        if (cVar != cVar2) {
            cVar2 = h.c.INITIALIZED;
        }
        a aVar = new a(lifecycleObserver, cVar2);
        if (this.f3207b.i(lifecycleObserver, aVar) == null && (oVar = this.f3209d.get()) != null) {
            boolean z10 = this.f3210e != 0 || this.f3211f;
            h.c e10 = e(lifecycleObserver);
            this.f3210e++;
            while (aVar.f3215a.compareTo(e10) < 0 && this.f3207b.contains(lifecycleObserver)) {
                n(aVar.f3215a);
                h.b c10 = h.b.c(aVar.f3215a);
                if (c10 != null) {
                    aVar.a(oVar, c10);
                    m();
                    e10 = e(lifecycleObserver);
                } else {
                    throw new IllegalStateException("no event up from " + aVar.f3215a);
                }
            }
            if (!z10) {
                p();
            }
            this.f3210e--;
        }
    }

    @Override // androidx.lifecycle.h
    public h.c b() {
        return this.f3208c;
    }

    @Override // androidx.lifecycle.h
    public void c(LifecycleObserver lifecycleObserver) {
        f("removeObserver");
        this.f3207b.k(lifecycleObserver);
    }

    public void h(h.b bVar) {
        f("handleLifecycleEvent");
        l(bVar.b());
    }

    @Deprecated
    public void j(h.c cVar) {
        f("markState");
        o(cVar);
    }

    public void o(h.c cVar) {
        f("setCurrentState");
        l(cVar);
    }

    private LifecycleRegistry(o oVar, boolean z10) {
        this.f3207b = new FastSafeIterableMap<>();
        this.f3210e = 0;
        this.f3211f = false;
        this.f3212g = false;
        this.f3213h = new ArrayList<>();
        this.f3209d = new WeakReference<>(oVar);
        this.f3208c = h.c.INITIALIZED;
        this.f3214i = z10;
    }
}
