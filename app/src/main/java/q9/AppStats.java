package q9;

import android.content.Context;
import android.hardware.Sensor;
import android.net.NetworkStatsHistory;
import android.os.SystemClock;
import b6.LocalLog;
import com.android.internal.os.PowerProfile;
import com.android.internal.os.UidSipper;
import java.util.HashMap;

/* compiled from: AppStats.java */
/* renamed from: q9.b, reason: use source file name */
/* loaded from: classes2.dex */
public class AppStats {

    /* renamed from: r, reason: collision with root package name */
    private static final boolean f16945r = LocalLog.g();

    /* renamed from: a, reason: collision with root package name */
    private Context f16946a;

    /* renamed from: b, reason: collision with root package name */
    public String f16947b;

    /* renamed from: c, reason: collision with root package name */
    public int f16948c;

    /* renamed from: d, reason: collision with root package name */
    public boolean f16949d;

    /* renamed from: e, reason: collision with root package name */
    public boolean f16950e = false;

    /* renamed from: f, reason: collision with root package name */
    public boolean f16951f = false;

    /* renamed from: g, reason: collision with root package name */
    public Cpu f16952g;

    /* renamed from: h, reason: collision with root package name */
    public Alarm f16953h;

    /* renamed from: i, reason: collision with root package name */
    public g f16954i;

    /* renamed from: j, reason: collision with root package name */
    public Sync f16955j;

    /* renamed from: k, reason: collision with root package name */
    public WakeLock f16956k;

    /* renamed from: l, reason: collision with root package name */
    public Screen f16957l;

    /* renamed from: m, reason: collision with root package name */
    public WiFi f16958m;

    /* renamed from: n, reason: collision with root package name */
    public Mobile f16959n;

    /* renamed from: o, reason: collision with root package name */
    public Sensor f16960o;

    /* renamed from: p, reason: collision with root package name */
    public Camera f16961p;

    /* renamed from: q, reason: collision with root package name */
    public FlashLight f16962q;

    public AppStats(int i10, String str, boolean z10, PowerProfile powerProfile, HashMap<Integer, Sensor> hashMap, Context context) {
        this.f16946a = context;
        this.f16947b = str;
        this.f16948c = i10;
        this.f16949d = z10;
        this.f16952g = new Cpu(powerProfile);
        this.f16953h = new Alarm(powerProfile);
        this.f16954i = new g(powerProfile);
        this.f16955j = new Sync(powerProfile);
        this.f16956k = new WakeLock(powerProfile);
        this.f16957l = new Screen(powerProfile);
        this.f16958m = new WiFi(powerProfile);
        this.f16959n = new Mobile(powerProfile);
        this.f16960o = new Sensor(powerProfile, hashMap);
        this.f16961p = new Camera(powerProfile);
        this.f16962q = new FlashLight(powerProfile);
    }

    private void b(UidSipper uidSipper, boolean z10) {
        this.f16953h.c(uidSipper, z10);
        if (this.f16947b.equals("com.android.launcher") || !f16945r) {
            return;
        }
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; AlarmWakeupCount = " + this.f16953h.f16939b + "; AlarmTotalWakeupCount = " + this.f16953h.f16940c);
    }

    private void c(UidSipper uidSipper, boolean z10, boolean z11, boolean z12) {
        this.f16953h.d(uidSipper, z10, z11, z12);
        if (this.f16947b.equals("com.android.launcher") || !f16945r) {
            return;
        }
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; AlarmFgWakeupCount = " + this.f16953h.f16942e + "; AlarmBgWakeupCount = " + this.f16953h.f16944g);
    }

    private void f(UidSipper uidSipper, boolean z10) {
        this.f16961p.b(uidSipper, z10);
        if (this.f16947b.equals("com.android.launcher") || !f16945r) {
            return;
        }
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; CameraTime = " + this.f16961p.f16965c + " ms; CameraTotalTime = " + this.f16961p.f16967e + " ms");
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; CameraPower = " + this.f16961p.f16966d + " mAh; CameraTotalPower = " + this.f16961p.f16968f + " ms");
    }

