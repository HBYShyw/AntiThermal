package androidx.lifecycle;

import androidx.lifecycle.h;
import g.ArchTaskExecutor;
import h.SafeIterableMap;

/* loaded from: classes.dex */
public abstract class LiveData<T> {

    /* renamed from: k, reason: collision with root package name */
    static final Object f3086k = new Object();

    /* renamed from: a, reason: collision with root package name */
    final Object f3087a = new Object();

    /* renamed from: b, reason: collision with root package name */
    private SafeIterableMap<Observer<? super T>, LiveData<T>.c> f3088b = new SafeIterableMap<>();

    /* renamed from: c, reason: collision with root package name */
    int f3089c = 0;

    /* renamed from: d, reason: collision with root package name */
    private boolean f3090d;

    /* renamed from: e, reason: collision with root package name */
    private volatile Object f3091e;

    /* renamed from: f, reason: collision with root package name */
    volatile Object f3092f;

    /* renamed from: g, reason: collision with root package name */
    private int f3093g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f3094h;

    /* renamed from: i, reason: collision with root package name */
    private boolean f3095i;

    /* renamed from: j, reason: collision with root package name */
    private final Runnable f3096j;

    /* loaded from: classes.dex */
    class LifecycleBoundObserver extends LiveData<T>.c implements LifecycleEventObserver {

        /* renamed from: i, reason: collision with root package name */
        final o f3097i;

        LifecycleBoundObserver(o oVar, Observer<? super T> observer) {
            super(observer);
            this.f3097i = oVar;
        }

        @Override // androidx.lifecycle.LifecycleEventObserver
        public void a(o oVar, h.b bVar) {
            h.c b10 = this.f3097i.getLifecycle().b();
            if (b10 == h.c.DESTROYED) {
                LiveData.this.l(this.f3101e);
                return;
            }
            h.c cVar = null;
            while (cVar != b10) {
                b(e());
                cVar = b10;
                b10 = this.f3097i.getLifecycle().b();
            }
        }

        @Override // androidx.lifecycle.LiveData.c
        void c() {
            this.f3097i.getLifecycle().c(this);
        }

        @Override // androidx.lifecycle.LiveData.c
        boolean d(o oVar) {
            return this.f3097i == oVar;
        }

        @Override // androidx.lifecycle.LiveData.c
        boolean e() {
            return this.f3097i.getLifecycle().b().a(h.c.STARTED);
        }
    }

    /* loaded from: classes.dex */
    class a implements Runnable {
        a() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.lang.Runnable
        public void run() {
            Object obj;
            synchronized (LiveData.this.f3087a) {
                obj = LiveData.this.f3092f;
                LiveData.this.f3092f = LiveData.f3086k;
            }
            LiveData.this.m(obj);
        }
    }

    /* loaded from: classes.dex */
    private class b extends LiveData<T>.c {
        b(Observer<? super T> observer) {
            super(observer);
        }

        @Override // androidx.lifecycle.LiveData.c
        boolean e() {
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public abstract class c {

        /* renamed from: e, reason: collision with root package name */
        final Observer<? super T> f3101e;

        /* renamed from: f, reason: collision with root package name */
        boolean f3102f;

        /* renamed from: g, reason: collision with root package name */
        int f3103g = -1;

        c(Observer<? super T> observer) {
            this.f3101e = observer;
        }

        void b(boolean z10) {
            if (z10 == this.f3102f) {
                return;
            }
            this.f3102f = z10;
            LiveData.this.b(z10 ? 1 : -1);
            if (this.f3102f) {
                LiveData.this.d(this);
            }
        }

        void c() {
        }

        boolean d(o oVar) {
            return false;
        }

        abstract boolean e();
    }

    public LiveData() {
        Object obj = f3086k;
        this.f3092f = obj;
        this.f3096j = new a();
        this.f3091e = obj;
        this.f3093g = -1;
    }

    static void a(String str) {
        if (ArchTaskExecutor.f().c()) {
            return;
        }
        throw new IllegalStateException("Cannot invoke " + str + " on a background thread");
    }

    private void c(LiveData<T>.c cVar) {
        if (cVar.f3102f) {
            if (!cVar.e()) {
                cVar.b(false);
                return;
            }
            int i10 = cVar.f3103g;
            int i11 = this.f3093g;
            if (i10 >= i11) {
                return;
            }
            cVar.f3103g = i11;
            cVar.f3101e.a((Object) this.f3091e);
        }
    }

    void b(int i10) {
        int i11 = this.f3089c;
        this.f3089c = i10 + i11;
        if (this.f3090d) {
            return;
        }
        this.f3090d = true;
        while (true) {
            try {
                int i12 = this.f3089c;
                if (i11 == i12) {
                    return;
                }
                boolean z10 = i11 == 0 && i12 > 0;
                boolean z11 = i11 > 0 && i12 == 0;
                if (z10) {
                    i();
                } else if (z11) {
                    j();
                }
                i11 = i12;
            } finally {
                this.f3090d = false;
            }
        }
    }

    void d(LiveData<T>.c cVar) {
        if (this.f3094h) {
            this.f3095i = true;
            return;
        }
        this.f3094h = true;
        do {
            this.f3095i = false;
            if (cVar != null) {
                c(cVar);
                cVar = null;
            } else {
                SafeIterableMap<Observer<? super T>, LiveData<T>.c>.d f10 = this.f3088b.f();
                while (f10.hasNext()) {
                    c((c) f10.next().getValue());
                    if (this.f3095i) {
                        break;
                    }
                }
            }
        } while (this.f3095i);
        this.f3094h = false;
    }

    public T e() {
        T t7 = (T) this.f3091e;
        if (t7 != f3086k) {
            return t7;
        }
        return null;
    }

    public boolean f() {
        return this.f3089c > 0;
    }

    public void g(o oVar, Observer<? super T> observer) {
        a("observe");
        if (oVar.getLifecycle().b() == h.c.DESTROYED) {
            return;
        }
        LifecycleBoundObserver lifecycleBoundObserver = new LifecycleBoundObserver(oVar, observer);
        LiveData<T>.c i10 = this.f3088b.i(observer, lifecycleBoundObserver);
        if (i10 != null && !i10.d(oVar)) {
            throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
        }
        if (i10 != null) {
            return;
        }
        oVar.getLifecycle().a(lifecycleBoundObserver);
    }

    public void h(Observer<? super T> observer) {
        a("observeForever");
        b bVar = new b(observer);
        LiveData<T>.c i10 = this.f3088b.i(observer, bVar);
        if (i10 instanceof LifecycleBoundObserver) {
            throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
        }
        if (i10 != null) {
            return;
        }
        bVar.b(true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void i() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void j() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void k(T t7) {
        boolean z10;
        synchronized (this.f3087a) {
            z10 = this.f3092f == f3086k;
            this.f3092f = t7;
        }
        if (z10) {
            ArchTaskExecutor.f().d(this.f3096j);
        }
    }

    public void l(Observer<? super T> observer) {
        a("removeObserver");
        LiveData<T>.c k10 = this.f3088b.k(observer);
        if (k10 == null) {
            return;
        }
        k10.c();
        k10.b(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void m(T t7) {
        a("setValue");
        this.f3093g++;
        this.f3091e = t7;
        d(null);
    }
}
