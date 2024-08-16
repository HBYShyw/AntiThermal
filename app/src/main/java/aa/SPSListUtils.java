package aa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import b6.LocalLog;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.domainmanager.DeviceDomainManager;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.json.JSONArray;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserFactory;

/* compiled from: SPSListUtils.java */
/* renamed from: aa.e, reason: use source file name */
/* loaded from: classes2.dex */
public class SPSListUtils {
    public static final String A;
    private static SPSListUtils B;

    /* renamed from: a, reason: collision with root package name */
    private Context f141a;

    /* renamed from: b, reason: collision with root package name */
    private boolean f142b = false;

    /* renamed from: c, reason: collision with root package name */
    private boolean f143c = false;

    /* renamed from: d, reason: collision with root package name */
    private String f144d = "sps_config_list";

    /* renamed from: e, reason: collision with root package name */
    private String f145e = "SPS_white_pkg_list";

    /* renamed from: f, reason: collision with root package name */
    private String f146f = "SPS_white_activity_list";

    /* renamed from: g, reason: collision with root package name */
    private String f147g = "SPS_white_service_list";

    /* renamed from: h, reason: collision with root package name */
    private String f148h = "SPS_white_broadcast_list";

    /* renamed from: i, reason: collision with root package name */
    private String f149i = "SPS_white_provider_list";

    /* renamed from: j, reason: collision with root package name */
    private String f150j = "SPS_white_action_list";

    /* renamed from: k, reason: collision with root package name */
    private String f151k = "SPS_black_list";

    /* renamed from: l, reason: collision with root package name */
    private String f152l = "SPS_desktop_list";

    /* renamed from: m, reason: collision with root package name */
    private String f153m = "SPS_mode";

    /* renamed from: n, reason: collision with root package name */
    private ArrayList<String> f154n = new ArrayList<>();

    /* renamed from: o, reason: collision with root package name */
    private ArrayList<String> f155o = new ArrayList<>();

    /* renamed from: p, reason: collision with root package name */
    private ArrayList<String> f156p = new ArrayList<>();

    /* renamed from: q, reason: collision with root package name */
    private ArrayList<String> f157q = new ArrayList<>();

    /* renamed from: r, reason: collision with root package name */
    private ArrayList<String> f158r = new ArrayList<>();

    /* renamed from: s, reason: collision with root package name */
    private ArrayList<String> f159s = new ArrayList<>();

    /* renamed from: t, reason: collision with root package name */
    private ArrayList<String> f160t = new ArrayList<>();

    /* renamed from: u, reason: collision with root package name */
    private ArrayList<String> f161u = new ArrayList<>();

    /* renamed from: v, reason: collision with root package name */
    private ArrayList<String> f162v = new ArrayList<>(Arrays.asList("jp.co.daj.consumer.ifilter.shop", "jp.co.daj.consumer.ifilter", "jp.netstar.familysmile", "com.kddi.familysmile.mvno", "jp.softbank.mb.parentalcontrols"));

    /* renamed from: w, reason: collision with root package name */
    private boolean f163w = false;

    /* renamed from: x, reason: collision with root package name */
    private ContentObserver f164x = null;

    /* renamed from: y, reason: collision with root package name */
    private ContentObserver f165y = null;

    /* renamed from: z, reason: collision with root package name */
    private Lock f166z = new ReentrantLock();

    /* compiled from: SPSListUtils.java */
    /* renamed from: aa.e$a */
    /* loaded from: classes2.dex */
    class a extends ContentObserver {
        a(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            LocalLog.a("SPSListUtils", "SuperPower mode settings changed!");
            super.onChange(z10);
            SPSListUtils.this.f();
        }
    }

    /* compiled from: SPSListUtils.java */
    /* renamed from: aa.e$b */
    /* loaded from: classes2.dex */
    class b extends ContentObserver {
        b(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            LocalLog.a("SPSListUtils", "DesktopList changed!");
            super.onChange(z10);
            SPSListUtils.this.e();
        }
    }

    static {
        StringBuilder sb2 = new StringBuilder();
        String str = File.separator;
        sb2.append(str);
        sb2.append("data");
        sb2.append(str);
        sb2.append("oplus");
        sb2.append(str);
        sb2.append("os");
        sb2.append(str);
        sb2.append("startup");
        sb2.append(str);
        A = sb2.toString();
        B = null;
    }

    private SPSListUtils(Context context) {
        this.f141a = context;
        h();
    }

