package sd;

/* compiled from: Char.kt */
/* renamed from: sd.c, reason: use source file name */
/* loaded from: classes2.dex */
class Char extends CharJVM {
    public static final boolean d(char c10, char c11, boolean z10) {
        if (c10 == c11) {
            return true;
        }
        if (!z10) {
            return false;
        }
        char upperCase = Character.toUpperCase(c10);
        char upperCase2 = Character.toUpperCase(c11);
        return upperCase == upperCase2 || Character.toLowerCase(upperCase) == Character.toLowerCase(upperCase2);
    }
}
