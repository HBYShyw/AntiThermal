package co.nstant.in.cbor.encoder;

import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.SinglePrecisionFloat;
import java.io.OutputStream;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class SinglePrecisionFloatEncoder extends AbstractEncoder<SinglePrecisionFloat> {
    public SinglePrecisionFloatEncoder(CborEncoder cborEncoder, OutputStream outputStream) {
        super(cborEncoder, outputStream);
    }

    @Override // co.nstant.in.cbor.encoder.AbstractEncoder
    public void encode(SinglePrecisionFloat singlePrecisionFloat) throws CborException {
        write(250);
        int floatToRawIntBits = Float.floatToRawIntBits(singlePrecisionFloat.getValue());
        write((floatToRawIntBits >> 24) & 255);
        write((floatToRawIntBits >> 16) & 255);
        write((floatToRawIntBits >> 8) & 255);
        write((floatToRawIntBits >> 0) & 255);
    }
}
