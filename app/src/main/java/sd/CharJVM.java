package sd;

import fb.PrimitiveRanges;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: CharJVM.kt */
/* renamed from: sd.b, reason: use source file name */
/* loaded from: classes2.dex */
public class CharJVM {
    public static int a(int i10) {
        if (new PrimitiveRanges(2, 36).i(i10)) {
            return i10;
        }
        throw new IllegalArgumentException("radix " + i10 + " was not in valid range " + new PrimitiveRanges(2, 36));
    }

    public static final int b(char c10, int i10) {
        return Character.digit((int) c10, i10);
    }

    public static boolean c(char c10) {
        return Character.isWhitespace(c10) || Character.isSpaceChar(c10);
    }
}
