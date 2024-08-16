package vendor.pixelworks.hardware.display.V1_1;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class LutData {
    public short dim = 0;
    public short gridSize = 0;
    public ArrayList<Integer> lutEntries = new ArrayList<>();
    public boolean validLutEntries = false;
    public ArrayList<Short> gridEntries = new ArrayList<>();
    public boolean validGridEntries = false;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != LutData.class) {
            return false;
        }
        LutData lutData = (LutData) obj;
        return this.dim == lutData.dim && this.gridSize == lutData.gridSize && HidlSupport.deepEquals(this.lutEntries, lutData.lutEntries) && this.validLutEntries == lutData.validLutEntries && HidlSupport.deepEquals(this.gridEntries, lutData.gridEntries) && this.validGridEntries == lutData.validGridEntries;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Short.valueOf(this.dim))), Integer.valueOf(HidlSupport.deepHashCode(Short.valueOf(this.gridSize))), Integer.valueOf(HidlSupport.deepHashCode(this.lutEntries)), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.validLutEntries))), Integer.valueOf(HidlSupport.deepHashCode(this.gridEntries)), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.validGridEntries))));
    }

    public final String toString() {
        return "{.dim = " + ((int) this.dim) + ", .gridSize = " + ((int) this.gridSize) + ", .lutEntries = " + this.lutEntries + ", .validLutEntries = " + this.validLutEntries + ", .gridEntries = " + this.gridEntries + ", .validGridEntries = " + this.validGridEntries + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(56L), 0L);
    }

    public static final ArrayList<LutData> readVectorFromParcel(HwParcel hwParcel) {
        ArrayList<LutData> arrayList = new ArrayList<>();
        HwBlob readBuffer = hwParcel.readBuffer(16L);
        int int32 = readBuffer.getInt32(8L);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 56, readBuffer.handle(), 0L, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            LutData lutData = new LutData();
            lutData.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * 56);
            arrayList.add(lutData);
        }
        return arrayList;
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.dim = hwBlob.getInt16(j + 0);
        this.gridSize = hwBlob.getInt16(j + 2);
        long j2 = j + 8;
        int int32 = hwBlob.getInt32(j2 + 8);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 4, hwBlob.handle(), j2 + 0, true);
        this.lutEntries.clear();
        for (int i = 0; i < int32; i++) {
            this.lutEntries.add(Integer.valueOf(readEmbeddedBuffer.getInt32(i * 4)));
        }
        this.validLutEntries = hwBlob.getBool(j + 24);
        long j3 = j + 32;
        int int322 = hwBlob.getInt32(8 + j3);
        HwBlob readEmbeddedBuffer2 = hwParcel.readEmbeddedBuffer(int322 * 2, hwBlob.handle(), j3 + 0, true);
        this.gridEntries.clear();
        for (int i2 = 0; i2 < int322; i2++) {
            this.gridEntries.add(Short.valueOf(readEmbeddedBuffer2.getInt16(i2 * 2)));
        }
        this.validGridEntries = hwBlob.getBool(j + 48);
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(56);
        writeEmbeddedToBlob(hwBlob, 0L);
        hwParcel.writeBuffer(hwBlob);
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList<LutData> arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8L, size);
        hwBlob.putBool(12L, false);
        HwBlob hwBlob2 = new HwBlob(size * 56);
        for (int i = 0; i < size; i++) {
            arrayList.get(i).writeEmbeddedToBlob(hwBlob2, i * 56);
        }
        hwBlob.putBlob(0L, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        hwBlob.putInt16(j + 0, this.dim);
        hwBlob.putInt16(j + 2, this.gridSize);
        int size = this.lutEntries.size();
        long j2 = j + 8;
        hwBlob.putInt32(j2 + 8, size);
        hwBlob.putBool(j2 + 12, false);
        HwBlob hwBlob2 = new HwBlob(size * 4);
        for (int i = 0; i < size; i++) {
            hwBlob2.putInt32(i * 4, this.lutEntries.get(i).intValue());
        }
        hwBlob.putBlob(j2 + 0, hwBlob2);
        hwBlob.putBool(j + 24, this.validLutEntries);
        int size2 = this.gridEntries.size();
        long j3 = j + 32;
        hwBlob.putInt32(8 + j3, size2);
        hwBlob.putBool(12 + j3, false);
        HwBlob hwBlob3 = new HwBlob(size2 * 2);
        for (int i2 = 0; i2 < size2; i2++) {
            hwBlob3.putInt16(i2 * 2, this.gridEntries.get(i2).shortValue());
        }
        hwBlob.putBlob(j3 + 0, hwBlob3);
        hwBlob.putBool(j + 48, this.validGridEntries);
    }
}
