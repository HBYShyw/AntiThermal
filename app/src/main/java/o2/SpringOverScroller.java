package o2;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.view.Choreographer;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.OverScroller;
import b2.COUILog;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;

/* compiled from: SpringOverScroller.java */
/* renamed from: o2.f, reason: use source file name */
/* loaded from: classes.dex */
public class SpringOverScroller extends OverScroller implements COUIIOverScroller {

    /* renamed from: o, reason: collision with root package name */
    public static boolean f16135o;

    /* renamed from: p, reason: collision with root package name */
    private static float f16136p;

    /* renamed from: q, reason: collision with root package name */
    private static float f16137q;

    /* renamed from: a, reason: collision with root package name */
    private c f16138a;

    /* renamed from: b, reason: collision with root package name */
    private c f16139b;

    /* renamed from: c, reason: collision with root package name */
    private Interpolator f16140c;

    /* renamed from: d, reason: collision with root package name */
    private int f16141d;

    /* renamed from: e, reason: collision with root package name */
    private Context f16142e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f16143f;

    /* renamed from: g, reason: collision with root package name */
    private int f16144g;

    /* renamed from: h, reason: collision with root package name */
    private long f16145h;

    /* renamed from: i, reason: collision with root package name */
    private float f16146i;

    /* renamed from: j, reason: collision with root package name */
    private boolean f16147j;

    /* renamed from: k, reason: collision with root package name */
    private long f16148k;

    /* renamed from: l, reason: collision with root package name */
    private long f16149l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f16150m;

    /* renamed from: n, reason: collision with root package name */
    private final Choreographer.FrameCallback f16151n;

    /* compiled from: SpringOverScroller.java */
    /* renamed from: o2.f$a */
    /* loaded from: classes.dex */
    class a implements Choreographer.FrameCallback {
        a() {
        }

        @Override // android.view.Choreographer.FrameCallback
        public void doFrame(long j10) {
            if (SpringOverScroller.this.f16138a != null) {
                SpringOverScroller.this.f16138a.z(j10);
            }
            if (SpringOverScroller.this.f16139b != null) {
                SpringOverScroller.this.f16139b.z(j10);
            }
            SpringOverScroller springOverScroller = SpringOverScroller.this;
            springOverScroller.f16148k = springOverScroller.f16149l;
            SpringOverScroller.this.f16149l = j10;
            SpringOverScroller.this.f16150m = true;
            if (SpringOverScroller.this.f16147j) {
                return;
            }
            Choreographer.getInstance().postFrameCallback(this);
        }
    }

    /* compiled from: SpringOverScroller.java */
    /* renamed from: o2.f$b */
    /* loaded from: classes.dex */
    static class b implements Interpolator {

        /* renamed from: a, reason: collision with root package name */
        private static final float f16153a;

        /* renamed from: b, reason: collision with root package name */
        private static final float f16154b;

        static {
            float a10 = 1.0f / a(1.0f);
            f16153a = a10;
            f16154b = 1.0f - (a10 * a(1.0f));
        }

        b() {
        }

