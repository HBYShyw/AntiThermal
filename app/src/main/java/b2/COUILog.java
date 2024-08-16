package b2;

import android.util.Log;

/* compiled from: COUILog.java */
/* renamed from: b2.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUILog {

    /* renamed from: a, reason: collision with root package name */
    public static final boolean f4542a;

    /* renamed from: b, reason: collision with root package name */
    public static final boolean f4543b;

    /* renamed from: c, reason: collision with root package name */
    public static final boolean f4544c;

    /* renamed from: d, reason: collision with root package name */
    public static final boolean f4545d;

    /* renamed from: e, reason: collision with root package name */
    public static final boolean f4546e;

    /* renamed from: f, reason: collision with root package name */
    public static final boolean f4547f;

    /* renamed from: g, reason: collision with root package name */
    public static final boolean f4548g;

    static {
        boolean isLoggable = Log.isLoggable("COUI", 2);
        f4542a = isLoggable;
        boolean isLoggable2 = Log.isLoggable("COUI", 3);
        f4543b = isLoggable2;
        boolean isLoggable3 = Log.isLoggable("COUI", 4);
        f4544c = isLoggable3;
        boolean isLoggable4 = Log.isLoggable("COUI", 5);
        f4545d = isLoggable4;
        boolean isLoggable5 = Log.isLoggable("COUI", 6);
        f4546e = isLoggable5;
        boolean isLoggable6 = Log.isLoggable("COUI", 7);
        f4547f = isLoggable6;
        f4548g = (isLoggable || isLoggable2 || isLoggable3 || isLoggable4 || isLoggable5 || isLoggable6) ? false : true;
    }

    public static void a(String str, String str2) {
        Log.d(str, str2);
    }

    public static void b(String str, String str2) {
        Log.e(str, str2);
    }

    public static void c(String str, String str2) {
        Log.i(str, str2);
    }

    public static boolean d(String str, int i10) {
        return Log.isLoggable(str, i10);
    }
}
