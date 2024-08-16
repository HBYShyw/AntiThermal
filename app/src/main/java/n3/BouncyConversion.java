package n3;

import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;

/* compiled from: BouncyConversion.java */
/* renamed from: n3.c, reason: use source file name */
/* loaded from: classes.dex */
public class BouncyConversion {

    /* renamed from: a, reason: collision with root package name */
    private final double f15725a;

    /* renamed from: b, reason: collision with root package name */
    private final double f15726b;

    /* renamed from: c, reason: collision with root package name */
    private final double f15727c;

    /* renamed from: d, reason: collision with root package name */
    private final double f15728d;

    public BouncyConversion(double d10, double d11) {
        this.f15727c = d10;
        this.f15728d = d11;
        double i10 = i(h(d11 / 1.7d, UserProfileInfo.Constant.NA_LAT_LON, 20.0d), UserProfileInfo.Constant.NA_LAT_LON, 0.8d);
        double i11 = i(h(d10 / 1.7d, UserProfileInfo.Constant.NA_LAT_LON, 20.0d), 0.5d, 200.0d);
        this.f15725a = i11;
        this.f15726b = j(i10, d(i11), 0.01d);
    }

    private double a(double d10) {
        return ((Math.pow(d10, 3.0d) * 7.0E-4d) - (Math.pow(d10, 2.0d) * 0.031d)) + (d10 * 0.64d) + 1.28d;
    }

    private double b(double d10) {
        return ((Math.pow(d10, 3.0d) * 4.4E-5d) - (Math.pow(d10, 2.0d) * 0.006d)) + (d10 * 0.36d) + 2.0d;
    }

    private double c(double d10) {
        return ((Math.pow(d10, 3.0d) * 4.5E-7d) - (Math.pow(d10, 2.0d) * 3.32E-4d)) + (d10 * 0.1078d) + 5.84d;
    }

    private double d(double d10) {
        if (d10 <= 18.0d) {
            return a(d10);
        }
        if (d10 <= 18.0d || d10 > 44.0d) {
            return d10 > 44.0d ? c(d10) : UserProfileInfo.Constant.NA_LAT_LON;
        }
        return b(d10);
    }

    private double g(double d10, double d11, double d12) {
        return (d12 * d10) + ((1.0d - d10) * d11);
    }

    private double h(double d10, double d11, double d12) {
        return (d10 - d11) / (d12 - d11);
    }

    private double i(double d10, double d11, double d12) {
        return d11 + (d10 * (d12 - d11));
    }

    private double j(double d10, double d11, double d12) {
        return g((2.0d * d10) - (d10 * d10), d11, d12);
    }

    public double e() {
        return this.f15726b;
    }

    public double f() {
        return this.f15725a;
    }
}
