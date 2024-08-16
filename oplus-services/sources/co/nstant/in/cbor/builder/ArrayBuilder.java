package co.nstant.in.cbor.builder;

import co.nstant.in.cbor.builder.AbstractBuilder;
import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.Map;
import co.nstant.in.cbor.model.Special;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ArrayBuilder<T extends AbstractBuilder<?>> extends AbstractBuilder<T> {
    private final Array array;

    public ArrayBuilder(T t, Array array) {
        super(t);
        this.array = array;
    }

    public ArrayBuilder<T> add(DataItem dataItem) {
        this.array.add(dataItem);
        return this;
    }

    public ArrayBuilder<T> add(long j) {
        add(convert(j));
        return this;
    }

    public ArrayBuilder<T> add(boolean z) {
        add(convert(z));
        return this;
    }

    public ArrayBuilder<T> add(float f) {
        add(convert(f));
        return this;
    }

    public ArrayBuilder<T> add(double d) {
        add(convert(d));
        return this;
    }

    public ArrayBuilder<T> add(byte[] bArr) {
        add(convert(bArr));
        return this;
    }

    public ArrayBuilder<T> add(String str) {
        add(convert(str));
        return this;
    }

    public ArrayBuilder<ArrayBuilder<T>> addArray() {
        Array array = new Array();
        add(array);
        return new ArrayBuilder<>(this, array);
    }

    public ArrayBuilder<ArrayBuilder<T>> startArray() {
        Array array = new Array();
        array.setChunked(true);
        add(array);
        return new ArrayBuilder<>(this, array);
    }

    public MapBuilder<ArrayBuilder<T>> addMap() {
        Map map = new Map();
        add(map);
        return new MapBuilder<>(this, map);
    }

    public MapBuilder<ArrayBuilder<T>> startMap() {
        Map map = new Map();
        map.setChunked(true);
        add(map);
        return new MapBuilder<>(this, map);
    }

    public T end() {
        if (this.array.isChunked()) {
            add(Special.BREAK);
        }
        return (T) getParent();
    }
}
