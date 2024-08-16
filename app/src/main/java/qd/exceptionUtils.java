package qd;

import za.k;

/* compiled from: exceptionUtils.kt */
/* renamed from: qd.c, reason: use source file name */
/* loaded from: classes2.dex */
public final class exceptionUtils {
    public static final boolean a(Throwable th) {
        k.e(th, "<this>");
        Class<?> cls = th.getClass();
        while (!k.a(cls.getCanonicalName(), "com.intellij.openapi.progress.ProcessCanceledException")) {
            cls = cls.getSuperclass();
            if (cls == null) {
                return false;
            }
        }
        return true;
    }

    public static final RuntimeException b(Throwable th) {
        k.e(th, "e");
        throw th;
    }
}