    private void g(UidSipper uidSipper, boolean z10, boolean z11, boolean z12) {
        this.f16961p.c(uidSipper, z10, z11, z12);
        if (this.f16947b.equals("com.android.launcher") || !f16945r) {
            return;
        }
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; FgCameraTime = " + this.f16961p.f16973k + " ms; BgCameraTime = " + this.f16961p.f16974l + " ms");
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; FgCameraPower = " + this.f16961p.f16975m + " mAh; BgCameraPower = " + this.f16961p.f16976n + " mAh");
    }

    private void h(UidSipper uidSipper, boolean z10) {
        this.f16952g.g(uidSipper, z10);
        if (this.f16947b.equals("com.android.launcher") || !f16945r) {
            return;
        }
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; CpuTime = " + this.f16952g.f16977b + " ms; CpuTotalTime = " + this.f16952g.f16978c + " ms");
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; CpuKernelTime = " + this.f16952g.f16981f + " ms; CpuTotalKernelTime = " + this.f16952g.f16982g + " ms");
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; CpuUserTime = " + this.f16952g.f16987l + " ms; CpuTotalUserTime = " + this.f16952g.f16988m + " ms");
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; CpuPower = " + this.f16952g.f16993r + " mAh.; CpuTotalPower = " + this.f16952g.f16994s + " mAh.");
    }

    private void i(UidSipper uidSipper, boolean z10, boolean z11, boolean z12) {
        this.f16952g.c(uidSipper, z10, z11, z12);
        this.f16952g.f(uidSipper, z10, z11, z12);
        this.f16952g.e();
        this.f16952g.d(uidSipper, z10, z11, z12);
        if (z11 && !this.f16947b.equals("com.android.launcher") && f16945r) {
            LocalLog.b("AppStats", this.f16947b + "first la");
        }
        if (this.f16947b.equals("com.android.launcher") || !f16945r) {
            return;
        }
        LocalLog.a("AppStats", "isFirstTimer: " + z11);
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; CpuFgTime = " + this.f16952g.f16979d + " ms; CpuBgTime = " + this.f16952g.f16980e + " ms.");
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; CpuKernelFgTime = " + this.f16952g.f16985j + " ms; CpuKernelBgTime = " + this.f16952g.f16986k + " ms.");
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; CpuUserFgTime = " + this.f16952g.f16991p + " ms ; CpuUserBgTime = " + this.f16952g.f16992q + " ms.");
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; CpuFgPower = " + this.f16952g.f16997v + " mAh.; CpuBgPower = " + this.f16952g.f16998w + " mAh.");
    }

    private void j(UidSipper uidSipper, boolean z10) {
        this.f16962q.b(uidSipper, z10);
        if (this.f16947b.equals("com.android.launcher") || !f16945r) {
            return;
        }
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; FlashLightTime = " + this.f16962q.f17000c + " ms; FlashLightTotalTime = " + this.f16962q.f17002e + " ms");
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; FlashLightPower = " + this.f16962q.f17001d + " mAh; FlashLightTotalPower = " + this.f16962q.f17003f + " mAh");
    }

    private void k(UidSipper uidSipper, boolean z10, boolean z11, boolean z12) {
        this.f16962q.c(uidSipper, z10, z11, z12);
        if (this.f16947b.equals("com.android.launcher") || !f16945r) {
            return;
        }
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; FgFlashLightTime = " + this.f16962q.f17008k + " ms; BgFlashLightTime = " + this.f16962q.f17009l + " ms");
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; FgFlashLightPower = " + this.f16962q.f17010m + " mAh; BgFlashLightPower = " + this.f16962q.f17011n + " mAh");
    }

    private void l(UidSipper uidSipper, boolean z10) {
        this.f16954i.b(uidSipper, z10);
        if (this.f16947b.equals("com.android.launcher") || !f16945r) {
            return;
        }
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; JobCount = " + this.f16954i.f17012b + "; JobTime = " + this.f16954i.f17014d + "; JobTotalCount = " + this.f16954i.f17013c + "; JobTotalTime = " + this.f16954i.f17015e);
    }

