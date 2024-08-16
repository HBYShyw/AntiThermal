package f1;

import java.math.BigInteger;
import java.util.Objects;

/* compiled from: CborUnsignedInteger.java */
/* renamed from: f1.v, reason: use source file name */
/* loaded from: classes.dex */
public class CborUnsignedInteger extends CborObject {

    /* renamed from: c, reason: collision with root package name */
    private final BigInteger f11305c;

    public CborUnsignedInteger(long j10) {
        this(j10, -1);
    }

    @Override // f1.CborObject
    public boolean equals(Object obj) {
        if (obj instanceof CborUnsignedInteger) {
            return super.equals(obj) && this.f11305c.equals(((CborUnsignedInteger) obj).f11305c);
        }
        return false;
    }

    public BigInteger g() {
        return this.f11305c;
    }

    public Object h() {
        if (this.f11305c.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) <= 0 && this.f11305c.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) >= 0) {
            if (this.f11305c.compareTo(BigInteger.valueOf(2147483647L)) <= 0 && this.f11305c.compareTo(BigInteger.valueOf(-2147483648L)) >= 0) {
                return Integer.valueOf(this.f11305c.intValue());
            }
            return Long.valueOf(this.f11305c.longValue());
        }
        return this.f11305c;
    }

    @Override // f1.CborObject
    public int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), this.f11305c);
    }

    public String toString() {
        if (c() != -1) {
            return c() + "(" + this.f11305c.toString(10) + ")";
        }
        return this.f11305c.toString(10);
    }

    public CborUnsignedInteger(long j10, int i10) {
        this(BigInteger.valueOf(j10), i10);
    }

    public CborUnsignedInteger(BigInteger bigInteger, int i10) {
        super(0, i10);
        this.f11305c = bigInteger;
    }
}
