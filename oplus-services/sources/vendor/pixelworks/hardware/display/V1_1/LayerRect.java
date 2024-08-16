package vendor.pixelworks.hardware.display.V1_1;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class LayerRect {
    public float left = 0.0f;
    public float top = 0.0f;
    public float right = 0.0f;
    public float bottom = 0.0f;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != LayerRect.class) {
            return false;
        }
        LayerRect layerRect = (LayerRect) obj;
        return this.left == layerRect.left && this.top == layerRect.top && this.right == layerRect.right && this.bottom == layerRect.bottom;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Float.valueOf(this.left))), Integer.valueOf(HidlSupport.deepHashCode(Float.valueOf(this.top))), Integer.valueOf(HidlSupport.deepHashCode(Float.valueOf(this.right))), Integer.valueOf(HidlSupport.deepHashCode(Float.valueOf(this.bottom))));
    }

    public final String toString() {
        return "{.left = " + this.left + ", .top = " + this.top + ", .right = " + this.right + ", .bottom = " + this.bottom + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(16L), 0L);
    }

    public static final ArrayList<LayerRect> readVectorFromParcel(HwParcel hwParcel) {
        ArrayList<LayerRect> arrayList = new ArrayList<>();
        HwBlob readBuffer = hwParcel.readBuffer(16L);
        int int32 = readBuffer.getInt32(8L);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 16, readBuffer.handle(), 0L, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            LayerRect layerRect = new LayerRect();
            layerRect.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * 16);
            arrayList.add(layerRect);
        }
        return arrayList;
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.left = hwBlob.getFloat(0 + j);
        this.top = hwBlob.getFloat(4 + j);
        this.right = hwBlob.getFloat(8 + j);
        this.bottom = hwBlob.getFloat(j + 12);
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(16);
        writeEmbeddedToBlob(hwBlob, 0L);
        hwParcel.writeBuffer(hwBlob);
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList<LayerRect> arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8L, size);
        hwBlob.putBool(12L, false);
        HwBlob hwBlob2 = new HwBlob(size * 16);
        for (int i = 0; i < size; i++) {
            arrayList.get(i).writeEmbeddedToBlob(hwBlob2, i * 16);
        }
        hwBlob.putBlob(0L, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        hwBlob.putFloat(0 + j, this.left);
        hwBlob.putFloat(4 + j, this.top);
        hwBlob.putFloat(8 + j, this.right);
        hwBlob.putFloat(j + 12, this.bottom);
    }
}
