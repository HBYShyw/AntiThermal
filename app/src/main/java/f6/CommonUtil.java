package f6;

import android.app.ActivityManager;
import android.app.OplusActivityManager;
import android.app.OplusWhiteListManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Xml;
import b6.LocalLog;
import c6.NotifyUtil;
import com.oplus.app.OplusAppInfo;
import com.oplus.battery.R;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.TriggerEvent;
import com.oplus.multiapp.OplusMultiAppManager;
import com.oplus.osense.OsenseResClient;
import com.oplus.osense.info.OsenseNotifyRequest;
import com.oplus.splitscreen.OplusSplitScreenManager;
import com.oplus.util.OplusCommonConfig;
import com.oplus.util.OplusProcDependData;
import d6.ConfigUpdateUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import w5.OplusBatteryConstants;
import w6.PluginMethodCaller;
import w6.PluginSupporter;
import y5.AppFeature;

/* compiled from: CommonUtil.java */
/* renamed from: f6.c, reason: use source file name */
/* loaded from: classes.dex */
public class CommonUtil {

    /* renamed from: a, reason: collision with root package name */
    public static boolean f11386a = false;

    /* renamed from: b, reason: collision with root package name */
    private static List<String> f11387b;

    public static int A() {
        try {
            return Integer.parseInt(SystemProperties.get("sys.oplus.high.performance"));
        } catch (Exception e10) {
            LocalLog.d("CommonUtil", "fail to get HighPerformance SystemProperties exception e=" + e10);
            return 0;
        }
    }

