package co.nstant.in.cbor.decoder;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.DoublePrecisionFloat;
import java.io.InputStream;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DoublePrecisionFloatDecoder extends AbstractDecoder<DoublePrecisionFloat> {
    public DoublePrecisionFloatDecoder(CborDecoder cborDecoder, InputStream inputStream) {
        super(cborDecoder, inputStream);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // co.nstant.in.cbor.decoder.AbstractDecoder
    public DoublePrecisionFloat decode(int i) throws CborException {
        return new DoublePrecisionFloat(Double.longBitsToDouble((nextSymbol() & 255) | (((((((((((((((nextSymbol() & 255) | 0) << 8) | (nextSymbol() & 255)) << 8) | (nextSymbol() & 255)) << 8) | (nextSymbol() & 255)) << 8) | (nextSymbol() & 255)) << 8) | (nextSymbol() & 255)) << 8) | (nextSymbol() & 255)) << 8)));
    }
}
