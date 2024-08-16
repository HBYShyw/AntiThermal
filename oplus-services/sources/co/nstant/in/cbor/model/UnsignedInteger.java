package co.nstant.in.cbor.model;

import java.math.BigInteger;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class UnsignedInteger extends Number {
    public UnsignedInteger(long j) {
        this(BigInteger.valueOf(j));
        assertTrue(j >= 0, "value " + j + " is not >= 0");
    }

    public UnsignedInteger(BigInteger bigInteger) {
        super(MajorType.UNSIGNED_INTEGER, bigInteger);
    }
}
