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

/* compiled from: CurvedDisplayPowerIssue.java */
/* renamed from: u8.j, reason: use source file name */
/* loaded from: classes2.dex */
public class CurvedDisplayPowerIssue extends BasicPowerIssue {
    public CurvedDisplayPowerIssue(Context context, String str, int i10, boolean z10, boolean z11) {
        super(context, str, i10, z10, z11);
    }

    private int r(String str) {
        int intForUser = Settings.Secure.getIntForUser(this.f18919i.getContentResolver(), str, 0, -2);
        LocalLog.l("CurvedDisplayIssue", str + ":" + intForUser);
        return intForUser;
    }

    private String s(String str) {
        String stringForUser = Settings.Secure.getStringForUser(this.f18919i.getContentResolver(), str, -2);
        LocalLog.l("CurvedDisplayIssue", str + ":" + stringForUser);
        return stringForUser;
    }

    private boolean t() {
        String s7;
        try {
            if (!(r("oplus_customize_aod_curved_display_notification_switch") == 1) || (s7 = s("oplus_customize_comm_incallui_curved_display_notification_color")) == null || "".equals(s7) || "null".equals(s7)) {
                return true;
            }
            return "none".equals(s7);
        } catch (Exception e10) {
            e10.printStackTrace();
            return true;
        }
    }

    private boolean u() {
        if (t()) {
            return false;
        }
        try {
            Settings.Secure.putIntForUser(this.f18919i.getContentResolver(), "oplus_customize_aod_curved_display_notification_switch", 0, -2);
            Settings.Secure.putStringForUser(this.f18919i.getContentResolver(), "oplus_customize_comm_incallui_curved_display_notification_color", "", -2);
            this.f18916f.Y();
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
        return 12.0d;
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
        return this.f18914d && this.f18915e;
    }

    @Override // u8.BasicPowerIssue
    public boolean o() {
        return y5.b.h();
    }
}
