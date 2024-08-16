package k8;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.IDeviceIdleController;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import b6.LocalLog;
import b9.PowerUtils;
import com.oplus.statistics.DataTypeConstants;
import g7.AppInfoManager;
import h8.IPowerControlPresenter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import l8.IPowerControlViewUpdate;
import u4.IRemoteGuardElfInterface;
import v4.GuardElfContext;

/* compiled from: PowerControlPresenter.java */
/* renamed from: k8.f, reason: use source file name */
/* loaded from: classes2.dex */
public class PowerControlPresenter implements IPowerControlPresenter {

    /* renamed from: a, reason: collision with root package name */
    private DevicePolicyManager f14114a;

    /* renamed from: b, reason: collision with root package name */
    private PackageManager f14115b;

    /* renamed from: c, reason: collision with root package name */
    private ActivityManager f14116c;

    /* renamed from: f, reason: collision with root package name */
    private int f14119f;

    /* renamed from: h, reason: collision with root package name */
    private String f14121h;

    /* renamed from: i, reason: collision with root package name */
    private d f14122i;

    /* renamed from: j, reason: collision with root package name */
    private c f14123j;

    /* renamed from: k, reason: collision with root package name */
    private IDeviceIdleController f14124k;

    /* renamed from: n, reason: collision with root package name */
    private SharedPreferences f14127n;

    /* renamed from: o, reason: collision with root package name */
    SharedPreferences.Editor f14128o;

    /* renamed from: p, reason: collision with root package name */
    private boolean f14129p;

    /* renamed from: q, reason: collision with root package name */
    private boolean f14130q;

    /* renamed from: r, reason: collision with root package name */
    private boolean f14131r;

    /* renamed from: s, reason: collision with root package name */
    private boolean f14132s;

    /* renamed from: x, reason: collision with root package name */
    private IPowerControlViewUpdate f14137x;

    /* renamed from: d, reason: collision with root package name */
    private List<String> f14117d = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    private List<String> f14118e = new ArrayList();

    /* renamed from: l, reason: collision with root package name */
    private IRemoteGuardElfInterface f14125l = null;

    /* renamed from: m, reason: collision with root package name */
    private Object f14126m = new Object();

    /* renamed from: t, reason: collision with root package name */
    private boolean f14133t = false;

    /* renamed from: u, reason: collision with root package name */
    private boolean f14134u = false;

    /* renamed from: v, reason: collision with root package name */
    private boolean f14135v = false;

    /* renamed from: w, reason: collision with root package name */
    private boolean f14136w = false;

    /* renamed from: y, reason: collision with root package name */
    private final BroadcastReceiver f14138y = new a();

    /* renamed from: z, reason: collision with root package name */
    ServiceConnection f14139z = new b();

    /* renamed from: g, reason: collision with root package name */
    private Context f14120g = GuardElfContext.e().c();

