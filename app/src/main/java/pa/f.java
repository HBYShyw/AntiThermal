package pa;

import java.util.Comparator;
import za.k;

/* compiled from: Comparisons.kt */
/* loaded from: classes2.dex */
final class f implements Comparator<Comparable<? super Object>> {

    /* renamed from: e, reason: collision with root package name */
    public static final f f16663e = new f();

    private f() {
    }

    @Override // java.util.Comparator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(Comparable<Object> comparable, Comparable<Object> comparable2) {
        k.e(comparable, "a");
        k.e(comparable2, "b");
        return comparable2.compareTo(comparable);
    }

    @Override // java.util.Comparator
    public final Comparator<Comparable<? super Object>> reversed() {
        return e.f16662e;
    }
}
