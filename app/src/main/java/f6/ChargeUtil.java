package f6;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import b6.LocalLog;
import com.oplus.deepsleep.DeepSleepUtils;
import com.oplus.deepthinker.sdk.app.geofence.Geofence;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.ToIntFunction;

/* compiled from: ChargeUtil.java */
/* renamed from: f6.b, reason: use source file name */
/* loaded from: classes.dex */
public class ChargeUtil {

    /* renamed from: a, reason: collision with root package name */
    public static boolean f11360a = true;

    /* renamed from: b, reason: collision with root package name */
    public static int f11361b = 3;

    /* renamed from: c, reason: collision with root package name */
    public static boolean f11362c = true;

    /* renamed from: d, reason: collision with root package name */
    public static int f11363d = 30;

    /* renamed from: e, reason: collision with root package name */
    public static int f11364e = 180;

    /* renamed from: f, reason: collision with root package name */
    public static int f11365f = 15;

    /* renamed from: g, reason: collision with root package name */
    public static float f11366g = 0.6f;

    /* renamed from: h, reason: collision with root package name */
    public static float f11367h = 0.4f;

    /* renamed from: i, reason: collision with root package name */
    public static int f11368i = 3;

    /* renamed from: j, reason: collision with root package name */
    public static float f11369j = 0.9f;

    /* renamed from: k, reason: collision with root package name */
    public static float f11370k = 0.5f;

    /* renamed from: l, reason: collision with root package name */
    public static int f11371l = 240;

    /* renamed from: m, reason: collision with root package name */
    public static int f11372m = 120;

    /* renamed from: n, reason: collision with root package name */
    public static int f11373n = 120;

    /* renamed from: o, reason: collision with root package name */
    public static int f11374o = 60;

    /* renamed from: p, reason: collision with root package name */
    public static int f11375p = 30;

    /* renamed from: q, reason: collision with root package name */
    public static int f11376q = 3;

    /* renamed from: r, reason: collision with root package name */
    public static float f11377r = 0.6f;

    /* renamed from: s, reason: collision with root package name */
    private static float[] f11378s = {0.7f, 0.775f, 0.85f, 0.925f, 1.0f};

    /* renamed from: t, reason: collision with root package name */
    private static float[] f11379t = {0.7f, 0.775f, 0.85f, 0.925f, 1.0f};

    /* renamed from: u, reason: collision with root package name */
    private static ArrayList<String> f11380u = new ArrayList<>();

    /* renamed from: v, reason: collision with root package name */
    public static boolean f11381v = true;

    /* renamed from: w, reason: collision with root package name */
    public static long f11382w = 5400000;

    /* renamed from: x, reason: collision with root package name */
    public static int f11383x = 5;

    /* renamed from: y, reason: collision with root package name */
    public static int f11384y = 5;

