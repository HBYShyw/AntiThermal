package co.nstant.in.cbor.encoder;

import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.MajorType;
import co.nstant.in.cbor.model.Map;
import co.nstant.in.cbor.model.Special;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class MapEncoder extends AbstractEncoder<Map> {
    public MapEncoder(CborEncoder cborEncoder, OutputStream outputStream) {
        super(cborEncoder, outputStream);
    }

    @Override // co.nstant.in.cbor.encoder.AbstractEncoder
    public void encode(Map map) throws CborException {
        Collection<DataItem> keys = map.getKeys();
        if (map.isChunked()) {
            encodeTypeChunked(MajorType.MAP);
        } else {
            encodeTypeAndLength(MajorType.MAP, keys.size());
        }
        if (keys.isEmpty()) {
            return;
        }
        if (map.isChunked()) {
            for (DataItem dataItem : keys) {
                this.encoder.encode(dataItem);
                this.encoder.encode(map.get(dataItem));
            }
            this.encoder.encode(Special.BREAK);
            return;
        }
        TreeMap treeMap = new TreeMap(new Comparator<byte[]>() { // from class: co.nstant.in.cbor.encoder.MapEncoder.1
            @Override // java.util.Comparator
            public int compare(byte[] bArr, byte[] bArr2) {
                if (bArr.length < bArr2.length) {
                    return -1;
                }
                if (bArr.length > bArr2.length) {
                    return 1;
                }
                for (int i = 0; i < bArr.length; i++) {
                    byte b = bArr[i];
                    byte b2 = bArr2[i];
                    if (b < b2) {
                        return -1;
                    }
                    if (b > b2) {
                        return 1;
                    }
                }
                return 0;
            }
        });
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CborEncoder cborEncoder = new CborEncoder(byteArrayOutputStream);
        for (DataItem dataItem2 : keys) {
            cborEncoder.encode(dataItem2);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.reset();
            cborEncoder.encode(map.get(dataItem2));
            byte[] byteArray2 = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.reset();
            treeMap.put(byteArray, byteArray2);
        }
        for (Map.Entry entry : treeMap.entrySet()) {
            write((byte[]) entry.getKey());
            write((byte[]) entry.getValue());
        }
    }
}
