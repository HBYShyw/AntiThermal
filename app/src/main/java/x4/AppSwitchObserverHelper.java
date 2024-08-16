package x4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.INetworkStatsService;
import android.net.INetworkStatsSession;
import android.net.NetworkStatsHistory;
import android.net.NetworkTemplate;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.util.Log;
import b6.LocalLog;
import com.android.internal.os.PowerProfile;
import com.android.internal.os.UidSipper;
import com.oplus.simplepowermonitor.alarm.NetworkStatusReceiver;
import f6.CommonUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import o9.HighPowerHelper;
import p9.PowerMonitor;
import q9.AppStats;
import q9.NetWork;
import r9.SimplePowerMonitorUtils;

/* compiled from: AppSwitchObserverHelper.java */
/* renamed from: x4.f, reason: use source file name */
/* loaded from: classes.dex */
public class AppSwitchObserverHelper {

    /* renamed from: q, reason: collision with root package name */
    @SuppressLint({"StaticFieldLeak"})
    private static volatile AppSwitchObserverHelper f19524q;

    /* renamed from: a, reason: collision with root package name */
    private Context f19525a;

    /* renamed from: b, reason: collision with root package name */
    private Class<?> f19526b;

    /* renamed from: c, reason: collision with root package name */
    private Method f19527c;

    /* renamed from: d, reason: collision with root package name */
    private UidSipper[] f19528d;

    /* renamed from: e, reason: collision with root package name */
    private PowerProfile f19529e;

    /* renamed from: f, reason: collision with root package name */
    private HashMap<Integer, Sensor> f19530f = new HashMap<>();

    /* renamed from: g, reason: collision with root package name */
    private NetWork f19531g;

    /* renamed from: h, reason: collision with root package name */
    private NetWork f19532h;

    /* renamed from: i, reason: collision with root package name */
    private INetworkStatsSession f19533i;

    /* renamed from: j, reason: collision with root package name */
    private INetworkStatsService f19534j;

    /* renamed from: k, reason: collision with root package name */
    private NetworkTemplate f19535k;

    /* renamed from: l, reason: collision with root package name */
    private NetworkTemplate f19536l;

    /* renamed from: m, reason: collision with root package name */
    private NetworkStatsHistory.Entry f19537m;

    /* renamed from: n, reason: collision with root package name */
    private NetworkStatsHistory.Entry f19538n;

    /* renamed from: o, reason: collision with root package name */
    private Handler f19539o;

    /* renamed from: p, reason: collision with root package name */
    private HandlerThread f19540p;

