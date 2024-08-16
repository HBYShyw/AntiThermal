package co.nstant.in.cbor.model;

import java.math.BigInteger;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class Number extends DataItem {
    private final BigInteger value;

    /* JADX INFO: Access modifiers changed from: protected */
    public Number(MajorType majorType, BigInteger bigInteger) {
        super(majorType);
        Objects.requireNonNull(bigInteger);
        this.value = bigInteger;
    }

    public BigInteger getValue() {
        return this.value;
    }

    @Override // co.nstant.in.cbor.model.DataItem
    public boolean equals(Object obj) {
        if (obj instanceof Number) {
            return super.equals(obj) && this.value.equals(((Number) obj).value);
        }
        return false;
    }

    @Override // co.nstant.in.cbor.model.DataItem
    public int hashCode() {
        return this.value.hashCode() ^ super.hashCode();
    }

    public String toString() {
        return this.value.toString();
    }
}
