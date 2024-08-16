package co.nstant.in.cbor.decoder;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.UnsignedInteger;
import java.io.InputStream;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class UnsignedIntegerDecoder extends AbstractDecoder<UnsignedInteger> {
    public UnsignedIntegerDecoder(CborDecoder cborDecoder, InputStream inputStream) {
        super(cborDecoder, inputStream);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // co.nstant.in.cbor.decoder.AbstractDecoder
    public UnsignedInteger decode(int i) throws CborException {
        return new UnsignedInteger(getLengthAsBigInteger(i));
    }
}
