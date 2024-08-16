package u8;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.net.TetheringManager;
import android.net.wifi.WifiClient;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.HandlerThread;
import android.os.SystemClock;
import b6.LocalLog;
import com.android.internal.app.IBatteryStats;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import com.oplus.thermalcontrol.config.policy.ThermalPolicy;
import e6.SmartModeSharepref;
import java.util.List;

/* compiled from: HotSpotPowerIssue.java */
/* renamed from: u8.p, reason: use source file name */
/* loaded from: classes2.dex */
public class HotSpotPowerIssue extends BasicPowerIssue {

    /* renamed from: m, reason: collision with root package name */
    private WifiManager f18924m;

    /* renamed from: n, reason: collision with root package name */
    private TetheringManager f18925n;

    /* renamed from: o, reason: collision with root package name */
    private Boolean f18926o;

    /* renamed from: p, reason: collision with root package name */
    private Handler f18927p;

    /* renamed from: q, reason: collision with root package name */
    private WifiManager.SoftApCallback f18928q;

    /* compiled from: HotSpotPowerIssue.java */
    /* renamed from: u8.p$a */
    /* loaded from: classes2.dex */
    class a implements WifiManager.SoftApCallback {
        a() {
        }

        public void onConnectedClientsChanged(List<WifiClient> list) {
            HotSpotPowerIssue.this.f18926o = Boolean.FALSE;
            LocalLog.l("HotSpotIssue", "clients.size(): " + list.size());
            if (list.size() == 0) {
                HotSpotPowerIssue.this.f18926o = Boolean.TRUE;
            }
            LocalLog.l("HotSpotIssue", "clients.size(): mIsShow" + HotSpotPowerIssue.this.f18926o);
        }
    }

    @SuppressLint({"WrongConstant"})
    public HotSpotPowerIssue(Context context, String str, int i10, boolean z10, boolean z11) {
        super(context, str, i10, z10, z11);
        this.f18926o = Boolean.FALSE;
        this.f18928q = new a();
        this.f18924m = (WifiManager) this.f18919i.getSystemService(ThermalPolicy.KEY_WIFI);
        this.f18925n = (TetheringManager) this.f18919i.getSystemService("tethering");
        HandlerThread handlerThread = new HandlerThread("HotSpotPowerIssue");
        handlerThread.start();
        this.f18927p = new Handler(handlerThread.getLooper());
        this.f18924m.registerSoftApCallback(new HandlerExecutor(this.f18927p), this.f18928q);
    }

    private boolean t() {
        return !this.f18924m.isWifiApEnabled();
    }

    private boolean u() {
        if (t()) {
            return false;
        }
        try {
            this.f18925n.stopTethering(0);
            this.f18916f.c0();
            this.f18926o = Boolean.TRUE;
            return true;
        } catch (Exception e10) {
            e10.printStackTrace();
            return false;
        }
    }

    @Override // u8.BasicPowerIssue
    public double a(IBatteryStats iBatteryStats, List<ActivityManager.RunningAppProcessInfo> list, List<ActivityManager.RunningAppProcessInfo> list2, double d10, long j10) {
        if (t()) {
            return UserProfileInfo.Constant.NA_LAT_LON;
        }
        return 16.666666666666668d;
    }

    @Override // u8.BasicPowerIssue
    public boolean c() {
        if (g() != 1) {
            return false;
        }
        this.f18913c = SystemClock.elapsedRealtime();
        this.f18912b = System.currentTimeMillis();
        l(3);
        SmartModeSharepref.e(this.f18919i, e(), this.f18912b);
        return u();
    }

    @Override // u8.BasicPowerIssue
    public void j(IBatteryStats iBatteryStats, List<ActivityManager.RunningAppProcessInfo> list, List<ActivityManager.RunningAppProcessInfo> list2, double d10, long j10) {
        super.j(iBatteryStats, list, list2, d10, j10);
        if (this.f18917g > 0) {
            this.f18918h = 2;
        } else {
            this.f18918h = 0;
        }
    }

    @Override // u8.BasicPowerIssue
    public boolean n() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("shouldShow: ");
        sb2.append(this.f18926o.booleanValue() && this.f18914d && this.f18915e);
        LocalLog.l("HotSpotIssue", sb2.toString());
        return this.f18926o.booleanValue() && this.f18914d && this.f18915e;
    }
}
