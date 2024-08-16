package co.nstant.in.cbor.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class Map extends ChunkableDataItem {
    private final List<DataItem> keys;
    private final HashMap<DataItem, DataItem> map;

    @Override // co.nstant.in.cbor.model.ChunkableDataItem
    public /* bridge */ /* synthetic */ boolean isChunked() {
        return super.isChunked();
    }

    @Override // co.nstant.in.cbor.model.ChunkableDataItem
    public /* bridge */ /* synthetic */ ChunkableDataItem setChunked(boolean z) {
        return super.setChunked(z);
    }

    public Map() {
        super(MajorType.MAP);
        this.keys = new LinkedList();
        this.map = new HashMap<>();
    }

    public Map(int i) {
        super(MajorType.MAP);
        this.keys = new LinkedList();
        this.map = new HashMap<>(i);
    }

    public Map put(DataItem dataItem, DataItem dataItem2) {
        if (this.map.put(dataItem, dataItem2) == null) {
            this.keys.add(dataItem);
        }
        return this;
    }

    public DataItem get(DataItem dataItem) {
        return this.map.get(dataItem);
    }

    public DataItem remove(DataItem dataItem) {
        this.keys.remove(dataItem);
        return this.map.remove(dataItem);
    }

    public Collection<DataItem> getKeys() {
        return this.keys;
    }

    public Collection<DataItem> getValues() {
        return this.map.values();
    }

    @Override // co.nstant.in.cbor.model.ChunkableDataItem, co.nstant.in.cbor.model.DataItem
    public boolean equals(Object obj) {
        if (obj instanceof Map) {
            return super.equals(obj) && this.map.equals(((Map) obj).map);
        }
        return false;
    }

    @Override // co.nstant.in.cbor.model.ChunkableDataItem, co.nstant.in.cbor.model.DataItem
    public int hashCode() {
        return this.map.hashCode() ^ super.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isChunked()) {
            sb.append("{_ ");
        } else {
            sb.append("{ ");
        }
        for (DataItem dataItem : this.keys) {
            sb.append(dataItem);
            sb.append(": ");
            sb.append(this.map.get(dataItem));
            sb.append(", ");
        }
        if (sb.toString().endsWith(", ")) {
            sb.setLength(sb.length() - 2);
        }
        sb.append(" }");
        return sb.toString();
    }
}
