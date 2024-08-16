package vendor.pixelworks.hardware.display.V1_1;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class HwcRect {
    public int left = 0;
    public int top = 0;
    public int right = 0;
    public int bottom = 0;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != HwcRect.class) {
            return false;
        }
        HwcRect hwcRect = (HwcRect) obj;
        return this.left == hwcRect.left && this.top == hwcRect.top && this.right == hwcRect.right && this.bottom == hwcRect.bottom;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.left))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.top))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.right))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.bottom))));
    }

    public final String toString() {
        return "{.left = " + this.left + ", .top = " + this.top + ", .right = " + this.right + ", .bottom = " + this.bottom + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(16L), 0L);
    }

    public static final ArrayList<HwcRect> readVectorFromParcel(HwParcel hwParcel) {
        ArrayList<HwcRect> arrayList = new ArrayList<>();
        HwBlob readBuffer = hwParcel.readBuffer(16L);
        int int32 = readBuffer.getInt32(8L);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 16, readBuffer.handle(), 0L, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            HwcRect hwcRect = new HwcRect();
            hwcRect.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * 16);
            arrayList.add(hwcRect);
        }
        return arrayList;
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.left = hwBlob.getInt32(0 + j);
        this.top = hwBlob.getInt32(4 + j);
        this.right = hwBlob.getInt32(8 + j);
        this.bottom = hwBlob.getInt32(j + 12);
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(16);
        writeEmbeddedToBlob(hwBlob, 0L);
        hwParcel.writeBuffer(hwBlob);
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList<HwcRect> arrayList) {
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
        hwBlob.putInt32(0 + j, this.left);
        hwBlob.putInt32(4 + j, this.top);
        hwBlob.putInt32(8 + j, this.right);
        hwBlob.putInt32(j + 12, this.bottom);
    }
}
