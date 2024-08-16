package x4;

import android.content.Context;
import android.os.UserHandle;
import b6.LocalLog;
import com.oplus.app.OplusAppEnterInfo;
import com.oplus.app.OplusAppExitInfo;
import com.oplus.app.OplusAppSwitchConfig;
import com.oplus.app.OplusAppSwitchManager;
import com.oplus.performance.GTModeBroadcastReceiver;
import java.util.List;

/* compiled from: AppSwitchDetectorV2.java */
/* renamed from: x4.b, reason: use source file name */
/* loaded from: classes.dex */
public class AppSwitchDetectorV2 extends AbsAppSwitchDetector {

    /* renamed from: d, reason: collision with root package name */
    private AppSwitchObserverHelper f19514d;

    /* renamed from: e, reason: collision with root package name */
    private String[] f19515e;

    /* renamed from: f, reason: collision with root package name */
    private OplusAppSwitchManager.OnAppSwitchObserver f19516f;

    /* compiled from: AppSwitchDetectorV2.java */
    /* renamed from: x4.b$a */
    /* loaded from: classes.dex */
    class a implements OplusAppSwitchManager.OnAppSwitchObserver {
        a() {
        }

        public void a() {
            if (AppSwitchDetectorV2.this.f19515e[0] == null || AppSwitchDetectorV2.this.f19515e[1] == null) {
                return;
            }
            AppSwitchDetectorV2.this.f19514d.f(AppSwitchDetectorV2.this.f19515e[0], AppSwitchDetectorV2.this.f19515e[1]);
            AppSwitchDetectorV2.this.f19515e[0] = null;
            AppSwitchDetectorV2.this.f19515e[1] = null;
        }

        public void onActivityEnter(OplusAppEnterInfo oplusAppEnterInfo) {
            if (LocalLog.f()) {
                LocalLog.a("AppSwitchDetectorV2", "OnAppSwitchObserver: onActivityEnter , info = " + oplusAppEnterInfo.targetName);
            }
        }

        public void onActivityExit(OplusAppExitInfo oplusAppExitInfo) {
            if (LocalLog.f()) {
                LocalLog.a("AppSwitchDetectorV2", "OnAppSwitchObserver: onActivityExit , info = " + oplusAppExitInfo.targetName);
            }
        }

        public void onAppEnter(OplusAppEnterInfo oplusAppEnterInfo) {
            String str = oplusAppEnterInfo.targetName;
            if (LocalLog.f()) {
                LocalLog.a("AppSwitchDetectorV2", "OnAppSwitchObserver: onAppEnter , info = " + str);
            }
            AppSwitchDetectorV2.this.f19515e[0] = str;
            if (LocalLog.f()) {
                LocalLog.a("AppSwitchDetectorV2", "enterPackageName: " + AppSwitchDetectorV2.this.f19515e[0]);
            }
            if (UserHandle.myUserId() == 0) {
                a();
                if (y5.b.E()) {
                    GTModeBroadcastReceiver.n(AppSwitchDetectorV2.this.f19512b, oplusAppEnterInfo.targetName);
                }
            }
        }

        public void onAppExit(OplusAppExitInfo oplusAppExitInfo) {
            if (LocalLog.f()) {
                LocalLog.a("AppSwitchDetectorV2", "OnAppSwitchObserver: onAppExit , info = " + oplusAppExitInfo.targetName);
            }
            String str = oplusAppExitInfo.targetName;
            AppSwitchDetectorV2.this.f19515e[1] = oplusAppExitInfo.targetName;
            if (LocalLog.f()) {
                LocalLog.a("AppSwitchDetectorV2", "exitPackageName: " + AppSwitchDetectorV2.this.f19515e[1]);
            }
            if (UserHandle.myUserId() == 0) {
                a();
            }
            if (oplusAppExitInfo.hasResumingActivity) {
                AppSwitchDetectorV2.this.f19511a.a(str, oplusAppExitInfo.resumingPackageName, oplusAppExitInfo.resumingActivityName);
            }
        }
    }

    public AppSwitchDetectorV2(AppSwitchObserver appSwitchObserver, Context context) {
        super(appSwitchObserver, context);
        this.f19515e = new String[]{null, null};
        this.f19516f = new a();
        if (UserHandle.myUserId() == 0) {
            this.f19514d = AppSwitchObserverHelper.d(context);
        }
    }

    @Override // x4.AbsAppSwitchDetector
    public void a() {
        LocalLog.a("AppSwitchDetectorV2", "start");
        OplusAppSwitchConfig oplusAppSwitchConfig = new OplusAppSwitchConfig();
        oplusAppSwitchConfig.addAppConfig(2, (List) null);
        OplusAppSwitchManager.getInstance().registerAppSwitchObserver(this.f19512b, this.f19516f, oplusAppSwitchConfig);
    }

    @Override // x4.AbsAppSwitchDetector
    public void b() {
        OplusAppSwitchManager.getInstance().unregisterAppSwitchObserver(this.f19512b, this.f19516f);
    }
}
