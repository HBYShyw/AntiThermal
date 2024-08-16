package co.nstant.in.cbor.builder;

import co.nstant.in.cbor.builder.AbstractBuilder;
import co.nstant.in.cbor.model.Special;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ByteStringBuilder<T extends AbstractBuilder<?>> extends AbstractBuilder<T> {
    public ByteStringBuilder(T t) {
        super(t);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ByteStringBuilder<T> add(byte[] bArr) {
        ((AbstractBuilder) getParent()).addChunk(convert(bArr));
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public T end() {
        ((AbstractBuilder) getParent()).addChunk(Special.BREAK);
        return (T) getParent();
    }
}
