package com.google.android.material.bottomappbar;

import c4.EdgeTreatment;
import c4.o;

/* compiled from: BottomAppBarTopEdgeTreatment.java */
/* renamed from: com.google.android.material.bottomappbar.a, reason: use source file name */
/* loaded from: classes.dex */
public class BottomAppBarTopEdgeTreatment extends EdgeTreatment implements Cloneable {

    /* renamed from: e, reason: collision with root package name */
    private float f8385e;

    /* renamed from: f, reason: collision with root package name */
    private float f8386f;

    /* renamed from: g, reason: collision with root package name */
    private float f8387g;

    /* renamed from: h, reason: collision with root package name */
    private float f8388h;

    /* renamed from: i, reason: collision with root package name */
    private float f8389i;

    /* renamed from: j, reason: collision with root package name */
    private float f8390j = -1.0f;

    public BottomAppBarTopEdgeTreatment(float f10, float f11, float f12) {
        this.f8386f = f10;
        this.f8385e = f11;
        i(f12);
        this.f8389i = 0.0f;
    }

    @Override // c4.EdgeTreatment
    public void b(float f10, float f11, float f12, o oVar) {
        float f13;
        float f14;
        float f15 = this.f8387g;
        if (f15 == 0.0f) {
            oVar.m(f10, 0.0f);
            return;
        }
        float f16 = ((this.f8386f * 2.0f) + f15) / 2.0f;
        float f17 = f12 * this.f8385e;
        float f18 = f11 + this.f8389i;
        float f19 = (this.f8388h * f12) + ((1.0f - f12) * f16);
        if (f19 / f16 >= 1.0f) {
            oVar.m(f10, 0.0f);
            return;
        }
        float f20 = this.f8390j;
        float f21 = f20 * f12;
        boolean z10 = f20 == -1.0f || Math.abs((f20 * 2.0f) - f15) < 0.1f;
        if (z10) {
            f13 = f19;
            f14 = 0.0f;
        } else {
            f14 = 1.75f;
            f13 = 0.0f;
        }
        float f22 = f16 + f17;
        float f23 = f13 + f17;
        float sqrt = (float) Math.sqrt((f22 * f22) - (f23 * f23));
        float f24 = f18 - sqrt;
        float f25 = f18 + sqrt;
        float degrees = (float) Math.toDegrees(Math.atan(sqrt / f23));
        float f26 = (90.0f - degrees) + f14;
        oVar.m(f24, 0.0f);
        float f27 = f17 * 2.0f;
        oVar.a(f24 - f17, 0.0f, f24 + f17, f27, 270.0f, degrees);
        if (z10) {
            oVar.a(f18 - f16, (-f16) - f13, f18 + f16, f16 - f13, 180.0f - f26, (f26 * 2.0f) - 180.0f);
        } else {
            float f28 = this.f8386f;
            float f29 = f21 * 2.0f;
            float f30 = f18 - f16;
            oVar.a(f30, -(f21 + f28), f30 + f28 + f29, f28 + f21, 180.0f - f26, ((f26 * 2.0f) - 180.0f) / 2.0f);
            float f31 = f18 + f16;
            float f32 = this.f8386f;
            oVar.m(f31 - ((f32 / 2.0f) + f21), f32 + f21);
            float f33 = this.f8386f;
            oVar.a(f31 - (f29 + f33), -(f21 + f33), f31, f33 + f21, 90.0f, f26 - 90.0f);
        }
        oVar.a(f25 - f17, 0.0f, f25 + f17, f27, 270.0f - degrees, degrees);
        oVar.m(f10, 0.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float c() {
        return this.f8388h;
    }

    public float d() {
        return this.f8390j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float e() {
        return this.f8386f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float f() {
        return this.f8385e;
    }

    public float g() {
        return this.f8387g;
    }

    public float h() {
        return this.f8389i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void i(float f10) {
        if (f10 >= 0.0f) {
            this.f8388h = f10;
            return;
        }
        throw new IllegalArgumentException("cradleVerticalOffset must be positive.");
    }

    public void j(float f10) {
        this.f8390j = f10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k(float f10) {
        this.f8386f = f10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void l(float f10) {
        this.f8385e = f10;
    }

    public void n(float f10) {
        this.f8387g = f10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o(float f10) {
        this.f8389i = f10;
    }
}
