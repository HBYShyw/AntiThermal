package u8;

import android.app.ActivityManager;
import android.content.Context;
import android.os.SystemClock;
import android.provider.Settings;
import b6.LocalLog;
import com.android.internal.app.IBatteryStats;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import e6.SmartModeSharepref;
import java.util.List;
import y5.AppFeature;

/* compiled from: FiveGPowerIssue.java */
/* renamed from: u8.m, reason: use source file name */
/* loaded from: classes2.dex */
public class FiveGPowerIssue extends BasicPowerIssue {
    public FiveGPowerIssue(Context context, String str, int i10, boolean z10, boolean z11) {
        super(context, str, i10, z10, z11);
    }

    private int r(String str) {
        try {
            int intForUser = Settings.System.getIntForUser(this.f18919i.getContentResolver(), str, 0);
            LocalLog.l("FiveGPowerIssue", str + ":" + intForUser);
            return intForUser;
        } catch (Settings.SettingNotFoundException e10) {
            e10.printStackTrace();
            return 0;
        }
    }

    private boolean s() {
        return r("oplus_show_fiveg_status") == 0;
    }

    @Override // u8.BasicPowerIssue
    public double a(IBatteryStats iBatteryStats, List<ActivityManager.RunningAppProcessInfo> list, List<ActivityManager.RunningAppProcessInfo> list2, double d10, long j10) {
        if (s()) {
            return UserProfileInfo.Constant.NA_LAT_LON;
        }
        try {
            Settings.System.putIntForUser(this.f18919i.getContentResolver(), "oplus_one_key_power_save_state", 0, 0);
        } catch (Exception e10) {
            LocalLog.a("FiveGPowerIssue", "reset one key power save state fail!");
            e10.printStackTrace();
        }
        return d10 * 0.057499999999999996d;
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
        this.f18917g = 0L;
    }

    @Override // u8.BasicPowerIssue
    public boolean n() {
        return r("oplus_support_fiveg_status") == 1 && this.f18914d && this.f18915e && !AppFeature.i();
    }

    @Override // u8.BasicPowerIssue
    public boolean o() {
        return true;
    }

    public boolean t() {
        if (s()) {
            LocalLog.d("FiveGPowerIssue", "Restricted user, not fg mode");
            return false;
        }
        try {
            Settings.System.putIntForUser(this.f18919i.getContentResolver(), "oplus_one_key_power_save_state", 1, 0);
            this.f18916f.T();
            return true;
        } catch (Exception e10) {
            LocalLog.b("FiveGPowerIssue", "set one key power save state fail!");
            e10.printStackTrace();
            return false;
        }
    }
}
