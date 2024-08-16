package android.hardware.boot.V1_1;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class MergeStatus {
    public static final int CANCELLED = 4;
    public static final int MERGING = 3;
    public static final int NONE = 0;
    public static final int SNAPSHOTTED = 2;
    public static final int UNKNOWN = 1;

    public static final String toString(int i) {
        if (i == 0) {
            return "NONE";
        }
        if (i == 1) {
            return "UNKNOWN";
        }
        if (i == 2) {
            return "SNAPSHOTTED";
        }
        if (i == 3) {
            return "MERGING";
        }
        if (i == 4) {
            return "CANCELLED";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("NONE");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("UNKNOWN");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("SNAPSHOTTED");
            i2 |= 2;
        }
        if ((i & 3) == 3) {
            arrayList.add("MERGING");
            i2 |= 3;
        }
        if ((i & 4) == 4) {
            arrayList.add("CANCELLED");
            i2 |= 4;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
