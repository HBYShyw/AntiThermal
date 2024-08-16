package o9;

import a7.PowerConsumeStatsImpl;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import b6.LocalLog;
import com.oplus.thermalcontrol.ThermalControlUtils;
import com.oplus.thermalcontrol.config.policy.ThermalPolicy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import n9.HighPowerConsumptionDataBase;
import p9.PowerMonitor;
import q9.AppStats;
import r9.SimplePowerMonitorUtils;
import x4.AppSwitchObserverHelper;
import x5.UploadDataUtil;

/* compiled from: HighPowerHelper.java */
/* renamed from: o9.e, reason: use source file name */
/* loaded from: classes2.dex */
public class HighPowerHelper {

    /* renamed from: m, reason: collision with root package name */
    private static volatile HighPowerHelper f16288m;

    /* renamed from: c, reason: collision with root package name */
    private HighPowerConsumptionDataBase f16291c;

    /* renamed from: f, reason: collision with root package name */
    private PowerConsumeStatsImpl f16294f;

    /* renamed from: g, reason: collision with root package name */
    private UploadDataUtil f16295g;

    /* renamed from: h, reason: collision with root package name */
    private SharedPreferences f16296h;

    /* renamed from: i, reason: collision with root package name */
    private SharedPreferences.Editor f16297i;

    /* renamed from: j, reason: collision with root package name */
    private Context f16298j;

    /* renamed from: k, reason: collision with root package name */
    private HandlerThread f16299k;

    /* renamed from: l, reason: collision with root package name */
    private Handler f16300l;

    /* renamed from: a, reason: collision with root package name */
    private String f16289a = null;

    /* renamed from: b, reason: collision with root package name */
    private String f16290b = null;

    /* renamed from: d, reason: collision with root package name */
    public ConcurrentHashMap<String, HighPowerSipper> f16292d = new ConcurrentHashMap<>();

    /* renamed from: e, reason: collision with root package name */
    private long f16293e = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: HighPowerHelper.java */
    /* renamed from: o9.e$a */
    /* loaded from: classes2.dex */
    public class a implements Comparator<Map.Entry<String, Long>> {
        a() {
        }

        @Override // java.util.Comparator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(Map.Entry<String, Long> entry, Map.Entry<String, Long> entry2) {
            return entry2.getValue().intValue() - entry.getValue().intValue();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: HighPowerHelper.java */
    /* renamed from: o9.e$b */
    /* loaded from: classes2.dex */
    public class b implements Comparator<Map.Entry<String, Integer>> {
        b() {
        }

        @Override // java.util.Comparator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(Map.Entry<String, Integer> entry, Map.Entry<String, Integer> entry2) {
            return entry2.getValue().intValue() - entry.getValue().intValue();
        }
    }

    public HighPowerHelper(Context context) {
        this.f16298j = context;
        this.f16294f = new PowerConsumeStatsImpl(context);
        this.f16295g = UploadDataUtil.S0(this.f16298j);
        this.f16291c = new HighPowerConsumptionDataBase(this.f16298j);
    }

