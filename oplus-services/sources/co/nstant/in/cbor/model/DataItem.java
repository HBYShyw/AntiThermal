package co.nstant.in.cbor.model;

import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DataItem {
    private final MajorType majorType;
    private Tag tag;

    /* JADX INFO: Access modifiers changed from: protected */
    public DataItem(MajorType majorType) {
        this.majorType = majorType;
        Objects.requireNonNull(majorType, "majorType is null");
    }

    public MajorType getMajorType() {
        return this.majorType;
    }

    public void setTag(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("tag number must be 0 or greater");
        }
        this.tag = new Tag(i);
    }

    public void setTag(Tag tag) {
        Objects.requireNonNull(tag, "tag is null");
        this.tag = tag;
    }

    public void removeTag() {
        this.tag = null;
    }

    public Tag getTag() {
        return this.tag;
    }

    public boolean hasTag() {
        return this.tag != null;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DataItem)) {
            return false;
        }
        DataItem dataItem = (DataItem) obj;
        Tag tag = this.tag;
        return tag != null ? tag.equals(dataItem.tag) && this.majorType == dataItem.majorType : dataItem.tag == null && this.majorType == dataItem.majorType;
    }

    public int hashCode() {
        return Objects.hash(this.majorType, this.tag);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void assertTrue(boolean z, String str) {
        if (!z) {
            throw new IllegalArgumentException(str);
        }
    }
}
