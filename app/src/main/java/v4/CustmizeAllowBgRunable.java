package v4;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.IDeviceIdleController;
import android.os.ServiceManager;
import b6.LocalLog;
import b9.PowerUtils;
import g7.AppInfoManager;
import java.util.ArrayList;
import java.util.List;
import u4.IRemoteGuardElfInterface;
import w5.OplusBatteryConstants;
import y5.AppFeature;
import z5.GuardElfDataManager;
import z5.LocalFileUtil;

/* compiled from: CustmizeAllowBgRunable.java */
/* renamed from: v4.b, reason: use source file name */
/* loaded from: classes.dex */
public class CustmizeAllowBgRunable {

    /* renamed from: h, reason: collision with root package name */
    private static final String f19062h = "v4.b";

    /* renamed from: i, reason: collision with root package name */
    private static volatile CustmizeAllowBgRunable f19063i;

    /* renamed from: e, reason: collision with root package name */
    private Context f19068e;

    /* renamed from: f, reason: collision with root package name */
    private Handler f19069f;

    /* renamed from: a, reason: collision with root package name */
    private List<String> f19064a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    private List<String> f19065b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    private IRemoteGuardElfInterface f19066c = null;

    /* renamed from: d, reason: collision with root package name */
    private Object f19067d = new Object();

    /* renamed from: g, reason: collision with root package name */
    ServiceConnection f19070g = new d();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: CustmizeAllowBgRunable.java */
    /* renamed from: v4.b$a */
    /* loaded from: classes.dex */
    public class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ List f19071e;

        a(List list) {
            this.f19071e = list;
        }

