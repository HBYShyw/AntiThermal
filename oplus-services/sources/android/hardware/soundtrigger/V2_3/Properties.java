package android.hardware.soundtrigger.V2_3;

import android.hardware.soundtrigger.V2_0.ISoundTriggerHw;
import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class Properties {
    public int audioCapabilities;
    public ISoundTriggerHw.Properties base = new ISoundTriggerHw.Properties();
    public String supportedModelArch = new String();

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != Properties.class) {
            return false;
        }
        Properties properties = (Properties) obj;
        return HidlSupport.deepEquals(this.base, properties.base) && HidlSupport.deepEquals(this.supportedModelArch, properties.supportedModelArch) && HidlSupport.deepEquals(Integer.valueOf(this.audioCapabilities), Integer.valueOf(properties.audioCapabilities));
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(this.base)), Integer.valueOf(HidlSupport.deepHashCode(this.supportedModelArch)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.audioCapabilities))));
    }

    public final String toString() {
        return "{.base = " + this.base + ", .supportedModelArch = " + this.supportedModelArch + ", .audioCapabilities = " + AudioCapabilities.dumpBitfield(this.audioCapabilities) + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(112L), 0L);
    }

    public static final ArrayList<Properties> readVectorFromParcel(HwParcel hwParcel) {
        ArrayList<Properties> arrayList = new ArrayList<>();
        HwBlob readBuffer = hwParcel.readBuffer(16L);
        int int32 = readBuffer.getInt32(8L);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 112, readBuffer.handle(), 0L, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            Properties properties = new Properties();
            properties.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * 112);
            arrayList.add(properties);
        }
        return arrayList;
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.base.readEmbeddedFromParcel(hwParcel, hwBlob, j + 0);
        long j2 = j + 88;
        this.supportedModelArch = hwBlob.getString(j2);
        hwParcel.readEmbeddedBuffer(r2.getBytes().length + 1, hwBlob.handle(), j2 + 0, false);
        this.audioCapabilities = hwBlob.getInt32(j + 104);
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(112);
        writeEmbeddedToBlob(hwBlob, 0L);
        hwParcel.writeBuffer(hwBlob);
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList<Properties> arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8L, size);
        hwBlob.putBool(12L, false);
        HwBlob hwBlob2 = new HwBlob(size * 112);
        for (int i = 0; i < size; i++) {
            arrayList.get(i).writeEmbeddedToBlob(hwBlob2, i * 112);
        }
        hwBlob.putBlob(0L, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        this.base.writeEmbeddedToBlob(hwBlob, 0 + j);
        hwBlob.putString(88 + j, this.supportedModelArch);
        hwBlob.putInt32(j + 104, this.audioCapabilities);
    }
}
