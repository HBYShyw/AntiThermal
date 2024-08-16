package co.nstant.in.cbor.encoder;

import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.MajorType;
import co.nstant.in.cbor.model.SimpleValue;
import co.nstant.in.cbor.model.UnicodeString;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class UnicodeStringEncoder extends AbstractEncoder<UnicodeString> {
    public UnicodeStringEncoder(CborEncoder cborEncoder, OutputStream outputStream) {
        super(cborEncoder, outputStream);
    }

    @Override // co.nstant.in.cbor.encoder.AbstractEncoder
    public void encode(UnicodeString unicodeString) throws CborException {
        String string = unicodeString.getString();
        if (unicodeString.isChunked()) {
            encodeTypeChunked(MajorType.UNICODE_STRING);
            if (string != null) {
                encode(new UnicodeString(string));
                return;
            }
            return;
        }
        if (string == null) {
            this.encoder.encode(SimpleValue.NULL);
            return;
        }
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        encodeTypeAndLength(MajorType.UNICODE_STRING, bytes.length);
        write(bytes);
    }
}
