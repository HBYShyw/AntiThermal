package aa;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.TextUtils;
import b6.LocalLog;
import com.oplus.util.OplusCommonConfig;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import u9.StartupConst;

/* compiled from: GlobalBlackListUtils.java */
/* renamed from: aa.c, reason: use source file name */
/* loaded from: classes2.dex */
public class GlobalBlackListUtils {

    /* renamed from: b, reason: collision with root package name */
    private static final String f123b = StartupConst.f18933c + "/etc/startup/blacklist_default.xml";

    /* renamed from: c, reason: collision with root package name */
    private static volatile GlobalBlackListUtils f124c = null;

    /* renamed from: a, reason: collision with root package name */
    private Context f125a;

    private GlobalBlackListUtils(Context context) {
        this.f125a = context;
    }

    public static GlobalBlackListUtils c(Context context) {
        if (f124c == null) {
            synchronized (GlobalBlackListUtils.class) {
                if (f124c == null) {
                    f124c = new GlobalBlackListUtils(context);
                }
            }
        }
        return f124c;
    }

    private void g(List<String> list) {
        if (list == null) {
            return;
        }
        StartupDataUtils.h(this.f125a).F("/startup/exp_global_blacklist_default.txt", list);
    }

    public ArrayList<String> a() {
        LocalLog.a("StartupManager.GlobalBlackList", "getBlackList start");
        return StartupDataUtils.h(this.f125a).y("/startup/exp_global_blacklist.txt");
    }

    public List<String> b() {
        LocalLog.a("StartupManager.GlobalBlackList", "getBlackListDefault start");
        new ArrayList();
        ArrayList<String> y4 = StartupDataUtils.h(this.f125a).y("/startup/exp_global_blacklist_default.txt");
        if (!y4.isEmpty()) {
            return y4;
        }
        LocalLog.a("StartupManager.GlobalBlackList", "getBlackListDefault: empty");
        List<String> d10 = d();
        g(d10);
        return d10;
    }

    public List<String> d() {
        int next;
        LocalLog.a("StartupManager.GlobalBlackList", "getLocalBlackListDefault start");
        ArrayList arrayList = new ArrayList();
        File file = new File(f123b);
        if (!file.exists()) {
            return arrayList;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
            newPullParser.setInput(fileInputStream, null);
            do {
                next = newPullParser.next();
                if (next == 2 && newPullParser.getName().equals("p")) {
                    String attributeValue = newPullParser.getAttributeValue(null, "package_name");
                    if (!TextUtils.isEmpty(attributeValue)) {
                        arrayList.add(attributeValue);
                    }
                }
            } while (next != 1);
        } catch (Exception e10) {
            LocalLog.b("StartupManager.GlobalBlackList", "getLocalBlackListDefault fail " + e10);
        }
        return arrayList;
    }

    public void e(List<String> list) {
        LocalLog.a("StartupManager.GlobalBlackList", "initBlackListFile start");
        PackageManager packageManager = this.f125a.getPackageManager();
        ArrayList arrayList = new ArrayList();
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(128);
        if (installedApplications != null) {
            int size = installedApplications.size();
            for (int i10 = 0; i10 < size; i10++) {
                String trim = installedApplications.get(i10).packageName.trim();
                if (list.contains(trim)) {
                    arrayList.add(trim);
                }
            }
        }
        f(arrayList);
    }

    public void f(List<String> list) {
        if (list == null) {
            return;
        }
        StartupDataUtils.h(this.f125a).F("/startup/exp_global_blacklist.txt", list);
    }

    public void h() {
        LocalLog.a("StartupManager.GlobalBlackList", "setBlackListToSystem start");
        ArrayList<String> a10 = a();
        try {
            Bundle bundle = new Bundle();
            bundle.putString("updatelist", "ex_global_black_list");
            bundle.putStringArrayList("ex_global_black_list", a10);
            bundle.putInt("userid", UserHandle.myUserId());
            StartupDataSyncUtils.f(bundle);
            OplusCommonConfig.getInstance().putConfigInfo("ex_global_black_list", bundle, 1);
        } catch (NoClassDefFoundError e10) {
            LocalLog.a("StartupManager.GlobalBlackList", "setBlackListToSystem error" + e10);
        }
    }

    public void i(List<String> list, List<String> list2) {
        LocalLog.a("StartupManager.GlobalBlackList", "updateBlackListReal start");
        List<String> b10 = b();
        for (String str : list) {
            if (!b10.contains(str)) {
                b10.add(str);
            }
        }
        b10.removeAll(list2);
        g(b10);
        e(b10);
        h();
    }

    public void j(String str, boolean z10) {
        if (!TextUtils.isEmpty(str) && z10) {
            if (b().contains(str)) {
                ArrayList<String> a10 = a();
                a10.add(str);
                f(a10);
                h();
                return;
            }
            ArrayList<String> a11 = a();
            if (a11.contains(str)) {
                a11.remove(str);
                f(a11);
                h();
            }
        }
    }
}
