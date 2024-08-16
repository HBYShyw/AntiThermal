package q9;

import com.android.internal.os.PowerProfile;
import com.android.internal.os.UidSipper;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;

/* compiled from: Cpu.java */
/* renamed from: q9.e, reason: use source file name */
/* loaded from: classes2.dex */
public class Cpu extends BasicPowerItem {

    /* renamed from: b, reason: collision with root package name */
    public long f16977b;

    /* renamed from: c, reason: collision with root package name */
    public long f16978c;

    /* renamed from: d, reason: collision with root package name */
    public long f16979d;

    /* renamed from: e, reason: collision with root package name */
    public long f16980e;

    /* renamed from: f, reason: collision with root package name */
    public long f16981f;

    /* renamed from: g, reason: collision with root package name */
    public long f16982g;

    /* renamed from: h, reason: collision with root package name */
    public long f16983h;

    /* renamed from: i, reason: collision with root package name */
    public long f16984i;

    /* renamed from: j, reason: collision with root package name */
    public long f16985j;

    /* renamed from: k, reason: collision with root package name */
    public long f16986k;

    /* renamed from: l, reason: collision with root package name */
    public long f16987l;

    /* renamed from: m, reason: collision with root package name */
    public long f16988m;

    /* renamed from: n, reason: collision with root package name */
    public long f16989n;

    /* renamed from: o, reason: collision with root package name */
    public long f16990o;

    /* renamed from: p, reason: collision with root package name */
    public long f16991p;

    /* renamed from: q, reason: collision with root package name */
    public long f16992q;

    /* renamed from: r, reason: collision with root package name */
    public double f16993r;

    /* renamed from: s, reason: collision with root package name */
    public double f16994s;

    /* renamed from: t, reason: collision with root package name */
    public double f16995t;

    /* renamed from: u, reason: collision with root package name */
    public double f16996u;

    /* renamed from: v, reason: collision with root package name */
    public double f16997v;

    /* renamed from: w, reason: collision with root package name */
    public double f16998w;

    public Cpu(PowerProfile powerProfile) {
        super(powerProfile);
        this.f16977b = 0L;
        this.f16978c = 0L;
        this.f16979d = 0L;
        this.f16980e = 0L;
        this.f16981f = 0L;
        this.f16982g = 0L;
        this.f16983h = 0L;
        this.f16984i = 0L;
        this.f16985j = 0L;
        this.f16986k = 0L;
        this.f16987l = 0L;
        this.f16988m = 0L;
        this.f16989n = 0L;
        this.f16990o = 0L;
        this.f16991p = 0L;
        this.f16992q = 0L;
        this.f16993r = UserProfileInfo.Constant.NA_LAT_LON;
        this.f16994s = UserProfileInfo.Constant.NA_LAT_LON;
        this.f16995t = UserProfileInfo.Constant.NA_LAT_LON;
        this.f16996u = UserProfileInfo.Constant.NA_LAT_LON;
        this.f16997v = UserProfileInfo.Constant.NA_LAT_LON;
        this.f16998w = UserProfileInfo.Constant.NA_LAT_LON;
    }

    private double b(UidSipper uidSipper) {
        int numCpuClusters = this.f16963a.getNumCpuClusters();
        double d10 = UserProfileInfo.Constant.NA_LAT_LON;
        for (int i10 = 0; i10 < numCpuClusters; i10++) {
            int numSpeedStepsInCpuCluster = this.f16963a.getNumSpeedStepsInCpuCluster(i10);
            for (int i11 = 0; i11 < numSpeedStepsInCpuCluster; i11++) {
                d10 += uidSipper.getTimeAtCpuSpeed(i10, i11) * this.f16963a.getAveragePowerForCpuCore(i10, i11);
            }
        }
        double cpuActiveTime = d10 + (uidSipper.getCpuActiveTime() * 1000 * this.f16963a.getAveragePower("cpu.active"));
        long[] cpuClusterTimes = uidSipper.getCpuClusterTimes();
        if (cpuClusterTimes != null && cpuClusterTimes.length == numCpuClusters) {
            for (int i12 = 0; i12 < numCpuClusters; i12++) {
                cpuActiveTime += cpuClusterTimes[i12] * 1000 * this.f16963a.getAveragePowerForCpuCluster(i12);
            }
        }
        return cpuActiveTime / 3.6E9d;
    }

    public void a() {
        this.f16981f = 0L;
        this.f16987l = 0L;
        this.f16977b = 0L;
        this.f16993r = UserProfileInfo.Constant.NA_LAT_LON;
        this.f16983h = 0L;
        this.f16984i = 0L;
        this.f16989n = 0L;
        this.f16990o = 0L;
        this.f16995t = UserProfileInfo.Constant.NA_LAT_LON;
        this.f16996u = UserProfileInfo.Constant.NA_LAT_LON;
    }

