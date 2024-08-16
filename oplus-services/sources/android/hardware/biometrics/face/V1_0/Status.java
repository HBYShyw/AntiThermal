package android.hardware.biometrics.face.V1_0;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class Status {
    public static final int ILLEGAL_ARGUMENT = 1;
    public static final int INTERNAL_ERROR = 3;
    public static final int NOT_ENROLLED = 4;
    public static final int OK = 0;
    public static final int OPERATION_NOT_SUPPORTED = 2;

    public static final String toString(int i) {
        if (i == 0) {
            return "OK";
        }
        if (i == 1) {
            return "ILLEGAL_ARGUMENT";
        }
        if (i == 2) {
            return "OPERATION_NOT_SUPPORTED";
        }
        if (i == 3) {
            return "INTERNAL_ERROR";
        }
        if (i == 4) {
            return "NOT_ENROLLED";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("OK");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("ILLEGAL_ARGUMENT");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("OPERATION_NOT_SUPPORTED");
            i2 |= 2;
        }
        if ((i & 3) == 3) {
            arrayList.add("INTERNAL_ERROR");
            i2 |= 3;
        }
        if ((i & 4) == 4) {
            arrayList.add("NOT_ENROLLED");
            i2 |= 4;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
