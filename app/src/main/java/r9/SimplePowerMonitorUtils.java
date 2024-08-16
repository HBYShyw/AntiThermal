package r9;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.UserHandle;
import b6.LocalLog;
import com.oplus.athena.interaction.PackageStateInfo;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.statistics.util.TimeInfoUtil;
import com.oplus.thermalcontrol.ThermalControlUtils;
import ea.StateManager;
import ia.AppInfoUtils;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/* compiled from: SimplePowerMonitorUtils.java */
/* renamed from: r9.a, reason: use source file name */
/* loaded from: classes2.dex */
public class SimplePowerMonitorUtils {

    /* renamed from: a, reason: collision with root package name */
    private Context f17676a;

    /* renamed from: b, reason: collision with root package name */
    private StateManager f17677b;

    /* renamed from: c, reason: collision with root package name */
    public static int f17652c = UserHandle.myUserId();

    /* renamed from: d, reason: collision with root package name */
    public static boolean f17653d = true;

    /* renamed from: e, reason: collision with root package name */
    public static boolean f17654e = true;

    /* renamed from: f, reason: collision with root package name */
    public static boolean f17655f = true;

    /* renamed from: g, reason: collision with root package name */
    public static boolean f17656g = false;

    /* renamed from: h, reason: collision with root package name */
    public static boolean f17657h = false;

    /* renamed from: i, reason: collision with root package name */
    public static boolean f17658i = true;

    /* renamed from: j, reason: collision with root package name */
    public static boolean f17659j = true;

    /* renamed from: k, reason: collision with root package name */
    public static boolean f17660k = false;

    /* renamed from: l, reason: collision with root package name */
    public static long f17661l = 300000;

    /* renamed from: m, reason: collision with root package name */
    public static long f17662m = 300000;

    /* renamed from: n, reason: collision with root package name */
    public static long f17663n = 1800000;

    /* renamed from: o, reason: collision with root package name */
    public static long f17664o = 1800000;

    /* renamed from: p, reason: collision with root package name */
    public static long f17665p = 180000;

    /* renamed from: q, reason: collision with root package name */
    public static long f17666q = 300000;

    /* renamed from: r, reason: collision with root package name */
    public static long f17667r = 1200000;

    /* renamed from: s, reason: collision with root package name */
    public static long f17668s = 2400000;

    /* renamed from: t, reason: collision with root package name */
    public static long f17669t = 360000;

    /* renamed from: u, reason: collision with root package name */
    public static long f17670u = 600000;

    /* renamed from: v, reason: collision with root package name */
    public static long f17671v = 300000;

    /* renamed from: w, reason: collision with root package name */
    public static long f17672w = 300000;

    /* renamed from: x, reason: collision with root package name */
    public static long f17673x = 20;

    /* renamed from: y, reason: collision with root package name */
    public static long f17674y = 180000;

    /* renamed from: z, reason: collision with root package name */
    public static long f17675z = 300000;
    public static long A = 1200000;
    public static long B = 2400000;
    public static long C = 360000;
    public static long D = 600000;
    public static long E = 300000;
    public static long F = 300000;
    public static long G = 20;
    private static CopyOnWriteArrayList<String> H = new CopyOnWriteArrayList<>();
    private static CopyOnWriteArrayList<String> I = new CopyOnWriteArrayList<>();
    private static CopyOnWriteArrayList<String> J = new CopyOnWriteArrayList<>();
    private static CopyOnWriteArrayList<String> K = new CopyOnWriteArrayList<>();
    public static long L = TimeInfoUtil.MILLISECOND_OF_A_DAY;
    public static long M = 259200000;
    private static ConcurrentHashMap<String, Integer> N = new ConcurrentHashMap<>();
    private static volatile SimplePowerMonitorUtils O = null;

    private SimplePowerMonitorUtils(Context context) {
        this.f17676a = context;
    }

    public static void A(long[] jArr) {
        f17661l = jArr[0] * 1000;
        f17662m = jArr[1] * 1000;
        f17663n = jArr[2] * 1000;
        f17664o = jArr[3] * 1000;
    }

    public static void B(ArrayList<String> arrayList, Context context) {
        H.clear();
        H.addAll(0, arrayList);
        for (int i10 = 0; i10 < arrayList.size(); i10++) {
            String str = arrayList.get(i10);
            if (!N.containsKey(str)) {
                N.put(str, Integer.valueOf(l(str, context)));
            }
        }
        LocalLog.a("SimplePowerMonitorUtils", "mPkgBlackArray.size(): " + H.size());
        LocalLog.a("SimplePowerMonitorUtils", "mPkgBlackArray.: " + H.get(0));
    }

    public static void C(long[] jArr) {
        f17665p = jArr[0] * 1000;
        f17666q = jArr[1] * 1000;
        f17667r = jArr[2] * 1000;
        f17668s = jArr[3] * 1000;
        f17669t = jArr[4] * 1000;
        f17670u = jArr[5] * 1000;
        f17671v = jArr[6] * 1000;
        f17672w = jArr[7] * 1000;
        f17673x = jArr[8];
    }

