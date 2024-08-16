package n8;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.ContextThemeWrapper;
import android.view.Window;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import b6.LocalLog;
import c6.NotifyUtil;
import c8.WordQuery;
import com.coui.appcompat.theme.COUIThemeOverlay;
import com.oplus.battery.R;
import f6.CommonUtil;
import ia.AppInfoUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import o9.HighPowerShuffleUtil;
import o9.HighPowerSipper;
import r9.SimplePowerMonitorUtils;
import x1.COUIAlertDialogBuilder;
import x5.UploadDataUtil;
import x8.DatabaseHelper;
import y5.AppFeature;
import z5.GuardElfDataManager;
import z7.AppInfoWrapper;
import z7.AppWrapperComparator;

/* compiled from: PowerConsumptionOptimizationHelper.java */
/* renamed from: n8.e, reason: use source file name */
/* loaded from: classes2.dex */
public class PowerConsumptionOptimizationHelper {

    /* renamed from: p, reason: collision with root package name */
    private static volatile PowerConsumptionOptimizationHelper f15889p;

    /* renamed from: b, reason: collision with root package name */
    private Context f15891b;

    /* renamed from: c, reason: collision with root package name */
    private Context f15892c;

    /* renamed from: d, reason: collision with root package name */
    private UploadDataUtil f15893d;

    /* renamed from: f, reason: collision with root package name */
    private SharedPreferences f15895f;

    /* renamed from: g, reason: collision with root package name */
    private SharedPreferences.Editor f15896g;

    /* renamed from: k, reason: collision with root package name */
    private Handler f15900k;

    /* renamed from: l, reason: collision with root package name */
    private HandlerThread f15901l;

    /* renamed from: a, reason: collision with root package name */
    private boolean f15890a = false;

    /* renamed from: e, reason: collision with root package name */
    private AlertDialog f15894e = null;

    /* renamed from: h, reason: collision with root package name */
    private List<ApplicationInfo> f15897h = new ArrayList();

    /* renamed from: i, reason: collision with root package name */
    private List<c> f15898i = new ArrayList();

    /* renamed from: j, reason: collision with root package name */
    private List<String> f15899j = new ArrayList();

    /* renamed from: m, reason: collision with root package name */
    private final Object f15902m = new Object();

    /* renamed from: n, reason: collision with root package name */
    private final Object f15903n = new Object();

    /* renamed from: o, reason: collision with root package name */
    private boolean f15904o = false;

