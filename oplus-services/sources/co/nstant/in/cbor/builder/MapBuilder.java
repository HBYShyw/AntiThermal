package co.nstant.in.cbor.builder;

import co.nstant.in.cbor.builder.AbstractBuilder;
import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.Map;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class MapBuilder<T extends AbstractBuilder<?>> extends AbstractBuilder<T> {
    private final Map map;

    public MapBuilder(T t, Map map) {
        super(t);
        this.map = map;
    }

    public MapBuilder<T> put(DataItem dataItem, DataItem dataItem2) {
        this.map.put(dataItem, dataItem2);
        return this;
    }

    public MapBuilder<T> put(long j, long j2) {
        put(convert(j), convert(j2));
        return this;
    }

    public MapBuilder<T> put(long j, boolean z) {
        put(convert(j), convert(z));
        return this;
    }

    public MapBuilder<T> put(long j, float f) {
        put(convert(j), convert(f));
        return this;
    }

    public MapBuilder<T> put(long j, double d) {
        put(convert(j), convert(d));
        return this;
    }

    public MapBuilder<T> put(long j, byte[] bArr) {
        put(convert(j), convert(bArr));
        return this;
    }

    public MapBuilder<T> put(long j, String str) {
        put(convert(j), convert(str));
        return this;
    }

    public MapBuilder<T> put(String str, long j) {
        put(convert(str), convert(j));
        return this;
    }

    public MapBuilder<T> put(String str, boolean z) {
        put(convert(str), convert(z));
        return this;
    }

    public MapBuilder<T> put(String str, float f) {
        put(convert(str), convert(f));
        return this;
    }

    public MapBuilder<T> put(String str, double d) {
        put(convert(str), convert(d));
        return this;
    }

    public MapBuilder<T> put(String str, byte[] bArr) {
        this.map.put(convert(str), convert(bArr));
        return this;
    }

    public MapBuilder<T> put(String str, String str2) {
        put(convert(str), convert(str2));
        return this;
    }

    public ArrayBuilder<MapBuilder<T>> putArray(DataItem dataItem) {
        Array array = new Array();
        put(dataItem, array);
        return new ArrayBuilder<>(this, array);
    }

    public ArrayBuilder<MapBuilder<T>> putArray(long j) {
        Array array = new Array();
        put(convert(j), array);
        return new ArrayBuilder<>(this, array);
    }

    public ArrayBuilder<MapBuilder<T>> putArray(String str) {
        Array array = new Array();
        put(convert(str), array);
        return new ArrayBuilder<>(this, array);
    }

    public ArrayBuilder<MapBuilder<T>> startArray(DataItem dataItem) {
        Array array = new Array();
        array.setChunked(true);
        put(dataItem, array);
        return new ArrayBuilder<>(this, array);
    }

    public ArrayBuilder<MapBuilder<T>> startArray(long j) {
        return startArray(convert(j));
    }

    public ArrayBuilder<MapBuilder<T>> startArray(String str) {
        Array array = new Array();
        array.setChunked(true);
        put(convert(str), array);
        return new ArrayBuilder<>(this, array);
    }

    public MapBuilder<MapBuilder<T>> putMap(DataItem dataItem) {
        Map map = new Map();
        put(dataItem, map);
        return new MapBuilder<>(this, map);
    }

    public MapBuilder<MapBuilder<T>> putMap(long j) {
        Map map = new Map();
        put(convert(j), map);
        return new MapBuilder<>(this, map);
    }

    public MapBuilder<MapBuilder<T>> putMap(String str) {
        Map map = new Map();
        put(convert(str), map);
        return new MapBuilder<>(this, map);
    }

    public MapBuilder<MapBuilder<T>> startMap(DataItem dataItem) {
        Map map = new Map();
        map.setChunked(true);
        put(dataItem, map);
        return new MapBuilder<>(this, map);
    }

    public MapBuilder<MapBuilder<T>> startMap(long j) {
        return startMap(convert(j));
    }

    public MapBuilder<MapBuilder<T>> startMap(String str) {
        return startMap(convert(str));
    }

    public T end() {
        return (T) getParent();
    }
}