    public static void D(long[] jArr) {
        L = jArr[0] * 1000;
        M = jArr[1] * 1000;
    }

    public static void E(ArrayList<String> arrayList) {
        K.clear();
        K.addAll(0, arrayList);
        LocalLog.a("SimplePowerMonitorUtils", "mPkgPrivilege.get(0): " + K.get(0));
    }

    public static void F(long[] jArr) {
        f17674y = jArr[0] * 1000;
        f17675z = jArr[1] * 1000;
        A = jArr[2] * 1000;
        B = jArr[3] * 1000;
        C = jArr[4] * 1000;
        D = jArr[5] * 1000;
        E = jArr[6] * 1000;
        F = jArr[7] * 1000;
        G = jArr[8];
    }

    public static void G(boolean[] zArr) {
        f17653d = zArr[0];
        f17654e = zArr[1];
        f17655f = zArr[2];
        f17656g = zArr[3];
        f17657h = zArr[4];
        f17658i = zArr[5];
        f17659j = zArr[6];
        f17660k = zArr[7];
    }

    public static void H(int i10) {
        f17652c = i10;
        LocalLog.a("SimplePowerMonitorUtils", "CURRENT_USER_Id: " + f17652c);
        LocalLog.a("SimplePowerMonitorUtils", "updateUserId: " + i10);
    }

    public static void I(ArrayList<String> arrayList) {
        I.clear();
        I.addAll(0, arrayList);
        LocalLog.a("SimplePowerMonitorUtils", "mPkgWhiteArray1.0: " + I.get(0));
        LocalLog.a("SimplePowerMonitorUtils", "mPkgWhiteArray1.1: " + I.get(1));
    }

    public static void J(ArrayList<String> arrayList, Context context) {
        J.clear();
        J.addAll(0, arrayList);
        for (int i10 = 0; i10 < arrayList.size(); i10++) {
            String str = arrayList.get(i10);
            if (N.containsKey(str)) {
                N.remove(str, Integer.valueOf(l(str, context)));
            }
        }
    }

    public static void a(String str, int i10) {
        if (N.containsKey(str)) {
            N.put(f17652c + str, Integer.valueOf(i10));
            return;
        }
        N.put(str, Integer.valueOf(i10));
    }

