package u9;

import aa.CleanupDataUtils;
import aa.MaliciousPreventUtils;
import aa.SPSListUtils;
import aa.StartupBackupRestoreUtils;
import aa.StartupDataSyncUtils;
import aa.StartupDataUtils;
import aa.UnstableAppUtils;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.util.Log;
import b6.LocalLog;
import ba.CdoGameAppsConfigListParser;
import ba.SysStartupManagerConfigListParser;
import ba.SysStartupV3ConfigListParser;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import f6.CommonUtil;
import java.util.ArrayList;
import w4.Affair;
import w4.IAffairCallback;
import x9.WhiteListUtils;

/* compiled from: StartupManagerAction.java */
/* renamed from: u9.c, reason: use source file name */
/* loaded from: classes2.dex */
public class StartupManagerAction implements IAffairCallback {

    /* renamed from: l, reason: collision with root package name */
    private static volatile StartupManagerAction f18945l;

    /* renamed from: e, reason: collision with root package name */
    private Handler f18946e;

    /* renamed from: f, reason: collision with root package name */
    private Context f18947f;

    /* renamed from: g, reason: collision with root package name */
    private volatile boolean f18948g = false;

    /* renamed from: h, reason: collision with root package name */
    private volatile boolean f18949h = false;

    /* renamed from: i, reason: collision with root package name */
    private volatile boolean f18950i = false;

    /* renamed from: j, reason: collision with root package name */
    private long f18951j = 0;

    /* renamed from: k, reason: collision with root package name */
    private StartupDataSyncUtils f18952k = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: StartupManagerAction.java */
    /* renamed from: u9.c$a */
    /* loaded from: classes2.dex */
    public class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ Bundle f18953e;

