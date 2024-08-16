package co.nstant.in.cbor.decoder;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.MajorType;
import co.nstant.in.cbor.model.Special;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ByteStringDecoder extends AbstractDecoder<ByteString> {
    public ByteStringDecoder(CborDecoder cborDecoder, InputStream inputStream) {
        super(cborDecoder, inputStream);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // co.nstant.in.cbor.decoder.AbstractDecoder
    public ByteString decode(int i) throws CborException {
        long length = getLength(i);
        if (length == -1) {
            if (this.decoder.isAutoDecodeInfinitiveByteStrings()) {
                return decodeInfinitiveLength();
            }
            ByteString byteString = new ByteString(null);
            byteString.setChunked(true);
            return byteString;
        }
        return decodeFixedLength(length);
    }

    private ByteString decodeInfinitiveLength() throws CborException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (true) {
            DataItem decodeNext = this.decoder.decodeNext();
            if (decodeNext == null) {
                throw new CborException("Unexpected end of stream");
            }
            MajorType majorType = decodeNext.getMajorType();
            if (!Special.BREAK.equals(decodeNext)) {
                if (majorType == MajorType.BYTE_STRING) {
                    byte[] bytes = ((ByteString) decodeNext).getBytes();
                    if (bytes != null) {
                        byteArrayOutputStream.write(bytes, 0, bytes.length);
                    }
                } else {
                    throw new CborException("Unexpected major type " + majorType);
                }
            } else {
                return new ByteString(byteArrayOutputStream.toByteArray());
            }
        }
    }

    private ByteString decodeFixedLength(long j) throws CborException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream((int) j);
        for (long j2 = 0; j2 < j; j2++) {
            byteArrayOutputStream.write(nextSymbol());
        }
        return new ByteString(byteArrayOutputStream.toByteArray());
    }
}
