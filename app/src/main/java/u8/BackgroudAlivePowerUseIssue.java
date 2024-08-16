package u8;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.IDeviceIdleController;
import android.os.ServiceManager;
import android.os.SystemClock;
import b6.LocalLog;
import com.android.internal.app.IBatteryStats;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import e6.SmartModeSharepref;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import u4.IRemoteGuardElfInterface;

/* compiled from: BackgroudAlivePowerUseIssue.java */
/* renamed from: u8.g, reason: use source file name */
/* loaded from: classes2.dex */
public class BackgroudAlivePowerUseIssue extends BasicPowerIssue {

    /* renamed from: m, reason: collision with root package name */
    private IRemoteGuardElfInterface f18904m;

    /* renamed from: n, reason: collision with root package name */
    private Object f18905n;

    /* renamed from: o, reason: collision with root package name */
    private ArrayList<String> f18906o;

    /* renamed from: p, reason: collision with root package name */
    private ArrayList<String> f18907p;

    /* renamed from: q, reason: collision with root package name */
    private Object f18908q;

    /* renamed from: r, reason: collision with root package name */
    ServiceConnection f18909r;

    /* compiled from: BackgroudAlivePowerUseIssue.java */
    /* renamed from: u8.g$a */
    /* loaded from: classes2.dex */
    class a implements ServiceConnection {
        a() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LocalLog.a("BackgroudAlivePowerUseIssue", "RemoteGuardElf connected!");
            BackgroudAlivePowerUseIssue.this.f18904m = IRemoteGuardElfInterface.a.z(iBinder);
            synchronized (BackgroudAlivePowerUseIssue.this.f18905n) {
                BackgroudAlivePowerUseIssue.this.f18905n.notifyAll();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            LocalLog.a("BackgroudAlivePowerUseIssue", "RemoteGuardElf disconnected!");
            BackgroudAlivePowerUseIssue.this.f18904m = null;
            BackgroudAlivePowerUseIssue backgroudAlivePowerUseIssue = BackgroudAlivePowerUseIssue.this;
            f6.f.b(backgroudAlivePowerUseIssue.f18909r, backgroudAlivePowerUseIssue.f18919i);
        }
    }

    public BackgroudAlivePowerUseIssue(Context context, String str, int i10, boolean z10, boolean z11) {
        super(context, str, i10, z10, z11);
        this.f18904m = null;
        this.f18905n = new Object();
        this.f18906o = new ArrayList<>();
        this.f18907p = new ArrayList<>();
        this.f18908q = new Object();
        this.f18909r = new a();
    }

    private void t(String str, int i10) {
        if (this.f18904m == null) {
            LocalLog.a("BackgroudAlivePowerUseIssue", "MSG_POLICY_CHANGE: RemoteGuardElf is null. wait...");
            synchronized (this.f18905n) {
                f6.f.b(this.f18909r, this.f18919i);
                try {
                    this.f18905n.wait(2000L);
                } catch (Exception e10) {
                    LocalLog.b("BackgroudAlivePowerUseIssue", "MSG_POLICY_CHANGE: wait Exception " + e10);
                }
            }
        }
        try {
            IRemoteGuardElfInterface iRemoteGuardElfInterface = this.f18904m;
            if (iRemoteGuardElfInterface != null) {
                iRemoteGuardElfInterface.e(str, i10);
            }
        } catch (Exception e11) {
            LocalLog.b("BackgroudAlivePowerUseIssue", "MSG_POLICY_CHANGE: Exception " + e11);
        }
    }

    @Override // u8.BasicPowerIssue
    public double a(IBatteryStats iBatteryStats, List<ActivityManager.RunningAppProcessInfo> list, List<ActivityManager.RunningAppProcessInfo> list2, double d10, long j10) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        f6.f.l(arrayList, arrayList2, this.f18919i.getApplicationContext());
        synchronized (this.f18908q) {
            this.f18906o.clear();
            this.f18907p.clear();
            this.f18906o.addAll(arrayList);
            this.f18907p.addAll(arrayList2);
            LocalLog.a("BackgroudAlivePowerUseIssue", "mListAllow size " + this.f18906o.size());
        }
        if (arrayList.size() <= 0) {
            return UserProfileInfo.Constant.NA_LAT_LON;
        }
        if (list.size() == 0 && list2.size() == 0) {
            return UserProfileInfo.Constant.NA_LAT_LON;
        }
        int i10 = 0;
        Iterator<ActivityManager.RunningAppProcessInfo> it = list.iterator();
        while (it.hasNext()) {
            if (arrayList.contains(it.next().processName)) {
                i10++;
            }
        }
        double size = (((arrayList.size() - i10) / ((list.size() * 20) + list2.size())) * d10) / 3.0d;
        LocalLog.a("BackgroudAlivePowerUseIssue", "key = " + e() + ", power = " + size);
        return size;
    }

    @Override // u8.BasicPowerIssue
    public boolean c() {
        if (g() != 1) {
            return false;
        }
        this.f18913c = SystemClock.elapsedRealtime();
        this.f18912b = System.currentTimeMillis();
        l(3);
        IDeviceIdleController asInterface = IDeviceIdleController.Stub.asInterface(ServiceManager.getService("deviceidle"));
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        synchronized (this.f18908q) {
            arrayList.addAll(this.f18906o);
            arrayList2.addAll(this.f18907p);
            this.f18906o.clear();
        }
        SharedPreferences sharedPreferences = this.f18919i.getSharedPreferences("power_settings_is_changed", 0);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putBoolean(str + "background", false);
            edit.apply();
            f6.f.M1(asInterface, str, this.f18919i.getApplicationContext());
            t(str, 3);
        }
        arrayList.clear();
        f6.f.N1(arrayList, arrayList2, this.f18919i.getApplicationContext());
        new ArrayList();
        new ArrayList();
        f6.f.l(arrayList, arrayList2, this.f18919i.getApplicationContext());
        SmartModeSharepref.e(this.f18919i, e(), this.f18912b);
        return true;
    }

    @Override // u8.BasicPowerIssue
    public void j(IBatteryStats iBatteryStats, List<ActivityManager.RunningAppProcessInfo> list, List<ActivityManager.RunningAppProcessInfo> list2, double d10, long j10) {
        super.j(iBatteryStats, list, list2, d10, j10);
        if (this.f18917g > 0) {
            this.f18918h = 1;
        } else {
            this.f18918h = 0;
        }
        this.f18917g = 0L;
    }

    @Override // u8.BasicPowerIssue
    public boolean n() {
        return this.f18914d && this.f18915e;
    }
}
