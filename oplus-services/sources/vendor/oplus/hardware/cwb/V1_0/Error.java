package vendor.oplus.hardware.cwb.V1_0;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class Error {
    public static final int INPUT_ERROR = 2;
    public static final int NONE = 0;
    public static final int RESOUCE_BUSY = 1;
    public static final int SCREEN_STAT_OFF = 3;

    public static final String toString(int i) {
        if (i == 0) {
            return "NONE";
        }
        if (i == 1) {
            return "RESOUCE_BUSY";
        }
        if (i == 2) {
            return "INPUT_ERROR";
        }
        if (i == 3) {
            return "SCREEN_STAT_OFF";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("NONE");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("RESOUCE_BUSY");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("INPUT_ERROR");
            i2 |= 2;
        }
        if ((i & 3) == 3) {
            arrayList.add("SCREEN_STAT_OFF");
            i2 |= 3;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
