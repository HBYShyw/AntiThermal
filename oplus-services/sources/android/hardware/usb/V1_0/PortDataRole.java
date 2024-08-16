package android.hardware.usb.V1_0;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class PortDataRole {
    public static final int DEVICE = 2;
    public static final int HOST = 1;
    public static final int NONE = 0;
    public static final int NUM_DATA_ROLES = 3;

    public static final String toString(int i) {
        if (i == 0) {
            return "NONE";
        }
        if (i == 1) {
            return "HOST";
        }
        if (i == 2) {
            return "DEVICE";
        }
        if (i == 3) {
            return "NUM_DATA_ROLES";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("NONE");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("HOST");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("DEVICE");
            i2 |= 2;
        }
        if ((i & 3) == 3) {
            arrayList.add("NUM_DATA_ROLES");
            i2 |= 3;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
