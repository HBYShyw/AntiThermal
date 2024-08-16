package co.nstant.in.cbor.model;

import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class SimpleValue extends Special {
    private final SimpleValueType simpleValueType;
    private final int value;
    public static final SimpleValue FALSE = new SimpleValue(SimpleValueType.FALSE);
    public static final SimpleValue TRUE = new SimpleValue(SimpleValueType.TRUE);
    public static final SimpleValue NULL = new SimpleValue(SimpleValueType.NULL);
    public static final SimpleValue UNDEFINED = new SimpleValue(SimpleValueType.UNDEFINED);

    public SimpleValue(SimpleValueType simpleValueType) {
        super(SpecialType.SIMPLE_VALUE);
        this.value = simpleValueType.getValue();
        this.simpleValueType = simpleValueType;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public SimpleValue(int i) {
        super(r0);
        SpecialType specialType;
        if (i <= 23) {
            specialType = SpecialType.SIMPLE_VALUE;
        } else {
            specialType = SpecialType.SIMPLE_VALUE_NEXT_BYTE;
        }
        this.value = i;
        this.simpleValueType = SimpleValueType.ofByte(i);
    }

    public SimpleValueType getSimpleValueType() {
        return this.simpleValueType;
    }

    public int getValue() {
        return this.value;
    }

    @Override // co.nstant.in.cbor.model.Special, co.nstant.in.cbor.model.DataItem
    public boolean equals(Object obj) {
        if (obj instanceof SimpleValue) {
            return super.equals(obj) && this.value == ((SimpleValue) obj).value;
        }
        return false;
    }

    @Override // co.nstant.in.cbor.model.Special, co.nstant.in.cbor.model.DataItem
    public int hashCode() {
        return Objects.hashCode(Integer.valueOf(this.value)) ^ super.hashCode();
    }

    @Override // co.nstant.in.cbor.model.Special
    public String toString() {
        return this.simpleValueType.toString();
    }
}
