package co.nstant.in.cbor;

import co.nstant.in.cbor.builder.AbstractBuilder;
import co.nstant.in.cbor.builder.ArrayBuilder;
import co.nstant.in.cbor.builder.ByteStringBuilder;
import co.nstant.in.cbor.builder.MapBuilder;
import co.nstant.in.cbor.builder.UnicodeStringBuilder;
import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.Map;
import co.nstant.in.cbor.model.UnicodeString;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class CborBuilder extends AbstractBuilder<CborBuilder> {
    private final List<DataItem> dataItems;

    public CborBuilder() {
        super(null);
        this.dataItems = new LinkedList();
    }

    public CborBuilder reset() {
        this.dataItems.clear();
        return this;
    }

    public List<DataItem> build() {
        return this.dataItems;
    }

    public CborBuilder add(DataItem dataItem) {
        this.dataItems.add(dataItem);
        return this;
    }

    public CborBuilder add(long j) {
        add(convert(j));
        return this;
    }

    public CborBuilder add(BigInteger bigInteger) {
        add(convert(bigInteger));
        return this;
    }

    public CborBuilder add(boolean z) {
        add(convert(z));
        return this;
    }

    public CborBuilder add(float f) {
        add(convert(f));
        return this;
    }

    public CborBuilder add(double d) {
        add(convert(d));
        return this;
    }

    public CborBuilder add(byte[] bArr) {
        add(convert(bArr));
        return this;
    }

    public ByteStringBuilder<CborBuilder> startByteString() {
        return startByteString(null);
    }

    public ByteStringBuilder<CborBuilder> startByteString(byte[] bArr) {
        add(new ByteString(bArr).setChunked(true));
        return new ByteStringBuilder<>(this);
    }

    public CborBuilder add(String str) {
        add(convert(str));
        return this;
    }

    public UnicodeStringBuilder<CborBuilder> startString() {
        return startString(null);
    }

    public UnicodeStringBuilder<CborBuilder> startString(String str) {
        add(new UnicodeString(str).setChunked(true));
        return new UnicodeStringBuilder<>(this);
    }

    public CborBuilder addTag(long j) {
        add(tag(j));
        return this;
    }

    public ArrayBuilder<CborBuilder> startArray() {
        Array array = new Array();
        array.setChunked(true);
        add(array);
        return new ArrayBuilder<>(this, array);
    }

    public ArrayBuilder<CborBuilder> addArray() {
        Array array = new Array();
        add(array);
        return new ArrayBuilder<>(this, array);
    }

    public MapBuilder<CborBuilder> addMap() {
        Map map = new Map();
        add(map);
        return new MapBuilder<>(this, map);
    }

    public MapBuilder<CborBuilder> startMap() {
        Map map = new Map();
        map.setChunked(true);
        add(map);
        return new MapBuilder<>(this, map);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // co.nstant.in.cbor.builder.AbstractBuilder
    public void addChunk(DataItem dataItem) {
        add(dataItem);
    }
}
