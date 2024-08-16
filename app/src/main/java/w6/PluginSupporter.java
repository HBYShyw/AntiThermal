package w6;

import android.content.Context;
import android.content.Intent;
import android.content.pm.OplusPackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import b6.LocalLog;
import com.google.android.play.core.splitinstall.SplitInstallManager;
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory;
import com.google.android.play.core.splitinstall.SplitInstallRequest;
import com.google.android.play.core.splitinstall.SplitInstallSessionState;
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.modulehub.pluginsupport.BatteryRouteServer;
import com.oplus.performance.GTModeBroadcastReceiver;
import d6.ConfigUpdateUtil;
import f6.CommonUtil;
import f6.f;
import g4.OnFailureListener;
import java.util.ArrayList;
import r6.ProcessCpuManager;
import v4.GuardElfContext;
import w4.Affair;
import w4.IAffairCallback;
import w5.OplusBatteryConstants;
import x5.UploadDataUtil;
import y5.AppFeature;
import z5.GuardElfDataManager;

/* compiled from: PluginSupporter.java */
/* renamed from: w6.d, reason: use source file name */
/* loaded from: classes.dex */
public class PluginSupporter implements IAffairCallback {

    /* renamed from: m, reason: collision with root package name */
    private static final String f19370m = "d";

    /* renamed from: e, reason: collision with root package name */
    private Context f19371e;

    /* renamed from: f, reason: collision with root package name */
    private UploadDataUtil f19372f;

    /* renamed from: g, reason: collision with root package name */
    private ConfigUpdateUtil f19373g;

    /* renamed from: h, reason: collision with root package name */
    private RegionPluginUtil f19374h;

    /* renamed from: i, reason: collision with root package name */
    private PluginMethodCaller f19375i;

    /* renamed from: j, reason: collision with root package name */
    private int f19376j;

    /* renamed from: k, reason: collision with root package name */
    private boolean f19377k;

    /* renamed from: l, reason: collision with root package name */
    private boolean f19378l;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: PluginSupporter.java */
    /* renamed from: w6.d$a */
    /* loaded from: classes.dex */
    public class a implements g4.c<Integer> {
        a() {
        }

