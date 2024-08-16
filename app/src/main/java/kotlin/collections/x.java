package kotlin.collections;

import fb.PrimitiveRanges;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ReversedViews.kt */
/* loaded from: classes2.dex */
public class x extends MutableCollections {
    public static <T> List<T> F(List<T> list) {
        za.k.e(list, "<this>");
        return new p0(list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int G(List<?> list, int i10) {
        int l10;
        int l11;
        int l12;
        l10 = r.l(list);
        if (new PrimitiveRanges(0, l10).i(i10)) {
            l12 = r.l(list);
            return l12 - i10;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Element index ");
        sb2.append(i10);
        sb2.append(" must be in range [");
        l11 = r.l(list);
        sb2.append(new PrimitiveRanges(0, l11));
        sb2.append("].");
        throw new IndexOutOfBoundsException(sb2.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int H(List<?> list, int i10) {
        if (new PrimitiveRanges(0, list.size()).i(i10)) {
            return list.size() - i10;
        }
        throw new IndexOutOfBoundsException("Position index " + i10 + " must be in range [" + new PrimitiveRanges(0, list.size()) + "].");
    }
}
