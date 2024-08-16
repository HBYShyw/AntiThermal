package x4;

import android.content.Context;
import b6.LocalLog;
import com.oplus.app.OplusAppSwitchManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* compiled from: AppSwitchManager.java */
/* renamed from: x4.c, reason: use source file name */
/* loaded from: classes.dex */
public class AppSwitchManager {

    /* renamed from: a, reason: collision with root package name */
    private AbsAppSwitchDetector f19518a;

    /* renamed from: b, reason: collision with root package name */
    private List<AppSwitchObserver> f19519b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    private AppSwitchObserver f19520c = new a();

    /* compiled from: AppSwitchManager.java */
    /* renamed from: x4.c$a */
    /* loaded from: classes.dex */
    class a implements AppSwitchObserver {
        a() {
        }

        @Override // x4.AppSwitchObserver
        public void a(String str, String str2, String str3) {
            Iterator it = AppSwitchManager.this.f19519b.iterator();
            while (it.hasNext()) {
                ((AppSwitchObserver) it.next()).a(str, str2, str3);
            }
        }
    }

    public AppSwitchManager(Context context) {
        LocalLog.a("AppSwitchManager", "APP_SWITCH_VERSION:" + OplusAppSwitchManager.APP_SWITCH_VERSION);
        this.f19518a = new AppSwitchDetectorV2(this.f19520c, context);
    }

    public void b(AppSwitchObserver appSwitchObserver) {
        if (appSwitchObserver == null || this.f19519b.contains(appSwitchObserver)) {
            return;
        }
        if (this.f19519b.size() == 0) {
            this.f19518a.c();
        }
        this.f19519b.add(appSwitchObserver);
    }

    public void c(AppSwitchObserver appSwitchObserver) {
        if (this.f19519b.contains(appSwitchObserver)) {
            this.f19519b.remove(appSwitchObserver);
            if (this.f19519b.size() == 0) {
                this.f19518a.d();
            }
        }
    }
}
