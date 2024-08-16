package aa;

import aa.StartupDataSyncUtils;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.text.TextUtils;
import b6.LocalLog;
import com.oplus.datasync.ISysStateChangeCallback;
import com.oplus.datasync.OplusDataSyncManager;
import u9.StartupManager;

/* compiled from: StartupDataSyncUtils.java */
/* renamed from: aa.h, reason: use source file name */
/* loaded from: classes2.dex */
public class StartupDataSyncUtils {

    /* renamed from: a, reason: collision with root package name */
    private Context f173a;

    /* renamed from: b, reason: collision with root package name */
    private a f174b = null;

    /* renamed from: c, reason: collision with root package name */
    private Handler f175c = null;

    /* compiled from: StartupDataSyncUtils.java */
    /* renamed from: aa.h$a */
    /* loaded from: classes2.dex */
    public class a extends ISysStateChangeCallback.Stub {
        public a() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void A(Bundle bundle) {
            MaliciousPreventUtils.i(StartupDataSyncUtils.this.f173a).w(bundle);
        }

        public void onSysStateChanged(final Bundle bundle) {
            if (bundle != null) {
                String string = bundle.getString("app_channel_type");
                if (string != null && "unstable".equals(string)) {
                    int i10 = bundle.getInt("userId");
                    if (i10 != UserHandle.myUserId()) {
                        LocalLog.a("StartupManager", "onSysStateChanged: unstable didn't happen in user " + i10);
                        return;
                    }
                    String string2 = bundle.getString("reason");
                    if (!TextUtils.isEmpty(string2) && string2.equals("crash")) {
                        LocalLog.a("StartupManager", "onUpdateUnstableRecord: unstable crash!");
                        UnstableAppUtils.b(StartupDataSyncUtils.this.f173a).j(bundle);
                    }
                }
                if ((string != null && "prevent".equals(string)) || "startup".equals(string)) {
                    if (LocalLog.f()) {
                        LocalLog.a("StartupManager", "onSysStateChanged: update launch record " + bundle);
                    }
                    StartupManager.i(StartupDataSyncUtils.this.f173a).x(bundle);
                }
                if (string != null && "unstable_work".equals(string)) {
                    int i11 = bundle.getInt("userId");
                    if (i11 != UserHandle.myUserId()) {
                        LocalLog.l("StartupManager", "onSysStateChanged: unstable prevent didn't happen in user " + i11);
                        return;
                    }
                    String string3 = bundle.getString("reason");
                    String string4 = bundle.getString("packageName");
                    if (!TextUtils.isEmpty(string3) && !TextUtils.isEmpty(string4)) {
                        LocalLog.l("StartupManager", "onUpdateUnstablePreventRecord:  prevent record :" + string4 + " reason : " + string3);
                        UnstableAppUtils.b(StartupDataSyncUtils.this.f173a).k(bundle);
                    }
                }
                if (string == null || !"malicious_intercept".equals(string)) {
                    return;
                }
                int i12 = bundle.getInt("userId");
                if (i12 != UserHandle.myUserId()) {
                    LocalLog.l("StartupManager", "onSysStateChanged: malicious prevent didn't happen in user " + i12);
                    return;
                }
                StartupDataSyncUtils.this.f175c.post(new Runnable() { // from class: aa.g
                    @Override // java.lang.Runnable
                    public final void run() {
                        StartupDataSyncUtils.a.this.A(bundle);
                    }
                });
            }
        }
    }

    public StartupDataSyncUtils(Context context) {
        this.f173a = context;
    }

    public static void d(String str) {
        String substring = str.substring(str.lastIndexOf(47) + 1);
        LocalLog.a("StartupManager", "notifyListChanged:  " + substring);
        Bundle bundle = new Bundle();
        bundle.putString("updatelist", substring);
        bundle.putInt("userid", UserHandle.myUserId());
        f(bundle);
    }

    public static void f(Bundle bundle) {
        try {
            OplusDataSyncManager.getInstance().updateAppData("startup", bundle);
        } catch (Exception e10) {
            e10.printStackTrace();
            LocalLog.a("StartupManager", "update app data failed");
        }
    }

    public void c(Handler handler) {
        this.f175c = handler;
    }

    public void e() {
        try {
            OplusDataSyncManager oplusDataSyncManager = OplusDataSyncManager.getInstance();
            a aVar = new a();
            this.f174b = aVar;
            oplusDataSyncManager.registerSysStateChangeObserver("startup", aVar);
            LocalLog.a("StartupManager", "data sync channel succeed for mode: startup");
        } catch (Exception e10) {
            LocalLog.a("StartupManager", "data sync channel set failed :" + e10);
        }
    }
}
