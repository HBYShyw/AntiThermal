package na;

import java.util.Arrays;
import java.util.List;
import za.k;

/* compiled from: ListBuilder.kt */
/* loaded from: classes2.dex */
public final class c {
    public static final <E> E[] d(int i10) {
        if (i10 >= 0) {
            return (E[]) new Object[i10];
        }
        throw new IllegalArgumentException("capacity must be non-negative.".toString());
    }

    public static final <T> T[] e(T[] tArr, int i10) {
        k.e(tArr, "<this>");
        T[] tArr2 = (T[]) Arrays.copyOf(tArr, i10);
        k.d(tArr2, "copyOf(this, newSize)");
        return tArr2;
    }

    public static final <E> void f(E[] eArr, int i10) {
        k.e(eArr, "<this>");
        eArr[i10] = null;
    }

    public static final <E> void g(E[] eArr, int i10, int i11) {
        k.e(eArr, "<this>");
        while (i10 < i11) {
            f(eArr, i10);
            i10++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final <T> boolean h(T[] tArr, int i10, int i11, List<?> list) {
        if (i11 != list.size()) {
            return false;
        }
        for (int i12 = 0; i12 < i11; i12++) {
            if (!k.a(tArr[i10 + i12], list.get(i12))) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final <T> int i(T[] tArr, int i10, int i11) {
        int i12 = 1;
        for (int i13 = 0; i13 < i11; i13++) {
            T t7 = tArr[i10 + i13];
            i12 = (i12 * 31) + (t7 != null ? t7.hashCode() : 0);
        }
        return i12;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final <T> String j(T[] tArr, int i10, int i11) {
        StringBuilder sb2 = new StringBuilder((i11 * 3) + 2);
        sb2.append("[");
        for (int i12 = 0; i12 < i11; i12++) {
            if (i12 > 0) {
                sb2.append(", ");
            }
            sb2.append(tArr[i10 + i12]);
        }
        sb2.append("]");
        String sb3 = sb2.toString();
        k.d(sb3, "sb.toString()");
        return sb3;
    }
}
