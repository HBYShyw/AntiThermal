package aa;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArrayMap;
import b6.LocalLog;
import com.oplus.deepthinker.sdk.app.DataLinkConstants;
import com.oplus.startupapp.data.database.RecordDatabase;
import com.oplus.statistics.DataTypeConstants;
import com.oplus.statistics.OplusTrack;
import java.io.Closeable;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import z9.UnstableRecord;

/* compiled from: UnstableAppUtils.java */
/* renamed from: aa.j, reason: use source file name */
/* loaded from: classes2.dex */
public class UnstableAppUtils {

    /* renamed from: o, reason: collision with root package name */
    private static final List<String> f189o = Arrays.asList("Activity", "Service", "Broadcast", "Provider");

    /* renamed from: p, reason: collision with root package name */
    private static volatile UnstableAppUtils f190p = null;

    /* renamed from: j, reason: collision with root package name */
    private Context f200j;

    /* renamed from: n, reason: collision with root package name */
    private RecordDatabase f204n;

    /* renamed from: a, reason: collision with root package name */
    private final SimpleDateFormat f191a = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());

    /* renamed from: b, reason: collision with root package name */
    private boolean f192b = false;

    /* renamed from: c, reason: collision with root package name */
    private boolean f193c = false;

    /* renamed from: d, reason: collision with root package name */
    private int f194d = DataLinkConstants.RUS_UPDATE;

    /* renamed from: e, reason: collision with root package name */
    private int f195e = 10;

    /* renamed from: f, reason: collision with root package name */
    private long f196f = 3600000;

    /* renamed from: g, reason: collision with root package name */
    private int f197g = DataTypeConstants.USER_ACTION;

    /* renamed from: h, reason: collision with root package name */
    private int f198h = DataTypeConstants.APP_LOG;

    /* renamed from: i, reason: collision with root package name */
    private int f199i = 4;

    /* renamed from: k, reason: collision with root package name */
    private Handler f201k = null;

    /* renamed from: l, reason: collision with root package name */
    private boolean f202l = true;

    /* renamed from: m, reason: collision with root package name */
    private ArrayMap<String, a> f203m = new ArrayMap<>();

    private UnstableAppUtils(Context context) {
        this.f200j = context;
        if (Application.getProcessName().equals("com.oplus.battery:remote")) {
            return;
        }
        this.f204n = RecordDatabase.u(context);
    }

    public static UnstableAppUtils b(Context context) {
        if (f190p == null) {
            synchronized (UnstableAppUtils.class) {
                if (f190p == null) {
                    f190p = new UnstableAppUtils(context);
                }
            }
        }
        return f190p;
    }

    private ArrayList<String> e() {
        ArrayList<String> arrayList = new ArrayList<>();
        Iterator<String> it = this.f203m.keySet().iterator();
        while (it.hasNext()) {
            arrayList.add(this.f203m.get(it.next()).f205a);
        }
        return arrayList;
    }

    private String f(String str, String str2) {
        int length;
        if (str2 != null && !str2.contains("crash")) {
            List<String> list = f189o;
            if (list.contains(str2)) {
                int indexOf = list.indexOf(str2);
                if (indexOf < this.f199i - 1) {
                    length = str.indexOf(list.get(indexOf + 1));
                } else {
                    list.get(indexOf);
                    length = str.length();
                }
                int indexOf2 = str.indexOf(str2) + str2.length();
                int parseInt = Integer.parseInt(str.substring(indexOf2, length)) + 1;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str.substring(0, indexOf2));
                sb2.append(parseInt);
                if (indexOf < this.f199i - 1) {
                    sb2.append(str.substring(length, str.length()));
                }
                return sb2.toString();
            }
        }
        LocalLog.a("StartupManager_Unstable", "getUnstableReasonMap: no prevent record before : " + str2);
        return str;
    }

    private void g(a aVar, String str, String str2, String str3) {
        List<UnstableRecord> z10 = this.f204n.v().z(aVar.f208d);
        if (z10 == null || z10.isEmpty()) {
            this.f204n.v().F(new UnstableRecord(aVar.f209e, aVar.f206b, aVar.f207c, aVar.f211g, str, str2, aVar.f208d, str3, this.f191a.format(new Date(aVar.f206b)), aVar.f205a));
        }
    }

    public static String l(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        int length = str.length() / 3;
        if (length == 0) {
            return str.replaceAll(".", "*");
        }
        int i10 = length * 2;
        return new StringBuilder(str).replace(length, i10, str.substring(length, i10).replaceAll(".", "*")).toString();
    }

    private void m(String str, boolean z10) {
        LocalLog.a("StartupManager_Unstable", "setUnstableAppInfoToSystem:  " + l(str));
        try {
            Bundle bundle = new Bundle();
            bundle.putString("updatelist", "unstable");
            bundle.putBoolean("isRestrict", z10);
            bundle.putString("unstablePkg", str);
            bundle.putLong("unstableRestrictInterval", this.f196f);
            bundle.putInt("userId", UserHandle.myUserId());
            StartupDataSyncUtils.f(bundle);
        } catch (NoClassDefFoundError e10) {
            LocalLog.a("StartupManager_Unstable", "setUnstableAppInfoToSystem error" + e10);
        }
    }

    private void n(a aVar, String str, String str2, String str3) {
        List<UnstableRecord> z10 = this.f204n.v().z(aVar.f208d);
        if (z10 != null && !z10.isEmpty()) {
            for (UnstableRecord unstableRecord : z10) {
                unstableRecord.a(aVar.f206b, aVar.f207c, aVar.f211g, str, str2, this.f191a.format(new Date(aVar.f206b)));
                this.f204n.v().O(unstableRecord);
            }
            return;
        }
        this.f204n.v().F(new UnstableRecord(aVar.f209e, aVar.f206b, aVar.f207c, aVar.f211g, str, str2, aVar.f208d, str3, this.f191a.format(new Date(aVar.f206b)), aVar.f205a));
    }

    public void a(String str) {
        if (!e().contains(str)) {
            LocalLog.a("StartupManager_Unstable", "derestrictUnstableApp: not unstable app :" + str);
            return;
        }
        LocalLog.a("StartupManager_Unstable", "derestrictUnstableApp: the app has been updated, packageName: " + str + "  MyUserId: " + UserHandle.myUserId());
        String c10 = c(str, UserHandle.myUserId());
        this.f204n.v().k(c10);
        this.f203m.remove(c10);
        m(str, false);
    }

    public String c(String str, int i10) {
        return str + "|" + i10;
    }

    public List<String> d() {
        return o(StartupDataUtils.h(this.f200j).e("sys_startupmanager_config_list"));
    }

    public void h(Handler handler) {
        this.f201k = handler;
        i(false, null);
        List<UnstableRecord> y4 = this.f204n.v().y();
        if (y4 == null || y4.isEmpty()) {
            return;
        }
        for (UnstableRecord unstableRecord : y4) {
            String str = unstableRecord.f20342g;
            int i10 = (int) unstableRecord.f20338c;
            long j10 = unstableRecord.f20339d;
            long j11 = unstableRecord.f20341f;
            LocalLog.a("StartupManager_Unstable", "recoverRestrictList: from database: pkg: " + str + "user: " + i10);
            a aVar = new a(str, i10, j10, j11);
            this.f203m.put(aVar.f208d, aVar);
        }
    }

    public void i(boolean z10, List<String> list) {
        if (list == null) {
            list = d();
        }
        if (list == null || list.isEmpty()) {
            return;
        }
        String str = list.get(0);
        String str2 = list.get(1);
        String str3 = list.get(2);
        String str4 = list.get(3);
        if (str.equals("true")) {
            this.f192b = true;
        } else if (str.equals("false")) {
            this.f192b = false;
        }
        this.f193c = true;
        this.f194d = Integer.parseInt(str2);
        this.f195e = Integer.parseInt(str3);
        this.f196f = Long.parseLong(str4);
        if (!z10 || this.f192b) {
            return;
        }
        Iterator<String> it = e().iterator();
        while (it.hasNext()) {
            m(it.next(), false);
        }
    }

    public void j(Bundle bundle) {
        int i10 = bundle.getInt("userId");
        long j10 = bundle.getLong("unstableTime");
        String string = bundle.getString("reason");
        String string2 = bundle.getString("packageName");
        String string3 = bundle.getString("exceptionClass");
        String string4 = bundle.getString("exceptionMsg");
        boolean z10 = bundle.getBoolean("unstable_restrict_switch");
        if (string != null && string2 != null && string3 != null && string4 != null) {
            String c10 = c(string2, i10);
            a aVar = this.f203m.get(c10);
            if (aVar == null) {
                this.f203m.put(c10, new a(string2, i10, j10));
                return;
            }
            if (aVar.k(j10, this.f194d)) {
                if (aVar.j(this.f195e)) {
                    a.b(aVar);
                    aVar.l();
                    aVar.f207c = System.currentTimeMillis();
                    this.f203m.put(c10, aVar);
                    if (this.f192b) {
                        m(string2, true);
                    } else if (!this.f193c && z10) {
                        this.f192b = true;
                        m(string2, true);
                    }
                    n(aVar, string3, string4, string);
                    return;
                }
                return;
            }
            aVar.f210f = 1;
            this.f203m.put(c10, aVar);
            return;
        }
        LocalLog.a("StartupManager_Unstable", "onUpdateUnstableRecord: null");
    }

    public void k(Bundle bundle) {
        int i10;
        String string = bundle.getString("packageName");
        String string2 = bundle.getString("reason");
        long j10 = bundle.getLong("unstableTime");
        String string3 = bundle.getString("unstable_record_type");
        if (string3 != null && "prevent".equals(string3)) {
            i10 = this.f197g;
        } else {
            if (string3 == null || !"derestrict".equals(string3)) {
                LocalLog.a("StartupManager_Unstable", "onUpdateUnstableWorkRecord: get unstable record type failed: " + string3);
                return;
            }
            i10 = this.f198h;
        }
        int i11 = i10;
        List<UnstableRecord> z10 = this.f204n.v().z(c(string, i11));
        if (!z10.isEmpty()) {
            for (UnstableRecord unstableRecord : z10) {
                unstableRecord.f20341f++;
                unstableRecord.f20345j = f(unstableRecord.f20345j, string2);
                this.f204n.v().O(unstableRecord);
            }
            return;
        }
        a aVar = new a(string, i11, j10);
        a.b(aVar);
        StringBuilder sb2 = new StringBuilder();
        for (String str : f189o) {
            if (str.equals(string2)) {
                sb2.append(str);
                sb2.append(1);
            } else {
                sb2.append(str);
                sb2.append(0);
            }
        }
        g(aVar, string3, String.valueOf(i11), sb2.toString());
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00ba  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00c0 A[Catch: IOException -> 0x00c6, TRY_LEAVE, TryCatch #4 {IOException -> 0x00c6, blocks: (B:45:0x00b6, B:48:0x00bc, B:50:0x00c0), top: B:44:0x00b6 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public List<String> o(String str) {
        StringReader stringReader;
        XmlPullParser newPullParser;
        int next;
        ArrayList arrayList = new ArrayList();
        if (str == null || str.isEmpty()) {
            return arrayList;
        }
        LocalLog.a("StartupManager_Unstable", "updateUnstableConfig: start update unstable config from RUS");
        Closeable closeable = null;
        try {
            try {
                newPullParser = XmlPullParserFactory.newInstance().newPullParser();
                try {
                } catch (Exception e10) {
                    e = e10;
                    stringReader = null;
                } catch (Throwable th) {
                    th = th;
                    stringReader = null;
                }
            } catch (Exception e11) {
                e = e11;
                stringReader = null;
            } catch (Throwable th2) {
                th = th2;
                stringReader = null;
            }
        } catch (IOException e12) {
            e12.printStackTrace();
        }
        if (!str.isEmpty()) {
            stringReader = new StringReader(str);
            try {
                newPullParser.setInput(stringReader);
                newPullParser.nextTag();
                do {
                    next = newPullParser.next();
                    if (next == 2 && "unstableRestrictConfig".equals(newPullParser.getName())) {
                        String attributeValue = newPullParser.getAttributeValue(null, "isRestrict");
                        String attributeValue2 = newPullParser.getAttributeValue(null, "crashInterval");
                        String attributeValue3 = newPullParser.getAttributeValue(null, "crashUploadInterval");
                        String attributeValue4 = newPullParser.getAttributeValue(null, "restrictInterval");
                        arrayList.add(attributeValue);
                        arrayList.add(attributeValue2);
                        arrayList.add(attributeValue3);
                        arrayList.add(attributeValue4);
                    }
                } while (next != 1);
                if (newPullParser instanceof Closeable) {
                    ((Closeable) newPullParser).close();
                } else {
                    stringReader.close();
                }
            } catch (Exception e13) {
                e = e13;
                closeable = newPullParser;
                try {
                    LocalLog.a("StartupManager_Unstable", "updateUnstableConfig:  failed");
                    e.printStackTrace();
                    if (closeable instanceof Closeable) {
                        closeable.close();
                    } else if (stringReader != null) {
                        stringReader.close();
                    }
                    return arrayList;
                } catch (Throwable th3) {
                    th = th3;
                    try {
                        if (!(closeable instanceof Closeable)) {
                            closeable.close();
                        } else if (stringReader != null) {
                            stringReader.close();
                        }
                    } catch (IOException e14) {
                        e14.printStackTrace();
                    }
                    throw th;
                }
            } catch (Throwable th4) {
                th = th4;
                closeable = newPullParser;
                if (!(closeable instanceof Closeable)) {
                }
                throw th;
            }
            return arrayList;
        }
        try {
            if (newPullParser instanceof Closeable) {
                ((Closeable) newPullParser).close();
            }
        } catch (IOException e15) {
            e15.printStackTrace();
        }
        return arrayList;
    }

    public void p() {
        ArrayList arrayList = new ArrayList();
        List<UnstableRecord> y4 = this.f204n.v().y();
        if (y4 != null && !y4.isEmpty()) {
            for (UnstableRecord unstableRecord : y4) {
                String str = unstableRecord.f20337b;
                String str2 = unstableRecord.f20342g;
                String str3 = unstableRecord.f20346k;
                String str4 = unstableRecord.f20343h;
                String str5 = unstableRecord.f20344i;
                String valueOf = String.valueOf(unstableRecord.f20341f);
                arrayList.add(str);
                long j10 = unstableRecord.f20338c;
                q(str2, str3, str4, (j10 == ((long) this.f197g) || j10 == ((long) this.f198h)) ? unstableRecord.f20345j : str5, valueOf);
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                String str6 = (String) it.next();
                this.f204n.v().k(str6);
                this.f203m.remove(str6);
            }
            return;
        }
        this.f203m.clear();
    }

    public void q(String str, String str2, String str3, String str4, String str5) {
        HashMap hashMap = new HashMap();
        hashMap.put("package_name", str);
        hashMap.put("crash_time", str2);
        hashMap.put("frequent_crash_count", str5);
        hashMap.put("exception_class", str3);
        hashMap.put("exception_message", str4);
        OplusTrack.onCommon(this.f200j, "20092", "unstable_crash", hashMap);
        LocalLog.a("StartupManager_Unstable", "uploadAppCrashWithHistory: who=" + str + "\n  ,frequentCrashCount=" + str5 + "\n  ,crashTimes=" + str2 + "\n  ,exceptionClasses=" + str3 + "\n  ,exceptionMsg=" + str4);
    }

    /* compiled from: UnstableAppUtils.java */
    /* renamed from: aa.j$a */
    /* loaded from: classes2.dex */
    public class a {

        /* renamed from: a, reason: collision with root package name */
        private String f205a;

        /* renamed from: b, reason: collision with root package name */
        private long f206b;

        /* renamed from: c, reason: collision with root package name */
        private long f207c;

        /* renamed from: d, reason: collision with root package name */
        private String f208d;

        /* renamed from: e, reason: collision with root package name */
        private int f209e;

        /* renamed from: f, reason: collision with root package name */
        private int f210f;

        /* renamed from: g, reason: collision with root package name */
        private long f211g;

        /* renamed from: h, reason: collision with root package name */
        private int f212h;

        /* renamed from: i, reason: collision with root package name */
        private int f213i;

        /* renamed from: j, reason: collision with root package name */
        private int f214j;

        /* renamed from: k, reason: collision with root package name */
        private int f215k;

        public a(String str, int i10, long j10) {
            this.f210f = 1;
            this.f211g = 0L;
            this.f212h = 0;
            this.f213i = 0;
            this.f214j = 0;
            this.f215k = 0;
            this.f205a = str;
            this.f209e = i10;
            this.f206b = j10;
            this.f208d = UnstableAppUtils.this.c(str, i10);
        }

        static /* synthetic */ long b(a aVar) {
            long j10 = aVar.f211g;
            aVar.f211g = 1 + j10;
            return j10;
        }

        public boolean j(long j10) {
            int i10 = this.f210f + 1;
            this.f210f = i10;
            return ((long) i10) >= j10;
        }

        public boolean k(long j10, int i10) {
            long j11 = this.f206b;
            if (j10 - j11 < i10 && j10 - j11 >= 0) {
                this.f206b = j10;
                return true;
            }
            this.f206b = j10;
            return false;
        }

        public void l() {
            this.f210f = 0;
        }

        public a(String str, int i10, long j10, long j11) {
            this.f210f = 1;
            this.f212h = 0;
            this.f213i = 0;
            this.f214j = 0;
            this.f215k = 0;
            this.f205a = str;
            this.f209e = i10;
            this.f206b = j10;
            this.f211g = j11;
            this.f208d = UnstableAppUtils.this.c(str, i10);
        }
    }
}
