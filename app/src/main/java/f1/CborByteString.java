package f1;

import e1.HexStringUtils;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;

/* compiled from: CborByteString.java */
/* renamed from: f1.c, reason: use source file name */
/* loaded from: classes.dex */
public class CborByteString extends CborObject {

    /* renamed from: c, reason: collision with root package name */
    private final byte[] f11267c;

    /* renamed from: d, reason: collision with root package name */
    private boolean f11268d;

    public CborByteString(byte[] bArr) {
        this(bArr, -1L);
    }

    @Override // f1.CborObject
    public boolean equals(Object obj) {
        if (!(obj instanceof CborByteString)) {
            return false;
        }
        CborByteString cborByteString = (CborByteString) obj;
        return this.f11268d == cborByteString.f11268d && super.equals(obj) && Arrays.equals(this.f11267c, cborByteString.f11267c);
    }

    public byte[] g() {
        return this.f11267c;
    }

    public boolean h() {
        return this.f11268d;
    }

    @Override // f1.CborObject
    public int hashCode() {
        return (Objects.hash(Integer.valueOf(super.hashCode()), Boolean.valueOf(this.f11268d)) * 31) + Arrays.hashCode(this.f11267c);
    }

    public byte[] i() {
        byte[] bArr = this.f11267c;
        return Arrays.copyOf(bArr, bArr.length);
    }

    public String toString() {
        if (c() == 2) {
            return new BigInteger(this.f11267c).toString(10);
        }
        if (c() == 3) {
            return BigInteger.valueOf(-1L).subtract(new BigInteger(this.f11267c)).toString(10);
        }
        String str = "h'" + HexStringUtils.a(this.f11267c) + "'";
        if (h()) {
            str = "_ " + str;
        }
        if (c() == -1) {
            return str;
        }
        return c() + "(" + str + ")";
    }

    public CborByteString(byte[] bArr, long j10) {
        super(2, j10);
        this.f11268d = false;
        this.f11267c = bArr;
    }
}
