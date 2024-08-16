package android.hardware.tv.cec.V1_0;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class OptionKey {
    public static final int ENABLE_CEC = 2;
    public static final int SYSTEM_CEC_CONTROL = 3;
    public static final int WAKEUP = 1;

    public static final String toString(int i) {
        if (i == 1) {
            return "WAKEUP";
        }
        if (i == 2) {
            return "ENABLE_CEC";
        }
        if (i == 3) {
            return "SYSTEM_CEC_CONTROL";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("WAKEUP");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("ENABLE_CEC");
            i2 |= 2;
        }
        if ((i & 3) == 3) {
            arrayList.add("SYSTEM_CEC_CONTROL");
            i2 |= 3;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
