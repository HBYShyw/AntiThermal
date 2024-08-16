package androidx.core.widget;

import android.content.res.Resources;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import androidx.core.view.ViewCompat;

/* compiled from: AutoScrollHelper.java */
/* renamed from: androidx.core.widget.a, reason: use source file name */
/* loaded from: classes.dex */
public abstract class AutoScrollHelper implements View.OnTouchListener {

    /* renamed from: v, reason: collision with root package name */
    private static final int f2461v = ViewConfiguration.getTapTimeout();

    /* renamed from: g, reason: collision with root package name */
    final View f2464g;

    /* renamed from: h, reason: collision with root package name */
    private Runnable f2465h;

    /* renamed from: k, reason: collision with root package name */
    private int f2468k;

    /* renamed from: l, reason: collision with root package name */
    private int f2469l;

    /* renamed from: p, reason: collision with root package name */
    private boolean f2473p;

    /* renamed from: q, reason: collision with root package name */
    boolean f2474q;

    /* renamed from: r, reason: collision with root package name */
    boolean f2475r;

    /* renamed from: s, reason: collision with root package name */
    boolean f2476s;

    /* renamed from: t, reason: collision with root package name */
    private boolean f2477t;

    /* renamed from: u, reason: collision with root package name */
    private boolean f2478u;

    /* renamed from: e, reason: collision with root package name */
    final a f2462e = new a();

    /* renamed from: f, reason: collision with root package name */
    private final Interpolator f2463f = new AccelerateInterpolator();

    /* renamed from: i, reason: collision with root package name */
    private float[] f2466i = {0.0f, 0.0f};

    /* renamed from: j, reason: collision with root package name */
    private float[] f2467j = {Float.MAX_VALUE, Float.MAX_VALUE};

    /* renamed from: m, reason: collision with root package name */
    private float[] f2470m = {0.0f, 0.0f};

    /* renamed from: n, reason: collision with root package name */
    private float[] f2471n = {0.0f, 0.0f};

    /* renamed from: o, reason: collision with root package name */
    private float[] f2472o = {Float.MAX_VALUE, Float.MAX_VALUE};

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AutoScrollHelper.java */
    /* renamed from: androidx.core.widget.a$a */
    /* loaded from: classes.dex */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        private int f2479a;

        /* renamed from: b, reason: collision with root package name */
        private int f2480b;

        /* renamed from: c, reason: collision with root package name */
        private float f2481c;

        /* renamed from: d, reason: collision with root package name */
        private float f2482d;

        /* renamed from: j, reason: collision with root package name */
        private float f2488j;

        /* renamed from: k, reason: collision with root package name */
        private int f2489k;

        /* renamed from: e, reason: collision with root package name */
        private long f2483e = Long.MIN_VALUE;

        /* renamed from: i, reason: collision with root package name */
        private long f2487i = -1;

        /* renamed from: f, reason: collision with root package name */
        private long f2484f = 0;

        /* renamed from: g, reason: collision with root package name */
        private int f2485g = 0;

        /* renamed from: h, reason: collision with root package name */
        private int f2486h = 0;

        a() {
        }

        private float e(long j10) {
            long j11 = this.f2483e;
            if (j10 < j11) {
                return 0.0f;
            }
            long j12 = this.f2487i;
            if (j12 >= 0 && j10 >= j12) {
                float f10 = this.f2488j;
                return (1.0f - f10) + (f10 * AutoScrollHelper.e(((float) (j10 - j12)) / this.f2489k, 0.0f, 1.0f));
            }
            return AutoScrollHelper.e(((float) (j10 - j11)) / this.f2479a, 0.0f, 1.0f) * 0.5f;
        }

        private float g(float f10) {
            return ((-4.0f) * f10 * f10) + (f10 * 4.0f);
        }

        public void a() {
            if (this.f2484f != 0) {
                long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
                float g6 = g(e(currentAnimationTimeMillis));
                long j10 = currentAnimationTimeMillis - this.f2484f;
                this.f2484f = currentAnimationTimeMillis;
                float f10 = ((float) j10) * g6;
                this.f2485g = (int) (this.f2481c * f10);
                this.f2486h = (int) (f10 * this.f2482d);
                return;
            }
            throw new RuntimeException("Cannot compute scroll delta before calling start()");
        }

