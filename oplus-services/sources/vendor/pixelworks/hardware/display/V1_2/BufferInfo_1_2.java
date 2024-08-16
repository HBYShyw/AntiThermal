package vendor.pixelworks.hardware.display.V1_2;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;
import vendor.pixelworks.hardware.display.V1_1.BufferInfo;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class BufferInfo_1_2 {
    public BufferInfo base = new BufferInfo();
    public int unalignedWidth = 0;
    public int unalignedHeight = 0;
    public ArrayList<Integer> reserved = new ArrayList<>();

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != BufferInfo_1_2.class) {
            return false;
        }
        BufferInfo_1_2 bufferInfo_1_2 = (BufferInfo_1_2) obj;
        return HidlSupport.deepEquals(this.base, bufferInfo_1_2.base) && this.unalignedWidth == bufferInfo_1_2.unalignedWidth && this.unalignedHeight == bufferInfo_1_2.unalignedHeight && HidlSupport.deepEquals(this.reserved, bufferInfo_1_2.reserved);
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(this.base)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.unalignedWidth))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.unalignedHeight))), Integer.valueOf(HidlSupport.deepHashCode(this.reserved)));
    }

    public final String toString() {
        return "{.base = " + this.base + ", .unalignedWidth = " + this.unalignedWidth + ", .unalignedHeight = " + this.unalignedHeight + ", .reserved = " + this.reserved + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(88L), 0L);
    }

    public static final ArrayList<BufferInfo_1_2> readVectorFromParcel(HwParcel hwParcel) {
        ArrayList<BufferInfo_1_2> arrayList = new ArrayList<>();
        HwBlob readBuffer = hwParcel.readBuffer(16L);
        int int32 = readBuffer.getInt32(8L);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 88, readBuffer.handle(), 0L, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            BufferInfo_1_2 bufferInfo_1_2 = new BufferInfo_1_2();
            bufferInfo_1_2.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * 88);
            arrayList.add(bufferInfo_1_2);
        }
        return arrayList;
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.base.readEmbeddedFromParcel(hwParcel, hwBlob, j + 0);
        this.unalignedWidth = hwBlob.getInt32(j + 64);
        this.unalignedHeight = hwBlob.getInt32(j + 68);
        long j2 = j + 72;
        int int32 = hwBlob.getInt32(8 + j2);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 4, hwBlob.handle(), j2 + 0, true);
        this.reserved.clear();
        for (int i = 0; i < int32; i++) {
            this.reserved.add(Integer.valueOf(readEmbeddedBuffer.getInt32(i * 4)));
        }
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(88);
        writeEmbeddedToBlob(hwBlob, 0L);
        hwParcel.writeBuffer(hwBlob);
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList<BufferInfo_1_2> arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8L, size);
        hwBlob.putBool(12L, false);
        HwBlob hwBlob2 = new HwBlob(size * 88);
        for (int i = 0; i < size; i++) {
            arrayList.get(i).writeEmbeddedToBlob(hwBlob2, i * 88);
        }
        hwBlob.putBlob(0L, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        this.base.writeEmbeddedToBlob(hwBlob, j + 0);
        hwBlob.putInt32(64 + j, this.unalignedWidth);
        hwBlob.putInt32(68 + j, this.unalignedHeight);
        int size = this.reserved.size();
        long j2 = j + 72;
        hwBlob.putInt32(8 + j2, size);
        hwBlob.putBool(12 + j2, false);
        HwBlob hwBlob2 = new HwBlob(size * 4);
        for (int i = 0; i < size; i++) {
            hwBlob2.putInt32(i * 4, this.reserved.get(i).intValue());
        }
        hwBlob.putBlob(j2 + 0, hwBlob2);
    }
}
