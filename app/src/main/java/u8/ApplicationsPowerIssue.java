package u8;

import a7.PowerConsumeStats;
import android.app.ActivityManager;
import android.app.OplusActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;
import b6.LocalLog;
import b9.PowerSipper;
import com.android.internal.app.IBatteryStats;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import e6.SmartModeSharepref;
import f6.CommonUtil;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import t8.PowerCpuUtil;
import t8.PowerUsageManager;
import y5.AppFeature;
import z5.GuardElfDataManager;

/* compiled from: ApplicationsPowerIssue.java */
/* renamed from: u8.d, reason: use source file name */
/* loaded from: classes2.dex */
public class ApplicationsPowerIssue extends BasicPowerIssue {

    /* renamed from: m, reason: collision with root package name */
    private Object f18894m;

    /* renamed from: n, reason: collision with root package name */
    private ArrayList<PackageInfo> f18895n;

    /* renamed from: o, reason: collision with root package name */
    private ArrayList<String> f18896o;

    /* renamed from: p, reason: collision with root package name */
    private ArrayList<Integer> f18897p;

    public ApplicationsPowerIssue(Context context, String str, int i10, boolean z10, boolean z11) {
        super(context, str, i10, z10, z11);
        this.f18894m = new Object();
        this.f18895n = new ArrayList<>();
        this.f18896o = new ArrayList<>();
        this.f18897p = new ArrayList<>();
    }

    private ArrayList<Integer> s() {
        Bundle bundle = new Bundle();
        try {
            Class<?> cls = Class.forName("android.app.OplusActivityManager");
            Method method = cls.getMethod("getPreloadPkgList", new Class[0]);
            if (method != null) {
                bundle = (Bundle) method.invoke(cls.newInstance(), new Object[0]);
            }
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        ArrayList<Integer> integerArrayList = bundle.getIntegerArrayList("uid_list");
        if (integerArrayList != null) {
            Iterator<Integer> it = integerArrayList.iterator();
            while (it.hasNext()) {
                LocalLog.l("ApplicationsPowerIssue", "uid:" + it.next().intValue() + "isPreload");
            }
        }
        return integerArrayList;
    }

    public static ArrayList<String> t(Context context, int i10) {
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList arrayList2 = (ArrayList) ((ActivityManager) context.getSystemService("activity")).getRecentTasks(i10, 2);
        if (arrayList2 != null) {
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                ActivityManager.RecentTaskInfo recentTaskInfo = (ActivityManager.RecentTaskInfo) it.next();
                ComponentName componentName = recentTaskInfo.baseActivity;
                if (componentName != null && componentName.getPackageName() != null) {
                    arrayList.add(recentTaskInfo.baseActivity.getPackageName());
                }
            }
        }
        return arrayList;
    }

    public static String u(Context context) {
        String packageName;
        ComponentName dockTopAppName = new OplusActivityManager().getDockTopAppName();
        if (dockTopAppName == null || (packageName = dockTopAppName.getPackageName()) == null) {
            return null;
        }
        return packageName;
    }

    @Override // u8.BasicPowerIssue
    public double a(IBatteryStats iBatteryStats, List<ActivityManager.RunningAppProcessInfo> list, List<ActivityManager.RunningAppProcessInfo> list2, double d10, long j10) {
        ArrayList<PowerConsumeStats.b> arrayList;
        int i10;
        synchronized (this.f18894m) {
            this.f18895n.clear();
        }
        if (list.size() == 0 && list2.size() == 0) {
            return UserProfileInfo.Constant.NA_LAT_LON;
        }
        List<String> e10 = GuardElfDataManager.d(this.f18919i).e("notify_whitelist.xml");
        ArrayMap<Integer, BasicPowerIssue> v7 = PowerUsageManager.x(this.f18919i.getApplicationContext()).v();
        int i11 = 0;
        while (true) {
            if (i11 >= v7.size()) {
                arrayList = null;
                break;
            }
            if ("abnormalapp".equals(v7.get(Integer.valueOf(i11)).e())) {
                arrayList = ((AbnormalAppsPowerIssue) v7.get(Integer.valueOf(i11))).r();
                break;
            }
            i11++;
        }
        String string = Settings.Secure.getString(this.f18919i.getContentResolver(), "default_input_method");
        Collections.sort(new ArrayList(), new PowerSipper());
        ArrayList<Integer> arrayList2 = this.f18897p;
        if (arrayList2 != null) {
            arrayList2.clear();
        }
        this.f18897p = s();
        synchronized (this.f18894m) {
            ArrayList<String> J = CommonUtil.J();
            Iterator<ActivityManager.RunningAppProcessInfo> it = list2.iterator();
            i10 = 0;
            while (it.hasNext()) {
                PackageInfo v10 = v(J, e10, string, arrayList, it.next());
                if (v10 != null) {
                    if (this.f18895n.size() < 12) {
                        this.f18895n.add(v10);
                    }
                    i10++;
                }
            }
            this.f18897p = null;
            LocalLog.a("ApplicationsPowerIssue", "CheckAsyncTask , backgroundOutList = " + i10 + ", mPkgIssues.size = " + this.f18895n.size());
        }
        return (i10 / ((list.size() * 20) + list2.size())) * d10 * 0.3333333333333333d;
    }

