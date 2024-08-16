package a7;

import a7.PowerConsumeStats;
import android.content.Context;
import android.os.SystemClock;
import android.util.ArrayMap;
import b6.LocalLog;
import c6.NotifyUtil;
import e7.ScenarioUtil;
import f6.CommonUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import n8.PowerConsumptionOptimizationHelper;
import o9.HighPowerSipper;
import p9.PowerMonitor;
import r9.SimplePowerMonitorUtils;
import y6.PowerCapacityHelper;
import z6.HighPowerDetector;
import z6.PowerDetector;

/* compiled from: PowerConsumeStatsImpl.java */
/* renamed from: a7.b, reason: use source file name */
/* loaded from: classes.dex */
public class PowerConsumeStatsImpl extends PowerConsumeStats {

    /* renamed from: h, reason: collision with root package name */
    private int f78h;

    /* renamed from: i, reason: collision with root package name */
    private PowerDetector f79i;

    /* renamed from: j, reason: collision with root package name */
    private boolean f80j;

    /* renamed from: k, reason: collision with root package name */
    private ArrayList<PowerConsumeStats.b> f81k;

    /* renamed from: l, reason: collision with root package name */
    private ArrayList<HighPowerSipper> f82l;

    /* renamed from: m, reason: collision with root package name */
    private ArrayList<String> f83m;

    /* renamed from: n, reason: collision with root package name */
    private ArrayMap<String, Long> f84n;

    public PowerConsumeStatsImpl(Context context) {
        super(context);
        this.f78h = -1;
        this.f79i = null;
        this.f80j = false;
        this.f81k = new ArrayList<>();
        this.f82l = new ArrayList<>();
        this.f83m = new ArrayList<>();
        this.f84n = new ArrayMap<>();
        this.f79i = new HighPowerDetector(context);
    }

    private int l(long j10, int i10) {
        LocalLog.a("PowerConsumeStats", "calculateAverage time interval is " + j10 + ", capacity interval is " + i10);
        return (int) ((3600000.0d / j10) * i10);
    }

