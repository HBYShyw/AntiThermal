package k;

import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import java.util.Arrays;

/* compiled from: ArcCurveFit.java */
/* renamed from: k.a, reason: use source file name */
/* loaded from: classes.dex */
class ArcCurveFit extends CurveFit {

    /* renamed from: a, reason: collision with root package name */
    private final double[] f13935a;

    /* renamed from: b, reason: collision with root package name */
    a[] f13936b;

    /* compiled from: ArcCurveFit.java */
    /* renamed from: k.a$a */
    /* loaded from: classes.dex */
    private static class a {

        /* renamed from: s, reason: collision with root package name */
        private static double[] f13937s = new double[91];

        /* renamed from: a, reason: collision with root package name */
        double[] f13938a;

        /* renamed from: b, reason: collision with root package name */
        double f13939b;

        /* renamed from: c, reason: collision with root package name */
        double f13940c;

        /* renamed from: d, reason: collision with root package name */
        double f13941d;

        /* renamed from: e, reason: collision with root package name */
        double f13942e;

        /* renamed from: f, reason: collision with root package name */
        double f13943f;

        /* renamed from: g, reason: collision with root package name */
        double f13944g;

        /* renamed from: h, reason: collision with root package name */
        double f13945h;

        /* renamed from: i, reason: collision with root package name */
        double f13946i;

        /* renamed from: j, reason: collision with root package name */
        double f13947j;

        /* renamed from: k, reason: collision with root package name */
        double f13948k;

        /* renamed from: l, reason: collision with root package name */
        double f13949l;

        /* renamed from: m, reason: collision with root package name */
        double f13950m;

        /* renamed from: n, reason: collision with root package name */
        double f13951n;

        /* renamed from: o, reason: collision with root package name */
        double f13952o;

        /* renamed from: p, reason: collision with root package name */
        double f13953p;

        /* renamed from: q, reason: collision with root package name */
        boolean f13954q;

        /* renamed from: r, reason: collision with root package name */
        boolean f13955r;

        a(int i10, double d10, double d11, double d12, double d13, double d14, double d15) {
            this.f13955r = false;
            this.f13954q = i10 == 1;
            this.f13940c = d10;
            this.f13941d = d11;
            this.f13946i = 1.0d / (d11 - d10);
            if (3 == i10) {
                this.f13955r = true;
            }
            double d16 = d14 - d12;
            double d17 = d15 - d13;
            if (!this.f13955r && Math.abs(d16) >= 0.001d && Math.abs(d17) >= 0.001d) {
                this.f13938a = new double[101];
                boolean z10 = this.f13954q;
                this.f13947j = d16 * (z10 ? -1 : 1);
                this.f13948k = d17 * (z10 ? 1 : -1);
                this.f13949l = z10 ? d14 : d12;
                this.f13950m = z10 ? d13 : d15;
                a(d12, d13, d14, d15);
                this.f13951n = this.f13939b * this.f13946i;
                return;
            }
            this.f13955r = true;
            this.f13942e = d12;
            this.f13943f = d14;
            this.f13944g = d13;
            this.f13945h = d15;
            double hypot = Math.hypot(d17, d16);
            this.f13939b = hypot;
            this.f13951n = hypot * this.f13946i;
            double d18 = this.f13941d;
            double d19 = this.f13940c;
            this.f13949l = d16 / (d18 - d19);
            this.f13950m = d17 / (d18 - d19);
        }

        private void a(double d10, double d11, double d12, double d13) {
            double d14;
            double d15 = d12 - d10;
            double d16 = d11 - d13;
            int i10 = 0;
            double d17 = UserProfileInfo.Constant.NA_LAT_LON;
            double d18 = UserProfileInfo.Constant.NA_LAT_LON;
            double d19 = UserProfileInfo.Constant.NA_LAT_LON;
            while (true) {
                if (i10 >= f13937s.length) {
                    break;
                }
                double d20 = d17;
                double radians = Math.toRadians((i10 * 90.0d) / (r15.length - 1));
                double sin = Math.sin(radians) * d15;
                double cos = Math.cos(radians) * d16;
                if (i10 > 0) {
                    d14 = Math.hypot(sin - d18, cos - d19) + d20;
                    f13937s[i10] = d14;
                } else {
                    d14 = d20;
                }
                i10++;
                d19 = cos;
                d17 = d14;
                d18 = sin;
            }
            double d21 = d17;
            this.f13939b = d21;
            int i11 = 0;
            while (true) {
                double[] dArr = f13937s;
                if (i11 >= dArr.length) {
                    break;
                }
                dArr[i11] = dArr[i11] / d21;
                i11++;
            }
            int i12 = 0;
            while (true) {
                if (i12 >= this.f13938a.length) {
                    return;
                }
                double length = i12 / (r1.length - 1);
                int binarySearch = Arrays.binarySearch(f13937s, length);
                if (binarySearch >= 0) {
                    this.f13938a[i12] = binarySearch / (f13937s.length - 1);
                } else if (binarySearch == -1) {
                    this.f13938a[i12] = 0.0d;
                } else {
                    int i13 = -binarySearch;
                    int i14 = i13 - 2;
                    double[] dArr2 = f13937s;
                    this.f13938a[i12] = (i14 + ((length - dArr2[i14]) / (dArr2[i13 - 1] - dArr2[i14]))) / (dArr2.length - 1);
                }
                i12++;
            }
        }

