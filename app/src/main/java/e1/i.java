package e1;

import android.util.Log;

/* compiled from: LogUtil.java */
/* loaded from: classes.dex */
public class i {

    /* renamed from: a, reason: collision with root package name */
    private static final boolean f10958a;

    /* renamed from: b, reason: collision with root package name */
    private static final boolean f10959b;

    /* renamed from: c, reason: collision with root package name */
    private static final boolean f10960c;

    static {
        boolean c10 = c("persist.sys.assert.panic", false);
        f10958a = c10;
        boolean c11 = c("persist.sys.assert.enable", false);
        f10959b = c11;
        f10960c = c10 || c11;
    }

    public static void a(String str, String str2) {
        if (f10960c) {
            Log.d("crypto-android-sdk", str + ":" + str2);
        }
    }

    public static void b(String str, String str2) {
        if (f10960c) {
            Log.e("crypto-android-sdk", str + ":" + str2);
        }
    }

    private static boolean c(String str, boolean z10) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            Object invoke = cls.getMethod("getBoolean", String.class, Boolean.TYPE).invoke(cls, str, Boolean.valueOf(z10));
            return invoke != null ? ((Boolean) invoke).booleanValue() : z10;
        } catch (Exception e10) {
            e10.printStackTrace();
            return z10;
        }
    }

    public static void d(String str, String str2) {
        if (f10960c) {
            Log.w("crypto-android-sdk", str + ":" + str2);
        }
    }
}