    public static synchronized SPSListUtils c(Context context) {
        SPSListUtils sPSListUtils;
        synchronized (SPSListUtils.class) {
            if (B == null) {
                B = new SPSListUtils(context);
            }
            sPSListUtils = B;
        }
        return sPSListUtils;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        o();
        m();
        l();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        o();
        m();
        l();
        if (this.f163w) {
            Intent intent = new Intent("oplus.intent.action.REQUEST_APP_CLEAN_RUNNING");
            intent.putExtra("caller_package", "com.oplus.safecenter");
            intent.putExtra("filterapplist", this.f161u);
            intent.setPackage("com.oplus.athena");
            this.f141a.startService(intent);
        }
    }

    public boolean d() {
        boolean z10 = Settings.System.getIntForUser(this.f141a.getContentResolver(), "super_powersave_mode_state", 0, 0) == 1;
        LocalLog.a("SPSListUtils", "Get SuperPower mode state: inSuperPowerMode: " + z10);
        return z10;
    }

    public void g(Handler handler) {
        this.f164x = new a(handler);
        this.f165y = new b(handler);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:17:0x00b9 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00ba  */
    /* JADX WARN: Type inference failed for: r6v2 */
    /* JADX WARN: Type inference failed for: r6v3 */
    /* JADX WARN: Type inference failed for: r6v4, types: [java.io.BufferedReader] */
    /* JADX WARN: Type inference failed for: r6v9 */
    /* JADX WARN: Type inference failed for: r7v1, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r7v2 */
    /* JADX WARN: Type inference failed for: r7v25, types: [java.io.FileReader, java.io.Reader] */
    /* JADX WARN: Type inference failed for: r7v3 */
    /* JADX WARN: Type inference failed for: r7v4, types: [java.io.FileReader] */
    /* JADX WARN: Type inference failed for: r7v5, types: [java.io.FileReader] */
    @SuppressLint({"NewApi"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean h() {
        BufferedReader bufferedReader;
        StringBuilder sb2;
        ArrayList<String> arrayList = new ArrayList();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(A);
        ?? r72 = "sps_list.txt";
        sb3.append("sps_list.txt");
        File file = new File(sb3.toString());
        ?? r62 = 0;
        r62 = 0;
        String str = null;
        try {
            try {
                r72 = new FileReader(file, StandardCharsets.UTF_8);
                try {
                    bufferedReader = new BufferedReader(r72);
                    while (true) {
                        try {
                            String readLine = bufferedReader.readLine();
                            if (readLine != null) {
                                arrayList.add(readLine);
                            } else {
                                try {
                                    break;
                                } catch (IOException e10) {
                                    LocalLog.a("SPSListUtils", "readSPSList br.close " + e10);
                                }
                            }
                        } catch (IOException e11) {
                            e = e11;
                            LocalLog.a("SPSListUtils", "readSPSList " + e);
                            if (bufferedReader != null) {
                                try {
                                    bufferedReader.close();
                                } catch (IOException e12) {
                                    LocalLog.a("SPSListUtils", "readSPSList br.close " + e12);
                                }
                            }
                            if (r72 != 0) {
                                try {
                                    r72.close();
                                } catch (IOException e13) {
                                    e = e13;
                                    sb2 = new StringBuilder();
                                    sb2.append("readSPSList reader.close ");
                                    sb2.append(e);
                                    LocalLog.a("SPSListUtils", sb2.toString());
                                    if (!arrayList.isEmpty()) {
                                    }
                                }
                            }
                            if (!arrayList.isEmpty()) {
                            }
                        }
                    }
                    bufferedReader.close();
                } catch (IOException e14) {
                    e = e14;
                    bufferedReader = null;
                } catch (Throwable th) {
                    th = th;
                    if (r62 != 0) {
                        try {
                            r62.close();
                        } catch (IOException e15) {
                            LocalLog.a("SPSListUtils", "readSPSList br.close " + e15);
                        }
                    }
                    if (r72 != 0) {
                        try {
                            r72.close();
                            throw th;
                        } catch (IOException e16) {
                            LocalLog.a("SPSListUtils", "readSPSList reader.close " + e16);
                            throw th;
                        }
                    }
                    throw th;
                }
            } catch (IOException e17) {
                e = e17;
                bufferedReader = null;
                r72 = 0;
            } catch (Throwable th2) {
                th = th2;
                r72 = 0;
            }
            try {
                r72.close();
            } catch (IOException e18) {
                e = e18;
                sb2 = new StringBuilder();
                sb2.append("readSPSList reader.close ");
                sb2.append(e);
                LocalLog.a("SPSListUtils", sb2.toString());
                if (!arrayList.isEmpty()) {
                }
            }
            if (!arrayList.isEmpty()) {
                return false;
            }
            try {
                try {
                    this.f166z.lock();
                    for (String str2 : arrayList) {
                        if (str2 != null) {
                            if (str2.contains(":")) {
                                str = str2;
                            } else if (!TextUtils.isEmpty(str)) {
                                char c10 = 65535;
                                switch (str.hashCode()) {
                                    case -1544198824:
                                        if (str.equals("SPSActivityWhiteList:")) {
                                            c10 = 2;
                                            break;
                                        }
                                        break;
                                    case -349576482:
                                        if (str.equals("SPSBroadcastWhiteList:")) {
                                            c10 = 4;
                                            break;
                                        }
                                        break;
                                    case 692562499:
                                        if (str.equals("SPSWhitePkgList:")) {
                                            c10 = 0;
                                            break;
                                        }
                                        break;
                                    case 1249771839:
                                        if (str.equals("SPSActionWhiteList:")) {
                                            c10 = 6;
                                            break;
                                        }
                                        break;
                                    case 1292424185:
                                        if (str.equals("SPSBlackpkgList:")) {
                                            c10 = 1;
                                            break;
                                        }
                                        break;
                                    case 1682228666:
                                        if (str.equals("SPSProviderWhiteList:")) {
                                            c10 = 5;
                                            break;
                                        }
                                        break;
                                    case 1885928018:
                                        if (str.equals("SPSServiceWhiteList:")) {
                                            c10 = 3;
                                            break;
                                        }
                                        break;
                                }
                                switch (c10) {
                                    case 0:
                                        this.f154n.add(str2);
                                        break;
                                    case 1:
                                        this.f160t.add(str2);
                                        break;
                                    case 2:
                                        this.f155o.add(str2);
                                        break;
                                    case 3:
                                        this.f156p.add(str2);
                                        break;
                                    case 4:
                                        this.f157q.add(str2);
                                        break;
                                    case 5:
                                        this.f158r.add(str2);
                                        break;
                                    case 6:
                                        this.f159s.add(str2);
                                        break;
                                }
                            }
                        }
                    }
                } finally {
                    this.f166z.unlock();
                }
            } catch (Exception e19) {
                LocalLog.a("SPSListUtils", "readSPSList " + e19);
            }
            return true;
        } catch (Throwable th3) {
            th = th3;
            r62 = file;
        }
    }

    public void i() {
        LocalLog.a("SPSListUtils", "registerDesktopListObserver");
        try {
            this.f141a.getContentResolver().registerContentObserver(Settings.System.getUriFor("super_power_save_desktop_app_list"), false, this.f165y);
            this.f143c = true;
        } catch (Exception e10) {
            LocalLog.a("SPSListUtils", "registerDesktopListObserver " + e10);
        }
    }

    public void j() {
        LocalLog.a("SPSListUtils", "registerSuperPowerModeObserver");
        try {
            this.f141a.getContentResolver().registerContentObserver(Settings.System.getUriFor("super_powersave_mode_state"), false, this.f164x);
            this.f142b = true;
        } catch (Exception e10) {
            LocalLog.a("SPSListUtils", "registerSuperPowerModeObserver " + e10);
        }
    }

    public void k() {
        StringBuilder sb2;
        ArrayList arrayList = new ArrayList();
        arrayList.clear();
        arrayList.add("SPSWhitePkgList:");
        arrayList.addAll(this.f154n);
        arrayList.add("SPSBlackpkgList:");
        arrayList.addAll(this.f160t);
        arrayList.add("SPSActivityWhiteList:");
        arrayList.addAll(this.f155o);
        arrayList.add("SPSServiceWhiteList:");
        arrayList.addAll(this.f156p);
        arrayList.add("SPSBroadcastWhiteList:");
        arrayList.addAll(this.f157q);
        arrayList.add("SPSProviderWhiteList:");
        arrayList.addAll(this.f158r);
        arrayList.add("SPSActionWhiteList:");
        arrayList.addAll(this.f159s);
        FileOutputStream fileOutputStream = null;
        try {
            try {
                String str = A;
                if (new File(str).exists() || new File(str).mkdirs()) {
                    FileOutputStream fileOutputStream2 = new FileOutputStream(new File(str + "sps_list.txt"));
                    try {
                        Iterator it = arrayList.iterator();
                        while (it.hasNext()) {
                            fileOutputStream2.write((((String) it.next()) + "\n").getBytes(StandardCharsets.UTF_8));
                        }
                        fileOutputStream = fileOutputStream2;
                    } catch (Exception e10) {
                        e = e10;
                        fileOutputStream = fileOutputStream2;
                        LocalLog.a("SPSListUtils", "saveSPSList " + e);
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                                return;
                            } catch (Exception e11) {
                                e = e11;
                                sb2 = new StringBuilder();
                                sb2.append("saveSPSList finally ");
                                sb2.append(e);
                                LocalLog.a("SPSListUtils", sb2.toString());
                            }
                        }
                        return;
                    } catch (Throwable th) {
                        th = th;
                        fileOutputStream = fileOutputStream2;
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (Exception e12) {
                                LocalLog.a("SPSListUtils", "saveSPSList finally " + e12);
                            }
                        }
                        throw th;
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (Exception e13) {
                        e = e13;
                        sb2 = new StringBuilder();
                        sb2.append("saveSPSList finally ");
                        sb2.append(e);
                        LocalLog.a("SPSListUtils", sb2.toString());
                    }
                }
            } catch (Exception e14) {
                e = e14;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public void l() {
        LocalLog.a("SPSListUtils", "setSPSListToSystem start");
        try {
            try {
                Bundle bundle = new Bundle();
                this.f166z.lock();
                if (!this.f154n.contains("jp.co.daj.consumer.ifilter")) {
                    this.f154n.addAll(this.f162v);
                }
                bundle.putStringArrayList(this.f145e, this.f154n);
                bundle.putStringArrayList(this.f146f, this.f155o);
                bundle.putStringArrayList(this.f147g, this.f156p);
                bundle.putStringArrayList(this.f148h, this.f157q);
                bundle.putStringArrayList(this.f149i, this.f158r);
                bundle.putStringArrayList(this.f150j, this.f159s);
                bundle.putStringArrayList(this.f151k, this.f160t);
                bundle.putStringArrayList(this.f152l, this.f161u);
                bundle.putBoolean(this.f153m, this.f163w);
                bundle.putString("updatelist", this.f144d);
                StartupDataSyncUtils.f(bundle);
                LocalLog.a("SPSListUtils", "setSPSListToSystem success");
            } catch (NoClassDefFoundError e10) {
                LocalLog.a("SPSListUtils", "setSPSListToSystem error" + e10);
            }
        } finally {
            this.f166z.unlock();
        }
    }

    public void m() {
        ArrayList arrayList = new ArrayList();
        String stringForUser = Settings.System.getStringForUser(this.f141a.getContentResolver(), "super_power_save_desktop_app_list", 0);
        if (stringForUser != null) {
            try {
                JSONArray jSONArray = new JSONArray(stringForUser);
                for (int i10 = 0; i10 < jSONArray.length(); i10++) {
                    arrayList.add(jSONArray.getJSONObject(i10).getString("packageName"));
                }
            } catch (JSONException e10) {
                LocalLog.a("SPSListUtils", "updateSPSDesktopList " + e10);
            }
        }
        synchronized (this.f161u) {
            this.f161u.clear();
            this.f161u.addAll(arrayList);
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(7:1|(2:2|3)|(3:175|176|(14:178|179|180|181|12|(7:13|14|15|(4:47|48|49|(6:81|82|83|84|85|(1:99)(3:89|(1:(1:97))|98))(4:51|(2:53|(1:55))(2:56|(2:58|(1:60))(2:61|(2:63|(1:65))(2:66|(2:68|(1:70))(2:71|(2:73|(1:75))(2:76|(2:78|(1:80)))))))|19|(1:22)(1:21)))(1:17)|18|19|(0)(0))|23|(1:25)(1:(1:42)(1:(1:44)))|26|27|28|29|30|31))|5|6|(14:8|9|10|11|12|(8:13|14|15|(0)(0)|18|19|(0)(0)|21)|23|(0)(0)|26|27|28|29|30|31)(4:159|160|161|(2:163|165)(1:166))|(2:(0)|(1:39))) */
    /* JADX WARN: Code restructure failed: missing block: B:170:0x01fc, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:171:0x01fd, code lost:
    
        r17 = "updateSPSList ";
        r2 = "updateSPSList finally ";
        r5 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:173:0x01f5, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:174:0x01f6, code lost:
    
        r2 = "updateSPSList finally ";
        r5 = null;
        r15 = 0;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:109:0x022d A[Catch: IOException -> 0x023f, TryCatch #16 {IOException -> 0x023f, blocks: (B:107:0x0229, B:109:0x022d, B:114:0x0235, B:116:0x023b), top: B:106:0x0229 }] */
    /* JADX WARN: Removed duplicated region for block: B:113:0x0233  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x02cb  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x02d7 A[Catch: IOException -> 0x02dd, TRY_LEAVE, TryCatch #19 {IOException -> 0x02dd, blocks: (B:122:0x02c7, B:126:0x02cf, B:129:0x02d3, B:131:0x02d7), top: B:121:0x02c7 }] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x017c  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x01b5 A[LOOP:0: B:13:0x007d->B:21:0x01b5, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0184 A[EDGE_INSN: B:22:0x0184->B:23:0x0184 BREAK  A[LOOP:0: B:13:0x007d->B:21:0x01b5], EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0188 A[Catch: IOException -> 0x019a, TryCatch #0 {IOException -> 0x019a, blocks: (B:23:0x0184, B:25:0x0188, B:42:0x0190, B:44:0x0196), top: B:22:0x0184 }] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x018e  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0084 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r14v1, types: [org.xmlpull.v1.XmlPullParser] */
    /* JADX WARN: Type inference failed for: r15v0 */
    /* JADX WARN: Type inference failed for: r15v1 */
    /* JADX WARN: Type inference failed for: r15v13, types: [java.io.FileInputStream] */
    /* JADX WARN: Type inference failed for: r15v14 */
    /* JADX WARN: Type inference failed for: r15v15 */
    /* JADX WARN: Type inference failed for: r15v16, types: [java.io.FileInputStream, java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r15v17 */
    /* JADX WARN: Type inference failed for: r15v18 */
    /* JADX WARN: Type inference failed for: r15v19 */
    /* JADX WARN: Type inference failed for: r15v27 */
    /* JADX WARN: Type inference failed for: r15v3 */
    /* JADX WARN: Type inference failed for: r15v4, types: [java.io.FileInputStream] */
    /* JADX WARN: Type inference failed for: r15v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void n(String str) {
        String str2;
        StringReader stringReader;
        Closeable closeable;
        ?? r15;
        String str3;
        FileInputStream fileInputStream;
        Throwable th;
        String str4;
        SPSListUtils sPSListUtils;
        ?? newPullParser;
        FileInputStream fileInputStream2;
        StringReader stringReader2;
        ?? r152;
        int next;
        String str5;
        Object obj;
        String str6 = "updateSPSList ";
        String str7 = "updateSPSList finally ";
        File file = new File("/data/oplus/os/startup/startup_manager.xml");
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        ArrayList arrayList5 = new ArrayList();
        ArrayList arrayList6 = new ArrayList();
        ArrayList arrayList7 = new ArrayList();
        try {
            newPullParser = XmlPullParserFactory.newInstance().newPullParser();
        } catch (Exception e10) {
            e = e10;
            str3 = "updateSPSList ";
            str2 = "updateSPSList finally ";
            stringReader = null;
            closeable = null;
            r15 = 0;
        } catch (Throwable th2) {
            th = th2;
            str2 = "updateSPSList finally ";
            stringReader = null;
            closeable = null;
            r15 = 0;
        }
        if (str != null) {
            try {
            } catch (Exception e11) {
                e = e11;
                str3 = "updateSPSList ";
                str2 = "updateSPSList finally ";
                stringReader = null;
                stringReader2 = stringReader;
                closeable = newPullParser;
                r15 = stringReader2;
                try {
                    StringBuilder sb2 = new StringBuilder();
                    str4 = str3;
                    sb2.append(str4);
                    sb2.append(e);
                    LocalLog.a("SPSListUtils", sb2.toString());
                    try {
                        if (!(closeable instanceof Closeable)) {
                        }
                    } catch (IOException e12) {
                        LocalLog.a("SPSListUtils", str2 + e12);
                    }
                    sPSListUtils = this;
                    try {
                        try {
                            sPSListUtils.f166z.lock();
                            sPSListUtils.f154n.clear();
                            sPSListUtils.f154n.addAll(arrayList);
                            sPSListUtils.f155o.clear();
                            sPSListUtils.f155o.addAll(arrayList6);
                            sPSListUtils.f156p.clear();
                            sPSListUtils.f156p.addAll(arrayList3);
                            sPSListUtils.f157q.clear();
                            sPSListUtils.f157q.addAll(arrayList4);
                            sPSListUtils.f158r.clear();
                            sPSListUtils.f158r.addAll(arrayList5);
                            sPSListUtils.f159s.clear();
                            sPSListUtils.f159s.addAll(arrayList6);
                            sPSListUtils.f160t.clear();
                            sPSListUtils.f160t.addAll(arrayList7);
                            k();
                        } catch (Exception e13) {
                            LocalLog.a("SPSListUtils", str4 + e13);
                        }
                        return;
                    } finally {
                        sPSListUtils.f166z.unlock();
                    }
                } catch (Throwable th3) {
                    th = th3;
                    th = th;
                    fileInputStream = r15;
                    try {
                        if (closeable instanceof Closeable) {
                            closeable.close();
                            throw th;
                        }
                        if (fileInputStream != null) {
                            fileInputStream.close();
                            throw th;
                        }
                        if (stringReader != null) {
                            stringReader.close();
                            throw th;
                        }
                        throw th;
                    } catch (IOException e14) {
                        LocalLog.a("SPSListUtils", str2 + e14);
                        throw th;
                    }
                }
            } catch (Throwable th4) {
                th = th4;
                str2 = "updateSPSList finally ";
                stringReader = null;
                fileInputStream2 = null;
            }
            if (!str.isEmpty()) {
                stringReader = new StringReader(str);
                try {
                    newPullParser.setInput(stringReader);
                    newPullParser.nextTag();
                    r152 = 0;
                    while (true) {
                        try {
                            try {
                                next = newPullParser.next();
                                if (next != 2) {
                                    try {
                                        String name = newPullParser.getName();
                                        str3 = str6;
                                        if ("sp".equals(name)) {
                                            try {
                                                String attributeValue = newPullParser.getAttributeValue(null, DeviceDomainManager.ARG_PKG);
                                                str5 = str7;
                                                try {
                                                    int indexOf = newPullParser.getAttributeValue(null, "mask").indexOf("1");
                                                    if (indexOf != -1 && !"".equals(attributeValue)) {
                                                        if (indexOf != 1 && indexOf != 3) {
                                                            if (indexOf == 0 || indexOf == 2) {
                                                                arrayList7.add(attributeValue);
                                                            }
                                                        }
                                                        arrayList.add(attributeValue);
                                                    } else {
                                                        LocalLog.a("SPSListUtils", "SPSPkgList parse error" + attributeValue);
                                                    }
                                                } catch (Exception e15) {
                                                    e = e15;
                                                    closeable = newPullParser;
                                                    str2 = str5;
                                                    r15 = r152;
                                                    StringBuilder sb22 = new StringBuilder();
                                                    str4 = str3;
                                                    sb22.append(str4);
                                                    sb22.append(e);
                                                    LocalLog.a("SPSListUtils", sb22.toString());
                                                    if (!(closeable instanceof Closeable)) {
                                                    }
                                                    sPSListUtils = this;
                                                    sPSListUtils.f166z.lock();
                                                    sPSListUtils.f154n.clear();
                                                    sPSListUtils.f154n.addAll(arrayList);
                                                    sPSListUtils.f155o.clear();
                                                    sPSListUtils.f155o.addAll(arrayList6);
                                                    sPSListUtils.f156p.clear();
                                                    sPSListUtils.f156p.addAll(arrayList3);
                                                    sPSListUtils.f157q.clear();
                                                    sPSListUtils.f157q.addAll(arrayList4);
                                                    sPSListUtils.f158r.clear();
                                                    sPSListUtils.f158r.addAll(arrayList5);
                                                    sPSListUtils.f159s.clear();
                                                    sPSListUtils.f159s.addAll(arrayList6);
                                                    sPSListUtils.f160t.clear();
                                                    sPSListUtils.f160t.addAll(arrayList7);
                                                    k();
                                                    return;
                                                } catch (Throwable th5) {
                                                    th = th5;
                                                    closeable = newPullParser;
                                                    str2 = str5;
                                                    fileInputStream = r152;
                                                    if (closeable instanceof Closeable) {
                                                    }
                                                }
                                            } catch (Exception e16) {
                                                e = e16;
                                                str2 = str7;
                                                stringReader2 = r152;
                                                closeable = newPullParser;
                                                r15 = stringReader2;
                                                StringBuilder sb222 = new StringBuilder();
                                                str4 = str3;
                                                sb222.append(str4);
                                                sb222.append(e);
                                                LocalLog.a("SPSListUtils", sb222.toString());
                                                if (!(closeable instanceof Closeable)) {
                                                    closeable.close();
                                                } else if (r15 != 0) {
                                                    r15.close();
                                                } else if (stringReader != null) {
                                                    stringReader.close();
                                                }
                                                sPSListUtils = this;
                                                sPSListUtils.f166z.lock();
                                                sPSListUtils.f154n.clear();
                                                sPSListUtils.f154n.addAll(arrayList);
                                                sPSListUtils.f155o.clear();
                                                sPSListUtils.f155o.addAll(arrayList6);
                                                sPSListUtils.f156p.clear();
                                                sPSListUtils.f156p.addAll(arrayList3);
                                                sPSListUtils.f157q.clear();
                                                sPSListUtils.f157q.addAll(arrayList4);
                                                sPSListUtils.f158r.clear();
                                                sPSListUtils.f158r.addAll(arrayList5);
                                                sPSListUtils.f159s.clear();
                                                sPSListUtils.f159s.addAll(arrayList6);
                                                sPSListUtils.f160t.clear();
                                                sPSListUtils.f160t.addAll(arrayList7);
                                                k();
                                                return;
                                            }
                                        } else {
                                            str5 = str7;
                                            obj = null;
                                            if ("SPSWhiteActivity".equals(name)) {
                                                String nextText = newPullParser.nextText();
                                                if (!nextText.equals("")) {
                                                    arrayList2.add(nextText);
                                                }
                                            } else if ("SPSWhiteService".equals(name)) {
                                                String nextText2 = newPullParser.nextText();
                                                if (!nextText2.equals("")) {
                                                    arrayList3.add(nextText2);
                                                }
                                            } else if ("SPSWhiteBroadcast".equals(name)) {
                                                String nextText3 = newPullParser.nextText();
                                                if (!nextText3.equals("")) {
                                                    arrayList4.add(nextText3);
                                                }
                                            } else if ("SPSWhiteProvider".equals(name)) {
                                                String nextText4 = newPullParser.nextText();
                                                if (!nextText4.equals("")) {
                                                    arrayList5.add(nextText4);
                                                }
                                            } else if ("SPSWhiteAction".equals(name)) {
                                                String nextText5 = newPullParser.nextText();
                                                if (!nextText5.equals("")) {
                                                    arrayList6.add(nextText5);
                                                }
                                            } else if ("SPSBlackPkg".equals(name)) {
                                                String nextText6 = newPullParser.nextText();
                                                if (!nextText6.equals("")) {
                                                    arrayList7.add(nextText6);
                                                }
                                            }
                                            if (next == 1) {
                                                try {
                                                    break;
                                                } catch (IOException e17) {
                                                    LocalLog.a("SPSListUtils", str5 + e17);
                                                }
                                            } else {
                                                str7 = str5;
                                                str6 = str3;
                                            }
                                        }
                                    } catch (Throwable th6) {
                                        th = th6;
                                        str2 = str7;
                                        fileInputStream2 = r152;
                                        closeable = newPullParser;
                                        fileInputStream = fileInputStream2;
                                        if (closeable instanceof Closeable) {
                                        }
                                    }
                                } else {
                                    str3 = str6;
                                    str5 = str7;
                                }
                                obj = null;
                                if (next == 1) {
                                }
                            } catch (Throwable th7) {
                                str2 = str7;
                                th = th7;
                                fileInputStream2 = r152;
                            }
                        } catch (Exception e18) {
                            e = e18;
                            str3 = str6;
                        }
                    }
                    if (!(newPullParser instanceof Closeable)) {
                        ((Closeable) newPullParser).close();
                    } else if (r152 != 0) {
                        r152.close();
                    } else if (stringReader != null) {
                        stringReader.close();
                    }
                    sPSListUtils = this;
                    str4 = str3;
                } catch (Exception e19) {
                    e = e19;
                    str3 = "updateSPSList ";
                    str2 = "updateSPSList finally ";
                    stringReader2 = null;
                    closeable = newPullParser;
                    r15 = stringReader2;
                    StringBuilder sb2222 = new StringBuilder();
                    str4 = str3;
                    sb2222.append(str4);
                    sb2222.append(e);
                    LocalLog.a("SPSListUtils", sb2222.toString());
                    if (!(closeable instanceof Closeable)) {
                    }
                    sPSListUtils = this;
                    sPSListUtils.f166z.lock();
                    sPSListUtils.f154n.clear();
                    sPSListUtils.f154n.addAll(arrayList);
                    sPSListUtils.f155o.clear();
                    sPSListUtils.f155o.addAll(arrayList6);
                    sPSListUtils.f156p.clear();
                    sPSListUtils.f156p.addAll(arrayList3);
                    sPSListUtils.f157q.clear();
                    sPSListUtils.f157q.addAll(arrayList4);
                    sPSListUtils.f158r.clear();
                    sPSListUtils.f158r.addAll(arrayList5);
                    sPSListUtils.f159s.clear();
                    sPSListUtils.f159s.addAll(arrayList6);
                    sPSListUtils.f160t.clear();
                    sPSListUtils.f160t.addAll(arrayList7);
                    k();
                    return;
                } catch (Throwable th8) {
                    th = th8;
                    str2 = "updateSPSList finally ";
                    fileInputStream2 = null;
                    closeable = newPullParser;
                    fileInputStream = fileInputStream2;
                    if (closeable instanceof Closeable) {
                    }
                }
                sPSListUtils.f166z.lock();
                sPSListUtils.f154n.clear();
                sPSListUtils.f154n.addAll(arrayList);
                sPSListUtils.f155o.clear();
                sPSListUtils.f155o.addAll(arrayList6);
                sPSListUtils.f156p.clear();
                sPSListUtils.f156p.addAll(arrayList3);
                sPSListUtils.f157q.clear();
                sPSListUtils.f157q.addAll(arrayList4);
                sPSListUtils.f158r.clear();
                sPSListUtils.f158r.addAll(arrayList5);
                sPSListUtils.f159s.clear();
                sPSListUtils.f159s.addAll(arrayList6);
                sPSListUtils.f160t.clear();
                sPSListUtils.f160t.addAll(arrayList7);
                k();
                return;
            }
        }
        if (file.exists()) {
            ?? fileInputStream3 = new FileInputStream(file);
            try {
                newPullParser.setInput(fileInputStream3, null);
                stringReader = null;
                r152 = fileInputStream3;
                while (true) {
                    next = newPullParser.next();
                    if (next != 2) {
                    }
                    obj = null;
                    if (next == 1) {
                    }
                    str7 = str5;
                    str6 = str3;
                }
                if (!(newPullParser instanceof Closeable)) {
                }
                sPSListUtils = this;
                str4 = str3;
            } catch (Exception e20) {
                e = e20;
                str3 = "updateSPSList ";
                str2 = "updateSPSList finally ";
                stringReader = null;
                stringReader2 = fileInputStream3;
                closeable = newPullParser;
                r15 = stringReader2;
                StringBuilder sb22222 = new StringBuilder();
                str4 = str3;
                sb22222.append(str4);
                sb22222.append(e);
                LocalLog.a("SPSListUtils", sb22222.toString());
                if (!(closeable instanceof Closeable)) {
                }
                sPSListUtils = this;
                sPSListUtils.f166z.lock();
                sPSListUtils.f154n.clear();
                sPSListUtils.f154n.addAll(arrayList);
                sPSListUtils.f155o.clear();
                sPSListUtils.f155o.addAll(arrayList6);
                sPSListUtils.f156p.clear();
                sPSListUtils.f156p.addAll(arrayList3);
                sPSListUtils.f157q.clear();
                sPSListUtils.f157q.addAll(arrayList4);
                sPSListUtils.f158r.clear();
                sPSListUtils.f158r.addAll(arrayList5);
                sPSListUtils.f159s.clear();
                sPSListUtils.f159s.addAll(arrayList6);
                sPSListUtils.f160t.clear();
                sPSListUtils.f160t.addAll(arrayList7);
                k();
                return;
            } catch (Throwable th9) {
                th = th9;
                str2 = "updateSPSList finally ";
                stringReader = null;
                r15 = fileInputStream3;
                closeable = newPullParser;
                th = th;
                fileInputStream = r15;
                if (closeable instanceof Closeable) {
                }
            }
            sPSListUtils.f166z.lock();
            sPSListUtils.f154n.clear();
            sPSListUtils.f154n.addAll(arrayList);
            sPSListUtils.f155o.clear();
            sPSListUtils.f155o.addAll(arrayList6);
            sPSListUtils.f156p.clear();
            sPSListUtils.f156p.addAll(arrayList3);
            sPSListUtils.f157q.clear();
            sPSListUtils.f157q.addAll(arrayList4);
            sPSListUtils.f158r.clear();
            sPSListUtils.f158r.addAll(arrayList5);
            sPSListUtils.f159s.clear();
            sPSListUtils.f159s.addAll(arrayList6);
            sPSListUtils.f160t.clear();
            sPSListUtils.f160t.addAll(arrayList7);
            k();
            return;
        }
        try {
            if (newPullParser instanceof Closeable) {
                ((Closeable) newPullParser).close();
            }
        } catch (IOException e21) {
            LocalLog.a("SPSListUtils", "updateSPSList finally " + e21);
        }
    }

    public void o() {
        this.f163w = d();
    }
}
