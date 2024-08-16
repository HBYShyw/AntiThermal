package p9;

import android.app.ActivityManager;
import android.app.IUidObserver;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.INetworkStatsService;
import android.net.INetworkStatsSession;
import android.net.NetworkStatsHistory;
import android.net.NetworkTemplate;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import b6.LocalLog;
import c6.NotifyUtil;
import com.android.internal.os.PowerProfile;
import com.android.internal.os.UidSipper;
import com.oplus.simplepowermonitor.alarm.NetworkStatusReceiver;
import f6.CommonUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import q9.AppStats;
import q9.NetWork;
import r9.SimplePowerMonitorUtils;
import x4.AppSwitchObserverHelper;

/* compiled from: PowerMonitor.java */
/* renamed from: p9.c, reason: use source file name */
/* loaded from: classes2.dex */
public class PowerMonitor {

    /* renamed from: q, reason: collision with root package name */
    private static Context f16638q;

    /* renamed from: r, reason: collision with root package name */
    private static volatile PowerMonitor f16639r;

    /* renamed from: s, reason: collision with root package name */
    private static ConcurrentHashMap<String, Integer> f16640s = new ConcurrentHashMap<>();

    /* renamed from: t, reason: collision with root package name */
    private static ConcurrentHashMap<String, AppStats> f16641t = new ConcurrentHashMap<>();

    /* renamed from: u, reason: collision with root package name */
    private static HashMap<Integer, Sensor> f16642u = new HashMap<>();

    /* renamed from: v, reason: collision with root package name */
    private static NetWork f16643v;

    /* renamed from: w, reason: collision with root package name */
    private static NetWork f16644w;

    /* renamed from: a, reason: collision with root package name */
    private HandlerThread f16645a;

    /* renamed from: b, reason: collision with root package name */
    private Handler f16646b;

    /* renamed from: c, reason: collision with root package name */
    private HandlerThread f16647c;

    /* renamed from: d, reason: collision with root package name */
    private Handler f16648d;

    /* renamed from: e, reason: collision with root package name */
    private PowerProfile f16649e;

    /* renamed from: f, reason: collision with root package name */
    private INetworkStatsSession f16650f;

    /* renamed from: g, reason: collision with root package name */
    private INetworkStatsService f16651g;

    /* renamed from: h, reason: collision with root package name */
    private NetworkTemplate f16652h;

    /* renamed from: i, reason: collision with root package name */
    private NetworkTemplate f16653i;

    /* renamed from: j, reason: collision with root package name */
    private NetworkStatsHistory.Entry f16654j;

    /* renamed from: k, reason: collision with root package name */
    private NetworkStatsHistory.Entry f16655k;

    /* renamed from: l, reason: collision with root package name */
    private Class<?> f16656l;

    /* renamed from: m, reason: collision with root package name */
    private Method f16657m;

    /* renamed from: n, reason: collision with root package name */
    private UidSipper f16658n;

    /* renamed from: o, reason: collision with root package name */
    private NotifyUtil f16659o;

    /* renamed from: p, reason: collision with root package name */
    private final IUidObserver f16660p = new a();

    /* compiled from: PowerMonitor.java */
    /* renamed from: p9.c$a */
    /* loaded from: classes2.dex */
    class a extends IUidObserver.Stub {
        a() {
        }

        public void onUidActive(int i10) {
            if (LocalLog.g()) {
                LocalLog.a("PowerMonitor", i10 + " is Active!");
            }
            String[] h10 = SimplePowerMonitorUtils.h(i10, PowerMonitor.f16638q);
            boolean z10 = SimplePowerMonitorUtils.f17652c == 0;
            if (h10 != null) {
                int length = h10.length;
                for (int i11 = 0; i11 < length; i11++) {
                    String str = h10[i11];
                    boolean s7 = SimplePowerMonitorUtils.s(str, i10, PowerMonitor.f16638q);
                    if (!z10) {
                        str = str + "." + SimplePowerMonitorUtils.f17652c;
                    }
                    if (!PowerMonitor.f16640s.containsKey(str) && s7) {
                        PowerMonitor.f16640s.put(str, Integer.valueOf(i10));
                    }
                }
            }
        }

        public void onUidCachedChanged(int i10, boolean z10) {
        }