    /* renamed from: z, reason: collision with root package name */
    public static int f11385z = 65;
    public static int A = 60;
    public static int B = 33;
    public static boolean C = true;
    public static int D = 5;
    public static final List<Integer> E = Collections.unmodifiableList(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    private static int F = 0;

    /* compiled from: ChargeUtil.java */
    /* renamed from: f6.b$a */
    /* loaded from: classes.dex */
    class a extends HashMap<String, Float> {
        a() {
            put("PEUM00_BIG", Float.valueOf(46.6f));
            put("PEUM00_SMALL", Float.valueOf(46.8f));
            put("PGEM10", Float.valueOf(167.65f));
            Float valueOf = Float.valueOf(169.0f);
            put("PJD110", valueOf);
            put("CPH2573", valueOf);
            put("PHY110", Float.valueOf(154.72f));
        }
    }

    public static void A(String str, Context context) {
        LocalLog.a("ChargeUtil", "setChgOlcConfig: data = " + str);
        try {
            Class<?> cls = Class.forName("android.os.OplusBatteryManager");
            LocalLog.a("ChargeUtil", "setChgOlcConfig " + ((Integer) cls.getMethod("setChgOlcConfig", String.class).invoke(cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]), str)).intValue());
        } catch (Exception e10) {
            e10.printStackTrace();
            LocalLog.l("ChargeUtil", "setChgOlcConfig fail!");
        }
    }

    public static void B(boolean z10) {
        f11360a = z10;
        u(z10);
    }

    public static void C(int i10) {
        f11364e = i10;
    }

    public static void D(int i10) {
        f11365f = i10;
    }

    public static void E(float f10, float f11) {
        f11366g = f10;
        f11367h = f11;
    }

    public static void F(float f10) {
        f11377r = f10;
    }

    public static void G(int i10) {
        f11368i = i10;
    }

    public static void H(boolean z10, Context context) {
        f11362c = z10;
    }

    public static void I(int i10) {
        f11375p = i10;
    }

    public static void J(int i10) {
        f11376q = i10;
    }

    public static void K(float f10, float[] fArr, float[] fArr2, float f11) {
        f11369j = f10;
        f11378s = fArr;
        f11379t = fArr2;
        f11370k = f11;
    }

    public static void L(int i10, int i11, int i12, int i13) {
        f11371l = i10;
        f11372m = i11;
        f11373n = i12;
        f11374o = i13;
    }

    public static void M(int i10) {
        f11363d = i10;
    }

    public static void N(int i10, Context context) {
        if ((i10 & 16777216) == 16777216) {
            String l10 = l();
            LocalLog.a("ChargeUtil", "reserveSocData : " + i10 + " reserveSocString : " + l10);
            if (l10 == null || l10.length() == 0) {
                return;
            }
            int[] array = Arrays.asList(l10.split(",")).stream().mapToInt(new ToIntFunction() { // from class: f6.a
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(Object obj) {
                    return Integer.parseInt((String) obj);
                }
            }).toArray();
            int i11 = (i10 >> 16) & 255;
            int i12 = (i10 >> 8) & 255;
            if (array == null || array.length != 2) {
                return;
            }
            if (i11 == array[0] && i12 == array[1]) {
                return;
            }
            X(context, i10);
            w(String.format(Locale.US, "%d,%d", Integer.valueOf(i11), Integer.valueOf(i12)));
            LocalLog.a("ChargeUtil", "reserve soc update to " + i11 + i12);
        }
    }

    public static void O(int i10, int i11) {
        LocalLog.a("ChargeUtil", "updateSlowChargeProtectAiAccuracyPercent: sleepPercent = " + i10 + ", wakePercent = " + i11);
        List<Integer> list = DeepSleepUtils.AI_SLEEP_PERCENT_LIST;
        if (list.contains(Integer.valueOf(i10))) {
            f11383x = i10;
        } else {
            LocalLog.l("ChargeUtil", "set invalid ai sleep percent " + i10);
        }
        if (list.contains(Integer.valueOf(i11))) {
            f11384y = i11;
            return;
        }
        LocalLog.l("ChargeUtil", "set invalid ai wake percent " + i11);
    }

    public static void P(boolean z10) {
        f11381v = z10;
    }

    public static void Q(int i10) {
        f11382w = i10 * 60000;
    }

    public static void R(int i10) {
        A = i10;
    }

    public static void S(int i10) {
        B = i10;
    }

    public static void T(int i10) {
        f11385z = i10;
    }

    public static void U(int i10) {
        f11361b = i10;
    }

    public static void V(boolean z10) {
        C = z10;
    }

    public static void W(int i10) {
        LocalLog.a("ChargeUtil", "updateSmartSpeedChargeAiLeaveHomePercent: percent = " + i10);
        if (E.contains(Integer.valueOf(i10))) {
            D = i10;
            return;
        }
        LocalLog.l("ChargeUtil", "set invalid ai leave home percent " + i10);
    }

    public static void X(Context context, int i10) {
        try {
            Class<?> cls = Class.forName("android.engineer.OplusEngineerManager");
            Class<?> cls2 = Integer.TYPE;
            Method method = cls.getMethod("saveEngineerData", cls2, byte[].class, cls2);
            byte[] p10 = p(i10);
            method.invoke(null, 1000063, p10, Integer.valueOf(p10.length));
        } catch (Exception unused) {
            LocalLog.a("ChargeUtil", "unable to writeReserveData");
        }
    }

    public static long a(double d10, long j10) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j10);
        calendar.set(13, 0);
        calendar.set(14, 0);
        int i10 = (int) (60.0d * d10);
        int i11 = i10 / 60;
        int i12 = i10 % 60;
        calendar.set(11, i11);
        calendar.set(12, i12);
        if (i11 >= 24.0d) {
            LocalLog.a("ChargeUtil", "convertDoubleTime: teime error. time=" + d10 + ", hour=" + i11 + ", minute=" + i12);
            return -1L;
        }
        return calendar.getTimeInMillis();
    }

    public static int b(float f10, int i10) {
        return (int) ((f10 * i10) / 360.0f);
    }

    public static long c(Context context) {
        return Settings.System.getLong(context.getContentResolver(), "charge_time_remaining_test_data", -99L);
    }

    public static int d() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(2);
        return ((calendar.get(7) + 5) % 7) + 1;
    }

    public static long e() {
        Calendar calendar = Calendar.getInstance();
        return ((System.currentTimeMillis() - (calendar.get(11) * 3600000)) - (calendar.get(12) * 60000)) - (calendar.get(13) * 1000);
    }

    public static int f() {
        return Calendar.getInstance().get(11);
    }

    public static String g() {
        return new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss").format(new Date(System.currentTimeMillis()));
    }

    public static String h() {
        return Build.MODEL;
    }

    public static float[] i() {
        return f11378s;
    }

    public static float[] j() {
        return f11379t;
    }

    public static int k(Context context) {
        int i10 = 0;
        try {
            Class<?> cls = Class.forName("android.os.OplusBatteryManager");
            i10 = ((Integer) cls.getMethod("getPsyChargeTech", new Class[0]).invoke(cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]), new Object[0])).intValue();
            LocalLog.a("ChargeUtil", "getPsyChargeTechValue: " + i10);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e10) {
            e10.printStackTrace();
            LocalLog.l("ChargeUtil", "getPsyChargeTech fail!");
        }
        switch (i10) {
            case 1:
                return 30;
            case 2:
            case 4:
                return 50;
            case 3:
                return 65;
            case 5:
                return 33;
            case 6:
                return 60;
            case 7:
                return 80;
            case 8:
                return 100;
            case 9:
                return Geofence.MIN_RADIUS;
            case 10:
            case 11:
            case 14:
            default:
                return 18;
            case 12:
                return 66;
            case 13:
                return 67;
            case 15:
                return 44;
        }
    }

    public static String l() {
        String str;
        Exception e10;
        try {
            Class<?> cls = Class.forName("android.os.OplusBatteryManager");
            str = (String) cls.getMethod("getReserveSocDebug", new Class[0]).invoke(cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]), new Object[0]);
            try {
                LocalLog.a("ChargeUtil", "getReserveSocDebug " + str);
            } catch (Exception e11) {
                e10 = e11;
                e10.printStackTrace();
                LocalLog.l("ChargeUtil", "getReserveSocDebug fail!");
                return str;
            }
        } catch (Exception e12) {
            str = null;
            e10 = e12;
        }
        return str;
    }

    public static String m() {
        String str;
        ReflectiveOperationException e10;
        try {
            Class<?> cls = Class.forName("android.os.OplusBatteryManager");
            str = (String) cls.getMethod("getQuickModeGain", new Class[0]).invoke(cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]), new Object[0]);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e11) {
            str = "";
            e10 = e11;
        }
        try {
            LocalLog.a("ChargeUtil", "getQuickModeGain: getSmartChargeIncome: " + str);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e12) {
            e10 = e12;
            e10.printStackTrace();
            LocalLog.l("ChargeUtil", "getQuickModeGain fail!");
            return str;
        }
        return str;
    }

    public static int n(Context context) {
        if (f.q(context) == 1) {
            LocalLog.b("ChargeUtil", "Current in BatteryHealthTestMode!");
            return f.o(context);
        }
        int i10 = 100;
        try {
            Class<?> cls = Class.forName("android.os.OplusBatteryManager");
            i10 = ((Integer) cls.getMethod("getUIsohValue", new Class[0]).invoke(cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]), new Object[0])).intValue();
            LocalLog.a("ChargeUtil", "currentUIsohValue: " + i10);
            return i10;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e10) {
            e10.printStackTrace();
            LocalLog.l("ChargeUtil", "getUIsohValue fail!");
            return i10;
        }
    }

    public static Map<String, Float> o() {
        return new a();
    }

    public static byte[] p(int i10) {
        return new byte[]{(byte) (i10 & 255), (byte) ((i10 >> 8) & 255), (byte) ((i10 >> 16) & 255), (byte) ((i10 >> 24) & 255)};
    }

    public static int q(String str) {
        int i10 = -1;
        try {
            Class<?> cls = Class.forName("android.os.OplusBatteryManager");
            i10 = ((Integer) cls.getMethod("setBatteryLogPush", String.class).invoke(cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]), str)).intValue();
            LocalLog.a("ChargeUtil", "setBatteryLogPush " + i10);
            return i10;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e10) {
            e10.printStackTrace();
            LocalLog.l("ChargeUtil", "setBatteryLogPush fail!");
            return i10;
        }
    }

    public static void r(long j10, Context context) {
        Settings.System.putLong(context.getContentResolver(), "charge_time_remaining_test_data", j10);
    }

    public static int s(String str) {
        int i10 = -1;
        if ("".equals(str)) {
            return -1;
        }
        try {
            Class<?> cls = Class.forName("android.os.OplusBatteryManager");
            i10 = ((Integer) cls.getMethod("setChgRusConfig", String.class).invoke(cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]), str)).intValue();
            LocalLog.a("ChargeUtil", "setChgRusConfig " + i10);
            return i10;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e10) {
            e10.printStackTrace();
            LocalLog.l("ChargeUtil", "setChgRusConfig fail!");
            return i10;
        }
    }

    public static int t(int i10, boolean z10) {
        LocalLog.a("ChargeUtil", "setCustomSelectChgMode: mode = " + i10 + ", enable = " + z10);
        int i11 = -1;
        try {
            Class<?> cls = Class.forName("android.os.OplusBatteryManager");
            i11 = ((Integer) cls.getMethod("setCustomSelectChgMode", Integer.TYPE, Boolean.TYPE).invoke(cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]), Integer.valueOf(i10), Boolean.valueOf(z10))).intValue();
            LocalLog.a("ChargeUtil", "setCustomSelectChgMode: successCustomSelectChgMode: " + i11);
            return i11;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e10) {
            e10.printStackTrace();
            LocalLog.l("ChargeUtil", "setCustomSelectChgMode fail!");
            return i11;
        }
    }

    private static void u(boolean z10) {
        LocalLog.a("ChargeUtil", "setLifeModeCharge: " + z10);
        try {
            Class<?> cls = Class.forName("android.os.OplusBatteryManager");
            Object newInstance = cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            Method method = cls.getMethod("getSmartChgMode", new Class[0]);
            Method method2 = cls.getMethod("setSmartChgMode", String.class);
            int intValue = ((Integer) method.invoke(newInstance, new Object[0])).intValue();
            LocalLog.a("ChargeUtil", "currentSmartChgMode: " + intValue);
            String valueOf = String.valueOf(z10 ? intValue | 1 : intValue & (-2));
            LocalLog.a("ChargeUtil", "setSmartChgModeValue: " + valueOf);
            LocalLog.a("ChargeUtil", "successSmartChgMode: " + ((Integer) method2.invoke(newInstance, valueOf)).intValue());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e10) {
            e10.printStackTrace();
            LocalLog.l("ChargeUtil", "setLifeModeCharge fail!");
        }
    }

    public static int v(Context context, int i10, int i11, int i12) {
        int i13 = -1;
        try {
            Class<?> cls = Class.forName("android.os.OplusBatteryManager");
            Object newInstance = cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            Class<?> cls2 = Integer.TYPE;
            i13 = ((Integer) cls.getMethod("setPsySlowChgEnable", cls2, cls2, cls2).invoke(newInstance, Integer.valueOf(i10), Integer.valueOf(i11), Integer.valueOf(i12))).intValue();
            LocalLog.a("ChargeUtil", "setPsySlowChgEnable: " + i13);
            return i13;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e10) {
            e10.printStackTrace();
            LocalLog.l("ChargeUtil", "setPsySlowChgEnable fail!");
            return i13;
        }
    }

    public static int w(String str) {
        LocalLog.a("ChargeUtil", "setReserveSocDebug: data = " + str);
        int i10 = -1;
        try {
            Class<?> cls = Class.forName("android.os.OplusBatteryManager");
            i10 = ((Integer) cls.getMethod("setReserveSocDebug", String.class).invoke(cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]), str)).intValue();
            LocalLog.a("ChargeUtil", "setReserveSocDebug " + i10);
            return i10;
        } catch (Exception e10) {
            e10.printStackTrace();
            LocalLog.l("ChargeUtil", "setReserveSocDebug fail!");
            return i10;
        }
    }

    public static int x(String str) {
        LocalLog.a("ChargeUtil", "setWlsThirdPartitionInfo: data = " + str);
        int i10 = -1;
        try {
            Class<?> cls = Class.forName("android.os.OplusBatteryManager");
            i10 = ((Integer) cls.getMethod("setWlsThirdPartitionInfo", String.class).invoke(cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]), str)).intValue();
            LocalLog.a("ChargeUtil", "setWlsThirdPartitionInfo " + i10);
            return i10;
        } catch (Exception e10) {
            e10.printStackTrace();
            LocalLog.l("ChargeUtil", "setWlsThirdPartitionInfofail!");
            return i10;
        }
    }

    public static String y(long j10) {
        return new SimpleDateFormat("YYYY-MM-dd HH:mm:ss", Locale.getDefault()).format(Long.valueOf(j10));
    }

    public static String z(long j10) {
        String[] split = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(Long.valueOf(j10)).split(":");
        return split[0] + ":" + split[1];
    }
}
