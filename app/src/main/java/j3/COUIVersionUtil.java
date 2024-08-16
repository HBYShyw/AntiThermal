package j3;

import android.util.Log;

/* compiled from: COUIVersionUtil.java */
/* renamed from: j3.b, reason: use source file name */
/* loaded from: classes.dex */
public class COUIVersionUtil {

    /* renamed from: a, reason: collision with root package name */
    private static String f12948a;

    /* renamed from: b, reason: collision with root package name */
    private static String f12949b;

    private static boolean a() {
        try {
            Class.forName("com.oplus.os.OplusBuild");
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public static int b() {
        f12948a = a() ? "com.oplus.os.OplusBuild" : a.c().d();
        f12949b = a() ? "getOplusOSVERSION" : a.c().e();
        try {
            Class<?> cls = Class.forName(f12948a);
            return ((Integer) cls.getDeclaredMethod(f12949b, new Class[0]).invoke(cls, new Object[0])).intValue();
        } catch (Exception e10) {
            Log.e("COUIVersionUtil", "getOSVersionCode failed. error = " + e10.getMessage());
            return 0;
        }
    }
}
