package f1;

import java.util.Objects;

/* compiled from: CborSimplePrecisionFloat.java */
/* renamed from: f1.p, reason: use source file name */
/* loaded from: classes.dex */
public class CborSimplePrecisionFloat extends CborOther {

    /* renamed from: d, reason: collision with root package name */
    private final float f11291d;

    public CborSimplePrecisionFloat(float f10) {
        super(CborOtherEnum.IEEE_754_SINGLE_PRECISION_FLOAT);
        this.f11291d = f10;
    }

    @Override // f1.CborOther, f1.CborObject
    public boolean equals(Object obj) {
        if (obj instanceof CborSimplePrecisionFloat) {
            return super.equals(obj) && this.f11291d == ((CborSimplePrecisionFloat) obj).f11291d;
        }
        return false;
    }

    public float h() {
        return this.f11291d;
    }

    @Override // f1.CborOther, f1.CborObject
    public int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), Float.valueOf(this.f11291d));
    }

    @Override // f1.CborOther
    public String toString() {
        if (c() != -1) {
            return c() + "(" + this.f11291d + ")";
        }
        return String.valueOf(this.f11291d);
    }

    public CborSimplePrecisionFloat(float f10, long j10) {
        super(CborOtherEnum.IEEE_754_SINGLE_PRECISION_FLOAT, j10);
        this.f11291d = f10;
    }
}
