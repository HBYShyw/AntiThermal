package android.hardware.usb.V1_2;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class ContaminantDetectionStatus {
    public static final int DETECTED = 3;
    public static final int DISABLED = 1;
    public static final int NOT_DETECTED = 2;
    public static final int NOT_SUPPORTED = 0;

    public static final String toString(int i) {
        if (i == 0) {
            return "NOT_SUPPORTED";
        }
        if (i == 1) {
            return "DISABLED";
        }
        if (i == 2) {
            return "NOT_DETECTED";
        }
        if (i == 3) {
            return "DETECTED";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("NOT_SUPPORTED");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("DISABLED");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("NOT_DETECTED");
            i2 |= 2;
        }
        if ((i & 3) == 3) {
            arrayList.add("DETECTED");
            i2 |= 3;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
