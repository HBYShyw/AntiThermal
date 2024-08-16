package v6;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import b6.LocalLog;
import c6.NotifyUtil;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import d6.ConfigUpdateUtil;
import f6.CommonUtil;
import ia.AppInfoUtils;
import java.util.ArrayList;
import java.util.List;
import r9.SimplePowerMonitorUtils;
import u4.IRemoteGuardElfInterface;
import v4.GuardElfContext;
import w4.Affair;
import w4.IAffairCallback;
import w6.PluginSupporter;
import w6.RegionPluginUtil;
import x5.UploadDataUtil;
import y5.AppFeature;

/* compiled from: AffairResControl.java */
/* renamed from: v6.b, reason: use source file name */
/* loaded from: classes.dex */
public class AffairResControl implements IAffairCallback {

    /* renamed from: o, reason: collision with root package name */
    private static Context f19134o;

    /* renamed from: e, reason: collision with root package name */
    private IRemoteGuardElfInterface f19135e;

    /* renamed from: h, reason: collision with root package name */
    private Handler f19138h;

    /* renamed from: f, reason: collision with root package name */
    private Object f19136f = new Object();

    /* renamed from: g, reason: collision with root package name */
    private ConfigUpdateUtil f19137g = null;

    /* renamed from: i, reason: collision with root package name */
    private boolean f19139i = false;

    /* renamed from: j, reason: collision with root package name */
    private boolean f19140j = false;

    /* renamed from: k, reason: collision with root package name */
    private Object f19141k = new Object();

    /* renamed from: l, reason: collision with root package name */
    ServiceConnection f19142l = new a();

    /* renamed from: m, reason: collision with root package name */
    private final Runnable f19143m = new b();

    /* renamed from: n, reason: collision with root package name */
    private final Runnable f19144n = new c();

    /* compiled from: AffairResControl.java */
    /* renamed from: v6.b$a */
    /* loaded from: classes.dex */
    class a implements ServiceConnection {
        a() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LocalLog.a("AffairResControl", "RemoteGuardElf connected!");
            AffairResControl.this.f19135e = IRemoteGuardElfInterface.a.z(iBinder);
            synchronized (AffairResControl.this.f19136f) {
                AffairResControl.this.f19136f.notifyAll();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            AffairResControl.this.f19135e = null;
            LocalLog.a("AffairResControl", "RemoteGuardElf disconnected!");
        }
    }