    @Override // u8.BasicPowerIssue
    public boolean c() {
        if (g() != 1) {
            return false;
        }
        this.f18913c = SystemClock.elapsedRealtime();
        this.f18912b = System.currentTimeMillis();
        l(3);
        synchronized (this.f18894m) {
            this.f18895n.clear();
        }
        String u7 = u(this.f18919i);
        ArrayList<String> t7 = t(this.f18919i, 2);
        if (!TextUtils.isEmpty(u7) && !t7.contains(u7)) {
            t7.add(u7);
        }
        Iterator<String> it = t7.iterator();
        while (it.hasNext()) {
            LocalLog.a("ApplicationsPowerIssue", "filter package " + it.next());
        }
        LocalLog.a("ApplicationsPowerIssue", "splitScreenPkg = " + u7);
        Intent intent = new Intent("oplus.intent.action.REQUEST_APP_CLEAN_RUNNING");
        intent.setPackage("com.oplus.athena");
        intent.putStringArrayListExtra("filterapplist", t7);
        intent.putExtra("caller_package", "com.oplus.battery.appspowerissue");
        intent.putExtra("reason", "com.oplus.battery.appspowerissue");
        try {
            this.f18919i.startService(intent);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        SmartModeSharepref.e(this.f18919i, e(), this.f18912b);
        PowerCpuUtil.a(this.f18919i);
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
    }

    public ArrayList<PackageInfo> r() {
        ArrayList<PackageInfo> arrayList;
        synchronized (this.f18894m) {
            arrayList = this.f18895n;
        }
        return arrayList;
    }

    PackageInfo v(List<String> list, List<String> list2, String str, List<PowerConsumeStats.b> list3, ActivityManager.RunningAppProcessInfo runningAppProcessInfo) {
        PackageInfo packageInfo;
        int i10;
        if (runningAppProcessInfo.uid < 10000 || list2.contains(runningAppProcessInfo.processName)) {
            return null;
        }
        List<String> a10 = AppFeature.a();
        if (a10 != null && a10.contains(runningAppProcessInfo.processName)) {
            return null;
        }
        ArrayList<Integer> arrayList = this.f18897p;
        if (arrayList != null && arrayList.contains(Integer.valueOf(runningAppProcessInfo.uid))) {
            return null;
        }
        if (str != null && str.contains(runningAppProcessInfo.processName)) {
            return null;
        }
        if (list != null && list.contains(runningAppProcessInfo.processName)) {
            return null;
        }
        String str2 = runningAppProcessInfo.processName;
        if (str2 != null && str2.startsWith("com.oplus.")) {
            return null;
        }
        String str3 = runningAppProcessInfo.processName;
        if (str3 != null && !str3.equals(runningAppProcessInfo.pkgList[0])) {
            return null;
        }
        try {
            packageInfo = this.f18919i.getPackageManager().getPackageInfo(runningAppProcessInfo.pkgList[0], 0);
            i10 = packageInfo.applicationInfo.flags;
        } catch (PackageManager.NameNotFoundException e10) {
            e10.printStackTrace();
        }
        if ((i10 & 1) == 0 && (i10 & 128) == 0) {
            if (list3 != null) {
                Iterator<PowerConsumeStats.b> it = list3.iterator();
                while (it.hasNext()) {
                    if (it.next().f75e == packageInfo.applicationInfo.uid) {
                        return null;
                    }
                }
            }
            if (packageInfo.applicationInfo.icon > 0) {
                return packageInfo;
            }
            return null;
        }
        return null;
    }
}
