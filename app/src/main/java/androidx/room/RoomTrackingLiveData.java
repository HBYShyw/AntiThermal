package androidx.room;

import android.annotation.SuppressLint;
import androidx.lifecycle.LiveData;
import androidx.room.InvalidationTracker;
import g.ArchTaskExecutor;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: RoomTrackingLiveData.java */
/* renamed from: androidx.room.j, reason: use source file name */
/* loaded from: classes.dex */
public class RoomTrackingLiveData<T> extends LiveData<T> {

    /* renamed from: l, reason: collision with root package name */
    final RoomDatabase f3929l;

    /* renamed from: m, reason: collision with root package name */
    final boolean f3930m;

    /* renamed from: n, reason: collision with root package name */
    final Callable<T> f3931n;

    /* renamed from: o, reason: collision with root package name */
    private final InvalidationLiveDataContainer f3932o;

    /* renamed from: p, reason: collision with root package name */
    final InvalidationTracker.c f3933p;

    /* renamed from: q, reason: collision with root package name */
    final AtomicBoolean f3934q = new AtomicBoolean(true);

    /* renamed from: r, reason: collision with root package name */
    final AtomicBoolean f3935r = new AtomicBoolean(false);

    /* renamed from: s, reason: collision with root package name */
    final AtomicBoolean f3936s = new AtomicBoolean(false);

    /* renamed from: t, reason: collision with root package name */
    final Runnable f3937t = new a();

    /* renamed from: u, reason: collision with root package name */
    final Runnable f3938u = new b();

    /* compiled from: RoomTrackingLiveData.java */
    /* renamed from: androidx.room.j$a */
    /* loaded from: classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            boolean z10;
            if (RoomTrackingLiveData.this.f3936s.compareAndSet(false, true)) {
                RoomTrackingLiveData.this.f3929l.i().b(RoomTrackingLiveData.this.f3933p);
            }
            do {
                if (RoomTrackingLiveData.this.f3935r.compareAndSet(false, true)) {
                    T t7 = null;
                    z10 = false;
                    while (RoomTrackingLiveData.this.f3934q.compareAndSet(true, false)) {
                        try {
                            try {
                                t7 = RoomTrackingLiveData.this.f3931n.call();
                                z10 = true;
                            } catch (Exception e10) {
                                throw new RuntimeException("Exception while computing database live data.", e10);
                            }
                        } finally {
                            RoomTrackingLiveData.this.f3935r.set(false);
                        }
                    }
                    if (z10) {
                        RoomTrackingLiveData.this.k(t7);
                    }
                } else {
                    z10 = false;
                }
                if (!z10) {
                    return;
                }
            } while (RoomTrackingLiveData.this.f3934q.get());
        }
    }

    /* compiled from: RoomTrackingLiveData.java */
    /* renamed from: androidx.room.j$b */
    /* loaded from: classes.dex */
    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            boolean f10 = RoomTrackingLiveData.this.f();
            if (RoomTrackingLiveData.this.f3934q.compareAndSet(false, true) && f10) {
                RoomTrackingLiveData.this.o().execute(RoomTrackingLiveData.this.f3937t);
            }
        }
    }

    /* compiled from: RoomTrackingLiveData.java */
    /* renamed from: androidx.room.j$c */
    /* loaded from: classes.dex */
    class c extends InvalidationTracker.c {
        c(String[] strArr) {
            super(strArr);
        }

        @Override // androidx.room.InvalidationTracker.c
        public void b(Set<String> set) {
            ArchTaskExecutor.f().b(RoomTrackingLiveData.this.f3938u);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @SuppressLint({"RestrictedApi"})
    public RoomTrackingLiveData(RoomDatabase roomDatabase, InvalidationLiveDataContainer invalidationLiveDataContainer, boolean z10, Callable<T> callable, String[] strArr) {
        this.f3929l = roomDatabase;
        this.f3930m = z10;
        this.f3931n = callable;
        this.f3932o = invalidationLiveDataContainer;
        this.f3933p = new c(strArr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.lifecycle.LiveData
    public void i() {
        super.i();
        this.f3932o.b(this);
        o().execute(this.f3937t);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.lifecycle.LiveData
    public void j() {
        super.j();
        this.f3932o.c(this);
    }

    Executor o() {
        if (this.f3930m) {
            return this.f3929l.l();
        }
        return this.f3929l.k();
    }
}
