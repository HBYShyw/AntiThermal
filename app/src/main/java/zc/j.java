package zc;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import kotlin.collections.MutableCollections;
import oc.Name;

/* compiled from: MemberScope.kt */
/* loaded from: classes2.dex */
public final class j {
    public static final Set<Name> a(Iterable<? extends h> iterable) {
        za.k.e(iterable, "<this>");
        HashSet hashSet = new HashSet();
        Iterator<? extends h> it = iterable.iterator();
        while (it.hasNext()) {
            Set<Name> f10 = it.next().f();
            if (f10 == null) {
                return null;
            }
            MutableCollections.z(hashSet, f10);
        }
        return hashSet;
    }
}
