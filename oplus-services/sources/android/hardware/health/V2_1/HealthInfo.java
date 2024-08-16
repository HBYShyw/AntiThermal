package android.hardware.health.V2_1;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import com.android.internal.util.FrameworkStatsLog;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class HealthInfo {
    public android.hardware.health.V2_0.HealthInfo legacy = new android.hardware.health.V2_0.HealthInfo();
    public int batteryCapacityLevel = 0;
    public long batteryChargeTimeToFullNowSeconds = 0;
    public int batteryFullChargeDesignCapacityUah = 0;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != HealthInfo.class) {
            return false;
        }
        HealthInfo healthInfo = (HealthInfo) obj;
        return HidlSupport.deepEquals(this.legacy, healthInfo.legacy) && this.batteryCapacityLevel == healthInfo.batteryCapacityLevel && this.batteryChargeTimeToFullNowSeconds == healthInfo.batteryChargeTimeToFullNowSeconds && this.batteryFullChargeDesignCapacityUah == healthInfo.batteryFullChargeDesignCapacityUah;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(this.legacy)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.batteryCapacityLevel))), Integer.valueOf(HidlSupport.deepHashCode(Long.valueOf(this.batteryChargeTimeToFullNowSeconds))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.batteryFullChargeDesignCapacityUah))));
    }

    public final String toString() {
        return "{.legacy = " + this.legacy + ", .batteryCapacityLevel = " + BatteryCapacityLevel.toString(this.batteryCapacityLevel) + ", .batteryChargeTimeToFullNowSeconds = " + this.batteryChargeTimeToFullNowSeconds + ", .batteryFullChargeDesignCapacityUah = " + this.batteryFullChargeDesignCapacityUah + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(136L), 0L);
    }

    public static final ArrayList<HealthInfo> readVectorFromParcel(HwParcel hwParcel) {
        ArrayList<HealthInfo> arrayList = new ArrayList<>();
        HwBlob readBuffer = hwParcel.readBuffer(16L);
        int int32 = readBuffer.getInt32(8L);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer(int32 * FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__SET_MANAGED_PROFILE_MAXIMUM_TIME_OFF, readBuffer.handle(), 0L, true);
        arrayList.clear();
        for (int i = 0; i < int32; i++) {
            HealthInfo healthInfo = new HealthInfo();
            healthInfo.readEmbeddedFromParcel(hwParcel, readEmbeddedBuffer, i * FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__SET_MANAGED_PROFILE_MAXIMUM_TIME_OFF);
            arrayList.add(healthInfo);
        }
        return arrayList;
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.legacy.readEmbeddedFromParcel(hwParcel, hwBlob, 0 + j);
        this.batteryCapacityLevel = hwBlob.getInt32(112 + j);
        this.batteryChargeTimeToFullNowSeconds = hwBlob.getInt64(120 + j);
        this.batteryFullChargeDesignCapacityUah = hwBlob.getInt32(j + 128);
    }

    public final void writeToParcel(HwParcel hwParcel) {
        HwBlob hwBlob = new HwBlob(FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__SET_MANAGED_PROFILE_MAXIMUM_TIME_OFF);
        writeEmbeddedToBlob(hwBlob, 0L);
        hwParcel.writeBuffer(hwBlob);
    }

    public static final void writeVectorToParcel(HwParcel hwParcel, ArrayList<HealthInfo> arrayList) {
        HwBlob hwBlob = new HwBlob(16);
        int size = arrayList.size();
        hwBlob.putInt32(8L, size);
        hwBlob.putBool(12L, false);
        HwBlob hwBlob2 = new HwBlob(size * FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__SET_MANAGED_PROFILE_MAXIMUM_TIME_OFF);
        for (int i = 0; i < size; i++) {
            arrayList.get(i).writeEmbeddedToBlob(hwBlob2, i * FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__SET_MANAGED_PROFILE_MAXIMUM_TIME_OFF);
        }
        hwBlob.putBlob(0L, hwBlob2);
        hwParcel.writeBuffer(hwBlob);
    }

    public final void writeEmbeddedToBlob(HwBlob hwBlob, long j) {
        this.legacy.writeEmbeddedToBlob(hwBlob, 0 + j);
        hwBlob.putInt32(112 + j, this.batteryCapacityLevel);
        hwBlob.putInt64(120 + j, this.batteryChargeTimeToFullNowSeconds);
        hwBlob.putInt32(j + 128, this.batteryFullChargeDesignCapacityUah);
    }
}
