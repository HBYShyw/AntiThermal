package co.nstant.in.cbor.decoder;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.NegativeInteger;
import java.io.InputStream;
import java.math.BigInteger;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class NegativeIntegerDecoder extends AbstractDecoder<NegativeInteger> {
    private static final BigInteger MINUS_ONE = BigInteger.valueOf(-1);

    public NegativeIntegerDecoder(CborDecoder cborDecoder, InputStream inputStream) {
        super(cborDecoder, inputStream);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // co.nstant.in.cbor.decoder.AbstractDecoder
    public NegativeInteger decode(int i) throws CborException {
        return new NegativeInteger(MINUS_ONE.subtract(getLengthAsBigInteger(i)));
    }
}
