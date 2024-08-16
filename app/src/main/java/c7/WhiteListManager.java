package c7;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import b6.LocalLog;
import com.oplus.deepthinker.sdk.app.ClientConnection;
import com.oplus.deepthinker.sdk.app.OplusDeepThinkerManager;
import com.oplus.statistics.util.TimeInfoUtil;
import e7.SimpleAppTypeUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* compiled from: WhiteListManager.java */
/* renamed from: c7.c, reason: use source file name */
/* loaded from: classes.dex */
public class WhiteListManager {

    /* renamed from: b, reason: collision with root package name */
    private Context f4906b;

    /* renamed from: a, reason: collision with root package name */
    private boolean f4905a = false;

    /* renamed from: g, reason: collision with root package name */
    private Comparator<PackageInfo> f4911g = new Comparator() { // from class: c7.b
        @Override // java.util.Comparator
        public final int compare(Object obj, Object obj2) {
            int i10;
            i10 = WhiteListManager.i((PackageInfo) obj, (PackageInfo) obj2);
            return i10;
        }
    };

    /* renamed from: c, reason: collision with root package name */
    private Set<String> f4907c = new HashSet();

    /* renamed from: d, reason: collision with root package name */
    private Set<String> f4908d = new HashSet();

    /* renamed from: e, reason: collision with root package name */
    private ArrayList<String> f4909e = new ArrayList<>();

    /* renamed from: f, reason: collision with root package name */
    private Set<String> f4910f = new HashSet();

    public WhiteListManager(Context context) {
        this.f4906b = context;
    }

    private void b() {
        ArrayList<String> a10 = SimpleAppTypeUtil.a(this.f4906b);
        if (a10.size() > 3) {
            a10 = new ArrayList<>(a10.subList(0, 3));
        }
        j(a10);
        LocalLog.a("SmartDoze:WhiteListManager", "cal GlobalWhiteList :" + a10);
    }

    private boolean c(Handler handler, ClientConnection clientConnection) {
        List<String> f10 = f(handler, clientConnection);
        if (f10 == null) {
            return false;
        }
        k(f10);
        LocalLog.a("SmartDoze:WhiteListManager", "cal ImportWhiteList:" + f10);
        return true;
    }

    private boolean d() {
        String str;
        List<PackageInfo> list;
        PackageManager packageManager = this.f4906b.getPackageManager();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> queryIntentActivities = packageManager.queryIntentActivities(intent, 0);
        if (queryIntentActivities == null) {
            return false;
        }
        HashSet hashSet = new HashSet();
        for (int i10 = 0; i10 < queryIntentActivities.size(); i10++) {
            hashSet.add(queryIntentActivities.get(i10).activityInfo.packageName);
        }
        int i11 = 1;
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(1);
        if (installedPackages == null) {
            return false;
        }
        ArrayList<String> arrayList = new ArrayList<>();
        installedPackages.sort(this.f4911g);
        int size = installedPackages.size();
        long j10 = size > 0 ? installedPackages.get(0).firstInstallTime : 0L;
        int i12 = size - 1;
        while (true) {
            if (i12 < 0) {
                str = "SmartDoze:WhiteListManager";
                break;
            }
            PackageInfo packageInfo = installedPackages.get(i12);
            if ((packageInfo.applicationInfo.flags & i11) == 0) {
                String str2 = packageInfo.packageName;
                if (hashSet.contains(str2)) {
                    long currentTimeMillis = System.currentTimeMillis();
                    long j11 = (currentTimeMillis - packageInfo.firstInstallTime) / TimeInfoUtil.MILLISECOND_OF_A_DAY;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("pkg:");
                    sb2.append(str2);
                    sb2.append(",installed Day = ");
                    sb2.append(j11);
                    sb2.append("install time:");
                    list = installedPackages;
                    sb2.append(packageInfo.firstInstallTime);
                    sb2.append(",minInstalledTime = ");
                    sb2.append(j10);
                    LocalLog.a("SmartDoze:WhiteListManager", sb2.toString());
                    if (j11 >= 0) {
                        str = "SmartDoze:WhiteListManager";
                        if (j11 <= 3) {
                            long j12 = packageInfo.firstInstallTime;
                            if (j12 - j10 > 120000 && j12 < currentTimeMillis) {
                                arrayList.add(str2);
                            }
                        }
                    } else {
                        str = "SmartDoze:WhiteListManager";
                    }
                    if (arrayList.size() >= 3) {
                        break;
                    }
                    i12--;
                    installedPackages = list;
                    i11 = 1;
                }
            }
            list = installedPackages;
            i12--;
            installedPackages = list;
            i11 = 1;
        }
        l(arrayList);
        LocalLog.a(str, "cal StudyWhiteList:" + arrayList);
        return true;
    }

    private List<String> e(Map<String, Integer> map) {
        ArrayList arrayList = new ArrayList();
        for (String str : map.keySet()) {
            if (map.get(str).intValue() == 3) {
                arrayList.add(str);
                if (arrayList.size() >= 5) {
                    break;
                }
            }
        }
        return arrayList;
    }

    private List<String> f(Handler handler, ClientConnection clientConnection) {
        OplusDeepThinkerManager oplusDeepThinkerManager = new OplusDeepThinkerManager(this.f4906b);
        oplusDeepThinkerManager.setRemote(clientConnection.getDeepThinkerBridge());
        Map<String, Integer> allNotificationLabelResult = oplusDeepThinkerManager.getAllNotificationLabelResult();
        if (allNotificationLabelResult == null) {
            LocalLog.a("SmartDoze:WhiteListManager", "notificationLabelResults is null");
            return null;
        }
        return e(allNotificationLabelResult);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int i(PackageInfo packageInfo, PackageInfo packageInfo2) {
        if (packageInfo == null || packageInfo2 == null) {
            return 0;
        }
        return Long.compare(packageInfo.firstInstallTime, packageInfo2.firstInstallTime);
    }

    private void j(ArrayList<String> arrayList) {
        this.f4908d.clear();
        this.f4908d.addAll(arrayList);
    }

    private void k(List<String> list) {
        this.f4907c.clear();
        this.f4907c.addAll(list);
    }

    private void l(ArrayList<String> arrayList) {
        this.f4909e = arrayList;
    }

    public Set<String> g() {
        if (!this.f4905a) {
            return new HashSet();
        }
        return this.f4910f;
    }

    public boolean h(Handler handler, ClientConnection clientConnection) {
        LocalLog.a("SmartDoze:WhiteListManager", "running WhiteList training job ");
        try {
            if (!c(handler, clientConnection)) {
                LocalLog.a("SmartDoze:WhiteListManager", "calculateImportWhiteList failed");
                return false;
            }
            b();
            if (!d()) {
                LocalLog.a("SmartDoze:WhiteListManager", "calculateStudyWhiteList failed");
                return false;
            }
            this.f4910f.clear();
            this.f4910f.addAll(this.f4908d);
            this.f4910f.addAll(this.f4909e);
            this.f4910f.addAll(this.f4907c);
            this.f4905a = true;
            LocalLog.a("SmartDoze:WhiteListManager", "finish running WhiteList training job ");
            return true;
        } catch (Exception e10) {
            LocalLog.m("SmartDoze:WhiteListManager", "error in cal whitelist:", e10);
            return false;
        }
    }
}
