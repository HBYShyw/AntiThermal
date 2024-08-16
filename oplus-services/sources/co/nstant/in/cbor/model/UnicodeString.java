package co.nstant.in.cbor.model;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class UnicodeString extends ChunkableDataItem {
    private final String string;

    @Override // co.nstant.in.cbor.model.ChunkableDataItem
    public /* bridge */ /* synthetic */ boolean isChunked() {
        return super.isChunked();
    }

    @Override // co.nstant.in.cbor.model.ChunkableDataItem
    public /* bridge */ /* synthetic */ ChunkableDataItem setChunked(boolean z) {
        return super.setChunked(z);
    }

    public UnicodeString(String str) {
        super(MajorType.UNICODE_STRING);
        this.string = str;
    }

    public String toString() {
        String str = this.string;
        return str == null ? "null" : str;
    }

    public String getString() {
        return this.string;
    }

    @Override // co.nstant.in.cbor.model.ChunkableDataItem, co.nstant.in.cbor.model.DataItem
    public boolean equals(Object obj) {
        if (!(obj instanceof UnicodeString) || !super.equals(obj)) {
            return false;
        }
        UnicodeString unicodeString = (UnicodeString) obj;
        String str = this.string;
        if (str == null) {
            return unicodeString.string == null;
        }
        return str.equals(unicodeString.string);
    }

    @Override // co.nstant.in.cbor.model.ChunkableDataItem, co.nstant.in.cbor.model.DataItem
    public int hashCode() {
        if (this.string != null) {
            return super.hashCode() + this.string.hashCode();
        }
        return 0;
    }
}