    private void m(ConcurrentHashMap<String, HighPowerSipper> concurrentHashMap) {
        long j10;
        int i10;
        ArrayList arrayList;
        if (LocalLog.f()) {
            LocalLog.a("PowerConsumeStats", "handleHighPower ");
            LocalLog.a("PowerConsumeStats", "highPowerSipperHashMap: " + concurrentHashMap.size());
        }
        SimplePowerMonitorUtils d10 = SimplePowerMonitorUtils.d(this.f74f);
        this.f82l.clear();
        this.f83m.clear();
        ArrayList arrayList2 = new ArrayList();
        Iterator<Map.Entry<String, HighPowerSipper>> it = concurrentHashMap.entrySet().iterator();
        while (it.hasNext()) {
            arrayList2.add(it.next().getValue());
        }
        if (LocalLog.f()) {
            LocalLog.a("PowerConsumeStats", "highPowerSipperList: " + arrayList2.size());
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        int i11 = 0;
        int i12 = 0;
        while (i12 < arrayList2.size()) {
            HighPowerSipper highPowerSipper = (HighPowerSipper) arrayList2.get(i12);
            String str = highPowerSipper.f16309a;
            String substring = SimplePowerMonitorUtils.f17652c != 0 ? str.substring(i11, str.lastIndexOf(".")) : str;
            List<Integer> c10 = d10.c(str);
            if (LocalLog.g()) {
                LocalLog.a("PowerConsumeStats", "pkg: " + str + "; isPkgRunning: " + PowerMonitor.i().containsKey(str));
            }
            if (!PowerMonitor.i().containsKey(str) || PowerMonitor.j().get(str) == null || PowerMonitor.j().get(str).f16949d) {
                j10 = elapsedRealtime;
                i10 = i11;
                arrayList = arrayList2;
            } else {
                if (LocalLog.g()) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("pkg: ");
                    sb2.append(str);
                    sb2.append("; isBg: ");
                    sb2.append(!PowerMonitor.j().get(str).f16949d);
                    LocalLog.a("PowerConsumeStats", sb2.toString());
                    LocalLog.a("PowerConsumeStats", "handleHighPowerSipper " + str);
                }
                this.f84n.put(str, Long.valueOf(elapsedRealtime));
                long[] jArr = new long[9];
                if (SimplePowerMonitorUtils.i().contains(str)) {
                    jArr[i11] = SimplePowerMonitorUtils.f17674y;
                    jArr[1] = SimplePowerMonitorUtils.f17675z;
                    jArr[2] = SimplePowerMonitorUtils.A;
                    jArr[3] = SimplePowerMonitorUtils.B;
                    jArr[4] = SimplePowerMonitorUtils.C;
                    jArr[5] = SimplePowerMonitorUtils.D;
                    jArr[6] = SimplePowerMonitorUtils.E;
                    jArr[7] = SimplePowerMonitorUtils.F;
                    jArr[8] = SimplePowerMonitorUtils.G;
                } else {
                    jArr[i11] = SimplePowerMonitorUtils.f17665p;
                    jArr[1] = SimplePowerMonitorUtils.f17666q;
                    jArr[2] = SimplePowerMonitorUtils.f17667r;
                    jArr[3] = SimplePowerMonitorUtils.f17668s;
                    jArr[4] = SimplePowerMonitorUtils.f17669t;
                    jArr[5] = SimplePowerMonitorUtils.f17670u;
                    jArr[6] = SimplePowerMonitorUtils.f17671v;
                    jArr[7] = SimplePowerMonitorUtils.f17672w;
                    jArr[8] = SimplePowerMonitorUtils.f17673x;
                }
                if (LocalLog.g()) {
                    int i13 = i11;
                    while (i13 < 9) {
                        LocalLog.a("PowerConsumeStats", "detectorThreshold: " + jArr[i13]);
                        i13++;
                        arrayList2 = arrayList2;
                        elapsedRealtime = elapsedRealtime;
                    }
                }
                arrayList = arrayList2;
                j10 = elapsedRealtime;
                try {
                    if (this.f82l.size() >= 1000) {
                        break;
                    }
                    if (LocalLog.g()) {
                        LocalLog.a("PowerConsumeStats", "pkg: " + str + "; simplePowerMonitorUtils.isSocFilter(appStates): " + d10.v(c10));
                    }
                    i10 = 0;
                    try {
                        if (highPowerSipper.f16311c > jArr[0] && !d10.v(c10)) {
                            if (!this.f82l.contains(highPowerSipper)) {
                                this.f82l.add(highPowerSipper);
                                this.f83m.add(highPowerSipper.f16309a);
                            }
                            highPowerSipper.f16333y++;
                            LocalLog.a("PowerConsumeStats", "sipper.mPkgName: " + highPowerSipper.f16309a);
                            highPowerSipper.f16310b.append("Cpu");
                            if (LocalLog.g()) {
                                LocalLog.a("PowerConsumeStats", "highpower_isnotificate: pkgName: " + str + " is ready to notificate; DrainReason:Cpu");
                            }
                        }
                        if (highPowerSipper.f16313e > jArr[1] && !d10.v(c10)) {
                            if (!this.f82l.contains(highPowerSipper)) {
                                this.f82l.add(highPowerSipper);
                                this.f83m.add(highPowerSipper.f16309a);
                            }
                            highPowerSipper.f16334z++;
                            highPowerSipper.f16310b.append("WakeLock");
                            if (LocalLog.g()) {
                                LocalLog.a("PowerConsumeStats", "highpower_isnotificate: pkgName: " + str + " is ready to notificate; DrainReason:WakeLock");
                            }
                        }
                        if ((highPowerSipper.f16314f > jArr[2] || highPowerSipper.f16315g > jArr[3]) && !d10.v(c10)) {
                            if (!this.f82l.contains(highPowerSipper)) {
                                this.f82l.add(highPowerSipper);
                                this.f83m.add(highPowerSipper.f16309a);
                            }
                            highPowerSipper.A++;
                            highPowerSipper.f16310b.append("Job");
                            if (LocalLog.g()) {
                                LocalLog.a("PowerConsumeStats", "highpower_isnotificate: pkgName: " + str + " is ready to notificate; DrainReason:Job");
                            }
                        }
                        if (highPowerSipper.f16317i > jArr[4] && !d10.y(c10)) {
                            if (!this.f82l.contains(highPowerSipper)) {
                                this.f82l.add(highPowerSipper);
                                this.f83m.add(highPowerSipper.f16309a);
                            }
                            highPowerSipper.C++;
                            highPowerSipper.f16310b.append("Wifi");
                            if (LocalLog.g()) {
                                LocalLog.a("PowerConsumeStats", "highpower_isnotificate: pkgName: " + str + " is ready to notificate; DrainReason:Wifi");
                            }
                        }
                        if (highPowerSipper.f16318j > jArr[5]) {
                            if (!this.f82l.contains(highPowerSipper)) {
                                this.f82l.add(highPowerSipper);
                                this.f83m.add(highPowerSipper.f16309a);
                            }
                            highPowerSipper.D++;
                            highPowerSipper.f16310b.append("Camera");
                            if (LocalLog.g()) {
                                LocalLog.a("PowerConsumeStats", "highpower_isnotificate: pkgName: " + str + " is ready to notificate; DrainReason:Camera");
                            }
                        }
                        if (highPowerSipper.f16319k > jArr[6]) {
                            if (!this.f82l.contains(highPowerSipper)) {
                                this.f82l.add(highPowerSipper);
                                this.f83m.add(highPowerSipper.f16309a);
                            }
                            highPowerSipper.E++;
                            highPowerSipper.f16310b.append("FlashLight");
                            if (LocalLog.g()) {
                                LocalLog.a("PowerConsumeStats", "highpower_isnotificate: pkgName: " + str + " is ready to notificate; DrainReason:FlashLight");
                            }
                        }
                        if (highPowerSipper.f16320l > jArr[7] && !ScenarioUtil.c().contains(substring) && !d10.q(c10)) {
                            if (!this.f82l.contains(highPowerSipper)) {
                                this.f82l.add(highPowerSipper);
                                this.f83m.add(highPowerSipper.f16309a);
                            }
                            highPowerSipper.F++;
                            highPowerSipper.f16310b.append("GPS");
                            if (LocalLog.g()) {
                                LocalLog.a("PowerConsumeStats", "highpower_isnotificate: pkgName: " + str + " is ready to notificate; DrainReason:GPS");
                            }
                        }
                        if (highPowerSipper.f16321m > jArr[8] && !d10.m(c10)) {
                            if (!this.f82l.contains(highPowerSipper)) {
                                this.f82l.add(highPowerSipper);
                                this.f83m.add(highPowerSipper.f16309a);
                            }
                            highPowerSipper.G++;
                            highPowerSipper.f16310b.append("Alarm");
                            if (LocalLog.g()) {
                                LocalLog.a("PowerConsumeStats", "highpower_isnotificate: pkgName: " + str + " is ready to notificate; DrainReason:Alarm");
                            }
                        }
                        if (highPowerSipper.f16322n && !d10.o(c10)) {
                            if (!this.f82l.contains(highPowerSipper)) {
                                this.f82l.add(highPowerSipper);
                                this.f83m.add(highPowerSipper.f16309a);
                            }
                            highPowerSipper.H++;
                            highPowerSipper.f16310b.append("Audio");
                            if (LocalLog.g()) {
                                LocalLog.a("PowerConsumeStats", "highpower_isnotificate: pkgName: " + str + " is ready to notificate; DrainReason:Audio");
                            }
                        }
                        if (highPowerSipper.f16316h) {
                            if (!this.f82l.contains(highPowerSipper)) {
                                this.f82l.add(highPowerSipper);
                                this.f83m.add(highPowerSipper.f16309a);
                            }
                            highPowerSipper.B++;
                            highPowerSipper.f16310b.append("Screen");
                            if (LocalLog.g()) {
                                LocalLog.a("PowerConsumeStats", "highpower_isnotificate: pkgName: " + str + " is ready to notificate; DrainReason:Screen");
                            }
                        }
                    } catch (Exception e10) {
                        e = e10;
                        e.printStackTrace();
                        LocalLog.l("PowerConsumeStats", "can not got package info for " + str);
                        i12++;
                        arrayList2 = arrayList;
                        i11 = i10;
                        elapsedRealtime = j10;
                    }
                } catch (Exception e11) {
                    e = e11;
                    i10 = 0;
                }
            }
            i12++;
            arrayList2 = arrayList;
            i11 = i10;
            elapsedRealtime = j10;
        }
        LocalLog.a("PowerConsumeStats", "mHighPowerSipperList.length(): " + this.f83m.size());
        if (this.f83m.size() > 0) {
            LocalLog.a("PowerConsumeStats", "handleHighPowerSipper is ready to send notification");
            if (CommonUtil.T(this.f74f)) {
                LocalLog.b("PowerConsumeStats", "skip boot reg");
            } else {
                PowerConsumptionOptimizationHelper.k(this.f74f).y(this.f83m, this.f82l);
                this.f80j = true;
            }
        }
    }

