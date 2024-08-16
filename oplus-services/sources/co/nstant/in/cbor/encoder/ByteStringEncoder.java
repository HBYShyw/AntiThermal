package co.nstant.in.cbor.encoder;

import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.ByteString;
import co.nstant.in.cbor.model.MajorType;
import co.nstant.in.cbor.model.SimpleValue;
import java.io.OutputStream;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ByteStringEncoder extends AbstractEncoder<ByteString> {
    public ByteStringEncoder(CborEncoder cborEncoder, OutputStream outputStream) {
        super(cborEncoder, outputStream);
    }

    @Override // co.nstant.in.cbor.encoder.AbstractEncoder
    public void encode(ByteString byteString) throws CborException {
        byte[] bytes = byteString.getBytes();
        if (byteString.isChunked()) {
            encodeTypeChunked(MajorType.BYTE_STRING);
            if (bytes != null) {
                encode(new ByteString(bytes));
                return;
            }
            return;
        }
        if (bytes == null) {
            this.encoder.encode(SimpleValue.NULL);
        } else {
            encodeTypeAndLength(MajorType.BYTE_STRING, bytes.length);
            write(bytes);
        }
    }
}