        double b() {
            double d10 = this.f13947j * this.f13953p;
            double hypot = this.f13951n / Math.hypot(d10, (-this.f13948k) * this.f13952o);
            if (this.f13954q) {
                d10 = -d10;
            }
            return d10 * hypot;
        }

        double c() {
            double d10 = this.f13947j * this.f13953p;
            double d11 = (-this.f13948k) * this.f13952o;
            double hypot = this.f13951n / Math.hypot(d10, d11);
            return this.f13954q ? (-d11) * hypot : d11 * hypot;
        }

        public double d(double d10) {
            return this.f13949l;
        }

        public double e(double d10) {
            return this.f13950m;
        }

        public double f(double d10) {
            double d11 = (d10 - this.f13940c) * this.f13946i;
            double d12 = this.f13942e;
            return d12 + (d11 * (this.f13943f - d12));
        }

        public double g(double d10) {
            double d11 = (d10 - this.f13940c) * this.f13946i;
            double d12 = this.f13944g;
            return d12 + (d11 * (this.f13945h - d12));
        }

        double h() {
            return this.f13949l + (this.f13947j * this.f13952o);
        }

        double i() {
            return this.f13950m + (this.f13948k * this.f13953p);
        }

        double j(double d10) {
            if (d10 <= UserProfileInfo.Constant.NA_LAT_LON) {
                return UserProfileInfo.Constant.NA_LAT_LON;
            }
            if (d10 >= 1.0d) {
                return 1.0d;
            }
            double[] dArr = this.f13938a;
            double length = d10 * (dArr.length - 1);
            int i10 = (int) length;
            return dArr[i10] + ((length - i10) * (dArr[i10 + 1] - dArr[i10]));
        }

