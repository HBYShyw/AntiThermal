package q9;

import android.net.NetworkStatsHistory;
import android.os.SystemClock;
import com.android.internal.os.PowerProfile;
import com.android.internal.os.UidSipper;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;

/* compiled from: WiFi.java */
/* renamed from: q9.n, reason: use source file name */
/* loaded from: classes2.dex */
public class WiFi extends BasicPowerItem {
    public long A;
    public long B;
    public long C;
    public long D;
    public long E;
    public double F;
    public double G;
    public double H;
    public double I;
    public double J;
    public double K;
    public double L;
    public double M;
    public double N;
    public double O;
    public double P;
    public double Q;
    public long R;
    public long S;
    public long T;
    public long U;
    public long V;
    public long W;
    public long X;
    public long Y;
    public long Z;

    /* renamed from: a0, reason: collision with root package name */
    public long f17112a0;

    /* renamed from: b, reason: collision with root package name */
    private long f17113b;

    /* renamed from: b0, reason: collision with root package name */
    public long f17114b0;

    /* renamed from: c, reason: collision with root package name */
    private double f17115c;

    /* renamed from: c0, reason: collision with root package name */
    public long f17116c0;

    /* renamed from: d, reason: collision with root package name */
    private final double f17117d;

    /* renamed from: d0, reason: collision with root package name */
    public double f17118d0;

    /* renamed from: e, reason: collision with root package name */
    private final double f17119e;

    /* renamed from: e0, reason: collision with root package name */
    public double f17120e0;

    /* renamed from: f, reason: collision with root package name */
    private final double f17121f;

    /* renamed from: f0, reason: collision with root package name */
    public double f17122f0;

    /* renamed from: g, reason: collision with root package name */
    private float f17123g;

    /* renamed from: g0, reason: collision with root package name */
    public double f17124g0;

    /* renamed from: h, reason: collision with root package name */
    public long f17125h;

    /* renamed from: h0, reason: collision with root package name */
    public double f17126h0;

    /* renamed from: i, reason: collision with root package name */
    public long f17127i;

    /* renamed from: i0, reason: collision with root package name */
    public double f17128i0;

    /* renamed from: j, reason: collision with root package name */
    public long f17129j;

    /* renamed from: j0, reason: collision with root package name */
    public double f17130j0;

    /* renamed from: k, reason: collision with root package name */
    public long f17131k;

    /* renamed from: k0, reason: collision with root package name */
    public double f17132k0;

    /* renamed from: l, reason: collision with root package name */
    public long f17133l;

    /* renamed from: l0, reason: collision with root package name */
    public double f17134l0;

    /* renamed from: m, reason: collision with root package name */
    public long f17135m;

    /* renamed from: m0, reason: collision with root package name */
    public double f17136m0;

    /* renamed from: n, reason: collision with root package name */
    public long f17137n;

    /* renamed from: n0, reason: collision with root package name */
    public double f17138n0;

    /* renamed from: o, reason: collision with root package name */
    public long f17139o;

    /* renamed from: o0, reason: collision with root package name */
    public double f17140o0;

    /* renamed from: p, reason: collision with root package name */
    public long f17141p;

    /* renamed from: p0, reason: collision with root package name */
    public double f17142p0;

    /* renamed from: q, reason: collision with root package name */
    public long f17143q;

    /* renamed from: q0, reason: collision with root package name */
    public double f17144q0;

    /* renamed from: r, reason: collision with root package name */
    public long f17145r;

    /* renamed from: r0, reason: collision with root package name */
    public double f17146r0;

    /* renamed from: s, reason: collision with root package name */
    public long f17147s;

    /* renamed from: s0, reason: collision with root package name */
    public double f17148s0;

    /* renamed from: t, reason: collision with root package name */
    public long f17149t;

    /* renamed from: t0, reason: collision with root package name */
    public double f17150t0;

    /* renamed from: u, reason: collision with root package name */
    public long f17151u;

    /* renamed from: u0, reason: collision with root package name */
    public double f17152u0;

    /* renamed from: v, reason: collision with root package name */
    public long f17153v;

    /* renamed from: v0, reason: collision with root package name */
    private long f17154v0;

    /* renamed from: w, reason: collision with root package name */
    public long f17155w;

    /* renamed from: w0, reason: collision with root package name */
    private long f17156w0;

    /* renamed from: x, reason: collision with root package name */
    public long f17157x;

    /* renamed from: y, reason: collision with root package name */
    public long f17158y;