    private void m(UidSipper uidSipper, boolean z10, boolean z11, boolean z12) {
        this.f16954i.c(uidSipper, z10, z11, z12);
        if (this.f16947b.equals("com.android.launcher") || !f16945r) {
            return;
        }
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; JobFgCount = " + this.f16954i.f17017g + "; JobBgCount = " + this.f16954i.f17021k + "; JobFgTime = " + this.f16954i.f17019i + "; JobBgTime = " + this.f16954i.f17023m);
    }

    private void n(UidSipper uidSipper, NetworkStatsHistory.Entry entry, boolean z10) {
        this.f16959n.c(uidSipper, entry, z10);
        if (this.f16947b.equals("com.android.launcher") || !f16945r) {
            return;
        }
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; MobileRxBytes = " + this.f16959n.f17045q + " Bytes; MobileTxBytes = " + this.f16959n.f17046r + " Bytes; MobileRxPackets = " + this.f16959n.f17031e + " ; MobileTxPackets = " + this.f16959n.f17033f + " ; MobileActiveCount = " + this.f16959n.O + " ; MobileActiveTime = " + this.f16959n.P + " ; MobileTotalRxBytes = " + this.f16959n.f17047s + " Bytes; MobileTotalTxBytes = " + this.f16959n.f17048t + " Bytes; MobileTotalRxPackets = " + this.f16959n.f17035g + " ; MobileTotalTxPackets = " + this.f16959n.f17036h + " ; MobileTotalActiveCount = " + this.f16959n.Q + " ; MobileTotalActiveTime = " + this.f16959n.R + " ");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Uid = ");
        sb2.append(this.f16948c);
        sb2.append("; pkgName = ");
        sb2.append(this.f16947b);
        sb2.append("; MobileRxPacketsPower = ");
        sb2.append(this.f16959n.C);
        sb2.append(" mAh; MobileTxPacketsPower = ");
        sb2.append(this.f16959n.D);
        sb2.append(" mAh; MobileTotalPower = ");
        sb2.append(this.f16959n.f17024a0);
        sb2.append(" mAh; MobileTotalRxPacketsPower = ");
        sb2.append(this.f16959n.E);
        sb2.append(" mAh; MobileTotalTxPacketsPower = ");
        sb2.append(this.f16959n.F);
        sb2.append(" mAh; MobileTotalTotalPower = ");
        sb2.append(this.f16959n.f17026b0);
        sb2.append(" mAh");
        LocalLog.b("AppStats", sb2.toString());
    }

    private void o(UidSipper uidSipper, NetworkStatsHistory.Entry entry, boolean z10, boolean z11, long j10, boolean z12) {
        this.f16959n.d(uidSipper, entry, z10, z11, z12);
        if (this.f16947b.equals("com.android.launcher") || !f16945r) {
            return;
        }
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; FgMobileRxBytes = " + this.f16959n.f17053y + " Bytes; BgMobileRxBytes = " + this.f16959n.f17054z + " Bytes; FgMobileTxBytes = " + this.f16959n.A + " Bytes; BgMobileTxBytes = " + this.f16959n.B + " Bytes; FgMobileRxPackets = " + this.f16959n.f17041m + " ; BgMobileRxPackets = " + this.f16959n.f17042n + " ; FgMobileTxPackets = " + this.f16959n.f17043o + " ; BgMobileTxPackets = " + this.f16959n.f17044p + " ; FgMobileActiveCount = " + this.f16959n.W + " ; BgMobileActiveCount = " + this.f16959n.X + " ; FgMobileActiveTime = " + this.f16959n.Y + " ; BgMobileActiveTime = " + this.f16959n.Z + " ");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Uid = ");
        sb2.append(this.f16948c);
        sb2.append("; pkgName = ");
        sb2.append(this.f16947b);
        sb2.append("; FgMobileRxPacketsPower = ");
        sb2.append(this.f16959n.K);
        sb2.append(" mAh; BgMobileRxPacketsPower = ");
        sb2.append(this.f16959n.L);
        sb2.append(" mAh; FgMobileTxPacketsPower = ");
        sb2.append(this.f16959n.M);
        sb2.append(" mAh; BgMobileTxPacketsPower = ");
        sb2.append(this.f16959n.N);
        sb2.append(" mAh; FgMobileTotalPower = ");
        sb2.append(this.f16959n.f17028c0);
        sb2.append(" mAh; BgMobileTotalPower = ");
        sb2.append(this.f16959n.f17030d0);
        sb2.append(" mAh.");
        LocalLog.b("AppStats", sb2.toString());
    }

    private void p(boolean z10, boolean z11, long j10, UidSipper uidSipper) {
        this.f16957l.b(j10, z10, z11, uidSipper);
        this.f16957l.a(z10, z10);
        if (this.f16947b.equals("com.android.launcher") || !f16945r) {
            return;
        }
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; ScreenFgTime = " + this.f16957l.f17061e + " ms; ScreenFgPower = " + this.f16957l.f17060d + " mAh.");
    }

    private void q(UidSipper uidSipper, boolean z10) {
        this.f16960o.b(uidSipper, z10);
        if (this.f16947b.equals("com.android.launcher") || !f16945r) {
            return;
        }
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; GpsTime = " + this.f16960o.f17065c + " ms; GpsTotalTime = " + this.f16960o.f17067e + " ms");
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; GpsPower = " + this.f16960o.f17066d + " mAh; GpsTotalPower = " + this.f16960o.f17068f + " mAh");
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; SensorTime = " + this.f16960o.f17077o + " ms; SensorTotalTime = " + this.f16960o.f17079q + " ms");
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; SensorPower = " + this.f16960o.f17078p + " mAh; SensorTotalPower = " + this.f16960o.f17080r + " mAh");
    }

    private void r(UidSipper uidSipper, boolean z10, boolean z11, boolean z12) {
        this.f16960o.c(uidSipper, z10, z11, z12);
        if (this.f16947b.equals("com.android.launcher") || !f16945r) {
            return;
        }
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; FgGpsTime = " + this.f16960o.f17071i + " ms; BgGpsTime = " + this.f16960o.f17075m + " ms.");
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; FgGpsPower = " + this.f16960o.f17072j + " mAh; BgGpsPower = " + this.f16960o.f17076n + " mAh.");
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; FgSensorTime = " + this.f16960o.f17083u + " ms; BgSensorTime = " + this.f16960o.f17087y + " ms.");
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; FgSensorPower = " + this.f16960o.f17084v + " mAh; BgSensorPower = " + this.f16960o.f17088z + " mAh.");
    }

    private void s(UidSipper uidSipper, boolean z10) {
        this.f16955j.b(uidSipper, z10);
        if (this.f16947b.equals("com.android.launcher") || !f16945r) {
            return;
        }
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; SyncCount = " + this.f16955j.f17089b + "; SyncTime = " + this.f16955j.f17091d + "; SyncTotalCount = " + this.f16955j.f17090c + "; SyncTotalTime = " + this.f16955j.f17092e);
    }

    private void t(UidSipper uidSipper, boolean z10, boolean z11, boolean z12) {
        this.f16955j.c(uidSipper, z10, z11, z12);
        if (this.f16947b.equals("com.android.launcher") || !f16945r) {
            return;
        }
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; SyncFgCount = " + this.f16955j.f17094g + "; SyncBgCount = " + this.f16955j.f17098k + "; SyncFgTime = " + this.f16955j.f17096i + "; SyncBgTime = " + this.f16955j.f17100m);
    }

    private void u(UidSipper uidSipper, boolean z10) {
        this.f16956k.b(uidSipper, z10);
        if (this.f16947b.equals("com.android.launcher") || !f16945r) {
            return;
        }
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; WakeLockTime = " + this.f16956k.f17107h + " ms; WakeLockPower = " + this.f16956k.f17108i + " mAh.; WakeLockTotalTime = " + this.f16956k.f17109j + " ms; WakeLockTotalPower = " + this.f16956k.f17110k + " mAh.");
    }

    private void v(UidSipper uidSipper, boolean z10, boolean z11, boolean z12) {
        this.f16956k.c(uidSipper, z10, z11, z12, this.f16947b);
        if (this.f16947b.equals("com.android.launcher") || !f16945r) {
            return;
        }
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; WakeLockFgTime = " + this.f16956k.f17102c + " ms; WakeLockFgPower = " + this.f16956k.f17103d + " mAh.; WakeLockBgTime = " + this.f16956k.f17105f + " ms; WakeLockBgPower = " + this.f16956k.f17106g + " mAh.");
    }

    private void w(UidSipper uidSipper, NetworkStatsHistory.Entry entry, boolean z10) {
        this.f16958m.b(uidSipper, entry, this.f16948c, z10);
        if (this.f16947b.equals("com.android.launcher")) {
            return;
        }
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; WifiRxBytes = " + this.f16958m.f17149t + " Bytes; WifiTxBytes = " + this.f16958m.f17151u + " Bytes; WifiRxPackets = " + this.f16958m.f17125h + " ; WifiTxPackets = " + this.f16958m.f17127i + " ; WiFiScanTimeMs = " + this.f16958m.R + " ; WiFiBatchScanTimeMs = " + this.f16958m.S + " ");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Uid = ");
        sb2.append(this.f16948c);
        sb2.append("; pkgName = ");
        sb2.append(this.f16947b);
        sb2.append("; WifiRxPacketsPower = ");
        sb2.append(this.f16958m.F);
        sb2.append(" mAh; WifiTxPacketsPower = ");
        sb2.append(this.f16958m.G);
        sb2.append(" mAh; WifiScanPower = ");
        sb2.append(this.f16958m.f17118d0);
        sb2.append(" mAh; WifiBatchScanPower = ");
        sb2.append(this.f16958m.f17120e0);
        sb2.append(" mAh; WiFiTotalPower = ");
        sb2.append(this.f16958m.f17142p0);
        sb2.append(" mAh");
        LocalLog.b("AppStats", sb2.toString());
    }

    private void x(UidSipper uidSipper, NetworkStatsHistory.Entry entry, long j10, boolean z10, boolean z11, boolean z12) {
        this.f16958m.c(uidSipper, entry, j10, this.f16948c, z10, z11, z12);
        if (this.f16947b.equals("com.android.launcher") || !f16945r) {
            return;
        }
        LocalLog.b("AppStats", "Uid = " + this.f16948c + "; pkgName = " + this.f16947b + "; FgWifiRxBytes = " + this.f16958m.B + " Bytes; BgWifiRxBytes = " + this.f16958m.C + " Bytes; FgWifiTxBytes = " + this.f16958m.D + " Bytes; BgWifiTxBytes = " + this.f16958m.E + " Bytes; FgWifiRxPackets = " + this.f16958m.f17141p + " ; BgWifiRxPackets = " + this.f16958m.f17143q + " ; FgWifiTxPackets = " + this.f16958m.f17145r + " ; BgWifiTxPackets = " + this.f16958m.f17147s + " ; FgWiFiScanTimeMs = " + this.f16958m.Z + " ; BgWiFiScanTimeMs = " + this.f16958m.f17112a0 + " ; FgWiFiBatchScanTimeMs = " + this.f16958m.f17114b0 + " ; BgWiFiBatchScanTimeMs = " + this.f16958m.f17116c0 + " ");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Uid = ");
        sb2.append(this.f16948c);
        sb2.append("; pkgName = ");
        sb2.append(this.f16947b);
        sb2.append("; FgWifiRxPacketsPower = ");
        sb2.append(this.f16958m.N);
        sb2.append(" mAh; BgWifiRxPacketsPower = ");
        sb2.append(this.f16958m.O);
        sb2.append(" mAh; FgWifiTxPacketsPower = ");
        sb2.append(this.f16958m.P);
        sb2.append(" mAh; BgWifiTxPacketsPower = ");
        sb2.append(this.f16958m.Q);
        sb2.append(" mAh; FgWifiScanPower = ");
        sb2.append(this.f16958m.f17134l0);
        sb2.append(" mAh; BgWifiScanPower = ");
        sb2.append(this.f16958m.f17136m0);
        sb2.append(" mAh; FgWifiBatchScanPower = ");
        sb2.append(this.f16958m.f17138n0);
        sb2.append(" mAh; BgWifiBatchScanPower = ");
        sb2.append(this.f16958m.f17140o0);
        sb2.append(" mAh; FgWiFiTotalPower = ");
        sb2.append(this.f16958m.f17150t0);
        sb2.append(" mAh; BgWiFiTotalPower = ");
        sb2.append(this.f16958m.f17152u0);
        sb2.append(" mAh.");
        LocalLog.b("AppStats", sb2.toString());
    }

    public void A(boolean z10) {
        this.f16949d = z10;
    }

    public void a() {
        LocalLog.a("AppStats", "clearStatsBeforeRefreshBatteryStats");
        this.f16952g.a();
        this.f16953h.a();
        this.f16954i.a();
        this.f16955j.a();
        this.f16956k.a();
        this.f16957l.c();
        this.f16957l.c();
        this.f16958m.a();
        this.f16959n.a();
        this.f16960o.a();
        this.f16961p.a();
        this.f16962q.a();
    }

    public void d(UidSipper uidSipper, NetworkStatsHistory.Entry entry, NetworkStatsHistory.Entry entry2, boolean z10) {
        this.f16950e = false;
        if (uidSipper == null) {
            LocalLog.b("AppStats", "uid sipper is null in screen off !");
            return;
        }
        h(uidSipper, z10);
        b(uidSipper, z10);
        l(uidSipper, z10);
        s(uidSipper, z10);
        u(uidSipper, z10);
        if (entry != null) {
            w(uidSipper, entry, z10);
        }
        if (entry2 != null) {
            n(uidSipper, entry2, z10);
        }
        q(uidSipper, z10);
        f(uidSipper, z10);
        j(uidSipper, z10);
    }

    public void e(UidSipper uidSipper, NetworkStatsHistory.Entry entry, NetworkStatsHistory.Entry entry2, boolean z10, boolean z11, boolean z12, boolean z13) {
        long elapsedRealtime = SystemClock.elapsedRealtime() * 1000;
        this.f16950e = true;
        if (uidSipper == null) {
            LocalLog.b("AppStats", "uid sipper is null in screen on !");
            return;
        }
        i(uidSipper, z10, z11, z13);
        c(uidSipper, z10, z11, z13);
        m(uidSipper, z10, z11, z13);
        t(uidSipper, z10, z11, z13);
        v(uidSipper, z10, z11, z13);
        if (!z12) {
            p(z10, z11, elapsedRealtime, uidSipper);
        }
        if (entry != null) {
            x(uidSipper, entry, elapsedRealtime, z10, z11, z13);
        }
        if (entry2 != null) {
            o(uidSipper, entry2, z10, z11, elapsedRealtime, z13);
        }
        r(uidSipper, z10, z11, z13);
        g(uidSipper, z10, z11, z13);
        k(uidSipper, z10, z11, z13);
    }

    public void y() {
        LocalLog.a("AppStats", "resetStatsAfterReportDCS");
        this.f16952g.h();
        this.f16953h.e();
        this.f16954i.d();
        this.f16955j.d();
        this.f16956k.d();
        this.f16957l.d();
        this.f16957l.d();
        this.f16958m.e();
        this.f16959n.e();
        this.f16960o.d();
        this.f16961p.d();
        this.f16962q.d();
    }

    public void z(boolean z10) {
        this.f16951f = z10;
    }
}
