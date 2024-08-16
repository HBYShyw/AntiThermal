package f1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/* compiled from: CborHelper.java */
/* renamed from: f1.i, reason: use source file name */
/* loaded from: classes.dex */
public class CborHelper {
    private static CborObject a(long j10) {
        if (j10 >= 0) {
            return new CborUnsignedInteger(j10);
        }
        return new CborNegativeInteger(j10);
    }

    public static CborMap b(byte[] bArr) {
        List<CborObject> c10 = c(bArr);
        if (c10.size() == 1) {
            CborObject cborObject = c10.get(0);
            if (cborObject instanceof CborMap) {
                return (CborMap) cborObject;
            }
            throw new CborException("Invalid major type value " + cborObject.b());
        }
        throw new CborException("Contains an inappropriate amount of data items " + c10.size());
    }

    public static List<CborObject> c(byte[] bArr) {
        ByteArrayInputStream byteArrayInputStream = null;
        try {
            try {
                ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream(bArr);
                try {
                    List<CborObject> a10 = new CborDecoder(byteArrayInputStream2).a();
                    try {
                        byteArrayInputStream2.close();
                    } catch (IOException e10) {
                        e1.i.b("CborHelper", "createFromCborByteArray error. " + e10);
                    }
                    return a10;
                } catch (CborException e11) {
                    e = e11;
                    throw new CborException(e);
                } catch (Throwable th) {
                    th = th;
                    byteArrayInputStream = byteArrayInputStream2;
                    if (byteArrayInputStream != null) {
                        try {
                            byteArrayInputStream.close();
                        } catch (IOException e12) {
                            e1.i.b("CborHelper", "createFromCborByteArray error. " + e12);
                        }
                    }
                    throw th;
                }
            } catch (CborException e13) {
                e = e13;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static CborObject d(Object obj) {
        int i10;
        if (obj == null) {
            return new CborSimpleValue(CborSimpleValueEnum.NULL);
        }
        if (obj instanceof CborObject) {
            return (CborObject) obj;
        }
        if (obj instanceof Integer) {
            return a(((Integer) obj).intValue());
        }
        if (obj instanceof Long) {
            return a(((Long) obj).longValue());
        }
        if (obj instanceof Short) {
            return a(((Short) obj).shortValue());
        }
        if (obj instanceof Float) {
            return new CborSimplePrecisionFloat(((Float) obj).floatValue());
        }
        if (obj instanceof Double) {
            return new CborDoublePrecisionFloat(((Double) obj).doubleValue());
        }
        if (obj instanceof byte[]) {
            return new CborByteString((byte[]) obj);
        }
        if (obj instanceof String) {
            return new CborTextString((String) obj);
        }
        if (obj instanceof Boolean) {
            if (((Boolean) obj).booleanValue()) {
                return new CborSimpleValue(CborSimpleValueEnum.TRUE);
            }
            return new CborSimpleValue(CborSimpleValueEnum.FALSE);
        }
        if (obj instanceof Map) {
            return new CborMap((Map<?, ?>) obj);
        }
        if (obj instanceof Object[]) {
            return new CborArray((Object[]) obj);
        }
        if (obj instanceof BigInteger) {
            BigInteger bigInteger = (BigInteger) obj;
            if (bigInteger.signum() < 0) {
                i10 = 3;
                bigInteger = BigInteger.valueOf(-1L).subtract(bigInteger).abs();
            } else {
                i10 = 2;
            }
            byte[] byteArray = bigInteger.toByteArray();
            if (byteArray.length > 0 && byteArray[0] == 0) {
                byte[] bArr = new byte[byteArray.length - 1];
                System.arraycopy(byteArray, 1, bArr, 0, byteArray.length - 1);
                return new CborByteString(bArr, i10);
            }
            return new CborByteString(byteArray, i10);
        }
        throw new CborException("Unable to convert java type \"" + obj.getClass().getCanonicalName() + "\" to CborObject");
    }
}
