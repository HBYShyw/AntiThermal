package android.hidl.safe_union.V1_0;

import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class Monostate {
    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != Monostate.class) {
            return false;
        }
        return true;
    }

    public final int hashCode() {
        return Objects.hash(new Object[0]);
    }

    public final String toString() {
        return "{}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(1L), 0L);
    }

    public static final ArrayList<Monostate> readVectorFromParcel(HwParcel hwParcel) {
        ArrayList<Monostate> arrayList = new ArrayList<>();
        HwBlob readBuffer = hwParcel.readBuffer(16L);
        int int32 = readBuffer.getInt32(8L);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 1, readBuffer.handle(), 0L, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            Monostate monostate = new Monostate();
            monostate.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * 1);
            arrayList.add(monostate);
        }
        return arrayList;
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(1);
        writeEmbeddedToBlob(hwBlob, 0L);
        hwParcel.writeBuffer(hwBlob);
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList<Monostate> arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8L, size);
        hwBlob.putBool(12L, false);
        HwBlob hwBlob2 = new HwBlob(size * 1);
        for (int i = 0; i < size; i++) {
            arrayList.get(i).writeEmbeddedToBlob(hwBlob2, i * 1);
        }
        hwBlob.putBlob(0L, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }
}