    @Override // a7.PowerConsumeStats
    public ArrayList<PowerConsumeStats.b> b() {
        ArrayList<PowerConsumeStats.b> arrayList;
        synchronized (this.f81k) {
            LocalLog.a("PowerConsumeStats", "server getAbnormalApps, size is " + this.f81k.size());
            arrayList = this.f81k;
        }
        return arrayList;
    }

    @Override // a7.PowerConsumeStats
    int c(long j10, int i10) {
        long j11 = j10 - this.f69a;
        int a10 = PowerCapacityHelper.a(this.f74f, i10);
        LocalLog.a("PowerConsumeStats", "getCurrent time interval is " + j11 + ", current capacity is " + a10);
        return l(j11, this.f78h - a10);
    }

    @Override // a7.PowerConsumeStats
    public void e() {
        o(SystemClock.elapsedRealtime(), this.f70b, true);
    }

    @Override // a7.PowerConsumeStats
    void g(int i10) {
    }

    @Override // a7.PowerConsumeStats
    void i() {
        LocalLog.a("PowerConsumeStats", "stopMonitoringImpl clear abnormals");
        synchronized (this.f81k) {
            this.f81k.clear();
        }
        if (this.f80j) {
            this.f80j = false;
            NotifyUtil.v(this.f74f).i();
        }
    }

    @Override // a7.PowerConsumeStats
    void k(long j10, int i10) {
        this.f78h = PowerCapacityHelper.a(this.f74f, i10);
        LocalLog.a("PowerConsumeStats", "updateMonitorInfoImpl capacity is " + this.f78h);
        this.f79i.b(j10, i10);
    }

    public void n() {
        if (this.f79i.a() != null) {
            ConcurrentHashMap<String, HighPowerSipper> a10 = this.f79i.a();
            LocalLog.a("PowerConsumeStats", "highPowerItems.size(): " + a10.size());
            m(a10);
        }
    }

    void o(long j10, int i10, boolean z10) {
        LocalLog.a("PowerConsumeStats", "unHandleAbnormalImpl clear abnormals");
        if (z10) {
            this.f73e = 1;
            j(j10, i10);
        }
        synchronized (this.f81k) {
            this.f81k.clear();
        }
        if (this.f80j) {
            this.f80j = false;
            NotifyUtil.v(this.f74f).i();
        }
    }
}
