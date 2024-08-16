package kotlin.collections;

import java.util.Collections;
import java.util.Set;
import na.SetBuilder;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: SetsJVM.kt */
/* renamed from: kotlin.collections.r0, reason: use source file name */
/* loaded from: classes2.dex */
public class SetsJVM {
    public static <E> Set<E> a(Set<E> set) {
        za.k.e(set, "builder");
        return ((SetBuilder) set).e();
    }

    public static <E> Set<E> b() {
        return new SetBuilder();
    }

    public static <E> Set<E> c(int i10) {
        return new SetBuilder(i10);
    }

    public static <T> Set<T> d(T t7) {
        Set<T> singleton = Collections.singleton(t7);
        za.k.d(singleton, "singleton(element)");
        return singleton;
    }
}
