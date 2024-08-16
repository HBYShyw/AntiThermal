package f1;

import java.util.Objects;

/* compiled from: CborDoublePrecisionFloat.java */
/* renamed from: f1.e, reason: use source file name */
/* loaded from: classes.dex */
public class CborDoublePrecisionFloat extends CborOther {

    /* renamed from: d, reason: collision with root package name */
    private final double f11272d;

    public CborDoublePrecisionFloat(double d10) {
        super(CborOtherEnum.IEEE_754_DOUBLE_PRECISION_FLOAT);
        this.f11272d = d10;
    }

    @Override // f1.CborOther, f1.CborObject
    public boolean equals(Object obj) {
        if (obj instanceof CborDoublePrecisionFloat) {
            return super.equals(obj) && this.f11272d == ((CborDoublePrecisionFloat) obj).f11272d;
        }
        return false;
    }

    public double h() {
        return this.f11272d;
    }

    @Override // f1.CborOther, f1.CborObject
    public int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), Double.valueOf(this.f11272d));
    }

    @Override // f1.CborOther
    public String toString() {
        if (c() != -1) {
            return c() + "(" + this.f11272d + ")";
        }
        return String.valueOf(this.f11272d);
    }

    public CborDoublePrecisionFloat(double d10, long j10) {
        super(CborOtherEnum.IEEE_754_DOUBLE_PRECISION_FLOAT, j10);
        this.f11272d = d10;
    }
}