    /* compiled from: PowerConsumptionOptimizationHelper.java */
    /* renamed from: n8.e$a */
    /* loaded from: classes2.dex */
    class a implements DialogInterface.OnClickListener {
        a() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
        }
    }

    /* compiled from: PowerConsumptionOptimizationHelper.java */
    /* renamed from: n8.e$b */
    /* loaded from: classes2.dex */
    class b implements DialogInterface.OnClickListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ HashMap f15906e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ String f15907f;

        b(HashMap hashMap, String str) {
            this.f15906e = hashMap;
            this.f15907f = str;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            this.f15906e.remove(this.f15907f);
            this.f15906e.put(this.f15907f, 0);
            f6.f.C3(this.f15906e, PowerConsumptionOptimizationHelper.this.f15891b);
            Toast.makeText(PowerConsumptionOptimizationHelper.this.f15891b.getApplicationContext(), PowerConsumptionOptimizationHelper.this.f15891b.getResources().getString(R.string.battery_ui_optimization_pco_toast_new), 0).show();
        }
    }

    /* compiled from: PowerConsumptionOptimizationHelper.java */
    /* renamed from: n8.e$c */
    /* loaded from: classes2.dex */
    public static class c extends AppInfoWrapper {
    }

    public PowerConsumptionOptimizationHelper(Context context) {
        this.f15891b = null;
        this.f15892c = null;
        this.f15893d = null;
        this.f15891b = context;
        this.f15892c = context.createDeviceProtectedStorageContext();
        this.f15893d = UploadDataUtil.S0(this.f15891b);
        HandlerThread handlerThread = new HandlerThread("PowerConsumptionOptimizationHelper");
        this.f15901l = handlerThread;
        handlerThread.start();
        this.f15900k = new Handler(this.f15901l.getLooper());
    }

    private List<c> g(boolean z10) {
        ArrayList<ApplicationInfo> arrayList;
        ArrayList arrayList2 = new ArrayList();
        List<String> a10 = AppFeature.a();
        a8.a e10 = a8.a.e();
        synchronized (this.f15903n) {
            arrayList = new ArrayList(this.f15897h);
        }
        ArrayMap<String, String> arrayMap = z10 ? new ArrayMap<>() : DatabaseHelper.u(this.f15891b).c();
        for (ApplicationInfo applicationInfo : arrayList) {
            c cVar = new c();
            String str = arrayMap.get(applicationInfo.packageName);
            if (str == null) {
                CharSequence loadLabel = applicationInfo.loadLabel(this.f15891b.getPackageManager());
                if (loadLabel != null) {
                    str = f6.f.i(loadLabel.toString().trim());
                }
                if (TextUtils.isEmpty(str)) {
                    str = applicationInfo.packageName;
                }
            }
            cVar.f20247b = str;
            cVar.f20246a = applicationInfo.packageName;
            cVar.f20248c = applicationInfo;
            int b10 = e10.b(str);
            cVar.f20250e = b10;
            String c10 = e10.c(b10);
            if (AppFeature.D() && Locale.getDefault().equals(Locale.TAIWAN) && TextUtils.equals(c10, "…")) {
                c10 = WordQuery.c(str);
            }
            char[] charArray = c10.toCharArray();
            if (charArray != null && charArray.length > 0) {
                cVar.f20249d = charArray[0];
            } else {
                cVar.f20249d = '#';
            }
            if (a10 != null && a10.contains(applicationInfo.packageName)) {
                LocalLog.l("PowerConsumptionOptimizationHelper", "is customized. pkg=" + applicationInfo.packageName);
            } else if ((applicationInfo.flags & 1) == 0) {
                arrayList2.add(cVar);
                this.f15899j.add(cVar.f20246a);
            }
        }
        v(arrayList2, this.f15899j);
        LocalLog.a("PowerConsumptionOptimizationHelper", "create app list");
        return arrayList2;
    }

    private boolean h() {
        File file = new File("/data/oplus/os/battery/kill_frequent_third_app.xml");
        if (!file.exists()) {
            LocalLog.b("PowerConsumptionOptimizationHelper", "file is not exist:/data/oplus/os/battery/kill_frequent_third_app.xml");
            return false;
        }
        if (file.isFile()) {
            return file.delete();
        }
        return false;
    }

    public static PowerConsumptionOptimizationHelper k(Context context) {
        if (f15889p == null) {
            synchronized (PowerConsumptionOptimizationHelper.class) {
                if (f15889p == null) {
                    f15889p = new PowerConsumptionOptimizationHelper(context);
                }
            }
        }
        return f15889p;
    }

    private String l(ArrayMap<String, Integer> arrayMap) {
        String str = null;
        int i10 = 0;
        for (int i11 = 0; i11 < arrayMap.size(); i11++) {
            Integer valueAt = arrayMap.valueAt(i11);
            if (valueAt != null && i10 < valueAt.intValue()) {
                i10 = valueAt.intValue();
                str = arrayMap.keyAt(i11);
            }
        }
        return str;
    }

    private Context m(Context context) {
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, R.style.Theme_Demo);
        COUIThemeOverlay.i().b(contextThemeWrapper);
        return contextThemeWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void o(Intent intent) {
        this.f15891b.sendBroadcastAsUser(intent, UserHandle.ALL, "oplus.permission.OPLUS_COMPONENT_SAFE");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void p(Intent intent) {
        this.f15891b.sendBroadcastAsUser(intent, UserHandle.ALL, "oplus.permission.OPLUS_COMPONENT_SAFE");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void q(Intent intent) {
        this.f15891b.sendBroadcastAsUser(intent, UserHandle.ALL, "oplus.permission.OPLUS_COMPONENT_SAFE");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void r(Intent intent) {
        this.f15891b.sendBroadcastAsUser(intent, UserHandle.ALL, "oplus.permission.OPLUS_COMPONENT_SAFE");
    }

    private void v(List<c> list, List<String> list2) {
        if (list == null) {
            return;
        }
        try {
            System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
            Collections.sort(list, new AppWrapperComparator(this.f15891b, list2, list2));
        } catch (IllegalArgumentException e10) {
            LocalLog.l("PowerConsumptionOptimizationHelper", "Collections.sort IllegalArgumentException: " + e10);
        }
    }

    public void f(ApplicationInfo applicationInfo) {
        boolean z10;
        synchronized (this.f15903n) {
            Iterator<ApplicationInfo> it = this.f15897h.iterator();
            while (true) {
                if (!it.hasNext()) {
                    z10 = true;
                    break;
                }
                ApplicationInfo next = it.next();
                if (next.packageName.equals(applicationInfo.packageName)) {
                    this.f15897h.remove(next);
                    z10 = false;
                    break;
                }
            }
            this.f15897h.add(applicationInfo);
        }
        synchronized (this.f15902m) {
            c cVar = new c();
            CharSequence loadLabel = applicationInfo.loadLabel(this.f15891b.getPackageManager());
            a8.a e10 = a8.a.e();
            List<String> a10 = AppFeature.a();
            if (a10 != null && a10.contains(applicationInfo.packageName)) {
                LocalLog.l("PowerConsumptionOptimizationHelper", "is customized. pkg=" + applicationInfo.packageName);
                this.f15904o = true;
                this.f15902m.notify();
                return;
            }
            String i10 = loadLabel != null ? f6.f.i(loadLabel.toString().trim()) : null;
            if (TextUtils.isEmpty(i10)) {
                i10 = applicationInfo.packageName;
            }
            cVar.f20247b = i10;
            cVar.f20246a = applicationInfo.packageName;
            cVar.f20248c = applicationInfo;
            int b10 = e10.b(i10);
            cVar.f20250e = b10;
            String c10 = e10.c(b10);
            if (AppFeature.D() && Locale.getDefault().equals(Locale.TAIWAN) && TextUtils.equals(c10, "…")) {
                c10 = WordQuery.c(i10);
            }
            char[] charArray = c10.toCharArray();
            if (charArray != null && charArray.length > 0) {
                cVar.f20249d = charArray[0];
            } else {
                cVar.f20249d = '#';
            }
            if ((applicationInfo.flags & 1) == 0) {
                if (z10) {
                    this.f15898i.add(cVar);
                    this.f15899j.add(cVar.f20246a);
                } else {
                    Iterator<c> it2 = this.f15898i.iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            break;
                        }
                        c next2 = it2.next();
                        if (next2.f20246a.equals(cVar.f20246a)) {
                            this.f15898i.remove(next2);
                            this.f15898i.add(cVar);
                            break;
                        }
                    }
                }
                v(this.f15898i, this.f15899j);
                LocalLog.a("PowerConsumptionOptimizationHelper", "add new app " + applicationInfo.packageName);
            }
            this.f15904o = true;
            this.f15902m.notify();
        }
    }

    public List<c> i() {
        List<c> list;
        if (LocalLog.f()) {
            LocalLog.a("PowerConsumptionOptimizationHelper", "getAppWrapper start ");
        }
        synchronized (this.f15902m) {
            while (!this.f15904o) {
                try {
                    this.f15902m.wait();
                    if (LocalLog.f()) {
                        LocalLog.a("PowerConsumptionOptimizationHelper", "getAppWrapper complete");
                    }
                } catch (InterruptedException e10) {
                    LocalLog.b("PowerConsumptionOptimizationHelper", "getAppWrapper " + e10.toString());
                }
            }
            if (LocalLog.f()) {
                LocalLog.a("PowerConsumptionOptimizationHelper", "getAppWrapper size " + this.f15898i.size());
            }
            list = this.f15898i;
        }
        return list;
    }

    public boolean j(Context context) {
        return Settings.System.getInt(context.getContentResolver(), "pco_flag", 0) == 1;
    }

    public void n(boolean z10) {
        synchronized (this.f15903n) {
            this.f15897h = this.f15891b.getPackageManager().getInstalledApplications(128);
        }
        synchronized (this.f15902m) {
            this.f15898i = g(z10);
            this.f15904o = true;
            this.f15902m.notify();
        }
        f6.f.B1(this.f15891b);
    }

    public void s(String str) {
        synchronized (this.f15903n) {
            Iterator<ApplicationInfo> it = this.f15897h.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                ApplicationInfo next = it.next();
                if (next.packageName.equals(str)) {
                    this.f15897h.remove(next);
                    break;
                }
            }
        }
        synchronized (this.f15902m) {
            Iterator<c> it2 = this.f15898i.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                c next2 = it2.next();
                if (next2.f20246a.equals(str)) {
                    this.f15898i.remove(next2);
                    LocalLog.a("PowerConsumptionOptimizationHelper", "remove app " + str);
                    break;
                }
            }
        }
    }

    public void t(boolean z10, Context context) {
        Settings.System.putInt(context.getContentResolver(), "pco_flag", z10 ? 1 : 0);
    }

    public void u(boolean z10) {
        this.f15890a = z10;
    }

    public void w(String str) {
        HashMap<String, Integer> B1 = f6.f.B1(this.f15891b);
        COUIAlertDialogBuilder cOUIAlertDialogBuilder = new COUIAlertDialogBuilder(m(this.f15891b), f6.f.k(this.f15891b));
        cOUIAlertDialogBuilder.h0(R.string.pco_notification_dialog_title).Y(R.string.pco_notification_dialog_text).e0(R.string.pco_notification_dialog_open, new b(B1, str)).a0(R.string.pco_notification_dialog_not_open, new a());
        AlertDialog a10 = cOUIAlertDialogBuilder.a();
        this.f15894e = a10;
        a10.setCancelable(false);
        Window window = this.f15894e.getWindow();
        window.setType(2003);
        try {
            f6.f.n2(window.getAttributes(), 1);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        this.f15894e.show();
    }

    public void x() {
        SharedPreferences sharedPreferences = this.f15892c.getSharedPreferences("PowerConsumptionOptimizationActivity", 0);
        this.f15895f = sharedPreferences;
        this.f15896g = sharedPreferences.edit();
        HashMap<String, Integer> B1 = f6.f.B1(this.f15891b);
        ArrayList<AppInfoUtils.a> arrayList = new ArrayList(AppInfoUtils.d().c());
        HashMap<String, Integer> x12 = f6.f.x1(this.f15891b);
        ArrayMap<String, Integer> A1 = f6.f.A1(this.f15891b);
        long j10 = this.f15895f.getLong("time", 0L);
        long currentTimeMillis = System.currentTimeMillis();
        NotifyUtil v7 = NotifyUtil.v(this.f15891b);
        for (AppInfoUtils.a aVar : arrayList) {
            if ((1 & aVar.f12690b) == 0) {
                if (x12.size() <= 0) {
                    x12.put(aVar.f12689a, 0);
                } else if (!x12.containsKey(aVar.f12689a)) {
                    x12.put(aVar.f12689a, 0);
                }
            }
        }
        int i10 = 0;
        while (i10 < A1.keySet().size()) {
            String l10 = l(A1);
            if (LocalLog.f()) {
                LocalLog.a("PowerConsumptionOptimizationHelper", "pkg = " + l10 + " value = " + A1.get(l10));
            }
            boolean z10 = ((B1.containsKey(l10) || !j(this.f15891b) || GuardElfDataManager.d(this.f15891b).e("notify_whitelist.xml").contains(l10)) && (B1.get(l10) == null || B1.get(l10).intValue() != 2 || GuardElfDataManager.d(this.f15891b).e("notify_whitelist.xml").contains(l10))) ? false : true;
            ArrayList arrayList2 = new ArrayList();
            HashMap<String, Integer> hashMap = B1;
            f6.f.l(arrayList2, new ArrayList(), this.f15891b);
            if (LocalLog.f()) {
                LocalLog.a("PowerConsumptionOptimizationHelper", "flag " + z10 + ", listAllow.contains(pkg) " + arrayList2.contains(l10) + ", time0 " + j10 + ", time1 " + currentTimeMillis);
            }
            if (z10 && !arrayList2.contains(l10)) {
                if (this.f15890a || currentTimeMillis - j10 > 259200000) {
                    if (x12.containsKey(l10) && x12.get(l10).intValue() != 0) {
                        v7.G(l10, false);
                    } else {
                        v7.G(l10, true);
                        x12.remove(l10);
                        x12.put(l10, 1);
                        f6.f.A3(x12, this.f15891b);
                    }
                    this.f15896g.putLong("time", currentTimeMillis);
                    this.f15896g.apply();
                    this.f15890a = false;
                    boolean h10 = h();
                    if (LocalLog.f()) {
                        LocalLog.a("PowerConsumptionOptimizationHelper", "is succeed = " + h10);
                        return;
                    }
                    return;
                }
                return;
            }
            A1.remove(l10);
            i10++;
            B1 = hashMap;
        }
    }

    public void y(List<String> list, List<HighPowerSipper> list2) {
        int i10;
        String str;
        HashMap<String, Integer> hashMap;
        boolean z10;
        int i11;
        int i12;
        long j10;
        int i13;
        List<String> list3 = list;
        int i14 = 0;
        SharedPreferences sharedPreferences = this.f15892c.getSharedPreferences("HighPowerConsumptionNotify", 0);
        this.f15895f = sharedPreferences;
        this.f15896g = sharedPreferences.edit();
        HashMap<String, Integer> B1 = f6.f.B1(this.f15891b);
        List<ApplicationInfo> installedApplications = this.f15891b.getPackageManager().getInstalledApplications(128);
        HashMap<String, Integer> x12 = f6.f.x1(this.f15891b);
        String str2 = "time";
        long j11 = this.f15895f.getLong("time", 0L);
        long currentTimeMillis = System.currentTimeMillis();
        NotifyUtil v7 = NotifyUtil.v(this.f15891b);
        for (ApplicationInfo applicationInfo : installedApplications) {
            if ((1 & applicationInfo.flags) == 0) {
                if (x12.size() <= 0) {
                    x12.put(applicationInfo.packageName, 0);
                } else if (!x12.containsKey(applicationInfo.packageName)) {
                    x12.put(applicationInfo.packageName, 0);
                }
            }
        }
        ka.a a10 = HighPowerShuffleUtil.a(this.f15891b, list3);
        int i15 = 0;
        while (i15 < list.size()) {
            String str3 = list3.get(i15);
            HighPowerSipper highPowerSipper = list2.get(i15);
            String substring = SimplePowerMonitorUtils.f17652c != 0 ? str3.substring(i14, str3.lastIndexOf(".")) : str3;
            if (LocalLog.f()) {
                StringBuilder sb2 = new StringBuilder();
                i10 = i15;
                sb2.append("realPkg: ");
                sb2.append(substring);
                LocalLog.a("PowerConsumptionOptimizationHelper", sb2.toString());
            } else {
                i10 = i15;
            }
            boolean z11 = (!B1.containsKey(substring) && j(this.f15891b)) || (B1.containsKey(substring) && 2 == B1.get(substring).intValue());
            ArrayList arrayList = new ArrayList();
            String str4 = str2;
            HashMap<String, Integer> hashMap2 = B1;
            f6.f.l(arrayList, new ArrayList(), this.f15891b);
            if (LocalLog.f()) {
                LocalLog.a("PowerConsumptionOptimizationHelper", "flag " + z11 + ", listAllow.contains(pkg) " + arrayList.contains(str3) + ", time0 " + j11 + ", time1 " + currentTimeMillis);
            }
            if (SimplePowerMonitorUtils.j().contains(str3)) {
                str = str4;
                hashMap = hashMap2;
                z10 = true;
                i11 = 0;
            } else {
                if (z11 && !arrayList.contains(str3)) {
                    if (this.f15890a || currentTimeMillis - j11 > SimplePowerMonitorUtils.M) {
                        if (LocalLog.f()) {
                            LocalLog.a("PowerConsumptionOptimizationHelper", "SimplePowerMonitorUtils.NOTIFICATON_CYCLE_BG_OFF: " + SimplePowerMonitorUtils.M);
                        }
                        if (x12.containsKey(str3) && x12.get(str3).intValue() != 0) {
                            if (SimplePowerMonitorUtils.f17652c != 0) {
                                final Intent intent = new Intent();
                                intent.setAction("oplus.intent.action.OPLUS_SUB_USER_NOTIFY");
                                intent.putExtra("pkgname", substring);
                                intent.putExtra("type", 1);
                                LocalLog.a("PowerConsumptionOptimizationHelper", "mContext.sendBroadcastAsUser1");
                                this.f15900k.post(new Runnable() { // from class: n8.a
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        PowerConsumptionOptimizationHelper.this.p(intent);
                                    }
                                });
                            } else {
                                v7.E(substring, false);
                            }
                            ka.b a11 = a10.a(str3);
                            if (a11 != null) {
                                str3 = a11.f14229a;
                            }
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append(SimplePowerMonitorUtils.z(str3 + SimplePowerMonitorUtils.z(String.valueOf(Build.VERSION.RELEASE))));
                            sb3.append(" DrainReason: ");
                            sb3.append((Object) highPowerSipper.f16310b);
                            sb3.append(" is");
                            sb3.append((Object) highPowerSipper.f16310b);
                            sb3.append("SceneFilter: false isAllowBgRunning: false isFirstlyNotify: false PowerConsumOptXml:");
                            sb3.append(hashMap2.containsKey(substring) ? hashMap2.get(substring) : "null");
                            this.f15893d.M(sb3.toString());
                            j10 = currentTimeMillis;
                        } else {
                            j10 = currentTimeMillis;
                            if (LocalLog.f()) {
                                LocalLog.a("PowerConsumptionOptimizationHelper", "appFirst.containsKey(pkg): " + x12.containsKey(str3));
                                LocalLog.a("PowerConsumptionOptimizationHelper", "appFirst.get(pkg): " + x12.get(str3));
                                LocalLog.a("PowerConsumptionOptimizationHelper", "appFirst.pkg: " + str3);
                            }
                            if (SimplePowerMonitorUtils.f17652c != 0) {
                                final Intent intent2 = new Intent();
                                intent2.setAction("oplus.intent.action.OPLUS_SUB_USER_NOTIFY");
                                intent2.putExtra("pkgname", substring);
                                intent2.putExtra("type", 0);
                                LocalLog.a("PowerConsumptionOptimizationHelper", "mContext.sendBroadcastAsUser0");
                                this.f15900k.post(new Runnable() { // from class: n8.b
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        PowerConsumptionOptimizationHelper.this.o(intent2);
                                    }
                                });
                                i13 = 1;
                            } else {
                                i13 = 1;
                                v7.E(substring, true);
                            }
                            LocalLog.a("PowerConsumptionOptimizationHelper", "sendBroadcastAsUser ");
                            x12.remove(str3);
                            x12.put(str3, Integer.valueOf(i13));
                            if (LocalLog.f()) {
                                LocalLog.a("PowerConsumptionOptimizationHelper", "AfterappFirst.containsKey(pkg): " + x12.containsKey(str3));
                                LocalLog.a("PowerConsumptionOptimizationHelper", "AfterappFirst.get(pkg): " + x12.get(str3));
                            }
                            f6.f.A3(x12, this.f15891b);
                            ka.b a12 = a10.a(str3);
                            if (a12 != null) {
                                str3 = a12.f14229a;
                            }
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append(SimplePowerMonitorUtils.z(str3 + SimplePowerMonitorUtils.z(String.valueOf(Build.VERSION.RELEASE))));
                            sb4.append(" DrainReason: ");
                            sb4.append((Object) highPowerSipper.f16310b);
                            sb4.append(" is");
                            sb4.append((Object) highPowerSipper.f16310b);
                            sb4.append("SceneFilter: false isAllowBgRunning: false isFirstlyNotify: true PowerConsumOptXml:");
                            sb4.append(hashMap2.containsKey(substring) ? hashMap2.get(substring) : "null");
                            this.f15893d.M(sb4.toString());
                        }
                        this.f15896g.putLong(str4, j10);
                        this.f15896g.apply();
                        this.f15890a = false;
                        boolean h10 = h();
                        if (LocalLog.f()) {
                            LocalLog.a("PowerConsumptionOptimizationHelper", "is succeed = " + h10);
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (arrayList.contains(str3)) {
                    if (this.f15890a || currentTimeMillis - j11 > SimplePowerMonitorUtils.L) {
                        if (x12.containsKey(str3) && x12.get(str3).intValue() != 0) {
                            if (SimplePowerMonitorUtils.f17652c != 0) {
                                final Intent intent3 = new Intent();
                                intent3.setAction("oplus.intent.action.OPLUS_SUB_USER_NOTIFY");
                                intent3.putExtra("pkgname", substring);
                                intent3.putExtra("type", 3);
                                LocalLog.a("PowerConsumptionOptimizationHelper", "mContext.sendBroadcastAsUser3");
                                this.f15900k.post(new Runnable() { // from class: n8.c
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        PowerConsumptionOptimizationHelper.this.r(intent3);
                                    }
                                });
                            } else {
                                v7.D(substring, false);
                            }
                            ka.b a13 = a10.a(str3);
                            if (a13 != null) {
                                str3 = a13.f14229a;
                            }
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append(SimplePowerMonitorUtils.z(str3 + SimplePowerMonitorUtils.z(String.valueOf(Build.VERSION.RELEASE))));
                            sb5.append(" DrainReason: ");
                            sb5.append((Object) highPowerSipper.f16310b);
                            sb5.append(" is");
                            sb5.append((Object) highPowerSipper.f16310b);
                            sb5.append("SceneFilter: false isAllowBgRunning: true isFirstlyNotify: false PowerConsumOptXml:");
                            sb5.append(hashMap2.containsKey(substring) ? hashMap2.get(substring) : "null");
                            this.f15893d.M(sb5.toString());
                        } else {
                            if (SimplePowerMonitorUtils.f17652c != 0) {
                                final Intent intent4 = new Intent();
                                intent4.setAction("oplus.intent.action.OPLUS_SUB_USER_NOTIFY");
                                intent4.putExtra("pkgname", substring);
                                intent4.putExtra("type", 2);
                                LocalLog.a("PowerConsumptionOptimizationHelper", "mContext.sendBroadcastAsUser2");
                                this.f15900k.post(new Runnable() { // from class: n8.d
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        PowerConsumptionOptimizationHelper.this.q(intent4);
                                    }
                                });
                                i12 = 1;
                            } else {
                                i12 = 1;
                                v7.D(substring, true);
                            }
                            x12.remove(str3);
                            x12.put(str3, Integer.valueOf(i12));
                            f6.f.A3(x12, this.f15891b);
                            ka.b a14 = a10.a(str3);
                            if (a14 != null) {
                                str3 = a14.f14229a;
                            }
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append(SimplePowerMonitorUtils.z(str3 + SimplePowerMonitorUtils.z(String.valueOf(Build.VERSION.RELEASE))));
                            sb6.append(" DrainReason: ");
                            sb6.append((Object) highPowerSipper.f16310b);
                            sb6.append(" is");
                            sb6.append((Object) highPowerSipper.f16310b);
                            sb6.append("SceneFilter: false isAllowBgRunning: true isFirstlyNotify: true PowerConsumOptXml:");
                            sb6.append(hashMap2.containsKey(substring) ? hashMap2.get(substring) : "null");
                            this.f15893d.M(sb6.toString());
                        }
                        this.f15896g.putLong(str4, currentTimeMillis);
                        this.f15896g.apply();
                        this.f15890a = false;
                        boolean h11 = h();
                        if (LocalLog.f()) {
                            LocalLog.a("PowerConsumptionOptimizationHelper", "is succeed = " + h11);
                            return;
                        }
                        return;
                    }
                    return;
                }
                hashMap = hashMap2;
                str = str4;
                i11 = 0;
                if (SimplePowerMonitorUtils.f17660k) {
                    if (hashMap.get(substring) != null && hashMap.get(substring).intValue() == 0) {
                        CommonUtil.Z(this.f15891b, str3, "highpower");
                    }
                    if (hashMap.get(substring) != null) {
                        z10 = true;
                        if (hashMap.get(substring).intValue() == 1 && AppFeature.D()) {
                            CommonUtil.Z(this.f15891b, str3, "highpower");
                        }
                    }
                }
                z10 = true;
            }
            str2 = str;
            i14 = i11;
            B1 = hashMap;
            i15 = i10 + 1;
            list3 = list;
        }
    }
}
