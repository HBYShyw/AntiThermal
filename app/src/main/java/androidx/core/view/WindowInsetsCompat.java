package androidx.core.view;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import androidx.core.graphics.Insets;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Preconditions;
import java.lang.reflect.Field;
import java.util.Objects;

/* compiled from: WindowInsetsCompat.java */
/* renamed from: androidx.core.view.k0, reason: use source file name */
/* loaded from: classes.dex */
public class WindowInsetsCompat {

    /* renamed from: b, reason: collision with root package name */
    public static final WindowInsetsCompat f2378b = j.f2397l;

    /* renamed from: a, reason: collision with root package name */
    private final k f2379a;

    /* compiled from: WindowInsetsCompat.java */
    @SuppressLint({"SoonBlockedPrivateApi"})
    /* renamed from: androidx.core.view.k0$a */
    /* loaded from: classes.dex */
    static class a {

        /* renamed from: a, reason: collision with root package name */
        private static Field f2380a;

        /* renamed from: b, reason: collision with root package name */
        private static Field f2381b;

        /* renamed from: c, reason: collision with root package name */
        private static Field f2382c;

        /* renamed from: d, reason: collision with root package name */
        private static boolean f2383d;

        static {
            try {
                Field declaredField = View.class.getDeclaredField("mAttachInfo");
                f2380a = declaredField;
                declaredField.setAccessible(true);
                Class<?> cls = Class.forName("android.view.View$AttachInfo");
                Field declaredField2 = cls.getDeclaredField("mStableInsets");
                f2381b = declaredField2;
                declaredField2.setAccessible(true);
                Field declaredField3 = cls.getDeclaredField("mContentInsets");
                f2382c = declaredField3;
                declaredField3.setAccessible(true);
                f2383d = true;
            } catch (ReflectiveOperationException e10) {
                Log.w("WindowInsetsCompat", "Failed to get visible insets from AttachInfo " + e10.getMessage(), e10);
            }
        }

        public static WindowInsetsCompat a(View view) {
            if (f2383d && view.isAttachedToWindow()) {
                try {
                    Object obj = f2380a.get(view.getRootView());
                    if (obj != null) {
                        Rect rect = (Rect) f2381b.get(obj);
                        Rect rect2 = (Rect) f2382c.get(obj);
                        if (rect != null && rect2 != null) {
                            WindowInsetsCompat a10 = new b().b(Insets.c(rect)).c(Insets.c(rect2)).a();
                            a10.t(a10);
                            a10.d(view.getRootView());
                            return a10;
                        }
                    }
                } catch (IllegalAccessException e10) {
                    Log.w("WindowInsetsCompat", "Failed to get insets from AttachInfo. " + e10.getMessage(), e10);
                }
            }
            return null;
        }
    }

    /* compiled from: WindowInsetsCompat.java */
    /* renamed from: androidx.core.view.k0$d */
    /* loaded from: classes.dex */
    private static class d extends c {
        d() {
        }

