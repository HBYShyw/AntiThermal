package android.hardware.tv.cec.V1_0;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class SendMessageResult {
    public static final int BUSY = 2;
    public static final int FAIL = 3;
    public static final int NACK = 1;
    public static final int SUCCESS = 0;

    public static final String toString(int i) {
        if (i == 0) {
            return "SUCCESS";
        }
        if (i == 1) {
            return "NACK";
        }
        if (i == 2) {
            return "BUSY";
        }
        if (i == 3) {
            return "FAIL";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("SUCCESS");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("NACK");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("BUSY");
            i2 |= 2;
        }
        if ((i & 3) == 3) {
            arrayList.add("FAIL");
            i2 |= 3;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
