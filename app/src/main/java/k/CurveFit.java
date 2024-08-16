package k;

import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;

/* compiled from: CurveFit.java */
/* renamed from: k.b, reason: use source file name */
/* loaded from: classes.dex */
public abstract class CurveFit {

    /* compiled from: CurveFit.java */
    /* renamed from: k.b$a */
    /* loaded from: classes.dex */
    static class a extends CurveFit {

        /* renamed from: a, reason: collision with root package name */
        double f13956a;

        /* renamed from: b, reason: collision with root package name */
        double[] f13957b;

        a(double d10, double[] dArr) {
            this.f13956a = d10;
            this.f13957b = dArr;
        }

        @Override // k.CurveFit
        public double c(double d10, int i10) {
            return this.f13957b[i10];
        }

        @Override // k.CurveFit
        public void d(double d10, double[] dArr) {
            double[] dArr2 = this.f13957b;
            System.arraycopy(dArr2, 0, dArr, 0, dArr2.length);
        }

        @Override // k.CurveFit
        public void e(double d10, float[] fArr) {
            int i10 = 0;
            while (true) {
                double[] dArr = this.f13957b;
                if (i10 >= dArr.length) {
                    return;
                }
                fArr[i10] = (float) dArr[i10];
                i10++;
            }
        }

        @Override // k.CurveFit
        public double f(double d10, int i10) {
            return UserProfileInfo.Constant.NA_LAT_LON;
        }

        @Override // k.CurveFit
        public void g(double d10, double[] dArr) {
            for (int i10 = 0; i10 < this.f13957b.length; i10++) {
                dArr[i10] = 0.0d;
            }
        }

        @Override // k.CurveFit
        public double[] h() {
            return new double[]{this.f13956a};
        }
    }

    public static CurveFit a(int i10, double[] dArr, double[][] dArr2) {
        if (dArr.length == 1) {
            i10 = 2;
        }
        if (i10 == 0) {
            return new MonotonicCurveFit(dArr, dArr2);
        }
        if (i10 != 2) {
            return new LinearCurveFit(dArr, dArr2);
        }
        return new a(dArr[0], dArr2[0]);
    }

    public static CurveFit b(int[] iArr, double[] dArr, double[][] dArr2) {
        return new ArcCurveFit(iArr, dArr, dArr2);
    }

    public abstract double c(double d10, int i10);

    public abstract void d(double d10, double[] dArr);

    public abstract void e(double d10, float[] fArr);

    public abstract double f(double d10, int i10);

    public abstract void g(double d10, double[] dArr);

    public abstract double[] h();
}
