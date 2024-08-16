package kotlin.collections;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MutableCollectionsJVM.kt */
/* renamed from: kotlin.collections.v, reason: use source file name */
/* loaded from: classes2.dex */
public class MutableCollectionsJVM extends u {
    public static <T extends Comparable<? super T>> void x(List<T> list) {
        za.k.e(list, "<this>");
        if (list.size() > 1) {
            Collections.sort(list);
        }
    }

    public static <T> void y(List<T> list, Comparator<? super T> comparator) {
        za.k.e(list, "<this>");
        za.k.e(comparator, "comparator");
        if (list.size() > 1) {
            Collections.sort(list, comparator);
        }
    }
}
