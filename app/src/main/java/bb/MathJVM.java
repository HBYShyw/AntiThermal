package bb;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MathJVM.kt */
/* renamed from: bb.c, reason: use source file name */
/* loaded from: classes2.dex */
public class MathJVM extends MathH {
    public static int a(double d10) {
        if (Double.isNaN(d10)) {
            throw new IllegalArgumentException("Cannot round NaN value.");
        }
        if (d10 > 2.147483647E9d) {
            return Integer.MAX_VALUE;
        }
        if (d10 < -2.147483648E9d) {
            return Integer.MIN_VALUE;
        }
        return (int) Math.round(d10);
    }

    public static int b(float f10) {
        if (Float.isNaN(f10)) {
            throw new IllegalArgumentException("Cannot round NaN value.");
        }
        return Math.round(f10);
    }
}
