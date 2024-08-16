package u8;

import a7.PowerConsumeStats;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import com.android.internal.app.IBatteryStats;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import f6.CommonUtil;
import java.util.ArrayList;
import java.util.List;
import t8.PowerUsageManager;

/* compiled from: HighPerformIssue.java */
/* renamed from: u8.o, reason: use source file name */
/* loaded from: classes2.dex */
public class HighPerformIssue extends BasicPowerIssue {
    public HighPerformIssue(Context context, String str, int i10, boolean z10, boolean z11) {
        super(context, str, i10, z10, z11);
    }

    @Override // u8.BasicPowerIssue
    public double a(IBatteryStats iBatteryStats, List<ActivityManager.RunningAppProcessInfo> list, List<ActivityManager.RunningAppProcessInfo> list2, double d10, long j10) {
        return UserProfileInfo.Constant.NA_LAT_LON;
    }

    @Override // u8.BasicPowerIssue
    public boolean c() {
        Log.d("HighPerformIssue", "doOptimization");
        if (g() != 1) {
            return false;
        }
        CommonUtil.b(this.f18919i);
        this.f18916f.b0();
        l(0);
        return true;
    }

    @Override // u8.BasicPowerIssue
    public void i(IBatteryStats iBatteryStats, ArrayList<PowerConsumeStats.b> arrayList, double d10, long j10) {
        long j11;
        int A = CommonUtil.A();
        Log.d("HighPerformIssue", "parse calcPower mode=" + A);
        if (A > 0) {
            j11 = PowerUsageManager.x(this.f18919i).w();
            Log.d("HighPerformIssue", "parse calcPower timeInMinute=" + j11);
            this.f18917g = j11;
        } else {
            j11 = 0;
        }
        Log.d("HighPerformIssue", "key = " + e() + ", timeInMinute = " + j11 + ", mPowerIssueTime = " + this.f18917g);
        if (this.f18917g > 0) {
            this.f18918h = 1;
        } else {
            this.f18918h = 0;
        }
    }

    @Override // u8.BasicPowerIssue
    public void j(IBatteryStats iBatteryStats, List<ActivityManager.RunningAppProcessInfo> list, List<ActivityManager.RunningAppProcessInfo> list2, double d10, long j10) {
        long j11;
        int A = CommonUtil.A();
        Log.d("HighPerformIssue", "parse calcPower mode=" + A);
        if (A > 0) {
            j11 = PowerUsageManager.x(this.f18919i).w();
            Log.d("HighPerformIssue", "parse calcPower timeInMinute=" + j11);
            this.f18917g = j11;
        } else {
            this.f18917g = 0L;
            j11 = 0;
        }
        Log.d("HighPerformIssue", "key = " + e() + ", timeInMinute = " + j11 + ", mPowerIssueTime = " + this.f18917g);
        if (this.f18917g > 0) {
            this.f18918h = 1;
        } else {
            this.f18918h = 0;
        }
    }

    @Override // u8.BasicPowerIssue
    public boolean o() {
        Log.d("HighPerformIssue", "support=" + y5.b.n());
        return y5.b.n();
    }
}
