package android.hardware.configstore.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class OptionalUInt32 {
    public boolean specified = false;
    public int value = 0;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != OptionalUInt32.class) {
            return false;
        }
        OptionalUInt32 optionalUInt32 = (OptionalUInt32) obj;
        return this.specified == optionalUInt32.specified && this.value == optionalUInt32.value;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.specified))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.value))));
    }

    public final String toString() {
        return "{.specified = " + this.specified + ", .value = " + this.value + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(8L), 0L);
    }

    public static final ArrayList<OptionalUInt32> readVectorFromParcel(HwParcel hwParcel) {
        ArrayList<OptionalUInt32> arrayList = new ArrayList<>();
        HwBlob readBuffer = hwParcel.readBuffer(16L);
        int int32 = readBuffer.getInt32(8L);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 8, readBuffer.handle(), 0L, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            OptionalUInt32 optionalUInt32 = new OptionalUInt32();
            optionalUInt32.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * 8);
            arrayList.add(optionalUInt32);
        }
        return arrayList;
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.specified = hwBlob.getBool(0 + j);
        this.value = hwBlob.getInt32(j + 4);
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(8);
        writeEmbeddedToBlob(hwBlob, 0L);
        hwParcel.writeBuffer(hwBlob);
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList<OptionalUInt32> arrayList) {
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
        hwBlob.putBool(0 + j, this.specified);
        hwBlob.putInt32(j + 4, this.value);
    }
}
