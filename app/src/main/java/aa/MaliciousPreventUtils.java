package aa;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import b6.LocalLog;
import com.oplus.startupapp.data.database.RecordDatabase;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.PatternSyntaxException;
import u9.StartupManager;
import z9.MaliciousDetailRecord;
import z9.MaliciousRecord;

/* compiled from: MaliciousPreventUtils.java */
/* renamed from: aa.d, reason: use source file name */
/* loaded from: classes2.dex */
public class MaliciousPreventUtils {

    /* renamed from: m, reason: collision with root package name */
    private static volatile MaliciousPreventUtils f126m;

    /* renamed from: n, reason: collision with root package name */
    private static final Set<String> f127n = Collections.unmodifiableSet(new ArraySet(new String[]{"com.tencent.mm", "com.ss.android.ugc.aweme", "com.tencent.mobileqq", "com.smile.gifmaker", "com.kuaishou.nebula", "com.xunmeng.pinduoduo", "com.ss.android.ugc.aweme.lite", "com.taobao.taobao", "com.eg.android.AlipayGphone", "com.baidu.searchbox"}));

    /* renamed from: c, reason: collision with root package name */
    private final Context f130c;

    /* renamed from: d, reason: collision with root package name */
    private RecordDatabase f131d;

    /* renamed from: j, reason: collision with root package name */
    private boolean f137j;

    /* renamed from: a, reason: collision with root package name */
    private final ArrayMap<String, MaliciousRecord> f128a = new ArrayMap<>();

    /* renamed from: b, reason: collision with root package name */
    private final ArrayMap<String, MaliciousDetailRecord> f129b = new ArrayMap<>();

    /* renamed from: e, reason: collision with root package name */
    private Handler f132e = null;

    /* renamed from: f, reason: collision with root package name */
    private int f133f = -14;

    /* renamed from: g, reason: collision with root package name */
    private long f134g = 60000;

    /* renamed from: h, reason: collision with root package name */
    private long f135h = 1000000;

    /* renamed from: i, reason: collision with root package name */
    private volatile boolean f136i = true;

    /* renamed from: k, reason: collision with root package name */
    private volatile boolean f138k = true;

