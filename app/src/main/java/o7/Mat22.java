package o7;

/* compiled from: Mat22.java */
/* renamed from: o7.c, reason: use source file name */
/* loaded from: classes.dex */
public class Mat22 {

    /* renamed from: a, reason: collision with root package name */
    public final Vector f16268a = new Vector();

    /* renamed from: b, reason: collision with root package name */
    public final Vector f16269b = new Vector();

    public static final void b(Mat22 mat22, Vector vector, Vector vector2) {
        Vector vector3 = mat22.f16268a;
        float f10 = vector3.f16270a * vector.f16270a;
        Vector vector4 = mat22.f16269b;
        float f11 = vector4.f16270a;
        float f12 = vector.f16271b;
        vector2.f16270a = f10 + (f11 * f12);
        vector2.f16271b = (vector3.f16271b * vector.f16270a) + (vector4.f16271b * f12);
    }

    public final Mat22 a() {
        Vector vector = this.f16268a;
        float f10 = vector.f16270a;
        Vector vector2 = this.f16269b;
        float f11 = vector2.f16270a;
        float f12 = vector.f16271b;
        float f13 = vector2.f16271b;
        float f14 = (f10 * f13) - (f11 * f12);
        if (f14 != 0.0f) {
            f14 = 1.0f / f14;
        }
        vector.f16270a = f13 * f14;
        float f15 = -f14;
        vector2.f16270a = f11 * f15;
        vector.f16271b = f15 * f12;
        vector2.f16271b = f14 * f10;
        return this;
    }
}
