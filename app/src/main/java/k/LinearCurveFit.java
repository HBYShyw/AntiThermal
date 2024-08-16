package k;

import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;

/* compiled from: LinearCurveFit.java */
/* renamed from: k.d, reason: use source file name */
/* loaded from: classes.dex */
public class LinearCurveFit extends CurveFit {

    /* renamed from: a, reason: collision with root package name */
    private double[] f13967a;

    /* renamed from: b, reason: collision with root package name */
    private double[][] f13968b;

    /* renamed from: c, reason: collision with root package name */
    private double f13969c;

    public LinearCurveFit(double[] dArr, double[][] dArr2) {
        this.f13969c = Double.NaN;
        int length = dArr.length;
        int length2 = dArr2[0].length;
        this.f13967a = dArr;
        this.f13968b = dArr2;
        if (length2 > 2) {
            int i10 = 0;
            double d10 = 0.0d;
            double d11 = 0.0d;
            while (i10 < dArr.length) {
                double d12 = dArr2[i10][0];
                double d13 = dArr2[i10][0];
                if (i10 > 0) {
                    Math.hypot(d12 - d10, d13 - d11);
                }
                i10++;
                d10 = d12;
                d11 = d13;
            }
            this.f13969c = UserProfileInfo.Constant.NA_LAT_LON;
        }
    }

    @Override // k.CurveFit
    public double c(double d10, int i10) {
        double[] dArr = this.f13967a;
        int length = dArr.length;
        int i11 = 0;
        if (d10 <= dArr[0]) {
            return this.f13968b[0][i10];
        }
        int i12 = length - 1;
        if (d10 >= dArr[i12]) {
            return this.f13968b[i12][i10];
        }
        while (i11 < i12) {
            double[] dArr2 = this.f13967a;
            if (d10 == dArr2[i11]) {
                return this.f13968b[i11][i10];
            }
            int i13 = i11 + 1;
            if (d10 < dArr2[i13]) {
                double d11 = (d10 - dArr2[i11]) / (dArr2[i13] - dArr2[i11]);
                double[][] dArr3 = this.f13968b;
                return (dArr3[i11][i10] * (1.0d - d11)) + (dArr3[i13][i10] * d11);
            }
            i11 = i13;
        }
        return UserProfileInfo.Constant.NA_LAT_LON;
    }

    @Override // k.CurveFit
    public void d(double d10, double[] dArr) {
        double[] dArr2 = this.f13967a;
        int length = dArr2.length;
        int i10 = 0;
        int length2 = this.f13968b[0].length;
        if (d10 <= dArr2[0]) {
            for (int i11 = 0; i11 < length2; i11++) {
                dArr[i11] = this.f13968b[0][i11];
            }
            return;
        }
        int i12 = length - 1;
        if (d10 >= dArr2[i12]) {
            while (i10 < length2) {
                dArr[i10] = this.f13968b[i12][i10];
                i10++;
            }
            return;
        }
        int i13 = 0;
        while (i13 < i12) {
            if (d10 == this.f13967a[i13]) {
                for (int i14 = 0; i14 < length2; i14++) {
                    dArr[i14] = this.f13968b[i13][i14];
                }
            }
            double[] dArr3 = this.f13967a;
            int i15 = i13 + 1;
            if (d10 < dArr3[i15]) {
                double d11 = (d10 - dArr3[i13]) / (dArr3[i15] - dArr3[i13]);
                while (i10 < length2) {
                    double[][] dArr4 = this.f13968b;
                    dArr[i10] = (dArr4[i13][i10] * (1.0d - d11)) + (dArr4[i15][i10] * d11);
                    i10++;
                }
                return;
            }
            i13 = i15;
        }
    }

    @Override // k.CurveFit
    public void e(double d10, float[] fArr) {
        double[] dArr = this.f13967a;
        int length = dArr.length;
        int i10 = 0;
        int length2 = this.f13968b[0].length;
        if (d10 <= dArr[0]) {
            for (int i11 = 0; i11 < length2; i11++) {
                fArr[i11] = (float) this.f13968b[0][i11];
            }
            return;
        }
        int i12 = length - 1;
        if (d10 >= dArr[i12]) {
            while (i10 < length2) {
                fArr[i10] = (float) this.f13968b[i12][i10];
                i10++;
            }
            return;
        }
        int i13 = 0;
        while (i13 < i12) {
            if (d10 == this.f13967a[i13]) {
                for (int i14 = 0; i14 < length2; i14++) {
                    fArr[i14] = (float) this.f13968b[i13][i14];
                }
            }
            double[] dArr2 = this.f13967a;
            int i15 = i13 + 1;
            if (d10 < dArr2[i15]) {
                double d11 = (d10 - dArr2[i13]) / (dArr2[i15] - dArr2[i13]);
                while (i10 < length2) {
                    double[][] dArr3 = this.f13968b;
                    fArr[i10] = (float) ((dArr3[i13][i10] * (1.0d - d11)) + (dArr3[i15][i10] * d11));
                    i10++;
                }
                return;
            }
            i13 = i15;
        }
    }

    @Override // k.CurveFit
    public double f(double d10, int i10) {
        double[] dArr = this.f13967a;
        int length = dArr.length;
        int i11 = 0;
        if (d10 < dArr[0]) {
            d10 = dArr[0];
        } else {
            int i12 = length - 1;
            if (d10 >= dArr[i12]) {
                d10 = dArr[i12];
            }
        }
        while (i11 < length - 1) {
            double[] dArr2 = this.f13967a;
            int i13 = i11 + 1;
            if (d10 <= dArr2[i13]) {
                double d11 = dArr2[i13] - dArr2[i11];
                double d12 = dArr2[i11];
                double[][] dArr3 = this.f13968b;
                return (dArr3[i13][i10] - dArr3[i11][i10]) / d11;
            }
            i11 = i13;
        }
        return UserProfileInfo.Constant.NA_LAT_LON;
    }

    @Override // k.CurveFit
    public void g(double d10, double[] dArr) {
        double[] dArr2 = this.f13967a;
        int length = dArr2.length;
        int length2 = this.f13968b[0].length;
        if (d10 <= dArr2[0]) {
            d10 = dArr2[0];
        } else {
            int i10 = length - 1;
            if (d10 >= dArr2[i10]) {
                d10 = dArr2[i10];
            }
        }
        int i11 = 0;
        while (i11 < length - 1) {
            double[] dArr3 = this.f13967a;
            int i12 = i11 + 1;
            if (d10 <= dArr3[i12]) {
                double d11 = dArr3[i12] - dArr3[i11];
                double d12 = dArr3[i11];
                for (int i13 = 0; i13 < length2; i13++) {
                    double[][] dArr4 = this.f13968b;
                    dArr[i13] = (dArr4[i12][i13] - dArr4[i11][i13]) / d11;
                }
                return;
            }
            i11 = i12;
        }
    }

    @Override // k.CurveFit
    public double[] h() {
        return this.f13967a;
    }
}
