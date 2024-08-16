package vendor.pixelworks.hardware.display.V1_1;

import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class LayerStack {
    public ArrayList<Layer> layers = new ArrayList<>();
    public int layerStackFlags = 0;
    public ArrayList<Integer> reserved = new ArrayList<>();

    public final String toString() {
        return "{.layers = " + this.layers + ", .layerStackFlags = " + this.layerStackFlags + ", .reserved = " + this.reserved + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(40L), 0L);
    }

    public static final ArrayList<LayerStack> readVectorFromParcel(HwParcel hwParcel) {
        ArrayList<LayerStack> arrayList = new ArrayList<>();
        HwBlob readBuffer = hwParcel.readBuffer(16L);
        int int32 = readBuffer.getInt32(8L);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 40, readBuffer.handle(), 0L, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            LayerStack layerStack = new LayerStack();
            layerStack.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * 40);
            arrayList.add(layerStack);
        }
        return arrayList;
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        long j2 = j + 0;
        int int32 = hwBlob.getInt32(j2 + 8);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 216, hwBlob.handle(), j2 + 0, true);
        this.layers.clear();
        for (int i = 0; i < int32; i++) {
            Layer layer = new Layer();
            layer.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * 216);
            this.layers.add(layer);
        }
        this.layerStackFlags = hwBlob.getInt32(j + 16);
        long j3 = j + 24;
        int int322 = hwBlob.getInt32(8 + j3);
        HwBlob readEmbeddedBuffer2 = hwParcel.readEmbeddedBuffer(int322 * 4, hwBlob.handle(), j3 + 0, true);
        this.reserved.clear();
        for (int i2 = 0; i2 < int322; i2++) {
            this.reserved.add(Integer.valueOf(readEmbeddedBuffer2.getInt32(i2 * 4)));
        }
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(40);
        writeEmbeddedToBlob(hwBlob, 0L);
        hwParcel.writeBuffer(hwBlob);
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList<LayerStack> arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8L, size);
        hwBlob.putBool(12L, false);
        HwBlob hwBlob2 = new HwBlob(size * 40);
        for (int i = 0; i < size; i++) {
            arrayList.get(i).writeEmbeddedToBlob(hwBlob2, i * 40);
        }
        hwBlob.putBlob(0L, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        int size = this.layers.size();
        long j2 = j + 0;
        hwBlob.putInt32(j2 + 8, size);
        hwBlob.putBool(j2 + 12, false);
        HwBlob hwBlob2 = new HwBlob(size * 216);
        for (int i = 0; i < size; i++) {
            this.layers.get(i).writeEmbeddedToBlob(hwBlob2, i * 216);
        }
        hwBlob.putBlob(j2 + 0, hwBlob2);
        hwBlob.putInt32(j + 16, this.layerStackFlags);
        int size2 = this.reserved.size();
        long j3 = j + 24;
        hwBlob.putInt32(8 + j3, size2);
        hwBlob.putBool(j3 + 12, false);
        HwBlob hwBlob3 = new HwBlob(size2 * 4);
        for (int i2 = 0; i2 < size2; i2++) {
            hwBlob3.putInt32(i2 * 4, this.reserved.get(i2).intValue());
        }
        hwBlob.putBlob(j3 + 0, hwBlob3);
    }
}