        public void onUidGone(int i10, boolean z10) {
            if (LocalLog.g()) {
                LocalLog.a("PowerMonitor", i10 + " is gone!");
            }
            String[] h10 = SimplePowerMonitorUtils.h(i10, PowerMonitor.f16638q);
            boolean z11 = SimplePowerMonitorUtils.f17652c == 0;
            HashMap<String, Boolean> w10 = PowerMonitor.this.f16659o.w();
            if (h10 != null) {
                int length = h10.length;
                for (int i11 = 0; i11 < length; i11++) {
                    String str = h10[i11];
                    if (w10 != null && w10.containsKey(str) && w10.get(str).booleanValue()) {
                        PowerMonitor.this.f16659o.k(str);
                    }
                    if (!z11) {
                        str = str + "." + SimplePowerMonitorUtils.f17652c;
                    }
                    if (PowerMonitor.f16640s.containsKey(str)) {
                        PowerMonitor.f16640s.remove(str, Integer.valueOf(i10));
                    }
                }
            }
        }

        public void onUidIdle(int i10, boolean z10) {
        }

        public void onUidProcAdjChanged(int i10, int i11) {
        }

        public void onUidStateChanged(int i10, int i11, long j10, int i12) {
        }
    }

    private PowerMonitor(Context context) {
        this.f16656l = null;
        this.f16657m = null;
        f16638q = context;
        m();
        this.f16649e = new PowerProfile(f16638q);
        SimplePowerMonitorUtils.k(f16638q, f16640s);
        List<Sensor> sensorList = ((SensorManager) context.getSystemService("sensor")).getSensorList(-1);
        int size = sensorList.size();
        for (int i10 = 0; i10 < size; i10++) {
            Sensor sensor = sensorList.get(i10);
            f16642u.put(Integer.valueOf(sensor.getType()), sensor);
        }
        try {
            Class<?> cls = Class.forName("com.android.internal.os.OplusBatteryStatsManager");
            this.f16656l = cls;
            Class<?> cls2 = Boolean.TYPE;
            this.f16657m = cls.getMethod("getUidSipper", int[].class, Long.TYPE, Integer.TYPE, cls2, cls2);
        } catch (Exception e10) {
            LocalLog.a("PowerMonitor", "getUidSipper error = " + e10.toString());
        }
        this.f16651g = INetworkStatsService.Stub.asInterface(ServiceManager.getService("netstats"));
        this.f16652h = NetworkTemplate.buildTemplateWifiWildcard();
        this.f16653i = NetworkTemplate.buildTemplateMobileWildcard();
        try {
            INetworkStatsSession openSession = this.f16651g.openSession();
            this.f16650f = openSession;
            f16643v = new NetWork(openSession, NetWork.a(this.f16652h, 30));
            f16644w = new NetWork(this.f16650f, NetWork.a(this.f16653i, 30));
            this.f16659o = NotifyUtil.v(f16638q);
            HandlerThread handlerThread = new HandlerThread("ScreenOnMonitor");
            this.f16645a = handlerThread;
            handlerThread.start();
            this.f16646b = new Handler(this.f16645a.getLooper());
            HandlerThread handlerThread2 = new HandlerThread("ScreenOffMonitor");
            this.f16647c = handlerThread2;
            handlerThread2.start();
            this.f16648d = new Handler(this.f16647c.getLooper());
        } catch (RemoteException e11) {
            throw new RuntimeException(e11);
        }
    }

    public static PowerMonitor h(Context context) {
        if (LocalLog.f()) {
            LocalLog.a("PowerMonitor", "getInstance");
        }
        if (f16639r == null) {
            synchronized (PowerMonitor.class) {
                if (f16639r == null) {
                    f16639r = new PowerMonitor(context);
                }
            }
        }
        return f16639r;
    }

    public static ConcurrentHashMap<String, Integer> i() {
        return f16640s;
    }

    public static ConcurrentHashMap<String, AppStats> j() {
        return f16641t;
    }

    private void m() {
        try {
            ActivityManager.getService().registerUidObserver(this.f16660p, 10, -1, (String) null);
        } catch (Exception unused) {
        }
    }

