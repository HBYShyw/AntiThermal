package android.hardware.boot.V1_0;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class BoolResult {
    public static final int FALSE = 0;
    public static final int INVALID_SLOT = -1;
    public static final int TRUE = 1;

    public static final String toString(int i) {
        if (i == 0) {
            return "FALSE";
        }
        if (i == 1) {
            return "TRUE";
        }
        if (i == -1) {
            return "INVALID_SLOT";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("FALSE");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("TRUE");
        } else {
            i2 = 0;
        }
        if ((i & (-1)) == -1) {
            arrayList.add("INVALID_SLOT");
            i2 |= -1;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
