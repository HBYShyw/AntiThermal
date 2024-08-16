package l4;

import android.graphics.PointF;

/* compiled from: CubicCurveData.java */
/* renamed from: l4.a, reason: use source file name */
/* loaded from: classes.dex */
public class CubicCurveData {

    /* renamed from: a, reason: collision with root package name */
    private final PointF f14592a;

    /* renamed from: b, reason: collision with root package name */
    private final PointF f14593b;

    /* renamed from: c, reason: collision with root package name */
    private final PointF f14594c;

    public CubicCurveData() {
        this.f14592a = new PointF();
        this.f14593b = new PointF();
        this.f14594c = new PointF();
    }

    public PointF a() {
        return this.f14592a;
    }

    public PointF b() {
        return this.f14593b;
    }

    public PointF c() {
        return this.f14594c;
    }

    public void d(float f10, float f11) {
        this.f14592a.set(f10, f11);
    }

    public void e(float f10, float f11) {
        this.f14593b.set(f10, f11);
    }

    public void f(float f10, float f11) {
        this.f14594c.set(f10, f11);
    }

    public CubicCurveData(PointF pointF, PointF pointF2, PointF pointF3) {
        this.f14592a = pointF;
        this.f14593b = pointF2;
        this.f14594c = pointF3;
    }
}
