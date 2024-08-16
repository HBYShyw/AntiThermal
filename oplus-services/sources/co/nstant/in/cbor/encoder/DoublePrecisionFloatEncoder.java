package co.nstant.in.cbor.encoder;

import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.DoublePrecisionFloat;
import java.io.OutputStream;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DoublePrecisionFloatEncoder extends AbstractEncoder<DoublePrecisionFloat> {
    public DoublePrecisionFloatEncoder(CborEncoder cborEncoder, OutputStream outputStream) {
        super(cborEncoder, outputStream);
    }

    @Override // co.nstant.in.cbor.encoder.AbstractEncoder
    public void encode(DoublePrecisionFloat doublePrecisionFloat) throws CborException {
        write(251);
        long doubleToRawLongBits = Double.doubleToRawLongBits(doublePrecisionFloat.getValue());
        write((int) ((doubleToRawLongBits >> 56) & 255));
        write((int) ((doubleToRawLongBits >> 48) & 255));
        write((int) ((doubleToRawLongBits >> 40) & 255));
        write((int) ((doubleToRawLongBits >> 32) & 255));
        write((int) ((doubleToRawLongBits >> 24) & 255));
        write((int) ((doubleToRawLongBits >> 16) & 255));
        write((int) ((doubleToRawLongBits >> 8) & 255));
        write((int) ((doubleToRawLongBits >> 0) & 255));
    }
}
