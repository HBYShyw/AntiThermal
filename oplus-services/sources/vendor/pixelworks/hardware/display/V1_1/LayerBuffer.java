package vendor.pixelworks.hardware.display.V1_1;

import android.os.HwBlob;
import android.os.HwParcel;
import android.os.NativeHandle;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class LayerBuffer {
    public int width = 0;
    public int height = 0;
    public int unalignedWidth = 0;
    public int unalignedHeight = 0;
    public ColorMetaData colorMetadata = new ColorMetaData();
    public int acquireFenceFd = 0;
    public int releaseFenceFd = 0;
    public int flags = 0;
    public NativeHandle bufferHandle = new NativeHandle();
    public BufferInfo bufferInfo = new BufferInfo();

    public final String toString() {
        return "{.width = " + this.width + ", .height = " + this.height + ", .unalignedWidth = " + this.unalignedWidth + ", .unalignedHeight = " + this.unalignedHeight + ", .colorMetadata = " + this.colorMetadata + ", .acquireFenceFd = " + this.acquireFenceFd + ", .releaseFenceFd = " + this.releaseFenceFd + ", .flags = " + this.flags + ", .bufferHandle = " + this.bufferHandle + ", .bufferInfo = " + this.bufferInfo + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(160L), 0L);
    }

    public static final ArrayList<LayerBuffer> readVectorFromParcel(HwParcel hwParcel) {
        ArrayList<LayerBuffer> arrayList = new ArrayList<>();
        HwBlob readBuffer = hwParcel.readBuffer(16L);
        int int32 = readBuffer.getInt32(8L);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 160, readBuffer.handle(), 0L, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            LayerBuffer layerBuffer = new LayerBuffer();
            layerBuffer.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * 160);
            arrayList.add(layerBuffer);
        }
        return arrayList;
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.width = hwBlob.getInt32(j + 0);
        this.height = hwBlob.getInt32(4 + j);
        this.unalignedWidth = hwBlob.getInt32(8 + j);
        this.unalignedHeight = hwBlob.getInt32(12 + j);
        this.colorMetadata.readEmbeddedFromParcel(hwParcel, hwBlob, 16 + j);
        this.acquireFenceFd = hwBlob.getInt32(64 + j);
        this.releaseFenceFd = hwBlob.getInt32(68 + j);
        this.flags = hwBlob.getInt32(72 + j);
        this.bufferHandle = hwParcel.readEmbeddedNativeHandle(hwBlob.handle(), 80 + j + 0);
        this.bufferInfo.readEmbeddedFromParcel(hwParcel, hwBlob, j + 96);
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(160);
        writeEmbeddedToBlob(hwBlob, 0L);
        hwParcel.writeBuffer(hwBlob);
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList<LayerBuffer> arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8L, size);
        hwBlob.putBool(12L, false);
        HwBlob hwBlob2 = new HwBlob(size * 160);
        for (int i = 0; i < size; i++) {
            arrayList.get(i).writeEmbeddedToBlob(hwBlob2, i * 160);
        }
        hwBlob.putBlob(0L, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        hwBlob.putInt32(0 + j, this.width);
        hwBlob.putInt32(4 + j, this.height);
        hwBlob.putInt32(8 + j, this.unalignedWidth);
        hwBlob.putInt32(12 + j, this.unalignedHeight);
        this.colorMetadata.writeEmbeddedToBlob(hwBlob, 16 + j);
        hwBlob.putInt32(64 + j, this.acquireFenceFd);
        hwBlob.putInt32(68 + j, this.releaseFenceFd);
        hwBlob.putInt32(72 + j, this.flags);
        hwBlob.putNativeHandle(80 + j, this.bufferHandle);
        this.bufferInfo.writeEmbeddedToBlob(hwBlob, j + 96);
    }
}