        @Override // java.lang.Runnable
        public void run() {
            boolean z10;
            f6.f.l(CustmizeAllowBgRunable.this.f19064a, CustmizeAllowBgRunable.this.f19065b, CustmizeAllowBgRunable.this.f19068e);
            IDeviceIdleController asInterface = IDeviceIdleController.Stub.asInterface(ServiceManager.getService("deviceidle"));
            for (String str : OplusBatteryConstants.f19362n) {
                CustmizeAllowBgRunable.this.f19064a.remove(str);
                CustmizeAllowBgRunable.this.f19065b.remove(str);
                f6.f.M1(asInterface, str, CustmizeAllowBgRunable.this.f19068e);
                CustmizeAllowBgRunable.this.m(3, str);
                f6.f.N1(CustmizeAllowBgRunable.this.f19064a, CustmizeAllowBgRunable.this.f19065b, CustmizeAllowBgRunable.this.f19068e);
                PowerUtils.k(str, 0, CustmizeAllowBgRunable.this.f19068e);
                PowerUtils.p(CustmizeAllowBgRunable.this.f19064a);
                LocalLog.l(CustmizeAllowBgRunable.f19062h, "INTELLIGENT LIMIT BACKGROUND RUN. pkg=" + str);
                AppInfoManager.m(CustmizeAllowBgRunable.this.f19068e).x(str, 0, false);
            }
            if (this.f19071e == null) {
                return;
            }
            for (int i10 = 0; i10 < this.f19071e.size(); i10++) {
                String str2 = (String) this.f19071e.get(i10);
                LocalLog.l(CustmizeAllowBgRunable.f19062h, "pkg=" + str2 + ", isAllow=" + CustmizeAllowBgRunable.this.f19064a.contains(str2));
                if (CustmizeAllowBgRunable.this.f19064a.contains(str2)) {
                    z10 = false;
                } else {
                    CustmizeAllowBgRunable.this.f19064a.add(str2);
                    z10 = true;
                }
                if (CustmizeAllowBgRunable.this.f19065b.contains(str2)) {
                    CustmizeAllowBgRunable.this.f19065b.remove(str2);
                    z10 = true;
                }
                if (!PowerUtils.h(asInterface, str2)) {
                    PowerUtils.a(asInterface, str2, CustmizeAllowBgRunable.this.f19068e);
                }
                CustmizeAllowBgRunable.this.m(1, str2);
                PowerUtils.k(str2, 0, CustmizeAllowBgRunable.this.f19068e);
                PowerUtils.p(CustmizeAllowBgRunable.this.f19064a);
                if (z10) {
                    f6.f.N1(CustmizeAllowBgRunable.this.f19064a, CustmizeAllowBgRunable.this.f19065b, CustmizeAllowBgRunable.this.f19068e);
                }
            }
        }
    }

    /* compiled from: CustmizeAllowBgRunable.java */
    /* renamed from: v4.b$b */
    /* loaded from: classes.dex */
    class b implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ List f19073e;

        b(List list) {
            this.f19073e = list;
        }

        @Override // java.lang.Runnable
        public void run() {
            IDeviceIdleController asInterface = IDeviceIdleController.Stub.asInterface(ServiceManager.getService("deviceidle"));
            for (String str : this.f19073e) {
                if (!PowerUtils.h(asInterface, str)) {
                    PowerUtils.a(asInterface, str, CustmizeAllowBgRunable.this.f19068e);
                }
                CustmizeAllowBgRunable.this.m(1, str);
                PowerUtils.k(str, 0, CustmizeAllowBgRunable.this.f19068e);
                PowerUtils.p(CustmizeAllowBgRunable.this.f19064a);
                AppInfoManager.m(CustmizeAllowBgRunable.this.f19068e).x(str, 2, true);
            }
            GuardElfDataManager.d(CustmizeAllowBgRunable.this.f19068e).b(this.f19073e, "power_control_white_list");
        }
    }

    /* compiled from: CustmizeAllowBgRunable.java */
    /* renamed from: v4.b$c */
    /* loaded from: classes.dex */
    class c implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ List f19075e;

        c(List list) {
            this.f19075e = list;
        }

        @Override // java.lang.Runnable
        public void run() {
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            f6.f.l(arrayList, arrayList2, CustmizeAllowBgRunable.this.f19068e);
            IDeviceIdleController asInterface = IDeviceIdleController.Stub.asInterface(ServiceManager.getService("deviceidle"));
            for (String str : this.f19075e) {
                if (arrayList2.contains(str)) {
                    if (PowerUtils.h(asInterface, str)) {
                        PowerUtils.j(asInterface, str, CustmizeAllowBgRunable.this.f19068e);
                    }
                    CustmizeAllowBgRunable.this.m(2, str);
                    f6.f.N1(CustmizeAllowBgRunable.this.f19064a, CustmizeAllowBgRunable.this.f19065b, CustmizeAllowBgRunable.this.f19068e);
                    PowerUtils.k(str, 1, CustmizeAllowBgRunable.this.f19068e);
                    PowerUtils.p(CustmizeAllowBgRunable.this.f19064a);
                    AppInfoManager.m(CustmizeAllowBgRunable.this.f19068e).x(str, 1, true);
                } else if (!arrayList.contains(str)) {
                    f6.f.M1(asInterface, str, CustmizeAllowBgRunable.this.f19068e);
                    CustmizeAllowBgRunable.this.m(3, str);
                    f6.f.N1(CustmizeAllowBgRunable.this.f19064a, CustmizeAllowBgRunable.this.f19065b, CustmizeAllowBgRunable.this.f19068e);
                    PowerUtils.k(str, 0, CustmizeAllowBgRunable.this.f19068e);
                    PowerUtils.p(CustmizeAllowBgRunable.this.f19064a);
                    AppInfoManager.m(CustmizeAllowBgRunable.this.f19068e).x(str, 0, true);
                }
            }
            GuardElfDataManager.d(CustmizeAllowBgRunable.this.f19068e).h(this.f19075e, "power_control_white_list");
        }
    }

    /* compiled from: CustmizeAllowBgRunable.java */
    /* renamed from: v4.b$d */
    /* loaded from: classes.dex */
    class d implements ServiceConnection {
        d() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LocalLog.a(CustmizeAllowBgRunable.f19062h, "RemoteGuardElf connected!");
            CustmizeAllowBgRunable.this.f19066c = IRemoteGuardElfInterface.a.z(iBinder);
            synchronized (CustmizeAllowBgRunable.this.f19067d) {
                CustmizeAllowBgRunable.this.f19067d.notifyAll();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            LocalLog.a(CustmizeAllowBgRunable.f19062h, "RemoteGuardElf disconnected!");
            CustmizeAllowBgRunable.this.f19066c = null;
        }
    }

    public CustmizeAllowBgRunable(Context context) {
        this.f19068e = context;
        l();
    }

    public static CustmizeAllowBgRunable i(Context context) {
        if (f19063i == null) {
            synchronized (CustmizeAllowBgRunable.class) {
                if (f19063i == null) {
                    f19063i = new CustmizeAllowBgRunable(context);
                }
            }
        }
        return f19063i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void m(int i10, String str) {
        if (this.f19066c == null) {
            LocalLog.a(f19062h, "powerProtectPolicyChange: RemoteGuardElf is null. wait...");
            synchronized (this.f19067d) {
                f6.f.b(this.f19070g, this.f19068e);
                try {
                    this.f19067d.wait(300L);
                } catch (Exception e10) {
                    LocalLog.b(f19062h, "MSG_POLICY_CHANGE: wait Exception " + e10);
                }
            }
        }
        IRemoteGuardElfInterface iRemoteGuardElfInterface = this.f19066c;
        if (iRemoteGuardElfInterface == null) {
            LocalLog.a(f19062h, "powerProtectPolicyChange: RemoteGuardElf is still null.");
            return;
        }
        try {
            iRemoteGuardElfInterface.e(str, i10);
        } catch (Exception e11) {
            LocalLog.b(f19062h, "MSG_POLICY_CHANGE: Exception " + e11);
        }
    }

    public void h(List<String> list) {
        this.f19069f.post(new b(list));
    }

    public List<String> j() {
        return LocalFileUtil.c().k("battery", "power_control_white_list", this.f19068e);
    }

    public void k() {
        List<String> a10 = AppFeature.a();
        LocalLog.l(f19062h, "custAllowBgRunable=" + a10);
        this.f19069f.postDelayed(new a(a10), 3000L);
    }

    public void l() {
        HandlerThread handlerThread = new HandlerThread("customize_handler");
        handlerThread.start();
        this.f19069f = new Handler(handlerThread.getLooper());
        f6.f.b(this.f19070g, this.f19068e);
        LocalLog.l(f19062h, "custAllowBgRunable:init");
    }

    public void n(List<String> list) {
        this.f19069f.post(new c(list));
    }
}