    /* renamed from: z, reason: collision with root package name */
    public long f17159z;

    public WiFi(PowerProfile powerProfile) {
        super(powerProfile);
        this.f17125h = 0L;
        this.f17127i = 0L;
        this.f17129j = 0L;
        this.f17131k = 0L;
        this.f17133l = 0L;
        this.f17135m = 0L;
        this.f17137n = 0L;
        this.f17139o = 0L;
        this.f17141p = 0L;
        this.f17143q = 0L;
        this.f17145r = 0L;
        this.f17147s = 0L;
        this.f17149t = 0L;
        this.f17151u = 0L;
        this.f17153v = 0L;
        this.f17155w = 0L;
        this.f17157x = 0L;
        this.f17158y = 0L;
        this.f17159z = 0L;
        this.A = 0L;
        this.B = 0L;
        this.C = 0L;
        this.D = 0L;
        this.E = 0L;
        this.F = UserProfileInfo.Constant.NA_LAT_LON;
        this.G = UserProfileInfo.Constant.NA_LAT_LON;
        this.H = UserProfileInfo.Constant.NA_LAT_LON;
        this.I = UserProfileInfo.Constant.NA_LAT_LON;
        this.J = UserProfileInfo.Constant.NA_LAT_LON;
        this.K = UserProfileInfo.Constant.NA_LAT_LON;
        this.L = UserProfileInfo.Constant.NA_LAT_LON;
        this.M = UserProfileInfo.Constant.NA_LAT_LON;
        this.N = UserProfileInfo.Constant.NA_LAT_LON;
        this.O = UserProfileInfo.Constant.NA_LAT_LON;
        this.P = UserProfileInfo.Constant.NA_LAT_LON;
        this.Q = UserProfileInfo.Constant.NA_LAT_LON;
        this.R = 0L;
        this.S = 0L;
        this.T = 0L;
        this.U = 0L;
        this.V = 0L;
        this.W = 0L;
        this.X = 0L;
        this.Y = 0L;
        this.Z = 0L;
        this.f17112a0 = 0L;
        this.f17114b0 = 0L;
        this.f17116c0 = 0L;
        this.f17118d0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17120e0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17122f0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17124g0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17126h0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17128i0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17130j0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17132k0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17134l0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17136m0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17138n0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17140o0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17142p0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17144q0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17146r0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17148s0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17150t0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17152u0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17113b = 0L;
        this.f17115c = (float) powerProfile.getAveragePower("wifi.active");
        this.f17117d = powerProfile.getAveragePower("wifi.on");
        this.f17119e = powerProfile.getAveragePower("wifi.scan");
        this.f17121f = powerProfile.getAveragePower("wifi.batchedscan");
        this.f17123g = 0.0f;
        this.f17154v0 = 0L;
        this.f17156w0 = 0L;
    }

    private void d(long j10) {
        float f10 = (float) (this.f17115c / 3600.0d);
        float f11 = j10 > 0 ? (((float) this.f17113b) * 1000.0f) / ((float) j10) : 0.0f;
        float f12 = f11 < 2.0f ? 10.0f : f11 < 20.0f ? 3.0f : f11 < 200.0f ? 1.0f : f11 < 1000.0f ? 0.25f : f11 < 2000.0f ? 0.15f : 0.08f;
        if (f11 > 0.0f) {
            this.f17123g = (f10 * f12) / 800.0f;
        }
    }

    public void a() {
        this.f17149t = 0L;
        this.f17151u = 0L;
        this.f17125h = 0L;
        this.f17127i = 0L;
        this.F = UserProfileInfo.Constant.NA_LAT_LON;
        this.G = UserProfileInfo.Constant.NA_LAT_LON;
        this.R = 0L;
        this.f17118d0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17120e0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17142p0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17157x = 0L;
        this.f17159z = 0L;
        this.f17133l = 0L;
        this.f17137n = 0L;
        this.J = UserProfileInfo.Constant.NA_LAT_LON;
        this.L = UserProfileInfo.Constant.NA_LAT_LON;
        this.V = 0L;
        this.f17126h0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17130j0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17146r0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17158y = 0L;
        this.A = 0L;
        this.f17135m = 0L;
        this.f17139o = 0L;
        this.K = UserProfileInfo.Constant.NA_LAT_LON;
        this.M = UserProfileInfo.Constant.NA_LAT_LON;
        this.W = 0L;
        this.f17128i0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17132k0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17148s0 = UserProfileInfo.Constant.NA_LAT_LON;
    }

