package u8;

import android.app.ActivityManager;
import android.app.UiModeManager;
import android.content.Context;
import android.os.SystemClock;
import com.android.internal.app.IBatteryStats;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import e6.SmartModeSharepref;
import java.util.List;

/* compiled from: DarkModePowerIssue.java */
/* renamed from: u8.k, reason: use source file name */
/* loaded from: classes2.dex */
public class DarkModePowerIssue extends BasicPowerIssue {

    /* renamed from: m, reason: collision with root package name */
    UiModeManager f18923m;

    public DarkModePowerIssue(Context context, String str, int i10, boolean z10, boolean z11) {
        super(context, str, i10, z10, z11);
        this.f18923m = null;
        this.f18923m = (UiModeManager) this.f18919i.getSystemService("uimode");
    }

    private boolean r() {
        return (this.f18919i.getResources().getConfiguration().uiMode & 48) == 32;
    }

    private boolean s() {
        if (this.f18923m == null) {
            return false;
        }
        this.f18916f.Z();
        this.f18923m.setNightModeActivated(true);
        return true;
    }

    @Override // u8.BasicPowerIssue
    public double a(IBatteryStats iBatteryStats, List<ActivityManager.RunningAppProcessInfo> list, List<ActivityManager.RunningAppProcessInfo> list2, double d10, long j10) {
        if (r()) {
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
        return s();
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
}
