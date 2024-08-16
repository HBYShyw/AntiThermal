package co.nstant.in.cbor.decoder;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.Special;
import java.io.InputStream;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ArrayDecoder extends AbstractDecoder<Array> {
    public ArrayDecoder(CborDecoder cborDecoder, InputStream inputStream) {
        super(cborDecoder, inputStream);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // co.nstant.in.cbor.decoder.AbstractDecoder
    public Array decode(int i) throws CborException {
        long length = getLength(i);
        if (length == -1) {
            return decodeInfinitiveLength();
        }
        return decodeFixedLength(length);
    }

    private Array decodeInfinitiveLength() throws CborException {
        Array array = new Array();
        array.setChunked(true);
        if (this.decoder.isAutoDecodeInfinitiveArrays()) {
            while (true) {
                DataItem decodeNext = this.decoder.decodeNext();
                if (decodeNext == null) {
                    throw new CborException("Unexpected end of stream");
                }
                Special special = Special.BREAK;
                if (special.equals(decodeNext)) {
                    array.add(special);
                    break;
                }
                array.add(decodeNext);
            }
        }
        return array;
    }

    private Array decodeFixedLength(long j) throws CborException {
        Array array = new Array();
        for (long j2 = 0; j2 < j; j2++) {
            DataItem decodeNext = this.decoder.decodeNext();
            if (decodeNext == null) {
                throw new CborException("Unexpected end of stream");
            }
            array.add(decodeNext);
        }
        return array;
    }
}
