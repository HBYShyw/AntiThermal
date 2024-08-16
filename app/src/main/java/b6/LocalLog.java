package b6;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;

/* compiled from: LocalLog.java */
/* renamed from: b6.a, reason: use source file name */
/* loaded from: classes.dex */
public class LocalLog {

    /* renamed from: a, reason: collision with root package name */
    private static boolean f4571a = SystemProperties.getBoolean("persist.sys.assert.panic", false);

    /* renamed from: b, reason: collision with root package name */
    private static boolean f4572b = SystemProperties.getBoolean("persist.sys.assert.enable", false);

    /* renamed from: c, reason: collision with root package name */
    private static boolean f4573c;

    /* renamed from: d, reason: collision with root package name */
    private static boolean f4574d;

    /* renamed from: e, reason: collision with root package name */
    private static boolean f4575e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: LocalLog.java */
    /* renamed from: b6.a$a */
    /* loaded from: classes.dex */
    public class a extends ContentObserver {
        a(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            LocalLog.j();
        }
    }

    static {
        boolean isLoggable = Log.isLoggable("Battery", 3);
        f4573c = isLoggable;
        boolean z10 = f4571a;
        f4574d = z10 || f4572b || isLoggable;
        f4575e = z10 || f4572b || isLoggable;
        Log.i("Battery", "LocalLog, IS_QE_LOG_ON = " + f4571a + ", IS_QE_LOG_ON_MTK = " + f4572b + ", isDebugTagOn = " + f4573c);
    }

    public static void a(String str, String str2) {
        if (f4574d) {
            Log.d("Battery", str + ": " + str2);
        }
    }

    public static void b(String str, String str2) {
        Log.e("Battery", str + ": " + str2);
    }

    public static void c(String str, String str2, Throwable th) {
        Log.e("Battery" + str, str2, th);
    }

    public static void d(String str, String str2) {
        if (f4574d) {
            Log.i("Battery", str + ": " + str2);
        }
    }

    public static void e(Context context) {
        h(context);
    }

    public static boolean f() {
        return f4574d;
    }

    public static boolean g() {
        return f4575e;
    }

    public static void h(Context context) {
        if (context == null) {
            return;
        }
        Log.d("Battery", "[observeLogSwitchChange] observe log switch");
        ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver != null) {
            contentResolver.registerContentObserver(Settings.System.getUriFor("log_switch_type"), true, new a(new Handler(Looper.getMainLooper())));
        }
    }

    public static void i(boolean z10) {
        f4574d = z10;
    }

    public static void j() {
        f4571a = SystemProperties.getBoolean("persist.sys.assert.panic", false);
        f4572b = SystemProperties.getBoolean("persist.sys.assert.enable", false);
        boolean isLoggable = Log.isLoggable("Battery", 3);
        f4573c = isLoggable;
        f4574d = f4571a || f4572b || isLoggable;
        Log.d("Battery", "LocalLog, IS_QE_LOG_ON = " + f4571a + ", IS_QE_LOG_ON_MTK = " + f4572b + ", isDebugTagOn = " + f4573c);
    }

    public static void k(String str, String str2) {
        if (f4574d) {
            Log.v("Battery", str + ": " + str2);
        }
    }

    public static void l(String str, String str2) {
        Log.w("Battery", str + ": " + str2);
    }

    public static void m(String str, String str2, Throwable th) {
        Log.w("Battery" + str, str2, th);
    }
}
