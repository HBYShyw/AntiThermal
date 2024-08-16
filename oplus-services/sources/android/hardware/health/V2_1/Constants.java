package android.hardware.health.V2_1;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class Constants {
    public static final long BATTERY_CHARGE_TIME_TO_FULL_NOW_SECONDS_UNSUPPORTED = -1;

    public static final String toString(long j) {
        if (j == -1) {
            return "BATTERY_CHARGE_TIME_TO_FULL_NOW_SECONDS_UNSUPPORTED";
        }
        return "0x" + Long.toHexString(j);
    }

    public static final String dumpBitfield(long j) {
        ArrayList arrayList = new ArrayList();
        long j2 = -1;
        if ((j & (-1)) == -1) {
            arrayList.add("BATTERY_CHARGE_TIME_TO_FULL_NOW_SECONDS_UNSUPPORTED");
        } else {
            j2 = 0;
        }
        if (j != j2) {
            arrayList.add("0x" + Long.toHexString(j & (~j2)));
        }
        return String.join(" | ", arrayList);
    }
}
