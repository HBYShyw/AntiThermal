package f0;

import android.annotation.SuppressLint;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* compiled from: Trace.java */
/* renamed from: f0.a, reason: use source file name */
/* loaded from: classes.dex */
public final class Trace {

    /* renamed from: a, reason: collision with root package name */
    private static long f11254a;

    /* renamed from: b, reason: collision with root package name */
    private static Method f11255b;

    public static void a(String str) {
        TraceApi18Impl.a(str);
    }

    public static void b() {
        TraceApi18Impl.b();
    }

    private static void c(String str, Exception exc) {
        if (exc instanceof InvocationTargetException) {
            Throwable cause = exc.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            throw new RuntimeException(cause);
        }
        Log.v("Trace", "Unable to call " + str + " via reflection", exc);
    }

    @SuppressLint({"NewApi"})
    public static boolean d() {
        try {
            if (f11255b == null) {
                return android.os.Trace.isEnabled();
            }
        } catch (NoClassDefFoundError | NoSuchMethodError unused) {
        }
        return e();
    }

    private static boolean e() {
        try {
            if (f11255b == null) {
                f11254a = android.os.Trace.class.getField("TRACE_TAG_APP").getLong(null);
                f11255b = android.os.Trace.class.getMethod("isTagEnabled", Long.TYPE);
            }
            return ((Boolean) f11255b.invoke(null, Long.valueOf(f11254a))).booleanValue();
        } catch (Exception e10) {
            c("isTagEnabled", e10);
            return false;
        }
    }
}
