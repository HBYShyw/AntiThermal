package co.nstant.in.cbor.decoder;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.MajorType;
import co.nstant.in.cbor.model.Special;
import co.nstant.in.cbor.model.UnicodeString;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class UnicodeStringDecoder extends AbstractDecoder<UnicodeString> {
    public UnicodeStringDecoder(CborDecoder cborDecoder, InputStream inputStream) {
        super(cborDecoder, inputStream);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // co.nstant.in.cbor.decoder.AbstractDecoder
    public UnicodeString decode(int i) throws CborException {
        long length = getLength(i);
        if (length == -1) {
            if (this.decoder.isAutoDecodeInfinitiveUnicodeStrings()) {
                return decodeInfinitiveLength();
            }
            UnicodeString unicodeString = new UnicodeString(null);
            unicodeString.setChunked(true);
            return unicodeString;
        }
        return decodeFixedLength(length);
    }

    private UnicodeString decodeInfinitiveLength() throws CborException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (true) {
            DataItem decodeNext = this.decoder.decodeNext();
            if (decodeNext == null) {
                throw new CborException("Unexpected end of stream");
            }
            MajorType majorType = decodeNext.getMajorType();
            if (!Special.BREAK.equals(decodeNext)) {
                if (majorType == MajorType.UNICODE_STRING) {
                    byte[] bytes = ((UnicodeString) decodeNext).toString().getBytes(StandardCharsets.UTF_8);
                    byteArrayOutputStream.write(bytes, 0, bytes.length);
                } else {
                    throw new CborException("Unexpected major type " + majorType);
                }
            } else {
                return new UnicodeString(new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8));
            }
        }
    }

    private UnicodeString decodeFixedLength(long j) throws CborException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream((int) j);
        for (long j2 = 0; j2 < j; j2++) {
            byteArrayOutputStream.write(nextSymbol());
        }
        return new UnicodeString(new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8));
    }
}