    public void c(UidSipper uidSipper, boolean z10, boolean z11, boolean z12) {
        if (uidSipper == null) {
            return;
        }
        if (z10 && z11) {
            this.f16983h = uidSipper.getSystemCpuTimeUs() / 1000;
        }
        if (z10 && !z11) {
            long systemCpuTimeUs = uidSipper.getSystemCpuTimeUs() / 1000;
            if (!z12) {
                this.f16983h = systemCpuTimeUs;
                this.f16986k += systemCpuTimeUs - this.f16984i;
            } else {
                this.f16986k += systemCpuTimeUs - this.f16984i;
                this.f16984i = systemCpuTimeUs;
            }
        }
        if (z10 || z11) {
            return;
        }
        long systemCpuTimeUs2 = uidSipper.getSystemCpuTimeUs() / 1000;
        this.f16984i = systemCpuTimeUs2;
        this.f16985j += systemCpuTimeUs2 - this.f16983h;
    }

    public void d(UidSipper uidSipper, boolean z10, boolean z11, boolean z12) {
        if (uidSipper == null) {
            return;
        }
        if (z10 && z11) {
            this.f16995t = b(uidSipper);
        }
        if (z10 && !z11) {
            double b10 = b(uidSipper);
            if (!z12) {
                this.f16995t = b10;
                this.f16998w += b10 - this.f16996u;
            } else {
                this.f16998w += b10 - this.f16996u;
                this.f16996u = b10;
            }
        }
        if (z10 || z11) {
            return;
        }
        double b11 = b(uidSipper);
        this.f16996u = b11;
        this.f16997v += b11 - this.f16995t;
    }

    public void e() {
        this.f16979d = this.f16985j + this.f16991p;
        this.f16980e = this.f16986k + this.f16992q;
    }

    public void f(UidSipper uidSipper, boolean z10, boolean z11, boolean z12) {
        if (uidSipper == null) {
            return;
        }
        if (z10 && z11) {
            this.f16989n = uidSipper.getUserCpuTimeUs() / 1000;
        }
        if (z10 && !z11) {
            long userCpuTimeUs = uidSipper.getUserCpuTimeUs() / 1000;
            if (!z12) {
                this.f16989n = userCpuTimeUs;
                this.f16992q += userCpuTimeUs - this.f16990o;
            } else {
                this.f16992q += userCpuTimeUs - this.f16990o;
                this.f16990o = userCpuTimeUs;
            }
        }
        if (z10 || z11) {
            return;
        }
        long userCpuTimeUs2 = uidSipper.getUserCpuTimeUs() / 1000;
        this.f16990o = userCpuTimeUs2;
        this.f16991p += userCpuTimeUs2 - this.f16989n;
    }

    public void g(UidSipper uidSipper, boolean z10) {
        long systemCpuTimeUs = uidSipper.getSystemCpuTimeUs() / 1000;
        long userCpuTimeUs = uidSipper.getUserCpuTimeUs() / 1000;
        long j10 = systemCpuTimeUs + userCpuTimeUs;
        double b10 = b(uidSipper);
        if (z10) {
            this.f16981f = systemCpuTimeUs;
            this.f16987l = userCpuTimeUs;
            this.f16977b = j10;
            this.f16993r = b10;
            this.f16982g += systemCpuTimeUs - systemCpuTimeUs;
            this.f16988m += userCpuTimeUs - userCpuTimeUs;
            this.f16978c += j10 - j10;
            this.f16994s += b10 - b10;
            return;
        }
        this.f16982g += systemCpuTimeUs - this.f16981f;
        this.f16988m += userCpuTimeUs - this.f16987l;
        this.f16978c += j10 - this.f16977b;
        this.f16994s += b10 - this.f16993r;
        this.f16981f = systemCpuTimeUs;
        this.f16987l = userCpuTimeUs;
        this.f16977b = j10;
        this.f16993r = b10;
    }

    public void h() {
        this.f16978c = 0L;
        this.f16979d = 0L;
        this.f16980e = 0L;
        this.f16982g = 0L;
        this.f16985j = 0L;
        this.f16986k = 0L;
        this.f16988m = 0L;
        this.f16991p = 0L;
        this.f16992q = 0L;
        this.f16994s = UserProfileInfo.Constant.NA_LAT_LON;
        this.f16997v = UserProfileInfo.Constant.NA_LAT_LON;
        this.f16998w = UserProfileInfo.Constant.NA_LAT_LON;
    }
}
