package androidx.core.content.res;

import androidx.core.graphics.ColorUtils;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;

/* compiled from: CamColor.java */
/* renamed from: androidx.core.content.res.a, reason: use source file name */
/* loaded from: classes.dex */
class CamColor {

    /* renamed from: a, reason: collision with root package name */
    private final float f2135a;

    /* renamed from: b, reason: collision with root package name */
    private final float f2136b;

    /* renamed from: c, reason: collision with root package name */
    private final float f2137c;

    /* renamed from: d, reason: collision with root package name */
    private final float f2138d;

    /* renamed from: e, reason: collision with root package name */
    private final float f2139e;

    /* renamed from: f, reason: collision with root package name */
    private final float f2140f;

    /* renamed from: g, reason: collision with root package name */
    private final float f2141g;

    /* renamed from: h, reason: collision with root package name */
    private final float f2142h;

    /* renamed from: i, reason: collision with root package name */
    private final float f2143i;

    CamColor(float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17, float f18) {
        this.f2135a = f10;
        this.f2136b = f11;
        this.f2137c = f12;
        this.f2138d = f13;
        this.f2139e = f14;
        this.f2140f = f15;
        this.f2141g = f16;
        this.f2142h = f17;
        this.f2143i = f18;
    }

    private static CamColor b(float f10, float f11, float f12) {
        float f13 = 1000.0f;
        float f14 = 0.0f;
        CamColor camColor = null;
        float f15 = 100.0f;
        float f16 = 1000.0f;
        while (Math.abs(f14 - f15) > 0.01f) {
            float f17 = ((f15 - f14) / 2.0f) + f14;
            int p10 = e(f17, f11, f10).p();
            float b10 = CamUtils.b(p10);
            float abs = Math.abs(f12 - b10);
            if (abs < 0.2f) {
                CamColor c10 = c(p10);
                float a10 = c10.a(e(c10.k(), c10.i(), f10));
                if (a10 <= 1.0f) {
                    camColor = c10;
                    f13 = abs;
                    f16 = a10;
                }
            }
            if (f13 == 0.0f && f16 == 0.0f) {
                break;
            }
            if (b10 < f12) {
                f14 = f17;
            } else {
                f15 = f17;
            }
        }
        return camColor;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CamColor c(int i10) {
        return d(i10, ViewingConditions.f2172k);
    }

    static CamColor d(int i10, ViewingConditions viewingConditions) {
        float[] f10 = CamUtils.f(i10);
        float[][] fArr = CamUtils.f2144a;
        float f11 = (f10[0] * fArr[0][0]) + (f10[1] * fArr[0][1]) + (f10[2] * fArr[0][2]);
        float f12 = (f10[0] * fArr[1][0]) + (f10[1] * fArr[1][1]) + (f10[2] * fArr[1][2]);
        float f13 = (f10[0] * fArr[2][0]) + (f10[1] * fArr[2][1]) + (f10[2] * fArr[2][2]);
        float f14 = viewingConditions.i()[0] * f11;
        float f15 = viewingConditions.i()[1] * f12;
        float f16 = viewingConditions.i()[2] * f13;
        float pow = (float) Math.pow((viewingConditions.c() * Math.abs(f14)) / 100.0d, 0.42d);
        float pow2 = (float) Math.pow((viewingConditions.c() * Math.abs(f15)) / 100.0d, 0.42d);
        float pow3 = (float) Math.pow((viewingConditions.c() * Math.abs(f16)) / 100.0d, 0.42d);
        float signum = ((Math.signum(f14) * 400.0f) * pow) / (pow + 27.13f);
        float signum2 = ((Math.signum(f15) * 400.0f) * pow2) / (pow2 + 27.13f);
        float signum3 = ((Math.signum(f16) * 400.0f) * pow3) / (pow3 + 27.13f);
        double d10 = signum3;
        float f17 = ((float) (((signum * 11.0d) + (signum2 * (-12.0d))) + d10)) / 11.0f;
        float f18 = ((float) ((signum + signum2) - (d10 * 2.0d))) / 9.0f;
        float f19 = signum2 * 20.0f;
        float f20 = (((signum * 20.0f) + f19) + (21.0f * signum3)) / 20.0f;
        float f21 = (((signum * 40.0f) + f19) + signum3) / 20.0f;
        float atan2 = (((float) Math.atan2(f18, f17)) * 180.0f) / 3.1415927f;
        if (atan2 < 0.0f) {
            atan2 += 360.0f;
        } else if (atan2 >= 360.0f) {
            atan2 -= 360.0f;
        }
        float f22 = atan2;
        float f23 = (3.1415927f * f22) / 180.0f;
        float pow4 = ((float) Math.pow((f21 * viewingConditions.f()) / viewingConditions.a(), viewingConditions.b() * viewingConditions.j())) * 100.0f;
        float d11 = viewingConditions.d() * (4.0f / viewingConditions.b()) * ((float) Math.sqrt(pow4 / 100.0f)) * (viewingConditions.a() + 4.0f);
        float pow5 = ((float) Math.pow(1.64d - Math.pow(0.29d, viewingConditions.e()), 0.73d)) * ((float) Math.pow((((((((float) (Math.cos((((((double) f22) < 20.14d ? 360.0f + f22 : f22) * 3.141592653589793d) / 180.0d) + 2.0d) + 3.8d)) * 0.25f) * 3846.1538f) * viewingConditions.g()) * viewingConditions.h()) * ((float) Math.sqrt((f17 * f17) + (f18 * f18)))) / (f20 + 0.305f), 0.9d)) * ((float) Math.sqrt(pow4 / 100.0d));
        float d12 = pow5 * viewingConditions.d();
        float sqrt = ((float) Math.sqrt((r2 * viewingConditions.b()) / (viewingConditions.a() + 4.0f))) * 50.0f;
        float f24 = (1.7f * pow4) / ((0.007f * pow4) + 1.0f);
        float log = ((float) Math.log((0.0228f * d12) + 1.0f)) * 43.85965f;
        double d13 = f23;
        return new CamColor(f22, pow5, pow4, d11, d12, sqrt, f24, log * ((float) Math.cos(d13)), log * ((float) Math.sin(d13)));
    }

    private static CamColor e(float f10, float f11, float f12) {
        return f(f10, f11, f12, ViewingConditions.f2172k);
    }

    private static CamColor f(float f10, float f11, float f12, ViewingConditions viewingConditions) {
        float b10 = (4.0f / viewingConditions.b()) * ((float) Math.sqrt(f10 / 100.0d)) * (viewingConditions.a() + 4.0f) * viewingConditions.d();
        float d10 = f11 * viewingConditions.d();
        float sqrt = ((float) Math.sqrt(((f11 / ((float) Math.sqrt(r4))) * viewingConditions.b()) / (viewingConditions.a() + 4.0f))) * 50.0f;
        float f13 = (1.7f * f10) / ((0.007f * f10) + 1.0f);
        float log = ((float) Math.log((d10 * 0.0228d) + 1.0d)) * 43.85965f;
        double d11 = (3.1415927f * f12) / 180.0f;
        return new CamColor(f12, f11, f10, b10, d10, sqrt, f13, log * ((float) Math.cos(d11)), log * ((float) Math.sin(d11)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int m(float f10, float f11, float f12) {
        return n(f10, f11, f12, ViewingConditions.f2172k);
    }

    static int n(float f10, float f11, float f12, ViewingConditions viewingConditions) {
        if (f11 >= 1.0d && Math.round(f12) > UserProfileInfo.Constant.NA_LAT_LON && Math.round(f12) < 100.0d) {
            float min = f10 < 0.0f ? 0.0f : Math.min(360.0f, f10);
            CamColor camColor = null;
            boolean z10 = true;
            float f13 = 0.0f;
            float f14 = f11;
            while (Math.abs(f13 - f11) >= 0.4f) {
                CamColor b10 = b(min, f14, f12);
                if (z10) {
                    if (b10 != null) {
                        return b10.o(viewingConditions);
                    }
                    z10 = false;
                } else if (b10 == null) {
                    f11 = f14;
                } else {
                    f13 = f14;
                    camColor = b10;
                }
                f14 = ((f11 - f13) / 2.0f) + f13;
            }
            if (camColor == null) {
                return CamUtils.a(f12);
            }
            return camColor.o(viewingConditions);
        }
        return CamUtils.a(f12);
    }

    float a(CamColor camColor) {
        float l10 = l() - camColor.l();
        float g6 = g() - camColor.g();
        float h10 = h() - camColor.h();
        return (float) (Math.pow(Math.sqrt((l10 * l10) + (g6 * g6) + (h10 * h10)), 0.63d) * 1.41d);
    }

    float g() {
        return this.f2142h;
    }

    float h() {
        return this.f2143i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float i() {
        return this.f2136b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float j() {
        return this.f2135a;
    }

    float k() {
        return this.f2137c;
    }

    float l() {
        return this.f2141g;
    }

    int o(ViewingConditions viewingConditions) {
        float pow = (float) Math.pow(((((double) i()) == UserProfileInfo.Constant.NA_LAT_LON || ((double) k()) == UserProfileInfo.Constant.NA_LAT_LON) ? 0.0f : i() / ((float) Math.sqrt(k() / 100.0d))) / Math.pow(1.64d - Math.pow(0.29d, viewingConditions.e()), 0.73d), 1.1111111111111112d);
        double j10 = (j() * 3.1415927f) / 180.0f;
        float cos = ((float) (Math.cos(2.0d + j10) + 3.8d)) * 0.25f;
        float a10 = viewingConditions.a() * ((float) Math.pow(k() / 100.0d, (1.0d / viewingConditions.b()) / viewingConditions.j()));
        float g6 = cos * 3846.1538f * viewingConditions.g() * viewingConditions.h();
        float f10 = a10 / viewingConditions.f();
        float sin = (float) Math.sin(j10);
        float cos2 = (float) Math.cos(j10);
        float f11 = (((0.305f + f10) * 23.0f) * pow) / (((g6 * 23.0f) + ((11.0f * pow) * cos2)) + ((pow * 108.0f) * sin));
        float f12 = cos2 * f11;
        float f13 = f11 * sin;
        float f14 = f10 * 460.0f;
        float f15 = (((451.0f * f12) + f14) + (288.0f * f13)) / 1403.0f;
        float f16 = ((f14 - (891.0f * f12)) - (261.0f * f13)) / 1403.0f;
        float signum = Math.signum(f15) * (100.0f / viewingConditions.c()) * ((float) Math.pow((float) Math.max(UserProfileInfo.Constant.NA_LAT_LON, (Math.abs(f15) * 27.13d) / (400.0d - Math.abs(f15))), 2.380952380952381d));
        float signum2 = Math.signum(f16) * (100.0f / viewingConditions.c()) * ((float) Math.pow((float) Math.max(UserProfileInfo.Constant.NA_LAT_LON, (Math.abs(f16) * 27.13d) / (400.0d - Math.abs(f16))), 2.380952380952381d));
        float signum3 = Math.signum(((f14 - (f12 * 220.0f)) - (f13 * 6300.0f)) / 1403.0f) * (100.0f / viewingConditions.c()) * ((float) Math.pow((float) Math.max(UserProfileInfo.Constant.NA_LAT_LON, (Math.abs(r6) * 27.13d) / (400.0d - Math.abs(r6))), 2.380952380952381d));
        float f17 = signum / viewingConditions.i()[0];
        float f18 = signum2 / viewingConditions.i()[1];
        float f19 = signum3 / viewingConditions.i()[2];
        float[][] fArr = CamUtils.f2145b;
        return ColorUtils.d((fArr[0][0] * f17) + (fArr[0][1] * f18) + (fArr[0][2] * f19), (fArr[1][0] * f17) + (fArr[1][1] * f18) + (fArr[1][2] * f19), (f17 * fArr[2][0]) + (f18 * fArr[2][1]) + (f19 * fArr[2][2]));
    }

    int p() {
        return o(ViewingConditions.f2172k);
    }
}
