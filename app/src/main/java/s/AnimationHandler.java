package s;

import android.os.SystemClock;
import android.view.Choreographer;
import j.SimpleArrayMap;
import java.util.ArrayList;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: AnimationHandler.java */
/* renamed from: s.a, reason: use source file name */
/* loaded from: classes.dex */
public class AnimationHandler {

    /* renamed from: g, reason: collision with root package name */
    public static final ThreadLocal<AnimationHandler> f17846g = new ThreadLocal<>();

    /* renamed from: d, reason: collision with root package name */
    private c f17850d;

    /* renamed from: a, reason: collision with root package name */
    private final SimpleArrayMap<b, Long> f17847a = new SimpleArrayMap<>();

    /* renamed from: b, reason: collision with root package name */
    final ArrayList<b> f17848b = new ArrayList<>();

    /* renamed from: c, reason: collision with root package name */
    private final a f17849c = new a();

    /* renamed from: e, reason: collision with root package name */
    long f17851e = 0;

    /* renamed from: f, reason: collision with root package name */
    private boolean f17852f = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AnimationHandler.java */
    /* renamed from: s.a$a */
    /* loaded from: classes.dex */
    public class a {
        a() {
        }

        void a() {
            AnimationHandler.this.f17851e = SystemClock.uptimeMillis();
            AnimationHandler animationHandler = AnimationHandler.this;
            animationHandler.c(animationHandler.f17851e);
            if (AnimationHandler.this.f17848b.size() > 0) {
                AnimationHandler.this.e().a();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AnimationHandler.java */
    /* renamed from: s.a$b */
    /* loaded from: classes.dex */
    public interface b {
        boolean doAnimationFrame(long j10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AnimationHandler.java */
    /* renamed from: s.a$c */
    /* loaded from: classes.dex */
    public static abstract class c {

        /* renamed from: a, reason: collision with root package name */
        final a f17854a;

        c(a aVar) {
            this.f17854a = aVar;
        }

        abstract void a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AnimationHandler.java */
    /* renamed from: s.a$d */
    /* loaded from: classes.dex */
    public static class d extends c {

        /* renamed from: b, reason: collision with root package name */
        private final Choreographer f17855b;

        /* renamed from: c, reason: collision with root package name */
        private final Choreographer.FrameCallback f17856c;

        /* compiled from: AnimationHandler.java */
        /* renamed from: s.a$d$a */
        /* loaded from: classes.dex */
        class a implements Choreographer.FrameCallback {
            a() {
            }

            @Override // android.view.Choreographer.FrameCallback
            public void doFrame(long j10) {
                d.this.f17854a.a();
            }
        }

        d(a aVar) {
            super(aVar);
            this.f17855b = Choreographer.getInstance();
            this.f17856c = new a();
        }

        @Override // s.AnimationHandler.c
        void a() {
            this.f17855b.postFrameCallback(this.f17856c);
        }
    }

    AnimationHandler() {
    }

    private void b() {
        if (this.f17852f) {
            for (int size = this.f17848b.size() - 1; size >= 0; size--) {
                if (this.f17848b.get(size) == null) {
                    this.f17848b.remove(size);
                }
            }
            this.f17852f = false;
        }
    }

    public static AnimationHandler d() {
        ThreadLocal<AnimationHandler> threadLocal = f17846g;
        if (threadLocal.get() == null) {
            threadLocal.set(new AnimationHandler());
        }
        return threadLocal.get();
    }

    private boolean f(b bVar, long j10) {
        Long l10 = this.f17847a.get(bVar);
        if (l10 == null) {
            return true;
        }
        if (l10.longValue() >= j10) {
            return false;
        }
        this.f17847a.remove(bVar);
        return true;
    }

    public void a(b bVar, long j10) {
        if (this.f17848b.size() == 0) {
            e().a();
        }
        if (!this.f17848b.contains(bVar)) {
            this.f17848b.add(bVar);
        }
        if (j10 > 0) {
            this.f17847a.put(bVar, Long.valueOf(SystemClock.uptimeMillis() + j10));
        }
    }

    void c(long j10) {
        long uptimeMillis = SystemClock.uptimeMillis();
        for (int i10 = 0; i10 < this.f17848b.size(); i10++) {
            b bVar = this.f17848b.get(i10);
            if (bVar != null && f(bVar, uptimeMillis)) {
                bVar.doAnimationFrame(j10);
            }
        }
        b();
    }

    c e() {
        if (this.f17850d == null) {
            this.f17850d = new d(this.f17849c);
        }
        return this.f17850d;
    }

    public void g(b bVar) {
        this.f17847a.remove(bVar);
        int indexOf = this.f17848b.indexOf(bVar);
        if (indexOf >= 0) {
            this.f17848b.set(indexOf, null);
            this.f17852f = true;
        }
    }
}
