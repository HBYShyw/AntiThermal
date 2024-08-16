package vendor.pixelworks.hardware.display.V1_1;

import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class Layer {
    public LayerBuffer inputBuffer = new LayerBuffer();
    public int composition = 0;
    public LayerRect dstRect = new LayerRect();
    public LayerTransform transform = new LayerTransform();
    public byte planeAlpha = 0;
    public int layerFlags = 0;
    public ArrayList<Integer> reserved = new ArrayList<>();

    public final String toString() {
        return "{.inputBuffer = " + this.inputBuffer + ", .composition = " + this.composition + ", .dstRect = " + this.dstRect + ", .transform = " + this.transform + ", .planeAlpha = " + ((int) this.planeAlpha) + ", .layerFlags = " + this.layerFlags + ", .reserved = " + this.reserved + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(216L), 0L);
    }

    public static final ArrayList<Layer> readVectorFromParcel(HwParcel hwParcel) {
        ArrayList<Layer> arrayList = new ArrayList<>();
        HwBlob readBuffer = hwParcel.readBuffer(16L);
        int int32 = readBuffer.getInt32(8L);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 216, readBuffer.handle(), 0L, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            Layer layer = new Layer();
            layer.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * 216);
            arrayList.add(layer);
        }
        return arrayList;
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.inputBuffer.readEmbeddedFromParcel(hwParcel, hwBlob, j + 0);
        this.composition = hwBlob.getInt32(j + 160);
        this.dstRect.readEmbeddedFromParcel(hwParcel, hwBlob, j + 164);
        this.transform.readEmbeddedFromParcel(hwParcel, hwBlob, j + 180);
        this.planeAlpha = hwBlob.getInt8(j + 188);
        this.layerFlags = hwBlob.getInt32(j + 192);
        long j2 = j + 200;
        int int32 = hwBlob.getInt32(8 + j2);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 4, hwBlob.handle(), j2 + 0, true);
        this.reserved.clear();
        for (int i = 0; i < int32; i++) {
            this.reserved.add(Integer.valueOf(readEmbeddedBuffer.getInt32(i * 4)));
        }
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(216);
        writeEmbeddedToBlob(hwBlob, 0L);
        hwParcel.writeBuffer(hwBlob);
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList<Layer> arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8L, size);
        hwBlob.putBool(12L, false);
        HwBlob hwBlob2 = new HwBlob(size * 216);
        for (int i = 0; i < size; i++) {
            arrayList.get(i).writeEmbeddedToBlob(hwBlob2, i * 216);
        }
        hwBlob.putBlob(0L, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        this.inputBuffer.writeEmbeddedToBlob(hwBlob, j + 0);
        hwBlob.putInt32(160 + j, this.composition);
        this.dstRect.writeEmbeddedToBlob(hwBlob, 164 + j);
        this.transform.writeEmbeddedToBlob(hwBlob, 180 + j);
        hwBlob.putInt8(188 + j, this.planeAlpha);
        hwBlob.putInt32(192 + j, this.layerFlags);
        int size = this.reserved.size();
        long j2 = j + 200;
        hwBlob.putInt32(8 + j2, size);
        hwBlob.putBool(12 + j2, false);
        HwBlob hwBlob2 = new HwBlob(size * 4);
        for (int i = 0; i < size; i++) {
            hwBlob2.putInt32(i * 4, this.reserved.get(i).intValue());
        }
        hwBlob.putBlob(j2 + 0, hwBlob2);
    }
}
