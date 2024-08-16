package k;

import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import java.util.Arrays;

/* compiled from: Oscillator.java */
/* renamed from: k.f, reason: use source file name */
/* loaded from: classes.dex */
public class Oscillator {

    /* renamed from: c, reason: collision with root package name */
    double[] f13975c;

    /* renamed from: d, reason: collision with root package name */
    int f13976d;

    /* renamed from: a, reason: collision with root package name */
    float[] f13973a = new float[0];

    /* renamed from: b, reason: collision with root package name */
    double[] f13974b = new double[0];

    /* renamed from: e, reason: collision with root package name */
    double f13977e = 6.283185307179586d;

    /* renamed from: f, reason: collision with root package name */
    private boolean f13978f = false;

    public void a(double d10, float f10) {
        int length = this.f13973a.length + 1;
        int binarySearch = Arrays.binarySearch(this.f13974b, d10);
        if (binarySearch < 0) {
            binarySearch = (-binarySearch) - 1;
        }
        this.f13974b = Arrays.copyOf(this.f13974b, length);
        this.f13973a = Arrays.copyOf(this.f13973a, length);
        this.f13975c = new double[length];
        double[] dArr = this.f13974b;
        System.arraycopy(dArr, binarySearch, dArr, binarySearch + 1, (length - binarySearch) - 1);
        this.f13974b[binarySearch] = d10;
        this.f13973a[binarySearch] = f10;
        this.f13978f = false;
    }

    double b(double d10) {
        if (d10 <= UserProfileInfo.Constant.NA_LAT_LON) {
            d10 = 1.0E-5d;
        } else if (d10 >= 1.0d) {
            d10 = 0.999999d;
        }
        int binarySearch = Arrays.binarySearch(this.f13974b, d10);
        if (binarySearch > 0 || binarySearch == 0) {
            return UserProfileInfo.Constant.NA_LAT_LON;
        }
        int i10 = (-binarySearch) - 1;
        float[] fArr = this.f13973a;
        int i11 = i10 - 1;
        double d11 = fArr[i10] - fArr[i11];
        double[] dArr = this.f13974b;
        double d12 = d11 / (dArr[i10] - dArr[i11]);
        return (fArr[i11] - (d12 * dArr[i11])) + (d10 * d12);
    }

    double c(double d10) {
        if (d10 < UserProfileInfo.Constant.NA_LAT_LON) {
            d10 = 0.0d;
        } else if (d10 > 1.0d) {
            d10 = 1.0d;
        }
        int binarySearch = Arrays.binarySearch(this.f13974b, d10);
        if (binarySearch > 0) {
            return 1.0d;
        }
        if (binarySearch == 0) {
            return UserProfileInfo.Constant.NA_LAT_LON;
        }
        int i10 = (-binarySearch) - 1;
        float[] fArr = this.f13973a;
        int i11 = i10 - 1;
        double d11 = fArr[i10] - fArr[i11];
        double[] dArr = this.f13974b;
        double d12 = d11 / (dArr[i10] - dArr[i11]);
        return this.f13975c[i11] + ((fArr[i11] - (dArr[i11] * d12)) * (d10 - dArr[i11])) + ((d12 * ((d10 * d10) - (dArr[i11] * dArr[i11]))) / 2.0d);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0006. Please report as an issue. */
    public double d(double d10) {
        double b10;
        double signum;
        double b11;
        double b12;
        double sin;
        switch (this.f13976d) {
            case 1:
                return UserProfileInfo.Constant.NA_LAT_LON;
            case 2:
                b10 = b(d10) * 4.0d;
                signum = Math.signum((((c(d10) * 4.0d) + 3.0d) % 4.0d) - 2.0d);
                return b10 * signum;
            case 3:
                b11 = b(d10);
                return b11 * 2.0d;
            case 4:
                b11 = -b(d10);
                return b11 * 2.0d;
            case 5:
                b12 = (-this.f13977e) * b(d10);
                sin = Math.sin(this.f13977e * c(d10));
                return b12 * sin;
            case 6:
                b10 = b(d10) * 4.0d;
                signum = (((c(d10) * 4.0d) + 2.0d) % 4.0d) - 2.0d;
                return b10 * signum;
            default:
                b12 = this.f13977e * b(d10);
                sin = Math.cos(this.f13977e * c(d10));
                return b12 * sin;
        }
    }

    public double e(double d10) {
        double abs;
        switch (this.f13976d) {
            case 1:
                return Math.signum(0.5d - (c(d10) % 1.0d));
            case 2:
                abs = Math.abs((((c(d10) * 4.0d) + 1.0d) % 4.0d) - 2.0d);
                break;
            case 3:
                return (((c(d10) * 2.0d) + 1.0d) % 2.0d) - 1.0d;
            case 4:
                abs = ((c(d10) * 2.0d) + 1.0d) % 2.0d;
                break;
            case 5:
                return Math.cos(this.f13977e * c(d10));
            case 6:
                double abs2 = 1.0d - Math.abs(((c(d10) * 4.0d) % 4.0d) - 2.0d);
                abs = abs2 * abs2;
                break;
            default:
                return Math.sin(this.f13977e * c(d10));
        }
        return 1.0d - abs;
    }

    public void f() {
        int i10 = 0;
        double d10 = 0.0d;
        while (true) {
            if (i10 >= this.f13973a.length) {
                break;
            }
            d10 += r7[i10];
            i10++;
        }
        double d11 = 0.0d;
        int i11 = 1;
        while (true) {
            float[] fArr = this.f13973a;
            if (i11 >= fArr.length) {
                break;
            }
            int i12 = i11 - 1;
            float f10 = (fArr[i12] + fArr[i11]) / 2.0f;
            double[] dArr = this.f13974b;
            d11 += (dArr[i11] - dArr[i12]) * f10;
            i11++;
        }
        int i13 = 0;
        while (true) {
            float[] fArr2 = this.f13973a;
            if (i13 >= fArr2.length) {
                break;
            }
            fArr2[i13] = (float) (fArr2[i13] * (d10 / d11));
            i13++;
        }
        this.f13975c[0] = 0.0d;
        int i14 = 1;
        while (true) {
            float[] fArr3 = this.f13973a;
            if (i14 < fArr3.length) {
                int i15 = i14 - 1;
                float f11 = (fArr3[i15] + fArr3[i14]) / 2.0f;
                double[] dArr2 = this.f13974b;
                double d12 = dArr2[i14] - dArr2[i15];
                double[] dArr3 = this.f13975c;
                dArr3[i14] = dArr3[i15] + (d12 * f11);
                i14++;
            } else {
                this.f13978f = true;
                return;
            }
        }
    }

    public void g(int i10) {
        this.f13976d = i10;
    }

    public String toString() {
        return "pos =" + Arrays.toString(this.f13974b) + " period=" + Arrays.toString(this.f13973a);
    }
}
