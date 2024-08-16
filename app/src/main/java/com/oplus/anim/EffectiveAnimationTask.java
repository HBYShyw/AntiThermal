package com.oplus.anim;

import android.os.Handler;
import android.os.Looper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/* compiled from: EffectiveAnimationTask.java */
/* renamed from: com.oplus.anim.f, reason: use source file name */
/* loaded from: classes.dex */
public class EffectiveAnimationTask<T> {

    /* renamed from: e, reason: collision with root package name */
    public static final Executor f9703e = Executors.newCachedThreadPool();

    /* renamed from: a, reason: collision with root package name */
    private final Set<EffectiveAnimationListener<T>> f9704a;

    /* renamed from: b, reason: collision with root package name */
    private final Set<EffectiveAnimationListener<Throwable>> f9705b;

    /* renamed from: c, reason: collision with root package name */
    private final Handler f9706c;

    /* renamed from: d, reason: collision with root package name */
    private volatile EffectiveAnimationResult<T> f9707d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: EffectiveAnimationTask.java */
    /* renamed from: com.oplus.anim.f$a */
    /* loaded from: classes.dex */
    public class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (EffectiveAnimationTask.this.f9707d == null) {
                return;
            }
            EffectiveAnimationResult effectiveAnimationResult = EffectiveAnimationTask.this.f9707d;
            if (effectiveAnimationResult.b() != null) {
                EffectiveAnimationTask.this.i(effectiveAnimationResult.b());
            } else {
                EffectiveAnimationTask.this.g(effectiveAnimationResult.a());
            }
        }
    }

    /* compiled from: EffectiveAnimationTask.java */
    /* renamed from: com.oplus.anim.f$b */
    /* loaded from: classes.dex */
    private class b extends FutureTask<EffectiveAnimationResult<T>> {
        b(Callable<EffectiveAnimationResult<T>> callable) {
            super(callable);
        }

        @Override // java.util.concurrent.FutureTask
        protected void done() {
            if (isCancelled()) {
                return;
            }
            try {
                EffectiveAnimationTask.this.l(get());
            } catch (InterruptedException | ExecutionException e10) {
                EffectiveAnimationTask.this.l(new EffectiveAnimationResult(e10));
            }
        }
    }

    public EffectiveAnimationTask(Callable<EffectiveAnimationResult<T>> callable) {
        this(callable, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void g(Throwable th) {
        ArrayList arrayList = new ArrayList(this.f9705b);
        if (arrayList.isEmpty()) {
            s4.e.d("EffectiveAnimation encountered an error but no failure listener was added:", th);
            return;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ((EffectiveAnimationListener) it.next()).a(th);
        }
    }

    private void h() {
        this.f9706c.post(new a());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void i(T t7) {
        Iterator it = new ArrayList(this.f9704a).iterator();
        while (it.hasNext()) {
            ((EffectiveAnimationListener) it.next()).a(t7);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void l(EffectiveAnimationResult<T> effectiveAnimationResult) {
        if (this.f9707d == null) {
            this.f9707d = effectiveAnimationResult;
            h();
            return;
        }
        throw new IllegalStateException("A task may only be set once.");
    }

    public synchronized EffectiveAnimationTask<T> e(EffectiveAnimationListener<Throwable> effectiveAnimationListener) {
        if (this.f9707d != null && this.f9707d.a() != null) {
            effectiveAnimationListener.a(this.f9707d.a());
        }
        this.f9705b.add(effectiveAnimationListener);
        return this;
    }

    public synchronized EffectiveAnimationTask<T> f(EffectiveAnimationListener<T> effectiveAnimationListener) {
        if (this.f9707d != null && this.f9707d.b() != null) {
            effectiveAnimationListener.a(this.f9707d.b());
        }
        this.f9704a.add(effectiveAnimationListener);
        return this;
    }

    public synchronized EffectiveAnimationTask<T> j(EffectiveAnimationListener<Throwable> effectiveAnimationListener) {
        this.f9705b.remove(effectiveAnimationListener);
        return this;
    }

    public synchronized EffectiveAnimationTask<T> k(EffectiveAnimationListener<T> effectiveAnimationListener) {
        this.f9704a.remove(effectiveAnimationListener);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public EffectiveAnimationTask(Callable<EffectiveAnimationResult<T>> callable, boolean z10) {
        this.f9704a = new LinkedHashSet(1);
        this.f9705b = new LinkedHashSet(1);
        this.f9706c = new Handler(Looper.getMainLooper());
        this.f9707d = null;
        if (z10) {
            try {
                l(callable.call());
                return;
            } catch (Throwable th) {
                l(new EffectiveAnimationResult<>(th));
                return;
            }
        }
        f9703e.execute(new b(callable));
    }
}
