package u8;

import android.app.ActivityManager;
import android.content.Context;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.Settings;
import b6.LocalLog;
import com.android.internal.app.IBatteryStats;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import e6.SmartModeSharepref;
import java.util.List;

/* compiled from: EarthquakeWarningPowerIssue.java */
/* renamed from: u8.l, reason: use source file name */
/* loaded from: classes2.dex */
public class EarthquakeWarningPowerIssue extends BasicPowerIssue {
    public EarthquakeWarningPowerIssue(Context context, String str, int i10, boolean z10, boolean z11) {
        super(context, str, i10, z10, z11);
    }

    private int r(String str) {
        try {
            int intForUser = Settings.System.getIntForUser(this.f18919i.getContentResolver(), str, -1, UserHandle.myUserId());
            LocalLog.l("EarthquakeWarningPowerIssue", str + ":" + intForUser);
            return intForUser;
        } catch (NumberFormatException unused) {
            return 0;
        }
    }

    private boolean s() {
        return r("setting_key_com_oplus_sos_earthquake_warning") != 1;
    }

    private boolean t() {
        if (s()) {
            return false;
        }
        try {
            Settings.System.putIntForUser(this.f18919i.getContentResolver(), "setting_key_com_oplus_sos_earthquake_warning", 0, UserHandle.myUserId());
            this.f18916f.f0();
            return true;
        } catch (NumberFormatException unused) {
            return false;
        }
    }

    @Override // u8.BasicPowerIssue
    public double a(IBatteryStats iBatteryStats, List<ActivityManager.RunningAppProcessInfo> list, List<ActivityManager.RunningAppProcessInfo> list2, double d10, long j10) {
        if (s()) {
            return UserProfileInfo.Constant.NA_LAT_LON;
        }
        return 1.0d;
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
        return this.f18914d && this.f18915e;
    }

    @Override // u8.BasicPowerIssue
    public boolean o() {
        return r("setting_key_com_oplus_sos_earthquake_warning") != -1;
    }
}