    /* compiled from: AffairResControl.java */
    /* renamed from: v6.b$b */
    /* loaded from: classes.dex */
    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            AffairResControl.this.q();
        }
    }

    /* compiled from: AffairResControl.java */
    /* renamed from: v6.b$c */
    /* loaded from: classes.dex */
    class c implements Runnable {
        c() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (AffairResControl.this.f19141k) {
                AffairResControl.this.f19139i = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AffairResControl.java */
    /* renamed from: v6.b$d */
    /* loaded from: classes.dex */
    public class d implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ String f19148e;

        d(String str) {
            this.f19148e = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            AffairResControl.this.t(this.f19148e);
        }
    }

    /* compiled from: AffairResControl.java */
    /* renamed from: v6.b$e */
    /* loaded from: classes.dex */
    private static class e {

        /* renamed from: a, reason: collision with root package name */
        private static final AffairResControl f19150a = new AffairResControl();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AffairResControl.java */
    /* renamed from: v6.b$f */
    /* loaded from: classes.dex */
    public class f implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        private Context f19151e;

        f(Context context) {
            this.f19151e = context;
        }

        @Override // java.lang.Runnable
        public void run() {
            Intent m10 = AffairResControl.this.m(AffairResControl.f19134o, new Intent("oplus.intent.action.REQUEST_APP_CLEAN_RUNNING"));
            if (m10 != null) {
                synchronized (AffairResControl.this.f19141k) {
                    AffairResControl.this.f19139i = true;
                }
                LocalLog.a("AffairResControl", "start ClearRunningAppService from Battery.-----------------");
                m10.putStringArrayListExtra("filterapplist", CommonUtil.x(this.f19151e));
                m10.putExtra("IsShowCleanFinishToast", false);
                m10.putExtra("screenOnLowMemClear", true);
                m10.putExtra("clean_trash", false);
                m10.putExtra("caller_package", "com.oplus.battery.lowmem");
                m10.putExtra("reason", "com.oplus.battery.lowmem");
                this.f19151e.startService(m10);
                if (AffairResControl.this.f19138h == null) {
                    AffairResControl.this.f19138h = new Handler(Looper.myLooper());
                }
                AffairResControl.this.f19138h.removeCallbacks(AffairResControl.this.f19144n);
                AffairResControl.this.f19138h.sendMessageDelayed(Message.obtain(AffairResControl.this.f19138h, AffairResControl.this.f19144n), 15000L);
                if (AffairResControl.this.f19137g.t()) {
                    UploadDataUtil.S0(this.f19151e).S();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AffairResControl.java */
    /* renamed from: v6.b$g */
    /* loaded from: classes.dex */
    public class g extends Thread {

        /* renamed from: e, reason: collision with root package name */
        private String f19153e;

        g(String str) {
            this.f19153e = str;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            super.run();
            String str = this.f19153e;
            if (str == null || str.equals("") || CommonUtil.y().equals(this.f19153e) || CommonUtil.U()) {
                return;
            }
            try {
                if (AppFeature.D()) {
                    LocalLog.b("AffairResControl", "K bg assistant app " + this.f19153e);
                    CommonUtil.Y(AffairResControl.f19134o, -1, this.f19153e, "assistapp");
                } else {
                    LocalLog.b("AffairResControl", "F bg assistant app " + this.f19153e);
                    ((ActivityManager) AffairResControl.f19134o.getSystemService("activity")).forceStopPackage(this.f19153e);
                }
            } catch (Exception unused) {
                LocalLog.a("AffairResControl", "Error forceStopPackage");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Intent m(Context context, Intent intent) {
        List<ResolveInfo> queryIntentServices = context.getPackageManager().queryIntentServices(intent, 0);
        if (queryIntentServices == null || queryIntentServices.size() != 1) {
            return null;
        }
        ServiceInfo serviceInfo = queryIntentServices.get(0).serviceInfo;
        ComponentName componentName = new ComponentName(serviceInfo.packageName, serviceInfo.name);
        Intent intent2 = new Intent(intent);
        intent2.setComponent(componentName);
        return intent2;
    }

    public static AffairResControl n() {
        f19134o = GuardElfContext.e().c();
        return e.f19150a;
    }

    private void o(Bundle bundle) {
        String string = bundle.getString("pre_app_pkgname", "");
        String string2 = bundle.getString("next_app_pkgname", "");
        bundle.getString("next_activity", "");
        if (!TextUtils.isEmpty(string) && !TextUtils.isEmpty(string2) && this.f19137g.H(string) && CommonUtil.u(f19134o).equals(string2)) {
            if (this.f19138h == null) {
                this.f19138h = new Handler(Looper.myLooper());
            }
            this.f19138h.postDelayed(new d(string), 5000L);
        }
        if (!TextUtils.isEmpty(string2) && this.f19140j && ConfigUpdateUtil.n(f19134o).r() && string2.equals(CommonUtil.v(f19134o))) {
            long r10 = CommonUtil.r(f19134o);
            LocalLog.d("AffairResControl", "availMem = " + r10);
            if (r10 < ConfigUpdateUtil.n(f19134o).s()) {
                if (this.f19138h == null) {
                    this.f19138h = new Handler(Looper.myLooper());
                }
                this.f19138h.removeCallbacks(this.f19143m);
                this.f19138h.sendMessageDelayed(Message.obtain(this.f19138h, this.f19143m), 500L);
            }
        }
    }

    private void p() {
        LocalLog.a("AffairResControl", "handleConnectionChange");
        RegionPluginUtil o10 = PluginSupporter.m().o();
        if (o10 != null) {
            o10.b();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void q() {
        synchronized (this.f19141k) {
            if (this.f19139i) {
                LocalLog.a("AffairResControl", "is low-mem clearing, return");
            } else {
                new Thread(new f(f19134o)).start();
            }
        }
    }

    private void s(String str) {
        boolean z10;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        f6.f.l(arrayList, arrayList2, f19134o);
        boolean z11 = true;
        if (arrayList.contains(str)) {
            arrayList.remove(str);
            z10 = true;
        } else {
            z10 = false;
        }
        if (arrayList2.contains(str)) {
            arrayList2.remove(str);
        } else {
            z11 = z10;
        }
        if (!z11) {
            LocalLog.a("AffairResControl", "handlePkgRmv: no change. pkg=" + str);
            return;
        }
        if (this.f19135e == null) {
            LocalLog.a("AffairResControl", "handlePkgRmv: RemoteGuardElf is null. wait...");
            synchronized (this.f19136f) {
                f6.f.b(this.f19142l, f19134o);
                try {
                    this.f19136f.wait(2000L);
                } catch (Exception e10) {
                    LocalLog.b("AffairResControl", "handlePkgRmv: wait Exception " + e10);
                }
            }
        }
        if (this.f19135e != null) {
            try {
                LocalLog.a("AffairResControl", "handlePkgRmv: PowerProtectPolicyChange");
                this.f19135e.e(str, 3);
            } catch (Exception e11) {
                LocalLog.b("AffairResControl", "handlePkgRmv: exec Exception " + e11);
            }
        }
        f6.f.N1(arrayList, arrayList2, f19134o.getApplicationContext());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void t(String str) {
        new g(str).start();
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        String schemeSpecificPart;
        if (i10 == 220) {
            NotifyUtil.v(f19134o).Y();
            return;
        }
        if (i10 == 221) {
            LocalLog.a("AffairResControl", "isForceClose = " + (!intent.getBooleanExtra("mIsLearningClearing", true)));
            return;
        }
        if (i10 != 224) {
            switch (i10) {
                case 1101:
                case 1103:
                    boolean z10 = !intent.getBooleanExtra("android.intent.extra.REPLACING", false);
                    schemeSpecificPart = intent.getData() != null ? intent.getData().getSchemeSpecificPart() : null;
                    LocalLog.a("AffairResControl", "pkg add or available=" + z10 + ", pkg=" + schemeSpecificPart);
                    if (z10 && schemeSpecificPart != null) {
                        r(schemeSpecificPart);
                    }
                    try {
                        AppInfoUtils.d().b(f19134o.getPackageManager().getApplicationInfo(schemeSpecificPart, 128));
                        return;
                    } catch (PackageManager.NameNotFoundException e10) {
                        e10.printStackTrace();
                        return;
                    }
                case 1102:
                    boolean z11 = !intent.getBooleanExtra("android.intent.extra.REPLACING", false);
                    schemeSpecificPart = intent.getData() != null ? intent.getData().getSchemeSpecificPart() : null;
                    LocalLog.a("AffairResControl", "pkg remove=" + z11 + ", pkg=" + schemeSpecificPart);
                    if (z11 && schemeSpecificPart != null) {
                        s(schemeSpecificPart);
                    }
                    if (SimplePowerMonitorUtils.f17653d && UserHandle.myUserId() == 0 && intent.getDataString() != null) {
                        String dataString = intent.getDataString();
                        if (dataString.length() > 8) {
                            String substring = dataString.substring(8);
                            if (substring.length() > 0) {
                                LocalLog.a("AffairResControl", "removed:" + substring + " programe    ");
                                String str = substring + " " + SimplePowerMonitorUtils.l(dataString, f19134o);
                                if (SimplePowerMonitorUtils.e().containsKey(str)) {
                                    Log.d("AffairResControl", "mPackageNameList---removed:" + str + " programe    ");
                                    SimplePowerMonitorUtils.e().remove(str);
                                }
                            }
                        }
                    }
                    AppInfoUtils.d().g(schemeSpecificPart);
                    f6.f.o2(f19134o, schemeSpecificPart, false);
                    return;
                default:
                    return;
            }
        }
        p();
    }

    public void r(String str) {
        RegionPluginUtil o10 = PluginSupporter.m().o();
        if (o10 != null) {
            o10.d(str);
        }
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, 1101);
        Affair.f().g(this, 1103);
        Affair.f().g(this, 1102);
        Affair.f().g(this, EventType.SCENE_SHORT_VIDEO);
        Affair.f().g(this, EventType.SCENE_MODE_RECORDING);
        Affair.f().g(this, 300);
        Affair.f().g(this, 224);
    }

    public void u() {
        this.f19137g = ConfigUpdateUtil.n(f19134o);
        this.f19140j = f19134o.getPackageManager().hasSystemFeature("oplus.lowramclear.support");
        registerAction();
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
        if (i10 != 300) {
            return;
        }
        o(bundle);
    }
}
