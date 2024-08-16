package co.nstant.in.cbor.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class Array extends ChunkableDataItem {
    private final ArrayList<DataItem> objects;

    @Override // co.nstant.in.cbor.model.ChunkableDataItem
    public /* bridge */ /* synthetic */ boolean isChunked() {
        return super.isChunked();
    }

    @Override // co.nstant.in.cbor.model.ChunkableDataItem
    public /* bridge */ /* synthetic */ ChunkableDataItem setChunked(boolean z) {
        return super.setChunked(z);
    }

    public Array() {
        super(MajorType.ARRAY);
        this.objects = new ArrayList<>();
    }

    public Array add(DataItem dataItem) {
        this.objects.add(dataItem);
        return this;
    }

    public List<DataItem> getDataItems() {
        return this.objects;
    }

    @Override // co.nstant.in.cbor.model.ChunkableDataItem, co.nstant.in.cbor.model.DataItem
    public boolean equals(Object obj) {
        if (obj instanceof Array) {
            return super.equals(obj) && this.objects.equals(((Array) obj).objects);
        }
        return false;
    }

    @Override // co.nstant.in.cbor.model.ChunkableDataItem, co.nstant.in.cbor.model.DataItem
    public int hashCode() {
        return this.objects.hashCode() ^ super.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        if (isChunked()) {
            sb.append("_ ");
        }
        sb.append(Arrays.toString(this.objects.toArray()).substring(1));
        return sb.toString();
    }
}
