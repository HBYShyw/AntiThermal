package co.nstant.in.cbor.builder;

import co.nstant.in.cbor.builder.AbstractBuilder;
import co.nstant.in.cbor.model.Special;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class UnicodeStringBuilder<T extends AbstractBuilder<?>> extends AbstractBuilder<T> {
    public UnicodeStringBuilder(T t) {
        super(t);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public UnicodeStringBuilder<T> add(String str) {
        ((AbstractBuilder) getParent()).addChunk(convert(str));
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public T end() {
        ((AbstractBuilder) getParent()).addChunk(Special.BREAK);
        return (T) getParent();
    }
}
