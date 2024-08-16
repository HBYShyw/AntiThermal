package k;

import android.util.Log;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import java.util.Arrays;

/* compiled from: Easing.java */
/* renamed from: k.c, reason: use source file name */
/* loaded from: classes.dex */
public class Easing {

    /* renamed from: b, reason: collision with root package name */
    static Easing f13958b = new Easing();

    /* renamed from: c, reason: collision with root package name */
    public static String[] f13959c = {"standard", "accelerate", "decelerate", "linear"};

    /* renamed from: a, reason: collision with root package name */
    String f13960a = "identity";

    /* compiled from: Easing.java */
    /* renamed from: k.c$a */
    /* loaded from: classes.dex */
    static class a extends Easing {

        /* renamed from: h, reason: collision with root package name */
        private static double f13961h = 0.01d;

        /* renamed from: i, reason: collision with root package name */
        private static double f13962i = 1.0E-4d;

        /* renamed from: d, reason: collision with root package name */
        double f13963d;

        /* renamed from: e, reason: collision with root package name */
        double f13964e;

        /* renamed from: f, reason: collision with root package name */
        double f13965f;

        /* renamed from: g, reason: collision with root package name */
        double f13966g;

        a(String str) {
            this.f13960a = str;
            int indexOf = str.indexOf(40);
            int indexOf2 = str.indexOf(44, indexOf);
            this.f13963d = Double.parseDouble(str.substring(indexOf + 1, indexOf2).trim());
            int i10 = indexOf2 + 1;
            int indexOf3 = str.indexOf(44, i10);
            this.f13964e = Double.parseDouble(str.substring(i10, indexOf3).trim());
            int i11 = indexOf3 + 1;
            int indexOf4 = str.indexOf(44, i11);
            this.f13965f = Double.parseDouble(str.substring(i11, indexOf4).trim());
            int i12 = indexOf4 + 1;
            this.f13966g = Double.parseDouble(str.substring(i12, str.indexOf(41, i12)).trim());
        }

        private double d(double d10) {
            double d11 = 1.0d - d10;
            double d12 = 3.0d * d11;
            return (this.f13963d * d11 * d12 * d10) + (this.f13965f * d12 * d10 * d10) + (d10 * d10 * d10);
        }

        private double e(double d10) {
            double d11 = 1.0d - d10;
            double d12 = 3.0d * d11;
            return (this.f13964e * d11 * d12 * d10) + (this.f13966g * d12 * d10 * d10) + (d10 * d10 * d10);
        }

        @Override // k.Easing
        public double a(double d10) {
            if (d10 <= UserProfileInfo.Constant.NA_LAT_LON) {
                return UserProfileInfo.Constant.NA_LAT_LON;
            }
            if (d10 >= 1.0d) {
                return 1.0d;
            }
            double d11 = 0.5d;
            double d12 = 0.5d;
            while (d11 > f13961h) {
                d11 *= 0.5d;
                d12 = d(d12) < d10 ? d12 + d11 : d12 - d11;
            }
            double d13 = d12 - d11;
            double d14 = d(d13);
            double d15 = d12 + d11;
            double d16 = d(d15);
            double e10 = e(d13);
            return (((e(d15) - e10) * (d10 - d14)) / (d16 - d14)) + e10;
        }

        @Override // k.Easing
        public double b(double d10) {
            double d11 = 0.5d;
            double d12 = 0.5d;
            while (d11 > f13962i) {
                d11 *= 0.5d;
                d12 = d(d12) < d10 ? d12 + d11 : d12 - d11;
            }
            double d13 = d12 - d11;
            double d14 = d12 + d11;
            return (e(d14) - e(d13)) / (d(d14) - d(d13));
        }
    }

    public static Easing c(String str) {
        if (str == null) {
            return null;
        }
        if (str.startsWith("cubic")) {
            return new a(str);
        }
        char c10 = 65535;
        switch (str.hashCode()) {
            case -1354466595:
                if (str.equals("accelerate")) {
                    c10 = 0;
                    break;
                }
                break;
            case -1263948740:
                if (str.equals("decelerate")) {
                    c10 = 1;
                    break;
                }
                break;
            case -1102672091:
                if (str.equals("linear")) {
                    c10 = 2;
                    break;
                }
                break;
            case 1312628413:
                if (str.equals("standard")) {
                    c10 = 3;
                    break;
                }
                break;
        }
        switch (c10) {
            case 0:
                return new a("cubic(0.4, 0.05, 0.8, 0.7)");
            case 1:
                return new a("cubic(0.0, 0.0, 0.2, 0.95)");
            case 2:
                return new a("cubic(1, 1, 0, 0)");
            case 3:
                return new a("cubic(0.4, 0.0, 0.2, 1)");
            default:
                Log.e("ConstraintSet", "transitionEasing syntax error syntax:transitionEasing=\"cubic(1.0,0.5,0.0,0.6)\" or " + Arrays.toString(f13959c));
                return f13958b;
        }
    }

    public double a(double d10) {
        return d10;
    }

    public double b(double d10) {
        return 1.0d;
    }

    public String toString() {
        return this.f13960a;
    }
}