    /* compiled from: PowerControlPresenter.java */
    /* renamed from: k8.f$a */
    /* loaded from: classes2.dex */
    class a extends BroadcastReceiver {
        a() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            LocalLog.l("PowerControlPresenter", "checkKillProcessesReceiver getResultCode=" + getResultCode());
            if (PowerControlPresenter.this.f14137x != null) {
                PowerControlPresenter.this.f14137x.a(getResultCode() != 0);
            }
        }
    }

    /* compiled from: PowerControlPresenter.java */
    /* renamed from: k8.f$b */
    /* loaded from: classes2.dex */
    class b implements ServiceConnection {
        b() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LocalLog.a("PowerControlPresenter", "RemoteGuardElf connected!");
            PowerControlPresenter.this.f14125l = IRemoteGuardElfInterface.a.z(iBinder);
            synchronized (PowerControlPresenter.this.f14126m) {
                PowerControlPresenter.this.f14126m.notifyAll();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            LocalLog.a("PowerControlPresenter", "RemoteGuardElf disconnected!");
            PowerControlPresenter.this.f14125l = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: PowerControlPresenter.java */
    /* renamed from: k8.f$c */
    /* loaded from: classes2.dex */
    public class c extends Handler {
        private c() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1001) {
                return;
            }
            Bundle data = message.getData();
            if (data == null) {
                LocalLog.l("PowerControlPresenter", "MSG_MAIN_UPDATE_FORCESTOP_BUTTON: data is null");
                return;
            }
            boolean z10 = data.getBoolean("enable");
            PowerControlPresenter.this.f14137x.a(z10);
            LocalLog.l("PowerControlPresenter", "MSG_MAIN_UPDATE_FORCESTOP_BUTTON: enable=" + z10);
        }

        /* synthetic */ c(PowerControlPresenter powerControlPresenter, a aVar) {
            this();
        }
    }

    /* compiled from: PowerControlPresenter.java */
    /* renamed from: k8.f$d */
    /* loaded from: classes2.dex */
    private class d extends Handler {
        public d(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            boolean z10;
            int i10 = message.what;
            if (1005 == i10) {
                if (!PowerControlPresenter.this.f14117d.contains(PowerControlPresenter.this.f14121h)) {
                    PowerControlPresenter.this.f14117d.add(PowerControlPresenter.this.f14121h);
                }
                if (PowerControlPresenter.this.f14118e.contains(PowerControlPresenter.this.f14121h)) {
                    PowerControlPresenter.this.f14118e.remove(PowerControlPresenter.this.f14121h);
                }
                if (!PowerUtils.h(PowerControlPresenter.this.f14124k, PowerControlPresenter.this.f14121h)) {
                    PowerUtils.a(PowerControlPresenter.this.f14124k, PowerControlPresenter.this.f14121h, PowerControlPresenter.this.f14120g);
                }
                PowerControlPresenter.this.y(1);
                f6.f.N1(PowerControlPresenter.this.f14117d, PowerControlPresenter.this.f14118e, PowerControlPresenter.this.f14120g);
                PowerUtils.k(PowerControlPresenter.this.f14121h, 0, PowerControlPresenter.this.f14120g);
                PowerUtils.p(PowerControlPresenter.this.f14117d);
                AppInfoManager.m(PowerControlPresenter.this.f14120g).x(PowerControlPresenter.this.f14121h, 2, false);
                LocalLog.l("PowerControlPresenter", "ALLOW BACKGROUND RUN. pkg=" + PowerControlPresenter.this.f14121h);
                return;
            }
            if (1006 == i10) {
                if (!PowerControlPresenter.this.f14118e.contains(PowerControlPresenter.this.f14121h)) {
                    PowerControlPresenter.this.f14118e.add(PowerControlPresenter.this.f14121h);
                }
                if (PowerControlPresenter.this.f14117d.contains(PowerControlPresenter.this.f14121h)) {
                    PowerControlPresenter.this.f14117d.remove(PowerControlPresenter.this.f14121h);
                }
                if (PowerUtils.h(PowerControlPresenter.this.f14124k, PowerControlPresenter.this.f14121h)) {
                    PowerUtils.j(PowerControlPresenter.this.f14124k, PowerControlPresenter.this.f14121h, PowerControlPresenter.this.f14120g);
                }
                PowerControlPresenter.this.y(2);
                f6.f.N1(PowerControlPresenter.this.f14117d, PowerControlPresenter.this.f14118e, PowerControlPresenter.this.f14120g);
                PowerUtils.k(PowerControlPresenter.this.f14121h, 1, PowerControlPresenter.this.f14120g);
                PowerUtils.p(PowerControlPresenter.this.f14117d);
                LocalLog.l("PowerControlPresenter", "PROHIBIT BACKGROUND RUN. pkg=" + PowerControlPresenter.this.f14121h);
                AppInfoManager.m(PowerControlPresenter.this.f14120g).x(PowerControlPresenter.this.f14121h, 1, false);
                return;
            }
            if (1007 == i10) {
                if (PowerControlPresenter.this.f14117d.contains(PowerControlPresenter.this.f14121h)) {
                    PowerControlPresenter.this.f14117d.remove(PowerControlPresenter.this.f14121h);
                }
                if (PowerControlPresenter.this.f14118e.contains(PowerControlPresenter.this.f14121h)) {
                    PowerControlPresenter.this.f14118e.remove(PowerControlPresenter.this.f14121h);
                }
                f6.f.M1(PowerControlPresenter.this.f14124k, PowerControlPresenter.this.f14121h, PowerControlPresenter.this.f14120g);
                PowerControlPresenter.this.y(3);
                f6.f.N1(PowerControlPresenter.this.f14117d, PowerControlPresenter.this.f14118e, PowerControlPresenter.this.f14120g);
                PowerUtils.k(PowerControlPresenter.this.f14121h, 0, PowerControlPresenter.this.f14120g);
                PowerUtils.p(PowerControlPresenter.this.f14117d);
                LocalLog.l("PowerControlPresenter", "INTELLIGENT LIMIT BACKGROUND RUN. pkg=" + PowerControlPresenter.this.f14121h);
                AppInfoManager.m(PowerControlPresenter.this.f14120g).x(PowerControlPresenter.this.f14121h, 0, false);
                return;
            }
            if (1003 == i10) {
                z10 = message.arg1 == 1;
                if (z10) {
                    PowerUtils.m(PowerControlPresenter.this.f14121h, PowerControlPresenter.this.f14120g);
                }
                PowerUtils.o(PowerControlPresenter.this.f14121h, PowerControlPresenter.this.f14120g, z10);
                LocalLog.l("PowerControlPresenter", "AUTO CHANGE: pkg=" + PowerControlPresenter.this.f14121h + ", isAllowed=" + z10);
                return;
            }
            if (1004 == i10) {
                z10 = message.arg1 == 1;
                if (z10) {
                    PowerUtils.m(PowerControlPresenter.this.f14121h, PowerControlPresenter.this.f14120g);
                }
                PowerUtils.n(PowerControlPresenter.this.f14121h, PowerControlPresenter.this.f14120g, z10);
                LocalLog.l("PowerControlPresenter", "ASSOCIATE CHANGE: pkg=" + PowerControlPresenter.this.f14121h + ", isAllowed=" + z10);
                return;
            }
            if (1008 == i10) {
                PowerControlPresenter.this.f14116c.forceStopPackageAsUser(PowerControlPresenter.this.f14121h, UserHandle.myUserId());
                if (y5.b.I()) {
                    PowerControlPresenter.this.f14116c.forceStopPackageAsUser(PowerControlPresenter.this.f14121h, 999);
                }
                PowerControlPresenter.this.w();
                PowerControlPresenter.this.z();
                return;
            }
            if (1009 == i10) {
                PowerControlPresenter.this.w();
                return;
            }
            if (1002 == i10) {
                synchronized (PowerControlPresenter.this.f14126m) {
                    PowerControlPresenter powerControlPresenter = PowerControlPresenter.this;
                    f6.f.b(powerControlPresenter.f14139z, powerControlPresenter.f14120g);
                    try {
                        PowerControlPresenter.this.f14126m.wait(100L);
                    } catch (Exception e10) {
                        LocalLog.b("PowerControlPresenter", "MSG_BIND_REMOTE_GUARDELF WaitRemoteGuard wait Exception " + e10);
                    }
                }
            }
        }
    }

    public PowerControlPresenter(String str, IPowerControlViewUpdate iPowerControlViewUpdate) {
        this.f14114a = null;
        this.f14115b = null;
        this.f14116c = null;
        this.f14122i = null;
        this.f14124k = null;
        this.f14127n = null;
        this.f14128o = null;
        this.f14129p = false;
        this.f14130q = false;
        this.f14131r = false;
        this.f14132s = false;
        this.f14121h = str;
        this.f14137x = iPowerControlViewUpdate;
        HandlerThread handlerThread = new HandlerThread("PowerControlThread");
        handlerThread.start();
        this.f14122i = new d(handlerThread.getLooper());
        this.f14123j = new c(this, null);
        this.f14114a = (DevicePolicyManager) this.f14120g.getSystemService("device_policy");
        this.f14115b = this.f14120g.getPackageManager();
        this.f14116c = (ActivityManager) this.f14120g.getSystemService("activity");
        this.f14124k = IDeviceIdleController.Stub.asInterface(ServiceManager.getService("deviceidle"));
        this.f14122i.sendEmptyMessage(DataTypeConstants.APP_LOG);
        SharedPreferences sharedPreferences = this.f14120g.getSharedPreferences("power_settings_is_changed", 0);
        this.f14127n = sharedPreferences;
        this.f14128o = sharedPreferences.edit();
        this.f14129p = this.f14127n.getBoolean(this.f14121h + "foreground", false);
        this.f14130q = this.f14127n.getBoolean(this.f14121h + "background", false);
        this.f14131r = this.f14127n.getBoolean(this.f14121h + "associate", false);
        this.f14132s = this.f14127n.getBoolean(this.f14121h + "autoStart", false);
    }

    private void A(int i10, Bundle bundle) {
        Message obtainMessage = this.f14123j.obtainMessage();
        obtainMessage.what = i10;
        if (bundle != null) {
            obtainMessage.setData(bundle);
        }
        this.f14123j.sendMessage(obtainMessage);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void w() {
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = this.f14115b.getApplicationInfo(this.f14121h, 0);
        } catch (PackageManager.NameNotFoundException e10) {
            LocalLog.b("PowerControlPresenter", "PackageNameNotFoundException e=" + e10);
            applicationInfo = null;
        }
        if (applicationInfo == null) {
            return;
        }
        if (this.f14114a.packageHasActiveAdmins(this.f14121h)) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("enable", false);
            A(DataTypeConstants.USER_ACTION, bundle);
            LocalLog.l("PowerControlPresenter", "checkForceStop: HasActiveAdmins. packageName=" + applicationInfo.packageName);
            return;
        }
        if ((applicationInfo.flags & 2097152) == 0) {
            Bundle bundle2 = new Bundle();
            bundle2.putBoolean("enable", true);
            A(DataTypeConstants.USER_ACTION, bundle2);
            LocalLog.l("PowerControlPresenter", "checkForceStop: no FLAG_STOPED. packageName=" + applicationInfo.packageName);
            return;
        }
        UserHandle userHandle = UserHandle.myUserId() == 999 ? new UserHandle(999) : UserHandle.CURRENT;
        Intent intent = new Intent("android.intent.action.QUERY_PACKAGE_RESTART", Uri.fromParts("package", applicationInfo.packageName, null));
        intent.putExtra("android.intent.extra.PACKAGES", new String[]{applicationInfo.packageName});
        intent.putExtra("android.intent.extra.UID", applicationInfo.uid);
        intent.putExtra("android.intent.extra.user_handle", UserHandle.getUserId(applicationInfo.uid));
        LocalLog.l("PowerControlPresenter", "checkForceStop uid=" + applicationInfo.uid + " packageName=" + applicationInfo.packageName);
        this.f14120g.sendOrderedBroadcastAsUser(intent, userHandle, null, this.f14138y, null, 0, null, null);
    }

    private ArrayList<ActivityManager.RecentTaskInfo> x() {
        return (ArrayList) this.f14116c.getRecentTasks(21, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void y(int i10) {
        if (this.f14125l == null) {
            LocalLog.a("PowerControlPresenter", "powerProtectPolicyChange: RemoteGuardElf is null. wait...");
            synchronized (this.f14126m) {
                f6.f.b(this.f14139z, this.f14120g);
                try {
                    this.f14126m.wait(100L);
                } catch (Exception e10) {
                    LocalLog.b("PowerControlPresenter", "MSG_POLICY_CHANGE: wait Exception " + e10);
                }
            }
        }
        IRemoteGuardElfInterface iRemoteGuardElfInterface = this.f14125l;
        if (iRemoteGuardElfInterface == null) {
            LocalLog.a("PowerControlPresenter", "powerProtectPolicyChange: RemoteGuardElf is still null.");
            return;
        }
        try {
            iRemoteGuardElfInterface.e(this.f14121h, i10);
        } catch (Exception e11) {
            LocalLog.b("PowerControlPresenter", "MSG_POLICY_CHANGE: Exception " + e11);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void z() {
        ArrayList<ActivityManager.RecentTaskInfo> x10 = x();
        IActivityManager service = ActivityManager.getService();
        Iterator<ActivityManager.RecentTaskInfo> it = x10.iterator();
        while (it.hasNext()) {
            ActivityManager.RecentTaskInfo next = it.next();
            if (this.f14121h.equals(next.baseIntent.getComponent().getPackageName()) && (next.userId == UserHandle.myUserId() || (y5.b.I() && next.userId == 999))) {
                try {
                    service.removeTask(next.persistentId);
                    LocalLog.a("PowerControlPresenter", "removeTask taskInfo.userId=" + next.userId);
                } catch (RemoteException unused) {
                    LocalLog.l("PowerControlPresenter", "fail remove task " + next.persistentId);
                }
            }
        }
    }

    @Override // h8.IPowerControlPresenter
    public void a() {
        this.f14129p = !this.f14129p;
        this.f14133t = !this.f14133t;
        this.f14128o.putBoolean(this.f14121h + "foreground", this.f14129p);
        this.f14128o.apply();
        this.f14122i.sendEmptyMessage(DataTypeConstants.COMMON);
    }

    @Override // h8.IPowerControlPresenter
    public int b() {
        f6.f.l(this.f14117d, this.f14118e, this.f14120g);
        if (this.f14117d.contains(this.f14121h)) {
            this.f14119f = 1;
        } else if (this.f14118e.contains(this.f14121h)) {
            this.f14119f = 2;
        } else {
            this.f14119f = 3;
        }
        return this.f14119f;
    }

    @Override // h8.IPowerControlPresenter
    public void c() {
        this.f14122i.sendEmptyMessage(DataTypeConstants.DEBUG_TYPE);
    }

    @Override // h8.IPowerControlPresenter
    public void d(boolean z10) {
        this.f14132s = !this.f14132s;
        this.f14136w = !this.f14136w;
        this.f14128o.putBoolean(this.f14121h + "autoStart", this.f14132s);
        this.f14128o.apply();
        Message obtainMessage = this.f14122i.obtainMessage();
        obtainMessage.what = DataTypeConstants.PAGE_VISIT;
        obtainMessage.arg1 = z10 ? 1 : 2;
        this.f14122i.sendMessage(obtainMessage);
    }

    @Override // h8.IPowerControlPresenter
    public void e(boolean z10) {
        if (z10) {
            this.f14129p = !this.f14129p;
            this.f14133t = !this.f14133t;
            this.f14128o.putBoolean(this.f14121h + "foreground", this.f14129p);
            this.f14128o.apply();
        } else {
            this.f14130q = !this.f14130q;
            this.f14134u = !this.f14134u;
            this.f14128o.putBoolean(this.f14121h + "background", this.f14130q);
            this.f14128o.apply();
        }
        this.f14122i.sendEmptyMessage(DataTypeConstants.DYNAMIC_EVENT_TYPE);
    }

    @Override // h8.IPowerControlPresenter
    public void f(boolean z10) {
        this.f14131r = !this.f14131r;
        this.f14135v = !this.f14135v;
        this.f14128o.putBoolean(this.f14121h + "associate", this.f14131r);
        this.f14128o.apply();
        Message obtainMessage = this.f14122i.obtainMessage();
        obtainMessage.what = DataTypeConstants.EXCEPTION;
        obtainMessage.arg1 = z10 ? 1 : 2;
        this.f14122i.sendMessage(obtainMessage);
    }

    @Override // h8.IPowerControlPresenter
    public void g() {
        LocalLog.a("PowerControlPresenter", "forceStopPackage");
        this.f14122i.sendEmptyMessage(DataTypeConstants.STATIC_EVENT_TYPE);
    }

    @Override // h8.IPowerControlPresenter
    public void h() {
        boolean z10 = this.f14133t || this.f14134u || this.f14135v || this.f14136w;
        this.f14128o.putBoolean("power_settings_is_changed_this_time", z10);
        this.f14128o.apply();
        LocalLog.a("PowerControlPresenter", "onPause, isChanged = " + z10);
    }

    @Override // h8.IPowerControlPresenter
    public void i() {
        this.f14130q = !this.f14130q;
        this.f14134u = !this.f14134u;
        this.f14128o.putBoolean(this.f14121h + "background", this.f14130q);
        this.f14128o.putBoolean(this.f14121h + "foreground", false);
        this.f14128o.apply();
        this.f14122i.sendEmptyMessage(DataTypeConstants.SPECIAL_APP_START);
    }

    @Override // h8.IPowerControlPresenter
    public void j() {
        this.f14137x = null;
        synchronized (this.f14126m) {
            f6.f.z3(this.f14139z, this.f14120g);
        }
    }

    @Override // h8.IPowerControlPresenter
    public void onCreate(Bundle bundle) {
    }
}