    public static int B(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "high_performance_mode_on_when_shutdown", 0, 0);
    }

    public static boolean C(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "high_performance_mode_on", 0, 0) == 1;
    }

    public static ArrayList<String> D(Context context) {
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<ActivityManager.RunningAppProcessInfo> L = L(context);
        if (L != null) {
            Iterator<ActivityManager.RunningAppProcessInfo> it = L.iterator();
            while (it.hasNext()) {
                ActivityManager.RunningAppProcessInfo next = it.next();
                if (next.importance <= 200) {
                    for (String str : next.pkgList) {
                        arrayList.add(str);
                    }
                }
            }
        }
        return arrayList;
    }

    public static List<String> E(Context context) {
        ArrayList globalWhiteList = new OplusWhiteListManager(context).getGlobalWhiteList(10);
        LocalLog.a("CommonUtil", "getNoClearNotificationPkg: list=" + globalWhiteList);
        return globalWhiteList;
    }

    public static List<String> F(Context context) {
        return new OplusWhiteListManager(context).getGlobalWhiteList();
    }

    public static String G(String str) {
        return "" + str.substring(str.indexOf("[ ") + 2, str.lastIndexOf(" ]"));
    }

    public static int H(Context context) {
        if (context == null) {
            return 1;
        }
        int A = A();
        if (Settings.System.getIntForUser(context.getContentResolver(), "high_performance_mode_on", 0, 0) != A) {
            Settings.System.putIntForUser(context.getContentResolver(), "high_performance_mode_on", A, 0);
            h0(context, A);
        }
        if (A == 1) {
            return 2;
        }
        return Settings.System.getIntForUser(context.getContentResolver(), "performance_mode_enable", 1, 0) == 0 ? 0 : 1;
    }

    public static int I(Context context, String str) {
        if (str == null) {
            return -1;
        }
        if (str.contains("/")) {
            str = str.substring(0, str.indexOf("/"));
        } else if (str.contains(":")) {
            str = str.substring(0, str.indexOf(":"));
        }
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(str, 16384);
            if (packageInfo != null) {
                return packageInfo.versionCode;
            }
            return 0;
        } catch (PackageManager.NameNotFoundException e10) {
            LocalLog.b("CommonUtil", "" + e10);
            return 0;
        }
    }

    public static synchronized ArrayList<String> J() {
        Bundle configInfo;
        synchronized (CommonUtil.class) {
            ArrayList<String> arrayList = new ArrayList<>();
            try {
                try {
                    configInfo = OplusCommonConfig.getInstance().getConfigInfo("recent_lock_list", 1);
                } catch (NoSuchMethodError e10) {
                    LocalLog.b("CommonUtil", "get recent lock list get error: " + e10);
                }
            } catch (Exception e11) {
                LocalLog.b("CommonUtil", "get recent lock get error: " + e11);
                e11.printStackTrace();
            }
            if (configInfo == null) {
                return arrayList;
            }
            ArrayList<String> stringArrayList = configInfo.getStringArrayList("recent_lock_list");
            if (stringArrayList == null) {
                return arrayList;
            }
            for (int i10 = 0; i10 < stringArrayList.size(); i10++) {
                String str = stringArrayList.get(i10);
                if (str != null) {
                    if (str.contains("#")) {
                        String str2 = str.split("#")[0];
                        if (str2 != null && !str2.isEmpty()) {
                            arrayList.add(str2);
                            if (LocalLog.g()) {
                                LocalLog.a("CommonUtil", "getRecentLockList: 11 lock list:  " + str2);
                            }
                        }
                    } else {
                        arrayList.add(str);
                        if (LocalLog.g()) {
                            LocalLog.a("CommonUtil", "getRecentLockList: 22 lock list:  " + str);
                        }
                    }
                }
            }
            return arrayList;
        }
    }

    public static ArrayList<ActivityManager.RecentTaskInfo> K(Context context) {
        return (ArrayList) ((ActivityManager) context.getSystemService("activity")).getRecentTasks(60, 2);
    }

    public static ArrayList<ActivityManager.RunningAppProcessInfo> L(Context context) {
        return (ArrayList) ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
    }

    public static boolean M() {
        ArrayList<String> t7;
        File file = new File(OplusBatteryConstants.f19356h, "sys_extreme_deepsleep_list.xml");
        return (!file.exists() || (t7 = t(file, "SupersleepForceState")) == null || t7.isEmpty()) ? false : true;
    }

    public static ArrayList<String> N() {
        ArrayList<String> t7;
        File file = new File(OplusBatteryConstants.f19356h, "sys_extreme_deepsleep_list.xml");
        if (!file.exists() || (t7 = t(file, "SupersleepWhitelist")) == null || t7.isEmpty()) {
            return null;
        }
        return t7;
    }

    public static List<String> O() {
        if (f11387b == null) {
            if (AppFeature.D() && AppFeature.H()) {
                f11387b = new ArrayList(OplusBatteryConstants.f19360l);
            } else {
                f11387b = new ArrayList(OplusBatteryConstants.f19359k);
            }
        }
        return f11387b;
    }

    public static int P(Context context, String str) {
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(str, 128);
        } catch (PackageManager.NameNotFoundException e10) {
            LocalLog.d("CommonUtil", "failed get uid for package  " + e10);
            applicationInfo = null;
        }
        if (applicationInfo != null) {
            return applicationInfo.uid;
        }
        return -1;
    }

    public static void Q(Context context) {
        int H = H(context);
        LocalLog.a("CommonUtil", "init perf mode = " + H);
        j0(context, H);
    }

    public static boolean R(Context context, String str) {
        ApplicationInfo applicationInfo;
        String substring;
        int i10;
        if (str == null || str.isEmpty()) {
            return false;
        }
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo2 = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(str, 128);
        } catch (PackageManager.NameNotFoundException unused) {
            applicationInfo = null;
        }
        boolean z10 = applicationInfo != null;
        if (!z10 && str.contains(":") && (substring = str.substring(0, str.indexOf(":"))) != null && !substring.isEmpty()) {
            try {
                i10 = packageManager.getUidForSharedUser(substring);
            } catch (PackageManager.NameNotFoundException unused2) {
                i10 = -1;
            }
            if (i10 != -1) {
                z10 = true;
            }
            if (!z10) {
                try {
                    applicationInfo2 = packageManager.getApplicationInfo(substring, 128);
                } catch (PackageManager.NameNotFoundException unused3) {
                }
                if (applicationInfo2 != null) {
                    return true;
                }
            }
        }
        return z10;
    }

    public static boolean S(Context context, String str) {
        if (str != null && !"".equals(str)) {
            PackageManager packageManager = context.getPackageManager();
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            packageManager.getPreferredActivities(arrayList, arrayList2, str);
            if (arrayList2.size() > 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean T(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "device_provisioned", 0) != 1;
    }

    public static boolean U() {
        try {
            return OplusSplitScreenManager.getInstance().isInSplitScreenMode();
        } catch (Error | Exception e10) {
            e10.printStackTrace();
            return false;
        }
    }

    public static boolean V() {
        return ActivityManager.isUserAMonkey();
    }

    public static boolean W(String str) {
        return OplusMultiAppManager.getInstance().getMultiAppList(0).contains(str);
    }

    public static boolean X(Context context, String str) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(str, 128);
        } catch (PackageManager.NameNotFoundException unused) {
            packageInfo = null;
        }
        return (packageInfo == null || (packageInfo.applicationInfo.flags & 1) == 0) ? false : true;
    }

    public static void Y(Context context, int i10, String str, String str2) {
        try {
            Intent intent = new Intent("oplus.intent.action.REQUEST_CLEAR_SPEC_APP");
            intent.setPackage("com.oplus.athena");
            intent.putExtra("pid", i10);
            intent.putExtra("p_name", str);
            intent.putExtra("caller_package", context.getPackageName() + "." + str2);
            intent.putExtra("reason", str2);
            intent.putExtra("type", 12);
            context.startService(intent);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
    }

    public static void Z(Context context, String str, String str2) {
        try {
            Intent intent = new Intent("oplus.intent.action.REQUEST_CLEAR_SPEC_APP");
            intent.setPackage("com.oplus.athena");
            intent.putExtra("p_name", str);
            intent.putExtra("caller_package", context.getPackageName() + "." + str2);
            intent.putExtra("reason", str2);
            intent.putExtra("type", 12);
            context.startService(intent);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
    }

    private static void a(ArrayList<String> arrayList, ArrayList<String> arrayList2) {
        if (arrayList == null || arrayList2 == null) {
            return;
        }
        for (int i10 = 0; i10 < arrayList2.size(); i10++) {
            String str = arrayList2.get(i10);
            if (!arrayList.contains(str)) {
                arrayList.add(str);
            }
        }
    }

    public static void a0(Context context, int i10, String str, String str2) {
        d0(context, 12, i10, str, str2);
    }

    public static void b(Context context) {
        StackTraceElement[] stackTrace;
        NotifyUtil.v(context).j();
        Settings.System.putIntForUser(context.getContentResolver(), "high_performance_mode_on", 0, 0);
        h0(context, 0);
        g0("0");
        i0(context, false);
        LocalLog.a("CommonUtil", "closeHighPerformanceMode " + context.getClass());
        LocalLog.a("CommonUtil", "closeHighPerformanceMode for user:" + UserHandle.myUserId());
        if (!LocalLog.f() || (stackTrace = new Throwable().getStackTrace()) == null) {
            return;
        }
        for (int i10 = 0; i10 < 5 && i10 < stackTrace.length; i10++) {
            LocalLog.d("CommonUtil", "closeHighPerformanceMode  |----" + stackTrace[i10].toString());
        }
    }

    public static void b0(Context context) {
        Settings.System.putIntForUser(context.getContentResolver(), "high_performance_mode_on", 1, 0);
        h0(context, 1);
        g0("1");
        boolean equals = "1".equals(SystemProperties.get("persist.camera.endurance.enter"));
        i0(context, !equals);
        if (LocalLog.f()) {
            LocalLog.a("CommonUtil", "openHighPerformanceMode " + context.getClass() + " enduranceScene:" + equals);
        }
        NotifyUtil.v(context).C();
    }

    public static void c(Context context) {
        try {
            Object systemService = context.getSystemService("statusbar");
            systemService.getClass().getMethod("collapsePanels", new Class[0]).invoke(systemService, new Object[0]);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e10) {
            LocalLog.b("CommonUtil", e10.toString());
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0037, code lost:
    
        if (r1 == null) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0033, code lost:
    
        if (r1 == null) goto L23;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static int c0(String str, String str2) {
        BufferedReader bufferedReader;
        Throwable th;
        File file = new File(str, str2);
        if (!file.exists()) {
            return -1;
        }
        String str3 = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            try {
                str3 = bufferedReader.readLine();
            } catch (FileNotFoundException unused) {
            } catch (IOException unused2) {
            } catch (Throwable th2) {
                th = th2;
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException unused3) {
                    }
                }
                throw th;
            }
        } catch (FileNotFoundException unused4) {
            bufferedReader = null;
        } catch (IOException unused5) {
            bufferedReader = null;
        } catch (Throwable th3) {
            bufferedReader = null;
            th = th3;
        }
        try {
            bufferedReader.close();
        } catch (IOException unused6) {
            if (str3 == null) {
                return -1;
            }
            try {
                return Integer.parseInt(str3.trim());
            } catch (NumberFormatException unused7) {
                return -1;
            }
        }
    }

    public static void d(Context context, String str, String str2) {
        try {
            Intent intent = new Intent("oplus.intent.action.REQUEST_CLEAR_SPEC_APP");
            intent.setPackage("com.oplus.athena");
            intent.putExtra("user_id", context.getUserId());
            intent.putExtra("p_name", str);
            intent.putExtra("caller_package", context.getPackageName() + "." + str2);
            intent.putExtra("reason", str2);
            intent.putExtra("type", 13);
            context.startService(intent);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
    }

    private static void d0(Context context, int i10, int i11, String str, String str2) {
        try {
            Intent intent = new Intent("oplus.intent.action.REQUEST_CLEAR_SPEC_APP");
            intent.setPackage("com.oplus.athena");
            intent.putExtra("caller_package", context.getPackageName() + "." + str2);
            intent.putExtra(TriggerEvent.EXTRA_UID, i11);
            intent.putExtra("p_name", str);
            intent.putExtra("type", i10);
            intent.putExtra("reason", str2);
            context.startService(intent);
        } catch (Exception e10) {
            LocalLog.b("CommonUtil", "Exception: " + e10);
        }
    }

    public static void e(Context context, int i10, String str, String str2) {
        d0(context, 13, i10, str, str2);
    }

    public static void e0(Context context) {
        if (context == null) {
            return;
        }
        int H = H(context);
        if (H == 2) {
            H = 1;
        }
        LocalLog.a("CommonUtil", "resetPerformanceMode mode = " + H);
        j0(context, H);
    }

    public static String f(Context context, double d10, boolean z10) {
        int i10;
        int i11;
        int i12;
        StringBuilder sb2 = new StringBuilder();
        int floor = (int) Math.floor(d10 / 1000.0d);
        if (!z10) {
            floor += 30;
        }
        if (floor >= 86400) {
            i10 = floor / 86400;
            floor -= 86400 * i10;
        } else {
            i10 = 0;
        }
        if (floor >= 3600) {
            i11 = floor / 3600;
            floor -= i11 * 3600;
        } else {
            i11 = 0;
        }
        if (floor >= 60) {
            i12 = floor / 60;
            floor -= i12 * 60;
        } else {
            i12 = 0;
        }
        if (z10) {
            if (i10 > 0) {
                sb2.append(context.getString(R.string.battery_history_days, Integer.valueOf(i10), 0, 0, 0));
            } else if (i11 > 0) {
                if (i11 == 23 && i12 == 59) {
                    sb2.append(context.getString(R.string.battery_history_days, 1, 0, 0, 0));
                } else {
                    sb2.append(context.getString(R.string.battery_history_hours, Integer.valueOf(i11), Integer.valueOf(i12), Integer.valueOf(floor)));
                }
            } else if (i12 > 0) {
                sb2.append(context.getString(R.string.battery_history_minutes, Integer.valueOf(i12), Integer.valueOf(floor)));
            } else {
                sb2.append(context.getString(R.string.battery_history_seconds, Integer.valueOf(floor)));
            }
        } else if (i10 > 0) {
            sb2.append(context.getString(R.string.battery_history_days_no_seconds, Integer.valueOf(i10), Integer.valueOf(i11), Integer.valueOf(i12)));
        } else if (i11 > 0) {
            sb2.append(context.getString(R.string.battery_history_hours_no_seconds, Integer.valueOf(i11), Integer.valueOf(i12)));
        } else {
            sb2.append(context.getString(R.string.battery_history_minutes_no_seconds, Integer.valueOf(i12)));
        }
        return sb2.toString();
    }

    public static void f0(boolean z10) {
        f11386a = z10;
    }

    private static String g(double d10) {
        return NumberFormat.getPercentInstance().format(d10);
    }

    public static void g0(String str) {
        try {
            SystemProperties.set("sys.oplus.high.performance", str);
        } catch (Exception e10) {
            LocalLog.d("CommonUtil", "fail to set HighPerformance SystemProperties exception e=" + e10);
        }
    }

    public static String h(int i10) {
        return g(i10 / 100.0d);
    }

    public static void h0(Context context, int i10) {
        Settings.System.putIntForUser(context.getApplicationContext().getContentResolver(), "high_performance_mode_on_when_shutdown", i10, 0);
    }

    public static String i(long j10) {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()).format((Date) new java.sql.Date(j10));
    }

    public static void i0(Context context, boolean z10) {
        OsenseNotifyRequest osenseNotifyRequest = new OsenseNotifyRequest(2, z10 ? 1 : 0);
        OsenseResClient osenseResClient = OsenseResClient.get(context.getClass());
        if (osenseResClient != null) {
            osenseResClient.osenseSetNotification(osenseNotifyRequest);
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(z10 ? "open" : "close");
        sb2.append(" high performance mode: osenseClient is null");
        LocalLog.b("CommonUtil", sb2.toString());
    }

    public static final void j(StringBuilder sb2, long j10) {
        long j11 = j10 / 1000;
        k(sb2, j11);
        sb2.append(j10 - (j11 * 1000));
        sb2.append("ms ");
    }

    public static void j0(Context context, int i10) {
        if (context == null) {
            LocalLog.b("CommonUtil", "setPerformanceMode null");
            return;
        }
        if (i10 == 0) {
            Settings.System.putIntForUser(context.getContentResolver(), "performance_mode_enable", 0, 0);
            b(context);
            PluginMethodCaller n10 = PluginSupporter.m().n();
            if (n10 != null) {
                n10.b(OplusBatteryConstants.f19358j.booleanValue());
                return;
            }
            return;
        }
        Settings.System.putIntForUser(context.getContentResolver(), "performance_mode_enable", 1, 0);
        if (i10 == 2) {
            b0(context);
        } else {
            b(context);
        }
    }

    private static final void k(StringBuilder sb2, long j10) {
        long j11 = j10 / 86400;
        if (j11 != 0) {
            sb2.append(j11);
            sb2.append("d ");
        }
        long j12 = j11 * 60 * 60 * 24;
        long j13 = (j10 - j12) / 3600;
        if (j13 != 0 || j12 != 0) {
            sb2.append(j13);
            sb2.append("h ");
        }
        long j14 = j12 + (j13 * 3600);
        long j15 = (j10 - j14) / 60;
        if (j15 != 0 || j14 != 0) {
            sb2.append(j15);
            sb2.append("m ");
        }
        long j16 = j14 + (j15 * 60);
        if (j10 == 0 && j16 == 0) {
            return;
        }
        sb2.append(j10 - j16);
        sb2.append("s ");
    }

    public static String l(Context context) {
        String parameters = ((AudioManager) context.getSystemService("audio")).getParameters("get_pid");
        if (parameters == null || parameters.length() == 0) {
            return null;
        }
        return parameters;
    }

    public static List<String> m(Context context) {
        List list;
        OplusActivityManager oplusActivityManager = new OplusActivityManager();
        ArrayList arrayList = new ArrayList();
        try {
            list = oplusActivityManager.getAllTopAppInfos();
        } catch (Exception e10) {
            LocalLog.c("CommonUtil", "getAllTopAppInfos exception", e10);
            list = null;
        }
        if (list == null) {
            return arrayList;
        }
        for (int i10 = 0; i10 < list.size(); i10++) {
            try {
                String str = ((OplusAppInfo) list.get(i10)).appInfo.packageName;
                if (str != null && !"".equals(str)) {
                    arrayList.add(str);
                }
            } catch (Exception e11) {
                LocalLog.c("CommonUtil", "getAllTopAppInfos get pkgName exception", e11);
            }
        }
        if (LocalLog.g()) {
            LocalLog.a("CommonUtil", "getAllTopPkgName: listTopPkg=" + arrayList + ", listTopPkgTmp=" + list);
        }
        return arrayList;
    }

    public static ArrayList<String> n(String str, int i10) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            List procDependency = new OplusActivityManager().getProcDependency(str, i10);
            if (procDependency != null && !procDependency.isEmpty()) {
                Iterator it = procDependency.iterator();
                while (it.hasNext()) {
                    for (OplusProcDependData.ProcItem procItem : ((OplusProcDependData) it.next()).mServices) {
                        if (!arrayList.contains(procItem.packageName)) {
                            arrayList.add(procItem.packageName);
                        }
                    }
                }
            }
        } catch (Exception e10) {
            LocalLog.a("CommonUtil", "get top app depend list error: " + e10);
        } catch (NoSuchMethodError e11) {
            LocalLog.a("CommonUtil", "get top app depend list error: " + e11);
        }
        return arrayList;
    }

    public static String o(Context context, String str) {
        ApplicationInfo applicationInfo;
        PackageManager packageManager = context.getPackageManager();
        try {
            applicationInfo = packageManager.getApplicationInfo(str, 128);
        } catch (PackageManager.NameNotFoundException e10) {
            LocalLog.d("CommonUtil", "failed getAppLabel " + e10);
            applicationInfo = null;
        }
        if (applicationInfo == null) {
            return "";
        }
        return "" + packageManager.getApplicationLabel(applicationInfo).toString();
    }

    public static ArrayList<String> p(Context context) {
        ArrayList<String> n10;
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<ActivityManager.RunningAppProcessInfo> L = L(context);
        String l10 = l(context);
        LocalLog.a("CommonUtil", "audio pids: " + l10);
        if (l10 != null) {
            String[] split = l10.split(":");
            int length = split.length;
            if (length > 1) {
                List<String> E = E(context);
                for (int i10 = 1; i10 < length; i10++) {
                    int parseInt = Integer.parseInt(split[i10].toString());
                    int i11 = 0;
                    while (true) {
                        if (i11 < L.size()) {
                            ActivityManager.RunningAppProcessInfo runningAppProcessInfo = L.get(i11);
                            if (!runningAppProcessInfo.processName.equals("system") && parseInt == runningAppProcessInfo.pid) {
                                int i12 = 0;
                                while (true) {
                                    String[] strArr = runningAppProcessInfo.pkgList;
                                    if (i12 < strArr.length) {
                                        String str = strArr[i12];
                                        if ("unKnow".equals(str)) {
                                            arrayList.add("unKnow");
                                        } else if (E != null && E.contains(str)) {
                                            arrayList.add(str);
                                            LocalLog.a("CommonUtil", "getAudioList: add audio list: " + str);
                                        }
                                        i12++;
                                    }
                                }
                            } else {
                                i11++;
                            }
                        }
                    }
                }
            }
        }
        if (arrayList.contains("unKnow") && (n10 = n("unKnow", UserHandle.myUserId())) != null) {
            for (int i13 = 0; i13 < n10.size(); i13++) {
                String str2 = n10.get(i13);
                if (!arrayList.contains(str2)) {
                    arrayList.add(str2);
                    LocalLog.a("CommonUtil", " getAudioList: using audio package: " + str2);
                }
            }
        }
        return arrayList;
    }

    public static ArrayList<String> q(Context context) {
        ArrayList<String> n10;
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<ActivityManager.RunningAppProcessInfo> L = L(context);
        String l10 = l(context);
        LocalLog.a("CommonUtil", "audio pids: " + l10);
        if (l10 != null) {
            String[] split = l10.split(":");
            int length = split.length;
            if (length > 1) {
                for (int i10 = 1; i10 < length; i10++) {
                    int parseInt = Integer.parseInt(split[i10].toString());
                    int i11 = 0;
                    while (true) {
                        if (i11 < L.size()) {
                            ActivityManager.RunningAppProcessInfo runningAppProcessInfo = L.get(i11);
                            if (!runningAppProcessInfo.processName.equals("system") && parseInt == runningAppProcessInfo.pid) {
                                int i12 = 0;
                                while (true) {
                                    String[] strArr = runningAppProcessInfo.pkgList;
                                    if (i12 < strArr.length) {
                                        String str = strArr[i12];
                                        if ("unKnow".equals(str)) {
                                            arrayList.add("unKnow");
                                        } else {
                                            arrayList.add(str);
                                            LocalLog.a("CommonUtil", "getAudioListNoNotificaion: add audio list: " + str);
                                        }
                                        i12++;
                                    }
                                }
                            } else {
                                i11++;
                            }
                        }
                    }
                }
            }
        }
        if (arrayList.contains("unKnow") && (n10 = n("unKnow", UserHandle.myUserId())) != null) {
            for (int i13 = 0; i13 < n10.size(); i13++) {
                String str2 = n10.get(i13);
                if (!arrayList.contains(str2)) {
                    arrayList.add(str2);
                    LocalLog.a("CommonUtil", " getAudioList: using audio package: " + str2);
                }
            }
        }
        return arrayList;
    }

    public static long r(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return (memoryInfo.availMem / 1024) / 1024;
    }

    public static int s() {
        return c0("/sys/class/power_supply/battery", "batt_fcc");
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x0044, code lost:
    
        r7 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0045, code lost:
    
        r8 = new java.lang.StringBuilder();
        r4 = r4;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static ArrayList<String> t(File file, String str) {
        StringBuilder sb2;
        FileInputStream fileInputStream;
        int next;
        String nextText;
        ArrayList<String> arrayList = new ArrayList<>();
        if (str == null || str.isEmpty()) {
            return arrayList;
        }
        FileInputStream fileInputStream2 = null;
        FileInputStream fileInputStream3 = null;
        FileInputStream fileInputStream4 = null;
        FileInputStream fileInputStream5 = null;
        FileInputStream fileInputStream6 = null;
        FileInputStream fileInputStream7 = null;
        try {
            try {
                FileInputStream fileInputStream8 = new FileInputStream(file);
                try {
                    XmlPullParser newPullParser = Xml.newPullParser();
                    newPullParser.setInput(fileInputStream8, null);
                    do {
                        next = newPullParser.next();
                        if (next == 2 && str.equals(newPullParser.getName()) && (nextText = newPullParser.nextText()) != null) {
                            arrayList.add(nextText);
                        }
                    } while (next != 1);
                    fileInputStream8.close();
                    fileInputStream2 = next;
                } catch (IOException e10) {
                    e = e10;
                    fileInputStream3 = fileInputStream8;
                    LocalLog.a("CommonUtil", "failed IOException " + e);
                    fileInputStream2 = fileInputStream3;
                    if (fileInputStream3 != null) {
                        try {
                            fileInputStream3.close();
                            fileInputStream2 = fileInputStream3;
                        } catch (IOException e11) {
                            e = e11;
                            sb2 = new StringBuilder();
                            fileInputStream = fileInputStream3;
                            sb2.append("Failed to close state FileInputStream ");
                            sb2.append(e);
                            LocalLog.a("CommonUtil", sb2.toString());
                            fileInputStream2 = fileInputStream;
                            return arrayList;
                        }
                    }
                    return arrayList;
                } catch (IndexOutOfBoundsException e12) {
                    e = e12;
                    fileInputStream4 = fileInputStream8;
                    LocalLog.a("CommonUtil", "failed parsing " + e);
                    fileInputStream2 = fileInputStream4;
                    if (fileInputStream4 != null) {
                        try {
                            fileInputStream4.close();
                            fileInputStream2 = fileInputStream4;
                        } catch (IOException e13) {
                            e = e13;
                            sb2 = new StringBuilder();
                            fileInputStream = fileInputStream4;
                            sb2.append("Failed to close state FileInputStream ");
                            sb2.append(e);
                            LocalLog.a("CommonUtil", sb2.toString());
                            fileInputStream2 = fileInputStream;
                            return arrayList;
                        }
                    }
                    return arrayList;
                } catch (NullPointerException e14) {
                    e = e14;
                    fileInputStream5 = fileInputStream8;
                    LocalLog.a("CommonUtil", "failed parsing " + e);
                    fileInputStream2 = fileInputStream5;
                    if (fileInputStream5 != null) {
                        try {
                            fileInputStream5.close();
                            fileInputStream2 = fileInputStream5;
                        } catch (IOException e15) {
                            e = e15;
                            sb2 = new StringBuilder();
                            fileInputStream = fileInputStream5;
                            sb2.append("Failed to close state FileInputStream ");
                            sb2.append(e);
                            LocalLog.a("CommonUtil", sb2.toString());
                            fileInputStream2 = fileInputStream;
                            return arrayList;
                        }
                    }
                    return arrayList;
                } catch (NumberFormatException e16) {
                    e = e16;
                    fileInputStream6 = fileInputStream8;
                    LocalLog.a("CommonUtil", "failed parsing " + e);
                    fileInputStream2 = fileInputStream6;
                    if (fileInputStream6 != null) {
                        try {
                            fileInputStream6.close();
                            fileInputStream2 = fileInputStream6;
                        } catch (IOException e17) {
                            e = e17;
                            sb2 = new StringBuilder();
                            fileInputStream = fileInputStream6;
                            sb2.append("Failed to close state FileInputStream ");
                            sb2.append(e);
                            LocalLog.a("CommonUtil", sb2.toString());
                            fileInputStream2 = fileInputStream;
                            return arrayList;
                        }
                    }
                    return arrayList;
                } catch (XmlPullParserException e18) {
                    e = e18;
                    fileInputStream7 = fileInputStream8;
                    LocalLog.a("CommonUtil", "failed parsing " + e);
                    fileInputStream2 = fileInputStream7;
                    if (fileInputStream7 != null) {
                        try {
                            fileInputStream7.close();
                            fileInputStream2 = fileInputStream7;
                        } catch (IOException e19) {
                            e = e19;
                            sb2 = new StringBuilder();
                            fileInputStream = fileInputStream7;
                            sb2.append("Failed to close state FileInputStream ");
                            sb2.append(e);
                            LocalLog.a("CommonUtil", sb2.toString());
                            fileInputStream2 = fileInputStream;
                            return arrayList;
                        }
                    }
                    return arrayList;
                } catch (Throwable th) {
                    th = th;
                    fileInputStream2 = fileInputStream8;
                    if (fileInputStream2 != null) {
                        try {
                            fileInputStream2.close();
                        } catch (IOException e20) {
                            LocalLog.a("CommonUtil", "Failed to close state FileInputStream " + e20);
                        }
                    }
                    throw th;
                }
            } catch (IOException e21) {
                e = e21;
            } catch (IndexOutOfBoundsException e22) {
                e = e22;
            } catch (NullPointerException e23) {
                e = e23;
            } catch (NumberFormatException e24) {
                e = e24;
            } catch (XmlPullParserException e25) {
                e = e25;
            }
            return arrayList;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static String u(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        Iterator<ResolveInfo> it = packageManager.queryIntentActivities(intent, 65536).iterator();
        while (it.hasNext()) {
            String str = it.next().activityInfo.packageName;
            if (S(context, str)) {
                return str;
            }
        }
        return "com.android.launcher";
    }

    public static String v(Context context) {
        ActivityInfo activityInfo;
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        ResolveInfo resolveActivity = packageManager.resolveActivity(intent, 65536);
        String str = (resolveActivity == null || (activityInfo = resolveActivity.activityInfo) == null) ? "com.android.launcher" : activityInfo.packageName;
        return str == null ? "com.android.launcher" : str;
    }

    public static Intent w(Context context, Intent intent) {
        List<ResolveInfo> queryIntentServices = context.getPackageManager().queryIntentServices(intent, 0);
        if (queryIntentServices != null && queryIntentServices.size() == 1) {
            ServiceInfo serviceInfo = queryIntentServices.get(0).serviceInfo;
            ComponentName componentName = new ComponentName(serviceInfo.packageName, serviceInfo.name);
            Intent intent2 = new Intent(intent);
            intent2.setComponent(componentName);
            return intent2;
        }
        LocalLog.d("CommonUtil", "getExplicitIntent, resolveInfo == null or more than one service have same intent action, return null");
        return null;
    }

    public static ArrayList<String> x(Context context) {
        Intent intent;
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<ActivityManager.RecentTaskInfo> K = K(context);
        if (K == null) {
            return null;
        }
        int size = K.size() < ConfigUpdateUtil.n(context).q() ? K.size() : ConfigUpdateUtil.n(context).q();
        if (size <= 0) {
            return null;
        }
        String v7 = v(context);
        for (int i10 = 0; i10 < K.size() && size > 0; i10++) {
            ActivityManager.RecentTaskInfo recentTaskInfo = K.get(i10);
            if (recentTaskInfo != null && (intent = recentTaskInfo.baseIntent) != null && intent.getComponent() != null) {
                String packageName = recentTaskInfo.baseIntent.getComponent().getPackageName();
                if (!TextUtils.isEmpty(packageName) && !"com.android.systemui".equals(packageName) && (TextUtils.isEmpty(v7) || !v7.equals(packageName))) {
                    arrayList.add(packageName);
                    size--;
                    LocalLog.a("CommonUtil", "low mem clear keep recent task :  " + packageName + ", count: " + size);
                }
            }
        }
        List<String> list = OplusBatteryConstants.f19350b;
        if (list != null) {
            for (String str : list) {
                if (str != null && !arrayList.contains(str)) {
                    arrayList.add(str);
                }
            }
        }
        a(arrayList, p(context));
        return arrayList;
    }

    public static String y() {
        ComponentName componentName;
        try {
            componentName = new OplusActivityManager().getTopActivityComponentName();
        } catch (Exception e10) {
            LocalLog.a("CommonUtil", "failed getFrontPackageName " + e10);
            componentName = null;
        }
        String packageName = componentName != null ? componentName.getPackageName() : "";
        return packageName == null ? "" : packageName;
    }

    public static ArrayList<String> z(Context context, int i10) {
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList arrayList2 = (ArrayList) ((ActivityManager) context.getSystemService("activity")).getRecentTasks(i10, 2);
        if (arrayList2 != null && arrayList2.size() != 0) {
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                arrayList.add(((ActivityManager.RecentTaskInfo) it.next()).baseIntent.getComponent().getPackageName());
            }
        }
        return arrayList;
    }
}
