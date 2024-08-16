package ia;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import b6.LocalLog;
import f6.CommonUtil;
import ia.AppInfoUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import v4.GuardElfContext;

/* compiled from: AppInfoUtils.java */
/* renamed from: ia.b, reason: use source file name */
/* loaded from: classes2.dex */
public class AppInfoUtils {

    /* renamed from: d, reason: collision with root package name */
    private static volatile AppInfoUtils f12685d;

    /* renamed from: a, reason: collision with root package name */
    private List<a> f12686a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    private final Object f12687b = new Object();

    /* renamed from: c, reason: collision with root package name */
    private boolean f12688c = false;

    /* compiled from: AppInfoUtils.java */
    /* renamed from: ia.b$a */
    /* loaded from: classes2.dex */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        public String f12689a;

        /* renamed from: b, reason: collision with root package name */
        public int f12690b;

        /* renamed from: c, reason: collision with root package name */
        public int f12691c;
    }

    private AppInfoUtils() {
        e();
    }

    public static AppInfoUtils d() {
        if (f12685d == null) {
            synchronized (AppInfoUtils.class) {
                if (f12685d == null) {
                    f12685d = new AppInfoUtils();
                }
            }
        }
        return f12685d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean f(String str, a aVar) {
        return str.equals(aVar.f12689a);
    }

    public void b(ApplicationInfo applicationInfo) {
        synchronized (this.f12687b) {
            g(applicationInfo.packageName);
            a aVar = new a();
            aVar.f12690b = applicationInfo.flags;
            aVar.f12689a = applicationInfo.packageName;
            aVar.f12691c = applicationInfo.uid;
            this.f12686a.add(aVar);
        }
    }

    public List<a> c() {
        List<a> list;
        synchronized (this.f12687b) {
            list = this.f12686a;
        }
        return list;
    }

    public void e() {
        synchronized (this.f12687b) {
            Context c10 = GuardElfContext.e().c();
            if (CommonUtil.T(c10)) {
                LocalLog.b("AppInfoUtils", "skip boot reg");
                return;
            }
            if (this.f12688c) {
                LocalLog.a("AppInfoUtils", "has been initialized");
                return;
            }
            this.f12686a.clear();
            for (ApplicationInfo applicationInfo : c10.getPackageManager().getInstalledApplications(128)) {
                a aVar = new a();
                aVar.f12690b = applicationInfo.flags;
                aVar.f12689a = applicationInfo.packageName;
                aVar.f12691c = applicationInfo.uid;
                this.f12686a.add(aVar);
            }
            this.f12688c = true;
        }
    }

    public void g(final String str) {
        synchronized (this.f12687b) {
            this.f12686a.removeIf(new Predicate() { // from class: ia.a
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean f10;
                    f10 = AppInfoUtils.f(str, (AppInfoUtils.a) obj);
                    return f10;
                }
            });
        }
    }
}
