package me;

import me.d;
import sd.StringsJVM;

/* compiled from: Util.kt */
/* loaded from: classes2.dex */
public final class b {

    /* renamed from: a, reason: collision with root package name */
    private static final d.a f15464a = new d.a();

    /* renamed from: b, reason: collision with root package name */
    private static final int f15465b = -1234567890;

    public static final boolean a(byte[] bArr, int i10, byte[] bArr2, int i11, int i12) {
        za.k.e(bArr, "a");
        za.k.e(bArr2, "b");
        for (int i13 = 0; i13 < i12; i13++) {
            if (bArr[i13 + i10] != bArr2[i13 + i11]) {
                return false;
            }
        }
        return true;
    }

    public static final void b(long j10, long j11, long j12) {
        if ((j11 | j12) < 0 || j11 > j10 || j10 - j11 < j12) {
            throw new ArrayIndexOutOfBoundsException("size=" + j10 + " offset=" + j11 + " byteCount=" + j12);
        }
    }

    public static final int c() {
        return f15465b;
    }

    public static final int d(g gVar, int i10) {
        za.k.e(gVar, "<this>");
        return i10 == f15465b ? gVar.t() : i10;
    }

    public static final int e(byte[] bArr, int i10) {
        za.k.e(bArr, "<this>");
        return i10 == f15465b ? bArr.length : i10;
    }

    public static final int f(int i10) {
        return ((i10 & 255) << 24) | (((-16777216) & i10) >>> 24) | ((16711680 & i10) >>> 8) | ((65280 & i10) << 8);
    }

    public static final short g(short s7) {
        int i10 = s7 & 65535;
        return (short) (((i10 & 255) << 8) | ((65280 & i10) >>> 8));
    }

    public static final String h(byte b10) {
        String n10;
        n10 = StringsJVM.n(new char[]{ne.b.f()[(b10 >> 4) & 15], ne.b.f()[b10 & 15]});
        return n10;
    }

    public static final String i(int i10) {
        String o10;
        if (i10 == 0) {
            return "0";
        }
        int i11 = 0;
        char[] cArr = {ne.b.f()[(i10 >> 28) & 15], ne.b.f()[(i10 >> 24) & 15], ne.b.f()[(i10 >> 20) & 15], ne.b.f()[(i10 >> 16) & 15], ne.b.f()[(i10 >> 12) & 15], ne.b.f()[(i10 >> 8) & 15], ne.b.f()[(i10 >> 4) & 15], ne.b.f()[i10 & 15]};
        while (i11 < 8 && cArr[i11] == '0') {
            i11++;
        }
        o10 = StringsJVM.o(cArr, i11, 8);
        return o10;
    }
}
