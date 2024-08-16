package co.nstant.in.cbor.model;

import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class Tag extends DataItem {
    private final long value;

    public Tag(long j) {
        super(MajorType.TAG);
        this.value = j;
    }

    public long getValue() {
        return this.value;
    }

    @Override // co.nstant.in.cbor.model.DataItem
    public boolean equals(Object obj) {
        if (obj instanceof Tag) {
            return super.equals(obj) && this.value == ((Tag) obj).value;
        }
        return false;
    }

    @Override // co.nstant.in.cbor.model.DataItem
    public int hashCode() {
        return Objects.hashCode(Long.valueOf(this.value)) ^ super.hashCode();
    }

    public String toString() {
        return "Tag(" + this.value + ")";
    }
}
