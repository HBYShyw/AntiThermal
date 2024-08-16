package co.nstant.in.cbor.encoder;

import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.MajorType;
import co.nstant.in.cbor.model.UnsignedInteger;
import java.io.OutputStream;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class UnsignedIntegerEncoder extends AbstractEncoder<UnsignedInteger> {
    public UnsignedIntegerEncoder(CborEncoder cborEncoder, OutputStream outputStream) {
        super(cborEncoder, outputStream);
    }

    @Override // co.nstant.in.cbor.encoder.AbstractEncoder
    public void encode(UnsignedInteger unsignedInteger) throws CborException {
        encodeTypeAndLength(MajorType.UNSIGNED_INTEGER, unsignedInteger.getValue());
    }
}
