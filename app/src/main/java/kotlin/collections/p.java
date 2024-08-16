package kotlin.collections;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import rd.Sequence;

/* loaded from: classes2.dex */
public final class p extends _Collections {
    public static /* bridge */ /* synthetic */ Set D0(Iterable iterable) {
        return _Collections.D0(iterable);
    }

    public static /* bridge */ /* synthetic */ Sequence K(Iterable iterable) {
        return _Collections.K(iterable);
    }

    public static /* bridge */ /* synthetic */ Object V(List list) {
        return _Collections.V(list);
    }

    public static /* bridge */ /* synthetic */ Appendable a0(Iterable iterable, Appendable appendable, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i10, CharSequence charSequence4, ya.l lVar, int i11, Object obj) {
        Appendable Z;
        Z = _Collections.Z(iterable, appendable, (r14 & 2) != 0 ? ", " : charSequence, (r14 & 4) != 0 ? "" : charSequence2, (r14 & 8) == 0 ? charSequence3 : "", (r14 & 16) != 0 ? -1 : i10, (r14 & 32) != 0 ? "..." : charSequence4, (r14 & 64) != 0 ? null : lVar);
        return Z;
    }

    public static /* bridge */ /* synthetic */ Comparable i0(Iterable iterable) {
        return _Collections.i0(iterable);
    }

    public static /* bridge */ /* synthetic */ List j() {
        return r.j();
    }

    public static /* bridge */ /* synthetic */ int l(List list) {
        return r.l(list);
    }

    public static /* bridge */ /* synthetic */ List q(List list) {
        return r.q(list);
    }

    public static /* bridge */ /* synthetic */ void s() {
        r.s();
    }

    public static /* bridge */ /* synthetic */ void t() {
        r.t();
    }

    public static /* bridge */ /* synthetic */ int u(Iterable iterable, int i10) {
        return s.u(iterable, i10);
    }

    public static /* bridge */ /* synthetic */ boolean z(Collection collection, Iterable iterable) {
        return MutableCollections.z(collection, iterable);
    }
}
