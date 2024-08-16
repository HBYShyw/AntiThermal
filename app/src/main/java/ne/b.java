package ne;

import ma.Unit;
import me.d;
import me.g;
import za.k;

/* compiled from: ByteString.kt */
/* loaded from: classes2.dex */
public final class b {

    /* renamed from: a */
    private static final char[] f16090a = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /* JADX WARN: Removed duplicated region for block: B:174:0x0220 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:240:0x0047 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:267:0x0083 A[EDGE_INSN: B:267:0x0083->B:268:0x0083 BREAK  A[LOOP:1: B:249:0x0051->B:276:0x008a, LOOP_LABEL: LOOP:0: B:2:0x0008->B:43:0x0008], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00df A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0173 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final int c(byte[] bArr, int i10) {
        boolean z10;
        boolean z11;
        int i11;
        boolean z12;
        boolean z13;
        boolean z14;
        int length = bArr.length;
        int i12 = 0;
        int i13 = 0;
        int i14 = 0;
        loop0: while (i12 < length) {
            byte b10 = bArr[i12];
            if (b10 >= 0) {
                int i15 = i14 + 1;
                if (i14 == i10) {
                    return i13;
                }
                if (b10 != 10 && b10 != 13) {
                    if (!(b10 >= 0 && b10 < 32)) {
                        if (!(Byte.MAX_VALUE <= b10 && b10 < 160)) {
                            z11 = false;
                            if (z11) {
                                return -1;
                            }
                        }
                    }
                    z11 = true;
                    if (z11) {
                    }
                }
                if (b10 == 65533) {
                    return -1;
                }
                i13 += b10 < 65536 ? 1 : 2;
                i12++;
                while (true) {
                    i14 = i15;
                    if (i12 < length && bArr[i12] >= 0) {
                        int i16 = i12 + 1;
                        byte b11 = bArr[i12];
                        i15 = i14 + 1;
                        if (i14 == i10) {
                            return i13;
                        }
                        if (b11 != 10 && b11 != 13) {
                            if (!(b11 >= 0 && b11 < 32)) {
                                if (!(Byte.MAX_VALUE <= b11 && b11 < 160)) {
                                    z10 = false;
                                    if (z10) {
                                        break loop0;
                                    }
                                }
                            }
                            z10 = true;
                            if (z10) {
                            }
                        }
                        if (b11 == 65533) {
                            break loop0;
                        }
                        i13 += b11 < 65536 ? 1 : 2;
                        i12 = i16;
                    }
                }
                return -1;
            }
            if ((b10 >> 5) == -2) {
                int i17 = i12 + 1;
                if (length <= i17) {
                    if (i14 == i10) {
                        return i13;
                    }
                    return -1;
                }
                byte b12 = bArr[i12];
                byte b13 = bArr[i17];
                if (!((b13 & 192) == 128)) {
                    if (i14 == i10) {
                        return i13;
                    }
                    return -1;
                }
                int i18 = (b13 ^ 3968) ^ (b12 << 6);
                if (i18 < 128) {
                    if (i14 == i10) {
                        return i13;
                    }
                    return -1;
                }
                i11 = i14 + 1;
                if (i14 == i10) {
                    return i13;
                }
                if (i18 != 10 && i18 != 13) {
                    if (!(i18 >= 0 && i18 < 32)) {
                        if (!(127 <= i18 && i18 < 160)) {
                            z14 = false;
                            if (z14) {
                                return -1;
                            }
                        }
                    }
                    z14 = true;
                    if (z14) {
                    }
                }
                if (i18 == 65533) {
                    return -1;
                }
                i13 += i18 < 65536 ? 1 : 2;
                Unit unit = Unit.f15173a;
                i12 += 2;
            } else if ((b10 >> 4) == -2) {
                int i19 = i12 + 2;
                if (length <= i19) {
                    if (i14 == i10) {
                        return i13;
                    }
                    return -1;
                }
                byte b14 = bArr[i12];
                byte b15 = bArr[i12 + 1];
                if (!((b15 & 192) == 128)) {
                    if (i14 == i10) {
                        return i13;
                    }
                    return -1;
                }
                byte b16 = bArr[i19];
                if (!((b16 & 192) == 128)) {
                    if (i14 == i10) {
                        return i13;
                    }
                    return -1;
                }
                int i20 = ((b16 ^ (-123008)) ^ (b15 << 6)) ^ (b14 << 12);
                if (i20 < 2048) {
                    if (i14 == i10) {
                        return i13;
                    }
                    return -1;
                }
                if (55296 <= i20 && i20 < 57344) {
                    if (i14 == i10) {
                        return i13;
                    }
                    return -1;
                }
                i11 = i14 + 1;
                if (i14 == i10) {
                    return i13;
                }
                if (i20 != 10 && i20 != 13) {
                    if (!(i20 >= 0 && i20 < 32)) {
                        if (!(127 <= i20 && i20 < 160)) {
                            z13 = false;
                            if (z13) {
                                return -1;
                            }
                        }
                    }
                    z13 = true;
                    if (z13) {
                    }
                }
                if (i20 == 65533) {
                    return -1;
                }
                i13 += i20 < 65536 ? 1 : 2;
                Unit unit2 = Unit.f15173a;
                i12 += 3;
            } else {
                if ((b10 >> 3) != -2) {
                    if (i14 == i10) {
                        return i13;
                    }
                    return -1;
                }
                int i21 = i12 + 3;
                if (length <= i21) {
                    if (i14 == i10) {
                        return i13;
                    }
                    return -1;
                }
                byte b17 = bArr[i12];
                byte b18 = bArr[i12 + 1];
                if (!((b18 & 192) == 128)) {
                    if (i14 == i10) {
                        return i13;
                    }
                    return -1;
                }
                byte b19 = bArr[i12 + 2];
                if (!((b19 & 192) == 128)) {
                    if (i14 == i10) {
                        return i13;
                    }
                    return -1;
                }
                byte b20 = bArr[i21];
                if (!((b20 & 192) == 128)) {
                    if (i14 == i10) {
                        return i13;
                    }
                    return -1;
                }
                int i22 = (((b20 ^ 3678080) ^ (b19 << 6)) ^ (b18 << 12)) ^ (b17 << 18);
                if (i22 > 1114111) {
                    if (i14 == i10) {
                        return i13;
                    }
                    return -1;
                }
                if (55296 <= i22 && i22 < 57344) {
                    if (i14 == i10) {
                        return i13;
                    }
                    return -1;
                }
                if (i22 < 65536) {
                    if (i14 == i10) {
                        return i13;
                    }
                    return -1;
                }
                i11 = i14 + 1;
                if (i14 == i10) {
                    return i13;
                }
                if (i22 != 10 && i22 != 13) {
                    if (!(i22 >= 0 && i22 < 32)) {
                        if (!(127 <= i22 && i22 < 160)) {
                            z12 = false;
                            if (z12) {
                                return -1;
                            }
                        }
                    }
                    z12 = true;
                    if (z12) {
                    }
                }
                if (i22 == 65533) {
                    return -1;
                }
                i13 += i22 < 65536 ? 1 : 2;
                Unit unit3 = Unit.f15173a;
                i12 += 4;
            }
            i14 = i11;
        }
        return i13;
    }

    public static final void d(g gVar, d dVar, int i10, int i11) {
        k.e(gVar, "<this>");
        k.e(dVar, "buffer");
        dVar.O(gVar.f(), i10, i11);
    }

    public static final int e(char c10) {
        if ('0' <= c10 && c10 < ':') {
            return c10 - '0';
        }
        char c11 = 'a';
        if (!('a' <= c10 && c10 < 'g')) {
            c11 = 'A';
            if (!('A' <= c10 && c10 < 'G')) {
                throw new IllegalArgumentException("Unexpected hex digit: " + c10);
            }
        }
        return (c10 - c11) + 10;
    }

    public static final char[] f() {
        return f16090a;
    }
}