    private String d() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
    }

    public static HighPowerHelper f(Context context) {
        if (f16288m == null) {
            synchronized (HighPowerHelper.class) {
                if (f16288m == null) {
                    f16288m = new HighPowerHelper(context);
                }
            }
        }
        return f16288m;
    }

    private void k(HashMap<String, Integer> hashMap, ArrayList<String> arrayList, ArrayList<Integer> arrayList2) {
        ArrayList<Map.Entry> arrayList3 = new ArrayList(hashMap.entrySet());
        Collections.sort(arrayList3, new b());
        int i10 = 0;
        for (Map.Entry entry : arrayList3) {
            if (i10 >= 3) {
                return;
            }
            arrayList.add((String) entry.getKey());
            arrayList2.add((Integer) entry.getValue());
            i10++;
        }
    }

    private void l(HashMap<String, Long> hashMap, ArrayList<String> arrayList, ArrayList<Long> arrayList2) {
        ArrayList<Map.Entry> arrayList3 = new ArrayList(hashMap.entrySet());
        Collections.sort(arrayList3, new a());
        int i10 = 0;
        for (Map.Entry entry : arrayList3) {
            if (i10 >= 3) {
                return;
            }
            arrayList.add((String) entry.getKey());
            arrayList2.add((Long) entry.getValue());
            i10++;
        }
    }

    private void n(String str) {
        String d10 = d();
        if (this.f16291c == null) {
            this.f16291c = new HighPowerConsumptionDataBase(this.f16298j);
        }
        SQLiteDatabase writableDatabase = this.f16291c.getWritableDatabase();
        for (Map.Entry<String, AppStats> entry : PowerMonitor.j().entrySet()) {
            ContentValues contentValues = new ContentValues();
            String key = entry.getKey();
            AppStats value = entry.getValue();
            contentValues.put("app_name", key);
            contentValues.put("time", d10);
            contentValues.put(ThermalPolicy.KEY_CPU, Long.valueOf(value.f16952g.f16980e));
            contentValues.put("wakelock", Long.valueOf(value.f16956k.f17109j));
            contentValues.put("job", Long.valueOf(value.f16954i.f17023m));
            contentValues.put("wifiscan", Long.valueOf(value.f16958m.f17112a0));
            contentValues.put("camera", Long.valueOf(value.f16961p.f16974l));
            contentValues.put("flashlight", Long.valueOf(value.f16962q.f17009l));
            contentValues.put("gps", Long.valueOf(value.f16960o.f17075m));
            contentValues.put("alarm", Integer.valueOf(value.f16953h.f16940c));
            writableDatabase.insert(str, null, contentValues);
        }
    }

    private void o() {
        if (this.f16289a == null) {
            this.f16289a = "high_power_consumption_one";
        }
        try {
            n(this.f16289a);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
    }

    public void b(String str) {
        if (this.f16291c == null || str == null) {
            return;
        }
        this.f16298j.deleteDatabase(str);
    }

    public void c() {
        for (Map.Entry<String, AppStats> entry : PowerMonitor.j().entrySet()) {
            String key = entry.getKey();
            AppStats value = entry.getValue();
            HighPowerSipper highPowerSipper = this.f16292d.get(key) == null ? new HighPowerSipper(key) : this.f16292d.get(key);
            highPowerSipper.c(value, this.f16293e);
            if (this.f16292d.get(key) == null) {
                this.f16292d.put(key, highPowerSipper);
            }
        }
    }

    public ConcurrentHashMap<String, HighPowerSipper> e() {
        LocalLog.a("HighPowerHelper", "mHighPowerSipperMap: " + this.f16292d.size());
        return this.f16292d;
    }

    /* renamed from: g, reason: merged with bridge method [inline-methods] */
    public void h() {
        HighPowerSipper highPowerSipper;
        this.f16293e++;
        LocalLog.a("HighPowerHelper", "getRunningAppStatsList: " + PowerMonitor.j().size());
        if (ThermalControlUtils.getInstance(this.f16298j).isScreenOn()) {
            AppSwitchObserverHelper.d(this.f16298j).g();
        } else {
            PowerMonitor h10 = PowerMonitor.h(this.f16298j);
            h10.p();
            h10.n(false);
        }
        for (Map.Entry<String, AppStats> entry : PowerMonitor.j().entrySet()) {
            String key = entry.getKey();
            AppStats value = entry.getValue();
            SimplePowerMonitorUtils.d(this.f16298j);
            if (this.f16292d.get(key) == null) {
                highPowerSipper = new HighPowerSipper(key);
                this.f16292d.put(key, highPowerSipper);
            } else {
                highPowerSipper = this.f16292d.get(key);
            }
            highPowerSipper.e(value);
            highPowerSipper.a();
            if (LocalLog.g()) {
                LocalLog.b("HighPowerHelper", "; pkgName = " + highPowerSipper.f16309a + "; mCpuTimeMsPerHourWhileScreenOn = " + highPowerSipper.f16311c + " ms; mWakeLockTimeMsPerHourWhileScreenOn = " + highPowerSipper.f16312d + " ms; mWakeLockTimeMsPerHourWhileScreenOff = " + highPowerSipper.f16313e + " ms; mJobTimeMsPerHourWhileScreenOn = " + highPowerSipper.f16314f + " ms; mJobTimeMsPerTwoHoursWhileScreenOn = " + highPowerSipper.f16315g + " ms; mWiFiScanTimeMsPerHourWhileScreenOn = " + highPowerSipper.f16317i + " ms; mCameraTimeMsPerHourWhileScreenOn = " + highPowerSipper.f16318j + " ms; mFlashLightTimeMsPerHourWhileScreenOn = " + highPowerSipper.f16319k + " ms; mGpsTimeMsPerHourWhileScreenOn = " + highPowerSipper.f16320l + " ms; mAlarmCountPerHourWhileScreenOn = " + highPowerSipper.f16321m + " ms");
            }
        }
        if (SimplePowerMonitorUtils.f17659j) {
            this.f16294f.n();
            c();
        }
        o();
    }

    public HashMap<String, String> i() {
        String str;
        int i10;
        String str2;
        HashMap<String, String> hashMap = new HashMap<>();
        HashMap<String, Long> hashMap2 = new HashMap<>();
        HashMap<String, Long> hashMap3 = new HashMap<>();
        HashMap<String, Long> hashMap4 = new HashMap<>();
        HashMap<String, Long> hashMap5 = new HashMap<>();
        HashMap<String, Long> hashMap6 = new HashMap<>();
        HashMap<String, Long> hashMap7 = new HashMap<>();
        HashMap<String, Long> hashMap8 = new HashMap<>();
        HashMap<String, Integer> hashMap9 = new HashMap<>();
        String str3 = "HighPowerHelper";
        if (LocalLog.f()) {
            LocalLog.a("HighPowerHelper", "RunningAppStatsList().size(): " + PowerMonitor.j().size());
        }
        ka.a a10 = HighPowerShuffleUtil.a(this.f16298j, PowerMonitor.j().keySet());
        for (Map.Entry<String, AppStats> entry : PowerMonitor.j().entrySet()) {
            String key = entry.getKey();
            ka.b a11 = a10.a(key);
            if (a11 != null) {
                key = a11.f14229a;
            }
            AppStats value = entry.getValue();
            hashMap2.put(key, Long.valueOf(value.f16952g.f16980e));
            hashMap3.put(key, Long.valueOf(value.f16956k.f17109j));
            hashMap4.put(key, Long.valueOf(value.f16954i.f17023m));
            hashMap5.put(key, Long.valueOf(value.f16958m.f17112a0));
            hashMap6.put(key, Long.valueOf(value.f16961p.f16974l));
            hashMap7.put(key, Long.valueOf(value.f16962q.f17009l));
            hashMap8.put(key, Long.valueOf(value.f16960o.f17075m));
            hashMap9.put(key, Integer.valueOf(value.f16953h.f16940c));
            str3 = str3;
            a10 = a10;
        }
        String str4 = str3;
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<Long> arrayList2 = new ArrayList<>();
        l(hashMap2, arrayList, arrayList2);
        int min = Math.min(arrayList.size(), 3);
        if (LocalLog.f()) {
            str = str4;
            LocalLog.a(str, "pkg.size(): " + arrayList.size());
        } else {
            str = str4;
        }
        if (min > 0) {
            int i11 = 0;
            while (i11 < min) {
                if (arrayList2.get(i11).longValue() > SimplePowerMonitorUtils.f17665p) {
                    StringBuilder sb2 = new StringBuilder();
                    i10 = min;
                    StringBuilder sb3 = new StringBuilder();
                    str2 = str;
                    sb3.append(arrayList.get(i11));
                    sb3.append(SimplePowerMonitorUtils.z(String.valueOf(Build.VERSION.RELEASE)));
                    sb2.append(SimplePowerMonitorUtils.z(sb3.toString()));
                    sb2.append(" cpu: ");
                    hashMap.put(sb2.toString(), arrayList2.get(i11).toString());
                } else {
                    i10 = min;
                    str2 = str;
                }
                i11++;
                min = i10;
                str = str2;
            }
        }
        String str5 = str;
        arrayList.clear();
        arrayList2.clear();
        l(hashMap3, arrayList, arrayList2);
        int min2 = Math.min(arrayList.size(), 3);
        if (min2 > 0) {
            for (int i12 = 0; i12 < min2; i12++) {
                if (arrayList2.get(i12).longValue() > SimplePowerMonitorUtils.f17666q) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(SimplePowerMonitorUtils.z(arrayList.get(i12) + SimplePowerMonitorUtils.z(String.valueOf(Build.VERSION.RELEASE))));
                    sb4.append(" wakeLock: ");
                    hashMap.put(sb4.toString(), arrayList2.get(i12).toString());
                }
            }
        }
        arrayList.clear();
        arrayList2.clear();
        l(hashMap4, arrayList, arrayList2);
        int min3 = Math.min(arrayList.size(), 3);
        if (min3 > 0) {
            for (int i13 = 0; i13 < min3; i13++) {
                if (arrayList2.get(i13).longValue() > SimplePowerMonitorUtils.f17667r) {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(SimplePowerMonitorUtils.z(arrayList.get(i13) + SimplePowerMonitorUtils.z(String.valueOf(Build.VERSION.RELEASE))));
                    sb5.append(" job: ");
                    hashMap.put(sb5.toString(), arrayList2.get(i13).toString());
                }
            }
        }
        arrayList.clear();
        arrayList2.clear();
        l(hashMap5, arrayList, arrayList2);
        int min4 = Math.min(arrayList.size(), 3);
        if (min4 > 0) {
            for (int i14 = 0; i14 < min4; i14++) {
                if (arrayList2.get(i14).longValue() > SimplePowerMonitorUtils.f17669t) {
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append(SimplePowerMonitorUtils.z(arrayList.get(i14) + SimplePowerMonitorUtils.z(String.valueOf(Build.VERSION.RELEASE))));
                    sb6.append(" wifiScan: ");
                    hashMap.put(sb6.toString(), arrayList2.get(i14).toString());
                }
            }
        }
        arrayList.clear();
        arrayList2.clear();
        l(hashMap6, arrayList, arrayList2);
        int min5 = Math.min(arrayList.size(), 3);
        if (min5 > 0) {
            for (int i15 = 0; i15 < min5; i15++) {
                if (arrayList2.get(i15).longValue() > SimplePowerMonitorUtils.f17670u) {
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append(SimplePowerMonitorUtils.z(arrayList.get(i15) + SimplePowerMonitorUtils.z(String.valueOf(Build.VERSION.RELEASE))));
                    sb7.append(" camera: ");
                    hashMap.put(sb7.toString(), arrayList2.get(i15).toString());
                }
            }
        }
        arrayList.clear();
        arrayList2.clear();
        l(hashMap7, arrayList, arrayList2);
        int min6 = Math.min(arrayList.size(), 3);
        if (min6 > 0) {
            for (int i16 = 0; i16 < min6; i16++) {
                if (arrayList2.get(i16).longValue() > SimplePowerMonitorUtils.f17671v) {
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append(SimplePowerMonitorUtils.z(arrayList.get(i16) + SimplePowerMonitorUtils.z(String.valueOf(Build.VERSION.RELEASE))));
                    sb8.append(" flashLight: ");
                    hashMap.put(sb8.toString(), arrayList2.get(i16).toString());
                }
            }
        }
        arrayList.clear();
        arrayList2.clear();
        l(hashMap8, arrayList, arrayList2);
        int min7 = Math.min(arrayList.size(), 3);
        if (min7 > 0) {
            for (int i17 = 0; i17 < min7; i17++) {
                if (arrayList2.get(i17).longValue() > SimplePowerMonitorUtils.f17672w) {
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append(SimplePowerMonitorUtils.z(arrayList.get(i17) + SimplePowerMonitorUtils.z(String.valueOf(Build.VERSION.RELEASE))));
                    sb9.append(" gps: ");
                    hashMap.put(sb9.toString(), arrayList2.get(i17).toString());
                }
            }
        }
        arrayList.clear();
        arrayList2.clear();
        ArrayList<Integer> arrayList3 = new ArrayList<>();
        k(hashMap9, arrayList, arrayList3);
        LocalLog.a(str5, "pkg.size(): " + arrayList.size());
        LocalLog.a(str5, "values.size(): " + arrayList3.size());
        int min8 = Math.min(arrayList.size(), 3);
        if (min8 > 0) {
            for (int i18 = 0; i18 < min8; i18++) {
                if (arrayList3.get(i18).intValue() > SimplePowerMonitorUtils.f17673x) {
                    StringBuilder sb10 = new StringBuilder();
                    sb10.append(SimplePowerMonitorUtils.z(arrayList.get(i18) + SimplePowerMonitorUtils.z(String.valueOf(Build.VERSION.RELEASE))));
                    sb10.append(" alarm: ");
                    hashMap.put(sb10.toString(), arrayList3.get(i18).toString());
                }
            }
        }
        Log.d(str5, "result.size(): " + hashMap.size());
        for (Map.Entry<String, String> entry2 : hashMap.entrySet()) {
            Log.d(str5, "pkg: " + entry2.getKey() + "; value: " + entry2.getValue());
        }
        return hashMap;
    }

    public HashMap<String, String> j() {
        int i10;
        int i11;
        HashMap<String, String> hashMap;
        HashMap<String, String> hashMap2;
        ka.a aVar;
        HashMap<String, String> hashMap3 = new HashMap<>();
        HashMap<String, Integer> hashMap4 = new HashMap<>();
        HashMap<String, Integer> hashMap5 = new HashMap<>();
        HashMap<String, Integer> hashMap6 = new HashMap<>();
        HashMap<String, Integer> hashMap7 = new HashMap<>();
        HashMap<String, Integer> hashMap8 = new HashMap<>();
        HashMap<String, Integer> hashMap9 = new HashMap<>();
        HashMap<String, Integer> hashMap10 = new HashMap<>();
        HashMap<String, Integer> hashMap11 = new HashMap<>();
        HashMap<String, Integer> hashMap12 = new HashMap<>();
        HashMap<String, Integer> hashMap13 = new HashMap<>();
        HighPowerHelper f10 = f(this.f16298j);
        ka.a a10 = HighPowerShuffleUtil.a(this.f16298j, f10.e().keySet());
        Iterator<Map.Entry<String, HighPowerSipper>> it = f10.e().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, HighPowerSipper> next = it.next();
            Iterator<Map.Entry<String, HighPowerSipper>> it2 = it;
            String key = next.getKey();
            HashMap<String, Integer> hashMap14 = hashMap12;
            ka.b a11 = a10.a(key);
            if (a11 != null) {
                aVar = a10;
                StringBuilder sb2 = new StringBuilder();
                hashMap2 = hashMap3;
                sb2.append("before process:");
                sb2.append(key);
                sb2.append(" after: ");
                sb2.append(a11.f14229a);
                LocalLog.a("HighPowerHelper", sb2.toString());
                key = a11.f14229a;
            } else {
                hashMap2 = hashMap3;
                aVar = a10;
            }
            HighPowerSipper value = next.getValue();
            hashMap4.put(key, Integer.valueOf(value.f16333y));
            hashMap5.put(key, Integer.valueOf(value.f16334z));
            hashMap6.put(key, Integer.valueOf(value.A));
            hashMap7.put(key, Integer.valueOf(value.B));
            hashMap8.put(key, Integer.valueOf(value.C));
            hashMap9.put(key, Integer.valueOf(value.D));
            hashMap10.put(key, Integer.valueOf(value.E));
            hashMap11.put(key, Integer.valueOf(value.F));
            hashMap13.put(key, Integer.valueOf(value.H));
            hashMap12 = hashMap14;
            it = it2;
            a10 = aVar;
            hashMap3 = hashMap2;
        }
        HashMap<String, String> hashMap15 = hashMap3;
        HashMap<String, Integer> hashMap16 = hashMap12;
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<Integer> arrayList2 = new ArrayList<>();
        k(hashMap4, arrayList, arrayList2);
        int min = Math.min(arrayList.size(), 3);
        Log.d("HighPowerHelper", "pkg.size(): " + arrayList.size());
        if (min > 0) {
            int i12 = 0;
            while (i12 < min) {
                if (arrayList2.get(i12).intValue() > 0) {
                    StringBuilder sb3 = new StringBuilder();
                    StringBuilder sb4 = new StringBuilder();
                    i11 = min;
                    sb4.append(arrayList.get(i12));
                    sb4.append(SimplePowerMonitorUtils.z(String.valueOf(Build.VERSION.RELEASE)));
                    sb3.append(SimplePowerMonitorUtils.z(sb4.toString()));
                    sb3.append(" cpu: ");
                    hashMap = hashMap15;
                    hashMap.put(sb3.toString(), arrayList2.get(i12).toString());
                } else {
                    i11 = min;
                    hashMap = hashMap15;
                }
                i12++;
                hashMap15 = hashMap;
                min = i11;
            }
        }
        HashMap<String, String> hashMap17 = hashMap15;
        arrayList.clear();
        arrayList2.clear();
        k(hashMap5, arrayList, arrayList2);
        int min2 = Math.min(arrayList.size(), 3);
        if (min2 > 0) {
            int i13 = 0;
            while (i13 < min2) {
                if (arrayList2.get(i13).intValue() > 0) {
                    StringBuilder sb5 = new StringBuilder();
                    StringBuilder sb6 = new StringBuilder();
                    i10 = min2;
                    sb6.append(arrayList.get(i13));
                    sb6.append(SimplePowerMonitorUtils.z(String.valueOf(Build.VERSION.RELEASE)));
                    sb5.append(SimplePowerMonitorUtils.z(sb6.toString()));
                    sb5.append(" wakeLock: ");
                    hashMap17.put(sb5.toString(), arrayList2.get(i13).toString());
                } else {
                    i10 = min2;
                }
                i13++;
                min2 = i10;
            }
        }
        arrayList.clear();
        arrayList2.clear();
        k(hashMap6, arrayList, arrayList2);
        int min3 = Math.min(arrayList.size(), 3);
        if (min3 > 0) {
            for (int i14 = 0; i14 < min3; i14++) {
                if (arrayList2.get(i14).intValue() > 0) {
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append(SimplePowerMonitorUtils.z(arrayList.get(i14) + SimplePowerMonitorUtils.z(String.valueOf(Build.VERSION.RELEASE))));
                    sb7.append(" job: ");
                    hashMap17.put(sb7.toString(), arrayList2.get(i14).toString());
                }
            }
        }
        arrayList.clear();
        arrayList2.clear();
        k(hashMap7, arrayList, arrayList2);
        int min4 = Math.min(arrayList.size(), 3);
        if (min4 > 0) {
            for (int i15 = 0; i15 < min4; i15++) {
                if (arrayList2.get(i15).intValue() > 0) {
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append(SimplePowerMonitorUtils.z(arrayList.get(i15) + SimplePowerMonitorUtils.z(String.valueOf(Build.VERSION.RELEASE))));
                    sb8.append(" screen: ");
                    hashMap17.put(sb8.toString(), arrayList2.get(i15).toString());
                }
            }
        }
        arrayList.clear();
        arrayList2.clear();
        k(hashMap8, arrayList, arrayList2);
        int min5 = Math.min(arrayList.size(), 3);
        if (min5 > 0) {
            for (int i16 = 0; i16 < min5; i16++) {
                if (arrayList2.get(i16).intValue() > 0) {
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append(SimplePowerMonitorUtils.z(arrayList.get(i16) + SimplePowerMonitorUtils.z(String.valueOf(Build.VERSION.RELEASE))));
                    sb9.append(" wifiScan: ");
                    hashMap17.put(sb9.toString(), arrayList2.get(i16).toString());
                }
            }
        }
        arrayList.clear();
        arrayList2.clear();
        k(hashMap9, arrayList, arrayList2);
        int min6 = Math.min(arrayList.size(), 3);
        if (min6 > 0) {
            for (int i17 = 0; i17 < min6; i17++) {
                if (arrayList2.get(i17).intValue() > 0) {
                    StringBuilder sb10 = new StringBuilder();
                    sb10.append(SimplePowerMonitorUtils.z(arrayList.get(i17) + SimplePowerMonitorUtils.z(String.valueOf(Build.VERSION.RELEASE))));
                    sb10.append(" camera: ");
                    hashMap17.put(sb10.toString(), arrayList2.get(i17).toString());
                }
            }
        }
        arrayList.clear();
        arrayList2.clear();
        k(hashMap10, arrayList, arrayList2);
        int min7 = Math.min(arrayList.size(), 3);
        if (min7 > 0) {
            for (int i18 = 0; i18 < min7; i18++) {
                if (arrayList2.get(i18).intValue() > 0) {
                    StringBuilder sb11 = new StringBuilder();
                    sb11.append(SimplePowerMonitorUtils.z(arrayList.get(i18) + SimplePowerMonitorUtils.z(String.valueOf(Build.VERSION.RELEASE))));
                    sb11.append(" flashLight: ");
                    hashMap17.put(sb11.toString(), arrayList2.get(i18).toString());
                }
            }
        }
        arrayList.clear();
        arrayList2.clear();
        k(hashMap11, arrayList, arrayList2);
        int min8 = Math.min(arrayList.size(), 3);
        if (min8 > 0) {
            for (int i19 = 0; i19 < min8; i19++) {
                if (arrayList2.get(i19).intValue() > 0) {
                    StringBuilder sb12 = new StringBuilder();
                    sb12.append(SimplePowerMonitorUtils.z(arrayList.get(i19) + SimplePowerMonitorUtils.z(String.valueOf(Build.VERSION.RELEASE))));
                    sb12.append(" gps: ");
                    hashMap17.put(sb12.toString(), arrayList2.get(i19).toString());
                }
            }
        }
        arrayList.clear();
        arrayList2.clear();
        k(hashMap16, arrayList, arrayList2);
        int min9 = Math.min(arrayList.size(), 3);
        if (min9 > 0) {
            for (int i20 = 0; i20 < min9; i20++) {
                if (arrayList2.get(i20).intValue() > 0) {
                    StringBuilder sb13 = new StringBuilder();
                    sb13.append(SimplePowerMonitorUtils.z(arrayList.get(i20) + SimplePowerMonitorUtils.z(String.valueOf(Build.VERSION.RELEASE))));
                    sb13.append(" alarm: ");
                    hashMap17.put(sb13.toString(), arrayList2.get(i20).toString());
                }
            }
        }
        arrayList.clear();
        arrayList2.clear();
        k(hashMap13, arrayList, arrayList2);
        int min10 = Math.min(arrayList.size(), 3);
        if (min10 > 0) {
            for (int i21 = 0; i21 < min10; i21++) {
                if (arrayList2.get(i21).intValue() > 0) {
                    StringBuilder sb14 = new StringBuilder();
                    sb14.append(SimplePowerMonitorUtils.z(arrayList.get(i21) + SimplePowerMonitorUtils.z(String.valueOf(Build.VERSION.RELEASE))));
                    sb14.append(" audio: ");
                    hashMap17.put(sb14.toString(), arrayList2.get(i21).toString());
                }
            }
        }
        Log.d("HighPowerHelper", "result.size(): " + hashMap17.size());
        for (Map.Entry<String, String> entry : hashMap17.entrySet()) {
            Log.d("HighPowerHelper", "pkg: " + entry.getKey() + "; value: " + entry.getValue());
        }
        return hashMap17;
    }

    public void m() {
        HandlerThread handlerThread = this.f16299k;
        if (handlerThread != null) {
            handlerThread.quitSafely();
        }
        HandlerThread handlerThread2 = new HandlerThread("get_stats");
        this.f16299k = handlerThread2;
        handlerThread2.start();
        Handler handler = new Handler(this.f16299k.getLooper());
        this.f16300l = handler;
        handler.post(new Runnable() { // from class: o9.d
            @Override // java.lang.Runnable
            public final void run() {
                HighPowerHelper.this.h();
            }
        });
    }

    public void p() {
        SharedPreferences sharedPreferences = this.f16298j.getSharedPreferences("HighPowerDataBaseTimeStamp", 0);
        this.f16296h = sharedPreferences;
        this.f16297i = sharedPreferences.edit();
        int i10 = this.f16296h.getInt("store_data_times", 0);
        int i11 = i10 + 1;
        this.f16297i.putInt("store_data_times", i10);
        this.f16297i.apply();
        int i12 = i11 % 3;
        if (i12 == 0) {
            this.f16289a = "high_power_consumption_one";
            if (i11 == 0) {
                this.f16290b = null;
            } else {
                this.f16290b = "high_power_consumption_one";
            }
        } else if (i12 == 1) {
            this.f16289a = "high_power_consumption_two";
            if (i11 == 1) {
                this.f16290b = null;
            } else {
                this.f16290b = "high_power_consumption_two";
            }
        } else if (i12 == 2) {
            this.f16289a = "high_power_consumption_three";
            if (i11 == 2) {
                this.f16290b = null;
            } else {
                this.f16290b = "high_power_consumption_three";
            }
        }
        if (ThermalControlUtils.getInstance(this.f16298j).isScreenOn()) {
            AppSwitchObserverHelper.d(this.f16298j).g();
        } else {
            PowerMonitor h10 = PowerMonitor.h(this.f16298j);
            h10.p();
            h10.n(false);
        }
        try {
            b(this.f16290b);
            n(this.f16289a);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        this.f16295g.K();
        this.f16295g.L();
        for (Map.Entry<String, AppStats> entry : PowerMonitor.j().entrySet()) {
            String key = entry.getKey();
            AppStats value = entry.getValue();
            HighPowerSipper highPowerSipper = this.f16292d.get(key) == null ? new HighPowerSipper(key) : this.f16292d.get(key);
            if (this.f16292d.get(key) == null) {
                this.f16292d.put(key, highPowerSipper);
            }
            value.y();
            highPowerSipper.c(value, this.f16293e);
            highPowerSipper.b();
        }
    }
}