        d(WindowInsetsCompat windowInsetsCompat) {
            super(windowInsetsCompat);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: WindowInsetsCompat.java */
    /* renamed from: androidx.core.view.k0$e */
    /* loaded from: classes.dex */
    public static class e {

        /* renamed from: a, reason: collision with root package name */
        private final WindowInsetsCompat f2386a;

        /* renamed from: b, reason: collision with root package name */
        Insets[] f2387b;

        e() {
            this(new WindowInsetsCompat((WindowInsetsCompat) null));
        }

        protected final void a() {
            Insets[] insetsArr = this.f2387b;
            if (insetsArr != null) {
                Insets insets = insetsArr[l.a(1)];
                Insets insets2 = this.f2387b[l.a(2)];
                if (insets2 == null) {
                    insets2 = this.f2386a.f(2);
                }
                if (insets == null) {
                    insets = this.f2386a.f(1);
                }
                f(Insets.a(insets, insets2));
                Insets insets3 = this.f2387b[l.a(16)];
                if (insets3 != null) {
                    e(insets3);
                }
                Insets insets4 = this.f2387b[l.a(32)];
                if (insets4 != null) {
                    c(insets4);
                }
                Insets insets5 = this.f2387b[l.a(64)];
                if (insets5 != null) {
                    g(insets5);
                }
            }
        }

        WindowInsetsCompat b() {
            throw null;
        }

        void c(Insets insets) {
            throw null;
        }

        void d(Insets insets) {
            throw null;
        }

        void e(Insets insets) {
            throw null;
        }

        void f(Insets insets) {
            throw null;
        }

        void g(Insets insets) {
            throw null;
        }

        e(WindowInsetsCompat windowInsetsCompat) {
            this.f2386a = windowInsetsCompat;
        }
    }

    /* compiled from: WindowInsetsCompat.java */
    /* renamed from: androidx.core.view.k0$h */
    /* loaded from: classes.dex */
    private static class h extends g {
        h(WindowInsetsCompat windowInsetsCompat, WindowInsets windowInsets) {
            super(windowInsetsCompat, windowInsets);
        }

        @Override // androidx.core.view.WindowInsetsCompat.k
        WindowInsetsCompat a() {
            return WindowInsetsCompat.v(this.f2388c.consumeDisplayCutout());
        }

        @Override // androidx.core.view.WindowInsetsCompat.f, androidx.core.view.WindowInsetsCompat.k
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof h)) {
                return false;
            }
            h hVar = (h) obj;
            return Objects.equals(this.f2388c, hVar.f2388c) && Objects.equals(this.f2392g, hVar.f2392g);
        }

        @Override // androidx.core.view.WindowInsetsCompat.k
        DisplayCutoutCompat f() {
            return DisplayCutoutCompat.e(this.f2388c.getDisplayCutout());
        }

        @Override // androidx.core.view.WindowInsetsCompat.k
        public int hashCode() {
            return this.f2388c.hashCode();
        }

