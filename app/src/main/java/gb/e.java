package gb;

/* compiled from: KClasses.kt */
/* loaded from: classes2.dex */
public final class e {
    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> T a(KClass<T> kClass, Object obj) {
        za.k.e(kClass, "<this>");
        if (kClass.y(obj)) {
            za.k.c(obj, "null cannot be cast to non-null type T of kotlin.reflect.KClasses.cast");
            return obj;
        }
        throw new ClassCastException("Value cannot be cast to " + kClass.u());
    }
}
