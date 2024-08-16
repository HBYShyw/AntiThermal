package qc;

/* compiled from: Utf8.java */
/* loaded from: classes2.dex */
final class y {
    private static int a(int i10) {
        if (i10 > -12) {
            return -1;
        }
        return i10;
    }

    private static int b(int i10, int i11) {
        if (i10 > -12 || i11 > -65) {
            return -1;
        }
        return i10 ^ (i11 << 8);
    }

    private static int c(int i10, int i11, int i12) {
        if (i10 > -12 || i11 > -65 || i12 > -65) {
            return -1;
        }
        return (i10 ^ (i11 << 8)) ^ (i12 << 16);
    }

    private static int d(byte[] bArr, int i10, int i11) {
        byte b10 = bArr[i10 - 1];
        int i12 = i11 - i10;
        if (i12 == 0) {
            return a(b10);
        }
        if (i12 == 1) {
            return b(b10, bArr[i10]);
        }
        if (i12 == 2) {
            return c(b10, bArr[i10], bArr[i10 + 1]);
        }
        throw new AssertionError();
    }

    public static boolean e(byte[] bArr) {
        return f(bArr, 0, bArr.length);
    }

    public static boolean f(byte[] bArr, int i10, int i11) {
        return h(bArr, i10, i11) == 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0015, code lost:
    
        if (r7[r8] > (-65)) goto L13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0046, code lost:
    
        if (r7[r8] > (-65)) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x007f, code lost:
    
        if (r7[r8] > (-65)) goto L53;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static int g(int i10, byte[] bArr, int i11, int i12) {
        int i13;
        if (i10 != 0) {
            if (i11 >= i12) {
                return i10;
            }
            byte b10 = (byte) i10;
            if (b10 < -32) {
                if (b10 >= -62) {
                    i13 = i11 + 1;
                }
                return -1;
            }
            if (b10 < -16) {
                byte b11 = (byte) (~(i10 >> 8));
                if (b11 == 0) {
                    int i14 = i11 + 1;
                    byte b12 = bArr[i11];
                    if (i14 >= i12) {
                        return b(b10, b12);
                    }
                    i11 = i14;
                    b11 = b12;
                }
                if (b11 <= -65 && ((b10 != -32 || b11 >= -96) && (b10 != -19 || b11 < -96))) {
                    i13 = i11 + 1;
                }
                return -1;
            }
            byte b13 = (byte) (~(i10 >> 8));
            byte b14 = 0;
            if (b13 == 0) {
                int i15 = i11 + 1;
                b13 = bArr[i11];
                if (i15 >= i12) {
                    return b(b10, b13);
                }
                i11 = i15;
            } else {
                b14 = (byte) (i10 >> 16);
            }
            if (b14 == 0) {
                int i16 = i11 + 1;
                b14 = bArr[i11];
                if (i16 >= i12) {
                    return c(b10, b13, b14);
                }
                i11 = i16;
            }
            if (b13 <= -65 && (((b10 << 28) + (b13 + 112)) >> 30) == 0 && b14 <= -65) {
                i13 = i11 + 1;
            }
            return -1;
            i11 = i13;
        }
        return h(bArr, i11, i12);
    }

    public static int h(byte[] bArr, int i10, int i11) {
        while (i10 < i11 && bArr[i10] >= 0) {
            i10++;
        }
        if (i10 >= i11) {
            return 0;
        }
        return i(bArr, i10, i11);
    }

    private static int i(byte[] bArr, int i10, int i11) {
        while (i10 < i11) {
            int i12 = i10 + 1;
            byte b10 = bArr[i10];
            if (b10 < 0) {
                if (b10 < -32) {
                    if (i12 >= i11) {
                        return b10;
                    }
                    if (b10 >= -62) {
                        i10 = i12 + 1;
                        if (bArr[i12] > -65) {
                        }
                    }
                    return -1;
                }
                if (b10 >= -16) {
                    if (i12 >= i11 - 2) {
                        return d(bArr, i12, i11);
                    }
                    int i13 = i12 + 1;
                    byte b11 = bArr[i12];
                    if (b11 <= -65 && (((b10 << 28) + (b11 + 112)) >> 30) == 0) {
                        int i14 = i13 + 1;
                        if (bArr[i13] <= -65) {
                            i12 = i14 + 1;
                            if (bArr[i14] > -65) {
                            }
                        }
                    }
                    return -1;
                }
                if (i12 >= i11 - 1) {
                    return d(bArr, i12, i11);
                }
                int i15 = i12 + 1;
                byte b12 = bArr[i12];
                if (b12 <= -65 && ((b10 != -32 || b12 >= -96) && (b10 != -19 || b12 < -96))) {
                    i10 = i15 + 1;
                    if (bArr[i15] > -65) {
                    }
                }
                return -1;
            }
            i10 = i12;
        }
        return 0;
    }
}
