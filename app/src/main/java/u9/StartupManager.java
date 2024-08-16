package u9;

import aa.EncodeUtils;
import aa.GlobalBlackListUtils;
import aa.MaliciousPreventUtils;
import aa.StartupDataUtils;
import aa.UnstableAppUtils;
import aa.UploadStatisticsUtils;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import b6.LocalLog;
import com.oplus.startupapp.data.database.RecordDatabase;
import com.oplus.statistics.OplusTrack;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import x9.AppFeatureUtils;
import x9.IconUtils;
import x9.WhiteListUtils;
import z9.AppToShow;
import z9.Record;

/* compiled from: StartupManager.java */
/* renamed from: u9.b, reason: use source file name */
/* loaded from: classes2.dex */
public class StartupManager {

    /* renamed from: f, reason: collision with root package name */
    public static final Object f18937f = new Object();

    /* renamed from: g, reason: collision with root package name */
    public static final Object f18938g = new Object();

    /* renamed from: h, reason: collision with root package name */
    private static volatile StartupManager f18939h;

    /* renamed from: a, reason: collision with root package name */
    public Map<String, Long> f18940a = new ArrayMap();

    /* renamed from: b, reason: collision with root package name */
    private Map<String, Map<String, String>> f18941b = new ArrayMap();

    /* renamed from: c, reason: collision with root package name */
    private ExecutorService f18942c;

    /* renamed from: d, reason: collision with root package name */
    private RecordDatabase f18943d;

    /* renamed from: e, reason: collision with root package name */
    private Context f18944e;

    private StartupManager(Context context) {
        this.f18944e = context;
        this.f18943d = RecordDatabase.u(context);
    }

    private void B() {
        long h10 = h();
        List<Record> v7 = this.f18943d.v().v("0", Long.valueOf(h10));
        List<Record> v10 = this.f18943d.v().v("1", Long.valueOf(h10));
        ArrayList arrayList = new ArrayList();
        s(arrayList, v7, true);
        s(arrayList, v10, false);
        if (arrayList.isEmpty()) {
            return;
        }
        OplusTrack.onCommonBatch(this.f18944e, "20089", "20089", "startup_prevent_record", arrayList, 1);
    }

    private void C() {
        long h10 = h();
        List<Record> v7 = this.f18943d.v().v("11", Long.valueOf(h10));
        ArrayList arrayList = new ArrayList();
        for (Record record : v7) {
            ArrayMap arrayMap = new ArrayMap();
            arrayMap.put("callerPkg", EncodeUtils.b(record.f20325c));
            arrayMap.put("calleePkg", EncodeUtils.b(record.f20324b));
            arrayMap.put("count", String.valueOf(record.f20330h));
            arrayMap.put("reason", record.f20335m);
            arrayMap.put("date", String.valueOf(record.f20334l));
            arrayMap.put("last_time", String.valueOf(record.f20333k));
            LocalLog.a("StartupManager", "upload sys app record: caller: " + record.f20325c + " called:" + record.f20324b + " count: " + record.f20330h + " date: " + record.f20334l + " reason: " + record.f20335m);
            arrayList.add(arrayMap);
        }
        if (arrayList.isEmpty()) {
            return;
        }
        OplusTrack.onCommonBatch(this.f18944e, "20089", "20089", "sys_app_prevent_start_record", arrayList, 1);
        this.f18943d.v().h(h10, "11");
    }

    private long h() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(5, -1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTimeInMillis();
    }

    public static StartupManager i(Context context) {
        if (f18939h == null) {
            synchronized (StartupManager.class) {
                if (f18939h == null) {
                    f18939h = new StartupManager(context);
                }
            }
        }
        return f18939h;
    }

