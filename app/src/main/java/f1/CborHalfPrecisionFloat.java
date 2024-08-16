package f1;

import java.util.Objects;

/* compiled from: CborHalfPrecisionFloat.java */
/* renamed from: f1.h, reason: use source file name */
/* loaded from: classes.dex */
public class CborHalfPrecisionFloat extends CborOther {

    /* renamed from: d, reason: collision with root package name */
    private final float f11276d;

    public CborHalfPrecisionFloat(float f10, long j10) {
        super(CborOtherEnum.IEEE_754_HALF_PRECISION_FLOAT, j10);
        this.f11276d = f10;
    }

    @Override // f1.CborOther, f1.CborObject
    public boolean equals(Object obj) {
        if (obj instanceof CborHalfPrecisionFloat) {
            return super.equals(obj) && this.f11276d == ((CborHalfPrecisionFloat) obj).f11276d;
        }
        return false;
    }

    public float h() {
        return this.f11276d;
    }

    @Override // f1.CborOther, f1.CborObject
    public int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), Float.valueOf(this.f11276d));
    }

    @Override // f1.CborOther
    public String toString() {
        if (c() != -1) {
            return c() + "(" + this.f11276d + ")";
        }
        return String.valueOf(this.f11276d);
    }
}
