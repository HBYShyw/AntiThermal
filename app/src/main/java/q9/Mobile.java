package q9;

import android.net.NetworkStatsHistory;
import android.os.health.TimerStat;
import android.telephony.CellSignalStrength;
import com.android.internal.os.PowerProfile;
import com.android.internal.os.UidSipper;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;

/* compiled from: Mobile.java */
/* renamed from: q9.h, reason: use source file name */
/* loaded from: classes2.dex */
public class Mobile extends BasicPowerItem {
    public long A;
    public long B;
    public double C;
    public double D;
    public double E;
    public double F;
    public double G;
    public double H;
    public double I;
    public double J;
    public double K;
    public double L;
    public double M;
    public double N;
    public long O;
    public long P;
    public long Q;
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
    public double f17024a0;

    /* renamed from: b, reason: collision with root package name */
    private final double f17025b;

    /* renamed from: b0, reason: collision with root package name */
    public double f17026b0;

    /* renamed from: c, reason: collision with root package name */
    int f17027c;

    /* renamed from: c0, reason: collision with root package name */
    public double f17028c0;

    /* renamed from: d, reason: collision with root package name */
    private final double[] f17029d;

    /* renamed from: d0, reason: collision with root package name */
    public double f17030d0;

    /* renamed from: e, reason: collision with root package name */
    public long f17031e;

    /* renamed from: e0, reason: collision with root package name */
    public double f17032e0;

    /* renamed from: f, reason: collision with root package name */
    public long f17033f;

    /* renamed from: f0, reason: collision with root package name */
    public double f17034f0;

    /* renamed from: g, reason: collision with root package name */
    public long f17035g;

    /* renamed from: h, reason: collision with root package name */
    public long f17036h;

    /* renamed from: i, reason: collision with root package name */
    public long f17037i;

    /* renamed from: j, reason: collision with root package name */
    public long f17038j;

    /* renamed from: k, reason: collision with root package name */
    public long f17039k;

    /* renamed from: l, reason: collision with root package name */
    public long f17040l;

    /* renamed from: m, reason: collision with root package name */
    public long f17041m;

    /* renamed from: n, reason: collision with root package name */
    public long f17042n;

    /* renamed from: o, reason: collision with root package name */
    public long f17043o;

    /* renamed from: p, reason: collision with root package name */
    public long f17044p;

    /* renamed from: q, reason: collision with root package name */
    public long f17045q;

    /* renamed from: r, reason: collision with root package name */
    public long f17046r;

    /* renamed from: s, reason: collision with root package name */
    public long f17047s;

    /* renamed from: t, reason: collision with root package name */
    public long f17048t;

    /* renamed from: u, reason: collision with root package name */
    public long f17049u;

    /* renamed from: v, reason: collision with root package name */
    public long f17050v;

    /* renamed from: w, reason: collision with root package name */
    public long f17051w;

    /* renamed from: x, reason: collision with root package name */
    public long f17052x;

    /* renamed from: y, reason: collision with root package name */
    public long f17053y;

    /* renamed from: z, reason: collision with root package name */
    public long f17054z;

