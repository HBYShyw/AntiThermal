package vendor.pixelworks.hardware.display.V1_0;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class ChipVersion {
    public static final int IRIS2 = 0;
    public static final int IRIS2_PLUS = 1;
    public static final int IRIS3_LITE = 2;

    public static final String toString(int i) {
        if (i == 0) {
            return "IRIS2";
        }
        if (i == 1) {
            return "IRIS2_PLUS";
        }
        if (i == 2) {
            return "IRIS3_LITE";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("IRIS2");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("IRIS2_PLUS");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("IRIS3_LITE");
            i2 |= 2;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
