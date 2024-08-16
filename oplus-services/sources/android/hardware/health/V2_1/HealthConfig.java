package android.hardware.health.V2_1;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class HealthConfig {
    public android.hardware.health.V1_0.HealthConfig battery = new android.hardware.health.V1_0.HealthConfig();
    public int bootMinCap = 0;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != HealthConfig.class) {
            return false;
        }
        HealthConfig healthConfig = (HealthConfig) obj;
        return HidlSupport.deepEquals(this.battery, healthConfig.battery) && this.bootMinCap == healthConfig.bootMinCap;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(this.battery)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.bootMinCap))));
    }

    public final String toString() {
        return "{.battery = " + this.battery + ", .bootMinCap = " + this.bootMinCap + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(208L), 0L);
    }

    public static final ArrayList<HealthConfig> readVectorFromParcel(HwParcel hwParcel) {
        ArrayList<HealthConfig> arrayList = new ArrayList<>();
        HwBlob readBuffer = hwParcel.readBuffer(16L);
        int int32 = readBuffer.getInt32(8L);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * 208, readBuffer.handle(), 0L, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            HealthConfig healthConfig = new HealthConfig();
            healthConfig.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * 208);
            arrayList.add(healthConfig);
        }
        return arrayList;
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.battery.readEmbeddedFromParcel(hwParcel, hwBlob, 0 + j);
        this.bootMinCap = hwBlob.getInt32(j + 200);
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(208);
        writeEmbeddedToBlob(hwBlob, 0L);
        hwParcel.writeBuffer(hwBlob);
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList<HealthConfig> arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8L, size);
        hwBlob.putBool(12L, false);
        HwBlob hwBlob2 = new HwBlob(size * 208);
        for (int i = 0; i < size; i++) {
            arrayList.get(i).writeEmbeddedToBlob(hwBlob2, i * 208);
        }
        hwBlob.putBlob(0L, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        this.battery.writeEmbeddedToBlob(hwBlob, 0 + j);
        hwBlob.putInt32(j + 200, this.bootMinCap);
    }
}
