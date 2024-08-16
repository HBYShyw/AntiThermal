package vendor.pixelworks.hardware.display.V1_1;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class DualChannelOsdStatus {
    public static final int GET_STATE = 0;
    public static final int IS_OVERFLOW = 1;

    public static final String toString(int i) {
        if (i == 0) {
            return "GET_STATE";
        }
        if (i == 1) {
            return "IS_OVERFLOW";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("GET_STATE");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("IS_OVERFLOW");
        } else {
            i2 = 0;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
