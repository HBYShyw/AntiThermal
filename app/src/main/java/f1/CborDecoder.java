package f1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

/* compiled from: CborDecoder.java */
/* renamed from: f1.d, reason: use source file name */
/* loaded from: classes.dex */
public class CborDecoder {

    /* renamed from: a, reason: collision with root package name */
    private final InputStream f11269a;

    /* renamed from: b, reason: collision with root package name */
    private int f11270b = -1;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: CborDecoder.java */
    /* renamed from: f1.d$a */
    /* loaded from: classes.dex */
    public static /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f11271a;

        static {
            int[] iArr = new int[AdditionalInformationEnum.values().length];
            f11271a = iArr;
            try {
                iArr[AdditionalInformationEnum.DIRECT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f11271a[AdditionalInformationEnum.ONE_BYTE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f11271a[AdditionalInformationEnum.TWO_BYTES.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f11271a[AdditionalInformationEnum.FOUR_BYTES.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f11271a[AdditionalInformationEnum.EIGHT_BYTES.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f11271a[AdditionalInformationEnum.INDEFINITE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f11271a[AdditionalInformationEnum.RESERVED.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    public CborDecoder(InputStream inputStream) {
        this.f11269a = inputStream;
    }

    private CborArray b(int i10, long j10) {
        long longValue = m(i10).longValue();
        CborArray cborArray = new CborArray(j10);
        if (longValue == -1) {
            cborArray.j(true);
            while (true) {
                CborObject h10 = h();
                if (h10 != null) {
                    if (new CborOther(CborOtherEnum.BREAK).equals(h10)) {
                        break;
                    }
                    cborArray.g(h10);
                } else {
                    throw new CborException("Unexpected end of stream");
                }
            }
        } else {
            for (long j11 = 0; j11 < longValue; j11++) {
                CborObject h11 = h();
                if (h11 != null) {
                    cborArray.g(h11);
                } else {
                    throw new CborException("Unexpected end of stream");
                }
            }
        }
        return cborArray;
    }

    private CborByteString c(int i10, long j10) {
        long longValue = m(i10).longValue();
        if (longValue == -1) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while (true) {
                CborObject h10 = h();
                if (h10 != null) {
                    int b10 = h10.b();
                    if (new CborOther(CborOtherEnum.BREAK).equals(h10)) {
                        return new CborByteString(byteArrayOutputStream.toByteArray(), j10);
                    }
                    if (b10 == 2) {
                        byte[] g6 = ((CborByteString) h10).g();
                        if (g6 != null) {
                            byteArrayOutputStream.write(g6, 0, g6.length);
                        }
                    } else {
                        throw new CborException("Unexpected major type " + b10);
                    }
                } else {
                    throw new CborException("Unexpected end of stream");
                }
            }
        } else {
            return new CborByteString(o(longValue), j10);
        }
    }

    private CborDoublePrecisionFloat d(long j10) {
        byte[] o10 = o(8L);
        return new CborDoublePrecisionFloat(Double.longBitsToDouble((((((((((((((((o10[0] & 255) | 0) << 8) | (o10[1] & 255)) << 8) | (o10[2] & 255)) << 8) | (o10[3] & 255)) << 8) | (o10[4] & 255)) << 8) | (o10[5] & 255)) << 8) | (o10[6] & 255)) << 8) | (o10[7] & 255)), j10);
    }

    private CborHalfPrecisionFloat e(long j10) {
        byte[] o10 = o(2L);
        return new CborHalfPrecisionFloat(p((short) (((short) (o10[1] & 255)) | ((short) ((o10[0] & 255) << 8)))), j10);
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x004a, code lost:
    
        throw new f1.CborException("Unexpected end of stream");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private CborMap f(int i10, long j10) {
        long longValue = m(i10).longValue();
        CborMap cborMap = new CborMap(j10);
        if (longValue == -1) {
            cborMap.p(true);
            while (true) {
                CborObject h10 = h();
                if (new CborOther(CborOtherEnum.BREAK).equals(h10)) {
                    break;
                }
                CborObject h11 = h();
                if (h10 == null || h11 == null) {
                    break;
                }
                if (!cborMap.h(h10)) {
                    cborMap.n(h10, h11);
                } else {
                    throw new CborException("Duplicate key found in map");
                }
            }
        } else {
            for (long j11 = 0; j11 < longValue; j11++) {
                CborObject h12 = h();
                CborObject h13 = h();
                if (h12 != null && h13 != null) {
                    if (!cborMap.h(h12)) {
                        cborMap.n(h12, h13);
                    } else {
                        throw new CborException("Duplicate key found in map");
                    }
                } else {
                    throw new CborException("Unexpected end of stream");
                }
            }
        }
        return cborMap;
    }

    private CborNegativeInteger g(int i10, long j10) {
        return new CborNegativeInteger(BigInteger.valueOf(-1L).subtract(m(i10)), j10);
    }

    private CborOther i(int i10, long j10) {
        CborOtherEnum a10 = CborOtherEnum.a(i10);
        if (a10 == CborOtherEnum.SIMPLE_VALUE) {
            CborSimpleValueEnum a11 = CborSimpleValueEnum.a(i10);
            CborSimpleValueEnum cborSimpleValueEnum = CborSimpleValueEnum.FALSE;
            if (a11 == cborSimpleValueEnum) {
                return new CborSimpleValue(cborSimpleValueEnum, j10);
            }
            CborSimpleValueEnum cborSimpleValueEnum2 = CborSimpleValueEnum.TRUE;
            if (a11 == cborSimpleValueEnum2) {
                return new CborSimpleValue(cborSimpleValueEnum2, j10);
            }
            CborSimpleValueEnum cborSimpleValueEnum3 = CborSimpleValueEnum.NULL;
            if (a11 == cborSimpleValueEnum3) {
                return new CborSimpleValue(cborSimpleValueEnum3, j10);
            }
            CborSimpleValueEnum cborSimpleValueEnum4 = CborSimpleValueEnum.UNDEFINED;
            if (a11 == cborSimpleValueEnum4) {
                return new CborSimpleValue(cborSimpleValueEnum4, j10);
            }
            if (a11 == CborSimpleValueEnum.UNASSIGNED) {
                return new CborSimpleValue(i10 & 31, j10);
            }
            throw new CborException("Not implemented");
        }
        if (a10 == CborOtherEnum.SIMPLE_VALUE_FOLLOWING_BYTE) {
            return new CborSimpleValue(n());
        }
        if (a10 == CborOtherEnum.IEEE_754_HALF_PRECISION_FLOAT) {
            return e(j10);
        }
        if (a10 == CborOtherEnum.IEEE_754_SINGLE_PRECISION_FLOAT) {
            return j(j10);
        }
        if (a10 == CborOtherEnum.IEEE_754_DOUBLE_PRECISION_FLOAT) {
            return d(j10);
        }
        CborOtherEnum cborOtherEnum = CborOtherEnum.BREAK;
        if (a10 == cborOtherEnum) {
            return new CborOther(cborOtherEnum, j10);
        }
        throw new CborException("Not implemented");
    }

    private CborSimplePrecisionFloat j(long j10) {
        byte[] o10 = o(4L);
        return new CborSimplePrecisionFloat(Float.intBitsToFloat((o10[3] & 255) | ((((((0 | (o10[0] & 255)) << 8) | (o10[1] & 255)) << 8) | (o10[2] & 255)) << 8)), j10);
    }

    private CborTextString k(int i10, long j10) {
        long longValue = m(i10).longValue();
        if (longValue == -1) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while (true) {
                CborObject h10 = h();
                if (h10 != null) {
                    int b10 = h10.b();
                    if (new CborOther(CborOtherEnum.BREAK).equals(h10)) {
                        return new CborTextString(new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8), j10);
                    }
                    if (b10 == 3) {
                        byte[] bytes = ((CborTextString) h10).g().getBytes(StandardCharsets.UTF_8);
                        byteArrayOutputStream.write(bytes, 0, bytes.length);
                    } else {
                        throw new CborException("Unexpected major type " + b10);
                    }
                } else {
                    throw new CborException("Unexpected end of stream");
                }
            }
        } else {
            return new CborTextString(new String(o(longValue), StandardCharsets.UTF_8), j10);
        }
    }

    private CborUnsignedInteger l(int i10, int i11) {
        return new CborUnsignedInteger(m(i10), i11);
    }

    private BigInteger m(int i10) {
        switch (a.f11271a[AdditionalInformationEnum.a(i10).ordinal()]) {
            case 1:
                return BigInteger.valueOf(i10 & 31);
            case 2:
                return BigInteger.valueOf(n());
            case 3:
                byte[] o10 = o(2L);
                return BigInteger.valueOf((o10[1] & 255) | ((o10[0] & 255) << 8) | 0);
            case 4:
                byte[] o11 = o(4L);
                return BigInteger.valueOf((o11[3] & 255) | ((o11[0] & 255) << 24) | 0 | ((o11[1] & 255) << 16) | ((o11[2] & 255) << 8));
            case 5:
                BigInteger bigInteger = BigInteger.ZERO;
                byte[] o12 = o(8L);
                return bigInteger.or(BigInteger.valueOf(o12[0] & 255).shiftLeft(56)).or(BigInteger.valueOf(o12[1] & 255).shiftLeft(48)).or(BigInteger.valueOf(o12[2] & 255).shiftLeft(40)).or(BigInteger.valueOf(o12[3] & 255).shiftLeft(32)).or(BigInteger.valueOf(o12[4] & 255).shiftLeft(24)).or(BigInteger.valueOf(o12[5] & 255).shiftLeft(16)).or(BigInteger.valueOf(o12[6] & 255).shiftLeft(8)).or(BigInteger.valueOf(o12[7] & 255));
            case 6:
                return BigInteger.valueOf(-1L);
            default:
                throw new CborException("Reserved additional information");
        }
    }

    private int n() {
        try {
            int read = this.f11269a.read();
            if (read != -1) {
                return read;
            }
            throw new IOException("Unexpected end of stream");
        } catch (IOException e10) {
            throw new CborException(e10);
        }
    }

    private byte[] o(long j10) {
        if (j10 <= 2147483647L) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int i10 = (int) j10;
            byte[] bArr = new byte[Math.min(i10, 4096)];
            while (i10 > 0) {
                try {
                    int read = this.f11269a.read(bArr, 0, Math.min(i10, 4096));
                    if (read != -1) {
                        byteArrayOutputStream.write(bArr, 0, read);
                        i10 -= read;
                    } else {
                        throw new IOException("Unexpected end of stream");
                    }
                } catch (IOException e10) {
                    throw new CborException(e10);
                }
            }
            return byteArrayOutputStream.toByteArray();
        }
        throw new CborException("Decoding fixed size items is limited to Integer.MAX_VALUE");
    }

    private float p(short s7) {
        int i10;
        int i11 = 65535 & s7;
        int i12 = 32768 & i11;
        int i13 = (i11 >>> 10) & 31;
        int i14 = i11 & 1023;
        int i15 = 0;
        if (i13 != 0) {
            int i16 = i14 << 13;
            i15 = i13 == 31 ? 255 : (i13 - 15) + 127;
            i10 = i16;
        } else {
            if (i14 != 0) {
                float intBitsToFloat = Float.intBitsToFloat(i14 + 1056964608) - Float.intBitsToFloat(1056964608);
                return i12 == 0 ? intBitsToFloat : -intBitsToFloat;
            }
            i10 = 0;
        }
        return Float.intBitsToFloat(i10 | (i12 << 16) | (i15 << 23));
    }

    public List<CborObject> a() {
        LinkedList linkedList = new LinkedList();
        while (true) {
            CborObject h10 = h();
            if (h10 == null) {
                return linkedList;
            }
            linkedList.add(h10);
        }
    }

    public CborObject h() {
        int i10 = this.f11270b;
        this.f11270b = -1;
        try {
            int read = this.f11269a.read();
            if (read == -1) {
                return null;
            }
            int a10 = CborMajorType.a(read);
            if (a10 == 0) {
                return l(read, i10);
            }
            if (a10 == 1) {
                return g(read, i10);
            }
            if (a10 == 2) {
                return c(read, i10);
            }
            if (a10 == 3) {
                return k(read, i10);
            }
            if (a10 == 4) {
                return b(read, i10);
            }
            if (a10 == 5) {
                return f(read, i10);
            }
            if (a10 == 6) {
                long longValue = m(read).longValue();
                if (CborTagType.a(longValue)) {
                    this.f11270b = (int) longValue;
                }
                return h();
            }
            if (a10 == 7) {
                return i(read, i10);
            }
            throw new CborException("Not implemented major type " + read);
        } catch (IOException e10) {
            throw new CborException(e10);
        }
    }
}
