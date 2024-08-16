package android.hardware.biometrics.face.V1_0;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class FaceError {
    public static final int CANCELED = 5;
    public static final int HW_UNAVAILABLE = 1;
    public static final int LOCKOUT = 7;
    public static final int LOCKOUT_PERMANENT = 9;
    public static final int NO_SPACE = 4;
    public static final int TIMEOUT = 3;
    public static final int UNABLE_TO_PROCESS = 2;
    public static final int UNABLE_TO_REMOVE = 6;
    public static final int VENDOR = 8;

    public static final String toString(int i) {
        if (i == 1) {
            return "HW_UNAVAILABLE";
        }
        if (i == 2) {
            return "UNABLE_TO_PROCESS";
        }
        if (i == 3) {
            return "TIMEOUT";
        }
        if (i == 4) {
            return "NO_SPACE";
        }
        if (i == 5) {
            return "CANCELED";
        }
        if (i == 6) {
            return "UNABLE_TO_REMOVE";
        }
        if (i == 7) {
            return "LOCKOUT";
        }
        if (i == 8) {
            return "VENDOR";
        }
        if (i == 9) {
            return "LOCKOUT_PERMANENT";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("HW_UNAVAILABLE");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("UNABLE_TO_PROCESS");
            i2 |= 2;
        }
        if ((i & 3) == 3) {
            arrayList.add("TIMEOUT");
            i2 |= 3;
        }
        if ((i & 4) == 4) {
            arrayList.add("NO_SPACE");
            i2 |= 4;
        }
        if ((i & 5) == 5) {
            arrayList.add("CANCELED");
            i2 |= 5;
        }
        if ((i & 6) == 6) {
            arrayList.add("UNABLE_TO_REMOVE");
            i2 |= 6;
        }
        if ((i & 7) == 7) {
            arrayList.add("LOCKOUT");
            i2 |= 7;
        }
        if ((i & 8) == 8) {
            arrayList.add("VENDOR");
            i2 |= 8;
        }
        if ((i & 9) == 9) {
            arrayList.add("LOCKOUT_PERMANENT");
            i2 |= 9;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
