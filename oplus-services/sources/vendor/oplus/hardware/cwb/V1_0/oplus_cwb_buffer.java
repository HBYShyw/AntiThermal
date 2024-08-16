package vendor.oplus.hardware.cwb.V1_0;

import android.os.HwBlob;
import android.os.HwParcel;
import android.os.NativeHandle;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class oplus_cwb_buffer {
    public NativeHandle bufferHandler = new NativeHandle();
    public int bufferSize = 0;

    public final String toString() {
        return "{.bufferHandler = " + this.bufferHandler + ", .bufferSize = " + this.bufferSize + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(24L), 0L);
    }

    public static final ArrayList<oplus_cwb_buffer> readVectorFromParcel(HwParcel hwParcel) {
        ArrayList<oplus_cwb_buffer> arrayList = new ArrayList<>();
        HwBlob readBuffer = hwParcel.readBuffer(16L);
        int int32 = readBuffer.getInt32(8L);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 24, readBuffer.handle(), 0L, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            oplus_cwb_buffer oplus_cwb_bufferVar = new oplus_cwb_buffer();
            oplus_cwb_bufferVar.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * 24);
            arrayList.add(oplus_cwb_bufferVar);
        }
        return arrayList;
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.bufferHandler = hwParcel.readEmbeddedNativeHandle(hwBlob.handle(), j + 0 + 0);
        this.bufferSize = hwBlob.getInt32(j + 16);
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(24);
        writeEmbeddedToBlob(hwBlob, 0L);
        hwParcel.writeBuffer(hwBlob);
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList<oplus_cwb_buffer> arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8L, size);
        hwBlob.putBool(12L, false);
        HwBlob hwBlob2 = new HwBlob(size * 24);
        for (int i = 0; i < size; i++) {
            arrayList.get(i).writeEmbeddedToBlob(hwBlob2, i * 24);
        }
        hwBlob.putBlob(0L, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        hwBlob.putNativeHandle(0 + j, this.bufferHandler);
        hwBlob.putInt32(j + 16, this.bufferSize);
    }
}