        void k(double d10) {
            double j10 = j((this.f13954q ? this.f13941d - d10 : d10 - this.f13940c) * this.f13946i) * 1.5707963267948966d;
            this.f13952o = Math.sin(j10);
            this.f13953p = Math.cos(j10);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:9:0x0026, code lost:
    
        if (r5 == 1) goto L12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ArcCurveFit(int[] iArr, double[] dArr, double[][] dArr2) {
        this.f13935a = dArr;
        this.f13936b = new a[dArr.length - 1];
        int i10 = 0;
        int i11 = 1;
        int i12 = 1;
        while (true) {
            a[] aVarArr = this.f13936b;
            if (i10 >= aVarArr.length) {
                return;
            }
            int i13 = iArr[i10];
            if (i13 != 0) {
                if (i13 != 1) {
                    if (i13 != 2) {
                        if (i13 == 3) {
                        }
                    }
                    i11 = 2;
                    i12 = i11;
                }
                i11 = 1;
                i12 = i11;
            } else {
                i12 = 3;
            }
            int i14 = i10 + 1;
            aVarArr[i10] = new a(i12, dArr[i10], dArr[i14], dArr2[i10][0], dArr2[i10][1], dArr2[i14][0], dArr2[i14][1]);
            i10 = i14;
        }
    }

    @Override // k.CurveFit
    public double c(double d10, int i10) {
        a[] aVarArr = this.f13936b;
        int i11 = 0;
        if (d10 < aVarArr[0].f13940c) {
            d10 = aVarArr[0].f13940c;
        } else if (d10 > aVarArr[aVarArr.length - 1].f13941d) {
            d10 = aVarArr[aVarArr.length - 1].f13941d;
        }
        while (true) {
            a[] aVarArr2 = this.f13936b;
            if (i11 >= aVarArr2.length) {
                return Double.NaN;
            }
            if (d10 <= aVarArr2[i11].f13941d) {
                if (aVarArr2[i11].f13955r) {
                    if (i10 == 0) {
                        return aVarArr2[i11].f(d10);
                    }
                    return aVarArr2[i11].g(d10);
                }
                aVarArr2[i11].k(d10);
                if (i10 == 0) {
                    return this.f13936b[i11].h();
                }
                return this.f13936b[i11].i();
            }
            i11++;
        }
    }

    @Override // k.CurveFit
    public void d(double d10, double[] dArr) {
        a[] aVarArr = this.f13936b;
        if (d10 < aVarArr[0].f13940c) {
            d10 = aVarArr[0].f13940c;
        }
        if (d10 > aVarArr[aVarArr.length - 1].f13941d) {
            d10 = aVarArr[aVarArr.length - 1].f13941d;
        }
        int i10 = 0;
        while (true) {
            a[] aVarArr2 = this.f13936b;
            if (i10 >= aVarArr2.length) {
                return;
            }
            if (d10 <= aVarArr2[i10].f13941d) {
                if (aVarArr2[i10].f13955r) {
                    dArr[0] = aVarArr2[i10].f(d10);
                    dArr[1] = this.f13936b[i10].g(d10);
                    return;
                } else {
                    aVarArr2[i10].k(d10);
                    dArr[0] = this.f13936b[i10].h();
                    dArr[1] = this.f13936b[i10].i();
                    return;
                }
            }
            i10++;
        }
    }

    @Override // k.CurveFit
    public void e(double d10, float[] fArr) {
        a[] aVarArr = this.f13936b;
        if (d10 < aVarArr[0].f13940c) {
            d10 = aVarArr[0].f13940c;
        } else if (d10 > aVarArr[aVarArr.length - 1].f13941d) {
            d10 = aVarArr[aVarArr.length - 1].f13941d;
        }
        int i10 = 0;
        while (true) {
            a[] aVarArr2 = this.f13936b;
            if (i10 >= aVarArr2.length) {
                return;
            }
            if (d10 <= aVarArr2[i10].f13941d) {
                if (aVarArr2[i10].f13955r) {
                    fArr[0] = (float) aVarArr2[i10].f(d10);
                    fArr[1] = (float) this.f13936b[i10].g(d10);
                    return;
                } else {
                    aVarArr2[i10].k(d10);
                    fArr[0] = (float) this.f13936b[i10].h();
                    fArr[1] = (float) this.f13936b[i10].i();
                    return;
                }
            }
            i10++;
        }
    }

    @Override // k.CurveFit
    public double f(double d10, int i10) {
        a[] aVarArr = this.f13936b;
        int i11 = 0;
        if (d10 < aVarArr[0].f13940c) {
            d10 = aVarArr[0].f13940c;
        }
        if (d10 > aVarArr[aVarArr.length - 1].f13941d) {
            d10 = aVarArr[aVarArr.length - 1].f13941d;
        }
        while (true) {
            a[] aVarArr2 = this.f13936b;
            if (i11 >= aVarArr2.length) {
                return Double.NaN;
            }
            if (d10 <= aVarArr2[i11].f13941d) {
                if (aVarArr2[i11].f13955r) {
                    if (i10 == 0) {
                        return aVarArr2[i11].d(d10);
                    }
                    return aVarArr2[i11].e(d10);
                }
                aVarArr2[i11].k(d10);
                if (i10 == 0) {
                    return this.f13936b[i11].b();
                }
                return this.f13936b[i11].c();
            }
            i11++;
        }
    }

    @Override // k.CurveFit
    public void g(double d10, double[] dArr) {
        a[] aVarArr = this.f13936b;
        if (d10 < aVarArr[0].f13940c) {
            d10 = aVarArr[0].f13940c;
        } else if (d10 > aVarArr[aVarArr.length - 1].f13941d) {
            d10 = aVarArr[aVarArr.length - 1].f13941d;
        }
        int i10 = 0;
        while (true) {
            a[] aVarArr2 = this.f13936b;
            if (i10 >= aVarArr2.length) {
                return;
            }
            if (d10 <= aVarArr2[i10].f13941d) {
                if (aVarArr2[i10].f13955r) {
                    dArr[0] = aVarArr2[i10].d(d10);
                    dArr[1] = this.f13936b[i10].e(d10);
                    return;
                } else {
                    aVarArr2[i10].k(d10);
                    dArr[0] = this.f13936b[i10].b();
                    dArr[1] = this.f13936b[i10].c();
                    return;
                }
            }
            i10++;
        }
    }

    @Override // k.CurveFit
    public double[] h() {
        return this.f13935a;
    }
}
