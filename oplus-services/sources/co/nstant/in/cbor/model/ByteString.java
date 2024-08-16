package co.nstant.in.cbor.model;

import java.util.Arrays;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ByteString extends ChunkableDataItem {
    private final byte[] bytes;

    @Override // co.nstant.in.cbor.model.ChunkableDataItem
    public /* bridge */ /* synthetic */ boolean isChunked() {
        return super.isChunked();
    }

    @Override // co.nstant.in.cbor.model.ChunkableDataItem
    public /* bridge */ /* synthetic */ ChunkableDataItem setChunked(boolean z) {
        return super.setChunked(z);
    }

    public ByteString(byte[] bArr) {
        super(MajorType.BYTE_STRING);
        if (bArr == null) {
            this.bytes = null;
        } else {
            this.bytes = bArr;
        }
    }

    public byte[] getBytes() {
        byte[] bArr = this.bytes;
        if (bArr == null) {
            return null;
        }
        return bArr;
    }

    @Override // co.nstant.in.cbor.model.ChunkableDataItem, co.nstant.in.cbor.model.DataItem
    public boolean equals(Object obj) {
        if (obj instanceof ByteString) {
            return super.equals(obj) && Arrays.equals(this.bytes, ((ByteString) obj).bytes);
        }
        return false;
    }

    @Override // co.nstant.in.cbor.model.ChunkableDataItem, co.nstant.in.cbor.model.DataItem
    public int hashCode() {
        return Arrays.hashCode(this.bytes) ^ super.hashCode();
    }
}
