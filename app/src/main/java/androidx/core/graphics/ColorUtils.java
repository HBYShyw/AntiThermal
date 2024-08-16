package androidx.core.graphics;

import android.graphics.Color;

/* compiled from: ColorUtils.java */
/* renamed from: androidx.core.graphics.a, reason: use source file name */
/* loaded from: classes.dex */
public final class ColorUtils {

    /* renamed from: a, reason: collision with root package name */
    private static final ThreadLocal<double[]> f2183a = new ThreadLocal<>();

    public static int a(float[] fArr) {
        int round;
        int round2;
        int round3;
        float f10 = fArr[0];
        float f11 = fArr[1];
        float f12 = fArr[2];
        float abs = (1.0f - Math.abs((f12 * 2.0f) - 1.0f)) * f11;
        float f13 = f12 - (0.5f * abs);
        float abs2 = (1.0f - Math.abs(((f10 / 60.0f) % 2.0f) - 1.0f)) * abs;
        switch (((int) f10) / 60) {
            case 0:
                round = Math.round((abs + f13) * 255.0f);
                round2 = Math.round((abs2 + f13) * 255.0f);
                round3 = Math.round(f13 * 255.0f);
                break;
            case 1:
                round = Math.round((abs2 + f13) * 255.0f);
                round2 = Math.round((abs + f13) * 255.0f);
                round3 = Math.round(f13 * 255.0f);
                break;
            case 2:
                round = Math.round(f13 * 255.0f);
                round2 = Math.round((abs + f13) * 255.0f);
                round3 = Math.round((abs2 + f13) * 255.0f);
                break;
            case 3:
                round = Math.round(f13 * 255.0f);
                round2 = Math.round((abs2 + f13) * 255.0f);
                round3 = Math.round((abs + f13) * 255.0f);
                break;
            case 4:
                round = Math.round((abs2 + f13) * 255.0f);
                round2 = Math.round(f13 * 255.0f);
                round3 = Math.round((abs + f13) * 255.0f);
                break;
            case 5:
            case 6:
                round = Math.round((abs + f13) * 255.0f);
                round2 = Math.round(f13 * 255.0f);
                round3 = Math.round((abs2 + f13) * 255.0f);
                break;
            default:
                round3 = 0;
                round = 0;
                round2 = 0;
                break;
        }
        return Color.rgb(l(round, 0, 255), l(round2, 0, 255), l(round3, 0, 255));
    }

    public static void b(int i10, int i11, int i12, float[] fArr) {
        float f10;
        float abs;
        float f11 = i10 / 255.0f;
        float f12 = i11 / 255.0f;
        float f13 = i12 / 255.0f;
        float max = Math.max(f11, Math.max(f12, f13));
        float min = Math.min(f11, Math.min(f12, f13));
        float f14 = max - min;
        float f15 = (max + min) / 2.0f;
        if (max == min) {
            f10 = 0.0f;
            abs = 0.0f;
        } else {
            f10 = max == f11 ? ((f12 - f13) / f14) % 6.0f : max == f12 ? ((f13 - f11) / f14) + 2.0f : 4.0f + ((f11 - f12) / f14);
            abs = f14 / (1.0f - Math.abs((2.0f * f15) - 1.0f));
        }
        float f16 = (f10 * 60.0f) % 360.0f;
        if (f16 < 0.0f) {
            f16 += 360.0f;
        }
        fArr[0] = k(f16, 0.0f, 360.0f);
        fArr[1] = k(abs, 0.0f, 1.0f);
        fArr[2] = k(f15, 0.0f, 1.0f);
    }

