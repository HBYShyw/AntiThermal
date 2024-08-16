package pa;

import java.util.Comparator;
import za.k;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Comparisons.kt */
/* loaded from: classes2.dex */
public class b {
    public static <T extends Comparable<?>> int a(T t7, T t10) {
        if (t7 == t10) {
            return 0;
        }
        if (t7 == null) {
            return -1;
        }
        if (t10 == null) {
            return 1;
        }
        return t7.compareTo(t10);
    }

    public static <T extends Comparable<? super T>> Comparator<T> b() {
        e eVar = e.f16662e;
        k.c(eVar, "null cannot be cast to non-null type java.util.Comparator<T of kotlin.comparisons.ComparisonsKt__ComparisonsKt.naturalOrder>{ kotlin.TypeAliasesKt.Comparator<T of kotlin.comparisons.ComparisonsKt__ComparisonsKt.naturalOrder> }");
        return eVar;
    }
}
