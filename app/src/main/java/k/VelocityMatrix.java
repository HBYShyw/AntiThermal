package k;

import androidx.constraintlayout.motion.widget.KeyCycleOscillator;
import androidx.constraintlayout.motion.widget.SplineSet;

/* compiled from: VelocityMatrix.java */
/* renamed from: k.h, reason: use source file name */
/* loaded from: classes.dex */
public class VelocityMatrix {

    /* renamed from: a, reason: collision with root package name */
    float f13993a;

    /* renamed from: b, reason: collision with root package name */
    float f13994b;

    /* renamed from: c, reason: collision with root package name */
    float f13995c;

    /* renamed from: d, reason: collision with root package name */
    float f13996d;

    /* renamed from: e, reason: collision with root package name */
    float f13997e;

    /* renamed from: f, reason: collision with root package name */
    float f13998f;

    public void a(float f10, float f11, int i10, int i11, float[] fArr) {
        float f12 = fArr[0];
        float f13 = fArr[1];
        float f14 = (f11 - 0.5f) * 2.0f;
        float f15 = f12 + this.f13995c;
        float f16 = f13 + this.f13996d;
        float f17 = f15 + (this.f13993a * (f10 - 0.5f) * 2.0f);
        float f18 = f16 + (this.f13994b * f14);
        float radians = (float) Math.toRadians(this.f13998f);
        float radians2 = (float) Math.toRadians(this.f13997e);
        double d10 = radians;
        double d11 = i11 * f14;
        float sin = f17 + (((float) ((((-i10) * r7) * Math.sin(d10)) - (Math.cos(d10) * d11))) * radians2);
        float cos = f18 + (radians2 * ((float) (((i10 * r7) * Math.cos(d10)) - (d11 * Math.sin(d10)))));
        fArr[0] = sin;
        fArr[1] = cos;
    }

    public void b() {
        this.f13997e = 0.0f;
        this.f13996d = 0.0f;
        this.f13995c = 0.0f;
        this.f13994b = 0.0f;
        this.f13993a = 0.0f;
    }

    public void c(KeyCycleOscillator keyCycleOscillator, float f10) {
        if (keyCycleOscillator != null) {
            this.f13997e = keyCycleOscillator.b(f10);
        }
    }

    public void d(SplineSet splineSet, float f10) {
        if (splineSet != null) {
            this.f13997e = splineSet.b(f10);
            this.f13998f = splineSet.a(f10);
        }
    }

    public void e(KeyCycleOscillator keyCycleOscillator, KeyCycleOscillator keyCycleOscillator2, float f10) {
        if (keyCycleOscillator == null && keyCycleOscillator2 == null) {
            return;
        }
        if (keyCycleOscillator == null) {
            this.f13993a = keyCycleOscillator.b(f10);
        }
        if (keyCycleOscillator2 == null) {
            this.f13994b = keyCycleOscillator2.b(f10);
        }
    }

    public void f(SplineSet splineSet, SplineSet splineSet2, float f10) {
        if (splineSet != null) {
            this.f13993a = splineSet.b(f10);
        }
        if (splineSet2 != null) {
            this.f13994b = splineSet2.b(f10);
        }
    }

    public void g(KeyCycleOscillator keyCycleOscillator, KeyCycleOscillator keyCycleOscillator2, float f10) {
        if (keyCycleOscillator != null) {
            this.f13995c = keyCycleOscillator.b(f10);
        }
        if (keyCycleOscillator2 != null) {
            this.f13996d = keyCycleOscillator2.b(f10);
        }
    }

    public void h(SplineSet splineSet, SplineSet splineSet2, float f10) {
        if (splineSet != null) {
            this.f13995c = splineSet.b(f10);
        }
        if (splineSet2 != null) {
            this.f13996d = splineSet2.b(f10);
        }
    }
}