        private static float a(float f10) {
            float f11 = f10 * 8.0f;
            if (f11 < 1.0f) {
                return f11 - (1.0f - ((float) Math.exp(-f11)));
            }
            return ((1.0f - ((float) Math.exp(1.0f - f11))) * 0.63212055f) + 0.36787945f;
        }

        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float f10) {
            float a10 = f16153a * a(f10);
            return a10 > 0.0f ? a10 + f16154b : a10;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SpringOverScroller.java */
    /* renamed from: o2.f$c */
    /* loaded from: classes.dex */
    public static class c {
        private static float C = 1.0f;
        private boolean A;
        private boolean B;

        /* renamed from: a, reason: collision with root package name */
        private b f16155a;

        /* renamed from: j, reason: collision with root package name */
        private double f16164j;

        /* renamed from: k, reason: collision with root package name */
        private double f16165k;

        /* renamed from: l, reason: collision with root package name */
        private int f16166l;

        /* renamed from: m, reason: collision with root package name */
        private int f16167m;

        /* renamed from: n, reason: collision with root package name */
        private int f16168n;

        /* renamed from: o, reason: collision with root package name */
        private long f16169o;

        /* renamed from: r, reason: collision with root package name */
        private boolean f16172r;

        /* renamed from: s, reason: collision with root package name */
        private boolean f16173s;

        /* renamed from: u, reason: collision with root package name */
        private long f16175u;

        /* renamed from: v, reason: collision with root package name */
        private long f16176v;

        /* renamed from: w, reason: collision with root package name */
        private long f16177w;

        /* renamed from: x, reason: collision with root package name */
        private long f16178x;

        /* renamed from: y, reason: collision with root package name */
        private long f16179y;

        /* renamed from: z, reason: collision with root package name */
        private long f16180z;

        /* renamed from: d, reason: collision with root package name */
        private a f16158d = new a();

        /* renamed from: e, reason: collision with root package name */
        private a f16159e = new a();

        /* renamed from: f, reason: collision with root package name */
        private a f16160f = new a();

        /* renamed from: g, reason: collision with root package name */
        private float f16161g = 0.32f;

        /* renamed from: h, reason: collision with root package name */
        private double f16162h = 20.0d;

        /* renamed from: i, reason: collision with root package name */
        private double f16163i = 0.05d;

        /* renamed from: p, reason: collision with root package name */
        private int f16170p = 1;

        /* renamed from: q, reason: collision with root package name */
        private boolean f16171q = false;

        /* renamed from: t, reason: collision with root package name */
        private float f16174t = 0.83f;

        /* renamed from: b, reason: collision with root package name */
        private b f16156b = new b(0.32f, UserProfileInfo.Constant.NA_LAT_LON);

        /* renamed from: c, reason: collision with root package name */
        private b f16157c = new b(12.1899995803833d, 16.0d);

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: SpringOverScroller.java */
        /* renamed from: o2.f$c$a */
        /* loaded from: classes.dex */
        public static class a {

            /* renamed from: a, reason: collision with root package name */
            double f16181a;

            /* renamed from: b, reason: collision with root package name */
            double f16182b;

            a() {
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: SpringOverScroller.java */
        /* renamed from: o2.f$c$b */
        /* loaded from: classes.dex */
        public static class b {

            /* renamed from: a, reason: collision with root package name */
            double f16183a;

            /* renamed from: b, reason: collision with root package name */
            double f16184b;

            b(double d10, double d11) {
                this.f16183a = a((float) d10);
                this.f16184b = d((float) d11);
            }

            private float a(float f10) {
                if (f10 == 0.0f) {
                    return 0.0f;
                }
                return 25.0f + ((f10 - 8.0f) * 3.0f);
            }

            private double d(float f10) {
                return f10 == 0.0f ? UserProfileInfo.Constant.NA_LAT_LON : ((f10 - 30.0f) * 3.62f) + 194.0f;
            }

            void b(double d10) {
                this.f16183a = a((float) d10);
            }

            void c(double d10) {
                this.f16184b = d((float) d10);
            }
        }

        c() {
            s(this.f16156b);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void z(long j10) {
            this.f16179y = this.f16180z;
            this.f16180z = j10;
            this.A = true;
        }

        void A(float f10) {
            a aVar = this.f16158d;
            int i10 = this.f16166l;
            aVar.f16181a = i10 + Math.round(f10 * (this.f16168n - i10));
        }

        void k(int i10, int i11) {
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            this.f16175u = currentAnimationTimeMillis;
            this.f16176v = currentAnimationTimeMillis;
            this.f16170p = 1;
            C = 1.0f;
            this.f16156b.b(this.f16161g);
            this.f16156b.c(UserProfileInfo.Constant.NA_LAT_LON);
            s(this.f16156b);
            t(i10, true);
            v(i11);
            long elapsedRealtime = SystemClock.elapsedRealtime();
            this.f16177w = elapsedRealtime;
            this.f16178x = elapsedRealtime;
        }

        double l() {
            return this.f16158d.f16181a;
        }

        double m(a aVar) {
            return Math.abs(this.f16165k - aVar.f16181a);
        }

        double n() {
            return this.f16165k;
        }

        double o() {
            return this.f16158d.f16182b;
        }

        boolean p() {
            return Math.abs(this.f16158d.f16182b) <= this.f16162h && (m(this.f16158d) <= this.f16163i || this.f16155a.f16184b == UserProfileInfo.Constant.NA_LAT_LON);
        }

        void q(int i10, int i11, int i12) {
            a aVar = this.f16158d;
            aVar.f16181a = i10;
            a aVar2 = this.f16159e;
            aVar2.f16181a = UserProfileInfo.Constant.NA_LAT_LON;
            aVar2.f16182b = UserProfileInfo.Constant.NA_LAT_LON;
            a aVar3 = this.f16160f;
            aVar3.f16181a = i11;
            aVar3.f16182b = aVar.f16182b;
        }

        void r() {
            a aVar = this.f16158d;
            double d10 = aVar.f16181a;
            this.f16165k = d10;
            this.f16160f.f16181a = d10;
            aVar.f16182b = UserProfileInfo.Constant.NA_LAT_LON;
            this.f16172r = false;
            this.B = true;
        }

        void s(b bVar) {
            if (bVar != null) {
                this.f16155a = bVar;
                return;
            }
            throw new IllegalArgumentException("springConfig is required");
        }

        void t(double d10, boolean z10) {
            this.f16164j = d10;
            if (!this.f16171q) {
                this.f16159e.f16181a = UserProfileInfo.Constant.NA_LAT_LON;
                this.f16160f.f16181a = UserProfileInfo.Constant.NA_LAT_LON;
            }
            this.f16158d.f16181a = d10;
            if (z10) {
                r();
            }
        }

        void u(double d10) {
            if (this.f16165k == d10) {
                return;
            }
            this.f16164j = l();
            this.f16165k = d10;
        }

        void v(double d10) {
            if (Math.abs(d10 - this.f16158d.f16182b) < 1.0000000116860974E-7d) {
                return;
            }
            this.f16158d.f16182b = d10;
        }

        boolean w(int i10, int i11, int i12) {
            t(i10, false);
            long elapsedRealtime = SystemClock.elapsedRealtime();
            this.f16177w = elapsedRealtime;
            this.f16178x = elapsedRealtime;
            if (i10 <= i12 && i10 >= i11) {
                s(new b(this.f16161g, UserProfileInfo.Constant.NA_LAT_LON));
                return false;
            }
            if (i10 > i12) {
                u(i12);
            } else if (i10 < i11) {
                u(i11);
            }
            this.f16172r = true;
            this.f16157c.b(SpringOverScroller.f16136p);
            this.f16157c.c(this.f16174t * 16.0f);
            s(this.f16157c);
            return true;
        }

        void x(int i10, int i11, int i12, long j10) {
            this.f16166l = i10;
            int i13 = i10 + i11;
            this.f16168n = i13;
            this.f16165k = i13;
            this.f16167m = i12;
            this.f16169o = j10;
            s(this.f16156b);
            long elapsedRealtime = SystemClock.elapsedRealtime();
            this.f16177w = elapsedRealtime;
            this.f16178x = elapsedRealtime;
        }

        boolean y() {
            String str;
            double d10;
            double d11;
            if (p()) {
                return false;
            }
            this.f16178x = SystemClock.elapsedRealtime();
            if (this.A) {
                this.A = false;
                if (SpringOverScroller.f16135o) {
                    Log.d("SpringOverScroller", "update if: " + (((float) (this.f16180z - this.f16179y)) / 1.0E9f));
                }
                float unused = SpringOverScroller.f16137q = Math.max(0.008f, ((float) (this.f16180z - this.f16179y)) / 1.0E9f);
            } else {
                if (SpringOverScroller.f16135o) {
                    Log.d("SpringOverScroller", "update else: " + (((float) (this.f16178x - this.f16177w)) / 1000.0f));
                }
                float unused2 = SpringOverScroller.f16137q = Math.max(0.008f, ((float) (this.f16178x - this.f16177w)) / 1000.0f);
            }
            if (SpringOverScroller.f16137q > 0.025f) {
                if (SpringOverScroller.f16135o) {
                    Log.d("SpringOverScroller", "update: error mRefreshTime = " + SpringOverScroller.f16137q);
                }
                float unused3 = SpringOverScroller.f16137q = 0.008f;
            }
            if (SpringOverScroller.f16135o) {
                Log.d("SpringOverScroller", "update: mRefreshTime = " + SpringOverScroller.f16137q + " mLastComputeTime = " + this.f16177w);
            }
            this.f16177w = this.f16178x;
            a aVar = this.f16158d;
            double d12 = aVar.f16181a;
            double d13 = aVar.f16182b;
            a aVar2 = this.f16160f;
            double d14 = aVar2.f16181a;
            double d15 = aVar2.f16182b;
            if (!this.f16172r) {
                long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
                long j10 = currentAnimationTimeMillis - this.f16175u;
                if (this.f16170p == 1) {
                    str = "SpringOverScroller";
                    if (Math.abs(this.f16158d.f16182b) <= 4000.0d || Math.abs(this.f16158d.f16182b) >= 10000.0d) {
                        d10 = d12;
                        if (Math.abs(this.f16158d.f16182b) <= 4000.0d) {
                            this.f16155a.f16183a = (Math.abs(this.f16158d.f16182b) / 10000.0d) + 4.5d;
                        }
                    } else {
                        d10 = d12;
                        this.f16155a.f16183a = (Math.abs(this.f16158d.f16182b) / 10000.0d) + 2.6d;
                    }
                    this.f16176v = currentAnimationTimeMillis;
                } else {
                    str = "SpringOverScroller";
                    d10 = d12;
                }
                if (this.f16170p > 1) {
                    if (j10 <= 480) {
                        d11 = d13;
                    } else if (Math.abs(this.f16158d.f16182b) > 2000.0d) {
                        d11 = d13;
                        this.f16155a.f16183a += SpringOverScroller.f16137q * 0.00125d;
                    } else {
                        d11 = d13;
                        b bVar = this.f16155a;
                        double d16 = bVar.f16183a;
                        if (d16 > 2.0d) {
                            bVar.f16183a = d16 - (SpringOverScroller.f16137q * 0.00125d);
                        }
                    }
                    this.f16176v = currentAnimationTimeMillis;
                } else {
                    d11 = d13;
                }
                if (p()) {
                    this.B = true;
                }
            } else {
                str = "SpringOverScroller";
                d10 = d12;
                d11 = d13;
                double m10 = m(aVar);
                if (!this.f16173s && m10 < 180.0d) {
                    this.f16173s = true;
                } else if (m10 < 0.25d) {
                    this.f16158d.f16181a = this.f16165k;
                    this.f16173s = false;
                    this.f16172r = false;
                    this.B = true;
                    return false;
                }
            }
            b bVar2 = this.f16155a;
            double d17 = (bVar2.f16184b * (this.f16165k - d14)) - (bVar2.f16183a * d15);
            double d18 = d11 + ((SpringOverScroller.f16137q * d17) / 2.0d);
            b bVar3 = this.f16155a;
            double d19 = (bVar3.f16184b * (this.f16165k - (d10 + ((d11 * SpringOverScroller.f16137q) / 2.0d)))) - (bVar3.f16183a * d18);
            double d20 = d11 + ((SpringOverScroller.f16137q * d19) / 2.0d);
            b bVar4 = this.f16155a;
            double d21 = (bVar4.f16184b * (this.f16165k - (d10 + ((SpringOverScroller.f16137q * d18) / 2.0d)))) - (bVar4.f16183a * d20);
            double d22 = d10 + (SpringOverScroller.f16137q * d20);
            double d23 = d11 + (SpringOverScroller.f16137q * d21);
            b bVar5 = this.f16155a;
            double d24 = (bVar5.f16184b * (this.f16165k - d22)) - (bVar5.f16183a * d23);
            double d25 = d10 + ((d11 + ((d18 + d20) * 2.0d) + d23) * 0.16699999570846558d * SpringOverScroller.f16137q);
            double d26 = d11 + ((d17 + ((d19 + d21) * 2.0d) + d24) * 0.16699999570846558d * SpringOverScroller.f16137q);
            a aVar3 = this.f16160f;
            aVar3.f16182b = d23;
            aVar3.f16181a = d22;
            a aVar4 = this.f16158d;
            aVar4.f16182b = d26;
            aVar4.f16181a = d25;
            if (SpringOverScroller.f16135o) {
                Log.d(str, "update: tension = " + this.f16155a.f16184b + " friction = " + this.f16155a.f16183a + "\nupdate: velocity = " + d26 + " position = " + d25);
            }
            this.f16170p++;
            return true;
        }
    }

    static {
        f16135o = COUILog.f4543b || COUILog.d("SpringOverScroller", 3);
        f16136p = 12.19f;
    }

    public SpringOverScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
        this.f16141d = 2;
        this.f16143f = true;
        this.f16146i = 1.0f;
        this.f16150m = false;
        this.f16151n = new a();
        this.f16138a = new c();
        this.f16139b = new c();
        if (interpolator == null) {
            this.f16140c = new b();
        } else {
            this.f16140c = interpolator;
        }
        D(0.016f);
        this.f16142e = context;
    }

    private void D(float f10) {
        f16137q = f10;
    }

    private int w(int i10) {
        if (!this.f16143f) {
            return i10;
        }
        long currentTimeMillis = System.currentTimeMillis();
        int i11 = this.f16144g;
        if (i11 <= 0) {
            if (i11 != 0) {
                return i10;
            }
            this.f16144g = i11 + 1;
            this.f16145h = currentTimeMillis;
            return i10;
        }
        if (currentTimeMillis - this.f16145h <= 500 && i10 >= 8000) {
            this.f16145h = currentTimeMillis;
            int i12 = i11 + 1;
            this.f16144g = i12;
            if (i12 <= 4) {
                return i10;
            }
            float f10 = this.f16146i * 1.4f;
            this.f16146i = f10;
            return Math.max(-70000, Math.min((int) (i10 * f10), 70000));
        }
        z();
        return i10;
    }

    private void z() {
        this.f16145h = 0L;
        this.f16144g = 0;
        this.f16146i = 1.0f;
    }

    public void A(boolean z10) {
        f16135o = z10;
    }

    public void B(boolean z10) {
        if (this.f16143f == z10) {
            return;
        }
        this.f16143f = z10;
        z();
    }

    public void C(boolean z10) {
        this.f16138a.f16171q = z10;
        this.f16139b.f16171q = z10;
    }

    public void E(float f10) {
        f16136p = f10;
    }

    public void F(float f10) {
        this.f16138a.f16174t = f10;
        this.f16139b.f16174t = f10;
    }

    public void G() {
        y();
        x();
        this.f16147j = false;
        this.f16138a.B = false;
        this.f16139b.B = false;
    }

    @Override // o2.COUIIOverScroller
    public float a() {
        return (float) this.f16138a.o();
    }

    @Override // android.widget.OverScroller, o2.COUIIOverScroller
    public void abortAnimation() {
        if (f16135o) {
            Log.d("SpringOverScroller", "abortAnimation", new Throwable());
        }
        this.f16141d = 2;
        this.f16138a.r();
        this.f16139b.r();
        this.f16147j = true;
    }

    @Override // o2.COUIIOverScroller
    public final int b() {
        return (int) Math.round(this.f16138a.l());
    }

    @Override // o2.COUIIOverScroller
    public final int c() {
        return (int) this.f16139b.n();
    }

    @Override // android.widget.OverScroller, o2.COUIIOverScroller
    public boolean computeScrollOffset() {
        if (j()) {
            this.f16147j = this.f16138a.B && this.f16139b.B;
            return false;
        }
        int i10 = this.f16141d;
        if (i10 != 0) {
            if (i10 == 1 && !this.f16138a.y() && !this.f16139b.y()) {
                abortAnimation();
            }
        } else {
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis() - this.f16138a.f16169o;
            int i11 = this.f16138a.f16167m;
            if (currentAnimationTimeMillis < i11) {
                float interpolation = this.f16140c.getInterpolation(((float) currentAnimationTimeMillis) / i11);
                this.f16138a.A(interpolation);
                this.f16139b.A(interpolation);
            } else {
                this.f16138a.A(1.0f);
                this.f16139b.A(1.0f);
                abortAnimation();
            }
        }
        return true;
    }

    @Override // o2.COUIIOverScroller
    public void d(float f10) {
        this.f16138a.f16158d.f16182b = f10;
    }

    @Override // o2.COUIIOverScroller
    public float e() {
        return (float) this.f16139b.o();
    }

    @Override // o2.COUIIOverScroller
    public final int f() {
        return (int) this.f16138a.n();
    }

    @Override // android.widget.OverScroller, o2.COUIIOverScroller
    public void fling(int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, int i18, int i19) {
        fling(i10, i11, i12, i13, i14, i15, i16, i17);
    }

    @Override // o2.COUIIOverScroller
    public final int g() {
        return (int) Math.round(this.f16139b.l());
    }

    @Override // android.widget.OverScroller
    public float getCurrVelocity() {
        double o10 = this.f16138a.o();
        double o11 = this.f16139b.o();
        return (int) Math.sqrt((o10 * o10) + (o11 * o11));
    }

    @Override // o2.COUIIOverScroller
    public void h(float f10) {
        this.f16139b.f16158d.f16182b = f10;
    }

    @Override // o2.COUIIOverScroller
    public void i(Interpolator interpolator) {
        if (interpolator == null) {
            this.f16140c = new b();
        } else {
            this.f16140c = interpolator;
        }
    }

    public boolean isScrollingInDirection(float f10, float f11) {
        return !isFinished() && Math.signum(f10) == Math.signum((float) ((int) (this.f16138a.f16165k - this.f16138a.f16164j))) && Math.signum(f11) == Math.signum((float) ((int) (this.f16139b.f16165k - this.f16139b.f16164j)));
    }

    @Override // o2.COUIIOverScroller
    public final boolean j() {
        boolean p10 = this.f16138a.p();
        boolean p11 = this.f16139b.p();
        if (f16135o) {
            Log.d("SpringOverScroller", "scrollX is rest: " + this.f16138a.p() + "  scrollY is rest: " + this.f16139b.p() + "  mMode = " + this.f16141d);
        }
        return p10 && p11 && this.f16141d != 0;
    }

    @Override // android.widget.OverScroller, o2.COUIIOverScroller
    public void notifyHorizontalEdgeReached(int i10, int i11, int i12) {
        this.f16138a.q(i10, i11, i12);
        springBack(i10, 0, 0, i11, 0, 0);
    }

    @Override // android.widget.OverScroller, o2.COUIIOverScroller
    public void notifyVerticalEdgeReached(int i10, int i11, int i12) {
        this.f16139b.q(i10, i11, i12);
        springBack(0, i10, 0, 0, 0, i11);
    }

    @Override // o2.COUIIOverScroller
    public void setFinalX(int i10) {
    }

    public void setFinalY(int i10) {
    }

    @Override // android.widget.OverScroller, o2.COUIIOverScroller
    public boolean springBack(int i10, int i11, int i12, int i13, int i14, int i15) {
        if (f16135o) {
            Log.d("SpringOverScroller", "springBack startX = " + i10 + " startY = " + i11 + " minX = " + i12 + " minY = " + i14 + " maxY = " + i15, new Throwable());
        }
        boolean w10 = this.f16138a.w(i10, i12, i13);
        boolean w11 = this.f16139b.w(i11, i14, i15);
        if (w10 || w11) {
            this.f16141d = 1;
        }
        return w10 || w11;
    }

    @Override // android.widget.OverScroller, o2.COUIIOverScroller
    public void startScroll(int i10, int i11, int i12, int i13) {
        startScroll(i10, i11, i12, i13, 250);
    }

    public void u() {
        this.f16147j = true;
    }

    public void v(int i10, int i11, int i12, int i13) {
        if (f16135o) {
            Log.d("SpringOverScroller", "fling startX = " + i10 + " startY = " + i11 + " velocityX = " + i12 + " velocityY = " + i13, new Throwable());
        }
        this.f16141d = 1;
        this.f16138a.k(i10, w(i12));
        this.f16139b.k(i11, w(i13));
    }

    void x() {
        if (f16135o) {
            Log.d("SpringOverScroller", "postChoreographerCallback: post Callback");
        }
        Choreographer.getInstance().postFrameCallback(this.f16151n);
    }

    void y() {
        if (f16135o) {
            Log.d("SpringOverScroller", "removeChoreographerCallback: remove Callback");
        }
        Choreographer.getInstance().removeFrameCallback(this.f16151n);
    }

    @Override // android.widget.OverScroller, o2.COUIIOverScroller
    public void fling(int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17) {
        v(i10, i11, i12, i13);
    }

    @Override // android.widget.OverScroller, o2.COUIIOverScroller
    public void startScroll(int i10, int i11, int i12, int i13, int i14) {
        if (f16135o) {
            Log.d("SpringOverScroller", "startScroll startX = " + i10 + " startY = " + i11 + " dx = " + i12 + " dy = " + i13 + " duration = " + i14, new Throwable());
        }
        this.f16141d = 0;
        long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
        this.f16138a.x(i10, i12, i14, currentAnimationTimeMillis);
        this.f16139b.x(i11, i13, i14, currentAnimationTimeMillis);
    }

    public SpringOverScroller(Context context) {
        this(context, null);
    }
}
