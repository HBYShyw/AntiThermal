package vendor.pixelworks.hardware.display.V1_1;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class DisplayConfigVariableInfo {
    public boolean valid = false;
    public int xPixels = 0;
    public int yPixels = 0;
    public float xDpi = 0.0f;
    public float yDpi = 0.0f;
    public int fps = 0;
    public int vsyncPeriodNs = 0;
    public boolean isYuv = false;
    public boolean smartPanel = false;
    public ArrayList<Integer> reserved = new ArrayList<>();

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != DisplayConfigVariableInfo.class) {
            return false;
        }
        DisplayConfigVariableInfo displayConfigVariableInfo = (DisplayConfigVariableInfo) obj;
        return this.valid == displayConfigVariableInfo.valid && this.xPixels == displayConfigVariableInfo.xPixels && this.yPixels == displayConfigVariableInfo.yPixels && this.xDpi == displayConfigVariableInfo.xDpi && this.yDpi == displayConfigVariableInfo.yDpi && this.fps == displayConfigVariableInfo.fps && this.vsyncPeriodNs == displayConfigVariableInfo.vsyncPeriodNs && this.isYuv == displayConfigVariableInfo.isYuv && this.smartPanel == displayConfigVariableInfo.smartPanel && HidlSupport.deepEquals(this.reserved, displayConfigVariableInfo.reserved);
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.valid))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.xPixels))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.yPixels))), Integer.valueOf(HidlSupport.deepHashCode(Float.valueOf(this.xDpi))), Integer.valueOf(HidlSupport.deepHashCode(Float.valueOf(this.yDpi))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.fps))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.vsyncPeriodNs))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.isYuv))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.smartPanel))), Integer.valueOf(HidlSupport.deepHashCode(this.reserved)));
    }

    public final String toString() {
        return "{.valid = " + this.valid + ", .xPixels = " + this.xPixels + ", .yPixels = " + this.yPixels + ", .xDpi = " + this.xDpi + ", .yDpi = " + this.yDpi + ", .fps = " + this.fps + ", .vsyncPeriodNs = " + this.vsyncPeriodNs + ", .isYuv = " + this.isYuv + ", .smartPanel = " + this.smartPanel + ", .reserved = " + this.reserved + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(48L), 0L);
    }

    public static final ArrayList<DisplayConfigVariableInfo> readVectorFromParcel(HwParcel hwParcel) {
        ArrayList<DisplayConfigVariableInfo> arrayList = new ArrayList<>();
        HwBlob readBuffer = hwParcel.readBuffer(16L);
        int int32 = readBuffer.getInt32(8L);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 48, readBuffer.handle(), 0L, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            DisplayConfigVariableInfo displayConfigVariableInfo = new DisplayConfigVariableInfo();
            displayConfigVariableInfo.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * 48);
            arrayList.add(displayConfigVariableInfo);
        }
        return arrayList;
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.valid = hwBlob.getBool(j + 0);
        this.xPixels = hwBlob.getInt32(j + 4);
        this.yPixels = hwBlob.getInt32(j + 8);
        this.xDpi = hwBlob.getFloat(j + 12);
        this.yDpi = hwBlob.getFloat(j + 16);
        this.fps = hwBlob.getInt32(j + 20);
        this.vsyncPeriodNs = hwBlob.getInt32(j + 24);
        this.isYuv = hwBlob.getBool(j + 28);
        this.smartPanel = hwBlob.getBool(j + 29);
        long j2 = j + 32;
        int int32 = hwBlob.getInt32(8 + j2);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 4, hwBlob.handle(), j2 + 0, true);
        this.reserved.clear();
        for (int i = 0; i < int32; i++) {
            this.reserved.add(Integer.valueOf(readEmbeddedBuffer.getInt32(i * 4)));
        }
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(48);
        writeEmbeddedToBlob(hwBlob, 0L);
        hwParcel.writeBuffer(hwBlob);
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList<DisplayConfigVariableInfo> arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8L, size);
        hwBlob.putBool(12L, false);
        HwBlob hwBlob2 = new HwBlob(size * 48);
        for (int i = 0; i < size; i++) {
            arrayList.get(i).writeEmbeddedToBlob(hwBlob2, i * 48);
        }
        hwBlob.putBlob(0L, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        hwBlob.putBool(j + 0, this.valid);
        hwBlob.putInt32(4 + j, this.xPixels);
        hwBlob.putInt32(j + 8, this.yPixels);
        hwBlob.putFloat(j + 12, this.xDpi);
        hwBlob.putFloat(16 + j, this.yDpi);
        hwBlob.putInt32(20 + j, this.fps);
        hwBlob.putInt32(24 + j, this.vsyncPeriodNs);
        hwBlob.putBool(28 + j, this.isYuv);
        hwBlob.putBool(29 + j, this.smartPanel);
        int size = this.reserved.size();
        long j2 = j + 32;
        hwBlob.putInt32(8 + j2, size);
        hwBlob.putBool(12 + j2, false);
        HwBlob hwBlob2 = new HwBlob(size * 4);
        for (int i = 0; i < size; i++) {
            hwBlob2.putInt32(i * 4, this.reserved.get(i).intValue());
        }
        hwBlob.putBlob(j2 + 0, hwBlob2);
    }
}
