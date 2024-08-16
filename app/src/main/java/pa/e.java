package pa;

import java.util.Comparator;
import za.k;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Comparisons.kt */
/* loaded from: classes2.dex */
public final class e implements Comparator<Comparable<? super Object>> {

    /* renamed from: e, reason: collision with root package name */
    public static final e f16662e = new e();

    private e() {
    }

    @Override // java.util.Comparator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(Comparable<Object> comparable, Comparable<Object> comparable2) {
        k.e(comparable, "a");
        k.e(comparable2, "b");
        return comparable.compareTo(comparable2);
    }

    @Override // java.util.Comparator
    public final Comparator<Comparable<? super Object>> reversed() {
        return f.f16663e;
    }
}
