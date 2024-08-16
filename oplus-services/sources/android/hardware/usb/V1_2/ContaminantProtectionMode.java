package android.hardware.usb.V1_2;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class ContaminantProtectionMode {
    public static final int FORCE_DISABLE = 4;
    public static final int FORCE_SINK = 1;
    public static final int FORCE_SOURCE = 2;
    public static final int NONE = 0;

    public static final String toString(int i) {
        if (i == 0) {
            return "NONE";
        }
        if (i == 1) {
            return "FORCE_SINK";
        }
        if (i == 2) {
            return "FORCE_SOURCE";
        }
        if (i == 4) {
            return "FORCE_DISABLE";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("NONE");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("FORCE_SINK");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("FORCE_SOURCE");
            i2 |= 2;
        }
        if ((i & 4) == 4) {
            arrayList.add("FORCE_DISABLE");
            i2 |= 4;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