    private void n(List<ApplicationInfo> list, Map<String, List<String>> map, boolean z10) {
        List<String> list2 = map.get("switch");
        List<String> k10 = StartupDataUtils.h(this.f18944e).k(z10);
        List<String> g6 = StartupDataUtils.h(this.f18944e).g(z10);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        boolean b10 = AppFeatureUtils.b(this.f18944e);
        Log.d("StartupManager", "get check list, when init list to show: isAutoStart: " + z10);
        if (!list.isEmpty()) {
            for (int i10 = 0; i10 < list.size(); i10++) {
                ApplicationInfo applicationInfo = list.get(i10);
                String str = applicationInfo.packageName;
                if (arrayList.contains(str)) {
                    Log.d("StartupManager", "get the same package when init autoStart list to show : " + str);
                } else {
                    arrayList.add(str);
                    if (!((applicationInfo.flags & 1) != 0) && !WhiteListUtils.l(str) && !WhiteListUtils.i(this.f18944e, str) && !g6.contains(str) && ((!b10 || !WhiteListUtils.h(this.f18944e, str)) && (applicationInfo.enabled || !str.equals("com.msd.JTClient")))) {
                        AppToShow appToShow = new AppToShow();
                        appToShow.f20305b = str;
                        appToShow.f20306c = null;
                        appToShow.f20310g = z10;
                        appToShow.f20307d = z10 ? o(str) : false;
                        appToShow.f20308e = !list2.contains(str);
                        if (k10.contains(str)) {
                            appToShow.f20309f = true;
                            arrayList2.add(appToShow);
                        } else {
                            appToShow.f20309f = false;
                            arrayList3.add(appToShow);
                        }
                    }
                }
            }
        }
        this.f18943d.v().B(arrayList2);
        this.f18943d.v().B(arrayList3);
    }

    private boolean o(String str) {
        Intent intent = new Intent("android.intent.action.BOOT_COMPLETED");
        intent.setPackage(str);
        List<ResolveInfo> queryBroadcastReceivers = this.f18944e.getPackageManager().queryBroadcastReceivers(intent, 0);
        return (queryBroadcastReceivers == null || queryBroadcastReceivers.isEmpty()) ? false : true;
    }

    private boolean r(Record record) {
        ApplicationInfo applicationInfo;
        if (WhiteListUtils.d(this.f18944e).j(record.f20325c)) {
            return false;
        }
        try {
            if (WhiteListUtils.d(this.f18944e).k(record.f20324b) && (applicationInfo = this.f18944e.getPackageManager().getApplicationInfo(record.f20325c, 128)) != null) {
                if ((applicationInfo.flags & 1) != 0) {
                    return false;
                }
            }
        } catch (PackageManager.NameNotFoundException unused) {
            Log.e("StartupManager", "loadPreventRecordAppLabel: not found package " + record.f20324b);
        }
        return true;
    }

    private void s(List<Map<String, String>> list, List<Record> list2, boolean z10) {
        Set<String> l10 = MaliciousPreventUtils.i(this.f18944e).l();
        for (Record record : list2) {
            if (record.f20331i != 0 || record.f20332j != 0) {
                if (!l10.contains(record.f20324b) && !l10.contains(record.f20325c)) {
                    ArrayMap arrayMap = new ArrayMap();
                    arrayMap.put("callerPkg", EncodeUtils.b(record.f20325c));
                    arrayMap.put("calleePkg", EncodeUtils.b(record.f20324b));
                    arrayMap.put("night_count", String.valueOf(record.f20331i));
                    arrayMap.put("reason", record.f20335m);
                    arrayMap.put("day_count", String.valueOf(record.f20332j));
                    arrayMap.put("time", String.valueOf(record.f20333k));
                    arrayMap.put("date", String.valueOf(record.f20334l));
                    arrayMap.put("start_type", z10 ? "selfStart" : "associateStart");
                    LocalLog.a("StartupManager", "upload startup prevent record: " + arrayMap);
                    list.add(arrayMap);
                }
            }
        }
    }

    public void A(String str, boolean z10, boolean z11) {
        StartupDataUtils.h(this.f18944e).L(z11, str, true, z10);
        this.f18943d.v().K(!z10, str, z11);
        UploadStatisticsUtils.a(this.f18944e, str, z10);
    }

