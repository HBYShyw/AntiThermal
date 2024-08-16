package u8;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import b6.LocalLog;
import com.android.internal.app.IBatteryStats;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import e6.SmartModeSharepref;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import p9.PowerMonitor;
import q9.AppStats;
import q9.Sensor;

/* compiled from: GPSPowerIssue.java */
/* renamed from: u8.n, reason: use source file name */
/* loaded from: classes2.dex */
public class GPSPowerIssue extends BasicPowerIssue {
    public GPSPowerIssue(Context context, String str, int i10, boolean z10, boolean z11) {
        super(context, str, i10, z10, z11);
    }

    private long r(double d10) {
        return (long) (d10 * 300000.0d);
    }

    private boolean s() {
        return ((UserManager) this.f18919i.getSystemService("user")).hasUserRestriction("no_share_location");
    }

    @Override // u8.BasicPowerIssue
    public double a(IBatteryStats iBatteryStats, List<ActivityManager.RunningAppProcessInfo> list, List<ActivityManager.RunningAppProcessInfo> list2, double d10, long j10) {
        long j11;
        boolean s7 = s();
        double d11 = UserProfileInfo.Constant.NA_LAT_LON;
        if (s7) {
            return UserProfileInfo.Constant.NA_LAT_LON;
        }
        if (Settings.Secure.getInt(this.f18919i.getContentResolver(), "location_mode", 0) != 0) {
            LocalLog.a("GPSPowerIssue", "gps is on");
            SystemClock.uptimeMillis();
            for (Map.Entry<String, AppStats> entry : PowerMonitor.j().entrySet()) {
                entry.getKey();
                Sensor sensor = entry.getValue().f16960o;
                d11 += sensor.f17075m + sensor.f17067e;
            }
            LocalLog.a("GPSPowerIssue", "user stats sumGps = " + d11);
            try {
                j11 = ((Long) Class.forName("com.android.internal.os.BatteryStatsImpl").getMethod("getBatteryUptime", Long.TYPE).invoke(Class.forName("com.android.internal.os.BatteryStatsImpl").getConstructor(new Class[0]).newInstance(new Object[0]), Long.valueOf(System.currentTimeMillis()))).longValue();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e10) {
                e10.printStackTrace();
                LocalLog.l("GPSPowerIssue", "getBatteryUptime fail!");
                j11 = 0;
            }
            long j12 = j11 / 60000;
            double d12 = j10;
            double d13 = d12 / 60.0d;
            double ceil = d13 >= 1.0d ? Math.ceil(d13) : 1.0d;
            if (j12 != 0) {
                d11 *= d12 / j12;
            }
            if (d11 < 300000.0d * ceil) {
                d11 = r(ceil);
            }
            return (d11 / 3600000.0d) * 60.0d;
        }
        LocalLog.a("GPSPowerIssue", "gps is off");
        return UserProfileInfo.Constant.NA_LAT_LON;
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
        return t(0);
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

    public boolean t(int i10) {
        if (s()) {
            return false;
        }
        int intForUser = Settings.Secure.getIntForUser(this.f18919i.getContentResolver(), "location_mode", 0, UserHandle.myUserId());
        Intent intent = new Intent("com.android.settings.location.MODE_CHANGING");
        intent.putExtra("CURRENT_MODE", intForUser);
        intent.putExtra("NEW_MODE", i10);
        this.f18919i.sendBroadcast(intent, "android.permission.WRITE_SECURE_SETTINGS");
        try {
            Settings.Secure.putIntForUser(this.f18919i.getContentResolver(), "location_mode", i10, UserHandle.myUserId());
            this.f18916f.a0();
            return true;
        } catch (Exception e10) {
            LocalLog.a("GPSPowerIssue", "setLocationMode, failed to put mode: " + i10 + " to secure key: location_mode. The mode value maybe illegal!!");
            e10.printStackTrace();
            return false;
        }
    }
}
