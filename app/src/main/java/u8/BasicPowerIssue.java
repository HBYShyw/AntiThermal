package u8;

import a7.PowerConsumeStats;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import b6.LocalLog;
import com.android.internal.app.IBatteryStats;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import e6.SmartModeSharepref;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import x5.UploadDataUtil;

/* compiled from: BasicPowerIssue.java */
/* renamed from: u8.h, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class BasicPowerIssue {

    /* renamed from: b, reason: collision with root package name */
    public long f18912b;

    /* renamed from: d, reason: collision with root package name */
    public boolean f18914d;

    /* renamed from: e, reason: collision with root package name */
    public boolean f18915e;

    /* renamed from: f, reason: collision with root package name */
    public UploadDataUtil f18916f;

    /* renamed from: i, reason: collision with root package name */
    Context f18919i;

    /* renamed from: j, reason: collision with root package name */
    private int f18920j;

    /* renamed from: k, reason: collision with root package name */
    private String f18921k;

    /* renamed from: a, reason: collision with root package name */
    public int f18911a = 1;

    /* renamed from: c, reason: collision with root package name */
    public long f18913c = 0;

    /* renamed from: g, reason: collision with root package name */
    protected long f18917g = 0;

    /* renamed from: h, reason: collision with root package name */
    protected int f18918h = 0;

    /* renamed from: l, reason: collision with root package name */
    private boolean f18922l = true;

    public BasicPowerIssue(Context context, String str, int i10, boolean z10, boolean z11) {
        this.f18912b = 0L;
        this.f18914d = true;
        this.f18915e = true;
        this.f18920j = 0;
        this.f18921k = null;
        this.f18919i = context;
        this.f18916f = UploadDataUtil.S0(context);
        this.f18920j = i10;
        this.f18921k = str;
        this.f18914d = z10;
        this.f18915e = z11;
        this.f18912b = SmartModeSharepref.c(context, str, 0L);
    }

    private long b(double d10, double d11, long j10) {
        if (d11 == UserProfileInfo.Constant.NA_LAT_LON) {
            return 0L;
        }
        long j11 = (long) ((j10 * d10) / d11);
        Log.d("BasicPowerIssue", "calcTime result=" + j11);
        return (d10 <= UserProfileInfo.Constant.NA_LAT_LON || j11 != 0) ? j11 : new Random().nextInt(2) + 1;
    }

    public abstract double a(IBatteryStats iBatteryStats, List<ActivityManager.RunningAppProcessInfo> list, List<ActivityManager.RunningAppProcessInfo> list2, double d10, long j10);

    public abstract boolean c();

    public int d() {
        return this.f18911a;
    }

    public String e() {
        return this.f18921k;
    }

    public long f() {
        Log.d("BasicPowerIssue", "getPowerTimeInMinute key=" + this.f18921k + " issueTime=" + this.f18917g);
        return this.f18917g;
    }

    public int g() {
        return this.f18918h;
    }

    public int h() {
        return this.f18920j;
    }

    public void i(IBatteryStats iBatteryStats, ArrayList<PowerConsumeStats.b> arrayList, double d10, long j10) {
        double d11;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("parse abnormal ");
        sb2.append(arrayList != null ? arrayList.size() : -1);
        LocalLog.a("BasicPowerIssue", sb2.toString());
        if (arrayList != null) {
            Iterator<PowerConsumeStats.b> it = arrayList.iterator();
            double d12 = 0.0d;
            while (it.hasNext()) {
                PowerConsumeStats.b next = it.next();
                LocalLog.a("xxx_test", "AbnormalPowerItem power " + next.f77g);
                d12 += next.f77g;
            }
            d11 = d12;
        } else {
            d11 = 0.0d;
        }
        if (d11 > UserProfileInfo.Constant.NA_LAT_LON) {
            this.f18917g = b(d11, d10, j10);
        } else {
            this.f18917g = 0L;
        }
        if (m()) {
            LocalLog.a("BasicPowerIssue", e() + " set should show false");
            this.f18922l = d11 > UserProfileInfo.Constant.NA_LAT_LON;
        }
        LocalLog.a("BasicPowerIssue", "key = " + e() + "power = " + d11 + ", mPowerIssueTime = " + this.f18917g);
        if (this.f18917g > 0) {
            this.f18918h = 1;
        } else {
            this.f18918h = 0;
        }
    }

    public void j(IBatteryStats iBatteryStats, List<ActivityManager.RunningAppProcessInfo> list, List<ActivityManager.RunningAppProcessInfo> list2, double d10, long j10) {
        double a10 = a(iBatteryStats, list, list2, d10, j10);
        if (a10 > UserProfileInfo.Constant.NA_LAT_LON) {
            this.f18917g = b(a10, d10, j10);
        } else {
            this.f18917g = 0L;
        }
        if (m()) {
            LocalLog.a("BasicPowerIssue", e() + " set should show false");
            this.f18922l = a10 > UserProfileInfo.Constant.NA_LAT_LON;
        }
        Log.d("HighPerformIssue", "key = " + e() + ", power = " + a10 + ", mPowerIssueTime = " + this.f18917g);
        if (this.f18917g > 0) {
            this.f18918h = 1;
        } else {
            this.f18918h = 0;
        }
    }

    public void k(int i10) {
        this.f18911a = i10;
    }

    public void l(int i10) {
        this.f18918h = i10;
    }

    public boolean m() {
        return false;
    }

    public boolean n() {
        return this.f18922l;
    }

    public boolean o() {
        return true;
    }

    public void p(boolean z10) {
        int i10 = this.f18918h;
        if (i10 == 0 || i10 == 3) {
            return;
        }
        this.f18918h = z10 ? 1 : 2;
    }

    public boolean q() {
        return false;
    }
}
