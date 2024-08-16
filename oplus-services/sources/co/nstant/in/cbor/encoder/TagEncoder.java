package co.nstant.in.cbor.encoder;

import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.MajorType;
import co.nstant.in.cbor.model.Tag;
import java.io.OutputStream;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class TagEncoder extends AbstractEncoder<Tag> {
    public TagEncoder(CborEncoder cborEncoder, OutputStream outputStream) {
        super(cborEncoder, outputStream);
    }

    @Override // co.nstant.in.cbor.encoder.AbstractEncoder
    public void encode(Tag tag) throws CborException {
        encodeTypeAndLength(MajorType.TAG, tag.getValue());
    }
}
