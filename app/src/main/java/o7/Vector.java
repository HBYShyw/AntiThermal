package o7;

/* compiled from: Vector.java */
/* renamed from: o7.e, reason: use source file name */
/* loaded from: classes.dex */
public class Vector {

    /* renamed from: a, reason: collision with root package name */
    public float f16270a;

    /* renamed from: b, reason: collision with root package name */
    public float f16271b;

    public Vector() {
        this(0.0f, 0.0f);
    }

    public final Vector a(Vector vector) {
        this.f16270a += vector.f16270a;
        this.f16271b += vector.f16271b;
        return this;
    }

    public final Vector b(float f10) {
        this.f16270a *= f10;
        this.f16271b *= f10;
        return this;
    }

    public final Vector c() {
        this.f16270a = -this.f16270a;
        this.f16271b = -this.f16271b;
        return this;
    }

    public final Vector d(float f10, float f11) {
        this.f16270a = f10;
        this.f16271b = f11;
        return this;
    }

    public final Vector e(Vector vector) {
        this.f16270a = vector.f16270a;
        this.f16271b = vector.f16271b;
        return this;
    }

    public final void f() {
        this.f16270a = 0.0f;
        this.f16271b = 0.0f;
    }

    public final Vector g(Vector vector) {
        this.f16270a -= vector.f16270a;
        this.f16271b -= vector.f16271b;
        return this;
    }

    public final String toString() {
        return "(" + this.f16270a + "," + this.f16271b + ")";
    }

    public Vector(float f10, float f11) {
        this.f16270a = f10;
        this.f16271b = f11;
    }
}
