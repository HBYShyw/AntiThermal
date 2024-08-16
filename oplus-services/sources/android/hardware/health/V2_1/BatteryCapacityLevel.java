package android.hardware.health.V2_1;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class BatteryCapacityLevel {
    public static final int CRITICAL = 1;
    public static final int FULL = 5;
    public static final int HIGH = 4;
    public static final int LOW = 2;
    public static final int NORMAL = 3;
    public static final int UNKNOWN = 0;
    public static final int UNSUPPORTED = -1;

    public static final String toString(int i) {
        if (i == -1) {
            return "UNSUPPORTED";
        }
        if (i == 0) {
            return "UNKNOWN";
        }
        if (i == 1) {
            return "CRITICAL";
        }
        if (i == 2) {
            return "LOW";
        }
        if (i == 3) {
            return "NORMAL";
        }
        if (i == 4) {
            return "HIGH";
        }
        if (i == 5) {
            return "FULL";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        int i2 = -1;
        if ((i & (-1)) == -1) {
            arrayList.add("UNSUPPORTED");
        } else {
            i2 = 0;
        }
        arrayList.add("UNKNOWN");
        if ((i & 1) == 1) {
            arrayList.add("CRITICAL");
            i2 |= 1;
        }
        if ((i & 2) == 2) {
            arrayList.add("LOW");
            i2 |= 2;
        }
        if ((i & 3) == 3) {
            arrayList.add("NORMAL");
            i2 |= 3;
        }
        if ((i & 4) == 4) {
            arrayList.add("HIGH");
            i2 |= 4;
        }
        if ((i & 5) == 5) {
            arrayList.add("FULL");
            i2 |= 5;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
