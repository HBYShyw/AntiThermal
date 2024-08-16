package f1;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* compiled from: CborEncoder.java */
/* renamed from: f1.f, reason: use source file name */
/* loaded from: classes.dex */
public class CborEncoder {

    /* renamed from: a, reason: collision with root package name */
    private final OutputStream f11273a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: CborEncoder.java */
    /* renamed from: f1.f$a */
    /* loaded from: classes.dex */
    public static /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f11274a;

        /* renamed from: b, reason: collision with root package name */
        static final /* synthetic */ int[] f11275b;

        static {
            int[] iArr = new int[CborOtherEnum.values().length];
            f11275b = iArr;
            try {
                iArr[CborOtherEnum.BREAK.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f11275b[CborOtherEnum.SIMPLE_VALUE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f11275b[CborOtherEnum.RESERVED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f11275b[CborOtherEnum.IEEE_754_HALF_PRECISION_FLOAT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f11275b[CborOtherEnum.IEEE_754_SINGLE_PRECISION_FLOAT.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f11275b[CborOtherEnum.IEEE_754_DOUBLE_PRECISION_FLOAT.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f11275b[CborOtherEnum.SIMPLE_VALUE_FOLLOWING_BYTE.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            int[] iArr2 = new int[CborSimpleValueEnum.values().length];
            f11274a = iArr2;
            try {
                iArr2[CborSimpleValueEnum.FALSE.ordinal()] = 1;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                f11274a[CborSimpleValueEnum.NULL.ordinal()] = 2;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                f11274a[CborSimpleValueEnum.TRUE.ordinal()] = 3;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                f11274a[CborSimpleValueEnum.UNDEFINED.ordinal()] = 4;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                f11274a[CborSimpleValueEnum.UNASSIGNED.ordinal()] = 5;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                f11274a[CborSimpleValueEnum.RESERVED.ordinal()] = 6;
            } catch (NoSuchFieldError unused13) {
            }
        }
    }

    public CborEncoder(OutputStream outputStream) {
        this.f11273a = outputStream;
    }

    private void a(CborArray cborArray) {
        List<CborObject> h10 = cborArray.h();
        if (cborArray.i()) {
            p(4);
        } else {
            n(4, h10.size());
        }
        Iterator<CborObject> it = h10.iterator();
        while (it.hasNext()) {
            g(it.next());
        }
        if (cborArray.i()) {
            h(new CborOther(CborOtherEnum.BREAK));
        }
    }

    private void b(CborByteString cborByteString) {
        byte[] g6 = cborByteString.g();
        if (cborByteString.h()) {
            p(2);
            if (g6 != null) {
                n(2, g6.length);
                s(g6);
            }
        } else if (g6 == null) {
            h(new CborSimpleValue(CborSimpleValueEnum.NULL));
        } else {
            n(2, g6.length);
            s(g6);
        }
        if (cborByteString.h()) {
            h(new CborOther(CborOtherEnum.BREAK));
        }
    }

    private void c(CborDoublePrecisionFloat cborDoublePrecisionFloat) {
        long doubleToRawLongBits = Double.doubleToRawLongBits(cborDoublePrecisionFloat.h());
        s(-5, (byte) ((doubleToRawLongBits >> 56) & 255), (byte) ((doubleToRawLongBits >> 48) & 255), (byte) ((doubleToRawLongBits >> 40) & 255), (byte) ((doubleToRawLongBits >> 32) & 255), (byte) ((doubleToRawLongBits >> 24) & 255), (byte) ((doubleToRawLongBits >> 16) & 255), (byte) ((doubleToRawLongBits >> 8) & 255), (byte) ((doubleToRawLongBits >> 0) & 255));
    }

    private void d(CborHalfPrecisionFloat cborHalfPrecisionFloat) {
        int q10 = q(cborHalfPrecisionFloat.h());
        s(-7, (byte) ((q10 >> 8) & 255), (byte) ((q10 >> 0) & 255));
    }

    private void e(CborMap cborMap) {
        Set<CborObject> m10 = cborMap.m();
        if (cborMap.l()) {
            p(5);
        } else {
            n(5, m10.size());
        }
        if (m10.isEmpty()) {
            return;
        }
        if (cborMap.l()) {
            m(cborMap);
            h(new CborOther(CborOtherEnum.BREAK));
        } else {
            m(cborMap);
        }
    }

    private void f(CborNegativeInteger cborNegativeInteger) {
        o(1, BigInteger.valueOf(-1L).subtract(cborNegativeInteger.g()).abs());
    }

    private void h(CborOther cborOther) {
        switch (a.f11275b[cborOther.g().ordinal()]) {
            case 1:
                r(255);
                return;
            case 2:
                CborSimpleValue cborSimpleValue = (CborSimpleValue) cborOther;
                switch (a.f11274a[cborSimpleValue.h().ordinal()]) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        r(cborSimpleValue.h().b() | 224);
                        return;
                    case 5:
                        r(cborSimpleValue.i() | 224);
                        return;
                    case 6:
                        return;
                    default:
                        throw new CborException("Encode invalid simple type: " + cborSimpleValue.h());
                }
            case 3:
                throw new CborException("Reserved special type");
            case 4:
                if (cborOther instanceof CborHalfPrecisionFloat) {
                    d((CborHalfPrecisionFloat) cborOther);
                    return;
                }
                throw new CborException("Wrong data item type");
            case 5:
                if (cborOther instanceof CborSimplePrecisionFloat) {
                    i((CborSimplePrecisionFloat) cborOther);
                    return;
                }
                throw new CborException("Wrong data item type");
            case 6:
                if (cborOther instanceof CborDoublePrecisionFloat) {
                    c((CborDoublePrecisionFloat) cborOther);
                    return;
                }
                throw new CborException("Wrong data item type");
            case 7:
                if (!(cborOther instanceof CborSimpleValue)) {
                    throw new CborException("Wrong data item type");
                }
                s(-8, (byte) ((CborSimpleValue) cborOther).i());
                return;
            default:
                throw new CborException("Encode invalid other type: " + cborOther.g());
        }
    }

    private void i(CborSimplePrecisionFloat cborSimplePrecisionFloat) {
        int floatToRawIntBits = Float.floatToRawIntBits(cborSimplePrecisionFloat.h());
        s(-6, (byte) ((floatToRawIntBits >> 24) & 255), (byte) ((floatToRawIntBits >> 16) & 255), (byte) ((floatToRawIntBits >> 8) & 255), (byte) ((floatToRawIntBits >> 0) & 255));
    }

    private void j(CborTag cborTag) {
        n(6, cborTag.g());
    }

    private void k(CborTextString cborTextString) {
        String g6 = cborTextString.g();
        if (cborTextString.h()) {
            p(3);
            if (g6 != null) {
                byte[] bytes = g6.getBytes(StandardCharsets.UTF_8);
                n(3, bytes.length);
                s(bytes);
            }
        } else if (g6 == null) {
            h(new CborSimpleValue(CborSimpleValueEnum.NULL));
        } else {
            byte[] bytes2 = g6.getBytes(StandardCharsets.UTF_8);
            n(3, bytes2.length);
            s(bytes2);
        }
        if (cborTextString.h()) {
            h(new CborOther(CborOtherEnum.BREAK));
        }
    }

    private void l(CborUnsignedInteger cborUnsignedInteger) {
        o(0, cborUnsignedInteger.g());
    }

    private void m(CborMap cborMap) {
        for (CborObject cborObject : cborMap.m()) {
            g(cborObject);
            g(cborMap.j(cborObject));
        }
    }

    private void n(int i10, long j10) {
        o(i10, BigInteger.valueOf(j10));
    }

    private void o(int i10, BigInteger bigInteger) {
        int i11 = i10 << 5;
        if (bigInteger.compareTo(BigInteger.valueOf(24L)) < 0) {
            r(bigInteger.intValue() | i11);
            return;
        }
        if (bigInteger.compareTo(BigInteger.valueOf(256L)) < 0) {
            s((byte) (AdditionalInformationEnum.ONE_BYTE.b() | i11), (byte) bigInteger.intValue());
            return;
        }
        if (bigInteger.compareTo(BigInteger.valueOf(65536L)) < 0) {
            int b10 = AdditionalInformationEnum.TWO_BYTES.b() | i11;
            long longValue = bigInteger.longValue();
            s((byte) b10, (byte) (longValue >> 8), (byte) (longValue & 255));
        } else if (bigInteger.compareTo(BigInteger.valueOf(4294967296L)) < 0) {
            int b11 = AdditionalInformationEnum.FOUR_BYTES.b() | i11;
            long longValue2 = bigInteger.longValue();
            s((byte) b11, (byte) ((longValue2 >> 24) & 255), (byte) ((longValue2 >> 16) & 255), (byte) ((longValue2 >> 8) & 255), (byte) (longValue2 & 255));
        } else if (bigInteger.compareTo(new BigInteger("18446744073709551616")) < 0) {
            int b12 = AdditionalInformationEnum.EIGHT_BYTES.b() | i11;
            BigInteger valueOf = BigInteger.valueOf(255L);
            s((byte) b12, bigInteger.shiftRight(56).and(valueOf).byteValue(), bigInteger.shiftRight(48).and(valueOf).byteValue(), bigInteger.shiftRight(40).and(valueOf).byteValue(), bigInteger.shiftRight(32).and(valueOf).byteValue(), bigInteger.shiftRight(24).and(valueOf).byteValue(), bigInteger.shiftRight(16).and(valueOf).byteValue(), bigInteger.shiftRight(8).and(valueOf).byteValue(), bigInteger.and(valueOf).byteValue());
        } else {
            if (i10 == 1) {
                j(new CborTag(3L));
            } else {
                j(new CborTag(2L));
            }
            b(new CborByteString(bigInteger.toByteArray()));
        }
    }

    private void p(int i10) {
        r((i10 << 5) | AdditionalInformationEnum.INDEFINITE.b());
    }

    private int q(float f10) {
        int floatToIntBits = Float.floatToIntBits(f10);
        int i10 = (floatToIntBits >>> 16) & 32768;
        int i11 = (floatToIntBits + 4096) & Integer.MAX_VALUE;
        if (i11 >= 1199570944) {
            if ((Integer.MAX_VALUE & floatToIntBits) < 1199570944) {
                return i10 | 31743;
            }
            if (i11 < 2139095040) {
                return i10 | 31744;
            }
            return ((floatToIntBits & 8388607) >>> 13) | i10 | 31744;
        }
        if (i11 >= 947912704) {
            return ((i11 - 939524096) >>> 13) | i10;
        }
        if (i11 < 855638016) {
            return i10;
        }
        int i12 = (floatToIntBits & Integer.MAX_VALUE) >>> 23;
        return ((((floatToIntBits & 8388607) | 8388608) + (8388608 >>> (i12 - 102))) >>> (126 - i12)) | i10;
    }

    private void r(int i10) {
        try {
            this.f11273a.write(i10);
        } catch (IOException e10) {
            throw new CborException(e10);
        }
    }

    private void s(byte... bArr) {
        try {
            this.f11273a.write(bArr);
        } catch (IOException e10) {
            throw new CborException(e10);
        }
    }

    public void g(CborObject cborObject) {
        if (cborObject == null) {
            cborObject = new CborSimpleValue(CborSimpleValueEnum.NULL);
        }
        if (cborObject.d()) {
            j(new CborTag(cborObject.c()));
        }
        if (cborObject instanceof CborArray) {
            a((CborArray) cborObject);
            return;
        }
        if (cborObject instanceof CborByteString) {
            b((CborByteString) cborObject);
            return;
        }
        if (cborObject instanceof CborMap) {
            e((CborMap) cborObject);
            return;
        }
        if (cborObject instanceof CborOther) {
            h((CborOther) cborObject);
            return;
        }
        if (cborObject instanceof CborNegativeInteger) {
            f((CborNegativeInteger) cborObject);
            return;
        }
        if (cborObject instanceof CborTextString) {
            k((CborTextString) cborObject);
            return;
        }
        if (cborObject instanceof CborUnsignedInteger) {
            l((CborUnsignedInteger) cborObject);
            return;
        }
        if (cborObject instanceof CborTag) {
            j((CborTag) cborObject);
            return;
        }
        throw new CborException("Can't encode \"" + cborObject + "\" of type " + cborObject.getClass());
    }
}
