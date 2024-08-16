package l9;

import android.os.Process;
import android.util.Log;

/* compiled from: LogUtils.java */
/* renamed from: l9.f, reason: use source file name */
/* loaded from: classes2.dex */
public class LogUtils {

    /* renamed from: a, reason: collision with root package name */
    private static String f14652a = "SceneSDK." + Process.myPid() + ".";

    /* renamed from: b, reason: collision with root package name */
    private static boolean f14653b = false;

    /* renamed from: c, reason: collision with root package name */
    private static int f14654c;

    public static void a(String str, String str2) {
        if (f14654c <= 3) {
            if (f14653b) {
                Log.d(f14652a + str, "(" + Thread.currentThread().getName() + ")" + str2);
                return;
            }
            Log.d(f14652a + str, str2);
        }
    }

    public static void b(String str, String str2) {
        if (f14654c <= 6) {
            if (f14653b) {
                Log.e(f14652a + str, "(" + Thread.currentThread().getName() + ")" + str2);
                return;
            }
            Log.e(f14652a + str, str2);
        }
    }

    public static void c(String str, String str2) {
        if (f14654c <= 5) {
            if (f14653b) {
                Log.w(f14652a + str, "(" + Thread.currentThread().getName() + ")" + str2);
                return;
            }
            Log.w(f14652a + str, str2);
        }
    }
}
