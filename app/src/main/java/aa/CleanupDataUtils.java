package aa;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.OplusPackageManager;
import android.os.Build;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.ArrayMap;
import b6.LocalLog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* compiled from: CleanupDataUtils.java */
/* renamed from: aa.a, reason: use source file name */
/* loaded from: classes2.dex */
public class CleanupDataUtils {

    /* renamed from: c, reason: collision with root package name */
    private static volatile CleanupDataUtils f117c;

    /* renamed from: a, reason: collision with root package name */
    private Map<String, String> f118a = new ArrayMap();

    /* renamed from: b, reason: collision with root package name */
    private Context f119b;

    private CleanupDataUtils(Context context) {
        this.f119b = context;
    }

    private boolean e() {
        SharedPreferences.Editor edit = this.f119b.getSharedPreferences("datacleaned", 0).edit();
        edit.putBoolean("iscleaned", true);
        edit.apply();
        return false;
    }

    public static CleanupDataUtils f(Context context) {
        if (f117c == null) {
            synchronized (CleanupDataUtils.class) {
                if (f117c == null) {
                    f117c = new CleanupDataUtils(context);
                }
            }
        }
        return f117c;
    }

    private String g(String str) {
        String str2 = this.f118a.get(str);
        if (str2 == null) {
            try {
                Class<?> cls = Class.forName("android.content.pm.OplusPackageManager");
                str2 = (String) cls.getMethod("getMigMappingPkgName", Boolean.TYPE, String.class).invoke(cls.newInstance(), Boolean.FALSE, str);
            } catch (Exception e10) {
                LocalLog.a("StartupManagerCleanupDataUtils", "getNewPkgName failed! " + e10.toString());
            }
            this.f118a.put(str, str2 == null ? "null" : str2);
        }
        if (str2 == "null") {
            return null;
        }
        return str2;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0045  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0048  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean a() {
        boolean z10;
        boolean z11;
        OplusPackageManager oplusPackageManager;
        if (this.f119b.getPackageManager().isDeviceUpgrading() && Build.VERSION.CODENAME.equals("S")) {
            try {
                oplusPackageManager = OplusPackageManager.getOplusPackageManager(this.f119b);
            } catch (RemoteException e10) {
                e10.printStackTrace();
            }
            if (oplusPackageManager != null && oplusPackageManager.isCrossVersionUpdate()) {
                z10 = !this.f119b.getSharedPreferences("datacleaned", 0).getBoolean("iscleaned", false);
                z11 = SystemProperties.getBoolean("persist.sys.startup.cleanupdata", false) ? true : z10;
                if (!z11) {
                    LocalLog.a("StartupManagerCleanupDataUtils", "no need to clean up data.");
                }
                return z11;
            }
        }
        z10 = false;
        if (SystemProperties.getBoolean("persist.sys.startup.cleanupdata", false)) {
        }
        if (!z11) {
        }
        return z11;
    }

    public void b() {
        c();
        e();
        d();
    }

    public void c() {
        StartupDataUtils h10 = StartupDataUtils.h(this.f119b);
        Map<String, Map<String, List<String>>> w10 = h10.w();
        Map<String, List<String>> map = w10.get("1");
        Map<String, List<String>> map2 = w10.get("0");
        List<String> list = map.get("switch");
        List<String> list2 = map.get("userCare");
        List<String> list3 = map.get("pkgName");
        List<String> list4 = map2.get("switch");
        List<String> list5 = map2.get("userCare");
        List<String> list6 = map2.get("pkgName");
        ArrayList arrayList = new ArrayList();
        ArrayList<String> arrayList2 = new ArrayList();
        arrayList.addAll(list3);
        arrayList2.addAll(list6);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            Iterator it2 = it;
            String g6 = g(str);
            StartupDataUtils startupDataUtils = h10;
            if (g6 != null) {
                LocalLog.a("StartupManagerCleanupDataUtils", "data clean from " + str + " to " + g6 + " auto start");
                Collections.replaceAll(list, str, g6);
                Collections.replaceAll(list2, str, g6);
                Collections.replaceAll(list3, str, g6);
            }
            it = it2;
            h10 = startupDataUtils;
        }
        StartupDataUtils startupDataUtils2 = h10;
        for (String str2 : arrayList2) {
            String g10 = g(str2);
            if (g10 != null) {
                LocalLog.a("StartupManagerCleanupDataUtils", "data clean from " + str2 + " to " + g10 + " associate start");
                Collections.replaceAll(list4, str2, g10);
                Collections.replaceAll(list5, str2, g10);
                Collections.replaceAll(list6, str2, g10);
            }
        }
        startupDataUtils2.C(map, map2, true, false);
    }

    public void d() {
        if (this.f118a.isEmpty()) {
            return;
        }
        this.f118a.clear();
    }
}