        a(Bundle bundle) {
            this.f18953e = bundle;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.f18953e == null) {
                LocalLog.b("StartupManager", "handleMaliciousRestrictRecordUpdate null bundle");
            } else {
                MaliciousPreventUtils.i(StartupManagerAction.this.f18947f).x(this.f18953e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: StartupManagerAction.java */
    /* renamed from: u9.c$b */
    /* loaded from: classes2.dex */
    public class b implements Runnable {
        private b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            StartupBackupRestoreUtils.a(StartupManagerAction.this.f18947f).j();
            StartupManager.i(StartupManagerAction.this.f18947f).u(null);
        }

        /* synthetic */ b(StartupManagerAction startupManagerAction, a aVar) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: StartupManagerAction.java */
    /* renamed from: u9.c$c */
    /* loaded from: classes2.dex */
    public class c implements Runnable {
        private c() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (StartupManagerAction.this.f18948g) {
                LocalLog.a("StartupManager", "we are cleaning old data, boot init will delay to start");
                StartupManagerAction.this.f18949h = true;
            } else {
                if (StartupManagerAction.this.f18950i) {
                    LocalLog.a("StartupManager", "we are doing list init. no need to do boot init again");
                    return;
                }
                LocalLog.a("StartupManager", "we will do boot init.");
                StartupManagerAction.this.f18951j = 0L;
                StartupManagerAction.this.y("is_need_init", false);
                StartupDataUtils.h(StartupManagerAction.this.f18947f).o();
            }
        }

        /* synthetic */ c(StartupManagerAction startupManagerAction, a aVar) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: StartupManagerAction.java */
    /* renamed from: u9.c$d */
    /* loaded from: classes2.dex */
    public class d implements Runnable {
        private d() {
        }

        @Override // java.lang.Runnable
        public void run() {
            UnstableAppUtils.b(StartupManagerAction.this.f18947f).p();
            StartupManager.i(StartupManagerAction.this.f18947f).z();
        }

        /* synthetic */ d(StartupManagerAction startupManagerAction, a aVar) {
            this();
        }
    }

    /* compiled from: StartupManagerAction.java */
    /* renamed from: u9.c$e */
    /* loaded from: classes2.dex */
    private class e implements Runnable {
        private e() {
        }

        @Override // java.lang.Runnable
        public void run() {
            SysStartupV3ConfigListParser sysStartupV3ConfigListParser = new SysStartupV3ConfigListParser(StartupManagerAction.this.f18947f);
            sysStartupV3ConfigListParser.u(true);
            StartupManager.i(StartupManagerAction.this.f18947f).u(sysStartupV3ConfigListParser.w());
        }

        /* synthetic */ e(StartupManagerAction startupManagerAction, a aVar) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: StartupManagerAction.java */
    /* renamed from: u9.c$f */
    /* loaded from: classes2.dex */
    public class f implements Runnable {
        private f() {
        }

        @Override // java.lang.Runnable
        public void run() {
            Log.d("StartupManager", "start init");
            StartupManagerAction startupManagerAction = StartupManagerAction.this;
            startupManagerAction.f18948g = CleanupDataUtils.f(startupManagerAction.f18947f).a();
            StartupManagerAction startupManagerAction2 = StartupManagerAction.this;
            startupManagerAction2.f18950i = startupManagerAction2.n("is_need_init");
            MaliciousPreventUtils.i(StartupManagerAction.this.f18947f).n(StartupManagerAction.this.f18946e);
            SPSListUtils.c(StartupManagerAction.this.f18947f).g(StartupManagerAction.this.f18946e);
            StartupManagerAction.this.registerAction();
            StartupManagerAction startupManagerAction3 = StartupManagerAction.this;
            startupManagerAction3.f18952k = new StartupDataSyncUtils(startupManagerAction3.f18947f);
            StartupManagerAction.this.f18952k.c(StartupManagerAction.this.f18946e);
            StartupManagerAction.this.f18952k.e();
            WhiteListUtils.d(StartupManagerAction.this.f18947f).e();
            UnstableAppUtils.b(StartupManagerAction.this.f18947f).h(StartupManagerAction.this.f18946e);
            if (StartupManagerAction.this.f18948g) {
                CleanupDataUtils.f(StartupManagerAction.this.f18947f).b();
            }
            StartupDataUtils h10 = StartupDataUtils.h(StartupManagerAction.this.f18947f);
            if (StartupManagerAction.this.f18949h || StartupManagerAction.this.f18950i) {
                StartupManagerAction.this.f18949h = false;
                LocalLog.a("StartupManager", "clean data finished, start boot init: " + StartupManagerAction.this.f18949h + " isNeedInit: " + StartupManagerAction.this.f18950i);
                h10.o();
                StartupManagerAction.this.y("is_need_init", false);
            }
            h10.N();
            LocalLog.a("StartupManager", "init data finished!");
        }

        /* synthetic */ f(StartupManagerAction startupManagerAction, a aVar) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: StartupManagerAction.java */
    /* renamed from: u9.c$g */
    /* loaded from: classes2.dex */
    public class g implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        Intent f18960e;

        g(Intent intent) {
            this.f18960e = intent;
        }

        @Override // java.lang.Runnable
        public void run() {
            Intent intent = this.f18960e;
            if (intent == null || intent.getData() == null) {
                return;
            }
            StartupManager.i(StartupManagerAction.this.f18947f).l(this.f18960e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: StartupManagerAction.java */
    /* renamed from: u9.c$h */
    /* loaded from: classes2.dex */
    public class h implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        Intent f18962e;

        h(Intent intent) {
            this.f18962e = intent;
        }

        @Override // java.lang.Runnable
        public void run() {
            Intent intent = this.f18962e;
            if (intent == null || intent.getData() == null) {
                return;
            }
            StartupManager.i(StartupManagerAction.this.f18947f).m(this.f18962e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: StartupManagerAction.java */
    /* renamed from: u9.c$i */
    /* loaded from: classes2.dex */
    public class i implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        ArrayList<String> f18964e;

        i(Intent intent) {
            this.f18964e = new ArrayList<>();
            if (intent != null) {
                this.f18964e = intent.getStringArrayListExtra("ROM_UPDATE_CONFIG_LIST");
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            ArrayList<String> arrayList = this.f18964e;
            if (arrayList == null || arrayList.isEmpty()) {
                return;
            }
            if (this.f18964e.contains("sys_startupmanager_config_list")) {
                new SysStartupManagerConfigListParser(false, StartupManagerAction.this.f18947f).u(true);
            }
            if (this.f18964e.contains("cdo_game_apps_config_list")) {
                new CdoGameAppsConfigListParser(StartupManagerAction.this.f18947f).u(true);
            }
            if (this.f18964e.contains("sys_startupmanager_monitor_list")) {
                LocalLog.a("StartupManager", "sys_startupmanager_monitor_list changed!");
                StartupDataUtils.h(StartupManagerAction.this.f18947f).Q("/data/oplus/os/startup/sys_startupmanager_monitor_list.xml");
            }
            if (this.f18964e.contains("sys_ams_skipbroadcast")) {
                LocalLog.a("StartupManager", "sys_ams_skipbroadcast changed!");
                StartupDataUtils.h(StartupManagerAction.this.f18947f).Q("sys_ams_skipbroadcast.xml");
            }
            if (this.f18964e.contains("sys_startup_v3_config_list")) {
                SysStartupV3ConfigListParser sysStartupV3ConfigListParser = new SysStartupV3ConfigListParser(StartupManagerAction.this.f18947f);
                sysStartupV3ConfigListParser.u(true);
                if (CommonUtil.T(StartupManagerAction.this.f18947f)) {
                    LocalLog.l("StartupManager", "UpdateRUSConfigList, skip boot reg");
                } else {
                    StartupManager.i(StartupManagerAction.this.f18947f).u(sysStartupV3ConfigListParser.w());
                }
            }
        }
    }

    public StartupManagerAction(Context context) {
        this.f18946e = null;
        this.f18947f = null;
        this.f18947f = context;
        HandlerThread handlerThread = new HandlerThread("StartupManager");
        handlerThread.start();
        this.f18946e = new Handler(handlerThread.getLooper());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean n(String str) {
        this.f18951j = SystemClock.elapsedRealtime();
        SharedPreferences sharedPreferences = this.f18947f.getSharedPreferences("check_list_init", 0);
        long j10 = sharedPreferences.getLong("last_boot_time", 0L);
        if (j10 == 0 || this.f18951j - j10 > 14400000) {
            return true;
        }
        return sharedPreferences.getBoolean(str, true);
    }

    public static StartupManagerAction o(Context context) {
        if (f18945l == null) {
            synchronized (StartupManagerAction.class) {
                if (f18945l == null) {
                    f18945l = new StartupManagerAction(context);
                }
            }
        }
        return f18945l;
    }

    private void p() {
        this.f18946e.post(new b(this, null));
    }

    private void q() {
        this.f18946e.post(new c(this, null));
    }

    private void r() {
        this.f18946e.post(new d(this, null));
    }

    private void t(Intent intent) {
        this.f18946e.post(new g(intent));
    }

    private void u(Intent intent) {
        this.f18946e.post(new h(intent));
    }

    private void v(Intent intent) {
        this.f18946e.post(new i(intent));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void y(String str, boolean z10) {
        SharedPreferences.Editor edit = this.f18947f.getSharedPreferences("check_list_init", 0).edit();
        edit.putBoolean(str, z10);
        edit.putLong("last_boot_time", this.f18951j);
        edit.apply();
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        if (i10 == 200) {
            q();
            return;
        }
        if (i10 == 207) {
            r();
            return;
        }
        if (i10 == 219) {
            v(intent);
            return;
        }
        if (i10 == 1401) {
            p();
        } else if (i10 == 1101) {
            t(intent);
        } else {
            if (i10 != 1102) {
                return;
            }
            u(intent);
        }
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, EventType.SCENE_MODE_VPN);
        Affair.f().g(this, EventType.SCENE_MODE_READING);
        Affair.f().g(this, 1101);
        Affair.f().g(this, 1102);
        Affair.f().g(this, 1401);
        Affair.f().g(this, 1402);
        SPSListUtils.c(this.f18947f).i();
        SPSListUtils.c(this.f18947f).j();
        WhiteListUtils.m(this.f18947f, this.f18946e);
    }

    public void s(Bundle bundle) {
        this.f18946e.post(new a(bundle));
    }

    public void w() {
        this.f18946e.post(new f(this, null));
    }

    public void x() {
        LocalLog.l("StartupManager", "init Data After BootReg!");
        this.f18946e.post(new e(this, null));
    }
}
