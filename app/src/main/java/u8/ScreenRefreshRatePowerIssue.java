package u8;

import android.app.ActivityManager;
import android.content.Context;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.Display;
import b6.LocalLog;
import com.android.internal.app.IBatteryStats;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import e6.SmartModeSharepref;
import java.util.List;

/* compiled from: ScreenRefreshRatePowerIssue.java */
/* renamed from: u8.s, reason: use source file name */
/* loaded from: classes2.dex */
public class ScreenRefreshRatePowerIssue extends BasicPowerIssue {

    /* renamed from: m, reason: collision with root package name */
    private final Display f18930m;

    public ScreenRefreshRatePowerIssue(Context context, String str, int i10, boolean z10, boolean z11) {
        super(context, str, i10, z10, z11);
        this.f18930m = this.f18919i.getDisplay();
    }

    private int r(String str) {
        int intForUser = Settings.Secure.getIntForUser(this.f18919i.getContentResolver(), str, 0, 0);
        LocalLog.l("ScreenRefreshRateIssue", str + ":" + intForUser);
        return intForUser;
    }

    private boolean s() {
        return r("oplus_customize_screen_refresh_rate") == 2;
    }

    private boolean t() {
        if (s()) {
            return false;
        }
        try {
            Settings.Secure.putIntForUser(this.f18919i.getContentResolver(), "oplus_customize_screen_refresh_rate", 2, 0);
            this.f18916f.e0();
            return true;
        } catch (Exception e10) {
            e10.printStackTrace();
            return false;
        }
    }

    @Override // u8.BasicPowerIssue
    public double a(IBatteryStats iBatteryStats, List<ActivityManager.RunningAppProcessInfo> list, List<ActivityManager.RunningAppProcessInfo> list2, double d10, long j10) {
        if (s()) {
            return UserProfileInfo.Constant.NA_LAT_LON;
        }
        int r10 = r("oplus_customize_screen_refresh_rate");
        return ((r10 != 1 ? r10 != 3 ? r10 != 4 ? 180.0d : 675.0d : 575.0d : 220.0d) / 1440.0d) * j10;
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
        return t();
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
    public boolean o() {
        if (!y5.b.t()) {
            return false;
        }
        for (float f10 : this.f18930m.getSupportedRefreshRates()) {
            if (Math.round(f10 * 10.0f) / 10 == 60) {
                return true;
            }
        }
        return false;
    }
}
