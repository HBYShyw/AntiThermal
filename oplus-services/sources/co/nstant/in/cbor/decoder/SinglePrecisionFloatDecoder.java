package co.nstant.in.cbor.decoder;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.SinglePrecisionFloat;
import java.io.InputStream;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class SinglePrecisionFloatDecoder extends AbstractDecoder<SinglePrecisionFloat> {
    public SinglePrecisionFloatDecoder(CborDecoder cborDecoder, InputStream inputStream) {
        super(cborDecoder, inputStream);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // co.nstant.in.cbor.decoder.AbstractDecoder
    public SinglePrecisionFloat decode(int i) throws CborException {
        return new SinglePrecisionFloat(Float.intBitsToFloat((nextSymbol() & 255) | (((((((nextSymbol() & 255) | 0) << 8) | (nextSymbol() & 255)) << 8) | (nextSymbol() & 255)) << 8)));
    }
}
