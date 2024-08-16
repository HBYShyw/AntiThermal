package androidx.core.util;

import java.util.Objects;

/* compiled from: Preconditions.java */
/* renamed from: androidx.core.util.h, reason: use source file name */
/* loaded from: classes.dex */
public final class Preconditions {
    public static void a(boolean z10, Object obj) {
        if (!z10) {
            throw new IllegalArgumentException(String.valueOf(obj));
        }
    }

    public static int b(int i10) {
        if (i10 >= 0) {
            return i10;
        }
        throw new IllegalArgumentException();
    }

    public static int c(int i10, String str) {
        if (i10 >= 0) {
            return i10;
        }
        throw new IllegalArgumentException(str);
    }

    public static <T> T d(T t7) {
        Objects.requireNonNull(t7);
        return t7;
    }

    public static <T> T e(T t7, Object obj) {
        if (t7 != null) {
            return t7;
        }
        throw new NullPointerException(String.valueOf(obj));
    }

    public static void f(boolean z10, String str) {
        if (!z10) {
            throw new IllegalStateException(str);
        }
    }
}