    public Mobile(PowerProfile powerProfile) {
        super(powerProfile);
        int numSignalStrengthLevels = CellSignalStrength.getNumSignalStrengthLevels();
        this.f17027c = numSignalStrengthLevels;
        this.f17029d = new double[numSignalStrengthLevels];
        this.f17031e = 0L;
        this.f17033f = 0L;
        this.f17035g = 0L;
        this.f17036h = 0L;
        this.f17037i = 0L;
        this.f17038j = 0L;
        this.f17039k = 0L;
        this.f17040l = 0L;
        this.f17041m = 0L;
        this.f17042n = 0L;
        this.f17043o = 0L;
        this.f17044p = 0L;
        this.f17045q = 0L;
        this.f17046r = 0L;
        this.f17047s = 0L;
        this.f17048t = 0L;
        this.f17049u = 0L;
        this.f17050v = 0L;
        this.f17051w = 0L;
        this.f17052x = 0L;
        this.f17053y = 0L;
        this.f17054z = 0L;
        this.A = 0L;
        this.B = 0L;
        this.C = UserProfileInfo.Constant.NA_LAT_LON;
        this.D = UserProfileInfo.Constant.NA_LAT_LON;
        this.E = UserProfileInfo.Constant.NA_LAT_LON;
        this.F = UserProfileInfo.Constant.NA_LAT_LON;
        this.G = UserProfileInfo.Constant.NA_LAT_LON;
        this.H = UserProfileInfo.Constant.NA_LAT_LON;
        this.I = UserProfileInfo.Constant.NA_LAT_LON;
        this.J = UserProfileInfo.Constant.NA_LAT_LON;
        this.K = UserProfileInfo.Constant.NA_LAT_LON;
        this.L = UserProfileInfo.Constant.NA_LAT_LON;
        this.M = UserProfileInfo.Constant.NA_LAT_LON;
        this.N = UserProfileInfo.Constant.NA_LAT_LON;
        this.O = 0L;
        this.P = 0L;
        this.Q = 0L;
        this.R = 0L;
        this.S = 0L;
        this.T = 0L;
        this.U = 0L;
        this.V = 0L;
        this.W = 0L;
        this.X = 0L;
        this.Y = 0L;
        this.Z = 0L;
        this.f17024a0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17026b0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17028c0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17030d0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17032e0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17034f0 = UserProfileInfo.Constant.NA_LAT_LON;
        double averagePowerOrDefault = powerProfile.getAveragePowerOrDefault("radio.active", -1.0d);
        int i10 = 0;
        int i11 = 1;
        if (averagePowerOrDefault != -1.0d) {
            this.f17025b = averagePowerOrDefault;
        } else {
            double averagePower = powerProfile.getAveragePower("modem.controller.rx") + UserProfileInfo.Constant.NA_LAT_LON;
            int i12 = 0;
            while (true) {
                if (i12 >= this.f17029d.length) {
                    break;
                }
                averagePower += powerProfile.getAveragePower("modem.controller.tx", i12);
                i12++;
            }
            this.f17025b = averagePower / (r3.length + 1);
        }
        if (powerProfile.getAveragePowerOrDefault("radio.on", -1.0d) != -1.0d) {
            while (true) {
                double[] dArr = this.f17029d;
                if (i10 >= dArr.length) {
                    return;
                }
                dArr[i10] = powerProfile.getAveragePower("radio.on", i10);
                i10++;
            }
        } else {
            double averagePower2 = powerProfile.getAveragePower("modem.controller.idle");
            this.f17029d[0] = (25.0d * averagePower2) / 180.0d;
            while (true) {
                double[] dArr2 = this.f17029d;
                if (i11 >= dArr2.length) {
                    return;
                }
                dArr2[i11] = Math.max(1.0d, averagePower2 / 256.0d);
                i11++;
            }
        }
    }

    private double b(long j10, long j11, long j12) {
        return ((this.f17025b / 3600.0d) / 12.20703125d) / 3600.0d;
    }

    public void a() {
        this.f17045q = 0L;
        this.f17046r = 0L;
        this.f17031e = 0L;
        this.f17033f = 0L;
        this.O = 0L;
        this.P = 0L;
        this.C = UserProfileInfo.Constant.NA_LAT_LON;
        this.D = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17024a0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17049u = 0L;
        this.f17050v = 0L;
        this.f17051w = 0L;
        this.f17052x = 0L;
        this.f17037i = 0L;
        this.f17038j = 0L;
        this.f17039k = 0L;
        this.f17040l = 0L;
        this.S = 0L;
        this.T = 0L;
        this.U = 0L;
        this.V = 0L;
        this.G = UserProfileInfo.Constant.NA_LAT_LON;
        this.H = UserProfileInfo.Constant.NA_LAT_LON;
        this.I = UserProfileInfo.Constant.NA_LAT_LON;
        this.J = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17028c0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17030d0 = UserProfileInfo.Constant.NA_LAT_LON;
    }

    public void c(UidSipper uidSipper, NetworkStatsHistory.Entry entry, boolean z10) {
        TimerStat mobileRadioActive = uidSipper.getMobileRadioActive();
        long time = mobileRadioActive.getTime() / 1000;
        long count = mobileRadioActive.getCount();
        long j10 = entry.rxBytes;
        long j11 = entry.txBytes;
        long j12 = entry.rxPackets;
        long j13 = entry.txPackets;
        double b10 = j12 * b(j12, j13, time);
        double b11 = j13 * b(j12, j13, time);
        double d10 = b10 + b11;
        if (z10) {
            this.f17045q = j10;
            this.f17046r = j11;
            this.f17031e = j12;
            this.f17033f = j13;
            this.O = count;
            this.P = time;
            this.C = b10;
            this.D = b11;
            this.f17024a0 = d10;
            this.f17047s += j10 - j10;
            this.f17048t += j11 - j11;
            this.f17035g += j12 - j12;
            this.f17036h += j13 - j13;
            this.Q += count - count;
            this.R += time - time;
            this.E += b10 - b10;
            this.F += b11 - b11;
            this.f17026b0 += d10 - d10;
            return;
        }
        this.f17047s += j10 - this.f17045q;
        this.f17048t += j11 - this.f17046r;
        this.f17035g += j12 - this.f17031e;
        this.f17036h += j13 - this.f17033f;
        this.Q += count - this.O;
        this.R += time - this.P;
        this.E += b10 - this.C;
        this.F += b11 - this.D;
        this.f17026b0 += d10 - this.f17024a0;
        this.f17045q = j10;
        this.f17046r = j11;
        this.f17031e = j12;
        this.f17033f = j13;
        this.O = count;
        this.P = time;
        this.C = b10;
        this.D = b11;
        this.f17024a0 = d10;
    }

