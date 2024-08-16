package o9;

import q9.AppStats;
import q9.WakeLock;

/* compiled from: HighPowerSipper.java */
/* renamed from: o9.g, reason: use source file name */
/* loaded from: classes2.dex */
public class HighPowerSipper {

    /* renamed from: a, reason: collision with root package name */
    public String f16309a;

    /* renamed from: b, reason: collision with root package name */
    public StringBuilder f16310b = new StringBuilder("");

    /* renamed from: c, reason: collision with root package name */
    public long f16311c = 0;

    /* renamed from: d, reason: collision with root package name */
    public long f16312d = 0;

    /* renamed from: e, reason: collision with root package name */
    public long f16313e = 0;

    /* renamed from: f, reason: collision with root package name */
    public long f16314f = 0;

    /* renamed from: g, reason: collision with root package name */
    public long f16315g = 0;

    /* renamed from: h, reason: collision with root package name */
    public boolean f16316h = false;

    /* renamed from: i, reason: collision with root package name */
    public long f16317i = 0;

    /* renamed from: j, reason: collision with root package name */
    public long f16318j = 0;

    /* renamed from: k, reason: collision with root package name */
    public long f16319k = 0;

    /* renamed from: l, reason: collision with root package name */
    public long f16320l = 0;

    /* renamed from: m, reason: collision with root package name */
    public long f16321m = 0;

    /* renamed from: n, reason: collision with root package name */
    public boolean f16322n = false;

    /* renamed from: o, reason: collision with root package name */
    public long f16323o = 0;

    /* renamed from: p, reason: collision with root package name */
    public long f16324p = 0;

    /* renamed from: q, reason: collision with root package name */
    public long f16325q = 0;

    /* renamed from: r, reason: collision with root package name */
    public long f16326r = 0;

    /* renamed from: s, reason: collision with root package name */
    public long f16327s = 0;

    /* renamed from: t, reason: collision with root package name */
    public long f16328t = 0;

    /* renamed from: u, reason: collision with root package name */
    public long f16329u = 0;

    /* renamed from: v, reason: collision with root package name */
    public long f16330v = 0;

    /* renamed from: w, reason: collision with root package name */
    public long f16331w = 0;

    /* renamed from: x, reason: collision with root package name */
    public int f16332x = 0;

    /* renamed from: y, reason: collision with root package name */
    public int f16333y = 0;

    /* renamed from: z, reason: collision with root package name */
    public int f16334z = 0;
    public int A = 0;
    public int B = 0;
    public int C = 0;
    public int D = 0;
    public int E = 0;
    public int F = 0;
    public int G = 0;
    public int H = 0;

    public HighPowerSipper(String str) {
        this.f16309a = str;
    }

    private void f(int i10) {
        long j10 = i10;
        this.f16311c *= j10;
        this.f16312d *= j10;
        this.f16313e *= j10;
        this.f16314f *= j10;
        this.f16315g *= i10 * 2;
        this.f16317i *= j10;
        this.f16318j *= j10;
        this.f16319k *= j10;
        this.f16320l *= j10;
        this.f16321m *= j10;
    }

    public void a() {
        f(2);
    }

    public void b() {
        this.f16333y = 0;
        this.f16334z = 0;
        this.A = 0;
        this.B = 0;
        this.C = 0;
        this.D = 0;
        this.E = 0;
        this.F = 0;
        this.G = 0;
        this.H = 0;
    }

    public void c(AppStats appStats, long j10) {
        this.f16310b = null;
        this.f16310b = new StringBuilder("");
        this.f16322n = false;
        this.f16316h = false;
        this.f16323o = appStats.f16952g.f16980e;
        WakeLock wakeLock = appStats.f16956k;
        this.f16324p = wakeLock.f17105f;
        this.f16325q = wakeLock.f17109j;
        long j11 = appStats.f16954i.f17023m;
        this.f16326r = j11;
        if (j10 % 2 == 0) {
            this.f16327s = j11;
        }
        this.f16328t = appStats.f16958m.f17112a0;
        this.f16329u = appStats.f16961p.f16974l;
        this.f16330v = appStats.f16962q.f17009l;
        this.f16331w = appStats.f16960o.f17075m;
        this.f16332x = appStats.f16953h.f16940c;
    }

    public void d() {
        this.f16323o = 0L;
        this.f16324p = 0L;
        this.f16325q = 0L;
        this.f16326r = 0L;
        this.f16327s = 0L;
        this.f16328t = 0L;
        this.f16329u = 0L;
        this.f16330v = 0L;
        this.f16331w = 0L;
        this.f16332x = 0;
    }

    public void e(AppStats appStats) {
        this.f16311c = appStats.f16952g.f16980e - this.f16323o;
        WakeLock wakeLock = appStats.f16956k;
        this.f16312d = wakeLock.f17105f - this.f16324p;
        this.f16313e = wakeLock.f17109j - this.f16325q;
        long j10 = appStats.f16954i.f17023m;
        this.f16314f = j10 - this.f16326r;
        this.f16315g = j10 - this.f16327s;
        this.f16317i = appStats.f16958m.f17112a0 - this.f16328t;
        this.f16318j = appStats.f16961p.f16974l - this.f16329u;
        this.f16319k = appStats.f16962q.f17009l - this.f16330v;
        this.f16320l = appStats.f16960o.f17075m - this.f16331w;
        this.f16321m = appStats.f16953h.f16940c - this.f16332x;
    }

    public void g(boolean z10) {
        this.f16322n = z10;
    }

    public void h(boolean z10) {
        this.f16316h = z10;
    }
}