        h(WindowInsetsCompat windowInsetsCompat, h hVar) {
            super(windowInsetsCompat, hVar);
        }
    }

    /* compiled from: WindowInsetsCompat.java */
    /* renamed from: androidx.core.view.k0$j */
    /* loaded from: classes.dex */
    private static class j extends i {

        /* renamed from: l, reason: collision with root package name */
        static final WindowInsetsCompat f2397l = WindowInsetsCompat.v(WindowInsets.CONSUMED);

        j(WindowInsetsCompat windowInsetsCompat, WindowInsets windowInsets) {
            super(windowInsetsCompat, windowInsets);
        }

        @Override // androidx.core.view.WindowInsetsCompat.f, androidx.core.view.WindowInsetsCompat.k
        final void d(View view) {
        }

        @Override // androidx.core.view.WindowInsetsCompat.f, androidx.core.view.WindowInsetsCompat.k
        public Insets g(int i10) {
            return Insets.d(this.f2388c.getInsets(m.a(i10)));
        }

        j(WindowInsetsCompat windowInsetsCompat, j jVar) {
            super(windowInsetsCompat, jVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: WindowInsetsCompat.java */
    /* renamed from: androidx.core.view.k0$k */
    /* loaded from: classes.dex */
    public static class k {

        /* renamed from: b, reason: collision with root package name */
        static final WindowInsetsCompat f2398b = new b().a().a().b().c();

        /* renamed from: a, reason: collision with root package name */
        final WindowInsetsCompat f2399a;

        k(WindowInsetsCompat windowInsetsCompat) {
            this.f2399a = windowInsetsCompat;
        }

        WindowInsetsCompat a() {
            return this.f2399a;
        }

        WindowInsetsCompat b() {
            return this.f2399a;
        }

        WindowInsetsCompat c() {
            return this.f2399a;
        }

        void d(View view) {
        }

        void e(WindowInsetsCompat windowInsetsCompat) {
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof k)) {
                return false;
            }
            k kVar = (k) obj;
            return o() == kVar.o() && n() == kVar.n() && ObjectsCompat.a(k(), kVar.k()) && ObjectsCompat.a(i(), kVar.i()) && ObjectsCompat.a(f(), kVar.f());
        }

        DisplayCutoutCompat f() {
            return null;
        }

        Insets g(int i10) {
            return Insets.f2184e;
        }

        Insets h() {
            return k();
        }

        public int hashCode() {
            return ObjectsCompat.b(Boolean.valueOf(o()), Boolean.valueOf(n()), k(), i(), f());
        }

        Insets i() {
            return Insets.f2184e;
        }

        Insets j() {
            return k();
        }

        Insets k() {
            return Insets.f2184e;
        }

        Insets l() {
            return k();
        }

        WindowInsetsCompat m(int i10, int i11, int i12, int i13) {
            return f2398b;
        }

        boolean n() {
            return false;
        }

        boolean o() {
            return false;
        }

        public void p(Insets[] insetsArr) {
        }

        void q(Insets insets) {
        }

        void r(WindowInsetsCompat windowInsetsCompat) {
        }
    }

    /* compiled from: WindowInsetsCompat.java */
    /* renamed from: androidx.core.view.k0$l */
    /* loaded from: classes.dex */
    public static final class l {
        static int a(int i10) {
            if (i10 == 1) {
                return 0;
            }
            if (i10 == 2) {
                return 1;
            }
            if (i10 == 4) {
                return 2;
            }
            if (i10 == 8) {
                return 3;
            }
            if (i10 == 16) {
                return 4;
            }
            if (i10 == 32) {
                return 5;
            }
            if (i10 == 64) {
                return 6;
            }
            if (i10 == 128) {
                return 7;
            }
            if (i10 == 256) {
                return 8;
            }
            throw new IllegalArgumentException("type needs to be >= FIRST and <= LAST, type=" + i10);
        }

        public static int b() {
            return 32;
        }

        public static int c() {
            return 7;
        }
    }

    /* compiled from: WindowInsetsCompat.java */
    /* renamed from: androidx.core.view.k0$m */
    /* loaded from: classes.dex */
    private static final class m {
        static int a(int i10) {
            int statusBars;
            int i11 = 0;
            for (int i12 = 1; i12 <= 256; i12 <<= 1) {
                if ((i10 & i12) != 0) {
                    if (i12 == 1) {
                        statusBars = WindowInsets.Type.statusBars();
                    } else if (i12 == 2) {
                        statusBars = WindowInsets.Type.navigationBars();
                    } else if (i12 == 4) {
                        statusBars = WindowInsets.Type.captionBar();
                    } else if (i12 == 8) {
                        statusBars = WindowInsets.Type.ime();
                    } else if (i12 == 16) {
                        statusBars = WindowInsets.Type.systemGestures();
                    } else if (i12 == 32) {
                        statusBars = WindowInsets.Type.mandatorySystemGestures();
                    } else if (i12 == 64) {
                        statusBars = WindowInsets.Type.tappableElement();
                    } else if (i12 == 128) {
                        statusBars = WindowInsets.Type.displayCutout();
                    }
                    i11 |= statusBars;
                }
            }
            return i11;
        }
    }

    private WindowInsetsCompat(WindowInsets windowInsets) {
        this.f2379a = new j(this, windowInsets);
    }

    static Insets o(Insets insets, int i10, int i11, int i12, int i13) {
        int max = Math.max(0, insets.f2185a - i10);
        int max2 = Math.max(0, insets.f2186b - i11);
        int max3 = Math.max(0, insets.f2187c - i12);
        int max4 = Math.max(0, insets.f2188d - i13);
        return (max == i10 && max2 == i11 && max3 == i12 && max4 == i13) ? insets : Insets.b(max, max2, max3, max4);
    }

    public static WindowInsetsCompat v(WindowInsets windowInsets) {
        return w(windowInsets, null);
    }

    public static WindowInsetsCompat w(WindowInsets windowInsets, View view) {
        WindowInsetsCompat windowInsetsCompat = new WindowInsetsCompat((WindowInsets) Preconditions.d(windowInsets));
        if (view != null && ViewCompat.P(view)) {
            windowInsetsCompat.t(ViewCompat.E(view));
            windowInsetsCompat.d(view.getRootView());
        }
        return windowInsetsCompat;
    }

    @Deprecated
    public WindowInsetsCompat a() {
        return this.f2379a.a();
    }

    @Deprecated
    public WindowInsetsCompat b() {
        return this.f2379a.b();
    }

    @Deprecated
    public WindowInsetsCompat c() {
        return this.f2379a.c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(View view) {
        this.f2379a.d(view);
    }

    public DisplayCutoutCompat e() {
        return this.f2379a.f();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof WindowInsetsCompat) {
            return ObjectsCompat.a(this.f2379a, ((WindowInsetsCompat) obj).f2379a);
        }
        return false;
    }

    public Insets f(int i10) {
        return this.f2379a.g(i10);
    }

    @Deprecated
    public Insets g() {
        return this.f2379a.i();
    }

    @Deprecated
    public Insets h() {
        return this.f2379a.j();
    }

    public int hashCode() {
        k kVar = this.f2379a;
        if (kVar == null) {
            return 0;
        }
        return kVar.hashCode();
    }

    @Deprecated
    public int i() {
        return this.f2379a.k().f2188d;
    }

    @Deprecated
    public int j() {
        return this.f2379a.k().f2185a;
    }

    @Deprecated
    public int k() {
        return this.f2379a.k().f2187c;
    }

    @Deprecated
    public int l() {
        return this.f2379a.k().f2186b;
    }

    @Deprecated
    public boolean m() {
        return !this.f2379a.k().equals(Insets.f2184e);
    }

    public WindowInsetsCompat n(int i10, int i11, int i12, int i13) {
        return this.f2379a.m(i10, i11, i12, i13);
    }

    public boolean p() {
        return this.f2379a.n();
    }

    @Deprecated
    public WindowInsetsCompat q(int i10, int i11, int i12, int i13) {
        return new b(this).c(Insets.b(i10, i11, i12, i13)).a();
    }

    void r(Insets[] insetsArr) {
        this.f2379a.p(insetsArr);
    }

    void s(Insets insets) {
        this.f2379a.q(insets);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void t(WindowInsetsCompat windowInsetsCompat) {
        this.f2379a.r(windowInsetsCompat);
    }

    public WindowInsets u() {
        k kVar = this.f2379a;
        if (kVar instanceof f) {
            return ((f) kVar).f2388c;
        }
        return null;
    }

    /* compiled from: WindowInsetsCompat.java */
    /* renamed from: androidx.core.view.k0$b */
    /* loaded from: classes.dex */
    public static final class b {

        /* renamed from: a, reason: collision with root package name */
        private final e f2384a;

        public b() {
            this.f2384a = new d();
        }

        public WindowInsetsCompat a() {
            return this.f2384a.b();
        }

        @Deprecated
        public b b(Insets insets) {
            this.f2384a.d(insets);
            return this;
        }

        @Deprecated
        public b c(Insets insets) {
            this.f2384a.f(insets);
            return this;
        }

        public b(WindowInsetsCompat windowInsetsCompat) {
            this.f2384a = new d(windowInsetsCompat);
        }
    }

    /* compiled from: WindowInsetsCompat.java */
    /* renamed from: androidx.core.view.k0$c */
    /* loaded from: classes.dex */
    private static class c extends e {

        /* renamed from: c, reason: collision with root package name */
        final WindowInsets.Builder f2385c;

        c() {
            this.f2385c = new WindowInsets.Builder();
        }

        @Override // androidx.core.view.WindowInsetsCompat.e
        WindowInsetsCompat b() {
            a();
            WindowInsetsCompat v7 = WindowInsetsCompat.v(this.f2385c.build());
            v7.r(this.f2387b);
            return v7;
        }

        @Override // androidx.core.view.WindowInsetsCompat.e
        void c(Insets insets) {
            this.f2385c.setMandatorySystemGestureInsets(insets.e());
        }

        @Override // androidx.core.view.WindowInsetsCompat.e
        void d(Insets insets) {
            this.f2385c.setStableInsets(insets.e());
        }

        @Override // androidx.core.view.WindowInsetsCompat.e
        void e(Insets insets) {
            this.f2385c.setSystemGestureInsets(insets.e());
        }

        @Override // androidx.core.view.WindowInsetsCompat.e
        void f(Insets insets) {
            this.f2385c.setSystemWindowInsets(insets.e());
        }

        @Override // androidx.core.view.WindowInsetsCompat.e
        void g(Insets insets) {
            this.f2385c.setTappableElementInsets(insets.e());
        }

        c(WindowInsetsCompat windowInsetsCompat) {
            super(windowInsetsCompat);
            WindowInsets.Builder builder;
            WindowInsets u7 = windowInsetsCompat.u();
            if (u7 != null) {
                builder = new WindowInsets.Builder(u7);
            } else {
                builder = new WindowInsets.Builder();
            }
            this.f2385c = builder;
        }
    }

    /* compiled from: WindowInsetsCompat.java */
    /* renamed from: androidx.core.view.k0$g */
    /* loaded from: classes.dex */
    private static class g extends f {

        /* renamed from: h, reason: collision with root package name */
        private Insets f2393h;

        g(WindowInsetsCompat windowInsetsCompat, WindowInsets windowInsets) {
            super(windowInsetsCompat, windowInsets);
            this.f2393h = null;
        }

        @Override // androidx.core.view.WindowInsetsCompat.k
        WindowInsetsCompat b() {
            return WindowInsetsCompat.v(this.f2388c.consumeStableInsets());
        }

        @Override // androidx.core.view.WindowInsetsCompat.k
        WindowInsetsCompat c() {
            return WindowInsetsCompat.v(this.f2388c.consumeSystemWindowInsets());
        }

        @Override // androidx.core.view.WindowInsetsCompat.k
        final Insets i() {
            if (this.f2393h == null) {
                this.f2393h = Insets.b(this.f2388c.getStableInsetLeft(), this.f2388c.getStableInsetTop(), this.f2388c.getStableInsetRight(), this.f2388c.getStableInsetBottom());
            }
            return this.f2393h;
        }

        @Override // androidx.core.view.WindowInsetsCompat.k
        boolean n() {
            return this.f2388c.isConsumed();
        }

        g(WindowInsetsCompat windowInsetsCompat, g gVar) {
            super(windowInsetsCompat, gVar);
            this.f2393h = null;
            this.f2393h = gVar.f2393h;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: WindowInsetsCompat.java */
    /* renamed from: androidx.core.view.k0$f */
    /* loaded from: classes.dex */
    public static class f extends k {

        /* renamed from: c, reason: collision with root package name */
        final WindowInsets f2388c;

        /* renamed from: d, reason: collision with root package name */
        private Insets[] f2389d;

        /* renamed from: e, reason: collision with root package name */
        private Insets f2390e;

        /* renamed from: f, reason: collision with root package name */
        private WindowInsetsCompat f2391f;

        /* renamed from: g, reason: collision with root package name */
        Insets f2392g;

        f(WindowInsetsCompat windowInsetsCompat, WindowInsets windowInsets) {
            super(windowInsetsCompat);
            this.f2390e = null;
            this.f2388c = windowInsets;
        }

        @SuppressLint({"WrongConstant"})
        private Insets s(int i10, boolean z10) {
            Insets insets = Insets.f2184e;
            for (int i11 = 1; i11 <= 256; i11 <<= 1) {
                if ((i10 & i11) != 0) {
                    insets = Insets.a(insets, t(i11, z10));
                }
            }
            return insets;
        }

        private Insets u() {
            WindowInsetsCompat windowInsetsCompat = this.f2391f;
            if (windowInsetsCompat != null) {
                return windowInsetsCompat.g();
            }
            return Insets.f2184e;
        }

        private Insets v(View view) {
            throw new UnsupportedOperationException("getVisibleInsets() should not be called on API >= 30. Use WindowInsets.isVisible() instead.");
        }

        @Override // androidx.core.view.WindowInsetsCompat.k
        void d(View view) {
            Insets v7 = v(view);
            if (v7 == null) {
                v7 = Insets.f2184e;
            }
            q(v7);
        }

        @Override // androidx.core.view.WindowInsetsCompat.k
        void e(WindowInsetsCompat windowInsetsCompat) {
            windowInsetsCompat.t(this.f2391f);
            windowInsetsCompat.s(this.f2392g);
        }

        @Override // androidx.core.view.WindowInsetsCompat.k
        public boolean equals(Object obj) {
            if (super.equals(obj)) {
                return Objects.equals(this.f2392g, ((f) obj).f2392g);
            }
            return false;
        }

        @Override // androidx.core.view.WindowInsetsCompat.k
        public Insets g(int i10) {
            return s(i10, false);
        }

        @Override // androidx.core.view.WindowInsetsCompat.k
        final Insets k() {
            if (this.f2390e == null) {
                this.f2390e = Insets.b(this.f2388c.getSystemWindowInsetLeft(), this.f2388c.getSystemWindowInsetTop(), this.f2388c.getSystemWindowInsetRight(), this.f2388c.getSystemWindowInsetBottom());
            }
            return this.f2390e;
        }

        @Override // androidx.core.view.WindowInsetsCompat.k
        WindowInsetsCompat m(int i10, int i11, int i12, int i13) {
            b bVar = new b(WindowInsetsCompat.v(this.f2388c));
            bVar.c(WindowInsetsCompat.o(k(), i10, i11, i12, i13));
            bVar.b(WindowInsetsCompat.o(i(), i10, i11, i12, i13));
            return bVar.a();
        }

        @Override // androidx.core.view.WindowInsetsCompat.k
        boolean o() {
            return this.f2388c.isRound();
        }

        @Override // androidx.core.view.WindowInsetsCompat.k
        public void p(Insets[] insetsArr) {
            this.f2389d = insetsArr;
        }

        @Override // androidx.core.view.WindowInsetsCompat.k
        void q(Insets insets) {
            this.f2392g = insets;
        }

        @Override // androidx.core.view.WindowInsetsCompat.k
        void r(WindowInsetsCompat windowInsetsCompat) {
            this.f2391f = windowInsetsCompat;
        }

        protected Insets t(int i10, boolean z10) {
            Insets g6;
            int i11;
            DisplayCutoutCompat f10;
            if (i10 == 1) {
                if (z10) {
                    return Insets.b(0, Math.max(u().f2186b, k().f2186b), 0, 0);
                }
                return Insets.b(0, k().f2186b, 0, 0);
            }
            if (i10 == 2) {
                if (z10) {
                    Insets u7 = u();
                    Insets i12 = i();
                    return Insets.b(Math.max(u7.f2185a, i12.f2185a), 0, Math.max(u7.f2187c, i12.f2187c), Math.max(u7.f2188d, i12.f2188d));
                }
                Insets k10 = k();
                WindowInsetsCompat windowInsetsCompat = this.f2391f;
                g6 = windowInsetsCompat != null ? windowInsetsCompat.g() : null;
                int i13 = k10.f2188d;
                if (g6 != null) {
                    i13 = Math.min(i13, g6.f2188d);
                }
                return Insets.b(k10.f2185a, 0, k10.f2187c, i13);
            }
            if (i10 != 8) {
                if (i10 == 16) {
                    return j();
                }
                if (i10 == 32) {
                    return h();
                }
                if (i10 == 64) {
                    return l();
                }
                if (i10 != 128) {
                    return Insets.f2184e;
                }
                WindowInsetsCompat windowInsetsCompat2 = this.f2391f;
                if (windowInsetsCompat2 != null) {
                    f10 = windowInsetsCompat2.e();
                } else {
                    f10 = f();
                }
                if (f10 != null) {
                    return Insets.b(f10.b(), f10.d(), f10.c(), f10.a());
                }
                return Insets.f2184e;
            }
            Insets[] insetsArr = this.f2389d;
            g6 = insetsArr != null ? insetsArr[l.a(8)] : null;
            if (g6 != null) {
                return g6;
            }
            Insets k11 = k();
            Insets u10 = u();
            int i14 = k11.f2188d;
            if (i14 > u10.f2188d) {
                return Insets.b(0, 0, 0, i14);
            }
            Insets insets = this.f2392g;
            if (insets != null && !insets.equals(Insets.f2184e) && (i11 = this.f2392g.f2188d) > u10.f2188d) {
                return Insets.b(0, 0, 0, i11);
            }
            return Insets.f2184e;
        }

        f(WindowInsetsCompat windowInsetsCompat, f fVar) {
            this(windowInsetsCompat, new WindowInsets(fVar.f2388c));
        }
    }

    public WindowInsetsCompat(WindowInsetsCompat windowInsetsCompat) {
        if (windowInsetsCompat != null) {
            k kVar = windowInsetsCompat.f2379a;
            if (kVar instanceof j) {
                this.f2379a = new j(this, (j) kVar);
            } else if (kVar instanceof i) {
                this.f2379a = new i(this, (i) kVar);
            } else if (kVar instanceof h) {
                this.f2379a = new h(this, (h) kVar);
            } else if (kVar instanceof g) {
                this.f2379a = new g(this, (g) kVar);
            } else if (kVar instanceof f) {
                this.f2379a = new f(this, (f) kVar);
            } else {
                this.f2379a = new k(this);
            }
            kVar.e(this);
            return;
        }
        this.f2379a = new k(this);
    }

    /* compiled from: WindowInsetsCompat.java */
    /* renamed from: androidx.core.view.k0$i */
    /* loaded from: classes.dex */
    private static class i extends h {

        /* renamed from: i, reason: collision with root package name */
        private Insets f2394i;

        /* renamed from: j, reason: collision with root package name */
        private Insets f2395j;

        /* renamed from: k, reason: collision with root package name */
        private Insets f2396k;

        i(WindowInsetsCompat windowInsetsCompat, WindowInsets windowInsets) {
            super(windowInsetsCompat, windowInsets);
            this.f2394i = null;
            this.f2395j = null;
            this.f2396k = null;
        }

        @Override // androidx.core.view.WindowInsetsCompat.k
        Insets h() {
            if (this.f2395j == null) {
                this.f2395j = Insets.d(this.f2388c.getMandatorySystemGestureInsets());
            }
            return this.f2395j;
        }

        @Override // androidx.core.view.WindowInsetsCompat.k
        Insets j() {
            if (this.f2394i == null) {
                this.f2394i = Insets.d(this.f2388c.getSystemGestureInsets());
            }
            return this.f2394i;
        }

        @Override // androidx.core.view.WindowInsetsCompat.k
        Insets l() {
            if (this.f2396k == null) {
                this.f2396k = Insets.d(this.f2388c.getTappableElementInsets());
            }
            return this.f2396k;
        }

        @Override // androidx.core.view.WindowInsetsCompat.f, androidx.core.view.WindowInsetsCompat.k
        WindowInsetsCompat m(int i10, int i11, int i12, int i13) {
            return WindowInsetsCompat.v(this.f2388c.inset(i10, i11, i12, i13));
        }

        i(WindowInsetsCompat windowInsetsCompat, i iVar) {
            super(windowInsetsCompat, iVar);
            this.f2394i = null;
            this.f2395j = null;
            this.f2396k = null;
        }
    }
}
