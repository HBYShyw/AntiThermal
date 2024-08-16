package d2;

/* compiled from: COUIMathUtils.java */
/* renamed from: d2.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUIMathUtils {
    public static int a(int i10, int i11) {
        int i12 = i10 / i11;
        return ((i10 ^ i11) >= 0 || i11 * i12 == i10) ? i12 : i12 - 1;
    }

    public static int b(int i10, int i11) {
        return i10 - (a(i10, i11) * i11);
    }
}
