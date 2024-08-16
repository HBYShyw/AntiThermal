package z5;

import android.content.Context;
import b6.LocalLog;
import f6.CommonUtil;
import java.util.ArrayList;
import java.util.List;

/* compiled from: GuardElfDataManager.java */
/* renamed from: z5.a, reason: use source file name */
/* loaded from: classes.dex */
public class GuardElfDataManager {

    /* renamed from: c, reason: collision with root package name */
    private static GuardElfDataManager f20238c;

    /* renamed from: a, reason: collision with root package name */
    private Context f20239a;

    /* renamed from: b, reason: collision with root package name */
    private List<String> f20240b = new ArrayList();

    private GuardElfDataManager(Context context) {
        this.f20239a = context;
    }

    public static synchronized GuardElfDataManager d(Context context) {
        GuardElfDataManager guardElfDataManager;
        synchronized (GuardElfDataManager.class) {
            if (f20238c == null) {
                f20238c = new GuardElfDataManager(context);
            }
            guardElfDataManager = f20238c;
        }
        return guardElfDataManager;
    }

    public void a(String str, String str2) {
        List<String> e10 = e(str2);
        if (e10.contains(str)) {
            return;
        }
        e10.add(str);
        LocalFileUtil.c().l("battery", str2, e10, this.f20239a);
    }

    public void b(List<String> list, String str) {
        List<String> e10 = e(str);
        boolean z10 = false;
        for (String str2 : list) {
            if (!e10.contains(str2)) {
                e10.add(str2);
                z10 = true;
            }
        }
        if (z10) {
            LocalFileUtil.c().l("battery", str, e10, this.f20239a);
        }
    }

    public void c(String str) {
        if (str == null) {
            return;
        }
        LocalLog.a("GuardElfDataManager", "addPeriodForeList " + str);
        synchronized (f20238c) {
            if (!this.f20240b.contains(str)) {
                this.f20240b.add(str);
            }
        }
    }

    public List<String> e(String str) {
        return LocalFileUtil.c().k("battery", str, this.f20239a);
    }

    public List<String> f() {
        List<String> list;
        synchronized (f20238c) {
            list = this.f20240b;
        }
        return list;
    }

    public void g(Context context, String str, ArrayList<String> arrayList) {
        String G;
        if (arrayList == null || str == null) {
            return;
        }
        List<String> e10 = e("startinfo_white.xml");
        for (int i10 = 0; i10 < arrayList.size(); i10++) {
            String str2 = arrayList.get(i10);
            if ((!str.equals("startinfo") || !str2.contains("warning")) && (G = CommonUtil.G(str2)) != null && !G.equals("") && !G.equals("null") && !CommonUtil.X(context, G) && str.equals("startinfo") && e10.contains(G) && !d(this.f20239a).e("notify_whitelist.xml").contains(G)) {
                a(G, "startinfo_user_not_restrict_thistime.xml");
            }
        }
    }

    public void h(List<String> list, String str) {
        List<String> e10 = e(str);
        boolean z10 = false;
        for (String str2 : list) {
            if (e10.contains(str2)) {
                e10.remove(str2);
                z10 = true;
            }
        }
        if (z10) {
            LocalFileUtil.c().l("battery", str, e10, this.f20239a);
        }
    }

    public boolean i(String str, String str2) {
        List<String> e10 = e(str2);
        if (!e10.contains(str)) {
            return false;
        }
        e10.remove(str);
        LocalFileUtil.c().l("battery", str2, e10, this.f20239a);
        return true;
    }

    public void j() {
        synchronized (f20238c) {
            this.f20240b = CommonUtil.m(this.f20239a);
            if (LocalLog.f()) {
                LocalLog.a("GuardElfDataManager", "resetPeriodForeList: periodForeList=" + this.f20240b);
            }
        }
    }

    public void k(List<String> list, String str) {
        LocalFileUtil.c().l("battery", str, list, this.f20239a);
    }
}