    public void b(UidSipper uidSipper, NetworkStatsHistory.Entry entry, int i10, boolean z10) {
        long j10 = entry.rxBytes;
        long j11 = entry.txBytes;
        long j12 = entry.rxPackets;
        long j13 = entry.txPackets;
        if (z10) {
            this.f17113b = 0L;
        } else {
            this.f17113b = j12 + j13;
        }
        long j14 = j12 + j13;
        this.f17113b = j14;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        d(elapsedRealtime - this.f17156w0);
        this.f17156w0 = elapsedRealtime;
        this.f17113b = j14;
        float f10 = (float) j14;
        float f11 = this.f17123g;
        double d10 = f10 * f11;
        double d11 = ((float) j12) * f11;
        double d12 = ((float) j13) * f11;
        long time = uidSipper.getWifiScanTime().getTime() / 1000;
        double d13 = (time * this.f17119e) / 3600000.0d;
        int i11 = 0;
        double d14 = 0.0d;
        while (i11 < 5) {
            d14 += (uidSipper.getWifiBatchedScanTime(i11) * this.f17121f) / 3600000.0d;
            i11++;
            j12 = j12;
        }
        long j15 = j12;
        double d15 = d14;
        double d16 = d10 + d13 + d15;
        if (z10) {
            this.f17149t = j10;
            this.f17151u = j11;
            this.f17125h = j15;
            this.f17127i = j13;
            this.F = d11;
            this.G = d12;
            this.R = time;
            this.f17118d0 = d13;
            this.f17120e0 = d15;
            this.f17142p0 = d16;
            this.f17153v += j10 - j10;
            this.f17155w += j11 - j11;
            this.f17129j += j15 - j15;
            this.f17131k += j13 - j13;
            this.H += d11 - d11;
            this.I += d12 - d12;
            this.T += time - time;
            this.f17122f0 += d13 - d13;
            this.f17124g0 += d15 - d15;
            this.f17144q0 += d16 - d16;
            return;
        }
        this.f17153v += j10 - this.f17149t;
        this.f17155w += j11 - this.f17151u;
        this.f17129j += j15 - this.f17125h;
        this.f17131k += j13 - this.f17127i;
        this.H += d11 - this.F;
        this.I += d12 - this.G;
        this.T += time - this.R;
        this.f17122f0 += d13 - this.f17118d0;
        this.f17124g0 += d15 - this.f17120e0;
        this.f17144q0 += d16 - this.f17142p0;
        this.f17149t = j10;
        this.f17151u = j11;
        this.f17125h = j15;
        this.f17127i = j13;
        this.F = d11;
        this.G = d12;
        this.R = time;
        this.f17118d0 = d13;
        this.f17120e0 = d15;
        this.f17142p0 = d16;
    }

