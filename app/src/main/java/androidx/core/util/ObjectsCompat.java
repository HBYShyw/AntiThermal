package androidx.core.util;

import java.util.Objects;

/* compiled from: ObjectsCompat.java */
/* renamed from: androidx.core.util.c, reason: use source file name */
/* loaded from: classes.dex */
public class ObjectsCompat {

    /* compiled from: ObjectsCompat.java */
    /* renamed from: androidx.core.util.c$a */
    /* loaded from: classes.dex */
    static class a {
        static boolean a(Object obj, Object obj2) {
            return Objects.equals(obj, obj2);
        }

        static int b(Object... objArr) {
            return Objects.hash(objArr);
        }
    }

    public static boolean a(Object obj, Object obj2) {
        return a.a(obj, obj2);
    }

    public static int b(Object... objArr) {
        return a.b(objArr);
    }

    public static <T> T c(T t7, String str) {
        Objects.requireNonNull(t7, str);
        return t7;
    }
}
