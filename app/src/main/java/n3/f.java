package n3;

import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

/* compiled from: Spring.java */
/* loaded from: classes.dex */
public class f {

    /* renamed from: o, reason: collision with root package name */
    private static int f15729o;

    /* renamed from: a, reason: collision with root package name */
    private SpringConfig f15730a;

    /* renamed from: b, reason: collision with root package name */
    private boolean f15731b;

    /* renamed from: c, reason: collision with root package name */
    private final String f15732c;

    /* renamed from: d, reason: collision with root package name */
    private final b f15733d;

    /* renamed from: e, reason: collision with root package name */
    private final b f15734e;

    /* renamed from: f, reason: collision with root package name */
    private final b f15735f;

    /* renamed from: g, reason: collision with root package name */
    private double f15736g;

    /* renamed from: h, reason: collision with root package name */
    private double f15737h;

    /* renamed from: i, reason: collision with root package name */
    private boolean f15738i = true;

    /* renamed from: j, reason: collision with root package name */
    private double f15739j = 0.005d;

    /* renamed from: k, reason: collision with root package name */
    private double f15740k = 0.005d;

    /* renamed from: l, reason: collision with root package name */
    private CopyOnWriteArraySet<SpringListener> f15741l = new CopyOnWriteArraySet<>();

    /* renamed from: m, reason: collision with root package name */
    private double f15742m = UserProfileInfo.Constant.NA_LAT_LON;

    /* renamed from: n, reason: collision with root package name */
    private final BaseSpringSystem f15743n;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: Spring.java */
    /* loaded from: classes.dex */
    public static class b {

        /* renamed from: a, reason: collision with root package name */
        double f15744a;

        /* renamed from: b, reason: collision with root package name */
        double f15745b;

        private b() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public f(BaseSpringSystem baseSpringSystem) {
        this.f15733d = new b();
        this.f15734e = new b();
        this.f15735f = new b();
        if (baseSpringSystem != null) {
            this.f15743n = baseSpringSystem;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("spring:");
            int i10 = f15729o;
            f15729o = i10 + 1;
            sb2.append(i10);
            this.f15732c = sb2.toString();
            p(SpringConfig.f15746c);
            return;
        }
        throw new IllegalArgumentException("Spring cannot be created outside of a BaseSpringSystem");
    }

    private double d(b bVar) {
        return Math.abs(this.f15737h - bVar.f15744a);
    }

    private void h(double d10) {
        b bVar = this.f15733d;
        double d11 = bVar.f15744a * d10;
        b bVar2 = this.f15734e;
        double d12 = 1.0d - d10;
        bVar.f15744a = d11 + (bVar2.f15744a * d12);
        bVar.f15745b = (bVar.f15745b * d10) + (bVar2.f15745b * d12);
    }

