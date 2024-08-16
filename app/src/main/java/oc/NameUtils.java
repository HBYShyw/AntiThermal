package oc;

/* compiled from: NameUtils.kt */
/* renamed from: oc.g, reason: use source file name */
/* loaded from: classes2.dex */
public final class NameUtils {

    /* renamed from: a, reason: collision with root package name */
    public static final NameUtils f16444a = new NameUtils();

    /* renamed from: b, reason: collision with root package name */
    private static final sd.j f16445b = new sd.j("[^\\p{L}\\p{Digit}]");

    private NameUtils() {
    }

    public static final String a(String str) {
        za.k.e(str, "name");
        return f16445b.c(str, "_");
    }
}
