package o2;

import android.content.Context;
import android.util.Log;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.OverScroller;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;

/* compiled from: COUILocateOverScroller.java */
/* renamed from: o2.c, reason: use source file name */
/* loaded from: classes.dex */
public class COUILocateOverScroller extends OverScroller implements COUIIOverScroller {

    /* renamed from: e, reason: collision with root package name */
    private static final Interpolator f16111e = new a();

    /* renamed from: a, reason: collision with root package name */
    private b f16112a;

    /* renamed from: b, reason: collision with root package name */
    private b f16113b;

    /* renamed from: c, reason: collision with root package name */
    private Interpolator f16114c;

    /* renamed from: d, reason: collision with root package name */
    private int f16115d;

    /* compiled from: COUILocateOverScroller.java */
    /* renamed from: o2.c$a */
    /* loaded from: classes.dex */
    class a implements Interpolator {
        a() {
        }

        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float f10) {
            float f11 = f10 - 1.0f;
            return (f11 * f11 * f11 * f11 * f11) + 1.0f;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: COUILocateOverScroller.java */
    /* renamed from: o2.c$b */
    /* loaded from: classes.dex */
    public static class b {

        /* renamed from: p, reason: collision with root package name */
        private static final float f16116p = (float) (Math.log(0.78d) / Math.log(0.9d));

        /* renamed from: q, reason: collision with root package name */
        private static final float[] f16117q = new float[101];

        /* renamed from: r, reason: collision with root package name */
        private static final float[] f16118r = new float[101];

        /* renamed from: a, reason: collision with root package name */
        private int f16119a;

        /* renamed from: b, reason: collision with root package name */
        private int f16120b;

        /* renamed from: c, reason: collision with root package name */
        private int f16121c;

        /* renamed from: d, reason: collision with root package name */
        private int f16122d;

        /* renamed from: e, reason: collision with root package name */
        private float f16123e;

        /* renamed from: f, reason: collision with root package name */
        private float f16124f;

        /* renamed from: g, reason: collision with root package name */
        private long f16125g;

        /* renamed from: h, reason: collision with root package name */
        private int f16126h;

        /* renamed from: i, reason: collision with root package name */
        private int f16127i;

        /* renamed from: j, reason: collision with root package name */
        private int f16128j;

        /* renamed from: l, reason: collision with root package name */
        private int f16130l;

        /* renamed from: o, reason: collision with root package name */
        private float f16133o;

        /* renamed from: m, reason: collision with root package name */
        private float f16131m = ViewConfiguration.getScrollFriction() * 2.5f;

        /* renamed from: n, reason: collision with root package name */
        private int f16132n = 0;

        /* renamed from: k, reason: collision with root package name */
        private boolean f16129k = true;

        static {
            float f10;
            float f11;
            float f12;
            float f13;
            float f14;
            float f15;
            float f16;
            float f17;
            float f18;
            float f19;
            float f20 = 0.0f;
            float f21 = 0.0f;
            for (int i10 = 0; i10 < 100; i10++) {
                float f22 = i10 / 100.0f;
                float f23 = 1.0f;
                while (true) {
                    f10 = 2.0f;
                    f11 = ((f23 - f20) / 2.0f) + f20;
                    f12 = 3.0f;
                    f13 = 1.0f - f11;
                    f14 = f11 * 3.0f * f13;
                    f15 = f11 * f11 * f11;
                    float f24 = (((f13 * 0.175f) + (f11 * 0.35000002f)) * f14) + f15;
                    if (Math.abs(f24 - f22) < 1.0E-5d) {
                        break;
                    } else if (f24 > f22) {
                        f23 = f11;
                    } else {
                        f20 = f11;
                    }
                }
                f16117q[i10] = (f14 * ((f13 * 0.5f) + f11)) + f15;
                float f25 = 1.0f;
                while (true) {
                    f16 = ((f25 - f21) / f10) + f21;
                    f17 = 1.0f - f16;
                    f18 = f16 * f12 * f17;
                    f19 = f16 * f16 * f16;
                    float f26 = (((f17 * 0.5f) + f16) * f18) + f19;
                    if (Math.abs(f26 - f22) < 1.0E-5d) {
                        break;
                    }
                    if (f26 > f22) {
                        f25 = f16;
                    } else {
                        f21 = f16;
                    }
                    f10 = 2.0f;
                    f12 = 3.0f;
                }
                f16118r[i10] = (f18 * ((f17 * 0.175f) + (f16 * 0.35000002f))) + f19;
            }
            f16117q[100] = 1.0f;
            f16118r[100] = 1.0f;
        }

        b(Context context) {
            this.f16133o = context.getResources().getDisplayMetrics().density * 160.0f * 386.0878f * 0.84f;
        }

        private void i(int i10, int i11, int i12) {
            float abs = Math.abs((i12 - i10) / (i11 - i10));
            int i13 = (int) (abs * 100.0f);
            if (i13 >= 100 || i13 < 0) {
                return;
            }
            float f10 = i13 / 100.0f;
            int i14 = i13 + 1;
            float[] fArr = f16118r;
            float f11 = fArr[i13];
            this.f16126h = (int) (this.f16126h * (f11 + (((abs - f10) / ((i14 / 100.0f) - f10)) * (fArr[i14] - f11))));
        }

        private void l(int i10, int i11, int i12) {
            float f10 = (-i12) / this.f16124f;
            float f11 = i12;
            float sqrt = (float) Math.sqrt((((((f11 * f11) / 2.0f) / Math.abs(r1)) + Math.abs(i11 - i10)) * 2.0d) / Math.abs(this.f16124f));
            this.f16125g -= (int) ((sqrt - f10) * 1000.0f);
            this.f16120b = i11;
            this.f16119a = i11;
            this.f16122d = (int) ((-this.f16124f) * sqrt);
        }

        private static float n(int i10) {
            return i10 > 0 ? -2000.0f : 2000.0f;
        }

        private double o(int i10) {
            return Math.log((Math.abs(i10) * 0.35f) / (this.f16131m * this.f16133o));
        }

        private double p(int i10) {
            double o10 = o(i10);
            float f10 = f16116p;
            return this.f16131m * this.f16133o * Math.exp((f10 / (f10 - 1.0d)) * o10);
        }

        private int q(int i10) {
            return (int) (Math.exp(o(i10) / (f16116p - 1.0f)) * 1000.0d);
        }

        private void s() {
            int i10 = this.f16122d;
            float f10 = i10 * i10;
            float abs = f10 / (Math.abs(this.f16124f) * 2.0f);
            float signum = Math.signum(this.f16122d);
            int i11 = this.f16130l;
            if (abs > i11) {
                this.f16124f = ((-signum) * f10) / (i11 * 2.0f);
                abs = i11;
            }
            this.f16130l = (int) abs;
            this.f16132n = 2;
            int i12 = this.f16119a;
            int i13 = this.f16122d;
            if (i13 <= 0) {
                abs = -abs;
            }
            this.f16121c = i12 + ((int) abs);
            this.f16126h = -((int) ((i13 * 1000.0f) / this.f16124f));
        }

        private void w(int i10, int i11, int i12, int i13) {
            if (i10 > i11 && i10 < i12) {
                Log.e("COUILocateOverScroller", "startAfterEdge called from a valid position");
                this.f16129k = true;
                return;
            }
            boolean z10 = i10 > i12;
            int i14 = z10 ? i12 : i11;
            if ((i10 - i14) * i13 >= 0) {
                x(i10, i14, i13);
            } else if (p(i13) > Math.abs(r4)) {
                m(i10, i13, z10 ? i11 : i10, z10 ? i10 : i12, this.f16130l);
            } else {
                z(i10, i14, i13);
            }
        }

        private void x(int i10, int i11, int i12) {
            this.f16124f = n(i12 == 0 ? i10 - i11 : i12);
            l(i10, i11, i12);
            s();
        }

        private void z(int i10, int i11, int i12) {
            this.f16129k = false;
            this.f16132n = 1;
            this.f16120b = i10;
            this.f16119a = i10;
            this.f16121c = i11;
            int i13 = i10 - i11;
            this.f16124f = n(i13);
            this.f16122d = -i13;
            this.f16130l = Math.abs(i13);
            this.f16126h = (int) (Math.sqrt((i13 * (-2.0f)) / this.f16124f) * 1000.0d);
        }

        boolean A() {
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis() - this.f16125g;
            if (currentAnimationTimeMillis == 0) {
                return this.f16126h > 0;
            }
            int i10 = this.f16126h;
            if (currentAnimationTimeMillis > i10) {
                return false;
            }
            double d10 = UserProfileInfo.Constant.NA_LAT_LON;
            int i11 = this.f16132n;
            if (i11 == 0) {
                int i12 = this.f16127i;
                float f10 = ((float) currentAnimationTimeMillis) / i12;
                int i13 = (int) (f10 * 100.0f);
                float f11 = 1.0f;
                float f12 = 0.0f;
                if (i13 < 100 && i13 >= 0) {
                    float f13 = i13 / 100.0f;
                    int i14 = i13 + 1;
                    float[] fArr = f16117q;
                    float f14 = fArr[i13];
                    f12 = (fArr[i14] - f14) / ((i14 / 100.0f) - f13);
                    f11 = f14 + ((f10 - f13) * f12);
                }
                int i15 = this.f16128j;
                this.f16123e = ((f12 * i15) / i12) * 1000.0f;
                d10 = f11 * i15;
            } else if (i11 == 1) {
                float f15 = ((float) currentAnimationTimeMillis) / i10;
                float f16 = f15 * f15;
                float signum = Math.signum(this.f16122d);
                int i16 = this.f16130l;
                d10 = i16 * signum * ((3.0f * f16) - ((2.0f * f15) * f16));
                this.f16123e = signum * i16 * 6.0f * ((-f15) + f16);
            } else if (i11 == 2) {
                float f17 = ((float) currentAnimationTimeMillis) / 1000.0f;
                int i17 = this.f16122d;
                float f18 = this.f16124f;
                this.f16123e = i17 + (f18 * f17);
                d10 = (i17 * f17) + (((f18 * f17) * f17) / 2.0f);
            }
            this.f16120b = this.f16119a + ((int) Math.round(d10));
            return true;
        }

        void B(float f10) {
            this.f16120b = this.f16119a + Math.round(f10 * (this.f16121c - r0));
        }

        boolean j() {
            int i10 = this.f16132n;
            if (i10 != 0) {
                if (i10 == 1) {
                    return false;
                }
                if (i10 == 2) {
                    this.f16125g += this.f16126h;
                    z(this.f16121c, this.f16119a, 0);
                }
            } else {
                if (this.f16126h >= this.f16127i) {
                    return false;
                }
                int i11 = this.f16121c;
                this.f16120b = i11;
                this.f16119a = i11;
                int i12 = (int) this.f16123e;
                this.f16122d = i12;
                this.f16124f = n(i12);
                this.f16125g += this.f16126h;
                s();
            }
            A();
            return true;
        }

        void k() {
            this.f16120b = this.f16121c;
            this.f16129k = true;
        }

        void m(int i10, int i11, int i12, int i13, int i14) {
            this.f16130l = i14;
            this.f16129k = false;
            this.f16123e = i11;
            this.f16122d = i11;
            this.f16126h = 0;
            this.f16127i = 0;
            this.f16125g = AnimationUtils.currentAnimationTimeMillis();
            this.f16120b = i10;
            this.f16119a = i10;
            if (i10 <= i13 && i10 >= i12) {
                this.f16132n = 0;
                double d10 = UserProfileInfo.Constant.NA_LAT_LON;
                if (i11 != 0) {
                    int q10 = q(i11);
                    this.f16126h = q10;
                    this.f16127i = q10;
                    d10 = p(i11);
                }
                int signum = (int) (d10 * Math.signum(r0));
                this.f16128j = signum;
                int i15 = i10 + signum;
                this.f16121c = i15;
                if (i15 < i12) {
                    i(this.f16119a, i15, i12);
                    this.f16121c = i12;
                }
                int i16 = this.f16121c;
                if (i16 > i13) {
                    i(this.f16119a, i16, i13);
                    this.f16121c = i13;
                    return;
                }
                return;
            }
            w(i10, i12, i13, i11);
        }

        void r(int i10, int i11, int i12) {
            if (this.f16132n == 0) {
                this.f16130l = i12;
                this.f16125g = AnimationUtils.currentAnimationTimeMillis();
                w(i10, i11, i11, (int) this.f16123e);
            }
        }

        void t(int i10) {
            this.f16121c = i10;
            this.f16128j = i10 - this.f16119a;
            this.f16129k = false;
        }

        void u(float f10) {
            this.f16131m = f10;
        }

        boolean v(int i10, int i11, int i12) {
            this.f16129k = true;
            this.f16120b = i10;
            this.f16119a = i10;
            this.f16121c = i10;
            this.f16122d = 0;
            this.f16125g = AnimationUtils.currentAnimationTimeMillis();
            this.f16126h = 0;
            if (i10 < i11) {
                z(i10, i11, 0);
            } else if (i10 > i12) {
                z(i10, i12, 0);
            }
            return !this.f16129k;
        }

        void y(int i10, int i11, int i12) {
            this.f16129k = false;
            this.f16120b = i10;
            this.f16119a = i10;
            this.f16121c = i10 + i11;
            this.f16125g = AnimationUtils.currentAnimationTimeMillis();
            this.f16126h = i12;
            this.f16124f = 0.0f;
            this.f16122d = 0;
        }
    }

    public COUILocateOverScroller(Context context) {
        this(context, null);
    }

    @Override // o2.COUIIOverScroller
    public float a() {
        return this.f16112a.f16123e;
    }

    @Override // android.widget.OverScroller, o2.COUIIOverScroller
    public void abortAnimation() {
        this.f16112a.k();
        this.f16113b.k();
    }

    @Override // o2.COUIIOverScroller
    public int b() {
        return this.f16112a.f16120b;
    }

    @Override // o2.COUIIOverScroller
    public int c() {
        return this.f16113b.f16121c;
    }

    @Override // android.widget.OverScroller, o2.COUIIOverScroller
    public boolean computeScrollOffset() {
        if (j()) {
            return false;
        }
        int i10 = this.f16115d;
        if (i10 == 0) {
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis() - this.f16112a.f16125g;
            int i11 = this.f16112a.f16126h;
            if (currentAnimationTimeMillis < i11) {
                float interpolation = this.f16114c.getInterpolation(((float) currentAnimationTimeMillis) / i11);
                this.f16112a.B(interpolation);
                this.f16113b.B(interpolation);
            } else {
                abortAnimation();
            }
        } else if (i10 == 1) {
            if (!this.f16112a.f16129k && !this.f16112a.A() && !this.f16112a.j()) {
                this.f16112a.k();
            }
            if (!this.f16113b.f16129k && !this.f16113b.A() && !this.f16113b.j()) {
                this.f16113b.k();
            }
        }
        return true;
    }

    @Override // o2.COUIIOverScroller
    public void d(float f10) {
        this.f16112a.f16123e = f10;
    }

    @Override // o2.COUIIOverScroller
    public float e() {
        return this.f16113b.f16123e;
    }

    @Override // o2.COUIIOverScroller
    public int f() {
        return this.f16112a.f16121c;
    }

    @Override // android.widget.OverScroller, o2.COUIIOverScroller
    public void fling(int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17) {
        fling(i10, i11, i12, i13, i14, i15, i16, i17, 0, 0);
    }

    @Override // o2.COUIIOverScroller
    public int g() {
        return this.f16113b.f16120b;
    }

    @Override // android.widget.OverScroller
    public float getCurrVelocity() {
        return (float) Math.hypot(this.f16112a.f16123e, this.f16113b.f16123e);
    }

    @Override // o2.COUIIOverScroller
    public void h(float f10) {
        this.f16113b.f16123e = f10;
    }

    @Override // o2.COUIIOverScroller
    public void i(Interpolator interpolator) {
        if (interpolator == null) {
            this.f16114c = f16111e;
        } else {
            this.f16114c = interpolator;
        }
    }

    public boolean isScrollingInDirection(float f10, float f11) {
        return !isFinished() && Math.signum(f10) == Math.signum((float) (this.f16112a.f16121c - this.f16112a.f16119a)) && Math.signum(f11) == Math.signum((float) (this.f16113b.f16121c - this.f16113b.f16119a));
    }

    @Override // o2.COUIIOverScroller
    public boolean j() {
        return this.f16112a.f16129k && this.f16113b.f16129k;
    }

    public void k(int i10, int i11, int i12, int i13) {
        this.f16115d = 1;
        this.f16112a.m(i10, i12, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        this.f16113b.m(i11, i13, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
    }

    public void l(float f10) {
        this.f16112a.u(f10);
        this.f16113b.u(f10);
    }

    @Override // android.widget.OverScroller, o2.COUIIOverScroller
    public void notifyHorizontalEdgeReached(int i10, int i11, int i12) {
        this.f16112a.r(i10, i11, i12);
        springBack(i10, 0, 0, 0, 0, 0);
    }

    @Override // android.widget.OverScroller, o2.COUIIOverScroller
    public void notifyVerticalEdgeReached(int i10, int i11, int i12) {
        this.f16113b.r(i10, i11, i12);
        springBack(0, i10, 0, 0, 0, 0);
    }

    @Override // o2.COUIIOverScroller
    public void setFinalX(int i10) {
        if (i10 == -1) {
            return;
        }
        this.f16112a.t(i10);
    }

    public void setFinalY(int i10) {
        if (i10 == -1) {
            return;
        }
        this.f16113b.t(i10);
    }

    @Override // android.widget.OverScroller, o2.COUIIOverScroller
    public boolean springBack(int i10, int i11, int i12, int i13, int i14, int i15) {
        boolean v7 = this.f16112a.v(i10, i12, i13);
        boolean v10 = this.f16113b.v(i11, i14, i15);
        if (v7 || v10) {
            this.f16115d = 1;
        }
        return v7 || v10;
    }

    @Override // android.widget.OverScroller, o2.COUIIOverScroller
    public void startScroll(int i10, int i11, int i12, int i13) {
        startScroll(i10, i11, i12, i13, 250);
    }

    public COUILocateOverScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
        this.f16112a = new b(context);
        this.f16113b = new b(context);
        if (interpolator == null) {
            this.f16114c = f16111e;
        } else {
            this.f16114c = interpolator;
        }
    }

    @Override // android.widget.OverScroller, o2.COUIIOverScroller
    public void fling(int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, int i18, int i19) {
        if (i11 <= i17 && i11 >= i16) {
            k(i10, i11, i12, i13);
        } else {
            springBack(i10, i11, i14, i15, i16, i17);
        }
    }

    @Override // android.widget.OverScroller, o2.COUIIOverScroller
    public void startScroll(int i10, int i11, int i12, int i13, int i14) {
        this.f16115d = 0;
        this.f16112a.y(i10, i12, i14);
        this.f16113b.y(i11, i13, i14);
    }
}
