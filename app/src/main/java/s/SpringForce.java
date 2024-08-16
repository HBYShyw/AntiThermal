package s;

import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import s.DynamicAnimation;

/* compiled from: SpringForce.java */
/* renamed from: s.g, reason: use source file name */
/* loaded from: classes.dex */
public final class SpringForce {

    /* renamed from: a, reason: collision with root package name */
    double f17899a;

    /* renamed from: b, reason: collision with root package name */
    double f17900b;

    /* renamed from: c, reason: collision with root package name */
    private boolean f17901c;

    /* renamed from: d, reason: collision with root package name */
    private double f17902d;

    /* renamed from: e, reason: collision with root package name */
    private double f17903e;

    /* renamed from: f, reason: collision with root package name */
    private double f17904f;

    /* renamed from: g, reason: collision with root package name */
    private double f17905g;

    /* renamed from: h, reason: collision with root package name */
    private double f17906h;

    /* renamed from: i, reason: collision with root package name */
    private double f17907i;

    /* renamed from: j, reason: collision with root package name */
    private final DynamicAnimation.p f17908j;

    public SpringForce() {
        this.f17899a = Math.sqrt(1500.0d);
        this.f17900b = 0.5d;
        this.f17901c = false;
        this.f17907i = Double.MAX_VALUE;
        this.f17908j = new DynamicAnimation.p();
    }

    private void b() {
        if (this.f17901c) {
            return;
        }
        if (this.f17907i != Double.MAX_VALUE) {
            double d10 = this.f17900b;
            if (d10 > 1.0d) {
                double d11 = this.f17899a;
                this.f17904f = ((-d10) * d11) + (d11 * Math.sqrt((d10 * d10) - 1.0d));
                double d12 = this.f17900b;
                double d13 = this.f17899a;
                this.f17905g = ((-d12) * d13) - (d13 * Math.sqrt((d12 * d12) - 1.0d));
            } else if (d10 >= UserProfileInfo.Constant.NA_LAT_LON && d10 < 1.0d) {
                this.f17906h = this.f17899a * Math.sqrt(1.0d - (d10 * d10));
            }
            this.f17901c = true;
            return;
        }
        throw new IllegalStateException("Error: Final position of the spring must be set before the animation starts");
    }

    public float a() {
        return (float) this.f17907i;
    }

    public boolean c(float f10, float f11) {
        return ((double) Math.abs(f11)) < this.f17903e && ((double) Math.abs(f10 - a())) < this.f17902d;
    }

    public SpringForce d(float f10) {
        if (f10 >= 0.0f) {
            this.f17900b = f10;
            this.f17901c = false;
            return this;
        }
        throw new IllegalArgumentException("Damping ratio must be non-negative");
    }

    public SpringForce e(float f10) {
        this.f17907i = f10;
        return this;
    }

    public SpringForce f(float f10) {
        if (f10 > 0.0f) {
            this.f17899a = Math.sqrt(f10);
            this.f17901c = false;
            return this;
        }
        throw new IllegalArgumentException("Spring stiffness constant must be positive.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g(double d10) {
        double abs = Math.abs(d10);
        this.f17902d = abs;
        this.f17903e = abs * 62.5d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DynamicAnimation.p h(double d10, double d11, long j10) {
        double cos;
        double d12;
        b();
        double d13 = j10 / 1000.0d;
        double d14 = d10 - this.f17907i;
        double d15 = this.f17900b;
        if (d15 > 1.0d) {
            double d16 = this.f17905g;
            double d17 = this.f17904f;
            double d18 = d14 - (((d16 * d14) - d11) / (d16 - d17));
            double d19 = ((d14 * d16) - d11) / (d16 - d17);
            d12 = (Math.pow(2.718281828459045d, d16 * d13) * d18) + (Math.pow(2.718281828459045d, this.f17904f * d13) * d19);
            double d20 = this.f17905g;
            double pow = d18 * d20 * Math.pow(2.718281828459045d, d20 * d13);
            double d21 = this.f17904f;
            cos = pow + (d19 * d21 * Math.pow(2.718281828459045d, d21 * d13));
        } else if (d15 == 1.0d) {
            double d22 = this.f17899a;
            double d23 = d11 + (d22 * d14);
            double d24 = d14 + (d23 * d13);
            d12 = Math.pow(2.718281828459045d, (-d22) * d13) * d24;
            double pow2 = d24 * Math.pow(2.718281828459045d, (-this.f17899a) * d13);
            double d25 = this.f17899a;
            cos = (d23 * Math.pow(2.718281828459045d, (-d25) * d13)) + (pow2 * (-d25));
        } else {
            double d26 = 1.0d / this.f17906h;
            double d27 = this.f17899a;
            double d28 = d26 * ((d15 * d27 * d14) + d11);
            double pow3 = Math.pow(2.718281828459045d, (-d15) * d27 * d13) * ((Math.cos(this.f17906h * d13) * d14) + (Math.sin(this.f17906h * d13) * d28));
            double d29 = this.f17899a;
            double d30 = this.f17900b;
            double d31 = (-d29) * pow3 * d30;
            double pow4 = Math.pow(2.718281828459045d, (-d30) * d29 * d13);
            double d32 = this.f17906h;
            double sin = (-d32) * d14 * Math.sin(d32 * d13);
            double d33 = this.f17906h;
            cos = d31 + (pow4 * (sin + (d28 * d33 * Math.cos(d33 * d13))));
            d12 = pow3;
        }
        DynamicAnimation.p pVar = this.f17908j;
        pVar.f17895a = (float) (d12 + this.f17907i);
        pVar.f17896b = (float) cos;
        return pVar;
    }

    public SpringForce(float f10) {
        this.f17899a = Math.sqrt(1500.0d);
        this.f17900b = 0.5d;
        this.f17901c = false;
        this.f17907i = Double.MAX_VALUE;
        this.f17908j = new DynamicAnimation.p();
        this.f17907i = f10;
    }
}
