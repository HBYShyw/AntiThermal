package ma;

import sd.CharJVM;

/* compiled from: UnsignedUtils.kt */
/* renamed from: ma.h0, reason: use source file name */
/* loaded from: classes2.dex */
public final class UnsignedUtils {
    public static final int a(int i10, int i11) {
        return za.k.f(i10 ^ Integer.MIN_VALUE, i11 ^ Integer.MIN_VALUE);
    }

    public static final int b(long j10, long j11) {
        return za.k.g(j10 ^ Long.MIN_VALUE, j11 ^ Long.MIN_VALUE);
    }

    public static final String c(long j10) {
        return d(j10, 10);
    }

    public static final String d(long j10, int i10) {
        int a10;
        int a11;
        int a12;
        if (j10 >= 0) {
            a12 = CharJVM.a(i10);
            String l10 = Long.toString(j10, a12);
            za.k.d(l10, "toString(this, checkRadix(radix))");
            return l10;
        }
        long j11 = i10;
        long j12 = ((j10 >>> 1) / j11) << 1;
        long j13 = j10 - (j12 * j11);
        if (j13 >= j11) {
            j13 -= j11;
            j12++;
        }
        StringBuilder sb2 = new StringBuilder();
        a10 = CharJVM.a(i10);
        String l11 = Long.toString(j12, a10);
        za.k.d(l11, "toString(this, checkRadix(radix))");
        sb2.append(l11);
        a11 = CharJVM.a(i10);
        String l12 = Long.toString(j13, a11);
        za.k.d(l12, "toString(this, checkRadix(radix))");
        sb2.append(l12);
        return sb2.toString();
    }
}
