package co.nstant.in.cbor.decoder;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.HalfPrecisionFloat;
import java.io.InputStream;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class HalfPrecisionFloatDecoder extends AbstractDecoder<HalfPrecisionFloat> {
    public HalfPrecisionFloatDecoder(CborDecoder cborDecoder, InputStream inputStream) {
        super(cborDecoder, inputStream);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // co.nstant.in.cbor.decoder.AbstractDecoder
    public HalfPrecisionFloat decode(int i) throws CborException {
        return new HalfPrecisionFloat(toFloat(nextSymbol() | (nextSymbol() << 8)));
    }

    private static float toFloat(int i) {
        int i2 = (32768 & i) >> 15;
        int i3 = (i & 31744) >> 10;
        int i4 = i & 1023;
        if (i3 == 0) {
            return (float) ((i2 == 0 ? 1 : -1) * Math.pow(2.0d, -14.0d) * (i4 / Math.pow(2.0d, 10.0d)));
        }
        if (i3 != 31) {
            return (float) ((i2 == 0 ? 1 : -1) * Math.pow(2.0d, i3 - 15) * ((i4 / Math.pow(2.0d, 10.0d)) + 1.0d));
        }
        if (i4 != 0) {
            return Float.NaN;
        }
        return (i2 == 0 ? 1 : -1) * Float.POSITIVE_INFINITY;
    }
}
