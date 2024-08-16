package co.nstant.in.cbor.encoder;

import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.MajorType;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ArrayEncoder extends AbstractEncoder<Array> {
    public ArrayEncoder(CborEncoder cborEncoder, OutputStream outputStream) {
        super(cborEncoder, outputStream);
    }

    @Override // co.nstant.in.cbor.encoder.AbstractEncoder
    public void encode(Array array) throws CborException {
        List<DataItem> dataItems = array.getDataItems();
        if (array.isChunked()) {
            encodeTypeChunked(MajorType.ARRAY);
        } else {
            encodeTypeAndLength(MajorType.ARRAY, dataItems.size());
        }
        Iterator<DataItem> it = dataItems.iterator();
        while (it.hasNext()) {
            this.encoder.encode(it.next());
        }
    }
}
