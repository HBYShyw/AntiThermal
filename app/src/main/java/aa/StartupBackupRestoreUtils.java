package aa;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import x9.WhiteListUtils;

/* compiled from: StartupBackupRestoreUtils.java */
/* renamed from: aa.f, reason: use source file name */
/* loaded from: classes2.dex */
public class StartupBackupRestoreUtils {

    /* renamed from: b, reason: collision with root package name */
    private static volatile StartupBackupRestoreUtils f169b;

    /* renamed from: a, reason: collision with root package name */
    private Context f170a;

    private StartupBackupRestoreUtils(Context context) {
        this.f170a = context;
    }

    public static StartupBackupRestoreUtils a(Context context) {
        if (f169b == null) {
            synchronized (StartupBackupRestoreUtils.class) {
                if (f169b == null) {
                    f169b = new StartupBackupRestoreUtils(context);
                }
            }
        }
        return f169b;
    }

    private void h(Map<String, List<String>> map, List<String> list) {
        List<String> b10 = b("/startup/ass_allow.txt");
        List<String> b11 = b("/startup/ass_check.txt");
        List<String> b12 = b("/startup/ass_fixed.txt");
        List<String> list2 = map.get("switch");
        List<String> list3 = map.get("userCare");
        List<String> list4 = map.get("pkgName");
        List<String> f10 = StartupDataUtils.h(this.f170a).f(false);
        g(b10, list2, f10, list);
        d(b11, f10, list2, list);
        f(b12, list3, list);
        list4.clear();
        list4.addAll(list2);
        list4.addAll(list3);
        StartupDataUtils.h(this.f170a).B(list4);
        map.put("switch", list2);
        map.put("userCare", list3);
        map.put("pkgName", list4);
    }

    private void i(Map<String, List<String>> map, List<String> list) {
        List<String> b10 = b("/startup/auto_allow.txt");
        List<String> b11 = b("/startup/auto_fixed.txt");
        e(b10, list);
        List<String> list2 = map.get("pkgName");
        List<String> list3 = map.get("switch");
        List<String> k10 = StartupDataUtils.h(this.f170a).k(true);
        List<String> b12 = WhiteListUtils.b(this.f170a);
        for (String str : list3) {
            if (!b11.contains(str) && k10.contains(str) && !b10.contains(str)) {
                b10.add(str);
            }
        }
        b10.addAll(b12);
        list2.clear();
        list2.addAll(b10);
        list2.addAll(b11);
        list2.addAll(b12);
        StartupDataUtils.h(this.f170a).B(list2);
        map.put("switch", b10);
        map.put("userCare", b11);
        map.put("pkgName", list2);
    }

    protected List<String> b(String str) {
        return StartupDataUtils.h(this.f170a).y(str);
    }

    public Bundle c() {
        Bundle bundle = new Bundle();
        Map<String, Map<String, List<String>>> w10 = StartupDataUtils.h(this.f170a).w();
        Map<String, List<String>> map = w10.get("1");
        Map<String, List<String>> map2 = w10.get("0");
        ArrayList<String> arrayList = new ArrayList<>(map.get("switch"));
        ArrayList<String> arrayList2 = new ArrayList<>(map.get("userCare"));
        ArrayList<String> arrayList3 = new ArrayList<>(StartupDataUtils.h(this.f170a).f(true));
        bundle.putStringArrayList("backup_auto_start_allow", arrayList);
        bundle.putStringArrayList("backup_auto_start_user_fixed", arrayList2);
        bundle.putStringArrayList("backup_auto_start_deny", arrayList3);
        ArrayList<String> arrayList4 = new ArrayList<>(map2.get("switch"));
        ArrayList<String> arrayList5 = new ArrayList<>(map2.get("userCare"));
        ArrayList<String> arrayList6 = new ArrayList<>(StartupDataUtils.h(this.f170a).f(false));
        bundle.putStringArrayList("backup_associate_start_allow", arrayList4);
        bundle.putStringArrayList("backup_associate_start_user_fixed", arrayList5);
        bundle.putStringArrayList("backup_associate_start_deny", arrayList6);
        return bundle;
    }

    protected void d(List<String> list, List<String> list2, List<String> list3, List<String> list4) {
        if (list == null || list2 == null) {
            return;
        }
        for (String str : list) {
            if (!TextUtils.isEmpty(str) && list4.contains(str) && list3.contains(str) && !list2.contains(str)) {
                list2.add(str);
                list3.remove(str);
            }
        }
    }

    protected void e(List<String> list, List<String> list2) {
        ArrayList arrayList = new ArrayList();
        if (list != null) {
            for (String str : list) {
                if (!TextUtils.isEmpty(str) && !list2.contains(str)) {
                    arrayList.add(str);
                }
            }
            list.removeAll(arrayList);
        }
    }

    protected void f(List<String> list, List<String> list2, List<String> list3) {
        if (list == null || list2 == null) {
            return;
        }
        for (String str : list) {
            if (!TextUtils.isEmpty(str) && list3.contains(str) && !list2.contains(str)) {
                list2.add(str);
            }
        }
    }

    protected void g(List<String> list, List<String> list2, List<String> list3, List<String> list4) {
        for (String str : list) {
            if (!TextUtils.isEmpty(str) && list4.contains(str) && !list2.contains(str) && list3.contains(str)) {
                list2.add(str);
                list3.remove(str);
            }
        }
    }

    public void j() {
        List<PackageInfo> installedPackages = this.f170a.getPackageManager().getInstalledPackages(128);
        ArrayList arrayList = new ArrayList();
        Iterator<PackageInfo> it = installedPackages.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().packageName.trim());
        }
        Map<String, Map<String, List<String>>> w10 = StartupDataUtils.h(this.f170a).w();
        Map<String, List<String>> map = w10.get("1");
        Map<String, List<String>> map2 = w10.get("0");
        i(map, arrayList);
        h(map2, arrayList);
        StartupDataUtils.h(this.f170a).C(map, map2, true, false);
    }
}
