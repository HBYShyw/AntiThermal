package vendor.pixelworks.hardware.display.V1_1;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class LayerTransform {
    public float rotation = 0.0f;
    public boolean flipHorizontal = false;
    public boolean flipVertical = false;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != LayerTransform.class) {
            return false;
        }
        LayerTransform layerTransform = (LayerTransform) obj;
        return this.rotation == layerTransform.rotation && this.flipHorizontal == layerTransform.flipHorizontal && this.flipVertical == layerTransform.flipVertical;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Float.valueOf(this.rotation))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.flipHorizontal))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.flipVertical))));
    }

    public final String toString() {
        return "{.rotation = " + this.rotation + ", .flipHorizontal = " + this.flipHorizontal + ", .flipVertical = " + this.flipVertical + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(8L), 0L);
    }

    public static final ArrayList<LayerTransform> readVectorFromParcel(HwParcel hwParcel) {
        ArrayList<LayerTransform> arrayList = new ArrayList<>();
        HwBlob readBuffer = hwParcel.readBuffer(16L);
        int int32 = readBuffer.getInt32(8L);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 8, readBuffer.handle(), 0L, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            LayerTransform layerTransform = new LayerTransform();
            layerTransform.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * 8);
            arrayList.add(layerTransform);
        }
        return arrayList;
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.rotation = hwBlob.getFloat(0 + j);
        this.flipHorizontal = hwBlob.getBool(4 + j);
        this.flipVertical = hwBlob.getBool(j + 5);
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(8);
        writeEmbeddedToBlob(hwBlob, 0L);
        hwParcel.writeBuffer(hwBlob);
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList<LayerTransform> arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8L, size);
        hwBlob.putBool(12L, false);
        HwBlob hwBlob2 = new HwBlob(size * 8);
        for (int i = 0; i < size; i++) {
            arrayList.get(i).writeEmbeddedToBlob(hwBlob2, i * 8);
        }
        hwBlob.putBlob(0L, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        hwBlob.putFloat(0 + j, this.rotation);
        hwBlob.putBool(4 + j, this.flipHorizontal);
        hwBlob.putBool(j + 5, this.flipVertical);
    }
}
