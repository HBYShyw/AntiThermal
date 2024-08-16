package android.hardware.tv.cec.V1_0;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class Result {
    public static final int FAILURE_BUSY = 5;
    public static final int FAILURE_INVALID_ARGS = 2;
    public static final int FAILURE_INVALID_STATE = 3;
    public static final int FAILURE_NOT_SUPPORTED = 4;
    public static final int FAILURE_UNKNOWN = 1;
    public static final int SUCCESS = 0;

    public static final String toString(int i) {
        if (i == 0) {
            return "SUCCESS";
        }
        if (i == 1) {
            return "FAILURE_UNKNOWN";
        }
        if (i == 2) {
            return "FAILURE_INVALID_ARGS";
        }
        if (i == 3) {
            return "FAILURE_INVALID_STATE";
        }
        if (i == 4) {
            return "FAILURE_NOT_SUPPORTED";
        }
        if (i == 5) {
            return "FAILURE_BUSY";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("SUCCESS");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("FAILURE_UNKNOWN");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("FAILURE_INVALID_ARGS");
            i2 |= 2;
        }
        if ((i & 3) == 3) {
            arrayList.add("FAILURE_INVALID_STATE");
            i2 |= 3;
        }
        if ((i & 4) == 4) {
            arrayList.add("FAILURE_NOT_SUPPORTED");
            i2 |= 4;
        }
        if ((i & 5) == 5) {
            arrayList.add("FAILURE_BUSY");
            i2 |= 5;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
