package co.nstant.in.cbor.decoder;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.Map;
import co.nstant.in.cbor.model.Special;
import java.io.InputStream;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class MapDecoder extends AbstractDecoder<Map> {
    public MapDecoder(CborDecoder cborDecoder, InputStream inputStream) {
        super(cborDecoder, inputStream);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // co.nstant.in.cbor.decoder.AbstractDecoder
    public Map decode(int i) throws CborException {
        long length = getLength(i);
        if (length == -1) {
            return decodeInfinitiveLength();
        }
        return decodeFixedLength(length);
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x004c, code lost:
    
        throw new co.nstant.in.cbor.CborException("Unexpected end of stream");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private Map decodeInfinitiveLength() throws CborException {
        Map map = new Map();
        map.setChunked(true);
        if (this.decoder.isAutoDecodeInfinitiveMaps()) {
            while (true) {
                DataItem decodeNext = this.decoder.decodeNext();
                if (Special.BREAK.equals(decodeNext)) {
                    break;
                }
                DataItem decodeNext2 = this.decoder.decodeNext();
                if (decodeNext == null || decodeNext2 == null) {
                    break;
                }
                if (this.decoder.isRejectDuplicateKeys() && map.get(decodeNext) != null) {
                    throw new CborException("Duplicate key found in map");
                }
                map.put(decodeNext, decodeNext2);
            }
        }
        return map;
    }

    private Map decodeFixedLength(long j) throws CborException {
        Map map = new Map((int) j);
        for (long j2 = 0; j2 < j; j2++) {
            DataItem decodeNext = this.decoder.decodeNext();
            DataItem decodeNext2 = this.decoder.decodeNext();
            if (decodeNext == null || decodeNext2 == null) {
                throw new CborException("Unexpected end of stream");
            }
            if (this.decoder.isRejectDuplicateKeys() && map.get(decodeNext) != null) {
                throw new CborException("Duplicate key found in map");
            }
            map.put(decodeNext, decodeNext2);
        }
        return map;
    }
}
