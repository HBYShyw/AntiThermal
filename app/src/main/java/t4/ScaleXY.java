package t4;

/* compiled from: ScaleXY.java */
/* renamed from: t4.d, reason: use source file name */
/* loaded from: classes.dex */
public class ScaleXY {

    /* renamed from: a, reason: collision with root package name */
    private float f18585a;

    /* renamed from: b, reason: collision with root package name */
    private float f18586b;

    public ScaleXY(float f10, float f11) {
        this.f18585a = f10;
        this.f18586b = f11;
    }

    public boolean a(float f10, float f11) {
        return this.f18585a == f10 && this.f18586b == f11;
    }

    public float b() {
        return this.f18585a;
    }

    public float c() {
        return this.f18586b;
    }

    public void d(float f10, float f11) {
        this.f18585a = f10;
        this.f18586b = f11;
    }

    public String toString() {
        return b() + "x" + c();
    }

    public ScaleXY() {
        this(1.0f, 1.0f);
    }
}
