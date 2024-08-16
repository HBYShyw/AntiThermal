package u8;

import a7.PowerConsumeStats;
import android.app.ActivityManager;
import android.os.SystemClock;
import android.util.ArrayMap;
import b6.LocalLog;
import com.android.internal.app.IBatteryStats;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import t8.MessengerUtil;

/* compiled from: AbnormalAppsPowerIssue.java */
/* renamed from: u8.c, reason: use source file name */
/* loaded from: classes2.dex */
public class AbnormalAppsPowerIssue extends BasicPowerIssue {

    /* renamed from: m, reason: collision with root package name */
    private ArrayList<PowerConsumeStats.b> f18891m;

    /* renamed from: n, reason: collision with root package name */
    private ArrayMap<String, MessengerUtil> f18892n;

    /* renamed from: o, reason: collision with root package name */
    private String f18893o;

    @Override // u8.BasicPowerIssue
    public double a(IBatteryStats iBatteryStats, List<ActivityManager.RunningAppProcessInfo> list, List<ActivityManager.RunningAppProcessInfo> list2, double d10, long j10) {
        return UserProfileInfo.Constant.NA_LAT_LON;
    }

    @Override // u8.BasicPowerIssue
    public boolean c() {
        if (g() != 1) {
            return false;
        }
        String str = this.f18893o;
        if (str != null) {
            s(str);
        }
        this.f18913c = SystemClock.elapsedRealtime();
        this.f18912b = System.currentTimeMillis();
        l(3);
        ActivityManager activityManager = (ActivityManager) this.f18919i.getSystemService("activity");
        synchronized (this.f18891m) {
            Iterator<PowerConsumeStats.b> it = this.f18891m.iterator();
            while (it.hasNext()) {
                activityManager.killUid(it.next().f75e, "abnormal kill");
            }
            this.f18891m.clear();
        }
        return false;
    }

    @Override // u8.BasicPowerIssue
    public boolean m() {
        return true;
    }

    @Override // u8.BasicPowerIssue
    public boolean o() {
        return false;
    }

    @Override // u8.BasicPowerIssue
    public boolean q() {
        return true;
    }

    public ArrayList<PowerConsumeStats.b> r() {
        ArrayList<PowerConsumeStats.b> arrayList;
        synchronized (this.f18891m) {
            arrayList = this.f18891m;
        }
        return arrayList;
    }

    public void s(String str) {
        LocalLog.a("AbnormalAppsPowerIssue", "sendDoOptimizationMsg");
        MessengerUtil messengerUtil = this.f18892n.get(str);
        if (messengerUtil != null) {
            messengerUtil.e();
        }
    }
}
