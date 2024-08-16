package androidx.core.view;

import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import j.SimpleArrayMap;

/* compiled from: WindowInsetsControllerCompat.java */
/* renamed from: androidx.core.view.l0, reason: use source file name */
/* loaded from: classes.dex */
public final class WindowInsetsControllerCompat {

    /* renamed from: a, reason: collision with root package name */
    private final b f2400a;

    /* compiled from: WindowInsetsControllerCompat.java */
    /* renamed from: androidx.core.view.l0$b */
    /* loaded from: classes.dex */
    private static class b {
        b() {
        }

        public void a(boolean z10) {
            throw null;
        }

        public void b(boolean z10) {
            throw null;
        }
    }

    public WindowInsetsControllerCompat(Window window, View view) {
        this.f2400a = new a(window, this);
    }

    public void a(boolean z10) {
        this.f2400a.a(z10);
    }

    public void b(boolean z10) {
        this.f2400a.b(z10);
    }

    /* compiled from: WindowInsetsControllerCompat.java */
    /* renamed from: androidx.core.view.l0$a */
    /* loaded from: classes.dex */
    private static class a extends b {

        /* renamed from: a, reason: collision with root package name */
        final WindowInsetsControllerCompat f2401a;

        /* renamed from: b, reason: collision with root package name */
        final WindowInsetsController f2402b;

        /* renamed from: c, reason: collision with root package name */
        private final SimpleArrayMap<Object, WindowInsetsController.OnControllableInsetsChangedListener> f2403c;

        /* renamed from: d, reason: collision with root package name */
        protected Window f2404d;

        a(Window window, WindowInsetsControllerCompat windowInsetsControllerCompat) {
            this(window.getInsetsController(), windowInsetsControllerCompat);
            this.f2404d = window;
        }

        @Override // androidx.core.view.WindowInsetsControllerCompat.b
        public void a(boolean z10) {
            if (z10) {
                if (this.f2404d != null) {
                    c(16);
                }
                this.f2402b.setSystemBarsAppearance(16, 16);
            } else {
                if (this.f2404d != null) {
                    d(16);
                }
                this.f2402b.setSystemBarsAppearance(0, 16);
            }
        }

        @Override // androidx.core.view.WindowInsetsControllerCompat.b
        public void b(boolean z10) {
            if (z10) {
                if (this.f2404d != null) {
                    c(8192);
                }
                this.f2402b.setSystemBarsAppearance(8, 8);
            } else {
                if (this.f2404d != null) {
                    d(8192);
                }
                this.f2402b.setSystemBarsAppearance(0, 8);
            }
        }

        protected void c(int i10) {
            View decorView = this.f2404d.getDecorView();
            decorView.setSystemUiVisibility(i10 | decorView.getSystemUiVisibility());
        }

        protected void d(int i10) {
            View decorView = this.f2404d.getDecorView();
            decorView.setSystemUiVisibility((~i10) & decorView.getSystemUiVisibility());
        }

        a(WindowInsetsController windowInsetsController, WindowInsetsControllerCompat windowInsetsControllerCompat) {
            this.f2403c = new SimpleArrayMap<>();
            this.f2402b = windowInsetsController;
            this.f2401a = windowInsetsControllerCompat;
        }
    }
}
