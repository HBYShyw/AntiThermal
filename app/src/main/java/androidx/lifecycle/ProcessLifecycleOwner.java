package androidx.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.lifecycle.ReportFragment;
import androidx.lifecycle.h;

/* compiled from: ProcessLifecycleOwner.java */
/* renamed from: androidx.lifecycle.x, reason: use source file name */
/* loaded from: classes.dex */
public class ProcessLifecycleOwner implements o {

    /* renamed from: m, reason: collision with root package name */
    private static final ProcessLifecycleOwner f3220m = new ProcessLifecycleOwner();

    /* renamed from: i, reason: collision with root package name */
    private Handler f3225i;

    /* renamed from: e, reason: collision with root package name */
    private int f3221e = 0;

    /* renamed from: f, reason: collision with root package name */
    private int f3222f = 0;

    /* renamed from: g, reason: collision with root package name */
    private boolean f3223g = true;

    /* renamed from: h, reason: collision with root package name */
    private boolean f3224h = true;

    /* renamed from: j, reason: collision with root package name */
    private final LifecycleRegistry f3226j = new LifecycleRegistry(this);

    /* renamed from: k, reason: collision with root package name */
    private Runnable f3227k = new a();

    /* renamed from: l, reason: collision with root package name */
    ReportFragment.a f3228l = new b();

    /* compiled from: ProcessLifecycleOwner.java */
    /* renamed from: androidx.lifecycle.x$a */
    /* loaded from: classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ProcessLifecycleOwner.this.f();
            ProcessLifecycleOwner.this.g();
        }
    }

    /* compiled from: ProcessLifecycleOwner.java */
    /* renamed from: androidx.lifecycle.x$b */
    /* loaded from: classes.dex */
    class b implements ReportFragment.a {
        b() {
        }

        @Override // androidx.lifecycle.ReportFragment.a
        public void a() {
        }

        @Override // androidx.lifecycle.ReportFragment.a
        public void b() {
            ProcessLifecycleOwner.this.b();
        }

        @Override // androidx.lifecycle.ReportFragment.a
        public void c() {
            ProcessLifecycleOwner.this.c();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ProcessLifecycleOwner.java */
    /* renamed from: androidx.lifecycle.x$c */
    /* loaded from: classes.dex */
    public class c extends EmptyActivityLifecycleCallbacks {

        /* compiled from: ProcessLifecycleOwner.java */
        /* renamed from: androidx.lifecycle.x$c$a */
        /* loaded from: classes.dex */
        class a extends EmptyActivityLifecycleCallbacks {
            a() {
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityPostResumed(Activity activity) {
                ProcessLifecycleOwner.this.b();
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityPostStarted(Activity activity) {
                ProcessLifecycleOwner.this.c();
            }
        }

        c() {
        }

        @Override // androidx.lifecycle.EmptyActivityLifecycleCallbacks, android.app.Application.ActivityLifecycleCallbacks
        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        @Override // androidx.lifecycle.EmptyActivityLifecycleCallbacks, android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPaused(Activity activity) {
            ProcessLifecycleOwner.this.a();
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPreCreated(Activity activity, Bundle bundle) {
            activity.registerActivityLifecycleCallbacks(new a());
        }

        @Override // androidx.lifecycle.EmptyActivityLifecycleCallbacks, android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStopped(Activity activity) {
            ProcessLifecycleOwner.this.d();
        }
    }

    private ProcessLifecycleOwner() {
    }

    public static o h() {
        return f3220m;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void i(Context context) {
        f3220m.e(context);
    }

    void a() {
        int i10 = this.f3222f - 1;
        this.f3222f = i10;
        if (i10 == 0) {
            this.f3225i.postDelayed(this.f3227k, 700L);
        }
    }

    void b() {
        int i10 = this.f3222f + 1;
        this.f3222f = i10;
        if (i10 == 1) {
            if (this.f3223g) {
                this.f3226j.h(h.b.ON_RESUME);
                this.f3223g = false;
            } else {
                this.f3225i.removeCallbacks(this.f3227k);
            }
        }
    }

    void c() {
        int i10 = this.f3221e + 1;
        this.f3221e = i10;
        if (i10 == 1 && this.f3224h) {
            this.f3226j.h(h.b.ON_START);
            this.f3224h = false;
        }
    }

    void d() {
        this.f3221e--;
        g();
    }

    void e(Context context) {
        this.f3225i = new Handler();
        this.f3226j.h(h.b.ON_CREATE);
        ((Application) context.getApplicationContext()).registerActivityLifecycleCallbacks(new c());
    }

    void f() {
        if (this.f3222f == 0) {
            this.f3223g = true;
            this.f3226j.h(h.b.ON_PAUSE);
        }
    }

    void g() {
        if (this.f3221e == 0 && this.f3223g) {
            this.f3226j.h(h.b.ON_STOP);
            this.f3224h = true;
        }
    }

    @Override // androidx.lifecycle.o
    public h getLifecycle() {
        return this.f3226j;
    }
}
