package co.nstant.in.cbor.model;

import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DoublePrecisionFloat extends Special {
    private final double value;

    public DoublePrecisionFloat(double d) {
        super(SpecialType.IEEE_754_DOUBLE_PRECISION_FLOAT);
        this.value = d;
    }

    public double getValue() {
        return this.value;
    }

    @Override // co.nstant.in.cbor.model.Special, co.nstant.in.cbor.model.DataItem
    public boolean equals(Object obj) {
        if (obj instanceof DoublePrecisionFloat) {
            return super.equals(obj) && this.value == ((DoublePrecisionFloat) obj).value;
        }
        return false;
    }

    @Override // co.nstant.in.cbor.model.Special, co.nstant.in.cbor.model.DataItem
    public int hashCode() {
        return Objects.hashCode(Double.valueOf(this.value)) ^ super.hashCode();
    }

    @Override // co.nstant.in.cbor.model.Special
    public String toString() {
        return String.valueOf(this.value);
    }
}
