package pd;

import java.util.Collection;
import java.util.LinkedHashSet;
import qd.SmartList;
import za.k;
import zc.h;

/* compiled from: scopeUtils.kt */
/* renamed from: pd.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class scopeUtils {
    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> Collection<T> a(Collection<? extends T> collection, Collection<? extends T> collection2) {
        k.e(collection2, "collection");
        if (collection2.isEmpty()) {
            return collection;
        }
        if (collection == 0) {
            return collection2;
        }
        if (collection instanceof LinkedHashSet) {
            ((LinkedHashSet) collection).addAll(collection2);
            return collection;
        }
        LinkedHashSet linkedHashSet = new LinkedHashSet(collection);
        linkedHashSet.addAll(collection2);
        return linkedHashSet;
    }

    public static final SmartList<h> b(Iterable<? extends h> iterable) {
        k.e(iterable, "scopes");
        SmartList<h> smartList = new SmartList<>();
        for (h hVar : iterable) {
            h hVar2 = hVar;
            if ((hVar2 == null || hVar2 == h.b.f20465b) ? false : true) {
                smartList.add(hVar);
            }
        }
        return smartList;
    }
}
