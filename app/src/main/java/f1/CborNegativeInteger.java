package f1;

import java.math.BigInteger;
import java.util.Objects;

/* compiled from: CborNegativeInteger.java */
/* renamed from: f1.l, reason: use source file name */
/* loaded from: classes.dex */
public class CborNegativeInteger extends CborObject {

    /* renamed from: c, reason: collision with root package name */
    private final BigInteger f11279c;

    public CborNegativeInteger(long j10) {
        this(j10, -1L);
    }

    @Override // f1.CborObject
    public boolean equals(Object obj) {
        if (obj instanceof CborNegativeInteger) {
            return super.equals(obj) && this.f11279c.equals(((CborNegativeInteger) obj).f11279c);
        }
        return false;
    }

    public BigInteger g() {
        return this.f11279c;
    }

    @Override // f1.CborObject
    public int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), this.f11279c);
    }

    public String toString() {
        if (c() != -1) {
            return c() + "(" + this.f11279c.toString(10) + ")";
        }
        return this.f11279c.toString(10);
    }

    public CborNegativeInteger(long j10, long j11) {
        this(BigInteger.valueOf(j10), -1L);
    }

    public CborNegativeInteger(BigInteger bigInteger, long j10) {
        super(1, j10);
        this.f11279c = bigInteger;
        a(bigInteger.signum() < 0, "value " + bigInteger + " is not < 0");
    }
}