    public f a(SpringListener springListener) {
        if (springListener != null) {
            this.f15741l.add(springListener);
            return this;
        }
        throw new IllegalArgumentException("newListener is required");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(double d10) {
        double d11;
        boolean z10;
        boolean z11;
        boolean i10 = i();
        if (i10 && this.f15738i) {
            return;
        }
        this.f15742m += d10 <= 0.064d ? d10 : 0.064d;
        SpringConfig springConfig = this.f15730a;
        double d12 = springConfig.f15748b;
        double d13 = springConfig.f15747a;
        b bVar = this.f15733d;
        double d14 = bVar.f15744a;
        double d15 = bVar.f15745b;
        b bVar2 = this.f15735f;
        double d16 = bVar2.f15744a;
        double d17 = bVar2.f15745b;
        while (true) {
            d11 = this.f15742m;
            if (d11 < 0.001d) {
                break;
            }
            double d18 = d11 - 0.001d;
            this.f15742m = d18;
            if (d18 < 0.001d) {
                b bVar3 = this.f15734e;
                bVar3.f15744a = d14;
                bVar3.f15745b = d15;
            }
            double d19 = this.f15737h;
            double d20 = ((d19 - d16) * d12) - (d13 * d15);
            double d21 = d15 + (d20 * 0.001d * 0.5d);
            double d22 = ((d19 - (((d15 * 0.001d) * 0.5d) + d14)) * d12) - (d13 * d21);
            double d23 = d15 + (d22 * 0.001d * 0.5d);
            double d24 = ((d19 - (d14 + ((d21 * 0.001d) * 0.5d))) * d12) - (d13 * d23);
            double d25 = d14 + (d23 * 0.001d);
            double d26 = d15 + (d24 * 0.001d);
            d14 += (d15 + ((d21 + d23) * 2.0d) + d26) * 0.16666666666666666d * 0.001d;
            d15 += (d20 + ((d22 + d24) * 2.0d) + (((d19 - d25) * d12) - (d13 * d26))) * 0.16666666666666666d * 0.001d;
            d16 = d25;
            d17 = d26;
        }
        b bVar4 = this.f15735f;
        bVar4.f15744a = d16;
        bVar4.f15745b = d17;
        b bVar5 = this.f15733d;
        bVar5.f15744a = d14;
        bVar5.f15745b = d15;
        if (d11 > UserProfileInfo.Constant.NA_LAT_LON) {
            h(d11 / 0.001d);
        }
        boolean z12 = true;
        if (i() || (this.f15731b && j())) {
            if (d12 > UserProfileInfo.Constant.NA_LAT_LON) {
                double d27 = this.f15737h;
                this.f15736g = d27;
                this.f15733d.f15744a = d27;
            } else {
                double d28 = this.f15733d.f15744a;
                this.f15737h = d28;
                this.f15736g = d28;
            }
            q(UserProfileInfo.Constant.NA_LAT_LON);
            z10 = true;
        } else {
            z10 = i10;
        }
        if (this.f15738i) {
            this.f15738i = false;
            z11 = true;
        } else {
            z11 = false;
        }
        if (z10) {
            this.f15738i = true;
        } else {
            z12 = false;
        }
        Iterator<SpringListener> it = this.f15741l.iterator();
        while (it.hasNext()) {
            SpringListener next = it.next();
            if (z11) {
                next.onSpringActivate(this);
            }
            next.onSpringUpdate(this);
            if (z12) {
                next.onSpringAtRest(this);
            }
        }
    }

    public double c() {
        return this.f15733d.f15744a;
    }

    public double e() {
        return this.f15737h;
    }

    public String f() {
        return this.f15732c;
    }

    public double g() {
        return this.f15733d.f15745b;
    }

    public boolean i() {
        return Math.abs(this.f15733d.f15745b) <= this.f15739j && (d(this.f15733d) <= this.f15740k || this.f15730a.f15748b == UserProfileInfo.Constant.NA_LAT_LON);
    }

    public boolean j() {
        return this.f15730a.f15748b > UserProfileInfo.Constant.NA_LAT_LON && ((this.f15736g < this.f15737h && c() > this.f15737h) || (this.f15736g > this.f15737h && c() < this.f15737h));
    }

    public f k() {
        this.f15741l.clear();
        return this;
    }

    public f l() {
        b bVar = this.f15733d;
        double d10 = bVar.f15744a;
        this.f15737h = d10;
        this.f15735f.f15744a = d10;
        bVar.f15745b = UserProfileInfo.Constant.NA_LAT_LON;
        return this;
    }

    public f m(double d10) {
        return n(d10, true);
    }

    public f n(double d10, boolean z10) {
        this.f15736g = d10;
        this.f15733d.f15744a = d10;
        this.f15743n.a(f());
        Iterator<SpringListener> it = this.f15741l.iterator();
        while (it.hasNext()) {
            it.next().onSpringUpdate(this);
        }
        if (z10) {
            l();
        }
        return this;
    }

    public f o(double d10) {
        if (this.f15737h == d10 && i()) {
            return this;
        }
        this.f15736g = c();
        this.f15737h = d10;
        this.f15743n.a(f());
        Iterator<SpringListener> it = this.f15741l.iterator();
        while (it.hasNext()) {
            it.next().onSpringEndStateChange(this);
        }
        return this;
    }

    public f p(SpringConfig springConfig) {
        if (springConfig != null) {
            this.f15730a = springConfig;
            return this;
        }
        throw new IllegalArgumentException("springConfig is required");
    }

    public f q(double d10) {
        b bVar = this.f15733d;
        if (d10 == bVar.f15745b) {
            return this;
        }
        bVar.f15745b = d10;
        this.f15743n.a(f());
        return this;
    }

    public boolean r() {
        return (i() && s()) ? false : true;
    }

    public boolean s() {
        return this.f15738i;
    }
}
