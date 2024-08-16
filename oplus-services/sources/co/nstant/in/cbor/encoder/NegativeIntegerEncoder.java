package co.nstant.in.cbor.encoder;

import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.MajorType;
import co.nstant.in.cbor.model.NegativeInteger;
import java.io.OutputStream;
import java.math.BigInteger;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class NegativeIntegerEncoder extends AbstractEncoder<NegativeInteger> {
    private static final BigInteger MINUS_ONE = BigInteger.valueOf(-1);

    public NegativeIntegerEncoder(CborEncoder cborEncoder, OutputStream outputStream) {
        super(cborEncoder, outputStream);
    }

    @Override // co.nstant.in.cbor.encoder.AbstractEncoder
    public void encode(NegativeInteger negativeInteger) throws CborException {
        encodeTypeAndLength(MajorType.NEGATIVE_INTEGER, MINUS_ONE.subtract(negativeInteger.getValue()).abs());
    }
}
