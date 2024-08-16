package vendor.pixelworks.hardware.display.V1_1;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class BufferInfo {
    public long id = 0;
    public int format = 0;
    public int type = 0;
    public int flags = 0;
    public int width = 0;
    public int height = 0;
    public float refreshRate = 0.0f;
    public int frcEnable = 0;
    public int frcCounter = 0;
    public long frcTimestamp = 0;
    public ArrayList<Integer> reserved = new ArrayList<>();

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != BufferInfo.class) {
            return false;
        }
        BufferInfo bufferInfo = (BufferInfo) obj;
        return this.id == bufferInfo.id && this.format == bufferInfo.format && this.type == bufferInfo.type && this.flags == bufferInfo.flags && this.width == bufferInfo.width && this.height == bufferInfo.height && this.refreshRate == bufferInfo.refreshRate && this.frcEnable == bufferInfo.frcEnable && this.frcCounter == bufferInfo.frcCounter && this.frcTimestamp == bufferInfo.frcTimestamp && HidlSupport.deepEquals(this.reserved, bufferInfo.reserved);
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Long.valueOf(this.id))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.format))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.type))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.flags))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.width))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.height))), Integer.valueOf(HidlSupport.deepHashCode(Float.valueOf(this.refreshRate))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.frcEnable))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.frcCounter))), Integer.valueOf(HidlSupport.deepHashCode(Long.valueOf(this.frcTimestamp))), Integer.valueOf(HidlSupport.deepHashCode(this.reserved)));
    }

    public final String toString() {
        return "{.id = " + this.id + ", .format = " + this.format + ", .type = " + this.type + ", .flags = " + this.flags + ", .width = " + this.width + ", .height = " + this.height + ", .refreshRate = " + this.refreshRate + ", .frcEnable = " + this.frcEnable + ", .frcCounter = " + this.frcCounter + ", .frcTimestamp = " + this.frcTimestamp + ", .reserved = " + this.reserved + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(64L), 0L);
    }

    public static final ArrayList<BufferInfo> readVectorFromParcel(HwParcel hwParcel) {
        ArrayList<BufferInfo> arrayList = new ArrayList<>();
        HwBlob readBuffer = hwParcel.readBuffer(16L);
        int int32 = readBuffer.getInt32(8L);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 64, readBuffer.handle(), 0L, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            BufferInfo bufferInfo = new BufferInfo();
            bufferInfo.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * 64);
            arrayList.add(bufferInfo);
        }
        return arrayList;
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.id = hwBlob.getInt64(j + 0);
        this.format = hwBlob.getInt32(j + 8);
        this.type = hwBlob.getInt32(j + 12);
        this.flags = hwBlob.getInt32(j + 16);
        this.width = hwBlob.getInt32(j + 20);
        this.height = hwBlob.getInt32(j + 24);
        this.refreshRate = hwBlob.getFloat(j + 28);
        this.frcEnable = hwBlob.getInt32(j + 32);
        this.frcCounter = hwBlob.getInt32(j + 36);
        this.frcTimestamp = hwBlob.getInt64(j + 40);
        long j2 = j + 48;
        int int32 = hwBlob.getInt32(8 + j2);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 4, hwBlob.handle(), j2 + 0, true);
        this.reserved.clear();
        for (int i = 0; i < int32; i++) {
            this.reserved.add(Integer.valueOf(readEmbeddedBuffer.getInt32(i * 4)));
        }
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(64);
        writeEmbeddedToBlob(hwBlob, 0L);
        hwParcel.writeBuffer(hwBlob);
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList<BufferInfo> arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8L, size);
        hwBlob.putBool(12L, false);
        HwBlob hwBlob2 = new HwBlob(size * 64);
        for (int i = 0; i < size; i++) {
            arrayList.get(i).writeEmbeddedToBlob(hwBlob2, i * 64);
        }
        hwBlob.putBlob(0L, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        hwBlob.putInt64(j + 0, this.id);
        hwBlob.putInt32(j + 8, this.format);
        hwBlob.putInt32(j + 12, this.type);
        hwBlob.putInt32(16 + j, this.flags);
        hwBlob.putInt32(20 + j, this.width);
        hwBlob.putInt32(24 + j, this.height);
        hwBlob.putFloat(28 + j, this.refreshRate);
        hwBlob.putInt32(32 + j, this.frcEnable);
        hwBlob.putInt32(36 + j, this.frcCounter);
        hwBlob.putInt64(40 + j, this.frcTimestamp);
        int size = this.reserved.size();
        long j2 = j + 48;
        hwBlob.putInt32(8 + j2, size);
        hwBlob.putBool(12 + j2, false);
        HwBlob hwBlob2 = new HwBlob(size * 4);
        for (int i = 0; i < size; i++) {
            hwBlob2.putInt32(i * 4, this.reserved.get(i).intValue());
        }
        hwBlob.putBlob(j2 + 0, hwBlob2);
    }
}
