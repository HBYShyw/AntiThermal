package android.view;

import android.annotation.SuppressLint;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;
import androidx.core.os.BuildCompat;
import androidx.core.util.Consumer;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.h;
import androidx.lifecycle.o;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Objects;

/* loaded from: classes.dex */
public final class OnBackPressedDispatcher {

    /* renamed from: a, reason: collision with root package name */
    private final Runnable f264a;

    /* renamed from: c, reason: collision with root package name */
    private Consumer<Boolean> f266c;

    /* renamed from: d, reason: collision with root package name */
    private OnBackInvokedCallback f267d;

    /* renamed from: e, reason: collision with root package name */
    private OnBackInvokedDispatcher f268e;

    /* renamed from: b, reason: collision with root package name */
    final ArrayDeque<OnBackPressedCallback> f265b = new ArrayDeque<>();

    /* renamed from: f, reason: collision with root package name */
    private boolean f269f = false;

    /* loaded from: classes.dex */
    private class LifecycleOnBackPressedCancellable implements LifecycleEventObserver, android.view.a {

        /* renamed from: e, reason: collision with root package name */
        private final h f270e;

        /* renamed from: f, reason: collision with root package name */
        private final OnBackPressedCallback f271f;

        /* renamed from: g, reason: collision with root package name */
        private android.view.a f272g;

        LifecycleOnBackPressedCancellable(h hVar, OnBackPressedCallback onBackPressedCallback) {
            this.f270e = hVar;
            this.f271f = onBackPressedCallback;
            hVar.a(this);
        }

        @Override // androidx.lifecycle.LifecycleEventObserver
        public void a(o oVar, h.b bVar) {
            if (bVar == h.b.ON_START) {
                this.f272g = OnBackPressedDispatcher.this.c(this.f271f);
                return;
            }
            if (bVar == h.b.ON_STOP) {
                android.view.a aVar = this.f272g;
                if (aVar != null) {
                    aVar.cancel();
                    return;
                }
                return;
            }
            if (bVar == h.b.ON_DESTROY) {
                cancel();
            }
        }

        @Override // android.view.a
        public void cancel() {
            this.f270e.c(this);
            this.f271f.e(this);
            android.view.a aVar = this.f272g;
            if (aVar != null) {
                aVar.cancel();
                this.f272g = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class a {
        static OnBackInvokedCallback a(Runnable runnable) {
            Objects.requireNonNull(runnable);
            return new j(runnable);
        }

        static void b(Object obj, int i10, Object obj2) {
            ((OnBackInvokedDispatcher) obj).registerOnBackInvokedCallback(i10, (OnBackInvokedCallback) obj2);
        }

        static void c(Object obj, Object obj2) {
            ((OnBackInvokedDispatcher) obj).unregisterOnBackInvokedCallback((OnBackInvokedCallback) obj2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class b implements android.view.a {

        /* renamed from: e, reason: collision with root package name */
        private final OnBackPressedCallback f274e;

        b(OnBackPressedCallback onBackPressedCallback) {
            this.f274e = onBackPressedCallback;
        }

        @Override // android.view.a
        public void cancel() {
            OnBackPressedDispatcher.this.f265b.remove(this.f274e);
            this.f274e.e(this);
            if (BuildCompat.c()) {
                this.f274e.g(null);
                OnBackPressedDispatcher.this.h();
            }
        }
    }

    public OnBackPressedDispatcher(Runnable runnable) {
        this.f264a = runnable;
        if (BuildCompat.c()) {
            this.f266c = new Consumer() { // from class: androidx.activity.h
                @Override // androidx.core.util.Consumer
                public final void accept(Object obj) {
                    OnBackPressedDispatcher.this.e((Boolean) obj);
                }
            };
            this.f267d = a.a(new Runnable() { // from class: androidx.activity.i
                @Override // java.lang.Runnable
                public final void run() {
                    OnBackPressedDispatcher.this.f();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void e(Boolean bool) {
        if (BuildCompat.c()) {
            h();
        }
    }

    @SuppressLint({"LambdaLast"})
    public void b(o oVar, OnBackPressedCallback onBackPressedCallback) {
        h lifecycle = oVar.getLifecycle();
        if (lifecycle.b() == h.c.DESTROYED) {
            return;
        }
        onBackPressedCallback.a(new LifecycleOnBackPressedCancellable(lifecycle, onBackPressedCallback));
        if (BuildCompat.c()) {
            h();
            onBackPressedCallback.g(this.f266c);
        }
    }

    android.view.a c(OnBackPressedCallback onBackPressedCallback) {
        this.f265b.add(onBackPressedCallback);
        b bVar = new b(onBackPressedCallback);
        onBackPressedCallback.a(bVar);
        if (BuildCompat.c()) {
            h();
            onBackPressedCallback.g(this.f266c);
        }
        return bVar;
    }

    public boolean d() {
        Iterator<OnBackPressedCallback> descendingIterator = this.f265b.descendingIterator();
        while (descendingIterator.hasNext()) {
            if (descendingIterator.next().c()) {
                return true;
            }
        }
        return false;
    }

    public void f() {
        Iterator<OnBackPressedCallback> descendingIterator = this.f265b.descendingIterator();
        while (descendingIterator.hasNext()) {
            OnBackPressedCallback next = descendingIterator.next();
            if (next.c()) {
                next.b();
                return;
            }
        }
        Runnable runnable = this.f264a;
        if (runnable != null) {
            runnable.run();
        }
    }

    public void g(OnBackInvokedDispatcher onBackInvokedDispatcher) {
        this.f268e = onBackInvokedDispatcher;
        h();
    }

    void h() {
        boolean d10 = d();
        OnBackInvokedDispatcher onBackInvokedDispatcher = this.f268e;
        if (onBackInvokedDispatcher != null) {
            if (d10 && !this.f269f) {
                a.b(onBackInvokedDispatcher, 0, this.f267d);
                this.f269f = true;
            } else {
                if (d10 || !this.f269f) {
                    return;
                }
                a.c(onBackInvokedDispatcher, this.f267d);
                this.f269f = false;
            }
        }
    }
}
