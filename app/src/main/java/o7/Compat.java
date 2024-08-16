package o7;

/* compiled from: Compat.java */
/* renamed from: o7.a, reason: use source file name */
/* loaded from: classes.dex */
public class Compat {

    /* renamed from: a, reason: collision with root package name */
    public static float f16263a = 0.008333334f;

    /* renamed from: b, reason: collision with root package name */
    public static float f16264b = 0.1f;

    /* renamed from: c, reason: collision with root package name */
    public static float f16265c = 160.0f;

    public static float a(float f10) {
        return (d.c(f10) * 2.8600001f) + 2.2141f;
    }

    public static boolean b(float f10) {
        return f10 < f16264b;
    }

    public static float c(float f10) {
        return f10 * f16265c;
    }

    public static float d(float f10) {
        return f10 / f16265c;
    }

    public static void e(float f10) {
        f16265c = (f10 * 55.0f) + 0.5f;
        f16264b = d(0.1f);
    }

    public static void f(float f10) {
        f16263a = f10;
    }
}
