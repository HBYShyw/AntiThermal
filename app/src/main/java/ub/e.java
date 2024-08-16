package ub;

/* compiled from: ReflectJavaClassFinder.kt */
/* loaded from: classes2.dex */
public final class e {
    public static final Class<?> a(ClassLoader classLoader, String str) {
        za.k.e(classLoader, "<this>");
        za.k.e(str, "fqName");
        try {
            return Class.forName(str, false, classLoader);
        } catch (ClassNotFoundException unused) {
            return null;
        }
    }
}
