package co.nstant.in.cbor.model;

import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AbstractFloat extends Special {
    private final float value;

    public AbstractFloat(SpecialType specialType, float f) {
        super(specialType);
        this.value = f;
    }

    public float getValue() {
        return this.value;
    }

    @Override // co.nstant.in.cbor.model.Special, co.nstant.in.cbor.model.DataItem
    public boolean equals(Object obj) {
        if (obj instanceof AbstractFloat) {
            return super.equals(obj) && this.value == ((AbstractFloat) obj).value;
        }
        return false;
    }

    @Override // co.nstant.in.cbor.model.Special, co.nstant.in.cbor.model.DataItem
    public int hashCode() {
        return Objects.hashCode(Float.valueOf(this.value)) ^ super.hashCode();
    }
}