    /* renamed from: f, reason: merged with bridge method [inline-methods] */
    public void k(boolean z10) {
        if (LocalLog.f()) {
            LocalLog.a("PowerMonitor", "doMonitorWhileScreenOff");
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        for (Map.Entry<String, Integer> entry : f16640s.entrySet()) {
            int intValue = entry.getValue().intValue();
            String key = entry.getKey();
            try {
                UidSipper[] uidSipperArr = (UidSipper[]) this.f16657m.invoke(this.f16656l.newInstance(), new int[]{intValue}, Long.valueOf(elapsedRealtime), 0, Boolean.TRUE, Boolean.FALSE);
                if (uidSipperArr != null && uidSipperArr.length != 0) {
                    this.f16658n = uidSipperArr[0];
                }
                if (NetworkStatusReceiver.f10476b) {
                    this.f16654j = f16643v.c(intValue);
                }
                if (NetworkStatusReceiver.f10477c) {
                    this.f16655k = f16644w.c(intValue);
                }
            } catch (RemoteException | IllegalAccessException | InstantiationException | InvocationTargetException e10) {
                e10.printStackTrace();
            }
            if (f16641t.get(key) == null) {
                AppStats appStats = new AppStats(intValue, key, false, this.f16649e, f16642u, f16638q);
                f16641t.put(key, appStats);
                appStats.d(this.f16658n, this.f16654j, this.f16655k, z10);
            } else {
                f16641t.get(key).d(this.f16658n, this.f16654j, this.f16655k, z10);
            }
        }
    }

    /* renamed from: g, reason: merged with bridge method [inline-methods] */
    public void l() {
    }

    public void n(final boolean z10) {
        if (LocalLog.f()) {
            LocalLog.a("PowerMonitor", "startScreenOffMonitor");
        }
        if (CommonUtil.T(f16638q)) {
            LocalLog.b("PowerMonitor", "skip boot reg");
        } else {
            this.f16648d.post(new Runnable() { // from class: p9.b
                @Override // java.lang.Runnable
                public final void run() {
                    PowerMonitor.this.k(z10);
                }
            });
        }
    }

    public void o() {
        if (LocalLog.f()) {
            LocalLog.a("PowerMonitor", "startScreenOnMonitor");
        }
        if (CommonUtil.T(f16638q)) {
            LocalLog.b("PowerMonitor", "skip boot reg");
        } else {
            this.f16646b.post(new Runnable() { // from class: p9.a
                @Override // java.lang.Runnable
                public final void run() {
                    PowerMonitor.this.l();
                }
            });
        }
    }

    public void p() {
        if (LocalLog.f()) {
            LocalLog.a("PowerMonitor", "stopScreenOffMonitor");
        }
        if (CommonUtil.T(f16638q)) {
            LocalLog.b("PowerMonitor", "skip boot reg");
            return;
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        for (Map.Entry<String, Integer> entry : f16640s.entrySet()) {
            int intValue = entry.getValue().intValue();
            String key = entry.getKey();
            try {
                UidSipper[] uidSipperArr = (UidSipper[]) this.f16657m.invoke(this.f16656l.newInstance(), new int[]{intValue}, Long.valueOf(elapsedRealtime), 0, Boolean.TRUE, Boolean.FALSE);
                if (uidSipperArr != null && uidSipperArr.length != 0) {
                    this.f16658n = uidSipperArr[0];
                }
                try {
                    if (NetworkStatusReceiver.f10476b) {
                        this.f16654j = f16643v.c(intValue);
                    }
                    if (NetworkStatusReceiver.f10477c) {
                        this.f16655k = f16644w.c(intValue);
                    }
                } catch (RemoteException e10) {
                    e10.printStackTrace();
                }
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e11) {
                e11.printStackTrace();
            }
            if (f16641t.get(key) != null && this.f16658n != null) {
                f16641t.get(key).d(this.f16658n, this.f16654j, this.f16655k, false);
            }
        }
    }

    public void q() {
        if (LocalLog.f()) {
            LocalLog.a("PowerMonitor", "stopScreenOnMonitor");
        }
        if (CommonUtil.T(f16638q)) {
            LocalLog.b("PowerMonitor", "skip boot reg");
        } else {
            AppSwitchObserverHelper.d(f16638q).g();
        }
    }
}