    public static void c(int i10, int i11, int i12, double[] dArr) {
        if (dArr.length == 3) {
            double d10 = i10 / 255.0d;
            double pow = d10 < 0.04045d ? d10 / 12.92d : Math.pow((d10 + 0.055d) / 1.055d, 2.4d);
            double d11 = i11 / 255.0d;
            double pow2 = d11 < 0.04045d ? d11 / 12.92d : Math.pow((d11 + 0.055d) / 1.055d, 2.4d);
            double d12 = i12 / 255.0d;
            double pow3 = d12 < 0.04045d ? d12 / 12.92d : Math.pow((d12 + 0.055d) / 1.055d, 2.4d);
            dArr[0] = ((0.4124d * pow) + (0.3576d * pow2) + (0.1805d * pow3)) * 100.0d;
            dArr[1] = ((0.2126d * pow) + (0.7152d * pow2) + (0.0722d * pow3)) * 100.0d;
            dArr[2] = ((pow * 0.0193d) + (pow2 * 0.1192d) + (pow3 * 0.9505d)) * 100.0d;
            return;
        }
        throw new IllegalArgumentException("outXyz must have a length of 3.");
    }

    public static int d(double d10, double d11, double d12) {
        double d13 = (((3.2406d * d10) + ((-1.5372d) * d11)) + ((-0.4986d) * d12)) / 100.0d;
        double d14 = ((((-0.9689d) * d10) + (1.8758d * d11)) + (0.0415d * d12)) / 100.0d;
        double d15 = (((0.0557d * d10) + ((-0.204d) * d11)) + (1.057d * d12)) / 100.0d;
        return Color.rgb(l((int) Math.round((d13 > 0.0031308d ? (Math.pow(d13, 0.4166666666666667d) * 1.055d) - 0.055d : d13 * 12.92d) * 255.0d), 0, 255), l((int) Math.round((d14 > 0.0031308d ? (Math.pow(d14, 0.4166666666666667d) * 1.055d) - 0.055d : d14 * 12.92d) * 255.0d), 0, 255), l((int) Math.round((d15 > 0.0031308d ? (Math.pow(d15, 0.4166666666666667d) * 1.055d) - 0.055d : d15 * 12.92d) * 255.0d), 0, 255));
    }

    public static double e(int i10) {
        double[] m10 = m();
        g(i10, m10);
        return m10[1] / 100.0d;
    }

    public static void f(int i10, float[] fArr) {
        b(Color.red(i10), Color.green(i10), Color.blue(i10), fArr);
    }

    public static void g(int i10, double[] dArr) {
        c(Color.red(i10), Color.green(i10), Color.blue(i10), dArr);
    }

    private static int h(int i10, int i11) {
        return 255 - (((255 - i11) * (255 - i10)) / 255);
    }

    public static int i(int i10, int i11) {
        int alpha = Color.alpha(i11);
        int alpha2 = Color.alpha(i10);
        int h10 = h(alpha2, alpha);
        return Color.argb(h10, j(Color.red(i10), alpha2, Color.red(i11), alpha, h10), j(Color.green(i10), alpha2, Color.green(i11), alpha, h10), j(Color.blue(i10), alpha2, Color.blue(i11), alpha, h10));
    }

    private static int j(int i10, int i11, int i12, int i13, int i14) {
        if (i14 == 0) {
            return 0;
        }
        return (((i10 * 255) * i11) + ((i12 * i13) * (255 - i11))) / (i14 * 255);
    }

    private static float k(float f10, float f11, float f12) {
        return f10 < f11 ? f11 : Math.min(f10, f12);
    }

    private static int l(int i10, int i11, int i12) {
        return i10 < i11 ? i11 : Math.min(i10, i12);
    }

    private static double[] m() {
        ThreadLocal<double[]> threadLocal = f2183a;
        double[] dArr = threadLocal.get();
        if (dArr != null) {
            return dArr;
        }
        double[] dArr2 = new double[3];
        threadLocal.set(dArr2);
        return dArr2;
    }

    public static int n(int i10, int i11) {
        if (i11 < 0 || i11 > 255) {
            throw new IllegalArgumentException("alpha must be between 0 and 255.");
        }
        return (i10 & 16777215) | (i11 << 24);
    }
}
