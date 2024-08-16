package b9;

import android.graphics.drawable.Drawable;
import java.io.Serializable;
import java.util.Comparator;

/* compiled from: PowerSipper.java */
/* renamed from: b9.c, reason: use source file name */
/* loaded from: classes2.dex */
public class PowerSipper implements Serializable, Comparator<PowerSipper> {

    /* renamed from: y, reason: collision with root package name */
    public static final Comparator<PowerSipper> f4599y = new a();

    /* renamed from: z, reason: collision with root package name */
    public static final Comparator<PowerSipper> f4600z = new b();

    /* renamed from: e, reason: collision with root package name */
    public String f4601e;

    /* renamed from: f, reason: collision with root package name */
    public String f4602f;

    /* renamed from: g, reason: collision with root package name */
    public String f4603g;

    /* renamed from: h, reason: collision with root package name */
    public String f4604h;

    /* renamed from: i, reason: collision with root package name */
    public int f4605i;

    /* renamed from: j, reason: collision with root package name */
    public int f4606j;

    /* renamed from: k, reason: collision with root package name */
    public long f4607k;

    /* renamed from: l, reason: collision with root package name */
    public long f4608l;

    /* renamed from: m, reason: collision with root package name */
    public long f4609m;

    /* renamed from: n, reason: collision with root package name */
    public long f4610n;

    /* renamed from: o, reason: collision with root package name */
    public long f4611o;

    /* renamed from: p, reason: collision with root package name */
    public long f4612p;

    /* renamed from: q, reason: collision with root package name */
    public double f4613q;

    /* renamed from: r, reason: collision with root package name */
    public Drawable f4614r;

    /* renamed from: s, reason: collision with root package name */
    public String f4615s;

    /* renamed from: t, reason: collision with root package name */
    public int f4616t;

    /* renamed from: v, reason: collision with root package name */
    public String f4618v;

    /* renamed from: w, reason: collision with root package name */
    public String f4619w;

    /* renamed from: u, reason: collision with root package name */
    public boolean f4617u = true;

    /* renamed from: x, reason: collision with root package name */
    public boolean f4620x = true;

    /* compiled from: PowerSipper.java */
    /* renamed from: b9.c$a */
    /* loaded from: classes2.dex */
    class a implements Comparator<PowerSipper> {
        a() {
        }

        @Override // java.util.Comparator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(PowerSipper powerSipper, PowerSipper powerSipper2) {
            if (Double.doubleToLongBits(powerSipper.f4613q) < Double.doubleToLongBits(powerSipper2.f4613q)) {
                return 1;
            }
            return Double.doubleToLongBits(powerSipper.f4613q) > Double.doubleToLongBits(powerSipper2.f4613q) ? -1 : 0;
        }
    }

    /* compiled from: PowerSipper.java */
    /* renamed from: b9.c$b */
    /* loaded from: classes2.dex */
    class b implements Comparator<PowerSipper> {
        b() {
        }

        @Override // java.util.Comparator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(PowerSipper powerSipper, PowerSipper powerSipper2) {
            long j10 = powerSipper.f4608l;
            long j11 = powerSipper2.f4608l;
            if (j10 < j11) {
                return 1;
            }
            return j10 > j11 ? -1 : 0;
        }
    }

    public PowerSipper(String str, String str2, String str3, long j10, long j11, long j12, long j13, long j14, long j15, double d10, String str4, int i10, int i11, int i12) {
        this.f4601e = str;
        this.f4602f = str2;
        this.f4603g = str3;
        this.f4607k = j10;
        this.f4608l = j11;
        this.f4609m = j12;
        this.f4610n = j13;
        this.f4611o = j14;
        this.f4612p = j15;
        this.f4613q = d10;
        this.f4604h = str4;
        this.f4605i = i10;
        this.f4606j = i11;
        this.f4616t = i12;
    }

    @Override // java.util.Comparator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(PowerSipper powerSipper, PowerSipper powerSipper2) {
        return Double.compare(powerSipper2.f4613q, powerSipper.f4613q);
    }

    public boolean b() {
        return this.f4617u;
    }

    public void c(boolean z10) {
        this.f4617u = z10;
    }

    public String toString() {
        return "" + this.f4601e + " " + this.f4602f + " " + this.f4603g + " " + this.f4607k + " " + this.f4608l + " " + this.f4609m + " " + this.f4610n + " " + this.f4611o + " " + this.f4612p + " " + this.f4613q + " " + this.f4604h + " " + this.f4605i + " " + this.f4606j;
    }

    public PowerSipper(String str, String str2, long j10, double d10, int i10) {
        this.f4601e = str;
        this.f4603g = str2;
        this.f4608l = j10;
        this.f4613q = d10;
        this.f4616t = i10;
    }

    public PowerSipper(PowerSipper powerSipper) {
        this.f4601e = powerSipper.f4601e;
        this.f4602f = powerSipper.f4602f;
        this.f4603g = powerSipper.f4603g;
        this.f4607k = powerSipper.f4607k;
        this.f4608l = powerSipper.f4608l;
        this.f4609m = powerSipper.f4609m;
        this.f4610n = powerSipper.f4610n;
        this.f4611o = powerSipper.f4611o;
        this.f4612p = powerSipper.f4612p;
        this.f4613q = powerSipper.f4613q;
        this.f4604h = powerSipper.f4604h;
        this.f4605i = powerSipper.f4605i;
        this.f4606j = powerSipper.f4606j;
        this.f4616t = powerSipper.f4616t;
    }

    public PowerSipper() {
    }
}
