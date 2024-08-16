package k;

import androidx.constraintlayout.motion.widget.MotionInterpolator;

/* compiled from: StopLogic.java */
/* renamed from: k.g, reason: use source file name */
/* loaded from: classes.dex */
public class StopLogic extends MotionInterpolator {

    /* renamed from: a, reason: collision with root package name */
    private float f13979a;

    /* renamed from: b, reason: collision with root package name */
    private float f13980b;

    /* renamed from: c, reason: collision with root package name */
    private float f13981c;

    /* renamed from: d, reason: collision with root package name */
    private float f13982d;

    /* renamed from: e, reason: collision with root package name */
    private float f13983e;

    /* renamed from: f, reason: collision with root package name */
    private float f13984f;

    /* renamed from: g, reason: collision with root package name */
    private float f13985g;

    /* renamed from: h, reason: collision with root package name */
    private float f13986h;

    /* renamed from: i, reason: collision with root package name */
    private float f13987i;

    /* renamed from: j, reason: collision with root package name */
    private int f13988j;

    /* renamed from: k, reason: collision with root package name */
    private String f13989k;

    /* renamed from: l, reason: collision with root package name */
    private boolean f13990l = false;

    /* renamed from: m, reason: collision with root package name */
    private float f13991m;

    /* renamed from: n, reason: collision with root package name */
    private float f13992n;

    private float b(float f10) {
        float f11 = this.f13982d;
        if (f10 <= f11) {
            float f12 = this.f13979a;
            return (f12 * f10) + ((((this.f13980b - f12) * f10) * f10) / (f11 * 2.0f));
        }
        int i10 = this.f13988j;
        if (i10 == 1) {
            return this.f13985g;
        }
        float f13 = f10 - f11;
        float f14 = this.f13983e;
        if (f13 < f14) {
            float f15 = this.f13985g;
            float f16 = this.f13980b;
            return f15 + (f16 * f13) + ((((this.f13981c - f16) * f13) * f13) / (f14 * 2.0f));
        }
        if (i10 == 2) {
            return this.f13986h;
        }
        float f17 = f13 - f14;
        float f18 = this.f13984f;
        if (f17 < f18) {
            float f19 = this.f13986h;
            float f20 = this.f13981c;
            return (f19 + (f20 * f17)) - (((f20 * f17) * f17) / (f18 * 2.0f));
        }
        return this.f13987i;
    }

    private void e(float f10, float f11, float f12, float f13, float f14) {
        if (f10 == 0.0f) {
            f10 = 1.0E-4f;
        }
        this.f13979a = f10;
        float f15 = f10 / f12;
        float f16 = (f15 * f10) / 2.0f;
        if (f10 < 0.0f) {
            float sqrt = (float) Math.sqrt((f11 - ((((-f10) / f12) * f10) / 2.0f)) * f12);
            if (sqrt < f13) {
                this.f13989k = "backward accelerate, decelerate";
                this.f13988j = 2;
                this.f13979a = f10;
                this.f13980b = sqrt;
                this.f13981c = 0.0f;
                float f17 = (sqrt - f10) / f12;
                this.f13982d = f17;
                this.f13983e = sqrt / f12;
                this.f13985g = ((f10 + sqrt) * f17) / 2.0f;
                this.f13986h = f11;
                this.f13987i = f11;
                return;
            }
            this.f13989k = "backward accelerate cruse decelerate";
            this.f13988j = 3;
            this.f13979a = f10;
            this.f13980b = f13;
            this.f13981c = f13;
            float f18 = (f13 - f10) / f12;
            this.f13982d = f18;
            float f19 = f13 / f12;
            this.f13984f = f19;
            float f20 = ((f10 + f13) * f18) / 2.0f;
            float f21 = (f19 * f13) / 2.0f;
            this.f13983e = ((f11 - f20) - f21) / f13;
            this.f13985g = f20;
            this.f13986h = f11 - f21;
            this.f13987i = f11;
            return;
        }
        if (f16 >= f11) {
            this.f13989k = "hard stop";
            this.f13988j = 1;
            this.f13979a = f10;
            this.f13980b = 0.0f;
            this.f13985g = f11;
            this.f13982d = (2.0f * f11) / f10;
            return;
        }
        float f22 = f11 - f16;
        float f23 = f22 / f10;
        if (f23 + f15 < f14) {
            this.f13989k = "cruse decelerate";
            this.f13988j = 2;
            this.f13979a = f10;
            this.f13980b = f10;
            this.f13981c = 0.0f;
            this.f13985g = f22;
            this.f13986h = f11;
            this.f13982d = f23;
            this.f13983e = f15;
            return;
        }
        float sqrt2 = (float) Math.sqrt((f12 * f11) + ((f10 * f10) / 2.0f));
        float f24 = (sqrt2 - f10) / f12;
        this.f13982d = f24;
        float f25 = sqrt2 / f12;
        this.f13983e = f25;
        if (sqrt2 < f13) {
            this.f13989k = "accelerate decelerate";
            this.f13988j = 2;
            this.f13979a = f10;
            this.f13980b = sqrt2;
            this.f13981c = 0.0f;
            this.f13982d = f24;
            this.f13983e = f25;
            this.f13985g = ((f10 + sqrt2) * f24) / 2.0f;
            this.f13986h = f11;
            return;
        }
        this.f13989k = "accelerate cruse decelerate";
        this.f13988j = 3;
        this.f13979a = f10;
        this.f13980b = f13;
        this.f13981c = f13;
        float f26 = (f13 - f10) / f12;
        this.f13982d = f26;
        float f27 = f13 / f12;
        this.f13984f = f27;
        float f28 = ((f10 + f13) * f26) / 2.0f;
        float f29 = (f27 * f13) / 2.0f;
        this.f13983e = ((f11 - f28) - f29) / f13;
        this.f13985g = f28;
        this.f13986h = f11 - f29;
        this.f13987i = f11;
    }

    @Override // androidx.constraintlayout.motion.widget.MotionInterpolator
    public float a() {
        return this.f13990l ? -d(this.f13992n) : d(this.f13992n);
    }

    public void c(float f10, float f11, float f12, float f13, float f14, float f15) {
        this.f13991m = f10;
        boolean z10 = f10 > f11;
        this.f13990l = z10;
        if (z10) {
            e(-f12, f10 - f11, f14, f15, f13);
        } else {
            e(f12, f11 - f10, f14, f15, f13);
        }
    }

    public float d(float f10) {
        float f11;
        float f12;
        float f13 = this.f13982d;
        if (f10 <= f13) {
            f11 = this.f13979a;
            f12 = this.f13980b;
        } else {
            int i10 = this.f13988j;
            if (i10 == 1) {
                return 0.0f;
            }
            f10 -= f13;
            f13 = this.f13983e;
            if (f10 >= f13) {
                if (i10 == 2) {
                    return this.f13986h;
                }
                float f14 = f10 - f13;
                float f15 = this.f13984f;
                if (f14 < f15) {
                    float f16 = this.f13981c;
                    return f16 - ((f14 * f16) / f15);
                }
                return this.f13987i;
            }
            f11 = this.f13980b;
            f12 = this.f13981c;
        }
        return f11 + (((f12 - f11) * f10) / f13);
    }

    @Override // android.animation.TimeInterpolator
    public float getInterpolation(float f10) {
        float b10 = b(f10);
        this.f13992n = f10;
        boolean z10 = this.f13990l;
        float f11 = this.f13991m;
        return z10 ? f11 - b10 : f11 + b10;
    }
}
