package ne;

import me.Segment;
import me.d;
import me.d0;
import me.q;
import za.k;

/* compiled from: Buffer.kt */
/* loaded from: classes2.dex */
public final class a {

    /* renamed from: a */
    private static final byte[] f16089a = d0.a("0123456789abcdef");

    public static final byte[] a() {
        return f16089a;
    }

    public static final boolean b(Segment segment, int i10, byte[] bArr, int i11, int i12) {
        k.e(segment, "segment");
        k.e(bArr, "bytes");
        int i13 = segment.f15533c;
        byte[] bArr2 = segment.f15531a;
        while (i11 < i12) {
            if (i10 == i13) {
                segment = segment.f15536f;
                k.b(segment);
                byte[] bArr3 = segment.f15531a;
                bArr2 = bArr3;
                i10 = segment.f15532b;
                i13 = segment.f15533c;
            }
            if (bArr2[i10] != bArr[i11]) {
                return false;
            }
            i10++;
            i11++;
        }
        return true;
    }

    public static final String c(d dVar, long j10) {
        k.e(dVar, "<this>");
        if (j10 > 0) {
            long j11 = j10 - 1;
            if (dVar.L(j11) == 13) {
                String t02 = dVar.t0(j11);
                dVar.V(2L);
                return t02;
            }
        }
        String t03 = dVar.t0(j10);
        dVar.V(1L);
        return t03;
    }

    public static final int d(d dVar, q qVar, boolean z10) {
        int i10;
        int i11;
        int i12;
        int i13;
        Segment segment;
        k.e(dVar, "<this>");
        k.e(qVar, "options");
        Segment segment2 = dVar.f15484e;
        if (segment2 == null) {
            return z10 ? -2 : -1;
        }
        byte[] bArr = segment2.f15531a;
        int i14 = segment2.f15532b;
        int i15 = segment2.f15533c;
        int[] h10 = qVar.h();
        Segment segment3 = segment2;
        int i16 = -1;
        int i17 = 0;
        loop0: while (true) {
            int i18 = i17 + 1;
            int i19 = h10[i17];
            int i20 = i18 + 1;
            int i21 = h10[i18];
            if (i21 != -1) {
                i16 = i21;
            }
            if (segment3 == null) {
                break;
            }
            if (i19 >= 0) {
                i10 = i14 + 1;
                int i22 = bArr[i14] & 255;
                int i23 = i20 + i19;
                while (i20 != i23) {
                    if (i22 == h10[i20]) {
                        i11 = h10[i20 + i19];
                        if (i10 == i15) {
                            segment3 = segment3.f15536f;
                            k.b(segment3);
                            i10 = segment3.f15532b;
                            bArr = segment3.f15531a;
                            i15 = segment3.f15533c;
                            if (segment3 == segment2) {
                                segment3 = null;
                            }
                        }
                    } else {
                        i20++;
                    }
                }
                return i16;
            }
            int i24 = i20 + (i19 * (-1));
            while (true) {
                int i25 = i14 + 1;
                int i26 = i20 + 1;
                if ((bArr[i14] & 255) != h10[i20]) {
                    return i16;
                }
                boolean z11 = i26 == i24;
                if (i25 == i15) {
                    k.b(segment3);
                    Segment segment4 = segment3.f15536f;
                    k.b(segment4);
                    i13 = segment4.f15532b;
                    byte[] bArr2 = segment4.f15531a;
                    i12 = segment4.f15533c;
                    if (segment4 != segment2) {
                        segment = segment4;
                        bArr = bArr2;
                    } else {
                        if (!z11) {
                            break loop0;
                        }
                        bArr = bArr2;
                        segment = null;
                    }
                } else {
                    Segment segment5 = segment3;
                    i12 = i15;
                    i13 = i25;
                    segment = segment5;
                }
                if (z11) {
                    i11 = h10[i26];
                    i10 = i13;
                    i15 = i12;
                    segment3 = segment;
                    break;
                }
                i14 = i13;
                i15 = i12;
                i20 = i26;
                segment3 = segment;
            }
            if (i11 >= 0) {
                return i11;
            }
            i17 = -i11;
            i14 = i10;
        }
        if (z10) {
            return -2;
        }
        return i16;
    }

    public static /* synthetic */ int e(d dVar, q qVar, boolean z10, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            z10 = false;
        }
        return d(dVar, qVar, z10);
    }
}