    public static String b(int i10, Context context) {
        PackageManager packageManager = context.getPackageManager();
        String str = "";
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
            try {
                if (runningAppProcessInfo.pid == i10) {
                    packageManager.getApplicationLabel(packageManager.getApplicationInfoAsUser(runningAppProcessInfo.processName, 128, UserHandle.myUserId()));
                    str = runningAppProcessInfo.processName;
                }
            } catch (Exception unused) {
            }
        }
        return str;
    }

    public static SimplePowerMonitorUtils d(Context context) {
        if (O == null) {
            synchronized (ThermalControlUtils.class) {
                if (O == null) {
                    O = new SimplePowerMonitorUtils(context);
                }
            }
        }
        return O;
    }

    public static ConcurrentHashMap<String, Integer> e() {
        return N;
    }

    public static void f(Context context) {
        N.clear();
        for (AppInfoUtils.a aVar : new ArrayList(AppInfoUtils.d().c())) {
            String str = aVar.f12689a;
            if ((aVar.f12690b & 1) != 0) {
                if (H.contains(str)) {
                    N.put(str, Integer.valueOf(l(str, context)));
                }
                LocalLog.a("SimplePowerMonitorUtils", "Installed package (System) :" + str);
            } else {
                LocalLog.a("SimplePowerMonitorUtils", "Installed package (User) :" + str);
                if (!J.contains(str)) {
                    N.put(str, Integer.valueOf(l(str, context)));
                }
            }
        }
    }

    private List<PackageStateInfo> g(int i10) {
        ArrayList arrayList = new ArrayList();
        StateManager stateManager = this.f17677b;
        if (stateManager == null) {
            this.f17677b = StateManager.f(this.f17676a);
            return arrayList;
        }
        try {
            return stateManager.h(i10);
        } catch (Exception e10) {
            LocalLog.a("SimplePowerMonitorUtils", "RemoteException=" + e10);
            e10.printStackTrace();
            return arrayList;
        }
    }

    public static String[] h(int i10, Context context) {
        String[] packagesForUid = context.getPackageManager().getPackagesForUid(i10);
        if (packagesForUid != null) {
            return packagesForUid;
        }
        return null;
    }

    public static List<String> i() {
        return K;
    }

    public static List<String> j() {
        return I;
    }

    public static void k(Context context, ConcurrentHashMap<String, Integer> concurrentHashMap) {
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
            if (runningAppProcessInfo.importance > 200) {
                for (String str : runningAppProcessInfo.pkgList) {
                    if (s(str, runningAppProcessInfo.uid, context)) {
                        concurrentHashMap.put(str, Integer.valueOf(runningAppProcessInfo.uid));
                    }
                }
            }
        }
    }

    public static int l(String str, Context context) {
        try {
            return context.getPackageManager().getApplicationInfoAsUser(str, 0, f17652c).uid;
        } catch (PackageManager.NameNotFoundException e10) {
            e10.printStackTrace();
            return -1;
        }
    }

    public static boolean s(String str, int i10, Context context) {
        if (H.contains(str)) {
            return true;
        }
        if (J.contains(str)) {
            return false;
        }
        for (AppInfoUtils.a aVar : new ArrayList(AppInfoUtils.d().c())) {
            if (aVar.f12689a.equals(str)) {
                return (aVar.f12690b & 1) == 0 && i10 >= 10000;
            }
        }
        return true;
    }

    public static String z(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
            byte[] digest = messageDigest.digest();
            StringBuilder sb2 = new StringBuilder();
            for (byte b10 : digest) {
                String hexString = Integer.toHexString(b10 & 255);
                if (hexString.length() == 1) {
                    sb2.append('0');
                }
                sb2.append(hexString);
            }
            return sb2.toString();
        } catch (NoSuchAlgorithmException e10) {
            throw new RuntimeException("Algorithm SHA-256 error", e10);
        }
    }

    public List<Integer> c(String str) {
        ArrayList arrayList = new ArrayList();
        StateManager stateManager = this.f17677b;
        if (stateManager == null) {
            this.f17677b = StateManager.f(this.f17676a);
            return arrayList;
        }
        try {
            return stateManager.j(str, UserHandle.myUserId());
        } catch (Exception e10) {
            LocalLog.a("SimplePowerMonitorUtils", "RemoteException=" + e10);
            e10.printStackTrace();
            return arrayList;
        }
    }

    public boolean m(List<Integer> list) {
        return x(list);
    }

    public boolean n(List<Integer> list) {
        List<PackageStateInfo> g6 = g(107);
        if (LocalLog.g()) {
            LocalLog.a("SimplePowerMonitorUtils", "audioInfo: " + g6);
        }
        if (g6 == null || g6.isEmpty()) {
            return false;
        }
        if (list != null && !list.isEmpty()) {
            list.contains(107);
        }
        return true;
    }

    public boolean o(List<Integer> list) {
        return x(list) || u(list) || n(list) || w(list) || r(list);
    }

    public boolean p(List<Integer> list) {
        List<PackageStateInfo> g6 = g(EventType.SCENE_MODE_LOCATION);
        if (LocalLog.g()) {
            LocalLog.a("SimplePowerMonitorUtils", "downloadInfo: " + g6);
        }
        if (g6 == null || g6.isEmpty()) {
            return false;
        }
        if (list != null && !list.isEmpty()) {
            list.contains(Integer.valueOf(EventType.SCENE_MODE_LOCATION));
        }
        return true;
    }

    public boolean q(List<Integer> list) {
        return r(list);
    }

    public boolean r(List<Integer> list) {
        List<PackageStateInfo> g6 = g(106);
        if (LocalLog.g()) {
            LocalLog.a("SimplePowerMonitorUtils", "navigationInfo: " + g6);
        }
        if (g6 == null || g6.isEmpty()) {
            return false;
        }
        if (list != null && !list.isEmpty()) {
            list.contains(106);
        }
        return true;
    }

    public boolean t(List<Integer> list) {
        List<PackageStateInfo> g6 = g(EventType.SCENE_MODE_AUDIO_IN);
        if (LocalLog.g()) {
            LocalLog.a("SimplePowerMonitorUtils", "recordAudioInfo: " + g6);
        }
        if (g6 == null || g6.isEmpty()) {
            return false;
        }
        if (list != null && !list.isEmpty()) {
            list.contains(Integer.valueOf(EventType.SCENE_MODE_AUDIO_IN));
        }
        return true;
    }

    public boolean u(List<Integer> list) {
        List<PackageStateInfo> g6 = g(EventType.SCENE_MODE_AUDIO_OUT);
        if (LocalLog.g()) {
            LocalLog.a("SimplePowerMonitorUtils", "shortVideoInfo: " + g6);
        }
        if (g6 == null || g6.isEmpty()) {
            return false;
        }
        if (list != null && !list.isEmpty()) {
            list.contains(Integer.valueOf(EventType.SCENE_MODE_AUDIO_OUT));
        }
        return true;
    }

    public boolean v(List<Integer> list) {
        return t(list) || x(list) || u(list) || n(list) || w(list) || p(list) || r(list);
    }

    public boolean w(List<Integer> list) {
        List<PackageStateInfo> g6 = g(105);
        if (LocalLog.g()) {
            LocalLog.a("SimplePowerMonitorUtils", "videoLiveInfo: " + g6);
        }
        if (g6 == null || g6.isEmpty()) {
            return false;
        }
        if (list != null && !list.isEmpty()) {
            list.contains(105);
        }
        return true;
    }

    public boolean x(List<Integer> list) {
        List<PackageStateInfo> g6 = g(101);
        if (LocalLog.g()) {
            LocalLog.a("SimplePowerMonitorUtils", "videoInfo: " + g6);
        }
        if (g6 == null || g6.isEmpty()) {
            return false;
        }
        if (list != null && !list.isEmpty()) {
            list.contains(101);
        }
        return true;
    }

    public boolean y(List<Integer> list) {
        return x(list) || u(list) || n(list) || w(list) || p(list) || r(list);
    }
}