    /* compiled from: AppSwitchObserverHelper.java */
    /* renamed from: x4.f$a */
    /* loaded from: classes.dex */
    private class a extends Handler {
        public a(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 10) {
                return;
            }
            String str = (String) message.obj;
            Log.d("AppSwitchObserverHelper", "packageName: " + str);
            String[] split = str.split("\\,");
            if (split.length > 1) {
                AppSwitchObserverHelper.this.c(split[0], split[1]);
            } else if (LocalLog.f()) {
                LocalLog.a("AppSwitchObserverHelper", "ips.length <= 1");
            }
        }
    }

    private AppSwitchObserverHelper(final Context context) {
        this.f19525a = null;
        this.f19526b = null;
        this.f19527c = null;
        this.f19525a = context;
        try {
            Class<?> cls = Class.forName("com.android.internal.os.OplusBatteryStatsManager");
            this.f19526b = cls;
            Class<?> cls2 = Boolean.TYPE;
            this.f19527c = cls.getMethod("getUidSipper", int[].class, Long.TYPE, Integer.TYPE, cls2, cls2);
        } catch (Exception e10) {
            if (LocalLog.f()) {
                LocalLog.a("AppSwitchObserverHelper", "getUidSipper error = " + e10.toString());
            }
        }
        this.f19529e = new PowerProfile(this.f19525a);
        this.f19534j = INetworkStatsService.Stub.asInterface(ServiceManager.getService("netstats"));
        this.f19535k = NetworkTemplate.buildTemplateWifiWildcard();
        this.f19536l = NetworkTemplate.buildTemplateMobileWildcard();
        try {
            INetworkStatsSession openSession = this.f19534j.openSession();
            this.f19533i = openSession;
            this.f19531g = new NetWork(openSession, NetWork.a(this.f19535k, 30));
            this.f19532h = new NetWork(this.f19533i, NetWork.a(this.f19536l, 30));
            HandlerThread handlerThread = new HandlerThread("AppChangeAction");
            this.f19540p = handlerThread;
            handlerThread.start();
            a aVar = new a(this.f19540p.getLooper());
            this.f19539o = aVar;
            aVar.post(new Runnable() { // from class: x4.e
                @Override // java.lang.Runnable
                public final void run() {
                    AppSwitchObserverHelper.this.e(context);
                }
            });
        } catch (RemoteException e11) {
            throw new RuntimeException(e11);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:106:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x01ff  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x02a1  */
    /* JADX WARN: Type inference failed for: r14v2 */
    /* JADX WARN: Type inference failed for: r14v3 */
    /* JADX WARN: Type inference failed for: r14v4 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void c(String str, String str2) {
        String str3;
        String str4;
        int i10;
        int i11;
        long j10;
        boolean z10;
        int i12;
        int i13;
        boolean z11;
        boolean z12;
        int i14;
        int i15;
        UidSipper[] uidSipperArr;
        UidSipper[] uidSipperArr2;
        Method method;
        Object newInstance;
        Object[] objArr;
        Long valueOf;
        int intValue;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long elapsedRealtime2 = SystemClock.elapsedRealtime();
        int i16 = SimplePowerMonitorUtils.f17652c;
        if (LocalLog.f()) {
            LocalLog.a("AppSwitchObserverHelper", "currentUserId: " + i16);
        }
        if (i16 != 0) {
            str3 = str + "." + i16;
            str4 = str2 + "." + i16;
        } else {
            str3 = str;
            str4 = str2;
        }
        ConcurrentHashMap<String, Integer> e10 = SimplePowerMonitorUtils.e();
        boolean z13 = e10.containsKey(str3) || PowerMonitor.i().containsKey(str3);
        boolean z14 = e10.containsKey(str4) || PowerMonitor.i().containsKey(str4);
        if (LocalLog.f()) {
            LocalLog.a("AppSwitchObserverHelper", "highpower_ismonitor: enterPackageName: " + str3 + ";   isMonitor: " + z13);
            LocalLog.a("AppSwitchObserverHelper", "highpower_ismonitor: exitPackageName: " + str4 + ";   isMonitor: " + z14);
        }
        ArrayList arrayList = new ArrayList();
        if (z13) {
            if (!e10.containsKey(str3)) {
                i10 = PowerMonitor.i().get(str3).intValue();
            } else {
                i10 = e10.get(str3).intValue();
            }
            if (LocalLog.f()) {
                LocalLog.a("AppSwitchObserverHelper", "highpower_ismonitor: enterUid: " + i10 + ";   isMonitor: " + z13);
            }
            arrayList.add(Integer.valueOf(i10));
        } else {
            i10 = 0;
        }
        if (z14) {
            if (!e10.containsKey(str4)) {
                intValue = PowerMonitor.i().get(str4).intValue();
            } else {
                intValue = e10.get(str4).intValue();
            }
            if (LocalLog.f()) {
                LocalLog.a("AppSwitchObserverHelper", "highpower_ismonitor: exitUid: " + intValue + ";   isMonitor: " + z13);
            }
            arrayList.add(Integer.valueOf(intValue));
            i11 = intValue;
        } else {
            i11 = 0;
        }
        int size = arrayList.size();
        if (size > 0) {
            int[] iArr = new int[size];
            for (int i17 = 0; i17 < size; i17++) {
                iArr[i17] = ((Integer) arrayList.get(i17)).intValue();
            }
            try {
                long elapsedRealtime3 = SystemClock.elapsedRealtime();
                z10 = z14;
                try {
                    method = this.f19527c;
                    i12 = size;
                } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e11) {
                    e = e11;
                    j10 = elapsedRealtime;
                    i12 = size;
                    i13 = i11;
                    z11 = true;
                    e.printStackTrace();
                    if (z13) {
                    }
                    z12 = z10;
                    i14 = i12;
                    i15 = i13;
                    if (z12) {
                    }
                }
                try {
                    j10 = elapsedRealtime;
                    i13 = i11;
                } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e12) {
                    e = e12;
                    j10 = elapsedRealtime;
                    i13 = i11;
                    z11 = true;
                    e.printStackTrace();
                    if (z13) {
                    }
                    z12 = z10;
                    i14 = i12;
                    i15 = i13;
                    if (z12) {
                    }
                }
                try {
                    newInstance = this.f19526b.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                    objArr = new Object[5];
                    objArr[0] = iArr;
                    valueOf = Long.valueOf(elapsedRealtime2);
                    z11 = true;
                } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e13) {
                    e = e13;
                    z11 = true;
                    e.printStackTrace();
                    if (z13) {
                    }
                    z12 = z10;
                    i14 = i12;
                    i15 = i13;
                    if (z12) {
                    }
                }
                try {
                    objArr[1] = valueOf;
                    objArr[2] = 0;
                    objArr[3] = Boolean.TRUE;
                    objArr[4] = Boolean.FALSE;
                    this.f19528d = (UidSipper[]) method.invoke(newInstance, objArr);
                    long elapsedRealtime4 = SystemClock.elapsedRealtime();
                    if (LocalLog.f()) {
                        LocalLog.a("AppSwitchObserverHelper", "getUidSipper_time: " + (elapsedRealtime4 - elapsedRealtime3));
                    }
                } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e14) {
                    e = e14;
                    e.printStackTrace();
                    if (z13) {
                    }
                    z12 = z10;
                    i14 = i12;
                    i15 = i13;
                    if (z12) {
                        return;
                    } else {
                        return;
                    }
                }
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e15) {
                e = e15;
                j10 = elapsedRealtime;
                z10 = z14;
            }
        } else {
            j10 = elapsedRealtime;
            z10 = z14;
            i12 = size;
            i13 = i11;
            z11 = true;
        }
        if (z13 || (uidSipperArr2 = this.f19528d) == null || uidSipperArr2.length == 0) {
            z12 = z10;
            i14 = i12;
            i15 = i13;
        } else {
            UidSipper uidSipper = uidSipperArr2[0];
            try {
                if (NetworkStatusReceiver.f10476b) {
                    this.f19537m = this.f19531g.c(i10);
                }
                if (NetworkStatusReceiver.f10477c) {
                    this.f19538n = this.f19532h.c(i10);
                }
            } catch (RemoteException e16) {
                e16.printStackTrace();
            }
            if (PowerMonitor.j().get(str3) == null) {
                z12 = z10;
                i14 = i12;
                i15 = i13;
                AppStats appStats = new AppStats(i10, str3, true, this.f19529e, this.f19530f, this.f19525a);
                PowerMonitor.j().put(str3, appStats);
                appStats.A(z11);
                appStats.e(uidSipper, this.f19537m, this.f19538n, true, true, false, false);
            } else {
                z12 = z10;
                i14 = i12;
                i15 = i13;
                AppStats appStats2 = PowerMonitor.j().get(str3);
                appStats2.A(z11);
                appStats2.e(uidSipper, this.f19537m, this.f19538n, true, false, false, false);
            }
            HighPowerHelper.f(this.f19525a).c();
        }
        if (z12 || (uidSipperArr = this.f19528d) == null || uidSipperArr.length == 0) {
            return;
        }
        UidSipper uidSipper2 = uidSipperArr[i14 == z11 ? 0 : z11];
        try {
            if (NetworkStatusReceiver.f10476b) {
                this.f19537m = this.f19531g.c(i15);
            }
            if (NetworkStatusReceiver.f10477c) {
                this.f19538n = this.f19532h.c(i15);
            }
        } catch (RemoteException e17) {
            e17.printStackTrace();
        }
        if (PowerMonitor.j().get(str4) != null) {
            AppStats appStats3 = PowerMonitor.j().get(str4);
            appStats3.A(false);
            appStats3.e(uidSipper2, this.f19537m, this.f19538n, false, false, false, false);
        }
        long elapsedRealtime5 = SystemClock.elapsedRealtime();
        if (LocalLog.f()) {
            LocalLog.a("AppSwitchObserverHelper", "app_onAppChangeAction_time: " + (elapsedRealtime5 - j10));
        }
    }

    public static AppSwitchObserverHelper d(Context context) {
        if (f19524q == null) {
            synchronized (AppSwitchObserverHelper.class) {
                if (f19524q == null) {
                    f19524q = new AppSwitchObserverHelper(context);
                }
            }
        }
        return f19524q;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void e(Context context) {
        List<Sensor> sensorList = ((SensorManager) context.getSystemService("sensor")).getSensorList(-1);
        int size = sensorList.size();
        for (int i10 = 0; i10 < size; i10++) {
            Sensor sensor = sensorList.get(i10);
            this.f19530f.put(Integer.valueOf(sensor.getType()), sensor);
        }
    }

    private void h(int i10, long j10, String str, String str2) {
        if (this.f19539o.hasMessages(i10)) {
            this.f19539o.removeMessages(i10);
        }
        Message obtain = Message.obtain();
        obtain.what = i10;
        obtain.obj = str + "," + str2;
        this.f19539o.sendMessageDelayed(obtain, j10);
    }

    public void f(String str, String str2) {
        h(10, 5000L, str, str2);
    }

    public void g() {
        NetWork netWork;
        UidSipper[] uidSipperArr;
        if (LocalLog.f()) {
            LocalLog.a("AppSwitchObserverHelper", "PowerMonitor.getRunningAppStatsList().size(): " + PowerMonitor.j().size());
        }
        if (CommonUtil.T(this.f19525a)) {
            LocalLog.b("AppSwitchObserverHelper", "skip boot reg");
            return;
        }
        if (PowerMonitor.j().size() == 0) {
            return;
        }
        UidSipper uidSipper = null;
        NetworkStatsHistory.Entry entry = null;
        NetworkStatsHistory.Entry entry2 = null;
        for (Map.Entry<String, Integer> entry3 : PowerMonitor.i().entrySet()) {
            int intValue = entry3.getValue().intValue();
            String key = entry3.getKey();
            long elapsedRealtime = SystemClock.elapsedRealtime();
            try {
                int[] iArr = {intValue};
                Class<?> cls = this.f19526b;
                if (cls != null && (uidSipperArr = (UidSipper[]) this.f19527c.invoke(cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]), iArr, Long.valueOf(elapsedRealtime), 0, Boolean.TRUE, Boolean.FALSE)) != null && uidSipperArr.length != 0) {
                    uidSipper = uidSipperArr[0];
                }
                if (NetworkStatusReceiver.f10476b && (netWork = this.f19531g) != null) {
                    entry = netWork.c(intValue);
                }
                if (NetworkStatusReceiver.f10477c && this.f19531g != null) {
                    entry2 = this.f19532h.c(intValue);
                }
                AppStats appStats = PowerMonitor.j().get(key);
                if (appStats != null && !appStats.f16949d && uidSipper != null) {
                    appStats.e(uidSipper, entry, entry2, true, false, true, true);
                    appStats.z(true);
                }
            } catch (Exception e10) {
                e10.printStackTrace();
            }
        }
    }
}
