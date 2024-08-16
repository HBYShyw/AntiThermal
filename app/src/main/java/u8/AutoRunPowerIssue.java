package u8;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.text.TextUtils;
import com.android.internal.app.IBatteryStats;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import e6.SmartModeSharepref;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import t8.AutoStartUtil;

/* compiled from: AutoRunPowerIssue.java */
/* renamed from: u8.f, reason: use source file name */
/* loaded from: classes2.dex */
public class AutoRunPowerIssue extends BasicPowerIssue {

    /* renamed from: m, reason: collision with root package name */
    private AutoStartUtil f18901m;

    /* renamed from: n, reason: collision with root package name */
    private Object f18902n;

    /* renamed from: o, reason: collision with root package name */
    private List<String> f18903o;

    public AutoRunPowerIssue(Context context, String str, int i10, boolean z10, boolean z11) {
        super(context, str, i10, z10, z11);
        this.f18901m = new AutoStartUtil();
        this.f18902n = new Object();
        this.f18903o = new ArrayList();
    }

    private List<String> r(Context context) {
        return this.f18901m.a(context, "powerOptimizationGetAutoStartList");
    }

    private boolean s(Context context, ArrayList<String> arrayList) {
        return TextUtils.equals("false", this.f18901m.b(context, "powerOptimizationSetAutoStartList", arrayList));
    }

    @Override // u8.BasicPowerIssue
    public double a(IBatteryStats iBatteryStats, List<ActivityManager.RunningAppProcessInfo> list, List<ActivityManager.RunningAppProcessInfo> list2, double d10, long j10) {
        double size;
        if (list.size() + list2.size() == 0) {
            return UserProfileInfo.Constant.NA_LAT_LON;
        }
        synchronized (this.f18902n) {
            this.f18903o = r(this.f18919i);
            size = (r6.size() / (list.size() + list2.size())) * d10 * 0.3333333333333333d * 0.05d;
        }
        return size;
    }

    @Override // u8.BasicPowerIssue
    public boolean c() {
        if (g() == 1) {
            this.f18913c = SystemClock.elapsedRealtime();
            this.f18912b = System.currentTimeMillis();
            l(3);
            ArrayList<String> arrayList = new ArrayList<>();
            synchronized (this.f18902n) {
                arrayList.addAll(this.f18903o);
            }
            SharedPreferences sharedPreferences = this.f18919i.getSharedPreferences("power_settings_is_changed", 0);
            Iterator<String> it = arrayList.iterator();
            while (it.hasNext()) {
                String next = it.next();
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putBoolean(next + "autoStart", false);
                edit.apply();
            }
            s(this.f18919i, arrayList);
            SmartModeSharepref.e(this.f18919i, e(), this.f18912b);
        }
        return true;
    }

    @Override // u8.BasicPowerIssue
    public void j(IBatteryStats iBatteryStats, List<ActivityManager.RunningAppProcessInfo> list, List<ActivityManager.RunningAppProcessInfo> list2, double d10, long j10) {
        super.j(iBatteryStats, list, list2, d10, j10);
        if (this.f18917g > 0) {
            this.f18918h = 1;
        } else {
            this.f18918h = 0;
        }
        this.f18917g = 0L;
    }
}