    public void d(UidSipper uidSipper, NetworkStatsHistory.Entry entry, boolean z10, boolean z11, boolean z12) {
        double d10;
        long j10;
        long j11;
        double d11;
        long j12;
        TimerStat mobileRadioActive = uidSipper.getMobileRadioActive();
        long time = mobileRadioActive.getTime() / 1000;
        long count = mobileRadioActive.getCount();
        long j13 = entry.rxBytes;
        long j14 = entry.txBytes;
        long j15 = entry.rxPackets;
        long j16 = entry.txPackets;
        double b10 = b(j15, j16, time);
        double d12 = j15 * b10;
        double d13 = j16 * b10;
        double d14 = d12 + d13;
        if (z10 && z11) {
            this.f17037i = j15;
            this.f17039k = j16;
            this.f17049u = j13;
            this.f17051w = j14;
            this.G = d12;
            this.I = d13;
            this.f17028c0 = d14;
            d10 = d14;
            this.S = count;
            j10 = time;
            this.U = j10;
        } else {
            d10 = d14;
            j10 = time;
        }
        if (!z10 || z11) {
            j11 = count;
            long j17 = j10;
            d11 = d12;
            j12 = j17;
        } else if (!z12) {
            this.f17037i = j15;
            this.f17039k = j16;
            this.f17049u = j13;
            this.f17051w = j14;
            this.G = d12;
            this.I = d13;
            this.f17028c0 = d10;
            this.S = count;
            this.U = j10;
            long j18 = j10;
            j11 = count;
            this.f17042n += j15 - this.f17038j;
            this.f17044p += j16 - this.f17040l;
            this.f17054z += j13 - this.f17050v;
            this.B += j14 - this.f17052x;
            this.L += d12 - this.H;
            this.N += d13 - this.J;
            this.f17034f0 += d10 - this.f17030d0;
            this.X += j11 - this.T;
            this.Z += j18 - this.V;
            j12 = j18;
            d13 = d13;
            d11 = d12;
        } else {
            j11 = count;
            double d15 = d10;
            long j19 = j10;
            this.f17042n += j15 - this.f17038j;
            this.f17044p += j16 - this.f17040l;
            this.f17054z += j13 - this.f17050v;
            this.B += j14 - this.f17052x;
            this.L += d12 - this.H;
            this.N += d13 - this.J;
            this.f17034f0 += d15 - this.f17030d0;
            this.X += j11 - this.T;
            this.Z += j19 - this.V;
            this.f17038j = j15;
            this.f17040l = j16;
            this.f17050v = j13;
            this.f17052x = j14;
            d11 = d12;
            this.H = d11;
            d13 = d13;
            this.J = d13;
            this.f17030d0 = d15;
            this.T = j11;
            j12 = j19;
            this.V = j12;
        }
        if (z10 || z11) {
            return;
        }
        this.f17038j = j15;
        this.f17040l = j16;
        this.f17050v = j13;
        this.f17052x = j14;
        this.H = d11;
        this.J = d13;
        this.f17030d0 = d10;
        this.T = j11;
        this.V = j12;
        this.f17041m += j15 - this.f17037i;
        this.f17043o += j16 - this.f17039k;
        this.f17053y += j13 - this.f17049u;
        this.A += j14 - this.f17051w;
        this.K += d11 - this.G;
        this.M += d13 - this.I;
        this.f17032e0 += d10 - this.f17028c0;
        this.W += j11 - this.S;
        this.Y += j12 - this.U;
    }

    public void e() {
        this.f17035g = 0L;
        this.f17036h = 0L;
        this.f17041m = 0L;
        this.f17042n = 0L;
        this.f17043o = 0L;
        this.f17044p = 0L;
        this.f17047s = 0L;
        this.f17048t = 0L;
        this.f17053y = 0L;
        this.f17054z = 0L;
        this.A = 0L;
        this.B = 0L;
        this.E = UserProfileInfo.Constant.NA_LAT_LON;
        this.F = UserProfileInfo.Constant.NA_LAT_LON;
        this.K = UserProfileInfo.Constant.NA_LAT_LON;
        this.L = UserProfileInfo.Constant.NA_LAT_LON;
        this.M = UserProfileInfo.Constant.NA_LAT_LON;
        this.N = UserProfileInfo.Constant.NA_LAT_LON;
        this.Q = 0L;
        this.R = 0L;
        this.W = 0L;
        this.X = 0L;
        this.Y = 0L;
        this.Z = 0L;
        this.f17026b0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17032e0 = UserProfileInfo.Constant.NA_LAT_LON;
        this.f17034f0 = UserProfileInfo.Constant.NA_LAT_LON;
    }
}