    public void c(UidSipper uidSipper, NetworkStatsHistory.Entry entry, long j10, int i10, boolean z10, boolean z11, boolean z12) {
        double d10;
        double d11;
        long j11;
        long j12;
        long j13;
        long j14;
        long j15 = entry.rxBytes;
        long j16 = entry.txBytes;
        long j17 = entry.rxPackets;
        long j18 = entry.txPackets;
        if (z11) {
            this.f17113b = 0L;
        } else {
            this.f17113b = j17 + j18;
        }
        d((j10 - this.f17154v0) / 1000);
        this.f17113b = j17 + j18;
        this.f17154v0 = j10;
        long time = uidSipper.getWifiScanTime().getTime() / 1000;
        double d12 = (time * this.f17119e) / 3600000.0d;
        int i11 = 0;
        double d13 = 0.0d;
        while (i11 < 5) {
            d13 += (uidSipper.getWifiBatchedScanTime(i11) * this.f17121f) / 3600000.0d;
            i11++;
            d12 = d12;
        }
        double d14 = d12;
        double d15 = d13;
        if (z10 && z11) {
            this.f17133l = j17;
            this.f17137n = j18;
            this.f17157x = j15;
            this.f17159z = j16;
            this.V = time;
            d10 = d14;
            this.f17126h0 = d10;
            this.f17130j0 = d15;
        } else {
            d10 = d14;
        }
        if (!z10 || z11) {
            d11 = d15;
            j11 = j16;
            j12 = j17;
            j13 = j18;
            j14 = j15;
        } else if (!z12) {
            this.V = time;
            this.f17126h0 = d10;
            this.f17130j0 = d15;
            this.f17133l = j17;
            this.f17137n = j18;
            this.f17157x = j15;
            this.f17159z = j16;
            double d16 = d10;
            long j19 = this.f17143q + (j17 - this.f17135m);
            this.f17143q = j19;
            long j20 = this.f17147s + (j18 - this.f17139o);
            this.f17147s = j20;
            this.C += j15 - this.f17158y;
            this.E += j16 - this.A;
            float f10 = this.f17123g;
            double d17 = ((float) j19) * f10;
            this.O = d17;
            double d18 = ((float) j20) * f10;
            this.Q = d18;
            this.f17112a0 += time - this.W;
            double d19 = this.f17136m0 + (d16 - this.f17128i0);
            this.f17136m0 = d19;
            d11 = d15;
            double d20 = this.f17140o0 + (d11 - this.f17132k0);
            this.f17140o0 = d20;
            this.f17152u0 = d20 + d19 + d18 + d17;
            d10 = d16;
            j12 = j17;
            j13 = j18;
            j11 = j16;
            time = time;
            j14 = j15;
        } else {
            d11 = d15;
            long j21 = this.f17143q + (j17 - this.f17135m);
            this.f17143q = j21;
            long j22 = this.f17147s + (j18 - this.f17139o);
            this.f17147s = j22;
            this.C += j15 - this.f17158y;
            this.E += j16 - this.A;
            float f11 = this.f17123g;
            double d21 = ((float) j21) * f11;
            this.O = d21;
            double d22 = ((float) j22) * f11;
            this.Q = d22;
            this.f17112a0 += time - this.W;
            double d23 = this.f17136m0 + (d10 - this.f17128i0);
            this.f17136m0 = d23;
            double d24 = this.f17140o0 + (d11 - this.f17132k0);
            this.f17140o0 = d24;
            this.f17152u0 = d24 + d23 + d22 + d21;
            time = time;
            this.W = time;
            this.f17128i0 = d10;
            this.f17132k0 = d11;
            j12 = j17;
            this.f17135m = j12;
            j13 = j18;
            this.f17139o = j13;
            j14 = j15;
            this.f17158y = j14;
            j11 = j16;
            this.A = j11;
        }
        if (z10 || z11) {
            return;
        }
        this.W = time;
        this.f17128i0 = d10;
        this.f17132k0 = d11;
        this.f17135m = j12;
        this.f17139o = j13;
        this.f17158y = j14;
        this.A = j11;
        double d25 = d11;
        long j23 = this.f17141p + (j12 - this.f17133l);
        this.f17141p = j23;
        long j24 = this.f17145r + (j13 - this.f17137n);
        this.f17145r = j24;
        this.B += j14 - this.f17157x;
        this.D += j11 - this.f17159z;
        float f12 = this.f17123g;
        double d26 = ((float) j23) * f12;
        this.N = d26;
        double d27 = ((float) j24) * f12;
        this.P = d27;
        this.Z += time - this.V;
        double d28 = this.f17134l0 + (d10 - this.f17126h0);
        this.f17134l0 = d28;
        double d29 = this.f17138n0 + (d25 - this.f17130j0);
        this.f17138n0 = d29;
        this.f17150t0 = d29 + d28 + d27 + d26;
    }

    public void e() {
        this.f17129j = 0L;
        this.f17131k = 0L;
        this.f17141p = 0L;
        this.f17143q = 0L;
        this.f17145r = 0L;
        this.f17147s = 0L;
        this.f17153v = 0L;
        this.f17155w = 0L;
        this.B = 0L;
        this.C = 0L;
        this.D = 0L;
        this.E = 0L;
        this.H = UserProfileInfo.Constant.NA_LAT_LON;
        this.I = UserProfileInfo.Constant.NA_LAT_LON;
        this.N = UserProfileInfo.Constant.NA_LAT_LON;
        this.O = UserProfileInfo.Constant.NA_LAT_LON;
        this.P = UserProfileInfo.Constant.NA_LAT_LON;
        this.Q = UserProfileInfo.Constant.NA_LAT_LON;
        this.T = 0L;
        this.U = 0L;
        this.Z = 0L;
        this.f17112a0 = 0L;
        this.f17114b0 = 0L;
        this.f17116c0 = 0L;
        this.f17122f0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17124g0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17134l0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17136m0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17138n0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17140o0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17144q0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17150t0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17152u0 = UserProfileInfo.Constant.NA_LAT_LON;
    }
}