    /* renamed from: l, reason: collision with root package name */
    private Set<String> f139l = new ArraySet();

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: MaliciousPreventUtils.java */
    /* renamed from: aa.d$b */
    /* loaded from: classes2.dex */
    public class b implements Runnable {
        private b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            MaliciousPreventUtils.this.f136i = true;
            if (!MaliciousPreventUtils.this.f128a.isEmpty()) {
                MaliciousPreventUtils.this.f131d.v().L(MaliciousPreventUtils.this.f128a);
                MaliciousPreventUtils.this.f128a.clear();
            }
            if (!MaliciousPreventUtils.this.f129b.isEmpty()) {
                ArrayList arrayList = new ArrayList();
                Iterator it = MaliciousPreventUtils.this.f129b.keySet().iterator();
                while (it.hasNext()) {
                    arrayList.add((MaliciousDetailRecord) MaliciousPreventUtils.this.f129b.get((String) it.next()));
                }
                MaliciousPreventUtils.this.f131d.v().C(arrayList);
                MaliciousPreventUtils.this.f129b.clear();
            }
            if (MaliciousPreventUtils.this.f137j) {
                Log.d("StartupManagerMaliciousPrevent", "delOldestData,startTime:" + Calendar.getInstance().getTimeInMillis());
            }
            MaliciousPreventUtils.this.f131d.v().b(StartupManager.i(MaliciousPreventUtils.this.f130c).g(), MaliciousPreventUtils.this.f135h);
            if (MaliciousPreventUtils.this.f137j) {
                Log.d("StartupManagerMaliciousPrevent", "delOldestData,endTime:" + Calendar.getInstance().getTimeInMillis());
            }
        }
    }

    private MaliciousPreventUtils(Context context) {
        this.f131d = null;
        this.f137j = false;
        this.f130c = context;
        if (!Application.getProcessName().equals("com.oplus.battery:remote")) {
            this.f131d = RecordDatabase.u(context);
        }
        this.f137j = SystemProperties.getBoolean("persist.sys.startup.malicious.debug", false);
        m();
    }

    public static MaliciousPreventUtils i(Context context) {
        if (f126m == null) {
            synchronized (MaliciousPreventUtils.class) {
                if (f126m == null) {
                    f126m = new MaliciousPreventUtils(context);
                }
            }
        }
        return f126m;
    }

    private String j(String str) {
        return this.f130c.getSharedPreferences("maliciousRecordConfig", 0).getString(str, null);
    }

    private boolean k(String str) {
        return this.f130c.getSharedPreferences("StartupPreventRecordDcs", 0).getBoolean(str, true);
    }

    private Set<String> p() {
        return this.f130c.getSharedPreferences("StartupPreventRecordDcs", 0).getStringSet("whitePkg", new ArraySet());
    }

    public long h() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTimeInMillis();
    }

    public Set<String> l() {
        synchronized (this.f139l) {
            if (this.f139l.isEmpty()) {
                return f127n;
            }
            return new ArraySet(this.f139l);
        }
    }

    public void m() {
        String j10 = j("detailInterval");
        String j11 = j("dateInterval");
        String j12 = j("detailMaxCount");
        if (j10 != null) {
            try {
                this.f134g = Long.parseLong(j10);
            } catch (Exception unused) {
                Log.e("StartupManagerMaliciousPrevent", "initConfig failed");
            }
        }
        if (j11 != null) {
            this.f133f = -Integer.parseInt(j11);
        }
        if (j12 != null) {
            this.f135h = Long.parseLong(j12);
        }
        Log.d("StartupManagerMaliciousPrevent", "detailInterval:" + this.f134g + " dateInterval" + this.f133f + " detailMaxCount" + this.f135h);
        this.f138k = k("enabled");
        synchronized (this.f139l) {
            this.f139l.clear();
            this.f139l.addAll(p());
        }
    }

    public void n(Handler handler) {
        this.f132e = handler;
    }

    public boolean o() {
        return this.f138k && y5.b.c();
    }

    public void q(long j10) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(h());
        calendar.add(5, this.f133f);
        long timeInMillis = calendar.getTimeInMillis();
        if (j10 < timeInMillis) {
            LocalLog.l("StartupManagerMaliciousPrevent", "removeMaliciousPreventRecordByDate: out of range maxDate: " + j10 + " minDate: " + timeInMillis);
            return;
        }
        this.f131d.v().e(timeInMillis, j10);
    }

    public void r(long j10, long j11) {
        if (j11 < j10) {
            LocalLog.l("StartupManagerMaliciousPrevent", "removeMaliciousPreventRecordByDate: out of range maxDate: " + j11 + " minDate: " + j10);
            return;
        }
        this.f131d.v().f(j10, j11);
    }

    public void s(String str, String str2, String str3) {
        SharedPreferences.Editor edit = this.f130c.getSharedPreferences("maliciousRecordConfig", 0).edit();
        edit.putString("detailInterval", str);
        edit.putString("dateInterval", str2);
        edit.putString("detailMaxCount", str3);
        edit.apply();
    }

    public void t(boolean z10) {
        SharedPreferences.Editor edit = this.f130c.getSharedPreferences("StartupPreventRecordDcs", 0).edit();
        edit.putBoolean("enabled", z10);
        edit.apply();
        this.f138k = z10;
        LocalLog.a("StartupManagerMaliciousPrevent", "update startup record dcs config: " + this.f138k);
    }

    public void u(Set<String> set) {
        synchronized (this.f139l) {
            this.f139l.clear();
            this.f139l.addAll(set);
        }
        SharedPreferences.Editor edit = this.f130c.getSharedPreferences("StartupPreventRecordDcs", 0).edit();
        edit.putStringSet("whitePkg", set);
        edit.apply();
        LocalLog.a("StartupManagerMaliciousPrevent", "update startup record dcs white");
    }

    public void v(String str, long j10, long j11) {
        LocalLog.l("StartupManagerMaliciousPrevent", "onUpdateDetailMalicious: get prevent record. called: " + str);
        this.f129b.put(str, new MaliciousDetailRecord(str, j10, j11, "Activity"));
        if (this.f136i) {
            this.f136i = false;
            this.f132e.postDelayed(new b(), this.f134g);
        }
    }

    public void w(Bundle bundle) {
        long j10;
        long j11;
        String string = bundle.getString("caller_pkg");
        String string2 = bundle.getString("called_pkg");
        String string3 = bundle.getString("cpn");
        if (!TextUtils.isEmpty(string) && !TextUtils.isEmpty(string2) && !TextUtils.isEmpty(string3)) {
            long timeInMillis = Calendar.getInstance().getTimeInMillis();
            long g6 = StartupManager.i(this.f130c).g();
            String str = string + "_" + string2 + "_" + g6 + "_" + string3;
            MaliciousRecord maliciousRecord = this.f128a.get(str);
            if (maliciousRecord == null) {
                j10 = g6;
                MaliciousRecord maliciousRecord2 = new MaliciousRecord(string2, string, 1L, timeInMillis, j10, string3);
                this.f128a.put(str, maliciousRecord2);
                maliciousRecord = maliciousRecord2;
                j11 = timeInMillis;
            } else {
                j10 = g6;
                maliciousRecord.f20319d++;
                j11 = timeInMillis;
                maliciousRecord.f20320e = j11;
                this.f128a.put(str, maliciousRecord);
            }
            LocalLog.l("StartupManagerMaliciousPrevent", "onUpdateMalicious: get prevent record. caller: " + string + " called: " + string2 + " count:" + maliciousRecord.f20319d + "time:" + maliciousRecord.f20320e);
            v(string2, j11, j10);
            return;
        }
        LocalLog.l("StartupManagerMaliciousPrevent", "onUpdateMalicious: get null pkgname from bundle, callerpkg: " + string + " calledpkg: " + string2 + " calledCpn: " + string3);
    }

    public void x(Bundle bundle) {
        Iterator<String> it;
        long j10;
        String[] split;
        long j11;
        long j12;
        long j13;
        String str;
        ArrayList<String> stringArrayList = bundle.getStringArrayList("malicious_record_list");
        if (stringArrayList != null && !stringArrayList.isEmpty()) {
            ArrayMap<String, MaliciousRecord> arrayMap = new ArrayMap<>();
            ArrayList arrayList = new ArrayList();
            long timeInMillis = Calendar.getInstance().getTimeInMillis();
            long g6 = StartupManager.i(this.f130c).g();
            Iterator<String> it2 = stringArrayList.iterator();
            while (it2.hasNext()) {
                String next = it2.next();
                try {
                    split = next.split("#");
                } catch (PatternSyntaxException unused) {
                    it = it2;
                    j10 = timeInMillis;
                    LocalLog.b("StartupManagerMaliciousPrevent", "updateMaliciousRestrictRecord, parse failed for record: " + next);
                }
                if (split.length != 4) {
                    LocalLog.b("StartupManagerMaliciousPrevent", "updateMaliciousRestrictRecord, parse failed for record: " + next);
                } else {
                    String str2 = split[0];
                    String str3 = split[1];
                    String str4 = split[2];
                    String str5 = split[3];
                    if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str3) && !TextUtils.isEmpty(str4) && !TextUtils.isEmpty(str5)) {
                        try {
                            it = it2;
                            j11 = Long.parseLong(str4);
                            j12 = Long.parseLong(str5);
                        } catch (NumberFormatException unused2) {
                            LocalLog.b("StartupManagerMaliciousPrevent", "updateMaliciousRestrictRecord, parse failed for record: " + next);
                            it = it2;
                            j11 = timeInMillis;
                            j12 = g6;
                        }
                        String str6 = str2 + str3 + j12;
                        j10 = timeInMillis;
                        MaliciousRecord maliciousRecord = arrayMap.get(str6);
                        if (maliciousRecord == null) {
                            j13 = j12;
                            str = str3;
                            arrayMap.put(str6, new MaliciousRecord("malicious_restrict", str2, 1L, j11, j13, str));
                        } else {
                            j13 = j12;
                            str = str3;
                            maliciousRecord.f20319d++;
                            maliciousRecord.f20320e = j11;
                        }
                        arrayList.add(new MaliciousDetailRecord(str2, j11, j13, str));
                    } else {
                        it = it2;
                        j10 = timeInMillis;
                        LocalLog.b("StartupManagerMaliciousPrevent", "updateMaliciousRestrictRecord, parse failed for record: " + next);
                    }
                    it2 = it;
                    timeInMillis = j10;
                }
            }
            if (!arrayMap.isEmpty()) {
                this.f131d.v().L(arrayMap);
            }
            if (arrayList.isEmpty()) {
                return;
            }
            this.f131d.v().C(arrayList);
            this.f131d.v().b(g6, this.f135h);
            return;
        }
        LocalLog.l("StartupManagerMaliciousPrevent", "updateMaliciousRestrictRecord, get null record list");
    }
}