        public int b() {
            return this.f2485g;
        }

        public int c() {
            return this.f2486h;
        }

        public int d() {
            float f10 = this.f2481c;
            return (int) (f10 / Math.abs(f10));
        }

        public int f() {
            float f10 = this.f2482d;
            return (int) (f10 / Math.abs(f10));
        }

        public boolean h() {
            return this.f2487i > 0 && AnimationUtils.currentAnimationTimeMillis() > this.f2487i + ((long) this.f2489k);
        }

        public void i() {
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            this.f2489k = AutoScrollHelper.f((int) (currentAnimationTimeMillis - this.f2483e), 0, this.f2480b);
            this.f2488j = e(currentAnimationTimeMillis);
            this.f2487i = currentAnimationTimeMillis;
        }

        public void j(int i10) {
            this.f2480b = i10;
        }

        public void k(int i10) {
            this.f2479a = i10;
        }

        public void l(float f10, float f11) {
            this.f2481c = f10;
            this.f2482d = f11;
        }

        public void m() {
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            this.f2483e = currentAnimationTimeMillis;
            this.f2487i = -1L;
            this.f2484f = currentAnimationTimeMillis;
            this.f2488j = 0.5f;
            this.f2485g = 0;
            this.f2486h = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AutoScrollHelper.java */
    /* renamed from: androidx.core.widget.a$b */
    /* loaded from: classes.dex */
    public class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            AutoScrollHelper autoScrollHelper = AutoScrollHelper.this;
            if (autoScrollHelper.f2476s) {
                if (autoScrollHelper.f2474q) {
                    autoScrollHelper.f2474q = false;
                    autoScrollHelper.f2462e.m();
                }
                a aVar = AutoScrollHelper.this.f2462e;
                if (!aVar.h() && AutoScrollHelper.this.u()) {
                    AutoScrollHelper autoScrollHelper2 = AutoScrollHelper.this;
                    if (autoScrollHelper2.f2475r) {
                        autoScrollHelper2.f2475r = false;
                        autoScrollHelper2.c();
                    }
                    aVar.a();
                    AutoScrollHelper.this.j(aVar.b(), aVar.c());
                    ViewCompat.c0(AutoScrollHelper.this.f2464g, this);
                    return;
                }
                AutoScrollHelper.this.f2476s = false;
            }
        }
    }

    public AutoScrollHelper(View view) {
        this.f2464g = view;
        float f10 = Resources.getSystem().getDisplayMetrics().density;
        float f11 = (int) ((1575.0f * f10) + 0.5f);
        o(f11, f11);
        float f12 = (int) ((f10 * 315.0f) + 0.5f);
        p(f12, f12);
        l(1);
        n(Float.MAX_VALUE, Float.MAX_VALUE);
        s(0.2f, 0.2f);
        t(1.0f, 1.0f);
        k(f2461v);
        r(500);
        q(500);
    }

    private float d(int i10, float f10, float f11, float f12) {
        float h10 = h(this.f2466i[i10], f11, this.f2467j[i10], f10);
        if (h10 == 0.0f) {
            return 0.0f;
        }
        float f13 = this.f2470m[i10];
        float f14 = this.f2471n[i10];
        float f15 = this.f2472o[i10];
        float f16 = f13 * f12;
        if (h10 > 0.0f) {
            return e(h10 * f16, f14, f15);
        }
        return -e((-h10) * f16, f14, f15);
    }

    static float e(float f10, float f11, float f12) {
        return f10 > f12 ? f12 : f10 < f11 ? f11 : f10;
    }

    static int f(int i10, int i11, int i12) {
        return i10 > i12 ? i12 : i10 < i11 ? i11 : i10;
    }

    private float g(float f10, float f11) {
        if (f11 == 0.0f) {
            return 0.0f;
        }
        int i10 = this.f2468k;
        if (i10 == 0 || i10 == 1) {
            if (f10 < f11) {
                if (f10 >= 0.0f) {
                    return 1.0f - (f10 / f11);
                }
                if (this.f2476s && i10 == 1) {
                    return 1.0f;
                }
            }
        } else if (i10 == 2 && f10 < 0.0f) {
            return f10 / (-f11);
        }
        return 0.0f;
    }

    private float h(float f10, float f11, float f12, float f13) {
        float interpolation;
        float e10 = e(f10 * f11, 0.0f, f12);
        float g6 = g(f11 - f13, e10) - g(f13, e10);
        if (g6 < 0.0f) {
            interpolation = -this.f2463f.getInterpolation(-g6);
        } else {
            if (g6 <= 0.0f) {
                return 0.0f;
            }
            interpolation = this.f2463f.getInterpolation(g6);
        }
        return e(interpolation, -1.0f, 1.0f);
    }

    private void i() {
        if (this.f2474q) {
            this.f2476s = false;
        } else {
            this.f2462e.i();
        }
    }

    private void v() {
        int i10;
        if (this.f2465h == null) {
            this.f2465h = new b();
        }
        this.f2476s = true;
        this.f2474q = true;
        if (!this.f2473p && (i10 = this.f2469l) > 0) {
            ViewCompat.d0(this.f2464g, this.f2465h, i10);
        } else {
            this.f2465h.run();
        }
        this.f2473p = true;
    }

    public abstract boolean a(int i10);

    public abstract boolean b(int i10);

    void c() {
        long uptimeMillis = SystemClock.uptimeMillis();
        MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
        this.f2464g.onTouchEvent(obtain);
        obtain.recycle();
    }

    public abstract void j(int i10, int i11);

    public AutoScrollHelper k(int i10) {
        this.f2469l = i10;
        return this;
    }

    public AutoScrollHelper l(int i10) {
        this.f2468k = i10;
        return this;
    }

    public AutoScrollHelper m(boolean z10) {
        if (this.f2477t && !z10) {
            i();
        }
        this.f2477t = z10;
        return this;
    }

    public AutoScrollHelper n(float f10, float f11) {
        float[] fArr = this.f2467j;
        fArr[0] = f10;
        fArr[1] = f11;
        return this;
    }

    public AutoScrollHelper o(float f10, float f11) {
        float[] fArr = this.f2472o;
        fArr[0] = f10 / 1000.0f;
        fArr[1] = f11 / 1000.0f;
        return this;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0013, code lost:
    
        if (r0 != 3) goto L20;
     */
    @Override // android.view.View.OnTouchListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (!this.f2477t) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked != 2) {
                }
            }
            i();
            return this.f2478u && this.f2476s;
        }
        this.f2475r = true;
        this.f2473p = false;
        this.f2462e.l(d(0, motionEvent.getX(), view.getWidth(), this.f2464g.getWidth()), d(1, motionEvent.getY(), view.getHeight(), this.f2464g.getHeight()));
        if (!this.f2476s && u()) {
            v();
        }
        if (this.f2478u) {
            return false;
        }
    }

    public AutoScrollHelper p(float f10, float f11) {
        float[] fArr = this.f2471n;
        fArr[0] = f10 / 1000.0f;
        fArr[1] = f11 / 1000.0f;
        return this;
    }

    public AutoScrollHelper q(int i10) {
        this.f2462e.j(i10);
        return this;
    }

    public AutoScrollHelper r(int i10) {
        this.f2462e.k(i10);
        return this;
    }

    public AutoScrollHelper s(float f10, float f11) {
        float[] fArr = this.f2466i;
        fArr[0] = f10;
        fArr[1] = f11;
        return this;
    }

    public AutoScrollHelper t(float f10, float f11) {
        float[] fArr = this.f2470m;
        fArr[0] = f10 / 1000.0f;
        fArr[1] = f11 / 1000.0f;
        return this;
    }

    boolean u() {
        a aVar = this.f2462e;
        int f10 = aVar.f();
        int d10 = aVar.d();
        return (f10 != 0 && b(f10)) || (d10 != 0 && a(d10));
    }
}