        @Override // g4.c
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onSuccess(Integer num) {
            LocalLog.a(PluginSupporter.f19370m, "secret_dynamicfeature onStartInstallSuccess, i=" + num);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: PluginSupporter.java */
    /* renamed from: w6.d$b */
    /* loaded from: classes.dex */
    public class b implements OnFailureListener {
        b() {
        }

        @Override // g4.OnFailureListener
        public void onFailure(Exception exc) {
            LocalLog.b(PluginSupporter.f19370m, "secret_dynamicfeature onStartInstallFail, e=" + exc);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: PluginSupporter.java */
    /* renamed from: w6.d$c */
    /* loaded from: classes.dex */
    public class c implements g4.c<Integer> {
        c() {
        }

        @Override // g4.c
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onSuccess(Integer num) {
            LocalLog.a(PluginSupporter.f19370m, "restrict_dynamicfeature onStartInstallSuccess, i=" + num);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: PluginSupporter.java */
    /* renamed from: w6.d$d */
    /* loaded from: classes.dex */
    public class d implements OnFailureListener {
        d() {
        }

        @Override // g4.OnFailureListener
        public void onFailure(Exception exc) {
            LocalLog.b(PluginSupporter.f19370m, "restrict_dynamicfeature onStartInstallFail, e=" + exc);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: PluginSupporter.java */
    /* renamed from: w6.d$e */
    /* loaded from: classes.dex */
    public static class e {

        /* renamed from: a, reason: collision with root package name */
        private static final PluginSupporter f19383a = new PluginSupporter(null);
    }

    /* synthetic */ PluginSupporter(a aVar) {
        this();
    }

    private void d() {
        PluginMethodCaller pluginMethodCaller = this.f19375i;
        if (pluginMethodCaller != null) {
            pluginMethodCaller.a();
        }
    }

    private void e() {
        PluginMethodCaller pluginMethodCaller;
        if (y5.b.f() && !f.a1(this.f19371e) && (pluginMethodCaller = this.f19375i) != null) {
            pluginMethodCaller.b(OplusBatteryConstants.f19358j.booleanValue());
        }
        if (y5.b.B()) {
            if (y5.b.E()) {
                if (Settings.System.getIntForUser(this.f19371e.getContentResolver(), "gt_mode_state_setting", 0, 0) == 1) {
                    GTModeBroadcastReceiver.k(this.f19371e);
                } else {
                    GTModeBroadcastReceiver.c(this.f19371e, 4);
                }
            }
            if (y5.b.A()) {
                return;
            }
            CommonUtil.Q(this.f19371e);
            PluginMethodCaller pluginMethodCaller2 = this.f19375i;
            if (pluginMethodCaller2 != null) {
                pluginMethodCaller2.b(OplusBatteryConstants.f19358j.booleanValue());
            }
        }
    }

    private void f() {
        this.f19375i = PluginMethodCaller.c(this.f19371e);
    }

    private void g() {
        this.f19374h = RegionPluginUtil.a(this.f19371e);
    }

    private void h(String str, String str2) {
        PluginMethodCaller pluginMethodCaller;
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            if (y5.b.f() && !f.a1(this.f19371e)) {
                this.f19376j = 0;
                PluginMethodCaller pluginMethodCaller2 = this.f19375i;
                if (pluginMethodCaller2 != null) {
                    pluginMethodCaller2.d();
                }
            } else if (this.f19376j < 5) {
                LocalLog.b(f19370m, "Don't support performance spec mode or super save mode is opened!");
                this.f19376j++;
            }
        }
        if (TextUtils.isEmpty(str2) || !y5.b.f() || f.a1(this.f19371e) || (pluginMethodCaller = this.f19375i) == null) {
            return;
        }
        pluginMethodCaller.e();
    }

    private void i(String str) {
        if (TextUtils.isEmpty(str) || !ProcessCpuManager.j(this.f19371e).n()) {
            return;
        }
        GuardElfDataManager.d(this.f19371e).c(str);
    }

    private void j() {
        LocalLog.a(f19370m, "loadRestrictPlugin");
        final String str = "battery_restrict_plugin";
        SplitInstallRequest c10 = SplitInstallRequest.a().b("battery_restrict_plugin").c();
        SplitInstallManager a10 = SplitInstallManagerFactory.a(this.f19371e);
        a10.b(new SplitInstallStateUpdatedListener() { // from class: w6.c
            public final void onStateUpdate(Object obj) {
                PluginSupporter.this.q(str, (SplitInstallSessionState) obj);
            }
        });
        a10.a(c10).a(new d()).b(new c());
    }

    private void k() {
        LocalLog.a(f19370m, "loadConfigPlugin");
        final String str = "battery_secret_plugin";
        SplitInstallRequest c10 = SplitInstallRequest.a().b("battery_secret_plugin").c();
        SplitInstallManager a10 = SplitInstallManagerFactory.a(this.f19371e);
        a10.b(new SplitInstallStateUpdatedListener() { // from class: w6.b
            public final void onStateUpdate(Object obj) {
                PluginSupporter.this.r(str, (SplitInstallSessionState) obj);
            }
        });
        a10.a(c10).a(new b()).b(new a());
    }

    private void l() {
        BatteryRouteServer.getInstance(this.f19371e);
    }

    public static PluginSupporter m() {
        return e.f19383a;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void q(String str, SplitInstallSessionState splitInstallSessionState) {
        if (splitInstallSessionState.moduleNames().contains(str)) {
            String str2 = f19370m;
            LocalLog.a(str2, "installing restrict plugin: " + splitInstallSessionState);
            if (splitInstallSessionState.status() == 5) {
                LocalLog.a(str2, "loadRestrictPlugin onPluginConnected");
                g();
                this.f19378l = true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void r(String str, SplitInstallSessionState splitInstallSessionState) {
        if (splitInstallSessionState.moduleNames().contains(str)) {
            String str2 = f19370m;
            LocalLog.a(str2, "installing secret plugin: " + splitInstallSessionState);
            if (splitInstallSessionState.status() == 5) {
                LocalLog.b(str2, "loadConfigPlugin onPluginConnected");
                this.f19377k = true;
                f();
                d();
                e();
            }
        }
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        PluginMethodCaller pluginMethodCaller;
        PluginMethodCaller pluginMethodCaller2;
        PluginMethodCaller pluginMethodCaller3;
        if (i10 == 216) {
            try {
                if (new OplusPackageManager(this.f19371e).isClosedSuperFirewall()) {
                    return;
                }
                ProcessCpuManager.j(this.f19371e).r();
                return;
            } catch (Exception e10) {
                e10.printStackTrace();
                return;
            }
        }
        if (i10 != 219) {
            switch (i10) {
                case 200:
                    PluginMethodCaller pluginMethodCaller4 = this.f19375i;
                    if (pluginMethodCaller4 != null) {
                        pluginMethodCaller4.b(OplusBatteryConstants.f19358j.booleanValue());
                        return;
                    }
                    return;
                case EventType.SCENE_MODE_LOCATION /* 201 */:
                    if (this.f19375i != null) {
                        ProcessCpuManager.j(this.f19371e).h();
                        this.f19375i.k();
                    }
                    if (!y5.b.f() || f.a1(this.f19371e) || (pluginMethodCaller2 = this.f19375i) == null) {
                        return;
                    }
                    pluginMethodCaller2.g();
                    return;
                case EventType.SCENE_MODE_AUDIO_OUT /* 202 */:
                    if (!y5.b.f() || (pluginMethodCaller3 = this.f19375i) == null) {
                        return;
                    }
                    pluginMethodCaller3.f();
                    return;
                case EventType.SCENE_MODE_AUDIO_IN /* 203 */:
                    if (y5.b.n() && y5.b.I()) {
                        int B = CommonUtil.B(this.f19371e);
                        if (y5.b.B()) {
                            PluginMethodCaller pluginMethodCaller5 = this.f19375i;
                            if (pluginMethodCaller5 != null) {
                                pluginMethodCaller5.b(OplusBatteryConstants.f19358j.booleanValue());
                            }
                            CommonUtil.e0(this.f19371e);
                        } else {
                            PluginMethodCaller pluginMethodCaller6 = this.f19375i;
                            if (pluginMethodCaller6 != null) {
                                pluginMethodCaller6.b(OplusBatteryConstants.f19358j.booleanValue());
                            }
                            CommonUtil.b(this.f19371e);
                        }
                        CommonUtil.h0(this.f19371e, B);
                        if (LocalLog.f()) {
                            LocalLog.a("PowerModeUnion", "highPerformanceShutdownState:" + B);
                            return;
                        }
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
        ArrayList<String> stringArrayListExtra = intent.getStringArrayListExtra("ROM_UPDATE_CONFIG_LIST");
        if (stringArrayListExtra == null || stringArrayListExtra.isEmpty()) {
            return;
        }
        if (stringArrayListExtra.contains("sys_guardelf_config_list") && (pluginMethodCaller = this.f19375i) != null) {
            pluginMethodCaller.l();
        }
        if (stringArrayListExtra.contains("sys_benchmark_list_config") && this.f19375i != null && y5.b.B()) {
            this.f19375i.m();
        }
    }

    public PluginMethodCaller n() {
        if (this.f19377k) {
            return this.f19375i;
        }
        return null;
    }

    public RegionPluginUtil o() {
        if (this.f19378l) {
            return this.f19374h;
        }
        return null;
    }

    public void p() {
        registerAction();
        l();
        if (!AppFeature.D() || y5.b.D()) {
            k();
        }
        if (AppFeature.D()) {
            return;
        }
        j();
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, 200);
        Affair.f().g(this, EventType.SCENE_MODE_LOCATION);
        Affair.f().g(this, EventType.SCENE_MODE_AUDIO_OUT);
        Affair.f().g(this, EventType.SCENE_MODE_AUDIO_IN);
        Affair.f().g(this, 300);
        Affair.f().g(this, EventType.SCENE_MODE_BT_DEVICE);
        Affair.f().g(this, EventType.SCENE_MODE_LEARNING);
        Affair.f().g(this, EventType.SCENE_MODE_VPN);
    }

    private PluginSupporter() {
        this.f19376j = 0;
        this.f19377k = false;
        this.f19378l = false;
        Context c10 = GuardElfContext.e().c();
        this.f19371e = c10;
        this.f19372f = UploadDataUtil.S0(c10);
        this.f19373g = ConfigUpdateUtil.n(this.f19371e);
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
        if (i10 != 300) {
            return;
        }
        String string = bundle.getString("pre_app_pkgname", "");
        String string2 = bundle.getString("next_app_pkgname", "");
        bundle.getString("next_activity", "");
        if (this.f19375i != null) {
            if (y5.b.f()) {
                this.f19375i.i(bundle);
            } else if (y5.b.B() && CommonUtil.H(this.f19371e) != 0) {
                this.f19375i.h(bundle);
            }
        }
        i(string2);
        h(string, string2);
    }
}
