package m1;

/* compiled from: COUIPhysicalAnimationUtil.java */
/* renamed from: m1.g, reason: use source file name */
/* loaded from: classes.dex */
public class COUIPhysicalAnimationUtil {
    public static int a(int i10, int i11, int i12) {
        return i10 + ((int) (i12 * 0.3731f * Math.tanh(((i11 / 1.5f) * 2.5f) / (r5 * 0.9f))));
    }

    public static int b(int i10, int i11, int i12) {
        return (int) (((i10 * (1.0f - Math.min((Math.abs(i11) * 1.0f) / i12, 1.0f))) / 5.0f) * 2.0f);
    }
}