    public boolean a(long j10) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j10);
        int i10 = calendar.get(11);
        return i10 < 8 || i10 > 20;
    }

    public void b(Runnable runnable) {
        ExecutorService executorService = this.f18942c;
        if (executorService == null || executorService.isShutdown()) {
            synchronized (this) {
                ExecutorService executorService2 = this.f18942c;
                if (executorService2 == null || executorService2.isShutdown()) {
                    this.f18942c = Executors.newFixedThreadPool(4);
                }
            }
        }
        this.f18942c.execute(runnable);
    }

    public Drawable c(PackageManager packageManager, ApplicationInfo applicationInfo) {
        if (this.f18944e == null) {
            return null;
        }
        try {
            return IconUtils.b(this.f18944e, applicationInfo.loadIcon(packageManager));
        } catch (Exception e10) {
            LocalLog.l("StartupManager", "getApplicationIcon failed for: " + e10.getMessage());
            return null;
        }
    }

    public Drawable d(String str) {
        Context context = this.f18944e;
        if (context == null) {
            return null;
        }
        try {
            return IconUtils.b(this.f18944e, context.getPackageManager().getApplicationIcon(str));
        } catch (Exception e10) {
            e10.printStackTrace();
            return null;
        }
    }

    public List<AppToShow> e() {
        return this.f18943d.v().u(true, false, false);
    }

    public int f(boolean z10) {
        return this.f18943d.v().m(z10, false, false).size();
    }

    public long g() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTimeInMillis();
    }

    public int j() {
        return y5.b.c() ? 5 : 20;
    }

    public PackageManager k() {
        return this.f18944e.getPackageManager();
    }

    public void l(Intent intent) {
        String str;
        try {
            str = intent.getData().getSchemeSpecificPart();
        } catch (Exception unused) {
            str = null;
        }
        if (intent.getBooleanExtra("android.intent.extra.REPLACING", false)) {
            LocalLog.a("StartupManager", " replace package: " + str);
            return;
        }
        if (str != null) {
            v(str, true);
            v(str, false);
            if (y5.b.c()) {
                return;
            }
            GlobalBlackListUtils.c(this.f18944e).j(str, true);
        }
    }

    public void m(Intent intent) {
        String str;
        try {
            String uri = intent.getData().toString();
            str = uri.substring(uri.indexOf(58) + 1);
            UnstableAppUtils.b(this.f18944e).a(str);
        } catch (Exception unused) {
            str = null;
        }
        boolean booleanExtra = intent.getBooleanExtra("android.intent.extra.REPLACING", false);
        if (TextUtils.isEmpty(str) || booleanExtra) {
            return;
        }
        w(str);
        t(str);
        if (y5.b.c()) {
            return;
        }
        GlobalBlackListUtils.c(this.f18944e).j(str, false);
    }

    public String p(String str, PackageManager packageManager) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            return packageManager.getApplicationInfo(str, 128).loadLabel(packageManager).toString();
        } catch (PackageManager.NameNotFoundException unused) {
            Log.d("StartupManager", "cannot find packageName: " + str);
            return null;
        }
    }

    public void q(ApplicationInfo applicationInfo, PackageManager packageManager, AppToShow appToShow, String str) {
        if (applicationInfo == null) {
            return;
        }
        Map<String, String> map = this.f18941b.get(str);
        if (map == null) {
            map = new ArrayMap<>();
            this.f18941b.put(str, map);
        }
        if (!map.containsKey(appToShow.f20305b)) {
            String charSequence = applicationInfo.loadLabel(packageManager).toString();
            appToShow.f20306c = charSequence;
            map.put(appToShow.f20305b, charSequence);
            return;
        }
        appToShow.f20306c = map.get(appToShow.f20305b);
    }

    public void t(String str) {
        this.f18943d.v().j(str);
    }

    public void u(Map<String, Map<String, List<String>>> map) {
        LocalLog.a("StartupManager", "restoreAppData start");
        synchronized (f18938g) {
            if (map == null) {
                map = StartupDataUtils.h(this.f18944e).w();
            }
            Map<String, List<String>> map2 = map.get("1");
            Map<String, List<String>> map3 = map.get("0");
            if (map2 == null || map3 == null) {
                Map<String, Map<String, List<String>>> w10 = StartupDataUtils.h(this.f18944e).w();
                map2 = w10.get("1");
                map3 = w10.get("0");
            }
            List<ApplicationInfo> installedApplications = this.f18944e.getPackageManager().getInstalledApplications(128);
            this.f18943d.v().a();
            this.f18943d.v().i("appToShow");
            n(installedApplications, map2, true);
            n(installedApplications, map3, false);
        }
        LocalLog.a("StartupManager", "restoreAppData end");
    }

    public void v(String str, boolean z10) {
        StartupDataUtils h10 = StartupDataUtils.h(this.f18944e);
        List<String> k10 = h10.k(z10);
        List<String> g6 = h10.g(z10);
        List<String> c10 = WhiteListUtils.c(this.f18944e, z10);
        try {
            boolean z11 = (this.f18944e.getPackageManager().getApplicationInfo(str, 128).flags & 1) != 0;
            boolean b10 = AppFeatureUtils.b(this.f18944e);
            if (k10.contains(str) || c10.contains(str)) {
                LocalLog.a("StartupManager", "add package : " + str + " isAutoStart: " + z10);
                h10.L(z10, str, false, true);
            }
            if (z11 || g6.contains(str) || WhiteListUtils.i(this.f18944e, str) || WhiteListUtils.l(str)) {
                return;
            }
            if (b10 && WhiteListUtils.h(this.f18944e, str)) {
                return;
            }
            AppToShow appToShow = new AppToShow();
            appToShow.f20308e = !k10.contains(str);
            appToShow.f20305b = str;
            appToShow.f20306c = null;
            appToShow.f20310g = z10;
            appToShow.f20309f = k10.contains(str);
            appToShow.f20307d = z10 ? o(str) : false;
            this.f18943d.v().A(appToShow);
        } catch (PackageManager.NameNotFoundException e10) {
            LocalLog.a("StartupManager", "cannot find packageName when update autoStart list" + e10);
        }
    }

    public void w(String str) {
        StartupDataUtils.h(this.f18944e).b(str);
        this.f18943d.v().c(str);
    }

    public void x(Bundle bundle) {
        Bundle bundle2 = new Bundle();
        RecordDatabase u7 = RecordDatabase.u(this.f18944e);
        if (bundle != null && bundle.containsKey("caller_pkg") && bundle.containsKey("called_pkg") && bundle.containsKey("launch_mode") && bundle.containsKey("launch_type")) {
            Record record = new Record();
            record.f20325c = bundle.getString("caller_pkg");
            record.f20324b = bundle.getString("called_pkg");
            record.f20328f = bundle.getString("launch_mode");
            record.f20329g = bundle.getString("launch_type");
            if (record.a()) {
                if (bundle.getBoolean("called_pkg_sys", false)) {
                    if (!StartupDataUtils.h(this.f18944e).r()) {
                        return;
                    }
                    if (!"0".equals(record.f20328f) && !"1".equals(record.f20328f)) {
                        return;
                    }
                    record.f20328f = "11";
                    record.f20326d = record.f20324b;
                    record.f20327e = record.f20325c;
                } else if (!r(record)) {
                    return;
                }
                String string = bundle.getString("reason");
                if (TextUtils.isEmpty(string)) {
                    string = "unknown";
                }
                record.f20335m = string;
                record.f20334l = i(this.f18944e).g();
                long timeInMillis = Calendar.getInstance().getTimeInMillis();
                record.f20333k = timeInMillis;
                record.f20332j = 1L;
                record.f20331i = 0L;
                if (a(timeInMillis)) {
                    record.f20331i = 1L;
                    record.f20332j = 0L;
                }
                List<Record> x10 = u7.v().x(record.f20324b, record.f20325c, record.f20328f, record.f20329g, Long.valueOf(record.f20334l), record.f20335m);
                if (x10 != null && !x10.isEmpty()) {
                    record.f20330h = x10.get(0).f20330h + 1;
                    record.f20331i += x10.get(0).f20331i;
                    record.f20332j += x10.get(0).f20332j;
                    bundle2.putLong("count", record.f20330h);
                    u7.v().N(record.f20324b, record.f20325c, record.f20328f, record.f20329g, record.f20334l, record.f20333k, record.f20330h, record.f20331i, record.f20332j, record.f20335m);
                    return;
                }
                record.f20330h = 1L;
                bundle2.putLong("count", 1L);
                u7.v().E(record);
            }
        }
    }

    public void y(Map<String, Boolean> map) {
        ArrayList arrayList = new ArrayList();
        for (String str : map.keySet()) {
            if (!map.get(str).booleanValue()) {
                arrayList.add(str);
            }
        }
        StartupDataUtils.h(this.f18944e).M(true, map, true);
        this.f18943d.v().J(true, arrayList, true);
    }

    public void z() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(g());
        long timeInMillis = calendar.getTimeInMillis();
        calendar.add(2, -1);
        long timeInMillis2 = calendar.getTimeInMillis();
        this.f18943d.v().g(timeInMillis2, timeInMillis);
        MaliciousPreventUtils.i(this.f18944e).r(timeInMillis2, timeInMillis);
        MaliciousPreventUtils.i(this.f18944e).q(timeInMillis);
        if (Calendar.getInstance().get(7) == 1) {
            UploadStatisticsUtils.b(this.f18944e, this.f18943d.v().n(true));
        }
        if (MaliciousPreventUtils.i(this.f18944e).o()) {
            B();
        }
        if (StartupDataUtils.h(this.f18944e).r()) {
            C();
        }
    }
}
